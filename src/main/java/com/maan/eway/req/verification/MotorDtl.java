package com.maan.eway.req.verification;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;
@Data
//@XmlRootElement(name="MotorDtl")
@ToString
@Jacksonized
@XmlAccessorType(XmlAccessType.FIELD)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MotorDtl {
 
	@JsonProperty("MotorCategory")
	@XmlElement(name = "MotorCategory")
    String motorCategory;
	@JsonProperty("MotorType")
	@XmlElement(name = "MotorType")
    String motorType;
	
	
	@JsonProperty("RegistrationNumber")
	@XmlElement(name = "RegistrationNumber")
    String registrationNumber;
	
	
	@JsonProperty("ChassisNumber")
	@XmlElement(name = "ChassisNumber")
    String chassisNumber;
	@JsonProperty("Make")
	@XmlElement(name = "Make")
    String make;
	@JsonProperty("Model")
	@XmlElement(name = "Model")
    String model;
	@JsonProperty("ModelNumber")
	@XmlElement(name = "ModelNumber")
    String modelNumber;
	@JsonProperty("BodyType")
	@XmlElement(name = "BodyType")
    String bodyType;

	@JsonProperty("Color")
	@XmlElement(name = "Color")
    String color;
	@JsonProperty("EngineNumber")
	@XmlElement(name = "EngineNumber")
    String engineNumber;

	@JsonProperty("EngineCapacity")
	@XmlElement(name = "EngineCapacity")
    String engineCapacity;
	@JsonProperty("FuelUsed")
	@XmlElement(name = "FuelUsed")
    String fuelUsed;
	@JsonProperty("NumberOfAxles")
	@XmlElement(name = "NumberOfAxles")
    String numberOfAxles;
	
	@JsonProperty("AxleDistance")
	@XmlElement(name = "AxleDistance")
    String axleDistance;

	@JsonProperty("SittingCapacity")
	@XmlElement(name = "SittingCapacity")
    String sittingCapacity;
	

	@JsonProperty("YearOfManufacture")
	@XmlElement(name = "YearOfManufacture")
    String yearOfManufacture;
	

	@JsonProperty("TareWeight")
	@XmlElement(name = "TareWeight")
    String tareWeight;
	
	
	@JsonProperty("GrossWeight")
	@XmlElement(name = "GrossWeight")
    String grossWeight;
	
	@JsonProperty("MotorUsage")
	@XmlElement(name = "MotorUsage")
    String motorUsage;
	
	@JsonProperty("OwnerName")
	@XmlElement(name = "OwnerName")
    String ownerName;
	@JsonProperty("OwnerCategory")
	@XmlElement(name = "OwnerCategory")
    String ownerCategory;
	
	@JsonProperty("OwnerAddress")
	@XmlElement(name = "OwnerAddress")
    String ownerAddress;
	
	
	
	
	 
}