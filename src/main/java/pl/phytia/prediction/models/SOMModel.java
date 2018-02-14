package pl.phytia.prediction.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import pl.phytia.ann.algorithms.som.KohonenAlgorithm;
import pl.phytia.ann.layers.Som2DimLayer;
import pl.phytia.ann.layers.Som3DimLayer;
import pl.phytia.ann.networks.SOMNetwork;
import pl.phytia.ann.neurons.SomNeuron;
import pl.phytia.api.SignalAPI;
import pl.phytia.model.enums.EnumIterationType;
import pl.phytia.model.enums.EnumSignalType;
import pl.phytia.model.sets.ClassPatternVO;
import pl.phytia.model.sets.DoubleVector;
import pl.phytia.model.sets.UnsupervisedDataSet;
import pl.phytia.utils.Normalizer;
import pl.phytia.ann.layers.SOMLayer;
import pl.phytia.model.conf.pred.AnnModelConfiguration;

public class SOMModel extends
		Model<AnnModelConfiguration, SOMModel, UnsupervisedDataSet> {

	private Logger logger = Logger.getLogger(SOMModel.class);

	/**
	 * Sieć neuronowa - samoorganizująca się mapa cech.
	 */
	private SOMNetwork network;

	/**
	 * Wynik klasyfikacji - mapa zawierająca pary: kasa, lista godzin wyjścia
	 * predytora w danej klasie.
	 */
	Map<String, List<Date>> classResults;

	/**
	 * Konstruktor bezargumentowy.
	 */
	public SOMModel() {
		super();
	}

	/**
	 * Konstruktor wypełniający wymagane pola.
	 * 
	 * @param network
	 *            Sieć neuronowa.
	 * @param algorithm
	 *            Algorytm treningu sieci.
	 * @param modelSet
	 *            Zbiór danych do treningu sieci.
	 * @param normalizer
	 *            Normalizator danych.
	 */
	public SOMModel(SOMNetwork network, KohonenAlgorithm algorithm,
			UnsupervisedDataSet modelSet, Normalizer normalizer) {
		setNetwork(network);
		setAlgorithm(algorithm);
		setModelSet(modelSet);
		setNormalizer(normalizer);
	}

	public void initialize(AnnModelConfiguration conf) {
		setConfig(conf);
		/*
		 * Pobranie danych do modelowania z bazy.
		 */
		String signalName = null;
		if (conf.isWithNormalization()) {
			signalName = EnumSignalType.MODELLING_DATA.toString();
		} else {
			signalName = EnumSignalType.MODELLING_DATA_NORMALIZED.toString();
		}
		setModelSet(SignalAPI.prepareAllTrainDataForClassifiction(signalName,
				conf.getMaxTrainDataStart(), conf.getMaxTrainDataStop(), conf
						.getInputEnergyDelay(),
				conf.getInputTemperatureDelay(), conf.getInputHumidityDelay(),
				conf.getInputInsolationDelay(), conf.isInputDayType(), conf
						.isInputHoliday(), conf.getInputEMean24Delay(), conf
						.getInputEMean168Delay()));
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
		}
	}

	@Override
	public void initialize(AnnModelConfiguration conf, Date fromDate,
			Date toDate) {
		setConfig(conf);
		/*
		 * Pobranie danych do modelowania z bazy.
		 */
		String signalName = null;
		if (conf.isWithNormalization()) {
			signalName = EnumSignalType.MODELLING_DATA.toString();
		} else {
			signalName = EnumSignalType.MODELLING_DATA_NORMALIZED.toString();
		}
		UnsupervisedDataSet modelSet = SignalAPI
				.prepareAllTrainDataForClassifiction(signalName, fromDate,
						toDate, conf.getInputEnergyDelay(), conf
								.getInputTemperatureDelay(), conf
								.getInputHumidityDelay(), conf
								.getInputInsolationDelay(), conf
								.isInputDayType(), conf.isInputHoliday(), conf
								.getInputEMean24Delay(), conf
								.getInputEMean168Delay());
		setModelSet(modelSet, conf.isLazySetLoading());

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
		}
	}

	public void initialize(AnnModelConfiguration conf, DoubleVector in) {
		setConfig(conf);
		List<ClassPatternVO> l = new ArrayList<ClassPatternVO>();
		l.add(new ClassPatternVO(in, null, 0, null));
		setPredictSet(new UnsupervisedDataSet(l, EnumIterationType.SERIAL));
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
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void modeling() {
		/*
		 * Klasyfikacja danych.
		 */
		getAlgorithm().training(getNetwork(), getModelSet());

		/*
		 * Wyniki klasyfikacji: wyodrębnienie listy klas i odpowiadających im
		 * dni.
		 */
		classResults = new HashMap<String, List<Date>>();
		for (ClassPatternVO p : getModelSet().getSet()) {
			if (classResults.containsKey(p.getClassName())) {
				List<Date> l = classResults.get(p.getClassName());
				l.add(p.getOutTime());
				classResults.put(p.getClassName(), l);
			} else {
				List<Date> l = new ArrayList<Date>();
				l.add(p.getOutTime());
				classResults.put(p.getClassName(), l);
			}
		}
	}

	@Override
	public void execution() {
		ClassPatternVO pattern = getPredictSet().getSet().get(0);
		getNetwork().simulation(pattern.getInputs());
		String winClass = ((SOMLayer) getNetwork().getLayers().get(0))
				.getWinnerClass();
		setModelClass(winClass);
		logger.debug("winClass = " + winClass);

	}

	@Override
	public void preparation() {
		for (ClassPatternVO p : getModelSet().getSet()) {
			getNetwork().simulation(p.getInputs());
			String className = ((SOMLayer) getNetwork().getLayers().get(0))
					.generateWinnerClassLabel();
			p.setClassName(className);
		}
		classResults = new HashMap<String, List<Date>>();
		for (ClassPatternVO p : getModelSet().getSet()) {
			if (classResults.containsKey(p.getClassName())) {
				List<Date> l = classResults.get(p.getClassName());
				l.add(p.getOutTime());
				classResults.put(p.getClassName(), l);
			} else {
				List<Date> l = new ArrayList<Date>();
				l.add(p.getOutTime());
				classResults.put(p.getClassName(), l);
			}
		}
	}

	public void run() {
		// TODO Auto-generated method stub - tego też nie robimy!
	}

	public List<Date> getDatesForCurrentClass() {
		return this.classResults.get(this.getModelClass());
	}

	public List<Date> getDatesForClass(String className) {
		return this.classResults.get(className);
	}

	public SOMNetwork getNetwork() {
		return network;
	}

	public void setNetwork(SOMNetwork network) {
		this.network = network;
	}

	public Map<String, List<Date>> getClassResults() {
		return classResults;
	}

	public void setClassResults(Map<String, List<Date>> classResults) {
		this.classResults = classResults;
	}

	/* ######################### TESTY ##################################### */
	/**
	 * Test na dla cyfr 0-9, matryca 3 wymiarowa.
	 * 
	 * @param conf
	 *            Konfiguracja modelu.
	 */
	@SuppressWarnings("unchecked")
	public void testDigitalsMap3Dim(AnnModelConfiguration conf) {
		setConfig(conf);
		double[][] numbers = new double[][] {
				{ 0, 1, 1, 0, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 0, 1, 1, 0 },
				{ 0, 0, 1, 0, 0, 1, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0 },
				{ 0, 1, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1 },
				{ 0, 1, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0 },
				{ 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0 },
				{ 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0 },
				{ 0, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0 },
				{ 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0 },
				{ 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0 },
				{ 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0 } };
		List<ClassPatternVO> list = new ArrayList<ClassPatternVO>();
		int indx = 0;
		for (int i = 0; i < numbers.length; ++i) {
			DoubleVector vect = new DoubleVector(numbers[i].length);
			for (int j = 0; j < numbers[i].length; ++j) {
				vect.add(new Double(numbers[i][j]));
			}
			ClassPatternVO p = new ClassPatternVO(vect, null, indx++, null);
			list.add(p);
		}
		setModelSet(new UnsupervisedDataSet(list, EnumIterationType.RANDOM));
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
		}
		/*
		 * Trening
		 */
		getAlgorithm().training(getNetwork(), getModelSet());
		/*
		 * Wyniki
		 */
		for (ClassPatternVO p : getModelSet().getSet()) {
			System.out.println(" " + p.getId() + " " + p.getClassName());
		}
		SomNeuron[][][] map = ((Som3DimLayer) network.getLayers().get(0))
				.getSomMap();
		for (int x = 0; x < map.length; ++x) {
			for (int y = 0; y < map[x].length; ++y) {
				for (int z = 0; z < map[x][y].length; ++z) {
					System.out
							.print("["
									+ x
									+ "]"
									+ "["
									+ y
									+ "]"
									+ "["
									+ z
									+ "]"
									+ (map[x][y][z].getClassName() != null
											&& map[x][y][z].getClassName()
													.length() > 0 ? map[x][y][z]
											.getClassName()
											: "       "));
				}
			}
			System.out.println("");
		}
	}

	/**
	 * Test klasyfikacji danych
	 * 
	 * @param conf
	 *            Konfiguracja modelu.
	 */
	@SuppressWarnings( { "unchecked", "deprecation" })
	public void testDiffsMap2Dim(AnnModelConfiguration conf) {
		setConfig(conf);
		/*
		 * Pobranie danych do modelowania z bazy.
		 */
		String signalName = null;
		if (conf.isWithNormalization()) {
			signalName = EnumSignalType.MODELLING_DATA.toString();
		} else {
			signalName = EnumSignalType.MODELLING_DATA_NORMALIZED.toString();
		}
		setModelSet(SignalAPI.prepareAllTrainDataForClassifiction(signalName,
				conf.getMaxTrainDataStart(), conf.getMaxTrainDataStop(), conf
						.getInputEnergyDelay(),
				conf.getInputTemperatureDelay(), conf.getInputHumidityDelay(),
				conf.getInputInsolationDelay(), conf.isInputDayType(), conf
						.isInputHoliday(), conf.getInputEMean24Delay(), conf
						.getInputEMean168Delay()));
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
		}
		/*
		 * Klasyfikacja danych.
		 */
		getAlgorithm().training(getNetwork(), getModelSet());
		/*
		 * Wyniki
		 */
		Map<String, List<Date>> res = new HashMap<String, List<Date>>();
		for (ClassPatternVO p : getModelSet().getSet()) {
			if (res.containsKey(p.getClassName())) {
				List<Date> l = res.get(p.getClassName());
				l.add(p.getOutTime());
				res.put(p.getClassName(), l);
			} else {
				List<Date> l = new ArrayList<Date>();
				l.add(p.getOutTime());
				res.put(p.getClassName(), l);
			}
		}
		System.out.println("---------------------");
		Set entries = res.entrySet();
		Iterator it = entries.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			System.out.println("");
			System.out.print("Klasa: " + entry.getKey() + " Czas: ");
			List<Date> dd = (List<Date>) entry.getValue();
			for (Date d : dd) {
				System.out.print(" " + d.toLocaleString());
			}
		}
		System.out.println("---------------------");
		SomNeuron[][] map = ((Som2DimLayer) network.getLayers().get(0))
				.getSomMap();
		for (int x = 0; x < map.length; ++x) {
			for (int y = 0; y < map[x].length; ++y) {
				System.out
						.print("["
								+ x
								+ "]"
								+ "["
								+ y
								+ "]"
								+ (map[x][y].getClassName() != null
										&& map[x][y].getClassName().length() > 0 ? map[x][y]
										.getClassName()
										: "       "));
			}
			System.out.println("");
		}
	}

}
