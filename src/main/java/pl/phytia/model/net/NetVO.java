package pl.phytia.model.net;

import java.util.Date;

public class NetVO {

	/**
	 * Identyfikator NET.ID
	 */
	private long id;

	/**
	 * Klasa dnia na który przygotowno model
	 */
	private String dClass;

	/**
	 * Dzień na który przygotowano model
	 */
	private Date dDay;

	/**
	 * Wagi sieci a własciwie cała sieć.
	 */
	private String weights;

	public NetVO() {
	}

	public NetVO(long id, String dClass, Date dDay, String weights) {
		super();
		this.id = id;
		this.dClass = dClass;
		this.dDay = dDay;
		this.weights = weights;
	}

	public String getDClass() {
		return dClass;
	}

	public void setDClass(String class1) {
		dClass = class1;
	}

	public Date getDDay() {
		return dDay;
	}

	public void setDDay(Date day) {
		dDay = day;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getWeights() {
		return weights;
	}

	public void setWeights(String weights) {
		this.weights = weights;
	}
}
