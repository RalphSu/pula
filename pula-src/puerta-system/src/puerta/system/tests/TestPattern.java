package puerta.system.tests;

import java.util.regex.Pattern;

public class TestPattern {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String reg = "^/app" + ".*";
		Pattern p = Pattern.compile(reg);
		String s = "/laputa/appfield/";
		String s1 = "/app/field";
		System.out.println("regex=" + reg);
		System.out.println(s + "==>" + p.matcher(s).find());
		System.out.println(s1 + "==>" + p.matcher(s1).find());

	}
}
