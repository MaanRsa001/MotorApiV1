package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BurglaryAndHouseSaveReq {
	
	@JsonProperty("AgencyCode")
	private String agencyCode;

	@JsonProperty("ApplicationId")
	private String applicationId;

	@JsonProperty("BdmCode")
	private String bdmCode;

	@JsonProperty("BranchCode")
	private String branchCode;

	@JsonProperty("BrokerBranchCode")
	private String brokerBranchCode;

	@JsonProperty("BrokerCode")
	private String brokerCode;

	@JsonProperty("BuidingAreaSqm")
	private String buidingAreaSqm;

	@JsonProperty("BuildingBuildYear")
	private String buildingBuildYear;

	@JsonProperty("BuildingCondition")
	private String buildingCondition;

	@JsonProperty("BuildingFloors")
	private String buildingFloors;

	@JsonProperty("BuildingOwnerYn")
	private String buildingOwnerYn;

	@JsonProperty("BuildingPurposeId")
	private String buildingPurposeId;

	@JsonProperty("SourceType")
	private String sourceType;

	@JsonProperty("CustomerCode")
	private String customerCode;

	@JsonProperty("InsuranceType")
	private String insuranceType;

	@JsonProperty("LoginId")
	private String loginId;

	@JsonProperty("UserType")
	private String userType;

	@JsonProperty("OutbuildConstructType")
	private String outbuildConstructType;

	@JsonProperty("SectionId")
	private String sectionId;

	@JsonProperty("SubUsertype")
	private String subUsertype;

	@JsonProperty("InsuranceForId")
	private String[] insuranceForId;

	@JsonProperty("NatureOfTradeId")
	private String natureOfTradeId;

	@JsonProperty("WallType")
	private String wallType;

	@JsonProperty("InternalWallType")
	private String internalWallType;

	@JsonProperty("CeilingType")
	private String ceilingType;

	@JsonProperty("FirstLossPercentId")
	private String firstLossPercentId;

	@JsonProperty("StockInTradeSi")
	private String stockInTradeSi;

	@JsonProperty("GoodsSi")
	private String goodsSi;

	@JsonProperty("FurnitureSi")
	private String furnitureSi;

	@JsonProperty("ApplianceSi")
	private String applianceSi;

	@JsonProperty("CashValueablesSi")
	private String cashValueablesSi;

	@JsonProperty("StockLossPercent")
	private String stockLossPercent;

	@JsonProperty("GoodsLossPercent")
	private String goodsLossPercent;

	@JsonProperty("FurnitureLossPercent")
	private String furnitureLossPercent;

	@JsonProperty("ApplianceLossPercent")
	private String applianceLossPercent;

	@JsonProperty("CashValueablesLossPercent")
	private String cashValueablesLossPercent;

	@JsonProperty("Address")
	private String address;

	@JsonProperty("RegionCode")
	private String regionCode;

	@JsonProperty("DistrictCode")
	private String districtCode;

	@JsonProperty("OccupiedYear")
	private String occupiedYear;

	@JsonProperty("WatchmanGuardHours")
	private String watchmanGuardHours;

	@JsonProperty("AccessibleWindows")
	private String accessibleWindows;

	@JsonProperty("ShowWindow")
	private String showWindow;

	@JsonProperty("FrontDoors")
	private String frontDoors;

	@JsonProperty("BackDoors")
	private String backDoors;

	@JsonProperty("TrapDoors")
	private String trapDoors;

	@JsonProperty("WindowsMaterialId")
	private String windowsMaterialId;

	@JsonProperty("DoorsMaterialId")
	private String doorsMaterialId;

	@JsonProperty("NightLeftDoor")
	private String nightLeftDoor;

	@JsonProperty("BuildingOccupied")
	private String buildingOccupied;

	@JsonProperty("BurglarySi")
	private Integer burglarySi;

	@JsonProperty("RoofType")
	private String roofType;

	@JsonProperty("Status")
	private String status;

}
