package puerta.support.vo;

public class Mix<T, E> {

	public T object1;
	public E object2;

	public static <T, E> Mix<T, E> create(T t, E e) {
		Mix<T, E> r = new Mix<T, E>();
		r.object1 = t;
		r.object2 = e;
		return r;
	}

	public T getObject1() {
		return object1;
	}

	public void setObject1(T object1) {
		this.object1 = object1;
	}

	public E getObject2() {
		return object2;
	}

	public void setObject2(E object2) {
		this.object2 = object2;
	}
}
