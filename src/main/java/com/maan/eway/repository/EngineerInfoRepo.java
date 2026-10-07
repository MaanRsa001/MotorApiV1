package com.maan.eway.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.eway.bean.EngineerInfo;
import com.maan.eway.bean.EngineerInfoId;
import com.maan.eway.bean.UwQuestionsDetails;

public interface EngineerInfoRepo  extends JpaRepository<EngineerInfo, EngineerInfoId> ,JpaSpecificationExecutor<EngineerInfo>{

	

	List<EngineerInfo> findByRequestReferenceNo(String requestReferenceNo);
	
	void deleteByProductidAndRequestReferenceNoAndLocationIdAndSectionId(
			Integer productid, String requestReferenceNo, Integer locationId, String sectionId);

	boolean existsByEngineNumber(String engineNumber);

	boolean existsBySerialNumber(String serialNumber);

	boolean existsByEngineNumberAndRequestReferenceNoNot(String engineNumber, String requestReferenceNo);

	boolean existsBySerialNumberAndRequestReferenceNoNot(String serialNumber, String requestReferenceNo);
}
