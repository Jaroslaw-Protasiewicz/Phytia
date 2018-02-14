package pl.phytia.ann.layers;

import java.util.ArrayList;
import java.util.List;

import pl.phytia.ann.neurons.RbfNeuron;
import pl.phytia.model.conf.layers.RbfLayerConfiguration;
import pl.phytia.model.enums.EnumCenterWidthType;
import pl.phytia.model.sets.DoubleVector;

/**
 * Warstwa radialna sieci RBF.
 * 
 * @author Jarosław Protasiewicz
 */
public class RbfLayer extends Layer<RbfLayer, RbfLayerConfiguration> {

	/**
	 * Neurony zawierające funkcje bazowe.
	 */
	private List<RbfNeuron> neurons;

	/**
	 * Konstruktor bezargumentowy.
	 */
	public RbfLayer() {
		super();
	}

	/**
	 * Konstruktor tworzący i inicjalizujący wartwę RBF sieci.
	 * 
	 * @param conf
	 *            Konfiguracja.
	 */
	public RbfLayer(RbfLayerConfiguration conf) {
		super();
		neurons = new ArrayList<RbfNeuron>();
		initialize(conf);
	}

	public void initialize(RbfLayerConfiguration conf) {
		this.config = conf;
	}

	/**
	 * Re-Inicjalizacja warstwy sieci neuronowej - właściwie równoznaczna jej
	 * utowrzeniu. param conf Konfiguracja.
	 * 
	 * @param centers
	 *            Centra funkcji bazowych.
	 */
	public void initialize(RbfLayerConfiguration conf,
			List<DoubleVector> centers) {
		/*
		 * Utwrzenie neuronów - funcji bazowych.
		 */
		neurons = new ArrayList<RbfNeuron>(centers.size());
		for (DoubleVector center : centers) {
			neurons.add(new RbfNeuron(conf.getRbfFunctionConf(), center));
		}
		/*
		 * Określenie rozmycia funkcji bazowych
		 */
		double width = 0.0;
		switch (conf.getCenterWidthType()) {
		case EnumCenterWidthType.CONSTANT:
			width = conf.getRbfFunctionConf().getDefaultWidth();
			break;
		case EnumCenterWidthType.ADAPTIVE:
			width = (findMaxDistanceBetweenCentres(centers) / (double) Math
					.sqrt(2 * centers.size()));
			break;
		default:
			width = (findMaxDistanceBetweenCentres(centers) / Math
					.sqrt(2 * centers.size()));
			break;
		}
		for (RbfNeuron n : neurons) {
			n.getRadialFunction().setWidth(width);
		}
		/*
		 * Zapis konfiguracji.
		 */
		initialize(conf);
	}

	public void simulation(DoubleVector inputs) {
		for (RbfNeuron n : neurons) {
			n.simulation(inputs);
		}
	}

	@Override
	public DoubleVector getOutputs() {
		DoubleVector outs = new DoubleVector(config.getNumberOfNeurons(), 0.0);
		for (int i = 0; i < outs.size(); ++i) {
			outs.set(i, neurons.get(i).getOutput());
		}
		return outs;
	}

	/**
	 * Wyznacza maksymalną odległość pomiędzy centrem funkcji a innymi centrami.
	 * 
	 * @param centers
	 *            Lista centrów sieci.
	 * @return Maksymalna odległość.
	 */
	@SuppressWarnings("unchecked")
	private double findMaxDistanceBetweenCentres(List<DoubleVector> centers) {
		double maxDistance = 0.0, currDistance = Double.MIN_VALUE;
		for (RbfNeuron n : neurons) {
			currDistance = n.getRadialFunction().findMaxDistanceBetweenCentres(
					centers);
			if (maxDistance < currDistance) {
				maxDistance = currDistance;
			}
		}
		return maxDistance;
	}

	@Override
	public double maxReturnValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double minReturnValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<RbfNeuron> getNeurons() {
		return neurons;
	}

	public void setNeurons(List<RbfNeuron> neurons) {
		this.neurons = neurons;
	}

}
