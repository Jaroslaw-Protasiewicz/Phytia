package pl.phytia.prediction.metaModels;

import java.util.ArrayList;
import java.util.List;

import pl.phytia.ann.algorithms.Algorithm;
import pl.phytia.ann.networks.MLPNetwork;
import pl.phytia.ann.networks.Network;
import pl.phytia.ann.networks.RBFNetwork;
import pl.phytia.core.Configurable;
import pl.phytia.prediction.models.Model;
import pl.phytia.model.conf.pred.AnnModelConfiguration;
import pl.phytia.model.conf.pred.MetaModelConfiguration;
import pl.phytia.model.db.ModelDbVO;

/**
 * Abstrakcyjny Meta-Model
 * 
 * @author Jarosław Protasiewicz
 * @param <M>
 *            Predyktor z siecią neuronową.
 */
public abstract class MetaModel<M extends Model> implements
		Configurable<MetaModelConfiguration> {

	/**
	 * Lista modeli - predyktorów.
	 */
	private List<M> predictors;

	/**
	 * Konfiguracja metamodelu.
	 */
	private MetaModelConfiguration config;

	/**
	 * Konstruktor bezarumentowy.
	 */
	public MetaModel() {
		super();
	}

	/**
	 * Konstruktor.
	 * 
	 * @param config
	 *            Konfiguracja metamodelu.
	 */
	public MetaModel(MetaModelConfiguration config, List<M> predictors) {
		super();
		this.config = config;
		this.predictors = predictors;
	}

	public void initialize(MetaModelConfiguration conf) {
		this.config = conf;
		this.predictors = new ArrayList<M>();
	}

	/* ------------- MODELS ------------------ */
	/**
	 * Przygotowanie modelu na podstawie stanu z bazy danych.
	 */
	protected abstract M preprareModel(ModelDbVO modelDb);

	/* ------------- MODELS CONF ------------------ */
	/**
	 * Przygotowuje konfigurację modelu - predyktora.
	 * 
	 * @return Konfiguracja modelu.
	 */
	protected abstract AnnModelConfiguration preparePredictorConfiguration();

	/**
	 * Odczytuje konfigoruajcę modeli (predyktora, klasyfikaotra) z
	 * serializowanego bean-a.
	 * 
	 * @param state
	 *            Serializowany stan konfiguracji.
	 * @return Konfiguracja modelu.
	 */
	protected AnnModelConfiguration prepareModelConfiguration(String state) {
		AnnModelConfiguration annConf = new AnnModelConfiguration();
		annConf.loadState(state);
		return annConf;
	}

	/* ------------- MLP NETWORKS ------------------ */
	/**
	 * Przygotowuje sieć neuronową.
	 * 
	 * @param conf
	 *            Konfiguracja modelu.
	 * @return Sieć neuronowa.
	 */
	protected abstract Network prepareNetwork(AnnModelConfiguration conf);

	/**
	 * @return Zwraca strukturę sieci neuronowej w postaci tablicy.
	 */
	public abstract int[] getPredictorStructure();

	/**
	 * Przygotowanie sieci neuronowej MLP na podstawie stanu z bazy danych.
	 * 
	 * @param State
	 *            Stan.
	 * @return Sieć neuronowa.
	 */
	protected Network prepareMLPNetwork(String state) {
		Network net = new MLPNetwork();
		net.loadState(state);
		return net;
	}

	/**
	 * Przygotowanie sieci neuronowej MLP na podstawie stanu z bazy danych.
	 * 
	 * @param State
	 *            Stan.
	 * @return Sieć neuronowa.
	 */
	protected Network prepareRBFNetwork(String state) {
		Network net = new RBFNetwork();
		net.loadState(state);
		return net;
	}

	/* ------------- ALGORITHMS ------------------ */
	/**
	 * Przygotowuje algorytm treningu sieci - faza pierwsza.
	 * 
	 * @return Algorytm teningu sieci.
	 */
	protected abstract Algorithm prepareFirstPhaseTrainAlgorithm();

	/**
	 * Przygotowuje algorytm treningu sieci - faza druga.
	 * 
	 * @return Algorytm teningu sieci.
	 */
	protected abstract Algorithm prepareSecondPhaseTrainAlgorithm();

	/* ------------- MAIN METHODS ------------------ */
	/**
	 * Wstępne przygotowanie.
	 */
	public abstract void preparation();

	/**
	 * Wykonanie prognozy wg konfiguracji.
	 */
	public abstract void prediction();

	/* ------------- GETTERS AND SETTERS ------------------- */
	public MetaModelConfiguration getConfig() {
		return config;
	}

	public void setConfig(MetaModelConfiguration config) {
		this.config = config;
	}

	public List<M> getPredictors() {
		return predictors;
	}

	public void setPredictors(List<M> predictors) {
		this.predictors = predictors;
	}
}
