package com.itcast.main.service.base;

import java.util.List;

import com.itcast.main.domain.base.TakeTime;

//收派时间 接口
public interface TakeTimeService {

	// 查询所有收派时间
	List<TakeTime> findAll();

}
