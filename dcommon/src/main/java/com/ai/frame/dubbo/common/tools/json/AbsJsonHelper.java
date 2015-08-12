package com.ai.frame.dubbo.common.tools.json;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ai.frame.dubbo.common.helper.JsonHelper;
import com.ai.frame.dubbo.common.log.Logger;
import com.ai.frame.dubbo.common.log.LoggerFactory;
import com.ai.frame.dubbo.common.util.ClassUtil;
import com.ai.frame.dubbo.common.util.StringUtil;

public abstract class AbsJsonHelper implements JsonHelper{
	protected static final String JSON_ERROR = "json_convert_error";
	protected static Logger log = LoggerFactory.getUtilLog(AbsJsonHelper.class);
	public <T> Map<String, String> convertBean2Map(T t) {
		Map<String, String> bean = new HashMap<String, String>();
		try {
			Field[] fields = t.getClass().getDeclaredFields();
			fields = ClassUtil.getClassFieldsWithSuper(t.getClass());
			for (Field field : fields) {
				String key = field.getName();
				if (!"serialVersionUID".equals(key)) {
					boolean visible = field.isAccessible();
					field.setAccessible(Boolean.TRUE);
					Object objval = ClassUtil.getFieldVal(t, field);
					String value = StringUtil.obj2TrimStr(objval);
					field.setAccessible(visible);
					
					bean.put(key, value);
				}
			}
			return bean;
		} catch (Exception e) {
			return bean;
		}
	}

	public <T> List<Map<String, String>> convert2ListMap(List<T> datas) {
		List<Map<String,String>> result = new ArrayList<Map<String,String>>();
		if(datas!=null){
			for(T t:datas){
				Map<String,String> mpdata = convertBean2Map(t);
				result.add(mpdata);
			}
		}
		return result;
	}
}
