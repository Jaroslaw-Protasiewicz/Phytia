package pl.phytia.model.enums;

import java.beans.BeanInfo;
import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.Expression;
import java.beans.IntrospectionException;
import java.beans.Introspector;

/**
 * Klasa niezbędna do serializacji przez Encoder typów Enum. W javie 1.5
 * wystepuję błąd serializacji jest opisany na stronie:
 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=5015403. Podobno w java
 * 1.6 już nie występuje.
 */
public class EnumPersistenceDelegate extends DefaultPersistenceDelegate {

	private static EnumPersistenceDelegate INSTANCE = new EnumPersistenceDelegate();

	public static void installFor(Enum<?>[] values) {
		Class<? extends Enum> declaringClass = values[0].getDeclaringClass();
		installFor(declaringClass);

		for (Enum<?> e : values)
			if (e.getClass() != declaringClass)
				installFor(e.getClass());
	}

	static void installFor(Class<? extends Enum> enumClass) {
		try {
			BeanInfo info = Introspector.getBeanInfo(enumClass);
			info.getBeanDescriptor().setValue("persistenceDelegate", INSTANCE);
		} catch (IntrospectionException exception) {
			throw new RuntimeException("Unable to persist enumerated type "
					+ enumClass, exception);
		}
	}

	protected Expression instantiate(Object oldInstance, Encoder out) {
		Enum e = (Enum) oldInstance;
		return new Expression(Enum.class, "valueOf", new Object[] {
				e.getDeclaringClass(), e.name() });
	}

	protected boolean mutatesTo(Object oldInstance, Object newInstance) {
		return oldInstance == newInstance;
	}
}