package com.ai.frame.dubbo.spi;


public interface IControlService {
	/**
	 * 统一远程调用入口方法
	 * @param param.service 后台实际调用service的id      (必须)
	 * @param param.method  后台实际调用service中具体方法 (必须)
	 * 
	 * @param inputObject 统一远程调用入参
	 * @return OutputObject 统一远程调用出参
	 */
	public OutputObject execute(InputObject param);

}
