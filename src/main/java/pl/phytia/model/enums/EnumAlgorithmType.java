package pl.phytia.model.enums;

/**
 * Typy algorytmów.
 * 
 * @author Jarosław Protasiewicz
 */
public enum EnumAlgorithmType {

	/**
	 * Alogrytm wstecznej propagacji błedu.
	 */
	BackProp("Alogrytm wstecznej propagacji błedu", "BackProp"),

	/**
	 * Alogrytm wstecznej propagacji błedu z momentum.
	 */
	BackPropMomentum("Alogrytm wstecznej propagacji błedu z momentum",
			"BackPropMomentum"),

	/**
	 * Szybki wstecznej propagacji błedu.
	 */
	QuickProp("Szybki wstecznej propagacji błedu", "QuickProp");

	/**
	 * Konstruktor bezparametrowy.
	 */
	EnumAlgorithmType() {
	}

	/**
	 * Konstruktor typu algorytmu.
	 * 
	 * @param name
	 *            Nazwa pełna.
	 * @param type
	 *            Nazwa krótka.
	 */
	EnumAlgorithmType(String name, String type) {
		this.name = name;
		this.type = type;
	}

	/**
	 * Nazwa sieci neuronowej.
	 */
	private String name;

	/**
	 * Nazwa skrócona(typ) sieci neuronowej.
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
