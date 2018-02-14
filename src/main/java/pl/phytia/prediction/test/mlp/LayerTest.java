package pl.phytia.prediction.test.mlp;

import pl.phytia.ann.layers.Layer;
import pl.phytia.ann.layers.MLPLayer;
import pl.phytia.model.conf.functions.neuron.SUnipolarFunctionConfiguration;
import pl.phytia.model.conf.layers.MLPLayerConfiguration;
import pl.phytia.model.sets.DoubleVector;

public class LayerTest {

	/**
	 * @param args
	 *            aa
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		MLPLayerConfiguration c = new MLPLayerConfiguration<SUnipolarFunctionConfiguration>();
		c.setNeuronFunctionConf(new SUnipolarFunctionConfiguration());
		c.setNumberOfNeurons(1);
		c.setNumberOfInputs(2);
		Layer l = new MLPLayer(c);
		DoubleVector inputs = new DoubleVector(2);
		inputs.add(1.0);
		inputs.add(1.0);
		l.simulation(inputs);
	}

}
