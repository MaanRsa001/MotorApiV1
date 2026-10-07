package com.maan.eway.req;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class VehicleInfoSaveReq {

	@JsonProperty("ResStatusCode")
	private String resStatusCode;

	@JsonProperty("ResStatusDesc")
	private String resStatusDesc;

	@JsonProperty("MotorCategory")
	private String resMotorCategory;
	
	@JsonProperty("MotorCategoryDesc")
	private String resMotorCategoryDesc;

	@JsonProperty("Registrationnumber")
	private String resRegNumber;

	@JsonProperty("Chassisnumber")
	private String resChassisNumber;

	@JsonProperty("Vehiclemake")
	private String resMake;

	@JsonProperty("Vehcilemodel")
	private String resModel;

	@JsonProperty("VehicleType")
	private String resBodyType;

	@JsonProperty("Color")
	private String resColor;

	@JsonProperty("EngineNumber")
	private String resEngineNumber;

	@JsonProperty("ResEngineCapacity")
	private String resEngineCapacity;

	@JsonProperty("FuelType")
	private String resFuelUsed;

	@JsonProperty("NumberOfAxels")
	private String resNumberOfAxles;

	@JsonProperty("AxelDistance")
	private String resAxleDistance;

	@JsonProperty("SeatingCapacity")
	private String resSittingCapacity;

	@JsonProperty("ManufactureYear")
	private String resYearOfManufacture;

	@JsonProperty("Tareweight")
	private String resTareWeight;
	
	@JsonProperty("Grossweight")
	private String resGrossWeight;

	@JsonProperty("Motorusage")
	private String resMotorUsage;

	@JsonProperty("ResOwnerName")
	private String resOwnerName;

	@JsonProperty("OwnerCategory")
	private String resOwnerCategory;
	
	@JsonProperty("ExcessLimit")
	private Double excessLimit;
	
	@JsonProperty("NonElecAccessoriesSi")
	private Double nonElecAccessoriesSi;
	
	@JsonProperty("Insuranceid")
	private Double insuranceid;

	
}
