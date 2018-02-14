package pl.phytia.prediction.test.conf;

import pl.phytia.model.conf.functions.neuron.SBipolarFunctionConfiguration;

public class ConfigurationTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		ConfigurationTest ct = new ConfigurationTest();
		ct.storeTest();

	}

	@SuppressWarnings("unchecked")
	public void storeTest() {
		SBipolarFunctionConfiguration conf = new SBipolarFunctionConfiguration();
		conf.setBetaRatio(1111);
		String state = conf.storeState();
		@SuppressWarnings("unused")
		SBipolarFunctionConfiguration conf1 = new SBipolarFunctionConfiguration();
		conf1.loadState(state);
	}

}
