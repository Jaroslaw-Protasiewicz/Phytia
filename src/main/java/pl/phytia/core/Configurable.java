package pl.phytia.core;

import pl.phytia.model.conf.Configuration;

/**
 * Intefejs dla klas, których obiekty są inicjalizowane za pomocą obiektów
 * configuracji.
 * 
 * @author Jarosław Protasiewicz
 */
public interface Configurable<C extends Configuration> {

	/**
	 * Inicjalizacja obiektu na podstawie obiektu configuracji
	 * 
	 * @param conf
	 *            Obiekt zawierający parametry konfiguracyjne
	 */
	void initialize(C conf);
}
