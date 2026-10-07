package com.maan.eway.req.acknowledge;

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
@XmlRootElement(name="MotorCoverNoteRefRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class MotorCoverNoteRefResFleet {
	@JsonProperty("FleetResHdr")
	@XmlElement(name = "FleetResHdr")
	private FleetResHdr fleetResHdr;
	@JsonProperty("FleetResDtl")
	@XmlElement(name = "FleetResDtl")
    private List<FleetResDtl> fleetResDtl;

}
