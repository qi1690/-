package com.itcast.main.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.itcast.main.domain.base.SubArea;

public interface SubAreaRepository extends JpaRepository<SubArea, Integer>,JpaSpecificationExecutor<SubArea>{

	@Query("delete from SubArea where id = ?")
	@Modifying
	public void deleteSubAreaById(String id);

	@Query("from SubArea where fixedArea.id = ?")
	public List<SubArea> findAllByFixedAreaId(String id);
}
