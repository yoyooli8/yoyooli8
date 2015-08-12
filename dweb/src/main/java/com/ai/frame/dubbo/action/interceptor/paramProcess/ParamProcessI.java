package com.ai.frame.dubbo.action.interceptor.paramProcess;

import com.ai.frame.dubbo.dconfig.control.bean.Parameter;


public interface ParamProcessI {
	/**
	 * 请求参数处理
	 * @return
	 */
	public abstract ParamProcessResult excute(Parameter param);
}
