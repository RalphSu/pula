/**
 * Created on 2010-3-20
 * WXL 2009
 * $Id$
 */
package puerta.system.intfs;

import java.util.List;

import puerta.support.dao.IWxlActor;

/**
 * 
 * @author tiyi
 * 
 */
public interface IWxlPluginActor<T extends IWxlActor> {

	public String getActorId(String no);

	public String getAppFieldNo();

	public List<T> getActorsByRole(String rno);
}
