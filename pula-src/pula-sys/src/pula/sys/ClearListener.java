package pula.sys;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

public class ClearListener implements ServletContextListener {

	public static Logger logger = Logger.getLogger(ClearListener.class);

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// On Application Startup, please…

		// Usually I'll make a singleton in here, set up my pool, etc.
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// On Application Shutdown, please…

		Enumeration<Driver> drivers = DriverManager.getDrivers();
		while (drivers.hasMoreElements()) {
			Driver driver = drivers.nextElement();
			try {
				DriverManager.deregisterDriver(driver);
				logger.debug(String.format("deregistering jdbc driver: %s",
						driver));
			} catch (SQLException e) {
				logger.debug(
						String.format("Error deregistering driver %s", driver),
						e);
			}

		}
	}

}