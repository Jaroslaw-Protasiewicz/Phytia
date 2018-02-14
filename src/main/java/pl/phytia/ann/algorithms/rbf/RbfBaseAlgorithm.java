package pl.phytia.ann.algorithms.rbf;

import pl.waw.ibspan.phytia.ann.algorithms.Algorithm;
import pl.waw.ibspan.phytia.ann.functions.centerSelect.CenterFunctionFactory;
import pl.waw.ibspan.phytia.ann.functions.centerSelect.CenterSelectFunction;
import pl.waw.ibspan.phytia.ann.networks.RBFNetwork;
import pl.waw.ibspan.phytia.model.conf.algorithms.RbfAlgorithmConfiguration;
import pl.waw.ibspan.phytia.model.sets.DoubleVector;
import pl.waw.ibspan.phytia.model.sets.SupervisedDataSet;
import Jama.Matrix;

/**
 * Abstrakcyjny algorytm treningu sieci RBF.
 * 
 * @author Jarosław Protasiewicz
 * 
 * @param <S>
 *            Typ algorytmu.
 */
public abstract class RbfBaseAlgorithm<S extends RbfBaseAlgorithm> extends
		Algorithm<S, RbfAlgorithmConfiguration, SupervisedDataSet, RBFNetwork> {

	/**
	 * Funkcja doboru centrów.
	 */
	private CenterSelectFunction centerSelectFunction;

	public RbfBaseAlgorithm() {
		super();
	}

	public RbfBaseAlgorithm(RbfAlgorithmConfiguration conf) {
		super();
		centerSelectFunction = CenterFunctionFactory.getCenterFunction(conf
				.getCenterSelFunctionConf());
	}

	/**
	 * Wypełnia kolumnę macierzy.
	 * 
	 * @param m
	 *            Macierz.
	 * @param colIdx
	 *            Indeks kolumny macierzy do wypełnienia.
	 * @param v
	 *            Wektor danych, którymi będzie wypelniania macierz.
	 */
	protected void fillMatrixColumn(Matrix m, int colIdx, DoubleVector v) {
		for (int i = 0; i < v.size(); ++i) {
			m.set(i, colIdx, v.get(i));
		}
	}

	/**
	 * Wypełnia wiersz macierzy.
	 * 
	 * @param m
	 *            Macierz.
	 * @param rowIdx
	 *            Indeks wiersza macierzy do wypełnienia.
	 * @param v
	 *            Wektor danych, którymi będzie wypelniania macierz.
	 */
	protected void fillMatrixRow(Matrix m, int rowIdx, DoubleVector v) {
		for (int i = 0; i < v.size(); ++i) {
			m.set(rowIdx, i, v.get(i));
		}
	}

	public CenterSelectFunction getCenterSelectFunction() {
		return centerSelectFunction;
	}

}
