package pl.phytia.ann.functions.neuron;

import java.util.List;

import pl.phytia.ann.functions.distance.DistanceFunction;
import pl.phytia.model.conf.functions.neuron.RbfFunctionConfiguration;
import pl.phytia.model.enums.EnumDistanceType;
import pl.phytia.model.sets.DoubleVector;

/**
 * Radialna funkcja Gaussa.
 * 
 * @author Jarosław Protasiewicz
 */
public final class GaussianRbfFunction extends
		RadialFunction<RbfFunctionConfiguration, GaussianRbfFunction> {

	private static final long serialVersionUID = 1456382285100139599L;

	public GaussianRbfFunction() {
		super();
		config = new RbfFunctionConfiguration();
		setCenter(new DoubleVector(config.getNumberOfInputs()));
		setWidth(config.getDefaultWidth());
	}

	public GaussianRbfFunction(int inputs, EnumDistanceType distanceType) {
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
	public GaussianRbfFunction(RbfFunctionConfiguration conf,
			DoubleVector center, double width, DistanceFunction distanceFunction) {
		super(center, width, conf.getDistanceType());
		initialize(conf);
	}

	@Override
	public double computeOutput(DoubleVector inputs) {
		double distance = getDistanceFunction().computeDistance(inputs,
				getCenter());
		setOutput(Math.exp(-Math.pow(distance, 2) / (2 * getWidth())));
		return getOutput();
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
		double error = 2 * weight / Math.pow(getWidth(), 2);
		DoubleVector deltaCenter = new DoubleVector();
		for (int i = 0; i < getCenter().size(); ++i) {
			double center = 0.0;
			for (int j = 0; j < errors.size(); ++j) {
				center += errors.get(j) * outputs.get(j)
						* (inputs.get(j).get(i) * getCenter().get(i));
			}
			deltaCenter.add(new Double(error * center));
		}
		return deltaCenter;
	}

	@Override
	public double computeWeightGradient(DoubleVector errors,
			DoubleVector outputs) {
		double deltaWeight = 0.0;
		for (int i = 0; i < errors.size(); ++i) {
			deltaWeight += (errors.get(i) * outputs.get(i));
		}
		return deltaWeight;
	}

	@Override
	public double computeWidthGradient(DoubleVector errors,
			DoubleVector outputs, double weight, List<DoubleVector> inputs) {
		double error = 2 * weight / Math.pow(getWidth(), 3);
		double deltaWidth = 0.0;
		for (int j = 0; j < errors.size(); ++j) {
			deltaWidth += errors.get(j)
					* outputs.get(j)
					* Math.pow(getDistanceFunction().computeDistance(
							inputs.get(j), getCenter()), 2);
		}
		return error * deltaWidth;
	}

	/*
	 * @Override public double computeOfflineWeightGradient(DoubleVector errors) {
	 * double gradient = 0; for (Double err : errors) { gradient += err; }
	 * return gradient * getOutput(); }
	 * 
	 * @Override public double computeOnlineWeightGradient(double error) {
	 * return (error * getOutput()); }
	 * 
	 * @Override public DoubleVector computeOfflineCentersGradient(DoubleVector
	 * errors, double weight, DoubleVector inputs) { double error = 0.0; for
	 * (Double err : errors) { error += err; } DoubleVector deltaCenter = new
	 * DoubleVector(getCenter().size()); for (int i = 0; i < getCenter().size();
	 * ++i) { deltaCenter.add( (2 * weight / Math.pow(getWidth(), 2)) * error *
	 * getOutput() * (inputs.get(i) - getCenter().get(i)) ); } return
	 * deltaCenter; }
	 * 
	 * @Override public DoubleVector computeOnlineCentersGradient(double error,
	 * double weight, DoubleVector inputs) { DoubleVector deltaCenter = new
	 * DoubleVector(getCenter().size()); for (int i = 0; i < getCenter().size();
	 * ++i) { deltaCenter.add(new Double(2 * weight * error * getOutput() *
	 * (inputs.get(i).doubleValue() - getCenter().get(i).doubleValue()) /
	 * Math.pow(getWidth(), 2))); } return deltaCenter; }
	 * 
	 * @Override public double computeOfflineWidthGradient(DoubleVector errors,
	 * double weight, DoubleVector inputs) { double error = 0.0; for (Double err :
	 * errors) { error += err; } return (2 * weight / Math.pow(getWidth(), 3) ) *
	 * error * getOutput() *
	 * Math.pow(getDistanceFunction().computeDistance(inputs, getCenter()), 2); }
	 * 
	 * @Override public double computeOnlineWidthGradient(double error, double
	 * weight, DoubleVector inputs) { return 2 * weight * error * getOutput() *
	 * Math.pow(getDistanceFunction().computeDistance(inputs, getCenter()), 2) /
	 * Math.pow(getWidth(), 3);
	 *  }
	 */
}
