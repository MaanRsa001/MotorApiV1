package com.maan.eway.req.fleet;

import java.util.List;

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
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="CoverNoteDtl")
public class FleetCoverNoteDtl {

	@JsonProperty("FleetHdr") 
	@XmlElement(name = "FleetHdr")
	private FleetHdr fleetHdr;
	
	@JsonProperty("FleetDtl") 
	@XmlElement(name = "FleetDtl")
	private List<FleetDtl> fleetDtl;	
}
