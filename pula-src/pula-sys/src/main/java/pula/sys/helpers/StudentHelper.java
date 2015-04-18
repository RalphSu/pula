package pula.sys.helpers;

import puerta.support.utils.MD5;
import puerta.support.utils.StringTool;
import pula.sys.domains.Student;

public class StudentHelper {

	public static String makePassword(String password) {
		return MD5.GetMD5String("student@" + password);
	}

	public static String makeNo(String branchNo, int inc) {
		return new StringBuilder(branchNo).append(StringTool.fillZero(inc, 5))
				.toString();
	}

	public static String buildRefId(long id) {
		return "S:" + id;
	}

	public static String buildComments(Student ef) {
		return new StringBuilder().append("学员:").append(ef.getNo()).append(" ")
				.append(ef.getName()).toString();
	}

	public static String buildFileRefId(long id, String attachmentKey) {
		return id + ":" + attachmentKey;
	}

}
