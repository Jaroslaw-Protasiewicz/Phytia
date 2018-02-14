package pl.phytia.ann.neurons;

import pl.phytia.model.conf.functions.neuron.NeuronFunctionConfiguration;
import pl.phytia.model.sets.DoubleVector;
import pl.phytia.utils.MathUtil;
import pl.phytia.ann.functions.neuron.NeuronActivationFunction;

/**
 * Sumator.
 * 
 * @param <S>
 *            Typ obietu zwaracanego przez <code>this</code>.
 * @param <C>
 *            Typ obiektu konfiguracji.
 * 
 * @author Jarosław Protasiewicz
 */
public abstract class Adder<S extends Neuron, C extends NeuronFunctionConfiguration>
		extends Neuron<S, C> {

	/* --------- FIELDS --- */
	/**
	 * Funkcja aktywacji neuronu
	 */
	protected NeuronActivationFunction function;

	/**
	 * Ważona suma sygnałów dopływających do neurony przez synapsy (wagi)
	 */
	protected double sum;

	/**
	 * Wagi neuronu łączącego go z warstwą poprzednią
	 */
	protected DoubleVector weights;

	/* ------------- METHODS -------------- */
	@SuppressWarnings("unchecked")
	public void initialize(C conf) {
		function.initialize(conf);
		initializeWeights(conf);
	}

	/**
	 * Inicjalizacja początkowa wag neuronu.
	 */
	@SuppressWarnings("static-access")
	public void initializeWeights(C conf) {
		MathUtil m = MathUtil.getInstance();
		for (int i = 0; i < conf.getNumberOfInputs() + 1; ++i) {
			weights.set(i, m.getRandomDouble(function.minReturnValue(),
					function.maxReturnValue()));
		}
	}

	/**
	 * Wyznaczenie sumy neuronu.
	 */
	public void computeSum(DoubleVector inputs) {
		double sum = 0;
		for (int i = 0; i < inputs.size(); ++i) {
			sum += inputs.get(i) * getWeights().get(i);
		}
		setSum(sum);
	}

	public void simulation(DoubleVector inputs) {
		computeSum(inputs);
		setOutput(getSum());
	}

	/**
	 * Modyfikacja wagi neuronu
	 * 
	 * @param indx
	 *            Indeks wagi
	 * @param delta
	 *            Przyrost
	 */
	public void updateWeight(int indx, double delta) {
		this.weights.set(indx, this.weights.get(indx) + delta);
	}

	/* ---------- SETTERS AND GETTERS --------- */
	/**
	 * @return wartość pola function
	 */
	public NeuronActivationFunction getFunction() {
		return function;
	}

	/**
	 * @param function
	 *            jest przypisywany do pola function
	 */
	public void setFunction(NeuronActivationFunction function) {
		this.function = function;
	}

	/**
	 * @return wartość pola sum
	 */
	public double getSum() {
		return sum;
	}

	/**
	 * @param sum
	 *            jest przypisywany do pola sum
	 */
	public void setSum(double sum) {
		this.sum = sum;
	}

	/**
	 * @return wartość pola weights
	 */
	public DoubleVector getWeights() {
		return weights;
	}

	/**
	 * @param weights
	 *            jest przypisywany do pola weights
	 */
	public void setWeights(DoubleVector weights) {
		this.weights = weights;
	}

}
