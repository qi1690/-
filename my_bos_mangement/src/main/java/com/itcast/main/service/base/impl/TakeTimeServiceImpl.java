package com.itcast.main.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itcast.main.dao.base.TakeTimeRepository;
import com.itcast.main.domain.base.TakeTime;
import com.itcast.main.service.base.TakeTimeService;

@Service
@Transactional
public class TakeTimeServiceImpl implements TakeTimeService {

	@Autowired
	private TakeTimeRepository takeTimeRepository;

	@Override
	public List<TakeTime> findAll() {
		return takeTimeRepository.findAll();
	}

}
