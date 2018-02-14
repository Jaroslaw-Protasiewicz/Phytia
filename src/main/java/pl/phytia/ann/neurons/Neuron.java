package pl.phytia.ann.neurons;

import java.io.Serializable;

import pl.phytia.core.Configurable;
import pl.phytia.core.Simulatable;
import pl.phytia.model.conf.functions.neuron.NeuronFunctionConfiguration;
import pl.phytia.utils.StateFixer;

/**
 * Abstrakcyjny neuron.
 * 
 * @param <S>
 *            Typ obietu zwaracanego przez <code>this</code>.
 * @param <C>
 *            Typ obiektu konfiguracji.
 * 
 * @author Jarosław Protasiewicz
 */
public abstract class Neuron<S extends Neuron, C extends NeuronFunctionConfiguration>
		extends StateFixer<S> implements Configurable<C>, Simulatable,
		Serializable {

	/**
	 * Wyjście neuronu.
	 */
	protected double output;

	/* ---------- SETTERS AND GETTERS --------- */
	/**
	 * @return wartość pola output
	 */
	public double getOutput() {
		return output;
	}

	/**
	 * @param output
	 *            jest przypisywany do pola output
	 */
	public void setOutput(double output) {
		this.output = output;
	}

}
