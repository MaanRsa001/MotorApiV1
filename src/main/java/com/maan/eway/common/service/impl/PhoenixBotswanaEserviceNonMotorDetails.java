package com.maan.eway.common.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.eway.bean.LoginMaster;
import com.maan.eway.bean.ProductSectionMaster;
import com.maan.eway.bean.SectionCoverMaster;
import com.maan.eway.common.req.NonMotEndtReq;
import com.maan.eway.common.req.NonMotorBrokerReq;
import com.maan.eway.common.req.NonMotorLocationReq;
import com.maan.eway.common.req.NonMotorPolicyReq;
import com.maan.eway.common.req.NonMotorSaveReq;
import com.maan.eway.common.req.NonMotorSectionReq;
import com.maan.eway.repository.EServiceBuildingDetailsRepository;
import com.maan.eway.repository.EServiceSectionDetailsRepository;
import com.maan.eway.repository.EserviceCommonDetailsRepository;
import com.maan.eway.repository.LoginMasterRepository;
import com.maan.eway.repository.ProductEmployeesDetailsRepository;
import com.maan.eway.repository.ProductSectionMasterRepository;
import com.maan.eway.repository.SectionCoverMasterRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;

@Service
public class PhoenixBotswanaEserviceNonMotorDetails {
	
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
	
	@Autowired
	private ProductSectionMasterRepository productSectionMasterRepository;
	
	@Autowired
	private ProductSectionMasterRepository productRepo;
	
	@Autowired
	private SectionCoverMasterRepository sectionRepo;
	
	
	private Logger log = LogManager.getLogger(EserviceBuildingDetailsServiceImpl.class);
	
	public List<String> validateNonMotorDetails(NonMotorSaveReq req){
		List<String> errorList=new ArrayList<String>();
		try {
			
			// Common Validation Policy Details and Broker Details
			sectionValidation(req,errorList);
			commonValidation(req,errorList);
			// Endorsement Details
			endtValidation(req,errorList);
			// Location Wise Validation
			locationSectionValidation(req,errorList);
	        
	    } catch (Exception e) {
	        log.error(e);
	        errorList.add("1551"); 
	    }

	    return errorList;
	}
	private List<String> sectionValidation(NonMotorSaveReq req, List<String> error) {
		try {
			NonMotorPolicyReq policyReq = req.getNonMotorPolicyReq();
			List<NonMotorLocationReq> locationList = req.getLocationList();
			List<NonMotorSectionReq> sectionList = locationList.get(0).getSectionList();
			String product = policyReq.getProductId();
			Integer productId = Integer.valueOf(product);
			String companyId = policyReq.getCompanyId();
			Map<String, List<NonMotorSectionReq>> groupByGroupId = sectionList.stream()
					.filter(o -> o.getSectionId() != null)
					.collect(Collectors.groupingBy(NonMotorSectionReq::getSectionId));
			for (Map.Entry<String, List<NonMotorSectionReq>> entry : groupByGroupId.entrySet()) {
				System.out.println("Section Set" + entry);
				String section = entry.getKey();
				Integer sectionId = Integer.valueOf(section);
				Set<Integer> covers = entry.getValue().stream().map(NonMotorSectionReq::getCoverId)
						.filter(Objects::nonNull).collect(Collectors.toSet());
				ProductSectionMaster sL = getActiveProductSections(sectionId, productId, companyId);
				if (sL == null) {
//					error.add("In This CompanyId :" + companyId + " and ProductId :" + productId
//							+ " This Section is not present : " + sectionId);
					
					error.add("3001" + "," + sectionId);
				}
				String res = getActiveSectionCovers(sectionId, productId, companyId, covers);
				if ("no".equalsIgnoreCase(res)) {
					error.add("3002");
				} else if ("yes".equalsIgnoreCase(res)) {
					return Collections.emptyList();
				}

				else if ("B".equalsIgnoreCase(res)) {
					List<String> status = new ArrayList<>();
					status.add("R");
					status.add("Y");
					List<SectionCoverMaster> sectionListcover = sectionRepo
							.findByCompanyIdAndProductIdAndSectionIdAndStatusInOrderByAmendIdDesc(companyId,
									productId, sectionId, status);
					List<SectionCoverMaster> distinctBTypeList = sectionListcover.stream()
							.filter(sect -> "B".equalsIgnoreCase(sect.getCoverageType()))
							.collect(Collectors.toMap(SectionCoverMaster::getCoverId, // distinct key
									sect -> sect, // value
									(existing, duplicate) -> existing // keep first, ignore others
							)).values().stream().collect(Collectors.toList());
					List<String> distinctCoverNames = distinctBTypeList.stream()
							.map(SectionCoverMaster::getCoverName).distinct().collect(Collectors.toList());

				//	error.add("Base Cover Missing :" + " " + distinctCoverNames);
					error.add("3003" + "," + distinctCoverNames);
				} else {
					error.add("3004" + "," +res);
				}

			}
		} catch (Exception e) {

			log.error(e);
			e.printStackTrace();
			error.add("Error In checking Section");
			
		}
		return error;
	}
	
	public ProductSectionMaster getActiveProductSections(Integer sectionId, Integer productId, String companyId) {
		try {
		ProductSectionMaster product = productRepo.findTopByCompanyIdAndProductIdAndSectionIdAndStatusOrderByAmendIdDesc(companyId,productId,sectionId,"Y");
		if(product==null)
		{
			  
            return null;
		}
		Date today = new Date();
        if (product.getEffectiveDateStart() != null && product.getEffectiveDateEnd() != null && !today.before(product.getEffectiveDateStart()) && !today.after(product.getEffectiveDateEnd())) {

            return product;
        }
        return null;
		}
		catch (Exception e){
			log.error(e);
			e.printStackTrace();
			return null;
		}
		
	}
	
	public String getActiveSectionCovers(Integer sectionId, Integer productId, String companyId,Set<Integer> cover) {
		try {
			String res= "";
			int a= 0;
			List<String> status=new ArrayList<>();
			status.add("R");
			status.add("Y");
								
			List<SectionCoverMaster> sectionList =sectionRepo.findLatestAmendPerCover(companyId,productId,sectionId,status,cover);
		 Date today = new Date();
//		 Map<Integer, SectionCoverMaster> latestPerCover = new HashMap<>();
//		  for (SectionCoverMaster s : sectionList) {
//	            latestPerCover.putIfAbsent(s.getCoverId(), s); // first (max) AmendId due to DESC order
//	        }
	    for(SectionCoverMaster section :sectionList) {
	    if (section.getEffectiveDateStart() != null && section.getEffectiveDateEnd() != null && !today.before(section.getEffectiveDateStart())
                && !today.after(section.getEffectiveDateEnd())) {
	    	a++;
        }
	    else
	    {
	    	res=(section.getCoverId()+"");
	    	return res;
	    }
		}
	    if(cover.size()!=sectionList.size())
		{
	    	return res="no";
		}
	    boolean isBTypePresent = sectionList.stream()
	            .anyMatch(section -> "B".equalsIgnoreCase(section.getCoverageType()));
	    if(!isBTypePresent)
	    {
	    	return res="B";
	    	
	    }
	    return res="yes";

		} catch (Exception e){
			log.error(e);
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean idValidation(String id,Set<String> ids) {
		if(ids.contains(id)) {
			return true;
		}
		else {
			ids.add(id);
			System.out.println(id);
			return false;
			}
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
			error.add("1551");
			
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
				if(!("SQ".equalsIgnoreCase(policyReq.getSavedFrom()))) {
					if (StringUtils.isBlank(policyReq.getCustomerReferenceNo())) {
						error.add("1569");
					}
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
						) {
					
					if(!policyReq.getMigratedPolicyYn().equalsIgnoreCase("Y")) {
						long MILLS_IN_A_DAY = 1000 * 60 * 60 * 24;
						long days90 = MILLS_IN_A_DAY * 90;
						Date today = new Date();
						Integer before=  eserviceSlideValidationImpl.getBackDaysNew(policyReq.getCompanyId(), policyReq.getProductId(), brokerReq.getBdmCode() ,brokerReq.getApplicationId());
						before = before==null ? 0 :before;
						int days = before == 0 ? -1 : -before;
						long backDays = MILLS_IN_A_DAY * days;
						Date beforedays = new Date(today.getTime() + backDays);
						Date resticDate = new Date(today.getTime() + days90);
						if (policyReq.getPolicyStartDate().before(beforedays)) {
							error.add("1572"+","+before);
						} else if (policyReq.getPolicyStartDate().after(resticDate)) {
							error.add("1573");
						}
					} else {
						Date today = new Date();
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(today);
						calendar.add(Calendar.YEAR, -1);
						Date oneYearAgo = calendar.getTime();
						if (policyReq.getPolicyStartDate().before(oneYearAgo)) {
							error.add("15723" + "," + policyReq.getPolicyStartDate());
						}
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
		
	
	private List<String> locationSectionValidation(NonMotorSaveReq req,List<String>error){
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
								error.add("2297");
							}
							
							else { 
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
									}
									
									else {
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

			} 
			
			else {
				error.add("2292");
			}

		} catch (Exception e) {

			log.error(e);
			e.printStackTrace();
			error.add("1551");
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
				Set<Integer> sectionsAllowBlankSumInsured = Set.of(
					    2, 8, 104, 160, 204, 213, 215, 321, 323
					);

					if (StringUtils.isBlank(dd.getSumInsured())) {

					    // Blank Sum Insured is allowed only for these section IDs
					    if (!sectionsAllowBlankSumInsured.contains(dd.getSectionId())) {
					        error.add("2264" + "," + locId);
					    }

					} else if (!dd.getSumInsured().matches("[0-9.]+")) {

					    // If Sum Insured is provided, validate the format
					    error.add("1547" + "," + locId);
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
			/*
			 * if("1".equalsIgnoreCase(dd.getSectionId())) {
			 * error=building(dd,locId,ids,error); }else
			 * if("40".equalsIgnoreCase(dd.getSectionId())) {
			 * error=fireAndAlliedPerils(dd,locId,error,ids); }else
			 * if("41".equalsIgnoreCase(dd.getSectionId())) {
			 * error=machineryBreakDown(dd,locId,ids,error); }else
			 * if("42".equalsIgnoreCase(dd.getSectionId())) { error=money(dd,locId,error);
			 * }else if("52".equalsIgnoreCase(dd.getSectionId())) {
			 * error=burglary(dd,locId,error); }else
			 * if("76".equalsIgnoreCase(dd.getSectionId())) {
			 * error=electeonicEquipment(dd,locId,error,ids); }else
			 * if("43".equalsIgnoreCase(dd.getSectionId())) {
			 * error=fiedlity(dd,locId,error,ids); }else
			 * if("45".equalsIgnoreCase(dd.getSectionId())) {
			 * error=workmenCompensation(dd,locId,error,ids); }else
			 * if("182".equalsIgnoreCase(dd.getSectionId())) {
			 * error=groupPersonalAccident(dd,locId,error,ids); }else
			 * if("54".equalsIgnoreCase(dd.getSectionId())) {
			 * error=liabiliyOrPublicLiability(dd,locId,error,ids); }else
			 * if("35".equalsIgnoreCase(dd.getSectionId())) {
			 * error=personalAccident(dd,locId,error,ids); }else
			 * if("117".equalsIgnoreCase(dd.getSectionId())) {
			 * error=bond(dd,locId,error,ids); }
			 */
			
		}catch (Exception e) {

			log.error(e);
			e.printStackTrace();
			error.add("1551");
			// error.add(new Error("19", "Common Error", e.getMessage()));
		}
		return error;
	}

	
	public List<ProductSectionMaster> getProductSection(String insuranceId, Integer productId) {
		
		try {
			Date todayStartTime = getTodayStartTime();
			List<ProductSectionMaster> productList = productSectionMasterRepository
					.findByCompanyIdAndProductIdAndStatusInAndEffectiveDateStartLessThanEqualAndEffectiveDateEndGreaterThanEqualOrderByAmendIdDesc(
							insuranceId, productId, List.of("Y", "R"), todayStartTime, getTodayEndTime());
			return productList;
		} catch (Exception e) {
            log.error("Error fetching Product Sections: {}", e.getMessage(), e);
            return Collections.emptyList(); // Return empty list instead of null
        }

	}
	    private Date getTodayStartTime() {
	        Calendar cal = Calendar.getInstance();
	        cal.set(Calendar.HOUR_OF_DAY, 23);
	        cal.set(Calendar.MINUTE, 1);
	        return cal.getTime();
	    }
	
	    private Date getTodayEndTime() {
	        Calendar cal = Calendar.getInstance();
	        cal.set(Calendar.HOUR_OF_DAY, 1);
	        cal.set(Calendar.MINUTE, 1);
	        return cal.getTime();
	    }

}
