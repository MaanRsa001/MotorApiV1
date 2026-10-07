package com.maan.eway.common.service;

import java.util.List;

import com.maan.eway.common.req.BurglaryPolicyInfoGetReq;
import com.maan.eway.common.req.BurglaryPolicyInfoSaveReq;
import com.maan.eway.common.res.BurglaryPolicyInfoGetRes;
import com.maan.eway.error.Error;
import com.maan.eway.res.SuccessRes;

public interface BurglaryPolicyInfoService {

	List<Error> validateBurglaryPolicyInfo(BurglaryPolicyInfoSaveReq req);

	SuccessRes saveBurglaryPolicyInfo(BurglaryPolicyInfoSaveReq req);

	BurglaryPolicyInfoGetRes getBurglaryPolicyInfo(BurglaryPolicyInfoGetReq req);


}
