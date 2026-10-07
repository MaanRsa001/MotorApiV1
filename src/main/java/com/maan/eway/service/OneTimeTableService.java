package com.maan.eway.service;

import com.maan.eway.req.OneTimeTableReq;
import com.maan.eway.res.CommonRes;
import com.maan.eway.res.OneTimeTableRes;

public interface OneTimeTableService {

	CommonRes insertOneTimeTables(OneTimeTableReq req);

}
