/*
 * Created on 2005-3-17
 *$Id: SelectOption.java,v 1.1 2006/12/05 09:33:36 tiyi Exp $
 * DiagCN.com (2003-2005)
 */
package puerta.support.vo;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import puerta.support.utils.StringTool;

/**
 * @author ibm 2005-3-17 23:56:56
 */
public class SelectOption {

	private String id;

	private String name;

	private boolean selected;

	/**
	 * 
	 */
	public SelectOption() {
		super();

	}

	public SelectOption(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public SelectOption(int type, String string) {
		this.id = String.valueOf(type);
		this.name = string;
	}

	/**
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param string
	 */
	public void setId(String string) {
		id = string;
	}

	/**
	 * @param string
	 */
	public void setName(String string) {
		name = string;
	}

	/**
	 * @return
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param b
	 */
	public void setSelected(boolean b) {
		selected = b;
	}

	public static String transferToRadios(String objName,
			List<SelectOption> selectOptions) {
		StringBuffer sb = new StringBuffer();
		String cls = StringUtils.remove(objName, '.');
		for (Iterator<SelectOption> iter = selectOptions.iterator(); iter
				.hasNext();) {
			SelectOption so = iter.next();
			sb.append("<label for='").append(objName).append(so.getId())
					.append("'>");
			sb.append("<input type='radio' name='").append(objName)
					.append("' ");
			sb.append("value='").append(so.getId()).append("' ");
			sb.append("id='").append(objName).append(so.getId()).append("' ");
			if (so.isSelected()) {
				sb.append(" checked ");
			}
			sb.append(" class='" + cls + "'>").append(so.getName())
					.append("</label>");
		}

		return sb.toString();
	}

	public static String transferToRadios(String objName,
			List<SelectOption> selectOptions, String event) {
		StringBuffer sb = new StringBuffer();
		for (Iterator<SelectOption> iter = selectOptions.iterator(); iter
				.hasNext();) {
			SelectOption so = iter.next();
			sb.append("<label for='").append(objName).append(so.getId())
					.append("'>");
			sb.append("<input type='radio' name='").append(objName)
					.append("' ");
			sb.append("value='").append(so.getId()).append("' ");
			sb.append("id='").append(objName).append(so.getId()).append("' ");
			if (so.isSelected()) {
				sb.append(" checked ");
			}
			sb.append("onclick=\"").append(event).append("\" ");
			sb.append(">").append(so.getName()).append("</label>");
		}

		return sb.toString();
	}

	public static String transferToDisabledRadios(String objName,
			SelectOptionList selectOptions) {
		StringBuffer sb = new StringBuffer();
		for (Iterator<SelectOption> iter = selectOptions.iterator(); iter
				.hasNext();) {
			SelectOption so = iter.next();
			sb.append("<label for='r").append(so.getId()).append("'>");
			sb.append("<input type='radio' name='").append(objName)
					.append("' ");
			sb.append("value='").append(so.getId()).append("' ");
			sb.append("id='r").append(so.getId()).append("' ");
			if (so.isSelected()) {
				sb.append(" checked ");
			}
			sb.append(" disabled >").append(so.getName()).append("</label>");
		}

		return sb.toString();
	}

	public static String transferToSelectedName(SelectOptionList selectOptions) {

		for (Iterator<SelectOption> iter = selectOptions.iterator(); iter
				.hasNext();) {
			SelectOption so = iter.next();
			if (so.isSelected()) {
				return so.getName();
			}

		}

		return StringUtils.EMPTY;
	}

	public static String transferToCheckboxes(String objName,
			SelectOptionList selectOptions) {
		StringBuffer sb = new StringBuffer();
		for (Iterator<SelectOption> iter = selectOptions.iterator(); iter
				.hasNext();) {
			SelectOption so = iter.next();
			sb.append("<label for='c").append(so.getId()).append("'>");
			sb.append("<input type='checkbox' name='").append(objName)
					.append("' ");
			sb.append("value='").append(so.getId()).append("' ");
			sb.append("id='c").append(so.getId()).append("' ");
			if (so.isSelected()) {
				sb.append(" checked ");
			}
			sb.append(">").append(so.getName()).append("</label>");
		}

		return sb.toString();
	}

	public static String transferToDisabledCheckBoxes(String objName,
			SelectOptionList selectOptions) {
		StringBuffer sb = new StringBuffer();
		for (Iterator<SelectOption> iter = selectOptions.iterator(); iter
				.hasNext();) {
			SelectOption so = iter.next();
			sb.append("<label for='c").append(so.getId()).append("'>");
			sb.append("<input type='checkbox' name='").append(objName)
					.append("' ");
			sb.append("value='").append(so.getId()).append("' ");
			sb.append("id='c").append(so.getId()).append("' ");
			if (so.isSelected()) {
				sb.append(" checked ");
			}
			sb.append(" disabled >").append(so.getName()).append("</label>");
		}

		return sb.toString();
	}

	public static void setChargingModeSelected(String selected,
			SelectOptionList list) {
		SelectOptionList l = list;
		for (Iterator<SelectOption> iter = l.iterator(); iter.hasNext();) {
			SelectOption so = (SelectOption) iter.next();
			if (StringUtils.equals(so.getId(), selected)) {
				so.setSelected(true);
				break;
			}
		}

	}

	/**
	 * @param string
	 * @param ls
	 * @return
	 */
	public static String transferToSelect(String objName,
			List<SelectOption> selectOptions) {
		StringBuffer sb = new StringBuffer();
		sb.append("<SELECT name='").append(objName).append("' id='")
				.append(objName).append("'>");

		for (Iterator<SelectOption> iter = selectOptions.iterator(); iter
				.hasNext();) {
			SelectOption so = iter.next();
			sb.append("<OPTION value='").append(so.getId()).append("'");
			if (so.isSelected()) {
				sb.append(" selected ");
			}

			sb.append(">").append(so.getName()).append("</OPTION>");

		}
		sb.append("</SELECT>");
		return sb.toString();
	}

	/**
	 * @param i
	 * @param cancel_types
	 * @return
	 */
	public static <T> SelectOptionList  getList(Object i, T[] cancel_types,
			SelectNameFetch<T> fetcher) {
		SelectOptionList list = new SelectOptionList();
		for (T n : cancel_types) {

			SelectOption so = new SelectOption(n.toString(), fetcher.getName(n));
			list.add(so);

			so.setSelected(n.equals(i));
		}

		return list;
	}

	/**
	 * @param i
	 * @param types
	 * @param types_name
	 * @return
	 */
	public static SelectOptionList getList(int selectedValue, int[] types,
			String[] types_name) {
		SelectOptionList list = new SelectOptionList();
		list.setValue(selectedValue);
		for (int i = 0; i < types.length; i++) {

			SelectOption so = new SelectOption(types[i], types_name[i]);
			list.add(so);

			so.setSelected(types[i] == selectedValue);
		}

		return list;
	}

	public static SelectOptionList getList(Object selectedValue,
			Object[] types, Object[] types_name) {
		SelectOptionList list = new SelectOptionList();
		for (int i = 0; i < types.length; i++) {

			SelectOption so = new SelectOption(types[i].toString(),
					types_name[i].toString());
			list.add(so);

			so.setSelected(types[i].equals(selectedValue));
		}

		return list;
	}

	/**
	 * @param type
	 * @param types
	 * @param types_name
	 */
	public static String getName(int selectedValue, int[] types,
			String[] types_name) {
		int index = ArrayUtils.indexOf(types, selectedValue);
		if (index < 0 || index > types_name.length) {
			return "UNKNOWN";
		}

		return types_name[index];

	}

	/**
	 * @param i
	 * @param without
	 * @param types
	 * @param types_name
	 * @return
	 */
	public static SelectOptionList getList(int selectedValue, int without,
			int[] types, String[] types_name) {
		SelectOptionList list = new SelectOptionList();
		for (int i = 0; i < types.length; i++) {
			if (i == without)
				continue;
			SelectOption so = new SelectOption(types[i], types_name[i]);
			list.add(so);

			so.setSelected(types[i] == selectedValue);
		}

		return list;
	}

	/**
	 * @param year
	 * @param i
	 * @return
	 */
	public static SelectOptionList getYear(int year, int count) {
		SelectOptionList lst = new SelectOptionList(10);
		for (int i = year - count; i < year + count; i++) {
			SelectOption so = new SelectOption(i, String.valueOf(i));
			so.setSelected(i == year);
			lst.add(so);
		}

		return lst;
	}

	/**
	 * @param month
	 * @return
	 */
	public static SelectOptionList getMonth(int month, int startFrom) {
		SelectOptionList lst = new SelectOptionList(12);
		int lastIndex = 11;
		int i = 0;
		if (startFrom == 1) {
			i = 1;
			lastIndex = 12;
		}
		for (; i <= lastIndex; i++) {
			SelectOption so = new SelectOption(i, String.valueOf(i));
			so.setSelected(i == month);
			lst.add(so);
		}
		return lst;
	}

	public static SelectOptionList getList(int selected, int from, int to) {
		SelectOptionList lst = new SelectOptionList(to - from + 1);

		for (int i = from; i <= to; i++) {
			SelectOption so = new SelectOption(i, String.valueOf(i));
			so.setSelected(i == selected);
			lst.add(so);
		}
		return lst;
	}

	/**
	 * @param i
	 * @param j
	 * @param k
	 * @return
	 */
	public static SelectOptionList getNumbers(int from, int to, int selected) {
		String str = String.valueOf(to);
		int strLen = str.length();
		SelectOptionList res = new SelectOptionList();
		for (int i = from; i <= to; i++) {
			SelectOption so = new SelectOption(i,
					StringTool.fillZero(i, strLen));
			so.setSelected(i == selected);
			res.add(so);
		}
		return res;
	}

	/**
	 * @param objsList
	 * @return
	 */
	public static SelectOptionList getList(List<Object[]> objsList) {
		SelectOptionList soList = new SelectOptionList();
		for (Object[] objs : objsList) {
			String no = (String) objs[0];
			String name = (String) objs[1];

			soList.add(new SelectOption(no, name));

		}
		return soList;
	}

	/**
	 * @param urlTypes
	 * @return
	 */
	public static SelectOptionList insertAll(SelectOptionList urlTypes, int n) {
		SelectOption so = new SelectOption(0, "-");
		so.setSelected(0 == n);
		SelectOptionList ret = new SelectOptionList(urlTypes.size() + 1);
		ret.add(so);
		ret.addAll(urlTypes);
		return ret;
	}

	/**
	 * @param n
	 * @param reportsType
	 * @return
	 */
	public static SelectOptionList getList(int n, String[] types_name) {
		SelectOptionList list = new SelectOptionList();
		for (int i = 0; i < types_name.length; i++) {

			SelectOption so = new SelectOption(i, types_name[i]);
			list.add(so);
			so.setSelected(i == n);
		}

		return list;
	}

	/**
	 * @param trim
	 * @param langs
	 * @param langNames
	 * @return
	 */
	public static String getName(String select, String[] langs,
			String[] langNames) {
		int i = -1;
		int index = 0;
		for (String s : langs) {
			if (select.equals(s)) {
				i = index;
				break;
			}
			index++;
		}
		if (i >= 0) {
			return langNames[i];
		}
		return select;
	}
}
