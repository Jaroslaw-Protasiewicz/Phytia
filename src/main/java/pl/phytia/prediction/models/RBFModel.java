package pl.phytia.prediction.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pl.phytia.ann.algorithms.Algorithm;
import pl.phytia.ann.networks.RBFNetwork;
import pl.phytia.api.SignalAPI;
import pl.phytia.model.enums.EnumIterationType;
import pl.phytia.model.enums.EnumSignalType;
import pl.phytia.model.sets.DoubleVector;
import pl.phytia.model.sets.PatternPairVO;
import pl.phytia.utils.MyLogger;
import pl.phytia.utils.Normalizer;
import pl.phytia.utils.Tools;
import pl.phytia.model.conf.pred.AnnModelConfiguration;
import pl.phytia.model.sets.SupervisedDataSet;

/**
 * Model neuronowy o sieć Rbf.
 * 
 * @author Jarosław Protasiewicz
 */
public class RBFModel extends
		Model<AnnModelConfiguration, RBFModel, SupervisedDataSet> {

	/**
	 * Punkt w czasie który reprezentuje model.
	 */
	private Date predPoint;

	/**
	 * Sieć neuronowa - predyktor.
	 */
	private RBFNetwork network;

	/**
	 * Algorytm uruchamiany w drugiej fazie uczenia.
	 */
	private Algorithm secondPhaseAlgorithm;

	/**
	 * Wyjście modelu.
	 */
	private DoubleVector results;

	public RBFModel() {
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
	public RBFModel(AnnModelConfiguration config, RBFNetwork network,
					Algorithm algorithm, Algorithm secondPhaseAlgorithm,
					SupervisedDataSet modelSet, SupervisedDataSet predictSet,
					Normalizer normalizer, String modelClass) {
		super(config, algorithm, modelSet, predictSet, normalizer, modelClass);
		this.network = network;
		this.secondPhaseAlgorithm = secondPhaseAlgorithm;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void modeling() {
		getAlgorithm().training(getNetwork(), getModelSet());

		// getNetwork().reInitialize(((RbfOnlineAlgorithm)getSecondPhaseAlgorithm()).getCenterSelectFunction().centerSelect(getModelSet()));
		// getSecondPhaseAlgorithm().training(getNetwork(), getModelSet());

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

	public void executionXOR() {
		setResults(new DoubleVector());
		getResults().setNormalized(getPredictSet().isNormalized());
		for (PatternPairVO pattern : getPredictSet().getSet()) {
			getNetwork().simulation(pattern.getInputs());
			/*
			 * Zakłada się jedno wyjście modelu.
			 */
			double out = getNetwork().getOutputs().get(0);
			getResults().add(out);
			MyLogger.logger.info(pattern.getInputs().toString());
			MyLogger.logger.info("NEED = " + pattern.getOutputs().get(0)
					+ " OUT = " + out + "\n");
		}

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
		this.predPoint = predictionDay;

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
			setModelSet(SignalAPI.prepareTrainSet(signalName, this.predPoint,
					predictionStartDay, config.getInputWindowSizeFrom(), config
							.getInputWindowSizeTo(), config
							.getInputWindowYearsDelay(), config
							.getInputEnergyDelay(), config
							.getInputTemperatureDelay(), config
							.getInputHumidityDelay(), config
							.getInputInsolationDelay()));
			setPredictSet(SignalAPI.preparePredictionSet(signalName,
					this.predPoint, config.getInputEnergyDelay(), config
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

	public void initializeXor(AnnModelConfiguration conf) {
		this.initialize(conf);

		List<PatternPairVO> lst = new ArrayList<PatternPairVO>();

		DoubleVector x1 = new DoubleVector(2, 0.0);
		DoubleVector y1 = new DoubleVector(1, 0.0);
		x1.set(0, 0.0);
		x1.set(1, 0.0);
		y1.set(0, 0.0);
		lst.add(new PatternPairVO(x1, y1));

		DoubleVector x2 = new DoubleVector(2, 0.0);
		DoubleVector y2 = new DoubleVector(1, 0.0);
		x2.set(0, 1.0);
		x2.set(1, 0.0);
		y2.set(0, 1.0);
		lst.add(new PatternPairVO(x2, y2));

		DoubleVector x3 = new DoubleVector(2, 0.0);
		DoubleVector y3 = new DoubleVector(1, 0.0);
		x3.set(0, 0.0);
		x3.set(1, 1.0);
		y3.set(0, 1.0);
		lst.add(new PatternPairVO(x3, y3));

		DoubleVector x4 = new DoubleVector(2, 0.0);
		DoubleVector y4 = new DoubleVector(1, 0.0);
		x4.set(0, 1.0);
		x4.set(1, 1.0);
		y4.set(0, 0.0);
		lst.add(new PatternPairVO(x4, y4));

		setModelSet(new SupervisedDataSet(lst, EnumIterationType.SERIAL));
		setPredictSet(new SupervisedDataSet(lst, EnumIterationType.SERIAL));
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
		this.predPoint = predHour;
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

	@Override
	public void preparation() {
		throw new UnsupportedOperationException("Nie obsługiwana metoda.");
	}

	public void run() {
		throw new UnsupportedOperationException("Nie obsługiwana metoda.");
	}

	public Algorithm getSecondPhaseAlgorithm() {
		return secondPhaseAlgorithm;
	}

	public void setSecondPhaseAlgorithm(Algorithm secondPhaseAlgorithm) {
		this.secondPhaseAlgorithm = secondPhaseAlgorithm;
	}

	public RBFNetwork getNetwork() {
		return network;
	}

	public void setNetwork(RBFNetwork network) {
		this.network = network;
	}

	public Date getPredPoint() {
		return predPoint;
	}

	public void setPredPoint(Date predPiont) {
		this.predPoint = predPiont;
	}

	public DoubleVector getResults() {
		return results;
	}

	public void setResults(DoubleVector results) {
		this.results = results;
	}

}
