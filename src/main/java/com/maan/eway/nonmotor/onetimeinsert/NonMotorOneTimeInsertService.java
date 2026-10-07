package com.maan.eway.nonmotor.onetimeinsert;

import com.maan.eway.common.res.CommonRes;
import com.maan.eway.nonmotor.onetimeinsert.request.NonMotorOneTimeRequest;

public interface NonMotorOneTimeInsertService {

	CommonRes onetimeInsert(NonMotorOneTimeRequest req);

}
