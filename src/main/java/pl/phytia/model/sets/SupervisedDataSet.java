package pl.phytia.model.sets;

import java.util.ArrayList;
import java.util.List;

import pl.phytia.model.enums.EnumIterationType;

/**
 * Zbiór danych zawierający wzroce dla uczenia nadzorowanego.
 * 
 * @author Jarosław Protasiewicz
 */
public class SupervisedDataSet extends
		DataSet<SupervisedDataSet, PatternPairVO> {

	/**
	 * Konstruktor zbioru.
	 * 
	 * @param trainSet
	 *            Zbiór uczący - dowlny zbiór danych.
	 * @param enumIterationType
	 *            Rodzaj iteracji po zbiorze.
	 */
	public SupervisedDataSet(List<PatternPairVO> trainSet,
			EnumIterationType enumIterationType) {
		super();
		if (trainSet != null) {
			setSet(trainSet, false);
		} else {
			setSet(new ArrayList<PatternPairVO>(), false);
		}
		setEnumIterationType(enumIterationType);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {

		ArrayList<PatternPairVO> list = (ArrayList<PatternPairVO>) this
				.getSet();
		SupervisedDataSet setColned = new SupervisedDataSet(
				(ArrayList<PatternPairVO>) list.clone(),
				EnumIterationType.RANDOM);
		return setColned;
	}
}
