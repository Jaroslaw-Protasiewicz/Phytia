package pl.phytia.prediction.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pl.phytia.ann.networks.Network;
import pl.phytia.model.sets.PatternPairVO;
import pl.phytia.utils.MathUtil;
import pl.phytia.utils.Normalizer;
import pl.phytia.ann.algorithms.Algorithm;
import pl.phytia.api.SignalAPI;
import pl.phytia.model.conf.pred.AnnModelConfiguration;
import pl.phytia.model.enums.EnumModelState;
import pl.phytia.model.enums.EnumSignalType;
import pl.phytia.model.sets.DoubleVector;
import pl.phytia.model.sets.SupervisedDataSet;
import pl.phytia.utils.Tools;

/**
 * Model z sieci neuronowej - perceptronowej.
 * 
 * @author Jarosław Protasiewicz
 */
public class MLPModel extends
		Model<AnnModelConfiguration, MLPModel, SupervisedDataSet> {

	/**
	 * Dzień, który reprezentuje model.
	 */
	private Date predictionDay;

	/**
	 * Sieć neuronowa.
	 */
	private Network network;

	/**
	 * Algorytm modelowania używany w drugie fazie treningu.
	 */
	private Algorithm secondPhaseAlgorithm;

	/**
	 * Wyjście modelu.
	 */
	private DoubleVector results;

	/**
	 * Bezargumentowy konstruktor.
	 */
	public MLPModel() {
		super();
	}

	/**
	 * Konstruktor.
	 * 
	 * @param config
	 *            Konfiguracja modelu.
	 * @param network
	 *            Sieć neuronowa użyta do modelowania.
	 * @param algorithm
	 *            Algorytm modelowania - pierwsza faza.
	 * @param secondPhaseAlgorithm
	 *            Algorytm modelowania - druga faza.
	 * @param modelSet
	 *            Zbiór danych do modelowania.
	 * @param predictSet
	 *            Zbiór danych do prognozy.
	 * @param normalizer
	 *            Normalizator danych.
	 * @param modelClass
	 *            Klasa modelu.
	 */
	public MLPModel(AnnModelConfiguration config, Network network,
					Algorithm algorithm, Algorithm secondPhaseAlgorithm,
					SupervisedDataSet modelSet, SupervisedDataSet predictSet,
					Normalizer normalizer, String modelClass) {
		super(config, algorithm, modelSet, predictSet, normalizer, modelClass);
		this.network = network;
		this.secondPhaseAlgorithm = secondPhaseAlgorithm;
	}

	public void initialize(AnnModelConfiguration conf) {
		this.config = conf;
		setResults(null);
		setResults(new DoubleVector(conf.getOutputPredictionSteps()));
		for (int i = 0; i < conf.getOutputPredictionSteps(); ++i) {
			getResults().add(new Double(0.0));
		}
	}

	@Override
	public void initialize(AnnModelConfiguration conf, Date predictionDay,
			Date predictionStartDay) {
		this.initialize(conf);
		this.predictionDay = predictionDay;

		/*
		 * Pobranie danych do modelowania z bazy.
		 */
		try {
			String signalName = null;
			if (conf.isWithNormalization()) {
				signalName = EnumSignalType.MODELLING_DATA.toString();
			} else {
				signalName = EnumSignalType.MODELLING_DATA_NORMALIZED
						.toString();
			}
			setModelSet(SignalAPI.prepareTrainSet(signalName,
					this.predictionDay, predictionStartDay, config
							.getInputWindowSizeFrom(), config
							.getInputWindowSizeTo(), config
							.getInputWindowYearsDelay(), config
							.getInputEnergyDelay(), config
							.getInputTemperatureDelay(), config
							.getInputHumidityDelay(), config
							.getInputInsolationDelay()));
			setPredictSet(SignalAPI.preparePredictionSet(signalName,
					this.predictionDay, config.getInputEnergyDelay(), config
							.getInputTemperatureDelay(), config
							.getInputHumidityDelay(), config
							.getInputInsolationDelay()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * Inicjalizacja normalizatora i normalizacja danych
		 */
		if (conf.isWithNormalization()) {
			if (getNormalizer() == null) {
				setNormalizer(new Normalizer());
			}
			getNormalizer().initialize(getModelSet().min(),
					getModelSet().max(),
					getNetwork().minReturnValue() + getConfig().getNormRatio(),
					getNetwork().maxReturnValue() - getConfig().getNormRatio());
			getModelSet().normalize(getNormalizer());
			getPredictSet().normalize(getNormalizer());
		}
	}

	/**
	 * Inicjalizacja modelu w opraci o konfigurację i listę godzin
	 * odpowiadjacych wyjściu sieci.
	 * 
	 * @param conf
	 *            Konfiguracja.
	 * @param predHour
	 *            Godzina wyjściowa
	 * @param hours
	 *            Lista godzin wejściowych
	 * @param whichSet :
	 *            1 = tylko model set, 2 = tylko predict set ; 3 = oba
	 */
	public void initialize(AnnModelConfiguration conf, Date predHour,
			List<Date> hours, int whichSet) {
		this.initialize(conf);
		this.predictionDay = predHour;
		/*
		 * Pobranie danych do modelowania z bazy.
		 */
		try {
			String signalName = null;
			if (conf.isWithNormalization()) {
				signalName = EnumSignalType.MODELLING_DATA.toString();
			} else {
				signalName = EnumSignalType.MODELLING_DATA_NORMALIZED
						.toString();
			}
			if (whichSet == 1 || whichSet == 3) {
				setModelSet(SignalAPI.prepareSetForTimePoints(signalName,
						hours, config.getInputEnergyDelay(), config
								.getInputTemperatureDelay(), config
								.getInputHumidityDelay(), config
								.getInputInsolationDelay(), config
								.isInputDayType(), config.isInputHoliday(),
						config.getInputEMean24Delay(), config
								.getInputEMean168Delay()));
			}
			if (predHour != null && (whichSet == 2 || whichSet == 3)) {
				List<Date> predHours = new ArrayList<Date>();
				predHours.add(predHour);
				setPredictSet(SignalAPI.prepareSetForTimePoints(signalName,
						predHours, config.getInputEnergyDelay(), config
								.getInputTemperatureDelay(), config
								.getInputHumidityDelay(), config
								.getInputInsolationDelay(), config
								.isInputDayType(), config.isInputHoliday(),
						config.getInputEMean24Delay(), config
								.getInputEMean168Delay()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * Inicjalizacja normalizatora i normalizacja danych
		 */
		if (conf.isWithNormalization()) {
			if (getNormalizer() == null) {
				setNormalizer(new Normalizer());
			}
			getNormalizer().initialize(getModelSet().min(),
					getModelSet().max(),
					getNetwork().minReturnValue() + getConfig().getNormRatio(),
					getNetwork().maxReturnValue() - getConfig().getNormRatio());
			getModelSet().normalize(getNormalizer());
			if (predHour != null) {
				getPredictSet().normalize(getNormalizer());
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void preparation() {
		try {
			/*
			 * Pobranie danych do modelowania z bazy.
			 */
			String signalName = null;
			if (getConfig().isWithNormalization()) {
				signalName = EnumSignalType.MODELLING_DATA.toString();
			} else {
				signalName = EnumSignalType.MODELLING_DATA_NORMALIZED
						.toString();
			}
			setModelSet(SignalAPI.prepareTrainForModelClass(signalName, config
					.getMaxTrainDataStart(), config.getMaxTrainDataStop(),
					getModelClass(), config.getInputEnergyDelay(), config
							.getInputTemperatureDelay(), config
							.getInputHumidityDelay(), config
							.getInputInsolationDelay()));
			/*
			 * Inicjalizacja normalizatora i normalizacja danych
			 */
			if (getConfig().isWithNormalization()) {
				getNormalizer().initialize(
						getModelSet().min(),
						getModelSet().max(),
						getNetwork().minReturnValue()
								+ getConfig().getNormRatio(),
						getNetwork().maxReturnValue()
								- getConfig().getNormRatio());
				getModelSet().normalize(getNormalizer());
				setModelState(EnumModelState.MODELING);
			}
			/*
			 * Wstępny trening modelu.
			 */
			getAlgorithm().training(getNetwork(), getModelSet());
			getSecondPhaseAlgorithm().training(getNetwork(), getModelSet());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void modeling() {
		getAlgorithm().training(getNetwork(), getModelSet());
		getSecondPhaseAlgorithm().training(getNetwork(), getModelSet());
	}

	@Override
	public void execution() {
		if (getPredictSet().getSet().size() != getConfig()
				.getOutputPredictionSteps()) {
			throw new IllegalStateException(
					"Niepoprawny rozmiar zbioru prognozy");
		}
		int indx = 0;
		double out = 0;
		setResults(new DoubleVector(getConfig().getOutputPredictionSteps(), 0.0));
		getResults().setNormalized(getPredictSet().isNormalized());
		DoubleVector inputs = null;
		// DoubleVector outputs = new
		// DoubleVector(getConfig().getOutputPredictionSteps(), 0.0);
		for (PatternPairVO pattern : getPredictSet().getSet()) {
			// outputs.set(indx, pattern.getOutputs().get(0));
			if (indx == 0) {
				inputs = pattern.getInputs();
			} else {
				inputs = Tools.shift(inputs, out, pattern.getInputs(),
						getConfig());
			}
			getNetwork().simulation(inputs);
			/*
			 * Zakłada się jedno wyjście modelu.
			 */
			out = getNetwork().getOutputs().get(0);
			getResults().set(indx, out);
			++indx;
		}
	}

	public void run() {
		synchronized (this) {
			switch (getModelState()) {
			case MODELING: {
				modeling();
				break;
			}
			case PREDICTION: {
				execution();
				break;
			}
			default: {
				throw new UnsupportedOperationException(
						"Nie zdefiniowana operacja w MLPModel.run() ");
			}
			}
		}
	}

	/**
	 * @return Błąd MAPE modelu.
	 */
	public double getMapeError() {
		double err = 0;
		for (int i = 0; i < getResults().size(); ++i) {
			err += MathUtil.ape(getResults().get(i), getPredictSet().getSet()
					.get(i).getOutputs().get(0));
		}
		return (err / getResults().size());
	}

	/* ############### GETTERS AND SETTERS #################### */
	/**
	 * @return wartość pola network
	 */
	public Network getNetwork() {
		return network;
	}

	/**
	 * @param network
	 *            jest przypisywany do pola network
	 */
	public void setNetwork(Network network) {
		this.network = network;
	}

	/**
	 * @return wartość pola predictionDay
	 */
	public Date getPredictionDay() {
		return predictionDay;
	}

	/**
	 * @param predictionDay
	 *            jest przypisywany do pola predictionDay
	 */
	public void setPredictionDay(Date predictionDay) {
		this.predictionDay = predictionDay;
	}

	/**
	 * @return wartość pola secondPhaseAlgorithm
	 */
	public Algorithm getSecondPhaseAlgorithm() {
		return secondPhaseAlgorithm;
	}

	/**
	 * @param secondPhaseAlgorithm
	 *            jest przypisywany do pola secondPhaseAlgorithm
	 */
	public void setSecondPhaseAlgorithm(Algorithm secondPhaseAlgorithm) {
		this.secondPhaseAlgorithm = secondPhaseAlgorithm;
	}

	/**
	 * @return wartość pola results
	 */
	public DoubleVector getResults() {
		return results;
	}

	/**
	 * @param results
	 *            jest przypisywany do pola results
	 */
	protected void setResults(DoubleVector results) {
		this.results = results;
	}

	/* ############### PRIVATE METHODS #################### */

}
