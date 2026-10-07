package com.maan.eway.req.push;
 
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
public class CoverNoteHdr {
	/*
	 * 
	 * <RequestId>NIC22424232355</RequestId>
<CompanyCode>IC0003</CompanyCode>
<SystemCode>SYS0003</SystemCode>
<CallBackUrl>http://nic.co.tz/api/CoverNoteref/response
</CallBackUrl>
<InsurerCompanyCode>IC001</InsurerCompanyCode>
<TranCompanyCode>IC001</TranCompanyCode>
<CoverNoteType>1</CoverNoteType>
	 */
	
	@JsonProperty("RequestId") 
	@XmlElement(name = "RequestId")
    String requestId="";
	@JsonProperty("CompanyCode") 
	@XmlElement(name = "CompanyCode")
    String companyCode="";
	@JsonProperty("SystemCode") 
	@XmlElement(name = "SystemCode")
    String systemCode="";	
	@JsonProperty("CallBackUrl") 
	@XmlElement(name = "CallBackUrl")
    String callBackUrl="";

	
	@JsonProperty("InsurerCompanyCode")
	@XmlElement(name = "InsurerCompanyCode")
    String insurerCompanyCode="";
	@JsonProperty("TranCompanyCode") 
	@XmlElement(name = "TranCompanyCode")
    String tranCompanyCode="";
	
	@JsonProperty("CoverNoteType") 
	@XmlElement(name = "CoverNoteType")
    String coverNoteType="";
	
	 
	
  
}