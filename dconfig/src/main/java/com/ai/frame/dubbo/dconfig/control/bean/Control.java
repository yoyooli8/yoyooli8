package com.ai.frame.dubbo.dconfig.control.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Control实体.整个应用的请求控制器.
 * 
 * @since 2012-07-03
 */
public class Control extends Entity {
	private List<Action> actions;
	private Map<String,Action> actionmap = new HashMap<String, Action>();
	
	/** 构造器 **/
	public Control() {
		actions = new ArrayList<Action>();
	}
	public Control(List<Action> actions) {
        this.actions = actions;
    }
	
	/**
	 * 添加子属性Action
	 * 
	 * @param action
	 *            子属性Action
	 */
	public void addAction(Action action) {
		actions.add(action);
	}

	/** Setters And Getters **/
	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}
	
	public Action getAction(String path){
        if(path!=null && path.length()>0){
            Action action = actionmap.get(path);
            if(action == null){
                action = findAction(path);
                if(action != null){
                    actionmap.put(path,action);
                    return action;
                }else{
                    return null;
                }
            }
            return action;
        }
        return null;
    }
	
	private Action findAction(String path){
        for(Action action :actions){
            if(path.equals(action.getPath())){
                return action;
            }
        }
        return null;
    }
}
