package com.itcast.main.web.action.base;


import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.itcast.main.domain.base.Archive;
import com.itcast.main.service.base.ArchiveService;
import com.itcast.main.web.action.common.BaseAction;

public class ArchiveAction extends BaseAction<Archive>{

	private Archive archive = new Archive();
	
	public Archive getModel() {
		return archive;
	}

	@Autowired
	private ArchiveService archiveService;
	
	@Action(value="archive_save",results={@Result(name="success",type="redirect",location="./pages/base/archives.html")})
	public String saveArchives(){
		
		archiveService.saveArchive(archive);
		
		return SUCCESS;
	}
	
	@Action(value="archive_update",results={@Result(name="success",type = "redirect",location="./page/base/archive.html")})
	public String updateArchive(){
		archiveService.updateArchive(archive);
		return SUCCESS;
	}
	
	//删除的主键ID
	private int id;
	public void setId(int id) {
		this.id = id;
	}

	@Action(value="archive_delete",results={@Result(name="success",type="redirect",location="./page/base/archive.html")})
	public String deleteArchive(){
		archiveService.deleteArchive(id);
		return SUCCESS;
	}

	@Action(value="archive_page",results={@Result(name="success",type="json")})
	public String pageQuery(){
		Pageable pageable = new PageRequest(page-1, rows);
		
		Page<Archive> pageData = archiveService.findPageData(pageable);
		
		//封装分页查询返回的结果
		pushPageDataToValueStack(pageData);
		
		return SUCCESS;
	}
}
