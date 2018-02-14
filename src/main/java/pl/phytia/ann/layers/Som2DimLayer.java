package pl.phytia.ann.layers;

import pl.phytia.ann.neurons.SomNeuron;
import pl.phytia.model.conf.layers.SOMLayerConfiguration;
import pl.phytia.model.sets.DoubleVector;

/**
 * Dwuwymiarowa warstwa sieci SOM.
 * 
 * @author Jarosław Protasiewicz
 */
public class Som2DimLayer extends SOMLayer {

	/**
	 * Neurony warstwy sieci.
	 */
	private SomNeuron[][] somMap;

	/**
	 * Konstruktor bezargumentowy.
	 */
	public Som2DimLayer() {
		super();
	}

	/**
	 * Konstruktor tworzący warstwę i ja inicjalizujący.
	 * 
	 * @param conf
	 *            Konfiguracja warstwy.
	 */
	public Som2DimLayer(SOMLayerConfiguration conf) {
		super(conf);
		somMap = new SomNeuron[conf.getSize()[0]][conf.getSize()[1]];
		for (int i = 0; i < somMap.length; ++i) {
			for (int j = 0; j < somMap[i].length; ++j) {
				somMap[i][j] = new SomNeuron(conf.getNeuronFunctionConf());
			}
		}
	}

	public void simulation(DoubleVector inputs) {
		double minDistance = Double.MAX_VALUE;
		int[] winnerIndx = new int[2];
		/*
		 * Wyznaczenie odległości każdego neuronu od wejścia i wyznaczenie
		 * zwyciezcy.
		 */
		for (int i = 0; i < somMap.length; ++i) {
			for (int j = 0; j < somMap[i].length; ++j) {
				double distance = getDistanceFunction().computeDistance(inputs,
						somMap[i][j].getWeights());
				somMap[i][j].setInputDistance(distance);
				if (distance <= minDistance) {
					minDistance = distance;
					winnerIndx[0] = i;
					winnerIndx[1] = j;
				}
				somMap[i][j].setWinner(false);
			}
		}
		somMap[winnerIndx[0]][winnerIndx[1]].setWinner(true);
		setWinnerIndx(winnerIndx);
	}

	@Override
	public String generateWinnerClassLabel() {
		if (somMap[getWinnerIndx()[0]][getWinnerIndx()[1]].getWinCount() == 0) {
			somMap[getWinnerIndx()[0]][getWinnerIndx()[1]]
					.setClassName(generateNextClassName());
			somMap[getWinnerIndx()[0]][getWinnerIndx()[1]].setWinCount(1);
		} else {
			somMap[getWinnerIndx()[0]][getWinnerIndx()[1]]
					.setWinCount(somMap[getWinnerIndx()[0]][getWinnerIndx()[1]]
							.getWinCount() + 1);
		}
		return somMap[getWinnerIndx()[0]][getWinnerIndx()[1]].getClassName();
	}

	@Override
	public void reset() {
		super.reset();
		for (int i = 0; i < somMap.length; ++i) {
			for (int j = 0; j < somMap[i].length; ++j) {
				somMap[i][j].reset();
			}
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
			DoubleVector win = new DoubleVector(2);
			win.add(new Double(getWinnerIndx()[0]));
			win.add(new Double(getWinnerIndx()[1]));
			DoubleVector pos = new DoubleVector(2);
			pos.add(new Double(0.0));
			pos.add(new Double(0.0));
			for (int x = 0; x < getConfig().getSize()[0]; ++x) {
				pos.set(0, new Double(x));
				for (int y = 0; y < getConfig().getSize()[1]; ++y) {
					pos.set(1, new Double(y));
					double dist = getDistanceFunction().computeDistance(win,
							pos);
					somMap[x][y].setWinnerDistance(dist);
				}
			}
			/*
			 * Wyznaczenie sąsiedztwa
			 */
			for (int x = 0; x < getConfig().getSize()[0]; ++x) {
				for (int y = 0; y < getConfig().getSize()[1]; ++y) {
					double neighbourhood = Math.exp(-Math.pow(somMap[x][y]
							.getWinnerDistance(), 2)
							/ 2
							* Math.pow(getNeighbourhoodFunction()
									.getCurrNeighbourhoodSize(), 2));
					somMap[x][y].setNeighbourhood(neighbourhood);
				}
			}
			break;
		}
		case Rectangular: {
			/*
			 * Wyznaczenie sąsiedztwa
			 */
			for (int x = 0; x < getConfig().getSize()[0]; ++x) {
				for (int y = 0; y < getConfig().getSize()[1]; ++y) {
					if (x >= (getWinnerIndx()[0] - (int) getNeighbourhoodFunction()
							.getCurrNeighbourhoodSize())
							&& x <= (getWinnerIndx()[0] + (int) getNeighbourhoodFunction()
									.getCurrNeighbourhoodSize())
							&& y >= (getWinnerIndx()[1] - (int) getNeighbourhoodFunction()
									.getCurrNeighbourhoodSize())
							&& y <= (getWinnerIndx()[1] + (int) getNeighbourhoodFunction()
									.getCurrNeighbourhoodSize())) {
						somMap[x][y].setNeighbourhood(1);
					} else {
						somMap[x][y].setNeighbourhood(0);
					}
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
			for (int y = 0; y < getConfig().getSize()[1]; ++y) {
				weightChange += somMap[x][y].computeWeightChange();
			}
		}
		return weightChange;
	}

	@Override
	public String getWinnerClass() {
		return somMap[getWinnerIndx()[0]][getWinnerIndx()[1]].getClassName();
	}

	@Override
	public DoubleVector getOutputs() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return wartość pola somMap
	 */
	public SomNeuron[][] getSomMap() {
		return somMap;
	}

	/**
	 * @param somMap
	 *            jest przypisywany do pola somMap
	 */
	public void setSomMap(SomNeuron[][] somMap) {
		this.somMap = somMap;
	}
}
