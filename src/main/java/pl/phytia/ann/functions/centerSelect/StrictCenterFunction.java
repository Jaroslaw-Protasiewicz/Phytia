package pl.phytia.ann.functions.centerSelect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pl.phytia.model.conf.functions.centerSelect.CenterSelFunctionConfiguration;
import pl.phytia.model.sets.DoubleVector;
import pl.phytia.model.sets.PatternPairVO;
import pl.phytia.model.sets.SupervisedDataSet;

/**
 * Funkcja dokładnego doboru centrów ze zbioru treningowego danych.
 * 
 * @author Jarosław Protasiewicz
 */
public class StrictCenterFunction extends
		CenterSelectFunction<StrictCenterFunction> {

	private static final long serialVersionUID = 9128846249322977599L;

	public StrictCenterFunction() {
		super();
		config = new CenterSelFunctionConfiguration();
	}

	public StrictCenterFunction(CenterSelFunctionConfiguration conf) {
		super();
		initialize(conf);
	}

	@Override
	public List<DoubleVector> centerSelect(SupervisedDataSet trainSet) {
		Iterator<PatternPairVO> it = trainSet.iterator();
		List<DoubleVector> centers = new ArrayList<DoubleVector>();
		while (it.hasNext()) {
			centers.add(it.next().getInputs().clone());
		}
		return centers;
	}
}
