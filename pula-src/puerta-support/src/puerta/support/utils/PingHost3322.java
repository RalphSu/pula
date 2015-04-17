/*
 * Created on 2005-2-2
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package puerta.support.utils;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;

import sun.misc.BASE64Encoder;

/**
 * @author ibm
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class PingHost3322 extends Thread {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(PingHost3322.class);

	// public static final String HOST =
	// "http://freetiyi.3322.org/ip/index.php";
	public static final String HOST = "http://www.3322.org/dyndns/update?system=dyndns&hostname=";

	private String userName, password;
	private String domain;

	private static final int SLEEP_MS = 15 * 60 * 1000; // 15min

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public static void ping() {
		PingHost3322 ph = new PingHost3322();
		ph.start();
	}

	public void run() {

		while (true) {
			logger.debug("ping host... :" + this.getDomain());
			try {
				this.startPing();
			} catch (Exception e) {
				e.printStackTrace();
			}
			logger.debug("ping host finish");
			try {
				Thread.sleep(SLEEP_MS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public void startPing() throws Exception {
		// GetFile getFile = new GetFile();
		// getFile.exist(HOST);

		URL url = new URL(HOST + domain);
		URLConnection connection = url.openConnection();
		String authInfo = userName + ":" + password;

		String encode = new BASE64Encoder().encode(authInfo.getBytes());
		connection.setRequestProperty("Authorization", "Basic " + encode);

		InputStream filestream = connection.getInputStream();

		char c = '\u0200';//
		byte a[] = new byte[c];
		for (int i = 0; (i = filestream.read(a)) != -1;) {
			logger.debug(new String(a, 0, i));

		}

		filestream.close();
		// filewrite.close();
		logger.debug("ping ok");
	}

	public static void main(String[] args) {
		PingHost3322 ph = new PingHost3322();

		ph.setUserName("dycmusic");
		ph.setPassword("dycmusic123");
		ph.setDomain("umsys.3322.org");

		try {
			ph.startPing();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
