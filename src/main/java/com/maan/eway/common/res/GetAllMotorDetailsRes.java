package com.maan.eway.common.res;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetAllMotorDetailsRes {

	@JsonProperty("CustomerReferenceNo")
	private String customerReferenceNo;
	@JsonProperty("BrokerBranchCode")
	private String brokerBranchCode;
	@JsonProperty("RequestReferenceNo")
	private String requestReferenceNo;
	@JsonProperty("Idnumber")
	private String idNumber;
	@JsonProperty("Vehicleid")
	private Integer vehicleId;
	private String accident;
	@JsonProperty("Windscreencoverrequired")
	private String windScreenCoverRequired;
	@JsonProperty("Insurancetype")
	private String insuranceType;
	@JsonProperty("InsuranceTypeDesc")
	private String insuranceTypeDesc;
	@JsonProperty("Registrationnumber")
	private String registrationNumber;
	@JsonProperty("Chassisnumber")
	private String chassisNumber;
	@JsonProperty("Vehiclemake")
	private String vehicleMake;
	@JsonProperty("HavePromoCode")
	private String havepromocode;

	@JsonProperty("PolicyTypeDesc")
	private String policyTypeDesc;

	@JsonProperty("AdminRemarks")
	private String adminRemarks;

	@JsonProperty("ReferalRemarks")
	private String referalRemarks;

	@JsonProperty("PromoCode")
	private String promocode;
	@JsonProperty("BankCode")
	private String bankCode;

	@JsonProperty("VehiclemakeDesc")
	private String vehicleMakeDesc;
	@JsonProperty("Vehcilemodel")
	private String vehcileModel;
	@JsonProperty("VehcilemodelDesc")
	private String vehcileModelDesc;
	@JsonProperty("VehicleType")
	private String vehicleType;
	@JsonProperty("VehicleTypeDesc")
	private String vehicleTypeDesc;
	@JsonProperty("ModelNumber")
	private String modelNumber;
	@JsonProperty("SumInsured")
	private Double sumInsured;
	@JsonProperty("DrivenByDesc")
	private String drivenByDesc;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("AgencyCode")
	private String agencyCode;
	@JsonProperty("SectionId")
	private String sectionId;
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("InsuranceId")
	private String companyId;
	@JsonProperty("InsuranceClass")
	private String insuranceClass;

	@JsonProperty("MotorUsageDesc")
	private String motorUsageDesc;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("ManufactureYear")
	private Date manufactureYear;

	@JsonProperty("Status")
	private String status;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("UpdatedDate")
	private Date updatedDate;

	@JsonProperty("UpdatedBy")
	private String updatedBy;

	@JsonProperty("CreatedBy")
	private String createdBy;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("PolicyStartDate")
	private Date policyStartDate;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("PolicyEndDate")
	private Date policyEndDate;

	@JsonProperty("SavedFrom")
	private String savedFrom;

	@JsonProperty("ActualPremiumLc")
	private String actualPremiumLc;

	@JsonProperty("AcctualPremiumFc")
	private String actualPremiumFc;

	@JsonProperty("OverallPremiumLc")
	private String overallPremiumLc;

	@JsonProperty("OverallPremiumFc")
	private String overallPremiumFc;

	@JsonProperty("BrokerCode")
	private String brokerCode;

	@JsonProperty("LoginId")
	private String loginId;

	@JsonProperty("AcExecutiveId")
	private String acExecutiveId;

	@JsonProperty("SubUserType")
	private String subUserType;

	@JsonProperty("ApplicationId")
	private String applicationId;

	@JsonProperty("Currency")
	private String currency;

	@JsonProperty("ExchangeRate")
	private String exchangeRate;

	@JsonProperty("QuoteNo")
	private String quoteNo;

	@JsonProperty("CustomerId")
	private String customerId;

	@JsonProperty("BdmCode")
	private String bdmCode;

	@JsonProperty("SourceTypeId")
	private String sourceTypeId;

	@JsonProperty("SourceType")
	private String sourceType;

	@JsonProperty("CustomerCode")
	private String customerCode;

	@JsonProperty("CommissionType")
	private String commissionType;

	@JsonProperty("EndorsementType")
	private String endorsementType;

	@JsonProperty("EndorsementTypeDesc")
	private String endorsementTypeDesc;

	@JsonProperty("PolicyRenewalYn")
	private String custRenewalYn;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EndorsementDate")
	private Date endorsementDate;

	@JsonProperty("EndorsmentRemarks")
	private String endorsementRemarks;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EndorsementEffectiveDate")
	private Date endorsementEffdate;

	@JsonProperty("OrginalPolicyNo")
	private String originalPolicyNo;

	@JsonProperty("EndtPrevPolicyNo")
	private String endtPrevPolicyNo;

	@JsonProperty("EndtPrevQuoteNo")
	private String endtPrevQuoteNo;

	@JsonProperty("EndtCount")
	private BigDecimal endtCount;

	@JsonProperty("EndtStatus")
	private String endtStatus;

	@JsonProperty("IsFinanceYesNo")
	private String isFinaceYn;

	@JsonProperty("EndtCategDesc")
	private String endtCategDesc;

	@JsonProperty("EndorsementYn")
	private String endorsementYn;

	@JsonProperty("MotorCategory")
	private String motorCategory;

	@JsonProperty("MotorCategoryDesc")
	private String motorCategoryDesc;

	@JsonProperty("FinalizeYn")
	private String finalizeYn;

	@JsonProperty("OwnerName")
	private String ownerName;

	@JsonProperty("ResOwnerName")
	private String resOwnerName;

	@JsonProperty("CarAlarmYn")
	private String carAlarmYn;

	@JsonProperty("VehicleClass")
	private String vehicleClass;

	@JsonProperty("ClaimType")
	private String claimType;

	@JsonProperty("ClaimTypeDesc")
	private String claimTypeDesc;

	@JsonProperty("VehicleValueType")
	private String vehicleValueType;

	@JsonProperty("VehicleValueTypeDesc")
	private String vehicleValueTypeDesc;

	@JsonProperty("Inflation")
	private String inflation;

	@JsonProperty("Ncb")
	private String ncb;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("PurchaseDate")
	private Date purchaseDate;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("RegistrationDate")
	private Date registrationDate;

	@JsonProperty("ExcessLimit")
	private String excessLimit;

	@JsonProperty("Deductibles")
	private String excess;

	@JsonProperty("ExcessDesc") // Deductibles Description
	private String excessDesc;

	@JsonProperty("DefenceValue")
	private String defenceValue;

	@JsonProperty("DefenceValueDesc")
	private String defenceValueDesc;

	@JsonProperty("Mileage")
	private Integer mileage;

	@JsonProperty("NoOfTrailers")
	private Integer noOfTrailers;

	@JsonProperty("NoOfPassengers")
	private Integer noOfPassengers;

	@JsonProperty("NoClaimYears")
	private Integer noClaimYears;

	@JsonProperty("ColorId")
	private Integer colorId;

	@JsonProperty("ColorDesc")
	private String colorDesc;

	@JsonProperty("NewValue")
	private Integer newValue;

	@JsonProperty("MarketValue")
	private Integer marketValue;

	@JsonProperty("AggregatedValue")
	private Integer aggregatedValue;

	@JsonProperty("MunicipalityTraffic")
	private String municipalityTraffic;

	@JsonProperty("TransportHydro")
	private String transportHydro;

	@JsonProperty("NumberOfCards")
	private Integer noOfCards;

	@JsonProperty("DisplacementInCM3")
	private String displacementInCM3;

	@JsonProperty("NumberOfCylinders")
	private Integer noOfCylinders;

	@JsonProperty("VehicleTypeDescLocal")
	private String vehicleTypeDescLocal;

	@JsonProperty("VehiclemakeDescLocal")
	private String vehicleMakeDescLocal;

	@JsonProperty("PlateType")
	private String plateType;

	@JsonProperty("Zone")
	private String zone;

	@JsonProperty("BusinessSourceId")
	private String businessSourceId;

	@JsonProperty("BusinessSource")
	private String businessSource;
	
	@JsonProperty("MigratedPolicyYn")
	private String migratedPolicyYn;
	
}
