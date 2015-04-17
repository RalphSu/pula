/**
 * Created on 2009-6-18
 * WXL 2009
 * $Id$
 */
package puerta.system.intfs;

import java.util.List;

/**
 * 
 * @author tiyi
 * 
 */
public interface IWxlPluginSetting<T> {

	List<T> doExports(String appFieldNo);

	void doImport(List<T> objs, boolean update);

	String getPluginName();

	JDOMTransfer<T> getTransfer();

	void doClear();

}
