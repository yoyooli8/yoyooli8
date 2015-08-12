package com.ai.frame.dubbo.dconfig.control.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Action实体.代表一个请求动作(Request/Response).
 * 
 * @since 2015-07-03
 */
public class Action extends Entity {
	private String path;          // Request请求路径
	private String service;       // Request请求调用后台的WebService服务类
	private String method;        // Request请求调用后台的WebService服务类的服务方法
	private List<Input> inputs;   // Request请求需要的参数
	private List<Output> outputs; // Request请求路径返回的数据
	private List<Result> results; // Request请求跳转路径

	//缓存数据便于查询用
    private Map<String,Input> inputmap = new HashMap<String, Input>();
    private Map<String,Output> outputmap = new HashMap<String, Output>();
    private Map<String,Result> resultmap = new HashMap<String, Result>();
    
	/** 构造器 **/
	public Action() {
		inputs = new ArrayList<Input>();
		outputs = new ArrayList<Output>();
		results = new ArrayList<Result>();
	}
	
	/**
	 * 添加子属性Input
	 * 
	 * @param input
	 *            子属性Input
	 */
	public void addInput(Input input) {
		inputs.add(input);
	}

	/**
	 * 添加子属性Output
	 * 
	 * @param output
	 *            子属性Output
	 */
	public void addOutput(Output output) {
		outputs.add(output);
	}

	/**
	 * 添加子属性Result
	 * 
	 * @param result
	 *            添加子属性Result
	 */
	public void addResult(Result result) {
		results.add(result);
	}

	/** Setters And Getters **/
	public List<Input> getInputs() {
		return inputs;
	}

	public void setInputs(List<Input> inputs) {
		this.inputs = inputs;
	}

	public List<Output> getOutputs() {
		return outputs;
	}

	public void setOutputs(List<Output> outputs) {
		this.outputs = outputs;
	}

	public List<Result> getResults() {
		return results;
	}

	public void setResults(List<Result> results) {
		this.results = results;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
	
	/*****/
	private Result findResult(String key){
        for(Result result :results){
            if(key.equals(result.getKey())){
                return result;
            }
        }
        return null;
    }
    public Result getResult(String uid){
        if (uid != null && uid.length() > 0) {
            Result result = resultmap.get(uid);
            if(result == null){
                result = findResult(uid);
                if(result != null){
                    resultmap.put(uid,result);
                    return result;
                }else{
                    return null;
                }
            }
            return result;
        }
        return null;
    }
    public Output getOutput(String uid){
        if(uid != null && uid.length() > 0){
            Output output = outputmap.get(uid);
            if(output == null){
                output = findOutput(uid);
                if(output != null){
                    outputmap.put(uid,output);
                    return output;
                }else{
                    return null;
                }
            }
            return output;
        }
        return null;
    }
    private Output findOutput(String uid){
        for(Output output :outputs){
            if(uid.equals(output.getUid())){
                return output;
            }
        }
        return null;
    }
    public Input getInput(String uid){
        if(uid != null && uid.length() > 0){
            Input input = inputmap.get(uid);
            if(input == null){
                input = findInput(uid);
                if(input != null){
                    inputmap.put(uid,input);
                    return input;
                }else{
                    return null;
                }
            }else{
                return input;
            }
        }
        return null;
    }
    private Input findInput(String uid){
        for(Input input :inputs){
            if(uid.equals(input.getUid())){
                return input;
            }
        }
        return null;
    }
    
    public int hashCode() {
        int result = super.hashCode() + ((path == null) ? 0 : path.hashCode());
        return result;
    }
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        if (this == obj)
            return true;

        final Action other = (Action) obj;

        if (path == null || path.length() < 1) {
            return false;
        } else {
            return path.equals(other.path);
        }
    }
}
