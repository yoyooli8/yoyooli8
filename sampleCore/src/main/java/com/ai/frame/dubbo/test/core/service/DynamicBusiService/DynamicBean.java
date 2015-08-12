package com.ai.frame.dubbo.test.core.service.DynamicBusiService;

public abstract class DynamicBean {
	protected String beanName;
	
	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
	
    /** 
     * 获取bean 的xml描述 
     * @return 
     */  
    protected abstract String getBeanXml();
    
    /** 
     * 生成完整的xml字符串 
     * @return 
     */  
    public String getXml(){ 
    	StringBuffer buf = new StringBuffer();
    	buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    	buf.append("<beans xmlns=\"http://www.springframework.org/schema/beans\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
    	buf.append(" xmlns:p=\"http://www.springframework.org/schema/p\"");
    	buf.append(" xmlns:context=\"http://www.springframework.org/schema/context\"");
    	buf.append("  xsi:schemaLocation=\"");
    	buf.append("http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.0.xsd");
    	buf.append("  http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-3.0.xsd\">");
    	buf.append(getBeanXml());
    	buf.append("</beans>");
    	
    	return buf.toString();
    }
    
}
