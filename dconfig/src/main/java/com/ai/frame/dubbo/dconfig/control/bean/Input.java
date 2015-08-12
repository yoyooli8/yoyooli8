package com.ai.frame.dubbo.dconfig.control.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Input实体.Request的请求参数。
 * 
 * @since 2015-07-03
 */
public class Input extends Entity {
	private String uid; // 请求的唯一编号
	private String service; // Request请求调用后台的WebService服务类。此时应该覆盖Action中的service.
	private String method; // Request请求调用后台的WebService服务类的服务方法。此时应该覆盖Action中的method.
	private String code;
	private String scope;//
	private String desc;// 描述
	private boolean sec;// 安全
	private boolean cache;
	private List<Parameter> parameters; // 请求的参数

	/** 无参构造器 **/
	public Input() {
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

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public boolean isSec() {
		return sec;
	}

	public void setSec(boolean sec) {
		this.sec = sec;
	}

	public boolean isCache() {
		return cache;
	}

	public void setCache(boolean cache) {
		this.cache = cache;
	}

	/**
	 * 根据key值获取对应Parameter对象中的toKey值
	 * 
	 * @param key
	 *            Parameter对象中的key值
	 * @return Parameter对象中的toKey值
	 */
	public String getToKeyByKey(String key) {
		for (Parameter p : parameters) {
			if (p.getKey().equalsIgnoreCase(key)) {
				return p.getToKey();
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

        final Input other = (Input) obj;

        if(uid == null){
            return false;
        }else{
            return uid.equals(other.uid);
        }
    }
}
