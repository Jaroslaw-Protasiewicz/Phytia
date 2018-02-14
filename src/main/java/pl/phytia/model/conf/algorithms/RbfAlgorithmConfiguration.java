package pl.phytia.model.conf.algorithms;

import pl.phytia.model.conf.functions.centerSelect.CenterSelFunctionConfiguration;
import pl.phytia.model.enums.EnumFunctionType;

/**
 * Konfiguracja alogrytmu nauki sieci RBF.
 * 
 * @author Jarosław Protasiewicz
 */
public class RbfAlgorithmConfiguration extends
		AlgorithmConfiguration<RbfAlgorithmConfiguration> {

	private static final long serialVersionUID = 1828297481408616479L;

	public RbfAlgorithmConfiguration() {
		super();
		centerSelFunctionConf = new CenterSelFunctionConfiguration();
		setNumerOfRepeat(1);
		setErrorFunctionType(EnumFunctionType.SSE);
		setStopCondFunctionType(EnumFunctionType.MAPE);
		setMaxIteration(100);
		setMaxNotFoundBetterSolution(10);
		setMinError(1);
		weightsAdaptation = true;
		centersAdaptation = true;
		widthAdaptation = true;
		weightsLearnigRatio = 0.1;
		centersLearningRatio = 0.1;
		widthLearnigRatio = 0.1;
	}

	/**
	 * Konfiguracja funkcji doboru centrów.
	 */
	private CenterSelFunctionConfiguration centerSelFunctionConf;

	/**
	 * Parametr decydujący czy wagi sieci podlegają adaptacji podczas nauczania
	 * nadzorowanego.
	 * <li><code>true</code> - podlegają adaptacji,</li>
	 * <li><code>false</code> - nie podlegają adaptacji.</li>
	 * <br>
	 * <br>
	 */
	private boolean weightsAdaptation;

	/**
	 * Parametr decydujący czy centra funkcji bazowych podlegają adaptacji
	 * podczas nauczania nadzorowanego.
	 * <li><code>true</code> - podlegają adaptacji,</li>
	 * <li><code>false</code> - nie podlegają adaptacji.</li>
	 * <br>
	 * <br>
	 */
	private boolean centersAdaptation;

	/**
	 * Parametr decydujący czy rozmycie funkcji bazowych podlega adaptacji
	 * podczas nauczania nadzorowanego.
	 * <li><code>true</code> - podlega adaptacji,</li>
	 * <li><code>false</code> - nie podlega adaptacji.</li>
	 * <br>
	 * <br>
	 */
	private boolean widthAdaptation;

	/**
	 * Współczynnik uczenia wag dla algorytmu nadzorowanego (online i offline)
	 */
	private double weightsLearnigRatio;

	/**
	 * Współczynnik uczenia centrów funkcji bazowych dla algorytmu nadzorowanego
	 * (online i offline)
	 */
	private double centersLearningRatio;

	/**
	 * Współczynnik uczenia rozmycia funkcji bazowych dla algorytmu
	 * nadzorowanego (online i offline)
	 */
	private double widthLearnigRatio;

	public CenterSelFunctionConfiguration getCenterSelFunctionConf() {
		return centerSelFunctionConf;
	}

	public void setCenterSelFunctionConf(
			CenterSelFunctionConfiguration centerSelFunctionConf) {
		this.centerSelFunctionConf = centerSelFunctionConf;
	}

	public boolean isCentersAdaptation() {
		return centersAdaptation;
	}

	public void setCentersAdaptation(boolean centersAdaptation) {
		this.centersAdaptation = centersAdaptation;
	}

	public boolean isWeightsAdaptation() {
		return weightsAdaptation;
	}

	public void setWeightsAdaptation(boolean weightsAdaptation) {
		this.weightsAdaptation = weightsAdaptation;
	}

	public boolean isWidthAdaptation() {
		return widthAdaptation;
	}

	public void setWidthAdaptation(boolean widthAdaptation) {
		this.widthAdaptation = widthAdaptation;
	}

	public double getCentersLearningRatio() {
		return centersLearningRatio;
	}

	public void setCentersLearningRatio(double centersLearningRatio) {
		this.centersLearningRatio = centersLearningRatio;
	}

	public double getWeightsLearnigRatio() {
		return weightsLearnigRatio;
	}

	public void setWeightsLearnigRatio(double weightsLearnigRatio) {
		this.weightsLearnigRatio = weightsLearnigRatio;
	}

	public double getWidthLearnigRatio() {
		return widthLearnigRatio;
	}

	public void setWidthLearnigRatio(double widthLearnigRatio) {
		this.widthLearnigRatio = widthLearnigRatio;
	}

}
