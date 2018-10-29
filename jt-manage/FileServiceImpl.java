package com.jt.manage.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jt.common.vo.PicUploadResult;
import com.jt.manage.service.FileService;

@Service
public class FileServiceImpl implements FileService{
	
	@Value("${image.localPath}")	//可以动态获取Spring 容器中的key值
	private String localPath;// = "D:/jt-upload/";
	@Value("${image.urlPath}")
	private String urlPath;// = "http://image.jt.com/";
	
	/** 
	 * 文件上传
	 * 1 判断图片的类型jpg|png|gif
	 * 2 判断是否为恶意程序 exe.jpg
	 * 3 为了提高检索的效率，将文件分文件存储
	 * 		UUID:hash随机算法（当前毫秒+算法+hash）=32位的hash值 2^32
	 * 				aaaaaaaa-bbbbbbbb-cccccccc-dddddddd/1.jpg
	 * 				优点：几乎永远不用修改代码
	 * 				缺点：但是非常消耗内存
	 * 		年月日：yyyy/MM/dd
	 * 4 如何杜绝文件重名现象
	 * 		UUID+随机数（100）.jpg
	 * 5 实现文件上传
	 */
	@Override
	public PicUploadResult fileUpload(MultipartFile uploadFile) {
		PicUploadResult uploadResult = new PicUploadResult();
		//1 判断是否为图片类型
		String fileName = uploadFile.getOriginalFilename();
		fileName = fileName.toLowerCase();//都改成小写，正则就不用判断JAG...那些
		//用正则
		String regex ="^.*\\.(jpg|png|gif)$";
		if(!fileName.matches(regex)){
			uploadResult.setError(1);	//1表示不是图片
			System.out.println("后缀名都不是图片的格式");
			return uploadResult;	//返回结果，文件不是图片
		}
		//2 判断是否为恶意程序
		try {
			BufferedImage buffereImager = ImageIO.read(uploadFile.getInputStream());
			int height = buffereImager.getHeight();	//获取图片的高度
			int width = buffereImager.getWidth(); 	//获取图片的宽度
			
			if(height==0 || width==0){//只要有0，就不是图片
				uploadResult.setError(1);
				return uploadResult;
			}
			//程序执行到这里，证明是一张图片了
			//为文件进行分文件存储，yyyy/MM/dd
			String datePath = 
					new SimpleDateFormat("yyyy/MM/dd").format(new Date());
			//定文件保存的路径
			String dirPath = localPath+datePath;
			//判断文件夹是否存在
			File dirFile = new File(dirPath);
			if(!dirFile.exists()){
				dirFile.mkdirs();
			}
			//动态生成文件名称UUID+三位随机数
			String uuid = UUID.randomUUID().toString().replace("-","");
			int randomNum = new Random().nextInt(1000);//3位数，所以要0-999
			String imageFileType = 
					fileName.substring(fileName.lastIndexOf("."));//.jpg
			String imageFileName = uuid + randomNum + imageFileType;
			//5 实现上传文件		文件路径+文件名
			File dest=new File(dirPath+"/"+imageFileName);
			uploadFile.transferTo(dest);
			//6 封装参数
			uploadResult.setHeight(height+"");
			uploadResult.setWidth(width+"");
			System.out.println("上传文件成功！");
			//7 准备虚拟路径
			String imageUrlPath = urlPath+datePath+"/"+imageFileName;
			uploadResult.setUrl(imageUrlPath);//添加虚拟路径
			
		} catch (IOException e) {
			e.printStackTrace();
			uploadResult.setError(1);
		}
		return uploadResult;
	}


}
