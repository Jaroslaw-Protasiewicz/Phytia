package pl.phytia.ann.networks;

import java.util.List;

import pl.phytia.ann.layers.Layer;
import pl.phytia.core.Configurable;
import pl.phytia.core.Simulatable;
import pl.phytia.model.conf.networks.NetworkConfiguration;
import pl.phytia.model.sets.DoubleVector;
import pl.phytia.model.sets.SupervisedDataSet;
import pl.phytia.utils.StateFixer;

/**
 * Abstrakcyjna sieć neuronowa.
 * 
 * @author Jarosław Protasiewicz
 */
public abstract class Network<S extends Network, C extends NetworkConfiguration>
		extends StateFixer<S> implements Configurable<C>, Simulatable {

	/* ----------------- FIELDS ---------------- */
	/**
	 * Konfiguracja sieci neuronowej.
	 */
	protected C config;

	/**
	 * Warstwy sieci neuronowej.
	 */
	protected List<Layer> layers;

	/**
	 * Zbiór danych treningowych i symulacyjnych
	 */
	protected SupervisedDataSet dataSet;

	/* ---------------- METHODTS -------------------------- */
	public Network() {
		super();
	}

	@Override
	public String toString() {
		// TODO: oprogramować.
		return super.toString();
	}

	/**
	 * Abstrakcyjna metoda zwracajaca wyjście sieci.
	 * 
	 * @return Wyjścia sieci
	 */
	public abstract DoubleVector getOutputs();

	/**
	 * Wyznacza minimalną wartość zwracaną przez dowolną warstwę sieci.
	 * 
	 * @return Wartość minimalna na dowolnym wyjściu z warstwy sieci.
	 */
	public abstract double minReturnValue();

	/**
	 * Wyznacza maksymalną wartość zwracaną przez dowolną warstwę sieci.
	 * 
	 * @return Wartość maksymalną na dowolnym wyjściu z warstwy sieci.
	 */
	public abstract double maxReturnValue();

	/**
	 * Ustawia najlepsze wagi sieci.
	 */
	public abstract void setTheBestWeights(boolean reverse);

	/* ---------------- GETTERS AND SETTERS ---------------- */
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

	/**
	 * @return wartość pola layers
	 */
	public List<Layer> getLayers() {
		return layers;
	}

	/**
	 * @param layers
	 *            jest przypisywany do pola layers
	 */
	public void setLayers(List<Layer> layers) {
		this.layers = layers;
	}

	/**
	 * @return wartość pola dataSet
	 */
	public SupervisedDataSet getDataSet() {
		return dataSet;
	}

	/**
	 * @param dataSet
	 *            jest przypisywany do pola dataSet
	 */
	public void setDataSet(SupervisedDataSet dataSet) {
		this.dataSet = dataSet;
	}

}
