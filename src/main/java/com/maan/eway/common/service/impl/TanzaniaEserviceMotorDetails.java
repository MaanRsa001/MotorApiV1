package com.maan.eway.common.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.maan.eway.bean.BranchMaster;
import com.maan.eway.bean.BrokerCommissionDetails;
import com.maan.eway.bean.CompanyProductMaster;
import com.maan.eway.bean.EserviceCustomerDetails;
import com.maan.eway.bean.EserviceDriverDetails;
import com.maan.eway.bean.EserviceMotorDetails;
import com.maan.eway.bean.EserviceSectionDetails;
import com.maan.eway.bean.HomePositionMaster;
import com.maan.eway.bean.InsuranceCompanyMaster;
import com.maan.eway.bean.ListItemValue;
import com.maan.eway.bean.LoginBranchMaster;
import com.maan.eway.bean.LoginMaster;
import com.maan.eway.bean.LoginUserInfo;
import com.maan.eway.bean.MotorBodyTypeMaster;
import com.maan.eway.bean.MotorColorMaster;
import com.maan.eway.bean.MotorDataDetails;
import com.maan.eway.bean.MotorDriverDetails;
import com.maan.eway.bean.MotorMakeModelMaster;
import com.maan.eway.bean.MotorVehicleInfo;
import com.maan.eway.bean.MotorVehicleUsageMaster;
import com.maan.eway.bean.ProductSectionMaster;
import com.maan.eway.common.req.DepreciationReq;
import com.maan.eway.common.req.DriverDetailsGetReq;
import com.maan.eway.common.req.DriverDetailsGetRes;
import com.maan.eway.common.req.DriverDetailsRes;
import com.maan.eway.common.req.EserviceMotorDetailsGetReq;
import com.maan.eway.common.req.EserviceMotorDetailsSaveReq;
import com.maan.eway.common.req.EserviceMotorDetailsSaveRes;
import com.maan.eway.common.req.MotorDriverSaveReq;
import com.maan.eway.common.req.SequenceGenerateReq;
import com.maan.eway.common.res.EserviceMotorDetailsRes;
import com.maan.eway.common.res.PremiaTiraReq;
import com.maan.eway.common.res.PremiaTiraRes;
import com.maan.eway.repository.CompanyProductMasterRepository;
import com.maan.eway.repository.EServiceDriverDetailsRepository;
import com.maan.eway.repository.EServiceMotorDetailsMapper;
import com.maan.eway.repository.EServiceMotorDetailsRepository;
import com.maan.eway.repository.EServiceSectionDetailsRepository;
import com.maan.eway.repository.EserviceCustomerDetailsRepository;
import com.maan.eway.repository.HomePositionMasterRepository;
import com.maan.eway.repository.LoginBranchMasterRepository;
import com.maan.eway.repository.LoginMasterRepository;
import com.maan.eway.repository.LoginUserInfoRepository;
import com.maan.eway.repository.MotorBodyTypeMasterRepository;
import com.maan.eway.repository.MotorColorMasterRepository;
import com.maan.eway.repository.MotorDataDetailsRepository;
import com.maan.eway.repository.MotorDriverDetailsRepository;
import com.maan.eway.repository.MotorMakeModelMasterRepository;
import com.maan.eway.repository.MotorVehicleInfoRepository;
import com.maan.eway.repository.MotorVehicleUsageMasterRepository;
import com.maan.eway.req.OneTimeTableReq;
import com.maan.eway.res.OneTimeTableRes;
import com.maan.eway.service.OneTimeService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class TanzaniaEserviceMotorDetails {
	private Logger log = LogManager.getLogger(TanzaniaEserviceMotorDetails.class);
	@PersistenceContext
	private EntityManager em;
	
	Gson json = new Gson();
	@Autowired
	private EServiceMotorDetailsRepository repo;
	
	@Autowired
	private HomePositionMasterRepository homeRepo;
	
	@Autowired
	private MotorVehicleInfoRepository motorVehRepo;
	
	@Autowired
	private EserviceMotorDetailsServiceImpl eserMotDetServImpl;
	
	@Autowired
	private LoginMasterRepository loginRepo;
	
	@Autowired
	private GenerateSeqNoServiceImpl genSeqNoService;
	
	@Autowired
	private PremiaBrokerServiceImpl premiaBrokerService;
	
	@Autowired
	private OneTimeService otService;
	
	@Autowired
	private MotorDataDetailsRepository motRepo;
	
	@Autowired
	private EserviceCustomerDetailsRepository custRepo;
	
	@Autowired
	private LoginUserInfoRepository loginUserRepo;
	
	@Autowired
	private LoginBranchMasterRepository lbranchRepo;
	
	@Autowired
	private MotorMakeModelMasterRepository modelrepo;
	
	@Autowired
	private CompanyProductMasterRepository companyProductMasterRepo;
	
	@Autowired
	private EServiceSectionDetailsRepository eserSecRepo;
	
	@Autowired
	private EServiceDriverDetailsRepository eserDriverRepo;
	
	@Autowired
	private MotorDriverDetailsRepository driverRepo;
	
	@Autowired
	private MotorColorMasterRepository motorColorMasterRepository;
	@Autowired
	private MotorVehicleUsageMasterRepository motorVehicleUsageMasterRepo;
	
	@Autowired
	private MotorBodyTypeMasterRepository bodyTypeMasterRepo;

	@Autowired
	@Qualifier("MotorLookupExecutor")
	private Executor motorLookupExecutor;

	public  static LocalDate convertToLocalDate(Date dateToConvert) {
		 if (dateToConvert == null) {
	            return null;
	        }
	        return dateToConvert.toInstant()
	                            .atZone(ZoneId.systemDefault())
	                            .toLocalDate();
	    }
	
	public List<String> validateMotorDetails(EserviceMotorDetailsSaveReq req) {
		List<String> error = new ArrayList<String>();
		long start = System.currentTimeMillis();
		System.out.println("Save Motor validation started block  at line 161 : "
				+( start));
		try {
			
			boolean skipVal = false;

			if (isTrailerVehicle(req)) {

			    if (Boolean.FALSE.equals(req.getSearchFromApi())) {
			        if (StringUtils.isNotBlank(req.getCompanyId())
			                && StringUtils.isNotBlank(req.getMotorUsageId())) {
			            MotorVehicleUsageMaster usage =
			                motorVehicleUsageMasterRepo
			                    .findTopByCompanyIdAndVehicleUsageIdOrderByAmendIdDesc(
			                        req.getCompanyId(),
			                        Integer.valueOf(req.getMotorUsageId())
			                    );
			            if (usage != null
			                    && Integer.valueOf(5).equals(usage.getVehicleUsageId())) {
			                skipVal = true;
			            }
			        }
			    } else {
			        skipVal = true;
			    }
			}

			
			Integer existsChassis =0;
			if (StringUtils.isBlank(req.getSaveOrSubmit()) || (!"Save".equalsIgnoreCase(req.getSaveOrSubmit()))) {
				if (StringUtils.isBlank(req.getInsuranceType())) {
					error.add("1130");
				}
				if (req.getInsuranceType() == null && req.getSectionId().size() < 0) {
					error.add("1130");
				}
				if (StringUtils.isBlank(req.getInsuranceClass())) {
					error.add("1131");
				}
				
				if (StringUtils.isBlank(req.getVehicleTypeId())) {
					error.add("1141");
				}
				if(!("SQ".equals(req.getSavedFrom()))) {
					if (StringUtils.isBlank(req.getMotorUsageId())) {
						error.add("1143");
					}
				}
				if (StringUtils.isBlank(req.getGpsTrackingInstalled())) {
					error.add("1135");
				}
				
//				if (StringUtils.isBlank(req.getNcdYn())) {
//					error.add("1142");
//				}
				if (StringUtils.isNotBlank(req.getInsuranceClass()) && (req.getInsuranceClass().equalsIgnoreCase("1") || req.getInsuranceClass().equalsIgnoreCase("2"))) {
					if (StringUtils.isBlank(req.getSumInsured()))
						error.add("1132");
					else if (!req.getSumInsured().matches("[0-9.]+"))
						error.add("1133");
					else if (Double.parseDouble(req.getSumInsured()) <= 0 && (req.getEndorsementType() == null || req.getEndorsementType() == 0)) {
						error.add("1134");
					}
	
				}
			}
			
			
			if (StringUtils.isBlank(req.getVehicleId())) {
				error.add("1144");
			}
			if (StringUtils.isBlank(req.getBranchCode())) {
				error.add("1092");
			} else if (req.getBranchCode().length() > 20) {
				error.add("1093");
			}
			if (StringUtils.isBlank(req.getProductId())) {
				error.add("1094");
			} else if (req.getProductId().length() > 20) {
				error.add("1095");
			}

			if (StringUtils.isBlank(req.getCompanyId())) {
				error.add("1096");
			} else if (req.getCompanyId().length() > 20) {
				error.add("1097");
			}

			// Chassis No Restrict
			
			if (StringUtils.isNotBlank(req.getRequestReferenceNo())
				    && StringUtils.isNotBlank(req.getChassisNumber())
				    && req.getVehicleId() != null && req.getEndorsementType()==null) {

				    Integer count = repo.existsDuplicateChassis(
				        req.getRequestReferenceNo(),
				        req.getChassisNumber(),
				        Integer.valueOf(req.getVehicleId())
				    );

				    if (count > 0) {
				        error.add("1098");
				    }
				}

//			if (StringUtils.isNotBlank(req.getRequestReferenceNo()) && StringUtils.isNotBlank(req.getChassisNumber())) {
//				existsChassis = repo.existsByRequestAndChassis(req.getRequestReferenceNo(), req.getChassisNumber());
//				if (existsChassis > 0) {
//					error.add("1098");
//				}
//				
//				//EserviceMotorDetails findData = repo.findByRequestReferenceNoAndChassisNumberOrderByRiskIdAsc(req.getRequestReferenceNo(), req.getChassisNumber());
//				//if (findData != null && !req.getVehicleId().equalsIgnoreCase(findData.getRiskId().toString())) {
//					//error.add("1098");
//				//}
//			}

			// validation for chassis number to short term policy in alliance

			try {
				 if(req !=null && StringUtils.isNotBlank(req.getCompanyId()) && req.getCompanyId().equals("100002")&&
                 		StringUtils.isNotBlank(req.getProductId())&& req.getProductId().equals("46")   )
                 {
                   if(StringUtils.isBlank(req.getVehicleMakeId()))
                   {
                 	  error.add("2256"); 
                   }
                   if(StringUtils.isBlank(req.getVehcileModelId()))
                   {
                 	  error.add("2257");  
                   }
                   if (!skipVal) {
                   if(req.getEngineCapacity()==null || req.getEngineCapacity().equals(0)  )
                   {
                 	  error.add("2258");  

                   }
                   }
                   if(StringUtils.isBlank(req.getFuelType()))
                   {
                	   error.add("2260");
                   }
                   if(StringUtils.isBlank(req.getColor()))
                   {
                	   error.add("2261");
                   }
                   if(StringUtils.isBlank(req.getMotorCategory()))
                   {
                	   error.add("2262");
                   }
                   if (!skipVal) {
                   if(req.getSeatingCapcity()==null|| String.valueOf(req.getSeatingCapcity() ==null?"":req.getSeatingCapcity()).isBlank())
                   {
                 	  error.add("1129");  
                   }
                   }
                   if(StringUtils.isBlank(req.getManufactureYear()==null|| req.getManufactureYear()==0? null: String.valueOf(req.getManufactureYear())))
                   {
                 	  error.add("2259");  

                   }
                 }
				if (null != req && StringUtils.isNotBlank(req.getCompanyId()) && "100002".equals(req.getCompanyId())
						&& StringUtils.isNotBlank(req.getProductId()) && "46".equals(req.getProductId())
						&& StringUtils.isNotBlank(req.getChassisNumber())
						&& StringUtils.isBlank(req.getEndtPrevQuoteNo())
						&& (req.getEndorsementType() == null || req.getEndorsementType() == 0)) {

//					List<EserviceMotorDetails> duplicateList = repo.findByCompanyIdAndProductIdAndChassisNumber(
//							req.getCompanyId(), req.getProductId(), req.getChassisNumber());

//					if (null != duplicateList && !duplicateList.isEmpty()) {
//
//						for (EserviceMotorDetails data : duplicateList) {
//
//							if (null != data.getRequestReferenceNo() && !data.getRequestReferenceNo().isEmpty()) {
//
//								List<HomePositionMaster> policyList = homeRepo
//										.findByRequestReferenceNoAndStatus(data.getRequestReferenceNo(), "P");
//
//								if (null != policyList && !policyList.isEmpty()) {
//
//									error.add("2204");
//
//									break;
//								}
//							}
//
//						}
//					}
					if( existsChassis !=1) {
						List<HomePositionMaster> policyList = homeRepo
								.findByRequestReferenceNoAndStatus(req.getRequestReferenceNo(), "P");
						if (null != policyList && !policyList.isEmpty()) {
							error.add("2204");
						}
					}
					
					

				}
				
			} catch (Exception e) {

				log.error("Exception Occurs When Validating The Chassis Number  " + e.getMessage());
				e.printStackTrace();
				// throw
			}

			
			
			try {
				if (null != req && StringUtils.isNotBlank(req.getCompanyId()) && "100002".equals(req.getCompanyId())
						&& StringUtils.isNotBlank(req.getProductId()) && "5".equals(req.getProductId())
						&& StringUtils.isNotBlank(req.getRegistrationnumber())
						&& StringUtils.isBlank(req.getEndtPrevQuoteNo())
						&& (req.getEndorsementType() == null || req.getEndorsementType() == 0)) {

//					List<EserviceMotorDetails> duplicateList = repo.findByCompanyIdAndProductIdAndRegistrationNumberAndStatus(
//							req.getCompanyId(), req.getProductId(), req.getRegistrationnumber(),"P");

//					if (null != duplicateList && !duplicateList.isEmpty()) {
//
//						for (EserviceMotorDetails data : duplicateList) {
//
//							if (null != data.getRequestReferenceNo() && !data.getRequestReferenceNo().isEmpty()) {
//
//								List<HomePositionMaster> policyList = homeRepo
//										.findByRequestReferenceNoAndStatus(data.getRequestReferenceNo(), "P");
//
//								if (null != policyList && !policyList.isEmpty()) {
//									Date expiryDate = policyList.get(0).getExpiryDate();
//
//									if (expiryDate != null && expiryDate.after(new Date())) {
//										//error.add("2285");commented fro renewal
//									}
//
//									break;
//								}
//							}
//
//						}
//					}

				}
				
			}catch(Exception e) {
				log.error("Exception Occurs when validating the chassis number "+e.getMessage());
				e.printStackTrace();
			}
			
			if ("API".equals(req.getSavedFrom()) && req.getEndorsementType() == null) {

			    List<Date> coverNoteEndDates = motorVehRepo
			            .findCoverNoteEndDateByRegNo(req.getRegistrationnumber());

			    Date coverNoteEndDate = (coverNoteEndDates != null && !coverNoteEndDates.isEmpty())
			            ? coverNoteEndDates.get(0)
			            : null;

			    if (coverNoteEndDate != null) {
			        LocalDate coverNoteEnd = coverNoteEndDate
			                .toInstant()
			                .atZone(ZoneId.systemDefault())
			                .toLocalDate();

			        LocalDate policyStart = req.getPolicyStartDate()
			                .toInstant()
			                .atZone(ZoneId.systemDefault())
			                .toLocalDate();

			        if (!policyStart.isAfter(coverNoteEnd)) {
			            error.add("2334");
			        }
			    }
			}

			
			if (!"SQ".equals(req.getSavedFrom())) {
				System.out.println("Save Motor validation started block  at line 355 : "
						+( System.currentTimeMillis()-start));
				if (StringUtils.isBlank(req.getChassisNumber())) {
					error.add("1099");
				} else if (StringUtils.isNotBlank(req.getChassisNumber())&& (!req.getChassisNumber().matches("[a-zA-Z0-9]+"))&& (!req.getCompanyId().equalsIgnoreCase("100004"))) {
					error.add("1100");
				} else if (req.getChassisNumber().length() < 3) {
					error.add("1101");
				} else if (req.getChassisNumber().length() > 20) {
					error.add("1102");
				} else if ((req.getSearchFromApi() == null || req.getSearchFromApi() == true) && req.getEndorsementType() == null) {
					
					MotorVehicleInfo data = motorVehRepo.findTop1ByResChassisNumberAndCompanyIdOrderByEntryDateDesc(req.getChassisNumber(), req.getCompanyId());
					if (data == null) {
						error.add("1103");
					}

				}
				 
			}

			if ("SQ".equals(req.getSavedFrom())) {
				System.out.println("Save Motor validation  block  at line 377 : "
						+( System.currentTimeMillis()-start));
				if (StringUtils.isBlank(req.getMobileNo())) {
					error.add("2248");
				} else if (req.getMobileNo().length() > 10 || req.getMobileNo().length() < 8) {
					error.add("2249");
				} else if (!req.getMobileNo().matches("[0-9]+")) {
					error.add("2250");
				} else if (req.getMobileNo().matches("[0-9]+") && Double.valueOf(req.getMobileNo()) <= 0) {
					error.add("2251");
				}
				if (StringUtils.isBlank(req.getMobileCode())) {
					error.add("2252");
				}
				if (req.getCustomerName().length() > 250) {
					error.add("2253");
				} else if (!req.getCustomerName().matches("[a-zA-Z.&() ]+")) {
					error.add("2254");
				}
			}
			if (!skipVal) {
			if (StringUtils.isNotBlank(req.getEngineNumber())) {
				
				String regex = "^[A-Za-z0-9\\-/ ]+$";
				
				if (StringUtils.isNotBlank(req.getEngineNumber()) && !req.getEngineNumber().trim().matches(regex) && (!req.getCompanyId().equalsIgnoreCase("100004"))) {
					error.add("1104");
				} else if (req.getEngineNumber().length() < 2) {
					error.add("1105");
				} else if (req.getEngineNumber().length() > 20) {
					error.add("1106");
				} 
				else if (StringUtils.isNotBlank(req.getChassisNumber())	&& StringUtils.isNotBlank(req.getEngineNumber())) {
					if (req.getChassisNumber().trim().equalsIgnoreCase(req.getEngineNumber().trim())) {
							error.add("1107");
					}
				}
			}
			}
			 
			
			if (!skipVal) {
			if (req.getEngineCapacity() != null) {
				if (!req.getEngineCapacity().toString().matches("[0-9.]+")) {
					error.add("1108");
				} else if (req.getEngineCapacity() != null && req.getEngineCapacity().compareTo(new BigDecimal("0")) <= 0) {
					error.add("1109");
				} else if (req.getEngineCapacity().compareTo(new BigDecimal("99999")) > 0) {
					error.add("1110");
				}
			}
			}

			if (StringUtils.isBlank(req.getHavepromocode())) {
				error.add("1111");
			} else if (!(req.getHavepromocode().equalsIgnoreCase("Y") || req.getHavepromocode().equalsIgnoreCase("N"))) {
				error.add("1112");
			} else if (req.getHavepromocode().equalsIgnoreCase("Y")) {
				if (StringUtils.isBlank(req.getPromocode())) {
					error.add("1113");
				} else if (req.getPromocode().length() > 20) {
					error.add("1114");
				}
			}

			if (StringUtils.isBlank(req.getCurrency())) {
				error.add("1115");
			}
			if (StringUtils.isBlank(req.getExchangeRate())) {
				error.add("1116");
			} else if (Double.valueOf(req.getExchangeRate()) <= 0D) {
				error.add("1117");
			} else {
				System.out.println("Save Motor validation started block  at line 446 : "
						+( System.currentTimeMillis()-start));
				Tuple minMax = eserMotDetServImpl.getMinMaxRate(req.getCurrency(), req.getCompanyId());
				if (minMax != null) {

					Double exRate = Double.valueOf(minMax.get("exchangeRate") == null ? "0" : minMax.get("exchangeRate").toString());
					Double minRate = Double.valueOf(minMax.get("minDiscount") == null ? "0" : minMax.get("minDiscount").toString());
					Double maxRate = Double.valueOf(minMax.get("maxLoading") == null ? "0" : minMax.get("maxLoading").toString());
					minRate = exRate - (exRate * minRate / 100);
					maxRate = exRate + (exRate * maxRate / 100);
					if (Double.valueOf(req.getExchangeRate()) <= minRate || Double.valueOf(req.getExchangeRate()) >= maxRate) {
						error.add("1001");
					}
				}
			}

			// Source Type Search Condition
			List<String> directSource = new ArrayList<String>();
			directSource.add("1");
			directSource.add("2");
			directSource.add("3");

			if (req.getUserType().equalsIgnoreCase("Issuer") && StringUtils.isBlank(req.getSourceTypeId())) {
				error.add("1118");

			} else if (StringUtils.isNotBlank(req.getSourceTypeId()) && directSource.contains(req.getSourceTypeId())) {
				if (StringUtils.isBlank(req.getBdmCode())) {
					error.add("1119");
				}
				if (StringUtils.isBlank(req.getCustomerName())) {
					error.add("1120");
				}

			} else {
				if (StringUtils.isBlank(req.getLoginId())) {
					error.add("1121");
				} else {
					LoginMaster loginData = loginRepo.findByLoginId(req.getCreatedBy());
					if (loginData.getSubUserType().equalsIgnoreCase("bank")) {
						if (StringUtils.isBlank(req.getAcExecutiveId())) {
							error.add("1122");
						}
					}
				}
				if (StringUtils.isBlank(req.getBrokerBranchCode())) {
					error.add("1123");
				}
			}
			String status = StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus();

			if (req.getPolicyStartDate() == null) {
				error.add("1124");
			} else if ((req.getEndorsementType() == null || req.getEndorsementType().equals(0))	&& !"RQ".equalsIgnoreCase(status)) {
				long MILLS_IN_A_DAY = 1000 * 60 * 60 * 24;
				long days90 = MILLS_IN_A_DAY * 90;
				Date today = new Date();

				int before = eserMotDetServImpl.getBackDays(req.getCompanyId(), req.getProductId(), req.getLoginId());
				int days = before == 0 ? -1 : -before;
				long backDays = MILLS_IN_A_DAY * days;
				Date beforedays = new Date(today.getTime() + backDays);
				Date resticDate = new Date(today.getTime() + days90);
				if (req.getPolicyStartDate().before(beforedays) && req.getEndtPrevQuoteNo()==null) {
					error.add("1124");
				} else if (req.getPolicyStartDate().after(resticDate)) {
					error.add("1125");
				}
			}
			if (req.getPolicyEndDate() == null) {
				error.add("1126");
			} else if (req.getPolicyStartDate() != null && req.getPolicyEndDate() != null	&& req.getEndorsementType() == null) {
				if (req.getPolicyStartDate().after(req.getPolicyEndDate())) {
					error.add("1127");
				}

			}

			if (StringUtils.isBlank(req.getUserType())) {
				error.add("1128");
			}

			if ("46".equalsIgnoreCase(req.getProductId()) && req.getSeatingCapcity() != null && req.getSeatingCapcity().toString().matches("[0-9]+")) {
				List<MotorBodyTypeMaster> bodyTypes = new ArrayList<MotorBodyTypeMaster>();
				if (StringUtils.isNotBlank(req.getCompanyId()) && StringUtils.isNotBlank(req.getBranchCode()) && StringUtils.isNotBlank(req.getVehicleTypeId())) {
					bodyTypes = eserMotDetServImpl.getInduvidualBodyTypeMasterDropdown(req.getCompanyId(), req.getBranchCode(), req.getVehicleTypeId());
				} else if (StringUtils.isBlank(req.getVehicleTypeId()) && StringUtils.isNotBlank(req.getVehicleType())) {
					bodyTypes = eserMotDetServImpl.getInduvidualBodyTypeMasterDropdown1(req.getCompanyId(), req.getBranchCode(), req.getVehicleType());
				}

				if (Integer.valueOf(req.getSeatingCapcity().toString()) <= 0) {
					error.add("1129"); 
				} else if (bodyTypes.size() > 0 && bodyTypes.get(0).getSeatingCapacity() != null && bodyTypes.get(0).getSeatingCapacity() < Integer.valueOf(req.getSeatingCapcity().toString())) {
					error.add("2226");
				}

			}

			if (null != req) {

				if (null != req.getMileage()) {
					if (!req.getMileage().toString().matches("[0-9]+")) {
						error.add("2230");
					}
				}
				if (null != req.getNoOfTrailers()) {
					if (!req.getNoOfTrailers().toString().matches("[0-9]+")) {
						error.add("2231");
					}
				}
				if (null != req.getNoOfPassengers()) {
					if (!req.getNoOfPassengers().toString().matches("[0-9]+")) {
						error.add("2232");
					}
				}
				if (null != req.getNoClaimYears()) {
					if (!req.getNoClaimYears().toString().matches("[0-9]+")) {
						error.add("2233");
					}
				}

			}

			if (StringUtils.isBlank(req.getSaveOrSubmit()) || (!"Save".equalsIgnoreCase(req.getSaveOrSubmit()))) {

				

				if (req.getPreviousLossRatio() != null && !req.getPreviousLossRatio().toString().matches("[0-9.]+")) {
					error.add("2255");
				}

				try {

					/*if (null != req && (null == req.getSectionId() || req.getSectionId().isEmpty()|| req.getSectionId().get(0).isEmpty())) {
						error.add("2190");
					}*/

					/**if (null != req && null != req.getPurchaseDate() && StringUtils.isNotBlank(req.getRequestReferenceNo())	&& StringUtils.isNotBlank(req.getVehicleId()) && req.getVehicleId().matches("[0-9]+")) {

						EserviceMotorDetails vehicleData = repo.findByRequestReferenceNoAndRiskId(req.getRequestReferenceNo(), Integer.valueOf(req.getVehicleId()));

						if (null != vehicleData && vehicleData.getManufactureYear() != null) {
							boolean isCompareDate = false;
							eserMotDetServImpl.validatePurchaseDateWithManufactureYear(req.getPurchaseDate(),String.valueOf(vehicleData.getManufactureYear()), isCompareDate, error);
						}

					}**/

				} catch (Exception e) {

					log.error("Exception Ocurs when Validate Purchase Date With Manufacture Year ### " + e.getMessage());
					e.printStackTrace();

				}

				

				if (StringUtils.isNotBlank(req.getAcccessoriesSumInsured())	&& (!req.getAcccessoriesSumInsured().toString().matches("[0-9.]+"))) {
					error.add("1136");
				}
				if (StringUtils.isNotBlank(req.getWindScreenSumInsured()) && (!req.getWindScreenSumInsured().toString().matches("[0-9.]+"))) {
					error.add("1137");
				}

				if (StringUtils.isNotBlank(req.getCollateralYn()) && req.getCollateralYn().equalsIgnoreCase("Y")) {
					if (StringUtils.isBlank(req.getBorrowerType())) {
						error.add("1138");
					}
					if (StringUtils.isBlank(req.getCollateralName())) {
						error.add("1139");
					}
					if (StringUtils.isBlank(req.getFirstLossPayee())) {
						error.add("1140");
					}

				}
				
				
				
				// 100027
				if (StringUtils.isNotBlank(req.getCompanyId()) && "100027".equalsIgnoreCase(req.getCompanyId())) {
					if (StringUtils.isBlank(req.getVehicleValueType()) || req.getVehicleValueType() == null || "".equalsIgnoreCase(req.getVehicleValueType())) {
						error.add("2148");
					}
					if (StringUtils.isBlank(req.getInflation()) || req.getInflation() == null || "".equalsIgnoreCase(req.getInflation())) {
						error.add("2149");
					}
					if (StringUtils.isBlank(req.getDefenceValue()) || req.getDefenceValue() == null || "".equalsIgnoreCase(req.getDefenceValue())) {
						error.add("2150");
					}
					if (StringUtils.isBlank(req.getExcess()) || req.getExcess() == null || "".equalsIgnoreCase(req.getExcess())) {
						error.add("2151");
					}

					if (req.getPurchaseDate() == null) {
						error.add("2153");
					}
					List<String> sectionId = req.getSectionId();
					if (!(sectionId == null || sectionId.isEmpty())) {
						if (!(sectionId.get(0).equalsIgnoreCase("95") || sectionId.get(0).equalsIgnoreCase("102"))) {
							if (StringUtils.isBlank(req.getSumInsured()))
								error.add("1132");
							else if (!req.getSumInsured().matches("[0-9.]+"))
								error.add("1133");
							else if (Double.parseDouble(req.getSumInsured()) <= 0 && (req.getEndorsementType() == null || req.getEndorsementType() == 0)) {
								error.add("1134");
							}

						}
					}
				}
               
					if (req.getDriverDetails() != null) {
						MotorDriverSaveReq driverReq = req.getDriverDetails();
						try {
							if (null != driverReq.getDriverDob() && StringUtils.isNotBlank(driverReq.getDriverExperience())	&& driverReq.getDriverExperience().matches("[0-9]+")) {

								if (Integer.valueOf(driverReq.getDriverExperience()) <= 0) {
									error.add("2229");
								} else {
									eserMotDetServImpl.validateDriverExperienceWithDateOfBirth(driverReq.getDriverDob(),driverReq.getDriverExperience(), error);
								}

							}
						} catch (Exception e) {
							log.error("Exception Occurs When validating The Driving Experience Of The Driver ### "
									+ e.getMessage());
							e.printStackTrace();
							// throw new DateTimeException("Date Parse Exception");

						}

						if (StringUtils.isBlank(driverReq.getDriverName()) || driverReq.getDriverName() == null	|| driverReq.getDriverName() == "") {
							error.add("2192");
						} else if (StringUtils.isNumeric(driverReq.getDriverName())) {
							error.add("2193");
						} else if (!driverReq.getDriverName().matches("[a-zA-Z ]*$")) {
							error.add("2193");
						}
						if (StringUtils.isBlank(driverReq.getDriverType())) {
							error.add("2256");
						}
						if (StringUtils.isBlank(driverReq.getDriverExperience())|| driverReq.getDriverExperience() == null || driverReq.getDriverExperience() == "") {
							error.add("2194");
						} else if (!StringUtils.isNumeric(driverReq.getDriverExperience())) {
							error.add("2195");
						}
						if(!("100020".equals(req.getCompanyId()) && "SQ".equals(req.getSavedFrom()))) {
						if (StringUtils.isBlank(driverReq.getMaritalStatus()) || driverReq.getMaritalStatus() == null || driverReq.getMaritalStatus() == "") {
							error.add("2196");
						}
						}
						if (StringUtils.isBlank(driverReq.getGender()) || driverReq.getGender() == null	|| driverReq.getGender() == "") {
							error.add("2197");
						}
						if(!("100020".equals(req.getCompanyId()) && "SQ".equals(req.getSavedFrom()))) {
							if (StringUtils.isBlank(driverReq.getLicenseNo()) || driverReq.getLicenseNo() == null || driverReq.getLicenseNo() == "") {
								error.add("2198");
							} else if (StringUtils.isAlpha(driverReq.getLicenseNo())) {
								error.add("2199");
							}
							else if (driverReq.getLicenseNo()!=null && driverReq.getLicenseNo().length() > 14) {
								error.add("2199");
							} else if (!driverReq.getLicenseNo().matches("^[a-zA-Z0-9]*$")) {
								error.add("2199");
							}
						}
						if (driverReq.getDriverDob() == null) {
							error.add("2200");
						}
						if (!"100028".equalsIgnoreCase(req.getCompanyId())) {
							if (StringUtils.isBlank(driverReq.getStateId()) || driverReq.getStateId() == null || driverReq.getStateId() == "") {
								error.add("2201");
							}
							if (StringUtils.isBlank(driverReq.getCityId()) || driverReq.getCityId() == null || driverReq.getCityId() == "") {
								error.add("2202");
							}
							if (StringUtils.isBlank(driverReq.getSuburbId()) || driverReq.getSuburbId() == null || driverReq.getSuburbId() == "") {
								error.add("2203");
							}
						}

					}
				// Eagle Insurance
				if (req.getCompanyId().equals("100028")) {

					for (String sec : req.getSectionId()) {
						if (sec.equals("104")) {
							if (null == req.getSumInsured() || req.getSumInsured().isEmpty()) {
								error.add("1132");
							} else if (req.getSumInsured().equals("0")) {
								error.add("1133");
							}
						}
					}
					if (StringUtils.isBlank(req.getClaimType())) {
						error.add("2158");
					}
					System.out.println("Save Motor validation  block  at line 742 : "
							+( System.currentTimeMillis()-start));
					if (req.getSumInsured() != null || StringUtils.isNotBlank(req.getSumInsured())) {
						if ((req.getVehicleMakeId() != null || StringUtils.isNotBlank(req.getVehicleMakeId())) && (req.getVehcileModelId() != null || StringUtils.isNotBlank(req.getVehcileModelId()))) {
							List<MotorMakeModelMaster> makeModel = eserMotDetServImpl.getMotorMakeModel(req.getVehicleMakeId(),req.getVehcileModelId(), req.getCompanyId(), req.getBranchCode());
							if (makeModel != null && makeModel.size() > 0) {
								Double vehicleValue = makeModel.get(0).getVehicleValue();
								if (vehicleValue != null) {
									if (Double.valueOf(req.getSumInsured()) > vehicleValue) {
										error.add("2214" + "," + vehicleValue);
									}
								}
							}
						}
					}
				}

				
			}

			List<EserviceMotorDetails> list = new ArrayList<EserviceMotorDetails>();
			if (
			// StringUtils.isNotBlank(req.getAccessoriesInformation())) &&
			// (StringUtils.isNotBlank(req.getAccessoriesInformation()))
			// && (StringUtils.isNotBlank(req.getAdditionalCircumstances())) &&
			// (StringUtils.isNotBlank(req.getAgencyCode()))
			(StringUtils.isNotBlank(req.getBorrowerType())) && (StringUtils.isNotBlank(req.getBranchCode()))
			// &&(StringUtils.isNotBlank(req.getChassisNumber())) &&
			// (StringUtils.isNotBlank(req.getCityLimit()))
					&& (StringUtils.isNotBlank(req.getClaimRatio()))
					&& (StringUtils.isNotBlank(req.getCollateralName()))
					// && (StringUtils.isNotBlank(req.getCollateralYn())) &&
					// (StringUtils.isNotBlank(req.getColor()))
					// && (StringUtils.isNotBlank(req.getCompanyId())) &&
					// (StringUtils.isNotBlank(req.getCovernoteNo()))
					&& (StringUtils.isNotBlank(req.getCreatedBy())) && (StringUtils.isNotBlank(req.getCurrency()))
					// &&(StringUtils.isNotBlank(req.getCustomerReferenceNo()))
					// && (StringUtils.isNotBlank(req.getDrivenByDesc()))
					// && (StringUtils.isNotBlank(req.getEngineNumber())) &&
					// (StringUtils.isNotBlank(req.getExchangeRate()))
					&& (StringUtils.isNotBlank(req.getFirstLossPayee()))
					&& (StringUtils.isNotBlank(req.getFleetOwnerYn()))
					// && (StringUtils.isNotBlank(req.getFuelType())) &&
					// (StringUtils.isNotBlank(req.getGpsTrackingInstalled()))
					// && (StringUtils.isNotBlank(req.getHoldInsurancePolicy())) &&
					// (StringUtils.isNotBlank(req.getIdNumber()))
					&& (StringUtils.isNotBlank(req.getInsuranceClass()))
//					&& (StringUtils.isNotBlank(req.getInsuranceType()))
					// && (StringUtils.isNotBlank(req.getManufactureYear())) &&
					// (StringUtils.isNotBlank(req.getModelNumber()))
					// && (StringUtils.isNotBlank(req.getMotorCategory())) &&
					// (StringUtils.isNotBlank(req.getMotorUsage()))
					&& (StringUtils.isNotBlank(req.getNcdYn())) && (StringUtils.isNotBlank(req.getNoOfComprehensives()))
					// && (StringUtils.isNotBlank(req.getNoOfVehicles())) &&
					// (StringUtils.isNotBlank(req.getOwnerCategory()))
					// &&(StringUtils.isNotBlank(req.getPeriodOfInsurance())) &&
					// (StringUtils.isNotBlank(req.getPolicyType()))
					// && (StringUtils.isNotBlank(req.getProductId())) &&
					// (StringUtils.isNotBlank(req.getRegistrationNumber()))
					// && (StringUtils.isNotBlank(req.getRequestReferenceNo())) &&
					// (StringUtils.isNotBlank(req.getSectionId()))
					// && (StringUtils.isNotBlank(req.getStatus())) &&
					// (StringUtils.isNotBlank(req.getStickerNo()))
					// && (StringUtils.isNotBlank(req.getTrailerDetails())) &&
					// (StringUtils.isNotBlank(req.getVehcileModel()))
					&& (StringUtils.isNotBlank(req.getVehicleId())) && (StringUtils.isNotBlank(req.getVehicleMake()))
					// && (StringUtils.isNotBlank(req.getVehicleType())) &&
					// (StringUtils.isNotBlank(req.getWindScreenCoverRequired()))
					// && (StringUtils.isNotBlank(req.getAcccessoriesSumInsured().toString())) &&
					// (StringUtils.isNotBlank(req.getActualPremium().toString()))
					// && (StringUtils.isNotBlank(req.getAxelDistance().toString())) &&
					// (StringUtils.isNotBlank(req.getCubicCapacity().toString()))
					// && (StringUtils.isNotBlank(req.getGrossWeight().toString())) &&
					// (StringUtils.isNotBlank(req.getInsurerSettlement().toString()))
					// &&(StringUtils.isNotBlank(req.getNcdYears().toString())) &&
					// (StringUtils.isNotBlank(req.getNoOfClaims().toString()))
					// && (StringUtils.isNotBlank(req.getNumberOfAxels().toString())) &&
					// (StringUtils.isNotBlank(req.getOverridePercentage().toString()))
					// && (StringUtils.isNotBlank(req.getPolicyEndDate().toString())) &&
					// (StringUtils.isNotBlank(req.getPolicyStartDate().toString()))
					// && (StringUtils.isNotBlank(req.getRadioorcasseteplayer().toString())) &&
					// (StringUtils.isNotBlank(req.getRegistrationYear().toString()))
					// && (StringUtils.isNotBlank(req.getRoofRack().toString())) &&
					// (StringUtils.isNotBlank(req.getSeatingCapacity().toString()))
					// &&(StringUtils.isNotBlank(req.getSpotFogLamp().toString())) &&
					// (StringUtils.isNotBlank(req.getSumInsured().toString()))
					// && (StringUtils.isNotBlank(req.getTareWeight().toString())) &&
					// (StringUtils.isNotBlank(req.getTppdFreeLimit().toString()))
					&& (req.getTppdIncreaeLimit() != null 
					        && StringUtils.isNotBlank(req.getTppdIncreaeLimit().toString() ))
					&& (req.getWindScreenSumInsured() != null) && (StringUtils.isNotBlank(req.getHavepromocode()))
					&& (StringUtils.isNotBlank(req.getPromocode()))) {
				System.out.println("Save Motor validation query  block  started  at line 833 : "
						+( System.currentTimeMillis()-start));
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<EserviceMotorDetails> query = cb.createQuery(EserviceMotorDetails.class);
				// Find all
				Root<EserviceMotorDetails> b = query.from(EserviceMotorDetails.class);
				// Select
				query.select(b);
				// Where

				Predicate n1 = cb.equal(b.get("acccessoriesSumInsured"), req.getAcccessoriesSumInsured());
				// Predicate n2 = (cb.like(cb.lower(b.get("accessoriesInformation")),
				// req.getAccessoriesInformation().toLowerCase()));
				Predicate n3 = (cb.like(cb.lower(b.get("accident")), req.getAccident().toLowerCase()));
				// Predicate n4 = (cb.equal(b.get("actualPremium"), req.getActualPremium()));
				// Predicate n5 = (cb.like(cb.lower(b.get("additionalCircumstances")),
				// req.getAdditionalCircumstances().toLowerCase()));
				Predicate n6 = (cb.like(cb.lower(b.get("agencyCode")), req.getAgencyCode().toLowerCase()));
				// Predicate n7 = (cb.equal(b.get("axelDistance"), req.getAxelDistance()));
				Predicate n8 = (cb.like(cb.lower(b.get("borrowerType")), req.getBorrowerType().toLowerCase()));
				Predicate n9 = (cb.like(cb.lower(b.get("branchCode")), req.getBranchCode().toLowerCase()));
				Predicate n10 = (cb.like(cb.lower(b.get("chassisNumber")), req.getChassisNumber().toLowerCase()));
				// Predicate n11 =
				// (cb.like(cb.lower(b.get("cityLimit")),req.getCityLimit().toLowerCase()));
				Predicate n12 = (cb.like(cb.lower(b.get("claimRatio")), req.getClaimRatio().toString().toLowerCase()));
				Predicate n13 = (cb.like(cb.lower(b.get("collateralName")), req.getCollateralName().toLowerCase()));
				Predicate n14 = (cb.like(cb.lower(b.get("collateralYn")), req.getCollateralYn().toLowerCase()));
				// Predicate n15 = (cb.like(cb.lower(b.get("color")),
				// req.getColor().toLowerCase()));
				Predicate n16 = (cb.like(cb.lower(b.get("companyId")), req.getCompanyId().toLowerCase()));
				// Predicate n17 = (cb.like(cb.lower(b.get("covernoteNo")),
				// req.getCovernoteNo().toLowerCase()));
				Predicate n18 = (cb.like(cb.lower(b.get("createdBy")), req.getCreatedBy().toLowerCase()));
				// Predicate n19 = (cb.equal(b.get("cubicCapacity"), req.getCubicCapacity()));
				Predicate n20 = (cb.like(cb.lower(b.get("currency")), req.getCurrency().toLowerCase()));
				// Predicate n21 = (cb.like(cb.lower(b.get("customerReferenceNo")),
				// req.getCustomerReferenceNo().toLowerCase()));
				// Predicate n22 = (cb.like(cb.lower(b.get("drivenByDesc")),
				// req.getDrivenByDesc().toLowerCase()));
				// Predicate n23 = (cb.like(cb.lower(b.get("engineNumber")),
				// req.getEngineNumber().toLowerCase()));
				Predicate n24 = (cb.equal(b.get("exchangeRate"), req.getExchangeRate()));
				Predicate n25 = (cb.like(cb.lower(b.get("firstLossPayee")), req.getFirstLossPayee().toLowerCase()));
				Predicate n26 = (cb.like(cb.lower(b.get("fleetOwnerYn")), req.getFleetOwnerYn().toLowerCase()));
				// Predicate n27 = (cb.like(cb.lower(b.get("fuelType")),
				// req.getFuelType().toLowerCase()));
				Predicate n28 = (cb.like(cb.lower(b.get("gpsTrackingInstalled")),
						req.getGpsTrackingInstalled().toLowerCase()));
				// Predicate n29 = (cb.equal(b.get("grossWeight"),req.getGrossWeight()));
				// Predicate n30 = (cb.like(cb.lower(b.get("holdInsurancePolicy")),
				// req.getHoldInsurancePolicy().toLowerCase()));
				Predicate n31 = (cb.like(cb.lower(b.get("idNumber")), req.getIdNumber().toLowerCase()));
				Predicate n32 = (cb.like(cb.lower(b.get("insuranceClass")), req.getInsuranceClass().toLowerCase()));
//				Predicate n33 = (cb.like(cb.lower(b.get("insuranceType")), req.getInsuranceType().toLowerCase()));
//				Predicate n34 = (cb.equal(b.get("insurerSettlement"), req.getInsurerSettlement()));
//				Predicate n35 = (cb.like(cb.lower(b.get("manufactureYear")), req.getManufactureYear().toLowerCase()));
//				Predicate n36 = (cb.like(cb.lower(b.get("modelNumber")), req.getModelNumber().toLowerCase()));
//				Predicate n37 = (cb.like(cb.lower(b.get("motorCategory")), req.getMotorCategory().toLowerCase()));
				Predicate n38 = (cb.like(cb.lower(b.get("motorUsage")), req.getMotorUsage().toLowerCase()));
//				Predicate n39 = (cb.equal(b.get("ncdYears"), req.getNcdYears()));
				Predicate n40 = (cb.like(cb.lower(b.get("ncdYn")), req.getNcdYn().toLowerCase()));
				// Predicate n41 = (cb.equal(b.get("noOfClaims"), req.getNoOfClaims()));
				Predicate n42 = (cb.like(cb.lower(b.get("noOfCompehensives")),
						req.getNoOfComprehensives().toLowerCase()));
				Predicate n43 = (cb.like(cb.lower(b.get("noOfVehicles")), req.getNoOfVehicles().toLowerCase()));
//				Predicate n44 = (cb.equal(b.get("numberOfAxels"), req.getNumberOfAxels()));
//				Predicate n45 = (cb.equal(b.get("overridePercentage"), req.getOverridePercentage()));
//				Predicate n46 = (cb.like(cb.lower(b.get("ownerCategory")), req.getOwnerCategory().toLowerCase()));
				Predicate n47 = (cb.like(cb.lower(b.get("periodOfInsurance")),
						req.getPeriodOfInsurance().toLowerCase()));
				Predicate n48 = (cb.equal(b.get("policyEndDate"), req.getPolicyEndDate()));
				Predicate n49 = (cb.equal(b.get("policyStartDate"), req.getPolicyStartDate()));
				// Predicate n50 = (cb.like(cb.lower(b.get("policyType")),
				// req.getPolicyType().toLowerCase()));
				Predicate n51 = (cb.like(cb.lower(b.get("productId")), req.getProductId().toLowerCase()));
//				Predicate n52 = (cb.equal(b.get("radioorcasseteplayer"), req.getRadioorcasseteplayer()));
//				Predicate n53 = (cb.equal(b.get("registrationNumber"), req.getRegistrationNumber()));
//				Predicate n54 = (cb.equal(b.get("registrationYear"), req.getRegistrationYear()));
				Predicate n55 = (cb.like(cb.lower(b.get("requestReferenceNo")),
						req.getRequestReferenceNo().toLowerCase()));
//				Predicate n56 = (cb.equal(b.get("roofRack"), req.getRoofRack()));
//				Predicate n57 = (cb.equal(b.get("seatingCapacity"), req.getSeatingCapacity()));
//				Predicate n58 = (cb.equal(b.get("sectionId"), req.getSectionId()));
//				Predicate n59 = (cb.equal(b.get("spotFogLamp"), req.getSpotFogLamp()));
				Predicate n60 = (cb.like(cb.lower(b.get("status")), req.getStatus().toLowerCase()));
				// Predicate n61 = (cb.like(cb.lower(b.get("stickerNo")),
				// req.getStickerNo().toLowerCase()));
				Predicate n62 = (cb.equal(b.get("sumInsured"), req.getSumInsured()));
//				Predicate n63 = (cb.equal(b.get("tareWeight"), req.getTareWeight()));
//				Predicate n64 = (cb.equal(b.get("tppdFreeLimit"), req.getTppdFreeLimit()));
				Predicate n65 = (cb.equal(b.get("tppdIncreaeLimit"), req.getTppdIncreaeLimit()));
				// Predicate n66 = (cb.like(cb.lower(b.get("trailerDetails")),
				// req.getTrailerDetails().toLowerCase()));
				Predicate n67 = (cb.like(cb.lower(b.get("vehcileModel")), req.getVehcileModel().toLowerCase()));
				Predicate n68 = (cb.like(cb.lower(b.get("vehicleId")), req.getVehicleId().toLowerCase()));
				Predicate n69 = (cb.like(cb.lower(b.get("vehicleMake")), req.getVehicleMake().toLowerCase()));
				Predicate n70 = (cb.like(cb.lower(b.get("vehicleType")), req.getVehicleType().toLowerCase()));
				// Predicate n71 = (cb.like(cb.lower(b.get("windScreenCoverRequired")),
				// req.getWindScreenCoverRequired().toLowerCase()));
				Predicate n72 = (cb.equal(b.get("windScreenSumInsured"), req.getWindScreenSumInsured()));
				Predicate n73 = (cb.equal(b.get("havepromocode"), req.getHavepromocode()));
				Predicate n74 = (cb.equal(b.get("promocode"), req.getPromocode()));

				query.where(n1,
						// n2,
						n3,
						// n4, n5,
						n6,
						// n7,
						n8, n9, n10,
						// n11,
						n12, n13, n14,
						// n15,
						n16,
						// n17,
						n18,
						// n19,
						n20,
						// n21,
						// n22, n23,
						n24, n25, n26,
						// n27,
						n28,
						// n29, n30,
						n31, n32, /* n33, */
						// n34, n35, n36, n37,
						n38,
						// n39,
						n40,
						// n41,
						n42, n43,
						// n44, n45, n46,
						n47, n48, n49,
						// n50,
						n51,
						// n52, n53, n54,
						n55,
						// n56, n57, n58, n59,
						n60,
						// n61,
						n62,
						// n63, n64,
						n65,
						// n66,
						n67, n68, n69, n70,
						// n71,
						n72, n73, n74

				);
				// Get Result
				TypedQuery<EserviceMotorDetails> result = em.createQuery(query);
				list = result.getResultList();
				if (list.size() > 0) {
					error.add("2227");

				}
				System.out.println("Save Motor validation query block block end  at line 989 : "
						+( System.currentTimeMillis()-start));
			}
			if(req.getEndorsementType()!=null && req.getEndorsementType()!=0 && req.getPolicyStartDate()!=null && req.getPolicyEndDate()!=null &&  req.getEndorsementType()!=846) {
				HomePositionMaster h = homeRepo.findByQuoteNo(req.getEndtPrevQuoteNo());
				Date inceptionDate = h.getInceptionDate();
				System.out.println("Inception Date is "+h.getInceptionDate()+"/n policystart date is: "+req.getPolicyStartDate());
			/*	if (!truncateTime(inceptionDate).equals(truncateTime(req.getPolicyStartDate()))) {
				    error.add("5017");
				}
				Date periodStart = truncateTime( req.getPolicyStartDate());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(periodStart);
				calendar.add(Calendar.YEAR,1);
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				Date periodEnd = truncateTime(calendar.getTime()) ;
				if(truncateTime(req.getPolicyEndDate()).compareTo(periodEnd)>0) {
					error.add("5018");
				}
				System.out.println("period end is "+ periodEnd+" request  end is "+truncateTime(req.getPolicyEndDate()));
				List<Date> endDates = getAllowedEndDates(periodEnd, req.getEndorsementDate());
				System.out.println(endDates.toString());
				if(!endDates.isEmpty()) {
					boolean allow = endDates.stream().anyMatch(e-> e.compareTo(truncateTime(req.getPolicyEndDate()))==0);
					if(!allow) {
						error.add("5019");
					}
				}*/
				if (!truncateTime(inceptionDate).equals(truncateTime(req.getPolicyStartDate())) && req.getEndorsementType() == null) {
				    error.add("5017");
				}
				Date periodStart =  req.getPolicyStartDate();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(periodStart);
				calendar.add(Calendar.YEAR,1);
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				Date periodEnd = calendar.getTime() ;
				if(truncateTime(req.getPolicyEndDate()).compareTo(periodEnd)>0) {
					error.add("5018");
				}else if(truncateTime(req.getPolicyEndDate()).compareTo(truncateTime(new Date()))<=0) {
					error.add("5020");
				}
			}
			System.out.println("Save Motor validation Full  block End at line 1034 : "
					+( System.currentTimeMillis()-start));
		} catch (Exception e) {

			log.error(e);
			e.printStackTrace();
			error.add("1951");
		}
		return error;
	}
	public List<EserviceMotorDetailsSaveRes> saveMotorDetails(EserviceMotorDetailsSaveReq req) {
		List<EserviceMotorDetailsSaveRes> res = new ArrayList<EserviceMotorDetailsSaveRes>();
		DozerBeanMapper mapper = new DozerBeanMapper();
		SimpleDateFormat yf = new SimpleDateFormat("yyyy");
		EserviceMotorDetails savedata = new EserviceMotorDetails();
		EserviceDriverDetails motorDriDetails = null;
		EserviceMotorDetails findData = null;
		String oaCode = "";
		try {

			if (null != req && req.getSumInsured() == null) {
				req.setSumInsured("0");
			}
			// Group 1: Parallelize pre-setup lookups
			CompletableFuture<CompanyProductMaster> companyProductFuture = CompletableFuture.supplyAsync(() ->
					eserMotDetServImpl.getCompanyProductMasterDropdown(req.getCompanyId(), req.getProductId()), motorLookupExecutor);

			boolean hasSections = req.getSectionId() != null && !req.getSectionId().isEmpty() && req.getSectionId().size() > 0;
			CompletableFuture<ProductSectionMaster> sectionFuture = hasSections
					? CompletableFuture.supplyAsync(() -> eserMotDetServImpl.getProductSectionDropdown(req.getCompanyId(), req.getProductId(), req.getSectionId().get(0)), motorLookupExecutor)
					: CompletableFuture.completedFuture(null);

			CompletableFuture<String> companyNameFuture = CompletableFuture.supplyAsync(() ->
					eserMotDetServImpl.getInscompanyMasterDropdown(req.getCompanyId()), motorLookupExecutor);

			CompletableFuture.allOf(companyProductFuture, sectionFuture, companyNameFuture).join();

			CompanyProductMaster companyProductMaster = companyProductFuture.join();
			String productName = companyProductMaster.getProductName();
			String sectionNameLocal = "";
			ProductSectionMaster section = sectionFuture.join();
			if (section != null) {
				sectionNameLocal = section.getSectionNameLocal();
			}
			String companyName = companyNameFuture.join();
			Integer vehId = 0;
			String refNo = "";
			Date entryDate = null;
			String createdBy = "",oldPolicyNumber="";
			String quoteNo=null;
			String customerId=null;
			//EserviceMotorDetails motOld = null;
			EServiceMotorDetailsMapper motOld = null;
			if (StringUtils.isBlank(req.getRequestReferenceNo())) {
				// Motor Insert
				entryDate = new Date();
				createdBy = req.getCreatedBy();

				// Generate Seq
				SequenceGenerateReq generateSeqReq = new SequenceGenerateReq();
				generateSeqReq.setInsuranceId(req.getCompanyId());
				generateSeqReq.setProductId(req.getProductId());
				generateSeqReq.setType("2");
				generateSeqReq.setTypeDesc("REQUEST_REFERENCE_NO");
				refNo = genSeqNoService.generateSeqCall(generateSeqReq);

			} else {
				// Motor Update
				refNo = req.getRequestReferenceNo();
			//	EserviceMotorDetails findoldData = repo.findByRequestReferenceNoAndRiskId(req.getRequestReferenceNo(),Integer.valueOf(req.getVehicleId()));
//				EServiceMotorDetailsProjectfindoldData = repo.findOldMinimal(req.getRequestReferenceNo(), Integer.valueOf(req.getVehicleId()));
//				if (findoldData != null) {
//					motOld = findoldData;
//					findData = new EserviceMotorDetails();
//					mapper.map(findoldData, findData);
//					repo.delete(findoldData);
//
//					entryDate = findData.getEntryDate();
//					createdBy = findData.getCreatedBy();
//					oldPolicyNumber=findData.getOldPolicyNumber();

				 motOld =
				        repo.findOldMinimal(refNo, Integer.valueOf(req.getVehicleId()));

				if (motOld != null) {
				    entryDate = motOld.getEntryDate();
				    createdBy = motOld.getCreatedBy();
				    oldPolicyNumber = motOld.getOldPolicyNumber();
				    quoteNo=motOld.getQuoteNo();
				    customerId=motOld.getCustomerId();
				    // isolate delete
				    deleteOldMotor(refNo, Integer.valueOf(req.getVehicleId()));
				} else {
				    entryDate = new Date();
				    createdBy = req.getCreatedBy();
				}

			}

			// Save Motor
			savedata = mapper.map(req, EserviceMotorDetails.class);
			savedata.setOldPolicyNumber(oldPolicyNumber);
			vehId = Integer.valueOf(req.getVehicleId());
			savedata.setRiskId(vehId);
			savedata.setCreatedBy(createdBy);
			savedata.setEntryDate(entryDate);
			savedata.setRequestReferenceNo(refNo);
			savedata.setUpdatedBy(req.getCreatedBy());
			savedata.setUpdatedDate(new Date());
			// CompanyId-100027
			savedata.setQuoteNo(StringUtils.isBlank(quoteNo) ? null :quoteNo);
			savedata.setCustomerId(StringUtils.isBlank(customerId) ? null :customerId);
			savedata.setExcess(StringUtils.isBlank(req.getExcess()) ? "" : req.getExcess());

			// Group 2: Batch all getListItem() calls into a single query
			Map<String, String> listItemBatch = new LinkedHashMap<>();
			if (req.getExcess() != null) {
				listItemBatch.put("DEDUCTIBLES", req.getExcess().toString());
			}
			if (req.getClassType() != null) {
				listItemBatch.put("CLASS", req.getClassType());
			}
			if (StringUtils.isNotBlank(req.getVehicleValueType())) {
				listItemBatch.put("VEHICLE_VALUE_TYPE", req.getVehicleValueType());
			}
			if (StringUtils.isNotBlank(req.getDefenceValue())) {
				listItemBatch.put("DEFENCE_COST", req.getDefenceValue());
			}
			if (req.getPaCoverId() != null) {
				listItemBatch.put("PA_COVER_ID", req.getPaCoverId());
			}
			if (req.getUsageId() != null) {
				listItemBatch.put("vehicle_usage", req.getUsageId());
			}
			if (req.getVehicleTypeIvr() != null) {
				listItemBatch.put("vehicle_type", req.getVehicleTypeIvr());
			}
			if (req.getZoneCirculation() != null) {
				listItemBatch.put("ZONE_CIRCULATION", req.getZoneCirculation());
			}

			Map<String, ListItemValue> batchResults = listItemBatch.isEmpty()
					? new LinkedHashMap<>()
					: eserMotDetServImpl.getListItems(req.getCompanyId(), req.getBranchCode(), listItemBatch);

			// Extract results from batch
			String excessLimiteDesc_en = "";
			String excessLimiteDesc_other = "";
			if (req.getExcess() != null) {
				ListItemValue li = batchResults.getOrDefault("DEDUCTIBLES|" + req.getExcess().toString(), new ListItemValue());
				excessLimiteDesc_en = li.getItemValue();
				excessLimiteDesc_other = li.getItemValueLocal();
			}
			if (req.getClassType() != null) {
				ListItemValue li = batchResults.getOrDefault("CLASS|" + req.getClassType(), new ListItemValue());
				savedata.setClassType(req.getClassType());
				savedata.setClassTypeDesc(li.getItemValue());
			}
			savedata.setExcessDesc(StringUtils.isBlank(excessLimiteDesc_en) ? "" : excessLimiteDesc_en);
			savedata.setExcessDescLocal(StringUtils.isBlank(excessLimiteDesc_other) ? "" : excessLimiteDesc_other);
			savedata.setVehicleValueType(StringUtils.isBlank(req.getVehicleValueType()) ? "" : req.getVehicleValueType());

			String vehicleValueTypeDesc_en = "";
			String vehicleValueTypeDesc_other = "";
			if (StringUtils.isNotBlank(req.getVehicleValueType())) {
				ListItemValue li = batchResults.getOrDefault("VEHICLE_VALUE_TYPE|" + req.getVehicleValueType(), new ListItemValue());
				vehicleValueTypeDesc_en = li.getItemValue();
				vehicleValueTypeDesc_other = li.getItemValueLocal();
			}
			savedata.setVehicleValueTypeDesc(StringUtils.isBlank(vehicleValueTypeDesc_en) ? "" : vehicleValueTypeDesc_en);
			savedata.setVehicleValueTypeDescLocal(StringUtils.isBlank(vehicleValueTypeDesc_other) ? "" : vehicleValueTypeDesc_other);
			savedata.setHorsePower(StringUtils.isBlank(req.getHorsePower()) ? null : Integer.valueOf(req.getHorsePower()));
			savedata.setZone(StringUtils.isBlank(req.getZone()) ? null : Integer.valueOf(req.getZone()!=null && !req.getZone().isBlank()?req.getZone():"0"));
			savedata.setInflation(StringUtils.isBlank(req.getInflation()) ? "" : req.getInflation());
			savedata.setInflationSi(req.getInflationSumInsured() != null ? req.getInflationSumInsured() : null);
			savedata.setPreviousInsuranceYn(StringUtils.isNotBlank(req.getPreviousInsuranceYN()) ? req.getPreviousInsuranceYN() : "");
			savedata.setLossRatio(req.getPreviousLossRatio() != null ? req.getPreviousLossRatio() : 0);
			savedata.setNcb(req.getNcb() == null ? "" : req.getNcb());
			savedata.setDefenceValue(StringUtils.isBlank(req.getDefenceValue()) ? "" : req.getDefenceValue());

			String defenceValueDesc_en = "";
			String defenceValueDesc_other = "";
			if (StringUtils.isNotBlank(req.getDefenceValue())) {
				ListItemValue li = batchResults.getOrDefault("DEFENCE_COST|" + req.getDefenceValue(), new ListItemValue());
				defenceValueDesc_en = li.getItemValue();
				defenceValueDesc_other = li.getItemValueLocal();
			}
			savedata.setDefenceValueDesc(StringUtils.isBlank(defenceValueDesc_en) ? "" : defenceValueDesc_en);
			savedata.setDefenceValueDescLocal(StringUtils.isBlank(defenceValueDesc_other) ? "" : defenceValueDesc_other);
			savedata.setRegistrationDate(req.getRegistrationDate() == null ? null : req.getRegistrationDate());
			savedata.setPurchaseDate(req.getPurchaseDate() == null ? null : req.getPurchaseDate());
			;

			// CompanyId 100028
			// Calim Type
			savedata.setNcdYears(StringUtils.isBlank(req.getClaimType()) ? 0 : Integer.valueOf(req.getClaimType()));
			savedata.setCustRenewalYn(StringUtils.isBlank(req.getCustRenewalYn()) ? "N" : req.getCustRenewalYn());

			//company_id 100040
			savedata.setCollateralCompanyName(StringUtils.isBlank(req.getCollateralCompanyName())?"":req.getCollateralCompanyName());
			savedata.setCollateralCompanyAddress(StringUtils.isBlank(req.getCollateralCompanyAddress())?"":req.getCollateralCompanyAddress());
			savedata.setBankingDelegation(StringUtils.isBlank(req.getBankingDelegation())?"":req.getBankingDelegation());
			savedata.setLoanStartDate(req.getLoanStartDate()==null?null:req.getLoanStartDate());
			savedata.setLoanEndDate(req.getLoanEndDate()==null?null:req.getLoanEndDate());
			savedata.setLoanAmount(req.getLoanAmount()!=null?req.getLoanAmount():0);
			savedata.setPaCoverId(StringUtils.isBlank(req.getPaCoverId())?"0":req.getPaCoverId());
		    savedata.setUsageId(StringUtils.isBlank(req.getUsageId())?"":req.getUsageId());
			savedata.setZonecirculation(StringUtils.isBlank(req.getZoneCirculation())?"":req.getZoneCirculation());
			savedata.setVehicleTypeIvr(StringUtils.isBlank(req.getVehicleTypeIvr())?"":req.getVehicleTypeIvr());

			// Extract PA_COVER_ID, vehicle_usage, vehicle_type, ZONE_CIRCULATION from batch results
			String pacoveriddes = req.getPaCoverId() != null
					? StringUtils.defaultString(batchResults.getOrDefault("PA_COVER_ID|" + req.getPaCoverId(), new ListItemValue()).getItemValue(), " ")
					: " ";
			String usageiddesc = req.getUsageId() != null
					? StringUtils.defaultString(batchResults.getOrDefault("vehicle_usage|" + req.getUsageId(), new ListItemValue()).getItemValue(), " ")
					: " ";
			String Vehicletypedes = req.getVehicleTypeIvr() != null
					? StringUtils.defaultString(batchResults.getOrDefault("vehicle_type|" + req.getVehicleTypeIvr(), new ListItemValue()).getItemValue(), " ")
					: " ";
			String zonecdes = req.getZoneCirculation() != null
					? StringUtils.defaultString(batchResults.getOrDefault("ZONE_CIRCULATION|" + req.getZoneCirculation(), new ListItemValue()).getItemValue(), " ")
					: " ";

			savedata.setPaCoveridDesc(pacoveriddes);
			savedata.setVehicleTypeDescIvr(Vehicletypedes);
			savedata.setUsageDesc(usageiddesc);
			savedata.setZonecirculationDesc(zonecdes);
			
			// Admin Details
//			if (findData != null) {
//
//				savedata.setAdminLoginId(findData.getAdminLoginId());
//				savedata.setAdminRemarks(findData.getAdminRemarks());
//				savedata.setReferalRemarks(findData.getReferalRemarks());
//				savedata.setRejectReason(findData.getRejectReason());
//				savedata.setQuoteNo(findData.getQuoteNo());
//				savedata.setCustomerId(findData.getCustomerId());
//				savedata.setOldReqRefNo(findData.getOldReqRefNo());
//				savedata.setCreatedBy(findData.getCreatedBy());
//				savedata.setEntryDate(findData.getEntryDate());
//				savedata.setManualReferalYn(findData.getManualReferalYn());
//
//			}
			String motorDesc_en = "";
			String motorUsageId = "";
			String usageDesc_en = "";
			String useageDesc_other = "";
			String bodyTypeName_en = "";
			String bodyTypeName_other = "";
			String bodyId = "";
			String makeDesc = "";

			if (StringUtils.isNotBlank(req.getVehicleTypeId())) {
				MotorBodyTypeMaster bodyType = eserMotDetServImpl.getBodyTypeName(req.getCompanyId(), req.getBranchCode(),req.getVehicleTypeId());
				if(bodyType!=null) {
					bodyTypeName_en = bodyType.getBodyNameEn();
					bodyTypeName_other = bodyType.getBodyNameLocal();
					savedata.setVehicleTypeDesc(bodyTypeName_en);
					savedata.setVehicleTypeDescLocal(bodyTypeName_other);
					savedata.setVehicleType(req.getVehicleTypeId());
				}
			} else if (StringUtils.isBlank(req.getVehicleTypeId())) {
				MotorBodyTypeMaster bodyTypeId = eserMotDetServImpl.getBodyTypeId(req.getCompanyId(), req.getBranchCode(),req.getVehicleType());
				if(bodyTypeId!=null) {
					bodyTypeName_en = bodyTypeId.getBodyNameEn();
					bodyTypeName_other = bodyTypeId.getBodyNameLocal();
					bodyId = bodyTypeId.getBodyId().toString();
					savedata.setVehicleType(bodyId);
					savedata.setVehicleTypeDesc(req.getVehicleType());
					savedata.setVehicleTypeDescLocal(bodyTypeName_other);
				}
			}

			if (req.getSearchFromApi() == null || req.getSearchFromApi() == true) {
				// Motor Vehice Info
			
				MotorVehicleInfo motorRes = new MotorVehicleInfo();
				motorRes = motorVehRepo.findTop1ByResRegNumberAndResChassisNumberAndSavedFromAndCompanyIdOrderByEntryDateDesc(
								req.getRegistrationnumber(), req.getChassisNumber(), "API", req.getCompanyId());
				if (motorRes == null) {
					motorRes = motorVehRepo.findTop1ByResRegNumberAndResChassisNumberAndSavedFromAndCompanyIdOrderByEntryDateDesc(
									req.getRegistrationnumber(), req.getChassisNumber(), "WEB", req.getCompanyId());
				}

				savedata.setAxelDistance(motorRes.getResAxleDistance() == null ? null: new BigDecimal(motorRes.getResAxleDistance().toString()));
				savedata.setChassisNumber(motorRes.getResChassisNumber().toUpperCase());
				savedata.setColor(motorRes.getResColor());

				savedata.setCubicCapacity(String.valueOf(motorRes.getResEngineCapacity()) != null? new BigDecimal(motorRes.getResEngineCapacity()): null);
				savedata.setColorDesc(motorRes.getResColor());
				savedata.setFuelType(motorRes.getResFuelUsed());
				savedata.setFuelTypeDesc(motorRes.getResFuelUsed());
				savedata.setEngineNumber(motorRes.getResEngineNumber());
				savedata.setGrossWeight(motorRes.getResGrossWeight() == null ? null: new BigDecimal(motorRes.getResGrossWeight().toString()));
				ListItemValue listItem = eserMotDetServImpl.getListItem(req.getCompanyId(), req.getBranchCode(), "MOTOR_CATEGORY",motorRes.getResMotorCategory() == null ? "" : motorRes.getResMotorCategory().toString()); 
				motorDesc_en = listItem.getItemValue();
				String motorDesc_other = listItem.getItemValueLocal();

				savedata.setMotorCategory(motorRes.getResMotorCategory() == null ? null : motorRes.getResMotorCategory().toString());
				savedata.setMotorCategoryDesc(motorDesc_en);
				savedata.setMotorCategoryDescLocal(motorDesc_other);

				savedata.setOwnerCategory(motorRes.getResOwnerCategory());
				savedata.setRegistrationNumber(motorRes.getResRegNumber());
				savedata.setNumberOfAxels(motorRes.getResNumberOfAxles() == null ? null	: Integer.valueOf(motorRes.getResNumberOfAxles()));
				savedata.setSeatingCapacity(motorRes.getResSittingCapacity() == null ? null	: Integer.valueOf(motorRes.getResSittingCapacity()));
				savedata.setTareWeight(motorRes.getResTareWeight() == null ? null : new BigDecimal(motorRes.getResTareWeight()));
				savedata.setManufactureYear(motorRes.getResYearOfManufacture() == null ? null : motorRes.getResYearOfManufacture());
				savedata.setTiraBodyType(motorRes.getResBodyType());
				savedata.setTiraMotorUsage(motorRes.getResMotorUsage());
				savedata.setModelNumber(motorRes.getModelNumber());
				savedata.setOwnerName(motorRes.getResOwnerName());
				savedata.setNoOfCylinders(motorRes.getNoOfCylinders());
				savedata.setPlateType(StringUtils.isBlank(motorRes.getPlateType())?"0":motorRes.getPlateType());
				savedata.setDisplacementInCM3(motorRes.getDisplacementInCM3());
				// Manufacture Age
				Date today = new Date();
				String year = yf.format(today);
				int manuAge = 0;

				if (motorRes.getResYearOfManufacture() != null) {
					manuAge = Integer.valueOf(year) - Integer.valueOf(motorRes.getResYearOfManufacture());
				}
				savedata.setManufactureAge(manuAge);
				savedata.setEngineNumber(motorRes.getResEngineNumber()==null?"":motorRes.getResEngineNumber().toUpperCase());
			
				String sectionIdStr = (req.getSectionId()!=null && ! req.getSectionId().isEmpty() && req.getSectionId().size() > 0 )? req.getSectionId().get(0): "";
				
					
				if (StringUtils.isNotBlank(req.getMotorUsageId()) && StringUtils.isNotBlank(sectionIdStr)) {
					MotorVehicleUsageMaster motorUsage = eserMotDetServImpl.getMotorUsageName(req.getCompanyId(), req.getBranchCode(),sectionIdStr, req.getMotorUsageId());
					if(motorUsage!=null) {
						savedata.setMotorUsage(req.getMotorUsageId());
						usageDesc_en = motorUsage.getVehicleUsageDesc();
						useageDesc_other = motorUsage.getVehicleUsageDescLocal();
						savedata.setMotorUsageDesc(usageDesc_en);
						savedata.setMotorUsageDescLocal(useageDesc_other);
					}
				} else if (StringUtils.isNotBlank(req.getMotorUsage()) && StringUtils.isBlank(req.getMotorUsageId())) {
					MotorVehicleUsageMaster motorUsage = eserMotDetServImpl.getMotorUsageId(req.getCompanyId(), req.getBranchCode(), null,req.getMotorUsage());
					if(motorUsage!=null) {
						usageDesc_en = motorUsage.getVehicleUsageDesc();
						useageDesc_other = motorUsage.getVehicleUsageDescLocal();
						savedata.setMotorUsageDesc(usageDesc_en);
						savedata.setMotorUsageDescLocal(useageDesc_other);
						savedata.setMotorUsage(motorUsageId);
					}
				}

				savedata.setVehicleMake(motorRes.getResMake());
				savedata.setVehicleMakeDesc(motorRes.getResMake());
				savedata.setVehcileModelDesc(motorRes.getResModel());
				savedata.setVehcileModel(motorRes.getResModel());
				savedata.setVehicleModelId(motorRes.getResModel());

			} else {
				savedata.setManufactureYear(req.getManufactureYear() == null ? null : req.getManufactureYear().intValue());
				Date today = new Date();
				String year = yf.format(today);
				int manuAge = 0;

				if (req.getManufactureYear() != null) {
					manuAge = Integer.valueOf(year) - req.getManufactureYear().intValue();
				}
				savedata.setManufactureAge(manuAge);
				savedata.setColor(req.getColor());
				savedata.setCubicCapacity(req.getEngineCapacity() == null ? BigDecimal.ZERO : req.getEngineCapacity());
				savedata.setSeatingCapacity(req.getSeatingCapcity() == null ? 0 : req.getSeatingCapcity().intValue());
				savedata.setGrossWeight(req.getGrossWeight()==null?new BigDecimal("10000"):new BigDecimal(req.getGrossWeight()));
				savedata.setOwnerCategory("");
				savedata.setFuelType(req.getFuelType());
				savedata.setVehicleMakeDesc(StringUtils.isBlank(req.getVehicleMakeDesc()) ? "" : req.getVehicleMakeDesc());
				savedata.setFuelTypeDesc(StringUtils.isBlank(req.getFuelTypeDesc()) ? "" : req.getFuelTypeDesc());
				savedata.setEngineNumber(StringUtils.isBlank(req.getEngineNumber()) ? "" : req.getEngineNumber().toUpperCase());
				savedata.setVehcileModelDesc(req.getVehcileModel());
				savedata.setVehcileModel(StringUtils.isBlank(req.getVehcileModelId()) ? "" : req.getVehcileModelId());
				savedata.setVehicleMakeDesc(StringUtils.isBlank(req.getVehicleMakeDesc()) ? "" : req.getVehicleMakeDesc());
//				savedata.setColorDesc(StringUtils.isBlank(req.getColorDesc()) ? "" : req.getColorDesc());
				savedata.setChassisNumber(StringUtils.isBlank(req.getChassisNumber()) ? "" : req.getChassisNumber().toUpperCase());
				savedata.setRegistrationNumber(StringUtils.isBlank(req.getRegistrationnumber()) ? "": req.getRegistrationnumber().toUpperCase());

				if(req.getVehicleId()!=null && StringUtils.isBlank(req.getColorDesc())) {
					Integer colorId=Integer.parseInt(req.getColor());
					List<MotorColorMaster>mcm= motorColorMasterRepository.findByColorIdAndCompanyIdAndStatus(colorId, req.getCompanyId(), "Y");
				         savedata.setColorDesc(mcm.get(0).getColorDesc());
				}
				
				if (StringUtils.isNotBlank(req.getMotorUsageId())) {
					MotorVehicleUsageMaster motorUsageName = eserMotDetServImpl.getMotorUsageName(req.getCompanyId(), req.getBranchCode(),"", req.getMotorUsageId());
					if(motorUsageName!=null) {
						savedata.setMotorUsage(req.getMotorUsageId());
						usageDesc_en = motorUsageName.getVehicleUsageDesc();
						useageDesc_other = motorUsageName.getVehicleUsageDescLocal();
						savedata.setMotorUsageDesc(usageDesc_en);
						savedata.setMotorUsageDescLocal(useageDesc_other);
					}
					
				} else if (StringUtils.isNotBlank(req.getMotorUsage()) && StringUtils.isBlank(req.getMotorUsageId())) {
					MotorVehicleUsageMaster motorUsage = eserMotDetServImpl.getMotorUsageId(req.getCompanyId(), req.getBranchCode(), null,req.getMotorUsage());
					if(motorUsage!=null) {
						motorUsageId = motorUsage.getVehicleUsageId().toString();
						usageDesc_en = motorUsage.getVehicleUsageDesc();
						useageDesc_other = motorUsage.getVehicleUsageDescLocal();
						savedata.setMotorUsageDesc(usageDesc_en);
						savedata.setMotorUsage(motorUsageId);
						savedata.setMotorUsageDescLocal(useageDesc_other);
					}
				}
				savedata.setTiraMotorUsage(usageDesc_en);
				savedata.setTiraBodyType(bodyTypeName_en);
				savedata.setMotorCategory(req.getMotorCategory());
				savedata.setMotorCategoryDesc(req.getMotorCategoryDesc());
				savedata.setVehicleMakeId(req.getVehicleMakeId());
				savedata.setVehicleMake(req.getVehicleMakeId());
				savedata.setVehicleMakeDesc(req.getVehicleMake());
				savedata.setVehicleModelId(req.getVehcileModelId());
				savedata.setVehcileModelDesc(req.getVehcileModel());

				if (StringUtils.isNotBlank(req.getVehicleTypeId())) {
					MotorBodyTypeMaster bodyTypeName = eserMotDetServImpl.getBodyTypeName(req.getCompanyId(), req.getBranchCode(),req.getVehicleTypeId());
					if(bodyTypeName!=null) {
						String bodyTypeNameEn = bodyTypeName.getBodyNameEn();
						//String bodyTypeNameLocal = bodyTypeName.getBodyNameLocal();
						savedata.setVehicleTypeDesc(bodyTypeNameEn);
						savedata.setVehicleType(req.getVehicleTypeId());
					}
				} else if (StringUtils.isBlank(req.getVehicleTypeId())) {
					MotorBodyTypeMaster bodyTypeId = eserMotDetServImpl.getBodyTypeId(req.getCompanyId(), req.getBranchCode(),req.getVehicleType());
					if(bodyTypeId!=null) {
					bodyId = bodyTypeId.getBodyId().toString();
					//String bodyTypeNameLocal = bodyTypeId.getBodyNameLocal();
					savedata.setVehicleType(bodyId);
					savedata.setVehicleTypeDesc(req.getVehicleType());
					}
				}

				if (StringUtils.isBlank(req.getVehicleMakeId())) {
					makeDesc = eserMotDetServImpl.getMotorMakeDesc(req.getCompanyId(), req.getBranchCode(), req.getVehicleMakeId());
					savedata.setVehicleMakeDesc(makeDesc);
				}
				if (StringUtils.isNotBlank(savedata.getVehicleMakeId())	&& StringUtils.isNotBlank(savedata.getVehicleModelId())) {
					if("99999".equals(req.getVehcileModelId())) {
						savedata.setVehcileModelDesc(req.getVehcileModel());						
					}
					else {
						List<MotorMakeModelMaster> models = eserMotDetServImpl.getModelDesc(req.getCompanyId(), req.getBranchCode(),req.getVehicleMakeId(), req.getVehcileModelId());
						savedata.setVehcileModelDesc(models.size() > 0 && models.get(0).getModelNameEn() != null? models.get(0).getModelNameEn().toString(): "");
					}
				}
				if (StringUtils.isNotBlank(req.getFuelType())) {
					String fuelTypedesc = eserMotDetServImpl.getListItemCode(req.getCompanyId(), req.getBranchCode(), "FUEL_TYPE",req.getFuelType()); 
					savedata.setFuelTypeDesc(StringUtils.isNotBlank(req.getFuelTypeDesc())?req.getFuelTypeDesc():fuelTypedesc);
				}

			}

			String borrowerDesc_en = "";
			String borrowerDesc_other = "";
			if (StringUtils.isNotBlank(req.getBorrowerType())) {
				ListItemValue borrowerDescL = eserMotDetServImpl.getListItem(req.getCompanyId(), req.getBranchCode(), "BORROWER_TYPE",req.getBorrowerType());
				borrowerDesc_en = borrowerDescL.getItemValue();
				borrowerDesc_other = borrowerDescL.getItemValueLocal();
			}
			String policyTypeName = eserMotDetServImpl.getPolicyTypeName(req.getInsuranceClass(), req.getCompanyId(), req.getProductId());

			savedata.setInsuranceClassDesc(policyTypeName);
			savedata.setPolicyType(req.getInsuranceClass());
			savedata.setSectionId((req.getSectionId()!=null &&! req.getSectionId().isEmpty() && req.getSectionId().size()>0  )? req.getSectionId().get(0): "0") ;
		    savedata.setPolicyTypeDesc(StringUtils.isBlank(req.getInsuranceClassDesc())? "":req.getInsuranceClassDesc());
			savedata.setInsuranceType(StringUtils.isBlank(req.getInsuranceType()) ? "0" : req.getInsuranceType());
			savedata.setInsuranceTypeDesc(StringUtils.isBlank(req.getInsuranceTypeDesc())? "":req.getInsuranceTypeDesc());
			savedata.setPolicyTypeDesc(policyTypeName);
			savedata.setAcExecutiveId(StringUtils.isBlank(req.getAcExecutiveId()) ? null : Integer.valueOf(req.getAcExecutiveId()));
			savedata.setBorrowerTypeDesc(borrowerDesc_en);
			savedata.setBorrowerTypeDescLocal(borrowerDesc_other);
			savedata.setNoOfVehicles(StringUtils.isBlank(req.getNoOfVehicles()) ? 0 : Integer.valueOf(req.getNoOfVehicles()));
			savedata.setNoOfCompehensives(StringUtils.isBlank(req.getNoOfComprehensives()) ? 0: Integer.valueOf(req.getNoOfComprehensives()));
			savedata.setClaimRatio(StringUtils.isBlank(req.getClaimRatio()) ? BigDecimal.ZERO : new BigDecimal(req.getClaimRatio()));
			if (StringUtils.isNotBlank(req.getFleetOwnerYn()) && "Y".equalsIgnoreCase(req.getFleetOwnerYn())) {
				savedata.setNoOfVehicles(StringUtils.isBlank(req.getVehicleId()) ? 0 : Integer.valueOf(req.getVehicleId()));
			} else {
				savedata.setNoOfVehicles(StringUtils.isBlank(req.getNoOfVehicles()) ? vehId : Integer.valueOf(req.getNoOfVehicles()));
			}
			savedata.setHavepromocode(req.getHavepromocode());
			savedata.setPromocode(req.getHavepromocode().equalsIgnoreCase("N") ? null : req.getPromocode());
			//savedata.setMotorUsageDesc(usageDesc_en);
			savedata.setMotorUsageDescLocal(useageDesc_other);
			savedata.setAccident(StringUtils.isBlank(req.getAccident()) ? "N" : req.getAccident());
			savedata.setWindScreenCoverRequired(req.getWindScreenSumInsured() != null && StringUtils.isNotBlank(req.getWindScreenSumInsured().toString()) ? "Y" : "N");
			savedata.setCustomerReferenceNo(req.getCustomerReferenceNo());
			savedata.setProductName(productName);
			savedata.setSectionName(section == null ? "" : section.getSectionName());
			savedata.setCompanyName(companyName);

			String decimalDigits = eserMotDetServImpl.currencyDecimalFormat(req.getCompanyId(), req.getCurrency()).toString();
			String stringFormat = "%0" + decimalDigits + "d";
			String decimalLength = decimalDigits.equals("0") ? "" : String.format(stringFormat, 0L);
			String pattern = StringUtils.isBlank(decimalLength) ? "#####0" : "#####0." + decimalLength;
			DecimalFormat df = new DecimalFormat(pattern);

			// Broker Commission
			// Source Type Search Condition
			List<String> directSource = new ArrayList<String>();
			directSource.add("1");
			directSource.add("2");
			directSource.add("3");
			directSource.add("4");
			Double commissionPercent = 0.0;
			// Source Type Search Condition
			List<ListItemValue> sourcerTypes = premiaBrokerService.getSourceTypeDropdown(req.getCompanyId(),req.getBranchCode(), "SOURCE_TYPE");
			List<ListItemValue> filterSource = sourcerTypes.stream()
					.filter(o -> StringUtils.isNotBlank(req.getSourceTypeId()) && (o.getItemCode().equalsIgnoreCase(req.getSourceTypeId())
									|| o.getItemValue().equalsIgnoreCase(req.getSourceTypeId()) && !"1".equalsIgnoreCase(req.getApplicationId()))).collect(Collectors.toList());

			if (filterSource.size() > 0 && directSource.contains(filterSource.get(0).getItemCode())) {
				String sourceType = filterSource.get(0).getItemValue();
				String subUserType = sourceType.contains("Broker") ? "Broker": sourceType.contains("Agent") ? "Agent" : "";
				LoginMaster premiaLogin = eserMotDetServImpl.getPremiaBroker(req.getBdmCode(), subUserType, req.getCompanyId());
				String premiaLoginId = premiaLogin != null ? premiaLogin.getLoginId() : "";

				ListItemValue commissionI = eserMotDetServImpl.getListItem(req.getCompanyId(), req.getBranchCode(), "COMMISSION_PERCENT",filterSource.get(0).getItemValue());
				String commission = commissionI.getItemValue();
				if (StringUtils.isNotBlank(premiaLoginId)) {
					List<BrokerCommissionDetails> commissionList = eserMotDetServImpl.getPolicyName(req.getCompanyId(), req.getProductId(),	premiaLoginId, req.getBrokerCode(), req.getInsuranceClass(), req.getUserType());
					commission = commissionList.size() > 0 && commissionList.get(0).getCommissionPercentage() != null? commissionList.get(0).getCommissionPercentage().toString(): commission;
				}
				// commissionPercent=12.5;
				commissionPercent = StringUtils.isNotBlank(commission) ? Double.valueOf(commission) : 0D;
				savedata.setCommissionPercentage(commissionPercent == null ? new BigDecimal("0") : new BigDecimal(commissionPercent));

			} else {
				String loginId = StringUtils.isNotBlank(req.getSourceType())&& req.getSourceType().toLowerCase().contains("b2c") ? "guest" : req.getLoginId();
				List<BrokerCommissionDetails> commissionList = eserMotDetServImpl.getPolicyName(req.getCompanyId(), req.getProductId(),loginId, req.getBrokerCode(), req.getInsuranceClass(), req.getUserType());
				if (commissionList != null && commissionList.size() > 0) {
					BrokerCommissionDetails comm = commissionList.get(0);
					savedata.setCommissionPercentage(comm.getCommissionPercentage() == null ? new BigDecimal("0"): new BigDecimal(comm.getCommissionPercentage()));
					savedata.setVatCommission(comm.getCommissionVatPercent() == null ? new BigDecimal("0"): new BigDecimal(comm.getCommissionVatPercent()));
				} else {
					savedata.setCommissionPercentage(new BigDecimal("0"));
					savedata.setVatCommission(new BigDecimal("0"));

				}
			}

			// Endrosement CHanges
			if (!(req.getEndorsementType() == null || req.getEndorsementType() == 0))

			{
				EserviceMotorDetails findoldData = repo.findByRequestReferenceNoAndRiskId(req.getRequestReferenceNo(),Integer.valueOf(req.getVehicleId()));
				
				savedata.setOriginalPolicyNo(req.getOriginalPolicyNo());
				savedata.setEndorsementDate(req.getEndorsementDate());
				savedata.setEndorsementRemarks(req.getEndorsementRemarks());
				savedata.setEndorsementEffdate(req.getEndorsementEffdate());
				savedata.setEndtPrevPolicyNo(req.getEndtPrevPolicyNo());
				savedata.setEndtPrevQuoteNo(req.getEndtPrevQuoteNo());
				savedata.setEndtCount(req.getEndtCount());
				savedata.setEndtStatus(req.getEndtStatus());
				savedata.setIsFinaceYn(req.getIsFinaceYn());
				savedata.setEndtCategDesc(req.getEndtCategDesc());
				savedata.setEndorsementType(req.getEndorsementType());
				savedata.setEndorsementTypeDesc(req.getEndorsementTypeDesc());

				// Commission
				MotorDataDetails mainMot = motRepo.findByQuoteNoAndVehicleId(req.getEndtPrevQuoteNo(),req.getVehicleId());
				if (mainMot != null && mainMot.getInsuranceClass().equals(req.getInsuranceClass())) {
					savedata.setCommissionPercentage(mainMot.getCommissionPercentage() == null ? savedata.getCommissionPercentage()	: new BigDecimal(mainMot.getCommissionPercentage()));
					savedata.setVatCommission(mainMot.getVatCommission() == null ? savedata.getVatCommission(): new BigDecimal(mainMot.getVatCommission()));
					// motor old vehicle details
					savedata.setAxelDistance(mainMot.getAxelDistance() == null ? null: new BigDecimal(mainMot.getAxelDistance().toString()));
					savedata.setChassisNumber(mainMot.getChassisNumber());
					savedata.setColor(mainMot.getColor());

					savedata.setCubicCapacity(mainMot.getCubicCapacity() == null ? null: new BigDecimal(mainMot.getCubicCapacity().toString()));
					savedata.setColorDesc(mainMot.getColorDesc());
					savedata.setFuelType(mainMot.getFuelType());
					savedata.setFuelTypeDesc(mainMot.getFuelTypeDesc());
					savedata.setEngineNumber(mainMot.getEngineNumber());
					savedata.setGrossWeight(mainMot.getGrossWeight() == null ? null: new BigDecimal(mainMot.getGrossWeight().toString()));
					savedata.setMotorCategory(mainMot.getMotorCategory());
					savedata.setMotorCategoryDesc(mainMot.getMotorCategoryDesc());
					savedata.setOwnerCategory(mainMot.getOwnerCategory());
					savedata.setRegistrationNumber(mainMot.getRegistrationNumber());
					savedata.setNumberOfAxels(mainMot.getNumberOfAxels());
					savedata.setSeatingCapacity(mainMot.getSeatingCapacity());
					savedata.setTareWeight(mainMot.getTareWeight() == null ? null: new BigDecimal(mainMot.getTareWeight().toString()));
					savedata.setManufactureYear(mainMot.getManufactureYear() == null ? null : mainMot.getManufactureYear());
					savedata.setTiraBodyType(mainMot.getTiraBodyType());
					savedata.setTiraMotorUsage(mainMot.getTiraMotorUsage());
					savedata.setModelNumber(mainMot.getModelNumber());
					// Manufacture Age
					savedata.setManufactureAge(mainMot.getManufactureAge());
					savedata.setVehcileModelDesc(req.getVehcileModel());
					savedata.setVehcileModelDesc(req.getVehcileModel());
					savedata.setEngineNumber(mainMot.getEngineNumber().toUpperCase());
					savedata.setVehicleMake(mainMot.getVehicleMake());
					savedata.setVehicleMakeDesc(mainMot.getVehicleMakeDesc());
					savedata.setVehcileModel(req.getVehcileModelId());
					savedata.setVehcileModelDesc(req.getVehcileModel());

				}
			}

			// Status
			savedata.setStatus(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus());

			// Group 4: Parallelize make/fuel/owner/customer lookups
			// Capture current values for lambda safety (savedata is not effectively final)
			final String vehicleMakeForLookup = savedata.getVehicleMake();
			final String fuelTypeForLookup = savedata.getFuelType();
			final String ownerCatForLookup = savedata.getOwnerCategory();
			final String companyIdForLookup = req.getCompanyId();
			final String branchCodeForLookup = req.getBranchCode();

			// Make Id — run in parallel with fuel/owner/customer lookups
			CompletableFuture<String> makeIdFuture = StringUtils.isNotBlank(vehicleMakeForLookup)
					? CompletableFuture.supplyAsync(() -> eserMotDetServImpl.getMotorMakeId(companyIdForLookup, branchCodeForLookup, vehicleMakeForLookup), motorLookupExecutor)
					: CompletableFuture.completedFuture(null);

			// Fuel Id
			CompletableFuture<String> fuelIdFuture = StringUtils.isNotBlank(fuelTypeForLookup)
					? CompletableFuture.supplyAsync(() -> eserMotDetServImpl.getListItemvalue(companyIdForLookup, branchCodeForLookup, "FUEL_TYPE", fuelTypeForLookup), motorLookupExecutor)
					: CompletableFuture.completedFuture(null);

			// Owner Category Id
			CompletableFuture<String> ownerCatIdFuture = StringUtils.isNotBlank(ownerCatForLookup)
					? CompletableFuture.supplyAsync(() -> eserMotDetServImpl.getListItemCode(companyIdForLookup, branchCodeForLookup, "OWNER_CATEGORY", ownerCatForLookup), motorLookupExecutor)
					: CompletableFuture.completedFuture(null);

			// Customer Type
			CompletableFuture<EserviceCustomerDetails> custFuture = CompletableFuture.supplyAsync(() ->
					custRepo.findByCustomerReferenceNo(req.getCustomerReferenceNo()), motorLookupExecutor);

			CompletableFuture.allOf(makeIdFuture, fuelIdFuture, ownerCatIdFuture, custFuture).join();

			// Set Make Id
			String makeId = makeIdFuture.join();
			if (makeId != null) {
				savedata.setVehicleMakeId(makeId);
			}
			// Model Id — depends on makeId, must run after
			if (StringUtils.isNotBlank(savedata.getVehcileModel())
					&& StringUtils.isNotBlank(savedata.getVehicleMakeId())) {
				List<MotorMakeModelMaster> models = eserMotDetServImpl.getModelId(req.getCompanyId(), req.getBranchCode(),
						savedata.getVehicleMakeId(), savedata.getVehicleType(), savedata.getVehcileModelDesc());
				savedata.setVehicleModelId(models.size() > 0 && models.get(0).getModelId() != null ? models.get(0).getModelId().toString(): "99999");
				savedata.setManufactureCountry(models.size() > 0 && models.get(0).getModelId() != null ? models.get(0).getVehManfCountry(): "");
			}
			// Set Fuel Id
			String fuelTypeId = fuelIdFuture.join();
			if (fuelTypeId != null) {
				savedata.setFuelTypeId(fuelTypeId);
			}
			// Set Owner Category Id
			String ownerCategoryId = ownerCatIdFuture.join();
			if (ownerCategoryId != null) {
				savedata.setOwnerCategoryId(ownerCategoryId);
			}
			// Set Customer Type
			EserviceCustomerDetails custData = custFuture.join();
			if (custData != null) {
				savedata.setCustomerType(custData.getPolicyHolderType());
			}

			if (motOld != null) {
				// Vehicle Suminsured
				BigDecimal Si = eserMotDetServImpl.exchangeRateScenario2(motOld.getSumInsured(),
						StringUtils.isBlank(req.getSumInsured()) ? null : new BigDecimal(req.getSumInsured()),
						motOld.getExchangeRate(), new BigDecimal(req.getExchangeRate()), motOld.getCurrency(),
						req.getCurrency(), df);
				savedata.setSumInsured(Si);
				savedata.setNonDepreciatedSi(Si);

				// Accessories Suminsured
				BigDecimal accSi = eserMotDetServImpl.exchangeRateScenario2(motOld.getAcccessoriesSumInsured(),StringUtils.isBlank(req.getAcccessoriesSumInsured()) ? null: new BigDecimal(req.getAcccessoriesSumInsured())
								,motOld.getExchangeRate(), new BigDecimal(req.getExchangeRate()), motOld.getCurrency(),req.getCurrency(), df);
				savedata.setAcccessoriesSumInsured(accSi);
				// Windscreen Suminsured
				BigDecimal windSi = eserMotDetServImpl.exchangeRateScenario2(motOld.getWindScreenSumInsured(),StringUtils.isBlank(req.getWindScreenSumInsured()) ? null: new BigDecimal(req.getWindScreenSumInsured()),
						motOld.getExchangeRate(), new BigDecimal(req.getExchangeRate()), motOld.getCurrency(),req.getCurrency(), df);
				savedata.setWindScreenSumInsured(windSi);
				// Tppd Sumisnured
				BigDecimal tppdSi = eserMotDetServImpl.exchangeRateScenario2(motOld.getTppdIncreaeLimit(),StringUtils.isBlank(req.getTppdIncreaeLimit()) ? null: new BigDecimal(req.getTppdIncreaeLimit()),
						motOld.getExchangeRate(), new BigDecimal(req.getExchangeRate()), motOld.getCurrency(),req.getCurrency(), df);
				savedata.setTppdIncreaeLimit(tppdSi);

				BigDecimal nonelecacc = eserMotDetServImpl.exchangeRateScenario2(motOld.getNonElecAccessoriesSiLc(),req.getNonELecAccessoriesSi() == null ? null : new BigDecimal(req.getNonELecAccessoriesSi()),
						motOld.getExchangeRate(), new BigDecimal(req.getExchangeRate()), motOld.getCurrency(),req.getCurrency(), df);
				savedata.setNonElecAccessoriesSi(nonelecacc == null ? null : new BigDecimal(nonelecacc.toString()));

				BigDecimal excesslimit = eserMotDetServImpl.exchangeRateScenario2(motOld.getExcessLimitLc(),req.getExcessLimit() == null ? null : new BigDecimal(req.getExcessLimit()),
						motOld.getExchangeRate(), new BigDecimal(req.getExchangeRate()), motOld.getCurrency(),req.getCurrency(), df);
				savedata.setExcessLimit(excesslimit == null ? null : new BigDecimal(excesslimit.toString()));

			} else {
				savedata.setSumInsured(StringUtils.isBlank(req.getSumInsured()) ? null: new BigDecimal(df.format(new BigDecimal(req.getSumInsured()))));
				savedata.setNonDepreciatedSi(StringUtils.isBlank(req.getSumInsured()) ? null: new BigDecimal(df.format(new BigDecimal(req.getSumInsured()))));
				savedata.setAcccessoriesSumInsured(StringUtils.isBlank(req.getAcccessoriesSumInsured()) ? null: new BigDecimal(df.format(new BigDecimal(req.getAcccessoriesSumInsured()))));
				savedata.setWindScreenSumInsured(StringUtils.isBlank(req.getWindScreenSumInsured()) ? null: new BigDecimal(df.format(new BigDecimal(req.getWindScreenSumInsured()))));
				savedata.setTppdIncreaeLimit(StringUtils.isBlank(req.getTppdIncreaeLimit()) ? null: new BigDecimal(df.format(new BigDecimal(req.getTppdIncreaeLimit()))));
				savedata.setNonElecAccessoriesSi(req.getNonELecAccessoriesSi() == null ? null: new BigDecimal(df.format(new BigDecimal(req.getNonELecAccessoriesSi()))));
				savedata.setExcessLimit(req.getExcessLimit() == null ? null: new BigDecimal(df.format(new BigDecimal(req.getExcessLimit()))));
				savedata.setTppdFreeLimit(null);
			}

			// }
			// Lc Calculation
			BigDecimal exchangeRate = StringUtils.isNotBlank(req.getExchangeRate())	? new BigDecimal(req.getExchangeRate())	: BigDecimal.ZERO;
			List<InsuranceCompanyMaster> companyDetails = eserMotDetServImpl.getInscompanyMasterDetails(req.getCompanyId());
			String currency = companyDetails.size() > 0 ? companyDetails.get(0).getCurrencyId() : req.getCurrency();
			decimalDigits = eserMotDetServImpl.currencyDecimalFormat(req.getCompanyId(), currency).toString();
			stringFormat = "%0" + decimalDigits + "d";
			decimalLength = decimalDigits.equals("0") ? "" : String.format(stringFormat, 0L);
			pattern = StringUtils.isBlank(decimalLength) ? "#####0" : "#####0." + decimalLength;
			df = new DecimalFormat(pattern);
			
			savedata.setSumInsuredLc(savedata.getSumInsured() == null ? null: new BigDecimal(df.format(savedata.getSumInsured().multiply(exchangeRate))));
			savedata.setAcccessoriesSumInsuredLc(savedata.getAcccessoriesSumInsured() == null ? null: new BigDecimal(df.format(savedata.getAcccessoriesSumInsured().multiply(exchangeRate))));
			savedata.setWindScreenSumInsuredLc(savedata.getWindScreenSumInsured() == null ? null: new BigDecimal(df.format(savedata.getWindScreenSumInsured().multiply(exchangeRate))));
			savedata.setTppdIncreaeLimitLc(savedata.getTppdIncreaeLimit() == null ? null: new BigDecimal(df.format(savedata.getTppdIncreaeLimit().multiply(exchangeRate))));
			savedata.setTppdFreeLimitLc(savedata.getTppdFreeLimit() == null ? null: new BigDecimal(df.format(savedata.getTppdFreeLimit().multiply(exchangeRate))));

			if (StringUtils.isNotBlank(req.getPeriodOfInsurance())
			        && req.getEndorsementType() != null
			        && req.getEndorsementType() == 0)				{
			// Date Differents
						Date periodStart = req.getPolicyStartDate();
						Date periodEnd = null;
						String diff = "0";

						Integer policyPeriod = Integer.valueOf(req.getPeriodOfInsurance());

						if (periodStart != null && policyPeriod != null && policyPeriod > 0) {
							
							Calendar cal = Calendar.getInstance();
						    cal.setTime(periodStart);
						    cal.add(Calendar.DAY_OF_MONTH, policyPeriod - 1); 
						    periodEnd = cal.getTime();

						    
						    req.setPolicyEndDate(periodEnd);  

						    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
						    String st = sdf.format(periodStart);
						    String ed = sdf.format(periodEnd);

						    if (st.equalsIgnoreCase(ed) && (req.getEndorsementType() == null || req.getEndorsementType() == 0)) {
						        diff = "1";
						    } else if (st.equalsIgnoreCase(ed)) {
						        diff = "0";
						    } else {
						        Long diffInMillies = Math.abs(periodEnd.getTime() - periodStart.getTime());
						        Long daysBetween = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS) + 1;
						        diff = String.valueOf(daysBetween);
						    }
						}

						savedata.setPeriodOfInsurance(diff);
						savedata.setPolicyEndDate(periodEnd);
			}
			else
			{
				String diff = "0";
				Date periodStart = req.getPolicyStartDate();
				Date periodEnd = req.getPolicyEndDate();
				if (periodStart != null && periodEnd != null) {
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					String st = sdf.format(req.getPolicyStartDate());
					String ed = sdf.format(req.getPolicyEndDate());
					if (st.equalsIgnoreCase(ed)
							&& (req.getEndorsementType() == null || req.getEndorsementType() == 0)) {
						diff = "1";
					} else if (st.equalsIgnoreCase(ed)) {
						diff = "0";
					} else {
						Long diffInMillies = Math.abs(periodEnd.getTime() - periodStart.getTime());
						Long daysBetween = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS) + 1;
						diff = String.valueOf(daysBetween);
					}
				}
				savedata.setPeriodOfInsurance(diff);
				savedata.setPolicyEndDate(periodEnd);
			}
	/*		if(req.getEndorsementType()!=null && req.getEndorsementType()==845) {
				savedata.setPeriodOfInsurance("365");
			}*/
			// One Time Table Columns
			savedata.setCdRefno(null);
			savedata.setVdRefno(null);
			savedata.setMsRefno(null);
		

			// Source Type Details
			BranchMaster branchData = eserMotDetServImpl.getBranchMasterRes(req.getCompanyId(), req.getBranchCode());
			savedata.setBranchCode(req.getBranchCode());
			savedata.setBranchName(branchData.getBranchName());
			savedata.setApplicationId(StringUtils.isBlank(req.getApplicationId()) ? "1" : req.getApplicationId());

			// Source Type Search Condition
			if (filterSource.size() > 0 && directSource.contains(filterSource.get(0).getItemCode()) && !"User".equals(req.getUserType())) {

				String sourceType = filterSource.get(0).getItemValue();
				String subUserType = sourceType.contains("Broker") ? "Broker": sourceType.contains("Agent") ? "Agent" : "";
				LoginMaster premiaLogin = eserMotDetServImpl.getPremiaBroker(req.getBdmCode(), subUserType, req.getCompanyId());
				String premiaLoginId = premiaLogin != null ? premiaLogin.getLoginId() : "";

				LoginUserInfo premiaUser = loginUserRepo.findByLoginId(premiaLoginId);
				LoginUserInfo loginUserData = premiaUser != null ? premiaUser: loginUserRepo.findByLoginId(branchData.getDirectBrokerId());

				LoginBranchMaster premiaBranch = lbranchRepo.findByLoginIdAndBranchCodeAndCompanyId(premiaLoginId,req.getBranchCode(), req.getCompanyId());
				LoginBranchMaster brokerBranch = premiaBranch != null ? premiaBranch: lbranchRepo.findByLoginIdAndBranchCodeAndCompanyId(branchData.getDirectBrokerId(),req.getBranchCode(), req.getCompanyId());

				savedata.setBrokerCode(loginUserData.getOaCode());
				savedata.setAgencyCode(loginUserData.getAgencyCode());
				savedata.setLoginId(loginUserData.getLoginId());
				savedata.setCustomerCode(req.getBdmCode());
				savedata.setCustomerName(req.getCustomerName());
				savedata.setBdmCode(req.getBdmCode());
				savedata.setBdmName(req.getBdmName());	
				savedata.setBrokerBranchCode(brokerBranch.getBrokerBranchCode());
				savedata.setBrokerBranchName(brokerBranch.getBrokerBranchName());
				savedata.setSourceTypeId(filterSource.get(0).getItemCode());
				savedata.setSourceType(filterSource.get(0).getItemValue());

				
				//(premiaLogin != null && premiaUser != null && premiaBranch != null)
				// Direct Source Type
				if (filterSource.get(0).getItemValue().contains("Direct")) {
					savedata.setSalePointCode(brokerBranch.getSalePointCode());
					savedata.setBrokerTiraCode(loginUserData.getRegulatoryCode());
				} 
				else if (filterSource.get(0).getItemValue().contains("digital")) {
					savedata.setSalePointCode(brokerBranch.getSalePointCode());
					savedata.setBrokerTiraCode(loginUserData.getRegulatoryCode());
				}
				else {

					try {
						String brokerTiraCode = "";
						String brokerSpCode = "";
						PremiaTiraReq tiSPcode = new PremiaTiraReq();
						tiSPcode.setInsuranceId(req.getCompanyId());
						tiSPcode.setPremiaCode(req.getBdmCode());
						List<PremiaTiraRes> preRes = premiaBrokerService.searchPremiaBrokerTiraCodeAndSalePoint(tiSPcode);
						System.out.println("PremiaBrokerTiraCode ====> :"  +preRes);
						if (preRes.size() > 0 && preRes != null) {
							brokerTiraCode = preRes.get(0).getTiraCode();
							brokerSpCode = preRes.get(0).getSalePoint();
							savedata.setSalePointCode(brokerSpCode);
							savedata.setBrokerTiraCode(brokerTiraCode);
						}
						else
						{
							savedata.setSalePointCode(brokerSpCode);
							savedata.setBrokerTiraCode(brokerTiraCode);
						}
						
						// Broker Tira Code
//						PremiaTiraReq brokerTiraCodeReq = new PremiaTiraReq();
//						brokerTiraCodeReq.setInsuranceId(req.getCompanyId());
//						brokerTiraCodeReq.setPremiaCode(req.getCustomerCode());
//						List<PremiaTiraRes> brokerTira = premiaBrokerService.searchPremiaBrokerTiraCode(brokerTiraCodeReq);
//						String brokerTiraCode = "";
//						if (brokerTira.size() > 0 && brokerTira != null) {
//							brokerTiraCode = brokerTira.get(0).getTiraCode();
//						}
//
//						PremiaTiraReq brokerSpCodeReq = new PremiaTiraReq();
//						brokerSpCodeReq.setInsuranceId(req.getCompanyId());
//						brokerSpCodeReq.setPremiaCode(brokerTiraCode);
//						List<PremiaTiraRes> brokerSp = premiaBrokerService.searchPremiaBrokerSpCode(brokerSpCodeReq);
//						String brokerSpCode = "";
//						if (brokerSp.size() > 0) {
//							brokerSpCode = brokerSp.get(0).getTiraCode();
//						}
						
					} catch (Exception e) {
						e.printStackTrace();
						log.info("Log Details" + e.getMessage());

					}

				}
			} else {
				LoginUserInfo loginUserData = loginUserRepo.findByLoginId(req.getLoginId());
				LoginBranchMaster brokerBranch =lbranchRepo.findByLoginIdAndBrokerBranchCodeAndCompanyIdAndBranchCode( req.getLoginId(), req.getBrokerBranchCode(), req.getCompanyId(),req.getBranchCode());
				LoginMaster loginData = loginRepo.findByLoginId(req.getLoginId());
				savedata.setBrokerCode(loginData.getOaCode());
				savedata.setAgencyCode(loginData.getAgencyCode());
				savedata.setCustomerCode(loginUserData.getCustomerCode());
				savedata.setLoginId(req.getLoginId());
				savedata.setCustomerName(loginUserData.getCustomerName());
				savedata.setBdmCode(null);
				savedata.setBrokerBranchCode(brokerBranch.getBrokerBranchCode());
				savedata.setBrokerBranchName(brokerBranch.getBrokerBranchName());
				if("User".equalsIgnoreCase(loginData.getUserType()))
				{
					LoginMaster agency = loginRepo.findByAgencyCode(loginData.getAgencyCode());
					LoginUserInfo userIn = loginUserRepo.findByLoginId(agency.getLoginId());
					LoginBranchMaster broker =lbranchRepo.findByLoginIdAndBrokerBranchCodeAndCompanyIdAndBranchCode(agency.getLoginId(), req.getBrokerBranchCode(), req.getCompanyId(),req.getBranchCode());
					savedata.setSalePointCode(broker.getSalePointCode());
					savedata.setBrokerTiraCode(userIn.getRegulatoryCode());
					
				}
				else
				{
					savedata.setSalePointCode(brokerBranch.getSalePointCode());
					savedata.setBrokerTiraCode(loginUserData.getRegulatoryCode());
					
				}
				List<ListItemValue> filterBrokerSource = sourcerTypes.stream().filter(o -> o.getItemValue().equalsIgnoreCase(loginData.getSubUserType())).collect(Collectors.toList());
				savedata.setSourceTypeId(filterBrokerSource.size() > 0 ? filterBrokerSource.get(0).getItemCode() : "");
				savedata.setSourceType(filterBrokerSource.size() > 0 ? filterBrokerSource.get(0).getItemValue(): loginData.getSubUserType());
			}
			savedata.setAcExecutiveId(StringUtils.isBlank(req.getAcExecutiveId()) ? null : Integer.valueOf(req.getAcExecutiveId()));
			savedata.setSubUserType(savedata.getSourceType());

			if ("1".equalsIgnoreCase(req.getApplicationId())) {
				savedata.setSubUserType(savedata.getSourceType());
			} else {
				LoginMaster issuerData = loginRepo.findByLoginId(req.getApplicationId());
				savedata.setSubUserType(issuerData != null ? issuerData.getSubUserType() : savedata.getSourceType());
			}

			savedata.setFinalizeYn(StringUtils.isNotBlank(req.getFinalizeYn()) ? req.getFinalizeYn() : "N");

			savedata.setCarAlarmYn(StringUtils.isNotBlank(req.getCarAlarmYn()) ? req.getCarAlarmYn() : "N");

			// Kenya Fields
			// Default value
			String ncdYn = "N";

			// Override only if present
			if (StringUtils.isNotBlank(req.getNcdYn())) {
			    ncdYn = req.getNcdYn().equalsIgnoreCase("Y") ? "Y" : "N";
			}

			savedata.setNcdYn(ncdYn);

			// Derived field
			savedata.setClaimNum12m0m("Y".equals(ncdYn) ? 1 : 0);
			savedata.setClaimNum24m12m(0);
			savedata.setClaimNum36m24m(0);
			savedata.setVehicleClass(req.getVehicleClass());
			savedata.setPaymentFrequency(12);

			if ("SQ".equals(req.getSavedFrom())) {
				savedata.setIdNumber("11111");
				savedata.setFleetOwnerYn("N");

				String customerRefNo = eserMotDetServImpl.createCustomerForShortQuote(req);
				req.setCustomerReferenceNo(customerRefNo);
				savedata.setCustomerReferenceNo(customerRefNo);

			}

			{
				savedata.setSectionNameLocal(StringUtils.isBlank(sectionNameLocal)?"":sectionNameLocal);

				// Group 5: Parallelize localization lookups
				boolean hasModelId = req.getVehcileModelId() != null && req.getCompanyId() != null && req.getVehcileModelId().matches("^\\d+$");

				CompletableFuture<List<MotorMakeModelMaster>> motorModelLocalFuture = hasModelId
						? CompletableFuture.supplyAsync(() -> modelrepo.findByBranchCodeAndCompanyIdAndModelIdOrderByAmendIdDesc("99999", req.getCompanyId(), Integer.valueOf(req.getVehcileModelId())), motorLookupExecutor)
						: CompletableFuture.completedFuture(null);

				CompletableFuture<List<MotorMakeModelMaster>> motorMakeLocalFuture = CompletableFuture.supplyAsync(() ->
						modelrepo.findByBranchCodeAndCompanyIdAndMakeNameEnOrderByAmendIdDesc("99999", req.getCompanyId(), req.getVehicleMake()), motorLookupExecutor);

				CompletableFuture<List<MotorColorMaster>> motorColorLocalFuture = CompletableFuture.supplyAsync(() ->
						eserMotDetServImpl.findMotorColorMasters(req.getCompanyId(), req.getBranchCode(), req.getColor()), motorLookupExecutor);

				CompletableFuture<List<ListItemValue>> fuelUsageLocalFuture = CompletableFuture.supplyAsync(() ->
						eserMotDetServImpl.findEwayListItemValues(req.getCompanyId(), req.getBranchCode(), "FUEL_TYPE", req.getFuelType()), motorLookupExecutor);

				CompletableFuture<List<CompanyProductMaster>> productNamesFuture = CompletableFuture.supplyAsync(() ->
						companyProductMasterRepo.findByCompanyIdAndProductIdOrderByAmendIdDesc(req.getCompanyId(), Integer.valueOf(req.getProductId())), motorLookupExecutor);

				CompletableFuture.allOf(motorModelLocalFuture, motorMakeLocalFuture, motorColorLocalFuture, fuelUsageLocalFuture, productNamesFuture).join();

				List<MotorMakeModelMaster> motormodel = motorModelLocalFuture.join();
				savedata.setVehcileModelDescLocal((motormodel != null && motormodel.size() > 0) ? motormodel.get(0).getModelNameLocal(): req.getVehcileModel());

				List<MotorMakeModelMaster> motorMake = motorMakeLocalFuture.join();
				savedata.setVehicleMakeDescLocal((motorMake != null && motorMake.size() > 0) ? motorMake.get(0).getMakeNameLocal(): req.getVehicleMake());

				List<MotorColorMaster> motorColor = motorColorLocalFuture.join();
				savedata.setColorDescLocal((motorColor != null && motorColor.size() > 0) ? motorColor.get(0).getColorDescLocal(): req.getColor());

				List<ListItemValue> fuelUsage = fuelUsageLocalFuture.join();
				savedata.setFuelTypeDescLocal((fuelUsage != null && fuelUsage.size() > 0) ? fuelUsage.get(0).getItemValueLocal(): req.getFuelType());

				List<CompanyProductMaster> productNames = productNamesFuture.join();
				savedata.setProductNameLocal((productNames != null && productNames.size() > 0) ? productNames.get(0).getProductNameLocal(): req.getProductId());
			}

			savedata.setNewValue(req.getNewValue());
			savedata.setMarketValue(req.getMarketValue());
			savedata.setAggregatedValue(req.getAggregatedValue());
		//	savedata.setMunicipalityTraffic(req.getMunicipalityTraffic());
			savedata.setTransportHydro(req.getTransportHydro());
			savedata.setNoOfCards(req.getNumberOfCards());
			savedata.setLocationId(1);
			savedata.setCoverId(req.getCoverid()!=null?req.getCoverid():0);
			savedata.setCollateralId(req.getCollateralId());
			repo.saveAndFlush(savedata);
			

			// Section Insert
			if (StringUtils.isBlank(req.getSaveOrSubmit()) || (!"Save".equalsIgnoreCase(req.getSaveOrSubmit()))) {

				List<EserviceSectionDetails> sectionList = eserSecRepo.findByRequestReferenceNoAndRiskIdAndProductIdOrderBySectionIdAsc(refNo, savedata.getRiskId(),savedata.getProductId());
				if (sectionList != null && sectionList.size() > 0) {
					eserSecRepo.deleteAll(sectionList);
				}
				List<EserviceSectionDetails> secList = new ArrayList<EserviceSectionDetails>();
				for (String sec : req.getSectionId()) {
					EserviceSectionDetails secData = new EserviceSectionDetails();
					mapper.map(savedata, secData);

					section = eserMotDetServImpl.getProductSectionDropdown(req.getCompanyId(), req.getProductId(), sec);
					secData.setSectionId(sec);
					secData.setExchangeRate(savedata.getExchangeRate());
					secData.setCurrencyId(savedata.getCurrency());
					secData.setSectionName(section!=null?section.getSectionName():"");
					secData.setRiskId(savedata.getRiskId());
					secData.setRequestReferenceNo(refNo);
					secData.setUserOpt("N");
					secData.setProductType(section.getMotorYn());
					secData.setRiskId(savedata.getRiskId());
					secData.setLocationId(1);
					ListItemValue productTypeItm = eserMotDetServImpl.getListItem(req.getCompanyId(), req.getBranchCode(),"PRODUCT_CATEGORY", secData.getProductType());
					String productTypeDesc = productTypeItm.getItemValue();
					String productTypeDesc_other = productTypeItm.getItemValueLocal();
					secData.setProductTypeDesc(productTypeDesc);
					secData.setProductTypeDesc(productTypeDesc_other);
					secList.add(secData);
				}
				eserSecRepo.saveAllAndFlush(secList);

				// Eservice Driver Details Save
				if (req.getDriverDetails() != null) {
					motorDriDetails = eserMotDetServImpl.eserviceDirDetailsSave(req, refNo, vehId);
				}

				try {

					DepreciationReq depreciationReq = new DepreciationReq();
					if (null != savedata) {
						depreciationReq.setInflationSumInsured(savedata.getSumInsured() != null ? savedata.getSumInsured() : null);
						depreciationReq.setInflationPercentage(StringUtils.isNotBlank(savedata.getInflation())&& savedata.getInflation().matches("[0-9]+(\\.[0-9]+)?")? new BigDecimal(savedata.getInflation()): null);
						depreciationReq.setPurchaseDate(savedata.getPurchaseDate() != null ? savedata.getPurchaseDate() : null);
						depreciationReq.setRequestReferenceNo(savedata.getRequestReferenceNo() != null ? savedata.getRequestReferenceNo() : null);
						depreciationReq.setRiskId(savedata.getRiskId() != null ? savedata.getRiskId() : null);

						BigDecimal exchangeRates = StringUtils.isNotBlank(req.getExchangeRate())? new BigDecimal(req.getExchangeRate()): BigDecimal.ZERO;
						depreciationReq.setExchangeRate(exchangeRates);
					}
					eserMotDetServImpl.depreciationCalculation(depreciationReq);

				} catch (Exception e) {

					log.error("Exception Ocurs When Calling the Depreciation Calculatyion Api  " + e.getMessage());
					e.printStackTrace();
					// throw

				}

				List<OneTimeTableRes> otRes = null;
				try {
					// One Time Table Thread Call — use savedata directly instead of re-reading from DB
					OneTimeTableReq otReq = new OneTimeTableReq();

					otReq.setRequestReferenceNo(savedata.getRequestReferenceNo());
					otReq.setVehicleId(savedata.getRiskId());
					otReq.setLocationId(1);
					otReq.setAgencyCode(StringUtils.isBlank(oaCode) ? savedata.getAgencyCode() : oaCode);
					otReq.setBranchCode(savedata.getBranchCode());
					otReq.setInsuranceId(savedata.getCompanyId());
					otReq.setProductId(Integer.parseInt(savedata.getProductId()));
					otReq.setSectionId(Integer.parseInt(savedata.getSectionId()));
					otReq.setSectionIds(req.getSectionId());
					otReq.setMotorDetails(savedata);
					if (motorDriDetails != null) {
						otReq.setMotorDriverDetails(motorDriDetails);
					}
					otRes = otService.call_OT_Insert(otReq);

					// Thread.sleep(10000L);
				} catch (Exception e) {
					e.printStackTrace();
				}

				try {

					for (OneTimeTableRes sec : otRes) {
						EserviceMotorDetailsSaveRes resq = new EserviceMotorDetailsSaveRes();
						resq.setResponse("Updated Successfully");
						resq.setRequestReferenceNo(refNo);
						resq.setCustomerReferenceNo(req.getCustomerReferenceNo());
						resq.setVehicleId(req.getVehicleId());
						resq.setLocationId(sec.getLocationId());
						resq.setVdRefNo(sec.getVdRefNo());
						resq.setCdRefNo(sec.getCdRefNo());
						resq.setDdRefNo(sec.getDdRefNo());
						resq.setMsrefno(sec.getMsRefNo());
						resq.setInsuranceId(req.getCompanyId());
						resq.setCreatedBy(req.getCreatedBy());
						resq.setProductId(req.getProductId());
						resq.setSectionId(sec.getSectionId());
						resq.setVehicleId(sec.getVehicleId());

						res.add(resq);
					}

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			} else {
				EserviceMotorDetailsSaveRes resq = new EserviceMotorDetailsSaveRes();
				resq.setResponse("Updated Successfully");
				resq.setRequestReferenceNo(refNo);
				resq.setCustomerReferenceNo(req.getCustomerReferenceNo());
				resq.setVehicleId(req.getVehicleId());

				resq.setVdRefNo("");
				resq.setCdRefNo("");
				resq.setMsrefno("");
				resq.setInsuranceId(req.getCompanyId());
				resq.setCreatedBy(req.getCreatedBy());
				resq.setProductId(req.getProductId());
				resq.setSectionId((req.getSectionId()!=null &&! req.getSectionId().isEmpty() && req.getSectionId().size()>0) ? req.getSectionId().get(0): null);
				resq.setVehicleId(req.getVehicleId());
				res.add(resq);
			}

		}

		catch (Exception e) {
			e.printStackTrace();
			log.info("Log Details" + e.getMessage());
			return null;
		}
		return res;
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void deleteOldMotor(String refNo, Integer riskId) {
	    repo.deleteByRequestRefAndRiskId(refNo, riskId);
	}


	public EserviceMotorDetailsRes getMotorDetails(EserviceMotorDetailsGetReq req) {
		EserviceMotorDetailsRes res = new EserviceMotorDetailsRes();
		DozerBeanMapper mapper = new DozerBeanMapper();
		try {
			EserviceMotorDetails data =repo.findByRequestReferenceNoAndRiskId(req.getRequestReferenceNo(),Integer.valueOf(req.getVehicleId()));
			res = mapper.map(data, EserviceMotorDetailsRes.class);
			res.setSumInsured(data.getNonDepreciatedSi() != null ? data.getNonDepreciatedSi().doubleValue()	: data.getSumInsured() != null ? data.getSumInsured().doubleValue() : 0.0);
			res.setInflationSumInsured(data.getInflationSi() != null ? data.getInflationSi() : null);
			res.setVehicleId(data.getRiskId());
			res.setNoOfComprehensives(data.getNoOfCompehensives() != null ? data.getNoOfCompehensives().toString() : "");
			res.setQuoteNo(data.getQuoteNo() != null ? data.getQuoteNo() : "");
			res.setCustomerId(data.getCustomerId() != null ? data.getCustomerId() : "");
			res.setDriverYn(data.getDriverYn());
			res.setOwnerName(data.getOwnerName());
			res.setResOwnerName(data.getOwnerName());
			res.setCarAlarmYn(StringUtils.isNotBlank(data.getCarAlarmYn()) ? data.getCarAlarmYn() : "N");
			res.setVehicleClass(data.getVehicleClass());
			res.setClaimType(data.getNcdYears() == null ? null : data.getNcdYears().toString());
			res.setExcess(StringUtils.isBlank(data.getExcess()) ? null : data.getExcess());
			res.setExcessDesc(StringUtils.isBlank(data.getExcessDesc()) ? null : data.getExcessDesc());
			res.setTransportHydro(data.getTransportHydro());
			res.setNoOfCylinders(data.getNoOfCylinders());	
			res.setVehicleTypeDescLocal(data.getVehicleTypeDescLocal());			
			res.setVehicleMakeDescLocal(data.getVehicleMakeDescLocal());			
			res.setInsurancetype(data.getInsuranceType());
			String bodyId = "";
			res.setTiraCode(data.getBrokerTiraCode());	
			res.setSalePointCode(data.getSalePointCode());
			res.setNonELecAccessoriesSi(
				    data.getNonElecAccessoriesSi() != null 
				        ? data.getNonElecAccessoriesSi().doubleValue() 
				        : null
				);
			res.setNoOfClaims(data.getNoOfClaims());
			if (StringUtils.isNotBlank(data.getTiraBodyType())) {
				// Vehicle Type
				MotorBodyTypeMaster bodyTypeId = eserMotDetServImpl.getBodyTypeId(data.getCompanyId(), data.getBranchCode(),data.getTiraBodyType());
				bodyId = bodyTypeId.getBodyId().toString();
			}
			String motorUsageId = "";
			String useageDesc_other = "";
			if (StringUtils.isNotBlank(data.getTiraMotorUsage())){
				MotorVehicleUsageMaster motorUsage = eserMotDetServImpl.getMotorUsageId(data.getCompanyId(), data.getBranchCode(), null,data.getTiraMotorUsage());
                if(motorUsage!=null) {
                	motorUsageId = motorUsage.getVehicleUsageId().toString();
                	useageDesc_other = motorUsage.getVehicleUsageDescLocal();
                }
			}
			res.setMotorUsage(StringUtils.isBlank(data.getMotorUsage()) ? motorUsageId : data.getMotorUsage());
			res.setMotorUsageDesc(StringUtils.isBlank(data.getMotorUsageDesc()) ? data.getTiraMotorUsage(): data.getMotorUsageDesc());
			res.setVehicleType(StringUtils.isBlank(data.getVehicleType()) ? bodyId : data.getVehicleType());
			res.setVehicleTypeDesc(StringUtils.isBlank(data.getVehicleTypeDesc()) ? data.getTiraBodyType(): data.getVehicleTypeDesc());
			res.setVehicleMake(StringUtils.isBlank(data.getVehicleMakeId()) ? data.getVehicleMake() : data.getVehicleMakeId());
			res.setVehcileModel(StringUtils.isBlank(data.getVehicleModelId()) ? "" : data.getVehicleModelId());

			EserviceDriverDetails dri = eserDriverRepo.findByRequestReferenceNoAndRiskId(data.getRequestReferenceNo(),data.getRiskId());
			DriverDetailsGetRes driData = new DriverDetailsGetRes();
			if (dri != null) {

				driData = mapper.map(dri, DriverDetailsGetRes.class);
				driData.setDriverDob(dri.getDriverDob());
				driData.setDriverId(dri.getDriverId());
				driData.setDriverName(dri.getDriverName());
				driData.setDriverType(dri.getDriverType());
				driData.setDriverTypedesc(dri.getDriverTypedesc());
				driData.setLicenseNo(dri.getLicenseNumber());
				driData.setAreaGroup(dri.getAreaGroup().toString());
				driData.setCityId(dri.getCityId().toString());
				driData.setStateId(dri.getStateId().toString());
				driData.setSuburbId(dri.getSuburbId().toString());
					
			}
			res.setDriverDetails(driData);
			
			List<EserviceSectionDetails> sectionList = eserSecRepo.findByRequestReferenceNoAndRiskIdOrderBySectionIdAsc(req.getRequestReferenceNo(), Integer.valueOf(req.getVehicleId()));
			List<String> sectionIds = new ArrayList<String>();
			if (sectionList.size() > 0 && sectionList != null) {
				sectionIds = sectionList.stream().map(EserviceSectionDetails::getSectionId).collect(Collectors.toList());
			}
			 res.setSectionIds(sectionIds);
			 res.setHorsePower(data.getHorsePower() != null ? String.valueOf(data.getHorsePower()) : "");
			 
			 res.setCollateralCompanyAddress(data.getCollateralCompanyAddress());
			 res.setCollateralCompanyName(data.getCollateralCompanyName());
			 res.setPaCoverId(data.getPaCoverId());
			 res.setBankingDelegation(data.getBankingDelegation());
			 res.setLoanAmount(data.getLoanAmount());
			 res.setLoanEndDate(data.getLoanEndDate());
			 res.setLoanStartDate(data.getLoanStartDate());
			 res.setVehicleTypeIvr(data.getVehicleTypeIvr());
			 res.setClassType(data.getClassType()!=null?data.getClassType():"");
			 res.setImportYN(data.getImportYN());
			 res.setCollateralId(data.getCollateralId());
			 
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Log Details" + e.getMessage());
			return null;
		}
		return res;
	}

	public List<DriverDetailsRes> getDriverDetails(DriverDetailsGetReq req) {
		List<DriverDetailsRes> resList = new ArrayList<DriverDetailsRes>();
		DozerBeanMapper dozerMapper = new DozerBeanMapper();
		
		try {
			List<MotorDriverDetails> driverList = driverRepo.findByRequestReferenceNo(req.getRequestReferenceNo());
			Map<Integer, List<MotorDriverDetails>> groupByRiskId = driverList.stream()
					.collect(Collectors.groupingBy(MotorDriverDetails::getRiskId));

			for (Integer riskId : groupByRiskId.keySet()) {
				List<MotorDriverDetails> driList = groupByRiskId.get(riskId);
				driList.sort(Comparator.comparing(MotorDriverDetails::getDriverId));

				for (MotorDriverDetails dri : driList) {
					DriverDetailsRes driverRes = new DriverDetailsRes();
					dozerMapper.map(dri, driverRes);
					driverRes.setLicenseNo(dri.getIdNumber());
					
					driverRes.setDrivingLicensingAge(dri.getDrivingLicensingAge());
					driverRes.setSubscriber(dri.getSubscriber());
					driverRes.setCivility(dri.getCivility());
					driverRes.setPlaceIssue(dri.getPlaceIssue());
					driverRes.setCategoryCode(dri.getCategoryCode());	
					driverRes.setCategoryExDate(dri.getCategoryExDate());
					driverRes.setCategoryDate(dri.getCategoryDate());
					driverRes.setEmail(dri.getEmail());
					driverRes.setContactCode(dri.getContactCode());
					driverRes.setContact(dri.getContact());
					
					
					resList.add(driverRes);

				}

			}

		} catch (Exception e) {

			log.error(e);
			e.printStackTrace();
			return null;
		}
		return resList;
	}
	
	public  List<Date> getAllowedEndDates( Date PolicyEndDate, Date today) {
        List<Date> dates = new ArrayList<>();
        
        try {
        Calendar temp = Calendar.getInstance();
        temp.setTime(PolicyEndDate);

        while (!temp.getTime().before(today)) {  // temp >= today
            dates.add(temp.getTime());
            temp.add(Calendar.MONTH, -1);
        }

        return dates;
        }catch(Exception e) {
        	e.printStackTrace();
        	return Collections.emptyList();
        }
    }
	private Date truncateTime(Date date) {
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    cal.set(Calendar.HOUR_OF_DAY, 0);
	    cal.set(Calendar.MINUTE, 0);
	    cal.set(Calendar.SECOND, 0);
	    cal.set(Calendar.MILLISECOND, 0);
	    return cal.getTime();
	}
	
	private boolean isTrailerValue(String value) {

	    if (StringUtils.isBlank(value)) {
	        return false;
	    }

	    String normalized = value.trim()
	            .toUpperCase()
	            .replaceAll("\\s+", " ");

	    return normalized.contains("TRAILER")
	            || normalized.contains("TRAILERS")
	            || normalized.contains("FLAT BED")
	            || normalized.contains("FLATBED")
	            || normalized.contains("BOX BODY");
	}
	private boolean isTrailerVehicle(EserviceMotorDetailsSaveReq req) {

	    
	    if (isTrailerValue(req.getVehicleType())
	            || isTrailerValue(req.getVehcileModel())
	            || isTrailerValue(req.getVehicleMake())) {
	        return true;
	    }

	    if (StringUtils.isNotBlank(req.getVehicleType())) {
	        List<MotorBodyTypeMaster> bodyTypes =
	                bodyTypeMasterRepo.findTrailerBodyType(req.getVehicleType());

	        if (bodyTypes != null && !bodyTypes.isEmpty()) {
	            return true;
	        }
	    }

	    return false;
	}

}
