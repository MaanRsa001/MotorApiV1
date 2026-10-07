package com.maan.eway.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MotorVehicleInfoExternalApiRes {

	@JsonProperty("registrationNo")
	private String registrationNo;

	@JsonProperty("make")
	private String make;

	@JsonProperty("model")
	private String model;

	@JsonProperty("chassisNo")
	private String chassisNo;

	@JsonProperty("engineNo")
	private String engineNo;

	@JsonProperty("yearMake")
	private String yearMake;

	@JsonProperty("gvm")
	private String gvm;
	
	@JsonProperty("bodyType")
	private String bodyType;

	@JsonProperty("mainColor")
	private String mainColor;

	@JsonProperty("numberOfSeats")
	private String numberOfSeats;
	
	@JsonProperty("firstRegDate")
	private String firstRegDate;
	
	@JsonProperty("currentLicenseExpiryDate")
	private String currentLicenseExpiryDate; 
	
	@JsonProperty("roadWorthinessExpiryDate")
	private String roadWorthinessExpiryDate; 

	@JsonProperty("registrationStatus")
	private String registrationStatus;
	
	@JsonProperty("category")
	private String category;

}
