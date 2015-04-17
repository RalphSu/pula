package puerta.support;

public interface ParameterProvider {

	String getString(String user);

	String getString(String host, String string);

	int getInt(String port, int i);

}
