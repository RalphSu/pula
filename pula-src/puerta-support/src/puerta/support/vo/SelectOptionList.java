/**
 * Created on 2009-12-1
 * WXL 2009
 * $Id$
 */
package puerta.support.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author tiyi
 * 
 */
public class SelectOptionList extends ArrayList<SelectOption> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8663206777484183450L;

	/**
	 * 
	 */
	public SelectOptionList() {
		super();

	}

	/**
	 * @param c
	 */
	public SelectOptionList(Collection<? extends SelectOption> c) {
		super(c);
	}

	public int value;

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * @param initialCapacity
	 */
	public SelectOptionList(int initialCapacity) {
		super(initialCapacity);
	}

	public SelectOptionList insertAll() {
		return SelectOption.insertAll(this, this.value);
	}

	/**
	 * @param i
	 * @param string
	 * @return
	 */
	public SelectOptionList replaceName(int i, String string) {
		if (i < 0 || i > this.size() - 1) {
			return null;
		}
		get(i).setName(string);
		return this;
	}

	public SelectOptionList makeSelected(int n) {
		for (SelectOption so : this) {
			so.setSelected(so.getId().equals(new Integer(n).toString()));
		}
		return this;
	}

	// transfer;
	public String toRadioButtons(String name) {
		return SelectOption.transferToRadios(name, this);
	}

	public String toCheckBoxs(String name) {
		return SelectOption.transferToCheckboxes(name, this);
	}

	public SelectOptionList insertAll(String n) {
		return this.insertAll().replaceName(0, n);
	}

	public String toSelect(String name) {
		return SelectOption.transferToSelect(name, this);
	}

	/**
	 * @param name
	 * @param event
	 * @return
	 */
	public String toRadioButtons(String name, String event) {
		return SelectOption.transferToRadios(name, this, event);
	}

	public static SelectOptionList createSingle(int v, String name) {
		SelectOptionList list = new SelectOptionList();
		SelectOption so = new SelectOption(v, name);
		list.add(so);
		return list;
	}

	public SelectOptionList removeLast() {
		int n = this.size();
		if (n > 0) {
			this.remove(n - 1);
		}
		return this;
	}

	public SelectOptionList reverse() {
		Collections.reverse(this);
		return this;
	}

	public SelectOptionList removeValue(Object value) {
		int idx = 0;

		for (SelectOption so : this) {
			if (StringUtils.equals(so.getId(), String.valueOf(value))) {
				break;
			}
			idx++;
		}
		this.remove(idx);
		return this;
	}

}
