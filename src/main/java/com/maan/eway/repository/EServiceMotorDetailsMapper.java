package com.maan.eway.repository;

import java.math.BigDecimal;
import java.util.Date;

public interface EServiceMotorDetailsMapper {

	String getChassisNumber();

	String getStatus();

	String getAccident();

	String getGpsTrackingInstalled();

	String getWindScreenCoverRequired();

	String getMotorUsage();

	String getInsuranceClass();

	String getInsuranceType();

	Integer getRiskId();

	Integer getNcdYears();

	String getNcdYn();

	Integer getNoOfClaims();

	Integer getNoOfCompehensives();

	Integer getNoOfVehicles();

	String getOwnerCategory();

	Date getRegistrationYear();

	String getVehicleType();

	BigDecimal getTppdIncreaeLimit1();

	BigDecimal getTppdIncreaeLimitLc();

	BigDecimal getWindScreenSumInsuredLc();

	BigDecimal getAcccessoriesSumInsuredLc();

	String getHavepromocode();

	String getPromocode();
	
	String getCustomerId();

	Integer getEndorsementType();

	String getIsFinaceYn();

	String getCarAlarmYn();

	String getExcess();

	BigDecimal getCreditShortfallSi();

	String getVehicleValueType();

	String getInflation();

	String getNcb();

	String getDefenceValue();

	Date getRegistrationDate();

	Date getPurchaseDate();

	String getCustRenewalYn();

	Integer getZone();

	Integer getHorsePower();

	String getPaCoverId();

	Integer getNoOfPassengers();

	Integer getCoverId();

	String getImportYN();

	// Financial info
	BigDecimal getSumInsured();

	BigDecimal getAcccessoriesSumInsured();

	BigDecimal getWindScreenSumInsured();

	BigDecimal getTppdIncreaeLimit();

	BigDecimal getNonElecAccessoriesSiLc();

	BigDecimal getExcessLimitLc();

	BigDecimal getExchangeRate();

	String getCurrency();

	Date getEntryDate();

	String getCreatedBy();

	String getOldPolicyNumber();
	
	String getQuoteNo();
	
	

	String getProductId();

	String getSectionId();

	String getBranchCode();

	String getCompanyId();

	Long getVdRefno();

	Long getCdRefno();

	Long getMsRefno();

	String getAgencyCode();

	String getVehicleMake();

	String getVehcileModel();

	String getFuelType();
	
	String getManufactureYear();
	
	Integer getManufactureAge();

	String getCityLimit();
	
	Integer getSeatingCapacity();
	
	String getPeriodOfInsurance();
	
	String getGrossWeight();
	
	String getFleetOwnerYn();
	BigDecimal getClaimRatio();
	

	
//	BigDecimal getSumInsured();
//
//	BigDecimal getAcccessoriesSumInsured();
//
//	BigDecimal getWindScreenSumInsured();
//
//	BigDecimal getTppdIncreaeLimit();
//
//	BigDecimal getNonElecAccessoriesSiLc();
//
//	BigDecimal getExcessLimitLc();
//
//	BigDecimal getExchangeRate();
//
//	String getCurrency();
//
//	Date getEntryDate();
//
//	String getCreatedBy();
//
//	String getOldPolicyNumber();
//
//	String getProductId();
//
//	String getSectionId();
//
//	String getBranchCode();
//
//	String getCompanyId();
//
//	Integer getRiskId();
//
//	String getAgencyCode();
//
//	Long getVdRefno();
//
//	Long getCdRefno();
//
//	Long getMsRefno();
//
//	Long getDdRefno();
//
//	String getChassisNumber();
//	
//	String getStatus();

}
