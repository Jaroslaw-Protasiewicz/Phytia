package pl.phytia.prediction.statistics;

public class BinVO {

	private double val;

	private int count;

	private double percent;

	private double percentAgr;

	public BinVO(Double val, int count) {
		this.val = val;
		this.count = count;
	}

	public void increment() {
		++count;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getVal() {
		return val;
	}

	public void setVal(double val) {
		this.val = val;
	}

	public double getPercent() {
		return percent;
	}

	public void setPercent(double percent) {
		this.percent = percent;
	}

	public double getPercentAgr() {
		return percentAgr;
	}

	public void setPercentAgr(double percentAgr) {
		this.percentAgr = percentAgr;
	}

}