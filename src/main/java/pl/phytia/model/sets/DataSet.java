package pl.phytia.model.sets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import pl.phytia.model.enums.EnumIterationType;
import pl.phytia.utils.MathUtil;
import pl.phytia.utils.Normalizer;
import pl.phytia.utils.StateFixer;

/**
 * Abstrakcyjny zbiór danych.
 * 
 * @author Jarosław Protasiewicz
 * 
 * @param <D>
 *            Typ obiektu, którego stan jest odczytywany lub zapisywany.
 * @param
 *            <P>
 *            Typ wzorca - elementu zbioru.
 */
public abstract class DataSet<D extends DataSet, P extends PatternVO> extends
		StateFixer<D> implements Normalizable {

	/**
	 * Zbiór danych.
	 */
	private List<P> set;

	/**
	 * Typ iteracji.
	 */
	private EnumIterationType enumIterationType;

	/**
	 * Informacja o stanie normalizacji zbioru.
	 */
	private boolean normalized = false;

	/**
	 * Konstruktor.
	 */
	public DataSet() {
		super();
	}

	/**
	 * @return Zbiór danych.
	 */
	public List<P> getSet() {
		return this.set;
	}

	/**
	 * Modyfikacja zawartości pola.
	 * 
	 * @param set
	 *            Dowolny zbiór danych.
	 * @param isUpdate
	 *            <li><code>true</code></li> - dodaje <code>trainSet</code>
	 *            do zbioru;
	 *            <li><code>false</code></li> - wstawia <code>set</code>
	 *            jako nowy zbiór.
	 */
	public void setSet(List<P> set, boolean isUpdate) {
		if (isUpdate) {
			this.set.addAll(set);
		} else {
			this.set = set;
		}
	}

	/**
	 * Iterator sekwencyjny.
	 */
	private class DataSetSerialIterator implements Iterator<P> {

		/**
		 * Bieżący indeks pary uczącej.
		 */
		protected int currIndx;

		/**
		 * Wewnętrzna kopia danych.
		 */
		private List<P> patterns;

		/**
		 * Konstruktor iteratora.
		 * 
		 * @param patterns
		 *            Zbiór danych do iteracji.
		 */
		public DataSetSerialIterator(List<P> patterns) {
			this.currIndx = 0;
			/*
			 * Klonowanie zbioru danych.
			 */
			this.patterns = new ArrayList<P>(patterns.size());
			for (P e : patterns)
				this.patterns.add(e);
		}

		public boolean hasNext() {
			return currIndx < patterns.size();
		}

		public P next() {
			if (currIndx == patterns.size()) {
				throw new NoSuchElementException();
			}
			return patterns.get(currIndx++);
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * Iterator losowy.
	 */
	private class DataSetRandomIterator implements Iterator<P> {
		/**
		 * Wewnętrzna kopia danych.
		 */
		private List<P> patterns;

		/**
		 * Konstruktor iteratora.
		 * 
		 * @param patterns
		 *            Zbiór danych do iteracji.
		 */
		public DataSetRandomIterator(List<P> patterns) {
			/*
			 * Klonowanie zbioru danych.
			 */
			this.patterns = new ArrayList<P>(patterns.size());
			for (P e : patterns)
				this.patterns.add(e);
		}

		public boolean hasNext() {
			return !patterns.isEmpty();
		}

		public P next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			int index = MathUtil.getRandomInt(patterns.size() - 1);
			P pattern = patterns.get(index);
			patterns.remove(index);
			return pattern;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	/**
	 * @return Iterator dla zbioru <code>set</code>.
	 */
	public Iterator<P> iterator() {
		Iterator<P> i = null;
		if (getEnumIterationType() == null) {
			setEnumIterationType(EnumIterationType.RANDOM);
		}
		switch (getEnumIterationType()) {
		case SERIAL: {
			i = new DataSetSerialIterator(this.set);
			break;
		}
		case RANDOM: {
			i = new DataSetRandomIterator(this.set);
			break;
		}
		default: {
			i = new DataSetRandomIterator(this.set);
			break;
		}
		}
		return i;
	}

	public double min() {
		double min = Double.MAX_VALUE;
		for (P p : getSet()) {
			double minPattern = p.min();
			if (minPattern < min) {
				min = minPattern;
			}
		}
		return min;
	}

	public double max() {
		double max = Double.MIN_VALUE;
		for (P p : this.set) {
			double maxPattern = p.max();
			if (maxPattern > max) {
				max = maxPattern;
			}
		}
		return max;
	}

	public void denormalize(Normalizer normalizer) {
		if (isNormalized()) {
			for (int i = 0; i < getSet().size(); ++i) {
				getSet().get(i).denormalize(normalizer);
			}
			setNormalized(false);
		}
	}

	public void normalize(Normalizer normalizer) {
		if (!isNormalized()) {
			for (int i = 0; i < getSet().size(); ++i) {
				getSet().get(i).normalize(normalizer);
			}
			setNormalized(true);
		}
	}

	public boolean isNormalized() {
		return this.normalized;
	}

	public void setNormalized(boolean normalized) {
		this.normalized = normalized;
	}

	public EnumIterationType getEnumIterationType() {
		return enumIterationType;
	}

	public void setEnumIterationType(EnumIterationType enumIterationType) {
		this.enumIterationType = enumIterationType;
	}

}
