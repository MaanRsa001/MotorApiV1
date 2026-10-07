package com.maan.eway.req.fleet;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.eway.req.push.PolicyHolders;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Jacksonized
@Builder
@XmlAccessorType(XmlAccessType.FIELD)
public class FleetHdr {

	@JsonProperty("FleetId") 
	@XmlElement(name = "FleetId")
	public String fleetId;
	
	@JsonProperty("FleetType") 
	@XmlElement(name = "FleetType")
	public Integer fleetType;
	
	@JsonProperty("FleetSize") 
	@XmlElement(name = "FleetSize")
	public Integer fleetSize;
	
	
	
	@JsonProperty("ComprehensiveInsured") 
	@XmlElement(name = "ComprehensiveInsured")
	public Integer comprehensiveInsured;	 
	
	
	@JsonProperty("SalePointCode") 
	@XmlElement(name = "SalePointCode")
	private String salePointCode;


	@JsonProperty("CoverNoteStartDate") 
	@XmlElement(name = "CoverNoteStartDate")
	private String coverNoteStartDate;
	
	@JsonProperty("CoverNoteEndDate") 
	@XmlElement(name = "CoverNoteEndDate")
	private String coverNoteEndDate;

	/*@JsonProperty("CoverNoteDesc") 
	@XmlElement(name = "CoverNoteDesc")
	private String coverNoteDesc;

	
	
	


	@JsonProperty("OperativeClause") 
	@XmlElement(name = "OperativeClause")
	private String operativeClause;*/

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
	
	
	@JsonProperty("PolicyHolders") 
	@XmlElement(name = "PolicyHolders")
	private PolicyHolders policyHoldersBean ;
}
