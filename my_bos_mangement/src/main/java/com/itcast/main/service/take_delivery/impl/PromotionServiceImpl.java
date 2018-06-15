package com.itcast.main.service.take_delivery.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itcast.main.dao.take_delivery.PromotionRepository;
import com.itcast.main.domain.page.PageBean;
import com.itcast.main.domain.take_delivery.Promotion;
import com.itcast.main.service.take_delivery.PromotionService;

@Service
@Transactional
public class PromotionServiceImpl implements PromotionService {

	@Autowired
	private PromotionRepository promotionRepository;

	@Override
	public void save(Promotion promotion) {
		promotionRepository.save(promotion);
	}

	@Override
	public Page<Promotion> findPageData(Pageable pageable) {
		return promotionRepository.findAll(pageable);
	}

	@Override
	public PageBean<Promotion> findPageData(int page, int rows) {
		Pageable pageable = new PageRequest(page - 1, rows);
		Page<Promotion> pageData = promotionRepository.findAll(pageable);

		// 封装到Page对象
		PageBean<Promotion> pageBean = new PageBean<Promotion>();
		pageBean.setTotalCount(pageData.getTotalElements());
		pageBean.setPageData(pageData.getContent());

		return pageBean;
	}
	
	@Override
	public Page<Promotion> findPageData2(int page, int rows) {
		Pageable pageable = new PageRequest(page - 1, rows);
		Page<Promotion> pageData = promotionRepository.findAll(pageable);
		System.out.println("findPageData2="+pageData);
		return pageData;
	}

	@Override
	public Promotion findById(Integer id) {
		return promotionRepository.findOne(id);
	}

	@Override
	public void updateStatus(Date date) {
		promotionRepository.updateStatus(date);
	}

}
