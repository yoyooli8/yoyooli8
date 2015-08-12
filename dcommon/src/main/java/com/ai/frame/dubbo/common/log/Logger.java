package com.ai.frame.dubbo.common.log;

import java.io.Serializable;

/**
 * 日志接口
 * 
 * @version: 2011-9-16
 */
public abstract interface Logger extends Serializable{

	/**
	 * 获取日志的类型
	 * 
	 * @return
	 */
	String getLoggerType();

	/**
	 * debug级别信息记录
	 * 
	 * @param method
	 *            方法的名称
	 * @param msg
	 *            方法的简介或业务逻辑的说明
	 * @return
	 */
	boolean debug(String method, String msg);

	/**
	 * debug级别信息记录
	 * 
	 * @param method
	 *            方法的名称
	 * @param userId
	 *            当前用户的唯一标识
	 * @param msg
	 *            方法的简介或业务逻辑的说明
	 * @return
	 */
	boolean debug(String method, String userId, String msg);

	/**
	 * debug级别信息记录
	 * 
	 * @param method
	 *            方法的名称
	 * @param msg
	 *            方法的简介或业务逻辑的说明
	 * @param ex
	 *            异常的对象
	 * @return
	 */
	boolean debug(String method, String msg, Throwable ex);

	/**
	 * debug级别信息记录
	 * 
	 * @param method
	 *            方法的名称
	 * @param userId
	 *            当前用户的唯一标识
	 * @param msg
	 *            方法的简介或业务逻辑的说明
	 * @param ex
	 *            异常的对象
	 * @return
	 */
	boolean debug(String method, String userId, String msg,
			Throwable ex);

	/**
	 * info级别信息记录
	 * 
	 * @param method
	 *            方法的名称
	 * @param msg
	 *            方法的简介或业务逻辑的说明
	 * @return
	 */
	boolean info(String method, String msg);

	/**
	 * info级别信息记录
	 * 
	 * @param method
	 *            方法的名称
	 * @param userId
	 *            当前用户的唯一标识
	 * @param msg
	 *            方法的简介或业务逻辑的说明
	 * @return
	 */
	boolean info(String method, String userId, String msg);

	/**
	 * info级别信息记录
	 * 
	 * @param method
	 *            方法的名称
	 * @param msg
	 *            方法的简介或业务逻辑的说明
	 * @param ex
	 *            异常的对象
	 * @return
	 */
	boolean info(String method, String msg, Throwable ex);

	/**
	 * info级别信息记录
	 * 
	 * @param method
	 *            方法的名称
	 * @param userId
	 *            当前用户的唯一标识
	 * @param msg
	 *            方法的简介或业务逻辑的说明
	 * @param ex
	 *            异常的对象
	 * @return
	 */
	boolean info(String method, String userId, String msg,
			Throwable ex);

	/**
	 * warn级别信息记录
	 * 
	 * @param method
	 *            方法的名称
	 * @param msg
	 *            方法的简介或业务逻辑的说明
	 * @return
	 */
	boolean warn(String method, String msg);

	/**
	 * warn级别信息记录
	 * 
	 * @param method
	 *            方法的名称
	 * @param userId
	 *            当前用户的唯一标识
	 * @param msg
	 *            方法的简介或业务逻辑的说明
	 * @return
	 */
	boolean warn(String method, String userId, String msg);

	/**
	 * warn级别信息记录
	 * 
	 * @param method
	 *            方法的名称
	 * @param msg
	 *            方法的简介或业务逻辑的说明
	 * @param ex
	 *            异常的对象
	 * @return
	 */
	boolean warn(String method, String msg, Throwable ex);

	/**
	 * warn级别信息记录
	 * 
	 * @param method
	 *            方法的名称
	 * @param userId
	 *            当前用户的唯一标识
	 * @param msg
	 *            方法的简介或业务逻辑的说明
	 * @param ex
	 *            异常的对象
	 * @return
	 */
	boolean warn(String method, String userId, String msg,
			Throwable ex);

	/**
	 * error级别信息记录
	 * 
	 * @param method
	 *            方法的名称
	 * @param msg
	 *            方法的简介或业务逻辑的说明
	 * @return
	 */
	boolean error(String method, String msg);

	/**
	 * error级别信息记录
	 * 
	 * @param method
	 *            方法的名称
	 * @param userId
	 *            当前用户的唯一标识
	 * @param msg
	 *            方法的简介或业务逻辑的说明
	 * @return
	 */
	boolean error(String method, String userId, String msg);

	/**
	 * error级别信息记录
	 * 
	 * @param method
	 *            方法的名称
	 * @param msg
	 *            方法的简介或业务逻辑的说明
	 * @param ex
	 *            异常的对象
	 * @return
	 */
	boolean error(String method, String msg, Throwable ex);

	/**
	 * error级别信息记录
	 * 
	 * @param method
	 *            方法的名称
	 * @param userId
	 *            当前用户的唯一标识
	 * @param msg
	 *            方法的简介或业务逻辑的说明
	 * @param ex
	 *            异常的对象
	 * @return
	 */
	boolean error(String method, String userId, String msg,
			Throwable ex);

	/**
	 * fatal级别信息记录
	 * 
	 * @param method
	 *            方法的名称
	 * @param msg
	 *            方法的简介或业务逻辑的说明
	 * @return
	 */
	boolean fatal(String method, String msg);

	/**
	 * fatal级别信息记录
	 * 
	 * @param method
	 *            方法的名称
	 * @param userId
	 *            当前用户的唯一标识
	 * @param msg
	 *            方法的简介或业务逻辑的说明
	 * @return
	 */
	boolean fatal(String method, String userId, String msg);

	/**
	 * fatal级别信息记录
	 * 
	 * @param method
	 *            方法的名称
	 * @param msg
	 *            方法的简介或业务逻辑的说明
	 * @param ex
	 *            异常的对象
	 * @return
	 */
	boolean fatal(String method, String msg, Throwable ex);

	/**
	 * fatal级别信息记录
	 * 
	 * @param method
	 *            方法的名称
	 * @param userId
	 *            当前用户的唯一标识
	 * @param msg
	 *            方法的简介或业务逻辑的说明
	 * @param ex
	 *            异常的对象
	 * @return
	 */
	boolean fatal(String method, String userId, String msg,
			Throwable ex);

	public boolean debug(String msg, Throwable ex,String ...args);
	public boolean error(String msg, Throwable ex,String ...args);
	public boolean info(String msg, Throwable ex,String ...args);
	public boolean warn(String msg, Throwable ex,String ...args);
}
