package com.maan.eway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.eway.bean.DamagePartsDetails;
import com.maan.eway.bean.DamagePartsDetailsId;
import java.util.List;


public interface DamagePartsDetailsRepository extends JpaRepository<DamagePartsDetails,DamagePartsDetailsId > , JpaSpecificationExecutor<DamagePartsDetails>{
	
	
	List<DamagePartsDetails> findByQuoteNoAndUniqueIdAndDocumentId(String quoteNo, String uniqueId, Integer documentId);
	
	List<DamagePartsDetails> findByQuoteNoAndUniqueId(String quoteNo, String uniqueId);
	
	
	 DamagePartsDetails findTopByOrderByDamageIdDesc();

}
