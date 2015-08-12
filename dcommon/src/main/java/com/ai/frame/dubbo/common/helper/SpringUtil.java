package com.ai.frame.dubbo.common.helper;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

public class SpringUtil implements BeanFactoryAware{
	private static BeanFactory factory;
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		factory = beanFactory;
	}
	public static <T> T getBean(String beanName, Class<T> rtnType) {
		return factory.getBean(beanName, rtnType);
	}

	public static Object getBean(String beanName) {
		return factory.getBean(beanName);
	} 
}
