package pl.phytia.model.sets;

import java.util.ArrayList;
import java.util.Collection;

import pl.phytia.utils.Normalizer;

/**
 * Wektor wartości typu <code>Double</code>
 * 
 * @author Jarosław Protasiewicz
 */
public class DoubleVector extends ArrayList<Double> implements Normalizable {

	private static final long serialVersionUID = 4533172467108742630L;

	/**
	 * Informacja o stanie normalizacji wektora.
	 */
	private boolean normalized = false;

	/**
	 * Domyślny konstruktor bezparametrowy.
	 */
	public DoubleVector() {
		super();
		this.normalized = false;
	}

	/**
	 * Konstruktor budujący wektor o określonej długości.
	 * 
	 * @param i
	 *            Długość wektora.
	 */
	public DoubleVector(int i) {
		super(i);
		this.normalized = false;
	}

	/**
	 * Konstruktor z inicjalizacja wektora.
	 * 
	 * @param i
	 *            Długość wektora.
	 * @param val
	 *            Wartość początkowa.
	 */
	public DoubleVector(int i, double val) {
		super(i);
		for (int j = 0; j < i; ++j) {
			this.add(new Double(val));
		}
		this.normalized = false;
	}

	/**
	 * Konstruktor budujący wektor na podstawie kolekcji
	 * 
	 * @param c
	 *            kolekcja
	 */
	public DoubleVector(Collection<? extends Double> c) {
		super(c);
	}

	@Override
	public DoubleVector clone() {
		return (DoubleVector) super.clone();
	}

	/**
	 * Metoda pozwalająca obliczyć średnią arytmetyczną dla wektora
	 * 
	 * @return średnia arytmetyczna
	 */
	public double mean() {
		double ret = 0;
		for (int i = 0; i < this.size(); i++)
			ret += this.get(i);
		return ret / this.size();
	}

	/**
	 * Metoda pozwalająca obliczyć wariancję dla wektora
	 * 
	 * @return wariancja
	 */
	public double variance() {
		double ret = 0, mean = this.mean();
		for (int i = 0; i < this.size(); i++)
			ret += Math.pow(this.get(i) - mean, 2.0);
		return (ret / (this.size() - 1));
	}

	public double standartDeviation() {
		double ret = 0, mean = this.mean();
		for (int i = 0; i < this.size(); i++)
			ret += Math.pow(this.get(i) - mean, 2.0);
		return Math.sqrt(ret / (this.size() - 1));
	}

	public double min() {
		double min = Double.MAX_VALUE;
		for (Double d : this) {
			if (d.doubleValue() < min) {
				min = d.doubleValue();
			}
		}
		return min;
	}

	public double max() {
		double max = Double.MIN_VALUE;
		for (Double d : this) {
			if (d.doubleValue() > max) {
				max = d.doubleValue();
			}
		}
		return max;
	}

	/**
	 * @return Błąd mape.
	 */
	public double mape() {
		double mape = 0;
		for (Double d : this) {
			mape += d.doubleValue();
		}
		return mape / this.size();
	}

	public boolean isNormalized() {
		return normalized;
	}

	public String toString() {
		StringBuilder out = new StringBuilder("Wektor o rozmiarze: "
				+ this.size() + "\n");
		for (int i = 0; i < this.size(); ++i) {
			out.append("indx = " + i + " | wartość = " + this.get(i) + "\n");
		}
		out.append("Normalized = " + this.normalized + "\n");
		return out.toString();
	}

	public void denormalize(Normalizer normalizer) {
		if (normalized) {
			for (int i = 0; i < this.size(); ++i) {
				this.set(i, normalizer.denormalize(this.get(i)));
			}
			normalized = false;
		}
	}

	public void normalize(Normalizer normalizer) {
		if (!normalized) {
			for (int i = 0; i < this.size(); ++i) {
				this.set(i, normalizer.normalize(this.get(i)));
			}
			normalized = true;
		}
	}

	public void setNormalized(boolean normalized) {
		this.normalized = normalized;
	}

}
