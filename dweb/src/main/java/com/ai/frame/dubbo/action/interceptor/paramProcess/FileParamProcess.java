package com.ai.frame.dubbo.action.interceptor.paramProcess;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ai.frame.dubbo.action.interceptor.HttpRequestWrapper;
import com.ai.frame.dubbo.action.interceptor.RequestInterceptor;
import com.ai.frame.dubbo.common.util.Constants;
import com.ai.frame.dubbo.common.util.IOUtil;
import com.ai.frame.dubbo.common.util.StringUtil;
import com.ai.frame.dubbo.dconfig.control.bean.Parameter;
import com.ai.frame.dubbo.spi.FileParam;
import com.ai.frame.dubbo.spi.InputObject;

public class FileParamProcess extends AbstractParamProcess {

	public FileParamProcess(InputObject inputObject,
			HttpRequestWrapper request, RequestInterceptor reqInterceptor) {
		super(inputObject, request, reqInterceptor);
	}

	public ParamProcessResult excute(Parameter param) {
		Map<String, Object> filepmVals = request.getParameters();
		ParamProcessResult result = paramProcessResult(inputObject, filepmVals,
				param.getKey(), param.getToKey());
		result.getInputObject().setMultiParams(true);

		return result;
	}

	private ParamProcessResult paramProcessResult(InputObject inputObject,
			Map<String, Object> filepmVals, String key, String toKey) {
		ParamProcessResult result = new ParamProcessResult(inputObject);

		File[] fileVals = (File[]) filepmVals.get(key);
		if (fileVals != null) {
			int index = 0;
			String[] fileNameVals = (String[]) filepmVals.get(key + Constants.UPNM_FILE_NAME.getValue());
			String validateTypes = reqInterceptor.getValueFromProperties(Constants.UPNM_FILE_TYPEALLOW.getValue());
			List<FileParam> files = new ArrayList<FileParam>();
			for (File file : fileVals) {
				String fileTypeVal = getFileType(fileNameVals[index]);
				if (validateFileType(validateTypes, fileTypeVal)) {
					String fieldname = key;
					if(!StringUtil.isEmpty(toKey)){
						fieldname = toKey;
					}
					
					FileParam fileParam = new FileParam(fieldname, fileNameVals[index], fileTypeVal, IOUtil.file2bytes(file, IOUtil.BUFFER));
					files.add(fileParam);
				} else {
					result.setRtnCode(ParamProcessResult.RTN_ERROR);
					result.setRtnMsg("file type [" + fileTypeVal + "] does not allow");
					return result;
				}
				index++;
			}
			inputObject.addFiles(key, toKey, files.toArray(new FileParam[files.size()]));
		}
		
		result.setRtnCode(ParamProcessResult.RTN_SUCESS);
		return result;
	}

	private String getFileType(String fileName) {
		int index = fileName.lastIndexOf(".");
		if (index != -1)
			return fileName.substring(index);
		return "";
	}

	private boolean validateFileType(String validateTypes, String fileTypeVal) {
		if (StringUtil.isEmpty(validateTypes)) {
			return true;
		} else {
			if (validateTypes.indexOf(fileTypeVal) > -1) {
				return true;
			} else {
				return false;
			}
		}
	}
}
