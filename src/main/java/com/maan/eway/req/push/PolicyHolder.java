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
//@XmlRootElement(name="PolicyHolder")
@ToString
@Jacksonized
@XmlAccessorType(XmlAccessType.FIELD)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PolicyHolder {

	@JsonProperty("PolicyHolderName")
	@XmlElement(name = "PolicyHolderName")
	private String policyHolderName;
	
	
	@JsonProperty("PolicyHolderBirthDate")
	@XmlElement(name = "PolicyHolderBirthDate")
	private String policyHolderBirthDate;
	
	@JsonProperty("PolicyHolderType")
    @XmlElement(name = "PolicyHolderType")
	private String policyHolderType;
	
	
	@JsonProperty("PolicyHolderIdNumber")
	@XmlElement(name = "PolicyHolderIdNumber")
	private String policyHolderIdNumber;
	
	@JsonProperty("PolicyHolderIdType")
	@XmlElement(name = "PolicyHolderIdType")
	private String policyHolderIdType;
	
	@JsonProperty("Gender")
	@XmlElement(name = "Gender")
	private String gender;
	
	@JsonProperty("CountryCode")
	@XmlElement(name = "CountryCode")
    private String countryCode;
	

	@JsonProperty("Region")
	@XmlElement(name = "Region")
	private String region;
	
	@JsonProperty("District")
	@XmlElement(name = "District")
	private String district;
	

	@JsonProperty("Street")
	@XmlElement(name = "Street")
	private String street;
   

	@JsonProperty("PolicyHolderPhoneNumber")
	@XmlElement(name = "PolicyHolderPhoneNumber")
	private String policyHolderPhoneNumber;
	

	@JsonProperty("PolicyHolderFax")
	@XmlElement(name = "PolicyHolderFax")
	private String policyHolderFax;
	

	@JsonProperty("PostalAddress")
	@XmlElement(name = "PostalAddress")
	private String postalAddress;
	
	
	@JsonProperty("EmailAddress")
	@XmlElement(name = "EmailAddress")
	private String emailAddress;
	

	/*
	
	@JsonProperty("IsMulti")
	@XmlElement(name = "IsMulti")
	private String isMulti;
	*/

	
	
	
	
  
}