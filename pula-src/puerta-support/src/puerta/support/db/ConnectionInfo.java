/**
 * Created on 2008-4-4 下午09:56:02
 *
 * NILVAR 2008
 * $Id$
 */
package puerta.support.db;

/**
 * 
 * @author tiyi
 * 
 */
public class ConnectionInfo {

	private String url, userName, password, driver;

	public ConnectionInfo(String driver, String url, String userName,
			String password) {
		this.driver = driver;
		this.url = url;
		this.userName = userName;
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public static ConnectionInfo create(String oriUrl, String oriUser,
			String oriPassword, String jdbc) {
		ConnectionInfo ci = new ConnectionInfo(jdbc, oriUrl, oriUser,
				oriPassword);
		return ci;
	}
}
