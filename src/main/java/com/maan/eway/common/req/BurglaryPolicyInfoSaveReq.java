package com.maan.eway.common.req;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
 

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BurglaryPolicyInfoSaveReq {

	@JsonProperty("Id")
    private String id   ;
	@JsonProperty("InsuranceId")
    private String companyId   ;
	@JsonProperty("BranchCode")
    private String branchCode ;
	@JsonProperty("ProductId")
    private String productId   ;
	@JsonProperty("SectionId")
    private String sectionId    ;
	@JsonProperty("RiskId")
    private String    riskId   ;
	@JsonProperty("RequestReferenceNo")
    private String     requestReferenceNo ;	
	@JsonProperty("QuoteNo")
    private String  quoteNo;
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EntryDate")
    private Date  entryDate ;
	@JsonProperty("CreatedBy")
    private String createdBy;
	@JsonProperty("Status")
    private String   status  ;
	@JsonProperty("LeftUninhabited")
    private String leftUninhabited;
	@JsonProperty("UninhabitedPerYear")
    private String UninhabitedPerYear;
	@JsonProperty("UninhabitedDays")
    private String UninhabitedDays;
	@JsonProperty("PremisesOccupied")
    private String premisesOccupied;
	@JsonProperty("ExtentOfLoss")
    private String extentOfLoss;
	@JsonProperty("PrecautionTaken")
    private String precautionTaken;
	@JsonProperty("StockBookMaintained")
    private String stockBookMaintained;
	@JsonProperty("StockBookLoss")
    private String stockBookLoss;
	@JsonProperty("LastStockTaken")
    private String lastStockTaken;
	@JsonProperty("SafetyLockerName")
    private String safetyLockerName;
	@JsonProperty("BurglarResistingSafe")
    private String burglarResistingSafe;
	@JsonProperty("SafetyLockerHeight")
    private String safetyLockerHeight;
	@JsonProperty("SafetyLockerWidth")
    private String safetyLockerWidth;
	@JsonProperty("SafetyLockerDepth")
    private String safetyLockerDepth;
	@JsonProperty("SafetyLockerStructure")
    private String safetyLockerStructure;
	@JsonProperty("NumberOfKeys")
    private String numberOfKeys;
	@JsonProperty("OpenOptionId")
    private String openOptionId;

	
	
}
