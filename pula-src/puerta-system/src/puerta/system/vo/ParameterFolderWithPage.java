/**
 * Created on 2008-12-21 05:03:18
 *
 * NILVAR 2008
 * $Id$
 */
package puerta.system.vo;

import java.util.List;

import puerta.system.po.AppField;
import puerta.system.po.ParameterFolder;
import puerta.system.po.ParameterPage;

/**
 * 
 * @author tiyi
 * 
 */
public class ParameterFolderWithPage {

	private ParameterFolder folder;
	private List<ParameterPage> pages;
	private AppField appField;

	/**
	 * @param folder2
	 * @param appField
	 * @param parameterPages
	 */
	public ParameterFolderWithPage(ParameterFolder folder2, AppField appField2,
			List<ParameterPage> parameterPages) {
		this.folder = folder2;
		this.appField = appField2;
		this.pages = parameterPages;
	}

	public AppField getAppField() {
		return appField;
	}

	public void setAppField(AppField appField) {
		this.appField = appField;
	}

	public ParameterFolder getFolder() {
		return folder;
	}

	public void setFolder(ParameterFolder folder) {
		this.folder = folder;
	}

	public List<ParameterPage> getPages() {
		return pages;
	}

	public void setPages(List<ParameterPage> pages) {
		this.pages = pages;
	}

}
