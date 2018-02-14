package pl.phytia.model.conf.functions.centerSelect;

import pl.phytia.model.conf.Configuration;
import pl.phytia.model.enums.EnumCenterSelectType;
import pl.phytia.model.enums.EnumDistanceType;

/**
 * Konfiguracja funkcji doboru centrów.
 * 
 * @author Jarosław Protasiewicz
 */
public class CenterSelFunctionConfiguration extends
		Configuration<CenterSelFunctionConfiguration> {

	private static final long serialVersionUID = -458464584492658746L;

	public CenterSelFunctionConfiguration() {
		super();
		centerSelectType = EnumCenterSelectType.RANDOM_CENTER_SELECT;
		initNumberOfCenters = 10;
		maxStepKMean = 50;
		distanceTypeKMean = EnumDistanceType.Euclidean;
		initLearnRatioKMean = 0.5;
	}

	/**
	 * Typ metody doboru centrów.
	 */
	private EnumCenterSelectType centerSelectType;

	/**
	 * Początkowa ilość centrów. <br>
	 * Parametr nie ma znaczenia w przypadku dokładnego doboru centrów.
	 */
	private int initNumberOfCenters;

	/**
	 * Maksymalna ilość kroków wykonywana podczas doboru centrów metodą
	 * k-średnich
	 */
	private int maxStepKMean;

	/**
	 * Typ funkcji odległości używany w metodzie k-średnich.
	 */
	private EnumDistanceType distanceTypeKMean;

	/**
	 * Początkowa wartość współczynnika uczenia w metodzie k-średnich.
	 */
	private double initLearnRatioKMean;

	public EnumCenterSelectType getCenterSelectType() {
		return centerSelectType;
	}

	public void setCenterSelectType(EnumCenterSelectType centerSelectType) {
		this.centerSelectType = centerSelectType;
	}

	public int getInitNumberOfCenters() {
		return initNumberOfCenters;
	}

	public void setInitNumberOfCenters(int initNumberOfCenters) {
		this.initNumberOfCenters = initNumberOfCenters;
	}

	public int getMaxStepKMean() {
		return maxStepKMean;
	}

	public void setMaxStepKMean(int maxStepKMean) {
		this.maxStepKMean = maxStepKMean;
	}

	public EnumDistanceType getDistanceTypeKMean() {
		return distanceTypeKMean;
	}

	public void setDistanceTypeKMean(EnumDistanceType distanceTypeKMean) {
		this.distanceTypeKMean = distanceTypeKMean;
	}

	public double getInitLearnRatioKMean() {
		return initLearnRatioKMean;
	}

	public void setInitLearnRatioKMean(double initLearnRatioKMean) {
		this.initLearnRatioKMean = initLearnRatioKMean;
	}

}
