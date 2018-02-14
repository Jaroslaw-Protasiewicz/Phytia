package pl.phytia.ann.functions.neighbourhood;

import pl.phytia.model.conf.functions.neighbourhood.RectangularNeighbourhoodConfiguration;

/**
 * Funkcja sąsiewdztwa prostokątnego.
 * 
 * @author Jarosław Protasiewicz
 */
public final class RectangularNeighbourhoodFunction
		extends
		NeighbourhoodFunction<RectangularNeighbourhoodConfiguration, RectangularNeighbourhoodFunction> {

	private static final long serialVersionUID = -3631832232963865036L;

	/**
	 * Konstruktor bezargumentowy.
	 */
	public RectangularNeighbourhoodFunction() {
		super();
	}

	public RectangularNeighbourhoodFunction(
			RectangularNeighbourhoodConfiguration conf) {
		super();
		config = conf;
		setCurrNeighbourhoodSize(config.getNeighbourhoodInitSize());
	}

	@Override
	public void changeNeighbourhood(int epoch) {
		/*
		 * Zmiana sąsiedztwa jeżeli: minęla już epoka od ktore zmiany są możliwe
		 * i jest to epoka w której nastepuje zmiana i sąsiedztwo jest niezerowe
		 */
		if (epoch > config.getStartEpochChangeNeighborhood()
				&& epoch % config.getNeighbourhoodStepChange() == 0
				&& getCurrNeighbourhoodSize() > 0) {
			setCurrNeighbourhoodSize(getCurrNeighbourhoodSize() - 1);
		}
	}
}