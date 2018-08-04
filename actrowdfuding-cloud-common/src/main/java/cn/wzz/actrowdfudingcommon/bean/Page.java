package cn.wzz.actrowdfudingcommon.bean;

import java.util.List;

/**
 * 分页类
 * @author 王子政
 * @param <T> 泛型T可以指定页面中储存的数据类型。
 */
public class Page<T> {

	//当前的页码（第几页）
	private Integer pageno;
	//一页中的记录条数
	private Integer pagesize;
	//总记录数
	private Integer totalsize;
	//总页数
	private Integer totalpage;
	//一页中所有的数据
	private List<T> datas;
	
	public Integer getPageno() {
		return pageno;
	}
	public void setPageno(Integer pageno) {
		this.pageno = pageno;
	}
	public Integer getPagesize() {
		return pagesize;
	}
	public void setPagesize(Integer pagesize) {
		this.pagesize = pagesize;
	}
	public Integer getTotalsize() {
		return totalsize;
	}
	public void setTotalsize(Integer totalsize) {
//		if(totalsize % pagesize == 0) {
//			totalpage = totalsize/pagesize;
//		} else {
//			totalpage = totalsize/pagesize + 1;
//		}
		this.totalsize = totalsize;
	}
	public Integer getTotalpage() {
		return totalpage;
	}
	public void setTotalpage(Integer totalpage) {
		this.totalpage = totalpage;
	}
	public List<T> getDatas() {
		return datas;
	}
	public void setDatas(List<T> datas) {
		this.datas = datas;
	}
	@Override
	public String toString() {
		return "Page [pageno=" + pageno + ", pagesize=" + pagesize + ", totalsize=" + totalsize + ", totalpage="
				+ totalpage + ", datas=" + datas + "]";
	}
	
}
