package com.maan.eway.common.res;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class NonMotorSectionRes {
	@JsonProperty("RiskId")
	private String riskId;
	@JsonProperty("SectionId")
	private String sectionId;
	@JsonProperty("SectionName")
	private String sectionDesc;
	@JsonProperty("OccupationId")
	private String occupationId;
//	@JsonProperty("BuildingSumInsured")
//	 private BigDecimal     buildingSuminsured ;
	@JsonProperty("OutbuildConstructType")
	private String internalWallType;
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
	@JsonProperty("TypeOfProperty")
	private String typeOfProperty;
	@JsonProperty("ContentId")
	private String contentId;
	@JsonProperty("ContentDesc")
	private String contentDesc;
	@JsonProperty("LocationName")
	private String locationName;
	@JsonProperty("SerialNo")
	private String serialNo;
	@JsonProperty("Description")
	private String description;
//	@JsonProperty("MiningPlantSi")
//	private String miningPlantSi;
//	@JsonProperty("NonminingPlantSi")
//	private String nonminingPlantSi;
//	@JsonProperty("GensetsSi")
//	private String gensetsSi;

	// Domestic servant type
	@JsonProperty("DomesticServantType")
	private String professionalType;
	@JsonProperty("DomesticServantTypeDesc")
	private String professionalTypeDesc;
//	@JsonProperty("DomesticServentSi")
//	private BigDecimal domesticServentSi;
	@JsonProperty("Count")
	private Integer count;
	// PersonalAccident
	@JsonProperty("RelationType")
	private String relationType;
	@JsonProperty("RelationTypeDesc")
	private String relationTypeDesc;
//	@JsonProperty("PersonalAccidentSi")
//	private String personalAccidentSi;
//	// Personal Liability
//	@JsonProperty("PersonalLiabilitySi")
//	private BigDecimal personalLiabilitySi;

	// Fire
	@JsonProperty("IndustryId")
	private Integer    industryId ;
	@JsonProperty("IndustryType")
	private String    Industrytype ;
	@JsonProperty("IndustryTypeDesc")
	private String industrytypedesc;
	@JsonProperty("OccupationDesc")
	private String occupationDesc;
	@JsonProperty("CoveringDetails")
	private String CoveringDetails;
	@JsonProperty("DescriptionOfRisk")
	private String descriptionOfRisk;
	@JsonProperty("RegionName")
	private String regionName;
	@JsonProperty("DistrictName")
	private String districtName;
	@JsonProperty("RegionCode")
	private String regionCode;
	@JsonProperty("DistrictCode")
	private String     districtCode ;
	@JsonProperty("BusinessInterruption")
	private String businessInterruption;
	@JsonProperty("FirePlantSi")
	private String firePlantSi;
	@JsonProperty("CategoryId")
	private String categoryId;
	@JsonProperty("CategoryDesc")
	private String categoryDesc;

	// Money
	@JsonProperty("MoneySafeLimit")
	private BigDecimal moneySafeLimit;
	@JsonProperty("StrongroomSi")
	private BigDecimal strongroomSi;
	@JsonProperty("LossOfRentSi")
	private BigDecimal lossOfRentSi;
	@JsonProperty("ArchitectsSi")
	private BigDecimal architectsSi;
	@JsonProperty("WaterTankSi")
	private BigDecimal waterTankSi;
	@JsonProperty("MoneyMajorLoss")
	private BigDecimal moneyMajorLoss;
	@JsonProperty("MoneyAnnualEstimate")
	private BigDecimal moneyAnnualEstimate;
	@JsonProperty("MoneyCollector")
	private BigDecimal moneyCollector;
	@JsonProperty("MoneyDirectorResidence")
	private BigDecimal moneyDirectorResidence;
	@JsonProperty("MoneyOutofSafe")
	private BigDecimal moneyOutofSafe;
	// tanzaniya
	@JsonProperty("MoneyInTransit")
	private BigDecimal moneyInTransit;
	@JsonProperty("EstimatedAnnualCarryings")
	private BigDecimal estimatedAnnualCarryings;
	@JsonProperty("StrongRoom")
	private BigDecimal strongRoom;
	@JsonProperty("Premises")
	private String premises;
	@JsonProperty("MoneyInSafe")
	private BigDecimal moneyInSafe;

//---------------------------------------------
	@JsonProperty("DomesticPackageYn")
	private String domesticPackageYn;
	@JsonProperty("BuildingFloors")
	private Integer buildingFloors;
	@JsonProperty("BuildingUsageYn")
	private String buildingUsageYn;
	@JsonProperty("BuildingType")
	private String buildingType;
	@JsonProperty("BuildingOccupationType")
	private String buildingOccupationType;
	@JsonProperty("ApartmentOrBorder")
	private String apartmentOrBorder;
	@JsonProperty("WithoutInhabitantDays")
	private Integer withoutInhabitantDays;
	@JsonProperty("BuildingCondition")
	private String buildingCondition;
	@JsonProperty("BuildingAge")
	private Integer buildingAge;
	@JsonProperty("BuildingAreaSqm")
	private BigDecimal buildingAreaSqm;
//	@JsonProperty("AllriskSuminsured")
//	private BigDecimal allriskSuminsured;
//	@JsonProperty("PersonalAccSuminsured")
//	private BigDecimal personalAccSuminsured;
//	@JsonProperty("ContentSuminsured")
//	private BigDecimal contentSuminsured;
	@JsonProperty("OccupationType")
	private String occupationType;
//	@JsonProperty("ElecEquipSuminsured")
//	private BigDecimal elecEquipSuminsured;
//	@JsonProperty("GoodsSinglecarrySuminsured")
//	private BigDecimal goodsSinglecarrySuminsured;
//	@JsonProperty("GoodsSi")
//	private BigDecimal goodsSi;
	@JsonProperty("Address")
	private String address;
//	@JsonProperty("PlateGlassSi")
//	private BigDecimal plateGlassSi;
//	@JsonProperty("MachineEquipSi")
//	private BigDecimal machineEquipSi;
//	@JsonProperty("BurglarySi")
//	private BigDecimal burglarySi;
//	@JsonProperty("PowerPlantSi")
//	private BigDecimal powerPlantSi;
//	@JsonProperty("ElecMachinesSi")
//	private BigDecimal elecMachinesSi;
//	@JsonProperty("EquipmentSi")
//	private BigDecimal equipmentSi;
//	@JsonProperty("GeneralMachineSi")
//	private BigDecimal generalMachineSi;
//	@JsonProperty("ManuUnitsSi")
//	private BigDecimal manuUnitsSi;
//	@JsonProperty("BoilerPlantsSi")
//	private BigDecimal boilerPlantsSi;
	@JsonProperty("OtherOccupation")
	private String otherOccupation;
	@JsonProperty("CoveringDetails")
	private String coveringDetails;
//	@JsonProperty("BondSuminsured")
//	private BigDecimal bondSuminsured;
	@JsonProperty("BondType")
	private String bondType;
	@JsonProperty("BondYear")
	private String bondYear;
	@JsonProperty("FirstLossPayee")
	private String firstLossPayee;
//	@JsonProperty("GroundUndergroundSi")
//	private BigDecimal groundUndergroundSi;
	@JsonProperty("RenewalDateYn")
	private String renewalDateYn;
	@JsonProperty("GeographicalCoverage")
	private String geographicalCoverage;
	@JsonProperty("TransportedBy")
	private String transportedBy;
	@JsonProperty("ModeOfTransport")
	private String modeOfTransport;
//	@JsonProperty("CarpetsSi")
//	private BigDecimal carpetsSi;
//	@JsonProperty("PaitingsSi")
//	private BigDecimal paitingsSi;
//	@JsonProperty("JewellerySi")
//	private BigDecimal jewellerySi;
	@JsonProperty("PlateGlassDesc")
	private String plateGlassDesc;
	@JsonProperty("PlateGlassType")
	private String plateGlassType;
	@JsonProperty("IndemityPeriod")
	private String indemityPeriod;
	@JsonProperty("IndemityPeriodDesc")
	private String indemityPeriodDesc;
	
	
	//Common Response
//	@JsonProperty("OriginalRiskId")
//	private Integer originalRiskId;
	@JsonProperty("SumInsured")
	private String sumInsured;
	@JsonProperty("Status")
	private String status;
	@JsonProperty("OldReqRefNo")
	private String oldReqRefNo;
	@JsonProperty("BenefitCoverMonth")
	private Integer benefitCoverMonth;
	@JsonProperty("SalaryPerAnnum")
	private BigDecimal salaryPerAnnum;
	@JsonProperty("PolicyPeriod")
	private Integer policyPeriod;
	@JsonProperty("JobJoiningMonth")
	 private String     jobJoiningMonth ;
	@JsonProperty("BetweenDiscontinued")
	private String betweenDiscontinued;
	@JsonProperty("EthicalWorkInvolved")
	private String ethicalWorkInvolved;
	@JsonProperty("NatureOfBusinessId")
	private Long natureOfBusinessId;
	@JsonProperty("NatureOfBusinessDesc")
	private String natureOfBusinessDesc;
	@JsonProperty("TotalNoOfEmployees")
	private Long totalNoOfEmployees;
	@JsonProperty("TotalExcludedEmployees")
	private Long totalExcludedEmployees;
	@JsonProperty("TotalRejoinedEmployees")
	private Long totalRejoinedEmployees;
	@JsonProperty("AccountOutstandingEmployees")
	private Long accountOutstandingEmployees;
	@JsonProperty("TotalOutstandingAmount")
	private BigDecimal totalOutstandingAmount;
	@JsonProperty("AccountAuditentType")
	private Integer accountAuditentType;
	@JsonProperty("AuditentTypeDesc")
	private String auditentTypeDesc;
//	@JsonProperty("LiabilitySi")
//	private BigDecimal liabilitySi;
	@JsonProperty("FidEmpCount")
	private BigDecimal fidEmpCount;
//	@JsonProperty("FidEmpSi")
//	private BigDecimal fidEmpSi;
//	@JsonProperty("EmpLiabilitySi")
//	private BigDecimal empLiabilitySi;
	@JsonProperty("PersonalLiabilityOccupation")
	private String personalLiabilityOccupation;
	@JsonProperty("PersonalLiabilityCategory")
	private BigDecimal personalLiabilityCategory;
//	@JsonProperty("AooSuminsured")
//	private BigDecimal aooSuminsured;
//	@JsonProperty("AggSuminsured")
//	private BigDecimal aggSuminsured;
//	@JsonProperty("ProductTurnoverSi")
//	private BigDecimal productTurnoverSi;
//	@JsonProperty("AnyAccidentSi")
//	private BigDecimal anyAccidentSi;
//	@JsonProperty("InsurancePeriodSi")
//	private BigDecimal insurancePeriodSi;
//	@JsonProperty("TtdSumInsured")
//	private Integer ttdSumInsured;
//	@JsonProperty("MeSumInsured")
//	private Integer meSumInsured;
//	@JsonProperty("FeSumInsured")
//	private Integer feSumInsured;
	@JsonProperty("NickName")
	private String nickName;
	@JsonProperty("Age")
	private Integer age;
//	@JsonProperty("PtdSumInsured")
//	private Integer ptdSumInsured;
	@JsonProperty("GroupId")
	private Integer groupId;
	@JsonProperty("IndemnityType")
	private String indemnityType;
	@JsonProperty("IndemnityTypeDesc")
	private String indemnityTypeDesc;
//	@JsonProperty("IndemnitySuminsured")
//	private BigDecimal indemnitySuminsured;
	@JsonProperty("FirstLossPercentId")
	private Integer firstLossPercentId;
	@JsonProperty("FirstLossPercent")
	private Integer    firstLossPercent ;
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("dob")
	private Date       dob ;
	
	@JsonProperty("CoverId")
	private Integer coverid;
	
	@JsonProperty("BorrowerType")
	private String borrowerType;
	@JsonProperty("BorrowerTypeDesc")
    private String     borrowerTypeDesc ;
	@JsonProperty("CollateralName")
	private String collateralName;
	@JsonProperty("CollateralYn")
    private String  collateralYn ;
	@JsonProperty("GrossProfitLc")
	private BigDecimal grossProfitLc;
	
	@JsonProperty("NoOfClaim")
	private String noOfClaim;
	
	@JsonProperty("NoOfClaimDesc")
	private String noOfClaimDesc;
	
	@JsonProperty("Gender")
	private String gender;
	

	@JsonProperty("Param1")
	private String param1;
	@JsonProperty("Param2")
	private String param2;
	@JsonProperty("Param3")
	private String param3;
	@JsonProperty("Param4")
	private String param4;
	@JsonProperty("Param5")
	private String param5;
	@JsonProperty("Param6")
	private String param6;
	@JsonProperty("Param7")
	private String param7;
	@JsonProperty("Param8")
	private String param8;
	@JsonProperty("Param9")
	private String param9;
	@JsonProperty("Param10")
	private String param10;
	@JsonProperty("Param11")
	private String param11;
	@JsonProperty("Param12")
	private String param12;
	@JsonProperty("Param13")
	private String param13;
	@JsonProperty("Param14")
	private String param14;
	@JsonProperty("Param15")
	private String param15;
	
	@JsonProperty("EndtOpdt")
	private String endtOpdt;

}
