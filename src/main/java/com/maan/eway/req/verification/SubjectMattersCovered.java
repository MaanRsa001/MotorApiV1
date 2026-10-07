package com.maan.eway.req.verification;
 
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;
@Data
//@XmlRootElement(name="SubjectMattersCovered")
@ToString
@Jacksonized
@XmlAccessorType(XmlAccessType.FIELD)
 
@AllArgsConstructor
@NoArgsConstructor
public class SubjectMattersCovered {
 
	@JacksonXmlElementWrapper(useWrapping=false)
    @JsonProperty("SubjectMatter") 
	@XmlElement(name = "SubjectMatter")
    List< SubjectMatter> subjectMatterBeanList ;
  
}