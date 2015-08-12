package com.ai.frame.dubbo.common.util;

public enum Constants {
	UID("uid"),
	RTNCODE_SUC("1"),
	RTNCODE_ERR("0"),
	PARAM_FORWORD_NAME("forwordTo"),
	PARAM_DIRECT_NAME("isDirect"),
	LOGIN_USER_SESSIONID("login-user-sessionId"),
	CONTEXTHELPER_ID("contextHelper"),
	SPRINGUTIL_ID("springUtil"),
	ACTION_ERR_KEY("ACTION_ERR_KEY"),
	ERR_DAO_NAME("dao.error"),
	ERR_ACTION_NAME("action.error"),
	ERR_SERVICE_NAME("service.error"),
	ERR_FRAME_NAME("aiframe.error"),
	REMOTE_SERVICE_ID("ai_nb_rmsvId"),
	UPNM_FILE_TYPE("ContentType"),
	UPNM_FILE_NAME("FileName"),
	UPNM_FILE_TYPEALLOW("upload.file.types"),
	INPUT_SCOPE_REQ("request"),
	INPUT_SCOPE_CON("control"),
	PARAM_SCOPE_PRO("properties"),
	PARAM_SCOPE_FIL("file"),
	PARAM_SCOPE_CON("constants"),
	PARAM_SCOPE_SES("session"),
	PARAM_SCOPE_REQ("request");
	
	private String val = "";
	private Constants(String val){
		this.val = val;
	}
	public String getValue(){
		return this.val;
	}
	
	public String toString(){
		return this.val;
	}
}
