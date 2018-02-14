package pl.phytia.model.enums;

/**
 * Rodzaje prognoz.
 * 
 * @author Jarosław Protasiewicz
 */
public enum EnumForecastType {

	/**
	 * Prognoza wykonana w oparciu o:
	 * <li> szereg energii E(t-1), E(t-2), ..., E(t-24).</li>
	 */
	E24, E24_D_H_M24_M168,

	/**
	 * Prognoza wykonana w oparciu o:
	 * <li> szereg energii E(t-1), E(t-2), ..., E(t-24); </li>
	 * <li> szereg temperatury T(t).</li>
	 */
	E24_T0,

	/**
	 * Prognoza wykonana w oparciu o:
	 * <li> szereg energii E(t-1), E(t-2), ..., E(t-24); </li>
	 * <li> szereg wilgotności H(t).</li>
	 */
	E24_H0,

	/**
	 * Prognoza wykonana w oparciu o:
	 * <li> szereg energii E(t-1), E(t-2), ..., E(t-24); </li>
	 * <li> szereg nasłoneczenienia I(t).</li>
	 */
	E24_I0, E24_T0_D_H_M24_M168,

	/**
	 * Prognoza wykonana w oparciu o:
	 * <li> szereg energii E(t-1), E(t-2), ..., E(t-24); </li>
	 * <li> szereg temperatury T(t).</li>
	 * <li> szereg wilgotności H(t).</li>
	 */
	E24_T0_H0,

	/**
	 * Prognoza wykonana w oparciu o:
	 * <li> szereg energii E(t-1), E(t-2), ..., E(t-24); </li>
	 * <li> szereg temperatury T(t).</li>
	 * <li> szereg nasłoneczenienia I(t).</li>
	 */
	E24_T0_I0, E24_T0_I0_D_H_M24_M168,
	/**
	 * Prognoza wykonana w oparciu o:
	 * <li> szereg energii E(t-1), E(t-2), ..., E(t-24); </li>
	 * <li> szereg wilgotności H(t).</li>
	 * <li> szereg nasłoneczenienia I(t).</li>
	 */
	E24_H0_I0,

	/**
	 * Prognoza wykonana w oparciu o:
	 * <li> szereg energii E(t-1), E(t-2), ..., E(t-24); </li>
	 * <li> szereg temperatury T(t).</li>
	 * <li> szereg wilgotności H(t).</li>
	 * <li> szereg nasłoneczenienia I(t).</li>
	 */
	E24_T0_H0_I0,

	/**
	 * Prognoza wykonana w oparciu o:
	 * <li> szereg energii E(t-1), E(t-2), ..., E(t-24); </li>
	 * <li> szereg temperatury T(t).</li>
	 * <li> szereg wilgotności H(t).</li>
	 * <li> szereg nasłoneczenienia I(t).</li>
	 * <li> kod dnia</li>
	 * <li> kod święta</li>
	 */
	E24_T0_H0_I0_D_H,

	E24_T0_H0_I0_M24_M168,

	E24_T0_H0_I0_D_H_M24_M168;
	static {
		EnumPersistenceDelegate.installFor(values());
	}
}
