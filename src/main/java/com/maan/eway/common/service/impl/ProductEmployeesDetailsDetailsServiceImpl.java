package com.maan.eway.common.service.impl;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maan.eway.bean.CommonDataDetails;
import com.maan.eway.bean.CompanyProductMaster;
import com.maan.eway.bean.CurrencyMaster;
import com.maan.eway.bean.DocumentTransactionDetails;
import com.maan.eway.bean.EserviceSectionDetails;
import com.maan.eway.bean.HomePositionMaster;
import com.maan.eway.bean.PolicyCoverData;
import com.maan.eway.bean.PolicyCoverDataIndividuals;
import com.maan.eway.bean.ProductEmployeeDetails;
import com.maan.eway.bean.SectionDataDetails;
import com.maan.eway.common.Thread.Thread_insertEmpDetails;
import com.maan.eway.common.req.OldDocumentsCopyReq;
import com.maan.eway.common.req.ProductEmployeeDeleteReq;
import com.maan.eway.common.req.ProductEmployeeSaveReq;
import com.maan.eway.common.req.ProductEmployeesGetReq;
import com.maan.eway.common.req.SaveProductDetailsReq;
import com.maan.eway.common.res.PremiumGroupDevideRes;
import com.maan.eway.common.res.ProductEmployeeGetRes;
import com.maan.eway.common.service.DocumentCopyService;
import com.maan.eway.common.service.ProductEmployeesDetailsService;
import com.maan.eway.config.thread.MyTaskList;
import com.maan.eway.error.Error;
import com.maan.eway.repository.CommonDataDetailsRepository;
import com.maan.eway.repository.DocumentTransactionDetailsRepository;
import com.maan.eway.repository.EserviceCommonDetailsRepository;
import com.maan.eway.repository.HomePositionMasterRepository;
import com.maan.eway.repository.PolicyCoverDataIndividualsRepository;
import com.maan.eway.repository.PolicyCoverDataRepository;
import com.maan.eway.repository.ProductEmployeeDetailsArchRepository;
import com.maan.eway.repository.ProductEmployeesDetailsRepository;
import com.maan.eway.repository.SectionDataDetailsRepository;
import com.maan.eway.req.EmployeeIdDuplicaionReq;
import com.maan.eway.res.SuccessRes;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

@Service
@Transactional
public class ProductEmployeesDetailsDetailsServiceImpl implements ProductEmployeesDetailsService {

	@Autowired
	private CommonDataDetailsRepository commonRepo;

	@Autowired
	private PolicyCoverDataRepository coverdataRepo;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private ProductEmployeesDetailsRepository productRepo;

	@Autowired
	private ProductEmployeeDetailsArchRepository empArchRepo;

	@Autowired
	private GenerateSeqNoServiceImpl genSeqNoService;

	@Autowired
	private DocumentCopyService docService;

	@Autowired
	private SectionDataDetailsRepository secRepo;

	@Autowired
	private DocumentTransactionDetailsRepository docTransRepo;

	@Autowired
	private PolicyCoverDataRepository coverRepo;

	@Autowired
	private PolicyCoverDataIndividualsRepository indiCoverRepo;

	@Autowired
	private HomePositionMasterRepository homeRepo;

	@Autowired
	private EserviceCommonDetailsRepository eserCommonRepo;

	private Logger log = LogManager.getLogger(ProductEmployeesDetailsDetailsServiceImpl.class);

	@Override
	public List<Error> validateProductEmployeesDetails(SaveProductDetailsReq req1) {
		List<Error> error = new ArrayList<Error>();

		try {
//			String excelUploadYN = StringUtils.isBlank(req1.getExcelUploadYN()) ?"":req1.getExcelUploadYN();
//					
//			if( ! excelUploadYN.equalsIgnoreCase("Y")) {
//				
//				error = validateProductEmployeesDetailsExcel(req1);
//		
//			}
		} catch (Exception e) {

			log.error(e);
			e.printStackTrace();
			error.add(new Error("03", "Common Error", e.getMessage()));
		}
		return error;
	}

	@SuppressWarnings({ "unchecked", "unlikely-arg-type" })
	@Override
	@Transactional
	public SuccessRes saveProductEmployeesDetails(SaveProductDetailsReq req1) {
		SuccessRes res = new SuccessRes();
		try {
			String quoteNo = req1.getQuoteNo();
			String sectionId = req1.getSectionId();
			String createdBy = req1.getCreatedBy();
			Integer productId = null;// Integer.valueOf(req1.getProductId()) ;
			String refNo = null;// req1.getRequestReferenceNo() ;
			String companyId = null;// req1.getInsuranceId() ;
			String excelUploadYN = null;// StringUtils.isBlank(req1.getExcelUploadYN()) ?"":req1.getExcelUploadYN();

			String sectionDesc = "";

			List<ProductEmployeeSaveReq> reqList = req1.getProductEmployeeSaveReq(); // largeList
			// Upload documents delete while employee's deleted
			Map<String, List<ProductEmployeeSaveReq>> groubyByLoc = reqList.stream()
					.collect(Collectors.groupingBy(ProductEmployeeSaveReq::getLocationId));
			for (String loc : groubyByLoc.keySet()) {
				List<ProductEmployeeSaveReq> filterEmpList = groubyByLoc.get(loc);
				List<String> nationalityIds = filterEmpList.stream().map(ProductEmployeeSaveReq::getNationalityId)
						.collect(Collectors.toList());

				// Delete Non Opted Loc Document
				Long nonOptedLocDocsCount = docTransRepo.countByQuoteNoAndLocationIdAndSectionIdAndIdTypeAndIdNotIn(
						quoteNo, Integer.valueOf(loc), Integer.valueOf(sectionId), "NATIONALITY_ID", nationalityIds);
				if (nonOptedLocDocsCount > 0) {
					docTransRepo.deleteByQuoteNoAndLocationIdAndSectionIdAndIdTypeAndIdNotIn(quoteNo,
							Integer.valueOf(loc), Integer.valueOf(sectionId), "NATIONALITY_ID", nationalityIds);
				}

				Long empCount = productRepo.countByQuoteNoAndLocationIdAndSectionIdAndNationalityIdNotIn(quoteNo,
						Integer.valueOf(loc), sectionId, nationalityIds);
				if (!excelUploadYN.equalsIgnoreCase("Y")) {
					if (empCount > 0) {
						productRepo.deleteByQuoteNoAndLocationIdAndSectionIdAndNationalityIdNotIn(quoteNo,
								Integer.valueOf(loc), sectionId, nationalityIds);
					}
				}

			}

			List<SectionDataDetails> seclist = secRepo.findBySectionIdAndQuoteNo(sectionId, quoteNo);
			if (seclist.size() > 0)
				sectionDesc = seclist.get(0).getSectionDesc();

			int threadCount = 2;
			int targetSize = 10;

			List<List<ProductEmployeeSaveReq>> partitionList = ListUtils.partition(reqList, targetSize);

			// save Thread call
			List<Callable<Object>> queue = new ArrayList<Callable<Object>>();

			int temp = 1;

			for (List<ProductEmployeeSaveReq> partition : partitionList) {
				threadCount = threadCount + 2;
				if (temp % 2 == 0) {
					Thread_insertEmpDetails insert = new Thread_insertEmpDetails("saveempdetails1", partition, quoteNo,
							sectionId, createdBy, productId, refNo, companyId, excelUploadYN, commonRepo, coverdataRepo,
							productRepo, docService, em, genSeqNoService, empArchRepo, sectionDesc, docTransRepo);
					queue.add(insert);
				} else {
					Thread_insertEmpDetails insert = new Thread_insertEmpDetails("saveempdetails2", partition, quoteNo,
							sectionId, createdBy, productId, refNo, companyId, excelUploadYN, commonRepo, coverdataRepo,
							productRepo, docService, em, genSeqNoService, empArchRepo, sectionDesc, docTransRepo);
					queue.add(insert);
				}

				temp++;
			}

			MyTaskList taskList = new MyTaskList(queue);
			ForkJoinPool forkjoin = new ForkJoinPool(threadCount);
			ConcurrentLinkedQueue<Future<Object>> invoke = (ConcurrentLinkedQueue<Future<Object>>) forkjoin
					.invoke(taskList);

			for (Future<Object> callable : invoke) {
				if (callable.isDone()) {
					res = (SuccessRes) callable.get();
				}
			}

			if (res.getResponse().equalsIgnoreCase("Failed to Delete")) {
				return res;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Log Details" + e.getMessage());
			return null;
		}

		return res;
	}

	@Override
	public List<ProductEmployeeGetRes> getallProductEmployeesDetails(ProductEmployeesGetReq req) {
		// TODO Auto-generated method stub
		List<ProductEmployeeGetRes> resList = new ArrayList<ProductEmployeeGetRes>();
		try {
			List<ProductEmployeeDetails> datas = productRepo
					.findByQuoteNoAndSectionIdOrderByEmployeeIdAsc(req.getQuoteNo(), req.getSectionId());

			datas.forEach(data -> {
				ProductEmployeeGetRes res = new ProductEmployeeGetRes();
				res.setDateOfBirth(data.getDateOfBirth());
				res.setDateOfJoiningYear(data.getDateOfJoiningYear().toString());
				res.setDateOfJoiningMonth(data.getDateOfJoiningMonth());
				// res.setCreatedBy(data.getCreatedBy());
				res.setEmployeeId(data.getEmployeeId().intValue());
				res.setEmployeeName(data.getEmployeeName());
				// res.setEntryDate(data.getEntryDate());
				res.setOccupationDesc(data.getOccupationDesc());
				res.setOccupationId(data.getOccupationId());
				// res.setQuoteNo(data.getQuoteNo());
				// res.setRequestReferenceNo(data.getRequestReferenceNo());
				res.setRiskId(data.getLocationId());
				res.setLocationId(data.getLocationId());
				res.setSalary(data.getSalary().toPlainString());
				res.setProductId(data.getProductId().toString());
				res.setProductDesc(data.getProductDesc());
				res.setNationalityId(data.getNationalityId());
				// res.setInsuranceId(data.getCompanyId());
				res.setAddress(data.getAddress());
				res.setSectionId(data.getSectionId());
				// res.setPolicyStartDate(data.getPolicyStartDate());
				// res.setPolicyEndDate(data.getPolicyEndDate());
				res.setRate(data.getRate() == null ? "0" : data.getRate().toString());
				res.setPremiumFc(data.getPremiumFc() == null ? "0" : data.getPremiumFc().toString());
				res.setPremiumLc(data.getPremiumLc() == null ? "0" : data.getPremiumLc().toString());
				// res.setCurrencyCode(data.getCurrencyCode());
				// res.setExchangeRate(data.getExchangeRate()==null?"":data.getPremiumLc().toString());

				resList.add(res);
			});

		} catch (Exception e) {
			e.printStackTrace();
			log.info("Log Details" + e.getMessage());
			return null;
		}
		return resList;
	}

	@Override
	public SuccessRes deleteProductEmployeesDetails(ProductEmployeeDeleteReq req) {
		SuccessRes res = new SuccessRes();
		try {

			// List<ProductEmployeeDetailsArch> savearchList = new
			// ArrayList<ProductEmployeeDetailsArch>();

			List<ProductEmployeeDetails> old = productRepo.findByQuoteNoAndSectionIdAndEmployeeIdAndStatusNot(
					req.getQuoteNo(), req.getSectionId(), Long.valueOf(req.getEmployeeId()), "D");
			old.forEach(o -> {
				o.setStatus("D");
			});

			productRepo.saveAllAndFlush(old);
			// docTransRepo.deleteAll(oldDoc);

			res.setResponse("Deleted Successfully");
			res.setSuccessId(req.getQuoteNo());
			;

		} catch (Exception e) {
			e.printStackTrace();
			log.info("Log Details" + e.getMessage());
			return null;
		}
		return res;
	}

	public String getCompanyProductMasterDropdown(String companyId, String productId) {
		String productName = "";
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
			productName = list.size() > 0 ? list.get(0).getProductName() : "";
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is --->" + e.getMessage());
			return null;
		}
		return productName;
	}

	@Override
	public List<Error> validateProductEmployeesDetailsExcel(SaveProductDetailsReq req1) {
		List<Error> error = new ArrayList<Error>();

		try {

			List<ProductEmployeeSaveReq> reqList = req1.getProductEmployeeSaveReq();
			// List<String> nationalityIds = new ArrayList<String>();
			List<EmployeeIdDuplicaionReq> nationalityIds = new ArrayList<EmployeeIdDuplicaionReq>();

			Long row = 0L;
			BigDecimal sumInsured = BigDecimal.ZERO;
			String quoteNo = "";
			List<CommonDataDetails> commonDatas = new ArrayList<CommonDataDetails>();

			if (reqList != null && reqList.size() > 0 && StringUtils.isNotBlank(req1.getQuoteNo())) {
				commonDatas = commonRepo.findByQuoteNoAndSectionId(req1.getQuoteNo(), req1.getSectionId());
				commonDatas = commonDatas.stream().filter(o -> !o.getStatus().equalsIgnoreCase("D"))
						.collect(Collectors.toList());
				quoteNo = req1.getQuoteNo();

			} else {
				error.add(new Error("01", "QuoteNo", "Please Enter Atleat one Employee Details "));
			}
			// Long reqempId = StringUtils.isBlank(reqList.get(0).getEmployeeId() ) ? null :
			// Long.valueOf( reqList.get(0).getEmployeeId()) ;

			if (StringUtils.isBlank(req1.getQuoteNo())) {
				error.add(new Error("01", "QuoteNo", "Please Select QuoteNo"));
			}

			if (StringUtils.isBlank(req1.getCreatedBy())) {
				error.add(new Error("01", "CreatedBy", "Please Select CreatedBy"));
			}
//			if (StringUtils.isBlank(req1.getRequestReferenceNo())) {
//				error.add(new Error("01", "RequestReferenceNo", "Please Select RequestReferenceNo"));
//			}
//			
//			if (StringUtils.isBlank(req1.getRequestReferenceNo())) {
//				error.add(new Error("01", "RequestReferenceNo", "Please Select RequestReferenceNo"));
//			}

			for (ProductEmployeeSaveReq req : reqList) {
				row = row + 1;

				if (StringUtils.isBlank(req.getOccupationDesc())) {
					error.add(new Error("01", "Occupation", "Please Select Occupation in Row : " + row));
				}

				if (StringUtils.isBlank(req.getNationalityId())) {
					error.add(new Error("01", "NationalityId", "Please Select NationalityId in Row : " + row));

				} else {

					// List<String> filterIds = nationalityIds.stream().filter( o ->
					// (o.equalsIgnoreCase(req.getNationalityId())) && ()
					// ).collect(Collectors.toList());
					List<EmployeeIdDuplicaionReq> filterIds = nationalityIds.stream()
							.filter(o -> (o.getNationalityId().equalsIgnoreCase(req.getNationalityId()))
									&& (o.getLocationId().equalsIgnoreCase(req.getLocationId())))
							.collect(Collectors.toList());

					if (filterIds.size() > 0) {
						error.add(new Error("01", "NationalityId", "Duplicate NationalityId in Row : " + row));
					} else {
						EmployeeIdDuplicaionReq nation = new EmployeeIdDuplicaionReq();
						nation.setLocationId(req.getLocationId());
						nation.setNationalityId(req.getNationalityId());
						nationalityIds.add(nation);
					}

				}

				if (StringUtils.isBlank(req.getLocationId())) {
					error.add(new Error("01", "LocationId", "Please Select LocationId in Row : " + row));
				}

				// Date Of Birth Validation
				Calendar cal = Calendar.getInstance();
				Date today = new Date();
				cal.setTime(today);
				cal.add(Calendar.DAY_OF_MONTH, -1);
				cal.set(Calendar.HOUR_OF_DAY, 23);
				cal.set(Calendar.MINUTE, 50);
				today = cal.getTime();

				if (req.getDateOfBirth() == null) {
					error.add(new Error("38", "DateOfBirth", "Please Select Date Of Birth in Row : " + row));

				} else if (req.getDateOfBirth().after(today)) {
					error.add(
							new Error("38", "DateOfBirth", "Please Select Date Of Birth as Past Date in Row : " + row));

				} else {
					LocalDate localDate1 = req.getDateOfBirth().toInstant().atZone(ZoneId.systemDefault())
							.toLocalDate();
					LocalDate localDate2 = today.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

					Integer years = Period.between(localDate1, localDate2).getYears();
					if (years > 100) {
						error.add(new Error("38", "DateOfBirth",
								"Date Of Birth Not Accepted More than 100 Years in Row : " + row));

					} else if (years < 18) {
						error.add(new Error("38", "DateOfBirth",
								"Date Of Birth Not Accepted Less than 18 Years For Induvidual in Row : " + row));

					}

				}

				// Date Of Joining
				int year = Calendar.getInstance().get(Calendar.YEAR);

				if (StringUtils.isBlank(req.getDateOfJoiningYear())) {
					error.add(
							new Error("33", "DateOfJoiningYear", "Please Enter Date Of Joining Year in Row : " + row));

				} else if (StringUtils.isNotBlank(req.getDateOfJoiningYear())) {
					Integer year1 = Integer.valueOf(req.getDateOfJoiningYear());
					Integer diff = year - year1;
					if ((StringUtils.isNotBlank(req.getDateOfJoiningYear())
							&& !req.getDateOfJoiningYear().matches("[0-9]+"))
							|| req.getDateOfJoiningYear().length() > 4) {
						error.add(new Error("33", "DateOfJoiningYear",
								"Please Enter Date Of Joining Format in YYYY in Row : " + row));
					} else if ((StringUtils.isNotBlank(req.getDateOfJoiningYear())
							&& !req.getDateOfJoiningYear().matches("[0-9]+"))
							|| req.getDateOfJoiningYear().length() < 4) {
						error.add(new Error("33", "DateOfJoiningYear",
								"Please Enter Date Of Joining Format in YYYY  in Row : " + row));
					} else if (StringUtils.isNotBlank(req.getDateOfJoiningYear())) {
						if (year1 > year) {
							error.add(new Error("33", "DateOfJoiningYear",
									"Please Enter Date Of Joining as Past Year  in Row : " + row));
						} else if (diff > 50) {
							error.add(new Error("33", "DateOfJoiningYear",
									"Please Enter Date Of Joining within 50 years  in Row : " + row));
						}
					}
				}

				if (StringUtils.isBlank(req.getEmployeeName())) {
					error.add(new Error("03", "Employee Name ", "Please Enter Employee Name In Row No : " + row));
				} else if ((StringUtils.isNotBlank(req.getEmployeeName()))
						&& !req.getEmployeeName().matches("[A-Z a-z]+")) {
					error.add(new Error("04", "Employee Name ", "Please Enter Valid Employee Name  In Row No:" + row));
				}

				// Drop Downs
				if (StringUtils.isBlank(req.getSalary())) {
					error.add(new Error("08", "Salary", "Please Enter Employee Salary In Row No : " + row));
				} else if (!req.getSalary().matches("[0-9.]+") || Double.valueOf(req.getSalary()) <= 0) {
					error.add(new Error("09", "Salary",
							"Please Enter Valid Number In Employee Salary  In Row No : " + row));
				} else {
					sumInsured = sumInsured.add(new BigDecimal(req.getSalary()));
				}

			}

			int checkCount = 0;
			int empCount = 0;
			double totalSi = 0.0;
			double empSi = 0.0;
			boolean temp1 = true;
			int indivcount = 0;
			String empcountSIvalidYN = "Y";// StringUtils.isBlank(req1.getEmpcountSIvalidYN())?"":req1.getEmpcountSIvalidYN();

			if (!empcountSIvalidYN.equalsIgnoreCase("Y")) { // Do you want to continue with incorrect count/Sum Insured?
															// Yes/No
				if (error.size() < 1) {

					empCount = commonDatas.stream().mapToInt(o -> o.getCount().intValue()).sum();

					// count
					if (reqList.size() > empCount || reqList.size() < empCount) {

						error.add(
								new Error("333", "Employees Count", "Employee's Details Count Should be " + empCount));
					}
				}

				if (error.size() < 1) {
					// Total si & Individual occupation count
					for (CommonDataDetails cdata : commonDatas) {
						indivcount = 0;
						checkCount = 0;
						empSi = 0.0;
						indivcount = indivcount + cdata.getCount().intValue();

						totalSi = cdata.getSumInsured() == null ? 0.0 : cdata.getSumInsured().doubleValue();

						checkCount = (int) reqList.stream().filter(o -> o.getOccupationId() != null
								&& o.getOccupationId().equalsIgnoreCase(cdata.getOccupationType())).count();
						empSi = reqList.stream()
								.filter(o -> o.getOccupationId() != null
										&& o.getOccupationId().equalsIgnoreCase(cdata.getOccupationType()))
								.mapToDouble(o -> Double.valueOf(o.getSalary())).sum();

						if (indivcount != checkCount) {
							error.add(new Error("111", "Occupation Count", "Employee Details count should be "
									+ indivcount + " for Occupation " + "'" + cdata.getOccupationDesc() + "'"));
							temp1 = false;
						}
						if (error.size() < 1) {
							if (totalSi != empSi) {

								error.add(new Error("222", "Sum Insured",
										"Total SumInsured not equal to the Actual SumInsured for occupation " + "'"
												+ cdata.getOccupationDesc() + "'"));
								temp1 = false;
							}
						}
						if (!temp1)
							break;
					}

				}
			}

		} catch (Exception e) {

			log.error(e);
			e.printStackTrace();
			error.add(new Error("03", "Common Error", e.getMessage()));
		}
		return error;
	}

	@SuppressWarnings("unchecked")
	private SuccessRes oldAllRecordsDeleteThreadCall(List<ProductEmployeeSaveReq> partition1, String quoteNo,
			String sectionId, String createdBy, Integer productId, String refNo, String companyId, String excelUploadYN,
			String sectionDesc) {
		SuccessRes res = new SuccessRes();
		try {
			int threadCount = 1;
			// Thread call
			List<Callable<Object>> queue = new ArrayList<Callable<Object>>();

			Thread_insertEmpDetails insert = new Thread_insertEmpDetails("deleteAllEmployees", partition1, quoteNo,
					sectionId, createdBy, productId, refNo, companyId, excelUploadYN, commonRepo, coverdataRepo,
					productRepo, docService, em, genSeqNoService, empArchRepo, sectionDesc, docTransRepo);
			queue.add(insert);
			MyTaskList taskList = new MyTaskList(queue);

			ForkJoinPool forkjoin = new ForkJoinPool(threadCount);
			ConcurrentLinkedQueue<Future<Object>> invoke = (ConcurrentLinkedQueue<Future<Object>>) forkjoin
					.invoke(taskList);

			for (Future<Object> callable : invoke) {
				if (callable.isDone()) {
					res = (SuccessRes) callable.get();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.info("Log Details" + e.getMessage());
			return null;
		}
		return res;
	}

	@Override
	public List<String> validateSaveEmployeesDetails(SaveProductDetailsReq req1) {
		List<String> error = new ArrayList<String>();

		try {

			List<ProductEmployeeSaveReq> reqList = req1.getProductEmployeeSaveReq();
			// List<String> nationalityIds = new ArrayList<String>();
			List<EmployeeIdDuplicaionReq> nationalityIds = new ArrayList<EmployeeIdDuplicaionReq>();

			Long row = 0L;
			List<CommonDataDetails> commonDatas = new ArrayList<CommonDataDetails>();
			List<ProductEmployeeDetails> old = productRepo.findByQuoteNoAndSectionIdAndStatusNot(req1.getQuoteNo(),
					req1.getSectionId(), "D");
			if (StringUtils.isBlank(req1.getQuoteNo())) {
				error.add("1155");
//				error.add(new Error("01", "QuoteNo", "Please Enter QuoteNo"));
			} else if (StringUtils.isBlank(req1.getSectionId())) {
				error.add("1154");
//				error.add(new Error("01", "SectionId", "Please Enter SectionId"));
			} else {
				if (reqList != null && reqList.size() > 0) {
					commonDatas = commonRepo.findByQuoteNoAndSectionId(req1.getQuoteNo(), req1.getSectionId());
					commonDatas = commonDatas.stream().filter(o -> !o.getStatus().equalsIgnoreCase("D"))
							.collect(Collectors.toList());

				} else {
					error.add("1196");
//					error.add(new Error("01", "QuoteNo", "Please Enter Atleat one Employee Details "));
				}
			}

			if (StringUtils.isBlank(req1.getCreatedBy())) {
				error.add("1197");
//				error.add(new Error("01", "CreatedBy", "Please Enter CreatedBy"));
			}

			if ("43".equalsIgnoreCase(req1.getProductId())) {
				for (ProductEmployeeSaveReq req : reqList) {
					row = row + 1;

					// Year Of Passing
					int year = Calendar.getInstance().get(Calendar.YEAR);

					if (StringUtils.isBlank(req.getDateOfJoiningYear())) {
						error.add("1244" + "," + row);
//						error.add(new Error("33", "DateOfJoiningYear",
//								"Please Enter Date Of Joining Year in Row : " + row));

					} else if (StringUtils.isNotBlank(req.getDateOfJoiningYear())) {
						Integer year1 = Integer.valueOf(req.getDateOfJoiningYear());
						Integer diff = year - year1;
						if ((StringUtils.isNotBlank(req.getDateOfJoiningYear())
								&& !req.getDateOfJoiningYear().matches("[0-9]+"))
								|| req.getDateOfJoiningYear().length() > 4) {
							error.add("1245" + "," + row);
//							error.add(new Error("33", "DateOfJoiningYear",
//									"Please Enter Date Of Joining Format in YYYY in Row : " + row));
						} else if ((StringUtils.isNotBlank(req.getDateOfJoiningYear())
								&& !req.getDateOfJoiningYear().matches("[0-9]+"))
								|| req.getDateOfJoiningYear().length() < 4) {
							error.add("1245" + "," + row);
//							error.add(new Error("33", "DateOfJoiningYear",
//									"Please Enter Date Of Joining Format in YYYY  in Row : " + row));
						} else if (StringUtils.isNotBlank(req.getDateOfJoiningYear())) {
							if (year1 > year) {
								error.add("1246" + "," + row);
//								error.add(new Error("33", "DateOfJoiningYear",
//										"Please Enter Date Of Joining as Past Year  in Row : " + row));
							} else if (diff > 50) {
								error.add("1247" + "," + row);
//								error.add(new Error("33", "DateOfJoiningYear",
//										"Please Enter Date Of Joining within 50 years  in Row : " + row));
							}
						}
					}

					if (StringUtils.isBlank(req.getEmployeeName())) {
						error.add("1210" + "," + row);
//						error.add(new Error("03", "Employee Name ", "Please Enter Employee Name In Row No : " + row));
					} else if ((StringUtils.isNotBlank(req.getEmployeeName()))
							&& !req.getEmployeeName().matches("[A-Z a-z]+")) {
						error.add("1211" + "," + row);
//						error.add(new Error("04", "Employee Name ",
//								"Please Enter Valid Employee Name  In Row No:" + row));
					}

					if (StringUtils.isBlank(req.getHighestQualificationHeld())) {
						error.add("1248" + "," + row);
//						error.add(new Error("03", "Highest Qualification Held", "Please Enter Highest Qualification Held In Row No : " + row));
					}
					if (StringUtils.isBlank(req.getIssuingAuthority())) {
						error.add("1249" + "," + row);
//						error.add(new Error("03", "Issuing Authority", "Please Enter Issuing Authority In Row No : " + row));
					}
				}
			} else {
				for (ProductEmployeeSaveReq req : reqList) {
					row = row + 1;

					if (StringUtils.isBlank(req.getOccupationDesc())) {
						error.add("1198" + "," + row);
//					error.add(new Error("01", "Occupation", "Please Select Occupation in Row : " + row));
					}

					if ((!req1.getSectionId().equals("45")
							|| !req1.getProductId().equals("57") | (!req1.getSectionId().equals("43")))
							&& StringUtils.isBlank(req.getNationalityId())) {
						error.add("1199" + "," + row);
//					error.add(new Error("01", "NationalityId", "Please Select NationalityId in Row : " + row));

					} else {

						List<ProductEmployeeDetails> filterOldIds = old.stream()
								.filter(o -> (o.getNationalityId().equalsIgnoreCase(req.getNationalityId()))
										&& (o.getLocationId().equals(Integer.valueOf(req.getLocationId()))))
								.collect(Collectors.toList());
						if ((!req1.getSectionId().equals("45") || !req1.getProductId().equals("57"))
								&& filterOldIds.size() > 0 && StringUtils.isBlank(req.getEmployeeId())) {
							error.add("1243" + "," + row);
//						error.add(new Error("12", "NationalityId", "Row No : "+ row + " NationalityId is Already Active in Previous Record "));
						} else if ((!req1.getSectionId().equals("45") || !req1.getProductId().equals("57"))
								&& filterOldIds.size() > 0 && StringUtils.isNotBlank(req.getEmployeeId())
								&& !filterOldIds.get(0).getEmployeeId().equals(Long.valueOf(req.getEmployeeId()))) {
							error.add("1243" + "," + row);
//						error.add(new Error("01", "NationalityId","Row NO : " + row + " NationalityId Is Already Active in Previous Record "));
						} else {
							// List<String> filterIds = nationalityIds.stream().filter( o ->
							// (o.equalsIgnoreCase(req.getNationalityId())) && ()
							// ).collect(Collectors.toList());
							List<EmployeeIdDuplicaionReq> filterIds = nationalityIds.stream()
									.filter(o -> (o.getNationalityId().equalsIgnoreCase(req.getNationalityId()))
											&& (o.getLocationId().equalsIgnoreCase(req.getLocationId())))
									.collect(Collectors.toList());

							if ((!req1.getSectionId().equals("45") || !req1.getProductId().equals("57"))
									&& filterIds.size() > 0) {
								error.add("1200" + "," + row);
//							error.add(new Error("01", "NationalityId", "Duplicate NationalityId in Row : " + row));
							} else {
								EmployeeIdDuplicaionReq nation = new EmployeeIdDuplicaionReq();
								nation.setLocationId(req.getLocationId());
								nation.setNationalityId(req.getNationalityId());
								nationalityIds.add(nation);
							}
						}

					}

					if (StringUtils.isBlank(req.getLocationId())) {
						error.add("1201" + "," + row);
//					error.add(new Error("01", "LocationId", "Please Select LocationId in Row : " + row));
					}

					// Date Of Birth Validation
					Calendar cal = Calendar.getInstance();
					Date today = new Date();
					cal.setTime(today);
					cal.add(Calendar.DAY_OF_MONTH, -1);
					cal.set(Calendar.HOUR_OF_DAY, 23);
					cal.set(Calendar.MINUTE, 50);
					today = cal.getTime();

					/*
					 * if (req.getDateOfBirth() == null) { error.add("1202"+","+row); //
					 * error.add(new Error("38", "DateOfBirth",
					 * "Please Select Date Of Birth in Row : " + row));
					 * 
					 * } else if (req.getDateOfBirth().after(today)) { error.add("1203"+","+row); //
					 * error.add(new Error("38", "DateOfBirth",
					 * "Please Select Date Of Birth as Past Date in Row : " + row));
					 * 
					 * } else { LocalDate localDate1 =
					 * req.getDateOfBirth().toInstant().atZone(ZoneId.systemDefault())
					 * .toLocalDate(); LocalDate localDate2 =
					 * today.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					 * 
					 * Integer years = Period.between(localDate1, localDate2).getYears(); if (years
					 * > 100) { error.add("1204"+","+row); // error.add(new Error("38",
					 * "DateOfBirth", "Date Of Birth Not Accepted More than 100 Years in Row : " +
					 * row));
					 * 
					 * } else if (years < 18) { error.add("1205"+","+row); // error.add(new
					 * Error("38", "DateOfBirth",
					 * "Date Of Birth Not Accepted Less than 18 Years For Induvidual in Row : " +
					 * row));
					 * 
					 * }
					 * 
					 * }
					 */

					// Date Of Joining

					if ((!req1.getSectionId().equals("45") && !req1.getSectionId().equals("43")
							&& !req1.getProductId().equals("57"))) {
						int year = Calendar.getInstance().get(Calendar.YEAR);

						if (StringUtils.isBlank(req.getDateOfJoiningYear())) {
							error.add("1206" + "," + row);
//					error.add(new Error("33", "DateOfJoiningYear", "Please Enter Date Of Joining Year in Row : " + row));

						} else if (StringUtils.isNotBlank(req.getDateOfJoiningYear())) {
							Integer year1 = Integer.valueOf(req.getDateOfJoiningYear());
							Integer diff = year - year1;
							if ((StringUtils.isNotBlank(req.getDateOfJoiningYear())
									&& !req.getDateOfJoiningYear().matches("[0-9]+"))
									|| req.getDateOfJoiningYear().length() > 4) {
								error.add("1207" + "," + row);
//						 error.add(new Error("33", "DateOfJoiningYear", "Please Enter Date Of Joining Format in YYYY in Row : " + row));
							} else if ((StringUtils.isNotBlank(req.getDateOfJoiningYear())
									&& !req.getDateOfJoiningYear().matches("[0-9]+"))
									|| req.getDateOfJoiningYear().length() < 4) {
								error.add("1207" + "," + row);
//						error.add(new Error("33", "DateOfJoiningYear","Please Enter Date Of Joining Format in YYYY  in Row : " + row));
							} else if (StringUtils.isNotBlank(req.getDateOfJoiningYear())) {
								if (year1 > year) {
									error.add("1208" + "," + row);
//							error.add(new Error("33", "DateOfJoiningYear","Please Enter Date Of Joining as Past Year  in Row : " + row));
								} else if (diff > 50) {
									error.add("1209" + "," + row);
//							error.add(new Error("33", "DateOfJoiningYear","Please Enter Date Of Joining within 50 years  in Row : " + row));
								}
							}
						}
					}

					if (StringUtils.isBlank(req.getEmployeeName())) {
						error.add("1210" + "," + row);
//					error.add(new Error("03", "Employee Name ", "Please Enter Employee Name In Row No : " + row));
					} else if ((StringUtils.isNotBlank(req.getEmployeeName()))
							&& !req.getEmployeeName().matches("[A-Z a-z]+")) {
						error.add("1211" + "," + row);
//					error.add(new Error("04", "Employee Name ", "Please Enter Valid Employee Name  In Row No:" + row ));
					}

					// Drop Downs
					if (StringUtils.isBlank(req.getSalary())) {
						error.add("1212" + "," + row);
//					error.add(new Error("08", "Salary", "Please Enter Employee Salary In Row No : " + row));
					} else if (!req.getSalary().matches("[0-9.]+") || Double.valueOf(req.getSalary()) <= 0) {
						error.add("1213" + "," + row);
//					error.add(new Error("09", "Salary", "Please Enter Valid Number In Employee Salary  In Row No : " + row));
					}

				}

			}
		} catch (Exception e) {

			log.error(e);
			e.printStackTrace();
			error.add("1185");
//		error.add(new Error("03", "Common Error", e.getMessage()));
		}
		return error;
	}

	@Override
	public SuccessRes saveEmployeesDetails(SaveProductDetailsReq req) {
		SuccessRes res = new SuccessRes();
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		try {
			List<ProductEmployeeSaveReq> reqList = req.getProductEmployeeSaveReq();
			// Primary Tables & Details
			String quoteNo = req.getQuoteNo();
			Integer sectionId = Integer.valueOf(req.getSectionId());
			Integer lastPassCount = Integer.valueOf(productRepo.countByQuoteNo(quoteNo).toString());
			List<PolicyCoverData> coverList = coverRepo.findByQuoteNoAndSectionId(quoteNo, sectionId);
			List<PremiumGroupDevideRes> covers = new ArrayList<PremiumGroupDevideRes>();
			Type listType = new TypeToken<List<PremiumGroupDevideRes>>() {
			}.getType();
			covers = modelMapper.map(coverList, listType);
			List<CommonDataDetails> groupList = commonRepo.findByQuoteNoAndSectionIdAndStatusNot(quoteNo,
					sectionId.toString(), "D");
			HomePositionMaster homeData = homeRepo.findByQuoteNo(quoteNo);

			Map<String, List<ProductEmployeeSaveReq>> groupByGroupId = null;
			// Grouping

			groupByGroupId = reqList.stream().filter(o -> o.getOccupationId() != null)
					.collect(Collectors.groupingBy(ProductEmployeeSaveReq::getOccupationId));
			Map<Integer, List<PremiumGroupDevideRes>> coverGroupByGroupId = covers.stream()
					.filter(o -> o.getVehicleId() != null)
					.collect(Collectors.groupingBy(PremiumGroupDevideRes::getVehicleId));

			// Framing List
			List<PolicyCoverDataIndividuals> totalIndiCovers = new ArrayList<PolicyCoverDataIndividuals>();

			List<ProductEmployeeDetails> saveList = new ArrayList<ProductEmployeeDetails>();
			for (String group : groupByGroupId.keySet()) {

				List<ProductEmployeeSaveReq> filterReqPass = groupByGroupId.get(group);
				List<PremiumGroupDevideRes> groupCovers = coverGroupByGroupId.get(Integer.valueOf(group));

				List<CommonDataDetails> filterGroupList = groupList.stream()
						.filter(o -> o.getRiskId().equals(Integer.valueOf(group))).collect(Collectors.toList());
				CommonDataDetails groupData = new CommonDataDetails();
				if (filterGroupList.size() > 0) {
					groupData = filterGroupList.get(0);

				}

				for (ProductEmployeeSaveReq data : filterReqPass) {
					ProductEmployeeDetails saveData = new ProductEmployeeDetails();

					List<PremiumGroupDevideRes> getDividedCovers = getDevidedCovers(quoteNo, BigDecimal.ONE,
							groupCovers);
					Double rate = getDividedCovers.stream()
							.filter(o -> o.getTaxId().equals(0) && o.getDiscLoadId().equals(0) && o.getRate() != null
									&& o.getRate().doubleValue() > 0D)
							.mapToDouble(o -> o.getRate().doubleValue()).sum();
					Double overAllPremiumFc = getDividedCovers.stream()
							.filter(o -> o.getTaxId().equals(0) && o.getDiscLoadId().equals(0)
									&& o.getPremiumIncludedTaxFc() != null
									&& o.getPremiumIncludedTaxFc().doubleValue() > 0D)
							.mapToDouble(o -> o.getPremiumIncludedTaxFc().doubleValue()).sum();
					Double overAllPremiumLc = getDividedCovers.stream()
							.filter(o -> o.getTaxId().equals(0) && o.getDiscLoadId().equals(0)
									&& o.getPremiumIncludedTaxLc() != null
									&& o.getPremiumIncludedTaxLc().doubleValue() > 0D)
							.mapToDouble(o -> o.getPremiumIncludedTaxLc().doubleValue()).sum();

					Integer empId = 0;

					if (StringUtils.isNotBlank(data.getEmployeeId())) {
						empId = Integer.valueOf(data.getEmployeeId());
					} else {
						lastPassCount = lastPassCount + 1;
						empId = lastPassCount;
					}
					Integer passCount = empId;

					// Employee Details
					saveData.setSectionId(sectionId.toString());
					saveData.setDateOfBirth(data.getDateOfBirth() == null ? null : data.getDateOfBirth());
					saveData.setDateOfJoiningYear(
							data.getDateOfJoiningYear() != null && !data.getDateOfJoiningYear().isEmpty()
									? Integer.valueOf(data.getDateOfJoiningYear())
									: null);
					saveData.setDateOfJoiningMonth(
							data.getDateOfJoiningMonth() == null ? null : data.getDateOfJoiningMonth());
					saveData.setCompanyId(homeData.getCompanyId());
					saveData.setCreatedBy(req.getCreatedBy());
					saveData.setEmployeeId(StringUtils.isBlank(data.getEmployeeId())?Long.valueOf(data.getEmployeeId()):Long.valueOf(empId));
					saveData.setEmployeeName(data.getEmployeeName());
					saveData.setEntryDate(new Date());

					saveData.setOccupationId(data.getOccupationId() == null ? null : data.getOccupationId());
					saveData.setOccupationDesc(data.getOccupationDesc() == null ? null : data.getOccupationDesc());
					saveData.setProductId(homeData.getProductId());
					saveData.setQuoteNo(quoteNo);
					saveData.setRequestReferenceNo(homeData.getRequestReferenceNo());
					saveData.setRiskId(data.getOccupationId() == null ? 1 : Integer.valueOf(data.getLocationId()));
					saveData.setLocationId(data.getLocationId() == null ? 1 : Integer.valueOf(data.getLocationId()));
					saveData.setStatus("Y");
					saveData.setSalary(data.getSalary() == null ? BigDecimal.ZERO : new BigDecimal(data.getSalary()));
					saveData.setNationalityId(data.getNationalityId() == null ? "0" : data.getNationalityId());
					saveData.setProductDesc(homeData.getProductName());
					saveData.setAddress(data.getAddress() == null ? " " : data.getAddress());
					saveData.setSectionDesc(groupData.getSectionDesc());
					saveData.setLocationName(data.getLocationName() == null ? " " : data.getLocationName());

					// Premium Details
					saveData.setPolicyStartDate(groupData.getPolicyStartDate());
					saveData.setPolicyEndDate(groupData.getPolicyEndDate());
					saveData.setRate(rate);
					saveData.setExchangeRate(groupData.getExchangeRate() == null ? null
							: Double.valueOf(groupData.getExchangeRate().toString()));
					saveData.setPremiumFc(overAllPremiumFc);
					saveData.setPremiumLc(overAllPremiumLc);
					saveData.setCurrencyCode(groupData.getCurrency());

					saveList.add(saveData);

					// Covers
					List<PolicyCoverDataIndividuals> indiCovers = new ArrayList<PolicyCoverDataIndividuals>();
					Type listType2 = new TypeToken<List<PolicyCoverDataIndividuals>>() {
					}.getType();
					indiCovers = modelMapper.map(getDividedCovers, listType2);
					for (PolicyCoverDataIndividuals o : indiCovers) {
						o.setGroupId(groupData.getRiskId());
						o.setGroupCount(groupData.getCount());
						o.setVehicleId(passCount);
						o.setIndividualId(passCount);
					}
					totalIndiCovers.addAll(indiCovers);
				}

			}
			// Save All Passengers
			List<Integer> passengerIds = new ArrayList<Integer>();
			List<Long> passengerId2 = new ArrayList<Long>();
			saveList.forEach(o -> {
				passengerIds.add(Integer.valueOf(o.getEmployeeId().toString()));
				passengerId2.add(o.getEmployeeId());
			});
			Long empCount = productRepo.countByQuoteNoAndSectionIdAndEmployeeIdInAndStatusNot(quoteNo,
					sectionId.toString(), passengerId2, "D");
			if (empCount > 0) {
				productRepo.deleteByQuoteNoAndSectionIdAndEmployeeIdInAndStatusNot(quoteNo, sectionId.toString(),
						passengerId2, "D");
			}
			productRepo.saveAllAndFlush(saveList);

			// Save Divided Indi Covers

			// Remove Deactivated Covers
			Long count = indiCoverRepo.countByQuoteNoAndSectionIdAndVehicleIdIn(quoteNo, sectionId, passengerIds);
			if (count > 0) {
				indiCoverRepo.deleteByQuoteNoAndSectionIdAndVehicleIdIn(quoteNo, sectionId, passengerIds);
			}
			indiCoverRepo.saveAllAndFlush(totalIndiCovers);

			// Copy Old Doc
			OldDocumentsCopyReq oldDocCopyReq = new OldDocumentsCopyReq();
			oldDocCopyReq.setInsuranceId(homeData.getCompanyId());
			oldDocCopyReq.setProductId(homeData.getProductId().toString());
			oldDocCopyReq.setSectionId(sectionId.toString());
			oldDocCopyReq.setQuoteNo(quoteNo);
			docService.copyOldDocumentsToNewQuote(oldDocCopyReq);

			// uploaded Documents delete

			List<ProductEmployeeDetails> pass = productRepo.findByQuoteNoAndSectionIdAndStatusNot(quoteNo,
					sectionId.toString(), "D");
			List<DocumentTransactionDetails> doc1 = docTransRepo.findByQuoteNoAndSectionId(quoteNo, sectionId);

			if (pass.size() > 0) {
				if (doc1.size() > 0) {

					List<DocumentTransactionDetails> filterDoc = doc1.stream()
							.filter(d -> pass.stream().filter(o -> !o.getStatus().equalsIgnoreCase("D"))
									.map(ProductEmployeeDetails::getSectionId)
									.anyMatch(e -> e.equals(d.getSectionId().toString())))
							.filter(d -> pass.stream().filter(o -> !o.getStatus().equalsIgnoreCase("D"))
									.map(ProductEmployeeDetails::getNationalityId).anyMatch(e -> e.equals(d.getId())))
							.collect(Collectors.toList());
					if (filterDoc.size() > 0) {
						List<DocumentTransactionDetails> del = doc1;
						del.removeAll(filterDoc);
						docTransRepo.deleteAll(del);
					} else
						docTransRepo.deleteAll(doc1);
				}

			} else {

				docTransRepo.deleteAll(doc1);
			}

			res.setSuccessId("1");
			res.setResponse("Updated Successfully");

		} catch (Exception e) {
			e.printStackTrace();
			log.info("Log Details" + e.getMessage());
			return null;
		}
		return res;
	}

	public List<PremiumGroupDevideRes> getDevidedCovers(String quoteNo, BigDecimal groupCount,
			List<PremiumGroupDevideRes> groupCovers) {
		List<PremiumGroupDevideRes> devidedCovers = new ArrayList<PremiumGroupDevideRes>();

		try {
			for (PremiumGroupDevideRes cover : groupCovers) {
				cover.setActualRate(
						cover.getActualRate() == null ? null : getDevidedValue(cover.getActualRate(), groupCount));
				cover.setMaxLodingAmount(cover.getMaxLodingAmount() == null ? null
						: getDevidedValue(cover.getMaxLodingAmount(), groupCount));
				cover.setMinimumPremium(cover.getMinimumPremium() == null ? null
						: getDevidedValue(cover.getMinimumPremium(), groupCount));
				cover.setPremiumAfterDiscountFc(cover.getPremiumAfterDiscountFc() == null ? null
						: getDevidedValue(cover.getPremiumAfterDiscountFc(), groupCount));
				cover.setPremiumAfterDiscountLc(cover.getPremiumAfterDiscountLc() == null ? null
						: getDevidedValue(cover.getPremiumAfterDiscountLc(), groupCount));
				cover.setPremiumBeforeDiscountFc(cover.getPremiumBeforeDiscountFc() == null ? null
						: getDevidedValue(cover.getPremiumBeforeDiscountFc(), groupCount));
				cover.setPremiumBeforeDiscountLc(cover.getPremiumBeforeDiscountLc() == null ? null
						: getDevidedValue(cover.getPremiumBeforeDiscountLc(), groupCount));
				cover.setPremiumExcludedTaxFc(cover.getPremiumExcludedTaxFc() == null ? null
						: getDevidedValue(cover.getPremiumExcludedTaxFc(), groupCount));
				cover.setPremiumExcludedTaxLc(cover.getPremiumExcludedTaxLc() == null ? null
						: getDevidedValue(cover.getPremiumExcludedTaxLc(), groupCount));
				cover.setPremiumIncludedTaxFc(cover.getPremiumIncludedTaxFc() == null ? null
						: getDevidedValue(cover.getPremiumIncludedTaxFc(), groupCount));
				cover.setPremiumIncludedTaxLc(cover.getPremiumIncludedTaxLc() == null ? null
						: getDevidedValue(cover.getPremiumIncludedTaxLc(), groupCount));
				cover.setRate(cover.getRate() == null ? null : getDevidedValue(cover.getRate(), groupCount));
				cover.setRegulSumInsured(cover.getRegulSumInsured() == null ? null
						: getDevidedValue(cover.getRegulSumInsured(), groupCount));
				cover.setSumInsured(
						cover.getSumInsured() == null ? null : getDevidedValue(cover.getSumInsured(), groupCount));
				cover.setTaxAmount(
						cover.getTaxAmount() == null ? null : getDevidedValue(cover.getTaxAmount(), groupCount));
				cover.setTaxRate(cover.getTaxRate() == null ? null : getDevidedValue(cover.getTaxRate(), groupCount));
				devidedCovers.add(cover);
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.info("Log Details" + e.getMessage());
			return devidedCovers;
		}
		return devidedCovers;
	}

	private synchronized BigDecimal getDevidedValue(BigDecimal inputValue, BigDecimal groupCount) {
		BigDecimal devidedValue = BigDecimal.ZERO;
		try {
			devidedValue = inputValue.divide(groupCount, 2, RoundingMode.HALF_UP);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception is ---> " + e.getMessage());
			return null;
		}

		return devidedValue;
	}

	public synchronized Integer currencyDecimalFormat(String insuranceId, String currencyId) {
		Integer decimalFormat = 0;
		try {
			Date today = new Date();
			Calendar cal = new GregorianCalendar();
			cal.setTime(today);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 1);
			today = cal.getTime();
			cal.set(Calendar.HOUR_OF_DAY, 1);
			cal.set(Calendar.MINUTE, 1);
			Date todayEnd = cal.getTime();

			// Criteria
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<CurrencyMaster> query = cb.createQuery(CurrencyMaster.class);
			List<CurrencyMaster> list = new ArrayList<CurrencyMaster>();

			// Find All
			Root<CurrencyMaster> c = query.from(CurrencyMaster.class);

			// Select
			query.select(c);

			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(c.get("currencyName")));

			// Effective Date Max Filter
			Subquery<Date> effectiveDate = query.subquery(Date.class);
			Root<CurrencyMaster> ocpm1 = effectiveDate.from(CurrencyMaster.class);
			effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
			Predicate a11 = cb.equal(c.get("currencyId"), ocpm1.get("currencyId"));
			Predicate a12 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
			Predicate a18 = cb.equal(c.get("status"), ocpm1.get("status"));
			Predicate a22 = cb.equal(c.get("companyId"), ocpm1.get("companyId"));

			effectiveDate.where(a11, a12, a18, a22);

			// Effective Date Max Filter
			Subquery<Date> effectiveDate2 = query.subquery(Date.class);
			Root<CurrencyMaster> ocpm2 = effectiveDate2.from(CurrencyMaster.class);
			effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
			Predicate a13 = cb.equal(c.get("currencyId"), ocpm2.get("currencyId"));
			Predicate a14 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
			Predicate a19 = cb.equal(c.get("status"), ocpm2.get("status"));
			Predicate a23 = cb.equal(c.get("companyId"), ocpm2.get("companyId"));

			effectiveDate2.where(a13, a14, a19, a23);

			// Where
			Predicate n1 = cb.equal(c.get("status"), "Y");
			Predicate n2 = cb.equal(c.get("effectiveDateStart"), effectiveDate);
			Predicate n3 = cb.equal(c.get("effectiveDateEnd"), effectiveDate2);
			Predicate n4 = cb.equal(c.get("companyId"), insuranceId);
			Predicate n5 = cb.equal(c.get("companyId"), "99999");
			Predicate n6 = cb.or(n4, n5);
			Predicate n7 = cb.equal(c.get("currencyId"), currencyId);
			query.where(n1, n2, n3, n6, n7).orderBy(orderList);

			// Get Result
			TypedQuery<CurrencyMaster> result = em.createQuery(query);
			list = result.getResultList();

			decimalFormat = list.size() > 0
					? (list.get(0).getDecimalDigit() == null ? 0 : list.get(0).getDecimalDigit())
					: 0;

		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null;
		}
		return decimalFormat;
	}

	@Override
	public List<String> validateProceedEmployeesDetails(SaveProductDetailsReq req1) {
		List<String> error = new ArrayList<String>();

		try {

			List<ProductEmployeeSaveReq> reqList = req1.getProductEmployeeSaveReq();
			// List<String> nationalityIds = new ArrayList<String>();
			List<EmployeeIdDuplicaionReq> nationalityIds = new ArrayList<EmployeeIdDuplicaionReq>();

			Long row = 0L;
			BigDecimal sumInsured = BigDecimal.ZERO;
			String quoteNo = "";
			List<CommonDataDetails> commonDatas = new ArrayList<CommonDataDetails>();

			if (StringUtils.isBlank(req1.getQuoteNo())) {
				error.add("1155");
//				error.add(new Error("01", "QuoteNo", "Please Enter QuoteNo"));
			} else if (StringUtils.isBlank(req1.getSectionId())) {
				error.add("1154");
//				error.add(new Error("01", "SectionId", "Please Enter SectionId"));
			} else {
				if (reqList != null && reqList.size() > 0) {
					commonDatas = commonRepo.findByQuoteNoAndSectionId(req1.getQuoteNo(), req1.getSectionId());
					commonDatas = commonDatas.stream().filter(o -> !o.getStatus().equalsIgnoreCase("D"))
							.collect(Collectors.toList());
					quoteNo = req1.getQuoteNo();
				} else {
					error.add("1196");
//					error.add(new Error("01", "QuoteNo", "Please Enter Atleat one Employee Details "));
				}
			}

			if (StringUtils.isBlank(req1.getCreatedBy())) {
				error.add("1197");
//				error.add(new Error("01", "CreatedBy", "Please Enter CreatedBy"));
			}

			for (ProductEmployeeSaveReq req : reqList) {
				row = row + 1;
				if (StringUtils.isBlank(req.getOccupationId())) {
					error.add("1198" + "," + row);
//					error.add(new Error("01", "Occupation", "Please Select Occupation in Row : " + row));
				}
				if (StringUtils.isBlank(req.getOccupationDesc())) {
					error.add("2302" + "," + row);
//					error.add(new Error("01", "Occupation", "Please Select Occupation in Row : " + row));
				}

				if (StringUtils.isBlank(req.getNationalityId())) {
					if ((!req1.getSectionId().equals("45") && (!req1.getSectionId().equals("43"))
							&& !req1.getProductId().equals("57"))) {

						error.add("1199" + "," + row);
//					error.add(new Error("01", "NationalityId", "Please Select NationalityId in Row : " + row));
					}
				} else {

					// List<String> filterIds = nationalityIds.stream().filter( o ->
					// (o.equalsIgnoreCase(req.getNationalityId())) && ()
					// ).collect(Collectors.toList());
					List<EmployeeIdDuplicaionReq> filterIds = nationalityIds.stream()
							.filter(o -> (o.getNationalityId().equalsIgnoreCase(req.getNationalityId()))
									&& (o.getLocationId().equalsIgnoreCase(req.getLocationId())))
							.collect(Collectors.toList());

					/*
					 * if(filterIds.size() >0 ) { error.add("1200"+"," + row); // error.add(new
					 * Error("01", "NationalityId", "Duplicate NationalityId in Row : " + row)); }
					 * else {
					 */
					EmployeeIdDuplicaionReq nation = new EmployeeIdDuplicaionReq();
					nation.setLocationId(req.getLocationId());
					nation.setNationalityId(req.getNationalityId());
					nationalityIds.add(nation);
					/* } */

				}

//				if((!req1.getSectionId().equals("45" ) )) {
//				if (StringUtils.isBlank(req.getLocationId())) {
//					error.add("1201"+","+row);
////					error.add(new Error("01", "LocationId", "Please Select LocationId in Row : " + row));
//				}
//				}
				if ((!req1.getSectionId().equals("45") && !req1.getProductId().equals("57"))) {
					// Date Of Birth Validation
					Calendar cal = Calendar.getInstance();
					Date today = new Date();
					cal.setTime(today);
					cal.add(Calendar.DAY_OF_MONTH, -1);
					cal.set(Calendar.HOUR_OF_DAY, 23);
					cal.set(Calendar.MINUTE, 50);
					today = cal.getTime();

					/*
					 * if (req.getDateOfBirth() == null) { error.add("1202"+","+row); //
					 * error.add(new Error("38", "DateOfBirth",
					 * "Please Select Date Of Birth in Row : " + row));
					 * 
					 * } else if (req.getDateOfBirth().after(today)) { error.add("1203"+","+row); //
					 * error.add(new Error("38", "DateOfBirth",
					 * "Please Select Date Of Birth as Past Date in Row : " + row));
					 * 
					 * } else { LocalDate localDate1 =
					 * req.getDateOfBirth().toInstant().atZone(ZoneId.systemDefault())
					 * .toLocalDate(); LocalDate localDate2 =
					 * today.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					 * 
					 * Integer years = Period.between(localDate1, localDate2).getYears(); if (years
					 * > 100) { error.add("1204"+","+row); // error.add(new Error("38",
					 * "DateOfBirth", "Date Of Birth Not Accepted More than 100 Years in Row : " +
					 * row));
					 * 
					 * } else if (years < 18) { error.add("1205"+","+row); // error.add(new
					 * Error("38", "DateOfBirth",
					 * "Date Of Birth Not Accepted Less than 18 Years For Induvidual in Row : " +
					 * row));
					 * 
					 * }
					 * 
					 * }
					 */
				}
				// Date Of Joining
				int year = Calendar.getInstance().get(Calendar.YEAR);

				if ((!req1.getSectionId().equals("45") && !req1.getSectionId().equals("43")
						&& !req1.getProductId().equals("57"))) {

					if (StringUtils.isBlank(req.getDateOfJoiningYear())) {
						error.add("1206" + "," + row);
//					error.add(new Error("33", "DateOfJoiningYear", "Please Enter Date Of Joining Year in Row : " + row));

					} else if (StringUtils.isNotBlank(req.getDateOfJoiningYear())) {
						if (req.getDateOfJoiningYear().matches("[0-9]+")) {
							Integer year1 = Integer.valueOf(req.getDateOfJoiningYear());
							Integer diff = year - year1;
							if ((StringUtils.isNotBlank(req.getDateOfJoiningYear())
									&& !req.getDateOfJoiningYear().matches("[0-9]+"))
									|| req.getDateOfJoiningYear().length() > 4) {
								error.add("1207" + "," + row);
//						 error.add(new Error("33", "DateOfJoiningYear", "Please Enter Date Of Joining Format in YYYY in Row : " + row));
							} else if ((StringUtils.isNotBlank(req.getDateOfJoiningYear())
									&& !req.getDateOfJoiningYear().matches("[0-9]+"))
									|| req.getDateOfJoiningYear().length() < 4) {
								error.add("1207" + "," + row);
//						error.add(new Error("33", "DateOfJoiningYear","Please Enter Date Of Joining Format in YYYY  in Row : " + row));
							} else if (StringUtils.isNotBlank(req.getDateOfJoiningYear())) {
								if (year1 > year) {
									error.add("1208" + "," + row);
//							error.add(new Error("33", "DateOfJoiningYear","Please Enter Date Of Joining as Past Year  in Row : " + row));
								} else if (diff > 50) {
									error.add("1209" + "," + row);
//							error.add(new Error("33", "DateOfJoiningYear","Please Enter Date Of Joining within 50 years  in Row : " + row));
								}
							}
						} else if (!req.getDateOfJoiningYear().matches("[0-9]+")) {
							error.add("1207" + "," + row);
						}
					}
				}
				if (StringUtils.isBlank(req.getEmployeeName())) {
					error.add("1210" + "," + row);
//					error.add(new Error("03", "Employee Name ", "Please Enter Employee Name In Row No : " + row));
				} else if ((StringUtils.isNotBlank(req.getEmployeeName()))
						&& !req.getEmployeeName().matches("[A-Z a-z]+")) {
					error.add("1211" + "," + row);
//					error.add(new Error("04", "Employee Name ", "Please Enter Valid Employee Name  In Row No:" + row ));
				}

				// Drop Downs
				if (StringUtils.isBlank(req.getSalary())) {
					error.add("1212" + "," + row);
//					error.add(new Error("08", "Salary", "Please Enter Employee Salary In Row No : " + row));
				} else if (!req.getSalary().matches("[0-9.]+") || Double.valueOf(req.getSalary()) <= 0) {
					error.add("1213" + "," + row);
//					error.add(new Error("09", "Salary", "Please Enter Valid Number In Employee Salary  In Row No : " + row));
				} else {
					sumInsured = sumInsured.add(new BigDecimal(req.getSalary()));
				}

			}

			int checkCount = 0;
			int empCount = 0;
			double totalSi = 0.0;
			double empSi = 0.0;
			boolean temp1 = true;
			int indivcount = 0;
			if (error.size() < 1) {

				empCount = commonDatas.stream().mapToInt(o -> o.getCount().intValue()).sum();
				//Save block --Count can be less
				if(StringUtils.isNotBlank(req1.getSaveOrSubmit()) && "Save".equalsIgnoreCase(req1.getSaveOrSubmit())) { 
					// count
					if (reqList.size() > empCount) {
						error.add("1214" + "," + empCount);
//						error.add(new Error("333", "Employees Count", "Employee's Details Count Should be "+empCount));
					}
				}else {//Submit Block

				// count
				if (reqList.size() > empCount || reqList.size() < empCount) {
					error.add("1214" + "," + empCount);
//					error.add(new Error("333", "Employees Count", "Employee's Details Count Should be "+empCount));
				}
				}
			}
//			if (error.size() < 1) {
//
//				empCount = commonDatas.stream().mapToInt(o -> o.getCount().intValue()).sum();
//
//				// count
//				if (reqList.size() > empCount || reqList.size() < empCount) {
//					error.add("1214" + "," + empCount);
////					error.add(new Error("333", "Employees Count", "Employee's Details Count Should be "+empCount));
//				}
//			}
			if (commonDatas != null&& commonDatas.size()>0) {
				Set<Integer> findlocationid = commonDatas.stream().map(CommonDataDetails::getLocationId).distinct()
						.collect(Collectors.toSet());
				for (Integer l : findlocationid) {
					if (error.size() < 1) {
						// Total si & Individual occupation count
						commonDatas = commonDatas.stream().filter(o -> o.getLocationId().equals(l))
								.collect(Collectors.toList());
						for (CommonDataDetails cdata : commonDatas) {
							indivcount = 0;
							checkCount = 0;
							empSi = 0.0;
							indivcount = indivcount + cdata.getCount().intValue();

							totalSi = cdata.getSumInsured() == null ? 0.0 : cdata.getSumInsured().doubleValue();

							checkCount = (int) reqList.stream().filter(o -> o.getOccupationId() != null
									&& o.getOccupationId().equalsIgnoreCase(cdata.getOccupationType())&& o.getLocationId().equalsIgnoreCase(cdata.getLocationId().toString())).count();
							empSi = reqList.stream()
									.filter(o -> o.getOccupationId() != null
											&& o.getOccupationId().equalsIgnoreCase(cdata.getOccupationType())
											&& o.getLocationId().equalsIgnoreCase(cdata.getLocationId().toString()))
									.mapToDouble(o -> Double.valueOf(o.getSalary())).sum();

							if (indivcount != checkCount) {
								error.add("1215" + "," + indivcount);
//						error.add(new Error("111", "Occupation Count", "Employee Details count should be "+indivcount+" for Occupation "+"'"+cdata.getOccupationDesc()+"'"));
								temp1 = false;
							}
							if (error.size() < 1) {
//								if (totalSi != empSi) {
								//Save Block
								if(StringUtils.isNotBlank(req1.getSaveOrSubmit()) && "Save".equalsIgnoreCase(req1.getSaveOrSubmit())) {
							
									if (totalSi < empSi) {
										error.add("1216" + "," + cdata.getOccupationDesc());
//										error.add(new Error("222", "Sum Insured", "Total SumInsured not equal to the Actual SumInsured for occupation "+"'"+cdata.getOccupationDesc()+"'" ));
										temp1 = false;
									}
								}else {//Sumbit block

								if (totalSi < empSi || totalSi > empSi ) {
									error.add("1216" + "," + cdata.getOccupationDesc());
//									error.add(new Error("222", "Sum Insured", "Total SumInsured not equal to the Actual SumInsured for occupation "+"'"+cdata.getOccupationDesc()+"'" ));
									temp1 = false;
								}
								}
							}
							if (!temp1)
								break;
						}

					}
				}
			}

		} catch (Exception e) {

			log.error(e);
			e.printStackTrace();
			error.add("1185");
//		error.add(new Error("03", "Common Error", e.getMessage()));
		}
		return error;
	}

	@Override
	public SuccessRes proceedEmployeesDetails(SaveProductDetailsReq req) {
		SuccessRes res = new SuccessRes();
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		try {
			List<ProductEmployeeSaveReq> reqList = req.getProductEmployeeSaveReq();

			// Primary Tables & Details
			String quoteNo = req.getQuoteNo();
			Integer sectionId = Integer.valueOf(req.getSectionId());
			List<ProductEmployeeDetails> delete = productRepo.findByQuoteNo(quoteNo);
			if (delete != null && delete.size() > 0) {
				productRepo.deleteAll(delete);
			}
			Integer lastPassCount = 0;
			lastPassCount = Integer.valueOf(productRepo.countByQuoteNo(quoteNo).toString());

			List<CommonDataDetails> groupList = commonRepo.findByQuoteNoAndSectionIdAndStatusNot(quoteNo,
					sectionId.toString(), "D");
			HomePositionMaster homeData = homeRepo.findByQuoteNo(quoteNo);

			List<ProductEmployeeDetails> saveList = new ArrayList<ProductEmployeeDetails>();
			List<PolicyCoverDataIndividuals> totalIndiCovers = new ArrayList<PolicyCoverDataIndividuals>();

			// Grouping By Location Id
			Map<String, List<ProductEmployeeSaveReq>> groupByLocationId = reqList.stream()
					.filter(o -> o.getLocationId() != null)
					.collect(Collectors.groupingBy(ProductEmployeeSaveReq::getLocationId));

			for (String location : groupByLocationId.keySet()) {
				List<ProductEmployeeSaveReq> filterReq = groupByLocationId.get(location);
				Map<String, List<ProductEmployeeSaveReq>> groupByGroupId = filterReq.stream()
						.filter(o -> o.getRiskId() != null)
						.collect(Collectors.groupingBy(ProductEmployeeSaveReq::getRiskId));

				List<PolicyCoverData> coverList = coverRepo.findByQuoteNoAndSectionIdAndLocationId(quoteNo, sectionId,
						Integer.valueOf(location));
				List<PremiumGroupDevideRes> covers = new ArrayList<PremiumGroupDevideRes>();
				Type listType = new TypeToken<List<PremiumGroupDevideRes>>() {
				}.getType();
				covers = modelMapper.map(coverList, listType);
				Map<Integer, List<PremiumGroupDevideRes>> coverGroupByGroupId = covers.stream()
						.filter(o -> o.getVehicleId() != null)
						.collect(Collectors.groupingBy(PremiumGroupDevideRes::getVehicleId));

				// Framing List

				for (String group : groupByGroupId.keySet()) {

					List<ProductEmployeeSaveReq> filterReqPass = groupByGroupId.get(group);
					List<PremiumGroupDevideRes> groupCovers = coverGroupByGroupId.get(Integer.valueOf(group));
					List<CommonDataDetails> filterGroupList = groupList.stream()
							.filter(o -> o.getRiskId().equals(Integer.valueOf(group))
									&& o.getLocationId().equals(Integer.valueOf(location)))
							.collect(Collectors.toList());
					CommonDataDetails groupData = new CommonDataDetails();
					if (filterGroupList.size() > 0) {
						groupData = filterGroupList.get(0);
					}

					for (ProductEmployeeSaveReq data : filterReqPass) {
						ProductEmployeeDetails saveData = new ProductEmployeeDetails();
						Integer empId = 0;

						if (StringUtils.isNotBlank(data.getEmployeeId())) {
							empId = Integer.valueOf(data.getEmployeeId());
						} else {
							lastPassCount = lastPassCount + 1;
							empId = lastPassCount;
						}
						Integer passCount = empId;

						// Employee Details
						saveData.setSectionId(sectionId.toString());
						saveData.setDateOfBirth(data.getDateOfBirth() == null ? null : data.getDateOfBirth());
						saveData.setDateOfJoiningYear(data.getDateOfJoiningYear() == null ? null
								: Integer.valueOf(data.getDateOfJoiningYear()));
						saveData.setDateOfJoiningMonth(
								data.getDateOfJoiningMonth() == null ? null : data.getDateOfJoiningMonth());
						saveData.setCompanyId(homeData.getCompanyId());
						saveData.setCreatedBy(req.getCreatedBy());
						saveData.setEmployeeId(Long.valueOf(empId));
						saveData.setEmployeeName(data.getEmployeeName());
						saveData.setEntryDate(new Date());
						saveData.setOccupationId(data.getOccupationId());
						saveData.setOccupationDesc(data.getOccupationDesc());
						saveData.setProductId(homeData.getProductId());
						saveData.setQuoteNo(quoteNo);
						saveData.setRequestReferenceNo(homeData.getRequestReferenceNo());
						saveData.setRiskId(Integer.valueOf(data.getRiskId()));
						saveData.setLocationId(
								(data.getLocationId() == null || StringUtils.isBlank(data.getLocationId()))
										? Integer.valueOf(data.getRiskId())
										: Integer.valueOf(data.getLocationId()));
						saveData.setLocationName(data.getLocationName());
						saveData.setStatus("Y");
						saveData.setSalary(new BigDecimal(data.getSalary()));
						saveData.setNationalityId(
								(data.getNationalityId() == null || StringUtils.isBlank(data.getNationalityId())) ? "0"
										: data.getNationalityId());
						saveData.setProductDesc(homeData.getProductName());
						saveData.setAddress(data.getAddress());
						saveData.setSectionDesc(groupData.getSectionDesc());
						saveData.setLocationName(data.getLocationName());

						// Premium Details
						saveData.setPolicyStartDate(groupData.getPolicyStartDate());
						saveData.setPolicyEndDate(groupData.getPolicyEndDate());

						BigDecimal individualSuminsured = new BigDecimal(data.getSalary());

//						BigDecimal sharePercent = new BigDecimal(groupData.getSumInsured().toString())

//						BigDecimal sharePercent = new BigDecimal(groupData.getEmpLiabilitySi())
//								.divide(individualSuminsured, 4, RoundingMode.HALF_UP);
						
						BigDecimal sharePercent = (groupData.getSumInsured())

								.divide(individualSuminsured, 4, RoundingMode.HALF_UP);


						List<PremiumGroupDevideRes> getDividedCovers = getDevidedCovers(quoteNo, sharePercent,
								groupCovers);
						Double rate = getDividedCovers.stream()
								.filter(o -> o.getTaxId().equals(0) && o.getDiscLoadId().equals(0)
										&& o.getRate() != null && o.getRate().doubleValue() > 0D)
								.mapToDouble(o -> o.getRate().doubleValue()).sum();
						Double overAllPremiumFc = getDividedCovers.stream()
								.filter(o -> o.getTaxId().equals(0) && o.getDiscLoadId().equals(0)
										&& o.getPremiumIncludedTaxFc() != null
										&& o.getPremiumIncludedTaxFc().doubleValue() > 0D)
								.mapToDouble(o -> o.getPremiumIncludedTaxFc().doubleValue()).sum();
						Double overAllPremiumLc = getDividedCovers.stream()
								.filter(o -> o.getTaxId().equals(0) && o.getDiscLoadId().equals(0)
										&& o.getPremiumIncludedTaxLc() != null
										&& o.getPremiumIncludedTaxLc().doubleValue() > 0D)
								.mapToDouble(o -> o.getPremiumIncludedTaxLc().doubleValue()).sum();

						saveData.setRate(rate);
						saveData.setExchangeRate(groupData.getExchangeRate() == null ? null
								: Double.valueOf(groupData.getExchangeRate().toString()));
						saveData.setPremiumFc(overAllPremiumFc);
						saveData.setPremiumLc(overAllPremiumLc);
						saveData.setCurrencyCode(groupData.getCurrency());

						saveList.add(saveData);

						// Covers
						List<PolicyCoverDataIndividuals> indiCovers = new ArrayList<PolicyCoverDataIndividuals>();
						Type listType2 = new TypeToken<List<PolicyCoverDataIndividuals>>() {
						}.getType();
						indiCovers = modelMapper.map(getDividedCovers, listType2);
						for (PolicyCoverDataIndividuals o : indiCovers) {
							o.setGroupId(groupData.getRiskId());
							o.setGroupCount(groupData.getCount());
							o.setVehicleId(passCount);
							o.setIndividualId(passCount);
							o.setLocationId(Integer.valueOf(location));
						}
						totalIndiCovers.addAll(indiCovers);
					}

				}
			}

			// Save All Passengers
			List<Integer> passengerIds = new ArrayList<Integer>();
			List<Long> passengerId2 = new ArrayList<Long>();
			saveList.forEach(o -> {
				passengerIds.add(Integer.valueOf(o.getEmployeeId().toString()));
				passengerId2.add(o.getEmployeeId());
			});
//			Long empCount = productRepo.countByQuoteNoAndSectionIdAndEmployeeIdInAndStatusNot(quoteNo,
//					sectionId.toString(), passengerId2, "D");
//			if (empCount > 0) {
//				productRepo.deleteByQuoteNoAndSectionIdAndEmployeeIdInAndStatusNot(quoteNo, sectionId.toString(),
//						passengerId2, "D");
//			}
			productRepo.saveAllAndFlush(saveList);

			// Save Divided Indi Covers
//			updateRemovedPassengers(quoteNo, passengerIds, sectionId, groupList.get(0).getPolicyStartDate());
			indiCoverRepo.saveAllAndFlush(totalIndiCovers);

			// Copy Old Doc
			/*
			 * OldDocumentsCopyReq oldDocCopyReq = new OldDocumentsCopyReq();
			 * oldDocCopyReq.setInsuranceId(homeData.getCompanyId());
			 * oldDocCopyReq.setProductId(homeData.getProductId().toString());
			 * oldDocCopyReq.setSectionId(sectionId.toString());
			 * oldDocCopyReq.setQuoteNo(quoteNo);
			 * docService.copyOldDocumentsToNewQuote(oldDocCopyReq);
			 * 
			 * // uploaded Documents delete
			 * 
			 * List<ProductEmployeeDetails> pass =
			 * productRepo.findByQuoteNoAndSectionId(quoteNo, sectionId.toString());
			 * List<DocumentTransactionDetails> doc1 =
			 * docTransRepo.findByQuoteNoAndSectionId(quoteNo, sectionId);
			 * 
			 * if (pass.size() > 0) { if (doc1.size() > 0) {
			 * 
			 * List<DocumentTransactionDetails> filterDoc = doc1.stream() .filter(d ->
			 * pass.stream().filter(o -> !o.getStatus().equalsIgnoreCase("D"))
			 * .map(ProductEmployeeDetails::getSectionId) .anyMatch(e ->
			 * e.equals(d.getSectionId().toString()))) .filter(d -> pass.stream().filter(o
			 * -> !o.getStatus().equalsIgnoreCase("D"))
			 * .map(ProductEmployeeDetails::getNationalityId).anyMatch(e ->
			 * e.equals(d.getId()))) .collect(Collectors.toList()); if (filterDoc.size() >
			 * 0) { List<DocumentTransactionDetails> del = doc1; del.removeAll(filterDoc);
			 * docTransRepo.deleteAll(del); } else docTransRepo.deleteAll(doc1); }
			 * 
			 * } else {
			 * 
			 * docTransRepo.deleteAll(doc1); }
			 */

			res.setResponse("Saved Successfully");
			res.setSuccessId("");

		} catch (Exception e) {
			e.printStackTrace();
			log.info("Log Details" + e.getMessage());
			return null;
		}

		return res;

	}

	/*
	 * @Override public SuccessRes proceedEmployeesDetails(SaveProductDetailsReq
	 * req) { SuccessRes res = new SuccessRes(); ModelMapper modelMapper = new
	 * ModelMapper(); modelMapper.getConfiguration().setAmbiguityIgnored(true); try
	 * { List<ProductEmployeeSaveReq> reqList = req.getProductEmployeeSaveReq(); //
	 * Primary Tables & Details String quoteNo = req.getQuoteNo(); Integer sectionId
	 * = Integer.valueOf(req.getSectionId()); Integer lastPassCount =
	 * Integer.valueOf( productRepo.countByQuoteNo(quoteNo).toString());
	 * List<PolicyCoverData> coverList = coverRepo.findByQuoteNoAndSectionId(quoteNo
	 * , sectionId ); List<PremiumGroupDevideRes> covers = new
	 * ArrayList<PremiumGroupDevideRes>(); Type listType = new
	 * TypeToken<List<PremiumGroupDevideRes>>(){}.getType(); covers =
	 * modelMapper.map(coverList,listType); List<CommonDataDetails> groupList =
	 * commonRepo.findByQuoteNoAndSectionIdAndStatusNot(quoteNo
	 * ,sectionId.toString(), "D" ); HomePositionMaster homeData =
	 * homeRepo.findByQuoteNo(quoteNo);
	 * 
	 * // Grouping // Map<String, List<ProductEmployeeSaveReq>> groupByGroupId =
	 * reqList.stream().filter( o -> o.getOccupationId() !=null ).collect(
	 * Collectors.groupingBy(ProductEmployeeSaveReq :: getOccupationId )) ;
	 * Map<String, List<ProductEmployeeSaveReq>> groupByGroupId =
	 * reqList.stream().filter( o -> o.getRiskId() !=null ).collect(
	 * Collectors.groupingBy(ProductEmployeeSaveReq :: getRiskId )) ; Map<Integer,
	 * List<PremiumGroupDevideRes>> coverGroupByGroupId = covers.stream().filter( o
	 * -> o.getVehicleId() !=null ).collect(
	 * Collectors.groupingBy(PremiumGroupDevideRes :: getVehicleId )) ;
	 * 
	 * // Framing List List<PolicyCoverDataIndividuals> totalIndiCovers = new
	 * ArrayList<PolicyCoverDataIndividuals>();
	 * 
	 * List<ProductEmployeeDetails> saveList = new
	 * ArrayList<ProductEmployeeDetails>(); for (String group :
	 * groupByGroupId.keySet() ) {
	 * 
	 * 
	 * List<ProductEmployeeSaveReq> filterReqPass = groupByGroupId.get(group);
	 * List<PremiumGroupDevideRes> groupCovers =
	 * coverGroupByGroupId.get(Integer.valueOf(group)); List<CommonDataDetails>
	 * filterGroupList = groupList.stream().filter( o ->
	 * o.getRiskId().equals(Integer.valueOf(group))).collect(Collectors.toList());
	 * CommonDataDetails groupData = new CommonDataDetails();
	 * if(filterGroupList.size() > 0) { groupData = filterGroupList.get(0); }
	 * 
	 * for(ProductEmployeeSaveReq data : filterReqPass ) { ProductEmployeeDetails
	 * saveData = new ProductEmployeeDetails(); Integer empId = 0 ;
	 * 
	 * if (StringUtils.isNotBlank(data.getEmployeeId())) { empId =
	 * Integer.valueOf(data.getEmployeeId()) ; }else { lastPassCount = lastPassCount
	 * + 1 ; empId = lastPassCount ; } Integer passCount = empId ;
	 * 
	 * // Employee Details saveData.setSectionId(sectionId.toString());
	 * saveData.setDateOfBirth(
	 * data.getDateOfBirth()==null?null:data.getDateOfBirth());
	 * saveData.setDateOfJoiningYear(data.getDateOfJoiningYear()==null?null:Integer.
	 * valueOf(data.getDateOfJoiningYear()) );
	 * saveData.setDateOfJoiningMonth(data.getDateOfJoiningMonth()==null?null:data.
	 * getDateOfJoiningMonth()); saveData.setCompanyId(homeData.getCompanyId());
	 * saveData.setCreatedBy(req.getCreatedBy());
	 * saveData.setEmployeeId(Long.valueOf(empId));
	 * saveData.setEmployeeName(data.getEmployeeName()); saveData.setEntryDate(new
	 * Date()); saveData.setOccupationId(data.getOccupationId());
	 * saveData.setOccupationDesc(data.getOccupationDesc());
	 * saveData.setProductId(homeData.getProductId()); saveData.setQuoteNo(quoteNo);
	 * saveData.setRequestReferenceNo(homeData.getRequestReferenceNo());
	 * saveData.setRiskId(Integer.valueOf(data.getRiskId()));
	 * saveData.setLocationId((data.getLocationId()==null ||
	 * StringUtils.isBlank(data.getLocationId()))?Integer.valueOf(data.getRiskId()):
	 * Integer.valueOf(data.getLocationId()));
	 * saveData.setLocationName(data.getLocationName()); saveData.setStatus("Y");
	 * saveData.setSalary(new BigDecimal(data.getSalary()));
	 * saveData.setNationalityId((data.getNationalityId()==null
	 * ||StringUtils.isBlank(data.getNationalityId()))?"0":data.getNationalityId());
	 * saveData.setProductDesc(homeData.getProductName());
	 * saveData.setAddress(data.getAddress());
	 * saveData.setSectionDesc(groupData.getSectionDesc());
	 * saveData.setLocationName(data.getLocationName());
	 * 
	 * // Premium Details
	 * saveData.setPolicyStartDate(groupData.getPolicyStartDate());
	 * saveData.setPolicyEndDate(groupData.getPolicyEndDate());
	 * 
	 * BigDecimal individualSuminsured = new BigDecimal(data.getSalary());
	 * BigDecimal sharePercent =
	 * groupData.getSumInsured().divide(individualSuminsured
	 * ,4,RoundingMode.HALF_UP);
	 * 
	 * List<PremiumGroupDevideRes> getDividedCovers = getDevidedCovers(quoteNo ,
	 * sharePercent, groupCovers ) ; Double rate = getDividedCovers.stream().filter(
	 * o -> o.getTaxId().equals(0) && o.getDiscLoadId().equals(0) &&
	 * o.getRate()!=null && o.getRate().doubleValue() > 0D ).mapToDouble( o ->
	 * o.getRate().doubleValue() ).sum(); Double overAllPremiumFc =
	 * getDividedCovers.stream().filter( o -> o.getTaxId().equals(0) &&
	 * o.getDiscLoadId().equals(0) && o.getPremiumIncludedTaxFc()!=null &&
	 * o.getPremiumIncludedTaxFc().doubleValue() > 0D ).mapToDouble( o ->
	 * o.getPremiumIncludedTaxFc().doubleValue() ).sum(); Double overAllPremiumLc =
	 * getDividedCovers.stream().filter( o -> o.getTaxId().equals(0) &&
	 * o.getDiscLoadId().equals(0) && o.getPremiumIncludedTaxLc()!=null &&
	 * o.getPremiumIncludedTaxLc().doubleValue() > 0D ).mapToDouble( o ->
	 * o.getPremiumIncludedTaxLc().doubleValue() ).sum();
	 * 
	 * saveData.setRate(rate);
	 * saveData.setExchangeRate(groupData.getExchangeRate()==null?null:Double.
	 * valueOf(groupData.getExchangeRate().toString()));
	 * saveData.setPremiumFc(overAllPremiumFc);
	 * saveData.setPremiumLc(overAllPremiumLc);
	 * saveData.setCurrencyCode(groupData.getCurrency());
	 * 
	 * saveList.add(saveData);
	 * 
	 * 
	 * // Covers List<PolicyCoverDataIndividuals> indiCovers = new
	 * ArrayList<PolicyCoverDataIndividuals>(); Type listType2 = new
	 * TypeToken<List<PolicyCoverDataIndividuals>>(){}.getType(); indiCovers =
	 * modelMapper.map(getDividedCovers,listType2); for( PolicyCoverDataIndividuals
	 * o : indiCovers ) { o.setGroupId(groupData.getRiskId());
	 * o.setGroupCount(groupData.getCount()); o.setVehicleId(passCount);
	 * o.setIndividualId(passCount); } totalIndiCovers.addAll(indiCovers); }
	 * 
	 * }
	 * 
	 * // Save All Passengers List<Integer> passengerIds = new ArrayList<Integer>();
	 * List<Long> passengerId2 = new ArrayList<Long>(); saveList.forEach( o -> {
	 * passengerIds.add(Integer.valueOf(o.getEmployeeId().toString())) ;
	 * passengerId2.add(o.getEmployeeId()) ; }); Long empCount =
	 * productRepo.countByQuoteNoAndSectionIdAndEmployeeIdInAndStatusNot(quoteNo,
	 * sectionId.toString(),passengerId2,"D"); if(empCount > 0 ) {
	 * productRepo.deleteByQuoteNoAndSectionIdAndEmployeeIdInAndStatusNot(quoteNo,
	 * sectionId.toString(), passengerId2,"D"); }
	 * productRepo.saveAllAndFlush(saveList);
	 * 
	 * // Save Divided Indi Covers updateRemovedPassengers(quoteNo ,passengerIds
	 * ,sectionId , groupList.get(0).getPolicyStartDate()) ;
	 * indiCoverRepo.saveAllAndFlush(totalIndiCovers);
	 * 
	 * // Copy Old Doc OldDocumentsCopyReq oldDocCopyReq = new
	 * OldDocumentsCopyReq(); oldDocCopyReq.setInsuranceId(homeData.getCompanyId());
	 * oldDocCopyReq.setProductId(homeData.getProductId().toString());
	 * oldDocCopyReq.setSectionId(sectionId.toString());
	 * oldDocCopyReq.setQuoteNo(quoteNo);
	 * docService.copyOldDocumentsToNewQuote(oldDocCopyReq);
	 * 
	 * //uploaded Documents delete
	 * 
	 * List<ProductEmployeeDetails> pass =
	 * productRepo.findByQuoteNoAndSectionId(quoteNo,sectionId.toString());
	 * List<DocumentTransactionDetails> doc1 =
	 * docTransRepo.findByQuoteNoAndSectionId(quoteNo, sectionId);
	 * 
	 * if(pass.size()>0) { if(doc1.size()>0) {
	 * 
	 * List<DocumentTransactionDetails> filterDoc = doc1.stream() .filter( d ->
	 * pass.stream().filter( o -> ! o.getStatus().equalsIgnoreCase("D")) .map(
	 * ProductEmployeeDetails :: getSectionId).anyMatch( e -> e.equals(
	 * d.getSectionId().toString()))) .filter( d -> pass.stream().filter( o -> !
	 * o.getStatus().equalsIgnoreCase("D")).map( ProductEmployeeDetails ::
	 * getNationalityId).anyMatch( e -> e.equals( d.getId()
	 * ))).collect(Collectors.toList()); if(filterDoc.size()>0) {
	 * List<DocumentTransactionDetails> del = doc1; del.removeAll(filterDoc);
	 * docTransRepo.deleteAll(del); } else docTransRepo.deleteAll(doc1); }
	 * 
	 * 
	 * }else {
	 * 
	 * docTransRepo.deleteAll(doc1); }
	 * 
	 * res.setResponse("Saved Successfully"); res.setSuccessId("");
	 * 
	 * 
	 * } catch (Exception e) { e.printStackTrace(); log.info("Log Details" +
	 * e.getMessage()); return null; }
	 * 
	 * return res;
	 * 
	 * }
	 * 
	 */
	public String updateRemovedPassengers(String quoteNo, List<Integer> passengerIds, Integer sectionId,
			Date startDate) {
		String res = "";
		try {
			{
				// Update Deactivated Passengers
				CriteriaBuilder cb = em.getCriteriaBuilder();
				// create update
				CriteriaUpdate<ProductEmployeeDetails> update = cb.createCriteriaUpdate(ProductEmployeeDetails.class);
				// set the root class
				Root<ProductEmployeeDetails> m = update.from(ProductEmployeeDetails.class);
				update.set("status", "D");
				// In
				Expression<String> e0 = m.get("employeeId");
				Predicate n1 = cb.equal(m.get("quoteNo"), quoteNo);
				Predicate n2 = e0.in(passengerIds).not();
				Predicate n3 = cb.equal(m.get("sectionId"), sectionId);
				update.where(n1, n2, n3);
				em.createQuery(update).executeUpdate();

			}

			// Remove Deactivated Covers
			Long count = indiCoverRepo.countByQuoteNoAndSectionIdAndVehicleIdIn(quoteNo, sectionId, passengerIds);
			if (count > 0) {
				indiCoverRepo.deleteByQuoteNoAndSectionIdAndVehicleIdIn(quoteNo, sectionId, passengerIds);
			}
			{
				// Date Diffrence
				Date periodStart = startDate;
				Date endDate = new Date();
				Long daysBetween = 0L;
				String diff = "";

				Long diffInMillies = Math.abs(endDate.getTime() - periodStart.getTime());
				daysBetween = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS) + 1;

				diff = String.valueOf(daysBetween);

				// Update Deactivated Passengers
				CriteriaBuilder cb = em.getCriteriaBuilder();
				// create update
				CriteriaUpdate<PolicyCoverDataIndividuals> update = cb
						.createCriteriaUpdate(PolicyCoverDataIndividuals.class);
				// set the root class
				Root<PolicyCoverDataIndividuals> m = update.from(PolicyCoverDataIndividuals.class);
				update.set("status", "D");
				update.set("noOfDays", new BigDecimal(diff));
				update.set("coverPeriodTo", endDate);
				// In
				Expression<String> e0 = m.get("vehicleId");
				Predicate n1 = cb.equal(m.get("quoteNo"), quoteNo);
				Predicate n2 = e0.in(passengerIds).not();
				Predicate n3 = cb.equal(m.get("sectionId"), sectionId);
				update.where(n1, n2, n3);
				em.createQuery(update).executeUpdate();

			}

			res = "Success";
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Log Details" + e.getMessage());
			return null;
		}
		return res;
	}

	@Override
	public List<ProductEmployeeGetRes> getallActiveEmployeesDetails(ProductEmployeesGetReq req) {
		// TODO Auto-generated method stub
		List<ProductEmployeeGetRes> resList = new ArrayList<ProductEmployeeGetRes>();
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		try {
			List<ProductEmployeeDetails> datas = productRepo.findByQuoteNoAndSectionIdAndStatusNotOrderByEmployeeIdAsc(
					req.getQuoteNo(), req.getSectionId(), "D");
			Type listType = new TypeToken<List<ProductEmployeeGetRes>>() {
			}.getType();
			resList = modelMapper.map(datas, listType);

			resList.forEach(o -> {
				o.setSalary(StringUtils.isBlank(o.getSalary()) ? "" : new BigDecimal(o.getSalary()).toPlainString());
			});

		} catch (Exception e) {
			e.printStackTrace();
			log.info("Log Details" + e.getMessage());
			return null;
		}
		return resList;
	}

	@Override
	public List<ProductEmployeeGetRes> getallRemovedEmployeesDetails(ProductEmployeesGetReq req) {
		// TODO Auto-generated method stub
		List<ProductEmployeeGetRes> resList = new ArrayList<ProductEmployeeGetRes>();
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		try {
			List<ProductEmployeeDetails> datas = productRepo
					.findByQuoteNoAndSectionIdAndStatusOrderByEmployeeIdAsc(req.getQuoteNo(), req.getSectionId(), "D");
			Type listType = new TypeToken<List<ProductEmployeeGetRes>>() {
			}.getType();
			resList = modelMapper.map(datas, listType);

			resList.forEach(o -> {
				o.setSalary(StringUtils.isBlank(o.getSalary()) ? "" : new BigDecimal(o.getSalary()).toPlainString());
			});
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Log Details" + e.getMessage());
			return null;
		}
		return resList;
	}

	@Override
	public SuccessRes deleteAllFidelityEmployees(ProductEmployeesGetReq req) {
		SuccessRes res = new SuccessRes();
		try {

			// List<ProductEmployeeDetailsArch> savearchList = new
			// ArrayList<ProductEmployeeDetailsArch>();

			List<ProductEmployeeDetails> old = productRepo.findByQuoteNoAndSectionIdAndStatusNot(req.getQuoteNo(),
					req.getSectionId(), "D");
			old.forEach(o -> {
				o.setStatus("D");
			});

			productRepo.saveAllAndFlush(old);
			// docTransRepo.deleteAll(oldDoc);

			res.setResponse("Deleted Successfully");
			res.setSuccessId(req.getQuoteNo());
			;

		} catch (Exception e) {
			e.printStackTrace();
			log.info("Log Details" + e.getMessage());
			return null;
		}
		return res;
	}

}
