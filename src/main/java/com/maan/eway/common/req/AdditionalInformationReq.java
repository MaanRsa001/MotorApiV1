package com.maan.eway.common.req;



import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdditionalInformationReq {

	private Long contentId;
	
    private String quoteNo;

    private String riskId;

    private String requestReferenceNo;

    private String companyId;

    private String productId;

    private String locationId;

    private String sectionId;

    private String coverId;

    private Date entryDate;

    private String param1;

    private String param2;

    private String value;

    private String param3;

    private String param4;

    private String param5;

    private String param6;

    private String param7;

    private String param8;

    private String param9;

    private String param10;

    private String status;

    private String endtTypeId;

    private String endtCount;

    private String endtStatus;
    
    private String endtReqrefNo;
    
    private String endtPrevQuote;
}
