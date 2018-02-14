package pl.phytia.model.db;

import pl.phytia.model.enums.EnumTableName;

/**
 * Klasa reprezentująca tabelę <code>model</code>.
 * 
 * @author Jarosław Protasiewicz
 */
public class ModelDbVO extends BaseDbVO {

	/* ########### FIELDS ########### */
	/**
	 * Kolumna <code>model_id</code>.
	 */
	private Long modelId;

	/**
	 * Kolumna <code>metamodel_id</code>.
	 */
	private Long metamodelId;

	/**
	 * Kolumna <code>model_type</code>.
	 */
	private String modelType;

	/**
	 * Kolumna <code>model_class</code>.
	 */
	private String modelClass;

	/**
	 * Kolumna <code>conf</code>.
	 */
	private String conf;

	/**
	 * Kolumna <code>network</code>.
	 */
	private String network;

	/* ########### CONSTRUCTORS ########### */
	/**
	 * Konstruktor bezarumentowy.
	 */
	public ModelDbVO() {
		super();
	}

	/**
	 * Konstruktor.
	 * 
	 * @param modelId
	 *            Kolumna <code>model_id</code>.
	 * @param metamodelId
	 *            Kolumna <code>metamodel_id</code>.
	 * @param modelType
	 *            Kolumna <code>model_type</code>.
	 * @param modelClass
	 *            Kolumna <code>model_class</code>.
	 * @param conf
	 *            Kolumna <code>conf</code>.
	 * @param network
	 *            Kolumna <code>network</code>.
	 */
	public ModelDbVO(Long modelId, Long metamodelId, String modelType,
			String modelClass, String conf, String network) {
		super();
		this.modelId = modelId;
		this.metamodelId = metamodelId;
		this.modelType = modelType;
		this.modelClass = modelClass;
		this.conf = conf;
		this.network = network;
	}

	/* ########### METHODS ########### */
	@Override
	public String getTableName() {
		return EnumTableName.model.toString();
	}

	/**
	 * @return Nazwa kolumny <code>model_id</code>.
	 */
	public final String getColumnNameForModelId() {
		return "model_id";
	}

	/**
	 * @return Nazwa kolumna <code>metamodel_id</code>.
	 */
	public final String getColumnNameForMetamodelId() {
		return "metamodel_id";
	}

	/**
	 * @return Nazwa kolumny <code>model_type</code>.
	 */
	public final String getColumnNameForModelType() {
		return "model_type";
	}

	/**
	 * @return Nazwa kolumny <code>model_class</code>.
	 */
	public final String getColumnNameForModelClass() {
		return "model_class";
	}

	/**
	 * @return Nazwa kolumny <code>conf</code>.
	 */
	public final String getColumnNameForConf() {
		return "conf";
	}

	/**
	 * @return Nazwa kolumny <code>network</code>.
	 */
	public final String getColumnNameForNetwork() {
		return "network";
	}

	/* ########### GETTERS AND SETTERS ########### */
	public String getConf() {
		return conf;
	}

	public void setConf(String conf) {
		this.conf = conf;
	}

	public Long getMetamodelId() {
		return metamodelId;
	}

	public void setMetamodelId(Long metamodelId) {
		this.metamodelId = metamodelId;
	}

	public String getModelClass() {
		return modelClass;
	}

	public void setModelClass(String modelClass) {
		this.modelClass = modelClass;
	}

	public Long getModelId() {
		return modelId;
	}

	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}

	public String getModelType() {
		return modelType;
	}

	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((conf == null) ? 0 : conf.hashCode());
		result = PRIME * result
				+ ((metamodelId == null) ? 0 : metamodelId.hashCode());
		result = PRIME * result
				+ ((modelClass == null) ? 0 : modelClass.hashCode());
		result = PRIME * result + ((modelId == null) ? 0 : modelId.hashCode());
		result = PRIME * result
				+ ((modelType == null) ? 0 : modelType.hashCode());
		result = PRIME * result + ((network == null) ? 0 : network.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ModelDbVO other = (ModelDbVO) obj;
		if (conf == null) {
			if (other.conf != null)
				return false;
		} else if (!conf.equals(other.conf))
			return false;
		if (metamodelId == null) {
			if (other.metamodelId != null)
				return false;
		} else if (!metamodelId.equals(other.metamodelId))
			return false;
		if (modelClass == null) {
			if (other.modelClass != null)
				return false;
		} else if (!modelClass.equals(other.modelClass))
			return false;
		if (modelId == null) {
			if (other.modelId != null)
				return false;
		} else if (!modelId.equals(other.modelId))
			return false;
		if (modelType == null) {
			if (other.modelType != null)
				return false;
		} else if (!modelType.equals(other.modelType))
			return false;
		if (network == null) {
			if (other.network != null)
				return false;
		} else if (!network.equals(other.network))
			return false;
		return true;
	}

}
