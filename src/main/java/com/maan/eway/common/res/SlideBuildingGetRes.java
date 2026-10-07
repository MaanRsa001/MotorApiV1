package com.maan.eway.common.res;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SlideBuildingGetRes {

	// Primary Key 
	@JsonProperty("RequestReferenceNo")
    private String     requestReferenceNo ;
	
	@JsonProperty("RiskId")
    private String    riskId   ;
	
	@JsonProperty("SectionId")
    private String sectionId    ;
	
	@JsonProperty("InsuranceId")
    private String insuranceId;
	
	@JsonProperty("ProductId")
    private String productId;
	
	@JsonProperty("CreatedBy")
	private String createdBy;
	
	
	@JsonProperty("BuildingSumInsured")
    private String buildingSumInsured;
	
	@JsonProperty("WallType")
    private String     wallType;
	
	@JsonProperty("RoofType")
    private String     roofType;

	
	@JsonProperty("BuildingUsageId")
    private String     buildingUsageId;


	@JsonProperty("BuildingOwnerYn")
	private String     buildingOwnerYn;
	
	@JsonProperty("BuildingBuildYear")
    private String    buildingBuildYear ;
	
	@JsonProperty("WaterTankSi")
    private String    waterTankSi;

//	@JsonProperty("ArchitectsSi")
//    private String    architectsSi;

	@JsonProperty("LossOfRentSi")
    private String    lossOfRentSi;


	@JsonProperty("TypeOfProperty")
    private String    typeOfProperty;
	

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
    private String     locationName;
	
	@JsonProperty("IndustryType")
    private String   industryType;
	
	@JsonProperty("OccupationId")
    private String   occupationId;
	
	@JsonProperty("CoveringDetails")
    private String  CoveringDetails ;
	
	
	@JsonProperty("DescriptionOfRisk")
    private String  descriptionOfRisk ;
	

	@JsonProperty("RegionCode")
    private String  regionCode ;
	

	@JsonProperty("RegionName")
    private String  regionName ;
	
	@JsonProperty("SectionDesc")
    private String  sectionDesc ;
	
	@JsonProperty("IndustryTypeDesc")
    private String  industryTypeDesc ;
	

	@JsonProperty("OccupationDesc")
    private String  occupationDesc ;
	
	@JsonProperty("DistrictCode")
    private String  districtCode ;
	
	@JsonProperty("DistrictName")
    private String  districtName ;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("PolicyStartDate")
    private Date       policyStartDate ;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("PolicyEndDate")
    private Date       policyEndDate ;
	
	@JsonProperty("Currency")
    private String     currency ;
	
	@JsonProperty("Promocode")
	private String   promoCode;
	
	@JsonProperty("ExchangeRate")
    private BigDecimal     exchangeRate ;
	
	@JsonProperty("BrokerCode")
	private String BrokerCode;
	
	@JsonProperty("UserType")
	private String UserType;
	@JsonProperty("TiraCoverNoteNo")
    private String     tiraCoverNoteNo ;
	@JsonProperty("AcexecutiveId")
    private String    acExecutiveId ;
	
	@JsonProperty("CommissionType")
    private String    commissionType;
	
	@JsonProperty("BankCode")
    private String    bankCode;
	
	@JsonProperty("CustomerCode")
    private String     customerCode;
	
	@JsonProperty("SubUsertype")
    private String     subUserType  ;
	
	@JsonProperty("BrokerBranchCode")
    private String     BrokerbranchCode   ;

	
	@JsonProperty("BranchCode")
    private String     branchCode   ;
	
	@JsonProperty("CustomerName")
    private String     customerName;
	
	@JsonProperty("Usertype")
	private String   subUsertype;
	
	
	@JsonProperty("SourceTypeId")
	private String sourceTypeId;
	
	@JsonProperty("SourceType")
	private String sourceType;
	
	@JsonProperty("ProductType")
	private String   productType;
	
	@JsonProperty("AgencyCode")
	private String   agencyCode;
	
	  @JsonProperty("BdmCode") // EndtCategoryDesc 
	  private String BdmCode;
	
		@JsonProperty("Business_Interruption")
		private String  businessInterruption;
		
		@JsonProperty("FirstLossPayee")
	    private String  firstLossPayee ;
	
	
}
