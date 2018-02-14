package pl.phytia.api;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import pl.phytia.dao.ConnectionManager;
import pl.phytia.dao.DAOFactory;
import pl.phytia.dao.SignalDAO;
import pl.phytia.model.db.MetaModelDbVO;
import pl.phytia.model.enums.EnumDataBase;
import pl.phytia.prediction.metaModels.ParallelMetaModel;
import pl.phytia.prediction.models.MLPModel;
import pl.phytia.prediction.models.Model;
import pl.phytia.dao.MetaModelDAO;
import pl.phytia.dao.ModelDAO;
import pl.phytia.dao.PredictionDAO;
import pl.phytia.model.db.ModelDbVO;
import pl.phytia.model.db.PredictionDbVO;
import pl.phytia.model.db.SignalNameVO;
import pl.phytia.model.enums.EnumNetworkType;
import pl.phytia.model.enums.EnumSignalType;
import pl.phytia.model.sets.DoubleVector;
import pl.phytia.prediction.metaModels.MetaModel;
import pl.phytia.prediction.metaModels.SerialMetaModel;
import pl.phytia.prediction.models.RBFModel;
import pl.phytia.prediction.models.SOMModel;
import pl.phytia.utils.Localization;

public class MetaModelAPI {

	private static DAOFactory facory = DAOFactory
			.getDAOFactory(EnumDataBase.PostgreSQL);

	/**
	 * Zapis danych szeregowego metamodelu do bazy danych.
	 * 
	 * @param metamodel
	 *            MetaModel.
	 * @param saveModels
	 *            Decyduje czy zapisywać modele.
	 * @param saveForecast
	 *            Decyduje czy zapisywać prognozy.
	 */
	public static void saveSerialMetaModel(SerialMetaModel metamodel,
			boolean saveModels, boolean saveForecast) {
		ConnectionManager manager = null;
		MetaModelDAO metaModelDao = facory.getMetaModelDAO();
		try {
			manager = metaModelDao.getManager(null, false);
			/*
			 * Zapis / modyfikacja metamodelu.
			 */
			MetaModelDbVO metaModelDb = saveMetaModel(manager.getConnection(),
					metamodel);
			/*
			 * Zapis modeli i definicji w szeregu.
			 */
			if (saveModels) {
				/*
				 * Zapis / modyfikacja modeli.
				 */
				for (int i = 0; i < metamodel.getPredictors().size(); ++i) {
					String modelType = ((Model) metamodel.getPredictors()
							.get(i)).getConfig().getModelType();
					if (modelType.equals(EnumNetworkType.MLP.getType())) {
						MLPModel model = ((MLPModel) metamodel.getPredictors()
								.get(i));
						saveModel(manager.getConnection(), model, metaModelDb
								.getIdMetamodelu(), metaModelDb
								.getOutputSignal(), null, saveForecast,
								new Integer(i + 1));
					} else {
						// TODO: obsługa modelu RBF.
						throw new RuntimeException("Nie obsługiwany typ modelu");
					}
				}
			}
			manager.commit();
		} catch (Exception e) {
			e.printStackTrace();
			manager.rollback();
		} finally {
			manager.close(null, null);
		}
	}

	/**
	 * Zapis modelu równoległego.
	 * 
	 * @param metamodel
	 *            Metamodel
	 * @param withClassifier
	 *            Czy zapisać klasyfikator.
	 * @param withPredictors
	 *            Czy zapisać predyktory. 0 - nie zapisywać, 1- zapisz MLP, 2
	 *            -zapisz RBF
	 * @param withForecasts
	 *            Czy zapisać prognozę.
	 */
	public static void saveParallelMetaModel(ParallelMetaModel metamodel,
			boolean withClassifier, int withPredictors, boolean withForecasts) {
		ConnectionManager manager = null;
		MetaModelDAO metaModelDao = facory.getMetaModelDAO();
		PredictionDAO predDao = facory.getPredictionDAO();
		try {
			manager = metaModelDao.getManager(null);
			/*
			 * Zapis / modyfikacja metamodelu.
			 */
			MetaModelDbVO metaModelDb = saveMetaModel(manager.getConnection(),
					metamodel);
			/*
			 * Zapis / modyfikacja klasyfikatora
			 */
			if (withClassifier) {
				saveModel(manager.getConnection(), metamodel.getClassifier(),
						metaModelDb.getIdMetamodelu(), metaModelDb
								.getOutputSignal(), null, false, null);
			}
			/*
			 * Zapis / modyfikacja predyktorów
			 */
			if (withPredictors == 1) {
				Iterator it = metamodel.getPredModelMap().keySet().iterator();
				String key = null;
				while (it.hasNext()) {
					key = (String) it.next();
					MLPModel model = (MLPModel) metamodel.getPredModelMap()
							.get(key);
					saveModel(manager.getConnection(), model, metaModelDb
							.getIdMetamodelu(), metaModelDb.getOutputSignal(),
							null, false, -1);
				}
			} else if (withPredictors == 2) {
				Iterator it = metamodel.getPredModelMap().keySet().iterator();
				String key = null;
				while (it.hasNext()) {
					key = (String) it.next();
					RBFModel model = (RBFModel) metamodel.getPredModelMap()
							.get(key);
					saveModel(manager.getConnection(), model, metaModelDb
							.getIdMetamodelu(), metaModelDb.getOutputSignal(),
							null, false, -1);
				}
			} else if (withPredictors == 3) {
				MLPModel model = (MLPModel) metamodel.getPredictors().get(0);
				saveModel(manager.getConnection(), model, metaModelDb
						.getIdMetamodelu(), metaModelDb.getOutputSignal(),
						null, false, -1);
			}
			if (withForecasts) {
				PredictionDbVO pred = null;
				for (int i = 0; i < metamodel.getResults().size(); ++i) {
					pred = (PredictionDbVO) metamodel.getResults().get(i);
					pred.setSignalNameId(metaModelDb.getOutputSignal());
					predDao.createOrUpdate(manager.getConnection(), pred);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			manager.close(null, null);
		}
	}

	/**
	 * Odczyt definicji modeli z bazy danych dla metamodelu szeregowego.
	 * 
	 * @param foreCastType
	 *            Typ prognozy <code>EnumForecastType</code>.
	 * @param metaModelType
	 *            Typ metamodelu <code>EnumMetamodelType</code>.
	 * @param modelType
	 *            Typ modelu <code>EnumNetworkType</code>.
	 * @param predStart
	 *            Początek okresu prognozy.
	 * @param daysAhead
	 *            Ilość dni wyprzedzenia.
	 * @return Lista modeli dla zadanego okresu.
	 */
	public static List<ModelDbVO> readDbModels(String foreCastType,
			String metaModelType, String modelType, Date predStart,
			int daysAhead) {
		List<ModelDbVO> dbModels = new ArrayList<ModelDbVO>();
		MetaModelDAO metaModelDao = facory.getMetaModelDAO();
		ModelDAO modelDao = facory.getModelDAO();
		SignalDAO signalDao = facory.getSignalDAO();
		ConnectionManager manager = null;
		try {
			manager = metaModelDao.getManager(null);
			/*
			 * Pobranie definicji metamodelu.
			 */
			MetaModelDbVO metamodel = new MetaModelDbVO(null, metaModelType,
					foreCastType, null, null, null);
			List<MetaModelDbVO> metamodels = metaModelDao.read(manager
					.getConnection(), metamodel);
			if (metamodels.size() != 1) {
				throw new RuntimeException("Błąd odczytu definicji metamodelu");
			}
			metamodel.setIdMetamodelu(metamodels.get(0).getIdMetamodelu());
			Calendar predCal = Calendar.getInstance(Localization.plLocale);
			predCal.setTime(predStart);
			for (int i = 0; i < daysAhead; ++i) {
				String cl[] = signalDao.getTypeOfDay(manager.getConnection(),
						EnumSignalType.MODELLING_DATA_NORMALIZED.toString(),
						predCal.getTimeInMillis());
				ModelDbVO query = new ModelDbVO(null, metamodel
						.getIdMetamodelu(), modelType, cl[1], null, null);
				List<ModelDbVO> modelsDb = modelDao.read(manager
						.getConnection(), query);
				if (modelsDb.size() != 1) {
					throw new RuntimeException(
							"Błąd odczytu modelu dla metamodelu");
				} else {
					dbModels.add(modelsDb.get(0));
				}
				predCal.add(Calendar.DAY_OF_YEAR, +1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			manager.close(null, null);
		}
		return dbModels;
	}

	/**
	 * Odczyt definicji modelu z bazy danych.
	 * 
	 * @param foreCastType
	 *            Typ prognozy <code>EnumForecastType</code>.
	 * @param metaModelType
	 *            Typ metamodelu <code>EnumMetamodelType</code>.
	 * @param modelType
	 *            Typ modelu <code>EnumNetworkType</code>.
	 * @param className
	 *            Nazwa klasy.
	 * @return Model.
	 */
	public static ModelDbVO readModel(String foreCastType,
			String metaModelType, String modelType, String className) {
		ModelDbVO model = null;
		ModelDAO modelDao = facory.getModelDAO();
		MetaModelDAO metaModelDao = facory.getMetaModelDAO();
		ConnectionManager manager = null;
		try {
			manager = metaModelDao.getManager(null);
			/*
			 * Pobranie definicji metamodelu.
			 */
			MetaModelDbVO metamodel = new MetaModelDbVO(null, metaModelType,
					foreCastType, null, null, null);
			List<MetaModelDbVO> metamodels = metaModelDao.read(manager
					.getConnection(), metamodel);
			if (metamodels.size() > 1) {
				metamodel.setIdMetamodelu(null);
				throw new RuntimeException("Błąd odczytu definicji metamodelu");
			} else if (metamodels.size() == 1) {
				metamodel.setIdMetamodelu(metamodels.get(0).getIdMetamodelu());
			} else {
				metamodel.setIdMetamodelu(null);
			}
			/*
			 * Pobranie modelu.
			 */
			if (metamodel.getIdMetamodelu() != null) {
				model = new ModelDbVO(null, metamodel.getIdMetamodelu(),
						modelType, className, null, null);
				List<ModelDbVO> modelsDb = modelDao.read(manager
						.getConnection(), model);
				if (modelsDb.size() > 1) {
					throw new RuntimeException(
							"Błąd odczytu modelu dla metamodelu");
				} else if (modelsDb.size() == 0) {
					model = null;
				} else {
					model = modelsDb.get(0);
				}
			} else {
				model = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			manager.close(null, null);
		}
		return model;
	}

	/* #################################################################### */
	/* ##################### PRIVATE ###################################### */
	/* #################################################################### */
	/**
	 * Zapis metamodelu do bazy danych.
	 * 
	 * @param dbCon
	 *            Połączenie z bazą danych.
	 * @param metamodel
	 *            MetaModel - zawsze zapisuje
	 * @return Obiekt zapisany / modyfikowany w bazie danych.
	 * @throws Exception
	 *             Wyjątek.
	 */
	private static MetaModelDbVO saveMetaModel(Connection dbCon,
			MetaModel metamodel) throws Exception {
		MetaModelDbVO metaModelDb = null;
		ConnectionManager manager = null;
		MetaModelDAO metaModelDao = facory.getMetaModelDAO();
		SignalDAO signalDao = facory.getSignalDAO();
		try {
			manager = metaModelDao.getManager(dbCon);
			/*
			 * Pobranie identyfikatorów sygnałów i utworzenie definicji sygnału
			 * wyjściowego w przypadku jej braku.
			 */
			SignalNameVO signalQuery = new SignalNameVO(null, metamodel
					.getConfig().getInputSignal().toString());
			List<SignalNameVO> inputSignals = signalDao.read(manager
					.getConnection(), signalQuery);
			if (inputSignals == null || inputSignals.size() != 1) {
				throw new Exception(
						"Niepoprawna definicja sygnału wejściowego Metamodelu!");
			}
			signalQuery = new SignalNameVO(null, metamodel.getConfig()
					.getOutputSignal().toString());
			List<SignalNameVO> outputSignals = signalDao.read(manager
					.getConnection(), signalQuery);
			Long outputSignalNameId = null;
			if (outputSignals.size() == 0) {
				// nowy sygnał
				outputSignalNameId = signalDao.create(manager.getConnection(),
						signalQuery);
			} else if (outputSignals.size() == 1) {
				// istnieje
				outputSignalNameId = outputSignals.get(0).getSignalNameId();
			} else {
				throw new Exception(
						"Niepoprawna definicja sygnału wyjściowego Metamodelu!");
			}
			/*
			 * Sprawdzenie czy istnieje dany metamodel. Jezeli istnieje to
			 * modyfikacja istniejącego w przeciwnym wypadku utworzenie nowego.
			 */
			metaModelDb = new MetaModelDbVO(null, metamodel.getConfig()
					.getMetamodelType().toString(), metamodel.getConfig()
					.getForcastType().toString(), inputSignals.get(0)
					.getSignalNameId(), outputSignalNameId, metamodel
					.getConfig().storeState());
			List<MetaModelDbVO> metaModels = metaModelDao.read(manager
					.getConnection(), metaModelDb);
			if (metaModels.size() == 0) {
				// nowy
				metaModelDb.setIdMetamodelu(metaModelDao.create(manager
						.getConnection(), metaModelDb));
			} else if (metaModels.size() == 1) {
				// modyfikacja
				metaModelDb
						.setIdMetamodelu(metaModels.get(0).getIdMetamodelu());
				metaModelDao.update(manager.getConnection(), metaModelDb);
			} else {
				throw new Exception("Niepoprawna definicja Metamodelu!");
			}
		} finally {
			manager.close(null, null);
		}
		return metaModelDb;
	}

	/**
	 * Zapis modelu i jego prognoz do bazy danych.
	 * 
	 * @param dbCon
	 *            Połączenie z bazą danych.
	 * @param model
	 *            Model.
	 * @param metamodelId
	 *            Identyfikator metamodelu.
	 * @param outputSignalNameId
	 *            Identyfikator sygnału prognozy.
	 * @param modelId
	 *            Identyfikator modelu (jeżeli podany to wykonywany jest
	 *            update).
	 * @param saveForecast
	 *            Decydyje o tym czy zapisywać prognozę.
	 * @param dayAhead
	 *            Dzień wyprzedzenia prognozy.
	 * @throws Exception
	 *             Wyjątek.
	 */
	private static void saveModel(Connection dbCon, Model model,
			Long metamodelId, Long outputSignalNameId, Long modelId,
			boolean saveForecast, Integer dayAhead) throws Exception {
		ModelDAO modelDao = facory.getModelDAO();
		PredictionDAO predDao = facory.getPredictionDAO();
		ConnectionManager manager = modelDao.getManager(dbCon);
		try {
			ModelDbVO modelDb = new ModelDbVO();
			Date predictionDay = null;
			DoubleVector results = null;
			if (model instanceof SOMModel) {
				SOMModel somModel = (SOMModel) model;
				modelDb.setConf(somModel.getConfig().storeState());
				modelDb.setModelClass(somModel.getModelClass());
				modelDb.setMetamodelId(metamodelId);
				modelDb.setModelId(modelId);
				modelDb.setModelType(somModel.getConfig().getModelType());
				modelDb.setNetwork(somModel.getNetwork().storeState());
			} else if (model instanceof MLPModel) {
				MLPModel mlpModel = (MLPModel) model;
				modelDb.setConf(mlpModel.getConfig().storeState());
				modelDb.setModelClass(mlpModel.getModelClass());
				modelDb.setMetamodelId(metamodelId);
				modelDb.setModelId(modelId);
				modelDb.setModelType(mlpModel.getConfig().getModelType());
				modelDb.setNetwork(mlpModel.getNetwork().storeState());
				predictionDay = mlpModel.getPredictionDay();
				results = mlpModel.getResults();
			} else if (model instanceof RBFModel) {
				RBFModel mlpModel = (RBFModel) model;
				modelDb.setConf(mlpModel.getConfig().storeState());
				modelDb.setModelClass(mlpModel.getModelClass());
				modelDb.setMetamodelId(metamodelId);
				modelDb.setModelId(modelId);
				modelDb.setModelType(mlpModel.getConfig().getModelType());
				modelDb.setNetwork(mlpModel.getNetwork().storeState());
				predictionDay = mlpModel.getPredPoint();
				results = mlpModel.getResults();
			} else {
				throw new Exception("Nie obsługiwany typ modelu");
			}
			/*
			 * Odczyt modelu.
			 */
			ModelDbVO query = new ModelDbVO(modelId, modelDb.getMetamodelId(),
					modelDb.getModelType(), model.getModelClass(), null, null);
			List<ModelDbVO> models = modelDao.read(manager.getConnection(),
					query);
			if (models.size() == 1) {
				/*
				 * Modyfikacja modelu.
				 */
				modelDb.setModelId(models.get(0).getModelId());
				modelDao.update(manager.getConnection(), modelDb);
			} else if (models.size() == 0) {
				/*
				 * Zapis modelu
				 */
				modelDao.create(manager.getConnection(), modelDb);
			} else {
				throw new RuntimeException("Błąd modeli w bazie danych");
			}
			/*
			 * Zapis prognozy
			 */
			if (saveForecast) {
				/*
				 * Zapis prognozy z metamodeli SOM-MLP
				 */
				if (results.size() == 1) {
					PredictionDbVO pred = null;
					Calendar cal = Calendar.getInstance(Localization.plLocale);
					for (Double out : results) {
						pred = new PredictionDbVO();
						cal.setTime(predictionDay);
						pred
								.setPredDay(new java.sql.Date(cal
										.getTimeInMillis()));
						pred.setPredHour(cal.get(Calendar.HOUR_OF_DAY) + 1);
						pred.setPredictionId(null);
						pred.setPredSeason(0);
						pred.setPredTime(cal.getTime());
						pred.setPredValue(out);
						pred.setSignalNameId(outputSignalNameId);
						pred.setDayAhead(dayAhead);
						predDao.createOrUpdate(manager.getConnection(), pred);
					}

				} else if (results.size() == 24) {
					PredictionDbVO pred = null;
					int hour = 0, htime = 0;
					Calendar cal = Calendar.getInstance(Localization.plLocale);
					for (Double out : results) {
						pred = new PredictionDbVO();
						cal.setTime(predictionDay);
						pred.setPredDay(cal.getTime());
						pred.setPredHour(new Integer(++hour));
						pred.setPredictionId(null);
						pred.setPredSeason(0);
						cal.set(Calendar.HOUR_OF_DAY, htime++);
						pred.setPredTime(cal.getTime());
						pred.setPredValue(out);
						pred.setSignalNameId(outputSignalNameId);
						pred.setDayAhead(dayAhead);
						predDao.createOrUpdate(manager.getConnection(), pred);
					}
				} else {
					throw new Exception("Nie obsługiwany typ prognozy");
				}
			}

		} finally {
			manager.close(null, null);
		}
	}
}
