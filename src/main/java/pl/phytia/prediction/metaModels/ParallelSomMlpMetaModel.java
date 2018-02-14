package pl.phytia.prediction.metaModels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import pl.phytia.ann.networks.MLPNetwork;
import pl.phytia.ann.networks.Network;
import pl.phytia.api.MetaModelAPI;
import pl.phytia.model.conf.functions.neighbourhood.GaussianNeighbourhoodConfiguration;
import pl.phytia.model.conf.functions.neuron.FahlmanFunctionConfiguration;
import pl.phytia.model.conf.networks.MLPNetworkConfiguration;
import pl.phytia.model.enums.EnumDistanceType;
import pl.phytia.model.enums.EnumIterationType;
import pl.phytia.model.enums.EnumLayerType;
import pl.phytia.model.sets.PatternPairVO;
import pl.phytia.prediction.models.MLPModel;
import pl.phytia.ann.networks.SOMNetwork;
import pl.phytia.api.SignalAPI;
import pl.phytia.model.conf.functions.learnRate.ExponentialLearningRateConfiguration;
import pl.phytia.model.conf.functions.neuron.SomNeuronConfiguration;
import pl.phytia.model.conf.layers.MLPLayerConfiguration;
import pl.phytia.model.conf.layers.SOMLayerConfiguration;
import pl.phytia.model.conf.networks.SOMNetworkConfiguration;
import pl.phytia.model.conf.pred.AnnModelConfiguration;
import pl.phytia.model.db.ModelDbVO;
import pl.phytia.model.db.PredictionDbVO;
import pl.phytia.model.enums.EnumFunctionType;
import pl.phytia.model.enums.EnumLearningRatioType;
import pl.phytia.model.enums.EnumModelState;
import pl.phytia.model.enums.EnumNeighbourhoodType;
import pl.phytia.model.enums.EnumNetworkType;
import pl.phytia.model.enums.EnumSignalType;
import pl.phytia.model.sets.DoubleVector;
import pl.phytia.model.sets.SupervisedDataSet;
import pl.phytia.prediction.models.SOMModel;
import pl.phytia.utils.Localization;
import pl.phytia.utils.Tools;

/**
 * Abstrakcyjny, równoległy metamodel: klasyfikator SOM - predyktory MLP
 * 
 * @author Jarosław Protasiewicz
 */
public abstract class ParallelSomMlpMetaModel extends
		ParallelMetaModel<MLPModel> {

	private Logger logger = Logger.getLogger(ParallelSomMlpMetaModel.class);

	private final int SAVE_MODEL_CYCLE = 1;

	public ParallelSomMlpMetaModel() {
		super();

	}

	@Override
	public void prediction() {
		if (getConfig() == null) {
			throw new IllegalArgumentException("Brak konfiguracji metamodelu");
		}
		/*
		 * Odczyt klasyfikatora z bazy i utworzenie jeżlei nie został wcześniej
		 * utowrzony.
		 */
		if (getClassifier() == null) {
			ModelDbVO modelDb = MetaModelAPI.readModel(getConfig()
					.getForcastType().toString(), getConfig()
					.getMetamodelType().toString(), EnumNetworkType.SOM
					.getType(), getConfig().getForcastType().toString());
			setClassifier(prepareClassifier(modelDb));
		}
		AnnModelConfiguration conf = prepareClassifierConfiguration();
		conf.setLazySetLoading(false);
		getClassifier().initialize(conf, conf.getMaxTrainDataStart(),
				conf.getMaxTrainDataStop());
		Calendar mainCal = Calendar.getInstance(Localization.plLocale);
		mainCal.setTime(getConfig().getPredictionStart());
		PredictionDbVO prediction = null;
		int daysCycle = 0;
		do {
			/*
			 * Ustawienie kalendarza na start prognozy.
			 */
			Calendar predCal = Calendar.getInstance(Localization.plLocale);
			predCal.setTime(mainCal.getTime());
			logger.debug("START PROGNOZY: " + mainCal.getTime());
			/*
			 * Inteligenta inicjalizacja klasyfikatora Za pierwszym razem sa
			 * ładowane wszystkie dane a potem dane są doczytywane w miarę
			 * postepu prognozy.
			 */
			if (conf.isLazySetLoading()) {
				Calendar locCal = Calendar.getInstance(Localization.plLocale);
				locCal.setTime(mainCal.getTime());
				locCal.add(Calendar.DAY_OF_YEAR, -1);
				conf.setLazySetLoading(true);
				getClassifier().initialize(conf, locCal.getTime(),
						locCal.getTime());
			}
			conf.setLazySetLoading(true);
			/*
			 * Wykonanie klasyfikacji wszystkich danych.
			 */
			getClassifier().preparation();
			/*
			 * Wykonywane dla każdej godziny z dni wyprzedzenia.
			 */
			int hoursAhead = getConfig().getDaysAhead() * 24;
			// DoubleVector results = new DoubleVector(hoursAhead);
			PatternPairVO pattern = null;
			DoubleVector in = null;
			Double result = null;
			int daysAhead = 0;
			results = new ArrayList<PredictionDbVO>();
			for (int i = 0; i < hoursAhead + 1; ++i) {
				logger.debug("Porgnoza na : " + predCal.getTime());
				/*
				 * Pobierz wektor z bazy / przesuń wektory
				 */
				pattern = SignalAPI.prepareVectorForTimePoint(
						EnumSignalType.MODELLING_DATA_NORMALIZED.toString(),
						predCal.getTime(), getClassifier().getConfig()
								.getInputEnergyDelay(), getClassifier()
								.getConfig().getInputTemperatureDelay(),
						getClassifier().getConfig().getInputHumidityDelay(),
						getClassifier().getConfig().getInputInsolationDelay(),
						getClassifier().getConfig().isInputDayType(),
						getClassifier().getConfig().isInputHoliday(),
						getClassifier().getConfig().getInputEMean24Delay(),
						getClassifier().getConfig().getInputEMean168Delay());
				logger.debug("Wektor pobrany do z bazy= \n"
						+ pattern.getInputs().toString());
				if (i == 0) {
					in = pattern.getInputs().clone();
				} else {
					if (getClassifier().getConfig().getInputEMean24Delay() == 1
							|| getClassifier().getConfig()
									.getInputEMean168Delay() == 1) {
						double avg24 = 0;
						if (getClassifier().getConfig().getInputEMean24Delay() == 1) {
							avg24 = computeAvg(mainCal.getTime(), i, 24);
						}
						double avg168 = 0;
						if (getClassifier().getConfig().getInputEMean168Delay() == 1) {
							avg168 = computeAvg(mainCal.getTime(), i, 168);
						}
						if (getClassifier().getConfig().getInputEMean24Delay() == 1
								&& getClassifier().getConfig()
										.getInputEMean168Delay() != 1) {
							pattern.getInputs().set(
									pattern.getInputs().size() - 1, avg24);
						}
						if (getClassifier().getConfig().getInputEMean24Delay() != 1
								&& getClassifier().getConfig()
										.getInputEMean168Delay() == 1) {
							pattern.getInputs().set(
									pattern.getInputs().size() - 1, avg168);
						}
						if (getClassifier().getConfig().getInputEMean24Delay() == 1
								&& getClassifier().getConfig()
										.getInputEMean168Delay() == 1) {
							pattern.getInputs().set(
									pattern.getInputs().size() - 2, avg24);
							pattern.getInputs().set(
									pattern.getInputs().size() - 1, avg168);
						}
					}
					in = Tools.shift(in, result.doubleValue(), pattern
							.getInputs(), conf);
				}
				logger.debug("Wektor do po przesunięciu= \n" + in.toString());
				getClassifier().initialize(conf, in);
				/*
				 * Wykonaj klasyfikację
				 */
				getClassifier().execution();
				/*
				 * Utwórz model MLP
				 */
				MLPModel model = null;
				/*
				 * Jeżeli model jest w pamięci to użyj go. W przeciwnym wypadku
				 * poszukaj go w bazie danych. Jeżeli nie go w bazie danych to
				 * utwórz go.
				 */
				if (predModelMap.containsKey(getClassifier().getModelClass())) {
					try {
						model = new MLPModel();
						BeanUtils.copyProperties(model, predModelMap
								.get(getClassifier().getModelClass()));
					} catch (Exception e) {
						e.printStackTrace();
					}

					model = predModelMap.get(getClassifier().getModelClass());
					List<Date> dates = getClassifier()
							.getDatesForCurrentClass();
					model.initialize(preparePredictorConfiguration(), predCal
							.getTime(), dates, 3);

					if (logger.isDebugEnabled()) {
						StringBuilder sb = new StringBuilder(
								"Daty zb uczącego: \n ");
						for (Date d : dates) {
							sb.append(d.toString() + "\n");
						}
						logger.debug("Przygotowałem model z predMLPMap "
								+ sb.toString());
					}
				} else {
					String kom = null;
					ModelDbVO modelDb = MetaModelAPI.readModel(getConfig()
							.getForcastType().toString(), getConfig()
							.getMetamodelType().toString(), EnumNetworkType.MLP
							.getType(), getClassifier().getModelClass());
					if (modelDb != null) {
						model = preprareModel(modelDb);
						kom = "Przygotowałem model z bazy danych";
					} else {
						model = prepareMLPPredictor(getClassifier()
								.getModelClass());
						kom = "Przygotowałem nowy model ";
					}

					/*
					 * Pobierz zbiór danych do treningu
					 */
					List<Date> dates = getClassifier()
							.getDatesForCurrentClass();
					model.initialize(preparePredictorConfiguration(), predCal
							.getTime(), dates, 3);

					if (logger.isDebugEnabled()) {
						StringBuilder sb = new StringBuilder(
								"\n Daty zb uczącego: \n");
						for (Date d : dates) {
							sb.append(d.toString() + "\n");
						}
						logger.debug(kom + sb.toString());
					}
					/*
					 * Doucz model.
					 */
					model.modeling();
					/*
					 * Zapamiętaj model w mapie aby ponownie go nie tworzyć dla
					 * prognozy w tym dniu i godzinie lub aby ukatulanić model w
					 * pamięci.
					 */
					predModelMap.put(getClassifier().getModelClass(), model);
				}
				/*
				 * Ustaw sygnał prognozy - wejście
				 */
				List<PatternPairVO> l = new ArrayList<PatternPairVO>();
				l.add(new PatternPairVO(in, null));
				model.setPredictSet(new SupervisedDataSet(l,
						EnumIterationType.SERIAL));

				/*
				 * Wykonaj prognozę.
				 */
				model.execution();
				prediction = new PredictionDbVO();
				if (i % 24 == 0)
					daysAhead++;
				result = model.getResults().get(0);
				prediction.setDayAhead(daysAhead);
				prediction.setPredDay(predCal.getTime());
				prediction.setPredHour(predCal.get(Calendar.HOUR_OF_DAY) + 1);
				prediction.setPredictionId(null);
				prediction.setPredSeason(0);
				prediction.setPredTime(predCal.getTime());
				prediction.setPredValue(result);
				prediction.setSignalNameId(null);
				this.results.add(prediction);
				logger.debug("Przygotowanie do zapisu:\n"
						+ prediction.toString());
				/*
				 * Przesuń godzinę
				 */
				predCal.add(Calendar.HOUR, +1);

			}
			/*
			 * Denormailzacja danych
			 */
			if (conf.isWithNormalization()) {
				for (MLPModel model : getPredictors()) {
					if (((MLPModel) model).getResults().isNormalized()) {
						((MLPModel) model).getResults().denormalize(
								model.getNormalizer());
					}
				}
			}
			/*
			 * Zapisz predyktory i prognozy do bazy danych. Co SAVE_MODEL_CYCLE
			 * cykli zapisuj modele.
			 */
			if (++daysCycle >= SAVE_MODEL_CYCLE) {
				daysCycle = 0;
				MetaModelAPI.saveParallelMetaModel(this, false, 1, true);
			} else {
				MetaModelAPI.saveParallelMetaModel(this, false, 0, true);
			}
			mainCal.add(Calendar.DAY_OF_YEAR, 1);
		} while (mainCal.getTime().before(getConfig().getPredictionStop()));
		/*
		 * Na koniec zapisz aktualny stan modeli.
		 */
		MetaModelAPI.saveParallelMetaModel(this, false, 1, false);
	}

	@Override
	protected void prepareClassifier(AnnModelConfiguration classifierConf) {
		/*
		 * Konfiguracja funkcji odległości.
		 */
		GaussianNeighbourhoodConfiguration nfc = new GaussianNeighbourhoodConfiguration();
		nfc.setNeighbourhoodFunctionType(EnumNeighbourhoodType.Gaussian);
		nfc.setNeighbourhoodInitSize(1);
		nfc.setNeighbourhoodRatio(1E-6);
		nfc.setStartEpochChangeNeighborhood(1);
		/*
		 * RectangularNeighbourhoodConfiguration nfc = new
		 * RectangularNeighbourhoodConfiguration();
		 * nfc.setNeighbourhoodFunctionType(EnumNeighbourhoodType.Rectangular);
		 * nfc.setNeighbourhoodInitSize(3); nfc.setNeighbourhoodStepChange(1);
		 * nfc.setStartEpochChangeNeighborhood(10);
		 */

		/*
		 * Konfiguracja funkcji współczynnika uczenia.
		 */
		ExponentialLearningRateConfiguration lrc = new ExponentialLearningRateConfiguration();
		lrc.setInitLearningRatio(0.5);
		lrc.setLearningRatioExpConst(1.E-4);
		lrc.setLearningRatioType(EnumLearningRatioType.EXPONENTIAL);
		lrc.setMaxLearningRatio(1);
		lrc.setMinLearnigRatio(0.001);
		lrc.setStartEpochChange(1);
		lrc.setStepEpochChange(1);
		/*
		 * LinearLearnigRateConfiguration lrc = new
		 * LinearLearnigRateConfiguration(); lrc.setInitLearningRatio(0.5);
		 * lrc.setLearningRatioChage(0.01);
		 * lrc.setLearningRatioType(EnumLearningRatioType.LINEAR);
		 * lrc.setMaxLearningRatio(1.1); lrc.setMinLearnigRatio(0.01);
		 * lrc.setStartEpochChange(1); lrc.setStepEpochChange(1);
		 */

		/*
		 * Konfiguracja neuronu - elementu matrycy SOM.
		 */
		SomNeuronConfiguration snc = new SomNeuronConfiguration();
		snc.setNumberOfInputs(classifierConf.getInputEnergyDelay()
				+ classifierConf.getInputHumidityDelay()
				+ classifierConf.getInputInsolationDelay()
				+ classifierConf.getInputTemperatureDelay()
				+ (classifierConf.isInputDayType() ? 1 : 0)
				+ (classifierConf.isInputHoliday() ? 1 : 0)
				+ classifierConf.getInputEMean24Delay()
				+ classifierConf.getInputEMean168Delay());
		snc.setTypeOfFunction(EnumFunctionType.Pipe);

		/*
		 * Konfuiguracja matrycy SOM.
		 */
		/*
		 * SOMLayerConfiguration slc = new SOMLayerConfiguration<GaussianNeighbourhoodConfiguration,
		 * ExponentialLearningRateConfiguration>( nfc, lrc,
		 * EnumDistanceType.Euclidean, snc, new int[] { 5, 5 });
		 */
		SOMLayerConfiguration slc = new SOMLayerConfiguration<GaussianNeighbourhoodConfiguration, ExponentialLearningRateConfiguration>(
				nfc, lrc, EnumDistanceType.Euclidean, snc,
				getClasifierStructure());
		slc.setTypeOfLayer(EnumLayerType.OUTPUT);
		List<SOMLayerConfiguration> liSlc = new ArrayList<SOMLayerConfiguration>();
		liSlc.add(slc);

		/*
		 * Konfiguracja sieci neuronowej.
		 */
		SOMNetworkConfiguration conf = new SOMNetworkConfiguration();
		conf.setLayersConf(liSlc);
		conf.setMaxNormValue(.9);
		conf.setMinNormValue(.1);
		conf.setTypeOfNetwork(EnumNetworkType.SOM);

		/*
		 * Utworzenie sieci.
		 */
		SOMNetwork somNet = new SOMNetwork(conf);

		/*
		 * Utworzenie i przypisanie modelu.
		 */
		setClassifier(new SOMModel(somNet, prepareClassifierAlgorithm(), null,
				null));
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Network prepareNetwork(AnnModelConfiguration conf) {
		int inputs = conf.getInputEnergyDelay() + conf.getInputHumidityDelay()
				+ conf.getInputInsolationDelay()
				+ conf.getInputTemperatureDelay()
				+ +(conf.isInputDayType() ? 1 : 0)
				+ (conf.isInputHoliday() ? 1 : 0) + conf.getInputEMean24Delay()
				+ conf.getInputEMean168Delay();
		int[] netArchConf = getPredictorStructure();
		/*
		 * Lista warstw sieci.
		 */
		List<MLPLayerConfiguration> cl = new ArrayList<MLPLayerConfiguration>();

		if (netArchConf.length == 1) {
			/*
			 * Konfiguracja warstwy wyjściowej sieci.
			 */
			MLPLayerConfiguration<FahlmanFunctionConfiguration> f1 = new MLPLayerConfiguration<FahlmanFunctionConfiguration>();
			f1.setNeuronFunctionConf(new FahlmanFunctionConfiguration());
			f1.setNumberOfNeurons(netArchConf[0]);
			f1.setNumberOfInputs(inputs);
			f1.setTypeOfLayer(EnumLayerType.INOUT);
			cl.add(f1);
		} else if (netArchConf.length == 2) {
			/*
			 * Konfiguracja 1 warstwy ukrytej sieci.
			 */
			MLPLayerConfiguration<FahlmanFunctionConfiguration> f1 = new MLPLayerConfiguration<FahlmanFunctionConfiguration>();
			f1.setNeuronFunctionConf(new FahlmanFunctionConfiguration());
			f1.setNumberOfNeurons(netArchConf[0]);
			f1.setNumberOfInputs(inputs);
			f1.setTypeOfLayer(EnumLayerType.INPUT);
			cl.add(f1);
			/*
			 * Konfiguracja warstwy wyjściowej.
			 */
			MLPLayerConfiguration<FahlmanFunctionConfiguration> f2 = new MLPLayerConfiguration<FahlmanFunctionConfiguration>();
			f2.setNeuronFunctionConf(new FahlmanFunctionConfiguration());
			f2.setNumberOfNeurons(netArchConf[1]);
			f2.setNumberOfInputs(netArchConf[0]);
			f2.setTypeOfLayer(EnumLayerType.OUTPUT);
			cl.add(f2);
		} else if (netArchConf.length == 3) {
			/*
			 * Konfiguracja 1 warstwy ukrytej sieci.
			 */
			MLPLayerConfiguration<FahlmanFunctionConfiguration> f1 = new MLPLayerConfiguration<FahlmanFunctionConfiguration>();
			f1.setNeuronFunctionConf(new FahlmanFunctionConfiguration());
			f1.setNumberOfNeurons(netArchConf[0]);
			f1.setNumberOfInputs(inputs);
			f1.setTypeOfLayer(EnumLayerType.INPUT);
			cl.add(f1);
			/*
			 * Konfiguracja 2 warstwy ukrytej sieci.
			 */
			MLPLayerConfiguration<FahlmanFunctionConfiguration> f2 = new MLPLayerConfiguration<FahlmanFunctionConfiguration>();
			f2.setNeuronFunctionConf(new FahlmanFunctionConfiguration());
			f2.setNumberOfNeurons(netArchConf[1]);
			f2.setNumberOfInputs(netArchConf[0]);
			f2.setTypeOfLayer(EnumLayerType.HIDDEN);
			cl.add(f2);
			/*
			 * Konfiguracja warstwy wyjściowej sieci.
			 */
			MLPLayerConfiguration<FahlmanFunctionConfiguration> f3 = new MLPLayerConfiguration<FahlmanFunctionConfiguration>();
			f3.setNeuronFunctionConf(new FahlmanFunctionConfiguration());
			f3.setNumberOfNeurons(netArchConf[2]);
			f3.setNumberOfInputs(netArchConf[1]);
			f3.setTypeOfLayer(EnumLayerType.OUTPUT);
			cl.add(f3);

		}
		/*
		 * Konfiguracja sieci neuronowej.
		 */
		MLPNetworkConfiguration netConf = new MLPNetworkConfiguration();
		netConf.setTypeOfNetwork(EnumNetworkType.MLP);
		netConf.setLayersConf(cl);
		/*
		 * Utworzenie sieci neuronowej.
		 */
		Network n = new MLPNetwork(netConf);
		n.initialize(netConf);
		return n;

	}

	protected MLPModel prepareMLPPredictor(String modelClass) {
		AnnModelConfiguration predConf = preparePredictorConfiguration();
		Network net = prepareNetwork(predConf);
		MLPModel model = new MLPModel();
		model.setAlgorithm(prepareFirstPhaseTrainAlgorithm());
		model.setSecondPhaseAlgorithm(prepareSecondPhaseTrainAlgorithm());
		model.setConfig(predConf);
		model.setModelClass(modelClass);
		model.setModelState(EnumModelState.MODELING);
		model.setNetwork(net);
		model.setNormalizer(null);
		model.setPredictionDay(null);
		model.setPredictSet(null);
		model.setModelSet(null);
		return model;
	}

	@Override
	protected void preparePredictors(AnnModelConfiguration predictorConf) {

		Network net = prepareNetwork(predictorConf);
		/*
		 * Utworzenie listy modeli MLP - predyktorów.
		 */
		List<MLPModel> predModels = new ArrayList<MLPModel>();
		Set<String> keySet = getClassifier().getClassResults().keySet();
		Iterator<String> it = keySet.iterator();
		String className = null;
		while (it.hasNext()) {
			className = it.next();
			MLPModel model = new MLPModel();
			model.setAlgorithm(prepareFirstPhaseTrainAlgorithm());
			model.setSecondPhaseAlgorithm(prepareSecondPhaseTrainAlgorithm());
			model.setConfig(predictorConf);
			model.setModelClass(className);
			model.setModelState(EnumModelState.MODELING);
			model.setNetwork(net);
			model.setNormalizer(null);
			model.setPredictionDay(null);
			model.setPredictSet(null);
			model.setModelSet(null);
			predModels.add(model);
		}
		setPredictors(predModels);
	}

	@Override
	protected MLPModel preprareModel(ModelDbVO modelDb) {
		return new MLPModel(prepareModelConfiguration(modelDb.getConf()),
				prepareMLPNetwork(modelDb.getNetwork()),
				prepareFirstPhaseTrainAlgorithm(),
				prepareSecondPhaseTrainAlgorithm(), null, null, null, modelDb
						.getModelClass());
	}

}
