package com.maan.eway.req.fleet;

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
public class TiraMsgVehiclePushFleet {
	@JsonProperty("MotorCoverNoteRefReq") 
	@XmlElement(name = "MotorCoverNoteRefReq")
	private FleetMotorCoverNoteRefReq motorCoverNoteRefReq;
	
	@JsonProperty("MsgSignature")
	@XmlElement(name = "MsgSignature")
	String msgSignature="";
}
