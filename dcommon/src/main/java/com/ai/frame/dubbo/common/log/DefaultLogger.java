package com.ai.frame.dubbo.common.log;

import com.ai.frame.dubbo.common.util.StringUtil;

/**
 * 日志接口
 * @author  yiyun
 * @version: 20150701
 */
@SuppressWarnings("serial")
public class DefaultLogger implements Logger {
	private org.slf4j.Logger log;
	private String loggerType;
	public DefaultLogger(){
		log = org.slf4j.LoggerFactory.getLogger(LoggerType.UTIL);
		this.loggerType = LoggerType.UTIL;
	}
	public DefaultLogger(String loggerType) {
		log = org.slf4j.LoggerFactory.getLogger(loggerType);
		this.loggerType = loggerType;
	}
	public DefaultLogger(org.slf4j.Logger log){
		this.log = log;
	}
	public DefaultLogger(String loggerType,org.slf4j.Logger log){
		this.log = log;
		this.loggerType = loggerType;
	}
	public boolean debug(String method, String msg) {
		log.debug(msg, method);
		
		return true;
	}

	public boolean debug(String method, String userId, String msg) {
		log.debug(msg, method, userId);
		
		return true;
	}

	public boolean debug(String method, String msg, Throwable ex) {
		String[] args = null;
		if(ex!=null){
			args = new String[]{method,getErrMsg(ex)};
		}else{
			args = new String[]{method};
		}
		log.debug(msg, args);
		
		return true;
	}

	public boolean debug(String method, String userId, String msg, Throwable ex) {
		String[] args = null;
		if(ex!=null){
			args = new String[]{method,userId,getErrMsg(ex)};
		}else{
			args = new String[]{method,userId};
		}
		log.debug(msg, args);
		
		return true;
	}
	public boolean debug(String msg, Throwable ex,String ...args) {
		String[] cargs = args;
		if(ex!=null){
			cargs = new String[args.length+1];
			System.arraycopy(args, 0, cargs, 0, args.length);
			
			cargs[args.length] = getErrMsg(ex);
		}
		
		log.debug(msg, args);
		
		return true;
	}
	public boolean error(String method, String msg) {
		log.error(msg, method);
		
		return true;
	}

	public boolean error(String method, String userId, String msg) {
		log.error(msg, method, userId);
		
		return true;
	}

	public boolean error(String method, String msg, Throwable ex) {
		String[] args = null;
		if(ex!=null){
			args = new String[]{method,getErrMsg(ex)};
		}else{
			args = new String[]{method};
		}
		log.error(msg, args);
		
		return true;
	}

	public boolean error(String method, String userId, String msg, Throwable ex) {
		String[] args = null;
		if(ex!=null){
			args = new String[]{method,userId,getErrMsg(ex)};
		}else{
			args = new String[]{method,userId};
		}
		log.error(msg, args);
		
		return true;
	}
	private String getErrMsg(Throwable ex){
		String errmsg = ex.getMessage();
		if(StringUtil.isEmpty(errmsg)){
			errmsg = ex.getCause().getMessage();
		}
		return StringUtil.trim(errmsg," unkown exception.");
	}
	public boolean error(String msg, Throwable ex,String ...args) {
		String[] cargs = args;
		if(ex!=null){
			cargs = new String[args.length+1];
			System.arraycopy(args, 0, cargs, 0, args.length);
			
			cargs[args.length] = getErrMsg(ex);
		}
		
		log.error(msg, args);
		
		return true;
	}
	public boolean fatal(String method, String msg) {
		log.error(msg, method);
		
		return true;
	}

	public boolean fatal(String method, String userId, String msg) {
		log.error(msg, method, userId);
		
		return true;
	}

	public boolean fatal(String method, String msg, Throwable ex) {
		String[] args = null;
		if(ex!=null){
			args = new String[]{method,getErrMsg(ex)};
		}else{
			args = new String[]{method};
		}
		log.error(msg, args);
		
		return true;
	}

	public boolean fatal(String method, String userId, String msg, Throwable ex) {
		String[] args = null;
		if(ex!=null){
			args = new String[]{method,userId,getErrMsg(ex)};
		}else{
			args = new String[]{method,userId};
		}
		log.error(msg, args);
		
		return true;
	}

	public String getLoggerType() {
		return this.loggerType;
	}

	public boolean info(String method, String msg) {
		log.info(msg, method);
		
		return true;
	}

	public boolean info(String method, String userId, String msg) {
		log.info(msg, method, userId);
		
		return true;
	}

	public boolean info(String method, String msg, Throwable ex) {
		String[] args = null;
		if(ex!=null){
			args = new String[]{method,getErrMsg(ex)};
		}else{
			args = new String[]{method};
		}
		log.info(msg, args);
		
		return true;
	}

	public boolean info(String method, String userId, String msg, Throwable ex) {
		String[] args = null;
		if(ex!=null){
			args = new String[]{method,userId,getErrMsg(ex)};
		}else{
			args = new String[]{method,userId};
		}
		log.info(msg, args);
		
		return true;
	}
	public boolean info(String msg, Throwable ex,String ...args) {
		String[] cargs = args;
		if(ex!=null){
			cargs = new String[args.length+1];
			System.arraycopy(args, 0, cargs, 0, args.length);
			
			cargs[args.length] = getErrMsg(ex);
		}
		
		log.info(msg, args);
		
		return true;
	}
	public boolean warn(String method, String msg) {
		log.warn(msg, method);
		
		return true;
	}

	public boolean warn(String method, String userId, String msg) {
		log.warn(msg, method, userId);
		
		return true;
	}

	public boolean warn(String method, String msg, Throwable ex) {
		String[] args = null;
		if(ex!=null){
			args = new String[]{method,getErrMsg(ex)};
		}else{
			args = new String[]{method};
		}
		log.warn(msg, args);
		
		return true;
	}

	public boolean warn(String method, String userId, String msg, Throwable ex) {
		String[] args = null;
		if(ex!=null){
			args = new String[]{method,userId,getErrMsg(ex)};
		}else{
			args = new String[]{method,userId};
		}
		log.warn(msg, args);
		
		return true;
	}
	public boolean warn(String msg, Throwable ex,String ...args) {
		String[] cargs = args;
		if(ex!=null){
			cargs = new String[args.length+1];
			System.arraycopy(args, 0, cargs, 0, args.length);
			
			cargs[args.length] = getErrMsg(ex);
		}
		
		log.warn(msg, args);
		
		return true;
	}


}