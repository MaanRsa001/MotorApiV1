package com.maan.eway.res;

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
@XmlAccessorType(XmlAccessType.FIELD)
@Builder
public class MotorCoverNoteRefReqAck {
	@JsonProperty("AcknowledgementId")
	@XmlElement(name = "AcknowledgementId")
	private String AcknowledgementId;
	@JsonProperty("RequestId")
	@XmlElement(name = "RequestId")
	private String RequestId;
	@JsonProperty("AcknowledgementStatusCode")
	@XmlElement(name = "AcknowledgementStatusCode")
	private String AcknowledgementStatusCode;
	@JsonProperty("AcknowledgementStatusDesc")
	@XmlElement(name = "AcknowledgementStatusDesc")
	private String AcknowledgementStatusDesc;
}
