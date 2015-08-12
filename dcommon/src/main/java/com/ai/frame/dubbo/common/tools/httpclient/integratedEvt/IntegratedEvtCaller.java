package com.ai.frame.dubbo.common.tools.httpclient.integratedEvt;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.ai.frame.dubbo.common.helper.JsonHelper;
import com.ai.frame.dubbo.common.log.Logger;
import com.ai.frame.dubbo.common.log.LoggerFactory;
import com.ai.frame.dubbo.common.tools.httpclient.integratedEvt.beans.CallerResponse;
import com.ai.frame.dubbo.common.tools.httpclient.integratedEvt.beans.PostBody;
import com.ai.frame.dubbo.common.util.DateUtil;

/***CRM集成环境接口调用入口类**/
public class IntegratedEvtCaller {
	/**1:ESB closed  0:ESB opened*/
    private int closeESB = 0;
    /**调用请求超时时间，单位秒**/
    private int requestTimeout = 6;
    /**调用连接超时时间，单位秒**/
    private int connectTimeout = 6;
    /**接口调用失败后，重复次数*/
    private int reRequestTimes = 6;
    /**CRM集成环境接口相关参数**/
	private IntegratedParam params;
	private JsonHelper jsonHelper;
	
	private ThreadLocal<HttpClient> crmHttpClient = new ThreadLocal<HttpClient>();
	private Logger log = LoggerFactory.getOuterCallerLog(IntegratedEvtCaller.class);
    
	/**
	 * 调用crm集成环境接口
	 * @param busiParams 接口入参
	 * @param busiCode   接口编码
	 * @return  返回接口调用返回的json封装成对象CallerResponse 
	 * 			用法看 common.ai.tools.common.ai.tools.IntegratedEvtCallerTest和common.ai.tools.common.ai.tools.IntegratedEvtCallerTest2
	 */
	public CallerResponse getHttpResonseJson(Object busiParams, String busiCode) {
		long startTime = System.currentTimeMillis();
		int reqNum = 0;
        Map<Integer, String> rtnMap = null;
        while (reqNum < reRequestTimes) {
        	rtnMap = invokService(busiParams, busiCode);
        	if (rtnMap != null && rtnMap.containsKey(Integer.valueOf(200))) {
                break;
            } else {
                reqNum++;
                log.info("invokService Request {} times failure! busiCode is {}", null, String.valueOf(reqNum),busiCode);
            }
        }
        long endTime = System.currentTimeMillis();
        
        log.info("invokService busiCode:{} ,used time:{} ms.method[{}]", null,busiCode,String.valueOf(endTime - startTime),"getHttpResonseJson");
        CallerResponse response = null;
        if (rtnMap != null) {
            String rtnJson = rtnMap.get(Integer.valueOf(200));
            if (rtnJson != null && rtnJson.length() > 0) {
            	response = jsonHelper.convertJson2Object(rtnJson, CallerResponse.class);
            	return response;
            }
        }
        response = new CallerResponse(CallerResponse.CODE_ERROR,"集成环境连接失败");
        return response;
	}
	private Map<Integer,String> invokService(Object busiParams,String busiCode){
		PostMethod post = new PostMethod();
		setParams2PostMethod(post,busiCode);
		
		StringBuffer httpUrl = new StringBuffer();
		if(closeESB == 0){// 调用ESB平台接口
            httpUrl.append("http://").append(params.getHttpEsbUrl()).append(":").append(params.getHttpEsbPort()).append("/").append(params.getEsbOperationPrefix());
        }else{
            httpUrl.append("http://").append(params.getHttpReqUrl()).append(":").append(params.getHttpReqPort()).append("/").append(params.getHttpReqServiceName());
        }
		String reqUrl = httpUrl.toString();
        log.info("{} invokService URL.set URL={}.method[{}]", null,busiCode,reqUrl,"invokService");
        int rtnStatus  = 0;
        String rtnJson = null;
        try {
        	post.setURI(new URI(reqUrl,true));    //设置请求URL
            HttpClient httpclient = getHttpClient();
            String postReqBody = getPostBodyJson(busiParams,busiCode);  //获得请求内容
            log.info("{} invokService Request Body. request parameters:{}.method[{}]", null,busiCode,postReqBody,"invokService");
            
            RequestEntity requestEntity = new StringRequestEntity(postReqBody);
            post.setRequestEntity(requestEntity);    //设置请求内容
            httpclient.executeMethod(post);
            rtnStatus = post.getStatusCode();
            if(rtnStatus==200){
                rtnJson = new String(post.getResponseBody(), "UTF-8");
            }else{
                rtnJson = "";
            }
            log.info("{} called's response:{}", null,"invokService",rtnJson);
            
        } catch (Exception e) {
        	rtnStatus = 0;
            rtnJson = "";
            
            log.error("{} invokService Request error:{} . method[{}]", e,busiCode,"invokService");
        }finally{relaseConnection(post);}
        
        Map<Integer,String> rtnMap = new HashMap<Integer, String>();
        rtnMap.put(Integer.valueOf(rtnStatus), rtnJson);
        return rtnMap;
	}
	private String getPostBodyJson(Object busiParams,String busiCode){
		PostBody postBody = new PostBody(busiParams,busiCode,params);
		
		return jsonHelper.convertObject2Json(postBody);
	}

	private void setParams2PostMethod(PostMethod post,String busiCode){
		Header contentTypeheader = new Header();
		if(closeESB == 0){// 调用ESB平台接口
			log.info("{} set postMethod cofing data for ESB .method[{}]", null,busiCode,"setParams2PostMethod");
			
			addEsbHeaders(post,busiCode);
            contentTypeheader.setName("Content-Type");
            contentTypeheader.setValue("application/x-www-form-urlencoded;charset=UTF-8"); 
		}else{
			log.info("{} set postMethod cofing data .method[{}]", null,busiCode,"setParams2PostMethod");
			
			contentTypeheader.setName("Content-Type");
            contentTypeheader.setValue("application/json;charset=UTF-8");
		}
		// hearder设置
		post.setRequestHeader(contentTypeheader);
		
		post.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, requestTimeout * 1000);
	}
	private void addEsbHeaders(PostMethod post,String busiCode){
		post.addRequestHeader("OperationCode", params.getEsbOperationPrefix()+"."+busiCode);
		post.addRequestHeader("ClientId", params.getEsbClientId());
		String ctransactionId = getTransactionId(busiCode);
		post.addRequestHeader("TransactionId", ctransactionId);
	}
	private String getTransactionId(String busiCode){
        String ctime = DateUtil.getCurrentTime1();
        return busiCode+ "_" + ctime;
    }

	private HttpClient getHttpClient(){
        HttpClient client = crmHttpClient.get();
        if(client == null){
            client = new HttpClient();
            // 设置连接请求超时参数
            client.getHttpConnectionManager().getParams().setConnectionTimeout(connectTimeout * 1000);
            crmHttpClient.set(client);
        }
        return client;
    }
	private void relaseConnection(PostMethod post){
        if(post!=null){
            post.releaseConnection();
        }
    }
	
	/**1:ESB closed  0:ESB opened*/
	public void setCloseESB(int closeESB) {
		this.closeESB = closeESB;
	}
	public void setRequestTimeout(int requestTimeout) {
		this.requestTimeout = requestTimeout;
	}
	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}
	
	/**error call repeat times*/
	public void setReRequestTimes(int reRequestTimes) {
		this.reRequestTimes = reRequestTimes;
	}
	public void setParams(IntegratedParam params) {
		this.params = params;
	}
}
