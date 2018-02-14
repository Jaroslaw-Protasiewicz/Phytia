package pl.phytia.prediction.tasks.serial;

/**
 * Wykonanie prognozy metamodelem szeregowym.
 * @author Jarosław Protasiewicz
 */
public class SuperSerialTask {
	
	public static void main(String[] args) {
		// Model M1 - energia
		MLPSerialTask_E24.doIt();
		// Model M2 - energia - temperatura
		MLPSerialTask_E24_T0.doIt();
		// Model M3 - energia - nasłonecznienie
		MLPSerialTask_E24_I0.doIt();
		// Model M4 - energia - wilgotność
		MLPSerialTask_E24_H0.doIt();
		// Model M5 - energia - temperatura - nasłonecznienie
		MLPSerialTask_E24_T0_I0.doIt();
		// Model M6 - energia - temperatura - wilgotność
		MLPSerialTask_E24_T0_H0.doIt();
		// Model M7 - energia - nasłonecznienie - wilgotność
		MLPSerialTask_E24_H0_I0.doIt();
		// Model M8 - energia - temperatura - nasłonecznienie - wilgotność
		MLPSerialTask_E24_T0_H0_I0.doIt();
	}
	
}
