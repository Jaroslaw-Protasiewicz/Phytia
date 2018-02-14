package pl.phytia.ann.functions.error;

import pl.phytia.ann.networks.Network;
import pl.phytia.model.conf.functions.error.SSEFunctionConfiguration;
import pl.phytia.model.sets.DoubleVector;
import pl.phytia.model.sets.SupervisedDataSet;

/**
 * Błąd jako różnica wartości oczekiwanej i wyjścia sieci.
 * 
 * @author Jarosław Protasiewicz
 */
public class DiffErrorFunction extends
		ErrorFunction<SSEFunctionConfiguration, DiffErrorFunction> {

	private static final long serialVersionUID = -7463830664469351349L;

	@Override
	public double computeError(Network net, SupervisedDataSet set) {
		throw new UnsupportedOperationException("Metoda nie jest oprogramowana");
	}

	@Override
	public double computeError(Network net, DoubleVector pattern) {
		return (pattern.get(0) - net.getOutputs().get(0));
	}

}
