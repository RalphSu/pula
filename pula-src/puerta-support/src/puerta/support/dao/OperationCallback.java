package puerta.support.dao;

public interface OperationCallback<T> {

	void after(T object , boolean value);
}
