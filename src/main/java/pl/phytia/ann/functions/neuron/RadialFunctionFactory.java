package pl.phytia.ann.functions.neuron;

import pl.phytia.model.conf.functions.neuron.RbfFunctionConfiguration;

/**
 * Prosta fabryka funkcji radialnych.
 * 
 * @author Jarosław Protasiewicz
 */
public class RadialFunctionFactory {

	/**
	 * Produkuje funkcję radialną na podstawie konfiguracji.
	 * 
	 * @param conf
	 *            Konfiguracja.
	 * @return Funkcja radialna.
	 */
	@SuppressWarnings("unchecked")
	public static RadialFunction getRadialNeuronFunction(
			RbfFunctionConfiguration conf) {
		RadialFunction radialFunction = null;
		switch (conf.getTypeOfFunction()) {
		case PoweredRbf:
			radialFunction = new PoweredRbfFunction(conf.getNumberOfInputs(),
					conf.getDistanceType());
			break;
		case GluedRbf:
			radialFunction = new GluedRbfFunction(conf.getNumberOfInputs(),
					conf.getDistanceType());
			break;
		case GaussianRbf:
			radialFunction = new GaussianRbfFunction(conf.getNumberOfInputs(),
					conf.getDistanceType());
			break;
		default:
			radialFunction = new GaussianRbfFunction(conf.getNumberOfInputs(),
					conf.getDistanceType());
			break;
		}
		radialFunction.setConfig(conf);
		return radialFunction;
	}
}
