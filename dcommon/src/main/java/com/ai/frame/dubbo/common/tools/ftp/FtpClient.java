package com.ai.frame.dubbo.common.tools.ftp;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import com.ai.frame.dubbo.common.log.Logger;
import com.ai.frame.dubbo.common.log.LoggerFactory;
import com.ai.frame.dubbo.common.util.IOUtil;
import com.ai.frame.dubbo.common.util.StringUtil;

public class FtpClient implements FtpClientI {
	private Logger log = LoggerFactory.getUtilLog(FtpClient.class);
	private FTPClient ftpClient;
	private FtpEntry ftpParams;
	public FtpClient(FtpEntry ftpParams){
		this.ftpParams = ftpParams;
		this.ftpClient = new FTPClient();
	}
	
	/* (non-Javadoc)
	 * @see common.ai.ftp.tools.FtpClientI#getRemoteFile(java.lang.String, java.lang.String)
	 */
	public byte[] getRemoteFile(String remoteDir,String filenNme){
		String tmpDir  = System.getProperty("user.home");
		String tmpFDir = tmpDir + "/" + filenNme;
		File tmpFile   = new File(tmpFDir);
		InputStream fileIn = null;
		try{
			tmpFile.deleteOnExit();
			
			boolean rtn = ftpClient.changeWorkingDirectory(remoteDir);
			if(rtn){
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
				fileIn = ftpClient.retrieveFileStream(filenNme);
				
				return IOUtil.inputStream2Bytes(fileIn, this.ftpParams.getReadbuf());
			}
			
			return null;
		}catch(Exception e){
			log.error("getRemoteFile", "{} called error:{}", e);
		}finally{
			IOUtil.closeInputStream(fileIn);
			disconnect();
		}
		return null;
	}
	/* (non-Javadoc)
	 * @see common.ai.ftp.tools.FtpClientI#uploadText(java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean uploadText(String remoteDir,String remoteFileName,String context){
		if(!StringUtil.isEmpty(remoteDir)||!StringUtil.isEmpty(remoteFileName)){
			context = StringUtil.trim(context);
			OutputStream fileOut = null;
			try {
				boolean rtn = mkdirs(remoteDir);
				if(rtn){
					ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
					ftpClient.changeWorkingDirectory(remoteDir);
					
					fileOut = ftpClient.storeFileStream(remoteFileName);
					fileOut.write(context.getBytes());
					
					fileOut.flush();
					
					return true;
				}
				return false;
			}catch (Exception e) {
				log.error("uploadText", "{} called error:{}", e);
			}finally{
				IOUtil.closeOutputStream(fileOut);
				disconnect();
			}
		}
		return false;
	}
	/* (non-Javadoc)
	 * @see common.ai.ftp.tools.FtpClientI#uploadFile(java.lang.String, java.lang.String, java.io.InputStream)
	 */
	public boolean uploadFile(String remoteDir,String remoteFileName,InputStream fileIn){
		if(!StringUtil.isEmpty(remoteDir)||!StringUtil.isEmpty(remoteFileName)){
			OutputStream fileOut = null;
			try {
				boolean rtn = mkdirs(remoteDir);
				if(rtn){
					ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
					ftpClient.changeWorkingDirectory(remoteDir);
					
					byte[] buffer = new byte[this.ftpParams.getReadbuf()];
					fileOut = ftpClient.storeFileStream(remoteFileName);
					BufferedInputStream  bin = new BufferedInputStream(fileIn,this.ftpParams.getReadbuf());
					int read = -1;
					while((read = bin.read(buffer))!=-1){
						fileOut.write(buffer,0,read);
					}
					
					fileOut.flush();
					bin.close();
					
					return true;
				}
				return false;
			}catch (Exception e) {
				log.error("uploadFile", "{} called error:{}", e);
			}finally{
				IOUtil.closeOutputStream(fileOut);
				IOUtil.closeInputStream(fileIn);
				disconnect();
			}
		}
		return false;
	}
	/* (non-Javadoc)
	 * @see common.ai.ftp.tools.FtpClientI#rmFile(java.lang.String, java.lang.String)
	 */
	public boolean rmFile(String basePath,String fileName){
		try {
			boolean rtn = ftpClient.changeWorkingDirectory(basePath);
			if(rtn){
				return ftpClient.deleteFile(fileName);
			}
			return false;
		}catch (Exception e) {
			log.error("rmFile", "{} called error:{}", e);
		}
		return false;
	}
	/* (non-Javadoc)
	 * @see common.ai.ftp.tools.FtpClientI#rmFile(java.lang.String)
	 */
	public boolean rmFile(String fileFullPath){
		if(!StringUtil.isEmpty(fileFullPath)){
			String tmp = fileFullPath.replace("\\\\", "/");
			int index  = tmp.lastIndexOf("/");
			if(index > -1){
				String basePath = tmp.substring(0,index);
				String fileName = tmp.substring(index+1);
				
				return rmFile(basePath,fileName);
			}
			return false;
		}
		return false;
	}
	/* (non-Javadoc)
	 * @see common.ai.ftp.tools.FtpClientI#mkdirs(java.lang.String)
	 */
	public boolean mkdirs(String fullDir){
		if(!StringUtil.isEmpty(fullDir)){
			String tmp = fullDir.replace("\\\\", "/");
			String[] subDirs = tmp.split("/");
			boolean rtn = false;
			String parent = "/";
			for(String subDir:subDirs){
				if(!"/".equals(parent)){
					rtn = mksingleDir(parent+ "/" + subDir);
				}else{
					rtn = mksingleDir(parent + subDir);
				}
				
				if(!rtn){
					break;
				}
				if(!"/".equals(parent)){
					parent = parent + "/" + subDir;
				}else{
					parent = parent + subDir;
				}
				
			}
			return rtn;
		}
		return false;
	}
	/* (non-Javadoc)
	 * @see common.ai.ftp.tools.FtpClientI#mksingleDir(java.lang.String)
	 */
	public boolean mksingleDir(String dir){
		try {
			boolean rtn = ftpClient.changeWorkingDirectory(dir);
			if(!rtn){
				rtn = ftpClient.makeDirectory(dir);
			}
			return rtn;
		}catch (Exception e) {
			log.error("mksingleDir", "{} called error:{}", e);
		}
		return false;
	}
	/* (non-Javadoc)
	 * @see common.ai.ftp.tools.FtpClientI#login()
	 */
	public boolean login(){
		try {
			ftpClient.connect(ftpParams.getHost(), ftpParams.getPort());
			ftpClient.login(ftpParams.getUname(), ftpParams.getUpwd());
			
			int replycode = ftpClient.getReplyCode();
			
			if(!FTPReply.isPositiveCompletion(replycode)){
				disconnect();
				logout();
				return false;
			}
			
			ftpClient.setControlEncoding(StringUtil.trim(ftpParams.getEncoding(),"UTF-8"));
			ftpClient.setSendBufferSize(ftpParams.getSendbuf());
			return true;
		}catch (Exception e) {
			log.error("login", "{} called error:{}", e);
			disconnect();
			logout();
		}
		return false;
	}
	/* (non-Javadoc)
	 * @see common.ai.ftp.tools.FtpClientI#logout()
	 */
	public void logout(){
		try {
			if(ftpClient!=null){
				ftpClient.logout();
				
				disconnect();
			}
		}catch (Exception e1) {}
	}
	/**断开连接*/
	private void disconnect(){
		try {
			if(ftpClient!=null){
				ftpClient.disconnect();
			}
		}catch (Exception e1) {}
	}
}
