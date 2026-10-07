package com.maan.eway.common.service.impl;

import java.util.List;

import com.maan.eway.common.req.FirstLossPayeeReq;
import com.maan.eway.common.req.NonMotorSaveReq;
import com.maan.eway.common.req.WhatsappPremiumCalcReq;
import com.maan.eway.common.res.CommonRes;
import com.maan.eway.common.res.FirstLossPayeeRes;
import com.maan.eway.common.res.NonMotorComRes;
import com.maan.eway.common.res.NonMotorRes;
import com.maan.eway.common.res.NonMotorSaveRes;
import com.maan.eway.common.res.SlideSectionSaveRes;
import com.maan.eway.common.res.SuccessRes;

public interface EserviceSlideSaveServiceTanzania {

	
	CommonRes saveRiskDetailsWithPremiumCalc(WhatsappPremiumCalcReq req, String string);

	NonMotorRes getAllNonMotorDetails(NonMotorComRes Req);

	NonMotorSaveRes getNonMotorDetails(NonMotorComRes req);

	SuccessRes SaveFirstLossPayee(List<FirstLossPayeeReq> req);

	List<FirstLossPayeeRes> getFirstLossPayee(FirstLossPayeeReq req);

	List<SlideSectionSaveRes> nonMotorSaveDetails(NonMotorSaveReq req);
}
