package pl.phytia.model.pred;

import java.util.Date;

import pl.phytia.model.db.SerialModelDbVO;
import pl.phytia.prediction.models.Model;

public class SerialModelVO<M extends Model> extends SerialModelDbVO {

	/**
	 * Model zawarty w szeregu.
	 */
	private M model;

	/**
	 * Domyślny konstruktor.
	 */
	public SerialModelVO() {
		super();
	}

	/**
	 * @param model
	 *            Model zawarty w szeregu.
	 * @param serialModelId
	 *            Kolumna serialmodel_id serial NOT NULL,
	 * @param metaModelId
	 *            Kolumna metamodel_id int4 NOT NULL,
	 * @param modelId
	 *            Kolumna model_id int4 NOT NULL,
	 * @param modelDate
	 *            Kolumna model_day date,
	 */
	public SerialModelVO(M model, Long serialModelId, Long metaModelId,
			Long modelId, Date modelDate) {
		super(serialModelId, metaModelId, modelId, modelDate);
		this.model = model;
	}

	/**
	 * @return wartość pola model
	 */
	public M getModel() {
		return model;
	}

	/**
	 * @param model
	 *            jest przypisywany do pola models
	 */
	public void setModels(M model) {
		this.model = model;
	}

}
