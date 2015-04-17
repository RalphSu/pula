/**
 * Created on 2008-9-15 02:25:27
 *
 * NILVAR 2008
 * $Id$
 */
package puerta.support;

/**
 * 
 * @author tiyi
 * 
 */
public class I18NException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8374475857386958317L;

	private String[] values;

	/**
	 * 
	 */
	public I18NException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public I18NException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public I18NException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public I18NException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param string
	 * @param no
	 */
	public I18NException(String key, String... values) {
		super(key);
		this.values = values;
	}

	public String[] getValues() {
		return this.values;
	}

}
