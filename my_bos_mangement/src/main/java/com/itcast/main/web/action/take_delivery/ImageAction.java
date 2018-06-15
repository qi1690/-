package com.itcast.main.web.action.take_delivery;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itcast.main.web.action.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;

/**
 * 处理kindeditor图片上传 、管理功能
 * 
 * @author itcast
 *
 */
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class ImageAction extends BaseAction<Object> {

	private File imgFile;
	private String imgFileFileName;
	private String imgFileContentType;

	public void setImgFile(File imgFile) {
		this.imgFile = imgFile;
	}

	public void setImgFileFileName(String imgFileFileName) {
		this.imgFileFileName = imgFileFileName;
	}

	public void setImgFileContentType(String imgFileContentType) {
		this.imgFileContentType = imgFileContentType;
	}

	//{"error":0,"url":"\/bos_management\/upload\/16d3137a-f299-4083-92d7-2a12764db1f4.jpg"}
	@Action(value = "image_upload", results = { @Result(name = "success", type = "json") })
	public String upload() throws IOException {
		//获取 工程中指定文件夹的 绝对路径
		String savePath = ServletActionContext.getServletContext().getRealPath(
				"/upload/");
		//获取 文件夹的网络路径：http://xxx:xx/xxx
		String saveUrl = ServletActionContext.getRequest().getContextPath()
				+ "/upload/";
		
		// 生成随机图片名
		UUID uuid = UUID.randomUUID();
		String ext = imgFileFileName
				.substring(imgFileFileName.lastIndexOf("."));
		String randomFileName = uuid + ext;

		// 保存图片 (绝对路径)
		File destFile = new File(savePath + "/" + randomFileName);
		FileUtils.copyFile(imgFile, destFile);
		
		System.out.println("保存后的文件名="+destFile.getName());
		System.out.println("保存后的文件路径="+destFile.getPath());
		System.out.println("保存后的文件绝对路径="+destFile.getAbsolutePath());

		// 通知浏览器文件上传成功
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("error", 0);
		result.put("url", saveUrl + randomFileName); // 返回相对路径
		ActionContext.getContext().getValueStack().push(result);

		return SUCCESS;
	}

	// http://localhost:9001/bos_management/image_manage.action?path=&order=NAME&dir=image&1494319409361
	@Action(value = "image_manage", results = { @Result(name = "success", type = "json") })
	public String manage() {
		// 根目录路径，可以指定绝对路径，比如 d:/xxx/upload/xxx.jpg
		String rootPath = ServletActionContext.getServletContext().getRealPath(
				"/")
				+ "upload/";
		// 根目录URL，可以指定绝对路径，比如 http://www.yoursite.com/attached/
		String rootUrl = ServletActionContext.getRequest().getContextPath()
				+ "/upload/";
		
		System.out.println("manage rootPath="+rootPath);
		System.out.println("manage rootUrl="+rootUrl);

		// 遍历目录取的文件信息
		List<Map<String, Object>> fileList = new ArrayList<Map<String, Object>>();
		// 当前上传目录
		File currentPathFile = new File(rootPath);
		// 图片扩展名
		String[] fileTypes = new String[] { "gif", "jpg", "jpeg", "png", "bmp" };

		if (currentPathFile.listFiles() != null) {
			for (File file : currentPathFile.listFiles()) {
				Map<String, Object> hash = new HashMap<String, Object>();
				String fileName = file.getName();
				if (file.isDirectory()) {
					hash.put("is_dir", true);
					hash.put("has_file", (file.listFiles() != null));
					hash.put("filesize", 0L);
					hash.put("is_photo", false);
					hash.put("filetype", "");
				} else if (file.isFile()) {
					String fileExt = fileName.substring(
							fileName.lastIndexOf(".") + 1).toLowerCase();
					hash.put("is_dir", false);
					hash.put("has_file", false);
					hash.put("filesize", file.length());
					hash.put("is_photo", Arrays.<String> asList(fileTypes)
							.contains(fileExt));
					hash.put("filetype", fileExt);
				}
				hash.put("filename", fileName);
				hash.put("datetime",
						new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file
								.lastModified()));
				fileList.add(hash);
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("moveup_dir_path", "");
		result.put("current_dir_path", rootPath);
		result.put("current_url", rootUrl);
		result.put("total_count", fileList.size());
		result.put("file_list", fileList);
		ActionContext.getContext().getValueStack().push(result);

		return SUCCESS;
	}
}
