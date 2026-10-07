package com.maan.eway.common.req;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class WhatsappMotorSaveReq {


	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("PolicyStartDate")
    private Date policyStartDate;

	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("PolicyEndDate")
    private Date policyEndDate;
	

//	@JsonProperty("Insurancetype")
//    private String     insuranceType ;
	
	@JsonProperty("Insurancetype")
	private List<String> insuranceType;
	
	@JsonProperty("Chassisnumber")
    private String     chassisNumber ;
	
	@JsonProperty("Registernumber")
    private String     registerNumber ;
	
	@JsonProperty("Vehiclemake")
    private String     vehicleMake  ;

	@JsonProperty("Vehcilemodel")
    private String     vehcileModel ;

	@JsonProperty("VehicleType")
    private String     vehicleType  ;
	
	@JsonProperty("periodOfInsurance")
    private String     periodOfInsurance ;
	
	@JsonProperty("SumInsured")
    private String     sumInsured   ;

	@JsonProperty("BranchCode")
    private String     branchCode ;
	@JsonProperty("BrokerBranchCode")
    private String     brokerBranchCode ;

	@JsonProperty("ProductId")
    private String  productId ;

	@JsonProperty("InsuranceId")
    private String  companyId ;

	@JsonProperty("InsuranceClass")
    private String  insuranceClass ;
	
	@JsonProperty("Currency")
    private String  currency;
	
	@JsonProperty("ExchangeRate")
    private String  exchangeRate;
		
	@JsonProperty("SearchFromApi")
	private Boolean searchFromApi ;
	
	@JsonProperty("Color")
	private String color ;
	
	@JsonProperty("EngineCapacity")
	private BigDecimal engineCapacity ;
	
	@JsonProperty("SeatingCapacity")
	private Long seatingCapcity;
	
	@JsonProperty("VehicleModelDesc")
	private String vehicleModelDesc;
	@JsonProperty("EngineNumber")
	private String     engineNumber ;
	@JsonProperty("FuelType")
	private String     fuelType     ;
	
	@JsonProperty("VehicleMakeDesc")
	private String     vehicleMakeDesc  ;	
	@JsonProperty("ColorDesc")
	private String     colorDesc ;
	@JsonProperty("FuelTypeDesc")
	private String     fuelTypeDesc;
	@JsonProperty("VehicleTypeDesc")
	private String     vehicleTypeDesc  ;
	
	@JsonProperty("VehicleUsage")
	private String     vehicleUsage;
	
	@JsonProperty("ManufactureYear")
	private String     manufactorYear;
	

}
