package com.maan.eway.common.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.eway.bean.FactorTypeDetails;
import com.maan.eway.common.req.NonMotorReqForDD;
import com.maan.eway.common.req.NonMotorSectionReq;
import com.maan.eway.common.res.CodeDescRes;
import com.maan.eway.common.service.NonMotorSectionService;
import com.maan.eway.repository.FactorTypeDetailsRepository;
@Service
public class NonMotorSectionServiceImpl implements NonMotorSectionService{
	
	@Autowired
	private FactorTypeDetailsRepository factRepo;

	 public List<CodeDescRes> getNonMotorSectionFields() {

	        List<CodeDescRes> response = new ArrayList<>();

	        Field[] fields = NonMotorSectionReq.class.getDeclaredFields();

	        for (Field field : fields) {

	            JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);

	            if (jsonProperty != null) {

	                CodeDescRes res = new CodeDescRes();
	                res.setCodeDesc(jsonProperty.value());
	                res.setCode(jsonProperty.value());
	                response.add(res);
	            }
	        }

	        return response;
	    }

	 @Override
	 public List<CodeDescRes> getNonMotorRatingDD(NonMotorReqForDD req) {
		 List<CodeDescRes> response = new ArrayList<>();
		 try {
	        
			 List<FactorTypeDetails> factlist = factRepo.findLatestByCompanyIdAndProductId(req.getCompanyId(),Integer.valueOf(req.getProductId()));
	        Field[] fields = NonMotorSectionReq.class.getDeclaredFields();

	        for (Field field : fields) {

	            JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);

	            if (jsonProperty != null) {
	            	String jsonValue = jsonProperty.value();
	                CodeDescRes res = new CodeDescRes();
	                res.setCodeDesc(jsonProperty.value());
	                res.setCode(jsonProperty.value());
	                boolean isFactorType = factlist.stream()
	                        .anyMatch(f ->
	                                jsonValue.equals(f.getJsonKey())
	                                || jsonValue.equals(f.getJsonKeyDropDown())
	                        );

	                if (isFactorType) {
	                    res.setFactorType("F");
	                }
	                
	                response.add(res);
	            }
	        }

	       
	    }catch(Exception e) {
	    	
	    }
		 return response;
	 }
}
