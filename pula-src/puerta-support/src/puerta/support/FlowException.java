package puerta.support;

public class FlowException extends BasementException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7816529816150037775L;
	public static final String FLOW = FlowException.class.getName()
			+ "_ATTRIBUTE";

	/**
	 * @param message
	 */
	public FlowException(String message) {
		super(message);
	}

}
