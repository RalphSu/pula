/**
 * Created on 2008-3-21 上午11:08:03
 *
 * NILVAR 2008
 * $Id$
 */
package puerta.support;

import java.util.List;

/**
 * 
 * @author tiyi
 * 
 */
public class PaginationSupport<T> {

	public final static int PAGESIZE = 100;

	protected int pageSize = PAGESIZE;

	protected List<T> items;

	protected int totalCount;

	protected int[] indexes = new int[0];

	protected int startIndex = 0;
	
	
	public PaginationSupport(){
		
	}

	public PaginationSupport(List<T> items, int totalCount) {
		setPageSize(PAGESIZE);
		setTotalCount(totalCount);
		setItems(items);
		setStartIndex(0);
	}

	public PaginationSupport(List<T> items, int totalCount, int startIndex) {
		setPageSize(PAGESIZE);
		setTotalCount(totalCount);
		setItems(items);
		setStartIndex(startIndex);
	}

	public PaginationSupport(List<T> items, int totalCount, int pageSize,
			int startIndex) {
		setPageSize(pageSize);
		setTotalCount(totalCount);
		setItems(items);
		setStartIndex(startIndex);
	}

	/** */
	/**
	 * 将页码转换为列表的startIndex，页大小为默认大小
	 */
	public static int convertFromPageToStartIndex(int pageNo) {
		return (pageNo - 1) * PAGESIZE;
	}

	/** */
	/**
	 * 将页码转换为列表的startIndex
	 */
	public static int convertFromPageToStartIndex(int pageNo, int pageSize) {
		return (pageNo - 1) * pageSize;
	}

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalCount() {
		return totalCount;
	}

	/** */
	/**
	 * 设置数据总数，并计算各页起始位置
	 */
	public void setTotalCount(int totalCount) {
		if (totalCount > 0) {
			this.totalCount = totalCount;
			int count = (int) totalCount / pageSize;
			if (totalCount % pageSize > 0)
				count++;
			indexes = new int[count];
			for (int i = 0; i < count; i++) {
				indexes[i] = pageSize * i;
			}
		} else {
			this.totalCount = 0;
		}
	}

	public int[] getIndexes() {
		return indexes;
	}

	public void setIndexes(int[] indexes) {
		this.indexes = indexes;
	}

	public int getStartIndex() {
		return startIndex;
	}

	/** */
	/**
	 * 设置当前起始位置
	 */
	public void setStartIndex(int startIndex) {
		if (totalCount <= 0)
			this.startIndex = 0;
		else if (startIndex >= totalCount)
			this.startIndex = indexes[indexes.length - 1];
		else if (startIndex < 0)
			this.startIndex = 0;
		else {
			this.startIndex = indexes[startIndex / pageSize];
		}
	}

	/** */
	/**
	 * 获得下页起始位置
	 */
	public int getNextIndex() {
		int nextIndex = getStartIndex() + pageSize;
		if (nextIndex >= totalCount)
			return getStartIndex();
		else
			return nextIndex;
	}

	/** */
	/**
	 * 获得上页起始位置
	 */
	public int getPreviousIndex() {
		int previousIndex = getStartIndex() - pageSize;
		if (previousIndex < 0)
			return 0;
		else
			return previousIndex;
	}

	/** */
	/**
	 * 取总页数.
	 */
	public long getTotalPageCount() {
		if (totalCount % pageSize == 0)
			return totalCount / pageSize;
		else
			return totalCount / pageSize + 1;
	}

	/** */
	/**
	 * 取该页当前页码,页码从1开始.
	 */
	public long getCurrentPageNo() {
		long l = startIndex / pageSize + 1;
		// System.out.println("l=" + l);
		return l;
	}

	/** */
	/**
	 * 该页是否有下一页.
	 */
	public boolean hasNextPage() {
		return this.getCurrentPageNo() <= this.getTotalPageCount() - 1;
	}

	/** */
	/**
	 * 该页是否有上一页.
	 */
	public boolean hasPreviousPage() {
		return this.getCurrentPageNo() > 1;
	}

	public <E> PaginationSupport<E> transfers(List<E> items2) {
		return new PaginationSupport<E>(items2, totalCount, PAGESIZE,
				startIndex);
	}

}
