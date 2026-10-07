package com.maan.eway.common.res;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class NonMotorHumanRes {

	@JsonProperty("RiskId ")
	private Integer riskId;

	@JsonProperty("SectionId ")
	private String sectionId;

	@JsonProperty("OriginalRiskId ")
	private Integer originalRiskId;

	@JsonProperty("OccupationDesc ")
	private String occupationDesc;

	@JsonProperty("OccupationType ")
	private String occupationType;

	@JsonProperty("CategoryId ")
	private String categoryId;

	@JsonProperty("SumInsured ")
	private BigDecimal sumInsured;

	@JsonProperty("Status ")
	private String status;

	@JsonProperty("Count")
	private Integer count;

	@JsonProperty("OldReqRefNo")
	private String oldReqRefNo;

	@JsonProperty("BenefitCoverMonth")
	private Integer benefitCoverMonth;

	@JsonProperty("SalaryPerAnnum")
	private BigDecimal salaryPerAnnum;

	@JsonProperty("PolicyPeriod")
	private Integer policyPeriod;

	@JsonProperty("JobJoiningMonth")
	 private String     jobJoiningMonth ;


	@JsonProperty("BetweenDiscontinued")
	private String betweenDiscontinued;

	@JsonProperty("EthicalWorkInvolved")
	private String ethicalWorkInvolved;

	@JsonProperty("NatureOfBusinessId")
	private Long natureOfBusinessId;

	@JsonProperty("NatureOfBusinessDesc")
	private String natureOfBusinessDesc;

	@JsonProperty("TotalNoOfEmployees")
	private Long totalNoOfEmployees;

	@JsonProperty("TotalExcludedEmployees")
	private Long totalExcludedEmployees;

	@JsonProperty("TotalRejoinedEmployees")
	private Long totalRejoinedEmployees;

	@JsonProperty("AccountOutstandingEmployees")
	private Long accountOutstandingEmployees;

	@JsonProperty("TotalOutstandingAmount")
	private BigDecimal totalOutstandingAmount;

	@JsonProperty("AccountAuditentType")
	private Integer accountAuditentType;

	@JsonProperty("AuditentTypeDesc")
	private String auditentTypeDesc;

	@JsonProperty("LiabilitySi")
	private BigDecimal liabilitySi;

	@JsonProperty("FidEmpCount")
	private BigDecimal fidEmpCount;

	@JsonProperty("FidEmpSi")
	private BigDecimal fidEmpSi;

	@JsonProperty("EmpLiabilitySi")
	private BigDecimal empLiabilitySi;

	@JsonProperty("PersonalLiabilitySi")
	private BigDecimal personalLiabilitySi;

	@JsonProperty("PersonalLiabilityOccupation")
	private String personalLiabilityOccupation;

	@JsonProperty("PersonalLiabilityCategory")
	private BigDecimal personalLiabilityCategory;

	@JsonProperty("AooSuminsured")
	private BigDecimal aooSuminsured;

	@JsonProperty("AggSuminsured")
	private BigDecimal aggSuminsured;

	@JsonProperty("ProductTurnoverSi")
	private BigDecimal productTurnoverSi;

	@JsonProperty("AnyAccidentSi")
	private BigDecimal anyAccidentSi;

	@JsonProperty("InsurancePeriodSi")
	private BigDecimal insurancePeriodSi;

	@JsonProperty("OtherOccupation")
	private String otherOccupation;

	@JsonProperty("TtdSumInsured")
	private Integer ttdSumInsured;

	@JsonProperty("MeSumInsured")
	private Integer meSumInsured;

	@JsonProperty("FeSumInsured")
	private Integer feSumInsured;

	@JsonProperty("RelationType")
	private String relationType;

	@JsonProperty("RelationTypeDesc")
	private String relationTypeDesc;

	@JsonProperty("NickName")
	private String nickName;

	@JsonProperty("Age")
	private Integer age;

	@JsonProperty("PtdSumInsured")
	private Integer ptdSumInsured;

	@JsonProperty("GroupId")
	private Integer groupId;

	@JsonProperty("ProfessionalType")
	private String professionalType;

	@JsonProperty("ProfessionalTypeDesc")
	private String professionalTypeDesc;

	@JsonProperty("IndemnityType")
	private String indemnityType;

	@JsonProperty("IndemnityTypeDesc")
	private String indemnityTypeDesc;

	@JsonProperty("IndemnitySuminsured")
	private BigDecimal indemnitySuminsured;

	@JsonProperty("DomesticServentSi")
	private BigDecimal domesticServentSi;
	
	@JsonProperty("Param1")
    private String  Param1 ;

	@JsonProperty("Param2")
    private String  Param2 ;
	

	@JsonProperty("Param3")
    private String  Param3 ;

	@JsonProperty("Param4")
    private String  Param4 ;
	
	@JsonProperty("Param5")
    private String  Param5 ;

	@JsonProperty("Param6")
    private String  Param6;

	@JsonProperty("Param7")
    private String  Param7 ;

	@JsonProperty("Param8")
    private String  Param8 ;

	@JsonProperty("Param9")
    private String  Param9 ;

	@JsonProperty("Param10")
    private String  Param10 ;


}
