package puerta.support.utils;

import java.beans.PropertyDescriptor;
import java.util.Calendar;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

public class CodeEngineer {
	public static final String[] IGNORE_PROPERTIES = new String[] { "class",
			"removed", "createdTime", "enabled", "updatedTime" };

	public static StringBuilder formBeanToDomain(Class<?> clz) {

		PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(clz);
		StringBuilder header = new StringBuilder(1024);
		StringBuilder importSb = new StringBuilder();
		StringBuilder classSb = new StringBuilder();

		String packageName = clz.getPackage().getName();
		header.append("package ").append(changePackage(packageName))
				.append(";\n");
		importSb.append("import ").append(clz.getName()).append(";\n");
		classSb.append("public class ").append(clz.getSimpleName())
				.append("Form extends ").append(clz.getSimpleName())
				.append(" {\n");

		StringBuilder fieldsb = new StringBuilder();

		StringBuilder sb = new StringBuilder();
		sb.append("public ").append(clz.getSimpleName()).append(" to")
				.append(clz.getSimpleName()).append("(){\n\t")
				.append(clz.getSimpleName()).append(" obj = new ")
				.append(clz.getSimpleName()).append("();\n");
		boolean date = false;
		boolean use_date = false;
		for (PropertyDescriptor pd : pds) {
			String name = pd.getName();

			if (ArrayUtils.contains(IGNORE_PROPERTIES, name)) {
				continue;
			}

			name = StringUtils.capitalize(name);
			date = false;
			if (pd.getReadMethod() == null) {
				System.err.println(name);
				continue;
			}
			Class<?> method_clz = pd.getReadMethod().getReturnType();
			String dateField = null;
			if (method_clz.equals(Calendar.class)) {

				dateField = StringUtils.lowerCase(name.substring(0, 1))
						+ StringUtils.right(name, name.length() - 1);

				fieldsb.append("\t private String ").append(dateField)
						.append("Text;\n");
				date = true;
			}

			if (pd.getWriteMethod() == null) {
				System.err.println(pd.getDisplayName());
				continue;
			}
			if (date) {
				use_date = true;
				sb.append("\tobj.").append(pd.getWriteMethod().getName())
						.append("( DateExTool.getDate( this.")
						.append(dateField);
				sb.append("Text) );\n");
			} else {
				sb.append("\tobj.").append(pd.getWriteMethod().getName())
						.append("( this.").append(pd.getReadMethod().getName());
				sb.append("() );\n");

			}

		}

		sb.append("\treturn obj;\n}\n \n}");

		if (use_date) {
			importSb.append("import puerta.support.utils.DateExTool;\n");
		}

		return header.append(importSb.toString()).append(classSb.toString())
				.append(fieldsb.toString()).append(sb.toString());

	}

	private static String changePackage(String packageName) {

		return StringUtils.replace(packageName, "domains", "forms");

	}

	public static StringBuilder update(Class<?> clz) {

		PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(clz);
		StringBuilder sb = new StringBuilder(1024);
		sb.append("\t").append(clz.getSimpleName())
				.append(" po = this.findById(rt.getId());\n");
		for (PropertyDescriptor pd : pds) {
			String name = pd.getName();
			if (ArrayUtils.contains(IGNORE_PROPERTIES, name)) {
				continue;
			}
			if (pd.getWriteMethod() == null) {
				System.err.println(pd.getDisplayName());
				continue;
			}
			if (pd.getWriteMethod() == null || pd.getReadMethod() == null) {
				System.err.println(pd.getDisplayName());
				continue;
			}

			// pd.name = StringUtils.capitalize(name);
			sb.append("\tpo.").append(pd.getWriteMethod().getName())
					.append("( rt.").append(pd.getReadMethod().getName())
					.append("() );\n");

		}
		sb.append("\t_update(po);\n\treturn po;\n");

		return sb;

	}

	public static StringBuilder daoToDomain(Class<?> clz) {
		// PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(clz);
		StringBuilder header = new StringBuilder(1024);
		StringBuilder importSb = new StringBuilder();
		StringBuilder classSb = new StringBuilder();

		String packageName = clz.getPackage().getName();
		header.append("package ")
				.append(StringUtils.replace(packageName, "domains", "dao"))
				.append(";\n");
		importSb.append("import ").append(clz.getName()).append(";\n");
		importSb.append("import puerta.support.dao.BaseDao;\n");
		classSb.append("public interface ").append(clz.getSimpleName())
				.append("Dao extends BaseDao<").append(clz.getSimpleName())
				.append(",Long> {\n");

		StringBuilder fieldsb = new StringBuilder();

		StringBuilder sb = new StringBuilder();

		sb.append("\n}");

		// importSb.append("import ").append(clz.getName()).append("\n");

		return header.append(importSb.toString()).append(classSb.toString())
				.append(fieldsb.toString()).append(sb.toString());

	}
}
