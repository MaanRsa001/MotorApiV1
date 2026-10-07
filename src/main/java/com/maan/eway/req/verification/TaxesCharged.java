package com.maan.eway.req.verification;

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
//@XmlRootElement(name="TaxesCharged")
@ToString
@Jacksonized
@XmlAccessorType(XmlAccessType.FIELD)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaxesCharged {
 
   @JsonProperty("TaxCharged") 
   @XmlElement(name = "TaxCharged")
   private TaxCharged taxChargedBean ;
    
    
    
    
    
}