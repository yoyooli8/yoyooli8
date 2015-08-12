package com.ai.frame.dubbo.dconfig.control.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Output实体.Request的返回结果集。
 * 
 * @since 2015-07-03
 */
public class Output extends Entity {
	private String uid;// 请求的唯一编号;
	private String key;
	private String src;
	private String scope;
	private String convertor;// Json转换类
	private String method;// 转换json调用的方法;
	private List<Parameter> parameters;
	
	/** 无参构造器 **/
	public Output() {
		parameters = new ArrayList<Parameter>();
	}

	/**
	 * 添加子属性Parameter
	 * 
	 * @param parameter
	 *            子属性Parameter
	 */
	public void addParameter(Parameter parameter) {
		parameters.add(parameter);
	}

	/** Setters And Getters **/
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getConvertor() {
		return convertor;
	}

	public void setConvertor(String convertor) {
		this.convertor = convertor;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}

	/**
	 * 根据Key值获取对应的Parameter对象
	 * 
	 * @param key
	 *            Parameter对象中的key
	 */
	public Parameter getParameterByKey(String key) {
		for (Parameter parameter : parameters) {
			if (key.equals(parameter.getKey())) {
				return parameter;
			}
		}
		return null;
	}
	
	public int hashCode() {
        int result = super.hashCode() + ((uid == null) ? 0 : uid.hashCode());
        return result;
    }
	public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        if (this == obj)
            return true;
        
        Output other = (Output)obj;
        
        if(uid == null){
            return false;
        }else{
            return uid.equals(other.uid);
        }
    }
}
