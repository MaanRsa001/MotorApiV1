package com.maan.eway.common.service.impl;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.eway.bean.LoginMaster;
import com.maan.eway.bean.ProductSectionMaster;
import com.maan.eway.common.req.NonMotEndtReq;
import com.maan.eway.common.req.NonMotorBrokerReq;
import com.maan.eway.common.req.NonMotorLocationReq;
import com.maan.eway.common.req.NonMotorPolicyReq;
import com.maan.eway.common.req.NonMotorSaveReq;
import com.maan.eway.common.req.NonMotorSectionReq;
import com.maan.eway.error.Error;
import com.maan.eway.repository.EServiceBuildingDetailsRepository;
import com.maan.eway.repository.EServiceSectionDetailsRepository;
import com.maan.eway.repository.EserviceCommonDetailsRepository;
import com.maan.eway.repository.LoginMasterRepository;
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
public class TanzaniaEserviceNonMotorDetails {

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
	private EserviceSlideValidateServiceImpl eserviceSlideValidationImpl;

	private Logger log = LogManager.getLogger(EserviceBuildingDetailsServiceImpl.class);

	public boolean idValidation(String Id, List<String> ids) {
		boolean error = false;
		try {
			List<String> id = ids.stream().filter(o -> o.equalsIgnoreCase(Id)).collect(Collectors.toList());
			if (id.size() > 0) {
				error = true;
			} else {
				ids.add(Id);
				System.out.println("IDs : "+ids);
			}
		} catch (Exception e) {

			log.error(e);
			e.printStackTrace();
		}
		return error;
	}
	public List<String> validateNonMotorDetails(NonMotorSaveReq req) {
		List<String> error = new ArrayList<String>();
		try {
			// Common Validation Policy Details and Broker Details
			error = commonValidation(req,error);
			// Endorsement Details
			error = endtValidation(req,error);
			// Location Wise Validation
			error = locationSectionValidation(req,error);
		} catch (Exception e) {

			log.error(e);
			e.printStackTrace();
			error.add("1224");
			// error.add(new Error("19", "Common Error", e.getMessage()));
		}
		return error;

	}

	private List<String> locationSectionValidation(NonMotorSaveReq req,List<String> error) {
		NonMotorPolicyReq policyReq = req.getNonMotorPolicyReq();
		NonMotorBrokerReq brokerReq = req.getNonMotorBrokerReq();
		NonMotEndtReq endtReq = req.getNonMotEndtReq();
		List<NonMotorLocationReq> locationReq = req.getLocationList();

		try {

			List<ProductSectionMaster> getProductSection = getProductSection(policyReq.getCompanyId(),
					Integer.valueOf(policyReq.getProductId()));

			System.out.println("********************Location Wise Validation Loop starts*******************");
			if (locationReq != null && locationReq.size() > 0) {

				for (NonMotorLocationReq locationdata : locationReq) {
					List<NonMotorSectionReq> sectionReq = locationdata.getSectionList();
					Integer locId = Integer.valueOf(locationdata.getLocationId());
					
					// Location Checking block **************************************
					if (StringUtils.isBlank(locationdata.getLocationId())) {
						error.add("2294" + "," + locId);
					} else if (StringUtils.isBlank(locationdata.getLocationName())) {
						error.add("2269" + "," + locId);
					} else if (locationdata.getLocationName().length() > 200) {
						error.add("2271" + "," + locId);
					} else {
//						if (StringUtils.isBlank(locationdata.getCoversRequiredYn())) {
//							error.add("2269" + "," + locId);
//						}
//						if (StringUtils.isBlank(locationdata.getBuildingOwnerYn())) {
//							error.add("1544" + "," + locId);
//						}
						if (sectionReq != null) {
							
							// Section Checking Block ***************************************
							Map<String, List<NonMotorSectionReq>> sectionIds = sectionReq.stream()
									.filter(o -> o.getSectionId() != null)
									.collect(Collectors.groupingBy(NonMotorSectionReq::getSectionId));

							System.out.println("Section Id " + sectionIds);
							if (sectionIds.isEmpty()) {
								error.add("2296");
							}else { 
							for (String group : sectionIds.keySet()) {
								List<NonMotorSectionReq> fiterData = sectionReq.stream()
										.filter(o -> o.getSectionId().equalsIgnoreCase(group.toString()))
										.collect(Collectors.toList());
								List<String> ids=new ArrayList<>();
								for (NonMotorSectionReq dd : fiterData) {
									System.out.println("LocationId : " + locId);
									System.out.println("SectionId : " + dd.getSectionId());
									if (StringUtils.isBlank(dd.getSectionId())) {
										error.add("2296");
									} else {
										List<ProductSectionMaster> sectionData = getProductSection.stream().filter(
												o -> o.getSectionId().equals(Integer.valueOf(dd.getSectionId())))
												.collect(Collectors.toList());

										if (sectionData == null || sectionData.isEmpty() || sectionData.size() <= 0) {
											error.add("2328");
										}else {
											//Section List Checking Block ****************************************************
											error=sectionListValidation(dd,locId,ids,error);
										}
										
									}

								}

							}
						}
						}else {
							error.add("2293");
						}

					}
				}

			} else {
				error.add("2292");
			}

		} catch (Exception e) {

			log.error(e);
			e.printStackTrace();
			error.add("1224");
		}
		return error;

	}

	private List<String> sectionListValidation(NonMotorSectionReq dd,Integer locId,List<String> ids,List<String> error) {
		try {
			//Common Validation
			if(!"42".equalsIgnoreCase(dd.getSectionId())) {
				if (StringUtils.isBlank(dd.getSectionName())) {
					error.add("2297"+","+locId);
				}
				if (StringUtils.isBlank(dd.getSumInsured())) {
					error.add("2264"+","+locId);
				} else if (!dd.getSumInsured().matches("[0-9.]+")) {
					error.add("1547"+","+locId);
				}

//				if (StringUtils.isBlank(dd.getDescriptionOfRisk())) {
//					error.add("2268"+","+locId);
//				} else if (dd.getDescriptionOfRisk().length() > 1000) {
//					error.add("2267"+","+locId);
//				}
//				if (StringUtils.isBlank(dd.getCoveringDetails())) {
//					error.add("2278"+","+locId);
//				} else if (dd.getCoveringDetails().length() > 1000) {
//					error.add("2279"+","+locId);
//				}

			}
			//Section Wise Mandatory Field
//			if("1".equalsIgnoreCase(dd.getSectionId())) {
//				error=building(dd,locId,ids,error);
//			}else if("40".equalsIgnoreCase(dd.getSectionId())) {
//				error=fireAndAlliedPerils(dd,locId,error,ids);
//			}else if("41".equalsIgnoreCase(dd.getSectionId())) {
//				error=machineryBreakDown(dd,locId,ids,error);
//			}else if("42".equalsIgnoreCase(dd.getSectionId())) {
//				error=money(dd,locId,error);
//			}else if("52".equalsIgnoreCase(dd.getSectionId())) {
//				error=burglary(dd,locId,error);
//			}else if("76".equalsIgnoreCase(dd.getSectionId())) {
//				error=electeonicEquipment(dd,locId,error,ids);
//			}else if("43".equalsIgnoreCase(dd.getSectionId())) {
//				error=fiedlity(dd,locId,error,ids);
//			}else if("45".equalsIgnoreCase(dd.getSectionId())) {
//				error=workmenCompensation(dd,locId,error,ids);
//			}else if("182".equalsIgnoreCase(dd.getSectionId())) {
//				error=groupPersonalAccident(dd,locId,error,ids);
//			}else if("54".equalsIgnoreCase(dd.getSectionId())) {
//				error=liabiliyOrPublicLiability(dd,locId,error,ids);
//			}else if("35".equalsIgnoreCase(dd.getSectionId())) {
//				error=personalAccident(dd,locId,error,ids);
//			}else if("117".equalsIgnoreCase(dd.getSectionId())) {
//				error=bond(dd,locId,error,ids);
//			}
//			
			
		}catch (Exception e) {

			log.error(e);
			e.printStackTrace();
			error.add("1224");
			// error.add(new Error("19", "Common Error", e.getMessage()));
		}
		return error;
	}

	private List<String> bond(NonMotorSectionReq dd, Integer locId, List<String> error, List<String> ids) {
		try {
			if (StringUtils.isBlank(dd.getBondType())) {
				error.add("2262"+","+locId);
			}
			//Id Validation
//			} else {
//				boolean e = idValidation(dd.getBondType(), ids);
//				if (e)
//					error.add("2298" + "," + locId);
//			}
			
			if (StringUtils.isBlank(dd.getBondYear())) {
				error.add("2263"+","+locId);
			} 

	} catch (Exception e) {

		log.error(e);
		e.printStackTrace();
		error.add("1224");
	}
	return error;

	}
	private List<String> workmenCompensation(NonMotorSectionReq dd, Integer locId, List<String> error,
			List<String> ids) {
		try {
			if (StringUtils.isBlank(dd.getOccupationId())) {
				error.add("2280" + "," + locId);
			} else {
				boolean e = idValidation(dd.getOccupationId(), ids);
				if (e)
					error.add("2298" + "," + locId);
			}
			
			if (StringUtils.isBlank(dd.getOccupationDesc())) {
				error.add("2315"+","+locId);
			}
			
			if (StringUtils.isBlank(dd.getCount())) {
				error.add("2320"+","+locId);
			} else if (!dd.getCount().matches("[0-9.]+")) {
				error.add("2321"+","+locId);
			}else  if(Integer.valueOf(dd.getCount())<=0) {
				error.add("2321"+","+locId);
			}

	} catch (Exception e) {

		log.error(e);
		e.printStackTrace();
		error.add("1224");
	}
	return error;

	}
	private List<String> personalAccident(NonMotorSectionReq dd, Integer locId, List<String> error, List<String> ids) {
		try {
			if (StringUtils.isBlank(dd.getOccupationId())) {
				error.add("2280" + "," + locId);
			} else {
				boolean e = idValidation(dd.getOccupationId(), ids);
				if (e)
					error.add("2298" + "," + locId);
			}
			if (StringUtils.isBlank(dd.getOccupationDesc())) {
				error.add("2315"+","+locId);
			}
		}catch (Exception e) {

			log.error(e);
			e.printStackTrace();
			error.add("1224");
			// error.add(new Error("19", "Common Error", e.getMessage()));
		}
		return error;
	}
	private List<String> building(NonMotorSectionReq dd, Integer locId, List<String> ids, List<String> error) {
		try {
				if (StringUtils.isBlank(dd.getWallType())) {
					error.add("2326"+","+locId);
				}
				if (StringUtils.isBlank(dd.getRoofType())) {
					error.add("2327"+","+locId);
				}
				
		}catch (Exception e) {

			log.error(e);
			e.printStackTrace();
			error.add("1224");
			// error.add(new Error("19", "Common Error", e.getMessage()));
		}
		return error;

	}
	private List<String> liabiliyOrPublicLiability(NonMotorSectionReq dd, Integer locId, List<String> error,
			List<String> ids) {
		try {
			if (StringUtils.isBlank(dd.getCategoryId())) {
				error.add("2324" + "," + locId);
			} else {
				boolean e = idValidation(dd.getCategoryId(), ids);
				if (e)
					error.add("2298" + "," + locId);
			}
			
			if (StringUtils.isBlank(dd.getCategoryDesc())) {
				error.add("2325"+","+locId);
			}
			
	} catch (Exception e) {

		log.error(e);
		e.printStackTrace();
		error.add("1224");
	}
	return error;
	}
	private List<String> fiedlity(NonMotorSectionReq dd, Integer locId, List<String> error, List<String> ids) {
		try {
			if (StringUtils.isBlank(dd.getOccupationId())) {
				error.add("2280" + "," + locId);
			} else {
				boolean e = idValidation(dd.getOccupationId(), ids);
				if (e)
					error.add("2298" + "," + locId);
			}
			
			if (StringUtils.isBlank(dd.getOccupationDesc())) {
				error.add("2315"+","+locId);
			}
			
			if (StringUtils.isBlank(dd.getCount())) {
				error.add("2320"+","+locId);
			} else if (!dd.getCount().matches("[0-9.]+")) {
				error.add("2321"+","+locId);
			}else  if(Integer.valueOf(dd.getCount())<=0) {
				error.add("2321"+","+locId);
			}

	} catch (Exception e) {

		log.error(e);
		e.printStackTrace();
		error.add("1224");
	}
	return error;

	}
	private List<String> groupPersonalAccident(NonMotorSectionReq dd, Integer locId, List<String> error, List<String> ids) {
		try {
			if (StringUtils.isBlank(dd.getOccupationId())) {
				error.add("2280" + "," + locId);
			} else {
				boolean e = idValidation(dd.getOccupationId(), ids);
				if (e)
					error.add("2298" + "," + locId);
			}
			if (StringUtils.isBlank(dd.getOccupationDesc())) {
				error.add("2315"+","+locId);
			}
			
			if (StringUtils.isBlank(dd.getIndemnityType())) {
				error.add("2318" + "," + locId);
			}
			if (StringUtils.isBlank(dd.getIndemnityTypeDesc())) {
				error.add("2319" + "," + locId);
			}
			
			if (StringUtils.isBlank(dd.getCount())) {
				error.add("2322"+","+locId);
			} else if (!dd.getCount().matches("[0-9.]+")) {
				error.add("2323"+","+locId);
			}else  if(Integer.valueOf(dd.getCount())<=0) {
				error.add("2323"+","+locId);
			}

	} catch (Exception e) {

		log.error(e);
		e.printStackTrace();
		error.add("1224");
	}
	return error;

	}

	private List<String> electeonicEquipment(NonMotorSectionReq dd, Integer locId, List<String> error, List<String> ids) {
		try {
				if (StringUtils.isBlank(dd.getContentId())) {
					error.add("2316" + "," + locId);
				} else {
					boolean e = idValidation(dd.getContentId(), ids);
					if (e)
						error.add("2298" + "," + locId);
				}

		} catch (Exception e) {

			log.error(e);
			e.printStackTrace();
			error.add("1224");
		}
		return error;

	}

	private List<String> burglary(NonMotorSectionReq dd, Integer locId, List<String> error) {
		try {
			if (StringUtils.isBlank(dd.getFirstLossPercentId())) {
				error.add("2314" + "," + locId);
			}

		} catch (Exception e) {

			log.error(e);
			e.printStackTrace();
			error.add("1224");
		}
		return error;

	}
	private List<String> money(NonMotorSectionReq dd, Integer locId,List<String> error) {
//		try {
//			
//			if ("41".equalsIgnoreCase(dd.getSectionId())) {
//				if ((StringUtils.isBlank(dd.getMoneyAnnualEstimate()) || "0.0".equalsIgnoreCase(dd.getMoneyAnnualEstimate())||"0".equalsIgnoreCase(dd.getMoneyAnnualEstimate()))
//						&& (StringUtils.isBlank(dd.getMoneyCollector()) ||"0.0".equalsIgnoreCase(dd.getMoneyCollector())||"0".equalsIgnoreCase(dd.getMoneyCollector()))
//						&& (StringUtils.isBlank(dd.getMoneyDirectorResidence()) || "0".equalsIgnoreCase(dd.getMoneyDirectorResidence())||"0.0".equalsIgnoreCase(dd.getMoneyDirectorResidence()))
//						&& (StringUtils.isBlank(dd.getMoneyOutofSafe()) || "0".equalsIgnoreCase(dd.getMoneyOutofSafe())||"0.0".equalsIgnoreCase(dd.getMoneyOutofSafe()))
//						&& (StringUtils.isBlank(dd.getMoneySafeLimit())||"0.0".equalsIgnoreCase(dd.getMoneySafeLimit())||"0".equalsIgnoreCase(dd.getMoneySafeLimit()))
//						&& (StringUtils.isBlank(dd.getMoneyMajorLoss())||"0.0".equalsIgnoreCase(dd.getMoneyMajorLoss())||"0".equalsIgnoreCase(dd.getMoneyMajorLoss()))
//						&& (StringUtils.isBlank(dd.getStrongroomSi())||"0.0".equalsIgnoreCase(dd.getStrongroomSi())||"0".equalsIgnoreCase(dd.getStrongroomSi())))
//				{
//					error.add("2299");
//				}
//				
//				if (StringUtils.isNotBlank(dd.getMoneyAnnualEstimate())) {
//					 if (!dd.getMoneyAnnualEstimate().matches("[0-9.]+")) {
//						 error.add("2300");
//					}
//				}
//				
//				
//				if (StringUtils.isNotBlank(dd.getMoneyCollector())) {
//					 if (!dd.getMoneyCollector().matches("[0-9.]+")) {
//						 error.add("2301");
//					 }
//				}
//				
//				if (StringUtils.isNotBlank(dd.getMoneyDirectorResidence())) {
//					 if (!dd.getMoneyDirectorResidence().matches("[0-9.]+")) {
//						 error.add("1458");
//						// error.add(new Error("41", "Residence Of Director",	"Please Enter Valid Number In Residence Of Director"));
//					 }
//				}
//				
//				if (StringUtils.isNotBlank(dd.getMoneyOutofSafe())) {
//					 if (!dd.getMoneyOutofSafe().matches("[0-9.]+")) {
//						 error.add("1459");
//						 //error.add(new Error("41", "Money out of safe ",	"Please Enter Valid Number In Money out of safe "));
//					 }
//				}
//				
//				
//				if (StringUtils.isNotBlank(dd.getMoneySafeLimit())) {
//					 if (!dd.getMoneySafeLimit().matches("[0-9.]+")) {
//						 error.add("1460");
//						 //error.add(new Error("41", "Damage to Safe Limit",	"Please Enter Valid Number In Damage to Safe Limit"));
//					 }
//				}
//				
//				if (StringUtils.isNotBlank(dd.getMoneyMajorLoss())) {
//					 if (!dd.getMoneyMajorLoss().matches("[0-9.]+")) {
//						 error.add("1461");
//						// error.add(new Error("41", "Major Loss Limit ",	"Please Enter Valid Number In Major Loss Limit "));
//					 }
//				}
//				if (StringUtils.isNotBlank(dd.getStrongroomSi())) {
//					 if (!dd.getStrongroomSi().matches("[0-9.]+")) {
//						 error.add("1463");
//						// error.add(new Error("41", "Strong Room",	"Please Enter Valid Number In Strong Room Suminsured"));
//					 }
//				} 
//				
//			} 
//		}catch (Exception e) {
//
//			log.error(e);
//			e.printStackTrace();
//			error.add("1224");
//		}
		return error;
	}

	

	private List<String> machineryBreakDown(NonMotorSectionReq dd, Integer locId,List<String> ids,List<String> error) {
		try {
			
			if (StringUtils.isBlank(dd.getContentId())) {
				error.add("2317" + "," + locId);
			} else {
				boolean e = idValidation(dd.getContentId(), ids);
				if (e)
					error.add("2298" + "," + locId);
			}

		} catch (Exception e) {

			log.error(e);
			e.printStackTrace();
			error.add("1224");
		}
		return error;
	}

	private List<String> fireAndAlliedPerils(NonMotorSectionReq dd,Integer locId,List<String> error,List<String> ids) {
		try {
				if (StringUtils.isBlank(dd.getBuildingUsageId())) {
					error.add("1540"+","+locId);
				}else {
					List<String> id =  ids.stream().filter( o -> o.equalsIgnoreCase(dd.getBuildingUsageId()) ).collect(Collectors.toList()); 
					if(id.size()>0  ) {
						error.add("2298"+","+locId);
					} else {
						ids.add(dd.getBuildingUsageId());
					}
				}
				if (StringUtils.isBlank(dd.getWallType())) {
					error.add("2326"+","+locId);
				}
				if (StringUtils.isBlank(dd.getRoofType())) {
					error.add("2327"+","+locId);
				}
		}catch (Exception e) {

			log.error(e);
			e.printStackTrace();
			error.add("1224");
			// error.add(new Error("19", "Common Error", e.getMessage()));
		}
		return error;
	}

	private List<String> endtValidation(NonMotorSaveReq req,List<String> error) {
		NonMotorPolicyReq policyReq = req.getNonMotorPolicyReq();
		NonMotorBrokerReq brokerReq = req.getNonMotorBrokerReq();
		NonMotEndtReq endtReq = req.getNonMotEndtReq();
		List<NonMotorLocationReq> locationReq = req.getLocationList();

		try {

			System.out.println("********************Endorsement Validation starts*******************");
			if (endtReq != null) {
				
				if (endtReq.getEndorsementType()!=null && endtReq.getEndorsementType() != 0) {

				} else {
					System.out.println("No Endorsement");
				}

			} else {
				System.out.println("No Endorsement");
			}
		} catch (Exception e) {

			log.error(e);
			e.printStackTrace();
			error.add("1224");
			// error.add(new Error("19", "Common Error", e.getMessage()));
		}
		return error;

	}

	private List<String> commonValidation(NonMotorSaveReq req,List<String> error) {
		NonMotorPolicyReq policyReq = req.getNonMotorPolicyReq();
		NonMotorBrokerReq brokerReq = req.getNonMotorBrokerReq();
		NonMotEndtReq endtReq = req.getNonMotEndtReq();

		try {
			if (policyReq != null && brokerReq != null) {
				System.out.println("********************Common  Validation starts*******************");
				if (StringUtils.isBlank(policyReq.getCustomerReferenceNo())) {
					error.add("1569");
				}

				if (StringUtils.isBlank(policyReq.getBranchCode())) {
					error.add("1570");
					// error.add(new Error("01", "BranchCode", "Please Enter BranchCode "));
				} else if (policyReq.getBranchCode().length() > 20) {
					error.add("2305");
					// error.add(new Error("01", "Branch Code", "Please Enter Branch Code within 20
					// Characters"));
				}

				if (StringUtils.isBlank(policyReq.getProductId())) {
					error.add("1565");
					// error.add(new Error("03", "Product Id", "Please Enter ProductId "));
				} else if (policyReq.getProductId().length() > 20) {
					error.add("1564");
					// error.add(new Error("03", "Product Id", "Please Enter Product Id within 20
					// Characters"));
				}

				if (StringUtils.isBlank(policyReq.getCompanyId())) {
					error.add("2306");
					// error.add(new Error("05", "CompanyId", "Please Enter CompanyId "));
				} else if (policyReq.getCompanyId().length() > 20) {
					error.add("2307");
					// error.add(new Error("05", "CompanyId", "Please Enter CompanyId within 20
					// Characters"));
				}

				if (StringUtils.isBlank(policyReq.getCurrency())) {
					error.add("2303");
					// error.add(new Error("10", "Currency", "Please Select Currency"));
				}
				if (StringUtils.isBlank(policyReq.getExchangeRate())) {
					error.add("2289");
					// error.add(new Error("11", "ExchangeRate", "Please Enter ExchangeRate"));
				} else if (Double.valueOf(policyReq.getExchangeRate()) <= 0D) {
					error.add("2329");
				} else {
					Tuple minMax = eserviceSlideValidationImpl.getMinMaxRate(policyReq.getCurrency(),
							policyReq.getCompanyId());
					if (minMax != null) {

						Double exRate = Double.valueOf(
								minMax.get("exchangeRate") == null ? "0" : minMax.get("exchangeRate").toString());
						Double minRate = Double.valueOf(
								minMax.get("minDiscount") == null ? "0" : minMax.get("minDiscount").toString());
						Double maxRate = Double
								.valueOf(minMax.get("maxLoading") == null ? "0" : minMax.get("maxLoading").toString());
						minRate = exRate - (exRate * minRate / 100);
						maxRate = exRate + (exRate * maxRate / 100);
						if (Double.valueOf(policyReq.getExchangeRate()) <= minRate
								|| Double.valueOf(policyReq.getExchangeRate()) >= maxRate) {
							error.add("2304"+","+policyReq.getCurrency());
						}
					}
				}

				if (StringUtils.isBlank(policyReq.getHavepromocode())) {
					error.add("2308");
					// error.add(new Error("46", "Havepromocode", "Please Enter Havepromocode"));
				}
				if ((StringUtils.isNotBlank(policyReq.getHavepromocode()))
						&& policyReq.getHavepromocode().equalsIgnoreCase("Y")) {
					if (StringUtils.isBlank(policyReq.getPromocode())) {
						error.add("2309");
						// error.add(new Error("47", "Promocode", "Please Enter Promocode"));
					}
				}

				String status = StringUtils.isBlank(policyReq.getStatus()) ? "Y" : policyReq.getStatus();
				System.out.println("********************Policy Date Validation *******************");

				if (policyReq.getPolicyStartDate() == null) {
					error.add("1566");
					// error.add(new Error("13", "PolicyStartDate", "Please Enter
					// PolicyStartDate"));
				} else if ((endtReq.getEndorsementType() == null || endtReq.getEndorsementType().equals(0))
						&& !"RQ".equalsIgnoreCase(status)) {
					int before = eserviceSlideValidationImpl.getBackDays(policyReq.getCompanyId(),
							policyReq.getProductId(), brokerReq.getLoginId());
					int days = before == 0 ? -1 : -before;
					long MILLS_IN_A_DAY = 1000 * 60 * 60 * 24;
					long backDays = MILLS_IN_A_DAY * days;
					Date today = new Date();
					Date resticDate = new Date(today.getTime() + backDays);
					long days90 = MILLS_IN_A_DAY * 90;
					Date after90 = new Date(today.getTime() + days90);
					if (policyReq.getPolicyStartDate().before(resticDate)) {
						error.add("1572"+","+before);
						// error.add(new Error("14", "PolicyStartDate", "Policy Start Date Before " +
						// before + " Days Not Allowed "));
					} else if (policyReq.getPolicyStartDate().after(after90)) {
						error.add("1573");
						// error.add(new Error("14", "PolicyStartDate", "PolicyStartDate even after 90
						// days Not Allowed"));
					}

				}

				if (policyReq.getPolicyEndDate() == null) {
					error.add("1571");
					// error.add(new Error("14", "PolicyEndDate", "Please Enter PolicyEndDate"));

				} else if (policyReq.getPolicyStartDate() != null && policyReq.getPolicyEndDate() != null
						&& endtReq.getEndorsementType() == null) {
					if (policyReq.getPolicyEndDate().equals(policyReq.getPolicyStartDate())
							|| policyReq.getPolicyEndDate().before(policyReq.getPolicyStartDate())) {
						error.add("2290");
						// error.add(new Error("14", "PolicyEndDate", "PolicyEndDate Before
						// PolicyStartDate Not Allowed"));
					}
				}

				// Source Validation
				// Source Type Search Condition
				System.out.println("********************Source Validation *******************");
				List<String> directSource = new ArrayList<String>();
				directSource.add("1");
				directSource.add("2");
				directSource.add("3");

				if (brokerReq.getUserType().equalsIgnoreCase("Issuer")
						&& StringUtils.isBlank(brokerReq.getSourceTypeId())) {
					error.add("2310");
					// error.add(new Error("10", "BdmCode", "Please Select Source Type"));

				} else if (StringUtils.isNotBlank(brokerReq.getSourceTypeId())
						&& directSource.contains(brokerReq.getSourceTypeId())) {
					if (StringUtils.isBlank(brokerReq.getBdmCode())) {
						error.add("2310");
						// error.add(new Error("10", "BdmCode", "Please Select Source Code"));
					}
					if (StringUtils.isBlank(brokerReq.getCustomerName())) {
						error.add("2291");
						// error.add(new Error("10", "CustomerName", "Please Select Customer Name"));
					}

				} else {
					if (StringUtils.isBlank(brokerReq.getLoginId())) {
						error.add("2311");
						// error.add(new Error("10", "Login ID", "Please Select login Id"));
					} else {
						if (StringUtils.isNotBlank(policyReq.getCreatedBy())) {
						if (StringUtils.isBlank(policyReq.getCreatedBy())) {
							LoginMaster loginData = loginRepo.findByLoginId(policyReq.getCreatedBy());
							if (loginData.getSubUserType().equalsIgnoreCase("bank")) {
								if (StringUtils.isBlank(policyReq.getAcExecutiveId())) {
									error.add("2312");
									// error.add(new Error("01", "AcExecutiveId", "Please Select AcExecutiveId"));
								}
							}
						}
					}
					}

					if (StringUtils.isBlank(brokerReq.getBrokerBranchCode())) {
						error.add("2313");
						// error.add(new Error("10", "BrokerBranchCode", "Please Enter
						// BrokerBranchCode"));
					}

				}

			} else {
				error.add("2293");
				// error.add(new Error("10", "Policy Request is null", "Please Enter
			}

		} catch (Exception e) {

			log.error(e);
			e.printStackTrace();
			error.add("1551");
			// error.add(new Error("19", "Common Error", e.getMessage()));
		}
		return error;

	}

	public List<ProductSectionMaster> getProductSection(String insuranceId, Integer productId) {
		List<ProductSectionMaster> list = new ArrayList<ProductSectionMaster>();
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
			orderList.add(cb.desc(c.get("amendId")));

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
			jakarta.persistence.criteria.Predicate a5 = cb.equal(c.get("sectionId"), ocpm2.get("sectionId"));
			Predicate a7 = cb.equal(c.get("companyId"), ocpm2.get("companyId"));
			Predicate a8 = cb.equal(c.get("productId"), ocpm2.get("productId"));

			jakarta.persistence.criteria.Predicate a6 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"),
					todayEnd);
			effectiveDate2.where(a5, a6, a7, a8);

			// Where
			jakarta.persistence.criteria.Predicate n1 = cb.equal(c.get("status"), "Y");
			jakarta.persistence.criteria.Predicate n2 = cb.equal(c.get("effectiveDateStart"), effectiveDate);
			jakarta.persistence.criteria.Predicate n3 = cb.equal(c.get("effectiveDateEnd"), effectiveDate2);
			jakarta.persistence.criteria.Predicate n4 = cb.equal(c.get("companyId"), insuranceId);
			jakarta.persistence.criteria.Predicate n5 = cb.equal(c.get("productId"), productId);
			Predicate n6 = cb.equal(c.get("status"), "R");
			Predicate n7 = cb.or(n1, n6);
			query.where(n7, n2, n3, n4, n5).orderBy(orderList);

			// Get Result
			TypedQuery<ProductSectionMaster> result = em.createQuery(query);
			list = result.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null;
		}
		return list;
	}
}
