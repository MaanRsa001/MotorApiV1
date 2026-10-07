package com.maan.eway.update;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UpdateMotorReq {	

	@JsonProperty("RequestReferenceNo")
	private String requestReferenceNo;
	
    @JsonProperty("AgencyCode")
    private String agencyCode;

    @JsonProperty("ApplicationId")
    private String applicationId;

    @JsonProperty("AxelDistance")
    private String axelDistance;

    @JsonProperty("BdmCode")
    private String bdmCode;

    @JsonProperty("BranchCode")
    private String branchCode;

    @JsonProperty("BrokerBranchCode")
    private String brokerBranchCode;

    @JsonProperty("Chassisnumber")
    private String chassisNumber;

    @JsonProperty("CollateralYn")
    private String collateralYn;

    @JsonProperty("Color")
    private String color;

    @JsonProperty("ColorDesc")
    private String colorDesc;

    @JsonProperty("CreatedBy")
    private String createdBy;

    @JsonProperty("CubicCapacity")
    private String cubicCapacity;

    @JsonProperty("Currency")
    private String currency;

    @JsonProperty("CustomerCode")
    private String customerCode;

    @JsonProperty("CustomerName")
    private String customerName;

    @JsonProperty("CustomerReferenceNo")
    private String customerReferenceNo;

    @JsonProperty("DisplacementInCM3")
    private String displacementInCM3;

    @JsonProperty("DrivenByDesc")
    private String drivenByDesc;

    @JsonProperty("EndorsementYn")
    private String endorsementYn;

    @JsonProperty("EngineCapacity")
    private String engineCapacity;

    @JsonProperty("EngineNumber")
    private String engineNumber;

    @JsonProperty("ExchangeRate")
    private String exchangeRate;

    @JsonProperty("FleetOwnerYn")
    private String fleetOwnerYn;

    @JsonProperty("FuelType")
    private String fuelType;

    @JsonProperty("FuelTypeDesc")
    private String fuelTypeDesc;

    @JsonProperty("Gpstrackinginstalled")
    private String gpsTrackingInstalled;

    @JsonProperty("Grossweight")
    private String grossWeight;

    @JsonProperty("HavePromoCode")
    private String havePromoCode;

    @JsonProperty("HoldInsurancePolicy")
    private String holdInsurancePolicy;

    @JsonProperty("HorsePower")
    private String horsePower;

    @JsonProperty("Idnumber")
    private String idNumber;

    @JsonProperty("InsuranceClass")
    private String insuranceClass;

    @JsonProperty("InsuranceId")
    private String insuranceId;

    @JsonProperty("LocationId")
    private String locationId;

    @JsonProperty("LoginId")
    private String loginId;

    @JsonProperty("ManufactureYear")
    private String manufactureYear;

    @JsonProperty("MobileCode")
    private String mobileCode;

    @JsonProperty("MobileNumber")
    private String mobileNumber;

    @JsonProperty("MotorCategory")
    private String motorCategory;

    @JsonProperty("Motorusage")
    private String motorUsage;

    @JsonProperty("MotorusageId")
    private String motorUsageId;

    @JsonProperty("NoOfVehicles")
    private String noOfVehicles;

    @JsonProperty("NumberOfAxels")
    private String numberOfAxels;

    @JsonProperty("NumberOfCylinders")
    private int numberOfCylinders;

    @JsonProperty("PolicyEndDate")
    private String policyEndDate;

    @JsonProperty("PolicyRenewalYn")
    private String policyRenewalYn;

    @JsonProperty("PolicyStartDate")
    private String policyStartDate;

    @JsonProperty("ProductId")
    private String productId;

    @JsonProperty("QuoteExpiryDays")
    private String quoteExpiryDays;

    @JsonProperty("RegistrationYear")
    private String registrationYear;

    @JsonProperty("Registrationnumber")
    private String registrationNumber;

    @JsonProperty("SaveOrSubmit")
    private String saveOrSubmit;

    @JsonProperty("SavedFrom")
    private String savedFrom;

    @JsonProperty("SearchFromApi")
    private boolean searchFromApi;

    @JsonProperty("SeatingCapacity")
    private String seatingCapacity;

    @JsonProperty("SourceType")
    private String sourceType;

    @JsonProperty("SourceTypeId")
    private String sourceTypeId;

    @JsonProperty("Status")
    private String status;

    @JsonProperty("SubUserType")
    private String subUserType;

    @JsonProperty("Tareweight")
    private String tareWeight;

    @JsonProperty("UserType")
    private String userType;

    @JsonProperty("Vehcilemodel")
    private String vehicleModel;

    @JsonProperty("VehcilemodelId")
    private String vehicleModelId;

    @JsonProperty("VehicleId")
    private String vehicleId;

    @JsonProperty("VehicleType")
    private String vehicleType;

    @JsonProperty("VehicleTypeId")
    private String vehicleTypeId;

    @JsonProperty("Vehiclemake")
    private String vehicleMake;

    @JsonProperty("VehiclemakeId")
    private String vehicleMakeId;
}
