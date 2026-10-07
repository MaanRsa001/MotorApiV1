package com.maan.eway.res;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.maan.eway.error.Error;

import lombok.Data;

@Data

public class MotorVehicleInfoRes {

	@JsonProperty("ReqRegNumber")
	private String reqRegNumber;

	@JsonProperty("ReqChassisNumber")
	private String reqChassisNumber;

	@JsonProperty("ReqRequestId")
	private String reqRequestId;

	@JsonProperty("ReqCompanyCode")
	private String reqCompanyCode;

	@JsonProperty("ReqSystemCode")
	private String reqSystemCode;

	@JsonProperty("ReqMotorCategory")
	private String reqMotorCategory;

	@JsonProperty("ReqMsgSignature")
	private String reqMsgSignature;

	@JsonFormat(pattern = "dd/MM/yyy")
	@JsonProperty("EntryDate")
	private Date entryDate;

	@JsonProperty("Status")
	private String status; 

	@JsonProperty("CreatedBy")
	private String createdBy;

	@JsonProperty("ResResponseId")
	private String resResponseId;

	@JsonProperty("ResRequestId")
	private String resRequestId;

	@JsonProperty("ResStatusCode")
	private String resStatusCode;

	@JsonProperty("ResStatusDesc")
	private String resStatusDesc; //registrationStatus

	@JsonProperty("MotorCategory")
	private String resMotorCategory;

	@JsonProperty("Registrationnumber")
	private String resRegNumber;

	@JsonProperty("Chassisnumber")
	private String resChassisNumber;
	
	@JsonProperty("VehiclemakeId")
	private String resMakeid;


	@JsonProperty("Vehiclemake")
	private String resMake;
	
	@JsonProperty("VehcilemodelId")
	private String resModelid;


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
	private String resYearOfManufacture; //yearMake

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

	@JsonProperty("ResMsgSignature")
	private String resMsgSignature;
	
	@JsonProperty("SavedFrom")
	private String savedFrom;
	
	@JsonProperty("PolicyYn")
	private String policyYn;
	
	@JsonProperty("PolicyHolderInfo")
	private PolicyHolderInfoDto holder;
	

	@JsonFormat(pattern = "dd/MM/yyy" , timezone="Africa/Dar_es_Salaam")
    @JsonProperty("PolicyStartDate")
    private Date       covernoteStartDate ;

	@JsonFormat(pattern = "dd/MM/yyy" , timezone="Africa/Dar_es_Salaam")
    @JsonProperty("PolicyEndDate")
    private Date       covernoteEndDate ;

    @JsonProperty("CURRENCY_CODE")
    private String     currencyCode;
    
    @JsonProperty("PRODUCT_CODE")    
    private String     productCode;
    
    @JsonProperty("RISK_CODE")
    private String     riskCode;
    
    @JsonProperty("SUM_INSURED")
    private BigDecimal sumInsured;
    
    @JsonProperty("ErrorMessage")
    private String errorMessage;
    
    @JsonProperty("PolicyType")
    private String     policyTypeId;
    
    @JsonProperty("MotorCategoryDesc")
    private String motorCategoryDesc;
    
    //madison additional fields
    @JsonProperty("Gvm")
    private String gvm;
    
	@JsonFormat(pattern = "dd/MM/yyy")
	@JsonProperty("currentLicenseExpiryDate")
	private Date currentLicenseExpiryDate; 
	
	@JsonFormat(pattern = "dd/MM/yyy")
	@JsonProperty("roadWorthinessExpiryDate")
	private Date roadWorthinessExpiryDate; 

	@JsonFormat(pattern = "dd/MM/yyy")
	@JsonProperty("firstRegDate")
	private Date firstRegDate;

	@JsonProperty("HorsePower") 
    private String horsePower;
	
	@JsonProperty("DisplacementInCM3")
	private String displacementInCM3;
	
	@JsonProperty("NumberOfCylinders")
	private String noOfCylinders;
	
	@JsonFormat(pattern = "dd/MM/yyy")
	@JsonProperty("RegistrationDate")
	private Date registrationDate ;

	@JsonProperty("PlateType") 
    private String plateType;
	
	@JsonProperty("PlateCode") 
    private String plateCode;
	
	@JsonProperty("VehicleTypeId")
	private String resBodyTypeId;
	
	private Boolean isError;
	private List<Error> errorMessage1;
	

}
