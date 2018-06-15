package com.itcast.main.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itcast.main.domain.base.TakeTime;

public interface TakeTimeRepository extends JpaRepository<TakeTime, Integer> {

}
