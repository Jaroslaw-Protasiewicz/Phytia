package pl.phytia.model.conf.pred;

import java.text.ParseException;
import java.util.Date;

import pl.phytia.model.conf.Configuration;
import pl.phytia.utils.Localization;

/**
 * Parametry konfiguracyjne abstracyjnego modelu prognostycznego.
 * 
 * @author Jarosław Protasiewicz
 */
public abstract class ModelConfiguration<M extends ModelConfiguration> extends
		Configuration<M> {

	private static final long serialVersionUID = 3796923329450749511L;

	/**
	 * Konstruktor bezargumentowy. <br>
	 * Ustawia domyślna konfigurację modelu.
	 */
	public ModelConfiguration() {
		super();
		this.inputTemperatureDelay = 1;
		this.inputHumidityDelay = 0;
		this.inputInsolationDelay = 0;
		this.inputEnergyDelay = 24;
		this.inputWindowSizeFrom = -15;
		this.inputWindowSizeTo = 15;
		this.inputWindowYearsDelay = -2;
		this.outputPredictionSteps = 24;
		this.normRatio = 0.1;
		try {
			this.maxTrainDataStart = Localization.plDateFormatMedium
					.parse("2002-01-02");
			this.maxTrainDataStop = Localization.plDateFormatMedium
					.parse("2003-12-31");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Konstruktor wypełniający wszystkie parametry konfiguracyjne.
	 * 
	 * @param inputTemperatureDelay
	 *            Opóźnienie (regresja) wejścia modelu - temperatura.
	 * @param inputHumidityDelay
	 *            Opóźnienie (regresja) wejścia modelu - wilgotność.
	 * @param inputInsolationDelay
	 *            Opóźnienie (regresja) wejścia modelu - nasłonecznienie.
	 * @param inputEnergyDelay
	 *            Opóźnienie (regresja) wejścia modelu - energia elektryczna.
	 * @param inputWindowSizeFrom
	 *            Rozmiar okna pomiarowego - oznacza liczbę dni na lewo od dnia
	 *            prognozy.
	 * @param inputWindowSizeTo
	 *            Rozmiar okna pomiarowego - oznacza liczbę dni na prawo od dnia
	 *            prognozy.
	 * @param inputWindowYearsDelay
	 *            Rozmiar okna pomiarowego - oznacza liczbę lat wstecz jakie
	 *            obejmuje.
	 * @param outputPredictionSteps
	 *            Wyprzedzenie prognozy (ilość kroków) wykonywanej przez model.
	 * @param normRatio
	 *            Współczynnik normalizacji - dodawany / odejmowany od min/max
	 *            zakresu normalizacji.
	 * @param maxTrainDataStart
	 *            Maksymalny zakres danych treningowych.
	 * @param maxTrainDataStop
	 *            Maksymalny zakres danych treningowych.
	 * @param modelType
	 *            Typ modelu.
	 */
	public ModelConfiguration(int inputTemperatureDelay,
			int inputHumidityDelay, int inputInsolationDelay,
			int inputEnergyDelay, int inputWindowSizeFrom,
			int inputWindowSizeTo, int inputWindowYearsDelay,
			int outputPredictionSteps, double normRatio,
			Date maxTrainDataStart, Date maxTrainDataStop, String modelType) {
		super();
		this.inputTemperatureDelay = inputTemperatureDelay;
		this.inputHumidityDelay = inputHumidityDelay;
		this.inputInsolationDelay = inputInsolationDelay;
		this.inputEnergyDelay = inputEnergyDelay;
		this.inputWindowSizeFrom = inputWindowSizeFrom;
		this.inputWindowSizeTo = inputWindowSizeTo;
		this.inputWindowYearsDelay = inputWindowYearsDelay;
		this.outputPredictionSteps = outputPredictionSteps;
		this.normRatio = normRatio;
		if (maxTrainDataStart != null) {
			this.maxTrainDataStart = maxTrainDataStart;
		} else {
			try {
				this.maxTrainDataStart = Localization.plDateFormatMedium
						.parse("2002-01-02");

			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (maxTrainDataStop != null) {
			this.maxTrainDataStop = maxTrainDataStop;
		} else {
			try {
				this.maxTrainDataStop = Localization.plDateFormatMedium
						.parse("2003-12-31");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		this.modelType = modelType;
	}

	/**
	 * Opóźnienie (regresja) wejścia modelu - temperatura.
	 * <li><code>0</code> - oznacza brak wejścia;</li>
	 * <li><code>1,2....</code> - oznacza stopień regresji.</li>
	 */
	private int inputTemperatureDelay;

	/**
	 * Opóźnienie (regresja) wejścia modelu - wilgotność.
	 * <li><code>0</code> - oznacza brak wejścia;</li>
	 * <li><code>1,2....</code> - oznacza stopień regresji.</li>
	 */
	private int inputHumidityDelay;

	/**
	 * Opóźnienie (regresja) wejścia modelu - nasłonecznienie.
	 * <li><code>0</code> - oznacza brak wejścia;</li>
	 * <li><code>1,2....</code> - oznacza stopień regresji.</li>
	 */
	private int inputInsolationDelay;

	/**
	 * Opóźnienie (regresja) wejścia modelu - energia elektryczna.
	 * <li><code>0</code> - oznacza brak wejścia;</li>
	 * <li><code>1,2....</code> - oznacza stopień regresji.</li>
	 */
	private int inputEnergyDelay;

	/**
	 * Rozmiar okna pomiarowego - oznacza liczbę dni na lewo od dnia prognozy.
	 */
	private int inputWindowSizeFrom;

	/**
	 * Rozmiar okna pomiarowego - oznacza liczbę dni na prawo od dnia prognozy.
	 */
	private int inputWindowSizeTo;

	/**
	 * Rozmiar okna pomiarowego - oznacza liczbę lat wstecz jakie obejmuje.
	 * Przyczym <br>:
	 * <li><code>0</code> - oznacza tylko bieżący rok;</li>
	 * <li><code>1,2....</code> - oznacza kolejne lata wstecz.</li>
	 */
	private int inputWindowYearsDelay;

	/**
	 * Wyprzedzenie prognozy (ilość kroków) wykonywanej przez model.
	 */
	private int outputPredictionSteps;

	/**
	 * Współczynnik normalizacji.
	 */
	private double normRatio;

	/**
	 * Maksymalny zakres danych treningowych.
	 */
	private Date maxTrainDataStart;

	/**
	 * Maksymalny zakres danych treningowych.
	 */
	private Date maxTrainDataStop;

	/**
	 * Typ modelu.
	 */
	private String modelType;

	/**
	 * @return wartość pola inputEnergyDelay
	 */
	public int getInputEnergyDelay() {
		return inputEnergyDelay;
	}

	/**
	 * @param inputEnergyDelay
	 *            jest przypisywany do pola inputEnergyDelay
	 */
	public void setInputEnergyDelay(int inputEnergyDelay) {
		this.inputEnergyDelay = inputEnergyDelay;
	}

	/**
	 * @return wartość pola inputHumidityDelay
	 */
	public int getInputHumidityDelay() {
		return inputHumidityDelay;
	}

	/**
	 * @param inputHumidityDelay
	 *            jest przypisywany do pola inputHumidityDelay
	 */
	public void setInputHumidityDelay(int inputHumidityDelay) {
		this.inputHumidityDelay = inputHumidityDelay;
	}

	/**
	 * @return wartość pola inputInsolationDelay
	 */
	public int getInputInsolationDelay() {
		return inputInsolationDelay;
	}

	/**
	 * @param inputInsolationDelay
	 *            jest przypisywany do pola inputInsolationDelay
	 */
	public void setInputInsolationDelay(int inputInsolationDelay) {
		this.inputInsolationDelay = inputInsolationDelay;
	}

	/**
	 * @return wartość pola inputTemperatureDelay
	 */
	public int getInputTemperatureDelay() {
		return inputTemperatureDelay;
	}

	/**
	 * @param inputTemperatureDelay
	 *            jest przypisywany do pola inputTemperatureDelay
	 */
	public void setInputTemperatureDelay(int inputTemperatureDelay) {
		this.inputTemperatureDelay = inputTemperatureDelay;
	}

	/**
	 * @return wartość pola inputWindowSizeFrom
	 */
	public int getInputWindowSizeFrom() {
		return inputWindowSizeFrom;
	}

	/**
	 * @param inputWindowSizeFrom
	 *            jest przypisywany do pola inputWindowSizeFrom
	 */
	public void setInputWindowSizeFrom(int inputWindowSizeFrom) {
		this.inputWindowSizeFrom = inputWindowSizeFrom;
	}

	/**
	 * @return wartość pola inputWindowSizeTo
	 */
	public int getInputWindowSizeTo() {
		return inputWindowSizeTo;
	}

	/**
	 * @param inputWindowSizeTo
	 *            jest przypisywany do pola inputWindowSizeTo
	 */
	public void setInputWindowSizeTo(int inputWindowSizeTo) {
		this.inputWindowSizeTo = inputWindowSizeTo;
	}

	/**
	 * @return wartość pola inputWindowYearsDelay
	 */
	public int getInputWindowYearsDelay() {
		return inputWindowYearsDelay;
	}

	/**
	 * @param inputWindowYearsDelay
	 *            jest przypisywany do pola inputWindowYearsDelay
	 */
	public void setInputWindowYearsDelay(int inputWindowYearsDelay) {
		this.inputWindowYearsDelay = inputWindowYearsDelay;
	}

	/**
	 * @return wartość pola outputPredictionSteps
	 */
	public int getOutputPredictionSteps() {
		return outputPredictionSteps;
	}

	/**
	 * @param outputPredictionSteps
	 *            jest przypisywany do pola outputPredictionSteps
	 */
	public void setOutputPredictionSteps(int outputPredictionSteps) {
		this.outputPredictionSteps = outputPredictionSteps;
	}

	/**
	 * @return wartość pola normRatio
	 */
	public double getNormRatio() {
		return normRatio;
	}

	/**
	 * @param normRatio
	 *            jest przypisywany do pola normRatio
	 */
	public void setNormRatio(double normRatio) {
		this.normRatio = normRatio;
	}

	/**
	 * @return wartość pola maxTrainDataStart
	 */
	public Date getMaxTrainDataStart() {
		return maxTrainDataStart;
	}

	/**
	 * @param maxTrainDataStart
	 *            jest przypisywany do pola maxTrainDataStart
	 */
	public void setMaxTrainDataStart(Date maxTrainDataStart) {
		this.maxTrainDataStart = maxTrainDataStart;
	}

	/**
	 * @return wartość pola maxTrainDataStop
	 */
	public Date getMaxTrainDataStop() {
		return maxTrainDataStop;
	}

	/**
	 * @param maxTrainDataStop
	 *            jest przypisywany do pola maxTrainDataStop
	 */
	public void setMaxTrainDataStop(Date maxTrainDataStop) {
		this.maxTrainDataStop = maxTrainDataStop;
	}

	/**
	 * @return wartość pola modelType
	 */
	public String getModelType() {
		return modelType;
	}

	/**
	 * @param modelType
	 *            jest przypisywany do pola modelType
	 */
	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("\n" + this.getClass().toString()
				+ "\n");
		sb.append("inputTemperatureDelay = " + this.inputTemperatureDelay
				+ "\n");
		sb.append("inputHumidityDelay = " + this.inputHumidityDelay + "\n");
		sb.append("inputInsolationDelay = " + this.inputInsolationDelay + "\n");
		sb.append("inputEnergyDelay = " + this.inputEnergyDelay + "\n");
		sb.append("inputWindowSizeFrom = " + this.inputWindowSizeFrom + "\n");
		sb.append("inputWindowSizeTo = " + this.inputWindowSizeTo + "\n");
		sb.append("inputWindowYearsDelay = " + this.inputWindowYearsDelay
				+ "\n");
		sb.append("outputPredictionSteps = " + this.outputPredictionSteps
				+ "\n");
		sb.append("normRatio = " + this.normRatio + "\n");
		sb.append(super.toString());
		return sb.toString();
	}
}
