package puerta.support.utils;

import java.net.NetworkInterface;
import java.security.SecureRandom;
import java.util.Enumeration;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class GetMac {

	public static void main(String[] args) {
		System.out.println("getMac=" + getMac());
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

				StringBuilder builder = new StringBuilder();
				for (byte b : mac) {
					builder.append(hexByte(b));
					builder.append("-");
				}
				builder.deleteCharAt(builder.length() - 1);

				// System.out.println(ni.getDisplayName() + "==>" + builder);
				return builder.toString();

			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return null;
	}

	private static String hexByte(byte b) {
		return Integer.toHexString(b);
	}


   
}
