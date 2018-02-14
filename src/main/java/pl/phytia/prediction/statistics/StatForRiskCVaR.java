package pl.phytia.prediction.statistics;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
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

public class StatForRiskCVaR {

	private static Logger logger = Logger.getLogger(StatForRiskCVaR.class);

	private static double[][] values = new double[][] {
			{ 125077.68, 125736.33, 124454.09, 124995.59, 124148.45, 124297.79,
					124478.08, 124539.97 },
			{ 117471.71, 118468.51, 116493.85, 117046.40, 116785.91, 115946.98,
					116621.09, 117177.12 },
			{ 113673.62, 115088.78, 113772.13, 113799.60, 113448.81, 112343.38,
					113635.41, 113906.95 },
			{ 112176.35, 113947.37, 112258.45, 112195.61, 112073.74, 110773.82,
					112524.30, 112198.46 },
			{ 115315.17, 116773.28, 114834.61, 114798.61, 115633.44, 113936.95,
					115822.57, 115853.83 },
			{ 124495.76, 125089.36, 123852.91, 124091.61, 125026.85, 123905.32,
					124813.09, 125780.85 },
			{ 138889.30, 138755.11, 138386.95, 139011.18, 138912.04, 138781.00,
					138746.59, 139677.40 },
			{ 153579.32, 152797.86, 153062.13, 153591.64, 153097.18, 152973.41,
					153444.82, 153558.48 },
			{ 160828.19, 160132.92, 160601.49, 161271.18, 160836.71, 160640.64,
					161697.75, 161137.62 },
			{ 160673.79, 160513.71, 162021.39, 162869.48, 162559.1, 161806.68,
					163452.34, 162680.66 },
			{ 157778.10, 157808.09, 159879.38, 160908.70, 160729.05, 159033.50,
					161603.03, 160453.09 },
			{ 157088.19, 156344.30, 159300.93, 160064.09, 159571.21, 157319.32,
					160497.92, 159271.25 },
			{ 158070.79, 156490.49, 160510.87, 161355.80, 159829.49, 157982.70,
					160581.59, 159688.67 },
			{ 158656.18, 157796.09, 161594.50, 162994.20, 160901.76, 159829.17,
					161267.39, 160580.01 },
			{ 156461.76, 156566.58, 158120.68, 161021.43, 158033.22, 158696.91,
					157732.00, 157815.67 },
			{ 153344.41, 153745.72, 153057.71, 157410.57, 153767.19, 155123.31,
					152743.86, 153713.83 },
			{ 152125.95, 151895.14, 150340.41, 155242.35, 151669.07, 151867.04,
					150092.24, 151556.03 },
			{ 153052.35, 152454.64, 151987.65, 156006.45, 152865.11, 150944.12,
					151859.78, 152732.50 },
			{ 164301.70, 164155.82, 165103.69, 167569.97, 164960.97, 162784.23,
					166043.66, 165089.48 },
			{ 184118.47, 184460.42, 182465.23, 184993.08, 182572.58, 183194.60,
					184749.32, 183124.50 },
			{ 189976.48, 190149.50, 186012.62, 189148.59, 186322.68, 188572.36,
					188514.26, 186973.74 },
			{ 175849.43, 175442.75, 174458.89, 176051.82, 173953.38, 175143.11,
					176524.17, 174696.33 },
			{ 154894.40, 155132.79, 156597.53, 156037.08, 155252.45, 154597.28,
					158009.85, 156100.54 },
			{ 137873.55, 138943.61, 139689.40, 138793.63, 138344.64, 137314.68,
					140122.92, 139267.56 } };

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

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		final int daysAhead = 2;
		java.util.Date dateStart = null;
		java.util.Date dateStop = null;
		try {
			dateStart = Localization.plDateFormatMedium.parse("2004-06-06");
			dateStop = Localization.plDateFormatMedium.parse("2004-09-15");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		/*
		 * Oblliczenie VaR i CVaR metoda sym historycznej.
		 */
		double[][] VaR = new double[8][24];
		double[][] CVaR = new double[8][24];

		HSSFWorkbook wb = new HSSFWorkbook();
		for (int t = 0; t < tasks.length; ++t) {
			/*
			 * Utowrzenie szeregu APE dla każdej godziny
			 */
			String title = "Zadanie : " + tasks[t];
			System.out.println(title);
			System.out.println("Wyprzedzenie = " + daysAhead);
			List<StatVO> stat = SignalAPI.predictionStatistic(tasks[t],
					dateStart, dateStop, daysAhead);
			DoubleVector[] hApe = saveToExcel(stat, wb, "M" + (t + 1));
			/*
			 * Wyznaczenie szeregów różnic APE, wartości i sortownaie.
			 */
			DoubleVector[] diffHApe = new DoubleVector[24];
			for (int h = 0; h < 24; ++h) {
				diffHApe[h] = new DoubleVector();
				double buff = 0;
				/*
				 * logger.debug(values.length); logger.debug(values[0].length);
				 */
				for (int j = 0; j < hApe[h].size(); ++j) {
					if (j > 0) {
						diffHApe[h].add(((buff - hApe[h].get(j)) / 100)
								* values[h][t]);
					}
					buff = hApe[h].get(j);
				}
				Collections.sort(diffHApe[h]);
				/*
				 * odczyt Var CVaR - 95%
				 */
				double sumCVaR = 0;
				for (int l = 95; l < 100; ++l) {
					sumCVaR += diffHApe[h].get(l);
				}
				VaR[t][h] = diffHApe[h].get(94);
				CVaR[t][h] = sumCVaR / 5;
			}
		}
		FileOutputStream fileOut = new FileOutputStream("/home/jprotas/"
				+ "StatForRiskCVaR" + ".xls");
		wb.write(fileOut);
		fileOut.close();

		saveRiskToExcel(VaR, "VaR95");
		saveRiskToExcel(CVaR, "CVaR95");

	}

	private static DoubleVector[] saveToExcel(List<StatVO> stat,
			HSSFWorkbook wb, String sheetTitle) throws IOException {
		DoubleVector[] wyniki = new DoubleVector[24];
		HSSFSheet sheet = wb.createSheet(sheetTitle);
		short rowIndx = 0;
		short cellIndx = 0;
		HSSFRow row = sheet.createRow(++rowIndx);
		row.createCell(cellIndx++).setCellValue("DZIEŃ");
		for (int i = 0; i < 24; ++i) {
			row.createCell(cellIndx++).setCellValue("H" + (i + 1));
			wyniki[i] = new DoubleVector();
		}
		for (int r = 0; r < stat.size(); ++r) {
			cellIndx = 0;
			row = sheet.createRow(++rowIndx);
			row.createCell(cellIndx++).setCellValue(stat.get(r).getStatDay());
			for (int k = 0; k < 24; ++k) {
				row.createCell(cellIndx++).setCellValue(
						stat.get(r).getApe().get(k));
				wyniki[k].add(stat.get(r).getApe().get(k));
			}

		}
		return wyniki;
	}

	private static void saveRiskToExcel(double[][] risk, String sheetTitle)
			throws IOException {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(sheetTitle);
		short rowIndx = 0;
		short cellIndx = 0;
		HSSFRow row = sheet.createRow(++rowIndx);
		row.createCell(cellIndx++).setCellValue("H");
		for (int i = 0; i < risk.length; ++i) {
			row.createCell(cellIndx++).setCellValue("M" + (i + 1));
		}
		for (int i = 0; i < risk[0].length; ++i) {
			cellIndx = 0;
			row = sheet.createRow(++rowIndx);
			row.createCell(cellIndx++).setCellValue(i + 1);
			for (int k = 0; k < risk.length; ++k) {
				row.createCell(cellIndx++).setCellValue(risk[k][i]);
			}
		}
		FileOutputStream fileOut = new FileOutputStream("/home/jprotas/"
				+ sheetTitle + ".xls");
		wb.write(fileOut);
		fileOut.close();
	}

}
