package pl.phytia.prediction.test.som;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import pl.phytia.model.conf.algorithms.KohonenConfiguration;
import pl.phytia.ann.algorithms.som.KohonenAlgorithm;
import pl.phytia.ann.networks.SOMNetwork;
import pl.phytia.model.conf.functions.learnRate.ExponentialLearningRateConfiguration;
import pl.phytia.model.conf.functions.neighbourhood.GaussianNeighbourhoodConfiguration;
import pl.phytia.model.conf.functions.neuron.SomNeuronConfiguration;
import pl.phytia.model.conf.layers.SOMLayerConfiguration;
import pl.phytia.model.conf.networks.SOMNetworkConfiguration;
import pl.phytia.model.conf.pred.AnnModelConfiguration;
import pl.phytia.model.enums.EnumDistanceType;
import pl.phytia.model.enums.EnumFunctionType;
import pl.phytia.model.enums.EnumLayerType;
import pl.phytia.model.enums.EnumLearningRatioType;
import pl.phytia.model.enums.EnumNeighbourhoodType;
import pl.phytia.model.enums.EnumNetworkType;
import pl.phytia.prediction.models.SOMModel;
import pl.phytia.utils.Localization;

public class SOM3Test {

	public static void main(String[] args) {

		SOM3Test test = new SOM3Test();

		SOMModel model = new SOMModel(test.prepareNetwork(), test
				.prepareAlgorithm(), null, null);
		AnnModelConfiguration conf = new AnnModelConfiguration();
		conf.setInputEnergyDelay(24);
		conf.setInputHumidityDelay(1);
		conf.setInputInsolationDelay(1);
		conf.setInputTemperatureDelay(1);
		conf.setNormRatio(0);
		try {
			conf.setMaxTrainDataStart(Localization.plDateFormatMedium
					.parse("2002-07-01"));
			conf.setMaxTrainDataStop(Localization.plDateFormatMedium
					.parse("2002-07-31"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		conf.setNetworkType(EnumNetworkType.SOM);
		model.initialize(conf);
		model.preparation();
	}

	public KohonenAlgorithm prepareAlgorithm() {
		KohonenConfiguration conf = new KohonenConfiguration();
		conf.setMinClasses(5);
		conf.setMinGlobalWeightsChange(1E-3);
		conf.setMaxIteration(50);
		KohonenAlgorithm alg = new KohonenAlgorithm();
		alg.setConfig(conf);
		return alg;
	}

	public SOMNetwork prepareNetwork() {
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
		 * Konfiguracjia neuronu - elementu matrycy SOM.
		 */
		SomNeuronConfiguration snc = new SomNeuronConfiguration();
		snc.setNumberOfInputs(27); // 24 + 1 + 1 + 1
		snc.setTypeOfFunction(EnumFunctionType.Pipe);
		/*
		 * Konfuiguracja matrycy SOM.
		 * 
		 * SOMLayerConfiguration slc = new SOMLayerConfiguration<GaussianNeighbourhoodConfiguration,
		 * ExponentialLearningRateConfiguration>( nfc, lrc,
		 * EnumDistanceType.Euclidean, snc, new int[] { 5, 5 });
		 */
		SOMLayerConfiguration slc = new SOMLayerConfiguration<GaussianNeighbourhoodConfiguration, ExponentialLearningRateConfiguration>(
				nfc, lrc, EnumDistanceType.Euclidean, snc,
				new int[] { 5, 5, 5 });
		slc.setTypeOfLayer(EnumLayerType.OUTPUT);
		List<SOMLayerConfiguration> liSlc = new ArrayList<SOMLayerConfiguration>();
		liSlc.add(slc);
		/*
		 * Konfiguracja sieci neuronowej.
		 */
		SOMNetworkConfiguration conf = new SOMNetworkConfiguration();
		conf.setLayersConf(liSlc);
		conf.setMaxNormValue(1);
		conf.setMinNormValue(0);
		conf.setTypeOfNetwork(EnumNetworkType.SOM);

		/*
		 * Utworzenie sieci.
		 */
		SOMNetwork net = new SOMNetwork(conf);

		return net;
	}

}
