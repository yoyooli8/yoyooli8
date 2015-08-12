package com.ai.frame.dubbo.common.helper;

import java.util.List;
import java.util.Map;

public interface JsonHelper {
	/**把java对象转成JSON*/
	public <T>String convertObject2Json(T t);
	/**带日志格式的对象转成JSON*/
	public <T> String convertObject2Json(T t,String dateFm);
	/**把JSON转成指定的java对象*/
	public <T>T convertJson2Object(String json,Class<T> cls);
	/**把JSON转成list的java对象*/
	public <T>List<T> convertJson2List(String json, Class<T> cls);
	/**把java对象转成map对象*/
	public <T>Map<String,String> convertBean2Map(T t);
	/**把list对象转成List<Map>对象*/
	public <T>List<Map<String,String>> convert2ListMap(List<T> datas);

}
