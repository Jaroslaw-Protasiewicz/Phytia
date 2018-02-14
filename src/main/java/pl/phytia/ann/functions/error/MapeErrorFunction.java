package pl.phytia.ann.functions.error;

import java.util.Iterator;

import pl.phytia.ann.layers.Layer;
import pl.phytia.ann.networks.Network;
import pl.phytia.model.conf.functions.error.SSEFunctionConfiguration;
import pl.phytia.model.sets.DoubleVector;
import pl.phytia.model.sets.PatternPairVO;
import pl.phytia.model.sets.SupervisedDataSet;

public class MapeErrorFunction extends
		ErrorFunction<SSEFunctionConfiguration, MapeErrorFunction> {

	private static final long serialVersionUID = -6245800424863251286L;

	@Override
	public double computeError(Network net, SupervisedDataSet set) {
		double mape = 0.0d;
		Iterator<PatternPairVO> it = set.iterator();
		while (it.hasNext()) {
			PatternPairVO pattern = it.next();
			DoubleVector in = pattern.getInputs();
			DoubleVector out = pattern.getOutputs();
			net.simulation(in);
			DoubleVector netOut = net.getOutputs();
			for (int i = 0; i < netOut.size(); ++i) {
				mape += Math.abs((out.get(i) - netOut.get(i)) / out.get(i));
			}
			// System.out.println(mape);
		}
		mape = mape / set.getSet().size() * 100;
		setError(mape);
		return mape;
	}

	@Override
	public double computeError(Network net, DoubleVector pattern) {
		DoubleVector netOut = ((Layer) net.getLayers().get(
				net.getLayers().size() - 1)).getOutputs();
		for (int i = 0; i < netOut.size(); ++i) {
			setError(getError()
					+ Math.abs((pattern.get(i) - netOut.get(i))
							/ pattern.get(i)));
		}
		return getError();
	}

}
