package com.maan.eway.common.res;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TravelPolicyTypeRes {

	
	@JsonProperty("PolicyTypeId")
    private String  policyTypeId  ;

	@JsonProperty("PolicyTypeDesc")
    private String  policyTypeDesc ;
	
	@JsonProperty("PlanTypeId")
    private String  planTypeId  ;

	@JsonProperty("PlanTypeDesc")
    private String  planTypeDesc ;
	
    @JsonFormat(pattern = "dd/MM/yyyy")
    @JsonProperty("EffectiveDateStart")
    private Date       effectiveDateStart ;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @JsonProperty("EffectiveDateEnd")
    private Date       effectiveDateEnd ;
    
	@JsonProperty("Remarks")
    private String  remarks;

	@JsonProperty("CoverDetails")
    private List<TravelPolicyTypeCoverRes> travelCover ;

}

