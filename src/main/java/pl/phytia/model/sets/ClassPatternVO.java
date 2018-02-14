package pl.phytia.model.sets;

import java.util.Date;

import pl.phytia.utils.Normalizer;

/**
 * Wzorzec dla uczenia nienadzorowanego.
 * 
 * @author Jarosław Protasiewicz
 */
public class ClassPatternVO extends PatternVO<ClassPatternVO> {

	/**
	 * Nazwa klasy, do której należy wzorzec.
	 */
	private String className;

	/**
	 * Idnetyfikator wektora;
	 */
	private long id;

	/**
	 * Konstruktor bezargumentowy.
	 */
	public ClassPatternVO() {
		super();
		setNormalized(false);
	}

	/**
	 * Konstruktor wypełniający pola klasy.
	 * 
	 * @param inputs
	 *            Wektor wejść.
	 * @param className
	 *            Nazwa klasy.
	 * @param id
	 *            Idnetyfikator wektora.
	 * @param outTime
	 *            Punkt w czasie jakiemu będzie odpowiadało oczekiwany wyjście
	 *            podczas prognozy.
	 */
	public ClassPatternVO(DoubleVector inputs, String className, long id,
			Date outTime) {
		super(inputs, outTime);
		this.className = className;
		this.id = id;
		setNormalized(false);
	}

	public void denormalize(Normalizer normalizer) {
		if (isNormalized()) {
			this.getInputs().denormalize(normalizer);
			setNormalized(false);
		}

	}

	public void normalize(Normalizer normalizer) {
		if (!isNormalized()) {
			this.getInputs().normalize(normalizer);
			setNormalized(true);
		}
	}

	public double min() {
		return getInputs().min();
	}

	public double max() {
		return getInputs().max();
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
