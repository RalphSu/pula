/*
 * Created on 2006-6-11
 *$Id: ControlAtom.java,v 1.1 2006/12/10 05:53:57 tiyi Exp $
 * DiagCN.com (2003-2005)
 */
package puerta.system.vo;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author tiyi 2006-6-11 16:26:16
 */
public abstract interface ControlAtom {

	public abstract String execute(MultipartFile file, String textValue)
			throws Exception;
}
