package com.maan.eway.repository;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.eway.bean.BuildingDetails;
import com.maan.eway.bean.BuildingDetailsId;

public interface BuildingDetailsRepository extends JpaRepository<BuildingDetails, BuildingDetailsId>, JpaSpecificationExecutor<BuildingDetails> {


	// BuildingDetails findByRequestReferenceNoAndRiskId(String requestReferenceNo, Integer valueOf);

	//	List<BuildingDetails> findByRequestReferenceNo(String requestReferenceNo);

	BuildingDetails findByRequestReferenceNoAndRiskId(String req, Integer valueOf);
	
	BuildingDetails findByQuoteNoAndRiskId(String quoteNo, Integer valueOf);

	List<BuildingDetails> findByQuoteNo(String quoteNo);

	Long countByQuoteNoAndRiskId(String quoteNo, Integer valueOf);

	@Transactional
	void deleteByQuoteNoAndRiskId(String quoteNo, Integer valueOf);

	List<BuildingDetails> findByQuoteNoAndSectionId(String quoteNo, String sectionId);

	List<BuildingDetails> findByRequestReferenceNo(String requestno);




//	BuildingDetails findByRequestReferenceNoAndSectionId(String requestReferenceNo, String sectionId);

}
