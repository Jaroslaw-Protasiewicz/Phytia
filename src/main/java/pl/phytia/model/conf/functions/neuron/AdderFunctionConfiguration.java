package pl.phytia.model.conf.functions.neuron;

import pl.phytia.model.enums.EnumFunctionType;

/**
 * Konfiguracja sumatora.
 * 
 * @author Jarosław Protasiewicz
 */
public class AdderFunctionConfiguration extends
		NeuronFunctionConfiguration<AdderFunctionConfiguration> {

	private static final long serialVersionUID = 6688416274313644360L;

	public AdderFunctionConfiguration() {
		super();
		setTypeOfFunction(EnumFunctionType.Pipe);
	}

}
