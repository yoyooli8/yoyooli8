package com.ai.frame.dubbo.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ai.frame.dubbo.common.log.Logger;
import com.ai.frame.dubbo.common.log.LoggerFactory;


public final class ClassUtil {
	private static final Logger log = LoggerFactory.getUtilLog(ClassUtil.class);
	private static final Map<Class<?>, Class<?>> primitiveWrapperMap = new HashMap<Class<?>, Class<?>>();
	static {
		primitiveWrapperMap.put(Boolean.TYPE, Boolean.class);
        primitiveWrapperMap.put(Byte.TYPE, Byte.class);
        primitiveWrapperMap.put(Character.TYPE, Character.class);
        primitiveWrapperMap.put(Short.TYPE, Short.class);
        primitiveWrapperMap.put(Integer.TYPE, Integer.class);
        primitiveWrapperMap.put(Long.TYPE, Long.class);
        primitiveWrapperMap.put(Double.TYPE, Double.class);
        primitiveWrapperMap.put(Float.TYPE, Float.class);
	}
	private static final Map<Class<?>, Class<?>> wrapperPrimitiveMap = new HashMap<Class<?>, Class<?>>();
	static {
        for (Class<?> primitiveClass : primitiveWrapperMap.keySet()) {
            Class<?> wrapperClass = primitiveWrapperMap.get(primitiveClass);
            if (!primitiveClass.equals(wrapperClass)) {
                wrapperPrimitiveMap.put(wrapperClass, primitiveClass);
            }
        }
    }
	private ClassUtil(){}
	
	public static boolean isPrimitiveOrWrapper(Class<?> type) {
        if (type == null) {
            return false;
        }
        return type.isPrimitive() || isPrimitiveWrapper(type);
    }
	public static boolean isPrimitiveWrapper(Class<?> type) {
        return wrapperPrimitiveMap.containsKey(type);
    }
	
	@SuppressWarnings("unchecked")
	public static <T>T getInstance(String clsstr,Class<T> retType){
		try{
			return (T)Class.forName(clsstr).newInstance();
		}catch(Exception e){}
		return null;
	}
	public static Object getInstance(String clsstr){
		try{
			return Class.forName(clsstr).newInstance();
		}catch(Exception e){}
		return null;
	}
	@SuppressWarnings("rawtypes")
	public static Object getInstance(String clsstr,Object[] paramVal, Class ...params){
		try{
			return Class.forName(clsstr).getConstructor(params).newInstance(paramVal);
		}catch(Exception e){
			log.error(" ", "{}getInstance error:{}", e);
		}
		return null;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T>T invokMethod(Class<T> retType,String clz,String method,Class[]paramcls,Object ...paramvals){
		Object obj = getInstance(clz);
		try{
			if(obj!=null && !StringUtil.isEmpty(method)){
				Method methodo = getMethodWithSuper(obj.getClass(),method, paramcls);
				methodo.setAccessible(true);
				
				return (T)methodo.invoke(obj, paramvals);
			} 
			return null;
		}catch(Exception e){
			log.error(" ", "{}invokMethod error:{}", e);
			//System.err.println("invokMethod error:"+e.getMessage());
		}
		return null;
	}
	@SuppressWarnings("rawtypes")
	public static void invokMethod(String clz,String method,Class[]paramcls,Object ...paramvals){
		Object obj = getInstance(clz);
		try{
			if(obj!=null && !StringUtil.isEmpty(method)){
				Method methodo = getMethodWithSuper(obj.getClass(),method, paramcls);
				methodo.setAccessible(true);
				
				methodo.invoke(obj, paramvals);
			} 
		}catch(Exception e){
			log.error(" ", "{}invokMethod error:{}", e);
			//System.err.println("invokMethod error:"+e.getMessage());
		}
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T>T invokMethod(Class<T> retType,Object obj,String method,Class[]paramcls,Object ...paramvals){
		try{
			if(obj!=null && !StringUtil.isEmpty(method)){
				Method methodo = getMethodWithSuper(obj.getClass(),method, paramcls);
				methodo.setAccessible(true);
				
				return (T)methodo.invoke(obj, paramvals);
			} 
			return null;
		}catch(Exception e){
			log.error(" ", "{}invokMethod error:{}", e);
//			System.err.println("invokMethod error:"+e.getMessage());
		}
		return null;
	}
	@SuppressWarnings("rawtypes")
	public static Method getMethodWithSuper(Class<?> cls,String method,Class[]paramcls){
		boolean isFind =false;
		try {
			Method mthd = cls.getDeclaredMethod(method, paramcls);
			if(mthd !=null){
				isFind = true;
				return mthd;
			}
			isFind = false;
		} catch (Exception e) {
			isFind = false;
		}
		Class upsercls = cls.getSuperclass();
		if (upsercls != Object.class && isFind == false) {
			return getMethodWithSuper(upsercls,method,paramcls);
		}
		return null;
	}
	@SuppressWarnings("rawtypes")
	public static void invokMethod(Object obj,String method,Class[]paramcls,Object ...paramvals){
		try{
			if(obj!=null && !StringUtil.isEmpty(method)){
				Method methodo = getMethodWithSuper(obj.getClass(),method, paramcls);
				methodo.setAccessible(true);
				
				methodo.invoke(obj, paramvals);
			} 
		}catch(Exception e){}
	}
	
	@SuppressWarnings("rawtypes")
	public static <T>Object getFieldVal(T t,String fileName){
		try {
			
            Class objcls = t.getClass();
            Field field = getFieldWithSuper(objcls,fileName);
            if(field!=null){
            	field.setAccessible(true);

                return field.get(t);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
	}
	@SuppressWarnings("rawtypes")
	public static Field getFieldWithSuper(Class<?> cls,String fileName){
		boolean isFind =false;
		try {
			Field field = cls.getDeclaredField(fileName);
			if(field !=null){
				isFind = true;
				return field;
			}
			isFind = false;
		} catch (Exception e) {
			isFind = false;
		}
		Class upsercls = cls.getSuperclass();
		if (upsercls != Object.class && isFind == false) {
			return getFieldWithSuper(upsercls,fileName);
		}
		return null;
	}
	public static <T>Object getFieldVal(T t,Field field){
		try {
            field.setAccessible(true);

            return field.get(t);
        } catch (Exception e) {
            return null;
        }
	}
	public static Method[] getClassMethodWithSuper(Class<?> cls){
		List<Method> methods = new ArrayList<Method>();
		if (cls != null) {
			getClassMethodWithSuper(cls,methods);
		}
		
		return methods.toArray(new Method[methods.size()]);
	}
	@SuppressWarnings("rawtypes")
	private static void getClassMethodWithSuper(Class<?> cls,List<Method> methods){
		Method[] cmethods = getClassMethodss(cls);
		List<Method> fieldList = Arrays.asList(cmethods);
		methods.addAll(fieldList);
		
		Class upsercls = cls.getSuperclass();
		if (upsercls != Object.class) {
			getClassMethodWithSuper(upsercls,methods);
		}
	}
	public static Method[] getClassMethodss(Class<?> cls){
		Method[] cmethods = cls.getDeclaredMethods();
		List<Method> methods = new ArrayList<Method>();
		for (Method m : cmethods) {
			if (m != null && !StringUtil.isEmpty(m.getName())) {
				methods.add(m);
				
			}
		}
		
		return methods.toArray(new Method[methods.size()]);
	}
	public static Field[] getClassFieldsWithSuper(Class<?> cls){
		List<Field> fields = new ArrayList<Field>();
		if (cls != null) {
			getClassFieldsWithSuper(cls,fields);
		}
		
		return fields.toArray(new Field[fields.size()]);
	}
	@SuppressWarnings("rawtypes")
	private static void getClassFieldsWithSuper(Class<?> cls,List<Field> fields){
		Field[] cfields = getClassFields(cls);
		List<Field> fieldList = Arrays.asList(cfields);
		fields.addAll(fieldList);
		
		Class upsercls = cls.getSuperclass();
		if (upsercls != Object.class) {
			getClassFieldsWithSuper(upsercls,fields);
		}
	}
	public static Field[] getClassFields(Class<?> cls){
		Field[] cfields = cls.getDeclaredFields();
		List<Field> fields = new ArrayList<Field>();
		for (Field f : cfields) {
			if (f != null && !StringUtil.isEmpty(f.getName())) {
				
				if(hasPubGetter(cls,f.getName())){
					fields.add(f);
				}
				
			}
		}
		
		return fields.toArray(new Field[fields.size()]);
	}
	@SuppressWarnings("rawtypes")
	public static boolean hasPubGetter(Class<?> cls,String fieldName){
		String getter = getGetterMethodName(fieldName);
		Class[] parameterTypes = null;
		try{
			Method getMethod = cls.getMethod(getter, parameterTypes);
			if(getMethod!=null){
				return true;
			}
		}catch(Exception e){}
		
		return false;
	}
	@SuppressWarnings("rawtypes")
	public static Object invokFieldGetMethodVal(Object obj,String fieldName){
		String getter = getGetterMethodName(fieldName); 
		if(!StringUtil.isEmpty(getter)){
			Class[] parameterTypes = null;
			try{
				Method getMethod = obj.getClass().getMethod(getter, parameterTypes);
				Object[] args = null;
				return getMethod.invoke(obj, args);
			}catch(Exception e){
				return null;
			}
		}
		return null;
	}
	@SuppressWarnings("rawtypes")
	private static Object getObjVal(Class type,String strvalue){
		if(type == Date.class){
            return DateUtil.string2Date(strvalue);
        }else if(type == Calendar.class){
            return DateUtil.string2Calendar(strvalue);
        }else if(type == Integer.class || type == int.class){
            return StringUtil.str2Int(strvalue);
        }else if(type == Double.class || type == double.class){
            return StringUtil.str2Double(strvalue);
        }else if(type == Long.class || type == long.class){
            return StringUtil.str2Long(strvalue);
        }else if(type == String.class){
            return StringUtil.trim(strvalue);
        }else if(type == Boolean.class || type == boolean.class){
            return StringUtil.str2Boolean(strvalue);
        }else{
        	return null;
        }
	}
	@SuppressWarnings("rawtypes")
	public static void setPropValFromMap(Map<String,String> map,Object targetObj){
		Field[] fields = ClassUtil.getClassFieldsWithSuper(targetObj.getClass());
		for(int i = 0;i<fields.length;i++){
			Field field = fields[i];
            String name = field.getName();
            Class type = field.getType();
            Object fvalue = null;
            String strvalue = map.get(name);
            if(strvalue != null){
            	fvalue = getObjVal(type,strvalue);
            	try{
	            	field.setAccessible(true);
	                field.set(targetObj, fvalue);
            	}catch(Exception e){}
            }
		}
	}
	public static void setFieldVal(String fieldName,Object targetObj,String fieldVal){
		try {
			Field field = getFieldWithSuper(targetObj.getClass(),fieldName);
			Object fieldValue = getObjVal(field.getType(),fieldVal);
			setFieldVal(field,targetObj,fieldValue);
		} catch (Exception e) {}
	}
	public static void setFieldVal(String fieldName,Object targetObj,Object fieldVal){
		try {
			Field field = getFieldWithSuper(targetObj.getClass(),fieldName);
			
			setFieldVal(field,targetObj,fieldVal);
		} catch (Exception e) {}
	}
	public static void setFieldVal(Field field,Object targetObj,Object fieldVal){
        try {
            field.setAccessible(true);
            field.set(targetObj, fieldVal);
        } catch (Exception e) {
        }
    }
	private static String getGetterMethodName(String fieldName){
		if(fieldName!=null&&fieldName.length()>1){
			String firstLettle = fieldName.substring(0,1);
			return "get" + firstLettle.toUpperCase() + fieldName.substring(1);
		}
		return null;
	}
}
