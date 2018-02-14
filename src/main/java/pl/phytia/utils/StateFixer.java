package pl.phytia.utils;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.apache.commons.beanutils.BeanUtils;

import pl.phytia.core.Persistable;
import pl.phytia.errors.ConfigurationException;

/**
 * Abstrakcyjna klasa narzędziowa odpowiedzialna za zapis / odczyt stanu obiektu
 * 
 * @param <S>
 *            Obiekt, którego stan jest odczytywany lub zapisywany
 * @author Jarosław Protasiewicz
 */
public abstract class StateFixer<S> implements Persistable {

	/**
	 * Domyślny konstruktor
	 */
	public StateFixer() {
		super();
	}

	public void loadState(String state) {
		try {
			BeanUtils.copyProperties(this, defaultLoad(state));
		} catch (Exception e) {
			throw new ConfigurationException("Błąd odczytu konfiguracji");
		}
	}

	public String storeState() {
		return defaultStore(this);
	}

	/**
	 * Domyślny odczyt stanu obiektu za pomocą mechanizmu serializacji.
	 * 
	 * @param state
	 *            Stan obiektu w postaci ciągu znaków.
	 * @return Obiekt, którego stan został odczytany
	 * @throws ConfigurationException
	 *             Wyjątek konfiguracji
	 */
	@SuppressWarnings("unchecked")
	protected S defaultLoad(String state) throws ConfigurationException {
		S object = null;
		try {
			XMLDecoder xdec = new XMLDecoder(new ByteArrayInputStream(state
					.getBytes()));
			object = (S) xdec.readObject();
			xdec.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ConfigurationException(
					"Błąd podczas deserializacji obiektu", e);
		}
		return object;
	}

	/**
	 * Domyślny zapis stanu obiektu za pomocą mechanizmu serializacji.
	 * 
	 * @param name
	 *            Obiekt, którego stan będzie serializowany.
	 * @return Stan obiektu uzyskany w wyniku seralizacji.
	 * @throws ConfigurationException
	 *             Wyjątek konfiguracji
	 */
	protected String defaultStore(StateFixer<S> name)
			throws ConfigurationException {
		String out = null;
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			XMLEncoder xenc = new XMLEncoder(os);
			xenc.writeObject(name);
			xenc.close();
			out = os.toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ConfigurationException(
					"Błąd podczas serializacji obiektu", e);
		}
		return out;
	}
}
