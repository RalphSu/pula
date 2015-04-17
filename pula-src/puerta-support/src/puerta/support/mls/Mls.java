/**
 * Created on 2008-7-24 03:20:10
 *
 * NILVAR 2008
 * $Id$
 */
package puerta.support.mls;

/**
 * 
 * @author tiyi
 * 
 */
public class Mls {
	private SystemLanguageSource systemLanguageSource;

	// private static Mls mlsInstance;
	// private static Map<String,Mls> map = new WxlSugar().newHashMap();

	public SystemLanguageSource getSystemLanguageSource() {
		return systemLanguageSource;
	}

	public void setSystemLanguageSource(
			SystemLanguageSource systemLanguageSource) {
		this.systemLanguageSource = systemLanguageSource;
	}

	public String t(String name, String... value0) {
		try {
			systemLanguageSource.afterPropertiesSet();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return systemLanguageSource.getText(name, value0);
	}

	public void setMls(Mls mlsInstance) {

	}

	/**
	 * @param resourceTypeMgrImpl
	 * @param string
	 * @param bean
	 * @return
	 */
	// public <T> String ct(Class<T> clazz, String name, String... value0) {
	// String packageName = ClassTool.getPackage(clazz, 1, 2, true, ".");
	// // logger.debug("package=" + packageName);
	// StringBuilder sb = new StringBuilder(40);
	// sb.append(packageName).append(".").append(name);
	// return t(sb.toString(), value0);
	// }

	/**
	 * @return
	 */
	public String getCurrentLanguage() {
		return systemLanguageSource.getCurrentLanguage();
	}

	/**
	 * @param names
	 * @return
	 */
	public String[] load(String[] names) {
		if (names == null | names.length == 0)
			return new String[] {};
		String[] strs = new String[names.length];
		int c = names.length;
		for (int n = 0; n < c; n++) {
			strs[n] = t(names[n]);
		}
		return strs;
	}

	// public static String[] l(String[] names) {
	// return mlsInstance.load(names);
	// }

	/**
	 * @param string
	 */
	public static void raise(String string, String... args) {
		throw new MlsException(string, args);

	}

	public String parseException(MlsException ex) {
		return t(ex.getMessage(), ex.getValues());
	}

	// public void init() {
	// // if (mlsInstance != null) {
	// // Pe.raise(" Mls has been initialized");
	// // }
	// // mlsInstance = this;
	//
	// }

	// public Mls getMls(String k){
	// if(map.containsKey(k)){
	// return map.get(k);
	// }
	//
	// }

	// public static String st(String string, String... values) {
	// return mlsInstance.t(string, values);
	// }
	//
	// public static Mls instance() {
	// return mlsInstance;
	// }

}
