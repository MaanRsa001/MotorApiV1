package com.maan.eway.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.maan.eway.bean.CollateralDetails;


@Repository
public interface CollateralDetailsRepository  extends JpaRepository<CollateralDetails,Integer> ,JpaSpecificationExecutor<CollateralDetails>{

	void deleteByProductIdAndRequestReferenceNoAndLocationIdAndSectionId(
	        String productId, String requestReferenceNo, String locationId, String sectionId
	);

	List<CollateralDetails> findByRequestReferenceNo(String requestReferenceno);


}
