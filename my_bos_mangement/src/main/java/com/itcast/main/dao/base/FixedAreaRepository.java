package com.itcast.main.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.itcast.main.domain.base.FixedArea;

public interface FixedAreaRepository extends JpaRepository<FixedArea, String>,
		JpaSpecificationExecutor<FixedArea> {

}
