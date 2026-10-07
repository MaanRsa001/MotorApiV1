package com.maan.eway.common.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.maan.eway.bean.CompanyProductMaster;
import com.maan.eway.bean.CurrencyMaster;
import com.maan.eway.bean.EserviceBuildingDetails;
import com.maan.eway.bean.EserviceCommonDetails;
import com.maan.eway.bean.EserviceMotorDetails;
import com.maan.eway.common.req.ChangeOfCurrencyReq;
import com.maan.eway.common.res.SuccessRes;
import com.maan.eway.repository.BuildingRiskDetailsRepository;
import com.maan.eway.repository.CommonDataDetailsRepository;
import com.maan.eway.repository.CompanyProductMasterRepository;
import com.maan.eway.repository.EServiceBuildingDetailsRepository;
import com.maan.eway.repository.EServiceMotorDetailsRepository;
import com.maan.eway.repository.EServiceSectionDetailsRepository;
import com.maan.eway.repository.EserviceCommonDetailsRepository;
import com.maan.eway.repository.EserviceTravelDetailsRepository;
import com.maan.eway.repository.FactorRateRequestDetailsRepository;
import com.maan.eway.repository.InsuranceCompanyMasterRepository;
import com.maan.eway.repository.ListItemValueRepository;
import com.maan.eway.repository.LoginBranchMasterRepository;
import com.maan.eway.repository.LoginMasterRepository;
import com.maan.eway.repository.LoginUserInfoRepository;
import com.maan.eway.repository.ProductSectionMasterRepository;
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

@Service
public class ChangeOfCurrencyServiceImpl {
	

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
	private TrackingDetailsServiceImpl trackService ;

	
	@Autowired
	private FactorRateRequestDetailsRepository factorRepo;

	@Autowired
	private CommonDataDetailsRepository commonRepo ;
	
	@Autowired
	private BuildingRiskDetailsRepository motBuildingRepo;
	
	@Autowired
	private LoginUserInfoRepository loginUserRepo ;
	
	@Autowired
	private EServiceMotorDetailsRepository eserMotRepo ;
	
	@Autowired
	private EserviceTravelDetailsRepository eserTraRepo ;
	
	
	@Value(value = "${travel.productId}")
	private String travelProductId;
	
	private Logger log = LogManager.getLogger(EserviceBuildingDetailsServiceImpl.class);


	
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

	public SuccessRes updateChangeOfCurrencySuminsured(ChangeOfCurrencyReq req) {
		SuccessRes res = new SuccessRes();
		try {
			CompanyProductMaster product =  getCompanyProductMasterDropdown(req.getInsuranceId() , req.getProductId());
			
			String decimalDigits = currencyDecimalFormat(req.getInsuranceId() , req.getCurrency() ).toString();
			String stringFormat = "%0"+decimalDigits+"d" ;
			String decimalLength = decimalDigits.equals("0") ?"" : String.format(stringFormat ,0L)  ;
			String pattern = StringUtils.isBlank(decimalLength) ?  "#####0" :   "#####0." + decimalLength;
			DecimalFormat df = new DecimalFormat(pattern);
			

			 if(product.getMotorYn().equalsIgnoreCase("H") &&  req.getProductId().equalsIgnoreCase(travelProductId)) {
					// Travel Product Details
				//	res =	updateTravelProductSuminsured( req , df);
					
			 } else if(product.getMotorYn().equalsIgnoreCase("M") ) {
				// Motor Product Details
				res =  updateMotorProductSuminsured( req, df);
				
			} else if(product.getMotorYn().equalsIgnoreCase("A") ) {
				// Asset Details
				res =	updateAssetProductSuminsured( req , df);
				
			} else {
				// Human Product Details
				res =	updateHumanProductSuminsured( req , df);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is --->" + e.getMessage());
			return null;
		}
		return res ;
	}
	
//	public SuccessRes updateTravelProductSuminsured(ChangeOfCurrencyReq req ,DecimalFormat df) {
//		SuccessRes res = new SuccessRes();
//		try {
//			
//			
//			
//		} catch ( Exception e) {
//			e.printStackTrace();
//			log.info("Exception is ---> " + e.getMessage());
//			return null;
//		}
//		return res;
//	}

	public SuccessRes updateMotorProductSuminsured(ChangeOfCurrencyReq req ,DecimalFormat df) {
		SuccessRes res = new SuccessRes();
		try {
			String oldCurrency = "" ;
			BigDecimal oldExchangeRate = null ;
			
			String newCurrency = req.getCurrency();
			BigDecimal newExchangeRate =  new BigDecimal(req.getExchangeRate()) ;
			
			List<EserviceMotorDetails> motDatas = eserMotRepo.findByRequestReferenceNo(req.getRequestReferenceNo());
			if(motDatas.size() > 0 ) {
				EserviceMotorDetails data = motDatas.get(0) ;
					oldCurrency = data.getCurrency() ;
					oldExchangeRate = data.getExchangeRate() ;
					
				if( ! newCurrency.equalsIgnoreCase(oldCurrency)   ) {
					String calcType = newExchangeRate.compareTo(BigDecimal.ONE) == 0 ? "multiply" : "divide" ;
					BigDecimal exchange = "multiply".equalsIgnoreCase(calcType) ? oldExchangeRate : newExchangeRate ;
					
					for(EserviceMotorDetails mot : motDatas) {
						mot.setCurrency(newCurrency);
						mot.setExchangeRate(newExchangeRate);
						mot.setSumInsured(exchangeFcCalc( exchange , mot.getSumInsured() , calcType ,df)   ) ;
						mot.setAcccessoriesSumInsured(exchangeFcCalc( exchange , mot.getAcccessoriesSumInsured() , calcType ,df)   );
						mot.setWindScreenSumInsured(exchangeFcCalc( exchange , mot.getWindScreenSumInsured() , calcType ,df)   );
						mot.setTppdIncreaeLimit(exchangeFcCalc( exchange , mot.getTppdIncreaeLimit() , calcType ,df)   );
						mot.setTppdFreeLimit(null);
					}
					eserMotRepo.saveAll(motDatas);
				}
			}
			
			
			res.setResponse("Successfully Updated");
			res.setSuccessId("1");
		} catch ( Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null;
		}
		return res;
	}


	public BigDecimal exchangeFcCalc(BigDecimal exchangeRate , BigDecimal oldSuminsured ,String calcType,DecimalFormat df) {
		BigDecimal sumInsured = null;
		try {
			if(oldSuminsured !=null  ) {
				if("divide".equalsIgnoreCase(calcType)) {
					sumInsured = new BigDecimal(df.format(oldSuminsured.divide(exchangeRate,4,RoundingMode.HALF_UP)));
					
				} else if("multiply".equalsIgnoreCase(calcType)) {
					sumInsured = new BigDecimal(df.format(oldSuminsured.multiply(exchangeRate)));
				} 
			} else {
				sumInsured = null ;
			}
	}
	catch(Exception e) {
		e.printStackTrace();
		log.info("Log Details" + e.getMessage());
			return null;
		}return sumInsured;
	}
	
	public SuccessRes updateAssetProductSuminsured(ChangeOfCurrencyReq req ,DecimalFormat df) {
		SuccessRes res = new SuccessRes();
		try {
			String oldCurrency = "" ;
			BigDecimal oldExchangeRate = null ;
			
			String newCurrency = req.getCurrency();
			BigDecimal newExchangeRate =  new BigDecimal(req.getExchangeRate()) ;

			
			List<EserviceBuildingDetails> buildDatas = buildingRepo.findByRequestReferenceNo(req.getRequestReferenceNo());
			if(buildDatas.size() > 0 ) {
				EserviceBuildingDetails data = buildDatas.get(0) ;
					oldCurrency = data.getCurrency() ;
					oldExchangeRate = data.getExchangeRate() ;
					
				if( ! newCurrency.equalsIgnoreCase(oldCurrency)   ) {
					String calcType = newExchangeRate.compareTo(BigDecimal.ONE) == 0 ? "multiply" : "divide" ;
					BigDecimal exchange = "multiply".equalsIgnoreCase(calcType) ? oldExchangeRate : newExchangeRate ;
					
					for(EserviceBuildingDetails build : buildDatas) {
						build.setCurrency(newCurrency);
//						build.setExchangeRate(newExchangeRate);


						build.setExchangeRate(newExchangeRate);

//						build.setBuildingSuminsured(exchangeFcCalc( exchange , build.getBuildingSuminsured() , calcType ,df)   );
//						build.setContentSuminsured(exchangeFcCalc( exchange , build.getContentSuminsured() , calcType ,df)   );
//						build.setAllriskSuminsured(exchangeFcCalc( exchange , build.getAllriskSuminsured() , calcType ,df)   );
//						build.setMiningPlantSi(exchangeFcCalc( exchange , build.getMiningPlantSi() , calcType ,df)   );
//						build.setNonminingPlantSi(exchangeFcCalc( exchange , build.getNonminingPlantSi() , calcType ,df)   );
//						build.setGensetsSi(exchangeFcCalc( exchange , build.getGensetsSi() , calcType ,df)   );
//						build.setEquipmentSi(exchangeFcCalc( exchange , build.getEquipmentSi() , calcType ,df)   );
//						build.setStockInTradeSi(exchangeFcCalc( exchange , build.getStockInTradeSi() , calcType ,df)   );;
//						build.setGoodsSi(exchangeFcCalc( exchange , build.getGoodsSi() , calcType ,df)   );	
//						build.setFurnitureSi(exchangeFcCalc( exchange , build.getFurnitureSi() , calcType ,df)   );
//						build.setCashValueablesSi(exchangeFcCalc( exchange , build.getCashValueablesSi() , calcType ,df)   );
//						build.setApplianceSi(exchangeFcCalc( exchange , build.getApplianceSi() , calcType ,df)   );;
//						build.setStockInTradeSi(exchangeFcCalc( exchange , build.getStockInTradeSi() , calcType ,df)   );
//						build.setBuildingSuminsured(exchangeFcCalc( exchange , build.getBuildingSuminsured() , calcType ,df)   );	
//						build.setEquipmentSi(exchangeFcCalc( exchange , build.getEquipmentSi() , calcType ,df)   );

//						build.setFirePlantSi(exchangeFcCalc( exchange , build.getFirePlantSi() , calcType ,df)   );


//						build.setFirePlantSi(exchangeFcCalc( exchange , build.getFirePlantSi() , calcType ,df)   );


//						build.setElecEquipSuminsured(exchangeFcCalc( exchange , build.getElecEquipSuminsured() , calcType ,df)   );
//						build.setMoneyAnnualEstimate(exchangeFcCalc( exchange , build.getMoneyAnnualEstimate() , calcType ,df)   );
//						build.setMoneyCollector(exchangeFcCalc( exchange , build.getMoneyCollector() , calcType ,df)   );
//						build.setMoneyDirectorResidence(exchangeFcCalc( exchange , build.getMoneyDirectorResidence() , calcType ,df)   );
//						build.setMoneyOutofSafe(exchangeFcCalc( exchange , build.getMoneyOutofSafe() , calcType ,df)   );
//						build.setMoneySafeLimit(exchangeFcCalc( exchange , build.getMoneySafeLimit() , calcType ,df)   );
//						build.setMoneyMajorLoss(exchangeFcCalc( exchange , build.getMoneyMajorLoss() , calcType ,df)   );
//						build.setBoilerPlantsSi(exchangeFcCalc( exchange , build.getBoilerPlantsSi() , calcType ,df)   );
//						build.setElecMachinesSi(exchangeFcCalc( exchange , build.getElecMachinesSi() , calcType ,df)   );
//						build.setEquipmentSi(exchangeFcCalc( exchange , build.getEquipmentSi() , calcType ,df)   );
//						build.setGeneralMachineSi(exchangeFcCalc( exchange , build.getGeneralMachineSi() , calcType ,df)   );
//						build.setMachineEquipSi(exchangeFcCalc( exchange , build.getMachineEquipSi() , calcType ,df)   );
//						build.setManuUnitsSi(exchangeFcCalc( exchange , build.getManuUnitsSi() , calcType ,df)   );
//						build.setPowerPlantSi(exchangeFcCalc( exchange , build.getPowerPlantSi() , calcType ,df)   );

						
						build.setSumInsured(exchangeFcCalc( exchange , build.getSumInsured() , calcType ,df)   );


						
						build.setSumInsured(exchangeFcCalc( exchange , build.getSumInsured() , calcType ,df)   );



						
					}
					buildingRepo.saveAll(buildDatas);
				}
				
				res =	updateHumanProductSuminsured( req , df);
			}
			
			
			
			res.setResponse("Successfully Updated");
			res.setSuccessId("1");
		} catch ( Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null;
		}
		return res;
	}

	public SuccessRes updateHumanProductSuminsured(ChangeOfCurrencyReq req ,DecimalFormat df) {
		SuccessRes res = new SuccessRes();
		try {
			String oldCurrency = "" ;
			BigDecimal oldExchangeRate = null ;
			
			String newCurrency = req.getCurrency();
			BigDecimal newExchangeRate =  new BigDecimal(req.getExchangeRate()) ;
			
			List<EserviceCommonDetails> humDatas = humanRepo.findByRequestReferenceNo(req.getRequestReferenceNo());
			if(humDatas.size() > 0 ) {
				EserviceCommonDetails data = humDatas.get(0) ;
					oldCurrency = data.getCurrency() ;
					oldExchangeRate = data.getExchangeRate() ;
					
				if( ! newCurrency.equalsIgnoreCase(oldCurrency)   ) {
					String calcType = newExchangeRate.compareTo(BigDecimal.ONE) == 0 ? "multiply" : "divide" ;
					BigDecimal exchange = "multiply".equalsIgnoreCase(calcType) ? oldExchangeRate : newExchangeRate ;
					
					for(EserviceCommonDetails human : humDatas) {
						human.setCurrency(newCurrency);
						human.setExchangeRate(newExchangeRate);
						human.setSumInsured(exchangeFcCalc( exchange , human.getSumInsured() , calcType ,df)   ) ;
//						human.setPersonalLiabilitySi(exchangeFcCalc( exchange , human.getPersonalLiabilitySi() , calcType ,df)   );
//						human.setEmpLiabilitySi(exchangeFcCalc( exchange , human.getEmpLiabilitySi() , calcType ,df)   );
						//human.setFidEmpSi(exchangeFcCalc( exchange , human.getFidEmpSi() , calcType ,df)   );
//						human.setAooSuminsured(exchangeFcCalc( exchange , human.getAooSuminsured() , calcType ,df)   );
//						human.setAggSuminsured(exchangeFcCalc( exchange , human.getAggSuminsured() , calcType ,df)   );
//						human.setLiabilitySi(exchangeFcCalc( exchange , human.getLiabilitySi() , calcType ,df)   );
					}
					humanRepo.saveAll(humDatas);
				}
			}
			
			
			
			res.setResponse("Successfully Updated");
			res.setSuccessId("1");
		} catch ( Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null;
		}
		return res;
	}

	public Integer currencyDecimalFormat(String insuranceId  ,String currencyId ) {
		Integer decimalFormat = 0 ;
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
			Root<CurrencyMaster>    c = query.from(CurrencyMaster.class);		
			
			// Select
			query.select(c);
			
		
			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(c.get("currencyName")));
			
			// Effective Date Max Filter
			Subquery<Date> effectiveDate = query.subquery(Date.class);
			Root<CurrencyMaster> ocpm1 = effectiveDate.from(CurrencyMaster.class);
			effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
			Predicate a11 = cb.equal(c.get("currencyId"),ocpm1.get("currencyId") );
			Predicate a12 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
			Predicate a18 = cb.equal(c.get("status"),ocpm1.get("status") );
			Predicate a22 = cb.equal(c.get("companyId"), ocpm1.get("companyId"));
			
			effectiveDate.where(a11,a12,a18,a22);
			
			// Effective Date Max Filter
			Subquery<Date> effectiveDate2 = query.subquery(Date.class);
			Root<CurrencyMaster> ocpm2 = effectiveDate2.from(CurrencyMaster.class);
			effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
			Predicate a13 = cb.equal(c.get("currencyId"),ocpm2.get("currencyId") );
			Predicate a14 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
			Predicate a19 = cb.equal(c.get("status"),ocpm2.get("status") );
			Predicate a23 = cb.equal(c.get("companyId"), ocpm2.get("companyId"));
			
			effectiveDate2.where(a13,a14,a19,a23);
			
		    // Where	
			Predicate n1 = cb.equal(c.get("status"), "Y");
			Predicate n2 = cb.equal(c.get("effectiveDateStart"), effectiveDate);
			Predicate n3 = cb.equal(c.get("effectiveDateEnd"), effectiveDate2);
			Predicate n4 = cb.equal(c.get("companyId"),insuranceId);
//			Predicate n5 = cb.equal(c.get("companyId"),"99999");
//			Predicate n6 = cb.or(n4,n5);
			Predicate n7 = cb.equal(c.get("currencyId"),currencyId);
			query.where(n1,n2,n3,n4,n7).orderBy(orderList);
			
			// Get Result
			TypedQuery<CurrencyMaster> result = em.createQuery(query);			
			list =  result.getResultList(); 
			
			decimalFormat = list.size() > 0 ? (list.get(0).getDecimalDigit()==null?0 :list.get(0).getDecimalDigit()) :0; 		
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null;
		}
		return decimalFormat;
	}
}
