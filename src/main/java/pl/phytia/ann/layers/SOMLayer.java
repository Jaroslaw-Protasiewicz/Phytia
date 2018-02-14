package pl.phytia.ann.layers;

import pl.phytia.ann.functions.distance.ChebyshevDistanceFunction;
import pl.phytia.ann.functions.distance.DistanceFunction;
import pl.phytia.ann.functions.distance.EuclideanDistanceFunction;
import pl.phytia.ann.functions.learningRate.ExponentialLearningRateFunction;
import pl.phytia.ann.functions.learningRate.LearningRateFunction;
import pl.phytia.ann.functions.learningRate.LinearLearningRateFunction;
import pl.phytia.ann.functions.neighbourhood.NeighbourhoodFunction;
import pl.phytia.ann.functions.neighbourhood.RectangularNeighbourhoodFunction;
import pl.phytia.model.conf.functions.learnRate.ExponentialLearningRateConfiguration;
import pl.phytia.model.conf.functions.learnRate.LinearLearnigRateConfiguration;
import pl.phytia.model.conf.functions.neighbourhood.GaussianNeighbourhoodConfiguration;
import pl.phytia.model.conf.functions.neighbourhood.RectangularNeighbourhoodConfiguration;
import pl.phytia.ann.functions.distance.ManhattanDistanceFunction;
import pl.phytia.ann.functions.neighbourhood.GaussianNeighbourhoodFunction;
import pl.phytia.model.conf.layers.SOMLayerConfiguration;

/**
 * Abstrakcyjna warstwa seci samoorganizujacej się.
 * 
 * @author Jarosław Protasiewicz
 */
public abstract class SOMLayer extends Layer<SOMLayer, SOMLayerConfiguration> {

	/**
	 * Funkcja dokonująca pomiaru odległości.
	 */
	private DistanceFunction distanceFunction;

	/**
	 * Funkcja określająca sąsiedztwo.
	 */
	private NeighbourhoodFunction neighbourhoodFunction;

	/**
	 * Funkcja współczynnika uczenia.
	 */
	private LearningRateFunction learningRateFunction;

	/**
	 * Indeksy zwycięzy.
	 */
	private int[] winnerIndx;

	/**
	 * Indeks kolejnej klasy.
	 */
	private int classIndx = 0;

	/**
	 * Konstruktor bezargumentowy.
	 */
	public SOMLayer() {
		super();
	}

	public SOMLayer(SOMLayerConfiguration conf) {
		initialize(conf);
	}

	@SuppressWarnings("unchecked")
	public void initialize(SOMLayerConfiguration conf) {
		this.config = conf;
		this.classIndx = 0;
		/*
		 * Inicjlaizacja miary odległości.
		 */
		switch (config.getDistanceType()) {
		case Euclidean:
			distanceFunction = new EuclideanDistanceFunction();
			break;
		case Chebyshev:
			distanceFunction = new ChebyshevDistanceFunction();
			break;
		case Manhattan:
			distanceFunction = new ManhattanDistanceFunction();
			break;
		default:
			distanceFunction = new EuclideanDistanceFunction();
		}
		/*
		 * Inicjalizacja miary sąsiedztwa.
		 */
		switch (config.getNeighbourhoodConfiguration()
				.getNeighbourhoodFunctionType()) {
		case Gaussian:
			neighbourhoodFunction = new GaussianNeighbourhoodFunction(
					(GaussianNeighbourhoodConfiguration) config
							.getNeighbourhoodConfiguration());
			break;
		case Rectangular:
			neighbourhoodFunction = new RectangularNeighbourhoodFunction(
					(RectangularNeighbourhoodConfiguration) config
							.getNeighbourhoodConfiguration());
			break;
		default:
			neighbourhoodFunction = new RectangularNeighbourhoodFunction();
		}
		neighbourhoodFunction.setConfig(config.getNeighbourhoodConfiguration());

		/*
		 * Inicjalizacji funkcji współczynnika uczenia
		 */
		switch (config.getLearningRateConfiguration().getLearningRatioType()) {
		case LINEAR:
			learningRateFunction = new LinearLearningRateFunction(
					(LinearLearnigRateConfiguration) conf
							.getLearningRateConfiguration());
			break;
		case EXPONENTIAL:
			learningRateFunction = new ExponentialLearningRateFunction(
					(ExponentialLearningRateConfiguration) conf
							.getLearningRateConfiguration());
			break;
		default:
			learningRateFunction = new LinearLearningRateFunction();
		}
		learningRateFunction.setConfig(conf.getLearningRateConfiguration());

	}

	@Override
	public double maxReturnValue() {
		return 0;
	}

	@Override
	public double minReturnValue() {
		return 1.0;
	}

	/**
	 * Generuje nazwę kolejnej klasy.
	 * 
	 * @return Nazwa klasy.
	 */
	public String generateNextClassName() {
		return "class_" + (++classIndx);
		// return "" + (++classIndx);
	}

	/**
	 * Generuje etykietę klasy którą reprezentuje zwycięzca.
	 * 
	 * @return Etykieta klasy.
	 */
	public abstract String generateWinnerClassLabel();

	/**
	 * Resetuje stan warstwy sieci.
	 */
	public void reset() {
		this.classIndx = 0;
		this.winnerIndx = null;
	}

	/**
	 * Wyznacza globalną zamianę wag mapy.
	 * 
	 * @return Zmiana wag.
	 */
	public abstract double computeGlobalWeightChange();

	/**
	 * Wyznacza sąsiedztwo wszystkich neuronów matrycy.
	 */
	public abstract void computeNeighbourhood();

	/**
	 * Zwraca nazwę klasy zwycięzcy.
	 * 
	 * @return Etykieta zwycięzcy.
	 */
	public abstract String getWinnerClass();

	/**
	 * @return wartość pola distanceFunction
	 */
	public DistanceFunction getDistanceFunction() {
		return distanceFunction;
	}

	/**
	 * @param distanceFunction
	 *            jest przypisywany do pola distanceFunction
	 */
	public void setDistanceFunction(DistanceFunction distanceFunction) {
		this.distanceFunction = distanceFunction;
	}

	/**
	 * @return wartość pola neighbourhoodFunction
	 */
	public NeighbourhoodFunction getNeighbourhoodFunction() {
		return neighbourhoodFunction;
	}

	/**
	 * @param neighbourhoodFunction
	 *            jest przypisywany do pola neighbourhoodFunction
	 */
	public void setNeighbourhoodFunction(
			NeighbourhoodFunction neighbourhoodFunction) {
		this.neighbourhoodFunction = neighbourhoodFunction;
	}

	/**
	 * @return wartość pola learningRateFunction
	 */
	public LearningRateFunction getLearningRateFunction() {
		return learningRateFunction;
	}

	/**
	 * @param learningRateFunction
	 *            jest przypisywany do pola learningRateFunction
	 */
	public void setLearningRateFunction(
			LearningRateFunction learningRateFunction) {
		this.learningRateFunction = learningRateFunction;
	}

	/**
	 * @return wartość pola winnerIndx
	 */
	public int[] getWinnerIndx() {
		return winnerIndx;
	}

	/**
	 * @param winnerIndx
	 *            jest przypisywany do pola winnerIndx
	 */
	public void setWinnerIndx(int[] winnerIndx) {
		this.winnerIndx = winnerIndx;
	}

	/**
	 * @return wartość pola classIndx
	 */
	public int getClassIndx() {
		return classIndx;
	}

}
