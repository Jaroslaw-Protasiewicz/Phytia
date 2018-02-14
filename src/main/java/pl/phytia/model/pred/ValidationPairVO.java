package pl.phytia.model.pred;

import pl.phytia.model.sets.SupervisedDataSet;

/**
 * Nośnik danych dla procedury walidacji krzyżowej.
 * 
 * @author Jarosław Protasiewicz
 */
public class ValidationPairVO {

	/**
	 * Zbiór treningowy.
	 */
	private SupervisedDataSet traningSet;

	/**
	 * Zbiór walidacyjny.
	 */
	private SupervisedDataSet validationSet;

	/**
	 * Klasa dnia.
	 */
	private String dayClass;

	/**
	 * Konstruktor bezarumentowy.
	 */
	public ValidationPairVO() {
		super();
	}

	/**
	 * Konstruktor.
	 * 
	 * @param traningSet
	 *            Zbiór treningowy.
	 * @param validationSet
	 *            Zbiór walidacyjny.
	 * @param dayClass
	 *            Klasa dnia.
	 */
	public ValidationPairVO(SupervisedDataSet traningSet,
			SupervisedDataSet validationSet, String dayClass) {
		super();
		this.traningSet = traningSet;
		this.validationSet = validationSet;
		this.dayClass = dayClass;
	}

	/**
	 * @return wartość pola traningSet
	 */
	public SupervisedDataSet getTraningSet() {
		return traningSet;
	}

	/**
	 * @param traningSet
	 *            jest przypisywany do pola traningSet
	 */
	public void setTraningSet(SupervisedDataSet traningSet) {
		this.traningSet = traningSet;
	}

	/**
	 * @return wartość pola validationSet
	 */
	public SupervisedDataSet getValidationSet() {
		return validationSet;
	}

	/**
	 * @param validationSet
	 *            jest przypisywany do pola validationSet
	 */
	public void setValidationSet(SupervisedDataSet validationSet) {
		this.validationSet = validationSet;
	}

	/**
	 * @return wartość pola dayClass
	 */
	public String getDayClass() {
		return dayClass;
	}

	/**
	 * @param dayClass
	 *            jest przypisywany do pola dayClass
	 */
	public void setDayClass(String dayClass) {
		this.dayClass = dayClass;
	}
}
