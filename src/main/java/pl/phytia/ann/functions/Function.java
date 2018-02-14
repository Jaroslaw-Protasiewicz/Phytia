package pl.phytia.ann.functions;

import java.io.Serializable;

import pl.phytia.core.Configurable;
import pl.phytia.model.conf.Configuration;
import pl.phytia.utils.StateFixer;

/**
 * Obiekt zawierający parametry konfiguracyjne
 * 
 * @author Jarosław Protasiewicz
 * 
 * @param <C>
 *            Typ obiektu z parametrami konfiguracyjnymi
 * @param <S>
 *            Typ obietu zwaracanego przez <code>this</code>
 */

public abstract class Function<C extends Configuration, S extends Function>
		extends StateFixer<S> implements Configurable<C>, Serializable {

	protected C config;

	public void initialize(C conf) {
		this.config = conf;
	}

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
