package com.maan.eway.common.Thread;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;

import com.maan.eway.bean.CommonDataDetails;
import com.maan.eway.bean.CompanyProductMaster;
import com.maan.eway.bean.DocumentTransactionDetails;
import com.maan.eway.bean.PolicyCoverData;
import com.maan.eway.bean.ProductEmployeeDetails;
import com.maan.eway.bean.ProductEmployeeDetailsArch;
import com.maan.eway.common.req.OldDocumentsCopyReq;
import com.maan.eway.common.req.ProductEmployeeSaveReq;
import com.maan.eway.common.service.DocumentCopyService;
import com.maan.eway.common.service.impl.GenerateSeqNoServiceImpl;
import com.maan.eway.repository.CommonDataDetailsRepository;
import com.maan.eway.repository.DocumentTransactionDetailsRepository;
import com.maan.eway.repository.PolicyCoverDataRepository;
import com.maan.eway.repository.ProductEmployeeDetailsArchRepository;
import com.maan.eway.repository.ProductEmployeesDetailsRepository;
import com.maan.eway.res.SuccessRes;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;




public class Thread_insertEmpDetails implements Callable<Object>{
	private Logger log=LogManager.getLogger(Thread_insertEmpDetails.class);

	

	private CommonDataDetailsRepository commonRepo;
	private PolicyCoverDataRepository coverdataRepo;
	private ProductEmployeesDetailsRepository productRepo;
	private DocumentCopyService  docService ;
	private EntityManager em;
	private GenerateSeqNoServiceImpl genSeqNoService;
	private ProductEmployeeDetailsArchRepository empArchRepo;
	private DocumentTransactionDetailsRepository docTransRepo;
	
	private List<ProductEmployeeSaveReq> partition;
	String quoteNo;
	String sectionId;
	String createdBy;
	Integer productId;
	String refNo;
	String companyId;
	String excelUploadYN;
	String type;
	String sectionDesc;
	
	
	public Thread_insertEmpDetails(String type, List<ProductEmployeeSaveReq> partition,String quoteNo, String sectionId, String createdBy, Integer productId,
			String refNo, String companyId, String excelUploadYN,CommonDataDetailsRepository commonRepo,PolicyCoverDataRepository coverdataRepo,
			ProductEmployeesDetailsRepository productRepo,DocumentCopyService  docService,EntityManager em,
			GenerateSeqNoServiceImpl genSeqNoService,ProductEmployeeDetailsArchRepository empArchRepo, String sectionDesc,DocumentTransactionDetailsRepository docTransRepo ) {
		this.partition =partition;
		this.quoteNo =quoteNo;
		this.sectionId =sectionId;
		this.createdBy =createdBy;
		this.productId =productId;
		this.refNo =refNo;
		this.companyId =companyId;
		this.excelUploadYN =excelUploadYN;
		this.type=type;
		this.sectionDesc = sectionDesc;
		
		this.commonRepo = commonRepo;
		this.coverdataRepo = coverdataRepo;
		this.productRepo = productRepo;
		this.docService = docService;
		this.em = em;
		this.genSeqNoService = genSeqNoService;
		this.empArchRepo = empArchRepo;
		this.docTransRepo = docTransRepo ;
	}
	
	@Override
	public SuccessRes call() throws Exception {
		SuccessRes res = new SuccessRes();
		try {
			
			if(type.equalsIgnoreCase("saveempdetails1"))
				res = call_insertEmpDetails1(partition,quoteNo,sectionId,createdBy,productId,refNo,companyId,excelUploadYN,commonRepo,coverdataRepo,productRepo,docService, em,genSeqNoService,empArchRepo,sectionDesc);
			else if(type.equalsIgnoreCase("saveempdetails2"))
				res = call_insertEmpDetails2(partition,quoteNo,sectionId,createdBy,productId,refNo,companyId,excelUploadYN,commonRepo,coverdataRepo,productRepo,docService, em,genSeqNoService,empArchRepo,sectionDesc);
			else if(type.equalsIgnoreCase("deleteAllEmployees"))
				res = deleteAllEmployees(quoteNo,sectionId);
			
			res.setResponse("Saved Successfully");
			res.setSuccessId("");
		
				
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Log Details" + e.getMessage());
			return null;
		}
	
		return res;
	}
	




	private synchronized SuccessRes call_insertEmpDetails1(List<ProductEmployeeSaveReq> reqList,String quoteNo, String sectionId, String createdBy, Integer productId,
			String refNo, String companyId, String excelUploadYN,CommonDataDetailsRepository commonRepo,PolicyCoverDataRepository coverdataRepo,
			ProductEmployeesDetailsRepository productRepo,DocumentCopyService  docService,EntityManager em,
			GenerateSeqNoServiceImpl genSeqNoService,ProductEmployeeDetailsArchRepository empArchRepo,String sectionDesc ) {
		SuccessRes res = new SuccessRes();
		DozerBeanMapper dozermapper = new DozerBeanMapper();
		try {
			
			
			Date entryDate = new Date();
		
			String productName =  getCompanyProductMasterDropdown(companyId , productId.toString() );
			
			List<ProductEmployeeDetails> saveList = new ArrayList<ProductEmployeeDetails>();
			

			List<CommonDataDetails> commlist = commonRepo.findByQuoteNoAndSectionId(quoteNo, sectionId)	;	
			List<PolicyCoverData> coverdata = coverdataRepo.findByQuoteNoAndCoverageTypeAndDiscLoadIdAndTaxId(quoteNo,"B",0,0); //for rate,
			Long empId   = 0L ;
			
			for (ProductEmployeeSaveReq req : reqList ) {

				empId = empId + 1L ;
				ProductEmployeeDetails saveRes = new ProductEmployeeDetails();
				
				List<CommonDataDetails> filtercomm=commlist.stream().filter(o->o.getOccupationType().equalsIgnoreCase(req.getOccupationId())).collect(Collectors.toList());
				if(filtercomm.size()>0) {
					CommonDataDetails commdata = filtercomm.get(0);
				
				saveRes.setPolicyStartDate(commdata.getPolicyStartDate());
				saveRes.setPolicyEndDate(commdata.getPolicyEndDate());
				saveRes.setRate(coverdata.get(0).getRate().doubleValue());
				saveRes.setExchangeRate(commdata.getExchangeRate()==null?0.0:Double.valueOf(commdata.getExchangeRate().toString()));
				
				double premiumfc = Double.valueOf(req.getSalary())*coverdata.get(0).getRate().doubleValue() ;
				
				saveRes.setPremiumFc(premiumfc);
				
				saveRes.setPremiumLc(saveRes.getExchangeRate()*premiumfc); //
				saveRes.setCurrencyCode(commdata.getCurrency());
			
				}
				
				saveRes.setSectionId(sectionId);
				saveRes.setDateOfBirth( req.getDateOfBirth());
				saveRes.setDateOfJoiningYear(Integer.valueOf(req.getDateOfJoiningYear()) );
				saveRes.setDateOfJoiningMonth(req.getDateOfJoiningMonth());			
				saveRes.setCompanyId(companyId);
				saveRes.setCreatedBy(createdBy);
				saveRes.setEmployeeId(empId);
				saveRes.setEmployeeName(req.getEmployeeName());
				saveRes.setEntryDate(entryDate);
				saveRes.setOccupationId(req.getOccupationId());
				saveRes.setOccupationDesc(req.getOccupationDesc());
				saveRes.setProductId(productId);
				saveRes.setQuoteNo(quoteNo);
				saveRes.setRequestReferenceNo(refNo);
				saveRes.setRiskId(Integer.valueOf(req.getLocationId()));
				saveRes.setLocationId(Integer.valueOf(req.getLocationId()));				
				saveRes.setStatus("Y");
				saveRes.setSalary(new BigDecimal(req.getSalary()));
				saveRes.setNationalityId(req.getNationalityId());
				saveRes.setProductDesc(productName);
				saveRes.setAddress(req.getAddress());
				saveRes.setSectionDesc(sectionDesc);
				saveRes.setLocationName(req.getLocationName());
				saveList.add(saveRes);
				
			}
			
			// Save All
			productRepo.saveAllAndFlush(saveList);
			
			// Change Location ID
			List<DocumentTransactionDetails> doc = docTransRepo.findByQuoteNoAndSectionIdAndProductIdAndCompanyId(quoteNo, Integer.valueOf(sectionId) ,productId,companyId);
			List<DocumentTransactionDetails> docUpdateList = new ArrayList<DocumentTransactionDetails>();
			for ( ProductEmployeeDetails emp : saveList ) {
				List<DocumentTransactionDetails> locfilter =  doc.stream().filter(o -> o.getIdType().equalsIgnoreCase("NATIONALITY_ID") 
						&& o.getId().equalsIgnoreCase(emp.getNationalityId())).collect(Collectors.toList());
					if(locfilter.size() > 0 ) {
						locfilter.forEach(  docum -> {
						DocumentTransactionDetails updDoc = new DocumentTransactionDetails(); 
						dozermapper.map(  docum  , updDoc);
						updDoc.setRiskId(emp.getEmployeeId().intValue());
						docUpdateList.add(updDoc);
						} ) ;
					}
					
			}
			docTransRepo.saveAllAndFlush(docUpdateList);
			
			// Copy Old Doc
			OldDocumentsCopyReq oldDocCopyReq = new OldDocumentsCopyReq();
			oldDocCopyReq.setInsuranceId(companyId);
			oldDocCopyReq.setProductId(productId.toString() );
			oldDocCopyReq.setSectionId(sectionId);
			oldDocCopyReq.setQuoteNo(quoteNo);
			docService.copyOldDocumentsToNewQuote(oldDocCopyReq);
			
			res.setResponse("Saved Successfully");
			res.setSuccessId("");
			
				
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Log Details" + e.getMessage());
			return null;
		}
	
		return res;
	}
		private synchronized SuccessRes call_insertEmpDetails2(List<ProductEmployeeSaveReq> reqList, String quoteNo,
			String sectionId, String createdBy, Integer productId, String refNo, String companyId,
			String excelUploadYN, CommonDataDetailsRepository commonRepo, PolicyCoverDataRepository coverdataRepo,
			ProductEmployeesDetailsRepository productRepo, DocumentCopyService docService,
			EntityManager em,GenerateSeqNoServiceImpl genSeqNoService,ProductEmployeeDetailsArchRepository empArchRepo, String sectionDesc) {
		
		DozerBeanMapper dozermapper = new DozerBeanMapper();
		SuccessRes res = new SuccessRes();
		try {
			
			
			Date entryDate = new Date();
		
			String productName =  getCompanyProductMasterDropdown(companyId , productId.toString() );
			
			List<ProductEmployeeDetails> saveList = new ArrayList<ProductEmployeeDetails>();
			
			
			List<CommonDataDetails> commlist = commonRepo.findByQuoteNoAndSectionId(quoteNo, sectionId)	;	
			List<PolicyCoverData> coverdata = coverdataRepo.findByQuoteNoAndCoverageTypeAndDiscLoadIdAndTaxId(quoteNo,"B",0,0); //for rate,
			Long empId   = 0L ;
			
			for (ProductEmployeeSaveReq req : reqList ) {

				empId = empId + 1L ;
				ProductEmployeeDetails saveRes = new ProductEmployeeDetails();
				
				List<CommonDataDetails> filtercomm=commlist.stream().filter(o->o.getOccupationType().equalsIgnoreCase(req.getOccupationId())).collect(Collectors.toList());
				if(filtercomm.size()>0) {
					CommonDataDetails commdata = filtercomm.get(0);
				
				saveRes.setPolicyStartDate(commdata.getPolicyStartDate());
				saveRes.setPolicyEndDate(commdata.getPolicyEndDate());
				saveRes.setRate(coverdata.get(0).getRate().doubleValue());
				saveRes.setExchangeRate(commdata.getExchangeRate()==null?0.0:Double.valueOf(commdata.getExchangeRate().toString()));
				
				double premiumfc = Double.valueOf(req.getSalary())*coverdata.get(0).getRate().doubleValue() ;
				
				saveRes.setPremiumFc(premiumfc);
				
				saveRes.setPremiumLc(saveRes.getExchangeRate()*premiumfc); //
				saveRes.setCurrencyCode(commdata.getCurrency());
			
				}
				
				saveRes.setSectionId(sectionId);
				saveRes.setDateOfBirth( req.getDateOfBirth());
				saveRes.setDateOfJoiningYear(Integer.valueOf(req.getDateOfJoiningYear()) );
				saveRes.setDateOfJoiningMonth(req.getDateOfJoiningMonth());			
				saveRes.setCompanyId(companyId);
				saveRes.setCreatedBy(createdBy);
				saveRes.setEmployeeId(empId);
				saveRes.setEmployeeName(req.getEmployeeName());
				saveRes.setEntryDate(entryDate);
				saveRes.setOccupationId(req.getOccupationId());
				saveRes.setOccupationDesc(req.getOccupationDesc());
				saveRes.setProductId(productId);
				saveRes.setQuoteNo(quoteNo);
				saveRes.setRequestReferenceNo(refNo);
				saveRes.setRiskId(Integer.valueOf(req.getLocationId()));
				saveRes.setLocationId(Integer.valueOf(req.getLocationId()));				
				saveRes.setStatus("Y");
				saveRes.setSalary(new BigDecimal(req.getSalary()));
				saveRes.setNationalityId(req.getNationalityId());
				saveRes.setProductDesc(productName);
				saveRes.setAddress(req.getAddress());
				saveRes.setSectionDesc(sectionDesc);
				saveRes.setLocationName(req.getLocationName());
				saveList.add(saveRes);
				
			}
			
			// Save All
			productRepo.saveAllAndFlush(saveList);
			
			// Save All
			productRepo.saveAllAndFlush(saveList);
			
			// Change Location ID
			List<DocumentTransactionDetails> doc = docTransRepo.findByQuoteNoAndSectionIdAndProductIdAndCompanyId(quoteNo, Integer.valueOf(sectionId) ,productId,companyId);
			List<DocumentTransactionDetails> docUpdateList = new ArrayList<DocumentTransactionDetails>();
			for ( ProductEmployeeDetails emp : saveList ) {
				List<DocumentTransactionDetails> locfilter =  doc.stream().filter(o -> o.getIdType().equalsIgnoreCase("NATIONALITY_ID") 
						&& o.getId().equalsIgnoreCase(emp.getNationalityId())).collect(Collectors.toList());
					if(locfilter.size() > 0 ) {
						locfilter.forEach(  docum -> {
						DocumentTransactionDetails updDoc = new DocumentTransactionDetails(); 
						dozermapper.map(  docum  , updDoc);
						updDoc.setRiskId(emp.getEmployeeId().intValue());
						docUpdateList.add(updDoc);
						} ) ;
					}
					
			}
			docTransRepo.saveAllAndFlush(docUpdateList);
			
			// Copy Old Doc
			OldDocumentsCopyReq oldDocCopyReq = new OldDocumentsCopyReq();
			oldDocCopyReq.setInsuranceId(companyId);
			oldDocCopyReq.setProductId(productId.toString() );
			oldDocCopyReq.setSectionId(sectionId);
			oldDocCopyReq.setQuoteNo(quoteNo);
			docService.copyOldDocumentsToNewQuote(oldDocCopyReq);
			
			res.setResponse("Saved Successfully");
			res.setSuccessId("");
			
				
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Log Details" + e.getMessage());
			return null;
		}
	
		return res;
	
	}
	public String getCompanyProductMasterDropdown(String companyId , String productId) {
		String productName = "";
		try {
			Date today = new Date();
			Calendar cal = new GregorianCalendar();
			cal.setTime(today);
			cal.set(Calendar.HOUR_OF_DAY, 23);;
			cal.set(Calendar.MINUTE, 1);
			today = cal.getTime();
			cal.set(Calendar.HOUR_OF_DAY, 1);
			cal.set(Calendar.MINUTE, 1);
			Date todayEnd = cal.getTime();
			
			// Criteria
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<CompanyProductMaster> query=  cb.createQuery(CompanyProductMaster.class);
			List<CompanyProductMaster> list = new ArrayList<CompanyProductMaster>();
			// Find All
			Root<CompanyProductMaster> c = query.from(CompanyProductMaster.class);
			//Select
			query.select(c);
			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(c.get("productName")));
			
			// Effective Date Start Max Filter
			Subquery<Date> effectiveDate = query.subquery(Date.class);
			Root<CompanyProductMaster> ocpm1 = effectiveDate.from(CompanyProductMaster.class);
			effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
			Predicate a1 = cb.equal(c.get("productId"),ocpm1.get("productId"));
			Predicate a2 = cb.equal(c.get("companyId"),ocpm1.get("companyId"));
			Predicate a3 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
			effectiveDate.where(a1,a2,a3);
			// Effective Date End Max Filter
			Subquery<Date> effectiveDate2 = query.subquery(Date.class);
			Root<CompanyProductMaster> ocpm2 = effectiveDate2.from(CompanyProductMaster.class);
			effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
			Predicate a4 = cb.equal(c.get("productId"),ocpm2.get("productId"));
			Predicate a5 = cb.equal(c.get("companyId"),ocpm2.get("companyId"));
			Predicate a6 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
			effectiveDate2.where(a4,a5,a6);
			
			// Where
			Predicate n1 = cb.equal(c.get("status"),"Y");
			Predicate n2 = cb.equal(c.get("effectiveDateStart"),effectiveDate);
			Predicate n3 = cb.equal(c.get("effectiveDateEnd"),effectiveDate2);	
			Predicate n4 = cb.equal(c.get("companyId"),companyId);
			Predicate n5 = cb.equal(c.get("productId"),productId);
			query.where(n1,n2,n3,n4,n5).orderBy(orderList);
			// Get Result
			TypedQuery<CompanyProductMaster> result = em.createQuery(query);
			list = result.getResultList();
			productName  = list.size()> 0 ? list.get(0).getProductName() : "";	
		}
			catch(Exception e) {
				e.printStackTrace();
				log.info("Exception is --->"+e.getMessage());
				return null;
				}
			return productName;
		}
	
	private synchronized SuccessRes deleteAllEmployees( String quoteNo, String sectionId) {
		SuccessRes res = new SuccessRes();
		DozerBeanMapper dozerMapper = new DozerBeanMapper();
		try {
			
			List<ProductEmployeeDetailsArch> savearchList = new ArrayList<ProductEmployeeDetailsArch>();
			
			List<ProductEmployeeDetails> old = productRepo.findByQuoteNoAndSectionId(quoteNo, sectionId);
			if(old.size()>0) {
				String archId = "AI-" +  genSeqNoService.generateArchId();
				for(ProductEmployeeDetails data : old) {
					ProductEmployeeDetailsArch savearch = new ProductEmployeeDetailsArch();
					savearch.setArchId(archId); 
					dozerMapper.map(data, savearch);
					savearchList.add(savearch);
				}
				empArchRepo.saveAllAndFlush(savearchList);
			
				productRepo.deleteAll(old);
			}
			
			res.setResponse("Deleted Successfully") ;
			res.setSuccessId("") ;
				
		}
		catch(Exception e) {
			e.printStackTrace();
			log.info("Log Details"+e.getMessage());
			res.setResponse("Failed to Delete") ;
			res.setSuccessId("") ;
			return res;
		}
		return res;
	}
}
