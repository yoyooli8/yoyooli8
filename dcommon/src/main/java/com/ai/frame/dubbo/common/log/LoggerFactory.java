package com.ai.frame.dubbo.common.log;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取Logger对象的工厂类
 * @version: 20150701
 */
@SuppressWarnings("serial")
public class LoggerFactory implements Serializable{
	private static Map<String, Map<String, DefaultLogger>> loggers = new HashMap<String, Map<String, DefaultLogger>>();

	/**
	 * 根据ID获取持久化Logger对象
	 * @param id Class的全路径信息
	 * @return
	 */
	public static Logger getDaoLog(String id) {
		if (!(loggers.containsKey(LoggerType.DAO))) {
			loggers.put(LoggerType.DAO, new HashMap<String, DefaultLogger>());
		}

		Map<String, DefaultLogger> logger = loggers.get(LoggerType.DAO);
		if (!(logger.containsKey(id))) {
			logger.put(id, new DefaultLogger(id, org.slf4j.LoggerFactory.getLogger(LoggerType.DAO)));
		}

		return logger.get(id);
	}

	/**
	 * 根据Class获取持久化Logger对象
	 * 
	 * @param clz
	 * @return
	 */
	public static Logger getDaoLog(Class<?> clz) {
		return getDaoLog(getClassName(clz));
	}

	/**
	 * 根据ID获取控制层Logger对象
	 * 
	 * @param id
	 *            Class的全路径信息
	 * @return
	 */
	public static Logger getActionLog(String id) {
		if (!(loggers.containsKey(LoggerType.ACTION))) {
			loggers.put(LoggerType.ACTION, new HashMap<String, DefaultLogger>());
		}

		Map<String, DefaultLogger> logger = loggers.get(LoggerType.ACTION);
		if (!(logger.containsKey(id))) {
			logger.put(id, new DefaultLogger(id, org.slf4j.LoggerFactory.getLogger(LoggerType.ACTION)));
		}

		return logger.get(id);
	}

	/**
	 * 根据Class获取控制层Logger对象
	 * 
	 * @param clz
	 * @return
	 */
	public static Logger getActionLog(Class<?> clz) {
		return getActionLog(getClassName(clz));
	}

	/**
	 * 根据ID获取工具类Logger对象
	 * 
	 * @param id
	 *            Class的全路径信息
	 * @return
	 */
	public static Logger getUtilLog(String id) {
		if (!(loggers.containsKey(LoggerType.UTIL))) {
			loggers.put(LoggerType.UTIL, new HashMap<String, DefaultLogger>());
		}

		Map<String, DefaultLogger> logger = loggers.get(LoggerType.UTIL);
		if (!(logger.containsKey(id))) {
			logger.put(id, new DefaultLogger(id, org.slf4j.LoggerFactory.getLogger(LoggerType.UTIL)));
		}
		return logger.get(id);
	}

	/**
	 * 根据Class获取应用层Logger对象
	 * 
	 * @param clz
	 * @return
	 */
	public static Logger getApplicationLog(Class<?> clz) {
		return getApplicationLog(getClassName(clz));
	}

	/**
	 * 根据ID获取应用层Logger对象
	 * 
	 * @param id
	 *            Class的全路径信息
	 * @return
	 */
	public static Logger getApplicationLog(String id) {
		if (!(loggers.containsKey(LoggerType.APPLICATION))) {
			loggers.put(LoggerType.APPLICATION, new HashMap<String, DefaultLogger>());
		}

		Map<String, DefaultLogger> logger = loggers.get(LoggerType.APPLICATION);
		if (!(logger.containsKey(id))) {
			logger.put(id, new DefaultLogger(id, org.slf4j.LoggerFactory.getLogger(LoggerType.APPLICATION)));
		}
		return logger.get(id);
	}

	/**
	 * 根据Class获取工具类Logger对象
	 * 
	 * @param clz
	 * @return
	 */
	public static Logger getUtilLog(Class<?> clz) {
		return getUtilLog(getClassName(clz));
	}

	/**
	 * 根据ID获取业务层Logger对象
	 * 
	 * @param id
	 *            Class的全路径信息
	 * @return
	 */
	public static Logger getServiceLog(String id) {
		if (!(loggers.containsKey(LoggerType.SERVICE))) {
			loggers.put(LoggerType.SERVICE, new HashMap<String, DefaultLogger>());
		}

		Map<String, DefaultLogger> logger = loggers.get(LoggerType.SERVICE);
		if (!(logger.containsKey(id))) {
			logger.put(id, new DefaultLogger(id, org.slf4j.LoggerFactory.getLogger(LoggerType.SERVICE)));
		}
		return logger.get(id);
	}
	/**
	 * 根据Class获取业务层Logger对象
	 * 
	 * @param clz
	 * @return
	 */
	public static Logger getServiceLog(Class<?> clz) {
		return getServiceLog(getClassName(clz));
	}
	/**
	 * 根据Id取得调用第三方系统接口的日志对象
	 * @param id
	 * @return
	 */
	public static Logger getOuterCallerLog(String id) {
		if (!(loggers.containsKey(LoggerType.OUTERCALLER))) {
			loggers.put(LoggerType.OUTERCALLER, new HashMap<String, DefaultLogger>());
		}

		Map<String, DefaultLogger> logger = loggers.get(LoggerType.OUTERCALLER);
		if (!(logger.containsKey(id))) {
			logger.put(id, new DefaultLogger(id, org.slf4j.LoggerFactory.getLogger(LoggerType.OUTERCALLER)));
		}
		return logger.get(id);
	}
	
	/**
	 * 根据Class取得调用第三方系统接口的日志对象
	 * @param clz
	 * @return
	 */
	public static Logger getOuterCallerLog(Class<?> clz) {
		return getOuterCallerLog(getClassName(clz));
	}
	/**
	 * 取得用户自定义的日志对象 
	 * @param id        日志id
	 * @param loggerId  日志对象id
	 * @return
	 */
	public static Logger getCustomerLog(String id,String loggerId) {
		if (!(loggers.containsKey(loggerId))) {
			loggers.put(loggerId, new HashMap<String, DefaultLogger>());
		}

		Map<String, DefaultLogger> logger = loggers.get(loggerId);
		if (!(logger.containsKey(id))) {
			logger.put(id, new DefaultLogger(id, org.slf4j.LoggerFactory.getLogger(loggerId)));
		}
		return logger.get(id);
	}
	/**
	 * 根据给定的class取得用户自定义日志对象
	 * @param clz        给定的class  
	 * @param loggerId   日志对象id
	 * @return
	 */
	public static Logger getCustomerLog(Class<?> clz,String loggerId) {
		return getCustomerLog(getClassName(clz),loggerId);
	}
	private static String getClassName(Class<?> clz) {
		return clz.getName();
	}
}