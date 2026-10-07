package com.maan.eway.res;

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
public class MotorTiraMsgRes {

	@JsonProperty("MotorVerificationRes")
	@XmlElement(name = "MotorVerificationRes")
	private MotorVerificationRes motorVerificationRes ;
	
	@JsonProperty("MsgSignature")
	@XmlElement(name = "MsgSignature")
	private String msgSignature ;
	@JsonProperty("MotorCoverNoteRefReqAck")
	@XmlElement(name = "MotorCoverNoteRefReqAck")	
	private MotorCoverNoteRefReqAck motorcoverNote;
	
	@JsonProperty("CoverNoteRefReqAck")
	@XmlElement(name = "CoverNoteRefReqAck")	
	private MotorCoverNoteRefReqAck nonmotorcoverNote; 
}
