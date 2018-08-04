package cn.wzz.actrowdfudingcommon.bean;

/**
 * 使用AJAX时，在后台向前端发送对象的封装结果集
 * @author 王子政
 */
public class AJAXResult {
	
	private boolean success;
	private Object data;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
}
