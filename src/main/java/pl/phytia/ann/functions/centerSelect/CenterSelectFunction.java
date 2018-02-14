package pl.phytia.ann.functions.centerSelect;

import java.util.List;

import pl.phytia.ann.functions.Function;
import pl.phytia.model.conf.functions.centerSelect.CenterSelFunctionConfiguration;
import pl.phytia.model.sets.DoubleVector;
import pl.phytia.model.sets.SupervisedDataSet;

/**
 * Abstrakcyjna funkcja dobierajaca centra sieci RBF.
 * 
 * @author Jarosław Protasiewicz
 * 
 * @param <S>
 *            Typ funkcji.
 */
public abstract class CenterSelectFunction<S extends CenterSelectFunction>
		extends Function<CenterSelFunctionConfiguration, S> {

	public CenterSelectFunction() {
		super();
	}

	/**
	 * Dobór centrów sieci RBF.
	 * 
	 * @param trainSet
	 *            Zbiór treningowy.
	 * 
	 * @return Lista centrów.
	 */
	public abstract List<DoubleVector> centerSelect(SupervisedDataSet trainSet);
}
