package pl.phytia.prediction.statistics;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import pl.phytia.model.sets.DoubleVector;

public class StatGe {
	final static String srcFileWithPath = "e:\\phd\\cd\\prognoza-ge\\ge-rdn-bledy-PE-APE.xls";

	public static void main(String[] args) throws Exception {

		DoubleVector[] ape = readXLS(srcFileWithPath, "APE");
		String fileName = "GE_RDN_Distribution";
		HSSFWorkbook wb = new HSSFWorkbook();
		double minVal = Double.MAX_VALUE, maxVal = Double.MIN_VALUE;
		for (DoubleVector val : ape) {
			double min = val.min();
			double max = val.max();
			if (min < minVal)
				minVal = min;
			if (max > maxVal)
				maxVal = max;
		}
		for (int i = 0; i < ape.length; ++i) {
			Histogram myHist = new Histogram(0.1, minVal, maxVal);
			myHist.generateDistribution(ape[i]);
			myHist.writeDistrToExcelSeet(wb, "" + i);
		}
		FileOutputStream fileOut = new FileOutputStream(
				"e:\\phd\\cd\\prognoza-ge\\" + fileName + ".xls");
		wb.write(fileOut);
		fileOut.close();

		DoubleVector[] pe = readXLS(srcFileWithPath, "PE");
		fileName = "GE_RDN_Histogram";
		wb = new HSSFWorkbook();
		minVal = Double.MAX_VALUE;
		maxVal = Double.MIN_VALUE;
		for (DoubleVector val : pe) {
			double min = val.min();
			double max = val.max();
			if (min < minVal)
				minVal = min;
			if (max > maxVal)
				maxVal = max;
		}
		for (int i = 0; i < ape.length; ++i) {
			Histogram myHist = new Histogram(0.5, minVal, maxVal);
			myHist.generateHist(pe[i]);
			myHist.writeHistToExcelSeet(wb, "" + i);
		}
		fileOut = new FileOutputStream("e:\\phd\\cd\\prognoza-ge\\" + fileName
				+ ".xls");
		wb.write(fileOut);
		fileOut.close();
	}

	private static DoubleVector[] readXLS(String srcFileWithPath,
			String sheetName) throws Exception {
		DoubleVector[] ape = new DoubleVector[7];
		for (int i = 0; i < ape.length; ++i) {
			ape[i] = new DoubleVector();
		}
		InputStream in = new FileInputStream(srcFileWithPath);
		POIFSFileSystem fs = new POIFSFileSystem(in);
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		HSSFSheet sheet = wb.getSheet(sheetName);
		Iterator rows = sheet.rowIterator();
		int rowIndx = 0;
		while (rows.hasNext()) {
			HSSFRow row = (HSSFRow) rows.next();
			if (rowIndx++ < 2)
				continue;
			Iterator cells = row.cellIterator();
			while (cells.hasNext()) {
				HSSFCell cell = (HSSFCell) cells.next();
				switch (cell.getCellNum()) {
				case 2:
					// if (cell.getNumericCellValue() > 0) {
					ape[0].add(cell.getNumericCellValue());
					// }
					break;
				case 3:
					// if (cell.getNumericCellValue() > 0) {
					ape[1].add(cell.getNumericCellValue());
					// }
					break;
				case 4:
					ape[2].add(cell.getNumericCellValue());
					break;
				case 5:
					ape[3].add(cell.getNumericCellValue());
					break;
				case 6:
					ape[4].add(cell.getNumericCellValue());
					break;
				case 7:
					ape[5].add(cell.getNumericCellValue());
					break;
				case 8:
					ape[6].add(cell.getNumericCellValue());
					break;
				}
			}
		}
		return ape;
	}
}
