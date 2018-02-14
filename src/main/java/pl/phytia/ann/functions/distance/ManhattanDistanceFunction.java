package pl.phytia.ann.functions.distance;

import pl.phytia.model.sets.DoubleVector;

/**
 * Miara odległości Manhatan.
 * 
 * @author Grzegorz Kubok
 * @author Jarosław Protasiewicz
 */
public final class ManhattanDistanceFunction extends
		DistanceFunction<ManhattanDistanceFunction> {

	private static final long serialVersionUID = 5674195278499001843L;

	@Override
	public double computeDistance(DoubleVector pointOne, DoubleVector pointTwo) {
		checkArgumets(pointOne, pointTwo);
		double distance = 0.0;
		for (int i = 0; i < pointOne.size(); ++i) {
			distance += Math.abs(pointOne.get(i) - pointTwo.get(i));
		}
		return distance;
	}

}
