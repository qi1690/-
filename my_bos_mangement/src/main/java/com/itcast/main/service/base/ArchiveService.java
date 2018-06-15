package com.itcast.main.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itcast.main.domain.base.Archive;
import com.itcast.main.domain.base.SubArchive;

public interface ArchiveService {

	void saveArchive(Archive archive);

	void updateArchive(Archive archive);

	void deleteArchive(Integer id);

	Page<Archive> findPageData(Pageable pageable);

	void saveSubArchive(SubArchive subArchive);

	void updateSubArchive(SubArchive subArchive);

	void deleteSubArchive(Integer id);

	Page<SubArchive> findSubPageData(Pageable pageable);
}
