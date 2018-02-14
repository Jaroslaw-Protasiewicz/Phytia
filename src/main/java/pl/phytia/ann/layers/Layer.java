package pl.phytia.ann.layers;

import pl.phytia.core.Configurable;
import pl.phytia.core.Simulatable;
import pl.phytia.model.conf.Configuration;
import pl.phytia.model.sets.DoubleVector;
import pl.phytia.utils.StateFixer;

/**
 * Abstrakcyjna klasa reprezentująca warstwę sieci neuronowej.
 * 
 * @param <S>
 *            Typ obietu zwaracanego przez <code>this</code>
 * @author Jarosław Protasiewicz
 */
public abstract class Layer<S extends Layer, C extends Configuration> extends
		StateFixer<S> implements Configurable<C>, Simulatable {

	/* ------------ FIELDS --------------- */
	/**
	 * Konfiguracja warstwy sieci neuronowej.
	 */
	protected C config;

	/* ------------- METHODS -------------- */
	/**
	 * Domyślny konstruktor
	 */
	public Layer() {
		super();
	}

	/**
	 * Wyznacza minimalną wartość zwracaną przez warstwę.
	 * 
	 * @return Wartość minimalna na wyjściu z warstwy.
	 */
	public abstract double minReturnValue();

	/**
	 * Wyznacza maksymalna wartość zwracaną przez warstwę.
	 * 
	 * @return Wartość maksymalna na wyjściu z warstwy.
	 */
	public abstract double maxReturnValue();

	/**
	 * Zwaraca wyścia warstwy sieci neuronowej
	 * 
	 * @return Wektor wyjść.
	 */
	public abstract DoubleVector getOutputs();

	/*------------ GETTERS AND SETTERS ------------ */
	/**
	 * @return wartość pola config
	 */
	public C getConfig() {
		return config;
	}

	/**
	 * @param config
	 *            jest przypisywany do pola config
	 */
	public void setConfig(C config) {
		this.config = config;
	}

}
