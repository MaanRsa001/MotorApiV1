package com.maan.eway.common.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EwayVehicleMakeDropdownRes {

	// --- ENTITY PRIMARY KEY

//	@JsonProperty("Vehicleid")
//	private String vehicleid;
//
//	@JsonProperty("Make")
//	private String make;
//
//	@JsonProperty("Modelgroup")
//	private String modelGroup;
//
//	@JsonProperty("Model")
//	private String model;
//
//	@JsonProperty("Bodytype")
//	private String bodytype;
//
//	@JsonProperty("Sourcevehicleid")
//	private String sourcevehicleid;
//
//	@JsonProperty("Filesource")
//	private String filesource;
//
//	@JsonProperty("Makemodel")
//	private String makemodel;
//
//	@JsonProperty("EnginesizeCc")
//	private String enginesizeCc;
//
//	@JsonProperty("Weight_kg")
//	private String weightKg;
//
//	@JsonProperty("Power_kw")
//	private String powerKw;
//
//	@JsonProperty("Fueltype")
//	private String fueltype;
//
//	@JsonProperty("Transmissiontype")
//	private String transmissiontype;
//
//	@JsonProperty("Isselectable")
//	private String isselectable;
//
//	@JsonProperty("Vehiclegroup")
//	private String vehiclegroup;
	
	@JsonProperty("Code")
	private String code;

	@JsonProperty("CodeDesc")
	private String codeDesc;

}
