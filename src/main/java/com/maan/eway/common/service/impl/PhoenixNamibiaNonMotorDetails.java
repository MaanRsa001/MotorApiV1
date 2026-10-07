package com.maan.eway.common.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.maan.eway.bean.LoginMaster;
import com.maan.eway.bean.ProductSectionMaster;
import com.maan.eway.common.req.NonMotEndtReq;
import com.maan.eway.common.req.NonMotorBrokerReq;
import com.maan.eway.common.req.NonMotorLocationReq;
import com.maan.eway.common.req.NonMotorPolicyReq;
import com.maan.eway.common.req.NonMotorSaveReq;
import com.maan.eway.common.req.NonMotorSectionReq;
import com.maan.eway.repository.LoginMasterRepository;
import com.maan.eway.repository.ProductSectionMasterRepository;

import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PhoenixNamibiaNonMotorDetails {

	private Logger log = LogManager.getLogger(PhoenixNamibiaNonMotorDetails.class);

	private final LoginMasterRepository loginRepo;

	private final EserviceSlideValidateServiceImpl eserviceSlideValidationImpl;
	
	private final ProductSectionMasterRepository productSectionMasterRepo;
 
	public List<String> validateNonMotorDetails(NonMotorSaveReq req) {
		List<String> error = new ArrayList<>();
		try {
			NonMotorPolicyReq policyReq = req.getNonMotorPolicyReq();
			NonMotorBrokerReq brokerReq = req.getNonMotorBrokerReq();
			NonMotEndtReq endtReq = req.getNonMotEndtReq();
			List<NonMotorLocationReq> locationReq = req.getLocationList();

			if (policyReq != null && brokerReq != null) {
				commonValidation(error, policyReq);
				policyDateValidation(error, policyReq, brokerReq, endtReq);
				sorceValidation(error, policyReq, brokerReq);
			} else {
				error.add("2293");
			}
			endtValidation(error, endtReq);
			locationSectionValidation(error, policyReq, locationReq);

		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			error.add("1551");
		}
		return error;
	}

	private void commonValidation(List<String> error, NonMotorPolicyReq policyReq) {
		try {
			log.info("********************Common  Validation starts*******************");
			if (StringUtils.isBlank(policyReq.getCustomerReferenceNo())) {
				error.add("1569");
			}

			if (StringUtils.isBlank(policyReq.getBranchCode())) {
				error.add("1570");
			} else if (policyReq.getBranchCode().length() > 20) {
				error.add("2305");
			}

			if (StringUtils.isBlank(policyReq.getProductId())) {
				error.add("1565");
			} else if (policyReq.getProductId().length() > 20) {
				error.add("1564");
			}

			if (StringUtils.isBlank(policyReq.getCurrency())) {
				error.add("2303");
			}
			if (StringUtils.isBlank(policyReq.getExchangeRate())) {
				error.add("2289");
			} else if (Double.valueOf(policyReq.getExchangeRate()) <= 0D) {
				error.add("2329");
			} else {
				Tuple minMax = eserviceSlideValidationImpl.getMinMaxRate(policyReq.getCurrency(),
						policyReq.getCompanyId());
				if (minMax != null) {

					Double exRate = Double
							.valueOf(minMax.get("exchangeRate") == null ? "0" : minMax.get("exchangeRate").toString());
					Double minRate = Double
							.valueOf(minMax.get("minDiscount") == null ? "0" : minMax.get("minDiscount").toString());
					Double maxRate = Double
							.valueOf(minMax.get("maxLoading") == null ? "0" : minMax.get("maxLoading").toString());
					minRate = exRate - (exRate * minRate / 100);
					maxRate = exRate + (exRate * maxRate / 100);
					if (Double.valueOf(policyReq.getExchangeRate()) <= minRate
							|| Double.valueOf(policyReq.getExchangeRate()) >= maxRate) {
						error.add("2304" + "," + policyReq.getCurrency());
					}
				}
			}

			if (StringUtils.isBlank(policyReq.getHavepromocode())) {
				error.add("2308");
			}
			if ((StringUtils.isNotBlank(policyReq.getHavepromocode()))
					&& policyReq.getHavepromocode().equalsIgnoreCase("Y")
					&& StringUtils.isBlank(policyReq.getPromocode())) {
				error.add("2309");
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			error.add("1551");
		}
	}

	private void sorceValidation(List<String> error, NonMotorPolicyReq policyReq, NonMotorBrokerReq brokerReq) {
		try {
			log.info("********************Source Validation *******************");
			List<String> directSource = new ArrayList<>();
			directSource.add("1");
			directSource.add("2");
			directSource.add("3");

			if (brokerReq.getUserType().equalsIgnoreCase("Issuer")
					&& StringUtils.isBlank(brokerReq.getSourceTypeId())) {
				error.add("2310");
			} else if (StringUtils.isNotBlank(brokerReq.getSourceTypeId())
					&& directSource.contains(brokerReq.getSourceTypeId())) {
				if (StringUtils.isBlank(brokerReq.getBdmCode())) {
					error.add("2310");
				}
				if (StringUtils.isBlank(brokerReq.getCustomerName())) {
					error.add("2291");
				}

			} else {
				if (StringUtils.isBlank(brokerReq.getLoginId())) {
					error.add("2311");
				} else {
					if (StringUtils.isNotBlank(policyReq.getCreatedBy())) {
						LoginMaster loginData = loginRepo.findByLoginId(policyReq.getCreatedBy());
						if (loginData.getSubUserType().equalsIgnoreCase("bank")
								&& StringUtils.isBlank(policyReq.getAcExecutiveId())) {
							error.add("2312");
						}
					}
				}
				if (StringUtils.isBlank(brokerReq.getBrokerBranchCode())) {
					error.add("2313");
				}
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			error.add("1551");
		}
	}

	private void policyDateValidation(List<String> error, NonMotorPolicyReq policyReq, NonMotorBrokerReq brokerReq,
			NonMotEndtReq endtReq) {
		try {
			String status = StringUtils.isBlank(policyReq.getStatus()) ? "Y" : policyReq.getStatus();
			log.info("********************Policy Date Validation *******************");

			if (policyReq.getPolicyStartDate() == null) {
				error.add("1566");
			} else if ((endtReq.getEndorsementType() == null || endtReq.getEndorsementType().equals(0))
					&& !"RQ".equalsIgnoreCase(status)) {
				int before = eserviceSlideValidationImpl.getBackDays(policyReq.getCompanyId(), policyReq.getProductId(),
						brokerReq.getLoginId());
				int days = before == 0 ? -1 : -before;
				long millsInADays = 1000 * 60 * 60 * 24;
				long backDays = millsInADays * days;
				Date today = new Date();
				Date resticDate = new Date(today.getTime() + backDays);
				long days90 = millsInADays * 90;
				Date after90 = new Date(today.getTime() + days90);
				if (policyReq.getPolicyStartDate().before(resticDate)) {
					error.add("1572" + "," + before);
				} else if (policyReq.getPolicyStartDate().after(after90)) {
					error.add("1573");
				}

			}

			if (policyReq.getPolicyEndDate() == null) {
				error.add("1571");

			} else if (policyReq.getPolicyStartDate() != null && policyReq.getPolicyEndDate() != null
					&& endtReq.getEndorsementType() == null
					&& (policyReq.getPolicyEndDate().equals(policyReq.getPolicyStartDate())
							|| policyReq.getPolicyEndDate().before(policyReq.getPolicyStartDate()))) {
				error.add("2290");
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			error.add("1551");
		}
	}

	private List<String> endtValidation(List<String> error, NonMotEndtReq endtReq) {
		try {
			log.info("********************Endorsement Validation starts*******************");
			if (endtReq != null) {
				if (endtReq.getEndorsementType() == null ) {
					log.info("No Endorsement");
				}

			} else {
				log.info("No Endorsement");
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			error.add("1224");
		}
		return error;
	}

	private List<String> locationSectionValidation(List<String> error, NonMotorPolicyReq policyReq,
			List<NonMotorLocationReq> locationReq) {
		try {
			List<ProductSectionMaster> getProductSection = getProductSection(policyReq.getCompanyId(),
					Integer.valueOf(policyReq.getProductId()));

			log.info("********************Location Wise Validation Loop starts*******************");
			if (locationReq != null && !locationReq.isEmpty()) {

				for (NonMotorLocationReq locationdata : locationReq) {
					List<NonMotorSectionReq> sectionReq = locationdata.getSectionList();
					Integer locId = Integer.valueOf(locationdata.getLocationId());

					/** Location Checking block **/
					if (StringUtils.isBlank(locationdata.getLocationId())) {
						error.add("2294" + "," + locId);
					} else if (StringUtils.isBlank(locationdata.getLocationName())) {
						error.add("2269" + "," + locId);
					} else if (locationdata.getLocationName().length() > 200) {
						error.add("2271" + "," + locId);
					} else {

						if (sectionReq != null) {
							sectionCheckingBlock(error, policyReq, locationReq, getProductSection, sectionReq,
									locId);
						} else {
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

	private List<String> sectionCheckingBlock(List<String> error, NonMotorPolicyReq policyReq,
			List<NonMotorLocationReq> locationReq, List<ProductSectionMaster> getProductSection,
			List<NonMotorSectionReq> sectionReq, Integer locId) {
		try {
			/** Section Checking Block **/
			Map<String, List<NonMotorSectionReq>> sectionIds = sectionReq.stream().filter(o -> o.getSectionId() != null)
					.collect(Collectors.groupingBy(NonMotorSectionReq::getSectionId));

			log.info("Section Id " + sectionIds);
			if (sectionIds.isEmpty()) {
				error.add("2297");
			} else if (StringUtils.isNoneBlank(policyReq.getProductId()) && policyReq.getProductId().equals("16")
					&& locationReq != null && !locationReq.isEmpty()) {}

			else {
				for (String group : sectionIds.keySet()) {

					List<NonMotorSectionReq> fiterData = sectionReq.stream()
							.filter(o -> o.getSectionId().equalsIgnoreCase(group))
							.toList();
					for (NonMotorSectionReq dd : fiterData) {
						log.info("LocationId : " + locId);
						log.info("SectionId : " + dd.getSectionId());
						if (StringUtils.isBlank(dd.getSectionId())) {
							error.add("2296");
						}

						else {
							List<ProductSectionMaster> sectionData = getProductSection.stream()
									.filter(o -> o.getSectionId().equals(Integer.valueOf(dd.getSectionId()))).toList();

							if (sectionData == null || sectionData.isEmpty()) {
								error.add("2328");
							} else {
								error = sectionListValidation(dd, locId, error);
							}
						}
					}
				}
			}
		} catch (Exception e) {

			log.error(e);
			e.printStackTrace();
			error.add("1224");
		}
		return error;
	}
	
	private List<String> sectionListValidation(NonMotorSectionReq dd, Integer locId, List<String> error) {
		try {
			/** Common Validation **/
			if(!"42".equalsIgnoreCase(dd.getSectionId())) {
				if (StringUtils.isBlank(dd.getSectionName())) {
					error.add("2297"+","+locId);
				}
				if (StringUtils.isBlank(dd.getSumInsured())) {
					error.add("2264"+","+locId);
				} else if (!dd.getSumInsured().matches("[0-9.]+")) {
					error.add("1547"+","+locId);
				}
			}
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			error.add("1224");
		}
		return error;
	}


	public List<ProductSectionMaster> getProductSection(String insuranceId, Integer productId) {
		try {

			Date today = new Date();
			return productSectionMasterRepo
					.findByProductIdAndCompanyIdAndEffectiveDateStartLessThanEqualAndEffectiveDateEndGreaterThanEqualAndStatusOrderByAmendIdDesc(
							productId, insuranceId, today, today, "Y");

		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return Collections.emptyList();
		}
	}
}
