package com.ai.frame.dubbo.common.tools.ftp;

import java.io.InputStream;

/**ftp适配器**/
public class FtpClientAdapter implements FtpClientI{
	private boolean isSftp = false;
	private FtpClientI ftpClient = null;
	/**
	 * ftp适配器
	 * @param ftpParams 连接ftp服务器相关参数
	 * @param isSftp    true：表示sftp,false:表示ftp
	 */
	public FtpClientAdapter(FtpEntry ftpParams,boolean isSftp){
		this.isSftp = isSftp;
		
		if(this.isSftp){
			ftpClient = new SFtpClient(ftpParams);
		}else{
			ftpClient = new FtpClient(ftpParams);
		}
	}
	public boolean getFtpConnection(){
		if(isSftp){
			return ((SFtpClient)ftpClient).getChannelConnect(true);
		}else{
			return true;
		}
	}
	/**连接ftp服务器*/
	public FtpClientAdapter(FtpEntry ftpParams){
		this(ftpParams,false);
	}
	public byte[] getRemoteFile(String remoteDir, String filenNme) {
		return ftpClient.getRemoteFile(remoteDir, filenNme);
	}

	public boolean login() {
		return ftpClient.login();
	}

	public void logout() {
		ftpClient.logout();		
	}

	public boolean mkdirs(String fullDir) {
		return ftpClient.mkdirs(fullDir);
	}

	public boolean mksingleDir(String dir) {
		return ftpClient.mksingleDir(dir);
	}

	public boolean rmFile(String basePath, String fileName) {
		return ftpClient.rmFile(basePath, fileName);
	}

	public boolean rmFile(String fileFullPath) {
		return ftpClient.rmFile(fileFullPath);
	}

	public boolean uploadFile(String remoteDir, String remoteFileName,
			InputStream fileIn) {
		return ftpClient.uploadFile(remoteDir, remoteFileName, fileIn);
	}

	public boolean uploadText(String remoteDir, String remoteFileName,
			String context) {
		return ftpClient.uploadText(remoteDir, remoteFileName, context);
	}

}
