package pl.phytia.ann.functions.centerSelect;

import java.util.Iterator;
import java.util.List;

import pl.phytia.ann.functions.distance.DistanceFunction;
import pl.phytia.model.conf.functions.centerSelect.CenterSelFunctionConfiguration;
import pl.phytia.model.sets.DoubleVector;
import pl.phytia.model.sets.PatternPairVO;
import pl.phytia.ann.functions.distance.DistanceFunctionFactory;
import pl.phytia.model.sets.SupervisedDataSet;

/**
 * Fukncja doboru centrów metodą k-średnich.
 * 
 * @author Jarosław Protasiewicz
 */
public class KMeanCenterFunction extends
		CenterSelectFunction<KMeanCenterFunction> {

	private static final long serialVersionUID = -2659759550644003120L;

	/**
	 * Fukcja pomiaru odległości.
	 */
	private DistanceFunction distanceFunction;

	public KMeanCenterFunction() {
		super();
		config = new CenterSelFunctionConfiguration();
		distanceFunction = DistanceFunctionFactory.getDistanceFunction(config
				.getDistanceTypeKMean());
	}

	public KMeanCenterFunction(CenterSelFunctionConfiguration conf) {
		super();
		initialize(conf);
		distanceFunction = DistanceFunctionFactory.getDistanceFunction(config
				.getDistanceTypeKMean());
	}

	@Override
	public List<DoubleVector> centerSelect(SupervisedDataSet trainSet) {
		/*
		 * Wstępna, losowa inicjalizacja centrów.
		 */
		List<DoubleVector> centers = new RandomCenterFunction(getConfig())
				.centerSelect(trainSet);
		/*
		 * Zmiana centrów metodą k-średnich.
		 */
		int iter = 0;
		double learnRatio = config.getInitLearnRatioKMean();
		Iterator<PatternPairVO> it = null;
		/*
		 * Dopóki nie wczyarpano ilości powtórzeń algorytmu.
		 */
		while (++iter <= config.getMaxStepKMean()) {
			it = trainSet.iterator();
			/*
			 * Dla każdego wzorca ze zbioru uczącego.
			 */
			while (it.hasNext()) {
				DoubleVector in = it.next().getInputs();
				/*
				 * Wyznaczana jest odlegość wzorca do każdego z centrów. i
				 * znajdowany ten, który jest najbliższy danemu wzorcowi.
				 */
				double minDistance = Double.MAX_VALUE;
				double currDist = minDistance;
				int minCenterIndx = 0;
				for (int i = 0; i < centers.size(); ++i) {
					currDist = distanceFunction.computeDistance(in, centers
							.get(i));
					if (currDist < minDistance) {
						minDistance = currDist;
						minCenterIndx = i;
					}
				}
				/*
				 * Centrum, które jest najbliższe do wektora wejściowego, jest
				 * modyfikowane.
				 */
				modifyCenter(in, centers.get(minCenterIndx), learnRatio);

			}
			/*
			 * Modyfikacja współczynnika uczenia.
			 */
			learnRatio = modifyLearnRatio(learnRatio, iter);
		}
		return centers;
	}

	private void modifyCenter(DoubleVector input, DoubleVector center,
			double learnRatio) {
		for (int i = 0; i < input.size(); ++i) {
			double delta = learnRatio * (input.get(i) - center.get(i));
			center.set(i, center.get(i) + delta);
		}
	}

	private double modifyLearnRatio(double currLearnRatio, int currIter) {
		return (currLearnRatio / (1 + (1 / currIter)));
	}

}
