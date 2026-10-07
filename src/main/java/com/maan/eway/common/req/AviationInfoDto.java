package com.maan.eway.common.req;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AviationInfoDto {

    @JsonProperty("ProductId")
    private Integer productId;

    @JsonProperty("LocationId")
    private Integer locationId;

    @JsonProperty("SectionId")
    private Integer sectionId;

    @JsonProperty("RiskId")
    private Integer riskId;

    @JsonProperty("RequestReferenceNo")
    private String requestReferenceNo;

    @JsonProperty("QuoteNo")
    private String quoteNo;

    @JsonProperty("LocationName")
    private String locationName;

    @JsonProperty("OwnerName")
    private String ownerName;

    @JsonProperty("AircraftName")
    private String aircraftName;

    @JsonProperty("Type")
    private String type;

    @JsonProperty("RegistrationNumber")
    private String registrationNumber;

    @JsonProperty("ManufactureYear")
    private String manufactureYear;

    @JsonProperty("Make")
    private String make;

    @JsonProperty("Model")
    private String model;

    @JsonProperty("UsageDesc")
    private String usageDesc;

    @JsonProperty("GeographicalLimit")
    private String geographicalLimit;

    @JsonProperty("EngineType")
    private String engineType;

    @JsonProperty("NoOfEngines")
    private Integer noOfEngines;

    @JsonProperty("Weight")
    private String weight;

    @JsonProperty("NightFlight")
    private String nightFlight;

    @JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("startDate")
	private Date startDate;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EndDate")
	private Date endDate;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EntryDate")
	private Date entryDate;
}