package com.ai.frame.dubbo.test.core.service.DynamicBusiService;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.ResourceEntityResolver;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

import com.ai.frame.dubbo.common.log.Logger;
import com.ai.frame.dubbo.common.log.LoggerFactory;

/**基于spring动态加载bean配置*/
public class DynamicBeanReaderImpl implements DynamicBeanReader,ApplicationContextAware{
	private static final Logger logger = LoggerFactory.getCustomerLog(DynamicBeanReaderImpl.class, "spring_dynamicBean");
	
	private ConfigurableApplicationContext applicationContext = null;
	private XmlBeanDefinitionReader beanDefinitionReader;
	
	/*初始化方法*/ 
	public void init(){
		beanDefinitionReader = new XmlBeanDefinitionReader((BeanDefinitionRegistry)applicationContext.getBeanFactory());
		
		beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(applicationContext));
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)throws BeansException {
		this.applicationContext = (ConfigurableApplicationContext)applicationContext;
	}
	
	@Override
	public void loadBean(DynamicBean dynamicBean) {
		long startTime = System.currentTimeMillis();
		
		String beanName = dynamicBean.getBeanName(); 
		if(applicationContext.containsBean(beanName)){
			logger.warn(beanName, "bean【{}】已经加载！");
			//TODO 已经存在旧的bean,须不须要动态更新
			return;
		}
		
		beanDefinitionReader.loadBeanDefinitions(new DynamicResource(dynamicBean)); 
		logger.info(beanName , String.valueOf(System.currentTimeMillis()-startTime), "初始化bean【{}】耗时{}毫秒.");
	}
}
