package com.ai.frame.dubbo.dconfig.control.xml;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultElement;

public class DomHelper {
	private Document doc;

	public DomHelper(Document doc) {
		this.doc = doc;
	}

	public DomHelper(SAXReader reader, InputStream in) throws DocumentException {
		Document doc = reader.read(in);
		this.doc = doc;
	}

	public Node getSingleNode(String xpath) {
		return doc.selectSingleNode(xpath);
	}

	@SuppressWarnings("unchecked")
	public List<Node> getNodeList(String xpath) {
		return doc.selectNodes(xpath);
	}

	public String getNodeAttribute(Node node, String attributeName) {
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			DefaultElement element = (DefaultElement) node;
			String attributeVal = element.attributeValue(attributeName);
			return attributeVal;
		}
		return null;
	}
	
	public <T> List<T> nodeList2ControlBeans(List<Node> nodeList, Class<T> cls) {
        List<T> beans = new LinkedList<T>();
        if (nodeList != null) {
            for (Node node : nodeList) {
                try {
                    T t = cls.newInstance();
                    setNodeVal2Object(node, t);

                    beans.add(t);
                } catch (Exception e) {
                }

            }
        }
        return beans;
    }
	public <T>void setNodeVal2Object(Node node,T t){
        Field[] fields = t.getClass().getDeclaredFields();
        for(Field f:fields){
            f.setAccessible(true);
            String fieldName = f.getName();
            String fieldVal  = getNodeAttribute(node, fieldName);
            Object filedValue= null;
            Class<?> type = f.getType();
            if(type == String.class){
            	filedValue = fieldVal;
            }else if(type == int.class || type == Integer.class){
            	filedValue = str2Int(fieldVal);
            }else if(type == boolean.class || type == Boolean.class){
            	filedValue = obj2Boolean(fieldVal);
            }
            
            if(filedValue!=null){
            	try{
                    f.set(t, fieldVal);
                }catch(Exception e){}
            }
        }
    }
	public int str2Int(String str){
        return str2Int(str,0);
    } 
	public int str2Int(String str,int defaultVal){
        try{
            return isEmpty(str) ? defaultVal : Integer.parseInt(str);
        }catch(Exception e){
            return defaultVal;
        }
    }
	public boolean isEmpty(String str) {
        return str == null || str.length() < 1 ? true : false;
    }
	public boolean obj2Boolean(Object obj) {
        String str = obj2TrimStr(obj);
        return str2Boolean(str);
    }
	public String obj2TrimStr(Object obj) {
        return trim(obj2Str(obj));
    }
	public String trim(String str) {
        return str == null ? "" : str.trim();
    }
	public String obj2Str(Object obj) {
        return obj == null ? null : obj.toString();
    }
	public boolean str2Boolean(String str){
        try{
            return isEmpty(str) ? false : Boolean.valueOf(str);
        }catch(Exception e){
            return false;
        }
    }
}
