package com.maan.eway.common.req;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MotorDriverSaveReq {

	@JsonProperty("QuoteNo")
	private String quoteNo;

	@JsonProperty("RequestReferenceNo")
	private String requestReferenceNo;

	@JsonProperty("RiskId")
	private String riskId;

	@JsonProperty("DriverName")
	private String driverName;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("DriverDob")
	private Date driverDob;

	@JsonProperty("DriverType")
	private String driverType;

	@JsonProperty("LicenseNo")
	private String licenseNo;

	@JsonProperty("CreatedBy")
	private String createdBy;

	@JsonProperty("StateId")
	private String stateId;

	@JsonProperty("CityId")
	private String cityId;

	@JsonProperty("CountryId")
	private String countryId;

	@JsonProperty("SuburbId")
	private String suburbId;

	@JsonProperty("AreaGroup")
	private String areaGroup;

	@JsonProperty("InsuranceId")
	private String insuranceId;

	@JsonProperty("MaritalStatus")
	private String maritalStatus;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("LicenseIssueDt")
	private Date licenseIssueDt;

	@JsonProperty("Gender")
	private String gender;

	@JsonProperty("DriverExperience")
	private String driverExperience;

//   @Column(name="LICENSE_DURATION", length=100)
//   private Integer licenseDuration ;
//
//   @Column(name="AGE", length=100)
//   private Integer age ;

//   @Column(name="LICENSE_EXPERIENCE", length=100)
//   private Integer     licenseExperience ;

	@JsonProperty("EndorsementDate") // EndorsementDate
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date endorsementDate;
	@JsonProperty("EndorsementRemarks") // EndorsementRemarks
	private String endorsementRemarks;
	@JsonProperty("EndorsementEffectiveDate") // EndorsementEffectiveDate
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date endorsementEffdate;
	@JsonProperty("OrginalPolicyNo") // OrginalPolicyNo
	private String originalPolicyNo;
	@JsonProperty("EndtPrevPolicyNo") // EndtPrevPolicyNo
	private String endtPrevPolicyNo;
	@JsonProperty("EndtPrevQuoteNo") // EndtPrevQuoteNo
	private String endtPrevQuoteNo;
	@JsonProperty("EndtCount") // EndtCount
	private BigDecimal endtCount;
	@JsonProperty("EndtStatus") // EndtStatus
	private String endtStatus;
	@JsonProperty("IsFinanceEndt") // IsFinanceEndt
	private String isFinaceYn;
	@JsonProperty("EndtCategoryDesc") // EndtCategoryDesc
	private String endtCategDesc;
	@JsonProperty("EndorsementType") // EndorsementType
	private Integer endorsementType;

	@JsonProperty("EndorsementTypeDesc") // EndorsementTypeDesc
	private String endorsementTypeDesc;

//	@JsonProperty("DrivingLicensingAge")
//	private Integer drivingLicensingAge;

	@JsonProperty("Subscriber")
	private String subscriber;

	@JsonProperty("Civility")
	private String civility;

	@JsonProperty("PlaceIssue")
	private String placeIssue;

	@JsonProperty("CategoryCode")
	private String categoryCode;

	@JsonProperty("CategoryExDate")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date categoryExDate;
	
	@JsonProperty("CategoryDate")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date categoryDate;
	
	@JsonProperty("Email")
	private String email;

	@JsonProperty("ContactCode")
	private String contactCode;
	
	@JsonProperty("Contact")
	private String contact;
	
	@JsonProperty("DrivingLicensingAge")
	private Integer drivingLicensingAge;
	
	@JsonProperty("DriverLicenseExpiryDate")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date driverLicenseExpiryDate;
	
    @JsonProperty("ClaimExperience")
	private Integer claimExperience;
    
    @JsonProperty("ClaimType")
	private String claimType;
    
    @JsonProperty("ClaimTypeDesc")
	private String claimTypeDesc;
	
    
	
	
}
