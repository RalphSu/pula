package puerta.system.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.util.WebUtils;

import puerta.support.vo.SelectOption;
import puerta.system.helper.ParameterHelper;
import puerta.system.po.Parameter;

public class ParameterEditor {

	Locale locale = null;
	MessageSource messageSource;

	public boolean isHandleEditor(Parameter p) {
		switch (p.getParamType()) {
		case Parameter.TYPE_BOOL:
		case Parameter.TYPE_DICT:
			return true;
		}

		return false;
	}

	public String getEditor(Parameter p) {
		if ((Parameter.TYPE_DICT == p.getParamType())) {
			String[] dicts = StringUtils.split(p.getMask(), ",");
			List<SelectOption> ls = new ArrayList<SelectOption>();
			for (int i = 0; i < dicts.length; i++) {
				String[] items = StringUtils.split(dicts[i], "-");
				if (items.length < 2)
					continue;
				SelectOption so = new SelectOption();

				so.setId(items[1]);
				so.setName(items[0]);
				so.setSelected(StringUtils.equals(items[1], p.getValue()));
				ls.add(so);
			}

			return SelectOption.transferToSelect("values", ls);
		} else if (Parameter.TYPE_BOOL == p.getParamType()) {
			List<SelectOption> ls = new ArrayList<SelectOption>();

			SelectOption so = new SelectOption(ParameterHelper.YES,
					getMessage("Common.Yes"));
			so.setSelected(StringUtils.equals(so.getId(), p.getValue()));
			ls.add(so);
			so = new SelectOption(ParameterHelper.NO, getMessage("Common.No"));
			so.setSelected(StringUtils.equals(so.getId(), p.getValue()));
			ls.add(so);
			return SelectOption.transferToSelect("values", ls);
		}

		return null;
	}

	private String getMessage(String string) {
		return messageSource.getMessage(string, null, this.locale);
	}

	public static ParameterEditor create(HttpServletRequest request,
			MessageSource messageSource) {
		Locale locale = (Locale) WebUtils.getSessionAttribute(request,
				SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);

		if (locale == null) {
			locale = Locale.getDefault();
		}

		ParameterEditor pe = new ParameterEditor();
		pe.locale = locale;
		pe.messageSource = messageSource;
		return pe;

	}
}
