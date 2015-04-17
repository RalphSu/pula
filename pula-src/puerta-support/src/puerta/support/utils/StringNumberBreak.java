/**
 * Created on 2009-12-28
 * WXL 2009
 * $Id$
 */
package puerta.support.utils;

import org.apache.commons.lang.math.NumberUtils;

/**
 * 
 * @author tiyi
 * 
 */
public class StringNumberBreak {

	private String prefix, suffix;
	private int number, numberLength;

	/**
	 * @param ret
	 * @param substring
	 */
	public StringNumberBreak(String ret, String substring) {
		this.suffix = ret;
		this.prefix = substring;

		this.numberLength = this.suffix.length();
		this.number = NumberUtils.toInt(suffix);
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getNumberLength() {
		return numberLength;
	}

	public void setNumberLength(int length) {
		this.numberLength = length;
	}

	/**
	 * @return
	 */
	public String getNextNo() {
		return this.prefix
				+ StringTool.fillZero(this.number+1, this.numberLength);
	}

	@Override
	public String toString() {
		return "prefix=" + this.prefix + " ; suffix=" + this.suffix
				+ " ; number=" + this.number + " ;len=" + this.numberLength;
	}

}
