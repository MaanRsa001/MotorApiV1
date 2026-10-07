package com.maan.eway.req;

import java.util.List;

import lombok.Data;

import com.maan.eway.error.Error;

@Data
public class VehicleValidationResponse {

    private Integer returnCode;
    private String amount;
    private String vehicleNo;
    private String name;
    private String engineSize;
    private String make;
    private String model;
    private String service;
    private String policyStartDate;
    private String policyEndDate;
    private String rawReturnMessage;
    private String customerReferenceNo;
    
    private Boolean isError;
    private List<Error> errorMessage;

}