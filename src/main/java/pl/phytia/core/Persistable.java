package pl.phytia.core;

/**
 * Interfejs dla klas, dla których stan ich obiektów jest zapisywalny w
 * zewtnęrznym zbiorze i odtwarzalny z niego.
 * 
 * @author Jarosław Protasiewicz
 */
public interface Persistable {
	/**
	 * Odczyt stanu obiektu ze zbioru zewnętrzengo <code>state<code>.
	 * @param state 		Ciąg znaków zawierający stan obiektu
	 */
	void loadState(String state);

	/**
	 * Zapis stanu obiektu do zbioru zewnętrznego <code>state<code>. 
	 * @return				Stan obiektu w postaci ciągu znaków.
	 */
	String storeState();
}
