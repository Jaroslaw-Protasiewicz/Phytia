package pl.phytia.model.conf;

import java.io.Serializable;

import pl.phytia.utils.StateFixer;

/**
 * Abstrakcyjna klasa bazowa dla klas - nośników parametrów konfiguracyjnych.
 * 
 * @param <S>
 *            Obiekt, którego stan jest odczytywany lub zapisywany
 * @author Jarosław Protasiewicz
 */
public abstract class Configuration<S extends Configuration> extends
		StateFixer<S> implements Serializable {

	/**
	 * Domyślny konstruktor
	 */
	public Configuration() {
		super();
	}
}
