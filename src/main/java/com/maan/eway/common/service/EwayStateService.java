package com.maan.eway.common.service;

import com.maan.eway.common.req.EwayStateReq;
import com.maan.eway.common.res.CommonRes;

public interface EwayStateService {

	CommonRes getStateAndRegionList(EwayStateReq req);

	CommonRes getsuburbListfromCityId(EwayStateReq req);

	

	

}
