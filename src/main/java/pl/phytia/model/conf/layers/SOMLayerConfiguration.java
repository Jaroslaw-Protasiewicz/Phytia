package pl.phytia.model.conf.layers;

import pl.phytia.model.conf.functions.learnRate.LearningRateConfiguration;
import pl.phytia.model.conf.functions.neighbourhood.NeighbourhoodFunctionConfiguration;
import pl.phytia.model.conf.functions.neuron.SomNeuronConfiguration;
import pl.phytia.model.enums.EnumDistanceType;

/**
 * Konfiguracja warstwy sieci samoorganizującej się.
 * 
 * @author Jarosław Protasiewicz
 * 
 * @param <N>
 *            Typ funkcji konfiguracji siąsiedztwa.
 * @param <L>
 *            Typ funkcji współczynnika uczenia.
 */
public final class SOMLayerConfiguration<N extends NeighbourhoodFunctionConfiguration, L extends LearningRateConfiguration>
		extends LayerConfiguration<SOMLayerConfiguration> {

	private static final long serialVersionUID = -492216338740027487L;

	/**
	 * Konfiguracja neuronu i funkcji aktywacji.
	 */
	private SomNeuronConfiguration neuronFunctionConf;

	/**
	 * Liczba wymiarów warstwy.
	 */
	private int dimension;

	/**
	 * Rozmiary warstw dla poszczególnych wymiarów;
	 */
	private int[] size;

	/**
	 * Typ funkcji wyznaczającej odległość.
	 */
	private EnumDistanceType distanceType;

	/**
	 * Konfiguracja funkcji sąsiedztwa.
	 */
	private N neighbourhoodConfiguration;

	/**
	 * Konfiguracja funkcji współczynnika uczenia.
	 */
	private L learningRateConfiguration;

	/**
	 * Konstruktor bezargumentowy
	 */
	public SOMLayerConfiguration() {
		super();
	}

	/**
	 * Konstruktor wypełniający pola warstwy.
	 * 
	 * @param dimension
	 *            Liczba wymiarów warstwy.
	 * @param size
	 *            Rozmiary warstw dla poszczególnych wymiarów;
	 * @param distanceType
	 *            Typ funkcji wyznaczającej odległość.
	 * @param neighbourhoodConfiguration
	 *            Konfiguracja funkcji sąsiedztwa.
	 * @param learningRateConfiguration
	 *            Konfiguracja funkcji współczynnika uczenia.
	 * @param neuronFunctionConf
	 *            Konfiguracja neuronu i funkcji aktywacji.
	 */
	public SOMLayerConfiguration(int dimension, int[] size,
			EnumDistanceType distanceType, N neighbourhoodConfiguration,
			L learningRateConfiguration,
			SomNeuronConfiguration neuronFunctionConf) {
		super();
		this.dimension = dimension;
		this.size = size;
		this.distanceType = distanceType;
		this.neighbourhoodConfiguration = neighbourhoodConfiguration;
		this.learningRateConfiguration = learningRateConfiguration;
		this.neuronFunctionConf = neuronFunctionConf;
	}

	/**
	 * Konstruktor dla warstwy N - wymiarowej.
	 * 
	 * @param neighbourhoodConfiguration
	 *            Konfiguracja funkcji sąsiedztwa.
	 * @param learningRateConfiguration
	 *            Konfiguracja funkcji współczynnika uczenia.
	 * @param distanceType
	 *            Typ funkcji wyznaczającej odległość.
	 * @param neuronFunctionConf
	 *            Konfiguracja neuronu i funkcji aktywacji.
	 * @param sizes
	 *            Wymiary warstwy.
	 */
	public SOMLayerConfiguration(N neighbourhoodConfiguration,
			L learningRateConfiguration, EnumDistanceType distanceType,
			SomNeuronConfiguration neuronFunctionConf, int... sizes) {
		super();
		this.dimension = sizes.length;
		this.size = new int[this.dimension];
		for (int i = 0; i < this.dimension; ++i) {
			size[i] = sizes[i];
		}
		this.distanceType = distanceType;
		this.neighbourhoodConfiguration = neighbourhoodConfiguration;
		this.learningRateConfiguration = learningRateConfiguration;
		this.neuronFunctionConf = neuronFunctionConf;

	}

	/**
	 * @return wartość pola dimension
	 */
	public int getDimension() {
		return dimension;
	}

	/**
	 * @param dimension
	 *            jest przypisywany do pola dimension
	 */
	public void setDimension(int dimension) {
		this.dimension = dimension;
	}

	/**
	 * @return wartość pola size
	 */
	public int[] getSize() {
		return size;
	}

	/**
	 * @param size
	 *            jest przypisywany do pola size
	 */
	public void setSize(int[] size) {
		this.size = size;
	}

	/**
	 * @return wartość pola distanceType
	 */
	public EnumDistanceType getDistanceType() {
		return distanceType;
	}

	/**
	 * @param distanceType
	 *            jest przypisywany do pola distanceType
	 */
	public void setDistanceType(EnumDistanceType distanceType) {
		this.distanceType = distanceType;
	}

	/**
	 * @return wartość pola neighbourhoodConfiguration
	 */
	public N getNeighbourhoodConfiguration() {
		return neighbourhoodConfiguration;
	}

	/**
	 * @param neighbourhoodConfiguration
	 *            jest przypisywany do pola neighbourhoodConfiguration
	 */
	public void setNeighbourhoodConfiguration(N neighbourhoodConfiguration) {
		this.neighbourhoodConfiguration = neighbourhoodConfiguration;
	}

	/**
	 * @return wartość pola learningRateConfiguration
	 */
	public L getLearningRateConfiguration() {
		return learningRateConfiguration;
	}

	/**
	 * @param learningRateConfiguration
	 *            jest przypisywany do pola learningRateConfiguration
	 */
	public void setLearningRateConfiguration(L learningRateConfiguration) {
		this.learningRateConfiguration = learningRateConfiguration;
	}

	/**
	 * @return wartość pola neuronFunctionConf
	 */
	public SomNeuronConfiguration getNeuronFunctionConf() {
		return neuronFunctionConf;
	}

	/**
	 * @param neuronFunctionConf
	 *            jest przypisywany do pola neuronFunctionConf
	 */
	public void setNeuronFunctionConf(SomNeuronConfiguration neuronFunctionConf) {
		this.neuronFunctionConf = neuronFunctionConf;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(this.getClass().toString() + "\n");
		sb.append(super.toString());
		sb.append("dimension = " + dimension + "\n");
		sb.append("size = " + size.toString() + "\n");
		sb.append("distanceType = " + distanceType.toString() + "\n");
		sb.append(learningRateConfiguration.toString());
		sb.append(neighbourhoodConfiguration.toString());
		sb.append(neuronFunctionConf.toString());
		return sb.toString();
	}

}
