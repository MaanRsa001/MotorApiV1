package com.maan.eway.nonmotor.onetimeinsert;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.maan.eway.error.Error;
import com.maan.eway.nonmotor.onetimeinsert.request.NonMotorOneTimeRequest;
import com.maan.eway.nonmotor.onetimeinsert.request.PersonalAccidentRequest;

@Component
public class NonMotorInputValidation {

	public List<Error> nonotorReq(NonMotorOneTimeRequest req) {
		List<Error> errorList = new ArrayList<>();
		
		if("13".equals(req.getProductId())) {
			if(StringUtils.isBlank(req.getInsuranceId())) {
				errorList.add(new Error("1","InsuranceId","InsuranceId is mandatory"));
			}if(StringUtils.isBlank(req.getProductId())) {
				errorList.add(new Error("1","ProductId","ProductId is mandatory"));
			}if(StringUtils.isBlank(req.getCreatedBy())) {
				errorList.add(new Error("1","CreatedBy","InsuranceId is CreatedBy"));
			}if(StringUtils.isBlank(req.getExchangeRate())) {
				errorList.add(new Error("1","ExchangeRate","Please Enter ExchangeRate"));
			}if(StringUtils.isBlank(req.getPolicyStartDate())) {
				errorList.add(new Error("1","PolicyStartDate","Please Enter PolicyStartDate"));
			}if(StringUtils.isBlank(req.getPolicyEndDate())) {
				errorList.add(new Error("1","PolicyEndDate","Please Enter PolicyStartDate"));
			}if(StringUtils.isBlank(req.getAgencyCode())) {
				errorList.add(new Error("1","AgencyCode","AgencyCode is mandatory"));
			}if(StringUtils.isBlank(req.getSubUsertype())) {
				errorList.add(new Error("1","SubUsertype","SubUsertype is mandatory"));
			}if(StringUtils.isBlank(req.getBdmCode())) {
				errorList.add(new Error("1","BdmCode","BdmCode is mandatory"));
			}if(StringUtils.isBlank(req.getBranchCode())) {
				errorList.add(new Error("1","BranchCode","BranchCode is mandatory"));
			}if(StringUtils.isBlank(req.getCurrency())) {
				errorList.add(new Error("1","Currency","Please choose currency"));
			}if(StringUtils.isBlank(req.getCustomerReferenceNo())) {
				errorList.add(new Error("1","CustomerReferenceNo","CustomerReferenceNo is mandatory"));
			}if(StringUtils.isBlank(req.getBrokerBranchCode())) {
				errorList.add(new Error("1","BrokerBranchCode","BrokerBranchCode is mandatory"));
			}if("Y".equals(req.getHavepromocode()) && StringUtils.isBlank(req.getPromoCode())) {
				errorList.add(new Error("1","PromoCode","PromoCode is mandatory"));
			}if(StringUtils.isBlank(req.getCustomerName())) {
				errorList.add(new Error("1","CustomerName","CustomerName is mandatory"));
			}
			
			if(req.getPaRequest().isEmpty()) {
				errorList.add(new Error("1","PersonalAccident","PersonalAccident Info Not Found"));
			}else {
				int index =1;
				for(PersonalAccidentRequest r : req.getPaRequest()) {
					if(StringUtils.isBlank(r.getDob())) {
						errorList.add(new Error("Index : "+index+"","Dob","Please enter Dob"));
					}if(StringUtils.isBlank(r.getOccupationId())) {
						errorList.add(new Error("Index : "+index+"","OccupationId","Please enter OccupationId"));
					}if(StringUtils.isBlank(r.getPersonName())) {
						errorList.add(new Error("Index : "+index+"","PersonName","Please enter PersonName"));
					}if(StringUtils.isBlank(r.getSalary())) {
						errorList.add(new Error("Index : "+index+"","Salary","Please enter salary"));
					}if(StringUtils.isBlank(r.getRiskId())) {
						errorList.add(new Error("Index : "+index+"","RiskId","Please enter RiskId"));
					}if(StringUtils.isBlank(r.getLocationName())) {
						errorList.add(new Error("Index : "+index+"","LocationName","Please enter LocationName"));
					}if(StringUtils.isBlank(r.getSectionId())) {
						errorList.add(new Error("Index : "+index+"","SectionId","Please enter SectionId"));
					}if(StringUtils.isBlank(req.getBranchCode())) {
						errorList.add(new Error("Index : "+index+"","BranchCode","BranchCode is mandatory"));
					}
					
					index ++;
				}
			}
			
		}
		return errorList;
	}

}
