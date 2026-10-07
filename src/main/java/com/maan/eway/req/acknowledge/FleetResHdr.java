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
@XmlRootElement(name="FleetResHdr")
@XmlAccessorType(XmlAccessType.FIELD)
public class FleetResHdr {
	@JsonProperty("RequestId")
	@XmlElement(name = "RequestId")
	private String requestId;
	
	@JsonProperty("FleetStatusCode")
	@XmlElement(name = "FleetStatusCode")
    private String fleetStatusCode;
	
	@JsonProperty("FleetId")
	@XmlElement(name = "FleetId")
    private String fleetId;
	
	@JsonProperty("ResponseId")
	@XmlElement(name = "ResponseId")
    private String responseId;
	
	@JsonProperty("FleetStatusDesc")
	@XmlElement(name = "FleetStatusDesc")
    private String fleetStatusDesc;

}
