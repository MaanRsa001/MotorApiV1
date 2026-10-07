package com.maan.eway.req.verification;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Data
@XmlRootElement(name="TiraMsg")
@ToString
@Jacksonized
@XmlAccessorType(XmlAccessType.FIELD)
@JacksonXmlRootElement(localName = "TiraMsg")
public class TiraMsgVehicleVerification {

	@JsonProperty("CoverNoteVerificationReq")
	@XmlElement(name = "CoverNoteVerificationReq")
	private CoverNoteVerificationReq coverNoteVerificationReq; 
	
	@JsonProperty("MsgSignature")
	@XmlElement(name = "MsgSignature")
	private String msgSignature="";

	
	
}
