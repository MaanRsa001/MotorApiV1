package com.maan.eway.req;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MotorVehicleInfoSaveReq {


	@JsonProperty("CreatedBy")
	private String createdBy;

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
	
	@JsonProperty("VehiclemakeId")
	private String resMakeId;

	@JsonProperty("Vehiclemake")
	private String resMake;

	@JsonProperty("VehcilemodelId")
	private String resModelId;

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
	
	@JsonProperty("Insuranceid")
	private String insuranceId;


	@JsonProperty("BranchCode")
	private String branchCode;
	
	
	@JsonProperty("ExcessLimit")
	private Double excessLimit;
	
	
	@JsonProperty("NonElecAccessoriesSi")
	private Double nonElecAccessoriesSi;
	

	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("RegistrationDate")
	private Date registrationDate;


	@JsonProperty("HorsePower") 
    private String horsePower;
	
	@JsonProperty("DisplacementInCM3")
	private String displacementInCM3;
	
	@JsonProperty("NumberOfCylinders")
	private Integer numberOfCyliners;
	
	@JsonProperty("PlateType") 
    private String plateType;
	
	@JsonProperty("PlateCode") 
    private String plateCode;
	
	@JsonProperty("VehicleTypeId")
	private String resBodyTypeId;

	
}
