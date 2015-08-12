package com.ai.frame.dubbo.dconfig.spring.schema;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class AiserviceNamespaceHandler extends NamespaceHandlerSupport{
	
	@Override
	public void init() {
		registerBeanDefinitionParser("service", new AiserviceBeanDefinitionParser());
	}

}
