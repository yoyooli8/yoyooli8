package com.ai.frame.dubbo.common.tools.ftp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.ai.frame.dubbo.common.log.Logger;
import com.ai.frame.dubbo.common.log.LoggerFactory;
import com.ai.frame.dubbo.common.util.IOUtil;
import com.ai.frame.dubbo.common.util.StringUtil;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SFtpClient implements FtpClientI {
	private Logger log = LoggerFactory.getUtilLog(SFtpClient.class);
	private FtpEntry ftpParams;
	private Session session = null;
	private ThreadLocal<ChannelSftp> stpChannel = new ThreadLocal<ChannelSftp>();
	//设置登陆超时时间
	private int loginTimeOut = 30000;
	public SFtpClient(FtpEntry ftpParams){
		this.ftpParams = ftpParams;
	}
	/**
	 * 建立SFTP通道的连接
	 * @param isNew   是否要在不存在ChannelSftp的情况下重新打开ChannelSftp
	 * @return        TRUE|FALSE
	 */
	public boolean getChannelConnect(boolean isNew){
		ChannelSftp chnnelSftp = stpChannel.get();
		if(chnnelSftp == null && isNew){
			try {
				chnnelSftp = (ChannelSftp)session.openChannel("sftp");
				
				stpChannel.set(chnnelSftp);
			} catch (Exception e) {
			}
		}
		//建立SFTP通道的连接
		try {
			if(chnnelSftp!=null && !chnnelSftp.isConnected()){
				chnnelSftp.connect(1000);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * 取得远程FTP服务器文件,执行之前需要先建立连接(getChannelConnect)
	 */
	public byte[] getRemoteFile(String remoteDir, String fileName) {
		ChannelSftp chnnelSftp = stpChannel.get();
		String tmpDir  = System.getProperty("user.home");
		String tmpFDir = tmpDir + "/" + fileName;
		File tmpFile   = new File(tmpFDir);
		FileOutputStream fileOut = null;
		BufferedOutputStream bos = null;
		try{
			fileOut = new FileOutputStream(tmpFile);
			bos = new BufferedOutputStream(fileOut,ftpParams.getReadbuf());
			chnnelSftp.get(remoteDir+"/"+fileName, bos);
			
			bos.flush();
			
			return IOUtil.file2bytes(tmpFile,ftpParams.getReadbuf());
		}catch(Exception e){
			log.error("getRemoteFile", "{} called error:{}", e);
		}finally{
			IOUtil.closeOutputStream(bos);
			IOUtil.closeOutputStream(fileOut);
			
			disconnect();
		}
		return null;
	}

	public boolean login() {
		JSch jsch = new JSch();
		try {
			//根据用户名，主机ip，端口获取一个Session对象
			session = jsch.getSession(ftpParams.getUname(), ftpParams.getHost(), ftpParams.getPort());
			session.setPassword(ftpParams.getUpwd());
			//设置第一次登陆的时候提示，可选值：(ask | yes | no)
			session.setConfig("StrictHostKeyChecking", "no");
			session.setTimeout(loginTimeOut);
			//通过Session建立链接
			session.connect();
			return true;
		} catch (Exception e) {
			log.error("login", "{} called error:{}", e);
			logout();
		}
		return false;
	}

	public void logout() {
		disconnect();
		
		if(session!=null){
			session.disconnect();
			session = null;
		}
	}
	/**断开连接*/
	private void disconnect(){
		ChannelSftp chnnelSftp = stpChannel.get();
		if(chnnelSftp!=null){
			chnnelSftp.disconnect();
			
			stpChannel.remove();
		}
	}
	
	public boolean mkdirs(String fullDir) {
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

	public boolean mksingleDir(String dir) {
		try {
			ChannelSftp chnnelSftp = stpChannel.get();
			try {
				chnnelSftp.cd(dir);
				
				return true;
			} catch (Exception e) {
				chnnelSftp.mkdir(dir);
				
				return true;
			}
		} catch (Exception e) {
			log.error("mksingleDir", "{} called error:{}", e);
		}
		
		return false;
	}

	public boolean rmFile(String basePath, String fileName) {
		ChannelSftp chnnelSftp = stpChannel.get();
		try {
			chnnelSftp.cd(basePath);
			
			chnnelSftp.rm(fileName);
			return true;
		} catch (SftpException e) {
			log.error("rmFile", "{} called error:{}", e);
		}
		
		return false;
	}

	public boolean rmFile(String fileFullPath) {
		ChannelSftp chnnelSftp = stpChannel.get();
		
		try {
			chnnelSftp.rm(fileFullPath);
			return true;
		} catch (SftpException e) {
			log.error("rmFile", "{} called error:{}", e);
		}
		
		return false;
	}

	public boolean uploadFile(String remoteDir, String remoteFileName,InputStream fileIn) {
		if(!StringUtil.isEmpty(remoteDir)||!StringUtil.isEmpty(remoteFileName)){
			if(fileIn!=null){
				try {
					ChannelSftp chnnelSftp = stpChannel.get();
					String dst = remoteDir + "/" + remoteFileName;
					
					chnnelSftp.put(fileIn, dst);
					
					return true;
				} catch (SftpException e) {
					log.error("uploadFile", "{} called error:{}", e);
				}finally{
					IOUtil.closeInputStream(fileIn);
					disconnect();
				}
			}
		}
		
		return false;
	}

	public boolean uploadText(String remoteDir, String remoteFileName,String context) {
		if(!StringUtil.isEmpty(remoteDir)||!StringUtil.isEmpty(remoteFileName)){
			context = StringUtil.trim(context);
			OutputStream fileOut = null;
			OutputStreamWriter  writer = null;
			try{
				boolean rtn = mkdirs(remoteDir);
				if(rtn){
					ChannelSftp chnnelSftp = stpChannel.get();
					chnnelSftp.cd(remoteDir);
					
					fileOut = chnnelSftp.put(remoteFileName);
					writer  = new OutputStreamWriter(fileOut,StringUtil.trim(ftpParams.getEncoding(),"UTF-8"));
					writer.write(context);
					
					writer.flush();
					return true;
				}
			}catch(Exception e){
				log.error("uploadText", "{} called error:{}", e);
			}finally{
				IOUtil.closeOutputStream(fileOut);
				disconnect();
			}
		}
		return false;
	}
	public void setLoginTimeOut(int loginTimeOut) {
		this.loginTimeOut = loginTimeOut;
	}

}
