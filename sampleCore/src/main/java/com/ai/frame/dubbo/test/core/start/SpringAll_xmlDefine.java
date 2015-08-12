package com.ai.frame.dubbo.test.core.start;


public abstract class SpringAll_xmlDefine{
	private String propertyHolder;
	
	public String getPropertyHolder() {
		return propertyHolder;
	}

	public void setPropertyHolder(String propertyHolder) {
		this.propertyHolder = propertyHolder;
	}
	protected abstract void setCustomerXml(StringBuffer xmlBuf);
	protected String getBeanXml() {
		StringBuffer xmlBuf = new StringBuffer();
		xmlBuf.append("<context:property-placeholder location=\"");
		xmlBuf.append(getPropertyHolder()).append("\"/>");
		xmlBuf.append("<import resource=\"classpath:com/ai/frame/dubbo/common/spring-context.xml\"/>");
		xmlBuf.append("<import resource=\"classpath:com/ai/frame/dubbo/orm/spring-druid.xml\"/>");
		xmlBuf.append("<import resource=\"classpath:com/ai/frame/dubbo/orm/spring-mybatis.xml\"/>");
		//xmlBuf.append("<import resource=\"classpath:spring/spring-dao.xml\"/>");
		//xmlBuf.append("<import resource=\"classpath:spring/spring-services.xml\"/>");
		setCustomerXml(xmlBuf);
		xmlBuf.append("<import resource=\"classpath:com/ai/frame/dubbo/remote/spring-dubbo.xml\"/>");
		return xmlBuf.toString();
	}

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
