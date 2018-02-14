package pl.phytia.utils;

import pl.phytia.model.enums.EnumForecastType;
import pl.phytia.model.enums.EnumMetaModelType;
import pl.phytia.model.enums.EnumSignalType;
import pl.phytia.model.sets.DoubleVector;
import pl.phytia.model.conf.pred.AnnModelConfiguration;

/**
 * Klasa zawierająca różne metody narzędziowe.
 * 
 * @author Jarosław Protasiewicz
 */
public class Tools {

	/**
	 * Buduje wektor wejściowy dla sieci neuronowej podczas procesu
	 * prognozowania. Jest to realizacja zawracania wyjścia na wejście sieci.
	 * 
	 * @param p
	 *            Wektor zawierający wektor energii do przesunięcia. Do wektora
	 *            wyjściowego wstawiane są wartości od <code>1</code> do
	 *            <code>conf.getInputEnergyDelay()</code>
	 * @param w
	 *            Wartość prognozowana wstawiana w indeksie <code>0</code>
	 *            wektora wynikowego.
	 * @param t
	 *            Wektor, z którego brane są czynnki metod do wektora
	 *            wyjsciowego
	 * @param conf
	 *            Konfiguracja modelu.
	 * @return Wektor (wzorzec) wejściowy do sieci neuronowej.
	 */
	public static DoubleVector shift(DoubleVector p, double w, DoubleVector t,
									 AnnModelConfiguration conf) {
		DoubleVector d = new DoubleVector(p.size(), 0.0);
		/*
		 * Przesnięcie wektora energii od indeksu 0 do (inputEnergyDelay - 1)
		 */
		for (int i = 1; i < conf.getInputEnergyDelay(); ++i) {
			d.set(i, p.get(i - 1));
		}
		/*
		 * Na początek wstawiane jest wyjście modelu.
		 */
		d.set(0, w);
		/*
		 * Wstawienie prognozy pogody - temperatura.
		 */
		int indx = 0;
		for (int i = 0; i < conf.getInputTemperatureDelay(); ++i) {
			indx = i + conf.getInputEnergyDelay();
			d.set(indx, t.get(indx));
		}
		/*
		 * Wstawienie prognozy pogody - wilgotność.
		 */
		for (int i = 0; i < conf.getInputHumidityDelay(); ++i) {
			indx = i + conf.getInputEnergyDelay()
					+ conf.getInputTemperatureDelay();
			d.set(indx, t.get(indx));
		}
		/*
		 * Wstawienie prognozy pogody - nasłonecznienie.
		 */
		for (int i = 0; i < conf.getInputInsolationDelay(); ++i) {
			indx = i + conf.getInputEnergyDelay()
					+ conf.getInputTemperatureDelay()
					+ conf.getInputHumidityDelay();
			d.set(indx, t.get(indx));
		}

		/*
		 * Wstawienie prognozy pogody - wstwienie klasy dania.
		 */
		int buf = 0;
		if (conf.isInputDayType()) {
			indx = conf.getInputEnergyDelay() + conf.getInputTemperatureDelay()
					+ conf.getInputHumidityDelay()
					+ conf.getInputInsolationDelay();
			buf++;
			d.set(indx, t.get(indx));
		}

		/*
		 * Wstawienie prognozy pogody - wstwienie klasy święta.
		 */
		if (conf.isInputHoliday()) {
			indx = buf + conf.getInputEnergyDelay()
					+ conf.getInputTemperatureDelay()
					+ conf.getInputHumidityDelay()
					+ conf.getInputInsolationDelay();
			buf++;
			d.set(indx, t.get(indx));
		}

		if (conf.getInputEMean24Delay() == 1) {
			indx = buf + conf.getInputEnergyDelay()
					+ conf.getInputTemperatureDelay()
					+ conf.getInputHumidityDelay()
					+ conf.getInputInsolationDelay();
			buf++;
			d.set(indx, t.get(indx));
		}

		if (conf.getInputEMean168Delay() == 1) {
			indx = buf + conf.getInputEnergyDelay()
					+ conf.getInputTemperatureDelay()
					+ conf.getInputHumidityDelay()
					+ conf.getInputInsolationDelay();
			buf++;
			d.set(indx, t.get(indx));
		}

		return d;
	}

	/**
	 * Generuje nazwę prognozy.
	 * 
	 * @param metamodelType
	 *            Typ metamodelu.
	 * @param forecastType
	 *            Typ prognozy.
	 * @return Nazwa prognozy.
	 */
	public static String getForecastName(EnumMetaModelType metamodelType,
			EnumForecastType forecastType) {
		return metamodelType.toString() + "_"
				+ EnumSignalType.FORECAST.toString() + "_"
				+ forecastType.toString();
	}

	/**
	 * Generuje nazwę prognozy.
	 * 
	 * @param metamodelType
	 *            Typ metamodelu.
	 * @param size
	 *            Rozmiar
	 * @param forecastType
	 *            Typ prognozy.
	 * @return Nazwa prognozy.
	 */
	public static String getForecastName(EnumMetaModelType metamodelType,
			int size, EnumForecastType forecastType) {
		return metamodelType.toString() + size + "_"
				+ EnumSignalType.FORECAST.toString() + "_"
				+ forecastType.toString();
	}
}
