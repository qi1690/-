package com.itcast.main.service.base.impl;

import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itcast.main.dao.base.CourierRepository;
import com.itcast.main.domain.base.Courier;
import com.itcast.main.service.base.CourierService;

@Service
@Transactional
public class CourierServiceImpl implements CourierService {

	// 注入DAO 对象
	@Autowired
	private CourierRepository courierRepository;

	@Override
	@RequiresPermissions("courier:add")
	public void save(Courier courier) {
		// 判断快递员是否存在
		// Courier persistCourier =
		// courierRepository.findByCourierNum(courier.getCourierNum());
		// if(persistCourier==null) {
		// courierRepository.save(courier);
		// } else {
		// Integer id = persistCourier.getId();
		// try {
		// BeanUtils.copyProperties(persistCourier, courier);
		// } catch (IllegalAccessException | InvocationTargetException e) {
		// e.printStackTrace();
		// }
		// persistCourier.setId(id);
		// }

		courierRepository.save(courier);
	}

	@Override
	public Page<Courier> findPageData(Specification<Courier> specification, Pageable pageable) {
		return courierRepository.findAll(specification, pageable);
	}

	@Override
	public void managerBatch(String[] idArray,Integer type) {
		if (type.intValue() == 1) {// 作废
			// 调用DAO实现 update修改操作，将deltag 修改为1
			for (String idStr : idArray) {
				Integer id = Integer.parseInt(idStr);
				courierRepository.updateDelTag(id, 1);
			}
		} else if (type.intValue() == 2) {// 还原
			// 调用DAO实现 update修改操作，将deltag 修改为1
			for (String idStr : idArray) {
				Integer id = Integer.parseInt(idStr);
				courierRepository.updateDelTag(id, 0);
			}
		}

	}

	@Override
	public List<Courier> findNoAssociation() {
		// 封装Specification
		Specification<Courier> specification = new Specification<Courier>() {
			@Override
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// 查询条件，判定列表size为空
				Predicate p = cb.isEmpty(root.get("fixedAreas").as(Set.class));
				return p;
			}
		};
		return courierRepository.findAll(specification);
	}

	@Override
	public Page<Courier> findAll(Pageable pageable) {
		return courierRepository.findAll(pageable);
	}

}
