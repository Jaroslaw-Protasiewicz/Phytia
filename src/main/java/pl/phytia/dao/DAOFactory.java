package pl.phytia.dao;

import pl.phytia.dao.pg.PostgreSqlDAOFactory;
import pl.phytia.model.enums.EnumDataBase;

/**
 * Fabryka Data Daccess Objects
 * 
 * @author Jarosław Protasiewicz
 */
public abstract class DAOFactory {

	/**
	 * @return Zwraca SignalDAO
	 */
	public abstract SignalDAO getSignalDAO();

	/**
	 * @return Zwraca MetaModelDAO
	 */
	public abstract MetaModelDAO getMetaModelDAO();

	/**
	 * @return Zwraca ModelDAO
	 */
	public abstract ModelDAO getModelDAO();

	/**
	 * @return Zwraca SerialModelDAO.
	 */
	public abstract SerialModelDAO getSerialModelDAO();

	/**
	 * @return Zwraca PredictionDAO.
	 */
	public abstract PredictionDAO getPredictionDAO();

	/**
	 * @param dataBaseType
	 *            Typ bazy danych.
	 * @return Fabryka DAO dla danej bazy danych.
	 */
	public static DAOFactory getDAOFactory(EnumDataBase dataBaseType) {
		switch (dataBaseType) {
		case PostgreSQL: {
			return new PostgreSqlDAOFactory();
		}
		default: {
			return null;
		}
		}
	}
}
