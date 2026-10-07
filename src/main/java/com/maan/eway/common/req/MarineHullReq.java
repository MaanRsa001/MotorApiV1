package com.maan.eway.common.req;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class MarineHullReq {

	@JsonProperty("ProductId")
	private Integer productid;

	@JsonProperty("LocationName")
	private String locationName;

	@JsonProperty("SectionId")
	private Integer sectionId;
	
	@JsonProperty("CoverId")
	private Integer coverId;

	@JsonProperty("LocationId")
	private Integer locationId;

	@JsonProperty("QuoteNo")
	private String quoteNo;

	@JsonProperty("RequestReferenceNo")
	private String requestReferenceNo;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("startDate")
	private Date startDate;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EndDate")
	private Date endDate;

	@JsonProperty("VesseId")
	private String vesseId;

	@JsonProperty("VesselName")
	private String vesselName;

	@JsonProperty("VesselRegistration")
	private String vesselRegistration;

	@JsonProperty("ManufacturingYear")
	private String manufacturingYear;

	@JsonProperty("MaxPassenger")
	private String maxPassenger;

	@JsonProperty("MaxCrew")
	private String maxCrew;

	@JsonProperty("Max")
	private String max;
	
	@JsonProperty("VesselUsage")
	private String vesselUsage;
	
	@JsonProperty("VesselType")
	private String vesselType;
	
	@JsonProperty("VesselValue")
	private String vesselValue;
	
	@JsonProperty("MaxCargoCapacity")
	private String maxCargoCapacity;
	
	@JsonProperty("TerritorialLimits")
	private String territorialLimits;
	
	@JsonProperty("Dimension")
	private String dimension;
	
	@JsonProperty("EngineType")
	private String engineType;
	
	@JsonProperty("ConstructionMaterial")
	private String constructionMaterial;
	
	@JsonProperty("PassengerLiability")
	private String passengerLiability;
	
	@JsonProperty("RiskId")
	private Integer riskId;
	
	
}
