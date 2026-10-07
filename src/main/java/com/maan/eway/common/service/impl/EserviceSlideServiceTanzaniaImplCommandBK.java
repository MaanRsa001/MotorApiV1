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
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
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
import com.maan.eway.bean.MsHumanDetails;
import com.maan.eway.bean.OccupationMaster;
import com.maan.eway.bean.ProductSectionMaster;
import com.maan.eway.bean.RegionMaster;
import com.maan.eway.bean.SectionCoverMaster;
import com.maan.eway.bean.StateMaster;
import com.maan.eway.common.controller.EserviceSlideController;
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
import com.maan.eway.common.req.SectionSaveReq;
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
import com.maan.eway.common.service.BuildingDetailsService;
import com.maan.eway.common.service.DocumentCopyService;
import com.maan.eway.repository.BuildingDetailsRepository;
import com.maan.eway.repository.BuildingRiskDetailsRepository;
import com.maan.eway.repository.CommonDataDetailsRepository;
import com.maan.eway.repository.CompanyProductMasterRepository;
import com.maan.eway.repository.ContentAndRiskRepository;
import com.maan.eway.repository.DocumentTransactionDetailsRepository;
import com.maan.eway.repository.EServiceBuildingDetailsRepository;
import com.maan.eway.repository.EServiceMotorDetailsRepository;
import com.maan.eway.repository.EServiceSectionDetailsRepository;
import com.maan.eway.repository.EserviceCommonDetailsRepository;
import com.maan.eway.repository.EserviceCustomerDetailsRepository;
import com.maan.eway.repository.EserviceTravelDetailsRepository;
import com.maan.eway.repository.FactorRateRequestDetailsRepository;
import com.maan.eway.repository.FirstLossPayeeRepository;
import com.maan.eway.repository.HomePositionMasterRepository;
import com.maan.eway.repository.IndustryMasterRepository;
import com.maan.eway.repository.InsuranceCompanyMasterRepository;
import com.maan.eway.repository.ListItemValueRepository;
import com.maan.eway.repository.LoginBranchMasterRepository;
import com.maan.eway.repository.LoginMasterRepository;
import com.maan.eway.repository.LoginUserInfoRepository;
import com.maan.eway.repository.MsAssetDetailsRepository;
import com.maan.eway.repository.MsCommonDetailsRepository;
import com.maan.eway.repository.MsHumanDetailsRepository;
import com.maan.eway.repository.PolicyCoverDataIndividualsRepository;
import com.maan.eway.repository.PolicyCoverDataRepository;
import com.maan.eway.repository.ProductEmployeesDetailsRepository;
import com.maan.eway.repository.ProductGroupMasterRepository;
import com.maan.eway.repository.ProductSectionMasterRepository;
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

public class EserviceSlideServiceTanzaniaImplCommandBK implements EserviceSlideSaveServicePhoenix{
	

			@Autowired
			private FirstLossPayeeRepository firstLossRepo;
			@Autowired
			private ContentAndRiskRepository contentRepo;
			@Autowired
			private ProductSectionMasterRepository productmaster;

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
			private BuildingDetailsRepository repo1;

			@Autowired
			private BuildingDetailsService service;

			@Autowired
			private EserviceCommonDetailsRepository humanRepo;

			@Autowired
			private ProductGroupMasterRepository pgmrepo;

			@Autowired
			private LoginMasterRepository loginRepo;

			@PersistenceContext
			private EntityManager em;

			@Autowired
			private DocumentTransactionDetailsRepository docTransRepo;

			@Autowired
			private DocumentCopyService docService;

			@Autowired
			private OneTimeService otService;

			@Autowired
			private CompanyProductMasterRepository productRepo;

			@Autowired
			private ProductSectionMasterRepository sectionRepo;

			@Autowired
			private InsuranceCompanyMasterRepository companyRepo;

			@Autowired
			private EServiceSectionDetailsRepository eserSecRepo;

			@Autowired
			private LoginBranchMasterRepository lbranchRepo;

			@Autowired
			private ListItemValueRepository listrepo;

			@Autowired
			private EserviceCommonDetailsRepository eserCommonRepo;

			@Autowired
			private GenerateSeqNoServiceImpl genSeqNoService;

			@Autowired
			private EServiceBuildingDetailsRepository repository;

			@Autowired
			private TrackingDetailsServiceImpl trackService;

			@Autowired
			private PolicyCoverDataIndividualsRepository indiCoverRepo;

			@Autowired
			private FactorRateRequestDetailsRepository factorRepo;

			@Autowired
			private CommonDataDetailsRepository commonRepo;

			@Autowired
			private BuildingRiskDetailsRepository motBuildingRepo;

			@Autowired
			private LoginUserInfoRepository loginUserRepo;

			@Autowired
			private EServiceMotorDetailsRepository eserMotRepo;

			@Autowired
			private EserviceTravelDetailsRepository eserTraRepo;

			@Autowired
			private EserviceCustomerDetailsRepository eserCustRepo;

			@Autowired
			private PremiaBrokerServiceImpl premiaBrokerService;

			@Autowired
			private ProductEmployeesDetailsRepository productEmplRepo;

			@Autowired
			private PolicyCoverDataRepository coverRepo;

			@Autowired
			private HomePositionMasterRepository homeRepo;

			@Autowired
			private EserviceSlideController eserviceSlideController;
			
			@Autowired
			private EserviceSlideServicePhoenixZambia EserviceSlideServiceZambia;
			@Autowired
			private MsAssetDetailsRepository msAssetRepo;
			@Autowired
			private MsHumanDetailsRepository mshumanRepo;
			@Autowired
			private MsCommonDetailsRepository msCommon;
			
			@Autowired
			private CalcAsyncService calcAsyncService;
			 
			@Value(value="${calc.section}")
			private String calcSection;

			@Value(value = "${travel.productId}")
			private String travelProductId;

			private Logger log = LogManager.getLogger(EserviceSlideSaveServiceImpl.class);

			Gson json = new Gson();

			private OkHttpClient httpClient = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS)
					.connectTimeout(60, TimeUnit.SECONDS).build();
		/*

			@Override
			public CommonSlideSaveRes saveCommonDetails(SlideCommonSaveReq req) {
				CommonSlideSaveRes res = new CommonSlideSaveRes();
				List<BuildingSectionRes> sectionResList = new ArrayList<BuildingSectionRes>();
				try {
					CompanyProductMaster product = getCompanyProductMasterDropdown(req.getCompanyId(), req.getProductId()); // productRepo.findByProductIdOrderByAmendIdDesc(Integer.valueOf(req.getProductId()));

					// Find Old
					List<EserviceCommonDetails> findHumans = new ArrayList<EserviceCommonDetails>();
					List<EserviceBuildingDetails> findBuildings = new ArrayList<EserviceBuildingDetails>();

					if (StringUtils.isNotBlank(req.getRequestReferenceNo())) {
						findHumans = humanRepo.findByRequestReferenceNoOrderByRiskIdAsc(req.getRequestReferenceNo());
						findBuildings = buildingRepo.findByRequestReferenceNo(req.getRequestReferenceNo());
						// Change Of Currency Suminsured Change
						if (findHumans.size() > 0 || findBuildings.size() > 0) {
							String decimalDigits = currencyDecimalFormat(req.getCompanyId(), req.getCurrency()).toString();
							String stringFormat = "%0" + decimalDigits + "d";
							String decimalLength = decimalDigits.equals("0") ? "" : String.format(stringFormat, 0L);
							String pattern = StringUtils.isBlank(decimalLength) ? "#####0" : "#####0." + decimalLength;
							DecimalFormat df = new DecimalFormat(pattern);

							BigDecimal exchangeRate = StringUtils.isBlank(req.getExchangeRate()) ? BigDecimal.ONE
									: new BigDecimal(req.getExchangeRate());
							// Update Building
							updateBuildingSuminsured(findBuildings, exchangeRate, req.getCurrency(), df);

							// Update Human
							updateHumanSuminsured(findHumans, exchangeRate, req.getCurrency(), df);
						}
					}

					// Human Insert
					if (StringUtils.isNotBlank(product.getMotorYn()) && product.getMotorYn().equalsIgnoreCase("H")) {
						EserviceCommonDetails saveHumanData = new EserviceCommonDetails();

						// Set Ref No
						saveHumanData = setHumanRefNo(req);

						// Set Policy Details
						saveHumanData = setHumanPolicyAndEndtDetails(req, saveHumanData);

						// Save Broker Details
						saveHumanData = setHumanBrokerDetails(req, saveHumanData);

						// One Time Table Columns
						saveHumanData.setCdRefno(null);
						saveHumanData.setVdRefno(null);
						saveHumanData.setMsRefno(null);
						saveHumanData.setLocationId(1);

						// saveHumanData.setRiskId(null != saveHumanData && null !=
						// saveHumanData.getRiskId() ? saveHumanData.getRiskId() : 1);
						humanRepo.save(saveHumanData);

						// Save Section
						sectionResList = insertHumanSectionDetails(req, saveHumanData);

						// Response
						res.setCompanyId(req.getCompanyId());
						res.setCreatedBy(req.getCreatedBy());
						res.setCustomerReferenceNo(req.getCustomerReferenceNo());
						res.setProductId(req.getProductId());

						res.setRequestReferenceNo(saveHumanData.getRequestReferenceNo());
						res.setResponse(" Saved Successfully");
						res.setRiskId(saveHumanData.getRiskId().toString());
						req.getSectionIds().remove("0");
						res.setSectionIds(req.getSectionIds());

						// Asset Insert
					} else {
						EserviceBuildingDetails saveBuildingData = new EserviceBuildingDetails();

						// Set Ref No
						saveBuildingData = setBuildingRefNo(req);// reference no block

						// Set Policy Details
						saveBuildingData = setBuildingPolicyAndEndtDetails(req, saveBuildingData);

						// Save Broker Details
						saveBuildingData = setBuildingBrokerDetails(req, saveBuildingData);

						// Save Building List
						saveBuildingData = saveBuildingList(req, saveBuildingData);

						// Save Section
						sectionResList = insertBuildingSectionDetails(req, saveBuildingData);

						// Response
						res.setCompanyId(req.getCompanyId());
						res.setCreatedBy(req.getCreatedBy());
						res.setCustomerReferenceNo(req.getCustomerReferenceNo());
						res.setProductId(req.getProductId());

						res.setRequestReferenceNo(saveBuildingData.getRequestReferenceNo());
						res.setResponse(" Saved Successfully");
						res.setRiskId(saveBuildingData.getRiskId().toString());
						req.getSectionIds().remove("0");
						res.setSectionIds(req.getSectionIds());

					}

					// Delete Old Section Trases
					// deleteSectionTraces(req ,res );

				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());
					return null;

				}

				return res;
			}*/
		/*
			public CommonSlideSaveRes updateBuildingSuminsured(List<EserviceBuildingDetails> findBuildings,
					BigDecimal newExRate, String newCurr, DecimalFormat df) {
				CommonSlideSaveRes res = new CommonSlideSaveRes();
				try {
					if (findBuildings != null && findBuildings.size() > 0) {
						// exchangeRateScenario2(BigDecimal oldSi , BigDecimal newSi , BigDecimal
						// oldExRate ,BigDecimal newExRate , String oldCurr , String newCurr ,
						// DecimalFormat df)

						// Lc Decimal
						BigDecimal exchangeRate = newExRate != null ? newExRate : BigDecimal.ZERO;
						List<InsuranceCompanyMaster> companyDetails = getInscompanyMasterDetails(
								findBuildings.get(0).getCompanyId());
						// String currency = companyDetails.size() > 0 ?
						// companyDetails.get(0).getCurrencyId() : newCurr ;
//						String decimalDigits = currencyDecimalFormat(findBuildings.get(0).getCompanyId() , currency ).toString();
//						String stringFormat = "%0"+decimalDigits+"d" ;
//						String decimalLength = decimalDigits.equals("0") ?"" : String.format(stringFormat ,0L)  ;
//						String pattern = StringUtils.isBlank(decimalLength) ?  "#####0" :   "#####0." + decimalLength;
						String pattern = "#####0";
						DecimalFormat df2 = new DecimalFormat(pattern);

						for (EserviceBuildingDetails build : findBuildings) {
							// Asset Suminsured

							// exchangeRateScenario2(hum.getSumInsured() , hum.getExchangeRate() , newExRate
							// , hum.getCurrency() ,newCurr ,df )
							// Building
							build.setBuildingSuminsured(exchangeRateScenario2(build.getBuildingSuminsured(),
									build.getExchangeRate(), newExRate, build.getCurrency(), newCurr, df));
							build.setWaterTankSi(exchangeRateScenario2(build.getWaterTankSi(), build.getExchangeRate(),
									newExRate, build.getCurrency(), newCurr, df));
							build.setLossOfRentSi(exchangeRateScenario2(build.getLossOfRentSi(), build.getExchangeRate(),
									newExRate, build.getCurrency(), newCurr, df));
							build.setArchitectsSi(exchangeRateScenario2(build.getArchitectsSi(), build.getExchangeRate(),
									newExRate, build.getCurrency(), newCurr, df));

							// Content
							build.setContentSuminsured(exchangeRateScenario2(build.getContentSuminsured(),
									build.getExchangeRate(), newExRate, build.getCurrency(), newCurr, df));
							build.setEquipmentSi(exchangeRateScenario2(build.getEquipmentSi(), build.getExchangeRate(),
									newExRate, build.getCurrency(), newCurr, df));
							build.setJewellerySi(exchangeRateScenario2(build.getJewellerySi(), build.getExchangeRate(),
									newExRate, build.getCurrency(), newCurr, df));
							build.setPaitingsSi(exchangeRateScenario2(build.getPaitingsSi(), build.getExchangeRate(), newExRate,
									build.getCurrency(), newCurr, df));
							build.setCarpetsSi(exchangeRateScenario2(build.getCarpetsSi(), build.getExchangeRate(), newExRate,
									build.getCurrency(), newCurr, df));

							// All Risk , Plant All Risk , Business All Risk
							build.setAllriskSuminsured(exchangeRateScenario2(build.getAllriskSuminsured(),
									build.getExchangeRate(), newExRate, build.getCurrency(), newCurr, df));
							build.setMiningPlantSi(exchangeRateScenario2(build.getMiningPlantSi(), build.getExchangeRate(),
									newExRate, build.getCurrency(), newCurr, df));
							build.setNonminingPlantSi(exchangeRateScenario2(build.getNonminingPlantSi(),
									build.getExchangeRate(), newExRate, build.getCurrency(), newCurr, df));
							build.setGensetsSi(exchangeRateScenario2(build.getGensetsSi(), build.getExchangeRate(), newExRate,
									build.getCurrency(), newCurr, df));
							// build.setEquipmentSi(exchangeRateScenario2(build.getEquipmentSi() ,
							// build.getExchangeRate() , newExRate , build.getCurrency() ,newCurr ,df));

							// Burgalry
							build.setStockInTradeSi(exchangeRateScenario2(build.getStockInTradeSi(), build.getExchangeRate(),
									newExRate, build.getCurrency(), newCurr, df));
							;
							build.setGoodsSi(exchangeRateScenario2(build.getGoodsSi(), build.getExchangeRate(), newExRate,
									build.getCurrency(), newCurr, df));
							build.setFurnitureSi(exchangeRateScenario2(build.getFurnitureSi(), build.getExchangeRate(),
									newExRate, build.getCurrency(), newCurr, df));
							build.setCashValueablesSi(exchangeRateScenario2(build.getCashValueablesSi(),
									build.getExchangeRate(), newExRate, build.getCurrency(), newCurr, df));
							build.setApplianceSi(exchangeRateScenario2(build.getApplianceSi(), build.getExchangeRate(),
									newExRate, build.getCurrency(), newCurr, df));
							;

							// Fire And Material Damage
							build.setFirePlantSi(exchangeRateScenario2(build.getFirePlantSi(), build.getExchangeRate(),
									newExRate, build.getCurrency(), newCurr, df));

							// Electronic Equipment
							build.setElecEquipSuminsured(exchangeRateScenario2(build.getElecEquipSuminsured(),
									build.getExchangeRate(), newExRate, build.getCurrency(), newCurr, df));

							// Money
							build.setMoneyAnnualEstimate(exchangeRateScenario2(build.getMoneyAnnualEstimate(),
									build.getExchangeRate(), newExRate, build.getCurrency(), newCurr, df));
							build.setMoneyCollector(exchangeRateScenario2(build.getMoneyCollector(), build.getExchangeRate(),
									newExRate, build.getCurrency(), newCurr, df));
							build.setMoneyDirectorResidence(exchangeRateScenario2(build.getMoneyDirectorResidence(),
									build.getExchangeRate(), newExRate, build.getCurrency(), newCurr, df));
							build.setMoneyOutofSafe(exchangeRateScenario2(build.getMoneyOutofSafe(), build.getExchangeRate(),
									newExRate, build.getCurrency(), newCurr, df));
							build.setMoneySafeLimit(exchangeRateScenario2(build.getMoneySafeLimit(), build.getExchangeRate(),
									newExRate, build.getCurrency(), newCurr, df));
							build.setMoneyMajorLoss(exchangeRateScenario2(build.getMoneyMajorLoss(), build.getExchangeRate(),
									newExRate, build.getCurrency(), newCurr, df));

							// Machinery
							build.setElecMachinesSi(exchangeRateScenario2(build.getElecMachinesSi(), build.getExchangeRate(),
									newExRate, build.getCurrency(), newCurr, df));
							build.setBoilerPlantsSi(exchangeRateScenario2(build.getBoilerPlantsSi(), build.getExchangeRate(),
									newExRate, build.getCurrency(), newCurr, df));
							// build.setEquipmentSi(exchangeRateScenario2(build.getEquipmentSi() ,
							// build.getExchangeRate() , newExRate , build.getCurrency() ,newCurr ,df));
							build.setGeneralMachineSi(exchangeRateScenario2(build.getGeneralMachineSi(),
									build.getExchangeRate(), newExRate, build.getCurrency(), newCurr, df));
							build.setMachineEquipSi(exchangeRateScenario2(build.getMachineEquipSi(), build.getExchangeRate(),
									newExRate, build.getCurrency(), newCurr, df));
							build.setManuUnitsSi(exchangeRateScenario2(build.getManuUnitsSi(), build.getExchangeRate(),
									newExRate, build.getCurrency(), newCurr, df));
							build.setPowerPlantSi(exchangeRateScenario2(build.getPowerPlantSi(), build.getExchangeRate(),
									newExRate, build.getCurrency(), newCurr, df));

							// Lc Calc
							build.setBuildingSuminsuredLc(build.getBuildingSuminsured() == null ? null
									: new BigDecimal(df2.format(build.getBuildingSuminsured().multiply(exchangeRate))));
							build.setContentSuminsuredLc(build.getContentSuminsured() == null ? null
									: new BigDecimal(df2.format(build.getContentSuminsured().multiply(exchangeRate))));
							build.setEquipmentSiLc(build.getEquipmentSi() == null ? null
									: new BigDecimal(df2.format(build.getEquipmentSi().multiply(exchangeRate))));
							build.setJewellerySiLc(build.getJewellerySi() == null ? null
									: new BigDecimal(df2.format(build.getJewellerySi().multiply(exchangeRate))));
							build.setPaitingsSiLc(build.getPaitingsSi() == null ? null
									: new BigDecimal(df2.format(build.getPaitingsSi().multiply(exchangeRate))));
							build.setCarpetsSiLc(build.getCarpetsSi() == null ? null
									: new BigDecimal(df2.format(build.getCarpetsSi().multiply(exchangeRate))));
							build.setAllriskSuminsuredLc(build.getAllriskSuminsured() == null ? null
									: new BigDecimal(df2.format(build.getAllriskSuminsured().multiply(exchangeRate))));
							build.setMiningPlantSiLc(build.getMiningPlantSi() == null ? null
									: new BigDecimal(df2.format(build.getMiningPlantSi().multiply(exchangeRate))));
							build.setNonminingPlantSi(build.getNonminingPlantSi() == null ? null
									: new BigDecimal(df2.format(build.getNonminingPlantSi().multiply(exchangeRate))));
							build.setGensetsSiLc(build.getGensetsSi() == null ? null
									: new BigDecimal(df2.format(build.getGensetsSi().multiply(exchangeRate))));
							build.setEquipmentSiLc(build.getEquipmentSi() == null ? null
									: new BigDecimal(df2.format(build.getEquipmentSi().multiply(exchangeRate))));
							build.setStockInTradeSiLc(build.getStockInTradeSi() == null ? null
									: new BigDecimal(df2.format(build.getStockInTradeSi().multiply(exchangeRate))));
							build.setGoodsSiLc(build.getGoodsSi() == null ? null
									: new BigDecimal(df2.format(build.getGoodsSi().multiply(exchangeRate))));
							build.setFurnitureSiLc(build.getFurnitureSi() == null ? null
									: new BigDecimal(df2.format(build.getFurnitureSi().multiply(exchangeRate))));
							build.setCashValueablesSiLc(build.getCashValueablesSi() == null ? null
									: new BigDecimal(df2.format(build.getCashValueablesSi().multiply(exchangeRate))));
							build.setApplianceSiLc(build.getApplianceSi() == null ? null
									: new BigDecimal(df2.format(build.getApplianceSi().multiply(exchangeRate))));
							build.setStockInTradeSiLc(build.getStockInTradeSi() == null ? null
									: new BigDecimal(df2.format(build.getStockInTradeSi().multiply(exchangeRate))));
							build.setFirePlantSiLc(build.getFirePlantSi() == null ? null
									: new BigDecimal(df2.format(build.getFirePlantSi().multiply(exchangeRate))));
							build.setElecEquipSuminsuredLc(build.getElecEquipSuminsured() == null ? null
									: new BigDecimal(df2.format(build.getElecEquipSuminsured().multiply(exchangeRate))));
							build.setMoneyAnnualEstimateLc(build.getMoneyAnnualEstimate() == null ? null
									: new BigDecimal(df2.format(build.getMoneyAnnualEstimate().multiply(exchangeRate))));
							build.setMoneyCollectorLc(build.getMoneyCollector() == null ? null
									: new BigDecimal(df2.format(build.getMoneyCollector().multiply(exchangeRate))));
							build.setMoneyDirectorResidenceLc(build.getMoneyDirectorResidence() == null ? null
									: new BigDecimal(df2.format(build.getMoneyDirectorResidence().multiply(exchangeRate))));
							build.setMoneyOutofSafeLc(build.getMoneyOutofSafe() == null ? null
									: new BigDecimal(df2.format(build.getMoneyOutofSafe().multiply(exchangeRate))));
							build.setMoneySafeLimitLc(build.getMoneySafeLimit() == null ? null
									: new BigDecimal(df2.format(build.getMoneySafeLimit().multiply(exchangeRate))));
							build.setMoneyMajorLossLc(build.getMoneyMajorLoss() == null ? null
									: new BigDecimal(df2.format(build.getMoneyMajorLoss().multiply(exchangeRate))));
							build.setElecMachinesSiLc(build.getElecMachinesSi() == null ? null
									: new BigDecimal(df2.format(build.getElecMachinesSi().multiply(exchangeRate))));
							build.setBoilerPlantsSiLc(build.getBoilerPlantsSi() == null ? null
									: new BigDecimal(df2.format(build.getBoilerPlantsSi().multiply(exchangeRate))));
							build.setEquipmentSiLc(build.getEquipmentSi() == null ? null
									: new BigDecimal(df2.format(build.getEquipmentSi().multiply(exchangeRate))));
							build.setGeneralMachineSiLc(build.getGeneralMachineSi() == null ? null
									: new BigDecimal(df2.format(build.getGeneralMachineSi().multiply(exchangeRate))));
							build.setMachineEquipSiLc(build.getMachineEquipSi() == null ? null
									: new BigDecimal(df2.format(build.getMachineEquipSi().multiply(exchangeRate))));
							build.setManuUnitsSiLc(build.getManuUnitsSi() == null ? null
									: new BigDecimal(df2.format(build.getManuUnitsSi().multiply(exchangeRate))));
							build.setPowerPlantSiLc(build.getPowerPlantSi() == null ? null
									: new BigDecimal(df2.format(build.getPowerPlantSi().multiply(exchangeRate))));
							build.setWaterTankSiLc(build.getWaterTankSi() == null ? null
									: new BigDecimal(df2.format(build.getWaterTankSi().multiply(exchangeRate))));
							build.setLossOfRentSiLc(build.getLossOfRentSi() == null ? null
									: new BigDecimal(df2.format(build.getLossOfRentSi().multiply(exchangeRate))));
							build.setArchitectsSiLc(build.getArchitectsSi() == null ? null
									: new BigDecimal(df2.format(build.getArchitectsSi().multiply(exchangeRate))));

						}
						buildingRepo.saveAll(findBuildings);
					}

				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());
					return null;
				}
				return res;
			}*/
		/*
			public CommonSlideSaveRes updateHumanSuminsured(List<EserviceCommonDetails> findHumans, BigDecimal newExRate,
					String newCurr, DecimalFormat df) {
				CommonSlideSaveRes res = new CommonSlideSaveRes();
				try {
					if (findHumans != null && findHumans.size() > 0) {
						// exchangeRateScenario2(BigDecimal oldSi , BigDecimal newSi , BigDecimal
						// oldExRate ,BigDecimal newExRate , String oldCurr , String newCurr ,
						// DecimalFormat df)

						// Lc Decimal
						BigDecimal exchangeRate = newExRate != null ? newExRate : BigDecimal.ZERO;
						List<InsuranceCompanyMaster> companyDetails = getInscompanyMasterDetails(
								findHumans.get(0).getCompanyId());
						String currency = companyDetails.size() > 0 ? companyDetails.get(0).getCurrencyId() : newCurr;
						String decimalDigits = currencyDecimalFormat(findHumans.get(0).getCompanyId(), currency).toString();
						String stringFormat = "%0" + decimalDigits + "d";
						String decimalLength = decimalDigits.equals("0") ? "" : String.format(stringFormat, 0L);
						String pattern = StringUtils.isBlank(decimalLength) ? "#####0" : "#####0." + decimalLength;
						DecimalFormat df2 = new DecimalFormat(pattern);

						for (EserviceCommonDetails hum : findHumans) {
							// Human Suminsured
							hum.setSumInsured(exchangeRateScenario2(hum.getSumInsured(), hum.getExchangeRate(), newExRate,
									hum.getCurrency(), newCurr, df));
							hum.setEmpLiabilitySi(exchangeRateScenario2(hum.getEmpLiabilitySi(), hum.getExchangeRate(),
									newExRate, hum.getCurrency(), newCurr, df));
							hum.setFidEmpSi(exchangeRateScenario2(hum.getFidEmpSi(), hum.getExchangeRate(), newExRate,
									hum.getCurrency(), newCurr, df));
							hum.setLiabilitySi(exchangeRateScenario2(hum.getLiabilitySi(), hum.getExchangeRate(), newExRate,
									hum.getCurrency(), newCurr, df));
							hum.setPersonalLiabilitySi(exchangeRateScenario2(hum.getPersonalLiabilitySi(),
									hum.getExchangeRate(), newExRate, hum.getCurrency(), newCurr, df));
							hum.setAooSuminsured(exchangeRateScenario2(hum.getAooSuminsured(), hum.getExchangeRate(), newExRate,
									hum.getCurrency(), newCurr, df));
							hum.setAggSuminsured(exchangeRateScenario2(hum.getAggSuminsured(), hum.getExchangeRate(), newExRate,
									hum.getCurrency(), newCurr, df));

							// Lc Calc
							hum.setSumInsuredLc(hum.getSumInsured() == null ? null
									: new BigDecimal(df2.format(hum.getSumInsured().multiply(exchangeRate))));
							hum.setEmpLiabilitySiLc(hum.getEmpLiabilitySi() == null ? null
									: new BigDecimal(df2.format(hum.getEmpLiabilitySi().multiply(exchangeRate))));
							hum.setFidEmpSiLc(hum.getFidEmpSi() == null ? null
									: new BigDecimal(df2.format(hum.getFidEmpSi().multiply(exchangeRate))));
							hum.setLiabilitySiLc(hum.getLiabilitySi() == null ? null
									: new BigDecimal(df2.format(hum.getLiabilitySi().multiply(exchangeRate))));
							hum.setPersonalLiabilitySiLc(hum.getPersonalLiabilitySi() == null ? null
									: new BigDecimal(df2.format(hum.getPersonalLiabilitySi().multiply(exchangeRate))));
							hum.setAooSuminsuredLc(hum.getAooSuminsured() == null ? null
									: new BigDecimal(df2.format(hum.getAooSuminsured().multiply(exchangeRate))));
							hum.setAggSuminsuredLc(hum.getAggSuminsured() == null ? null
									: new BigDecimal(df2.format(hum.getAggSuminsured().multiply(exchangeRate))));

						}
						humanRepo.saveAll(findHumans);
					}

				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());
					return null;
				}

				return res;
			}*/

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
//							Predicate n5 = cb.equal(c.get("companyId"),"99999");
//							Predicate n6 = cb.or(n4,n5);
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

//			public EserviceBuildingDetails saveBuildingList(SlideCommonSaveReq req, EserviceBuildingDetails data) {
//				EserviceBuildingDetails riskData = data;
//				DozerBeanMapper dozerMapper = new DozerBeanMapper();
//				try {
		//
//					// One Time Table Columns
//					riskData.setCdRefno(null);
//					riskData.setVdRefno(null);
//					riskData.setMsRefno(null);
		//
//					List<String> optedSections = req.getSectionIds();
//					optedSections.add("0");
//					Collections.sort(optedSections);
		//
//					// Find Old
//					List<EserviceBuildingDetails> findList = repository.findByRequestReferenceNo(data.getRequestReferenceNo());
//					if (findList.size() > 0) {
//						repository.deleteByRequestReferenceNoAndRiskIdAndSectionIdNotIn(data.getRequestReferenceNo(),
//								data.getRiskId(), optedSections);
//					}
		//
//					List<ProductSectionMaster> sectionList = getProductSectionDropdown(req.getCompanyId(), req.getProductId());
		//
//					List<EserviceBuildingDetails> buildingList = new ArrayList<EserviceBuildingDetails>();
//					for (String sectionId : optedSections) {
//						EserviceBuildingDetails saveData = new EserviceBuildingDetails();
//						dozerMapper.map(riskData, saveData);
//						saveData.setLocationId(1);
//						if ("0".equalsIgnoreCase(sectionId)) {
//							// Default Entry
//							saveData.setSectionId(sectionId);
//							saveData.setSectionDesc("Default Entry");
//							// Status
//							saveData.setIndustryDesc(StringUtils.isBlank(req.getIndustryDesc()) ? null : req.getIndustryDesc());
//							saveData.setStatus(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus());
//							buildingList.add(saveData);
//						} else {
//							List<ProductSectionMaster> filterAsset = sectionList.stream()
//									.filter(o -> o.getSectionId().equals(Integer.valueOf(sectionId)))
//									.collect(Collectors.toList());
		//
//							if (filterAsset.size() > 0 && "A".equalsIgnoreCase(filterAsset.get(0).getMotorYn())) {
//								List<EserviceBuildingDetails> filterList = findList.stream()
//										.filter(o -> o.getSectionId().equalsIgnoreCase(sectionId)).collect(Collectors.toList());
//								if (filterList.size() > 0) {
		//
//									dozerMapper.map(filterList.get(0), saveData);
//									// Set Policy Details
//									saveData = setBuildingPolicyAndEndtDetails(req, filterList.get(0));
		//
//									// Save Broker Details
//									saveData = setBuildingBrokerDetails(req, filterList.get(0));
		//
//								}
//								saveData.setIndustryDesc(
//										StringUtils.isBlank(req.getIndustryDesc()) ? null : req.getIndustryDesc());
//								saveData.setSectionId(sectionId);
//								saveData.setSectionDesc(filterAsset.get(0).getSectionName());
//								// Status
//								saveData.setStatus(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus());
//								buildingList.add(saveData);
//							}
		//
//						}
		//
//					}
		//
//					// Save All BUildings
//					repository.saveAllAndFlush(buildingList);
		//
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Exception Is ---> " + e.getMessage());
//					return null;
//				}
		//
//				return riskData;
//			}
		/*
			public CommonSlideSaveRes deleteSectionTraces(SlideCommonSaveReq req, CommonSlideSaveRes res) {
				try {

					// Delete Old Section Trases
					SlideSectionGetReq deleteReq = new SlideSectionGetReq();
					deleteReq.setRequestReferenceNo(res.getRequestReferenceNo());
					deleteReq.setRiskId(res.getRiskId());

					boolean accidentDamage = req.getSectionIds().stream().filter(o -> o.equalsIgnoreCase("56"))
							.collect(Collectors.toList()).size() > 0 ? true : false;
					if (accidentDamage == false) {
						deleteReq.setSectionId("56");
						deleteAccidentDamgeDetails(deleteReq);
					}

					boolean allRisk = req.getSectionIds().stream().filter(o -> o.equalsIgnoreCase("3"))
							.collect(Collectors.toList()).size() > 0 ? true : false;
					if (allRisk == false) {
						deleteReq.setSectionId("3");
						deleteAllRiskDetails(deleteReq);
					}

					boolean burglary = req.getSectionIds().stream().filter(o -> o.equalsIgnoreCase("52"))
							.collect(Collectors.toList()).size() > 0 ? true : false;
					if (burglary == false) {
						deleteReq.setSectionId("52");
						deleteBurglaryAndHouseBreakingDetails(deleteReq);
					}

					boolean FireAndAllierPerils = req.getSectionIds().stream().filter(o -> o.equalsIgnoreCase("40"))
							.collect(Collectors.toList()).size() > 0 ? true : false;
					if (FireAndAllierPerils == false) {
						deleteReq.setSectionId("40");
						deleteFireAndAlliedPerils(deleteReq);
					}

					boolean content = req.getSectionIds().stream().filter(o -> o.equalsIgnoreCase("47"))
							.collect(Collectors.toList()).size() > 0 ? true : false;
					if (content == false) {
						deleteReq.setSectionId("47");
						deleteContentDetails(deleteReq);
					}

					boolean electronicEquip = req.getSectionIds().stream().filter(o -> o.equalsIgnoreCase("39"))
							.collect(Collectors.toList()).size() > 0 ? true : false;
					if (electronicEquip == false) {
						deleteReq.setSectionId("39");
						deleteElectronicEquipDetails(deleteReq);
					}

					boolean employeeLiability = req.getSectionIds().stream().filter(o -> o.equalsIgnoreCase("45"))
							.collect(Collectors.toList()).size() > 0 ? true : false;
					if (employeeLiability == false) {
						deleteReq.setSectionId("45");
						deleteEmpLiabilityDetails(deleteReq);
					}

					boolean fidelity = req.getSectionIds().stream().filter(o -> o.equalsIgnoreCase("43"))
							.collect(Collectors.toList()).size() > 0 ? true : false;
					if (fidelity == false) {
						deleteReq.setSectionId("43");
						deleteSlideFidelityGuarantyDetails(deleteReq);
					}

					boolean money = req.getSectionIds().stream().filter(o -> o.equalsIgnoreCase("42"))
							.collect(Collectors.toList()).size() > 0 ? true : false;
					if (money == false) {
						deleteReq.setSectionId("42");
						deleteSlideMoneyDetails(deleteReq);
					}

					boolean machinery = req.getSectionIds().stream().filter(o -> o.equalsIgnoreCase("41"))
							.collect(Collectors.toList()).size() > 0 ? true : false;
					if (machinery == false) {
						deleteReq.setSectionId("41");
						deleteSlideMachineryBreakdownDetails(deleteReq);
					}

					boolean plateGlass = req.getSectionIds().stream().filter(o -> o.equalsIgnoreCase("53"))
							.collect(Collectors.toList()).size() > 0 ? true : false;
					if (plateGlass == false) {
						deleteReq.setSectionId("53");
						deleteSlidePlateGlassDetails(deleteReq);
					}

					boolean liability = req.getSectionIds().stream().filter(o -> o.equalsIgnoreCase("54"))
							.collect(Collectors.toList()).size() > 0 ? true : false;
					if (liability == false) {
						deleteReq.setSectionId("54");
						deleteSlidePublicLiablityDetails(deleteReq);
					}

					// Delete Policy COver Data
					List<Integer> sectionIds = new ArrayList<Integer>();
					req.getSectionIds().forEach(o -> {
						sectionIds.add(Integer.valueOf(o));
					});

					Long coverCount = factorRepo.countByRequestReferenceNoAndProductIdAndSectionIdNotIn(
							res.getRequestReferenceNo(), Integer.valueOf(res.getProductId()), sectionIds);
					if (coverCount > 0) {
						factorRepo.deleteByRequestReferenceNoAndProductIdAndSectionIdNotIn(res.getRequestReferenceNo(),
								Integer.valueOf(res.getProductId()), sectionIds);
					}

				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());
					return null;

				}

				return res;
			}
		*/

//			@Override
//			public CommonSlideSaveRes saveCommonDetails(SlideCommonSaveReq req) {
//				CommonSlideSaveRes res = new CommonSlideSaveRes();
//				List<BuildingSectionRes> sectionResList = new ArrayList<BuildingSectionRes>();
//				try {
//					CompanyProductMaster product = getCompanyProductMasterDropdown(req.getCompanyId(), req.getProductId()); // productRepo.findByProductIdOrderByAmendIdDesc(Integer.valueOf(req.getProductId()));
		//
//					// Find Old
//					List<EserviceCommonDetails> findHumans = new ArrayList<EserviceCommonDetails>();
//					List<EserviceBuildingDetails> findBuildings = new ArrayList<EserviceBuildingDetails>();
		//
//					if (StringUtils.isNotBlank(req.getRequestReferenceNo())) {
//						findHumans = humanRepo.findByRequestReferenceNoOrderByRiskIdAsc(req.getRequestReferenceNo());
//						findBuildings = buildingRepo.findByRequestReferenceNo(req.getRequestReferenceNo());
//						// Change Of Currency Suminsured Change
//						if (findHumans.size() > 0 || findBuildings.size() > 0) {
//							String decimalDigits = currencyDecimalFormat(req.getCompanyId(), req.getCurrency()).toString();
//							String stringFormat = "%0" + decimalDigits + "d";
//							String decimalLength = decimalDigits.equals("0") ? "" : String.format(stringFormat, 0L);
//							String pattern = StringUtils.isBlank(decimalLength) ? "#####0" : "#####0." + decimalLength;
//							DecimalFormat df = new DecimalFormat(pattern);
		//
//							BigDecimal exchangeRate = StringUtils.isBlank(req.getExchangeRate()) ? BigDecimal.ONE
//									: new BigDecimal(req.getExchangeRate());
//							// Update Building
//							updateBuildingSuminsured(findBuildings, exchangeRate, req.getCurrency(), df);
		//
//							// Update Human
//							updateHumanSuminsured(findHumans, exchangeRate, req.getCurrency(), df);
//						}
//					}
		//
//					// Human Insert
//					if (StringUtils.isNotBlank(product.getMotorYn()) && product.getMotorYn().equalsIgnoreCase("H")) {
//						EserviceCommonDetails saveHumanData = new EserviceCommonDetails();
		//
//						// Set Ref No
//						saveHumanData = setHumanRefNo(req);
		//
//						// Set Policy Details
//						saveHumanData = setHumanPolicyAndEndtDetails(req, saveHumanData);
		//
//						// Save Broker Details
//						saveHumanData = setHumanBrokerDetails(req, saveHumanData);
		//
//						// One Time Table Columns
//						saveHumanData.setCdRefno(null);
//						saveHumanData.setVdRefno(null);
//						saveHumanData.setMsRefno(null);
//						saveHumanData.setLocationId(1);
		//
//						// saveHumanData.setRiskId(null != saveHumanData && null !=
//						// saveHumanData.getRiskId() ? saveHumanData.getRiskId() : 1);
//						humanRepo.save(saveHumanData);
		//
//						// Save Section
//						sectionResList = insertHumanSectionDetails(req, saveHumanData);
		//
//						// Response
//						res.setCompanyId(req.getCompanyId());
//						res.setCreatedBy(req.getCreatedBy());
//						res.setCustomerReferenceNo(req.getCustomerReferenceNo());
//						res.setProductId(req.getProductId());
		//
//						res.setRequestReferenceNo(saveHumanData.getRequestReferenceNo());
//						res.setResponse(" Saved Successfully");
//						res.setRiskId(saveHumanData.getRiskId().toString());
//						req.getSectionIds().remove("0");
//						res.setSectionIds(req.getSectionIds());
		//
//						// Asset Insert
//					} else {
//						EserviceBuildingDetails saveBuildingData = new EserviceBuildingDetails();
		//
//						// Set Ref No
//						saveBuildingData = setBuildingRefNo(req);// reference no block
		//
//						// Set Policy Details
//						saveBuildingData = setBuildingPolicyAndEndtDetails(req, saveBuildingData);
		//
//						// Save Broker Details
//						saveBuildingData = setBuildingBrokerDetails(req, saveBuildingData);
		//
//						// Save Building List
//						saveBuildingData = saveBuildingList(req, saveBuildingData);
		//
//						// Save Section
//						sectionResList = insertBuildingSectionDetails(req, saveBuildingData);
		//
//						// Response
//						res.setCompanyId(req.getCompanyId());
//						res.setCreatedBy(req.getCreatedBy());
//						res.setCustomerReferenceNo(req.getCustomerReferenceNo());
//						res.setProductId(req.getProductId());
		//
//						res.setRequestReferenceNo(saveBuildingData.getRequestReferenceNo());
//						res.setResponse(" Saved Successfully");
//						res.setRiskId(saveBuildingData.getRiskId().toString());
//						req.getSectionIds().remove("0");
//						res.setSectionIds(req.getSectionIds());
		//
//					}
		//
//					// Delete Old Section Trases
//					// deleteSectionTraces(req ,res );
		//
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Log Details" + e.getMessage());
//					return null;
		//
//				}
		//
//				return res;
//			}*/
		///*
//			public CommonSlideSaveRes updateBuildingSuminsured(List<EserviceBuildingDetails> findBuildings,
//					BigDecimal newExRate, String newCurr, DecimalFormat df) {
//				CommonSlideSaveRes res = new CommonSlideSaveRes();
//				try {
//					if (findBuildings != null && findBuildings.size() > 0) {
//						// exchangeRateScenario2(BigDecimal oldSi , BigDecimal newSi , BigDecimal
//						// oldExRate ,BigDecimal newExRate , String oldCurr , String newCurr ,
//						// DecimalFormat df)
		//
//						// Lc Decimal
//						BigDecimal exchangeRate = newExRate != null ? newExRate : BigDecimal.ZERO;
//						List<InsuranceCompanyMaster> companyDetails = getInscompanyMasterDetails(
//								findBuildings.get(0).getCompanyId());
//						// String currency = companyDetails.size() > 0 ?
//						// companyDetails.get(0).getCurrencyId() : newCurr ;
////						String decimalDigits = currencyDecimalFormat(findBuildings.get(0).getCompanyId() , currency ).toString();
////						String stringFormat = "%0"+decimalDigits+"d" ;
////						String decimalLength = decimalDigits.equals("0") ?"" : String.format(stringFormat ,0L)  ;
////						String pattern = StringUtils.isBlank(decimalLength) ?  "#####0" :   "#####0." + decimalLength;
//						String pattern = "#####0";
//						DecimalFormat df2 = new DecimalFormat(pattern);
		//
//						for (EserviceBuildingDetails build : findBuildings) {
//							// Asset Suminsured
		//
//							// exchangeRateScenario2(hum.getSumInsured() , hum.getExchangeRate() , newExRate
//							// , hum.getCurrency() ,newCurr ,df )
//							// Building
////							build.setBuildingSuminsured(exchangeRateScenario2(build.getBuildingSuminsured(),
////									build.getExchangeRate(), newExRate, build.getCurrency(), newCurr, df));
////							build.setWaterTankSi(exchangeRateScenario2(build.getWaterTankSi(), build.getExchangeRate(),
////									newExRate, build.getCurrency(), newCurr, df));
////							build.setLossOfRentSi(exchangeRateScenario2(build.getLossOfRentSi(), build.getExchangeRate(),
////									newExRate, build.getCurrency(), newCurr, df));
////							build.setArchitectsSi(exchangeRateScenario2(build.getArchitectsSi(), build.getExchangeRate(),
////									newExRate, build.getCurrency(), newCurr, df));
		//
//							// Content
////							build.setContentSuminsured(exchangeRateScenario2(build.getContentSuminsured(),
////									build.getExchangeRate(), newExRate, build.getCurrency(), newCurr, df));
////							build.setEquipmentSi(exchangeRateScenario2(build.getEquipmentSi(), build.getExchangeRate(),
////									newExRate, build.getCurrency(), newCurr, df));
////							build.setJewellerySi(exchangeRateScenario2(build.getJewellerySi(), build.getExchangeRate(),
////									newExRate, build.getCurrency(), newCurr, df));
////							build.setPaitingsSi(exchangeRateScenario2(build.getPaitingsSi(), build.getExchangeRate(), newExRate,
////									build.getCurrency(), newCurr, df));
////							build.setCarpetsSi(exchangeRateScenario2(build.getCarpetsSi(), build.getExchangeRate(), newExRate,
////									build.getCurrency(), newCurr, df));
		//
//							// All Risk , Plant All Risk , Business All Risk
////							build.setAllriskSuminsured(exchangeRateScenario2(build.getAllriskSuminsured(),
////									build.getExchangeRate(), newExRate, build.getCurrency(), newCurr, df));
////							build.setMiningPlantSi(exchangeRateScenario2(build.getMiningPlantSi(), build.getExchangeRate(),
////									newExRate, build.getCurrency(), newCurr, df));
////							build.setNonminingPlantSi(exchangeRateScenario2(build.getNonminingPlantSi(),
////									build.getExchangeRate(), newExRate, build.getCurrency(), newCurr, df));
////							build.setGensetsSi(exchangeRateScenario2(build.getGensetsSi(), build.getExchangeRate(), newExRate,
////									build.getCurrency(), newCurr, df));
//							// build.setEquipmentSi(exchangeRateScenario2(build.getEquipmentSi() ,
//							// build.getExchangeRate() , newExRate , build.getCurrency() ,newCurr ,df));
		//
//							// Burgalry
//							build.setStockInTradeSi(exchangeRateScenario2(build.getStockInTradeSi(), build.getExchangeRate(),
//									newExRate, build.getCurrency(), newCurr, df));
//							;
//							build.setGoodsSi(exchangeRateScenario2(build.getGoodsSi(), build.getExchangeRate(), newExRate,
//									build.getCurrency(), newCurr, df));
//							build.setFurnitureSi(exchangeRateScenario2(build.getFurnitureSi(), build.getExchangeRate(),
//									newExRate, build.getCurrency(), newCurr, df));
//							build.setCashValueablesSi(exchangeRateScenario2(build.getCashValueablesSi(),
//									build.getExchangeRate(), newExRate, build.getCurrency(), newCurr, df));
//							build.setApplianceSi(exchangeRateScenario2(build.getApplianceSi(), build.getExchangeRate(),
//									newExRate, build.getCurrency(), newCurr, df));
//							;
		//
//							// Fire And Material Damage
//							build.setFirePlantSi(exchangeRateScenario2(build.getFirePlantSi(), build.getExchangeRate(),
//									newExRate, build.getCurrency(), newCurr, df));
		//
//							// Electronic Equipment
////							build.setElecEquipSuminsured(exchangeRateScenario2(build.getElecEquipSuminsured(),
////									build.getExchangeRate(), newExRate, build.getCurrency(), newCurr, df));
		//
//							// Money
//							build.setMoneyAnnualEstimate(exchangeRateScenario2(build.getMoneyAnnualEstimate(),
//									build.getExchangeRate(), newExRate, build.getCurrency(), newCurr, df));
//							build.setMoneyCollector(exchangeRateScenario2(build.getMoneyCollector(), build.getExchangeRate(),
//									newExRate, build.getCurrency(), newCurr, df));
//							build.setMoneyDirectorResidence(exchangeRateScenario2(build.getMoneyDirectorResidence(),
//									build.getExchangeRate(), newExRate, build.getCurrency(), newCurr, df));
//							build.setMoneyOutofSafe(exchangeRateScenario2(build.getMoneyOutofSafe(), build.getExchangeRate(),
//									newExRate, build.getCurrency(), newCurr, df));
//							build.setMoneySafeLimit(exchangeRateScenario2(build.getMoneySafeLimit(), build.getExchangeRate(),
//									newExRate, build.getCurrency(), newCurr, df));
//							build.setMoneyMajorLoss(exchangeRateScenario2(build.getMoneyMajorLoss(), build.getExchangeRate(),
//									newExRate, build.getCurrency(), newCurr, df));
		//
//							// Machinery
////							build.setElecMachinesSi(exchangeRateScenario2(build.getElecMachinesSi(), build.getExchangeRate(),
////									newExRate, build.getCurrency(), newCurr, df));
////							build.setBoilerPlantsSi(exchangeRateScenario2(build.getBoilerPlantsSi(), build.getExchangeRate(),
////									newExRate, build.getCurrency(), newCurr, df));
//							// build.setEquipmentSi(exchangeRateScenario2(build.getEquipmentSi() ,
//							// build.getExchangeRate() , newExRate , build.getCurrency() ,newCurr ,df));
////							build.setGeneralMachineSi(exchangeRateScenario2(build.getGeneralMachineSi(),
////									build.getExchangeRate(), newExRate, build.getCurrency(), newCurr, df));
////							build.setMachineEquipSi(exchangeRateScenario2(build.getMachineEquipSi(), build.getExchangeRate(),
////									newExRate, build.getCurrency(), newCurr, df));
////							build.setManuUnitsSi(exchangeRateScenario2(build.getManuUnitsSi(), build.getExchangeRate(),
////									newExRate, build.getCurrency(), newCurr, df));
////							build.setPowerPlantSi(exchangeRateScenario2(build.getPowerPlantSi(), build.getExchangeRate(),
////									newExRate, build.getCurrency(), newCurr, df));
		//
//							// Lc Calc
////							build.setBuildingSuminsuredLc(build.getBuildingSuminsured() == null ? null
////									: new BigDecimal(df2.format(build.getBuildingSuminsured().multiply(exchangeRate))));
////							build.setContentSuminsuredLc(build.getContentSuminsured() == null ? null
////									: new BigDecimal(df2.format(build.getContentSuminsured().multiply(exchangeRate))));
////							build.setEquipmentSiLc(build.getEquipmentSi() == null ? null
////									: new BigDecimal(df2.format(build.getEquipmentSi().multiply(exchangeRate))));
////							build.setJewellerySiLc(build.getJewellerySi() == null ? null
////									: new BigDecimal(df2.format(build.getJewellerySi().multiply(exchangeRate))));
////							build.setPaitingsSiLc(build.getPaitingsSi() == null ? null
////									: new BigDecimal(df2.format(build.getPaitingsSi().multiply(exchangeRate))));
////							build.setCarpetsSiLc(build.getCarpetsSi() == null ? null
////									: new BigDecimal(df2.format(build.getCarpetsSi().multiply(exchangeRate))));
////							build.setAllriskSuminsuredLc(build.getAllriskSuminsured() == null ? null
////									: new BigDecimal(df2.format(build.getAllriskSuminsured().multiply(exchangeRate))));
////		 				    build.setMiningPlantSiLc(build.getMiningPlantSi() == null ? null
////									: new BigDecimal(df2.format(build.getMiningPlantSi().multiply(exchangeRate))));
////							build.setNonminingPlantSi(build.getNonminingPlantSi() == null ? null
////									: new BigDecimal(df2.format(build.getNonminingPlantSi().multiply(exchangeRate))));
////							build.setGensetsSiLc(build.getGensetsSi() == null ? null
////									: new BigDecimal(df2.format(build.getGensetsSi().multiply(exchangeRate))));
////							build.setEquipmentSiLc(build.getEquipmentSi() == null ? null
////									: new BigDecimal(df2.format(build.getEquipmentSi().multiply(exchangeRate))));
//							build.setStockInTradeSiLc(build.getStockInTradeSi() == null ? null
//									: new BigDecimal(df2.format(build.getStockInTradeSi().multiply(exchangeRate))));
//							build.setGoodsSiLc(build.getGoodsSi() == null ? null
//									: new BigDecimal(df2.format(build.getGoodsSi().multiply(exchangeRate))));
//							build.setFurnitureSiLc(build.getFurnitureSi() == null ? null
//									: new BigDecimal(df2.format(build.getFurnitureSi().multiply(exchangeRate))));
//							build.setCashValueablesSiLc(build.getCashValueablesSi() == null ? null
//									: new BigDecimal(df2.format(build.getCashValueablesSi().multiply(exchangeRate))));
//							build.setApplianceSiLc(build.getApplianceSi() == null ? null
//									: new BigDecimal(df2.format(build.getApplianceSi().multiply(exchangeRate))));
//							build.setStockInTradeSiLc(build.getStockInTradeSi() == null ? null
//									: new BigDecimal(df2.format(build.getStockInTradeSi().multiply(exchangeRate))));
////							build.setFirePlantSiLc(build.getFirePlantSi() == null ? null
////									: new BigDecimal(df2.format(build.getFirePlantSi().multiply(exchangeRate))));
////							build.setElecEquipSuminsuredLc(build.getElecEquipSuminsured() == null ? null
////									: new BigDecimal(df2.format(build.getElecEquipSuminsured().multiply(exchangeRate))));
//							build.setMoneyAnnualEstimateLc(build.getMoneyAnnualEstimate() == null ? null
//									: new BigDecimal(df2.format(build.getMoneyAnnualEstimate().multiply(exchangeRate))));
//							build.setMoneyCollectorLc(build.getMoneyCollector() == null ? null
//									: new BigDecimal(df2.format(build.getMoneyCollector().multiply(exchangeRate))));
//							build.setMoneyDirectorResidenceLc(build.getMoneyDirectorResidence() == null ? null
//									: new BigDecimal(df2.format(build.getMoneyDirectorResidence().multiply(exchangeRate))));
//							build.setMoneyOutofSafeLc(build.getMoneyOutofSafe() == null ? null
//									: new BigDecimal(df2.format(build.getMoneyOutofSafe().multiply(exchangeRate))));
//							build.setMoneySafeLimitLc(build.getMoneySafeLimit() == null ? null
//									: new BigDecimal(df2.format(build.getMoneySafeLimit().multiply(exchangeRate))));
//							build.setMoneyMajorLossLc(build.getMoneyMajorLoss() == null ? null
//									: new BigDecimal(df2.format(build.getMoneyMajorLoss().multiply(exchangeRate))));
////							build.setElecMachinesSiLc(build.getElecMachinesSi() == null ? null
////									: new BigDecimal(df2.format(build.getElecMachinesSi().multiply(exchangeRate))));
////							build.setBoilerPlantsSiLc(build.getBoilerPlantsSi() == null ? null
////									: new BigDecimal(df2.format(build.getBoilerPlantsSi().multiply(exchangeRate))));
////							build.setEquipmentSiLc(build.getEquipmentSi() == null ? null
////									: new BigDecimal(df2.format(build.getEquipmentSi().multiply(exchangeRate))));
////							build.setGeneralMachineSiLc(build.getGeneralMachineSi() == null ? null
////									: new BigDecimal(df2.format(build.getGeneralMachineSi().multiply(exchangeRate))));
////							build.setMachineEquipSiLc(build.getMachineEquipSi() == null ? null
////									: new BigDecimal(df2.format(build.getMachineEquipSi().multiply(exchangeRate))));
////							build.setManuUnitsSiLc(build.getManuUnitsSi() == null ? null
////									: new BigDecimal(df2.format(build.getManuUnitsSi().multiply(exchangeRate))));
////							build.setPowerPlantSiLc(build.getPowerPlantSi() == null ? null
////									: new BigDecimal(df2.format(build.getPowerPlantSi().multiply(exchangeRate))));
////							build.setWaterTankSiLc(build.getWaterTankSi() == null ? null
////									: new BigDecimal(df2.format(build.getWaterTankSi().multiply(exchangeRate))));
////							build.setLossOfRentSiLc(build.getLossOfRentSi() == null ? null
////									: new BigDecimal(df2.format(build.getLossOfRentSi().multiply(exchangeRate))));
////							build.setArchitectsSiLc(build.getArchitectsSi() == null ? null
////									: new BigDecimal(df2.format(build.getArchitectsSi().multiply(exchangeRate))));
		//
//						}
//						buildingRepo.saveAll(findBuildings);
//					}
		//
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Log Details" + e.getMessage());
//					return null;
//				}
//				return res;
//			}*/
		///*
//			public CommonSlideSaveRes updateHumanSuminsured(List<EserviceCommonDetails> findHumans, BigDecimal newExRate,
//					String newCurr, DecimalFormat df) {
//				CommonSlideSaveRes res = new CommonSlideSaveRes();
//				try {
//					if (findHumans != null && findHumans.size() > 0) {
//						// exchangeRateScenario2(BigDecimal oldSi , BigDecimal newSi , BigDecimal
//						// oldExRate ,BigDecimal newExRate , String oldCurr , String newCurr ,
//						// DecimalFormat df)
		//
//						// Lc Decimal
//						BigDecimal exchangeRate = newExRate != null ? newExRate : BigDecimal.ZERO;
//						List<InsuranceCompanyMaster> companyDetails = getInscompanyMasterDetails(
//								findHumans.get(0).getCompanyId());
//						String currency = companyDetails.size() > 0 ? companyDetails.get(0).getCurrencyId() : newCurr;
//						String decimalDigits = currencyDecimalFormat(findHumans.get(0).getCompanyId(), currency).toString();
//						String stringFormat = "%0" + decimalDigits + "d";
//						String decimalLength = decimalDigits.equals("0") ? "" : String.format(stringFormat, 0L);
//						String pattern = StringUtils.isBlank(decimalLength) ? "#####0" : "#####0." + decimalLength;
//						DecimalFormat df2 = new DecimalFormat(pattern);
		//
//						for (EserviceCommonDetails hum : findHumans) {
//							// Human Suminsured
//							hum.setSumInsured(exchangeRateScenario2(hum.getSumInsured(), hum.getExchangeRate(), newExRate,
//									hum.getCurrency(), newCurr, df));
//					//		hum.setEmpLiabilitySi(exchangeRateScenario2(hum.getEmpLiabilitySi(), hum.getExchangeRate(),
//					//				newExRate, hum.getCurrency(), newCurr, df));
//					//		hum.setFidEmpSi(exchangeRateScenario2(hum.getFidEmpSi(), hum.getExchangeRate(), newExRate,
//					//				hum.getCurrency(), newCurr, df));
//							hum.setLiabilitySi(exchangeRateScenario2(hum.getLiabilitySi(), hum.getExchangeRate(), newExRate,
//									hum.getCurrency(), newCurr, df));
////							hum.setPersonalLiabilitySi(exchangeRateScenario2(hum.getPersonalLiabilitySi(),
////									hum.getExchangeRate(), newExRate, hum.getCurrency(), newCurr, df));
////							hum.setAooSuminsured(exchangeRateScenario2(hum.getAooSuminsured(), hum.getExchangeRate(), newExRate,
////									hum.getCurrency(), newCurr, df));
////							hum.setAggSuminsured(exchangeRateScenario2(hum.getAggSuminsured(), hum.getExchangeRate(), newExRate,
////									hum.getCurrency(), newCurr, df));
		//
//							// Lc Calc
//							hum.setSumInsuredLc(hum.getSumInsured() == null ? null
//									: new BigDecimal(df2.format(hum.getSumInsured().multiply(exchangeRate))));
//					//		hum.setEmpLiabilitySiLc(hum.getEmpLiabilitySi() == null ? null
//					//				: new BigDecimal(df2.format(hum.getEmpLiabilitySi().multiply(exchangeRate))));
//					//		hum.setFidEmpSiLc(hum.getFidEmpSi() == null ? null
//					//				: new BigDecimal(df2.format(hum.getFidEmpSi().multiply(exchangeRate))));
////							hum.setLiabilitySiLc(hum.getLiabilitySi() == null ? null
////									: new BigDecimal(df2.format(hum.getLiabilitySi().multiply(exchangeRate))));
////							hum.setPersonalLiabilitySiLc(hum.getPersonalLiabilitySi() == null ? null
////									: new BigDecimal(df2.format(hum.getPersonalLiabilitySi().multiply(exchangeRate))));
////							hum.setAooSuminsuredLc(hum.getAooSuminsured() == null ? null
////									: new BigDecimal(df2.format(hum.getAooSuminsured().multiply(exchangeRate))));
////							hum.setAggSuminsuredLc(hum.getAggSuminsured() == null ? null
////									: new BigDecimal(df2.format(hum.getAggSuminsured().multiply(exchangeRate))));
		//
//						}
//						humanRepo.saveAll(findHumans);
//					}
		//
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Log Details" + e.getMessage());
//					return null;
//				}
		//
//				return res;
//			}*/
		//
//			public List<InsuranceCompanyMaster> getInscompanyMasterDetails(String companyId) {
//				List<InsuranceCompanyMaster> list = new ArrayList<InsuranceCompanyMaster>();
		//
//				try {
//					Date today = new Date();
//					Calendar cal = new GregorianCalendar();
//					cal.setTime(today);
//					cal.set(Calendar.HOUR_OF_DAY, 23);
//					cal.set(Calendar.MINUTE, 1);
//					today = cal.getTime();
//					cal.set(Calendar.HOUR_OF_DAY, 1);
//					cal.set(Calendar.MINUTE, 1);
//					Date todayEnd = cal.getTime();
		//
//					// Criteria
//					CriteriaBuilder cb = em.getCriteriaBuilder();
//					CriteriaQuery<InsuranceCompanyMaster> query = cb.createQuery(InsuranceCompanyMaster.class);
		//
//					// Find All
//					Root<InsuranceCompanyMaster> c = query.from(InsuranceCompanyMaster.class);
		//
//					// Select
//					query.select(c);
		//
//					// Order By
//					List<Order> orderList = new ArrayList<Order>();
//					orderList.add(cb.asc(c.get("effectiveDateStart")));
		//
//					// Effective Date Max Filter
//					Subquery<Date> effectiveDate = query.subquery(Date.class);
//					Root<InsuranceCompanyMaster> ocpm1 = effectiveDate.from(InsuranceCompanyMaster.class);
//					effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
//					Predicate a1 = cb.equal(c.get("companyId"), ocpm1.get("companyId"));
//					Predicate a2 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
//					effectiveDate.where(a1, a2);
		//
//					// Effective Date End
//					Subquery<Date> effectiveDate2 = query.subquery(Date.class);
//					Root<InsuranceCompanyMaster> ocpm2 = effectiveDate2.from(InsuranceCompanyMaster.class);
//					effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
//					Predicate a3 = cb.equal(c.get("companyId"), ocpm2.get("companyId"));
//					Predicate a4 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
//					effectiveDate2.where(a3, a4);
		//
//					// Where
//					Predicate n1 = cb.equal(c.get("status"), "Y");
//					Predicate n11 = cb.equal(c.get("status"), "R");
//					Predicate n12 = cb.or(n1, n11);
//					Predicate n2 = cb.equal(c.get("effectiveDateStart"), effectiveDate);
//					Predicate n4 = cb.equal(c.get("effectiveDateEnd"), effectiveDate2);
//					Predicate n13 = cb.equal(c.get("companyId"), companyId);
//					query.where(n12, n2, n4, n13).orderBy(orderList);
		//
//					// Get Result
//					TypedQuery<InsuranceCompanyMaster> result = em.createQuery(query);
//					list = result.getResultList();
		//
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Exception is ---> " + e.getMessage());
//					return null;
//				}
//				return list;
//			}
		//
//			public BigDecimal exchangeRateScenario2(BigDecimal oldSi, BigDecimal oldExRate, BigDecimal newExRate,
//					String oldCurr, String newCurr, DecimalFormat df) {
//				BigDecimal suminsured = BigDecimal.ZERO;
//				try {
//					// Change Of Currency
//					if (!oldCurr.equalsIgnoreCase(newCurr) && (oldSi != null)) {
//						String calcType = newExRate.compareTo(BigDecimal.ONE) == 0 ? "multiply" : "divide";
//						BigDecimal exchange = "multiply".equalsIgnoreCase(calcType) ? oldExRate : newExRate;
//						// Suminsured
//						suminsured = exchangeFcCalc(exchange, oldSi, calcType, df);
		//
//					} else {
//						suminsured = oldSi;
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Log Details" + e.getMessage());
//					return null;
//				}
//				return suminsured;
//			}
		//
//			public BigDecimal exchangeFcCalc(BigDecimal exchangeRate, BigDecimal oldSuminsured, String calcType,
//					DecimalFormat df) {
//				BigDecimal sumInsured = null;
//				try {
//					if ("divide".equalsIgnoreCase(calcType)) {
//						sumInsured = new BigDecimal(Math
//								.round(Double.valueOf(oldSuminsured.divide(exchangeRate, RoundingMode.UP).toPlainString())));
		//
//					} else if ("multiply".equalsIgnoreCase(calcType)) {
		//
//						sumInsured = new BigDecimal(
//								Math.round(Double.valueOf(oldSuminsured.multiply(exchangeRate).toPlainString())));
//					}
		//
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Log Details" + e.getMessage());
//					return null;
//				}
//				return sumInsured;
//			}
		//
//			public Integer currencyDecimalFormat(String insuranceId, String currencyId) {
//				Integer decimalFormat = 0;
//				try {
//					Date today = new Date();
//					Calendar cal = new GregorianCalendar();
//					cal.setTime(today);
//					cal.set(Calendar.HOUR_OF_DAY, 23);
//					cal.set(Calendar.MINUTE, 1);
//					today = cal.getTime();
//					cal.set(Calendar.HOUR_OF_DAY, 1);
//					cal.set(Calendar.MINUTE, 1);
//					Date todayEnd = cal.getTime();
		//
//					// Criteria
//					CriteriaBuilder cb = em.getCriteriaBuilder();
//					CriteriaQuery<CurrencyMaster> query = cb.createQuery(CurrencyMaster.class);
//					List<CurrencyMaster> list = new ArrayList<CurrencyMaster>();
		//
//					// Find All
//					Root<CurrencyMaster> c = query.from(CurrencyMaster.class);
		//
//					// Select
//					query.select(c);
		//
//					// Order By
//					List<Order> orderList = new ArrayList<Order>();
//					orderList.add(cb.asc(c.get("currencyName")));
		//
//					// Effective Date Max Filter
//					Subquery<Date> effectiveDate = query.subquery(Date.class);
//					Root<CurrencyMaster> ocpm1 = effectiveDate.from(CurrencyMaster.class);
//					effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
//					Predicate a11 = cb.equal(c.get("currencyId"), ocpm1.get("currencyId"));
//					Predicate a12 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
//					Predicate a18 = cb.equal(c.get("status"), ocpm1.get("status"));
//					Predicate a22 = cb.equal(c.get("companyId"), ocpm1.get("companyId"));
		//
//					effectiveDate.where(a11, a12, a18, a22);
		//
//					// Effective Date Max Filter
//					Subquery<Date> effectiveDate2 = query.subquery(Date.class);
//					Root<CurrencyMaster> ocpm2 = effectiveDate2.from(CurrencyMaster.class);
//					effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
//					Predicate a13 = cb.equal(c.get("currencyId"), ocpm2.get("currencyId"));
//					Predicate a14 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
//					Predicate a19 = cb.equal(c.get("status"), ocpm2.get("status"));
//					Predicate a23 = cb.equal(c.get("companyId"), ocpm2.get("companyId"));
		//
//					effectiveDate2.where(a13, a14, a19, a23);
		//
//					// Where
//					Predicate n1 = cb.equal(c.get("status"), "Y");
//					Predicate n2 = cb.equal(c.get("effectiveDateStart"), effectiveDate);
//					Predicate n3 = cb.equal(c.get("effectiveDateEnd"), effectiveDate2);
//					Predicate n4 = cb.equal(c.get("companyId"), insuranceId);
////							Predicate n5 = cb.equal(c.get("companyId"),"99999");
////							Predicate n6 = cb.or(n4,n5);
//					Predicate n7 = cb.equal(c.get("currencyId"), currencyId);
//					query.where(n1, n2, n3, n4, n7).orderBy(orderList);
		//
//					// Get Result
//					TypedQuery<CurrencyMaster> result = em.createQuery(query);
//					list = result.getResultList();
		//
//					decimalFormat = list.size() > 0
//							? (list.get(0).getDecimalDigit() == null ? 0 : list.get(0).getDecimalDigit())
//							: 0;
		//
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Exception is ---> " + e.getMessage());
//					return null;
//				}
//				return decimalFormat;
//			}
		//
//			public EserviceBuildingDetails saveBuildingList(SlideCommonSaveReq req, EserviceBuildingDetails data) {
//				EserviceBuildingDetails riskData = data;
//				DozerBeanMapper dozerMapper = new DozerBeanMapper();
//				try {
		//
//					// One Time Table Columns
//					riskData.setCdRefno(null);
//					riskData.setVdRefno(null);
//					riskData.setMsRefno(null);
		//
//					List<String> optedSections = req.getSectionIds();
//					optedSections.add("0");
//					Collections.sort(optedSections);
		//
//					// Find Old
//					List<EserviceBuildingDetails> findList = repository.findByRequestReferenceNo(data.getRequestReferenceNo());
//					if (findList.size() > 0) {
//						repository.deleteByRequestReferenceNoAndRiskIdAndSectionIdNotIn(data.getRequestReferenceNo(),
//								data.getRiskId(), optedSections);
//					}
		//
//					List<ProductSectionMaster> sectionList = getProductSectionDropdown(req.getCompanyId(), req.getProductId());
		//
//					List<EserviceBuildingDetails> buildingList = new ArrayList<EserviceBuildingDetails>();
//					for (String sectionId : optedSections) {
//						EserviceBuildingDetails saveData = new EserviceBuildingDetails();
//						dozerMapper.map(riskData, saveData);
//						saveData.setLocationId(1);
//						if ("0".equalsIgnoreCase(sectionId)) {
//							// Default Entry
//							saveData.setSectionId(sectionId);
//							saveData.setSectionDesc("Default Entry");
//							// Status
//							saveData.setIndustryDesc(StringUtils.isBlank(req.getIndustryDesc()) ? null : req.getIndustryDesc());
//							saveData.setStatus(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus());
//							buildingList.add(saveData);
//						} else {
//							List<ProductSectionMaster> filterAsset = sectionList.stream()
//									.filter(o -> o.getSectionId().equals(Integer.valueOf(sectionId)))
//									.collect(Collectors.toList());
		//
//							if (filterAsset.size() > 0 && "A".equalsIgnoreCase(filterAsset.get(0).getMotorYn())) {
//								List<EserviceBuildingDetails> filterList = findList.stream()
//										.filter(o -> o.getSectionId().equalsIgnoreCase(sectionId)).collect(Collectors.toList());
//								if (filterList.size() > 0) {
		//
//									dozerMapper.map(filterList.get(0), saveData);
//									// Set Policy Details
//									saveData = setBuildingPolicyAndEndtDetails(req, filterList.get(0));
		//
//									// Save Broker Details
//									saveData = setBuildingBrokerDetails(req, filterList.get(0));
		//
//								}
//								saveData.setIndustryDesc(
//										StringUtils.isBlank(req.getIndustryDesc()) ? null : req.getIndustryDesc());
//								saveData.setSectionId(sectionId);
//								saveData.setSectionDesc(filterAsset.get(0).getSectionName());
//								// Status
//								saveData.setStatus(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus());
//								buildingList.add(saveData);
//							}
		//
//						}
		//
//					}
		//
//					// Save All BUildings
//					repository.saveAllAndFlush(buildingList);
		//
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Exception Is ---> " + e.getMessage());
//					return null;
//				}
		//
//				return riskData;
//			}
		///*
//			public CommonSlideSaveRes deleteSectionTraces(SlideCommonSaveReq req, CommonSlideSaveRes res) {
//				try {
		//
//					// Delete Old Section Trases
//					SlideSectionGetReq deleteReq = new SlideSectionGetReq();
//					deleteReq.setRequestReferenceNo(res.getRequestReferenceNo());
//					deleteReq.setRiskId(res.getRiskId());
		//
//					boolean accidentDamage = req.getSectionIds().stream().filter(o -> o.equalsIgnoreCase("56"))
//							.collect(Collectors.toList()).size() > 0 ? true : false;
//					if (accidentDamage == false) {
//						deleteReq.setSectionId("56");
//						deleteAccidentDamgeDetails(deleteReq);
//					}
		//
//					boolean allRisk = req.getSectionIds().stream().filter(o -> o.equalsIgnoreCase("3"))
//							.collect(Collectors.toList()).size() > 0 ? true : false;
//					if (allRisk == false) {
//						deleteReq.setSectionId("3");
//						deleteAllRiskDetails(deleteReq);
//					}
		//
//					boolean burglary = req.getSectionIds().stream().filter(o -> o.equalsIgnoreCase("52"))
//							.collect(Collectors.toList()).size() > 0 ? true : false;
//					if (burglary == false) {
//						deleteReq.setSectionId("52");
//						deleteBurglaryAndHouseBreakingDetails(deleteReq);
//					}
		//
//					boolean FireAndAllierPerils = req.getSectionIds().stream().filter(o -> o.equalsIgnoreCase("40"))
//							.collect(Collectors.toList()).size() > 0 ? true : false;
//					if (FireAndAllierPerils == false) {
//						deleteReq.setSectionId("40");
//						deleteFireAndAlliedPerils(deleteReq);
//					}
		//
//					boolean content = req.getSectionIds().stream().filter(o -> o.equalsIgnoreCase("47"))
//							.collect(Collectors.toList()).size() > 0 ? true : false;
//					if (content == false) {
//						deleteReq.setSectionId("47");
//						deleteContentDetails(deleteReq);
//					}
		//
//					boolean electronicEquip = req.getSectionIds().stream().filter(o -> o.equalsIgnoreCase("39"))
//							.collect(Collectors.toList()).size() > 0 ? true : false;
//					if (electronicEquip == false) {
//						deleteReq.setSectionId("39");
//						deleteElectronicEquipDetails(deleteReq);
//					}
		//
//					boolean employeeLiability = req.getSectionIds().stream().filter(o -> o.equalsIgnoreCase("45"))
//							.collect(Collectors.toList()).size() > 0 ? true : false;
//					if (employeeLiability == false) {
//						deleteReq.setSectionId("45");
//						deleteEmpLiabilityDetails(deleteReq);
//					}
		//
//					boolean fidelity = req.getSectionIds().stream().filter(o -> o.equalsIgnoreCase("43"))
//							.collect(Collectors.toList()).size() > 0 ? true : false;
//					if (fidelity == false) {
//						deleteReq.setSectionId("43");
//						deleteSlideFidelityGuarantyDetails(deleteReq);
//					}
		//
//					boolean money = req.getSectionIds().stream().filter(o -> o.equalsIgnoreCase("42"))
//							.collect(Collectors.toList()).size() > 0 ? true : false;
//					if (money == false) {
//						deleteReq.setSectionId("42");
//						deleteSlideMoneyDetails(deleteReq);
//					}
		//
//					boolean machinery = req.getSectionIds().stream().filter(o -> o.equalsIgnoreCase("41"))
//							.collect(Collectors.toList()).size() > 0 ? true : false;
//					if (machinery == false) {
//						deleteReq.setSectionId("41");
//						deleteSlideMachineryBreakdownDetails(deleteReq);
//					}
		//
//					boolean plateGlass = req.getSectionIds().stream().filter(o -> o.equalsIgnoreCase("53"))
//							.collect(Collectors.toList()).size() > 0 ? true : false;
//					if (plateGlass == false) {
//						deleteReq.setSectionId("53");
//						deleteSlidePlateGlassDetails(deleteReq);
//					}
		//
//					boolean liability = req.getSectionIds().stream().filter(o -> o.equalsIgnoreCase("54"))
//							.collect(Collectors.toList()).size() > 0 ? true : false;
//					if (liability == false) {
//						deleteReq.setSectionId("54");
//						deleteSlidePublicLiablityDetails(deleteReq);
//					}
		//
//					// Delete Policy COver Data
//					List<Integer> sectionIds = new ArrayList<Integer>();
//					req.getSectionIds().forEach(o -> {
//						sectionIds.add(Integer.valueOf(o));
//					});
		//
//					Long coverCount = factorRepo.countByRequestReferenceNoAndProductIdAndSectionIdNotIn(
//							res.getRequestReferenceNo(), Integer.valueOf(res.getProductId()), sectionIds);
//					if (coverCount > 0) {
//						factorRepo.deleteByRequestReferenceNoAndProductIdAndSectionIdNotIn(res.getRequestReferenceNo(),
//								Integer.valueOf(res.getProductId()), sectionIds);
//					}
		//
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Log Details" + e.getMessage());
//					return null;
		//
//				}
		//
//				return res;
//			}
		//*/

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
		//
//			public EserviceBuildingDetails setBuildingRefNo(SlideCommonSaveReq req) {
//				EserviceBuildingDetails saveData = new EserviceBuildingDetails();
//				String refNo = "";
//				try {
		//
//					Integer riskId = StringUtils.isBlank(req.getRiskId()) ? Integer.valueOf(req.getRiskId()) : 1;
//					Date entryDate = new Date();
//					String createdBy = req.getCreatedBy();
		//
//					EserviceBuildingDetails findData = new EserviceBuildingDetails();
//					if (StringUtils.isBlank(req.getRequestReferenceNo())) {
//						// Save
//						// String refShortCode = getListItem(req.getCompanyId(), req.getBranchCode(),
//						// "PRODUCT_SHORT_CODE",req.getProductId());
//						// refNo = refShortCode + "-" + genSeqNoService.generateRefNo();
//						// Generate Seq
//						SequenceGenerateReq generateSeqReq = new SequenceGenerateReq();
//						generateSeqReq.setInsuranceId(req.getCompanyId());
//						generateSeqReq.setProductId(req.getProductId());
//						generateSeqReq.setType("2");
//						generateSeqReq.setTypeDesc("REQUEST_REFERENCE_NO");
//						refNo = genSeqNoService.generateSeqCall(generateSeqReq);
//						// Primary Key
//						saveData.setRiskId(riskId);
//						saveData.setRequestReferenceNo(refNo);
//						saveData.setEntryDate(entryDate);
//						saveData.setCreatedBy(createdBy);
//						saveData.setUpdatedDate(entryDate);
//						saveData.setUpdatedBy(createdBy);
//						saveData.setLocationId(1);
		//
//					} else {
//						// Update
//						refNo = req.getRequestReferenceNo();
//						findData = buildingRepo.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo(),
//								riskId, "0");
		//
//						if (findData != null) {
//							// buildingRepo.delete(findData);
//							entryDate = findData.getEntryDate();
//							createdBy = findData.getCreatedBy();
//							saveData.setQuoteNo(findData.getQuoteNo());
//							saveData.setLocationId(findData.getLocationId());
//						}
		//
//					}
//					saveData.setEntryDate(entryDate);
//					saveData.setCreatedBy(createdBy);
//					saveData.setRiskId(riskId);
//					saveData.setRequestReferenceNo(refNo);
//					saveData.setLocationId(1);
		//
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Log Details" + e.getMessage());
//					return null;
//				}
		//
//				return saveData;
//			}
		//
//			public EserviceBuildingDetails setBuildingPolicyAndEndtDetails(SlideCommonSaveReq req,
//					EserviceBuildingDetails data) {
//				EserviceBuildingDetails saveData = data;
//				try {
//					// Date Differents
//					Date periodStart = req.getPolicyStartDate();
//					Date periodEnd = req.getPolicyEndDate();
//					String diff = "0";
		//
//					if (periodStart != null && periodEnd != null) {
//						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//						String st = sdf.format(req.getPolicyStartDate());
//						String ed = sdf.format(req.getPolicyEndDate());
//						if (st.equalsIgnoreCase(ed) && (req.getEndorsementType() == null || req.getEndorsementType() == 0)) {
//							diff = "1";
//						} else if (st.equalsIgnoreCase(ed)) {
//							diff = "0";
//						} else {
//							Long diffInMillies = Math.abs(periodEnd.getTime() - periodStart.getTime());
//							Long daysBetween = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS) + 1;
		//
//							// Check Leap Year
//							// SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
//							// boolean leapYear = LocalDate.parse(sdf2.format(periodEnd) ).isLeapYear();
////							diff = String.valueOf(daysBetween==364 &&  leapYear==false ? daysBetween+1 : 
////								  daysBetween==364 &&  leapYear==true ? daysBetween+2 : 
////								  daysBetween==365 &&  leapYear==true ? daysBetween+1 : daysBetween );
//							diff = String.valueOf(daysBetween);
//						}
		//
//					}
//					saveData.setPolicyStartDate(periodStart);
//					saveData.setPolicyEndDate(periodEnd);
//					saveData.setPolicyPeriord(Integer.valueOf(diff));
//					saveData.setPromocode(req.getPromocode());
//					saveData.setHavepromocode(req.getHavepromocode());
//					saveData.setCurrency(req.getCurrency());
//					saveData.setExchangeRate(
//							StringUtils.isBlank(req.getExchangeRate()) ? null : new BigDecimal(req.getExchangeRate()));
//					saveData.setCustomerReferenceNo(req.getCustomerReferenceNo());
//					saveData.setProductId(req.getProductId());
//					saveData.setStatus(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus());
//					CompanyProductMaster product = getCompanyProductMasterDropdown(req.getCompanyId(), req.getProductId()); // productRepo.findByProductIdOrderByAmendIdDesc(Integer.valueOf(req.getProductId()));
//					saveData.setProductDesc(product.getProductName());
//					saveData.setUpdatedDate(new Date());
//					saveData.setUpdatedBy(req.getCreatedBy());
//					saveData.setCompanyId(req.getCompanyId());
//					saveData.setIndustryDesc(StringUtils.isBlank(req.getIndustryDesc()) ? null : req.getIndustryDesc());
//					String companyName = getInscompanyMasterDropdown(req.getCompanyId()); // companyRepo.findByCompanyIdOrderByAmendIdDesc(req.getCompanyId());
//					saveData.setCompanyName(companyName);
//					saveData.setBuildingOwnerYn(req.getBuildingOwnerYn());
//					saveData.setDomesticPackageYn("Y");
//					saveData.setCommissionType(req.getCommissionType());
//					if (StringUtils.isNotBlank(req.getIndustryId())) {
		//
//						saveData.setIndustryId(
//								StringUtils.isBlank(req.getIndustryId()) ? null : Integer.valueOf(req.getIndustryId()));
		//
//						IndustryMaster industry = getIndustryName(req.getCompanyId(), req.getProductId(), req.getBranchCode(),
//								req.getIndustryId());
		//
//						saveData.setIndustryDesc(industry != null ? industry.getIndustryName() : "");
//						saveData.setCategoryId(industry != null ? industry.getCategoryId() : "");
//						saveData.setCategoryDesc(industry != null ? industry.getCategoryDesc() : "");
//					}
		//
//					// Endorsement Changes
//					if (!(req.getEndorsementType() == null || req.getEndorsementType() == 0))
		//
//					{
		//
//						saveData.setOriginalPolicyNo(req.getOriginalPolicyNo());
//						saveData.setEndorsementDate(req.getEndorsementDate());
//						saveData.setEndorsementRemarks(req.getEndorsementRemarks());
//						saveData.setEndorsementEffdate(req.getEndorsementEffdate());
//						saveData.setEndtPrevPolicyNo(req.getEndtPrevPolicyNo());
//						saveData.setEndtPrevQuoteNo(req.getEndtPrevQuoteNo());
//						saveData.setEndtCount(req.getEndtCount());
//						saveData.setEndtStatus(req.getEndtStatus());
//						saveData.setIsFinyn(req.getIsFinaceYn());
//						saveData.setEndtCategDesc(req.getEndtCategDesc());
//						saveData.setEndorsementType(req.getEndorsementType());
//						saveData.setEndorsementTypeDesc(req.getEndorsementTypeDesc());
//						saveData.setPolicyNo(req.getPolicyNo());
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Exception Is ---> " + e.getMessage());
//					return null;
//				}
		//
//				return saveData;
//			}
		//
//			public List<BuildingSectionRes> insertBuildingSectionDetails(SlideCommonSaveReq req,
//					EserviceBuildingDetails buildingData) {
//				List<BuildingSectionRes> sectionResList = new ArrayList<BuildingSectionRes>();
//				DozerBeanMapper dozerMapper = new DozerBeanMapper();
//				try {
		//
//					// Building Section Insert
//					Long buildSecCount = eserSecRepo.countByRequestReferenceNoAndRiskId(buildingData.getRequestReferenceNo(),
//							buildingData.getRiskId());
//					if (buildSecCount > 0) {
//						eserSecRepo.deleteByRequestReferenceNoAndRiskId(buildingData.getRequestReferenceNo(),
//								buildingData.getRiskId());
//					}
		//
//					List<ProductSectionMaster> sectionList = getProductSectionDropdown(req.getCompanyId(), req.getProductId());
		//
//					List<EserviceSectionDetails> sectionds = new ArrayList<EserviceSectionDetails>();
//					for (String section : req.getSectionIds()) {
		//
//						EserviceSectionDetails secData = new EserviceSectionDetails();
//						List<ProductSectionMaster> filterSection = sectionList.stream()
//								.filter(o -> o.getSectionId().equals(Integer.valueOf(section))).collect(Collectors.toList());
//						if (filterSection.size() > 0) {
//							ProductSectionMaster sec = filterSection.get(0);
//							dozerMapper.map(buildingData, secData);
//							secData.setExchangeRate(buildingData.getExchangeRate());
//							secData.setCurrencyId(buildingData.getCurrency());
//							secData.setSectionId(section);
//							secData.setSectionName(sec.getSectionName());
//							secData.setRiskId(buildingData.getRiskId());
//							secData.setProductType(sec.getMotorYn());
		//
//							String productTypeDesc = getListItem(req.getCompanyId(), req.getBranchCode(), "PRODUCT_CATEGORY",
//									secData.getProductType());
//							secData.setProductTypeDesc(productTypeDesc);
//							secData.setUserOpt("N");
//							sectionds.add(secData);
		//
//							// Response
//							BuildingSectionRes secRes = new BuildingSectionRes();
//							secRes.setSectionId(section);
//							secRes.setSectionName(sec.getSectionName());
//							secRes.setMotorYn(sec.getMotorYn());
//							sectionResList.add(secRes);
		//
//						}
		//
//					}
//					eserSecRepo.saveAllAndFlush(sectionds);
		//
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Exception Is ---> " + e.getMessage());
		//
//				}
		//
//				return sectionResList;
//			}
		//
//			public EserviceCommonDetails setHumanRefNo(SlideCommonSaveReq req) {
//				EserviceCommonDetails saveData = new EserviceCommonDetails();
//				String refNo = "";
//				try {
		//
//					Integer riskId = StringUtils.isBlank(req.getRiskId()) ? Integer.valueOf(req.getRiskId()) : 1;
//					Date entryDate = new Date();
//					String createdBy = req.getCreatedBy();
		//
//					EserviceCommonDetails findData = new EserviceCommonDetails();
//					if (StringUtils.isBlank(req.getRequestReferenceNo())) {
//						// Save
//						// String refShortCode = getListItem(req.getCompanyId(), req.getBranchCode(),
//						// "PRODUCT_SHORT_CODE",req.getProductId());
//						// refNo = refShortCode + "-" + genSeqNoService.generateRefNo();
//						// Generate Seq
//						SequenceGenerateReq generateSeqReq = new SequenceGenerateReq();
//						generateSeqReq.setInsuranceId(req.getCompanyId());
//						generateSeqReq.setProductId(req.getProductId());
//						generateSeqReq.setType("2");
//						generateSeqReq.setTypeDesc("REQUEST_REFERENCE_NO");
//						refNo = genSeqNoService.generateSeqCall(generateSeqReq);
//						// Primary Key
//						saveData.setRiskId(riskId);
//						saveData.setRequestReferenceNo(refNo);
//						saveData.setEntryDate(entryDate);
//						saveData.setCreatedBy(createdBy);
//						saveData.setUpdatedDate(entryDate);
//						saveData.setUpdatedBy(createdBy);
//						saveData.setLocationId(1);
//					} else {
//						// Update
//						refNo = req.getRequestReferenceNo();
//						findData = humanRepo.findByRequestReferenceNoOrderByRiskIdAsc(req.getRequestReferenceNo()).get(0);
		//
//						if (findData != null) {
//							humanRepo.delete(findData);
//							saveData = findData;
//							saveData.setUpdatedDate(entryDate);
//							saveData.setUpdatedBy(createdBy);
//							saveData.setQuoteNo(findData.getQuoteNo());
//							saveData.setLocationId(findData.getLocationId());
		//
//						}
//					}
//					saveData.setUpdatedDate(entryDate);
//					saveData.setUpdatedBy(createdBy);
//					saveData.setRequestReferenceNo(refNo);
		//
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Log Details" + e.getMessage());
		//
//				}
		//
//				return saveData;
//			}
		//
//			public EserviceCommonDetails setHumanPolicyAndEndtDetails(SlideCommonSaveReq req, EserviceCommonDetails humanData) {
//				EserviceCommonDetails saveData = humanData;
//				try {
//					// Date Differents
//					Date periodStart = req.getPolicyStartDate();
//					Date periodEnd = req.getPolicyEndDate();
//					String diff = "0";
		//
//					if (periodStart != null && periodEnd != null) {
//						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//						String st = sdf.format(req.getPolicyStartDate());
//						String ed = sdf.format(req.getPolicyEndDate());
//						if (st.equalsIgnoreCase(ed) && (req.getEndorsementType() == null || req.getEndorsementType() == 0)) {
//							diff = "1";
//						} else if (st.equalsIgnoreCase(ed)) {
//							diff = "0";
//						} else {
//							Long diffInMillies = Math.abs(periodEnd.getTime() - periodStart.getTime());
//							Long daysBetween = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
		//
//							// Check Leap Year
//							// SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
//							// boolean leapYear = LocalDate.parse(sdf2.format(periodEnd) ).isLeapYear();
////							diff = String.valueOf(daysBetween==364 &&  leapYear==false ? daysBetween+1 : 
////								  daysBetween==364 &&  leapYear==true ? daysBetween+2 : 
////								  daysBetween==365 &&  leapYear==true ? daysBetween+1 : daysBetween );
//							diff = String.valueOf(daysBetween);
//						}
		//
//					}
//					saveData.setPolicyStartDate(periodStart);
//					saveData.setPolicyEndDate(periodEnd);
//					saveData.setPolicyPeriod(Integer.valueOf(diff));
//					saveData.setPromocode(req.getPromocode());
//					saveData.setHavepromocode(req.getHavepromocode());
//					saveData.setCurrency(req.getCurrency());
//					saveData.setExchangeRate(
//							StringUtils.isBlank(req.getExchangeRate()) ? null : new BigDecimal(req.getExchangeRate()));
//					saveData.setCustomerReferenceNo(req.getCustomerReferenceNo());
//					saveData.setProductId(req.getProductId());
//					saveData.setStatus(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus());
//					CompanyProductMaster product = getCompanyProductMasterDropdown(req.getCompanyId(), req.getProductId()); // productRepo.findByProductIdOrderByAmendIdDesc(Integer.valueOf(req.getProductId()));
//					saveData.setProductDesc(product.getProductName());
//					saveData.setSectionId(req.getSectionIds().get(0));
//					saveData.setCompanyId(req.getCompanyId());
//					String companyName = getInscompanyMasterDropdown(req.getCompanyId()); // companyRepo.findByCompanyIdOrderByAmendIdDesc(req.getCompanyId());
//					saveData.setCompanyName(companyName);
//					if (StringUtils.isNotBlank(req.getIndustryId())) {
		//
//						saveData.setIndustryId(StringUtils.isBlank(req.getIndustryId()) ? null : req.getIndustryId());
//						IndustryMaster industry = getIndustryName(req.getCompanyId(), req.getProductId(), req.getBranchCode(),
//								req.getIndustryId());
//						saveData.setIndustryName(industry != null ? industry.getIndustryName() : "");
		//
//					}
//					// Endorsement Changes
//					if (!(req.getEndorsementType() == null || req.getEndorsementType() == 0))
		//
//					{
		//
//						saveData.setOriginalPolicyNo(req.getOriginalPolicyNo());
//						saveData.setEndorsementDate(req.getEndorsementDate());
//						saveData.setEndorsementRemarks(req.getEndorsementRemarks());
//						saveData.setEndorsementEffdate(req.getEndorsementEffdate());
//						saveData.setEndtPrevPolicyNo(req.getEndtPrevPolicyNo());
//						saveData.setEndtPrevQuoteNo(req.getEndtPrevQuoteNo());
//						saveData.setEndtCount(req.getEndtCount());
//						saveData.setEndtStatus(req.getEndtStatus());
//						saveData.setIsFinyn(req.getIsFinaceYn());
//						saveData.setEndtCategDesc(req.getEndtCategDesc());
//						saveData.setEndorsementType(req.getEndorsementType());
//						saveData.setEndorsementTypeDesc(req.getEndorsementTypeDesc());
//						saveData.setPolicyNo(req.getPolicyNo());
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Exception Is ---> " + e.getMessage());
//					return null;
//				}
		//
//				return saveData;
//			}
		//
//			public List<BuildingSectionRes> insertHumanSectionDetails(SlideCommonSaveReq req, EserviceCommonDetails humanData) {
//				List<BuildingSectionRes> sectionResList = new ArrayList<BuildingSectionRes>();
//				DozerBeanMapper dozerMapper = new DozerBeanMapper();
//				try {
		//
//					// Building Section Insert
//					Long buildSecCount = eserSecRepo.countByRequestReferenceNoAndRiskId(humanData.getRequestReferenceNo(), 1);
//					if (buildSecCount > 0) {
//						eserSecRepo.deleteByRequestReferenceNoAndRiskId(humanData.getRequestReferenceNo(), 1);
//					}
		//
//					List<ProductSectionMaster> sectionList = getProductSectionDropdown(req.getCompanyId(), req.getProductId());
		//
//					List<EserviceSectionDetails> sectionds = new ArrayList<EserviceSectionDetails>();
//					for (String section : req.getSectionIds()) {
		//
//						EserviceSectionDetails secData = new EserviceSectionDetails();
//						List<ProductSectionMaster> filterSection = sectionList.stream()
//								.filter(o -> o.getSectionId().equals(Integer.valueOf(section))).collect(Collectors.toList());
//						if (filterSection.size() > 0) {
//							ProductSectionMaster sec = filterSection.get(0);
//							dozerMapper.map(humanData, secData);
//							secData.setExchangeRate(humanData.getExchangeRate());
//							secData.setCurrencyId(humanData.getCurrency());
//							secData.setSectionId(section);
//							secData.setSectionName(sec.getSectionName());
//							secData.setRiskId(1);
//							secData.setUserOpt("N");
//							secData.setProductType(sec.getMotorYn());
//							secData.setLocationId(1);
//							String productTypeDesc = getListItem(req.getCompanyId(), req.getBranchCode(), "PRODUCT_CATEGORY",
//									secData.getProductType());
//							secData.setProductTypeDesc(productTypeDesc);
//							sectionds.add(secData);
		//
//							// Response
//							BuildingSectionRes secRes = new BuildingSectionRes();
//							secRes.setSectionId(section);
//							secRes.setSectionName(sec.getSectionName());
//							secRes.setMotorYn(sec.getMotorYn());
//							sectionResList.add(secRes);
		//
//						}
		//
//					}
//					eserSecRepo.saveAllAndFlush(sectionds);
		//
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Exception Is ---> " + e.getMessage());
		//
//				}
		//
//				return sectionResList;
//			}
		//
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
		//
//			public EserviceBuildingDetails setBuildingBrokerDetails(SlideCommonSaveReq req, EserviceBuildingDetails data) {
//				EserviceBuildingDetails saveData = data;
//				try {
//					// Source Type Details
//					BranchMaster branchData = getBranchMasterRes(req.getCompanyId(), req.getBranchCode());
//					saveData.setBranchCode(req.getBranchCode());
//					saveData.setBranchName(branchData.getBranchName());
//					saveData.setApplicationId(StringUtils.isBlank(req.getApplicationId()) ? "1" : req.getApplicationId());
		//
//					// Source Type Search Condition
//					List<String> directSource = new ArrayList<String>();
//					directSource.add("1");
//					directSource.add("2");
//					directSource.add("3");
		//
//					List<ListItemValue> sourcerTypes = premiaBrokerService.getSourceTypeDropdown(req.getCompanyId(),
//							req.getBranchCode(), "SOURCE_TYPE");
//					List<ListItemValue> filterSource = sourcerTypes.stream()
//							.filter(o -> StringUtils.isNotBlank(req.getSourceTypeId())
//									&& (o.getItemCode().equalsIgnoreCase(req.getSourceTypeId())
//											|| o.getItemValue().equalsIgnoreCase(req.getSourceTypeId())))
//							.collect(Collectors.toList());
		//
//					if (filterSource.size() > 0 && directSource.contains(filterSource.get(0).getItemCode())) {
		//
//						String sourceType = filterSource.get(0).getItemValue();
//						String subUserType = sourceType.contains("Broker") ? "Broker"
//								: sourceType.contains("Agent") ? "Agent" : "";
//						LoginMaster premiaLogin = getPremiaBroker(req.getBdmCode(), subUserType, req.getCompanyId());
//						String brokerLoginId = premiaLogin != null ? premiaLogin.getLoginId() : branchData.getDirectBrokerId();
		//
//						LoginUserInfo premiaUser = loginUserRepo.findByLoginId(brokerLoginId);
//						LoginUserInfo loginUserData = premiaUser != null ? premiaUser
//								: loginUserRepo.findByLoginId(branchData.getDirectBrokerId());
		//
//						LoginBranchMaster premiaBranch = lbranchRepo.findByLoginIdAndBranchCodeAndCompanyId(brokerLoginId,
//								req.getBranchCode(), req.getCompanyId());
//						LoginBranchMaster brokerBranch = premiaBranch != null ? premiaBranch
//								: lbranchRepo.findByLoginIdAndBranchCodeAndCompanyId(branchData.getDirectBrokerId(),
//										req.getBranchCode(), req.getCompanyId());
		//
//						saveData.setBrokerCode(loginUserData.getOaCode());
//						saveData.setAgencyCode(loginUserData.getAgencyCode());
//						saveData.setLoginId(loginUserData.getLoginId());
//						saveData.setCustomerCode(req.getBdmCode());
//						saveData.setCustomerName(req.getCustomerName());
//						saveData.setBdmCode(req.getBdmCode());
//						saveData.setBrokerBranchCode(brokerBranch.getBrokerBranchCode());
//						saveData.setBrokerBranchName(brokerBranch.getBrokerBranchName());
//						saveData.setSourceTypeId(filterSource.get(0).getItemCode());
//						saveData.setSourceType(filterSource.get(0).getItemValue());
		//
//						// Direct Source Type
//						if (filterSource.get(0).getItemValue().contains("Direct")
//								|| (premiaLogin != null && premiaUser != null && premiaBranch != null)) {
//							saveData.setSalePointCode(brokerBranch.getSalePointCode());
//							saveData.setBrokerTiraCode(loginUserData.getRegulatoryCode());
//						} else {
//							try {
//								// Broker Tira Code
//								PremiaTiraReq brokerTiraCodeReq = new PremiaTiraReq();
//								brokerTiraCodeReq.setInsuranceId(req.getCompanyId());
//								brokerTiraCodeReq.setPremiaCode(req.getCustomerCode());
//								List<PremiaTiraRes> brokerTira = premiaBrokerService
//										.searchPremiaBrokerTiraCode(brokerTiraCodeReq);
//								String brokerTiraCode = "";
//								if (brokerTira.size() > 0) {
//									brokerTiraCode = brokerTira.get(0).getTiraCode();
//								}
		//
//								// Sale Point Code
//								PremiaTiraReq brokerSpCodeReq = new PremiaTiraReq();
//								brokerSpCodeReq.setInsuranceId(req.getCompanyId());
//								brokerSpCodeReq.setPremiaCode(brokerTiraCode);
//								List<PremiaTiraRes> brokerSp = premiaBrokerService.searchPremiaBrokerSpCode(brokerSpCodeReq);
//								String brokerSpCode = "";
//								if (brokerSp.size() > 0) {
//									brokerSpCode = brokerSp.get(0).getTiraCode();
//								}
		//
//								saveData.setSalePointCode(brokerSpCode);
//								saveData.setBrokerTiraCode(brokerTiraCode);
//							} catch (Exception e) {
//								e.printStackTrace();
//								log.info("Log Details" + e.getMessage());
		//
//							}
//						}
		//
//					} else {
//						LoginUserInfo loginUserData = loginUserRepo.findByLoginId(req.getLoginId());
//						LoginBranchMaster brokerBranch = lbranchRepo.findByLoginIdAndBrokerBranchCodeAndCompanyId(
//								req.getLoginId(), req.getBrokerBranchCode(), req.getCompanyId());
//						LoginMaster loginData = loginRepo.findByLoginId(req.getLoginId());
		//
//						saveData.setBrokerCode(loginData.getOaCode());
//						saveData.setAgencyCode(loginData.getAgencyCode());
//						saveData.setCustomerCode(loginUserData.getCustomerCode());
//						saveData.setLoginId(req.getLoginId());
//						saveData.setCustomerName(loginUserData.getCustomerName());
//						saveData.setBdmCode(null);
//						saveData.setBrokerBranchCode(brokerBranch.getBrokerBranchCode());
//						saveData.setBrokerBranchName(brokerBranch.getBrokerBranchName());
//						saveData.setSalePointCode(brokerBranch.getSalePointCode());
//						saveData.setBrokerTiraCode(loginUserData.getRegulatoryCode());
//						List<ListItemValue> filterBrokerSource = sourcerTypes.stream()
//								.filter(o -> o.getItemValue().equalsIgnoreCase(loginData.getSubUserType()))
//								.collect(Collectors.toList());
//						saveData.setSourceTypeId(filterBrokerSource.size() > 0 ? filterBrokerSource.get(0).getItemCode() : "");
//						saveData.setSourceType(filterBrokerSource.size() > 0 ? filterBrokerSource.get(0).getItemValue()
//								: loginData.getSubUserType());
		//
//					}
//					if ("1".equalsIgnoreCase(req.getApplicationId())) {
//						saveData.setSubUserType(saveData.getSourceType());
//					} else {
//						LoginMaster issuerData = loginRepo.findByLoginId(req.getApplicationId());
//						saveData.setSubUserType(issuerData != null ? issuerData.getSubUserType() : saveData.getSourceType());
//					}
//					saveData.setSubUserType(saveData.getSourceType());
		//
//					// Broker Commission
//					Double commissionPercent = 0D;
//					if (filterSource.size() > 0 && directSource.contains(filterSource.get(0).getItemCode())) {
//						// commissionPercent=12.5;
//						String sourceType = filterSource.get(0).getItemValue();
//						String subUserType = sourceType.contains("Broker") ? "Broker"
//								: sourceType.contains("Agent") ? "Agent" : "";
//						LoginMaster premiaLogin = getPremiaBroker(req.getBdmCode(), subUserType, req.getCompanyId());
//						String premiaLoginId = premiaLogin != null ? premiaLogin.getLoginId() : "";
		//
//						String commission = getListItem(req.getCompanyId(), req.getBranchCode(), "COMMISSION_PERCENT",
//								filterSource.get(0).getItemValue());
//						if (StringUtils.isNotBlank(premiaLoginId)) {
//							List<BrokerCommissionDetails> commissionList = getPolicyName(req.getCompanyId(), req.getProductId(),
//									premiaLoginId, req.getBrokerCode(), "99999", req.getUserType());
//							commission = commissionList.size() > 0 && commissionList.get(0).getCommissionPercentage() != null
//									? commissionList.get(0).getCommissionPercentage().toString()
//									: commission;
//						}
//						commissionPercent = StringUtils.isNotBlank(commission) ? Double.valueOf(commission) : 0D;
//						saveData.setCommissionPercentage(
//								commissionPercent == null ? new BigDecimal("0") : new BigDecimal(commissionPercent));
		//
//					} else {
//						String loginId = StringUtils.isNotBlank(req.getSourceType())
//								&& req.getSourceType().toLowerCase().contains("b2c") ? "guest" : req.getLoginId();
//						List<BrokerCommissionDetails> commissionList = getPolicyName(req.getCompanyId(), req.getProductId(),
//								loginId, req.getBrokerCode(), "99999", req.getUserType());
//						if (commissionList != null && commissionList.size() > 0) {
//							BrokerCommissionDetails comm = commissionList.get(0);
//							saveData.setCommissionPercentage(comm.getCommissionPercentage() == null ? new BigDecimal("0")
//									: new BigDecimal(comm.getCommissionPercentage()));
//							saveData.setVatCommission(comm.getCommissionVatPercent() == null ? new BigDecimal("0")
//									: new BigDecimal(comm.getCommissionVatPercent()));
//						} else {
//							saveData.setCommissionPercentage(new BigDecimal("0"));
//							saveData.setVatCommission(new BigDecimal("0"));
//						}
//					}
		//
//					// Endt Commission
//					if (!(req.getEndorsementType() == null || req.getEndorsementType() == 0))
		//
//					{
//						List<BuildingRiskDetails> mainMot = motBuildingRepo.findByQuoteNo(req.getEndtPrevQuoteNo());
//						for (BuildingRiskDetails mot : mainMot) {
//							saveData.setCommissionPercentage(
//									mot.getCommissionPercentage() == null ? saveData.getCommissionPercentage()
//											: mot.getCommissionPercentage());
//							saveData.setVatCommission(
//									mot.getVatCommission() == null ? saveData.getVatCommission() : mot.getVatCommission());
//						}
		//
//					}
		//
//					saveData.setTiraCoverNoteNo(req.getTiraCoverNoteNo());
//					saveData.setBankCode(req.getBankCode());
//					saveData.setAcExecutiveId(
//							StringUtils.isBlank(req.getAcExecutiveId()) ? null : Integer.valueOf(req.getAcExecutiveId()));
//					if (StringUtils.isNotBlank(req.getCommissionType())) {
//						String commistionDesc = getListItem(req.getCompanyId(), req.getBranchCode(), "COMMISSION_TYPE",
//								req.getCommissionType());
//						saveData.setCommissionTypeDesc(commistionDesc);
//					}
		//
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Exception Is ---> " + e.getMessage());
//					return null;
//				}
		//
//				return saveData;
//			}
		//
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

//			public EserviceCommonDetails setHumanBrokerDetails(SlideCommonSaveReq req, EserviceCommonDetails humanData) {
//				EserviceCommonDetails saveData = humanData;
//				try {
//					// Source Type Details
//					BranchMaster branchData = getBranchMasterRes(req.getCompanyId(), req.getBranchCode());
//					saveData.setBranchCode(req.getBranchCode());
//					saveData.setBranchName(branchData.getBranchName());
//					saveData.setApplicationId(StringUtils.isBlank(req.getApplicationId()) ? "1" : req.getApplicationId());
		//
//					// Source Type Search Condition
//					List<String> directSource = new ArrayList<String>();
//					directSource.add("1");
//					directSource.add("2");
//					directSource.add("3");
		//
//					List<ListItemValue> sourcerTypes = premiaBrokerService.getSourceTypeDropdown(req.getCompanyId(),
//							req.getBranchCode(), "SOURCE_TYPE");
//					List<ListItemValue> filterSource = sourcerTypes.stream()
//							.filter(o -> StringUtils.isNotBlank(req.getSourceTypeId())
//									&& (o.getItemCode().equalsIgnoreCase(req.getSourceTypeId())
//											|| o.getItemValue().equalsIgnoreCase(req.getSourceTypeId())))
//							.collect(Collectors.toList());
		//
//					if (filterSource.size() > 0 && directSource.contains(filterSource.get(0).getItemCode())) {
		//
//						String sourceType = filterSource.get(0).getItemValue();
//						String subUserType = sourceType.contains("Broker") ? "Broker"
//								: sourceType.contains("Agent") ? "Agent" : "";
//						LoginMaster premiaLogin = getPremiaBroker(req.getBdmCode(), subUserType, req.getCompanyId());
//						String premiaLoginId = premiaLogin != null ? premiaLogin.getLoginId() : "";
		//
//						LoginUserInfo premiaUser = loginUserRepo.findByLoginId(premiaLoginId);
//						LoginUserInfo loginUserData = premiaUser != null ? premiaUser
//								: loginUserRepo.findByLoginId(branchData.getDirectBrokerId());
		//
//						LoginBranchMaster premiaBranch = lbranchRepo.findByLoginIdAndBranchCodeAndCompanyId(premiaLoginId,
//								req.getBranchCode(), req.getCompanyId());
//						LoginBranchMaster brokerBranch = premiaBranch != null ? premiaBranch
//								: lbranchRepo.findByLoginIdAndBranchCodeAndCompanyId(branchData.getDirectBrokerId(),
//										req.getBranchCode(), req.getCompanyId());
		//
//						saveData.setBrokerCode(loginUserData.getOaCode());
//						saveData.setAgencyCode(loginUserData.getAgencyCode());
//						saveData.setLoginId(loginUserData.getLoginId());
//						saveData.setCustomerCode(req.getBdmCode());
//						saveData.setCustomerName(req.getCustomerName());
//						saveData.setBdmCode(req.getBdmCode());
//						saveData.setBrokerBranchCode(brokerBranch.getBrokerBranchCode());
//						saveData.setBrokerBranchName(brokerBranch.getBrokerBranchName());
//						saveData.setSourceTypeId(filterSource.get(0).getItemCode());
//						saveData.setSourceType(filterSource.get(0).getItemValue());
//						saveData.setLocationId(1);
//						if (filterSource.get(0).getItemValue().contains("Direct")
//								|| (premiaLogin != null && premiaUser != null && premiaBranch != null)) {
//							saveData.setSalePointCode(brokerBranch.getSalePointCode());
//							saveData.setBrokerTiraCode(loginUserData.getRegulatoryCode());
//						} else {
//							try {
//								// Broker Tira Code
//								PremiaTiraReq brokerTiraCodeReq = new PremiaTiraReq();
//								brokerTiraCodeReq.setInsuranceId(req.getCompanyId());
//								brokerTiraCodeReq.setPremiaCode(req.getCustomerCode());
//								List<PremiaTiraRes> brokerTira = premiaBrokerService
//										.searchPremiaBrokerTiraCode(brokerTiraCodeReq);
//								String brokerTiraCode = "";
//								if (brokerTira.size() > 0) {
//									brokerTiraCode = brokerTira.get(0).getTiraCode();
//								}
		//
//								// Sale Point Code
//								PremiaTiraReq brokerSpCodeReq = new PremiaTiraReq();
//								brokerSpCodeReq.setInsuranceId(req.getCompanyId());
//								brokerSpCodeReq.setPremiaCode(brokerTiraCode);
//								List<PremiaTiraRes> brokerSp = premiaBrokerService.searchPremiaBrokerSpCode(brokerSpCodeReq);
//								String brokerSpCode = "";
//								if (brokerSp.size() > 0) {
//									brokerSpCode = brokerSp.get(0).getTiraCode();
//								}
		//
//								saveData.setSalePointCode(brokerSpCode);
//								saveData.setBrokerTiraCode(brokerTiraCode);
//							} catch (Exception e) {
//								e.printStackTrace();
//								log.info("Log Details" + e.getMessage());
		//
//							}
//						}
		//
//					} else {
//						LoginUserInfo loginUserData = loginUserRepo.findByLoginId(req.getLoginId());
//						LoginBranchMaster brokerBranch = lbranchRepo.findByLoginIdAndBrokerBranchCodeAndCompanyId(
//								req.getLoginId(), req.getBrokerBranchCode(), req.getCompanyId());
//						LoginMaster loginData = loginRepo.findByLoginId(req.getLoginId());
		//
//						saveData.setBrokerCode(loginData.getOaCode());
//						saveData.setAgencyCode(loginData.getAgencyCode());
//						saveData.setCustomerCode(loginUserData.getCustomerCode());
//						saveData.setLoginId(req.getLoginId());
//						saveData.setCustomerName(loginUserData.getCustomerName());
//						saveData.setBdmCode(null);
//						saveData.setBrokerBranchCode(brokerBranch.getBrokerBranchCode());
//						saveData.setBrokerBranchName(brokerBranch.getBrokerBranchName());
//						saveData.setSalePointCode(brokerBranch.getSalePointCode());
//						saveData.setBrokerTiraCode(loginUserData.getRegulatoryCode());
		//
//						List<ListItemValue> filterBrokerSource = sourcerTypes.stream()
//								.filter(o -> o.getItemValue().equalsIgnoreCase(loginData.getSubUserType()))
//								.collect(Collectors.toList());
		//
//						saveData.setSourceTypeId(filterBrokerSource.size() > 0 ? filterBrokerSource.get(0).getItemCode() : "");
//						saveData.setSourceType(filterBrokerSource.size() > 0 ? filterBrokerSource.get(0).getItemValue()
//								: loginData.getSubUserType());
		//
//					}
//					if ("1".equalsIgnoreCase(req.getApplicationId())) {
//						saveData.setSubUserType(saveData.getSourceType());
//					} else {
//						LoginMaster issuerData = loginRepo.findByLoginId(req.getApplicationId());
//						saveData.setSubUserType(issuerData != null ? issuerData.getSubUserType() : saveData.getSourceType());
//					}
		//
//					// Broker Commission
//					Double commissionPercent = 0D;
//					if (filterSource.size() > 0 && directSource.contains(filterSource.get(0).getItemCode())) {
//						// commissionPercent=12.5;
//						String sourceType = filterSource.get(0).getItemValue();
//						String subUserType = sourceType.contains("Broker") ? "Broker"
//								: sourceType.contains("Agent") ? "Agent" : "";
//						LoginMaster premiaLogin = getPremiaBroker(req.getBdmCode(), subUserType, req.getCompanyId());
//						String premiaLoginId = premiaLogin != null ? premiaLogin.getLoginId() : "";
		//
//						String commission = getListItem(req.getCompanyId(), req.getBranchCode(), "COMMISSION_PERCENT",
//								filterSource.get(0).getItemValue());
//						if (StringUtils.isNotBlank(premiaLoginId)) {
//							List<BrokerCommissionDetails> commissionList = getPolicyName(req.getCompanyId(), req.getProductId(),
//									premiaLoginId, req.getBrokerCode(), "99999", req.getUserType());
//							commission = commissionList.size() > 0 && commissionList.get(0).getCommissionPercentage() != null
//									? commissionList.get(0).getCommissionPercentage().toString()
//									: commission;
//						}
//						commissionPercent = StringUtils.isNotBlank(commission) ? Double.valueOf(commission) : 0D;
//						saveData.setCommissionPercentage(
//								commissionPercent == null ? new BigDecimal("0") : new BigDecimal(commissionPercent));
		//
//					} else {
//						String loginId = StringUtils.isNotBlank(req.getSourceType())
//								&& req.getSourceType().toLowerCase().contains("b2c") ? "guest" : req.getLoginId();
//						List<BrokerCommissionDetails> commissionList = getPolicyName(req.getCompanyId(), req.getProductId(),
//								loginId, req.getBrokerCode(), "99999", req.getUserType());
//						if (commissionList != null && commissionList.size() > 0) {
//							BrokerCommissionDetails comm = commissionList.get(0);
//							saveData.setCommissionPercentage(comm.getCommissionPercentage() == null ? new BigDecimal("0")
//									: new BigDecimal(comm.getCommissionPercentage()));
//							saveData.setVatCommission(comm.getCommissionVatPercent() == null ? new BigDecimal("0")
//									: new BigDecimal(comm.getCommissionVatPercent()));
//						} else {
//							saveData.setCommissionPercentage(new BigDecimal("0"));
//							saveData.setVatCommission(new BigDecimal("0"));
//						}
//					}
//					// Endt Commission
//					if (!(req.getEndorsementType() == null || req.getEndorsementType() == 0))
		//
//					{
//						List<CommonDataDetails> mainMotList = commonRepo.findByQuoteNo(req.getEndtPrevQuoteNo());
//						if (mainMotList != null && mainMotList.size() > 0) {
//							CommonDataDetails mainMot = mainMotList.get(0);
//							saveData.setCommissionPercentage(
//									mainMot.getCommissionPercentage() == null ? saveData.getCommissionPercentage()
//											: new BigDecimal(mainMot.getCommissionPercentage()));
//							saveData.setVatCommission(mainMot.getVatCommission() == null ? saveData.getVatCommission()
//									: new BigDecimal(mainMot.getVatCommission()));
//						}
//					}
		//
//					saveData.setTiraCoverNoteNo(req.getTiraCoverNoteNo());
//					saveData.setBankCode(req.getBankCode());
//					saveData.setAcExecutiveId(StringUtils.isBlank(req.getAcExecutiveId()) ? null : req.getAcExecutiveId());
//					// Status
//					saveData.setStatus(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus());
//					saveData.setLocationId(1);
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Exception Is ---> " + e.getMessage());
//					return null;
//				}
		//
//				return saveData;
//			}
		//
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


		/*	@Override
			public List<SlideSectionSaveRes> saveEmpLiabilityDetails(List<SlideEmpLiabilitySaveReq> reqList) {
				// TODO Auto-generated method stub
				List<SlideSectionSaveRes> resList = new ArrayList<SlideSectionSaveRes>();
				EserviceBuildingDetails saveData = new EserviceBuildingDetails();
				List<EserviceCommonDetails> humanDatas = new ArrayList<EserviceCommonDetails>();
				EserviceCommonDetails humanData = new EserviceCommonDetails();
				EserviceSectionDetails sectionData = new EserviceSectionDetails();
				DozerBeanMapper dozerMapper = new DozerBeanMapper();
				List<BuildingSectionRes> sectionList = new ArrayList<BuildingSectionRes>();

				try {
					BigDecimal endtCount = BigDecimal.ZERO;
					String refNo = reqList.get(0).getRequestReferenceNo();
					String productId = reqList.get(0).getProductId();
					String sectionId = reqList.get(0).getSectionId();

					saveData = buildingRepo.findByRequestReferenceNoAndRiskIdAndSectionId(refNo, 1, "0");
					if (saveData != null) {

						dozerMapper.map(saveData, humanData);
						humanData.setPolicyPeriod(saveData.getPolicyPeriord());

					}
					humanDatas = humanRepo.findByRequestReferenceNoAndSectionId(refNo, sectionId);
					SlideEmpLiabilitySaveReq firstRow = reqList.get(0);
					boolean hasAllNulls = humanDatas.stream().allMatch(a -> a == null);

					if ((firstRow.getEndorsementType() == null || firstRow.getEndorsementType() == 0) && humanDatas.size() > 0
							|| !humanDatas.isEmpty() || !hasAllNulls) {
						humanRepo.deleteAll(humanDatas);
						dozerMapper.map(humanDatas.get(0), humanData);
					} else if (humanDatas.size() > 0) {
						List<EserviceCommonDetails> humanData1 = new ArrayList<EserviceCommonDetails>();
						if (!(firstRow.getEndorsementType() == null || firstRow.getEndorsementType() == 0)) {

							// search
							if (reqList.get(0).getEndtCount().compareTo(BigDecimal.ONE) == 0) {
								endtCount = reqList.get(0).getEndtCount();
								humanData1 = humanRepo.findBySectionIdAndPolicyNoAndStatusNot(sectionId,
										reqList.get(0).getOriginalPolicyNo(), "D");
							} else {
								endtCount = reqList.get(0).getEndtCount().subtract(BigDecimal.ONE);
								humanData1 = humanRepo.findByEndtCountAndSectionIdAndOriginalPolicyNoAndStatusNot(endtCount,
										sectionId, reqList.get(0).getOriginalPolicyNo(), "D");
							}

							// delete all
							humanRepo.deleteAll(humanDatas);

						}
						List<EserviceCommonDetails> humanDatas2 = new ArrayList<EserviceCommonDetails>();
						dozerMapper.map(humanDatas.get(0), humanData);
						String policyNo = humanDatas.get(0).getPolicyNo();
						humanData1.forEach(hum -> {
							EserviceCommonDetails o = new EserviceCommonDetails();
							dozerMapper.map(hum, o);
							o.setStatus("D");
							o.setRequestReferenceNo(refNo);
							o.setQuoteNo("");
							o.setPolicyNo(policyNo);
							if (!(firstRow.getEndorsementType() == null || firstRow.getEndorsementType() == 0))

							{

								o.setOriginalPolicyNo(firstRow.getOriginalPolicyNo());
								o.setEndorsementDate(firstRow.getEndorsementDate());
								o.setEndorsementRemarks(firstRow.getEndorsementRemarks());
								o.setEndorsementEffdate(firstRow.getEndorsementEffdate());
								o.setEndtPrevPolicyNo(firstRow.getEndtPrevPolicyNo());
								o.setEndtPrevQuoteNo(firstRow.getEndtPrevQuoteNo());
								o.setEndtCount(firstRow.getEndtCount());
								o.setEndtStatus(firstRow.getEndtStatus());
								o.setIsFinyn(firstRow.getIsFinaceYn());
								o.setEndtCategDesc(firstRow.getEndtCategDesc());
								o.setEndorsementType(firstRow.getEndorsementType());
								o.setEndorsementTypeDesc(firstRow.getEndorsementTypeDesc());

							}

							humanDatas2.add(o);
						});
						humanRepo.saveAllAndFlush(humanDatas2);

					}

					sectionData = eserSecRepo.findByRequestReferenceNoAndRiskIdAndProductIdAndSectionId(refNo, 1, productId,
							sectionId);

					List<EserviceCommonDetails> saveHumanList = new ArrayList<EserviceCommonDetails>();
					for (SlideEmpLiabilitySaveReq data : reqList) {
						// Save Human
						EserviceCommonDetails saveHuman = new EserviceCommonDetails();
						saveHuman = dozerMapper.map(humanData, EserviceCommonDetails.class);
						saveHuman.setRequestReferenceNo(data.getRequestReferenceNo());
						// saveHuman.setRiskId(Integer.valueOf(data.getLiabilityOccupationId())) ;
						saveHuman.setRiskId(data.getOriginalRiskId() == null || StringUtils.isBlank(data.getOriginalRiskId())
								? Integer.valueOf(data.getRiskId())
								: Integer.valueOf(data.getOriginalRiskId()));
						saveHuman.setOriginalRiskId(
								data.getOriginalRiskId() == null || StringUtils.isBlank(data.getOriginalRiskId())
										? Integer.valueOf(data.getRiskId())
										: Integer.valueOf(data.getOriginalRiskId()));
						saveHuman.setSectionId(data.getSectionId());
						saveHuman.setSectionName(sectionData != null ? sectionData.getSectionName() : "");
						saveHuman.setCreatedBy(data.getCreatedBy());
						saveHuman.setEmpLiabilitySi(StringUtils.isBlank(data.getEmpLiabilitySi()) ? new BigDecimal(0)
								: new BigDecimal(data.getEmpLiabilitySi()));
						saveHuman.setSumInsured(StringUtils.isBlank(data.getEmpLiabilitySi()) ? new BigDecimal(0)
								: new BigDecimal(data.getEmpLiabilitySi()));
						// Personal Liability
						if (StringUtils.isNotBlank(data.getLiabilityOccupationId())) {
							OccupationMaster occupationData = getOccupationMasterDropdown(data.getInsuranceId(), "99999",
									data.getProductId(), data.getLiabilityOccupationId());
							saveHuman.setOccupationType(occupationData.getOccupationId().toString());
							saveHuman.setOccupationDesc(occupationData.getOccupationName());
							saveHuman.setCategoryId(occupationData.getCategoryId());
							// saveHuman.setCategoryDesc("Class " + occupationData.getCategoryId());
						}

						saveHuman.setOtherOccupation(data.getOtherOccupation());
						saveHuman.setTotalNoOfEmployees(StringUtils.isBlank(data.getTotalNoOfEmployees()) ? null
								: Long.valueOf(data.getTotalNoOfEmployees()));
						saveHuman.setCount(StringUtils.isBlank(data.getTotalNoOfEmployees()) ? null
								: Integer.valueOf(data.getTotalNoOfEmployees()));
						// Lc Calculation
						BigDecimal exchangeRate = humanData.getExchangeRate() != null ? humanData.getExchangeRate()
								: BigDecimal.ZERO;
						saveHuman.setSumInsuredLc(
								saveHuman.getSumInsured() == null ? null : saveHuman.getSumInsured().multiply(exchangeRate));
						saveHuman.setLiabilitySiLc(
								saveHuman.getLiabilitySi() == null ? null : saveHuman.getLiabilitySi().multiply(exchangeRate));
						saveHuman.setFidEmpSiLc(
								saveHuman.getFidEmpSi() == null ? null : saveHuman.getFidEmpSi().multiply(exchangeRate));
						saveHuman.setEmpLiabilitySiLc(saveHuman.getEmpLiabilitySi() == null ? null
								: saveHuman.getEmpLiabilitySi().multiply(exchangeRate));
						saveHuman.setPersonalLiabilitySiLc(saveHuman.getPersonalLiabilitySi() == null ? null
								: saveHuman.getPersonalLiabilitySi().multiply(exchangeRate));
						if (data.getSectionId().equals("106")) {
							saveHuman.setDomesticServentSi(StringUtils.isBlank(data.getEmpLiabilitySi()) ? new BigDecimal(0)
									: new BigDecimal(data.getEmpLiabilitySi()));
							saveHuman.setDomesticServentSiLc(saveHuman.getDomesticServentSi() == null ? null
									: saveHuman.getDomesticServentSi().multiply(exchangeRate));

						}
						// Endorsement CHanges
						if (!(data.getEndorsementType() == null || data.getEndorsementType() == 0))

						{

							saveHuman.setOriginalPolicyNo(data.getOriginalPolicyNo());
							saveHuman.setEndorsementDate(data.getEndorsementDate());
							saveHuman.setEndorsementRemarks(data.getEndorsementRemarks());
							saveHuman.setEndorsementEffdate(data.getEndorsementEffdate());
							saveHuman.setEndtPrevPolicyNo(data.getEndtPrevPolicyNo());
							saveHuman.setEndtPrevQuoteNo(data.getEndtPrevQuoteNo());
							saveHuman.setEndtCount(data.getEndtCount());
							saveHuman.setEndtStatus(data.getEndtStatus());
							saveHuman.setIsFinyn(data.getIsFinaceYn());
							saveHuman.setEndtCategDesc(data.getEndtCategDesc());
							saveHuman.setEndorsementType(data.getEndorsementType());
							saveHuman.setEndorsementTypeDesc(data.getEndorsementTypeDesc());
							saveHuman.setStatus("E");
						}
						saveHumanList.add(saveHuman);

					}
					humanRepo.saveAllAndFlush(saveHumanList);
					if ("14".equalsIgnoreCase(productId)) {
						updatesectionDetails(refNo);
					}

					List<EserviceCommonDetails> esercommonDatas = eserCommonRepo
							.findByRequestReferenceNoAndSectionId(humanData.getRequestReferenceNo(), humanData.getSectionId());
					if (esercommonDatas.size() == 0) {
						esercommonDatas = saveHumanList;
					}
					for (EserviceCommonDetails data : esercommonDatas) {
						// Section Req
						BuildingSectionRes sec = new BuildingSectionRes();
						sec.setMotorYn(sectionData.getProductType());
						sec.setSectionId(sectionData.getSectionId());
						sec.setSectionName(sectionData.getSectionName());
						sec.setOccupationId(data.getOccupationType());
						sec.setOriginalRiskId(data.getOriginalRiskId() != null ? data.getOriginalRiskId() : 0);
						sectionList.add(sec);
					}
				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());

				}
				try {
					List<OneTimeTableRes> otResList = null;
					// One Time Table Thread Call
					OneTimeTableReq otReq = new OneTimeTableReq();
					SlideEmpLiabilitySaveReq req = reqList.get(0);
					otReq.setRequestReferenceNo(req.getRequestReferenceNo());
					// otReq.setVehicleId(1);
					otReq.setBranchCode(saveData != null ? saveData.getBranchCode() : humanData.getBranchCode());
					otReq.setInsuranceId(saveData != null ? saveData.getCompanyId() : humanData.getCompanyId());
					otReq.setProductId(Integer.valueOf(req.getProductId()));
					otReq.setSectionList(sectionList);
					otReq.setLocationId(saveData != null ? saveData.getLocationId() : 1);

					otResList = otService.call_OT_Insert(otReq);
					for (OneTimeTableRes otRes : otResList) {
						SlideSectionSaveRes res = new SlideSectionSaveRes();
						res.setResponse("Saved Successfully");
						res.setRiskId(otRes.getVehicleId());
						res.setVdRefNo(otRes.getVdRefNo());
						res.setCdRefNo(otRes.getCdRefNo());
						res.setMsrefno(otRes.getMsRefNo());
						res.setCompanyId(otRes.getCompanyId());
						res.setProductId(otRes.getProductId());
						res.setSectionId(otRes.getSectionId());
						res.setLocationId(otRes.getLocationId());
						res.setCustomerReferenceNo(
								saveData != null ? saveData.getCustomerReferenceNo() : humanData.getCustomerReferenceNo());
						res.setRequestReferenceNo(
								saveData != null ? saveData.getRequestReferenceNo() : humanData.getRequestReferenceNo());
						res.setCreatedBy(reqList.get(0).getCreatedBy());
						resList.add(res);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				return resList;
			}*/
		/*
			@Override
			public List<SlideSectionSaveRes> saveSlideMoneyDetails(List<SlideMoneySaveReq> reqList) {
				// TODO Auto-generated method stub
				List<SlideSectionSaveRes> resList = new ArrayList<SlideSectionSaveRes>();
				EserviceBuildingDetails saveData = new EserviceBuildingDetails();

				EserviceSectionDetails sectionData = new EserviceSectionDetails();
				DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
				try {
					List<EserviceBuildingDetails> saveData1 = buildingRepo.findByRequestReferenceNoAndSectionId(
							reqList.get(0).getRequestReferenceNo(), reqList.get(0).getSectionId());
					if (saveData1 != null && saveData1.size() > 0) {
						buildingRepo.deleteAll(saveData1);
					}
					sectionData = eserSecRepo.findByRequestReferenceNoAndRiskIdAndProductIdAndSectionId(
							reqList.get(0).getRequestReferenceNo(), Integer.valueOf(reqList.get(0).getRiskId()),
							reqList.get(0).getProductId(), reqList.get(0).getSectionId());
					for (SlideMoneySaveReq req : reqList) {

						EserviceBuildingDetails data = buildingRepo
								.findByRequestReferenceNoAndRiskIdAndSectionId(reqList.get(0).getRequestReferenceNo(), 1, "0");

						if (data != null) {

							dozerBeanMapper.map(data, saveData);

						}

						saveData.setRiskId(Integer.valueOf(req.getRiskId()));
						saveData.setRequestReferenceNo(req.getRequestReferenceNo());
						saveData.setSectionId(req.getSectionId());
						saveData.setMoneyAnnualEstimate(StringUtils.isBlank(req.getMoneyAnnualEstimate()) ? new BigDecimal(0)
								: new BigDecimal(req.getMoneyAnnualEstimate()));
						saveData.setMoneyCollector(StringUtils.isBlank(req.getMoneyCollector()) ? new BigDecimal(0)
								: new BigDecimal(req.getMoneyCollector()));
						saveData.setMoneyDirectorResidence(
								StringUtils.isBlank(req.getMoneyDirectorResidence()) ? new BigDecimal(0)
										: new BigDecimal(req.getMoneyDirectorResidence()));
						saveData.setMoneyOutofSafe(StringUtils.isBlank(req.getMoneyOutofSafe()) ? new BigDecimal(0)
								: new BigDecimal(req.getMoneyOutofSafe()));
						saveData.setMoneySafeLimit(StringUtils.isBlank(req.getMoneySafeLimit()) ? new BigDecimal(0)
								: new BigDecimal(req.getMoneySafeLimit()));
//					saveData.setMoneyMajorLoss(StringUtils.isBlank(req.getMoneyMajorLoss()) ? new BigDecimal(0)
//							: new BigDecimal(req.getMoneyMajorLoss()));
//					saveData.setStrongroomSi(StringUtils.isBlank(req.getStrongroomSi()) ? new BigDecimal(0)
//							: new BigDecimal(req.getStrongroomSi()));
						saveData.setStrongroomSi(StringUtils.isBlank(req.getMoneyInSafe()) ? new BigDecimal(0)
								: new BigDecimal(req.getMoneyInSafe()));
						saveData.setMoneyMajorLoss(StringUtils.isBlank(req.getMoneyInTransit()) ? new BigDecimal(0)
								: new BigDecimal(req.getMoneyInTransit()));

						if (StringUtils.isNotBlank(saveData.getExchangeRate().toString())) {
							BigDecimal exRate = saveData.getExchangeRate();
							if (StringUtils.isNotBlank(req.getMoneyAnnualEstimate())) {
								saveData.setMoneyAnnualEstimateLc(
										new BigDecimal(req.getMoneyAnnualEstimate()).multiply(exRate));
							} else {
								saveData.setMoneyAnnualEstimateLc(BigDecimal.ZERO);
							}
							if (StringUtils.isNotBlank(req.getMoneyCollector())) {
								saveData.setMoneyCollectorLc(new BigDecimal(req.getMoneyCollector()).multiply(exRate));
							} else {
								saveData.setMoneyCollectorLc(BigDecimal.ZERO);
							}
							if (StringUtils.isNotBlank(req.getMoneyDirectorResidence())) {
								saveData.setMoneyDirectorResidenceLc(
										new BigDecimal(req.getMoneyDirectorResidence()).multiply(exRate));
							} else {
								saveData.setMoneyDirectorResidenceLc(BigDecimal.ZERO);
							}
							if (StringUtils.isNotBlank(req.getMoneyOutofSafe())) {
								saveData.setMoneyOutofSafeLc(new BigDecimal(req.getMoneyOutofSafe()).multiply(exRate));
							} else {
								saveData.setMoneyOutofSafeLc(BigDecimal.ZERO);
							}
							if (StringUtils.isNotBlank(req.getMoneySafeLimit())) {
								saveData.setMoneySafeLimitLc(new BigDecimal(req.getMoneySafeLimit()).multiply(exRate));
							} else {
								saveData.setMoneySafeLimitLc(BigDecimal.ZERO);
							}
							if (StringUtils.isNotBlank(req.getMoneyInTransit())) {
								saveData.setMoneyMajorLossLc(new BigDecimal(req.getMoneyInTransit()).multiply(exRate));
							} else {
								saveData.setMoneyMajorLossLc(BigDecimal.ZERO);
							}

							if (StringUtils.isNotBlank(req.getMoneyInSafe())) {
								saveData.setStrongroomSiLc(new BigDecimal(req.getMoneyInSafe()).multiply(exRate));
							} else {
								saveData.setStrongroomSiLc(BigDecimal.ZERO);
							}

						}

						// Endorsement Changes
						if (!(req.getEndorsementType() == null || req.getEndorsementType() == 0))

						{

							saveData.setOriginalPolicyNo(req.getOriginalPolicyNo());
							saveData.setEndorsementDate(req.getEndorsementDate());
							saveData.setEndorsementRemarks(req.getEndorsementRemarks());
							saveData.setEndorsementEffdate(req.getEndorsementEffdate());
							saveData.setEndtPrevPolicyNo(req.getEndtPrevPolicyNo());
							saveData.setEndtPrevQuoteNo(req.getEndtPrevQuoteNo());
							saveData.setEndtCount(req.getEndtCount());
							saveData.setEndtStatus(req.getEndtStatus());
							saveData.setIsFinyn(req.getIsFinaceYn());
							saveData.setEndtCategDesc(req.getEndtCategDesc());
							saveData.setEndorsementType(req.getEndorsementType());
							saveData.setEndorsementTypeDesc(req.getEndorsementTypeDesc());

						}
						// Status
						saveData.setStatus(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus());
						saveData.setLocationName(
								(StringUtils.isBlank(req.getLocationName()) || req.getLocationName() == null) ? null
										: req.getLocationName());
						saveData.setAddress(
								(StringUtils.isBlank(req.getAddress()) || req.getAddress() == null) ? null : req.getAddress());
						saveData.setRegionCode((StringUtils.isBlank(req.getRegionCode()) || req.getRegionCode() == null) ? null
								: req.getRegionCode());
						saveData.setDistrictCode(
								(StringUtils.isBlank(req.getDistrictCode()) || req.getDistrictCode() == null) ? null
										: req.getDistrictCode());

						if (req.getRegionCode() != null) {
							List<RegionMaster> regiondata = regionrepo.findByRegionCode(req.getRegionCode());
							saveData.setRegionDesc(
									!regiondata.isEmpty() && StringUtils.isBlank(regiondata.get(0).getRegionName()) ? null
											: regiondata.get(0).getRegionName());
						}

						if (req.getDistrictCode() != null) {
							List<StateMaster> statedata = staterepo.findByStateId(Integer.valueOf(req.getDistrictCode()));
							saveData.setDistrictDesc(
									!statedata.isEmpty() && StringUtils.isBlank(statedata.get(0).getStateName()) ? null
											: statedata.get(0).getStateName());
						}

						saveData.setSectionDesc(sectionData.getSectionName());
						buildingRepo.save(saveData);


		//
		///*	@Override
//			public List<SlideSectionSaveRes> saveEmpLiabilityDetails(List<SlideEmpLiabilitySaveReq> reqList) {
//				// TODO Auto-generated method stub
//				List<SlideSectionSaveRes> resList = new ArrayList<SlideSectionSaveRes>();
//				EserviceBuildingDetails saveData = new EserviceBuildingDetails();
//				List<EserviceCommonDetails> humanDatas = new ArrayList<EserviceCommonDetails>();
//				EserviceCommonDetails humanData = new EserviceCommonDetails();
//				EserviceSectionDetails sectionData = new EserviceSectionDetails();
//				DozerBeanMapper dozerMapper = new DozerBeanMapper();
//				List<BuildingSectionRes> sectionList = new ArrayList<BuildingSectionRes>();
		//
//				try {
//					BigDecimal endtCount = BigDecimal.ZERO;
//					String refNo = reqList.get(0).getRequestReferenceNo();
//					String productId = reqList.get(0).getProductId();
//					String sectionId = reqList.get(0).getSectionId();
		//
//					saveData = buildingRepo.findByRequestReferenceNoAndRiskIdAndSectionId(refNo, 1, "0");
//					if (saveData != null) {
		//
//						dozerMapper.map(saveData, humanData);
//						humanData.setPolicyPeriod(saveData.getPolicyPeriord());
		//
//					}
//					humanDatas = humanRepo.findByRequestReferenceNoAndSectionId(refNo, sectionId);
//					SlideEmpLiabilitySaveReq firstRow = reqList.get(0);
//					boolean hasAllNulls = humanDatas.stream().allMatch(a -> a == null);
		//
//					if ((firstRow.getEndorsementType() == null || firstRow.getEndorsementType() == 0) && humanDatas.size() > 0
//							|| !humanDatas.isEmpty() || !hasAllNulls) {
//						humanRepo.deleteAll(humanDatas);
//						dozerMapper.map(humanDatas.get(0), humanData);
//					} else if (humanDatas.size() > 0) {
//						List<EserviceCommonDetails> humanData1 = new ArrayList<EserviceCommonDetails>();
//						if (!(firstRow.getEndorsementType() == null || firstRow.getEndorsementType() == 0)) {
		//
//							// search
//							if (reqList.get(0).getEndtCount().compareTo(BigDecimal.ONE) == 0) {
//								endtCount = reqList.get(0).getEndtCount();
//								humanData1 = humanRepo.findBySectionIdAndPolicyNoAndStatusNot(sectionId,
//										reqList.get(0).getOriginalPolicyNo(), "D");
//							} else {
//								endtCount = reqList.get(0).getEndtCount().subtract(BigDecimal.ONE);
//								humanData1 = humanRepo.findByEndtCountAndSectionIdAndOriginalPolicyNoAndStatusNot(endtCount,
//										sectionId, reqList.get(0).getOriginalPolicyNo(), "D");
//							}
		//
//							// delete all
//							humanRepo.deleteAll(humanDatas);
		//
//						}
//						List<EserviceCommonDetails> humanDatas2 = new ArrayList<EserviceCommonDetails>();
//						dozerMapper.map(humanDatas.get(0), humanData);
//						String policyNo = humanDatas.get(0).getPolicyNo();
//						humanData1.forEach(hum -> {
//							EserviceCommonDetails o = new EserviceCommonDetails();
//							dozerMapper.map(hum, o);
//							o.setStatus("D");
//							o.setRequestReferenceNo(refNo);
//							o.setQuoteNo("");
//							o.setPolicyNo(policyNo);
//							if (!(firstRow.getEndorsementType() == null || firstRow.getEndorsementType() == 0))
		//
//							{
		//
//								o.setOriginalPolicyNo(firstRow.getOriginalPolicyNo());
//								o.setEndorsementDate(firstRow.getEndorsementDate());
//								o.setEndorsementRemarks(firstRow.getEndorsementRemarks());
//								o.setEndorsementEffdate(firstRow.getEndorsementEffdate());
//								o.setEndtPrevPolicyNo(firstRow.getEndtPrevPolicyNo());
//								o.setEndtPrevQuoteNo(firstRow.getEndtPrevQuoteNo());
//								o.setEndtCount(firstRow.getEndtCount());
//								o.setEndtStatus(firstRow.getEndtStatus());
//								o.setIsFinyn(firstRow.getIsFinaceYn());
//								o.setEndtCategDesc(firstRow.getEndtCategDesc());
//								o.setEndorsementType(firstRow.getEndorsementType());
//								o.setEndorsementTypeDesc(firstRow.getEndorsementTypeDesc());
		//
//							}
		//
//							humanDatas2.add(o);
//						});
//						humanRepo.saveAllAndFlush(humanDatas2);
		//
//					}
		//
//					sectionData = eserSecRepo.findByRequestReferenceNoAndRiskIdAndProductIdAndSectionId(refNo, 1, productId,
//							sectionId);
		//
//					List<EserviceCommonDetails> saveHumanList = new ArrayList<EserviceCommonDetails>();
//					for (SlideEmpLiabilitySaveReq data : reqList) {
//						// Save Human
//						EserviceCommonDetails saveHuman = new EserviceCommonDetails();
//						saveHuman = dozerMapper.map(humanData, EserviceCommonDetails.class);
//						saveHuman.setRequestReferenceNo(data.getRequestReferenceNo());
//						// saveHuman.setRiskId(Integer.valueOf(data.getLiabilityOccupationId())) ;
//						saveHuman.setRiskId(data.getOriginalRiskId() == null || StringUtils.isBlank(data.getOriginalRiskId())
//								? Integer.valueOf(data.getRiskId())
//								: Integer.valueOf(data.getOriginalRiskId()));
//						saveHuman.setOriginalRiskId(
//								data.getOriginalRiskId() == null || StringUtils.isBlank(data.getOriginalRiskId())
//										? Integer.valueOf(data.getRiskId())
//										: Integer.valueOf(data.getOriginalRiskId()));
//						saveHuman.setSectionId(data.getSectionId());
//						saveHuman.setSectionName(sectionData != null ? sectionData.getSectionName() : "");
//						saveHuman.setCreatedBy(data.getCreatedBy());
////						saveHuman.setEmpLiabilitySi(StringUtils.isBlank(data.getEmpLiabilitySi()) ? new BigDecimal(0)
////								: new BigDecimal(data.getEmpLiabilitySi()));
//						saveHuman.setSumInsured(StringUtils.isBlank(data.getEmpLiabilitySi()) ? new BigDecimal(0)
//								: new BigDecimal(data.getEmpLiabilitySi()));
//						// Personal Liability
//						if (StringUtils.isNotBlank(data.getLiabilityOccupationId())) {
//							OccupationMaster occupationData = getOccupationMasterDropdown(data.getInsuranceId(), "99999",
//									data.getProductId(), data.getLiabilityOccupationId());
//							saveHuman.setOccupationType(occupationData.getOccupationId().toString());
//							saveHuman.setOccupationDesc(occupationData.getOccupationName());
//							saveHuman.setCategoryId(occupationData.getCategoryId());
//							// saveHuman.setCategoryDesc("Class " + occupationData.getCategoryId());
//						}
		//
//						saveHuman.setOtherOccupation(data.getOtherOccupation());
//						saveHuman.setTotalNoOfEmployees(StringUtils.isBlank(data.getTotalNoOfEmployees()) ? null
//								: Long.valueOf(data.getTotalNoOfEmployees()));
//						saveHuman.setCount(StringUtils.isBlank(data.getTotalNoOfEmployees()) ? null
//								: Integer.valueOf(data.getTotalNoOfEmployees()));
//						// Lc Calculation
//						BigDecimal exchangeRate = humanData.getExchangeRate() != null ? humanData.getExchangeRate()
//								: BigDecimal.ZERO;
//						saveHuman.setSumInsuredLc(
//								saveHuman.getSumInsured() == null ? null : saveHuman.getSumInsured().multiply(exchangeRate));
////						saveHuman.setLiabilitySiLc(
////								saveHuman.getLiabilitySi() == null ? null : saveHuman.getLiabilitySi().multiply(exchangeRate));
////						saveHuman.setFidEmpSiLc(
////								saveHuman.getFidEmpSi() == null ? null : saveHuman.getFidEmpSi().multiply(exchangeRate));
////						saveHuman.setEmpLiabilitySiLc(saveHuman.getEmpLiabilitySi() == null ? null
////								: saveHuman.getEmpLiabilitySi().multiply(exchangeRate));
////						saveHuman.setPersonalLiabilitySiLc(saveHuman.getPersonalLiabilitySi() == null ? null
////								: saveHuman.getPersonalLiabilitySi().multiply(exchangeRate));
////						if (data.getSectionId().equals("106")) {
////							saveHuman.setDomesticServentSi(StringUtils.isBlank(data.getEmpLiabilitySi()) ? new BigDecimal(0)
////									: new BigDecimal(data.getEmpLiabilitySi()));
////							saveHuman.setDomesticServentSiLc(saveHuman.getDomesticServentSi() == null ? null
////									: saveHuman.getDomesticServentSi().multiply(exchangeRate));
		//
////						}
//						// Endorsement CHanges
//						if (!(data.getEndorsementType() == null || data.getEndorsementType() == 0))
		//
//						{
		//
//							saveHuman.setOriginalPolicyNo(data.getOriginalPolicyNo());
//							saveHuman.setEndorsementDate(data.getEndorsementDate());
//							saveHuman.setEndorsementRemarks(data.getEndorsementRemarks());
//							saveHuman.setEndorsementEffdate(data.getEndorsementEffdate());
//							saveHuman.setEndtPrevPolicyNo(data.getEndtPrevPolicyNo());
//							saveHuman.setEndtPrevQuoteNo(data.getEndtPrevQuoteNo());
//							saveHuman.setEndtCount(data.getEndtCount());
//							saveHuman.setEndtStatus(data.getEndtStatus());
//							saveHuman.setIsFinyn(data.getIsFinaceYn());
//							saveHuman.setEndtCategDesc(data.getEndtCategDesc());
//							saveHuman.setEndorsementType(data.getEndorsementType());
//							saveHuman.setEndorsementTypeDesc(data.getEndorsementTypeDesc());
//							saveHuman.setStatus("E");
//						}
//						saveHumanList.add(saveHuman);
		//
//					}
//					humanRepo.saveAllAndFlush(saveHumanList);
//					if ("14".equalsIgnoreCase(productId)) {
//						updatesectionDetails(refNo);
//					}
		//
//					List<EserviceCommonDetails> esercommonDatas = eserCommonRepo
//							.findByRequestReferenceNoAndSectionId(humanData.getRequestReferenceNo(), humanData.getSectionId());
//					if (esercommonDatas.size() == 0) {
//						esercommonDatas = saveHumanList;
//					}
//					for (EserviceCommonDetails data : esercommonDatas) {
//						// Section Req
//						BuildingSectionRes sec = new BuildingSectionRes();
//						sec.setMotorYn(sectionData.getProductType());
//						sec.setSectionId(sectionData.getSectionId());
//						sec.setSectionName(sectionData.getSectionName());
//						sec.setOccupationId(data.getOccupationType());
//						sec.setOriginalRiskId(data.getOriginalRiskId() != null ? data.getOriginalRiskId() : 0);
//						sectionList.add(sec);
//					}

//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Log Details" + e.getMessage());
		//
//				}
//				try {

						List<OneTimeTableRes> otResList = null;
						// Section Req
						List<BuildingSectionRes> sectionList = new ArrayList<BuildingSectionRes>();
						BuildingSectionRes sec = new BuildingSectionRes();
						sec.setMotorYn(sectionData.getProductType());
						sec.setSectionId(sectionData.getSectionId());
						sec.setSectionName(sectionData.getSectionName());
						sectionList.add(sec);

						// One Time Table Thread Call
						OneTimeTableReq otReq = new OneTimeTableReq();
						otReq.setRequestReferenceNo(saveData.getRequestReferenceNo());
						otReq.setVehicleId(saveData.getRiskId());
						otReq.setBranchCode(saveData.getBranchCode());
						otReq.setInsuranceId(saveData.getCompanyId());
						otReq.setProductId(Integer.valueOf(saveData.getProductId()));
						otReq.setSectionList(sectionList);

						otResList = otService.call_OT_Insert(otReq);
						for (OneTimeTableRes otRes : otResList) {
							SlideSectionSaveRes res = new SlideSectionSaveRes();
							res.setResponse("Saved Successfully");
							res.setRiskId(otRes.getVehicleId());
							res.setVdRefNo(otRes.getVdRefNo());
							res.setCdRefNo(otRes.getCdRefNo());
							res.setMsrefno(otRes.getMsRefNo());
							res.setCompanyId(otRes.getCompanyId());
							res.setProductId(otRes.getProductId());
							res.setSectionId(otRes.getSectionId());
							res.setCustomerReferenceNo(saveData.getCustomerReferenceNo());
							res.setRequestReferenceNo(saveData.getRequestReferenceNo());
							res.setCreatedBy(req.getCreatedBy());
							resList.add(res);
						}
					}
					if ("16".equalsIgnoreCase(reqList.get(0).getProductId())) {
						updatesectionDetails(reqList.get(0).getRequestReferenceNo());
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				return resList;
			}*/
		/*
			@Override
			public List<SlideSectionSaveRes> saveAccidentDamageDetails(AccidentDamageSaveRequest req) {
				// TODO Auto-generated method stub
				List<SlideSectionSaveRes> resList = new ArrayList<SlideSectionSaveRes>();
				EserviceBuildingDetails saveData = new EserviceBuildingDetails();
				EserviceSectionDetails sectionData = new EserviceSectionDetails();

				try {
					saveData = buildingRepo.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo(),
							Integer.valueOf(req.getRiskId()), req.getSectionId());
					sectionData = eserSecRepo.findByRequestReferenceNoAndRiskIdAndProductIdAndSectionId(
							req.getRequestReferenceNo(), Integer.valueOf(req.getRiskId()), req.getProductId(),
							req.getSectionId());

					// Set Fields
					// saveData.setAccDamageSi(StringUtils.isBlank(req.getAccDamageSi()) ? new
					// BigDecimal(0): new BigDecimal(req.getAccDamageSi()) );
					// Status
					saveData.setStatus(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus());
					buildingRepo.save(saveData);

				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());

				}
				try {
					List<OneTimeTableRes> otResList = null;
					// Section Req
					List<BuildingSectionRes> sectionList = new ArrayList<BuildingSectionRes>();
					BuildingSectionRes sec = new BuildingSectionRes();
					sec.setMotorYn(sectionData.getProductType());
					sec.setSectionId(sectionData.getSectionId());
					sec.setSectionName(sectionData.getSectionName());
					sectionList.add(sec);

					// One Time Table Thread Call
					OneTimeTableReq otReq = new OneTimeTableReq();
					otReq.setRequestReferenceNo(saveData.getRequestReferenceNo());
					otReq.setVehicleId(saveData.getRiskId());
					otReq.setBranchCode(saveData.getBranchCode());
					otReq.setInsuranceId(saveData.getCompanyId());
					otReq.setProductId(Integer.valueOf(saveData.getProductId()));
					otReq.setSectionList(sectionList);

					otResList = otService.call_OT_Insert(otReq);

					for (OneTimeTableRes otRes : otResList) {
						SlideSectionSaveRes res = new SlideSectionSaveRes();
						res.setResponse("Saved Successfully");
						res.setRiskId(otRes.getVehicleId());
						res.setVdRefNo(otRes.getVdRefNo());
						res.setCdRefNo(otRes.getCdRefNo());
						res.setMsrefno(otRes.getMsRefNo());
						res.setCompanyId(otRes.getCompanyId());
						res.setProductId(otRes.getProductId());
						res.setSectionId(otRes.getSectionId());
						res.setCustomerReferenceNo(saveData.getCustomerReferenceNo());
						res.setRequestReferenceNo(saveData.getRequestReferenceNo());
						res.setCreatedBy(req.getCreatedBy());
						resList.add(res);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				return resList;
			}*/
		/*
			@Override
			public List<SlideSectionSaveRes> saveAllRiskDetails(AllRiskDetailsReq req) {
				// TODO Auto-generated method stub
				List<SlideSectionSaveRes> resList = new ArrayList<SlideSectionSaveRes>();
				EserviceBuildingDetails saveData = new EserviceBuildingDetails();
				EserviceSectionDetails sectionData = new EserviceSectionDetails();
				DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
				try {
//					saveData = buildingRepo.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo() , Integer.valueOf(req.getRiskId()),req.getSectionId());
//					sectionData = eserSecRepo.findByRequestReferenceNoAndRiskIdAndProductIdAndSectionId(req.getRequestReferenceNo(),Integer.valueOf(req.getRiskId()) , req.getProductId() ,req.getSectionId());

					Double totalSumInsured = null;
					Map<Integer, List<ContentAndRisk>> riskGroup = null;

					List<ContentAndRisk> contentList = contentRepo
							.findByRequestReferenceNoAndSectionId(req.getRequestReferenceNo(), req.getSectionId());
					if (contentList.size() > 0 && contentList != null) {

						riskGroup = contentList.stream().filter(o -> o.getRiskId() != null)
								.collect(Collectors.groupingBy(ContentAndRisk::getRiskId));

					}
					List<Integer> riskIds = contentList.stream()
							.map(a -> a.getRiskId() == null ? 0 : Integer.valueOf(a.getRiskId())).collect(Collectors.toList());

					try {
						if (null != riskIds && !riskIds.stream().anyMatch(a -> a == 1)) {

							riskIds.add(1);
							List<EserviceBuildingDetails> existingData = buildingRepo
									.findByRequestReferenceNoAndSectionIdAndRiskId(req.getRequestReferenceNo(),
											req.getSectionId(), 1);
							if (null != existingData && !existingData.isEmpty()) {

								existingData.stream().forEach(a -> {

									a.setAllriskSuminsured(null);
									buildingRepo.save(a);
								});

							}
						}
					} catch (Exception e) {

						log.error("Exception Occurs When Get The Existing Data In ALl Risk Save Api");
						log.error(e.getMessage());
						e.printStackTrace();
						// throw new

					}

					List<EserviceBuildingDetails> exist = buildingRepo.findByRequestReferenceNoAndSectionIdAndRiskIdNotIn(
							req.getRequestReferenceNo(), req.getSectionId(), riskIds);
					if (exist != null && exist.size() > 0) {

						buildingRepo.deleteAll(exist);
					}

					for (Integer risk : riskGroup.keySet()) {

						List<ContentAndRisk> con = contentRepo.findByRequestReferenceNoAndSectionIdAndRiskId(
								req.getRequestReferenceNo(), req.getSectionId(), risk);

						totalSumInsured = con.stream().filter(o -> o.getSumInsured() != null)
								.mapToDouble(o -> o.getSumInsured().doubleValue()).sum();

						EserviceBuildingDetails data = buildingRepo.findByRequestReferenceNoAndRiskIdAndSectionId(
								req.getRequestReferenceNo(), 1, req.getSectionId());
						sectionData = eserSecRepo.findByRequestReferenceNoAndRiskIdAndProductIdAndSectionId(
								req.getRequestReferenceNo(), Integer.valueOf(req.getRiskId()), req.getProductId(),
								req.getSectionId());
						if (data != null) {
							dozerBeanMapper.map(data, saveData);

						}
						saveData.setRiskId(risk);

//					saveData.setAllriskSuminsured(StringUtils.isBlank(req.getAllriskSuminsured()) ? new BigDecimal(0):new BigDecimal(req.getAllriskSuminsured()));
						saveData.setAllriskSuminsured(
								totalSumInsured == null ? new BigDecimal(0) : new BigDecimal(totalSumInsured));
						saveData.setMiningPlantSi(StringUtils.isBlank(req.getMiningPlantSi()) ? new BigDecimal(0)
								: new BigDecimal(req.getMiningPlantSi()));
						saveData.setNonminingPlantSi(StringUtils.isBlank(req.getNonminingPlantSi()) ? new BigDecimal(0)
								: new BigDecimal(req.getNonminingPlantSi()));
						saveData.setGensetsSi(StringUtils.isBlank(req.getGensetsSi()) ? new BigDecimal(0)
								: new BigDecimal(req.getGensetsSi()));
						saveData.setEquipmentSi(
								StringUtils.isNotBlank(req.getEquipmentSi()) && "100004".equalsIgnoreCase(req.getInsuranceId())
										? new BigDecimal(req.getEquipmentSi())
										: saveData.getEquipmentSi());
						if (StringUtils.isNotBlank(saveData.getExchangeRate().toString())) {
							BigDecimal exRate = saveData.getExchangeRate();

							// Lc Calc
							if (totalSumInsured > 0.0) {
								saveData.setAllriskSuminsuredLc(new BigDecimal(totalSumInsured).multiply(exRate));

							} else {
								saveData.setAllriskSuminsuredLc(BigDecimal.ZERO);
							}

							if (StringUtils.isNotBlank(req.getMiningPlantSi())) {
								saveData.setMiningPlantSiLc(new BigDecimal(req.getMiningPlantSi()).multiply(exRate));

							} else {
								saveData.setMiningPlantSiLc(BigDecimal.ZERO);
							}

							if (StringUtils.isNotBlank(req.getGensetsSi())) {
								saveData.setGensetsSiLc(new BigDecimal(req.getGensetsSi()).multiply(exRate));

							} else {
								saveData.setGensetsSiLc(BigDecimal.ZERO);
							}

							if (StringUtils.isNotBlank(req.getNonminingPlantSi())) {
								saveData.setNonminingPlantSiLc(new BigDecimal(req.getNonminingPlantSi()).multiply(exRate));

							} else {
								saveData.setNonminingPlantSiLc(BigDecimal.ZERO);
							}

							if (StringUtils.isNotBlank(req.getEquipmentSi())) {
								saveData.setEquipmentSiLc(new BigDecimal(req.getEquipmentSi()).multiply(exRate));

							} else {
								saveData.setEquipmentSiLc(BigDecimal.ZERO);
							}
						}
						// Endorsement CHanges
						if (!(req.getEndorsementType() == null || req.getEndorsementType() == 0))

						{

							saveData.setOriginalPolicyNo(req.getOriginalPolicyNo());
							saveData.setEndorsementDate(req.getEndorsementDate());
							saveData.setEndorsementRemarks(req.getEndorsementRemarks());
							saveData.setEndorsementEffdate(req.getEndorsementEffdate());
							saveData.setEndtPrevPolicyNo(req.getEndtPrevPolicyNo());
							saveData.setEndtPrevQuoteNo(req.getEndtPrevQuoteNo());
							saveData.setEndtCount(req.getEndtCount());
							saveData.setEndtStatus(req.getEndtStatus());
							saveData.setIsFinyn(req.getIsFinaceYn());
							saveData.setEndtCategDesc(req.getEndtCategDesc());
							saveData.setEndorsementType(req.getEndorsementType());
							saveData.setEndorsementTypeDesc(req.getEndorsementTypeDesc());

						}
						// Status
						saveData.setStatus(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus());
						buildingRepo.save(saveData);

						try {
							List<OneTimeTableRes> otResList = null;
							// Section Req
							List<BuildingSectionRes> sectionList = new ArrayList<BuildingSectionRes>();
							BuildingSectionRes sec = new BuildingSectionRes();
							sec.setMotorYn(sectionData.getProductType());
							sec.setSectionId(sectionData.getSectionId());
							sec.setSectionName(sectionData.getSectionName());
							sectionList.add(sec);

							// One Time Table Thread Call
							OneTimeTableReq otReq = new OneTimeTableReq();
							otReq.setRequestReferenceNo(saveData.getRequestReferenceNo());
							otReq.setVehicleId(saveData.getRiskId());
							otReq.setBranchCode(saveData.getBranchCode());
							otReq.setInsuranceId(saveData.getCompanyId());
							otReq.setProductId(Integer.valueOf(saveData.getProductId()));
							otReq.setSectionList(sectionList);

							otResList = otService.call_OT_Insert(otReq);

							for (OneTimeTableRes otRes : otResList) {
								SlideSectionSaveRes res = new SlideSectionSaveRes();
								res.setResponse("Saved Successfully");
								res.setRiskId(otRes.getVehicleId());
								res.setVdRefNo(otRes.getVdRefNo());
								res.setCdRefNo(otRes.getCdRefNo());
								res.setMsrefno(otRes.getMsRefNo());
								res.setCompanyId(otRes.getCompanyId());
								res.setProductId(otRes.getProductId());
								res.setSectionId(otRes.getSectionId());
								res.setCustomerReferenceNo(saveData.getCustomerReferenceNo());
								res.setRequestReferenceNo(saveData.getRequestReferenceNo());
								res.setCreatedBy(req.getCreatedBy());
								resList.add(res);
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());

				}
				return resList;

			}*/

			/*@Override
			public List<SlideSectionSaveRes> saveBurglaryAndHouseBreakingDetails(BurglaryAndHouseBreakingSaveReq req) {
				// TODO Auto-generated method stub
				List<SlideSectionSaveRes> resList = new ArrayList<SlideSectionSaveRes>();
				EserviceBuildingDetails saveData = new EserviceBuildingDetails();
				EserviceSectionDetails sectionData = new EserviceSectionDetails();
				try {
					saveData = buildingRepo.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo(),
							Integer.valueOf(req.getRiskId()), req.getSectionId());
					sectionData = eserSecRepo.findByRequestReferenceNoAndRiskIdAndProductIdAndSectionId(
							req.getRequestReferenceNo(), Integer.valueOf(req.getRiskId()), req.getProductId(),
							req.getSectionId());

					if ("100002".equalsIgnoreCase(req.getInsuranceId())) {
						saveData.setBurglarySi(StringUtils.isBlank(req.getBurglarySi()) ? new BigDecimal(0)
								: new BigDecimal(req.getBurglarySi()));
						if (StringUtils.isNotBlank(saveData.getExchangeRate().toString())) {
							BigDecimal exRate = saveData.getExchangeRate();
							if (StringUtils.isNotBlank(req.getBurglarySi())) {
								saveData.setBurglarySiLc(new BigDecimal(req.getBurglarySi()).multiply(exRate));
								;
							} else {
								saveData.setBurglarySiLc(BigDecimal.ZERO);
							}
						}
					} else {
						saveData.setStockLossPercent(StringUtils.isBlank(req.getStockLossPercent()) ? null
								: Integer.valueOf(req.getStockLossPercent()));
						saveData.setGoodsLossPercent(StringUtils.isBlank(req.getGoodsLossPercent()) ? null
								: Integer.valueOf(req.getGoodsLossPercent()));
						saveData.setFurnitureLossPercent(StringUtils.isBlank(req.getFurnitureLossPercent()) ? null
								: Integer.valueOf(req.getFurnitureLossPercent()));
						saveData.setApplianceLossPercent(StringUtils.isBlank(req.getApplianceLossPercent()) ? null
								: Integer.valueOf(req.getApplianceLossPercent()));
						saveData.setCashValueablesLossPercent(StringUtils.isBlank(req.getCashValueablesLossPercent()) ? null
								: Integer.valueOf(req.getCashValueablesLossPercent()));

						// Lc Insert
						if (StringUtils.isNotBlank(saveData.getExchangeRate().toString())) {
							BigDecimal exRate = saveData.getExchangeRate();
							if (StringUtils.isNotBlank(req.getStockInTradeSi())) {
								saveData.setStockInTradeSiLc(new BigDecimal(req.getStockInTradeSi()).multiply(exRate));
							} else {
								saveData.setStockInTradeSiLc(BigDecimal.ZERO);
							}
							if (StringUtils.isNotBlank(req.getGoodsSi())) {
								saveData.setGoodsSiLc(new BigDecimal(req.getGoodsSi()).multiply(exRate));
							} else {
								saveData.setGoodsSiLc(BigDecimal.ZERO);
							}
							if (StringUtils.isNotBlank(req.getFurnitureSi())) {
								saveData.setFurnitureSiLc(new BigDecimal(req.getFurnitureSi()).multiply(exRate));
							} else {
								saveData.setFurnitureSiLc(BigDecimal.ZERO);
							}
							if (StringUtils.isNotBlank(req.getApplianceSi())) {
								saveData.setApplianceSiLc(new BigDecimal(req.getApplianceSi()).multiply(exRate));
							} else {
								saveData.setApplianceSiLc(BigDecimal.ZERO);
							}
							if (StringUtils.isNotBlank(req.getCashValueablesSi())) {
								saveData.setCashValueablesSiLc(new BigDecimal(req.getCashValueablesSi()).multiply(exRate));
							} else {
								saveData.setCashValueablesSiLc(BigDecimal.ZERO);
							}
						}
						saveData.setStockInTradeSi(StringUtils.isBlank(req.getStockInTradeSi()) ? new BigDecimal(0)
								: new BigDecimal(req.getStockInTradeSi()));
						saveData.setGoodsSi(
								StringUtils.isBlank(req.getGoodsSi()) ? new BigDecimal(0) : new BigDecimal(req.getGoodsSi()));
						saveData.setFurnitureSi(StringUtils.isBlank(req.getFurnitureSi()) ? new BigDecimal(0)
								: new BigDecimal(req.getFurnitureSi()));
						saveData.setApplianceSi(StringUtils.isBlank(req.getApplianceSi()) ? new BigDecimal(0)
								: new BigDecimal(req.getApplianceSi()));
						saveData.setCashValueablesSi(StringUtils.isBlank(req.getCashValueablesSi()) ? new BigDecimal(0)
								: new BigDecimal(req.getCashValueablesSi()));
						saveData.setBackDoors(
								StringUtils.isBlank(req.getBackDoors()) ? null : Integer.valueOf(req.getBackDoors()));
						saveData.setBuildingOccupied(StringUtils.isBlank(req.getBuildingOccupied()) ? 0
								: Integer.valueOf(req.getBuildingOccupied()));
						saveData.setCeilingType(
								StringUtils.isBlank(req.getCeilingType()) ? 0 : Integer.valueOf(req.getCeilingType()));
						saveData.setDistrictCode(StringUtils.isBlank(req.getDistrictCode()) ? "" : (req.getDistrictCode()));
						saveData.setDoorsMaterialId(
								StringUtils.isBlank(req.getDoorsMaterialId()) ? 0 : Integer.valueOf(req.getDoorsMaterialId()));
						saveData.setFrontDoors(
								StringUtils.isBlank(req.getFrontDoors()) ? null : Integer.valueOf(req.getFrontDoors()));
						saveData.setAccessibleWindows(StringUtils.isBlank(req.getAccessibleWindows()) ? null
								: Integer.valueOf(req.getAccessibleWindows()));
						saveData.setAddress(StringUtils.isBlank(req.getAddress()) ? "" : (req.getAddress()));
						saveData.setBuildingOwnerYn(
								StringUtils.isBlank(req.getBuildingOwnerYn()) ? "N" : (req.getBuildingOwnerYn()));
						// saveData.setBurglarySi(StringUtils.isBlank(req.getBurglarySi()) ? new
						// BigDecimal(0): new BigDecimal(req.getBurglarySi()));
						if (req.getInsuranceForId() != null && req.getInsuranceForId().size() > 0) {
							String insuranceForIds = "";
							String insuranceForDesc = "";
							for (String id : req.getInsuranceForId()) {
								String insDesc = getListItem(saveData.getCompanyId(), saveData.getBranchCode(),
										"BURGLARY_INSURANCE_FOR", id);

								insuranceForIds = StringUtils.isBlank(insuranceForIds) ? id : insuranceForIds + "," + id;
								insuranceForDesc = StringUtils.isBlank(insuranceForDesc) ? insDesc
										: insuranceForDesc + "~" + insDesc;
							}

							saveData.setInsuranceForId(insuranceForIds);
							saveData.setInsuranceForDesc(insuranceForDesc);

						}
						if (StringUtils.isNotBlank(req.getNatureOfTradeId())) {
							String naturOfTrade = getListItem(saveData.getCompanyId(), saveData.getBranchCode(),
									"NATURE_OF_TRADE", req.getNatureOfTradeId());
							saveData.setNatureOfTradeId(Integer.valueOf(req.getNatureOfTradeId()));
							saveData.setNatureOfTradeDesc(naturOfTrade);

						}

						saveData.setInternalWallType(StringUtils.isBlank(req.getInternalWallType()) ? null
								: Integer.valueOf(req.getInternalWallType()));
						saveData.setNightLeftDoor(
								StringUtils.isBlank(req.getNightLeftDoor()) ? null : Integer.valueOf(req.getNightLeftDoor()));
						saveData.setOccupiedYear(
								StringUtils.isBlank(req.getOccupiedYear()) ? null : Integer.valueOf(req.getOccupiedYear()));
						saveData.setShowWindows(
								StringUtils.isBlank(req.getShowWindow()) ? null : Integer.valueOf(req.getShowWindow()));
						saveData.setTrapDoors(
								StringUtils.isBlank(req.getTrapDoors()) ? null : Integer.valueOf(req.getTrapDoors()));
						saveData.setWatchmanGuardHours(StringUtils.isBlank(req.getWatchmanGuardHours()) ? null
								: Integer.valueOf(req.getWatchmanGuardHours()));
						saveData.setWindowsMaterialId(StringUtils.isBlank(req.getWindowsMaterialId()) ? null
								: Integer.valueOf(req.getWindowsMaterialId()));
						if (StringUtils.isNotBlank(req.getWallType())) {
							String wallType = getListItem(saveData.getCompanyId(), saveData.getBranchCode(), "WALL_TYPE",
									req.getWallType());
							saveData.setWallType(req.getWallType());
							saveData.setWallTypeDesc(wallType);

						}

						if (StringUtils.isNotBlank(req.getRoofType())) {
							String roofType = getListItem(saveData.getCompanyId(), saveData.getBranchCode(), "ROOF_TYPE",
									req.getRoofType());
							saveData.setRoofType(req.getRoofType());
							saveData.setRoofTypeDesc(roofType);

						}
						saveData.setBuildingBuildYear(Integer.valueOf(req.getBuildingBuildYear()));
						saveData.setRegionCode(req.getRegionCode());
						saveData.setDistrictCode(req.getDistrictCode());
					}

					// Endorsement CHanges
					if (!(req.getEndorsementType() == null || req.getEndorsementType() == 0))

					{

						saveData.setOriginalPolicyNo(req.getOriginalPolicyNo());
						saveData.setEndorsementDate(req.getEndorsementDate());
						saveData.setEndorsementRemarks(req.getEndorsementRemarks());
						saveData.setEndorsementEffdate(req.getEndorsementEffdate());
						saveData.setEndtPrevPolicyNo(req.getEndtPrevPolicyNo());
						saveData.setEndtPrevQuoteNo(req.getEndtPrevQuoteNo());
						saveData.setEndtCount(req.getEndtCount());
						saveData.setEndtStatus(req.getEndtStatus());
						saveData.setIsFinyn(req.getIsFinaceYn());
						saveData.setEndtCategDesc(req.getEndtCategDesc());
						saveData.setEndorsementType(req.getEndorsementType());
						saveData.setEndorsementTypeDesc(req.getEndorsementTypeDesc());

					}

					// Status
					saveData.setStatus(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus());
					buildingRepo.save(saveData);

				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());
				}
				try {
					List<OneTimeTableRes> otResList = null;
					// Section Req
					List<BuildingSectionRes> sectionList = new ArrayList<BuildingSectionRes>();
					BuildingSectionRes sec = new BuildingSectionRes();
					sec.setMotorYn(sectionData.getProductType());
					sec.setSectionId(sectionData.getSectionId());
					sec.setSectionName(sectionData.getSectionName());
					sectionList.add(sec);

					// One Time Table Thread Call
					OneTimeTableReq otReq = new OneTimeTableReq();
					otReq.setRequestReferenceNo(saveData.getRequestReferenceNo());
					otReq.setVehicleId(saveData.getRiskId());
					otReq.setBranchCode(saveData.getBranchCode());
					otReq.setInsuranceId(saveData.getCompanyId());
					otReq.setProductId(Integer.valueOf(saveData.getProductId()));
					otReq.setSectionList(sectionList);

					otResList = otService.call_OT_Insert(otReq);
					for (OneTimeTableRes otRes : otResList) {
						SlideSectionSaveRes res = new SlideSectionSaveRes();
						res.setResponse("Saved Successfully");
						res.setRiskId(otRes.getVehicleId());
						res.setVdRefNo(otRes.getVdRefNo());
						res.setCdRefNo(otRes.getCdRefNo());
						res.setMsrefno(otRes.getMsRefNo());
						res.setCompanyId(otRes.getCompanyId());
						res.setProductId(otRes.getProductId());
						res.setSectionId(otRes.getSectionId());
						res.setCustomerReferenceNo(saveData.getCustomerReferenceNo());
						res.setRequestReferenceNo(saveData.getRequestReferenceNo());
						res.setCreatedBy(req.getCreatedBy());
						resList.add(res);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				return resList;
			}

			@Override
			public List<SlideSectionSaveRes> saveBurglaryAndHouseBreakingDetailsList(
					List<BurglaryAndHouseBreakingSaveReq> reqList) {
				// TODO Auto-generated method stub
				List<SlideSectionSaveRes> resList = new ArrayList<SlideSectionSaveRes>();
				EserviceBuildingDetails saveData = new EserviceBuildingDetails();
				EserviceSectionDetails sectionData = new EserviceSectionDetails();
				DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
				try {
					EserviceBuildingDetails data = buildingRepo.findByRequestReferenceNoAndRiskIdAndSectionId(
							reqList.get(0).getRequestReferenceNo(), 1, reqList.get(0).getSectionId());

					if (data != null) {

						dozerBeanMapper.map(data, saveData);

					}
					List<EserviceBuildingDetails> saveData1 = buildingRepo.findByRequestReferenceNoAndSectionId(
							reqList.get(0).getRequestReferenceNo(), reqList.get(0).getSectionId());
					if (saveData1 != null && saveData1.size() > 0) {
						buildingRepo.deleteAll(saveData1);
					}
					sectionData = eserSecRepo.findByRequestReferenceNoAndRiskIdAndProductIdAndSectionId(
							reqList.get(0).getRequestReferenceNo(), Integer.valueOf(1), reqList.get(0).getProductId(),
							reqList.get(0).getSectionId());
					for (BurglaryAndHouseBreakingSaveReq req : reqList) {
						saveData.setRiskId(Integer.valueOf(req.getRiskId()));
						saveData.setBurglarySi(StringUtils.isBlank(req.getBurglarySi()) ? new BigDecimal(0)
								: new BigDecimal(req.getBurglarySi()));
						if (StringUtils.isNotBlank(saveData.getExchangeRate().toString())) {
							BigDecimal exRate = saveData.getExchangeRate();
							if (StringUtils.isNotBlank(req.getBurglarySi())) {
								saveData.setBurglarySiLc(new BigDecimal(req.getBurglarySi()).multiply(exRate));
								;
							} else {
								saveData.setBurglarySiLc(BigDecimal.ZERO);
							}
						}
						saveData.setFirstLossPercentId(StringUtils.isBlank(req.getFirstLossPercentId()) ? null
								: Integer.valueOf(req.getFirstLossPercentId()));
						if (StringUtils.isNotBlank(req.getFirstLossPercentId())) {
							String firstLossPercent = getListItem(req.getInsuranceId(), saveData.getBranchCode(),
									"BURGLARY_FIRST_LOSS", req.getFirstLossPercentId());
							saveData.setFirstLossPercent(
									StringUtils.isBlank(firstLossPercent) ? null : Integer.valueOf(firstLossPercent));
						}

						// Endorsement CHanges
						if (!(req.getEndorsementType() == null || req.getEndorsementType() == 0))

						{

							saveData.setOriginalPolicyNo(req.getOriginalPolicyNo());
							saveData.setEndorsementDate(req.getEndorsementDate());
							saveData.setEndorsementRemarks(req.getEndorsementRemarks());
							saveData.setEndorsementEffdate(req.getEndorsementEffdate());
							saveData.setEndtPrevPolicyNo(req.getEndtPrevPolicyNo());
							saveData.setEndtPrevQuoteNo(req.getEndtPrevQuoteNo());
							saveData.setEndtCount(req.getEndtCount());
							saveData.setEndtStatus(req.getEndtStatus());
							saveData.setIsFinyn(req.getIsFinaceYn());
							saveData.setEndtCategDesc(req.getEndtCategDesc());
							saveData.setEndorsementType(req.getEndorsementType());
							saveData.setEndorsementTypeDesc(req.getEndorsementTypeDesc());

						}

						// Status
						saveData.setStatus(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus());
						saveData.setRegionCode((StringUtils.isBlank(req.getRegionCode()) || req.getRegionCode() == null) ? null
								: req.getRegionCode());
						saveData.setDistrictCode(
								(StringUtils.isBlank(req.getDistrictCode()) || req.getDistrictCode() == null) ? null
										: req.getDistrictCode());

						if (req.getRegionCode() != null) {
							List<RegionMaster> regiondata = regionrepo.findByRegionCode(req.getRegionCode());
							saveData.setRegionDesc(
									!regiondata.isEmpty() && StringUtils.isBlank(regiondata.get(0).getRegionName()) ? null
											: regiondata.get(0).getRegionName());
						}

						if (req.getDistrictCode() != null) {
							List<StateMaster> statedata = staterepo.findByStateId(Integer.valueOf(req.getDistrictCode()));
							saveData.setDistrictDesc(
									!statedata.isEmpty() && StringUtils.isBlank(statedata.get(0).getStateName()) ? null
											: statedata.get(0).getStateName());
						}

						saveData.setLocationName(
								(req.getLocationName() == null || StringUtils.isBlank(req.getLocationName())) ? ""
										: req.getLocationName());
						saveData.setCoveringDetails(
								(req.getCoveringDetails() == null || StringUtils.isBlank(req.getCoveringDetails())) ? ""
										: req.getCoveringDetails());
						saveData.setDescriptionOfRisk(
								(req.getDescriptionOfRisk() == null || StringUtils.isBlank(req.getDescriptionOfRisk())) ? ""
										: req.getDescriptionOfRisk());
						saveData.setCategoryId(
								(req.getIndustryType() == null || StringUtils.isBlank(req.getIndustryType())) ? ""
										: req.getIndustryType());
						saveData.setAddress(
								(req.getAddress() == null || StringUtils.isBlank(req.getAddress())) ? "" : req.getAddress());

						buildingRepo.save(saveData);


//					List<OneTimeTableRes> otResList = null;
//					// One Time Table Thread Call
//					OneTimeTableReq otReq = new OneTimeTableReq();
//					SlideEmpLiabilitySaveReq req = reqList.get(0);
//					otReq.setRequestReferenceNo(req.getRequestReferenceNo());
//					// otReq.setVehicleId(1);
//					otReq.setBranchCode(saveData != null ? saveData.getBranchCode() : humanData.getBranchCode());
//					otReq.setInsuranceId(saveData != null ? saveData.getCompanyId() : humanData.getCompanyId());
//					otReq.setProductId(Integer.valueOf(req.getProductId()));
//					otReq.setSectionList(sectionList);
//					otReq.setLocationId(saveData != null ? saveData.getLocationId() : 1);
		//
//					otResList = otService.call_OT_Insert(otReq);
//					for (OneTimeTableRes otRes : otResList) {
//						SlideSectionSaveRes res = new SlideSectionSaveRes();
//						res.setResponse("Saved Successfully");
//						res.setRiskId(otRes.getVehicleId());
//						res.setVdRefNo(otRes.getVdRefNo());
//						res.setCdRefNo(otRes.getCdRefNo());
//						res.setMsrefno(otRes.getMsRefNo());
//						res.setCompanyId(otRes.getCompanyId());
//						res.setProductId(otRes.getProductId());
//						res.setSectionId(otRes.getSectionId());
//						res.setLocationId(otRes.getLocationId());
//						res.setCustomerReferenceNo(
//								saveData != null ? saveData.getCustomerReferenceNo() : humanData.getCustomerReferenceNo());
//						res.setRequestReferenceNo(
//								saveData != null ? saveData.getRequestReferenceNo() : humanData.getRequestReferenceNo());
//						res.setCreatedBy(reqList.get(0).getCreatedBy());
//						resList.add(res);
//					}
		//
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				return resList;
//			}*/
		///*
//			@Override
//			public List<SlideSectionSaveRes> saveSlideMoneyDetails(List<SlideMoneySaveReq> reqList) {
//				// TODO Auto-generated method stub
//				List<SlideSectionSaveRes> resList = new ArrayList<SlideSectionSaveRes>();
//				EserviceBuildingDetails saveData = new EserviceBuildingDetails();
		//
//				EserviceSectionDetails sectionData = new EserviceSectionDetails();
//				DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
//				try {
//					List<EserviceBuildingDetails> saveData1 = buildingRepo.findByRequestReferenceNoAndSectionId(
//							reqList.get(0).getRequestReferenceNo(), reqList.get(0).getSectionId());
//					if (saveData1 != null && saveData1.size() > 0) {
//						buildingRepo.deleteAll(saveData1);
//					}
//					sectionData = eserSecRepo.findByRequestReferenceNoAndRiskIdAndProductIdAndSectionId(
//							reqList.get(0).getRequestReferenceNo(), Integer.valueOf(reqList.get(0).getRiskId()),
//							reqList.get(0).getProductId(), reqList.get(0).getSectionId());
//					for (SlideMoneySaveReq req : reqList) {
		//
//						EserviceBuildingDetails data = buildingRepo
//								.findByRequestReferenceNoAndRiskIdAndSectionId(reqList.get(0).getRequestReferenceNo(), 1, "0");
		//
//						if (data != null) {
		//
//							dozerBeanMapper.map(data, saveData);
		//
//						}
		//
//						saveData.setRiskId(Integer.valueOf(req.getRiskId()));
//						saveData.setRequestReferenceNo(req.getRequestReferenceNo());
//						saveData.setSectionId(req.getSectionId());
//						saveData.setMoneyAnnualEstimate(StringUtils.isBlank(req.getMoneyAnnualEstimate()) ? new BigDecimal(0)
//								: new BigDecimal(req.getMoneyAnnualEstimate()));
//						saveData.setMoneyCollector(StringUtils.isBlank(req.getMoneyCollector()) ? new BigDecimal(0)
//								: new BigDecimal(req.getMoneyCollector()));
//						saveData.setMoneyDirectorResidence(
//								StringUtils.isBlank(req.getMoneyDirectorResidence()) ? new BigDecimal(0)
//										: new BigDecimal(req.getMoneyDirectorResidence()));
//						saveData.setMoneyOutofSafe(StringUtils.isBlank(req.getMoneyOutofSafe()) ? new BigDecimal(0)
//								: new BigDecimal(req.getMoneyOutofSafe()));
//						saveData.setMoneySafeLimit(StringUtils.isBlank(req.getMoneySafeLimit()) ? new BigDecimal(0)
//								: new BigDecimal(req.getMoneySafeLimit()));
////					saveData.setMoneyMajorLoss(StringUtils.isBlank(req.getMoneyMajorLoss()) ? new BigDecimal(0)
////							: new BigDecimal(req.getMoneyMajorLoss()));
////					saveData.setStrongroomSi(StringUtils.isBlank(req.getStrongroomSi()) ? new BigDecimal(0)
////							: new BigDecimal(req.getStrongroomSi()));
//						saveData.setStrongroomSi(StringUtils.isBlank(req.getMoneyInSafe()) ? new BigDecimal(0)
//								: new BigDecimal(req.getMoneyInSafe()));
//						saveData.setMoneyMajorLoss(StringUtils.isBlank(req.getMoneyInTransit()) ? new BigDecimal(0)
//								: new BigDecimal(req.getMoneyInTransit()));
		//
//						if (StringUtils.isNotBlank(saveData.getExchangeRate().toString())) {
//							BigDecimal exRate = saveData.getExchangeRate();
//							if (StringUtils.isNotBlank(req.getMoneyAnnualEstimate())) {
//								saveData.setMoneyAnnualEstimateLc(
//										new BigDecimal(req.getMoneyAnnualEstimate()).multiply(exRate));
//							} else {
//								saveData.setMoneyAnnualEstimateLc(BigDecimal.ZERO);
//							}
//							if (StringUtils.isNotBlank(req.getMoneyCollector())) {
//								saveData.setMoneyCollectorLc(new BigDecimal(req.getMoneyCollector()).multiply(exRate));
//							} else {
//								saveData.setMoneyCollectorLc(BigDecimal.ZERO);
//							}
//							if (StringUtils.isNotBlank(req.getMoneyDirectorResidence())) {
//								saveData.setMoneyDirectorResidenceLc(
//										new BigDecimal(req.getMoneyDirectorResidence()).multiply(exRate));
//							} else {
//								saveData.setMoneyDirectorResidenceLc(BigDecimal.ZERO);
//							}
//							if (StringUtils.isNotBlank(req.getMoneyOutofSafe())) {
//								saveData.setMoneyOutofSafeLc(new BigDecimal(req.getMoneyOutofSafe()).multiply(exRate));
//							} else {
//								saveData.setMoneyOutofSafeLc(BigDecimal.ZERO);
//							}
//							if (StringUtils.isNotBlank(req.getMoneySafeLimit())) {
//								saveData.setMoneySafeLimitLc(new BigDecimal(req.getMoneySafeLimit()).multiply(exRate));
//							} else {
//								saveData.setMoneySafeLimitLc(BigDecimal.ZERO);
//							}
//							if (StringUtils.isNotBlank(req.getMoneyInTransit())) {
//								saveData.setMoneyMajorLossLc(new BigDecimal(req.getMoneyInTransit()).multiply(exRate));
//							} else {
//								saveData.setMoneyMajorLossLc(BigDecimal.ZERO);
//							}
		//
//							if (StringUtils.isNotBlank(req.getMoneyInSafe())) {
//								saveData.setStrongroomSiLc(new BigDecimal(req.getMoneyInSafe()).multiply(exRate));
//							} else {
//								saveData.setStrongroomSiLc(BigDecimal.ZERO);
//							}
		//
//						}
		//
//						// Endorsement Changes
//						if (!(req.getEndorsementType() == null || req.getEndorsementType() == 0))
		//
//						{
		//
//							saveData.setOriginalPolicyNo(req.getOriginalPolicyNo());
//							saveData.setEndorsementDate(req.getEndorsementDate());
//							saveData.setEndorsementRemarks(req.getEndorsementRemarks());
//							saveData.setEndorsementEffdate(req.getEndorsementEffdate());
//							saveData.setEndtPrevPolicyNo(req.getEndtPrevPolicyNo());
//							saveData.setEndtPrevQuoteNo(req.getEndtPrevQuoteNo());
//							saveData.setEndtCount(req.getEndtCount());
//							saveData.setEndtStatus(req.getEndtStatus());
//							saveData.setIsFinyn(req.getIsFinaceYn());
//							saveData.setEndtCategDesc(req.getEndtCategDesc());
//							saveData.setEndorsementType(req.getEndorsementType());
//							saveData.setEndorsementTypeDesc(req.getEndorsementTypeDesc());
		//
//						}
//						// Status
//						saveData.setStatus(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus());
//						saveData.setLocationName(
//								(StringUtils.isBlank(req.getLocationName()) || req.getLocationName() == null) ? null
//										: req.getLocationName());
//						saveData.setAddress(
//								(StringUtils.isBlank(req.getAddress()) || req.getAddress() == null) ? null : req.getAddress());
//						saveData.setRegionCode((StringUtils.isBlank(req.getRegionCode()) || req.getRegionCode() == null) ? null
//								: req.getRegionCode());
//						saveData.setDistrictCode(
//								(StringUtils.isBlank(req.getDistrictCode()) || req.getDistrictCode() == null) ? null
//										: req.getDistrictCode());
		//
//						if (req.getRegionCode() != null) {
//							List<RegionMaster> regiondata = regionrepo.findByRegionCode(req.getRegionCode());
//							saveData.setRegionDesc(
//									!regiondata.isEmpty() && StringUtils.isBlank(regiondata.get(0).getRegionName()) ? null
//											: regiondata.get(0).getRegionName());
//						}
		//
//						if (req.getDistrictCode() != null) {
//							List<StateMaster> statedata = staterepo.findByStateId(Integer.valueOf(req.getDistrictCode()));
//							saveData.setDistrictDesc(
//									!statedata.isEmpty() && StringUtils.isBlank(statedata.get(0).getStateName()) ? null
//											: statedata.get(0).getStateName());
//						}
		//
//						saveData.setSectionDesc(sectionData.getSectionName());
//						buildingRepo.save(saveData);
		//
////				} catch (Exception e) {
////					e.printStackTrace();
////					log.info("Log Details" + e.getMessage());
		////
////				}
////				try {
//						List<OneTimeTableRes> otResList = null;
//						// Section Req
//						List<BuildingSectionRes> sectionList = new ArrayList<BuildingSectionRes>();
//						BuildingSectionRes sec = new BuildingSectionRes();
//						sec.setMotorYn(sectionData.getProductType());
//						sec.setSectionId(sectionData.getSectionId());
//						sec.setSectionName(sectionData.getSectionName());
//						sectionList.add(sec);
		//
//						// One Time Table Thread Call
//						OneTimeTableReq otReq = new OneTimeTableReq();
//						otReq.setRequestReferenceNo(saveData.getRequestReferenceNo());
//						otReq.setVehicleId(saveData.getRiskId());
//						otReq.setBranchCode(saveData.getBranchCode());
//						otReq.setInsuranceId(saveData.getCompanyId());
//						otReq.setProductId(Integer.valueOf(saveData.getProductId()));
//						otReq.setSectionList(sectionList);
		//
//						otResList = otService.call_OT_Insert(otReq);
//						for (OneTimeTableRes otRes : otResList) {
//							SlideSectionSaveRes res = new SlideSectionSaveRes();
//							res.setResponse("Saved Successfully");
//							res.setRiskId(otRes.getVehicleId());
//							res.setVdRefNo(otRes.getVdRefNo());
//							res.setCdRefNo(otRes.getCdRefNo());
//							res.setMsrefno(otRes.getMsRefNo());
//							res.setCompanyId(otRes.getCompanyId());
//							res.setProductId(otRes.getProductId());
//							res.setSectionId(otRes.getSectionId());
//							res.setCustomerReferenceNo(saveData.getCustomerReferenceNo());
//							res.setRequestReferenceNo(saveData.getRequestReferenceNo());
//							res.setCreatedBy(req.getCreatedBy());
//							resList.add(res);
//						}
//					}
//					if ("16".equalsIgnoreCase(reqList.get(0).getProductId())) {
//						updatesectionDetails(reqList.get(0).getRequestReferenceNo());
//					}
		//
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
		//
//				return resList;
//			}*/
		///*
//			@Override
//			public List<SlideSectionSaveRes> saveAccidentDamageDetails(AccidentDamageSaveRequest req) {
//				// TODO Auto-generated method stub
//				List<SlideSectionSaveRes> resList = new ArrayList<SlideSectionSaveRes>();
//				EserviceBuildingDetails saveData = new EserviceBuildingDetails();
//				EserviceSectionDetails sectionData = new EserviceSectionDetails();
		//
//				try {
//					saveData = buildingRepo.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo(),
//							Integer.valueOf(req.getRiskId()), req.getSectionId());
//					sectionData = eserSecRepo.findByRequestReferenceNoAndRiskIdAndProductIdAndSectionId(
//							req.getRequestReferenceNo(), Integer.valueOf(req.getRiskId()), req.getProductId(),
//							req.getSectionId());
		//
//					// Set Fields
//					// saveData.setAccDamageSi(StringUtils.isBlank(req.getAccDamageSi()) ? new
//					// BigDecimal(0): new BigDecimal(req.getAccDamageSi()) );
//					// Status
//					saveData.setStatus(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus());
//					buildingRepo.save(saveData);
		//
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Log Details" + e.getMessage());
		//
//				}
//				try {
//					List<OneTimeTableRes> otResList = null;
//					// Section Req
//					List<BuildingSectionRes> sectionList = new ArrayList<BuildingSectionRes>();
//					BuildingSectionRes sec = new BuildingSectionRes();
//					sec.setMotorYn(sectionData.getProductType());
//					sec.setSectionId(sectionData.getSectionId());
//					sec.setSectionName(sectionData.getSectionName());
//					sectionList.add(sec);
		//
//					// One Time Table Thread Call
//					OneTimeTableReq otReq = new OneTimeTableReq();
//					otReq.setRequestReferenceNo(saveData.getRequestReferenceNo());
//					otReq.setVehicleId(saveData.getRiskId());
//					otReq.setBranchCode(saveData.getBranchCode());
//					otReq.setInsuranceId(saveData.getCompanyId());
//					otReq.setProductId(Integer.valueOf(saveData.getProductId()));
//					otReq.setSectionList(sectionList);
		//
//					otResList = otService.call_OT_Insert(otReq);
		//
//					for (OneTimeTableRes otRes : otResList) {
//						SlideSectionSaveRes res = new SlideSectionSaveRes();
//						res.setResponse("Saved Successfully");
//						res.setRiskId(otRes.getVehicleId());
//						res.setVdRefNo(otRes.getVdRefNo());
//						res.setCdRefNo(otRes.getCdRefNo());
//						res.setMsrefno(otRes.getMsRefNo());
//						res.setCompanyId(otRes.getCompanyId());
//						res.setProductId(otRes.getProductId());
//						res.setSectionId(otRes.getSectionId());
//						res.setCustomerReferenceNo(saveData.getCustomerReferenceNo());
//						res.setRequestReferenceNo(saveData.getRequestReferenceNo());
//						res.setCreatedBy(req.getCreatedBy());
//						resList.add(res);
//					}
		//
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				return resList;
//			}*/
		///*
//			@Override
//			public List<SlideSectionSaveRes> saveAllRiskDetails(AllRiskDetailsReq req) {
//				// TODO Auto-generated method stub
//				List<SlideSectionSaveRes> resList = new ArrayList<SlideSectionSaveRes>();
//				EserviceBuildingDetails saveData = new EserviceBuildingDetails();
//				EserviceSectionDetails sectionData = new EserviceSectionDetails();
//				DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
//				try {
////					saveData = buildingRepo.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo() , Integer.valueOf(req.getRiskId()),req.getSectionId());
////					sectionData = eserSecRepo.findByRequestReferenceNoAndRiskIdAndProductIdAndSectionId(req.getRequestReferenceNo(),Integer.valueOf(req.getRiskId()) , req.getProductId() ,req.getSectionId());
		//
//					Double totalSumInsured = null;
//					Map<Integer, List<ContentAndRisk>> riskGroup = null;
		//
//					List<ContentAndRisk> contentList = contentRepo
//							.findByRequestReferenceNoAndSectionId(req.getRequestReferenceNo(), req.getSectionId());
//					if (contentList.size() > 0 && contentList != null) {
		//
//						riskGroup = contentList.stream().filter(o -> o.getRiskId() != null)
//								.collect(Collectors.groupingBy(ContentAndRisk::getRiskId));
		//
//					}
//					List<Integer> riskIds = contentList.stream()
//							.map(a -> a.getRiskId() == null ? 0 : Integer.valueOf(a.getRiskId())).collect(Collectors.toList());
		//
//					try {
//						if (null != riskIds && !riskIds.stream().anyMatch(a -> a == 1)) {
		//
//							riskIds.add(1);
//							List<EserviceBuildingDetails> existingData = buildingRepo
//									.findByRequestReferenceNoAndSectionIdAndRiskId(req.getRequestReferenceNo(),
//											req.getSectionId(), 1);
//							if (null != existingData && !existingData.isEmpty()) {
		//
//								existingData.stream().forEach(a -> {
		//
////									a.setAllriskSuminsured(null);
//									buildingRepo.save(a);
//								});
		//
//							}
//						}
//					} catch (Exception e) {
		//
//						log.error("Exception Occurs When Get The Existing Data In ALl Risk Save Api");
//						log.error(e.getMessage());
//						e.printStackTrace();
//						// throw new
		//
//					}
		//
//					List<EserviceBuildingDetails> exist = buildingRepo.findByRequestReferenceNoAndSectionIdAndRiskIdNotIn(
//							req.getRequestReferenceNo(), req.getSectionId(), riskIds);
//					if (exist != null && exist.size() > 0) {
		//
//						buildingRepo.deleteAll(exist);
//					}
		//
//					for (Integer risk : riskGroup.keySet()) {
		//
//						List<ContentAndRisk> con = contentRepo.findByRequestReferenceNoAndSectionIdAndRiskId(
//								req.getRequestReferenceNo(), req.getSectionId(), risk);
		//
//						totalSumInsured = con.stream().filter(o -> o.getSumInsured() != null)
//								.mapToDouble(o -> o.getSumInsured().doubleValue()).sum();
		//
//						EserviceBuildingDetails data = buildingRepo.findByRequestReferenceNoAndRiskIdAndSectionId(
//								req.getRequestReferenceNo(), 1, req.getSectionId());
//						sectionData = eserSecRepo.findByRequestReferenceNoAndRiskIdAndProductIdAndSectionId(
//								req.getRequestReferenceNo(), Integer.valueOf(req.getRiskId()), req.getProductId(),
//								req.getSectionId());
//						if (data != null) {
//							dozerBeanMapper.map(data, saveData);
		//
//						}
//						saveData.setRiskId(risk);
		//
////					saveData.setAllriskSuminsured(StringUtils.isBlank(req.getAllriskSuminsured()) ? new BigDecimal(0):new BigDecimal(req.getAllriskSuminsured()));
////						saveData.setAllriskSuminsured(
////								totalSumInsured == null ? new BigDecimal(0) : new BigDecimal(totalSumInsured));
////						saveData.setMiningPlantSi(StringUtils.isBlank(req.getMiningPlantSi()) ? new BigDecimal(0)
////								: new BigDecimal(req.getMiningPlantSi()));
////						saveData.setNonminingPlantSi(StringUtils.isBlank(req.getNonminingPlantSi()) ? new BigDecimal(0)
////								: new BigDecimal(req.getNonminingPlantSi()));
////						saveData.setGensetsSi(StringUtils.isBlank(req.getGensetsSi()) ? new BigDecimal(0)
////								: new BigDecimal(req.getGensetsSi()));
////						saveData.setEquipmentSi(
////								StringUtils.isNotBlank(req.getEquipmentSi()) && "100004".equalsIgnoreCase(req.getInsuranceId())
////										? new BigDecimal(req.getEquipmentSi())
////										: saveData.getEquipmentSi());
//						if (StringUtils.isNotBlank(saveData.getExchangeRate().toString())) {
//							BigDecimal exRate = saveData.getExchangeRate();
		//
//							// Lc Calc
//					/*		if (totalSumInsured > 0.0) {
//					//			saveData.setAllriskSuminsuredLc(new BigDecimal(totalSumInsured).multiply(exRate));
		//
//							} else {
//					//			saveData.setAllriskSuminsuredLc(BigDecimal.ZERO);
//							} */
		//
//					/*		if (StringUtils.isNotBlank(req.getMiningPlantSi())) {
//								saveData.setMiningPlantSiLc(new BigDecimal(req.getMiningPlantSi()).multiply(exRate));
		//
//							} else {
//								saveData.setMiningPlantSiLc(BigDecimal.ZERO);
//							}
		//
//							if (StringUtils.isNotBlank(req.getGensetsSi())) {
//								saveData.setGensetsSiLc(new BigDecimal(req.getGensetsSi()).multiply(exRate));
		//
//							} else {
//								saveData.setGensetsSiLc(BigDecimal.ZERO);
//							}
		//
//							if (StringUtils.isNotBlank(req.getNonminingPlantSi())) {
//								saveData.setNonminingPlantSiLc(new BigDecimal(req.getNonminingPlantSi()).multiply(exRate));
		//
//							} else {
//								saveData.setNonminingPlantSiLc(BigDecimal.ZERO);
//							}
		//
//							if (StringUtils.isNotBlank(req.getEquipmentSi())) {
//								saveData.setEquipmentSiLc(new BigDecimal(req.getEquipmentSi()).multiply(exRate));
		//
//							} else {
//								saveData.setEquipmentSiLc(BigDecimal.ZERO);
//							} */
//						}
//						// Endorsement CHanges
//						if (!(req.getEndorsementType() == null || req.getEndorsementType() == 0))
		//
//						{
		//
//							saveData.setOriginalPolicyNo(req.getOriginalPolicyNo());
//							saveData.setEndorsementDate(req.getEndorsementDate());
//							saveData.setEndorsementRemarks(req.getEndorsementRemarks());
//							saveData.setEndorsementEffdate(req.getEndorsementEffdate());
//							saveData.setEndtPrevPolicyNo(req.getEndtPrevPolicyNo());
//							saveData.setEndtPrevQuoteNo(req.getEndtPrevQuoteNo());
//							saveData.setEndtCount(req.getEndtCount());
//							saveData.setEndtStatus(req.getEndtStatus());
//							saveData.setIsFinyn(req.getIsFinaceYn());
//							saveData.setEndtCategDesc(req.getEndtCategDesc());
//							saveData.setEndorsementType(req.getEndorsementType());
//							saveData.setEndorsementTypeDesc(req.getEndorsementTypeDesc());
		//
//						}
//						// Status
//						saveData.setStatus(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus());
//						buildingRepo.save(saveData);
		//
//						try {
//							List<OneTimeTableRes> otResList = null;
//							// Section Req
//							List<BuildingSectionRes> sectionList = new ArrayList<BuildingSectionRes>();
//							BuildingSectionRes sec = new BuildingSectionRes();
//							sec.setMotorYn(sectionData.getProductType());
//							sec.setSectionId(sectionData.getSectionId());
//							sec.setSectionName(sectionData.getSectionName());
//							sectionList.add(sec);
		//
//							// One Time Table Thread Call
//							OneTimeTableReq otReq = new OneTimeTableReq();
//							otReq.setRequestReferenceNo(saveData.getRequestReferenceNo());
//							otReq.setVehicleId(saveData.getRiskId());
//							otReq.setBranchCode(saveData.getBranchCode());
//							otReq.setInsuranceId(saveData.getCompanyId());
//							otReq.setProductId(Integer.valueOf(saveData.getProductId()));
//							otReq.setSectionList(sectionList);
		//
//							otResList = otService.call_OT_Insert(otReq);
		//
//							for (OneTimeTableRes otRes : otResList) {
//								SlideSectionSaveRes res = new SlideSectionSaveRes();
//								res.setResponse("Saved Successfully");
//								res.setRiskId(otRes.getVehicleId());
//								res.setVdRefNo(otRes.getVdRefNo());
//								res.setCdRefNo(otRes.getCdRefNo());
//								res.setMsrefno(otRes.getMsRefNo());
//								res.setCompanyId(otRes.getCompanyId());
//								res.setProductId(otRes.getProductId());
//								res.setSectionId(otRes.getSectionId());
//								res.setCustomerReferenceNo(saveData.getCustomerReferenceNo());
//								res.setRequestReferenceNo(saveData.getRequestReferenceNo());
//								res.setCreatedBy(req.getCreatedBy());
//								resList.add(res);
//							}
		//
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Log Details" + e.getMessage());
		//
//				}
//				return resList;
		//
//			}*/
		//
//			/*@Override
//			public List<SlideSectionSaveRes> saveBurglaryAndHouseBreakingDetails(BurglaryAndHouseBreakingSaveReq req) {
//				// TODO Auto-generated method stub
//				List<SlideSectionSaveRes> resList = new ArrayList<SlideSectionSaveRes>();
//				EserviceBuildingDetails saveData = new EserviceBuildingDetails();
//				EserviceSectionDetails sectionData = new EserviceSectionDetails();
//				try {
//					saveData = buildingRepo.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo(),
//							Integer.valueOf(req.getRiskId()), req.getSectionId());
//					sectionData = eserSecRepo.findByRequestReferenceNoAndRiskIdAndProductIdAndSectionId(
//							req.getRequestReferenceNo(), Integer.valueOf(req.getRiskId()), req.getProductId(),
//							req.getSectionId());
		//
//					if ("100002".equalsIgnoreCase(req.getInsuranceId())) {
////						saveData.setBurglarySi(StringUtils.isBlank(req.getBurglarySi()) ? new BigDecimal(0)
////								: new BigDecimal(req.getBurglarySi()));
//						if (StringUtils.isNotBlank(saveData.getExchangeRate().toString())) {
//							BigDecimal exRate = saveData.getExchangeRate();
//							if (StringUtils.isNotBlank(req.getBurglarySi())) {
////								saveData.setBurglarySiLc(new BigDecimal(req.getBurglarySi()).multiply(exRate));
//								;
//							} else {
////								saveData.setBurglarySiLc(BigDecimal.ZERO);
//							}
//						}
//					} else {
//						saveData.setStockLossPercent(StringUtils.isBlank(req.getStockLossPercent()) ? null
//								: Integer.valueOf(req.getStockLossPercent()));
//						saveData.setGoodsLossPercent(StringUtils.isBlank(req.getGoodsLossPercent()) ? null
//								: Integer.valueOf(req.getGoodsLossPercent()));
//						saveData.setFurnitureLossPercent(StringUtils.isBlank(req.getFurnitureLossPercent()) ? null
//								: Integer.valueOf(req.getFurnitureLossPercent()));
//						saveData.setApplianceLossPercent(StringUtils.isBlank(req.getApplianceLossPercent()) ? null
//								: Integer.valueOf(req.getApplianceLossPercent()));
//						saveData.setCashValueablesLossPercent(StringUtils.isBlank(req.getCashValueablesLossPercent()) ? null
//								: Integer.valueOf(req.getCashValueablesLossPercent()));
		//
//						// Lc Insert
//						if (StringUtils.isNotBlank(saveData.getExchangeRate().toString())) {
//							BigDecimal exRate = saveData.getExchangeRate();
//							if (StringUtils.isNotBlank(req.getStockInTradeSi())) {
//								saveData.setStockInTradeSiLc(new BigDecimal(req.getStockInTradeSi()).multiply(exRate));
//							} else {
//								saveData.setStockInTradeSiLc(BigDecimal.ZERO);
//							}
//							if (StringUtils.isNotBlank(req.getGoodsSi())) {
//								saveData.setGoodsSiLc(new BigDecimal(req.getGoodsSi()).multiply(exRate));
//							} else {
//								saveData.setGoodsSiLc(BigDecimal.ZERO);
//							}
//							if (StringUtils.isNotBlank(req.getFurnitureSi())) {
//								saveData.setFurnitureSiLc(new BigDecimal(req.getFurnitureSi()).multiply(exRate));
//							} else {
//								saveData.setFurnitureSiLc(BigDecimal.ZERO);
//							}
//							if (StringUtils.isNotBlank(req.getApplianceSi())) {
//								saveData.setApplianceSiLc(new BigDecimal(req.getApplianceSi()).multiply(exRate));
//							} else {
//								saveData.setApplianceSiLc(BigDecimal.ZERO);
//							}
//							if (StringUtils.isNotBlank(req.getCashValueablesSi())) {
//								saveData.setCashValueablesSiLc(new BigDecimal(req.getCashValueablesSi()).multiply(exRate));
//							} else {
//								saveData.setCashValueablesSiLc(BigDecimal.ZERO);
//							}
//						}
//						saveData.setStockInTradeSi(StringUtils.isBlank(req.getStockInTradeSi()) ? new BigDecimal(0)
//								: new BigDecimal(req.getStockInTradeSi()));
//						saveData.setGoodsSi(
//								StringUtils.isBlank(req.getGoodsSi()) ? new BigDecimal(0) : new BigDecimal(req.getGoodsSi()));
//						saveData.setFurnitureSi(StringUtils.isBlank(req.getFurnitureSi()) ? new BigDecimal(0)
//								: new BigDecimal(req.getFurnitureSi()));
//						saveData.setApplianceSi(StringUtils.isBlank(req.getApplianceSi()) ? new BigDecimal(0)
//								: new BigDecimal(req.getApplianceSi()));
//						saveData.setCashValueablesSi(StringUtils.isBlank(req.getCashValueablesSi()) ? new BigDecimal(0)
//								: new BigDecimal(req.getCashValueablesSi()));
//						saveData.setBackDoors(
//								StringUtils.isBlank(req.getBackDoors()) ? null : Integer.valueOf(req.getBackDoors()));
//						saveData.setBuildingOccupied(StringUtils.isBlank(req.getBuildingOccupied()) ? 0
//								: Integer.valueOf(req.getBuildingOccupied()));
//						saveData.setCeilingType(
//								StringUtils.isBlank(req.getCeilingType()) ? 0 : Integer.valueOf(req.getCeilingType()));
//						saveData.setDistrictCode(StringUtils.isBlank(req.getDistrictCode()) ? "" : (req.getDistrictCode()));
//						saveData.setDoorsMaterialId(
//								StringUtils.isBlank(req.getDoorsMaterialId()) ? 0 : Integer.valueOf(req.getDoorsMaterialId()));
//						saveData.setFrontDoors(
//								StringUtils.isBlank(req.getFrontDoors()) ? null : Integer.valueOf(req.getFrontDoors()));
//						saveData.setAccessibleWindows(StringUtils.isBlank(req.getAccessibleWindows()) ? null
//								: Integer.valueOf(req.getAccessibleWindows()));
//						saveData.setAddress(StringUtils.isBlank(req.getAddress()) ? "" : (req.getAddress()));
//						saveData.setBuildingOwnerYn(
//								StringUtils.isBlank(req.getBuildingOwnerYn()) ? "N" : (req.getBuildingOwnerYn()));
//						// saveData.setBurglarySi(StringUtils.isBlank(req.getBurglarySi()) ? new
//						// BigDecimal(0): new BigDecimal(req.getBurglarySi()));
//						if (req.getInsuranceForId() != null && req.getInsuranceForId().size() > 0) {
//							String insuranceForIds = "";
//							String insuranceForDesc = "";
//							for (String id : req.getInsuranceForId()) {
//								String insDesc = getListItem(saveData.getCompanyId(), saveData.getBranchCode(),
//										"BURGLARY_INSURANCE_FOR", id);
		//
//								insuranceForIds = StringUtils.isBlank(insuranceForIds) ? id : insuranceForIds + "," + id;
//								insuranceForDesc = StringUtils.isBlank(insuranceForDesc) ? insDesc
//										: insuranceForDesc + "~" + insDesc;
//							}
		//
//							saveData.setInsuranceForId(insuranceForIds);
//							saveData.setInsuranceForDesc(insuranceForDesc);
		//
//						}
//						if (StringUtils.isNotBlank(req.getNatureOfTradeId())) {
//							String naturOfTrade = getListItem(saveData.getCompanyId(), saveData.getBranchCode(),
//									"NATURE_OF_TRADE", req.getNatureOfTradeId());
//							saveData.setNatureOfTradeId(Integer.valueOf(req.getNatureOfTradeId()));
//							saveData.setNatureOfTradeDesc(naturOfTrade);
		//
//						}
		//
//						saveData.setInternalWallType(StringUtils.isBlank(req.getInternalWallType()) ? null
//								: Integer.valueOf(req.getInternalWallType()));
//						saveData.setNightLeftDoor(
//								StringUtils.isBlank(req.getNightLeftDoor()) ? null : Integer.valueOf(req.getNightLeftDoor()));
//						saveData.setOccupiedYear(
//								StringUtils.isBlank(req.getOccupiedYear()) ? null : Integer.valueOf(req.getOccupiedYear()));
//						saveData.setShowWindows(
//								StringUtils.isBlank(req.getShowWindow()) ? null : Integer.valueOf(req.getShowWindow()));
//						saveData.setTrapDoors(
//								StringUtils.isBlank(req.getTrapDoors()) ? null : Integer.valueOf(req.getTrapDoors()));
//						saveData.setWatchmanGuardHours(StringUtils.isBlank(req.getWatchmanGuardHours()) ? null
//								: Integer.valueOf(req.getWatchmanGuardHours()));
//						saveData.setWindowsMaterialId(StringUtils.isBlank(req.getWindowsMaterialId()) ? null
//								: Integer.valueOf(req.getWindowsMaterialId()));
//						if (StringUtils.isNotBlank(req.getWallType())) {
//							String wallType = getListItem(saveData.getCompanyId(), saveData.getBranchCode(), "WALL_TYPE",
//									req.getWallType());
//							saveData.setWallType(req.getWallType());
//							saveData.setWallTypeDesc(wallType);
		//
//						}
		//
//						if (StringUtils.isNotBlank(req.getRoofType())) {
//							String roofType = getListItem(saveData.getCompanyId(), saveData.getBranchCode(), "ROOF_TYPE",
//									req.getRoofType());
//							saveData.setRoofType(req.getRoofType());
//							saveData.setRoofTypeDesc(roofType);
		//
//						}
//						saveData.setBuildingBuildYear(Integer.valueOf(req.getBuildingBuildYear()));
//						saveData.setRegionCode(req.getRegionCode());
//						saveData.setDistrictCode(req.getDistrictCode());
//					}
		//
//					// Endorsement CHanges
//					if (!(req.getEndorsementType() == null || req.getEndorsementType() == 0))
		//
//					{
		//
//						saveData.setOriginalPolicyNo(req.getOriginalPolicyNo());
//						saveData.setEndorsementDate(req.getEndorsementDate());
//						saveData.setEndorsementRemarks(req.getEndorsementRemarks());
//						saveData.setEndorsementEffdate(req.getEndorsementEffdate());
//						saveData.setEndtPrevPolicyNo(req.getEndtPrevPolicyNo());
//						saveData.setEndtPrevQuoteNo(req.getEndtPrevQuoteNo());
//						saveData.setEndtCount(req.getEndtCount());
//						saveData.setEndtStatus(req.getEndtStatus());
//						saveData.setIsFinyn(req.getIsFinaceYn());
//						saveData.setEndtCategDesc(req.getEndtCategDesc());
//						saveData.setEndorsementType(req.getEndorsementType());
//						saveData.setEndorsementTypeDesc(req.getEndorsementTypeDesc());
		//
//					}
		//
//					// Status
//					saveData.setStatus(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus());
//					buildingRepo.save(saveData);
		//

//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Log Details" + e.getMessage());
//				}
//				try {

//						List<OneTimeTableRes> otResList = null;
//						// Section Req
//						List<BuildingSectionRes> sectionList = new ArrayList<BuildingSectionRes>();
//						BuildingSectionRes sec = new BuildingSectionRes();
//						sec.setMotorYn(sectionData.getProductType());
//						sec.setSectionId(sectionData.getSectionId());
//						sec.setSectionName(sectionData.getSectionName());
//						sectionList.add(sec);
		//
//						// One Time Table Thread Call
//						OneTimeTableReq otReq = new OneTimeTableReq();
//						otReq.setRequestReferenceNo(saveData.getRequestReferenceNo());
//						otReq.setVehicleId(saveData.getRiskId());
//						otReq.setBranchCode(saveData.getBranchCode());
//						otReq.setInsuranceId(saveData.getCompanyId());
//						otReq.setProductId(Integer.valueOf(saveData.getProductId()));
//						otReq.setSectionList(sectionList);
		//
//						otResList = otService.call_OT_Insert(otReq);
//						for (OneTimeTableRes otRes : otResList) {
//							SlideSectionSaveRes res = new SlideSectionSaveRes();
//							res.setResponse("Saved Successfully");
//							res.setRiskId(otRes.getVehicleId());
//							res.setVdRefNo(otRes.getVdRefNo());
//							res.setCdRefNo(otRes.getCdRefNo());
//							res.setMsrefno(otRes.getMsRefNo());
//							res.setCompanyId(otRes.getCompanyId());
//							res.setProductId(otRes.getProductId());
//							res.setSectionId(otRes.getSectionId());
//							res.setCustomerReferenceNo(saveData.getCustomerReferenceNo());
//							res.setRequestReferenceNo(saveData.getRequestReferenceNo());
//							res.setCreatedBy(req.getCreatedBy());
//							resList.add(res);
//						}
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				return resList;
//			}

//			@Override
//			public List<SlideSectionSaveRes> saveFireAndAlliedPerillsDetails(FireAndAlliedPerillsSaveReq req) {
//				// TODO Auto-generated method stub
//				List<SlideSectionSaveRes> resList = new ArrayList<SlideSectionSaveRes>();
//				EserviceBuildingDetails saveData = new EserviceBuildingDetails();
//				EserviceSectionDetails sectionData = new EserviceSectionDetails();
		//
//				try {
//					saveData = buildingRepo.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo(),
//							Integer.valueOf(req.getRiskId()), req.getSectionId());
//					sectionData = eserSecRepo.findByRequestReferenceNoAndRiskIdAndProductIdAndSectionId(
//							req.getRequestReferenceNo(), Integer.valueOf(req.getRiskId()), req.getProductId(),
//							req.getSectionId());
		//
//					saveData.setFirePlantSi(StringUtils.isBlank(req.getFirePlantSi()) ? new BigDecimal(0)
//							: new BigDecimal(req.getFirePlantSi()));
//					saveData.setStockInTradeSi(StringUtils.isBlank(req.getStockInTradeSi()) ? new BigDecimal(0)
//							: new BigDecimal(req.getStockInTradeSi()));
//					saveData.setBuildingSuminsured(StringUtils.isBlank(req.getBuildingSuminsured()) ? new BigDecimal(0)
//							: new BigDecimal(req.getBuildingSuminsured()));
//					saveData.setEquipmentSi(StringUtils.isBlank(req.getFireEquipSi()) ? new BigDecimal(0)
//							: new BigDecimal(req.getFireEquipSi()));
//					saveData.setOnStockSi(
//							StringUtils.isBlank(req.getOnStockSi()) ? new BigDecimal(0) : new BigDecimal(req.getOnStockSi()));
//					saveData.setOnAssetsSi(
//							StringUtils.isBlank(req.getOnAssetsSi()) ? new BigDecimal(0) : new BigDecimal(req.getOnAssetsSi()));
		//
//					saveData.setMakutiYn(req.getMakutiYn());
		//
//					if (StringUtils.isNotBlank(saveData.getExchangeRate().toString())) {
//						BigDecimal exRate = saveData.getExchangeRate();
//						if (StringUtils.isNotBlank(req.getBuildingSuminsured())) {
//							saveData.setBuildingSuminsuredLc(new BigDecimal(req.getBuildingSuminsured()).multiply(exRate));
//						} else {
//							saveData.setBuildingSuminsuredLc(BigDecimal.ZERO);
//						}
		//
//						if (StringUtils.isNotBlank(req.getOnStockSi())) {
//							saveData.setOnStockSiLc(new BigDecimal(req.getOnStockSi()).multiply(exRate));
//						} else {
//							saveData.setOnStockSiLc(BigDecimal.ZERO);
//						}
		//
//						if (StringUtils.isNotBlank(req.getOnAssetsSi())) {
//							saveData.setOnAssetsSiLc(new BigDecimal(req.getOnAssetsSi()).multiply(exRate));
//						} else {
//							saveData.setOnAssetsSiLc(BigDecimal.ZERO);
//						}
		//
//						if (StringUtils.isNotBlank(req.getBuildingSuminsured())) {
//							saveData.setBuildingSuminsuredLc(new BigDecimal(req.getBuildingSuminsured()).multiply(exRate));
//						} else {
//							saveData.setBuildingSuminsuredLc(BigDecimal.ZERO);
//						}
		//
//						if (StringUtils.isNotBlank(req.getFirePlantSi())) {
//							saveData.setFirePlantSiLc(new BigDecimal(req.getFirePlantSi()).multiply(exRate));
//						} else {
//							saveData.setFirePlantSiLc(BigDecimal.ZERO);
//						}
//						if (StringUtils.isNotBlank(req.getStockInTradeSi())) {
//							saveData.setStockInTradeSiLc(new BigDecimal(req.getStockInTradeSi()).multiply(exRate));
//						} else {
//							saveData.setStockInTradeSiLc(BigDecimal.ZERO);
//						}
//						if (StringUtils.isNotBlank(req.getBuildingSuminsured())) {
//							saveData.setBuildingSuminsuredLc(new BigDecimal(req.getBuildingSuminsured()).multiply(exRate));
//						} else {
//							saveData.setBuildingSuminsuredLc(BigDecimal.ZERO);
//						}
//						if (StringUtils.isNotBlank(req.getFireEquipSi())) {
//							saveData.setEquipmentSiLc(new BigDecimal(req.getFireEquipSi()).multiply(exRate));
//						} else {
//							saveData.setEquipmentSiLc(BigDecimal.ZERO);
//						}
//					}
//					if (StringUtils.isNotBlank(req.getIndemityPeriod())) {
//						String indemityPeriodDesc = getListItem(req.getInsuranceId(), saveData.getBranchCode(),
//								"INDEMITY_PERIOD", req.getIndemityPeriod());
//						saveData.setIndemityPeriod(req.getIndemityPeriod());
//						saveData.setIndemityPeriodDesc(indemityPeriodDesc);
//					}
		//
//					// Endorsement CHanges
//					if (!(req.getEndorsementType() == null || req.getEndorsementType() == 0))
		//
//					{
		//
//						saveData.setOriginalPolicyNo(req.getOriginalPolicyNo());
//						saveData.setEndorsementDate(req.getEndorsementDate());
//						saveData.setEndorsementRemarks(req.getEndorsementRemarks());
//						saveData.setEndorsementEffdate(req.getEndorsementEffdate());
//						saveData.setEndtPrevPolicyNo(req.getEndtPrevPolicyNo());
//						saveData.setEndtPrevQuoteNo(req.getEndtPrevQuoteNo());
//						saveData.setEndtCount(req.getEndtCount());
//						saveData.setEndtStatus(req.getEndtStatus());
//						saveData.setIsFinyn(req.getIsFinaceYn());
//						saveData.setEndtCategDesc(req.getEndtCategDesc());
//						saveData.setEndorsementType(req.getEndorsementType());
//						saveData.setEndorsementTypeDesc(req.getEndorsementTypeDesc());
		//
//					}
//					// Status
//					saveData.setStatus(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus());
//					buildingRepo.save(saveData);
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Log Details" + e.getMessage());
		//
//				}
//				try {
//					List<OneTimeTableRes> otResList = null;
//					// Section Req
//					List<BuildingSectionRes> sectionList = new ArrayList<BuildingSectionRes>();
//					BuildingSectionRes sec = new BuildingSectionRes();
//					sec.setMotorYn(sectionData.getProductType());
//					sec.setSectionId(sectionData.getSectionId());
//					sec.setSectionName(sectionData.getSectionName());
//					sectionList.add(sec);
		//
//					// One Time Table Thread Call
//					OneTimeTableReq otReq = new OneTimeTableReq();
//					otReq.setRequestReferenceNo(saveData.getRequestReferenceNo());
//					otReq.setVehicleId(saveData.getRiskId());
//					otReq.setBranchCode(saveData.getBranchCode());
//					otReq.setInsuranceId(saveData.getCompanyId());
//					otReq.setProductId(Integer.valueOf(saveData.getProductId()));
//					otReq.setSectionList(sectionList);
		//
//					otResList = otService.call_OT_Insert(otReq);
//					for (OneTimeTableRes otRes : otResList) {
//						SlideSectionSaveRes res = new SlideSectionSaveRes();
//						res.setResponse("Saved Successfully");
//						res.setRiskId(otRes.getVehicleId());
//						res.setVdRefNo(otRes.getVdRefNo());
//						res.setCdRefNo(otRes.getCdRefNo());
//						res.setMsrefno(otRes.getMsRefNo());
//						res.setCompanyId(otRes.getCompanyId());
//						res.setProductId(otRes.getProductId());
//						res.setSectionId(otRes.getSectionId());
//						res.setCustomerReferenceNo(saveData.getCustomerReferenceNo());
//						res.setRequestReferenceNo(saveData.getRequestReferenceNo());
//						res.setCreatedBy(req.getCreatedBy());
//						resList.add(res);
//					}
		//
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				return resList;
//			}

//			@Override
//			public List<SlideSectionSaveRes> saveSlideFidelityGuarantyDetails(List<SlideFidelityGuarantySaveReq> reqList) {
//				// TODO Auto-generated method stub
//				List<SlideSectionSaveRes> resList = new ArrayList<SlideSectionSaveRes>();
//				EserviceBuildingDetails saveData = new EserviceBuildingDetails();
//				List<EserviceCommonDetails> humanDatas = new ArrayList<EserviceCommonDetails>();
//				EserviceCommonDetails humanData = new EserviceCommonDetails();
//				EserviceSectionDetails sectionData = new EserviceSectionDetails();
//				DozerBeanMapper dozerMapper = new DozerBeanMapper();
//				List<BuildingSectionRes> sectionList = new ArrayList<BuildingSectionRes>();
		//
//				try {
//					BigDecimal endtCount = BigDecimal.ZERO;
//					String refNo = reqList.get(0).getRequestReferenceNo();
//					String productId = reqList.get(0).getProductId();
//					String sectionId = reqList.get(0).getSectionId();
		//
//					saveData = buildingRepo.findByRequestReferenceNoAndRiskIdAndSectionId(refNo, 1, "0");
//					if (saveData != null) {
		//
//						dozerMapper.map(saveData, humanData);
//						humanData.setPolicyPeriod(saveData.getPolicyPeriord());
		//
//					}
//					humanDatas = humanRepo.findByRequestReferenceNoAndSectionId(refNo, sectionId);
//					SlideFidelityGuarantySaveReq firstRow = reqList.get(0);
		//
//					if ((firstRow.getEndorsementType() == null || firstRow.getEndorsementType() == 0)
//							&& humanDatas.size() > 0) {
//						humanRepo.deleteAll(humanDatas);
//						dozerMapper.map(humanDatas.get(0), humanData);
//					} else if (humanDatas.size() > 0) {
//						List<EserviceCommonDetails> humanData1 = new ArrayList<EserviceCommonDetails>();
//						if (!(firstRow.getEndorsementType() == null || firstRow.getEndorsementType() == 0)) {
		//
//							// search
//							if (reqList.get(0).getEndtCount().compareTo(BigDecimal.ONE) == 0) {
//								endtCount = reqList.get(0).getEndtCount();
//								humanData1 = humanRepo.findBySectionIdAndPolicyNoAndStatusNot(sectionId,
//										reqList.get(0).getOriginalPolicyNo(), "D");
//							} else {
//								endtCount = reqList.get(0).getEndtCount().subtract(BigDecimal.ONE);
//								humanData1 = humanRepo.findByEndtCountAndSectionIdAndOriginalPolicyNoAndStatusNot(endtCount,
//										sectionId, reqList.get(0).getOriginalPolicyNo(), "D");
//							}
		//
//							// delete all
//							humanRepo.deleteAll(humanDatas);
		//
//						}
//						List<EserviceCommonDetails> humanDatas2 = new ArrayList<EserviceCommonDetails>();
//						dozerMapper.map(humanDatas.get(0), humanData);
//						String policyNo = humanDatas.get(0).getPolicyNo();
//						humanData1.forEach(hum -> {
//							EserviceCommonDetails o = new EserviceCommonDetails();
//							dozerMapper.map(hum, o);
//							o.setStatus("D");
//							o.setRequestReferenceNo(refNo);
//							o.setQuoteNo("");
//							o.setPolicyNo(policyNo);
//							if (!(firstRow.getEndorsementType() == null || firstRow.getEndorsementType() == 0))
		//
//							{
		//
//								o.setOriginalPolicyNo(firstRow.getOriginalPolicyNo());
//								o.setEndorsementDate(firstRow.getEndorsementDate());
//								o.setEndorsementRemarks(firstRow.getEndorsementRemarks());
//								o.setEndorsementEffdate(firstRow.getEndorsementEffdate());
//								o.setEndtPrevPolicyNo(firstRow.getEndtPrevPolicyNo());
//								o.setEndtPrevQuoteNo(firstRow.getEndtPrevQuoteNo());
//								o.setEndtCount(firstRow.getEndtCount());
//								o.setEndtStatus(firstRow.getEndtStatus());
//								o.setIsFinyn(firstRow.getIsFinaceYn());
//								o.setEndtCategDesc(firstRow.getEndtCategDesc());
//								o.setEndorsementType(firstRow.getEndorsementType());
//								o.setEndorsementTypeDesc(firstRow.getEndorsementTypeDesc());
		//
//							}
		//
//							humanDatas2.add(o);
//						});
//						humanRepo.saveAllAndFlush(humanDatas2);
		//
//					}
		//
//					sectionData = eserSecRepo.findByRequestReferenceNoAndRiskIdAndProductIdAndSectionId(refNo, 1, productId,
//							sectionId);
//					List<EserviceCommonDetails> saveHumanList = new ArrayList<EserviceCommonDetails>();
//					for (SlideFidelityGuarantySaveReq data : reqList) {
//						// Save Human
//						EserviceCommonDetails saveHuman = new EserviceCommonDetails();
//						saveHuman = dozerMapper.map(humanData, EserviceCommonDetails.class);
//						saveHuman.setRequestReferenceNo(data.getRequestReferenceNo());
//						saveHuman.setRiskId(data.getOriginalRiskId() == null || StringUtils.isBlank(data.getOriginalRiskId())
//								? Integer.valueOf(data.getRiskId())
//								: Integer.valueOf(data.getOriginalRiskId()));
//						saveHuman.setOriginalRiskId(
//								data.getOriginalRiskId() == null || StringUtils.isBlank(data.getOriginalRiskId())
//										? Integer.valueOf(data.getRiskId())
//										: Integer.valueOf(data.getOriginalRiskId()));
//						saveHuman.setSectionId(data.getSectionId());
//						saveHuman.setSectionName(sectionData.getSectionName());
//						;
//						saveHuman.setCreatedBy(data.getCreatedBy());
//						saveHuman.setFidEmpSi(StringUtils.isBlank(data.getFidEmpSi()) ? new BigDecimal(0)
//								: new BigDecimal(data.getFidEmpSi()));
//						saveHuman.setSumInsured(StringUtils.isBlank(data.getFidEmpSi()) ? new BigDecimal(0)
//								: new BigDecimal(data.getFidEmpSi()));
//						// Personal Liability
//						if (StringUtils.isNotBlank(data.getLiabilityOccupationId())) {
//							OccupationMaster occupationData = getOccupationMasterDropdown(data.getInsuranceId(), "99999",
//									data.getProductId(), data.getLiabilityOccupationId());
//							saveHuman.setOccupationType(occupationData.getOccupationId().toString());
//							saveHuman.setOccupationDesc(occupationData.getOccupationName());
//							saveHuman.setCategoryId(occupationData.getCategoryId());
//							// saveHuman.setCategoryDesc("Class " + occupationData.getCategoryId());
//						}
//						saveHuman.setOtherOccupation(data.getOtherOccupation());
//						saveHuman.setFidEmpCount(
//								StringUtils.isBlank(data.getFidEmpCount()) ? null : new BigDecimal(data.getFidEmpCount()));
//						saveHuman.setTotalNoOfEmployees(
//								StringUtils.isBlank(data.getFidEmpCount()) ? null : Long.valueOf(data.getFidEmpCount()));
//						saveHuman.setCount(
//								StringUtils.isBlank(data.getFidEmpCount()) ? null : Integer.valueOf(data.getFidEmpCount()));
//						// Lc Calculation
//						BigDecimal exchangeRate = humanData.getExchangeRate() != null ? humanData.getExchangeRate()
//								: BigDecimal.ZERO;
//						saveHuman.setSumInsuredLc(
//								saveHuman.getSumInsured() == null ? null : saveHuman.getSumInsured().multiply(exchangeRate));
//						saveHuman.setLiabilitySiLc(
//								saveHuman.getLiabilitySi() == null ? null : saveHuman.getLiabilitySi().multiply(exchangeRate));
//						saveHuman.setFidEmpSiLc(
//								saveHuman.getFidEmpSi() == null ? null : saveHuman.getFidEmpSi().multiply(exchangeRate));
//						saveHuman.setEmpLiabilitySiLc(saveHuman.getEmpLiabilitySi() == null ? null
//								: saveHuman.getEmpLiabilitySi().multiply(exchangeRate));
//						saveHuman.setPersonalLiabilitySiLc(saveHuman.getPersonalLiabilitySi() == null ? null
//								: saveHuman.getPersonalLiabilitySi().multiply(exchangeRate));
		//
//						// Endorsement CHanges
//						if (!(data.getEndorsementType() == null || data.getEndorsementType() == 0))
		//
//						{
		//
//							saveHuman.setOriginalPolicyNo(data.getOriginalPolicyNo());
//							saveHuman.setEndorsementDate(data.getEndorsementDate());
//							saveHuman.setEndorsementRemarks(data.getEndorsementRemarks());
//							saveHuman.setEndorsementEffdate(data.getEndorsementEffdate());
//							saveHuman.setEndtPrevPolicyNo(data.getEndtPrevPolicyNo());
//							saveHuman.setEndtPrevQuoteNo(data.getEndtPrevQuoteNo());
//							saveHuman.setEndtCount(data.getEndtCount());
//							saveHuman.setEndtStatus(data.getEndtStatus());
//							saveHuman.setIsFinyn(data.getIsFinaceYn());
//							saveHuman.setEndtCategDesc(data.getEndtCategDesc());
//							saveHuman.setEndorsementType(data.getEndorsementType());
//							saveHuman.setEndorsementTypeDesc(data.getEndorsementTypeDesc());
//							saveHuman.setStatus("E");
		//
//						}
//						saveHumanList.add(saveHuman);
		//
//					}
//					humanRepo.saveAllAndFlush(saveHumanList);
//					if ("32".equalsIgnoreCase(productId)) {
//						updatesectionDetails(refNo);
//					}
		//
//					List<EserviceCommonDetails> esercommonDatas = eserCommonRepo
//							.findByRequestReferenceNoAndSectionId(humanData.getRequestReferenceNo(), humanData.getSectionId());
//					if (esercommonDatas.size() == 0) {
//						esercommonDatas = saveHumanList;
//					}
//					for (EserviceCommonDetails data : esercommonDatas) {
//						// Section Req
//						BuildingSectionRes sec = new BuildingSectionRes();
//						sec.setMotorYn(sectionData.getProductType());
//						sec.setSectionId(sectionData.getSectionId());
//						sec.setSectionName(sectionData.getSectionName());
//						sec.setOccupationId(data.getOccupationType());
//						sectionList.add(sec);
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Log Details" + e.getMessage());
		//
//				}
//				try {
//					List<OneTimeTableRes> otResList = null;
//					// One Time Table Thread Call
//					OneTimeTableReq otReq = new OneTimeTableReq();
//					SlideFidelityGuarantySaveReq req = reqList.get(0);
//					otReq.setRequestReferenceNo(req.getRequestReferenceNo());
//					otReq.setVehicleId(1);
//					otReq.setBranchCode(saveData != null ? saveData.getBranchCode() : humanData.getBranchCode());
//					otReq.setInsuranceId(saveData != null ? saveData.getCompanyId() : humanData.getCompanyId());
//					otReq.setProductId(Integer.valueOf(req.getProductId()));
//					otReq.setSectionList(sectionList);
		//
//					otResList = otService.call_OT_Insert(otReq);
//					for (OneTimeTableRes otRes : otResList) {
//						SlideSectionSaveRes res = new SlideSectionSaveRes();
//						res.setResponse("Saved Successfully");
//						res.setRiskId(otRes.getVehicleId());
//						res.setVdRefNo(otRes.getVdRefNo());
//						res.setCdRefNo(otRes.getCdRefNo());
//						res.setMsrefno(otRes.getMsRefNo());
//						res.setCompanyId(otRes.getCompanyId());
//						res.setProductId(otRes.getProductId());
//						res.setSectionId(otRes.getSectionId());
//						res.setCustomerReferenceNo(
//								saveData != null ? saveData.getCustomerReferenceNo() : humanData.getCustomerReferenceNo());
//						res.setRequestReferenceNo(
//								saveData != null ? saveData.getRequestReferenceNo() : humanData.getRequestReferenceNo());
//						res.setCreatedBy(reqList.get(0).getCreatedBy());
//						resList.add(res);
//					}
		//
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				return resList;
//			}
		//
//			@Override
//			public List<SlideSectionSaveRes> saveSlideMachineryBreakdownDetails(SlideMachineryBreakdownSaveReq req) {
//				// TODO Auto-generated method stub
//				List<SlideSectionSaveRes> resList = new ArrayList<SlideSectionSaveRes>();
//				EserviceBuildingDetails saveData = new EserviceBuildingDetails();
//				EserviceSectionDetails sectionData = new EserviceSectionDetails();
		//
//				try {
//					saveData = buildingRepo.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo(),
//							Integer.valueOf(req.getRiskId()), req.getSectionId());
//					sectionData = eserSecRepo.findByRequestReferenceNoAndRiskIdAndProductIdAndSectionId(
//							req.getRequestReferenceNo(), Integer.valueOf(req.getRiskId()), req.getProductId(),
//							req.getSectionId());
		//
//					saveData.setBoilerPlantsSi(StringUtils.isBlank(req.getBoilerPlantsSi()) ? new BigDecimal(0)
//							: new BigDecimal(req.getBoilerPlantsSi()));
//					saveData.setElecMachinesSi(StringUtils.isBlank(req.getElecMachinesSi()) ? new BigDecimal(0)
//							: new BigDecimal(req.getElecMachinesSi()));
//					saveData.setEquipmentSi(StringUtils.isBlank(req.getEquipmentSi()) ? new BigDecimal(0)
//							: new BigDecimal(req.getEquipmentSi()));
//					saveData.setGeneralMachineSi(StringUtils.isBlank(req.getGeneralMachineSi()) ? new BigDecimal(0)
//							: new BigDecimal(req.getGeneralMachineSi()));
//					saveData.setMachineEquipSi(StringUtils.isBlank(req.getMachineEquipSi()) ? new BigDecimal(0)
//							: new BigDecimal(req.getMachineEquipSi()));
//					saveData.setManuUnitsSi(StringUtils.isBlank(req.getManuUnitsSi()) ? new BigDecimal(0)
//							: new BigDecimal(req.getManuUnitsSi()));
//					saveData.setPowerPlantSi(StringUtils.isBlank(req.getPowerPlantSi()) ? new BigDecimal(0)
//							: new BigDecimal(req.getPowerPlantSi()));
//					saveData.setMachinerySi(StringUtils.isBlank(req.getMachinerySi()) ? new BigDecimal(0)
//							: new BigDecimal(req.getMachinerySi()));
		//
//					// Lc Insert
//					if (StringUtils.isNotBlank(saveData.getExchangeRate().toString())) {
//						BigDecimal exRate = saveData.getExchangeRate();
//						if (StringUtils.isNotBlank(req.getBoilerPlantsSi())) {
//							saveData.setBoilerPlantsSiLc(new BigDecimal(req.getBoilerPlantsSi()).multiply(exRate));
//						} else {
//							saveData.setBoilerPlantsSiLc(BigDecimal.ZERO);
//						}
//						if (StringUtils.isNotBlank(req.getElecMachinesSi())) {
//							saveData.setElecMachinesSiLc(new BigDecimal(req.getElecMachinesSi()).multiply(exRate));
//						} else {
//							saveData.setElecMachinesSiLc(BigDecimal.ZERO);
//						}
//						if (StringUtils.isNotBlank(req.getEquipmentSi())) {
//							saveData.setEquipmentSiLc(new BigDecimal(req.getEquipmentSi()).multiply(exRate));
//						} else {
//							saveData.setEquipmentSiLc(BigDecimal.ZERO);
//						}
//						if (StringUtils.isNotBlank(req.getGeneralMachineSi())) {
//							saveData.setGeneralMachineSiLc(new BigDecimal(req.getGeneralMachineSi()).multiply(exRate));
//						} else {
//							saveData.setGeneralMachineSiLc(BigDecimal.ZERO);
//						}
//						if (StringUtils.isNotBlank(req.getMachineEquipSi())) {
//							saveData.setMachineEquipSiLc(new BigDecimal(req.getMachineEquipSi()).multiply(exRate));
//						} else {
//							saveData.setMachineEquipSiLc(BigDecimal.ZERO);
//						}
//						if (StringUtils.isNotBlank(req.getManuUnitsSi())) {
//							saveData.setManuUnitsSiLc(new BigDecimal(req.getManuUnitsSi()).multiply(exRate));
//						} else {
//							saveData.setManuUnitsSiLc(BigDecimal.ZERO);
//						}
//						if (StringUtils.isNotBlank(req.getPowerPlantSi())) {
//							saveData.setPowerPlantSiLc(new BigDecimal(req.getPowerPlantSi()).multiply(exRate));
//						} else {
//							saveData.setPowerPlantSiLc(BigDecimal.ZERO);
//						}
		//
//						if (StringUtils.isNotBlank(req.getMachinerySi())) {
//							saveData.setMachinerySiLc(new BigDecimal(req.getMachinerySi()).multiply(exRate));
//						} else {
//							saveData.setMachinerySiLc(BigDecimal.ZERO);
//						}
//					}
		//
//					// Endorsement Changes
//					if (!(req.getEndorsementType() == null || req.getEndorsementType() == 0))
		//
//					{
		//
//						saveData.setOriginalPolicyNo(req.getOriginalPolicyNo());
//						saveData.setEndorsementDate(req.getEndorsementDate());
//						saveData.setEndorsementRemarks(req.getEndorsementRemarks());
//						saveData.setEndorsementEffdate(req.getEndorsementEffdate());
//						saveData.setEndtPrevPolicyNo(req.getEndtPrevPolicyNo());
//						saveData.setEndtPrevQuoteNo(req.getEndtPrevQuoteNo());
//						saveData.setEndtCount(req.getEndtCount());
//						saveData.setEndtStatus(req.getEndtStatus());
//						saveData.setIsFinyn(req.getIsFinaceYn());
//						saveData.setEndtCategDesc(req.getEndtCategDesc());
//						saveData.setEndorsementType(req.getEndorsementType());
//						saveData.setEndorsementTypeDesc(req.getEndorsementTypeDesc());
		//
//					}
//					// Status
//					saveData.setStatus(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus());
//					buildingRepo.save(saveData);
		//
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Log Details" + e.getMessage());
		//
//				}
//				try {
//					List<OneTimeTableRes> otResList = null;
//					// Section Req
//					List<BuildingSectionRes> sectionList = new ArrayList<BuildingSectionRes>();
//					BuildingSectionRes sec = new BuildingSectionRes();
//					sec.setMotorYn(sectionData.getProductType());
//					sec.setSectionId(sectionData.getSectionId());
//					sec.setSectionName(sectionData.getSectionName());
//					sectionList.add(sec);
		//
//					// One Time Table Thread Call
//					OneTimeTableReq otReq = new OneTimeTableReq();
//					otReq.setRequestReferenceNo(saveData.getRequestReferenceNo());
//					otReq.setVehicleId(saveData.getRiskId());
//					otReq.setBranchCode(saveData.getBranchCode());
//					otReq.setInsuranceId(saveData.getCompanyId());
//					otReq.setProductId(Integer.valueOf(saveData.getProductId()));
//					otReq.setSectionList(sectionList);
		//
//					otResList = otService.call_OT_Insert(otReq);
//					for (OneTimeTableRes otRes : otResList) {
//						SlideSectionSaveRes res = new SlideSectionSaveRes();
//						res.setResponse("Saved Successfully");
//						res.setRiskId(otRes.getVehicleId());
//						res.setVdRefNo(otRes.getVdRefNo());
//						res.setCdRefNo(otRes.getCdRefNo());
//						res.setMsrefno(otRes.getMsRefNo());
//						res.setCompanyId(otRes.getCompanyId());
//						res.setProductId(otRes.getProductId());
//						res.setSectionId(otRes.getSectionId());
//						res.setCustomerReferenceNo(saveData.getCustomerReferenceNo());
//						res.setRequestReferenceNo(saveData.getRequestReferenceNo());
//						res.setCreatedBy(req.getCreatedBy());
//						resList.add(res);
//					}
		//
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				return resList;
//			}
		//
//			@Override
//			public List<SlideSectionSaveRes> saveSlidePlateGlassDetails(SlidePlateGlassSaveReq req) {
//				// TODO Auto-generated method stub
//				List<SlideSectionSaveRes> resList = new ArrayList<SlideSectionSaveRes>();
//				EserviceBuildingDetails saveData = new EserviceBuildingDetails();
//				EserviceSectionDetails sectionData = new EserviceSectionDetails();
//				try {
//					saveData = buildingRepo.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo(),
//							Integer.valueOf(req.getRiskId()), req.getSectionId());
//					sectionData = eserSecRepo.findByRequestReferenceNoAndRiskIdAndProductIdAndSectionId(
//							req.getRequestReferenceNo(), Integer.valueOf(req.getRiskId()), req.getProductId(),
//							req.getSectionId());
		//
//					saveData.setPlateGlassSi(StringUtils.isBlank(req.getPlateGlassSi()) ? new BigDecimal(0)
//							: new BigDecimal(req.getPlateGlassSi()));
//					if (StringUtils.isNotBlank(saveData.getExchangeRate().toString())) {
//						BigDecimal exRate = saveData.getExchangeRate();
//						if (StringUtils.isNotBlank(req.getPlateGlassSi())) {
//							saveData.setPlateGlassSiLc(new BigDecimal(req.getPlateGlassSi()).multiply(exRate));
//						} else {
//							saveData.setPlateGlassSiLc(BigDecimal.ZERO);
//						}
//					}
		//
//					if (StringUtils.isNotBlank(req.getPlateGlassType())) {
//						String plateGlassTypeDesc = getListItem(saveData.getCompanyId(), saveData.getBranchCode(),
//								"PLATE_GLASS_TYPE", req.getPlateGlassType());
//						saveData.setPlateGlassType(req.getPlateGlassType());
//						saveData.setPlateGlassDesc(plateGlassTypeDesc);
//					}
//					// Status
//					saveData.setStatus(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus());
//					buildingRepo.save(saveData);
		//
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Log Details" + e.getMessage());
//				}
//				try {
//					List<OneTimeTableRes> otResList = null;
//					// Section Req
//					List<BuildingSectionRes> sectionList = new ArrayList<BuildingSectionRes>();
//					BuildingSectionRes sec = new BuildingSectionRes();
//					sec.setMotorYn(sectionData.getProductType());
//					sec.setSectionId(sectionData.getSectionId());
//					sec.setSectionName(sectionData.getSectionName());
//					sectionList.add(sec);
		//
//					// One Time Table Thread Call
//					OneTimeTableReq otReq = new OneTimeTableReq();
//					otReq.setRequestReferenceNo(saveData.getRequestReferenceNo());
//					otReq.setVehicleId(saveData.getRiskId());
//					otReq.setBranchCode(saveData.getBranchCode());
//					otReq.setInsuranceId(saveData.getCompanyId());
//					otReq.setProductId(Integer.valueOf(saveData.getProductId()));
//					otReq.setSectionList(sectionList);
		//
//					otResList = otService.call_OT_Insert(otReq);
//					for (OneTimeTableRes otRes : otResList) {
//						SlideSectionSaveRes res = new SlideSectionSaveRes();
//						res.setResponse("Saved Successfully");
//						res.setRiskId(otRes.getVehicleId());
//						res.setVdRefNo(otRes.getVdRefNo());
//						res.setCdRefNo(otRes.getCdRefNo());
//						res.setMsrefno(otRes.getMsRefNo());
//						res.setCompanyId(otRes.getCompanyId());
//						res.setProductId(otRes.getProductId());
//						res.setSectionId(otRes.getSectionId());
//						res.setCustomerReferenceNo(saveData.getCustomerReferenceNo());
//						res.setRequestReferenceNo(saveData.getRequestReferenceNo());
//						res.setCreatedBy(req.getCreatedBy());
//						resList.add(res);
//					}
		//
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				return resList;
//			}
		//
//			@Override
//			public List<SlideSectionSaveRes> saveSlidePublicLiablityDetails(SlidePublicLiabilitySaveReq req) {
//				// TODO Auto-generated method stub
//				List<SlideSectionSaveRes> resList = new ArrayList<SlideSectionSaveRes>();
//				EserviceBuildingDetails saveData = new EserviceBuildingDetails();
//				List<EserviceCommonDetails> humanDatas = new ArrayList<EserviceCommonDetails>();
//				EserviceCommonDetails humanData = new EserviceCommonDetails();
//				EserviceSectionDetails sectionData = new EserviceSectionDetails();
//				DozerBeanMapper dozerMapper = new DozerBeanMapper();
//				List<BuildingSectionRes> sectionList = new ArrayList<BuildingSectionRes>();
		//
//				try {
//					String refNo = req.getRequestReferenceNo();
//					String productId = req.getProductId();
//					String sectionId = req.getSectionId();
		//
//					saveData = buildingRepo.findByRequestReferenceNoAndRiskIdAndSectionId(refNo, 1, "0");
//					if (saveData != null) {
		//
//						dozerMapper.map(saveData, humanData);
//						humanData.setPolicyPeriod(saveData.getPolicyPeriord());
		//
//					} else {
//						humanDatas = humanRepo.findByRequestReferenceNoAndSectionId(refNo, sectionId);
//						if (humanDatas.size() > 0) {
//							humanRepo.deleteAll(humanDatas);
//							dozerMapper.map(humanDatas.get(0), humanData);
//						}
		//
//					}
		//
//					sectionData = eserSecRepo.findByRequestReferenceNoAndRiskIdAndProductIdAndSectionId(refNo, 1, productId,
//							sectionId);
		//
//					// Save Human
//					EserviceCommonDetails saveHuman = new EserviceCommonDetails();
//					saveHuman = dozerMapper.map(humanData, EserviceCommonDetails.class);
//					saveHuman.setRequestReferenceNo(req.getRequestReferenceNo());
//					saveHuman.setRiskId(Integer.valueOf(1));
//					saveHuman.setSectionId(req.getSectionId());
//					saveHuman.setSectionName(sectionData.getSectionName());
//					;
//					saveHuman.setCreatedBy(req.getCreatedBy());
		//
//					if ("43".equalsIgnoreCase(req.getProductId())) {
//						String pattern = "######";
//						DecimalFormat df = new DecimalFormat(pattern);
		//
//						// saveHuman.setSumInsured(StringUtils.isBlank(req.getAooSumInsured()) ? new
//						// BigDecimal(0) : new
//						// BigDecimal(df.format(Double.valueOf(req.getAooSumInsured()))) );
//						// saveHuman.setLiabilitySi(StringUtils.isBlank(req.getAggSumInsured()) ? new
//						// BigDecimal(0) : new
//						// BigDecimal(df.format(Double.valueOf(req.getAggSumInsured()))) ) ;
//						saveHuman.setAooSuminsured(StringUtils.isBlank(req.getAooSumInsured()) ? new BigDecimal(0)
//								: new BigDecimal(df.format(Double.valueOf(req.getAooSumInsured()))));
//						saveHuman.setAggSuminsured(StringUtils.isBlank(req.getAggSumInsured()) ? new BigDecimal(0)
//								: new BigDecimal(df.format(Double.valueOf(req.getAggSumInsured()))));
//						// Lc Calculation
//						BigDecimal exchangeRate = humanData.getExchangeRate() != null ? humanData.getExchangeRate()
//								: BigDecimal.ZERO;
//						saveHuman.setAooSuminsuredLc(saveHuman.getAooSuminsured() == null ? null
//								: saveHuman.getAooSuminsured().multiply(exchangeRate));
//						saveHuman.setAggSuminsuredLc(saveHuman.getAggSuminsured() == null ? null
//								: saveHuman.getAggSuminsured().multiply(exchangeRate));
//						saveHuman.setCategoryId(req.getCategory());
		//
//					} else if ("100002".equalsIgnoreCase(req.getInsuranceId())) {
//						saveHuman.setAnyAccidentSi(StringUtils.isBlank(req.getAnyAccidentSi()) ? new BigDecimal(0)
//								: new BigDecimal(req.getAnyAccidentSi()));
//						saveHuman.setInsurancePeriodSi(StringUtils.isBlank(req.getInsurancePeriodSi()) ? new BigDecimal(0)
//								: new BigDecimal(req.getInsurancePeriodSi()));
		//
//						// Lc Calculation
//						BigDecimal exchangeRate = humanData.getExchangeRate() != null ? humanData.getExchangeRate()
//								: BigDecimal.ZERO;
//						saveHuman.setInsurancePeriodSiLc(saveHuman.getInsurancePeriodSi() == null ? null
//								: saveHuman.getInsurancePeriodSi().multiply(exchangeRate));
//						saveHuman.setAnyAccidentSiLc(saveHuman.getAnyAccidentSi() == null ? null
//								: saveHuman.getInsurancePeriodSi().multiply(exchangeRate));
		//
//					} else {
//						saveHuman.setLiabilitySi(StringUtils.isBlank(req.getLiabilitySi()) ? new BigDecimal(0)
//								: new BigDecimal(req.getLiabilitySi()));
//						saveHuman.setProductTurnoverSi(StringUtils.isBlank(req.getProductTurnoverSi()) ? new BigDecimal(0)
//								: new BigDecimal(req.getProductTurnoverSi()));
		//
//						// Lc Calculation
//						BigDecimal exchangeRate = humanData.getExchangeRate() != null ? humanData.getExchangeRate()
//								: BigDecimal.ZERO;
//						saveHuman.setLiabilitySiLc(
//								saveHuman.getLiabilitySi() == null ? null : saveHuman.getLiabilitySi().multiply(exchangeRate));
//						saveHuman.setProductTurnoverSiLc(saveHuman.getProductTurnoverSi() == null ? null
//								: saveHuman.getProductTurnoverSi().multiply(exchangeRate));
//					}
//					// Status
//					saveHuman.setStatus(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus());
//					humanRepo.save(saveHuman);
		//
//					// Section Req
//					BuildingSectionRes sec = new BuildingSectionRes();
//					sec.setMotorYn(sectionData.getProductType());
//					sec.setSectionId(sectionData.getSectionId());
//					sec.setSectionName(sectionData.getSectionName());
//					sec.setOccupationId("1");
//					sectionList.add(sec);
		//
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Log Details" + e.getMessage());
		//
//				}
//				try {
//					List<OneTimeTableRes> otResList = null;
//					// One Time Table Thread Call
//					OneTimeTableReq otReq = new OneTimeTableReq();
//					otReq.setRequestReferenceNo(humanData.getRequestReferenceNo());
//					otReq.setVehicleId(humanData.getRiskId());
//					otReq.setBranchCode(humanData.getBranchCode());
//					otReq.setInsuranceId(humanData.getCompanyId());
//					otReq.setProductId(Integer.valueOf(humanData.getProductId()));
//					otReq.setSectionList(sectionList);
		//
//					otResList = otService.call_OT_Insert(otReq);
//					for (OneTimeTableRes otRes : otResList) {
//						SlideSectionSaveRes res = new SlideSectionSaveRes();
//						res.setResponse("Saved Successfully");
//						res.setRiskId(otRes.getVehicleId());
//						res.setVdRefNo(otRes.getVdRefNo());
//						res.setCdRefNo(otRes.getCdRefNo());
//						res.setMsrefno(otRes.getMsRefNo());
//						res.setCompanyId(otRes.getCompanyId());
//						res.setProductId(otRes.getProductId());
//						res.setSectionId(otRes.getSectionId());
//						res.setCustomerReferenceNo(humanData.getCustomerReferenceNo());
//						res.setRequestReferenceNo(humanData.getRequestReferenceNo());
//						res.setCreatedBy(req.getCreatedBy());
//						resList.add(res);
//					}
		//
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				return resList;
//			}

//					List<OneTimeTableRes> otResList = null;
//					// Section Req
//					List<BuildingSectionRes> sectionList = new ArrayList<BuildingSectionRes>();
//					BuildingSectionRes sec = new BuildingSectionRes();
//					sec.setMotorYn(sectionData.getProductType());
//					sec.setSectionId(sectionData.getSectionId());
//					sec.setSectionName(sectionData.getSectionName());
//					sectionList.add(sec);
		//
//					// One Time Table Thread Call
//					OneTimeTableReq otReq = new OneTimeTableReq();
//					otReq.setRequestReferenceNo(saveData.getRequestReferenceNo());
//					otReq.setVehicleId(saveData.getRiskId());
//					otReq.setBranchCode(saveData.getBranchCode());
//					otReq.setInsuranceId(saveData.getCompanyId());
//					otReq.setProductId(Integer.valueOf(saveData.getProductId()));
//					otReq.setSectionList(sectionList);
		//
//					otResList = otService.call_OT_Insert(otReq);
//					for (OneTimeTableRes otRes : otResList) {
//						SlideSectionSaveRes res = new SlideSectionSaveRes();
//						res.setResponse("Saved Successfully");
//						res.setRiskId(otRes.getVehicleId());
//						res.setVdRefNo(otRes.getVdRefNo());
//						res.setCdRefNo(otRes.getCdRefNo());
//						res.setMsrefno(otRes.getMsRefNo());
//						res.setCompanyId(otRes.getCompanyId());
//						res.setProductId(otRes.getProductId());
//						res.setSectionId(otRes.getSectionId());
//						res.setCustomerReferenceNo(saveData.getCustomerReferenceNo());
//						res.setRequestReferenceNo(saveData.getRequestReferenceNo());
//						res.setCreatedBy(req.getCreatedBy());
//						resList.add(res);
//					}
		//
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				return resList;
//			}
		//
//			@Override
//			public List<SlideSectionSaveRes> saveBurglaryAndHouseBreakingDetailsList(
//					List<BurglaryAndHouseBreakingSaveReq> reqList) {
//				// TODO Auto-generated method stub
//				List<SlideSectionSaveRes> resList = new ArrayList<SlideSectionSaveRes>();
//				EserviceBuildingDetails saveData = new EserviceBuildingDetails();
//				EserviceSectionDetails sectionData = new EserviceSectionDetails();
//				DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
//				try {
//					EserviceBuildingDetails data = buildingRepo.findByRequestReferenceNoAndRiskIdAndSectionId(
//							reqList.get(0).getRequestReferenceNo(), 1, reqList.get(0).getSectionId());
		//
//					if (data != null) {
		//
//						dozerBeanMapper.map(data, saveData);
		//
//					}
//					List<EserviceBuildingDetails> saveData1 = buildingRepo.findByRequestReferenceNoAndSectionId(
//							reqList.get(0).getRequestReferenceNo(), reqList.get(0).getSectionId());
//					if (saveData1 != null && saveData1.size() > 0) {
//						buildingRepo.deleteAll(saveData1);
//					}
//					sectionData = eserSecRepo.findByRequestReferenceNoAndRiskIdAndProductIdAndSectionId(
//							reqList.get(0).getRequestReferenceNo(), Integer.valueOf(1), reqList.get(0).getProductId(),
//							reqList.get(0).getSectionId());
//					for (BurglaryAndHouseBreakingSaveReq req : reqList) {
//						saveData.setRiskId(Integer.valueOf(req.getRiskId()));
////						saveData.setBurglarySi(StringUtils.isBlank(req.getBurglarySi()) ? new BigDecimal(0)
////								: new BigDecimal(req.getBurglarySi()));
//						if (StringUtils.isNotBlank(saveData.getExchangeRate().toString())) {
//							BigDecimal exRate = saveData.getExchangeRate();
////							if (StringUtils.isNotBlank(req.getBurglarySi())) {
////								saveData.setBurglarySiLc(new BigDecimal(req.getBurglarySi()).multiply(exRate));
////								;
////							} else {
////								saveData.setBurglarySiLc(BigDecimal.ZERO);
////							}
//						}
//						saveData.setFirstLossPercentId(StringUtils.isBlank(req.getFirstLossPercentId()) ? null
//								: Integer.valueOf(req.getFirstLossPercentId()));
//						if (StringUtils.isNotBlank(req.getFirstLossPercentId())) {
//							String firstLossPercent = getListItem(req.getInsuranceId(), saveData.getBranchCode(),
//									"BURGLARY_FIRST_LOSS", req.getFirstLossPercentId());
//							saveData.setFirstLossPercent(
//									StringUtils.isBlank(firstLossPercent) ? null : Integer.valueOf(firstLossPercent));
//						}
		//
//						// Endorsement CHanges
//						if (!(req.getEndorsementType() == null || req.getEndorsementType() == 0))
		//
//						{
		//
//							saveData.setOriginalPolicyNo(req.getOriginalPolicyNo());
//							saveData.setEndorsementDate(req.getEndorsementDate());
//							saveData.setEndorsementRemarks(req.getEndorsementRemarks());
//							saveData.setEndorsementEffdate(req.getEndorsementEffdate());
//							saveData.setEndtPrevPolicyNo(req.getEndtPrevPolicyNo());
//							saveData.setEndtPrevQuoteNo(req.getEndtPrevQuoteNo());
//							saveData.setEndtCount(req.getEndtCount());
//							saveData.setEndtStatus(req.getEndtStatus());
//							saveData.setIsFinyn(req.getIsFinaceYn());
//							saveData.setEndtCategDesc(req.getEndtCategDesc());
//							saveData.setEndorsementType(req.getEndorsementType());
//							saveData.setEndorsementTypeDesc(req.getEndorsementTypeDesc());
		//
//						}
		//
//						// Status
//						saveData.setStatus(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus());
//						saveData.setRegionCode((StringUtils.isBlank(req.getRegionCode()) || req.getRegionCode() == null) ? null
//								: req.getRegionCode());
//						saveData.setDistrictCode(
//								(StringUtils.isBlank(req.getDistrictCode()) || req.getDistrictCode() == null) ? null
//										: req.getDistrictCode());
		//
//						if (req.getRegionCode() != null) {
//							List<RegionMaster> regiondata = regionrepo.findByRegionCode(req.getRegionCode());
//							saveData.setRegionDesc(
//									!regiondata.isEmpty() && StringUtils.isBlank(regiondata.get(0).getRegionName()) ? null
//											: regiondata.get(0).getRegionName());
//						}
		//
//						if (req.getDistrictCode() != null) {
//							List<StateMaster> statedata = staterepo.findByStateId(Integer.valueOf(req.getDistrictCode()));
//							saveData.setDistrictDesc(
//									!statedata.isEmpty() && StringUtils.isBlank(statedata.get(0).getStateName()) ? null
//											: statedata.get(0).getStateName());
//						}
		//
//						saveData.setLocationName(
//								(req.getLocationName() == null || StringUtils.isBlank(req.getLocationName())) ? ""
//										: req.getLocationName());
//						saveData.setCoveringDetails(
//								(req.getCoveringDetails() == null || StringUtils.isBlank(req.getCoveringDetails())) ? ""
//										: req.getCoveringDetails());
//						saveData.setDescriptionOfRisk(
//								(req.getDescriptionOfRisk() == null || StringUtils.isBlank(req.getDescriptionOfRisk())) ? ""
//										: req.getDescriptionOfRisk());
//						saveData.setCategoryId(
//								(req.getIndustryType() == null || StringUtils.isBlank(req.getIndustryType())) ? ""
//										: req.getIndustryType());
//						saveData.setAddress(
//								(req.getAddress() == null || StringUtils.isBlank(req.getAddress())) ? "" : req.getAddress());
		//
//						buildingRepo.save(saveData);
		//
////				} catch (Exception e) {
////					e.printStackTrace();
////					log.info("Log Details" + e.getMessage());
////				}
////				try {
//						List<OneTimeTableRes> otResList = null;
//						// Section Req
//						List<BuildingSectionRes> sectionList = new ArrayList<BuildingSectionRes>();
//						BuildingSectionRes sec = new BuildingSectionRes();
//						sec.setMotorYn(sectionData.getProductType());
//						sec.setSectionId(sectionData.getSectionId());
//						sec.setSectionName(sectionData.getSectionName());
//						sectionList.add(sec);
		//
//						// One Time Table Thread Call
//						OneTimeTableReq otReq = new OneTimeTableReq();
//						otReq.setRequestReferenceNo(saveData.getRequestReferenceNo());
//						otReq.setVehicleId(saveData.getRiskId());
//						otReq.setBranchCode(saveData.getBranchCode());
//						otReq.setInsuranceId(saveData.getCompanyId());
//						otReq.setProductId(Integer.valueOf(saveData.getProductId()));
//						otReq.setSectionList(sectionList);
		//
//						otResList = otService.call_OT_Insert(otReq);
//						for (OneTimeTableRes otRes : otResList) {
//							SlideSectionSaveRes res = new SlideSectionSaveRes();
//							res.setResponse("Saved Successfully");
//							res.setRiskId(otRes.getVehicleId());
//							res.setVdRefNo(otRes.getVdRefNo());
//							res.setCdRefNo(otRes.getCdRefNo());
//							res.setMsrefno(otRes.getMsRefNo());
//							res.setCompanyId(otRes.getCompanyId());
//							res.setProductId(otRes.getProductId());
//							res.setSectionId(otRes.getSectionId());
//							res.setCustomerReferenceNo(saveData.getCustomerReferenceNo());
//							res.setRequestReferenceNo(saveData.getRequestReferenceNo());
//							res.setCreatedBy(req.getCreatedBy());
//							resList.add(res);
//						}
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				return resList;
//			}
		//
//			@Override
//			public List<SlideSectionSaveRes> saveFireAndAlliedPerillsDetails(FireAndAlliedPerillsSaveReq req) {
//				// TODO Auto-generated method stub
//				List<SlideSectionSaveRes> resList = new ArrayList<SlideSectionSaveRes>();
//				EserviceBuildingDetails saveData = new EserviceBuildingDetails();
//				EserviceSectionDetails sectionData = new EserviceSectionDetails();
		//
//				try {
//					saveData = buildingRepo.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo(),
//							Integer.valueOf(req.getRiskId()), req.getSectionId());
//					sectionData = eserSecRepo.findByRequestReferenceNoAndRiskIdAndProductIdAndSectionId(
//							req.getRequestReferenceNo(), Integer.valueOf(req.getRiskId()), req.getProductId(),
//							req.getSectionId());
		//
//					saveData.setFirePlantSi(StringUtils.isBlank(req.getFirePlantSi()) ? new BigDecimal(0)
//							: new BigDecimal(req.getFirePlantSi()));
//					saveData.setStockInTradeSi(StringUtils.isBlank(req.getStockInTradeSi()) ? new BigDecimal(0)
//							: new BigDecimal(req.getStockInTradeSi()));
////					saveData.setBuildingSuminsured(StringUtils.isBlank(req.getBuildingSuminsured()) ? new BigDecimal(0)
////							: new BigDecimal(req.getBuildingSuminsured()));
////					saveData.setEquipmentSi(StringUtils.isBlank(req.getFireEquipSi()) ? new BigDecimal(0)
////							: new BigDecimal(req.getFireEquipSi()));
//					saveData.setOnStockSi(
//							StringUtils.isBlank(req.getOnStockSi()) ? new BigDecimal(0) : new BigDecimal(req.getOnStockSi()));
//					saveData.setOnAssetsSi(
//							StringUtils.isBlank(req.getOnAssetsSi()) ? new BigDecimal(0) : new BigDecimal(req.getOnAssetsSi()));
		//
//					saveData.setMakutiYn(req.getMakutiYn());
		//
//					if (StringUtils.isNotBlank(saveData.getExchangeRate().toString())) {
//						BigDecimal exRate = saveData.getExchangeRate();
////						if (StringUtils.isNotBlank(req.getBuildingSuminsured())) {
////							saveData.setBuildingSuminsuredLc(new BigDecimal(req.getBuildingSuminsured()).multiply(exRate));
////						} else {
////							saveData.setBuildingSuminsuredLc(BigDecimal.ZERO);
////						}
		//
//						if (StringUtils.isNotBlank(req.getOnStockSi())) {
//							saveData.setOnStockSiLc(new BigDecimal(req.getOnStockSi()).multiply(exRate));
//						} else {
//							saveData.setOnStockSiLc(BigDecimal.ZERO);
//						}
		//
//						if (StringUtils.isNotBlank(req.getOnAssetsSi())) {
//							saveData.setOnAssetsSiLc(new BigDecimal(req.getOnAssetsSi()).multiply(exRate));
//						} else {
//							saveData.setOnAssetsSiLc(BigDecimal.ZERO);
//						}
		//
////						if (StringUtils.isNotBlank(req.getBuildingSuminsured())) {
////							saveData.setBuildingSuminsuredLc(new BigDecimal(req.getBuildingSuminsured()).multiply(exRate));
////						} else {
////							saveData.setBuildingSuminsuredLc(BigDecimal.ZERO);
////						}
		//
////						if (StringUtils.isNotBlank(req.getFirePlantSi())) {
////							saveData.setFirePlantSiLc(new BigDecimal(req.getFirePlantSi()).multiply(exRate));
////						} else {
////							saveData.setFirePlantSiLc(BigDecimal.ZERO);
////						}
//						if (StringUtils.isNotBlank(req.getStockInTradeSi())) {
//							saveData.setStockInTradeSiLc(new BigDecimal(req.getStockInTradeSi()).multiply(exRate));
//						} else {
//							saveData.setStockInTradeSiLc(BigDecimal.ZERO);
//						}
////						if (StringUtils.isNotBlank(req.getBuildingSuminsured())) {
////							saveData.setBuildingSuminsuredLc(new BigDecimal(req.getBuildingSuminsured()).multiply(exRate));
////						} else {
////							saveData.setBuildingSuminsuredLc(BigDecimal.ZERO);
////						}
////						if (StringUtils.isNotBlank(req.getFireEquipSi())) {
////							saveData.setEquipmentSiLc(new BigDecimal(req.getFireEquipSi()).multiply(exRate));
////						} else {
////							saveData.setEquipmentSiLc(BigDecimal.ZERO);
////						}
//					}
//					if (StringUtils.isNotBlank(req.getIndemityPeriod())) {
//						String indemityPeriodDesc = getListItem(req.getInsuranceId(), saveData.getBranchCode(),
//								"INDEMITY_PERIOD", req.getIndemityPeriod());
//						saveData.setIndemityPeriod(req.getIndemityPeriod());
//						saveData.setIndemityPeriodDesc(indemityPeriodDesc);
//					}
		//
//					// Endorsement CHanges
//					if (!(req.getEndorsementType() == null || req.getEndorsementType() == 0))
		//
//					{
		//
//						saveData.setOriginalPolicyNo(req.getOriginalPolicyNo());
//						saveData.setEndorsementDate(req.getEndorsementDate());
//						saveData.setEndorsementRemarks(req.getEndorsementRemarks());
//						saveData.setEndorsementEffdate(req.getEndorsementEffdate());
//						saveData.setEndtPrevPolicyNo(req.getEndtPrevPolicyNo());
//						saveData.setEndtPrevQuoteNo(req.getEndtPrevQuoteNo());
//						saveData.setEndtCount(req.getEndtCount());
//						saveData.setEndtStatus(req.getEndtStatus());
//						saveData.setIsFinyn(req.getIsFinaceYn());
//						saveData.setEndtCategDesc(req.getEndtCategDesc());
//						saveData.setEndorsementType(req.getEndorsementType());
//						saveData.setEndorsementTypeDesc(req.getEndorsementTypeDesc());
		//
//					}
//					// Status
//					saveData.setStatus(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus());
//					buildingRepo.save(saveData);
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Log Details" + e.getMessage());
		//
//				}
//				try {
//					List<OneTimeTableRes> otResList = null;
//					// Section Req
//					List<BuildingSectionRes> sectionList = new ArrayList<BuildingSectionRes>();
//					BuildingSectionRes sec = new BuildingSectionRes();
//					sec.setMotorYn(sectionData.getProductType());
//					sec.setSectionId(sectionData.getSectionId());
//					sec.setSectionName(sectionData.getSectionName());
//					sectionList.add(sec);
		//
//					// One Time Table Thread Call
//					OneTimeTableReq otReq = new OneTimeTableReq();
//					otReq.setRequestReferenceNo(saveData.getRequestReferenceNo());
//					otReq.setVehicleId(saveData.getRiskId());
//					otReq.setBranchCode(saveData.getBranchCode());
//					otReq.setInsuranceId(saveData.getCompanyId());
//					otReq.setProductId(Integer.valueOf(saveData.getProductId()));
//					otReq.setSectionList(sectionList);
		//
//					otResList = otService.call_OT_Insert(otReq);
//					for (OneTimeTableRes otRes : otResList) {
//						SlideSectionSaveRes res = new SlideSectionSaveRes();
//						res.setResponse("Saved Successfully");
//						res.setRiskId(otRes.getVehicleId());
//						res.setVdRefNo(otRes.getVdRefNo());
//						res.setCdRefNo(otRes.getCdRefNo());
//						res.setMsrefno(otRes.getMsRefNo());
//						res.setCompanyId(otRes.getCompanyId());
//						res.setProductId(otRes.getProductId());
//						res.setSectionId(otRes.getSectionId());
//						res.setCustomerReferenceNo(saveData.getCustomerReferenceNo());
//						res.setRequestReferenceNo(saveData.getRequestReferenceNo());
//						res.setCreatedBy(req.getCreatedBy());
//						resList.add(res);
//					}
		//
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				return resList;
//			}
		//
//			@Override
//			public List<SlideSectionSaveRes> saveSlideFidelityGuarantyDetails(List<SlideFidelityGuarantySaveReq> reqList) {
//				// TODO Auto-generated method stub
//				List<SlideSectionSaveRes> resList = new ArrayList<SlideSectionSaveRes>();
//				EserviceBuildingDetails saveData = new EserviceBuildingDetails();
//				List<EserviceCommonDetails> humanDatas = new ArrayList<EserviceCommonDetails>();
//				EserviceCommonDetails humanData = new EserviceCommonDetails();
//				EserviceSectionDetails sectionData = new EserviceSectionDetails();
//				DozerBeanMapper dozerMapper = new DozerBeanMapper();
//				List<BuildingSectionRes> sectionList = new ArrayList<BuildingSectionRes>();
		//
//				try {
//					BigDecimal endtCount = BigDecimal.ZERO;
//					String refNo = reqList.get(0).getRequestReferenceNo();
//					String productId = reqList.get(0).getProductId();
//					String sectionId = reqList.get(0).getSectionId();
		//
//					saveData = buildingRepo.findByRequestReferenceNoAndRiskIdAndSectionId(refNo, 1, "0");
//					if (saveData != null) {
		//
//						dozerMapper.map(saveData, humanData);
//						humanData.setPolicyPeriod(saveData.getPolicyPeriord());
		//
//					}
//					humanDatas = humanRepo.findByRequestReferenceNoAndSectionId(refNo, sectionId);
//					SlideFidelityGuarantySaveReq firstRow = reqList.get(0);
		//
//					if ((firstRow.getEndorsementType() == null || firstRow.getEndorsementType() == 0)
//							&& humanDatas.size() > 0) {
//						humanRepo.deleteAll(humanDatas);
//						dozerMapper.map(humanDatas.get(0), humanData);
//					} else if (humanDatas.size() > 0) {
//						List<EserviceCommonDetails> humanData1 = new ArrayList<EserviceCommonDetails>();
//						if (!(firstRow.getEndorsementType() == null || firstRow.getEndorsementType() == 0)) {
		//
//							// search
//							if (reqList.get(0).getEndtCount().compareTo(BigDecimal.ONE) == 0) {
//								endtCount = reqList.get(0).getEndtCount();
//								humanData1 = humanRepo.findBySectionIdAndPolicyNoAndStatusNot(sectionId,
//										reqList.get(0).getOriginalPolicyNo(), "D");
//							} else {
//								endtCount = reqList.get(0).getEndtCount().subtract(BigDecimal.ONE);
//								humanData1 = humanRepo.findByEndtCountAndSectionIdAndOriginalPolicyNoAndStatusNot(endtCount,
//										sectionId, reqList.get(0).getOriginalPolicyNo(), "D");
//							}
		//
//							// delete all
//							humanRepo.deleteAll(humanDatas);
		//
//						}
//						List<EserviceCommonDetails> humanDatas2 = new ArrayList<EserviceCommonDetails>();
//						dozerMapper.map(humanDatas.get(0), humanData);
//						String policyNo = humanDatas.get(0).getPolicyNo();
//						humanData1.forEach(hum -> {
//							EserviceCommonDetails o = new EserviceCommonDetails();
//							dozerMapper.map(hum, o);
//							o.setStatus("D");
//							o.setRequestReferenceNo(refNo);
//							o.setQuoteNo("");
//							o.setPolicyNo(policyNo);
//							if (!(firstRow.getEndorsementType() == null || firstRow.getEndorsementType() == 0))
		//
//							{
		//
//								o.setOriginalPolicyNo(firstRow.getOriginalPolicyNo());
//								o.setEndorsementDate(firstRow.getEndorsementDate());
//								o.setEndorsementRemarks(firstRow.getEndorsementRemarks());
//								o.setEndorsementEffdate(firstRow.getEndorsementEffdate());
//								o.setEndtPrevPolicyNo(firstRow.getEndtPrevPolicyNo());
//								o.setEndtPrevQuoteNo(firstRow.getEndtPrevQuoteNo());
//								o.setEndtCount(firstRow.getEndtCount());
//								o.setEndtStatus(firstRow.getEndtStatus());
//								o.setIsFinyn(firstRow.getIsFinaceYn());
//								o.setEndtCategDesc(firstRow.getEndtCategDesc());
//								o.setEndorsementType(firstRow.getEndorsementType());
//								o.setEndorsementTypeDesc(firstRow.getEndorsementTypeDesc());
		//
//							}
		//
//							humanDatas2.add(o);
//						});
//						humanRepo.saveAllAndFlush(humanDatas2);
		//
//					}
		//
//					sectionData = eserSecRepo.findByRequestReferenceNoAndRiskIdAndProductIdAndSectionId(refNo, 1, productId,
//							sectionId);
//					List<EserviceCommonDetails> saveHumanList = new ArrayList<EserviceCommonDetails>();
//					for (SlideFidelityGuarantySaveReq data : reqList) {
//						// Save Human
//						EserviceCommonDetails saveHuman = new EserviceCommonDetails();
//						saveHuman = dozerMapper.map(humanData, EserviceCommonDetails.class);
//						saveHuman.setRequestReferenceNo(data.getRequestReferenceNo());
//						saveHuman.setRiskId(data.getOriginalRiskId() == null || StringUtils.isBlank(data.getOriginalRiskId())
//								? Integer.valueOf(data.getRiskId())
//								: Integer.valueOf(data.getOriginalRiskId()));
//						saveHuman.setOriginalRiskId(
//								data.getOriginalRiskId() == null || StringUtils.isBlank(data.getOriginalRiskId())
//										? Integer.valueOf(data.getRiskId())
//										: Integer.valueOf(data.getOriginalRiskId()));
//						saveHuman.setSectionId(data.getSectionId());
//						saveHuman.setSectionName(sectionData.getSectionName());
//						;
//						saveHuman.setCreatedBy(data.getCreatedBy());
//					//	saveHuman.setFidEmpSi(StringUtils.isBlank(data.getFidEmpSi()) ? new BigDecimal(0)
//					//			: new BigDecimal(data.getFidEmpSi()));
//						saveHuman.setSumInsured(data.getSumInsured() == null ? new BigDecimal(0)
//								: data.getSumInsured()); 
//						// Personal Liability
//						if (StringUtils.isNotBlank(data.getLiabilityOccupationId())) {
//							OccupationMaster occupationData = getOccupationMasterDropdown(data.getInsuranceId(), "99999",
//									data.getProductId(), data.getLiabilityOccupationId());
//							saveHuman.setOccupationType(occupationData.getOccupationId().toString());
//							saveHuman.setOccupationDesc(occupationData.getOccupationName());
//							saveHuman.setCategoryId(occupationData.getCategoryId());
//							// saveHuman.setCategoryDesc("Class " + occupationData.getCategoryId());
//						}
//						saveHuman.setOtherOccupation(data.getOtherOccupation());
//						saveHuman.setFidEmpCount(
//								StringUtils.isBlank(data.getFidEmpCount()) ? null : new BigDecimal(data.getFidEmpCount()));
//						saveHuman.setTotalNoOfEmployees(
//								StringUtils.isBlank(data.getFidEmpCount()) ? null : Long.valueOf(data.getFidEmpCount()));
//						saveHuman.setCount(
//								StringUtils.isBlank(data.getFidEmpCount()) ? null : Integer.valueOf(data.getFidEmpCount()));
//						// Lc Calculation
//						BigDecimal exchangeRate = humanData.getExchangeRate() != null ? humanData.getExchangeRate()
//								: BigDecimal.ZERO;
//						saveHuman.setSumInsuredLc(
//								saveHuman.getSumInsured() == null ? null : saveHuman.getSumInsured().multiply(exchangeRate));
////						saveHuman.setLiabilitySiLc(
////								saveHuman.getLiabilitySi() == null ? null : saveHuman.getLiabilitySi().multiply(exchangeRate));
//				//		saveHuman.setFidEmpSiLc(
//				//				saveHuman.getFidEmpSi() == null ? null : saveHuman.getFidEmpSi().multiply(exchangeRate));
////						saveHuman.setEmpLiabilitySiLc(saveHuman.getEmpLiabilitySi() == null ? null
////								: saveHuman.getEmpLiabilitySi().multiply(exchangeRate));
////						saveHuman.setPersonalLiabilitySiLc(saveHuman.getPersonalLiabilitySi() == null ? null
////								: saveHuman.getPersonalLiabilitySi().multiply(exchangeRate));
		//
//						// Endorsement CHanges
//						if (!(data.getEndorsementType() == null || data.getEndorsementType() == 0))
		//
//						{
		//
//							saveHuman.setOriginalPolicyNo(data.getOriginalPolicyNo());
//							saveHuman.setEndorsementDate(data.getEndorsementDate());
//							saveHuman.setEndorsementRemarks(data.getEndorsementRemarks());
//							saveHuman.setEndorsementEffdate(data.getEndorsementEffdate());
//							saveHuman.setEndtPrevPolicyNo(data.getEndtPrevPolicyNo());
//							saveHuman.setEndtPrevQuoteNo(data.getEndtPrevQuoteNo());
//							saveHuman.setEndtCount(data.getEndtCount());
//							saveHuman.setEndtStatus(data.getEndtStatus());
//							saveHuman.setIsFinyn(data.getIsFinaceYn());
//							saveHuman.setEndtCategDesc(data.getEndtCategDesc());
//							saveHuman.setEndorsementType(data.getEndorsementType());
//							saveHuman.setEndorsementTypeDesc(data.getEndorsementTypeDesc());
//							saveHuman.setStatus("E");
		//
//						}
//						saveHumanList.add(saveHuman);
		//
//					}
//					humanRepo.saveAllAndFlush(saveHumanList);
//					if ("32".equalsIgnoreCase(productId)) {
//						updatesectionDetails(refNo);
//					}
		//
//					List<EserviceCommonDetails> esercommonDatas = eserCommonRepo
//							.findByRequestReferenceNoAndSectionId(humanData.getRequestReferenceNo(), humanData.getSectionId());
//					if (esercommonDatas.size() == 0) {
//						esercommonDatas = saveHumanList;
//					}
//					for (EserviceCommonDetails data : esercommonDatas) {
//						// Section Req
//						BuildingSectionRes sec = new BuildingSectionRes();
//						sec.setMotorYn(sectionData.getProductType());
//						sec.setSectionId(sectionData.getSectionId());
//						sec.setSectionName(sectionData.getSectionName());
//						sec.setOccupationId(data.getOccupationType());
//						sectionList.add(sec);
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Log Details" + e.getMessage());
		//
//				}
//				try {
//					List<OneTimeTableRes> otResList = null;
//					// One Time Table Thread Call
//					OneTimeTableReq otReq = new OneTimeTableReq();
//					SlideFidelityGuarantySaveReq req = reqList.get(0);
//					otReq.setRequestReferenceNo(req.getRequestReferenceNo());
//					otReq.setVehicleId(1);
//					otReq.setBranchCode(saveData != null ? saveData.getBranchCode() : humanData.getBranchCode());
//					otReq.setInsuranceId(saveData != null ? saveData.getCompanyId() : humanData.getCompanyId());
//					otReq.setProductId(Integer.valueOf(req.getProductId()));
//					otReq.setSectionList(sectionList);
		//
//					otResList = otService.call_OT_Insert(otReq);
//					for (OneTimeTableRes otRes : otResList) {
//						SlideSectionSaveRes res = new SlideSectionSaveRes();
//						res.setResponse("Saved Successfully");
//						res.setRiskId(otRes.getVehicleId());
//						res.setVdRefNo(otRes.getVdRefNo());
//						res.setCdRefNo(otRes.getCdRefNo());
//						res.setMsrefno(otRes.getMsRefNo());
//						res.setCompanyId(otRes.getCompanyId());
//						res.setProductId(otRes.getProductId());
//						res.setSectionId(otRes.getSectionId());
//						res.setCustomerReferenceNo(
//								saveData != null ? saveData.getCustomerReferenceNo() : humanData.getCustomerReferenceNo());
//						res.setRequestReferenceNo(
//								saveData != null ? saveData.getRequestReferenceNo() : humanData.getRequestReferenceNo());
//						res.setCreatedBy(reqList.get(0).getCreatedBy());
//						resList.add(res);
//					}
		//
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				return resList;
//			}
		//
//			@Override
//			public List<SlideSectionSaveRes> saveSlideMachineryBreakdownDetails(SlideMachineryBreakdownSaveReq req) {
//				// TODO Auto-generated method stub
//				List<SlideSectionSaveRes> resList = new ArrayList<SlideSectionSaveRes>();
//				EserviceBuildingDetails saveData = new EserviceBuildingDetails();
//				EserviceSectionDetails sectionData = new EserviceSectionDetails();
		//
//				try {
//					saveData = buildingRepo.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo(),
//							Integer.valueOf(req.getRiskId()), req.getSectionId());
//					sectionData = eserSecRepo.findByRequestReferenceNoAndRiskIdAndProductIdAndSectionId(
//							req.getRequestReferenceNo(), Integer.valueOf(req.getRiskId()), req.getProductId(),
//							req.getSectionId());
		//
////					saveData.setBoilerPlantsSi(StringUtils.isBlank(req.getBoilerPlantsSi()) ? new BigDecimal(0)
////							: new BigDecimal(req.getBoilerPlantsSi()));
////					saveData.setElecMachinesSi(StringUtils.isBlank(req.getElecMachinesSi()) ? new BigDecimal(0)
////							: new BigDecimal(req.getElecMachinesSi()));
////					saveData.setEquipmentSi(StringUtils.isBlank(req.getEquipmentSi()) ? new BigDecimal(0)
////							: new BigDecimal(req.getEquipmentSi()));
////					saveData.setGeneralMachineSi(StringUtils.isBlank(req.getGeneralMachineSi()) ? new BigDecimal(0)
////							: new BigDecimal(req.getGeneralMachineSi()));
////					saveData.setMachineEquipSi(StringUtils.isBlank(req.getMachineEquipSi()) ? new BigDecimal(0)
////							: new BigDecimal(req.getMachineEquipSi()));
////					saveData.setManuUnitsSi(StringUtils.isBlank(req.getManuUnitsSi()) ? new BigDecimal(0)
////							: new BigDecimal(req.getManuUnitsSi()));
////					saveData.setPowerPlantSi(StringUtils.isBlank(req.getPowerPlantSi()) ? new BigDecimal(0)
////							: new BigDecimal(req.getPowerPlantSi()));
////					saveData.setMachinerySi(StringUtils.isBlank(req.getMachinerySi()) ? new BigDecimal(0)
////							: new BigDecimal(req.getMachinerySi()));
		//
//					// Lc Insert
//					if (StringUtils.isNotBlank(saveData.getExchangeRate().toString())) {
//						BigDecimal exRate = saveData.getExchangeRate();
////						if (StringUtils.isNotBlank(req.getBoilerPlantsSi())) {
////							saveData.setBoilerPlantsSiLc(new BigDecimal(req.getBoilerPlantsSi()).multiply(exRate));
////						} else {
////							saveData.setBoilerPlantsSiLc(BigDecimal.ZERO);
////						}
////						if (StringUtils.isNotBlank(req.getElecMachinesSi())) {
////							saveData.setElecMachinesSiLc(new BigDecimal(req.getElecMachinesSi()).multiply(exRate));
////						} else {
////							saveData.setElecMachinesSiLc(BigDecimal.ZERO);
////						}
////						if (StringUtils.isNotBlank(req.getEquipmentSi())) {
////							saveData.setEquipmentSiLc(new BigDecimal(req.getEquipmentSi()).multiply(exRate));
////						} else {
////							saveData.setEquipmentSiLc(BigDecimal.ZERO);
////						}
////						if (StringUtils.isNotBlank(req.getGeneralMachineSi())) {
////							saveData.setGeneralMachineSiLc(new BigDecimal(req.getGeneralMachineSi()).multiply(exRate));
////						} else {
////							saveData.setGeneralMachineSiLc(BigDecimal.ZERO);
////						}
////						if (StringUtils.isNotBlank(req.getMachineEquipSi())) {
////							saveData.setMachineEquipSiLc(new BigDecimal(req.getMachineEquipSi()).multiply(exRate));
////						} else {
////							saveData.setMachineEquipSiLc(BigDecimal.ZERO);
////						}
////						if (StringUtils.isNotBlank(req.getManuUnitsSi())) {
////							saveData.setManuUnitsSiLc(new BigDecimal(req.getManuUnitsSi()).multiply(exRate));
////						} else {
////							saveData.setManuUnitsSiLc(BigDecimal.ZERO);
////						}
////						if (StringUtils.isNotBlank(req.getPowerPlantSi())) {
////							saveData.setPowerPlantSiLc(new BigDecimal(req.getPowerPlantSi()).multiply(exRate));
////						} else {
////							saveData.setPowerPlantSiLc(BigDecimal.ZERO);
////						}
		//
////						if (StringUtils.isNotBlank(req.getMachinerySi())) {
////							saveData.setMachinerySiLc(new BigDecimal(req.getMachinerySi()).multiply(exRate));
////						} else {
////							saveData.setMachinerySiLc(BigDecimal.ZERO);
////						}
//					}
		//
//					// Endorsement Changes
//					if (!(req.getEndorsementType() == null || req.getEndorsementType() == 0))
		//
//					{
		//
//						saveData.setOriginalPolicyNo(req.getOriginalPolicyNo());
//						saveData.setEndorsementDate(req.getEndorsementDate());
//						saveData.setEndorsementRemarks(req.getEndorsementRemarks());
//						saveData.setEndorsementEffdate(req.getEndorsementEffdate());
//						saveData.setEndtPrevPolicyNo(req.getEndtPrevPolicyNo());
//						saveData.setEndtPrevQuoteNo(req.getEndtPrevQuoteNo());
//						saveData.setEndtCount(req.getEndtCount());
//						saveData.setEndtStatus(req.getEndtStatus());
//						saveData.setIsFinyn(req.getIsFinaceYn());
//						saveData.setEndtCategDesc(req.getEndtCategDesc());
//						saveData.setEndorsementType(req.getEndorsementType());
//						saveData.setEndorsementTypeDesc(req.getEndorsementTypeDesc());
		//
//					}
//					// Status
//					saveData.setStatus(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus());
//					buildingRepo.save(saveData);
		//
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Log Details" + e.getMessage());
		//
//				}
//				try {
//					List<OneTimeTableRes> otResList = null;
//					// Section Req
//					List<BuildingSectionRes> sectionList = new ArrayList<BuildingSectionRes>();
//					BuildingSectionRes sec = new BuildingSectionRes();
//					sec.setMotorYn(sectionData.getProductType());
//					sec.setSectionId(sectionData.getSectionId());
//					sec.setSectionName(sectionData.getSectionName());
//					sectionList.add(sec);
		//
//					// One Time Table Thread Call
//					OneTimeTableReq otReq = new OneTimeTableReq();
//					otReq.setRequestReferenceNo(saveData.getRequestReferenceNo());
//					otReq.setVehicleId(saveData.getRiskId());
//					otReq.setBranchCode(saveData.getBranchCode());
//					otReq.setInsuranceId(saveData.getCompanyId());
//					otReq.setProductId(Integer.valueOf(saveData.getProductId()));
//					otReq.setSectionList(sectionList);
		//
//					otResList = otService.call_OT_Insert(otReq);
//					for (OneTimeTableRes otRes : otResList) {
//						SlideSectionSaveRes res = new SlideSectionSaveRes();
//						res.setResponse("Saved Successfully");
//						res.setRiskId(otRes.getVehicleId());
//						res.setVdRefNo(otRes.getVdRefNo());
//						res.setCdRefNo(otRes.getCdRefNo());
//						res.setMsrefno(otRes.getMsRefNo());
//						res.setCompanyId(otRes.getCompanyId());
//						res.setProductId(otRes.getProductId());
//						res.setSectionId(otRes.getSectionId());
//						res.setCustomerReferenceNo(saveData.getCustomerReferenceNo());
//						res.setRequestReferenceNo(saveData.getRequestReferenceNo());
//						res.setCreatedBy(req.getCreatedBy());
//						resList.add(res);
//					}
		//
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				return resList;
//			}
		//
//			@Override
//			public List<SlideSectionSaveRes> saveSlidePlateGlassDetails(SlidePlateGlassSaveReq req) {
//				// TODO Auto-generated method stub
//				List<SlideSectionSaveRes> resList = new ArrayList<SlideSectionSaveRes>();
//				EserviceBuildingDetails saveData = new EserviceBuildingDetails();
//				EserviceSectionDetails sectionData = new EserviceSectionDetails();
//				try {
//					saveData = buildingRepo.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo(),
//							Integer.valueOf(req.getRiskId()), req.getSectionId());
//					sectionData = eserSecRepo.findByRequestReferenceNoAndRiskIdAndProductIdAndSectionId(
//							req.getRequestReferenceNo(), Integer.valueOf(req.getRiskId()), req.getProductId(),
//							req.getSectionId());
		//
////					saveData.setPlateGlassSi(StringUtils.isBlank(req.getPlateGlassSi()) ? new BigDecimal(0)
////							: new BigDecimal(req.getPlateGlassSi()));
//					if (StringUtils.isNotBlank(saveData.getExchangeRate().toString())) {
//						BigDecimal exRate = saveData.getExchangeRate();
////						if (StringUtils.isNotBlank(req.getPlateGlassSi())) {
////							saveData.setPlateGlassSiLc(new BigDecimal(req.getPlateGlassSi()).multiply(exRate));
////						} else {
////							saveData.setPlateGlassSiLc(BigDecimal.ZERO);
////						}
//					}
		//
//					if (StringUtils.isNotBlank(req.getPlateGlassType())) {
//						String plateGlassTypeDesc = getListItem(saveData.getCompanyId(), saveData.getBranchCode(),
//								"PLATE_GLASS_TYPE", req.getPlateGlassType());
//						saveData.setPlateGlassType(req.getPlateGlassType());
//						saveData.setPlateGlassDesc(plateGlassTypeDesc);
//					}
//					// Status
//					saveData.setStatus(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus());
//					buildingRepo.save(saveData);
		//
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Log Details" + e.getMessage());
//				}
//				try {
//					List<OneTimeTableRes> otResList = null;
//					// Section Req
//					List<BuildingSectionRes> sectionList = new ArrayList<BuildingSectionRes>();
//					BuildingSectionRes sec = new BuildingSectionRes();
//					sec.setMotorYn(sectionData.getProductType());
//					sec.setSectionId(sectionData.getSectionId());
//					sec.setSectionName(sectionData.getSectionName());
//					sectionList.add(sec);
		//
//					// One Time Table Thread Call
//					OneTimeTableReq otReq = new OneTimeTableReq();
//					otReq.setRequestReferenceNo(saveData.getRequestReferenceNo());
//					otReq.setVehicleId(saveData.getRiskId());
//					otReq.setBranchCode(saveData.getBranchCode());
//					otReq.setInsuranceId(saveData.getCompanyId());
//					otReq.setProductId(Integer.valueOf(saveData.getProductId()));
//					otReq.setSectionList(sectionList);
		//
//					otResList = otService.call_OT_Insert(otReq);
//					for (OneTimeTableRes otRes : otResList) {
//						SlideSectionSaveRes res = new SlideSectionSaveRes();
//						res.setResponse("Saved Successfully");
//						res.setRiskId(otRes.getVehicleId());
//						res.setVdRefNo(otRes.getVdRefNo());
//						res.setCdRefNo(otRes.getCdRefNo());
//						res.setMsrefno(otRes.getMsRefNo());
//						res.setCompanyId(otRes.getCompanyId());
//						res.setProductId(otRes.getProductId());
//						res.setSectionId(otRes.getSectionId());
//						res.setCustomerReferenceNo(saveData.getCustomerReferenceNo());
//						res.setRequestReferenceNo(saveData.getRequestReferenceNo());
//						res.setCreatedBy(req.getCreatedBy());
//						resList.add(res);
//					}
		//
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				return resList;
//			}
		//
//			@Override
//			public List<SlideSectionSaveRes> saveSlidePublicLiablityDetails(SlidePublicLiabilitySaveReq req) {
//				// TODO Auto-generated method stub
//				List<SlideSectionSaveRes> resList = new ArrayList<SlideSectionSaveRes>();
//				EserviceBuildingDetails saveData = new EserviceBuildingDetails();
//				List<EserviceCommonDetails> humanDatas = new ArrayList<EserviceCommonDetails>();
//				EserviceCommonDetails humanData = new EserviceCommonDetails();
//				EserviceSectionDetails sectionData = new EserviceSectionDetails();
//				DozerBeanMapper dozerMapper = new DozerBeanMapper();
//				List<BuildingSectionRes> sectionList = new ArrayList<BuildingSectionRes>();
		//
//				try {
//					String refNo = req.getRequestReferenceNo();
//					String productId = req.getProductId();
//					String sectionId = req.getSectionId();
		//
//					saveData = buildingRepo.findByRequestReferenceNoAndRiskIdAndSectionId(refNo, 1, "0");
//					if (saveData != null) {
		//
//						dozerMapper.map(saveData, humanData);
//						humanData.setPolicyPeriod(saveData.getPolicyPeriord());
		//
//					} else {
//						humanDatas = humanRepo.findByRequestReferenceNoAndSectionId(refNo, sectionId);
//						if (humanDatas.size() > 0) {
//							humanRepo.deleteAll(humanDatas);
//							dozerMapper.map(humanDatas.get(0), humanData);
//						}
		//
//					}
		//
//					sectionData = eserSecRepo.findByRequestReferenceNoAndRiskIdAndProductIdAndSectionId(refNo, 1, productId,
//							sectionId);
		//
//					// Save Human
//					EserviceCommonDetails saveHuman = new EserviceCommonDetails();
//					saveHuman = dozerMapper.map(humanData, EserviceCommonDetails.class);
//					saveHuman.setRequestReferenceNo(req.getRequestReferenceNo());
//					saveHuman.setRiskId(Integer.valueOf(1));
//					saveHuman.setSectionId(req.getSectionId());
//					saveHuman.setSectionName(sectionData.getSectionName());
//					;
//					saveHuman.setCreatedBy(req.getCreatedBy());
		//
//					if ("43".equalsIgnoreCase(req.getProductId())) {
//						String pattern = "######";
//						DecimalFormat df = new DecimalFormat(pattern);
		//
//						// saveHuman.setSumInsured(StringUtils.isBlank(req.getAooSumInsured()) ? new
//						// BigDecimal(0) : new
//						// BigDecimal(df.format(Double.valueOf(req.getAooSumInsured()))) );
//						// saveHuman.setLiabilitySi(StringUtils.isBlank(req.getAggSumInsured()) ? new
//						// BigDecimal(0) : new
//						// BigDecimal(df.format(Double.valueOf(req.getAggSumInsured()))) ) ;
////						saveHuman.setAooSuminsured(StringUtils.isBlank(req.getAooSumInsured()) ? new BigDecimal(0)
////								: new BigDecimal(df.format(Double.valueOf(req.getAooSumInsured()))));
////						saveHuman.setAggSuminsured(StringUtils.isBlank(req.getAggSumInsured()) ? new BigDecimal(0)
////								: new BigDecimal(df.format(Double.valueOf(req.getAggSumInsured()))));
//						saveHuman.setSumInsured(req.getSumInsured()==null ? new BigDecimal(0): req.getSumInsured());				// Lc Calculation
//						BigDecimal exchangeRate = humanData.getExchangeRate() != null ? humanData.getExchangeRate()
//								: BigDecimal.ZERO;
////						saveHuman.setAooSuminsuredLc(saveHuman.getAooSuminsured() == null ? null
////								: saveHuman.getAooSuminsured().multiply(exchangeRate));
////						saveHuman.setAggSuminsuredLc(saveHuman.getAggSuminsured() == null ? null
////								: saveHuman.getAggSuminsured().multiply(exchangeRate));
//						saveHuman.setCategoryId(req.getCategory());
		//
//					} else if ("100002".equalsIgnoreCase(req.getInsuranceId())) {
////						saveHuman.setAnyAccidentSi(StringUtils.isBlank(req.getAnyAccidentSi()) ? new BigDecimal(0)
////								: new BigDecimal(req.getAnyAccidentSi()));
////						saveHuman.setInsurancePeriodSi(StringUtils.isBlank(req.getInsurancePeriodSi()) ? new BigDecimal(0)
////								: new BigDecimal(req.getInsurancePeriodSi()));
		//
//						// Lc Calculation
//						BigDecimal exchangeRate = humanData.getExchangeRate() != null ? humanData.getExchangeRate()
//								: BigDecimal.ZERO;
////						saveHuman.setInsurancePeriodSiLc(saveHuman.getInsurancePeriodSi() == null ? null
////								: saveHuman.getInsurancePeriodSi().multiply(exchangeRate));
////						saveHuman.setAnyAccidentSiLc(saveHuman.getAnyAccidentSi() == null ? null
////								: saveHuman.getInsurancePeriodSi().multiply(exchangeRate));
		//
//					} else {
//						saveHuman.setLiabilitySi(StringUtils.isBlank(req.getLiabilitySi()) ? new BigDecimal(0)
//								: new BigDecimal(req.getLiabilitySi()));
//						saveHuman.setProductTurnoverSi(StringUtils.isBlank(req.getProductTurnoverSi()) ? new BigDecimal(0)
//								: new BigDecimal(req.getProductTurnoverSi()));
		//
//						// Lc Calculation
//						BigDecimal exchangeRate = humanData.getExchangeRate() != null ? humanData.getExchangeRate()
//								: BigDecimal.ZERO;
////						saveHuman.setLiabilitySiLc(
////								saveHuman.getLiabilitySi() == null ? null : saveHuman.getLiabilitySi().multiply(exchangeRate));
//						saveHuman.setProductTurnoverSiLc(saveHuman.getProductTurnoverSi() == null ? null
//								: saveHuman.getProductTurnoverSi().multiply(exchangeRate));
//					}
//					// Status
//					saveHuman.setStatus(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus());
//					humanRepo.save(saveHuman);
		//
//					// Section Req
//					BuildingSectionRes sec = new BuildingSectionRes();
//					sec.setMotorYn(sectionData.getProductType());
//					sec.setSectionId(sectionData.getSectionId());
//					sec.setSectionName(sectionData.getSectionName());
//					sec.setOccupationId("1");
//					sectionList.add(sec);
		//
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Log Details" + e.getMessage());
		//
//				}
//				try {
//					List<OneTimeTableRes> otResList = null;
//					// One Time Table Thread Call
//					OneTimeTableReq otReq = new OneTimeTableReq();
//					otReq.setRequestReferenceNo(humanData.getRequestReferenceNo());
//					otReq.setVehicleId(humanData.getRiskId());
//					otReq.setBranchCode(humanData.getBranchCode());
//					otReq.setInsuranceId(humanData.getCompanyId());
//					otReq.setProductId(Integer.valueOf(humanData.getProductId()));
//					otReq.setSectionList(sectionList);
		//
//					otResList = otService.call_OT_Insert(otReq);
//					for (OneTimeTableRes otRes : otResList) {
//						SlideSectionSaveRes res = new SlideSectionSaveRes();
//						res.setResponse("Saved Successfully");
//						res.setRiskId(otRes.getVehicleId());
//						res.setVdRefNo(otRes.getVdRefNo());
//						res.setCdRefNo(otRes.getCdRefNo());
//						res.setMsrefno(otRes.getMsRefNo());
//						res.setCompanyId(otRes.getCompanyId());
//						res.setProductId(otRes.getProductId());
//						res.setSectionId(otRes.getSectionId());
//						res.setCustomerReferenceNo(humanData.getCustomerReferenceNo());
//						res.setRequestReferenceNo(humanData.getRequestReferenceNo());
//						res.setCreatedBy(req.getCreatedBy());
//						resList.add(res);
//					}
		//
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				return resList;
//			}
		//*/

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


			/*@Override
			public List<SlideSectionSaveRes> saveContentDetails(ContentSaveReq req) {
				// TODO Auto-generated method stub
				List<SlideSectionSaveRes> resList = new ArrayList<SlideSectionSaveRes>();
				EserviceBuildingDetails saveData = new EserviceBuildingDetails();
				EserviceSectionDetails sectionData = new EserviceSectionDetails();
				DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
				try {
					Double totalSumInsured = null;
					Map<Integer, List<ContentAndRisk>> riskGroup = null;

					List<ContentAndRisk> contentList = contentRepo
							.findByRequestReferenceNoAndSectionId(req.getRequestReferenceNo(), req.getSectionId());
					if (contentList.size() > 0 && contentList != null) {

						riskGroup = contentList.stream().filter(o -> o.getRiskId() != null)
								.collect(Collectors.groupingBy(ContentAndRisk::getRiskId));

					}
					EserviceBuildingDetails data = null;
					try {
						List<EserviceBuildingDetails> copyDataList = buildingRepo
								.findByRequestReferenceNoAndSectionId(req.getRequestReferenceNo(), req.getSectionId());
						if (null != copyDataList && !copyDataList.isEmpty()) {

							data = copyDataList.get(0);
						}
					} catch (Exception e) {

						log.error("Exception Occurs When Copy The Data From raw Table");
						log.error(e.getMessage());
						e.printStackTrace();
						// throw new

					}

					List<Integer> riskIds = contentList.stream()
							.map(a -> a.getRiskId() == null ? 0 : Integer.valueOf(a.getRiskId())).collect(Collectors.toList());

					if (!riskIds.stream().anyMatch(a -> a == 1)) {
						riskIds.add(1);
						List<EserviceBuildingDetails> saveDatas = buildingRepo.findByRequestReferenceNoAndSectionIdAndRiskId(
								req.getRequestReferenceNo(), req.getSectionId(), 1);

						if (null != saveDatas && !saveDatas.isEmpty()) {

							saveDatas.get(0).setContentSuminsured(null);
							buildingRepo.save(saveDatas.get(0));

							if (data == null) {
								data = saveDatas.get(0);
							}
						}

					}

					List<EserviceBuildingDetails> exist = buildingRepo.findByRequestReferenceNoAndSectionIdAndRiskIdNotIn(
							req.getRequestReferenceNo(), req.getSectionId(), riskIds);
					if (exist != null && exist.size() > 0) {

						buildingRepo.deleteAll(exist);
					}

					for (Integer risk : riskGroup.keySet()) {

						List<ContentAndRisk> con = contentRepo.findByRequestReferenceNoAndSectionIdAndRiskId(
								req.getRequestReferenceNo(), req.getSectionId(), risk);

						totalSumInsured = con.stream().filter(o -> o.getSumInsured() != null)
								.mapToDouble(o -> o.getSumInsured().doubleValue()).sum();

//						EserviceBuildingDetails data = buildingRepo.findByRequestReferenceNoAndRiskIdAndSectionId(
//								req.getRequestReferenceNo(), 1, req.getSectionId());
						sectionData = eserSecRepo.findByRequestReferenceNoAndRiskIdAndProductIdAndSectionId(
								req.getRequestReferenceNo(), Integer.valueOf(req.getRiskId()), req.getProductId(),
								req.getSectionId());
						if (data != null) {
							dozerBeanMapper.map(data, saveData);

						}
						saveData.setRiskId(risk);

//							saveData.setContentSuminsured(StringUtils.isBlank(req.getContentSuminsured()) ? new BigDecimal(0)
//									: new BigDecimal(req.getContentSuminsured()));

						if (req.getSectionId().equals("76")) {
							saveData.setEquipmentSi(
									totalSumInsured == null ? new BigDecimal(0) : new BigDecimal(totalSumInsured));
							saveData.setContentSuminsured(null);
						} else {
							saveData.setContentSuminsured(
									totalSumInsured == null ? new BigDecimal(0) : new BigDecimal(totalSumInsured));
							saveData.setEquipmentSi(StringUtils.isBlank(req.getEquipmentSi()) ? new BigDecimal(0)
									: new BigDecimal(req.getEquipmentSi()));
						}

						saveData.setJewellerySi(StringUtils.isBlank(req.getJewellerySi()) ? new BigDecimal(0)
								: new BigDecimal(req.getJewellerySi()));
						saveData.setPaitingsSi(StringUtils.isBlank(req.getPaitingsSi()) ? new BigDecimal(0)
								: new BigDecimal(req.getPaitingsSi()));
						saveData.setCarpetsSi(StringUtils.isBlank(req.getCarpetsSi()) ? new BigDecimal(0)
								: new BigDecimal(req.getCarpetsSi()));

						BigDecimal exchangeRate = saveData.getExchangeRate() != null ? saveData.getExchangeRate()
								: BigDecimal.ZERO;
						saveData.setContentSuminsuredLc(saveData.getContentSuminsured() == null ? null
								: saveData.getContentSuminsured().multiply(exchangeRate));
						saveData.setEquipmentSiLc(
								saveData.getEquipmentSi() == null ? null : saveData.getEquipmentSi().multiply(exchangeRate));
						saveData.setJewellerySiLc(
								saveData.getJewellerySi() == null ? null : saveData.getJewellerySi().multiply(exchangeRate));
						saveData.setPaitingsSiLc(
								saveData.getPaitingsSi() == null ? null : saveData.getPaitingsSi().multiply(exchangeRate));
						saveData.setCarpetsSiLc(
								saveData.getCarpetsSi() == null ? null : saveData.getCarpetsSi().multiply(exchangeRate));

						// Status
						saveData.setStatus(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus());
						// Endorsement CHanges
						if (!(req.getEndorsementType() == null || req.getEndorsementType() == 0))

						{

							saveData.setOriginalPolicyNo(req.getOriginalPolicyNo());
							saveData.setEndorsementDate(req.getEndorsementDate());
							saveData.setEndorsementRemarks(req.getEndorsementRemarks());
							saveData.setEndorsementEffdate(req.getEndorsementEffdate());
							saveData.setEndtPrevPolicyNo(req.getEndtPrevPolicyNo());
							saveData.setEndtPrevQuoteNo(req.getEndtPrevQuoteNo());
							saveData.setEndtCount(req.getEndtCount());
							saveData.setEndtStatus(req.getEndtStatus());
							saveData.setIsFinyn(req.getIsFinaceYn());
							saveData.setEndtCategDesc(req.getEndtCategDesc());
							saveData.setEndorsementType(req.getEndorsementType());
							saveData.setEndorsementTypeDesc(req.getEndorsementTypeDesc());

						}
						buildingRepo.save(saveData);

//				catch (Exception e) {
//						e.printStackTrace();
//						log.info("Log Details" + e.getMessage());
		//
//				}		

//			/*@Override
//			public List<SlideSectionSaveRes> saveContentDetails(ContentSaveReq req) {
//				// TODO Auto-generated method stub
//				List<SlideSectionSaveRes> resList = new ArrayList<SlideSectionSaveRes>();
//				EserviceBuildingDetails saveData = new EserviceBuildingDetails();
//				EserviceSectionDetails sectionData = new EserviceSectionDetails();
//				DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();

//				try {
//					Double totalSumInsured = null;
//					Map<Integer, List<ContentAndRisk>> riskGroup = null;
		//
//					List<ContentAndRisk> contentList = contentRepo
//							.findByRequestReferenceNoAndSectionId(req.getRequestReferenceNo(), req.getSectionId());
//					if (contentList.size() > 0 && contentList != null) {
		//
//						riskGroup = contentList.stream().filter(o -> o.getRiskId() != null)
//								.collect(Collectors.groupingBy(ContentAndRisk::getRiskId));
		//
//					}
//					EserviceBuildingDetails data = null;
//					try {
//						List<EserviceBuildingDetails> copyDataList = buildingRepo
//								.findByRequestReferenceNoAndSectionId(req.getRequestReferenceNo(), req.getSectionId());
//						if (null != copyDataList && !copyDataList.isEmpty()) {
		//
//							data = copyDataList.get(0);
//						}
//					} catch (Exception e) {
		//
//						log.error("Exception Occurs When Copy The Data From raw Table");
//						log.error(e.getMessage());
//						e.printStackTrace();
//						// throw new
		//
//					}
		//
//					List<Integer> riskIds = contentList.stream()
//							.map(a -> a.getRiskId() == null ? 0 : Integer.valueOf(a.getRiskId())).collect(Collectors.toList());
		//
//					if (!riskIds.stream().anyMatch(a -> a == 1)) {
//						riskIds.add(1);
//						List<EserviceBuildingDetails> saveDatas = buildingRepo.findByRequestReferenceNoAndSectionIdAndRiskId(
//								req.getRequestReferenceNo(), req.getSectionId(), 1);
		//
//						if (null != saveDatas && !saveDatas.isEmpty()) {
		//
////							saveDatas.get(0).setContentSuminsured(null);
//							buildingRepo.save(saveDatas.get(0));
		//
//							if (data == null) {
//								data = saveDatas.get(0);
//							}
//						}
		//
//					}
		//
//					List<EserviceBuildingDetails> exist = buildingRepo.findByRequestReferenceNoAndSectionIdAndRiskIdNotIn(
//							req.getRequestReferenceNo(), req.getSectionId(), riskIds);
//					if (exist != null && exist.size() > 0) {
		//
//						buildingRepo.deleteAll(exist);
//					}
		//
//					for (Integer risk : riskGroup.keySet()) {
		//
//						List<ContentAndRisk> con = contentRepo.findByRequestReferenceNoAndSectionIdAndRiskId(
//								req.getRequestReferenceNo(), req.getSectionId(), risk);
		//
//						totalSumInsured = con.stream().filter(o -> o.getSumInsured() != null)
//								.mapToDouble(o -> o.getSumInsured().doubleValue()).sum();
		//
////						EserviceBuildingDetails data = buildingRepo.findByRequestReferenceNoAndRiskIdAndSectionId(
////								req.getRequestReferenceNo(), 1, req.getSectionId());
//						sectionData = eserSecRepo.findByRequestReferenceNoAndRiskIdAndProductIdAndSectionId(
//								req.getRequestReferenceNo(), Integer.valueOf(req.getRiskId()), req.getProductId(),
//								req.getSectionId());
//						if (data != null) {
//							dozerBeanMapper.map(data, saveData);
		//
//						}
//						saveData.setRiskId(risk);
		//
////							saveData.setContentSuminsured(StringUtils.isBlank(req.getContentSuminsured()) ? new BigDecimal(0)
////									: new BigDecimal(req.getContentSuminsured()));
		//
//					/*	if (req.getSectionId().equals("76")) {
//							saveData.setEquipmentSi(
//									totalSumInsured == null ? new BigDecimal(0) : new BigDecimal(totalSumInsured));
//							saveData.setContentSuminsured(null);
//						} else {
//							saveData.setContentSuminsured(
//									totalSumInsured == null ? new BigDecimal(0) : new BigDecimal(totalSumInsured));
//							saveData.setEquipmentSi(StringUtils.isBlank(req.getEquipmentSi()) ? new BigDecimal(0)
//									: new BigDecimal(req.getEquipmentSi()));
//						} */
		//
//			//			saveData.setJewellerySi(StringUtils.isBlank(req.getJewellerySi()) ? new BigDecimal(0)
//			//					: new BigDecimal(req.getJewellerySi()));
//			//			saveData.setPaitingsSi(StringUtils.isBlank(req.getPaitingsSi()) ? new BigDecimal(0)
//			//					: new BigDecimal(req.getPaitingsSi()));
//			//			saveData.setCarpetsSi(StringUtils.isBlank(req.getCarpetsSi()) ? new BigDecimal(0)
//			//					: new BigDecimal(req.getCarpetsSi()));
		//
//						BigDecimal exchangeRate = saveData.getExchangeRate() != null ? saveData.getExchangeRate()
//								: BigDecimal.ZERO;
//				/*		saveData.setContentSuminsuredLc(saveData.getContentSuminsured() == null ? null
//								: saveData.getContentSuminsured().multiply(exchangeRate));
//						saveData.setEquipmentSiLc(
//								saveData.getEquipmentSi() == null ? null : saveData.getEquipmentSi().multiply(exchangeRate));
//						saveData.setJewellerySiLc(
//								saveData.getJewellerySi() == null ? null : saveData.getJewellerySi().multiply(exchangeRate));
//						saveData.setPaitingsSiLc(
//								saveData.getPaitingsSi() == null ? null : saveData.getPaitingsSi().multiply(exchangeRate));
//						saveData.setCarpetsSiLc(
//								saveData.getCarpetsSi() == null ? null : saveData.getCarpetsSi().multiply(exchangeRate));
//		                 */
//						// Status
//						saveData.setStatus(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus());
//						// Endorsement CHanges
//						if (!(req.getEndorsementType() == null || req.getEndorsementType() == 0))
		//
//						{
		//
//							saveData.setOriginalPolicyNo(req.getOriginalPolicyNo());
//							saveData.setEndorsementDate(req.getEndorsementDate());
//							saveData.setEndorsementRemarks(req.getEndorsementRemarks());
//							saveData.setEndorsementEffdate(req.getEndorsementEffdate());
//							saveData.setEndtPrevPolicyNo(req.getEndtPrevPolicyNo());
//							saveData.setEndtPrevQuoteNo(req.getEndtPrevQuoteNo());
//							saveData.setEndtCount(req.getEndtCount());
//							saveData.setEndtStatus(req.getEndtStatus());
//							saveData.setIsFinyn(req.getIsFinaceYn());
//							saveData.setEndtCategDesc(req.getEndtCategDesc());
//							saveData.setEndorsementType(req.getEndorsementType());
//							saveData.setEndorsementTypeDesc(req.getEndorsementTypeDesc());
		//
//						}
//						buildingRepo.save(saveData);
		//
////				catch (Exception e) {
////						e.printStackTrace();
////						log.info("Log Details" + e.getMessage());
		////
////				}		
////				try {
//						List<OneTimeTableRes> otResList = null;
//						// Section Req
//						List<BuildingSectionRes> sectionList = new ArrayList<BuildingSectionRes>();
//						BuildingSectionRes sec = new BuildingSectionRes();
//						sec.setMotorYn(sectionData.getProductType());
//						sec.setSectionId(sectionData.getSectionId());
//						sec.setSectionName(sectionData.getSectionName());
//						sectionList.add(sec);
		//
//						// One Time Table Thread Call
//						OneTimeTableReq otReq = new OneTimeTableReq();
//						otReq.setRequestReferenceNo(saveData.getRequestReferenceNo());
//						otReq.setVehicleId(saveData.getRiskId());
//						otReq.setBranchCode(saveData.getBranchCode());
//						otReq.setInsuranceId(saveData.getCompanyId());
//						otReq.setProductId(Integer.valueOf(saveData.getProductId()));
//						otReq.setSectionList(sectionList);
		//
//						otResList = otService.call_OT_Insert(otReq);
//						for (OneTimeTableRes otRes : otResList) {
//							SlideSectionSaveRes res = new SlideSectionSaveRes();
//							res.setResponse("Saved Successfully");
//							res.setRiskId(otRes.getVehicleId());
//							res.setVdRefNo(otRes.getVdRefNo());
//							res.setCdRefNo(otRes.getCdRefNo());
//							res.setMsrefno(otRes.getMsRefNo());
//							res.setCompanyId(otRes.getCompanyId());
//							res.setProductId(otRes.getProductId());
//							res.setSectionId(otRes.getSectionId());
//							res.setCustomerReferenceNo(saveData.getCustomerReferenceNo());
//							res.setRequestReferenceNo(saveData.getRequestReferenceNo());
//							res.setCreatedBy(req.getCreatedBy());
//							resList.add(res);
//						}
		//
//					}
		//
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				return resList;
//			}
		//
//			@Override
//			public List<SlideSectionSaveRes> saveElectronicEquipDetails(List<ElectronicEquipSaveReq> reqList) {
//				// TODO Auto-generated method stub
		//
//				List<SlideSectionSaveRes> resList = new ArrayList<SlideSectionSaveRes>();
//				EserviceBuildingDetails saveData = new EserviceBuildingDetails();
//				EserviceSectionDetails sectionData = new EserviceSectionDetails();
//				sectionData = eserSecRepo.findByRequestReferenceNoAndSectionIdAndRiskId(reqList.get(0).getRequestReferenceNo(),
//						reqList.get(0).getSectionId(), 1);
//				DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
//				double totalSumInsured = 0.0;
//				try {
		//
//						List<OneTimeTableRes> otResList = null;
//						// Section Req
//						List<BuildingSectionRes> sectionList = new ArrayList<BuildingSectionRes>();
//						BuildingSectionRes sec = new BuildingSectionRes();
//						sec.setMotorYn(sectionData.getProductType());
//						sec.setSectionId(sectionData.getSectionId());
//						sec.setSectionName(sectionData.getSectionName());
		//
//						sectionList.add(sec);
		//
//						// One Time Table Thread Call
//						OneTimeTableReq otReq = new OneTimeTableReq();
//						otReq.setRequestReferenceNo(saveData.getRequestReferenceNo());
//						otReq.setVehicleId(saveData.getRiskId());
//						otReq.setBranchCode(saveData.getBranchCode());
//						otReq.setInsuranceId(saveData.getCompanyId());
//						otReq.setProductId(Integer.valueOf(saveData.getProductId()));
//						otReq.setSectionList(sectionList);
//						otReq.setLocationId(saveData.getLocationId());
//						otResList = otService.call_OT_Insert(otReq);
//						for (OneTimeTableRes otRes : otResList) {
//							SlideSectionSaveRes res = new SlideSectionSaveRes();
//							res.setResponse("Saved Successfully");
//							res.setRiskId(otRes.getVehicleId());
//							res.setVdRefNo(otRes.getVdRefNo());
//							res.setCdRefNo(otRes.getCdRefNo());
//							res.setMsrefno(otRes.getMsRefNo());
//							res.setCompanyId(otRes.getCompanyId());
//							res.setProductId(otRes.getProductId());
//							res.setSectionId(otRes.getSectionId());
//							res.setCustomerReferenceNo(saveData.getCustomerReferenceNo());
//							res.setRequestReferenceNo(saveData.getRequestReferenceNo());
//							res.setCreatedBy(req.getCreatedBy());
//							resList.add(res);
//						}
//					}
		//
//					if ("25".equalsIgnoreCase(reqList.get(0).getProductId())) {
//						updatesectionDetails(reqList.get(0).getRequestReferenceNo());
//					}
		//
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				return resList;
//			}

//					List<EserviceBuildingDetails> saveData00 = buildingRepo.findByRequestReferenceNoAndSectionId(
//							reqList.get(0).getRequestReferenceNo(), reqList.get(0).getSectionId());
		//
//					EserviceBuildingDetails data = buildingRepo.findByRequestReferenceNoAndRiskIdAndSectionId(
//							reqList.get(0).getRequestReferenceNo(), 1, reqList.get(0).getSectionId());
		//
//					if (data != null) {
		//
//						dozerBeanMapper.map(data, saveData);
		//
//					}
//					List<EserviceBuildingDetails> saveData1 = buildingRepo.findByRequestReferenceNoAndSectionId(
//							reqList.get(0).getRequestReferenceNo(), reqList.get(0).getSectionId());
//					if (saveData1 != null && saveData1.size() > 0) {
//						buildingRepo.deleteAll(saveData1);
//					}
//					// sectionData =
//					// eserSecRepo.findByRequestReferenceNoAndRiskIdAndProductIdAndSectionId(req.getRequestReferenceNo(),Integer.valueOf(req.getRiskId())
//					// , req.getProductId() ,req.getSectionId());
		//
//					for (ElectronicEquipSaveReq req : reqList) {
//						List<ContentAndRisk> contentList = contentRepo.findByRequestReferenceNoAndSectionIdAndRiskId(
//								req.getRequestReferenceNo(), req.getSectionId(), Integer.valueOf(req.getRiskId()));
//						if (!contentList.isEmpty() && !"25".equalsIgnoreCase(req.getProductId())) {
//							totalSumInsured = contentList.stream()
//									.mapToDouble(content -> content != null ? content.getSumInsured().doubleValue() : 0.0) // Replace
//									.sum();
//						} else if (contentList.isEmpty() && StringUtils.isNotBlank(req.getElecEquipSuminsured())) {
//							totalSumInsured = Double.valueOf(req.getElecEquipSuminsured());
//						}
//						if ("42".equalsIgnoreCase(req.getProductId())) {
//							String occupationDesc = getListItem(saveData.getCompanyId(), saveData.getBranchCode(),
//									"CYBER_INSURANCE_TYPE", req.getOccupationType());
//							saveData.setOccupationType(req.getOccupationType());
//							saveData.setOccupationTypeDesc(occupationDesc);
//						} else {
//							// saveData.setElecEquipSuminsured(StringUtils.isBlank(req.getElecEquipSuminsured())
//							// ? new BigDecimal(0): new BigDecimal(req.getElecEquipSuminsured()));
		//
////							saveData.setElecEquipSuminsured(new BigDecimal(totalSumInsured));
		//
//						}
//						// Content Addition Info
//						saveData.setContentId((StringUtils.isBlank(req.getContentId()) || req.getContentId() == null) ? null
//								: req.getContentId());
//						saveData.setContentDesc(
//								(StringUtils.isBlank(req.getContentDesc()) || req.getContentDesc() == null) ? null
//										: req.getContentDesc());
//						saveData.setLocationName(
//								(StringUtils.isBlank(req.getLocationName()) || req.getLocationName() == null) ? null
//										: req.getLocationName());
//						saveData.setSerialNo((StringUtils.isBlank(req.getSerialNo()) || req.getSerialNo() == null) ? null
//								: req.getSerialNo());
//						saveData.setDescriptionOfRisk(
//								(StringUtils.isBlank(req.getDescription()) || req.getDescription() == null) ? null
//										: req.getDescription());
//						// ---
//						saveData.setRiskId(Integer.valueOf(req.getRiskId()));
//						// Lc Insert
//						if (saveData1 != null && StringUtils.isNotBlank(saveData1.get(0).getExchangeRate().toString())) {
//							BigDecimal exRate = saveData.getExchangeRate();
//				//			if (StringUtils.isNotBlank(req.getElecEquipSuminsured())) {
//				//				saveData.setElecEquipSuminsuredLc(new BigDecimal(totalSumInsured).multiply(exRate));
//				//			} else {
//				//				saveData.setElecEquipSuminsuredLc(BigDecimal.ZERO);
//				//			}
//						} //else {
//					//		saveData.setElecEquipSuminsuredLc(BigDecimal.ZERO);
//					//	}
		//
//						if (sectionData != null) {
//							saveData.setCustomerReferenceNo(sectionData.getCustomerReferenceNo());
//							saveData.setRequestReferenceNo(sectionData.getRequestReferenceNo());
		//
//							saveData.setSectionId(sectionData.getSectionId());
//							saveData.setSectionDesc(sectionData.getSectionName());
//							saveData.setProductDesc(sectionData.getProductDesc());
		//
//						}
//						// Endorsement CHanges
//						if (!(req.getEndorsementType() == null || req.getEndorsementType() == 0))
		//
//						{
		//
//							saveData.setOriginalPolicyNo(req.getOriginalPolicyNo());
//							saveData.setEndorsementDate(req.getEndorsementDate());
//							saveData.setEndorsementRemarks(req.getEndorsementRemarks());
//							saveData.setEndorsementEffdate(req.getEndorsementEffdate());
//							saveData.setEndtPrevPolicyNo(req.getEndtPrevPolicyNo());
//							saveData.setEndtPrevQuoteNo(req.getEndtPrevQuoteNo());
//							saveData.setEndtCount(req.getEndtCount());
//							saveData.setEndtStatus(req.getEndtStatus());
//							saveData.setIsFinyn(req.getIsFinaceYn());
//							saveData.setEndtCategDesc(req.getEndtCategDesc());
//							saveData.setEndorsementType(req.getEndorsementType());
//							saveData.setEndorsementTypeDesc(req.getEndorsementTypeDesc());
		//
//						}
		//
//						// Status
//						saveData.setStatus(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus());
		//
//						buildingRepo.saveAndFlush(saveData);
		//
////				} catch (Exception e) {
////					e.printStackTrace();
////					log.info("Log Details" + e.getMessage());
		////
////				}
////				try {
//						List<OneTimeTableRes> otResList = null;
//						// Section Req
//						List<BuildingSectionRes> sectionList = new ArrayList<BuildingSectionRes>();
//						BuildingSectionRes sec = new BuildingSectionRes();
//						sec.setMotorYn(sectionData.getProductType());
//						sec.setSectionId(sectionData.getSectionId());
//						sec.setSectionName(sectionData.getSectionName());
		//
//						sectionList.add(sec);
		//
//						// One Time Table Thread Call
//						OneTimeTableReq otReq = new OneTimeTableReq();
//						otReq.setRequestReferenceNo(saveData.getRequestReferenceNo());
//						otReq.setVehicleId(saveData.getRiskId());
//						otReq.setBranchCode(saveData.getBranchCode());
//						otReq.setInsuranceId(saveData.getCompanyId());
//						otReq.setProductId(Integer.valueOf(saveData.getProductId()));
//						otReq.setSectionList(sectionList);
//						otReq.setLocationId(saveData.getLocationId());
//						otResList = otService.call_OT_Insert(otReq);
//						for (OneTimeTableRes otRes : otResList) {
//							SlideSectionSaveRes res = new SlideSectionSaveRes();
//							res.setResponse("Saved Successfully");
//							res.setRiskId(otRes.getVehicleId());
//							res.setVdRefNo(otRes.getVdRefNo());
//							res.setCdRefNo(otRes.getCdRefNo());
//							res.setMsrefno(otRes.getMsRefNo());
//							res.setCompanyId(otRes.getCompanyId());
//							res.setProductId(otRes.getProductId());
//							res.setSectionId(otRes.getSectionId());
//							res.setCustomerReferenceNo(saveData.getCustomerReferenceNo());
//							res.setRequestReferenceNo(saveData.getRequestReferenceNo());
//							res.setCreatedBy(req.getCreatedBy());
//							resList.add(res);
//						}
//					}
		//
//					if ("25".equalsIgnoreCase(reqList.get(0).getProductId())) {
//						updatesectionDetails(reqList.get(0).getRequestReferenceNo());
//					}
		//
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				return resList;
//			}
		//*/

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

		/*
			@Override
			public SlideCommonSaveRes getCommonDetails(CommonGetReq req) {
				SlideCommonSaveRes res = new SlideCommonSaveRes();
				DozerBeanMapper dozerMapper = new DozerBeanMapper();
				try {
					CompanyProductMaster product = getCompanyProductMasterDropdown(req.getInsuranceId(), req.getProductId()); // productRepo.findByProductIdOrderByAmendIdDesc(Integer.valueOf(req.getProductId()));

					// Human Get
					if (StringUtils.isNotBlank(product.getMotorYn()) && product.getMotorYn().equalsIgnoreCase("H")) {
						List<EserviceCommonDetails> getHumanData = humanRepo
								.findByRequestReferenceNo(req.getRequestReferenceNo());
						if (getHumanData.size() > 0) {
							dozerMapper.map(getHumanData.get(0), res);
						}

						// Asset Get
					} else {
						EserviceBuildingDetails getBuildingData = buildingRepo
								.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo(), 1, "0");

						dozerMapper.map(getBuildingData, res);

					}

					List<EserviceSectionDetails> sectionDetails = eserSecRepo
							.findByRequestReferenceNoOrderByRiskIdAsc(req.getRequestReferenceNo());
					List<String> sectionIds = new ArrayList<String>();
					for (EserviceSectionDetails sec : sectionDetails) {
						sectionIds.add(sec.getSectionId());
					}
					res.setSectionIds(sectionIds);

				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());
					return null;

				}
				return res;
			}

			@Override
			public AccidentDamageSaveResponse getAccidentDamgeDetails(SlideSectionGetReq req) {
				AccidentDamageSaveResponse res = new AccidentDamageSaveResponse();
				try {
					EserviceBuildingDetails getBuildingData = buildingRepo
							.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo(), 1, req.getSectionId());

					if (getBuildingData != null) {
						res.setCreatedBy(getBuildingData.getCreatedBy());
						res.setInsuranceId(getBuildingData.getCompanyId());
						res.setProductId(getBuildingData.getProductId());
						res.setRequestReferenceNo(getBuildingData.getRequestReferenceNo());
						res.setRiskId(getBuildingData.getRiskId() == null ? "" : getBuildingData.getRiskId().toString());
						res.setSectionId(req.getSectionId());
						// res.setAccDamageSi(getBuildingData.getAccDamageSi()==null?"" :
						// getBuildingData.getAccDamageSi().toPlainString());
					}

				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());
					return null;

				}
				return res;
			}

			@Override
			public AllRiskDetailsRes getAllRiskDetails(SlideSectionGetReq req) {
				AllRiskDetailsRes res = new AllRiskDetailsRes();
				try {
					EserviceBuildingDetails getBuildingData = buildingRepo
							.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo(), 1, req.getSectionId());

					if (getBuildingData != null) {
						res.setCreatedBy(getBuildingData.getCreatedBy());
						res.setInsuranceId(getBuildingData.getCompanyId());
						res.setProductId(getBuildingData.getProductId());
						res.setRequestReferenceNo(getBuildingData.getRequestReferenceNo());
						res.setRiskId(getBuildingData.getRiskId() == null ? "" : getBuildingData.getRiskId().toString());
						res.setSectionId(req.getSectionId());
						res.setAllriskSuminsured(getBuildingData.getAllriskSuminsured() == null ? ""
								: getBuildingData.getAllriskSuminsured().toPlainString());
						res.setMiningPlantSi(getBuildingData.getMiningPlantSi() == null ? ""
								: getBuildingData.getMiningPlantSi().toPlainString());
						res.setNonminingPlantSi(getBuildingData.getNonminingPlantSi() == null ? ""
								: getBuildingData.getNonminingPlantSi().toPlainString());
						res.setGensetsSi(
								getBuildingData.getGensetsSi() == null ? "" : getBuildingData.getGensetsSi().toPlainString());
						res.setEquipmentSi(getBuildingData.getEquipmentSi() == null ? ""
								: getBuildingData.getEquipmentSi().toPlainString());
						res.setEndorsementDate(
								getBuildingData.getEndorsementDate() == null ? null : getBuildingData.getEndorsementDate());
						res.setEndorsementEffdate(getBuildingData.getEndorsementEffdate() == null ? null
								: getBuildingData.getEndorsementEffdate());
						res.setEndorsementRemarks(getBuildingData.getEndorsementRemarks() == null ? ""
								: getBuildingData.getEndorsementRemarks().toString());
						res.setEndorsementType(getBuildingData.getEndorsementType() == null ? ""
								: getBuildingData.getEndorsementType().toString());
						res.setEndorsementTypeDesc(getBuildingData.getEndorsementTypeDesc() == null ? ""
								: getBuildingData.getEndorsementTypeDesc().toString());
						res.setEndtCount(
								getBuildingData.getEndtCount() == null ? "" : getBuildingData.getEndtCount().toString());
						res.setEndtCategDesc(
								getBuildingData.getEndtCategDesc() == null ? "" : getBuildingData.getEndtCategDesc());
						res.setOriginalPolicyNo(
								getBuildingData.getOriginalPolicyNo() == null ? null : getBuildingData.getOriginalPolicyNo());
						res.setIsFinaceYn(
								getBuildingData.getIsFinyn() == null ? null : getBuildingData.getIsFinyn().toString());
						res.setEndtPrevPolicyNo(getBuildingData.getEndtPrevPolicyNo() == null ? ""
								: getBuildingData.getEndtPrevPolicyNo().toString());
						res.setEndtPrevQuoteNo(getBuildingData.getEndtPrevQuoteNo() == null ? ""
								: getBuildingData.getEndtPrevQuoteNo().toString());
						res.setEndtStatus(
								getBuildingData.getEndtStatus() == null ? "" : getBuildingData.getEndtStatus().toString());
					}
				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());
					return null;

				}
				return res;
			}

			@Override
			public List<BurglaryAndHouseBreakingSaveRes> getBurglaryAndHouseBreakingDetails(SlideSectionGetReq req) {
				List<BurglaryAndHouseBreakingSaveRes> resList = new ArrayList<BurglaryAndHouseBreakingSaveRes>();
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
				try {
					List<EserviceBuildingDetails> getBuildingDatas = buildingRepo
							.findByRequestReferenceNoAndSectionId(req.getRequestReferenceNo(), req.getSectionId());

					if (getBuildingDatas != null) {
						for (EserviceBuildingDetails getBuildingData : getBuildingDatas) {
							BurglaryAndHouseBreakingSaveRes res = new BurglaryAndHouseBreakingSaveRes();
							// <ListItemValue> firstLossList =
							// getFirstLossDropDown(getBuildingData.getCompanyId(),
							// getBuildingData.getBranchCode() , "FIRST_LOSS_PERCENT");
							res.setFirstLossPercentId(getBuildingData.getFirstLossPercentId() == null ? ""
									: getBuildingData.getFirstLossPercentId().toString());
							res.setStockLossPercent(getBuildingData.getStockLossPercent() == null ? null
									: getBuildingData.getStockLossPercent().toString());
							res.setGoodsLossPercent(getBuildingData.getGoodsLossPercent() == null ? null
									: getBuildingData.getGoodsLossPercent().toString());
							res.setFurnitureLossPercent(getBuildingData.getFurnitureLossPercent() == null ? null
									: getBuildingData.getFurnitureLossPercent().toString());
							res.setApplianceLossPercent(getBuildingData.getApplianceLossPercent() == null ? null
									: getBuildingData.getApplianceLossPercent().toString());
							res.setCashValueablesLossPercent(getBuildingData.getCashValueablesLossPercent() == null ? null
									: getBuildingData.getCashValueablesLossPercent().toString());
							res.setBurglarySi(getBuildingData.getBurglarySi() == null ? null
									: getBuildingData.getBurglarySi().toPlainString());

							res.setCreatedBy(getBuildingData.getCreatedBy());
							res.setInsuranceId(getBuildingData.getCompanyId());
							res.setProductId(getBuildingData.getProductId());
							res.setRequestReferenceNo(getBuildingData.getRequestReferenceNo());
							res.setRiskId(getBuildingData.getRiskId() == null ? "" : getBuildingData.getRiskId().toString());
							res.setSectionId(req.getSectionId());
							res.setStockInTradeSi(getBuildingData.getStockInTradeSi() == null ? ""
									: getBuildingData.getStockInTradeSi().toPlainString());
							res.setGoodsSi(
									getBuildingData.getGoodsSi() == null ? "" : getBuildingData.getGoodsSi().toPlainString());
							res.setFurnitureSi(getBuildingData.getFurnitureSi() == null ? ""
									: getBuildingData.getFurnitureSi().toPlainString());
							res.setApplianceSi(getBuildingData.getApplianceSi() == null ? ""
									: getBuildingData.getApplianceSi().toPlainString());
							res.setCashValueablesSi(getBuildingData.getCashValueablesSi() == null ? ""
									: getBuildingData.getCashValueablesSi().toPlainString());
							res.setBuildingOwnerYn(
									getBuildingData.getBuildingOwnerYn() == null ? "" : getBuildingData.getBuildingOwnerYn());
							res.setAccessibleWindows(getBuildingData.getAccessibleWindows() == null ? ""
									: getBuildingData.getAccessibleWindows().toString());
							res.setAddress(getBuildingData.getAddress() == null ? "" : (getBuildingData.getAddress()));
							res.setBackDoors(
									getBuildingData.getBackDoors() == null ? "" : getBuildingData.getBackDoors().toString());
							res.setBuildingOccupied(getBuildingData.getBuildingOccupied() == null ? ""
									: getBuildingData.getBuildingOccupied().toString());
							res.setCeilingType(getBuildingData.getCeilingType() == null ? ""
									: getBuildingData.getCeilingType().toString());
							res.setDistrictCode(getBuildingData.getDistrictCode() == null ? ""
									: getBuildingData.getDistrictCode().toString());
							res.setDoorsMaterialId(getBuildingData.getDoorsMaterialId() == null ? ""
									: getBuildingData.getDoorsMaterialId().toString());
							res.setFrontDoors(
									getBuildingData.getFrontDoors() == null ? "" : getBuildingData.getFrontDoors().toString());
							res.setInsuranceForId(getBuildingData.getInsuranceForId() != null
									? Arrays.asList(getBuildingData.getInsuranceForId().split(","))
									: null);
							res.setNatureOfTradeId(getBuildingData.getNatureOfTradeId() == null ? ""
									: getBuildingData.getNatureOfTradeId().toString());
							res.setInternalWallType(getBuildingData.getInternalWallType() == null ? ""
									: getBuildingData.getInternalWallType().toString());
							res.setNightLeftDoor(getBuildingData.getNightLeftDoor() == null ? ""
									: getBuildingData.getNightLeftDoor().toString());
							res.setOccupiedYear(getBuildingData.getOccupiedYear() == null ? ""
									: getBuildingData.getOccupiedYear().toString());
							res.setShowWindow(getBuildingData.getShowWindows() == null ? ""
									: getBuildingData.getShowWindows().toString());
							res.setTrapDoors(
									getBuildingData.getTrapDoors() == null ? "" : getBuildingData.getTrapDoors().toString());
							res.setWatchmanGuardHours(getBuildingData.getWatchmanGuardHours() == null ? ""
									: getBuildingData.getWatchmanGuardHours().toString());
							res.setWindowsMaterialId(getBuildingData.getWindowsMaterialId() == null ? ""
									: getBuildingData.getWindowsMaterialId().toString());
							res.setBuildingBuildYear(getBuildingData.getBuildingBuildYear() == null ? ""
									: getBuildingData.getBuildingBuildYear().toString());
							res.setRoofType(
									getBuildingData.getRoofType() == null ? "" : getBuildingData.getRoofType().toString());
							res.setWallType(
									getBuildingData.getWallType() == null ? "" : getBuildingData.getWallType().toString());
							res.setRegionCode(
									getBuildingData.getRegionCode() == null ? "" : getBuildingData.getRegionCode().toString());
							res.setDistrictCode(
									getBuildingData.getDistrictCode() == null ? "" : getBuildingData.getDistrictCode());

							res.setRegionName(getBuildingData.getRegionDesc() == null ? null : getBuildingData.getRegionDesc());

							res.setDistrictName(
									getBuildingData.getDistrictDesc() == null ? null : getBuildingData.getDistrictDesc());

							res.setLocationName((getBuildingData.getLocationName() == ""
									|| StringUtils.isBlank(getBuildingData.getCoveringDetails())) ? ""
											: getBuildingData.getLocationName());
							res.setCoveringDetails((getBuildingData.getCoveringDetails() == null
									|| StringUtils.isBlank(getBuildingData.getCoveringDetails())) ? ""
											: getBuildingData.getCoveringDetails());
							res.setDescriptionOfRisk((getBuildingData.getDescriptionOfRisk() == null
									|| StringUtils.isBlank(getBuildingData.getDescriptionOfRisk())) ? ""
											: getBuildingData.getDescriptionOfRisk());
							res.setIndustryType((getBuildingData.getCategoryId() == null
									|| StringUtils.isBlank(getBuildingData.getCategoryId())) ? ""
											: getBuildingData.getCategoryId());
							res.setAddress(
									(getBuildingData.getAddress() == null || StringUtils.isBlank(getBuildingData.getAddress()))
											? ""
											: getBuildingData.getAddress());

							// Endorsement
							res.setEndorsementDate(
									getBuildingData.getEndorsementDate() == null ? null : getBuildingData.getEndorsementDate());
							res.setEndorsementEffdate(getBuildingData.getEndorsementEffdate() == null ? null
									: getBuildingData.getEndorsementEffdate());
							res.setEndorsementRemarks(getBuildingData.getEndorsementRemarks() == null ? ""
									: getBuildingData.getEndorsementRemarks().toString());
							res.setEndorsementType(getBuildingData.getEndorsementType() == null ? ""
									: getBuildingData.getEndorsementType().toString());
							res.setEndorsementTypeDesc(getBuildingData.getEndorsementTypeDesc() == null ? ""
									: getBuildingData.getEndorsementTypeDesc().toString());
							res.setEndtCount(
									getBuildingData.getEndtCount() == null ? "" : getBuildingData.getEndtCount().toString());
							res.setEndtCategDesc(
									getBuildingData.getEndtCategDesc() == null ? "" : getBuildingData.getEndtCategDesc());
							res.setOriginalPolicyNo(getBuildingData.getOriginalPolicyNo() == null ? null
									: getBuildingData.getOriginalPolicyNo());
							res.setIsFinaceYn(
									getBuildingData.getIsFinyn() == null ? null : getBuildingData.getIsFinyn().toString());
							res.setEndtPrevPolicyNo(getBuildingData.getEndtPrevPolicyNo() == null ? ""
									: getBuildingData.getEndtPrevPolicyNo().toString());
							res.setEndtPrevQuoteNo(getBuildingData.getEndtPrevQuoteNo() == null ? ""
									: getBuildingData.getEndtPrevQuoteNo().toString());
							res.setEndtStatus(
									getBuildingData.getEndtStatus() == null ? "" : getBuildingData.getEndtStatus().toString());
							resList.add(res);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());
					return null;

				}
				return resList;
			}

			@Override
			public FireAndAlliedPerillsSaveRes getFireAndAlliedPerils(SlideSectionGetReq req) {
				FireAndAlliedPerillsSaveRes res = new FireAndAlliedPerillsSaveRes();
				try {
					EserviceBuildingDetails getBuildingData = buildingRepo
							.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo(), 1, req.getSectionId());

					if (getBuildingData != null) {
						res.setCreatedBy(getBuildingData.getCreatedBy());
						res.setInsuranceId(getBuildingData.getCompanyId());
						res.setProductId(getBuildingData.getProductId());
						res.setRequestReferenceNo(getBuildingData.getRequestReferenceNo());
						res.setRiskId(getBuildingData.getRiskId() == null ? "" : getBuildingData.getRiskId().toString());
						res.setSectionId(req.getSectionId());
						res.setBuildingSuminsured(getBuildingData.getBuildingSuminsured() == null ? ""
								: getBuildingData.getBuildingSuminsured().toPlainString());
						res.setFirePlantSi(getBuildingData.getFirePlantSi() == null ? ""
								: getBuildingData.getFirePlantSi().toPlainString());
						res.setStockInTradeSi(getBuildingData.getStockInTradeSi() == null ? ""
								: getBuildingData.getStockInTradeSi().toPlainString());
						res.setFireEquipSi(getBuildingData.getEquipmentSi() == null ? ""
								: getBuildingData.getEquipmentSi().toPlainString());
						res.setIndemityPeriod(getBuildingData.getIndemityPeriod() == null ? ""
								: getBuildingData.getIndemityPeriod().toString());
						res.setMakutiYn(getBuildingData.getMakutiYn() == null ? "" : getBuildingData.getMakutiYn());
						res.setOnStockSi(
								getBuildingData.getOnStockSi() == null ? "" : getBuildingData.getOnStockSi().toPlainString());
						res.setOnAssetsSi(
								getBuildingData.getOnAssetsSi() == null ? "" : getBuildingData.getOnAssetsSi().toPlainString());

						// Endorsement
						res.setEndorsementDate(
								getBuildingData.getEndorsementDate() == null ? null : getBuildingData.getEndorsementDate());
						res.setEndorsementEffdate(getBuildingData.getEndorsementEffdate() == null ? null
								: getBuildingData.getEndorsementEffdate());
						res.setEndorsementRemarks(getBuildingData.getEndorsementRemarks() == null ? ""
								: getBuildingData.getEndorsementRemarks().toString());
						res.setEndorsementType(getBuildingData.getEndorsementType() == null ? ""
								: getBuildingData.getEndorsementType().toString());
						res.setEndorsementTypeDesc(getBuildingData.getEndorsementTypeDesc() == null ? ""
								: getBuildingData.getEndorsementTypeDesc().toString());
						res.setEndtCount(
								getBuildingData.getEndtCount() == null ? "" : getBuildingData.getEndtCount().toString());
						res.setEndtCategDesc(
								getBuildingData.getEndtCategDesc() == null ? "" : getBuildingData.getEndtCategDesc());
						res.setOriginalPolicyNo(
								getBuildingData.getOriginalPolicyNo() == null ? null : getBuildingData.getOriginalPolicyNo());
						res.setIsFinaceYn(
								getBuildingData.getIsFinyn() == null ? null : getBuildingData.getIsFinyn().toString());
						res.setEndtPrevPolicyNo(getBuildingData.getEndtPrevPolicyNo() == null ? ""
								: getBuildingData.getEndtPrevPolicyNo().toString());
						res.setEndtPrevQuoteNo(getBuildingData.getEndtPrevQuoteNo() == null ? ""
								: getBuildingData.getEndtPrevQuoteNo().toString());
						res.setEndtStatus(
								getBuildingData.getEndtStatus() == null ? "" : getBuildingData.getEndtStatus().toString());

					}

				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());
					return null;

				}
				return res;
			}

			@Override
			public ContentSaveRes getContentDetails(SlideSectionGetReq req) {
				ContentSaveRes res = new ContentSaveRes();
				try {
					EserviceBuildingDetails getBuildingData = buildingRepo
							.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo(), 1, req.getSectionId());

					if (getBuildingData != null) {
						res.setCreatedBy(getBuildingData.getCreatedBy());
						res.setInsuranceId(getBuildingData.getCompanyId());
						res.setProductId(getBuildingData.getProductId());
						res.setRequestReferenceNo(getBuildingData.getRequestReferenceNo());
						res.setRiskId(getBuildingData.getRiskId() == null ? "" : getBuildingData.getRiskId().toString());
						res.setSectionId(req.getSectionId());
						res.setContentSuminsured(getBuildingData.getContentSuminsured() == null ? ""
								: getBuildingData.getContentSuminsured().toPlainString());
						res.setEquipmentSi(getBuildingData.getEquipmentSi() == null ? ""
								: getBuildingData.getEquipmentSi().toPlainString());
						res.setJewellerySi(getBuildingData.getJewellerySi() == null ? ""
								: getBuildingData.getJewellerySi().toPlainString());
						res.setPaitingsSi(
								getBuildingData.getPaitingsSi() == null ? "" : getBuildingData.getPaitingsSi().toPlainString());
						res.setCarpetsSi(
								getBuildingData.getCarpetsSi() == null ? "" : getBuildingData.getCarpetsSi().toPlainString());

						res.setEndorsementDate(
								getBuildingData.getEndorsementDate() == null ? null : getBuildingData.getEndorsementDate());
						res.setEndorsementEffdate(getBuildingData.getEndorsementEffdate() == null ? null
								: getBuildingData.getEndorsementEffdate());
						res.setEndorsementRemarks(getBuildingData.getEndorsementRemarks() == null ? ""
								: getBuildingData.getEndorsementRemarks().toString());
						res.setEndorsementType(getBuildingData.getEndorsementType() == null ? ""
								: getBuildingData.getEndorsementType().toString());
						res.setEndorsementTypeDesc(getBuildingData.getEndorsementTypeDesc() == null ? ""
								: getBuildingData.getEndorsementTypeDesc().toString());
						res.setEndtCount(
								getBuildingData.getEndtCount() == null ? "" : getBuildingData.getEndtCount().toString());
						res.setEndtCategDesc(
								getBuildingData.getEndtCategDesc() == null ? "" : getBuildingData.getEndtCategDesc());
						res.setOriginalPolicyNo(
								getBuildingData.getOriginalPolicyNo() == null ? null : getBuildingData.getOriginalPolicyNo());
						res.setIsFinaceYn(
								getBuildingData.getIsFinyn() == null ? null : getBuildingData.getIsFinyn().toString());
						res.setEndtPrevPolicyNo(getBuildingData.getEndtPrevPolicyNo() == null ? ""
								: getBuildingData.getEndtPrevPolicyNo().toString());
						res.setEndtPrevQuoteNo(getBuildingData.getEndtPrevQuoteNo() == null ? ""
								: getBuildingData.getEndtPrevQuoteNo().toString());
						res.setEndtStatus(
								getBuildingData.getEndtStatus() == null ? "" : getBuildingData.getEndtStatus().toString());
					}
				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());
					return null;

				}
				return res;
			}

			@Override
			public List<ElectronicEquipSaveRes> getElectronicEquipDetails(SlideSectionGetReq req) {
				List<ElectronicEquipSaveRes> resList = new ArrayList<ElectronicEquipSaveRes>();
				try {
					List<EserviceBuildingDetails> buildingData = buildingRepo
							.findByRequestReferenceNoAndSectionId(req.getRequestReferenceNo(), req.getSectionId());
					List<EserviceSectionDetails> sectionDatas = eserSecRepo
							.findByRequestReferenceNoOrderByRiskIdAsc(req.getRequestReferenceNo());

					if (buildingData != null) {
						for (EserviceBuildingDetails getBuildingData : buildingData) {
							ElectronicEquipSaveRes res = new ElectronicEquipSaveRes();
							res.setCreatedBy(getBuildingData.getCreatedBy());
							res.setInsuranceId(getBuildingData.getCompanyId());
							res.setProductId(getBuildingData.getProductId());
							res.setRequestReferenceNo(getBuildingData.getRequestReferenceNo());
							res.setRiskId(getBuildingData.getRiskId() == null ? "" : getBuildingData.getRiskId().toString());
							res.setSectionId("42".equalsIgnoreCase(getBuildingData.getProductId()) && sectionDatas.size() > 0
									? sectionDatas.get(0).getSectionId()
									: req.getSectionId());
							res.setElecEquipSuminsured(getBuildingData.getElecEquipSuminsured() == null ? ""
									: getBuildingData.getElecEquipSuminsured().toPlainString());
							res.setContentId(
									StringUtil.isBlank(getBuildingData.getContentId()) ? "" : getBuildingData.getContentId());
							res.setContentDesc(StringUtil.isBlank(getBuildingData.getContentDesc()) ? ""
									: getBuildingData.getContentDesc());
							res.setLocationName(StringUtil.isBlank(getBuildingData.getLocationName()) ? ""
									: getBuildingData.getLocationName());
							res.setDescription(StringUtil.isBlank(getBuildingData.getDescriptionOfRisk()) ? ""
									: getBuildingData.getDescriptionOfRisk());
							res.setSerialNo(
									StringUtil.isBlank(getBuildingData.getSerialNo()) ? "" : getBuildingData.getSerialNo());
							res.setOccupationType(getBuildingData.getOccupationType());
							// Endorsement
							res.setEndorsementDate(
									getBuildingData.getEndorsementDate() == null ? null : getBuildingData.getEndorsementDate());
							res.setEndorsementEffdate(getBuildingData.getEndorsementEffdate() == null ? null
									: getBuildingData.getEndorsementEffdate());
							res.setEndorsementRemarks(getBuildingData.getEndorsementRemarks() == null ? ""
									: getBuildingData.getEndorsementRemarks().toString());
							res.setEndorsementType(getBuildingData.getEndorsementType() == null ? ""
									: getBuildingData.getEndorsementType().toString());
							res.setEndorsementTypeDesc(getBuildingData.getEndorsementTypeDesc() == null ? ""
									: getBuildingData.getEndorsementTypeDesc().toString());
							res.setEndtCount(
									getBuildingData.getEndtCount() == null ? "" : getBuildingData.getEndtCount().toString());
							res.setEndtCategDesc(
									getBuildingData.getEndtCategDesc() == null ? "" : getBuildingData.getEndtCategDesc());
							res.setOriginalPolicyNo(getBuildingData.getOriginalPolicyNo() == null ? null
									: getBuildingData.getOriginalPolicyNo());
							res.setIsFinaceYn(
									getBuildingData.getIsFinyn() == null ? null : getBuildingData.getIsFinyn().toString());
							res.setEndtPrevPolicyNo(getBuildingData.getEndtPrevPolicyNo() == null ? ""
									: getBuildingData.getEndtPrevPolicyNo().toString());
							res.setEndtPrevQuoteNo(getBuildingData.getEndtPrevQuoteNo() == null ? ""
									: getBuildingData.getEndtPrevQuoteNo().toString());
							res.setEndtStatus(
									getBuildingData.getEndtStatus() == null ? "" : getBuildingData.getEndtStatus().toString());
							resList.add(res);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());
					return null;

				}
				return resList;
			}

			@Override
			public List<SlideEmpLiabilitySaveRes> getEmpLiabilityDetails(SlideSectionGetReq req) {
				List<SlideEmpLiabilitySaveRes> resList = new ArrayList<SlideEmpLiabilitySaveRes>();
				try {
					List<String> statusNot = new ArrayList<String>();
					statusNot.add("D");

					List<EserviceCommonDetails> humanDatas = humanRepo.findByRequestReferenceNoAndSectionIdAndStatusNotIn(
							req.getRequestReferenceNo(), req.getSectionId(), statusNot);

					for (EserviceCommonDetails humanData : humanDatas) {
						SlideEmpLiabilitySaveRes res = new SlideEmpLiabilitySaveRes();
						res.setCreatedBy(humanData.getCreatedBy());
						res.setInsuranceId(humanData.getCompanyId());
						res.setProductId(humanData.getProductId());
						res.setRequestReferenceNo(humanData.getRequestReferenceNo());
						res.setRiskId(humanData.getRiskId() == null ? "" : humanData.getRiskId().toString());
						res.setSectionId(req.getSectionId());
						res.setEmpLiabilitySi(
								humanData.getSumInsured() == null ? "" : humanData.getSumInsured().toPlainString());
						res.setLiabilityOccupationId(
								humanData.getOccupationType() == null ? "" : humanData.getOccupationType().toString());
						res.setOtherOccupation(humanData.getOtherOccupation());
						res.setTotalNoOfEmployees(
								humanData.getTotalNoOfEmployees() == null ? "" : humanData.getTotalNoOfEmployees().toString());
						// Endorsement
						res.setEndorsementDate(humanData.getEndorsementDate() == null ? null : humanData.getEndorsementDate());
						res.setEndorsementEffdate(
								humanData.getEndorsementEffdate() == null ? null : humanData.getEndorsementEffdate());
						res.setEndorsementRemarks(
								humanData.getEndorsementRemarks() == null ? "" : humanData.getEndorsementRemarks().toString());
						res.setEndorsementType(
								humanData.getEndorsementType() == null ? "" : humanData.getEndorsementType().toString());
						res.setEndorsementTypeDesc(humanData.getEndorsementTypeDesc() == null ? ""
								: humanData.getEndorsementTypeDesc().toString());
						res.setEndtCount(humanData.getEndtCount() == null ? "" : humanData.getEndtCount().toString());
						res.setEndtCategDesc(humanData.getEndtCategDesc() == null ? "" : humanData.getEndtCategDesc());
						res.setOriginalPolicyNo(
								humanData.getOriginalPolicyNo() == null ? null : humanData.getOriginalPolicyNo());
						res.setIsFinaceYn(humanData.getIsFinyn() == null ? null : humanData.getIsFinyn().toString());
						res.setEndtPrevPolicyNo(
								humanData.getEndtPrevPolicyNo() == null ? "" : humanData.getEndtPrevPolicyNo().toString());
						res.setEndtPrevQuoteNo(
								humanData.getEndtPrevQuoteNo() == null ? "" : humanData.getEndtPrevQuoteNo().toString());
						res.setEndtStatus(humanData.getEndtStatus() == null ? "" : humanData.getEndtStatus().toString());
						res.setTtdSumInsured(humanData.getTtdSumInsured());
						res.setMeSumInsured(humanData.getMeSumInsured());
						res.setFeSumInsured(humanData.getFeSumInsured());
						resList.add(res);
					}

				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());
					return null;

				}
				return resList;
			}

			@Override
			public List<SlideFidelityGuarantySaveRes> getSlideFidelityGuarantyDetails(SlideSectionGetReq req) {
				List<SlideFidelityGuarantySaveRes> resList = new ArrayList<SlideFidelityGuarantySaveRes>();
				DecimalFormat format = new DecimalFormat("##########");
				try {
					List<String> statusNot = new ArrayList<String>();
					statusNot.add("D");

					List<EserviceCommonDetails> humanDatas = humanRepo.findByRequestReferenceNoAndSectionIdAndStatusNotIn(
							req.getRequestReferenceNo(), req.getSectionId(), statusNot);

					for (EserviceCommonDetails humanData : humanDatas) {
						SlideFidelityGuarantySaveRes res = new SlideFidelityGuarantySaveRes();
						res.setCreatedBy(humanData.getCreatedBy());
						res.setInsuranceId(humanData.getCompanyId());
						res.setProductId(humanData.getProductId());
						res.setRequestReferenceNo(humanData.getRequestReferenceNo());
						res.setRiskId(humanData.getRiskId() == null ? "" : humanData.getRiskId().toString());
						res.setSectionId(req.getSectionId());
						Double si = Double.valueOf(humanData.getFidEmpSi().toString());
						res.setFidEmpSi(humanData.getFidEmpSi() == null ? "" : (new DecimalFormat("#").format(si)).toString());
						res.setLiabilityOccupationId(
								humanData.getOccupationType() == null ? "" : humanData.getOccupationType().toString());
						res.setOtherOccupation(humanData.getOtherOccupation());
						res.setFidEmpCount(humanData.getFidEmpCount() == null ? "" : humanData.getFidEmpCount().toString());
						// Endorsement
						res.setEndorsementDate(humanData.getEndorsementDate() == null ? null : humanData.getEndorsementDate());
						res.setEndorsementEffdate(
								humanData.getEndorsementEffdate() == null ? null : humanData.getEndorsementEffdate());
						res.setEndorsementRemarks(
								humanData.getEndorsementRemarks() == null ? "" : humanData.getEndorsementRemarks().toString());
						res.setEndorsementType(
								humanData.getEndorsementType() == null ? "" : humanData.getEndorsementType().toString());
						res.setEndorsementTypeDesc(humanData.getEndorsementTypeDesc() == null ? ""
								: humanData.getEndorsementTypeDesc().toString());
						res.setEndtCount(humanData.getEndtCount() == null ? "" : humanData.getEndtCount().toString());
						res.setEndtCategDesc(humanData.getEndtCategDesc() == null ? "" : humanData.getEndtCategDesc());
						res.setOriginalPolicyNo(
								humanData.getOriginalPolicyNo() == null ? null : humanData.getOriginalPolicyNo());
						res.setIsFinaceYn(humanData.getIsFinyn() == null ? null : humanData.getIsFinyn().toString());
						res.setEndtPrevPolicyNo(
								humanData.getEndtPrevPolicyNo() == null ? "" : humanData.getEndtPrevPolicyNo().toString());
						res.setEndtPrevQuoteNo(
								humanData.getEndtPrevQuoteNo() == null ? "" : humanData.getEndtPrevQuoteNo().toString());
						res.setEndtStatus(humanData.getEndtStatus() == null ? "" : humanData.getEndtStatus().toString());

						resList.add(res);
					}

				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());
					return null;

				}
				return resList;
			}

			@Override
			public SlideMachineryBreakdownSaveRes getSlideMachineryBreakdownDetails(SlideSectionGetReq req) {
				SlideMachineryBreakdownSaveRes res = new SlideMachineryBreakdownSaveRes();
				try {
					EserviceBuildingDetails getBuildingData = buildingRepo
							.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo(), 1, req.getSectionId());

					if (getBuildingData != null) {
						res.setCreatedBy(getBuildingData.getCreatedBy());
						res.setInsuranceId(getBuildingData.getCompanyId());
						res.setProductId(getBuildingData.getProductId());
						res.setRequestReferenceNo(getBuildingData.getRequestReferenceNo());
						res.setRiskId(getBuildingData.getRiskId() == null ? "" : getBuildingData.getRiskId().toString());
						res.setSectionId(req.getSectionId());
						res.setBoilerPlantsSi(getBuildingData.getBoilerPlantsSi() == null ? ""
								: getBuildingData.getBoilerPlantsSi().toPlainString());
						res.setElecMachinesSi(getBuildingData.getElecMachinesSi() == null ? ""
								: getBuildingData.getElecMachinesSi().toPlainString());
						res.setEquipmentSi(getBuildingData.getEquipmentSi() == null ? ""
								: getBuildingData.getEquipmentSi().toPlainString());
						res.setGeneralMachineSi(getBuildingData.getGeneralMachineSi() == null ? ""
								: getBuildingData.getGeneralMachineSi().toPlainString());
						res.setMachineEquipSi(getBuildingData.getMachineEquipSi() == null ? ""
								: getBuildingData.getMachineEquipSi().toPlainString());
						res.setManuUnitsSi(getBuildingData.getManuUnitsSi() == null ? ""
								: getBuildingData.getManuUnitsSi().toPlainString());
						res.setPowerPlantSi(getBuildingData.getPowerPlantSi() == null ? ""
								: getBuildingData.getPowerPlantSi().toPlainString());
						res.setMachinerySi(getBuildingData.getMachinerySi() == null ? ""
								: getBuildingData.getMachinerySi().toPlainString());

						// Endorsement
						res.setEndorsementDate(
								getBuildingData.getEndorsementDate() == null ? null : getBuildingData.getEndorsementDate());
						res.setEndorsementEffdate(getBuildingData.getEndorsementEffdate() == null ? null
								: getBuildingData.getEndorsementEffdate());
						res.setEndorsementRemarks(getBuildingData.getEndorsementRemarks() == null ? ""
								: getBuildingData.getEndorsementRemarks().toString());
						res.setEndorsementType(getBuildingData.getEndorsementType() == null ? ""
								: getBuildingData.getEndorsementType().toString());
						res.setEndorsementTypeDesc(getBuildingData.getEndorsementTypeDesc() == null ? ""
								: getBuildingData.getEndorsementTypeDesc().toString());
						res.setEndtCount(
								getBuildingData.getEndtCount() == null ? "" : getBuildingData.getEndtCount().toString());
						res.setEndtCategDesc(
								getBuildingData.getEndtCategDesc() == null ? "" : getBuildingData.getEndtCategDesc());
						res.setOriginalPolicyNo(
								getBuildingData.getOriginalPolicyNo() == null ? null : getBuildingData.getOriginalPolicyNo());
						res.setIsFinaceYn(
								getBuildingData.getIsFinyn() == null ? null : getBuildingData.getIsFinyn().toString());
						res.setEndtPrevPolicyNo(getBuildingData.getEndtPrevPolicyNo() == null ? ""
								: getBuildingData.getEndtPrevPolicyNo().toString());
						res.setEndtPrevQuoteNo(getBuildingData.getEndtPrevQuoteNo() == null ? ""
								: getBuildingData.getEndtPrevQuoteNo().toString());
						res.setEndtStatus(
								getBuildingData.getEndtStatus() == null ? "" : getBuildingData.getEndtStatus().toString());
					}
				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());
					return null;

				}
				return res;
			}


		///*
//			@Override
//			public SlideCommonSaveRes getCommonDetails(CommonGetReq req) {
//				SlideCommonSaveRes res = new SlideCommonSaveRes();
//				DozerBeanMapper dozerMapper = new DozerBeanMapper();
//				try {
//					CompanyProductMaster product = getCompanyProductMasterDropdown(req.getInsuranceId(), req.getProductId()); // productRepo.findByProductIdOrderByAmendIdDesc(Integer.valueOf(req.getProductId()));
		//
//					// Human Get
//					if (StringUtils.isNotBlank(product.getMotorYn()) && product.getMotorYn().equalsIgnoreCase("H")) {
//						List<EserviceCommonDetails> getHumanData = humanRepo
//								.findByRequestReferenceNo(req.getRequestReferenceNo());
//						if (getHumanData.size() > 0) {
//							dozerMapper.map(getHumanData.get(0), res);
//						}
		//
//						// Asset Get
//					} else {
//						EserviceBuildingDetails getBuildingData = buildingRepo
//								.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo(), 1, "0");
		//
//						dozerMapper.map(getBuildingData, res);
		//
//					}
		//
//					List<EserviceSectionDetails> sectionDetails = eserSecRepo
//							.findByRequestReferenceNoOrderByRiskIdAsc(req.getRequestReferenceNo());
//					List<String> sectionIds = new ArrayList<String>();
//					for (EserviceSectionDetails sec : sectionDetails) {
//						sectionIds.add(sec.getSectionId());
//					}
//					res.setSectionIds(sectionIds);
		//
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Log Details" + e.getMessage());
//					return null;
		//
//				}
//				return res;
//			}
		//
//			@Override
//			public AccidentDamageSaveResponse getAccidentDamgeDetails(SlideSectionGetReq req) {
//				AccidentDamageSaveResponse res = new AccidentDamageSaveResponse();
//				try {
//					EserviceBuildingDetails getBuildingData = buildingRepo
//							.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo(), 1, req.getSectionId());
		//
//					if (getBuildingData != null) {
//						res.setCreatedBy(getBuildingData.getCreatedBy());
//						res.setInsuranceId(getBuildingData.getCompanyId());
//						res.setProductId(getBuildingData.getProductId());
//						res.setRequestReferenceNo(getBuildingData.getRequestReferenceNo());
//						res.setRiskId(getBuildingData.getRiskId() == null ? "" : getBuildingData.getRiskId().toString());
//						res.setSectionId(req.getSectionId());
//						// res.setAccDamageSi(getBuildingData.getAccDamageSi()==null?"" :
//						// getBuildingData.getAccDamageSi().toPlainString());
//					}
		//
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Log Details" + e.getMessage());
//					return null;
		//
//				}
//				return res;
//			}
		//
//			@Override
//			public AllRiskDetailsRes getAllRiskDetails(SlideSectionGetReq req) {
//				AllRiskDetailsRes res = new AllRiskDetailsRes();
//				try {
//					EserviceBuildingDetails getBuildingData = buildingRepo
//							.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo(), 1, req.getSectionId());
		//
//					if (getBuildingData != null) {
//						res.setCreatedBy(getBuildingData.getCreatedBy());
//						res.setInsuranceId(getBuildingData.getCompanyId());
//						res.setProductId(getBuildingData.getProductId());
//						res.setRequestReferenceNo(getBuildingData.getRequestReferenceNo());
//						res.setRiskId(getBuildingData.getRiskId() == null ? "" : getBuildingData.getRiskId().toString());
//						res.setSectionId(req.getSectionId());
////						res.setAllriskSuminsured(getBuildingData.getAllriskSuminsured() == null ? ""
////								: getBuildingData.getAllriskSuminsured().toPlainString());
////						res.setMiningPlantSi(getBuildingData.getMiningPlantSi() == null ? ""
////								: getBuildingData.getMiningPlantSi().toPlainString());
////						res.setNonminingPlantSi(getBuildingData.getNonminingPlantSi() == null ? ""
////								: getBuildingData.getNonminingPlantSi().toPlainString());
////						res.setGensetsSi(
////								getBuildingData.getGensetsSi() == null ? "" : getBuildingData.getGensetsSi().toPlainString());
////						res.setEquipmentSi(getBuildingData.getEquipmentSi() == null ? ""
////								: getBuildingData.getEquipmentSi().toPlainString());
//						res.setEndorsementDate(
//								getBuildingData.getEndorsementDate() == null ? null : getBuildingData.getEndorsementDate());
//						res.setEndorsementEffdate(getBuildingData.getEndorsementEffdate() == null ? null
//								: getBuildingData.getEndorsementEffdate());
//						res.setEndorsementRemarks(getBuildingData.getEndorsementRemarks() == null ? ""
//								: getBuildingData.getEndorsementRemarks().toString());
//						res.setEndorsementType(getBuildingData.getEndorsementType() == null ? ""
//								: getBuildingData.getEndorsementType().toString());
//						res.setEndorsementTypeDesc(getBuildingData.getEndorsementTypeDesc() == null ? ""
//								: getBuildingData.getEndorsementTypeDesc().toString());
//						res.setEndtCount(
//								getBuildingData.getEndtCount() == null ? "" : getBuildingData.getEndtCount().toString());
//						res.setEndtCategDesc(
//								getBuildingData.getEndtCategDesc() == null ? "" : getBuildingData.getEndtCategDesc());
//						res.setOriginalPolicyNo(
//								getBuildingData.getOriginalPolicyNo() == null ? null : getBuildingData.getOriginalPolicyNo());
//						res.setIsFinaceYn(
//								getBuildingData.getIsFinyn() == null ? null : getBuildingData.getIsFinyn().toString());
//						res.setEndtPrevPolicyNo(getBuildingData.getEndtPrevPolicyNo() == null ? ""
//								: getBuildingData.getEndtPrevPolicyNo().toString());
//						res.setEndtPrevQuoteNo(getBuildingData.getEndtPrevQuoteNo() == null ? ""
//								: getBuildingData.getEndtPrevQuoteNo().toString());
//						res.setEndtStatus(
//								getBuildingData.getEndtStatus() == null ? "" : getBuildingData.getEndtStatus().toString());
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Log Details" + e.getMessage());
//					return null;
		//
//				}
//				return res;
//			}
		//
//			@Override
//			public List<BurglaryAndHouseBreakingSaveRes> getBurglaryAndHouseBreakingDetails(SlideSectionGetReq req) {
//				List<BurglaryAndHouseBreakingSaveRes> resList = new ArrayList<BurglaryAndHouseBreakingSaveRes>();
//				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
//				try {
//					List<EserviceBuildingDetails> getBuildingDatas = buildingRepo
//							.findByRequestReferenceNoAndSectionId(req.getRequestReferenceNo(), req.getSectionId());
		//
//					if (getBuildingDatas != null) {
//						for (EserviceBuildingDetails getBuildingData : getBuildingDatas) {
//							BurglaryAndHouseBreakingSaveRes res = new BurglaryAndHouseBreakingSaveRes();
//							// <ListItemValue> firstLossList =
//							// getFirstLossDropDown(getBuildingData.getCompanyId(),
//							// getBuildingData.getBranchCode() , "FIRST_LOSS_PERCENT");
//							res.setFirstLossPercentId(getBuildingData.getFirstLossPercentId() == null ? ""
//									: getBuildingData.getFirstLossPercentId().toString());
//							res.setStockLossPercent(getBuildingData.getStockLossPercent() == null ? null
//									: getBuildingData.getStockLossPercent().toString());
//							res.setGoodsLossPercent(getBuildingData.getGoodsLossPercent() == null ? null
//									: getBuildingData.getGoodsLossPercent().toString());
//							res.setFurnitureLossPercent(getBuildingData.getFurnitureLossPercent() == null ? null
//									: getBuildingData.getFurnitureLossPercent().toString());
//							res.setApplianceLossPercent(getBuildingData.getApplianceLossPercent() == null ? null
//									: getBuildingData.getApplianceLossPercent().toString());
//							res.setCashValueablesLossPercent(getBuildingData.getCashValueablesLossPercent() == null ? null
//									: getBuildingData.getCashValueablesLossPercent().toString());
////							res.setBurglarySi(getBuildingData.getBurglarySi() == null ? null
////									: getBuildingData.getBurglarySi().toPlainString());
		//
//							res.setCreatedBy(getBuildingData.getCreatedBy());
//							res.setInsuranceId(getBuildingData.getCompanyId());
//							res.setProductId(getBuildingData.getProductId());
//							res.setRequestReferenceNo(getBuildingData.getRequestReferenceNo());
//							res.setRiskId(getBuildingData.getRiskId() == null ? "" : getBuildingData.getRiskId().toString());
//							res.setSectionId(req.getSectionId());
//							res.setStockInTradeSi(getBuildingData.getStockInTradeSi() == null ? ""
//									: getBuildingData.getStockInTradeSi().toPlainString());
//							res.setGoodsSi(
//									getBuildingData.getGoodsSi() == null ? "" : getBuildingData.getGoodsSi().toPlainString());
//							res.setFurnitureSi(getBuildingData.getFurnitureSi() == null ? ""
//									: getBuildingData.getFurnitureSi().toPlainString());
//							res.setApplianceSi(getBuildingData.getApplianceSi() == null ? ""
//									: getBuildingData.getApplianceSi().toPlainString());
//							res.setCashValueablesSi(getBuildingData.getCashValueablesSi() == null ? ""
//									: getBuildingData.getCashValueablesSi().toPlainString());
//							res.setBuildingOwnerYn(
//									getBuildingData.getBuildingOwnerYn() == null ? "" : getBuildingData.getBuildingOwnerYn());
//							res.setAccessibleWindows(getBuildingData.getAccessibleWindows() == null ? ""
//									: getBuildingData.getAccessibleWindows().toString());
//							res.setAddress(getBuildingData.getAddress() == null ? "" : (getBuildingData.getAddress()));
//							res.setBackDoors(
//									getBuildingData.getBackDoors() == null ? "" : getBuildingData.getBackDoors().toString());
//							res.setBuildingOccupied(getBuildingData.getBuildingOccupied() == null ? ""
//									: getBuildingData.getBuildingOccupied().toString());
//							res.setCeilingType(getBuildingData.getCeilingType() == null ? ""
//									: getBuildingData.getCeilingType().toString());
//							res.setDistrictCode(getBuildingData.getDistrictCode() == null ? ""
//									: getBuildingData.getDistrictCode().toString());
//							res.setDoorsMaterialId(getBuildingData.getDoorsMaterialId() == null ? ""
//									: getBuildingData.getDoorsMaterialId().toString());
//							res.setFrontDoors(
//									getBuildingData.getFrontDoors() == null ? "" : getBuildingData.getFrontDoors().toString());
//							res.setInsuranceForId(getBuildingData.getInsuranceForId() != null
//									? Arrays.asList(getBuildingData.getInsuranceForId().split(","))
//									: null);
//							res.setNatureOfTradeId(getBuildingData.getNatureOfTradeId() == null ? ""
//									: getBuildingData.getNatureOfTradeId().toString());
//							res.setInternalWallType(getBuildingData.getInternalWallType() == null ? ""
//									: getBuildingData.getInternalWallType().toString());
//							res.setNightLeftDoor(getBuildingData.getNightLeftDoor() == null ? ""
//									: getBuildingData.getNightLeftDoor().toString());
//							res.setOccupiedYear(getBuildingData.getOccupiedYear() == null ? ""
//									: getBuildingData.getOccupiedYear().toString());
//							res.setShowWindow(getBuildingData.getShowWindows() == null ? ""
//									: getBuildingData.getShowWindows().toString());
//							res.setTrapDoors(
//									getBuildingData.getTrapDoors() == null ? "" : getBuildingData.getTrapDoors().toString());
//							res.setWatchmanGuardHours(getBuildingData.getWatchmanGuardHours() == null ? ""
//									: getBuildingData.getWatchmanGuardHours().toString());
//							res.setWindowsMaterialId(getBuildingData.getWindowsMaterialId() == null ? ""
//									: getBuildingData.getWindowsMaterialId().toString());
//							res.setBuildingBuildYear(getBuildingData.getBuildingBuildYear() == null ? ""
//									: getBuildingData.getBuildingBuildYear().toString());
//							res.setRoofType(
//									getBuildingData.getRoofType() == null ? "" : getBuildingData.getRoofType().toString());
//							res.setWallType(
//									getBuildingData.getWallType() == null ? "" : getBuildingData.getWallType().toString());
//							res.setRegionCode(
//									getBuildingData.getRegionCode() == null ? "" : getBuildingData.getRegionCode().toString());
//							res.setDistrictCode(
//									getBuildingData.getDistrictCode() == null ? "" : getBuildingData.getDistrictCode());
		//
//							res.setRegionName(getBuildingData.getRegionDesc() == null ? null : getBuildingData.getRegionDesc());
		//
//							res.setDistrictName(
//									getBuildingData.getDistrictDesc() == null ? null : getBuildingData.getDistrictDesc());
		//
//							res.setLocationName((getBuildingData.getLocationName() == ""
//									|| StringUtils.isBlank(getBuildingData.getCoveringDetails())) ? ""
//											: getBuildingData.getLocationName());
//							res.setCoveringDetails((getBuildingData.getCoveringDetails() == null
//									|| StringUtils.isBlank(getBuildingData.getCoveringDetails())) ? ""
//											: getBuildingData.getCoveringDetails());
//							res.setDescriptionOfRisk((getBuildingData.getDescriptionOfRisk() == null
//									|| StringUtils.isBlank(getBuildingData.getDescriptionOfRisk())) ? ""
//											: getBuildingData.getDescriptionOfRisk());
//							res.setIndustryType((getBuildingData.getCategoryId() == null
//									|| StringUtils.isBlank(getBuildingData.getCategoryId())) ? ""
//											: getBuildingData.getCategoryId());
//							res.setAddress(
//									(getBuildingData.getAddress() == null || StringUtils.isBlank(getBuildingData.getAddress()))
//											? ""
//											: getBuildingData.getAddress());
		//
//							// Endorsement
//							res.setEndorsementDate(
//									getBuildingData.getEndorsementDate() == null ? null : getBuildingData.getEndorsementDate());
//							res.setEndorsementEffdate(getBuildingData.getEndorsementEffdate() == null ? null
//									: getBuildingData.getEndorsementEffdate());
//							res.setEndorsementRemarks(getBuildingData.getEndorsementRemarks() == null ? ""
//									: getBuildingData.getEndorsementRemarks().toString());
//							res.setEndorsementType(getBuildingData.getEndorsementType() == null ? ""
//									: getBuildingData.getEndorsementType().toString());
//							res.setEndorsementTypeDesc(getBuildingData.getEndorsementTypeDesc() == null ? ""
//									: getBuildingData.getEndorsementTypeDesc().toString());
//							res.setEndtCount(
//									getBuildingData.getEndtCount() == null ? "" : getBuildingData.getEndtCount().toString());
//							res.setEndtCategDesc(
//									getBuildingData.getEndtCategDesc() == null ? "" : getBuildingData.getEndtCategDesc());
//							res.setOriginalPolicyNo(getBuildingData.getOriginalPolicyNo() == null ? null
//									: getBuildingData.getOriginalPolicyNo());
//							res.setIsFinaceYn(
//									getBuildingData.getIsFinyn() == null ? null : getBuildingData.getIsFinyn().toString());
//							res.setEndtPrevPolicyNo(getBuildingData.getEndtPrevPolicyNo() == null ? ""
//									: getBuildingData.getEndtPrevPolicyNo().toString());
//							res.setEndtPrevQuoteNo(getBuildingData.getEndtPrevQuoteNo() == null ? ""
//									: getBuildingData.getEndtPrevQuoteNo().toString());
//							res.setEndtStatus(
//									getBuildingData.getEndtStatus() == null ? "" : getBuildingData.getEndtStatus().toString());
//							resList.add(res);
//						}
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Log Details" + e.getMessage());
//					return null;
		//
//				}
//				return resList;
//			}
		//
//			@Override
//			public FireAndAlliedPerillsSaveRes getFireAndAlliedPerils(SlideSectionGetReq req) {
//				FireAndAlliedPerillsSaveRes res = new FireAndAlliedPerillsSaveRes();
//				try {
//					EserviceBuildingDetails getBuildingData = buildingRepo
//							.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo(), 1, req.getSectionId());
		//
//					if (getBuildingData != null) {
//						res.setCreatedBy(getBuildingData.getCreatedBy());
//						res.setInsuranceId(getBuildingData.getCompanyId());
//						res.setProductId(getBuildingData.getProductId());
//						res.setRequestReferenceNo(getBuildingData.getRequestReferenceNo());
//						res.setRiskId(getBuildingData.getRiskId() == null ? "" : getBuildingData.getRiskId().toString());
//						res.setSectionId(req.getSectionId());
////						res.setBuildingSuminsured(getBuildingData.getBuildingSuminsured() == null ? ""
////								: getBuildingData.getBuildingSuminsured().toPlainString());
//						res.setFirePlantSi(getBuildingData.getFirePlantSi() == null ? ""
//								: getBuildingData.getFirePlantSi().toPlainString());
//						res.setStockInTradeSi(getBuildingData.getStockInTradeSi() == null ? ""
//								: getBuildingData.getStockInTradeSi().toPlainString());
////						res.setFireEquipSi(getBuildingData.getEquipmentSi() == null ? ""
////								: getBuildingData.getEquipmentSi().toPlainString());
//						res.setIndemityPeriod(getBuildingData.getIndemityPeriod() == null ? ""
//								: getBuildingData.getIndemityPeriod().toString());
//						res.setMakutiYn(getBuildingData.getMakutiYn() == null ? "" : getBuildingData.getMakutiYn());
//						res.setOnStockSi(
//								getBuildingData.getOnStockSi() == null ? "" : getBuildingData.getOnStockSi().toPlainString());
//						res.setOnAssetsSi(
//								getBuildingData.getOnAssetsSi() == null ? "" : getBuildingData.getOnAssetsSi().toPlainString());
		//
//						// Endorsement
//						res.setEndorsementDate(
//								getBuildingData.getEndorsementDate() == null ? null : getBuildingData.getEndorsementDate());
//						res.setEndorsementEffdate(getBuildingData.getEndorsementEffdate() == null ? null
//								: getBuildingData.getEndorsementEffdate());
//						res.setEndorsementRemarks(getBuildingData.getEndorsementRemarks() == null ? ""
//								: getBuildingData.getEndorsementRemarks().toString());
//						res.setEndorsementType(getBuildingData.getEndorsementType() == null ? ""
//								: getBuildingData.getEndorsementType().toString());
//						res.setEndorsementTypeDesc(getBuildingData.getEndorsementTypeDesc() == null ? ""
//								: getBuildingData.getEndorsementTypeDesc().toString());
//						res.setEndtCount(
//								getBuildingData.getEndtCount() == null ? "" : getBuildingData.getEndtCount().toString());
//						res.setEndtCategDesc(
//								getBuildingData.getEndtCategDesc() == null ? "" : getBuildingData.getEndtCategDesc());
//						res.setOriginalPolicyNo(
//								getBuildingData.getOriginalPolicyNo() == null ? null : getBuildingData.getOriginalPolicyNo());
//						res.setIsFinaceYn(
//								getBuildingData.getIsFinyn() == null ? null : getBuildingData.getIsFinyn().toString());
//						res.setEndtPrevPolicyNo(getBuildingData.getEndtPrevPolicyNo() == null ? ""
//								: getBuildingData.getEndtPrevPolicyNo().toString());
//						res.setEndtPrevQuoteNo(getBuildingData.getEndtPrevQuoteNo() == null ? ""
//								: getBuildingData.getEndtPrevQuoteNo().toString());
//						res.setEndtStatus(
//								getBuildingData.getEndtStatus() == null ? "" : getBuildingData.getEndtStatus().toString());
		//
//					}
		//
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Log Details" + e.getMessage());
//					return null;
		//
//				}
//				return res;
//			}
		//
//			@Override
//			public ContentSaveRes getContentDetails(SlideSectionGetReq req) {
//				ContentSaveRes res = new ContentSaveRes();
//				try {
//					EserviceBuildingDetails getBuildingData = buildingRepo
//							.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo(), 1, req.getSectionId());
		//
//					if (getBuildingData != null) {
//						res.setCreatedBy(getBuildingData.getCreatedBy());
//						res.setInsuranceId(getBuildingData.getCompanyId());
//						res.setProductId(getBuildingData.getProductId());
//						res.setRequestReferenceNo(getBuildingData.getRequestReferenceNo());
//						res.setRiskId(getBuildingData.getRiskId() == null ? "" : getBuildingData.getRiskId().toString());
//						res.setSectionId(req.getSectionId());
////						res.setContentSuminsured(getBuildingData.getContentSuminsured() == null ? ""
////								: getBuildingData.getContentSuminsured().toPlainString());
////						res.setEquipmentSi(getBuildingData.getEquipmentSi() == null ? ""
////								: getBuildingData.getEquipmentSi().toPlainString());
////						res.setJewellerySi(getBuildingData.getJewellerySi() == null ? ""
////								: getBuildingData.getJewellerySi().toPlainString());
////						res.setPaitingsSi(
////								getBuildingData.getPaitingsSi() == null ? "" : getBuildingData.getPaitingsSi().toPlainString());
////						res.setCarpetsSi(
////								getBuildingData.getCarpetsSi() == null ? "" : getBuildingData.getCarpetsSi().toPlainString());
		//
//						res.setEndorsementDate(
//								getBuildingData.getEndorsementDate() == null ? null : getBuildingData.getEndorsementDate());
//						res.setEndorsementEffdate(getBuildingData.getEndorsementEffdate() == null ? null
//								: getBuildingData.getEndorsementEffdate());
//						res.setEndorsementRemarks(getBuildingData.getEndorsementRemarks() == null ? ""
//								: getBuildingData.getEndorsementRemarks().toString());
//						res.setEndorsementType(getBuildingData.getEndorsementType() == null ? ""
//								: getBuildingData.getEndorsementType().toString());
//						res.setEndorsementTypeDesc(getBuildingData.getEndorsementTypeDesc() == null ? ""
//								: getBuildingData.getEndorsementTypeDesc().toString());
//						res.setEndtCount(
//								getBuildingData.getEndtCount() == null ? "" : getBuildingData.getEndtCount().toString());
//						res.setEndtCategDesc(
//								getBuildingData.getEndtCategDesc() == null ? "" : getBuildingData.getEndtCategDesc());
//						res.setOriginalPolicyNo(
//								getBuildingData.getOriginalPolicyNo() == null ? null : getBuildingData.getOriginalPolicyNo());
//						res.setIsFinaceYn(
//								getBuildingData.getIsFinyn() == null ? null : getBuildingData.getIsFinyn().toString());
//						res.setEndtPrevPolicyNo(getBuildingData.getEndtPrevPolicyNo() == null ? ""
//								: getBuildingData.getEndtPrevPolicyNo().toString());
//						res.setEndtPrevQuoteNo(getBuildingData.getEndtPrevQuoteNo() == null ? ""
//								: getBuildingData.getEndtPrevQuoteNo().toString());
//						res.setEndtStatus(
//								getBuildingData.getEndtStatus() == null ? "" : getBuildingData.getEndtStatus().toString());
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Log Details" + e.getMessage());
//					return null;
		//
//				}
//				return res;
//			}
		//
//			@Override
//			public List<ElectronicEquipSaveRes> getElectronicEquipDetails(SlideSectionGetReq req) {
//				List<ElectronicEquipSaveRes> resList = new ArrayList<ElectronicEquipSaveRes>();
//				try {
//					List<EserviceBuildingDetails> buildingData = buildingRepo
//							.findByRequestReferenceNoAndSectionId(req.getRequestReferenceNo(), req.getSectionId());
//					List<EserviceSectionDetails> sectionDatas = eserSecRepo
//							.findByRequestReferenceNoOrderByRiskIdAsc(req.getRequestReferenceNo());
		//
//					if (buildingData != null) {
//						for (EserviceBuildingDetails getBuildingData : buildingData) {
//							ElectronicEquipSaveRes res = new ElectronicEquipSaveRes();
//							res.setCreatedBy(getBuildingData.getCreatedBy());
//							res.setInsuranceId(getBuildingData.getCompanyId());
//							res.setProductId(getBuildingData.getProductId());
//							res.setRequestReferenceNo(getBuildingData.getRequestReferenceNo());
//							res.setRiskId(getBuildingData.getRiskId() == null ? "" : getBuildingData.getRiskId().toString());
//							res.setSectionId("42".equalsIgnoreCase(getBuildingData.getProductId()) && sectionDatas.size() > 0
//									? sectionDatas.get(0).getSectionId()
//									: req.getSectionId());
////							res.setElecEquipSuminsured(getBuildingData.getElecEquipSuminsured() == null ? ""
////									: getBuildingData.getElecEquipSuminsured().toPlainString());
//							res.setContentId(
//									StringUtil.isBlank(getBuildingData.getContentId()) ? "" : getBuildingData.getContentId());
//							res.setContentDesc(StringUtil.isBlank(getBuildingData.getContentDesc()) ? ""
//									: getBuildingData.getContentDesc());
//							res.setLocationName(StringUtil.isBlank(getBuildingData.getLocationName()) ? ""
//									: getBuildingData.getLocationName());
//							res.setDescription(StringUtil.isBlank(getBuildingData.getDescriptionOfRisk()) ? ""
//									: getBuildingData.getDescriptionOfRisk());
//							res.setSerialNo(
//									StringUtil.isBlank(getBuildingData.getSerialNo()) ? "" : getBuildingData.getSerialNo());
//							res.setOccupationType(getBuildingData.getOccupationType());
//							// Endorsement
//							res.setEndorsementDate(
//									getBuildingData.getEndorsementDate() == null ? null : getBuildingData.getEndorsementDate());
//							res.setEndorsementEffdate(getBuildingData.getEndorsementEffdate() == null ? null
//									: getBuildingData.getEndorsementEffdate());
//							res.setEndorsementRemarks(getBuildingData.getEndorsementRemarks() == null ? ""
//									: getBuildingData.getEndorsementRemarks().toString());
//							res.setEndorsementType(getBuildingData.getEndorsementType() == null ? ""
//									: getBuildingData.getEndorsementType().toString());
//							res.setEndorsementTypeDesc(getBuildingData.getEndorsementTypeDesc() == null ? ""
//									: getBuildingData.getEndorsementTypeDesc().toString());
//							res.setEndtCount(
//									getBuildingData.getEndtCount() == null ? "" : getBuildingData.getEndtCount().toString());
//							res.setEndtCategDesc(
//									getBuildingData.getEndtCategDesc() == null ? "" : getBuildingData.getEndtCategDesc());
//							res.setOriginalPolicyNo(getBuildingData.getOriginalPolicyNo() == null ? null
//									: getBuildingData.getOriginalPolicyNo());
//							res.setIsFinaceYn(
//									getBuildingData.getIsFinyn() == null ? null : getBuildingData.getIsFinyn().toString());
//							res.setEndtPrevPolicyNo(getBuildingData.getEndtPrevPolicyNo() == null ? ""
//									: getBuildingData.getEndtPrevPolicyNo().toString());
//							res.setEndtPrevQuoteNo(getBuildingData.getEndtPrevQuoteNo() == null ? ""
//									: getBuildingData.getEndtPrevQuoteNo().toString());
//							res.setEndtStatus(
//									getBuildingData.getEndtStatus() == null ? "" : getBuildingData.getEndtStatus().toString());
//							resList.add(res);
//						}
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Log Details" + e.getMessage());
//					return null;
		//
//				}
//				return resList;
//			}
		//
//			@Override
//			public List<SlideEmpLiabilitySaveRes> getEmpLiabilityDetails(SlideSectionGetReq req) {
//				List<SlideEmpLiabilitySaveRes> resList = new ArrayList<SlideEmpLiabilitySaveRes>();
//				try {
//					List<String> statusNot = new ArrayList<String>();
//					statusNot.add("D");
		//
//					List<EserviceCommonDetails> humanDatas = humanRepo.findByRequestReferenceNoAndSectionIdAndStatusNotIn(
//							req.getRequestReferenceNo(), req.getSectionId(), statusNot);
		//
//					for (EserviceCommonDetails humanData : humanDatas) {
//						SlideEmpLiabilitySaveRes res = new SlideEmpLiabilitySaveRes();
//						res.setCreatedBy(humanData.getCreatedBy());
//						res.setInsuranceId(humanData.getCompanyId());
//						res.setProductId(humanData.getProductId());
//						res.setRequestReferenceNo(humanData.getRequestReferenceNo());
//						res.setRiskId(humanData.getRiskId() == null ? "" : humanData.getRiskId().toString());
//						res.setSectionId(req.getSectionId());
//						res.setEmpLiabilitySi(
//								humanData.getSumInsured() == null ? "" : humanData.getSumInsured().toPlainString());
//						res.setLiabilityOccupationId(
//								humanData.getOccupationType() == null ? "" : humanData.getOccupationType().toString());
//						res.setOtherOccupation(humanData.getOtherOccupation());
//						res.setTotalNoOfEmployees(
//								humanData.getTotalNoOfEmployees() == null ? "" : humanData.getTotalNoOfEmployees().toString());
//						// Endorsement
//						res.setEndorsementDate(humanData.getEndorsementDate() == null ? null : humanData.getEndorsementDate());
//						res.setEndorsementEffdate(
//								humanData.getEndorsementEffdate() == null ? null : humanData.getEndorsementEffdate());
//						res.setEndorsementRemarks(
//								humanData.getEndorsementRemarks() == null ? "" : humanData.getEndorsementRemarks().toString());
//						res.setEndorsementType(
//								humanData.getEndorsementType() == null ? "" : humanData.getEndorsementType().toString());
//						res.setEndorsementTypeDesc(humanData.getEndorsementTypeDesc() == null ? ""
//								: humanData.getEndorsementTypeDesc().toString());
//						res.setEndtCount(humanData.getEndtCount() == null ? "" : humanData.getEndtCount().toString());
//						res.setEndtCategDesc(humanData.getEndtCategDesc() == null ? "" : humanData.getEndtCategDesc());
//						res.setOriginalPolicyNo(
//								humanData.getOriginalPolicyNo() == null ? null : humanData.getOriginalPolicyNo());
//						res.setIsFinaceYn(humanData.getIsFinyn() == null ? null : humanData.getIsFinyn().toString());
//						res.setEndtPrevPolicyNo(
//								humanData.getEndtPrevPolicyNo() == null ? "" : humanData.getEndtPrevPolicyNo().toString());
//						res.setEndtPrevQuoteNo(
//								humanData.getEndtPrevQuoteNo() == null ? "" : humanData.getEndtPrevQuoteNo().toString());
//						res.setEndtStatus(humanData.getEndtStatus() == null ? "" : humanData.getEndtStatus().toString());
////						res.setTtdSumInsured(humanData.getTtdSumInsured());
////						res.setMeSumInsured(humanData.getMeSumInsured());
////						res.setFeSumInsured(humanData.getFeSumInsured());
//						resList.add(res);
//					}
		//
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Log Details" + e.getMessage());
//					return null;
		//
//				}
//				return resList;
//			}
		//
//			@Override
//			public List<SlideFidelityGuarantySaveRes> getSlideFidelityGuarantyDetails(SlideSectionGetReq req) {
//				List<SlideFidelityGuarantySaveRes> resList = new ArrayList<SlideFidelityGuarantySaveRes>();
//				DecimalFormat format = new DecimalFormat("##########");
//				try {
//					List<String> statusNot = new ArrayList<String>();
//					statusNot.add("D");
		//
//					List<EserviceCommonDetails> humanDatas = humanRepo.findByRequestReferenceNoAndSectionIdAndStatusNotIn(
//							req.getRequestReferenceNo(), req.getSectionId(), statusNot);
		//
//					for (EserviceCommonDetails humanData : humanDatas) {
//						SlideFidelityGuarantySaveRes res = new SlideFidelityGuarantySaveRes();
//						res.setCreatedBy(humanData.getCreatedBy());
//						res.setInsuranceId(humanData.getCompanyId());
//						res.setProductId(humanData.getProductId());
//						res.setRequestReferenceNo(humanData.getRequestReferenceNo());
//						res.setRiskId(humanData.getRiskId() == null ? "" : humanData.getRiskId().toString());
//						res.setSectionId(req.getSectionId());
////						Double si = Double.valueOf(humanData.getFidEmpSi().toString());
////						res.setFidEmpSi(humanData.getFidEmpSi() == null ? "" : (new DecimalFormat("#").format(si)).toString());
//						Double si = Double.valueOf(humanData.getSumInsured().toString());
//						res.setSumInsured(humanData.getSumInsured() == null ? new BigDecimal(0) : new BigDecimal(si));
//						res.setLiabilityOccupationId(
//								humanData.getOccupationType() == null ? "" : humanData.getOccupationType().toString());
//						res.setOtherOccupation(humanData.getOtherOccupation());
//						res.setFidEmpCount(humanData.getFidEmpCount() == null ? "" : humanData.getFidEmpCount().toString());
//						// Endorsement
//						res.setEndorsementDate(humanData.getEndorsementDate() == null ? null : humanData.getEndorsementDate());
//						res.setEndorsementEffdate(
//								humanData.getEndorsementEffdate() == null ? null : humanData.getEndorsementEffdate());
//						res.setEndorsementRemarks(
//								humanData.getEndorsementRemarks() == null ? "" : humanData.getEndorsementRemarks().toString());
//						res.setEndorsementType(
//								humanData.getEndorsementType() == null ? "" : humanData.getEndorsementType().toString());
//						res.setEndorsementTypeDesc(humanData.getEndorsementTypeDesc() == null ? ""
//								: humanData.getEndorsementTypeDesc().toString());
//						res.setEndtCount(humanData.getEndtCount() == null ? "" : humanData.getEndtCount().toString());
//						res.setEndtCategDesc(humanData.getEndtCategDesc() == null ? "" : humanData.getEndtCategDesc());
//						res.setOriginalPolicyNo(
//								humanData.getOriginalPolicyNo() == null ? null : humanData.getOriginalPolicyNo());
//						res.setIsFinaceYn(humanData.getIsFinyn() == null ? null : humanData.getIsFinyn().toString());
//						res.setEndtPrevPolicyNo(
//								humanData.getEndtPrevPolicyNo() == null ? "" : humanData.getEndtPrevPolicyNo().toString());
//						res.setEndtPrevQuoteNo(
//								humanData.getEndtPrevQuoteNo() == null ? "" : humanData.getEndtPrevQuoteNo().toString());
//						res.setEndtStatus(humanData.getEndtStatus() == null ? "" : humanData.getEndtStatus().toString());
		//
//						resList.add(res);
//					}
		//
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Log Details" + e.getMessage());
//					return null;
		//
//				}
//				return resList;
//			}
		//
//			@Override
//			public SlideMachineryBreakdownSaveRes getSlideMachineryBreakdownDetails(SlideSectionGetReq req) {
//				SlideMachineryBreakdownSaveRes res = new SlideMachineryBreakdownSaveRes();
//				try {
//					EserviceBuildingDetails getBuildingData = buildingRepo
//							.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo(), 1, req.getSectionId());
		//
//					if (getBuildingData != null) {
//						res.setCreatedBy(getBuildingData.getCreatedBy());
//						res.setInsuranceId(getBuildingData.getCompanyId());
//						res.setProductId(getBuildingData.getProductId());
//						res.setRequestReferenceNo(getBuildingData.getRequestReferenceNo());
//						res.setRiskId(getBuildingData.getRiskId() == null ? "" : getBuildingData.getRiskId().toString());
//						res.setSectionId(req.getSectionId());
////						res.setBoilerPlantsSi(getBuildingData.getBoilerPlantsSi() == null ? ""
////								: getBuildingData.getBoilerPlantsSi().toPlainString());
		///*				res.setElecMachinesSi(getBuildingData.getElecMachinesSi() == null ? ""
//								: getBuildingData.getElecMachinesSi().toPlainString());
//						res.setEquipmentSi(getBuildingData.getEquipmentSi() == null ? ""
//								: getBuildingData.getEquipmentSi().toPlainString());
//						res.setGeneralMachineSi(getBuildingData.getGeneralMachineSi() == null ? ""
//								: getBuildingData.getGeneralMachineSi().toPlainString());
//						res.setMachineEquipSi(getBuildingData.getMachineEquipSi() == null ? ""
//								: getBuildingData.getMachineEquipSi().toPlainString());
//						res.setManuUnitsSi(getBuildingData.getManuUnitsSi() == null ? ""
//								: getBuildingData.getManuUnitsSi().toPlainString());
//						res.setPowerPlantSi(getBuildingData.getPowerPlantSi() == null ? ""
//								: getBuildingData.getPowerPlantSi().toPlainString());
//						res.setMachinerySi(getBuildingData.getMachinerySi() == null ? ""
//								: getBuildingData.getMachinerySi().toPlainString());
		//*/
//						// Endorsement
//						res.setEndorsementDate(
//								getBuildingData.getEndorsementDate() == null ? null : getBuildingData.getEndorsementDate());
//						res.setEndorsementEffdate(getBuildingData.getEndorsementEffdate() == null ? null
//								: getBuildingData.getEndorsementEffdate());
//						res.setEndorsementRemarks(getBuildingData.getEndorsementRemarks() == null ? ""
//								: getBuildingData.getEndorsementRemarks().toString());
//						res.setEndorsementType(getBuildingData.getEndorsementType() == null ? ""
//								: getBuildingData.getEndorsementType().toString());
//						res.setEndorsementTypeDesc(getBuildingData.getEndorsementTypeDesc() == null ? ""
//								: getBuildingData.getEndorsementTypeDesc().toString());
//						res.setEndtCount(
//								getBuildingData.getEndtCount() == null ? "" : getBuildingData.getEndtCount().toString());
//						res.setEndtCategDesc(
//								getBuildingData.getEndtCategDesc() == null ? "" : getBuildingData.getEndtCategDesc());
//						res.setOriginalPolicyNo(
//								getBuildingData.getOriginalPolicyNo() == null ? null : getBuildingData.getOriginalPolicyNo());
//						res.setIsFinaceYn(
//								getBuildingData.getIsFinyn() == null ? null : getBuildingData.getIsFinyn().toString());
//						res.setEndtPrevPolicyNo(getBuildingData.getEndtPrevPolicyNo() == null ? ""
//								: getBuildingData.getEndtPrevPolicyNo().toString());
//						res.setEndtPrevQuoteNo(getBuildingData.getEndtPrevQuoteNo() == null ? ""
//								: getBuildingData.getEndtPrevQuoteNo().toString());
//						res.setEndtStatus(
//								getBuildingData.getEndtStatus() == null ? "" : getBuildingData.getEndtStatus().toString());
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Log Details" + e.getMessage());
//					return null;
		//
//				}
//				return res;
//			}
		/*

			@Override
			public List<SlideMoneySaveRes> getSlideMoneyDetails(SlideSectionGetReq req) {
				List<SlideMoneySaveRes> resList = new ArrayList<SlideMoneySaveRes>();
				try {
					List<EserviceBuildingDetails> getBuildingList = buildingRepo
							.findByRequestReferenceNoAndSectionId(req.getRequestReferenceNo(), req.getSectionId());

					if (getBuildingList != null) {
						for (EserviceBuildingDetails getBuildingData : getBuildingList) {
							SlideMoneySaveRes res = new SlideMoneySaveRes();
							res.setCreatedBy(getBuildingData.getCreatedBy());
							res.setInsuranceId(getBuildingData.getCompanyId());
							res.setProductId(getBuildingData.getProductId());
							res.setRequestReferenceNo(getBuildingData.getRequestReferenceNo());
							res.setRiskId(getBuildingData.getRiskId() == null ? "" : getBuildingData.getRiskId().toString());
							res.setSectionId(req.getSectionId());
							res.setMoneyAnnualEstimate(getBuildingData.getMoneyAnnualEstimate() == null ? "0"
									: getBuildingData.getMoneyAnnualEstimate().toPlainString());
							res.setMoneyCollector(getBuildingData.getMoneyCollector() == null ? "0"
									: getBuildingData.getMoneyCollector().toPlainString());
							res.setMoneyDirectorResidence(getBuildingData.getMoneyDirectorResidence() == null ? "0"
									: getBuildingData.getMoneyDirectorResidence().toPlainString());
							res.setMoneyOutofSafe(getBuildingData.getMoneyOutofSafe() == null ? "0"
									: getBuildingData.getMoneyOutofSafe().toPlainString());
							res.setMoneySafeLimit(getBuildingData.getMoneySafeLimit() == null ? "0"
									: getBuildingData.getMoneySafeLimit().toPlainString());
							res.setMoneyMajorLoss(getBuildingData.getMoneyMajorLoss() == null ? "0"
									: getBuildingData.getMoneyMajorLoss().toPlainString());
							res.setStrongroomSi(getBuildingData.getStrongroomSi() == null ? "0"
									: getBuildingData.getStrongroomSi().toPlainString());

							// Endorsement
							res.setEndorsementDate(
									getBuildingData.getEndorsementDate() == null ? null : getBuildingData.getEndorsementDate());
							res.setEndorsementEffdate(getBuildingData.getEndorsementEffdate() == null ? null
									: getBuildingData.getEndorsementEffdate());
							res.setEndorsementRemarks(getBuildingData.getEndorsementRemarks() == null ? ""
									: getBuildingData.getEndorsementRemarks().toString());
							res.setEndorsementType(getBuildingData.getEndorsementType() == null ? ""
									: getBuildingData.getEndorsementType().toString());
							res.setEndorsementTypeDesc(getBuildingData.getEndorsementTypeDesc() == null ? ""
									: getBuildingData.getEndorsementTypeDesc().toString());
							res.setEndtCount(
									getBuildingData.getEndtCount() == null ? "" : getBuildingData.getEndtCount().toString());
							res.setEndtCategDesc(
									getBuildingData.getEndtCategDesc() == null ? "" : getBuildingData.getEndtCategDesc());
							res.setOriginalPolicyNo(getBuildingData.getOriginalPolicyNo() == null ? null
									: getBuildingData.getOriginalPolicyNo());
							res.setIsFinaceYn(
									getBuildingData.getIsFinyn() == null ? null : getBuildingData.getIsFinyn().toString());
							res.setEndtPrevPolicyNo(getBuildingData.getEndtPrevPolicyNo() == null ? ""
									: getBuildingData.getEndtPrevPolicyNo().toString());
							res.setEndtPrevQuoteNo(getBuildingData.getEndtPrevQuoteNo() == null ? ""
									: getBuildingData.getEndtPrevQuoteNo().toString());
							res.setEndtStatus(
									getBuildingData.getEndtStatus() == null ? "" : getBuildingData.getEndtStatus().toString());
							res.setLocationName(
									getBuildingData.getLocationName() == null ? "" : getBuildingData.getLocationName());
							res.setAddress(getBuildingData.getAddress() == null ? "" : getBuildingData.getAddress());
							res.setRegionCode(getBuildingData.getRegionCode() == null ? "" : getBuildingData.getRegionCode());
							res.setRegionDesc(getBuildingData.getRegionDesc() == null ? "" : getBuildingData.getRegionDesc());
							res.setDistrictCode(
									getBuildingData.getDistrictCode() == null ? "" : getBuildingData.getDistrictCode());
							res.setDistrictDesc(
									getBuildingData.getDistrictDesc() == null ? "" : getBuildingData.getDistrictDesc());
							resList.add(res);
						}

					}

				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());
					return null;

				}
				return resList;
			}

			@Override
			public SlidePlateGlassSaveRes getSlidePlateGlassDetails(SlideSectionGetReq req) {
				SlidePlateGlassSaveRes res = new SlidePlateGlassSaveRes();
				try {
					EserviceBuildingDetails getBuildingData = buildingRepo
							.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo(), 1, req.getSectionId());

					if (getBuildingData != null) {
						res.setCreatedBy(getBuildingData.getCreatedBy());
						res.setInsuranceId(getBuildingData.getCompanyId());
						res.setProductId(getBuildingData.getProductId());
						res.setRequestReferenceNo(getBuildingData.getRequestReferenceNo());
						res.setRiskId(getBuildingData.getRiskId() == null ? "" : getBuildingData.getRiskId().toString());
						res.setSectionId(req.getSectionId());
//						res.setPlateGlassSi(getBuildingData.getPlateGlassSi() == null ? ""
//								: getBuildingData.getPlateGlassSi().toPlainString());
						res.setPlateGlassType(getBuildingData.getPlateGlassType());
					}
				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());
					return null;

				}
				return res;
			}

			@Override
			public SlidePublicLiabilitySaveRes getSlidePublicLiablityDetails(SlideSectionGetReq req) {
				SlidePublicLiabilitySaveRes res = new SlidePublicLiabilitySaveRes();
				try {
					List<EserviceCommonDetails> humanDatas = humanRepo
							.findByRequestReferenceNoAndSectionId(req.getRequestReferenceNo(), req.getSectionId());
					String pattern = "######";
					DecimalFormat df = new DecimalFormat(pattern);

					if (humanDatas.size() > 0) {
						EserviceCommonDetails humanData = humanDatas.get(0);
						res.setCreatedBy(humanData.getCreatedBy());
						res.setInsuranceId(humanData.getCompanyId());
						res.setProductId(humanData.getProductId());
						res.setRequestReferenceNo(humanData.getRequestReferenceNo());
						res.setRiskId(humanData.getRiskId() == null ? "" : humanData.getRiskId().toString());
						res.setSectionId(req.getSectionId());
						res.setLiabilitySi(humanData.getLiabilitySi() == null ? ""
								: df.format(Double.valueOf(humanData.getLiabilitySi().toPlainString())));
//						res.setAooSumInsured(humanData.getAooSuminsured() == null ? ""
//								: df.format(Double.valueOf(humanData.getAooSuminsured().toPlainString())));
//						res.setAggSumInsured(humanData.getAggSuminsured() == null ? ""
//								: df.format(Double.valueOf(humanData.getAggSuminsured().toPlainString())));
						res.setProductTurnoverSi(humanData.getProductTurnoverSi() == null ? ""
								: df.format(Double.valueOf(humanData.getProductTurnoverSi().toPlainString())));
						res.setCategory(humanData.getCategoryId());
//						res.setAnyAccidentSi(humanData.getAnyAccidentSi() == null ? ""
//								: df.format(Double.valueOf(humanData.getAnyAccidentSi().toPlainString())));
//						res.setAnyAccidentSiLc(humanData.getAnyAccidentSiLc() == null ? ""
//								: df.format(Double.valueOf(humanData.getAnyAccidentSiLc().toPlainString())));
//						res.setInsurancePeriodSi(humanData.getInsurancePeriodSi() == null ? ""
//								: df.format(Double.valueOf(humanData.getInsurancePeriodSi().toPlainString())));
//						res.setInsurancePeriodSiLc(humanData.getInsurancePeriodSiLc() == null ? ""
//								: df.format(Double.valueOf(humanData.getInsurancePeriodSiLc().toPlainString())));
						res.setSumInsured(humanData.getSumInsured() == null ? ""
								: df.format(Double.valueOf(humanData.getSumInsured().toPlainString())));
						
					}
								
					} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());
					return null;

				}
				return res;
			}

			@Override
			public SuccessRes deleteAccidentDamgeDetails(SlideSectionGetReq req) {
				SuccessRes res = new SuccessRes();
				try {
					EserviceBuildingDetails getBuildingData = buildingRepo
							.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo(), 1, req.getSectionId());

					// Update Eservice
					if (getBuildingData != null) {
						// getBuildingData.setAccDamageSi(new BigDecimal(0));
						buildingRepo.delete(getBuildingData);

					}

					// Delete Section
					List<EserviceSectionDetails> section = eserSecRepo
							.findByRequestReferenceNoAndSectionId(req.getRequestReferenceNo(), req.getSectionId());
					if (section != null) {
						eserSecRepo.deleteAll(section);
					}

					res.setResponse("Deleted Successfully");
					res.setSuccessId(req.getSectionId());

				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());
					return null;

				}
				return res;
			}

			@Override
			public SuccessRes deleteAllRiskDetails(SlideSectionGetReq req) {
				SuccessRes res = new SuccessRes();
				try {
					EserviceBuildingDetails getBuildingData = buildingRepo
							.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo(), 1, req.getSectionId());

					// Update Eservice
					if (getBuildingData != null) {
						// getBuildingData.setAccDamageSi(new BigDecimal(0));
						buildingRepo.delete(getBuildingData);

					}

					// Delete Section
					List<EserviceSectionDetails> section = eserSecRepo
							.findByRequestReferenceNoAndSectionId(req.getRequestReferenceNo(), req.getSectionId());
					if (section != null) {
						eserSecRepo.deleteAll(section);
					}

					res.setResponse("Deleted Successfully");
					res.setSuccessId(req.getSectionId());

				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());
					return null;

				}
				return res;
			}

			@Override
			public SuccessRes deleteBurglaryAndHouseBreakingDetails(SlideSectionGetReq req) {
				SuccessRes res = new SuccessRes();
				try {
					EserviceBuildingDetails getBuildingData = buildingRepo
							.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo(), 1, req.getSectionId());

					// Update Eservice
					if (getBuildingData != null) {
						// getBuildingData.setAccDamageSi(new BigDecimal(0));
						buildingRepo.delete(getBuildingData);
					}

					// Delete Section
					List<EserviceSectionDetails> section = eserSecRepo
							.findByRequestReferenceNoAndSectionId(req.getRequestReferenceNo(), req.getSectionId());
					if (section != null) {
						eserSecRepo.deleteAll(section);
					}

					res.setResponse("Deleted Successfully");
					res.setSuccessId(req.getSectionId());

				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());
					return null;

				}
				return res;
			}

			@Override
			public SuccessRes deleteFireAndAlliedPerils(SlideSectionGetReq req) {
				SuccessRes res = new SuccessRes();
				try {

					EserviceBuildingDetails getBuildingData = buildingRepo
							.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo(), 1, req.getSectionId());

					// Update Eservice
					if (getBuildingData != null) {
						// getBuildingData.setAccDamageSi(new BigDecimal(0));
						buildingRepo.delete(getBuildingData);

					}

					// Delete Section
					List<EserviceSectionDetails> section = eserSecRepo
							.findByRequestReferenceNoAndSectionId(req.getRequestReferenceNo(), req.getSectionId());
					if (section != null) {
						eserSecRepo.deleteAll(section);
					}

					res.setResponse("Deleted Successfully");
					res.setSuccessId(req.getSectionId());

				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());
					return null;

				}
				return res;
			}

			@Override
			public SuccessRes deleteContentDetails(SlideSectionGetReq req) {
				SuccessRes res = new SuccessRes();
				try {
					EserviceBuildingDetails getBuildingData = buildingRepo
							.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo(), 1, req.getSectionId());

					// Update Eservice
					if (getBuildingData != null) {
						// getBuildingData.setAccDamageSi(new BigDecimal(0));
						buildingRepo.delete(getBuildingData);

					}

					// Delete Section
					List<EserviceSectionDetails> section = eserSecRepo
							.findByRequestReferenceNoAndSectionId(req.getRequestReferenceNo(), req.getSectionId());
					if (section != null) {
						eserSecRepo.deleteAll(section);
					}

					res.setResponse("Deleted Successfully");
					res.setSuccessId(req.getSectionId());

				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());
					return null;

				}
				return res;
			}

			@Override
			public SuccessRes deleteElectronicEquipDetails(SlideSectionGetReq req) {
				SuccessRes res = new SuccessRes();
				try {
					EserviceBuildingDetails getBuildingData = buildingRepo
							.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo(), 1, req.getSectionId());

					// Update Eservice
					if (getBuildingData != null) {
						// getBuildingData.setAccDamageSi(new BigDecimal(0));
						buildingRepo.delete(getBuildingData);

					}

					// Delete Section
					List<EserviceSectionDetails> section = eserSecRepo
							.findByRequestReferenceNoAndSectionId(req.getRequestReferenceNo(), req.getSectionId());
					if (section != null) {
						eserSecRepo.deleteAll(section);
					}

					res.setResponse("Deleted Successfully");
					res.setSuccessId(req.getSectionId());

				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());
					return null;

				}
				return res;
			}

			@Override
			public SuccessRes deleteEmpLiabilityDetails(SlideSectionGetReq req) {
				SuccessRes res = new SuccessRes();
				try {
					List<EserviceCommonDetails> getHumanDatas = humanRepo
							.findByRequestReferenceNoAndSectionId(req.getRequestReferenceNo(), req.getSectionId());

					// Update Eservice
					if (getHumanDatas.size() > 0) {
						humanRepo.deleteAll(getHumanDatas);
					}

					// Delete Section
					List<EserviceSectionDetails> section = eserSecRepo
							.findByRequestReferenceNoAndSectionId(req.getRequestReferenceNo(), req.getSectionId());
					if (section != null) {
						eserSecRepo.deleteAll(section);
					}

					res.setResponse("Deleted Successfully");
					res.setSuccessId(req.getSectionId());

				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());
					return null;

				}
				return res;
			}

			@Override
			public SuccessRes deleteSlideFidelityGuarantyDetails(SlideSectionGetReq req) {
				SuccessRes res = new SuccessRes();
				try {
					List<EserviceCommonDetails> getHumanDatas = humanRepo
							.findByRequestReferenceNoAndSectionId(req.getRequestReferenceNo(), req.getSectionId());

					// Update Eservice
					if (getHumanDatas.size() > 0) {
						humanRepo.deleteAll(getHumanDatas);
					}

					// Delete Section
					List<EserviceSectionDetails> section = eserSecRepo
							.findByRequestReferenceNoAndSectionId(req.getRequestReferenceNo(), req.getSectionId());
					if (section != null) {
						eserSecRepo.deleteAll(section);
					}

					res.setResponse("Deleted Successfully");
					res.setSuccessId(req.getSectionId());

				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());
					return null;

				}
				return res;
			}

			@Override
			public SuccessRes deleteSlideMoneyDetails(SlideSectionGetReq req) {
				SuccessRes res = new SuccessRes();
				try {
					EserviceBuildingDetails getBuildingData = buildingRepo
							.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo(), 1, req.getSectionId());

					// Update Eservice
					if (getBuildingData != null) {
						// getBuildingData.setAccDamageSi(new BigDecimal(0));
						buildingRepo.delete(getBuildingData);

					}

					// Delete Section
					List<EserviceSectionDetails> section = eserSecRepo
							.findByRequestReferenceNoAndSectionId(req.getRequestReferenceNo(), req.getSectionId());
					if (section != null) {
						eserSecRepo.deleteAll(section);
					}

					res.setResponse("Deleted Successfully");
					res.setSuccessId(req.getSectionId());

				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());
					return null;

				}
				return res;
			}

			@Override
			public SuccessRes deleteSlideMachineryBreakdownDetails(SlideSectionGetReq req) {
				SuccessRes res = new SuccessRes();
				try {
					EserviceBuildingDetails getBuildingData = buildingRepo
							.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo(), 1, req.getSectionId());

					// Update Eservice
					if (getBuildingData != null) {
						// getBuildingData.setAccDamageSi(new BigDecimal(0));
						buildingRepo.delete(getBuildingData);

					}

					// Delete Section
					List<EserviceSectionDetails> section = eserSecRepo
							.findByRequestReferenceNoAndSectionId(req.getRequestReferenceNo(), req.getSectionId());
					if (section != null) {
						eserSecRepo.deleteAll(section);
					}

					res.setResponse("Deleted Successfully");
					res.setSuccessId(req.getSectionId());

				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());
					return null;

				}
				return res;
			}

			@Override
			public SuccessRes deleteSlidePlateGlassDetails(SlideSectionGetReq req) {
				SuccessRes res = new SuccessRes();
				try {
					EserviceBuildingDetails getBuildingData = buildingRepo
							.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo(), 1, req.getSectionId());

					// Update Eservice
					if (getBuildingData != null) {
						// getBuildingData.setAccDamageSi(new BigDecimal(0));
						buildingRepo.delete(getBuildingData);

					}

					// Delete Section
					List<EserviceSectionDetails> section = eserSecRepo
							.findByRequestReferenceNoAndSectionId(req.getRequestReferenceNo(), req.getSectionId());
					if (section != null) {
						eserSecRepo.deleteAll(section);
					}

					res.setResponse("Deleted Successfully");
					res.setSuccessId(req.getSectionId());

				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());
					return null;

				}
				return res;
			}
		*/
//			@Override
//			public SuccessRes deleteSlidePublicLiablityDetails(SlideSectionGetReq req) {
//				SuccessRes res = new SuccessRes();
//				try {
//					List<EserviceCommonDetails> getHumanDatas = humanRepo
//							.findByRequestReferenceNoAndSectionId(req.getRequestReferenceNo(), req.getSectionId());
		//
//					// Update Eservice
//					if (getHumanDatas.size() > 0) {
//						humanRepo.deleteAll(getHumanDatas);
//					}
		//
//					// Delete Section
//					List<EserviceSectionDetails> section = eserSecRepo
//							.findByRequestReferenceNoAndSectionId(req.getRequestReferenceNo(), req.getSectionId());
//					if (section != null) {
//						eserSecRepo.deleteAll(section);
//					}
		//
//					res.setResponse("Deleted Successfully");
//					res.setSuccessId(req.getSectionId());
		//
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Log Details" + e.getMessage());
//					return null;
		//
//				}
//				return res;
//			}

//			@Override
//			public List<DropDownRes> getAooDropdown(MedMalDropDownReq req) {
//				List<DropDownRes> resList = new ArrayList<DropDownRes>();
//				try {
//					Date today = new Date();
//					Calendar cal = new GregorianCalendar();
//					cal.setTime(today);
//					cal.set(Calendar.HOUR_OF_DAY, 23);
//					;
//					cal.set(Calendar.MINUTE, 1);
//					today = cal.getTime();
//					cal.set(Calendar.HOUR_OF_DAY, 1);
//					cal.set(Calendar.MINUTE, 1);
//					Date todayEnd = cal.getTime();
		//
//					// Criteria
//					CriteriaBuilder cb = em.getCriteriaBuilder();
//					CriteriaQuery<MedMalLiabilityMaster> query = cb.createQuery(MedMalLiabilityMaster.class);
//					List<MedMalLiabilityMaster> list = new ArrayList<MedMalLiabilityMaster>();
//					// Find All
//					Root<MedMalLiabilityMaster> c = query.from(MedMalLiabilityMaster.class);
//					// Select
//					query.select(c);
//					// Order By
//					List<Order> orderList = new ArrayList<Order>();
//					orderList.add(cb.asc(c.get("aoo")));
		//
//					// Effective Date Start Max Filter
//					Subquery<Date> effectiveDate = query.subquery(Date.class);
//					Root<MedMalLiabilityMaster> ocpm1 = effectiveDate.from(MedMalLiabilityMaster.class);
//					effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
//					Predicate a1 = cb.equal(c.get("sNo"), ocpm1.get("sNo"));
//					Predicate a2 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
//					Predicate a3 = cb.equal(c.get("companyId"), ocpm1.get("companyId"));
////					Predicate a4 = cb.equal(c.get("branchCode"), ocpm1.get("branchCode"));
//					effectiveDate.where(a1, a2, a3);
//					// Effective Date End Max Filter
//					Subquery<Date> effectiveDate2 = query.subquery(Date.class);
//					Root<MedMalLiabilityMaster> ocpm2 = effectiveDate2.from(MedMalLiabilityMaster.class);
//					effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
//					Predicate a6 = cb.equal(c.get("sNo"), ocpm2.get("sNo"));
//					Predicate a7 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
//					Predicate a8 = cb.equal(c.get("companyId"), ocpm2.get("companyId"));
////					Predicate a9 = cb.equal(c.get("branchCode"), ocpm2.get("branchCode"));
		//
//					effectiveDate2.where(a6, a7, a8);
//					// Where
//					Predicate n1 = cb.equal(c.get("status"), "Y");
//					Predicate n2 = cb.equal(c.get("effectiveDateStart"), effectiveDate);
//					Predicate n3 = cb.equal(c.get("effectiveDateEnd"), effectiveDate2);
//					Predicate n4 = cb.equal(c.get("companyId"), req.getInsuranceId());
////					Predicate n5 = cb.equal(c.get("branchCode"), req.getBranchCode());
////					Predicate n6 = cb.equal(c.get("branchCode"), "99999");
////					Predicate n7 = cb.or(n5,n6);
//					query.where(n1, n2, n3, n4).orderBy(orderList);
//					// Get Result
//					TypedQuery<MedMalLiabilityMaster> result = em.createQuery(query);
//					list = result.getResultList();
//					list = list.stream().filter(distinctByKey(o -> Arrays.asList(o.getAooId()))).collect(Collectors.toList());
//					list.sort(Comparator.comparing(MedMalLiabilityMaster::getAoo));
//					for (MedMalLiabilityMaster data : list) {
//						// Response
//						DropDownRes res = new DropDownRes();
//						res.setCode(data.getAoo().toString());
//						res.setCodeDesc(data.getAoo().toString());
//						res.setStatus(data.getStatus());
//						resList.add(res);
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Exception is --->" + e.getMessage());
//					return null;
//				}
//				return resList;
//			}

//			@Override
//			public List<DropDownRes> getAggDropdown(MedMalDropDownReq req) {
//				List<DropDownRes> resList = new ArrayList<DropDownRes>();
//				try {
//					Date today = new Date();
//					Calendar cal = new GregorianCalendar();
//					cal.setTime(today);
//					cal.set(Calendar.HOUR_OF_DAY, 23);
//					;
//					cal.set(Calendar.MINUTE, 1);
//					today = cal.getTime();
//					cal.set(Calendar.HOUR_OF_DAY, 1);
//					cal.set(Calendar.MINUTE, 1);
//					Date todayEnd = cal.getTime();
		//
//					// Criteria
//					CriteriaBuilder cb = em.getCriteriaBuilder();
//					CriteriaQuery<MedMalLiabilityMaster> query = cb.createQuery(MedMalLiabilityMaster.class);
//					List<MedMalLiabilityMaster> list = new ArrayList<MedMalLiabilityMaster>();
//					// Find All
//					Root<MedMalLiabilityMaster> c = query.from(MedMalLiabilityMaster.class);
//					// Select
//					query.select(c);
//					// Order By
//					List<Order> orderList = new ArrayList<Order>();
//					orderList.add(cb.asc(c.get("agg")));
		//
//					// Effective Date Start Max Filter
//					Subquery<Date> effectiveDate = query.subquery(Date.class);
//					Root<MedMalLiabilityMaster> ocpm1 = effectiveDate.from(MedMalLiabilityMaster.class);
//					effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
//					Predicate a1 = cb.equal(c.get("sNo"), ocpm1.get("sNo"));
//					Predicate a2 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
//					Predicate a3 = cb.equal(c.get("companyId"), ocpm1.get("companyId"));
////					Predicate a4 = cb.equal(c.get("branchCode"), ocpm1.get("branchCode"));
//					effectiveDate.where(a1, a2, a3);
//					// Effective Date End Max Filter
//					Subquery<Date> effectiveDate2 = query.subquery(Date.class);
//					Root<MedMalLiabilityMaster> ocpm2 = effectiveDate2.from(MedMalLiabilityMaster.class);
//					effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
//					Predicate a6 = cb.equal(c.get("sNo"), ocpm2.get("sNo"));
//					Predicate a7 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
//					Predicate a8 = cb.equal(c.get("companyId"), ocpm2.get("companyId"));
////					Predicate a9 = cb.equal(c.get("branchCode"), ocpm2.get("branchCode"));
		//
//					effectiveDate2.where(a6, a7, a8);
//					// Where
//					Predicate n1 = cb.equal(c.get("status"), "Y");
//					Predicate n2 = cb.equal(c.get("effectiveDateStart"), effectiveDate);
//					Predicate n3 = cb.equal(c.get("effectiveDateEnd"), effectiveDate2);
//					Predicate n4 = cb.equal(c.get("companyId"), req.getInsuranceId());
//					Predicate n8 = cb.equal(c.get("aoo"), req.getAoo());
////					Predicate n5 = cb.equal(c.get("branchCode"), req.getBranchCode());
////					Predicate n6 = cb.equal(c.get("branchCode"), "99999");
////					Predicate n7 = cb.or(n5,n6);
//					query.where(n1, n2, n3, n4, n8).orderBy(orderList);
//					// Get Result
//					TypedQuery<MedMalLiabilityMaster> result = em.createQuery(query);
//					list = result.getResultList();
////					list = list.stream().filter(distinctByKey(o -> Arrays.asList(o.getAooId()))).collect(Collectors.toList());
////					list.sort(Comparator.comparing(MedMalLiabilityMaster :: getAgg ));
//					for (MedMalLiabilityMaster data : list) {
//						// Response
//						DropDownRes res = new DropDownRes();
//						res.setCode(data.getAgg().toString());
//						res.setCodeDesc(data.getAgg().toString());
//						res.setStatus(data.getStatus());
//						resList.add(res);
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Exception is --->" + e.getMessage());
//					return null;
//				}
//				return resList;
//			}

		/*	public CommonRes deletefire(List<FireDelete> Req) {

				CommonRes res = new CommonRes();
				int count = 0;
				try {

					if (!Req.isEmpty()) {

						for (FireDelete data : Req) {

							EserviceBuildingDetails da = buildingRepo.findByRequestReferenceNoAndRiskIdAndSectionId(
									data.getRequestReferenceNo(), Integer.valueOf(data.getRiskId()), data.getSectionId());
							EserviceSectionDetails d1 = secRepo.findByRequestReferenceNoAndRiskIdAndProductIdAndSectionId(
									data.getRequestReferenceNo(), Integer.valueOf(data.getRiskId()), "6", data.getSectionId());
							if (d1 != null) {
								secRepo.delete(d1);
							}
							if (da != null && da.getSectionId() != "0")
								buildingRepo.delete(da);
							count++;

						}

						// }

						if (count > 0) {

							res.setMessage("Deleted Fire Records Successfully..");
							res.setIsError(false);

						} else {
							res.setMessage("Deleted Fire Records Failed..");
							res.setIsError(true);
						}
					}
				} catch (Exception dd) {
					System.out.println("**************************Exception in fire delete****************************");
					dd.printStackTrace();
					res.setMessage("Deleted Fire Records Failed..");
					res.setIsError(true);
				}
				return res;

			}
		*/

//			}


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

			/*public boolean saveSectiondetails(FireReq req) {
				try {

					BigDecimal exchangerate = new BigDecimal(req.getExchangeRate());
					EserviceSectionDetails save = new EserviceSectionDetails();
					List<ProductSectionMaster> sectiondetails = productmaster
							.findBySectionIdOrderByAmendIdDesc(Integer.valueOf(req.getSectionId()));
					List<CompanyProductMaster> productdetails = productRepo.findByCompanyIdAndProductIdOrderByAmendIdDesc(
							req.getInsuranceId(), Integer.valueOf(req.getProductId()));
					List<InsuranceCompanyMaster> companydetials = companyRepo
							.findByCompanyIdOrderByAmendIdDesc(req.getInsuranceId());
					EserviceSectionDetails sectionrecords = null;

					Date entryDate = new Date();
					Date periodStart = req.getPolicyStartDate();
					Date periodEnd = req.getPolicyEndDate();
					String diff = "0";

					if (periodStart != null && periodEnd != null) {
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
						String st = sdf.format(req.getPolicyStartDate());
						String ed = sdf.format(req.getPolicyEndDate());
						if (st.equalsIgnoreCase(ed) && (req.getEndorsementType() == null || req.getEndorsementType() == " 0")) {
							diff = "1";
						} else if (st.equalsIgnoreCase(ed)) {
							diff = "0";
						} else {
							Long diffInMillies = Math.abs(periodEnd.getTime() - periodStart.getTime());
							Long daysBetween = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS) + 1;
							diff = String.valueOf(daysBetween);
						}

					}
					{

						// delete
						{
							EserviceSectionDetails d1 = secRepo.findByRequestReferenceNoAndRiskIdAndProductIdAndSectionId(
									req.getRequestReferenceNo(), Integer.valueOf(req.getRiskId()), req.getProductId(),
									req.getSectionId());
							/*
							 * List<EserviceSectionDetails> d1 =
							 * secRepo.findByRequestReferenceNoAndRiskIdAndProductId(
							 * req.getRequestReferenceNo(), Integer.valueOf(req.getRiskId()),
							 * req.getProductId());
							 */

				/*			if (d1 != null) {
								secRepo.delete(d1);
							}
						}
						// sectiondetails save
						save.setRequestReferenceNo(req.getRequestReferenceNo());
						save.setRiskId(Integer.valueOf(req.getRiskId()));
						save.setCustomerReferenceNo(req.getCustomerReferenceNo());
						save.setProductId(req.getProductId());
						save.setProductDesc(productdetails.get(0).getProductName());
						save.setCurrencyId(req.getCurrency());
						save.setExchangeRate(exchangerate);
						save.setSectionId(req.getSectionId());
						save.setSectionName(sectiondetails.get(0).getSectionName());
						save.setLocationId(1);
						save.setEntryDate(entryDate);
						save.setUpdatedDate(entryDate);
						if (req.getProductType() != null && req.getProductType().equals("A")) {
							save.setProductType("A");
							save.setProductDesc("Assest");

						}
						if (!companydetials.isEmpty()) {
							save.setCompanyName(companydetials.get(0).getCompanyName());
						}
						save.setCreatedBy(req.getCreatedBy());
						save.setStatus("Y");
						save.setUserOpt("N");
						save.setCustomerId(req.getCustomerCode());
						save.setCompanyId(req.getInsuranceId());

						secRepo.save(save);
					}

					// default entry
					EserviceBuildingDetails data = new EserviceBuildingDetails();
					List<EserviceBuildingDetails> Existingrecords = buildingRepo
							.findByRequestReferenceNoAndSectionId(req.getRequestReferenceNo(), "0");

					{

						EserviceBuildingDetails Existingraw = buildingRepo.findByRequestReferenceNoAndRiskIdAndSectionId(
								req.getRequestReferenceNo(), Integer.valueOf(req.getRiskId()), req.getSectionId());
						// delete raw records
						if (Existingraw != null) {
							buildingRepo.delete(Existingraw);
						}

						// save raw table Assest

						data = new DozerBeanMapper().map(req, EserviceBuildingDetails.class);
						{
							// BrokerDetils

							List<ListItemValue> sourcerTypes = premiaBrokerService.getSourceTypeDropdown(req.getInsuranceId(),
									req.getBranchCode(), "SOURCE_TYPE");
							List<ListItemValue> filterSource = sourcerTypes.stream()
									.filter(o -> StringUtils.isNotBlank(req.getSourceTypeId())
											&& (o.getItemCode().equalsIgnoreCase(req.getSourceTypeId())
													|| o.getItemValue().equalsIgnoreCase(req.getSourceTypeId())))
									.collect(Collectors.toList());
							BranchMaster branchData = getBranchMasterRes(req.getInsuranceId(), req.getBranchCode());
							List<String> directSource = new ArrayList<String>();
							directSource.add("1");
							directSource.add("2");
							directSource.add("3");

							if (filterSource.size() > 0 && directSource.contains(filterSource.get(0).getItemCode())) {
								String sourceType = filterSource.get(0).getItemValue();
								String subUserType = sourceType.contains("Broker") ? "Broker"
										: sourceType.contains("Agent") ? "Agent" : "";
								LoginMaster premiaLogin = getPremiaBroker(req.getBdmCode(), subUserType, req.getInsuranceId());
								String brokerLoginId = premiaLogin != null ? premiaLogin.getLoginId()
										: branchData.getDirectBrokerId();

								LoginUserInfo premiaUser = loginUserRepo.findByLoginId(brokerLoginId);
								LoginUserInfo loginUserData = premiaUser != null ? premiaUser
										: loginUserRepo.findByLoginId(branchData.getDirectBrokerId());

								LoginBranchMaster premiaBranch = lbranchRepo.findByLoginIdAndBranchCodeAndCompanyId(
										brokerLoginId, req.getBranchCode(), req.getInsuranceId());
								LoginBranchMaster brokerBranch = premiaBranch != null ? premiaBranch
										: lbranchRepo.findByLoginIdAndBranchCodeAndCompanyId(branchData.getDirectBrokerId(),
												req.getBranchCode(), req.getInsuranceId());

								data.setBranchCode(req.getBranchCode());
								data.setBrokerCode(loginUserData.getOaCode());
								data.setBranchName(branchData.getBranchName());
								data.setApplicationId(
										StringUtils.isBlank(req.getApplicationId()) ? "1" : req.getApplicationId());

								data.setAgencyCode(loginUserData.getAgencyCode());
								data.setLoginId(loginUserData.getLoginId());
								data.setCustomerCode(req.getBdmCode());
								data.setCustomerName(req.getCustomerName());
								data.setBdmCode(req.getBdmCode());
								data.setBrokerBranchCode(brokerBranch.getBrokerBranchCode());
								data.setBrokerBranchName(brokerBranch.getBrokerBranchName());
								data.setSourceTypeId(filterSource.get(0).getItemCode());
								data.setSourceType(filterSource.get(0).getItemValue());
								data.setLocationId(1);
								// Direct Source Type
								if (filterSource.get(0).getItemValue().contains("Direct")
										|| (premiaLogin != null && premiaUser != null && premiaBranch != null)) {
									data.setSalePointCode(brokerBranch.getSalePointCode());
									data.setBrokerTiraCode(loginUserData.getRegulatoryCode());
								} else {
									try {
										// Broker Tira Code
										PremiaTiraReq brokerTiraCodeReq = new PremiaTiraReq();
										brokerTiraCodeReq.setInsuranceId(req.getInsuranceId());
										brokerTiraCodeReq.setPremiaCode(req.getCustomerCode());
										List<PremiaTiraRes> brokerTira = premiaBrokerService
												.searchPremiaBrokerTiraCode(brokerTiraCodeReq);
										String brokerTiraCode = "";
										if (brokerTira.size() > 0) {
											brokerTiraCode = brokerTira.get(0).getTiraCode();
										}

										// Sale Point Code
										PremiaTiraReq brokerSpCodeReq = new PremiaTiraReq();
										brokerSpCodeReq.setInsuranceId(req.getInsuranceId());
										brokerSpCodeReq.setPremiaCode(brokerTiraCode);
										List<PremiaTiraRes> brokerSp = premiaBrokerService
												.searchPremiaBrokerSpCode(brokerSpCodeReq);
										String brokerSpCode = "";
										if (brokerSp.size() > 0) {
											brokerSpCode = brokerSp.get(0).getTiraCode();
										}

										data.setSalePointCode(brokerSpCode);
										data.setBrokerTiraCode(brokerTiraCode);
									} catch (Exception e) {
										e.printStackTrace();
										log.info("Log Details" + e.getMessage());

									}
								}

							} else {
								LoginUserInfo loginUserData = loginUserRepo.findByLoginId(req.getCreatedBy());
								LoginBranchMaster brokerBranch = lbranchRepo.findByLoginIdAndBrokerBranchCodeAndCompanyId(
										req.getCreatedBy(), req.getBrokerbranchCode(), req.getInsuranceId());
								LoginMaster loginData = loginRepo.findByLoginId(req.getCreatedBy());

								data.setBrokerCode(loginData.getOaCode());
								data.setAgencyCode(loginData.getAgencyCode());
								data.setApplicationId(
										StringUtils.isBlank(req.getApplicationId()) ? "1" : req.getApplicationId());
								data.setLocationId(1);
								data.setBranchName(branchData.getBranchName());
								data.setCustomerCode(loginUserData.getCustomerCode());
								data.setLoginId(req.getCreatedBy());
								data.setCustomerName(loginUserData.getCustomerName());
								data.setBdmCode(null);
								data.setBrokerBranchCode(brokerBranch.getBrokerBranchCode());
								data.setBrokerBranchName(brokerBranch.getBrokerBranchName());
								data.setSalePointCode(brokerBranch.getSalePointCode());
								data.setBrokerTiraCode(loginUserData.getRegulatoryCode());
								List<ListItemValue> filterBrokerSource = sourcerTypes.stream()
										.filter(o -> o.getItemValue().equalsIgnoreCase(loginData.getSubUserType()))
										.collect(Collectors.toList());
								data.setSourceTypeId(
										filterBrokerSource.size() > 0 ? filterBrokerSource.get(0).getItemCode() : "");
								data.setSourceType(filterBrokerSource.size() > 0 ? filterBrokerSource.get(0).getItemValue()
										: loginData.getSubUserType());

							}
							if ("1".equalsIgnoreCase(req.getApplicationId())) {
								data.setSubUserType(data.getSourceType());
							} else {
								LoginMaster issuerData = loginRepo.findByLoginId(req.getApplicationId());
								data.setSubUserType(issuerData != null ? issuerData.getSubUserType() : data.getSourceType());
							}
							data.setSubUserType(data.getSourceType());
							Double commissionPercent = 0D;
							if (filterSource.size() > 0 && directSource.contains(filterSource.get(0).getItemCode())) {
								// commissionPercent=12.5;
								String sourceType = filterSource.get(0).getItemValue();
								String subUserType = sourceType.contains("Broker") ? "Broker"
										: sourceType.contains("Agent") ? "Agent" : "";
								LoginMaster premiaLogin = getPremiaBroker(req.getBdmCode(), subUserType, req.getInsuranceId());
								String premiaLoginId = premiaLogin != null ? premiaLogin.getLoginId() : "";

								String commission = getListItem(req.getInsuranceId(), req.getBranchCode(), "COMMISSION_PERCENT",
										filterSource.get(0).getItemValue());
								if (StringUtils.isNotBlank(premiaLoginId)) {
									List<BrokerCommissionDetails> commissionList = getPolicyName(req.getInsuranceId(),
											req.getProductId(), premiaLoginId, req.getBrokerCode(), "99999", req.getUserType());
									commission = commissionList.size() > 0
											&& commissionList.get(0).getCommissionPercentage() != null
													? commissionList.get(0).getCommissionPercentage().toString()
													: commission;
								}
								commissionPercent = StringUtils.isNotBlank(commission) ? Double.valueOf(commission) : 0D;
								data.setCommissionPercentage(
										commissionPercent == null ? new BigDecimal("0") : new BigDecimal(commissionPercent));

							} else {
								String loginId = StringUtils.isNotBlank(req.getSourceTypeId())
										&& req.getSourceTypeId().toLowerCase().contains("b2c") ? "guest" : req.getCreatedBy();
								List<BrokerCommissionDetails> commissionList = getPolicyName(req.getInsuranceId(),
										req.getProductId(), loginId, req.getBrokerCode(), "99999", req.getUserType());
								if (commissionList != null && commissionList.size() > 0) {
									BrokerCommissionDetails comm = commissionList.get(0);
									data.setCommissionPercentage(comm.getCommissionPercentage() == null ? new BigDecimal("0")
											: new BigDecimal(comm.getCommissionPercentage()));
									data.setVatCommission(comm.getCommissionVatPercent() == null ? new BigDecimal("0")
											: new BigDecimal(comm.getCommissionVatPercent()));
								} else {
									data.setCommissionPercentage(new BigDecimal("0"));
									data.setVatCommission(new BigDecimal("0"));
								}
							}
							// Endt Commission
							if (!(req.getEndorsementType() == null))

							{
								List<BuildingRiskDetails> mainMot = motBuildingRepo.findByQuoteNo(req.getEndtPrevQuoteNo());
								for (BuildingRiskDetails mot : mainMot) {
									data.setCommissionPercentage(
											mot.getCommissionPercentage() == null ? data.getCommissionPercentage()
													: mot.getCommissionPercentage());
									data.setVatCommission(
											mot.getVatCommission() == null ? data.getVatCommission() : mot.getVatCommission());
								}

							}

							data.setTiraCoverNoteNo(req.getTiraCoverNoteNo());
							data.setBankCode(req.getBankCode());
							data.setAcExecutiveId(StringUtils.isBlank(req.getAcExecutiveId()) ? null
									: Integer.valueOf(req.getAcExecutiveId()));
							if (StringUtils.isNotBlank(req.getCommissionType())) {
								String commistionDesc = getListItem(req.getInsuranceId(), req.getBranchCode(),
										"COMMISSION_TYPE", req.getCommissionType());
								data.setCommissionTypeDesc(commistionDesc);
							}

						}
						data.setPolicyPeriord(Integer.valueOf(diff));
						data.setEntryDate(entryDate);
						data.setUpdatedDate(entryDate);
						data.setDomesticPackageYn("Y");
						data.setUpdatedBy(req.getCreatedBy());

						data.setSectionId(req.getSectionId());
						data.setRiskId(Integer.valueOf(req.getRiskId()));
						data.setProductId(req.getProductId());
						data.setProductDesc(productdetails.get(0).getProductName());
						if (!sectiondetails.isEmpty()) {
							data.setSectionDesc(sectiondetails.get(0).getSectionName());
						}
						data.setCompanyId(req.getInsuranceId());
						if (!companydetials.isEmpty()) {
							data.setCompanyName(companydetials.get(0).getCompanyName());
						}
						data.setExchangeRate(exchangerate);

						data.setPolicyStartDate(req.getPolicyStartDate());
						data.setPolicyEndDate(req.getPolicyEndDate());
						data.setCurrency(req.getCurrency());
						data.setProductDesc(productdetails.get(0).getProductDesc());

						data.setCustomerReferenceNo(req.getCustomerReferenceNo());
						data.setRequestReferenceNo(req.getRequestReferenceNo());

						data.setCreatedBy(req.getCreatedBy());
						data.setPromocode(req.getPromoCode());
						// data.setCommissionType(req.getCommissionType());

						if (Existingrecords.isEmpty()) {
							EserviceBuildingDetails data0 = new EserviceBuildingDetails();
							data0 = new DozerBeanMapper().map(data, EserviceBuildingDetails.class);
							data0.setSectionId("0");
							data0.setSectionDesc("Default Entry");
							data0.setRiskId(1);
							buildingRepo.save(data0);
						}
						buildingRepo.save(data);
					}

				} catch (Exception dd) {
					System.out.println("***************Exception in Eservice SEction Details Block***************");

					dd.printStackTrace();
					return false;

				}
				return true;

			}

			public boolean DeleteFire(CommonRequest req) {
				try {
					List<EserviceBuildingDetails> building = buildingRepo.findByRequestReferenceNo(req.getRequestReferenceNo());
					building = building.stream().filter(a -> a.getSectionId() != "0").collect(Collectors.toList());
					List<EserviceSectionDetails> sectionData = secRepo.findByRequestReferenceNo(req.getRequestReferenceNo());

					if (!building.isEmpty()) {
						buildingRepo.deleteAll(building);
					}
					if (!sectionData.isEmpty()) {
						secRepo.deleteAll(sectionData);
					}

				} catch (Exception cc) {
					cc.printStackTrace();
					return false;
				}
				return true;
			}

			public SlideSectionSaveRes saveFire(FireReq req) {

				EserviceBuildingDetails Existingrecords = null;
				EserviceBuildingDetails newrecords = new EserviceBuildingDetails();
				SlideSectionSaveRes res = new SlideSectionSaveRes();
				// List<EserviceSectionDetails> sectionData = null;
				EserviceSectionDetails sectionData = null;
				try {
					// save existing records

					// sectionData =
					// secRepo.findByRequestReferenceNoAndSectionId(req.getRequestReferenceNo(),
					// req.getSectionId());
					sectionData = secRepo.findByRequestReferenceNoAndSectionIdAndRiskId(req.getRequestReferenceNo(),
							req.getSectionId(), Integer.valueOf(req.getRiskId()));
					Existingrecords = buildingRepo.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo(),
							Integer.valueOf(req.getRiskId()), req.getSectionId());
					newrecords = new DozerBeanMapper().map(Existingrecords, EserviceBuildingDetails.class);

					// delete block
					{
						List<Integer> riskId = new ArrayList<>();
						riskId.add(Integer.valueOf(req.getRiskId()));
						EserviceBuildingDetails Existing = buildingRepo.findByRequestReferenceNoAndRiskIdAndSectionId(
								req.getRequestReferenceNo(), Integer.valueOf(req.getRiskId()), req.getSectionId());

						/*
						 * List<EserviceBuildingDetails> Existing =
						 * buildingRepo.findByRequestReferenceNoAndRiskId( req.getRequestReferenceNo(),
						 * Integer.valueOf(req.getRiskId())); for (EserviceBuildingDetails dd :
						 * Existing) {
						 */
		/*				if (!Existing.getSectionId().equals("0") && !Existing.getRiskId().equals("1")) {
							buildingRepo.delete(Existing);
						}
						// }
						// if(Existing!=null) {buildingRepo.deleteAll(Existing);}
						/// if(Existing1!=null) {buildingRepo.delete(Existing1);}

					}

					// save block
					// saveData =
					// buildingRepo.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo(),1,
					// req.getSectionId());
//					newrecords.setBuildingSuminsured(
//							StringUtils.isNotBlank(String.valueOf(req.getBuildingSumInsured())) ? req.getBuildingSumInsured()
//									: new BigDecimal("0"));
					newrecords.setRiskId(Integer.valueOf(req.getRiskId()));
					newrecords.setIndustryId(
							StringUtils.isBlank(req.getIndustryType()) ? null : Integer.valueOf(req.getIndustryType()));
					IndustryMaster industrydel = industryrepo.findByIndustryIdAndCompanyIdAndProductId(
							Integer.valueOf(req.getIndustryType()), req.getInsuranceId(), req.getProductId());
					newrecords.setIndustryDesc(industrydel.getIndustryName());
					newrecords.setCategoryId(req.getOccupationId());
					if (StringUtils.isNotBlank(req.getOccupationId())) {
						OccupationMaster occupationData = getOccupationMasterDropdown(req.getInsuranceId(), "99999",
								req.getProductId(), req.getOccupationId());
						newrecords.setCategoryDesc(occupationData != null ? occupationData.getOccupationName() : null);
					}

					newrecords.setAddress(req.getLocationName());
					newrecords.setCoveringDetails(req.getCoveringDetails());
					newrecords.setDescriptionOfRisk(req.getDescriptionOfRisk());
					newrecords.setBusinessInterruption(req.getBusinessInterruption());
					newrecords.setCategoryId(req.getOccupationId());
					newrecords.setLocationId(1);
					newrecords.setIndustryId(Integer.valueOf(req.getIndustryType()));

					newrecords.setEntryDate(new Date());
					newrecords.setUpdatedDate(new Date());
					if (req.getRegionCode() != null) {
						List<RegionMaster> data = regionrepo.findByRegionCode(req.getRegionCode());
						newrecords.setRegionDesc(!data.isEmpty() && StringUtils.isBlank(data.get(0).getRegionName()) ? null
								: data.get(0).getRegionName());
					}

					if (req.getDistrictCode() != null) {
						List<StateMaster> statedata = staterepo.findByStateId(Integer.valueOf(req.getDistrictCode()));
						newrecords.setDistrictDesc(
								!statedata.isEmpty() && StringUtils.isBlank(statedata.get(0).getStateName()) ? null
										: statedata.get(0).getStateName());
					}

					if (newrecords.getExchangeRate() != null) {
						BigDecimal exRate = newrecords.getExchangeRate();

//						if (StringUtils.isNotBlank(String.valueOf(req.getBuildingSumInsured()))) {
//							newrecords.setBuildingSuminsuredLc(req.getBuildingSumInsured().multiply(exRate));
//						} else {
//							newrecords.setBuildingSuminsuredLc(BigDecimal.ZERO);
//						}
					}

					if (!(req.getEndorsementType() == null || req.getEndorsementType() == "0"))

					{

						newrecords.setOriginalPolicyNo(req.getOriginalPolicyNo());
						newrecords.setEndorsementDate(req.getEndorsementDate());
						newrecords.setEndorsementRemarks(req.getEndorsementRemarks());
						newrecords.setEndorsementEffdate(req.getEndorsementEffdate());
						newrecords.setEndtPrevPolicyNo(req.getEndtPrevPolicyNo());
						newrecords.setEndtPrevQuoteNo(req.getEndtPrevQuoteNo());
						newrecords.setEndtCount(req.getEndtCount());
						newrecords.setEndtStatus(req.getEndtStatus());

						/* newrecords.setIsFinyn(req.getIsFinaceYn()); */
		/*				newrecords.setEndtCategDesc(req.getEndtCategDesc());
						newrecords.setEndorsementType(Integer.valueOf(req.getEndorsementType()));
						newrecords.setEndorsementTypeDesc(req.getEndorsementTypeDesc());

					}

					newrecords.setStatus(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus());
					buildingRepo.saveAndFlush(newrecords);

					// one time insert

					try {
						List<OneTimeTableRes> otResList = null;
						// Section Req
						List<BuildingSectionRes> sectionList = new ArrayList<BuildingSectionRes>();
						BuildingSectionRes sec = new BuildingSectionRes();
						sec.setMotorYn(sectionData.getProductType());
						sec.setSectionId(sectionData.getSectionId());
						sec.setSectionName(sectionData.getSectionName());
						sectionList.add(sec);

						// One Time Table Thread Call
						OneTimeTableReq otReq = new OneTimeTableReq();
						otReq.setRequestReferenceNo(newrecords.getRequestReferenceNo());
						otReq.setVehicleId(newrecords.getRiskId());
						otReq.setBranchCode(newrecords.getBranchCode());
						otReq.setInsuranceId(newrecords.getCompanyId());
						otReq.setProductId(Integer.valueOf(newrecords.getProductId()));
						otReq.setSectionList(sectionList);
						otReq.setAgencyCode(newrecords.getAgencyCode());
						otReq.setLocationId(newrecords.getLocationId());
						otResList = otService.call_OT_Insert(otReq);
						for (OneTimeTableRes otRes : otResList) {

							res.setResponse("Saved Successfully");
							res.setRiskId(req.getRiskId());
							res.setVdRefNo(otRes.getVdRefNo());
							res.setLocationId(otRes.getLocationId());
							res.setCdRefNo(otRes.getCdRefNo());
							res.setMsrefno(otRes.getMsRefNo());
							res.setCompanyId(otRes.getCompanyId());
							res.setProductId(otRes.getProductId());
							res.setSectionId(otRes.getSectionId());
							res.setCustomerReferenceNo(req.getCustomerReferenceNo());
							res.setRequestReferenceNo(req.getRequestReferenceNo());
							res.setCreatedBy(req.getCreatedBy());

						}

					} catch (Exception e) {
						e.printStackTrace();
						return null;
					}

				} catch (Exception Ex) {
					System.out.println("Fire Product  Exception ");
					Ex.printStackTrace();
					log.info("Log Details" + Ex.getMessage());
					return null;
				}

				return res;
			}

			public List<BondRes> getBoundDetails(SlideSectionGetReq req) {
				List<BondRes> resList = new ArrayList<>();
				List<EserviceBuildingDetails> getlist = null;
				List<EserviceBuildingDetails> getall = null;
				try {
					getlist = buildingRepo.findByRequestReferenceNoAndSectionIdAndRiskId(req.getRequestReferenceNo(),
							req.getSectionId(), Integer.valueOf(req.getRiskId()));
					if (!getlist.isEmpty()) {
						BondRes data = new BondRes();
						data.setRequestReferenceNo(getlist.get(0).getRequestReferenceNo());
						data.setRiskId(getlist.get(0).getRiskId());
						data.setProductId(getlist.get(0).getProductId());
						data.setInsuranceId(getlist.get(0).getCompanyId());
						data.setCreatedBy(getlist.get(0).getCreatedBy());
//						data.setBondSumInsured(getlist.get(0).getBondSuminsured());
						data.setBondType(getlist.get(0).getBondType());
						data.setBondYear(getlist.get(0).getBondYear());
						resList.add(data);
					} else {
						return resList;
					}

				} catch (Exception cc) {
					System.out.println("Get Bound Details Issue :");
					cc.printStackTrace();
					return null;
				}
				return resList;
			}

			public List<SlideSectionSaveRes> saveBond(BondCommonReq req) {
				List<EserviceBuildingDetails> saveDataList = new ArrayList<>();
				EserviceBuildingDetails Existingrecords = null;
				EserviceBuildingDetails saveData = null;
				EserviceSectionDetails sectionData = null;
				List<BuildingSectionRes> sectionList = new ArrayList<>();
				List<SlideSectionSaveRes> resList = new ArrayList<SlideSectionSaveRes>();

				try {
					for (BondMajorReq bondDetails : req.getBondDetails()) {
						saveData = buildingRepo.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo(), 1,
								bondDetails.getBondType());

						sectionData = eserSecRepo.findByRequestReferenceNoAndRiskIdAndProductIdAndSectionId(
								req.getRequestReferenceNo(), 1, req.getProductId(), bondDetails.getBondType());

						if (saveData != null) {
							// deleted
							Existingrecords = buildingRepo.findByRequestReferenceNoAndRiskIdAndSectionId(
									req.getRequestReferenceNo(), bondDetails.getRiskId(), bondDetails.getBondType());
							if (Existingrecords != null) {
								buildingRepo.delete(Existingrecords);
							}

							// insert
							EserviceBuildingDetails newrecords = new EserviceBuildingDetails();
							newrecords = new DozerBeanMapper().map(saveData, EserviceBuildingDetails.class);
							newrecords.setBondType(bondDetails.getBondType());
							newrecords.setBondYear(bondDetails.getBondYear());
//							newrecords.setBondSuminsured(
//									StringUtils.isBlank(bondDetails.getBondSumInsured()) ? new BigDecimal("0")
//											: new BigDecimal(bondDetails.getBondSumInsured()));
							newrecords.setRiskId(bondDetails.getRiskId());
							newrecords.setIndustryDesc(travelProductId);
							if (!(req.getEndorsementType() == null || req.getEndorsementType() == 0))

							{

								newrecords.setOriginalPolicyNo(req.getOriginalPolicyNo());
								newrecords.setEndorsementDate(req.getEndorsementDate());
								newrecords.setEndorsementRemarks(req.getEndorsementRemarks());
								newrecords.setEndorsementEffdate(req.getEndorsementEffdate());
								newrecords.setEndtPrevPolicyNo(req.getEndtPrevPolicyNo());
								newrecords.setEndtPrevQuoteNo(req.getEndtPrevQuoteNo());
								newrecords.setEndtCount(req.getEndtCount());
								newrecords.setEndtStatus(req.getEndtStatus());
								newrecords.setIsFinyn(req.getIsFinaceYn());
								newrecords.setEndtCategDesc(req.getEndtCategDesc());
								newrecords.setEndorsementType(req.getEndorsementType());
								newrecords.setEndorsementTypeDesc(req.getEndorsementTypeDesc());

							}

							saveDataList.add(newrecords);
							buildingRepo.saveAndFlush(newrecords);

						} else {
							saveData = buildingRepo.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo(),
									1, "0");
							sectionData = eserSecRepo.findByRequestReferenceNoAndRiskIdAndProductIdAndSectionId(
									req.getRequestReferenceNo(), 1, req.getProductId(), bondDetails.getBondType());
							EserviceBuildingDetails newrecords = new EserviceBuildingDetails();
							newrecords = new DozerBeanMapper().map(saveData, EserviceBuildingDetails.class);
							newrecords.setBondType(bondDetails.getBondType());
							newrecords.setBondYear(bondDetails.getBondYear());
//							newrecords.setBondSuminsured(
//									StringUtils.isBlank(bondDetails.getBondSumInsured()) ? new BigDecimal("0")
//											: new BigDecimal(bondDetails.getBondSumInsured()));
							newrecords.setRiskId(bondDetails.getRiskId());
							newrecords.setSectionId(sectionData.getSectionId());
							newrecords.setSectionDesc(sectionData.getSectionName());
							if (!(req.getEndorsementType() == null || req.getEndorsementType() == 0))

							{

								newrecords.setOriginalPolicyNo(req.getOriginalPolicyNo());
								newrecords.setEndorsementDate(req.getEndorsementDate());
								newrecords.setEndorsementRemarks(req.getEndorsementRemarks());
								newrecords.setEndorsementEffdate(req.getEndorsementEffdate());
								newrecords.setEndtPrevPolicyNo(req.getEndtPrevPolicyNo());
								newrecords.setEndtPrevQuoteNo(req.getEndtPrevQuoteNo());
								newrecords.setEndtCount(req.getEndtCount());
								newrecords.setEndtStatus(req.getEndtStatus());
								newrecords.setIsFinyn(req.getIsFinaceYn());
								newrecords.setEndtCategDesc(req.getEndtCategDesc());
								newrecords.setEndorsementType(req.getEndorsementType());
								newrecords.setEndorsementTypeDesc(req.getEndorsementTypeDesc());

							}
							saveDataList.add(newrecords);
							buildingRepo.saveAndFlush(newrecords);

						}

					}
					// buildingRepo.saveAllAndFlush(saveDataList);
					for (EserviceBuildingDetails data : saveDataList) {
						// Section Req
						BuildingSectionRes sec = new BuildingSectionRes();
						sec.setMotorYn(sectionData.getProductType());
						sec.setSectionId(data.getSectionId());
						sec.setSectionName(data.getSectionDesc());

						sectionList.add(sec);
					}

				} catch (Exception Bond) {
					System.out.println("*****************Bond Save Exception********************");
					Bond.printStackTrace();
				}
				try {

					// Section Req

					// One Time Table Thread Call
					List<OneTimeTableRes> otResList1 = null;
					OneTimeTableReq otReq = new OneTimeTableReq();
					EserviceBuildingDetails save = saveDataList.get(0);
					otReq.setRequestReferenceNo(save.getRequestReferenceNo());
					otReq.setVehicleId(save.getRiskId());
					otReq.setBranchCode(save != null ? save.getBranchCode() : save.getBranchCode());
					otReq.setInsuranceId(save != null ? save.getCompanyId() : req.getInsuranceId());

					otReq.setProductId(Integer.valueOf(req.getProductId()));
					otReq.setSectionList(sectionList);

					otResList1 = otService.call_OT_Insert(otReq);
					for (OneTimeTableRes otRes : otResList1) {
						SlideSectionSaveRes res = new SlideSectionSaveRes();
						res.setResponse("Saved Successfully");
						res.setRiskId(otRes.getVehicleId());
						res.setVdRefNo(otRes.getVdRefNo());
						res.setCdRefNo(otRes.getCdRefNo());
						res.setMsrefno(otRes.getMsRefNo());
						res.setCompanyId(otRes.getCompanyId());
						res.setProductId(otRes.getProductId());
						res.setSectionId(otRes.getSectionId());
						res.setCustomerReferenceNo(saveData.getCustomerReferenceNo());
						res.setRequestReferenceNo(saveData.getRequestReferenceNo());
						res.setCreatedBy(req.getCreatedBy());
						resList.add(res);
					}
					// update Eservice Section details
					updatesectionDetails(req.getRequestReferenceNo());
				} catch (Exception e) {
					e.printStackTrace();
				}

				return resList;

			}

			@Override
			public List<SlideSectionSaveRes> saveBuilding(SlideBuildingSaveReq req, List<EserviceBuildingDetails> NewDataList) {
				// TODO Auto-generated method stub
				List<SlideSectionSaveRes> resList = new ArrayList<SlideSectionSaveRes>();
				EserviceBuildingDetails saveData = new EserviceBuildingDetails();
				EserviceSectionDetails sectionData = new EserviceSectionDetails();

				try {
					saveData = buildingRepo.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo(),
							Integer.valueOf(req.getRiskId()), req.getSectionId());

					if (null != req && StringUtils.isNotBlank(req.getRiskId()) && saveData == null && NewDataList != null
							&& !NewDataList.isEmpty()) {

						Integer riskId = req.getRiskId().matches("[0-9.]+") ? Integer.valueOf(req.getRiskId()) : 0;
						List<EserviceBuildingDetails> filtList = NewDataList.stream().filter(a -> a.getRiskId().equals(riskId))
								.collect(Collectors.toList());

						if (null != filtList && !filtList.isEmpty()) {

							saveData = filtList.get(0);
						}
					}
//					sectionData = eserSecRepo.findByRequestReferenceNoAndProductIdAndSectionId(req.getRequestReferenceNo(), req.getProductId() ,req.getSectionId());			
					sectionData = eserSecRepo.findByRequestReferenceNoAndRiskIdAndProductIdAndSectionId(
							req.getRequestReferenceNo(), 1, req.getProductId(), req.getSectionId());
					saveData.setSectionId(StringUtils.isBlank(req.getSectionId()) ? "1" : req.getSectionId());
					saveData.setSectionDesc(sectionData.getSectionName());
//					saveData.setBuildingSuminsured(
//							StringUtils.isNotBlank(req.getBuildingSumInsured()) ? new BigDecimal(req.getBuildingSumInsured())
//									: new BigDecimal("0"));
//					saveData.setWaterTankSi(StringUtils.isNotBlank(req.getWaterTankSi()) ? new BigDecimal(req.getWaterTankSi())
//							: new BigDecimal("0"));
//					saveData.setLossOfRentSi(
//							StringUtils.isNotBlank(req.getLossOfRentSi()) ? new BigDecimal(req.getLossOfRentSi())
//									: new BigDecimal("0"));
//					saveData.setArchitectsSi(
//							StringUtils.isNotBlank(req.getArchitectsSi()) ? new BigDecimal(req.getArchitectsSi())
//									: new BigDecimal("0"));
//					saveData.setGroundUndergroundSi(
//							StringUtils.isNotBlank(req.getGroundUndergroundSi()) ? new BigDecimal(req.getGroundUndergroundSi())
//									: new BigDecimal("0"));

					if (saveData.getExchangeRate() != null) {
						BigDecimal exRate = saveData.getExchangeRate();
//						if (StringUtils.isNotBlank(req.getBuildingSumInsured())) {
//							saveData.setBuildingSuminsuredLc(new BigDecimal(req.getBuildingSumInsured()).multiply(exRate));
//						} else {
//							saveData.setBuildingSuminsuredLc(BigDecimal.ZERO);
//						}

						// new Inputs
//						if (StringUtils.isNotBlank(req.getWaterTankSi())) {
//							saveData.setWaterTankSiLc(new BigDecimal(req.getWaterTankSi()).multiply(exRate));
//						} else {
//							saveData.setWaterTankSiLc(BigDecimal.ZERO);
//						}
//						if (StringUtils.isNotBlank(req.getLossOfRentSi())) {
//							saveData.setLossOfRentSiLc(new BigDecimal(req.getLossOfRentSi()).multiply(exRate));
//						} else {
//							saveData.setLossOfRentSiLc(BigDecimal.ZERO);
//						}
//						if (StringUtils.isNotBlank(req.getArchitectsSi())) {
//							saveData.setArchitectsSiLc(new BigDecimal(req.getArchitectsSi()).multiply(exRate));
//						} else {
//							saveData.setArchitectsSiLc(BigDecimal.ZERO);
//						}

					}
					if (StringUtils.isNotBlank(req.getWallType())) {
						String wallType = getListItem(saveData.getCompanyId(), saveData.getBranchCode(), "WALL_TYPE",
								req.getWallType());
						saveData.setWallType(req.getWallType());
						saveData.setWallTypeDesc(wallType);

					}

					if (StringUtils.isNotBlank(req.getRoofType())) {
						String roofType = getListItem(saveData.getCompanyId(), saveData.getBranchCode(), "ROOF_TYPE",
								req.getRoofType());
						saveData.setRoofType(req.getRoofType());
						saveData.setRoofTypeDesc(roofType);

					}

					if (StringUtils.isNotBlank(req.getBuildingUsageId())) {
						String buildingusage = getListItem(saveData.getCompanyId(), saveData.getBranchCode(), "BUILDING_USAGE",
								req.getBuildingUsageId());
						saveData.setBuildingUsageId(req.getBuildingUsageId());
						saveData.setBuildingUsageDesc(buildingusage);

					}
					SimpleDateFormat yf = new SimpleDateFormat("yyyy");
					Date today = new Date();
					String year = yf.format(today);
					// Building Age
					if (StringUtils.isNotBlank(req.getBuildingBuildYear())) {
						String buidingYear = req.getBuildingBuildYear();
						int buildingAge = Integer.valueOf(year) - Integer.valueOf(buidingYear);
						saveData.setBuildingAge(buildingAge);
						saveData.setBuildingBuildYear(Integer.valueOf(buidingYear));

					}

					// New Inputs
					if (StringUtils.isNotBlank(req.getTypeOfProperty())) {
						String typeOfProperty = getListItem(saveData.getCompanyId(), saveData.getBranchCode(),
								"TYPE_OF_PROPERTIES", req.getTypeOfProperty());
						saveData.setTypeOfProperty(req.getTypeOfProperty());
						saveData.setTypeOfPropertyDesc(typeOfProperty);

					}

					saveData.setBuildingOwnerYn(req.getBuildingOwnerYn());
					// Endorsement CHanges
					if (!(req.getEndorsementType() == null || req.getEndorsementType() == 0))

					{

						saveData.setOriginalPolicyNo(req.getOriginalPolicyNo());
						saveData.setEndorsementDate(req.getEndorsementDate());
						saveData.setEndorsementRemarks(req.getEndorsementRemarks());
						saveData.setEndorsementEffdate(req.getEndorsementEffdate());
						saveData.setEndtPrevPolicyNo(req.getEndtPrevPolicyNo());
						saveData.setEndtPrevQuoteNo(req.getEndtPrevQuoteNo());
						saveData.setEndtCount(req.getEndtCount());
						saveData.setEndtStatus(req.getEndtStatus());
						saveData.setIsFinyn(req.getIsFinaceYn());
						saveData.setEndtCategDesc(req.getEndtCategDesc());
						saveData.setEndorsementType(req.getEndorsementType());
						saveData.setEndorsementTypeDesc(req.getEndorsementTypeDesc());

					}

					if (req.getSectionId() != "1" && req.getProductId().equals("6")) {
						saveData.setIndustryId(
								StringUtils.isBlank(req.getIndustryType()) ? null : Integer.valueOf(req.getIndustryType()));
						IndustryMaster industrydel = industryrepo.findByIndustryIdAndCompanyIdAndProductId(
								Integer.valueOf(req.getIndustryType()), req.getInsuranceId(), req.getProductId());
						saveData.setIndustryDesc(industrydel.getIndustryName());
						saveData.setCategoryId(req.getOccupationId());
						if (StringUtils.isNotBlank(req.getOccupationId())) {
							OccupationMaster occupationData = getOccupationMasterDropdown(req.getInsuranceId(), "99999",
									req.getProductId(), req.getOccupationId());
							saveData.setCategoryDesc(occupationData != null ? occupationData.getOccupationName() : null);
						}
						saveData.setRegionDesc(req.getRegionName());
						saveData.setDistrictDesc(req.getDistrictName());
						saveData.setAddress(req.getLocationName());
						saveData.setCoveringDetails(req.getCoveringDetails());
						saveData.setDescriptionOfRisk(req.getDescriptionOfRisk());
					}

					// Status
					saveData.setStatus(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus());
					saveData.setFirstLossPayee(StringUtils.isBlank(req.getFirstLossPayee()) ? "None" : req.getFirstLossPayee());
					buildingRepo.save(saveData);

				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());

				}
				try {
					List<OneTimeTableRes> otResList = null;
					// Section Req
					List<BuildingSectionRes> sectionList = new ArrayList<BuildingSectionRes>();
					BuildingSectionRes sec = new BuildingSectionRes();
					sec.setMotorYn(sectionData.getProductType());
					sec.setSectionId(sectionData.getSectionId());
					sec.setSectionName(sectionData.getSectionName());
					sectionList.add(sec);

					// One Time Table Thread Call
					OneTimeTableReq otReq = new OneTimeTableReq();
					otReq.setRequestReferenceNo(saveData.getRequestReferenceNo());
					otReq.setVehicleId(saveData.getRiskId());
					otReq.setBranchCode(saveData.getBranchCode());
					otReq.setInsuranceId(saveData.getCompanyId());
					otReq.setProductId(Integer.valueOf(saveData.getProductId()));
					otReq.setSectionList(sectionList);

					otResList = otService.call_OT_Insert(otReq);
					for (OneTimeTableRes otRes : otResList) {
						SlideSectionSaveRes res = new SlideSectionSaveRes();
						res.setResponse("Saved Successfully");
						res.setRiskId(otRes.getVehicleId());
						res.setVdRefNo(otRes.getVdRefNo());
						res.setCdRefNo(otRes.getCdRefNo());
						res.setMsrefno(otRes.getMsRefNo());
						res.setCompanyId(otRes.getCompanyId());
						res.setProductId(otRes.getProductId());
						res.setSectionId(otRes.getSectionId());
						res.setCustomerReferenceNo(saveData.getCustomerReferenceNo());
						res.setRequestReferenceNo(saveData.getRequestReferenceNo());
						res.setCreatedBy(req.getCreatedBy());
						resList.add(res);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				return resList;
			}
			//

			@Override
			public List<SlideBuildingGetRes> getSlideBuilding(SlideSectionGetReq req) {

				List<SlideBuildingGetRes> resList = new ArrayList<>();
				List<EserviceBuildingDetails> getBuildingDataList = null;
				try {

//					EserviceBuildingDetails getBuildingData = buildingRepo.findByRequestReferenceNoAndRiskIdAndSectionId( req.getRequestReferenceNo() , Integer.valueOf(req.getRiskId()) , req.getSectionId());
					if (req.getSectionId().equals("555")) {
						getBuildingDataList = buildingRepo.findByRequestReferenceNoAndProductId(req.getRequestReferenceNo(),
								"6");
						getBuildingDataList = getBuildingDataList.stream().filter(s -> !s.getSectionId().equalsIgnoreCase("0"))
								.collect(Collectors.toList());

					}

					else {
						getBuildingDataList = buildingRepo.findAllByRequestReferenceNoAndSectionId(req.getRequestReferenceNo(),
								req.getSectionId());
					}
					if (null != getBuildingDataList && !getBuildingDataList.isEmpty()) {

						for (EserviceBuildingDetails getBuildingData : getBuildingDataList) {

							// BuildingDetails
							// data=Buildinginfo.findByRequestReferenceNoAndRiskId(getBuildingData.getRequestReferenceNo(),getBuildingData.getRiskId());
							SlideBuildingGetRes res = new SlideBuildingGetRes();
							BuildingDetails data1 = repo1.findByRequestReferenceNoAndRiskId(
									getBuildingData.getRequestReferenceNo(), getBuildingData.getRiskId());

							if (getBuildingData != null) {

								res.setCreatedBy(getBuildingData.getCreatedBy());
								res.setInsuranceId(getBuildingData.getCompanyId());
								res.setProductId(getBuildingData.getProductId());
								res.setRequestReferenceNo(getBuildingData.getRequestReferenceNo());
								res.setIndustryType(String.valueOf(getBuildingData.getIndustryId()));
								res.setOccupationId(getBuildingData.getCategoryId());
								res.setLocationName(getBuildingData.getAddress());
								res.setCoveringDetails(getBuildingData.getCoveringDetails());
								res.setDescriptionOfRisk(getBuildingData.getDescriptionOfRisk());
								res.setRegionName(getBuildingData.getRegionDesc());
								res.setRegionCode(getBuildingData.getRegionCode());
								res.setDistrictName(getBuildingData.getDistrictDesc());
								res.setDistrictCode(getBuildingData.getDistrictCode());
								res.setExchangeRate(getBuildingData.getExchangeRate());
								res.setPolicyStartDate(getBuildingData.getPolicyStartDate());
								res.setPolicyEndDate(getBuildingData.getPolicyEndDate());
								res.setPromoCode(getBuildingData.getPromocode());
								res.setCurrency(getBuildingData.getCurrency());
								res.setOccupationDesc(getBuildingData.getCategoryDesc());
								res.setIndustryTypeDesc(getBuildingData.getIndustryDesc());
								res.setSectionDesc(getBuildingData.getSectionDesc());
								res.setRiskId(
										getBuildingData.getRiskId() == null ? "" : getBuildingData.getRiskId().toString());
								res.setSectionId(getBuildingData.getSectionId());
//								res.setBuildingSumInsured(getBuildingData.getBuildingSuminsured() == null ? ""
//										: getBuildingData.getBuildingSuminsured().toPlainString());
//								res.setWaterTankSi(getBuildingData.getWaterTankSi() == null ? ""
//										: getBuildingData.getWaterTankSi().toPlainString());
//								res.setLossOfRentSi(getBuildingData.getLossOfRentSi() == null ? ""
//										: getBuildingData.getLossOfRentSi().toPlainString());
//								res.setArchitectsSi(getBuildingData.getArchitectsSi() == null ? ""
//										: getBuildingData.getArchitectsSi().toPlainString());
								res.setTypeOfProperty(
										getBuildingData.getTypeOfProperty() == null ? "" : getBuildingData.getTypeOfProperty());

								res.setBuildingUsageId(getBuildingData.getBuildingUsageId());
								res.setRoofType(getBuildingData.getRoofType());
								res.setWallType(getBuildingData.getWallType());
								res.setBuildingBuildYear(getBuildingData.getBuildingBuildYear() == null ? ""
										: getBuildingData.getBuildingBuildYear().toString());
								res.setEndorsementDate(getBuildingData.getEndorsementDate() == null ? null
										: getBuildingData.getEndorsementDate());
								res.setEndorsementEffdate(getBuildingData.getEndorsementEffdate() == null ? null
										: getBuildingData.getEndorsementEffdate());
								res.setEndorsementRemarks(getBuildingData.getEndorsementRemarks() == null ? ""
										: getBuildingData.getEndorsementRemarks().toString());
								res.setEndorsementType(getBuildingData.getEndorsementType() == null ? ""
										: getBuildingData.getEndorsementType().toString());
								res.setEndorsementTypeDesc(getBuildingData.getEndorsementTypeDesc() == null ? ""
										: getBuildingData.getEndorsementTypeDesc().toString());
								res.setEndtCount(getBuildingData.getEndtCount() == null ? ""
										: getBuildingData.getEndtCount().toString());
								res.setEndtCategDesc(
										getBuildingData.getEndtCategDesc() == null ? "" : getBuildingData.getEndtCategDesc());
								res.setOriginalPolicyNo(getBuildingData.getOriginalPolicyNo() == null ? null
										: getBuildingData.getOriginalPolicyNo());
								res.setIsFinaceYn(
										getBuildingData.getIsFinyn() == null ? null : getBuildingData.getIsFinyn().toString());
								res.setEndtPrevPolicyNo(getBuildingData.getEndtPrevPolicyNo() == null ? ""
										: getBuildingData.getEndtPrevPolicyNo().toString());
								res.setEndtPrevQuoteNo(getBuildingData.getEndtPrevQuoteNo() == null ? ""
										: getBuildingData.getEndtPrevQuoteNo().toString());
								res.setEndtStatus(getBuildingData.getEndtStatus() == null ? ""
										: getBuildingData.getEndtStatus().toString());
								if (data1 != null) {
									res.setLocationName(
											StringUtils.isBlank(data1.getLocationName()) ? "" : data1.getLocationName());
								}

								res.setSourceType(getBuildingData.getSourceType());
								res.setSourceTypeId(getBuildingData.getSourceTypeId());
								res.setCustomerName(getBuildingData.getCustomerName());
								res.setCustomerCode(getBuildingData.getCustomerCode());
								res.setBusinessInterruption(getBuildingData.getBusinessInterruption());
								res.setFirstLossPayee(getBuildingData.getFirstLossPayee());
		// commented temporary						
//								else {
//									res.setLocationName(null);
//								}
								resList.add(res);

							}

						}
					}

				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());
					return null;

				}
				return resList;
			}

			@Override
			public SuccessRes deleteSlideBuilding(SlideSectionGetReq req) {
				SuccessRes res = new SuccessRes();
				try {
					EserviceBuildingDetails getBuildingData = buildingRepo
							.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo(), 1, req.getSectionId());
					// Update Eservice
					if (getBuildingData != null) {
						buildingRepo.delete(getBuildingData);
					}

					// Delete Section
					List<EserviceSectionDetails> section = eserSecRepo
							.findByRequestReferenceNoAndSectionId(req.getRequestReferenceNo(), req.getSectionId());
					if (section != null) {
						eserSecRepo.deleteAll(section);
					}

					res.setResponse("Deleted Successfully");
					res.setSuccessId(req.getSectionId());

				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());
					return null;

				}
				return res;
			}

			@Override
			public List<SlidePersonalAccidentGetRes> getSlidePersonalAccident(SlideSectionGetReq req) {
				List<SlidePersonalAccidentGetRes> resList = new ArrayList<SlidePersonalAccidentGetRes>();
				try {
					List<String> statusNot = new ArrayList<String>();
					statusNot.add("D");

					List<EserviceCommonDetails> humanDatas = humanRepo.findByRequestReferenceNoAndSectionIdAndStatusNotIn(
							req.getRequestReferenceNo(), req.getSectionId(), statusNot);

					for (EserviceCommonDetails humanData : humanDatas) {
						SlidePersonalAccidentGetRes res = new SlidePersonalAccidentGetRes();
						res.setCreatedBy(humanData.getCreatedBy());
						res.setCustomerName(humanData.getCustomerName());
						res.setDob(humanData.getDob());
						res.setLocationName("");
						res.setInsuranceId(humanData.getCompanyId());
						res.setProductId(humanData.getProductId());
						res.setRequestReferenceNo(humanData.getRequestReferenceNo());
						res.setRiskId(humanData.getRiskId() == null ? "" : humanData.getRiskId().toString());
						res.setSectionId(req.getSectionId());
						res.setSumInsured(humanData.getSumInsured() == null ? "" : humanData.getSumInsured().toPlainString());
						res.setOccupationType(
								humanData.getOccupationType() == null ? "" : humanData.getOccupationType().toString());
						res.setOtherOccupation(humanData.getOtherOccupation());
						res.setTotalNoOfPersons(humanData.getCount() == null ? "" : humanData.getCount().toString());
						// Endorsement
						res.setEndorsementDate(humanData.getEndorsementDate() == null ? null : humanData.getEndorsementDate());
						res.setEndorsementEffdate(
								humanData.getEndorsementEffdate() == null ? null : humanData.getEndorsementEffdate());
						res.setEndorsementRemarks(
								humanData.getEndorsementRemarks() == null ? "" : humanData.getEndorsementRemarks().toString());
						res.setEndorsementType(
								humanData.getEndorsementType() == null ? "" : humanData.getEndorsementType().toString());
						res.setEndorsementTypeDesc(humanData.getEndorsementTypeDesc() == null ? ""
								: humanData.getEndorsementTypeDesc().toString());
						res.setEndtCount(humanData.getEndtCount() == null ? "" : humanData.getEndtCount().toString());
						res.setEndtCategDesc(humanData.getEndtCategDesc() == null ? "" : humanData.getEndtCategDesc());
						res.setOriginalPolicyNo(
								humanData.getOriginalPolicyNo() == null ? null : humanData.getOriginalPolicyNo());
						res.setIsFinaceYn(humanData.getIsFinyn() == null ? null : humanData.getIsFinyn().toString());
						res.setEndtPrevPolicyNo(
								humanData.getEndtPrevPolicyNo() == null ? "" : humanData.getEndtPrevPolicyNo().toString());
						res.setEndtPrevQuoteNo(
								humanData.getEndtPrevQuoteNo() == null ? "" : humanData.getEndtPrevQuoteNo().toString());
						res.setEndtStatus(humanData.getEndtStatus() == null ? "" : humanData.getEndtStatus().toString());

						resList.add(res);
					}

				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());
					return null;

				}
				return resList;
			}

			@Override
			public SuccessRes deleteSlidePersonalAccident(SlideSectionGetReq req) {
				SuccessRes res = new SuccessRes();
				try {
					List<EserviceCommonDetails> getHumanDatas = humanRepo
							.findByRequestReferenceNoAndSectionId(req.getRequestReferenceNo(), req.getSectionId());

					// Update Eservice
					if (getHumanDatas.size() > 0) {
						humanRepo.deleteAll(getHumanDatas);
					}

					// Delete Section
					List<EserviceSectionDetails> section = eserSecRepo
							.findByRequestReferenceNoAndSectionId(req.getRequestReferenceNo(), req.getSectionId());
					if (section != null) {
						eserSecRepo.deleteAll(section);
					}

					res.setResponse("Deleted Successfully");
					res.setSuccessId(req.getSectionId());

				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());
					return null;

				}
				return res;
			}

			@Override
			public List<SlideSectionSaveRes> savePersonalAccident(List<SlidePersonalAccidentSaveReq> reqList) {

				List<SlideSectionSaveRes> resList = new ArrayList<SlideSectionSaveRes>();
				EserviceBuildingDetails saveData = new EserviceBuildingDetails();
				List<EserviceCommonDetails> humanDatas = new ArrayList<EserviceCommonDetails>();
				EserviceCommonDetails humanData = new EserviceCommonDetails();
				EserviceSectionDetails sectionData = new EserviceSectionDetails();
				DozerBeanMapper dozerMapper = new DozerBeanMapper();
				List<BuildingSectionRes> sectionList = new ArrayList<BuildingSectionRes>();

				try {
					BigDecimal endtCount = BigDecimal.ZERO;
					String refNo = reqList.get(0).getRequestReferenceNo();
					String productId = reqList.get(0).getProductId();
					String sectionId = reqList.get(0).getSectionId();
					Integer riskId = 1;

					if (null != reqList && !reqList.isEmpty() && StringUtils.isNotBlank(reqList.get(0).getRiskId())
							&& reqList.get(0).getRiskId().matches("[0-9]+")) {

						riskId = Integer.valueOf(reqList.get(0).getRiskId());

					}
					saveData = buildingRepo.findByRequestReferenceNoAndRiskIdAndSectionId(refNo, riskId, "0");
					if (saveData != null) {

						dozerMapper.map(saveData, humanData);
						humanData.setPolicyPeriod(saveData.getPolicyPeriord());

					}
					// sectionData =
					// eserSecRepo.findByRequestReferenceNoAndRiskIdAndProductIdAndSectionId(refNo
					// ,1 , productId ,sectionId);
					humanDatas = humanRepo.findByRequestReferenceNoAndSectionId(refNo, sectionId);
					SlidePersonalAccidentSaveReq firstRow = reqList.get(0);

					if ((firstRow.getEndorsementType() == null || firstRow.getEndorsementType() == 0)
							&& humanDatas.size() > 0) {
						humanRepo.deleteAll(humanDatas);
						dozerMapper.map(humanDatas.get(0), humanData);
					} else if (humanDatas.size() > 0) {
						List<EserviceCommonDetails> humanData1 = new ArrayList<EserviceCommonDetails>();
						if (!(firstRow.getEndorsementType() == null || firstRow.getEndorsementType() == 0)) {

							// search
							if (reqList.get(0).getEndtCount().compareTo(BigDecimal.ONE) == 0) {
								endtCount = reqList.get(0).getEndtCount();
								humanData1 = humanRepo.findBySectionIdAndPolicyNoAndStatusNot(sectionId,
										reqList.get(0).getOriginalPolicyNo(), "D");
							} else {
								endtCount = reqList.get(0).getEndtCount().subtract(BigDecimal.ONE);
								humanData1 = humanRepo.findByEndtCountAndSectionIdAndOriginalPolicyNoAndStatusNot(endtCount,
										sectionId, reqList.get(0).getOriginalPolicyNo(), "D");
							}

							// delete all
							humanRepo.deleteAll(humanDatas);

						}
						List<EserviceCommonDetails> humanDatas2 = new ArrayList<EserviceCommonDetails>();
						dozerMapper.map(humanDatas.get(0), humanData);
						String policyNo = humanDatas.get(0).getPolicyNo();
						humanData1.forEach(hum -> {
							EserviceCommonDetails o = new EserviceCommonDetails();
							dozerMapper.map(hum, o);
							o.setStatus("D");
							o.setRequestReferenceNo(refNo);
							o.setQuoteNo("");
							o.setPolicyNo(policyNo);
							if (!(firstRow.getEndorsementType() == null || firstRow.getEndorsementType() == 0))

							{

								o.setOriginalPolicyNo(firstRow.getOriginalPolicyNo());
								o.setEndorsementDate(firstRow.getEndorsementDate());
								o.setEndorsementRemarks(firstRow.getEndorsementRemarks());
								o.setEndorsementEffdate(firstRow.getEndorsementEffdate());
								o.setEndtPrevPolicyNo(firstRow.getEndtPrevPolicyNo());
								o.setEndtPrevQuoteNo(firstRow.getEndtPrevQuoteNo());
								o.setEndtCount(firstRow.getEndtCount());
								o.setEndtStatus(firstRow.getEndtStatus());
								o.setIsFinyn(firstRow.getIsFinaceYn());
								o.setEndtCategDesc(firstRow.getEndtCategDesc());
								o.setEndorsementType(firstRow.getEndorsementType());
								o.setEndorsementTypeDesc(firstRow.getEndorsementTypeDesc());

							}

							humanDatas2.add(o);
						});
						humanRepo.saveAllAndFlush(humanDatas2);

					}

					sectionData = eserSecRepo.findByRequestReferenceNoAndRiskIdAndProductIdAndSectionId(refNo, 1, productId,
							sectionId);
					List<EserviceCommonDetails> saveHumanList = new ArrayList<EserviceCommonDetails>();
					for (SlidePersonalAccidentSaveReq data : reqList) {
						// Save Human
						EserviceCommonDetails saveHuman = new EserviceCommonDetails();
						saveHuman = dozerMapper.map(humanData, EserviceCommonDetails.class);
						saveHuman.setRequestReferenceNo(data.getRequestReferenceNo());
						// saveHuman.setRiskId(Integer.valueOf(data.getOccupationType())) ;
						saveHuman.setRiskId(data.getRiskId() != null ? Integer.valueOf(data.getRiskId()) : null);
						saveHuman.setOriginalRiskId(
								data.getOriginalRiskId() != null ? Integer.valueOf(data.getOriginalRiskId()) : null);
						saveHuman.setSectionId(data.getSectionId());
						saveHuman.setSectionName(sectionData != null ? sectionData.getSectionName() : "");
						;
						saveHuman.setCreatedBy(data.getCreatedBy());
						saveHuman.setSumInsured(StringUtils.isBlank(data.getSumInsured()) ? new BigDecimal(0)
								: new BigDecimal(data.getSumInsured()));
//						saveHuman.setTtdSumInsured(data.getTtdSumInsured() != null ? data.getTtdSumInsured() : 0);
//						saveHuman.setMeSumInsured(data.getMeSumInsured() != null ? data.getMeSumInsured() : 0);
//						saveHuman.setFeSumInsured(data.getFeSumInsured() != null ? data.getFeSumInsured() : 0);
//						saveHuman.setPtdSumInsured(data.getPtdSumInsured() != null ? data.getPtdSumInsured() : 0);

						// Personal Liability
						if (StringUtils.isNotBlank(data.getOccupationType())) {
							OccupationMaster occupationData = getOccupationMasterDropdown(humanData.getCompanyId(),
									humanData.getBranchCode(), humanData.getProductId(), data.getOccupationType());
							saveHuman.setOccupationType(occupationData.getOccupationId().toString());
							saveHuman.setOccupationDesc(occupationData.getOccupationName());
							saveHuman.setCategoryId(occupationData.getCategoryId());
							// saveHuman.setCategoryDesc("Class " + occupationData.getCategoryId());
						}
						saveHuman.setOtherOccupation(data.getOtherOccupation());
						saveHuman.setTotalNoOfEmployees(StringUtils.isBlank(data.getTotalNoOfPersons()) ? null
								: Long.valueOf(data.getTotalNoOfPersons()));
						saveHuman.setCount(StringUtils.isBlank(data.getTotalNoOfPersons()) ? null
								: Integer.valueOf(data.getTotalNoOfPersons()));
						// Lc Calculation
						BigDecimal exchangeRate = humanData.getExchangeRate() != null ? humanData.getExchangeRate()
								: BigDecimal.ZERO;
						saveHuman.setSumInsuredLc(
								saveHuman.getSumInsured() == null ? null : saveHuman.getSumInsured().multiply(exchangeRate));
//						saveHuman.setTtdSumInsuredLc(
//								saveHuman.getTtdSumInsured() != null ? saveHuman.getTtdSumInsured() * exchangeRate.intValue()
//										: 0);
//						saveHuman.setMeSumInsuredLc(
//								saveHuman.getMeSumInsured() != null ? saveHuman.getMeSumInsured() * exchangeRate.intValue()
//										: 0);
//						saveHuman.setFeSumInsuredLc(
//								saveHuman.getFeSumInsured() != null ? saveHuman.getFeSumInsured() * exchangeRate.intValue()
//										: 0);
//						saveHuman.setPtdSumInsuredLc(
//								saveHuman.getPtdSumInsured() != null ? saveHuman.getPtdSumInsured() * exchangeRate.intValue()
//										: 0);
						// Endorsement CHanges
						if (!(data.getEndorsementType() == null || data.getEndorsementType() == 0))

						{

							saveHuman.setOriginalPolicyNo(data.getOriginalPolicyNo());
							saveHuman.setEndorsementDate(data.getEndorsementDate());
							saveHuman.setEndorsementRemarks(data.getEndorsementRemarks());
							saveHuman.setEndorsementEffdate(data.getEndorsementEffdate());
							saveHuman.setEndtPrevPolicyNo(data.getEndtPrevPolicyNo());
							saveHuman.setEndtPrevQuoteNo(data.getEndtPrevQuoteNo());
							saveHuman.setEndtCount(data.getEndtCount());
							saveHuman.setEndtStatus(data.getEndtStatus());
							saveHuman.setIsFinyn(data.getIsFinaceYn());
							saveHuman.setEndtCategDesc(data.getEndtCategDesc());
							saveHuman.setEndorsementType(data.getEndorsementType());
							saveHuman.setEndorsementTypeDesc(data.getEndorsementTypeDesc());
							saveHuman.setStatus("E");
						}
						saveHumanList.add(saveHuman);

					}
					humanRepo.saveAllAndFlush(saveHumanList);

					List<EserviceCommonDetails> esercommonDatas = eserCommonRepo
							.findByRequestReferenceNoAndSectionId(humanData.getRequestReferenceNo(), humanData.getSectionId());
					if (esercommonDatas.size() == 0) {
						esercommonDatas = saveHumanList;
					}
					for (EserviceCommonDetails data : esercommonDatas) {
						// Section Req
						BuildingSectionRes sec = new BuildingSectionRes();
						sec.setMotorYn(sectionData.getProductType());
						sec.setSectionId(sectionData.getSectionId());
						sec.setSectionName(sectionData.getSectionName());
						sec.setOccupationId(data.getOccupationType());
						sec.setOriginalRiskId(data.getOriginalRiskId() != null ? data.getOriginalRiskId() : 0);
						sectionList.add(sec);
					}
				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());

				}
				try {
					List<OneTimeTableRes> otResList = null;
					// One Time Table Thread Call

//					for( SlidePersonalAccidentSaveReq req :   reqList) {
					OneTimeTableReq otReq = new OneTimeTableReq();
					SlidePersonalAccidentSaveReq req = reqList.get(0);
					otReq.setRequestReferenceNo(req.getRequestReferenceNo());
					otReq.setVehicleId(1);
					otReq.setBranchCode(saveData != null ? saveData.getBranchCode() : humanData.getBranchCode());
					otReq.setInsuranceId(saveData != null ? saveData.getCompanyId() : humanData.getCompanyId());
					otReq.setProductId(Integer.valueOf(req.getProductId()));
					otReq.setRiskId(req.getRiskId() != null ? req.getRiskId() : "");
					otReq.setSectionList(sectionList);

					otResList = otService.call_OT_Insert(otReq);
					for (OneTimeTableRes otRes : otResList) {
						SlideSectionSaveRes res = new SlideSectionSaveRes();
						res.setResponse("Saved Successfully");
						res.setRiskId(otRes.getVehicleId());
						res.setVdRefNo(otRes.getVdRefNo());
						res.setCdRefNo(otRes.getCdRefNo());
						res.setMsrefno(otRes.getMsRefNo());
						res.setCompanyId(otRes.getCompanyId());
						res.setProductId(otRes.getProductId());
						res.setSectionId(otRes.getSectionId());
						res.setCustomerReferenceNo(
								saveData != null ? saveData.getCustomerReferenceNo() : humanData.getCustomerReferenceNo());
						res.setRequestReferenceNo(
								saveData != null ? saveData.getRequestReferenceNo() : humanData.getRequestReferenceNo());
						res.setCreatedBy(reqList.get(0).getCreatedBy());
						resList.add(res);
					}

//					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				return resList;
			}

			*/
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
//					motSaveReq.setAcExecutiveId null;
//					motSaveReq.setCommissionType null;
//					motSaveReq.setCustomerCode 620499;
//					motSaveReq.setCustomerName null;
//					motSaveReq.setBdmCode();
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
//					motSaveReq.setTppdFreeLimit null;
//					motSaveReq.setTppdIncreaeLimit ;
//					motSaveReq.setTrailerDetails null;
					motSaveReq.setVehcileModel(motReq.getVehcileModel());
					motSaveReq.setVehicleModelDesc(motReq.getVehicleModelDesc());
					motSaveReq.setVehicleType(motReq.getVehicleType());
					motSaveReq.setVehicleTypeDesc(motReq.getVehicleTypeDesc());
					motSaveReq.setVehicleMake(motReq.getVehicleMake());
					motSaveReq.setVehicleMakeDesc(motReq.getVehicleMakeDesc());
//					motSaveReq.setWindScreenSumInsured ;
//					motSaveReq.setWindscreencoverrequired null;
//					motSaveReq.setaccident null;
//					motSaveReq.setperiodOfInsurance 30;
					motSaveReq.setPolicyStartDate(motReq.getPolicyStartDate());
					motSaveReq.setPolicyEndDate(motReq.getPolicyEndDate());
					motSaveReq.setCurrency(motReq.getCurrency());
					motSaveReq.setExchangeRate(motReq.getExchangeRate());
					motSaveReq.setHavepromocode("N");
					// motSaveReq.setPromoCode null;
					motSaveReq.setCollateralYn("N");
//					motSaveReq.setBorrowerType null;
//					motSaveReq.setCollateralName null;
//					motSaveReq.setFirstLossPayee null;
					motSaveReq.setFleetOwnerYn("N");
					motSaveReq.setNoOfVehicles("1");
//					motSaveReq.setNoOfComprehensives null;
//					motSaveReq.setClaimRatio null;
					motSaveReq.setSavedFrom("Owner");
					motSaveReq.setUserType("User");
					motSaveReq.setSearchFromApi(false);
//					motSaveReq.setTiraCoverNoteNo null;
//					motSaveReq.setEndorsementYn N;
//					motSaveReq.setEndorsementDate null;
//					motSaveReq.setEndorsementEffectiveDate null;
//					motSaveReq.setEndorsementRemarks null;
//					motSaveReq.setEndorsementType null;
//					motSaveReq.setEndorsementTypeDesc null;
//					motSaveReq.setEndtCategoryDesc null;
//					motSaveReq.setEndtCount null;
//					motSaveReq.setEndtPrevPolicyNo null;
//					motSaveReq.setEndtPrevQuoteNo null;
//					motSaveReq.setEndtStatus null;
//					motSaveReq.setIsFinanceEndt null;
//					motSaveReq.setOrginalPolicyNo null;
//					    Scenarios {
//					        ExchangeRateScenario {
//					            OldAcccessoriesSumInsured null;
//					            OldCurrency null;
//					            OldExchangeRate null;
//					            OldSumInsured null;
//					            OldTppdIncreaeLimit null;
//					            OldWindScreenSumInsured null
//					        }

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
//				map.put("customerReferenceNo",custReq.getCustomerReferenceNo())	
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
//				calcReq.setSectionId(motorReq.getInsuranceType());
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
//					Map<String,Object> resMap=(Map<String,Object>)map.get("Result");

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
		/*
			@Override
			public List<SlideSectionSaveRes> saveBusinessInterruption(SlideBusinessInterruptionReq req) {

				List<SlideSectionSaveRes> resList = new ArrayList<SlideSectionSaveRes>();
				EserviceBuildingDetails saveData = new EserviceBuildingDetails();
				EserviceSectionDetails sectionData = new EserviceSectionDetails();

				try {
					saveData = buildingRepo.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo(),
							Integer.valueOf(req.getRiskId()), req.getSectionId());
					sectionData = eserSecRepo.findByRequestReferenceNoAndRiskIdAndProductIdAndSectionId(
							req.getRequestReferenceNo(), Integer.valueOf(req.getRiskId()), req.getProductId(),
							req.getSectionId());

					saveData.setGrossProfitFc(StringUtils.isBlank(req.getGrossProfitSi()) ? new BigDecimal(0)
							: new BigDecimal(req.getGrossProfitSi()));
					saveData.setIndemnityPeriodFc(StringUtils.isBlank(req.getIndemnityPeriodSi()) ? new BigDecimal(0)
							: new BigDecimal(req.getIndemnityPeriodSi()));

					// Endorsement CHanges
					if (!(req.getEndorsementType() == null || req.getEndorsementType() == 0))

					{

						saveData.setOriginalPolicyNo(req.getOriginalPolicyNo());
						saveData.setEndorsementDate(req.getEndorsementDate());
						saveData.setEndorsementRemarks(req.getEndorsementRemarks());
						saveData.setEndorsementEffdate(req.getEndorsementEffdate());
						saveData.setEndtPrevPolicyNo(req.getEndtPrevPolicyNo());
						saveData.setEndtPrevQuoteNo(req.getEndtPrevQuoteNo());
						saveData.setEndtCount(req.getEndtCount());
						saveData.setEndtStatus(req.getEndtStatus());
						saveData.setIsFinyn(req.getIsFinaceYn());
						saveData.setEndtCategDesc(req.getEndtCategDesc());
						saveData.setEndorsementType(req.getEndorsementType());
						saveData.setEndorsementTypeDesc(req.getEndorsementTypeDesc());

					}
					// Status
					saveData.setStatus(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus());
					buildingRepo.save(saveData);
				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());

				}
				try {
					List<OneTimeTableRes> otResList = null;
					// Section Req
					List<BuildingSectionRes> sectionList = new ArrayList<BuildingSectionRes>();
					BuildingSectionRes sec = new BuildingSectionRes();
					sec.setMotorYn(sectionData.getProductType());
					sec.setSectionId(sectionData.getSectionId());
					sec.setSectionName(sectionData.getSectionName());
					sectionList.add(sec);

					// One Time Table Thread Call
					OneTimeTableReq otReq = new OneTimeTableReq();
					otReq.setRequestReferenceNo(saveData.getRequestReferenceNo());
					otReq.setVehicleId(saveData.getRiskId());
					otReq.setBranchCode(saveData.getBranchCode());
					otReq.setInsuranceId(saveData.getCompanyId());
					otReq.setProductId(Integer.valueOf(saveData.getProductId()));
					otReq.setSectionList(sectionList);

					otResList = otService.call_OT_Insert(otReq);
					for (OneTimeTableRes otRes : otResList) {
						SlideSectionSaveRes res = new SlideSectionSaveRes();
						res.setResponse("Saved Successfully");
						res.setRiskId(otRes.getVehicleId());
						res.setVdRefNo(otRes.getVdRefNo());
						res.setCdRefNo(otRes.getCdRefNo());
						res.setMsrefno(otRes.getMsRefNo());
						res.setCompanyId(otRes.getCompanyId());
						res.setProductId(otRes.getProductId());
						res.setSectionId(otRes.getSectionId());
						res.setCustomerReferenceNo(saveData.getCustomerReferenceNo());
						res.setRequestReferenceNo(saveData.getRequestReferenceNo());
						res.setCreatedBy(req.getCreatedBy());
						resList.add(res);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				return resList;

			}

			@Override
			public BusinessInterruptionRes getBusinessInterruption(SlideSectionGetReq req) {
				BusinessInterruptionRes res = new BusinessInterruptionRes();
				try {
					EserviceBuildingDetails getBuildingData = buildingRepo
							.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo(), 1, req.getSectionId());

					if (getBuildingData != null) {
						res.setCreatedBy(getBuildingData.getCreatedBy());
						res.setInsuranceId(getBuildingData.getCompanyId());
						res.setProductId(getBuildingData.getProductId());
						res.setRequestReferenceNo(getBuildingData.getRequestReferenceNo());
						res.setRiskId(getBuildingData.getRiskId() == null ? "" : getBuildingData.getRiskId().toString());
						res.setSectionId(req.getSectionId());
						res.setGrossProfitSi(getBuildingData.getGrossProfitFc() == null ? ""
								: getBuildingData.getGrossProfitFc().toPlainString());
						res.setIndemnityPeriodSi(getBuildingData.getIndemnityPeriodFc() == null ? ""
								: getBuildingData.getIndemnityPeriodFc().toPlainString()); // Endorsement
						res.setEndorsementDate(
								getBuildingData.getEndorsementDate() == null ? null : getBuildingData.getEndorsementDate());
						res.setEndorsementEffdate(getBuildingData.getEndorsementEffdate() == null ? null
								: getBuildingData.getEndorsementEffdate());
						res.setEndorsementRemarks(getBuildingData.getEndorsementRemarks() == null ? ""
								: getBuildingData.getEndorsementRemarks().toString());
						res.setEndorsementType(getBuildingData.getEndorsementType() == null ? ""
								: getBuildingData.getEndorsementType().toString());
						res.setEndorsementTypeDesc(getBuildingData.getEndorsementTypeDesc() == null ? ""
								: getBuildingData.getEndorsementTypeDesc().toString());
						res.setEndtCount(
								getBuildingData.getEndtCount() == null ? "" : getBuildingData.getEndtCount().toString());
						res.setEndtCategDesc(
								getBuildingData.getEndtCategDesc() == null ? "" : getBuildingData.getEndtCategDesc());
						res.setOriginalPolicyNo(
								getBuildingData.getOriginalPolicyNo() == null ? null : getBuildingData.getOriginalPolicyNo());
						res.setIsFinaceYn(
								getBuildingData.getIsFinyn() == null ? null : getBuildingData.getIsFinyn().toString());
						res.setEndtPrevPolicyNo(getBuildingData.getEndtPrevPolicyNo() == null ? ""
								: getBuildingData.getEndtPrevPolicyNo().toString());
						res.setEndtPrevQuoteNo(getBuildingData.getEndtPrevQuoteNo() == null ? ""
								: getBuildingData.getEndtPrevQuoteNo().toString());
						res.setEndtStatus(
								getBuildingData.getEndtStatus() == null ? "" : getBuildingData.getEndtStatus().toString());

					}

				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());
					return null;

				}
				return res;

			}

			@Override
			public List<SlideSectionSaveRes> saveGoodsInTransit(SlideGoodsInTransitSaveReq req) {
				List<SlideSectionSaveRes> resList = new ArrayList<SlideSectionSaveRes>();
				EserviceBuildingDetails saveData = new EserviceBuildingDetails();
				EserviceSectionDetails sectionData = new EserviceSectionDetails();

				try {
					saveData = buildingRepo.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo(),
							Integer.valueOf(req.getRiskId()), req.getSectionId());
					sectionData = eserSecRepo.findByRequestReferenceNoAndRiskIdAndProductIdAndSectionId(
							req.getRequestReferenceNo(), Integer.valueOf(req.getRiskId()), req.getProductId(),
							req.getSectionId());

					saveData.setTransportedBy(req.getTransportedBy());
					saveData.setGeographicalCoverage(req.getGeographicalCoverage());
					saveData.setModeOfTransport(req.getModeOfTransport());
					saveData.setSingleRoadSiFc(StringUtils.isBlank(req.getSingleRoadSiFc()) ? new BigDecimal(0)
							: new BigDecimal(req.getSingleRoadSiFc()));
					// saveData.setSingleRoadSiLc(StringUtils.isBlank(req.getSingleRoadSiFc()) ? new
					// BigDecimal(0): new BigDecimal(req.getSingleRoadSiFc()));
					saveData.setEstAnnualCarriesSiFc(StringUtils.isBlank(req.getEstAnnualCarriesSiFc()) ? new BigDecimal(0)
							: new BigDecimal(req.getEstAnnualCarriesSiFc()));
					// saveData.setEstAnnualCarriesSiLc(StringUtils.isBlank(req.getEstAnnualCarriesSiFc())
					// ? new BigDecimal(0): new BigDecimal(req.getEstAnnualCarriesSiFc()));

					if (StringUtils.isNotBlank(saveData.getExchangeRate().toString())) {
						BigDecimal exRate = saveData.getExchangeRate();
						if (StringUtils.isNotBlank(req.getSingleRoadSiFc())) {
							saveData.setSingleRoadSiLc(new BigDecimal(req.getSingleRoadSiFc()).multiply(exRate));
						} else {
							saveData.setSingleRoadSiLc(BigDecimal.ZERO);
						}

						if (StringUtils.isNotBlank(req.getEstAnnualCarriesSiFc())) {
							saveData.setEstAnnualCarriesSiLc(new BigDecimal(req.getEstAnnualCarriesSiFc()).multiply(exRate));
						} else {
							saveData.setEstAnnualCarriesSiLc(BigDecimal.ZERO);
						}
					}
					// Endorsement CHanges
					if (!(req.getEndorsementType() == null || req.getEndorsementType() == 0))

					{

						saveData.setOriginalPolicyNo(req.getOriginalPolicyNo());
						saveData.setEndorsementDate(req.getEndorsementDate());
						saveData.setEndorsementRemarks(req.getEndorsementRemarks());
						saveData.setEndorsementEffdate(req.getEndorsementEffdate());
						saveData.setEndtPrevPolicyNo(req.getEndtPrevPolicyNo());
						saveData.setEndtPrevQuoteNo(req.getEndtPrevQuoteNo());
						saveData.setEndtCount(req.getEndtCount());
						saveData.setEndtStatus(req.getEndtStatus());
						saveData.setIsFinyn(req.getIsFinaceYn());
						saveData.setEndtCategDesc(req.getEndtCategDesc());
						saveData.setEndorsementType(req.getEndorsementType());
						saveData.setEndorsementTypeDesc(req.getEndorsementTypeDesc());

					}
					// Status
					saveData.setStatus(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus());
					buildingRepo.save(saveData);
				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());

				}
				try {
					List<OneTimeTableRes> otResList = null;
					// Section Req
					List<BuildingSectionRes> sectionList = new ArrayList<BuildingSectionRes>();
					BuildingSectionRes sec = new BuildingSectionRes();
					sec.setMotorYn(sectionData.getProductType());
					sec.setSectionId(sectionData.getSectionId());
					sec.setSectionName(sectionData.getSectionName());
					sectionList.add(sec);

					// One Time Table Thread Call
					OneTimeTableReq otReq = new OneTimeTableReq();
					otReq.setRequestReferenceNo(saveData.getRequestReferenceNo());
					otReq.setVehicleId(saveData.getRiskId());
					otReq.setBranchCode(saveData.getBranchCode());
					otReq.setInsuranceId(saveData.getCompanyId());
					otReq.setProductId(Integer.valueOf(saveData.getProductId()));
					otReq.setSectionList(sectionList);

					otResList = otService.call_OT_Insert(otReq);
					for (OneTimeTableRes otRes : otResList) {
						SlideSectionSaveRes res = new SlideSectionSaveRes();
						res.setResponse("Saved Successfully");
						res.setRiskId(otRes.getVehicleId());
						res.setVdRefNo(otRes.getVdRefNo());
						res.setCdRefNo(otRes.getCdRefNo());
						res.setMsrefno(otRes.getMsRefNo());
						res.setCompanyId(otRes.getCompanyId());
						res.setProductId(otRes.getProductId());
						res.setSectionId(otRes.getSectionId());
						res.setCustomerReferenceNo(saveData.getCustomerReferenceNo());
						res.setRequestReferenceNo(saveData.getRequestReferenceNo());
						res.setCreatedBy(req.getCreatedBy());
						resList.add(res);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				return resList;

			}

			@Override
			public GoodInTransitRes getGoodsInTransit(SlideSectionGetReq req) {
				GoodInTransitRes res = new GoodInTransitRes();
				try {
					EserviceBuildingDetails getBuildingData = buildingRepo
							.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo(), 1, req.getSectionId());

					if (getBuildingData != null) {
						res.setCreatedBy(getBuildingData.getCreatedBy());
						res.setInsuranceId(getBuildingData.getCompanyId());
						res.setProductId(getBuildingData.getProductId());
						res.setRequestReferenceNo(getBuildingData.getRequestReferenceNo());
						res.setRiskId(getBuildingData.getRiskId() == null ? "" : getBuildingData.getRiskId().toString());
						res.setSectionId(req.getSectionId());
						res.setTransportedBy(
								getBuildingData.getTransportedBy() == null ? "" : getBuildingData.getTransportedBy());
						res.setGeographicalCoverage(getBuildingData.getGeographicalCoverage() == null ? ""
								: getBuildingData.getGeographicalCoverage());
						res.setModeOfTransport(
								getBuildingData.getModeOfTransport() == null ? "" : getBuildingData.getModeOfTransport()); // Endorsement
						res.setEndorsementDate(
								getBuildingData.getEndorsementDate() == null ? null : getBuildingData.getEndorsementDate());
						res.setEndorsementEffdate(getBuildingData.getEndorsementEffdate() == null ? null
								: getBuildingData.getEndorsementEffdate());
						res.setEndorsementRemarks(
								getBuildingData.getEndorsementRemarks() == null ? "" : getBuildingData.getEndorsementRemarks());
						res.setEndorsementType(getBuildingData.getEndorsementType() == null ? ""
								: getBuildingData.getEndorsementType().toString());
						res.setEndorsementTypeDesc(getBuildingData.getEndorsementTypeDesc() == null ? ""
								: getBuildingData.getEndorsementTypeDesc().toString());
						res.setEndtCount(
								getBuildingData.getEndtCount() == null ? "" : getBuildingData.getEndtCount().toString());
						res.setEndtCategDesc(
								getBuildingData.getEndtCategDesc() == null ? "" : getBuildingData.getEndtCategDesc());
						res.setOriginalPolicyNo(
								getBuildingData.getOriginalPolicyNo() == null ? null : getBuildingData.getOriginalPolicyNo());
						res.setIsFinaceYn(
								getBuildingData.getIsFinyn() == null ? null : getBuildingData.getIsFinyn().toString());
						res.setEndtPrevPolicyNo(getBuildingData.getEndtPrevPolicyNo() == null ? ""
								: getBuildingData.getEndtPrevPolicyNo().toString());
						res.setEndtPrevQuoteNo(getBuildingData.getEndtPrevQuoteNo() == null ? ""
								: getBuildingData.getEndtPrevQuoteNo().toString());
						res.setEndtStatus(
								getBuildingData.getEndtStatus() == null ? "" : getBuildingData.getEndtStatus().toString());
						res.setSingleRoadSiFc(getBuildingData.getSingleRoadSiFc() == null ? ""
								: getBuildingData.getSingleRoadSiFc().toString());
						res.setSingleRoadSiLc(getBuildingData.getSingleRoadSiLc() == null ? ""
								: getBuildingData.getSingleRoadSiLc().toString());
						res.setEstAnnualCarriesSiFc(getBuildingData.getEstAnnualCarriesSiFc() == null ? ""
								: getBuildingData.getEstAnnualCarriesSiFc().toString());
						res.setEstAnnualCarriesSiLc(getBuildingData.getEstAnnualCarriesSiLc() == null ? ""
								: getBuildingData.getEstAnnualCarriesSiLc().toString());

					}

				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());
					return null;

				}
				return res;

			}
		*/
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
					System.out.println(">>>>>>>>>LogIn_id....");
					lg = list.size() > 0 ? list.get(0) : null;
					
				} catch (Exception e) {
					e.printStackTrace();
					log.info("Exception is ---> " + e.getMessage());
					return null;
				}
				return lg;
			}
		/*
			@Override
			public List<SlideSectionSaveRes> saveHealthInsureDetails(List<SlideHealthInsureSaveReq> reqList) {
				// TODO Auto-generated method stub
				List<SlideSectionSaveRes> resList = new ArrayList<SlideSectionSaveRes>();
				// List<SlideHIFamilyDetailsReq> family = reqList.get(0).getFamilyDetails();
				EserviceBuildingDetails saveData = new EserviceBuildingDetails();
				List<EserviceCommonDetails> humanDatas = new ArrayList<EserviceCommonDetails>();
				EserviceCommonDetails humanData = new EserviceCommonDetails();
				EserviceSectionDetails sectionData = new EserviceSectionDetails();
				DozerBeanMapper dozerMapper = new DozerBeanMapper();
				List<BuildingSectionRes> sectionList = new ArrayList<BuildingSectionRes>();

				try {
					String refNo = reqList.get(0).getRequestReferenceNo();
					// humanRepo.deleteByRequestReferenceNo(refNo);
					BigDecimal endtCount = BigDecimal.ZERO;
					// String refNo = reqList.get(0).getRequestReferenceNo();
					String productId = reqList.get(0).getProductId();
					String sectionId = reqList.get(0).getSectionId();
					String companyId = reqList.get(0).getInsuranceId();
					// int length=reqList.get(0).getFamilyDetails().size();
					saveData = buildingRepo.findByRequestReferenceNoAndRiskIdAndSectionId(refNo, 1, "0");
					if (saveData != null) {

						dozerMapper.map(saveData, humanData);
						humanData.setPolicyPeriod(saveData.getPolicyPeriord());

					}
					humanDatas = humanRepo.findByRequestReferenceNoAndSectionId(refNo, sectionId);

					SlideHealthInsureSaveReq firstRow = reqList.get(0);

					if ((firstRow.getEndorsementType() == null || firstRow.getEndorsementType() == 0)
							&& humanDatas.size() > 0) {
						humanRepo.deleteAll(humanDatas);
						dozerMapper.map(humanDatas.get(0), humanData);
					} else if (humanDatas.size() > 0) {
						List<EserviceCommonDetails> humanData1 = new ArrayList<EserviceCommonDetails>();
						if (!(firstRow.getEndorsementType() == null || firstRow.getEndorsementType() == 0)) {

							// search
							if (reqList.get(0).getEndtCount().compareTo(BigDecimal.ONE) == 0) {
								endtCount = reqList.get(0).getEndtCount();
								humanData1 = humanRepo.findBySectionIdAndPolicyNoAndStatusNot(sectionId,
										reqList.get(0).getOriginalPolicyNo(), "D");
							} else {
								endtCount = reqList.get(0).getEndtCount().subtract(BigDecimal.ONE);
								humanData1 = humanRepo.findByEndtCountAndSectionIdAndOriginalPolicyNoAndStatusNot(endtCount,
										sectionId, reqList.get(0).getOriginalPolicyNo(), "D");
							}

							// delete all
							humanRepo.deleteAll(humanDatas);

						}
						List<EserviceCommonDetails> humanDatas2 = new ArrayList<EserviceCommonDetails>();
						dozerMapper.map(humanDatas.get(0), humanData);
						String policyNo = humanDatas.get(0).getPolicyNo();
						humanData1.forEach(hum -> {
							EserviceCommonDetails o = new EserviceCommonDetails();
							dozerMapper.map(hum, o);
							o.setStatus("D");
							o.setRequestReferenceNo(refNo);
							o.setQuoteNo("");
							o.setPolicyNo(policyNo);
							if (!(firstRow.getEndorsementType() == null || firstRow.getEndorsementType() == 0))

							{

								o.setOriginalPolicyNo(firstRow.getOriginalPolicyNo());
								o.setEndorsementDate(firstRow.getEndorsementDate());
								o.setEndorsementRemarks(firstRow.getEndorsementRemarks());
								o.setEndorsementEffdate(firstRow.getEndorsementEffdate());
								o.setEndtPrevPolicyNo(firstRow.getEndtPrevPolicyNo());
								o.setEndtPrevQuoteNo(firstRow.getEndtPrevQuoteNo());
								o.setEndtCount(firstRow.getEndtCount());
								o.setEndtStatus(firstRow.getEndtStatus());
								o.setIsFinyn(firstRow.getIsFinaceYn());
								o.setEndtCategDesc(firstRow.getEndtCategDesc());
								o.setEndorsementType(firstRow.getEndorsementType());
								o.setEndorsementTypeDesc(firstRow.getEndorsementTypeDesc());

							}

							humanDatas2.add(o);
						});
						humanRepo.saveAllAndFlush(humanDatas2);

					}

					sectionData = eserSecRepo.findByRequestReferenceNoAndRiskIdAndProductIdAndSectionId(refNo, 1, productId,
							sectionId);

					List<EserviceCommonDetails> saveHumanList = new ArrayList<EserviceCommonDetails>();
					int ageprinc = 0;
					int grpid = 0;

					List<ProductGroupMaster> Grp = pgmrepo.findByCompanyIdAndProductId(companyId, Integer.valueOf(productId));

					// get principle age

					for (int i = 0; i < reqList.size(); i++) {

						if (reqList.get(i).getRiskId().equals("1")) {
							Date dob = null;
							if (reqList.get(i).getDateOfBirth() != null) {
								dob = reqList.get(i).getDateOfBirth();
								Date today = new Date();
								ageprinc = today.getYear() - dob.getYear();
							}
						}
					}
					// Get Groupid based on the agefrom and ageto
					for (ProductGroupMaster data : Grp) {
						if (ageprinc >= Integer.valueOf(data.getGroupFrom())
								&& ageprinc <= Integer.valueOf(data.getGroupTo())) {
							grpid = data.getGroupId();
							break;
						}

					}
//							     for(int i=0;i<reqList.size();i++)
//							     {
//							    	 for(int j=0;j<length;j++) {
//							    	 EserviceCommonDetails saveHuman = new EserviceCommonDetails();
//							    		saveHuman = dozerMapper.map(humanData, EserviceCommonDetails.class)	;
//							    		saveHuman.setRequestReferenceNo(reqList.get(i).getRequestReferenceNo());
//							    		saveHuman.setRiskId(Integer.valueOf(reqList.get(i).getFamilyDetails().get(j).getRiskId())) ;
//							    		saveHuman.setSectionId(reqList.get(i).getSectionId()); 
//							    		saveHuman.setSectionName(sectionData!=null ? sectionData.getSectionName() : "" );    ;
//							    		saveHuman.setCreatedBy(reqList.get(i).getCreatedBy());
//		           			    	
//										saveHuman.setEmpLiabilitySi( new BigDecimal(0) )  ;
//										saveHuman.setSumInsured(new BigDecimal(0) );
//										saveHuman.setCount(1);
//										saveHuman.setOccupationType(reqList.get(i).getFamilyDetails().get(j).getRiskId());
//										// Age Calculation
//										int age = 0 ;
//										Date dob = null;
//										if (reqList.get(i).getFamilyDetails().get(j).getDateOfBirth() !=null) {
//											dob = reqList.get(i).getFamilyDetails().get(j).getDateOfBirth();
//											Date today = new Date();
//											age = today.getYear() - dob.getYear();
//										}
//										saveHuman.setAge(age);
//										saveHuman.setNickName(reqList.get(i).getFamilyDetails().get(j).getNickName());
//										saveHuman.setRelationType(reqList.get(i).getFamilyDetails().get(j).getRelationType());
//										saveHuman.setDob(reqList.get(i).getFamilyDetails().get(j).getDateOfBirth());
//										if(StringUtils.isNotBlank(reqList.get(i).getFamilyDetails().get(j).getRelationType()) ) {
//											String relationType = getListItem (companyId , "99999" ,"RATING_RELATION_TYPE",reqList.get(i).getFamilyDetails().get(j).getRelationType() ); 
//											saveHuman.setRelationTypeDesc(relationType);
//											saveHuman.setOccupationDesc(relationType + " - " + reqList.get(i).getFamilyDetails().get(j).getNickName());
//											
//										}
//										saveHuman.setGroupId(grpid);
//										saveHuman.setCustomerReferenceNo(sectionData.getCustomerReferenceNo());//a
//									
//										// Lc Calculation
//										BigDecimal exchangeRate = humanData.getExchangeRate()!=null ? humanData.getExchangeRate() : BigDecimal.ZERO ;
//										if(!(reqList.get(i).getEndorsementType()==null || reqList.get(j).getEndorsementType()==0))
//											
//										 {
//										  
//											 saveHuman.setOriginalPolicyNo(reqList.get(i).getOriginalPolicyNo());
//											 saveHuman.setEndorsementDate(reqList.get(i).getEndorsementDate());
//											 saveHuman.setEndorsementRemarks(reqList.get(i).getEndorsementRemarks());
//											 saveHuman.setEndorsementEffdate(reqList.get(i).getEndorsementEffdate());
//											 saveHuman.setEndtPrevPolicyNo(reqList.get(i).getEndtPrevPolicyNo());
//											 saveHuman.setEndtPrevQuoteNo(reqList.get(i).getEndtPrevQuoteNo());
//											 saveHuman.setEndtCount(reqList.get(i).getEndtCount());
//											 saveHuman.setEndtStatus(reqList.get(i).getEndtStatus());
//											 saveHuman.setIsFinyn(reqList.get(i).getIsFinaceYn());
//											 saveHuman.setEndtCategDesc(reqList.get(i).getEndtCategDesc());
//											 saveHuman.setEndorsementType(reqList.get(i).getEndorsementType());
//											 saveHuman.setEndorsementTypeDesc(reqList.get(i).getEndorsementTypeDesc()); 
//											 saveHuman.setStatus("E");					
//										 }
//										saveHumanList.add(saveHuman);
//							     }
					humanRepo.deleteByRequestReferenceNoAndOccupationDescIsNotNull(refNo);
					for (SlideHealthInsureSaveReq data : reqList) {
						// Save Human
						EserviceCommonDetails saveHuman = new EserviceCommonDetails();
						saveHuman = dozerMapper.map(humanData, EserviceCommonDetails.class);
						saveHuman.setRequestReferenceNo(data.getRequestReferenceNo());
						saveHuman.setRiskId(Integer.valueOf(data.getRiskId()));
						saveHuman.setSectionId(data.getSectionId());
						saveHuman.setSectionName(sectionData != null ? sectionData.getSectionName() : "");
						;
						saveHuman.setCreatedBy(data.getCreatedBy());
//						saveHuman.setEmpLiabilitySi(new BigDecimal(0));
						saveHuman.setSumInsured(new BigDecimal(0));
						saveHuman.setCount(1);
						saveHuman.setOccupationType(data.getRiskId());

						// Age Calculation
						int age = 0;
						Date dob = null;
						if (data.getDateOfBirth() != null) {
							dob = data.getDateOfBirth();
							Date today = new Date();
							age = today.getYear() - dob.getYear();
						}
						saveHuman.setAge(age);

						saveHuman.setNickName(data.getNickName());
						saveHuman.setRelationType(data.getRelationType());
						saveHuman.setDob(data.getDateOfBirth());
						if (StringUtils.isNotBlank(data.getRelationType())) {
							String relationType = getListItem(companyId, "99999", "RATING_RELATION_TYPE",
									data.getRelationType());
							saveHuman.setRelationTypeDesc(relationType);
							saveHuman.setOccupationDesc(relationType + " - " + data.getNickName());

						}
						saveHuman.setGroupId(grpid);
						// Lc Calculation
						BigDecimal exchangeRate = humanData.getExchangeRate() != null ? humanData.getExchangeRate()
								: BigDecimal.ZERO;

						// Endorsement CHanges
						if (!(data.getEndorsementType() == null || data.getEndorsementType() == 0))

						{

							saveHuman.setOriginalPolicyNo(data.getOriginalPolicyNo());
							saveHuman.setEndorsementDate(data.getEndorsementDate());
							saveHuman.setEndorsementRemarks(data.getEndorsementRemarks());
							saveHuman.setEndorsementEffdate(data.getEndorsementEffdate());
							saveHuman.setEndtPrevPolicyNo(data.getEndtPrevPolicyNo());
							saveHuman.setEndtPrevQuoteNo(data.getEndtPrevQuoteNo());
							saveHuman.setEndtCount(data.getEndtCount());
							saveHuman.setEndtStatus(data.getEndtStatus());
							saveHuman.setIsFinyn(data.getIsFinaceYn());
							saveHuman.setEndtCategDesc(data.getEndtCategDesc());
							saveHuman.setEndorsementType(data.getEndorsementType());
							saveHuman.setEndorsementTypeDesc(data.getEndorsementTypeDesc());
							saveHuman.setStatus("E");
						}
						saveHumanList.add(saveHuman);

					}
					humanRepo.saveAllAndFlush(saveHumanList);

					List<EserviceCommonDetails> esercommonDatas = eserCommonRepo
							.findByRequestReferenceNoAndSectionId(humanData.getRequestReferenceNo(), humanData.getSectionId());
					if (esercommonDatas.size() == 0) {
						esercommonDatas = saveHumanList;
					}
					for (EserviceCommonDetails data : esercommonDatas) {
						// Section Req
						BuildingSectionRes sec = new BuildingSectionRes();
						sec.setMotorYn(sectionData.getProductType());
						sec.setSectionId(sectionData.getSectionId());
						sec.setSectionName(sectionData.getSectionName());
						sec.setOccupationId(data.getOccupationType());
						sectionList.add(sec);
					}

				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());

				}
				try {
					List<OneTimeTableRes> otResList = null;
					// One Time Table Thread Call
					OneTimeTableReq otReq = new OneTimeTableReq();
					SlideHealthInsureSaveReq req = reqList.get(0);
					otReq.setRequestReferenceNo(req.getRequestReferenceNo());
					otReq.setVehicleId(1);
					otReq.setBranchCode(saveData != null ? saveData.getBranchCode() : humanData.getBranchCode());
					otReq.setInsuranceId(saveData != null ? saveData.getCompanyId() : humanData.getCompanyId());

					otReq.setProductId(Integer.valueOf(req.getProductId()));
					otReq.setSectionList(sectionList);

					otResList = otService.call_OT_Insert(otReq);
					for (OneTimeTableRes otRes : otResList) {
						SlideSectionSaveRes res = new SlideSectionSaveRes();
						res.setResponse("Saved Successfully");
						res.setRiskId(otRes.getVehicleId());
						res.setVdRefNo(otRes.getVdRefNo());
						res.setCdRefNo(otRes.getCdRefNo());
						res.setMsrefno(otRes.getMsRefNo());
						res.setCompanyId(otRes.getCompanyId());
						res.setProductId(otRes.getProductId());
						res.setSectionId(otRes.getSectionId());
						res.setCustomerReferenceNo(
								saveData != null ? saveData.getCustomerReferenceNo() : humanData.getCustomerReferenceNo());
						res.setRequestReferenceNo(
								saveData != null ? saveData.getRequestReferenceNo() : humanData.getRequestReferenceNo());
						res.setCreatedBy(reqList.get(0).getCreatedBy());
						resList.add(res);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				return resList;
			}

			public SuccessRes saveadditionalinfoHI(SaveAddinfoHI req) {
				int count = 0;
				SuccessRes res = new SuccessRes();
				ModelMapper modelMapper = new ModelMapper();
				modelMapper.getConfiguration().setAmbiguityIgnored(true);
				try {

					List<SlideHIFamilyDetailsReq> reqList = req.getFamilyDetails();
					// Primary Tables & Details
					String quoteNo = req.getQuoteNo();
					Integer sectionId = Integer.valueOf(req.getSectionId());
					Integer lastPassCount = Integer.valueOf(productEmplRepo.countByQuoteNo(quoteNo).toString());
					List<PolicyCoverData> coverList = coverRepo.findByQuoteNoAndSectionId(quoteNo, sectionId);
					List<PremiumGroupDevideRes> covers = new ArrayList<PremiumGroupDevideRes>();
					Type listType = new TypeToken<List<PremiumGroupDevideRes>>() {
					}.getType();
					covers = modelMapper.map(coverList, listType);
					List<CommonDataDetails> groupList = commonRepo.findByQuoteNoAndSectionIdAndStatusNot(quoteNo,
							sectionId.toString(), "D");
					HomePositionMaster homeData = homeRepo.findByQuoteNo(quoteNo);

					// Grouping
					Map<String, List<SlideHIFamilyDetailsReq>> groupByGroupId = reqList.stream()
							.filter(o -> o.getRiskId() != null)
							.collect(Collectors.groupingBy(SlideHIFamilyDetailsReq::getRiskId));
					Map<Integer, List<PremiumGroupDevideRes>> coverGroupByGroupId = covers.stream()
							.filter(o -> o.getVehicleId() != null)
							.collect(Collectors.groupingBy(PremiumGroupDevideRes::getVehicleId));

					// Framing List
					List<PolicyCoverDataIndividuals> totalIndiCovers = new ArrayList<PolicyCoverDataIndividuals>();

					List<ProductEmployeeDetails> saveList = new ArrayList<ProductEmployeeDetails>();

					// Delete Duplicates
					// 1)check Duplicates
					List<ProductEmployeeDetails> empdel = productEmplRepo.findByQuoteNoAndSectionIdAndProductIdAndCompanyId(
							quoteNo, req.getSectionId(), Integer.valueOf(req.getProductId()), req.getInsuranceId());
					// 2)delete Duplicates
					if (empdel.size() > 0) {
						for (ProductEmployeeDetails del : empdel) {
							productEmplRepo.deleteByQuoteNoAndRiskIdAndSectionId(quoteNo, del.getRiskId(), req.getSectionId());
						}
					}

					for (String group : groupByGroupId.keySet()) {

						// List<ProductEmployeeSaveReq> filterReqPass = groupByGroupId.get(group);
						List<SlideHIFamilyDetailsReq> filterReqPass = groupByGroupId.get(group);
						List<PremiumGroupDevideRes> groupCovers = coverGroupByGroupId.get(Integer.valueOf(group));
						List<CommonDataDetails> filterGroupList = groupList.stream()
								.filter(o -> o.getRiskId().equals(Integer.valueOf(group))).collect(Collectors.toList());
						CommonDataDetails groupData = new CommonDataDetails();
						if (filterGroupList.size() > 0) {
							groupData = filterGroupList.get(0);
						}

						for (SlideHIFamilyDetailsReq data : filterReqPass) {

							ProductEmployeeDetails saveData = new ProductEmployeeDetails();
							Integer empId = 0;

							if (StringUtils.isNotBlank(data.getEmployeeId())) {// data.getEmployeeId()
								empId = Integer.valueOf(data.getEmployeeId());// data.getEmployeeId()
							} else {
								lastPassCount = lastPassCount + 1;
								empId = lastPassCount;
							}
							Integer passCount = empId;

							// Employee Details
							saveData.setSectionId(sectionId.toString());
							saveData.setDateOfBirth(data.getDateOfBirth());
							// saveData.setDateOfJoiningYear(Integer.valueOf(data.getDateOfJoiningYear()) );
							// saveData.setDateOfJoiningMonth(data.getDateOfJoiningMonth());
							saveData.setCompanyId(homeData.getCompanyId());
							saveData.setCreatedBy(req.getCreatedBy());
							saveData.setEmployeeId(Long.valueOf(data.getRiskId()));
							saveData.setEmployeeName(data.getFirstName().concat(" ").concat(data.getLastName()));
							saveData.setEntryDate(new Date());
							// saveData.setOccupationId(data.getOccupationId());
							// saveData.setOccupationDesc(data.getOccupationDesc());
							saveData.setProductId(homeData.getProductId());
							saveData.setQuoteNo(quoteNo);
							saveData.setRequestReferenceNo(homeData.getRequestReferenceNo());
							saveData.setRiskId(Integer.valueOf(data.getRiskId()));
							saveData.setLocationId(Integer.valueOf(data.getRiskId()));
							saveData.setStatus("Y");
							// saveData.setSalary(new BigDecimal(data.getSalary()));
							saveData.setNationalityId(data.getNationalityId());
							saveData.setProductDesc(homeData.getProductName());
							// saveData.setAddress(data.getAddress());
							saveData.setSectionDesc(groupData.getSectionDesc());
							// saveData.setLocationName(data.getLocationName());

							// Premium Details
							saveData.setPolicyStartDate(groupData.getPolicyStartDate());
							saveData.setPolicyEndDate(groupData.getPolicyEndDate());
							/*
							 * BigDecimal individualSuminsured = new
							 * BigDecimal(data.getSalary());//data.getSalary() BigDecimal sharePercent =
							 * groupData.getSumInsured().divide(individualSuminsured
							 * ,4,RoundingMode.HALF_UP); List<PremiumGroupDevideRes> getDividedCovers =
							 * getDevidedCovers(quoteNo , sharePercent, groupCovers ) ;
							 * //List<PremiumGroupDevideRes> getDividedCovers = getDevidedCovers(quoteNo ,
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
							 */
			/*				saveList.add(saveData);

							// Covers
							/*
							 * List<PolicyCoverDataIndividuals> indiCovers = new
							 * ArrayList<PolicyCoverDataIndividuals>(); Type listType2 = new
							 * TypeToken<List<PolicyCoverDataIndividuals>>(){}.getType(); indiCovers =
							 * modelMapper.map(getDividedCovers,listType2); for( PolicyCoverDataIndividuals
							 * o : indiCovers ) { o.setGroupId(groupData.getRiskId());
							 * o.setGroupCount(groupData.getCount()); o.setVehicleId(passCount);
							 * o.setIndividualId(passCount); } totalIndiCovers.addAll(indiCovers);
							 */
			/*				count++;
						}

					}

				// Save All Passengers
					List<Integer> passengerIds = new ArrayList<Integer>();
					List<Long> passengerId2 = new ArrayList<Long>();
					saveList.forEach(o -> {
						passengerIds.add(Integer.valueOf(o.getEmployeeId().toString()));
						passengerId2.add(o.getEmployeeId());
					});

					productEmplRepo.saveAllAndFlush(saveList);

					// Save Divided Indi Covers

					// Remove Deactivated Covers
					/*
					 * Long count1 = indiCoverRepo.countByQuoteNoAndSectionIdAndVehicleIdIn(quoteNo
					 * ,sectionId,passengerIds); if(count1 > 0 ) {
					 * indiCoverRepo.deleteByQuoteNoAndSectionIdAndVehicleIdIn(quoteNo
					 * ,sectionId,passengerIds); } indiCoverRepo.saveAllAndFlush(totalIndiCovers);
					 */
					// updateRemovedPassengers(quoteNo ,passengerIds ,sectionId ,
					// groupList.get(0).getPolicyStartDate()) ;
					// indiCoverRepo.saveAllAndFlush(totalIndiCovers);

					// Copy Old Doc
				/*	OldDocumentsCopyReq oldDocCopyReq = new OldDocumentsCopyReq();
					oldDocCopyReq.setInsuranceId(homeData.getCompanyId());
					oldDocCopyReq.setProductId(homeData.getProductId().toString());
					oldDocCopyReq.setSectionId(sectionId.toString());
					oldDocCopyReq.setQuoteNo(quoteNo);
					docService.copyOldDocumentsToNewQuote(oldDocCopyReq);

					// uploaded Documents delete

					List<ProductEmployeeDetails> pass = productEmplRepo.findByQuoteNoAndSectionId(quoteNo,
							sectionId.toString());

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

					res.setResponse("Saved Successfully");
					res.setSuccessId("");

				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());
					return null;
				}

				return res;

			}

			private List<PremiumGroupDevideRes> getDevidedCovers(String quoteNo, BigDecimal sharePercent,
					List<PremiumGroupDevideRes> groupCovers) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public List<HealthInsureGetRes> getHealthInsure(SlideSectionGetReq req) {
				List<HealthInsureGetRes> resList = new ArrayList<HealthInsureGetRes>();
				try {
					List<String> statusNot = new ArrayList<String>();
					statusNot.add("D");

					List<EserviceCommonDetails> humanData = humanRepo.findByRequestReferenceNoAndSectionIdAndStatusNotIn(
							req.getRequestReferenceNo(), req.getSectionId(), statusNot);
					int i = humanData.size();
					if (i == 0) {
						return resList;
					}
					List<ProductEmployeeDetails> ped = productEmplRepo.findByQuoteNoAndSectionIdAndProductIdAndCompanyId(
							humanData.get(0).getQuoteNo(), req.getSectionId(), Integer.valueOf(humanData.get(0).getProductId()),
							req.getInsuranceId());

					int count = 0;

					int j = ped.size();

					// checking both records to avoid ArrayIndexOutBound Exception
					if (j == i) {
						for (ProductEmployeeDetails ped1 : ped) {

							if (ped.size() > 0) {
								HealthInsureGetRes res = new HealthInsureGetRes();
								String[] fullname = ped1.getEmployeeName().split(" ");
								System.out.println("FirstName--" + fullname[0]);
								System.out.println("Lastname--" + fullname[1]);
								res.setCreatedBy(humanData.get(count).getCreatedBy());
								res.setInsuranceId(humanData.get(count).getCompanyId());
								res.setProductId(humanData.get(count).getProductId());
								res.setRequestReferenceNo(humanData.get(count).getRequestReferenceNo());
								res.setRiskId(humanData.get(count).getRiskId() == null ? ""
										: humanData.get(count).getRiskId().toString());
								res.setSectionId(req.getSectionId());
								res.setRelationType(humanData.get(count).getRelationType());
								res.setRelationTypeDesc(humanData.get(count).getRelationTypeDesc());
								res.setDateOfBirth(humanData.get(count).getDob());
								res.setNickName(humanData.get(count).getNickName());
								res.setFirstName(fullname[0]);
								res.setLastName(fullname[1]);
								res.setNationalityId(ped.get(count).getNationalityId());
								resList.add(res);
								count++;
							}
						}
					}

					else {

						for (EserviceCommonDetails humanDatas : humanData) {
							HealthInsureGetRes res = new HealthInsureGetRes();
							res.setCreatedBy(humanDatas.getCreatedBy());
							res.setInsuranceId(humanDatas.getCompanyId());
							res.setProductId(humanDatas.getProductId());
							res.setRequestReferenceNo(humanDatas.getRequestReferenceNo());
							res.setRiskId(humanDatas.getRiskId() == null ? "" : humanDatas.getRiskId().toString());
							res.setSectionId(req.getSectionId());
							res.setRelationType(humanDatas.getRelationType());
							res.setRelationTypeDesc(humanDatas.getRelationTypeDesc());
							res.setDateOfBirth(humanDatas.getDob());
							res.setNickName(humanDatas.getNickName());

							resList.add(res);
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());
					return null;

				}
				return resList;
			}

			@Override
			public List<HealthInsureGetRes> getHumantype(ProductLevelReq req1) {
				List<HealthInsureGetRes> resList = new ArrayList<HealthInsureGetRes>();
				try {

					Integer productid = Integer.parseInt(req1.getProductId());
					String quoteno = "";
					String insurance_id = req1.getInsuranceId();
					String sectionid = "";
					for (SectionLevelReq req : req1.getSectionLevelReq()) {

						int count = 0;
						List<String> statusNot = new ArrayList<String>();
						statusNot.add("D");

						List<EserviceCommonDetails> humanData = humanRepo.findByRequestReferenceNoAndSectionIdAndStatusNotIn(
								req.getRequestReferenceNo(), req.getSectionId(), statusNot);
						int i = humanData.size();
						if (i == 0) {
							return resList;
						}
						quoteno = humanData.get(count).getQuoteNo();
						sectionid = req.getSectionId();
						List<ProductEmployeeDetails> ped = productEmplRepo.findByQuoteNoAndSectionId(quoteno, sectionid);

						int j = ped.size();

						// checking both records to avoid ArrayIndexOutBound Exception
						if (j == i) {
							for (ProductEmployeeDetails ped1 : ped) {

								if (ped.size() > 0) {
									HealthInsureGetRes res = new HealthInsureGetRes();
									String[] fullname = ped1.getEmployeeName().split(" ");
									System.out.println("FirstName--" + fullname[0]);
									System.out.println("Lastname--" + fullname[1]);
									res.setCreatedBy(humanData.get(count).getCreatedBy());
									res.setInsuranceId(humanData.get(count).getCompanyId());
									res.setProductId(humanData.get(count).getProductId());
									res.setRequestReferenceNo(humanData.get(count).getRequestReferenceNo());
									res.setRiskId(humanData.get(count).getRiskId() == null ? ""
											: humanData.get(count).getRiskId().toString());
									res.setSectionId(req.getSectionId());
									res.setRelationType(humanData.get(count).getRelationType());
									res.setRelationTypeDesc(humanData.get(count).getRelationTypeDesc());
									res.setDateOfBirth(humanData.get(count).getDob());
									res.setNickName(humanData.get(count).getNickName());
									res.setFirstName(fullname[0]);
									res.setLastName(fullname[1]);
									res.setNationalityId(ped.get(count).getNationalityId());
									res.setOccupationId(humanData.get(count).getOccupationType());
									res.setInternitytype(humanData.get(count).getIndemnityType());
									res.setInternityDesc(humanData.get(count).getIndemnityTypeDesc());
									res.setProfessionaltype(humanData.get(count).getProfessionalType());
									res.setProfessionalDesc(humanData.get(count).getProfessionalTypeDesc());
									if (humanData.get(count).getSectionId().equals("106")) {
										res.setGrossIncome(humanData.get(count).getSumInsured());
//										res.setIndernitysi(humanData.get(count).getIndemnitySuminsured().doubleValue());
									} else {
										res.setGrossIncome(null);
										res.setIndernitysi(null);
									}
									res.setEmployeeCount(humanData.get(count).getTotalNoOfEmployees());
									resList.add(res);
									count++;
								}
							}
						}

						else {

							for (EserviceCommonDetails humanDatas : humanData) {
								HealthInsureGetRes res = new HealthInsureGetRes();
								res.setCreatedBy(humanDatas.getCreatedBy());
								res.setInsuranceId(humanDatas.getCompanyId());
								res.setProductId(humanDatas.getProductId());
								res.setRequestReferenceNo(humanDatas.getRequestReferenceNo());
								res.setRiskId(humanDatas.getRiskId() == null ? "" : humanDatas.getRiskId().toString());
								res.setSectionId(req.getSectionId());
								res.setRelationType(humanDatas.getRelationType());
								res.setRelationTypeDesc(humanDatas.getRelationTypeDesc());
								res.setDateOfBirth(humanDatas.getDob());
								res.setNickName(humanDatas.getNickName());
								res.setOccupationId(humanDatas.getOccupationType());
								res.setInternitytype(humanDatas.getIndemnityType());
								res.setInternityDesc(humanDatas.getIndemnityTypeDesc());
								res.setProfessionaltype(humanDatas.getProfessionalType());
								res.setProfessionalDesc(humanDatas.getProfessionalTypeDesc());

								if (humanDatas.getSectionId().equals("106")) {
									res.setGrossIncome(humanDatas.getSumInsured());
//									res.setIndernitysi(humanDatas.getIndemnitySuminsured().doubleValue());
								} else {
									res.setGrossIncome(null);
									res.setIndernitysi(null);
								}
								res.setEmployeeCount(humanDatas.getTotalNoOfEmployees());
								resList.add(res);
							}
						}

					}
				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());
					return null;

				}
				return resList;
			}

			public List<SlideSectionSaveRes> saveprofindernity(ProfessionalIndeminityReq reqLists) {
				// TODO Auto-generated method stub

				List<EmployeeDetailsReq> reqList = updateRequest(reqLists);

				List<SlideSectionSaveRes> resList = new ArrayList<SlideSectionSaveRes>();
				EserviceBuildingDetails saveData = new EserviceBuildingDetails();
				List<EserviceCommonDetails> humanDatas = new ArrayList<EserviceCommonDetails>();
				EserviceCommonDetails humanData = new EserviceCommonDetails();
				List<EserviceSectionDetails> sectionDataList = new ArrayList<EserviceSectionDetails>();
				DozerBeanMapper dozerMapper = new DozerBeanMapper();
				List<BuildingSectionRes> sectionList = new ArrayList<BuildingSectionRes>();
				List<String> sectionIdList = new ArrayList<String>();

				try {
					BigDecimal endtCount = BigDecimal.ZERO;
					String refNo = reqList.get(0).getRequestReferenceNo();
					String productId = reqList.get(0).getProductId();
					String sectionId = reqList.get(0).getSectionId();
					String company_id = reqList.get(0).getInsuranceId();
					String branchCode = "";

					saveData = buildingRepo.findByRequestReferenceNoAndRiskIdAndSectionId(refNo, 1, "0");
					if (saveData != null) {

						dozerMapper.map(saveData, humanData);
						humanData.setPolicyPeriod(saveData.getPolicyPeriord());

					}

					humanDatas = humanRepo.findByRequestReferenceNoAndSectionId(refNo, sectionId);
					if (humanDatas.isEmpty()) {
						humanDatas = humanRepo.findByRequestReferenceNo(refNo);
					}
					branchCode = humanDatas.get(0).getBranchCode();
					EmployeeDetailsReq firstRow = reqList.get(0);

					if ((firstRow.getEndorsementType() == null || firstRow.getEndorsementType() == 0)
							&& humanDatas.size() > 0) {
						humanRepo.deleteAll(humanDatas);
						dozerMapper.map(humanDatas.get(0), humanData);
					} else if (humanDatas.size() > 0) {
						List<EserviceCommonDetails> humanData1 = new ArrayList<EserviceCommonDetails>();
						if (!(firstRow.getEndorsementType() == null || firstRow.getEndorsementType() == 0)) {

							// search
							if (reqList.get(0).getEndtCount().compareTo(BigDecimal.ONE) == 0) {
								endtCount = reqList.get(0).getEndtCount();
								humanData1 = humanRepo.findBySectionIdAndPolicyNoAndStatusNot(sectionId,
										reqList.get(0).getOriginalPolicyNo(), "D");
							} else {
								endtCount = reqList.get(0).getEndtCount().subtract(BigDecimal.ONE);
								humanData1 = humanRepo.findByEndtCountAndSectionIdAndOriginalPolicyNoAndStatusNot(endtCount,
										sectionId, reqList.get(0).getOriginalPolicyNo(), "D");
							}

							// delete all
							humanRepo.deleteAll(humanDatas);

						}
						List<EserviceCommonDetails> humanDatas2 = new ArrayList<EserviceCommonDetails>();
						dozerMapper.map(humanDatas.get(0), humanData);
						String policyNo = humanDatas.get(0).getPolicyNo();
						humanData1.forEach(hum -> {
							EserviceCommonDetails o = new EserviceCommonDetails();
							dozerMapper.map(hum, o);
							o.setStatus("D");
							o.setRequestReferenceNo(refNo);
							o.setQuoteNo("");
							o.setPolicyNo(policyNo);
							if (!(firstRow.getEndorsementType() == null || firstRow.getEndorsementType() == 0))

							{

								o.setOriginalPolicyNo(firstRow.getOriginalPolicyNo());
								o.setEndorsementDate(firstRow.getEndorsementDate());
								o.setEndorsementRemarks(firstRow.getEndorsementRemarks());
								o.setEndorsementEffdate(firstRow.getEndorsementEffdate());
								o.setEndtPrevPolicyNo(firstRow.getEndtPrevPolicyNo());
								o.setEndtPrevQuoteNo(firstRow.getEndtPrevQuoteNo());
								o.setEndtCount(firstRow.getEndtCount());
								o.setEndtStatus(firstRow.getEndtStatus());
								o.setIsFinyn(firstRow.getIsFinaceYn());
								o.setEndtCategDesc(firstRow.getEndtCategDesc());
								o.setEndorsementType(firstRow.getEndorsementType());
								o.setEndorsementTypeDesc(firstRow.getEndorsementTypeDesc());

							}

							humanDatas2.add(o);
						});
						humanRepo.saveAllAndFlush(humanDatas2);

					}
					if (!reqList.isEmpty()) {
						for (int p = 0; p < reqList.size(); p++) {

							if ((!reqList.get(p).getSectionId().isEmpty()) && reqList != null) {

								sectionDataList.add(eserSecRepo.findByRequestReferenceNoAndRiskIdAndProductIdAndSectionId(refNo,
										1, productId, reqList.get(p).getSectionId()));

							}

						}
					}
					// save in table
					List<EserviceCommonDetails> saveHumanList = new ArrayList<EserviceCommonDetails>();
//					for (EmployeeDetailsReq data : reqList ) {
					for (int i = 0; i < reqList.size(); i++) {

						// Save Human
						EmployeeDetailsReq data = reqList.get(i);

						EserviceCommonDetails saveHuman = new EserviceCommonDetails();
						saveHuman = dozerMapper.map(humanData, EserviceCommonDetails.class);
						saveHuman.setRequestReferenceNo(data.getRequestReferenceNo());
						saveHuman.setRiskId(Integer.valueOf(data.getOccupationid()));
						saveHuman.setSectionId(data.getSectionId());
						// saveHuman.setSectionId(StringUtils.isEmpty(sectionDataList) ?
						// null:sectionDataList.get(i).getSectionName());
						// saveHuman.setSectionId(sectionDataList.isEmpty() ? null :
						// sectionDataList.get(i).getSectionId());

						saveHuman.setSectionName(sectionDataList != null ? sectionDataList.get(i).getSectionName() : "");
						;
						saveHuman.setCreatedBy(data.getCreatedBy());
						saveHuman.setSumInsured(StringUtils.isBlank(data.getGrossincome()) ? new BigDecimal(0)
								: new BigDecimal(data.getGrossincome()));
						saveHuman.setOccupationDesc(data.getOccupationDesc());
						saveHuman.setOccupationType(data.getOccupationid());

//						saveHuman.setIndemnitySuminsured(
//								StringUtils.isBlank(data.getIndemnitySi()) ? null : new BigDecimal(data.getIndemnitySi()));

						saveHuman.setOtherOccupation(data.getOccupationDesc());
						saveHuman.setTotalNoOfEmployees(
								StringUtils.isBlank(data.getEmployeecount()) ? null : Long.valueOf(data.getEmployeecount()));

						// BigDecimal exchangeRate = humanData.getExchangeRate()!=null ?
						// humanData.getExchangeRate() : BigDecimal.ZERO ;

						saveHuman.setIndemnityType(
								StringUtils.isBlank(data.getIndemnitytype()) ? null : data.getIndemnitytype());
						saveHuman.setProfessionalType(
								StringUtils.isBlank(data.getProfessionaltype()) ? null : data.getProfessionaltype());
						if (!StringUtils.isBlank(data.getProfessionaltype())) {
							String professionType = getListItem(data.getInsuranceId(), "99999", "PROFESSIONAL_TYPE",
									data.getProfessionaltype());
							saveHuman.setProfessionalTypeDesc(professionType);
						}

						if (!StringUtils.isBlank(data.getIndemnitytype())) {
							String indernityType = getListItem(data.getInsuranceId(), "99999", "PROFESSIONAL_INDEMNITY",
									data.getIndemnitytype());

							if (data.getProfessionaltype().equals("1")) {
								saveHuman.setIndemnityTypeDesc(indernityType);
							} else {
								saveHuman.setIndemnityTypeDesc(null);
							}
						}

						// Endorsement CHanges
						if (!(data.getEndorsementType() == null || data.getEndorsementType() == 0))

						{

							saveHuman.setOriginalPolicyNo(data.getOriginalPolicyNo());
							saveHuman.setEndorsementDate(data.getEndorsementDate());
							saveHuman.setEndorsementRemarks(data.getEndorsementRemarks());
							saveHuman.setEndorsementEffdate(data.getEndorsementEffdate());
							saveHuman.setEndtPrevPolicyNo(data.getEndtPrevPolicyNo());
							saveHuman.setEndtPrevQuoteNo(data.getEndtPrevQuoteNo());
							saveHuman.setEndtCount(data.getEndtCount());
							saveHuman.setEndtStatus(data.getEndtStatus());
							saveHuman.setIsFinyn(data.getIsFinaceYn());
							saveHuman.setEndtCategDesc(data.getEndtCategDesc());
							saveHuman.setEndorsementType(data.getEndorsementType());
							saveHuman.setEndorsementTypeDesc(data.getEndorsementTypeDesc());
							saveHuman.setStatus("E");
						}
						saveHumanList.add(saveHuman);

					}
					humanRepo.saveAllAndFlush(saveHumanList);

					List<EserviceCommonDetails> esercommonDatas = eserCommonRepo
							.findByRequestReferenceNo(humanData.getRequestReferenceNo());
					if (esercommonDatas.size() == 0) {
						esercommonDatas = saveHumanList;
					}
					for (int g = 0; g < esercommonDatas.size(); g++) {
						// Section Req
						BuildingSectionRes sec = new BuildingSectionRes();
						sec.setMotorYn(sectionDataList.get(g).getProductType());
						sec.setSectionId(sectionDataList.get(g).getSectionId());
						sec.setSectionName(sectionDataList.get(g).getSectionName());

						sec.setOccupationId(esercommonDatas.get(g).getOccupationType());
						sectionList.add(sec);
					}

				} catch (Exception e) {
					e.printStackTrace();
					log.info("Log Details" + e.getMessage());

				}
				try {
					List<OneTimeTableRes> otResList = null;
					// One Time Table Thread Call
					OneTimeTableReq otReq = new OneTimeTableReq();
					EmployeeDetailsReq req = reqList.get(0);
					otReq.setRequestReferenceNo(req.getRequestReferenceNo());
					otReq.setVehicleId(1);
					otReq.setBranchCode(saveData != null ? saveData.getBranchCode() : humanData.getBranchCode());
					otReq.setInsuranceId(saveData != null ? saveData.getCompanyId() : humanData.getCompanyId());
					otReq.setProductId(Integer.valueOf(req.getProductId()));
					otReq.setSectionList(sectionList);

					otResList = otService.call_OT_Insert(otReq);
					for (OneTimeTableRes otRes : otResList) {
						SlideSectionSaveRes res = new SlideSectionSaveRes();
						res.setResponse("Saved Successfully");
						res.setRiskId(otRes.getVehicleId());
						res.setVdRefNo(otRes.getVdRefNo());
						res.setCdRefNo(otRes.getCdRefNo());
						res.setMsrefno(otRes.getMsRefNo());
						res.setCompanyId(otRes.getCompanyId());
						res.setProductId(otRes.getProductId());
						res.setSectionId(otRes.getSectionId());
						res.setCustomerReferenceNo(
								saveData != null ? saveData.getCustomerReferenceNo() : humanData.getCustomerReferenceNo());
						res.setRequestReferenceNo(
								saveData != null ? saveData.getRequestReferenceNo() : humanData.getRequestReferenceNo());
						res.setCreatedBy(reqList.get(0).getCreatedBy());
						resList.add(res);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				return resList;
			}

			private List<EmployeeDetailsReq> updateRequest(ProfessionalIndeminityReq reqList) {

				List<EmployeeDetailsReq> empReqList = new ArrayList<EmployeeDetailsReq>();

				DozerBeanMapper mapper = new DozerBeanMapper();

				if (null != reqList) {

					if (null != reqList.getPrincipalData()) {

						EmployeeDetailsReq empReq = new EmployeeDetailsReq();
						mapper.map(reqList, empReq);
						mapper.map(reqList.getPrincipalData(), empReq);

						empReqList.add(empReq);

					}
					if (null != reqList.getProffesoionalStaffData()) {

						EmployeeDetailsReq empReq = new EmployeeDetailsReq();
						mapper.map(reqList, empReq);
						mapper.map(reqList.getProffesoionalStaffData(), empReq);

						empReqList.add(empReq);

					}
					if (null != reqList.getNonProffesoionalStaffData()) {

						EmployeeDetailsReq empReq = new EmployeeDetailsReq();
						mapper.map(reqList, empReq);
						mapper.map(reqList.getNonProffesoionalStaffData(), empReq);

						empReqList.add(empReq);

					}

				}
				for (int i = 0; i < empReqList.size(); i++) {
					EmployeeDetailsReq req = empReqList.get(i);
					if ("106".equals(req.getSectionId())) {
						req.setProfessionaltype("1");
					} else if ("107".equals(req.getSectionId())) {
						req.setProfessionaltype("2");
					} else if ("108".equals(req.getSectionId())) {
						req.setProfessionaltype("3");
					}
					// No need to add the modified object back to the list
				}
				return empReqList;
			}

			@Override
			public ResponseEntity<CommonResponse> saveAllSectionDetails(AllSectionSaveReq req, CommonResponse data,
					List<Object> objects) {

				DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
				boolean isBadRequest = true;
				List<BuildingDetailsSaveReq> buildinginfolist = new ArrayList<>();
				try {

					if (req == null) {
						data.setMessage("Failed - BadRequest");
						data.setError("request data is null");
						data.setIsError(true);

						List<Error> errorList = new ArrayList<>();
						Error error = new Error();
						error.setCode("400");
						error.setField("");
						error.setMessage("Check request Data");
						errorList.add(error);

						data.setErrorMessage(errorList);
						data.setCommonResponse(null);
						data.setErroCode("0");

						return ResponseEntity.badRequest().body(data);
					}

					// History Should Be Maintain

					if (StringUtils.isNotBlank(req.getRequestReferenceNo())) {

						List<EserviceBuildingDetails> buildingDataList = buildingRepo
								.findByRequestReferenceNo(req.getRequestReferenceNo());

						List<EserviceBuildingDetails> originalBuildingData = null;

						if (null != req && null != req.getBuildingData() && !req.getBuildingData().isEmpty()) { // update based
																												// on riskid

							List<Integer> riskIds = req.getBuildingData().stream()
									.map(a -> StringUtils.isNotBlank(a.getRiskId()) && a.getRiskId().matches("[0-9.]+")
											? Integer.valueOf(a.getRiskId())
											: 0)
									.collect(Collectors.toList());

							originalBuildingData = buildingRepo.findByRequestReferenceNoAndSectionIdAndRiskIdNotIn(
									req.getRequestReferenceNo(), "1", riskIds);

						}

						List<EserviceCommonDetails> humanDataList = humanRepo
								.findByRequestReferenceNo(req.getRequestReferenceNo());

						if (null != originalBuildingData && !originalBuildingData.isEmpty()) {

							originalBuildingData.stream().forEach(a -> {

								if (StringUtils.isNotBlank(a.getSectionId()) && a.getSectionId().equals("1")
										&& null != a.getRiskId() && a.getRiskId() == 1) {

//									a.setBuildingSuminsured(null);
//									a.setBuildingSuminsuredLc(null);

									buildingRepo.saveAndFlush(a);
								} else {

									buildingRepo.delete(a);
								}

							});

						}

						if (null != buildingDataList && !buildingDataList.isEmpty()) {

							if (null == req.getBuildingData()) {

								buildingDataList.stream().forEach(a -> {

									if (StringUtils.isNotBlank(a.getSectionId()) && a.getSectionId().equals("1")
											&& null != a.getRiskId() && a.getRiskId() == 1) {
//										a.setBuildingSuminsured(null);
//										a.setBuildingSuminsuredLc(null);

										buildingRepo.saveAndFlush(a);
									} else if (StringUtils.isNotBlank(a.getSectionId()) && a.getSectionId().equals("1")) {

										buildingRepo.delete(a);
									}

								});

							}
						}

						if (null != buildingDataList && !buildingDataList.isEmpty()) {

							if (null == req.getFireAndAlliedPerills()) {
								List<ProductSectionMaster> sectionid = productmaster
										.findByProductIdAndCompanyId(Integer.valueOf(req.getProductId()), req.getInsuranceId());
								List<Long> sectionIds = sectionid.stream().mapToLong(ProductSectionMaster::getSectionId).boxed()
										.collect(Collectors.toList());

								buildingDataList.stream().filter(
										a -> StringUtils.isNotBlank(a.getSectionId()) && sectionIds.contains(a.getSectionId())) // Check
																																// if
																																// sectionId
																																// exists
																																// in
																																// sectionIds
										.forEach(a -> {
											if (null != a.getRiskId() && a.getRiskId() == 1) {
//												a.setBuildingSuminsured(null);
//												a.setBuildingSuminsuredLc(null);
												buildingRepo.saveAndFlush(a); // Save the modified object
											} else {
												buildingRepo.delete(a); // Delete if riskId != 1 and sectionId in sectionIds
											}

										});

							}
						}

						if (null != buildingDataList && !buildingDataList.isEmpty()) {

							if (null == req.getContentData()) {
								buildingDataList.stream().forEach(a -> {

									if (StringUtils.isNotBlank(a.getSectionId()) && a.getSectionId().equals("47")
											&& null != a.getRiskId() && a.getRiskId() == 1) {
//										a.setContentSuminsured(null);
		//
										buildingRepo.saveAndFlush(a);
									} else if (StringUtils.isNotBlank(a.getSectionId()) && a.getSectionId().equals("47")) {

										buildingRepo.delete(a);
									}

								});

							}

							if (null == req.getElectronicEquipment()) {
								buildingDataList.stream().forEach(a -> {

									if (StringUtils.isNotBlank(a.getSectionId()) && a.getSectionId().equals("76")
											&& null != a.getRiskId() && a.getRiskId() == 1) {
//										a.setContentSuminsured(null);
//										a.setContentSuminsuredLc(null);

										buildingRepo.saveAndFlush(a);
									} else if (StringUtils.isNotBlank(a.getSectionId()) && a.getSectionId().equals("76")) {

										buildingRepo.delete(a);
									}

								});

							}

							if (null == req.getAllRiskData()) {

								buildingDataList.stream().forEach(a -> {

									if (StringUtils.isNotBlank(a.getSectionId()) && a.getSectionId().equals("3")) {

//										a.setAllriskSuminsured(null);
//										a.setAllriskSuminsuredLc(null);

										buildingRepo.saveAndFlush(a);
									} else if (StringUtils.isNotBlank(a.getSectionId()) && a.getSectionId().equals("3")) {

										buildingRepo.delete(a);
									}

								});

							}

							if (null == req.getEmployeeLiabilityData()) {

								humanDataList.stream().forEach(a -> {

									if (StringUtils.isNotBlank(a.getSectionId()) && a.getSectionId().equals("36")) {

										a.setSumInsured(null);
										a.setSumInsuredLc(null);
										a.setOccupationType(null);
										humanRepo.saveAndFlush(a);
									}

								});

							}

							if (null == req.getDomesticServant()) {

								humanDataList.stream().forEach(a -> {

									if (StringUtils.isNotBlank(a.getSectionId()) && a.getSectionId().equals("106")) {

										a.setSumInsured(null);
										a.setSumInsuredLc(null);
										a.setOccupationType(null);
										humanRepo.saveAndFlush(a);
									}

								});

							}

							if (null == req.getPersonalAccidentData()) {

								humanDataList.stream().forEach(a -> {

									if (StringUtils.isNotBlank(a.getSectionId()) && a.getSectionId().equals("35")) {

										a.setSumInsured(null);
										a.setSumInsuredLc(null);
										a.setOccupationType(null);

										humanRepo.saveAndFlush(a);
									}

								});

							}

						}

					} else {

						data.setMessage("Failed - BadRequest");
						data.setError("check request reference key");
						data.setIsError(true);

						List<Error> errorList = new ArrayList<>();
						Error error = new Error();
						error.setCode("400");
						error.setField("");
						error.setMessage("Check RequestRefNo key");
						errorList.add(error);

						data.setErrorMessage(errorList);
						data.setCommonResponse(null);
						data.setErroCode("0");

						return ResponseEntity.badRequest().body(data);
					}

		// domestic product section save started here 	

					if (null != req.getBuildingData() && !req.getBuildingData().isEmpty()) {

						isBadRequest = false;

						ResponseEntity<CommonRes> buildingRes = null;

						List<EserviceBuildingDetails> NewDataList = new ArrayList<>();

						// Remove deleted risk
						List<String> riskIds = req.getBuildingData().stream().map(BuildingSecSaveReq::getRiskId)
								.collect(Collectors.toList());
						List<Integer> deletedRiskIds = new ArrayList<Integer>();
						if (riskIds != null && riskIds.size() > 0) {
							for (int i = 0; i < riskIds.size(); i++) {
								deletedRiskIds.add(Integer.valueOf(riskIds.get(i)));
							}
							List<EserviceBuildingDetails> deletedRiskList = buildingRepo
									.findByRequestReferenceNoAndSectionIdAndRiskIdNotIn(req.getRequestReferenceNo(), "1",
											deletedRiskIds);
							if (deletedRiskList.size() > 0 && deletedRiskList != null) {
								buildingRepo.deleteAll(deletedRiskList);
							}
						}
						List<EserviceBuildingDetails> buildingDataList = buildingRepo
								.findByRequestReferenceNoAndSectionIdAndRiskId(req.getRequestReferenceNo(), "0", 1);

						for (BuildingSecSaveReq build : req.getBuildingData()) {

							if (build != null && StringUtils.isNotBlank(build.getRiskId()) && !"1".equals(build.getRiskId())) {

								if (build.getRiskId().matches("[0-9.]+")) {
									List<EserviceBuildingDetails> IsExistEditData = buildingRepo
											.findByRequestReferenceNoAndSectionIdAndRiskId(req.getRequestReferenceNo(), "1",
													Integer.valueOf(build.getRiskId()));

									if (null == IsExistEditData || IsExistEditData.isEmpty()) {

										if (null != buildingDataList && !buildingDataList.isEmpty()) {

											EserviceBuildingDetails cloneData = new EserviceBuildingDetails();
											dozerBeanMapper.map(buildingDataList.get(0), cloneData);

											if (null != cloneData) {

												cloneData.setRiskId(Integer.valueOf(build.getRiskId()));
//												cloneData.setBuildingSuminsured(null);
												cloneData.setBuildingAge(null);
												cloneData.setBuildingBuildYear(null);
												cloneData.setBuildingUsageId(null);
												cloneData.setBuildingUsageDesc(null);

												NewDataList.add(cloneData);

											}

										}

									}
								}

							}

							SlideBuildingSaveReq building = new SlideBuildingSaveReq();

							dozerBeanMapper.map(build, building);

							// below is also we can use dozer bean mapper to map
							// dozerBeanMapper.map(req , building);

							building.setRequestReferenceNo(
									req.getRequestReferenceNo() != null ? req.getRequestReferenceNo() : "");
							building.setRiskId(StringUtils.isNotBlank(build.getRiskId()) ? build.getRiskId() : "");
							building.setProductId(req.getProductId() != null ? req.getProductId() : "");
							building.setInsuranceId(req.getInsuranceId() != null ? req.getInsuranceId() : "");
							building.setCreatedBy(req.getCreatedBy() != null ? req.getCreatedBy() : "");
							building.setEndorsementDate(req.getEndorsementDate() != null ? req.getEndorsementDate() : null);
							building.setEndorsementRemarks(
									req.getEndorsementRemarks() != null ? req.getEndorsementRemarks() : "");
							building.setEndorsementEffdate(
									req.getEndorsementEffdate() != null ? req.getEndorsementEffdate() : null);
							building.setOriginalPolicyNo(req.getOriginalPolicyNo() != null ? req.getOriginalPolicyNo() : "");
							building.setEndtPrevPolicyNo(req.getEndtPrevPolicyNo() != null ? req.getEndtPrevPolicyNo() : "");
							building.setEndtPrevQuoteNo(req.getEndtPrevQuoteNo() != null ? req.getEndtPrevQuoteNo() : "");
							building.setEndtCount(req.getEndtCount() != null ? req.getEndtCount() : BigDecimal.ZERO);
							building.setEndtStatus(req.getEndtStatus() != null ? req.getEndtStatus() : "");
							building.setIsFinaceYn(req.getIsFinaceYn() != null ? req.getIsFinaceYn() : "");
							building.setEndtCategDesc(req.getEndtCategDesc() != null ? req.getEndtCategDesc() : "");
							building.setEndorsementType(req.getEndorsementType() != null ? req.getEndorsementType() : 0);

							buildingRes = eserviceSlideController.saveBuilding(building, NewDataList);

							/* BUILDING ADDITIONAL INFO REQUEST MAPPING */
					/*		{
								BuildingDetailsSaveReq info = new BuildingDetailsSaveReq();
								info.setBuildingSuminsured(build.getBuildingSumInsured());
								info.setBuildingAddress(build.getBuildingAddress());
								info.setCreatedBy(req.getCreatedBy());
								info.setRequestReferenceNo(req.getRequestReferenceNo());
								info.setRiskId(build.getRiskId());
								info.setSectionId(build.getSectionId());
								info.setLocationName(build.getLocationName());
								buildinginfolist.add(info);
							}
							//

							if (null != buildingRes && null != buildingRes.getBody()) {

								if (null != buildingRes.getBody().getMessage()
										&& !buildingRes.getBody().getMessage().isEmpty()) {

									data.setMessage(buildingRes.getBody().getMessage());

								}
								if (null != buildingRes.getBody().getIsError()) {

									data.setError(String.valueOf(buildingRes.getBody().getIsError()));

								}
								if (null != buildingRes.getBody().getIsError() && true == buildingRes.getBody().getIsError()) {

									data.setIsError(true);

								}
								if (null != buildingRes.getBody().getErrorMessage()
										&& !buildingRes.getBody().getErrorMessage().isEmpty()) {

									data.setErrorMessage(buildingRes.getBody().getErrorMessage());
								}
								if (null != buildingRes.getBody().getCommonResponse()) {

									Object obj = buildingRes.getBody().getCommonResponse();
									if (null != obj) {
										if (obj instanceof List) {

											List<Object> list = (List<Object>) obj;
											if (null != list && !list.isEmpty()) {
												objects.add(list.get(0));
											}

										} else {
											objects.add(obj);
										}
									}

								}
								if (buildingRes.getBody().getErroCode() != 0) {

									data.setErroCode(buildingRes.getBody().getErroCode() + "");
								}
							}

						}

					}
				} catch (Exception e) {

					e.printStackTrace();
					log.error("The error comes from BUILDING-DATA save block " + e.getMessage());

				}

				try {

					if (null != req.getAllRiskData() && !req.getAllRiskData().isEmpty()) {

						if (StringUtils.isNotBlank(req.getRequestReferenceNo())) {

							EserviceBuildingDetails buildDatas = buildingRepo
									.findByRequestReferenceNoAndSectionIdAndStatus(req.getRequestReferenceNo(), "3", "N");

							if (null != buildDatas) {

								buildDatas.setStatus("Y");

								buildingRepo.saveAndFlush(buildDatas);
							}
						}

						isBadRequest = false;
						AllRiskDetailsReq allRisk = null;

						for (AllRiskSecSaveReq risk : req.getAllRiskData()) {

							allRisk = new AllRiskDetailsReq();
							dozerBeanMapper.map(risk, allRisk);

							allRisk.setRequestReferenceNo(
									req.getRequestReferenceNo() != null ? req.getRequestReferenceNo() : "");
							allRisk.setRiskId(req.getRiskId() != null ? req.getRiskId() : "");
							allRisk.setProductId(req.getProductId() != null ? req.getProductId() : "");
							allRisk.setInsuranceId(req.getInsuranceId() != null ? req.getInsuranceId() : "");
							allRisk.setCreatedBy(req.getCreatedBy() != null ? req.getCreatedBy() : "");
							allRisk.setEndorsementDate(req.getEndorsementDate() != null ? req.getEndorsementDate() : null);
							allRisk.setEndorsementRemarks(
									req.getEndorsementRemarks() != null ? req.getEndorsementRemarks() : "");
							allRisk.setEndorsementEffdate(
									req.getEndorsementEffdate() != null ? req.getEndorsementEffdate() : null);
							allRisk.setOriginalPolicyNo(req.getOriginalPolicyNo() != null ? req.getOriginalPolicyNo() : "");
							allRisk.setEndtPrevPolicyNo(req.getEndtPrevPolicyNo() != null ? req.getEndtPrevPolicyNo() : "");
							allRisk.setEndtPrevQuoteNo(req.getEndtPrevQuoteNo() != null ? req.getEndtPrevQuoteNo() : "");
							allRisk.setEndtCount(req.getEndtCount() != null ? req.getEndtCount() : BigDecimal.ZERO);
							allRisk.setEndtStatus(req.getEndtStatus() != null ? req.getEndtStatus() : "");
							allRisk.setIsFinaceYn(req.getIsFinaceYn() != null ? req.getIsFinaceYn() : "");
							allRisk.setEndtCategDesc(req.getEndtCategDesc() != null ? req.getEndtCategDesc() : "");
							allRisk.setEndorsementType(req.getEndorsementType() != null ? req.getEndorsementType() : 0);

							ResponseEntity<CommonRes> allRiskRes = eserviceSlideController.saveAllRiskDetails(allRisk);

							if (null != allRiskRes && null != allRiskRes.getBody()) {

								if (null != allRiskRes.getBody().getMessage() && !allRiskRes.getBody().getMessage().isEmpty()) {

									if (null != data && null != data.getMessage() && !data.getMessage().isEmpty()) {

										data.setMessage(data.getMessage() + " - " + allRiskRes.getBody().getMessage());
									} else {

										data.setMessage(allRiskRes.getBody().getMessage());
									}

								}
								if (null != allRiskRes.getBody().getIsError()) {

									if (null != data.getError()) {

										data.setError(
												data.getError() + " - " + String.valueOf(allRiskRes.getBody().getIsError()));
									} else {

										data.setError(String.valueOf(allRiskRes.getBody().getIsError()));
									}
									if (true == allRiskRes.getBody().getIsError()) {
										data.setIsError(true);
									}

								}
								if (null != allRiskRes.getBody().getErrorMessage()
										&& !allRiskRes.getBody().getErrorMessage().isEmpty()) {

									if (null != data.getErrorMessage() && !data.getErrorMessage().isEmpty()) {
										data.getErrorMessage().addAll(allRiskRes.getBody().getErrorMessage());

									} else {

										data.setErrorMessage(allRiskRes.getBody().getErrorMessage());
									}
								}
								if (null != allRiskRes.getBody().getCommonResponse()) {

									Object obj = allRiskRes.getBody().getCommonResponse();

									if (null != obj) {
										if (obj instanceof List) {

											List<Object> list = (List<Object>) obj;
											if (null != list && !list.isEmpty()) {
												for (Object obje : list) {
													objects.add(obje);
												}
											}

										} else {
											objects.add(obj);
										}
									}

								}
								if (allRiskRes.getBody().getErroCode() != 0) {

									if (null != data.getErroCode() && !data.getErroCode().isEmpty()) {

										data.setErroCode(data.getErroCode() + " - " + allRiskRes.getBody().getErroCode());
									} else {
										data.setErroCode(allRiskRes.getBody().getErroCode() + "");
									}
								}

							}

						}
					} else {

						if (StringUtils.isNotBlank(req.getRequestReferenceNo())) {

							List<EserviceBuildingDetails> buildDatas = buildingRepo
									.findAllByRequestReferenceNoAndSectionId(req.getRequestReferenceNo(), "3");
							buildDatas.forEach(o -> {
								o.setStatus("N");
							});

							buildingRepo.saveAllAndFlush(buildDatas);
						}

					}

				} catch (Exception e) {

					e.printStackTrace();
					log.error("The error comes from ALLRISK-DATA save block " + e.getMessage());

				}
				try {
					if (null != req.getEmployeeLiabilityData() && !req.getEmployeeLiabilityData().isEmpty()) {

						isBadRequest = false;

						List<SlideEmpLiabilitySaveReq> empLoyeeLiabiltyList = new ArrayList<>();

						for (EmpLiabilitySecSaveReq emp : req.getEmployeeLiabilityData()) {

							SlideEmpLiabilitySaveReq employeeLiability = new SlideEmpLiabilitySaveReq();

							dozerBeanMapper.map(emp, employeeLiability);

							employeeLiability.setRequestReferenceNo(
									req.getRequestReferenceNo() != null ? req.getRequestReferenceNo() : "");
							employeeLiability.setRiskId(req.getRiskId() != null ? req.getRiskId() : "");
							employeeLiability.setProductId(req.getProductId() != null ? req.getProductId() : "");
							employeeLiability.setInsuranceId(req.getInsuranceId() != null ? req.getInsuranceId() : "");
							employeeLiability.setCreatedBy(req.getCreatedBy() != null ? req.getCreatedBy() : "");
							employeeLiability
									.setEndorsementDate(req.getEndorsementDate() != null ? req.getEndorsementDate() : null);
							employeeLiability.setEndorsementRemarks(
									req.getEndorsementRemarks() != null ? req.getEndorsementRemarks() : "");
							employeeLiability.setEndorsementEffdate(
									req.getEndorsementEffdate() != null ? req.getEndorsementEffdate() : null);
							employeeLiability
									.setOriginalPolicyNo(req.getOriginalPolicyNo() != null ? req.getOriginalPolicyNo() : "");
							employeeLiability
									.setEndtPrevPolicyNo(req.getEndtPrevPolicyNo() != null ? req.getEndtPrevPolicyNo() : "");
							employeeLiability
									.setEndtPrevQuoteNo(req.getEndtPrevQuoteNo() != null ? req.getEndtPrevQuoteNo() : "");
							employeeLiability.setEndtCount(req.getEndtCount() != null ? req.getEndtCount() : BigDecimal.ZERO);
							employeeLiability.setEndtStatus(req.getEndtStatus() != null ? req.getEndtStatus() : "");
							employeeLiability.setIsFinaceYn(req.getIsFinaceYn() != null ? req.getIsFinaceYn() : "");
							employeeLiability.setEndtCategDesc(req.getEndtCategDesc() != null ? req.getEndtCategDesc() : "");
							employeeLiability
									.setEndorsementType(req.getEndorsementType() != null ? req.getEndorsementType() : 0);

							empLoyeeLiabiltyList.add(employeeLiability);

						}

						ResponseEntity<CommonRes> employeeLiabilityRes = eserviceSlideController
								.saveEmpLiabilityDetails(empLoyeeLiabiltyList);
						if (null != employeeLiabilityRes && null != employeeLiabilityRes.getBody()) {

							if (null != employeeLiabilityRes.getBody().getMessage()
									&& !employeeLiabilityRes.getBody().getMessage().isEmpty()) {

								if (null != data && null != data.getMessage() && !data.getMessage().isEmpty()) {

									data.setMessage(data.getMessage() + " - " + employeeLiabilityRes.getBody().getMessage());
								} else {

									data.setMessage(employeeLiabilityRes.getBody().getMessage());
								}

							}
							if (null != employeeLiabilityRes.getBody().getIsError()) {

								if (null != data.getError()) {

									data.setError(data.getError() + " - "
											+ String.valueOf(employeeLiabilityRes.getBody().getIsError()));
								} else {

									data.setError(String.valueOf(employeeLiabilityRes.getBody().getIsError()));
								}
								if (true == employeeLiabilityRes.getBody().getIsError()) {
									data.setIsError(true);
								}

							}
							if (null != employeeLiabilityRes.getBody().getErrorMessage()
									&& !employeeLiabilityRes.getBody().getErrorMessage().isEmpty()) {

								if (null != data.getErrorMessage() && !data.getErrorMessage().isEmpty()) {
									data.getErrorMessage().addAll(employeeLiabilityRes.getBody().getErrorMessage());

								} else {

									data.setErrorMessage(employeeLiabilityRes.getBody().getErrorMessage());
								}
							}
							if (null != employeeLiabilityRes.getBody().getCommonResponse()) {

								Object obj = employeeLiabilityRes.getBody().getCommonResponse();

								if (null != obj) {
									if (obj instanceof List) {

										List<Object> list = (List<Object>) obj;
										if (null != list && !list.isEmpty()) {

											for (Object obje : list) {
												objects.add(obje);

											}
										}

									} else {
										objects.add(obj);
									}
								}

							}
							if (employeeLiabilityRes.getBody().getErroCode() != 0) {

								if (null != data.getErroCode() && !data.getErroCode().isEmpty()) {

									data.setErroCode(data.getErroCode() + " - " + employeeLiabilityRes.getBody().getErroCode());
								} else {
									data.setErroCode(employeeLiabilityRes.getBody().getErroCode() + "");
								}
							}

						}
					}
				} catch (Exception e) {

					e.printStackTrace();
					log.error("The error comes from EMPLOYEELIABILITY-DATA save block " + e.getMessage());

				}

				try {
					if (null != req.getDomesticServant() && !req.getDomesticServant().isEmpty()) {

						isBadRequest = false;

						// List<SlideEmpLiabilitySaveReq> empLoyeeLiabiltyList = new ArrayList<>();
						List<SlideEmpLiabilitySaveReq> DomesticServent = new ArrayList<>();

						for (EmpLiabilitySecSaveReq emp : req.getDomesticServant()) {

							SlideEmpLiabilitySaveReq domesticservent = new SlideEmpLiabilitySaveReq();

							dozerBeanMapper.map(emp, domesticservent);

							domesticservent.setRequestReferenceNo(
									req.getRequestReferenceNo() != null ? req.getRequestReferenceNo() : "");
							domesticservent.setRiskId(req.getRiskId() != null ? req.getRiskId() : "");
							domesticservent.setProductId(req.getProductId() != null ? req.getProductId() : "");
							domesticservent.setInsuranceId(req.getInsuranceId() != null ? req.getInsuranceId() : "");
							domesticservent.setCreatedBy(req.getCreatedBy() != null ? req.getCreatedBy() : "");
							domesticservent
									.setEndorsementDate(req.getEndorsementDate() != null ? req.getEndorsementDate() : null);
							domesticservent.setEndorsementRemarks(
									req.getEndorsementRemarks() != null ? req.getEndorsementRemarks() : "");
							domesticservent.setEndorsementEffdate(
									req.getEndorsementEffdate() != null ? req.getEndorsementEffdate() : null);
							domesticservent
									.setOriginalPolicyNo(req.getOriginalPolicyNo() != null ? req.getOriginalPolicyNo() : "");
							domesticservent
									.setEndtPrevPolicyNo(req.getEndtPrevPolicyNo() != null ? req.getEndtPrevPolicyNo() : "");
							domesticservent
									.setEndtPrevQuoteNo(req.getEndtPrevQuoteNo() != null ? req.getEndtPrevQuoteNo() : "");
							domesticservent.setEndtCount(req.getEndtCount() != null ? req.getEndtCount() : BigDecimal.ZERO);
							domesticservent.setEndtStatus(req.getEndtStatus() != null ? req.getEndtStatus() : "");
							domesticservent.setIsFinaceYn(req.getIsFinaceYn() != null ? req.getIsFinaceYn() : "");
							domesticservent.setEndtCategDesc(req.getEndtCategDesc() != null ? req.getEndtCategDesc() : "");
							domesticservent.setEndorsementType(req.getEndorsementType() != null ? req.getEndorsementType() : 0);

							DomesticServent.add(domesticservent);

						}

						ResponseEntity<CommonRes> employeeLiabilityRes = eserviceSlideController
								.saveEmpLiabilityDetails(DomesticServent);
						if (null != employeeLiabilityRes && null != employeeLiabilityRes.getBody()) {

							if (null != employeeLiabilityRes.getBody().getMessage()
									&& !employeeLiabilityRes.getBody().getMessage().isEmpty()) {

								if (null != data && null != data.getMessage() && !data.getMessage().isEmpty()) {

									data.setMessage(data.getMessage() + " - " + employeeLiabilityRes.getBody().getMessage());
								} else {

									data.setMessage(employeeLiabilityRes.getBody().getMessage());
								}

							}
							if (null != employeeLiabilityRes.getBody().getIsError()) {

								if (null != data.getError()) {

									data.setError(data.getError() + " - "
											+ String.valueOf(employeeLiabilityRes.getBody().getIsError()));
								} else {

									data.setError(String.valueOf(employeeLiabilityRes.getBody().getIsError()));
								}
								if (true == employeeLiabilityRes.getBody().getIsError()) {
									data.setIsError(true);
								}

							}
							if (null != employeeLiabilityRes.getBody().getErrorMessage()
									&& !employeeLiabilityRes.getBody().getErrorMessage().isEmpty()) {

								if (null != data.getErrorMessage() && !data.getErrorMessage().isEmpty()) {
									data.getErrorMessage().addAll(employeeLiabilityRes.getBody().getErrorMessage());

								} else {

									data.setErrorMessage(employeeLiabilityRes.getBody().getErrorMessage());
								}
							}
							if (null != employeeLiabilityRes.getBody().getCommonResponse()) {

								Object obj = employeeLiabilityRes.getBody().getCommonResponse();

								if (null != obj) {
									if (obj instanceof List) {

										List<Object> list = (List<Object>) obj;
										if (null != list && !list.isEmpty()) {

											for (Object obje : list) {
												objects.add(obje);

											}
										}

									} else {
										objects.add(obj);
									}
								}

							}
							if (employeeLiabilityRes.getBody().getErroCode() != 0) {

								if (null != data.getErroCode() && !data.getErroCode().isEmpty()) {

									data.setErroCode(data.getErroCode() + " - " + employeeLiabilityRes.getBody().getErroCode());
								} else {
									data.setErroCode(employeeLiabilityRes.getBody().getErroCode() + "");
								}
							}

						}
					}
				} catch (Exception e) {

					e.printStackTrace();
					log.error("The error comes from  Domestic Servant -DATA save block " + e.getMessage());

				}
				try {
					if (null != req.getFireAndAlliedPerills() && !req.getFireAndAlliedPerills().isEmpty()) {

						isBadRequest = false;

						ResponseEntity<CommonRes> buildingRes = null;

						List<EserviceBuildingDetails> NewDataList = new ArrayList<>();

						for (BuildingSecSaveReq build : req.getFireAndAlliedPerills()) {
							List<EserviceBuildingDetails> buildingDataList = buildingRepo
									.findByRequestReferenceNoAndSectionIdAndRiskId(req.getRequestReferenceNo(),
											build.getSectionId(), 1);

							if (build != null && StringUtils.isNotBlank(build.getRiskId()) && !"1".equals(build.getRiskId())) {

								/*
								 * if (build.getRiskId().matches("[0-9.]+")) {
								 * 
								 * List<EserviceBuildingDetails> IsExistEditData = buildingRepo
								 * .findByRequestReferenceNoAndSectionIdAndRiskId(req.getRequestReferenceNo() ,
								 * "1" ,Integer.valueOf(build.getRiskId()) );
								 * 
								 * if (null == IsExistEditData || IsExistEditData.isEmpty()) {
								 * 
								 * if(null != buildingDataList && !buildingDataList.isEmpty()) {
								 * 
								 * EserviceBuildingDetails cloneData = new EserviceBuildingDetails();
								 * dozerBeanMapper.map(buildingDataList.get(0), cloneData);
								 * 
								 * if(null != cloneData) {
								 * 
								 * cloneData.setRiskId(Integer.valueOf(build.getRiskId()));
								 * cloneData.setBuildingSuminsured(null); cloneData.setBuildingAge(null);
								 * cloneData.setBuildingBuildYear(null); cloneData.setBuildingUsageId(null);
								 * cloneData.setBuildingUsageDesc(null);
								 * 
								 * 
								 * NewDataList.add(cloneData);
								 * 
								 * }
								 * 
								 * }
								 * 
								 * 
								 * } }
								 */
		/*					}

							SlideBuildingSaveReq building = new SlideBuildingSaveReq();

							dozerBeanMapper.map(build, building);

							// below is also we can use dozer bean mapper to map
							// dozerBeanMapper.map(req , building);

							building.setRequestReferenceNo(
									req.getRequestReferenceNo() != null ? req.getRequestReferenceNo() : "");
							building.setRiskId(StringUtils.isNotBlank(build.getRiskId()) ? build.getRiskId() : "");
							building.setProductId(req.getProductId() != null ? req.getProductId() : "");
							building.setInsuranceId(req.getInsuranceId() != null ? req.getInsuranceId() : "");
							building.setCreatedBy(req.getCreatedBy() != null ? req.getCreatedBy() : "");
							building.setEndorsementDate(req.getEndorsementDate() != null ? req.getEndorsementDate() : null);
							building.setEndorsementRemarks(
									req.getEndorsementRemarks() != null ? req.getEndorsementRemarks() : "");
							building.setEndorsementEffdate(
									req.getEndorsementEffdate() != null ? req.getEndorsementEffdate() : null);
							building.setOriginalPolicyNo(req.getOriginalPolicyNo() != null ? req.getOriginalPolicyNo() : "");
							building.setEndtPrevPolicyNo(req.getEndtPrevPolicyNo() != null ? req.getEndtPrevPolicyNo() : "");
							building.setEndtPrevQuoteNo(req.getEndtPrevQuoteNo() != null ? req.getEndtPrevQuoteNo() : "");
							building.setEndtCount(req.getEndtCount() != null ? req.getEndtCount() : BigDecimal.ZERO);
							building.setEndtStatus(req.getEndtStatus() != null ? req.getEndtStatus() : "");
							building.setIsFinaceYn(req.getIsFinaceYn() != null ? req.getIsFinaceYn() : "");
							building.setEndtCategDesc(req.getEndtCategDesc() != null ? req.getEndtCategDesc() : "");
							building.setEndorsementType(req.getEndorsementType() != null ? req.getEndorsementType() : 0);
							building.setIndustryType(
									StringUtils.isNotBlank(build.getIndustryType()) ? build.getIndustryType() : null);
							building.setOccupationId(
									StringUtils.isNotBlank(build.getOccupationId()) ? build.getOccupationId() : null);
							building.setCoveringDetails(
									StringUtils.isNotBlank(build.getCoveringDetails()) ? build.getCoveringDetails() : null);
							building.setDescriptionOfRisk(
									StringUtils.isNotBlank(build.getDescriptionOfRisk()) ? build.getDescriptionOfRisk() : null);

							buildingRes = eserviceSlideController.saveBuilding(building, NewDataList);

							//

							if (null != buildingRes && null != buildingRes.getBody()) {

								if (null != buildingRes.getBody().getMessage()
										&& !buildingRes.getBody().getMessage().isEmpty()) {

									data.setMessage(buildingRes.getBody().getMessage());

								}
								if (null != buildingRes.getBody().getIsError()) {

									data.setError(String.valueOf(buildingRes.getBody().getIsError()));

								}
								if (null != buildingRes.getBody().getIsError() && true == buildingRes.getBody().getIsError()) {

									data.setIsError(true);

								}
								if (null != buildingRes.getBody().getErrorMessage()
										&& !buildingRes.getBody().getErrorMessage().isEmpty()) {

									data.setErrorMessage(buildingRes.getBody().getErrorMessage());
								}
								if (null != buildingRes.getBody().getCommonResponse()) {

									Object obj = buildingRes.getBody().getCommonResponse();
									if (null != obj) {
										if (obj instanceof List) {

											List<Object> list = (List<Object>) obj;
											if (null != list && !list.isEmpty()) {
												objects.add(list.get(0));
											}

										} else {
											objects.add(obj);
										}
									}

								}
								if (buildingRes.getBody().getErroCode() != 0) {

									data.setErroCode(buildingRes.getBody().getErroCode() + "");
								}
							}

						}

					}
				} catch (Exception e) {

					e.printStackTrace();
					log.error("The error comes from  Fire And Allied Perills -DATA save block " + e.getMessage());

				}

				try {
					if (null != req.getContentData()) {

						Double totalSumInsured = null;
						if (null != req.getBuildingData() && !req.getBuildingData().isEmpty()) {

							totalSumInsured = req.getBuildingData().stream().filter(
									a -> a.getBuildingSumInsured() != null && a.getBuildingSumInsured().matches("[0-9.]+"))
									.mapToDouble(a -> Double.parseDouble(a.getBuildingSumInsured())).sum();
						}

						isBadRequest = false;

						ContentSaveReq content = new ContentSaveReq();

						dozerBeanMapper.map(req.getContentData(), content);

						content.setRequestReferenceNo(req.getRequestReferenceNo() != null ? req.getRequestReferenceNo() : "");
						content.setRiskId(req.getRiskId() != null ? req.getRiskId() : "");
						content.setProductId(req.getProductId() != null ? req.getProductId() : "");
						content.setInsuranceId(req.getInsuranceId() != null ? req.getInsuranceId() : "");
						content.setCreatedBy(req.getCreatedBy() != null ? req.getCreatedBy() : "");
						content.setEndorsementDate(req.getEndorsementDate() != null ? req.getEndorsementDate() : null);
						content.setEndorsementRemarks(req.getEndorsementRemarks() != null ? req.getEndorsementRemarks() : "");
						content.setEndorsementEffdate(req.getEndorsementEffdate() != null ? req.getEndorsementEffdate() : null);
						content.setOriginalPolicyNo(req.getOriginalPolicyNo() != null ? req.getOriginalPolicyNo() : "");
						content.setEndtPrevPolicyNo(req.getEndtPrevPolicyNo() != null ? req.getEndtPrevPolicyNo() : "");
						content.setEndtPrevQuoteNo(req.getEndtPrevQuoteNo() != null ? req.getEndtPrevQuoteNo() : "");
						content.setEndtCount(req.getEndtCount() != null ? req.getEndtCount() : BigDecimal.ZERO);
						content.setEndtStatus(req.getEndtStatus() != null ? req.getEndtStatus() : "");
						content.setIsFinaceYn(req.getIsFinaceYn() != null ? req.getIsFinaceYn() : "");
						content.setEndtCategDesc(req.getEndtCategDesc() != null ? req.getEndtCategDesc() : "");
						content.setEndorsementType(req.getEndorsementType() != null ? req.getEndorsementType() : 0);

						ResponseEntity<CommonRes> contentRes = eserviceSlideController.saveContentDetails(content,
								totalSumInsured);

						if (null != contentRes && null != contentRes.getBody()) {

							if (null != contentRes.getBody().getMessage() && !contentRes.getBody().getMessage().isEmpty()) {

								if (null != data && null != data.getMessage() && !data.getMessage().isEmpty()) {

									data.setMessage(data.getMessage() + " - " + contentRes.getBody().getMessage());
								} else {

									data.setMessage(contentRes.getBody().getMessage());
								}

							}
							if (null != contentRes.getBody().getIsError()) {

								if (null != data.getError()) {

									data.setError(data.getError() + " - " + String.valueOf(contentRes.getBody().getIsError()));
								} else {

									data.setError(String.valueOf(contentRes.getBody().getIsError()));
								}
								if (true == contentRes.getBody().getIsError()) {

									data.setIsError(true);
								}

							}
							if (null != contentRes.getBody().getErrorMessage()
									&& !contentRes.getBody().getErrorMessage().isEmpty()) {

								if (null != data.getErrorMessage() && !data.getErrorMessage().isEmpty()) {
									data.getErrorMessage().addAll(contentRes.getBody().getErrorMessage());

								} else {

									data.setErrorMessage(contentRes.getBody().getErrorMessage());
								}
							}
							if (null != contentRes.getBody().getCommonResponse()) {

								Object obj = contentRes.getBody().getCommonResponse();

								if (null != obj) {
									if (obj instanceof List) {

										List<Object> list = (List<Object>) obj;
										if (null != list && !list.isEmpty()) {
											for (Object obje : list) {
												objects.add(obje);

											}
										}

									} else {
										objects.add(obj);
									}
								}
							}
							if (contentRes.getBody().getErroCode() != 0) {

								if (null != data.getErroCode() && !data.getErroCode().isEmpty()) {

									data.setErroCode(data.getErroCode() + " - " + contentRes.getBody().getErroCode());
								} else {
									data.setErroCode(contentRes.getBody().getErroCode() + "");
								}
							}

						}
					}
				} catch (Exception e) {

					e.printStackTrace();
					log.error("The error comes from CONTENTRISK-DATA save block " + e.getMessage());

				}

				try {
					if (null != req.getElectronicEquipment()) {

						Double totalSumInsured = null;
						if (null != req.getBuildingData() && !req.getBuildingData().isEmpty()) {

							totalSumInsured = req.getBuildingData().stream().filter(
									a -> a.getBuildingSumInsured() != null && a.getBuildingSumInsured().matches("[0-9.]+"))
									.mapToDouble(a -> Double.parseDouble(a.getBuildingSumInsured())).sum();
						}

						isBadRequest = false;

						ContentSaveReq content = new ContentSaveReq();

						dozerBeanMapper.map(req.getElectronicEquipment(), content);

						content.setRequestReferenceNo(req.getRequestReferenceNo() != null ? req.getRequestReferenceNo() : "");
						content.setRiskId(req.getRiskId() != null ? req.getRiskId() : "");
						content.setProductId(req.getProductId() != null ? req.getProductId() : "");
						content.setInsuranceId(req.getInsuranceId() != null ? req.getInsuranceId() : "");
						content.setCreatedBy(req.getCreatedBy() != null ? req.getCreatedBy() : "");
						content.setEndorsementDate(req.getEndorsementDate() != null ? req.getEndorsementDate() : null);
						content.setEndorsementRemarks(req.getEndorsementRemarks() != null ? req.getEndorsementRemarks() : "");
						content.setEndorsementEffdate(req.getEndorsementEffdate() != null ? req.getEndorsementEffdate() : null);
						content.setOriginalPolicyNo(req.getOriginalPolicyNo() != null ? req.getOriginalPolicyNo() : "");
						content.setEndtPrevPolicyNo(req.getEndtPrevPolicyNo() != null ? req.getEndtPrevPolicyNo() : "");
						content.setEndtPrevQuoteNo(req.getEndtPrevQuoteNo() != null ? req.getEndtPrevQuoteNo() : "");
						content.setEndtCount(req.getEndtCount() != null ? req.getEndtCount() : BigDecimal.ZERO);
						content.setEndtStatus(req.getEndtStatus() != null ? req.getEndtStatus() : "");
						content.setIsFinaceYn(req.getIsFinaceYn() != null ? req.getIsFinaceYn() : "");
						content.setEndtCategDesc(req.getEndtCategDesc() != null ? req.getEndtCategDesc() : "");
						content.setEndorsementType(req.getEndorsementType() != null ? req.getEndorsementType() : 0);

						ResponseEntity<CommonRes> contentRes = eserviceSlideController.saveContentDetails(content,
								totalSumInsured);

						if (null != contentRes && null != contentRes.getBody()) {

							if (null != contentRes.getBody().getMessage() && !contentRes.getBody().getMessage().isEmpty()) {

								if (null != data && null != data.getMessage() && !data.getMessage().isEmpty()) {

									data.setMessage(data.getMessage() + " - " + contentRes.getBody().getMessage());
								} else {

									data.setMessage(contentRes.getBody().getMessage());
								}

							}
							if (null != contentRes.getBody().getIsError()) {

								if (null != data.getError()) {

									data.setError(data.getError() + " - " + String.valueOf(contentRes.getBody().getIsError()));
								} else {

									data.setError(String.valueOf(contentRes.getBody().getIsError()));
								}
								if (true == contentRes.getBody().getIsError()) {

									data.setIsError(true);
								}

							}
							if (null != contentRes.getBody().getErrorMessage()
									&& !contentRes.getBody().getErrorMessage().isEmpty()) {

								if (null != data.getErrorMessage() && !data.getErrorMessage().isEmpty()) {
									data.getErrorMessage().addAll(contentRes.getBody().getErrorMessage());

								} else {

									data.setErrorMessage(contentRes.getBody().getErrorMessage());
								}
							}
							if (null != contentRes.getBody().getCommonResponse()) {

								Object obj = contentRes.getBody().getCommonResponse();

								if (null != obj) {
									if (obj instanceof List) {

										List<Object> list = (List<Object>) obj;
										if (null != list && !list.isEmpty()) {
											for (Object obje : list) {
												objects.add(obje);

											}
										}

									} else {
										objects.add(obj);
									}
								}
							}
							if (contentRes.getBody().getErroCode() != 0) {

								if (null != data.getErroCode() && !data.getErroCode().isEmpty()) {

									data.setErroCode(data.getErroCode() + " - " + contentRes.getBody().getErroCode());
								} else {
									data.setErroCode(contentRes.getBody().getErroCode() + "");
								}
							}

						}
					}
				} catch (Exception e) {

					e.printStackTrace();
					log.error("The error comes from Electronic  save block " + e.getMessage());

				}
				try {
					if (null != req.getPersonalAccidentData() && !req.getPersonalAccidentData().isEmpty()) {

						isBadRequest = false;

						List<SlidePersonalAccidentSaveReq> personalList = new ArrayList<>();

						for (PersonalAccidentSecSaveReq personal : req.getPersonalAccidentData()) {

							SlidePersonalAccidentSaveReq personalAccident = new SlidePersonalAccidentSaveReq();

							dozerBeanMapper.map(personal, personalAccident);

							personalAccident.setRequestReferenceNo(
									req.getRequestReferenceNo() != null ? req.getRequestReferenceNo() : "");
							personalAccident.setRiskId(req.getRiskId() != null ? req.getRiskId() : "");
							personalAccident.setProductId(req.getProductId() != null ? req.getProductId() : "");
							personalAccident.setInsuranceId(req.getInsuranceId() != null ? req.getInsuranceId() : "");
							personalAccident.setCreatedBy(req.getCreatedBy() != null ? req.getCreatedBy() : "");
							personalAccident
									.setEndorsementDate(req.getEndorsementDate() != null ? req.getEndorsementDate() : null);
							personalAccident.setEndorsementRemarks(
									req.getEndorsementRemarks() != null ? req.getEndorsementRemarks() : "");
							personalAccident.setEndorsementEffdate(
									req.getEndorsementEffdate() != null ? req.getEndorsementEffdate() : null);
							personalAccident
									.setOriginalPolicyNo(req.getOriginalPolicyNo() != null ? req.getOriginalPolicyNo() : "");
							personalAccident
									.setEndtPrevPolicyNo(req.getEndtPrevPolicyNo() != null ? req.getEndtPrevPolicyNo() : "");
							personalAccident
									.setEndtPrevQuoteNo(req.getEndtPrevQuoteNo() != null ? req.getEndtPrevQuoteNo() : "");
							personalAccident.setEndtCount(req.getEndtCount() != null ? req.getEndtCount() : BigDecimal.ZERO);
							personalAccident.setEndtStatus(req.getEndtStatus() != null ? req.getEndtStatus() : "");
							personalAccident.setIsFinaceYn(req.getIsFinaceYn() != null ? req.getIsFinaceYn() : "");
							personalAccident.setEndtCategDesc(req.getEndtCategDesc() != null ? req.getEndtCategDesc() : "");
							personalAccident
									.setEndorsementType(req.getEndorsementType() != null ? req.getEndorsementType() : 0);

							personalList.add(personalAccident);

						}

						ResponseEntity<CommonRes> personalAccidentRes = eserviceSlideController
								.savePersonalAccident(personalList);

						if (null != personalAccidentRes && null != personalAccidentRes.getBody()) {

							if (null != personalAccidentRes.getBody().getMessage()
									&& !personalAccidentRes.getBody().getMessage().isEmpty()) {

								if (null != data && null != data.getMessage() && !data.getMessage().isEmpty()) {

									data.setMessage(data.getMessage() + " - " + personalAccidentRes.getBody().getMessage());
								} else {

									data.setMessage(personalAccidentRes.getBody().getMessage());
								}

							}
							if (null != personalAccidentRes.getBody().getIsError()) {

								if (null != data.getError()) {

									data.setError(data.getError() + " - "
											+ String.valueOf(personalAccidentRes.getBody().getIsError()));
								} else {

									data.setError(String.valueOf(personalAccidentRes.getBody().getIsError()));
								}
								if (true == personalAccidentRes.getBody().getIsError()) {
									data.setIsError(true);
								}

							}
							if (null != personalAccidentRes.getBody().getErrorMessage()
									&& !personalAccidentRes.getBody().getErrorMessage().isEmpty()) {

								if (null != data.getErrorMessage() && !data.getErrorMessage().isEmpty()) {
									data.getErrorMessage().addAll(personalAccidentRes.getBody().getErrorMessage());

								} else {

									data.setErrorMessage(personalAccidentRes.getBody().getErrorMessage());
								}
							}
							if (null != personalAccidentRes.getBody().getCommonResponse()) {

								Object obj = personalAccidentRes.getBody().getCommonResponse();

								if (null != obj) {
									if (obj instanceof List) {

										List<Object> list = (List<Object>) obj;
										if (null != list && !list.isEmpty()) {

											for (Object obje : list) {
												objects.add(obje);
											}
										}

									} else {
										objects.add(obj);
									}
								}
							}
							if (personalAccidentRes.getBody().getErroCode() != 0) {

								if (null != data.getErroCode() && !data.getErroCode().isEmpty()) {

									data.setErroCode(data.getErroCode() + " - " + personalAccidentRes.getBody().getErroCode());
								} else {
									data.setErroCode(personalAccidentRes.getBody().getErroCode() + "");
								}
							}
						}

					}
				} catch (Exception e) {

					e.printStackTrace();
					log.error("The error comes from PERSONALACCIDENT-DATA save block " + e.getMessage());

				}

				// corporate plus Unique sections save started here

				try {
					if (null != req.getElectronicData()) {

						isBadRequest = false;

						// ElectronicEquipSaveReq electronic = new ElectronicEquipSaveReq();
						List<ElectronicEquipSaveReq> elecEqui = new ArrayList<ElectronicEquipSaveReq>();
						for (ElectronicEquipmentSaveReq dd : req.getElectronicData()) {
							ElectronicEquipSaveReq data1 = new ElectronicEquipSaveReq();
							// ElectronicEquipSaveReq
							dozerBeanMapper.map(dd, data1);
							data1.setRequestReferenceNo(req.getRequestReferenceNo() != null ? req.getRequestReferenceNo() : "");
							data1.setProductId(req.getProductId() != null ? req.getProductId() : "");
							data1.setInsuranceId(req.getInsuranceId() != null ? req.getInsuranceId() : "");
							data1.setCreatedBy(req.getCreatedBy() != null ? req.getCreatedBy() : "");
							data1.setEndorsementDate(req.getEndorsementDate() != null ? req.getEndorsementDate() : null);
							data1.setEndorsementRemarks(req.getEndorsementRemarks() != null ? req.getEndorsementRemarks() : "");
							data1.setEndorsementEffdate(
									req.getEndorsementEffdate() != null ? req.getEndorsementEffdate() : null);
							data1.setOriginalPolicyNo(req.getOriginalPolicyNo() != null ? req.getOriginalPolicyNo() : "");
							data1.setEndtPrevPolicyNo(req.getEndtPrevPolicyNo() != null ? req.getEndtPrevPolicyNo() : "");
							data1.setEndtPrevQuoteNo(req.getEndtPrevQuoteNo() != null ? req.getEndtPrevQuoteNo() : "");
							data1.setEndtCount(req.getEndtCount() != null ? req.getEndtCount() : BigDecimal.ZERO);
							data1.setEndtStatus(req.getEndtStatus() != null ? req.getEndtStatus() : "");
							data1.setIsFinaceYn(req.getIsFinaceYn() != null ? req.getIsFinaceYn() : "");
							data1.setEndtCategDesc(req.getEndtCategDesc() != null ? req.getEndtCategDesc() : "");
							data1.setEndorsementType(req.getEndorsementType() != null ? req.getEndorsementType() : 0);
							data1.setElecEquipSuminsured(String.valueOf(dd.getElecEquipSuminsured()));
							data1.setSectionId(StringUtils.isBlank(dd.getSectionId()) ? null : dd.getSectionId());
							data1.setRiskId(StringUtils.isBlank(dd.getRiskid()) ? null : dd.getRiskid());
							data1.setStatus("Y");
							elecEqui.add(data1);
						}
						ResponseEntity<CommonRes> electronicRes = eserviceSlideController.saveElectronicEquipDetails(elecEqui);

						if (null != electronicRes && null != electronicRes.getBody()) {

							if (null != electronicRes.getBody().getMessage()
									&& !electronicRes.getBody().getMessage().isEmpty()) {

								if (null != data && null != data.getMessage() && !data.getMessage().isEmpty()) {

									data.setMessage(data.getMessage() + " - " + electronicRes.getBody().getMessage());
								} else {

									data.setMessage(electronicRes.getBody().getMessage());
								}

							}
							if (null != electronicRes.getBody().getIsError()) {

								if (null != data.getError()) {

									data.setError(
											data.getError() + " - " + String.valueOf(electronicRes.getBody().getIsError()));
								} else {

									data.setError(String.valueOf(electronicRes.getBody().getIsError()));
								}
								if (true == electronicRes.getBody().getIsError()) {
									data.setIsError(true);
								}

							}
							if (null != electronicRes.getBody().getErrorMessage()
									&& !electronicRes.getBody().getErrorMessage().isEmpty()) {

								if (null != data.getErrorMessage() && !data.getErrorMessage().isEmpty()) {
									data.getErrorMessage().addAll(electronicRes.getBody().getErrorMessage());

								} else {

									data.setErrorMessage(electronicRes.getBody().getErrorMessage());
								}
							}
							if (null != electronicRes.getBody().getCommonResponse()) {

								Object obj = electronicRes.getBody().getCommonResponse();

								if (null != obj) {
									if (obj instanceof List) {

										List<Object> list = (List<Object>) obj;
										if (null != list && !list.isEmpty()) {
											objects.add(list.get(0));
										}

									} else {
										objects.add(obj);
									}
								}
							}
							if (electronicRes.getBody().getErroCode() != 0) {

								if (null != data.getErroCode() && !data.getErroCode().isEmpty()) {

									data.setErroCode(data.getErroCode() + " - " + electronicRes.getBody().getErroCode());
								} else {
									data.setErroCode(electronicRes.getBody().getErroCode() + "");
								}
							}
						}

					}

				} catch (Exception e) {

					e.printStackTrace();
					log.error("The error comes from ELECTRONIC-DATA save block " + e.getMessage());

				}

				try {
					if (null != req.getPublicLiabilityData()) {

						isBadRequest = false;

						SlidePublicLiabilitySaveReq publicLiability = new SlidePublicLiabilitySaveReq();

						dozerBeanMapper.map(req.getPublicLiabilityData(), publicLiability);
						dozerBeanMapper.map(req, publicLiability);

						ResponseEntity<CommonRes> publicLiabilityRes = eserviceSlideController
								.saveSlidePublicLiablityDetails(publicLiability);

						if (null != publicLiabilityRes && null != publicLiabilityRes.getBody()) {

							if (null != publicLiabilityRes.getBody().getMessage()
									&& !publicLiabilityRes.getBody().getMessage().isEmpty()) {

								if (null != data && null != data.getMessage() && !data.getMessage().isEmpty()) {

									data.setMessage(data.getMessage() + " - " + publicLiabilityRes.getBody().getMessage());
								} else {

									data.setMessage(publicLiabilityRes.getBody().getMessage());
								}

							}
							if (null != publicLiabilityRes.getBody().getIsError()) {

								if (null != data.getError()) {

									data.setError(data.getError() + " - "
											+ String.valueOf(publicLiabilityRes.getBody().getIsError()));
								} else {

									data.setError(String.valueOf(publicLiabilityRes.getBody().getIsError()));
								}
								if (true == publicLiabilityRes.getBody().getIsError()) {
									data.setIsError(true);
								}

							}
							if (null != publicLiabilityRes.getBody().getErrorMessage()
									&& !publicLiabilityRes.getBody().getErrorMessage().isEmpty()) {

								if (null != data.getErrorMessage() && !data.getErrorMessage().isEmpty()) {
									data.getErrorMessage().addAll(publicLiabilityRes.getBody().getErrorMessage());

								} else {

									data.setErrorMessage(publicLiabilityRes.getBody().getErrorMessage());
								}
							}
							if (null != publicLiabilityRes.getBody().getCommonResponse()) {

								Object obj = publicLiabilityRes.getBody().getCommonResponse();

								if (null != obj) {
									if (obj instanceof List) {

										List<Object> list = (List<Object>) obj;
										if (null != list && !list.isEmpty()) {
											objects.add(list.get(0));
										}

									} else {
										objects.add(obj);
									}
								}
							}
							if (publicLiabilityRes.getBody().getErroCode() != 0) {

								if (null != data.getErroCode() && !data.getErroCode().isEmpty()) {

									data.setErroCode(data.getErroCode() + " - " + publicLiabilityRes.getBody().getErroCode());
								} else {
									data.setErroCode(publicLiabilityRes.getBody().getErroCode() + "");
								}
							}
						}

					}
				} catch (Exception e) {

					e.printStackTrace();
					log.error("The error comes from PUBLICLIABILITY-DATA save block " + e.getMessage());

				}

				try {
					if (null != req.getMachinaryData()) {

						isBadRequest = false;

						SlideMachineryBreakdownSaveReq machinary = new SlideMachineryBreakdownSaveReq();

						dozerBeanMapper.map(req.getMachinaryData(), machinary);
						dozerBeanMapper.map(req, machinary);

						ResponseEntity<CommonRes> Res = eserviceSlideController.saveSlideMachineryBreakdownDetails(machinary);

						if (null != Res && null != Res.getBody()) {

							if (null != Res.getBody().getMessage() && !Res.getBody().getMessage().isEmpty()) {

								if (null != data && null != data.getMessage() && !data.getMessage().isEmpty()) {

									data.setMessage(data.getMessage() + " - " + Res.getBody().getMessage());
								} else {

									data.setMessage(Res.getBody().getMessage());
								}

							}
							if (null != Res.getBody().getIsError()) {

								if (null != data.getError()) {

									data.setError(data.getError() + " - " + String.valueOf(Res.getBody().getIsError()));
								} else {

									data.setError(String.valueOf(Res.getBody().getIsError()));
								}
								if (true == Res.getBody().getIsError()) {
									data.setIsError(true);
								}

							}
							if (null != Res.getBody().getErrorMessage() && !Res.getBody().getErrorMessage().isEmpty()) {

								if (null != data.getErrorMessage() && !data.getErrorMessage().isEmpty()) {
									data.getErrorMessage().addAll(Res.getBody().getErrorMessage());

								} else {

									data.setErrorMessage(Res.getBody().getErrorMessage());
								}
							}
							if (null != Res.getBody().getCommonResponse()) {

								Object obj = Res.getBody().getCommonResponse();

								if (null != obj) {
									if (obj instanceof List) {

										List<Object> list = (List<Object>) obj;
										if (null != list && !list.isEmpty()) {
											objects.add(list.get(0));
										}

									} else {
										objects.add(obj);
									}
								}
							}
							if (Res.getBody().getErroCode() != 0) {

								if (null != data.getErroCode() && !data.getErroCode().isEmpty()) {

									data.setErroCode(data.getErroCode() + " - " + Res.getBody().getErroCode());
								} else {
									data.setErroCode(Res.getBody().getErroCode() + "");
								}
							}
						}

					}
				} catch (Exception e) {

					e.printStackTrace();
					log.error("The error comes from MACHINARYBREAKDOWN-DATA save block " + e.getMessage());

				}

				try {
					if (null != req.getFidelityData() && !req.getFidelityData().isEmpty()) {

						isBadRequest = false;

						List<SlideFidelityGuarantySaveReq> fidelityList = new ArrayList<SlideFidelityGuarantySaveReq>();

						for (FidelityEmpSaveReq fid : req.getFidelityData()) {

							SlideFidelityGuarantySaveReq fidelity = new SlideFidelityGuarantySaveReq();
							dozerBeanMapper.map(fid, fidelity);
							dozerBeanMapper.map(req, fidelity);

							fidelityList.add(fidelity);
						}

						ResponseEntity<CommonRes> Res = eserviceSlideController.saveSlideFidelityGuarantyDetails(fidelityList);

						if (null != Res && null != Res.getBody()) {

							if (null != Res.getBody().getMessage() && !Res.getBody().getMessage().isEmpty()) {

								if (null != data && null != data.getMessage() && !data.getMessage().isEmpty()) {

									data.setMessage(data.getMessage() + " - " + Res.getBody().getMessage());
								} else {

									data.setMessage(Res.getBody().getMessage());
								}

							}
							if (null != Res.getBody().getIsError()) {

								if (null != data.getError()) {

									data.setError(data.getError() + " - " + String.valueOf(Res.getBody().getIsError()));
								} else {

									data.setError(String.valueOf(Res.getBody().getIsError()));
								}
								if (true == Res.getBody().getIsError()) {
									data.setIsError(true);
								}

							}
							if (null != Res.getBody().getErrorMessage() && !Res.getBody().getErrorMessage().isEmpty()) {

								if (null != data.getErrorMessage() && !data.getErrorMessage().isEmpty()) {
									data.getErrorMessage().addAll(Res.getBody().getErrorMessage());

								} else {

									data.setErrorMessage(Res.getBody().getErrorMessage());
								}
							}
							if (null != Res.getBody().getCommonResponse()) {

								Object obj = Res.getBody().getCommonResponse();

								if (null != obj) {
									if (obj instanceof List) {

										List<Object> list = (List<Object>) obj;
										if (null != list && !list.isEmpty()) {
											objects.add(list.get(0));
										}

									} else {
										objects.add(obj);
									}
								}
							}
							if (Res.getBody().getErroCode() != 0) {

								if (null != data.getErroCode() && !data.getErroCode().isEmpty()) {

									data.setErroCode(data.getErroCode() + " - " + Res.getBody().getErroCode());
								} else {
									data.setErroCode(Res.getBody().getErroCode() + "");
								}
							}
						}

					}
				} catch (Exception e) {

					e.printStackTrace();
					log.error("The error comes from FIDELITY-DATA save block " + e.getMessage());

				}

				try {
					if (null != req.getMoneyData()) {

						isBadRequest = false;
						List<SlideMoneySaveReq> moneyList = new ArrayList<SlideMoneySaveReq>();
						for (MoneySaveReq dd : req.getMoneyData()) {
							SlideMoneySaveReq money = new SlideMoneySaveReq();

//						dozerBeanMapper.map(req.getMoneyData(), money);
							dozerBeanMapper.map(dd, money);
							moneyList.add(money);
						}
						ResponseEntity<CommonRes> Res = eserviceSlideController.saveSlideMoneyDetails(moneyList);

						if (null != Res && null != Res.getBody()) {

							if (null != Res.getBody().getMessage() && !Res.getBody().getMessage().isEmpty()) {

								if (null != data && null != data.getMessage() && !data.getMessage().isEmpty()) {

									data.setMessage(data.getMessage() + " - " + Res.getBody().getMessage());
								} else {

									data.setMessage(Res.getBody().getMessage());
								}

							}
							if (null != Res.getBody().getIsError()) {

								if (null != data.getError()) {

									data.setError(data.getError() + " - " + String.valueOf(Res.getBody().getIsError()));
								} else {

									data.setError(String.valueOf(Res.getBody().getIsError()));
								}
								if (true == Res.getBody().getIsError()) {
									data.setIsError(true);
								}

							}
							if (null != Res.getBody().getErrorMessage() && !Res.getBody().getErrorMessage().isEmpty()) {

								if (null != data.getErrorMessage() && !data.getErrorMessage().isEmpty()) {
									data.getErrorMessage().addAll(Res.getBody().getErrorMessage());

								} else {

									data.setErrorMessage(Res.getBody().getErrorMessage());
								}
							}
							if (null != Res.getBody().getCommonResponse()) {

								Object obj = Res.getBody().getCommonResponse();

								if (null != obj) {
									if (obj instanceof List) {

										List<Object> list = (List<Object>) obj;
										if (null != list && !list.isEmpty()) {
											objects.add(list.get(0));
										}

									} else {
										objects.add(obj);
									}
								}
							}
							if (Res.getBody().getErroCode() != 0) {

								if (null != data.getErroCode() && !data.getErroCode().isEmpty()) {

									data.setErroCode(data.getErroCode() + " - " + Res.getBody().getErroCode());
								} else {
									data.setErroCode(Res.getBody().getErroCode() + "");
								}
							}
						}

					}
				} catch (Exception e) {

					e.printStackTrace();
					log.error("The error comes from MONEY-DATA save block " + e.getMessage());

				}

				try {
					if (null != req.getBurglaryData()) {

						isBadRequest = false;

						BurglaryAndHouseBreakingSaveReq burglary = new BurglaryAndHouseBreakingSaveReq();

						dozerBeanMapper.map(req.getBurglaryData(), burglary);
						dozerBeanMapper.map(req, burglary);

						ResponseEntity<CommonRes> Res = eserviceSlideController.saveBurglaryAndHouseBreakingDetails(burglary);

						if (null != Res && null != Res.getBody()) {

							if (null != Res.getBody().getMessage() && !Res.getBody().getMessage().isEmpty()) {

								if (null != data && null != data.getMessage() && !data.getMessage().isEmpty()) {

									data.setMessage(data.getMessage() + " - " + Res.getBody().getMessage());
								} else {

									data.setMessage(Res.getBody().getMessage());
								}

							}
							if (null != Res.getBody().getIsError()) {

								if (null != data.getError()) {

									data.setError(data.getError() + " - " + String.valueOf(Res.getBody().getIsError()));
								} else {

									data.setError(String.valueOf(Res.getBody().getIsError()));
								}
								if (true == Res.getBody().getIsError()) {
									data.setIsError(true);
								}

							}
							if (null != Res.getBody().getErrorMessage() && !Res.getBody().getErrorMessage().isEmpty()) {

								if (null != data.getErrorMessage() && !data.getErrorMessage().isEmpty()) {
									data.getErrorMessage().addAll(Res.getBody().getErrorMessage());

								} else {

									data.setErrorMessage(Res.getBody().getErrorMessage());
								}
							}
							if (null != Res.getBody().getCommonResponse()) {

								Object obj = Res.getBody().getCommonResponse();

								if (null != obj) {
									if (obj instanceof List) {

										List<Object> list = (List<Object>) obj;
										if (null != list && !list.isEmpty()) {
											objects.add(list.get(0));
										}

									} else {
										objects.add(obj);
									}
								}
							}
							if (Res.getBody().getErroCode() != 0) {

								if (null != data.getErroCode() && !data.getErroCode().isEmpty()) {

									data.setErroCode(data.getErroCode() + " - " + Res.getBody().getErroCode());
								} else {
									data.setErroCode(Res.getBody().getErroCode() + "");
								}
							}
						}

					}
				} catch (Exception e) {

					e.printStackTrace();
					log.error("The error comes from BURGLARYANDHOUSE-DATA save block " + e.getMessage());

				}

				try {
					if (null != req.getBusinessData()) {

						isBadRequest = false;

						SlideBusinessInterruptionReq business = new SlideBusinessInterruptionReq();

						dozerBeanMapper.map(req.getBusinessData(), business);
						dozerBeanMapper.map(req, business);

						ResponseEntity<CommonRes> Res = eserviceSlideController.saveSlideBusinessInterruptionDetails(business);

						if (null != Res && null != Res.getBody()) {

							if (null != Res.getBody().getMessage() && !Res.getBody().getMessage().isEmpty()) {

								if (null != data && null != data.getMessage() && !data.getMessage().isEmpty()) {

									data.setMessage(data.getMessage() + " - " + Res.getBody().getMessage());
								} else {

									data.setMessage(Res.getBody().getMessage());
								}

							}
							if (null != Res.getBody().getIsError()) {

								if (null != data.getError()) {

									data.setError(data.getError() + " - " + String.valueOf(Res.getBody().getIsError()));
								} else {

									data.setError(String.valueOf(Res.getBody().getIsError()));
								}
								if (true == Res.getBody().getIsError()) {
									data.setIsError(true);
								}

							}
							if (null != Res.getBody().getErrorMessage() && !Res.getBody().getErrorMessage().isEmpty()) {

								if (null != data.getErrorMessage() && !data.getErrorMessage().isEmpty()) {
									data.getErrorMessage().addAll(Res.getBody().getErrorMessage());

								} else {

									data.setErrorMessage(Res.getBody().getErrorMessage());
								}
							}
							if (null != Res.getBody().getCommonResponse()) {

								Object obj = Res.getBody().getCommonResponse();

								if (null != obj) {
									if (obj instanceof List) {

										List<Object> list = (List<Object>) obj;
										if (null != list && !list.isEmpty()) {
											objects.add(list.get(0));
										}

									} else {
										objects.add(obj);
									}
								}
							}
							if (Res.getBody().getErroCode() != 0) {

								if (null != data.getErroCode() && !data.getErroCode().isEmpty()) {

									data.setErroCode(data.getErroCode() + " - " + Res.getBody().getErroCode());
								} else {
									data.setErroCode(Res.getBody().getErroCode() + "");
								}
							}
						}

					}
				} catch (Exception e) {

					e.printStackTrace();
					log.error("The error comes from BUSINESS-DATA save block " + e.getMessage());

				}

				try {
					if (null != req.getGoodsIntransitData()) {

						isBadRequest = false;

						SlideGoodsInTransitSaveReq GoodsInTransitive = new SlideGoodsInTransitSaveReq();

						dozerBeanMapper.map(req.getGoodsIntransitData(), GoodsInTransitive);
						dozerBeanMapper.map(req, GoodsInTransitive);

						ResponseEntity<CommonRes> Res = eserviceSlideController.saveSlideGoodsInTransit(GoodsInTransitive);

						if (null != Res && null != Res.getBody()) {

							if (null != Res.getBody().getMessage() && !Res.getBody().getMessage().isEmpty()) {

								if (null != data && null != data.getMessage() && !data.getMessage().isEmpty()) {

									data.setMessage(data.getMessage() + " - " + Res.getBody().getMessage());
								} else {

									data.setMessage(Res.getBody().getMessage());
								}

							}
							if (null != Res.getBody().getIsError()) {

								if (null != data.getError()) {

									data.setError(data.getError() + " - " + String.valueOf(Res.getBody().getIsError()));
								} else {

									data.setError(String.valueOf(Res.getBody().getIsError()));
								}
								if (true == Res.getBody().getIsError()) {
									data.setIsError(true);
								}

							}
							if (null != Res.getBody().getErrorMessage() && !Res.getBody().getErrorMessage().isEmpty()) {

								if (null != data.getErrorMessage() && !data.getErrorMessage().isEmpty()) {
									data.getErrorMessage().addAll(Res.getBody().getErrorMessage());

								} else {

									data.setErrorMessage(Res.getBody().getErrorMessage());
								}
							}
							if (null != Res.getBody().getCommonResponse()) {

								Object obj = Res.getBody().getCommonResponse();

								if (null != obj) {
									if (obj instanceof List) {

										List<Object> list = (List<Object>) obj;
										if (null != list && !list.isEmpty()) {
											objects.add(list.get(0));
										}

									} else {
										objects.add(obj);
									}
								}
							}
							if (Res.getBody().getErroCode() != 0) {

								if (null != data.getErroCode() && !data.getErroCode().isEmpty()) {

									data.setErroCode(data.getErroCode() + " - " + Res.getBody().getErroCode());
								} else {
									data.setErroCode(Res.getBody().getErroCode() + "");
								}
							}
						}

					}
				} catch (Exception e) {

					e.printStackTrace();
					log.error("The error comes from GOODSINTRANSIT-DATA save block " + e.getMessage());

				}

				if (null != objects && !objects.isEmpty()) {

					data.setCommonResponse(objects);
					/* CALL BUILDING ADDITIONAL INFO SAVE.... */
//					BUILDINGINFO(buildinginfolist);
			/*		updatesectionDetails(req.getRequestReferenceNo());
				}

				if (isBadRequest) {
					data.setMessage("Failed - BadRequest");
					data.setError("Parameter Keys Not Matched");
					data.setIsError(true);

					List<Error> errorList = new ArrayList<>();
					Error error = new Error();
					error.setCode("400");
					error.setField("");
					error.setMessage("Check request Data Section Headers");
					errorList.add(error);

					data.setErrorMessage(errorList);
					data.setCommonResponse(null);
					data.setErroCode("0");

					return ResponseEntity.badRequest().body(data);
				}
				if (null == data.getIsError())
					data.setIsError(false);

				return new ResponseEntity<CommonResponse>(data, HttpStatus.OK);

			}

			public void BUILDINGINFO(List<BuildingDetailsSaveReq> reqList) {
				SuccessRes1 res = service.savebuildingDetails(reqList);

			}

			public void updatesectionDetails(String ReferenceNo) {
				try {
					if (ReferenceNo != null) {
						List<EserviceSectionDetails> sectiondetails = eserSecRepo.findByRequestReferenceNo(ReferenceNo);
						List<EserviceBuildingDetails> findBuildings = new ArrayList<EserviceBuildingDetails>();
						List<EserviceCommonDetails> findCommon = new ArrayList<EserviceCommonDetails>();
						Map<String, Integer> unmatchedSectionAndRisk = null;
						findBuildings = buildingRepo.findByRequestReferenceNo(ReferenceNo);
						findCommon = humanRepo.findByRequestReferenceNo(ReferenceNo);
						// remove the entry sectionid=0
						// Assest
						if (!findBuildings.isEmpty()) {
							findBuildings = findBuildings.stream().filter(building -> !building.getSectionId().equals("0"))
									.distinct().collect(Collectors.toList());

							/*
							 * // find the missing risk unmatchedSectionAndRisk = findBuildings.stream()
							 * .filter(building -> sectiondetails.stream() .noneMatch(sd ->
							 * sd.getSectionId().equals(building.getSectionId()) &&
							 * sd.getRiskId().equals(building.getRiskId())))
							 * .collect(Collectors.toMap(EserviceBuildingDetails::getSectionId,
							 * EserviceBuildingDetails::getRiskId));
							 */
							// insert the missing risk id in section details
			/*				for (EserviceBuildingDetails building : findBuildings) {
								EserviceSectionDetails risk = eserSecRepo.findByRequestReferenceNoAndSectionIdAndRiskId(
										ReferenceNo, building.getSectionId(), building.getRiskId());
								if (risk == null) {
									EserviceSectionDetails risk0 = eserSecRepo.findByRequestReferenceNoAndSectionIdAndRiskId(
											ReferenceNo, building.getSectionId(), 1);
									EserviceSectionDetails addrisk = new EserviceSectionDetails();

									DozerBeanMapper mapper = new DozerBeanMapper();
									mapper.map(risk0, addrisk);
									addrisk.setRiskId(building.getRiskId());
									eserSecRepo.save(addrisk);
								}
							}
							/*
							 * for (Map.Entry<String, Integer> data : unmatchedSectionAndRisk.entrySet()) {
							 * String Sectionid = data.getKey(); int riskid = data.getValue();
							 * EserviceSectionDetails risk = eserSecRepo
							 * .findByRequestReferenceNoAndSectionIdAndRiskId(ReferenceNo, Sectionid, 1);
							 * EserviceSectionDetails addrisk = new EserviceSectionDetails();
							 * 
							 * DozerBeanMapper mapper = new DozerBeanMapper(); mapper.map(risk, addrisk);
							 * addrisk.setRiskId(riskid); eserSecRepo.save(addrisk); }
							 */
		/*
						}
						// Human
						if (!findCommon.isEmpty()) {
							findCommon = findCommon.stream().filter(building -> !building.getSectionId().equals("0"))
									.collect(Collectors.toList());

							for (EserviceCommonDetails common : findCommon) {
								EserviceSectionDetails risk = eserSecRepo.findByRequestReferenceNoAndSectionIdAndRiskId(
										ReferenceNo, common.getSectionId(), common.getRiskId());
								if (risk == null) {
									EserviceSectionDetails risk0 = eserSecRepo.findByRequestReferenceNoAndSectionIdAndRiskId(
											ReferenceNo, common.getSectionId(), 1);
									EserviceSectionDetails addrisk = new EserviceSectionDetails();

									DozerBeanMapper mapper = new DozerBeanMapper();
									mapper.map(risk0, addrisk);
									addrisk.setRiskId(common.getRiskId());

									eserSecRepo.save(addrisk);
								}
							}

						}

					}
				} catch (Exception Ex) {
					System.out.println("Exception in UpdateSectionDetails");
					Ex.printStackTrace();
				}
			}

			@Override
			public List<SlidePersonalAccidentSaveReq> fetchOriginalRequest(List<SlidePersonalAccidentSaveReq> req) {

				try {

					if (null != req && !req.isEmpty()) {

						List<SlidePersonalAccidentSaveReq> SaveList = new ArrayList<>();

						SlidePersonalAccidentSaveReq requestData = req.get(0);

						if (null != requestData && StringUtils.isNotEmpty(requestData.getSectionId())
								&& StringUtils.isNotEmpty(requestData.getRequestReferenceNo())) {

							List<ProductEmployeeDetails> actualRequest = productEmplRepo
									.findAllByRequestReferenceNoAndSectionId(requestData.getRequestReferenceNo(),
											requestData.getSectionId());

							if (null != actualRequest && !actualRequest.isEmpty()) {

								Map<Object, Double> groupedByRiskAndOccupation = actualRequest.stream()
										.collect(Collectors.groupingBy(
												a -> new AbstractMap.SimpleEntry<>(a.getRiskId(), a.getOccupationId()),
												Collectors.summingDouble(b -> b.getSalary().doubleValue())));

								DozerBeanMapper mapper = new DozerBeanMapper();

								Integer riskIdValue = 1;

								for (Map.Entry<Object, Double> entry : groupedByRiskAndOccupation.entrySet()) {

									Object key = entry.getKey();
									Double sumSalary = entry.getValue();

									if (null != key) {
										Object riskId = ((AbstractMap.SimpleEntry<?, ?>) key).getKey();
										Object occupationId = ((AbstractMap.SimpleEntry<?, ?>) key).getValue();

										if (null != riskId && null != occupationId) {

											SlidePersonalAccidentSaveReq requ = new SlidePersonalAccidentSaveReq();

											Optional<ProductEmployeeDetails> data = actualRequest.stream()
													.filter(a -> a.getRiskId().equals(riskId)
															&& a.getOccupationId().equals(occupationId))
													.findFirst();

											Long totalNoOfPersons = actualRequest.stream()
													.filter(a -> a.getRiskId().equals(riskId)
															&& a.getOccupationId().equals(occupationId))
													.count();

											if (data.isPresent()) {

												ProductEmployeeDetails dataReq = data.get();
												dataReq.setSalary(new BigDecimal(sumSalary));

												mapper.map(dataReq, requ);
												requ.setRequestReferenceNo(dataReq.getRequestReferenceNo() != null
														? dataReq.getRequestReferenceNo()
														: "");
												requ.setRiskId(riskIdValue.toString());
												requ.setOriginalRiskId(
														dataReq.getRiskId() != null ? dataReq.getRiskId().toString() : "0");
												requ.setProductId(
														dataReq.getProductId() != null ? dataReq.getProductId().toString()
																: "");
												requ.setSectionId(dataReq.getSectionId() != null ? dataReq.getSectionId() : "");
												requ.setInsuranceId(
														dataReq.getCompanyId() != null ? dataReq.getCompanyId() : "");
												requ.setTotalNoOfPersons(
														totalNoOfPersons != null ? totalNoOfPersons.toString() : "");
												requ.setCreatedBy(dataReq.getCreatedBy() != null ? dataReq.getCreatedBy() : "");
												requ.setSumInsured(
														dataReq.getSalary() != null ? dataReq.getSalary().toString() : "");
												requ.setOccupationType(
														dataReq.getOccupationId() != null ? dataReq.getOccupationId() : "");
												requ.setEndorsementDate(
														dataReq.getEndorsementDate() != null ? dataReq.getEndorsementDate()
																: new Date());
												requ.setEndorsementRemarks(dataReq.getEndorsementRemarks() != null
														? dataReq.getEndorsementRemarks()
														: "");
												requ.setEndorsementEffdate(dataReq.getEndorsementEffdate() != null
														? dataReq.getEndorsementEffdate()
														: new Date());
												requ.setOriginalPolicyNo(
														dataReq.getOriginalPolicyNo() != null ? dataReq.getOriginalPolicyNo()
																: "");
												requ.setEndtPrevPolicyNo(
														dataReq.getEndtPrevPolicyNo() != null ? dataReq.getEndtPrevPolicyNo()
																: "");
												requ.setEndtPrevQuoteNo(
														dataReq.getEndtPrevQuoteNo() != null ? dataReq.getEndtPrevQuoteNo()
																: "");
												requ.setEndtCount(
														dataReq.getEndtCount() != null ? new BigDecimal(dataReq.getEndtCount())
																: BigDecimal.ZERO);
												requ.setEndtStatus(
														dataReq.getEndtStatus() != null ? dataReq.getEndtStatus() : "");
												// requ.setIsFinanceYn(dataReq.getIsFinanceYn() != null ?
												// dataReq.getIsFinanceYn() : "");
												requ.setEndtCategDesc(
														dataReq.getEndtCategDesc() != null ? dataReq.getEndtCategDesc() : "");
												requ.setEndorsementType(
														dataReq.getEndorsementType() != null ? dataReq.getEndorsementType()
																: 0);
												requ.setEndorsementTypeDesc(dataReq.getEndorsementTypeDesc() != null
														? dataReq.getEndorsementTypeDesc()
														: "");
//											requ.setOtherOccupation(dataReq.getOtherOccupation() != null ? dataReq.getOtherOccupation() : "");
//											requ.setTtdSumInsured(dataReq.getTtdSumInsured() != null ? dataReq.getTtdSumInsured() : 0);
//											requ.setMeSumInsured(dataReq.getMeSumInsured() != null ? dataReq.getMeSumInsured() : 0);
//											requ.setFeSumInsured(dataReq.getFeSumInsured() != null ? dataReq.getFeSumInsured() : 0);
//											requ.setPtdSumInsured(dataReq.getPtdSumInsured() != null ? dataReq.getPtdSumInsured() : 0);

												SaveList.add(requ);

												riskIdValue++;

											}

										}
									}

								}

								return SaveList;
							}
						}
					}
				}

				catch (Exception e) {

					log.error("Exception Occurs When Fetching Personal Accident Data From Add details Table ****** +  ");
					log.error(e.getMessage());
					e.printStackTrace();
					// throw new DataAccessResourceFailureException("");

				}

				return null;

			}

			@Override
			public List<SlideEmpLiabilitySaveReq> fetchOriginalRequestData(List<SlideEmpLiabilitySaveReq> req) {

				try {

					if (null != req && !req.isEmpty()) {

						List<SlideEmpLiabilitySaveReq> SaveList = new ArrayList<>();

						SlideEmpLiabilitySaveReq requestData = req.get(0);

						if (null != requestData && StringUtils.isNotEmpty(requestData.getSectionId())
								&& StringUtils.isNotEmpty(requestData.getRequestReferenceNo())) {

							List<ProductEmployeeDetails> actualRequest = productEmplRepo
									.findAllByRequestReferenceNoAndSectionId(requestData.getRequestReferenceNo(),
											requestData.getSectionId());

							if (null != actualRequest && !actualRequest.isEmpty()) {

								Map<Object, Double> groupedByRiskAndOccupation = actualRequest.stream()
										.collect(Collectors.groupingBy(
												a -> new AbstractMap.SimpleEntry<>(a.getRiskId(), a.getOccupationId()),
												Collectors.summingDouble(b -> b.getSalary().doubleValue())));

								DozerBeanMapper mapper = new DozerBeanMapper();

								Integer riskIdValue = 1;

								for (Map.Entry<Object, Double> entry : groupedByRiskAndOccupation.entrySet()) {

									Object key = entry.getKey();
									Double sumSalary = entry.getValue();

									if (null != key) {
										Object riskId = ((AbstractMap.SimpleEntry<?, ?>) key).getKey();
										Object occupationId = ((AbstractMap.SimpleEntry<?, ?>) key).getValue();

										if (null != riskId && null != occupationId) {

											SlideEmpLiabilitySaveReq requ = new SlideEmpLiabilitySaveReq();

											Optional<ProductEmployeeDetails> data = actualRequest.stream()
													.filter(a -> a.getRiskId().equals(riskId)
															&& a.getOccupationId().equals(occupationId))
													.findFirst();

											Long totalNoOfPersons = actualRequest.stream()
													.filter(a -> a.getRiskId().equals(riskId)
															&& a.getOccupationId().equals(occupationId))
													.count();

											if (data.isPresent()) {

												ProductEmployeeDetails dataReq = data.get();
												dataReq.setSalary(new BigDecimal(sumSalary));

												mapper.map(dataReq, requ);
												requ.setRequestReferenceNo(dataReq.getRequestReferenceNo() != null
														? dataReq.getRequestReferenceNo()
														: "");
												requ.setRiskId(riskIdValue.toString());
												requ.setOriginalRiskId(
														dataReq.getRiskId() != null ? dataReq.getRiskId().toString() : "");
												requ.setProductId(
														dataReq.getProductId() != null ? dataReq.getProductId().toString()
																: "");
												requ.setSectionId(dataReq.getSectionId() != null ? dataReq.getSectionId() : "");
												requ.setInsuranceId(
														dataReq.getCompanyId() != null ? dataReq.getCompanyId() : "");
												requ.setTotalNoOfEmployees(
														totalNoOfPersons != null ? totalNoOfPersons.toString() : "");
												requ.setCreatedBy(dataReq.getCreatedBy() != null ? dataReq.getCreatedBy() : "");
												requ.setEmpLiabilitySi(
														dataReq.getSalary() != null ? dataReq.getSalary().toString() : "");
												requ.setLiabilityOccupationId(
														dataReq.getOccupationId() != null ? dataReq.getOccupationId().toString()
																: "");
												requ.setEndorsementDate(
														dataReq.getEndorsementDate() != null ? dataReq.getEndorsementDate()
																: new Date());
												requ.setEndorsementRemarks(dataReq.getEndorsementRemarks() != null
														? dataReq.getEndorsementRemarks()
														: "");
												requ.setEndorsementEffdate(dataReq.getEndorsementEffdate() != null
														? dataReq.getEndorsementEffdate()
														: new Date());
												requ.setOriginalPolicyNo(
														dataReq.getOriginalPolicyNo() != null ? dataReq.getOriginalPolicyNo()
																: "");
												requ.setEndtPrevPolicyNo(
														dataReq.getEndtPrevPolicyNo() != null ? dataReq.getEndtPrevPolicyNo()
																: "");
												requ.setEndtPrevQuoteNo(
														dataReq.getEndtPrevQuoteNo() != null ? dataReq.getEndtPrevQuoteNo()
																: "");
												requ.setEndtCount(
														dataReq.getEndtCount() != null ? new BigDecimal(dataReq.getEndtCount())
																: BigDecimal.ZERO);
												requ.setEndtStatus(
														dataReq.getEndtStatus() != null ? dataReq.getEndtStatus() : "");
												// requ.setIsFinanceYn(dataReq.getIsFinanceYn() != null ?
												// dataReq.getIsFinanceYn() : "");
												requ.setEndtCategDesc(
														dataReq.getEndtCategDesc() != null ? dataReq.getEndtCategDesc() : "");
												requ.setEndorsementType(
														dataReq.getEndorsementType() != null ? dataReq.getEndorsementType()
																: 0);
												requ.setEndorsementTypeDesc(dataReq.getEndorsementTypeDesc() != null
														? dataReq.getEndorsementTypeDesc()
														: "");
//											requ.setOtherOccupation(dataReq.getOtherOccupation() != null ? dataReq.getOtherOccupation() : "");
//											requ.setTtdSumInsured(dataReq.getTtdSumInsured() != null ? dataReq.getTtdSumInsured() : 0);
//											requ.setMeSumInsured(dataReq.getMeSumInsured() != null ? dataReq.getMeSumInsured() : 0);
//											requ.setFeSumInsured(dataReq.getFeSumInsured() != null ? dataReq.getFeSumInsured() : 0);
//											requ.setPtdSumInsured(dataReq.getPtdSumInsured() != null ? dataReq.getPtdSumInsured() : 0);

												SaveList.add(requ);

												riskIdValue++;

											}

										}
									}

								}

								return SaveList;
							}
						}
					}
				}

				catch (Exception e) {

					log.error("Exception Occurs When Fetching Personal Accident Data From Add details Table ****** +  ");
					log.error(e.getMessage());
					e.printStackTrace();
					// throw new DataAccessResourceFailureException("");

				}

				return null;

			}
		*/
			
			
			
			// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			/* NON MOTOR SAVE */
			
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
						Map<String, List<NonMotorSectionReq>> groupByGroupId = sectionReq.stream().filter(o -> o.getSectionId() != null)
								.collect(Collectors.groupingBy(NonMotorSectionReq::getSectionId));
						List<String> findsectionId = sectionReq.stream().map(NonMotorSectionReq::getSectionId)
								.collect(Collectors.toList());
						System.out.println("Section Id "+groupByGroupId);
						for (String group : groupByGroupId.keySet()) {
							
//							List<NonMotorSectionReq> filterReq = groupByGroupId.get(group);
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
//									sectionResList = insertBuildingSectionNonMotor(req, data, riskId, locId, requestReferenceNo,
//											locationName,data.getCoverId());
								} catch (Exception e) {
									e.printStackTrace();
									log.info("Exception Is ---> " + e.getMessage());

								}
								EserviceBuildingDetails saveData = null;
								// Insert Raw Table
								// Asset
								if (StringUtils.isNotBlank(sectionResList.get(0).getMotorYn()) && sectionResList.get(0).getMotorYn().equalsIgnoreCase("A")) {
									System.out.println("********************Eservice Building Save*******************");
									try {
//										saveData = insertAssetPolicyAndEndtDetailsAndBrokerDetails(req, requestReferenceNo,
//												riskId, data, locId, locationName, sectionResList.get(0).getSectionName(),buildingOwnerYn,address);
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
//										saveCommon = insertCommonPolicyAndEndtAndBrokerDetails(req, requestReferenceNo, riskId,
//												data, locId, locationName, sectionResList.get(0).getSectionName(),address);
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
					List<EserviceCustomerDetails> customers = eserCustRepo
							.findByMobileCode1AndMobileNo1AndCreatedByAndCompanyIdAndProductIdOrderByUpdatedDateDesc(
									req.getMobileCode(), req.getMobileNo(), req.getCreatedBy(), req.getCompanyId(),
									Integer.parseInt(req.getProductId()));
					if (customers == null || customers.isEmpty()) {
						Date entryDate = new Date();
						String createdBy = req.getCreatedBy();
						
						Integer productId = Integer.valueOf(req.getProductId());
						
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

			
//			public CommonRes insetSection(NonMotorSaveReq req,String token){
//			    long start = System.currentTimeMillis();  
//			    CommonRes comres = new CommonRes();
//				List<SlideSectionSaveRes> resList = new ArrayList<SlideSectionSaveRes>();
//				List<BuildingSectionRes> sectionResList = new ArrayList<BuildingSectionRes>();
//				NonMotorPolicyReq policyReq = req.getNonMotorPolicyReq();
//				NonMotorBrokerReq brokerReq = req.getNonMotorBrokerReq();
//				List<EserviceCommonDetails> findHumans = new ArrayList<EserviceCommonDetails>();
//				List<EserviceBuildingDetails> findBuildings = new ArrayList<EserviceBuildingDetails>();
//				List<EserviceSectionDetails> findsectionDetails = new ArrayList<EserviceSectionDetails>();
//				Integer locId = Integer.valueOf(req.getLocationList().get(0).getLocationId());
//				 int size = req.getLocationList().size();
//				List<Map> list = new ArrayList<>();
//				try {
//					if ("SQ".equals(policyReq.getSavedFrom())) {
//						if (StringUtils.isBlank(policyReq.getCustomerReferenceNo())) {
//							String customerRefNo = createCustomerForShortQuote(policyReq);
//							policyReq.setCustomerReferenceNo(customerRefNo);
//						}
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Customer insert ---> " + e.getMessage());
//				}
//				String requestReferenceNo= "";
//				try {
//					CompanyProductMaster product = getCompanyProductMasterDropdown(policyReq.getCompanyId(),policyReq.getProductId());
//					if (StringUtils.isBlank(policyReq.getRequestReferenceNo())) {
//						requestReferenceNo = generateRequestReferenceno(policyReq.getProductId(),
//								policyReq.getCompanyId());
//						if ("N".equalsIgnoreCase(product.getPackageYn())) {
//							try {
//
//								findHumans = humanRepo.findByRequestReferenceNoAndLocationId(requestReferenceNo, locId);
//								findBuildings = buildingRepo.findByRequestReferenceNoAndLocationId(requestReferenceNo,locId);
//								findsectionDetails = secRepo.findByRequestReferenceNoAndLocationId(requestReferenceNo,locId);
//								List<MsAssetDetails> ast = msAssetRepo.findByRequestReferenceNoAndLocationId(requestReferenceNo, locId);
//								List<MsHumanDetails> mshuman = mshumanRepo.findByRequestReferenceNoAndLocationId(requestReferenceNo, locId);
//								List<MsCommonDetails> com = msCommon.findByRequestreferencenoAndLocationId(requestReferenceNo, locId);
//
//								// Delete Old Records
//								if (findHumans != null && findHumans.size() > 0) {
//									humanRepo.deleteAll(findHumans);
//								}
//								if (ast != null && ast.size() > 0) {
//									msAssetRepo.deleteAll(ast);
//								}
//								if (mshuman != null && mshuman.size() > 0) {
//									mshumanRepo.deleteAll(mshuman);
//								}
//								if (com != null && com.size() > 0) {
//									msCommon.deleteAll(com);
//
//								}
//								if (findBuildings != null && findBuildings.size() > 0) {
//									buildingRepo.deleteAll(findBuildings);
//								}
//								if (!findsectionDetails.isEmpty()) {
//									secRepo.deleteAll(findsectionDetails);
//								}
//								System.out.println("Full section is Deleted Successfully");
//							} catch (Exception e) {
//								e.printStackTrace();
//								log.info("Exception Is ---> " + e.getMessage());
//
//							}
//						}
//					}
//					else
//					{
//						requestReferenceNo = policyReq.getRequestReferenceNo();
//					}
//					try
//				   {
//						insertHomePositionMaster(requestReferenceNo,policyReq,brokerReq,size,product);
//				   }catch (Exception e) {
//						e.printStackTrace();
//						log.info("Exception Is ---> " + e.getMessage());
//				   }
//					
//					List<NonMotorLocationReq> locationReq = req.getLocationList();
//				//	comres.setLocationList(locationReq);
//					System.out.println("********************Location Loop starts*******************");
//					for (NonMotorLocationReq locationdata : locationReq) {
//						List<NonMotorSectionReq> sectionReq = locationdata.getSectionList();
//						Map<String, List<NonMotorSectionReq>> groupByGroupId = sectionReq.stream().filter(o -> o.getSectionId() != null).collect(Collectors.groupingBy(NonMotorSectionReq::getSectionId));
//						List<String> findsectionId = sectionReq.stream().map(NonMotorSectionReq::getSectionId).collect(Collectors.toList());
//						Set<String> distinctSectionIds = groupByGroupId.keySet();
//						System.out.println("Section Id ->" + distinctSectionIds);
//						if (!("N".equalsIgnoreCase(product.getPackageYn())))
//						{
//							try {
//							findHumans = humanRepo.findByRequestReferenceNoAndSectionIdInAndLocationId(requestReferenceNo, distinctSectionIds,locId);
//							findBuildings = buildingRepo.findByRequestReferenceNoAndSectionIdInAndLocationId(requestReferenceNo,distinctSectionIds,locId);
//							findsectionDetails = secRepo.findByRequestReferenceNoAndSectionIdInAndLocationId(requestReferenceNo,distinctSectionIds,locId);
//
//							// Delete Old Records
//							if (findHumans != null && findHumans.size() > 0) {
//								humanRepo.deleteAll(findHumans);
//							}
//							if (findBuildings != null && findBuildings.size() > 0) {
//								buildingRepo.deleteAll(findBuildings);
//							}
//							if (!findsectionDetails.isEmpty()) {
//								secRepo.deleteAll(findsectionDetails);
//							}
//							System.out.println("Deleted Successfully");
//						} catch (Exception e) {
//							e.printStackTrace();
//							log.info("Exception Is ---> " + e.getMessage());
//						}
//						}
//						for (String group : distinctSectionIds) {
//							String locationName = locationdata.getLocationName();
//							String address = (StringUtils.isBlank(locationdata.getAddress()) ? null: locationdata.getAddress());
//							String buildingOwnerYn = (StringUtils.isBlank(locationdata.getBuildingOwnerYn()) ? null: locationdata.getBuildingOwnerYn());
//							List<NonMotorSectionReq> fiterData = sectionReq.stream().filter(o -> o.getSectionId().equalsIgnoreCase(group.toString())).collect(Collectors.toList());
//							List<NonMotorSectionReq> fiterData1 = generaterisk(fiterData);
//							for (NonMotorSectionReq data : fiterData1) {
//								String riskId = StringUtils.isBlank(data.getRiskId()) ? "0" : data.getRiskId();
//								System.out.println("Section : " + findsectionId);
//								System.out.println("LocationId : " + locId);
//								System.out.println("Location Name : " + locationName);
//								System.out.println("SectionId : " + data.getSectionId());
//								System.out.println("SectionName : " + data.getSectionName());
//								System.out.println("RiskId : " + riskId);
//								System.out.println("Cover Id :" + data.getCoverId());
//								System.out.println("OccupationId : " + data.getOccupationId());
//								System.out.println("********************Section Insert*******************");
//								try {
//									sectionResList = insertBuildingSectionNonMotor(req, data, riskId, locId,
//											requestReferenceNo, locationName, data.getCoverId());
//								} catch (Exception e) {
//									e.printStackTrace();
//									log.info("Exception Is ---> " + e.getMessage());
//
//								}
//								EserviceBuildingDetails saveData = null;
//								// Insert Raw Table
//								// Asset
//								if (StringUtils.isNotBlank(sectionResList.get(0).getMotorYn())
//										&& sectionResList.get(0).getMotorYn().equalsIgnoreCase("A")) {
//									System.out.println("********************Eservice Building Save*******************");
//									try {
//										saveData = insertAssetPolicyAndEndtDetailsAndBrokerDetails(req,requestReferenceNo, riskId, data, locId, locationName,sectionResList.get(0).getSectionName(), buildingOwnerYn, address);
//									} catch (Exception e) {
//										e.printStackTrace();
//										log.info("Exception Is ---> " + e.getMessage());
//
//									}
//									// One Time table Save
//									try {
//										List<OneTimeTableRes> otResList = null;
//										// Section Req
//										List<BuildingSectionRes> sectionList = new ArrayList<BuildingSectionRes>();
//										BuildingSectionRes sec = new BuildingSectionRes();
//										sec.setMotorYn(sectionResList.get(0).getMotorYn());
//										sec.setSectionId(sectionResList.get(0).getSectionId());
//										sec.setSectionName(sectionResList.get(0).getSectionName());
//										sec.setCoverId(sectionResList.get(0).getCoverId());
//										sec.setRiskId(sectionResList.get(0).getRiskId());
//										sectionList.add(sec);
//
//										// One Time Table Thread Call
//										OneTimeTableReq otReq = new OneTimeTableReq();
//										otReq.setRequestReferenceNo(saveData.getRequestReferenceNo());
//										otReq.setVehicleId(saveData.getRiskId());
//										otReq.setLocationId(saveData.getLocationId());
//										otReq.setBranchCode(saveData.getBranchCode());
//										otReq.setInsuranceId(saveData.getCompanyId());
//										otReq.setProductId(Integer.valueOf(saveData.getProductId()));
//										otReq.setSectionList(sectionList);
//										System.out.println("********************One Time table Save*******************");
//										otResList = otService.call_OT_Insert(otReq);
//										for (OneTimeTableRes otRes : otResList) {
//											SlideSectionSaveRes res = new SlideSectionSaveRes();
//											res.setResponse("Saved Successfully");
//											res.setRiskId(otRes.getVehicleId());
//											res.setLocationId(otRes.getLocationId());
//											res.setVdRefNo(otRes.getVdRefNo());
//											res.setCdRefNo(otRes.getCdRefNo());
//											res.setMsrefno(otRes.getMsRefNo());
//											res.setCompanyId(otRes.getCompanyId());
//											res.setProductId(otRes.getProductId());
//											res.setSectionId(otRes.getSectionId());
//											res.setCustomerReferenceNo(saveData.getCustomerReferenceNo());
//											res.setRequestReferenceNo(saveData.getRequestReferenceNo());
//											res.setCreatedBy(policyReq.getCreatedBy());
//											res.setCoverid(otRes.getCoverid());
//											resList.add(res);
//										}
//									} catch (Exception e) {
//										e.printStackTrace();
//										log.info("Exception Is ---> " + e.getMessage());
//
//									}
//
//								} else {
//									System.out.println("********************CommonDetails Save*******************");
//									EserviceCommonDetails saveCommon = new EserviceCommonDetails();
//									try {
//										saveCommon = insertCommonPolicyAndEndtAndBrokerDetails(req,
//												requestReferenceNo, riskId, data, locId, locationName,
//												sectionResList.get(0).getSectionName(), address);
//									} catch (Exception e) {
//										e.printStackTrace();
//										log.info("Exception Is ---> " + e.getMessage());
//
//									}
//									// One Time table Save
//									try {
//										List<OneTimeTableRes> otResList = null;
//										// Section Req
//										List<BuildingSectionRes> sectionList = new ArrayList<BuildingSectionRes>();
//										BuildingSectionRes sec = new BuildingSectionRes();
//										sec.setMotorYn(sectionResList.get(0).getMotorYn());
//										sec.setSectionId(sectionResList.get(0).getSectionId());
//										sec.setSectionName(sectionResList.get(0).getSectionName());
//										sec.setRiskId(saveCommon.getRiskId());
//										sec.setCoverId(sectionResList.get(0).getCoverId());
//
//										sectionList.add(sec);
//
//										// One Time Table Thread Call
//										OneTimeTableReq otReq = new OneTimeTableReq();
//										otReq.setRequestReferenceNo(saveCommon.getRequestReferenceNo());
//										otReq.setVehicleId(saveCommon.getRiskId());
//										otReq.setLocationId(saveCommon.getLocationId());
//										otReq.setBranchCode(saveCommon.getBranchCode());
//										otReq.setInsuranceId(saveCommon.getCompanyId());
//										otReq.setProductId(Integer.valueOf(saveCommon.getProductId()));
//										otReq.setSectionList(sectionList);
//										System.out.println(
//												"********************One Time table Save*******************");
//										otResList = otService.call_OT_Insert(otReq);
//										for (OneTimeTableRes otRes : otResList) {
//											SlideSectionSaveRes res = new SlideSectionSaveRes();
//											res.setResponse("Saved Successfully");
//											res.setRiskId(otRes.getVehicleId());
//											res.setLocationId(otRes.getLocationId());
//											res.setVdRefNo(otRes.getVdRefNo());
//											res.setCdRefNo(otRes.getCdRefNo());
//											res.setMsrefno(otRes.getMsRefNo());
//											res.setCompanyId(otRes.getCompanyId());
//											res.setProductId(otRes.getProductId());
//											res.setSectionId(otRes.getSectionId());
//											res.setCustomerReferenceNo(saveCommon.getCustomerReferenceNo());
//											res.setRequestReferenceNo(saveCommon.getRequestReferenceNo());
//											res.setCreatedBy(policyReq.getCreatedBy());
//											resList.add(res);
//										}
//									} catch (Exception e) {
//										e.printStackTrace();
//										log.info("Exception Is ---> " + e.getMessage());
//
//									}
//								}
//
//							}
//
//							comres.setCommonResponse(resList);
//
//							long end = System.currentTimeMillis(); // End time
//
//							long timeTaken = end - start;  
//
//							System.out.println("Nonmotor Time END");
//							System.out.println("⏳ Time Taken for NonMotor: " + timeTaken + " ms");
//							System.out.println("⏳ In seconds: " + (timeTaken / 1000.0) + " sec");
//							System.out.println("⏳ In minutes: " + (timeTaken / 60000.0) + " min");
//
////							try {
////								
////								Map<String, Object> reqMap = new HashMap<String, Object>();
////								reqMap.put("InsuranceId", policyReq.getCompanyId());
////								reqMap.put("LocationId", locationdata.getLocationId());
////								reqMap.put("SectionId", group);
////								reqMap.put("RequestReferenceNo", requestReferenceNo);
////								reqMap.put("ProductId", policyReq.getProductId());
////								if (req.getNonMotEndtReq().getCoverModification() != null) {
////									reqMap.put("EffectiveDate", req.getNonMotEndtReq().getEndorsementEffdate());
////									reqMap.put("CoverModification", req.getNonMotEndtReq().getCoverModification());
////									reqMap.put("PolicyEndDate", req.getNonMotorPolicyReq().getPolicyEndDate());
////									
////								}
////								ObjectMapper obj = new ObjectMapper();
////								String calc = obj.writeValueAsString(reqMap);
////								System.out.println(token);
////								System.out.println(calc);
////							//	RestTemplate temp = new RestTemplate();
////								String pureToken = token.split(",")[0];
////							//	HttpHeaders header = new HttpHeaders();
////							//	header.setContentType(MediaType.APPLICATION_JSON);
////							//	header.set("Authorization", pureToken);
////								String url = calcSection;
////							//	HttpEntity<String> requestEntity = new HttpEntity<>(calc, header);
////								try {
////									long cstart = System.currentTimeMillis();
////									if ((!("N".equalsIgnoreCase(product.getPackageYn()))) && (req.getNonMotEndtReq().getEndorsementType()==null) || (req.getNonMotEndtReq().getEndorsementType()!=842)) {
//////										ResponseEntity<Map> response = temp.postForEntity(url, requestEntity, Map.class);
//////										list.add(response.getBody());
//////										comres.setCalcResponse(list);
////										calcAsyncService.callCalcAsync(reqMap, pureToken, url);
////									} 
////									long cend = System.currentTimeMillis();
////									long ctimeTaken = cend - cstart;
////									System.out.println("⏳ Time Taken for calc: " + ctimeTaken + " ms");
////									System.out.println("⏳ In seconds: " + (ctimeTaken / 1000.0) + " sec");
////									System.out.println("⏳ In minutes: " + (ctimeTaken / 60000.0) + " min");
////
////								} catch (Exception e) {
////									e.printStackTrace();
////								}
////							} catch (Exception e) {
////								e.printStackTrace();
////							}
//
//						}
//
//					}
//
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Exception Is ---> " + e.getMessage());
//				}
//				return comres;
//			}

			
			
			public CommonRes insetSection(NonMotorSaveReq req,String token){
			    long start = System.currentTimeMillis();  
			    CommonRes comres = new CommonRes();
				List<SlideSectionSaveRes> resList = new ArrayList<SlideSectionSaveRes>();
				List<BuildingSectionRes> sectionResList = new ArrayList<BuildingSectionRes>();
				NonMotorPolicyReq policyReq = req.getNonMotorPolicyReq();
				NonMotorBrokerReq brokerReq = req.getNonMotorBrokerReq();
				List<EserviceCommonDetails> findHumans = new ArrayList<EserviceCommonDetails>();
				List<EserviceBuildingDetails> findBuildings = new ArrayList<EserviceBuildingDetails>();
				List<EserviceSectionDetails> findsectionDetails = new ArrayList<EserviceSectionDetails>();
				Integer locId = Integer.valueOf(req.getLocationList().get(0).getLocationId());
				 int size = req.getLocationList().size();
				List<Map> list = new ArrayList<>();
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
				String requestReferenceNo= "";
				try {
					CompanyProductMaster product = getCompanyProductMasterDropdown(policyReq.getCompanyId(),policyReq.getProductId());
					if (StringUtils.isBlank(policyReq.getRequestReferenceNo())) {
						requestReferenceNo = generateRequestReferenceno(policyReq.getProductId(), policyReq.getCompanyId());
					}
					else
					{
						requestReferenceNo = policyReq.getRequestReferenceNo();
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
					if ("N".equalsIgnoreCase(product.getPackageYn()))
					{
						try {

							findHumans = humanRepo.findByRequestReferenceNoAndLocationId(requestReferenceNo, locId);
							findBuildings = buildingRepo.findByRequestReferenceNoAndLocationId(requestReferenceNo,locId);
							findsectionDetails = secRepo.findByRequestReferenceNoAndLocationId(requestReferenceNo,locId);
							List<MsAssetDetails> ast= msAssetRepo.findByRequestReferenceNoAndLocationId(requestReferenceNo,locId);
							List<MsHumanDetails> mshuman= mshumanRepo.findByRequestReferenceNoAndLocationId(requestReferenceNo,locId);
			                List<MsCommonDetails> com =msCommon.findByRequestreferencenoAndLocationId(requestReferenceNo,locId);

							// Delete Old Records
							if (findHumans != null && findHumans.size() > 0) {
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
//					LoginMaster loginData = loginRepo.findByLoginId(brokerReq.getLoginId());
				//	LoginBranchMaster brokerBranch1 = lbranchRepo.findByLoginIdAndBrokerBranchCodeAndCompanyId(brokerReq.getLoginId(), brokerReq.getBrokerBranchCode(), policyReq.getCompanyId());
				//	LoginUserInfo loginUserData1 = loginUserRepo.findByLoginId(brokerReq.getLoginId());
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

						 commission = getListItem(policyReq.getCompanyId(), policyReq.getBranchCode(),
								"COMMISSION_PERCENT", filterSource.get(0).getItemValue());
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
							 String loginId = StringUtils.isNotBlank(brokerReq.getSourceType())&& brokerReq.getSourceType().toLowerCase().contains("b2c") ? "guest" : brokerReq.getLoginId();
							 commissionList = getPolicyName(policyReq.getCompanyId(),policyReq.getProductId(), loginId, brokerReq.getBrokerCode(), "99999", brokerReq.getUserType());
					}

					IndustryMaster industry = getIndustryName(policyReq.getCompanyId(), policyReq.getProductId(),policyReq.getBranchCode(), policyReq.getIndustryId());
					
					List<NonMotorLocationReq> locationReq = req.getLocationList();
					System.out.println("********************Location Loop starts*******************");
					for (NonMotorLocationReq locationdata : locationReq) {
						List<NonMotorSectionReq> sectionReq = locationdata.getSectionList();
						Set<String> distinctSectionIds = sectionReq.stream().map(NonMotorSectionReq::getSectionId).collect(Collectors.toSet());
						if (!("N".equalsIgnoreCase(product.getPackageYn())))
						{
							try {
							findHumans = humanRepo.findByRequestReferenceNoAndSectionIdInAndLocationId(requestReferenceNo, distinctSectionIds,locId);
							findBuildings = buildingRepo.findByRequestReferenceNoAndSectionIdInAndLocationId(requestReferenceNo,distinctSectionIds,locId);
							findsectionDetails = secRepo.findByRequestReferenceNoAndSectionIdInAndLocationId(requestReferenceNo,distinctSectionIds,locId);

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
											);
								} catch (Exception e) {
									e.printStackTrace();
									log.info("Exception Is ---> " + e.getMessage());

								}
								EserviceBuildingDetails saveData = null;
								List<EserviceBuildingDetails> saveDatalist = new ArrayList<EserviceBuildingDetails>();
								// Insert Raw Table
								// Asset
								if (StringUtils.isNotBlank(sectionResList.get(0).getMotorYn())
										&& sectionResList.get(0).getMotorYn().equalsIgnoreCase("A")) {
									System.out.println("********************Eservice Building Save*******************");
									try {
										saveData = insertAssetPolicyAndEndtDetailsAndBrokerDetails(req,requestReferenceNo, riskId, data, locId, locationName,sectionResList.get(0).getSectionName(), 
												buildingOwnerYn, address,product,companyName,branchData,sourcerTypes,issuerData,industry,commissionList,
												loginUserData,brokerBranch,premiaUser,premiaBranch,premiaLogin,loginData,commission);
										
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
										otReq.setEserviceBuilding(saveDatalist);								
										System.out.println(
												"********************One Time table Save*******************");
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
									List<EserviceCommonDetails> saveCommonList=new ArrayList<>();
									try {
										saveCommon = insertCommonPolicyAndEndtAndBrokerDetails(req,
												requestReferenceNo, riskId, data, locId, locationName,
												sectionResList.get(0).getSectionName(), address,companyName,
												sourcerTypes,premiaLogin,loginUserData,brokerBranch,premiaUser,premiaBranch,loginData
												, commissionList);
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
										otReq.setEserviceCommon(saveCommonList);
										System.out.println(
												"********************One Time table Save*******************");
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

							comres.setCommonResponse(resList);

							long end = System.currentTimeMillis(); // End time

							long timeTaken = end - start;  

							System.out.println("Nonmotor Time END");
							System.out.println("⏳ Time Taken for NonMotor: " + timeTaken + " ms");
							System.out.println("⏳ In seconds: " + (timeTaken / 1000.0) + " sec");
							System.out.println("⏳ In minutes: " + (timeTaken / 60000.0) + " min");

							try {
								
								Map<String, Object> reqMap = new HashMap<String, Object>();
								reqMap.put("InsuranceId", policyReq.getCompanyId());
								reqMap.put("LocationId", locationdata.getLocationId());
								reqMap.put("SectionId", group);
								reqMap.put("RequestReferenceNo", requestReferenceNo);
								reqMap.put("ProductId", policyReq.getProductId());
								if (req.getNonMotEndtReq().getCoverModification() != null) {
									reqMap.put("EffectiveDate", req.getNonMotEndtReq().getEndorsementEffdate());
									reqMap.put("CoverModification", req.getNonMotEndtReq().getCoverModification());
									reqMap.put("PolicyEndDate", req.getNonMotorPolicyReq().getPolicyEndDate());
									
								}
								ObjectMapper obj = new ObjectMapper();
								String calc = obj.writeValueAsString(reqMap);
								System.out.println(token);
								System.out.println(calc);
							//	RestTemplate temp = new RestTemplate();
								String pureToken = token.split(",")[0];
							//	HttpHeaders header = new HttpHeaders();
							//	header.setContentType(MediaType.APPLICATION_JSON);
							//	header.set("Authorization", pureToken);
								String url = calcSection;
							//	HttpEntity<String> requestEntity = new HttpEntity<>(calc, header);
								try {
									long cstart = System.currentTimeMillis();
									if ((!("N".equalsIgnoreCase(product.getPackageYn()))) && (req.getNonMotEndtReq().getEndorsementType()==null) || (req.getNonMotEndtReq().getEndorsementType()!=842)) {
//										ResponseEntity<Map> response = temp.postForEntity(url, requestEntity, Map.class);
//										list.add(response.getBody());
//										comres.setCalcResponse(list);
										calcAsyncService.callCalcAsync(reqMap, pureToken, url);
									} 
									long cend = System.currentTimeMillis();
									long ctimeTaken = cend - cstart;
									System.out.println("⏳ Time Taken for calc: " + ctimeTaken + " ms");
									System.out.println("⏳ In seconds: " + (ctimeTaken / 1000.0) + " sec");
									System.out.println("⏳ In minutes: " + (ctimeTaken / 60000.0) + " min");

								} catch (Exception e) {
									e.printStackTrace();
								}
							} catch (Exception e) {
								e.printStackTrace();
							}


						}

					}

				} catch (Exception e) {
					e.printStackTrace();
					log.info("Exception Is ---> " + e.getMessage());
				}
				return comres;
			}

			private HomePositionMaster insertHomePositionMaster(String requestReferenceNo, NonMotorPolicyReq policyReq,NonMotorBrokerReq brokerReq,int size,CompanyProductMaster product)
			{
			HomePositionMaster home = null;
			long a=99999;
			try {
				home = homeRepo.findByRequestReferenceNo(requestReferenceNo);
				if (home == null) {
					HomePositionMaster last=homeRepo.findTopByOrderByHomeSnoDesc();
					Long maxHomeSno = last != null ? last.getHomeSno() : 0;
					home = new HomePositionMaster();
					home.setHomeSno(maxHomeSno+1);
					home.setNoOfVehicles(size);
					home.setSectionId(0);
					home.setAmendId(0L);
					home.setApplicationNo(a);			
					home.setApplicationId(StringUtils.isBlank(brokerReq.getApplicationId()) ? "0" : brokerReq.getApplicationId());
					home.setBdmCode(StringUtils.isBlank(brokerReq.getBdmCode()) ? "0" : brokerReq.getBdmCode());
					home.setLoginId(StringUtils.isBlank(policyReq.getCreatedBy()) ? null : policyReq.getCreatedBy());
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



			private List<SectionCoverMaster> getSectionCoverDetails(String productId, String sectionId,String companyId) {
				List<SectionCoverMaster> resList = new ArrayList<SectionCoverMaster>();
				try {
					Date today = new Date();
					Calendar cal = new GregorianCalendar();
					cal.setTime(today);
					cal.set(Calendar.HOUR_OF_DAY, 23);
					cal.set(Calendar.MINUTE, 50);
					today = cal.getTime();

					// Find Latest Record
					CriteriaBuilder cb2 = em.getCriteriaBuilder();
					CriteriaQuery<SectionCoverMaster> query2 = cb2.createQuery(SectionCoverMaster.class);

					// Find All
					Root<SectionCoverMaster> b2 = query2.from(SectionCoverMaster.class);

					// Amed Id

					Subquery<Long> amendId2 = query2.subquery(Long.class);
					Root<SectionCoverMaster> ocpm2 = amendId2.from(SectionCoverMaster.class);
					amendId2.select(cb2.max(ocpm2.get("amendId")));
					Predicate a7 = cb2.equal(ocpm2.get("coverId"), b2.get("coverId"));
					Predicate a12 = cb2.equal(ocpm2.get("subCoverId"), b2.get("subCoverId"));
					Predicate a8 = cb2.equal(ocpm2.get("sectionId"), b2.get("sectionId"));
					Predicate a9 = cb2.equal(ocpm2.get("productId"), b2.get("productId"));
					Predicate a10 = cb2.equal(ocpm2.get("companyId"), b2.get("companyId"));
					Predicate a13 = cb2.equal(ocpm2.get("agencyCode"), b2.get("agencyCode"));
					Predicate a14 = cb2.equal(ocpm2.get("branchCode"), b2.get("branchCode"));
					amendId2.where(a7, a8, a9, a10, a12, a13, a14);
					query2.select(b2);

					// Order By
					List<Order> orderList2 = new ArrayList<Order>();
					orderList2.add(cb2.asc(b2.get("coverName")));

					// Where
					Predicate n4 = cb2.equal(b2.get("amendId"), amendId2);
					Predicate n6 = cb2.equal(b2.get("subCoverId"), "0");
					Predicate n7 = cb2.equal(b2.get("productId"), productId);
					Predicate n14 = cb2.equal(b2.get("companyId"), companyId);
					Predicate n15 = cb2.equal(b2.get("sectionId"), sectionId);
					Predicate n9 = cb2.equal(b2.get("agencyCode"), "99999");
					Predicate n12 = cb2.equal(b2.get("branchCode"), "99999");
					Predicate n13 = cb2.equal(b2.get("status"), "Y");
					Predicate n16 = cb2.equal(b2.get("dependentCoverYn"), "Y");
					query2.where(n4, n6, n7, n14, n15, n9, n12, n13, n16).orderBy(orderList2);

					// Get Result
					TypedQuery<SectionCoverMaster> result2 = em.createQuery(query2);
					resList = result2.getResultList();

				} catch (Exception e) {
					e.printStackTrace();
					return null;

				}
				return resList;
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
					LoginUserInfo premiaUser,LoginBranchMaster premiaBranch,LoginMaster premiaLogin,LoginMaster loginData,String commission) {
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
					saveData.setPolicyStartDate(periodStart);
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
			//		CompanyProductMaster product = getCompanyProductMasterDropdown(policyReq.getCompanyId(),
			//				policyReq.getProductId());
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
					if (StringUtils.isNotBlank(data.getIndustryId())) {

						saveData.setIndustryId(StringUtils.isBlank(data.getIndustryId()) ? null
								: Integer.valueOf(data.getIndustryId()));

//						IndustryMaster industry = getIndustryName(policyReq.getCompanyId(), policyReq.getProductId(),
//								policyReq.getBranchCode(), data.getIndustryId());

						saveData.setIndustryDesc(industry != null ? industry.getIndustryName() : "");
						saveData.setCategoryId(industry != null ? industry.getCategoryId() : "");
						saveData.setCategoryDesc(industry != null ? industry.getCategoryDesc() : "");
					}
					else if(StringUtils.isNotBlank(data.getIndustryType())) {
						saveData.setIndustryId(StringUtils.isBlank(data.getIndustryId()) ? null
								: Integer.valueOf(data.getIndustryType()));

//						IndustryMaster industry = getIndustryName(policyReq.getCompanyId(), policyReq.getProductId(),
//								policyReq.getBranchCode(), data.getIndustryType());

						saveData.setIndustryDesc(industry != null ? industry.getIndustryName() : "");
						saveData.setCategoryId(industry != null ? industry.getCategoryId() : "");
						saveData.setCategoryDesc(industry != null ? industry.getCategoryDesc() : "");
					}
		              
					// Endorsement Changes
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
			//		BranchMaster branchData = getBranchMasterRes(policyReq.getCompanyId(), policyReq.getBranchCode());
					
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

//					List<ListItemValue> sourcerTypes = premiaBrokerService.getSourceTypeDropdown(policyReq.getCompanyId(),
//							policyReq.getBranchCode(), "SOURCE_TYPE");
					List<ListItemValue> filterSource = sourcerTypes.stream()
							.filter(o -> StringUtils.isNotBlank(brokerReq.getSourceTypeId())
									&& (o.getItemCode().equalsIgnoreCase(brokerReq.getSourceTypeId())
											|| o.getItemValue().equalsIgnoreCase(brokerReq.getSourceTypeId())))
							.collect(Collectors.toList());

					if (filterSource.size() > 0 && directSource.contains(filterSource.get(0).getItemCode())) {

		//				String sourceType = filterSource.get(0).getItemValue();
//						String subUserType = sourceType.contains("Broker") ? "Broker"
//								: sourceType.contains("Agent") ? "Agent" : "";
//						LoginMaster premiaLogin = getPremiaBroker(brokerReq.getBdmCode(), subUserType,
//								policyReq.getCompanyId());
			//			String brokerLoginId = premiaLogin != null ? premiaLogin.getLoginId() : branchData.getDirectBrokerId();

				//		LoginUserInfo premiaUser = loginUserRepo.findByLoginId(brokerLoginId);
				//		LoginUserInfo loginUserData = premiaUser != null ? premiaUser: loginUserRepo.findByLoginId(branchData.getDirectBrokerId());

				//		LoginBranchMaster premiaBranch = lbranchRepo.findByLoginIdAndBranchCodeAndCompanyId(brokerLoginId,policyReq.getBranchCode(), policyReq.getCompanyId());
				//		LoginBranchMaster brokerBranch = premiaBranch != null ? premiaBranch: lbranchRepo.findByLoginIdAndBranchCodeAndCompanyId(branchData.getDirectBrokerId(),
				//						policyReq.getBranchCode(), policyReq.getCompanyId());

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
					//	LoginUserInfo loginUserData = loginUserRepo.findByLoginId(brokerReq.getLoginId());
					//	LoginBranchMaster brokerBranch = lbranchRepo.findByLoginIdAndBrokerBranchCodeAndCompanyId(brokerReq.getLoginId(), brokerReq.getBrokerBranchCode(), policyReq.getCompanyId());
					//	LoginMaster loginData = loginRepo.findByLoginId(brokerReq.getLoginId());

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
					//	LoginMaster issuerData = loginRepo.findByLoginId(brokerReq.getApplicationId());
						saveData.setSubUserType(issuerData != null ? issuerData.getSubUserType() : saveData.getSourceType());
					}
					saveData.setSubUserType(saveData.getSourceType());

					// Broker Commission
					Double commissionPercent = 0D;
					if (filterSource.size() > 0 && directSource.contains(filterSource.get(0).getItemCode())) {
						// commissionPercent=12.5;
//						String sourceType = filterSource.get(0).getItemValue();
//						String subUserType = sourceType.contains("Broker") ? "Broker"
//								: sourceType.contains("Agent") ? "Agent" : "";
////						LoginMaster premiaLogin = getPremiaBroker(brokerReq.getBdmCode(), subUserType,
////								policyReq.getCompanyId());
//						String premiaLoginId = premiaLogin != null ? premiaLogin.getLoginId() : "";

//						commission = getListItem(policyReq.getCompanyId(), policyReq.getBranchCode(),
//								"COMMISSION_PERCENT", filterSource.get(0).getItemValue());
				
						//	List<BrokerCommissionDetails> commissionList = getPolicyName(policyReq.getCompanyId(),policyReq.getProductId(), premiaLoginId, brokerReq.getBrokerCode(), "99999",brokerReq.getUserType());
							commission = commissionList.size() > 0 && commissionList.get(0).getCommissionPercentage() != null
									? commissionList.get(0).getCommissionPercentage().toString()
									: commission;
						
						commissionPercent = StringUtils.isNotBlank(commission) ? Double.valueOf(commission) : 0D;
						saveData.setCommissionPercentage(
								commissionPercent == null ? new BigDecimal("0") : new BigDecimal(commissionPercent));

					} else {
//						String loginId = StringUtils.isNotBlank(brokerReq.getSourceType())
//								&& brokerReq.getSourceType().toLowerCase().contains("b2c") ? "guest" : brokerReq.getLoginId();
//						List<BrokerCommissionDetails> commissionList = getPolicyName(policyReq.getCompanyId(),
//								policyReq.getProductId(), loginId, brokerReq.getBrokerCode(), "99999", brokerReq.getUserType());
						if (commissionList != null && commissionList.size() > 0) {
							BrokerCommissionDetails comm = commissionList.get(0);
							saveData.setCommissionPercentage(comm.getCommissionPercentage() == null ? new BigDecimal("0")
									: new BigDecimal(comm.getCommissionPercentage()));
							saveData.setVatCommission(comm.getCommissionVatPercent() == null ? new BigDecimal("0")
									: new BigDecimal(comm.getCommissionVatPercent()));
						} else {
							saveData.setCommissionPercentage(new BigDecimal("0"));
							saveData.setVatCommission(new BigDecimal("0"));
						}
					}

					// Endt Commission
					if(endtReq!=null) {
					if (!(endtReq.getEndorsementType() == null || endtReq.getEndorsementType() == 0))

					{
						List<BuildingRiskDetails> mainMot = motBuildingRepo.findByQuoteNo(endtReq.getEndtPrevQuoteNo());
						for (BuildingRiskDetails mot : mainMot) {
							saveData.setCommissionPercentage(mot.getCommissionPercentage() == null ? saveData.getCommissionPercentage(): mot.getCommissionPercentage());
							saveData.setVatCommission(mot.getVatCommission() == null ? saveData.getVatCommission() : mot.getVatCommission());
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
					//for Goods product
					if (("100002".equals(req.getNonMotorPolicyReq().getCompanyId()))
							&& ("49".equals(req.getNonMotorPolicyReq().getProductId()))) {
						saveData.setWallType(data.getWallType()); // originatingCountry
						saveData.setWallTypeDesc(data.getWallType());// originatingCountry
						saveData.setRoofType(data.getRoofType());// destinationCountry
						saveData.setRoofTypeDesc(data.getWallType());// destinationCountry
					} else {
						if (StringUtils.isNotBlank(data.getWallType())) {
							String wallType = getListItem(saveData.getCompanyId(), saveData.getBranchCode(),
									"WALL_TYPE", data.getWallType());
							saveData.setWallType(data.getWallType());
							saveData.setWallTypeDesc(wallType);

						}

						if (StringUtils.isNotBlank(data.getRoofType())) {
							String roofType = getListItem(saveData.getCompanyId(), saveData.getBranchCode(),
									"ROOF_TYPE", data.getRoofType());
							saveData.setRoofType(data.getRoofType());
							saveData.setRoofTypeDesc(roofType);

						}
					}

					/*if (StringUtils.isNotBlank(data.getWallType())) {
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

					}*/

//					if (StringUtils.isNotBlank(data.getBuildingUsageId())) {
//						String buildingusage = getListItem(saveData.getCompanyId(), saveData.getBranchCode(), "BUILDING_USAGE",
//								data.getBuildingUsageId());
//						saveData.setBuildingUsageId(data.getBuildingUsageId());
//						saveData.setBuildingUsageDesc(buildingusage);
		//
//					}
					
					//Package plus-- Fire section 
					if (StringUtils.isNotBlank(data.getBuildingUsageId())) {
						String buildingusage = getListItem(saveData.getCompanyId(), saveData.getBranchCode(), "FIRE_SUMINSURED_TYPE",
								data.getBuildingUsageId());
						saveData.setBuildingUsageId(data.getBuildingUsageId());
						saveData.setBuildingUsageDesc(StringUtils.isBlank(data.getBuildingUsageDesc())?buildingusage:data.getBuildingUsageDesc());

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
					saveData.setBuildingFloors(data.getBuildingFloors());
					
					saveData.setBuildingAreaSqm(data.getBuildingAreaSqm());	
					
					
//					if (StringUtils.isNotBlank(data.getTypeOfProperty())) {
//						String typeOfProperty = getListItem(saveData.getCompanyId(), saveData.getBranchCode(),
//								"TYPE_OF_PROPERTIES", data.getTypeOfProperty());
//						saveData.setTypeOfProperty(data.getTypeOfProperty());
//						saveData.setTypeOfPropertyDesc(typeOfProperty);
//
//					}
					String industryName="";
					if (StringUtils.isNotBlank(data.getIndustryId()) && Integer.valueOf(data.getIndustryId())>0) {
						IndustryMaster industrydel = industryrepo.findByIndustryIdAndCompanyIdAndProductId(
								Integer.valueOf(data.getIndustryId()), policyReq.getCompanyId(), policyReq.getProductId());
						if(industrydel==null) {
							industrydel = industryrepo.findByIndustryIdAndCompanyIdAndProductId(
									Integer.valueOf(data.getIndustryId()), policyReq.getCompanyId(),"99999");
						}
						if (industrydel != null) {
							industryName = industrydel.getIndustryName();
							saveData.setIndustryDesc(industryName);
						}
					}
					Integer industryId = null;

					if (StringUtils.isNotBlank(data.getIndustryId())) {
					    industryId = Integer.valueOf(data.getIndustryId());
					} else if (StringUtils.isNotBlank(data.getIndustryType())) {
					    industryId = Integer.valueOf(data.getIndustryType());
					}

					saveData.setIndustryId(industryId);
					
				
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

					// SumInsured fields Building
//					saveData.setBuildingSuminsured(
//							StringUtils.isNotBlank(data.getBuildingSumInsured()) ? new BigDecimal(data.getBuildingSumInsured())
//									: new BigDecimal("0"));
					saveData.setInternalWallType(
							StringUtils.isNotBlank(data.getInternalWallType()) ? Integer.valueOf(data.getInternalWallType())
									: 0);
					if (StringUtils.isNotBlank(data.getInternalWallType())) {
					String wall_type = getListItem(policyReq.getCompanyId(), policyReq.getBranchCode(), "wall_type",data.getInternalWallType());
					saveData.setInternalWallDesc(StringUtils.isBlank(wall_type) ? "" : wall_type);
					}
					
					String firstLossPayee="";
					if (!StringUtils.isBlank(data.getFirstLossPayee())) {
						firstLossPayee= getByBankCode(companyId, branchCode, data.getFirstLossPayee());
						
					}
					saveData.setFirstLossPayee(StringUtils.isBlank(firstLossPayee) ? "" : firstLossPayee);
//					saveData.setWaterTankSi(
//							StringUtils.isNotBlank(data.getWaterTankSi()) ? new BigDecimal(data.getWaterTankSi())
//									: new BigDecimal("0"));
//					saveData.setLossOfRentSi(
//							StringUtils.isNotBlank(data.getLossOfRentSi()) ? new BigDecimal(data.getLossOfRentSi())
//									: new BigDecimal("0"));
//					saveData.setArchitectsSi(
//							StringUtils.isNotBlank(data.getArchitectsSi()) ? new BigDecimal(data.getArchitectsSi())
//									: new BigDecimal("0"));
//					saveData.setGroundUndergroundSi(StringUtils.isNotBlank(data.getGroundUndergroundSi())
//							? new BigDecimal(data.getGroundUndergroundSi())
//							: new BigDecimal("0"));
//					// SumInsured fields Content
//					saveData.setContentSuminsured(StringUtils.isBlank(data.getContentSuminsured()) ? new BigDecimal(0)
//							: new BigDecimal(data.getContentSuminsured()));
		//


					// SumInsured fields Content
//					saveData.setContentSuminsured(StringUtils.isBlank(data.getContentSuminsured()) ? new BigDecimal(0)
//							: new BigDecimal(data.getContentSuminsured()));


//					saveData.setJewellerySi(StringUtils.isBlank(data.getJewellerySi()) ? new BigDecimal(0)
//							: new BigDecimal(data.getJewellerySi()));
//					saveData.setPaitingsSi(StringUtils.isBlank(data.getPaitingsSi()) ? new BigDecimal(0)
//							: new BigDecimal(data.getPaitingsSi()));
//					saveData.setCarpetsSi(
//							StringUtils.isBlank(data.getCarpetsSi()) ? new BigDecimal(0) : new BigDecimal(data.getCarpetsSi()));
//					// SumInsured fields Electronic Equipments
//					saveData.setElecEquipSuminsured(StringUtils.isBlank(data.getElecEquipSuminsured()) ? new BigDecimal(0)
//							: new BigDecimal(data.getElecEquipSuminsured()));
//					// SumInsured fields All Risk
//					saveData.setAllriskSuminsured(StringUtils.isBlank(data.getAllriskSuminsured()) ? new BigDecimal(0)
//							: new BigDecimal(data.getAllriskSuminsured()));
		//
//					if (saveData.getExchangeRate() != null) {
//						BigDecimal exRate = saveData.getExchangeRate();


					// SumInsured fields Electronic Equipments
//					saveData.setElecEquipSuminsured(StringUtils.isBlank(data.getElecEquipSuminsured()) ? new BigDecimal(0)
//							: new BigDecimal(data.getElecEquipSuminsured()));
					// SumInsured fields All Risk
//					saveData.setAllriskSuminsured(StringUtils.isBlank(data.getAllriskSuminsured()) ? new BigDecimal(0)
//							: new BigDecimal(data.getAllriskSuminsured()));

					if (saveData.getExchangeRate() != null) {
						BigDecimal exRate = saveData.getExchangeRate();

//						if (StringUtils.isNotBlank(data.getBuildingSumInsured())) {
//							saveData.setBuildingSuminsuredLc(new BigDecimal(data.getBuildingSumInsured()).multiply(exRate));
//						} else {
//							saveData.setBuildingSuminsuredLc(BigDecimal.ZERO);
//						}
		//
//						// new Inputs



						// new Inputs

//						if (StringUtils.isNotBlank(data.getWaterTankSi())) {
//							saveData.setWaterTankSiLc(new BigDecimal(data.getWaterTankSi()).multiply(exRate));
//						} else {
//							saveData.setWaterTankSiLc(BigDecimal.ZERO);
//						}
//						if (StringUtils.isNotBlank(data.getLossOfRentSi())) {
//							saveData.setLossOfRentSiLc(new BigDecimal(data.getLossOfRentSi()).multiply(exRate));
//						} else {
//							saveData.setLossOfRentSiLc(BigDecimal.ZERO);
//						}
//						if (StringUtils.isNotBlank(data.getArchitectsSi())) {
//							saveData.setArchitectsSiLc(new BigDecimal(data.getArchitectsSi()).multiply(exRate));
//						} else {
//							saveData.setArchitectsSiLc(BigDecimal.ZERO);
//						}
		//
//					}
		//
//					BigDecimal exchangeRate = saveData.getExchangeRate() != null ? saveData.getExchangeRate() : BigDecimal.ZERO;



					}

					BigDecimal exchangeRate = saveData.getExchangeRate() != null ? saveData.getExchangeRate() : BigDecimal.ZERO;

//					saveData.setContentSuminsuredLc(saveData.getContentSuminsured() == null ? null
//							: saveData.getContentSuminsured().multiply(exchangeRate));
//					saveData.setEquipmentSiLc(
//							saveData.getEquipmentSi() == null ? null : saveData.getEquipmentSi().multiply(exchangeRate));
//					saveData.setJewellerySiLc(
//							saveData.getJewellerySi() == null ? null : saveData.getJewellerySi().multiply(exchangeRate));
//					saveData.setPaitingsSiLc(
//							saveData.getPaitingsSi() == null ? null : saveData.getPaitingsSi().multiply(exchangeRate));
//					saveData.setCarpetsSiLc(
//							saveData.getCarpetsSi() == null ? null : saveData.getCarpetsSi().multiply(exchangeRate));
//					saveData.setElecEquipSuminsuredLc(saveData.getElecEquipSuminsured() == null ? null
//							: saveData.getElecEquipSuminsured().multiply(exchangeRate));
//					saveData.setAllriskSuminsuredLc(saveData.getAllriskSuminsured() == null ? null
//							: saveData.getAllriskSuminsured().multiply(exchangeRate));
		//
//					// Fire
//					saveData.setFirePlantSi(StringUtils.isNotBlank(data.getFirePlantSi())||data.getFirePlantSi()!=null
//							? new BigDecimal(data.getFirePlantSi())
//							: new BigDecimal("0"));
//				
//					// Fire SI LC



					// Fire
//					saveData.setFirePlantSi(StringUtils.isNotBlank(data.getFirePlantSi())||data.getFirePlantSi()!=null
//							? new BigDecimal(data.getFirePlantSi())
//							: new BigDecimal("0"));
				
					// Fire SI LC

//					saveData.setFirePlantSiLc(StringUtils.isBlank(data.getFirePlantSi()) ? null
//							: new BigDecimal(data.getFirePlantSi()).multiply(exchangeRate));
					saveData.setCoveringDetails(
							StringUtils.isBlank(data.getCoveringDetails()) ? "" : data.getCoveringDetails());
					saveData.setDescriptionOfRisk(
							StringUtils.isBlank(data.getDescriptionOfRisk()) ? "" : data.getDescriptionOfRisk());
					saveData.setBusinessInterruption(
							StringUtils.isBlank(data.getBusinessInterruption()) ? "" : data.getBusinessInterruption());
					saveData.setCategoryId(StringUtils.isBlank(data.getCategoryId())?null:data.getCategoryId());
//					if (StringUtils.isNotBlank(data.getCategoryId())) {
//						OccupationMaster occupationData = getOccupationMasterDropdown(policyReq.getCompanyId(), "99999",
//								policyReq.getProductId(), data.getCategoryId());
//						saveData.setCategoryDesc(occupationData != null ? occupationData.getOccupationName() : null);
//					}
					saveData.setCategoryDesc(StringUtils.isBlank(data.getCategoryDesc())?null:data.getCategoryDesc());
					saveData.setOccupationType(StringUtils.isBlank(data.getOccupationId())?null:data.getOccupationId());
					saveData.setOccupationTypeDesc(StringUtils.isBlank(data.getOccupationDesc())?null:data.getOccupationDesc());
//					saveData.setIndustryId(
//							StringUtils.isBlank(data.getIndustryId()) ? 0 : Integer.valueOf(data.getIndustryId()));

					// Bond
					saveData.setBondType(data.getBondType());
					saveData.setBondYear(data.getBondYear());
//					saveData.setBondSuminsured(StringUtils.isBlank(data.getBondSumInsured()) ? new BigDecimal("0")
//							: new BigDecimal(data.getBondSumInsured()));

//					// Bond SI LC


//					saveData.setBondSuminsuredLc(StringUtils.isBlank(data.getBondSumInsured()) ? null
//							: new BigDecimal(data.getBondSumInsured()).multiply(exchangeRate));


					// Money
//					Estimated annual cash carryings---MoneyAnnualEstimate
//					Cash in transit limit---MoneyMajorLoss
//					Custody of collectors---MoneyCollector
//					Safe during working hours---MoneySafeLimit
//					safe outside working hours---MoneyOutofSafe
//					Residence of director or partner---MoneyDirectorResidence
//					Value of safe---StrongroomSi

//					MoneyAnnualEstimate
//					saveData.setMoneyAnnualEstimate(StringUtils.isBlank(data.getMoneyAnnualEstimate()) ? new BigDecimal(0)
//							: new BigDecimal(data.getMoneyAnnualEstimate()));
////					MoneyCollector
//					saveData.setMoneyCollector(StringUtils.isBlank(data.getMoneyCollector()) ? new BigDecimal(0)
//							: new BigDecimal(data.getMoneyCollector()));
////					MoneyDirectorResidence
//					saveData.setMoneyDirectorResidence(StringUtils.isBlank(data.getMoneyDirectorResidence()) ? new BigDecimal(0)
//							: new BigDecimal(data.getMoneyDirectorResidence()));
////					MoneyOutofSafe
//					saveData.setMoneyOutofSafe(StringUtils.isBlank(data.getMoneyOutofSafe()) ? new BigDecimal(0)
//							: new BigDecimal(data.getMoneyOutofSafe()));
////					MoneySafeLimit
//					saveData.setMoneySafeLimit(StringUtils.isBlank(data.getMoneySafeLimit()) ? new BigDecimal(0)
//							: new BigDecimal(data.getMoneySafeLimit()));
////					StrongroomSi
//					saveData.setStrongroomSi(StringUtils.isBlank(data.getStrongroomSi()) ? new BigDecimal(0)
//							: new BigDecimal(data.getStrongroomSi()));
//					saveData.setMoneyMajorLoss(StringUtils.isBlank(data.getMoneyMajorLoss()) ? new BigDecimal(0)
//							: new BigDecimal(data.getMoneyMajorLoss()));
//
//					if (StringUtils.isNotBlank(saveData.getExchangeRate().toString())) {
//						BigDecimal exRate = saveData.getExchangeRate();
//						if (StringUtils.isNotBlank(data.getMoneyAnnualEstimate())) {
//							saveData.setMoneyAnnualEstimateLc(new BigDecimal(data.getMoneyAnnualEstimate()).multiply(exRate));
//						} else {
//							saveData.setMoneyAnnualEstimateLc(BigDecimal.ZERO);
//						}
//						if (StringUtils.isNotBlank(data.getMoneyCollector())) {
//							saveData.setMoneyCollectorLc(new BigDecimal(data.getMoneyCollector()).multiply(exRate));
//						} else {
//							saveData.setMoneyCollectorLc(BigDecimal.ZERO);
//						}
//						if (StringUtils.isNotBlank(data.getMoneyDirectorResidence())) {
//							saveData.setMoneyDirectorResidenceLc(
//									new BigDecimal(data.getMoneyDirectorResidence()).multiply(exRate));
//						} else {
//							saveData.setMoneyDirectorResidenceLc(BigDecimal.ZERO);
//						}
//						if (StringUtils.isNotBlank(data.getMoneyOutofSafe())) {
//							saveData.setMoneyOutofSafeLc(new BigDecimal(data.getMoneyOutofSafe()).multiply(exRate));
//						} else {
//							saveData.setMoneyOutofSafeLc(BigDecimal.ZERO);
//						}
//						if (StringUtils.isNotBlank(data.getMoneySafeLimit())) {
//							saveData.setMoneySafeLimitLc(new BigDecimal(data.getMoneySafeLimit()).multiply(exRate));
//						} else {
//							saveData.setMoneySafeLimitLc(BigDecimal.ZERO);
//						}
//						if (StringUtils.isNotBlank(data.getMoneyInTransit())) {
//							saveData.setMoneyMajorLossLc(new BigDecimal(data.getMoneyMajorLoss()).multiply(exRate));
//						} else {
//							saveData.setMoneyMajorLossLc(BigDecimal.ZERO);
//						}
//
//						if (StringUtils.isNotBlank(data.getMoneyInSafe())) {
//							saveData.setStrongroomSiLc(new BigDecimal(data.getStrongroomSi()).multiply(exRate));
//						} else {
//							saveData.setStrongroomSiLc(BigDecimal.ZERO);
//						}
//
//					}
					//Burglary
//					saveData.setBurglarySi(StringUtils.isBlank(data.getBurglarySi()) ? new BigDecimal(0)
//							: new BigDecimal(data.getBurglarySi()));
//					if (StringUtils.isNotBlank(saveData.getExchangeRate().toString())) {
//						BigDecimal exRate = saveData.getExchangeRate();


					if (StringUtils.isNotBlank(saveData.getExchangeRate().toString())) {
						BigDecimal exRate = saveData.getExchangeRate();

//						if (StringUtils.isNotBlank(data.getBurglarySi())) {
//							saveData.setBurglarySiLc(new BigDecimal(data.getBurglarySi()).multiply(exRate));
//							;
//						} else {
//							saveData.setBurglarySiLc(BigDecimal.ZERO);
//						}
//					}


					}

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
					buildingRepo.save(saveData);
					
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


			public List<BuildingSectionRes> insertBuildingSectionNonMotor(NonMotorSaveReq req, NonMotorSectionReq section,
					String riskId, Integer locId, String requestReferenceNo, String locationName,Integer Coverid ,List<ProductSectionMaster> sectionList,String productTypeDesc) {
				List<BuildingSectionRes> sectionResList = new ArrayList<BuildingSectionRes>();
				NonMotorPolicyReq policyReq = req.getNonMotorPolicyReq();
				NonMotorBrokerReq brokerReq = req.getNonMotorBrokerReq();
				NonMotEndtReq endtReq = req.getNonMotEndtReq();
				DozerBeanMapper dozerMapper = new DozerBeanMapper();
				try {
					String companyId = policyReq.getCompanyId();
					String productId = policyReq.getProductId();
					String branchCode = policyReq.getBranchCode();
				//	List<ProductSectionMaster> sectionList = getProductSectionDropdown(companyId, productId);

					EserviceSectionDetails secData = new EserviceSectionDetails();
					List<ProductSectionMaster> filterSection = sectionList.stream()
							.filter(o -> o.getSectionId().equals(Integer.valueOf(section.getSectionId())))
							.collect(Collectors.toList());
					ProductSectionMaster sec = filterSection.get(0);
					dozerMapper.map(policyReq, secData);
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

					// Response
					BuildingSectionRes secRes = new BuildingSectionRes();
					secRes.setSectionId(section.getSectionId());
					secRes.setSectionName(sec.getSectionName());
					secRes.setMotorYn(sec.getMotorYn());
					String riskIdStr = section.getRiskId();
					if (riskIdStr == null || riskIdStr.isBlank()) {
					    secRes.setRiskId(0);
					} else {
					    secRes.setRiskId(Integer.valueOf(riskIdStr));
					}
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

					sectionResList.add(secRes);
					eserSecRepo.saveAndFlush(secData);

				} catch (Exception e) {
					e.printStackTrace();
					log.info("Exception Is ---> " + e.getMessage());

				}

				return sectionResList;
			}
			
			public EserviceCommonDetails insertCommonPolicyAndEndtAndBrokerDetails(NonMotorSaveReq req,
					String requestReferenceNo, String riskId, NonMotorSectionReq data, Integer locId, String locationName,
					String sectionName,String address,String companyName,List<ListItemValue> sourcerTypes
					,LoginMaster premiaLogin,LoginUserInfo loginUserData,LoginBranchMaster brokerBranch
					,LoginUserInfo premiaUser,LoginBranchMaster premiaBranch,LoginMaster loginData,List<BrokerCommissionDetails> commissionList) {
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
							Long daysBetween = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

							diff = String.valueOf(daysBetween);
						}

					}
					saveData = new DozerBeanMapper().map(req, EserviceCommonDetails.class);
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
					saveData.setRequestReferenceNo(requestReferenceNo);
					saveData.setQuoteNo(StringUtils.isBlank(policyReq.getQuoteNo())?null:policyReq.getQuoteNo());
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
					BigDecimal exchangeRate = policyReq.getExchangeRate() != null ? new BigDecimal(policyReq.getExchangeRate())
							: BigDecimal.ZERO;
					saveData.setCustomerReferenceNo(policyReq.getCustomerReferenceNo());
					saveData.setProductId(productId);
					saveData.setStatus(StringUtils.isBlank(policyReq.getStatus()) ? "Y" : policyReq.getStatus());
					CompanyProductMaster product = getCompanyProductMasterDropdown(companyId, productId); // productRepo.findByProductIdOrderByAmendIdDesc(Integer.valueOf(productId));
					saveData.setProductDesc(product.getProductName());
					saveData.setSectionId(data.getSectionId());
					saveData.setCompanyId(companyId);
				//	String companyName = getInscompanyMasterDropdown(companyId); // companyRepo.findByCompanyIdOrderByAmendIdDesc(companyId);
					saveData.setCompanyName(companyName);
					saveData.setAddress(address);
					// Endorsement Changes
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
//					List<ListItemValue> sourcerTypes = premiaBrokerService.getSourceTypeDropdown(companyId, branchCode,
//							"SOURCE_TYPE");
					List<ListItemValue> filterSource = sourcerTypes.stream()
							.filter(o -> StringUtils.isNotBlank(brokerReq.getSourceTypeId())
									&& (o.getItemCode().equalsIgnoreCase(brokerReq.getSourceTypeId())
											|| o.getItemValue().equalsIgnoreCase(brokerReq.getSourceTypeId())))
							.collect(Collectors.toList());

					if (filterSource.size() > 0 && directSource.contains(filterSource.get(0).getItemCode())) {

//						String sourceType = filterSource.get(0).getItemValue();
//						String subUserType = sourceType.contains("Broker") ? "Broker"
//								: sourceType.contains("Agent") ? "Agent" : sourceType;
//						LoginMaster premiaLogin = getPremiaBroker(brokerReq.getBdmCode(), subUserType, companyId);
//						String premiaLoginId = premiaLogin != null ? premiaLogin.getLoginId() : "";
		//
//						LoginUserInfo premiaUser = loginUserRepo.findByLoginId(premiaLoginId);
//						LoginUserInfo loginUserData = premiaUser != null ? premiaUser
//								: loginUserRepo.findByLoginId(branchData.getDirectBrokerId());
		//
//						LoginBranchMaster premiaBranch = lbranchRepo.findByLoginIdAndBranchCodeAndCompanyId(premiaLoginId,
//								branchCode, companyId);
//						LoginBranchMaster brokerBranch = premiaBranch != null ? premiaBranch
//								: lbranchRepo.findByLoginIdAndBranchCodeAndCompanyId(branchData.getDirectBrokerId(), branchCode,
//										companyId);

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
//						LoginUserInfo loginUserData = loginUserRepo.findByLoginId(brokerReq.getLoginId());
//						LoginBranchMaster brokerBranch = lbranchRepo.findByLoginIdAndBrokerBranchCodeAndCompanyId(
//								brokerReq.getLoginId(), brokerReq.getBrokerBranchCode(), companyId);
//						LoginMaster loginData = loginRepo.findByLoginId(brokerReq.getLoginId());

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
						LoginMaster issuerData = loginRepo.findByLoginId(brokerReq.getApplicationId());
						saveData.setSubUserType(issuerData != null ? issuerData.getSubUserType() : saveData.getSourceType());
					}

					// Broker Commission
					Double commissionPercent = 0D;
				/**	if (filterSource.size() > 0 && directSource.contains(filterSource.get(0).getItemCode())) {
						// commissionPercent=12.5;
						String sourceType = filterSource.get(0).getItemValue();
						String subUserType = sourceType.contains("Broker") ? "Broker"
								: sourceType.contains("Agent") ? "Agent" :sourceType;
						LoginMaster premiaLogin = getPremiaBroker(brokerReq.getBdmCode(), subUserType, companyId);
						String premiaLoginId = premiaLogin != null ? premiaLogin.getLoginId() : "";

						String commission = getListItem(companyId, branchCode, "COMMISSION_PERCENT",
								filterSource.get(0).getItemValue());
						if (StringUtils.isNotBlank(premiaLoginId)) {
							List<BrokerCommissionDetails> commissionList = getPolicyName(companyId, productId, premiaLoginId,
									brokerReq.getBrokerCode(), "99999", brokerReq.getUserType());
							commission = commissionList.size() > 0 && commissionList.get(0).getCommissionPercentage() != null
									? commissionList.get(0).getCommissionPercentage().toString()
									: commission;
						}
						commissionPercent = StringUtils.isNotBlank(commission) ? Double.valueOf(commission) : 0D;
						saveData.setCommissionPercentage(
								commissionPercent == null ? new BigDecimal("0") : new BigDecimal(commissionPercent));

					} else {**/
//						String loginId = StringUtils.isNotBlank(brokerReq.getSourceType())
//								&& brokerReq.getSourceType().toLowerCase().contains("b2c") ? "guest" : brokerReq.getLoginId();
//						List<BrokerCommissionDetails> commissionList = getPolicyName(companyId, productId, loginId,
//								brokerReq.getBrokerCode(), "99999", brokerReq.getUserType());
//						if (commissionList != null && commissionList.size() > 0) {
//							BrokerCommissionDetails comm = commissionList.get(0);
//							saveData.setCommissionPercentage(comm.getCommissionPercentage() == null ? new BigDecimal("0")
//									: new BigDecimal(comm.getCommissionPercentage()));
//							saveData.setVatCommission(comm.getCommissionVatPercent() == null ? new BigDecimal("0")
//									: new BigDecimal(comm.getCommissionVatPercent()));
//						} else {
//							saveData.setCommissionPercentage(new BigDecimal("0"));
//							saveData.setVatCommission(new BigDecimal("0"));
//						}
					
//					String sourceTypeIdRaw = brokerReq.getSourceTypeId();
//					String sourceTypeRaw   = brokerReq.getSourceType();   
//					String sourceTypeId = sourceTypeIdRaw == null ? "" : sourceTypeIdRaw;
//					String sourceType    = sourceTypeRaw == null ? "" : sourceTypeRaw.toLowerCase();
		//
//					String loginId = null;
//					String userType = "Broker";
//					String brokerCode = null;
		//
//					if ("2".equals(sourceTypeId) || "1".equals(sourceTypeId)) {
		//
//					    loginId = brokerReq.getCustomerName();
//					    LoginMaster agencyCode = loginRepo.findByLoginId(loginId);
//					    brokerCode = (agencyCode != null) ? agencyCode.getAgencyCode() : brokerReq.getBrokerCode();
		//
//					} else if ("broker".equalsIgnoreCase(sourceTypeId) && !sourceType.contains("b2c")) {
		//
//					    loginId = brokerReq.getLoginId();
//					    brokerCode = brokerReq.getBrokerCode();
//					} else {
		//
//					    loginId = brokerReq.getLoginId();
//					    brokerCode = brokerReq.getBrokerCode();
//					}
		//
		//
//					List<BrokerCommissionDetails> commissionList =
//					        getPolicyName(policyReq.getCompanyId(),
//					                policyReq.getProductId(),
//					                loginId,
//					                brokerCode,
//					                "99999",
//					                userType);
					if (commissionList != null && !commissionList.isEmpty()) {

					    BrokerCommissionDetails comm = commissionList.get(0);

					    saveData.setCommissionPercentage(
					            comm.getCommissionPercentage() == null ?
					                    BigDecimal.ZERO : new BigDecimal(comm.getCommissionPercentage()));

					    saveData.setVatCommission(
					            comm.getCommissionVatPercent() == null ?
					                    BigDecimal.ZERO : new BigDecimal(comm.getCommissionVatPercent()));

					} else {
					    
						 CompanyProductMaster cpm = productRepo
						            .findTopByCompanyIdAndProductIdOrderByAmendIdDesc(policyReq.getCompanyId(), Integer.valueOf(policyReq.getProductId()));

					    if (cpm != null) {
					    	saveData.setCommissionPercentage(
					    	        cpm.getCommission() == null ? BigDecimal.ZERO : cpm.getCommission()
					    	    );

					        saveData.setVatCommission( BigDecimal.ZERO);
					    } else {
					       
					        saveData.setCommissionPercentage(BigDecimal.ZERO);
					        saveData.setVatCommission(BigDecimal.ZERO);
					    }
					}

					//}
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
//					saveData.setStatus(StringUtils.isBlank(policyReq.getStatus()) ? "Y" : policyReq.getStatus());
					saveData.setSectionName(sectionName);
					saveData.setCreatedBy(policyReq.getCreatedBy());

					//Industry Id
		 			if (StringUtils.isNotBlank(data.getIndustryId())) {

						saveData.setIndustryId(
								StringUtils.isBlank(data.getIndustryId()) ? null : data.getIndustryId());
						IndustryMaster industry = getIndustryName(companyId, productId, branchCode, data.getIndustryId());
						saveData.setIndustryName(industry != null ? industry.getIndustryName() : "");

					}
		 			else if(StringUtils.isNotBlank(data.getIndustryType())) {
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
					saveData.setOtherOccupation(StringUtils.isBlank(data.getOtherOccupation())?null:data.getOtherOccupation());
					
					// Occupation Type
					if (StringUtils.isNotBlank(data.getCategoryId()) && StringUtils.isBlank(data.getOccupationId())) {
						saveData.setCategoryId(StringUtils.isBlank(data.getCategoryId())?null:data.getCategoryId());
						saveData.setCategoryDesc(StringUtils.isBlank(data.getCategoryDesc())?null:data.getCategoryDesc());
						
					}

					//employers liability Count
					saveData.setTotalNoOfEmployees(StringUtils.isBlank(data.getTotalNoOfEmployees()) ? 0l : Long.valueOf(data.getTotalNoOfEmployees()));
					//Count
					if(StringUtils.isNotBlank(data.getTotalNoOfEmployees())) {
						saveData.setCount(StringUtils.isBlank(data.getTotalNoOfEmployees()) ? 1 : Integer.valueOf(data.getTotalNoOfEmployees()));
					}else if(StringUtils.isNotBlank(data.getFidEmpCount())) {
						saveData.setCount(StringUtils.isBlank(data.getFidEmpCount()) ? 1 : Integer.valueOf(data.getFidEmpCount()));
					}else if(StringUtils.isNotBlank(data.getCount())) {
						saveData.setCount(StringUtils.isBlank(data.getCount()) ? 1 : Integer.valueOf(data.getCount()));
					}
					
					//Fidelity Count
					saveData.setFidEmpCount(StringUtils.isBlank(data.getFidEmpCount()) ? null : new BigDecimal(data.getFidEmpCount()));
					
					//Common suminsured field for additional info 
					// Personal Accident

					BigDecimal sumInsured= BigDecimal.ZERO;
					if(StringUtils.isNotBlank(data.getSumInsured())){
						sumInsured=new BigDecimal(data.getSumInsured());
					}else if(StringUtils.isNotBlank(data.getPersonalAccidentSi())){
						sumInsured=new BigDecimal(data.getPersonalAccidentSi());
					}
//					else	if(StringUtils.isNotBlank(data.getEmpLiabilitySi())){
//						sumInsured=new BigDecimal(data.getEmpLiabilitySi());
//					} 
				//	else	if(StringUtils.isNotBlank(data.getFidEmpSi())){
				//		sumInsured=new BigDecimal(data.getFidEmpSi());
				//	} 
					saveData.setSumInsured(sumInsured);
					// Lc Calculation
					saveData.setSumInsuredLc(sumInsured==null ?   BigDecimal.ZERO: sumInsured.multiply(exchangeRate));
					
					saveData.setNickName(StringUtils.isBlank(data.getNickName())?null:data.getNickName());
					saveData.setDob(data.getDob()==null?null:data.getDob());
					saveData.setRelationType(StringUtils.isBlank(data.getRelationType()) ? null : data.getRelationType());
					String relationTypeDesc = getListItem(companyId, branchCode, "RELATION_TYPE_HOME", data.getRelationType());
					saveData.setRelationTypeDesc(StringUtils.isBlank(relationTypeDesc) ? "" : relationTypeDesc);
					// -------------------------------------------------------
					// Personal Liablity
//					saveData.setPersonalLiabilitySi(StringUtils.isBlank(data.getPersonalLiabilitySi()) ? new BigDecimal(0)
//							: new BigDecimal(data.getPersonalLiabilitySi()));
					// Lc Calculation
//					saveData.setPersonalLiabilitySiLc(StringUtils.isBlank(data.getPersonalLiabilitySi()) ? null
//							: new BigDecimal(data.getPersonalLiabilitySi()).multiply(exchangeRate));
					// -------------------------------------------------------
					// Domestic Servant
//					saveData.setDomesticServentSi(StringUtils.isBlank(data.getDomesticServantSi()) ? new BigDecimal(0)
//							: new BigDecimal(data.getDomesticServantSi()));
					// Lc Calculation
//					saveData.setDomesticServentSiLc(StringUtils.isBlank(data.getDomesticServantSi()) ? null
//							: new BigDecimal(data.getDomesticServantSi()).multiply(exchangeRate));
					saveData.setProfessionalType(
							StringUtils.isBlank(data.getProfessionalType()) ? "" : data.getProfessionalType());
					if(StringUtils.isNotBlank(data.getProfessionalType()))
							{
						String professionalTypeDesc = getListItem(companyId, branchCode, "Servant TYPE",
								data.getProfessionalType());
						saveData.setProfessionalTypeDesc(StringUtils.isBlank(professionalTypeDesc) ? "" : professionalTypeDesc);
							}
					
					
					// -------------------------------------------------------
					//	Employers Liablility
//					saveData.setEmpLiabilitySi(StringUtils.isBlank(data.getEmpLiabilitySi()) ? new BigDecimal(0)
//							: new BigDecimal(data.getEmpLiabilitySi()));
					// Lc Calculation
//					saveData.setEmpLiabilitySiLc(saveData.getEmpLiabilitySi() == null ? null
//							: saveData.getEmpLiabilitySi().multiply(exchangeRate));

					//Fidelity
					saveData.setSumInsured(StringUtils.isBlank(data.getSumInsured()) ? new BigDecimal(0): new BigDecimal(data.getSumInsured()));
					//Lc 

//					saveData.setFidEmpSiLc(saveData.getFidEmpSi() == null ? null : saveData.getFidEmpSi().multiply(exchangeRate));
					
					//Group Personal Accident Period
					saveData.setIndemnityType(StringUtils.isBlank(data.getIndemnityType())?null:data.getIndemnityType());
					saveData.setIndemnityTypeDesc(StringUtils.isBlank(data.getIndemnityTypeDesc())?null:data.getIndemnityTypeDesc());


				//	saveData.setFidEmpSiLc(saveData.getFidEmpSi() == null ? null : saveData.getFidEmpSi().multiply(exchangeRate));
		                
					saveData.setAge(data.getAge());
					saveData.setCoverId(data.getCoverId());
					
				eserCommonRepo.saveAndFlush(saveData);

				} catch (Exception e) {
					e.printStackTrace();
					log.info("Exception Is ---> " + e.getMessage());
					return null;
				}

				return saveData;
			}

			

//			public EserviceCommonDetails insertCommonPolicyAndEndtAndBrokerDetails(NonMotorSaveReq req,
//					String requestReferenceNo, String riskId, NonMotorSectionReq data, Integer locId, String locationName,
//					String sectionName,String address) {
//				EserviceCommonDetails saveData = new EserviceCommonDetails();
//				NonMotorPolicyReq policyReq = req.getNonMotorPolicyReq();
//				NonMotorBrokerReq brokerReq = req.getNonMotorBrokerReq();
//				NonMotEndtReq endtReq = req.getNonMotEndtReq();
//				try {
//					String companyId = policyReq.getCompanyId();
//					String productId = policyReq.getProductId();
//					String branchCode = policyReq.getBranchCode();
//					// Date Differents
//					Date periodStart = policyReq.getPolicyStartDate();
//					Date periodEnd = policyReq.getPolicyEndDate();
//					String diff = "0";
//
//					if (periodStart != null && periodEnd != null) {
//						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//						String st = sdf.format(policyReq.getPolicyStartDate());
//						String ed = sdf.format(policyReq.getPolicyEndDate());
//						if (st.equalsIgnoreCase(ed)
//								&& (endtReq.getEndorsementType() == null || endtReq.getEndorsementType() == 0)) {
//							diff = "1";
//						} else if (st.equalsIgnoreCase(ed)) {
//							diff = "0";
//						} else {
//							Long diffInMillies = Math.abs(periodEnd.getTime() - periodStart.getTime());
//							Long daysBetween = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS) +1;
//
//							diff = String.valueOf(daysBetween);
//						}
//
//					}
//					saveData = new DozerBeanMapper().map(req, EserviceCommonDetails.class);
//					saveData.setParam1(StringUtils.isBlank(data.getParam1())?"":data.getParam1());
//					saveData.setParam2(StringUtils.isBlank(data.getParam2())?"":data.getParam2());
//					saveData.setParam3(StringUtils.isBlank(data.getParam3())?"":data.getParam3());
//					saveData.setParam4(StringUtils.isBlank(data.getParam4())?"":data.getParam4());
//					saveData.setParam5(StringUtils.isBlank(data.getParam5())?"":data.getParam5());
//					saveData.setParam6(StringUtils.isBlank(data.getParam6())?"":data.getParam6());
//					saveData.setParam7(StringUtils.isBlank(data.getParam7())?"":data.getParam7());
//					saveData.setParam8(StringUtils.isBlank(data.getParam8())?"":data.getParam8());
//					saveData.setParam9(StringUtils.isBlank(data.getParam9())?"":data.getParam9());
//					saveData.setParam10(StringUtils.isBlank(data.getParam10())?"":data.getParam10());
//					saveData.setAge(data.getAge());
//					saveData.setRequestReferenceNo(requestReferenceNo);
//					saveData.setQuoteNo(StringUtils.isBlank(policyReq.getQuoteNo())?null:policyReq.getQuoteNo());
//					saveData.setRiskId(Integer.valueOf(riskId));
//					saveData.setOriginalRiskId(Integer.valueOf(riskId));
//					saveData.setLocationId(locId);
//					saveData.setLocationName(locationName);
//					saveData.setSectionId(data.getSectionId());
//					saveData.setSectionName(sectionName);
//					saveData.setPolicyStartDate(periodStart);
//					saveData.setPolicyEndDate(periodEnd);
//					saveData.setPolicyPeriod(Integer.valueOf(diff));
//					saveData.setPromocode(policyReq.getPromocode());
//					saveData.setHavepromocode(policyReq.getHavepromocode());
//					saveData.setCurrency(policyReq.getCurrency());
//					saveData.setEntryDate(new Date());
//					saveData.setUpdatedDate(new Date());
//					saveData.setExchangeRate(StringUtils.isBlank(policyReq.getExchangeRate()) ? null: new BigDecimal(policyReq.getExchangeRate()));
//					BigDecimal exchangeRate = policyReq.getExchangeRate() != null ? new BigDecimal(policyReq.getExchangeRate()): BigDecimal.ZERO;
//					saveData.setCustomerReferenceNo(policyReq.getCustomerReferenceNo());
//					saveData.setProductId(productId);
//					saveData.setStatus(StringUtils.isBlank(policyReq.getStatus()) ? "Y" : policyReq.getStatus());
//					CompanyProductMaster product = getCompanyProductMasterDropdown(companyId, productId); // productRepo.findByProductIdOrderByAmendIdDesc(Integer.valueOf(productId));
//					saveData.setProductDesc(product.getProductName());
//					saveData.setSectionId(data.getSectionId());
//					saveData.setCompanyId(companyId);
//					String companyName = getInscompanyMasterDropdown(companyId); // companyRepo.findByCompanyIdOrderByAmendIdDesc(companyId);
//					saveData.setCompanyName(companyName);
//					saveData.setAddress(address);
//					// Endorsement Changes
//					if(endtReq!=null) {
//					if (!(endtReq.getEndorsementType() == null || endtReq.getEndorsementType() == 0))
//
//					{
//						saveData.setOriginalPolicyNo(endtReq.getOriginalPolicyNo());
//						saveData.setEndorsementDate(endtReq.getEndorsementDate());
//						saveData.setEndorsementRemarks(endtReq.getEndorsementRemarks());
//						saveData.setEndorsementEffdate(endtReq.getEndorsementEffdate());
//						saveData.setEndtPrevPolicyNo(endtReq.getEndtPrevPolicyNo());
//						saveData.setEndtPrevQuoteNo(endtReq.getEndtPrevQuoteNo());
//						saveData.setEndtCount(endtReq.getEndtCount());
//						saveData.setEndtStatus(endtReq.getEndtStatus());
//						saveData.setIsFinyn(endtReq.getIsFinaceYn());
//						saveData.setEndtCategDesc(endtReq.getEndtCategDesc());
//						saveData.setEndorsementType(endtReq.getEndorsementType());
//						saveData.setEndorsementTypeDesc(endtReq.getEndorsementTypeDesc());
//						saveData.setPolicyNo(endtReq.getPolicyNo());
//						saveData.setStatus(StringUtils.isBlank(policyReq.getStatus()) ? "E" : policyReq.getStatus());
//					}
//					}
//
//					// Source Type Details
//					BranchMaster branchData = getBranchMasterRes(companyId, branchCode);
//					saveData.setBranchCode(branchCode);
//					saveData.setBranchName(branchData.getBranchName());
//					saveData.setApplicationId(StringUtils.isBlank(brokerReq.getApplicationId()) ? "1" : brokerReq.getApplicationId());
//
//					// Source Type Search Condition
//					List<String> directSource = new ArrayList<String>();
//					directSource.add("1");
//					directSource.add("2");
//					directSource.add("3");
//					directSource.add("4");
//
//					List<ListItemValue> sourcerTypes = premiaBrokerService.getSourceTypeDropdown(companyId, branchCode,
//							"SOURCE_TYPE");
//					List<ListItemValue> filterSource = sourcerTypes.stream()
//							.filter(o -> StringUtils.isNotBlank(brokerReq.getSourceTypeId())
//									&& (o.getItemCode().equalsIgnoreCase(brokerReq.getSourceTypeId())
//											|| o.getItemValue().equalsIgnoreCase(brokerReq.getSourceTypeId())))
//							.collect(Collectors.toList());
//
//					if (filterSource.size() > 0 && directSource.contains(filterSource.get(0).getItemCode())) {
//
//						String sourceType = filterSource.get(0).getItemValue();
//						String subUserType = sourceType.contains("Broker") ? "Broker"
//								: sourceType.contains("Agent") ? "Agent" : "";
//						LoginMaster premiaLogin = getPremiaBroker(brokerReq.getBdmCode(), subUserType, companyId);
//						String premiaLoginId = premiaLogin != null ? premiaLogin.getLoginId() : "";
//
//						LoginUserInfo premiaUser = loginUserRepo.findByLoginId(premiaLoginId);
//						LoginUserInfo loginUserData = premiaUser != null ? premiaUser
//								: loginUserRepo.findByLoginId(branchData.getDirectBrokerId());
//
//						LoginBranchMaster premiaBranch = lbranchRepo.findByLoginIdAndBranchCodeAndCompanyId(premiaLoginId,
//								branchCode, companyId);
//						LoginBranchMaster brokerBranch = premiaBranch != null ? premiaBranch
//								: lbranchRepo.findByLoginIdAndBranchCodeAndCompanyId(branchData.getDirectBrokerId(), branchCode,
//										companyId);
//
//						saveData.setBrokerCode(loginUserData.getOaCode());
//						saveData.setAgencyCode(loginUserData.getAgencyCode());
//						saveData.setLoginId(loginUserData.getLoginId());
//						saveData.setCustomerCode(brokerReq.getBdmCode());
//						saveData.setCustomerName(brokerReq.getCustomerName());
//						saveData.setBdmName(brokerReq.getBdmName());
//						saveData.setBdmCode(brokerReq.getBdmCode());
//						saveData.setBrokerBranchCode(brokerBranch.getBrokerBranchCode());
//						saveData.setBrokerBranchName(brokerBranch.getBrokerBranchName());
//						saveData.setSourceTypeId(filterSource.get(0).getItemCode());
//						saveData.setSourceType(filterSource.get(0).getItemValue());
//						if (filterSource.get(0).getItemValue().contains("Direct")
//								|| (premiaLogin != null && premiaUser != null && premiaBranch != null)) {
//							saveData.setSalePointCode(brokerBranch.getSalePointCode());
//							saveData.setBrokerTiraCode(loginUserData.getRegulatoryCode());
//						} else {
//							try {
//								// Broker Tira Code
//								PremiaTiraReq brokerTiraCodeReq = new PremiaTiraReq();
//								brokerTiraCodeReq.setInsuranceId(companyId);
//								brokerTiraCodeReq.setPremiaCode(brokerReq.getCustomerCode());
//								List<PremiaTiraRes> brokerTira = premiaBrokerService
//										.searchPremiaBrokerTiraCode(brokerTiraCodeReq);
//								String brokerTiraCode = "";
//								if (brokerTira.size() > 0) {
//									brokerTiraCode = brokerTira.get(0).getTiraCode();
//								}
//
//								// Sale Point Code
//								PremiaTiraReq brokerSpCodeReq = new PremiaTiraReq();
//								brokerSpCodeReq.setInsuranceId(companyId);
//								brokerSpCodeReq.setPremiaCode(brokerTiraCode);
//								List<PremiaTiraRes> brokerSp = premiaBrokerService.searchPremiaBrokerSpCode(brokerSpCodeReq);
//								String brokerSpCode = "";
//								if (brokerSp.size() > 0) {
//									brokerSpCode = brokerSp.get(0).getTiraCode();
//								}
//
//								saveData.setSalePointCode(brokerSpCode);
//								saveData.setBrokerTiraCode(brokerTiraCode);
//							} catch (Exception e) {
//								e.printStackTrace();
//								log.info("Log Details" + e.getMessage());
//
//							}
//						}
//
//					} else {
//						LoginUserInfo loginUserData = loginUserRepo.findByLoginId(brokerReq.getLoginId());
//						LoginBranchMaster brokerBranch = lbranchRepo.findByLoginIdAndBrokerBranchCodeAndCompanyId(
//								brokerReq.getLoginId(), brokerReq.getBrokerBranchCode(), companyId);
//						LoginMaster loginData = loginRepo.findByLoginId(brokerReq.getLoginId());
//
//						saveData.setBrokerCode(loginData.getOaCode());
//						saveData.setAgencyCode(loginData.getAgencyCode());
//						saveData.setCustomerCode(loginUserData.getCustomerCode());
//						saveData.setLoginId(brokerReq.getLoginId());
//						saveData.setCustomerName(loginUserData.getCustomerName());
//						saveData.setBdmCode(null);
//						saveData.setBrokerBranchCode(brokerBranch.getBrokerBranchCode());
//						saveData.setBrokerBranchName(brokerBranch.getBrokerBranchName());
//						saveData.setSalePointCode(brokerBranch.getSalePointCode());
//						saveData.setBrokerTiraCode(loginUserData.getRegulatoryCode());
//
//						List<ListItemValue> filterBrokerSource = sourcerTypes.stream()
//								.filter(o -> o.getItemValue().equalsIgnoreCase(loginData.getSubUserType()))
//								.collect(Collectors.toList());
//
//						saveData.setSourceTypeId(filterBrokerSource.size() > 0 ? filterBrokerSource.get(0).getItemCode() : "");
//						saveData.setSourceType(filterBrokerSource.size() > 0 ? filterBrokerSource.get(0).getItemValue()
//								: loginData.getSubUserType());
//
//					}
//					if ("1".equalsIgnoreCase(brokerReq.getApplicationId())) {
//						saveData.setSubUserType(saveData.getSourceType());
//					} else {
//						LoginMaster issuerData = loginRepo.findByLoginId(brokerReq.getApplicationId());
//						saveData.setSubUserType(issuerData != null ? issuerData.getSubUserType() : saveData.getSourceType());
//					}
//
//					// Broker Commission
//					Double commissionPercent = 0D;
//					if (filterSource.size() > 0 && directSource.contains(filterSource.get(0).getItemCode())) {
//						// commissionPercent=12.5;
//						String sourceType = filterSource.get(0).getItemValue();
//						String subUserType = sourceType.contains("Broker") ? "Broker"
//								: sourceType.contains("Agent") ? "Agent" : "";
//						LoginMaster premiaLogin = getPremiaBroker(brokerReq.getBdmCode(), subUserType, companyId);
//						String premiaLoginId = premiaLogin != null ? premiaLogin.getLoginId() : "";
//
//						String commission = getListItem(companyId, branchCode, "COMMISSION_PERCENT",
//								filterSource.get(0).getItemValue());
//						if (StringUtils.isNotBlank(premiaLoginId)) {
//							List<BrokerCommissionDetails> commissionList = getPolicyName(companyId, productId, premiaLoginId,
//									brokerReq.getBrokerCode(), "99999", brokerReq.getUserType());
//							commission = commissionList.size() > 0 && commissionList.get(0).getCommissionPercentage() != null
//									? commissionList.get(0).getCommissionPercentage().toString()
//									: commission;
//						}
//						commissionPercent = StringUtils.isNotBlank(commission) ? Double.valueOf(commission) : 0D;
//						saveData.setCommissionPercentage(
//								commissionPercent == null ? new BigDecimal("0") : new BigDecimal(commissionPercent));
//
//					} else {
//						String loginId = StringUtils.isNotBlank(brokerReq.getSourceType())
//								&& brokerReq.getSourceType().toLowerCase().contains("b2c") ? "guest" : brokerReq.getLoginId();
//						List<BrokerCommissionDetails> commissionList = getPolicyName(companyId, productId, loginId,
//								brokerReq.getBrokerCode(), "99999", brokerReq.getUserType());
//						if (commissionList != null && commissionList.size() > 0) {
//							BrokerCommissionDetails comm = commissionList.get(0);
//							saveData.setCommissionPercentage(comm.getCommissionPercentage() == null ? new BigDecimal("0")
//									: new BigDecimal(comm.getCommissionPercentage()));
//							saveData.setVatCommission(comm.getCommissionVatPercent() == null ? new BigDecimal("0")
//									: new BigDecimal(comm.getCommissionVatPercent()));
//						} else {
//							saveData.setCommissionPercentage(new BigDecimal("0"));
//							saveData.setVatCommission(new BigDecimal("0"));
//						}
//					}
//					// Endt Commission
//					if (!(endtReq.getEndorsementType() == null || endtReq.getEndorsementType() == 0))
//
//					{
//						List<CommonDataDetails> mainMotList = commonRepo.findByQuoteNo(endtReq.getEndtPrevQuoteNo());
//						if (mainMotList != null && mainMotList.size() > 0) {
//							CommonDataDetails mainMot = mainMotList.get(0);
//							saveData.setCommissionPercentage(
//									mainMot.getCommissionPercentage() == null ? saveData.getCommissionPercentage()
//											: new BigDecimal(mainMot.getCommissionPercentage()));
//							saveData.setVatCommission(mainMot.getVatCommission() == null ? saveData.getVatCommission()
//									: new BigDecimal(mainMot.getVatCommission()));
//						}
//					}
//
//					saveData.setTiraCoverNoteNo(policyReq.getTiraCoverNoteNo());
//					saveData.setBankCode(brokerReq.getBankCode());
//					saveData.setAcExecutiveId(
//							StringUtils.isBlank(policyReq.getAcExecutiveId()) ? null : policyReq.getAcExecutiveId());
//					// Status
////					saveData.setStatus(StringUtils.isBlank(policyReq.getStatus()) ? "Y" : policyReq.getStatus());
//					saveData.setSectionName(sectionName);
//					saveData.setCreatedBy(policyReq.getCreatedBy());
//
//					//Industry Id
//		 			if (StringUtils.isNotBlank(data.getIndustryId())) {
//
//						saveData.setIndustryId(
//								StringUtils.isBlank(data.getIndustryId()) ? null : data.getIndustryId());
//						IndustryMaster industry = getIndustryName(companyId, productId, branchCode, data.getIndustryId());
//						saveData.setIndustryName(industry != null ? industry.getIndustryName() : "");
//
//					}
//		 			else if(StringUtils.isNotBlank(data.getIndustryType())) {
//		 				saveData.setIndustryId(data.getIndustryType());
//		 				saveData.setIndustryName(data.getIndustrytypedesc());
//		 			}
//
//					// Occupation Type
//					if (StringUtils.isNotBlank(data.getOccupationId())) {
//						OccupationMaster occupationData = getOccupationMasterDropdown(companyId, "99999", productId,
//								data.getOccupationId());
//						if(occupationData==null){
//							List<ListItemValue> listItem2 = getListItem2(companyId, branchCode, "EMPLOYESS_OCCUPIED", data.getOccupationId());
//							if(listItem2!=null) {
//							saveData.setOccupationType(data.getOccupationId());
//							saveData.setOccupationDesc(listItem2.get(0).getItemType());
//							saveData.setCategoryId(listItem2.get(0).getParam1());
//							}
//						}else {
//						saveData.setOccupationType(occupationData.getOccupationId().toString());
//						saveData.setOccupationDesc(occupationData.getOccupationName());
//						saveData.setCategoryId(occupationData.getCategoryId());
//						// saveData.setCategoryDesc("Class " + occupationData.getCategoryId());
//						}
//					}
//					saveData.setOtherOccupation(StringUtils.isBlank(data.getOtherOccupation())?null:data.getOtherOccupation());
//					
//					// Occupation Type
//					if (StringUtils.isNotBlank(data.getCategoryId()) && StringUtils.isBlank(data.getOccupationId())) {
//						saveData.setCategoryId(StringUtils.isBlank(data.getCategoryId())?null:data.getCategoryId());
//						saveData.setCategoryDesc(StringUtils.isBlank(data.getCategoryDesc())?null:data.getCategoryDesc());
//						
//					}
//
//					//employers liability Count
//					saveData.setTotalNoOfEmployees(StringUtils.isBlank(data.getTotalNoOfEmployees()) ? StringUtils.isBlank(data.getCount())?0l: Long.valueOf(data.getCount()) : Long.valueOf(data.getTotalNoOfEmployees()));
//					//Count
//					if(StringUtils.isNotBlank(data.getTotalNoOfEmployees())) {
//						saveData.setCount(StringUtils.isBlank(data.getTotalNoOfEmployees()) ? 1 : Integer.valueOf(data.getTotalNoOfEmployees()));
//					}else if(StringUtils.isNotBlank(data.getFidEmpCount())) {
//						saveData.setCount(StringUtils.isBlank(data.getFidEmpCount()) ? 1 : Integer.valueOf(data.getFidEmpCount()));
//					}else if(StringUtils.isNotBlank(data.getCount())) {
//						saveData.setCount(StringUtils.isBlank(data.getCount()) ? 1 : Integer.valueOf(data.getCount()));
//					}
//					
//					//Fidelity Count
//					saveData.setFidEmpCount(StringUtils.isBlank(data.getFidEmpCount()) ? null : new BigDecimal(data.getFidEmpCount()));
//					
//					//Common suminsured field for additional info 
//					// Personal Accident
//
//					BigDecimal sumInsured= BigDecimal.ZERO;
//					if(StringUtils.isNotBlank(data.getSumInsured())){
//						sumInsured=new BigDecimal(data.getSumInsured());
//					}else if(StringUtils.isNotBlank(data.getPersonalAccidentSi())){
//						sumInsured=new BigDecimal(data.getPersonalAccidentSi());
//					}
////					else	if(StringUtils.isNotBlank(data.getEmpLiabilitySi())){
////						sumInsured=new BigDecimal(data.getEmpLiabilitySi());
////					} 
//				//	else	if(StringUtils.isNotBlank(data.getFidEmpSi())){
//				//		sumInsured=new BigDecimal(data.getFidEmpSi());
//				//	} 
//					saveData.setSumInsured(sumInsured);
//					// Lc Calculation
//					saveData.setSumInsuredLc(sumInsured==null ?   BigDecimal.ZERO: sumInsured.multiply(exchangeRate));
//					
//					saveData.setNickName(StringUtils.isBlank(data.getNickName())?null:data.getNickName());
//					saveData.setDob(data.getDob()==null?null:data.getDob());
//					saveData.setRelationType(StringUtils.isBlank(data.getRelationType()) ? null : data.getRelationType());
//					String relationTypeDesc = getListItem(companyId, branchCode, "RELATION_TYPE_HOME", data.getRelationType());
//					saveData.setRelationTypeDesc(StringUtils.isBlank(relationTypeDesc) ? "" : relationTypeDesc);
//					// -------------------------------------------------------
//					// Personal Liablity
////					saveData.setPersonalLiabilitySi(StringUtils.isBlank(data.getPersonalLiabilitySi()) ? new BigDecimal(0)
////							: new BigDecimal(data.getPersonalLiabilitySi()));
//					// Lc Calculation
////					saveData.setPersonalLiabilitySiLc(StringUtils.isBlank(data.getPersonalLiabilitySi()) ? null
////							: new BigDecimal(data.getPersonalLiabilitySi()).multiply(exchangeRate));
//					// -------------------------------------------------------
//					// Domestic Servant
////					saveData.setDomesticServentSi(StringUtils.isBlank(data.getDomesticServantSi()) ? new BigDecimal(0)
////							: new BigDecimal(data.getDomesticServantSi()));
//					// Lc Calculation
////					saveData.setDomesticServentSiLc(StringUtils.isBlank(data.getDomesticServantSi()) ? null
////							: new BigDecimal(data.getDomesticServantSi()).multiply(exchangeRate));
//					saveData.setProfessionalType(
//							StringUtils.isBlank(data.getProfessionalType()) ? "" : data.getProfessionalType());
//					String professionalTypeDesc = getListItem(companyId, branchCode, "Servant TYPE",
//							data.getProfessionalType());
//					saveData.setProfessionalTypeDesc(StringUtils.isBlank(professionalTypeDesc) ? "" : professionalTypeDesc);
//					// -------------------------------------------------------
//					//	Employers Liablility
////					saveData.setEmpLiabilitySi(StringUtils.isBlank(data.getEmpLiabilitySi()) ? new BigDecimal(0)
////							: new BigDecimal(data.getEmpLiabilitySi()));
//					// Lc Calculation
////					saveData.setEmpLiabilitySiLc(saveData.getEmpLiabilitySi() == null ? null
////							: saveData.getEmpLiabilitySi().multiply(exchangeRate));
//
//					//Fidelity
//					saveData.setSumInsured(StringUtils.isBlank(data.getSumInsured()) ? new BigDecimal(0): new BigDecimal(data.getSumInsured()));
//					//Lc 
//
////					saveData.setFidEmpSiLc(saveData.getFidEmpSi() == null ? null : saveData.getFidEmpSi().multiply(exchangeRate));
//					
//					//Group Personal Accident Period
//					saveData.setIndemnityType(StringUtils.isBlank(data.getIndemnityType())?null:data.getIndemnityType());
//					saveData.setIndemnityTypeDesc(StringUtils.isBlank(data.getIndemnityTypeDesc())?null:data.getIndemnityTypeDesc());
//
//
//				//	saveData.setFidEmpSiLc(saveData.getFidEmpSi() == null ? null : saveData.getFidEmpSi().multiply(exchangeRate));
//		                
//
//					saveData.setCoverId(data.getCoverId());
//					
//				eserCommonRepo.saveAndFlush(saveData);
//
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("Exception Is ---> " + e.getMessage());
//					return null;
//				}
//
//				return saveData;
//			}

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
//						policyRes.setPolicyStartDate(humman.get(0).getPolicyStartDate());
//						policyRes.setPolicyEndDate(humman.get(0).getPolicyEndDate());
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
////						policyRes.setPolicyStartDate(asset.get(0).getPolicyStartDate());
//						policyRes.setPolicyEndDate(asset.get(0).getPolicyEndDate());
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
//					List<FirstLossPayee> firsLosslist= new ArrayList<FirstLossPayee>();
//					  CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
//			            CriteriaQuery<FirstLossPayee> criteriaQuery = criteriaBuilder.createQuery(FirstLossPayee.class);
//			            Root<FirstLossPayee> root = criteriaQuery.from(FirstLossPayee.class);
		//
//			            criteriaQuery.select(root);
//			            TypedQuery<FirstLossPayee> query = em.createQuery(criteriaQuery);
//			            firsLosslist = query.getResultList();
//			            firsLosslist = firsLosslist.stream().filter(distinctByKey(o -> Arrays.asList(o.getFirstLossPayeeId()))).collect(Collectors.toList());
//			            Integer firstLossId=firsLosslist.size()>0?firsLosslist.get(0).getFirstLossPayeeId():0;
//			            System.out.println("***********firstLossId :"+firstLossId);
			            for(FirstLossPayeeReq r:req) {
						FirstLossPayee save=new FirstLossPayee();
						save  = dozerMapper.map(r, FirstLossPayee.class);
						save.setStatus("Y");
						save.setEntryDate(new Date());
						save.setLocationId(Integer.valueOf(r.getLocationId()));
//						firstLossId+=1;
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
		
		public synchronized List<ListItemValue> getListItem2(String insuranceId, String branchCode, String itemType, String itemCode) {
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

				return list;
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Exception is ---> " + e.getMessage());
				return null;
			}
		}

		

		public CommonRes deleteSectionAndCover(SectionSaveReq req, Set<String> allTypes,List<EserviceSectionDetails> sec) {
		    CommonRes res = new CommonRes();
		    try {
		    	Integer product = Integer.valueOf(req.getProductId());
		    	Integer locationId=Integer.valueOf(req.getLocationId());
		    	Integer section=Integer.valueOf(req.getSectionId());
		    	String meg="";
		        if (allTypes != null && !allTypes.isEmpty()) {
		            // Loop through all types and delete accordingly
		            for (String type : allTypes) {
		            	
		                if ("A".equalsIgnoreCase(type)) {
		                    List<EserviceBuildingDetails> building = buildingRepo.findAllByRequestReferenceNoAndSectionIdAndProductIdAndLocationId(req.getRequestRefNo(), req.getSectionId(),req.getProductId(),locationId);
		                    List<MsAssetDetails> ast= msAssetRepo.findByRequestReferenceNoAndLocationIdAndSectionIdAndProductId(req.getRequestRefNo(),locationId,section,product);
		                    List<MsCommonDetails> com =msCommon.findByRequestreferencenoAndSectionIdAndProductIdAndLocationId(req.getRequestRefNo(),section,product,locationId);
		                    if (com != null && !com.isEmpty())
		                    {
		                    	msCommon.deleteAll(com);
		                    	meg="msCommon ,";
		                    	
		                    }
		                    if (ast != null && !ast.isEmpty())
		                    {
		                    	msAssetRepo.deleteAll(ast);
		                    	meg=meg+"msHuman ,";
		                    }
		                    if (building != null && !building.isEmpty()) {
		                        buildingRepo.deleteAll(building);
		                        res.setCommonResponse(meg+"Building(s) deleted");
		                        res.setIsError(false);
		                        res.setMessage("Sucess");		                   
		                        }
		                } else if ("H".equalsIgnoreCase(type)) {
		                    List<EserviceCommonDetails> human = humanRepo.findByRequestReferenceNoAndSectionIdAndProductIdAndLocationId(req.getRequestRefNo(), req.getSectionId(),req.getProductId(),locationId);
		                    List<MsHumanDetails> mshuman= mshumanRepo.findByRequestReferenceNoAndLocationIdAndSectionid(req.getRequestRefNo(),locationId,section);
		                    List<MsCommonDetails> com =msCommon.findByRequestreferencenoAndSectionIdAndProductIdAndLocationId(req.getRequestRefNo(),section,product,locationId);
		                    if (com != null && !com.isEmpty())
		                    {
		                    	msCommon.deleteAll(com);
		                    	meg="msCommon ,";
		                    }
		                    if (human != null && !human.isEmpty()) {
		                    	mshumanRepo.deleteAll(mshuman);  
		                    	meg=meg+"msHuman ,";
		                    	}
		                    if (human != null && !human.isEmpty()) {
		                        humanRepo.deleteAll(human);
		                        res.setCommonResponse(meg+"Human(s) deleted");
		                        res.setIsError(false);
		                        res.setMessage("Sucess");
		                    }
		                } else {
		                    // Optional: handle other types if needed
		                    log.info("Unknown type: " + type);
		                    res.setCommonResponse("None of them deleted ");
		                    res.setIsError(true);
		                    res.setMessage("Faild");
		                    
		                }
		            }
		        }
		        secRepo.deleteAll(sec);
		        // Delete section record itself
		       // secRepo.deleteByRequestReferenceNoAndSectionIdAndProductIdAndLocationId(req.getRequestRefNo(),req.getSectionId(),req.getProductId(),locationId);
		        res.setCommonResponse((res.getCommonResponse() != null ? res.getCommonResponse() + " and Section deleted" : "Section deleted"));

		    } catch (Exception e) {
		        e.printStackTrace();
		        log.info("Exception is ---> " + e.getMessage());
		        res.setIsError(true);
		        res.setMessage("Failed to delete section and associated details: " + e.getMessage());
		    }

		    return res;
		}
		

		


}
