package com.maan.eway.common.req;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class DriverDetailsGetRes {

	@JsonProperty("DriverId")
	private Integer driverId;
	
	@JsonProperty("DriverName")
	private String driverName;
	
	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("DriverDob")
	private Date driverDob;
	
	@JsonProperty("DriverType")
	private String driverType;
	
	@JsonProperty("DriverTypedesc")
	private String driverTypedesc;
	
	@JsonProperty("LicenseNo")
	private String licenseNo;
	
	
	@JsonProperty("QuoteNo")
	private String quoteNo;
	
	@JsonProperty("RequestReferenceNo")
	private String requestReferenceNo;
	
	@JsonProperty("RiskId")
    private Integer    riskId ;
	
	@JsonProperty("CreatedBy")
	private String createdBy;
	
	@JsonProperty("StateId")
	private String stateId;
	
	@JsonProperty("State")
	private String state;
	
	@JsonProperty("CityId")
	private String cityId;
	
	@JsonProperty("City")
	private String city;
	
	@JsonProperty("CountryId")
	private String countryId;
	
	@JsonProperty("CountryName")
	private String     countryName ;
	
	@JsonProperty("SuburbId")
	private String suburbId;
	
	@JsonProperty("Suburb")
	private String     suburb ;
	
	@JsonProperty("AreaGroup")
	private String areaGroup;
	
	@JsonProperty("MaritalStatus")
	private String maritalStatus;
	

	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("LicenseIssueDt")
	private Date licenseIssueDt;
	
	@JsonProperty("Gender")
	private String driverGender;
	
	@JsonProperty("DriverExperience")
	private Integer licenseExperience;
	
	@JsonProperty("DdRefno")
    private Long       ddRefno ;
  
    @JsonProperty("EndorsementDate") //EndorsementDate
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date       endorsementDate ;
    @JsonProperty("EndorsementRemarks") // EndorsementRemarks
    private String     endorsementRemarks ;    
    @JsonProperty("EndorsementEffectiveDate") // EndorsementEffectiveDate
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date       endorsementEffdate ;
    @JsonProperty("OrginalPolicyNo") // OrginalPolicyNo
    private String     originalPolicyNo ;
    @JsonProperty("EndtPrevPolicyNo") // EndtPrevPolicyNo
    private String     endtPrevPolicyNo ;
    @JsonProperty("EndtPrevQuoteNo") // EndtPrevQuoteNo
    private String     endtPrevQuoteNo ;
    @JsonProperty("EndtCount")  // EndtCount
    private BigDecimal endtCount ;
    @JsonProperty("EndtStatus") //EndtStatus
    private String     endtStatus ;   
    @JsonProperty("IsFinanceEndt") //IsFinanceEndt
    private String     isFinaceYn ;  
    @JsonProperty("EndtCategoryDesc") //EndtCategoryDesc
    private String     endtCategDesc ;
    @JsonProperty("EndorsementType") //EndorsementType
    private Integer    endorsementType ;

    @JsonProperty("EndorsementTypeDesc") // EndorsementTypeDesc
    private String     endorsementTypeDesc ;
    

    @JsonProperty("ClaimExperience")
	private Integer claimExperience;
    
    @JsonProperty("ClaimType")
	private String claimType;
    
    @JsonProperty("ClaimTypeDesc")
	private String claimTypeDesc;
	
	
//    @JsonProperty("DrivingLicensingAge")
//	private Integer drivingLicenseAge;
}
