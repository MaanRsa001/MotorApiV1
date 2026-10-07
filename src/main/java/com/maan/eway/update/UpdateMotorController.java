package com.maan.eway.update;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.eway.common.req.EserviceMotorDetailsSaveRes;
import com.maan.eway.common.res.CommonRes;

@RestController
@RequestMapping("/updatetira")
public class UpdateMotorController {

	@Autowired
	private UpdateMotorService service;

	@PostMapping("/updatemotordetails")
	public ResponseEntity<CommonRes> updateMotorDetails(@RequestBody UpdateMotorReq req) {

		CommonRes response = service.updateMotorDetails(req);
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/getvehicleids")
    public ResponseEntity<CommonRes> getVehicleIds(@RequestBody UpdateMotorReq req) {
        CommonRes response = service.getVehicleIds(req);
        return ResponseEntity.ok(response);
    }
}
