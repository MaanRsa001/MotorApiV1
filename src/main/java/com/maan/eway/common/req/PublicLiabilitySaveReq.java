package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PublicLiabilitySaveReq {
	
	
	@JsonProperty("SectionId")
    private String sectionId;

    @JsonProperty("LiabilitySi")
    private Integer liabilitySi; 

    @JsonProperty("ProductTurnoverSi")
    private Integer productTurnoverSi; 

    @JsonProperty("InsurancePeriodSi")
    private Integer insurancePeriodSi;

    @JsonProperty("AnyAccidentSi")
    private Integer anyAccidentSi;

    @JsonProperty("Status")
    private String status;

}
