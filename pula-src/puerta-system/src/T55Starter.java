import java.io.File;

import org.apache.catalina.Context;
import org.apache.catalina.Engine;
import org.apache.catalina.Host;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Embedded;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.modeler.util.IntrospectionUtils;
import org.apache.log4j.Logger;

import puerta.support.Pe;

public class T55Starter {

	private static final Logger logger = Logger.getLogger(T55Starter.class);
	public static final String DEFAULT_ROOT = "D:\\server\\apache-tomcat-5.5.33-embed";
	public static String URI_ENCODING = "UTF-8";
	public static boolean SECURE = false;
	public static boolean CLIENT_AUTH = false;
	public static int SSL_PORT = 8443;

	public static void main(String[] args) throws Exception {

		// Embedded tomcat = new Embedded();
		// String path = "D:/server/apache-tomcat-5.5.17-embed";
		// tomcat.setCatalinaHome(path);
		// // tomcat.addEngine(engine);
		// // tomcat.setDebug(Logger.WARNING);
		//
		// Engine engine = tomcat.createEngine();
		// engine.setName("EspServer");
		//
		// Host host = tomcat.createHost("localhost", tomcat.getCatalinaHome()
		// + "/webapps");
		//
		// engine.addChild(host);
		// engine.setDefaultHost(host.getName());
		//
		// tomcat.addEngine(engine);
		//
		// Context ctxtRoot = tomcat
		// .createContext("", host.getAppBase() + "/ROOT");
		// ctxtRoot.setPrivileged(true);
		//
		// host.addChild(ctxtRoot);

		String root = args[0];
		String deploy = args[1];
		String mapping = args[2];

		int port = 8080;

		if (args.length > 3) {
			try {
				port = Integer.parseInt(args[3]);
			} catch (Exception ex) {
				logger.error("ERROR:Port Number is not an integer:" + port);
			}
		} else {
			// logger.info("Default Port=" + port);
		}

		startServer(root, deploy, mapping, port);

	}

	public static void startServer(String root, String deploy, String mapping,
			int port) {

		if (!(new File(root)).exists()) {
			Pe.raise("not found root :" + root);
		}

		T55Starter s = new T55Starter();
		// root = "D:\\server\\apache-tomcat-5.5.28-embed";
		logger.info("Tomcat Home =" + root);
		logger.info("Deploy Apps =" + deploy);
		logger.info("Mapping Path=" + mapping);
		logger.info("Listener Port=" + port);

		try {
			Embedded t = s.createTomcat(root, new String[] { deploy },
					new String[] { mapping }, port);
			t.start();

			logger.info("Tomcat started");
			// return t;
		} catch (Exception e) {

			e.printStackTrace();
			// return null;
		}

		logger.warn("http://localhost:" + port + mapping);
	}

	public static void startServer(String root, String[] deploy,
			String[] mapping, int port) throws Exception {

		T55Starter s = new T55Starter();

		logger.info("Tomcat Home =" + root);
		logger.info("Deploy Apps =" + deploy);
		logger.info("Mapping Path=" + mapping);
		logger.info("Listener Port=" + port);

		Embedded t = s.createTomcat(root, deploy, mapping, port);

		try {
			t.start();

			logger.info("Tomcat started");
			// return t;
		} catch (LifecycleException e) {

			e.printStackTrace();
			// return null;
		}
	}

	public Embedded createTomcat(String root, String[] deploy,
			String[] mapping, int port) throws Exception {
		Embedded tomcat = new Embedded();

		// logger.info("file :" + tomcat.getClass().getResource(".").getFile());

		tomcat.setCatalinaHome(root);

		Engine engine = tomcat.createEngine();
		engine.setName("EspServer");

		Host host = tomcat.createHost("localhost", tomcat.getCatalinaHome()
				+ "/webapps");

		engine.addChild(host);
		engine.setDefaultHost(host.getName());

		if (ArrayUtils.indexOf(mapping, "/") > 0) {

			// if (!StringUtils.isEmpty(mapping)) {
			logger.debug("root :" + host.getAppBase() + "/ROOT");
			Context ctxtRoot = tomcat.createContext("", host.getAppBase()
					+ "/ROOT");
			ctxtRoot.setPrivileged(true);

			host.addChild(ctxtRoot);
		}
		// }

		// create webroot of nilnut
		// PersistentManager m = new
		// org.apache.catalina.session.PersistentManager();
		// m.setSaveOnRestart(true);
		// m.setMaxActiveSessions(-1);
		// m.setMinIdleSwap(-1);
		// m.setMaxIdleSwap(-1);
		// m.setMaxIdleBackup(-1);
		//
		// // FileStore s = new FileStore();
		// // s.setDirectory("session");
		// // m.setStore(s);
		//
		// org.apache.catalina.session.JDBCStore store = new JDBCStore();
		// store.setConnectionURL("jdbc:mysql://localhost:7788/hst01?useUnicode=true&characterEncoding=utf-8&user=tiyi&password=tiyilon");
		// store.setDriverName("com.p6spy.engine.spy.P6SpyDriver");
		// store.setSessionTable("tomcat$sessions");
		// store.setSessionIdCol("id");
		// store.setSessionDataCol("data");
		// store.setSessionValidCol("valid");
		// store.setSessionAppCol("app");
		// store.setSessionMaxInactiveCol("maxinactive");
		// store.setSessionLastAccessedCol("lastaccess");
		//
		// m.setStore(store);
		// m.addLifecycleListener(new LifecycleListener() {
		//
		// @Override
		// public void lifecycleEvent(LifecycleEvent arg0) {
		// System.err.println(arg0.getData());
		// System.err.println(arg0.getSource());
		// System.err.println(arg0.getType());
		//
		// }
		// });
		// store.addLifecycleListener(new LifecycleListener() {
		//
		// @Override
		// public void lifecycleEvent(LifecycleEvent arg0) {
		// System.err.println(arg0.getData());
		// System.err.println(arg0.getSource());
		// System.err.println(arg0.getType());
		//
		// }
		// });

		for (int i = 0; i < deploy.length; i++) {
			Context ctxtEsp = tomcat.createContext(mapping[i], deploy[i]);
			ctxtEsp.setSessionTimeout(40);
			host.addChild(ctxtEsp);
			// ctxtEsp.setManager(m);
		}

		tomcat.addEngine(engine);

		String addr = null;
		Connector c = tomcat.createConnector(addr, port, false);

		// System.out.println(c);
		// CoyoteConnector cc = (CoyoteConnector) c;
		if (!StringUtils.isEmpty(URI_ENCODING)) {
			c.setURIEncoding(URI_ENCODING);
		}
		// c.setURIEncoding("GBK");
		tomcat.addConnector(c);

		if (SECURE) {
			Connector ssl = tomcat.createConnector(addr, SSL_PORT, true);

			IntrospectionUtils.setAttribute(ssl, "sslProtocol", "TLS");
			IntrospectionUtils.setProperty(ssl, "keystoreFile",
					"D:\\work\\escort\\openssl\\localhost.jks");
			IntrospectionUtils.setProperty(ssl, "truststoreFile",
					"D:\\work\\escort\\openssl\\证书\\root\\root.jks");
			IntrospectionUtils.setProperty(ssl, "keystoreType", "JKS");
			if (CLIENT_AUTH) {
				IntrospectionUtils.setProperty(ssl, "clientAuth", "true");
			} else {
				IntrospectionUtils.setProperty(ssl, "clientAuth", "false");
			}
			IntrospectionUtils.setProperty(ssl, "keystorePass", "123456");
			IntrospectionUtils.setProperty(ssl, "acceptCount", "100");
			IntrospectionUtils.setProperty(ssl, "truststorePass", "123456");
			IntrospectionUtils.setProperty(ssl, "truststoreType", "JKS");
			ssl.setProtocol("SSL");

			// System.out.println(ssl.getProtocol());
			tomcat.addConnector(ssl);
		}

		return tomcat;
	}

	public static void startServer(Class<?> class1, String mapping, int port) {
		String root = T55Starter.DEFAULT_ROOT;

		String path = class1.getResource("/").getFile();

		// System.out.println(path);

		if (path.startsWith("/")) {
			path = path.substring(1);
		}

		int pos = path.indexOf("WEB-INF");
		if (pos > 0) {
			path = path.substring(0, pos);
		}

		String deploy = path;

		T55Starter.startServer(root, deploy, mapping, port);

	}
}
