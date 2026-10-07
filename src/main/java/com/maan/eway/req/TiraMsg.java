package com.maan.eway.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Data
@XmlRootElement(name="TiraMsg")
@ToString
@Jacksonized
@XmlAccessorType(XmlAccessType.FIELD)
public class TiraMsg {

	@JsonProperty("MotorVerificationReq")
	//@XmlElement(name = "MotorVerificationReq")
	private MotorVerificationReq MotorVerificationReq ;
	
	@JsonProperty("MsgSignature")
	//@XmlElement(name = "MotorVerificationReq")
	private String MsgSignature ;
	
}
