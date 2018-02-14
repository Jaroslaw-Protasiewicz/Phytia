package pl.phytia.prediction.statistics;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import pl.phytia.model.enums.EnumForecastType;
import pl.phytia.api.SignalAPI;
import pl.phytia.model.enums.EnumMetaModelType;
import pl.phytia.model.sets.DoubleVector;
import pl.phytia.model.signals.StatVO;
import pl.phytia.utils.Localization;
import pl.phytia.utils.Tools;

public class Stat {

	private static final String[] tasks = {
			/*
			 * Tools.getForecastName(EnumMetaModelType.SERIAL_MLP,
			 * EnumForecastType.E24),
			 * Tools.getForecastName(EnumMetaModelType.SERIAL_MLP,
			 * EnumForecastType.E24_T0),
			 * Tools.getForecastName(EnumMetaModelType.SERIAL_MLP,
			 * EnumForecastType.E24_I0),
			 * Tools.getForecastName(EnumMetaModelType.SERIAL_MLP,
			 * EnumForecastType.E24_H0),
			 * Tools.getForecastName(EnumMetaModelType.SERIAL_MLP,
			 * EnumForecastType.E24_T0_I0),
			 * Tools.getForecastName(EnumMetaModelType.SERIAL_MLP,
			 * EnumForecastType.E24_T0_H0),
			 * Tools.getForecastName(EnumMetaModelType.SERIAL_MLP,
			 * EnumForecastType.E24_H0_I0),
			 * Tools.getForecastName(EnumMetaModelType.SERIAL_MLP,
			 * EnumForecastType.E24_T0_H0_I0)
			 */
			/*
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_4x4_MLP,
			 * EnumForecastType.E24_T0_H0_I0_D_H),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_MLP,
			 * EnumForecastType.E24_T0_H0_I0_D_H),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_7x7_MLP,
			 * EnumForecastType.E24_T0_H0_I0_D_H),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_10x10_MLP,
			 * EnumForecastType.E24_T0_H0_I0_D_H),
			 */
			/*
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF25_R01,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF25_R025,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF25_R05,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * 
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF10_R01,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF10_R025,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF10_R05,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * 
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF5_R01,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF5_R025,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF5_R05,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF5_R075,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF5_R1,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * 
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF3_R01,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF3_R025,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF3_R05,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF3_R075,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 */
			Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_MLP,
					EnumForecastType.E24_D_H_M24_M168),
			Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_MLP,
					EnumForecastType.E24_T0_D_H_M24_M168),
			Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_MLP,
					EnumForecastType.E24_T0_I0_D_H_M24_M168),
			Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_MLP,
					EnumForecastType.E24_T0_H0_I0_D_H_M24_M168),
			Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_MLP,
					EnumForecastType.E24_T0_H0_I0_M24_M168),
			Tools.getForecastName(EnumMetaModelType.SERIAL_MLP,
					EnumForecastType.E24_T0),
			Tools.getForecastName(EnumMetaModelType.SERIAL_MLP,
					EnumForecastType.E24_T0_I0), };

	public static void main(String[] args) throws Exception {
		final int daysAhead = 2;
		java.util.Date dateStart = null;
		java.util.Date dateStop = null;
		try {
			dateStart = Localization.plDateFormatMedium.parse("2004-09-01");
			dateStop = Localization.plDateFormatMedium.parse("2004-09-30");
		} catch (ParseException e) {

			e.printStackTrace();
		}

		/*
		 * Wyznaczenie min i max
		 */
		double minVal = Double.MAX_VALUE;
		double maxVal = Double.MIN_VALUE;
		for (int t = 0; t < tasks.length; ++t) {
			List<List<StatVO>> stats = new ArrayList<List<StatVO>>(daysAhead);
			String title = "Zadanie : " + tasks[t];
			System.out.println(title);
			for (int i = 1; i <= daysAhead; ++i) {

				System.out.println("Wyprzedzenie = " + i);

				List<StatVO> stat = SignalAPI.predictionStatistic(tasks[t],
						dateStart, dateStop, i);
				stats.add(stat);
			}
			DoubleVector vals = new DoubleVector();
			for (int s = 0; s < stats.size(); ++s) {
				List<StatVO> stat = (List<StatVO>) stats.get(s);
				for (StatVO st : stat) {
					vals.addAll(st.getPe());
				}
			}
			double min = vals.min();
			double max = vals.max();
			if (min < minVal)
				minVal = min;
			if (max > maxVal)
				maxVal = max;
		}

		/*
		 * Wykonanie statysytyk.
		 */
		for (int t = 0; t < tasks.length; ++t) {
			List<List<StatVO>> stats = new ArrayList<List<StatVO>>(daysAhead);
			String title = "Zadanie : " + tasks[t];
			System.out.println(title);
			for (int i = 1; i <= daysAhead; ++i) {
				System.out.println("Wyprzedzenie = " + i);
				List<StatVO> stat = SignalAPI.predictionStatistic(tasks[t],
						dateStart, dateStop, i);
				stats.add(stat);
			}
			histogram(stats, tasks[t], minVal, maxVal);
			// distribution(stats, tasks[t], minVal, maxVal);
			saveToExcel(stats, tasks[t]);
		}
	}

	private static void histogram(List<List<StatVO>> stats, String taskName,
			double minVal, double maxVal) throws Exception {
		final double binSize = 0.5;
		String fileName = "H_" + taskName;
		HSSFWorkbook wb = new HSSFWorkbook();
		for (int s = 0; s < stats.size(); ++s) {
			List<StatVO> stat = (List<StatVO>) stats.get(s);
			DoubleVector pe = new DoubleVector();
			for (StatVO st : stat) {
				pe.addAll(st.getPe());
			}
			Histogram myHist = new Histogram(binSize, minVal, maxVal);
			myHist.generateHist(pe);
			myHist.writeHistToExcelSeet(wb, "AEAHD_" + (s + 1));
		}
		FileOutputStream fileOut = new FileOutputStream("/home/jprotas/tmp/"
				+ fileName + ".xls");
		wb.write(fileOut);
		fileOut.close();
	}

	private static void distribution(List<List<StatVO>> stats, String taskName,
			double minVal, double maxVal) throws Exception {
		final double binSize = 0.1;
		String fileName = "D_" + taskName;
		HSSFWorkbook wb = new HSSFWorkbook();
		for (int s = 0; s < stats.size(); ++s) {
			List<StatVO> stat = (List<StatVO>) stats.get(s);
			DoubleVector ape = new DoubleVector();
			for (StatVO st : stat) {
				ape.addAll(st.getApe());
			}
			Histogram myHist = new Histogram(binSize, minVal, maxVal);
			myHist.generateDistribution(ape);
			myHist.writeDistrToExcelSeet(wb, "AEAHD_" + (s + 1));
		}
		FileOutputStream fileOut = new FileOutputStream("/home/jprotas/tmp/"
				+ fileName + ".xls");
		wb.write(fileOut);
		fileOut.close();
	}

	private static void saveToExcel(List<List<StatVO>> stats, String taskName)
			throws IOException {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("Statystyka");
		short rowIndx = 0;
		DoubleVector[] globalMape = new DoubleVector[stats.size()];
		/*
		 * Nagłówki.
		 */
		HSSFRow row = sheet.createRow(++rowIndx);
		row.createCell((short) 2).setCellValue("Zadanie : " + taskName);
		row = sheet.createRow(++rowIndx);
		row.createCell((short) 1).setCellValue("Dzien");
		for (int i = 1; i <= stats.size(); ++i) {
			row.createCell((short) (i * 2)).setCellValue("w = ");
			row.createCell((short) (1 + (i * 2))).setCellValue("" + i);
			globalMape[i - 1] = new DoubleVector();
		}

		for (int i = 0; i < stats.get(0).size(); ++i) {
			row = sheet.createRow(++rowIndx);
			row.createCell((short) (1)).setCellValue(
					""
							+ ((StatVO) ((List<StatVO>) stats.get(0)).get(i))
									.getStatDay());
			for (int j = 1; j <= stats.size(); ++j) {
				List<StatVO> stat = stats.get(j - 1);
				row.createCell((short) (j * 2)).setCellValue(
						"" + ((StatVO) stat.get(i)).getMape());
				row.createCell((short) (1 + (j * 2))).setCellValue(
						"" + ((StatVO) stat.get(i)).getApe().max());
				globalMape[j - 1].addAll(((StatVO) stat.get(i)).getApe());
			}
		}
		row = sheet.createRow(++rowIndx);
		row.createCell((short) 1).setCellValue("Srd.");
		for (int k = 1; k <= globalMape.length; ++k) {
			row.createCell((short) (k * 2)).setCellValue(
					globalMape[k - 1].mape());
		}
		FileOutputStream fileOut = new FileOutputStream("/home/jprotas/tmp/"
				+ taskName + ".xls");
		wb.write(fileOut);
		fileOut.close();
	}
}
