package pl.phytia.model.db;

import pl.phytia.model.enums.EnumTableName;

/**
 * Klasa reprezentujaca tabelę <code>signal_names</code>
 * 
 * @author Jarosław Protasiewicz
 */
public class SignalNameVO extends BaseDbVO {

	/**
	 * Kolumna signal_name_id serial NOT NULL.
	 */
	private Long signalNameId;

	/**
	 * Kolumna name varchar(100) NOT NULL.
	 */
	private String name;

	/**
	 * Konstruktor bezparametrowy.
	 */
	public SignalNameVO() {
		super();
	}

	/**
	 * Konstruktor.
	 * 
	 * @param signalNameId
	 *            Kolumna signal_name_id serial NOT NULL.
	 * @param name
	 *            Kolumna name varchar(100) NOT NULL.
	 */
	public SignalNameVO(Long signalNameId, String name) {
		super();
		this.signalNameId = signalNameId;
		this.name = name;
	}

	@Override
	public String getTableName() {
		return EnumTableName.signal_names.toString();
	}

	/**
	 * @return Nazwa kolumny <code>signal_name_id</code>.
	 */
	public final String getColumnNameForSignalNameId() {
		return "signal_name_id";
	}

	/**
	 * @return Nazwa kolumny <code>name</code>.
	 */
	public final String getColumnNameForName() {
		return "name";
	}

	/**
	 * @return wartość pola name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            jest przypisywany do pola name
	 */
	public void setName(String name) {
		this.name = name;
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
}
