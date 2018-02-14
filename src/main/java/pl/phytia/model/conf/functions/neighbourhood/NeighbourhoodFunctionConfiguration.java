package pl.phytia.model.conf.functions.neighbourhood;

import pl.phytia.model.conf.Configuration;
import pl.phytia.model.enums.EnumNeighbourhoodType;

/**
 * Abstrakcyjna konfiguracja funkcji siąsiedztwa.
 * 
 * @author Jarosław Protasiewicz
 * 
 * @param <S>
 *            Obiekt, którego stan jest odczytywany lub zapisywany.
 */
public abstract class NeighbourhoodFunctionConfiguration<S extends NeighbourhoodFunctionConfiguration>
		extends Configuration<S> {

	private static final long serialVersionUID = 6177993951100746830L;

	/**
	 * Typ funkcji sąsiedztwa (pole informacyjne).
	 */
	private EnumNeighbourhoodType neighbourhoodFunctionType;

	/**
	 * Epoka treningu, od której rozpoczyna sie zamiany rozmiaru sąsiedztwa.
	 */
	private int startEpochChangeNeighborhood;

	/**
	 * Konstruktor wypełniający wszystkie pola.
	 * 
	 * @param neighbourhoodFunctionType
	 *            Typ funkcji sąsiedztwa (pole informacyjne).
	 * @param startEpochChangeNeighborhood
	 *            Epoka treningu, od której rozpoczyna się zamiany rozmiaru
	 *            sąsiedztwa.
	 */
	public NeighbourhoodFunctionConfiguration(
			EnumNeighbourhoodType neighbourhoodFunctionType,
			int startEpochChangeNeighborhood) {
		super();
		this.neighbourhoodFunctionType = neighbourhoodFunctionType;
		this.startEpochChangeNeighborhood = startEpochChangeNeighborhood;
	}

	/**
	 * Konstruktor bezparametrowy. Inicjalizuje pola wartościami domyślnymi
	 */
	public NeighbourhoodFunctionConfiguration() {
		super();
		this.neighbourhoodFunctionType = EnumNeighbourhoodType.Gaussian;
		this.startEpochChangeNeighborhood = 100;
	}

	/**
	 * @return wartość pola startEpochChangeNeighborhood
	 */
	public int getStartEpochChangeNeighborhood() {
		return startEpochChangeNeighborhood;
	}

	/**
	 * @param startEpochChangeNeighborhood
	 *            jest przypisywany do pola startEpochChangeNeighborhood
	 */
	public void setStartEpochChangeNeighborhood(int startEpochChangeNeighborhood) {
		this.startEpochChangeNeighborhood = startEpochChangeNeighborhood;
	}

	/**
	 * @return wartość pola neighbourhoodFunctionType
	 */
	public EnumNeighbourhoodType getNeighbourhoodFunctionType() {
		return neighbourhoodFunctionType;
	}

	/**
	 * @param neighbourhoodFunctionType
	 *            jest przypisywany do pola neighbourhoodFunctionType
	 */
	public void setNeighbourhoodFunctionType(
			EnumNeighbourhoodType neighbourhoodFunctionType) {
		this.neighbourhoodFunctionType = neighbourhoodFunctionType;
	}

}
