package pl.phytia.ann.functions.neuron;

import java.util.List;

import pl.phytia.ann.functions.distance.DistanceFunction;
import pl.phytia.ann.functions.distance.DistanceFunctionFactory;
import pl.phytia.model.conf.functions.neuron.NeuronFunctionConfiguration;
import pl.phytia.model.enums.EnumDistanceType;
import pl.phytia.model.sets.DoubleVector;

/**
 * Abstrakcyjna funkcja radialna.
 * 
 * @param <C>
 *            Typ obiektu z parametrami konfiguracyjnymi
 * @param <S>
 *            Typ obietu zwaracanego przez <code>this</code>
 * @author Jarosław Protasiewicz
 */
public abstract class RadialFunction<C extends NeuronFunctionConfiguration, S extends RadialFunction>
		extends NeuronFunction<C, S> {

	/**
	 * Centrum funkcji radialnej.
	 */
	private DoubleVector center;

	/**
	 * Szerokość funkcji radialnej.
	 */
	private double width;

	/**
	 * Odpowiedź funkcji radialnej.
	 */
	private double output;

	/**
	 * Funkcja odległości.
	 */
	private DistanceFunction distanceFunction;

	/**
	 * Konstruktor bezargumentowy.
	 */
	public RadialFunction() {
		super();
	}

	/**
	 * Konstruktor
	 * 
	 * @param inputs
	 *            Ilość wejść.
	 * @param distanceType
	 *            Typ funkcji odległości.
	 */
	public RadialFunction(int inputs, EnumDistanceType distanceType) {
		super();
		this.center = new DoubleVector(inputs, 0.0);
		this.distanceFunction = DistanceFunctionFactory
				.getDistanceFunction(distanceType);
	}

	/**
	 * Konstruktor wypełnijacy pola.
	 * 
	 * @param center
	 *            Centrum funkcji radialnej.
	 * @param width
	 *            Szerokość funkcji radialnej.
	 * @param distanceType
	 *            Typ funkcji odległości.
	 */
	public RadialFunction(DoubleVector center, double width,
			EnumDistanceType distanceType) {
		super();
		this.center = center;
		this.width = width;
		this.distanceFunction = DistanceFunctionFactory
				.getDistanceFunction(distanceType);
	}

	/**
	 * Wyznacza maksymalną odległość pomiędzy centrem funkcji a innymi centrami.
	 * 
	 * @param centers
	 *            Lista centrów sieci.
	 * @return Maksymalna odległość.
	 */
	public double findMaxDistanceBetweenCentres(List<DoubleVector> centers) {
		double maxDistance = 0.0, currDistance = Double.MIN_VALUE;
		for (DoubleVector otherCenter : centers) {
			currDistance = distanceFunction
					.computeDistance(center, otherCenter);
			if (maxDistance < currDistance) {
				maxDistance = currDistance;
			}
		}
		return maxDistance;
	}

	/**
	 * Oblicza wyjście funkcji radialnej
	 * 
	 * @param inputs
	 *            wartości wejść
	 * @return wyście neuronu
	 */
	public abstract double computeOutput(DoubleVector inputs);

	/**
	 * Wyznacza gradient dla modyfikacji wag.
	 * 
	 * @param errors
	 *            Błędy dopasowania wyjścia sieci.
	 * @param outputs
	 *            Wyjścia sieci.
	 * @return Gradient wagi.
	 */
	public abstract double computeWeightGradient(DoubleVector errors,
			DoubleVector outputs);

	/**
	 * Wyznacza gradient dla modyfikacji centrów funkcji bazowej.
	 * 
	 * @param errors
	 *            Błędy dopasowania wyjścia sieci.
	 * @param outputs
	 *            Wyjścia sieci.
	 * @param weight
	 *            Waga, dla która zajduje się na drodze sygnału od cnetrum do
	 *            wyjścia.
	 * @param inputs
	 *            Wejścia sieci.
	 * @return Gradient centrów funkcji bazowej.
	 */
	public abstract DoubleVector computeCentersGradient(DoubleVector errors,
			DoubleVector outputs, double weight, List<DoubleVector> inputs);

	/**
	 * Wyznacza gradient dla modyfikacji rozycia funcji bazowej.
	 * 
	 * @param errors
	 *            Błędy dopasowania wyjścia sieci.
	 * @param outputs
	 *            Wyjścia sieci.
	 * @param weight
	 *            Waga, dla która zajduje się na drodze sygnału od cnetrum do
	 *            wyjścia.
	 * @param inputs
	 *            Wejścia sieci.
	 * @return Gradient rozmycia funkcji bazowej.
	 */
	public abstract double computeWidthGradient(DoubleVector errors,
			DoubleVector outputs, double weight, List<DoubleVector> inputs);

	/**
	 * @return Centrum funkcji radialnej.
	 */
	public DoubleVector getCenter() {
		return center;
	}

	/**
	 * @param center
	 *            Centrum funkcji radialnej.
	 */
	public void setCenter(DoubleVector center) {
		this.center = center;
	}

	/**
	 * @return Szerokość funkcji radialnej.
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * @param width
	 *            Szerokość funkcji radialnej.
	 */
	public void setWidth(double width) {
		this.width = width;
	}

	/**
	 * @return Funkcja odległości.
	 */
	public DistanceFunction getDistanceFunction() {
		return distanceFunction;
	}

	/**
	 * @param distanceFunction
	 *            Funkcja odległości.
	 */
	public void setDistanceFunction(DistanceFunction distanceFunction) {
		this.distanceFunction = distanceFunction;
	}

	public double getOutput() {
		return output;
	}

	public void setOutput(double output) {
		this.output = output;
	}

}
