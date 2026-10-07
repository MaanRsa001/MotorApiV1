package com.maan.eway.update;

import java.util.List;

import com.maan.eway.common.req.EserviceMotorDetailsSaveRes;
import com.maan.eway.common.res.CommonRes;

public interface UpdateMotorService {

	CommonRes updateMotorDetails(UpdateMotorReq req);

	CommonRes getVehicleIds(UpdateMotorReq req);

	
}
