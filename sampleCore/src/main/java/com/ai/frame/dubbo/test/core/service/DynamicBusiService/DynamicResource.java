package com.ai.frame.dubbo.test.core.service.DynamicBusiService;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import org.springframework.core.io.Resource;

public class DynamicResource implements Resource{
	private DynamicBean dynamicBean;
	public DynamicResource(DynamicBean dynamicBean){  
        this.dynamicBean = dynamicBean;  
    } 
	public InputStream getInputStream() throws IOException {  
        return new ByteArrayInputStream(dynamicBean.getXml().getBytes("UTF-8"));  
    }
	@Override
	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isReadable() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isOpen() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public URL getURL() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public URI getURI() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public File getFile() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public long contentLength() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public long lastModified() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public Resource createRelative(String relativePath) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getFilename() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}
}
