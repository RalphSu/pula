package pula.web.tests;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MakeMapping {

//	public static void main(String[] args) {
//
//		String path = getWebRoot();
//
//		String cmd = "cmd.exe /c mklink /d %sstatic\\%s D:\\work\\puerta\\javascript\\%s";
//
//		String root = path.replace('/', '\\');
//		run(String.format(cmd, root, "laputa", "laputa"));
//		run(String.format(cmd, root, "library", "library"));
//
//		// mklink /d D:\work\java\mibim\WebRoot\static\laputa
//		// D:\work\puerta\javascript\laputa
//		// mklink /d D:\work\java\mibim\WebRoot\static\library
//		// D:\work\puerta\javascript\library
//
//	}
//
//	public static String getWebRoot() {
//		String path = MakeMapping.class.getResource("/").getFile();
//
//		System.out.println(path);
//
//		if (path.startsWith("/")) {
//			path = path.substring(1);
//		}
//
//		int pos = path.indexOf("WEB-INF");
//		if (pos > 0) {
//			path = path.substring(0, pos);
//		}
//		return path;
//	}
//
//	public static void run(String cmd) {
//		System.out.println("cmd=" + cmd);
//
//		try {
//			Runtime rt = Runtime.getRuntime();
//			Process proc = rt.exec(cmd);
//			InputStream stderr = proc.getErrorStream();
//			InputStreamReader isr = new InputStreamReader(stderr);
//			BufferedReader br = new BufferedReader(isr);
//			String line = null;
//			System.out.println("<ERROR>");
//			while ((line = br.readLine()) != null)
//				System.out.println(line);
//			System.out.println("</ERROR>");
//			int exitVal = proc.waitFor();
//			System.out.println("Process exitValue: " + exitVal);
//		} catch (Throwable t) {
//			t.printStackTrace();
//		}
//	}
}
