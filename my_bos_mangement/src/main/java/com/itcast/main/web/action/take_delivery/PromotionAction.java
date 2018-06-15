package com.itcast.main.web.action.take_delivery;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.itcast.main.domain.take_delivery.Promotion;
import com.itcast.main.service.take_delivery.PromotionService;
import com.itcast.main.web.action.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;

/**
 * 宣传活动 管理
 * 
 * @author itcast
 *
 */
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class PromotionAction extends BaseAction<Promotion> {

	private File titleImgFile;
	private String titleImgFileFileName;

	public void setTitleImgFileFileName(String titleImgFileFileName) {
		this.titleImgFileFileName = titleImgFileFileName;
	}

	public void setTitleImgFile(File titleImgFile) {
		this.titleImgFile = titleImgFile;
	}

	@Autowired
	private PromotionService promotionService;

	@Action(value = "promotion_save", results = { @Result(name = "success", type = "redirect", location = "./pages/take_delivery/promotion.html") })
	public String save() throws IOException {
		// 宣传图 上传、在数据表保存 宣传图路径
		String savePath = ServletActionContext.getServletContext().getRealPath(
				"/upload/");
		String saveUrl = ServletActionContext.getRequest().getContextPath()
				+ "/upload/";
		
		System.out.println("promotion savePath="+savePath);
		System.out.println("promotion saveUrl="+saveUrl);

		// 生成随机图片名
		UUID uuid = UUID.randomUUID();
		String ext = titleImgFileFileName.substring(titleImgFileFileName
				.lastIndexOf("."));
		String randomFileName = uuid + ext;

		// 保存图片 (绝对路径)
		File destFile = new File(savePath + "/" + randomFileName);
		System.out.println(destFile.getAbsolutePath());
		FileUtils.copyFile(titleImgFile, destFile);

		// 将保存路径 相对工程web访问路径，保存model中
		String titleImgPath = ServletActionContext.getRequest().getContextPath()
				+ "/upload/" + randomFileName;
//		System.out.println("titleImgPath="+titleImgPath);
		model.setTitleImg(titleImgPath);

		// 调用业务层，完成活动任务数据保存
		promotionService.save(model);

		return SUCCESS;
	}
	
	@Action(value = "promotion_edit", results = { @Result(name = "success", type = "redirect", location = "./pages/take_delivery/promotion.html") })
	public String edit() throws IOException {
		if(titleImgFile==null || StringUtils.isBlank(titleImgFileFileName)){
			Promotion promotion = promotionService.findById(model.getId());
			model.setTitleImg(promotion.getTitleImg());
		} else {
			// 宣传图 上传、在数据表保存 宣传图路径
			String savePath = ServletActionContext.getServletContext().getRealPath(
					"/upload/");
			String saveUrl = ServletActionContext.getRequest().getContextPath()
					+ "/upload/";
			
			System.out.println("promotion savePath="+savePath);
			System.out.println("promotion saveUrl="+saveUrl);

			// 生成随机图片名
			UUID uuid = UUID.randomUUID();
			String ext = titleImgFileFileName.substring(titleImgFileFileName
					.lastIndexOf("."));
			String randomFileName = uuid + ext;

			// 保存图片 (绝对路径)
			File destFile = new File(savePath + "/" + randomFileName);
			System.out.println(destFile.getAbsolutePath());
			FileUtils.copyFile(titleImgFile, destFile);

			// 将保存路径 相对工程web访问路径，保存model中
			String titleImgPath = ServletActionContext.getRequest().getContextPath()
					+ "/upload/" + randomFileName;
//			System.out.println("titleImgPath="+titleImgPath);
			model.setTitleImg(titleImgPath);
		}

		// 调用业务层，完成活动任务数据保存
		promotionService.save(model);

		return SUCCESS;
	}

	@Action(value = "promotion_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {
		// 构造分页查询参数
		Pageable pageable = new PageRequest(page - 1, rows);
		// 调用业务层 完成查询
		Page<Promotion> pageData = promotionService.findPageData(pageable);
		// 压入值栈
		pushPageDataToValueStack(pageData);

		return SUCCESS;
	}
	
	
	@Action(value = "promotion_findById",results = { @Result(name = "success", type = "json") })
	public String findById() throws IOException { 
		Promotion promotion = promotionService.findById(model.getId());
		ActionContext.getContext().getValueStack().push(promotion);
		return SUCCESS;
	}
}
