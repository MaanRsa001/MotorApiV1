package com.maan.eway.req.verification;

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
//@XmlRootElement(name="CoverNoteAddon")
@ToString
@Jacksonized
@XmlAccessorType(XmlAccessType.FIELD)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoverNoteAddon {

	@JsonProperty("AddonReference") 
	@XmlElement(name = "AddonReference")
	private String addonReference;
	@JsonProperty("AddonDesc") 
	@XmlElement(name = "AddonDesc")
	private String addonDesc;
	
	@JsonProperty("AddonAmount") 
	@XmlElement(name = "AddonAmount")
	private String addonAmount;
	
	@JsonProperty("AddonPremiumRate") 
	@XmlElement(name = "AddonPremiumRate")
	private String addonPremiumRate;
	
	/*@JsonProperty("IsMulti") 
	@XmlElement(name = "IsMulti")
	private String isMulti;*/
	@JsonProperty("PremiumExcludingTax")
	@XmlElement(name = "PremiumExcludingTax")
	private String premiumExcludingTax;
	@JsonProperty("PremiumExcludingTaxEquivalent")
	@XmlElement(name = "PremiumExcludingTaxEquivalent")
	private String premiumExcludingTaxEquivalent;
	@JsonProperty("PremiumIncludingTax")
	@XmlElement(name = "PremiumIncludingTax")
	private String premiumIncludingTax;

	@JsonProperty("TaxesCharged") 
	@XmlElement(name = "TaxesCharged")
	private TaxesCharged taxesChargedBean ;


}