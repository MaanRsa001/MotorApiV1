package com.maan.eway.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class EmployeeIdDuplicaionReq {
	
	@JsonProperty("NationalityId")
    private String    nationalityId;
	
	
	@JsonProperty("LocationId")
    private String     locationId ;
	
}
