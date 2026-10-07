package com.maan.eway.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PolicyIssuanceDto {

	@JsonProperty("memberCompanyID")
    private Integer memberCompanyID;
	
	@JsonProperty("policyNumber")
    private String policyNumber;
	
	@JsonProperty("policyholder")
    private String policyholder;
	
	@JsonProperty("insuredPIN")
    private String insuredPIN;
	
	@JsonProperty("policyHolderEmailId")
    private String policyHolderEmailId;
	
	@JsonProperty("fromDate")
    private String fromDate;
	
	@JsonProperty("toDate")
    private String toDate;
    
    @JsonProperty("profession")
    private String profession;
    
    @JsonProperty("anyoneclaims")
    private Integer anyoneclaims;
    
    @JsonProperty("allclaims")
    private Integer allclaims;
    
    @JsonProperty("documents")
    private Integer documents;
    
    @JsonProperty("slandar")
    private Integer slandar;
    
    @JsonProperty("dishonesty")
    private Integer dishonesty;
    
    @JsonProperty("excess")
    private String excess;
    
    @JsonProperty("KMPDCRegNo")
    private String kmpdcRegNo;
    
    @JsonProperty("groupID")
    private Integer groupID;
    
    @JsonProperty("specilisationID")
    private Integer specilisationID;
    
    @JsonProperty("subSpecilisationID")
    private Integer subSpecilisationID;

}