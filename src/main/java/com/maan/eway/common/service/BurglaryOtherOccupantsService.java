package com.maan.eway.common.service;

import java.util.List;

import com.maan.eway.common.req.BurglaryOtherOccupantsGetReq;
import com.maan.eway.common.req.BurglaryOtherOccupantsSaveReq;
import com.maan.eway.common.res.BurglaryOtherOccupantsGetRes;
import com.maan.eway.error.Error;
import com.maan.eway.res.SuccessRes;

public interface BurglaryOtherOccupantsService {

	List<Error> validateBurglaryOtherOccupants(List<BurglaryOtherOccupantsSaveReq> req);

	SuccessRes saveBurglaryOtherOccupants(List<BurglaryOtherOccupantsSaveReq> req);

	BurglaryOtherOccupantsGetRes getBurglaryQuoteNo(BurglaryOtherOccupantsGetReq req);


}
