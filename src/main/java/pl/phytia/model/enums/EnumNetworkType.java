package pl.phytia.model.enums;

/**
 * Typy i nazwy sieci neuronowych.
 * 
 * @author Jarosław Protasiewicz
 */
public enum EnumNetworkType {

	/**
	 * Wielowarstwowa sieć perceptronowa.
	 */
	MLP("Wielowarstwowa sieć perceptronowa", "MLP"),

	/**
	 * Samoorganizująca się mapa cech.
	 */
	SOM("Samoorganizująca się mapa cech", "SOM"),

	/**
	 * Sieć o radialnych funkcjach bazowych.
	 */
	RBF("Sieć o radialnych funkcjach bazowych", "RBF");

	/**
	 * Konstruktor bezparametrowy.
	 */
	EnumNetworkType() {
	}

	/**
	 * Konstruktor typu sieci neronowej.
	 * 
	 * @param name
	 *            Nazwa sieci.
	 */
	EnumNetworkType(String name, String type) {
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
