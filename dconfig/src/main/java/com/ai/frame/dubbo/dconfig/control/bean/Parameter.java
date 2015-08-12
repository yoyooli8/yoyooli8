package com.ai.frame.dubbo.dconfig.control.bean;

/**
 * Parameter实体.Request的参数映射。
 * 
 * @since 2015-07-03
 */
public class Parameter extends Entity {
	private String key;// Request中对应的key
	private String toKey;// InputObject对象中params属性中的key值
	private String value;
	private String scope;// key属性对应值的取值范围(request|session|constants|properties|file)
	private int order;// 参数的顺序,按照验证需求添加
	private String regex;// 正则表达式验证，按照验证需求添加

	/** 无参构造器 **/
	public Parameter() {
	}

	/** 构造器 **/
	public Parameter(String key, String toKey) {
		this.setKey(key);
		this.setToKey(toKey);
	}

	/** Setters And Getters **/
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getToKey() {
		return toKey;
	}

	public void setToKey(String toKey) {
		this.toKey = toKey;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Parameter other = (Parameter) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}
	
}
