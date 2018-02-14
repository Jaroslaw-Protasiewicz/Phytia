package pl.phytia.prediction.metaModels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pl.phytia.ann.networks.Network;
import pl.phytia.api.MetaModelAPI;
import pl.phytia.model.sets.DoubleVector;
import pl.phytia.model.sets.PatternPairVO;
import pl.phytia.prediction.models.MLPModel;
import pl.phytia.prediction.models.Model;
import pl.phytia.utils.Normalizer;
import pl.phytia.ann.algorithms.Algorithm;
import pl.phytia.model.conf.pred.AnnModelConfiguration;
import pl.phytia.model.db.ModelDbVO;
import pl.phytia.model.enums.EnumHolidayClass;
import pl.phytia.model.enums.EnumNetworkType;
import pl.phytia.model.enums.EnumWorkDaysClass;
import pl.phytia.model.sets.SupervisedDataSet;
import pl.phytia.utils.Localization;

/**
 * Abstrakcyjny Meta-Model szeregowy.
 * 
 * @author Jarosław Protasiewicz
 * @param <M>
 *            Model z siecią neuronową.
 */
public abstract class SerialMetaModel<M extends Model> extends MetaModel<M> {

	/**
	 * Konstruktor bezargumentowy.
	 */
	public SerialMetaModel() {
		super();

	}

	@Override
	public void prediction() {
		if (getConfig() == null) {
			throw new IllegalArgumentException("Brak konfiguracji metamodelu");
		}
		Calendar mainCal = Calendar.getInstance(Localization.plLocale);
		mainCal.setTime(getConfig().getPredictionStart());
		do {
			/*
			 * Ustwaienie kalendarza na start prognozy.
			 */
			Calendar predCal = Calendar.getInstance(Localization.plLocale);
			predCal.setTime(mainCal.getTime());
			System.out.println("START PROGNOZY: " + mainCal.getTime());
			/*
			 * Odczyt modeli z bazy danych, inicjalizacja i trening. TODO:
			 * Wpisano na stałe sieć MLP : w przypadku realizacji RBF trzeba
			 * przenieść to do konfiguracji.
			 */
			List<ModelDbVO> dbModels = MetaModelAPI.readDbModels(getConfig()
					.getForcastType().toString(), getConfig()
					.getMetamodelType().toString(), EnumNetworkType.MLP
					.getType(), predCal.getTime(), getConfig().getDaysAhead());
			if (dbModels.size() != getConfig().getDaysAhead()) {
				throw new RuntimeException("Błąd odczytu modeli z bazy");
			}

			List<M> models = new ArrayList<M>();
			for (int i = 0; i < getConfig().getDaysAhead(); ++i) {
				ModelDbVO modelDb = dbModels.get(i);
				M model = preprareModel(modelDb);
				model.initialize(preparePredictorConfiguration(), predCal
						.getTime(), mainCal.getTime());
				model.modeling();
				models.add(model);
				System.out.println("PROGNOZA: " + predCal.getTime());
				predCal.add(Calendar.DAY_OF_YEAR, +1);
			}
			setPredictors(models);
			/*
			 * Wykonanie prognozy dla każdego z modeli.
			 */
			predCal.setTime(mainCal.getTime());
			DoubleVector inputs = null;
			for (int i = 0; i < getConfig().getDaysAhead(); ++i) {
				M model = getPredictors().get(i);
				if (i > 0) {
					shift(
							(PatternPairVO) model.getPredictSet().getSet().get(
									0), inputs, model.getConfig()
									.getInputEnergyDelay());
				}
				model.execution();
				inputs = ((MLPModel) model).getResults();
			}
			/*
			 * Denormailzacja danych
			 */
			boolean withNormalization = ((MLPModel) getPredictors().get(0))
					.getConfig().isWithNormalization();
			if (withNormalization) {
				for (M model : getPredictors()) {
					if (((MLPModel) model).getResults().isNormalized()) {
						((MLPModel) model).getResults().denormalize(
								model.getNormalizer());
					}
				}
			}
			/*
			 * Zapis do bazy i Zwiększenie głównego licznika dni.
			 */
			MetaModelAPI.saveSerialMetaModel(this, true, true);
			mainCal.add(Calendar.DAY_OF_YEAR, 1);
		} while (mainCal.getTime().before(getConfig().getPredictionStop()));
	}

	@Override
	public void preparation() {
		AnnModelConfiguration modelConf = preparePredictorConfiguration();
		Network net = prepareNetwork(modelConf);
		Algorithm algorithmFirstPhase = prepareFirstPhaseTrainAlgorithm();
		Algorithm algorithmSecondPhase = prepareSecondPhaseTrainAlgorithm();
		/*
		 * Utworzenie modeli klass dni roboczych.
		 */
		for (EnumWorkDaysClass e : EnumWorkDaysClass.values()) {
			getPredictors().add(
					preprareMLPModel(modelConf, net, algorithmFirstPhase,
							algorithmSecondPhase, null, null, new Normalizer(),
							e.toString()));
		}
		/*
		 * Utworzenie odpowiednich modeli klas dni świątecznych
		 */
		for (EnumHolidayClass e : EnumHolidayClass.values()) {
			getPredictors().add(
					preprareMLPModel(modelConf, net, algorithmFirstPhase,
							algorithmSecondPhase, null, null, new Normalizer(),
							e.toString()));
		}
		/*
		 * Przygotowanie każdego z modeli.
		 */
		for (M model : getPredictors()) {
			model.preparation();
		}
		/*
		 * Zapis do bazy danych
		 */
		MetaModelAPI.saveSerialMetaModel(this, true, false);
	}

	protected abstract M preprareMLPModel(AnnModelConfiguration config,
			Network network, Algorithm algorithm,
			Algorithm secondPhaseAlgorithm, SupervisedDataSet modelSet,
			SupervisedDataSet predictSet, Normalizer normalizer,
			String modelClass);

	private void shift(PatternPairVO p, DoubleVector out, int eLag) {
		int indx = eLag;
		for (int i = 0; i < eLag; ++i) {
			p.getInputs().set(--indx, out.get(i));
		}
	}
}
