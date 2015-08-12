package com.ai.frame.dubbo.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.ai.frame.dubbo.common.encryption.EncryptionUtil;
import com.ai.frame.dubbo.common.log.Logger;
import com.ai.frame.dubbo.common.log.LoggerFactory;

/**IO流相关工具**/
public class IOUtil {
	private static Logger log = LoggerFactory.getUtilLog(IOUtil.class);
	public static final int BUFFER = 1024;
	private final static String FILESTECODE = "ISO-8859-1";
	/**
	 * str字符串转换为输入流,主要用来处理图片类转换,excel或是work类型文档不能用此方法转换
	 * @param str
	 * @return
	 */
	public static InputStream str2InputStream(String str){
		try {
            ByteArrayInputStream is = new ByteArrayInputStream(str.getBytes(FILESTECODE));
            return is;
        } catch (Exception e) {
        	log.error("{} called error:{}.", e,"str2InputStream");
           return null;
        }
	}
	/**
	 * 输入流转换为str字符串,主要用来处理图片类转换,excel或是work类型文档不能用此方法转换
	 * @param in
	 * @return
	 */
	public static String inputStream2String(InputStream in){
        if(in == null )return null;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream(); 
        byte[] data = new byte[1024];
        String rtn = null;
        int count = -1;
        try {
            while ((count = in.read(data)) != -1) {
                outStream.write(data, 0, count);
            }
            rtn = new String(outStream.toByteArray(),FILESTECODE);
            
            outStream.flush();
        } catch (Exception e) {
        }
        try {
            data = null;
            if(in!=null)in.close();
            if(outStream!=null)outStream.close();
        } catch (Exception e) {
        	log.error("{} called error:{}.", e,"inputStream2String");
        }
        return rtn;
    }
	
	/**
	 * int == > bytes
	 * @param v
	 * @return
	 */
	public static byte[] intToBytes(int v){
        byte[] b = new byte[4];
        b[0] = (byte) ((v >>> 24));
        b[1] = (byte) ((v >>> 16));
        b[2] = (byte) ((v >>> 8));
        b[3] = (byte) ((v >>> 0));
        
        return b;
    }
	/**
	 * byte == > int
	 * @param intBytes
	 * @return
	 */
	public static int byteToInt(byte[] intBytes){
		if(intBytes.length != 4){
			return 0;
		}else{
			int rtn  = ((intBytes[0] & 0x000000ff)<<24);
			    rtn |= ((intBytes[1] & 0x000000ff) <<16);
			    rtn |= ((intBytes[2] & 0x000000ff) <<8);
			    rtn |=  (intBytes[3] & 0x000000ff);
			return rtn;
		}
	}
	
	/**
	 * 把一个inputStream流进行md5加密
	 * @param in       inputStream流
	 * @param readBuf  读取流的缓冲区大小
	 * @return 返回加密后的字符串
	 */
	public static String md5File(InputStream in,int readBuf){
		if(in == null)return null;
		BufferedInputStream bufin = null;
		try {
			byte[] bytes = new byte[readBuf];
			bufin = new BufferedInputStream(in);
			MessageDigest md = MessageDigest.getInstance("MD5");
			int read = -1;
			while((read = bufin.read(bytes))!= -1){
				md.update(bytes, 0, read);
			}
			
			byte[] updateBytes = md.digest();
			
			return EncryptionUtil.encodeStr2Hex(updateBytes);
		} catch (Exception e) {
			log.error("{} called error:{}.", e,"md5File");
		}finally{
			closeInputStream(bufin);
		}
		return null;
	}
	/**
	 * 把file对象转成inputStream流
	 * @param file
	 * @return
	 */
	public static InputStream file2InputStream(File file){
		if(file == null)return null;
		try {
            InputStream in = new FileInputStream(file);
            return in;
        } catch (Exception e) {
        	log.error("{} called error:{}.", e,"file2InputStream");
        } 
        return null;
	}
	/**
	 * 输入流转换为文件存储在本地
	 * @param in
	 * @param Filepath
	 */
	public static void inputStream2File(InputStream in,String filepath){
        File file = new File(filepath);
        byte buf[] = new byte[1024];
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try{
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos,1024);
            int count = -1;
            while((count = in.read(buf)) != -1){
                bos.write(buf,0,count);
            }
            bos.flush();
        }catch(Exception e){
        	log.error("{} called error:{}.", e,"inputStream2File");
        }finally{
        	closeInputStream(in);
        	closeOutputStream(bos);
			closeOutputStream(fos);
        }
        setFileWritable(file,true,false);
    }
	/**
	 * 设置文件的可写属性
	 * @param file
	 * @param writable
	 * @param ownerOnly
	 */
	@SuppressWarnings("rawtypes")
	public static void setFileWritable(File file,boolean writable, boolean ownerOnly){
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            security.checkWrite(file.getPath());
        }
        Object fileObj  = ClassUtil.getFieldVal(file, "fs");
        
        Class[]parameterTypes = new Class[]{File.class,int.class,boolean.class,boolean.class};
        Object[] params = new Object[]{file,0x02,writable,ownerOnly};
        ClassUtil.invokMethod(fileObj, "setPermission", parameterTypes, params);
    }
	
	/***
	 * 把bytes字节数组转成文件
	 * @param fbytes    字节数组
	 * @param fileName  要生成的文件路径名
	 * @param writerBuf 写文件的缓冲区大小
	 * @return
	 */
	public static File bytes2File(byte[] fbytes,String fileName,int writerBuf){
		if(fbytes == null)return null;
		File file = new File(fileName);
		
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		ByteArrayInputStream bin = null;
		try{
			byte[] buf = new byte[writerBuf];
			bin = new ByteArrayInputStream(fbytes);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos,writerBuf);
			
			int read = -1;
			while((read = bin.read(buf))!= -1){
				bos.write(buf,0,read);
			}
			bos.flush();
			
			return file;
		}catch(Exception e){
			log.error("{} called error:{}.", e,"bytes2File");
		}finally{
			closeOutputStream(bos);
			closeOutputStream(fos);
			closeInputStream(bin);
		}
		return null;
	}
	/***
	 * 文件转成字节数组
	 * @param file
	 * @param readBuf
	 * @return
	 */
	public static byte[] file2bytes(File file,int readBuf){
		InputStream fileIn = file2InputStream(file);
		
		return inputStream2Bytes(fileIn,readBuf);
	}
	
	/**
	 * bytes转换为inputStream流
	 * @param bytes
	 * @return
	 */
	public static InputStream byte2InputStream(byte[] bytes){
		try {
            ByteArrayInputStream is = new ByteArrayInputStream(bytes);
            return is;
        } catch (Exception e) {
        	log.error("{} called error:{}.", e,"byte2InputStream");
           return null;
        }
	}
	/**
	 * InputStream流转成字节数组
	 * @param in
	 * @param buf
	 * @return
	 */
	public static byte[] inputStream2Bytes(InputStream in,int buf){
		ByteArrayOutputStream bout = null;
		BufferedOutputStream  bof  = null;
		try{
			bout = new ByteArrayOutputStream();
			bof  = new BufferedOutputStream(bout,buf);
			byte[] wbuf = new byte[buf];
			int read = -1;
			while((read = in.read(wbuf))!= -1){
				bof.write(wbuf,0,read);
			}
			bof.flush();
			
			return bout.toByteArray();
		}catch(Exception e){
			log.error("{} called error:{}.", e,"inputStream2Bytes");
		}finally{
			closeOutputStream(bof);
			closeOutputStream(bout);
		}
		return null;
	}
	
	/**
	 * gzip压缩字节数组
	 * @param data
	 * @return
	 */
	public static byte[] compressByteWithGzip(byte[] data){
		ByteArrayInputStream bais = null;
		ByteArrayOutputStream baos = null;
		try {
			bais = new ByteArrayInputStream(data);
			baos = new ByteArrayOutputStream();
			
			gzipCompress(bais, baos,BUFFER);
			
			byte[] output = baos.toByteArray();
			
			baos.flush();
			
			return output;
		}catch (Exception e) {
			log.error("{} called error:{}.", e,"compressByteWithGzip");
		} finally {
			closeOutputStream(baos);
			closeInputStream(bais);
		}
		return null;
	}
	
	/**
	 * 把数据流is通过gizp压缩写入outputStream流中
	 * @param is
	 * @param os
	 */
	private static void gzipCompress(InputStream is, OutputStream os,int buf){
		GZIPOutputStream gos = null;
		try {
			gos = new GZIPOutputStream(os);
			int count;
			byte data[] = new byte[buf];
			while ((count = is.read(data, 0, buf)) != -1) {
				gos.write(data, 0, count);
			}
			
			gos.finish();
			gos.flush();
		} catch (Exception e) {
			log.error("{} called error:{}.", e,"gzipCompress");
		} finally {
			closeOutputStream(gos);
		}
	}
	
	/**
	 * gzip解压字节数组
	 * @param data
	 * @return
	 */
	public static byte[] unCompressByteWithGzip(byte[] data){
		ByteArrayInputStream bais = null;
		ByteArrayOutputStream baos = null;
		try {
			bais = new ByteArrayInputStream(data);
			baos = new ByteArrayOutputStream();
			
			gzipUncompress(bais, baos,BUFFER);
			
			data = baos.toByteArray();
			
			baos.flush();
		}catch (Exception e) {
			log.error("{} called error:{}.", e,"unCompressByteWithGzip");
		} finally {
			closeOutputStream(baos);
			closeInputStream(bais);
		}
		
		return data;
	}
	/**
	 * 把输入流通过ungzip解压后写入输出流中
	 * @param is
	 * @param os
	 * @param buf
	 */
	private  static void gzipUncompress(InputStream is, OutputStream os,int buf){
		GZIPInputStream gis = null;
		try{
			gis = new GZIPInputStream(is);
			
			int count;
			byte data[] = new byte[buf];
			while ((count = gis.read(data, 0, buf)) != -1) {
				os.write(data, 0, count);
			}
			
		}catch(Exception e){
			log.error("{} called error:{}.", e,"gzipUncompress");
		}finally{
			closeInputStream(gis);
		}
	}
	/**
	 * 关闭输出流
	 * @param out
	 */
	public static void closeOutputStream(OutputStream out){
		if(out!=null){
            try {
                out.close();
                out = null;
            } catch (Exception e) {
            }
        }
	}
	/**
	 * 关闭输入流
	 * @param in
	 */
	public static void closeInputStream(InputStream in){
		if(in!=null){
            try {
                in.close();
                in = null;
            } catch (Exception e) {
            }
        }
	}
}
