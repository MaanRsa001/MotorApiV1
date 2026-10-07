package com.maan.eway.common.res;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PersonalAccidentGetRes {

	@JsonProperty("RequestReferenceNo")
	private String requestReferenceNo;
	@JsonProperty("RiskId")
	private String riskId;
	@JsonProperty("SectionId")
	private String sectionId;
	@JsonProperty("SerialNo")
	private String serialNo;
	@JsonProperty("SectionDesc")
	private String sectionDesc;
	@JsonProperty("PersonId")
	private String personId;
	
	@JsonProperty("PersonName")
	private String personName;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "en-IN", timezone = "Asia/Calcutta")
	@JsonProperty("Dob")
	private Date dob;
	@JsonProperty("Age")
	private String age;
	@JsonProperty("Height")
	private String height;
	@JsonProperty("Weight")
	private String weight;
	@JsonProperty("Description")
	private String description;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EntryDate")
	private Date entryDate;

	@JsonProperty("Createdby")
	private String createdBy;

	@JsonProperty("Status")
	private String status;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("UpdatedDate")
	private Date updatedDate;

	@JsonProperty("Updatedby")
	private String updatedBy;

	@JsonProperty("QuoteNo")
	private String quoteNo;

	@JsonProperty("OccupationId")
	private String occupationId;

	@JsonProperty("CategoryId")
	private String categoryId;

	@JsonProperty("OccupationDesc")
	private String occupationDesc;
	@JsonProperty("Salary")
	private String salary;

	@JsonProperty("Type")
	private String type;

	@JsonProperty("TypeDesc")
	private String typeDesc;
	
	@JsonProperty("NationalityId")
	private String nationalityId;
	
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
