package com.ai.frame.dubbo.common.tools.httpclient.integratedEvt;

/**crm集成环境接口相关参数**/
public class IntegratedParam {
    /**esb params**/
    private String esbOperationPrefix;
    private String esbClientId;
    private String httpEsbUrl;
    private String httpEsbPort;
    
    /**Integrated params*/
    private String httpReqUrl;
    private String httpReqPort;
    private String httpReqServiceName;
    
    /**common params*/
    private String crmInterfaceId;
    private String crmInterfaceType;
    private String crmOpId;
    private String crmCountyCode;
    private String crmOrgId;
    private String crmClientIP;
    private String crmRegionCode;
	public String getEsbOperationPrefix() {
		return esbOperationPrefix;
	}
	public void setEsbOperationPrefix(String esbOperationPrefix) {
		this.esbOperationPrefix = esbOperationPrefix;
	}
	public String getEsbClientId() {
		return esbClientId;
	}
	public void setEsbClientId(String esbClientId) {
		this.esbClientId = esbClientId;
	}
	public String getHttpEsbUrl() {
		return httpEsbUrl;
	}
	public void setHttpEsbUrl(String httpEsbUrl) {
		this.httpEsbUrl = httpEsbUrl;
	}
	public String getHttpEsbPort() {
		return httpEsbPort;
	}
	public void setHttpEsbPort(String httpEsbPort) {
		this.httpEsbPort = httpEsbPort;
	}
	public String getHttpReqUrl() {
		return httpReqUrl;
	}
	public void setHttpReqUrl(String httpReqUrl) {
		this.httpReqUrl = httpReqUrl;
	}
	public String getHttpReqPort() {
		return httpReqPort;
	}
	public void setHttpReqPort(String httpReqPort) {
		this.httpReqPort = httpReqPort;
	}
	public String getHttpReqServiceName() {
		return httpReqServiceName;
	}
	public void setHttpReqServiceName(String httpReqServiceName) {
		this.httpReqServiceName = httpReqServiceName;
	}
	public String getCrmInterfaceId() {
		return crmInterfaceId;
	}
	public void setCrmInterfaceId(String crmInterfaceId) {
		this.crmInterfaceId = crmInterfaceId;
	}
	public String getCrmInterfaceType() {
		return crmInterfaceType;
	}
	public void setCrmInterfaceType(String crmInterfaceType) {
		this.crmInterfaceType = crmInterfaceType;
	}
	public String getCrmOpId() {
		return crmOpId;
	}
	public void setCrmOpId(String crmOpId) {
		this.crmOpId = crmOpId;
	}
	public String getCrmCountyCode() {
		return crmCountyCode;
	}
	public void setCrmCountyCode(String crmCountyCode) {
		this.crmCountyCode = crmCountyCode;
	}
	public String getCrmOrgId() {
		return crmOrgId;
	}
	public void setCrmOrgId(String crmOrgId) {
		this.crmOrgId = crmOrgId;
	}
	public String getCrmClientIP() {
		return crmClientIP;
	}
	public void setCrmClientIP(String crmClientIP) {
		this.crmClientIP = crmClientIP;
	}
	public String getCrmRegionCode() {
		return crmRegionCode;
	}
	public void setCrmRegionCode(String crmRegionCode) {
		this.crmRegionCode = crmRegionCode;
	}
    
}
