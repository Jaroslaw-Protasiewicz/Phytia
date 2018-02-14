package pl.phytia.model.conf.functions.neighbourhood;

import pl.phytia.model.enums.EnumNeighbourhoodType;

/**
 * Konfiguracja sąsiewdztwa gausowskiego.
 * 
 * @author Jarosław Protasiewicz
 */
public final class GaussianNeighbourhoodConfiguration extends
		NeighbourhoodFunctionConfiguration<GaussianNeighbourhoodConfiguration> {

	private static final long serialVersionUID = -7964299451572108174L;

	/**
	 * Konstruktor wypełniający wszystkie pola.
	 * 
	 * @param neighbourhoodFunctionType
	 *            Typ funkcji sąsiedztwa (pole informacyjne).
	 * @param startEpochChangeNeighborhood
	 *            Epoka treningu, od której rozpoczyna się zamiany rozmiaru
	 *            sąsiedztwa.
	 * @param neighbourhoodInitSize
	 *            Początkowy wspołczynnik sąsiewdztwa gausowsskiego.
	 * @param neighbourhoodRatio
	 *            Współczynnik sąsiewdztwa gaussowskiego.
	 */
	public GaussianNeighbourhoodConfiguration(
			EnumNeighbourhoodType neighbourhoodFunctionType,
			int startEpochChangeNeighborhood, double neighbourhoodInitSize,
			double neighbourhoodRatio) {
		super(neighbourhoodFunctionType, startEpochChangeNeighborhood);
		this.neighbourhoodInitSize = neighbourhoodInitSize;
		this.neighbourhoodRatio = neighbourhoodRatio;
	}

	/**
	 * Konstruktor bezaparametrowy. Inicjalizuje pola klasy.
	 */
	public GaussianNeighbourhoodConfiguration() {
		super(EnumNeighbourhoodType.Gaussian, 100);
		this.neighbourhoodInitSize = 1.0;
		this.neighbourhoodRatio = 1.E-4;
	}

	/**
	 * Początkowy wspołczynnik sąsiewdztwa gausowsskiego
	 */
	private double neighbourhoodInitSize;

	/**
	 * Współczynnik sąsiewdztwa gaussowskiego.
	 */
	private double neighbourhoodRatio;

	/**
	 * @return wartość pola neighbourhoodRatio
	 */
	public double getNeighbourhoodRatio() {
		return neighbourhoodRatio;
	}

	/**
	 * @param neighbourhoodRatio
	 *            jest przypisywany do pola neighbourhoodRatio
	 */
	public void setNeighbourhoodRatio(double neighbourhoodRatio) {
		this.neighbourhoodRatio = neighbourhoodRatio;
	}

	/**
	 * @return wartość pola neighbourhoodInitSize
	 */
	public double getNeighbourhoodInitSize() {
		return neighbourhoodInitSize;
	}

	/**
	 * @param neighbourhoodInitSize
	 *            jest przypisywany do pola neighbourhoodInitSize
	 */
	public void setNeighbourhoodInitSize(double neighbourhoodInitSize) {
		this.neighbourhoodInitSize = neighbourhoodInitSize;
	}

}
