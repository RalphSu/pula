/*
 * Created on 2005-7-25
 *$Id: Arith.java,v 1.1 2006/12/05 09:33:37 tiyi Exp $
 * DiagCN.com (2003-2005)
 */
package puerta.support.utils;

import java.math.BigDecimal;

/**
 * @author tiyi 2005-7-25 21:08:59
 */
/**
 * 
 * 由于Java的简单类型不能够精确的对浮点数进行运算，这个工具类提供精
 * 
 * 确的浮点数运算，包括加减乘除和四舍五入。
 * 
 */

public class Arith {

	// 默认除法运算精度

	private static final int DEF_DIV_SCALE = 10;

	// 这个类不能实例化

	private Arith() {

	}

	/**
	 * 
	 * 提供精确的加法运算。
	 * 
	 * @param v1
	 *            被加数
	 * 
	 * @param v2
	 *            加数
	 * @param tax
	 * 
	 * @return 两个参数的和
	 * 
	 */

	public static double add(double... v1) {
		if (v1.length > 0) {
			BigDecimal b1 = new BigDecimal(Double.toString(v1[0]));

			for (int i = 1; i < v1.length; i++) {
				BigDecimal b2 = new BigDecimal(Double.toString(v1[i]));
				b1 = b1.add(b2);
			}

			return b1.doubleValue();
		}
		return 0d;
	}

	/**
	 * 
	 * 提供精确的减法运算。
	 * 
	 * @param v1
	 *            被减数
	 * 
	 * @param v2
	 *            减数
	 * 
	 * @return 两个参数的差
	 * 
	 */

	public static double sub(double v1, double v2) {

		BigDecimal b1 = new BigDecimal(Double.toString(v1));

		BigDecimal b2 = new BigDecimal(Double.toString(v2));

		return b1.subtract(b2).doubleValue();

	}

	/**
	 * 
	 * 提供精确的乘法运算。
	 * 
	 * @param v1
	 *            被乘数
	 * 
	 * @param v2
	 *            乘数
	 * 
	 * @return 两个参数的积
	 * 
	 */

	public static double mul(double v1, double v2) {

		BigDecimal b1 = new BigDecimal(Double.toString(v1));

		BigDecimal b2 = new BigDecimal(Double.toString(v2));

		return b1.multiply(b2).doubleValue();

	}

	/**
	 * 
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
	 * 
	 * 小数点以后10位，以后的数字四舍五入。
	 * 
	 * @param v1
	 *            被除数
	 * 
	 * @param v2
	 *            除数
	 * 
	 * @return 两个参数的商
	 * 
	 */

	public static double div(double v1, double v2) {

		return div(v1, v2, DEF_DIV_SCALE);

	}

	/**
	 * 
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
	 * 
	 * 定精度，以后的数字四舍五入。
	 * 
	 * @param v1
	 *            被除数
	 * 
	 * @param v2
	 *            除数
	 * 
	 * @param scale
	 *            表示表示需要精确到小数点以后几位。
	 * 
	 * @return 两个参数的商
	 * 
	 */

	public static double div(double v1, double v2, int scale) {

		if (scale < 0) {

			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");

		}

		BigDecimal b1 = new BigDecimal(Double.toString(v1));

		BigDecimal b2 = new BigDecimal(Double.toString(v2));

		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();

	}

	/**
	 * 
	 * 提供精确的小数位四舍五入处理。
	 * 
	 * @param v
	 *            需要四舍五入的数字
	 * 
	 * @param scale
	 *            小数点后保留几位
	 * 
	 * @return 四舍五入后的结果
	 * 
	 */

	public static double round(double v, int scale) {

		if (scale < 0) {

			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");

		}

		BigDecimal b = new BigDecimal(Double.toString(v));

		BigDecimal one = new BigDecimal("1");

		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();

	}

	/**
	 * @param ds
	 * @return
	 */
	public static double sumAll(double[] ds) {
		double t = 0d;

		for (double d : ds) {
			t = add(t, d);
		}

		return t;
	}

	/**
	 * @param i
	 * @param realWorkMinute
	 * @return
	 */
	public static double percent(double i, double i2, int len) {
		if (i2 == 0d) {
			return 0;
		}
		return div(mul(i, 100), i2, len);
	}

	/**
	 * @param workHours
	 * @param xishuofclient
	 * @param quantity
	 * @return
	 */
	public static double mul(double... ds) {
		double res = 1d;
		for (double d : ds) {
			res = Arith.mul(d, res);
		}
		return res;
	}

}
