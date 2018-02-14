package pl.phytia.model.enums;

/**
 * Typy współczynników uczenia.
 * 
 * @author Jarosław Protasiewicz
 */
public enum EnumLearningRatioType {

	/**
	 * Stały współczynnik uczenia.
	 */
	CONSTANT,

	/**
	 * Adapacyjny współczynnik uczenia (sieć MLP).
	 */
	ADAPTATIVE,

	/**
	 * Liowo zmienny współczynnik uczenia (sieć SOM).
	 */
	LINEAR,

	/**
	 * Ekspotencjalnie zmienny współczynnik uczenia (sieć SOM).
	 */
	EXPONENTIAL;

	static {
		EnumPersistenceDelegate.installFor(values());
	}
}
