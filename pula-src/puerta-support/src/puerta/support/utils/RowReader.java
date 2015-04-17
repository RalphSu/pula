/**
 * Created on 2010-2-1
 * WXL 2009
 * $Id$
 */
package puerta.support.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @author tiyi
 * 
 */
public class RowReader {

	private List<Object[]> _objsList;
	int row;
	int col;

	public RowReader(List<Object[]> objsList) {
		this._objsList = objsList;
		row = 0;
	}

	public boolean hasNextRow() {
		return row < _objsList.size();
	}

	public void nextRow() {
		row++;
		col = 0;
	}

	@SuppressWarnings("unchecked")
	public <E> E next() {
		return (E) _objsList.get(row)[col++];
	}

	public Object[] fullRow() {

		return _objsList.get(row);
	}

	/**
	 * @param objsList
	 * @return
	 */
	public static RowReader create(List<Object[]> objsList) {
		return new RowReader(objsList);
	}

	/**
	 * @param objs
	 * @return
	 */
	public static RowReader createSingle(Object[] objs) {
		if (objs == null) {
			List<Object[]> ls = Collections.emptyList();
			return new RowReader(ls);
		}
		List<Object[]> objsList = new ArrayList<Object[]>(1);
		objsList.add(objs);
		return new RowReader(objsList);
	}
}
