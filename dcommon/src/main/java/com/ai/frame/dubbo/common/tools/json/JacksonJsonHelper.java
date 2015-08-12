package com.ai.frame.dubbo.common.tools.json;

import java.text.SimpleDateFormat;
import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import com.ai.frame.dubbo.common.exception.AinbException;

public class JacksonJsonHelper extends AbsJsonHelper {
	private static final ObjectMapper objectMapper = new ObjectMapper();
	static{
		objectMapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
	    objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	public <T> String convertObject2Json(T t) {
		try {
			return objectMapper.writeValueAsString(t);
		} catch (Exception e) {
			log.error("{} called.jackson error:{}.", e,"convertObject2Json");
		}
		return null;
	}
	public <T> String convertObject2Json(T t,String dateFm){
		try {
			return objectMapper.writer(new SimpleDateFormat(dateFm)).writeValueAsString(t);
		} catch (Exception e) {
			log.error("{} called.jackson error:{}.", e,"convertObject2JsonDateFm");
		}
		return null;
	}
	public <T> T convertJson2Object(String json, Class<T> cls) {
		try {
			return objectMapper.readValue(json, cls);
		} catch (Exception e) {
			log.error("{} called.jackson error:{}.", e,"convertJson2Object");
		}
		return null;
	}
	/**把JSON转成list的java对象*/
	public <T>List<T> convertJson2List(String json, Class<T> cls){
		throw new AinbException(JSON_ERROR, "not implement,please use convertJson2Object method.");
	}
}
