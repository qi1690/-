package com.itcast.main.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.itcast.main.domain.base.SubArea;

public interface SubAreaService {

	Page<SubArea> findPageData(Pageable pageable);

	Page<SubArea> findPageData(Specification<SubArea> specification,Pageable pageable);

	void saveSubArea(SubArea subArea);

	void modifySubArea(SubArea subArea);
	
	void deleteSubAreaById(String[] idArray);

	List<SubArea> findAllByFixedAreaId(String id);
}
