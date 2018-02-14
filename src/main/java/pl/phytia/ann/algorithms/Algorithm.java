package pl.phytia.ann.algorithms;

import org.apache.log4j.Logger;

import pl.phytia.ann.functions.error.DiffErrorFunction;
import pl.phytia.ann.functions.error.MapeErrorFunction;
import pl.phytia.ann.functions.error.SumSquaredErrorFunction;
import pl.phytia.ann.networks.Network;
import pl.phytia.core.Configurable;
import pl.phytia.model.conf.algorithms.AlgorithmConfiguration;
import pl.phytia.model.enums.EnumFunctionType;
import pl.phytia.utils.StateFixer;
import pl.phytia.ann.functions.error.ErrorFunction;
import pl.phytia.core.Trainable;
import pl.phytia.model.sets.DataSet;

/**
 * Abstrakcyjny algorytm.
 * 
 * @author Jarosław Protasiewicz
 * 
 * @param <S>
 *            Typ algorytmu.
 * @param <C>
 *            Typ konfiguracji algorytmu.
 * @param <D>
 *            Typ zbioru danych.
 * @param <N>
 *            Typ sieci neuronowej
 */
public abstract class Algorithm<S extends Algorithm, C extends AlgorithmConfiguration, D extends DataSet, N extends Network>
		extends StateFixer<S> implements Configurable<C>, Trainable<D, N> {

	protected Logger logger = Logger.getLogger(Algorithm.class);

	/**
	 * Konstruktor
	 */
	public Algorithm() {
		super();
	}

	/**
	 * Konfiguracja algorytmu.
	 */
	protected C config;

	/**
	 * Aktualny numer iteracji
	 */
	protected int currEpoch = 0;

	/**
	 * Funcja szacująca błądu treningu sieci.
	 */
	private ErrorFunction errorFunction;

	/***************************************************************************
	 * Funkcja szacująca warunek stop.
	 */
	private ErrorFunction stopCondFunction;

	public void initialize(C conf) {
		this.config = conf;
		/*
		 * Utowrzenie funkcji błęd.
		 */
		if (conf.getStopCondFunctionType() != null) {
			switch (conf.getErrorFunctionType()) {
			case EnumFunctionType.SSE:
				this.errorFunction = new SumSquaredErrorFunction();
				break;
			case EnumFunctionType.MAPE:
				this.errorFunction = new SumSquaredErrorFunction();
				break;
			case EnumFunctionType.DIFF:
				this.errorFunction = new DiffErrorFunction();
				break;
			default:
				this.errorFunction = new SumSquaredErrorFunction();
				break;
			}
		} else {
			this.errorFunction = new SumSquaredErrorFunction();
		}
		/*
		 * Utworzenie funkcji błędu dla warunku stop.
		 */
		if (conf.getStopCondFunctionType() != null) {
			switch (conf.getStopCondFunctionType()) {
			case EnumFunctionType.SSE:
				this.stopCondFunction = new SumSquaredErrorFunction();
				break;
			case EnumFunctionType.MAPE:
				this.stopCondFunction = new MapeErrorFunction();
				break;
			default:
				this.errorFunction = null;
				break;
			}
		} else {
			this.stopCondFunction = null;
		}
	}

	/**
	 * Zwiększa licznik iteracji.
	 */
	public void increaseCurrEpoch() {
		this.currEpoch++;
	}

	/* ---------- GETTERS AND SETTERS ------------- */
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
	 * @return wartość pola errorFunction
	 */
	public ErrorFunction getErrorFunction() {
		return errorFunction;
	}

	/**
	 * @param errorFunction
	 *            jest przypisywany do pola errorFunction
	 */
	public void setErrorFunction(ErrorFunction errorFunction) {
		this.errorFunction = errorFunction;
	}

	/**
	 * @return wartość pola stopCondFunction
	 */
	protected ErrorFunction getStopCondFunction() {
		return stopCondFunction;
	}

	/**
	 * @param stopCondFunction
	 *            jest przypisywany do pola stopCondFunction
	 */
	protected void setStopCondFunction(ErrorFunction stopCondFunction) {
		this.stopCondFunction = stopCondFunction;
	}

}
