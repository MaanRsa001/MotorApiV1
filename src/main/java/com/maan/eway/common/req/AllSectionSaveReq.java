package com.maan.eway.common.req;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AllSectionSaveReq {

	@JsonProperty("RequestReferenceNo")
	private String requestReferenceNo;

	@JsonProperty("RiskId")
	private String riskId;

	@JsonProperty("ProductId")
	private String productId;

	@JsonProperty("InsuranceId")
	private String insuranceId;

	@JsonProperty("CreatedBy")
	private String createdBy;

	@JsonProperty("EndorsementDate") // EndorsementDate
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date endorsementDate;
	@JsonProperty("EndorsementRemarks") // EndorsementRemarks
	private String endorsementRemarks;
	@JsonProperty("EndorsementEffectiveDate") // EndorsementEffectiveDate
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date endorsementEffdate;
	@JsonProperty("OrginalPolicyNo") // OrginalPolicyNo
	private String originalPolicyNo;
	@JsonProperty("EndtPrevPolicyNo") // EndtPrevPolicyNo
	private String endtPrevPolicyNo;
	@JsonProperty("EndtPrevQuoteNo") // EndtPrevQuoteNo
	private String endtPrevQuoteNo;
	@JsonProperty("EndtCount") // EndtCount
	private BigDecimal endtCount;
	@JsonProperty("EndtStatus") // EndtStatus
	private String endtStatus;
	@JsonProperty("IsFinanceEndt") // IsFinanceEndt
	private String isFinaceYn;
	@JsonProperty("EndtCategoryDesc") // EndtCategoryDesc
	private String endtCategDesc;
	@JsonProperty("EndorsementType") // EndorsementType
	private Integer endorsementType;

	@JsonProperty("EndorsementTypeDesc") // EndorsementTypeDesc
	private String endorsementTypeDesc;
	
	// ----------domestic------------------

	@JsonProperty("BuildingDetails")
	private List<BuildingSecSaveReq> buildingData;

	@JsonProperty("AllRiskDetails")            // Updated to list
	private List<AllRiskSecSaveReq> allRiskData;

	@JsonProperty("EmployeeLiabilityDetails")
	private List<EmpLiabilitySecSaveReq> employeeLiabilityData;
	
	@JsonProperty("DomesticServant")
	private List<EmpLiabilitySecSaveReq> domesticServant;
	
	@JsonProperty("FireAndAlliedPerills")
	private List<BuildingSecSaveReq> fireAndAlliedPerills;

	@JsonProperty("ContentDetails")
	private ContentSecSaveReq contentData;
	
	@JsonProperty("ElectronicEquipment")
	private ContentSecSaveReq electronicEquipment;

	@JsonProperty("PersonalAccidentDetails")
	private List<PersonalAccidentSecSaveReq> personalAccidentData;
	
	
	// ---------------corporate plus -----------------------
	
	@JsonProperty("ElectronicEquipmentDetails")
	private List<ElectronicEquipmentSaveReq> electronicData;
	
	@JsonProperty("PublicLiabilityDetails")
	private PublicLiabilitySaveReq publicLiabilityData;
	
	@JsonProperty("MachinaryBreakdownDetails")
	private MachinaryBreakdownSaveReq machinaryData;
	
	@JsonProperty("FidelityEmployeeDetails")
	private List<FidelityEmpSaveReq> fidelityData;
	
	@JsonProperty("MoneyDetails")
	private List<MoneySaveReq> moneyData;
	
	@JsonProperty("BurglaryAndHouseDetails")
	private BurglaryAndHouseSaveReq burglaryData;
	
	@JsonProperty("BusinessInterruptionDetails")
	private BusinessSaveReq businessData;
	
	@JsonProperty("GoodsIntransitDetails")
    private GoodsIntransitSaveReq goodsIntransitData; 
}
