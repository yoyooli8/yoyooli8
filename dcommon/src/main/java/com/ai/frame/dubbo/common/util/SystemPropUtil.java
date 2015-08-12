package com.ai.frame.dubbo.common.util;

import java.util.ResourceBundle;

/***
 * config/system.properties属性文件中的内容获取工具类
 * @author yiyun
 *
 */
public class SystemPropUtil {
	private static final String BUNDLE_NAME = "config/system";
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
	
	private SystemPropUtil() {
	}
	/**取得属性中对应key的值*/
	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (Exception e) {
			return null;
		}
	}
}
