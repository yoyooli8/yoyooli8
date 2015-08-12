package com.ai.frame.dubbo.action;

import com.ai.frame.dubbo.action.interceptor.paramProcess.ParamProcessResult;
import com.ai.frame.dubbo.dconfig.control.bean.Input;
import com.ai.frame.dubbo.dconfig.control.bean.Output;
import com.ai.frame.dubbo.spi.InputObject;

public class ThreadLocalCache {
	private static final ThreadLocalCache instance = new ThreadLocalCache();
	private ThreadLocal<ParamProcessResult> errorResult = new ThreadLocal<ParamProcessResult>();
	private ThreadLocal<InputObject> inputObject = new ThreadLocal<InputObject>();
	private ThreadLocal<Output> output = new ThreadLocal<Output>();
	private ThreadLocal<Input> input = new ThreadLocal<Input>();
	
	public void setInput(Input input){
		this.input.remove();
		
		this.input.set(input);
	}
	public Input getInput(){
		return input.get();
	}
	
	public void setInputObject(InputObject inputObject){
		this.inputObject.remove();
		
		this.inputObject.set(inputObject);
	}
	public InputObject getInputObject(){
		return inputObject.get();
	}
	
	public void setOutput(Output outPut){
		this.output.remove();
		
		this.output.set(outPut);
	}
	public Output getOutput(){
		return output.get();
	}
	public void setParamProcessErrorResult(ParamProcessResult processResult){
		errorResult.remove();
		
		errorResult.set(processResult);
	}
	public ParamProcessResult getParamProcessErrorResult(){
		return errorResult.get();
	}
	
	public static ThreadLocalCache getInstance(){
		return instance;
	}
	private ThreadLocalCache(){}
}
