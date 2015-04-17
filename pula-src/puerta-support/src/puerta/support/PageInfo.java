/*
 * Created on 2004-10-29
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package puerta.support;

import org.apache.log4j.Logger;

/**
 * 
 * @author tiyi 2004-10-29
 */
public class PageInfo {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(PageInfo.class);

	private static int PAGE_SIZE = PaginationSupport.PAGESIZE;

	protected int pageSize, pageNo;

	protected int startIndex;

	public PageInfo() {
		this.pageSize = PAGE_SIZE;
	}

	public PageInfo(int pNo) {
		pageSize = PAGE_SIZE;
		this.pageNo = pNo;

		if (this.pageNo <= 0) {
			this.pageNo = 1;
		}

		this.startIndex = PaginationSupport.convertFromPageToStartIndex(
				this.pageNo, pageSize);

		logger.debug("startIndex=" + startIndex);
	}

	public PageInfo(int pNo, int pageSize) {

		this.pageSize = pageSize;
		this.pageNo = pNo;

		if (this.pageNo <= 0) {
			this.pageNo = 1;
		}

		this.startIndex = PaginationSupport.convertFromPageToStartIndex(
				this.pageNo, pageSize);
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public static PageInfo createStart(int pageNo2) {
		PageInfo pi = new PageInfo();
		pi.startIndex = pageNo2;
		return pi;
	}

}
