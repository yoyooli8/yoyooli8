package com.ai.frame.dubbo.remote.spi;

import com.ai.frame.dubbo.common.helper.ContextHelper;
import com.ai.frame.dubbo.common.log.Logger;
import com.ai.frame.dubbo.common.log.LoggerFactory;
import com.ai.frame.dubbo.common.util.ClassUtil;
import com.ai.frame.dubbo.common.util.Constants;
import com.ai.frame.dubbo.common.util.StringUtil;
import com.ai.frame.dubbo.remote.log.AppOperLog;
import com.ai.frame.dubbo.spi.IControlService;
import com.ai.frame.dubbo.spi.InputObject;
import com.ai.frame.dubbo.spi.OutputObject;

public class ControlService implements IControlService {
	private Logger logger = LoggerFactory.getServiceLog(ControlService.class);
	private ContextHelper contextHelper;
	
	@SuppressWarnings("rawtypes")
	@Override
	public OutputObject execute(InputObject param) {
		OutputObject outObj = new OutputObject();
		
		long start = System.currentTimeMillis();
		if (param != null) {
			logger.info("from {} called the service[{}]'s [{}] method start.", null, param.getServerIp(),param.getService(),param.getMethod());
			
			Object object = contextHelper.getBean(param.getService());
			if(object!=null){
				Class[] paramcls = new Class[]{InputObject.class};
				try{
					OutputObject rtnobj = ClassUtil.invokMethod(OutputObject.class, object, param.getMethod(), paramcls, param);
					if(rtnobj!=null){
						
						logger.info("call the service [{}] 's retCode is {} and the rtnMsg is {}.", null,param.getService(),rtnobj.getReturnCode(),rtnobj.getReturnMessage());
						
						outObj = rtnobj;
					}else{
						outObj.setReturnCode(Constants.RTNCODE_ERR.getValue());
						outObj.setReturnMessage("call the service [" + param.getService() + "] return null.");
						
						logger.error("call the service [{}] return null{}.", null,param.getService(),StringUtil.EMPTYSTR);
					}
				}catch(Exception e){
					outObj.setReturnCode(Constants.RTNCODE_ERR.getValue());
					outObj.setReturnMessage("call the service [" + param.getService() + "] error:"+e.getMessage());
					
					logger.error("call the service [{}] error:{}", e, param.getService());
				}
			}else{
				outObj.setReturnCode(Constants.RTNCODE_ERR.getValue());
				outObj.setReturnMessage("can't find the service id [" + param.getService() + "].");
				
				logger.error("can't find the service id [{}]{}.", null,param.getService(),StringUtil.EMPTYSTR);
			}
		}else{
			outObj.setReturnCode(Constants.RTNCODE_ERR.getValue());
			outObj.setReturnMessage("the input param is null.");
			
			logger.error("the input param is null.{}{}", null,StringUtil.EMPTYSTR,StringUtil.EMPTYSTR);
		}
		long end = System.currentTimeMillis();
		
		//需要记录操作日志
		saveOperlog(param,outObj,end - start);
		
		return outObj;
	}
	
	private void saveOperlog(final InputObject inObj,final OutputObject outObj, final long calledTime) {
		AppOperLog applog = null;
		try {
			applog = contextHelper.getBean(AppOperLog.SERVICEID, AppOperLog.class);
		} catch (Exception e) {
			logger.error(AppOperLog.SERVICEID,"No bean named '{}' is defined.");
		}
		
		if (applog != null) {
			applog.saveOperLog(inObj, outObj, calledTime);
		}
	}
	
	public void setContextHelper(ContextHelper contextHelper) {
		this.contextHelper = contextHelper;
	}


}
