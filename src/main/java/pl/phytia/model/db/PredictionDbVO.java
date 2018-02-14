package pl.phytia.model.db;

import java.util.Date;

import pl.phytia.model.enums.EnumTableName;

/**
 * Klasa reprezentujaca tabelę <code>prediction</code>
 * 
 * @author Jarosław Protasiewicz
 */
public class PredictionDbVO extends BaseDbVO {

	/**
	 * Kolumna <code>prediction_id serial NOT NULL</code>.
	 */
	private Long predictionId;

	/**
	 * Kolumna <code>signal_name_id int4</code>.
	 */
	private Long signalNameId;

	/**
	 * Kolumna <code>pred_time timestamp NOT NULL</code>.
	 */
	private Date predTime;

	/**
	 * Kolumna <code>pred_season int4 NOT NULL</code>.
	 */
	private Integer predSeason;

	/**
	 * Kolumna <code>pred_day Date NOT NULL</code>.
	 */
	private Date predDay;

	/**
	 * Kolumna <code>pred_hour int4 NOT NULL</code>.
	 */
	private Integer predHour;

	/**
	 * Kolumna <code>day_ahead int4 NOT NULL</code>.
	 */
	private Integer dayAhead;

	/**
	 * Kolumna <code>pred_value numeric(10,6)</code>.
	 */
	private Double predValue;

	/**
	 * Konstruktor bezparametrowy.
	 */
	public PredictionDbVO() {
		super();
	}

	/**
	 * Konstruktor.
	 * 
	 * @param predictionId
	 *            Kolumna <code>prediction_id serial NOT NULL</code>.
	 * @param signalNameId
	 *            Kolumna <code>signal_name_id int4</code>.
	 * @param predTime
	 *            Kolumna <code>pred_time timestamp NOT NULL</code>.
	 * @param predSeason
	 *            Kolumna <code>pred_season int4 NOT NULL</code>.
	 * @param predDay
	 *            Kolumna <code>pred_day Day NOT NULL</code>.
	 * @param predHour
	 *            Kolumna <code>pred_hour int4 NOT NULL</code>.
	 * @param dayAhead
	 *            Kolumna <code>day_ahead int4 NOT NULL</code>.
	 * @param predValue
	 *            Kolumna <code>pred_value numeric(10,6)</code>.
	 */
	public PredictionDbVO(Long predictionId, Long signalNameId, Date predTime,
			Integer predSeason, Date predDay, Integer predHour,
			Integer dayAhead, Double predValue) {
		super();
		this.predictionId = predictionId;
		this.signalNameId = signalNameId;
		this.predTime = predTime;
		this.predSeason = predSeason;
		this.predDay = predDay;
		this.predHour = predHour;
		this.dayAhead = dayAhead;
		this.predValue = predValue;
	}

	@Override
	public String getTableName() {
		return EnumTableName.prediction.toString();
	}

	/**
	 * @return Nazwa kolumny <code>prediction_id serial NOT NULL</code>.
	 */
	public final String getColumnNameForPredictionId() {
		return "prediction_id";
	}

	/**
	 * @return Nazwa kolumny <code>signal_name_id int4</code>.
	 */
	public final String getColumnNameForSignalNameId() {
		return "signal_name_id";
	}

	/**
	 * @return Nazwa kolumny <code>pred_time timestamp NOT NULL</code>.
	 */
	public final String getColumnNameForPredTime() {
		return "pred_time";
	}

	/**
	 * @return Nazwa kolumny <code>pred_season int4 NOT NULL</code>.
	 */
	public final String getColumnNameForPredSeason() {
		return "pred_season";
	}

	/**
	 * @return Nazwa kolumny <code>pred_day Date NOT NULL</code>.
	 */
	public final String getColumnNameForPredDay() {
		return "pred_day";
	}

	/**
	 * @return Nazwa kolumny <code>pred_hour int4 NOT NULL</code>.
	 */
	public final String getColumnNameForPredHour() {
		return "pred_hour";
	}

	/**
	 * @return Nazwa kolumny <code>day_ahead int4 NOT NULL</code>.
	 */
	public final String getColumnNameForDayAhead() {
		return "day_ahead";
	}

	/**
	 * @return Nazwa kolumny <code>pred_value numeric(10,6)</code>.
	 */
	public final String getColumnNameForPredValue() {
		return "pred_value";
	}

	/**
	 * @return wartość pola predHour
	 */
	public Integer getPredHour() {
		return predHour;
	}

	/**
	 * @param predHour
	 *            jest przypisywany do pola predHour
	 */
	public void setPredHour(Integer predHour) {
		this.predHour = predHour;
	}

	/**
	 * @return wartość pola predictionId
	 */
	public Long getPredictionId() {
		return predictionId;
	}

	/**
	 * @param predictionId
	 *            jest przypisywany do pola predictionId
	 */
	public void setPredictionId(Long predictionId) {
		this.predictionId = predictionId;
	}

	/**
	 * @return wartość pola predSeason
	 */
	public Integer getPredSeason() {
		return predSeason;
	}

	/**
	 * @param predSeason
	 *            jest przypisywany do pola predSeason
	 */
	public void setPredSeason(Integer predSeason) {
		this.predSeason = predSeason;
	}

	/**
	 * @return wartość pola predTime
	 */
	public Date getPredTime() {
		return predTime;
	}

	/**
	 * @param predTime
	 *            jest przypisywany do pola predTime
	 */
	public void setPredTime(Date predTime) {
		this.predTime = predTime;
	}

	/**
	 * @return wartość pola predValue
	 */
	public Double getPredValue() {
		return predValue;
	}

	/**
	 * @param predValue
	 *            jest przypisywany do pola predValue
	 */
	public void setPredValue(Double predValue) {
		this.predValue = predValue;
	}

	/**
	 * @return wartość pola signalNameId
	 */
	public Long getSignalNameId() {
		return signalNameId;
	}

	/**
	 * @param signalNameId
	 *            jest przypisywany do pola signalNameId
	 */
	public void setSignalNameId(Long signalNameId) {
		this.signalNameId = signalNameId;
	}

	/**
	 * @return wartość pola predDay
	 */
	public Date getPredDay() {
		return predDay;
	}

	/**
	 * @param predDay
	 *            jest przypisywany do pola predDay
	 */
	public void setPredDay(Date predDay) {
		this.predDay = predDay;
	}

	/**
	 * @return wartość pola dayAhead
	 */
	public Integer getDayAhead() {
		return dayAhead;
	}

	/**
	 * @param dayAhead
	 *            jest przypisywany do pola dayAhead
	 */
	public void setDayAhead(Integer dayAhead) {
		this.dayAhead = dayAhead;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(this.getClass().getCanonicalName());
		sb.append(getColumnNameForDayAhead() + " = "
				+ (getDayAhead() != null ? getDayAhead() : null) + "\n");
		sb.append(getColumnNameForPredDay() + " = "
				+ (getPredDay() != null ? getPredDay() : null) + "\n");
		sb.append(getColumnNameForPredHour() + " = "
				+ (getPredHour() != null ? getPredHour() : null) + "\n");
		sb
				.append(getColumnNameForPredictionId()
						+ " = "
						+ (getPredictionId() != null ? getPredictionId() : null)
						+ "\n");
		sb.append(getColumnNameForPredSeason() + " = "
				+ (getPredSeason() != null ? getPredSeason() : null) + "\n");
		sb.append(getColumnNameForPredTime() + " = "
				+ (getPredTime() != null ? getPredTime() : null) + "\n");
		sb.append(getColumnNameForPredValue() + " = "
				+ (getPredValue() != null ? getPredValue() : null) + "\n");
		sb
				.append(getColumnNameForSignalNameId()
						+ " = "
						+ (getSignalNameId() != null ? getSignalNameId() : null)
						+ "\n");
		return sb.toString();
	}
}
