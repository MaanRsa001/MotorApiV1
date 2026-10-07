package com.maan.eway.common.req;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
 

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BuildingSaveReq {

	@JsonProperty("RequestReferenceNo")
    private String     requestReferenceNo ;
	@JsonProperty("LocationId")
    private String    locationId   ;
	@JsonProperty("CustomerReferenceNo")
    private String     customerReferenceNo ;

    //----------------------------------------------------------------------
    // ENTITY DATA FIELDS 
    //----------------------------------------------------------------------    
	@JsonProperty("ProductId")
    private String    productId    ;
	@JsonProperty("SectionId")
    private List<String> sectionId    ;
	@JsonProperty("InsuranceId")
    private String     companyId    ;
	@JsonProperty("Status")
    private String     status;
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
	@JsonProperty("BuildingPurposeId")
    private String     buildingPurposeId;
	@JsonProperty("BuildingUsageId")
    private String     buildingUsageId;

	
	
	@JsonProperty("BuildingType")
	private String     buildingType;

	@JsonProperty("BuildingOwnerYn")
	private String     buildingOwnerYn;

	
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
	@JsonProperty("AllriskSumInsured")
    private String     allriskSuminsured ;
	
	@JsonProperty("WallType")
    private String     wallType;
	
	@JsonProperty("RoofType")
    private String     roofType;
	
	@JsonProperty("ContentSuminsured")
    private String     contentSuminsured ;
	@JsonProperty("PersonalIntermediarySuminsured")
    private String     personalIntermediarySuminsured ;

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
	
	@JsonProperty("UserType")
    private String     userType;
	
	@JsonProperty("BankCode")
    private String    bankCode;
	
	
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


	@JsonProperty("DomesticPackageYn")
    private String    domesticPackageYn;

	@JsonProperty("CategoryId")
    private String    categoryId;

	@JsonProperty("OccupationType")
    private String    occupationType;
	
	@JsonProperty("PersonalAccSuminsured")
    private String    personalAccSuminsured;

	@JsonProperty("Count")
    private String    count;
	
	@JsonProperty("SourceTypeId")
	private String sourceTypeId;
	
	@JsonProperty("SourceType")
	private String sourceType;
	
//	@JsonProperty("OccupationDetails")
//    private List<OccupationReqClass>  occupation;
	
	@JsonProperty("CustomerCode")
    private String     customerCode;
	@JsonProperty("BdmCode")
    private String     bdmCode   ;
	
	@JsonProperty("CommissionType")
    private String    commissionType;

	
	// For SME Product
	
	@JsonProperty("ElecEquipSuminsured")
    private String     elecEquipSuminsured ;
		

	@JsonProperty("MoneySinglecarrySuminsured")
    private String   moneySinglecarrySuminsured ;
		
	@JsonProperty("MoneyAnnualcarrySuminsured")
    private String   moneyAnnualcarrySuminsured ;
	
	@JsonProperty("MoneyInsafeSuminsured")
    private String   moneyInsafeSuminsured ;
	
	@JsonProperty("FidelityAnyoccuSuminsured")
    private String   fidelityAnyoccuSuminsured ;
	
	@JsonProperty("FidelityAnnualSuminsured")
    private String   fidelityAnnualSuminsured ;
	
	@JsonProperty("TpliabilityAnyoccuSuminsured")
    private String tpliabilityAnyoccuSuminsured ;
	
	@JsonProperty("EmpliabilityAnnualSuminsured")
    private String empliabilityAnnualSuminsured ;
	
	@JsonProperty("EmpliabilityExcessSuminsured")
    private String empliabilityExcessSuminsured ;

	@JsonProperty("GoodsSinglecarrySuminsured")
    private String   goodsSinglecarrySuminsured ;

	@JsonProperty("GoodsTurnoverSuminsured")
    private String   goodsTurnoverSuminsured ;
	
    
    @JsonProperty("LiabilityOccupationId") 
    private String     liabilityOccupationId ;
	
	@JsonProperty("IndustryId")
    private String   industryId;
	
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
    
  
    
    
    @JsonProperty("PolicyNo")
    private String policyNo;
    
    
    // Burglary Product

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
    
    @JsonProperty("Address")
    private String address;
    
    @JsonProperty("RegionCode")
    private String regionCode;
     
    @JsonProperty("DistrictCode")
    private String districtCode ;
    
    @JsonProperty("TrapDoors")
    private String trapDoors ;
    
    
    // Money Related
    
    @JsonProperty("CashInHandDirectors")
    private String cashInHandDirectors;
    
    @JsonProperty("CashInTransit")
    private String cashInTransit;
    
    @JsonProperty("CashInHandEmployees")
    private String cashInHandEmployees;
    
    @JsonProperty("CashInSafe")
    private String cashInSafe;
    
    @JsonProperty("CashInPremises")
    private String cashInPremises;
    
    @JsonProperty("RevenueFromStamps")
    private String revenueFromStamps;
    
    @JsonProperty("MoneyInSafeBusiness")
    private String moneyInSafeBusiness;
    
    @JsonProperty("MoneyOutSafeBusiness")
    private String moneyOutSafeBusiness;
    
    @JsonProperty("MoneyInPremises")
    private String moneyInPremises;
    
    @JsonProperty("MoneyInLocker")
    private String moneyInLocker;
    
    @JsonProperty("MachineEquipSi")
    private String machineEquipSi ;

    @JsonProperty("PlateGlassSi")
    private String plateGlassSi ;
    
    @JsonProperty("AccDamageSi")
    private String accDamageSi ;
    
    @JsonProperty("FirstLossPercentId")
    private String firstLossPercentId ;
    
    @JsonProperty("BurglarySi")
    private String burglarySi ;
    
    @JsonProperty("PowerPlantSi")
    private String powerPlantSi ;
    
    @JsonProperty("ElecMachinesSi")
    private String elecMachinesSi ;
    
    @JsonProperty("EquipmentSi")
    private String equipmentSi ;
    
    @JsonProperty("GeneralMachineSi")
    private String generalMachineSi ;
    
    @JsonProperty("ManuUnitsSi")
    private String manuUnitsSi ;
    
    @JsonProperty("BoilerPlantsSi")
    private String boilerPlantsSi ;
    
    
    //--- ENTITY DATA FIELDS 
   
    @JsonProperty("SumInsured")
    private String sumInsured;
    

    @JsonProperty("CustomerName")
    private String     customerName;
    
    @JsonProperty("TotalNoOfEmployees")
    private String     totalNoOfEmployees;
    
    
    @JsonProperty("LiabilitySi")
    private String liabilitySi;
    
    @JsonProperty("FidEmpCount")
    private String       fidEmpCount;
    
    @JsonProperty("FidEmpSi")
    private String       fidEmpSi;
    
    @JsonProperty("EmpLiabilitySi")
    private String empLiabilitySi;

	@JsonProperty("TiraCoverNoteNo")
    private String     tiraCoverNoteNo ;
	

}
