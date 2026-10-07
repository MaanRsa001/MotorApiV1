package com.maan.eway.common.req;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FireRiskInfoSaveReq {

    private String requestReferenceNo;
    private String quoteNo;
    private Integer productId;
    private Integer sectionId;
    private Integer companyId;
    private Integer locationId;
    private String locationName;
    private Integer typeOfIndustry;
    private String typeOfIndustryDesc;
    private Integer claddingType;
    private String claddingTypeDesc;
    private Integer constructionType;
    private String constructionTypeDesc;
    private Integer occupancyType;
    private String occupancyTypeDesc;
    private String noOfFloors;
    private String ageOfBuilding;
    private String sprinklerYn;
    private String extinguisherYn;
    private String hydrantYn;
    private String watchmanYn;
    private String surveyedYn;
    private String riskLongitude;
    private String riskLatitude;
    private String gpsLocation;
    private String riskFullAddress;
    private String param1;
    private String param2;
    private String param3;
    private String param4;
    private String param5;
    @JsonFormat(pattern = "dd/MM/yyyy")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate startDate;
 
    @JsonFormat(pattern = "dd/MM/yyyy")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate endDate;
    private String status;
}