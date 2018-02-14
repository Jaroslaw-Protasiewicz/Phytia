package pl.phytia.model.enums;

import java.io.Serializable;

/**
 * Typy funkcji aktywacji neuronu.
 * 
 * @author Jarosław Protasiewicz
 */
public enum EnumFunctionType implements Serializable {

	/**
	 * Przepuszczająca sygnał bez zmian.
	 */
	Pipe("Pipe", "Przepuszczająca sygnał bez zmian"),

	/**
	 * Liniowaz nasyceniem obustronnym.
	 */
	Linear("Linear", "Liniowa funkcja aktwacji z nasyceniem obustronnym"),

	/**
	 * Sigmoidalna unipolarna.
	 */
	SigmoidalUnipolar("SigmoidalUnipolar", "Sigmoidalna unipolarna"),

	/**
	 * Sigmoidalna bipolarna.
	 */
	SigmoidalBipolar("SigmoidalBipolar", "Sigmoidalna bipolarna"),

	/**
	 * Sigmoidalna unipolarna z modyfikacją Fahlmana.
	 */
	FahlmanUnipolar("FahlmanUnipolar",
			"Sigmoidalna unipolarna z modyfikacją Fahlmana"),

	/**
	 * Funkcja błędu - Zsumowany błąd średniokwadratowy.
	 */
	SSE("SSE", "Zsumowany błąd średniokwadratowy"),

	/**
	 * Funkcja błędu - Średni mooduł błędu procentowego.
	 */
	MAPE("MAPE", "Średni mooduł błędu procentowego"),

	/**
	 * Funkcja róznica
	 */
	DIFF("DIFF", "Różnica"),

	/**
	 * Radialna funkcja Gaussa.
	 */
	GaussianRbf("GaussianRbf", "Radialna funkcja Gaussa"),

	/**
	 * Radialna funkcja sklejana.
	 */
	GluedRbf("GluedRbf", "Radialna funkcja sklejana"),

	/**
	 * Potęgowa funkcja radialna.
	 */
	PoweredRbf("PoweredRbf", "Potęgowa funkcja radialna");

	/**
	 * Konstruktor typu.
	 * 
	 * @param type
	 *            Typ - skrót nazwy.
	 * @param name
	 *            Opisowa nazwa typu.
	 */
	EnumFunctionType(String type, String name) {
		this.type = type;
		this.name = name;
	}

	/**
	 * Typ funkcji aktywacji neuronu.
	 */
	private String type;

	/**
	 * Nazwa funkcji aktywacji neronu.
	 */
	private String name;

	/**
	 * @return wartość pola name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return wartość pola type
	 */
	public String getType() {
		return type;
	}

	static {
		EnumPersistenceDelegate.installFor(values());
	}
}
