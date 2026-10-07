package com.maan.eway.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maan.eway.bean.AdditionalInformation;

public interface AdditionalInformationRepository extends JpaRepository<AdditionalInformation, Long> {

	 List<AdditionalInformation> findByQuoteNoAndRequestReferenceNo(String quoteNo, String requestReferenceNo);

	 void deleteByQuoteNoAndRequestReferenceNoAndCompanyIdAndProductIdAndSectionIdAndCoverId(
            String quoteno, String requestReferenceNo, String companyId,
             String productId, String sectionId, String coverId);

	 List<AdditionalInformation> findByQuoteNoAndContentIdIn(String quoteNo, Set<Long> content);

	 void deleteByEndtReqRefNoAndContentIdIn(String endtReqRefNo, Set<Long> content);

	

	 List<AdditionalInformation> findByQuoteNoAndContentIdNotIn(String quoteNo, Set<Long> content);

	 List<AdditionalInformation> findByQuoteNoAndStatus(String quoteNo, String string);

}
