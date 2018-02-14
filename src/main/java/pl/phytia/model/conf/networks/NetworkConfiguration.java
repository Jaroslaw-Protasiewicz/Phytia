package pl.phytia.model.conf.networks;

import pl.phytia.model.conf.Configuration;
import pl.phytia.model.enums.EnumNetworkType;

/**
 * Abstrakcyjna klasa - nośnik parametrów konfiguracyjncyh sieci neuronowej.
 * 
 * @param <S>
 *            Obiekt, którego stan jest odczytywany lub zapisywany
 * @author Jarosław Protasiewicz
 */
public abstract class NetworkConfiguration<S extends NetworkConfiguration>
		extends Configuration<S> {

	/**
	 * Typ sieci neuronowej<br>
	 * Pole pełni jedynie funkcję informacyjną.
	 */
	private EnumNetworkType typeOfNetwork;

	/**
	 * @return wartość pola typeOfNetwork
	 */
	public EnumNetworkType getTypeOfNetwork() {
		return typeOfNetwork;
	}

	/**
	 * @param typeOfNetwork
	 *            jest przypisywany do pola typeOfNetwork
	 */
	public void setTypeOfNetwork(EnumNetworkType typeOfNetwork) {
		this.typeOfNetwork = typeOfNetwork;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(this.getClass().toString() + "\n");
		sb.append("typeOfNetwork = "
				+ (typeOfNetwork != null ? typeOfNetwork.toString() : "")
				+ "\n");
		sb.append(super.toString());
		return sb.toString();
	}

}
