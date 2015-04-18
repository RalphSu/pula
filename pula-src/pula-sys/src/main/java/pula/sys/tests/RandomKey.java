package pula.sys.tests;

import puerta.support.utils.MD5;
import puerta.support.utils.RandomTool;

public class RandomKey {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(MD5.GetMD5String(RandomTool.getRandomString(40))
				.substring(0, 24));

	}

}
