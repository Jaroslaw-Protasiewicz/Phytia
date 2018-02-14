package pl.phytia.prediction.models;

import pl.phytia.ann.networks.Network;
import pl.phytia.utils.Normalizer;
import pl.phytia.ann.algorithms.Algorithm;
import pl.phytia.model.conf.pred.AnnModelConfiguration;
import pl.phytia.model.sets.SupervisedDataSet;

public class CVMLPModel extends MLPModel {

	public double validation() {

		return 0d;
	}

	@Override
	public void initialize(AnnModelConfiguration conf) {
		setConfig(conf);
		/*
		 * Inicjalizacja normalizatora i normalizacja danych
		 */
		if (conf.isWithNormalization()) {
			if (getNormalizer() == null) {
				setNormalizer(new Normalizer());
			}
			getNormalizer().initialize(getModelSet().min(),
					getModelSet().max(),
					getNetwork().minReturnValue() + getConfig().getNormRatio(),
					getNetwork().maxReturnValue() - getConfig().getNormRatio());
			getModelSet().normalize(getNormalizer());
			getPredictSet().normalize(getNormalizer());
		}
	}

	/**
	 * 
	 */
	public CVMLPModel() {
		super();
	}

	/**
	 * @param config
	 * @param network
	 * @param algorithm
	 * @param secondPhaseAlgorithm
	 * @param modelSet
	 * @param predictSet
	 * @param normalizer
	 * @param modelClass
	 */
	public CVMLPModel(AnnModelConfiguration config, Network network,
			Algorithm algorithm, Algorithm secondPhaseAlgorithm,
			SupervisedDataSet modelSet, SupervisedDataSet predictSet,
			Normalizer normalizer, String modelClass) {
		super(config, network, algorithm, secondPhaseAlgorithm, modelSet,
				predictSet, normalizer, modelClass);

	}

}
