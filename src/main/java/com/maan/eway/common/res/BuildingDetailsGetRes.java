package com.maan.eway.common.res;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BuildingDetailsGetRes {

	@JsonProperty("RequestReferenceNo")
	private String requestReferenceNo;
	@JsonProperty("RiskId")
	private String riskId;
	@JsonProperty("SectionId")
	private String sectionId;
	@JsonProperty("InbuildConstructType")
	private String inbuildConstructType;
	@JsonProperty("InbuildConstructTypeDesc")
	private String inbuildConstructTypeDesc;
	@JsonProperty("BuildingFloors")
	private String buildingFloors;
	@JsonProperty("BuildingUsageId")
	private String buildingUsageId;
	@JsonProperty("BuildingUsageDesc")
	private String buildingUsageDesc;
	@JsonProperty("BuildingUsageYn")
	private String buildingUsageYn;
	@JsonProperty("BuildingType")
	private String buildingType;
	@JsonProperty("LocationName")
    private String     locationName;
	@JsonProperty("BuildingOccupationType")
	private String buildingOccupationType;
	@JsonProperty("ApartmentOrBorder")
	private String apartmentOrBorder;
	@JsonProperty("WithoutInhabitantDays")
	private String withoutInhabitantDays;
	@JsonProperty("BuildingCondition")
	private String buildingCondition;
	@JsonProperty("BuildingBuildYear")
	private String buildingBuildYear;
	@JsonProperty("BuildingAge")
	private String buildingAge;

	@JsonProperty("BuildingAreaSqm")
	private String buildingAreaSqm;
	@JsonProperty("BuildingSuminsured")
	private String buildingSuminsured;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EntryDate")
	private Date entryDate;

	@JsonProperty("CustomerId")
	private String customerId;

	@JsonProperty("QuoteNo")
	private String quoteNo;

	@JsonProperty("Createdby")
	private String createdBy;

	@JsonProperty("Status")
	private String status;

	@JsonProperty("Updatedby")
	private String updatedBy;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("UpdatedDate")
	private Date updatedDate;

	@JsonProperty("BuildingAddress")
	private String buildingAddress;

	@JsonProperty("SectionDesc")
	private String sectionDesc;

	 @JsonProperty("EndorsementType") //EndorsementType
	    private Integer    endorsementType ;

	    @JsonProperty("EndorsementTypeDesc") // EndorsementTypeDesc
	    private String     endorsementTypeDesc ;
	   
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
	    @JsonProperty("PolicyNo")
	    private String policyNo;
	    
		@JsonProperty("MiningPlantSi")
		private String miningPlantSi;
		
		@JsonProperty("NonminingPlantSi")
		private String nonminingPlantSi;
		
		@JsonProperty("GensetsSi")
		private String gensetsSi;
		
		@JsonProperty("EquipmentSi")
		private String equipmentSi;
}
