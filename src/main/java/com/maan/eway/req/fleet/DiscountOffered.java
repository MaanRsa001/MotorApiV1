package com.maan.eway.req.fleet;

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
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Jacksonized
@Builder
@XmlAccessorType(XmlAccessType.FIELD)
public class DiscountOffered {

	
	@JsonProperty("DiscountType") 
	@XmlElement(name = "DiscountType")
	private  String discountType;
	
	@JsonProperty("DiscountRate") 
	@XmlElement(name = "DiscountRate")
	private  String discountRate;
	@JsonProperty("DiscountAmount") 
	@XmlElement(name = "DiscountAmount")
    private String discountAmount;
}