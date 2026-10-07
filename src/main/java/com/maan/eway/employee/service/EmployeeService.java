package com.maan.eway.employee.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.eway.common.res.CommonRes;
import com.maan.eway.employee.bean.EmployeeDto;
import com.maan.eway.employee.bean.EmployeeInfo;
import com.maan.eway.employee.bean.EmployeeInfoRepo;
import com.maan.eway.error.Error;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeInfoRepo empRepo;
	
	public List<Error> validateEnpDetails(EmployeeDto request) {
		try
		{
			  List<Error> error = new ArrayList<>();
			  if (StringUtils.isBlank(request.getEmployeeName())) {
			    	error.add(new Error("01", "EmployeeName","Please Enter Employee Name"));
			  } else {
				  if (request.getEmployeeName().length() > 100) {
			            error.add(new Error("02", "EmployeeName","Employee Name must not exceed 100 characters"));
			        }
				  if (!request.getEmployeeName().matches("^[A-Za-z0-9&.\\-'(), ]+$")) {
			            error.add(new Error("03", "EmployeeName","Employee Name contains invalid characters. Allowed: letters, numbers, spaces, &, ., -, ', comma, (, )"));
			        }
			    }
			  
//			  if (StringUtils.isBlank(request.getVisaNo())) {
//				  error.add(new Error("04", "I.D No (Visa No)","Please Enter I.D No (Visa No)"));
//			  } else if (!request.getVisaNo().matches("^\\d+$")) {
//				  error.add(new Error("05", "I.D No (Visa No)", "I.D No (Visa No) must contain only numeric digits"));
//			  } else if (request.getVisaNo().length() < 9 || request.getVisaNo().length() > 10) {
//				  error.add(new Error("06", "I.D No (Visa No)","I.D No (Visa No) must be between 9 and 10 digits"));
//			  }
//			// Visa Expiry Date
//			// Visa Issue Date (validated first so it's safe to use below)
//			  if (request.getVisaIssueDate() == null) {
//			      error.add(new Error("19", "VisaIssueDate","Please Select Visa Issue Date"));
//			  }
//
//			  // Visa Expiry Date
//			  if (request.getVisaExpiryDate() == null) {
//			      error.add(new Error("07", "VisaExpiryDate","Please Enter Visa Expiry Date"));
//			  } else {
//			      Date today = new Date();
//
//			      // 1. Must be a future date, always checked against today
//			      if (!request.getVisaExpiryDate().after(today)) {
//			          error.add(new Error("08", "VisaExpiryDate","Visa Expiry Date must be a future date"));
//			      }
//			      // 2. "Expiring soon" warning, checked against Visa Issue Date + 6 months
//			      else if (request.getVisaIssueDate() != null) {
//			          Calendar cal = Calendar.getInstance();
//			          cal.setTime(request.getVisaIssueDate());
//			          cal.add(Calendar.MONTH, 6);
//			          if (request.getVisaExpiryDate().before(cal.getTime())) {
//			              SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//			              error.add(new Error("09", "VisaExpiryDate", "Warning: Visa is expiring within 6 months (expiry: "
//			                      + sdf.format(request.getVisaExpiryDate()) + "). Please ensure renewal is in progress."));
//			          }
//			      }
//
//			      // 3. Visa Issue Date must be earlier than Visa Expiry Date
//			      if (request.getVisaIssueDate() != null && !request.getVisaIssueDate().before(request.getVisaExpiryDate())) {
//			          error.add(new Error("20", "VisaIssueDate","Visa Issue Date must be earlier than Visa Expiry Date"));
//			      }
//			  }
			  
			  if (StringUtils.isBlank(request.getPassportNo())) {

				    error.add(new Error("10", "PassportNo","Please Enter Passport Number"));
			  } else {
				  request.setPassportNo(request.getPassportNo().trim().toUpperCase());
				  if (!request.getPassportNo().matches("^[A-Z0-9]+$")) {
					  error.add(new Error("11", "PassportNo","Passport Number must contain only letters and numbers"));
				  }
				    else if (request.getPassportNo().length() < 6 || request.getPassportNo().length() > 9) {
				    	error.add(new Error("12", "PassportNo","Passport Number must be between 6 and 9 characters"));
				    }
				}
			  if (request.getPassportExpiryDate() == null) {

			      error.add(new Error("13", "PassportExpiryDate","Please Enter Passport Expiry Date"));

			  } else {
				  Date today = new Date();
			      if (!request.getPassportExpiryDate().after(today)) {

			          error.add(new Error("14", "PassportExpiryDate","Passport Expiry Date must be a future date"));
			      } else {
			    	  Calendar cal = Calendar.getInstance();
			          cal.setTime(today);
			          cal.add(Calendar.MONTH, 6);

			          if (request.getPassportExpiryDate().before(cal.getTime())) {
			        	  SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			              error.add(new Error("15", "PassportExpiryDate","Warning: Passport is expiring within 6 months (expiry: "+ sdf.format(request.getPassportExpiryDate()) + "). Please ensure renewal is in progress."));
			          }
			      }
			  }
			  
			  if (StringUtils.isBlank(request.getNationality())) {
				  error.add(new Error("16", "Nationality","Please Select Nationality"));
			  }
			// Date of Birth
			  if (request.getDateOfBirth() == null) {

			      error.add(new Error("17", "DateOfBirth", "Please Select Date of Birth"));

			  } else {
			  LocalDate dob = request.getDateOfBirth();
			  LocalDate today = LocalDate.now();

			  int age = Period.between(dob, today).getYears();

			  if (age < 18 || age > 61) {
			      error.add(new Error("18", "DateOfBirth",
			              "Employee age must be between 18 and 61 years"));
			  }}
//			// Visa Issue Date
//			  if (request.getVisaIssueDate() == null) {
//
//			      error.add(new Error("19", "VisaIssueDate",
//			              "Please Select Visa Issue Date"));
//
//			  } else {
//				  if (request.getVisaExpiryDate() != null && !request.getVisaIssueDate().before(request.getVisaExpiryDate())) {
//			    	  error.add(new Error("20", "VisaIssueDate","Visa Issue Date must be earlier than Visa Expiry Date"));
//			      }
//			  }
			  if (StringUtils.isBlank(request.getMobileNo())) {

				    error.add(new Error("21", "MobileNo","Please Enter Mobile Number"));

				} else if (!request.getMobileNo().matches("^[79]\\d{7}$")) {
					error.add(new Error("22", "MobileNo","Mobile Number must be 8 digits and start with 7 or 9"));
				}
			    return error;
		}catch (Exception e) {
	        e.printStackTrace();
		}

		return null;
	}
	
	

	public CommonRes insertEmp(EmployeeDto request) {

	    CommonRes res = new CommonRes();

	    try {
	    	
	 //   	List<EmployeeInfo> employeeList = empRepo.findByPassportNoAndCustomerReferenceNo( request.getPassportNo(),request.getCustomerReferenceNo());
	    	
	    	if(StringUtils.isNotBlank(request.getRequestReferenceNo())) {
	    		
	    		List<EmployeeInfo> employeeList = empRepo.findByRequestReferenceNoOrderByEmpIdDesc( request.getRequestReferenceNo());
		         if (!employeeList.isEmpty()) {
		        	 Long empId = employeeList.get(0).getEmpId();
		        	 request.setEmpId(empId);
		             empRepo.deleteAll(employeeList);
		             
		         }
	    		
	    	}
	    	 

	        EmployeeInfo entity = new EmployeeInfo();
	      //  entity.setRequestReferenceNo(request.getRequestReferenceNo());
	        if( request.getEmpId()!= null)
	        {
	        	entity.setEmpId(request.getEmpId());
	        }
	        else
	        {
	        	Long maxEmpId = empRepo.findMaxEmpId();
	            entity.setEmpId(maxEmpId == null ? 1L : maxEmpId + 1);
	        }
	        entity.setEmployeeName(request.getEmployeeName());
	        entity.setVisaNo(request.getVisaNo());
	        entity.setVisaExpiryDate(request.getVisaExpiryDate());
	        entity.setPassportNo(request.getPassportNo());
	        entity.setPassportExpiryDate(request.getPassportExpiryDate());
	        entity.setNationality(request.getNationality());
	        if (request.getDateOfBirth() != null) {
	            Date dob = Date.from( request.getDateOfBirth().atStartOfDay(ZoneId.systemDefault()).toInstant());
	            entity.setDateOfBirth(dob);
	        }
	        entity.setGender(request.getGender());
	        entity.setVisaIssueDate(request.getVisaIssueDate());
	        entity.setCountryEntryDate(request.getCountryEntryDate());
	        entity.setMobileNo(request.getMobileNo());
	        entity.setCurrentHouseLocation(request.getCurrentHouseLocation());
	        entity.setHouseNo(request.getHouseNo());
	        entity.setGovernorate(request.getGovernorate());
	        entity.setCity(request.getCity());
	        entity.setCountry(request.getCountry());
	        entity.setCustomerId(request.getCustomerId());
	        entity.setCustomerReferenceNo(request.getCustomerReferenceNo());
	        entity.setLoginId(request.getLoginId());
	        entity.setApplicationId(request.getApplicationId());
	        entity.setEntryDate(new Timestamp(System.currentTimeMillis()));
	        entity.setCompanyId(request.getCompanyId());
	        entity.setIdOrVisa(request.getIdOrVisa());
	        entity.setOccupation(request.getOccupation());
	        entity.setEmailId(request.getEmailId());
	        entity.setTypeOfContractor(request.getTypeOfContractor());
	        entity.setOccupationDesc(request.getOccupationDesc());
	        entity.setNationalityDesc(request.getNationalityDesc());
	        entity.setAreaLocality(request.getAreaLocality());
	        empRepo.save(entity);

	        res.setIsError(false);
	        res.setMessage("Employee details saved successfully");
	        res.setCommonResponse(entity);

	    } catch (Exception e) {
	        e.printStackTrace();
	        res.setIsError(true);
	        res.setMessage("Failed to save employee details");
	        res.setCommonResponse("Failed to save employee details" + e);
	    }

	    return res;
	}


	public CommonRes getEmp(EmployeeDto request) {

	    CommonRes res = new CommonRes();

	    try {

	        List<EmployeeInfo> employeeList = empRepo.findByRequestReferenceNoOrderByEntryDateDesc( request.getRequestReferenceNo());
	        
	        
	        if (employeeList != null && !employeeList.isEmpty()) {

	        	List<EmployeeDto> dtoList = new ArrayList<>();
	        	
	        	EmployeeInfo entity =employeeList.get(0);

	        	    EmployeeDto dto = new EmployeeDto();

	        	    dto.setRequestReferenceNo(entity.getRequestReferenceNo());
	        	    dto.setEmpId(entity.getEmpId());
	        	    dto.setEmployeeName(entity.getEmployeeName());
	        	    dto.setVisaNo(entity.getVisaNo());
	        	    dto.setVisaExpiryDate(entity.getVisaExpiryDate());
	        	    dto.setPassportNo(entity.getPassportNo());
	        	    dto.setPassportExpiryDate(entity.getPassportExpiryDate());
	        	    dto.setNationality(entity.getNationality());
	        	    dto.setAreaLocality(entity.getAreaLocality());
	        	    if (entity.getDateOfBirth() != null) {
	        	        java.sql.Date sqlDate =
	        	                new java.sql.Date(entity.getDateOfBirth().getTime());
	        	        dto.setDateOfBirth(sqlDate.toLocalDate());
	        	    }

	        	    dto.setGender(entity.getGender());
	        	    dto.setVisaIssueDate(entity.getVisaIssueDate());
	        	    dto.setCountryEntryDate(entity.getCountryEntryDate());
	        	    dto.setMobileNo(entity.getMobileNo());
	        	    dto.setCurrentHouseLocation(entity.getCurrentHouseLocation());
	        	    dto.setHouseNo(entity.getHouseNo());
	        	    dto.setGovernorate(entity.getGovernorate());
	        	    dto.setCity(entity.getCity());
	        	    dto.setCountry(entity.getCountry());
	        	    dto.setCustomerId(entity.getCustomerId());
	        	    dto.setCustomerReferenceNo(entity.getCustomerReferenceNo());
	        	    dto.setLoginId(entity.getLoginId());
	        	    dto.setApplicationId(entity.getApplicationId());
	        	    dto.setEntryDate(entity.getEntryDate());
	        	    dto.setCompanyId(entity.getCompanyId());
	        	    dto.setIdOrVisa(entity.getIdOrVisa());
	        	    dto.setOccupation(entity.getOccupation());
	        	    dto.setEmailId(entity.getEmailId());
	        	    dto.setTypeOfContractor(entity.getTypeOfContractor());

	        	res.setIsError(false);
	        	res.setMessage("Employee details fetched successfully");
	        	res.setCommonResponse(dto);

	        } else {
	            res.setIsError(true);
	            res.setMessage("Employee not found");
	            res.setCommonResponse(Collections.emptyList());
	        }

	    } catch (Exception e) {
	    	e.printStackTrace();
	        res.setIsError(true);
	        res.setMessage("Failed to fetch employee details");
	        res.setCommonResponse(e.getMessage());
	    }

	    return res;
	}
}
