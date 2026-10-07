package com.maan.eway.common.service;

import java.util.List;

import com.maan.eway.common.req.NonMotorReqForDD;
import com.maan.eway.common.res.CodeDescRes;

public interface NonMotorSectionService  {

	List<CodeDescRes> getNonMotorSectionFields();

	List<CodeDescRes> getNonMotorRatingDD(NonMotorReqForDD req);

}
