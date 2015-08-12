package com.ai.frame.dubbo.remote.log;

import com.ai.frame.dubbo.common.helper.ContextHelper;
import com.ai.frame.dubbo.common.log.Logger;
import com.ai.frame.dubbo.common.log.LoggerFactory;
import com.ai.frame.dubbo.common.util.StringUtil;
import com.ai.frame.dubbo.spi.InputObject;
import com.ai.frame.dubbo.spi.OutputObject;

public abstract class AbsAppOperLog implements AppOperLog {
	private Logger logger = LoggerFactory.getOuterCallerLog(AbsAppOperLog.class);
	protected ContextHelper context;
	public void setContext(ContextHelper context) {
		this.context = context;
	}
	public int saveOperLog(final InputObject inObj,final OutputObject outObj,final long calledTime){
		final String opLogType = inObj.getParams().get(APPLOGPARAMKEY);
		if(!StringUtil.isEmpty(opLogType)){ // 配置了记录日志的类型
			final String operator=inObj.getParams().get(APPLOGOPERATOR);
			
			new Thread(new Runnable() {
				public void run() {
					try{
						String paramstr = context.getJsonHelper().convertObject2Json(inObj);
						String respstr  = context.getJsonHelper().convertObject2Json(outObj);
						paramstr = StringUtil.subStrWithBytes(paramstr, MAXSTRLEN);
						respstr  = StringUtil.subStrWithBytes(respstr, MAXSTRLEN);
						
						Operlog operlog = new Operlog();
						operlog.setLogType(opLogType);
						operlog.setOperator(operator);
						operlog.setUsedTime(calledTime);
						
						operlog.setOpParam(paramstr);
						operlog.setOpResp(respstr);
						
						saveOperLog(inObj,outObj,operlog);
					}catch(Exception e){
						logger.error("save the {} log, use {} operator error:{}",  e, opLogType,operator);
					}
				}
			},"TH-saveOperLog").start();
		}
		
		return 1;
	}
	/**
	 * 操作日志入库
	 * @param in      操作入参
	 * @param out     操作出参
	 * @param operlog 操日志对象,子系统可以扩展
	 * @return        返回记录的日志主键ID 
	 */
	protected abstract int saveOperLog(InputObject in,OutputObject out,Operlog operlog);
}
