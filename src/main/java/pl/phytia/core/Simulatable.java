package pl.phytia.core;

import pl.phytia.model.sets.DoubleVector;

public interface Simulatable {

	/**
	 * Symulacja obiektu neuronowego : neuronu / warstwy / sieci
	 */
	void simulation(DoubleVector inputs);

}
