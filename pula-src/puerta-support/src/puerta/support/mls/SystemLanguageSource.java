/**
 * Created on 2008-7-19 08:43:56
 *
 * NILVAR 2008
 * $Id$
 */
package puerta.support.mls;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * 
 * @author tiyi
 * 
 */
public class SystemLanguageSource implements ResourceLoaderAware,
		InitializingBean {

	/**
	 * Logger for this class
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger
			.getLogger(SystemLanguageSource.class);

	private Properties properties = new Properties();

	private ResourceLoader resourceLoader = new DefaultResourceLoader();
	private String currentLanguage;

	private String[] languageFiles;

	public String getCurrentLanguage() {
		return currentLanguage;
	}

	public void setCurrentLanguage(String currentLanguage) {
		this.currentLanguage = currentLanguage;
	}

	public void setLanguageFiles(String[] files) {
		languageFiles = files;

		// initLanguages

		// load languages;

	}

	/**
	 * @param name
	 * @param value0
	 * @param value1
	 * @param value2
	 * @param value3
	 * @return
	 */
	public String getText(String name, String... value0) {
		// checkModified();
		String str = null;
		if (properties.containsKey(name)) {
			str = properties.getProperty(name);
		} else {
			// String value = name;
			// if (value0 != null && value0.length > 0) {
			// value += " : " + value0.length;
			// }
			// properties.setProperty(name, value);
			// storeLanguage();
			return name + "=";
		}

		if (value0 == null)
			return str;
		int n = 0;
		for (String s : value0) {
			str = StringUtils.replace(str, "{" + (n++) + "}", s);
		}

		return str;

	}

	/**
	 * 
	 */
	public void storeLanguage() {

		try {
			properties.store(new FileOutputStream(
					new File("c:/test.properties")), "what?");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			for (String f : this.languageFiles) {
				Resource r = resourceLoader.getResource(f);
				if (r.exists()) {
					properties.load(r.getInputStream());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param fs
	 * @return
	 */
	// private long getLastModified(SystemLanguageFile[] fs) {
	// long n = 1;
	// for (SystemLanguageFile f : fs) {
	// if (f.getType() == SystemLanguageFile.TYPE_WEB_ROOT) {
	// if (n < f.getFile().lastModified()) {
	// n = f.getFile().lastModified();
	// }
	// }
	// }
	//
	// return n;
	// }

}
