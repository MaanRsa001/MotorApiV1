package com.maan.eway.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Jacksonized
@XmlRootElement(name="VerificationDtl")

@XmlAccessorType(XmlAccessType.FIELD)
public class VerificationHdrDtlsRes {

	@JsonProperty("MotorCategory")
	@XmlElement(name = "MotorCategory")
	private String motorCategory ;
	
	@JsonProperty("RegistrationNumber")
	@XmlElement(name = "RegistrationNumber")
	private String registrationNumber ;
	
	@JsonProperty("ChassisNumber")
	@XmlElement(name = "ChassisNumber")
	private String chassisNumber ;
	
	@JsonProperty("Make")
	@XmlElement(name = "Make")
	private String make ;
	
	@JsonProperty("Model")
	@XmlElement(name = "Model")
	private String model ;
	
	@JsonProperty("ModelNumber")
	@XmlElement(name = "ModelNumber")
	private String modelNumber ;
	
	@JsonProperty("BodyType")
	@XmlElement(name = "BodyType")
	private String bodyType ;
	
	@JsonProperty("Color")
	@XmlElement(name = "Color")
	private String color ;
	
	@JsonProperty("EngineNumber")
	@XmlElement(name = "EngineNumber")
	private String engineNumber ;
	
	@JsonProperty("EngineCapacity")
	@XmlElement(name = "EngineCapacity")
	private String engineCapacity ;
	
	@JsonProperty("FuelUsed")
	@XmlElement(name = "FuelUsed")
	private String fuelUsed ;
	
	@JsonProperty("NumberOfAxles")
	@XmlElement(name = "NumberOfAxles")
	private String numberOfAxles ;
	
	@JsonProperty("AxleDistance")
	@XmlElement(name = "AxleDistance")
	private String axleDistance ;
	
	@JsonProperty("SittingCapacity")
	@XmlElement(name = "SittingCapacity")
	private String sittingCapacity ;
	
	@JsonProperty("YearOfManufacture")
	@XmlElement(name = "YearOfManufacture")
	private String yearOfManufacture ;
	
	@JsonProperty("TareWeight")
	@XmlElement(name = "TareWeight")
	private String tareWeight ;
	
	@JsonProperty("GrossWeight")
	@XmlElement(name = "GrossWeight")
	private String grossWeight ;
	
	@JsonProperty("MotorUsage")
	@XmlElement(name = "MotorUsage")
	private String motorUsage ;
	
	@JsonProperty("OwnerName")
	@XmlElement(name = "OwnerName")
	private String ownerName ;
	
	@JsonProperty("OwnerCategory")
	@XmlElement(name = "OwnerCategory")
	private String ownerCategory ;

}
