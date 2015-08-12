package com.ai.frame.dubbo.common.helper;

import java.util.Date;

/**
 * 缓存帮助类,需要具模块统进行实现
 * @author yiyun
 *
 */
public interface CacheHelper {
	/**缓存对象*/
	public <T>void put(String key,T value);
	/**从缓存中获取对象*/
	public <T>T get(String key);
	/**删除具体的缓存*/
	public void remove(String key);
	/**清除所有缓存数据*/
	public void clear();
	/**清除指定日期之前的缓存数据*/
	public void clear(Date date);
}
