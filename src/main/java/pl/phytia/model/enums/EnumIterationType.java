package pl.phytia.model.enums;

/**
 * Definicja typów iteracji po zbiorach.
 * 
 * @author Jarosław Protasiewicz
 */
public enum EnumIterationType {

	/**
	 * Iteracja sekwencyjna.
	 */
	SERIAL,
	/**
	 * Iteracja losowa.
	 */
	RANDOM;

	static {
		EnumPersistenceDelegate.installFor(values());
	}
}
