package com.maan.eway.service;

import java.util.List;

import jakarta.transaction.Transactional;

import com.maan.eway.req.OneTimeTableReq;
import com.maan.eway.res.OneTimeTableRes;
@Transactional
public interface OneTimeService {

	List<OneTimeTableRes> call_OT_Insert(OneTimeTableReq req);

	

}
