package pl.phytia.ann.functions.centerSelect;

import pl.phytia.model.conf.functions.centerSelect.CenterSelFunctionConfiguration;
import pl.phytia.model.enums.EnumCenterSelectType;

import static pl.phytia.model.enums.EnumCenterSelectType.*;
import static pl.phytia.model.enums.EnumCenterSelectType.K_MEAN_CENTER_SELECT;

/**
 * Prosta fabryka funkcji doboru centrów.
 * 
 * @author Jarosław Protasiewicz
 */
public class CenterFunctionFactory {

	/**
	 * Toworzy funkcję doboru centrów na podstawie jej konfiguracji.
	 * 
	 * @param conf
	 *            Konfiguracja.
	 * @return Funkcja doboru centrów sieci RBF.
	 */
	public static CenterSelectFunction getCenterFunction(
			CenterSelFunctionConfiguration conf) {
		CenterSelectFunction centerFunction = null;
		switch (conf.getCenterSelectType()) {
		case K_MEAN_CENTER_SELECT:
			centerFunction = new KMeanCenterFunction(conf);
			break;
		case RANDOM_CENTER_SELECT:
			centerFunction = new RandomCenterFunction(conf);
			break;
		case STRICT_CENTER_SELECT:
			centerFunction = new StrictCenterFunction(conf);
			break;
		default:
			centerFunction = new KMeanCenterFunction(conf);
			break;
		}
		return centerFunction;
	}
}
