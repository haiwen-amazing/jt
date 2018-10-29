package com.jt.manage.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jt.common.vo.PicUploadResult;
import com.jt.manage.service.FileService;

@Controller
public class FileController {

	@Autowired
	private FileService fileService ;
	
	/**
	 * 测试：
	 * 如果参数需要赋值，需要通过springMVC所提供的解析器才可以
	 * 参数接受，必须是和页面提交的name属性相同，否则参数不能提交
	 * @param image
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/file")
	public String imageFile(MultipartFile image) throws IOException{
		//1 定义文件上传的目录d:\jt-upload
		File imageFile = new File("D:/jt-upload");
		//2 判断文件是否存在
		if(!imageFile.exists()){//如果不存在
			//创建文件夹
			imageFile.mkdirs();
		}
		//3 获取文件流信息和名称,spring已经封装好输出流和输入流，用这个方法就行了
		//InputStream inputStream = image.getInputStream();
		String fileName = image.getOriginalFilename();
		//4 实现文件上传 	文件路径/文件名称
		File dest = new File("D:/jt-upload/"+fileName);
		image.transferTo(dest);
		System.out.println("文件上传成功");
		//跳转到index页面
		return "index";
	}

	/** 文件上传 */
	@RequestMapping("/pic/upload")
	@ResponseBody
	public PicUploadResult fileUpload(MultipartFile uploadFile){
		
		return fileService.fileUpload(uploadFile);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
