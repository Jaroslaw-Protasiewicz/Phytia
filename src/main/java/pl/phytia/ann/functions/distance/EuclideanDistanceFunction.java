package pl.phytia.ann.functions.distance;

import pl.phytia.model.sets.DoubleVector;

/**
 * Euklidesowa miara odległości.
 * 
 * @author Jarosław Protasiewicz
 */
public final class EuclideanDistanceFunction extends
		DistanceFunction<EuclideanDistanceFunction> {

	private static final long serialVersionUID = 1761907291709428927L;

	@Override
	public double computeDistance(DoubleVector pointOne, DoubleVector pointTwo) {
		checkArgumets(pointOne, pointTwo);
		double distance = 0.0;
		for (int i = 0; i < pointOne.size(); i++) {
			distance += Math.pow(pointOne.get(i) - pointTwo.get(i), 2.0);
		}
		return Math.sqrt(distance);
	}

}
