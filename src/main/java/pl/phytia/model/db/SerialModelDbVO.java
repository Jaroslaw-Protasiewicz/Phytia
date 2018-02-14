package pl.phytia.model.db;

import java.util.Date;

import pl.phytia.model.enums.EnumTableName;

/**
 * Klasa reprezentująca tabelę <code>serialmodel</code>.
 * 
 * @author Jarosław Protasiewicz
 */
public class SerialModelDbVO extends BaseDbVO {

	/* ########### FIELDS ########### */
	/**
	 * Kolumna <code>serialmodel_id serial NOT NULL,</code>.
	 */
	private Long serialModelId;

	/**
	 * Kolumna <code>metamodel_id int4 NOT NULL,</code>.
	 */
	private Long metaModelId;

	/**
	 * Kolumna <code>model_id int4 NOT NULL,</code>.
	 */
	private Long modelId;

	/**
	 * Kolumna <code>model_day date,</code>.
	 */
	private Date modelDate;

	/* ########### CONSTRUCTORS ########### */
	/**
	 * Konstruktor bezarumentowy.
	 */
	public SerialModelDbVO() {
		super();
	}

	/**
	 * Konstruktor.
	 * 
	 * @param serialModelId
	 *            Kolumna serialmodel_id serial NOT NULL,
	 * @param metaModelId
	 *            Kolumna metamodel_id int4 NOT NULL,
	 * @param modelId
	 *            Kolumna model_id int4 NOT NULL,
	 * @param modelDate
	 *            Kolumna model_day date,
	 */
	public SerialModelDbVO(Long serialModelId, Long metaModelId, Long modelId,
			Date modelDate) {
		super();
		this.serialModelId = serialModelId;
		this.metaModelId = metaModelId;
		this.modelId = modelId;
		this.modelDate = modelDate;
	}

	/* ########### METHODS ########### */
	@Override
	public String getTableName() {
		return EnumTableName.serialmodel.toString();
	}

	/**
	 * @return Nazwa kolumny <code>serialmodel_id</code>.
	 */
	public final String getColumnNameForSerialModelId() {
		return "serialmodel_id";
	}

	/**
	 * @return Nazwa kolumny <code>metamodel_id</code>.
	 */
	public final String getColumnNameForMetaModelId() {
		return "metamodel_id";
	}

	/**
	 * @return Nazwa kolumny <code>model_id</code>.
	 */
	public final String getColumnNameForModelId() {
		return "model_id";
	}

	/**
	 * @return Nazwa kolumny <code>model_day</code>.
	 */
	public final String getColumnNameForModelDate() {
		return "model_day";
	}

	/* ########### GETTERS AND SETTERS ########### */
	/**
	 * @return wartość pola metaModelId
	 */
	public Long getMetaModelId() {
		return metaModelId;
	}

	/**
	 * @param metaModelId
	 *            jest przypisywany do pola metaModelId
	 */
	public void setMetaModelId(Long metaModelId) {
		this.metaModelId = metaModelId;
	}

	/**
	 * @return wartość pola modelDate
	 */
	public Date getModelDate() {
		return modelDate;
	}

	/**
	 * @param modelDate
	 *            jest przypisywany do pola modelDate
	 */
	public void setModelDate(Date modelDate) {
		this.modelDate = modelDate;
	}

	/**
	 * @return wartość pola modelId
	 */
	public Long getModelId() {
		return modelId;
	}

	/**
	 * @param modelId
	 *            jest przypisywany do pola modelId
	 */
	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}

	/**
	 * @return wartość pola serialModelId
	 */
	public Long getSerialModelId() {
		return serialModelId;
	}

	/**
	 * @param serialModelId
	 *            jest przypisywany do pola serialModelId
	 */
	public void setSerialModelId(Long serialModelId) {
		this.serialModelId = serialModelId;
	}
}
