package pl.phytia.model.enums;

/**
 * Klasy dni świątecznych.
 * 
 * @author Jarosław Protasiewicz
 */
public enum EnumHolidayClass {

	/**
	 * Nowy Rok
	 */
	H1NR,

	/**
	 * Pierwszy dzień Świąt Wielkanocy
	 */
	H1WN,

	/**
	 * Drugi dzień Świąt Wielkanocy
	 */
	H2WN,

	/**
	 * Święto pracy - 1 maja
	 */
	H1M,

	/**
	 * Świeto Konstytucji 3-go Maja
	 */
	H3M,

	/**
	 * Boże Ciało
	 */
	HBC,

	/**
	 * 15 sierpnia - Święto Wojska Polskiego
	 */
	H15S,

	/**
	 * Dzień Wszystkich Świętych
	 */
	H1L,

	/**
	 * 11 listopada - Święto Niepodległości
	 */
	H11L,

	/**
	 * Dzień 23 godzinny
	 */
	H23P,

	/**
	 * Dzień 25-cio godzinny
	 */
	H25P,

	/**
	 * Wigilia Świąt Bożego Narodzenia
	 */
	HWG,

	/**
	 * Pierwszy dzień Świąt Bożego Narodzenia
	 */
	H1BN,

	/**
	 * Drugi dzień Świąt Bożego Narodzenia
	 */
	H2BN,

	/**
	 * Sylwester
	 */
	H31G;

	static {
		EnumPersistenceDelegate.installFor(values());
	}
}