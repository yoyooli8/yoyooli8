package com.ai.frame.dubbo.dconfig.control.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.ai.frame.dubbo.common.exception.AinbException;
import com.ai.frame.dubbo.common.log.Logger;
import com.ai.frame.dubbo.common.log.LoggerFactory;
import com.ai.frame.dubbo.common.util.Constants;
import com.ai.frame.dubbo.dconfig.control.bean.Action;
import com.ai.frame.dubbo.dconfig.control.bean.Control;
import com.ai.frame.dubbo.dconfig.control.bean.Input;
import com.ai.frame.dubbo.dconfig.control.bean.Output;
import com.ai.frame.dubbo.dconfig.control.bean.Parameter;
import com.ai.frame.dubbo.dconfig.control.bean.Result;

public class ControlFactory {
    private static final String PROXY_FILE_NAME = "config/control.xml";
    private static final String INCLUDE_FILE_NAME    = "file";
    private static final String DEFAULT_DTDFILE_NAME = "config/common-1.9.dtd";
    private static final String SUB_DTDFILE_NAME     = "config/common-sub-1.9.dtd";
    private static Logger log = LoggerFactory.getUtilLog(ClassPathResource.class);
    
    private static Control control;
    public static Control getControl(){
        return getControl(null,null);
    }
    public static Control getControl(String controlFile){
        return getControl(controlFile,null);
    }
    
    public static InputStream getInputStream(String path){
    	Resource resource = new ClassPathResource(path);
        try {
			InputStream fileIn = resource.getInputStream();
			return fileIn;
		} catch (Exception e) {
			log.error("read config file[{}] error:{}.", e, path);
		}
        return null;
    }
    public synchronized static Control getControl(String controlFile,String controlDtdFile) {
        if(control == null){
            String path = PROXY_FILE_NAME;
            if (controlFile != null && controlFile.length() > 0) {
                path = controlFile;
            }
//            System.out.println("Control Path is:" + path);
            log.info(path, "Control path is :{}");
            
            InputStream fileIn = getInputStream(path);
            if(fileIn!=null){
                control = parse2Control(fileIn);
            }else{
//                System.err.println("read Control file["+path+"]to java inputStream error.");
                log.error(path, "read Control file[{}]to java inputStream error.");
            }
        }
        return control;
    }
    
    private static Control parse2Control(InputStream controlStream){
        SAXReader reader = new SAXReader();
        setValidation(reader,DEFAULT_DTDFILE_NAME);
        
        try {
            DomHelper helper = new DomHelper(reader,controlStream);
            List<Action> actionbeans = parse2Actions(helper);
            
            List<Action> allActions = new ArrayList<Action>();
            if(actionbeans!=null)allActions.addAll(actionbeans);
            
            //处理包含文件
            List<Node> actions = helper.getNodeList("/control/include");
            if(actions !=null && actions.size()>0){
                for(Node node:actions){
                    String subfilepath = helper.getNodeAttribute(node, INCLUDE_FILE_NAME);
                    Resource subresource = new ClassPathResource(subfilepath);
                    InputStream fileIn = subresource.getInputStream();
                    
                    SAXReader subreader = new SAXReader();
                    setValidation(subreader,SUB_DTDFILE_NAME);
                    
                    DomHelper subhelper = new DomHelper(subreader,fileIn);
                    
                    List<Action> subactionbeans = parse2Actions(subhelper);
                    if(subactionbeans!=null)mergeActions(allActions,subactionbeans);
                    
                    fileIn.close();
                }
            }
            
            Control control = new Control(allActions);
            
            return control;
        }catch (Exception e) {
            throw new AinbException(Constants.ERR_FRAME_NAME.getValue(),"parse Control file to Control Object error.",e);
        }finally{
            try {
                controlStream.close();
            } catch (IOException e) {
            }
        }
    }
    
    private static List<Action> parse2Actions(DomHelper helper){
        List<Node> actions = helper.getNodeList("/control/action");
        List<Action> actionbeans = nodeList2ControlBeans(helper,actions,Action.class);
        
        for(Action action:actionbeans){
            String path = action.getPath();
            if (path != null && path.length() > 0) {
                String outputxpath = "/control/action[@path='"+action.getPath()+"']" + "/output";
                List<Node> outputs = helper.getNodeList(outputxpath);
                List<Output> outputbeans = nodeList2ControlBeans(helper,outputs,Output.class);
                setParams2Output(outputbeans,helper,"/control/action[@path='"+action.getPath()+"']");
                action.setOutputs(outputbeans);
                
                String resultxpath = "/control/action[@path='"+action.getPath()+"']" + "/result";
                List<Node> results = helper.getNodeList(resultxpath);
                List<Result> resultbeans = nodeList2ControlBeans(helper,results,Result.class);
                action.setResults(resultbeans);
                
                String inputxpath = "/control/action[@path='"+action.getPath()+"']" + "/input";
                List<Node> inputs  = helper.getNodeList(inputxpath);
                List<Input> inputbeans = nodeList2ControlBeans(helper,inputs,Input.class);
                setParams2Input(inputbeans,helper,"/control/action[@path='"+action.getPath()+"']");
                action.setInputs(inputbeans);
            }
        }
        return actionbeans;
    }
    
    private static void setParams2Output(List<Output> outputbeans,DomHelper helper,String preXpath){
        for(Output output:outputbeans){
            String uid = output.getUid();
            if (uid != null && uid.length() > 0) {
                String paramXpath = preXpath + "/output[@uid='"+uid+"']/parameter";
                List<Node> parameters = helper.getNodeList(paramXpath);
                List<Parameter> params = nodeList2ControlBeans(helper,parameters,Parameter.class);
                output.setParameters(params);
            }
        }
    }
    private static void setParams2Input(List<Input> inputbeans,DomHelper helper,String preXpath){
        for(Input input:inputbeans){
            String uid = input.getUid();
            if (uid != null && uid.length() > 0) {
                String paramXpath = preXpath + "/input[@uid='"+uid+"']/parameter";
                List<Node> parameters = helper.getNodeList(paramXpath);
                List<Parameter> params = nodeList2ControlBeans(helper,parameters,Parameter.class);
                input.setParameters(params);
            }
        }
    }
    private static <T> List<T> nodeList2ControlBeans(DomHelper helper, List<Node> nodeList, Class<T> cls) {
        return helper.nodeList2ControlBeans(nodeList, cls);
    }
    
    private static void mergeActions(List<Action> allActions,List<Action> subactionbeans){
        for(Action subAction:subactionbeans){
            if(isExistsActon(allActions,subAction)){//存在相同的action
                Action oldAction = allActions.get(allActions.indexOf(subAction));
                
                mergeAction(oldAction,subAction);
            }else{
                allActions.add(subAction);
            }
        }
    }
    private static void mergeAction(Action oldAction,Action subAction){
        List<Input> subInputs  = subAction.getInputs();
        List<Output>subOutputs = subAction.getOutputs();
        List<Result> results   = subAction.getResults();
        
        if(subInputs!=null){
            for(Input subInput:subInputs){
                Input oldInput = oldAction.getInput(subInput.getUid());
                if(oldInput == null){
                    oldAction.getInputs().add(subInput);
                }
            }
        }
        
        if(subOutputs!=null){
            for(Output subOutput:subOutputs){
                Output oldOutput = oldAction.getOutput(subOutput.getUid());
                if(oldOutput == null){
                    oldAction.getOutputs().add(subOutput);
                }
            }
        }
        
        if(results!=null){
            for(Result subresult:results){
                Result oldResult = oldAction.getResult(subresult.getKey());
                if(oldResult == null){
                    oldAction.getResults().add(subresult);
                }
            }
        }
    }
    
    private static boolean isExistsActon(List<Action> allActions,Action subAction){
        int index = allActions.indexOf(subAction);
        return index > -1 ? true:false;
    }
    
    private static void setValidation(SAXReader reader,final String dtdpath){
        reader.setValidation(true);
        reader.setEncoding("UTF-8");
        
        // 添加验证的dtd文件
        reader.setEntityResolver(new EntityResolver(){
            public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                String xmldtd = new String(dtdpath);
                Resource resource = new ClassPathResource(xmldtd);

                InputStream byteStream = resource.getInputStream();
                if (byteStream != null) {
                    InputSource ins = new InputSource();
                    ins.setByteStream(byteStream);
                    ins.setPublicId(publicId);
                    ins.setSystemId(systemId);
                    return ins;
                }
                throw new IOException("cant't find the file["+dtdpath+"].");

            }
        });
    }
}
