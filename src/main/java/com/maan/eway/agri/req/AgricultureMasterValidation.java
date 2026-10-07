package com.maan.eway.agri.req;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import com.maan.eway.error.Error;

@Component
public class AgricultureMasterValidation {
	
	public List<Error> validateAgricultureMasterRequest(AgricultureMasterReq req) {
	    List<Error> errors = new ArrayList<>();

	    try {
	    	  if (req.getCompanyId() == null || req.getCompanyId() == 0) {
	        errors.add(new Error("1", "companyId", "Please Enter Company Id"));
	    }
	    if (req.getProductId() == null || req.getProductId() == 0) {
	        errors.add(new Error("2", "productId", "Please Enter Product Id"));
	    }
	    if (req.getProvinceId() == null || req.getProvinceId() == 0) {
	        errors.add(new Error("3", "provinceId", "Please Enter Province Id"));
	    }
	    if (StringUtils.isBlank(req.getProvinceDesc())) {
	        errors.add(new Error("4", "provinceDesc", "Please Enter Province Description"));
	    }
	    if (req.getDistrictId() == null || req.getDistrictId() == 0) {
	        errors.add(new Error("5", "districtId", "Please Enter District Id"));
	    }
	    if (StringUtils.isBlank(req.getDistrictDesc())) {
	        errors.add(new Error("6", "districtDesc", "Please Enter District Description"));
	    }
	    if (req.getAez() == null || req.getAez() == 0) {
	        errors.add(new Error("7", "aez", "Please Enter Agro Ecological Zone"));
	    }
	    if (req.getCropId() == null || req.getCropId() == 0) {
	        errors.add(new Error("8", "cropId", "Please Enter Crop Id"));
	    }
	    if (StringUtils.isBlank(req.getCropDesc())) {
	        errors.add(new Error("9", "cropDesc", "Please Enter Crop Description"));
	    }
	    if (req.getPerHaCost() == null || req.getPerHaCost() <= 0) {
	        errors.add(new Error("10", "perHaCost", "Please Enter Valid Per Hectare Cost"));
	    }
//	    if (req.getSectionId() == null || req.getSectionId() == 0) {
//	        errors.add(new Error("11", "sectionId", "Please Enter Section Id"));
//	    }
	    if (StringUtils.isBlank(req.getCoreAppCode())) {
	        errors.add(new Error("12", "coreAppCode", "Please Enter Core App Code"));
	    }
	    if (StringUtils.isBlank(req.getStatus())) {
	        errors.add(new Error("13", "status", "Please Enter Status"));
	    }
	    if (req.getEffectiveDateStart() == null) {
	        errors.add(new Error("14", "effectiveDateStart", "Please Enter Effective Start Date"));
	    }
	    if (req.getEffectiveDateEnd() == null) {
	        errors.add(new Error("15", "effectiveDateEnd", "Please Enter Effective End Date"));
	    }
	    if (req.getEffectiveDateStart() != null && req.getEffectiveDateEnd() != null) {
	        if (req.getEffectiveDateEnd().before(req.getEffectiveDateStart())) {
	            errors.add(new Error("16", "effectiveDateEnd", "Effective End Date cannot be before Start Date"));
	        }
	    }

	    if (StringUtils.isBlank(req.getRemarks())) {
	         errors.add(new Error("17", "remarks", "Please Enter Remarks"));
	    }
	    }catch (Exception e) {
			e.printStackTrace();
		}

	    return errors;
	}


}
