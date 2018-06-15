package com.itcast.main.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.itcast.main.domain.base.Courier;

/**
 * 快递员操作接口
 * 
 * @author itcast
 *
 */
public interface CourierService {

	// 保存快递员
	public void save(Courier courier);

	// 分页查询
	public Page<Courier> findPageData(Specification<Courier> specification,
			Pageable pageable);

	// 批量作废
	public void managerBatch(String[] idArray,Integer type);

	// 查询未关联定区的快递员
	public List<Courier> findNoAssociation();

	public Page<Courier> findAll(Pageable pageable);

}
