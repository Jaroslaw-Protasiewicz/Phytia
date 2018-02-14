package pl.phytia.ann.algorithms.rbf;

import java.util.List;

import pl.phytia.ann.layers.AdderLayer;
import pl.phytia.ann.layers.RbfLayer;
import pl.phytia.ann.networks.RBFNetwork;
import pl.phytia.ann.neurons.AdderNeuron;
import pl.phytia.ann.neurons.RbfNeuron;
import pl.phytia.model.conf.algorithms.RbfAlgorithmConfiguration;
import pl.phytia.model.sets.DoubleVector;
import pl.phytia.model.sets.SupervisedDataSet;

/**
 * Abstrakcyjny agorytm treningu nadzorowanego sieci RBF
 * 
 * @author Jarosław Protasiewicz
 * 
 * @param <S>
 *            Typ algorytmu
 */
public abstract class RbfSupervisedAlgorithm<S extends RbfBaseAlgorithm>
		extends RbfBaseAlgorithm<S> {

	RbfSupervisedAlgorithm() {

	}

	public RbfSupervisedAlgorithm(RbfAlgorithmConfiguration conf) {
		super(conf);
	}

	/**
	 * Wyznaczenie wartości modyfikacji wagi.
	 * 
	 * @param neuron
	 *            Neuron z funcją bazową.
	 * @param errors
	 *            Błąd / błędy dopasowania sieci.
	 * @return Modyfikacja wagi.
	 */
	// public abstract double computeWeightGradient(RbfNeuron neuron,
	// DoubleVector errors);
	/**
	 * Wyznaczenie wartości modyfikacji centrów.
	 * 
	 * @param neuron
	 *            Neuron z funcją bazową.
	 * @param errors
	 *            Błąd / błędy dopasowania sieci.
	 * @param weight
	 *            Waga na drodze centrum do wyjścia.
	 * @param inputs
	 *            Wejścia sieci.
	 * @return Modyfikacja centrów.
	 */
	// public abstract DoubleVector computeCentersGradient(RbfNeuron neuron,
	// DoubleVector errors, double weight, DoubleVector inputs);
	/**
	 * Wyznaczenie wartości modyfikacji rozmycia.
	 * 
	 * @param neuron
	 *            Neuron z funcją bazową.
	 * @param errors
	 *            Błąd / błędy dopasowania sieci.
	 * @param weight
	 *            Waga na drodze centrum do wyjścia.
	 * @param inputs
	 *            Wejścia sieci.
	 * @return Modyfikacja rozmycia.
	 */
	// public abstract double computeWidthGradient(RbfNeuron neuron,
	// DoubleVector errors, double weight, DoubleVector inputs);

	/**
	 * Modyfikacja wag sieci podczas wykonania algortymu uczenia nadzorowanego.
	 * 
	 * @param net
	 *            Sieć neuronowa.
	 * @param errors
	 *            Błąd / błędy dopasowania sieci.
	 * @param outputs
	 *            Wyjścia warstwy rbf.
	 */
	public void updateWeights(RBFNetwork net, DoubleVector errors,
			DoubleVector[] outputs) {
		RbfLayer rbfLayer = (RbfLayer) net.getLayers().get(0);
		AdderNeuron adder = ((AdderLayer) net.getLayers().get(1)).getNeurons()
				.get(0);
		int indx = 0;
		for (RbfNeuron n : rbfLayer.getNeurons()) {
			double grad = n.getRadialFunction().computeWeightGradient(errors,
					outputs[indx]);
			adder.updateWeight(indx++, getConfig().getWidthLearnigRatio()
					* grad);
		}
	}

	/**
	 * Modyfikacja centrów funkcji bazowych sieci podczas wykonania algortymu
	 * uczenia nadzorowanego.
	 * 
	 * @param net
	 *            Sieć neuronowa.
	 * @param errors
	 *            Błedy dopasowania.
	 * @param outputs
	 *            Wyjścia warstwy rbf.
	 * @param inputs
	 *            Wejścia sieci.
	 */
	@SuppressWarnings("unchecked")
	public void updateCenters(RBFNetwork net, DoubleVector errors,
			DoubleVector[] outputs, List<DoubleVector> inputs) {
		RbfLayer rbfLayer = (RbfLayer) net.getLayers().get(0);
		AdderNeuron adder = ((AdderLayer) net.getLayers().get(1)).getNeurons()
				.get(0);
		int indx = 0;
		DoubleVector deltaCenter = null;
		for (RbfNeuron n : rbfLayer.getNeurons()) {
			deltaCenter = n.getRadialFunction().computeCentersGradient(errors,
					outputs[indx], adder.getWeights().get(indx++), inputs);
			for (int i = 0; i < deltaCenter.size(); ++i) {
				deltaCenter.set(i, deltaCenter.get(i)
						* getConfig().getCentersLearningRatio());
			}
			n.updateFunctionCenter(deltaCenter);
		}
	}

	/**
	 * Modyfikacja rozmycia funkcji bazowych sieci podczas wykonania algortymu
	 * uczenia nadzorowanego.
	 * 
	 * @param net
	 *            Sieć neuronowa.
	 * @param errors
	 *            Błedy dopasowania.
	 * @param outputs
	 *            Wyjścia sieci.
	 * @param inputs
	 *            Wejścia sieci.
	 */
	@SuppressWarnings("unchecked")
	public void updateWidth(RBFNetwork net, DoubleVector errors,
			DoubleVector[] outputs, List<DoubleVector> inputs) {
		RbfLayer rbfLayer = (RbfLayer) net.getLayers().get(0);
		AdderNeuron adder = ((AdderLayer) net.getLayers().get(1)).getNeurons()
				.get(0);
		int indx = 0;
		for (RbfNeuron n : rbfLayer.getNeurons()) {
			n.updateFunctionCenterWidth(getConfig().getWidthLearnigRatio()
					* n.getRadialFunction().computeWidthGradient(errors,
							outputs[indx], adder.getWeights().get(indx++),
							inputs));

		}
	}

	public boolean checkStopCondition(RBFNetwork net, SupervisedDataSet set) {
		double error = getStopCondFunction().computeError(net, set);
		logger.debug("Epoka = " + currEpoch + "Error = " + error);
		if (error < getConfig().getMinError()
				|| currEpoch >= getConfig().getMaxIteration()) {
			return true;
		} else {
			return false;
		}
	}

	public void updateParams(RBFNetwork net, SupervisedDataSet set) {
		// TODO Auto-generated method stub
	}

	public void updateWeights(RBFNetwork net, DoubleVector inputs) {
		// TODO Auto-generated method stub

	}

}
