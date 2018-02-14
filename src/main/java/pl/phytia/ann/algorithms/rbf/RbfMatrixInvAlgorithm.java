package pl.phytia.ann.algorithms.rbf;

import java.util.Iterator;

import pl.phytia.ann.layers.AdderLayer;
import pl.phytia.ann.networks.RBFNetwork;
import pl.phytia.model.conf.algorithms.RbfAlgorithmConfiguration;
import pl.phytia.model.enums.EnumIterationType;
import pl.phytia.model.sets.DoubleVector;
import pl.phytia.model.sets.PatternPairVO;
import pl.phytia.ann.layers.RbfLayer;
import pl.phytia.model.sets.SupervisedDataSet;
import Jama.Matrix;

/**
 * Algorytm trningu sieci RBF dokonujący:
 * <li>inicjalizacji centrów sieci: dokładnej, losowej, metodą k-średnich;</>
 * <li>wyznaczenia wag sieci metoda pseudoinwersji.</><br>
 * <br>
 * 
 * @author Jarosław Protasiewicz
 */
public class RbfMatrixInvAlgorithm extends
		RbfBaseAlgorithm<RbfMatrixInvAlgorithm> {

	public RbfMatrixInvAlgorithm() {
		super();
	}

	public RbfMatrixInvAlgorithm(RbfAlgorithmConfiguration conf) {
		super(conf);
	}

	@SuppressWarnings("unchecked")
	public void training(RBFNetwork net, SupervisedDataSet set) {
		/*
		 * Pierwszy krok - dobór centrów i inicjalizacja sieci.
		 */
		net.reInitialize(getCenterSelectFunction().centerSelect(set));
		/*
		 * Drugi krok - dobór wag sieci.
		 */
		computeWeights(net, set);
		/*
		 * DoubleVector w = new DoubleVector(); w.add(-0.003868670608168999);
		 * w.add(1.0010122183753711); w.add(1.001012218375371);
		 * w.add(-0.003868670608168998);
		 * ((AdderLayer)net.getLayers().get(1)).getNeurons().get(0).setWeights(w);
		 */
	}

	public void updateParams(RBFNetwork net, SupervisedDataSet set) {
		throw new UnsupportedOperationException(
				"Metoda nie jest implementowana dla tej wersji algorytmu");
	}

	public void updateWeights(RBFNetwork net, DoubleVector inputs) {
		throw new UnsupportedOperationException(
				"Metoda nie jest implementowana dla tej wersji algorytmu");
	}

	public boolean checkStopCondition(RBFNetwork net, SupervisedDataSet set) {
		throw new UnsupportedOperationException(
				"Metoda nie jest implementowana dla tej wersji algorytmu");
	}

	/**
	 * Wyznaczenie wag sieci.
	 * 
	 * @param net
	 *            Sieć
	 * @param trainSet
	 *            Zbiór uczący.
	 */
	public void computeWeights(RBFNetwork net, SupervisedDataSet trainSet) {
		RbfLayer rbfLayer = (RbfLayer) net.getLayers().get(0);
		int size = rbfLayer.getNeurons().size();
		AdderLayer adderLayer = (AdderLayer) net.getLayers().get(1);
		Matrix G = new Matrix(trainSet.getSet().size(), size);
		Matrix G0 = new Matrix(size, size);
		Matrix y = new Matrix(trainSet.getSet().size(), 1);
		Matrix w = new Matrix(size, 1);
		/*
		 * Wyznaczenie macierzy wyjść funkcji bazowych oraz wektora wyjść sieci.
		 */
		trainSet.setEnumIterationType(EnumIterationType.SERIAL);
		Iterator<PatternPairVO> it = trainSet.iterator();
		int indx = 0;
		PatternPairVO pattern = null;
		while (it.hasNext()) {
			pattern = it.next();
			net.simulation(pattern.getInputs());
			fillMatrixRow(G, indx, rbfLayer.getOutputs());
			fillMatrixRow(y, indx, pattern.getOutputs());
			++indx;
		}
		/*
		 * Wyznaczenie macierzy regularyzacyjnej.
		 */

		DoubleVector centerIntput = null;
		for (int m = 0; m < size; ++m) {
			centerIntput = rbfLayer.getNeurons().get(m).getRadialFunction()
					.getCenter();
			net.simulation(centerIntput);
			fillMatrixRow(G0, m, rbfLayer.getOutputs());
		}
		/*
		 * Wyznaczenie macierzy odwrotnej - wag sieci. w = (GT*G-lambda*G)^-1 *
		 * GT*y
		 */
		Matrix GT = G.transpose();
		Matrix GTG = GT.times(G);
		G0 = G0.times(0.001);
		GTG = GTG.minusEquals(G0);
		Matrix GTGInv = GTG.inverse();
		Matrix m1 = GTGInv.times(GT);
		w = m1.times(y);
		/*
		 * Wypełnienie wag sieci
		 */
		DoubleVector weights = adderLayer.getNeurons().get(0).getWeights();
		for (int i = 0; i < weights.size(); ++i) {
			weights.set(i, w.get(i, 0));
		}
	}

}
