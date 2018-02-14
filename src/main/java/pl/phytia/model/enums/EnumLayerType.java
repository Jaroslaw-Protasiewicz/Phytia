package pl.phytia.model.enums;

/**
 * Typy warstw sieci neuronowych.
 * 
 * @author Jarosław Protasiewicz
 */
public enum EnumLayerType {

	/**
	 * Warstwa wejściowa.
	 */
	INPUT("INPUT", "Warstwa wejściowa"),

	/**
	 * Warstwa ukryta.
	 */
	HIDDEN("HIDDEN", "Warstwa ukryta"),

	/**
	 * Warstwa wyjściowa.
	 */
	OUTPUT("OUTPUT", "Warstwa wyjściowa"),

	/**
	 * Warstwa wejściowa i wyjściowa.
	 */
	INOUT("INOUT", "Warstwa wejściowy i wyjściowa");

	/**
	 * Konstruktor bezparametrowy.
	 */
	EnumLayerType() {
	}

	/**
	 * Konstruktor typu.
	 * 
	 * @param type
	 *            Typ - skrót nazwy.
	 * @param name
	 *            Nazwa opisowa.
	 */
	EnumLayerType(String type, String name) {
		this.type = type;
		this.name = name;
	}

	/**
	 * Typ warstwy.
	 */
	private String type;

	/**
	 * Nazwa warstwy.
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
