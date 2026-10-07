package com.maan.eway.common.res;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SlideMoneySaveRes {


	@JsonProperty("RequestReferenceNo")
    private String     requestReferenceNo ;
	
	@JsonProperty("RiskId")
    private String    riskId   ;
	
	@JsonProperty("ProductId")
    private String    productId    ;
	
	@JsonProperty("SectionId")
    private String sectionId    ;
	
	@JsonProperty("InsuranceId")
    private String insuranceId    ;
	
	@JsonProperty("CreatedBy")
    private String createdBy    ;
	

	// other companies
	@JsonProperty("StrongroomSi")
    private String strongroomSi;

	@JsonProperty("MoneySafeLimit")
    private String moneySafeLimit    ;
	
	@JsonProperty("MoneyOutofSafe")
    private String moneyOutofSafe    ;
	
	@JsonProperty("MoneyDirectorResidence")
    private String moneyDirectorResidence    ;
	
	@JsonProperty("MoneyCollector")
    private String moneyCollector    ;
	
	@JsonProperty("MoneyAnnualEstimate")
    private String moneyAnnualEstimate    ;
	
	@JsonProperty("MoneyMajorLoss")
    private String moneyMajorLoss;
	
	
	//Tanzaniya
	@JsonProperty("MoneyInSafe")
    private String moneyInSafe;
	
	@JsonProperty("Premises")
    private String premises    ;

	@JsonProperty("StrongRoom")
    private String strongRoom    ;


	@JsonProperty("EstimatedAnnualCarryings")
    private String estimatedAnnualCarryings    ;

	@JsonProperty("MoneyInTransit")
    private String moneyInTransit;
	
	
	
	
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
	private String endtCount;
	@JsonProperty("EndtStatus") // EndtStatus
	private String endtStatus;
	@JsonProperty("IsFinanceEndt") // IsFinanceEndt
	private String isFinaceYn;
	@JsonProperty("EndtCategoryDesc") // EndtCategoryDesc
	private String endtCategDesc;
	@JsonProperty("EndorsementType") // EndorsementType
	private String endorsementType;

	@JsonProperty("EndorsementTypeDesc") // EndorsementTypeDesc
	private String endorsementTypeDesc;
	@JsonProperty("LocationName")
    private String locationName;
	
	  @JsonProperty("Address")
      private String address;
      
      @JsonProperty("RegionCode")
      private String regionCode;
      
      @JsonProperty("RegionDesc")
      private String     regionDesc ;
      
      @JsonProperty("DistrictCode")
      private String districtCode ;
      
      @JsonProperty("DistrictDesc")
      private String     districtDesc ;
	
}
