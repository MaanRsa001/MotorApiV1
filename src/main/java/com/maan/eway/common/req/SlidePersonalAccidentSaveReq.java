package com.maan.eway.common.req;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SlidePersonalAccidentSaveReq {
	@JsonProperty("RequestReferenceNo")
    private String     requestReferenceNo ;
	
	@JsonProperty("RiskId")
    private String    riskId   ;
	
	@JsonProperty("ProductId")
    private String    productId    ;
	
	@JsonProperty("SectionId")
    private String sectionId    ;
	
	@JsonProperty("InsuranceId")
	private String insuranceId;

	@JsonProperty("TotalNoOfPersons")
	private String totalNoOfPersons;

	
	@JsonProperty("CreatedBy")
    private String createdBy    ;
	
	@JsonProperty("SumInsured")
    private String sumInsured ;
	
	@JsonProperty("OccupationType")
    private String occupationType;
	
	@JsonProperty("TotalNoOfPersons")
	private String TotalNoOfPersons ;
	
	@JsonProperty("EndorsementDate") // EndorsementDate
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date endorsementDate;
	@JsonProperty("EndorsementRemarks") // EndorsementRemarks
	private String endorsementRemarks;
	@JsonProperty("EndorsementEffectiveDate") // EndorsementEffectiveDate
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date endorsementEffdate;
	@JsonProperty("OrginalPolicyNo") // OrginalPolicyNo
	private String originalPolicyNo;
	@JsonProperty("EndtPrevPolicyNo") // EndtPrevPolicyNo
	private String endtPrevPolicyNo;
	@JsonProperty("EndtPrevQuoteNo") // EndtPrevQuoteNo
	private String endtPrevQuoteNo;
	@JsonProperty("EndtCount") // EndtCount
	private BigDecimal endtCount;
	@JsonProperty("EndtStatus") // EndtStatus
	private String endtStatus;
	@JsonProperty("IsFinanceEndt") // IsFinanceEndt
	private String isFinaceYn;
	@JsonProperty("EndtCategoryDesc") // EndtCategoryDesc
	private String endtCategDesc;
	@JsonProperty("EndorsementType") // EndorsementType
	private Integer endorsementType;

	@JsonProperty("EndorsementTypeDesc") // EndorsementTypeDesc
	private String endorsementTypeDesc;
	
	@JsonProperty("OtherOccupation")
	private String otherOccupation;
		
//	@JsonProperty("TTDSumInsured")
//	private Integer ttdSumInsured;

//	@JsonProperty("MESumInsured")
//	private Integer meSumInsured;

	@JsonProperty("FESumInsured")
	private Integer feSumInsured;
	
	@JsonProperty("PTDSumInsured")
	private Integer ptdSumInsured;
	
	@JsonProperty("OriginalRiskId")
	private String originalRiskId;

}
