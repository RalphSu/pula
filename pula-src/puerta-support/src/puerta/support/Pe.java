package puerta.support;

public class Pe {

	public static <E> E raise(String text) {
		throw new BasementException(text);
	}

	public static <E> E notLogin() {
		throw new NotLoginException();
	}

	public static <E> E notFound() {
		throw new NotFoundException();
	}

	public static <E> E raiseFlow(String text) {
		throw new FlowException(text);
	}

	public static <E> E raiseI18N(String text, String... values) {
		throw new I18NException(text, values);
	}
}
