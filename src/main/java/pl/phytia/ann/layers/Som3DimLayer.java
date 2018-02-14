package pl.phytia.ann.layers;

import pl.phytia.ann.neurons.SomNeuron;
import pl.phytia.model.conf.layers.SOMLayerConfiguration;
import pl.phytia.model.sets.DoubleVector;

/**
 * Trójwymiarowa matryca warstwy sieci samoorganizujacej się.
 * 
 * @author Jarosław Protasiewicz
 */
public class Som3DimLayer extends SOMLayer {

	/**
	 * Neurony warstwy sieci.
	 */
	private SomNeuron[][][] somMap;

	/**
	 * Konstruktor bezargumentowy.
	 */
	public Som3DimLayer() {
		super();
	}

	/**
	 * Konstruktor tworzący warstwę i ja inicjalizujący.
	 * 
	 * @param conf
	 *            Konfiguracja warstwy.
	 */
	public Som3DimLayer(SOMLayerConfiguration conf) {
		super(conf);
		somMap = new SomNeuron[conf.getSize()[0]][conf.getSize()[1]][conf
				.getSize()[2]];
		for (int x = 0; x < somMap.length; ++x) {
			for (int y = 0; y < somMap[x].length; ++y) {
				for (int z = 0; z < somMap[x][y].length; ++z) {
					somMap[x][y][z] = new SomNeuron(conf
							.getNeuronFunctionConf());
				}
			}
		}
	}

	public void simulation(DoubleVector inputs) {
		double minDistance = Double.MAX_VALUE;
		int[] winnerIndx = new int[3];
		/*
		 * Wyznaczenie odległości każdego neuronu od wejścia i wyznaczenie
		 * zwyciezcy.
		 */
		for (int x = 0; x < somMap.length; ++x) {
			for (int y = 0; y < somMap[x].length; ++y) {
				for (int z = 0; z < somMap[x][y].length; ++z) {
					double distance = getDistanceFunction().computeDistance(
							inputs, somMap[x][y][z].getWeights());
					somMap[x][y][z].setInputDistance(distance);
					if (distance <= minDistance) {
						minDistance = distance;
						winnerIndx[0] = x;
						winnerIndx[1] = y;
						winnerIndx[2] = z;
					}
					somMap[x][y][z].setWinner(false);
				}
			}
		}
		somMap[winnerIndx[0]][winnerIndx[1]][winnerIndx[1]].setWinner(true);
		setWinnerIndx(winnerIndx);
	}

	@Override
	public String generateWinnerClassLabel() {
		if (somMap[getWinnerIndx()[0]][getWinnerIndx()[1]][getWinnerIndx()[2]]
				.getWinCount() == 0) {
			somMap[getWinnerIndx()[0]][getWinnerIndx()[1]][getWinnerIndx()[2]]
					.setClassName(generateNextClassName());
			somMap[getWinnerIndx()[0]][getWinnerIndx()[1]][getWinnerIndx()[2]]
					.setWinCount(1);
		} else {
			somMap[getWinnerIndx()[0]][getWinnerIndx()[1]][getWinnerIndx()[2]]
					.setWinCount(somMap[getWinnerIndx()[0]][getWinnerIndx()[1]][getWinnerIndx()[2]]
							.getWinCount() + 1);
		}
		return somMap[getWinnerIndx()[0]][getWinnerIndx()[1]][getWinnerIndx()[2]]
				.getClassName();
	}

	@Override
	public void reset() {
		super.reset();
		for (int x = 0; x < somMap.length; ++x) {
			for (int y = 0; y < somMap[x].length; ++y) {
				for (int z = 0; z < somMap[x][y].length; ++z) {
					somMap[x][y][z].reset();
				}
			}
		}
	}

	@Override
	public double computeGlobalWeightChange() {
		double weightChange = 0;
		for (int x = 0; x < somMap.length; ++x) {
			for (int y = 0; y < somMap[x].length; ++y) {
				for (int z = 0; z < somMap[x][y].length; ++z) {
					weightChange += somMap[x][y][z].computeWeightChange();
				}
			}
		}
		return weightChange;
	}

	@Override
	public void computeNeighbourhood() {
		switch (getConfig().getNeighbourhoodConfiguration()
				.getNeighbourhoodFunctionType()) {
		case Gaussian: {
			/*
			 * wyznaczenie odległości od zwycięzcy
			 */
			DoubleVector win = new DoubleVector(3);
			win.add(new Double(getWinnerIndx()[0]));
			win.add(new Double(getWinnerIndx()[1]));
			win.add(new Double(getWinnerIndx()[2]));
			DoubleVector pos = new DoubleVector(3);
			pos.add(new Double(0.0));
			pos.add(new Double(0.0));
			pos.add(new Double(0.0));
			for (int x = 0; x < somMap.length; ++x) {
				pos.set(0, new Double(x));
				for (int y = 0; y < getConfig().getSize()[1]; ++y) {
					pos.set(1, new Double(y));
					for (int z = 0; z < somMap[x][y].length; ++z) {
						pos.set(2, new Double(z));
						double dist = getDistanceFunction().computeDistance(
								win, pos);
						somMap[x][y][z].setWinnerDistance(dist);
					}
				}
			}
			/*
			 * Wyznaczenie sąsiedztwa
			 */
			for (int x = 0; x < somMap.length; ++x) {
				for (int y = 0; y < somMap[x].length; ++y) {
					for (int z = 0; z < somMap[x][y].length; ++z) {
						double neighbourhood = Math.exp(-Math.pow(
								somMap[x][y][z].getWinnerDistance(), 2)
								/ 2
								* Math.pow(getNeighbourhoodFunction()
										.getCurrNeighbourhoodSize(), 2));
						somMap[x][y][z].setNeighbourhood(neighbourhood);
					}
				}
			}
			break;
		}
		case Rectangular: {
			/*
			 * Wyznaczenie sąsiedztwa
			 */
			for (int x = 0; x < somMap.length; ++x) {
				for (int y = 0; y < somMap[x].length; ++y) {
					for (int z = 0; z < somMap[x][y].length; ++z) {
						if (x >= (getWinnerIndx()[0] - (int) getNeighbourhoodFunction()
								.getCurrNeighbourhoodSize())
								&& x <= (getWinnerIndx()[0] + (int) getNeighbourhoodFunction()
										.getCurrNeighbourhoodSize())
								&& y >= (getWinnerIndx()[1] - (int) getNeighbourhoodFunction()
										.getCurrNeighbourhoodSize())
								&& y <= (getWinnerIndx()[1] + (int) getNeighbourhoodFunction()
										.getCurrNeighbourhoodSize())
								&& z >= (getWinnerIndx()[2] - (int) getNeighbourhoodFunction()
										.getCurrNeighbourhoodSize())
								&& z <= (getWinnerIndx()[2] + (int) getNeighbourhoodFunction()
										.getCurrNeighbourhoodSize())) {
							somMap[x][y][z].setNeighbourhood(1);
						} else {
							somMap[x][y][z].setNeighbourhood(0);
						}
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
	public DoubleVector getOutputs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getWinnerClass() {
		return somMap[getWinnerIndx()[0]][getWinnerIndx()[1]][getWinnerIndx()[2]]
				.getClassName();
	}

	public SomNeuron[][][] getSomMap() {
		return somMap;
	}

	public void setSomMap(SomNeuron[][][] somMap) {
		this.somMap = somMap;
	}

}
