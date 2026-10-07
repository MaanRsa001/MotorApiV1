package com.maan.eway.common.service;

import java.util.List;

import com.maan.eway.common.req.AddRiskInfoReq;
import com.maan.eway.common.req.AdditionalInformationFlatRequest;
import com.maan.eway.common.req.AviationInfoDto;
import com.maan.eway.common.req.CollateralDetailsReq;
import com.maan.eway.common.req.EngineeringReq;
import com.maan.eway.common.req.FireRiskInfoGetReq;
import com.maan.eway.common.req.FireRiskInfoSaveReq;
import com.maan.eway.common.req.FirstLossPayeeReq;
import com.maan.eway.common.req.GetAddInfoReq;
import com.maan.eway.common.req.MarineHullReq;
import com.maan.eway.common.req.NonMotorCommonRequest;
import com.maan.eway.common.req.NonMotorSaveReq;
import com.maan.eway.common.req.SectionSaveReq;
import com.maan.eway.common.req.WhatsappPremiumCalcReq;
import com.maan.eway.common.res.CommonRes;
import com.maan.eway.common.res.FireRiskInfoRes;
import com.maan.eway.common.res.FirstLossPayeeRes;
import com.maan.eway.common.res.NonMotorComRes;
import com.maan.eway.common.res.NonMotorRes;
import com.maan.eway.common.res.NonMotorSaveRes;
import com.maan.eway.common.res.SlideSectionSaveRes;
import com.maan.eway.common.res.SuccessRes;

public interface EserviceSlideSaveService {

	
//	CommonSlideSaveRes saveCommonDetails(SlideCommonSaveReq req);
//
//	List<SlideSectionSaveRes> saveEmpLiabilityDetails(List<SlideEmpLiabilitySaveReq> req);
//
//	List<SlideSectionSaveRes> saveSlideFidelityGuarantyDetails(List<SlideFidelityGuarantySaveReq> req);
//
//	List<SlideSectionSaveRes> saveSlideMachineryBreakdownDetails(SlideMachineryBreakdownSaveReq req);
//
//	List<SlideSectionSaveRes> saveSlideMoneyDetails(List<SlideMoneySaveReq> req);
//
//	List<SlideSectionSaveRes> saveSlidePlateGlassDetails(SlidePlateGlassSaveReq req);
//
//	List<SlideSectionSaveRes> saveSlidePublicLiablityDetails(SlidePublicLiabilitySaveReq req);
//
//	List<SlideSectionSaveRes> saveAccidentDamageDetails(AccidentDamageSaveRequest req);
//
//	List<SlideSectionSaveRes> saveAllRiskDetails(AllRiskDetailsReq req);
//
//	List<SlideSectionSaveRes> saveBurglaryAndHouseBreakingDetails(BurglaryAndHouseBreakingSaveReq req);
//
//	List<SlideSectionSaveRes> saveFireAndAlliedPerillsDetails(FireAndAlliedPerillsSaveReq req);
//
//	List<SlideSectionSaveRes> saveContentDetails(ContentSaveReq req);
//
//	List<SlideSectionSaveRes> saveElectronicEquipDetails(List<ElectronicEquipSaveReq> reqList);
//
//	SlideCommonSaveRes getCommonDetails(CommonGetReq req);
//
//	AccidentDamageSaveResponse getAccidentDamgeDetails(SlideSectionGetReq req);
//
//	AllRiskDetailsRes getAllRiskDetails(SlideSectionGetReq req);
//
//	List<BurglaryAndHouseBreakingSaveRes> getBurglaryAndHouseBreakingDetails(SlideSectionGetReq req);
//
//	FireAndAlliedPerillsSaveRes getFireAndAlliedPerils(SlideSectionGetReq req);
//
//	ContentSaveRes getContentDetails(SlideSectionGetReq req);
//
//	List<ElectronicEquipSaveRes> getElectronicEquipDetails(SlideSectionGetReq req);
//
//	List<SlideEmpLiabilitySaveRes> getEmpLiabilityDetails(SlideSectionGetReq req);
//
//	List<SlideFidelityGuarantySaveRes> getSlideFidelityGuarantyDetails(SlideSectionGetReq req);
//
//	SlideMachineryBreakdownSaveRes getSlideMachineryBreakdownDetails(SlideSectionGetReq req);
//
//	List<SlideMoneySaveRes> getSlideMoneyDetails(SlideSectionGetReq req);
//
//	SlidePlateGlassSaveRes getSlidePlateGlassDetails(SlideSectionGetReq req);
//
//	SlidePublicLiabilitySaveRes getSlidePublicLiablityDetails(SlideSectionGetReq req);
//	
//     SuccessRes saveadditionalinfoHI(SaveAddinfoHI req);
//
//	SuccessRes deleteAccidentDamgeDetails(SlideSectionGetReq req);
//
//	SuccessRes deleteAllRiskDetails(SlideSectionGetReq req);
//
//	SuccessRes deleteBurglaryAndHouseBreakingDetails(SlideSectionGetReq req);
//
//	SuccessRes deleteFireAndAlliedPerils(SlideSectionGetReq req);
//
//	SuccessRes deleteContentDetails(SlideSectionGetReq req);
//
//	SuccessRes deleteElectronicEquipDetails(SlideSectionGetReq req);
//
//	SuccessRes deleteEmpLiabilityDetails(SlideSectionGetReq req);
//
//	SuccessRes deleteSlideFidelityGuarantyDetails(SlideSectionGetReq req);
//
//	SuccessRes deleteSlideMoneyDetails(SlideSectionGetReq req);
//
//	SuccessRes deleteSlideMachineryBreakdownDetails(SlideSectionGetReq req);
//
//	SuccessRes deleteSlidePlateGlassDetails(SlideSectionGetReq req);
//
//	SuccessRes deleteSlidePublicLiablityDetails(SlideSectionGetReq req);
//
//	List<DropDownRes> getAooDropdown(MedMalDropDownReq req);
//
//	List<DropDownRes> getAggDropdown(MedMalDropDownReq req);
//
//	List<SlideSectionSaveRes> saveBuilding(SlideBuildingSaveReq req , List<EserviceBuildingDetails> NewDataList);
//
//	List<SlideBuildingGetRes> getSlideBuilding(SlideSectionGetReq req);
//
//	SuccessRes deleteSlideBuilding(SlideSectionGetReq req);
//
//	List<SlidePersonalAccidentGetRes> getSlidePersonalAccident(SlideSectionGetReq req);
//
//	SuccessRes deleteSlidePersonalAccident(SlideSectionGetReq req);
//
//	List<SlideSectionSaveRes> savePersonalAccident(List<SlidePersonalAccidentSaveReq> req);
//

//	CommonRes saveRiskDetailsWithPremiumCalc(WhatsappPremiumCalcReq req, String string);

	CommonRes saveRiskDetailsWithPremiumCalc(WhatsappPremiumCalcReq req, String string);

//	
//	List<SlideSectionSaveRes> saveBusinessInterruption(SlideBusinessInterruptionReq req);
//
//	 BusinessInterruptionRes getBusinessInterruption(SlideSectionGetReq req);
//	 
//	 List<SlideSectionSaveRes> saveGoodsInTransit(SlideGoodsInTransitSaveReq req);
//
//	 GoodInTransitRes getGoodsInTransit(SlideSectionGetReq req);
//
//	List<SlideSectionSaveRes> saveHealthInsureDetails(List<SlideHealthInsureSaveReq> req);
//
//	List<HealthInsureGetRes> getHealthInsure(SlideSectionGetReq req);
//
//	List<SlideSectionSaveRes> saveprofindernity(ProfessionalIndeminityReq req);
//
//	List<HealthInsureGetRes> getHumantype(ProductLevelReq req);
//
//	ResponseEntity<CommonResponse> saveAllSectionDetails(AllSectionSaveReq req, CommonResponse data,
//			List<Object> objects);
//
//	List<SlidePersonalAccidentSaveReq> fetchOriginalRequest(List<SlidePersonalAccidentSaveReq> req);
//
//	List<SlideEmpLiabilitySaveReq> fetchOriginalRequestData(List<SlideEmpLiabilitySaveReq> req);
//
//	SlideSectionSaveRes saveFire(FireReq req);
//	
//	boolean DeleteFire(CommonRequest req);
//
//    String generaterequestno(CommonRequest req);
//
//	boolean saveSectiondetails(FireReq req);
//
//	CommonRes deletefire(List<FireDelete> req);
//
//	List<SlideSectionSaveRes> saveBond(BondCommonReq req);
//
//	List<BondRes> getBoundDetails(SlideSectionGetReq req);
//
//	List<SlideSectionSaveRes> saveBurglaryAndHouseBreakingDetailsList(List<BurglaryAndHouseBreakingSaveReq> req);

	
	
	NonMotorRes getAllNonMotorDetails(NonMotorComRes Req);

	NonMotorSaveRes getNonMotorDetails(NonMotorComRes req);

	SuccessRes SaveFirstLossPayee(List<FirstLossPayeeReq> req);

	List<FirstLossPayeeRes> getFirstLossPayee(FirstLossPayeeReq req);

	List<SlideSectionSaveRes> nonMotorSaveDetails(NonMotorSaveReq req);
	
	List<SlideSectionSaveRes> MapBasedOnCompany(NonMotorSaveReq req);

	String saveEngineer(List<EngineeringReq> req);
	
	List<EngineeringReq> getEngineerInfo(String requestreferenceNO);

	List<SlideSectionSaveRes> nonMotorCommonSaveDetails(NonMotorCommonRequest req, String tokens);
	
	CommonRes updateSectionRecords(NonMotorSaveReq req,String token);

	CommonRes deletSectionRecords(SectionSaveReq req);

	CommonRes MapBasedOnCompany(NonMotorSaveReq req, String tokens);


   String saveCollateralDetailSave(List<CollateralDetailsReq> req);

List<CollateralDetailsReq> getCollateralDetails(String requestReferenceno);

String insertMarineHull(List<MarineHullReq> req);

List<MarineHullReq> getMarinHullInfo(String requestReferenceno);

String insertAviationInfoList(List<AviationInfoDto> req);

List<AviationInfoDto> getAviationInfoList(String requestRefNo);

NonMotorSaveRes setNonmotorsave(NonMotorComRes req);

CommonRes addRiskInfoEndt(AddRiskInfoReq request);

CommonRes getaddRiskInfoEndt(GetAddInfoReq request);

CommonRes addRiskInfoEndtAll(AdditionalInformationFlatRequest request);

String saveOrUpdateFireRiskInfo(List<FireRiskInfoSaveReq> reqList);

List<FireRiskInfoRes> getFireRiskInfoList(FireRiskInfoGetReq req);


}
