package pl.phytia.ann.functions.distance;

import pl.phytia.model.sets.DoubleVector;

/**
 * Miara odległości Czybyszewa (inaczej Maksimum).
 * 
 * @author Jarosław Protasiewicz
 */
public final class ChebyshevDistanceFunction extends
		DistanceFunction<ChebyshevDistanceFunction> {

	private static final long serialVersionUID = -1683419735681048800L;

	@Override
	public double computeDistance(DoubleVector pointOne, DoubleVector pointTwo) {
		checkArgumets(pointOne, pointTwo);
		double distance = Double.MIN_VALUE;
		for (int i = 0; i < pointOne.size(); i++) {
			Double tmp = Math.abs(pointOne.get(i) - pointTwo.get(i));
			if (tmp > distance) {
				distance = tmp;
			}
		}
		return distance;
	}

}
