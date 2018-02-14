package pl.phytia.ann.neurons;

import pl.phytia.ann.functions.neuron.PipeFunction;
import pl.phytia.model.sets.DoubleVector;
import pl.phytia.utils.MathUtil;
import pl.phytia.model.conf.functions.neuron.SomNeuronConfiguration;

/**
 * Neuron - element sieci SOM.
 * 
 * @author Jarosław Protasiewicz
 */
public final class SomNeuron extends Adder<SomNeuron, SomNeuronConfiguration> {

	private static final long serialVersionUID = 4205195205727644232L;

	/**
	 * Odległość do wejścia
	 */
	private double inputDistance;

	/**
	 * Odległość do zwycięzcy.
	 */
	private double winnerDistance;

	/**
	 * Wartość sąsiedztwa.
	 */
	private double neighbourhood;

	/**
	 * Określa zwycięzcę.
	 */
	private boolean winner = false;

	/**
	 * Nazwa klasy, którą reprezentuje neuron.
	 */
	private String className;

	/**
	 * Ilość zwycięst w symulacji.
	 */
	private int winCount = 0;

	/**
	 * Poprzednie wagi
	 */
	private DoubleVector recentWeights;

	/**
	 * Domyślny konstruktor.
	 */
	@SuppressWarnings("unchecked")
	public SomNeuron() {
		super();
	}

	public SomNeuron(SomNeuronConfiguration conf) {
		super();
		function = new PipeFunction();
		weights = new DoubleVector(conf.getNumberOfInputs());
		recentWeights = new DoubleVector(conf.getNumberOfInputs());
		for (int i = 0; i < conf.getNumberOfInputs(); ++i) {
			weights.add(new Double(0.0));
			recentWeights.add(new Double(0.0));
		}
		this.initialize(conf);
	}

	@SuppressWarnings( { "unchecked", "static-access" })
	@Override
	public void initialize(SomNeuronConfiguration conf) {
		function.initialize(conf);
		MathUtil m = MathUtil.getInstance();
		for (int i = 0; i < conf.getNumberOfInputs(); ++i) {
			weights.set(i, m.getRandomDouble(function.minReturnValue(),
					function.maxReturnValue()));
			recentWeights.set(i, new Double(0.0));
		}
	}

	public void reset() {
		className = null;
		winCount = 0;
		winner = false;
	}

	/**
	 * Wyaznacza sumaryczną wartośc zamiany wag neuronu
	 * 
	 * @return Zmiana wag.
	 */
	public double computeWeightChange() {
		double weightChange = 0.0;
		for (int i = 0; i < weights.size(); ++i) {
			weightChange += Math.abs(weights.get(i) - recentWeights.get(i));
		}
		return weightChange;
	}

	/**
	 * @return wartość pola inputDistance
	 */
	public double getInputDistance() {
		return inputDistance;
	}

	/**
	 * @param inputDistance
	 *            jest przypisywany do pola inputDistance
	 */
	public void setInputDistance(double inputDistance) {
		this.inputDistance = inputDistance;
	}

	/**
	 * @return wartość pola winner
	 */
	public boolean isWinner() {
		return winner;
	}

	/**
	 * @param winner
	 *            jest przypisywany do pola winner
	 */
	public void setWinner(boolean winner) {
		this.winner = winner;
	}

	/**
	 * @return wartość pola winnerDistance
	 */
	public double getWinnerDistance() {
		return winnerDistance;
	}

	/**
	 * @param winnerDistance
	 *            jest przypisywany do pola winnerDistance
	 */
	public void setWinnerDistance(double winnerDistance) {
		this.winnerDistance = winnerDistance;
	}

	/**
	 * @return wartość pola neighbourhood
	 */
	public double getNeighbourhood() {
		return neighbourhood;
	}

	/**
	 * @param neighbourhood
	 *            jest przypisywany do pola neighbourhood
	 */
	public void setNeighbourhood(double neighbourhood) {
		this.neighbourhood = neighbourhood;
	}

	/**
	 * @return wartość pola className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className
	 *            jest przypisywany do pola className
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * @return wartość pola winCount
	 */
	public int getWinCount() {
		return winCount;
	}

	/**
	 * @param winCount
	 *            jest przypisywany do pola winCount
	 */
	public void setWinCount(int winCount) {
		this.winCount = winCount;
	}

	/**
	 * @return wartość pola recentWeights
	 */
	public DoubleVector getRecentWeights() {
		return recentWeights;
	}

	/**
	 * @param recentWeights
	 *            jest przypisywany do pola recentWeights
	 */
	public void setRecentWeights(DoubleVector recentWeights) {
		this.recentWeights = recentWeights;
	}

}
