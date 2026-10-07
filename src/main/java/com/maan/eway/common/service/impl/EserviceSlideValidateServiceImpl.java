
package com.maan.eway.common.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.eway.bean.BrokerCommissionDetails;
import com.maan.eway.bean.CompanyProductMaster;
import com.maan.eway.bean.CurrencyMaster;
import com.maan.eway.bean.EserviceBuildingDetails;
import com.maan.eway.bean.EserviceCommonDetails;
import com.maan.eway.bean.EserviceSectionDetails;
import com.maan.eway.bean.ExchangeMaster;
import com.maan.eway.bean.ListItemValue;
import com.maan.eway.bean.LoginMaster;
import com.maan.eway.bean.LoginProductMaster;
import com.maan.eway.bean.ProductEmployeeDetails;
import com.maan.eway.common.req.AccidentDamageSaveRequest;
import com.maan.eway.common.req.AllRiskDetailsReq;
import com.maan.eway.common.req.BondCommonReq;
import com.maan.eway.common.req.BondMajorReq;
import com.maan.eway.common.req.BurglaryAndHouseBreakingSaveReq;
import com.maan.eway.common.req.CommonRequest;
import com.maan.eway.common.req.ContentSaveReq;
import com.maan.eway.common.req.ElectronicEquipSaveReq;
import com.maan.eway.common.req.EngineeringReq;
import com.maan.eway.common.req.FireAndAlliedPerillsSaveReq;
import com.maan.eway.common.req.FireReqData;
import com.maan.eway.common.req.FirstLossPayeeReq;
import com.maan.eway.common.req.MainInfoValidationReq;
import com.maan.eway.common.req.NonMotorLocationReq;
import com.maan.eway.common.req.NonMotorPolicyReq;
import com.maan.eway.common.req.NonMotorSaveReq;
import com.maan.eway.common.req.NonMotorSectionReq;
import com.maan.eway.common.req.SlideBuildingSaveReq;
import com.maan.eway.common.req.SlideBusinessInterruptionReq;
import com.maan.eway.common.req.SlideCommonSaveReq;
import com.maan.eway.common.req.SlideEmpLiabilitySaveReq;
import com.maan.eway.common.req.SlideFidelityGuarantySaveReq;
import com.maan.eway.common.req.SlideFireAndPerillsSaveReq;
import com.maan.eway.common.req.SlideGoodsInTransitSaveReq;
import com.maan.eway.common.req.SlideHIFamilyDetailsReq;
import com.maan.eway.common.req.SlideHealthInsureSaveReq;
import com.maan.eway.common.req.SlideMachineryBreakdownSaveReq;
import com.maan.eway.common.req.SlideMoneySaveReq;
import com.maan.eway.common.req.SlidePersonalAccidentSaveReq;
import com.maan.eway.common.req.SlidePlateGlassSaveReq;
import com.maan.eway.common.req.SlidePublicLiabilitySaveReq;
import com.maan.eway.error.Error;
import com.maan.eway.repository.BrokerCommissionDetailsRepository;
import com.maan.eway.repository.EServiceBuildingDetailsRepository;
import com.maan.eway.repository.EServiceSectionDetailsRepository;
import com.maan.eway.repository.EngineerInfoRepo;
import com.maan.eway.repository.EserviceCommonDetailsRepository;
import com.maan.eway.repository.ListItemValueRepository;
import com.maan.eway.repository.LoginMasterRepository;
import com.maan.eway.repository.LoginProductMasterRepository;
import com.maan.eway.repository.ProductEmployeesDetailsRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

@Service
public class EserviceSlideValidateServiceImpl implements EserviceSlideValidateService {
	
	@Autowired
	private LoginMasterRepository loginRepo;
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private EServiceSectionDetailsRepository secRepo;
	
	@Autowired
	private ProductEmployeesDetailsRepository empRepo;
	
	@Autowired
	private EServiceBuildingDetailsRepository buildRepo;
	
	@Autowired
	private EserviceCommonDetailsRepository commRepo;
	
	@Autowired
	private TanzaniaEserviceNonMotorDetails tanzaniaEserviceNonMotorDetails;
	
	@Autowired 
	private TanzaniaEserviceNonMotorValidation tanzaniaEserviceNonMotorValidation;
	
	@Autowired
	private PhoenixZambiaEserviceNonMotorDetails PhoenixZambiaEserviceNonMotorDetails;
	
	@Autowired
	private PhoenixBotswanaEserviceNonMotorDetails PhoenixBotswanaEserviceNonMotorDetail;
	
	@Autowired
	private PhoenixMocambiqueDetails PhoenixMocambiqueDetails;
	
	@Autowired
	private PhoenixNamibiaDetails PhoenixNamibiaDetails;
	
	@Autowired
	private EagleNonmotorSaveValidation eagleNonmotorSaveValidation;
	
	@Autowired
	private KenyaEserviceNonMotorValidation kenyaEserviceNonMotorValidation;
	
	@Autowired
	private PhoenixSwazilanNonMotorValidation phoenixSwazilanNonMotorValidation;
	
	@Autowired
	private LoginProductMasterRepository loginProductMasterRepository;
	
	@Autowired
	private EngineerInfoRepo engineerInfoRepo;
	
	@Autowired
	private UgandaEserviceNonMotorValidation ugandaEserviceNonMotorValidation;
	
	@Autowired
	private  SalamaNonMotorValidateServiceImpl salama;
	
	@Autowired
	private   OmanEserviceNonMotor oman;
	
	
	@Autowired
	private BrokerCommissionDetailsRepository brokerCommissionDetailsRepository;
	
	@Autowired
	private ListItemValueRepository listItemValueRepository;
	
	private Logger log = LogManager.getLogger(EserviceBuildingDetailsServiceImpl.class);
	
	@Override
	public List<String> validateCommonDetails(SlideCommonSaveReq req) {
		List<String> error = new ArrayList<String>();
		
		try {
			if (StringUtils.isBlank(req.getRiskId())) {
				error.add("1198");
				// error.add(new Error("02", "RiskId", "Please Enter RiskId "));
			}
			if (StringUtils.isBlank(req.getBranchCode())) {
				error.add("1199");
				// error.add(new Error("01", "BranchCode", "Please Enter BranchCode "));
			} else if (req.getBranchCode().length() > 20) {
				error.add("1200");
				// error.add(new Error("01", "Branch Code", "Please Enter Branch Code within 20
				// Characters"));
			}
			
			if (StringUtils.isBlank(req.getProductId())) {
				error.add("1201");
				// error.add(new Error("03", "Product Id", "Please Enter ProductId "));
			} else if (req.getProductId().length() > 20) {
				error.add("1202");
				// error.add(new Error("03", "Product Id", "Please Enter Product Id within 20
				// Characters"));
			}
			
			if (req.getSectionIds() == null || req.getSectionIds().size() <= 0) {
				error.add("1203");
				// error.add(new Error("04", "SectionId", "Please Select Section "));
			}
			
			if (StringUtils.isBlank(req.getCompanyId())) {
				error.add("1204");
				// error.add(new Error("05", "CompanyId", "Please Enter CompanyId "));
			} else if (req.getCompanyId().length() > 20) {
				error.add("1205");
				// error.add(new Error("05", "CompanyId", "Please Enter CompanyId within 20
				// Characters"));
			}
			
			if (StringUtils.isBlank(req.getCurrency())) {
				error.add("1206");
				// error.add(new Error("10", "Currency", "Please Select Currency"));
			}
			if (StringUtils.isBlank(req.getExchangeRate())) {
				error.add("1207");
				// error.add(new Error("11", "ExchangeRate", "Please Enter ExchangeRate"));
			}
			
			// Source Validation
			// Source Type Search Condition
			List<String> directSource = new ArrayList<String>();
			directSource.add("1");
			directSource.add("2");
			directSource.add("3");
			
			if (req.getUserType().equalsIgnoreCase("Issuer") && StringUtils.isBlank(req.getSourceTypeId())) {
				error.add("1208");
				// error.add(new Error("10", "BdmCode", "Please Select Source Type"));
				
			} else if (StringUtils.isNotBlank(req.getSourceTypeId()) && directSource.contains(req.getSourceTypeId())) {
				if (StringUtils.isBlank(req.getBdmCode())) {
					error.add("1208");
					// error.add(new Error("10", "BdmCode", "Please Select Source Code"));
				}
				if (StringUtils.isBlank(req.getCustomerName())) {
					error.add("1209");
					// error.add(new Error("10", "CustomerName", "Please Select Customer Name"));
				}
				
			} else {
				if (StringUtils.isBlank(req.getLoginId())) {
					error.add("1210");
					// error.add(new Error("10", "Login ID", "Please Select login Id"));
				} else {
					LoginMaster loginData = loginRepo.findByLoginId(req.getCreatedBy());
					if (loginData.getSubUserType().equalsIgnoreCase("bank")) {
						if (StringUtils.isBlank(req.getAcExecutiveId())) {
							error.add("1211");
							// error.add(new Error("01", "AcExecutiveId", "Please Select AcExecutiveId"));
						}
//									
					}
				}
				
				if (StringUtils.isBlank(req.getBrokerBranchCode())) {
					error.add("1212");
					// error.add(new Error("10", "BrokerBranchCode", "Please Enter
					// BrokerBranchCode"));
				}
				
			}
			String status = StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus();
			if (req.getPolicyStartDate() == null) {
				error.add("1213");
				// error.add(new Error("13", "PolicyStartDate", "Please Enter
				// PolicyStartDate"));
			} else if ((req.getEndorsementType() == null || req.getEndorsementType().equals(0))
					&& !"RQ".equalsIgnoreCase(status)) {
				int before = getBackDays(req.getCompanyId(), req.getProductId(), req.getLoginId());
				int days = before == 0 ? -1 : -before;
				long MILLS_IN_A_DAY = 1000 * 60 * 60 * 24;
				long backDays = MILLS_IN_A_DAY * days;
				Date today = new Date();
				Date resticDate = new Date(today.getTime() + backDays);
				long days90 = MILLS_IN_A_DAY * 90;
				Date after90 = new Date(today.getTime() + days90);
				if (req.getPolicyStartDate().before(resticDate)) {
					error.add("1214");
					// error.add(new Error("14", "PolicyStartDate", "Policy Start Date Before " +
					// before + " Days Not Allowed "));
				} else if (req.getPolicyStartDate().after(after90)) {
					error.add("1215");
					// error.add(new Error("14", "PolicyStartDate", "PolicyStartDate even after 90
					// days Not Allowed"));
				}
				
			}
			
			if (StringUtils.isBlank(req.getHavepromocode())) {
				error.add("1216");
				// error.add(new Error("46", "Havepromocode", "Please Enter Havepromocode"));
			}
			if ((StringUtils.isNotBlank(req.getHavepromocode())) && req.getHavepromocode().equalsIgnoreCase("Y")) {
				if (StringUtils.isBlank(req.getPromocode())) {
					error.add("1217");
					// error.add(new Error("47", "Promocode", "Please Enter Promocode"));
				}
			}
			
			if (req.getPolicyEndDate() == null) {
				error.add("1218");
				// error.add(new Error("14", "PolicyEndDate", "Please Enter PolicyEndDate"));
				
			} else if (req.getPolicyStartDate() != null && req.getPolicyEndDate() != null
					&& req.getEndorsementType() == null) {
				if (req.getPolicyEndDate().equals(req.getPolicyStartDate())
						|| req.getPolicyEndDate().before(req.getPolicyStartDate())) {
					error.add("1219");
					// error.add(new Error("14", "PolicyEndDate", "PolicyEndDate Before
					// PolicyStartDate Not Allowed"));
				}
			}
			
			if (StringUtils.isNotBlank(req.getProductId()) && StringUtils.isNotBlank(req.getCompanyId())) {
				
				if (!"42".equalsIgnoreCase(req.getProductId())) {
					if ((StringUtils.isBlank(req.getBuildingOwnerYn())) || req.getBuildingOwnerYn() == null) {
						error.add("1220");
						// error.add(new Error("37", "BuildingOwnerYn", "Please Select Building Cover
						// Requeried Yes/No"));
					} else if (!(req.getBuildingOwnerYn().equalsIgnoreCase("N")
							|| req.getBuildingOwnerYn().equalsIgnoreCase("Y"))) {
						error.add("1221");
						// error.add(new Error("37", "BuildingOwnerYn", "Please Select Valid Y/N in
						// Building Cover Requeried Yes/No"));
						
					}
					
					if (StringUtils.isNotBlank(req.getProductId()) && !req.getProductId().equalsIgnoreCase("43")
							&& !req.getProductId().equalsIgnoreCase("25")) {
						if (StringUtils.isBlank(req.getIndustryId())) {
							error.add("1222");
							// error.add(new Error("44", "IndustryId", "Please Select Industry Id"));
						} else if (!req.getIndustryId().matches("[0-9]+")) {
							error.add("1223");
							// error.add(new Error("44", "IndustryId", "Please Select Valid Industry Id"));
						}
					}
				}
				
			}
			
		} catch (Exception e) {
			
			log.error(e);
			e.printStackTrace();
			error.add("1224");
			// error.add(new Error("19", "Common Error", e.getMessage()));
		}
		return error;
	}
	
	public Integer getBackDays(String companyId, String productId, String loginId) {
		Integer backDays = 0;
		try {
			
			LoginMaster login = loginRepo.findByCompanyIdAndLoginId(companyId,loginId);
			String userType = login != null ? login.getUserType() : null;
			
			if (!"BROKER".equalsIgnoreCase(userType)) {
				
				Date today = new Date();
				List<LoginProductMaster> result = loginProductMasterRepository
						.findByCompanyIdAndLoginIdAndProductIdAndEffectiveDateStartLessThanEqualAndEffectiveDateEndGreaterThanEqualOrderByEffectiveDateStartAsc(
								companyId, loginId, Integer.valueOf(productId), today, today);
				backDays = (result.isEmpty() || result.get(0).getBackDays() == 0)
						? 14
								: result.get(0).getBackDays(); 
				return backDays;
			}
			Date today = new Date();
			Calendar cal = new GregorianCalendar();
			cal.setTime(today);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 1);
			today = cal.getTime();
			cal.set(Calendar.HOUR_OF_DAY, 1);
			cal.set(Calendar.MINUTE, 1);
			Date todayEnd = cal.getTime();
			
			List<BrokerCommissionDetails> list = new ArrayList<BrokerCommissionDetails>();
			
			// Find Latest Record
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<BrokerCommissionDetails> query = cb.createQuery(BrokerCommissionDetails.class);
			
			// Find All
			Root<BrokerCommissionDetails> b = query.from(BrokerCommissionDetails.class);
			
			// Select
			query.select(b);
			
			// Effective Date Max Filter
			Subquery<Date> effectiveDate = query.subquery(Date.class);
			Root<BrokerCommissionDetails> ocpm1 = effectiveDate.from(BrokerCommissionDetails.class);
			effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
			jakarta.persistence.criteria.Predicate a1 = cb.equal(b.get("companyId"), ocpm1.get("companyId"));
			jakarta.persistence.criteria.Predicate a2 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
			jakarta.persistence.criteria.Predicate a3 = cb.equal(b.get("loginId"), ocpm1.get("loginId"));
			jakarta.persistence.criteria.Predicate a4 = cb.equal(b.get("productId"), ocpm1.get("productId"));
			jakarta.persistence.criteria.Predicate a11 = cb.equal(b.get("policyType"), ocpm1.get("policyType"));
			jakarta.persistence.criteria.Predicate a12 = cb.equal(b.get("id"), ocpm1.get("id"));
			effectiveDate.where(a1, a2, a3, a4, a11, a12);
			
			// Effective Date End Max Filter
			Subquery<Date> effectiveDate2 = query.subquery(Date.class);
			Root<BrokerCommissionDetails> ocpm2 = effectiveDate2.from(BrokerCommissionDetails.class);
			effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
			jakarta.persistence.criteria.Predicate a6 = cb.equal(b.get("companyId"), ocpm2.get("companyId"));
			jakarta.persistence.criteria.Predicate a8 = cb.equal(b.get("productId"), ocpm2.get("productId"));
			jakarta.persistence.criteria.Predicate a9 = cb.equal(b.get("loginId"), ocpm2.get("loginId"));
			jakarta.persistence.criteria.Predicate a10 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"),
					todayEnd);
			jakarta.persistence.criteria.Predicate a13 = cb.equal(b.get("policyType"), ocpm2.get("policyType"));
			jakarta.persistence.criteria.Predicate a14 = cb.equal(b.get("id"), ocpm2.get("id"));
			effectiveDate2.where(a6, a8, a9, a10, a13, a14);
			
			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(b.get("policyType")));
			
			// Where
			Predicate n1 = cb.equal(b.get("effectiveDateStart"), effectiveDate);
			Predicate n2 = cb.equal(b.get("effectiveDateEnd"), effectiveDate2);
			Predicate n3 = cb.equal(b.get("companyId"), companyId);
			Predicate n4 = cb.equal(b.get("productId"), productId);
			Predicate n5 = cb.equal(b.get("loginId"), loginId);
			Predicate n6 = cb.equal(b.get("policyType"), "99999");
			Predicate n7 = cb.equal(b.get("id"), "99999");
			query.where(n1, n2, n3, n4, n5, n6, n7).orderBy(orderList);
			
			// Get Result
			TypedQuery<BrokerCommissionDetails> result = em.createQuery(query);
			
			list = result.getResultList();
			backDays = list.size() > 0 ? (list.get(0).getBackDays() != null ? list.get(0).getBackDays() : 14) : 14;
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null;
		}
		return backDays;
	}
	
	public CompanyProductMaster getCompanyProductMasterDropdown(String companyId, String productId) {
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
			product = list.size() > 0 ? list.get(0) : null;
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is --->" + e.getMessage());
			return null;
		}
		return product;
	}
	
	@Override
	public List<String> validateEmpLiabilityDetails(List<SlideEmpLiabilitySaveReq> reqList) {
		List<String> error = new ArrayList<String>();
		
		try {
			Long rowNo = 0L;
			List<String> occupationId = new ArrayList<String>();
			
			if (reqList == null || reqList.size() <= 0) {
				error.add("1385");
				// error.add(new Error("01", "EployeeList", "Please Enter Atleast One Row"));
				
			} else {
				for (SlideEmpLiabilitySaveReq req : reqList) {
					rowNo = rowNo + 1;
					
					if (StringUtils.isBlank(req.getProductId())) {
						error.add("1386");
						// error.add(new Error("01", "ProductId", "Please Select ProductId Row No In Row
						// No : " + rowNo));
					}
					if (StringUtils.isBlank(req.getSectionId())) {
						error.add("1387");
						// error.add(new Error("01", "SectionId", "Please Select SectionId Row No In Row
						// No : " + rowNo));
					}
					if (StringUtils.isBlank(req.getRiskId())) {
						error.add("1388");
						// error.add(new Error("01", "riskId", "Please Select RiskId Row No In Row No :
						// " + rowNo));
					}
					if (StringUtils.isBlank(req.getRequestReferenceNo())) {
						error.add("1389");
						// error.add(new Error("01", "RequestReferenceNo", "Please Select Request
						// Reference No In Row No : " + rowNo));
					}
					if (StringUtils.isBlank(req.getInsuranceId())) {
						error.add("1390");
						// error.add(new Error("01", "InsuranceId", "Please Select Insurance Id In Row
						// No : " + rowNo));
					}
					if (StringUtils.isBlank(req.getCreatedBy())) {
						error.add("1391");
						// error.add(new Error("01", "CreatedBy", "Please Select Created By In Row No :
						// " + rowNo));
					}
					
					if (StringUtils.isBlank(req.getEmpLiabilitySi())) {
						if (reqList.size() == 1) {
							error.add("1392");
							// error.add(new Error("44", "Employers Liability Suminsured", "Please Enter
							// Personal Liability Suminsured "));
						} else {
							error.add("1393");
							// error.add(new Error("44", "Employers Liability Suminsured", "Please Enter
							// Employers Liability Suminsured In Row No : " + rowNo));
						}
						
					} else if (!req.getEmpLiabilitySi().matches("[0-9.]+")) {
						if (reqList.size() == 1) {
							error.add("1394");
							// error.add(new Error("44", "Employers Liability Suminsured","Please Enter
							// Valid Number In Personal Liability Suminsured "));
						} else {
							error.add("1395");
							// error.add(new Error("44", "Employers Liability Suminsured","Please Enter
							// Valid Number In Employers Liability Suminsured In Row No : " + rowNo));
						}
						
					} else if (Double.valueOf(req.getEmpLiabilitySi()) <= 0) {
						if (reqList.size() == 1) {
							error.add("1396");
							// error.add(new Error("44", "Employers Liability Suminsured", "Please Enter
							// Personal Liability Suminsured Above Zero "));
						} else {
							error.add("1397");
							// error.add(new Error("44", "Employers Liability Suminsured", "Please Enter
							// Employers Liability Suminsured Above Zero In Row No : " + rowNo));
						}
						
					}
					
					if (StringUtils.isBlank(req.getLiabilityOccupationId())) {
						if (reqList.size() == 1) {
							error.add("1398");
							// error.add(new Error("44", "LiabilityOccupationId", "Please Select Personal
							// Liability Occupation "));
						} else {
							error.add("1399");
							// error.add(new Error("44", "LiabilityOccupationId", "Please Select Employers
							// Liability Occupation In Row No : " + rowNo));
						}
						
					} else if (!req.getLiabilityOccupationId().matches("[0-9.]+")) {
						if (reqList.size() == 1) {
							error.add("1400");
							// error.add(new Error("44", "LiabilityOccupationId","Please Enter Valid Number
							// In Personal Liability Occupation "));
						} else {
							error.add("1401");
							// error.add(new Error("44", "LiabilityOccupationId","Please Enter Valid Number
							// In Employers Liability Occupation In Row No : " + rowNo));
						}
						
					} else {
						List<String> filterOccupation = occupationId.stream()
								.filter(o -> o.equalsIgnoreCase(req.getLiabilityOccupationId()))
								.collect(Collectors.toList());
						if (filterOccupation.size() > 0) {
							// error.add("1402");
							// error.add(new Error("44", "LiabilityOccupationId","Employers Liability
							// Occupation Duplicate In Row No : " + rowNo));
						} else {
							occupationId.add(req.getLiabilityOccupationId());
						}
					}
					
					if (StringUtils.isBlank(req.getLiabilityOccupationId())) {
						error.add("1403");
						// error.add(new Error("23", "Occupation", "Please Select Occupation"));
					} else if (req.getLiabilityOccupationId().equalsIgnoreCase("99999")) {
						if (StringUtils.isBlank(req.getOtherOccupation())) {
							error.add("1404");
							// error.add(new Error("47", "Other Occupation", "Please Enter Other
							// Occupation"));
						} else if (req.getOtherOccupation().length() > 100) {
							error.add("1405");
							// error.add(new Error("47","Other Occupation", "Please Enter Other Occupation
							// within 100 Characters"));
						} else if (!req.getOtherOccupation().matches("[a-zA-Z\\s]+")) {
							error.add("1406");
							// error.add(new Error("47","Other Occupation", "Please Enter Valid Other
							// Occupation"));
						}
					}
					
					if (StringUtils.isBlank(req.getTotalNoOfEmployees())) {
						error.add("1407");
						// error.add(new Error("44", "TotalNoOfEmployees", "Please Select Total No Of
						// Employers In Row No : " + rowNo));
					} else if (!req.getTotalNoOfEmployees().matches("[0-9.]+")) {
						error.add("1408");
						/// error.add(new Error("44", "TotalNoOfEmployees","Please Enter Valid Number In
						/// Total No Of Employers In Row No : " + rowNo));
					} else if (Long.valueOf(req.getTotalNoOfEmployees()) <= 0) {
						error.add("1409");
						// error.add(new Error("44", "TotalNoOfEmployees","Please Enter Total No Of
						// Employers Above Zero In Row No : " + rowNo));
					}
				}
			}
			
		} catch (Exception e) {
			
			log.error(e);
			e.printStackTrace();
			error.add("1410");
			// error.add(new Error("19", "Common Error", e.getMessage()));
		}
		return error;
	}
	
	@Override
	public List<String> validateSlideFidelityGuarantyDetails(List<SlideFidelityGuarantySaveReq> reqList) {
		List<String> error = new ArrayList<String>();
		
		try {
			Long rowNo = 0L;
			List<String> occupationId = new ArrayList<String>();
			
			if (reqList == null || reqList.size() <= 0) {
				error.add("1411");
				// error.add(new Error("01", "EployeeList", "Please Enter Atleast One Row"));
				
			} else {
				for (SlideFidelityGuarantySaveReq req : reqList) {
					rowNo = rowNo + 1;
					
					if (StringUtils.isBlank(req.getProductId())) {
						error.add("1412");
						// error.add(new Error("01", "ProductId", "Please Select ProductId Row No In Row
						// No : " + rowNo));
					}
					if (StringUtils.isBlank(req.getSectionId())) {
						error.add("1413");
						// error.add(new Error("01", "SectionId", "Please Select SectionId Row No In Row
						// No : " + rowNo));
					}
					if (StringUtils.isBlank(req.getRiskId())) {
						error.add("1414");
						// error.add(new Error("01", "RiskId", "Please Select RiskId Row No In Row No :
						// " + rowNo));
					}
					if (StringUtils.isBlank(req.getRequestReferenceNo())) {
						error.add("1415");
						// error.add(new Error("01", "RequestReferenceNo", "Please Select Request
						// Reference No In Row No : " + rowNo));
					}
					if (StringUtils.isBlank(req.getInsuranceId())) {
						error.add("1416");
						// error.add(new Error("01", "InsuranceId", "Please Select Insurance Id In Row
						// No : " + rowNo));
					}
					if (StringUtils.isBlank(req.getCreatedBy())) {
						error.add("1417");
						// error.add(new Error("01", "CreatedBy", "Please Select Created By In Row No :
						// " + rowNo));
					}
					
					if (StringUtils.isBlank(req.getFidEmpSi())) {
						error.add("1418");
						// error.add(new Error("44", "FidEmpSi", "Please Enter Fidelity Employee
						// Suminsured In Row No : " + rowNo));
					} else if (!req.getFidEmpSi().matches("[0-9.]+")) {
						error.add("1419");
						// error.add(new Error("44", "FidEmpSi","Please Enter Valid Number In Fidelity
						// Employee Suminsured In Row No : " + rowNo));
					} else if (Double.valueOf(req.getFidEmpSi()) <= 0) {
						error.add("1420");
						// error.add(new Error("44", "FidEmpSi", "Please Enter Fidelity Employee
						// Suminsured Above Zero In Row No : " + rowNo));
					}
					
					if (StringUtils.isBlank(req.getLiabilityOccupationId())) {
						error.add("1421");
						// error.add(new Error("44", "OccupationType", "Please Select Occupation In Row
						// No : " + rowNo));
					} else if (!req.getLiabilityOccupationId().matches("[0-9.]+")) {
						error.add("1422");
						// error.add(new Error("44", "OccupationType","Please Enter Valid Number In
						// Occupation In Row No : " + rowNo));
						
					} else {
						List<String> filterOccupation = occupationId.stream()
								.filter(o -> o.equalsIgnoreCase(req.getLiabilityOccupationId()))
								.collect(Collectors.toList());
						if (filterOccupation.size() > 0) {
							error.add("1423");
							// error.add(new Error("44", "OccupationType"," Fidelity Employee Occupation
							// Duplicate In Row No : " + rowNo));
						} else {
							occupationId.add(req.getLiabilityOccupationId());
						}
					}
					
					/*
					 * if (StringUtils.isBlank(req.getLiabilityOccupationId()) ) {
					 * error.add("1424"); //error.add(new Error("23", "Occupation",
					 * "Please Select Occupation")); } else
					 * if(req.getLiabilityOccupationId().equalsIgnoreCase("99999")){ if
					 * (StringUtils.isBlank(req.getOtherOccupation()) ) { error.add("1425");
					 * //error.add(new Error("47", "Other Occupation",
					 * "Please Enter Other Occupation")); }else if
					 * (req.getOtherOccupation().length() > 100){ error.add("1426"); //error.add(new
					 * Error("47","Other Occupation",
					 * "Please Enter Other Occupation within 100 Characters")); }else
					 * if(!req.getOtherOccupation().matches("[a-zA-Z\\s]+")){ error.add("1427");
					 * //error.add(new Error("47","Other Occupation",
					 * "Please Enter Valid Other Occupation")); } }
					 */
					
					if (StringUtils.isBlank(req.getFidEmpCount())) {
						error.add("1428");
						// error.add(new Error("44", "FidEmpCount", "Please Select Fidelity Employee
						// Count In Row No : " + rowNo));
					} else if (!req.getFidEmpCount().matches("[0-9.]+")) {
						error.add("1429");
						// error.add(new Error("44", "FidEmpCount","Please Enter Valid Number Total
						// Fidelity Employee Count In Row No : " + rowNo));
					} else if (Long.valueOf(req.getFidEmpCount()) <= 0) {
						error.add("1430");
						// error.add(new Error("44", "FidEmpCount","Please Enter Fidelity Employee Count
						// Above Zero In Row No : " + rowNo));
					}
				}
			}
			
		} catch (Exception e) {
			
			log.error(e);
			e.printStackTrace();
			error.add("1431");
			// error.add(new Error("19", "Common Error", e.getMessage()));
		}
		return error;
	}
	
	@Override
	public List<Error> validateSlideFireAndPerillsDetails(SlideFireAndPerillsSaveReq req) {
		List<Error> error = new ArrayList<Error>();
		
		try {
			if (StringUtils.isBlank(req.getProductId())) {
				error.add(new Error("01", "ProductId", "Please Select ProductId"));
			}
			if (StringUtils.isBlank(req.getSectionId())) {
				error.add(new Error("01", "SectionId", "Please Select SectionId"));
			}
			if (StringUtils.isBlank(req.getRiskId())) {
				error.add(new Error("01", "riskId", "Please Select RiskId"));
			}
			
		} catch (Exception e) {
			
			log.error(e);
			e.printStackTrace();
			error.add(new Error("19", "Common Error", e.getMessage()));
		}
		return error;
	}
	
	@Override
	public List<String> validateSlideMachineryBreakdownDetails(SlideMachineryBreakdownSaveReq req) {
		List<String> error = new ArrayList<String>();
		
		try {
			// Primary Key Validation
			if (StringUtils.isBlank(req.getProductId())) {
				error.add("1432");
				// error.add(new Error("01", "ProductId", "Please Select ProductId " ));
			}
			if (StringUtils.isBlank(req.getSectionId().toString())) {
				error.add("1433");
				// error.add(new Error("01", "SectionId", "Please Select SectionId"));
			}
			if (StringUtils.isBlank(req.getRiskId())) {
				error.add("1434");
				// error.add(new Error("01", "RiskId", "Please Select RiskId "));
			}
			if (StringUtils.isBlank(req.getRequestReferenceNo())) {
				error.add("1435");
				// error.add(new Error("01", "RequestReferenceNo", "Please Select Request
				// Reference No "));
			}
			if (StringUtils.isBlank(req.getInsuranceId())) {
				error.add("1436");
				// error.add(new Error("01", "InsuranceId", "Please Select Insurance Id "));
			}
			if (StringUtils.isBlank(req.getCreatedBy())) {
				error.add("1437");
				// error.add(new Error("01", "CreatedBy", "Please Select Created By "));
			}
			
			if (StringUtils.isNotBlank(req.getInsuranceId()) && "100004".equalsIgnoreCase(req.getInsuranceId())) {
				// Input Fields Validation
				if ((StringUtils.isBlank(req.getPowerPlantSi()) || "0.0".equalsIgnoreCase(req.getPowerPlantSi())
						|| "0".equalsIgnoreCase(req.getPowerPlantSi()))
						&& (StringUtils.isBlank(req.getElecMachinesSi())
								|| "0.0".equalsIgnoreCase(req.getElecMachinesSi())
								|| "0".equalsIgnoreCase(req.getElecMachinesSi()))
						&& (StringUtils.isBlank(req.getEquipmentSi()) || "0".equalsIgnoreCase(req.getEquipmentSi())
								|| "0.0".equalsIgnoreCase(req.getEquipmentSi()))
						&& (StringUtils.isBlank(req.getGeneralMachineSi())
								|| "0".equalsIgnoreCase(req.getGeneralMachineSi())
								|| "0.0".equalsIgnoreCase(req.getGeneralMachineSi()))
						&& (StringUtils.isBlank(req.getManuUnitsSi()) || "0.0".equalsIgnoreCase(req.getManuUnitsSi())
								|| "0".equalsIgnoreCase(req.getManuUnitsSi()))
						&& (StringUtils.isBlank(req.getBoilerPlantsSi())
								|| "0".equalsIgnoreCase(req.getBoilerPlantsSi())
								|| "0.0".equalsIgnoreCase(req.getBoilerPlantsSi()))
						&& (StringUtils.isBlank(req.getMachineEquipSi())
								|| "0.0".equalsIgnoreCase(req.getMachineEquipSi())
								|| "0".equalsIgnoreCase(req.getMachineEquipSi()))) {
					error.add("1438");
					// error.add(new Error("01", "Sum Insured","Please Enter Atleast Any One
					// SumInsured And Greater than Zero"));
				}
				
				if (StringUtils.isNotBlank(req.getPowerPlantSi())) {
					if (!req.getPowerPlantSi().matches("[0-9.]+")) {
						error.add("1439");
						// error.add(new Error("44", "Power Plant Suminsured","Please Enter Valid Number
						// In Power Plant Suminsured"));
					}
				}
				
				if (StringUtils.isNotBlank(req.getElecMachinesSi())) {
					if (!req.getElecMachinesSi().matches("[0-9.]+")) {
						error.add("1440");
						// error.add(new Error("44", "ElecMachinesSi","Please Enter Valid Number In
						// Electical Machines Suminsured"));
					}
				}
				
				if (StringUtils.isNotBlank(req.getEquipmentSi())) {
					if (!req.getEquipmentSi().matches("[0-9.]+")) {
						error.add("1441");
						// error.add(new Error("44", "EquipmentSi","Please Enter Valid Number In
						// Equipments Suminsured"));
					}
				}
				
				if (StringUtils.isNotBlank(req.getGeneralMachineSi())) {
					if (!req.getGeneralMachineSi().matches("[0-9.]+")) {
						error.add("1442");
						// error.add(new Error("44", "GeneralMachineSi","Please Enter Valid Number In
						// General Machines Suminsured"));
					}
				}
				
				if (StringUtils.isNotBlank(req.getManuUnitsSi())) {
					if (!req.getManuUnitsSi().matches("[0-9.]+")) {
						error.add("1443");
						// error.add(new Error("44", "ManuUnitsSi","Please Enter Valid Number In
						// Manufacturing Units Suminsured"));
					}
				}
				
				if (StringUtils.isNotBlank(req.getBoilerPlantsSi())) {
					if (!req.getBoilerPlantsSi().matches("[0-9.]+")) {
						error.add("1444");
						// error.add(new Error("44", "BoilerPlantsSi","Please Enter Valid Number In
						// Boiler And Pressure Plants Suminsured"));
					}
				}
				
				if (StringUtils.isNotBlank(req.getMachineEquipSi())) {
					if (!req.getMachineEquipSi().matches("[0-9.]+")) {
						error.add("1445");
						// error.add(new Error("44", "MachineEquipSi","Please Enter Valid Number In
						// Machines Electronic Equipment Suminsured"));
					}
				}
			} else {
				
				if ((StringUtils.isBlank(req.getMachinerySi()) || "0".equalsIgnoreCase(req.getMachinerySi())
						|| "0.0".equalsIgnoreCase(req.getMachinerySi()))) {
					
					error.add("1446");
					
				} else if (!StringUtils.isBlank(req.getMachinerySi())) {
					if (!req.getMachinerySi().matches("[0-9.]+")) {
						error.add("1446");
					} else if (Double.valueOf(req.getMachinerySi()) < 0.0) {
						error.add("1446");
					}
					
					if (StringUtils.isBlank(req.getMachinerySi())) {
						error.add("1446");
						// error.add(new Error("44", "MachinerySi","Please Enter Valid Number In
						// Machinery Suminsured"));
						
					} else if (!req.getMachinerySi().matches("[0-9.]+")) {
						error.add("1447");
						// error.add(new Error("44", "MachineEquipSi","Please Enter Valid Number In
						// Machinery Suminsured"));
						
					}
				}
				
			}
		} catch (Exception e) {
			
			log.error(e);
			e.printStackTrace();
			error.add("1448");
			// error.add(new Error("19", "Common Error", e.getMessage()));
		}
		return error;
	}
	
	@Override
	public List<String> validateSlideMoneyDetails(List<SlideMoneySaveReq> reqList) {
		List<String> error = new ArrayList<String>();
		
		try {
			for (SlideMoneySaveReq req : reqList) {
				// Primary Key Validation
				if (StringUtils.isBlank(req.getProductId())) {
					error.add("1449");
					// error.add(new Error("01", "ProductId", "Please Select ProductId " ));
				}
				if (StringUtils.isBlank(req.getSectionId().toString())) {
					error.add("1450");
					// error.add(new Error("01", "SectionId", "Please Select SectionId"));
				}
				if (StringUtils.isBlank(req.getRiskId())) {
					error.add("1451");
					// error.add(new Error("01", "RiskId", "Please Select RiskId "));
				}
				if (StringUtils.isBlank(req.getRequestReferenceNo())) {
					error.add("1452");
					// error.add(new Error("01", "RequestReferenceNo", "Please Select Request
					// Reference No "));
				}
				if (StringUtils.isBlank(req.getInsuranceId())) {
					error.add("1453");
					// error.add(new Error("01", "InsuranceId", "Please Select Insurance Id "));
				}
				if (StringUtils.isBlank(req.getCreatedBy())) {
					error.add("1454");
					/// error.add(new Error("01", "CreatedBy", "Please Select Created By "));
				}
				
				// Input Fields Validation
				
				if ((StringUtils.isBlank(req.getMoneyAnnualEstimate())
						|| "0.0".equalsIgnoreCase(req.getMoneyAnnualEstimate())
						|| "0".equalsIgnoreCase(req.getMoneyAnnualEstimate()))
						&& (StringUtils.isBlank(req.getMoneyCollector())
								|| "0.0".equalsIgnoreCase(req.getMoneyCollector())
								|| "0".equalsIgnoreCase(req.getMoneyCollector()))
						&& (StringUtils.isBlank(req.getMoneyDirectorResidence())
								|| "0".equalsIgnoreCase(req.getMoneyDirectorResidence())
								|| "0.0".equalsIgnoreCase(req.getMoneyDirectorResidence()))
						&& (StringUtils.isBlank(req.getMoneyOutofSafe())
								|| "0".equalsIgnoreCase(req.getMoneyOutofSafe())
								|| "0.0".equalsIgnoreCase(req.getMoneyOutofSafe()))
						&& (StringUtils.isBlank(req.getMoneySafeLimit())
								|| "0.0".equalsIgnoreCase(req.getMoneySafeLimit())
								|| "0".equalsIgnoreCase(req.getMoneySafeLimit()))
//					&& (StringUtils.isBlank(req.getMoneyMajorLoss())||"0.0".equalsIgnoreCase(req.getMoneyMajorLoss())||"0".equalsIgnoreCase(req.getMoneyMajorLoss()))
						&& (StringUtils.isBlank(req.getStrongroomSi()) || "0.0".equalsIgnoreCase(req.getStrongroomSi())
								|| "0".equalsIgnoreCase(req.getStrongroomSi()))
						&& (StringUtils.isBlank(req.getMoneyInTransit())
								|| "0.0".equalsIgnoreCase(req.getMoneyInTransit())
								|| "0".equalsIgnoreCase(req.getMoneyInTransit()))
						&& (StringUtils.isBlank(req.getMoneyInSafe()) || "0.0".equalsIgnoreCase(req.getMoneyInSafe())
								|| "0".equalsIgnoreCase(req.getMoneyInSafe()))) {
					error.add("1455");
					// error.add(new Error("01", "Sum Insured","Please Enter Atleast Any One
					// SumInsured And Greater than Zero"));
				}
				
				if (StringUtils.isNotBlank(req.getInsuranceId())) {
					if ("100004".equalsIgnoreCase(req.getInsuranceId())) {
						if (StringUtils.isNotBlank(req.getMoneyAnnualEstimate())) {
							if (!req.getMoneyAnnualEstimate().matches("[0-9.]+")) {
								error.add("1456");
								// error.add(new Error("41", "Estimated Annual Carry", "Please Enter Valid
								// Number In Estimated Annual Carry"));
							}
						}
						
						if (StringUtils.isNotBlank(req.getMoneyCollector())) {
							if (!req.getMoneyCollector().matches("[0-9.]+")) {
								error.add("1457");
								// error.add(new Error("41", "Collectors / salesman", "Please Enter Valid Number
								// In Collectors / salesman"));
							}
						}
						
						if (StringUtils.isNotBlank(req.getMoneyDirectorResidence())) {
							if (!req.getMoneyDirectorResidence().matches("[0-9.]+")) {
								error.add("1458");
								// error.add(new Error("41", "Residence Of Director", "Please Enter Valid Number
								// In Residence Of Director"));
							}
						}
						
						if (StringUtils.isNotBlank(req.getMoneyOutofSafe())) {
							if (!req.getMoneyOutofSafe().matches("[0-9.]+")) {
								error.add("1459");
								// error.add(new Error("41", "Money out of safe ", "Please Enter Valid Number In
								// Money out of safe "));
							}
						}
						
						if (StringUtils.isNotBlank(req.getMoneySafeLimit())) {
							if (!req.getMoneySafeLimit().matches("[0-9.]+")) {
								error.add("1460");
								// error.add(new Error("41", "Damage to Safe Limit", "Please Enter Valid Number
								// In Damage to Safe Limit"));
							}
						}
						
						if (StringUtils.isNotBlank(req.getMoneyMajorLoss())) {
							if (!req.getMoneyMajorLoss().matches("[0-9.]+")) {
								error.add("1461");
								// error.add(new Error("41", "Major Loss Limit ", "Please Enter Valid Number In
								// Major Loss Limit "));
							}
						}
						
					} else {
						if (StringUtils.isNotBlank(req.getMoneyAnnualEstimate())) {
							if (!req.getMoneyAnnualEstimate().matches("[0-9.]+")) {
								error.add("1462");
								// error.add(new Error("41", "Estimated Annual Carry", "Please Enter Valid
								// Number In Estimated Annual Carry"));
							}
						}
						if (StringUtils.isNotBlank(req.getStrongroomSi())) {
							if (!req.getStrongroomSi().matches("[0-9.]+")) {
								error.add("1463");
								// error.add(new Error("41", "Strong Room", "Please Enter Valid Number In Strong
								// Room Suminsured"));
							}
						}
						
						if (StringUtils.isNotBlank(req.getMoneyCollector())) {
							if (!req.getMoneyCollector().matches("[0-9.]+")) {
								error.add("1464");
								// error.add(new Error("41", "MoneyCollector", "Please Enter Valid Number In
								// Money Collector"));
							}
						}
						
						if (StringUtils.isNotBlank(req.getMoneyDirectorResidence())) {
							if (!req.getMoneyDirectorResidence().matches("[0-9.]+")) {
								error.add("1465");
								// error.add(new Error("41", "Premises Suminsured", "Please Enter Valid Number
								// In Premises Suminsured"));
							}
						}
						
//					if (StringUtils.isNotBlank(req.getMoneyOutofSafe())) {
//						 if (!req.getMoneyOutofSafe().matches("[0-9.]+")) {
//							 error.add(new Error("41", "Safe Outside Working Hours",	"Please Enter Valid Number In Safe Outside Working Hours"));
//						 }
//					}
						
						if (StringUtils.isNotBlank(req.getMoneySafeLimit())) {
							if (!req.getMoneySafeLimit().matches("[0-9.]+")) {
								error.add("1466");
								// error.add(new Error("41", "Money In Safe", "Please Enter Valid Number In
								// Money In Safe"));
							}
						}
						
//					if (StringUtils.isNotBlank(req.getMoneyMajorLoss())) {
//						 if (!req.getMoneyMajorLoss().matches("[0-9.]+")) {
//							 error.add(new Error("41", "Money in Transit",	"Please Enter Valid Number In Money in Transit"));
//						 }
//					}
						if (StringUtils.isNotBlank(req.getMoneyInTransit())) {
							if (!req.getMoneyMajorLoss().matches("[0-9.]+")) {
								error.add("1467");
								// error.add(new Error("41", "Money In Transit", "Please Enter Valid Number In
								// Money In Transit "));
							}
						}
					}
				}
				// New Validations
				if (StringUtils.isBlank(req.getLocationName())) {
					error.add("2267");
				} else if (!req.getLocationName().matches("[a-zA-Z]+")) {
					error.add("2268");
				} else if (req.getLocationName().length() > 100) {
					error.add("2269");
				}
				
				if (StringUtils.isBlank(req.getRegionCode())) {
					error.add("2270");
				}
				
				if (StringUtils.isBlank(req.getDistrictCode())) {
					error.add("2273");
				}
			}
			
		} catch (Exception e) {
			
			log.error(e);
			e.printStackTrace();
			error.add("1468");
			// error.add(new Error("19", "Common Error", e.getMessage()));
		}
		return error;
	}
	
	@Override
	public List<String> validateSlidePlateGlassDetails(SlidePlateGlassSaveReq req) {
		// TODO Auto-generated method stub
		List<String> error = new ArrayList<String>();
		try {
			// Primary Key Validation
			if (StringUtils.isBlank(req.getProductId())) {
				error.add("1469");
				// error.add(new Error("01", "ProductId", "Please Select ProductId " ));
			}
			if (StringUtils.isBlank(req.getSectionId().toString())) {
				error.add("1470");
				// error.add(new Error("01", "SectionId", "Please Select SectionId"));
			}
			if (StringUtils.isBlank(req.getRiskId())) {
				error.add("1471");
				// error.add(new Error("01", "RiskId", "Please Select RiskId "));
			}
			if (StringUtils.isBlank(req.getRequestReferenceNo())) {
				error.add("1472");
				// error.add(new Error("01", "RequestReferenceNo", "Please Select Request
				// Reference No "));
			}
			if (StringUtils.isBlank(req.getInsuranceId())) {
				error.add("1473");
				// error.add(new Error("01", "InsuranceId", "Please Select Insurance Id "));
			}
			if (StringUtils.isBlank(req.getCreatedBy())) {
				error.add("1474");
				// error.add(new Error("01", "CreatedBy", "Please Select Created By "));
			}
			
			// Input Fields Validation
			if (StringUtils.isBlank(req.getPlateGlassSi())) {
				error.add("1475");
				// error.add(new Error("35", "PlateGlassSi", "Please Enter Plate Glass
				// Suminsured"));
			} else if (!req.getPlateGlassSi().matches("[0-9.]+")) {
				error.add("1476");
				// error.add(new Error("35", "PlateGlassSi","Please Enter Valid Number In Plate
				// Glass Suminsured"));
			} else if (Double.valueOf(req.getPlateGlassSi()) <= 0) {
				error.add("1477");
				// error.add(new Error("35", "PlateGlassSi","Please Enter Plate Glass Suminsured
				// Above Zero"));
			}
			
			if (StringUtils.isBlank(req.getPlateGlassType())) {
				error.add("1478");
				// error.add(new Error("35", "PlateGlassType", "Please Select Plate Glass
				// Type"));
			} else if (!req.getPlateGlassType().matches("[0-9]+")) {
				error.add("1479");
				// error.add(new Error("35", "PlateGlassType","Please Select Valid Plate Glass
				// Type"));
			}
			
		} catch (Exception e) {
			
			log.error(e);
			e.printStackTrace();
			
		}
		return error;
	}
	
	@Override
	public List<String> validateSlidePublicLiablityDetails(SlidePublicLiabilitySaveReq req) {
		// TODO Auto-generated method stub
		List<String> error = new ArrayList<String>();
		try {
			// Primary Key Validation
			if (StringUtils.isBlank(req.getProductId())) {
				error.add("1480");
				// error.add(new Error("01", "ProductId", "Please Select ProductId " ));
			}
			if (StringUtils.isBlank(req.getSectionId().toString())) {
				error.add("1481");
				// error.add(new Error("01", "SectionId", "Please Select SectionId"));
			}
			if (StringUtils.isBlank(req.getRiskId())) {
				error.add("1482");
				// error.add(new Error("01", "RiskId", "Please Select RiskId "));
			}
			if (StringUtils.isBlank(req.getRequestReferenceNo())) {
				error.add("1483");
				// error.add(new Error("01", "RequestReferenceNo", "Please Select Request
				// Reference No "));
			}
			if (StringUtils.isBlank(req.getInsuranceId())) {
				error.add("1484");
				// error.add(new Error("01", "InsuranceId", "Please Select Insurance Id "));
			}
			if (StringUtils.isBlank(req.getCreatedBy())) {
				error.add("1485");
				// error.add(new Error("01", "CreatedBy", "Please Select Created By "));
			}
			
			// Input Fields Validation
			if (StringUtils.isNotBlank(req.getProductId()) && req.getProductId().equalsIgnoreCase("43")) {
				if (StringUtils.isBlank(req.getAggSumInsured())) {
					error.add("1486");
					// error.add(new Error("35", "Agg SumInsured", "Please Select Agg SumInsured"));
				} else if (!req.getAggSumInsured().matches("[0-9]+")) {
					error.add("1487");
					// error.add(new Error("35", "Agg SumInsured","Please Select Valid Agg
					// SumInsured"));
				} else if (Double.valueOf(req.getAggSumInsured()) <= 0) {
					error.add("1488");
					// error.add(new Error("35", "Agg SumInsured","Please Select Agg SumInsured
					// Above Zero"));
				}
				
				if (StringUtils.isBlank(req.getAooSumInsured())) {
					error.add("1489");
					// error.add(new Error("35", "Aoo SumInsured", "Please Select Aoo SumInsured"));
				} else if (!req.getAggSumInsured().matches("[0-9]+")) {
					error.add("1490");
					// error.add(new Error("35", "Aoo SumInsured","Please Select Valid Aoo
					// SumInsured"));
				} else if (Double.valueOf(req.getAggSumInsured()) <= 0) {
					error.add("1491");
					// error.add(new Error("35", "Aoo SumInsured","Please Select Aoo SumInsured
					// Above Zero"));
				}
				
				if (StringUtils.isBlank(req.getCategory())) {
					error.add("1492");
					// error.add(new Error("35", "Category", "Please Select Category "));
				}
			} else if ("100002".equalsIgnoreCase(req.getInsuranceId())) {
				if (StringUtils.isNotBlank(req.getAnyAccidentSi())) {
					// error.add(new Error("36", "AnyAccidentSi", "Please Enter Any one accident /
					// event Suminsured "));
					if (!req.getAnyAccidentSi().matches("[0-9.]+")) {
						error.add("1493");
						// error.add(new Error("36", "AnyAccidentSi","Please Enter Valid Number In Any
						// one Accident / Event / Occurence Suminsured "));
					} else if (Double.valueOf(req.getAnyAccidentSi()) <= 0) {
						error.add("1494");
						// error.add(new Error("36", "AnyAccidentSi","Please Enter Any one Accident /
						// Event / Occurence Suminsured Above Zero"));
					}
				}
				
				/*
				 * if ( StringUtils.isNotBlank(req.getInsurancePeriodSi())) { //error.add(new
				 * Error("38", "InsurancePeriodSi",
				 * "Please Enter Any one period of insurance Suminsured")); if (!
				 * req.getInsurancePeriodSi().matches("[0-9.]+")) { error.add("1495");
				 * //error.add(new Error("38",
				 * "InsurancePeriodSi","Please Enter Valid Number In Any one Period of Insurance Suminsured"
				 * )); } else if ( Double.valueOf(req.getInsurancePeriodSi()) <= 0 ) {
				 * error.add("1496"); //error.add(new Error("38",
				 * "InsurancePeriodSi","Please Enter Any one Period of Insurance Suminsured Above Zero"
				 * )); } }
				 */
				
//					if ( StringUtils.isBlank(req.getAnyAccidentSi()) && StringUtils.isBlank(req.getInsurancePeriodSi())) {
//						error.add("1497");
//						//error.add(new Error("36", "Suminsured", "Please Enter Atleast one Suminsured "));
//					}
				
			} else {
				if (StringUtils.isNotBlank(req.getLiabilitySi())) {
					// error.add(new Error("35", "LiabilitySi", "Please Enter Legal Liability Annual
					// Aggreagte Suminsured"));
					if (!req.getLiabilitySi().matches("[0-9.]+")) {
						error.add("1498");
						// error.add(new Error("35", "LiabilitySi","Please Enter Valid Number In Legal
						// Liability Annual Aggreagte Suminsured"));
					} else if (Double.valueOf(req.getLiabilitySi()) <= 0) {
						error.add("1499");
						// error.add(new Error("35", "LiabilitySi","Please Enter Legal Liability Annual
						// Aggreagte Suminsured Above Zero"));
					}
				}
				
				if (StringUtils.isNotBlank(req.getProductTurnoverSi())) {
					// error.add(new Error("35", "ProductTurnOver", "Please Enter Product Turnover
					// Suminsured"));
					if (!req.getProductTurnoverSi().matches("[0-9.]+")) {
						error.add("1500");
						// error.add(new Error("35", "ProductTurnoverSi","Please Enter Valid Number In
						// Product Turnover Suminsured"));
					} else if (Double.valueOf(req.getProductTurnoverSi()) <= 0) {
						error.add("1501");
						// error.add(new Error("35", "ProductTurnoverSi","Please Enter Product Turnover
						// Suminsured Above Zero"));
					}
				}
				if (StringUtils.isBlank(req.getLiabilitySi()) && StringUtils.isBlank(req.getProductTurnoverSi())) {
					error.add("1502");
					// error.add(new Error("36", "Suminsured", "Please Enter Atleast one Suminsured
					// "));
				}
			}
			
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return error;
	}
	
	@Override
	public List<String> validateAccidentDamageDetails(AccidentDamageSaveRequest req) {
		// TODO Auto-generated method stub
		List<String> error = new ArrayList<String>();
		
		try {
			// Primary Key Validation
			if (StringUtils.isBlank(req.getProductId())) {
				error.add("1125");
				// error.add(new Error("01", "ProductId", "Please Select ProductId " ));
			}
			if (StringUtils.isBlank(req.getSectionId().toString())) {
				error.add("1126");
				// error.add(new Error("01", "SectionId", "Please Select SectionId"));
			}
			if (StringUtils.isBlank(req.getRiskId())) {
				error.add("1127");
				// error.add(new Error("01", "RiskId", "Please Select RiskId "));
			}
			if (StringUtils.isBlank(req.getRequestReferenceNo())) {
				error.add("1128");
				// error.add(new Error("01", "RequestReferenceNo", "Please Select Request
				// Reference No "));
			}
			if (StringUtils.isBlank(req.getInsuranceId())) {
				error.add("1129");
				// error.add(new ErrorR("01", "InsuranceId", "Please Select Insurance Id "));
			}
			if (StringUtils.isBlank(req.getCreatedBy())) {
				error.add("1130");
				// error.add(new Error("01", "CreatedBy", "Please Select Created By "));
			}
			
			// Input Fields Validation
			if (StringUtils.isBlank(req.getAccDamageSi())) {
				error.add("1131");
				// error.add(new Error("35", "AccDamageSi","Please Enter Accidental Damage
				// Suminsured"));
				
			} else if (!req.getAccDamageSi().matches("[0-9.]+")) {
				error.add("1132");
				// error.add(new Error("35", "AccDamageSi","Please Enter Valid Number In
				// Accidental Damage Suminsured"));
				
			} else if (Double.valueOf(req.getAccDamageSi()) <= 0) {
				error.add("1132");
				// error.add(new Error("35", "AccDamageSi","Please Enter Accidental Damage
				// Suminsured Above Zero"));
			}
			
		} catch (Exception e) {
			
			log.error(e);
			e.printStackTrace();
		}
		return error;
	}
	
	@Override
	public List<String> validateAllRiskDetails(AllRiskDetailsReq req) {
		List<String> error = new ArrayList<String>();
		
		try {
			// Primary Key Validation
			if (StringUtils.isBlank(req.getProductId())) {
				error.add("1234");
				// error.add(new Error("01", "ProductId", "Please Select ProductId " ));
			}
			if (StringUtils.isBlank(req.getSectionId())) {
				error.add("1235");
				// error.add(new Error("01", "SectionId", "Please Select SectionId"));
			}
			if (StringUtils.isBlank(req.getRiskId())) {
				error.add("1236");
				// error.add(new Error("01", "RiskId", "Please Select RiskId "));
			}
			if (StringUtils.isBlank(req.getRequestReferenceNo())) {
				error.add("1237");
				// error.add(new Error("01", "RequestReferenceNo", "Please Select Request
				// Reference No "));
			}
			if (StringUtils.isBlank(req.getInsuranceId())) {
				error.add("1238");
				// error.add(new Error("01", "InsuranceId", "Please Select Insurance Id "));
			}
			if (StringUtils.isBlank(req.getCreatedBy())) {
				error.add("1239");
				/// error.add(new Error("01", "CreatedBy", "Please Select Created By "));
			}
			
			if (StringUtils.isNotBlank(req.getProductId()) && StringUtils.isNotBlank(req.getInsuranceId())) {
				if ("100004".equalsIgnoreCase(req.getInsuranceId()) && "21".equalsIgnoreCase(req.getProductId())) {
					Double value = 0D;
					// Input Fields Validation
					if (StringUtils.isNotBlank(req.getMiningPlantSi())) {
						if (!req.getMiningPlantSi().matches("[0-9.]+")) {
							error.add("1239");
							// error.add(new Error("35", "Mining Plant Suminsured","c"));
							
						} else if (Double.valueOf(req.getMiningPlantSi()) < 0) {
							error.add("1240");
							// error.add(new Error("35", "Mining Plant Suminsured","Please Enter Mining
							// Plant Suminsured Above Zero"));
						} else {
							value = value + Double.valueOf(req.getMiningPlantSi());
						}
					}
					
					if (StringUtils.isNotBlank(req.getNonminingPlantSi())) {
						if (!req.getNonminingPlantSi().matches("[0-9.]+")) {
							error.add("1241");
							// error.add(new Error("35", "Nonmining Plant Suminsured","Please Enter Valid
							// Number In Nonmining Plant Suminsured"));
							
						} else if (Double.valueOf(req.getNonminingPlantSi()) < 0) {
							error.add("1242");
							// error.add(new Error("35", "Nonmining Plant Suminsured","Please Enter
							// Nonmining Plant Suminsured Above Zero"));
						} else {
							value = value + Double.valueOf(req.getNonminingPlantSi());
						}
					}
					
					if (StringUtils.isNotBlank(req.getGensetsSi())) {
						if (!req.getGensetsSi().matches("[0-9.]+")) {
							error.add("1243");
							// error.add(new Error("35", "Gensets SumInsured ","Please Enter Valid Number In
							// Gensets Suminsured"));
							
						} else if (Double.valueOf(req.getGensetsSi()) < 0) {
							error.add("1244");
							// error.add(new Error("35", "Gensets SumInsured ","Please Enter Gensets
							// Suminsured Above Zero"));
						} else {
							value = value + Double.valueOf(req.getGensetsSi());
						}
					}
					
					if (value <= 0) {
						error.add("1245");
						// error.add(new Error("35", "Suminsured","Please Enter Anyone Suminsured Above
						// Zero"));
					}
					
				} else if ("100004".equalsIgnoreCase(req.getInsuranceId())
						&& "26".equalsIgnoreCase(req.getProductId())) {
					// Input Fields Validation
					if (StringUtils.isBlank(req.getEquipmentSi())) {
						error.add("1246");
						// error.add(new Error("35", "EquipmentSi","Please Enter Equipment
						// Suminsured"));
					} else if (!req.getEquipmentSi().matches("[0-9.]+")) {
						error.add("1247");
						// error.add(new Error("35", "EquipmentSi","Please Enter Valid Number In
						// Equipment Suminsured"));
						
					} else if (Double.valueOf(req.getEquipmentSi().toString()) <= 0) {
						error.add("1248");
						// error.add(new Error("35", "AllriskSuminsured","Please Enter Equipment
						// Suminsured Above Zero"));
					}
					
				} else if ("19".equalsIgnoreCase(req.getProductId()) && "69".equalsIgnoreCase(req.getSectionId())) {
					if (StringUtils.isBlank(req.getAllriskSuminsured())) {
						error.add("1249");
						// error.add(new Error("35", "BusinessAllriskSuminsured","Please Enter Business
						// All Risk Suminsured"));
					} else if (!req.getAllriskSuminsured().toString().matches("[0-9.]+")) {
						error.add("1250");
						// error.add(new Error("35", "BusinessAllriskSuminsured","Please Enter Valid
						// Number In Business All Risk Suminsured"));
						
					} else if (Double.valueOf(req.getAllriskSuminsured().toString()) <= 0) {
						error.add("1251");
						// error.add(new Error("35", "BusinessAllriskSuminsured","Please Enter Business
						// All Risk Suminsured Above Zero"));
					}
					// Input Fields Validation
				} else {
					
					if (StringUtils.isBlank(req.getAllriskSuminsured())) {
						error.add("1252");
						// error.add(new Error("35", "AllriskSuminsured","Please Enter All Risk
						// Suminsured"));
					} else if (!req.getAllriskSuminsured().toString().matches("[0-9.]+")) {
						error.add("1253");
						// error.add(new Error("35", "AllriskSuminsured","Please Enter Valid Number In
						// All Risk Suminsured"));
						
					} else if (Double.valueOf(req.getAllriskSuminsured().toString()) <= 0) {
						error.add("1254");
						// error.add(new Error("35", "AllriskSuminsured","Please Enter All Risk
						// Suminsured Above Zero"));
					}
				}
				
			}
			
		} catch (Exception e) {
			
			log.error(e);
			e.printStackTrace();
		}
		return error;
	}
	
	@Override
	public List<String> validateBurglaryAndHouseBreakingDetails(BurglaryAndHouseBreakingSaveReq req) {
		List<String> error = new ArrayList<String>();
		
		try {
			// Primary Key Validation
			if (StringUtils.isBlank(req.getProductId())) {
				error.add("1255");
				// error.add(new Error("01", "ProductId", "Please Select ProductId" ));
			}
			if (StringUtils.isBlank(req.getSectionId().toString())) {
				error.add("1256");
				/// error.add(new Error("02", "SectionId", "Please Select SectionId"));
			}
			if (StringUtils.isBlank(req.getRiskId())) {
				error.add("1257");
				// error.add(new Error("03", "RiskId", "Please Select RiskId "));
			}
			if (StringUtils.isBlank(req.getRequestReferenceNo())) {
				error.add("1258");
				// error.add(new Error("04", "RequestReferenceNo", "Please Select Request
				// Reference No "));
			}
			if (StringUtils.isBlank(req.getInsuranceId())) {
				error.add("1259");
				// error.add(new Error("05", "InsuranceId", "Please Select Insurance Id "));
			}
			if (StringUtils.isBlank(req.getCreatedBy())) {
				error.add("1260");
				// error.add(new Error("06", "CreatedBy", "Please Select Created By "));
			}
			
			// Input Fields Validation
//			if (StringUtils.isBlank(req.getBurglarySi())) {
//				error.add(new Error("01", "BurglarySi",  "Please Enter Burglary SumInsured"));
//			} else if (! req.getBurglarySi().matches("[0-9.]+") ) {
//				error.add(new Error("01", "BurglarySi",  "Please Enter Valid Number in Burglary SumInsured"));
//			} else if ( Double.valueOf(req.getBurglarySi()) <=0 ) {
//				error.add(new Error("01", "BurglarySi",  "Please Enter  Burglary SumInsured Above Zero"));
//			}
//			
//			if (StringUtils.isBlank(req.getFirstLossPercentId())) {
//				error.add(new Error("01", "FirstLossPercent",  "Please Enter First Loss Percent"));
//			} else if (! req.getFirstLossPercentId().matches("[0-9.]+") ) {
//				error.add(new Error("01", "FirstLossPercent",  "Please Enter Valid Number in First Loss Percent"));
//			} else if ( Double.valueOf(req.getFirstLossPercentId()) <=0 ) {
//				error.add(new Error("01", "FirstLossPercent",  "Please Enter  First Loss Percent Above Zero"));
//			} else if ( Double.valueOf(req.getFirstLossPercentId()) >= 100 ) {
//				error.add(new Error("01", "FirstLossPercent",  "Please Enter  First Loss Percent Below 100"));
//			}
			if ("100002".equalsIgnoreCase(req.getInsuranceId())) {
				if (StringUtils.isBlank(req.getBurglarySi())) {
					error.add("1261");
					// error.add(new Error("35", "BurglarySuminsured", "Please Enter Burglary
					// Suminsured"));
					
				} else if (!req.getBurglarySi().matches("[0-9.]+")) {
					error.add("1262");
					// error.add(new Error("35", "BurglarySuminsured","Please Enter Valid Number In
					// Burglary Suminsured"));
				} else if (Double.valueOf(req.getBurglarySi()) < 0) {
					error.add("1262");
					/// error.add(new Error("35", "BurglarySuminsured","Please Enter Valid Number In
					/// Burglary Suminsured"));
				}
			} else {
				// Sum Insured
				
				if (StringUtils.isBlank(req.getStockInTradeSi()) || "0.0".equalsIgnoreCase(req.getStockInTradeSi())
						|| "0".equalsIgnoreCase(req.getStockInTradeSi())) {
					if (StringUtils.isNotBlank(req.getStockLossPercent())) {
						error.add("1263");
						// error.add(new Error("01", "StockInTradeSi","Please Enter Stock Sum Insured
						// And Greater than Zero"));
					}
				}
				if (StringUtils.isBlank(req.getGoodsSi()) || "0.0".equalsIgnoreCase(req.getGoodsSi())
						|| "0".equalsIgnoreCase(req.getGoodsSi())) {
					if (StringUtils.isNotBlank(req.getGoodsLossPercent())) {
						error.add("1264");
						// error.add(new Error("01", "GoodsSi","Please Enter Goods Sum Insured And
						// Greater than Zero"));
					}
				}
				if (StringUtils.isBlank(req.getFurnitureSi()) || "0.0".equalsIgnoreCase(req.getFurnitureSi())
						|| "0".equalsIgnoreCase(req.getFurnitureSi())) {
					if (StringUtils.isNotBlank(req.getFurnitureLossPercent())) {
						error.add("1265");
						// error.add(new Error("01", "FurnitureSi","Please Enter Furniture Sum Insured
						// And Greater than Zero"));
					}
				}
				if (StringUtils.isBlank(req.getApplianceSi()) || "0.0".equalsIgnoreCase(req.getApplianceSi())
						|| "0".equalsIgnoreCase(req.getApplianceSi())) {
					if (StringUtils.isNotBlank(req.getApplianceLossPercent())) {
						error.add("1266");
						// error.add(new Error("01", "ApplianceSi","Please Enter Appliance Sum Insured
						// And Greater than Zero"));
					}
				}
				if (StringUtils.isBlank(req.getCashValueablesSi()) || "0.0".equalsIgnoreCase(req.getCashValueablesSi())
						|| "0".equalsIgnoreCase(req.getCashValueablesSi())) {
					if (StringUtils.isNotBlank(req.getCashValueablesLossPercent())) {
						error.add("1267");
						// error.add(new Error("01", "CashValueablesSi","Please Enter CashValueables Sum
						// Insured And Greater than Zero"));
					}
				}
				
				if ((StringUtils.isBlank(req.getStockInTradeSi()) || "0.0".equalsIgnoreCase(req.getStockInTradeSi())
						|| "0".equalsIgnoreCase(req.getStockInTradeSi()))
						&& (StringUtils.isBlank(req.getGoodsSi()) || "0.0".equalsIgnoreCase(req.getGoodsSi())
								|| "0".equalsIgnoreCase(req.getGoodsSi()))
						&& (StringUtils.isBlank(req.getFurnitureSi()) || "0".equalsIgnoreCase(req.getFurnitureSi())
								|| "0.0".equalsIgnoreCase(req.getFurnitureSi()))
						&& (StringUtils.isBlank(req.getApplianceSi()) || "0".equalsIgnoreCase(req.getApplianceSi())
								|| "0.0".equalsIgnoreCase(req.getApplianceSi()))
						&& (StringUtils.isBlank(req.getCashValueablesSi())
								|| "0.0".equalsIgnoreCase(req.getCashValueablesSi())
								|| "0".equalsIgnoreCase(req.getCashValueablesSi()))) {
					error.add("1268");
					// error.add(new Error("01", "Sum Insured","Please Enter Atleast Any One
					// SumInsured And Greater than Zero"));
				}
				if (StringUtils.isNotBlank(req.getStockInTradeSi())
						&& (!"0.0".equalsIgnoreCase(req.getStockInTradeSi()))
						&& (!"0".equalsIgnoreCase(req.getStockInTradeSi()))) {
					if (!req.getStockInTradeSi().matches("[0-9.]+")) {
						error.add("1269");
						// error.add(new Error("07", "StockInTradeSi", "Please Enter Valid Number in
						// Stock In Trade SumInsured"));
					} else if (Double.valueOf(req.getStockInTradeSi()) < 0) {
						error.add("1270");
						// error.add(new Error("07", "StockInTradeSi", "Greater Then Zero Only Allowed
						// In Stock In Trade SumInsured"));
					}
					
					if (StringUtils.isBlank(req.getStockInTradeSi())) {
						error.add("1271");
						// error.add(new Error("07", "Stock Loss Percent", "Please Enter Stock Loss
						// Percent"));
					} else if (!req.getStockLossPercent().matches("[0-9.]+")) {
						error.add("1272");
						// error.add(new Error("07", "Stock Loss Percent", "Please Enter Valid Number in
						// Stock Loss Percent"));
					} else if (Double.valueOf(req.getStockLossPercent()) < 0) {
						error.add("1273");
						// error.add(new Error("07", "Stock Loss Percent", "Greater Then Zero Only
						// Allowed InStock Loss Percent"));
					} else if (Double.valueOf(req.getStockLossPercent()) > 100) {
						error.add("1274");
						// error.add(new Error("07", "Stock Loss Percent", "Greater Then Hundred is Not
						// Allowed In Stock Loss Percent"));
					}
					
				}
				
				if (StringUtils.isNotBlank(req.getGoodsSi()) && (!"0.0".equalsIgnoreCase(req.getGoodsSi()))
						&& (!"0".equalsIgnoreCase(req.getGoodsSi()))) {
					if (!req.getGoodsSi().matches("[0-9.]+")) {
						error.add("1275");
						// error.add(new Error("08", "GoodsSi", "Please Enter Valid Number in o Goods in
						// Trust SumInsured"));
					} else if (Double.valueOf(req.getGoodsSi()) < 0) {
						error.add("1276");
						// error.add(new Error("08", "GoodsSi", "Greater Then Zero Only Allowed In Goods
						// in Trust SumInsured"));
					}
					
					if (StringUtils.isBlank(req.getGoodsLossPercent())) {
						error.add("1277");
						// error.add(new Error("08", "Goods Loss Percent", "Please Enter Goods Loss
						// Percent"));
					} else if (!req.getGoodsLossPercent().matches("[0-9.]+")) {
						error.add("1278");
						// error.add(new Error("09", "Goods Loss Percent", "Please Enter Valid Number in
						// Goods Loss Percent"));
					} else if (Double.valueOf(req.getGoodsLossPercent()) < 0) {
						error.add("1279");
						// error.add(new Error("09", "Goods Loss Percent", "Greater Then Zero Only
						// Allowed In Goods Loss Percent"));
					} else if (Double.valueOf(req.getGoodsLossPercent()) > 100) {
						error.add("1280");
						// error.add(new Error("09", " Goods Loss Percent", "Greater Then Hundred is Not
						// Allowed In Goods Loss Percent"));
					}
				}
				
				if (StringUtils.isNotBlank(req.getFurnitureSi()) && (!"0.0".equalsIgnoreCase(req.getFurnitureSi()))
						&& (!"0".equalsIgnoreCase(req.getFurnitureSi()))) {
					if (!req.getFurnitureSi().matches("[0-9.]+")) {
						error.add("1281");
						// error.add(new Error("10", "FurnitureSi", "Please Enter Valid Number in
						// Furniture Fixtures SumInsured"));
					} else if (Double.valueOf(req.getFurnitureSi()) < 0) {
						error.add("1282");
						// error.add(new Error("10", "FurnitureSi", "Greater Then Zero Only Allowed
						// Furniture Fixtures SumInsured"));
					}
					
					if (StringUtils.isBlank(req.getFurnitureLossPercent())) {
						error.add("1283");
						// error.add(new Error("10", "Furniture Loss Percent", "Please Enter Furniture
						// Loss Percent"));
					} else if (!req.getFurnitureLossPercent().matches("[0-9.]+")) {
						error.add("1284");
						// error.add(new Error("10", "Furniture Loss Percent", "Please Enter Valid
						// Number in Furniture Loss Percent"));
					} else if (Double.valueOf(req.getFurnitureLossPercent()) < 0) {
						error.add("1285");
						// error.add(new Error("10", "Furniture Loss Percent", "Greater Then Zero Only
						// Allowed Furniture Loss Percent"));
					} else if (Double.valueOf(req.getFurnitureLossPercent()) > 100) {
						error.add("1286");
						// error.add(new Error("10", " Furniture Loss Percent", "Greater Then Hundred is
						// Not Allowed In Furniture Loss Percent"));
					}
				}
				
				if (StringUtils.isNotBlank(req.getApplianceSi()) && (!"0.0".equalsIgnoreCase(req.getApplianceSi()))
						&& (!"0".equalsIgnoreCase(req.getApplianceSi()))) {
					if (!req.getApplianceSi().matches("[0-9.]+")) {
						error.add("1287");
						// error.add(new Error("11", "ApplianceSi", "Please Enter Valid Number in
						// Business Plan & Appliances SumInsured"));
					} else if (Double.valueOf(req.getApplianceSi()) < 0) {
						error.add("1288");
						// error.add(new Error("11", "ApplianceSi", "Greater Then Zero Only Allowed
						// Business Plan & Appliances SumInsured"));
					}
					
					if (StringUtils.isBlank(req.getApplianceLossPercent())) {
						error.add("1289");
						// error.add(new Error("11", "Appliance Loss Percent", "Please Enter Appliance
						// Loss Percent"));
					} else if (!req.getApplianceSi().matches("[0-9.]+")) {
						error.add("1290");
						// error.add(new Error("11", "Appliance Loss Percent", "Please Enter Valid
						// Number in Appliance Loss Percent"));
					} else if (Double.valueOf(req.getApplianceLossPercent()) < 0) {
						error.add("1291");
						// error.add(new Error("11", "Appliance Loss Percent", "Greater Then Zero Only
						// Allowed Appliance Loss Percent"));
					} else if (Double.valueOf(req.getApplianceLossPercent()) > 100) {
						error.add("1292");
						// error.add(new Error("11", " Appliance Loss Percent", "Greater Then Hundred is
						// Not Allowed In Appliance Loss Percent"));
					}
				}
				
				if (StringUtils.isNotBlank(req.getCashValueablesSi())
						&& (!"0.0".equalsIgnoreCase(req.getCashValueablesSi()))
						&& (!"0".equalsIgnoreCase(req.getCashValueablesSi()))) {
					if (!req.getCashValueablesSi().matches("[0-9.]+")) {
						error.add("1293");
						// error.add(new Error("12", "CashValueablesSi", "Please Enter Valid Number in
						// Cash or Valuables SumInsured"));
					} else if (Double.valueOf(req.getCashValueablesSi()) < 0) {
						error.add("1294");
						// error.add(new Error("12", "CashValueablesSi", "Greater Then Zero Only Allowed
						// Cash or Valuables SumInsured"));
					}
					
					if (StringUtils.isBlank(req.getCashValueablesLossPercent())) {
						error.add("1295");
						// error.add(new Error("12", "CashValueablesLossPercent", "Please Enter Cash
						// Valueables Loss Percent"));
					} else if (!req.getCashValueablesLossPercent().matches("[0-9.]+")) {
						error.add("1296");
						// error.add(new Error("12", "CashValueablesLossPercent", "Please Enter Valid
						// Number in Cash or Valuables Loss Percent"));
					} else if (Double.valueOf(req.getCashValueablesLossPercent()) < 0) {
						error.add("1297");
						// error.add(new Error("12", "CashValueablesLossPercent", "Greater Then Zero
						// Only Allowed Cash or Valuables Loss Percent"));
					} else if (Double.valueOf(req.getCashValueablesLossPercent()) > 100) {
						error.add("1298");
						// error.add(new Error("12", " CashValueablesLossPercent", "Greater Then Hundred
						// is Not Allowed In Cash or Valuables Loss Percent"));
					}
				}
				
				if (StringUtils.isBlank(req.getNatureOfTradeId())) {
					error.add("1299");
					// error.add(new Error("13", "NatureOfTradeId", "Please Select Nature Of Trade "
					// ));
				}
				if (req.getInsuranceForId() == null || req.getInsuranceForId().size() <= 0) {
					error.add("1300");
					// error.add(new Error("14", "InsuranceForId", "Please Select Insurance For"));
				}
				if (StringUtils.isBlank(req.getWindowsMaterialId())) {
					error.add("1301");
					// error.add(new Error("15", "WindowsMaterialId", "Please Select Windows
					// Material"));
				}
				if (StringUtils.isBlank(req.getDoorsMaterialId())) {
					error.add("1302");
					// error.add(new Error("16", "DoorsMaterial", "Please Select Request Doors
					// Material "));
				}
				if (StringUtils.isBlank(req.getBuildingOccupied())) {
					error.add("1303");
					// error.add(new Error("17", "BuildingOccupied", "Please Select Building
					// Occupied "));
				}
				if (StringUtils.isBlank(req.getNightLeftDoor())) {
					error.add("1304");
					// error.add(new Error("18", "NightLeftDoor", "Please Select Night Left Door
					// "));
				}
				
				if (StringUtils.isNotBlank(req.getWatchmanGuardHours())) {
					if (!req.getWatchmanGuardHours().matches("[0-9.]+")) {
						error.add("1305");
						// error.add(new Error("01", "WatchmanGuardHours", "Please Enter Valid Number in
						// Watchman Guard the Premises Hours"));
					} else if (Double.valueOf(req.getWatchmanGuardHours()) < 0) {
						error.add("1306");
						// error.add(new Error("01", "WatchmanGuardHours", "Greater Then Zero Only
						// Allowed in in Watchman Guard the Premises Hours"));
					} else if (Double.valueOf(req.getWatchmanGuardHours()) > 24) {
						error.add("1307");
						// error.add(new Error("01", "WatchmanGuardHours", "Less Then 24 Only Allowed in
						// in Watchman Guard the Premises Hours"));
					}
				}
				
				// Windows
				if (StringUtils.isNotBlank(req.getAccessibleWindows())) {
					if (!req.getAccessibleWindows().matches("[0-9.]+")) {
						error.add("1308");
						// error.add(new Error("01", "AccessibleWindows", "Please Enter Valid Number in
						// Accessible Windows"));
					} else if (Double.valueOf(req.getAccessibleWindows()) < 0) {
						error.add("1309");
						// error.add(new Error("01", "AccessibleWindows", "Greater Then Zero Only
						// Allowed in Accessible Windows"));
					} else if (Double.valueOf(req.getAccessibleWindows()) > 100) {
						error.add("1310");
						// error.add(new Error("01", "AccessibleWindows", "Less Then 100 Only Allowed
						// Accessible Windows"));
					}
				}
				
				if (StringUtils.isNotBlank(req.getShowWindow())) {
					if (!req.getAccessibleWindows().matches(("[0-9.]+"))) {
						error.add("1311");
						// error.add(new Error("01", "ShowWindow", "Please Enter Valid Number in Show
						// Window"));
					} else if (Double.valueOf(req.getShowWindow()) < 0) {
						error.add("1312");
						// error.add(new Error("01", "ShowWindow", "Greater Then Zero Only Allowed in
						// Show Window"));
					} else if (Double.valueOf(req.getShowWindow()) > 100) {
						error.add("1313");
						// error.add(new Error("01", "ShowWindow", "Less Then 100 Only Allowed Show
						// Window"));
					}
				}
				
				if (StringUtils.isNotBlank(req.getFrontDoors())) {
					if (!req.getFrontDoors().matches("[0-9.]+")) {
						error.add("1314");
						// error.add(new Error("01", "Front Doors", "Please Enter Valid Number in Front
						// Doors"));
					} else if (Double.valueOf(req.getFrontDoors()) < 0) {
						error.add("1315");
						// error.add(new Error("01", "Front Doors", "Greater Then Zero Only Allowed in
						// Front Doors"));
					} else if (Double.valueOf(req.getFrontDoors()) > 100) {
						error.add("1316");
						// error.add(new Error("01", "AccessibleWindows", "Less Then 100 Only Allowed
						// FrontDoors"));
					}
				}
				
				if (StringUtils.isNotBlank(req.getBackDoors())) {
					if (!req.getBackDoors().matches("[0-9.]+")) {
						error.add("1317");
						// error.add(new Error("01", "Back Doors", "Please Enter Valid Number in Back
						// Doors"));
					} else if (Double.valueOf(req.getBackDoors()) < 0) {
						error.add("1318");
						// error.add(new Error("01", "Back Doors", "Greater Then Zero Only Allowed in
						// Back Doors"));
					} else if (Double.valueOf(req.getBackDoors()) > 100) {
						error.add("1319");
						// error.add(new Error("01", "Back Doors", "Less Then 100 Only Allowed Back
						// Doors"));
					}
				}
				
				if (StringUtils.isNotBlank(req.getTrapDoors())) {
					if (!req.getFrontDoors().matches("[0-9.]+")) {
						error.add("1320");
						// error.add(new Error("01", "Trap Doors", "Please Enter Valid Number in Trap
						// Doors"));
					} else if (Double.valueOf(req.getTrapDoors()) < 0) {
						error.add("1321");
						// error.add(new Error("01", "Trap Doors", "Greater Then Zero Only Allowed in
						// Trap Doors"));
					} else if (Double.valueOf(req.getTrapDoors()) > 100) {
						error.add("1322");
						// error.add(new Error("01", "Trap Doors", "Less Then 100 Only Allowed Trap
						// Doors"));
					}
				}
				
				if (StringUtils.isNotBlank(req.getWindowsMaterialId())) {
					if (!req.getWindowsMaterialId().matches("[0-9.]+")) {
						error.add("1323");
						// error.add(new Error("01", "Windows Material", "Please Enter Valid Id in
						// Windows Material"));
					}
				}
				
				if (StringUtils.isNotBlank(req.getDoorsMaterialId())) {
					if (!req.getDoorsMaterialId().matches("[0-9.]+")) {
						error.add("1324");
						// error.add(new Error("01", "Doors Material", "Please Enter Valid Id in Doors
						// Material"));
					}
				}
				
				if (StringUtils.isNotBlank(req.getNightLeftDoor())) {
					if (!req.getNightLeftDoor().matches("[0-9.]+")) {
						error.add("1325");
						// error.add(new Error("01", "NightLeftDoor", "Please Enter Valid Id in Night
						// Left Door"));
					}
				}
				
				if (StringUtils.isNotBlank(req.getBuildingOccupied())) {
					if (!req.getBuildingOccupied().matches("[0-9.]+")) {
						error.add("1326");
						// error.add(new Error("01", "Building Occupied", "Please Enter Valid Id in
						// Building Occupied"));
					}
					if (StringUtils.isNotBlank(req.getBuildingBuildYear())
							&& StringUtils.isNotBlank(req.getOccupiedYear())) {
						if (Integer.valueOf(req.getBuildingBuildYear()) > Integer.valueOf(req.getOccupiedYear())) {
							error.add("1327");
							// error.add(new Error("01", "Building Occupied", "Building Occupied year should
							// be greater than or equal to Built Construction Year"));
						}
					}
					
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
				Date today = new Date();
				if (StringUtils.isBlank(req.getBuildingBuildYear())) {
					error.add("1328");
					// error.add(new Error("01", "BuildingBuildYear", "Please Enter Valid Id in
					// Building Build Year"));
				} else if (!req.getBuildingBuildYear().matches("[0-9.]+")) {
					error.add("1329");
					// error.add(new Error("01", "BuildingBuildYear", "Please Enter Valid Id in
					// Building Build Year"));
				} else {
					int year = Integer.valueOf(sdf.format(today));
					int buildYear = Integer.valueOf(req.getBuildingBuildYear());
					if (buildYear < 1900) {
						error.add("1330");
						// error.add(new Error("01", "BuildingBuildYear", "Building Build Year Less Then
						// 1900 Not Allowed"));
					} else if (buildYear > year) {
						error.add("1331");
						// error.add(new Error("01", "BuildingBuildYear", "Future Year Not Allowed in
						// Building Build Year "));
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
			error.add("1332");
		}
		return error;
		
	}
	
	@Override
	public List<String> validateBurglaryAndHouseBreakingDetailsList(List<BurglaryAndHouseBreakingSaveReq> reqList) {
		List<String> error = new ArrayList<String>();
		
		try {
			for (BurglaryAndHouseBreakingSaveReq req : reqList) {
				// Primary Key Validation
				if (StringUtils.isBlank(req.getProductId())) {
					error.add("1255");
					// error.add(new Error("01", "ProductId", "Please Select ProductId" ));
				}
				if (StringUtils.isBlank(req.getSectionId().toString())) {
					error.add("1256");
					/// error.add(new Error("02", "SectionId", "Please Select SectionId"));
				}
				if (StringUtils.isBlank(req.getRiskId())) {
					error.add("1257");
					// error.add(new Error("03", "RiskId", "Please Select RiskId "));
				}
				if (StringUtils.isBlank(req.getRequestReferenceNo())) {
					error.add("1258");
					// error.add(new Error("04", "RequestReferenceNo", "Please Select Request
					// Reference No "));
				}
				if (StringUtils.isBlank(req.getInsuranceId())) {
					error.add("1259");
					// error.add(new Error("05", "InsuranceId", "Please Select Insurance Id "));
				}
				if (StringUtils.isBlank(req.getCreatedBy())) {
					error.add("1260");
					// error.add(new Error("06", "CreatedBy", "Please Select Created By "));
				}
				
				// Input Fields Validation
//			if (StringUtils.isBlank(req.getBurglarySi())) {
//				error.add(new Error("01", "BurglarySi",  "Please Enter Burglary SumInsured"));
//			} else if (! req.getBurglarySi().matches("[0-9.]+") ) {
//				error.add(new Error("01", "BurglarySi",  "Please Enter Valid Number in Burglary SumInsured"));
//			} else if ( Double.valueOf(req.getBurglarySi()) <=0 ) {
//				error.add(new Error("01", "BurglarySi",  "Please Enter  Burglary SumInsured Above Zero"));
//			}
//			
//			if (StringUtils.isBlank(req.getFirstLossPercentId())) {
//				error.add(new Error("01", "FirstLossPercent",  "Please Enter First Loss Percent"));
//			} else if (! req.getFirstLossPercentId().matches("[0-9.]+") ) {
//				error.add(new Error("01", "FirstLossPercent",  "Please Enter Valid Number in First Loss Percent"));
//			} else if ( Double.valueOf(req.getFirstLossPercentId()) <=0 ) {
//				error.add(new Error("01", "FirstLossPercent",  "Please Enter  First Loss Percent Above Zero"));
//			} else if ( Double.valueOf(req.getFirstLossPercentId()) >= 100 ) {
//				error.add(new Error("01", "FirstLossPercent",  "Please Enter  First Loss Percent Below 100"));
//			}
				if ("100002".equalsIgnoreCase(req.getInsuranceId())) {
					if (StringUtils.isBlank(req.getBurglarySi())) {
						error.add("1261");
						// error.add(new Error("35", "BurglarySuminsured", "Please Enter Burglary
						// Suminsured"));
						
					} else if (!req.getBurglarySi().matches("[0-9.]+")) {
						error.add("1262");
						// error.add(new Error("35", "BurglarySuminsured","Please Enter Valid Number In
						// Burglary Suminsured"));
					} else if (Double.valueOf(req.getBurglarySi()) <= 0) {
						error.add("1262");
						/// error.add(new Error("35", "BurglarySuminsured","Please Enter Valid Number In
						/// Burglary Suminsured"));
					}
				}
				// General validations
				if (StringUtils.isBlank(req.getFirstLossPercentId())) {
					error.add("2263"); // First Loss Percent cannot be empty
				}
				if (StringUtils.isBlank(req.getIndustryType())) {
					error.add("2264"); // Industry Type cannot be empty
				}
				if (StringUtils.isBlank(req.getCoveringDetails())) {
					error.add("2265"); // Covering Details cannot be empty
				} else if (req.getCoveringDetails().length() > 1000) {
					error.add("2266"); // Covering Details should be max 1000 characters
				}
				if (StringUtils.isBlank(req.getDescriptionOfRisk())) {
					error.add("2267"); // Description Of Risk cannot be empty
				} else if (req.getDescriptionOfRisk().length() > 1000) {
					error.add("2268"); // Description Of Risk should be max 1000 characters
				}
				// error.addAll(validateLocationDetails(req.getLocationName(),req.getRegionName(),req.getDistrictName()));;
				// // validating location related feilds
				// Location Validation
				if (StringUtils.isBlank(req.getLocationName())) {
					error.add("2269"); // Location Blank validation
				} else if (req.getLocationName().length() > 100) {
					error.add("2271"); // Less than 100 characters are allowed
				} else if (!req.getLocationName().matches("[a-zA-Z ]+")) {
					error.add("2270"); // Please Enter valid Location (only alphabets allowed)
				}
				
				// Region Validation
				if (StringUtils.isBlank(req.getRegionCode())) {
					error.add("2272"); // Region Blank validation
				}
				
				// District Validation
				if (StringUtils.isBlank(req.getDistrictCode())) {
					error.add("2275"); // District Blank validation
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
			error.add("1332");
		}
		return error;
		
	}
	
	@Override
	public List<String> validateFireAndAlliedPerillsDetails(FireAndAlliedPerillsSaveReq req) {
		// TODO Auto-generated method stub
		List<String> error = new ArrayList<String>();
		try {
			
			// Primary Key Validation
			if (StringUtils.isBlank(req.getProductId())) {
				error.add("1333");
			}
			if (StringUtils.isBlank(req.getSectionId().toString())) {
				error.add("1334");
			}
			if (StringUtils.isBlank(req.getRiskId())) {
				error.add("1335");
				// error.add(new Error("01", "RiskId", "Please Select RiskId "));
			}
			if (StringUtils.isBlank(req.getRequestReferenceNo())) {
				error.add("1336");
				// error.add(new Error("01", "RequestReferenceNo", "Please Select Request
				// Reference No "));
			}
			if (StringUtils.isBlank(req.getInsuranceId())) {
				error.add("1337");
				// error.add(new Error("01", "InsuranceId", "Please Select Insurance Id "));
			}
			if (StringUtils.isBlank(req.getCreatedBy())) {
				error.add("1338");
				// error.add(new Error("01", "CreatedBy", "Please Select Created By "));
			}
			
			// Input Fields Validation
			// Company 100002
			if ("100002".equalsIgnoreCase(req.getInsuranceId())) {
				if (StringUtils.isNotBlank(req.getBuildingSuminsured())) {
					// error.add("1339");
					// error.add(new Error("35", "BuildingSuminsured", "Please Enter Building
					// Suminsured"));
					if (!req.getBuildingSuminsured().matches("[0-9.]+")) {
						error.add("1341");
						// error.add(new Error("35", "BuildingSuminsured","Please Enter Valid Number In
						// Building Suminsured"));
					} else if (Double.valueOf(req.getBuildingSuminsured()) < 0) {
						error.add("1341");
						// error.add(new Error("35", "BuildingSuminsured","Please Enter Valid Number In
						// Building Suminsured"));
					}
				}
				
				if (StringUtils.isNotBlank(req.getOnStockSi())) {
					// error.add(new Error("35", "OnStock", "Please Enter On Stock Suminsured"));
					if (!req.getOnStockSi().matches("[0-9.]+")) {
						error.add("1340");
						// error.add(new Error("35", "OnStock","Please Enter Valid Number In Stock
						// Suminsured"));
					} else if (Double.valueOf(req.getOnStockSi()) < 0) {
						error.add("1340");
						// error.add(new Error("35", "OnStock","Please Enter Valid Number In Stock
						// Suminsured"));
					}
				}
				
				if (StringUtils.isNotBlank(req.getOnAssetsSi())) {
					// error.add(new Error("35", "OnAssets", "Please Enter On Assets Suminsured"));
					if (!req.getOnAssetsSi().matches("[0-9.]+")) {
						error.add("1342");
						// error.add(new Error("35", "OnAssets","Please Enter Valid Number In Assets
						// Suminsured"));
					} else if (Double.valueOf(req.getOnAssetsSi()) < 0) {
						error.add("1343");
						// error.add(new Error("35", "OnAssets","Please Enter Valid Number In Assets
						// Suminsured"));
					}
				}
				
				if (StringUtils.isBlank(req.getBuildingSuminsured()) && StringUtils.isBlank(req.getOnStockSi())
						&& StringUtils.isBlank(req.getOnAssetsSi())) {
					error.add("1344");
					// error.add(new Error("35", "FireAndAlliedPerills","Please Enter Atleast One
					// Suminsured in Fire And Allied Perills"));
					
				} else if (StringUtils.isNotBlank(req.getBuildingSuminsured())
						&& StringUtils.isNotBlank(req.getOnStockSi()) && StringUtils.isNotBlank(req.getOnAssetsSi())) {
					if (req.getBuildingSuminsured().matches("[0-9.]+") && req.getOnStockSi().matches("[0-9.]+")
							&& req.getOnAssetsSi().matches("[0-9.]+")) {
						if (Double.valueOf(req.getBuildingSuminsured()) == 0 && Double.valueOf(req.getOnStockSi()) == 0
								&& Double.valueOf(req.getOnAssetsSi()) == 0) {
							error.add("1345");
							// error.add(new Error("35", "FireAndAlliedPerills","Please Enter Atleast One
							// Suminsured Above Zero in Fire And Allied Perills"));
						}
					}
				}
				
//		if ( StringUtils.isBlank(req.getIndemityPeriod())) {
//			error.add(new Error("35", "IndemityPeriod", "Please Select Indemity Period"));
//		} else if (! req.getIndemityPeriod().matches("[0-9]+")) {
//			error.add(new Error("35", "IndemityPeriod","Please Select Valid Indemity Period"));
//		}
//		
//		if ( StringUtils.isBlank(req.getMakutiYn()) ) {
//			error.add(new Error("35", "MakutiYn", "Please Select Makuti Yes/No"));
//		} else if ( !( req.getMakutiYn().equalsIgnoreCase("Y") || req.getMakutiYn().equalsIgnoreCase("N")) ) {
//			error.add(new Error("35", "MakutiYn","Please Select Valid Makuti Yes/No"));
//		}
				// Company 100004
			} else if ("100004".equalsIgnoreCase(req.getInsuranceId())) {
				
				if (StringUtils.isBlank(req.getBuildingSuminsured()) && StringUtils.isBlank(req.getStockInTradeSi())
						&& StringUtils.isBlank(req.getFirePlantSi()) && StringUtils.isBlank(req.getFireEquipSi())) {
					error.add("1346");
					// error.add(new Error("35", "Suminsured","Please Enter Atleast One
					// Suminsured"));
					
				}
				
				else if (req.getBuildingSuminsured() != null && Double.valueOf(req.getBuildingSuminsured()) <= 0
						&& Double.valueOf(req.getStockInTradeSi()) <= 0 && Double.valueOf(req.getFirePlantSi()) <= 0
						&& Double.valueOf(req.getFireEquipSi()) <= 0) {
					error.add("1347");
					// error.add(new Error("35", "Suminsured","Please Enter Atleast One Suminsured
					// Above Zero"));
				}
				
				if (StringUtils.isNotBlank(req.getBuildingSuminsured())) {
					
					if (!req.getBuildingSuminsured().matches("[0-9.]+")) {
						error.add("1348");
						// error.add(new Error("35", "BuildingSuminsured","Please Enter Valid Number In
						// Building Suminsured"));
					}
				}
				
				if (StringUtils.isNotBlank(req.getStockInTradeSi())) {
					
					if (!req.getStockInTradeSi().matches("[0-9.]+")) {
						error.add("1349");
						// error.add(new Error("36", "StockSunmInsured","Please Enter Valid Number In
						// Stock Suminsured"));
					}
				}
				
				if (StringUtils.isNotBlank(req.getFirePlantSi())) {
					
					if (!req.getFirePlantSi().matches("[0-9.]+")) {
						error.add("1350");
						// error.add(new Error("37", "PlantSunmInsured","Please Enter Valid Number In
						// Plant Suminsured"));
					}
				}
				if (StringUtils.isNotBlank(req.getFireEquipSi())) {
					
					if (!req.getFireEquipSi().matches("[0-9.]+")) {
						error.add("1351");
						// error.add(new Error("37", "EquipmentSunmInsured","Please Enter Valid Number
						// In Equipment Suminsured"));
					}
				}
				
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			error.add("1352");
			
		}
		
		return error;
	}
	
	@Override
	public List<String> validateContentDetails(ContentSaveReq req, Double si) {
		// TODO Auto-generated method stub
		List<String> error = new ArrayList<String>();
		try {
			// Primary Key Validation
			if (StringUtils.isBlank(req.getProductId())) {
				error.add("1353");
				// error.add(new Error("01", "ProductId", "Please Select ProductId " ));
			}
			if (StringUtils.isBlank(req.getSectionId().toString())) {
				error.add("1354");
				// error.add(new Error("01", "SectionId", "Please Select SectionId"));
			}
			if (StringUtils.isBlank(req.getRiskId())) {
				error.add("1355");
				// error.add(new Error("01", "RiskId", "Please Select RiskId "));
			}
			if (StringUtils.isBlank(req.getRequestReferenceNo())) {
				error.add("1356");
				// error.add(new Error("01", "RequestReferenceNo", "Please Select Request
				// Reference No "));
			}
			if (StringUtils.isBlank(req.getInsuranceId())) {
				error.add("1357");
				// error.add(new Error("01", "InsuranceId", "Please Select Insurance Id "));
			}
			if (StringUtils.isBlank(req.getCreatedBy())) {
				error.add("1358");
				// error.add(new Error("01", "CreatedBy", "Please Select Created By "));
			}
			
			// New Inputs Validation
			if (StringUtils.isNotBlank(req.getInsuranceId()) && "100004".equalsIgnoreCase(req.getInsuranceId())
					&& StringUtils.isNotBlank(req.getProductId()) && "24".equalsIgnoreCase(req.getProductId())) {
				
				if (StringUtils.isBlank(req.getContentSuminsured())) {
					error.add("1359");
					// error.add(new Error("35", "ContentSuminsured", "Please Enter Content
					// Suminsured"));
				} else if (!req.getContentSuminsured().matches("[0-9.]+")) {
					error.add("1360");
					// error.add(new Error("35", "ContentSuminsured","Please Enter Valid Number In
					// Content Suminsured"));
				} else if (Double.valueOf(req.getContentSuminsured()) <= 0) {
					error.add("1361");
					// error.add(new Error("35", "ContentSuminsured","Please Enter Content
					// Suminsured Above Zero"));
				}
				
			} else if (StringUtils.isNotBlank(req.getInsuranceId())
					&& "100004".equalsIgnoreCase(req.getInsuranceId())) {
				if (StringUtils.isNotBlank(req.getEquipmentSi())) {
					if (!req.getEquipmentSi().matches("[0-9.]+")) {
						error.add("1362");
						// error.add(new Error("35", "Equipment Suminsured","Please Enter Valid Number
						// In Equipment Suminsured"));
					}
				}
				if (StringUtils.isNotBlank(req.getJewellerySi())) {
					if (!req.getJewellerySi().matches("[0-9.]+")) {
						error.add("1363");
						// error.add(new Error("35", "Jewellery Suminsured","Please Enter Valid Number
						// In Jewellery Suminsured"));
					}
				}
				if (StringUtils.isNotBlank(req.getPaitingsSi())) {
					if (!req.getPaitingsSi().matches("[0-9.]+")) {
						error.add("1364");
						// error.add(new Error("35", "Work Of Art Suminsured","Please Enter Valid Number
						// In Work Of Art Suminsured"));
					}
				}
				if (StringUtils.isNotBlank(req.getCarpetsSi())) {
					if (!req.getCarpetsSi().matches("[0-9.]+")) {
						error.add("1365");
						// error.add(new Error("35", "Carpets Suminsured","Please Enter Valid Number In
						// Carpets Suminsured"));
					}
				}
				
				if (StringUtils.isBlank(req.getEquipmentSi()) && StringUtils.isBlank(req.getJewellerySi())
						&& StringUtils.isBlank(req.getPaitingsSi()) && StringUtils.isBlank(req.getCarpetsSi())
						&& StringUtils.isBlank(req.getContentSuminsured())) {
					error.add("1366");
					// error.add(new Error("35", "Content Suminsured","Please Enter Alteat One
					// Suminsured in Content Section "));
				}
				
			}
			/*
			 * else { if ( StringUtils.isBlank(req.getContentSuminsured())) {
			 * error.add("1367"); //error.add(new Error("35", "ContentSuminsured",
			 * "Please Enter Content Suminsured")); } else if (!
			 * req.getContentSuminsured().matches("[0-9.]+")) { error.add("1368");
			 * //error.add(new Error("35",
			 * "ContentSuminsured","Please Enter Valid Number In Content Suminsured"));
			 * }else if ( Double.valueOf(req.getContentSuminsured()) <= 0 ) {
			 * error.add("1369"); //error.add(new Error("35",
			 * "ContentSuminsured","Please Enter Content Suminsured Above Zero")); }else {
			 * EserviceBuildingDetails buildingData =
			 * buildRepo.findByRequestReferenceNoAndRiskIdAndSectionId(req.
			 * getRequestReferenceNo() , Integer.valueOf(req.getRiskId()) , "1"); if
			 * (buildingData !=null && buildingData.getBuildingSuminsured()!=null && si !=
			 * null) { Double a1 =
			 * Double.valueOf(buildingData.getBuildingSuminsured().toPlainString()); Double
			 * b1 = si / 2; Double c1 = StringUtils.isBlank(req.getContentSuminsured()) ? 0
			 * : Double.valueOf(req.getContentSuminsured()); if
			 * (StringUtils.isNotBlank(req.getContentSuminsured()) && c1 > b1) {
			 * error.add("1370"); //error.add(new Error("38", "ContentSuminsured",
			 * "Content Suminsured Should not be greater than 50 % of BuildingSuminsured"));
			 * }
			 * 
			 * }
			 * 
			 * } }
			 */
			
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			error.add("1371");
		}
		return error;
	}
	
	@Override
	public List<String> validateElectronicEquipDetails(List<ElectronicEquipSaveReq> reqList) {
		// TODO Auto-generated method stub
		List<String> error = new ArrayList<String>();
		try {
			for (ElectronicEquipSaveReq req : reqList) {
				// Primary Key Validation
				if (StringUtils.isBlank(req.getProductId())) {
					error.add("1372");
					// error.add(new Error("01", "ProductId", "Please Select ProductId " ));
				}
				
				if (StringUtils.isBlank(req.getRiskId())) {
					error.add("1373");
					// error.add(new Error("01", "RiskId", "Please Select RiskId "));
				}
				if (StringUtils.isBlank(req.getRequestReferenceNo())) {
					error.add("1374");
					// error.add(new Error("01", "RequestReferenceNo", "Please Select Request
					// Reference No "));
				}
				if (StringUtils.isBlank(req.getInsuranceId())) {
					error.add("1375");
					// error.add(new Error("01", "InsuranceId", "Please Select Insurance Id "));
				}
				if (StringUtils.isBlank(req.getCreatedBy())) {
					error.add("1376");
					// error.add(new Error("01", "CreatedBy", "Please Select Created By "));
				}
				
				// Input Fields Validation
				if (StringUtils.isNotBlank(req.getProductId()) && "42".equalsIgnoreCase(req.getProductId())) {
					if (StringUtils.isBlank(req.getOccupationType())) {
						error.add("1377");
						// error.add(new Error("35", "OccupationType", "Please Select Cyber Occupation
						// Type"));
					} else if (!req.getOccupationType().matches("[0-9.]+")) {
						error.add("1378");
						// error.add(new Error("35", "ElecEquipSuminsured","Please Enter Valid Number In
						// Electronic Equipment Suminsured"));
					}
					
					if (StringUtils.isBlank(req.getSectionId())) {
						error.add("1379");
						// error.add(new Error("01", "PlanType", "Please Select Plan Type"));
					}
				} else {
					if (StringUtils.isBlank(req.getSectionId())) {
						error.add("1380");
						// error.add(new Error("01", "SectionId", "Please Select SectionId"));
					}
					
					if (StringUtils.isBlank(req.getElecEquipSuminsured())) {
						error.add("1381");
						// error.add(new Error("35", "ElecEquipSuminsured", "Please Enter Electronic
						// Equipment Suminsured"));
					} else if (!req.getElecEquipSuminsured().matches("[0-9.]+")) {
						error.add("1382");
						// error.add(new Error("35", "ElecEquipSuminsured","Please Enter Valid Number In
						// Electronic Equipment Suminsured"));
					} else if (Double.valueOf(req.getElecEquipSuminsured()) <= 0) {
						error.add("1383");
						// error.add(new Error("35", "ElecEquipSuminsured","Please Enter Electronic
						// Equipment Suminsured Above Zero"));
					}
				}
				if (StringUtils.isNotBlank(req.getProductId()) && "25".equalsIgnoreCase(req.getProductId())) {
					// Additional Field Validations
					if (StringUtils.isBlank(req.getContentId())) {
						error.add("2265");// content cannot be empty
					}
					if (StringUtils.isBlank(req.getLocationName())) {
						error.add("2270"); // Error code for LocationName Blank
					} else if (!req.getLocationName().matches("[a-zA-Z]+")) {
						error.add("2271"); // Error code for valid LocationName (alphabets only)
					} else if (req.getLocationName().length() > 100) {
						error.add("2271"); // Error code for LocationName max length exceeded
					}
					
					if (StringUtils.isBlank(req.getSerialNo())) {
						error.add("2266"); // Error code for SerialNo Blank
					} else if (!req.getSerialNo().matches("[0-9]+")) {
						error.add("2267"); // Error code for valid SerialNo (numbers only)
					}
					
					if (StringUtils.isBlank(req.getDescription())) {
						error.add("2268"); // Error code for Description Blank
					} else if (req.getDescription().length() > 1000) {
						error.add("2269"); // Error code for Description max length exceeded
					}
				}
				
			}
			
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			error.add("1384");
		}
		return error;
	}
	
	@Override // Corporate plus main info skip validation (12 sections)
	public List<String> mainInfoValidation(MainInfoValidationReq req) {
		List<String> error = new ArrayList<String>();
		
		try {
			List<EserviceSectionDetails> secs = secRepo.findByRequestReferenceNoAndStatus(req.getRequestReferenceNo(),
					"Y");
			List<EserviceBuildingDetails> build = buildRepo.findByRequestReferenceNo(req.getRequestReferenceNo());
			EserviceBuildingDetails data = build.get(0);
			
			if (secs.size() > 0) {
				
				for (EserviceSectionDetails sec : secs) {
					
					if (sec.getSectionId().equalsIgnoreCase("40")) { // Fire And Allied Perills
						
						if (data.getSumInsured() == null || data.getSumInsured().equals(BigDecimal.ZERO)) {
							error.add("1513");
							
//						if(data.getBuildingSuminsured()==null || data.getBuildingSuminsured().equals(BigDecimal.ZERO)) {
//							error.add("1513");
							
							// error.add(new Error("35", sec.getSectionName(),"Please Enter Data in Section
							// " + sec.getSectionName()));
//						}
							
						}
						
						if (sec.getSectionId().equalsIgnoreCase("41")) { // Machinery Breakdown
							
							// Input Fields Validation
							
							if (data.getSumInsured() == null || data.getSumInsured().equals(BigDecimal.ZERO)) {
								error.add("1513");
								// error.add(new Error("35", sec.getSectionName(),"Please Enter Data in Section
								// " + sec.getSectionName()));
							}
//						if ((data.getPowerPlantSi()==null || data.getPowerPlantSi().equals(BigDecimal.ZERO))
//								&& (data.getElecMachinesSi()==null || data.getElecMachinesSi().equals(BigDecimal.ZERO))
//								&& (data.getEquipmentSi()==null || data.getEquipmentSi().equals(BigDecimal.ZERO))
//								&& (data.getGeneralMachineSi()==null || data.getGeneralMachineSi().equals(BigDecimal.ZERO))
//								&& (data.getManuUnitsSi()==null || data.getManuUnitsSi().equals(BigDecimal.ZERO))
//								&& (data.getBoilerPlantsSi()==null || data.getBoilerPlantsSi().equals(BigDecimal.ZERO))
//								&& (data.getMachineEquipSi()==null || data.getMachineEquipSi().equals(BigDecimal.ZERO))
//								) {
//							error.add("1513");
//						//	error.add(new Error("01", sec.getSectionName(),"Please Enter Data in Section "+ sec.getSectionName() ));
							
//						//	error.add(new Error("01", sec.getSectionName(),"Please Enter Data in Section "+ sec.getSectionName() ));
							
							// error.add(new Error("01", sec.getSectionName(),"Please Enter Data in Section
							// "+ sec.getSectionName() ));
							
//						}
							
						}
						
//						if (sec.getSectionId().equalsIgnoreCase("42")) { // Money Insurance
//
//							if ((data.getMoneyAnnualEstimate() == null
//									|| data.getMoneyAnnualEstimate().equals(BigDecimal.ZERO))
//									&& (data.getMoneyCollector() == null
//											|| data.getMoneyCollector().equals(BigDecimal.ZERO))
//									&& (data.getMoneyDirectorResidence() == null
//											|| data.getMoneyDirectorResidence().equals(BigDecimal.ZERO))
//									&& (data.getMoneyOutofSafe() == null
//											|| data.getMoneyOutofSafe().equals(BigDecimal.ZERO))
//									&& (data.getMoneySafeLimit() == null
//											|| data.getMoneySafeLimit().equals(BigDecimal.ZERO))) {
//								error.add("1513");
//								// error.add(new Error("01", sec.getSectionName(),"Please Enter Data in Section
//								// "+ sec.getSectionName() ));
//							}
//
//						}
						
						if (sec.getSectionId().equalsIgnoreCase("43") || sec.getSectionId().equalsIgnoreCase("45")) { // Fidelity
							// Guarantee,Employers
							// Liabilty
							
							List<ProductEmployeeDetails> emp = empRepo.findByRequestReferenceNoAndSectionId(
									req.getRequestReferenceNo(), sec.getSectionId());
							
							if (!(emp.size() > 0)) {
								error.add("1513");
								// error.add(new Error("35", sec.getSectionName(), "Please Enter Data for
								// Section "+sec.getSectionName()));
							}
							
						}
						
						if (sec.getSectionId().equalsIgnoreCase("47")) { // Content
							
//						if(data.getContentSuminsured()==null || data.getContentSuminsured().equals(BigDecimal.ZERO)) {
//							error.add("1513");
							
//							//error.add(new Error("35", sec.getSectionName(),"Please Enter Data in Section " + sec.getSectionName()));
//						}
							if (data.getSumInsured() == null || data.getSumInsured().equals(BigDecimal.ZERO)) {
								error.add("1513");
								
								// error.add(new Error("35", sec.getSectionName(),"Please Enter Data in Section
								// " + sec.getSectionName()));
//						}
								
							}
							
							if (sec.getSectionId().equalsIgnoreCase("52")) { // Burglary and House breaking
								
								if (data.getNatureOfTradeId() == null) {
									error.add("1513");
									// error.add(new Error("01", sec.getSectionName(),"Please Enter Data in Section
									// "+sec.getSectionName()));
								}
								
							}
							
							if (sec.getSectionId().equalsIgnoreCase("56")) { // Accidental Damage
								
//						if(data.getAccDamageSi()==null || data.getAccDamageSi().equals(BigDecimal.ZERO)){
//							error.add(new Error("01", sec.getSectionName(),"Please Enter Data in Section "+sec.getSectionName()));
//						}
								
							}
							
//					if(sec.getSectionId().equalsIgnoreCase("40")) { //Electronic Equipments section id not in section master
//						
//						if(data.getElecEquipSuminsured()==null || data.getElecEquipSuminsured().equals(BigDecimal.ZERO)){
//							error.add(new Error("01", sec.getSectionName(),"Please Enter Data in Section "+sec.getSectionName()));
//						}
//						
//					}
							
							if (sec.getSectionId().equalsIgnoreCase("53")) { // Plate Glass
								
//						if(data.getPlateGlassSi()==null || data.getPlateGlassSi().equals(BigDecimal.ZERO)){
//							error.add("1513");
								
//							//error.add(new Error("01", sec.getSectionName(),"Please Enter Data in Section "+sec.getSectionName()));
//						}
								
								if (data.getSumInsured() == null || data.getSumInsured().equals(BigDecimal.ZERO)) {
									error.add("1513");
									// error.add(new Error("35", sec.getSectionName(),"Please Enter Data in Section
									// " + sec.getSectionName()));
								}
								
								// error.add(new Error("01", sec.getSectionName(),"Please Enter Data in Section
								// "+sec.getSectionName()));
//						}
								
							}
							
							if (sec.getSectionId().equalsIgnoreCase("3")) { // All Risk
								
//						if(data.getAllriskSuminsured()==null || data.getAllriskSuminsured().equals(BigDecimal.ZERO)){
//							error.add("1513");
//							//error.add(new Error("01", sec.getSectionName(),"Please Enter Data in Section "+sec.getSectionName()));
//						}
								if (data.getSumInsured() == null || data.getSumInsured().equals(BigDecimal.ZERO)) {
									error.add("1513");
									// error.add(new Error("35", sec.getSectionName(),"Please Enter Data in Section
									// " + sec.getSectionName()));
								}
								
								// error.add(new Error("01", sec.getSectionName(),"Please Enter Data in Section
								// "+sec.getSectionName()));
//						}
								
							}
							
							if (sec.getSectionId().equalsIgnoreCase("54")) { // Public Liability
								
								List<EserviceCommonDetails> humanDatas = commRepo.findByRequestReferenceNoAndSectionId(
										req.getRequestReferenceNo(), sec.getSectionId());
								
								if (humanDatas.size() > 0) {
									EserviceCommonDetails humanData = humanDatas.get(0);
									
//							if(humanData.getLiabilitySi()==null || humanData.getLiabilitySi().equals(BigDecimal.ZERO)) 
//								error.add("1513");
									// error.add(new Error("01", sec.getSectionName(),"Please Enter Data in Section
									// "+sec.getSectionName()));
									
								} else
									error.add("1513");
								// error.add(new Error("01", sec.getSectionName(),"Please Enter Data in Section
								// "+sec.getSectionName()));
								
							}
							
						}
						
					}
					
				}
			}
			
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return error;
	}
	
	@Override
	public List<String> validatePersonalAccident(List<SlidePersonalAccidentSaveReq> reqList) {
		List<String> error = new ArrayList<String>();
		
		try {
			Long rowNo = 0L;
			List<String> occupationId = new ArrayList<String>();
			
			if (reqList == null || reqList.size() <= 0) {
				error.add("1514");
				// error.add(new Error("01", "EmployeeList", "Please Enter Atleast One Row"));
				
			} else {
				for (SlidePersonalAccidentSaveReq req : reqList) {
					rowNo = rowNo + 1;
					
					if (StringUtils.isBlank(req.getProductId())) {
						error.add("1515");
						// error.add(new Error("01", "ProductId", "Please Select ProductId Row No In Row
						// No : " + rowNo));
					}
					if (StringUtils.isBlank(req.getSectionId())) {
						error.add("1516");
						// error.add(new Error("01", "SectionId", "Please Select SectionId Row No In Row
						// No : " + rowNo));
					}
					if (StringUtils.isBlank(req.getRiskId())) {
						error.add("1517");
						// error.add(new Error("01", "RiskId", "Please Select RiskId Row No In Row No :
						// " + rowNo));
					}
					if (StringUtils.isBlank(req.getRequestReferenceNo())) {
						error.add("1518");
						// error.add(new Error("01", "RequestReferenceNo", "Please Select Request
						// Reference No In Row No : " + rowNo));
					}
					if (StringUtils.isBlank(req.getInsuranceId())) {
						error.add("1519");
						// error.add(new Error("01", "InsuranceId", "Please Select Insurance Id In Row
						// No : " + rowNo));
					}
					if (StringUtils.isBlank(req.getCreatedBy())) {
						error.add("1520");
						// error.add(new Error("01", "CreatedBy", "Please Select Created By In Row No :
						// " + rowNo));
					}
					
					if (StringUtils.isBlank(req.getSumInsured())) {
						if (reqList.size() == 1) {
							error.add("1521");
							// error.add(new Error("44", "SumInsured", "Please Enter Personal Accident
							// SumInsured "));
						} else {
							error.add("1522");
							// error.add(new Error("44", "SumInsured", "Please Enter SumInsured In Row No :
							// " + rowNo));
						}
						
					} else if (!req.getSumInsured().matches("[0-9.]+")) {
						if (reqList.size() == 1) {
							error.add("1523");
							// error.add(new Error("44", "SumInsured","Please Enter Valid Number In Personal
							// Accident SumInsured "));
						} else {
							error.add("1524");
							// error.add(new Error("44", "SumInsured","Please Enter Valid Number In
							// SumInsured In Row No : " + rowNo));
						}
						
					} else if (Double.valueOf(req.getSumInsured()) <= 0) {
						if (reqList.size() == 1) {
							error.add("1525");
							// error.add(new Error("44", "SumInsured", "Please Enter Personal Accident
							// SumInsured Above Zero "));
						} else {
							error.add("1526");
							// error.add(new Error("44", "SumInsured", "Please Enter SumInsured Above Zero
							// In Row No : " + rowNo));
						}
						
					}
					
					if (StringUtils.isBlank(req.getOccupationType())) {
						if (reqList.size() == 1) {
							error.add("1527");
							// error.add(new Error("44", "OccupationType", "Please Select Personal Accident
							// OccupationType " ));
						} else {
							error.add("1528");
							// error.add(new Error("44", "OccupationType", "Please Select Personal Accident
							// OccupationType In Row No : " + rowNo));
						}
						
					} else if (!req.getOccupationType().matches("[0-9.]+")) {
						if (reqList.size() == 1) {
							error.add("1529");
							// error.add(new Error("44", "OccupationType","Please Enter Valid Number In
							// Personal Accident OccupationType "));
						} else {
							error.add("1530");
							// error.add(new Error("44", "OccupationType","Please Enter Valid Number In
							// Personal Accident OccupationType In Row No : " + rowNo));
						}
						
					} else {
						List<String> filterOccupation = occupationId.stream()
								.filter(o -> o.equalsIgnoreCase(req.getOccupationType())).collect(Collectors.toList());
						if (filterOccupation.size() > 0) {
//							error.add("1531");
							// error.add(new Error("44", "OccupationType"," Occupation Duplicate In Row No :
							// " + rowNo));
						} else {
							occupationId.add(req.getOccupationType());
						}
					}
					
					if (StringUtils.isBlank(req.getOccupationType())) {
						error.add("1532");
						// error.add(new Error("23", "Occupation", "Please Select Occupation"));
					} else if (req.getOccupationType().equalsIgnoreCase("99999")) {
						if (StringUtils.isBlank(req.getOtherOccupation())) {
							error.add("1533");
							// error.add(new Error("47", "Other Occupation", "Please Enter Other
							// Occupation"));
						} else if (req.getOtherOccupation().length() > 100) {
							error.add("1534");
							// error.add(new Error("47","Other Occupation", "Please Enter Other Occupation
							// within 100 Characters"));
						} else if (!req.getOtherOccupation().matches("[a-zA-Z\\s]+")) {
							error.add("1535");
							// error.add(new Error("47","Other Occupation", "Please Enter Valid Other
							// Occupation"));
						}
					}
					
					if (StringUtils.isBlank(req.getTotalNoOfPersons())) {
						error.add("1536");
						// error.add(new Error("44", "TotalNoOfPersons", "Please Select Total No Of
						// Persons In Row No : " + rowNo));
					} else if (!req.getTotalNoOfPersons().matches("[0-9.]+")) {
						error.add("1537");
						// error.add(new Error("44", "TotalNoOfPersons","Please Enter Valid Number In
						// Total No Of Persons In Row No : " + rowNo));
					} else if (Long.valueOf(req.getTotalNoOfPersons()) <= 0) {
						error.add("1538");
						// error.add(new Error("44", "TotalNoOfPersons","Please Enter Total No Of
						// Persons Above Zero In Row No : " + rowNo));
					}
				}
			}
			
		} catch (Exception e) {
			
			log.error(e);
			e.printStackTrace();
			error.add("1539");
			// error.add(new Error("19", "Common Error", e.getMessage()));
		}
		return error;
	}
	
	public List<String> validateBond(BondCommonReq req) {
		List<String> error = new ArrayList<String>();
		try {
			if (StringUtils.isBlank(req.getRequestReferenceNo())) {
				error.add("1540");
				
			}
			// Validate BondDetails
			if (req.getBondDetails() == null || req.getBondDetails().isEmpty()) {
				error.add("2262");
				error.add("2263");
				error.add("2264");// Bond Details cannot be empty
			} else {
				for (BondMajorReq bond : req.getBondDetails()) {
					if (StringUtils.isBlank(bond.getBondType())) {
						error.add("2262");// Bond Type cannot be empty
						break;
					}
					// Validate BondYear
					if (StringUtils.isBlank(bond.getBondYear())) {
						error.add("2263"); // Number Of Years can't be empty
						break;
					}
					
					// Validate BondSumInsured
					if (StringUtils.isBlank(bond.getBondSumInsured())) {
						error.add("2264"); // SumInsured cannot be empty
						break;
					}
					
					// Check for valid numeric value in BondSumInsured
					if (!bond.getBondSumInsured().matches("\\d+(\\.\\d+)?")) {
						error.add("2265"); // Valid Sum insured (no alphabets)
						break;
					}
					
					// Check if SumInsured is greater than 0
					BigDecimal sumInsured = new BigDecimal(bond.getBondSumInsured());
					if (sumInsured.compareTo(BigDecimal.ZERO) <= 0) {
						error.add("2265"); // Sum insured Greater than 0
						break;
					}
				}
			}
		} catch (Exception ss) {
			System.out.println("**************Bond validation Block Catched Exception**********");
			ss.printStackTrace();
			return null;
		}
		return error;
	}
	
	@Override
	public List<String> validateBuilding(SlideBuildingSaveReq req) {
		List<String> error = new ArrayList<String>();
		
		try {
			// For Domestic Product
			// Setion ids
			if (StringUtils.isBlank(req.getBuildingUsageId())) {
				error.add("1540");
				// error.add(new Error("32", "BuidingUsageId", "Please Select Building Usage
				// Id"));
			}
			
			int year = Calendar.getInstance().get(Calendar.YEAR);
			
			if (StringUtils.isNotBlank(req.getBuildingBuildYear())) {
				Integer year1 = Integer.valueOf(req.getBuildingBuildYear());
				Integer diff = year - year1;
				if ((StringUtils.isNotBlank(req.getBuildingBuildYear())
						&& !req.getBuildingBuildYear().matches("[0-9]+")) || req.getBuildingBuildYear().length() > 4) {
					error.add("1541");
					// error.add(new Error("33", "BuildingBuildYear",
					// "Please Enter Building Build Year Format in YYYY"));
				} else if ((StringUtils.isNotBlank(req.getBuildingBuildYear())
						&& !req.getBuildingBuildYear().matches("[0-9]+")) || req.getBuildingBuildYear().length() < 4) {
					error.add("1541");
					// error.add(new Error("33", "BuildingBuildYear",
					// "Please Enter Building Build Year Format in YYYY"));
				} else if (StringUtils.isNotBlank(req.getBuildingBuildYear())) {
					if (year1 > year) {
						error.add("1542");
						// error.add(new Error("33", "BuildingBuildYear",
						// "Please Enter Building Build Year as Past Year"));
					} else if (diff > 100) {
						error.add("1543");
						// error.add(new Error("33", "BuildingBuildYear",
						// "Please Enter Building Build Year within 100 years"));
						
					}
				}
			}
			
			if ((StringUtils.isBlank(req.getBuildingOwnerYn())) || req.getBuildingOwnerYn() == null) {
				error.add("1544");
				// error.add(new Error("37", "BuildingOwnerYn", "Please Select
				// BuildingOwnerYn"));
			} else if ((StringUtils.isNotBlank(req.getBuildingOwnerYn()))
					&& req.getBuildingOwnerYn().equalsIgnoreCase("Y")) {
				if (StringUtils.isBlank(req.getBuildingSumInsured())) {
					error.add("1545");
					// error.add(new Error("35", "BuildingSuminsured", "Please Enter
					// BuildingSuminsured"));
				} else if (!req.getBuildingSumInsured().matches("[0-9.]+")) {
					error.add("1546");
					// error.add(new Error("35", "BuildingSuminsured",
					// "Please Enter Valid Number In BuildingSuminsured"));
				}
				
			}
			
			// New Inputs Validation
			if (StringUtils.isNotBlank(req.getInsuranceId()) && "100004".equalsIgnoreCase(req.getInsuranceId())) {
				if (StringUtils.isNotBlank(req.getWaterTankSi())) {
					if (!req.getWaterTankSi().matches("[0-9.]+")) {
						error.add("1547");
						// error.add(new Error("35", "Water Tank Suminsured","Please Enter Valid Number
						// In Water Tank Suminsured"));
					}
				}
				if (StringUtils.isNotBlank(req.getArchitectsSi())) {
					if (!req.getArchitectsSi().matches("[0-9.]+")) {
						error.add("1548");
						// error.add(new Error("35", "Architects Suminsured","Please Enter Valid Number
						// In Architects Suminsured"));
					}
				}
				if (StringUtils.isNotBlank(req.getLossOfRentSi())) {
					if (!req.getLossOfRentSi().matches("[0-9.]+")) {
						error.add("1549");
						// error.add(new Error("35", "Loss Of Rent Suminsured","Please Enter Valid
						// Number In Loss Of Rent Suminsured"));
					}
				}
				
				if (StringUtils.isBlank(req.getTypeOfProperty()) || req.getTypeOfProperty().equalsIgnoreCase("0")) {
					error.add("1550");
					// error.add(new Error("35", "TypeOfProperty","Please Select Type Of Property
					// "));
				}
				
			}
			
		} catch (Exception e) {
			
			log.error(e);
			e.printStackTrace();
			error.add("1551");
			// error.add(new Error("19", "Common Error", e.getMessage()));
		}
		return error;
	}
	
	public List<Error> validateFireAndAlliedPerills(CommonRequest req1) {
		List<Error> validation = new ArrayList<>();
		List<FireReqData> req = req1.getFiredel();
		try {
			/*
			 * s FireSuminsured > Business Interipation Suminsured for(int
			 * i=0;i<req.size();i++) { for(int j=i+1;j<req.size();j++) {
			 * System.out.println("req.get(i).getBusinessInterruption()"+req.get(i).
			 * getBusinessInterruption()+"req.get(j).getBusinessInterruption()"+req.get(j).
			 * getBusinessInterruption());
			 * System.out.println("req.get(i).getRiskId()"+req.get(j).getRiskId()+
			 * "req.get(i).getRiskId()"+req.get(j).getRiskId());
			 * 
			 * if((req.get(i).getBusinessInterruption().equals(req.get(j).
			 * getBusinessInterruption()) )&&
			 * (req.get(i).getRiskId().equals(req.get(j).getRiskId())) ) {
			 * 
			 * if(req1.getFiredel().get(i).getBuildingSumInsured()<req1.getFiredel().get(j).
			 * getBuildingSumInsured()) { Error errors=new Error(); errors.setCode("10");
			 * errors.setField("SumInsured");
			 * errors.setMessage("FireSumInsured "+req1.getFiredel().get(i).
			 * getBuildingSumInsured()
			 * +" Should Be Greater then Business Interupation Suminsured "+req1.getFiredel(
			 * ).get(j).getBuildingSumInsured()); validation.add(errors); } } } }
			 */
			
			for (int i = 0; i < req.size(); i++) {
				int j = i + 1;
				if (StringUtils.isBlank(req.get(i).getIndustryType())) {
					// Industry Type cannot be empty
					validation.add(new Error("01", "Industry Type", "Please Select  the Industry type in row :" + j)); //
				}
				if (StringUtils.isBlank(req.get(i).getRiskId())) {
					validation.add(new Error("02", "Risk Id", "Risk Id Can't Be Null :" + j)); //
					
				}
				
				if (StringUtils.isBlank(req.get(i).getIndustrytypedesc())) {
					validation.add(new Error("03", "Industry Description",
							"Please Select  the Industry Description in row :" + j));
				}
				if (StringUtils.isBlank(String.valueOf(req.get(i).getBuildingSumInsured()))) {
					validation
					.add(new Error("04", "Building SumInsured", "Please Enter the Building Suminsured :" + j));
				}
				if (StringUtils.isBlank(req.get(i).getSectionId())) {
					validation
					.add(new Error("05", "Building SumInsured", "Please Enter the Building Suminsured :" + j));
					
				}
				if (StringUtils.isBlank(req.get(i).getDescriptionOfRisk())) {
					validation
					.add(new Error("06", "Description of Risk", "Please Enter the Description of Risk :" + j));
				} else if (req.get(i).getDescriptionOfRisk().length() > 1000) {
					validation.add(new Error("07", "Description of Risk",
							"Valid and max 1000 char validation for Description Of Risk :" + j)); //
				}
				if (StringUtils.isBlank(req.get(i).getCoveringDetails())) {
					validation.add(new Error("08", "Covering Details", "Please Enter the Covering Details :" + j)); // cannot
					// be
					// empty
				} else if (req.get(i).getCoveringDetails().length() > 1000) {
					validation.add(new Error("08", "Covering Details",
							"Valid Covering Details and length max 1000 char :" + j)); // cannot be empty
					//
				}
				if (StringUtils.isBlank(req.get(i).getOccupationId())) {
					validation.add(new Error("08", "Industry Cover", "Please Select  the Industry Cover :" + j)); // cannot
					// be
					// empty
					
				}
				// error.addAll(validateLocationDetails(req.getLocationName(),req.getRegionName(),req.getDistrictName()));
				// Location Validation
				if (StringUtils.isBlank(req.get(i).getLocationName())) {
					validation.add(new Error("09", "Location Name", "Please Select  the Location Name :" + j)); // Location
					// Blank
					// validation
				} else if (req.get(i).getLocationName().length() > 100) {
					validation.add(new Error("10", "Location Name", "Enter the location name within 1000 char :" + j)); // Location
					// Blank
					// validation
					// Less than 100 characters are allowed
				} else if (!req.get(i).getLocationName().matches("[a-zA-Z ]+")) {
					validation.add(new Error("11", "Location Name", "Enter the Vaild Location Name :" + j)); // Location
					// Blank
					// validation
					// Please Enter valid Location (only alphabets allowed)
				}
				
				// Region Validation
				if (StringUtils.isBlank(req.get(i).getRegionCode())) {
					validation.add(new Error("12", "Region Code", "Please select the region :" + j)); // Location Blank
					// validation
					// Location Blank validation
					
				}
				
				// District Validation
				if (StringUtils.isBlank(req.get(i).getDistrictCode())) {
					validation.add(new Error("13", "District Code", "Please select the district :" + j)); // Location
					// Blank
					// validation
					// District Blank validation
				}
				/*
				 * if(Existing!=null) { error.add("1561"); }
				 */
			}
			
		} catch (Exception data) {
			System.out.println("Exception fire raw data............");
			data.printStackTrace();
			validation.add(new Error("14", "Common Error", "Oops something Went Wrong :")); // Location Blank validation
			
		}
		return validation;
	}
	
	public List<String> validatecommondetails(CommonRequest req1) {
		List<String> error = new ArrayList<String>();
		
		// EserviceBuildingDetails Existing
		// =buildRepo.findByRiskIdAndRequestReferenceNoAndSectionId(Integer.valueOf(req.getRiskId()),req.getRequestReferenceNo(),req.getSectionId());
		
		try {
			
			if (StringUtils.isBlank(req1.getCreatedBy())) {
				error.add("1562"); // Created By Can't Be Null
			}
			if (StringUtils.isBlank(req1.getInsuranceId())) {
				error.add("1563"); // Insurance Id can't Be Null
			}
			if (StringUtils.isBlank(req1.getProductId())) {
				error.add("1564"); // Product Id can't Be Null
			}
			if (StringUtils.isBlank(req1.getProductType())) {
				error.add("1565"); // Product Type can't Be Null
			}
			
			if (req1.getPolicyStartDate() == null) {
				error.add("1566"); //
			}
			if (req1.getPolicyStartDate() == null) {
				error.add("1571");// Please Enter PolicyStartDate
				// error.add(new Error("13", "PolicyStartDate", "Please Enter
				// PolicyStartDate"));
			} else if ((req1.getEndorsementType() == null || req1.getEndorsementType().equals(0))
					&& !"RQ".equalsIgnoreCase(req1.getStatus())) {
				int before = getBackDays(req1.getInsuranceId(), req1.getProductId(), req1.getCreatedBy());
				int days = before == 0 ? -1 : -before;
				long MILLS_IN_A_DAY = 1000 * 60 * 60 * 24;
				long backDays = MILLS_IN_A_DAY * days;
				Date today = new Date();
				Date resticDate = new Date(today.getTime() + backDays);
				long days90 = MILLS_IN_A_DAY * 90;
				Date after90 = new Date(today.getTime() + days90);
				if (req1.getPolicyStartDate().before(resticDate)) {
					error.add("1572");// Policy Start Date Before Day Not Allowed
					// error.add(new Error("14", "PolicyStartDate", "Policy Start Date Before " +
					// before + " Days Not Allowed "));
				} else if (req1.getPolicyStartDate().after(after90)) {
					error.add("1573");// PolicyStartDate even after 90 days Not Allowed
					// error.add(new Error("14", "PolicyStartDate", "PolicyStartDate even after 90
					// days Not Allowed"));
				}
				
			}
			if (StringUtils.isBlank(req1.getAgencyCode())) {
				error.add("1567"); //
				
			}
			if (StringUtils.isBlank(req1.getSubUserType())) {
				error.add("1568"); //
				
			}
			if (StringUtils.isBlank(req1.getBranchCode())) {
				error.add("1570"); //
				
			}
			if (StringUtils.isBlank(req1.getCustomerReferenceNo())) {
				error.add("1569"); // 1
				
			}
			
		} catch (Exception data) {
			System.out.println("The Exception occured in common save....");
			data.printStackTrace();
		}
		
		return error;
	}
	
	@Override
	public List<String> validateSlideBusinessInterruption(SlideBusinessInterruptionReq req) {
		
		List<String> error = new ArrayList<String>();
		
		try {
			if ("100002".equalsIgnoreCase(req.getInsuranceId())) {
				
				if ((StringUtils.isBlank(req.getGrossProfitSi()) || "0".equalsIgnoreCase(req.getGrossProfitSi())
						|| "0.0".equalsIgnoreCase(req.getGrossProfitSi()))
						&& (StringUtils.isBlank(req.getIndemnityPeriodSi())
								|| "0".equalsIgnoreCase(req.getIndemnityPeriodSi())
								|| "0.0".equalsIgnoreCase(req.getIndemnityPeriodSi()))) {
					
					error.add("1552");
				} else if (StringUtils.isNotBlank(req.getGrossProfitSi())) {
					if (Double.valueOf(req.getGrossProfitSi()) < 0.0) {
						error.add("1552");
					} else if (!req.getGrossProfitSi().matches("[0-9.]+")) {
						error.add("1552");
					}
				} else if (StringUtils.isNotBlank(req.getIndemnityPeriodSi())) {
					if (Double.valueOf(req.getIndemnityPeriodSi()) < 0.0) {
						error.add("1553");
					} else if (!req.getIndemnityPeriodSi().matches("[0-9.]+")) {
						error.add("1553");
					}
				}
				
				// error.add("1503");
				// error.add(new Error("01", "Additional increase in Cost of Working","Please
				// Enter Additional increase in Cost of Working "));
			}
			if (StringUtils.isBlank(req.getIndemnityPeriodSi())) {
				error.add("1553");
				// error.add(new Error("01", "Indemnity Period","Please Enter Indemnity Period
				// "));
			}
			
			else {
				
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			error.add("1505");
			// error.add(new Error("19", "Common Error", e.getMessage()));
		}
		return error;
	}
	
	@Override
	public List<String> validateSlideGoodsInTransit(SlideGoodsInTransitSaveReq req) {
		
		List<String> error = new ArrayList<String>();
		
		try {
			if ("100002".equalsIgnoreCase(req.getInsuranceId())) {
				if (StringUtils.isBlank(req.getTransportedBy())) {
					error.add("1506");
					// error.add(new Error("01", "Transported By","Please Enter Transported By "));
				}
				if (StringUtils.isBlank(req.getModeOfTransport())) {
					error.add("1507");
					// error.add(new Error("02", "Mode Of Transport","Please Enter Mode Of
					// Transport"));
				}
				
				if (StringUtils.isNotBlank(req.getSingleRoadSiFc())) {
					if (!req.getSingleRoadSiFc().matches("[0-9.]+")) {
						error.add("1509");
					} else if (Double.valueOf(req.getSingleRoadSiFc()) < 0.0) {
						error.add("1509");
					}
				}
				
				if ((StringUtils.isBlank(req.getEstAnnualCarriesSiFc())
						|| "0".equalsIgnoreCase(req.getEstAnnualCarriesSiFc())
						|| "0.0".equalsIgnoreCase(req.getEstAnnualCarriesSiFc()))
						&& (StringUtils.isBlank(req.getSingleRoadSiFc())
								|| "0".equalsIgnoreCase(req.getSingleRoadSiFc())
								|| "0.0".equalsIgnoreCase(req.getSingleRoadSiFc()))) {
					error.add("1153");
				}
//				if(StringUtils.isBlank(req.getSingleRoadSiFc())) {
//					error.add("1508");
//					//error.add(new Error("03", "SingleRoadLimit","Please Enter Single road limit"));
//				}
//				if(!req.getSingleRoadSiFc().matches("[0-9.]+")) {
//					error.add("1509");
//					//error.add(new Error("03", "SingleRoadLimit","Please Enter Valid Single road limit"));
//				}
				
//				if(StringUtils.isBlank(req.getEstAnnualCarriesSiFc())){
//					error.add("1510");
//					//error.add(new Error("05", "EstimatedAnnualCarries","Please Enter Estimated Annual Carries"));
//				} 
//				if(!req.getEstAnnualCarriesSiFc().matches("[0-9.]+")) {
//					error.add("1510");
//					//error.add(new Error("05", "EstimatedAnnualCarries","Please Enter Estimated Annual Carries"));
//
//				}
//				if(StringUtils.isBlank(req.getEstAnnualCarriesSiFc())|| "0".equalsIgnoreCase(req.getEstAnnualCarriesSiFc())|| "0.0".equalsIgnoreCase(req.getEstAnnualCarriesSiFc())){
//					error.add(new Error("05", "Field","Please Enter Estimated Annual Carries"));
//				}  
				if (StringUtils.isNotBlank(req.getEstAnnualCarriesSiFc())) {
					if (!req.getEstAnnualCarriesSiFc().matches("[0-9.]+")) {
						error.add("1510");
					} else if (Double.valueOf(req.getEstAnnualCarriesSiFc()) < 0.0) {
						error.add("1510");
					}
				}
				
				if (StringUtils.isBlank(req.getGeographicalCoverage())) {
					error.add("1511");
					// error.add(new Error("07", "GeographicalCoverage","Please Enter
					// GeographicalCoverage"));
				}
				
				else {
					
				}
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			error.add("1512");
			// error.add(new Error("19", "Common Error", e.getMessage()));
		}
		return error;
	}
	
	public List<String> vaildateInfoHealthDetails(List<SlideHIFamilyDetailsReq> req) {
		List<String> error = new ArrayList<String>();
		List<SlideHIFamilyDetailsReq> reqList = req;
		int count1 = 0;
		try {
			if (req == null || req.isEmpty()) {
				error.add("2159");
			} else {
				
				for (SlideHIFamilyDetailsReq REQ : reqList) {
					count1 = (int) reqList.stream()
							.filter(req1 -> req1.getNationalityId().equals(REQ.getNationalityId())).count();
					if (StringUtils.isBlank(REQ.getFirstName())) {
						error.add("2160");
						// System.out.println("FirstName is null");
					} else if (StringUtils.isBlank(REQ.getLastName())) {
						error.add("2161");
						// System.out.println("LastName is null");
					} else if (StringUtils.isBlank(REQ.getNationalityId())) {
						error.add("2162");
						// System.out.println("Nationality is null"); //NationalityId
					} else if (count1 > 1) {
						error.add("2163");
						break;
						// System.out.println("Invaild Nationality Id 1001");
					}
				}
			}
			
		} catch (Exception d) {
			log.error(d);
			d.printStackTrace();
			error.add("2180");
		}
		return error;
		
	}
	
	@Override
	public List<String> validateHealthInsurDetails(List<SlideHealthInsureSaveReq> reqList) {
		/* List<SlideHIFamilyDetailsReq> family = reqList.get(0).getFamilyDetails(); */
		List<String> error = new ArrayList<String>();
		
		try {
			Long rowNo = 0L;
			
			List<String> occupationId = new ArrayList<String>();
			long count = 0;
			if (reqList == null || reqList.size() <= 0) {
				error.add("2166");
				// error.add(new Error("01", "EployeeList", "Please Enter Atleast One Row"));
				
			} else {
				// check whethere is any relationtype=1
				count = reqList.stream().filter(req -> req.getRelationType().equals("1")).count();
				if (count <= 0) {
					error.add("2167");// Please Select Alteast One Principle
					// System.out.print("Alteast One principle should be Relationtype");
				} else if (count > 1) {
					error.add("2164");
				}
				
				for (int req = 0; req < reqList.size(); req++) {
					rowNo = rowNo + 1;
					
					if (StringUtils.isBlank(reqList.get(req).getProductId())) {
						error.add("2168");
						// error.add(new Error("01", "ProductId", "Please Select ProductId Row No In Row
						// No : " + rowNo));
					}
					if (StringUtils.isBlank(reqList.get(req).getSectionId())) {
						error.add("2169");
						// error.add(new Error("01", "SectionId", "Please Select SectionId Row No In Row
						// No : " + rowNo));
					}
					if (StringUtils.isBlank(reqList.get(req).getRiskId())) {
						error.add("2170");
						// error.add(new Error("01", "riskId", "Please Select RiskId Row No In Row No :
						// " + rowNo));
					}
					if (StringUtils.isBlank(reqList.get(req).getRequestReferenceNo())) {
						error.add("2171");
						// error.add(new Error("01", "RequestReferenceNo", "Please Select Request
						// Reference No In Row No : " + rowNo));
					}
					if (StringUtils.isBlank(reqList.get(req).getInsuranceId())) {
						error.add("2172");
						// error.add(new Error("01", "InsuranceId", "Please Select Insurance Id In Row
						// No : " + rowNo));
					}
					if (StringUtils.isBlank(reqList.get(req).getCreatedBy())) {
						error.add("2173");
						// error.add(new Error("01", "CreatedBy", "Please Select Created By In Row No :
						// " + rowNo));
					}
					
					if (StringUtils.isBlank(reqList.get(req).getRelationType())) {
						error.add("2174");
						// error.add(new Error("44", "Employers Liability Suminsured", "Please Enter
						// Employers Liability Suminsured In Row No : " + rowNo));
					}
					
					if (reqList.get(req).getDateOfBirth() == null) {
						error.add("2165");
						// error.add(new Error("44", "Employers Liability Suminsured", "Please Enter
						// Employers Liability Suminsured In Row No : " + rowNo));
					}
					// age calculation and vaildation
					if (reqList.get(req).getDateOfBirth() != null && reqList.get(req).getRelationType().equals("1")
							|| reqList.get(req).getRelationType().equals("2")) {
						Date dob = null;
						int age = 0;
						
						dob = reqList.get(req).getDateOfBirth();
						Date today = new Date();
						if (dob == null) {
							age = -1;
						} else {
							age = today.getYear() - dob.getYear();
						}
						// System.out.print("age==="+age);
						if (age >= 0 && age < 19) {
							if (reqList.get(req).getRelationType().equals("1")) {
								error.add("2175");
							} else if (reqList.get(req).getRelationType().equals("2")) {
								error.add("2176");
							}
							
						} // Dob Not Accepted Less than 19 Years For Induvidual
						
					}
					if (reqList.get(req).getDateOfBirth() != null && reqList.get(req).getRelationType().equals("3")) {
						Date dob = null;
						int age = 0;
						dob = reqList.get(req).getDateOfBirth();
						Date today = new Date();
						age = today.getYear() - dob.getYear();
						
						// System.out.print("age==="+age);
						if (!(age >= 19)) {
							// System.out.print("child should be lesser then 18");
							error.add("2177");
						} // Dob Not Accepted Greater than 18 Years For Children
						
					}
					
					if (reqList.get(req).getNickName() == null || reqList.get(req).getNickName().trim().isEmpty()) {
						error.add("2178");
						// error.add(new Error("44", "Employers Liability Suminsured", "Please Enter
						// Employers Liability Suminsured In Row No : " + rowNo));
					} else if (reqList.get(req).getNickName().length() > 200) {
						error.add("2179");
						// error.add(new Error("44", "Employers Liability Suminsured", "Please Enter
						// Employers Liability Suminsured In Row No : " + rowNo));
					}
				}
			}
			
		} catch (Exception e) {
			
			log.error(e);
			e.printStackTrace();
			error.add("2180");
			// error.add(new Error("19", "Common Error", e.getMessage()));
		}
		return error;
	}
	
//-------------------------------------------------------------------------------------------------------------------------------------
//	Non Motor Validation Company Wise 
	private BigDecimal getGpaExpenseValue(String itemCode, String productId) {

	    Optional<ListItemValue> item =
	            listItemValueRepository.findByItemTypeAndItemCodeAndProductIdAndStatus(
	                    "GPAEXPENSE",
	                    itemCode,
	                    productId,
	                    "Y"
	            );

	    if (item.isPresent() && StringUtils.isNotBlank(item.get().getItemValue())) {
	        return new BigDecimal(item.get().getItemValue().trim());
	    }

	    return null;
	}
	private void validateGPAParam2(NonMotorSaveReq req, List<String> error) {

		NonMotorPolicyReq policyReq = req.getNonMotorPolicyReq();

		if (policyReq == null || req.getLocationList() == null) {
			return;
		}

		// Only ProductId = 57
		if (!"57".equals(policyReq.getProductId())) {
			return;
		}

		// Fetch GPA Expense values from DB
		BigDecimal maxDeathAmount = getGpaExpenseValue("01", policyReq.getProductId());
		BigDecimal maxPTDAmount = getGpaExpenseValue("02", policyReq.getProductId());
		BigDecimal maxMedicalAmount = getGpaExpenseValue("03", policyReq.getProductId());

		for (NonMotorLocationReq location : req.getLocationList()) {

			if (location.getSectionList() == null) {
				continue;
			}

			for (NonMotorSectionReq section : location.getSectionList()) {

				// Only SectionId = 182
				if (!"182".equals(section.getSectionId())) {
					continue;
				}

				String param2Str = section.getParam2();
				String sumInsuredStr = section.getSumInsured();

				if (StringUtils.isBlank(param2Str)) {
					continue;
				}

				try {

					BigDecimal param2 = new BigDecimal(param2Str.trim());

					// =========================================================
					// 1. CoverId = 123
					// Param2 <= SumInsured * 4
					// Maximum allowed comes from GPAEXPENSE ITEM_CODE = 01
					// =========================================================
					if (section.getCoverId() != null && section.getCoverId() == 123) {

						if (StringUtils.isBlank(sumInsuredStr)) {
							continue;
						}

						BigDecimal sumInsured = new BigDecimal(sumInsuredStr.trim());

						BigDecimal maxAllowed = sumInsured.multiply(new BigDecimal("4"));

						if (maxDeathAmount != null) {
							maxAllowed = maxAllowed.min(maxDeathAmount);
						}

						if (param2.compareTo(maxAllowed) > 0) {
							error.add("2500");
						}
					}

					// =========================================================
					// 2. CoverId = 124
					// Param2 <= SumInsured * 5
					// Maximum allowed comes from GPAEXPENSE ITEM_CODE = 02
					// =========================================================
					if (section.getCoverId() != null && section.getCoverId() == 124) {

						if (StringUtils.isBlank(sumInsuredStr)) {
							continue;
						}

						BigDecimal sumInsured = new BigDecimal(sumInsuredStr.trim());

						BigDecimal maxAllowed = sumInsured.multiply(new BigDecimal("5"));

						if (maxPTDAmount != null) {
							maxAllowed = maxAllowed.min(maxPTDAmount);
						}

						if (param2.compareTo(maxAllowed) > 0) {
							error.add("2501");
						}
					}

					// =========================================================
					// 3. CoverId = 50
					//
					// IndemnityType = 1 -> multiplier = 52
					// IndemnityType = 2 -> multiplier = 102
					// Formula:
					// (Param2 / 52 * 66.66%) * multiplier
					// =========================================================
					if (section.getCoverId() != null && section.getCoverId() == 50) {

						String indemnityTypeStr = String.valueOf(section.getIndemnityType());

						if (StringUtils.isBlank(indemnityTypeStr) || "null".equalsIgnoreCase(indemnityTypeStr)) {
							continue;
						}

						int indemnityType = Integer.parseInt(indemnityTypeStr.trim());

						BigDecimal multiplier;

						if (indemnityType == 1) {
							multiplier = new BigDecimal("52");
						} else if (indemnityType == 2) {
							multiplier = new BigDecimal("102");
						} else {
							error.add("2502");
							continue;
						}

						BigDecimal maxAllowed = param2.divide(new BigDecimal("52"), 10, RoundingMode.HALF_UP)
								.multiply(new BigDecimal("0.6666")).multiply(multiplier);

						if (param2.compareTo(maxAllowed) > 0) {
							error.add("2502");
						}
					}

					// =========================================================
					// 4. Medical Expense - CoverId = 126
					//
					// SumInsured <= GPAEXPENSE ITEM_CODE = 03
					// =========================================================
					if (section.getCoverId() != null && section.getCoverId() == 126) {

						if (StringUtils.isBlank(sumInsuredStr)) {
							continue;
						}

						BigDecimal sumInsured = new BigDecimal(sumInsuredStr.trim());

						if (maxMedicalAmount != null && sumInsured.compareTo(maxMedicalAmount) > 0) {
							error.add("2503");
						}
					}

				} catch (NumberFormatException e) {

					// Based on CoverId, add respective validation error
					if (section.getCoverId() != null && section.getCoverId() == 123) {
						error.add("2500");

					} else if (section.getCoverId() != null && section.getCoverId() == 124) {
						error.add("2501");

					} else if (section.getCoverId() != null && section.getCoverId() == 50) {
						error.add("2502");

					} else if (section.getCoverId() != null && section.getCoverId() == 126) {
						error.add("2503");
					}
				}
			}
		}
	}
	
	@Override
	public List<String> CompanyBasedValidation(NonMotorSaveReq req) {

		List<String> error = new ArrayList<String>();
		NonMotorPolicyReq policyReq = req.getNonMotorPolicyReq();
		try {
			
			System.out.println("non Motor Validation Company Wise Start...");
		    //  System.out.println("Request " + req);
			
			if (policyReq != null) {
				if (StringUtils.isBlank(policyReq.getCompanyId())) {
					// error.add(new Error("05", "CompanyId", "Please Enter CompanyId "));
					error.add("2293");
				} else if (policyReq.getCompanyId().length() > 20) {
					error.add("2294");
					// error.add(new Error("05", "CompanyId", "Please Enter CompanyId within 20
					// Characters"));
				}
				validateGPAParam2(req, error);

				List<String> companyErrors = new ArrayList<>();

				if (StringUtils.isNotBlank(policyReq.getCompanyId()) && "100002".equalsIgnoreCase(policyReq.getCompanyId())) {
				    companyErrors = tanzaniaEserviceNonMotorValidation.validateNonMotorDetails(req);
				}
				else if (StringUtils.isNotBlank(policyReq.getCompanyId()) && "100019".equalsIgnoreCase(policyReq.getCompanyId())) {
				    companyErrors = ugandaEserviceNonMotorValidation.validateNonMotorDetails(req);
				}
				else if (StringUtils.isNotBlank(policyReq.getCompanyId()) && "100020".equalsIgnoreCase(policyReq.getCompanyId())) {
				    companyErrors = kenyaEserviceNonMotorValidation.validateNonMotorDetails(req);
				}
				else if (StringUtils.isNotBlank(policyReq.getCompanyId()) && "100028".equalsIgnoreCase(policyReq.getCompanyId())) {
				    companyErrors = eagleNonmotorSaveValidation.validateNonMotorDetails(req);
				}
				else if (StringUtils.isNotBlank(policyReq.getCompanyId()) && "100046".equalsIgnoreCase(policyReq.getCompanyId())) {
				    companyErrors = PhoenixZambiaEserviceNonMotorDetails.validateNonMotorDetails(req);
				}
				else if (StringUtils.isNotBlank(policyReq.getCompanyId()) && "100047".equalsIgnoreCase(policyReq.getCompanyId())) {
				    companyErrors = PhoenixBotswanaEserviceNonMotorDetail.validateNonMotorDetails(req);
				}
				else if (StringUtils.isNotBlank(policyReq.getCompanyId()) && "100048".equalsIgnoreCase(policyReq.getCompanyId())) {
				    companyErrors = PhoenixMocambiqueDetails.validateNonMotorDetails(req);
				}
				else if (StringUtils.isNotBlank(policyReq.getCompanyId()) && "100050".equalsIgnoreCase(policyReq.getCompanyId())) {
				    companyErrors = PhoenixNamibiaDetails.validateNonMotorDetails(req);
				}
				else if (StringUtils.isNotBlank(policyReq.getCompanyId()) && "100049".equalsIgnoreCase(policyReq.getCompanyId())) {
				    companyErrors = phoenixSwazilanNonMotorValidation.validateNonMotorDetails(req);
				}
				else if (StringUtils.isNotBlank(policyReq.getCompanyId()) && "100053".equalsIgnoreCase(policyReq.getCompanyId())) {
				    companyErrors = salama.validateNonMotorDetails(req);
				}
				else if (StringUtils.isNotBlank(policyReq.getCompanyId()) && "100055".equalsIgnoreCase(policyReq.getCompanyId())) {
				    companyErrors = oman.validateNonMotorDetails(req);
				}
				else {
				    companyErrors.add("2293");
				}

				if (companyErrors != null) {
				    error.addAll(companyErrors);
				}
				

			System.out.println("Motor Validation Company Wise End.");

			}
		}catch (Exception e) {

			log.error(e);
			e.printStackTrace();
			error.add("1224");
			// error.add(new Error("19", "Common Error", e.getMessage()));
		}
		return error;

	}
	
	@Override
	public List<String> validatenonfirstLossPayee(List<FirstLossPayeeReq> reqList) {
		List<String> error = new ArrayList<String>();
		try {
			Integer row = 0;
			for (FirstLossPayeeReq req : reqList) {
				row += 1;
//			if (StringUtils.isBlank(req.getRiskId())) {
//				error.add("1198"+ ","+row);
//			}
//			if (StringUtils.isBlank(req.getBranchCode())) {
//				error.add("1199"+ ","+row);
//			} else if (req.getBranchCode().length() > 20) {
//				error.add("1200"+ ","+row);
//			}
				
				if (StringUtils.isBlank(req.getProductId())) {
					error.add("1201" + "," + row);
				} else if (req.getProductId().length() > 20) {
					error.add("1202" + "," + row);
				}
				
				if (StringUtils.isBlank(req.getSectionId())) {
					error.add("1203" + "," + row);
				}
				
				if (StringUtils.isBlank(req.getCompanyId())) {
					error.add("1204" + "," + row);
				} else if (req.getCompanyId().length() > 20) {
					error.add("1205" + "," + row);
				}
				
				if (StringUtils.isBlank(req.getRequestReferenceNo())) {
					error.add("2278" + "," + row);
				}
				if (StringUtils.isBlank(req.getFirstLossPayeeId())) {
					error.add("2279" + "," + row);
				}
				if (StringUtils.isBlank(req.getFirstLossPayeeDesc())) {
					error.add("2280" + "," + row);
				}
				if (StringUtils.isBlank(req.getLocationId())) {
					error.add("2281" + "," + row);
				}
				if (StringUtils.isBlank(req.getLocationName())) {
					error.add("2282" + "," + row);
				}
				if (StringUtils.isBlank(req.getCreatedBy())) {
					error.add("1184" + "," + row);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			error.add("1224");
			System.out.println(e.getMessage());
		}
		return error;
	}
	
	public Tuple getMinMaxRate(String Currency, String CompanyId) {
		Tuple currencyRes = null;
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
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class);
			List<Tuple> list = new ArrayList<Tuple>();
			
			// Find All
			Root<CurrencyMaster> c = query.from(CurrencyMaster.class);
			
			Subquery<Long> exchangeRate = query.subquery(Long.class);
			Root<ExchangeMaster> ex = exchangeRate.from(ExchangeMaster.class);
			// Exchange Effective Date Start Max Filter
			Subquery<Date> effectiveDate3 = query.subquery(Date.class);
			Root<ExchangeMaster> ocpm3 = effectiveDate3.from(ExchangeMaster.class);
			effectiveDate3.select(cb.greatest(ocpm3.get("effectiveDateStart").as(Date.class)));
			Predicate a1 = cb.equal(ex.get("exchangeId"), ocpm3.get("exchangeId"));
			Predicate a2 = cb.equal(ex.get("currencyId"), ocpm3.get("currencyId"));
			Predicate a15 = cb.equal(ex.get("companyId"), ocpm3.get("companyId"));
			Predicate a3 = cb.lessThanOrEqualTo(ocpm3.get("effectiveDateStart"), today);
			effectiveDate3.where(a1, a2, a3, a15);
			
			// Exhange Effective Date End Max Filter
			Subquery<Date> effectiveDate4 = query.subquery(Date.class);
			Root<ExchangeMaster> ocpm4 = effectiveDate4.from(ExchangeMaster.class);
			effectiveDate4.select(cb.greatest(ocpm4.get("effectiveDateEnd").as(Date.class)));
			Predicate a4 = cb.equal(ex.get("exchangeId"), ocpm4.get("exchangeId"));
			Predicate a5 = cb.equal(ex.get("currencyId"), ocpm4.get("currencyId"));
			Predicate a16 = cb.equal(ex.get("companyId"), ocpm4.get("companyId"));
			Predicate a6 = cb.greaterThanOrEqualTo(ocpm4.get("effectiveDateEnd"), todayEnd);
			effectiveDate4.where(a4, a5, a6, a16);
			
			// Exhange Rate Sub Query
			exchangeRate.select(ex.get("exchangeRate"));
			Predicate a7 = cb.equal(ex.get("currencyId"), Currency);
			Predicate a9 = cb.equal(ex.get("effectiveDateStart"), effectiveDate3);
			Predicate a10 = cb.equal(ex.get("effectiveDateEnd"), effectiveDate4);
			Predicate a17 = cb.equal(ex.get("companyId"), CompanyId);
			exchangeRate.where(a7, a9, a10, a17);
			
			// Select
			query.multiselect(c.get("currencyId").alias("currencyId"), c.get("minDiscount").alias("minDiscount"),
					c.get("maxLoading").alias("maxLoading"), exchangeRate.alias("exchangeRate"));
			
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
			Predicate n11 = cb.equal(c.get("status"), "R");
			Predicate n12 = cb.or(n1, n11);
			Predicate n2 = cb.equal(c.get("effectiveDateStart"), effectiveDate);
			Predicate n3 = cb.equal(c.get("effectiveDateEnd"), effectiveDate2);
			Predicate n4 = cb.equal(c.get("companyId"), CompanyId);
			// Predicate n5 = cb.equal(c.get("companyId"),"99999");
			// Predicate n6 = cb.or(n4,n5);
			query.where(n12, n2, n3, n4).orderBy(orderList);
			
			// Get Result
			TypedQuery<Tuple> result = em.createQuery(query);
			list = result.getResultList();
			if (list.size() > 0) {
				currencyRes = list.get(0);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			return null;
		}
		return currencyRes;
	}
	public List<String> Engineervalidation(List<EngineeringReq> req) {
		List<String> error = new ArrayList<String>();
		Integer row = 0;
		try {
			List<String> dependencyErrors = new ArrayList<>();
			for (EngineeringReq req1 : req) {
				
				/**
				 * if (req1.getLocationId()==null || req1.getLocationId()==0) { error.add("2281"
				 * + "," + row); } if (StringUtils.isBlank(req1.getLocationName())) {
				 * error.add("2282" + "," + row); } if (req1.getProductid()==null) {
				 * error.add("1201" + "," + row); } else if (req1.getProductid()==0) {
				 * error.add("1202" + "," + row); } if
				 * (StringUtils.isBlank(req1.getRequestReferenceNo())) { error.add("2278" + ","
				 * + row); } if(StringUtils.isBlank(req1.getPeriodOfActivity())) {
				 * error.add("2330" + "," + row); } if(req1.getStartDate()==null) {
				 * error.add("2331" + "," + row); }
				 **/
				if (req1.getEngineNumber() != null && engineerInfoRepo. existsByEngineNumberAndRequestReferenceNoNot(req1.getEngineNumber(),req1.getRequestReferenceNo())) {
					dependencyErrors.add("Duplicate Engine Number: " + req1.getEngineNumber() + " (Row " + row + ")");
				}
				
				if (req1.getSerialNumber() != null && engineerInfoRepo. existsBySerialNumberAndRequestReferenceNoNot(req1.getSerialNumber(),req1.getRequestReferenceNo())) {
					dependencyErrors.add("Duplicate Serial Number: " + req1.getSerialNumber() + " (Row " + row + ")");
				}
				
				row++;
			}
			
			if (!dependencyErrors.isEmpty()) {
				System.err.println("Duplicate record(s) found: " + String.join(", ", dependencyErrors));
				error.addAll(dependencyErrors);
			}
			
		} catch (Exception Ex) {
			System.out.println("The Exception Occured in Engineer vaildation  :" + Ex.getMessage());
			Ex.printStackTrace();
		}
		return error;
	}
	
	public Integer getBackDaysNew(String companyId, String productId, String loginId, String applicationId) {
		Integer backDays = 0;
		try {
			
			LoginProductMaster issure = loginProductMasterRepository.findFirstByCompanyIdAndLoginIdAndProductIdAndStatusOrderByAmendIdDesc(companyId,loginId, Integer.valueOf(productId),"Y");
			backDays = issure==null ? null : issure.getBackDays();
			if(issure!=null)
			{
				return backDays;
			}
			else if(!"1".equalsIgnoreCase(applicationId))
			{
				LoginProductMaster issure1 = loginProductMasterRepository.findFirstByCompanyIdAndLoginIdAndProductIdAndStatusOrderByAmendIdDesc(companyId,applicationId, Integer.valueOf(productId),"Y");
				backDays = issure1==null ? 0 : issure1.getBackDays();
				return backDays;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return 0;
		}
		return backDays;
	}
	
	
	
}