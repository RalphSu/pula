/**
 * Created on 2007-5-7 下午12:43:51
 *
 * DiagCN.COM 2004-2006
 * $Id$
 */
package puerta.system.keeper;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;

/**
 * 
 * @author tiyi
 * 
 */
@Service
public class ProjectPath implements ServletContextAware {

	private String webRoot;

	public String getWebRoot() {
		return this.webRoot;
	}

	public String getUserFilePath() {
		return getWebRoot() + "/userFiles";
	}

	public String getWebInf() {
		return getWebRoot() + "/WEB-INF/";
	}

	public String getPath(String path) {
		return getWebRoot() + path;
	}

	/**
	 * @param image_savepath_vehicle
	 * @return
	 */
	public String parsePath(String path) {
		String tmp = StringUtils.replace(path, "${userFiles}",
				getUserFilePath());
		tmp = StringUtils.replace(tmp, "${webRoot}", getWebRoot());
		// if (ServletActionContext.getRequest() != null) {
		// tmp = StringUtils.replace(tmp, "${contextPath}",
		// ServletActionContext.getRequest().getContextPath());
		// }

		tmp = StringUtils.replace(tmp, "\\", File.separator);

		return tmp;
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.webRoot = servletContext.getRealPath("/");

	}

	public String parsePath(String string, HttpServletRequest request) {
		String tmp = StringUtils.replace(string, "${contextPath}",
				request.getContextPath());
		return tmp;
	}
}
