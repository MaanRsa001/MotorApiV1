package com.maan.eway.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name="MotorVerificationReq")
@XmlAccessorType(XmlAccessType.FIELD)
public class MotorVerificationReq {

	@JsonProperty("VerificationHdr")
	@XmlElement(name = "VerificationHdr")
	private VerificationHdrReq verificationHdr ;
	
	@JsonProperty("VerificationDtl")
	@XmlElement(name = "VerificationDtl")
	private VerificationDetailsReq verificationDtl ;
	
}
