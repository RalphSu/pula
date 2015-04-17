/*
 * Created on 2005-4-8
 *$Id: ClassTool.java,v 1.3 2006/12/23 08:54:32 tiyi Exp $
 * DiagCN.com (2003-2005)
 */
package puerta.support.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang.ClassUtils;
import org.apache.log4j.Logger;

/**
 * @author tiyi 2005-4-8 18:28:21
 */
public class ClassTool {
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(ClassTool.class);

	public static String getClassPath() {
		String url = ClassTool.class.getResource("/").getPath();
		return url;
	}

	public static String[] getPackageAllClassName(String classLocation,
			String packageName) {
		// 将packageName分解
		String[] packagePathSplit = packageName.split("[.]");
		String realClassLocation = classLocation;
		int packageLength = packagePathSplit.length;
		for (int i = 0; i < packageLength; i++) {
			realClassLocation = realClassLocation + File.separator
					+ packagePathSplit[i];
		}
		File packeageDir = new File(realClassLocation);
		if (packeageDir.isDirectory()) {
			String[] allClassName = packeageDir.list();
			return allClassName;
		}
		return null;
	}

	/**
	 * 从包package中获取所有的Class
	 * 
	 * @param pack
	 * @return
	 */
	public static Set<Class<?>> getClasses(String packageName) {

		// 第一个class类的集合
		Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
		// 是否循环迭代
		boolean recursive = true;
		// 获取包的名字 并进行替换
		//String packageName = pack.getName();
		String packageDirName = packageName.replace('.', '/');
		// 定义一个枚举的集合 并进行循环来处理这个目录下的things
		Enumeration<URL> dirs;
		try {
			dirs = Thread.currentThread().getContextClassLoader()
					.getResources(packageDirName);
			// 循环迭代下去
			while (dirs.hasMoreElements()) {
				// 获取下一个元素
				URL url = dirs.nextElement();
				// 得到协议的名称
				String protocol = url.getProtocol();
				// 如果是以文件的形式保存在服务器上
				if ("file".equals(protocol)) {
					// 获取包的物理路径
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					// 以文件的方式扫描整个包下的文件 并添加到集合中
					findAndAddClassesInPackageByFile(packageName, filePath,
							recursive, classes);
				} else if ("jar".equals(protocol)) {
					// 如果是jar包文件
					// 定义一个JarFile
					JarFile jar;
					try {
						// 获取jar
						jar = ((JarURLConnection) url.openConnection())
								.getJarFile();
						// 从此jar包 得到一个枚举类
						Enumeration<JarEntry> entries = jar.entries();
						// 同样的进行循环迭代
						while (entries.hasMoreElements()) {
							// 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
							JarEntry entry = entries.nextElement();
							String name = entry.getName();
							// 如果是以/开头的
							if (name.charAt(0) == '/') {
								// 获取后面的字符串
								name = name.substring(1);
							}
							// 如果前半部分和定义的包名相同
							if (name.startsWith(packageDirName)) {
								int idx = name.lastIndexOf('/');
								// 如果以"/"结尾 是一个包
								if (idx != -1) {
									// 获取包名 把"/"替换成"."
									packageName = name.substring(0, idx)
											.replace('/', '.');
								}
								// 如果可以迭代下去 并且是一个包
								if ((idx != -1) || recursive) {
									// 如果是一个.class文件 而且不是目录
									if (name.endsWith(".class")
											&& !entry.isDirectory()) {
										// 去掉后面的".class" 获取真正的类名
										String className = name.substring(
												packageName.length() + 1,
												name.length() - 6);
										try {
											// 添加到classes
											classes.add(Class
													.forName(packageName + '.'
															+ className));
										} catch (ClassNotFoundException e) {
											log.error("添加用户自定义视图类错误 找不到此类的.class文件");
											e.printStackTrace();
										}
									}
								}
							}
						}
					} catch (IOException e) {
						log.error("在扫描用户定义视图时从jar包获取文件出错");
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return classes;
	}

	/**
	 * 以文件的形式来获取包下的所有Class
	 * 
	 * @param packageName
	 * @param packagePath
	 * @param recursive
	 * @param classes
	 */
	public static void findAndAddClassesInPackageByFile(String packageName,
			String packagePath, final boolean recursive, Set<Class<?>> classes) {
		// 获取此包的目录 建立一个File
		File dir = new File(packagePath);
		// 如果不存在或者 也不是目录就直接返回
		if (!dir.exists() || !dir.isDirectory()) {
			log.warn("用户定义包名 " + packageName + " 下没有任何文件");
			return;
		}
		// 如果存在 就获取包下的所有文件 包括目录
		File[] dirfiles = dir.listFiles(new FileFilter() {
			// 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
			public boolean accept(File file) {
				return (recursive && file.isDirectory())
						|| (file.getName().endsWith(".class"));
			}
		});
		// 循环所有文件
		for (File file : dirfiles) {
			// 如果是目录 则继续扫描
			if (file.isDirectory()) {
				findAndAddClassesInPackageByFile(
						packageName + "." + file.getName(),
						file.getAbsolutePath(), recursive, classes);
			} else {
				// 如果是java类文件 去掉后面的.class 只留下类名
				String className = file.getName().substring(0,
						file.getName().length() - 6);
				try {
					// 添加到集合中去
					classes.add(Class.forName(packageName + '.' + className));
				} catch (ClassNotFoundException e) {
					log.error("添加用户自定义视图类错误 找不到此类的.class文件");
					e.printStackTrace();
				}
			}
		}
	}

	// public static List<String> getClasses(String packageName) {
	// return getClasses(packageName, null);
	// }

	// public static <T> List<String> getClasses(String packageName, Class<T>
	// clazz) {
	// List<String> res = new ArrayList<String>();
	// // String pckgname = "com.diagcn.udk.bvrw.actions.admin";
	// logger.debug("clazz=" + clazz);
	// // pckgname = packageName;
	// String name = new String(packageName);
	// if (!name.startsWith("/")) {
	// name = "/" + name;
	// }
	// name = name.replace('.', '/');
	//
	// // 获得一个File对象
	// // URL url = ClassTool.class.getResource(name);
	//
	// name = name + "/";
	// logger.debug("name=" + name);
	// URL url = ClassLoaderUtil.getResource(name, clazz);
	// if (url == null)
	// url = ClassTool.class.getResource(name);
	// File directory = new File(url.getFile());
	//
	// if (directory.exists()) {
	// // 获得包里面的文件清单
	// String[] files = directory.list();
	// for (int i = 0; i < files.length; i++) {
	//
	// // 我们只对.class文件感兴趣
	// if (files[i].endsWith(".class")) {
	// // 删除.class文件扩展名
	// String classname = files[i].substring(0,
	// files[i].length() - 6);
	// try {
	// String clsName = packageName + "." + classname;
	// logger.debug("clsName=" + clsName);
	//
	// if (clazz.isAssignableFrom(Class.forName(clsName))) {
	// res.add(packageName + "." + classname);
	// }
	//
	// // 尝试创建该对象的一个实例
	// // Object o = Class.forName(clsName).newInstance();
	// // if (o instanceof Class) {
	// // System.out.println(classname);
	// // }
	//
	// // if (o instanceof Object) {
	// // /}
	//
	// // if (clazz != null) {
	// //
	// // if (!o
	// // .getClass()
	// // .getName()
	// // .equals((clazz.getName()))) {
	// // logger.debug("obj.class" + o.getClass());
	// // continue;
	// // }
	// // }
	//
	// } catch (ClassNotFoundException cnfex) {
	// logger.error("getClasses(String)" + cnfex, cnfex);
	// }
	// }
	// }
	// }
	//
	// return res;
	// }

	public static String getPackage(Object obj, int level, boolean levelInverse) {
		return getPackage(obj.getClass(), level, levelInverse);

	}

	/**
	 * ���ָ������
	 * 
	 * @param cls
	 *            ��
	 * @param level
	 *            �㼶(0��ʼ)
	 * @param levelInverse
	 *            (true)�Ӻ���ǰ��㼶
	 * @return ָ���㼶�İ���
	 */
	public static <T> String getPackage(Class<T> cls, int level,
			boolean levelInverse) {
		String packageName = ClassUtils.getPackageName(cls);
		String res = "";
		for (int i = 0; i <= level; i++) {
			int pos = -1;
			if (levelInverse) {
				pos = packageName.lastIndexOf('.');
			} else {
				pos = packageName.indexOf('.');
			}
			if (pos == -1) {
				// System.out.println("not found");
				break;
			}
			if (levelInverse) {
				res = packageName.substring(pos + 1, packageName.length());
				packageName = packageName.substring(0, pos);
			} else {
				res = packageName.substring(0, pos);
				packageName = packageName.substring(pos + 1,
						packageName.length());
			}

			// System.out.println("packageName=" + packageName);
		}

		return res;
	}

	public static String getPackage(Object obj, int beginLevel, int endLevel,
			boolean levelInverse, String sperator) {

		return getPackage(obj.getClass(), beginLevel, endLevel, levelInverse,
				sperator);
	}

	public static <T> String getPackage(Class<T> clazz, int beginLevel,
			int endLevel, boolean levelInverse, String seprator) {
		String packageName = ClassUtils.getPackageName(clazz);

		StringTokenizer stringToken = new StringTokenizer(packageName, ".");
		int count = stringToken.countTokens();
		int span = endLevel - beginLevel;
		if (levelInverse) {
			/*
			 * 反转! 原先是 c = (a.b.c.d.e.f) b= 1 (e),e = 2(d) 反转后,应该是(d,e) 则 b= 3 ,
			 * e = 4;
			 */
			beginLevel = count - 1 - endLevel;
			endLevel = beginLevel + span;
		}

		StringBuffer sb = new StringBuffer();
		String res;
		int i = 0;
		while (stringToken.hasMoreTokens()) {
			if (i < beginLevel || i > endLevel) {
				i++;
				stringToken.nextToken();
				continue;
			}

			res = stringToken.nextToken();
			sb.append(res);
			if (i != endLevel) {
				sb.append(seprator);
			}

			i++;
		}
		return sb.toString();
		// String packageName = ClassUtils.getPackageName(clazz);
		// String res = "";
		// List l = new ArrayList();
		// StringBuffer sb = new StringBuffer();
		// for (int i = 0; i <= endLevel; i++) {
		// int pos = -1;
		// if (levelInverse) {
		// pos = packageName.lastIndexOf('.');
		// } else {
		// pos = packageName.indexOf('.');
		// }
		// if (pos == -1) {
		// // System.out.println("not found");
		// break;
		// }
		// if (levelInverse) {
		// res = packageName.substring(pos + 1, packageName.length());
		// packageName = packageName.substring(0, pos);
		// } else {
		// res = packageName.substring(0, pos);
		// packageName = packageName.substring(pos + 1, packageName
		// .length());
		// }
		//
		// if (i >= beginLevel) {
		// l.add(res);
		// }
		//
		// // System.out.println("packageName=" + packageName);
		// }
		// if(levelInverse){
		//
		// for (int i = l.size()-1 ; i >=0 ; i-- ) {
		// res = (String) l.size() ;
		// sb.append(res).append(sperator);
		// }
		// }
		//
		// // if (levelInverse) {
		// // Iterator iter = l.iterator();
		// // }else{
		// // ListIterator iter = l.listIterator();
		// // iter.
		// // }
		// // for (; iter.hasNext();) {
		// // res = (String) iter.next();
		// // sb.append(res).append(sperator);
		// // }
		//
		// return sb.toString();
	}

	public static void main(String[] args) {
		// List l = getClasses("com.diagcn.udk.bvrw.actions.admin");

		// System.out.println("Name=" + getPackage(ClassTool.class, 4, false));

		// System.out.println("Name="
		// + getPackage(ListPerson.class, 1, 2, true, "/"));
	}

	public static Object getInstance(String clsName) {
		try {
			return Class.forName(clsName).newInstance();

		} catch (Exception ex) {
			return null;
		}
	}

	// public static <T> InputStream getResourceAsStream(String string,
	// Class<T> clazz) {
	// InputStream is = ClassLoaderUtil.getResourceAsStream(string, clazz);
	// if (is == null) {
	// return clazz.getResourceAsStream(string);
	// }
	// return is;
	// }
}
