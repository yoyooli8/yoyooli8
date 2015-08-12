package com.ai.frame.dubbo.test.services.plugins;

import com.ai.frame.dubbo.test.core.service.DynamicBusiService.DynamicBean;

public class TestPluginDynamicBean extends DynamicBean {

	@Override
	protected String getBeanXml() {
		StringBuffer xmlBuf = new StringBuffer();
		xmlBuf.append("<bean id=\"").append(getBeanName()).append("\"");
		xmlBuf.append(" class=\"com.ai.frame.dubbo.test.services.TestService\" parent=\"bsService\">");
		xmlBuf.append("    <property name=\"testDao\" ref=\"testDao\" />");
		xmlBuf.append("</bean>");
		return xmlBuf.toString();
	}

}
