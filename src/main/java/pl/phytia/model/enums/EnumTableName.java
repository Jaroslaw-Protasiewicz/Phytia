package pl.phytia.model.enums;

/**
 * Nazwy tabel bazy danych.
 * 
 * @author Jarosław Protasiewicz
 */
public enum EnumTableName {

	/**
	 * Definicja metamodelu.
	 */
	metamodel,
	/**
	 * Definicja modelu.
	 */
	model,
	/**
	 * Połączenie szeregowe modeli.
	 */
	serialmodel,
	/**
	 * Definicje sygnałów.
	 */
	signal_names,
	/**
	 * Wartości sygnałów wejściowych.
	 */
	signals,
	/**
	 * Wartości prognoz.
	 */
	prediction;

	static {
		EnumPersistenceDelegate.installFor(values());
	}
}
