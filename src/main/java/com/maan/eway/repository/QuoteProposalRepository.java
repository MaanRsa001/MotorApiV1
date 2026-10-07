/**
 * @author : Ashok Kumar S 
 * @since  : 23-12-2024
 */
package com.maan.eway.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maan.eway.bean.QuoteProposal;
import com.maan.eway.bean.QuoteProposalPK;



@Repository
public interface QuoteProposalRepository extends JpaRepository<QuoteProposal, QuoteProposalPK>{
	
	public QuoteProposal findTopByCompanyIdAndProductIdOrderByProposalIdDesc(Integer companyId, Integer productId);
	
	public Optional<QuoteProposal> findByCompanyIdAndProductIdAndProposalId(Integer companyId, Integer productId, Long proposalId);
	
	public List<QuoteProposal> findAllByCompanyIdAndProductId(Integer companyId, Integer productId);
	
	public List<QuoteProposal> findAllByCompanyIdAndProductIdAndProposalStatus(
			Integer companyId, Integer productId, String proposalStatus);
	
	public Optional<QuoteProposal> findByCompanyIdAndProductIdAndCustomerReferenceNoAndRequestReferenceNoAndQuoteNo(
			Integer companyId, Integer productId, String customerReferenceNo, 
			String requestReferenceNo, String quoteNo);

}
