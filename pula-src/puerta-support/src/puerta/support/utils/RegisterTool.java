package puerta.support.utils;

import java.net.NetworkInterface;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Enumeration;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.lang.StringUtils;

import puerta.support.Pe;

public class RegisterTool {

	public static String byte2hex2(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString((255 - b[n]) & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();

	}

	/**
	 * 
	 * 二行制转字符串
	 * 
	 * @param b
	 * 
	 * @return
	 * 
	 */

	public static String byte2hex(byte[] b) {

		String hs = "";

		String stmp = "";

		for (int n = 0; n < b.length; n++) {

			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}

		return hs.toUpperCase();

	}

	/**
	 * 
	 * 功能:字符串二行制
	 * 
	 * @param b
	 * @return
	 * 
	 */
	public static byte[] hex2byte(byte[] b) {

		if ((b.length % 2) != 0)
			throw new IllegalArgumentException("长度不是偶数");
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}

	private static String getMac() {
		try {
			Enumeration<NetworkInterface> el = NetworkInterface
					.getNetworkInterfaces();
			while (el.hasMoreElements()) {
				NetworkInterface ni = el.nextElement();

				byte[] mac = ni.getHardwareAddress();
				if (mac == null || mac.length == 0)
					continue;

				return byte2hex2(mac);

			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return null;
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		System.out.println("machineNo=" + getMac());
		// String clientName = "uniquemotosport";
		String clientName = "pula_web";

		// clientName = "ANYWHERE";
		String machineNo = TIYI_LOCAL;
		// String machineNo = "FFDCABA4FDA6";
		String sn = generate(machineNo, DateExTool.string2date("2042-6-27"),
				clientName);
		System.out.println("SN=" + sn);
		checkRegisterNo(sn, getMac(), clientName);
		Calendar cal = getExpiredTime(sn);
		System.out.println("sn=" + sn);
		System.out.println("gettime=" + DateExTool.dateTime2String(cal));
	}

	public static Calendar getExpiredTime(String sn) throws Exception {
		if (StringUtils.isEmpty(sn)) {
			return null;
		}
		String decryptKey = new String(decrypt(hex2byte(sn.getBytes()),
				PASSWORD_CRYPT_KEY.getBytes()));
		// System.out.println("解密后注册码:" + decryptKey);
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(Long.valueOf(decryptKey.substring(
				decryptKey.lastIndexOf(":") + 1, decryptKey.length())));
		// DateFormat df = DateFormat.getDateInstance();
		// System.out.println("解密后有效信息:" + df.format(c.getTime()));
		return c;
	}

	public static String getMachineNo() {
		return getMac();
	}

	public static void checkRegisterNo(String registerNo, String machineNo,
			String clientName) {
		if (!TIYI_LOCAL.equals(machineNo)
				&& !StringUtils.equals(getMac(), machineNo)) {
			Pe.raise("无效的机器码：" + machineNo);
		}

		if (!isRegisterNo(registerNo, clientName)) {
			Pe.raise("无效的注册码");
		}
	}

	public static String generate(String machineNo, Calendar date,
			String clientName) {
		byte[] b = null;
		try {
			machineNo += ":" + clientName + ":"
					+ String.valueOf(date.getTimeInMillis());
			System.out.println(machineNo);
			b = encrypt(machineNo.getBytes(), PASSWORD_CRYPT_KEY.getBytes());
		} catch (Exception e) {

			e.printStackTrace();
		}

		String encryptKey = byte2hex(b);
		return encryptKey;
	}

	private static final String PASSWORD_CRYPT_KEY = "johnlock";// 密钥
	private final static String DES = "DES";// DES算法名称

	public static byte[] encrypt(byte[] src, byte[] key) throws Exception {
		// DES算法要求有一个可信任的随机数源
		SecureRandom sr = new SecureRandom();
		// 从原始密匙数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);
		// 创建一个密匙工厂，然后用它把DESKeySpec转换成
		// 一个SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);
		// Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance(DES);
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
		// 现在，获取数据并加密
		// 正式执行加密操作
		return cipher.doFinal(src);
	}

	public static byte[] decrypt(byte[] src, byte[] key) throws Exception {
		// DES算法要求有一个可信任的随机数源
		SecureRandom sr = new SecureRandom();
		// 从原始密匙数据创建一个DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);
		// 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
		// 一个SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);
		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance(DES);
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
		// 现在，获取数据并解密
		// 正式执行解密操作
		return cipher.doFinal(src);
	}

	public static final String TIYI_LOCAL = "9F1496EC2398";

	public static boolean isRegisterNo(String registerNo, String clientName) {
		if (StringUtils.isEmpty(registerNo)) {
			return false;
		}
		try {
			String decryptKey = new String(decrypt(
					hex2byte(registerNo.getBytes()),
					PASSWORD_CRYPT_KEY.getBytes()));
			String machineNo = decryptKey.substring(0, decryptKey.indexOf(":"));
			String getCn = decryptKey.substring(decryptKey.indexOf(":") + 1,
					decryptKey.lastIndexOf(":"));
			// System.out
			// .println("machineNo=" + machineNo + " getmac=" + getMac());
			// System.out.println("clientName=" + clientName + " cn=" + getCn);
			if (machineNo.equals(TIYI_LOCAL)
					|| (machineNo.equals(getMac()) && getCn.equals(clientName))) {
				return true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();

		}
		return false;
	}
}
