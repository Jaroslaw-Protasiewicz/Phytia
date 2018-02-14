package pl.phytia.ann.neurons;

import pl.phytia.ann.functions.neuron.LinearFunction;
import pl.phytia.ann.functions.neuron.SigmoidFahlmanFunction;
import pl.phytia.ann.functions.neuron.SigmoidUnipolarFunction;
import pl.phytia.errors.ConfigurationException;
import pl.phytia.model.conf.functions.neuron.FahlmanFunctionConfiguration;
import pl.phytia.model.conf.functions.neuron.LinearFunctionConfiguration;
import pl.phytia.model.conf.functions.neuron.NeuronFunctionConfiguration;
import pl.phytia.model.conf.functions.neuron.SBipolarFunctionConfiguration;
import pl.phytia.model.enums.EnumFunctionType;
import pl.phytia.model.sets.DoubleVector;
import pl.phytia.ann.functions.neuron.SigmoidBipolarFunction;
import pl.phytia.model.conf.functions.neuron.SUnipolarFunctionConfiguration;

/**
 * Perceptoron - element sieci MLP.
 * 
 * @author Jarosław Protasiewicz
 * @param <C>
 *            Typ obiektu konfiguracji.
 * @author Jarosław Protasiewicz
 */
public final class Perceptron<C extends NeuronFunctionConfiguration> extends
		Adder<Perceptron, C> {

	private static final long serialVersionUID = 4814503466347478184L;

	/**
	 * Błąd neuronu.
	 */
	private double error;

	/**
	 * Poprzedni przyrost wag.
	 */
	private DoubleVector recentWeightsChange;

	/**
	 * Poprzedni przyrost momentu.
	 */
	private DoubleVector recentMomentumChange;

	/**
	 * Wagi dla których osiągnięto najlepsze dopasowanie.
	 */
	private DoubleVector theBestWeights;

	/**
	 * Bieżący gradient z zaburzeniem wag.
	 */
	private DoubleVector slope;

	/**
	 * Poprzedni gradient z zaburzeniem wag.
	 */
	private DoubleVector recentSlope;

	/**
	 * Uchyb gradientu.
	 */
	private DoubleVector deltaSlope;

	/* --------------METHODS -------------- */
	/**
	 * Konstruktor bezparametrowy.
	 */
	public Perceptron() {
		super();
	}

	/**
	 * Konstruktor perceptronu.
	 * 
	 * @param c
	 *            Konfiguracja perceptronu i funcji aktwyacji.
	 * @param numberOfInputs
	 *            Ilość wejść do neuronu.
	 */
	@SuppressWarnings("unchecked")
	public Perceptron(C c, int numberOfInputs) {
		super();
		boolean isRightFun = true;
		/*
		 * Utworzenie odpowiedniej funkcji aktywacji.
		 */
		if (c instanceof SUnipolarFunctionConfiguration) {
			function = new SigmoidUnipolarFunction();
			c.setTypeOfFunction(EnumFunctionType.SigmoidalUnipolar);
		} else if (c instanceof SBipolarFunctionConfiguration) {
			function = new SigmoidBipolarFunction();
			c.setTypeOfFunction(EnumFunctionType.SigmoidalBipolar);
		} else if (c instanceof FahlmanFunctionConfiguration) {
			function = new SigmoidFahlmanFunction();
			c.setTypeOfFunction(EnumFunctionType.FahlmanUnipolar);
		} else if (c instanceof LinearFunctionConfiguration) {
			function = new LinearFunction();
			c.setTypeOfFunction(EnumFunctionType.Linear);
		} else {
			isRightFun = false;
		}
		if (isRightFun) {
			/*
			 * Utworzenie wag sieci o wartościach zerowych. Uwzględniane jest
			 * dodatkowe wejście BIAS.
			 */
			weights = new DoubleVector(numberOfInputs + 1);
			recentWeightsChange = new DoubleVector(numberOfInputs + 1);
			recentMomentumChange = new DoubleVector(numberOfInputs + 1);
			recentSlope = new DoubleVector(numberOfInputs + 1);
			slope = new DoubleVector(numberOfInputs + 1);
			deltaSlope = new DoubleVector(numberOfInputs + 1);
			theBestWeights = new DoubleVector(numberOfInputs + 1);
			for (int i = 0; i < numberOfInputs + 1; ++i) {
				weights.add(new Double(0.0));
				recentWeightsChange.add(new Double(0.0));
				recentMomentumChange.add(new Double(0.0));
				recentSlope.add(new Double(0.0));
				slope.add(new Double(0.0));
				deltaSlope.add(new Double(0.0));
				theBestWeights.add(new Double(0.0));
			}
			function.setConfig(c);
		}
	}

	@Override
	public void simulation(DoubleVector inputs) {
		/*
		 * Kontrola poprawności konfiguracji. Wektor wejściowy już zawiera BIAS.
		 */
		if (inputs.size() != getWeights().size()) {
			StringBuilder sbErr = new StringBuilder(
					"Niepoprawny rozmiar wektora wejść i wag neuronu: ");
			sbErr.append(inputs.size() + " <> " + getWeights().size());
			throw new ConfigurationException(sbErr.toString());
		}
		/*
		 * Wyznaczenie sumy neuronu.
		 */
		computeSum(inputs);

		/*
		 * Wyznaczenie wyjścia neuronu.
		 */
		setOutput(function.computeActivation(sum));
	}

	@Override
	public void initialize(C conf) {
		super.initialize(conf);
		for (int i = 0; i < this.recentWeightsChange.size(); ++i) {
			this.recentWeightsChange.set(i, 0.0d);
			recentMomentumChange.set(i, 0.0d);
			recentSlope.set(i, 0.0d);
			slope.set(i, 0.0d);
		}
	}

	/**
	 * Modyfikacja poprzedniej zaminy wagi neuronu.
	 * 
	 * @param indx
	 *            Indeks wagi
	 * @param delta
	 *            Przyrost
	 */
	public void updateRecentWeightChange(int indx, double delta) {
		this.recentWeightsChange.set(indx, delta);
	}

	public void updateRecentMomentumChange(int indx, double momentum) {
		recentMomentumChange.set(indx, recentMomentumChange.get(indx)
				+ momentum);
	}

	public void updateRecentSlope(int indx, double slope, boolean isUpdate) {
		if (isUpdate) {
			recentSlope.set(indx, recentSlope.get(indx) + slope);
		} else {
			recentSlope.set(indx, slope);
		}
	}

	public void updateSlope(int indx, double slope, boolean isUpdate) {
		if (isUpdate) {
			this.slope.set(indx, this.slope.get(indx) + slope);
		} else {
			this.slope.set(indx, slope);
		}
	}

	public void computeDeltaSlope(int indx) {
		double diff = this.recentSlope.get(indx) - this.slope.get(indx);
		if (diff != 0) {
			this.deltaSlope.set(indx, this.slope.get(indx) / diff);
		} else {
			this.deltaSlope.set(indx, Double.MAX_VALUE);
		}
	}

	/**
	 * Ustawia wartości najlepszych wag.
	 * 
	 * @param reverse
	 *            <li>gdy równa się <code>true</code> przepisuje najlepsze
	 *            wagi na wagi</li>
	 *            <li>gdy równa się <code>false</code> przepisuje wagi na
	 *            najlepsze wagi</li>
	 */
	public void setTheBestWeights(boolean reverse) {
		if (reverse) {
			this.weights = this.theBestWeights.clone();
			/*
			 * for (int i = 0; i < this.theBestWeights.size(); ++i) {
			 * this.weights.set(i, new
			 * Double(this.theBestWeights.get(i).doubleValue())); }
			 */
		} else {
			this.theBestWeights = this.weights.clone();
			/*
			 * for (int i = 0; i < this.weights.size(); ++i) {
			 * this.theBestWeights.set(i, new
			 * Double(this.weights.get(i).doubleValue())); }
			 */
		}
	}

	public void setTheBestWeights(DoubleVector weights) {
		this.theBestWeights = weights;
	}

	/* ---------------- SETTERS AND GETTERS ----------------- */
	/**
	 * @return wartość pola error
	 */
	public double getError() {
		return error;
	}

	/**
	 * @param error
	 *            jest przypisywany do pola error
	 */
	public void setError(double error) {
		this.error = error;
	}

	/**
	 * @return wartość pola recentWeightsChange
	 */
	public DoubleVector getRecentWeightsChange() {
		return recentWeightsChange;
	}

	/**
	 * @param recentWeightsChange
	 *            jest przypisywany do pola recentWeightsChange
	 */
	public void setRecentWeightsChange(DoubleVector recentWeightsChange) {
		this.recentWeightsChange = recentWeightsChange;
	}

	/**
	 * @return wartość pola recentMomentumChange
	 */
	public DoubleVector getRecentMomentumChange() {
		return recentMomentumChange;
	}

	/**
	 * @param recentMomentumChange
	 *            jest przypisywany do pola recentMomentumChange
	 */
	public void setRecentMomentumChange(DoubleVector recentMomentumChange) {
		this.recentMomentumChange = recentMomentumChange;
	}

	/**
	 * @return wartość pola recentSlope
	 */
	public DoubleVector getRecentSlope() {
		return recentSlope;
	}

	/**
	 * @param recentSlope
	 *            jest przypisywany do pola recentSlope
	 */
	public void setRecentSlope(DoubleVector recentSlope) {
		this.recentSlope = recentSlope;
	}

	/**
	 * @return wartość pola slope
	 */
	public DoubleVector getSlope() {
		return slope;
	}

	/**
	 * @param slope
	 *            jest przypisywany do pola slope
	 */
	public void setSlope(DoubleVector slope) {
		this.slope = slope;
	}

	/**
	 * @return wartość pola theBestWeights
	 */
	public DoubleVector getTheBestWeights() {
		return theBestWeights;
	}

	public DoubleVector getDeltaSlope() {
		return deltaSlope;
	}

	public void setDeltaSlope(DoubleVector deltaSlope) {
		this.deltaSlope = deltaSlope;
	}

}
