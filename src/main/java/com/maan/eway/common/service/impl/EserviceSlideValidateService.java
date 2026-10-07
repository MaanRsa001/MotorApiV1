package com.maan.eway.common.service.impl;

import java.util.List;

import com.maan.eway.common.req.AccidentDamageSaveRequest;
import com.maan.eway.common.req.AllRiskDetailsReq;
import com.maan.eway.common.req.BondCommonReq;
import com.maan.eway.common.req.BurglaryAndHouseBreakingSaveReq;
import com.maan.eway.common.req.CommonRequest;
import com.maan.eway.common.req.ContentSaveReq;
import com.maan.eway.common.req.ElectronicEquipSaveReq;
import com.maan.eway.common.req.EngineeringReq;
import com.maan.eway.common.req.FireAndAlliedPerillsSaveReq;
import com.maan.eway.common.req.FireReq;
import com.maan.eway.common.req.FirstLossPayeeReq;
import com.maan.eway.common.req.MainInfoValidationReq;
import com.maan.eway.common.req.NonMotorSaveReq;
import com.maan.eway.common.req.SaveAddinfoHI;
import com.maan.eway.common.req.SlideBuildingSaveReq;
import com.maan.eway.common.req.SlideBusinessInterruptionReq;
import com.maan.eway.common.req.SlideCommonSaveReq;
import com.maan.eway.common.req.SlideEmpLiabilitySaveReq;
import com.maan.eway.common.req.SlideFidelityGuarantySaveReq;
import com.maan.eway.common.req.SlideFireAndPerillsSaveReq;
import com.maan.eway.common.req.SlideGoodsInTransitSaveReq;
import com.maan.eway.common.req.SlideHIFamilyDetailsReq;
import com.maan.eway.common.req.SlideHealthInsureSaveReq;
import com.maan.eway.common.req.SlideMachineryBreakdownSaveReq;
import com.maan.eway.common.req.SlideMoneySaveReq;
import com.maan.eway.common.req.SlidePersonalAccidentSaveReq;
import com.maan.eway.common.req.SlidePlateGlassSaveReq;
import com.maan.eway.common.req.SlidePublicLiabilitySaveReq;
import com.maan.eway.error.Error;

public interface EserviceSlideValidateService {

	List<String> validateCommonDetails(SlideCommonSaveReq req);

	List<String> validateEmpLiabilityDetails(List<SlideEmpLiabilitySaveReq> req);

	List<String> validateSlideFidelityGuarantyDetails(List<SlideFidelityGuarantySaveReq> req);

	List<Error> validateSlideFireAndPerillsDetails(SlideFireAndPerillsSaveReq req);

	List<String> validateSlideMachineryBreakdownDetails(SlideMachineryBreakdownSaveReq req);

	List<String> validateSlideMoneyDetails(List<SlideMoneySaveReq> req);

	List<String> validateSlidePlateGlassDetails(SlidePlateGlassSaveReq req);

	List<String> validateSlidePublicLiablityDetails(SlidePublicLiabilitySaveReq req);
	
	List<String> validateAccidentDamageDetails(AccidentDamageSaveRequest req) ;
	
	List<String> validateAllRiskDetails(AllRiskDetailsReq req);
	 
    List<String> validateBurglaryAndHouseBreakingDetails(BurglaryAndHouseBreakingSaveReq req) ;

	List<String> validateFireAndAlliedPerillsDetails(FireAndAlliedPerillsSaveReq req);

	List<String> validateContentDetails(ContentSaveReq req , Double si);

	List<String> validateElectronicEquipDetails(List<ElectronicEquipSaveReq> reqList);

	List<String> mainInfoValidation(MainInfoValidationReq req);
	List<String> validatePersonalAccident(List<SlidePersonalAccidentSaveReq> req);

	List<String> validateBuilding(SlideBuildingSaveReq req );
	
	
	List<String> validateSlideBusinessInterruption(SlideBusinessInterruptionReq req);
	
	List<String> validateSlideGoodsInTransit(SlideGoodsInTransitSaveReq req);

	List<String> validateHealthInsurDetails(List<SlideHealthInsureSaveReq> req);

	List<String> vaildateInfoHealthDetails(List<SlideHIFamilyDetailsReq> req);


	List<String> validatecommondetails(CommonRequest req);
	
	
	List<Error> validateFireAndAlliedPerills(CommonRequest req);

	List<String> validateBond(BondCommonReq req);

	List<String> validateBurglaryAndHouseBreakingDetailsList(List<BurglaryAndHouseBreakingSaveReq> req);

	List<String> CompanyBasedValidation(NonMotorSaveReq req);

	List<String> validatenonfirstLossPayee(List<FirstLossPayeeReq> req);

	List<String> Engineervalidation(List<EngineeringReq> req);


}
