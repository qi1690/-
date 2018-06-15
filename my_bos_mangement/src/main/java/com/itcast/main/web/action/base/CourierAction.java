package com.itcast.main.web.action.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.itcast.main.domain.base.Courier;
import com.itcast.main.domain.base.Standard;
import com.itcast.main.service.base.CourierService;
import com.itcast.main.web.action.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class CourierAction extends BaseAction<Courier>{


	// 注入Service
	@Autowired
	private CourierService courierService;

	// 添加快递员方法
	@Action(value = "courier_save", 
			results = { @Result(name = "success", location = "./pages/base/courier.html", type = "redirect") })
	public String save() {
		
		courierService.save(model);
		return SUCCESS;
	}

	
	
	// 属性驱动接收客户端分页参数
	//1、接收前端传过来的值（属性驱动）：page+rows
	
	private int page;
	private int rows;

	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	@Action(value="test_pageQuery",results={@Result(name="success",type="json")})
	public String testPageQuery(){
//		2、将上面的两个值封装到pageable(pageRequest)
		Pageable pageable = new PageRequest(page -1, rows);
//		3、调用service层的pageQuery方法，同时将pageable传递过去
//		4、service调用的是JPARepository中的findAll(Pageable)
		Page<Courier> pageData = courierService.findAll(pageable);
//		5、将返回回来的Page对象，进行重新封装
		Map<String, Object> result = new HashMap<>();
		result.put("total", pageData.getTotalElements());
		result.put("rows", pageData.getContent());
//		6、将封装后的结果集压入到植栈
		ActionContext.getContext().getValueStack().push(result);
		
		return SUCCESS;
	}
	
	// 分页查询方法
	/*
	 * 带条件的分页查询：
	 * 
	 * 使用hql语句来写查询：
	 * 		from Courier where num = ? and standard.name like ? and xxx
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	
	
	
	
	@Action(value = "courier_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {
		System.out.println("pageQuery courier.getStandard()="+model.getStandard());
		// 封装Pageable对象
		Pageable pageable = new PageRequest(page - 1, rows);
		
		
		// 封装条件查询对象 Specification
		Specification<Courier> specification = new Specification<Courier>() {
			@Override
			/*
			 *  Root 用于获取属性字段，
			 *  CriteriaQuery可以用于简单条件查询，
			 *  CriteriaBuilder 用于构造复杂条件查询
			 *  
			 *  
			 *  完整sql:
			 *  
			 *  select * from T_COURIER 
			 *  
			 *  
			 *  where C_COURIER_NUM = ? and C_COMPANY like ? and C_TYPE = ?  limit 起始页,每页数量
			 */
			public Predicate toPredicate(Root<Courier> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				// 简单单表查询
				// sql: 
				if (StringUtils.isNotBlank(model.getCourierNum())) {
					// root.get("courierNum") ------ > 得到Courier实体类中的属性courierNum对应的表字段 “C_COURIER_NUM”的值
					// C_COURIER_NUM = ? 
					Predicate p1 = cb.equal(root.get("courierNum").as(String.class),model.getCourierNum());
					list.add(p1);
				}
				
				// company like %?%  
				if (StringUtils.isNotBlank(model.getCompany())) {
					Predicate p2 = cb.like(
							root.get("company").as(String.class),
							"%" + model.getCompany() + "%");
					list.add(p2);
				}
				
				// type = ?
				if (StringUtils.isNotBlank(model.getType())) {
					Predicate p3 = cb.equal(root.get("type").as(String.class),
							model.getType());
					list.add(p3);
				}
				
//				select * from 
//				t_courier c inner join t_standard s on c.c_standard_id = s.c_id;
				
				/*
				 * courier 表与 standard表的连接查询
				 * 查询条件：工号、收派标准、所属单位、类型
				 * select * from courier inner join stadard on C_STANDARD_ID = C_ID where 工号=? and 
				 * 			收派标准 = ? and 所属单位 like %?% and 类型 = ?
				 */
				
				// 多表查询(进入 到  courier实体类中的对象属性 Standard实体类的操作中
				Join<Courier, Standard> standardRoot = root.join("standard",
						JoinType.INNER);
				if (model.getStandard() != null
						&& StringUtils.isNotBlank(model.getStandard().getName())) {
					Predicate p4 = cb.like(
							//c_name like ....
							standardRoot.get("name").as(String.class), "%"
									+ model.getStandard().getName() + "%");
					list.add(p4);
				}
				/*
				 * 四个条件的查询Sql合并到一起的效果应该是：
				 * 
				 * 	select * from t_courier inner join t_standard s on c.c_standard_id = s.c_id
				 * 	where C_COURIER_NUM = ? and C_COMPANY like ? and C_TYPE = ?
				 * 
				 * 		and s.name like ?
				 * 
				 * 
				 * 
				 */
				
				
				return cb.and(list.toArray(new Predicate[0]));   
//				return cb.and(list.toArray(new Predicate[list.size()])); 
//				Predicate[] arrayType = new Predicate[list.size()];
//				return cb.and(list.toArray(arrayType)); 
			}
		};

		// 调用业务层 ，返回 Page
		Page<Courier> pageData = courierService.findPageData(specification,
				pageable);
		// 将返回page对象 转换datagrid需要格式
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", pageData.getTotalElements());
		result.put("rows", pageData.getContent());
		// 将结果对象 压入值栈顶部
		ActionContext.getContext().getValueStack().push(result);

		return SUCCESS;
	}

	// 属性驱动
	private String ids;

	public void setIds(String ids) {
		this.ids = ids;
	}
	
	private String batchType;

	public void setBatchType(String batchType) {
		this.batchType = batchType;
	}

	// 作废快递员
	@Action(value = "courier_managerBatch", results = { @Result(name = "success", location = "./pages/base/courier.html", type = "redirect") })
	public String managerBatch() {
		// 按,分隔ids
		String[] idArray = ids.split(",");
		// 调用业务层，批量作废
		courierService.managerBatch(idArray,Integer.parseInt(batchType));

		return SUCCESS;
	}

	@Action(value = "courier_findnoassociation", 
			results = { @Result(name = "success", type = "json") })
	public String findnoassociation() {
		// 调用业务层，查询未关联定区的快递员 
		List<Courier> couriers = courierService.findNoAssociation();
		// 将查询快递员列表 压入值栈 
		ActionContext.getContext().getValueStack().push(couriers);
		return SUCCESS;
	}
}
