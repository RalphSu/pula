package pula.sys.tests;

import puerta.support.utils.CodeEngineer;
import pula.sys.domains.CourseTaskResult;

public class CodeTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// System.out.println(CodeEngineer.formBeanToDomain(CourseTaskPlan.class));
		System.out.println(CodeEngineer.update(CourseTaskResult.class));

	}
}
