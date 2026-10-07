package com.maan.eway.common.req;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
 
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EwayVehicleMakeModelSaveReq {
	
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
	private String bodyId;

	@JsonProperty("modelGroupId")
	private String modelgroupId;

	@JsonProperty("MakeId")
	private String makeId;

	@JsonProperty("ProductId")
	private String productId;

	@JsonProperty("InsuranceId")
	private String companyId;

	@JsonProperty("SectionId")
	private String sectionId;

	@JsonProperty("ModelId")
	private String modelId;
	
	@JsonProperty("ModelIGroupId")
	private String modelGroupId;

	@JsonProperty("Status")
	private String status;

	@JsonProperty("CreatedBy")
	private String createdBy;
	
	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("EffectiveDateStart")
	private Date effectiveDateStart;
	
}
