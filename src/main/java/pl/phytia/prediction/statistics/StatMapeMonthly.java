package pl.phytia.prediction.statistics;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import pl.phytia.model.enums.EnumForecastType;
import pl.phytia.api.SignalAPI;
import pl.phytia.model.enums.EnumMetaModelType;
import pl.phytia.model.enums.EnumSignalType;
import pl.phytia.model.signals.StatVO;
import pl.phytia.utils.Localization;
import pl.phytia.utils.Tools;

public class StatMapeMonthly {

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
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_MLP,
			 * EnumForecastType.E24_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_MLP,
			 * EnumForecastType.E24_T0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_MLP,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_MLP,
			 * EnumForecastType.E24_T0_H0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_MLP,
			 * EnumForecastType.E24_T0_H0_I0_M24_M168)
			 * 
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF05_R1,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF05_R2,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF05_R3,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF05_R4,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF05_R5,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF05_R6,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF05_R10,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF05_R15,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF05_R20,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF05_R30,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168) ,
			 */
			// PARALLEL_SOM_5x5_RBF05_ ... jest oki.
			/*
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF1_R01,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF1_R025,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF1_R05,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF1_R075,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF1_R1,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF1_R125,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF1_R15,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF1_R175,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF1_R2,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF1_R3,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF1_R4,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF1_R5,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF1_R6,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF1_R10,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF1_R15,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF1_R20,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF1_R30,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168)/*,
			 * //PARALLEL_SOM_5x5_RB1_.. jest oki.
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF3_R01,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF3_R025,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF3_R05,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF3_R075,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF3_R1,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168), //
			 * PARALLEL_SOM_5x5_RBF3_.. jest oki.
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF5_R01,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF5_R025,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF5_R05,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF5_R075,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF5_R1,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168), //
			 * PARALLEL_SOM_5x5_RBF5_.. jest oki.
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF10_R01,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF10_R025,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF10_R05,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF10_R1,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * 
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF25_R01,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF25_R025,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF25_R05,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168),
			 * Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF25_R1,
			 * EnumForecastType.E24_T0_I0_D_H_M24_M168)
			 */

			Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF1_R5,
					EnumForecastType.E24_T0_I0_D_H_M24_M168),
			Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF1_R5,
					EnumForecastType.E24_D_H_M24_M168),
			Tools.getForecastName(
					EnumMetaModelType.PARALLEL_SOM_5x5_RBF1_R5_TA,
					EnumForecastType.E24_D_H_M24_M168),
			Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF1_R5,
					EnumForecastType.E24_T0_D_H_M24_M168),
			Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF1_R5,
					EnumForecastType.E24_T0_H0_I0_D_H_M24_M168),
			Tools.getForecastName(EnumMetaModelType.PARALLEL_SOM_5x5_RBF1_R5,
					EnumForecastType.E24_T0_H0_I0_M24_M168)

	};

	public static void main(String[] args) throws IOException {
		final int daysAhead = 2;
		java.util.Date dateStart = null;
		java.util.Date dateStop = null;
		try {
			dateStart = Localization.plDateFormatMedium.parse("2004-09-01");
			dateStop = Localization.plDateFormatMedium.parse("2004-09-30");
		} catch (ParseException e) {

			e.printStackTrace();
		}
		HSSFWorkbook wb = new HSSFWorkbook();
		for (int i = 1; i <= daysAhead; ++i) {
			TreeMap<String, List<StatVO>> stats = new TreeMap<String, List<StatVO>>();
			String sheetTitle = "Wyprzedzenie prognozy = " + i;
			System.out.println(sheetTitle);
			for (int t = 0; t < tasks.length; ++t) {
				System.out.println(tasks[t]);
				List<StatVO> statMonthly = SignalAPI.predictionMonthlyStat(
						tasks[t], EnumSignalType.MODELLING_DATA_NORMALIZED
								.toString(), dateStart, dateStop, i);
				for (StatVO s : statMonthly) {
					System.out.println((s.getYear() == 0 ? "Miesiąc: "
							+ s.getMonth() : "Rok: " + s.getYear())
							+ " MAPE = "
							+ s.getMape()
							+ " Max APE = "
							+ s.getMaxApe());
				}
				stats.put((t + 1) + "." + tasks[t], statMonthly);
			}
			saveToExcel(stats, wb, sheetTitle);
		}
		FileOutputStream fileOut = new FileOutputStream("/home/jprotas/"
				+ "StatMapeMonthly" + ".xls");
		wb.write(fileOut);
		fileOut.close();

	}

	private static void saveToExcel(TreeMap<String, List<StatVO>> stats,
			HSSFWorkbook wb, String sheetTitle) throws IOException {

		HSSFSheet sheet = wb.createSheet(sheetTitle);
		short rowIndx = 0;
		// tytuł tabeli
		HSSFRow row = sheet.createRow(++rowIndx);
		row.createCell((short) 0).setCellValue("MAPEavg");
		Iterator it = stats.keySet().iterator();
		String model = null;
		boolean firstRow = true;
		while (it.hasNext()) {
			model = (String) it.next();
			List<StatVO> statsRow = stats.get(model);
			if (firstRow) {
				row = sheet.createRow(++rowIndx);
				short indx = 0;
				row.createCell(indx++).setCellValue("Model");
				for (StatVO st : statsRow) {
					if (st.getYear() == 0) {
						row.createCell(indx++).setCellValue(st.getMonth());
					} else {
						row.createCell(indx++).setCellValue(st.getYear());
					}
				}
				firstRow = false;
			}
			row = sheet.createRow(++rowIndx);
			short indx = 0;
			row.createCell(indx++).setCellValue(model);
			for (StatVO st : statsRow) {
				row.createCell(indx++).setCellValue(st.getMape());
			}
		}

		row = sheet.createRow(++rowIndx);
		row.createCell((short) 0).setCellValue("MAPEmax");
		it = stats.keySet().iterator();
		model = null;
		firstRow = true;
		while (it.hasNext()) {
			model = (String) it.next();
			List<StatVO> statsRow = stats.get(model);
			if (firstRow) {
				row = sheet.createRow(++rowIndx);
				short indx = 0;
				row.createCell(indx++).setCellValue("Model");
				for (StatVO st : statsRow) {
					if (st.getYear() == 0) {
						row.createCell(indx++).setCellValue(st.getMonth());
					} else {
						row.createCell(indx++).setCellValue(st.getYear());
					}
				}
				firstRow = false;
			}
			row = sheet.createRow(++rowIndx);
			short indx = 0;
			row.createCell(indx++).setCellValue(model);
			for (StatVO st : statsRow) {
				row.createCell(indx++).setCellValue(st.getMaxApe());
			}
		}
	}
}
