package cn.wzz.actrowdfudingcommon;

import cn.wzz.actrowdfudingcommon.bean.AJAXResult;

public abstract class BaseController {
		
	//所谓的线程安全问题，就是在多线程并发执行时，对共享内存中的共享对象的属性进行修改时所导致的数据冲突问题。
	private ThreadLocal<AJAXResult> resultLocal = new ThreadLocal<AJAXResult>();
	
	protected void start() {
		resultLocal.set(new AJAXResult());
	}
	
	/**给返回的结果集中，传入数据*/
	protected void putData(Object data) {
		AJAXResult result = resultLocal.get();
		result.setData(data);
	}

	protected Object end() {
		Object obj = resultLocal.get();
		resultLocal.remove();
		return obj;
	}
	
	protected void success() {
		success(true);
	}
	
	protected void success(boolean falg) {
		AJAXResult result = resultLocal.get();
		result.setSuccess(falg);
	}
	
	protected void fail() {
		success(false);
	}
	
}
