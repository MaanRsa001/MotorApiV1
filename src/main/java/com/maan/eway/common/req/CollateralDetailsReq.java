package com.maan.eway.common.req;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CollateralDetailsReq{

    @JsonProperty("COMPANY_ID")
    private String companyId;

    @JsonProperty("REQUEST_REFERENCE_NO")
    private String requestReferenceNo;

    @JsonProperty("QUOTE_NO")
    private String quoteNo;

    @JsonProperty("PRODUCT_ID")
    private String productId;

    @JsonProperty("PRODUCT_DESC")
    private String productDesc;

    @JsonProperty("SECTION_ID")
    private String sectionId;

    @JsonProperty("LOCATION_ID")
    private String locationId;

    @JsonProperty("SECTION_DESC")
    private String sectionDesc;

    @JsonProperty("COLLATERAL_TYPE_ID")
    private String collateralTypeId;

    @JsonProperty("COLLATERAL_DESC")
    private String collateralDesc;

    @JsonProperty("PARAM1")
    private String param1;

    @JsonProperty("PARAM2")
    private String param2;

    @JsonProperty("PARAM3")
    private String param3;

    @JsonProperty("PARAM4")
    private String param4;

    @JsonProperty("PARAM5")
    private String param5;

    @JsonProperty("COLLATERL_VALUE")
    private String collaterlValue;

    @JsonProperty("ENTRY_DATE")
    private Date entryDate;

    @JsonProperty("STATUS")
    private String status;

    @JsonProperty("REMARKS")
    private String remarks;
}
