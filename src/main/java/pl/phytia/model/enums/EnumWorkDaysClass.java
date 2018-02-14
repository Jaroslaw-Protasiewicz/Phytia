package pl.phytia.model.enums;

/**
 * Klasy dni roboczych.
 * 
 * @author Jarosław Protasiewicz
 */
public enum EnumWorkDaysClass {

	/**
	 * Poniedziałek
	 */
	D1,

	/**
	 * Wtorek
	 */
	D2,

	/**
	 * Środa
	 */
	D3,

	/**
	 * Czwartek
	 */
	D4,

	/**
	 * Piątek
	 */
	D5,

	/**
	 * Sobota
	 */
	D6,

	/**
	 * Niedziela
	 */
	D7;

	static {
		EnumPersistenceDelegate.installFor(values());
	}
}
