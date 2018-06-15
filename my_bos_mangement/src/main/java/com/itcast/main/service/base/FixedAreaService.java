package com.itcast.main.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.itcast.main.domain.base.FixedArea;

public interface FixedAreaService {

	void save(FixedArea model);

	Page<FixedArea> findPageData(Specification<FixedArea> specification,
			Pageable pageable);

	void associationCourierToFixedArea(FixedArea model, Integer courierId,
			Integer takeTimeId);

	List<FixedArea> findAll();


}
