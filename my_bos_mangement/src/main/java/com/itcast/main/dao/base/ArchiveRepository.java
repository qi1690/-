package com.itcast.main.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itcast.main.domain.base.Archive;

public interface ArchiveRepository extends JpaRepository<Archive, Integer>{

}
