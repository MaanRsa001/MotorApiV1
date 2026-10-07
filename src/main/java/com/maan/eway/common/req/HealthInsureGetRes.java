package com.maan.eway.common.req;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class HealthInsureGetRes {

	@JsonProperty("RequestReferenceNo")
    private String     requestReferenceNo ;
		
	@JsonProperty("ProductId")
    private String    productId    ;
	
	@JsonProperty("SectionId")
    private String sectionId    ;
	
	@JsonProperty("InsuranceId")
	private String insuranceId;
	
	@JsonProperty("CreatedBy")
	private String createdBy;
	
	@JsonProperty("RiskId")
    private String    riskId   ;

	@JsonProperty("RelationType")
	private String RelationType;
	
	@JsonProperty("RelationTypeDesc")
	private String RelationTypeDesc;
	
	
	@JsonFormat(pattern ="dd/MM/yyyy")
	@JsonProperty("DateOfBirth")
	private Date dateOfBirth;
	
	@JsonProperty("NickName")
	private String nickName;
	
	@JsonProperty("FirstName")
	private String firstName;
	
	@JsonProperty("LastName")
	private String lastName;
	
	@JsonProperty("NationalityId")
	private String nationalityId;
	
	@JsonProperty("OccupationId")
	private String occupationId;
	
	@JsonProperty("ProfessionalType")
	private String professionaltype;
	
	@JsonProperty("ProfessionalDesc")
	private String professionalDesc;
	
	@JsonProperty("InternityType")
	private String internitytype;
	
	@JsonProperty("InternityDesc")
	private String internityDesc;
	
	@JsonProperty("EmployeeCount")
	private Long employeeCount;
	
	@JsonProperty("GrossIncome")
	private BigDecimal grossIncome;
	
	@JsonProperty("IndernitySI")
	private Double  indernitysi ;
	
}
