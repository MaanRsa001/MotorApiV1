package com.maan.eway.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.maan.eway.bean.MotorColorMaster;
import com.maan.eway.bean.MotorColorMasterId;

public interface MotorColorMasterRepository
		extends JpaRepository<MotorColorMaster, MotorColorMasterId>,
		JpaSpecificationExecutor<MotorColorMaster> {
	
	MotorColorMaster findByColorIdAndCompanyId(Integer colorid,String cmp_id);

	List<MotorColorMaster> findByColorIdAndCompanyIdAndStatusAndEffectiveDateStartLessThanAndEffectiveDateEndGreaterThan(
			Integer colorIdValue, String companyId, String string, Date date, Date date2);

	List<MotorColorMaster> findByColorIdAndCompanyIdAndStatus(Integer colorIdValue, String companyId, String string);


	List<MotorColorMaster> findTop1AmentIdByColorIdAndCompanyIdOrderByAmendIdDesc(Integer colorIdValue,
			String companyId);
	
	


}
