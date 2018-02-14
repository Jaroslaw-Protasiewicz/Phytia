package pl.phytia.ann.functions.neuron;

import java.util.List;

import pl.phytia.model.conf.functions.neuron.RbfFunctionConfiguration;
import pl.phytia.model.enums.EnumDistanceType;
import pl.phytia.model.sets.DoubleVector;

/**
 * Potęgowa funkcja radialna.
 * 
 * @author Jarosław Protasiewicz
 */
public final class PoweredRbfFunction extends
		RadialFunction<RbfFunctionConfiguration, PoweredRbfFunction> {

	private static final long serialVersionUID = -4817609558500699722L;

	/**
	 * Konstruktor bezargumentowy.
	 */
	public PoweredRbfFunction() {
		super();
	}

	public PoweredRbfFunction(int inputs, EnumDistanceType distanceType) {
		super(inputs, distanceType);
	}

	/**
	 * Konstruktor wypełnijacy pola.
	 * 
	 * @param center
	 *            Centrum funkcji radialnej.
	 * @param width
	 *            Szerokość funkcji radialnej.
	 * @param distanceType
	 *            Typ funkcji odległości.
	 */
	public PoweredRbfFunction(DoubleVector center, double width,
			EnumDistanceType distanceType) {
		super(center, width, distanceType);
	}

	@Override
	public double computeOutput(DoubleVector inputs) {
		double distance = getDistanceFunction().computeDistance(inputs,
				getCenter());
		return Math.pow(Math.pow(distance, 2) + Math.pow(getWidth(), 2), config
				.getPowerDegree());
	}

	@Override
	public double maxReturnValue() {
		return 1;
	}

	@Override
	public double minReturnValue() {
		return 0;
	}

	@Override
	public DoubleVector computeCentersGradient(DoubleVector errors,
			DoubleVector outputs, double weight, List<DoubleVector> inputs) {
		throw new UnsupportedOperationException(
				"Nie zdefiniowano metody dla tej funcji");
	}

	@Override
	public double computeWeightGradient(DoubleVector errors,
			DoubleVector outputs) {
		throw new UnsupportedOperationException(
				"Nie zdefiniowano metody dla tej funcji");
	}

	@Override
	public double computeWidthGradient(DoubleVector errors,
			DoubleVector outputs, double weight, List<DoubleVector> inputs) {
		throw new UnsupportedOperationException(
				"Nie zdefiniowano metody dla tej funcji");
	}

}
