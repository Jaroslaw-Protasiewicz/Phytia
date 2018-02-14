package pl.phytia.model.enums;

/**
 * Stany modelu.
 * 
 * @author Jarosław Protasiewicz
 */
public enum EnumModelState {

	/**
	 * Stan modelowania.
	 */
	MODELING,

	/**
	 * Stan prognozowania.
	 */
	PREDICTION;

	static {
		EnumPersistenceDelegate.installFor(values());
	}
}
