package pl.phytia.model.db;

import pl.phytia.model.enums.EnumTableName;

/**
 * Klasa reprezentująca tabelę <code>METAMODEL</code>.
 * 
 * @author Jarosław Protasiewicz
 */
public class MetaModelDbVO extends BaseDbVO {

	/* ########### FIELDS ########### */
	/**
	 * Kolumna <code>METAMODEL_ID</code>.
	 */
	private Long idMetamodelu;

	/**
	 * Kolumna <code>TYPE</code>.
	 */
	private String metaModelType;

	/**
	 * Kolumna <code>FORECAST_TYPE</code>.
	 */
	private String forecastType;

	/**
	 * Kolumna <code>INPUT_SIGNAL</code>.
	 */
	private Long inputSignal;

	/**
	 * Kolumna <code>OUTPUT_SIGNAL</code>.
	 */
	private Long outputSignal;

	/**
	 * Kolumna <code>CONF</code>.
	 */
	private String conf;

	/* ########### CONSTRUCTORS ########### */
	/**
	 * Konstruktor bezargumentowy.
	 */
	public MetaModelDbVO() {
		super();
	}

	/**
	 * Konstruktor.
	 * 
	 * @param idMetamodelu
	 *            Kolumna <code>METAMODEL_ID</code>.
	 * @param metaModelType
	 *            Kolumna <code>TYPE</code>.
	 * @param forecastType
	 *            Kolumna <code>FORECAST_TYPE</code>.
	 * @param inputSignal
	 *            Kolumna <code>INPUT_SIGNAL</code>.
	 * @param outputSignal
	 *            Kolumna <code>OUTPUT_SIGNAL</code>
	 * @param conf
	 *            Kolumna <code>CONF</code>.
	 */
	public MetaModelDbVO(Long idMetamodelu, String metaModelType,
			String forecastType, Long inputSignal, Long outputSignal,
			String conf) {
		super();
		this.idMetamodelu = idMetamodelu;
		this.metaModelType = metaModelType;
		this.forecastType = forecastType;
		this.inputSignal = inputSignal;
		this.outputSignal = outputSignal;
		this.conf = conf;
	}

	/* ########### METHODS ########### */
	@Override
	public String getTableName() {
		return EnumTableName.metamodel.toString();
	}

	/**
	 * @return Nazwa kolumny <code>metamodel_id</code>.
	 */
	public final String getColumnNameForIdMetamodelu() {
		return "metamodel_id";
	}

	/**
	 * @return Nazwa kolumny <code>metamodel_type</code>.
	 */
	public final String getColumnNameForMetaModelType() {
		return "metamodel_type";
	}

	/**
	 * @return Nazwa kolumny <code>forecast_type</code>.
	 */
	public final String getColumnNameForForecastType() {
		return "forecast_type";
	}

	/**
	 * @return Nazwa kolumny <code>input_signal</code>.
	 */
	public final String getColumnNameForInputSignal() {
		return "input_signal";
	}

	/**
	 * @return Nazwa kolumny <code>output_signal</code>.
	 */
	public final String getColumnNameForOutputSignal() {
		return "output_signal";
	}

	/**
	 * @return Nazwa kolumny <code>conf</code>.
	 */
	public final String getColumnNameForConf() {
		return "conf";
	}

	/* ########### GETTERS AND SETTERS ########### */
	/**
	 * @return wartość pola conf
	 */
	public String getConf() {
		return conf;
	}

	/**
	 * @param conf
	 *            jest przypisywany do pola conf
	 */
	public void setConf(String conf) {
		this.conf = conf;
	}

	/**
	 * @return wartość pola forecastType
	 */
	public String getForecastType() {
		return forecastType;
	}

	/**
	 * @param forecastType
	 *            jest przypisywany do pola forecastType
	 */
	public void setForcastType(String forecastType) {
		this.forecastType = forecastType;
	}

	/**
	 * @return wartość pola idMetamodelu
	 */
	public Long getIdMetamodelu() {
		return idMetamodelu;
	}

	/**
	 * @param idMetamodelu
	 *            jest przypisywany do pola idMetamodelu
	 */
	public void setIdMetamodelu(Long idMetamodelu) {
		this.idMetamodelu = idMetamodelu;
	}

	/**
	 * @return wartość pola inputSignal
	 */
	public Long getInputSignal() {
		return inputSignal;
	}

	/**
	 * @param inputSignal
	 *            jest przypisywany do pola inputSignal
	 */
	public void setInputSignal(Long inputSignal) {
		this.inputSignal = inputSignal;
	}

	/**
	 * @return wartość pola outputSignal
	 */
	public Long getOutputSignal() {
		return outputSignal;
	}

	/**
	 * @param outputSignal
	 *            jest przypisywany do pola outputSignal
	 */
	public void setOutputSignal(Long outputSignal) {
		this.outputSignal = outputSignal;
	}

	/**
	 * @return wartość pola metaModelType
	 */
	public String getMetaModelType() {
		return metaModelType;
	}

	/**
	 * @param metaModelType
	 *            jest przypisywany do pola metaModelType
	 */
	public void setMetaModelType(String metaModelType) {
		this.metaModelType = metaModelType;
	}
}
