package com.ai.frame.dubbo.dconfig.control.bean;

/**
 * Result实体.Request请求跳转路径.
 * 
 * @since 2015-07-03
 */
public class Result extends Entity {
	private String key;
	private String path;
	private String type;

	/** Setters And Getters **/
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public int hashCode() {
        int result = super.hashCode() + ((key == null) ? 0 : key.hashCode());
        return result;
    }
	public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        if (this == obj)
            return true;
        
        Result other = (Result)obj;
        if(key == null){
            return false;
        }else{
            return key.equals(other.key);
        }
    }
}
