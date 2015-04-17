/**
 * Created on 2008-12-21 05:12:32
 *
 * NILVAR 2008
 * $Id$
 */
package puerta.system.vo;

import java.util.List;

import puerta.system.po.Parameter;
import puerta.system.po.ParameterFolder;
import puerta.system.po.ParameterPage;

/**
 * 
 * @author tiyi
 * 
 */
public class ParameterWithPageAndFolder {

	private Parameter parameter;
	private List<ParameterPage> parameterPages;
	private List<ParameterFolder> parameterFolders;

	/**
	 * @param parameter2
	 * @param parameterPages2
	 * @param parameterFolders2
	 */
	public ParameterWithPageAndFolder(Parameter parameter2,
			List<ParameterPage> parameterPages2,
			List<ParameterFolder> parameterFolders2) {
		this.parameter = parameter2;
		this.parameterFolders = parameterFolders2;
		this.parameterPages = parameterPages2;
	}

	public Parameter getParameter() {
		return parameter;
	}

	public void setParameter(Parameter parameter) {
		this.parameter = parameter;
	}

	public List<ParameterPage> getParameterPages() {
		return parameterPages;
	}

	public void setParameterPages(List<ParameterPage> parameterPages) {
		this.parameterPages = parameterPages;
	}

	public List<ParameterFolder> getParameterFolders() {
		return parameterFolders;
	}

	public void setParameterFolders(List<ParameterFolder> parameterFolders) {
		this.parameterFolders = parameterFolders;
	}

}
