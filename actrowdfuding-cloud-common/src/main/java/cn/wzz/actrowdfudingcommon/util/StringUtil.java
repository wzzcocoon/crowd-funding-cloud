package cn.wzz.actrowdfudingcommon.util;

public class StringUtil {

	public static boolean isEmpty(String s) {
		// 逻辑与(短路) || 和 按位与 | 的区别	
		return s == null || s.trim().equals("");
	}

}
