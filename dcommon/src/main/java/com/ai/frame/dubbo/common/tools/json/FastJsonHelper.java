package com.ai.frame.dubbo.common.tools.json;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

public class FastJsonHelper extends AbsJsonHelper{

	public <T> String convertObject2Json(T t) {
		try{
			return JSON.toJSONString(t);
		}catch(Exception e){
			log.error("{} called.fastJson error:{}.", e,"convertObject2Json");
			return null;
		}
	}
	/**带日志格式的对象转成JSON*/
	public <T> String convertObject2Json(T t,String dateFm) {
		try{
			SerializeConfig mapping = new SerializeConfig();
			mapping.put(Date.class, new SimpleDateFormatSerializer(dateFm));

			return JSON.toJSONString(t,mapping);
		}catch(Exception e){
			log.error("{} called.fastJson error:{}.", e,"convertObject2JsonDateFm");
			return null;
		}
	}
	public <T> T convertJson2Object(String json, Class<T> cls) {
		try{
			return JSON.parseObject(json, cls);
		}catch(Exception e){
			log.error("{} called.fastJson error:{}.", e,"convertJson2Object");
		}
		return null;
	}
	/**把JSON转成list的java对象*/
	public <T>List<T> convertJson2List(String json, Class<T> cls){
		try{
			return JSON.parseArray(json, cls);
		}catch(Exception e){
			log.error("{} called.fastJson error:{}.", e,"convertJson2List");
		}
		return null;
	}
}
