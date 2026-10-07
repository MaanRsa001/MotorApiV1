package com.maan.eway.repository;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.eway.bean.ProductEmployeeDetails;
import com.maan.eway.bean.ProductEmployeeDetailsId;

public interface ProductEmployeesDetailsRepository extends JpaRepository<ProductEmployeeDetails, ProductEmployeeDetailsId>, JpaSpecificationExecutor<ProductEmployeeDetails> {

	Long countByQuoteNoAndRiskId(String quoteNo, Integer valueOf);

	@Transactional
	void deleteByQuoteNoAndRiskId(String quoteNo, Integer valueOf);

	List<ProductEmployeeDetails> findByQuoteNo(String quoteNo);
	
	List<ProductEmployeeDetails> findByRequestReferenceNo(String reqno);
	

	List<ProductEmployeeDetails> findByQuoteNoAndRiskIdOrderByEmployeeIdAsc(String quoteNo, Integer valueOf);

	Long countByQuoteNoAndRiskIdAndEmployeeId(String quoteNo, Integer valueOf, Long valueOf2);

	@Transactional
	void deleteByQuoteNoAndRiskIdAndEmployeeId(String quoteNo, Integer valueOf, Long valueOf2);

	List<ProductEmployeeDetails> findByQuoteNoAndRiskIdAndSectionIdOrderByEmployeeIdAsc(String quoteNo, Integer valueOf,
			String sectionId);

	Long countByQuoteNoAndRiskIdAndEmployeeIdAndSectionId(String quoteNo, Integer valueOf, Long valueOf2,
			String sectionId);

	@Transactional
	void deleteByQuoteNoAndRiskIdAndEmployeeIdAndSectionId(String quoteNo, Integer valueOf, Long valueOf2,
			String sectionId);

	Long countByQuoteNoAndRiskIdAndSectionId(String quoteNo, Integer riskId, String sectionId);

	@Transactional
	void deleteByQuoteNoAndRiskIdAndSectionId(String quoteNo, Integer riskId, String sectionId);

	List<ProductEmployeeDetails> findByQuoteNoAndRiskIdAndSectionIdAndEmployeeId(String quoteNo, Integer valueOf,
			String sectionId, Long valueOf2);

	List<ProductEmployeeDetails> findByQuoteNoAndRiskIdAndSectionId(String quoteNo, Integer valueOf, String sectionId);

	List<ProductEmployeeDetails> findByQuoteNoAndSectionId(String quoteNo, String sectionId);

	@Transactional
	void deleteByQuoteNoAndSectionId(String quoteNo, String sectionId);

	List<ProductEmployeeDetails> findByQuoteNoAndSectionIdOrderByEmployeeIdAsc(String quoteNo, String sectionId);

	List<ProductEmployeeDetails> findByQuoteNoAndSectionIdAndEmployeeId(String quoteNo, String sectionId, Long valueOf);

	@Transactional
	void deleteByQuoteNoAndEmployeeIdAndSectionId(String quoteNo, Long valueOf, String sectionId);

	ProductEmployeeDetails findByQuoteNoAndRiskIdAndEmployeeIdAndSectionId(String quoteNo, Integer valueOf, Long valueOf2,
			String sectionId);

	List<ProductEmployeeDetails> findByQuoteNoAndSectionIdAndProductIdAndCompanyId(String quoteNo, String sectionId,
			Integer productId, String companyId);

	List<ProductEmployeeDetails> findByRequestReferenceNoAndSectionId(String requestReferenceNo, String sec);

	List<ProductEmployeeDetails> findByQuoteNoAndLocationIdNotIn(String quoteNo, List<Integer> locIds);

	@Transactional
	void deleteByQuoteNoAndLocationIdNotIn(String quoteNo, List<Integer> locIds);

	Long countByQuoteNoAndLocationIdAndSectionIdAndNationalityIdNotIn(String quoteNo, Integer valueOf, String valueOf2,
			List<String> nationalityIds);

	@Transactional
	void deleteByQuoteNoAndLocationIdAndSectionIdAndNationalityIdNotIn(String quoteNo, Integer valueOf,
			String valueOf2, List<String> nationalityIds);

	List<ProductEmployeeDetails> findByQuoteNoAndStatusNotAndSectionId(String quoteNo, String string, String sectioId);

	Object countByQuoteNo(String quoteNo);

	Long countByQuoteNoAndEmployeeIdInAndStatusNot(String quoteNo, List<Long> passengerId2, String string);

	void deleteByQuoteNoAndEmployeeIdInAndStatusNot(String quoteNo, List<Long> passengerId2, String string);

	List<ProductEmployeeDetails> findByQuoteNoAndSectionIdAndEmployeeIdAndStatusNot(String quoteNo, String sectionId,
			Long valueOf, String string);

	List<ProductEmployeeDetails> findByQuoteNoAndSectionIdAndStatusNot(String quoteNo, String sectionId, String string);

	List<ProductEmployeeDetails> findByRequestReferenceNoAndSectionIdAndStatusNot(String req_no, String sectionId, String string);
	
	List<ProductEmployeeDetails> findByQuoteNoAndSectionIdAndStatusNotOrderByEmployeeIdAsc(String quoteNo,
			String sectionId, String string);

	List<ProductEmployeeDetails> findByQuoteNoAndSectionIdAndStatusOrderByEmployeeIdAsc(String quoteNo,
			String sectionId, String string);

	Long countByQuoteNoAndSectionIdAndEmployeeIdInAndStatusNot(String quoteNo, String string, List<Long> passengerId2,
			String string2);

	void deleteByQuoteNoAndSectionIdAndEmployeeIdInAndStatusNot(String quoteNo, String string, List<Long> passengerId2,
			String string2);

	Long countByQuoteNoAndSectionIdAndRequestReferenceNo(String quoteNo, String string, String requestReferenceNo);

	List<ProductEmployeeDetails> findByRequestReferenceNoAndLocationIdNotIn(String requestno, List<Integer> locIds);

	void deleteByRequestReferenceNoAndLocationIdNotIn(String requestno, List<Integer> locIds);

	List<ProductEmployeeDetails> findAllByRequestReferenceNoAndSectionId(String requestReferenceNo, String sectionId);

	List<ProductEmployeeDetails> findAllByRequestReferenceNo(String requestReferenceNo);

	
}
