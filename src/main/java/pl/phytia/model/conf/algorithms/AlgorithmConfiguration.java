package pl.phytia.model.conf.algorithms;

import pl.phytia.model.conf.Configuration;
import pl.phytia.model.enums.EnumFunctionType;

/**
 * Abstrakcyjna klasa - nośnik informacji o konfiguracji algorytmu.
 * 
 * @param <S>
 *            Obiekt, którego stan jest odczytywany lub zapisywany
 * @author Jarosław Protasiewicz
 */
public abstract class AlgorithmConfiguration<S extends AlgorithmConfiguration>
		extends Configuration<S> {

	public AlgorithmConfiguration() {
		super();
	}

	/**
	 * Konstruktor.
	 * 
	 * @param enumFunctionType
	 *            Typ funkcji błędu.
	 * @param stopCondFunctionType
	 *            Typ funkcji błędu dla warunku stop.
	 */
	public AlgorithmConfiguration(EnumFunctionType enumFunctionType,
			EnumFunctionType stopCondFunctionType) {
		super();
		this.maxIteration = 5000;
		this.minError = 0.001;
		this.numerOfRepeat = 1;
		this.errorFunctionType = enumFunctionType;
		this.stopCondFunctionType = stopCondFunctionType;
		this.maxNotFoundBetterSolution = 250;
	}

	/**
	 * Błąd, którego osiągnięcie powoduje zatrzymanie algorytmu.
	 */
	private double minError;

	/**
	 * Maksymalna ilość iteracji.
	 */
	private int maxIteration;

	/**
	 * Liczba powtórzeń algorytmu.
	 */
	private double numerOfRepeat;

	/**
	 * Ile razy można nie znależć lepszego rozwiązania. Po przekroczeniu tej
	 * wartości nastepuje STOP algorytmu.
	 */
	private double maxNotFoundBetterSolution;

	/**
	 * Typ funkcji błędu do oceny jakości treningu. TODO: dodać klasę
	 * SupervisedAlgorithmConfoguration i tam zamieścić ten parametr.
	 */
	private EnumFunctionType errorFunctionType;

	/**
	 * Typ funkcji błędu do warunku stop.
	 */
	private EnumFunctionType stopCondFunctionType;

	/**
	 * @return wartość pola maxIteration
	 */
	public int getMaxIteration() {
		return maxIteration;
	}

	/**
	 * @param maxIteration
	 *            jest przypisywany do pola maxIteration
	 */
	public void setMaxIteration(int maxIteration) {
		this.maxIteration = maxIteration;
	}

	/**
	 * @return wartość pola minError
	 */
	public double getMinError() {
		return minError;
	}

	/**
	 * @param minError
	 *            jest przypisywany do pola minError
	 */
	public void setMinError(double minError) {
		this.minError = minError;
	}

	/**
	 * @return wartość pola numerOfRepeat
	 */
	public double getNumerOfRepeat() {
		return numerOfRepeat;
	}

	/**
	 * @param numerOfRepeat
	 *            jest przypisywany do pola numerOfRepeat
	 */
	public void setNumerOfRepeat(double numerOfRepeat) {
		this.numerOfRepeat = numerOfRepeat;
	}

	/**
	 * @return wartość pola errorFunctionType
	 */
	public EnumFunctionType getErrorFunctionType() {
		return errorFunctionType;
	}

	/**
	 * @param errorFunctionType
	 *            jest przypisywany do pola errorFunctionType
	 */
	public void setErrorFunctionType(EnumFunctionType errorFunctionType) {
		this.errorFunctionType = errorFunctionType;
	}

	/**
	 * @return wartość pola stopCondFunctionType
	 */
	public EnumFunctionType getStopCondFunctionType() {
		return stopCondFunctionType;
	}

	/**
	 * @param stopCondFunctionType
	 *            jest przypisywany do pola stopCondFunctionType
	 */
	public void setStopCondFunctionType(EnumFunctionType stopCondFunctionType) {
		this.stopCondFunctionType = stopCondFunctionType;
	}

	/**
	 * @return wartość pola maxNotFoundBetterSolution
	 */
	public double getMaxNotFoundBetterSolution() {
		return maxNotFoundBetterSolution;
	}

	/**
	 * @param maxNotFoundBetterSolution
	 *            jest przypisywany do pola maxNotFoundBetterSolution
	 */
	public void setMaxNotFoundBetterSolution(double maxNotFoundBetterSolution) {
		this.maxNotFoundBetterSolution = maxNotFoundBetterSolution;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("\n" + this.getClass().toString()
				+ "\n");
		sb.append("minError = " + this.minError + "\n");
		sb.append("maxIteration = " + this.maxIteration + "\n");
		sb.append("numerOfRepeat = " + this.numerOfRepeat + "\n");
		sb.append("maxNotFoundBetterSolution = "
				+ this.maxNotFoundBetterSolution + "\n");
		sb.append("errorFunctionType = " + this.errorFunctionType.toString()
				+ "\n");
		sb.append("stopCondFunctionType = "
				+ this.stopCondFunctionType.toString() + "\n");
		sb.append(super.toString());
		return super.toString();
	}
}
