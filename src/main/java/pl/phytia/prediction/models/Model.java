package pl.phytia.prediction.models;

import java.util.Date;

import pl.phytia.utils.Normalizer;
import pl.phytia.ann.algorithms.Algorithm;
import pl.phytia.core.Configurable;
import pl.phytia.model.conf.pred.AnnModelConfiguration;
import pl.phytia.model.conf.pred.ModelConfiguration;
import pl.phytia.model.enums.EnumModelState;
import pl.phytia.model.sets.DataSet;
import pl.phytia.utils.StateFixer;

/**
 * Abstrakcyjny model.
 * 
 * @author Jarosław Protasiewicz
 * 
 * @param <C>
 *            Konfiguracja modelu.
 * @param <S>
 *            Stan modelu. "param <D> Zbiór danych.
 */
public abstract class Model<C extends ModelConfiguration, S extends Model, D extends DataSet>
		extends StateFixer<S> implements Configurable<C>, Runnable {

	/**
	 * Konfiguracja modelu.
	 */
	protected C config;

	/**
	 * Algorytm modelowania.
	 */
	private Algorithm algorithm;

	/**
	 * Zbiór danych do modelowania.
	 */
	private D modelSet;

	/**
	 * Zbiór danych do prognozowania.
	 */
	private D predictSet;

	/**
	 * Normalizator danych.
	 */
	private Normalizer normalizer;

	/**
	 * Stan modelu.
	 */
	private EnumModelState modelState;

	/**
	 * Klasa modelu.
	 */
	private String modelClass;

	/**
	 * Bezargumentowy konstruktor.
	 */
	public Model() {
		super();
	}

	/**
	 * Konstruktor.
	 * 
	 * @param config
	 *            Konfiguracja modelu.
	 * @param algorithm
	 *            Algorytm modelowania.
	 * @param modelSet
	 *            Zbiór danych do modelowania.
	 * @param predictSet
	 *            Zbiór danych do prognozy.
	 * @param normalizer
	 *            Normalizator danych.
	 * @param modelClass
	 *            Klasa modelu.
	 */
	public Model(C config, Algorithm algorithm, D modelSet, D predictSet,
			Normalizer normalizer, String modelClass) {
		super();
		this.config = config;
		this.algorithm = algorithm;
		this.modelSet = modelSet;
		this.predictSet = predictSet;
		this.normalizer = normalizer;
		this.modelClass = modelClass;
	}

	/**
	 * Inicjalizajca modelu.
	 * 
	 * @param conf
	 *            Konfiguracja modelu.
	 * @param predictionDay
	 *            Dzień prognozy - dzień, na który jest przygotowywany model.
	 * @param predictionStartDay
	 *            Dzień startu prognozy - może być inny niż
	 *            <code>predictionDay</code>.
	 */
	public abstract void initialize(AnnModelConfiguration conf,
			Date predictionDay, Date predictionStartDay);

	/**
	 * Metoda realizuje pierwsze przygotowanie modelu.
	 */
	public abstract void preparation();

	/**
	 * Metoda realizuje proces modelowania (treningu / samoorganizacji).
	 */
	public abstract void modeling();

	/**
	 * Metoda realizuje proces prognozowania / klasyfikacji.
	 */
	public abstract void execution();

	/* ################# GETTERS AND SETTERS ###################### */
	/**
	 * @return wartość pola algorithm
	 */
	public Algorithm getAlgorithm() {
		return algorithm;
	}

	/**
	 * @param algorithm
	 *            jest przypisywany do pola algorithm
	 */
	public void setAlgorithm(Algorithm algorithm) {
		this.algorithm = algorithm;
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

	/**
	 * @return wartość pola modelSet
	 */
	public D getModelSet() {
		return modelSet;
	}

	/**
	 * @param modelSet
	 *            jest przypisywany do pola modelSet
	 */
	public void setModelSet(D modelSet) {
		this.modelSet = modelSet;
	}

	@SuppressWarnings("unchecked")
	public void setModelSet(D modelSet, boolean lazyLoading) {
		if (lazyLoading) {
			this.modelSet.getSet().addAll(modelSet.getSet());
		} else {
			this.modelSet = modelSet;
		}
	}

	/**
	 * @return wartość pola predictSet
	 */
	public D getPredictSet() {
		return predictSet;
	}

	/**
	 * @param predictSet
	 *            jest przypisywany do pola predictSet
	 */
	public void setPredictSet(D predictSet) {
		this.predictSet = predictSet;
	}

	/**
	 * @return wartość pola modelState
	 */
	public EnumModelState getModelState() {
		return modelState;
	}

	/**
	 * @param modelState
	 *            jest przypisywany do pola modelState
	 */
	public void setModelState(EnumModelState modelState) {
		this.modelState = modelState;
	}

	/**
	 * @return wartość pola normalizer
	 */
	public Normalizer getNormalizer() {
		return normalizer;
	}

	/**
	 * @param normalizer
	 *            jest przypisywany do pola normalizer
	 */
	public void setNormalizer(Normalizer normalizer) {
		this.normalizer = normalizer;
	}

	/**
	 * @return wartość pola modelClass
	 */
	public String getModelClass() {
		return modelClass;
	}

	/**
	 * @param modelClass
	 *            jest przypisywany do pola modelClass
	 */
	public void setModelClass(String modelClass) {
		this.modelClass = modelClass;
	}

}
