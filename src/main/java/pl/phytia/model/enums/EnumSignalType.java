package pl.phytia.model.enums;

/**
 * Rodzaje danych.
 * 
 * @author Jarosław Protasiewicz
 */
public enum EnumSignalType {

	/**
	 * Dane pomiarowe - zródłowe do modelowania - prognozowania.
	 */
	MODELLING_DATA,

	/**
	 * Znormalizowane dane pomiarowe - zródłowe do modelowania - prognozowania.
	 */
	MODELLING_DATA_NORMALIZED,

	/**
	 * Prognoza.
	 */
	FORECAST;

	static {
		EnumPersistenceDelegate.installFor(values());
	}
}
