package com.ai.frame.dubbo.common.helper;

/**应用上下文帮助类*/
public interface ContextHelper {
	/**取得缓存帮助*/
	public CacheHelper getCacheHelper();
	
	/**取得service实体*/
	public <T> T getBean(String beanName,Class<T> rtnType);
	/**取得service实体*/
	public <T> T getBean(String beanName);
	/**取得JSON处理帮助*/
	public JsonHelper getJsonHelper();
	
}
