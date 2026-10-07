package com.maan.eway.common.req;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class VehicleDetailsReq {

	@JsonProperty("VehicleId")
	private String vehicleId;

	@JsonProperty("SeriesNo")
	private String seriesNo;

	@JsonProperty("NoCylinder")
	private Integer noCylinder;

	@JsonProperty("NoCylinderDes")
	private String noCylinderDesc;

	@JsonProperty("PlateType")
	private String plateType;

	@JsonProperty("PlateTypeDesc")
	private String platetypedesc;

	@JsonProperty("PlateColor")
	private String plateColor;

	@JsonProperty("PlateColorId")
	private Integer plateColorId;

	@JsonProperty("NoDoors")
	private Integer noDoors;

	@JsonProperty("NoDoorsDes")
	private String noDoorsDesc;

	@JsonProperty("CertificateType")
	private String certificateType;

	@JsonProperty("CertificateNo")
	private String certificateNo;

	@JsonProperty("BookId")
	private String bookId;

	@JsonProperty("ChassisNumber")
	private String chassisNumber;

	@JsonProperty("EngineNumber")
	private String engineNumber;

	@JsonProperty("SeatingCapacity")
	private Integer seatingCapacity;

	@JsonProperty("Color")
	private String color;

	@JsonProperty("ColorDesc")
	private String colorDesc;

	@JsonProperty("ValCompanyId")
	private String ValCompanyId;

	@JsonProperty("ValCompanyName")
	private String ValCompanyName;

	@JsonProperty("OriginalRegistrationDate")
	private String originalRegistrationDate;

	@JsonProperty("CountryRegistrationDate")
	private String countryRegistrationDate;

	@JsonProperty("GrossWeight")
	private String grossWeight;

}
