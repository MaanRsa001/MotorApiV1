package com.maan.eway.employee.bean;

import java.time.LocalDate;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class EmployeeDto {

	private Long empId;
	
	private String requestReferenceNo;
	
	private String idOrVisa;
	
    private String employeeName;

    private String visaNo;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date visaExpiryDate;

    private String passportNo;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date passportExpiryDate;

    private String nationality;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateOfBirth;

    private String gender;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date visaIssueDate;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date countryEntryDate;

    private String mobileNo;

    private String currentHouseLocation;

    private Integer houseNo;

    private String governorate;

    private String city;

    private String country;

    private String customerId;

    private String customerReferenceNo;

    private String loginId;

    private String applicationId;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date entryDate;

    private String companyId;
    
    private String occupation;
    
    private String emailId;

	private String typeOfContractor;
	
	private String occupationDesc;
	
	private String nationalityDesc;
	
	private String areaLocality;
	
	
}
