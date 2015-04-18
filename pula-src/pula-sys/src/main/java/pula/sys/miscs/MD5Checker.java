package pula.sys.miscs;

import java.security.MessageDigest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import puerta.support.Pe;
import puerta.system.keeper.ParameterKeeper;
import pula.sys.BhzqConstants;

public class MD5Checker {

	private static final String DEFAULT_SECERT = "@3333242";

	private static final Logger logger = Logger.getLogger(MD5Checker.class);

	public static void check(ParameterKeeper pk, String md5, Object... objs) {

		String secret = pk.getString(BhzqConstants.INTERFACE_CALL_SECRET, DEFAULT_SECERT);

		StringBuilder sb = new StringBuilder(secret);

		for (Object object : objs) {
			if (object == null) {

			} else {
				sb.append(object.toString());
			}
		}

		String enc = Md5Here.md5s(sb.toString());
		if (StringUtils.equals(enc, md5)) {
			return;
		}

		logger.debug(sb.toString() + " md5=" + enc + " ios md5=" + md5);

		Pe.raise("校验错误");

	}

	public static void main(String[] args) {
		String src = "@333324217774882-30a1-4169-b4b5-1915375fd0a4B247B4DE-8C53-4048-B904-620D5FB126FE改革13544654665秘密0012";

		System.out.println("md5=" + Md5Here.md5s(src));
	}
}

class Md5Here {

	public static String md5s(String plainText) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes("UTF-8"));
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			// str = buf.toString();
			System.out.println("result: " + buf.toString());// 32位的加密
			System.out.println("result: " + buf.toString().substring(8, 24));// 16位的加密
			return buf.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return null;
	}

}
