package com.ai.frame.dubbo.test.core.util;

public enum Constant {
	appHome("busi.app.home"),
	pluginHome("busi.plugin.home");
	private String str;
	
	private Constant(String str){
		this.str = str;
	}
	public String getVal(){
		return str;
	}
}
