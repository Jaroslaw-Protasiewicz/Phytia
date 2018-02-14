package pl.phytia.prediction.metaModels;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import pl.phytia.prediction.crossValidation.MLPArchitectureTask;
import pl.phytia.prediction.models.CVMLPModel;
import pl.phytia.utils.Localization;

public abstract class CvMetaModel extends MetaModel<CVMLPModel> {

	protected static Logger logger = Logger
			.getLogger(MLPArchitectureTask.class);

	/**
	 * Dni testu w jako data.
	 */
	protected Date[] taksDays;

	/**
	 * Dni na które jest wykonywana prognoza.
	 */
	protected final static String[] taskDays = new String[] { "2004-01-28",
			"2004-02-11", "2004-03-17", "2004-04-14", "2004-05-12",
			"2004-06-16", "2004-07-14", "2004-08-18", "2004-09-15",
			"2004-10-13", "2004-11-17", "2004-12-15", "2004-01-25",
			"2002-02-22", "2004-04-18", "2004-05-01", "2004-05-03",
			"2004-06-20", "2004-07-18", "2004-08-15", "2004-09-19",
			"2004-11-01", "2004-12-24", "2004-12-25" };

	public List<String> crossValidationErrors() {
		double error = 0.0;
		List<String> ret = new ArrayList<String>();
		ret.add("DAY\tCLASS\tMAPE\t");
		double maxError = Double.MIN_VALUE;
		double locSrdError = 0;
		int i = 0, j = 0, l = 0;
		Date temp = getPredictors().get(0).getPredictionDay();
		for (CVMLPModel model : getPredictors()) {
			if (temp.equals(model.getPredictionDay())) {
				locSrdError += model.getMapeError();
				++i;
			} else {
				ret.add(Localization.plDateFormatMedium.format(temp)
						+ "\tMAPE_LOC_AVG\t" + (locSrdError / i));
				error += (locSrdError / i);
				locSrdError = model.getMapeError();
				i = 1;
				++l;
			}
			temp = model.getPredictionDay();
			ret.add(Localization.plDateFormatMedium.format(model
					.getPredictionDay())
					+ "\t"
					+ model.getModelClass()
					+ "\t"
					+ model.getMapeError());
			if (++j == getPredictors().size()) {
				ret.add(Localization.plDateFormatMedium.format(temp)
						+ "\tMAPE_LOC_AVG\t" + (locSrdError / i));
				error += (locSrdError / i);
				++l;
			}
			if (model.getMapeError() > maxError) {
				maxError = model.getMapeError();
			}

		}
		ret.add("SUMMATION\tMAPE_AVG\t" + (error / l));
		ret.add("SUMMATION\tMAPE_MAX\t" + maxError);
		return ret;
	}

	@Override
	public void prediction() {
		for (CVMLPModel model : getPredictors()) {
			model.modeling();
			model.execution();
		}
	}

	public void prepareTestDays(String[] dates) {
		taksDays = new Date[dates.length];
		try {
			for (int i = 0; i < dates.length; ++i) {
				taksDays[i] = Localization.plDateFormatMedium.parse(dates[i]);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return wartość pola taksDays
	 */
	public Date[] getTaksDays() {
		return taksDays;
	}

	/**
	 * @param taksDays
	 *            jest przypisywany do pola taksDays
	 */
	public void setTaksDays(Date[] taksDays) {
		this.taksDays = taksDays;
	}

	/*
	 * public abstract CVMLPModel[] prepareModels(int size);
	 * 
	 * public abstract AnnModelConfiguration[] prepareModelConfigurations(int
	 * size);
	 * 
	 * public abstract Algorithm[] prepareFirstPhaseAlgorithms(int size);
	 * 
	 * public abstract Algorithm[] prepareSecondPhaseAlgorithms(int size);
	 * 
	 * public abstract Network[] prepareNetworks(AnnModelConfiguration conf[]);
	 * 
	 * public abstract Date[] prepareTestDays(String[] dates);
	 */

}
