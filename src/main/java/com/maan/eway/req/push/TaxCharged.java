package com.maan.eway.req.push;

import com.fasterxml.jackson.annotation.JsonProperty;

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
@Builder
@ToString
@Jacksonized
@XmlAccessorType(XmlAccessType.FIELD)
@AllArgsConstructor
@NoArgsConstructor
public class TaxCharged {

	@JsonProperty("TaxCode")
	@XmlElement(name = "TaxCode")
	private String taxCode;
	
	@JsonProperty("IsTaxExempted")
	@XmlElement(name = "IsTaxExempted")
	private String isTaxExempted;
	
	@JsonProperty("TaxExemptionType")
	@XmlElement(name = "TaxExemptionType")
	private String taxExemptionType;
	
	@JsonProperty("TaxExemptionReference")
	@XmlElement(name = "TaxExemptionReference")
	private String taxExemptionReference;
	
	@JsonProperty("TaxRate")
	@XmlElement(name = "TaxRate")
	private String taxRate;
	
	@JsonProperty("TaxAmount")
	@XmlElement(name = "TaxAmount")
	private String taxAmount;
	
	

}