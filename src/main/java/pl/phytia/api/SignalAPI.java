package pl.phytia.api;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import pl.phytia.dao.ConnectionManager;
import pl.phytia.dao.DAOFactory;
import pl.phytia.dao.SignalDAO;
import pl.phytia.model.enums.EnumDataBase;
import pl.phytia.model.enums.EnumIterationType;
import pl.phytia.model.sets.DoubleVector;
import pl.phytia.model.sets.PatternPairVO;
import pl.phytia.model.signals.DataVO;
import pl.phytia.model.signals.WindowVO;
import pl.phytia.utils.MathUtil;
import pl.phytia.dao.PredictionDAO;
import pl.phytia.model.db.PredictionDbVO;
import pl.phytia.model.db.SignalNameVO;
import pl.phytia.model.enums.EnumHolidayClass;
import pl.phytia.model.enums.EnumSignalType;
import pl.phytia.model.enums.EnumWorkDaysClass;
import pl.phytia.model.pred.ValidationPairVO;
import pl.phytia.model.sets.ClassPatternVO;
import pl.phytia.model.sets.SupervisedDataSet;
import pl.phytia.model.sets.UnsupervisedDataSet;
import pl.phytia.model.signals.StatVO;
import pl.phytia.utils.Localization;

/**
 * API danych pomiarowych.
 * 
 * @author Jarosław Protasiewicz
 */
public class SignalAPI {

	private static Calendar cal = Calendar.getInstance(Localization.plLocale);

	private static DAOFactory facory = DAOFactory
			.getDAOFactory(EnumDataBase.PostgreSQL);

	protected static Logger logger = Logger.getLogger(SignalAPI.class);

	public static double computeSumOfEnergy(String signalName, Date formTime,
			Date toTime) {
		double sumOfEnergy = 0;
		ConnectionManager manager = null;
		try {
			SignalDAO signal = facory.getSignalDAO();
			manager = signal.getManager(null);
			sumOfEnergy = signal.getSumOfEnergy(manager.getConnection(),
					signalName, formTime, toTime);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			manager.close(null, null);
		}
		return sumOfEnergy;
	}

	/**
	 * Przygotwanie jednego wektora wejściowego
	 * 
	 * @param outTimePoint
	 *            data wyjscia
	 * @param eLag
	 * @param tLag
	 * @param hLag
	 * @param iLag
	 * @param dayType
	 * @param holidayType
	 * @param eMean24Lag
	 * @param eMean168Lag
	 * @return PatternPairVO
	 */
	public static PatternPairVO prepareVectorForTimePoint(String signalName,
														  Date outTimePoint, int eLag, int tLag, int hLag, int iLag,
														  boolean dayType, boolean holidayType, int eMean24Lag,
														  int eMean168Lag) {

		ConnectionManager manager = null;
		PatternPairVO pattern = null;
		try {
			SignalDAO signal = facory.getSignalDAO();
			manager = signal.getManager(null);
			pattern = signal.getSetForHour(manager.getConnection(), signalName,
					outTimePoint, eLag, tLag, hLag, iLag, dayType, holidayType,
					eMean24Lag, eMean168Lag);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			manager.close(null, null);
		}
		return pattern;
	}

	/**
	 * Przygotowuje dane dla zadanej listy punktów.
	 * 
	 * @param timePoints
	 * @param eLag
	 * @param tLag
	 * @param hLag
	 * @param iLag
	 * @param dayType
	 * @param holidayType
	 * @param eMean24Lag
	 * @param eMean168Lag
	 * @return Zb.
	 */
	public static SupervisedDataSet prepareSetForTimePoints(String signalName,
			List<Date> timePoints, int eLag, int tLag, int hLag, int iLag,
			boolean dayType, boolean holidayType, int eMean24Lag,
			int eMean168Lag) {
		SupervisedDataSet set = new SupervisedDataSet(
				new ArrayList<PatternPairVO>(), EnumIterationType.RANDOM);
		ConnectionManager manager = null;
		try {
			SignalDAO signal = facory.getSignalDAO();
			manager = signal.getManager(null);
			for (Date d : timePoints) {
				set.getSet().add(
						signal.getSetForHour(manager.getConnection(),
								signalName, d, eLag, tLag, hLag, iLag, dayType,
								holidayType, eMean24Lag, eMean168Lag));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			manager.close(null, null);
		}
		return set;
	}

	/**
	 * Pobiera dane dla klasyfikacji.
	 * 
	 * @param setFormDate
	 *            Zakres zbioru uczącego.
	 * @param setToDate
	 *            Zakres zbioru uczącego.
	 * @param eLag
	 *            Opóźnienie - regresja szeregu energii.
	 * @param tLag
	 *            Opóźnienie - regresja szeregu temepratury.
	 * @param hLag
	 *            Opóźnienie - regresja szeregu wilgotności.
	 * @param iLag
	 *            Opóźnienie - regresja szeregu nasłonecznienia.
	 * @param dayType
	 *            Typ dnia
	 * @param holidayType
	 *            Typ świeta
	 * @param eMean24Lag
	 *            Opóźnienie - średnia dobowa.
	 * @param eMean186Lag
	 *            Opóźnienie - średnia tygodniowa.
	 * @return Zbiór danych treningowych dla danego modelu.
	 */
	public static UnsupervisedDataSet prepareAllTrainDataForClassifiction(
			String signalName, Date setFormDate, Date setToDate, int eLag,
			int tLag, int hLag, int iLag, boolean dayType, boolean holidayType,
			int eMean24Lag, int eMean186Lag) {

		UnsupervisedDataSet set = new UnsupervisedDataSet(null,
				EnumIterationType.RANDOM);
		ConnectionManager manager = null;
		int id = 0;
		try {
			SignalDAO signal = facory.getSignalDAO();
			manager = signal.getManager(null);
			Calendar cal = Calendar.getInstance(Localization.plLocale);
			cal.setTime(setFormDate);
			cal.add(Calendar.DAY_OF_YEAR, -1);
			do {
				cal.add(Calendar.DAY_OF_YEAR, +1);
				List<PatternPairVO> tmp = signal.getSetForDay(manager
						.getConnection(), signalName, cal.getTime(), eLag,
						tLag, hLag, iLag, dayType, holidayType, eMean24Lag,
						eMean186Lag);
				List<ClassPatternVO> pat = new ArrayList<ClassPatternVO>();
				for (PatternPairVO pp : tmp) {
					pat.add(new ClassPatternVO(pp.getInputs().clone(), null,
							id++, pp.getOutTime()));
				}
				set.setSet(pat, true);
				logger
						.debug("Odczyt danych dla dnia: "
								+ Localization.plDateFormatMedium.format(cal
										.getTime()));

			} while (cal.getTime().before(setToDate));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			manager.close(null, null);
		}
		return set;
	}

	/**
	 * Generuje na podstawie bazy danych zbiór uczący dla modelu danej klasy.
	 * 
	 * @param setFormDate
	 *            Zakres zbioru uczącego.
	 * @param setToDate
	 *            Zakres zbioru uczącego.
	 * @param modelClass
	 *            Klasa danych - modelu.
	 * @param eLag
	 *            Opóźnienie - regresja szeregu energii.
	 * @param tLag
	 *            Opóźnienie - regresja szeregu temepratury.
	 * @param hLag
	 *            Opóźnienie - regresja szeregu wilgotności.
	 * @param iLag
	 *            Opóźnienie - regresja szeregu nasłonecznienia.
	 * @return Zbiór danych treningowych dla danego modelu.
	 */
	public static SupervisedDataSet prepareTrainForModelClass(
			String signalName, Date setFormDate, Date setToDate,
			String modelClass, int eLag, int tLag, int hLag, int iLag) {

		SupervisedDataSet set = new SupervisedDataSet(null,
				EnumIterationType.RANDOM);
		ConnectionManager manager = null;
		try {
			SignalDAO signal = facory.getSignalDAO();
			manager = signal.getManager(null);
			/*
			 * Ustalam okno pomiarowe.
			 */
			ArrayList<WindowVO> windows = new ArrayList<WindowVO>();
			WindowVO w = new WindowVO();
			Calendar cal = Calendar.getInstance(Localization.plLocale);
			cal.setTime(setFormDate);
			w.setDateFrom(cal.getTime());
			w.setDateFromLong(cal.getTimeInMillis());
			cal.setTime(setToDate);
			w.setDateTo(cal.getTime());
			w.setDateToLong(cal.getTimeInMillis());
			windows.add(w);
			/*
			 * Ustalam dni z okna pomiarowego, które odpowiadają klasie modelu.
			 */
			String[] cl = new String[2];
			boolean isClassFound = false;
			for (EnumWorkDaysClass e : EnumWorkDaysClass.values()) {
				if (modelClass.equals(e.toString()) && !isClassFound) {
					cl[0] = "d";
					cl[1] = modelClass;
					isClassFound = true;
				}
			}
			if (!isClassFound) {
				for (EnumHolidayClass e : EnumHolidayClass.values()) {
					if (modelClass.equals(e.toString())) {
						cl[0] = "h";
						cl[1] = modelClass;
					}
				}
			}
			ArrayList<Date> trDaty = signal.getWindowsClasses(manager
					.getConnection(), signalName, windows, cl, null);

			for (Date day : trDaty) {
				logger.debug("Pobieram zb. uczący dla klasy " + modelClass
						+ " z dnia: "
						+ Localization.plDateFormatMedium.format(day));
				set.setSet(signal.getSetForDay(manager.getConnection(),
						signalName, day, eLag, tLag, hLag, iLag), true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			manager.close(null, null);
		}
		return set;
	}

	public static List<ValidationPairVO> prepareLOOCossValidationData(
			String signalName, Date dzienPrognozy, int minus, int plus,
			int years, int eLag, int tLag, int hLag, int iLag) throws Exception {
		List<ValidationPairVO> result = new ArrayList<ValidationPairVO>();

		ConnectionManager manager = null;
		try {
			SignalDAO signal = facory.getSignalDAO();
			manager = signal.getManager(null);
			/*
			 * Ustalam okno pomiarowe.
			 */
			cal.setTime(dzienPrognozy);
			cal.add(Calendar.DAY_OF_YEAR, 1);
			ArrayList<WindowVO> windows = getDatesWindow(cal.getTime(), cal
					.getTime(), minus - 1, plus - 1, years);
			/*
			 * Ustalam kalasę dnia prognozy.
			 */
			cal.setTime(dzienPrognozy);
			String[] cl = signal.getTypeOfDay(manager.getConnection(),
					signalName, cal.getTimeInMillis());
			/*
			 * Ustalam dni z okna pomiarowego, które odpowiadają klasie modelu.
			 * Generuję zbiory walidacyjne.
			 */
			ArrayList<Date> trDaty = signal.getWindowsClasses(manager
					.getConnection(), signalName, windows, cl, dzienPrognozy);
			for (int i = 0; i < trDaty.size(); ++i) {
				logger
						.debug("Pobieram zb. uczący dla dnia: "
								+ Localization.plDateFormatMedium.format(trDaty
										.get(i)));
				SupervisedDataSet trainingSet = new SupervisedDataSet(null,
						EnumIterationType.RANDOM);
				SupervisedDataSet validationSet = new SupervisedDataSet(null,
						EnumIterationType.SERIAL);
				for (int j = 0; j < trDaty.size(); ++j) {
					Date day = trDaty.get(j);
					if (i != j) {
						trainingSet.setSet(signal.getSetForDay(manager
								.getConnection(), signalName, day, eLag, tLag,
								hLag, iLag), true);
					} else {
						validationSet.setSet(signal.getSetForDay(manager
								.getConnection(), signalName, day, eLag, tLag,
								hLag, iLag), true);
					}
				}
				result.add(new ValidationPairVO(trainingSet, validationSet,
						cl[1]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			manager.close(null, null);
		}
		return result;
	}

	/**
	 * Generuje na podstawie bazy danych zbiór uczący dla jednego modelu z
	 * Meta-modelu.
	 * 
	 * @param dzienPrognozy
	 *            Modelowany - prognozowany dzień.
	 * @param dzienStartPrognozy
	 *            Pierszy dzień prognozy.
	 * @param minus
	 *            Szerokość okna na lewo od <code>dzienPrognozy</code>.
	 * @param plus
	 *            Szerokość okna na prawo od <code>dzienPrognozy</code>.
	 * @param years
	 *            Ilość lat wstecz.
	 * @param eLag
	 *            Opóźnienie - regresja szeregu energii.
	 * @param tLag
	 *            Opóźnienie - regresja szeregu temepratury.
	 * @param hLag
	 *            Opóźnienie - regresja szeregu wilgotności.
	 * @param iLag
	 *            Opóźnienie - regresja szeregu nasłonecznienia.
	 * @return Zbiór danych treningowych dla danego modelu.
	 * @throws Exception
	 *             Wyjątek.
	 */
	public static SupervisedDataSet prepareTrainSet(String signalName,
			Date dzienPrognozy, Date dzienStartPrognozy, int minus, int plus,
			int years, int eLag, int tLag, int hLag, int iLag) throws Exception {

		// System.out.println("START: getTrainSet");
		SupervisedDataSet set = new SupervisedDataSet(null,
				EnumIterationType.RANDOM);
		ConnectionManager manager = null;
		try {
			SignalDAO signal = facory.getSignalDAO();
			manager = signal.getManager(null);
			/*
			 * Ustalam okno pomiarowe.
			 */
			ArrayList<WindowVO> windows = getDatesWindow(dzienPrognozy,
					dzienStartPrognozy, minus, plus, years);
			for (WindowVO win : windows) {
				// System.out.println("from: " +
				// Localization.plDateFormatMedium.format(win.getDateFrom()) + "
				// to: " +
				// Localization.plDateFormatMedium.format(win.getDateTo()));
			}
			/*
			 * Ustalam kalasę dnia prognozy.
			 */
			cal.setTime(dzienPrognozy);
			String[] cl = signal.getTypeOfDay(manager.getConnection(),
					signalName, cal.getTimeInMillis());
			/*
			 * Ustalam dni z okna pomiarowego, które odpowiadają klasie modelu.
			 */
			ArrayList<Date> trDaty = signal.getWindowsClasses(manager
					.getConnection(), signalName, windows, cl, dzienPrognozy);

			for (Date day : trDaty) {
				logger.debug("Pobieram zb. uczący dla klasy " + cl[0] + " "
						+ cl[1] + " i dnia: "
						+ Localization.plDateFormatMedium.format(day));
				set.setSet(signal.getSetForDay(manager.getConnection(),
						signalName, day, eLag, tLag, hLag, iLag), true);
			}
		} finally {
			manager.close(null, null);
		}
		return set;
	}

	public static SupervisedDataSet preparePredictionSet(String signalName,
			Date predictionDay, int eLag, int tLag, int hLag, int iLag)
			throws Exception {
		SupervisedDataSet set = new SupervisedDataSet(null,
				EnumIterationType.SERIAL);
		SignalDAO signal = facory.getSignalDAO();
		ConnectionManager manager = signal.getManager(null);
		set.setSet(signal.getSetForDay(manager.getConnection(), signalName,
				predictionDay, eLag, tLag, hLag, iLag), false);
		return set;
	}

	public static List<StatVO> predictionStatistic(String predType,
			Date statFrom, Date statTo, Integer dayAhead) {
		List<StatVO> results = new ArrayList<StatVO>();
		PredictionDAO predDao = facory.getPredictionDAO();
		SignalDAO sigDao = facory.getSignalDAO();
		ConnectionManager manager = predDao.getManager(null);
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			/*
			 * Pobranie identyfikatorów sygnałów i utworzenie definicji sygnału
			 * wyjściowego w przypadku jej braku.
			 */
			SignalNameVO signalQuery = new SignalNameVO(null, predType);
			List<SignalNameVO> signals = sigDao.read(manager.getConnection(),
					signalQuery);
			if (signals == null || signals.size() != 1) {
				throw new Exception("Niepoprawna definicja nazwy sygnału!");
			}
			List<PredictionDbVO> predictions = predDao.read(manager
					.getConnection(), signals.get(0).getSignalNameId(),
					statFrom, statTo, dayAhead);
			/*
			 * TreeMap<Date, PredictionDbVO> predMLPMap = new TreeMap<Date,
			 * PredictionDbVO>(); for (PredictionDbVO p: predictions) {
			 * predMLPMap.put(p.getPredTime(), p); }
			 */
			List<DataVO> reals = sigDao.readSignal(manager.getConnection(),
					EnumSignalType.MODELLING_DATA_NORMALIZED.toString(),
					statFrom, statTo);
			/*
			 * TreeMap<Date, DataVO> realMap = new TreeMap<Date, DataVO>();
			 * for (DataVO r: reals) { realMap.put(r.getTime(), r); }
			 */

			if (predictions.size() != reals.size()) {
				throw new Exception(
						"Nieporawny rozmiar wektora prognozy i wykonania");
			}
			PredictionDbVO pred = null;
			DataVO re = null;
			Date chaneDay = new Date(statFrom.getTime());
			StatVO stat = new StatVO();
			stat.setApe(new DoubleVector(24, 0.0));
			stat.setPe(new DoubleVector(24, 0.0));
			int indx = 0;
			for (int i = 0; i < predictions.size(); ++i) {
				re = reals.get(i);
				pred = predictions.get(i);
				if (!re.getData().equals(pred.getPredDay())
						&& re.getHour() != pred.getPredHour()) {
					throw new Exception("Niezgodne dni");
				}
				if (!chaneDay.equals(re.getData())) {
					stat.setStatDay(chaneDay);
					stat.setMape(stat.getApe().mape());
					results.add(stat);
					stat = new StatVO();
					stat.setApe(new DoubleVector(24, 0.0));
					stat.setPe(new DoubleVector(24, 0.0));
					indx = 0;
					chaneDay = re.getData();
				}
				if (chaneDay.equals(re.getData())) {
					stat.getApe().set(indx,
							MathUtil.ape(re.getEnergy(), pred.getPredValue()));
					stat.getPe().set(indx++,
							MathUtil.pe(re.getEnergy(), pred.getPredValue()));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			manager.close(rs, ps);
		}
		return results;
	}

	public static List<StatVO> predictionMonthlyStat(String predSignalName,
			String realSignalName, Date statFrom, Date statTo, Integer dayAhead) {
		List<StatVO> results = new ArrayList<StatVO>();
		PredictionDAO predDao = facory.getPredictionDAO();
		SignalDAO sigDao = facory.getSignalDAO();
		ConnectionManager manager = predDao.getManager(null);
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			/*
			 * Pobranie identyfikatora sygnału porgnozy i pobranie danych
			 * prognozy
			 */
			SignalNameVO signalQuery = new SignalNameVO(null, predSignalName);
			List<SignalNameVO> signals = sigDao.read(manager.getConnection(),
					signalQuery);
			if (signals == null || signals.size() != 1) {
				throw new Exception(
						"Niepoprawna definicja nazwy sygnału prognozy!");
			}
			List<PredictionDbVO> predictions = predDao.read(manager
					.getConnection(), signals.get(0).getSignalNameId(),
					statFrom, statTo, dayAhead);
			/*
			 * Pobranie sygnału rzeczywistego.
			 */
			List<DataVO> reals = sigDao.readSignal(manager.getConnection(),
					EnumSignalType.MODELLING_DATA_NORMALIZED.toString(),
					statFrom, statTo);
			/*
			 * Kontrola rozmiarów sygnałów.
			 */
			if (predictions.size() != reals.size()) {
				throw new Exception(
						"Nieporawny rozmiar wektora prognozy i wykonania!");
			}
			Calendar cal = Calendar.getInstance(Localization.plLocale);
			PredictionDbVO pred = null;
			DataVO real = null;
			DoubleVector apeMonthErrors = null;
			DoubleVector apeYearErrors = null;
			StatVO result = null;
			int currMonth = -1, lastMonth = -1;
			for (int i = 0; i < predictions.size(); ++i) {
				pred = predictions.get(i);
				real = reals.get(i);
				if (!real.getData().equals(pred.getPredDay())
						&& real.getHour() != pred.getPredHour()) {
					throw new Exception(
							"Niegodność danych prognozy i wykonania!");
				}
				cal.setTime(pred.getPredTime());
				currMonth = cal.get(Calendar.MONTH);
				if (i == 0) {
					apeMonthErrors = new DoubleVector();
					apeYearErrors = new DoubleVector();
					apeMonthErrors.add(MathUtil.ape(real.getEnergy(), pred
							.getPredValue().doubleValue()));
					apeYearErrors.add(MathUtil.ape(real.getEnergy(), pred
							.getPredValue().doubleValue()));
				} else if (currMonth != lastMonth) {
					result = new StatVO();
					result.setMape(apeMonthErrors.mape());
					result.setMaxApe(apeMonthErrors.max());
					result.setMonth(lastMonth + 1);
					results.add(result);
					apeMonthErrors = new DoubleVector();
					apeMonthErrors.add(MathUtil.ape(real.getEnergy(), pred
							.getPredValue().doubleValue()));
					apeYearErrors.add(MathUtil.ape(real.getEnergy(), pred
							.getPredValue().doubleValue()));
				} else if (i == predictions.size() - 1) {
					apeMonthErrors.add(MathUtil.ape(real.getEnergy(), pred
							.getPredValue().doubleValue()));
					apeYearErrors.add(MathUtil.pe(real.getEnergy(), pred
							.getPredValue().doubleValue()));
					result = new StatVO();
					result.setMape(apeMonthErrors.mape());
					result.setMaxApe(apeMonthErrors.max());
					result.setMonth(currMonth + 1);
					results.add(result);
					result = new StatVO();
					result.setMape(apeYearErrors.mape());
					result.setMaxApe(apeYearErrors.max());
					result.setYear(cal.get(Calendar.YEAR));
					results.add(result);
				} else {
					apeMonthErrors.add(MathUtil.ape(real.getEnergy(), pred
							.getPredValue().doubleValue()));
					apeYearErrors.add(MathUtil.ape(real.getEnergy(), pred
							.getPredValue().doubleValue()));
				}
				lastMonth = currMonth;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			manager.close(rs, ps);
		}
		return results;
	}

	/**
	 * Czyta dane z pliku o ustalonym formacie.
	 * 
	 * @param fileName
	 *            Nazwa pliku ze ścieżką.
	 * @return Lista odczytanych danych.
	 * @throws IOException
	 *             Wyjątek operacji we-wy.
	 */
	public static List<DataVO> readDataFromFile(String fileName)
			throws IOException {
		List<DataVO> lst = new ArrayList<DataVO>();
		InputStream in = new FileInputStream(fileName);
		POIFSFileSystem fs = new POIFSFileSystem(in);
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		HSSFSheet sheet = wb.getSheetAt(0);
		Iterator rows = sheet.rowIterator();
		while (rows.hasNext()) {
			HSSFRow row = (HSSFRow) rows.next();
			Iterator cells = row.cellIterator();
			DataVO dat = new DataVO();
			while (cells.hasNext()) {
				HSSFCell cell = (HSSFCell) cells.next();
				switch (cell.getCellNum()) {
				case 0:
					dat.setTime(cell.getDateCellValue());
					break;
				case 1:
					dat.setTimeType((short) cell.getNumericCellValue());
					break;
				case 2:
					dat.setEnergy(cell.getNumericCellValue());
					break;
				case 3:
					dat.setInsolation(cell.getNumericCellValue());
					break;
				case 4:
					dat.setHumidity(cell.getNumericCellValue());
					break;
				case 5:
					dat.setTemperature(cell.getNumericCellValue());
					break;
				case 6:
					dat.setDayType(cell.getStringCellValue());
					break;
				case 7:
					if (cell.getStringCellValue() != null
							&& cell.getStringCellValue().length() > 0) {
						dat.setHoliday(cell.getStringCellValue());
					}
					break;
				default:
					throw new IOException("Błąd czytania komórki pliku");
				}
			}
			lst.add(dat);
		}
		return lst;
	}

	/**
	 * Zwraca daty okna pomiarowego. Daty graniczne wchodzą w zakres pobieranych
	 * danych.
	 * 
	 * @param dzienPrognozy
	 *            Modelowany dzień.
	 * @param dzienStartPrognozy
	 *            Pierwszy prognozowany (modelowany) dzień.
	 * @param minus
	 *            Szerokość okna na lewo od <code>dzienPrognozy</code>.
	 * @param plus
	 *            Szerokość okna na prawo od <code>dzienPrognozy</code>.
	 * @param years
	 *            Ilość lat wstecz.
	 * @return Okon pomiarowe.
	 */
	private static ArrayList<WindowVO> getDatesWindow(Date dzienPrognozy,
			Date dzienStartPrognozy, int minus, int plus, int years) {
		ArrayList<WindowVO> lst = new ArrayList<WindowVO>();
		int y = 0;
		int year;
		do {
			WindowVO w = new WindowVO();
			cal.setTime(dzienPrognozy);
			cal.add(Calendar.YEAR, y);
			year = cal.get(Calendar.YEAR);
			cal.add(Calendar.DAY_OF_YEAR, minus);
			if (year != cal.get(Calendar.YEAR)) {
				cal.set(year, 0, 1);
			}
			w.setDateFromLong(cal.getTimeInMillis());
			w.setDateFrom(cal.getTime());
			if (y == 0) {
				cal.setTime(dzienStartPrognozy);
				cal.add(Calendar.DAY_OF_YEAR, -1);
			} else {
				cal.setTime(dzienPrognozy);
				cal.add(Calendar.YEAR, y);
				year = cal.get(Calendar.YEAR);
				cal.add(Calendar.DAY_OF_YEAR, plus);
				if (year != cal.get(Calendar.YEAR)) {
					cal.set(year, 11, 31);
				}
			}
			w.setDateToLong(cal.getTimeInMillis());
			w.setDateTo(cal.getTime());
			lst.add(w);
			--y;
		} while (y > years - 1);
		return lst;
	}

}
