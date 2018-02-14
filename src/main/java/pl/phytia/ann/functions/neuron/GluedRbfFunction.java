package pl.phytia.ann.functions.neuron;

import java.util.List;

import pl.phytia.ann.functions.distance.DistanceFunction;
import pl.phytia.model.conf.functions.neuron.RbfFunctionConfiguration;
import pl.phytia.model.enums.EnumDistanceType;
import pl.phytia.model.sets.DoubleVector;

/**
 * Radialna funkcja sklejana.
 * 
 * @author Jarosław Protasiewicz
 */
public final class GluedRbfFunction extends
		RadialFunction<RbfFunctionConfiguration, GluedRbfFunction> {

	private static final long serialVersionUID = 8340352949417742984L;

	public GluedRbfFunction() {
		super();
		config = new RbfFunctionConfiguration();
		setCenter(new DoubleVector(config.getNumberOfInputs()));
		setWidth(config.getDefaultWidth());
	}

	public GluedRbfFunction(int inputs, EnumDistanceType distanceType) {
		super(inputs, distanceType);
	}

	/**
	 * Konstruktor wypełnijacy pola.
	 * 
	 * @param center
	 *            Centrum funkcji radialnej.
	 * @param width
	 *            Szerokość funkcji radialnej.
	 * @param distanceFunction
	 *            Funkcja odległości.
	 */
	public GluedRbfFunction(RbfFunctionConfiguration conf, DoubleVector center,
			double width, DistanceFunction distanceFunction) {
		super(center, width, conf.getDistanceType());
		initialize(conf);
	}

	@Override
	public double computeOutput(DoubleVector inputs) {
		double distance = getDistanceFunction().computeDistance(inputs,
				getCenter());
		return Math.pow(distance, 2) * Math.log(distance);
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
