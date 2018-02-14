package pl.phytia.ann.neurons;

import pl.phytia.model.conf.functions.neuron.NeuronFunctionConfiguration;
import pl.phytia.model.sets.DoubleVector;

/**
 * Element Adaline - liniowy, adaptacyjny neuron.
 * 
 * @param <C>
 *            Typ obiektu konfiguracji.
 * @author Jarosław Protasiewicz
 */
public final class Adaline<C extends NeuronFunctionConfiguration> extends
		Adder<Adaline, C> {

	private static final long serialVersionUID = -2323517349908459417L;

	@Override
	public void simulation(DoubleVector inputs) {
		// TODO Auto-generated method stub
		super.simulation(inputs);
	}
}
