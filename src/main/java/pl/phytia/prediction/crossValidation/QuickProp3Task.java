package pl.phytia.prediction.crossValidation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

import pl.phytia.ann.algorithms.Algorithm;
import pl.phytia.ann.networks.MLPNetwork;
import pl.phytia.model.conf.algorithms.BackPropConfiguration;
import pl.phytia.model.enums.EnumFunctionType;
import pl.phytia.ann.algorithms.bp.QuickPropAlgorithm;
import pl.phytia.model.enums.EnumLearningRatioType;
import pl.phytia.model.sets.SupervisedDataSet;

public class QuickProp3Task extends QuickProp2Task {

	public static void doIt() throws Exception {
		String kom = null;
		String path = "E:\\home\\phd\\QuickProp\\";

		QuickProp3Task task = new QuickProp3Task();
		task.prepareTestDays(taskDays);
		double maximumGrowthRatio[] = new double[] { 0.001, 0.01, 0.1, 0.5,
				1.0, 1.5, 1.75, 2, 5 };

		for (int k = 7; k < maximumGrowthRatio.length; ++k) {
			StringBuilder fileName = new StringBuilder("QuickProp3Task");
			if (k < 10) {
				fileName.append("-0" + k + ".txt");
			} else {
				fileName.append("-" + k + ".txt");
			}
			BufferedWriter out = new BufferedWriter(new FileWriter(path
					+ fileName.toString()));
			task.prepareModels(maximumGrowthRatio[k]);
			out
					.write("\n-----------------------------------------------------------\n");
			kom = "Test dla algorytmu:\n"
					+ task.getPredictors().get(0).getAlgorithm().getConfig()
							.toString();
			System.out.println(kom);
			out.write(kom + "\n");
			out
					.write("-----------------------------------------------------------\n");
			task.prediction();
			List<String> resultTxt = task.crossValidationErrors();
			for (String res : resultTxt) {
				System.out.println(res);
				out.write(" " + res + "\n");
			}
			out.close();
		}

	}

	protected Algorithm prepareFirstPhaseMLPTrainAlgorithm(
			double maximumGrowthRatio) {
		BackPropConfiguration conf = new BackPropConfiguration(
				EnumLearningRatioType.CONSTANT, EnumFunctionType.SSE,
				EnumFunctionType.MAPE);
		conf.setMaximumGrowthRatio(maximumGrowthRatio);
		conf.setMomentumRatio(0.3);
		conf.setLearningRatio(0.1);
		conf.setWeightRatio(-1.0e-9);
		conf.setMaxIteration(500);
		conf.setMaxNotFoundBetterSolution(500);
		conf.setMinError(1.0);
		Algorithm<QuickPropAlgorithm, BackPropConfiguration, SupervisedDataSet, MLPNetwork> alg = new QuickPropAlgorithm(
				conf);
		alg.initialize(conf);
		return alg;
	}
}
