package com.maan.eway.req.acknowledge;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Jacksonized
@XmlRootElement(name="MotorCoverNoteRefResAck")
@XmlAccessorType(XmlAccessType.FIELD)
public class MotorCoverNoteRefResAck {
	/*<AcknowledgementId>NIC22424232355</AcknowledgementId>
	<ResponseId>TIRA22424232355</ResponseId >
	<AcknowledgementStatusCode>TIRA001</AcknowledgementStatusCode>
	<AcknowledgementStatusDesc>Successful</AcknowledgementStatusDesc>*/
	@JsonProperty("AcknowledgementId")
	@XmlElement(name = "AcknowledgementId")
	private String acknowledgementId;
	
	@JsonProperty("ResponseId")
	@XmlElement(name = "ResponseId")
	private String responseId;
	
	@JsonProperty("AcknowledgementStatusCode")
	@XmlElement(name = "AcknowledgementStatusCode")
	private String acknowledgementStatusCode;
	
	@JsonProperty("AcknowledgementStatusDesc")
	@XmlElement(name = "AcknowledgementStatusDesc")
	private String acknowledgementStatusDesc;
	
}
