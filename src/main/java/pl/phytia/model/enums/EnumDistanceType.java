package pl.phytia.model.enums;

/**
 * Typy miar odległości.
 * 
 * @author Jarosław Protasiewicz
 */
public enum EnumDistanceType {

	Euclidean("Euklidesowa miara odległości", "EUCLIDEAN_DISTANCE"),

	Manhattan("Miara odległości Manhatan", "MANHATTAN_DISTANCE"),

	Chebyshev("Miara odległości Czybyszewa", "CHEBYSHEV_DISTANCE");

	/**
	 * Konstruktor bezparametrowy.
	 */
	EnumDistanceType() {
	}

	/**
	 * Konstruktor typu sieci neronowej.
	 * 
	 * @param name
	 *            Nazwa sieci.
	 */
	EnumDistanceType(String name, String type) {
		this.name = name;
		this.type = type;
	}

	/**
	 * Nazwa miary odległości.
	 */
	private String name;

	/**
	 * Nazwa skrócona(typ) miary odległości.
	 */
	private String type;

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
