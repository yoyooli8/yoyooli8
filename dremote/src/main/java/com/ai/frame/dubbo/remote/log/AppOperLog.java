package com.ai.frame.dubbo.remote.log;

import com.ai.frame.dubbo.spi.InputObject;
import com.ai.frame.dubbo.spi.OutputObject;

/**日志记录,为不影响应用业务逻辑，可以另起线程进行操作*/
public interface AppOperLog {
	int MAXSTRLEN = 3990;
	String SERVICEID = "appOperLog";
	String APPLOGPARAMKEY = "appOperlog";
	String APPLOGOPERATOR = "logOperator";
	public int saveOperLog(InputObject inObj,OutputObject outObj,long calledTime);
}
