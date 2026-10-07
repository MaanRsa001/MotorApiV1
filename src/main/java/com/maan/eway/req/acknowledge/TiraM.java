package com.maan.eway.req.acknowledge;

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
@ToString
@Jacksonized
@XmlAccessorType(XmlAccessType.FIELD)
@JacksonXmlRootElement(localName = "TiraMsg")
@XmlRootElement
public class TiraM {

	@JsonProperty("MotorCoverNoteRefResAck")
	@XmlElement(name = "MotorCoverNoteRefResAck")
	private MotorCoverNoteRefResAck motorCoverNoteRefResAck; 
	
	@JsonProperty("MsgSignature")
	@XmlElement(name = "MsgSignature")
	private String msgSignature="";
}
