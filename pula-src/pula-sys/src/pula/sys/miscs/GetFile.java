package pula.sys.miscs;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.commons.io.FileUtils;

public class GetFile {

	/**
	 * @param args
	 *            简单的jdbc连接sqlserver2000 ，别忘了把三个驱动jar包放到lib目录下
	 */
	public Connection getCon() {
		try {
			// 连接SQLServer2000
			Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
			Connection con = DriverManager
					.getConnection(
							"jdbc:jtds:sqlserver://localhost:1433/nvdata2;charset=CP936",
							"sa", "tiyilon");
			return con;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			GetFile getPhoto = new GetFile();
			// String f = "2004315195907[1] copy.jpg";
			getPhoto.readImgFromDb("g:\\tddownload\\1\\out\\");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void readImgFromDb(String outFolder) {
		System.out.println("Reading from database to dir:" + outFolder);
		Connection con = null;
		try {
			con = getCon();

			Statement st = con.createStatement();
			ResultSet rs = st
					.executeQuery("select * from NComment where id='402881e40ed7f47a010ed818a5e2000d' ");
			while (rs.next()) {
				String fn = "1.txt";
				String newName = outFolder + fn;
				FileUtils.writeStringToFile(new File(newName),
						rs.getString("comment"));
				System.out.println("Reading from database to file " + newName
						+ " success");

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			con = null;
		}
	}
}
