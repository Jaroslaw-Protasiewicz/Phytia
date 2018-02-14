package pl.phytia.model.sets;

import java.util.Date;

import pl.phytia.utils.StateFixer;

/**
 * Abstrakcyjny wzorzec danych.
 * 
 * @author Jarosław Protasiewicz
 * 
 * @param <S>
 *            Typ obiektu, którego stan jest odczytywany lub zapisywany.
 */
public abstract class PatternVO<S extends PatternVO> extends StateFixer<S>
		implements Normalizable {

	/**
	 * Wzorzec - wektor wejść.
	 */
	private DoubleVector inputs;

	/**
	 * Punkt w czasie jakiemu będzie odpowiadało oczekiwany wyjście podczas
	 * prognozy.
	 */
	private Date outTime;

	/**
	 * Informacja o stanie normalizacji wzorca.
	 */
	private boolean normalized = false;

	/**
	 * Konstruktor bezargumentowy.
	 */
	public PatternVO() {
		super();
	}

	/**
	 * Konstruktor wypłeniajcy wzorzec.
	 * 
	 * @param inputs
	 *            Wektor wejść.
	 */
	public PatternVO(DoubleVector inputs) {
		super();
		this.inputs = inputs;
		this.normalized = false;
	}

	/**
	 * Konstruktor wypełnijacy wzorzec i określający odpowidający mu punkt w
	 * czasie.
	 * 
	 * @param inputs
	 *            Wektor wejść.
	 * @param outTime
	 *            Punkt w czasie jakiemu będzie odpowiadało oczekiwany wyjście
	 *            podczas prognozy.
	 */
	public PatternVO(DoubleVector inputs, Date outTime) {
		super();
		this.inputs = inputs;
		this.outTime = outTime;
	}

	/**
	 * Konstruktor wypełnijacy wszystkie pola klasy
	 * 
	 * @param inputs
	 *            Wektor wejść.
	 * @param outTime
	 *            Punkt w czasie jakiemu będzie odpowiadało oczekiwany wyjście
	 *            podczas prognozy.
	 * @param normalized
	 *            Informacja o stanie normalizacji wzorca.
	 */
	public PatternVO(DoubleVector inputs, Date outTime, boolean normalized) {
		super();
		this.inputs = inputs;
		this.outTime = outTime;
		this.normalized = normalized;
	}

	public boolean isNormalized() {
		return normalized;
	}

	public DoubleVector getInputs() {
		return inputs;
	}

	public void setInputs(DoubleVector inputs) {
		this.inputs = inputs;
	}

	public void setNormalized(boolean normalized) {
		this.normalized = normalized;
	}

	public Date getOutTime() {
		return outTime;
	}

	public void setOutTime(Date outTime) {
		this.outTime = outTime;
	}
}
