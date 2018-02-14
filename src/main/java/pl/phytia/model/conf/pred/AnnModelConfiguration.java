package pl.phytia.model.conf.pred;

import java.text.ParseException;
import java.util.Date;

import pl.phytia.model.enums.EnumNetworkType;
import pl.phytia.utils.Localization;

public class AnnModelConfiguration extends
		ModelConfiguration<AnnModelConfiguration> {

	private static final long serialVersionUID = 6002574927648340727L;

	/**
	 * Rodzaj sieci neuronowej użyty w modelu.
	 */
	private EnumNetworkType networkType;

	/**
	 * Decyduje o tym czy ma być stosowana normalizacja.
	 */
	private boolean withNormalization;

	/**
	 * Decyduje o intelignetym doczytywnaiu zbioru treningowego
	 */
	private boolean lazySetLoading;

	/**
	 * Decyduje o tym czy uwzględniać typ dnia.
	 */
	private boolean inputDayType;

	/**
	 * Decyduje o tym czy uwzględniać typ świąt.
	 */
	private boolean inputHoliday;

	/**
	 * Wartość opóxnienia średniej kroczacej 24-ro godzinnej.
	 */
	private int inputEMean24Delay;

	/**
	 * Wartość opóźnienia średniej kroczącej 168-ro godzinnej
	 */
	private int inputEMean168Delay;

	/**
	 * Rodzaj alogrytmu modelowania.
	 */
	// private EnumAlgorithmType algorithmType;
	/**
	 * 
	 */
	public AnnModelConfiguration() {
		super();
		try {
			setMaxTrainDataStart(Localization.plDateFormatMedium
					.parse("2002-01-02"));
			setMaxTrainDataStop(Localization.plDateFormatMedium
					.parse("2003-12-31"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Konstruktor wypełniający wszystkie parametry konfiguracyjne.
	 * 
	 * @param networkType
	 *            Rodzaj sieci neuronowej.
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
	 * @param withNormalization
	 *            Decyduje o tym czy ma być stosowana normalizacja.
	 * @param lazySetLoading
	 *            Decyduje o intelignetym doczytywnaiu zbioru treningowego.
	 */
	public AnnModelConfiguration(EnumNetworkType networkType,
			int inputTemperatureDelay, int inputHumidityDelay,
			int inputInsolationDelay, int inputEnergyDelay,
			int inputWindowSizeFrom, int inputWindowSizeTo,
			int inputWindowYearsDelay, int outputPredictionSteps,
			double normRatio, Date maxTrainDataStart, Date maxTrainDataStop,
			boolean withNormalization, boolean lazySetLoading) {
		super(inputTemperatureDelay, inputHumidityDelay, inputInsolationDelay,
				inputEnergyDelay, inputWindowSizeFrom, inputWindowSizeTo,
				inputWindowYearsDelay, outputPredictionSteps, normRatio,
				maxTrainDataStart, maxTrainDataStop, networkType.getType());
		this.networkType = networkType;
		this.withNormalization = withNormalization;
		this.lazySetLoading = lazySetLoading;
	}

	/**
	 * @return wartość pola networkType
	 */
	public EnumNetworkType getNetworkType() {
		return networkType;
	}

	/**
	 * @param networkType
	 *            jest przypisywany do pola networkType
	 */
	public void setNetworkType(EnumNetworkType networkType) {
		this.networkType = networkType;
	}

	public boolean isWithNormalization() {
		return withNormalization;
	}

	public void setWithNormalization(boolean withNormalization) {
		this.withNormalization = withNormalization;
	}

	public boolean isLazySetLoading() {
		return lazySetLoading;
	}

	public void setLazySetLoading(boolean lazySetLoading) {
		this.lazySetLoading = lazySetLoading;
	}

	public boolean isInputDayType() {
		return inputDayType;
	}

	public void setInputDayType(boolean inputDayType) {
		this.inputDayType = inputDayType;
	}

	public boolean isInputHoliday() {
		return inputHoliday;
	}

	public void setInputHoliday(boolean inputHoliday) {
		this.inputHoliday = inputHoliday;
	}

	public int getInputEMean168Delay() {
		return inputEMean168Delay;
	}

	public void setInputEMean168Delay(int inputEMean168Delay) {
		this.inputEMean168Delay = inputEMean168Delay;
	}

	public int getInputEMean24Delay() {
		return inputEMean24Delay;
	}

	public void setInputEMean24Delay(int inputEMean24Delay) {
		this.inputEMean24Delay = inputEMean24Delay;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("\n" + this.getClass().toString()
				+ "\n");
		sb.append("networkType = " + this.networkType + "\n");
		sb.append("withNormalization = " + this.withNormalization + "\n");
		sb.append("lazySetLoading = " + this.lazySetLoading + "\n");
		sb.append("inputHoliday = " + this.inputHoliday + "\n");
		sb.append("inputDayType = " + this.inputDayType + "\n");
		sb.append("inputEMean24Delay = " + this.inputEMean24Delay + "\n");
		sb.append("inputEMean168Delay = " + this.inputEMean168Delay + "\n");
		sb.append(super.toString());
		return sb.toString();
	}

}
