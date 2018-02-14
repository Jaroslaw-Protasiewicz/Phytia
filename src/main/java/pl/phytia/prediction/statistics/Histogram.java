package pl.phytia.prediction.statistics;

import java.util.Iterator;
import java.util.TreeMap;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import pl.phytia.model.sets.DoubleVector;

/**
 * Klasa budująca histogram
 * 
 * @author Jarosław Protasiewicz
 */
public class Histogram {

	private double binSize;

	private TreeMap<Integer, BinVO> binsHist;

	private TreeMap<Integer, BinVO> binsDistr;

	private double minVal;

	private double maxVal;

	public Histogram(double binSize, double minVal, double maxVal) {
		this.binSize = binSize;
		this.minVal = minVal;
		this.maxVal = maxVal;

	}

	public void generateDistribution(DoubleVector vals) {

		/*
		 * Inicjalizacja histogramu.
		 */
		binsDistr = new TreeMap<Integer, BinVO>();
		double val = 0.0;
		int count = 0;
		do {
			binsDistr.put(new Integer(count), new BinVO(val, 0));
			++count;
			val += binSize;
		} while (val < maxVal);
		binsDistr.put(new Integer(count), new BinVO(val, 0));

		/*
		 * Wyznaczenie kategorii.
		 */
		for (Double v : vals) {
			int cat = (int) Math.round(v / binSize);
			binsDistr.get(cat).increment();
		}

		/*
		 * Wyznaczenie procentowego udziału kategorii w populacji
		 */
		Iterator it = binsDistr.keySet().iterator();
		BinVO bin = null;
		Integer key = null;
		double agr = 0.0;
		while (it.hasNext()) {
			key = (Integer) it.next();
			bin = binsDistr.get(key);
			bin.setPercent((bin.getCount() / (double) vals.size()) * 100);
			agr += bin.getPercent();
			bin.setPercentAgr(agr);
		}
	}

	public void generateHist(DoubleVector vals) {

		/*
		 * Inicjalizacja histogramu.
		 */
		binsHist = new TreeMap<Integer, BinVO>();
		double val = 0.0;
		int count = 0;
		do {
			binsHist.put(new Integer(count), new BinVO(val, 0));
			++count;
			val += binSize;
		} while (val < maxVal);
		binsHist.put(new Integer(count), new BinVO(val, 0));
		val = 0.0;
		count = 0;
		do {
			--count;
			val -= binSize;
			binsHist.put(new Integer(count), new BinVO(val, 0));
		} while (val > minVal);

		/*
		 * Wyznaczenie kategorii.
		 */
		for (Double v : vals) {
			int cat = (int) Math.round(v / binSize);
			binsHist.get(cat).increment();
		}

		/*
		 * Wyznaczenie procentowego udziału kategorii w polulacji
		 */
		Iterator it = binsHist.keySet().iterator();
		BinVO bin = null;
		Integer key = null;
		while (it.hasNext()) {
			key = (Integer) it.next();
			bin = binsHist.get(key);
			bin.setPercent((bin.getCount() / (double) vals.size()) * 100);
			// /bins.put(key, bin);
		}
	}

	public void writeDistrToExcelSeet(HSSFWorkbook wb, String sheetTitle) {
		HSSFSheet sheet = wb.createSheet(sheetTitle);
		short rowIndx = 0, cellIndx = 0;
		HSSFRow row = sheet.createRow(++rowIndx);
		row.createCell(cellIndx++).setCellValue("APE");
		row.createCell(cellIndx++).setCellValue("Agr%");
		row.createCell(cellIndx++).setCellValue("%");
		row.createCell(cellIndx++).setCellValue("COUNT");
		Iterator it = binsDistr.keySet().iterator();
		BinVO bin = null;
		Integer key = null;
		while (it.hasNext()) {
			cellIndx = 0;
			key = (Integer) it.next();
			bin = binsDistr.get(key);
			row = sheet.createRow(++rowIndx);
			row.createCell(cellIndx++).setCellValue(bin.getVal());
			row.createCell(cellIndx++).setCellValue(bin.getPercentAgr());
			row.createCell(cellIndx++).setCellValue(bin.getPercent());
			row.createCell(cellIndx++).setCellValue(bin.getCount());
		}
	}

	public short writeDistrToExcelSeet(HSSFSheet sheet, short cellIndx) {
		short rowIndx = 0;
		HSSFRow row = sheet.createRow(++rowIndx);
		row.createCell(cellIndx++).setCellValue("APE");
		row.createCell(cellIndx++).setCellValue("Agr%");
		row.createCell(cellIndx++).setCellValue("%");
		row.createCell(cellIndx++).setCellValue("COUNT");
		Iterator it = binsDistr.keySet().iterator();
		BinVO bin = null;
		Integer key = null;
		while (it.hasNext()) {
			cellIndx = 0;
			key = (Integer) it.next();
			bin = binsDistr.get(key);
			row = sheet.createRow(++rowIndx);
			row.createCell(cellIndx++).setCellValue(bin.getVal());
			row.createCell(cellIndx++).setCellValue(bin.getPercentAgr());
			row.createCell(cellIndx++).setCellValue(bin.getPercent());
			row.createCell(cellIndx++).setCellValue(bin.getCount());
		}
		return cellIndx;
	}

	public void writeHistToExcelSeet(HSSFWorkbook wb, String sheetTitle) {
		HSSFSheet sheet = wb.createSheet(sheetTitle);
		short rowIndx = 0, cellIndx = 0;
		HSSFRow row = sheet.createRow(++rowIndx);
		row.createCell(cellIndx++).setCellValue("PE");
		row.createCell(cellIndx++).setCellValue("%");
		row.createCell(cellIndx++).setCellValue("COUNT");
		Iterator it = binsHist.keySet().iterator();
		BinVO bin = null;
		Integer key = null;
		while (it.hasNext()) {
			cellIndx = 0;
			key = (Integer) it.next();
			bin = binsHist.get(key);
			row = sheet.createRow(++rowIndx);
			row.createCell(cellIndx++).setCellValue(bin.getVal());
			row.createCell(cellIndx++).setCellValue(bin.getPercent());
			row.createCell(cellIndx++).setCellValue(bin.getCount());
		}
	}

	public short writeHistToExcelSeet(HSSFSheet sheet, short cellIndx) {
		short rowIndx = 0;
		HSSFRow row = sheet.createRow(++rowIndx);
		row.createCell(cellIndx++).setCellValue("PE");
		row.createCell(cellIndx++).setCellValue("%");
		row.createCell(cellIndx++).setCellValue("COUNT");
		Iterator it = binsHist.keySet().iterator();
		BinVO bin = null;
		Integer key = null;
		while (it.hasNext()) {
			cellIndx = 0;
			key = (Integer) it.next();
			bin = binsHist.get(key);
			row = sheet.createRow(++rowIndx);
			row.createCell(cellIndx++).setCellValue(bin.getVal());
			row.createCell(cellIndx++).setCellValue(bin.getPercent());
			row.createCell(cellIndx++).setCellValue(bin.getCount());
		}
		return cellIndx;
	}
}
