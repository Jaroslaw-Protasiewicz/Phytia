package pl.phytia.model.conf.functions.error;

import pl.phytia.model.conf.functions.neuron.NeuronFunctionConfiguration;

/**
 * Konfiguracja funkcji błędu średniokwadratowego.
 * 
 * @author Jarosław Protasiewicz
 */
public final class SSEFunctionConfiguration extends
		NeuronFunctionConfiguration<SSEFunctionConfiguration> {

	private static final long serialVersionUID = 1717538422724951225L;

	/**
	 * Domyślny konstruktor
	 */
	public SSEFunctionConfiguration() {
		super();
		this.errorSensitivity = 0.000001;
	}

	/**
	 * Parametr określający czułość - dokładność błędu. <br>
	 * Wszystkie różnice pomiędzy wartością oczekiwaną a odpowiedzią sieci,
	 * będące poniżej tej wartości są ignorowane. TODO: usunąć!
	 */
	private double errorSensitivity;

	/**
	 * @return wartość pola errorSensitivity
	 */
	public double getErrorSensitivity() {
		return errorSensitivity;
	}

	/**
	 * @param errorSensitivity
	 *            jest przypisywany do pola errorSensitivity
	 */
	public void setErrorSensitivity(double errorSensitivity) {
		this.errorSensitivity = errorSensitivity;
	}
}
