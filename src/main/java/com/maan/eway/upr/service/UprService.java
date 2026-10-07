package com.maan.eway.upr.service;

import java.util.List;

import com.maan.eway.upr.dto.UprPolicyResponse;
import com.maan.eway.upr.dto.UprReq;

public interface UprService {

	List<UprPolicyResponse> saveUpr(UprReq req);

}
