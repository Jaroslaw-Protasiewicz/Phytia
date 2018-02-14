package pl.phytia.model.sets;

import java.util.ArrayList;
import java.util.List;

import pl.phytia.model.enums.EnumIterationType;

/**
 * Zbiór danych zawierający wzroce dla uczenia nienadzorowanego.
 * 
 * @author Jarosław Protasiewicz
 */
public class UnsupervisedDataSet extends
		DataSet<UnsupervisedDataSet, ClassPatternVO> {

	/**
	 * Konstruktor.
	 * 
	 * @param trainSet
	 *            Zbiór uczący - dowlny zbiór danych.
	 * @param enumIterationType
	 *            Rodzaj iteracji po zbiorze.
	 */
	public UnsupervisedDataSet(List<ClassPatternVO> trainSet,
			EnumIterationType enumIterationType) {
		super();
		if (trainSet != null) {
			setSet(trainSet, false);
		} else {
			setSet(new ArrayList<ClassPatternVO>(), false);
		}
		setEnumIterationType(enumIterationType);
	}

	/**
	 * Ustawia nazwę klasy dla zadanego wzora ze zbioru.
	 * 
	 * @param patternId
	 *            Identyfikator wzorca.
	 * @param className
	 *            Nazwa klasy.
	 */
	public void setPatternClassName(long patternId, String className) {
		ClassPatternVO p = getSet().get((int) patternId);
		p.setClassName(className);
	}
}
