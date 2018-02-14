package pl.phytia.model.signals;

import java.util.Date;

import pl.phytia.model.sets.DoubleVector;

/**
 * Klasa - nośnik informacji, gerowanych przez statystyki.
 * 
 * @author Jarosław Protasiewicz
 */
public class StatVO {

	/**
	 * Dzień, którego dotyczy statystyka.
	 */
	private Date statDay;

	/**
	 * Miesiąc, dla którego dotyczy statystyka
	 */
	private int month;

	/**
	 * Rok którego dotyczy statystyka
	 */
	private int year;

	/**
	 * Błąd MAPE w danym dniu / miesiącu / roku.
	 */
	private double mape;

	/**
	 * Wektor błędów APE dla każdej godziny dnia.
	 */
	private DoubleVector ape;

	/**
	 * Wektor błędów PE dla każdej godziny dnia.
	 */
	private DoubleVector pe;

	/**
	 * Maksymalny błąd ape dla danego dnia / miesiąca / roku
	 */
	private double maxApe;

	public StatVO() {
		super();
	}

	public DoubleVector getApe() {
		return ape;
	}

	public void setApe(DoubleVector ape) {
		this.ape = ape;
	}

	public double getMape() {
		return mape;
	}

	public void setMape(double mape) {
		this.mape = mape;
	}

	public Date getStatDay() {
		return statDay;
	}

	public void setStatDay(Date statDay) {
		this.statDay = statDay;
	}

	public double getMaxApe() {
		return maxApe;
	}

	public void setMaxApe(double maxApe) {
		this.maxApe = maxApe;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public DoubleVector getPe() {
		return pe;
	}

	public void setPe(DoubleVector pe) {
		this.pe = pe;
	}

}
