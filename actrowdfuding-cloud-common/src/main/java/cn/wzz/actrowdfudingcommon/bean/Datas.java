package cn.wzz.actrowdfudingcommon.bean;

import java.util.List;

/**
 * 数据包装类
 * @author 王子政
 */
public class Datas {
	
	private List<Integer> ids;
	private List<MemberCert> mcs; 

	public List<Integer> getIds() {
		return ids;
	}
	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}
	public List<MemberCert> getMcs() {
		return mcs;
	}
	public void setMcs(List<MemberCert> mcs) {
		this.mcs = mcs;
	}
	
}
