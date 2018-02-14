package pl.phytia.model.conf.pred;

import java.util.Date;

import pl.phytia.model.conf.Configuration;
import pl.phytia.model.enums.EnumForecastType;
import pl.phytia.model.enums.EnumMetaModelType;
import pl.phytia.model.enums.EnumSignalType;

/**
 * Konfiguracja metamodelu.
 * 
 * @author Jarosław Protasiewicz
 */
public class MetaModelConfiguration extends
		Configuration<MetaModelConfiguration> {

	private static final long serialVersionUID = 7613502061022121200L;

	/* ################### FIELDS #################### */
	/**
	 * Typ metamodelu.
	 */
	private EnumMetaModelType metamodelType;

	/**
	 * Typ prognozy.
	 */
	private EnumForecastType forcastType;

	/**
	 * Ilość dni wyprzedzenia prognozy.
	 */
	private int daysAhead;

	/**
	 * Punkt startu prognozy - pierwszy dzień prognozy.
	 */
	private Date predictionStart;

	/**
	 * Punkt końca prognozy - ostatni dzień prognozy.
	 */
	private Date predictionStop;

	/**
	 * Nazwa sygnału wejściowego - używany do modelowania.
	 */
	private EnumSignalType inputSignal;

	/**
	 * Nazwa sygnału wyjściowego - wynik prognozowania.
	 */
	private String outputSignal;

	/* ################### CONSTRUCTORS #################### */
	/**
	 * Konstruktor bezargumentowy.
	 */
	public MetaModelConfiguration() {
		super();
	}

	/**
	 * Konstruktor.
	 * 
	 * @param metamodelType
	 *            Typ metamodelu.
	 * @param forcastType
	 *            Typ prognozy.
	 * @param daysAhead
	 *            Ilość dni wyprzedzenia prognozy.
	 * @param predictionStart
	 *            Punkt startu prognozy - pierwszy dzień prognozy.
	 * @param predictionStop
	 *            Punkt końca prognozy - ostatni dzień prognozy.
	 * @param inputSignal
	 *            Nazwa sygnału wejściowego - używanego do modelowania.
	 * @param outputSignal
	 *            Nazwa sygnału wyjściowego - wynik prognozowania.
	 */
	public MetaModelConfiguration(EnumMetaModelType metamodelType,
			EnumForecastType forcastType, int daysAhead, Date predictionStart,
			Date predictionStop, EnumSignalType inputSignal, String outputSignal) {
		super();
		this.metamodelType = metamodelType;
		this.forcastType = forcastType;
		this.daysAhead = daysAhead;
		this.predictionStart = predictionStart;
		this.predictionStop = predictionStop;
		this.inputSignal = inputSignal;
		this.outputSignal = outputSignal;
	}

	/* ##################### GETTERS AND SETTERS ################### */
	/**
	 * @return wartość pola daysAhead
	 */
	public int getDaysAhead() {
		return daysAhead;
	}

	/**
	 * @param daysAhead
	 *            jest przypisywany do pola daysAhead
	 */
	public void setDaysAhead(int daysAhead) {
		this.daysAhead = daysAhead;
	}

	/**
	 * @return wartość pola inputSignal
	 */
	public EnumSignalType getInputSignal() {
		return inputSignal;
	}

	/**
	 * @param inputSignal
	 *            jest przypisywany do pola inputSignal
	 */
	public void setInputSignal(EnumSignalType inputSignal) {
		this.inputSignal = inputSignal;
	}

	/**
	 * @return wartość pola metamodelType
	 */
	public EnumMetaModelType getMetamodelType() {
		return metamodelType;
	}

	/**
	 * @param metamodelType
	 *            jest przypisywany do pola metamodelType
	 */
	public void setMetamodelType(EnumMetaModelType metamodelType) {
		this.metamodelType = metamodelType;
	}

	/**
	 * @return wartość pola forcastType
	 */
	public EnumForecastType getForcastType() {
		return forcastType;
	}

	/**
	 * @param forcastType
	 *            jest przypisywany do pola forcastType
	 */
	public void setForcastType(EnumForecastType forcastType) {
		this.forcastType = forcastType;
	}

	/**
	 * @return wartość pola outputSignal
	 */
	public String getOutputSignal() {
		return outputSignal;
	}

	/**
	 * @param outputSignal
	 *            jest przypisywany do pola outputSignal
	 */
	public void setOutputSignal(String outputSignal) {
		this.outputSignal = outputSignal;
	}

	/**
	 * @return wartość pola predictionStart
	 */
	public Date getPredictionStart() {
		return predictionStart;
	}

	/**
	 * @param predictionStart
	 *            jest przypisywany do pola predictionStart
	 */
	public void setPredictionStart(Date predictionStart) {
		this.predictionStart = predictionStart;
	}

	/**
	 * @return wartość pola predictionStop
	 */
	public Date getPredictionStop() {
		return predictionStop;
	}

	/**
	 * @param predictionStop
	 *            jest przypisywany do pola predictionStop
	 */
	public void setPredictionStop(Date predictionStop) {
		this.predictionStop = predictionStop;
	}
}
