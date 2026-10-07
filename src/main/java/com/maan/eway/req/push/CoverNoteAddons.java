package com.maan.eway.req.push;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

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
//@XmlRootElement(name="CoverNoteAddons")
@ToString
@Jacksonized
@XmlAccessorType(XmlAccessType.FIELD)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoverNoteAddons {
	@JacksonXmlElementWrapper(useWrapping=false)
	@JsonProperty("CoverNoteAddon") 
	@XmlElement(name = "CoverNoteAddon")
    List< CoverNoteAddon> coverNoteAddonBeanList ;
   
}