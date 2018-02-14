package pl.phytia.prediction.crossValidation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import pl.phytia.model.conf.functions.neuron.FahlmanFunctionConfiguration;
import pl.phytia.model.conf.networks.MLPNetworkConfiguration;
import pl.phytia.ann.algorithms.Algorithm;
import pl.phytia.ann.algorithms.bp.BPAlgorithm;
import pl.phytia.ann.algorithms.bp.QuickPropAlgorithm;
import pl.phytia.ann.networks.MLPNetwork;
import pl.phytia.ann.networks.Network;
import pl.phytia.api.SignalAPI;
import pl.phytia.dao.pg.PgSignalDAO;
import pl.phytia.model.conf.algorithms.BackPropConfiguration;
import pl.phytia.model.conf.layers.MLPLayerConfiguration;
import pl.phytia.model.conf.pred.AnnModelConfiguration;
import pl.phytia.model.db.ModelDbVO;
import pl.phytia.model.enums.EnumFunctionType;
import pl.phytia.model.enums.EnumLayerType;
import pl.phytia.model.enums.EnumLearningRatioType;
import pl.phytia.model.enums.EnumNetworkType;
import pl.phytia.model.enums.EnumSignalType;
import pl.phytia.model.pred.ValidationPairVO;
import pl.phytia.model.sets.SupervisedDataSet;
import pl.phytia.prediction.metaModels.CvMetaModel;
import pl.phytia.prediction.models.CVMLPModel;
import pl.phytia.utils.Normalizer;

/**
 * Wyznaczenie optymalnego okna pomiarowego.
 * 
 * @author Jarosław Protasiewicz
 */
public class DataWindowTask extends CvMetaModel {

	protected static Logger logger = Logger.getLogger(PgSignalDAO.class);

	/**
	 * Aktualny indeks testu.
	 */
	private int indxPrep;

	/**
	 * Aktualna klasa testu
	 */
	private String testClass;

	/**
	 * Dni na które jest wykonywana prognoza dla Klasy D3.
	 */
	private final static String[] taskDaysD3 = new String[] { "2004-01-28",
			"2004-02-11", "2004-03-17", "2004-04-14", "2004-05-12",
			"2004-06-16", "2004-07-14", "2004-08-18", "2004-09-15",
			"2004-10-13", "2004-11-17", "2004-12-15" };

	/**
	 * Dni na które jest wykonywana prognoza dla klasy D7.
	 */
	private final static String[] taskDaysD7 = new String[] { "2004-01-25",
			"2004-02-15", "2004-03-21", "2004-04-18", "2004-05-16",
			"2004-06-20", "2004-07-18", "2004-08-22", "2004-09-19",
			"2004-10-17", "2004-11-21", "2004-12-19" };

	/**
	 * Wykonanie zadania.
	 */
	public static void main(String[] args) throws Exception {
		String kom = null;
		String ikea = null;
		if (args.length < 2) {
			ikea = "c:\\tmp\\Test-10-DataWindowTask.txt";
		} else {
			ikea = args[1];
		}
		BufferedWriter out = new BufferedWriter(new FileWriter(ikea));

		DataWindowTask task = new DataWindowTask();
		/*
		 * Klasa C3 - test dla każdej konfiguracji modelu
		 */
		task.setTestClass("C3");
		task.prepareTestDays(taskDaysD3);
		AnnModelConfiguration[] modelConfigs = task
				.prepareModelConfigurations();
		for (int k = 0; k < modelConfigs.length; ++k) {
			out
					.write("\n-----------------------------------------------------------\n");
			kom = "Test dla konfiguracji :" + modelConfigs[k].toString();
			System.out.println(kom);
			out.write(kom + "\n");
			task.prepareModels(modelConfigs[k]);
			task.prediction();
			List<String> resultTxt = task.crossValidationErrors();
			for (String res : resultTxt) {
				System.out.println(res);
				out.write(" " + res + "\n");
			}
		}

		/*
		 * Klasa C7 - test dla każdej konfiguracji modelu
		 */
		task.setTestClass("C7");
		task.prepareTestDays(taskDaysD7);
		modelConfigs = task.prepareModelConfigurations();
		for (int k = 0; k < modelConfigs.length; ++k) {
			out
					.write("-----------------------------------------------------------\n");
			kom = "Test dla konfiguracji :" + modelConfigs[k].toString();
			System.out.println(kom);
			out.write(kom + "\n");
			task.prepareModels(modelConfigs[k]);
			task.preparation();
			task.prediction();
			List<String> resultTxt = task.crossValidationErrors();
			for (String res : resultTxt) {
				System.out.println(res);
				out.write(" " + res + "\n");
			}
		}
		out.close();
	}

	@SuppressWarnings("unchecked")
	public void prepareModels(AnnModelConfiguration modelConf) {
		try {
			List<CVMLPModel> models = new ArrayList<CVMLPModel>();
			for (int i = 0; i < taksDays.length; ++i) {
				List<ValidationPairVO> valSets = SignalAPI
						.prepareLOOCossValidationData(
								EnumSignalType.MODELLING_DATA_NORMALIZED
										.toString(), taksDays[i], modelConf
										.getInputWindowSizeFrom(), modelConf
										.getInputWindowSizeTo(), modelConf
										.getInputWindowYearsDelay(), modelConf
										.getInputEnergyDelay(), modelConf
										.getInputTemperatureDelay(), modelConf
										.getInputHumidityDelay(), modelConf
										.getInputInsolationDelay());
				for (int j = 0; j < valSets.size(); ++j) {
					CVMLPModel m = new CVMLPModel(modelConf,
							prepareNetwork(modelConf),
							prepareFirstPhaseAlgorithm(),
							prepareSecondPhaseAlgorithm(), valSets.get(j)
									.getTraningSet(), valSets.get(j)
									.getValidationSet(), new Normalizer(),
							testClass);
					m.setPredictionDay(taksDays[i]);
					m.initialize(modelConf);
					models.add(m);
				}
				setPredictors(models);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private AnnModelConfiguration[] prepareModelConfigurations() {
		int[][] mConf = new int[][] {

		{ -1 - (2 * 7), 1 + (0 * 7), -0 }, { -1 - (3 * 7), 1 + (0 * 7), -0 },
				{ -1 - (4 * 7), 1 + (0 * 7), -0 },
				{ -1 - (1 * 7), 1 + (1 * 7), -1 },
				{ -1 - (2 * 7), 1 + (2 * 7), -1 },
				{ -1 - (3 * 7), 1 + (3 * 7), -1 },
				{ -1 - (4 * 7), 1 + (4 * 7), -1 },
				{ -1 - (1 * 7), 1 + (1 * 7), -2 },
				{ -1 - (2 * 7), 1 + (2 * 7), -2 },
				{ -1 - (3 * 7), 1 + (3 * 7), -2 } };

		AnnModelConfiguration[] confs = new AnnModelConfiguration[mConf.length];
		for (int i = 0; i < mConf.length; ++i) {
			confs[i] = new AnnModelConfiguration(EnumNetworkType.MLP, 1, 0, 0,
					24, mConf[i][0], mConf[i][1], mConf[i][2], 24, 0.1, null,
					null, true, false);
		}
		return confs;
	}

	public Algorithm prepareFirstPhaseAlgorithm() {
		BackPropConfiguration conf = new BackPropConfiguration(
				EnumLearningRatioType.CONSTANT, EnumFunctionType.SSE,
				EnumFunctionType.MAPE);
		conf.setMaximumGrowthRatio(1.75);
		conf.setMomentumRatio(0.3);
		conf.setLearningRatio(0.2);
		conf.setWeightRatio(-0.00001);
		conf.setMaxIteration(500);
		conf.setMinError(2);
		Algorithm<QuickPropAlgorithm, BackPropConfiguration, SupervisedDataSet, MLPNetwork> alg = new QuickPropAlgorithm(
				conf);
		alg.initialize(conf);
		return alg;
	}

	public Algorithm prepareSecondPhaseAlgorithm() {
		BackPropConfiguration conf = new BackPropConfiguration(
				EnumLearningRatioType.CONSTANT, EnumFunctionType.SSE,
				EnumFunctionType.MAPE);
		conf.setLearningRatio(0.1);
		conf.setMaxIteration(3000);
		conf.setMinError(1.5);
		conf.setMaximumGrowthRatio(1.04);
		Algorithm<BPAlgorithm, BackPropConfiguration, SupervisedDataSet, MLPNetwork> alg = new BPAlgorithm(
				conf);
		alg.initialize(conf);
		return alg;
	}

	@SuppressWarnings("unchecked")
	public Network prepareNetwork(AnnModelConfiguration conf) {

		/*
		 * Lista warstw sieci.
		 */
		List<MLPLayerConfiguration> cl = new ArrayList<MLPLayerConfiguration>();
		/*
		 * Konfiguracja 1 warstwy sieci.
		 */
		MLPLayerConfiguration<FahlmanFunctionConfiguration> f1 = new MLPLayerConfiguration<FahlmanFunctionConfiguration>();
		f1.setNeuronFunctionConf(new FahlmanFunctionConfiguration());
		f1.setNumberOfNeurons(10);
		f1.setNumberOfInputs(conf.getInputEnergyDelay()
				+ conf.getInputHumidityDelay() + conf.getInputInsolationDelay()
				+ conf.getInputTemperatureDelay());
		f1.setTypeOfLayer(EnumLayerType.INPUT);
		cl.add(f1);
		/*
		 * Konfiguracja 2 warstwy sieci.
		 */
		MLPLayerConfiguration<FahlmanFunctionConfiguration> f2 = new MLPLayerConfiguration<FahlmanFunctionConfiguration>();
		f2.setNeuronFunctionConf(new FahlmanFunctionConfiguration());
		f2.setNumberOfNeurons(7);
		f2.setNumberOfInputs(10);
		f2.setTypeOfLayer(EnumLayerType.HIDDEN);
		cl.add(f2);
		/*
		 * Konfiguracja 3 warstwy sieci.
		 */
		MLPLayerConfiguration<FahlmanFunctionConfiguration> f3 = new MLPLayerConfiguration<FahlmanFunctionConfiguration>();
		f3.setNeuronFunctionConf(new FahlmanFunctionConfiguration());
		f3.setNumberOfNeurons(1);
		f3.setNumberOfInputs(7);
		f3.setTypeOfLayer(EnumLayerType.OUTPUT);
		cl.add(f3);
		/*
		 * Konfiguracja sieci neuronowej.
		 */
		MLPNetworkConfiguration netConf = new MLPNetworkConfiguration();
		netConf.setTypeOfNetwork(EnumNetworkType.MLP);
		netConf.setLayersConf(cl);
		/*
		 * Utworzenie sieci neuronowej.
		 */
		Network net = new MLPNetwork(netConf);
		net.initialize(netConf);
		return net;
	}

	/**
	 * @return wartość pola testClass
	 */
	public String getTestClass() {
		return testClass;
	}

	/**
	 * @param testClass
	 *            jest przypisywany do pola testClass
	 */
	public void setTestClass(String testClass) {
		this.testClass = testClass;
	}

	/**
	 * @return wartość pola indxPrep
	 */
	public int getIndxPrep() {
		return indxPrep;
	}

	/**
	 * @param indxPrep
	 *            jest przypisywany do pola indxPrep
	 */
	public void setIndxPrep(int indxPrep) {
		this.indxPrep = indxPrep;
	}

	/**
	 * @return wartość pola taksDays
	 */
	public Date[] getTaksDays() {
		return taksDays;
	}

	/**
	 * @param taksDays
	 *            jest przypisywany do pola taksDays
	 */
	public void setTaksDays(Date[] taksDays) {
		this.taksDays = taksDays;
	}

	@Override
	public void preparation() {
		// TODO Auto-generated method stub

	}

	@Override
	protected AnnModelConfiguration preparePredictorConfiguration() {
		return null;
	}

	@Override
	protected Network prepareMLPNetwork(String State) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getPredictorStructure() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Algorithm prepareFirstPhaseTrainAlgorithm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Algorithm prepareSecondPhaseTrainAlgorithm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected CVMLPModel preprareModel(ModelDbVO modelDb) {
		// TODO Auto-generated method stub
		return null;
	}

}
