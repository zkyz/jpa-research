package kr.or.knia.config.mybatis.page.impl;

import java.util.ArrayList;
import java.util.List;

import kr.or.knia.config.mybatis.page.Pageable;

public class Pagination implements Pageable {
	private boolean enabled = true;
	private int page = 0;
	private int rowPerPage = 15;
	private int pageCountPerList = 5;
	private String sort;
	private int rowCount = 0;
	private List<Integer> pages;

	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public Integer getTotalPage() {
		return new Double(Math.ceil((double)getRowCount() / getRowPerPage())).intValue();
	}

	public Integer getPrevPage() {
		int startPage = ((getPage() - 1) / getPageCountPerList() * getPageCountPerList()) + 1;
		return getPage() > getPageCountPerList() ? startPage - 1 : null;  
	}

	public Integer getNextPage() {
		int startPage = ((getPage() - 1) / getPageCountPerList() * getPageCountPerList()) + 1;
		int totalPage = getTotalPage();
		return (startPage + getPageCountPerList() <= totalPage) ? startPage + getPageCountPerList() : null;  
	}

	public List<Integer> getPages() {
		pages = new ArrayList<Integer>(getPageCountPerList());
		int totalPage = getTotalPage();
		int startPage = ((getPage() - 1) / getPageCountPerList() * getPageCountPerList()) + 1;
		
		for(int i = startPage;  i <= totalPage && i < startPage + getPageCountPerList(); i++)
			pages.add(i);

		return pages;
	}

	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRowPerPage() {
		return rowPerPage;
	}
	public void setRowPerPage(int rowPerPage) {
		this.rowPerPage = rowPerPage;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;

		if(getTotalPage() > 0 && getTotalPage() < getPage())
			setPage(getTotalPage());
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}

	public int getPageCountPerList() {
		return pageCountPerList;
	}

	public void setPageCountPerList(int pageCountPerList) {
		this.pageCountPerList = pageCountPerList;
	}
}