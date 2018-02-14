package pl.phytia.model.sets;

import java.util.Date;

import pl.phytia.utils.Normalizer;

/**
 * Wzorzec dla uczenia nadzorowanego. Para ucząca : wejścia - wyjścia.
 * 
 * @author Jarosław Protasiewicz
 */
public class PatternPairVO extends PatternVO<PatternPairVO> {

	/**
	 * Wzorzec wyjść.
	 */
	private DoubleVector outputs;

	/**
	 * Konstruktor bezargumentowy.
	 */
	public PatternPairVO() {
		super();
	}

	/**
	 * Konstruktor.
	 * 
	 * @param inputs
	 *            Wektor wejść.
	 * @param outputs
	 *            Wzorzec wyjść.
	 */
	public PatternPairVO(DoubleVector inputs, DoubleVector outputs) {
		super(inputs);
		this.outputs = outputs;
		setNormalized(false);
	}

	/**
	 * Konstruktor.
	 * 
	 * @param inputs
	 *            Wektor wejść.
	 * @param outputs
	 *            Wzorzec wyjść.
	 * @param outTime
	 *            Punkt w czasie jakiemu będzie odpowiadało oczekiwany wyjście
	 *            podczas prognozy.
	 */
	public PatternPairVO(DoubleVector inputs, DoubleVector outputs, Date outTime) {
		super(inputs, outTime);
		this.outputs = outputs;
		setNormalized(false);
	}

	public double min() {
		double min1 = getInputs().min();
		double min2 = outputs.min();
		if (min1 < min2) {
			return min1;
		} else {
			return min2;
		}
	}

	public double max() {
		double max1 = getInputs().max();
		double max2 = outputs.max();
		if (max1 > max2) {
			return max1;
		} else {
			return max2;
		}
	}

	public void denormalize(Normalizer normalizer) {
		if (isNormalized()) {
			this.getInputs().denormalize(normalizer);
			this.getOutputs().denormalize(normalizer);
			setNormalized(false);
		}

	}

	public void normalize(Normalizer normalizer) {
		if (!isNormalized()) {
			this.getInputs().normalize(normalizer);
			this.getOutputs().normalize(normalizer);
			setNormalized(true);
		}
	}

	public DoubleVector getOutputs() {
		return outputs;
	}

	public void setOutputs(DoubleVector outputs) {
		this.outputs = outputs;
	}
}
