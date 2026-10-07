package com.maan.eway.common.res;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EwayVehicleMakeModelGetRes {

	// --- ENTITY PRIMARY KEY

	@JsonProperty("VehicleId")
	private String vehicleid;

	@JsonProperty("Make")
	private String make;

	@JsonProperty("Modelgroup")
	private String modelGroup;

	@JsonProperty("Model")
	private String model;

	@JsonProperty("BodyType")
	private String bodytype;

	@JsonProperty("SourceVehicleId")
	private String sourcevehicleid;

	@JsonProperty("FileSource")
	private String filesource;

	@JsonProperty("MakeModel")
	private String makemodel;

	@JsonProperty("EnginesizeCc")
	private String enginesizeCc;

	@JsonProperty("WeightKg")
	private String weightKg;

	@JsonProperty("PowerKw")
	private String powerKw;

	@JsonProperty("FuelType")
	private String fueltype;

	@JsonProperty("TransmissionType")
	private String transmissiontype;

	@JsonProperty("IsSelecTable")
	private String isselectable;

	@JsonProperty("VehicleGroup")
	private String vehiclegroup;

	@JsonProperty("BodyTypeId")
	private Integer bodyId;

	@JsonProperty("modelGroupId")
	private Integer modelgroupId;

	@JsonProperty("MakeId")
	private Integer makeId;

	@JsonProperty("ProductId")
	private Integer productId;

	@JsonProperty("InsuranceId")
	private String companyId;

	@JsonProperty("SectionId")
	private Integer sectionId;

	@JsonProperty("ModelId")
	private Integer modelId;

	@JsonProperty("Status")
	private String status;

	@JsonProperty("CreatedBy")
	private String createdBy;
	
	@JsonFormat(pattern="dd/MM/YYYY")
	@JsonProperty("EntryDate")
	private Date entryDate;
	
	@JsonFormat(pattern="dd/MM/YYYY")
	@JsonProperty("EffectiveDateStart")
	private Date effectiveDateStart;
	
	@JsonFormat(pattern="dd/MM/YYYY")
	@JsonProperty("EffectiveDateEnd")
	private Date effectiveDateEnd;

}
