package com.ai.frame.dubbo.common.log;

/**
 * 定义Logger类型的接口
 * 
 * @author: niewr@asiainfo-linkage.com
 * @version: 2011-9-22
 */
public abstract interface LoggerType {
	/** 控制层日志类型 **/
	String ACTION = "ACTION";
	/** 业务层日志类型 **/
	String SERVICE = "SERVICE";
	/** 持久化层日志类型 **/
	String DAO = "DAO";
	/** 工具类日志类型 **/
	String UTIL = "UTIL";
	/** 应用层日志类型 **/
	String APPLICATION = "APPLICATION";
	/** 调用外部接口 **/
	String OUTERCALLER = "OUTERCALLER";
}
