package com.maan.eway.common.req;

import java.math.BigDecimal;
import java.util.Date;
 

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SlideBuildingSaveReq {

	@JsonProperty("RequestReferenceNo")
	private String requestReferenceNo;

	@JsonProperty("RiskId")
	private String riskId;

	@JsonProperty("ProductId")
	private String productId;

	@JsonProperty("SectionId")
	private String sectionId;

	@JsonProperty("InsuranceId")
	private String insuranceId;

	@JsonProperty("CreatedBy")
	private String createdBy;

	@JsonProperty("BuildingSumInsured")
	private String buildingSumInsured;

	@JsonProperty("WallType")
	private String wallType;

	@JsonProperty("RoofType")
	private String roofType;

	@JsonProperty("BuildingUsageId")
	private String buildingUsageId;

	@JsonProperty("BuildingOwnerYn")
	private String buildingOwnerYn;

	@JsonProperty("BuildingBuildYear")
	private String buildingBuildYear;

	@JsonProperty("Status")

    private String    status;

	@JsonProperty("WaterTankSi")
    private String    waterTankSi;

	@JsonProperty("ArchitectsSi")
    private String    architectsSi;

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
	
	@JsonProperty("GroundUndergroundSi")
      private 	String groundUndergroundSi;
	
	
	//Fire And Allied Perills
	
	
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
	

	@JsonProperty("RegionName")
    private String  regionName ;
	
	@JsonProperty("DistrictName")
    private String  districtName ;
	
	@JsonProperty("FirstLossPayee")
    private String  firstLossPayee ;
	
    
}
