package com.itcast.main.dao.take_delivery;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.itcast.main.domain.take_delivery.Promotion;

public interface PromotionRepository extends JpaRepository<Promotion, Integer> {

	@Query("update Promotion set status='2' where endDate<? and status='1'")
	@Modifying
	void updateStatus(Date now);

}
