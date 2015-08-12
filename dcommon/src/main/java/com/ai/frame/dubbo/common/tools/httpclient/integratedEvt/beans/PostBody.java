package com.ai.frame.dubbo.common.tools.httpclient.integratedEvt.beans;

import com.ai.frame.dubbo.common.tools.httpclient.integratedEvt.IntegratedParam;
import com.ai.frame.dubbo.common.util.DateUtil;

public class PostBody {
	private BodyRequest Request;
	private BodyPubInfo PubInfo;
	
	public PostBody(){}
	public PostBody(BodyRequest Request,BodyPubInfo PubInfo){
		this.Request = Request;
		this.PubInfo = PubInfo;
	}
	public PostBody(Object busiParams, String busiCode, IntegratedParam params) {
		this.Request = new BodyRequest(busiParams,busiCode);
		
		String transactionId = getTransactionId(busiCode);
		BodyPubInfo pubInfo  = new BodyPubInfo();
		pubInfo.setInterfaceId(params.getCrmInterfaceId());
		pubInfo.setTransactionId(transactionId);
		pubInfo.setInterfaceType(params.getCrmInterfaceType());
		pubInfo.setOpId(params.getCrmOpId());
		pubInfo.setCountyCode(params.getCrmCountyCode());
		pubInfo.setOrgId(params.getCrmOrgId());
		pubInfo.setClientIP(params.getCrmClientIP());
		pubInfo.setRegionCode(params.getCrmRegionCode());
		pubInfo.setTransactionTime(DateUtil.getCurrentTime1());
		
		this.PubInfo = pubInfo;
		
	}
	private String getTransactionId(String busiCode){
        String ctime = DateUtil.getCurrentTime1();
        return busiCode+ "_" + ctime;
    }
	public BodyRequest getRequest() {
		return Request;
	}
	public void setRequest(BodyRequest request) {
		Request = request;
	}
	public BodyPubInfo getPubInfo() {
		return PubInfo;
	}
	public void setPubInfo(BodyPubInfo pubInfo) {
		PubInfo = pubInfo;
	}
	
}
