package com.maan.eway.common.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.jsoup.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.maan.eway.bean.BankMaster;
import com.maan.eway.bean.BranchMaster;
import com.maan.eway.bean.BrokerCommissionDetails;
import com.maan.eway.bean.BuildingRiskDetails;
import com.maan.eway.bean.CommonDataDetails;
import com.maan.eway.bean.CompanyProductMaster;
import com.maan.eway.bean.CurrencyMaster;
import com.maan.eway.bean.EserviceBuildingDetails;
import com.maan.eway.bean.EserviceCommonDetails;
import com.maan.eway.bean.EserviceCustomerDetails;
import com.maan.eway.bean.EserviceSectionDetails;
import com.maan.eway.bean.FirstLossPayee;
import com.maan.eway.bean.HomePositionMaster;
import com.maan.eway.bean.IndustryMaster;
import com.maan.eway.bean.InsuranceCompanyMaster;
import com.maan.eway.bean.ListItemValue;
import com.maan.eway.bean.LoginBranchMaster;
import com.maan.eway.bean.LoginMaster;
import com.maan.eway.bean.LoginUserInfo;
import com.maan.eway.bean.MsAssetDetails;
import com.maan.eway.bean.MsCommonDetails;
import com.maan.eway.bean.MsCustomerDetails;
import com.maan.eway.bean.MsHumanDetails;
import com.maan.eway.bean.OccupationMaster;
import com.maan.eway.bean.ProductSectionMaster;
import com.maan.eway.bean.RegionMaster;
import com.maan.eway.bean.StateMaster;
import com.maan.eway.common.req.CalcEngineReq;
import com.maan.eway.common.req.CommonRequest;
import com.maan.eway.common.req.EserviceCustomerSaveReq;
import com.maan.eway.common.req.EserviceMotorDetailsSaveReq;
import com.maan.eway.common.req.EserviceMotorDetailsSaveRes;
import com.maan.eway.common.req.FirstLossPayeeReq;
import com.maan.eway.common.req.NonMotEndtReq;
import com.maan.eway.common.req.NonMotorBrokerReq;
import com.maan.eway.common.req.NonMotorLocationReq;
import com.maan.eway.common.req.NonMotorPolicyReq;
import com.maan.eway.common.req.NonMotorSaveReq;
import com.maan.eway.common.req.NonMotorSectionReq;
import com.maan.eway.common.req.SequenceGenerateReq;
import com.maan.eway.common.req.WhatsappCustomerSaveReq;
import com.maan.eway.common.req.WhatsappMotorSaveReq;
import com.maan.eway.common.req.WhatsappPremiumCalcReq;
import com.maan.eway.common.res.BuildingSectionRes;
import com.maan.eway.common.res.CommonRes;
import com.maan.eway.common.res.FirstLossPayeeRes;
import com.maan.eway.common.res.NonMotEndtRes;
import com.maan.eway.common.res.NonMotorAssestRes;
import com.maan.eway.common.res.NonMotorBrokerRes;
import com.maan.eway.common.res.NonMotorComRes;
import com.maan.eway.common.res.NonMotorHumanRes;
import com.maan.eway.common.res.NonMotorLocRes;
import com.maan.eway.common.res.NonMotorLocationRes;
import com.maan.eway.common.res.NonMotorPolicyRes;
import com.maan.eway.common.res.NonMotorRes;
import com.maan.eway.common.res.NonMotorSaveRes;
import com.maan.eway.common.res.NonMotorSectionRes;
import com.maan.eway.common.res.PremiaTiraReq;
import com.maan.eway.common.res.PremiaTiraRes;
import com.maan.eway.common.res.SlideSectionSaveRes;
import com.maan.eway.common.res.SuccessRes;
import com.maan.eway.repository.BuildingRiskDetailsRepository;
import com.maan.eway.repository.CommonDataDetailsRepository;
import com.maan.eway.repository.CompanyProductMasterRepository;
import com.maan.eway.repository.EServiceBuildingDetailsRepository;
import com.maan.eway.repository.EServiceSectionDetailsRepository;
import com.maan.eway.repository.EserviceCommonDetailsRepository;
import com.maan.eway.repository.EserviceCustomerDetailsRepository;
import com.maan.eway.repository.FirstLossPayeeRepository;
import com.maan.eway.repository.HomePositionMasterRepository;
import com.maan.eway.repository.IndustryMasterRepository;
import com.maan.eway.repository.LoginBranchMasterRepository;
import com.maan.eway.repository.LoginMasterRepository;
import com.maan.eway.repository.LoginUserInfoRepository;
import com.maan.eway.repository.MsAssetDetailsRepository;
import com.maan.eway.repository.MsCommonDetailsRepository;
import com.maan.eway.repository.MsCustomerDetailsRepository;
import com.maan.eway.repository.MsHumanDetailsRepository;
import com.maan.eway.repository.RegionMasterRepository;
import com.maan.eway.repository.StateMasterRepository;
import com.maan.eway.req.FactorRateDetailsGetReq;
import com.maan.eway.req.OneTimeTableReq;
import com.maan.eway.res.OneTimeTableRes;
import com.maan.eway.service.OneTimeService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class EserviceSlideServicePhoenixMocambique implements EserviceSlideSaveServicePhoenix {

	@Autowired
	private FirstLossPayeeRepository firstLossRepo;

	@Autowired
	private IndustryMasterRepository industryrepo;

	@Autowired
	private StateMasterRepository staterepo;

	@Autowired
	private RegionMasterRepository regionrepo;

	@Autowired
	private EServiceSectionDetailsRepository secRepo;

	@Autowired
	private EServiceBuildingDetailsRepository buildingRepo;

	@Autowired
	private EserviceCommonDetailsRepository humanRepo;

	@Autowired
	private LoginMasterRepository loginRepo;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private OneTimeService otService;

	@Autowired
	private CompanyProductMasterRepository productRepo;

	@Autowired
	private EServiceSectionDetailsRepository eserSecRepo;

	@Autowired
	private LoginBranchMasterRepository lbranchRepo;

	@Autowired
	private EserviceCommonDetailsRepository eserCommonRepo;

	@Autowired
	private GenerateSeqNoServiceImpl genSeqNoService;

	@Autowired
	private CommonDataDetailsRepository commonRepo;

	@Autowired
	private BuildingRiskDetailsRepository motBuildingRepo;

	@Autowired
	private LoginUserInfoRepository loginUserRepo;

	@Autowired
	private EserviceCustomerDetailsRepository eserCustRepo;

	@Autowired
	private PremiaBrokerServiceImpl premiaBrokerService;

	@Autowired
	private HomePositionMasterRepository homeRepo;

	@Autowired
	private MsCustomerDetailsRepository msCustomerRepo;
	
	  @Autowired
	  private DozerBeanMapper dozerBeanMapper;

	@Autowired
	private MsAssetDetailsRepository msAssetRepo;
	@Autowired
	private MsHumanDetailsRepository mshumanRepo;
	@Autowired
	private MsCommonDetailsRepository msCommon;
	
	@Autowired
	private NonMotorServiceImpl nonmotor;

	@Value(value = "${calc.section}")
	private String calcSection;

	@Value(value = "${travel.productId}")
	private String travelProductId;

	private Logger log = LogManager.getLogger(EserviceSlideSaveServiceImpl.class);

	Gson json = new Gson();

	private OkHttpClient httpClient = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS)
			.connectTimeout(60, TimeUnit.SECONDS).build();
	
	private final ConcurrentHashMap<String, Object> nonMotorLocks = new ConcurrentHashMap<>();
	

		public List<InsuranceCompanyMaster> getInscompanyMasterDetails(String companyId) {
			List<InsuranceCompanyMaster> list = new ArrayList<InsuranceCompanyMaster>();

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
				CriteriaQuery<InsuranceCompanyMaster> query = cb.createQuery(InsuranceCompanyMaster.class);

				// Find All
				Root<InsuranceCompanyMaster> c = query.from(InsuranceCompanyMaster.class);

				// Select
				query.select(c);

				// Order By
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(c.get("effectiveDateStart")));

				// Effective Date Max Filter
				Subquery<Date> effectiveDate = query.subquery(Date.class);
				Root<InsuranceCompanyMaster> ocpm1 = effectiveDate.from(InsuranceCompanyMaster.class);
				effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
				Predicate a1 = cb.equal(c.get("companyId"), ocpm1.get("companyId"));
				Predicate a2 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
				effectiveDate.where(a1, a2);

				// Effective Date End
				Subquery<Date> effectiveDate2 = query.subquery(Date.class);
				Root<InsuranceCompanyMaster> ocpm2 = effectiveDate2.from(InsuranceCompanyMaster.class);
				effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
				Predicate a3 = cb.equal(c.get("companyId"), ocpm2.get("companyId"));
				Predicate a4 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
				effectiveDate2.where(a3, a4);

				// Where
				Predicate n1 = cb.equal(c.get("status"), "Y");
				Predicate n11 = cb.equal(c.get("status"), "R");
				Predicate n12 = cb.or(n1, n11);
				Predicate n2 = cb.equal(c.get("effectiveDateStart"), effectiveDate);
				Predicate n4 = cb.equal(c.get("effectiveDateEnd"), effectiveDate2);
				Predicate n13 = cb.equal(c.get("companyId"), companyId);
				query.where(n12, n2, n4, n13).orderBy(orderList);

				// Get Result
				TypedQuery<InsuranceCompanyMaster> result = em.createQuery(query);
				list = result.getResultList();

			} catch (Exception e) {
				e.printStackTrace();
				log.info("Exception is ---> " + e.getMessage());
				return null;
			}
			return list;
		}

		public BigDecimal exchangeRateScenario2(BigDecimal oldSi, BigDecimal oldExRate, BigDecimal newExRate,
				String oldCurr, String newCurr, DecimalFormat df) {
			BigDecimal suminsured = BigDecimal.ZERO;
			try {
				// Change Of Currency
				if (!oldCurr.equalsIgnoreCase(newCurr) && (oldSi != null)) {
					String calcType = newExRate.compareTo(BigDecimal.ONE) == 0 ? "multiply" : "divide";
					BigDecimal exchange = "multiply".equalsIgnoreCase(calcType) ? oldExRate : newExRate;
					// Suminsured
					suminsured = exchangeFcCalc(exchange, oldSi, calcType, df);

				} else {
					suminsured = oldSi;
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Log Details" + e.getMessage());
				return null;
			}
			return suminsured;
		}

		public BigDecimal exchangeFcCalc(BigDecimal exchangeRate, BigDecimal oldSuminsured, String calcType,
				DecimalFormat df) {
			BigDecimal sumInsured = null;
			try {
				if ("divide".equalsIgnoreCase(calcType)) {
					sumInsured = new BigDecimal(Math
							.round(Double.valueOf(oldSuminsured.divide(exchangeRate, RoundingMode.UP).toPlainString())));

				} else if ("multiply".equalsIgnoreCase(calcType)) {

					sumInsured = new BigDecimal(
							Math.round(Double.valueOf(oldSuminsured.multiply(exchangeRate).toPlainString())));
				}

			} catch (Exception e) {
				e.printStackTrace();
				log.info("Log Details" + e.getMessage());
				return null;
			}
			return sumInsured;
		}

		public Integer currencyDecimalFormat(String insuranceId, String currencyId) {
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
//						Predicate n5 = cb.equal(c.get("companyId"),"99999");
//						Predicate n6 = cb.or(n4,n5);
				Predicate n7 = cb.equal(c.get("currencyId"), currencyId);
				query.where(n1, n2, n3, n4, n7).orderBy(orderList);

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


		private IndustryMaster getIndustryName(String companyId, String productId, String branchCode, String industryId) {
			IndustryMaster industry = new IndustryMaster();
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
				CriteriaQuery<IndustryMaster> query = cb.createQuery(IndustryMaster.class);
				List<IndustryMaster> list = new ArrayList<IndustryMaster>();

				// Find All
				Root<IndustryMaster> c = query.from(IndustryMaster.class);

				// Select
				query.select(c);

				// Order By
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(c.get("categoryDesc")));

				// Effective Date Max Filter
				Subquery<Date> effectiveDate = query.subquery(Date.class);
				Root<IndustryMaster> ocpm1 = effectiveDate.from(IndustryMaster.class);
				effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
				Predicate a11 = cb.equal(c.get("industryId"), ocpm1.get("industryId"));
				Predicate a1 = cb.equal(c.get("categoryId"), ocpm1.get("categoryId"));
				Predicate a2 = cb.equal(c.get("companyId"), ocpm1.get("companyId"));
				Predicate a3 = cb.equal(c.get("productId"), ocpm1.get("productId"));
				Predicate a4 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
				Predicate a5 = cb.equal(c.get("branchCode"), ocpm1.get("branchCode"));

				effectiveDate.where(a1, a2, a3, a4, a5, a11);

				// Effective Date End
				Subquery<Date> effectiveDate2 = query.subquery(Date.class);
				Root<IndustryMaster> ocpm2 = effectiveDate2.from(IndustryMaster.class);
				effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
				Predicate a12 = cb.equal(c.get("industryId"), ocpm2.get("industryId"));
				Predicate a6 = cb.equal(c.get("categoryId"), ocpm2.get("categoryId"));
				Predicate a7 = cb.equal(c.get("companyId"), ocpm2.get("companyId"));
				Predicate a8 = cb.equal(c.get("productId"), ocpm2.get("productId"));
				Predicate a9 = cb.equal(c.get("branchCode"), ocpm2.get("branchCode"));
				Predicate a10 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
				effectiveDate2.where(a6, a7, a8, a9, a10, a12);

				// Where
				Predicate n1 = cb.equal(c.get("status"), "Y");
				Predicate n2 = cb.equal(c.get("effectiveDateStart"), effectiveDate);
				Predicate n3 = cb.equal(c.get("effectiveDateEnd"), effectiveDate2);
				Predicate n4 = cb.equal(c.get("companyId"), companyId);
				Predicate n5 = cb.equal(c.get("productId"), productId);
				Predicate n10 = cb.equal(c.get("industryId"), industryId);
				Predicate n7 = cb.equal(c.get("branchCode"), branchCode);
				Predicate n8 = cb.equal(c.get("branchCode"), "99999");
				Predicate n9 = cb.or(n7, n8);

				query.where(n1, n2, n3, n4, n5, n9, n10).orderBy(orderList);

				// Get Result
				TypedQuery<IndustryMaster> result = em.createQuery(query);
				list = result.getResultList();
				industry = list.size() > 0 ? list.get(0) : null;
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Exception is ---> " + e.getMessage());
				return null;
			}
			return industry;
		}

		public String getInscompanyMasterDropdown(String companyId) {
			String companyName = "";
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
				CriteriaQuery<InsuranceCompanyMaster> query = cb.createQuery(InsuranceCompanyMaster.class);
				List<InsuranceCompanyMaster> list = new ArrayList<InsuranceCompanyMaster>();

				// Find All
				Root<InsuranceCompanyMaster> c = query.from(InsuranceCompanyMaster.class);

				// Select
				query.select(c);

				// Order By
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(c.get("companyName")));

				// Effective Date Max Filter
				Subquery<Date> effectiveDate = query.subquery(Date.class);
				Root<InsuranceCompanyMaster> ocpm1 = effectiveDate.from(InsuranceCompanyMaster.class);
				effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
				Predicate a1 = cb.equal(c.get("companyId"), ocpm1.get("companyId"));
				Predicate a2 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
				effectiveDate.where(a1, a2);

				// Effective Date End
				Subquery<Date> effectiveDate2 = query.subquery(Date.class);
				Root<InsuranceCompanyMaster> ocpm2 = effectiveDate2.from(InsuranceCompanyMaster.class);
				effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
				Predicate a3 = cb.equal(c.get("companyId"), ocpm2.get("companyId"));
				Predicate a4 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
				effectiveDate2.where(a3, a4);

				// Where
				Predicate n1 = cb.equal(c.get("status"), "Y");
				Predicate n2 = cb.equal(c.get("effectiveDateStart"), effectiveDate);
				Predicate n3 = cb.equal(c.get("effectiveDateEnd"), effectiveDate2);
				Predicate n4 = cb.equal(c.get("companyId"), companyId);

				query.where(n1, n2, n3, n4).orderBy(orderList);

				// Get Result
				TypedQuery<InsuranceCompanyMaster> result = em.createQuery(query);
				list = result.getResultList();
				companyName = list.size() > 0 ? list.get(0).getCompanyName() : "";

			} catch (Exception e) {
				e.printStackTrace();
				log.info("Exception is ---> " + e.getMessage());
				return null;
			}
			return companyName;
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

		public List<ProductSectionMaster> getProductSectionDropdown(String companyId, String productId) {
			List<ProductSectionMaster> sectionList = new ArrayList<ProductSectionMaster>();
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
				CriteriaQuery<ProductSectionMaster> query = cb.createQuery(ProductSectionMaster.class);

				// Find All
				Root<ProductSectionMaster> c = query.from(ProductSectionMaster.class);

				// Select
				query.select(c);

				// Order By
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(c.get("sectionName")));

				// Effective Date Max Filter
				Subquery<Date> effectiveDate = query.subquery(Date.class);
				Root<ProductSectionMaster> ocpm1 = effectiveDate.from(ProductSectionMaster.class);
				effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
				Predicate a1 = cb.equal(c.get("sectionId"), ocpm1.get("sectionId"));
				Predicate a2 = cb.equal(c.get("companyId"), ocpm1.get("companyId"));
				Predicate a3 = cb.equal(c.get("productId"), ocpm1.get("productId"));
				Predicate a4 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
				effectiveDate.where(a1, a2, a3, a4);

				// Effective Date End
				Subquery<Date> effectiveDate2 = query.subquery(Date.class);
				Root<ProductSectionMaster> ocpm2 = effectiveDate2.from(ProductSectionMaster.class);
				effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
				Predicate a5 = cb.equal(c.get("sectionId"), ocpm2.get("sectionId"));
				Predicate a7 = cb.equal(c.get("companyId"), ocpm2.get("companyId"));
				Predicate a8 = cb.equal(c.get("productId"), ocpm2.get("productId"));

				Predicate a6 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
				effectiveDate2.where(a5, a6, a7, a8);

				// Where
				Predicate n1 = cb.equal(c.get("status"), "Y");
				Predicate n2 = cb.equal(c.get("effectiveDateStart"), effectiveDate);
				Predicate n3 = cb.equal(c.get("effectiveDateEnd"), effectiveDate2);
				Predicate n4 = cb.equal(c.get("companyId"), companyId);
				Predicate n5 = cb.equal(c.get("productId"), productId);
				// Predicate n6 = cb.equal(c.get("sectionId"), sectionId);
				// query.where(n1, n2, n3, n4, n5, n6).orderBy(orderList);
				query.where(n1, n2, n3, n4, n5).orderBy(orderList);

				// Get Result
				TypedQuery<ProductSectionMaster> result = em.createQuery(query);
				sectionList = result.getResultList();

			} catch (Exception e) {
				e.printStackTrace();
				log.info("Exception is ---> " + e.getMessage());
				return null;
			}
			return sectionList;
		}

		private List<BrokerCommissionDetails> getPolicyName(String companyId, String productId, String loginId,
				String agencyCode, String policyType, String userType) {
			// TODO Auto-generated method stub
			List<BrokerCommissionDetails> list = new ArrayList<BrokerCommissionDetails>();
			try {
				Date today = new Date();
				// Find Latest Record
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<BrokerCommissionDetails> query = cb.createQuery(BrokerCommissionDetails.class);

				// Find All
				Root<BrokerCommissionDetails> b = query.from(BrokerCommissionDetails.class);

				// Select
				query.select(b);

				// Effective Date Max Filter
				Subquery<Long> amendId = query.subquery(Long.class);
				Root<BrokerCommissionDetails> ocpm1 = amendId.from(BrokerCommissionDetails.class);
				amendId.select(cb.max(ocpm1.get("amendId")));
				Predicate a1 = cb.equal(ocpm1.get("id"), b.get("id"));
				Predicate a2 = cb.equal(ocpm1.get("companyId"), b.get("companyId"));
				Predicate a3 = cb.equal(ocpm1.get("productId"), b.get("productId"));
				Predicate a4 = cb.equal(ocpm1.get("policyType"), b.get("policyType"));
				Predicate a5 = cb.equal(ocpm1.get("loginId"), b.get("loginId"));
				if ("Broker".equalsIgnoreCase(userType)) {
					Predicate a6 = cb.equal(ocpm1.get("agencyCode"), b.get("agencyCode"));
					amendId.where(a1, a2, a3, a4, a5, a6);
				} else if ("User".equalsIgnoreCase(userType)) {
					amendId.where(a1, a2, a3, a4, a5);
				}

				Predicate n1 = cb.equal(b.get("amendId"), amendId);
				Predicate n2 = cb.equal(b.get("policyType"), policyType);
				Predicate n3 = cb.equal(b.get("companyId"), companyId);
				Predicate n4 = cb.equal(b.get("productId"), productId);
				Predicate n5 = cb.equal(b.get("loginId"), loginId);
				if ("Broker".equalsIgnoreCase(userType)) {
					Predicate n6 = cb.equal(b.get("agencyCode"), agencyCode);
					query.where(n1, n2, n3, n4, n5, n6);
				} else if ("User".equalsIgnoreCase(userType)) {
					query.where(n1, n2, n3, n4, n5);
				}
				// Get Result
				TypedQuery<BrokerCommissionDetails> result = em.createQuery(query);
				list = result.getResultList();

			} catch (Exception e) {
				e.printStackTrace();

			}
			return list;
		}


		public BranchMaster getBranchMasterRes(String companyId, String branchCode) {
			BranchMaster branchRes = new BranchMaster();
			try {
				Date today = new Date();
				Calendar cal = new GregorianCalendar();
				cal.setTime(today);
				cal.set(Calendar.HOUR_OF_DAY, 23);
				cal.set(Calendar.MINUTE, 1);
				today = cal.getTime();
				cal.setTime(today);
				cal.set(Calendar.HOUR_OF_DAY, 1);
				cal.set(Calendar.MINUTE, 1);
				Date todayEnd = cal.getTime();

				// Criteria
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<BranchMaster> query = cb.createQuery(BranchMaster.class);
				List<BranchMaster> list = new ArrayList<BranchMaster>();

				// Find All
				Root<BranchMaster> c = query.from(BranchMaster.class);

				// Select
				query.select(c);

				// Order By
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(c.get("branchName")));

				// Effective Date Max Filter
				Subquery<Date> effectiveDate = query.subquery(Date.class);
				Root<BranchMaster> ocpm1 = effectiveDate.from(BranchMaster.class);
				effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
				Predicate a1 = cb.equal(c.get("branchCode"), ocpm1.get("branchCode"));
				Predicate a2 = cb.equal(c.get("companyId"), ocpm1.get("companyId"));
				Predicate a3 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
				effectiveDate.where(a1, a2, a3);

				// Effective Date Max Filter
				Subquery<Date> effectiveDate2 = query.subquery(Date.class);
				Root<BranchMaster> ocpm2 = effectiveDate2.from(BranchMaster.class);
				effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
				Predicate a4 = cb.equal(c.get("branchCode"), ocpm2.get("branchCode"));
				Predicate a5 = cb.equal(c.get("companyId"), ocpm2.get("companyId"));
				Predicate a6 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
				effectiveDate2.where(a4, a5, a6);

				// Where
				Predicate n1 = cb.equal(c.get("status"), "Y");
				Predicate n2 = cb.equal(c.get("effectiveDateStart"), effectiveDate);
				Predicate n3 = cb.equal(c.get("effectiveDateEnd"), effectiveDate2);
				Predicate n4 = cb.equal(c.get("companyId"), companyId);
				Predicate n5 = cb.equal(c.get("branchCode"), branchCode);

				query.where(n1, n2, n3, n4, n5).orderBy(orderList);

				// Get Result
				TypedQuery<BranchMaster> result = em.createQuery(query);
				list = result.getResultList();
				branchRes =list.isEmpty()?null:list.get(0);

			} catch (Exception e) {
				e.printStackTrace();
				log.info("Exception is ---> " + e.getMessage());
				return null;
			}
			return branchRes;
		}

		public BranchMaster getCompanyBranch(String insuranceId, String branchCode) {
			BranchMaster branchData = new BranchMaster();
			DozerBeanMapper dozerMapper = new DozerBeanMapper();
			SimpleDateFormat idf = new SimpleDateFormat("yyMMddhhssmmss");
			try {
				Calendar cal = new GregorianCalendar();
				Date today = new Date();
				cal.setTime(new Date());
				cal.setTime(today);
				cal.set(Calendar.HOUR_OF_DAY, 23);
				cal.set(Calendar.MINUTE, 1);
				today = cal.getTime();
				cal.set(Calendar.HOUR_OF_DAY, 1);
				cal.set(Calendar.MINUTE, 1);
				Date todayEnd = cal.getTime();

				// Login Data
				// Criteria
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<BranchMaster> query = cb.createQuery(BranchMaster.class);
				List<BranchMaster> branchlist = new ArrayList<BranchMaster>();

				// Find All
				Root<BranchMaster> c = query.from(BranchMaster.class);

				// Select
				query.select(c);

				// Order By
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.desc(c.get("branchCode")));

				// Effective Date Max Filter
				Subquery<Date> effectiveDate = query.subquery(Date.class);
				Root<BranchMaster> ocpm1 = effectiveDate.from(BranchMaster.class);
				effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
				Predicate a1 = cb.equal(c.get("branchCode"), ocpm1.get("branchCode"));
				Predicate a2 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
				Predicate a3 = cb.equal(c.get("companyId"), ocpm1.get("companyId"));
				effectiveDate.where(a1, a2, a3);

				// Effective Date Max Filter
				Subquery<Date> effectiveDate2 = query.subquery(Date.class);
				Root<BranchMaster> ocpm2 = effectiveDate2.from(BranchMaster.class);
				effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
				Predicate a4 = cb.equal(c.get("branchCode"), ocpm2.get("branchCode"));
				Predicate a5 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
				Predicate a6 = cb.equal(c.get("companyId"), ocpm2.get("companyId"));
				effectiveDate2.where(a4, a5, a6);

				// Where
				Predicate n1 = cb.equal(c.get("status"), "Y");
				Predicate n2 = cb.equal(c.get("effectiveDateStart"), effectiveDate);
				Predicate n3 = cb.equal(c.get("effectiveDateEnd"), effectiveDate2);
				Predicate n5 = cb.equal(c.get("companyId"), insuranceId);
				Predicate n6 = cb.equal(c.get("branchCode"), branchCode);
				query.where(n1, n2, n3, n5, n6).orderBy(orderList);

				// Get Result
				TypedQuery<BranchMaster> result = em.createQuery(query);
				branchlist = result.getResultList();

				branchData = branchlist.get(0);

			} catch (Exception e) {
				e.printStackTrace();
				log.info("Exception is --->" + e.getMessage());
				return null;
			}
			return branchData;
		}

public synchronized String getListItem(String insuranceId, String branchCode, String itemType, String itemCode) {
			String itemDesc = "";
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
				Predicate b1 = cb.equal(c.get("branchCode"), ocpm1.get("branchCode"));
				Predicate b2 = cb.equal(c.get("companyId"), ocpm1.get("companyId"));

				Predicate a2 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
				effectiveDate.where(a1, a2, b1, b2);
				// Effective Date End Max Filter
				Subquery<Date> effectiveDate2 = query.subquery(Date.class);
				Root<ListItemValue> ocpm2 = effectiveDate2.from(ListItemValue.class);
				effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
				Predicate a3 = cb.equal(c.get("itemId"), ocpm2.get("itemId"));
				Predicate b3 = cb.equal(c.get("companyId"), ocpm2.get("companyId"));
				Predicate b4 = cb.equal(c.get("branchCode"), ocpm2.get("branchCode"));
				Predicate a4 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
				effectiveDate2.where(a3, a4, b3, b4);

				// Where
				Predicate n1 = cb.equal(c.get("status"), "Y");
				Predicate n12 = cb.equal(c.get("status"), "R");
				Predicate n13 = cb.or(n1, n12);
				Predicate n2 = cb.equal(c.get("effectiveDateStart"), effectiveDate);
				Predicate n3 = cb.equal(c.get("effectiveDateEnd"), effectiveDate2);
				Predicate n4 = cb.equal(c.get("companyId"), insuranceId);
				Predicate n5 = cb.equal(c.get("companyId"), "99999");
				Predicate n6 = cb.equal(c.get("branchCode"), branchCode);
				Predicate n7 = cb.equal(c.get("branchCode"), "99999");
				Predicate n8 = cb.or(n4, n5);
				Predicate n9 = cb.or(n6, n7);
				Predicate n10 = cb.equal(c.get("itemType"), itemType);
				Predicate n11 = cb.equal(c.get("itemCode"), itemCode);

				if (itemType.equalsIgnoreCase("PRODUCT_SHORT_CODE") || itemType.equalsIgnoreCase("PRODUCT_CATEGORY")) // not
																														// company
																														// based
					query.where(n13, n2, n3, n8, n9, n10, n11).orderBy(orderList);
				else
					query.where(n13, n2, n3, n4, n9, n10, n11).orderBy(orderList);

				// Get Result
				TypedQuery<ListItemValue> result = em.createQuery(query);
				list = result.getResultList();

				itemDesc = list.size() > 0 ? list.get(0).getItemValue() : "";
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Exception is ---> " + e.getMessage());
				return null;
			}
			return itemDesc;
		}

		public synchronized List<ListItemValue> getFirstLossDropDown(String insuranceId, String branchCode,
				String itemType) {
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
				Predicate a2 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
				effectiveDate.where(a1, a2);
				// Effective Date End Max Filter
				Subquery<Date> effectiveDate2 = query.subquery(Date.class);
				Root<ListItemValue> ocpm2 = effectiveDate2.from(ListItemValue.class);
				effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
				Predicate a3 = cb.equal(c.get("itemId"), ocpm2.get("itemId"));
				Predicate a4 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
				effectiveDate2.where(a3, a4);

				// Where
				Predicate n1 = cb.equal(c.get("status"), "Y");
				Predicate n12 = cb.equal(c.get("status"), "R");
				Predicate n13 = cb.or(n1, n12);
				Predicate n2 = cb.equal(c.get("effectiveDateStart"), effectiveDate);
				Predicate n3 = cb.equal(c.get("effectiveDateEnd"), effectiveDate2);
				Predicate n4 = cb.equal(c.get("companyId"), insuranceId);
				// Predicate n5 = cb.equal(c.get("companyId"), "99999");
				Predicate n6 = cb.equal(c.get("branchCode"), branchCode);
				Predicate n7 = cb.equal(c.get("branchCode"), "99999");
				// Predicate n8 = cb.or(n4, n5);
				Predicate n9 = cb.or(n6, n7);
				Predicate n10 = cb.equal(c.get("itemType"), itemType);
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

public OccupationMaster getOccupationMasterDropdown(String companyId, String branchCode, String productId,
				String occupationId) {
			OccupationMaster occupation = null;
			try {
				Date today = new Date();
				Calendar cal = new GregorianCalendar();
				cal.setTime(today);
				today = cal.getTime();
				Date todayEnd = cal.getTime();

				// Criteria
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<OccupationMaster> query = cb.createQuery(OccupationMaster.class);
				List<OccupationMaster> list = new ArrayList<OccupationMaster>();

				// Find All
				Root<OccupationMaster> c = query.from(OccupationMaster.class);
				// Select
				query.select(c);
				// Order By
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(c.get("branchCode")));

				// Effective Date Start Max Filter
				Subquery<Date> effectiveDate = query.subquery(Date.class);
				Root<OccupationMaster> ocpm1 = effectiveDate.from(OccupationMaster.class);
				effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
				Predicate a1 = cb.equal(c.get("occupationId"), ocpm1.get("occupationId"));
				Predicate a2 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
				Predicate a5 = cb.equal(c.get("companyId"), ocpm1.get("companyId"));
				Predicate a6 = cb.equal(c.get("branchCode"), ocpm1.get("branchCode"));
				Predicate a9 = cb.equal(c.get("productId"), ocpm1.get("productId"));

				effectiveDate.where(a1, a2, a5, a6, a9);
				// Effective Date End Max Filter
				Subquery<Date> effectiveDate2 = query.subquery(Date.class);
				Root<OccupationMaster> ocpm2 = effectiveDate2.from(OccupationMaster.class);
				effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
				Predicate a3 = cb.equal(c.get("occupationId"), ocpm2.get("occupationId"));
				Predicate a4 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
				Predicate a7 = cb.equal(c.get("companyId"), ocpm2.get("companyId"));
				Predicate a8 = cb.equal(c.get("branchCode"), ocpm2.get("branchCode"));
				Predicate a10 = cb.equal(c.get("productId"), ocpm2.get("productId"));
				effectiveDate2.where(a3, a4, a7, a8, a10);
				// Where
				Predicate n1 = cb.equal(c.get("status"), "Y");
				Predicate n2 = cb.equal(c.get("effectiveDateStart"), effectiveDate);
				Predicate n3 = cb.equal(c.get("effectiveDateEnd"), effectiveDate2);
				Predicate n4 = cb.equal(c.get("companyId"), companyId);
				// Predicate n10 = cb.equal(c.get("companyId"), "99999");
				// Predicate n11 = cb.or(n4, n10);
				Predicate n5 = cb.equal(c.get("branchCode"), branchCode);
				Predicate n6 = cb.equal(c.get("branchCode"), "99999");
				Predicate n7 = cb.or(n5, n6);
				Predicate n8 = cb.equal(c.get("occupationId"), occupationId);
				Predicate n9 = cb.or(cb.equal(c.get("productId"), productId), cb.equal(c.get("productId"), "99999"));

				query.where(n1, n2, n3, n7, n8, n4, n9).orderBy(orderList);
				TypedQuery<OccupationMaster> result = em.createQuery(query);
				list = result.getResultList();

				if (list.size() > 0) {
					list = list.stream().filter(distinctByKey(o -> Arrays.asList(o.getOccupationId())))
							.collect(Collectors.toList());
					list.sort(Comparator.comparing(OccupationMaster::getOccupationName));
					occupation = list.get(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Exception is --->" + e.getMessage());
				return null;
			}
			return occupation;
		}

		private static <T> java.util.function.Predicate<T> distinctByKey(
				java.util.function.Function<? super T, ?> keyExtractor) {
			Map<Object, Boolean> seen = new ConcurrentHashMap<>();
			return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
		}
public String generaterequestno(CommonRequest req) {
			String request_Reference_no = null;
			SequenceGenerateReq generateSeqReq = new SequenceGenerateReq();
			generateSeqReq.setInsuranceId(req.getInsuranceId());
			generateSeqReq.setProductId(req.getProductId());
			generateSeqReq.setType("2");
			generateSeqReq.setTypeDesc("REQUEST_REFERENCE_NO");
			request_Reference_no = genSeqNoService.generateSeqCall(generateSeqReq);
			return request_Reference_no;
		}

		
@Override
		public CommonRes saveRiskDetailsWithPremiumCalc(WhatsappPremiumCalcReq req, String token) {
			CommonRes coverRes = new CommonRes();
			try {

				// Save Customer
				EserviceCustomerSaveReq custSaveReq = frameCustSaveReq(req);
				CommonRes comRes = whatsappCustomerSave(custSaveReq, token);
				SuccessRes custRes = new SuccessRes();
				String custId = "";
				if (comRes.getCommonResponse() == null) {
					return comRes;
				} else {
					Map<String, String> map = (Map<String, String>) comRes.getCommonResponse();
					custId = map.get("SuccessId");
				}
				EserviceCustomerDetails findCust = eserCustRepo.findByCustomerReferenceNo(custId);

				// Save Motor
				EserviceMotorDetailsSaveReq motSaveReq = frameMotSaveReq(req, findCust);
				comRes = whatsappMotorSave(motSaveReq, token);

				Map<String, Object> map = new HashMap<>();
				if (comRes.getCommonResponse() == null) {
					return comRes;
				} else {
					EserviceMotorDetailsSaveRes motRes = new EserviceMotorDetailsSaveRes();
					map = (Map<String, Object>) comRes.getCommonResponse();
				}

				// Call Premium Calc

				CalcEngineReq calcReq = frameCalcEngineReq(custSaveReq, motSaveReq, map);

				comRes = whatsappCalcEngine(calcReq, token);

				Map<String, Object> map1 = new HashMap<>();

				map1 = (Map<String, Object>) comRes.getCommonResponse();

				coverRes = whatsappViewCalc(map1, token);

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return coverRes;
		}

		public EserviceCustomerSaveReq frameCustSaveReq(WhatsappPremiumCalcReq req) {
			EserviceCustomerSaveReq custSaveReq = new EserviceCustomerSaveReq();
			try {
				WhatsappCustomerSaveReq custReq = req.getCustomerSaveReq();
				WhatsappMotorSaveReq motReq = req.getMotorSaveReq();

				String createdBy = custReq.getWhatsappDesc() + custReq.getWhatsappNo();
				List<EserviceCustomerDetails> custList = eserCustRepo
						.findByCreatedByAndBrokerBranchCodeAndBranchCodeOrderByUpdatedDateDesc(createdBy,
								motReq.getBrokerBranchCode(), motReq.getBranchCode());
				String custRefNo = "";
				if (custList.size() > 0) {
					EserviceCustomerDetails custData = custList.get(0);
					custRefNo = custData.getCustomerReferenceNo();

				}

				custSaveReq.setBrokerBranchCode(motReq.getBranchCode());
				custSaveReq.setCustomerReferenceNo(custRefNo);
				custSaveReq.setCompanyId(motReq.getCompanyId());
				custSaveReq.setBranchCode(motReq.getBranchCode());
				custSaveReq.setProductId(motReq.getProductId());
				// custSaveReq.setAppointmentDate( null);
				// custSaveReq.setAddress1 null;
				// custSaveReq.setAddress2 null;
				custSaveReq.setBusinessType("1");
				// custSaveReq.setCityCode null;
				// custSaveReq.setCityName null;
				custSaveReq.setClientName(custReq.getClientName());
				custSaveReq.setClientStatus("Y");
				custSaveReq.setCreatedBy(createdBy);
				// custSaveReq.setDobOrRegDate ;
				// custSaveReq.setEmail2 null;
				// custSaveReq.setEmail3 null;
				// custSaveReq.setFax null;
				// custSaveReq.setGender null;
				custSaveReq.setIdNumber(custReq.getIdNumber());
				custSaveReq.setIdType(custReq.getIdType());
				custSaveReq.setIsTaxExempted("N");
				custSaveReq.setLanguage("1");
				custSaveReq.setMobileNo1(custReq.getWhatsappNo());
				// custSaveReq.setMobileNo2 null;
				// custSaveReq.setMobileNo3 null;
				// custSaveReq.setNationality null;
				// custSaveReq.setPlaceofbirth Chennai;
				custSaveReq.setPolicyHolderType("1");
				// custSaveReq.setPolicyHolderTypeid 1;
				custSaveReq.setPreferredNotification("Sms");
				custSaveReq.setRegionCode("01");
				custSaveReq.setMobileCode1("+255".equalsIgnoreCase(custReq.getWhatsappDesc()) ? "1" : "2");
				custSaveReq.setWhatsappCode("+255".equalsIgnoreCase(custReq.getWhatsappDesc()) ? "1" : "2");
				;
				// custSaveReq.setMobileCodeDesc1(custReq.getWhatsappDesc());
				// custSaveReq.setWhatsappDesc(custReq.getWhatsappDesc());
				custSaveReq.setWhatsappNo(custReq.getWhatsappNo());
				// custSaveReq.setStateCode null;
				// custSaveReq.setStateName null;
				custSaveReq.setStatus("Y");
				// custSaveReq.setStreet null;
				// custSaveReq.setTaxExemptedId null;
				// custSaveReq.setPinCode null;
				/// custSaveReq.setTelephoneNo2 null;
				// custSaveReq.setTelephoneNo3 null;
				custSaveReq.setTitle(custReq.getTitle());
				// custSaveReq.setVrTinNo();
				custSaveReq.setSaveOrSubmit("Submit");

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return custSaveReq;
		}

		public EserviceMotorDetailsSaveReq frameMotSaveReq(WhatsappPremiumCalcReq req, EserviceCustomerDetails custData) {
			EserviceMotorDetailsSaveReq motSaveReq = new EserviceMotorDetailsSaveReq();
			try {
				WhatsappCustomerSaveReq custReq = req.getCustomerSaveReq();
				WhatsappMotorSaveReq motReq = req.getMotorSaveReq();

				LoginMaster loginData = loginRepo.findByLoginId("guest");

				String createdBy = custReq.getWhatsappDesc() + custReq.getWhatsappNo();

				motSaveReq.setBrokerBranchCode(motReq.getBrokerBranchCode());
//				motSaveReq.setAcExecutiveId null;
//				motSaveReq.setCommissionType null;
//				motSaveReq.setCustomerCode 620499;
//				motSaveReq.setCustomerName null;
//				motSaveReq.setBdmCode();
				motSaveReq.setBrokerCode(loginData.getOaCode());
				motSaveReq.setLoginId(createdBy);
				motSaveReq.setSubUserType(loginData.getSubUserType());
				motSaveReq.setApplicationId("1");
				motSaveReq.setCustomerReferenceNo(custData.getCustomerReferenceNo());
				// motSaveReq.setRequestReferenceNo(null);
				motSaveReq.setIdNumber(custData.getIdNumber());
				motSaveReq.setVehicleId("1");
				// motSaveReq.setAcccessoriesSumInsured null;
				// motSaveReq.setAccessoriesInformation ;
				// motSaveReq.setAdditionalCircumstances ;
				// motSaveReq.setAxelDistance(01);
				motSaveReq.setChassisNumber(motReq.getChassisNumber());
				motSaveReq.setColor(motReq.getColor());
				motSaveReq.setColorDesc(motReq.getColorDesc());
				// motSaveReq.setCityLimit null;
				// motSaveReq.setCoverNoteNo null;
				// motSaveReq.setOwnerCategory() ;
				// motSaveReq.setCubicCapacity(100);
				motSaveReq.setCreatedBy(createdBy);
				// motSaveReq.setDrivenByDesc("Driver");
				motSaveReq.setEngineNumber(motReq.getEngineNumber());
				motSaveReq.setEngineCapacity(motReq.getEngineCapacity());
				motSaveReq.setFuelType(motReq.getFuelType());
				motSaveReq.setFuelTypeDesc(motReq.getFuelTypeDesc());
				motSaveReq.setGpsTrackingInstalled("N");
				// motSaveReq.setGrossweight("100");
				// motSaveReq.setHoldInsurancePolicy("N");
				//motSaveReq.setInsuranceType(motReq.getInsuranceType());
				motSaveReq.setSectionId(motSaveReq.getSectionId());
				motSaveReq.setCompanyId(motReq.getCompanyId());
				motSaveReq.setInsuranceClass(motReq.getInsuranceClass());
				// motSaveReq.setInsurerSettlement ;
				// motSaveReq.setInterestedCompanyDetails ;
				motSaveReq.setManufactureYear(
						StringUtils.isBlank(motReq.getManufactorYear()) ? 0L : Long.valueOf(motReq.getManufactorYear()));
				// motSaveReq.setModelNumber(motReq.get null;
				// motSaveReq.setMotorCategory("01");
				motSaveReq.setMotorUsage(motReq.getVehicleUsage());
				// motSaveReq.setMMotorusageDesc General Goods Carrying\r\n(Commercial);

				motSaveReq.setNcdYn("N");
				// motSaveReq.setNoOfClaims null;
				// motSaveReq.setNumberOfAxels("1");
				motSaveReq.setBranchCode(motReq.getBranchCode());
				motSaveReq.setAgencyCode(loginData.getOaCode());
				motSaveReq.setProductId(motReq.getProductId());
				// motSaveReq.setSectionId(motReq.getInsuranceType());
				// motSaveReq.setPolicyType("1");
				// motSaveReq.setRadioOrCasseteplayer null;
				// SimpleDateFormat sdf = new SimpleDateFormat(dd/MM/yyyy);
				// motSaveReq.setRegistrationYear(new Date());
				// motSaveReq.setRegisterNumber(StringUtils.isBlank(motReq.getRegisterNumber())
				// ? motReq.getChassisNumber() : motReq.getRegisterNumber());
				// motSaveReq.setRoofRack null;
				motSaveReq.setSeatingCapcity(motReq.getSeatingCapcity());
				motSaveReq.setSourceType(loginData.getSubUserType());
				// motSaveReq.setSpotFogLamp null;
				// motSaveReq.setStickerno null;
				motSaveReq.setSumInsured(motReq.getSumInsured());
				// motSaveReq.setTareweight("100");
//				motSaveReq.setTppdFreeLimit null;
//				motSaveReq.setTppdIncreaeLimit ;
//				motSaveReq.setTrailerDetails null;
				motSaveReq.setVehcileModel(motReq.getVehcileModel());
				motSaveReq.setVehicleModelDesc(motReq.getVehicleModelDesc());
				motSaveReq.setVehicleType(motReq.getVehicleType());
				motSaveReq.setVehicleTypeDesc(motReq.getVehicleTypeDesc());
				motSaveReq.setVehicleMake(motReq.getVehicleMake());
				motSaveReq.setVehicleMakeDesc(motReq.getVehicleMakeDesc());
//				motSaveReq.setWindScreenSumInsured ;
//				motSaveReq.setWindscreencoverrequired null;
//				motSaveReq.setaccident null;
//				motSaveReq.setperiodOfInsurance 30;
				motSaveReq.setPolicyStartDate(motReq.getPolicyStartDate());
				motSaveReq.setPolicyEndDate(motReq.getPolicyEndDate());
				motSaveReq.setCurrency(motReq.getCurrency());
				motSaveReq.setExchangeRate(motReq.getExchangeRate());
				motSaveReq.setHavepromocode("N");
				// motSaveReq.setPromoCode null;
				motSaveReq.setCollateralYn("N");
//				motSaveReq.setBorrowerType null;
//				motSaveReq.setCollateralName null;
//				motSaveReq.setFirstLossPayee null;
				motSaveReq.setFleetOwnerYn("N");
				motSaveReq.setNoOfVehicles("1");
//				motSaveReq.setNoOfComprehensives null;
//				motSaveReq.setClaimRatio null;
				motSaveReq.setSavedFrom("Owner");
				motSaveReq.setUserType("User");
				motSaveReq.setSearchFromApi(false);
//				motSaveReq.setTiraCoverNoteNo null;
//				motSaveReq.setEndorsementYn N;
//				motSaveReq.setEndorsementDate null;
//				motSaveReq.setEndorsementEffectiveDate null;
//				motSaveReq.setEndorsementRemarks null;
//				motSaveReq.setEndorsementType null;
//				motSaveReq.setEndorsementTypeDesc null;
//				motSaveReq.setEndtCategoryDesc null;
//				motSaveReq.setEndtCount null;
//				motSaveReq.setEndtPrevPolicyNo null;
//				motSaveReq.setEndtPrevQuoteNo null;
//				motSaveReq.setEndtStatus null;
//				motSaveReq.setIsFinanceEndt null;
//				motSaveReq.setOrginalPolicyNo null;
//				    Scenarios {
//				        ExchangeRateScenario {
//				            OldAcccessoriesSumInsured null;
//				            OldCurrency null;
//				            OldExchangeRate null;
//				            OldSumInsured null;
//				            OldTppdIncreaeLimit null;
//				            OldWindScreenSumInsured null
//				        }

				motSaveReq.setStatus("Y");

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return motSaveReq;
		}

		public CommonRes whatsappCustomerSave(EserviceCustomerSaveReq custReq, String token) {
			CommonRes custRes = new CommonRes();
			try {

				String url = "http://192.168.1.18:8086/api/customer";

				OkHttpClient h = new OkHttpClient.Builder().readTimeout(600, TimeUnit.SECONDS)
						.connectTimeout(60, TimeUnit.SECONDS).build();
				Response response = null;
				okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");

				Map<String, String> map = new HashMap();

				map.put("SaveOrSubmit", custReq.getSaveOrSubmit());
//			map.put("customerReferenceNo",custReq.getCustomerReferenceNo())	
				map.put("IdNumber", custReq.getIdNumber());
				map.put("BusinessType", custReq.getBusinessType());
				map.put("RegionCode", custReq.getRegionCode());
				map.put("IsTaxExempted", custReq.getIsTaxExempted());
				map.put("ClientName", custReq.getClientName());
				map.put("Title", custReq.getTitle());
				map.put("Clientstatus", custReq.getClientStatus());
				map.put("PolicyHolderType", custReq.getPolicyHolderType());
				map.put("PreferredNotification", custReq.getPreferredNotification());
				map.put("IdType", custReq.getIdType());
				map.put("MobileNo1", custReq.getMobileNo1());
				map.put("MobileCode1", custReq.getMobileCode1());
				map.put("WhatsappCode", custReq.getWhatsappCode());
				map.put("WhatsappNo", custReq.getWhatsappNo());
				map.put("Language", custReq.getLanguage());
				map.put("CreatedBy", custReq.getCreatedBy());
				map.put("Status", custReq.getStatus());
				map.put("InsuranceId", custReq.getCompanyId());
				map.put("BranchCode", custReq.getBranchCode());
				map.put("BrokerBranchCode", custReq.getBrokerBranchCode());
				map.put("ProductId", custReq.getProductId());
				map.put("PolicyHolderTypeid", "1");

				String req = json.toJson(map);
				RequestBody apiReqBody = RequestBody.create(req, mediaType);
				Request apiReq = new Request.Builder().addHeader("Authorization", "Bearer " + token).url(url)
						.post(apiReqBody).build();
				// System.out.println( new Date()+" Start "+ url);
				log.info("Request -----------> " + json.toJson(custReq));
				response = h.newCall(apiReq).execute();
				// System.out.println( new Date()+" End "+ url);
				String apiResponse = response.body().string();

				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				@SuppressWarnings("unchecked")
				Map<String, Object> resMap = mapper.readValue(apiResponse, Map.class);
				resMap.get("Result");
				custRes.setCommonResponse(resMap.get("Result"));

				try {

				} catch (Exception e) {
					e.printStackTrace();
				}

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return custRes;
		}

		public CommonRes whatsappMotorSave(EserviceMotorDetailsSaveReq motReq, String token) {
			CommonRes motRes = new CommonRes();
			try {

				String url = "http://192.168.1.18:8085/api/savemotordetails";
				// byte[] encodedAuth =
				// Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")) );
				String authHeader = "Bearer " + token;

				RestTemplate restTemplate = new RestTemplate();
				HttpHeaders headers = new HttpHeaders();
				headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
				headers.setContentType(MediaType.APPLICATION_JSON);
				headers.set("Authorization", authHeader);
				HttpEntity<Object> entityReq = new HttpEntity<Object>(motReq, headers);

				log.info("Api Url -----------> " + url);
				log.info("Request -----------> " + json.toJson(motReq));
				ResponseEntity<Object> response = restTemplate.postForEntity(url, entityReq, Object.class);
				log.info("Response -----------> " + json.toJson(response.getBody()));

				ObjectMapper mapper = new ObjectMapper();

				Map<String, Object> map = (Map<String, Object>) response.getBody();
				Map<String, Object> resMap = (Map<String, Object>) map.get("Result");

				motRes.setCommonResponse(resMap);
				// Map<String, Object> map=mapper.readValue(response, Map.class);

				// mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				// motRes = mapper.convertValue(response ,new TypeReference<CommonRes>(){});

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return motRes;
		}

		public CalcEngineReq frameCalcEngineReq(EserviceCustomerSaveReq custReq, EserviceMotorDetailsSaveReq motorReq,
				Map<String, Object> map) {
			List<String> sectionId = motorReq.getSectionId();
			String sect = (sectionId == null || sectionId.isEmpty()) ? "99999" : sectionId.get(0);
			CalcEngineReq calcReq = new CalcEngineReq();

			calcReq.setInsuranceId(custReq.getCompanyId());
			calcReq.setBranchCode(custReq.getBranchCode());
//			calcReq.setSectionId(motorReq.getInsuranceType());
			calcReq.setSectionId(sect);
			calcReq.setProductId(custReq.getProductId());
			calcReq.setMsrefno(map.get("MSRefNo").toString());
			calcReq.setCdRefNo(map.get("CdRefNo").toString());
			calcReq.setVdRefNo(map.get("VdRefNo").toString());
			calcReq.setVehicleId(map.get("VehicleId").toString());
			calcReq.setCreatedBy(map.get("CreatedBy").toString());
			calcReq.setRequestReferenceNo(map.get("RequestReferenceNo").toString());
			calcReq.setEffectiveDate(motorReq.getPolicyStartDate());
			calcReq.setPolicyEndDate(motorReq.getPolicyEndDate());
			calcReq.setCoverModification("N");

			return calcReq;
		}

		public CommonRes whatsappCalcEngine(CalcEngineReq req, String token) {
			CommonRes motRes = new CommonRes();
			try {

				String url = "http://192.168.1.18:8086/calculator/calc";

				// http://102.69.166.162:8080/EwayCommonApi/calculator/calc
				// byte[] encodedAuth =
				// Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")) );
				String authHeader = "Bearer " + token;

				RestTemplate restTemplate = new RestTemplate();
				HttpHeaders headers = new HttpHeaders();
				headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
				headers.setContentType(MediaType.APPLICATION_JSON);
				headers.set("Authorization", authHeader);
				HttpEntity<Object> entityReq = new HttpEntity<Object>(req, headers);

				log.info("Api Url -----------> " + url);
				log.info("Request -----------> " + json.toJson(req));
				ResponseEntity<Object> response = restTemplate.postForEntity(url, entityReq, Object.class);
				log.info("Response -----------> " + json.toJson(response.getBody()));

				ObjectMapper mapper = new ObjectMapper();

				Map<String, Object> map = (Map<String, Object>) response.getBody();
//				Map<String,Object> resMap=(Map<String,Object>)map.get("Result");

				motRes.setCommonResponse(map);
				// Map<String, Object> map=mapper.readValue(response, Map.class);

				// mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				// motRes = mapper.convertValue(response ,new TypeReference<CommonRes>(){});

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return motRes;
		}

		public CommonRes whatsappViewCalc(Map<String, Object> map, String token) {
			CommonRes motRes = new CommonRes();
			try {

				FactorRateDetailsGetReq req = new FactorRateDetailsGetReq();

				req.setRequestReferenceNo((String) map.get("RequestReferenceNo"));
				req.setProductId((String) map.get("ProductId"));

				String url = "http://192.168.1.18:8086/api/view/calc";

				// http://102.69.166.162:8080/EwayCommonApi/calculator/calc
				// byte[] encodedAuth =
				// Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")) );
				String authHeader = "Bearer " + token;

				RestTemplate restTemplate = new RestTemplate();
				HttpHeaders headers = new HttpHeaders();
				headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
				headers.setContentType(MediaType.APPLICATION_JSON);
				headers.set("Authorization", authHeader);
				HttpEntity<Object> entityReq = new HttpEntity<Object>(req, headers);

				log.info("Api Url -----------> " + url);
				log.info("Request -----------> " + json.toJson(req));
				ResponseEntity<Object> response = restTemplate.postForEntity(url, entityReq, Object.class);
				log.info("Response -----------> " + json.toJson(response.getBody()));

				ObjectMapper mapper = new ObjectMapper();

				Map<String, Object> map1 = (Map<String, Object>) response.getBody();
				// Map<String,Object> resMap=(Map<String,Object>)map.get("Result");

				motRes.setCommonResponse(map1);
				// Map<String, Object> map=mapper.readValue(response, Map.class);

				// mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				// motRes = mapper.convertValue(response ,new TypeReference<CommonRes>(){});

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return motRes;
		}
	
		public synchronized LoginMaster getPremiaBroker(String customerCode, String subUserType, String insuranceId) {
			LoginMaster lg = new LoginMaster();
			try {

				// Criteria
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<LoginMaster> query = cb.createQuery(LoginMaster.class);
				// Find All
				Root<LoginMaster> l = query.from(LoginMaster.class);
				Root<LoginUserInfo> lu = query.from(LoginUserInfo.class);

				// Select
				query.select(l);
				// Order By
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.desc(l.get("entryDate")));

				// Where
				// Predicate n1 = cb.equal(l.get("status"),"Y");
				Predicate n2 = cb.equal(l.get("companyId"), insuranceId);
				Predicate n3 = cb.equal(l.get("userType"), "Broker");
				Predicate n4 = cb.equal(l.get("userType"), "User");
				Predicate n5 = cb.or(n3, n4);
				Predicate n6 = cb.equal(lu.get("loginId"), customerCode);
				Predicate n7 = cb.equal(l.get("loginId"), lu.get("loginId"));
				Predicate n8 = cb.equal(cb.lower(l.get("subUserType")), subUserType.toLowerCase());
				Predicate n9 = cb.equal(lu.get("customerCode"), customerCode);
				query.where(n2, n5, n6, n7, n8).orderBy(orderList);

				// Get Result
				TypedQuery<LoginMaster> result = em.createQuery(query);
				List<LoginMaster> list = result.getResultList();
				if (list.isEmpty()) {
					query.where(n2, n5, n9, n7, n8).orderBy(orderList);
					TypedQuery<LoginMaster> result1 = em.createQuery(query);
					list = result1.getResultList();
				}

				lg = list.size() > 0 ? list.get(0) : null;
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Exception is ---> " + e.getMessage());
				return null;
			}
			return lg;
		}
	
		
		 public static List<NonMotorSectionReq> generaterisk( List<NonMotorSectionReq> sectionReq)
		 { 
			Map<String, AtomicInteger> riskCounters = new ConcurrentHashMap<>(); // To store counters per (sectionId + coverId)

			List<NonMotorSectionReq> updatedList = sectionReq.stream()
			    .filter(s -> s.getSectionId() != null && s.getCoverId() != null) // Ensure no null values for sectionId and coverId
			    .peek(req -> {
			        // Create a unique key based on sectionId and coverId
			        String key = req.getSectionId() + "-" + req.getCoverId();
			        
			        // Initialize counter for this combination if absent
			        riskCounters.putIfAbsent(key, new AtomicInteger(1));
			        
			        // Set and update the riskId
			        String RiskID = String.valueOf(riskCounters.get(key).getAndIncrement());
			        req.setRiskId(RiskID);
			    })
			    .collect(Collectors.toList());
			return updatedList;
		 }
		 
			
			

			
			public synchronized ListItemValue getListItemNonMotor(String insuranceId, String branchCode, String itemType,
					String itemCode) {
				String itemDesc = "";
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
					Predicate b3 = cb.equal(c.get("branchCode"), ocpm1.get("branchCode"));
					Predicate b4 = cb.equal(c.get("companyId"), ocpm1.get("companyId"));
					Predicate a2 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
					effectiveDate.where(a1, a2, b3, b4);
					// Effective Date End Max Filter
					Subquery<Date> effectiveDate2 = query.subquery(Date.class);
					Root<ListItemValue> ocpm2 = effectiveDate2.from(ListItemValue.class);
					effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
					Predicate a3 = cb.equal(c.get("itemId"), ocpm2.get("itemId"));
					Predicate b1 = cb.equal(c.get("branchCode"), ocpm2.get("branchCode"));
					Predicate b2 = cb.equal(c.get("companyId"), ocpm2.get("companyId"));
					Predicate a4 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
					effectiveDate2.where(a3, a4, b1, b2);

					// Where
					Predicate n1 = cb.equal(c.get("status"), "Y");
					Predicate n12 = cb.equal(c.get("status"), "R");
					Predicate n13 = cb.or(n1, n12);
					Predicate n2 = cb.equal(c.get("effectiveDateStart"), effectiveDate);
					Predicate n3 = cb.equal(c.get("effectiveDateEnd"), effectiveDate2);
					Predicate n4 = cb.equal(c.get("companyId"), insuranceId);
					Predicate n5 = cb.equal(c.get("companyId"), "99999");
					Predicate n6 = cb.equal(c.get("branchCode"), branchCode);
					Predicate n7 = cb.equal(c.get("branchCode"), "99999");
					Predicate n8 = cb.or(n4, n5);
					Predicate n9 = cb.or(n6, n7);
					Predicate n10 = cb.equal(c.get("itemType"), itemType);
					Predicate n11 = cb.equal(c.get("itemCode"), itemCode);

					if (itemType.equalsIgnoreCase("PRODUCT_SHORT_CODE") || itemType.equalsIgnoreCase("PRODUCT_CATEGORY")) // not
																															// company
																															// based
						query.where(n13, n2, n3, n8, n9, n10, n11).orderBy(orderList);
					else
						query.where(n13, n2, n3, n4, n9, n10, n11).orderBy(orderList);

					// Get Result
					TypedQuery<ListItemValue> result = em.createQuery(query);
					list = result.getResultList();

					// itemDesc = list.size() > 0 ? list.get(0).getItemValue() : "" ;

					if (list != null && list.size() > 0) {
						return list.get(0);
					} else {
						return new ListItemValue();
					}

					// return new ListItemValue();
				} catch (Exception e) {
					e.printStackTrace();
					log.info("Exception is ---> " + e.getMessage());
					return null;
				}
				// return itemDesc ;
			}

			
			public String createCustomerForShortQuote(NonMotorPolicyReq req) {
				try {
					List<EserviceCustomerDetails> customers = eserCustRepo.findByMobileCode1AndMobileNo1AndCreatedByAndCompanyIdAndProductIdOrderByUpdatedDateDesc(
									req.getMobileCode(), req.getMobileNo(), req.getCreatedBy(), req.getCompanyId(),
									Integer.parseInt(req.getProductId()));
					if (customers == null || customers.isEmpty()) {
						Date entryDate = new Date();
						String createdBy = req.getCreatedBy();
						// Random rand = new Random();
						// int random = rand.nextInt(90) + 10;
						Integer productId = Integer.valueOf(req.getProductId());
						// custRefNo = "Cust-" + generateCustRefNo() ; // idf.format(new Date()) +
						// random ;
						// Generate Seq
						SequenceGenerateReq generateSeqReq = new SequenceGenerateReq();
						generateSeqReq.setInsuranceId(req.getCompanyId());
						generateSeqReq.setProductId(req.getProductId());
						generateSeqReq.setType("1");
						generateSeqReq.setTypeDesc("CUSTOMER_REFERENCE_NO");
						String custRefNo = genSeqNoService.generateSeqCall(generateSeqReq);
						EserviceCustomerDetails saveData = new EserviceCustomerDetails();
						saveData.setProductId(productId);
						saveData.setEntryDate(entryDate);
						saveData.setCreatedBy(createdBy);
						saveData.setUpdatedDate(new Date());
						saveData.setUpdatedBy(req.getCreatedBy());
						saveData.setCustomerReferenceNo(custRefNo);
						saveData.setStatus("P");
						saveData.setLicenseIssuedDate(new Date());
						saveData.setLicenseDuration(20);

						saveData.setWhatsappCode(req.getMobileCode());
						saveData.setMobileCode1(req.getMobileCode());
						saveData.setMobileNo1(req.getMobileNo());
						saveData.setWhatsappNo(req.getMobileNo());
						saveData.setCompanyId(req.getCompanyId());
						saveData.setClientName(req.getCustomerName());
						saveData.setFirstName(req.getFirstName());
						saveData.setLastName(req.getLastName());
						saveData.setPolicyHolderTypeid("1");
						saveData.setPolicyHolderType("1");
						saveData.setIdType("1");
						saveData.setAge(20);
						saveData.setGender("M");
						saveData.setOccupation(StringUtils.isBlank(req.getOccupation())?"99999":req.getOccupation());
						saveData.setIsTaxExempted("N");
						saveData.setIdNumber("NA");
						saveData.setBranchCode(req.getBranchCode());
						saveData.setBrokerBranchCode(req.getBrokerBranchCode());
						saveData.setEmail1(req.getEmail1());
						ListItemValue policyHolderTypeIdL = getListItemNonMotor(req.getCompanyId(), req.getBranchCode(),
								"POLICY_HOLDER_ID_TYPE", saveData.getPolicyHolderTypeid());// listRepo.findByItemTypeAndItemCode("POLICY_HOLDER_ID_TYPE",
																							// req.getPolicyHolderTypeid());
						String policyHolderTypeId_en = policyHolderTypeIdL.getItemValue();
						String policyHolderTypeId_other = policyHolderTypeIdL.getItemValueLocal();
						saveData.setPolicyHolderTypeIdDesc(policyHolderTypeId_en);
						ListItemValue policyHolderTypeL = getListItemNonMotor("99999", req.getBranchCode(), "POLICY_HOLDER_TYPE",
								saveData.getPolicyHolderType());// listRepo.findByItemTypeAndItemCode("POLICY_HOLDER_TYPE",
																// req.getPolicyHolderType());
						String policyHolderType_en = policyHolderTypeL.getItemValue();
						String policyHolderType_other = policyHolderTypeL.getItemValueLocal();
						saveData.setPolicyHolderTypeDesc(policyHolderType_en);
						eserCustRepo.save(saveData);
						return custRefNo;

					} else {
						return customers.get(0).getCustomerReferenceNo();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;

			}
			
			public  CommonRes insetSection(NonMotorSaveReq req,String token){
			    long start = System.currentTimeMillis();  
			    CommonRes comres = new CommonRes();
			    String lockKey = req.getNonMotorPolicyReq().getCustomerReferenceNo();
				Object lock = nonMotorLocks.computeIfAbsent(lockKey, k -> new Object());
				synchronized (lock) {
				List<SlideSectionSaveRes> resList = new ArrayList<SlideSectionSaveRes>();
			    BuildingSectionRes sectionResList = new BuildingSectionRes();
				NonMotorPolicyReq policyReq = req.getNonMotorPolicyReq();
				NonMotorBrokerReq brokerReq = req.getNonMotorBrokerReq();
				int size = req.getLocationList().size();
				sQcustomerSave(policyReq);
				String requestReferenceNo= "";
				String customerId=null;
				try {
					CompanyProductMaster product = getCompanyProductMasterDropdown(policyReq.getCompanyId(),policyReq.getProductId());
					if (policyReq.getRequestReferenceNo() == null ||StringUtils.isBlank(policyReq.getRequestReferenceNo())) {
						requestReferenceNo = generateRequestReferenceno(policyReq.getProductId(),
								policyReq.getCompanyId());
					}
					else
					{
						requestReferenceNo = policyReq.getRequestReferenceNo();
						if (req.getNonMotEndtReq().getEndorsementType() != null) {
							if (("N".equalsIgnoreCase(product.getPackageYn()))) {
								req = nonmotor.endtNonpackageDelete(requestReferenceNo, req);
								customerId = req.getCustomerId();
							} else {
								List<NonMotorLocationReq> locationReq = req.getLocationList();
								for (NonMotorLocationReq locationdata : locationReq) {
									Integer locId = Integer.valueOf(locationdata.getLocationId());
									List<NonMotorSectionReq> sectionReq = locationdata.getSectionList();
									Set<Integer> sectionin = sectionReq.stream().map(NonMotorSectionReq::getSectionId)
											.filter(Objects::nonNull).map(Integer::valueOf).collect(Collectors.toSet());
									Set<String> distinctSectionIds = sectionReq.stream()
											.map(NonMotorSectionReq::getSectionId).collect(Collectors.toSet());
									req = nonmotor.endtPackageDelete(requestReferenceNo, req,locId,sectionin,distinctSectionIds);
								}
								customerId = req.getCustomerId();

							}
						} 
						else {
								if (("N".equalsIgnoreCase(product.getPackageYn()))) {
								List<NonMotorLocationReq> locationReq = req.getLocationList();
								customerId = nonmotor.nonpackageDelete(requestReferenceNo, product, locationReq);
							} else {
								List<NonMotorLocationReq> locationReq = req.getLocationList();
								for (NonMotorLocationReq locationdata : locationReq) {
									Integer locId = Integer.valueOf(locationdata.getLocationId());
									List<NonMotorSectionReq> sectionReq = locationdata.getSectionList();
									Set<Integer> sectionin = sectionReq.stream().map(NonMotorSectionReq::getSectionId)
											.filter(Objects::nonNull).map(Integer::valueOf).collect(Collectors.toSet());
									Set<String> distinctSectionIds = sectionReq.stream()
											.map(NonMotorSectionReq::getSectionId).collect(Collectors.toSet());

									customerId = nonmotor.packagedeleteSection(locId, requestReferenceNo, product,
											distinctSectionIds, sectionin);

								}
							}
						}
					}
					try
					{
						if (req.getNonMotEndtReq().getEndorsementType() == null) {
							insertHomePositionMaster(requestReferenceNo, policyReq, brokerReq, size, product);
						}

					}catch (Exception e) {
						e.printStackTrace();
						log.info("Exception Is ---> " + e.getMessage());
				   }
					
					List<ProductSectionMaster> sectionListfix = getProductSectionDropdown(policyReq.getCompanyId(),policyReq.getProductId());
					LoginMaster issuerData = loginRepo.findByLoginId(brokerReq.getApplicationId());
					BranchMaster branchData = getBranchMasterRes(policyReq.getCompanyId(), policyReq.getBranchCode());
					List<ListItemValue> sourcerTypes = premiaBrokerService.getSourceTypeDropdown(policyReq.getCompanyId(),policyReq.getBranchCode(), "SOURCE_TYPE");
					List<ListItemValue> filterSource = sourcerTypes.stream().filter(o -> StringUtils.isNotBlank(brokerReq.getSourceTypeId())
									&& (o.getItemCode().equalsIgnoreCase(brokerReq.getSourceTypeId())|| o.getItemValue().equalsIgnoreCase(brokerReq.getSourceTypeId())))
							.collect(Collectors.toList());
					List<String> directSource = new ArrayList<String>();
					directSource.add("1");
					directSource.add("2");
					directSource.add("3");
					directSource.add("4");
					LoginUserInfo loginUserData=new LoginUserInfo();
					LoginBranchMaster brokerBranch = new LoginBranchMaster();
					LoginUserInfo premiaUser = new LoginUserInfo(); 
					LoginBranchMaster premiaBranch= new LoginBranchMaster();
					LoginMaster premiaLogin= new LoginMaster();
					LoginMaster loginData = new LoginMaster();
					String commission="";
					List<BrokerCommissionDetails> commissionList= new ArrayList<BrokerCommissionDetails>();
					if (filterSource.size() > 0 && directSource.contains(filterSource.get(0).getItemCode())) {

						String sourceType = filterSource.get(0).getItemValue();
						String subUserType = sourceType.contains("Broker") ? "Broker": sourceType.contains("Agent") ? "Agent" : sourceType;
						premiaLogin = getPremiaBroker(brokerReq.getBdmCode(), subUserType, policyReq.getCompanyId());
						String brokerLoginId = premiaLogin != null ? premiaLogin.getLoginId(): branchData.getDirectBrokerId();
	//
//						 commission = getListItem(policyReq.getCompanyId(), policyReq.getBranchCode(),
//								"COMMISSION_PERCENT", filterSource.get(0).getItemValue());
						if (StringUtils.isNotBlank(brokerLoginId)) {
						 commissionList = getPolicyName(policyReq.getCompanyId(),
									policyReq.getProductId(), brokerLoginId, brokerReq.getBrokerCode(), "99999",
									brokerReq.getUserType());
						}
						premiaUser = loginUserRepo.findByLoginId(brokerLoginId);
						loginUserData = premiaUser != null ? premiaUser: loginUserRepo.findByLoginId(branchData.getDirectBrokerId());

						premiaBranch = lbranchRepo.findByLoginIdAndBranchCodeAndCompanyId(brokerLoginId,policyReq.getBranchCode(), policyReq.getCompanyId());
						brokerBranch = premiaBranch != null ? premiaBranch: lbranchRepo.findByLoginIdAndBranchCodeAndCompanyId(branchData.getDirectBrokerId(),policyReq.getBranchCode(), policyReq.getCompanyId());
					}
					else {
							 loginUserData = loginUserRepo.findByLoginId(brokerReq.getLoginId());
							 brokerBranch = lbranchRepo.findByLoginIdAndBrokerBranchCodeAndCompanyId(brokerReq.getLoginId(), brokerReq.getBrokerBranchCode(), policyReq.getCompanyId());
							 loginData = loginRepo.findByLoginId(brokerReq.getLoginId());
						//	 String loginId = StringUtils.isNotBlank(brokerReq.getSourceType())&& brokerReq.getSourceType().toLowerCase().contains("b2c") ? "guest" : brokerReq.getLoginId();
						//	 commissionList = getPolicyName(policyReq.getCompanyId(),policyReq.getProductId(), loginId, brokerReq.getBrokerCode(), "99999", brokerReq.getUserType());
					}

					String sourceTypeIdRaw = brokerReq.getSourceTypeId();
					String sourceTypeRaw   = brokerReq.getSourceType();   
					String sourceTypeId = sourceTypeIdRaw == null ? "" : sourceTypeIdRaw;
					String sourceType    = sourceTypeRaw == null ? "" : sourceTypeRaw.toLowerCase();

					String loginId = null;
					String userType = "Broker";
					String brokerCode = null;

					if ("2".equals(sourceTypeId) || "1".equals(sourceTypeId)) {

					    loginId = brokerReq.getBdmCode();
					    LoginMaster agencyCode = loginRepo.findByLoginId(loginId);
					    brokerCode = (agencyCode != null) ? agencyCode.getAgencyCode() : brokerReq.getBrokerCode();

					} else if ("broker".equalsIgnoreCase(sourceTypeId) && !sourceType.contains("b2c")) {

					    loginId = brokerReq.getLoginId();
					    brokerCode = brokerReq.getBrokerCode();
					} else {

					    loginId = brokerReq.getLoginId();
					    brokerCode = brokerReq.getBrokerCode();
					}
					commissionList = getPolicyName(policyReq.getCompanyId(),policyReq.getProductId(),loginId, brokerCode,"99999", userType);
					IndustryMaster industry = getIndustryName(policyReq.getCompanyId(), policyReq.getProductId(),policyReq.getBranchCode(), policyReq.getIndustryId());
					  Optional<MsCustomerDetails> customerOpt = Optional.empty();
					  EserviceCustomerDetails custData = req.getCustomerDetails();
						if(custData!=null)
						{
							 customerOpt = msCustomerRepo.findMatchingCustomerFast(
									custData.getPolicyHolderTypeid(), custData.getPolicyHolderType(), custData.getAge(),
									custData.getGender(), custData.getOccupation(), custData.getStatus(), custData.getIdNumber(),
									custData.getTaxExemptedId(),
									custData.getRegionCode() == null ? "NA" : custData.getRegionCode());
						}
					List<NonMotorLocationReq> locationReq = req.getLocationList();
					System.out.println("********************Location Loop starts*******************");
					for (NonMotorLocationReq locationdata : locationReq) {
						Integer locId = Integer.valueOf(locationdata.getLocationId());
						String endtLocationYn = locationdata.getEndtLocationYn();
						List<NonMotorSectionReq> sectionReq = locationdata.getSectionList();
						Set<String> distinctSectionIds = sectionReq.stream().map(NonMotorSectionReq::getSectionId).collect(Collectors.toSet());
						String companyName = getInscompanyMasterDropdown(policyReq.getCompanyId());
						System.out.println("Section Id ->" + distinctSectionIds);
						for (String group : distinctSectionIds) {
							String locationName = locationdata.getLocationName();
							String address = (StringUtils.isBlank(locationdata.getAddress()) ? null: locationdata.getAddress());
							String buildingOwnerYn = (StringUtils.isBlank(locationdata.getBuildingOwnerYn()) ? null: locationdata.getBuildingOwnerYn());
							List<NonMotorSectionReq> fiterData = sectionReq.stream().filter(o -> o.getSectionId().equalsIgnoreCase(group.toString())).collect(Collectors.toList());
							List<ProductSectionMaster> filterSection = sectionListfix.stream()
									.filter(o -> o.getSectionId().equals(Integer.valueOf(group)))
									.collect(Collectors.toList());
							ProductSectionMaster sec1 = filterSection.get(0);
							String productTypeDesc = getListItem(policyReq.getCompanyId(), policyReq.getBranchCode(), "PRODUCT_CATEGORY", sec1.getMotorYn());
							List<NonMotorSectionReq> fiterData1 = generaterisk(fiterData);
							int a=0;
							System.out.println("loop : " + a++);
							List<EserviceBuildingDetails> saveDatalist = new ArrayList<EserviceBuildingDetails>();
							List<EserviceCommonDetails> saveCommonList=new ArrayList<>();
							List<EserviceSectionDetails> sectionDetaillist = new ArrayList<EserviceSectionDetails>();
							List<MsHumanDetails> savemsHumanList=new ArrayList<>();
							List<MsAssetDetails> savemsAssertlist=new ArrayList<>();
							List<MsCommonDetails> msCommonList=new ArrayList<>();
							List<MsCustomerDetails> msCustomer=new ArrayList<>();
							// [0]=OneTimeTableReq [1]=isBuilding [2]=EserviceBuildingDetails [3]=EserviceCommonDetails
							List<Object[]> otContextList = new ArrayList<>();
							// --- Phase 1: sequential inserts, build OT request contexts ---
							for (NonMotorSectionReq data : fiterData1) {
								String riskId = StringUtils.isBlank(data.getRiskId()) ? "0" : data.getRiskId();
								System.out.println("Section : " + group);
								System.out.println("LocationId : " + locId);
								System.out.println("Location Name : " + locationName);
								System.out.println("SectionId : " + data.getSectionId());
								System.out.println("SectionName : " + data.getSectionName());
								System.out.println("RiskId : " + riskId);
								System.out.println("Cover Id :" + data.getCoverId());
								System.out.println("OccupationId : " + data.getOccupationId());
								System.out.println("********************Section Insert*******************");
								try {
									sectionResList = insertBuildingSectionNonMotor(req, data, riskId, locId,
										requestReferenceNo, locationName, data.getCoverId(), filterSection, productTypeDesc,endtLocationYn);
									sectionDetaillist.add(sectionResList.getSectiondetails());
								} catch (Exception e) {
									e.printStackTrace(); log.info("Exception Is ---> " + e.getMessage());
								}
								if (StringUtils.isNotBlank(sectionResList.getMotorYn())
									&& sectionResList.getMotorYn().equalsIgnoreCase("A")) {
									System.out.println("********************Eservice Building Save*******************");
									EserviceBuildingDetails saveData = null;
									try {
										saveData = insertAssetPolicyAndEndtDetailsAndBrokerDetails(req, requestReferenceNo, riskId, data, locId, locationName,
											sec1.getSectionName(), buildingOwnerYn, address, product, companyName, branchData, sourcerTypes, issuerData, industry, commissionList,
											loginUserData, brokerBranch, premiaUser, premiaBranch, premiaLogin, loginData, commission, customerId);
										saveDatalist.add(saveData);
									} catch (Exception e) {
										e.printStackTrace(); log.info("Exception Is ---> " + e.getMessage());
									}
									List<BuildingSectionRes> sectionList = new ArrayList<BuildingSectionRes>();
									BuildingSectionRes sec = new BuildingSectionRes();
									sec.setMotorYn(sectionResList.getMotorYn());
									sec.setSectionId(sectionResList.getSectionId());
									sec.setSectionName(sectionResList.getSectionName());
									sec.setCoverId(sectionResList.getCoverId());
									sec.setRiskId(sectionResList.getRiskId());
									sectionList.add(sec);
									OneTimeTableReq otReq = new OneTimeTableReq();
									if (saveData != null) {
										otReq.setRequestReferenceNo(saveData.getRequestReferenceNo());
										otReq.setVehicleId(saveData.getRiskId());
										otReq.setLocationId(saveData.getLocationId());
										otReq.setBranchCode(saveData.getBranchCode());
										otReq.setInsuranceId(saveData.getCompanyId());
										otReq.setProductId(Integer.valueOf(saveData.getProductId()));
									}
									otReq.setSectionList(sectionList);
									otReq.setMsAssertlist(new MsAssetDetails());
									otReq.setMsCommon(new MsCommonDetails());
									otReq.setEcustomer(req.getCustomerDetails());
									otReq.setPsm(sectionListfix);
									otReq.setY(true);
									otReq.setProduct(product);
									otReq.setOTCustome(customerOpt);
									otContextList.add(new Object[]{otReq, true, saveData, null});
								} else {
									System.out.println("********************Eservice Common Save*******************");
									EserviceCommonDetails saveCommon = new EserviceCommonDetails();
									try {
										saveCommon = insertCommonPolicyAndEndtAndBrokerDetails(req,
											requestReferenceNo, riskId, data, locId, locationName,
											sec1.getSectionName(), address, companyName,
											sourcerTypes, premiaLogin, loginUserData, brokerBranch, premiaUser, premiaBranch, loginData,
											commissionList, customerId);
										saveCommonList.add(saveCommon);
									} catch (Exception e) {
										e.printStackTrace(); log.info("Exception Is ---> " + e.getMessage());
									}
									List<BuildingSectionRes> sectionList = new ArrayList<BuildingSectionRes>();
									BuildingSectionRes sec = new BuildingSectionRes();
									sec.setMotorYn(sectionResList.getMotorYn());
									sec.setSectionId(sectionResList.getSectionId());
									sec.setSectionName(sectionResList.getSectionName());
									sec.setRiskId(saveCommon.getRiskId());
									sec.setCoverId(sectionResList.getCoverId());
									sectionList.add(sec);
									OneTimeTableReq otReq = new OneTimeTableReq();
									otReq.setRequestReferenceNo(saveCommon.getRequestReferenceNo());
									otReq.setVehicleId(saveCommon.getRiskId());
									otReq.setLocationId(saveCommon.getLocationId());
									otReq.setBranchCode(saveCommon.getBranchCode());
									otReq.setInsuranceId(saveCommon.getCompanyId());
									otReq.setProductId(Integer.valueOf(saveCommon.getProductId()));
									otReq.setSectionList(sectionList);
									otReq.setMsHuman(new MsHumanDetails());
									otReq.setMsCommon(new MsCommonDetails());
									otReq.setEcustomer(req.getCustomerDetails());
									otReq.setPsm(sectionListfix);
									otReq.setY(true);
									otReq.setProduct(product);
									otReq.setOTCustome(customerOpt);
									otContextList.add(new Object[]{otReq, false, null, saveCommon});
								}
							}

							// Populate complete lists now all inserts are done
							for (Object[] ctx : otContextList) {
								OneTimeTableReq otReq = (OneTimeTableReq) ctx[0];
								boolean isBuilding = (Boolean) ctx[1];
								if (isBuilding) { otReq.setEserviceBuilding(saveDatalist); }
								else { otReq.setEserviceCommon(saveCommonList); }
							}

							// --- Phase 2: run all OT inserts in parallel ---
							System.out.println("********************One Time table Save*******************");
							if (!otContextList.isEmpty()) {
								int poolSize = Math.min(otContextList.size(), 3);
								ExecutorService otExecutor = Executors.newFixedThreadPool(poolSize);
								@SuppressWarnings("unchecked")
								List<OneTimeTableRes>[] otResults = new List[otContextList.size()];
								List<CompletableFuture<Void>> futures = new ArrayList<>();
								for (int i = 0; i < otContextList.size(); i++) {
									final int idx = i;
									final OneTimeTableReq otReq = (OneTimeTableReq) otContextList.get(i)[0];
									futures.add(CompletableFuture.runAsync(() -> {
										try { otResults[idx] = otService.call_OT_Insert(otReq); }
										catch (Exception e) { log.info("OT error idx "+idx+": "+e.getMessage()); otResults[idx] = new ArrayList<>(); }
									}, otExecutor));
								}
								try { CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join(); }
								finally { otExecutor.shutdown(); }

								for (int i = 0; i < otContextList.size(); i++) {
									Object[] ctx = otContextList.get(i);
									OneTimeTableReq otReq = (OneTimeTableReq) ctx[0];
									boolean isBuilding = (Boolean) ctx[1];
									EserviceBuildingDetails buildData = (EserviceBuildingDetails) ctx[2];
									EserviceCommonDetails commonData = (EserviceCommonDetails) ctx[3];
									if (isBuilding) { savemsAssertlist.add(otReq.getMsAssertlist()); }
									else { savemsHumanList.add(otReq.getMsHuman()); }
									msCommonList.add(otReq.getMsCommon());
									if (otReq.getMsCustomer() != null) { msCustomer.add(otReq.getMsCustomer()); }
									List<OneTimeTableRes> otResList = otResults[i] != null ? otResults[i] : new ArrayList<>();
									for (OneTimeTableRes otRes : otResList) {
										SlideSectionSaveRes res = new SlideSectionSaveRes();
										res.setResponse("Saved Successfully");
										res.setRiskId(otRes.getVehicleId());
										res.setLocationId(otRes.getLocationId());
										res.setVdRefNo(otRes.getVdRefNo());
										res.setCdRefNo(otRes.getCdRefNo());
										res.setMsrefno(otRes.getMsRefNo());
										res.setCompanyId(otRes.getCompanyId());
										res.setProductId(otRes.getProductId());
										res.setSectionId(otRes.getSectionId());
										res.setCreatedBy(policyReq.getCreatedBy());
										if (isBuilding && buildData != null) {
											res.setCustomerReferenceNo(buildData.getCustomerReferenceNo());
											res.setRequestReferenceNo(buildData.getRequestReferenceNo());
											res.setCoverid(otRes.getCoverid());
										} else if (!isBuilding && commonData != null) {
											res.setCustomerReferenceNo(commonData.getCustomerReferenceNo());
											res.setRequestReferenceNo(commonData.getRequestReferenceNo());
										}
										resList.add(res);
									}
								}
							}
							if (savemsHumanList != null && !savemsHumanList.isEmpty()) {
							    mshumanRepo.saveAll(savemsHumanList);
							}
							
							if (msCommonList != null && !msCommonList.isEmpty()) {
								msCommon.saveAll(msCommonList);
							}
							
							if (sectionDetaillist != null && !sectionDetaillist.isEmpty()) {
								eserSecRepo.saveAll(sectionDetaillist);
							}
							
							if (saveCommonList != null && !saveCommonList.isEmpty()) {
							    eserCommonRepo.saveAll(saveCommonList);
							}

							if (saveDatalist != null && !saveDatalist.isEmpty()) {
							    buildingRepo.saveAll(saveDatalist);
							}

							if (savemsAssertlist != null && !savemsAssertlist.isEmpty()) {
							    msAssetRepo.saveAll(savemsAssertlist);
							}
							
							if (msCustomer != null && !msCustomer.isEmpty()) {
								msCustomerRepo.saveAll(msCustomer);
							}

							comres.setCommonResponse(resList);

							long end = System.currentTimeMillis(); // End time

							long timeTaken = end - start;  

							System.out.println("Nonmotor Time END");
							System.out.println("⏳ Time Taken for NonMotor: " + timeTaken + " ms");
							System.out.println("⏳ In seconds: " + (timeTaken / 1000.0) + " sec");
							System.out.println("⏳ In minutes: " + (timeTaken / 60000.0) + " min");
						}

					}

				} catch (Exception e) {
					e.printStackTrace();
					log.info("Exception Is ---> " + e.getMessage());
				}
				return comres;
			}
			}

			
			public  CommonRes insetSectionOwn(NonMotorSaveReq req,String token){
			    long start = System.currentTimeMillis();  
			    CommonRes comres = new CommonRes();
			    String lockKey = req.getNonMotorPolicyReq().getCustomerReferenceNo();
				Object lock = nonMotorLocks.computeIfAbsent(lockKey, k -> new Object());
				synchronized (lock) {
				List<SlideSectionSaveRes> resList = new ArrayList<SlideSectionSaveRes>();
			    BuildingSectionRes sectionResList = new BuildingSectionRes();
				NonMotorPolicyReq policyReq = req.getNonMotorPolicyReq();
				NonMotorBrokerReq brokerReq = req.getNonMotorBrokerReq();
				Integer locId = Integer.valueOf(req.getLocationList().get(0).getLocationId());
				int size = req.getLocationList().size();
				sQcustomerSave(policyReq);
				String requestReferenceNo= "";
				String customerId=null;
				boolean flag=true;
				try {
					CompanyProductMaster product = getCompanyProductMasterDropdown(policyReq.getCompanyId(),policyReq.getProductId());
					if (StringUtils.isBlank(policyReq.getRequestReferenceNo())) {
						requestReferenceNo = generateRequestReferenceno(policyReq.getProductId(), policyReq.getCompanyId());
						flag=false;
						
					}
					else
					{
						requestReferenceNo = policyReq.getRequestReferenceNo();
						customerId=	nonpackageDelete(locId, requestReferenceNo, product);				}
					try
					{
						if (req.getNonMotEndtReq().getEndorsementType() == null) {
							insertHomePositionMaster(requestReferenceNo, policyReq, brokerReq, size, product);
						}

					}catch (Exception e) {
						e.printStackTrace();
						log.info("Exception Is ---> " + e.getMessage());
				   }
					
					List<ProductSectionMaster> sectionListfix = getProductSectionDropdown(policyReq.getCompanyId(),policyReq.getProductId());
					LoginMaster issuerData = loginRepo.findByLoginId(brokerReq.getApplicationId());
					BranchMaster branchData = getBranchMasterRes(policyReq.getCompanyId(), policyReq.getBranchCode());
					List<ListItemValue> sourcerTypes = premiaBrokerService.getSourceTypeDropdown(policyReq.getCompanyId(),policyReq.getBranchCode(), "SOURCE_TYPE");
					List<ListItemValue> filterSource = sourcerTypes.stream().filter(o -> StringUtils.isNotBlank(brokerReq.getSourceTypeId())
									&& (o.getItemCode().equalsIgnoreCase(brokerReq.getSourceTypeId())|| o.getItemValue().equalsIgnoreCase(brokerReq.getSourceTypeId())))
							.collect(Collectors.toList());
					List<String> directSource = new ArrayList<String>();
					directSource.add("1");
					directSource.add("2");
					directSource.add("3");
					directSource.add("4");
					LoginUserInfo loginUserData=new LoginUserInfo();
					LoginBranchMaster brokerBranch = new LoginBranchMaster();
					LoginUserInfo premiaUser = new LoginUserInfo(); 
					LoginBranchMaster premiaBranch= new LoginBranchMaster();
					LoginMaster premiaLogin= new LoginMaster();
					LoginMaster loginData = new LoginMaster();
					String commission="";
					List<BrokerCommissionDetails> commissionList= new ArrayList<BrokerCommissionDetails>();
					if (filterSource.size() > 0 && directSource.contains(filterSource.get(0).getItemCode())) {

						String sourceType = filterSource.get(0).getItemValue();
						String subUserType = sourceType.contains("Broker") ? "Broker": sourceType.contains("Agent") ? "Agent" : sourceType;
						premiaLogin = getPremiaBroker(brokerReq.getBdmCode(), subUserType, policyReq.getCompanyId());
						String brokerLoginId = premiaLogin != null ? premiaLogin.getLoginId(): branchData.getDirectBrokerId();
	//
//						 commission = getListItem(policyReq.getCompanyId(), policyReq.getBranchCode(),
//								"COMMISSION_PERCENT", filterSource.get(0).getItemValue());
						if (StringUtils.isNotBlank(brokerLoginId)) {
						 commissionList = getPolicyName(policyReq.getCompanyId(),
									policyReq.getProductId(), brokerLoginId, brokerReq.getBrokerCode(), "99999",
									brokerReq.getUserType());
						}
						premiaUser = loginUserRepo.findByLoginId(brokerLoginId);
						loginUserData = premiaUser != null ? premiaUser: loginUserRepo.findByLoginId(branchData.getDirectBrokerId());

						premiaBranch = lbranchRepo.findByLoginIdAndBranchCodeAndCompanyId(brokerLoginId,policyReq.getBranchCode(), policyReq.getCompanyId());
						brokerBranch = premiaBranch != null ? premiaBranch: lbranchRepo.findByLoginIdAndBranchCodeAndCompanyId(branchData.getDirectBrokerId(),policyReq.getBranchCode(), policyReq.getCompanyId());
					}
					else {
							 loginUserData = loginUserRepo.findByLoginId(brokerReq.getLoginId());
							 brokerBranch = lbranchRepo.findByLoginIdAndBrokerBranchCodeAndCompanyId(brokerReq.getLoginId(), brokerReq.getBrokerBranchCode(), policyReq.getCompanyId());
							 loginData = loginRepo.findByLoginId(brokerReq.getLoginId());
						//	 String loginId = StringUtils.isNotBlank(brokerReq.getSourceType())&& brokerReq.getSourceType().toLowerCase().contains("b2c") ? "guest" : brokerReq.getLoginId();
						//	 commissionList = getPolicyName(policyReq.getCompanyId(),policyReq.getProductId(), loginId, brokerReq.getBrokerCode(), "99999", brokerReq.getUserType());
					}

					String sourceTypeIdRaw = brokerReq.getSourceTypeId();
					String sourceTypeRaw   = brokerReq.getSourceType();   
					String sourceTypeId = sourceTypeIdRaw == null ? "" : sourceTypeIdRaw;
					String sourceType    = sourceTypeRaw == null ? "" : sourceTypeRaw.toLowerCase();

					String loginId = null;
					String userType = "Broker";
					String brokerCode = null;

					if ("2".equals(sourceTypeId) || "1".equals(sourceTypeId)) {

					    loginId = brokerReq.getBdmCode();
					    LoginMaster agencyCode = loginRepo.findByLoginId(loginId);
					    brokerCode = (agencyCode != null) ? agencyCode.getAgencyCode() : brokerReq.getBrokerCode();

					} else if ("broker".equalsIgnoreCase(sourceTypeId) && !sourceType.contains("b2c")) {

					    loginId = brokerReq.getLoginId();
					    brokerCode = brokerReq.getBrokerCode();
					} else {

					    loginId = brokerReq.getLoginId();
					    brokerCode = brokerReq.getBrokerCode();
					}
					commissionList = getPolicyName(policyReq.getCompanyId(),policyReq.getProductId(),loginId, brokerCode,"99999", userType);
					IndustryMaster industry = getIndustryName(policyReq.getCompanyId(), policyReq.getProductId(),policyReq.getBranchCode(), policyReq.getIndustryId());
					
					List<NonMotorLocationReq> locationReq = req.getLocationList();
					System.out.println("********************Location Loop starts*******************");
					for (NonMotorLocationReq locationdata : locationReq) {
						List<NonMotorSectionReq> sectionReq = locationdata.getSectionList();
						Set<Integer> sectionin =sectionReq.stream().map(NonMotorSectionReq::getSectionId).filter(Objects::nonNull).map(Integer::valueOf).collect(Collectors.toSet());
						Set<String> distinctSectionIds = sectionReq.stream().map(NonMotorSectionReq::getSectionId).collect(Collectors.toSet());
						if(flag) {
							if (!("N".equalsIgnoreCase(product.getPackageYn())))
							{
							customerId=packagedeleteSection(locId, requestReferenceNo, product, distinctSectionIds, sectionin);
							}
							}
						String companyName = getInscompanyMasterDropdown(policyReq.getCompanyId());
						System.out.println("Section Id ->" + distinctSectionIds);
						for (String group : distinctSectionIds) {
							String locationName = locationdata.getLocationName();
							String address = (StringUtils.isBlank(locationdata.getAddress()) ? null: locationdata.getAddress());
							String buildingOwnerYn = (StringUtils.isBlank(locationdata.getBuildingOwnerYn()) ? null: locationdata.getBuildingOwnerYn());
							List<NonMotorSectionReq> fiterData = sectionReq.stream().filter(o -> o.getSectionId().equalsIgnoreCase(group.toString())).collect(Collectors.toList());
							List<ProductSectionMaster> filterSection = sectionListfix.stream()
									.filter(o -> o.getSectionId().equals(Integer.valueOf(group)))
									.collect(Collectors.toList());
							ProductSectionMaster sec1 = filterSection.get(0);
							String productTypeDesc = getListItem(policyReq.getCompanyId(), policyReq.getBranchCode(), "PRODUCT_CATEGORY", sec1.getMotorYn());
							List<NonMotorSectionReq> fiterData1 = generaterisk(fiterData);
							int a=0;
							System.out.println("loop : " + a++);
							List<EserviceBuildingDetails> saveDatalist = new ArrayList<EserviceBuildingDetails>();
							List<EserviceCommonDetails> saveCommonList=new ArrayList<>();
							MsAssetDetails msAssertlist=new MsAssetDetails();
							MsHumanDetails msHuman=new MsHumanDetails();
							MsCommonDetails msCommonl= new MsCommonDetails();
							List<EserviceSectionDetails> sectionDetaillist = new ArrayList<EserviceSectionDetails>();
							List<MsHumanDetails> savemsHumanList=new ArrayList<>();
							List<MsAssetDetails> savemsAssertlist=new ArrayList<>();
							List<MsCommonDetails> msCommonList=new ArrayList<>();
							List<MsCustomerDetails> msCustomer=new ArrayList<>();
							for (NonMotorSectionReq data : fiterData1) {
								String riskId = StringUtils.isBlank(data.getRiskId()) ? "0" : data.getRiskId();
								System.out.println("Section : " + group);
								System.out.println("LocationId : " + locId);
								System.out.println("Location Name : " + locationName);
								System.out.println("SectionId : " + data.getSectionId());
								System.out.println("SectionName : " + data.getSectionName());
								System.out.println("RiskId : " + riskId);
								System.out.println("Cover Id :" + data.getCoverId());
								System.out.println("OccupationId : " + data.getOccupationId());
								System.out.println("********************Section Insert*******************");
								try {
									sectionResList = insertBuildingSectionNonMotor(req, data, riskId, locId,
											requestReferenceNo, locationName, data.getCoverId(),filterSection,productTypeDesc
											,"");
									sectionDetaillist.add(sectionResList.getSectiondetails());
								} catch (Exception e) {
									e.printStackTrace();
									log.info("Exception Is ---> " + e.getMessage());

								}
								EserviceBuildingDetails saveData = null;
								
								if (StringUtils.isNotBlank(sectionResList.getMotorYn())
										&& sectionResList.getMotorYn().equalsIgnoreCase("A")) {
									System.out.println("********************Eservice Building Save*******************");
									try {
										saveData = insertAssetPolicyAndEndtDetailsAndBrokerDetails(req,requestReferenceNo, riskId, data, locId, locationName,
												sec1.getSectionName(),buildingOwnerYn, address,product,companyName,branchData,sourcerTypes,issuerData,industry,commissionList,
												loginUserData,brokerBranch,premiaUser,premiaBranch,premiaLogin,loginData,commission,customerId);
										
										saveDatalist.add(saveData);
									} catch (Exception e) {
										e.printStackTrace();
										log.info("Exception Is ---> " + e.getMessage());

									}
									// One Time table Save
									try {
										List<OneTimeTableRes> otResList = null;
										// Section Req
										List<BuildingSectionRes> sectionList = new ArrayList<BuildingSectionRes>();
										BuildingSectionRes sec = new BuildingSectionRes();
										sec.setMotorYn(sectionResList.getMotorYn());
										sec.setSectionId(sectionResList.getSectionId());
										sec.setSectionName(sectionResList.getSectionName());
										sec.setCoverId(sectionResList.getCoverId());
										sec.setRiskId(sectionResList.getRiskId());
										sectionList.add(sec);

										// One Time Table Thread Call
										OneTimeTableReq otReq = new OneTimeTableReq();
										otReq.setRequestReferenceNo(saveData.getRequestReferenceNo());
										otReq.setVehicleId(saveData.getRiskId());
										otReq.setLocationId(saveData.getLocationId());
										otReq.setBranchCode(saveData.getBranchCode());
										otReq.setInsuranceId(saveData.getCompanyId());
										otReq.setProductId(Integer.valueOf(saveData.getProductId()));
										otReq.setSectionList(sectionList);
										otReq.setEserviceBuilding(saveDatalist);	
										otReq.setMsAssertlist(msAssertlist);
										otReq.setMsCommon(msCommonl);
										otReq.setEcustomer(req.getCustomerDetails());
										otReq.setPsm(sectionListfix);	
										otReq.setY(true);
										otReq.setProduct(product);
										
										System.out.println("********************One Time table Save*******************");
										otResList = otService.call_OT_Insert(otReq);
										savemsAssertlist.add(otReq.getMsAssertlist());
										msCommonList.add(otReq.getMsCommon());	
										if(otReq.getMsCustomer()!=null)
										msCustomer.add(otReq.getMsCustomer());
										for (OneTimeTableRes otRes : otResList) {
											SlideSectionSaveRes res = new SlideSectionSaveRes();
											res.setResponse("Saved Successfully");
											res.setRiskId(otRes.getVehicleId());
											res.setLocationId(otRes.getLocationId());
											res.setVdRefNo(otRes.getVdRefNo());
											res.setCdRefNo(otRes.getCdRefNo());
											res.setMsrefno(otRes.getMsRefNo());
											res.setCompanyId(otRes.getCompanyId());
											res.setProductId(otRes.getProductId());
											res.setSectionId(otRes.getSectionId());
											res.setCustomerReferenceNo(saveData.getCustomerReferenceNo());
											res.setRequestReferenceNo(saveData.getRequestReferenceNo());
											res.setCreatedBy(policyReq.getCreatedBy());
											res.setCoverid(otRes.getCoverid());
											resList.add(res);
										}
									} catch (Exception e) {
										e.printStackTrace();
										log.info("Exception Is ---> " + e.getMessage());

									}

								} else {
									EserviceCommonDetails saveCommon = new EserviceCommonDetails();
								
									try {
										saveCommon = insertCommonPolicyAndEndtAndBrokerDetails(req,
												requestReferenceNo, riskId, data, locId, locationName,
												sec1.getSectionName(),address,companyName,
												sourcerTypes,premiaLogin,loginUserData,brokerBranch,premiaUser,premiaBranch,loginData
												, commissionList,customerId);
										saveCommonList.add(saveCommon);
										} catch (Exception e) {
										e.printStackTrace();
										log.info("Exception Is ---> " + e.getMessage());

									}
									// One Time table Save
									try {
										List<OneTimeTableRes> otResList = null;
										// Section Req
										List<BuildingSectionRes> sectionList = new ArrayList<BuildingSectionRes>();
										BuildingSectionRes sec = new BuildingSectionRes();
										sec.setMotorYn(sectionResList.getMotorYn());
										sec.setSectionId(sectionResList.getSectionId());
										sec.setSectionName(sectionResList.getSectionName());
										sec.setRiskId(saveCommon.getRiskId());
										sec.setCoverId(sectionResList.getCoverId());

										sectionList.add(sec);

										// One Time Table Thread Call
										OneTimeTableReq otReq = new OneTimeTableReq();
										otReq.setRequestReferenceNo(saveCommon.getRequestReferenceNo());
										otReq.setVehicleId(saveCommon.getRiskId());
										otReq.setLocationId(saveCommon.getLocationId());
										otReq.setBranchCode(saveCommon.getBranchCode());
										otReq.setInsuranceId(saveCommon.getCompanyId());
										otReq.setProductId(Integer.valueOf(saveCommon.getProductId()));
										otReq.setSectionList(sectionList);
										otReq.setEserviceCommon(saveCommonList);
										otReq.setMsHuman(msHuman);
										otReq.setMsCommon(msCommonl);
										otReq.setEcustomer(req.getCustomerDetails());
										otReq.setPsm(sectionListfix);	
										otReq.setY(true);
										otReq.setProduct(product);
										System.out.println("********************One Time table Save*******************");
										otResList = otService.call_OT_Insert(otReq);
										savemsHumanList.add(otReq.getMsHuman());
										msCommonList.add(otReq.getMsCommon());	
										if(otReq.getMsCustomer()!=null)
										msCustomer.add(otReq.getMsCustomer());
										for (OneTimeTableRes otRes : otResList) {
											SlideSectionSaveRes res = new SlideSectionSaveRes();
											res.setResponse("Saved Successfully");
											res.setRiskId(otRes.getVehicleId());
											res.setLocationId(otRes.getLocationId());
											res.setVdRefNo(otRes.getVdRefNo());
											res.setCdRefNo(otRes.getCdRefNo());
											res.setMsrefno(otRes.getMsRefNo());
											res.setCompanyId(otRes.getCompanyId());
											res.setProductId(otRes.getProductId());
											res.setSectionId(otRes.getSectionId());
											res.setCustomerReferenceNo(saveCommon.getCustomerReferenceNo());
											res.setRequestReferenceNo(saveCommon.getRequestReferenceNo());
											res.setCreatedBy(policyReq.getCreatedBy());
											resList.add(res);
										}
									} catch (Exception e) {
										e.printStackTrace();
										log.info("Exception Is ---> " + e.getMessage());

									}
								}

							}
							if (savemsHumanList != null && !savemsHumanList.isEmpty()) {
							    mshumanRepo.saveAll(savemsHumanList);
							}
							
							if (msCommonList != null && !msCommonList.isEmpty()) {
								msCommon.saveAll(msCommonList);
							}
							
							if (sectionDetaillist != null && !sectionDetaillist.isEmpty()) {
								eserSecRepo.saveAll(sectionDetaillist);
							}
							
							if (saveCommonList != null && !saveCommonList.isEmpty()) {
							    eserCommonRepo.saveAll(saveCommonList);
							}

							if (saveDatalist != null && !saveDatalist.isEmpty()) {
							    buildingRepo.saveAll(saveDatalist);
							}

							if (savemsAssertlist != null && !savemsAssertlist.isEmpty()) {
							    msAssetRepo.saveAll(savemsAssertlist);
							}
							
							if (msCustomer != null && !msCustomer.isEmpty()) {
								msCustomerRepo.saveAll(msCustomer);
							}

							comres.setCommonResponse(resList);

							long end = System.currentTimeMillis(); // End time

							long timeTaken = end - start;  

							System.out.println("Nonmotor Time END");
							System.out.println("⏳ Time Taken for NonMotor: " + timeTaken + " ms");
							System.out.println("⏳ In seconds: " + (timeTaken / 1000.0) + " sec");
							System.out.println("⏳ In minutes: " + (timeTaken / 60000.0) + " min");
						}

					}

				} catch (Exception e) {
					e.printStackTrace();
					log.info("Exception Is ---> " + e.getMessage());
				}
				return comres;
			}
			}
			
			private void sQcustomerSave(NonMotorPolicyReq policyReq) {
				try {
					if ("SQ".equals(policyReq.getSavedFrom())) {
						if (StringUtils.isBlank(policyReq.getCustomerReferenceNo())) {
							String customerRefNo = createCustomerForShortQuote(policyReq);
							policyReq.setCustomerReferenceNo(customerRefNo);

						}

					}
				} catch (Exception e) {
					e.printStackTrace();
					log.info("Customer insert ---> " + e.getMessage());
				}
			}

//					
			private String packagedeleteSection(Integer locId, String requestReferenceNo, CompanyProductMaster product,
					Set<String> distinctSectionIds, Set<Integer> sectionin) {
				List<EserviceCommonDetails> findHumans;
				List<EserviceBuildingDetails> findBuildings;
				List<EserviceSectionDetails> findsectionDetails;
				List<MsAssetDetails> ast;
				List<MsHumanDetails> mshuman;
				List<MsCommonDetails> com;
				String customerId = null;
				if (!("N".equalsIgnoreCase(product.getPackageYn()))) {
					try {
						findHumans = humanRepo.findByRequestReferenceNoAndSectionIdInAndLocationId(requestReferenceNo,
								distinctSectionIds, locId);
						findBuildings = buildingRepo.findByRequestReferenceNoAndSectionIdInAndLocationId(requestReferenceNo,
								distinctSectionIds, locId);
						findsectionDetails = secRepo.findByRequestReferenceNoAndSectionIdInAndLocationId(requestReferenceNo,
								distinctSectionIds, locId);
						ast = msAssetRepo.findByRequestReferenceNoAndLocationIdAndSectionIdIn(requestReferenceNo, locId,
								sectionin);
						mshuman = mshumanRepo.findByRequestReferenceNoAndLocationIdAndSectionidIn(requestReferenceNo, locId,
								sectionin);
						com = msCommon.findByRequestreferencenoAndLocationIdAndSectionIdIn(requestReferenceNo, locId,
								sectionin);
						// Delete Old Records
						if (findHumans != null && findHumans.size() > 0) {
							customerId = findHumans.get(0).getCustomerId();
							humanRepo.deleteAll(findHumans);
						}
						if (findBuildings != null && findBuildings.size() > 0) {
							customerId = findBuildings.get(0).getCustomerId();
							buildingRepo.deleteAll(findBuildings);
						}
						if (!findsectionDetails.isEmpty()) {
							secRepo.deleteAll(findsectionDetails);
						}
						if (ast != null && ast.size() > 0) {
							msAssetRepo.deleteAll(ast);
						}
						if (mshuman != null && mshuman.size() > 0) {
							mshumanRepo.deleteAll(mshuman);
						}
						if (com != null && com.size() > 0) {
							msCommon.deleteAll(com);

						}
						System.out.println("Deleted Successfully");
					} catch (Exception e) {
						e.printStackTrace();
						log.info("Exception Is ---> " + e.getMessage());

					}
				}
				return customerId;
			}

			private String nonpackageDelete(Integer locId, String requestReferenceNo, CompanyProductMaster product) {
				List<EserviceCommonDetails> findHumans;
				List<EserviceBuildingDetails> findBuildings;
				List<EserviceSectionDetails> findsectionDetails;
				List<MsAssetDetails> ast;
				List<MsHumanDetails> mshuman;
				List<MsCommonDetails> com;
				String customerId = null;
				if ("N".equalsIgnoreCase(product.getPackageYn())) {
					try {

						findHumans = humanRepo.findByRequestReferenceNoAndLocationId(requestReferenceNo, locId);
						findBuildings = buildingRepo.findByRequestReferenceNoAndLocationId(requestReferenceNo, locId);
						findsectionDetails = secRepo.findByRequestReferenceNoAndLocationId(requestReferenceNo, locId);
						ast = msAssetRepo.findByRequestReferenceNoAndLocationId(requestReferenceNo, locId);
						mshuman = mshumanRepo.findByRequestReferenceNoAndLocationId(requestReferenceNo, locId);
						com = msCommon.findByRequestreferencenoAndLocationId(requestReferenceNo, locId);
						// Delete Old Records
						if (findHumans != null && findHumans.size() > 0) {
							customerId = findHumans.get(0).getCustomerId();
							humanRepo.deleteAll(findHumans);
						}
						if (ast != null && ast.size() > 0) {
							msAssetRepo.deleteAll(ast);
						}
						if (mshuman != null && mshuman.size() > 0) {
							mshumanRepo.deleteAll(mshuman);
						}
						if (com != null && com.size() > 0) {
							msCommon.deleteAll(com);

						}
						if (findBuildings != null && findBuildings.size() > 0) {
							customerId = findBuildings.get(0).getCustomerId();
							buildingRepo.deleteAll(findBuildings);
						}
						if (!findsectionDetails.isEmpty()) {
							secRepo.deleteAll(findsectionDetails);
						}
						System.out.println("Full section is Deleted Successfully");
					} catch (Exception e) {
						e.printStackTrace();
						log.info("Exception Is ---> " + e.getMessage());

					}
				}
				return customerId;
			}



			private HomePositionMaster insertHomePositionMaster(String requestReferenceNo, NonMotorPolicyReq policyReq,NonMotorBrokerReq brokerReq,int size,CompanyProductMaster product)
			{
			HomePositionMaster home = null;
			long a=99999;
			boolean isSameDate=false;
			try {
				home = homeRepo.findByRequestReferenceNo(requestReferenceNo);
				if (home != null) {
					if (StringUtils.isBlank(home.getQuoteNo())) {
						Date existingStart = home.getInceptionDate();
						Date requestStart = policyReq.getPolicyStartDate();

						isSameDate = existingStart != null && requestStart != null
								&& existingStart.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
										.equals(requestStart.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
					} else {
						isSameDate = true;
					}

				}
				if (!isSameDate) {
					home = new HomePositionMaster();
					home.setNoOfVehicles(size);
					home.setSectionId(0);
					home.setAmendId(0L);
					home.setApplicationNo(a);			
					home.setApplicationId(StringUtils.isBlank(brokerReq.getApplicationId()) ? "0" : brokerReq.getApplicationId());
					home.setBdmCode(StringUtils.isBlank(brokerReq.getBdmCode()) ? "0" : brokerReq.getBdmCode());
					home.setLoginId(StringUtils.isBlank(policyReq.getCreatedBy()) ? null : policyReq.getCreatedBy());
					home.setBdmName(StringUtils.isBlank(brokerReq.getBdmName()) ? null : brokerReq.getBdmName());
					home.setCustomerId("99999");
					home.setRequestReferenceNo(requestReferenceNo);
					home.setCompanyId(StringUtils.isBlank(policyReq.getCompanyId()) ? null : policyReq.getCompanyId());
					home.setProductId(StringUtils.isBlank(policyReq.getProductId()) ? null: Integer.valueOf(policyReq.getProductId()));
					home.setBrokerCode(StringUtils.isBlank(brokerReq.getBrokerCode()) ? null : brokerReq.getBrokerCode());
					home.setAgencyCode(StringUtils.isBlank(brokerReq.getAgencyCode()) ? null: Integer.valueOf(brokerReq.getAgencyCode()));
					home.setEntryDate(policyReq.getPolicyStartDate() == null ? null : policyReq.getPolicyStartDate());
					home.setInceptionDate(policyReq.getPolicyStartDate() == null ? null : policyReq.getPolicyStartDate());
					home.setEffectiveDate(policyReq.getPolicyStartDate() == null ? null : policyReq.getPolicyStartDate());
					home.setExpiryDate(policyReq.getPolicyEndDate() == null ? null : policyReq.getPolicyEndDate());	
					home.setBranchCode(StringUtils.isBlank(policyReq.getBranchCode()) ? null : policyReq.getBranchCode());
					home.setExchangeRate(StringUtils.isBlank(policyReq.getExchangeRate()) ? null: new BigDecimal(policyReq.getExchangeRate()));
					home.setCustomerCode(StringUtils.isBlank(policyReq.getCustomerReferenceNo()) ? "99999" : policyReq.getCustomerReferenceNo());
					home.setProductName(product.getProductName());
					home.setBrokerBranchCode(StringUtils.isBlank(brokerReq.getBrokerBranchCode()) ? null: brokerReq.getBrokerBranchCode());
					home.setStatus("Y");	
					home.setSourceTypeId(StringUtils.isBlank(brokerReq.getSourceTypeId()) ? null : brokerReq.getSourceTypeId());
					home.setSourceType(StringUtils.isBlank(brokerReq.getSourceType()) ? null : brokerReq.getSourceType());
					home.setCompanyName(getInscompanyMasterDropdown(StringUtils.isBlank(policyReq.getCompanyId()) ? null : policyReq.getCompanyId()));
					homeRepo.save(home);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Exception Is HomePositionMaster ---> " + e.getMessage());
				return null;
			}
			return home;

			}

		public String generateRequestReferenceno(String productId, String companyId) {
			String request_Reference_no = null;
			SequenceGenerateReq generateSeqReq = new SequenceGenerateReq();
			generateSeqReq.setInsuranceId(companyId);
			generateSeqReq.setProductId(productId);
			generateSeqReq.setType("2");
			generateSeqReq.setTypeDesc("REQUEST_REFERENCE_NO");
			request_Reference_no = genSeqNoService.generateSeqCall(generateSeqReq);
			return request_Reference_no;
		}

		public EserviceBuildingDetails insertAssetPolicyAndEndtDetailsAndBrokerDetails(NonMotorSaveReq req,
				String requestReferenceNo, String riskId, NonMotorSectionReq data, Integer locId, String locationName,
				String sectionName,String buildingOwnerYn,String address,CompanyProductMaster product,String companyName,BranchMaster branchData,
				List<ListItemValue> sourcerTypes,LoginMaster issuerData,IndustryMaster industry,List<BrokerCommissionDetails> commissionList,
				LoginUserInfo loginUserData,LoginBranchMaster brokerBranch,
				LoginUserInfo premiaUser,LoginBranchMaster premiaBranch,LoginMaster premiaLogin,LoginMaster loginData,String commission,String cusId ) {
			EserviceBuildingDetails saveData = new EserviceBuildingDetails();
			NonMotorPolicyReq policyReq = req.getNonMotorPolicyReq();
			NonMotorBrokerReq brokerReq = req.getNonMotorBrokerReq();
			NonMotEndtReq endtReq = req.getNonMotEndtReq();
			try {
				// Date Differents
				Date periodStart = policyReq.getPolicyStartDate();
				Date periodEnd = policyReq.getPolicyEndDate();
				String diff = "0";
				saveData.setRequestReferenceNo(requestReferenceNo);
				saveData.setQuoteNo((StringUtils.isBlank(policyReq.getQuoteNo()) || "null".equalsIgnoreCase(policyReq.getQuoteNo()))?null:policyReq.getQuoteNo());
				saveData.setRiskId(Integer.valueOf(riskId));
				saveData.setLocationId(locId);
				saveData.setLocationName(locationName);
				saveData.setSectionId(data.getSectionId());
				saveData.setSectionDesc(sectionName);
				Date entryDate = new Date();
				String productId=policyReq.getProductId();
				String companyId=policyReq.getCompanyId();
				String branchCode=policyReq.getBranchCode();
				if (periodStart != null && periodEnd != null) {
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					String st = sdf.format(policyReq.getPolicyStartDate());
					String ed = sdf.format(policyReq.getPolicyEndDate());
					if (st.equalsIgnoreCase(ed)
							&& (endtReq.getEndorsementType() == null || endtReq.getEndorsementType() == 0)) {
						diff = "1";
					} else if (st.equalsIgnoreCase(ed)) {
						diff = "0";
					} else {
						Long diffInMillies = Math.abs(periodEnd.getTime() - periodStart.getTime());
						Long daysBetween = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS) + 1;
						diff = String.valueOf(daysBetween);
					}
				}
				LocalDate localDate1 = policyReq.getPolicyStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				LocalDate localDate2 = entryDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				if (localDate1.equals(localDate2)) {
					saveData.setPolicyStartDate(entryDate);
				} else {
					saveData.setPolicyStartDate(policyReq.getPolicyStartDate());
				}
				saveData.setParam1(StringUtils.isBlank(data.getParam1())?"":data.getParam1());
				saveData.setParam2(StringUtils.isBlank(data.getParam2())?"":data.getParam2());
				saveData.setParam3(StringUtils.isBlank(data.getParam3())?"":data.getParam3());
				saveData.setParam4(StringUtils.isBlank(data.getParam4())?"":data.getParam4());
				saveData.setParam5(StringUtils.isBlank(data.getParam5())?"":data.getParam5());
				saveData.setParam6(StringUtils.isBlank(data.getParam6())?"":data.getParam6());
				saveData.setParam7(StringUtils.isBlank(data.getParam7())?"":data.getParam7());
				saveData.setParam8(StringUtils.isBlank(data.getParam8())?"":data.getParam8());
				saveData.setParam9(StringUtils.isBlank(data.getParam9())?"":data.getParam9());
				saveData.setParam10(StringUtils.isBlank(data.getParam10())?"":data.getParam10());
				saveData.setParam11(StringUtils.isBlank(data.getParam11())?"":data.getParam11());
				saveData.setParam12(StringUtils.isBlank(data.getParam12())?"":data.getParam12());
				saveData.setParam13(StringUtils.isBlank(data.getParam13())?"":data.getParam13());
				saveData.setParam14(StringUtils.isBlank(data.getParam14())?"":data.getParam14());
				saveData.setParam15(StringUtils.isBlank(data.getParam15())?"":data.getParam15());
				saveData.setPolicyEndDate(periodEnd);
				saveData.setPolicyPeriord(Integer.valueOf(diff));
				saveData.setPromocode(policyReq.getPromocode());
				saveData.setHavepromocode(policyReq.getHavepromocode());
				saveData.setCurrency(policyReq.getCurrency());
				saveData.setExchangeRate(StringUtils.isBlank(policyReq.getExchangeRate()) ? null
						: new BigDecimal(policyReq.getExchangeRate()));
				saveData.setCustomerReferenceNo(policyReq.getCustomerReferenceNo());
				saveData.setProductId(policyReq.getProductId());
				saveData.setStatus(StringUtils.isBlank(policyReq.getStatus()) ? "Y" : policyReq.getStatus());
			//	CompanyProductMaster product = getCompanyProductMasterDropdown(policyReq.getCompanyId(),policyReq.getProductId());
				saveData.setProductDesc(product.getProductName());
				saveData.setUpdatedDate(new Date());
				saveData.setEntryDate(new Date());
				saveData.setCreatedBy(policyReq.getCreatedBy());
				saveData.setCompanyId(policyReq.getCompanyId());
				saveData.setIndustryDesc(
						StringUtils.isBlank(policyReq.getIndustryDesc()) ? null : data.getIndustrytypedesc());
		//		String companyName = getInscompanyMasterDropdown(policyReq.getCompanyId());
				saveData.setCompanyName(companyName);
				saveData.setBuildingOwnerYn(buildingOwnerYn);
				saveData.setAddress(address);
				saveData.setDomesticPackageYn("Y");
				saveData.setCommissionType(brokerReq.getCommissionType());
				if (StringUtils.isNotBlank(policyReq.getIndustryId())) {

					saveData.setIndustryId(StringUtils.isBlank(policyReq.getIndustryId()) ? null
							: Integer.valueOf(policyReq.getIndustryId()));

//					IndustryMaster industry = getIndustryName(policyReq.getCompanyId(), policyReq.getProductId(),
//							policyReq.getBranchCode(), policyReq.getIndustryId());

					saveData.setIndustryDesc(industry != null ? industry.getIndustryName() : "");
					saveData.setCategoryId(industry != null ? industry.getCategoryId() : "");
					saveData.setCategoryDesc(industry != null ? industry.getCategoryDesc() : "");
				}
	              
				// Endorsement Changes
				saveData.setEndtoptd(data.getEndtOpdt());
				if(endtReq!=null) {
				if (!(endtReq.getEndorsementType() == null || endtReq.getEndorsementType() == 0))

				{

					saveData.setOriginalPolicyNo(endtReq.getOriginalPolicyNo());
					saveData.setEndorsementDate(endtReq.getEndorsementDate());
					saveData.setEndorsementRemarks(endtReq.getEndorsementRemarks());
					saveData.setEndorsementEffdate(endtReq.getEndorsementEffdate());
					saveData.setEndtPrevPolicyNo(endtReq.getEndtPrevPolicyNo());
					saveData.setEndtPrevQuoteNo(endtReq.getEndtPrevQuoteNo());
					saveData.setEndtCount(endtReq.getEndtCount());
					saveData.setEndtStatus(endtReq.getEndtStatus());
					saveData.setIsFinyn(endtReq.getIsFinaceYn());
					saveData.setEndtCategDesc(endtReq.getEndtCategDesc());
					saveData.setEndorsementType(endtReq.getEndorsementType());
					saveData.setEndorsementTypeDesc(endtReq.getEndorsementTypeDesc());
					saveData.setPolicyNo(endtReq.getPolicyNo());
					saveData.setStatus(StringUtils.isBlank(policyReq.getStatus()) ? "E" : policyReq.getStatus());
				}
				}

				// Source Type Details
	
				
				saveData.setBranchCode(policyReq.getBranchCode());
				saveData.setBranchName(branchData!=null?branchData.getBranchName():"");
			
				saveData.setApplicationId(
						StringUtils.isBlank(brokerReq.getApplicationId()) ? "1" : brokerReq.getApplicationId());

				// Source Type Search Condition
				List<String> directSource = new ArrayList<String>();
				directSource.add("1");
				directSource.add("2");
				directSource.add("3");
				directSource.add("4");

				List<ListItemValue> filterSource = sourcerTypes.stream()
						.filter(o -> StringUtils.isNotBlank(brokerReq.getSourceTypeId())
								&& (o.getItemCode().equalsIgnoreCase(brokerReq.getSourceTypeId())
										|| o.getItemValue().equalsIgnoreCase(brokerReq.getSourceTypeId())))
						.collect(Collectors.toList());

				if (filterSource.size() > 0 && directSource.contains(filterSource.get(0).getItemCode())) {



					saveData.setBrokerCode(loginUserData.getOaCode());
					saveData.setAgencyCode(loginUserData.getAgencyCode());
					saveData.setLoginId(loginUserData.getLoginId());
					saveData.setCustomerCode(brokerReq.getBdmCode());
					saveData.setCustomerName(brokerReq.getCustomerName());
					saveData.setBdmCode(brokerReq.getBdmCode());
					saveData.setBdmName(brokerReq.getBdmName());
					saveData.setBrokerBranchCode(brokerBranch.getBrokerBranchCode());
					saveData.setBrokerBranchName(brokerBranch.getBrokerBranchName());
					saveData.setSourceTypeId(filterSource.get(0).getItemCode());
					saveData.setSourceType(filterSource.get(0).getItemValue());

					// Direct Source Type
					if (filterSource.get(0).getItemValue().contains("Direct")
							|| (premiaLogin != null && premiaUser != null && premiaBranch != null)) {
						saveData.setSalePointCode(brokerBranch.getSalePointCode());
						saveData.setBrokerTiraCode(loginUserData.getRegulatoryCode());
					} else {
						try {
							// Broker Tira Code
							PremiaTiraReq brokerTiraCodeReq = new PremiaTiraReq();
							brokerTiraCodeReq.setInsuranceId(policyReq.getCompanyId());
							brokerTiraCodeReq.setPremiaCode(brokerReq.getCustomerCode());
							List<PremiaTiraRes> brokerTira = premiaBrokerService
									.searchPremiaBrokerTiraCode(brokerTiraCodeReq);
							String brokerTiraCode = "";
							if (brokerTira.size() > 0) {
								brokerTiraCode = brokerTira.get(0).getTiraCode();
							}

							// Sale Point Code
							PremiaTiraReq brokerSpCodeReq = new PremiaTiraReq();
							brokerSpCodeReq.setInsuranceId(policyReq.getCompanyId());
							brokerSpCodeReq.setPremiaCode(brokerTiraCode);
							List<PremiaTiraRes> brokerSp = premiaBrokerService.searchPremiaBrokerSpCode(brokerSpCodeReq);
							String brokerSpCode = "";
							if (brokerSp.size() > 0) {
								brokerSpCode = brokerSp.get(0).getTiraCode();
							}

							saveData.setSalePointCode(brokerSpCode);
							saveData.setBrokerTiraCode(brokerTiraCode);
						} catch (Exception e) {
							e.printStackTrace();
							log.info("Log Details" + e.getMessage());

						}
					}

				} else {

					saveData.setBrokerCode(loginData.getOaCode());
					saveData.setAgencyCode(loginData.getAgencyCode());
					saveData.setCustomerCode(loginUserData.getCustomerCode());
					saveData.setLoginId(brokerReq.getLoginId());
					saveData.setCustomerName(loginUserData.getCustomerName());
					saveData.setBdmCode(null);
					saveData.setBrokerBranchCode(brokerBranch.getBrokerBranchCode());
					saveData.setBrokerBranchName(brokerBranch.getBrokerBranchName());
					saveData.setSalePointCode(brokerBranch.getSalePointCode());
					saveData.setBrokerTiraCode(loginUserData.getRegulatoryCode());
					List<ListItemValue> filterBrokerSource = sourcerTypes.stream()
							.filter(o -> o.getItemValue().equalsIgnoreCase(loginData.getSubUserType()))
							.collect(Collectors.toList());
					saveData.setSourceTypeId(filterBrokerSource.size() > 0 ? filterBrokerSource.get(0).getItemCode() : "");
					saveData.setSourceType(filterBrokerSource.size() > 0 ? filterBrokerSource.get(0).getItemValue()
							: loginData.getSubUserType());

				}
				if ("1".equalsIgnoreCase(brokerReq.getApplicationId())) {
					saveData.setSubUserType(saveData.getSourceType());
				} else {
		//			LoginMaster issuerData = loginRepo.findByLoginId(brokerReq.getApplicationId());
					saveData.setSubUserType(issuerData != null ? issuerData.getSubUserType() : saveData.getSourceType());
				}
				saveData.setSubUserType(saveData.getSourceType());

				// Broker Commission
				
					if (commissionList != null && commissionList.size() > 0) {
						BrokerCommissionDetails comm = commissionList.get(0);
						saveData.setCommissionPercentage(comm.getCommissionPercentage() == null ? new BigDecimal("0")
								: new BigDecimal(comm.getCommissionPercentage()));
						saveData.setVatCommission(comm.getCommissionVatPercent() == null ? new BigDecimal("0")
								: new BigDecimal(comm.getCommissionVatPercent()));
					} else {
					    
						//	 CompanyProductMaster cpm = productRepo
						//	            .findTopByCompanyIdAndProductIdOrderByAmendIdDesc(policyReq.getCompanyId(), Integer.valueOf(policyReq.getProductId()));
						if (filterSource.size() > 0 && directSource.contains(filterSource.get(0).getItemCode())) {
						if(!filterSource.get(0).getItemValue().contains("Direct"))	{
							saveData.setCommissionPercentage(product.getCommission() == null ? BigDecimal.ZERO : product.getCommission());
							 saveData.setVatCommission( BigDecimal.ZERO);
						}
						}
					}
				//}

				// Endt Commission
				if(endtReq!=null) {
				if (!(endtReq.getEndorsementType() == null || endtReq.getEndorsementType() == 0))

				{
					List<BuildingRiskDetails> mainMot = motBuildingRepo.findByQuoteNo(endtReq.getEndtPrevQuoteNo());
					for (BuildingRiskDetails mot : mainMot) {
						saveData.setCommissionPercentage(
								mot.getCommissionPercentage() == null ? saveData.getCommissionPercentage()
										: mot.getCommissionPercentage());
						saveData.setVatCommission(
								mot.getVatCommission() == null ? saveData.getVatCommission() : mot.getVatCommission());
					}

				}
				}

				saveData.setTiraCoverNoteNo(policyReq.getTiraCoverNoteNo());
				saveData.setBankCode(brokerReq.getBankCode());
				saveData.setAcExecutiveId(StringUtils.isBlank(policyReq.getAcExecutiveId()) ? null
						: Integer.valueOf(policyReq.getAcExecutiveId()));
				if (StringUtils.isNotBlank(brokerReq.getCommissionType())) {
					String commistionDesc = getListItem(policyReq.getCompanyId(), policyReq.getBranchCode(),
							"COMMISSION_TYPE", brokerReq.getCommissionType());
					saveData.setCommissionTypeDesc(commistionDesc);
				}

				if (StringUtils.isNotBlank(data.getWallType())) {
					String wallType = getListItem(saveData.getCompanyId(), saveData.getBranchCode(), "WALL_TYPE",
							data.getWallType());
					saveData.setWallType(data.getWallType());
					saveData.setWallTypeDesc(wallType);

				}

				if (StringUtils.isNotBlank(data.getRoofType())) {
					String roofType = getListItem(saveData.getCompanyId(), saveData.getBranchCode(), "ROOF_TYPE",
							data.getRoofType());
					saveData.setRoofType(data.getRoofType());
					saveData.setRoofTypeDesc(roofType);

				}

//				if (StringUtils.isNotBlank(data.getBuildingUsageId())) {
//					String buildingusage = getListItem(saveData.getCompanyId(), saveData.getBranchCode(), "BUILDING_USAGE",
//							data.getBuildingUsageId());
//					saveData.setBuildingUsageId(data.getBuildingUsageId());
//					saveData.setBuildingUsageDesc(buildingusage);
	//
//				}
				
				//Package plus-- Fire section 
				if (StringUtils.isNotBlank(data.getBuildingUsageId())) {
					String buildingusage = getListItem(saveData.getCompanyId(), saveData.getBranchCode(), "FIRE_SUMINSURED_TYPE",
							data.getBuildingUsageId());
					saveData.setBuildingUsageId(data.getBuildingUsageId());
					saveData.setBuildingUsageDesc(StringUtils.isNotBlank(data.getBuildingUsageDesc())?buildingusage:data.getBuildingUsageDesc());

				}
				SimpleDateFormat yf = new SimpleDateFormat("yyyy");
				Date today = new Date();
				String year = yf.format(today);
				// Building Age
				if (StringUtils.isNotBlank(data.getBuildingBuildYear())) {
					String buidingYear = data.getBuildingBuildYear();
					int buildingAge =Integer.valueOf(buidingYear);
					// saveData.setBuildingAge(buildingAge);
					saveData.setBuildingAge(buildingAge);
					// saveData.setBuildingBuildYear(Integer.valueOf(buidingYear));

					}
				// New Inputs
				if (StringUtils.isNotBlank(data.getTypeOfProperty())) {
					String typeOfProperty = getListItem(saveData.getCompanyId(), saveData.getBranchCode(),
							"TYPE_OF_PROPERTIES", data.getTypeOfProperty());
			//		saveData.setTypeOfProperty(data.getTypeOfProperty());
			//		saveData.setTypeOfPropertyDesc(typeOfProperty);

				}
//				String industryName="";
//				if (StringUtils.isNotBlank(data.getIndustryId()) && Integer.valueOf(data.getIndustryId())>0) {
//					IndustryMaster industrydel = industryrepo.findByIndustryIdAndCompanyIdAndProductId(
//							Integer.valueOf(data.getIndustryId()), policyReq.getCompanyId(), policyReq.getProductId());
//					if(industrydel==null) {
//						industrydel = industryrepo.findByIndustryIdAndCompanyIdAndProductId(
//								Integer.valueOf(data.getIndustryId()), policyReq.getCompanyId(),"99999");
//					}
//					if (industrydel != null) {
//						industryName = industrydel.getIndustryName();
//						saveData.setIndustryDesc(industryName);
//					}
//				}
//				saveData.setIndustryId(
//						StringUtils.isBlank(data.getIndustryId()) ? null : Integer.valueOf(data.getIndustryId()));
//				
			
				// Region Code
				if (!StringUtils.isBlank(data.getRegionCode())) {
					if(StringUtils.isBlank(data.getRegionName())) {
						List<RegionMaster> dataList = regionrepo.findByRegionCode(data.getRegionCode());
						saveData.setRegionDesc(
								!dataList.isEmpty() && !StringUtils.isBlank(dataList.get(0).getRegionName()) ? dataList.get(0).getRegionName(): null);
					}else {
						saveData.setRegionDesc(data.getRegionName());
					}	
					saveData.setRegionCode(data.getRegionCode());
				}
				// District Code
				if (!StringUtils.isBlank(data.getDistrictCode())) {
					if(StringUtils.isBlank(data.getDistrictName())) {
						List<StateMaster> statedata = staterepo.findByStateId(Integer.valueOf(data.getDistrictCode()));
						saveData.setDistrictDesc(
								!statedata.isEmpty() && !StringUtils.isBlank(statedata.get(0).getStateName()) ?  statedata.get(0).getStateName() : null);
					}else {
						saveData.setDistrictDesc(data.getDistrictName());
					}
				  	saveData.setDistrictCode(data.getDistrictCode());
				}
				//-------------------------------------------------------------
				//Common for All Section type of Sum insured
				saveData.setSumInsured(StringUtils.isBlank(data.getSumInsured()) ? new BigDecimal(0)
						: new BigDecimal(data.getSumInsured()));
				if (StringUtils.isNotBlank(saveData.getExchangeRate().toString())) {
					BigDecimal exRate = saveData.getExchangeRate();
					if (StringUtils.isNotBlank(data.getSumInsured())) {
						saveData.setSumInsuredLc(new BigDecimal(data.getSumInsured()).multiply(exRate));
						;
					} else {
						saveData.setSumInsuredLc(BigDecimal.ZERO);    
					}
				}
			    //-------------------------------------------------------------

				// Content Addition Info
				saveData.setContentId((StringUtils.isBlank(data.getContentId()) || data.getContentId() == null) ? null
						: data.getContentId());
				saveData.setContentDesc((StringUtils.isBlank(data.getContentDesc()) || data.getContentDesc() == null) ? null
						: data.getContentDesc());
				saveData.setSerialNo((StringUtils.isBlank(data.getSerialNo()) || data.getSerialNo() == null) ? null
						: data.getSerialNo());
				saveData.setDescriptionOfRisk(
						(StringUtils.isBlank(data.getDescription()) || data.getDescription() == null) ? null
								: data.getDescription());

				saveData.setInternalWallType(
						StringUtils.isNotBlank(data.getInternalWallType()) ? Integer.valueOf(data.getInternalWallType())
								: 0);
				String wall_type = getListItem(policyReq.getCompanyId(), policyReq.getBranchCode(), "wall_type",
						data.getInternalWallType());
				saveData.setInternalWallDesc(StringUtils.isBlank(wall_type) ? "" : wall_type);
				
				
				String firstLossPayee="";
				if (!StringUtils.isBlank(data.getFirstLossPayee())) {
					firstLossPayee= getByBankCode(companyId, branchCode, data.getFirstLossPayee());
					
				}
				saveData.setFirstLossPayee(StringUtils.isBlank(firstLossPayee) ? "" : firstLossPayee);



				BigDecimal exchangeRate = saveData.getExchangeRate() != null ? saveData.getExchangeRate() : BigDecimal.ZERO;

				saveData.setCoveringDetails(
						StringUtils.isBlank(data.getCoveringDetails()) ? "" : data.getCoveringDetails());
				saveData.setDescriptionOfRisk(
						StringUtils.isBlank(data.getDescriptionOfRisk()) ? "" : data.getDescriptionOfRisk());
				saveData.setBusinessInterruption(
						StringUtils.isBlank(data.getBusinessInterruption()) ? "" : data.getBusinessInterruption());
				saveData.setCategoryId(StringUtils.isBlank(data.getCategoryId())?null:data.getCategoryId());

				saveData.setCategoryDesc(StringUtils.isBlank(data.getCategoryDesc())?null:data.getCategoryDesc());
				saveData.setOccupationType(StringUtils.isBlank(data.getOccupationId())?null:data.getOccupationId());
				saveData.setOccupationTypeDesc(StringUtils.isBlank(data.getOccupationDesc())?null:data.getOccupationDesc());
				saveData.setIndustryId(
						StringUtils.isBlank(data.getIndustryType()) ? 0 : Integer.valueOf(data.getIndustryType()));

				// Bond
				saveData.setBondType(data.getBondType());
				saveData.setBondYear(data.getBondYear());


				
				saveData.setFirstLossPercentId(StringUtils.isBlank(data.getFirstLossPercentId()) ? null
						: Integer.valueOf(data.getFirstLossPercentId()));
				if (StringUtils.isNotBlank(data.getFirstLossPercentId())) {
					String firstLossPercent = getListItem(policyReq.getCompanyId(),policyReq.getBranchCode(),
							"BURGLARY_FIRST_LOSS", data.getFirstLossPercentId());
					saveData.setFirstLossPercent(
							StringUtils.isBlank(firstLossPercent) ? null : Integer.valueOf(firstLossPercent));
				}
				
				saveData.setIndemityPeriod(StringUtil.isBlank(data.getIndemityPeriod())?"0":data.getIndemityPeriod());
				saveData.setIndemityPeriodDesc(StringUtils.isNotBlank(data.getIndemnityTypeDesc())? data.getIndemnityTypeDesc():"");
				saveData.setCoverId(data.getCoverId());
				saveData.setCollateralName(StringUtils.isBlank(data.getCollateralName())?"":data.getCollateralName());
				saveData.setCollateralYn(StringUtils.isBlank(data.getCollateralYn())?" ":data.getCollateralYn());
				saveData.setBorrowerType(StringUtils.isBlank(data.getBorrowerType())?" ":data.getBorrowerType());
				saveData.setBorrowerTypeDesc(StringUtils.isBlank(data.getBorrowerTypeDesc())?" ":data.getBorrowerTypeDesc());
				
				saveData.setGeographicalCoverage(StringUtils.isBlank(data.getGeographicalCoverage())?" ":data.getGeographicalCoverage());	//  phoenix minimum Limit per 
				saveData.setModeOfTransport(StringUtils.isBlank(data.getModeOfTransport())?" ":data.getModeOfTransport()); // phoenix (Trip per)	
				 	
				saveData.setNoOfClaim(StringUtils.isBlank(data.getNoOfClaim())?0:Integer.valueOf(data.getNoOfClaim()));
				saveData.setNoOfClaimDesc(StringUtils.isBlank(data.getNoOfClaimDesc())?null:data.getNoOfClaimDesc());
				saveData.setGrossProfitLc(StringUtils.isNotBlank(data.getGrossProfitLc())?new BigDecimal(data.getGrossProfitLc()):BigDecimal.ZERO);
				saveData.setBusinessSource(brokerReq.getBusinessSource());
                saveData.setBusinessSourcId(brokerReq.getBusinessSourceId());
			//	buildingRepo.save(saveData);
                
                if (StringUtils.isNotBlank(data.getIndustryId())) {
    				//
//    										saveData.setIndustryId(StringUtils.isBlank(data.getIndustryId()) ? null
//    												: Integer.valueOf(data.getIndustryId()));
    				//
//    										saveData.setIndustryDesc(industry != null ? industry.getIndustryName() : "");
//    										saveData.setCategoryId(industry != null ? industry.getCategoryId() : "");
//    										saveData.setCategoryDesc(industry != null ? industry.getCategoryDesc() : "");
    				data.setIndustryType(data.getIndustryId());
    			} else if (StringUtils.isNotBlank(data.getIndustryType())) {

    				saveData.setIndustryId(
    						StringUtils.isBlank(data.getIndustryType()) ? null : Integer.valueOf(data.getIndustryType()));
    				List<IndustryMaster> industrydelList = industryrepo
    						.findByIndustryIdAndCompanyIdAndProductIdAndStatusOrderByAmendIdDesc(
    								Integer.valueOf(data.getIndustryType()), policyReq.getCompanyId(),
    								policyReq.getProductId(), "Y");
    				IndustryMaster industrydel = null;
    				if (industrydelList != null && !industrydelList.isEmpty()) {
    					industrydel = industrydelList.get(0);
    				}
    				if (industrydel == null) {
    					industrydelList = industryrepo.findByIndustryIdAndCompanyIdAndProductIdAndStatusOrderByAmendIdDesc(
    							Integer.valueOf(data.getIndustryType()), policyReq.getCompanyId(), "99999", "Y");
    					if (industrydelList != null && !industrydelList.isEmpty()) {
    						industrydel = industrydelList.get(0);
    					}
    				}
    				if (industrydel != null) {
    					saveData.setIndustryDesc(industrydel.getIndustryName());
    				}

    			//	saveData.setCategoryId(industrydel != null ? industrydel.getCategoryId() : "");
    			//	saveData.setCategoryDesc(industrydel != null ? industrydel.getCategoryDesc() : "");

    			}
    			if(!StringUtils.isBlank(data.getIndustrytypedesc())){
    			saveData.setIndustryDesc(StringUtils.isBlank(data.getIndustrytypedesc()) ? "" : data.getIndustrytypedesc());
    			}
    			saveData.setBuildingFloors(data.getBuildingFloors());
    			saveData.setCount(StringUtils.isBlank(data.getCount()) ? null : data.getCount());
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Exception Is ---> " + e.getMessage());
				return null;
			}

			return saveData;
		}
		public String getByBankCode(String companyId,String branchCode, String bankCode) {
			String firstLossPayee="";
			DozerBeanMapper mapper = new DozerBeanMapper();
			try {
				Date today = new Date();
				Calendar cal = new GregorianCalendar();
				cal.setTime(today);
				cal.set(Calendar.HOUR_OF_DAY, 23);
				cal.set(Calendar.MINUTE, 1);
				today = cal.getTime();

				List<BankMaster> list = new ArrayList<BankMaster>();
			
				// Find Latest Record
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<BankMaster> query = cb.createQuery(BankMaster.class);

				// Find All
				Root<BankMaster> b = query.from(BankMaster.class);

				// Select
				query.select(b);

				// Amend ID Max Filter
				Subquery<Long> amendId = query.subquery(Long.class);
				Root<BankMaster> ocpm1 = amendId.from(BankMaster.class);
				amendId.select(cb.max(ocpm1.get("amendId")));
				Predicate a1 = cb.equal(ocpm1.get("bankCode"), b.get("bankCode"));
				Predicate a2 = cb.equal(ocpm1.get("companyId"), b.get("companyId"));
				Predicate a3 = cb.equal(ocpm1.get("branchCode"),b.get("branchCode"));

				amendId.where(a1, a2,a3);

				// Order By
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(b.get("branchCode")));

				// Where
				Predicate n1 = cb.equal(b.get("amendId"), amendId);
				Predicate n2 = cb.equal(b.get("companyId"), companyId);
				Predicate n3 = cb.equal(b.get("branchCode"), branchCode);
				Predicate n4 = cb.equal(b.get("bankCode"), bankCode);
				Predicate n6 = cb.equal(b.get("branchCode"), "99999");
				Predicate n7 = cb.or(n3,n6);
				query.where(n1,n2,n4,n7).orderBy(orderList);
				
				// Get Result
				TypedQuery<BankMaster> result = em.createQuery(query);

				list = result.getResultList();
				list = list.stream().filter(distinctByKey(o -> Arrays.asList(o.getBankCode()))).collect(Collectors.toList());
				list.sort(Comparator.comparing(BankMaster :: getBankFullName ));
				//firstLossPayee=list.get(0).getBankFullName();
				firstLossPayee=list.get(0).getBankCode();
				} catch (Exception e) {
				e.printStackTrace();
				log.info("Exception is ---> " + e.getMessage());
				return null;
			}
			return firstLossPayee;
		}


		public BuildingSectionRes insertBuildingSectionNonMotor(NonMotorSaveReq req, NonMotorSectionReq section,
				String riskId, Integer locId, String requestReferenceNo, String locationName,Integer Coverid ,List<ProductSectionMaster> sectionList,String productTypeDesc
				,String endtLocation) {
		//	List<BuildingSectionRes> sectionResList = new ArrayList<BuildingSectionRes>();
			BuildingSectionRes secRes = new BuildingSectionRes();
			NonMotorPolicyReq policyReq = req.getNonMotorPolicyReq();
		//	NonMotorBrokerReq brokerReq = req.getNonMotorBrokerReq();
			NonMotEndtReq endtReq = req.getNonMotEndtReq();
		//	DozerBeanMapper dozerMapper = new DozerBeanMapper();
			try {
			//	String companyId = policyReq.getCompanyId();
		//		String productId = policyReq.getProductId();
			//	String branchCode = policyReq.getBranchCode();
			//	List<ProductSectionMaster> sectionList = getProductSectionDropdown(companyId, productId);

				EserviceSectionDetails secData = new EserviceSectionDetails();
//				List<ProductSectionMaster> filterSection = sectionList.stream()
//						.filter(o -> o.getSectionId().equals(Integer.valueOf(section.getSectionId())))
//						.collect(Collectors.toList());
				ProductSectionMaster sec = sectionList.get(0);
				dozerBeanMapper.map(policyReq, secData);
				secData.setRequestReferenceNo(requestReferenceNo);
				secData.setQuoteNo(StringUtils.isBlank(policyReq.getQuoteNo())?null:policyReq.getQuoteNo());
				secData.setLocationId(locId);
				secData.setLocationName(locationName);
				secData.setOverallPremiumFc(null);
				secData.setOverallPremiumLc(null);
				secData.setExchangeRate(new BigDecimal(policyReq.getExchangeRate()));
				secData.setCurrencyId(policyReq.getCurrency());
				secData.setSectionId(section.getSectionId());
				secData.setSectionName(sec.getSectionName());
				secData.setRiskId(Integer.valueOf(riskId));
				secData.setProductType(sec.getMotorYn());
				secData.setEntryDate(new Date());
				secData.setUpdatedBy(policyReq.getCreatedBy());
				secData.setStatus("Y");
				secData.setCoverId(Coverid);
			//	String productTypeDesc = getListItem(companyId, branchCode, "PRODUCT_CATEGORY", secData.getProductType());
				secData.setProductTypeDesc(productTypeDesc);
				secData.setUserOpt("Y");
				secData.setEndtLocationyn(StringUtils.isNotBlank(endtLocation)?endtLocation:null);
				// Response
				
				secRes.setSectionId(section.getSectionId());
				secRes.setSectionName(sec.getSectionName());
				secRes.setMotorYn(sec.getMotorYn());
				secRes.setRiskId(section.getRiskId().isBlank()?0:Integer.valueOf(section.getRiskId()));
				secRes.setCoverId(section.getCoverId());
				
				if (endtReq != null) {
					if (!(endtReq.getEndorsementType() == null || endtReq.getEndorsementType() == 0))

					{

						secData.setOriginalPolicyNo(endtReq.getOriginalPolicyNo());
						secData.setEndorsementDate(endtReq.getEndorsementDate());
						secData.setEndorsementRemarks(endtReq.getEndorsementRemarks());
						secData.setEndorsementEffdate(endtReq.getEndorsementEffdate());
						secData.setEndtPrevPolicyNo(endtReq.getEndtPrevPolicyNo());
						secData.setEndtPrevQuoteNo(endtReq.getEndtPrevQuoteNo());
						secData.setEndtCount(endtReq.getEndtCount());
						secData.setEndtStatus(endtReq.getEndtStatus());
						secData.setIsFinyn(endtReq.getIsFinaceYn());
						secData.setEndtCategDesc(endtReq.getEndtCategDesc());
						secData.setEndorsementType(endtReq.getEndorsementType());
						secData.setEndorsementTypeDesc(endtReq.getEndorsementTypeDesc());
						secData.setPolicyNo(endtReq.getPolicyNo());
						secData.setStatus(StringUtils.isBlank(policyReq.getStatus()) ? "E" : policyReq.getStatus());
					}
				}
				secRes.setSectiondetails(secData);
			//	sectionResList.add(secRes);
			//	eserSecRepo.saveAndFlush(secData);

			} catch (Exception e) {
				e.printStackTrace();
				log.info("Exception Is ---> " + e.getMessage());

			}
			return secRes;

			
		}
		public EserviceCommonDetails insertCommonPolicyAndEndtAndBrokerDetails(NonMotorSaveReq req,
				String requestReferenceNo, String riskId, NonMotorSectionReq data, Integer locId, String locationName,
				String sectionName,String address,String companyName,List<ListItemValue> sourcerTypes
				,LoginMaster premiaLogin,LoginUserInfo loginUserData,LoginBranchMaster brokerBranch
				, LoginUserInfo premiaUser, LoginBranchMaster premiaBranch, LoginMaster loginData,
				List<BrokerCommissionDetails> commissionList,String cusId) {
			EserviceCommonDetails saveData = new EserviceCommonDetails();
			NonMotorPolicyReq policyReq = req.getNonMotorPolicyReq();
			NonMotorBrokerReq brokerReq = req.getNonMotorBrokerReq();
			NonMotEndtReq endtReq = req.getNonMotEndtReq();
			try {
				String companyId = policyReq.getCompanyId();
				String productId = policyReq.getProductId();
				String branchCode = policyReq.getBranchCode();
				// Date Differents
				Date periodStart = policyReq.getPolicyStartDate();
				Date periodEnd = policyReq.getPolicyEndDate();
				String diff = "0";
				Date entryDate = new Date();
				if (periodStart != null && periodEnd != null) {
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					String st = sdf.format(policyReq.getPolicyStartDate());
					String ed = sdf.format(policyReq.getPolicyEndDate());
					if (st.equalsIgnoreCase(ed)
							&& (endtReq.getEndorsementType() == null || endtReq.getEndorsementType() == 0)) {
						diff = "1";
					} else if (st.equalsIgnoreCase(ed)) {
						diff = "0";
					} else {
						Long diffInMillies = Math.abs(periodEnd.getTime() - periodStart.getTime());
						Long daysBetween = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS) + 1;

						diff = String.valueOf(daysBetween);
					}

				}
				saveData = new DozerBeanMapper().map(req, EserviceCommonDetails.class);
				LocalDate localDate1 = policyReq.getPolicyStartDate().toInstant().atZone(ZoneId.systemDefault())
						.toLocalDate();
				LocalDate localDate2 = entryDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				if (localDate1.equals(localDate2)) {
					saveData.setPolicyStartDate(entryDate);
				} else {
					saveData.setPolicyStartDate(policyReq.getPolicyStartDate());
				}
				saveData.setParam1(StringUtils.isBlank(data.getParam1()) ? "" : data.getParam1());
				saveData.setParam2(StringUtils.isBlank(data.getParam2()) ? "" : data.getParam2());
				saveData.setParam3(StringUtils.isBlank(data.getParam3()) ? "" : data.getParam3());
				saveData.setParam4(StringUtils.isBlank(data.getParam4()) ? "" : data.getParam4());
				saveData.setParam5(StringUtils.isBlank(data.getParam5()) ? "" : data.getParam5());
				saveData.setParam6(StringUtils.isBlank(data.getParam6()) ? "" : data.getParam6());
				saveData.setParam7(StringUtils.isBlank(data.getParam7()) ? "" : data.getParam7());
				saveData.setParam8(StringUtils.isBlank(data.getParam8()) ? "" : data.getParam8());
				saveData.setParam9(StringUtils.isBlank(data.getParam9()) ? "" : data.getParam9());
				saveData.setParam10(StringUtils.isBlank(data.getParam10()) ? "" : data.getParam10());
				saveData.setRequestReferenceNo(requestReferenceNo);
				saveData.setQuoteNo(StringUtils.isBlank(policyReq.getQuoteNo()) ? null : policyReq.getQuoteNo());
				saveData.setRiskId(Integer.valueOf(riskId));
				saveData.setOriginalRiskId(Integer.valueOf(riskId));
				saveData.setLocationId(locId);
				saveData.setLocationName(locationName);
				saveData.setSectionId(data.getSectionId());
				saveData.setSectionName(sectionName);
				saveData.setPolicyStartDate(periodStart);
				saveData.setPolicyEndDate(periodEnd);
				saveData.setPolicyPeriod(Integer.valueOf(diff));
				saveData.setPromocode(policyReq.getPromocode());
				saveData.setHavepromocode(policyReq.getHavepromocode());
				saveData.setCurrency(policyReq.getCurrency());
				saveData.setEntryDate(new Date());
				saveData.setUpdatedDate(new Date());
				saveData.setExchangeRate(StringUtils.isBlank(policyReq.getExchangeRate()) ? null
						: new BigDecimal(policyReq.getExchangeRate()));
				BigDecimal exchangeRate = policyReq.getExchangeRate() != null
						? new BigDecimal(policyReq.getExchangeRate())
						: BigDecimal.ZERO;
				saveData.setCustomerReferenceNo(policyReq.getCustomerReferenceNo());
				saveData.setProductId(productId);
				saveData.setStatus(StringUtils.isBlank(policyReq.getStatus()) ? "Y" : policyReq.getStatus());
				CompanyProductMaster product = getCompanyProductMasterDropdown(companyId, productId); // productRepo.findByProductIdOrderByAmendIdDesc(Integer.valueOf(productId));
				saveData.setProductDesc(product.getProductName());
				saveData.setSectionId(data.getSectionId());
				saveData.setCompanyId(companyId);
				// String companyName = getInscompanyMasterDropdown(companyId); //
				// companyRepo.findByCompanyIdOrderByAmendIdDesc(companyId);
				saveData.setCompanyName(companyName);
				saveData.setAddress(address);
				// Endorsement Changes
				saveData.setEndtoptd(data.getEndtOpdt());
				if (endtReq != null) {
					if (!(endtReq.getEndorsementType() == null || endtReq.getEndorsementType() == 0))

					{

						saveData.setOriginalPolicyNo(endtReq.getOriginalPolicyNo());
						saveData.setEndorsementDate(endtReq.getEndorsementDate());
						saveData.setEndorsementRemarks(endtReq.getEndorsementRemarks());
						saveData.setEndorsementEffdate(endtReq.getEndorsementEffdate());
						saveData.setEndtPrevPolicyNo(endtReq.getEndtPrevPolicyNo());
						saveData.setEndtPrevQuoteNo(endtReq.getEndtPrevQuoteNo());
						saveData.setEndtCount(endtReq.getEndtCount());
						saveData.setEndtStatus(endtReq.getEndtStatus());
						saveData.setIsFinyn(endtReq.getIsFinaceYn());
						saveData.setEndtCategDesc(endtReq.getEndtCategDesc());
						saveData.setEndorsementType(endtReq.getEndorsementType());
						saveData.setEndorsementTypeDesc(endtReq.getEndorsementTypeDesc());
						saveData.setPolicyNo(endtReq.getPolicyNo());
						saveData.setStatus(StringUtils.isBlank(policyReq.getStatus()) ? "E" : policyReq.getStatus());
					}
				}

				// Source Type Details
				BranchMaster branchData = getBranchMasterRes(companyId, branchCode);
				saveData.setBranchCode(branchCode);
				saveData.setBranchName(branchData.getBranchName());
				saveData.setApplicationId(
						StringUtils.isBlank(brokerReq.getApplicationId()) ? "1" : brokerReq.getApplicationId());

				// Source Type Search Condition
				List<String> directSource = new ArrayList<String>();
				directSource.add("1");
				directSource.add("2");
				directSource.add("3");
				directSource.add("4");

				List<ListItemValue> filterSource = sourcerTypes.stream()
						.filter(o -> StringUtils.isNotBlank(brokerReq.getSourceTypeId())
								&& (o.getItemCode().equalsIgnoreCase(brokerReq.getSourceTypeId())
										|| o.getItemValue().equalsIgnoreCase(brokerReq.getSourceTypeId())))
						.collect(Collectors.toList());

				if (filterSource.size() > 0 && directSource.contains(filterSource.get(0).getItemCode())) {

					saveData.setBrokerCode(loginUserData.getOaCode());
					saveData.setAgencyCode(loginUserData.getAgencyCode());
					saveData.setLoginId(loginUserData.getLoginId());
					saveData.setCustomerCode(brokerReq.getBdmCode());
					saveData.setCustomerName(brokerReq.getCustomerName());
					saveData.setBdmCode(brokerReq.getBdmCode());
					saveData.setBdmName(brokerReq.getBdmName());
					saveData.setBrokerBranchCode(brokerBranch.getBrokerBranchCode());
					saveData.setBrokerBranchName(brokerBranch.getBrokerBranchName());
					saveData.setSourceTypeId(filterSource.get(0).getItemCode());
					saveData.setSourceType(filterSource.get(0).getItemValue());
					if (filterSource.get(0).getItemValue().contains("Direct")
							|| (premiaLogin != null && premiaUser != null && premiaBranch != null)) {
						saveData.setSalePointCode(brokerBranch.getSalePointCode());
						saveData.setBrokerTiraCode(loginUserData.getRegulatoryCode());
					} else {
						try {
							// Broker Tira Code
							PremiaTiraReq brokerTiraCodeReq = new PremiaTiraReq();
							brokerTiraCodeReq.setInsuranceId(companyId);
							brokerTiraCodeReq.setPremiaCode(brokerReq.getCustomerCode());
							List<PremiaTiraRes> brokerTira = premiaBrokerService
									.searchPremiaBrokerTiraCode(brokerTiraCodeReq);
							String brokerTiraCode = "";
							if (brokerTira.size() > 0) {
								brokerTiraCode = brokerTira.get(0).getTiraCode();
							}

							// Sale Point Code
							PremiaTiraReq brokerSpCodeReq = new PremiaTiraReq();
							brokerSpCodeReq.setInsuranceId(companyId);
							brokerSpCodeReq.setPremiaCode(brokerTiraCode);
							List<PremiaTiraRes> brokerSp = premiaBrokerService
									.searchPremiaBrokerSpCode(brokerSpCodeReq);
							String brokerSpCode = "";
							if (brokerSp.size() > 0) {
								brokerSpCode = brokerSp.get(0).getTiraCode();
							}

							saveData.setSalePointCode(brokerSpCode);
							saveData.setBrokerTiraCode(brokerTiraCode);
						} catch (Exception e) {
							e.printStackTrace();
							log.info("Log Details" + e.getMessage());

						}
					}

				} else {

					saveData.setBrokerCode(loginData.getOaCode());
					saveData.setAgencyCode(loginData.getAgencyCode());
					saveData.setCustomerCode(loginUserData.getCustomerCode());
					saveData.setLoginId(brokerReq.getLoginId());
					saveData.setCustomerName(loginUserData.getCustomerName());
					saveData.setBdmCode(null);
					saveData.setBrokerBranchCode(brokerBranch.getBrokerBranchCode());
					saveData.setBrokerBranchName(brokerBranch.getBrokerBranchName());
					saveData.setSalePointCode(brokerBranch.getSalePointCode());
					saveData.setBrokerTiraCode(loginUserData.getRegulatoryCode());

					List<ListItemValue> filterBrokerSource = sourcerTypes.stream()
							.filter(o -> o.getItemValue().equalsIgnoreCase(loginData.getSubUserType()))
							.collect(Collectors.toList());

					saveData.setSourceTypeId(
							filterBrokerSource.size() > 0 ? filterBrokerSource.get(0).getItemCode() : "");
					saveData.setSourceType(filterBrokerSource.size() > 0 ? filterBrokerSource.get(0).getItemValue()
							: loginData.getSubUserType());

				}
				if ("1".equalsIgnoreCase(brokerReq.getApplicationId())) {
					saveData.setSubUserType(saveData.getSourceType());
				} else {
					LoginMaster issuerData = loginRepo.findByLoginId(brokerReq.getApplicationId());
					saveData.setSubUserType(
							issuerData != null ? issuerData.getSubUserType() : saveData.getSourceType());
				}

				// Broker Commission

				if (commissionList != null && !commissionList.isEmpty()) {

					BrokerCommissionDetails comm = commissionList.get(0);

					saveData.setCommissionPercentage(comm.getCommissionPercentage() == null ? BigDecimal.ZERO
							: new BigDecimal(comm.getCommissionPercentage()));

					saveData.setVatCommission(comm.getCommissionVatPercent() == null ? BigDecimal.ZERO
							: new BigDecimal(comm.getCommissionVatPercent()));

				} else {

					// CompanyProductMaster cpm = productRepo
					// .findTopByCompanyIdAndProductIdOrderByAmendIdDesc(policyReq.getCompanyId(),
					// Integer.valueOf(policyReq.getProductId()));
					if (filterSource.size() > 0 && directSource.contains(filterSource.get(0).getItemCode())) {
					if (!filterSource.get(0).getItemValue().contains("Direct")) {
						saveData.setCommissionPercentage(
								product.getCommission() == null ? BigDecimal.ZERO : product.getCommission());
						saveData.setVatCommission(BigDecimal.ZERO);
					}
					}
				}

				// }
				// Endt Commission
				if (!(endtReq.getEndorsementType() == null || endtReq.getEndorsementType() == 0))

				{
					List<CommonDataDetails> mainMotList = commonRepo.findByQuoteNo(endtReq.getEndtPrevQuoteNo());
					if (mainMotList != null && mainMotList.size() > 0) {
						CommonDataDetails mainMot = mainMotList.get(0);
						saveData.setCommissionPercentage(
								mainMot.getCommissionPercentage() == null ? saveData.getCommissionPercentage()
										: new BigDecimal(mainMot.getCommissionPercentage()));
						saveData.setVatCommission(mainMot.getVatCommission() == null ? saveData.getVatCommission()
								: new BigDecimal(mainMot.getVatCommission()));
					}
				}

				saveData.setTiraCoverNoteNo(policyReq.getTiraCoverNoteNo());
				saveData.setBankCode(brokerReq.getBankCode());
				saveData.setAcExecutiveId(
						StringUtils.isBlank(policyReq.getAcExecutiveId()) ? null : policyReq.getAcExecutiveId());
				// Status

				saveData.setSectionName(sectionName);
				saveData.setCreatedBy(policyReq.getCreatedBy());

				// Industry Id
				if (StringUtils.isNotBlank(data.getIndustryId())) {

					saveData.setIndustryId(StringUtils.isBlank(data.getIndustryId()) ? null : data.getIndustryId());
					IndustryMaster industry = getIndustryName(companyId, productId, branchCode, data.getIndustryId());
					saveData.setIndustryName(industry != null ? industry.getIndustryName() : "");

				} else if (StringUtils.isNotBlank(data.getIndustryType())) {
					saveData.setIndustryId(data.getIndustryType());
					saveData.setIndustryName(data.getIndustrytypedesc());
				}

				// Occupation Type
				if (StringUtils.isNotBlank(data.getOccupationId())) {
					OccupationMaster occupationData = getOccupationMasterDropdown(companyId, "99999", productId,
							data.getOccupationId());
					saveData.setOccupationType(occupationData.getOccupationId().toString());
					saveData.setOccupationDesc(occupationData.getOccupationName());
					saveData.setCategoryId(occupationData.getCategoryId());
					// saveData.setCategoryDesc("Class " + occupationData.getCategoryId());
				}
				saveData.setOtherOccupation(
						StringUtils.isBlank(data.getOtherOccupation()) ? null : data.getOtherOccupation());

				// Occupation Type
				if (StringUtils.isNotBlank(data.getCategoryId()) && StringUtils.isBlank(data.getOccupationId())) {
					saveData.setCategoryId(StringUtils.isBlank(data.getCategoryId()) ? null : data.getCategoryId());
					saveData.setCategoryDesc(
							StringUtils.isBlank(data.getCategoryDesc()) ? null : data.getCategoryDesc());

				}

				// employers liability Count
				saveData.setTotalNoOfEmployees(StringUtils.isBlank(data.getTotalNoOfEmployees()) ? 0l
						: Long.valueOf(data.getTotalNoOfEmployees()));
				// Count
				if (StringUtils.isNotBlank(data.getTotalNoOfEmployees())) {
					saveData.setCount(StringUtils.isBlank(data.getTotalNoOfEmployees()) ? 1
							: Integer.valueOf(data.getTotalNoOfEmployees()));
				} else if (StringUtils.isNotBlank(data.getFidEmpCount())) {
					saveData.setCount(
							StringUtils.isBlank(data.getFidEmpCount()) ? 1 : Integer.valueOf(data.getFidEmpCount()));
				} else if (StringUtils.isNotBlank(data.getCount())) {
					saveData.setCount(StringUtils.isBlank(data.getCount()) ? 1 : Integer.valueOf(data.getCount()));
				}

				// Fidelity Count
				saveData.setFidEmpCount(
						StringUtils.isBlank(data.getFidEmpCount()) ? null : new BigDecimal(data.getFidEmpCount()));

				// Common suminsured field for additional info
				// Personal Accident

				BigDecimal sumInsured = BigDecimal.ZERO;
				if (StringUtils.isNotBlank(data.getSumInsured())) {
					sumInsured = new BigDecimal(data.getSumInsured());
				} else if (StringUtils.isNotBlank(data.getPersonalAccidentSi())) {
					sumInsured = new BigDecimal(data.getPersonalAccidentSi());
				}

				saveData.setSumInsured(sumInsured);
				// Lc Calculation
				saveData.setSumInsuredLc(sumInsured == null ? BigDecimal.ZERO : sumInsured.multiply(exchangeRate));

				saveData.setNickName(StringUtils.isBlank(data.getNickName()) ? null : data.getNickName());
				saveData.setDob(data.getDob() == null ? null : data.getDob());
				saveData.setRelationType(StringUtils.isBlank(data.getRelationType()) ? null : data.getRelationType());
				String relationTypeDesc = getListItem(companyId, branchCode, "RELATION_TYPE_HOME",
						data.getRelationType());
				saveData.setRelationTypeDesc(StringUtils.isBlank(relationTypeDesc) ? "" : relationTypeDesc);

				saveData.setProfessionalType(
						StringUtils.isBlank(data.getProfessionalType()) ? "" : data.getProfessionalType());
				if (StringUtils.isNotBlank(data.getProfessionalType())) {
					String professionalTypeDesc = getListItem(companyId, branchCode, "Servant TYPE",
							data.getProfessionalType());
					saveData.setProfessionalTypeDesc(
							StringUtils.isBlank(professionalTypeDesc) ? "" : professionalTypeDesc);
				}

				// Fidelity
				saveData.setSumInsured(StringUtils.isBlank(data.getSumInsured()) ? new BigDecimal(0)
						: new BigDecimal(data.getSumInsured()));

				saveData.setIndemnityType(
						StringUtils.isBlank(data.getIndemnityType()) ? null : data.getIndemnityType());
				saveData.setIndemnityTypeDesc(
						StringUtils.isBlank(data.getIndemnityTypeDesc()) ? null : data.getIndemnityTypeDesc());

				saveData.setAge(data.getAge());
				saveData.setCoverId(data.getCoverId());
				saveData.setBusinessSource(brokerReq.getBusinessSource());
				saveData.setBusinessSourcId(brokerReq.getBusinessSourceId());
				if (StringUtils.isNotBlank(data.getIndustryId())) {
					//
//											saveData.setIndustryId(StringUtils.isBlank(data.getIndustryId()) ? null
//													: Integer.valueOf(data.getIndustryId()));
					//
//											saveData.setIndustryDesc(industry != null ? industry.getIndustryName() : "");
//											saveData.setCategoryId(industry != null ? industry.getCategoryId() : "");
//											saveData.setCategoryDesc(industry != null ? industry.getCategoryDesc() : "");
					data.setIndustryType(data.getIndustryId());
				} else if (StringUtils.isNotBlank(data.getIndustryType())) {

					saveData.setIndustryId(
							StringUtils.isBlank(data.getIndustryType()) ? null : data.getIndustryType());
					List<IndustryMaster> industrydelList = industryrepo
							.findByIndustryIdAndCompanyIdAndProductIdAndStatusOrderByAmendIdDesc(
									Integer.valueOf(data.getIndustryType()), policyReq.getCompanyId(),
									policyReq.getProductId(), "Y");
					IndustryMaster industrydel = null;
					if (industrydelList != null && !industrydelList.isEmpty()) {
						industrydel = industrydelList.get(0);
					}
					if (industrydel == null) {
						industrydelList = industryrepo.findByIndustryIdAndCompanyIdAndProductIdAndStatusOrderByAmendIdDesc(
								Integer.valueOf(data.getIndustryType()), policyReq.getCompanyId(), "99999", "Y");
						if (industrydelList != null && !industrydelList.isEmpty()) {
							industrydel = industrydelList.get(0);
						}
					}
					if (industrydel != null) {
						saveData.setIndustryName(relationTypeDesc);
					}

				//	saveData.setCategoryId(industrydel != null ? industrydel.getCategoryId() : "");
					//saveData.setCategoryDesc(industrydel != null ? industrydel.getCategoryDesc() : "");

				}
				if(!StringUtils.isBlank(data.getIndustrytypedesc())){
				saveData.setIndustryName(StringUtils.isBlank(data.getIndustrytypedesc()) ? "" : data.getIndustrytypedesc());
				}
				if (StringUtils.isNotBlank(data.getCount())) {
					saveData.setCount(StringUtils.isBlank(data.getCount()) ? null : Integer.valueOf(data.getCount()));
				}
				// eserCommonRepo.saveAndFlush(saveData);

			} catch (Exception e) {
				e.printStackTrace();
				log.info("Exception Is ---> " + e.getMessage());
				return null;
			}

			return saveData;
		}


		@Override
		public NonMotorSaveRes getNonMotorDetails(NonMotorComRes req) {

			List<EserviceSectionDetails> sectiondatadetails = null;
			List<NonMotorLocRes> locationList = new ArrayList<NonMotorLocRes>();

			NonMotorSaveRes result = new NonMotorSaveRes();
			try {
				String address=""; 
				// Check the Request Reference is present in table or not
				sectiondatadetails = secRepo.findByRequestReferenceNo(req.getRequestReferenceNo());
				if (sectiondatadetails != null && !sectiondatadetails.isEmpty()) {
					// set Common details
					result = NonMotorCommonInfoMapping(req.getRequestReferenceNo());

					// Get Location Id and LocationName
					if (result != null) {
						Set<Integer> findlocationid = sectiondatadetails.stream().map(EserviceSectionDetails::getLocationId)
								.distinct().collect(Collectors.toSet());
						for (Integer data : findlocationid) {
							List<EserviceSectionDetails> secFilter = sectiondatadetails.stream()
									.filter(o -> o.getLocationId().equals(data))
									.collect(Collectors.toList());
							NonMotorLocRes dd = new NonMotorLocRes();
							dd.setLocationId(data.toString());
							dd.setLocationName(secFilter.get(0).getLocationName());
							List<NonMotorSectionRes> seclist =new ArrayList<NonMotorSectionRes>();
							 for(EserviceSectionDetails s:secFilter) {
								 System.out.println("Printing Section Id "+s.getSectionId());
								 System.out.println("Printing Section Name "+s.getSectionName());
								 System.out.println("Printing Product Type Id "+s.getProductType());
								 System.out.println("Printing Product Type Desc "+s.getProductDesc());
								 if("A".equalsIgnoreCase(s.getProductType())) {
										List<EserviceBuildingDetails> buildingdata =buildingRepo
												.findByRequestReferenceNoAndLocationId(req.getRequestReferenceNo(),data);
										List<EserviceBuildingDetails> building=buildingdata.stream()
												.filter(o -> o.getLocationId().equals(data) && o.getSectionId().equals(s.getSectionId())
														&& o.getRiskId().equals(s.getRiskId()))
												.collect(Collectors.toList());
										
										DozerBeanMapper dozerMapper = new DozerBeanMapper();
										for (EserviceBuildingDetails bd : building) {
											address=StringUtils.isBlank(building.get(0).getAddress())?"":building.get(0).getAddress();
											NonMotorSectionRes asset1 = dozerMapper.map(bd, NonMotorSectionRes.class);
											
											String sumInsuredString = bd.getSumInsured() != null 
												    ? bd.getSumInsured().setScale(0, BigDecimal.ROUND_DOWN).toPlainString() 
												    : "0";
											asset1.setSumInsured(sumInsuredString!=null?sumInsuredString:"0");
											asset1.setIndustrytype(asset1.getIndustryId()!=null && asset1.getIndustryId()>0  ?asset1.getIndustryId().toString():"0");
											asset1.setCoverid(bd.getCoverId());
											asset1.setSectionDesc(StringUtils.isBlank(bd.getSectionDesc())?null:bd.getSectionDesc());										
											asset1.setOccupationId(bd.getOccupationType());
											asset1.setOccupationDesc(bd.getOccupationTypeDesc());
											asset1.setCollateralName(bd.getCollateralName());
											asset1.setCollateralYn(bd.getCollateralYn());
											asset1.setBorrowerType(bd.getBorrowerType());
											asset1.setBorrowerTypeDesc(bd.getBorrowerTypeDesc());
											
											seclist.add(asset1);
										}
									
								 }else  if("H".equalsIgnoreCase(s.getProductType())) {
									 List<EserviceCommonDetails> comdata = humanRepo.findByRequestReferenceNoAndLocationId(req.getRequestReferenceNo(),data);
									 List<EserviceCommonDetails> common=comdata.stream()
												.filter(o -> o.getLocationId().equals(data) && o.getSectionId().equals(s.getSectionId())
														&& o.getRiskId().equals(s.getRiskId()))
												.collect(Collectors.toList());
										DozerBeanMapper dozerMapper = new DozerBeanMapper();
										for (EserviceCommonDetails cd : common) {
											address=StringUtils.isBlank(common.get(0).getAddress())?"":common.get(0).getAddress();

											NonMotorSectionRes commonres = dozerMapper.map(cd, NonMotorSectionRes.class);
											commonres.setOccupationId(StringUtils.isBlank(cd.getOccupationType())?null:cd.getOccupationType());
											commonres.setIndustrytype(commonres.getIndustryId()!=null && commonres.getIndustryId()>0  ?commonres.getIndustryId().toString():"0");

											commonres.setSumInsured(cd.getSumInsured()!=null?cd.getSumInsured().toPlainString():"0");
											commonres.setCoverid(cd.getCoverId());
											commonres.setSectionDesc(StringUtils.isBlank(cd.getSectionName())?null:cd.getSectionName());
											seclist.add(commonres);
										}
									 
								 }
								 
								 dd.setSectionList(seclist);
								 	 
							 }
							
							locationList.add(dd);
							dd.setAddress(address);
						}
						result.setLocationList(locationList);

					}
				} else {
					return null;
				}

			} catch (Exception SS) {
				System.out.println("***********The Exception Occured in GetMotorDetails  Api ***********");
				SS.printStackTrace();
				return null;
			}

			return result;
		}

		public NonMotorSaveRes NonMotorCommonInfoMapping(String Req_no) {
			NonMotorSaveRes result = new NonMotorSaveRes();
			DozerBeanMapper dozerMapper = new DozerBeanMapper();
			List<EserviceBuildingDetails> asset = null;
			List<EserviceCommonDetails> humman = null;
			NonMotorPolicyRes policyRes = new NonMotorPolicyRes();
			NonMotorBrokerRes brokerRes = new NonMotorBrokerRes();
			NonMotEndtRes endtRes = new NonMotEndtRes();
			try {
				asset = buildingRepo.findByRequestReferenceNo(Req_no);
				humman = humanRepo.findByRequestReferenceNo(Req_no);
				if (humman != null && !humman.isEmpty()) {
					policyRes = dozerMapper.map(humman, NonMotorPolicyRes.class);

					policyRes.setRequestReferenceNo(humman.get(0).getRequestReferenceNo());
					policyRes.setCustomerReferenceNo(humman.get(0).getCustomerReferenceNo());
					policyRes.setProductId(humman.get(0).getProductId());
					policyRes.setCompanyId(humman.get(0).getCompanyId());
					policyRes.setBranchCode(humman.get(0).getBranchCode());
					policyRes.setCreatedBy(humman.get(0).getCreatedBy());
					policyRes.setAcExecutiveId(humman.get(0).getAcExecutiveId());
					brokerRes.setApplicationId(humman.get(0).getApplicationId());
					brokerRes.setBrokerCode(humman.get(0).getBrokerCode());
					brokerRes.setSubUserType(humman.get(0).getSubUserType());
					brokerRes.setLoginId(humman.get(0).getLoginId());
					brokerRes.setAgencyCode(humman.get(0).getAgencyCode());
					// brokeRes.setUserType(asset.get(0).getuser);
					brokerRes.setBankCode(humman.get(0).getBankCode());
//					policyRes.setPolicyStartDate(humman.get(0).getPolicyStartDate());
//					policyRes.setPolicyEndDate(humman.get(0).getPolicyEndDate());
					policyRes.setCurrency(humman.get(0).getCurrency());
					policyRes.setExchangeRate(humman.get(0).getExchangeRate().toString());
					brokerRes.setBrokerBranchCode(humman.get(0).getBrokerBranchCode());
					policyRes.setHavepromocode(humman.get(0).getHavepromocode());
					policyRes.setPromocode(humman.get(0).getPromocode());
					brokerRes.setSourceTypeId(humman.get(0).getSourceTypeId());
					brokerRes.setCustomerCode(humman.get(0).getCustomerCode());
					brokerRes.setBdmCode(humman.get(0).getBdmCode());
					// policyRes.setCommissionType(humman.get(0).getcom);
					policyRes.setIndustryId(StringUtils.isBlank(humman.get(0).getIndustryId()) ? ""
							: humman.get(0).getIndustryId().toString());
					policyRes.setIndustryDesc(humman.get(0).getIndustryName());
					endtRes.setEndorsementDate(humman.get(0).getEndorsementDate());
					endtRes.setEndorsementEffdate(humman.get(0).getEndorsementEffdate());
					endtRes.setEndorsementRemarks(humman.get(0).getEndorsementRemarks());
					endtRes.setOriginalPolicyNo(humman.get(0).getOriginalPolicyNo());
					endtRes.setEndtPrevPolicyNo(humman.get(0).getEndtPrevPolicyNo());
					endtRes.setEndtPrevQuoteNo(humman.get(0).getEndtPrevQuoteNo());
					endtRes.setEndtCount(humman.get(0).getEndtCount());
					endtRes.setEndtStatus(humman.get(0).getEndtStatus());
					endtRes.setIsFinaceYn(humman.get(0).getIsFinyn());
					endtRes.setEndtCategDesc(humman.get(0).getEndtCategDesc());
					endtRes.setEndorsementType(humman.get(0).getEndorsementType());
					endtRes.setEndorsementTypeDesc(humman.get(0).getEndorsementTypeDesc());
					policyRes.setPolicyNo(humman.get(0).getPolicyNo());
					policyRes.setTiraCoverNoteNo(humman.get(0).getTiraCoverNoteNo());
					policyRes.setCustomerName(humman.get(0).getCustomerName());
					policyRes.setStatus(humman.get(0).getStatus());		
					result.setNonMotorPolicyRes(policyRes);
					result.setNonMotorBrokerRes(brokerRes);
					result.setNonMotEndtRes(endtRes);
				} else {
					policyRes = dozerMapper.map(asset, NonMotorPolicyRes.class);
					policyRes.setRequestReferenceNo(asset.get(0).getRequestReferenceNo());
					policyRes.setCustomerReferenceNo(asset.get(0).getCustomerReferenceNo());
					policyRes.setProductId(asset.get(0).getProductId());
					policyRes.setCompanyId(asset.get(0).getCompanyId());
					policyRes.setBranchCode(asset.get(0).getBranchCode());
					policyRes.setCreatedBy(asset.get(0).getCreatedBy());
					policyRes.setAcExecutiveId(String.valueOf(asset.get(0).getAcExecutiveId()));
					brokerRes.setApplicationId(asset.get(0).getApplicationId());
					brokerRes.setBrokerCode(asset.get(0).getBrokerCode());
					brokerRes.setSubUserType(asset.get(0).getSubUserType());
					brokerRes.setLoginId(asset.get(0).getLoginId());
					brokerRes.setAgencyCode(asset.get(0).getAgencyCode());
					// brokerRes.setUserType(asset.get(0).getuser);
					brokerRes.setBankCode(asset.get(0).getBankCode());
//					policyRes.setPolicyStartDate(asset.get(0).getPolicyStartDate());
//					policyRes.setPolicyEndDate(asset.get(0).getPolicyEndDate());
					policyRes.setCurrency(asset.get(0).getCurrency());
					policyRes.setExchangeRate(asset.get(0).getExchangeRate().toString());
					brokerRes.setBrokerBranchCode(asset.get(0).getBrokerBranchCode());
					policyRes.setHavepromocode(asset.get(0).getHavepromocode());
					policyRes.setPromocode(asset.get(0).getPromocode());
					brokerRes.setSourceType(asset.get(0).getSourceType());
					brokerRes.setSourceTypeId(asset.get(0).getSourceTypeId());
					brokerRes.setCustomerCode(asset.get(0).getCustomerCode());
					brokerRes.setBdmCode(asset.get(0).getBdmCode());
					brokerRes.setCommissionType(asset.get(0).getCommissionType());
					policyRes.setIndustryId(asset.get(0).getIndustryId().toString());
					policyRes.setIndustryDesc(asset.get(0).getIndustryDesc());
					endtRes.setEndorsementDate(asset.get(0).getEndorsementDate());
					endtRes.setEndorsementEffdate(asset.get(0).getEndorsementEffdate());
					endtRes.setEndorsementRemarks(asset.get(0).getEndorsementRemarks());
					endtRes.setOriginalPolicyNo(asset.get(0).getOriginalPolicyNo());
					endtRes.setEndtPrevPolicyNo(asset.get(0).getEndtPrevPolicyNo());
					endtRes.setEndtPrevQuoteNo(asset.get(0).getEndtPrevQuoteNo());
					endtRes.setEndtCount(asset.get(0).getEndtCount());
					endtRes.setEndtStatus(asset.get(0).getEndtStatus());
					endtRes.setIsFinaceYn(asset.get(0).getIsFinyn());
					endtRes.setEndtCategDesc(asset.get(0).getEndtCategDesc());
					endtRes.setEndorsementType(asset.get(0).getEndorsementType());
					endtRes.setEndorsementTypeDesc(asset.get(0).getEndorsementTypeDesc());
					endtRes.setPolicyNo(asset.get(0).getPolicyNo());
					policyRes.setTiraCoverNoteNo(asset.get(0).getTiraCoverNoteNo());
					policyRes.setCustomerName(asset.get(0).getCustomerName());
					policyRes.setStatus(asset.get(0).getStatus());	
					result.setNonMotorPolicyRes(policyRes);
					result.setNonMotorBrokerRes(brokerRes);
					result.setNonMotEndtRes(endtRes);
				}

			} catch (Exception cc) {
				System.out.println("***********The Exception Occured in mapping Common Details   Api ***********");
				cc.printStackTrace();
				return null;
			}
			return result;
		}

		public List<NonMotorSectionRes> getAssetList(String requestref, Integer LocationId) {
			List<NonMotorSectionRes> result = new ArrayList<>();
			try {
				List<EserviceBuildingDetails> data = buildingRepo.findByRequestReferenceNoAndLocationId(requestref,
						LocationId);

				DozerBeanMapper dozerMapper = new DozerBeanMapper();
				for (EserviceBuildingDetails dd : data) {
					EserviceBuildingDetails Assest = buildingRepo
							.findByRequestReferenceNoAndRiskIdAndSectionIdAndLocationId(dd.getRequestReferenceNo(),
									dd.getRiskId(), dd.getSectionId(), dd.getLocationId());
					NonMotorSectionRes asset1 = dozerMapper.map(Assest, NonMotorSectionRes.class);
					result.add(asset1);
				}
			} catch (Exception dd) {
				System.out.println("***********The exception occured in get list of assest details***********");
				dd.printStackTrace();
			}
			return result;
		}

		public List<NonMotorSectionRes> getHumanList(String requestref, Integer LocationId) {
			List<NonMotorSectionRes> result = new ArrayList<NonMotorSectionRes>();
			try {

				List<EserviceCommonDetails> data = humanRepo.findByRequestReferenceNoAndLocationId(requestref, LocationId);

				DozerBeanMapper dozerMapper = new DozerBeanMapper();
				for (EserviceCommonDetails dd : data) {
					EserviceCommonDetails Assest = humanRepo.findByRequestReferenceNoAndRiskIdAndSectionIdAndLocationId(
							dd.getRequestReferenceNo(), dd.getRiskId(), dd.getSectionId(), dd.getLocationId());
					NonMotorSectionRes asset1 = dozerMapper.map(Assest, NonMotorSectionRes.class);
					result.add(asset1);
				}
			} catch (Exception dd) {
				System.out.println("***********The exception occured in get list of human details***********");
				dd.printStackTrace();
			}
			return result;
		}
		@Override
		public NonMotorRes getAllNonMotorDetails(NonMotorComRes req) {

			List<EserviceSectionDetails> sectiondatadetails = null;
			List<NonMotorLocationRes> locationList = new ArrayList<>();

			NonMotorRes result = new NonMotorRes();
			try {
				// Check the Request Reference is present in table or not
				sectiondatadetails = secRepo.findByRequestReferenceNo(req.getRequestReferenceNo());
				if (sectiondatadetails != null && !sectiondatadetails.isEmpty()) {
					// set Common details
					result = NonMotorCommonDetailsMapping(req.getRequestReferenceNo());

					// Get Location Id and LocationName
					if (result != null) {
						Set<Integer> findlocationid = sectiondatadetails.stream().map(EserviceSectionDetails::getLocationId)
								.distinct().collect(Collectors.toSet());
						for (Integer data : findlocationid) {
							NonMotorLocationRes dd = new NonMotorLocationRes();
							List<EserviceSectionDetails> section = secRepo
									.findByRequestReferenceNoAndLocationId(req.getRequestReferenceNo(), data);
							dd.setLocationId(data);

							dd.setLocationName(section.get(0).getLocationName());
							// Mapping the Assest Details
							List<NonMotorAssestRes> asset = getAsset(req.getRequestReferenceNo(), data);
							List<NonMotorHumanRes> human = getHuman(req.getRequestReferenceNo(), data);
							if (!asset.isEmpty() && asset != null) {
								dd.setAssest(asset);
							}
							if (!human.isEmpty() && human != null) {
								dd.setHuman(human);
							}
							locationList.add(dd);
						}
						result.setLocationList(locationList);

					}
				} else {
					return null;
				}

			} catch (Exception SS) {
				System.out.println("***********The Exception Occured in GetMotorDetails  Api ***********");
				SS.printStackTrace();
				return null;
			}

			return result;
		}

		public NonMotorRes NonMotorCommonDetailsMapping(String Req_no) {
			NonMotorRes result = new NonMotorRes();
			DozerBeanMapper dozerMapper = new DozerBeanMapper();
			List<EserviceBuildingDetails> asset = null;
			List<EserviceCommonDetails> humman = null;
			try {
				asset = buildingRepo.findByRequestReferenceNo(Req_no);
				humman = humanRepo.findByRequestReferenceNo(Req_no);
				if (humman != null && !humman.isEmpty()) {
					result = dozerMapper.map(humman, NonMotorRes.class);
					result.setRequestReferenceNo(humman.get(0).getRequestReferenceNo());
					result.setCustomerReferenceNo(humman.get(0).getCustomerReferenceNo());
					result.setProductId(humman.get(0).getProductId());
					result.setCompanyId(humman.get(0).getCompanyId());
					result.setBranchCode(humman.get(0).getBranchCode());
					result.setCreatedBy(humman.get(0).getCreatedBy());
					result.setAcExecutiveId(humman.get(0).getAcExecutiveId());
					result.setApplicationId(humman.get(0).getApplicationId());
					result.setBrokerCode(humman.get(0).getBrokerCode());
					result.setSubUserType(humman.get(0).getSubUserType());
					result.setLoginId(humman.get(0).getLoginId());
					result.setAgencyCode(humman.get(0).getAgencyCode());
					// result.setUserType(asset.get(0).getuser);
					result.setBankCode(humman.get(0).getBankCode());
					result.setPolicyStartDate(humman.get(0).getPolicyStartDate());
					result.setPolicyEndDate(humman.get(0).getPolicyEndDate());
					result.setCurrency(humman.get(0).getCurrency());
					result.setExchangeRate(humman.get(0).getExchangeRate());
					result.setBrokerBranchCode(humman.get(0).getBrokerBranchCode());
					result.setHavepromocode(humman.get(0).getHavepromocode());
					result.setPromocode(humman.get(0).getPromocode());
					result.setSourceType(humman.get(0).getSourceType());
					result.setSourceTypeId(humman.get(0).getSourceTypeId());
					result.setCustomerCode(humman.get(0).getCustomerCode());
					result.setBdmCode(humman.get(0).getBdmCode());
					// result.setCommissionType(humman.get(0).getcom);
					result.setIndustryId(StringUtils.isBlank(humman.get(0).getIndustryId()) ? 0
							: Integer.valueOf(humman.get(0).getIndustryId()));
					result.setIndustryDesc(humman.get(0).getIndustryName());
					result.setEndorsementDate(humman.get(0).getEndorsementDate());
					result.setEndorsementEffdate(humman.get(0).getEndorsementEffdate());
					result.setEndorsementRemarks(humman.get(0).getEndorsementRemarks());
					result.setOriginalPolicyNo(humman.get(0).getOriginalPolicyNo());
					result.setEndtPrevPolicyNo(humman.get(0).getEndtPrevPolicyNo());
					result.setEndtPrevQuoteNo(humman.get(0).getEndtPrevQuoteNo());
					result.setEndtCount(humman.get(0).getEndtCount());
					result.setEndtStatus(humman.get(0).getEndtStatus());
					result.setIsFinaceYn(humman.get(0).getIsFinyn());
					result.setEndtCategDesc(humman.get(0).getEndtCategDesc());
					result.setEndorsementType(humman.get(0).getEndorsementType());
					result.setEndorsementTypeDesc(humman.get(0).getEndorsementTypeDesc());
					result.setPolicyNo(humman.get(0).getPolicyNo());
					result.setTiraCoverNoteNo(humman.get(0).getTiraCoverNoteNo());
					result.setCustomerName(humman.get(0).getCustomerName());
				} else {
					result = dozerMapper.map(asset, NonMotorRes.class);
					result.setRequestReferenceNo(asset.get(0).getRequestReferenceNo());
					result.setCustomerReferenceNo(asset.get(0).getCustomerReferenceNo());
					result.setProductId(asset.get(0).getProductId());
					result.setCompanyId(asset.get(0).getCompanyId());
					result.setBranchCode(asset.get(0).getBranchCode());
					result.setCreatedBy(asset.get(0).getCreatedBy());
					result.setAcExecutiveId(String.valueOf(asset.get(0).getAcExecutiveId()));
					result.setApplicationId(asset.get(0).getApplicationId());
					result.setBrokerCode(asset.get(0).getBrokerCode());
					result.setSubUserType(asset.get(0).getSubUserType());
					result.setLoginId(asset.get(0).getLoginId());
					result.setAgencyCode(asset.get(0).getAgencyCode());
					// result.setUserType(asset.get(0).getuser);
					result.setBankCode(asset.get(0).getBankCode());
					result.setPolicyStartDate(asset.get(0).getPolicyStartDate());
					result.setPolicyEndDate(asset.get(0).getPolicyEndDate());
					result.setCurrency(asset.get(0).getCurrency());
					result.setExchangeRate(asset.get(0).getExchangeRate());
					result.setBrokerBranchCode(asset.get(0).getBrokerBranchCode());
					result.setHavepromocode(asset.get(0).getHavepromocode());
					result.setPromocode(asset.get(0).getPromocode());
					result.setSourceType(asset.get(0).getSourceType());
					result.setSourceTypeId(asset.get(0).getSourceTypeId());
					result.setCustomerCode(asset.get(0).getCustomerCode());
					result.setBdmCode(asset.get(0).getBdmCode());
					result.setCommissionType(asset.get(0).getCommissionType());
					result.setIndustryId(asset.get(0).getIndustryId());
					result.setIndustryDesc(asset.get(0).getIndustryDesc());
					result.setEndorsementDate(asset.get(0).getEndorsementDate());
					result.setEndorsementEffdate(asset.get(0).getEndorsementEffdate());
					result.setEndorsementRemarks(asset.get(0).getEndorsementRemarks());
					result.setOriginalPolicyNo(asset.get(0).getOriginalPolicyNo());
					result.setEndtPrevPolicyNo(asset.get(0).getEndtPrevPolicyNo());
					result.setEndtPrevQuoteNo(asset.get(0).getEndtPrevQuoteNo());
					result.setEndtCount(asset.get(0).getEndtCount());
					result.setEndtStatus(asset.get(0).getEndtStatus());
					result.setIsFinaceYn(asset.get(0).getIsFinyn());
					result.setEndtCategDesc(asset.get(0).getEndtCategDesc());
					result.setEndorsementType(asset.get(0).getEndorsementType());
					result.setEndorsementTypeDesc(asset.get(0).getEndorsementTypeDesc());
					result.setPolicyNo(asset.get(0).getPolicyNo());
					result.setTiraCoverNoteNo(asset.get(0).getTiraCoverNoteNo());
					result.setCustomerName(asset.get(0).getCustomerName());
				}

			} catch (Exception cc) {
				System.out.println("***********The Exception Occured in mapping Common Details   Api ***********");
				cc.printStackTrace();
				return null;
			}
			return result;
		}

		public List<NonMotorAssestRes> getAsset(String requestref, Integer LocationId) {
			List<NonMotorAssestRes> result = new ArrayList<>();
			try {
				List<EserviceBuildingDetails> data = buildingRepo.findByRequestReferenceNoAndLocationId(requestref,
						LocationId);

				DozerBeanMapper dozerMapper = new DozerBeanMapper();
				for (EserviceBuildingDetails dd : data) {
					EserviceBuildingDetails Assest = buildingRepo
							.findByRequestReferenceNoAndRiskIdAndSectionIdAndLocationId(dd.getRequestReferenceNo(),
									dd.getRiskId(), dd.getSectionId(), dd.getLocationId());
					NonMotorAssestRes asset1 = dozerMapper.map(Assest, NonMotorAssestRes.class);
					result.add(asset1);
				}
			} catch (Exception dd) {
				System.out.println("***********The exception occured in get list of assest details***********");
				dd.printStackTrace();
			}
			return result;
		}

		public List<NonMotorHumanRes> getHuman(String requestref, Integer LocationId) {
			List<NonMotorHumanRes> result = new ArrayList<>();
			try {

				List<EserviceCommonDetails> data = humanRepo.findByRequestReferenceNoAndLocationId(requestref, LocationId);

				DozerBeanMapper dozerMapper = new DozerBeanMapper();
				for (EserviceCommonDetails dd : data) {
					EserviceCommonDetails Assest = humanRepo.findByRequestReferenceNoAndRiskIdAndSectionIdAndLocationId(
							dd.getRequestReferenceNo(), dd.getRiskId(), dd.getSectionId(), dd.getLocationId());
					NonMotorHumanRes asset1 = dozerMapper.map(Assest, NonMotorHumanRes.class);
					result.add(asset1);
				}
			} catch (Exception dd) {
				System.out.println("***********The exception occured in get list of human details***********");
				dd.printStackTrace();
			}
			return result;
		}
	@Transactional
		@Override
		public SuccessRes SaveFirstLossPayee(List<FirstLossPayeeReq> req) {
			SuccessRes res=new SuccessRes();
			DozerBeanMapper dozerMapper = new DozerBeanMapper();
			try {
				
				System.out.println("***********Save First Loss Payee***********");
				System.out.println("***********Req :"+req);
				List<FirstLossPayee> list=firstLossRepo.findByRequestReferenceNo(req.get(0).getRequestReferenceNo());
				if(list.size()>0) {
					firstLossRepo.deleteAll(list);
				}

		            for(FirstLossPayeeReq r:req) {
					FirstLossPayee save=new FirstLossPayee();
					save  = dozerMapper.map(r, FirstLossPayee.class);
					save.setStatus("Y");
					save.setEntryDate(new Date());
					save.setLocationId(Integer.valueOf(r.getLocationId()));
//					firstLossId+=1;
					save.setFirstLossPayeeId(Integer.valueOf(r.getFirstLossPayeeId()));
					firstLossRepo.save(save);
					}
				res.setSuccessId("");
				res.setResponse("Saved Successfully");
				
			}catch(Exception e) {
				System.out.println("***********The exception occured in save details***********");
				e.getStackTrace();
				}
			
			return res;
		}
	@Override
	public List<FirstLossPayeeRes> getFirstLossPayee(FirstLossPayeeReq req) {
		List<FirstLossPayeeRes> resList=new ArrayList<FirstLossPayeeRes>();
		DozerBeanMapper dozerMapper = new DozerBeanMapper();
		try {
			
			System.out.println("***********Get First Loss Payee***********");
			System.out.println("***********Req :"+req);
			List<FirstLossPayee> list=firstLossRepo.findByRequestReferenceNoAndLocationId(req.getRequestReferenceNo(),Integer.valueOf(req.getLocationId()));
			
			for(FirstLossPayee r:list) {
				FirstLossPayeeRes res=new FirstLossPayeeRes();
				res  = dozerMapper.map(r, FirstLossPayeeRes.class);
				res.setEntryDate(r.getEntryDate());
				res.setFirstLossPayeeId(r.getFirstLossPayeeId().toString());
				res.setLocationId(r.getLocationId().toString());
				res.setRiskId(r.getRiskId()==null?null :r.getRiskId().toString());
				resList.add(res);
				}
		}catch(Exception e) {
			System.out.println("***********The exception occured in save details***********");
			//e.getStackTrace();
			e.printStackTrace();
			}
		
		return resList;
	}

	@Override
	public List<SlideSectionSaveRes> nonMotorSaveDetails(NonMotorSaveReq req) {
		List<SlideSectionSaveRes> resList = new ArrayList<SlideSectionSaveRes>();
		List<BuildingSectionRes> sectionResList = new ArrayList<BuildingSectionRes>();
		NonMotorPolicyReq policyReq = req.getNonMotorPolicyReq();
		NonMotorBrokerReq brokerReq = req.getNonMotorBrokerReq();
		NonMotEndtReq endtReq = req.getNonMotEndtReq();
		try {
			CompanyProductMaster product = getCompanyProductMasterDropdown(policyReq.getCompanyId(),
					policyReq.getProductId());
			List<EserviceCommonDetails> findHumans = new ArrayList<EserviceCommonDetails>();
			List<EserviceBuildingDetails> findBuildings = new ArrayList<EserviceBuildingDetails>();
			List<EserviceSectionDetails> findsectionDetails = new ArrayList<EserviceSectionDetails>();
			String requestReferenceNo = "";
			if (StringUtils.isBlank(policyReq.getRequestReferenceNo())) {
				requestReferenceNo = generateRequestReferenceno(policyReq.getProductId(), policyReq.getCompanyId());
			} else {
				try {
					// Find Old
					requestReferenceNo = policyReq.getRequestReferenceNo();
					findHumans = humanRepo.findByRequestReferenceNoOrderByRiskIdAsc(requestReferenceNo);
					findBuildings = buildingRepo.findByRequestReferenceNo(requestReferenceNo);
					findsectionDetails = secRepo.findByRequestReferenceNo(requestReferenceNo);

					// Delete Old Records
					if (findHumans != null && findHumans.size() > 0) {
						humanRepo.deleteAll(findHumans);
					}
					if (findBuildings != null && findBuildings.size() > 0) {
						buildingRepo.deleteAll(findBuildings);
					}
					if (!findsectionDetails.isEmpty()) {
						secRepo.deleteAll(findsectionDetails);
					}
					System.out.println("Deleted Successfully");
				} catch (Exception e) {
					e.printStackTrace();
					log.info("Exception Is ---> " + e.getMessage());

				}
			}
			// Insert E-service Section Details

			List<NonMotorLocationReq> locationReq = req.getLocationList();
			System.out.println("********************Location Loop starts*******************");
			for (NonMotorLocationReq locationdata : locationReq) {
				Integer locId = Integer.valueOf(locationdata.getLocationId());
				String locationName = locationdata.getLocationName();
				String address = (StringUtils.isBlank(locationdata.getAddress())?null:locationdata.getAddress());
				String buildingOwnerYn=(StringUtils.isBlank(locationdata.getBuildingOwnerYn())?null:locationdata.getBuildingOwnerYn());
				List<NonMotorSectionReq> sectionReq = locationdata.getSectionList();
				Map<String, List<NonMotorSectionReq>> groupByGroupId = sectionReq.stream()
						.filter(o -> o.getSectionId() != null)
						.collect(Collectors.groupingBy(NonMotorSectionReq::getSectionId));
				List<String> findsectionId = sectionReq.stream().map(NonMotorSectionReq::getSectionId)
						.collect(Collectors.toList());
				System.out.println("Section Id "+groupByGroupId);
				for (String group : groupByGroupId.keySet()) {
					
//					List<NonMotorSectionReq> filterReq = groupByGroupId.get(group);
					List<NonMotorSectionReq> fiterData = sectionReq.stream()
							.filter(o -> o.getSectionId().equalsIgnoreCase(group.toString()))
							.collect(Collectors.toList());
					List<NonMotorSectionReq> fiterData1= generaterisk(fiterData);
					
					for (NonMotorSectionReq data : fiterData1) {
						List<Integer> findcoverId = sectionReq.stream().map(NonMotorSectionReq::getCoverId)
								.collect(Collectors.toList());
						
						String riskId = StringUtils.isBlank(data.getRiskId()) ? "0" : data.getRiskId();

						System.out.println("Total SectionId : " + findsectionId);
						System.out.println("LocationId : " + locId);
						System.out.println("Location Name : " + locationName);
						System.out.println("SectionId : " + data.getSectionId());
						System.out.println("SectionName : " + data.getSectionName());
						System.out.println("RiskId : " + riskId);
						System.out.println("Cover Id :"+data.getCoverId());
						System.out.println("OccupationId : " + data.getOccupationId());
						System.out.println("********************Section Insert*******************");
						try {
//							sectionResList = insertBuildingSectionNonMotor(req, data, riskId, locId, requestReferenceNo,
//									locationName,data.getCoverId());
						} catch (Exception e) {
							e.printStackTrace();
							log.info("Exception Is ---> " + e.getMessage());

						}
						EserviceBuildingDetails saveData = null;
						// Insert Raw Table
						// Asset
						if (StringUtils.isNotBlank(sectionResList.get(0).getMotorYn())
								&& sectionResList.get(0).getMotorYn().equalsIgnoreCase("A")) {
							System.out.println("********************Eservice Building Save*******************");
							try {
						//		saveData = insertAssetPolicyAndEndtDetailsAndBrokerDetails(req, requestReferenceNo,
						//				riskId, data, locId, locationName, sectionResList.get(0).getSectionName(),buildingOwnerYn,address);
							} catch (Exception e) {
								e.printStackTrace();
								log.info("Exception Is ---> " + e.getMessage());

							}
							// One Time table Save
							try {
								List<OneTimeTableRes> otResList = null;
								// Section Req
								List<BuildingSectionRes> sectionList = new ArrayList<BuildingSectionRes>();
								BuildingSectionRes sec = new BuildingSectionRes();
								sec.setMotorYn(sectionResList.get(0).getMotorYn());
								sec.setSectionId(sectionResList.get(0).getSectionId());
								sec.setSectionName(sectionResList.get(0).getSectionName());
								sec.setCoverId(sectionResList.get(0).getCoverId());
								sec.setRiskId(sectionResList.get(0).getRiskId());
								sectionList.add(sec);

								// One Time Table Thread Call
								OneTimeTableReq otReq = new OneTimeTableReq();
								otReq.setRequestReferenceNo(saveData.getRequestReferenceNo());
								otReq.setVehicleId(saveData.getRiskId());
								otReq.setLocationId(saveData.getLocationId());
								otReq.setBranchCode(saveData.getBranchCode());
								otReq.setInsuranceId(saveData.getCompanyId());
								otReq.setProductId(Integer.valueOf(saveData.getProductId()));
								otReq.setSectionList(sectionList);
								System.out.println("********************One Time table Save*******************");
								otResList = otService.call_OT_Insert(otReq);
								for (OneTimeTableRes otRes : otResList) {
									SlideSectionSaveRes res = new SlideSectionSaveRes();
									res.setResponse("Saved Successfully");
									res.setRiskId(otRes.getVehicleId());
									res.setLocationId(otRes.getLocationId());
									res.setVdRefNo(otRes.getVdRefNo());
									res.setCdRefNo(otRes.getCdRefNo());
									res.setMsrefno(otRes.getMsRefNo());
									res.setCompanyId(otRes.getCompanyId());
									res.setProductId(otRes.getProductId());
									res.setSectionId(otRes.getSectionId());
									res.setCustomerReferenceNo(saveData.getCustomerReferenceNo());
									res.setRequestReferenceNo(saveData.getRequestReferenceNo());
									res.setCreatedBy(policyReq.getCreatedBy());
									res.setCoverid(otRes.getCoverid());
									resList.add(res);
								}
							} catch (Exception e) {
								e.printStackTrace();
								log.info("Exception Is ---> " + e.getMessage());

							}

						} else {
							EserviceCommonDetails saveCommon = new EserviceCommonDetails();
							try {
						//		saveCommon = insertCommonPolicyAndEndtAndBrokerDetails(req, requestReferenceNo, riskId,
						//				data, locId, locationName, sectionResList.get(0).getSectionName(),address);
							} catch (Exception e) {
								e.printStackTrace();
								log.info("Exception Is ---> " + e.getMessage());

							}
							// One Time table Save
							try {
								List<OneTimeTableRes> otResList = null;
								// Section Req
								List<BuildingSectionRes> sectionList = new ArrayList<BuildingSectionRes>();
								BuildingSectionRes sec = new BuildingSectionRes();
								sec.setMotorYn(sectionResList.get(0).getMotorYn());
								sec.setSectionId(sectionResList.get(0).getSectionId());
								sec.setSectionName(sectionResList.get(0).getSectionName());
								sec.setRiskId(saveCommon.getRiskId());
								sec.setCoverId(sectionResList.get(0).getCoverId());

								sectionList.add(sec);

								// One Time Table Thread Call
								OneTimeTableReq otReq = new OneTimeTableReq();
								otReq.setRequestReferenceNo(saveCommon.getRequestReferenceNo());
								otReq.setVehicleId(saveCommon.getRiskId());
								otReq.setLocationId(saveCommon.getLocationId());
								otReq.setBranchCode(saveCommon.getBranchCode());
								otReq.setInsuranceId(saveCommon.getCompanyId());
								otReq.setProductId(Integer.valueOf(saveCommon.getProductId()));
								otReq.setSectionList(sectionList);
								System.out.println("********************One Time table Save*******************");
								otResList = otService.call_OT_Insert(otReq);
								for (OneTimeTableRes otRes : otResList) {
									SlideSectionSaveRes res = new SlideSectionSaveRes();
									res.setResponse("Saved Successfully");
									res.setRiskId(otRes.getVehicleId());
									res.setLocationId(otRes.getLocationId());
									res.setVdRefNo(otRes.getVdRefNo());
									res.setCdRefNo(otRes.getCdRefNo());
									res.setMsrefno(otRes.getMsRefNo());
									res.setCompanyId(otRes.getCompanyId());
									res.setProductId(otRes.getProductId());
									res.setSectionId(otRes.getSectionId());
									res.setCustomerReferenceNo(saveCommon.getCustomerReferenceNo());
									res.setRequestReferenceNo(saveCommon.getRequestReferenceNo());
									res.setCreatedBy(policyReq.getCreatedBy());
									resList.add(res);
								}
							} catch (Exception e) {
								e.printStackTrace();
								log.info("Exception Is ---> " + e.getMessage());

							}
						}
					}

				}
				System.out.println("********************Location Loop End*******************");
			}

		
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Log Details" + e.getMessage());
			return null;

		}

		return resList;
	}



}
