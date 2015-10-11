package pula.sys;

import puerta.system.flag.OrderNoMutex;

public class BhzqConstants {

	public static final int GENDER_MALE = 1;
	public static final int GENDER_FEMALE = 2;

	public static final OrderNoMutex TICKET = new OrderNoMutex("TICKET");
	public static final OrderNoMutex TICKET_ERROR = new OrderNoMutex(
			"TICKET_ERROR");
	public static final OrderNoMutex PURCHASE_NOTE = new OrderNoMutex(
			"PURCHASE_NOTE");
	public static final OrderNoMutex TRANSFER_ORDER = new OrderNoMutex(
			"TRANSFER_ORDER");
	public static final OrderNoMutex INVENTORY_SHEET = new OrderNoMutex(
			"INVENTORY_SHEET");

	public static final Integer AP_GROUP = 1;
	public static final Integer AP_USER = 2;
	public static final String SESSION_ROLE = "ROLE";
	public static final String SESSION_TYPES = "TYPES";
	public static final String SESSION_CAM_NO = "CAM_NO";
	public static final String SESSION_PRINTER = "PRINTER";

	public static final String ROLE_TEACH_DIRECTOR = "R010_TEACH_DIRECTOR";
	public static final String ROLE_HEADQUARTER = "R001_HEADQUARTER";
	public static final String ROLE_SALES_DIRECTOR = "R015_SALES_DIRECTOR";
	public static final String ROLE_SALES = "R020_SALES";

	public static final String ROLE_CEO = "R100_CEO";
	public static final String ROLE_ADMIN = "R900_ADMIN";

	public static final String SC_PART_GROUP = "P_PART";

	public static final String DRAWING_PRINT_SIZE = "P_DRAWING_PRINT_SIZE";

	// 数据来源，程序单，工序
	public static final int NEW = 1;
	public static final int INTERNAL_DESIGN_CHANGE = 2;
	public static final int CLIENT_DESIGN_CHNAGE = 3;
	public static final int ACCIDENT = 4;
	public static final int REPAIR = 5; // 修模

	public static final String ELECTRODE_LIMIT = "P_ELECTRODE_LIMIT";

	public static final String WS_USER = "WS_USER";
	public static final String WS_PASSWORD = "WS_PASSWORD";
	public static final String CLIENT_VERSION = "P_CLIENT_VERSION";
	public static final String PRINTER_VERSION = "CLIENT_VERSION";

	public static final String GROUP_MAIN = "P_MAIN";
	public static final String ALERTS_MAPPING = "ALERTS_MAPPING";
	public static final String FILE_TRIAL_PATH = "P_FILE_TRIAL_DIR";
	public static final String FILE_BOM_DIR = "P_FILE_BOM_DIR";
	public static final String MT_ELECTRODE = "005";
	public static final String MATERIAL_ELECTRODE_PREFIX = "05";
	public static final String MT_PART = "009";
	public static final String MATERIAL_PART_PREFIX = "09";
	public static final String PART_DINGGAN = "60";
	public static final Object PART_MOJIA = "1000";

	// pula

	public static final String FILE_TEACHER_DIR = "P_FILE_TEACHER_DIR";
	public static final String FILE_STUDENT_DIR = "P_FILE_STUDENT_DIR";
	public static final String FILE_STUDENT_WORK_DIR = "P_FILE_STUDENT_WORK_DIR";
	public static final String FILE_NOTICE_ICON_DIR = "P_NOTICE_ICON_DIR";
	public static final String FILE_TIMECOURSE_ICON_DIR = "P_TIMECOURSE_ICON_DIR";
	public static final String FILE_UPLOAD_DIR = "P_FILE_UPLOAD_DIR";
	public static final String SC_COURSE_CATEGORY = "SC_COURSE_CATEGORY";
	public static final String SC_MATERIAL_TYPE = "SC_MATERIAL_TYPE";
	public static final String SC_AUDITION_RESULT = "SC_AUDITION_RESULT";
	public static final String SC_GIFT_TYPE = "SC_GIFT_TYPE";

	public static final String SESSION_HEADQUARTER = "HEADQUARTER";
	public static final String SESSION_BRANCH = "BRANCH";
	public static final String FILE_MATERIAL_DIR = "P_FILE_MATERIAL_DIR";
	public static final Object STUDENT_SYNC = new byte[1];
	public static final Object ORDER_FORM_SYNC = new byte[1];
	public static final String SYS_USER_GROUP_SALESMEN = "101";
	public static final String FILE_GIFT_DIR = "P_FILE_GIFT_DIR";
	public static final String COUNTER_ORDER_FORM = "ORDER_FORM";
	public static final String COUNTER_STUDENT = "STUDENT";
	// 学生上过课
	public static final String POINTS_TAKE_CLASS = "P_POINTS_TAKE_CLASS";
	public static final String INTERFACE_CALL_SECRET = "P_INTERFACE_CALL_SECRET";
	public static final String POINTS_TAKE_GAME = "P_POINTS_TAKE_GAME";

}
