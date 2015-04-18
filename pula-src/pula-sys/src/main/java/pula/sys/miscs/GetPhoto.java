package pula.sys.miscs;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class GetPhoto {

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
							"jdbc:jtds:sqlserver://localhost:1433/a;charset=CP936",
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
			GetPhoto getPhoto = new GetPhoto();
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
			ResultSet rs = st.executeQuery("select * from NFile ");
			while (rs.next()) {
				String fn = rs.getString("fileName");
				String newName = outFolder + fn;
				OutputStream out = new FileOutputStream(newName);
				System.out.println(fn);
				int tmpi = 0;
				InputStream ins = rs.getBinaryStream("image");
				while ((tmpi = ins.read()) != -1) {
					out.write(tmpi);
				}
				ins.close();
				out.flush();
				out.close();
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
