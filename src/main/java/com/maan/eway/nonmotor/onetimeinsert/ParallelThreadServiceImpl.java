package com.maan.eway.nonmotor.onetimeinsert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.maan.eway.bean.EserviceCommonDetails;
import com.maan.eway.bean.EserviceCustomerDetails;
import com.maan.eway.bean.MsCustomerDetails;
import com.maan.eway.bean.MsHumanDetails;
import com.maan.eway.bean.SeqOnetimetable;
import com.maan.eway.repository.EserviceCustomerDetailsRepository;
import com.maan.eway.repository.MsCustomerDetailsRepository;
import com.maan.eway.repository.MsHumanDetailsRepository;
import com.maan.eway.repository.SeqOnetimetableRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;


@Component
public class ParallelThreadServiceImpl {
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private  SeqOnetimetableRepository oneNoRepo ;
	
	@Autowired
	private MsHumanDetailsRepository msHumanRepository;
	
	@Autowired
	private EserviceCustomerDetailsRepository eserviceCustomerRepo ;
	
	@Autowired
	private MsCustomerDetailsRepository msCustomerRepo;
	
	
	
	
	@Async("ONETIME_TABLE_INSERT")
	public CompletableFuture<List<Map<String,Object>>> personal_accident_insert(List<EserviceCommonDetails>  data){
		List<Map<String,Object>> response_data = new ArrayList<>();
		try {
			String vdRefNo ="";
			for(EserviceCommonDetails commonData : data) {
				
				String sectionId = commonData.getSectionId() ;
				String branchCode = commonData.getBranchCode();
				String agencyCode =  commonData.getBrokerCode();
				String productId =  commonData.getProductId();
				String companyId =  commonData.getCompanyId();
								
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<MsHumanDetails> query = cb.createQuery(MsHumanDetails.class);

				// Find All
				Root<MsHumanDetails> b = query.from(MsHumanDetails.class);

				// Select
				query.select(b);
				Predicate n1 = (cb.like(cb.lower(b.get("requestReferenceNo")), commonData.getRequestReferenceNo().toString().toLowerCase()));		
				Predicate n4 = (cb.like(cb.lower(b.get("createdBy")), commonData.getCreatedBy().toString().toLowerCase()));		
				Predicate n5 = (cb.equal(b.get("humanId"), commonData.getRiskId()));		
				Predicate n8 = (cb.like(cb.lower(b.get("status")), commonData.getStatus().toString().toLowerCase()));		
				Predicate n9 = (cb.equal(b.get("periodOfInsurance"), commonData.getPolicyPeriod()));		
				Predicate n10 = (cb.like(cb.lower(b.get("currency")), commonData.getCurrency().toString().toLowerCase()));		
				Predicate n11 = (cb.equal(b.get("exchangeRate"), commonData.getExchangeRate()));		
				Predicate n12 = (cb.equal(b.get("sumInsured"), commonData.getSumInsured()));		
				Predicate n13 = (cb.equal(b.get("categoryId"), commonData.getCategoryId()));		
				Predicate n14 = cb.equal(cb.lower(b.get("havepromocode")), commonData.getHavepromocode().toString().toLowerCase());
				Predicate n15 = cb.equal(cb.lower(b.get("promocode")), commonData.getPromocode()==null?"" : commonData.getPromocode().toString().toLowerCase());
					
				query.where(n1,n4,n5,n8,n9,n10,n11,n12,n13,n14,n15);					
				// Get Result
				TypedQuery<MsHumanDetails> result = em.createQuery(query);
				List<MsHumanDetails> list = result.getResultList();		
				
				if(list.size()>0) {
					
					vdRefNo = String.format("%05d",list.get(0).getVdRefno());

				}else {
					vdRefNo = genOneTimeTableRefNo();
					
					MsHumanDetails pa = MsHumanDetails.builder()
							.requestReferenceNo(commonData.getRequestReferenceNo())
							.humanId(commonData.getRiskId())
							.vdRefno(Long.valueOf(vdRefNo))
							.categoryId(commonData.getCategoryId())
							.currency(commonData.getCurrency())
							.exchangeRate(commonData.getExchangeRate())
							.sumInsured(commonData.getSalaryPerAnnum())
							.entryDate(new Date())
							.createdBy(commonData.getCreatedBy())
							.havepromocode(commonData.getHavepromocode())
							.promocode(StringUtils.isBlank(commonData.getPromocode())?null:commonData.getPromocode())
							.groupId(0)
							.groupCount(1)
							.periodOfInsurance(commonData.getPolicyPeriod())
							.categoryId(commonData.getCategoryId())
							.endtTypeId(commonData.getEndorsementType()==null?0:commonData.getEndorsementType())
							.endtCategoryId(commonData.getIsFinyn()==null?"N" :commonData.getIsFinyn())
							.sumInsured(commonData.getSalaryPerAnnum())
							.insuranceClass("99999")
							.uwLoading(BigDecimal.ZERO)
							.status(commonData.getStatus())
							.build();
					
					
					msHumanRepository.save(pa);
					
					
					Map<String,Object> map = new HashMap<>();					
					map.put("SectionId", sectionId);
					map.put("BranchCode", branchCode);
					map.put("AgencyCode", agencyCode);
					map.put("ProductId", productId);
					map.put("CompanyId", companyId);
					map.put("vdRefNo", vdRefNo);
					map.put("RiskId", commonData.getRiskId());

					response_data.add(map);
				}

			}
			
			return  CompletableFuture.completedFuture(response_data);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return CompletableFuture.completedFuture(response_data);
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	 public  String genOneTimeTableRefNo() {
	       try {
	    	   SeqOnetimetable entity;
	            entity = oneNoRepo.save(new SeqOnetimetable());          
	            return String.format("%05d",entity.getReferenceNo()) ;
	        } catch (Exception e) {
				e.printStackTrace();
	            return null;
	        }
	       
	 }
	 
	 @Async("ONETIME_TABLE_INSERT")
		public CompletableFuture<MsCustomerDetails> ms_customer_insert(String customer_ref_no){
			try {
				
				EserviceCustomerDetails custData = eserviceCustomerRepo.findByCustomerReferenceNo(customer_ref_no);
				
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<MsCustomerDetails> query = cb.createQuery(MsCustomerDetails.class);
				Root<MsCustomerDetails> b = query.from(MsCustomerDetails.class);
				query.select(b);
				Predicate n1 = cb.equal(cb.lower(b.get("policyHolderTypeid")),custData.getPolicyHolderTypeid().toLowerCase());
				Predicate n2 = cb.equal(cb.lower(b.get("policyHolderType")),custData.getPolicyHolderType().toLowerCase());
				Predicate n3 = cb.equal(b.get("age"),custData.getAge());
				Predicate n4 = cb.equal(cb.lower(b.get("gender")),custData.getGender().toLowerCase());
				Predicate n5 = cb.equal(cb.lower(b.get("occupation")),custData.getOccupation()==null?null:custData.getOccupation().toLowerCase());
				Predicate n7 = cb.equal(cb.lower(b.get("regionCode")),custData.getRegionCode()==null?"NA":custData.getRegionCode().toLowerCase());
			//	Predicate n8 = cb.equal(b.get("cityCode"),custData.getCityCode());
				Predicate n9 = cb.equal(b.get("taxExemptedId"),custData.getTaxExemptedId());
				Predicate n10 = cb.equal(cb.lower(b.get("status")),custData.getStatus().toLowerCase());
				Predicate n11 = cb.equal(cb.lower(b.get("idNumber")),custData.getIdNumber().toLowerCase());

				query.where(n1,n2,n3,n4,n5,n7,n9,n10,n11);					

				TypedQuery<MsCustomerDetails> result = em.createQuery(query);
				List<MsCustomerDetails> list  =  result.getResultList(); 
		
				
				String cd_ref_no="";
				
				if(list.size()>0) {
					cd_ref_no =String.format("%05d", list.get(0).getCdRefno()) ;
				}else {
					
					cd_ref_no =  genOneTimeTableRefNo() ;  			
					
					MsCustomerDetails customerDetails = MsCustomerDetails.builder()
							.policyHolderType(StringUtils.isBlank(custData.getPolicyHolderType())?null:custData.getPolicyHolderType())
							.policyHolderTypeid(StringUtils.isBlank(custData.getPolicyHolderTypeid())?null:custData.getPolicyHolderTypeid())
							.idType(StringUtils.isBlank(custData.getIdType())?null:custData.getIdType())
							.idTypeDesc(StringUtils.isBlank(custData.getIdTypeDesc())?null:custData.getIdTypeDesc())
							.idNumber(custData.getIdNumber()==null?null:custData.getIdNumber())
							.dobOrRegDate(custData.getDobOrRegDate()==null?null:custData.getDobOrRegDate())
							.age(custData.getAge()==null?null:custData.getAge())
							.nationality(StringUtils.isBlank(custData.getNationality())?null:custData.getNationality())
							.placeOfBirth(StringUtils.isBlank(custData.getPlaceOfBirth())?null:custData.getPlaceOfBirth())
							.gender(StringUtils.isBlank(custData.getGender())?null:custData.getGender())
							.genderDesc(StringUtils.isBlank(custData.getGenderDesc())?null:custData.getGenderDesc())
							.occupation(StringUtils.isBlank(custData.getOccupation())?null:custData.getOccupation())
							.occupationDesc(StringUtils.isBlank(custData.getOccupationDesc())?null:custData.getOccupationDesc())
							.businessType(StringUtils.isBlank(custData.getBusinessType())?null:custData.getBusinessType())
							.businessTypeDesc(StringUtils.isBlank(custData.getBusinessTypeDesc())?null:custData.getBusinessTypeDesc())
							.regionCode(StringUtils.isBlank(custData.getRegionCode())?"NA":custData.getRegionCode())
							.stateCode(custData.getStateCode()==null?null:custData.getRegionCode())
							.stateName(StringUtils.isBlank(custData.getStateName())?null:custData.getStateName())
							.cityCode(custData.getCityCode()==null?null:custData.getCityCode().toString())
							.cityName(StringUtils.isBlank(custData.getCityName())?null:custData.getCityName())
							.isTaxExempted(StringUtils.isBlank(custData.getIsTaxExempted())?"N":custData.getIsTaxExempted())
							.taxExemptedId(StringUtils.isBlank(custData.getTaxExemptedId())?"N":custData.getTaxExemptedId())
							.entryDate(new Date())
							.status("Y")
							.createdBy(custData.getCreatedBy())
							.updatedDate(new Date())
							.cdRefno(Long.valueOf(cd_ref_no))
							.mobileCode(StringUtils.isBlank(custData.getMobileCode1())?null : custData.getMobileCode1())
							.mobileNo(StringUtils.isBlank(custData.getMobileNo1())?null : custData.getMobileNo1())
							.email(StringUtils.isBlank(custData.getEmail1())?null : custData.getEmail1())
							.licenseIssuedDate(custData.getLicenseIssuedDate()==null?null:custData.getLicenseIssuedDate())
							.licenseDuration(custData.getLicenseDuration()==null?null:custData.getLicenseDuration())
							.maritalStatus(StringUtils.isBlank(custData.getMaritalStatus())?null : custData.getMaritalStatus())
							.build();
					
					return  CompletableFuture.completedFuture(msCustomerRepo.save(customerDetails));
				}
								
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			return null;
		}

	
}
