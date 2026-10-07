package com.maan.eway.common.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maan.eway.bean.CompanyProductMaster;
import com.maan.eway.bean.DocumentTransactionDetails;
import com.maan.eway.bean.DocumentUniqueDetails;
import com.maan.eway.bean.EndtTypeMaster;
import com.maan.eway.bean.HomePositionMaster;
import com.maan.eway.bean.ListItemValue;
import com.maan.eway.bean.MotorDataDetails;
import com.maan.eway.bean.ProductEmployeeDetails;
import com.maan.eway.bean.SectionDataDetails;
import com.maan.eway.bean.TravelPassengerDetails;
import com.maan.eway.common.req.FrameOldDocSaveReq;
import com.maan.eway.common.req.OldDocumentsCopyReq;
import com.maan.eway.common.service.DocumentCopyService;
import com.maan.eway.repository.DocumentTransactionDetailsRepository;
import com.maan.eway.repository.DocumentUniqueDetailsRepository;
import com.maan.eway.repository.EndtTypeMasterRepository;
import com.maan.eway.repository.HomePositionMasterRepository;
import com.maan.eway.repository.MotorDataDetailsRepository;
import com.maan.eway.repository.ProductEmployeesDetailsRepository;
import com.maan.eway.repository.SectionDataDetailsRepository;
import com.maan.eway.repository.TravelPassengerDetailsRepository;
import com.maan.eway.res.SuccessRes;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

@Service
public class DocumentCopySeviceImpl implements DocumentCopyService {
	
	private Logger log = LogManager.getLogger(DocumentCopySeviceImpl.class);
	
	@Autowired
	private HomePositionMasterRepository homeRepo ;
	
	@Autowired
	private SectionDataDetailsRepository secDataRepo ;
	
	@Autowired
	private TravelPassengerDetailsRepository passRepo ;
	
	@Autowired
	private ProductEmployeesDetailsRepository empRepo ;
	
	@Autowired
	private MotorDataDetailsRepository motRepo ;
	
	@Autowired
	private DocumentUniqueDetailsRepository docUniqueRepo ;
	
	@Autowired
	private DocumentTransactionDetailsRepository docTranRepo ;
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private EndtTypeMasterRepository endtTypeRepo;
	
	@Value(value = "${travel.productId}")
	private String travelProductId;
	
	@Override
	@Transactional
	public SuccessRes copyOldDocumentsToNewQuote(OldDocumentsCopyReq req) {
		// TODO Auto-generated method stub
		SuccessRes res = new SuccessRes();
		try {
			
			CompanyProductMaster product =  getCompanyProductMasterDropdown(req.getInsuranceId() , req.getProductId());
			HomePositionMaster homeData = homeRepo.findByQuoteNo(req.getQuoteNo());
			List<ListItemValue> docTypeList = getListItem( "99999" , homeData.getBranchCode() , "DOC_ID_TYPE");
			String idType = "" ; 
			List<FrameOldDocSaveReq> frameReqList = new ArrayList<FrameOldDocSaveReq>(); 
			
			if ( product.getMotorYn().equalsIgnoreCase("H") &&  product.getProductId().toString().equalsIgnoreCase(travelProductId)  ) {
				// Travel
				idType = docTypeList.stream().filter( o -> o.getItemCode().equalsIgnoreCase("T") ).collect(Collectors.toList()).get(0).getItemValue() ;	
				frameReqList = frameTravelDocRequest(homeData , req.getSectionId() );
						
				
			} else if ( product.getMotorYn().equalsIgnoreCase("M") ) {
				// Motor 
				idType = docTypeList.stream().filter( o -> o.getItemCode().equalsIgnoreCase("M") ).collect(Collectors.toList()).get(0).getItemValue() ;	
				frameReqList = frameMotorDocRequest(homeData );
				
			} else if ( product.getMotorYn().equalsIgnoreCase("A") ) {
				// Asset
				idType = docTypeList.stream().filter( o -> o.getItemCode().equalsIgnoreCase("H") ).collect(Collectors.toList()).get(0).getItemValue() ;
				frameReqList = frameDomesticDocRequest(homeData , req.getSectionId()  );
				
			} else  {
				// Human
				idType = docTypeList.stream().filter( o -> o.getItemCode().equalsIgnoreCase("H") ).collect(Collectors.toList()).get(0).getItemValue() ;	
				frameReqList =  frameHumanDocRequest(homeData , req.getSectionId()  );
				
			}
			
			// Save Old Documents in New Quote 
			saveDocumentsNewQuote(homeData , idType , frameReqList );
			
			res.setResponse("Old Documents Copied Successfully");
			res.setSuccessId("1");
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Log Details" + e.getMessage());
			res.setResponse("Old Documents Copy Fialed");
			res.setSuccessId("1");
			return null;
		}
	
		return res;
	}
	
	public List<FrameOldDocSaveReq> frameMotorDocRequest( HomePositionMaster homeData ) {
		List<FrameOldDocSaveReq> reqList = new ArrayList<FrameOldDocSaveReq>();
		try {
			List<MotorDataDetails> motList = motRepo.findByQuoteNo(homeData.getQuoteNo());
			List<SectionDataDetails> secDatas =  secDataRepo.findByQuoteNoOrderByRiskIdAsc(homeData.getQuoteNo());
			
			// Frame Req 
			motList.forEach(  mot -> { 
				FrameOldDocSaveReq saveReq  = new FrameOldDocSaveReq();
				List<SectionDataDetails> filterSec = secDatas.stream().filter(  o -> o.getSectionId().equalsIgnoreCase(mot.getSectionId().toString())  ).collect(Collectors.toList());
				SectionDataDetails section = filterSec.get(0) ;
				 
				saveReq.setRiskId(mot.getVehicleId());
				saveReq.setId(mot.getChassisNumber());
				saveReq.setLocationId("1");
				saveReq.setLocationName(homeData.getProductName());
				saveReq.setSectionId(section.getSectionId()) ;
				saveReq.setSectionName(section.getSectionDesc());
				reqList.add(saveReq);
				
			} ) ;
						
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Log Details" + e.getMessage());
			return null;
		}
	
		return reqList ;
	}
	
	public List<FrameOldDocSaveReq> frameTravelDocRequest( HomePositionMaster homeData , String sectioId ) {
		List<FrameOldDocSaveReq> reqList = new ArrayList<FrameOldDocSaveReq>();
		try {
			List<TravelPassengerDetails> passList = passRepo.findByQuoteNoAndStatusNotOrderByPassengerIdAsc(homeData.getQuoteNo() , "D");
			List<SectionDataDetails> secDatas =  secDataRepo.findByQuoteNoOrderByRiskIdAsc(homeData.getQuoteNo());
			List<SectionDataDetails> filterSec = secDatas.stream().filter(  o -> o.getSectionId().equalsIgnoreCase(sectioId)  ).collect(Collectors.toList());
			SectionDataDetails section = filterSec.get(0) ;
			
			// Frame Req 
			passList.forEach(  pass -> { 
				FrameOldDocSaveReq saveReq  = new FrameOldDocSaveReq();
				 
				saveReq.setRiskId(pass.getPassengerId().toString());
				saveReq.setId(pass.getPassportNo());
				saveReq.setLocationId("1");
				saveReq.setLocationName(homeData.getProductName());
				saveReq.setSectionId(section.getSectionId()) ;
				saveReq.setSectionName(section.getSectionDesc());
				reqList.add(saveReq);
				
			} ) ;
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Log Details" + e.getMessage());
			return null;
		}
	
		return reqList ;
	}
	
	public List<FrameOldDocSaveReq> frameDomesticDocRequest( HomePositionMaster homeData , String sectioId ) {
		List<FrameOldDocSaveReq> reqList = new ArrayList<FrameOldDocSaveReq>();
		try {
			List<ProductEmployeeDetails> empList = empRepo.findByQuoteNoAndStatusNotAndSectionId(homeData.getQuoteNo(),"D" , sectioId);
			List<SectionDataDetails> secDatas =  secDataRepo.findByQuoteNoOrderByRiskIdAsc(homeData.getQuoteNo());
			List<SectionDataDetails> filterSec = secDatas.stream().filter(  o -> o.getSectionId().equalsIgnoreCase(sectioId)  ).collect(Collectors.toList());
			SectionDataDetails section = filterSec.get(0) ;
			
			// Frame Req 
			empList.forEach(  emp -> { 
				FrameOldDocSaveReq saveReq  = new FrameOldDocSaveReq();
				 
				saveReq.setRiskId(emp.getEmployeeId().toString());
				saveReq.setId(emp.getNationalityId());
				saveReq.setLocationId("1");
				saveReq.setLocationName(emp.getLocationName());
				saveReq.setSectionId(section.getSectionId()) ;
				saveReq.setSectionName(section.getSectionDesc());
				reqList.add(saveReq);
				
			} ) ;
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Log Details" + e.getMessage());
			return null;
		}
	
		return reqList ;
	}
	
	public List<FrameOldDocSaveReq> frameHumanDocRequest( HomePositionMaster homeData , String sectionId   ) {
		List<FrameOldDocSaveReq> reqList = new ArrayList<FrameOldDocSaveReq>();
		try {
			List<ProductEmployeeDetails> empList = empRepo.findByQuoteNoAndStatusNotAndSectionId(homeData.getQuoteNo(), "D" ,sectionId);
			List<SectionDataDetails> secDatas =  secDataRepo.findByQuoteNoOrderByRiskIdAsc(homeData.getQuoteNo());
			List<SectionDataDetails> filterSec = secDatas.stream().filter(  o -> o.getSectionId().equalsIgnoreCase(sectionId)  ).collect(Collectors.toList());
			SectionDataDetails section = filterSec.get(0) ;
			
			// Frame Req 
			empList.forEach(  emp -> { 
				FrameOldDocSaveReq saveReq  = new FrameOldDocSaveReq();
				 
				saveReq.setRiskId(emp.getEmployeeId().toString());
				saveReq.setId(emp.getNationalityId());
				saveReq.setLocationId("1");
				saveReq.setLocationName(emp.getLocationName());
				saveReq.setSectionId(section.getSectionId()) ;
				saveReq.setSectionName(section.getSectionDesc());
				reqList.add(saveReq);
				
			} ) ;
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Log Details" + e.getMessage());
			return null;
		}
	
		return reqList ;
	}
	
	public SuccessRes saveDocumentsNewQuote( HomePositionMaster homeData , String idType , List<FrameOldDocSaveReq> framendReqList  ) {
		// TODO Auto-generated method stub
		SuccessRes res = new SuccessRes();
		try {
			Long  docTranCount = docTranRepo.countByQuoteNo(homeData.getQuoteNo());
			
			if(docTranCount <= 0 ) {
				int targetSize = 500;
				List<FrameOldDocSaveReq> largeList = framendReqList ;
				List<List<FrameOldDocSaveReq>> partitionList = ListUtils.partition(largeList, targetSize);
				
				for (List<FrameOldDocSaveReq> partitionIds :  partitionList ) {
					
					List<String> ids = partitionIds.stream().map( FrameOldDocSaveReq :: getId ).collect(Collectors.toList());
					List<DocumentUniqueDetails> uniqueDatas = docUniqueRepo.findByIdTypeAndIdInOrderByEntryDateDesc(idType , ids);
					uniqueDatas = uniqueDatas.stream().filter(distinctByKey(o -> Arrays.asList(o.getId()))).collect(Collectors.toList());
					
					List<DocumentTransactionDetails> saveDocList = new ArrayList<DocumentTransactionDetails>();
					uniqueDatas.forEach ( uniq ->  {  
						
						DocumentTransactionDetails docTran = new DocumentTransactionDetails();
						docTran.setUniqueId( uniq.getUniqueId());
						docTran.setId(uniq.getId());
						docTran.setIdType(idType);
						docTran.setRequestReferenceNo(homeData.getRequestReferenceNo());
						docTran.setQuoteNo(homeData.getQuoteNo());
						docTran.setCompanyId(homeData.getCompanyId());
						docTran.setCompanyName(homeData.getCompanyName());
						docTran.setProductId(homeData.getProductId());
						docTran.setProductName(homeData.getProductName());
						docTran.setEntryDate(new Date());
						docTran.setCreatedBy(homeData.getLoginId());
						docTran.setStatus("Y");
						
						List<FrameOldDocSaveReq> filterPartitions = partitionIds.stream().filter(  o -> o.getId().equalsIgnoreCase(uniq.getId())  ).collect(Collectors.toList());
						if ( filterPartitions.size() > 0 ) {
							FrameOldDocSaveReq partition = filterPartitions.get(0);
							docTran.setSectionId(Integer.valueOf(partition.getSectionId()));
							docTran.setSectionName(partition.getSectionName());
							docTran.setProductType(uniq.getProductType());
							docTran.setLocationId(Integer.valueOf(partition.getLocationId()) );
							docTran.setLocationName(partition.getLocationName());
							docTran.setRiskId(Integer.valueOf(partition.getRiskId()));
						
						}
						
						if (StringUtils.isNotBlank(homeData.getEndtTypeId())) {
							EndtTypeMaster entMaster=endtTypeRepo.findByCompanyIdAndProductIdAndStatusAndEndtTypeId(homeData.getCompanyId(), homeData.getProductId(), "Y",Integer.parseInt(homeData.getEndtTypeId()));
							if (entMaster != null) {
								docTran.setEndorsementDate(homeData.getEndtDate() == null ? null : new Date());
								docTran.setEndorsementEffdate(homeData.getEndorsementEffdate() == null ? null : homeData.getEndorsementEffdate());
								docTran.setEndorsementRemarks(homeData.getEndorsementRemarks() == null ? "" : homeData.getEndorsementRemarks());
								docTran.setEndorsementTypeDesc(entMaster.getEndtTypeDesc());
								docTran.setIsFinaceYn(entMaster.getEndtTypeCategoryId()==2?"Y":"N");
								docTran.setEndtCategDesc(entMaster.getEndtTypeCategory());
								docTran.setEndtStatus(homeData.getEndtStatus());
								docTran.setEndtCount(new BigDecimal(homeData.getEndtCount()));
								docTran.setEndtPrevPolicyNo(homeData.getEndtPrevPolicyNo());
								docTran.setEndtPrevQuoteNo(homeData.getEndtPrevQuoteNo());
							}
						}
						
						saveDocList.add(docTran);
						
					} );
					
					docTranRepo.saveAllAndFlush(saveDocList);
				}
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Log Details" + e.getMessage());
			return null;
		}
	
		return res;
	}
	
	private static <T> java.util.function.Predicate<T> distinctByKey(java.util.function.Function<? super T, ?> keyExtractor) {
	    Map<Object, Boolean> seen = new ConcurrentHashMap<>();
	    return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}
	
	public synchronized List<ListItemValue>  getListItem(String insuranceId, String branchCode, String itemType) {
		List<ListItemValue> list = new ArrayList<ListItemValue>();
		try {
			Date today = new Date();
			Calendar cal = new GregorianCalendar();
			cal.setTime(today);
			today = cal.getTime();
			Date todayEnd = cal.getTime();

			// Criteria
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<ListItemValue> query = cb.createQuery(ListItemValue.class);
			// Find All
			Root<ListItemValue> c = query.from(ListItemValue.class);

			// Select
			query.select(c);
			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(c.get("branchCode")));

			// Effective Date Start Max Filter
			Subquery<Date> effectiveDate = query.subquery(Date.class);
			Root<ListItemValue> ocpm1 = effectiveDate.from(ListItemValue.class);
			effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
			Predicate a1 = cb.equal(c.get("itemId"), ocpm1.get("itemId"));
			Predicate a5 = cb.equal(c.get("branchCode"), ocpm1.get("branchCode"));
			Predicate a6 = cb.equal(c.get("companyId"), ocpm1.get("companyId"));
			Predicate a2 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
			effectiveDate.where(a1, a2,a5,a6);
			// Effective Date End Max Filter
			Subquery<Date> effectiveDate2 = query.subquery(Date.class);
			Root<ListItemValue> ocpm2 = effectiveDate2.from(ListItemValue.class);
			effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
			Predicate a3 = cb.equal(c.get("itemId"), ocpm2.get("itemId"));
			Predicate a4 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
			Predicate a7 = cb.equal(c.get("companyId"), ocpm2.get("companyId"));
			Predicate a8 = cb.equal(c.get("branchCode"), ocpm2.get("branchCode"));
			effectiveDate2.where(a3, a4,a7,a8);

			// Where
			Predicate n1 = cb.equal(c.get("status"),"Y");
			Predicate n12 = cb.equal(c.get("status"),"R");
			Predicate n13 = cb.or(n1,n12);
			Predicate n2 = cb.equal(c.get("effectiveDateStart"), effectiveDate);
			Predicate n3 = cb.equal(c.get("effectiveDateEnd"), effectiveDate2);
			Predicate n4 = cb.equal(c.get("companyId"), insuranceId);
		//	Predicate n5 = cb.equal(c.get("companyId"), "99999");
			Predicate n6 = cb.equal(c.get("branchCode"), branchCode);
			Predicate n7 = cb.equal(c.get("branchCode"), "99999");
		//	Predicate n8 = cb.or(n4, n5);
			Predicate n9 = cb.or(n6, n7);
			Predicate n10 = cb.equal(c.get("itemType"), itemType);
		//	Predicate n11 = cb.equal(c.get("itemCode"), itemCode);
			query.where(n13, n2, n3, n4, n9, n10).orderBy(orderList);
			// Get Result
			TypedQuery<ListItemValue> result = em.createQuery(query);
			list = result.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null;
		}
		return list;
	}
	
	public synchronized CompanyProductMaster getCompanyProductMasterDropdown(String companyId, String productId) {
		CompanyProductMaster product = new CompanyProductMaster();
		try {
			Date today = new Date();
			Calendar cal = new GregorianCalendar();
			cal.setTime(today);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			;
			cal.set(Calendar.MINUTE, 1);
			today = cal.getTime();
			cal.set(Calendar.HOUR_OF_DAY, 1);
			cal.set(Calendar.MINUTE, 1);
			Date todayEnd = cal.getTime();

			// Criteria
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<CompanyProductMaster> query = cb.createQuery(CompanyProductMaster.class);
			List<CompanyProductMaster> list = new ArrayList<CompanyProductMaster>();
			// Find All
			Root<CompanyProductMaster> c = query.from(CompanyProductMaster.class);
			// Select
			query.select(c);
			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(c.get("productName")));

			// Effective Date Start Max Filter
			Subquery<Date> effectiveDate = query.subquery(Date.class);
			Root<CompanyProductMaster> ocpm1 = effectiveDate.from(CompanyProductMaster.class);
			effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
			Predicate a1 = cb.equal(c.get("productId"), ocpm1.get("productId"));
			Predicate a2 = cb.equal(c.get("companyId"), ocpm1.get("companyId"));
			Predicate a3 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
			effectiveDate.where(a1, a2, a3);
			// Effective Date End Max Filter
			Subquery<Date> effectiveDate2 = query.subquery(Date.class);
			Root<CompanyProductMaster> ocpm2 = effectiveDate2.from(CompanyProductMaster.class);
			effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
			Predicate a4 = cb.equal(c.get("productId"), ocpm2.get("productId"));
			Predicate a5 = cb.equal(c.get("companyId"), ocpm2.get("companyId"));
			Predicate a6 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
			effectiveDate2.where(a4, a5, a6);

			// Where
			Predicate n1 = cb.equal(c.get("status"), "Y");
			Predicate n2 = cb.equal(c.get("effectiveDateStart"), effectiveDate);
			Predicate n3 = cb.equal(c.get("effectiveDateEnd"), effectiveDate2);
			Predicate n4 = cb.equal(c.get("companyId"), companyId);
			Predicate n5 = cb.equal(c.get("productId"), productId);
			query.where(n1, n2, n3, n4, n5).orderBy(orderList);
			// Get Result
			TypedQuery<CompanyProductMaster> result = em.createQuery(query);
			list = result.getResultList();
			product = list.size() > 0 ? list.get(0) :null;
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is --->" + e.getMessage());
			return null;
		}
		return product;
	}
	
	
}
