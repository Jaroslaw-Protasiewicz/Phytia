package pl.phytia.model.enums;

/**
 * Typy baz danych
 * 
 * @author Jarosław Protasiewicz
 */
public enum EnumDataBase {

	/**
	 * Baza danych postgress
	 */
	PostgreSQL;

	static {
		EnumPersistenceDelegate.installFor(values());
	}
}
