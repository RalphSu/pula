package puerta.system.helper;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import puerta.support.vo.SelectOption;
import puerta.support.vo.SelectOptionList;
import puerta.system.po.AppField;

/**
 * 
 * @author tiyi
 * 
 */
public class AppFieldHelper {

	/**
	 * @param list
	 * @return
	 */
	public static SelectOptionList getSelectOptions(List<AppField> list) {
		SelectOptionList lst = new SelectOptionList();
		for (int i = 0; i < list.size(); i++) {
			SelectOption so = new SelectOption();
			if (i == 0) {
				so.setSelected(true);
			} else {
				so.setSelected(false);
			}

			AppField as = list.get(i);

			so.setId(as.getNo());
			so.setName(as.getName());

			lst.add(so);
		}
		return lst;
	}

	public static SelectOptionList getSelectOptions(List<AppField> list,
			String selected) {
		SelectOptionList lst = new SelectOptionList();
		for (int i = 0; i < list.size(); i++) {

			AppField as = list.get(i);
			SelectOption so = new SelectOption();
			if (StringUtils.equals(selected, as.getNo())
					|| (selected == null && i == 0)) {
				so.setSelected(true);
			} else {
				so.setSelected(false);
			}

			so.setId(as.getNo());
			so.setName(as.getName());

			lst.add(so);
		}
		return lst;
	}

}
