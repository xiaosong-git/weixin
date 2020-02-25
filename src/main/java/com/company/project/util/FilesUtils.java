package com.company.project.util;

import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;

public class FilesUtils {
	/**
	 * 生成Byte流 TODO
	 * 
	 * @history
	 * @knownBugs
	 * @param
	 * @return
	 * @exception
	 */
	public static byte[] getBytesFromFile(File file) {
		byte[] ret = null;
		try {
			if (file == null) {
				// log.error("helper:the file is null!");
				return null;
			}
			FileInputStream in = new FileInputStream(file);
			ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
			byte[] b = new byte[4096];
			int n;
			while ((n = in.read(b)) != -1) {
				out.write(b, 0, n);
			}
			in.close();
			out.close();
			ret = out.toByteArray();
		} catch (IOException e) {
			// log.error("helper:get bytes from file process error!");
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * 把流生成图片 TODO
	 * 
	 * @history
	 * @knownBugs
	 * @param
	 * @return
	 * @exception
	 */
	public static File getFileFromBytes(byte[] files, String outputFile,String fileName) {
		File ret = null;
		
		BufferedOutputStream stream = null;
		try {
		if (StringUtils.isBlank(fileName)) {
			ret = new File(outputFile);
		}else {
			ret = new File(outputFile+fileName);
		}
			
			
			File fileParent = ret.getParentFile();  
			if(!fileParent.exists()){  
			    fileParent.mkdirs();  
			}  
			ret.createNewFile(); 
			
			FileOutputStream fstream = new FileOutputStream(ret);
			
			stream = new BufferedOutputStream(fstream);
			
			stream.write(files);
			
			
		} catch (Exception e) {
		
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					// log.error("helper:get file from byte process error!");
					e.printStackTrace();
				}
			}
		}
		return ret;
	}

	/***
	 * 根据路径获取
	 * 
	 * @param path
	 * @return
	 */
	public static byte[] getPhoto(String path) {
		byte[] data = null;
		FileImageInputStream input = null;
		try {
			input = new FileImageInputStream(new File(path));
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int numBytesRead = 0;
			while ((numBytesRead = input.read(buf)) != -1) {
				output.write(buf, 0, numBytesRead);
			}
			data = output.toByteArray();
			output.close();
			input.close();
		} catch (FileNotFoundException ex1) {
			ex1.printStackTrace();
		} catch (IOException ex1) {
			ex1.printStackTrace();
		}
		return data;
	}

	/**
	 * 获取网络地址图片
	 * @param strUrl
	 * @return
	 */
	public static byte[] getImageFromNetByUrl(String strUrl) {
	    try {
	        URL url = new URL(strUrl);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");
	        conn.setConnectTimeout(5 * 1000);
	        InputStream inStream = conn.getInputStream();// 通过输入流获取图片数据
	        byte[] btImg = readInputStream(inStream);// 得到图片的二进制数据
	        return btImg;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	}

	   /**
	 * 从输入流中获取数据
	 *
	 * @param inStream
	 *            输入流
	 * @return
	 * @throws Exception
	 */
	public static byte[] readInputStream(InputStream inStream) throws Exception {
	    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
	    byte[] buffer = new byte[10240];
	    int len = 0;
	    while ((len = inStream.read(buffer)) != -1) {
	        outStream.write(buffer, 0, len);
	    }
	    inStream.close();
	    return outStream.toByteArray();
	}

	/**
	 * 文件下发
	 * update by cwf  2019/8/26 11:34
	 */
	public static void sendFile(String path, String filename, HttpServletResponse response) throws Exception {
		//否则直接使用response.setHeader("content-disposition", "attachment;filename=" + filename);即可
		response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(path); //获取文件的流
			int len = 0;
			//缓存作用
			byte buf[] = new byte[1024];
			//输出流
			out = response.getOutputStream();
			while ((len = in.read(buf)) > 0) {
				//向客户端输出，实际是把数据存放在response中，然后web服务器再去response中读取
				out.write(buf, 0, len);
			}
		}catch (Exception e){
			e.printStackTrace();
			return;
		}finally {
			if(in!=null){
				in.close();
			}
			if (out != null) {
				out.close();
				out.flush();
			}
		}
	}
	/**
	 * 将图片压缩
	 * @param srcImgData
	 * @param maxSize  10240L （10KB）
	 * @return
	 * @throws Exception
	 */

	public static byte[] compressUnderSize(byte[] srcImgData, long maxSize)
			throws Exception {
		double scale = 0.9;
		byte[] imgData = Arrays.copyOf(srcImgData, srcImgData.length);

		if (imgData.length > maxSize) {
			do {
				try {
					imgData = compress(imgData, scale);

				} catch (IOException e) {
					throw new IllegalStateException("压缩图片过程中出错，请及时联系管理员！", e);
				}

			} while (imgData.length > maxSize);
		}

		return imgData;
	}
	public static byte[] compress(byte[] srcImgData, double scale)
			throws IOException {
		BufferedImage bi = ImageIO.read(new ByteArrayInputStream(srcImgData));
		int width = (int) (bi.getWidth() * scale); // 源图宽度
		int height = (int) (bi.getHeight() * scale); // 源图高度

		Image image = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		BufferedImage tag = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		Graphics g = tag.getGraphics();
		g.setColor(Color.RED);
		g.drawImage(image, 0, 0, null); // 绘制处理后的图
		g.dispose();

		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		ImageIO.write(tag, "JPEG", bOut);

		return bOut.toByteArray();
	}
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws Exception {
	byte []aBytes=	getPhoto("D:\\test\\tempotos\\CRN48_GRBk0o785wDIVTtuGba8cyRHBmCfqgEMaF76IVIgSos59TTQ4K8btxlwr75503246316947700906.jpg");
		try {
			String a="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";

			String b= Base64.encode(FilesUtils.compressUnderSize(aBytes, 10240L));
			getFileFromBytes(FilesUtils.compressUnderSize(aBytes, 10240L),"D:\\test\\tempotos\\","test.jpg");
			System.out.println(b);
			if (a.equals(b)) {
				System.out.println("===");
			}else {
				System.out.println("!=");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}


	}
}
