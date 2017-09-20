package gwall.entity;

import java.util.List;

public class PageModel<T> {

	private Integer pageNumber; //当前页数
    private Integer pageSize;   //一页显示数量
    private Integer startRow;   //查询起始行
    private Integer total;      //总记录行数
    private List<T> rows;       //查询结果数据
    private T queryObj;         //查询对象

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public void setQueryObj(T queryObj) {
        this.queryObj = queryObj;
    }

    public T getQueryObj() {
        return queryObj;
    }

	public Integer getStartRow() {
		if(pageNumber!=null && pageSize!=null) {
			startRow = (pageNumber - 1) * pageSize;
        } else {
        	startRow = 0;
        }
		return startRow;
	}

	public void setStartRow(Integer startRow) {
		this.startRow = startRow;
	}
    
}
