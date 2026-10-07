package com.maan.eway.common.service;

import java.util.List;

import com.maan.eway.common.req.SlideBusinessInterruptionReq;
import com.maan.eway.common.req.SlideCommonSaveReq;
import com.maan.eway.common.req.SlideEmpLiabilitySaveReq;
import com.maan.eway.common.req.SlideFidelityGuarantySaveReq;
import com.maan.eway.common.req.SlideFireAndPerillsSaveReq;
import com.maan.eway.common.req.SlideMachineryBreakdownSaveReq;
import com.maan.eway.common.req.SlideMoneySaveReq;
import com.maan.eway.common.req.SlidePlateGlassSaveReq;
import com.maan.eway.common.req.SlidePublicLiabilitySaveReq;
import com.maan.eway.common.res.CommonSlideSaveRes;
import com.maan.eway.common.res.EserviceBuildingSaveRes;
import com.maan.eway.common.res.SlideSectionSaveRes;
import com.maan.eway.error.Error;

public interface EserviceSlideValidateService {

	
	CommonSlideSaveRes saveCommonDetails(SlideCommonSaveReq req);

	List<SlideSectionSaveRes> saveEmpLiabilityDetails(SlideEmpLiabilitySaveReq req);

	List<SlideSectionSaveRes> saveSlideFidelityGuarantyDetails(SlideFidelityGuarantySaveReq req);

	List<SlideSectionSaveRes> saveSlideFireAndPerillsDetails(SlideFireAndPerillsSaveReq req);

	List<SlideSectionSaveRes> saveSlideMachineryBreakdownDetails(SlideMachineryBreakdownSaveReq req);

	List<SlideSectionSaveRes> saveSlideMoneyDetails(SlideMoneySaveReq req);

	List<SlideSectionSaveRes> saveSlidePlateGlassDetails(SlidePlateGlassSaveReq req);

	List<SlideSectionSaveRes> saveSlidePublicLiablityDetails(SlidePublicLiabilitySaveReq req);
	
	List<SlideSectionSaveRes> saveSlideBusinessinterruptionDetails(SlideBusinessInterruptionReq req);

}
