package pl.phytia.ann.functions.neighbourhood;

import pl.phytia.model.conf.functions.neighbourhood.GaussianNeighbourhoodConfiguration;

/**
 * Funkcja sąsiewdztwa gaussowskiego.
 * 
 * @author Jarosław Protasiewicz
 */
public final class GaussianNeighbourhoodFunction
		extends
		NeighbourhoodFunction<GaussianNeighbourhoodConfiguration, GaussianNeighbourhoodFunction> {

	private static final long serialVersionUID = -7822443560430930351L;

	/**
	 * Konstruktor bezargumentowy.
	 */
	public GaussianNeighbourhoodFunction() {
		super();
	}

	public GaussianNeighbourhoodFunction(GaussianNeighbourhoodConfiguration conf) {
		config = conf;
		setCurrNeighbourhoodSize(config.getNeighbourhoodInitSize());
	}

	@Override
	public void changeNeighbourhood(int epoch) {
		/*
		 * Zmiana sąsiedztwa następuje jeżeli: jest to epoka od której nastepują
		 * zamiany i sąsiedztwo jest niezerowe
		 */
		if (epoch > config.getStartEpochChangeNeighborhood()
				&& getCurrNeighbourhoodSize() > 0) {
			setCurrNeighbourhoodSize(getCurrNeighbourhoodSize()
					* Math.exp(-config.getNeighbourhoodRatio() * epoch));
		}
	}
}
