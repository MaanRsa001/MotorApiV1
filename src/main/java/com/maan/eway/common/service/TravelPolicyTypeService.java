package com.maan.eway.common.service;

import java.util.List;

import com.maan.eway.common.req.TravelPolicyTypeGetReq;
import com.maan.eway.common.res.TravelPolicyTypeRes;

public interface TravelPolicyTypeService {

	TravelPolicyTypeRes getTravelPolicyType(TravelPolicyTypeGetReq req);


}
