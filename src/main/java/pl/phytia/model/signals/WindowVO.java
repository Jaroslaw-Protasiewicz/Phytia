package pl.phytia.model.signals;

import java.util.Date;

/**
 * Klasa - nośnik danych okna pomiarowego.
 * 
 * @author Jarosław Protasiewicz
 */
public class WindowVO {

	/**
	 * Początek okna w jako typ <code>long</code>.
	 */
	private long dateFromLong;

	/**
	 * Początek okna.
	 */
	private Date dateFrom;

	/**
	 * Koniec okna w jako typ <code>long</code>.
	 */
	private long dateToLong;

	/**
	 * Koniec okna.
	 */
	private Date dateTo;

	public WindowVO() {
		super();
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

	public long getDateFromLong() {
		return dateFromLong;
	}

	public void setDateFromLong(long dateFromLong) {
		this.dateFromLong = dateFromLong;
	}

	public long getDateToLong() {
		return dateToLong;
	}

	public void setDateToLong(long dateToLong) {
		this.dateToLong = dateToLong;
	}

}
