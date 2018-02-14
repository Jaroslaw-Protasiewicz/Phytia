package pl.phytia.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import pl.phytia.model.sets.PatternPairVO;
import pl.phytia.model.signals.DataVO;
import pl.phytia.model.signals.WindowVO;
import pl.phytia.model.db.SignalNameVO;

/**
 * Intefejs obiektów dostepu do bazy danych obsługujących sygnały przechowywane
 * w tabelach: <code>signals</code> i <code>signal_names</code>.
 * 
 * @author Jarosław Protasiewicz
 */
public interface SignalDAO extends BaseDAO {

	/**
	 * Pobiera sumę energii.
	 * 
	 * @param dbCon
	 *            Połączenie z bazą danych.
	 * @param signalName
	 *            Nazwa sygnału.
	 * @param fromTime
	 *            Od czasu
	 * @param toTime
	 *            Do czasu
	 * @return Suma energii
	 * @throws SQLException
	 *             Wyjątek zgłaszany przez bazę danych.
	 */
	public abstract double getSumOfEnergy(Connection dbCon, String signalName,
			Date fromTime, Date toTime) throws SQLException;

	/**
	 * Pobiera zbiór treningowy dla określonej godziny dnia.
	 * 
	 * @param dbCon
	 *            Połączenie z bazą danych.
	 * @param signalName
	 *            Nazwa sygnału.
	 * @param timePoint
	 *            Godzina, dla której pobierane są dane.
	 * @param eLag
	 *            Opóźnienie - regresja szeregu energii.
	 * @param tLag
	 *            Opóźnienie - regresja szeregu temepratury.
	 * @param hLag
	 *            Opóźnienie - regresja szeregu wilgotności.
	 * @param iLag
	 *            Opóźnienie - regresja szeregu nasłonecznienia.
	 * @return Zbiór danych treningowych wygenerowany dla godziny
	 *         <code>timePoint</code>
	 * @throws SQLException
	 *             Wyjątek zgłaszany przez bazę danych.
	 */
	public abstract PatternPairVO getSetForHour(Connection dbCon,
												String signalName, Date timePoint, int eLag, int tLag, int hLag,
												int iLag) throws SQLException;

	/**
	 * Pobiera zbiór treningowy dla określonej godziny dnia.
	 * 
	 * @param dbCon
	 *            Połączenie z bazą danych.
	 * @param signalName
	 *            Nazwa sygnału.
	 * @param timePoint
	 *            Godzina, dla której pobierane są dane.
	 * @param eLag
	 *            Opóźnienie - regresja szeregu energii.
	 * @param tLag
	 *            Opóźnienie - regresja szeregu temepratury.
	 * @param hLag
	 *            Opóźnienie - regresja szeregu wilgotności.
	 * @param iLag
	 *            Opóźnienie - regresja szeregu nasłonecznienia.
	 * @param dayType
	 *            Regresja - typ dnia
	 * @param holidayType
	 *            Regresja - typ święta
	 * @param eMean24Lag
	 * @param eMean168Lag
	 * @return Zbiór danych treningowych wygenerowany dla godziny
	 *         <code>timePoint</code>
	 * @throws SQLException
	 *             Wyjątek zgłaszany przez bazę danych.
	 */
	public abstract PatternPairVO getSetForHour(Connection dbCon,
			String signalName, Date timePoint, int eLag, int tLag, int hLag,
			int iLag, boolean dayType, boolean holidayType, int eMean24Lag,
			int eMean168Lag) throws SQLException;

	/**
	 * Pobiera zbiór treningowy dla określonego dnia.
	 * 
	 * @param dbCon
	 *            Połączenie z bazą danych.
	 * @param signalName
	 *            Nazwa sygnału.
	 * @param day
	 *            Dzień, z którego pobierane są dane.
	 * @param eLag
	 *            Opóźnienie - regresja szeregu energii.
	 * @param tLag
	 *            Opóźnienie - regresja szeregu temepratury.
	 * @param hLag
	 *            Opóźnienie - regresja szeregu wilgotności.
	 * @param iLag
	 *            Opóźnienie - regresja szeregu nasłonecznienia.
	 * @return Zbiór danych treningowych wygenerowany z dnia <code>day</code>
	 * @throws SQLException
	 *             Wyjątek zgłaszany przez bazę danych.
	 */
	public abstract List<PatternPairVO> getSetForDay(Connection dbCon,
			String signalName, Date day, int eLag, int tLag, int hLag, int iLag)
			throws SQLException;

	/**
	 * Pobiera zbiór treningowy dla określonego dnia.
	 * 
	 * @param dbCon
	 *            Połączenie z bazą danych.
	 * @param signalName
	 *            Nazwa sygnału.
	 * @param day
	 *            Dzień, z którego pobierane są dane.
	 * @param eLag
	 *            Opóźnienie - regresja szeregu energii.
	 * @param tLag
	 *            Opóźnienie - regresja szeregu temepratury.
	 * @param hLag
	 *            Opóźnienie - regresja szeregu wilgotności.
	 * @param iLag
	 *            Opóźnienie - regresja szeregu nasłonecznienia.
	 * @param dayType
	 *            Regresja - typ dnia
	 * @param holidayType
	 *            Regresja - typ święta
	 * @param eMean24Lag
	 *            Opóźnienie - średnia dobowa.
	 * @param eMean186Lag
	 *            Opóźnienie - średnia tygodniowa.
	 * @return Zbiór danych treningowych wygenerowany z dnia <code>day</code>
	 * @throws SQLException
	 *             Wyjątek zgłaszany przez bazę danych.
	 */
	public abstract List<PatternPairVO> getSetForDay(Connection dbCon,
			String signalName, Date day, int eLag, int tLag, int hLag,
			int iLag, boolean dayType, boolean holidayType, int eMean24Lag,
			int eMean186Lag) throws SQLException;

	/**
	 * Zraca identyfikator rekordu z tabeli <code>singals</code> dla podanego
	 * dnia i godziny TODO : co z dniem 25 godzinnym ?
	 * 
	 * @param dbCon
	 *            Połączenie z bazą danych.
	 * @param signalName
	 *            Nazwa sygnału
	 * @param day
	 *            Dzień.
	 * @param hour
	 *            Godzina.
	 * @return Indetyfikator rekordu.
	 * @throws SQLException
	 *             Wyjątek zgłaszany przez bazę danych.
	 */
	public abstract long getSignalId(Connection dbCon, String signalName,
			Date day, int hour) throws SQLException;

	/**
	 * Zwraca klasę danego dnia.
	 * 
	 * @param dbCon
	 *            Połączenie z bazą danych.
	 * @param signalName
	 *            Nazwa sygnału.
	 * @param predictionDay
	 *            Badany dzień.
	 * @return String[0] - h/d, String[1] - klasa.
	 * @throws SQLException
	 *             Wyjątek zgłaszany przez bazę danych.
	 */
	public abstract String[] getTypeOfDay(Connection dbCon, String signalName,
			long predictionDay) throws SQLException;

	/**
	 * Pobiera dni należące do danej klasy i zwierające się w danym oknie
	 * pomiarowym.
	 * 
	 * @param dbCon
	 *            Połączenie z bazą danych.
	 * @param signalName
	 *            Nazwa sygnału.
	 * @param windows
	 *            Okno pomiarowe.
	 * @param cl
	 *            Puszukiwana klasa dnia.
	 * @param dzienPrognozy
	 *            Dzień kóry będzie modelowany (środek okna pomiarowego).
	 * @return Lista dat - dni danej klasy.
	 * @throws SQLException
	 *             Wyjątek zgłaszany przez bazę danych.
	 */
	public abstract ArrayList<Date> getWindowsClasses(Connection dbCon,
													  String signalName, ArrayList<WindowVO> windows, String[] cl,
													  Date dzienPrognozy) throws SQLException;

	/**
	 * Czyta wszystkie dane pomiarowe z tabeli <code>signals</code>.
	 * 
	 * @param dbCon
	 *            Połączenie z bazą danych.
	 * @param signalName
	 *            Nazwa sygnału.
	 * @return Mapa profili dni.
	 * @throws SQLException
	 *             Wyjątek zgłaszany przez bazę danych.
	 */
	public abstract Map<Date, List<DataVO>> getAllDayProfiles(Connection dbCon,
															  String signalName) throws SQLException;

	/**
	 * Pobiera listę różnych klas dni znajdujących się w tabeli
	 * <code>signals</code>.
	 * 
	 * @param dbCon
	 *            Połączenie z bazą danych.
	 * @return Lista nazw klas.
	 * @throws SQLException
	 *             Wyjątek zgłaszany przez bazę danych.
	 */
	public abstract List<String> getDiffClasses(Connection dbCon)
			throws SQLException;

	/**
	 * Zapisuje listę danych w bazie danych w tabelach <code>signals</code> i
	 * <code>signal_names</code>.
	 * 
	 * @param dbCon
	 *            Połączenie z bazą danych.
	 * @param lst
	 *            Lista danych.
	 * @param signalNameId
	 *            Identyfikator definicji sygnału.
	 * @throws SQLException
	 *             Wyjątek zgłaszany przez bazę danych.
	 */
	public void saveToDataBase(Connection dbCon, List<DataVO> lst,
			Long signalNameId) throws SQLException;

	/**
	 * Zapisuje listę danych w bazie danych w tabelach <code>signals</code> i
	 * <code>signal_names</code>.
	 * 
	 * @param dbCon
	 *            Połączenie z bazą danych.
	 * @param lst
	 *            Lista danych.
	 * @param signalNameId
	 *            Identyfikator definicji sygnału.
	 * @throws SQLException
	 *             Wyjątek zgłaszany przez bazę danych.
	 */
	public void updateMean(Connection dbCon, List<DataVO> lst, Long signalNameId)
			throws SQLException;

	/**
	 * Pobiera wiersz/wiersze z tabeli <code>signal_names</code>.
	 * 
	 * @param dbCon
	 *            Połączenie z bazą danych.
	 * @param query
	 *            Warunki selekcji
	 * @return Lista obiektów reprezentujących wiersze tabeli.
	 * @throws SQLException
	 *             Wyjątek zgłaszany przez bazę danych.
	 */
	public List<SignalNameVO> read(Connection dbCon, SignalNameVO query)
			throws SQLException;

	/**
	 * Tworzy wiersz w tabeli <code>signal_names</code>.
	 * 
	 * @param dbCon
	 *            Połączenie z bazą danych.
	 * @param signalName
	 *            Dane do zapisania w bazie danych.
	 * @return Identyfikator dodanego rekordu.
	 * @throws SQLException
	 *             Wyjątek zgłaszany przez bazę danych.
	 */
	public Long create(Connection dbCon, SignalNameVO signalName)
			throws SQLException;

	/**
	 * Pobiera wiersz/wiersze z tabeli <code>signal_names</code>.
	 * 
	 * @param dbCon
	 *            Połączenie z bazą danych.
	 * @return Lista obiektów reprezentujących wiersze tabeli.
	 * @throws SQLException
	 *             Wyjątek zgłaszany przez bazę danych.
	 */
	public List<DataVO> readSignal(Connection dbCon, String singalName,
			Date from, Date to) throws SQLException;

}
