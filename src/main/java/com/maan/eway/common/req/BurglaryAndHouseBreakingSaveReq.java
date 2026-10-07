package com.maan.eway.common.req;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BurglaryAndHouseBreakingSaveReq {

	
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
	
	@JsonProperty("BuildingOwnerYn")
	private String     buildingOwnerYn;
	
	@JsonProperty("BurglarySi")
	private String burglarySi;
//	
	@JsonProperty("FirstLossPercentId")
	private String firstLossPercentId;
	
	 @JsonProperty("NatureOfTradeId")
     private String natureOfTradeId;
	    
	    @JsonProperty("InsuranceForId")
	    private List<String> insuranceForId ;
	    
	    @JsonProperty("InternalWallType")
	    private String internalWallType;
	    
	    @JsonProperty("CeilingType")
	    private String ceilingType;
	    
	    @JsonProperty("StockInTradeSi")
	    private String stockInTradeSi ;
	    
	    @JsonProperty("GoodsSi")
	    private String goodsSi;
	    
	    @JsonProperty("FurnitureSi")
	    private String furnitureSi;
	    
	    @JsonProperty("ApplianceSi")
	    private String applianceSi;
	    
	    @JsonProperty("CashValueablesSi")
	    private String cashValueablesSi;
	    
	    @JsonProperty("StockLossPercent")
		private String stockLossPercent ;
	  
	    @JsonProperty("GoodsLossPercent")
	  private String goodsLossPercent;
	  
	    @JsonProperty("FurnitureLossPercent")
	  private String furnitureLossPercent;
	  
	    @JsonProperty("ApplianceLossPercent")
	  private String applianceLossPercent;
	  
	    @JsonProperty("CashValueablesLossPercent")
	  private String cashValueablesLossPercent;
	  
	    
	    @JsonProperty("OccupiedYear")
	    private String occupiedYear;
	    
	    @JsonProperty("ShowWindow")
	    private String showWindow;
	    
	    @JsonProperty("FrontDoors")
	    private String frontDoors;
	    
	    @JsonProperty("BackDoors")
	    private String backDoors;
	    
	    @JsonProperty("WindowsMaterialId")
	    private String windowsMaterialId;
	    
	    @JsonProperty("DoorsMaterialId")
	    private String doorsMaterialId; 
	    
	    @JsonProperty("NightLeftDoor")
	    private String nightLeftDoor; 
	    
	    @JsonProperty("BuildingOccupied")
	    private String buildingOccupied; 
	    
	    @JsonProperty("WatchmanGuardHours")
	    private String watchmanGuardHours;
	    
	    @JsonProperty("AccessibleWindows")
	    private String accessibleWindows;
	    
	    @JsonProperty("LocationName")
	    private String     locationName;
	    
	    @JsonProperty("Address")
	    private String address;

	    @JsonProperty("IndustryType")
	    private String   industryType;
		
		@JsonProperty("IndustryTypeDesc")
	    private String   industrytypedesc;
		
		@JsonProperty("CoveringDetails")
	    private String  CoveringDetails ;
		
		@JsonProperty("DescriptionOfRisk")
	    private String  descriptionOfRisk ;

		@JsonProperty("RegionName")
	    private String  regionName ;
		
		@JsonProperty("DistrictName")
	    private String  districtName ;
		
		@JsonProperty("RegionCode")
	    private String  regionCode ;
		
		@JsonProperty("DistrictCode")
	    private String  districtCode ;

	    
	    @JsonProperty("TrapDoors")
	    private String trapDoors ;
	    
	    @JsonProperty("Status")
		private String status;
	    
		@JsonProperty("WallType")
	    private String     wallType;
		
		@JsonProperty("RoofType")
	    private String     roofType;
		
		@JsonProperty("BuildingBuildYear")
	    private String    buildingBuildYear ;
		
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

		
	  
}
