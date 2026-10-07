package com.maan.eway.req;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MotorVehicleInfoListSaveReq {
	
	//	Motor Vehicle Info Save Req
	@JsonProperty("VehicleInfoSaveReq")
	private List<VehicleInfoSaveReq> VehicleInfoSaveReq;

	//Save motor details
	@JsonProperty("BrokerBranchCode")
    private String     brokerBranchCode ;
	
	@JsonProperty("SourceTypeId")
	private String sourceTypeId;
	
	@JsonProperty("SourceType")
	private String sourceType;
	
	@JsonProperty("CustomerCode")
	private String customerCode;
	
	@JsonProperty("ApplicationId")
	private String applicationId;
	
	@JsonProperty("BdmCode")
	private String bdmCode;
	
	@JsonProperty("BrokerCode")
	private String brokerCode;
	
	@JsonProperty("SubUserType")
	private String subUserType;
	
	@JsonProperty("CustomerReferenceNo")
    private String   customerReferenceNo ;
	
    @JsonProperty("RequestReferenceNo")
    private String   requestReferenceNo ;
    
	@JsonProperty("Idnumber")
    private String     idNumber     ;
	
	@JsonProperty("VehicleId")
    private String   vehicleId    ;
	
	@JsonProperty("AgencyCode")
    private String     agencyCode ;
	
	@JsonProperty("ProductId")
    private String  productId ;
	
	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("PolicyStartDate")
    private Date policyStartDate;

	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("PolicyEndDate")
    private Date policyEndDate;
	
	@JsonProperty("Currency")
    private String  currency;
	
	@JsonProperty("ExchangeRate")
    private String  exchangeRate;
	
	@JsonProperty("HavePromoCode")
    private String     havepromocode ;
	
	@JsonProperty("PromoCode")
    private String     promocode    ;
	
	@JsonProperty("FleetOwnerYn")
    private String fleetOwnerYn;
	
	@JsonProperty("UserType")
	private String userType;
	
	@JsonProperty("SaveOrSubmit")
    private String    saveOrSubmit;
	
	@JsonProperty("Status")
    private String   status;
	
	@JsonProperty("CarAlarmYn")
	private String carAlarmYn;
	
	@JsonProperty("LoginId")
	private String loginId;
	
	@JsonProperty("Insuranceid")
	private String insuranceId;

	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("CreatedBy")
	private String createdBy;
	
	@JsonProperty("TransactionId")
	private String transactionId;
	
	
}
