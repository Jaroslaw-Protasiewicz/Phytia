package pl.phytia.prediction.statistics;

import java.text.ParseException;
import java.util.List;

import org.apache.log4j.Logger;

import pl.phytia.api.SignalAPI;
import pl.phytia.model.enums.EnumForecastType;
import pl.phytia.model.enums.EnumMetaModelType;
import pl.phytia.model.sets.DoubleVector;
import pl.phytia.utils.Localization;
import pl.phytia.utils.Tools;
import pl.phytia.model.signals.StatVO;

public class StatForRiskVaR {

	private static Logger logger = Logger.getLogger(StatForRiskVaR.class);

	private static final String[] tasks = {
			Tools.getForecastName(EnumMetaModelType.SERIAL_MLP,
					EnumForecastType.E24),
			Tools.getForecastName(EnumMetaModelType.SERIAL_MLP,
					EnumForecastType.E24_T0),
			Tools.getForecastName(EnumMetaModelType.SERIAL_MLP,
					EnumForecastType.E24_I0),
			Tools.getForecastName(EnumMetaModelType.SERIAL_MLP,
					EnumForecastType.E24_H0),
			Tools.getForecastName(EnumMetaModelType.SERIAL_MLP,
					EnumForecastType.E24_T0_I0),
			Tools.getForecastName(EnumMetaModelType.SERIAL_MLP,
					EnumForecastType.E24_T0_H0),
			Tools.getForecastName(EnumMetaModelType.SERIAL_MLP,
					EnumForecastType.E24_H0_I0),
			Tools.getForecastName(EnumMetaModelType.SERIAL_MLP,
					EnumForecastType.E24_T0_H0_I0) };

	public static void main(String[] args) throws Exception {
		final int daysAhead = 2;
		java.util.Date dateStart = null;
		java.util.Date dateStop = null;
		try {
			dateStart = Localization.plDateFormatMedium.parse("2004-01-07");
			dateStop = Localization.plDateFormatMedium.parse("2004-09-14");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		/*
		 * Wykonanie statysytyk.
		 */
		for (int t = 0; t < tasks.length; ++t) {
			String title = "Zadanie : " + tasks[t];
			System.out.println(title);
			System.out.println("Wyprzedzenie = " + daysAhead);
			List<StatVO> stat = SignalAPI.predictionStatistic(tasks[t],
					dateStart, dateStop, daysAhead);
			DoubleVector[] apeHour = new DoubleVector[24];
			DoubleVector[] peHour = new DoubleVector[24];
			for (int i = 0; i < 24; ++i) {
				apeHour[i] = new DoubleVector();
				peHour[i] = new DoubleVector();
			}
			for (StatVO vals : stat) {
				int indx = 0;
				for (Double val : vals.getApe()) {
					apeHour[indx++].add(val);
				}
				indx = 0;
				for (Double val : vals.getPe()) {
					peHour[indx++].add(val);
				}
			}
			for (int i = 0; i < 24; ++i) {
				logger.debug(tasks[t] + " H" + i + " MAPE= "
						+ apeHour[i].mape() + " SD= "
						+ apeHour[i].standartDeviation() + " SD= "
						+ peHour[i].standartDeviation() + " VARIANCJA= "
						+ peHour[i].variance());
			}
		}
	}

}
