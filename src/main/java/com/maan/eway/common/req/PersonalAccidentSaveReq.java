package com.maan.eway.common.req;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
  

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PersonalAccidentSaveReq {

	@JsonProperty("RequestReferenceNo")
	private String requestReferenceNo;
	@JsonProperty("SectionId")
	private String sectionId;
	@JsonProperty("QuoteNo")
	private String quoteNo;
	@JsonProperty("CreatedBy")
	private String createdBy;
	@JsonProperty("Description")
	private String description;
	@JsonProperty("Type")
	private String type;
	@JsonProperty("SaveOrSubmit")
	private String saveOrSubmit;
	
	
	@JsonProperty("PersonalDetails")
	private List<PersonalAccidentDetailsListReq> personaldetails;
	
	 @JsonProperty("EndorsementType") //EndorsementType
	    private Integer    endorsementType ;

	    @JsonProperty("EndorsementTypeDesc") // EndorsementTypeDesc
	    private String     endorsementTypeDesc ;
	   
	    @JsonProperty("EndorsementDate") //EndorsementDate
	    @JsonFormat(pattern = "dd/MM/yyyy")
	    private Date       endorsementDate ;
	    @JsonProperty("EndorsementRemarks") // EndorsementRemarks
	    private String     endorsementRemarks ;    
	    @JsonProperty("EndorsementEffectiveDate") // EndorsementEffectiveDate
	    @JsonFormat(pattern = "dd/MM/yyyy")
	    private Date       endorsementEffdate ;
	    @JsonProperty("OrginalPolicyNo") // OrginalPolicyNo
	    private String     originalPolicyNo ;
	    @JsonProperty("EndtPrevPolicyNo") // EndtPrevPolicyNo
	    private String     endtPrevPolicyNo ;
	    @JsonProperty("EndtPrevQuoteNo") // EndtPrevQuoteNo
	    private String     endtPrevQuoteNo ;
	    @JsonProperty("EndtCount")  // EndtCount
	    private BigDecimal endtCount ;
	    @JsonProperty("EndtStatus") //EndtStatus
	    private String     endtStatus ;   
	    @JsonProperty("IsFinanceEndt") //IsFinanceEndt
	    private String     isFinaceYn ;  
	    @JsonProperty("EndtCategoryDesc") //EndtCategoryDesc
	    private String     endtCategDesc ;
	    @JsonProperty("PolicyNo")
	    private String policyNo;

}
