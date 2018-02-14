package pl.phytia.ann.functions.distance;

import pl.phytia.model.enums.EnumDistanceType;

/**
 * Prosta fabryka funkcji odlegości.
 * 
 * @author Jarosław Protasiewicz
 */
public class DistanceFunctionFactory {

	/**
	 * Produkuje funcję odległości.
	 * 
	 * @param distanceType
	 *            Typ funkcji odlełości.
	 * @return Funkcja odległości.
	 */
	public static DistanceFunction getDistanceFunction(
			EnumDistanceType distanceType) {
		DistanceFunction distanceFunction = null;
		switch (distanceType) {
		case Chebyshev:
			distanceFunction = new ChebyshevDistanceFunction();
			break;
		case Euclidean:
			distanceFunction = new EuclideanDistanceFunction();
			break;
		case Manhattan:
			distanceFunction = new ManhattanDistanceFunction();
			break;
		default:
			distanceFunction = new EuclideanDistanceFunction();
			break;
		}
		return distanceFunction;
	}

}
