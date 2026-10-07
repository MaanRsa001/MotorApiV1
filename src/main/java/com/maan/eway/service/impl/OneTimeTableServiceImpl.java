package com.maan.eway.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.eway.error.Error;
import com.maan.eway.req.OneTimeTableReq;
import com.maan.eway.res.CommonRes;
import com.maan.eway.res.OneTimeTableRes;
import com.maan.eway.service.OneTimeService;
import com.maan.eway.service.OneTimeTableService;

@Service
public class OneTimeTableServiceImpl implements OneTimeTableService {

	private Logger log = LogManager.getLogger(OneTimeTableServiceImpl.class);
	
	@Autowired
	private OneTimeService otSer;
	
	@Override
	public CommonRes insertOneTimeTables(OneTimeTableReq req) {
		CommonRes response = new CommonRes();
		List<Error> errors = new ArrayList<Error>();
		try {
			
			List<OneTimeTableRes>	res = call_OT_Prem(req);

			// Response 
			response.setCommonResponse(res);
			response.setIsError(false);
			response.setErrorMessage(Collections.emptyList());
			response.setMessage("Success");

		} catch (Exception e) {
			log.error(e);
			errors.add(new Error("01" ,"Common Error" , e.getMessage()));
			response.setCommonResponse(null);
			response.setIsError(true);
			response.setErrorMessage(errors);
			response.setMessage("Failed");
		}
		return response;
	}
	
	private List<OneTimeTableRes> call_OT_Prem(OneTimeTableReq req) {
		List<OneTimeTableRes> res = new ArrayList<OneTimeTableRes>();
		try {

			res = otSer.call_OT_Insert(req);
			return res;
			
		} catch (Exception e) {
			log.error(e);
			return null ;
		}
	}

}
