package com.ai.frame.dubbo.action.spring;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ai.frame.dubbo.action.interceptor.HttpRequestWrapper;
import com.ai.frame.dubbo.common.util.IOUtil;
import com.ai.frame.dubbo.common.util.StringUtil;

public class SpringHttpRequestWrapper extends HttpRequestWrapper {
	public SpringHttpRequestWrapper(HttpServletRequest request) {
		super(request);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map<String, Object> getParameters() {
		if(request instanceof MultipartHttpServletRequest){
			MultipartHttpServletRequest mreq = (MultipartHttpServletRequest)request;
			Map parameters = mreq.getParameterMap();
			Map<String,MultipartFile> fileMap = mreq.getFileMap();
			Iterator<Map.Entry<String, MultipartFile>> mapEntry = fileMap.entrySet().iterator();
			
			while(mapEntry.hasNext()){
				try {
					Map.Entry<String, MultipartFile> fileEntry = mapEntry.next();
					String fileName = fileEntry.getKey();
					MultipartFile filePart = fileEntry.getValue();
					
					String uploadPath = request.getSession().getServletContext().getRealPath("/upload");
					File filePath = new File(uploadPath);
					if(!filePath.exists()){
						filePath.mkdir();
					}
					String fileFullName = filePart.getOriginalFilename();
					String extName = "";
					if(fileFullName.indexOf(".")>-1){
						extName = fileFullName.substring(fileFullName.lastIndexOf("."));
					}
					uploadPath = uploadPath + File.separator + StringUtil.getUUID() + extName;
					
					File file = IOUtil.bytes2File(filePart.getBytes(), uploadPath,IOUtil.BUFFER);
					String fileFileName = fileName + "FileName";
					String fileFileVal  = filePart.getName() + extName;
					String fileFileType = fileName + "ContentType";
					String fileFileTypeVal = filePart.getContentType();
					
					parameters.remove(fileName);
					parameters.put(fileName, new File[]{file});
					parameters.put(fileFileName, new String[]{fileFileVal});
					parameters.put(fileFileType, new String[]{fileFileTypeVal});
					
				} catch (Exception e) {
				}
			}
			return parameters;
		}
		return request.getParameterMap();
	}

	@Override
	public boolean isMultiPartRequest() {
		if(request instanceof MultipartHttpServletRequest){
			return true;
		}
		
		return false;
	}

}
