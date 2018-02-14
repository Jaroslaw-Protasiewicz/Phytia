package pl.phytia.prediction.test.rbf;

import java.util.Date;

import pl.phytia.api.SignalAPI;
import pl.phytia.model.conf.algorithms.RbfAlgorithmConfiguration;
import pl.phytia.model.enums.EnumIterationType;
import pl.phytia.ann.algorithms.rbf.RbfMatrixInvAlgorithm;
import pl.phytia.ann.networks.RBFNetwork;
import pl.phytia.model.conf.networks.RbfNetworkConfiguration;
import pl.phytia.model.enums.EnumSignalType;
import pl.phytia.model.sets.SupervisedDataSet;
import pl.phytia.utils.Localization;

public class RbfTest {

	public static void main(String[] args) throws Exception {

		Date dzienPrognozy = Localization.plDateFormatMedium
				.parse("2004-07-15");
		Date dzienStartPrognozy = dzienPrognozy;

		SupervisedDataSet set = SignalAPI.prepareTrainSet(
				EnumSignalType.MODELLING_DATA_NORMALIZED.toString(),
				dzienPrognozy, dzienStartPrognozy, -15, +15, 1, 24, 1, 0, 0);
		set.setEnumIterationType(EnumIterationType.RANDOM);

		/*
		 * Konfiguracja sieci RBF.
		 */
		RbfNetworkConfiguration confRbf = new RbfNetworkConfiguration();

		/*
		 * Sieć.
		 */
		RBFNetwork net = new RBFNetwork(confRbf);
		/*
		 * Algorytm.
		 */
		RbfAlgorithmConfiguration aglConf = new RbfAlgorithmConfiguration();
		RbfMatrixInvAlgorithm alg = new RbfMatrixInvAlgorithm(aglConf);
		alg.training(net, set);

	}

}
