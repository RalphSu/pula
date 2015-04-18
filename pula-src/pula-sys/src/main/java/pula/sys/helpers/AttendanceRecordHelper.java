/**
 * Created on 2007-11-12 下午09:06:46
 *
 * DiagCN.COM 2004-2006
 * $Id$
 */
package pula.sys.helpers;

import puerta.support.vo.SelectOption;
import puerta.support.vo.SelectOptionList;
import pula.sys.domains.AttendanceRecord;

/**
 * 
 * @author tiyi
 * 
 */
public class AttendanceRecordHelper {

	public static final int[] types = new int[] { AttendanceRecord.DATA_FROM_MANUAL,
		AttendanceRecord.DATA_FROM_MATCHINE };

	public static final String[] typeNames = new String[] { "手动", "设备"
	};

	public static SelectOptionList getTypes(int i) {
		return SelectOption.getList(i, types, typeNames);
		// return null;
	}

	/**
	 * @param type
	 * @return
	 */
	public static String getTypeName(int type) {
		return SelectOption.getName(type, types, typeNames);
	}

}
