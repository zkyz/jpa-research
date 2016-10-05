package kr.or.knia.config.mybatis;

public class Fn {
	public static boolean empty(Object obj) {
		return obj == null || "".equals(obj);
	}
	
	public static boolean notEmpty(Object obj) {
		return !empty(obj);
	}
}