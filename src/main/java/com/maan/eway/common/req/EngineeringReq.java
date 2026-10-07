package com.maan.eway.common.req;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EngineeringReq {

	@JsonProperty("ProductId")
	private Integer productid;

	@JsonProperty("AnnualOpen")
	private String annualOpen;

	@JsonProperty("PrincipalOwner")
	private String principalOwner;

	@JsonProperty("Description")
	private String description;

	@JsonProperty("LocationId")
	private Integer locationId;

	@JsonProperty("RequestReferenceNo")
	private String requestReferenceNo;

	@JsonProperty("QuoteNo")
	private String quoteNo;

	@JsonProperty("PeriodOfActivity")
	private String periodOfActivity;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("StartDate")
	private Date startDate;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("YearOfManufacture")
	private String yearOfManufacture;

	@JsonProperty("Manufacture")
	private String manufacture;

	@JsonProperty("EngineNumber")
	private String engineNumber;

	@JsonProperty("SerialNumber")
	private String serialNumber;

	@JsonProperty("OwnershipTypeId")
	private Integer ownershipTypeId;

	@JsonProperty("OwnershipDesc")
	private String ownershipDesc;

	@JsonProperty("BasisOfValuationId")
	private Integer basisOfValuationId;

	@JsonProperty("BasisOfValuationDesc")
	private String basisOfValuationDesc;

	@JsonProperty("ConstructionType")
	private String constructionType;

	@JsonProperty("LocationName")
	private String locationName;

	@JsonProperty("SectionId")
	private String sectionId;

	@JsonProperty("PeriodType")
	private String periodType;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("MAINT_START_DATE")
	private Date maintStartDate;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("MAINT_END_DATE")
	private Date maintEndDate;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("TC_START_DATE")
	private Date tcStartDate;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("TC_END_DATE")
	private Date tcEndDate;

	@JsonProperty("SUB_CONTRA_NAME")
	private String subContraName;

	@JsonProperty("SUB_CONTRA_REMARKS")
	private String subContraRemarks;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("PROJECT_AWARDED_DATE")
	private Date projectAwardedDate;

	@JsonProperty("TYPE_OF_LICENCE")
	private String typeOfLicence;
	
	@JsonProperty("LIMIT_PER_CONTRACT")
	private String limitPerContract;
	
	@JsonProperty("NO_OF_CONTRACT")
	private String NoOfContract;
	
	@JsonProperty("MaxCargoCapacity")
	private String maxCargoCapacity;
	
	@JsonProperty("Param1")
	private String param1;
	
	@JsonProperty("Param2")
	private String param2;
	
	@JsonProperty("Param3")
	private String param3;
	
	@JsonProperty("Param4")
	private String param4;
	
	@JsonProperty("Param5")
	private String param5;

}
