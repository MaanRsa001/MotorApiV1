package com.maan.eway.common.service;

import java.util.List;

import com.maan.eway.common.req.AdditionalInfoGetReq;
import com.maan.eway.common.req.AdditionalInformationRequest;
import com.maan.eway.common.req.ContentAndRiskSaveReq;
import com.maan.eway.common.req.ContentRiskGetAllReq;
import com.maan.eway.common.req.ContentRiskGetReq;
import com.maan.eway.common.res.AdditionalInformationResponse;
import com.maan.eway.common.res.ContentRiskGetRes;
import com.maan.eway.common.res.ContentRiskGetallRes;
import com.maan.eway.res.SuccessRes;

public interface ContentAndRiskService {

	List<String> validatecontentrisk(ContentAndRiskSaveReq req);

	SuccessRes savecontentrisk(ContentAndRiskSaveReq req);

	ContentRiskGetRes getcontentrisk(ContentRiskGetReq req);

	ContentRiskGetallRes getallcontentrisk(ContentRiskGetAllReq req);

	public AdditionalInformationResponse saveOrUpdate(AdditionalInformationRequest req);
	
	public AdditionalInformationResponse getByQuoteAndRef(AdditionalInfoGetReq req);

	List<String> validateAdditionalInformation(AdditionalInformationRequest req);
}
