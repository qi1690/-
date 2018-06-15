package com.itcast.main.web.action.base;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.itcast.main.domain.base.Area;
import com.itcast.main.service.base.AreaService;
import com.itcast.main.utils.PinYin4jUtils;
import com.itcast.main.web.action.common.BaseAction;

public class AreaAction extends BaseAction<Area> {

	// 注入业务层对象
	@Autowired
	private AreaService areaService;

	// 接收上传文件
	private File file2;
	private String file2FileName;
	private String file2ContentType;

	// public void setFile(File file) {
	// this.file = file;
	// }
	//
	// public void setFileFileName(String fileFileName) {
	// this.fileFileName = fileFileName;
	// }
	//
	// public void setFileContentType(String fileContentType) {
	// this.fileContentType = fileContentType;
	// }

	/**
	 * 专门用于解析excel文件，并返回集合区域信息
	 * 
	 * @author caiwen
	 * @date 2017年11月30日
	 * @param file
	 *            要解析的Excel的文件
	 * @return 区域信息的集合列表
	 * @throws Exception
	 */
	public List<Area> batchExcel(File file) throws Exception {
		List<Area> list = new ArrayList<Area>();

		// //通过类名称来实例化
		// Class clazz = Class.forName(beanName);
		// Object obj = clazz.newInstance();
		// //获取clazz的属性和方法
		// Field[] fs = clazz.getFields();
		// for(Field f : fs){
		// f.getName()
		// }

		// 实例化HSSFWorkbook
//		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(file));
		
		
		Workbook  workbook = WorkbookFactory.create(file);
		// 得到Sheet
		Sheet sheet = workbook.getSheetAt(0);
		// 循环遍历
		for (Row row : sheet) {
			// 一行数据 对应 一个区域对象
			if (row.getRowNum() == 0) {
				// 第一行 跳过
				continue;
			}
			// 跳过空行
			if (row.getCell(0) == null || StringUtils.isBlank(row.getCell(0).getStringCellValue())) {
				continue;
			}

			Area area = new Area();
			area.setId(row.getCell(0).getStringCellValue());
			area.setProvince(row.getCell(1).getStringCellValue());
			area.setCity(row.getCell(2).getStringCellValue());
			area.setDistrict(row.getCell(3).getStringCellValue());
			area.setPostcode(row.getCell(4).getStringCellValue());
			// 基于pinyin4j生成城市编码和简码
			String province = area.getProvince();
			String city = area.getCity();
			String district = area.getDistrict();
			province = province.substring(0, province.length() - 1);
			city = city.substring(0, city.length() - 1);
			district = district.substring(0, district.length() - 1);
			// 简码
			String[] headArray = PinYin4jUtils.getHeadByString(province + city + district);
			StringBuffer buffer = new StringBuffer();
			for (String headStr : headArray) {
				buffer.append(headStr);
			}
			String shortcode = buffer.toString();
			area.setShortcode(shortcode);
			// 城市编码
			String citycode = PinYin4jUtils.hanziToPinyin(city, "");
			area.setCitycode(citycode);

			// 将结果集Area保存到集合中
			list.add(area);
		}

		workbook.close();
		return list;

	}

	// 批量区域数据导入
	@Action(value = "area_batchImport")
	public String batchImport() throws Exception {

		List<Area> areas = new ArrayList<Area>();
		System.out.println("文件名=" + file2FileName);
		System.out.println("文件类型=" + file2ContentType);
		areas = batchExcel(file2);
		// 调用业务层
		areaService.saveBatch(areas);
		// }
		return NONE;
	}

	public void setFile2(File file2) {
		this.file2 = file2;
	}

	public void setFile2FileName(String file2FileName) {
		this.file2FileName = file2FileName;
	}

	public void setFile2ContentType(String file2ContentType) {
		this.file2ContentType = file2ContentType;
	}

	// 分页查询
	@Action(value = "area_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {
		// 构造分页查询对象
		Pageable pageable = new PageRequest(page - 1, rows);
		// 构造条件查询对象
		Specification<Area> specification = new Specification<Area>() {
			@Override
			public Predicate toPredicate(Root<Area> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				if (StringUtils.isNotBlank(model.getProvince())) {
					Predicate p1 = cb.like(root.get("province").as(String.class), "%" + model.getProvince() + "%");
					list.add(p1);
				}
				if (StringUtils.isNotBlank(model.getCity())) {
					Predicate p2 = cb.like(root.get("city").as(String.class), "%" + model.getCity() + "%");
					list.add(p2);
				}
				if (StringUtils.isNotBlank(model.getDistrict())) {
					Predicate p3 = cb.like(root.get("district").as(String.class), "%" + model.getDistrict() + "%");
					list.add(p3);
				}
				return cb.and(list.toArray(new Predicate[0]));
			}
		};
		// 调用业务层完成查询
		Page<Area> pageData = areaService.findPageData(specification, pageable);
		// 压入值栈
		pushPageDataToValueStack(pageData);

		return SUCCESS;
	}
}
