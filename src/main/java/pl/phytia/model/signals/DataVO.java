package pl.phytia.model.signals;

import java.util.Date;

/**
 * Klasa - nośnik danych pomiarowych.
 * 
 * @author Jarosław Protasiewicz
 */
public class DataVO {

	/**
	 * Punkt czasu, którego dotyczą dane.
	 */
	private Date time;

	/**
	 * Dzień, którego dane.
	 */
	private Date data;

	/**
	 * Godzina, którj dotyczą dane.
	 */
	private short hour;

	/**
	 * Typ czasu: lato / zima.
	 */
	private short timeType;

	/**
	 * Dana: energia.
	 */
	private double energy;

	/**
	 * Dana: nasłonecznienie.
	 */
	private double insolation;

	/**
	 * Dana: wilogtoność.
	 */
	private double humidity;

	/**
	 * Dana: temperatura.
	 */
	private double temperature;

	/**
	 * Dana: temperatura maksymalna dnia.
	 */
	private double tempMax;

	/**
	 * Dana: temperatura minimalna dnia.
	 */
	private double tempMin;

	/**
	 * Klasa dnia roboczego.
	 */
	private String dayType;

	/**
	 * Klasa dnia świątecznego.
	 */
	private String holiday;

	private double eMean24;

	private double eMean168;

	public Date getTime() {
		return this.time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Date getData() {
		return this.data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public short getHour() {
		return this.hour;
	}

	public void setHour(short hour) {
		this.hour = hour;
	}

	public short getTimeType() {
		return this.timeType;
	}

	public void setTimeType(short timeType) {
		this.timeType = timeType;
	}

	public double getEnergy() {
		return this.energy;
	}

	public void setEnergy(double energy) {
		this.energy = energy;
	}

	public double getInsolation() {
		return this.insolation;
	}

	public void setInsolation(double insolation) {
		this.insolation = insolation;
	}

	public double getHumidity() {
		return this.humidity;
	}

	public void setHumidity(double humidity) {
		this.humidity = humidity;
	}

	public double getTemperature() {
		return this.temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public String getDayType() {
		return this.dayType;
	}

	public void setDayType(String dayType) {
		this.dayType = dayType;
	}

	public String getHoliday() {
		return this.holiday;
	}

	public void setHoliday(String holiday) {
		this.holiday = holiday;
	}

	public double getTempMax() {
		return tempMax;
	}

	public void setTempMax(double tempMax) {
		this.tempMax = tempMax;
	}

	public double getTempMin() {
		return tempMin;
	}

	public void setTempMin(double tempMin) {
		this.tempMin = tempMin;
	}

	public double getEMean168() {
		return eMean168;
	}

	public void setEMean168(double mean168) {
		eMean168 = mean168;
	}

	public double getEMean24() {
		return eMean24;
	}

	public void setEMean24(double mean24) {
		eMean24 = mean24;
	}

	/**
	 * Implementacja metody toString()
	 */
	public String toString() {
		StringBuilder s = new StringBuilder("time = ");
		s.append((this.time != null) ? this.time.toString() : "");
		s.append("\ndata = ");
		s.append((this.data != null) ? this.data.toString() : "");
		s.append("\nhourt = " + this.hour);
		s.append("\ntimeType = " + this.timeType);
		s.append("\nenergy = " + this.energy);
		s.append("\ninsolation = " + this.insolation);
		s.append("\nhumidity = " + this.humidity);
		s.append("\ntemperature = " + this.temperature);
		s.append("\ntempMax = " + this.tempMax);
		s.append("\ntempMin = " + this.tempMin);
		s.append("\ndayType = " + this.dayType);
		s.append("\nholiday = " + this.holiday);
		s.append("\neMean24 = " + this.eMean24);
		s.append("\neMean168 = " + this.eMean168);
		return s.toString();
	}
}
