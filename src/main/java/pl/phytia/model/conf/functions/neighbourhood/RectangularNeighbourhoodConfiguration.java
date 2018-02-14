package pl.phytia.model.conf.functions.neighbourhood;

import pl.phytia.model.enums.EnumNeighbourhoodType;

/**
 * Konfiguracja sąsiedztwa prostokątnego.
 * 
 * @author Jarosław Protasiewicz
 */
public final class RectangularNeighbourhoodConfiguration
		extends
		NeighbourhoodFunctionConfiguration<RectangularNeighbourhoodConfiguration> {

	private static final long serialVersionUID = 5107948129461679255L;

	/**
	 * Konstruktor wypełniający wszystkie pola.
	 * 
	 * @param neighbourhoodFunctionType
	 *            Typ funkcji sąsiedztwa (pole informacyjne).
	 * @param startEpochChangeNeighborhood
	 *            Epoka treningu, od której rozpoczyna się zamiany rozmiaru
	 *            sąsiedztwa.
	 * @param neighbourhoodInitSize
	 *            Rozmiar początkowy sąsiedztwa.
	 * @param neighbourhoodStepChange
	 *            Parametr określający co ile epok następuje zmiana sąsiedztwa.
	 */
	public RectangularNeighbourhoodConfiguration(
			EnumNeighbourhoodType neighbourhoodFunctionType,
			int startEpochChangeNeighborhood, int neighbourhoodInitSize,
			int neighbourhoodStepChange) {
		super(neighbourhoodFunctionType, startEpochChangeNeighborhood);
		this.neighbourhoodInitSize = neighbourhoodInitSize;
		this.neighbourhoodStepChange = neighbourhoodStepChange;
	}

	/**
	 * Konstruktor bezparametrowy. Inicjalizuje pola wartościami domyślnymi.
	 */
	public RectangularNeighbourhoodConfiguration() {
		super(EnumNeighbourhoodType.Rectangular, 100);
		this.neighbourhoodInitSize = 10;
		this.neighbourhoodStepChange = 1;
	}

	/**
	 * Rozmiar początkowy sąsiedztwa.
	 */
	private int neighbourhoodInitSize;

	/**
	 * Parametr określający co ile epok następuje zmiana sąsiedztwa.
	 */
	private int neighbourhoodStepChange;

	/**
	 * @return wartość pola neighbourhoodInitSize
	 */
	public int getNeighbourhoodInitSize() {
		return neighbourhoodInitSize;
	}

	/**
	 * @param neighbourhoodInitSize
	 *            jest przypisywany do pola neighbourhoodInitSize
	 */
	public void setNeighbourhoodInitSize(int neighbourhoodInitSize) {
		this.neighbourhoodInitSize = neighbourhoodInitSize;
	}

	/**
	 * @return wartość pola neighbourhoodStepChange
	 */
	public int getNeighbourhoodStepChange() {
		return neighbourhoodStepChange;
	}

	/**
	 * @param neighbourhoodStepChange
	 *            jest przypisywany do pola neighbourhoodStepChange
	 */
	public void setNeighbourhoodStepChange(int neighbourhoodStepChange) {
		this.neighbourhoodStepChange = neighbourhoodStepChange;
	}

}
