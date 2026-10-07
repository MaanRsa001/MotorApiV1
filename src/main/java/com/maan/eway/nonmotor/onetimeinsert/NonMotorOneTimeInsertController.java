package com.maan.eway.nonmotor.onetimeinsert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.eway.common.res.CommonRes;
import com.maan.eway.nonmotor.onetimeinsert.request.NonMotorOneTimeRequest;

@RestController
@RequestMapping("/nonmotor")
public class NonMotorOneTimeInsertController {
	
	@Autowired
	private NonMotorOneTimeInsertService service;
	
	
	@PostMapping("/onetime/insert")
	public CommonRes onetimeInsert(@RequestBody NonMotorOneTimeRequest req) {
		return service.onetimeInsert(req);
	}
	
	

}
