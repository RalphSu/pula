/**
 * Created on 2010-1-1
 * WXL 2009
 * $Id$
 */
package puerta.support.db;

import java.io.IOException;

/**
 * 
 * @author tiyi
 * 
 */
public class MysqlBackup {

	private String user, password, database, exportPath;
	private String mysqlPath;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getExportPath() {
		return exportPath;
	}

	public void setExportPath(String exportPath) {
		this.exportPath = exportPath;
	}

	public String getMysqlPath() {
		return mysqlPath;
	}

	public void setMysqlPath(String mysqlPath) {
		this.mysqlPath = mysqlPath;
	}

	public void export() {

		String stmt1 = "\"" + mysqlPath + "bin/mysqldump\" " + database
				+ " -u " + user + " -p" + password + " --result-file="
				+ exportPath;
		try {
			Runtime.getRuntime().exec(stmt1);
			System.out.println("数据已导出到文件" + exportPath + "中");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		MysqlBackup backup = new MysqlBackup();

		backup.setUser("tiyi");
		backup.setPassword("tiyilon");
		backup.setDatabase("umsystem");
		backup.setExportPath("d:\\a.sql");
		backup.setMysqlPath("d:/server/mysql/MySQL Server 5.0/");
		backup.export();
	}
}
