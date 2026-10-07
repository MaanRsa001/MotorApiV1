package com.maan.eway.common.res;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ContentRiskGetRes {

	@JsonProperty("RequestReferenceNo")
	private String requestReferenceNo;
	@JsonProperty("RiskId")
	private String riskId;
	@JsonProperty("SectionId")
	private String sectionId;
	@JsonProperty("SectionDesc")
	private String sectionDesc;
	@JsonProperty("ItemId")
	private String itemId;
	@JsonProperty("ItemDesc")
	private String itemDesc;
	@JsonProperty("ItemValue")
	private String itemValue;
	@JsonProperty("SumInsured")
	private String sumInsured;
	@JsonProperty("SerialNo")
	private String serialNo;

	@JsonProperty("MakeAndModel")
	private String makeAndModel;
	
	@JsonProperty("ManufactureYear")
	private String manufactureYear;

	@JsonProperty("Type")
	private String type;

	@JsonProperty("TypeDesc")
	private String typeDesc;

	@JsonProperty("QuoteNo")
	private String quoteNo;

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

	@JsonProperty("PurchaseYear")
	private String purchaseYear;
	@JsonProperty("PurchaseMonth")
	private String purchaseMonth;
	
	@JsonProperty("ContentRiskDesc")
	private String contentRiskDesc ;
	
	@JsonProperty("SerialNoDesc")
	private String serialNoDesc ;
	
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
