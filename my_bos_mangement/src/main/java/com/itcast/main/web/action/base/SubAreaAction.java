package com.itcast.main.web.action.base;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.itcast.main.domain.base.Area;
import com.itcast.main.domain.base.FixedArea;
import com.itcast.main.domain.base.SubArea;
import com.itcast.main.service.base.SubAreaService;
import com.itcast.main.web.action.common.BaseAction;

public class SubAreaAction extends BaseAction<SubArea> {

	@Autowired
	private SubAreaService subAreaService;

	@Action(value = "subarea_page", results = { @Result(name = "success", type = "json") })
	public String subAreaPageQuery() {
		Pageable pageable = new PageRequest(page - 1, rows);

		Specification<SubArea> specification = new Specification<SubArea>() {
			@Override
			public Predicate toPredicate(Root<SubArea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> precList = new ArrayList<>();

				if (StringUtils.isNotBlank(model.getKeyWords())) {// 关键字判断
					Predicate pr1 = cb.like(root.get("keyWords").as(String.class), "%" + model.getKeyWords() + "%");
					precList.add(pr1);
				}
				// 与区域表进行关联 查询
				Join<SubArea, Area> areaJoin = root.join("area", JoinType.INNER);
				// 条件：省份查询
				if (model.getArea() != null && StringUtils.isNotBlank(model.getArea().getProvince())) {
					Predicate pr2 = cb.equal(areaJoin.get("province").as(String.class), model.getArea().getProvince());
					precList.add(pr2);
				}
				// 条件：城市 查询
				if (model.getArea() != null && StringUtils.isNotBlank(model.getArea().getCity())) {
					Predicate pr3 = cb.equal(areaJoin.get("city").as(String.class), model.getArea().getCity());
					precList.add(pr3);
				}
				// 条件：区域 查询
				if (model.getArea() != null && StringUtils.isNotBlank(model.getArea().getDistrict())) {
					Predicate pr4 = cb.equal(areaJoin.get("district").as(String.class), model.getArea().getDistrict());
					precList.add(pr4);
				}
				// 外连接 定区表
				Join<SubArea, FixedArea> fixedAreaJoin = root.join("fixedArea", JoinType.INNER);
				if (model.getFixedArea() != null && StringUtils.isNotBlank(model.getFixedArea().getFixedAreaName())) {
					Predicate pre5 = cb.like(fixedAreaJoin.get("fixedAreaName").as(String.class),
							"%" + model.getFixedArea().getFixedAreaName() + "%");
					precList.add(pre5);
				}
				//
				if (model.getFixedArea() != null
						&& org.apache.commons.lang3.StringUtils.isNotBlank(model.getFixedArea().getId())) {
					Predicate pre6 = cb.like(fixedAreaJoin.get("id").as(String.class), model.getFixedArea().getId());
					precList.add(pre6);
				}

				return cb.and(precList.toArray(new Predicate[0]));
			}
		};
		Page<SubArea> pageData = subAreaService.findPageData(specification, pageable);

		pushPageDataToValueStack(pageData);

		return SUCCESS;
	}

	@Action(value = "findAllByFixedAreaId", results = { @Result(name = "success", type = "json") })
	public String findSubareaListByFixedAreaId() {
		List<SubArea> subareaList = subAreaService.findAllByFixedAreaId(model.getFixedArea().getId());
		pushPageDataToValueStack(subareaList);
		return SUCCESS;
	}

	@Action(value = "subarea_modify", results = {
			@Result(name = "success", type = "redirect", location = "./pages/base/sub_area.html") })
	public String modifySubArea() {
		subAreaService.modifySubArea(model);
		return SUCCESS;
	}

	private String ids;

	public void setIds(String ids) {
		this.ids = ids;
	}

	@Action(value = "subarea_delete", results = {
			@Result(name = "success", type = "redirect", location = "./pages/base/sub_area.html") })
	public String deleteById() {
		String[] idArray = ids.split(",");
		subAreaService.deleteSubAreaById(idArray);
		return SUCCESS;
	}
}
