package com.ai.frame.dubbo.common.helper;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;

import com.ai.frame.dubbo.common.tools.cache.OscacheHelper;
import com.ai.frame.dubbo.common.tools.json.FastJsonHelper;

public class DefaultContextHelper implements ContextHelper,BeanFactoryAware,InitializingBean{
	private CacheHelper cacheHelper;
	private JsonHelper  jsonHelper;
	public void setCacheHelper(CacheHelper cacheHelper) {
		this.cacheHelper = cacheHelper;
	}

	public void setJsonHelper(JsonHelper jsonHelper) {
		this.jsonHelper = jsonHelper;
	}

	private BeanFactory factory;
	
	public CacheHelper getCacheHelper() {
		return cacheHelper;
	}

	public <T> T getBean(String beanName, Class<T> rtnType) {
		return factory.getBean(beanName, rtnType);
	}

	@SuppressWarnings("unchecked")
	public <T> T getBean(String beanName) {
		return (T)factory.getBean(beanName);
	}

	public JsonHelper getJsonHelper() {
		return jsonHelper;
	}

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.factory = beanFactory;
	}

	public void afterPropertiesSet() throws Exception {
		if(cacheHelper == null){
			cacheHelper = new OscacheHelper();
		}
		if(jsonHelper == null){
			jsonHelper = new FastJsonHelper();
		}
	}

}
