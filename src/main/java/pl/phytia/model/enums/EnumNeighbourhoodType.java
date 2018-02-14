package pl.phytia.model.enums;

/**
 * Typy sąsiedztwa.
 * 
 * @author Jarosław Protasiewicz
 */
public enum EnumNeighbourhoodType {

	/**
	 * Sąsiedztwo gaussowskie.
	 */
	Gaussian("GAUSSIAN_NEIGHBOURHOOD", "Sąsiedztwo gaussowskie"),

	/**
	 * Sąsiedztwo prostokątne.
	 */
	Rectangular("RECTANGULAR_NEIGHBOURHOOD", "Sąsiedztwo prostokątne");

	/**
	 * Konstruktor typu.
	 * 
	 * @param type
	 *            Typ - skrót nazwy.
	 * @param name
	 *            Opisowa nazwa typu.
	 */
	EnumNeighbourhoodType(String type, String name) {
		this.type = type;
		this.name = name;
	}

	/**
	 * Typ funkcji sąsiedztwa.
	 */
	private String type;

	/**
	 * Nazwa funkcji sąsiedztwa.
	 */
	private String name;

	/**
	 * @return wartość pola name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return wartość pola type
	 */
	public String getType() {
		return type;
	}

	static {
		EnumPersistenceDelegate.installFor(values());
	}
}
