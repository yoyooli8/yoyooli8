package com.ai.frame.dubbo.common.tools.httpclient;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.ai.frame.dubbo.common.log.Logger;
import com.ai.frame.dubbo.common.log.LoggerFactory;
import com.ai.frame.dubbo.common.util.IOUtil;
import com.ai.frame.dubbo.common.util.StringUtil;

public class HttpClientHelper {
	public static final String REQ_JSON_PARAM = "jsonParams";
	private int requestTimeout = 6;
    private int connectTimeout = 6;
    private int reRequestTimes = 6;
    private String httpReqUrl;
    private String httpReqPort;
    private String httpReqServiceName;
    private Logger logger = LoggerFactory.getUtilLog(HttpClientHelper.class);
    
    private ThreadLocal<HttpClient> httpClient = new ThreadLocal<HttpClient>();
    
    /**从网络上下载文件*/
    public boolean getFileFromRemote(String fileUrl,String storeFile){
    	HttpClient hclient = getHttpClient();
    	GetMethod get = new GetMethod();
    	
    	Header getheader = new Header();
    	getheader.setName("Content-Type");
    	getheader.setValue("application/x-www-form-urlencoded;charset=UTF-8");
    	get.setRequestHeader(getheader);
    	
    	get.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, requestTimeout * 1000);
    	try {
    		get.setURI(new URI(fileUrl,true));
			hclient.executeMethod(get);
			
			int rtnStatus = get.getStatusCode();
			if(rtnStatus==200){
				IOUtil.bytes2File(get.getResponseBody(), storeFile, 1024);
				return true;
			}else{
				logger.error("down load file[{}] error. state:{}", null, fileUrl,String.valueOf(rtnStatus));
				return false;
			}
		} catch (Exception e) {
			logger.error("down load file[{}] error:{}", e, fileUrl);
			return false;
		}finally{
			relaseConnection(get);
		}
    }
    private void relaseConnection(GetMethod get){
        if(get!=null){
        	get.releaseConnection();
        }
    }
    /**
     * 简单请求发送
     * @param data     请求参数NameValuePair
     * @param busiCode 服务标识号,记录日志用
     * @return
     */
    public String getHttpResonseJson(final Map<String,String> data,String busiCode){
    	Iterator<Map.Entry<String, String>> mpit = data.entrySet().iterator();
    	NameValuePair[] params = new NameValuePair[data.size()];
    	int i = 0;
    	while(mpit.hasNext()){
    		Map.Entry<String, String> mpent = mpit.next();
    		params[i] = new NameValuePair(mpent.getKey(),mpent.getValue());
    		
    		i++;
    	}
    	return getHttpResonseJson(params,busiCode);
    }
    public String getHttpResonseJson(final NameValuePair[] data,String busiCode){
    	GetHttpRespJson respJson = new GetHttpRespJson(){
            Map<Integer, String> invokRmService(String busiParams, Object... params) {
                return invokService(data);
            }
        };
        return respJson.getHttpResonseJson(null, busiCode, (Object[])null);
    }
    private Map<Integer,String> invokService(final NameValuePair[] data){
        PostExecute postExecute = new PostExecute(){
            @Override
            void setRequestBody(PostMethod post,String busiParams, Object... files)throws Exception{
                post.setRequestBody(data);
            }
        };
        return postExecute.invokService(null, (Object[])null);
    }
    /**
     * 简单请求发送
     * @param busiParams 请求参数json串
     * @param busiCode   服务标识号,记录日志用
     * */
    public String getHttpResonseJson(String busiParams,String busiCode){
        GetHttpRespJson respJson = new GetHttpRespJson(){
            Map<Integer, String> invokRmService(String busiParams, Object... params) {
                return invokService(busiParams);
            }
        };
        return respJson.getHttpResonseJson(busiParams, busiCode, (Object[])null);
    }
    private Map<Integer,String> invokService(String busiParams){
        PostExecute postExecute = new PostExecute(){
            @Override
            void setRequestBody(PostMethod post,String busiParams, Object... files)throws Exception{
                NameValuePair[] data = new NameValuePair[]{new NameValuePair(REQ_JSON_PARAM, busiParams)};
                post.setRequestBody(data);
            }
        };
        return postExecute.invokService(busiParams, (Object[])null);
    }
    /**
     * 带byte[]数组的请求发送
     * @param busiParams 请求参数json串
     * @param busiCode   服务标识号,记录日志用
     * */
    public String getMultipartHttpResonseJson(String busiParams,String busiCode,Map<String,byte[]> files){
        GetHttpRespJson respJson = new GetHttpRespJson(){
            @SuppressWarnings("unchecked")
            Map<Integer, String> invokRmService(String busiParams, Object... params) {
                Map<String,byte[]> fileParmas = (Map<String,byte[]>)params[0];
                return invokMultipartService(busiParams,fileParmas);
            }
        };
        return respJson.getHttpResonseJson(busiParams, busiCode, files);
    }
    private Map<Integer,String> invokMultipartService(String busiParams,Map<String,byte[]> files){
        PostExecute postExecute = new PostExecute(){
            @SuppressWarnings("unchecked")
            @Override
            void setRequestBody(PostMethod post,String busiParams, Object... filesArgs)throws Exception{
                Map<String,byte[]> files = (Map<String,byte[]>)filesArgs[0];
                Part[] parts = new Part[files.size()+1];
                StringPart stringpart = new StringPart(REQ_JSON_PARAM, busiParams);
                parts[0]=stringpart;

                Iterator<Map.Entry<String, byte[]>> it = files.entrySet().iterator();
                int i = 1;
                while(it.hasNext()){
                    Map.Entry<String, byte[]> fileEn = it.next();
                    String tmpFile = StringUtil.getUUID()+".tmp";
                    parts[i] = new FilePart(fileEn.getKey(),IOUtil.bytes2File(fileEn.getValue(), tmpFile, IOUtil.BUFFER));
                    i++;
                }
                MultipartRequestEntity postEntity = new MultipartRequestEntity(parts,post.getParams());
                post.setRequestEntity(postEntity);
            }
        };
        return postExecute.invokService(busiParams, files);
    }
    /**
     * 带File的文件上传请求发送
     * @param busiParams 请求参数json串
     * @param busiCode   服务标识号,记录日志用
     * */
    public String getMultipartHttpResonseJson1(String busiParams,String busiCode,Map<String,File> files){
        GetHttpRespJson respJson = new GetHttpRespJson(){
            @SuppressWarnings("unchecked")
            Map<Integer, String> invokRmService(String busiParams, Object... params) {
                Map<String,File> fileParmas = (Map<String,File>)params[0];
                return invokMultipartService1(busiParams,fileParmas);
            }
        };
        return respJson.getHttpResonseJson(busiParams, busiCode, files);
    }
    public String getHttpResonseJson(final Map<String,String> data,final String busiCode,Map<String,File> files){
    	GetHttpRespJson respJson = new GetHttpRespJson(){
            @SuppressWarnings("unchecked")
            Map<Integer, String> invokRmService(String busiParams, Object... params) {
            	Map<String,File> fileParmas = (Map<String,File>)params[0];
                return exeHttpResonseJson(busiParams,data,fileParmas);
            }
        };
        return respJson.getHttpResonseJson(null, busiCode, files);
    }
    public Map<Integer,String> exeHttpResonseJson(String busiParams,final Map<String,String> data,Map<String,File> files){
    	PostExecute postExecute = new PostExecute(){
            @SuppressWarnings("unchecked")
            @Override
            void setRequestBody(PostMethod post,String busiParams, Object... filesArgs)throws Exception{
                Map<String,File> files = (Map<String,File>)filesArgs[0];
                Part[] parts = new Part[files.size()+data.size()];
                Iterator<Map.Entry<String, String>> mpit = data.entrySet().iterator();
                int i = 0;
            	while(mpit.hasNext()){
            		Map.Entry<String, String> mpent = mpit.next();
            		parts[i] = new StringPart(mpent.getKey(),mpent.getValue());
            		i++;
            	}

                Iterator<Map.Entry<String, File>> it = files.entrySet().iterator();
                while(it.hasNext()){
                    Map.Entry<String, File> fileEn = it.next();
                    parts[i] = new FilePart(fileEn.getKey(),fileEn.getValue());
                    i++;
                }
                MultipartRequestEntity postEntity = new MultipartRequestEntity(parts,post.getParams());
                post.setRequestEntity(postEntity);
            }
        };
        return postExecute.invokService(busiParams, files);
    }
    private Map<Integer,String> invokMultipartService1(String busiParams,Map<String,File> files){
        PostExecute postExecute = new PostExecute(){
            @SuppressWarnings("unchecked")
            @Override
            void setRequestBody(PostMethod post,String busiParams, Object... filesArgs)throws Exception{
                Map<String,File> files = (Map<String,File>)filesArgs[0];
                Part[] parts = new Part[files.size()+1];
                StringPart stringpart = new StringPart(REQ_JSON_PARAM, busiParams);
                parts[0]=stringpart;

                Iterator<Map.Entry<String, File>> it = files.entrySet().iterator();
                int i = 1;
                while(it.hasNext()){
                    Map.Entry<String, File> fileEn = it.next();
                    //PartSource filesource   = new FilePartSource(fileEn.getKey(),fileEn.getValue());
                    parts[i] = new FilePart(fileEn.getKey(),fileEn.getValue());
                    i++;
                }
                MultipartRequestEntity postEntity = new MultipartRequestEntity(parts,post.getParams());
                post.setRequestEntity(postEntity);
            }
        };
        return postExecute.invokService(busiParams, files);
    }
    private void setPostHeaders(PostMethod post,boolean header){
        // add post header
    	if(header){
    		Header postheader = new Header();
            postheader.setName("Content-Type");
            postheader.setValue("application/x-www-form-urlencoded;charset=UTF-8");
            post.setRequestHeader(postheader);
    	}
        
        //request time out
        post.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, requestTimeout * 1000);
    }
    public HttpClient getHttpClient(){
        HttpClient client = httpClient.get();
        if(client == null){
            client = new HttpClient();
            // 设置连接请求超时参数
            client.getHttpConnectionManager().getParams().setConnectionTimeout(connectTimeout * 1000);
            httpClient.set(client);
        }
        return client;
    }
    public void relaseConnection(PostMethod post){
        if(post!=null){
            post.releaseConnection();
        }
    }
    private String getRequestUrl(){
        StringBuffer httpUrl = new StringBuffer();
        if(StringUtil.str2Int(httpReqPort) == 80){
        	httpUrl.append("http://").append(httpReqUrl).append("/").append(httpReqServiceName);
        }else{
        	httpUrl.append("http://").append(httpReqUrl).append(":").append(httpReqPort).append("/").append(httpReqServiceName);
        }
        
        String reqUrl = httpUrl.toString();

        logger.info("invokService URL.set URL={}. method[{}]",null,reqUrl,"getRequestUrl");
        return reqUrl;
    }
    abstract class PostExecute{
    	public Map<Integer,String> invokService(String busiParams,Object...files){
    		logger.info("invokMultipartService start...json.params:{},files:{}.",null,busiParams,String.valueOf(files==null?0:files.length));
    		PostMethod post = new PostMethod();
    		//add post header
    		boolean isheader = true;
    		if(files!=null && files.length>0)isheader=false;
    		setPostHeaders(post,isheader);
    		
    		int rtnStatus  = 0;
    		String rtnJson = null;
    		try {
                post.setURI(new URI(getRequestUrl(),true));
                HttpClient httpclient = getHttpClient();
                setRequestBody(post,busiParams,files);

                httpclient.executeMethod(post);
                rtnStatus = post.getStatusCode();
                if(rtnStatus==200){
                    rtnJson = new String(post.getResponseBody(), "UTF-8");
                }else{
                    rtnJson = new String(post.getResponseBody(), "UTF-8");
                }
                logger.info("invokService's response:{}={}",null,String.valueOf(rtnStatus),rtnJson);
            }catch (Exception e) {
                rtnStatus = 0;
                rtnJson = "";
                logger.error("invokService's Request error:{} . method[{}]",e,"invokService");
            }finally{
                relaseConnection(post);
            }
            
            Map<Integer,String> rtnMap = new HashMap<Integer, String>();
            rtnMap.put(Integer.valueOf(rtnStatus), rtnJson);
            return rtnMap;
    	}
    	
    	abstract void setRequestBody(PostMethod post,String busiParams,Object...files) throws Exception;
    }
    abstract class GetHttpRespJson{
    	 public String getHttpResonseJson(String busiParams,String busiCode,Object...params){
    		 long startTime = System.currentTimeMillis();
             int reqNum = 0;
             Map<Integer, String> rtnMap = null;
             while (reqNum < reRequestTimes) {
                 rtnMap = invokRmService(busiParams,params);
                 if (rtnMap != null && rtnMap.containsKey(Integer.valueOf(200))) {
                     break;
                 }else {
                     reqNum++;
                     logger.info("{} invokService Request {} times failure! method[getHttpResonseJson]",null,busiCode,String.valueOf(reqNum));
                 }
             }
             long endTime = System.currentTimeMillis();
             logger.info("invokService busiCode:{} ,used time {} ms. method[getHttpResonseJson]",null,busiCode,String.valueOf(endTime - startTime));

             if (rtnMap != null) {
                 String rtnJson = rtnMap.get(Integer.valueOf(200));
                 if (rtnJson != null && rtnJson.length() > 0) {
                     String rsp = rtnMap.get(Integer.valueOf(200));
                     return rsp;
                 }
             }
             logger.info("{} the response is empty.",busiCode);
             return "";
    	 }
    	 
    	 abstract Map<Integer, String> invokRmService(String busiParams,Object...params);
    }
    /**请求超时时间,单位秒*/
	public void setRequestTimeout(int requestTimeout) {
		this.requestTimeout = requestTimeout;
	}
	/**连接超时时间,单位秒*/
	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}
	/**请求失败重连次数*/
	public void setReRequestTimes(int reRequestTimes) {
		this.reRequestTimes = reRequestTimes;
	}
	/**请求连接*/
	public void setHttpReqUrl(String httpReqUrl) {
		this.httpReqUrl = httpReqUrl;
	}
	/**请求连接端口*/
	public void setHttpReqPort(String httpReqPort) {
		this.httpReqPort = httpReqPort;
	}
	/**请求连接服务名*/
	public void setHttpReqServiceName(String httpReqServiceName) {
		this.httpReqServiceName = httpReqServiceName;
	}
}
