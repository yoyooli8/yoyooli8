package com.ai.frame.dubbo.action.interceptor.param;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import com.ai.frame.dubbo.action.interceptor.HttpRequestWrapper;
import com.ai.frame.dubbo.common.util.Constants;
import com.ai.frame.dubbo.dconfig.control.bean.Input;
import com.ai.frame.dubbo.dconfig.control.bean.Parameter;

public class RequestInputParameters extends AbstractorInputParameter {
	public RequestInputParameters(HttpRequestWrapper request, Input input) {
		super(request, input);
	}

	public List<Parameter> getParameters() {
		List<Parameter> parameters = new ArrayList<Parameter>();
		Enumeration<String> enumName = request.getParameterNames();

		while (enumName.hasMoreElements()) {
			String name = enumName.nextElement();

			Parameter param = new Parameter();
			param.setKey(name);
			param.setToKey(name);
			param.setScope(Constants.PARAM_SCOPE_REQ.getValue());
			
			if(!parameters.contains(param)) parameters.add(param);
		}

		for (Parameter param : input.getParameters()) {
			if (param.getScope() != null
					&& !Constants.PARAM_SCOPE_REQ.getValue().equals(param.getScope())
					&& !Constants.PARAM_SCOPE_FIL.getValue().equals(param.getScope())) {
				
				if(param!= null && !parameters.contains(param)) parameters.add(param);
			}
		}

		// add file params
		if (request.isMultiPartRequest()) {
			String[] uploadFilenames = getUploadFileNames();
			for (String fileName : uploadFilenames) {
				Parameter param = new Parameter();
				param.setKey(fileName);
				param.setToKey(fileName);
				param.setScope(Constants.PARAM_SCOPE_FIL.getValue());

				if(!parameters.contains(param)) parameters.add(param);
			}
		}

		return parameters;
	}

	private String[] getUploadFileNames() {
		Map<String, Object> parameters = request.getParameters();
		List<String> uploadFileNames = new ArrayList<String>();
		if (parameters != null) {
			for (Map.Entry<String, Object> entry : parameters.entrySet()) {
				String keyname = entry.getKey();
				if (keyname.endsWith(Constants.UPNM_FILE_NAME.getValue())) {
					keyname = keyname.substring(0, keyname.length() - 8);
					uploadFileNames.add(keyname);
				}
			}
		}
		return uploadFileNames.toArray(new String[uploadFileNames.size()]);
	}
}
