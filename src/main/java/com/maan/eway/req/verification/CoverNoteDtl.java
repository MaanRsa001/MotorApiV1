package com.maan.eway.req.verification;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Jacksonized
@XmlAccessorType(XmlAccessType.FIELD)

public class CoverNoteDtl{
	@JsonProperty("CoverNoteNumber") 
	@XmlElement(name = "CoverNoteNumber")
	private String coverNoteNumber;
	

	@JsonProperty("PrevCoverNoteReferenceNumber") 
	@XmlElement(name = "PrevCoverNoteReferenceNumber")
	private String prevCoverNoteReferenceNumber;
	

	@JsonProperty("SalePointCode") 
	@XmlElement(name = "SalePointCode")
	private String salePointCode;


	@JsonProperty("CoverNoteStartDate") 
	@XmlElement(name = "CoverNoteStartDate")
	private String coverNoteStartDate;
	
	@JsonProperty("CoverNoteEndDate") 
	@XmlElement(name = "CoverNoteEndDate")
	private String coverNoteEndDate;

	@JsonProperty("CoverNoteDesc") 
	@XmlElement(name = "CoverNoteDesc")
	private String coverNoteDesc;

	
	
	


	@JsonProperty("OperativeClause") 
	@XmlElement(name = "OperativeClause")
	private String operativeClause;

	@JsonProperty("PaymentMode") 
	@XmlElement(name = "PaymentMode")
	private String paymentMode;

	@JsonProperty("CurrencyCode") 
	@XmlElement(name = "CurrencyCode")
	private String currencyCode;


	@JsonProperty("ExchangeRate") 
	@XmlElement(name = "ExchangeRate")
	private String exchangeRate;
	

	@JsonProperty("TotalPremiumExcludingTax") 
	@XmlElement(name = "TotalPremiumExcludingTax") 
	private String totalPremiumExcludingTax;

	@JsonProperty("TotalPremiumIncludingTax")
	@XmlElement(name = "TotalPremiumIncludingTax")
	private String totalPremiumIncludingTax;

	@JsonProperty("CommisionPaid") 
	@XmlElement(name = "CommisionPaid")
	private String commisionPaid;

	@JsonProperty("CommisionRate")
	@XmlElement(name = "CommisionRate")
	private String commisionRate;
	

	@JsonProperty("OfficerName") 
	@XmlElement(name = "OfficerName")
	private String officerName;

	@JsonProperty("OfficerTitle") 
	@XmlElement(name = "OfficerTitle")
	private String officerTitle;

	@JsonProperty("ProductCode") 
	@XmlElement(name = "ProductCode")
	private String productCode;
	


	@JsonProperty("EndorsementType") 
	@XmlElement(name = "EndorsementType")
	private String endorsementType;
	
	@XmlElement(name = "EndorsementReason")
	@JsonProperty("EndorsementReason") 
	private String endorsementReason;
	
	@JsonProperty("EndorsementPremiumEarned") 
	@XmlElement(name = "EndorsementPremiumEarned")
	private String endorsementPremiumEarned;

  
	@JsonProperty("RisksCovered") 
	@XmlElement(name = "RisksCovered")
	private RisksCovered risksCoveredBean ;

	@JsonProperty("SubjectMattersCovered") 
	@XmlElement(name = "SubjectMattersCovered")
	private  SubjectMattersCovered subjectMattersCoveredBean ;

	@JsonProperty("CoverNoteAddons") 
	@XmlElement(name = "CoverNoteAddons")
	private CoverNoteAddons coverNoteAddonsBean ;

	@JsonProperty("PolicyHolders") 
	@XmlElement(name = "PolicyHolders")
	private PolicyHolders policyHoldersBean ;
	@JsonProperty("MotorDtl") 
	@XmlElement(name = "MotorDtl")
	private MotorDtl motorDtlBean ;

}