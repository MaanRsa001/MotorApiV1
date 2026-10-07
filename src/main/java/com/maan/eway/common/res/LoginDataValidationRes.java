package com.maan.eway.common.res;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LoginDataValidationRes {
	
	@JsonProperty("BrokerBranchCode")
	private List<String> brokerBranchCode;
	
    @JsonProperty("BrokerCode")
    private String brokerCode;

    @JsonProperty("LoginId")
    private String loginId;

    @JsonProperty("SubUserType")
    private String subUserType;

//    @JsonProperty("SourceTypeId")
//    private String sourceTypeId;

    @JsonProperty("UserType")
    private String userType;

    @JsonProperty("Zone")
    private String zone;

    @JsonProperty("AgencyCode")
    private String agencyCode;

    @JsonProperty("BdmCode")
    private String bdmCode;

//    @JsonProperty("BranchCode")
//    private String branchCode;

//    @JsonProperty("SourceType")
//    private String sourceType;
    
    @JsonProperty("Source")
    private Map<String, String> source;


}
