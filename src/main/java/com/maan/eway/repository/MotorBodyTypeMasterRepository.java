package com.maan.eway.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.maan.eway.bean.MotorBodyTypeMaster;
import com.maan.eway.bean.MotorBodyTypeMasterId;

public interface MotorBodyTypeMasterRepository extends JpaRepository<MotorBodyTypeMaster, MotorBodyTypeMasterId> , JpaSpecificationExecutor<MotorBodyTypeMaster>{

	List<MotorBodyTypeMaster> findByBodyId(Integer valueOf);

	List<MotorBodyTypeMaster> findByBodyNameEnAndBranchCodeAndCompanyIdOrderByAmendIdDesc(String resBodyType,
			String string, String insuranceId);
	List<MotorBodyTypeMaster> findAllByCompanyIdAndBranchCodeAndBodyIdAndEffectiveDateStartLessThanEqualAndEffectiveDateEndGreaterThan(
			String insuranceId, String string, Integer bodyId, Date today, Date today2);
	
	@Query("""
			SELECT b FROM MotorBodyTypeMaster b
			WHERE (
			    UPPER(REPLACE(b.bodyNameEn,' ','')) 
			        LIKE CONCAT('%', UPPER(REPLACE(:bodyName,' ','')), '%')
			    OR
			    UPPER(REPLACE(:bodyName,' ','')) 
			        LIKE CONCAT('%', UPPER(REPLACE(b.bodyNameEn,' ','')), '%')
			)
			AND UPPER(b.bodyType) = 'T'
			""")
			List<MotorBodyTypeMaster> findTrailerBodyType(@Param("bodyName") String bodyName);


}
