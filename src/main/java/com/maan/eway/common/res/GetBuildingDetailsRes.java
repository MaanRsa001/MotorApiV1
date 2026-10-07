package com.maan.eway.common.res;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetBuildingDetailsRes {

	@JsonProperty("RequestReferenceNo")
    private String     requestReferenceNo ;
	@JsonProperty("LocationId")
    private String    locationId   ;
	
	@JsonProperty("CustomerReferenceNo")
    private String     customerReferenceNo ;
	@JsonProperty("ProductId")
    private String    productId    ;
	@JsonProperty("InsuranceId")
    private String     companyId    ;
	@JsonProperty("BranchCode")
    private String     branchCode   ;
	@JsonProperty("InbuildConstructType")
    private String     inbuildConstructType ;
	@JsonProperty("BuildingFloors")
    private String buildingFloors ;
	@JsonProperty("OutbuildConstructType")
    private String     outbuildConstructType ;
	@JsonProperty("BuildingUsageYn")
    private String     buildingUsageYn ;
	@JsonProperty("BuildingPurpose")
    private String     buildingPurpose;
	@JsonProperty("BuildingUsageDesc")
    private String     buildingUsageDesc ;
	@JsonProperty("BuildingPurposeId")
    private String     buildingPurposeId;
	@JsonProperty("BuildingUsageId")
    private String     buildingUsageId;

	@JsonProperty("SectionId")
    private Object sectionId    ;
	
	@JsonProperty("WallType")
    private String     wallType;
	
	@JsonProperty("RoofType")
    private String     roofType;
	
	@JsonProperty("BuildingType")
	private String     buildingType;

	@JsonProperty("BuildingOwnerYn")
	private String     buildingOwnerYn;
	@JsonProperty("PersonalIntermediarySuminsured")
    private String     personalIntermediarySuminsured ;
	
	
	@JsonProperty("BuildingOccupationType")
    private String     buildingOccupationType ;
	@JsonProperty("WithoutInhabitantDays")
    private String    withoutInhabitantDays ;

	@JsonProperty("BuildingCondition")
    private String     buildingCondition ;
	@JsonProperty("BuildingBuildYear")
    private String    buildingBuildYear ;
	
	@JsonProperty("BuidingAreaSqm")
    private String     buidingAreaSqm ;
	@JsonProperty("BuildingSuminsured")
    private String     buildingSuminsured ;
//	@JsonProperty("AllriskSumInsured")
//   private String     allriskSuminsured ;
	@JsonProperty("ContentSuminsured")
    private String     contentSuminsured ;
	@JsonProperty("WorkmenCompSuminsured")
    private String    workmenCompSuminsured ;

	@JsonProperty("Createdby")
    private String     createdBy    ;
	
	@JsonProperty("AcexecutiveId")
    private String    acExecutiveId ;
	@JsonProperty("ApplicationId")
    private String     applicationId ;
	@JsonProperty("BrokerCode")
    private String     brokerCode   ;
	@JsonProperty("SubUsertype")
    private String     subUserType  ;
	@JsonProperty("LoginId")
    private String     loginId      ;
	@JsonProperty("AgencyCode")
    private String     agencyCode   ;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("PolicyStartDate")
    private Date       policyStartDate ;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("PolicyEndDate")
    private Date       policyEndDate ;
	
	@JsonProperty("Currency")
    private String     currency     ;
	@JsonProperty("ExchangeRate")
    private String     exchangeRate ;
	@JsonProperty("BrokerBranchCode")
    private String     brokerBranchCode  ;
	
	@JsonProperty("Havepromocode")
    private String     havepromocode;
	
	@JsonProperty("Promocode")
    private String     promocode;
	
	@JsonProperty("InsuranceType")
    private String    insuranceType;
	
	@JsonProperty("OccupationType")
    private String    occupationType;

	@JsonProperty("OccupationTypeDesc")
    private String    occupationTypeDesc;

	@JsonProperty("DomesticPackageYn")
    private String    domesticPackageYn;

	@JsonProperty("CategoryId")
    private String    categoryId;

	@JsonProperty("BankCode")
    private String    bankCode;

	@JsonProperty("SourceTypeId")
	private String sourceTypeId;
	
	@JsonProperty("SourceType")
	private String sourceType;
	
	
	@JsonProperty("CustomerCode")
    private String     customerCode;
	@JsonProperty("BdmCode")
    private String     bdmCode   ;
	
//	@JsonProperty("OccupationDetails")
//    private List<OccupationReqClass>  occupation;
	
	@JsonProperty("PersonalAccSuminsured")
    private String    personalAccSuminsured;

	@JsonProperty("Count")
    private String    count;
	
	@JsonProperty("ElecEquipSuminsured")
    private String     elecEquipSuminsured ;

	@JsonProperty("GoodsSinglecarrySuminsured")
    private String   goodsSinglecarrySuminsured ;

	@JsonProperty("GoodsTurnoverSuminsured")
    private String   goodsTurnoverSuminsured ;
	
	@JsonProperty("IndustryId")
    private String  industryId;
	
	@JsonProperty("IndustryDesc")
    private String   industryDesc;
	@JsonProperty("Status")
    private String   status;
	
	   @JsonProperty("LiabilityOccupationId") 
	    private String     liabilityOccupationId ;
	   
	   @JsonProperty("LiabilityOccupationDesc") 
	    private String     liabilityOccupationDesc;

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
    private String endtCount ;
    @JsonProperty("EndtStatus") //EndtStatus
    private String     endtStatus ;   
    @JsonProperty("IsFinanceEndt") //IsFinanceEndt
    private String     isFinaceYn ;  
    @JsonProperty("EndtCategoryDesc") //EndtCategoryDesc
    private String     endtCategDesc ;
    

    @JsonProperty("EndorsementType") //EndorsementType
    private String    endorsementType ;

    @JsonProperty("EndorsementTypeDesc") // EndorsementTypeDesc
    private String     endorsementTypeDesc ;
	
    
    // Burglary Changes

    @JsonProperty("NatureOfTradeId")
    private String natureOfTradeId;
    
     @JsonProperty("NatureOfTradeDesc")
     private String     natureOfTradeDesc ;
    
     @JsonProperty("InsuranceForId")
     private Object insuranceForId ;
    
     @JsonProperty("InsuranceForDesc")
     private String     insuranceForDesc ;
    
     @JsonProperty("InternalWallType")
     private String internalWallType;
    
     @JsonProperty("InternalWallDesc")
     private String     internalWallDesc ;
    
     @JsonProperty("CeilingType")
     private String ceilingType;
    
     @JsonProperty("CeilingTypeDesc")
     private String     ceilingTypeDesc ;
    
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
    
     @JsonProperty("OccupiedYear")
     private String occupiedYear;
    
     @JsonProperty("showWindow")
     private String showWindow;
    
     @JsonProperty("FRONT_DOORS")
     private String frontDoors;
    
     @JsonProperty("BACK_DOORS")
     private String backDoors;
    
     @JsonProperty("WindowsMaterialId")
     private String windowsMaterialId;
    
     @JsonProperty("WindowsMaterialDesc")
     private String     windowsMaterialDesc;
    
     @JsonProperty("DoorsMaterialId")
     private String doorsMaterialId; 
    
     @JsonProperty("DoorsMaterialDesc")
     private String     doorsMaterialDesc;
    
     @JsonProperty("NightLeftDoor")
     private String nightLeftDoor; 
    
     @JsonProperty("NightLeftDoorDesc")
     private String     nightLeftDoorDesc ;
    
     @JsonProperty("BuildingOccupied")
     private String buildingOccupied; 
    
     @JsonProperty("BuildingOccupiedDesc")
     private String     buildingOccupiedDesc;
    
     @JsonProperty("WatchmanGuardHours")
     private String watchmanGuardHours;
    
     @JsonProperty("AccessibleWindows")
     private String accessibleWindows;
     
     @JsonProperty("TrapDoors")
     private String trapDoors;

  // Money Related
    
     
     @JsonProperty("FirstLossPercentId")
     private String firstLossPercentId;

     @JsonProperty("FirstLossPercent")
     private String firstLossPercent;
     
     @JsonProperty("TiraCoverNoteNo")
     private String     tiraCoverNoteNo ;
     
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

	@JsonProperty("CustomerName")
    private String     customerName;
	
	@JsonProperty("Suminsured")
    private String     suminsured ;

	}
