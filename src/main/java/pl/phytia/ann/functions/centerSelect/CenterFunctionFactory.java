package pl.phytia.ann.functions.centerSelect;

import pl.phytia.model.conf.functions.centerSelect.CenterSelFunctionConfiguration;
import pl.phytia.model.enums.EnumCenterSelectType;

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
		case EnumCenterSelectType.K_MEAN_CENTER_SELECT:
			centerFunction = new KMeanCenterFunction(conf);
			break;
		case EnumCenterSelectType.RANDOM_CENTER_SELECT:
			centerFunction = new RandomCenterFunction(conf);
			break;
		case EnumCenterSelectType.STRICT_CENTER_SELECT:
			centerFunction = new StrictCenterFunction(conf);
			break;
		default:
			centerFunction = new KMeanCenterFunction(conf);
			break;
		}
		return centerFunction;
	}
}
