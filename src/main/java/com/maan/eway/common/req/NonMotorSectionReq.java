package com.maan.eway.common.req;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class NonMotorSectionReq {
	@JsonProperty("RiskId")
	private String riskId;
	@JsonProperty("SectionId")
	private String sectionId;
	@JsonProperty("SectionName")
	private String sectionName;
	@JsonProperty("OccupationId")
	private String occupationId;
	//Common For all Products in Non Motor
	@JsonProperty("SumInsured")
	private String sumInsured;

	// Building Request

	@JsonProperty("BuildingAreaSqm")
	private BigDecimal buildingAreaSqm;
	@JsonProperty("BuildingFloors")
	private Integer buildingFloors;
//	@JsonProperty("BuildingSumInsured")
//	private String buildingSumInsured;
	@JsonProperty("OutbuildConstructType")
	private String internalWallType;
	@JsonProperty("WallType")
	private String wallType;
	@JsonProperty("RoofType")
	private String roofType;
	@JsonProperty("BuildingUsageId")
	private String buildingUsageId;
	@JsonProperty("BuildingUsageDesc")
	private String buildingUsageDesc;
	@JsonProperty("BuildingOwnerYn")
	private String buildingOwnerYn;
	@JsonProperty("BuildingBuildYear")
	private String buildingBuildYear;
	@JsonProperty("TypeOfProperty")
	private String typeOfProperty;
//	@JsonProperty("NoofMonthForConstruction")
//	private Integer noOfMonthsConstruction;
//	@JsonProperty("WaterTankSi")
//	private String waterTankSi;
//	@JsonProperty("ArchitectsSi")
//	private String architectsSi;
//	@JsonProperty("LossOfRentSi")
//	private String lossOfRentSi;
//	@JsonProperty("GroundUndergroundSi")
//	private String groundUndergroundSi;
//	@JsonProperty("LocationName")
//	private String locationName;



	// Content Info
	@JsonProperty("ContentId")
	private String contentId;
	@JsonProperty("ContentDesc")
	private String contentDesc;
	
	@JsonProperty("SerialNo")
	private String serialNo;
	@JsonProperty("Description")
	private String description;

	// Suminsured
	/*
	 * @JsonProperty("ContentSuminsured") private String contentSuminsured;
	 */
	/*
	 * @JsonProperty("JewellerySi") private String jewellerySi;
	 */
	/*
	 * @JsonProperty("PaitingsSi") private String paitingsSi;
	 */
	/*
	 * @JsonProperty("CarpetsSi") private String carpetsSi;
	 */
	/*
	 * @JsonProperty("ElecEquipSuminsured") private String elecEquipSuminsured;
	 */
	/*
	 * @JsonProperty("AllriskSumInsured") private String allriskSuminsured;
	 */
	/*
	 * @JsonProperty("MiningPlantSi") private String miningPlantSi;
	 */
	/*
	 * @JsonProperty("NonminingPlantSi") private String nonminingPlantSi;
	 */
	/*
	 * @JsonProperty("GensetsSi") private String gensetsSi;
	 */

	/*
	 * @JsonProperty("DomesticServentSi") private String domesticServantSi;
	 */
	// Domestic servant type
	@JsonProperty("DomesticServantType")
	private String professionalType;
	
	@JsonProperty("Count")
	private String count;
	// PersonalAccident
	@JsonProperty("RelationType")
	private String relationType;
	@JsonProperty("PersonalAccidentSi")
	private String personalAccidentSi;
	
	@JsonProperty("NickName")
	private String     nickName ;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("dob")
	private Date       dob ;
	// Personal Liability
//	@JsonProperty("PersonalLiabilitySi")
//	private String personalLiabilitySi;

	@JsonProperty("IndustryId")
	private String industryId;
	// Fire
	@JsonProperty("IndustryType")
	private String industryType;
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
	private String districtCode;
	@JsonProperty("BusinessInterruption")
	private String businessInterruption;

	@JsonProperty("CategoryId")
	private String categoryId;
	@JsonProperty("CategoryDesc")
	private String categoryDesc;
	// Bond
	@JsonProperty("BondType")
	private String bondType;
	@JsonProperty("BondYear")
	private String bondYear;
//	@JsonProperty("BondSuminsured")
//	private String bondSumInsured;
	// Money
//	@JsonProperty("FirePlantSi")
//	private String firePlantSi;
//	Estimated annual cash carryings---MoneyAnnualEstimate
//	Cash in transit limit---MoneyMajorLoss
//	Custody of collectors---MoneyCollector
//	Safe during working hours---MoneySafeLimit
//	safe outside working hours---MoneyOutofSafe
//	Residence of director or partner---MoneyDirectorResidence
//	Value of safe---StrongroomSi
	/*
	 * @JsonProperty("MoneySafeLimit") private String moneySafeLimit;
	 * 
	 * @JsonProperty("MoneyOutofSafe") private String moneyOutofSafe; // Safe
	 * Outside Working Hours
	 * 
	 * @JsonProperty("StrongroomSi") private String strongroomSi;
	 * 
	 * @JsonProperty("MoneyDirectorResidence") private String
	 * moneyDirectorResidence;
	 * 
	 * @JsonProperty("MoneyCollector") private String moneyCollector;
	 * 
	 * @JsonProperty("MoneyAnnualEstimate") private String moneyAnnualEstimate;
	 * 
	 * @JsonProperty("MoneyMajorLoss") private String moneyMajorLoss; // tanzaniya
	 * 
	 * @JsonProperty("MoneyInTransit") private String moneyInTransit;
	 */
	/*
	 * @JsonProperty("BurglarySi") private String burglarySi;
	 */
	/*
	 * @JsonProperty("EstimatedAnnualCarryings") private String
	 * estimatedAnnualCarryings;
	 */
//	@JsonProperty("StrongRoom")
//	private String strongRoom;
//	@JsonProperty("Premises")
//	private String premises;
//	@JsonProperty("MoneyInSafe")
//	private String moneyInSafe;
	// Employers liablity
//	@JsonProperty("EmpLiabilitySi")
//	private String empLiabilitySi;
	@JsonProperty("TotalNoOfEmployees")
	private String totalNoOfEmployees;
	// Burglary
	@JsonProperty("FirstLossPercentId")
	private String firstLossPercentId;


	//
	@JsonProperty("OtherOccupation")
	private String otherOccupation;
	// Fidelity
//	@JsonProperty("FidEmpSi")
//	private String fidEmpSi;
	@JsonProperty("FidEmpCount")
	private String fidEmpCount;
	@JsonProperty("FirstLossPayee")
	private String firstLossPayee;
	@JsonProperty("IndemityPeriod")
	private String indemityPeriod;
	
	@JsonProperty("IndemnityType")
	private String indemnityType;
	@JsonProperty("IndemnityTypeDesc")
    private String indemnityTypeDesc;
	
	@JsonProperty("CoverId")
    private Integer coverId ;
	
	
	@JsonProperty("BorrowerType")
	private String borrowerType;
	@JsonProperty("BorrowerTypeDesc")
    private String borrowerTypeDesc ;
	@JsonProperty("CollateralName")
	private String collateralName;
	@JsonProperty("CollateralYn")
    private String  collateralYn ;
	//Phoenix -
	@JsonProperty("GeographicalCoverage")//Minimum limit per
	private String geographicalCoverage;
	@JsonProperty("ModeOfTransport") // trip per amount
	private String modeOfTransport;
	
	@JsonProperty("NoOfClaim")
	private String noOfClaim;
	
	@JsonProperty("NoOfClaimDesc")
	private String noOfClaimDesc;
	
	@JsonProperty("GrossProfitLc")
	private String grossProfitLc;
	
	@JsonProperty("Age")
	private Integer age ;
	
	@JsonProperty("Gender")
	private String Gender;
	

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
