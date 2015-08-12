package com.ai.frame.dubbo.common.tools.ftp;

public class FtpEntry {
	private String host;
	private String uname;
	private String upwd;
	private int port;
	private String encoding;
	private int sendbuf = 1024000; // 1kb
	private int readbuf = 1024000; // 1kb
	public FtpEntry(){}
	public FtpEntry(String host,String uname,String upwd,int port){
		this.host  = host;
		this.uname = uname;
		this.upwd  = upwd;
		this.port  = port;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getUpwd() {
		return upwd;
	}
	public void setUpwd(String upwd) {
		this.upwd = upwd;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public int getSendbuf() {
		return sendbuf;
	}
	public void setSendbuf(int sendbuf) {
		this.sendbuf = sendbuf;
	}
	public int getReadbuf() {
		return readbuf;
	}
	public void setReadbuf(int readbuf) {
		this.readbuf = readbuf;
	}
	public String getEncoding() {
		return encoding;
	}
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	
}
