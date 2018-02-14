package pl.phytia.ann.functions.centerSelect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pl.phytia.model.conf.functions.centerSelect.CenterSelFunctionConfiguration;
import pl.phytia.model.enums.EnumIterationType;
import pl.phytia.model.sets.DoubleVector;
import pl.phytia.model.sets.PatternPairVO;
import pl.phytia.model.sets.SupervisedDataSet;

/**
 * Funkcja losowego doboru centrów ze zbioru treningowego.
 * 
 * @author jprotas
 * 
 */
public class RandomCenterFunction extends
		CenterSelectFunction<RandomCenterFunction> {

	private static final long serialVersionUID = -8677535062484444270L;

	public RandomCenterFunction() {
		super();
		config = new CenterSelFunctionConfiguration();
	}

	public RandomCenterFunction(CenterSelFunctionConfiguration conf) {
		super();
		initialize(conf);
	}

	@Override
	public List<DoubleVector> centerSelect(SupervisedDataSet trainSet) {
		float a = (float) config.getInitNumberOfCenters() / 100;
		int maxCentres = Math.round(a * trainSet.getSet().size());
		if (maxCentres < 1) {
			maxCentres = trainSet.getSet().size();
		}
		trainSet.setEnumIterationType(EnumIterationType.RANDOM);
		Iterator<PatternPairVO> it = trainSet.iterator();
		int indx = 0;
		List<DoubleVector> centers = new ArrayList<DoubleVector>();
		while (it.hasNext()) {
			if (++indx > maxCentres) {
				break;
			}
			centers.add(it.next().getInputs().clone());
		}
		return centers;
	}

}
