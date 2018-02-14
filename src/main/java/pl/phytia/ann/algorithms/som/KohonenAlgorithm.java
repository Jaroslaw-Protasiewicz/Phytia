package pl.phytia.ann.algorithms.som;

import java.util.Iterator;

import pl.waw.ibspan.phytia.ann.algorithms.Algorithm;
import pl.waw.ibspan.phytia.ann.layers.SOMLayer;
import pl.waw.ibspan.phytia.ann.layers.Som2DimLayer;
import pl.waw.ibspan.phytia.ann.layers.Som3DimLayer;
import pl.waw.ibspan.phytia.ann.networks.SOMNetwork;
import pl.waw.ibspan.phytia.model.conf.algorithms.KohonenConfiguration;
import pl.waw.ibspan.phytia.model.conf.layers.SOMLayerConfiguration;
import pl.waw.ibspan.phytia.model.sets.ClassPatternVO;
import pl.waw.ibspan.phytia.model.sets.DoubleVector;
import pl.waw.ibspan.phytia.model.sets.UnsupervisedDataSet;

/**
 * Algorytm treningu Kohonena. Zakłada się, że sieć ma jedną warstwę.
 * 
 * @author Jarosław Protasiewicz
 * 
 */
public class KohonenAlgorithm
		extends
		Algorithm<KohonenAlgorithm, KohonenConfiguration, UnsupervisedDataSet, SOMNetwork> {

	public KohonenAlgorithm() {
		super();
	}

	public void training(SOMNetwork net, UnsupervisedDataSet set) {
		/*
		 * Dopóki nie spełnione są warunki STOP, ale conajmniej jeden raz.
		 */
		do {
			increaseCurrEpoch();
			reset(net);
			Iterator<ClassPatternVO> it = set.iterator();
			while (it.hasNext()) {
				ClassPatternVO pattern = it.next();
				DoubleVector in = pattern.getInputs();
				// System.out.println("" + pattern.getId());
				/*
				 * Wyznaczenie zwycięzcy.
				 */
				net.simulation(in);
				/*
				 * Przypisanie etykiet klasy
				 */
				String className = ((SOMLayer) net.getLayers().get(0))
						.generateWinnerClassLabel();
				// set.setPatternClassName(pattern.getId(), className);
				pattern.setClassName(className);
				/*
				 * Wyznaczenie sąsiadów.
				 */
				((SOMLayer) net.getLayers().get(0)).computeNeighbourhood();

				/*
				 * int [] zw =
				 * ((SOMLayer)net.getLayers().get(0)).getWinnerIndx();
				 * System.out.println( "Zwycięzca: " + zw[0] + " " + zw[1]);
				 * SomNeuron[][] map =
				 * ((Som2DimLayer)net.getLayers().get(0)).getSomMap(); for (int
				 * x = 0; x < map.length ; ++x) { for (int y = 0; y <
				 * map[x].length; ++y) { System.out.print("["+x+"]"+"["+y+"]=" +
				 * NumberFormat.getNumberInstance().format(map[x][y].getNeighbourhood()) + "
				 * "); } System.out.println(""); } System.out.println("
				 * Sąsiedztwo: " +
				 * ((SOMLayer)net.getLayers().get(0)).getNeighbourhoodFunction().getCurrNeighbourhoodSize());
				 */

				/*
				 * Aktualizacja wag.
				 */
				updateWeights(net, in);
			}
			/*
			 * Aktualizacja sąsiedztwa i współczynnika uczenia.
			 */
			updateParams(net, set);
		} /*
			 * Warunek STOP.
			 */
		while (!checkStopCondition(net, set));

	}

	public boolean checkStopCondition(SOMNetwork net, UnsupervisedDataSet set) {
		double globalWeightChange = ((SOMLayer) net.getLayers().get(0))
				.computeGlobalWeightChange();
		int classCount = ((SOMLayer) net.getLayers().get(0)).getClassIndx();
		System.out.println("Interacja: "
				+ currEpoch
				+ " Ilość klas: "
				+ classCount
				+ " Zmiana wag: "
				+ globalWeightChange
				+ " Sąsiedztwo: "
				+ ((SOMLayer) net.getLayers().get(0))
						.getNeighbourhoodFunction().getCurrNeighbourhoodSize()
				+ " Wsp. uczenia: "
				+ ((SOMLayer) net.getLayers().get(0)).getLearningRateFunction()
						.getLearningRatio());
		/*
		 * Stop jeżeli: ilość epoch nie wieksza niż założona wartośc; LUB
		 * globalna zamian wag nie mniejsza niż założona wartość; LUB osiągnięto
		 * założoną ilość klas.
		 * 
		 */
		if (currEpoch > getConfig().getMaxIteration()) {
			return true;
		} else if (globalWeightChange < config.getMinGlobalWeightsChange()) {
			return true;
		} else if (classCount < config.getMinClasses()) {
			return true;
		} else {
			return false;
		}
	}

	public void updateParams(SOMNetwork net, UnsupervisedDataSet set) {
		/*
		 * Zmiana sąsiedztwa
		 */
		((SOMLayer) net.getLayers().get(0)).getNeighbourhoodFunction()
				.changeNeighbourhood(currEpoch);
		/*
		 * Zmiana współczynnika uczenia
		 */
		((SOMLayer) net.getLayers().get(0)).getLearningRateFunction()
				.computeLearningRatio(currEpoch);
	}

	public void updateWeights(SOMNetwork net, DoubleVector inputs) {
		SOMLayerConfiguration layerConf = (SOMLayerConfiguration) net
				.getConfig().getLayersConf().get(0);

		switch (layerConf.getDimension()) {
		case 1: {
			throw new UnsupportedOperationException(
					"Nie obsługiwany wymiar matrycy!");
		}
		case 2: {
			Som2DimLayer layer = (Som2DimLayer) net.getLayers().get(0);
			for (int x = 0; x < layerConf.getSize()[0]; ++x) {
				for (int y = 0; y < layerConf.getSize()[1]; ++y) {
					DoubleVector weights = layer.getSomMap()[x][y].getWeights();
					layer.getSomMap()[x][y].setRecentWeights(weights.clone());
					double neighbourhood = layer.getSomMap()[x][y]
							.getNeighbourhood();
					for (int w = 0; w < weights.size(); ++w) {
						double delta = neighbourhood
								* layer.getLearningRateFunction()
										.getLearningRatio()
								* (inputs.get(w) - weights.get(w));
						layer.getSomMap()[x][y].updateWeight(w, delta);
					}
				}
			}
			break;
		}
		case 3: {
			Som3DimLayer layer = (Som3DimLayer) net.getLayers().get(0);
			for (int x = 0; x < layerConf.getSize()[0]; ++x) {
				for (int y = 0; y < layerConf.getSize()[1]; ++y) {
					for (int z = 0; z < layerConf.getSize()[2]; ++z) {
						DoubleVector weights = layer.getSomMap()[x][y][z]
								.getWeights();
						layer.getSomMap()[x][y][z].setRecentWeights(weights
								.clone());
						double neighbourhood = layer.getSomMap()[x][y][z]
								.getNeighbourhood();
						for (int w = 0; w < weights.size(); ++w) {
							double delta = neighbourhood
									* layer.getLearningRateFunction()
											.getLearningRatio()
									* (inputs.get(w) - weights.get(w));
							layer.getSomMap()[x][y][z].updateWeight(w, delta);
						}
					}
				}
			}
			break;
		}
		default:
			throw new UnsupportedOperationException(
					"Nie obsługiwany wymiar matrycy");

		}
	}

	private void reset(SOMNetwork net) {
		((SOMLayer) net.getLayers().get(0)).reset();
	}
}
