package com.ai.frame.dubbo.common.tools.ftp;

import java.io.InputStream;

public interface FtpClientI {

	/**
	 * 取得FTP服务器上的文件
	 * @param remoteDir
	 * @param filenNme
	 * @return
	 */
	public abstract byte[] getRemoteFile(String remoteDir, String filenNme);

	/**
	 * 上传文本信息
	 * @param remoteDir
	 * @param remoteFileName
	 * @param context
	 * @return
	 */
	public abstract boolean uploadText(String remoteDir, String remoteFileName,
			String context);

	/**
	 * 上传文件
	 * @param remoteDir
	 * @param remoteFileName
	 * @param fileIn
	 * @return
	 */
	public abstract boolean uploadFile(String remoteDir, String remoteFileName,
			InputStream fileIn);

	/**
	 * 删除文件
	 * @param basePath 文件目录
	 * @param fileName 文件名称
	 * @return
	 */
	public abstract boolean rmFile(String basePath, String fileName);

	/**
	 * 删除文件
	 * @param fileFullPath
	 * @return
	 */
	public abstract boolean rmFile(String fileFullPath);

	/**
	 * 创建目录,包含创建子目录
	 * @param fullDir
	 * @return
	 */
	public abstract boolean mkdirs(String fullDir);

	/**
	 * 创建单级目录
	 * @param dir
	 * @return
	 */
	public abstract boolean mksingleDir(String dir);

	/**FTP登录**/
	public abstract boolean login();

	/**登出*/
	public abstract void logout();

}