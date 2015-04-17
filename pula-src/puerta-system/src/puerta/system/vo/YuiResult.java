package puerta.system.vo;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import puerta.support.PaginationSupport;
import puerta.support.utils.WxlSugar;

public class YuiResult {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7381244871867005545L;

	private static final YuiResult EMPTY = _empty();

	private int recordsReturned, totalRecords, startIndex, pageSize;
	private long totalPages, pageIndex;
	private String sort, dir;

	private List<Object> records;

	public long getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(long totalPages) {
		this.totalPages = totalPages;
	}

	public long getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(long pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getRecordsReturned() {
		return recordsReturned;
	}

	public void setRecordsReturned(int recordsReturned) {
		this.recordsReturned = recordsReturned;
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
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

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public List<Object> getRecords() {
		return records;
	}

	public void setRecords(List<Object> records) {
		this.records = records;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static <T> YuiResult create(PaginationSupport<T> results,
			YuiResultMapper<T> mapper) {
		YuiResult yr = new YuiResult();
		List<Object> records = WxlSugar.newArrayList(results.getItems().size());
		for (T t : results.getItems()) {
			if (mapper == null) {
				records.add(t);

			} else {
				Map<String, Object> v = mapper.toMap(t);
				records.add(v);
			}
		}
		yr.records = records;
		yr.totalRecords = results.getTotalCount();
		yr.pageSize = results.getPageSize();
		yr.setStartIndex(results.getStartIndex());
		yr.setRecordsReturned(records.size());
		yr.totalPages = results.getTotalPageCount();
		yr.pageIndex = results.getCurrentPageNo();
		return yr;
	}

	public static <T> YuiResult create(List<T> results,
			YuiResultMapper<T> mapper) {
		YuiResult yr = new YuiResult();
		List<Object> records = WxlSugar.newArrayList(results.size());
		for (T t : results) {
			if (mapper == null) {
				records.add(t);

			} else {
				Map<String, Object> v = mapper.toMap(t);
				records.add(v);
			}
		}
		yr.records = records;
		yr.totalRecords = results.size();
		yr.pageSize = 0;
		yr.setStartIndex(0);
		yr.setRecordsReturned(records.size());
		yr.totalPages = 1;
		yr.pageIndex = 1;
		return yr;
	}

	private static YuiResult _empty() {
		YuiResult yr = new YuiResult();

		yr.records = Collections.emptyList();
		return yr;
	}

	public static YuiResult empty() {
		return EMPTY;
	}

	public static YuiResult create(PaginationSupport<MapBean> results) {
		YuiResult yr = new YuiResult();
		List<Object> records = WxlSugar.newArrayList(results.getItems().size());
		for (MapBean t : results.getItems()) {

			records.add(t);
		}
		yr.records = records;
		yr.totalRecords = results.getTotalCount();
		yr.pageSize = results.getPageSize();
		yr.setStartIndex(results.getStartIndex());
		yr.setRecordsReturned(records.size());
		yr.totalPages = results.getTotalPageCount();
		yr.pageIndex = results.getCurrentPageNo();
		return yr;
	}

}
