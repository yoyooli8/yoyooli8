package com.ai.frame.dubbo.dconfig.spring.schema;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ai.frame.dubbo.common.log.Logger;
import com.ai.frame.dubbo.common.log.LoggerFactory;
import com.ai.frame.dubbo.common.util.StringUtil;

public class AiserviceBeanDefinitionParser extends AbstractSingleBeanDefinitionParser{
	public static final String SPRINGSCHEMAS = "spring.custschemas.log";
	public static final String CONTEXTHELPER = "contextHelper";
	private static final Logger logger = LoggerFactory.getCustomerLog(AiserviceBeanDefinitionParser.class, SPRINGSCHEMAS);

	protected String getBeanClassName(Element element) {
		String cls = element.getAttribute("class");
		return cls;
	}
	
	protected void doParse(Element element, BeanDefinitionBuilder bean) {
		String contextHelper = element.getAttribute(CONTEXTHELPER);
		if(StringUtil.isEmpty(contextHelper)){//处理property
			parseContextHelper(element.getChildNodes(), bean);
		}else{
			bean.addPropertyReference(CONTEXTHELPER, contextHelper);
		}
	}
	
	private static void parseContextHelper(NodeList nodeList, BeanDefinitionBuilder bean) {
		if (nodeList != null && nodeList.getLength() > 0) {
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				if (node instanceof Element) {
					if (CONTEXTHELPER.equals(node.getNodeName())
                            || CONTEXTHELPER.equals(node.getLocalName())) {
						String name = ((Element) node).getAttribute("name");
						if (name != null && name.length() > 0) {
							String ref = ((Element) node).getAttribute("ref");
							bean.addPropertyReference(name, ref);
						}else{
							throw new UnsupportedOperationException("Unsupported <property name=\"" + name + "\"> sub tag, Only supported <property name=\"" + name + "\" ref=\"...\" /> ");
						}
					}else{
						logger.error("{}<property> tag 's name can't empty.", null, "");
					}
				}
			}
		}
	}
}
