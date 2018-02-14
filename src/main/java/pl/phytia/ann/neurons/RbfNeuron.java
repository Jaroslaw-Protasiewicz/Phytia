package pl.phytia.ann.neurons;

import pl.phytia.ann.functions.neuron.RadialFunction;
import pl.phytia.ann.functions.neuron.RadialFunctionFactory;
import pl.phytia.model.conf.functions.neuron.RbfFunctionConfiguration;
import pl.phytia.model.sets.DoubleVector;

/**
 * Element radialny sieci RBF.
 * 
 * @author Jarosław Protasiewicz
 */
public class RbfNeuron extends Neuron<RbfNeuron, RbfFunctionConfiguration> {

	private static final long serialVersionUID = 2068976135184237657L;

	/**
	 * Funkcja radialna neuronu.
	 */
	private RadialFunction radialFunction;

	/**
	 * Konstruktor bezargumentowy.
	 */
	public RbfNeuron() {
		super();
	}

	/**
	 * Tworzy neuron i inicjalizuje go.
	 * 
	 * @param conf
	 *            Konfiguracja neuronu - funkcji radialnej.
	 * @param center
	 *            Centrum neuronu - funkcji radialnej.
	 */
	public RbfNeuron(RbfFunctionConfiguration conf, DoubleVector center) {
		super();
		radialFunction = RadialFunctionFactory.getRadialNeuronFunction(conf);
		initialize(conf);
		radialFunction.setCenter(center);
	}

	@SuppressWarnings("unchecked")
	public void initialize(RbfFunctionConfiguration conf) {
		radialFunction.initialize(conf);
	}

	public void simulation(DoubleVector inputs) {
		setOutput(getRadialFunction().computeOutput(inputs));
	}

	/**
	 * Modyfikuje centrum funkcji bazowej.
	 * 
	 * @param deltaCenter
	 *            Modyfikacja centrum.
	 */
	public void updateFunctionCenter(DoubleVector deltaCenter) {
		DoubleVector center = getRadialFunction().getCenter();
		for (int i = 0; i < deltaCenter.size(); ++i) {
			center.set(i, center.get(i) + deltaCenter.get(i));
		}
	}

	/**
	 * Modyfikuje rozmycie fukcji bazowej.
	 * 
	 * @param deltaWidth
	 *            Modyfikacja rozmycia.
	 */
	public void updateFunctionCenterWidth(double deltaWidth) {
		getRadialFunction().setWidth(
				getRadialFunction().getWidth() + deltaWidth);
	}

	public RadialFunction getRadialFunction() {
		return radialFunction;
	}

	public void setRadialFunction(RadialFunction radialFunction) {
		this.radialFunction = radialFunction;
	}

}
