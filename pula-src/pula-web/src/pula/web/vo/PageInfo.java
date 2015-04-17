package pula.web.vo;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PageInfo<T> {

	protected List<T> items;

	protected int totalCount, totalPageCount;

	private long currentPageNo;
	private boolean nextPage, previousPage;
	private int pageSize;

	public int getTotalPageCount() {
		return totalPageCount;
	}

	public void setTotalPageCount(int totalPageCount) {
		this.totalPageCount = totalPageCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public long getCurrentPageNo() {
		return currentPageNo;
	}

	public void setCurrentPageNo(long currentPage) {
		this.currentPageNo = currentPage;
	}

	public boolean isNextPage() {
		return nextPage;
	}

	public void setNextPage(boolean nextPage) {
		this.nextPage = nextPage;
	}

	public boolean isPreviousPage() {
		return previousPage;
	}

	public void setPreviousPage(boolean previousPage) {
		this.previousPage = previousPage;
	}

	// public <E> PageInfo<E> create(PaginationSupport<E> ps) {
	// PageInfo<E> e = new PageInfo<E>();
	// e.setCurrentPage(ps.getCurrentPageNo());
	// e.setItems(ps.getItems());
	// e.setNextPage(ps.hasNextPage());
	// e.setPreviousPage(ps.hasPreviousPage());
	// e.setTotalCount(ps.getTotalCount());
	//
	// return e;
	// }

}
