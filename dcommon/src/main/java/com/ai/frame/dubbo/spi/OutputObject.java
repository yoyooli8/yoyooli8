package com.ai.frame.dubbo.spi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ai.frame.dubbo.common.util.StringUtil;

/**
 * 统一的输出结果集
 * @since 20150701
 */
@SuppressWarnings("serial")
public class OutputObject implements Serializable {
	private String busiCode;
	private String opId;
	private String phone;
	private String returnCode;
	private String returnMessage;
	private Map<String, String> bean = new HashMap<String, String>();
	private List<Map<String, String>> beans = new ArrayList<Map<String, String>>();

	private Map<String, FileParam> file = new HashMap<String, FileParam>();//文件信息
	
	public String getBusiCode() {
		return busiCode;
	}

	public void setBusiCode(String busiCode) {
		this.busiCode = busiCode;
	}

	public String getOpId() {
		return opId;
	}

	public void setOpId(String opId) {
		this.opId = opId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnMessage() {
		return returnMessage;
	}

	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}

	public Map<String, String> getBean() {
		return bean;
	}

	public void setBean(Map<String, String> bean) {
		this.bean = bean;
	}

	public List<Map<String, String>> getBeans() {
		return beans;
	}

	public void setBeans(List<Map<String, String>> beans) {
		this.beans = beans;
	}

	public Map<String, FileParam> getFile() {
		return file;
	}

	public void setFile(Map<String, FileParam> file) {
		this.file = file;
	}

	
	/**
	 * 往bean属性中放入键值对
	 * 
	 * @param key
	 * @param toKey
	 * @param value
	 */
	public void addBean(String key, String toKey, String value) {
		if (!StringUtil.isEmpty(toKey)) {
			bean.put(toKey, String.valueOf(value));
		} else {
			bean.put(key, String.valueOf(value));
		}
	}

	/**
	 * 往beans中的index处的Map中放入键值对
	 * 
	 * @param key
	 * @param toKey
	 * @param index
	 * @param value
	 */
	public void addBeans(String key, String toKey, int index,String value) {
		if (beans.size() <= index) {
			beans.add(new HashMap<String, String>());
		}
		index = beans.size()-1;
		
		if (!StringUtil.isEmpty(toKey)) {
			beans.get(index).put(toKey, value);
		} else {
			beans.get(index).put(key, value);
		}
	}
	
	/**
	 * 往file属性中放入键值对
	 */
	public void addFile(String key, String toKey, FileParam fin) {
		if (toKey != null && !"".equals(toKey) && fin !=null) {
			file.put(toKey, fin);
		} else if(fin !=null){
			file.put(key, fin);
		}
	}
	
}
