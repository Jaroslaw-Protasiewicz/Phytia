package pl.phytia.model.enums;

/**
 * Typy moetod doboru centrów sieci RBF.
 * 
 * @author Jarosław Protasiewicz
 * 
 */
public enum EnumCenterSelectType {
	/**
	 * Dokładny dobór centrów - równy zbiorowi treningowemu.
	 */
	STRICT_CENTER_SELECT,
	/**
	 * Locowy dobór centrów - losowany ze zbioru treningowego.
	 */
	RANDOM_CENTER_SELECT,
	/**
	 * Dobór centrów metodą k-średnich.
	 */
	K_MEAN_CENTER_SELECT;

}
