package com.maan.eway.common.res;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class NonMotorAssestRes {

	@JsonProperty("RiskId ")
	private Integer riskId;

	@JsonProperty("DomesticPackageYn ")
	private String domesticPackageYn;

	@JsonProperty("SectionId")
	private String sectionId;

	@JsonProperty("BuildingFloors")
	private Integer buildingFloors;

	@JsonProperty("BuildingUsageYn")
	private String buildingUsageYn;

	@JsonProperty("BuildingUsageId")
	private String buildingUsageId;

	@JsonProperty("BuildingOwnerYn")
	private String buildingOwnerYn;

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

	@JsonProperty("BuildingBuildYear")
	private Integer buildingBuildYear;

	@JsonProperty("BuildingAge")
	private Integer buildingAge;

	@JsonProperty("BuildingAreaSqm") 
	  private BigDecimal     buildingAreaSqm ;
	
	  
	@JsonProperty("BuildingSuminsured")
	private BigDecimal buildingSuminsured;

	@JsonProperty("AllriskSuminsured")
	private BigDecimal allriskSuminsured;

	@JsonProperty("PersonalAccSuminsured")
	private BigDecimal personalAccSuminsured;

	@JsonProperty("ContentSuminsured")
	private BigDecimal contentSuminsured;

	@JsonProperty("OccupationType")
	private String occupationType;

	@JsonProperty("CategoryId")
	private String categoryId;

	@JsonProperty("ElecEquipSuminsured")
	private BigDecimal elecEquipSuminsured;

	@JsonProperty("GoodsSinglecarrySuminsured")
	private BigDecimal goodsSinglecarrySuminsured;

	/*
	 * @JsonProperty("IndustryId") private Integer industryId;
	 */

	@JsonProperty("GoodsSi")
	private BigDecimal goodsSi;

	@JsonProperty("Address")
	private String address;

	@JsonProperty("RegionCode")
	private String regionCode;

	@JsonProperty("DistrictCode")
	private String districtCode;

	@JsonProperty("PlateGlassSi")
	private BigDecimal plateGlassSi;
	
	@JsonProperty("MachineEquipSi")
	private BigDecimal machineEquipSi;

	@JsonProperty("BurglarySi")
	private BigDecimal burglarySi;

	@JsonProperty("PowerPlantSi")
	private BigDecimal powerPlantSi;

	@JsonProperty("ElecMachinesSi")
	private BigDecimal elecMachinesSi;

	@JsonProperty("EquipmentSi")
	private BigDecimal equipmentSi;

	@JsonProperty("GeneralMachineSi")
	private BigDecimal generalMachineSi;

	@JsonProperty("ManuUnitsSi")
	private BigDecimal manuUnitsSi;

	@JsonProperty("BoilerPlantsSi")
	private BigDecimal boilerPlantsSi;

	@JsonProperty("OtherOccupation")
	private String otherOccupation;

	@JsonProperty("CoveringDetails")
	private String coveringDetails;

	@JsonProperty("DescriptionOfRisk")
	private String descriptionOfRisk;

	@JsonProperty("BondSuminsured")
	private BigDecimal bondSuminsured;

	@JsonProperty("BondType")
	private String bondType;

	@JsonProperty("BondYear")
	private String bondYear;

	@JsonProperty("ContentId")
	private String contentId;

	@JsonProperty("LocationName")
	private String locationName;

	@JsonProperty("SerialNo")
	private String serialNo;

	@JsonProperty("BusinessInterruption")
	private String businessInterruption;
	
	@JsonProperty("FirstLossPayee")
	private String  firstLossPayee;
	
	@JsonProperty("GroundUndergroundSi")
	 private BigDecimal     groundUndergroundSi ;
	  
	@JsonProperty("RenewalDateYn")
	 private String     renewalDateYn ;
	
	@JsonProperty("GeographicalCoverage")
    private String     geographicalCoverage ;
	
	@JsonProperty("TransportedBy")
	private String     transportedBy ;

	@JsonProperty("ModeOfTransport")
	private String     modeOfTransport ;
	
	@JsonProperty("CarpetsSi")
 private BigDecimal     carpetsSi ;
	 
	@JsonProperty("PAITINGSSI")
	private BigDecimal     paitingsSi ;
	
	
	@JsonProperty("JewellerySi")
	    private BigDecimal     jewellerySi ;
	
	@JsonProperty("LossOfRentSi")
	    private BigDecimal     lossOfRentSi ;

	@JsonProperty("ARCHITECTS_SI")
	    private BigDecimal     architectsSi ;
	

	@JsonProperty("WaterTankSi")
	  private BigDecimal     waterTankSi ;
	
	@JsonProperty("MoneyMajorLoss")
    private BigDecimal     moneyMajorLoss ;
	
	@JsonProperty("MoneyAnnualEstimate")
    private BigDecimal     moneyAnnualEstimate ;
	
	@JsonProperty("MoneyCollector")
	    private BigDecimal     moneyCollector ;
	
	
	@JsonProperty("MoneyDirectorResidence")
	  private BigDecimal     moneyDirectorResidence ;
	
	
	@JsonProperty("MoneyOutofSafe")
	    private BigDecimal     moneyOutofSafe ;
	
	@JsonProperty("PlateGlassDesc")
	    private String     plateGlassDesc ;
	
	@JsonProperty("PlateGlassType")
    private String     plateGlassType ;
	
	@JsonProperty("IndemityPeriod")
    private String     indemityPeriod ;

	@JsonProperty("IndemityPeriodDesc")
    private String     indemityPeriodDesc ;
	
	@JsonProperty("BorrowerType")
	private String borrowerType;
	
	@JsonProperty("BorrowerTypeDesc")
    private String     borrowerTypeDesc ;
	
	@JsonProperty("CollateralName")
	private String collateralName;
	
	@JsonProperty("CollateralYn")
    private String  collateralYn ;
	
	@JsonProperty("Param1")
    private String  Param1 ;

	@JsonProperty("Param2")
    private String  Param2 ;
	

	@JsonProperty("Param3")
    private String  Param3 ;

	@JsonProperty("Param4")
    private String  Param4 ;
	
	@JsonProperty("Param5")
    private String  Param5 ;

	@JsonProperty("Param6")
    private String  Param6;

	@JsonProperty("Param7")
    private String  Param7 ;

	@JsonProperty("Param8")
    private String  Param8 ;

	@JsonProperty("Param9")
    private String  Param9 ;

	@JsonProperty("Param10")
    private String  Param10 ;

	@JsonProperty("Param11")
    private String  Param11 ;

	@JsonProperty("Param12")
    private String  Param12 ;

	@JsonProperty("Param13")
    private String  Param13 ;

	@JsonProperty("Param14")
    private String  Param14 ;

	@JsonProperty("Param15")
    private String  Param15;
	
  
	
	
	

	
	
	
	
}
