package pl.phytia.model.conf.functions.neuron;

import pl.phytia.model.enums.EnumDistanceType;
import pl.phytia.model.enums.EnumFunctionType;

/**
 * Konfiguracja funkcji radialnej.
 * 
 * @author Jarosław Protasiewicz
 */
public class RbfFunctionConfiguration extends
		NeuronFunctionConfiguration<RbfFunctionConfiguration> {

	private static final long serialVersionUID = -2614547514806752640L;

	/**
	 * Domyślna szerekość funkcji radialnej.
	 */
	private double defaultWidth;

	/**
	 * Stopień potęgowania - używany tylko dla funkcji potęgowej.
	 */
	private int powerDegree;

	/**
	 * Typ funkcji odległości.
	 */
	private EnumDistanceType distanceType;

	public RbfFunctionConfiguration() {
		super();
		defaultWidth = 5;
		distanceType = EnumDistanceType.Euclidean;
		powerDegree = 3;
		setTypeOfFunction(EnumFunctionType.GaussianRbf);
	}

	public double getDefaultWidth() {
		return defaultWidth;
	}

	public void setDefaultWidth(double defaultWidth) {
		this.defaultWidth = defaultWidth;
	}

	public EnumDistanceType getDistanceType() {
		return distanceType;
	}

	public void setDistanceType(EnumDistanceType distanceType) {
		this.distanceType = distanceType;
	}

	public int getPowerDegree() {
		return powerDegree;
	}

	public void setPowerDegree(int powerDegree) {
		this.powerDegree = powerDegree;
	}

}
