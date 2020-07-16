package com.lch.o2o.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.lch.o2o.dto.ImageHolder;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;


public class ImageUtil {
	
	private static String ClassPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
	private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final Random r = new Random();
	private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);
	
	/*
	 * 将CommonsMultipartFile转换为File
	 */
	public static File transferCommonsMultipartFileToFile(CommonsMultipartFile cFile) {
		File newFile = new File(cFile.getOriginalFilename());
		try {
			cFile.transferTo(newFile);
		} catch (IllegalStateException e) {
			logger.error(e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		return newFile;
	}
	
	/*
	 * 处理缩略图，并返回新生成图片的相对值路径
	 */
	public static String generateThumbnail(ImageHolder thumbnail, String targetAddr) {
		String realFileName = getRandomFileName();
		String extension = getFileExtension(thumbnail.getImageName());
		makeDirPath(targetAddr);
		String relativeAddr = targetAddr + realFileName + extension;
		logger.debug("current relativeAddr is:" + relativeAddr);
		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
		logger.debug("current completeAddr is:" + PathUtil.getImgBasePath() + relativeAddr);
		try {
			Thumbnails.of(thumbnail.getImage())
			.size(300, 300)
			.watermark(Positions.BOTTOM_RIGHT, 
					ImageIO.read(new File(ClassPath + "/watermark.jpg")), 
					0.9f)
			.outputQuality(1f)
			.toFile(dest);
		}catch(IOException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		return relativeAddr;
	}
	
	/*
	 * 处理图片，并返回新生成图片的相对值路径
	 */
	public static String generateNormalImg(ImageHolder thumbnail, String targetAddr) {
		String realFileName = getRandomFileName();
		String extension = getFileExtension(thumbnail.getImageName());
		makeDirPath(targetAddr);
		String relativeAddr = targetAddr + realFileName + extension;
		logger.debug("current relativeAddr is:" + relativeAddr);
		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
		logger.debug("current completeAddr is:" + PathUtil.getImgBasePath() + relativeAddr);
		try {
			Thumbnails.of(thumbnail.getImage())
			.size(337, 640)
			.watermark(Positions.BOTTOM_RIGHT, 
					ImageIO.read(new File(ClassPath + "/watermark.jpg")), 
					0.9f)
			.outputQuality(1f)
			.toFile(dest);
		}catch(IOException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		return relativeAddr;
	}
	
	/*
	 * 创建目标路径所涉及到的目录
	 */
	private static void makeDirPath(String targetAddr) {
		String realFileParentPath = PathUtil.getImgBasePath() + targetAddr;
		File dirPath = new File(realFileParentPath);
		if(!dirPath.exists()) {
			dirPath.mkdirs();
		}
		
	}

	/*
	 * 获取输入文件流的扩展名
	 */
	private static String getFileExtension(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}

	/*
	 * 生成随机文件名，当前年月日小时分钟秒钟+五位随机数
	 */
	public static String getRandomFileName() {
		//获取随机的五位数
		int rannum = r.nextInt(89999) + 10000;
		String nowTimeStr = sDateFormat.format(new Date());
		return nowTimeStr + rannum;
	}
	
	/*
	 * 判断storePath是文件的路径还是目录的路径，
	 * 如果是文件路径，则删除该文件
	 * 如果是目录路径，则删除该目录下的所有文件
	 */
	public static void deleteFileOrPath(String storePath) {
		File fileOrPath = new File(PathUtil.getImgBasePath() + storePath);
		if(fileOrPath.exists()) {
			if(fileOrPath.isDirectory()) {
				File files[] = fileOrPath.listFiles();
				for(File file:files) {
					file.delete();
				}
			}
			fileOrPath.delete();
		}
	}
	
	
	public static void main(String agrs[]) throws IOException {
		//String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		//System.out.println(basePath);
		Thumbnails.of(new File("C:/Users/刘成辉/Desktop/love.jpg"))
		.size(200, 200)
		.watermark(Positions.BOTTOM_RIGHT, 
				ImageIO.read(new File(ClassPath + "/watermark.jpg")), 
				0.25f)
		.outputQuality(1f)
		.toFile("C:/Users/刘成辉/Desktop/lovenew.jpg");
	}
}
