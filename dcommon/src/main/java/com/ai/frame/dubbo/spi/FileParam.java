package com.ai.frame.dubbo.spi;

import java.io.Serializable;

/**
 * 文件参数
 * @since 20150701
 */
@SuppressWarnings("serial")
public class FileParam implements Serializable{
	private String fileName;
	private String fieldname;
	private String fileType;
//	private InputStream fileIn;
	private byte[] fileIn;
	public FileParam(){}
	public FileParam(String fieldname,String fileName,String fileType,byte[] fileIn){
		this.fieldname = fieldname;
		this.fileName  = fileName;
		this.fileType  = fileType;
		this.fileIn    = fileIn;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFieldname() {
		return fieldname;
	}
	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public byte[] getFileIn() {
		return fileIn;
	}
	public void setFileIn(byte[] fileIn) {
		this.fileIn = fileIn;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null)return false;
		if(obj instanceof FileParam){
			FileParam oth = (FileParam)obj;
			return oth.getFieldname().equals(this.getFieldname());
		}
		return false;
	}
	
}
