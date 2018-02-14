package pl.phytia.ann.layers;

import pl.phytia.ann.neurons.SomNeuron;
import pl.phytia.model.sets.DoubleVector;
import pl.phytia.model.conf.layers.SOMLayerConfiguration;

/**
 * Jednowymiarowa warstwa sieci SOM.
 * 
 * @author Jarosław Protasiewicz
 */
public class Som1DimLayer extends SOMLayer {

	/**
	 * Neurony warstwy sieci.
	 */
	private SomNeuron[] somMap;

	/**
	 * Konstruktor bezargumentowy.
	 */
	public Som1DimLayer() {
		super();
	}

	/**
	 * Konstruktor tworzący warstwę i ja inicjalizujący.
	 * 
	 * @param conf
	 *            Konfiguracja warstwy.
	 */
	public Som1DimLayer(SOMLayerConfiguration conf) {
		super(conf);
		somMap = new SomNeuron[conf.getSize()[0]];
		for (int i = 0; i < somMap.length; ++i) {
			somMap[i] = new SomNeuron(conf.getNeuronFunctionConf());
		}
	}

	public void simulation(DoubleVector inputs) {
		double minDistance = Double.MAX_VALUE;
		int[] winnerIndx = new int[1];
		/*
		 * Wyznaczenie odległości każdego neuronu od wejścia i wyznaczenie
		 * zwyciezcy.
		 */
		for (int i = 0; i < somMap.length; ++i) {
			double distance = getDistanceFunction().computeDistance(inputs,
					somMap[i].getWeights());
			somMap[i].setInputDistance(distance);
			if (distance <= minDistance) {
				minDistance = distance;
				winnerIndx[0] = i;
			}
			somMap[i].setWinner(false);
		}
		somMap[winnerIndx[0]].setWinner(true);
		setWinnerIndx(winnerIndx);
	}

	@Override
	public DoubleVector getOutputs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String generateWinnerClassLabel() {
		if (somMap[getWinnerIndx()[0]].getWinCount() == 0) {
			somMap[getWinnerIndx()[0]].setClassName(generateNextClassName());
			somMap[getWinnerIndx()[0]].setWinCount(1);
		} else {
			somMap[getWinnerIndx()[0]].setWinCount(somMap[getWinnerIndx()[0]]
					.getWinCount() + 1);
		}
		return somMap[getWinnerIndx()[0]].getClassName();
	}

	@Override
	public void reset() {
		super.reset();
		for (int i = 0; i < somMap.length; ++i) {
			somMap[i].reset();
		}
	}

	@Override
	public void computeNeighbourhood() {
		switch (getConfig().getNeighbourhoodConfiguration()
				.getNeighbourhoodFunctionType()) {
		case Gaussian: {
			/*
			 * wyznaczenie odległości od zwycięzcy
			 */
			DoubleVector win = new DoubleVector(1);
			win.add(new Double(getWinnerIndx()[0]));
			DoubleVector pos = new DoubleVector(1);
			pos.add(new Double(0.0));
			for (int x = 0; x < getConfig().getSize()[0]; ++x) {
				pos.set(0, new Double(x));
				double dist = getDistanceFunction().computeDistance(win, pos);
				somMap[x].setWinnerDistance(dist);
			}
			/*
			 * Wyznaczenie sąsiedztwa
			 */
			for (int x = 0; x < getConfig().getSize()[0]; ++x) {
				double neighbourhood = Math.exp(-Math.pow(somMap[x]
						.getWinnerDistance(), 2)
						/ 2
						* Math.pow(getNeighbourhoodFunction()
								.getCurrNeighbourhoodSize(), 2));
				somMap[x].setNeighbourhood(neighbourhood);
			}
			break;
		}
		case Rectangular: {
			/*
			 * Wyznaczenie sąsiedztwa
			 */
			for (int x = 0; x < getConfig().getSize()[0]; ++x) {
				if (x >= (getWinnerIndx()[0] - (int) getNeighbourhoodFunction()
						.getCurrNeighbourhoodSize())
						&& x <= (getWinnerIndx()[0] + (int) getNeighbourhoodFunction()
								.getCurrNeighbourhoodSize())) {
					somMap[x].setNeighbourhood(1);
				} else {
					somMap[x].setNeighbourhood(0);
				}
			}
			break;
		}
		default:
			throw new UnsupportedOperationException(
					"Nie osbługiwany typ sąsiedztwa!");
		}
	}

	@Override
	public double computeGlobalWeightChange() {
		double weightChange = 0;
		for (int x = 0; x < getConfig().getSize()[0]; ++x) {
			weightChange += somMap[x].computeWeightChange();
		}
		return weightChange;
	}

	@Override
	public String getWinnerClass() {
		return somMap[getWinnerIndx()[0]].getClassName();
	}

	public SomNeuron[] getSomMap() {
		return somMap;
	}

	public void setSomMap(SomNeuron[] somMap) {
		this.somMap = somMap;
	}

}
