package com.maan.eway.common.req;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OtherVehicleInfoReq {
	
	
	@JsonProperty("CompanyId")
	private String companyId;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("SectionId")
	private String sectionId;
	
	@JsonProperty("QuoteNo")
	private String quoteNo;
	
	@JsonProperty("RequestReferenceNo")
	private String requestReferenceNo;
	
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
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date originalRegistrationDate;
    
    @JsonProperty("CountryRegistrationDate") 
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date countryRegistrationDate;
    
    @JsonProperty("GrossWeight")
   	private String grossWeight;
    

}
