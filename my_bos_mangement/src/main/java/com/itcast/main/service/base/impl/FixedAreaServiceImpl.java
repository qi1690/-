package com.itcast.main.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itcast.main.dao.base.CourierRepository;
import com.itcast.main.dao.base.FixedAreaRepository;
import com.itcast.main.dao.base.TakeTimeRepository;
import com.itcast.main.domain.base.Courier;
import com.itcast.main.domain.base.FixedArea;
import com.itcast.main.domain.base.TakeTime;
import com.itcast.main.service.base.FixedAreaService;

@Service
@Transactional
public class FixedAreaServiceImpl implements FixedAreaService {

	// 注入DAO
	@Autowired
	private FixedAreaRepository fixedAreaRepository;

	@Override
	public void save(FixedArea fixedArea) {
		fixedAreaRepository.save(fixedArea);
	}

	@Override
	public Page<FixedArea> findPageData(Specification<FixedArea> specification,
			Pageable pageable) {
		return fixedAreaRepository.findAll(specification, pageable);
	}

	@Autowired
	private CourierRepository courierRepository;
	@Autowired
	private TakeTimeRepository takeTimeRepository;

	@Override
	public void associationCourierToFixedArea(FixedArea fixedArea,
			Integer courierId, Integer takeTimeId) {
		FixedArea persistFixedArea = fixedAreaRepository.findOne(fixedArea
				.getId());
		Courier courier = courierRepository.findOne(courierId);
		TakeTime takeTime = takeTimeRepository.findOne(takeTimeId);
		// 快递员 关联到 定区上
		persistFixedArea.getCouriers().add(courier);

		// 将收派标准 关联到 快递员上
		courier.setTakeTime(takeTime);
	}

	@Override
	public List<FixedArea> findAll() {
		return fixedAreaRepository.findAll();
	}

}
