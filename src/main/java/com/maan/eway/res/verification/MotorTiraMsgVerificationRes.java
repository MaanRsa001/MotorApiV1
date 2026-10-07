package com.maan.eway.res.verification;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
@XmlRootElement
@Jacksonized
@XmlAccessorType(XmlAccessType.FIELD)
@JacksonXmlRootElement(localName = "TiraMsg")
@Data
public class MotorTiraMsgVerificationRes {
	@JsonProperty("CoverNoteVerificationRes")
	private CoverNoteVerificationRes coverNoteVerifications;
	
	@JsonProperty("MsgSignature")
	private String msgSignature ;
	
}
