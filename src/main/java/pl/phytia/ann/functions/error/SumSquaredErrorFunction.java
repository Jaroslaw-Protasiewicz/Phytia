package pl.phytia.ann.functions.error;

import java.util.Iterator;

import pl.phytia.ann.layers.Layer;
import pl.phytia.ann.networks.Network;
import pl.phytia.model.conf.functions.error.SSEFunctionConfiguration;
import pl.phytia.model.sets.DoubleVector;
import pl.phytia.model.sets.PatternPairVO;
import pl.phytia.model.sets.SupervisedDataSet;

/**
 * Zsumowany błąd średniokrwadratowy.
 * 
 * @author Jarosław Protasiewicz
 */
public final class SumSquaredErrorFunction extends
		ErrorFunction<SSEFunctionConfiguration, SumSquaredErrorFunction> {

	private static final long serialVersionUID = -4204238024247180238L;

	/**
	 * Domyślny konstruktor.
	 */
	public SumSquaredErrorFunction() {
		super();
		this.config = new SSEFunctionConfiguration();
	}

	/**
	 * Konstruktor.
	 * 
	 * @param conf
	 *            Konfiguracja funkcji.
	 */
	public SumSquaredErrorFunction(SSEFunctionConfiguration conf) {
		super();
		this.config = conf;
	}

	@Override
	public double computeError(Network net, SupervisedDataSet set) {
		double sse = 0.0d;
		Iterator<PatternPairVO> it = set.iterator();
		while (it.hasNext()) {
			PatternPairVO pattern = it.next();
			DoubleVector in = pattern.getInputs();
			DoubleVector out = pattern.getOutputs();
			net.simulation(in);
			DoubleVector netOut = ((Layer) net.getLayers().get(
					net.getLayers().size() - 1)).getOutputs();
			for (int i = 0; i < netOut.size(); ++i) {
				sse += Math.pow(out.get(i) - netOut.get(i), 2);
			}
		}
		setError(sse);
		return sse;
	}

	/*
	 * @Override public double computeError(Network net, DataSetGK set) { double
	 * sse = 0.0d; set.reset(); while (set.hasNext()) {
	 * net.simulation(set.getNextInput()); DoubleVectorGK netOut = ((Layer)
	 * net.getLayers().get(net.getLayers().size() -1)).getOutputs();
	 * DoubleVectorGK pattern = set.getNextOutput(); for (int i = 0; i <
	 * netOut.size(); ++i) { sse += Math.pow(pattern.get(i) - netOut.get(i), 2); } }
	 * sse = Math.sqrt(sse); setError(sse); set.reset(); return sse; }
	 */

	@Override
	public double computeError(Network net, DoubleVector pattern) {
		DoubleVector netOut = ((Layer) net.getLayers().get(
				net.getLayers().size() - 1)).getOutputs();
		for (int i = 0; i < netOut.size(); ++i) {
			setError(getError() + Math.pow(pattern.get(i) - netOut.get(i), 2));
		}
		return getError();
	}
}
