package pl.phytia.model.db;

/**
 * Abstrakcyjny nośnik danych bazo-danowych.
 * 
 * @author Jarosław Protasiewicz
 */
public abstract class BaseDbVO extends BaseVO {

	/**
	 * @return Nazwa tabeli w bazie danych, reprezentowanej przez klasę.
	 */
	public abstract String getTableName();

}
