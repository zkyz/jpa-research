package kr.or.knia.config.mybatis.page;

import java.util.List;

public interface Pageable {
	public int getPage();
	public int getRowPerPage();
	public int getPageCountPerList();
	public int getRowCount();
	public Integer getTotalPage();
	public Integer getPrevPage();
	public Integer getNextPage();
	public List<Integer> getPages();
	public String getSort();

	public boolean isEnabled();
	public void setEnabled(boolean enabled);
	public void setRowCount(int count);
}