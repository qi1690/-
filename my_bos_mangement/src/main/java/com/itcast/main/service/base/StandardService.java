package com.itcast.main.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itcast.main.domain.base.Standard;

/**
 * 收派标准管理
 * 
 * @author itcast
 * 
 */
public interface StandardService {
	public void save(Standard standard);

	// 分页查询
	public Page<Standard> findPageData(Pageable pageable);

	// 查询所有收派标准
	public List<Standard> findAll();

}
