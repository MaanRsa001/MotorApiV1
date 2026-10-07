package com.maan.eway.common.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.maan.eway.bean.EserviceCustomerDetails;
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
import com.maan.eway.common.req.updateDateReq;
import com.maan.eway.common.res.CommonRes;
import com.maan.eway.common.res.FireRiskInfoRes;
import com.maan.eway.common.res.FirstLossPayeeRes;
import com.maan.eway.common.res.NonMotorComRes;
import com.maan.eway.common.res.NonMotorRes;
import com.maan.eway.common.res.NonMotorSaveRes;
import com.maan.eway.common.res.PremiaSpCodeReq;
import com.maan.eway.common.res.PremiaTiraReq;
import com.maan.eway.common.res.PremiaTiraRes;
import com.maan.eway.common.res.SlideSectionSaveRes;
import com.maan.eway.common.res.SuccessRes;
import com.maan.eway.common.service.EserviceSlideSaveService;
import com.maan.eway.common.service.UpdateDateService;
import com.maan.eway.common.service.impl.EserviceSlideValidateService;
import com.maan.eway.common.service.impl.FetchErrorDescServiceImpl;
import com.maan.eway.common.service.impl.GenerateSeqNoServiceImpl;
import com.maan.eway.common.service.impl.PremiaBrokerServiceImpl;
import com.maan.eway.error.Error;
import com.maan.eway.repository.EserviceCustomerDetailsRepository;
import com.maan.eway.res.CommonErrorModuleReq;
import com.maan.eway.res.DropDownRes;
import com.maan.eway.service.PrintReqService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
@Api(tags = "ESERVICE SLIDE DETAILS", description = "API's")
public class EserviceSlideController {

	@Autowired
	private  EserviceSlideSaveService entityService;
	
	@Autowired
	private  EserviceSlideValidateService validateService;

	@Autowired
	private  PremiaBrokerServiceImpl tiraService;

	@Autowired
	private PrintReqService reqPrinter;
	
	@Autowired
	private FetchErrorDescServiceImpl errorDescService ;
	
	
	@Autowired
	private EserviceCustomerDetailsRepository custRepo;
	
	
	

	@Autowired
	private UpdateDateService update;
	
	
	

	@Autowired
	private GenerateSeqNoServiceImpl genSeqNoService;
	
	// ------------------------------------------- Insert Apis ------------------------------------------------------//
	
	/*@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping(value = "/slide/savecommondetails")
	@ApiOperation(value = "This method is Slide Common Details")
	public ResponseEntity<CommonRes> saveCommonDetails(@RequestBody  SlideCommonSaveReq req) {
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		List<String> validationCodes = validateService.validateCommonDetails(req);
		
		List<Error> validation = null;
		if(validationCodes!=null && validationCodes.size() > 0 ) {
			CommonErrorModuleReq comErrDescReq = new CommonErrorModuleReq();
			comErrDescReq.setBranchCode(req.getBranchCode());
			comErrDescReq.setInsuranceId(req.getCompanyId());
			comErrDescReq.setProductId("99999");
			comErrDescReq.setModuleId("12");
			comErrDescReq.setModuleName("COMMON RISK");
			
			validation = errorDescService.getErrorDesc(validationCodes ,comErrDescReq);
		}
		//// validation
		if (validation != null && validation.size() != 0) {
			data.setCommonResponse(null);
			data.setIsError(true);
			data.setErrorMessage(validation);
			data.setMessage("Failed");
			return new ResponseEntity<CommonRes>(data, HttpStatus.OK);

		}
	  else {
			/////// save
			CommonSlideSaveRes res = entityService.saveCommonDetails(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
		}
    }
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping(value = "/slide1/saveaccidentdamage")
	@ApiOperation(value = "This method is Insert Accident Damage Details")
	public ResponseEntity<CommonRes> saveAccidentDamgeDetails(@RequestBody   AccidentDamageSaveRequest req) {
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		List<String> validationCodes = validateService.validateAccidentDamageDetails(req);
		
	    List<Error> validation = null;
		
		if(validationCodes!=null && validationCodes.size() > 0 ) {
			CommonErrorModuleReq comErrDescReq = new CommonErrorModuleReq();
			//comErrDescReq.setBranchCode(req.getBranchCode());
			comErrDescReq.setInsuranceId(req.getInsuranceId());
			comErrDescReq.setProductId("99999");
			comErrDescReq.setModuleId("13");
			comErrDescReq.setModuleName("ACCIDENT RISK");
			
			validation = errorDescService.getErrorDesc(validationCodes ,comErrDescReq);
		}
		//// validation
		if (validation != null && validation.size() != 0) {
			data.setCommonResponse(null);
			data.setIsError(true);
			data.setErrorMessage(validation);
			data.setMessage("Failed");
			return new ResponseEntity<CommonRes>(data, HttpStatus.OK);

		}
		else {
			/////// save
			List<SlideSectionSaveRes> res = entityService.saveAccidentDamageDetails(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
		}
    }
	
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping(value = "/slide2/saveallriskdetails")
	@ApiOperation(value = "This method is Insert All Risk Details")
	public ResponseEntity<CommonRes> saveAllRiskDetails(@RequestBody   AllRiskDetailsReq req) {
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		List<String> validationCodes = validateService.validateAllRiskDetails(req);
		//// validation
          List<Error> validation = null;
		
		if(validationCodes!=null && validationCodes.size() > 0 ) {
			CommonErrorModuleReq comErrDescReq = new CommonErrorModuleReq();
			//comErrDescReq.setBranchCode(req.getBranchCode());
			comErrDescReq.setInsuranceId(req.getInsuranceId());
			comErrDescReq.setProductId("99999");
			comErrDescReq.setModuleId("14");
			comErrDescReq.setModuleName("ALL RISK");
			
			validation = errorDescService.getErrorDesc(validationCodes ,comErrDescReq);
		}
		//// validation
		if (validation != null && validation.size() != 0) {
			data.setCommonResponse(null);
			data.setIsError(true);
			data.setErrorMessage(validation);
			data.setMessage("Failed");
			return new ResponseEntity<CommonRes>(data, HttpStatus.OK);

		}
        else {
			/////// save
			List<SlideSectionSaveRes> res = entityService.saveAllRiskDetails(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
		}
    }
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping(value = "/slide6/saveelectronicequip")
	@ApiOperation(value = "This method is Slide Save Electronic Equip Details")
	public ResponseEntity<CommonRes> saveElectronicEquipDetails(@RequestBody  List<ElectronicEquipSaveReq> reqList) {
		reqPrinter.reqPrint(reqList);
		CommonRes data = new CommonRes();
		List<String> validationCodes = validateService.validateElectronicEquipDetails(reqList);
		
		   List<Error> validation = null;
			
			if(validationCodes!=null && validationCodes.size() > 0 ) {
				CommonErrorModuleReq comErrDescReq = new CommonErrorModuleReq();
				//comErrDescReq.setBranchCode(req.getBranchCode());
				comErrDescReq.setInsuranceId(reqList.get(0).getInsuranceId());
				comErrDescReq.setProductId("99999");
				comErrDescReq.setModuleId("18");
				comErrDescReq.setModuleName("ELEC EUIP RISK");
				
				validation = errorDescService.getErrorDesc(validationCodes ,comErrDescReq);
			}
			
		//// validation
		if (validation != null && validation.size() != 0) {
			data.setCommonResponse(null);
			data.setIsError(true);
			data.setErrorMessage(validation);
			data.setMessage("Failed");
			return new ResponseEntity<CommonRes>(data, HttpStatus.OK);

		} else {
			/////// save
			List<SlideSectionSaveRes> res = entityService.saveElectronicEquipDetails(reqList);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
		}
    }
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping(value = "/slide3/saveburglaryandhouse")
	@ApiOperation(value = "This method is Insert Burglary  and House breaking Details")
	public ResponseEntity<CommonRes> saveBurglaryAndHouseBreakingDetails(@RequestBody   BurglaryAndHouseBreakingSaveReq req) {
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		List<String> validationCodes = validateService.validateBurglaryAndHouseBreakingDetails(req);
		
		 List<Error> validation = null;
			
			if(validationCodes!=null && validationCodes.size() > 0 ) {
				CommonErrorModuleReq comErrDescReq = new CommonErrorModuleReq();
				//comErrDescReq.setBranchCode(req.getBranchCode());
				comErrDescReq.setInsuranceId(req.getInsuranceId());
				comErrDescReq.setProductId("99999");
				comErrDescReq.setModuleId("15");
				comErrDescReq.setModuleName("BURGLARY RISK");
				
				validation = errorDescService.getErrorDesc(validationCodes ,comErrDescReq);
			}
			//// validation
			if (validation != null && validation.size() != 0) {
				data.setCommonResponse(null);
				data.setIsError(true);
				data.setErrorMessage(validation);
				data.setMessage("Failed");
				return new ResponseEntity<CommonRes>(data, HttpStatus.OK);

			}
            else {
			/////// save
			List<SlideSectionSaveRes> res = entityService.saveBurglaryAndHouseBreakingDetails(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
		}
    }

	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping(value = "/slide3/saveburglaryandhouselist")
	@ApiOperation(value = "This method is Insert Burglary  and House breaking Details")
	public ResponseEntity<CommonRes> saveBurglaryAndHouseBreakingDetailsList(@RequestBody   List<BurglaryAndHouseBreakingSaveReq> req) {
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		List<String> validationCodes = validateService.validateBurglaryAndHouseBreakingDetailsList(req);
		
		 List<Error> validation = null;
			
			if(validationCodes!=null && validationCodes.size() > 0 ) {
				CommonErrorModuleReq comErrDescReq = new CommonErrorModuleReq();
				//comErrDescReq.setBranchCode(req.getBranchCode());
				comErrDescReq.setInsuranceId(req.get(0).getInsuranceId());
				comErrDescReq.setProductId("99999");
				comErrDescReq.setModuleId("15");
				comErrDescReq.setModuleName("BURGLARY RISK");
				
				validation = errorDescService.getErrorDesc(validationCodes ,comErrDescReq);
			}
			//// validation
			if (validation != null && validation.size() != 0) {
				data.setCommonResponse(null);
				data.setIsError(true);
				data.setErrorMessage(validation);
				data.setMessage("Failed");
				return new ResponseEntity<CommonRes>(data, HttpStatus.OK);

			}
            else {
			/////// save
			List<SlideSectionSaveRes> res = entityService.saveBurglaryAndHouseBreakingDetailsList(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
		}
    }

	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping(value = "/slide4/savefireandperils")
	@ApiOperation(value = "This method is Insert Burglary  and House breaking Details")
	public ResponseEntity<CommonRes> saveFireAndAlliedPerils(@RequestBody   FireAndAlliedPerillsSaveReq req) {
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		List<String> validationCodes = validateService.validateFireAndAlliedPerillsDetails(req);
		
		List<Error> validation = null;
		
		if(validationCodes!=null && validationCodes.size() > 0 ) {
			CommonErrorModuleReq comErrDescReq = new CommonErrorModuleReq();
			//comErrDescReq.setBranchCode(req.getBranchCode());
			comErrDescReq.setInsuranceId(req.getInsuranceId());
			comErrDescReq.setProductId("99999");
			comErrDescReq.setModuleId("16");
			comErrDescReq.setModuleName("FIRE AND PERIL RISK");
			
			validation = errorDescService.getErrorDesc(validationCodes ,comErrDescReq);
		}
		//// validation
		if (validation != null && validation.size() != 0) {
			data.setCommonResponse(null);
			data.setIsError(true);
			data.setErrorMessage(validation);
			data.setMessage("Failed");
			return new ResponseEntity<CommonRes>(data, HttpStatus.OK);

		}

		
		//// validation
		if (validation != null && validation.size() != 0) {
			data.setCommonResponse(null);
			data.setIsError(true);
			data.setErrorMessage(validation);
			data.setMessage("Failed");
			return new ResponseEntity<CommonRes>(data, HttpStatus.OK);

		} else {
			/////// save
			List<SlideSectionSaveRes> res = entityService.saveFireAndAlliedPerillsDetails(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
		}
    }
	
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping(value = "/slide5/savecontent")
	@ApiOperation(value = "This method is Slide Save Content Details")
	public ResponseEntity<CommonRes> saveContentDetails(@RequestBody  ContentSaveReq req, @RequestParam(value = "Si"  , required =  false )
	       Double si) {
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		List<String> validationCodes = validateService.validateContentDetails(req , si);
		
		
          List<Error> validation = null;
		
		if(validationCodes!=null && validationCodes.size() > 0 ) {
			CommonErrorModuleReq comErrDescReq = new CommonErrorModuleReq();
			//comErrDescReq.setBranchCode(req.getBranchCode());
			comErrDescReq.setInsuranceId(req.getInsuranceId());
			comErrDescReq.setProductId("99999");
			comErrDescReq.setModuleId("17");
			comErrDescReq.setModuleName("CONTENT RISK");
			
			validation = errorDescService.getErrorDesc(validationCodes ,comErrDescReq);
		}
		
		
		//// validation
		if (validation != null && validation.size() != 0) {
			data.setCommonResponse(null);
			data.setIsError(true);
			data.setErrorMessage(validation);
			data.setMessage("Failed");
			return new ResponseEntity<CommonRes>(data, HttpStatus.OK);

		} else {
			/////// save
			List<SlideSectionSaveRes> res = entityService.saveContentDetails(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
		}
    }
	

	
	
	
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping(value = "/slide7/saveempliablity")
	@ApiOperation(value = "This method is Slide Emp Liability Details")
	public ResponseEntity<CommonRes> saveEmpLiabilityDetails(@RequestBody  List<SlideEmpLiabilitySaveReq> req) {
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		
		 if(!req.get(0).getProductId().equals("14")&& !req.get(0).getProductId().equals("15")&&req.get(0).getInsuranceId().equals("100002"))
				 {
				 req = entityService.fetchOriginalRequestData(req);
				 }
		
		List<String> validationCodes = validateService.validateEmpLiabilityDetails(req);
		
		  List<Error> validation = null;
			
			
			  if(validationCodes!=null && validationCodes.size() > 0 ) {
			 CommonErrorModuleReq comErrDescReq = new CommonErrorModuleReq();
			 //comErrDescReq.setBranchCode(req.getBranchCode());
			  comErrDescReq.setInsuranceId(req.get(0).getInsuranceId());
			  comErrDescReq.setProductId("99999"); comErrDescReq.setModuleId("19");
			  comErrDescReq.setModuleName("EMPLOYEE LIABILITY RISK");
			  
			  validation = errorDescService.getErrorDesc(validationCodes ,comErrDescReq); }
			 
			
		
		
		
		 if (validation != null && validation.size() != 0) {
		  data.setCommonResponse(null); data.setIsError(true);
		  data.setErrorMessage(validation); data.setMessage("Failed"); return new
		  ResponseEntity<CommonRes>(data, HttpStatus.OK);
		  
		  }
		  
		
		 else {
		 
		  List<SlideSectionSaveRes> res = entityService.saveEmpLiabilityDetails(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
		 }
		
    }
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping(value = "/slide7/saveprofindernity")
	@ApiOperation(value = "This method is Slide Emp Liability Details")
	public ResponseEntity<CommonRes> saveprofindernity(@RequestBody ProfessionalIndeminityReq req) {
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		
		//List<String> validationCodes = validateService.validateEmpLiabilityDetails(req);
		
		//  List<Error> validation = null;
		/*
		 * if(validationCodes!=null && validationCodes.size() > 0 ) {
		 * CommonErrorModuleReq comErrDescReq = new CommonErrorModuleReq();
		 * //comErrDescReq.setBranchCode(req.getBranchCode());
		 * comErrDescReq.setInsuranceId(req.get(0).getInsuranceId());
		 * comErrDescReq.setProductId("99999"); comErrDescReq.setModuleId("19");
		 * comErrDescReq.setModuleName("EMPLOYEE LIABILITY RISK");
		 * 
		 * validation = errorDescService.getErrorDesc(validationCodes ,comErrDescReq); }
		 */	
		
		//// validation
		/*
		 * if (validation != null && validation.size() != 0) {
		 * data.setCommonResponse(null); data.setIsError(true);
		 * data.setErrorMessage(validation); data.setMessage("Failed"); return new
		 * ResponseEntity<CommonRes>(data, HttpStatus.OK);
		 * 
		 * }
		 * add else under
//		 */
	/*		/////// save
			//List<SlideSectionSaveRes> res = entityService.saveEmpLiabilityDetails(req);
			List<SlideSectionSaveRes> res = entityService.saveprofindernity(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
		
    }*/
	/*
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping(value = "/slide8/savefidelityemp")
	@ApiOperation(value = "This method is Save Slide Fidelity Emp Details")
	public ResponseEntity<CommonRes> saveSlideFidelityGuarantyDetails(@RequestBody  List<SlideFidelityGuarantySaveReq> req) {
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		List<String> validationCodes = validateService.validateSlideFidelityGuarantyDetails(req);
		

		  List<Error> validation = null;
			
		if(validationCodes!=null && validationCodes.size() > 0 ) {
				CommonErrorModuleReq comErrDescReq = new CommonErrorModuleReq();
				//comErrDescReq.setBranchCode(req.getBranchCode());
				//comErrDescReq.setInsuranceId(req.getInsuranceId());
				comErrDescReq.setProductId("99999");
				comErrDescReq.setModuleId("20");
				comErrDescReq.setModuleName("FIDELITY EMP RISK");
				
				validation = errorDescService.getErrorDesc(validationCodes ,comErrDescReq);
		}
			
		//// validation
		if (validation != null && validation.size() != 0) {
			data.setCommonResponse(null);
			data.setIsError(true);
			data.setErrorMessage(validation);
			data.setMessage("Failed");
			return new ResponseEntity<CommonRes>(data, HttpStatus.OK);

		} else {
			/////// save
			List<SlideSectionSaveRes> res = entityService.saveSlideFidelityGuarantyDetails(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
		}
    }
	

	
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping(value = "/slide9/savemachinerybreakdown")
	@ApiOperation(value = "This method is Save Slide Machinery Breakdown Details")
	public ResponseEntity<CommonRes> saveSlideMachineryBreakdownDetails(@RequestBody  SlideMachineryBreakdownSaveReq req) {
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		List<String> validationCodes = validateService.validateSlideMachineryBreakdownDetails(req);
		

		  List<Error> validation = null;
			
		if(validationCodes!=null && validationCodes.size() > 0 ) {
				CommonErrorModuleReq comErrDescReq = new CommonErrorModuleReq();
				//comErrDescReq.setBranchCode(req.getBranchCode());
				comErrDescReq.setInsuranceId(req.getInsuranceId());
				comErrDescReq.setProductId("99999");
				comErrDescReq.setModuleId("21");
				comErrDescReq.setModuleName("MACHINERY BREAKDOWN RISK");
				
				validation = errorDescService.getErrorDesc(validationCodes ,comErrDescReq);
		}
		
		//// validation
		if (validation != null && validation.size() != 0) {
			data.setCommonResponse(null);
			data.setIsError(true);
			data.setErrorMessage(validation);
			data.setMessage("Failed");
			return new ResponseEntity<CommonRes>(data, HttpStatus.OK);

		} else {
			/////// save
			List<SlideSectionSaveRes> res = entityService.saveSlideMachineryBreakdownDetails(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
		}
    }
	
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping(value = "/slide10/savemoneydetails")
	@ApiOperation(value = "This method is Save Money Details")
	public ResponseEntity<CommonRes> saveSlideMoneyDetails(@RequestBody  List<SlideMoneySaveReq> req) {
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		List<String> validationCodes = validateService.validateSlideMoneyDetails(req);
		
		  List<Error> validation = null;
			
		if(validationCodes!=null && validationCodes.size() > 0 ) {	
			CommonErrorModuleReq comErrDescReq = new CommonErrorModuleReq();
			//comErrDescReq.setBranchCode(req.getBranchCode());
			comErrDescReq.setInsuranceId(req.get(0).getInsuranceId());
			comErrDescReq.setProductId("99999");
			comErrDescReq.setModuleId("23");
			comErrDescReq.setModuleName("MONEY SAVE RISK");
					
			validation = errorDescService.getErrorDesc(validationCodes ,comErrDescReq);
	  }
	
		//// validation
		if (validation != null && validation.size() != 0) {
			data.setCommonResponse(null);
			data.setIsError(true);
			data.setErrorMessage(validation);
			data.setMessage("Failed");
			return new ResponseEntity<CommonRes>(data, HttpStatus.OK);

		} else {
			/////// save
			List<SlideSectionSaveRes> res = entityService.saveSlideMoneyDetails(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
		}
    }
	
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping(value = "/slide11/saveplateglass")
	@ApiOperation(value = "This method is Save Plate Glass Details")
	public ResponseEntity<CommonRes> saveSlidePlateGlassDetails(@RequestBody  SlidePlateGlassSaveReq req) {
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		List<String> validationCodes = validateService.validateSlidePlateGlassDetails(req);
		
		 List<Error> validation = null;
			
		if(validationCodes!=null && validationCodes.size() > 0 ) {
				
			CommonErrorModuleReq comErrDescReq = new CommonErrorModuleReq();
			//comErrDescReq.setBranchCode(req.getBranchCode());
			comErrDescReq.setInsuranceId(req.getInsuranceId());
		    comErrDescReq.setProductId("99999");
			comErrDescReq.setModuleId("24");
			comErrDescReq.setModuleName("PLATE GLASS RISK");
					
			validation = errorDescService.getErrorDesc(validationCodes ,comErrDescReq);
		}
		//// validation
		if (validation != null && validation.size() != 0) {
			data.setCommonResponse(null);
			data.setIsError(true);
			data.setErrorMessage(validation);
			data.setMessage("Failed");
			return new ResponseEntity<CommonRes>(data, HttpStatus.OK);

		} else {
			/////// save
			List<SlideSectionSaveRes> res = entityService.saveSlidePlateGlassDetails(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
		}
    }
	
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping(value = "/slide12/savepublicliability")
	@ApiOperation(value = "This method is Save Public Liability Details")
	public ResponseEntity<CommonRes> saveSlidePublicLiablityDetails(@RequestBody  SlidePublicLiabilitySaveReq req) {
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		List<String> validationCodes = validateService.validateSlidePublicLiablityDetails(req);
		
		List<Error> validation = null;
		
		if(validationCodes!=null && validationCodes.size() > 0 ) {
				
			CommonErrorModuleReq comErrDescReq = new CommonErrorModuleReq();
			//comErrDescReq.setBranchCode(req.getBranchCode());
			comErrDescReq.setInsuranceId(req.getInsuranceId());
		    comErrDescReq.setProductId("99999");
			comErrDescReq.setModuleId("25");
			comErrDescReq.setModuleName("PUBLIC LIABILITY RISK");
					
			validation = errorDescService.getErrorDesc(validationCodes ,comErrDescReq);
		}
		
		
		//// validation
		if (validation != null && validation.size() != 0) {
			data.setCommonResponse(null);
			data.setIsError(true);
			data.setErrorMessage(validation);
			data.setMessage("Failed");
			return new ResponseEntity<CommonRes>(data, HttpStatus.OK);

		} else {
			/////// save
			List<SlideSectionSaveRes> res = entityService.saveSlidePublicLiablityDetails(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
		}
    }
	
	
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping(value = "/slide13/savebusinessinterruption")
	@ApiOperation(value = "This method is Save Public Liability Details")
	public ResponseEntity<CommonRes> saveSlideBusinessInterruptionDetails(@RequestBody  SlideBusinessInterruptionReq req) {
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		List<String> validationCodes = validateService.validateSlideBusinessInterruption(req);
		

		List<Error> validation = null;
		
		if(validationCodes!=null && validationCodes.size() > 0 ) {
				
			CommonErrorModuleReq comErrDescReq = new CommonErrorModuleReq();
			//comErrDescReq.setBranchCode(req.getBranchCode());
			comErrDescReq.setInsuranceId(req.getInsuranceId());
		    comErrDescReq.setProductId("99999");
			comErrDescReq.setModuleId("26");
			comErrDescReq.setModuleName("BUSINESS INTERRUPTION RISK");
					
			validation = errorDescService.getErrorDesc(validationCodes ,comErrDescReq);
		}
		
		//// validation
		if (validation != null && validation.size() != 0) {
			data.setCommonResponse(null);
			data.setIsError(true);
			data.setErrorMessage(validation);
			data.setMessage("Failed");
			return new ResponseEntity<CommonRes>(data, HttpStatus.OK);

		} else {
			/////// save
			List<SlideSectionSaveRes> res = entityService.saveBusinessInterruption(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
		}
    }
	
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping(value = "/slide14/savegoodsintransit")
	@ApiOperation(value = "This method is Save Public Liability Details")
	public ResponseEntity<CommonRes> saveSlideGoodsInTransit(@RequestBody  SlideGoodsInTransitSaveReq req) {
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		List<String> validationCodes = validateService.validateSlideGoodsInTransit(req);
		
       List<Error> validation = null;
		
		if(validationCodes!=null && validationCodes.size() > 0 ) {
				
			CommonErrorModuleReq comErrDescReq = new CommonErrorModuleReq();
			//comErrDescReq.setBranchCode(req.getBranchCode());
			comErrDescReq.setInsuranceId(req.getInsuranceId());
		    comErrDescReq.setProductId("99999");
			comErrDescReq.setModuleId("27");
			comErrDescReq.setModuleName("GOODS IN TRANSIT RISK");
					
			validation = errorDescService.getErrorDesc(validationCodes ,comErrDescReq);
		}
		
		//// validation
		if (validation != null && validation.size() != 0) {
			data.setCommonResponse(null);
			data.setIsError(true);
			data.setErrorMessage(validation);
			data.setMessage("Failed");
			return new ResponseEntity<CommonRes>(data, HttpStatus.OK);

		} else {
			/////// save
			List<SlideSectionSaveRes> res = entityService.saveGoodsInTransit(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
		}
    }
  
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping(value = "/slide15/savehealthinsure")
	@ApiOperation(value = "This method is Slide Emp Liability Details")
	public ResponseEntity<CommonRes> saveHealthInsureDetails(@RequestBody  List<SlideHealthInsureSaveReq> req) {
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		
		List<String> validationCodes = validateService.validateHealthInsurDetails(req);
		
		  List<Error> validation = null;
			
		if(validationCodes!=null && validationCodes.size() > 0 ) {
				CommonErrorModuleReq comErrDescReq = new CommonErrorModuleReq();
				//comErrDescReq.setBranchCode(req.getBranchCode()); 
				comErrDescReq.setInsuranceId(req.get(0).getInsuranceId());
				comErrDescReq.setProductId("99999");
				comErrDescReq.setModuleId("34");
				comErrDescReq.setModuleName("HOME INSURANCE");
				
				validation = errorDescService.getErrorDesc(validationCodes ,comErrDescReq);
		}
			
		
		//// validation
		if (validation != null && validation.size() != 0) {
			data.setCommonResponse(null);
			data.setIsError(true);
			data.setErrorMessage(validation);
			data.setMessage("Failed");
			return new ResponseEntity<CommonRes>(data, HttpStatus.OK);

		} else {
			/////// save
			List<SlideSectionSaveRes> res = entityService.saveHealthInsureDetails(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
		}
    }
	
	
	//--------------------------------------------Additional Info---------------------------------------------------
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping("/slide15/infohealthinsurance")
	@ApiOperation(value="This method is to Save Product Employees Details")
	public ResponseEntity<CommonRes>  additionalinfoHI(@RequestBody SaveAddinfoHI req){
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		List<SlideHIFamilyDetailsReq> reqList = new ArrayList<SlideHIFamilyDetailsReq>();
		
			reqList=req.getFamilyDetails();
			
		List<String> validationCodes = validateService.vaildateInfoHealthDetails(reqList);
		//Validation
		List<Error> validation =null;
		if(validationCodes!=null && validationCodes.size() > 0 ) {
		CommonErrorModuleReq comErrDescReq = new CommonErrorModuleReq();
	//	comErrDescReq.setBranchCode("99999");
		comErrDescReq.setInsuranceId(req.getInsuranceId());
		comErrDescReq.setProductId("99999");
		comErrDescReq.setModuleId("34");
		comErrDescReq.setModuleName("HOME INSURANCE");

		
		
		validation = errorDescService.getErrorDesc(validationCodes ,comErrDescReq);
		}
		if(validation!=null && validation.size()!=0) {
			data.setCommonResponse(null);
			data.setIsError(true);
			data.setErrorMessage(validation);
			data.setMessage("Failed");
			return new ResponseEntity<CommonRes>(data, HttpStatus.OK);
		}
		else {
			//SuccessRes res = service.proceedEmployeesDetails(req);
			SuccessRes res = entityService.saveadditionalinfoHI(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if(res !=null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			}
			else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
		}
		
	}

	
	
	// ------------------------------------------- Get Apis ------------------------------------------------------//
	
	
	
	
		@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
		@PostMapping(value = "/slide/getcommondetails")
		@ApiOperation(value = "This method is Slide Common Details")
		public ResponseEntity<CommonRes> saveCommonDetails(@RequestBody  CommonGetReq req) {
			reqPrinter.reqPrint(req);
			CommonRes data = new CommonRes();
			/////// Get
			SlideCommonSaveRes res = entityService.getCommonDetails(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
			
	    }
		
		@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
		@PostMapping(value = "/slide1/getaccidentdamage")
		@ApiOperation(value = "This method is Insert Accident Damage Details")
		public ResponseEntity<CommonRes> getAccidentDamgeDetails(@RequestBody   SlideSectionGetReq req) {
			reqPrinter.reqPrint(req);
			CommonRes data = new CommonRes();
			/////// Get
			AccidentDamageSaveResponse res = entityService.getAccidentDamgeDetails(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
			
	    }
		
		@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
		@PostMapping(value = "/slide2/getallriskdetails")
		@ApiOperation(value = "This method is Insert All Risk Details")
		public ResponseEntity<CommonRes> getAllRiskDetails(@RequestBody   SlideSectionGetReq req) {
			reqPrinter.reqPrint(req);
			CommonRes data = new CommonRes();
			/////// Get
			AllRiskDetailsRes res = entityService.getAllRiskDetails(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
			
	    }
		
		@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
		@PostMapping(value = "/slide3/getburglaryandhouse")
		@ApiOperation(value = "This method is Insert Burglary  and House breaking Details")
		public ResponseEntity<CommonRes> getBurglaryAndHouseBreakingDetails(@RequestBody   SlideSectionGetReq req) {
			reqPrinter.reqPrint(req);
			CommonRes data = new CommonRes();
			/////// Get
			List<BurglaryAndHouseBreakingSaveRes> res = entityService.getBurglaryAndHouseBreakingDetails(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
			
	    }
		
		@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
		@PostMapping(value = "/slide4/getfireandperils")
		@ApiOperation(value = "This method is Insert Burglary  and House breaking Details")
		public ResponseEntity<CommonRes> getFireAndAlliedPerils(@RequestBody   SlideSectionGetReq req) {
			reqPrinter.reqPrint(req);
			CommonRes data = new CommonRes();
			/////// Get
			FireAndAlliedPerillsSaveRes res = entityService.getFireAndAlliedPerils(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
			
	    }
		
		@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
		@PostMapping(value = "/slide5/getcontent")
		@ApiOperation(value = "This method is Slide Save Content Details")
		public ResponseEntity<CommonRes> getContentDetails(@RequestBody  SlideSectionGetReq req) {
			reqPrinter.reqPrint(req);
			CommonRes data = new CommonRes();
			/////// Get
			ContentSaveRes res = entityService.getContentDetails(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
			
	    }
		
		@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
		@PostMapping(value = "/slide6/getelectronicequip")
		@ApiOperation(value = "This method is Slide Save Electronic Equip Details")
		public ResponseEntity<CommonRes> getElectronicEquipDetails(@RequestBody  SlideSectionGetReq req) {
			reqPrinter.reqPrint(req);
			CommonRes data = new CommonRes();
			/////// Get
			List<ElectronicEquipSaveRes> res = entityService.getElectronicEquipDetails(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
			
	    }
		
		@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
		@PostMapping(value = "/slide7/getempliablity")
		@ApiOperation(value = "This method is Slide Emp Liability Details")
		public ResponseEntity<CommonRes> getEmpLiabilityDetails(@RequestBody  SlideSectionGetReq req) {
			reqPrinter.reqPrint(req);
			CommonRes data = new CommonRes();
			/////// Get
			List<SlideEmpLiabilitySaveRes> res = entityService.getEmpLiabilityDetails(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
			
	    }
		
		@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
		@PostMapping(value = "/slide15/gethealthinsure")
		@ApiOperation(value = "This method is Slide Emp Liability Details")
		public ResponseEntity<CommonRes> getHealthInsure(@RequestBody  SlideSectionGetReq req) {
			reqPrinter.reqPrint(req);
			CommonRes data = new CommonRes();
			/////// Get
			List<HealthInsureGetRes> res = entityService.getHealthInsure(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
			
	    }
		
		@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
		@PostMapping(value = "/slide15/gethumantype")
		@ApiOperation(value = "This method is Slide Emp Liability Details")
		public ResponseEntity<CommonRes> getHumanType(@RequestBody  ProductLevelReq req) {
			reqPrinter.reqPrint(req);
			CommonRes data = new CommonRes();
			/////// Get
			List<HealthInsureGetRes> res = entityService.getHumantype(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
			
	    }
		
		@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
		@PostMapping(value = "/slide8/getfidelityemp")
		@ApiOperation(value = "This method is Save Slide Fidelity Emp Details")
		public ResponseEntity<CommonRes> getSlideFidelityGuarantyDetails(@RequestBody  SlideSectionGetReq req) {
			reqPrinter.reqPrint(req);
			CommonRes data = new CommonRes();
			/////// Get
			List<SlideFidelityGuarantySaveRes> res = entityService.getSlideFidelityGuarantyDetails(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
			
	    }
		

		
		@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
		@PostMapping(value = "/slide9/getmachinerybreakdown")
		@ApiOperation(value = "This method is Save Slide Machinery Breakdown Details")
		public ResponseEntity<CommonRes> getSlideMachineryBreakdownDetails(@RequestBody  SlideSectionGetReq req) {
			reqPrinter.reqPrint(req);
			CommonRes data = new CommonRes();
			/////// Get
			SlideMachineryBreakdownSaveRes res = entityService.getSlideMachineryBreakdownDetails(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
			
	    }
		
		@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
		@PostMapping(value = "/slide10/getmoneydetails")
		@ApiOperation(value = "This method is Save Money Details")
		public ResponseEntity<CommonRes> getSlideMoneyDetails(@RequestBody  SlideSectionGetReq req) {
			reqPrinter.reqPrint(req);
			CommonRes data = new CommonRes();
			/////// Get
			List<SlideMoneySaveRes> res = entityService.getSlideMoneyDetails(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
			
	    }
		
		@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
		@PostMapping(value = "/slide11/getplateglass")
		@ApiOperation(value = "This method is Save Plate Glass Details")
		public ResponseEntity<CommonRes> getSlidePlateGlassDetails(@RequestBody  SlideSectionGetReq req) {
			reqPrinter.reqPrint(req);
			CommonRes data = new CommonRes();
			/////// Get
			SlidePlateGlassSaveRes res = entityService.getSlidePlateGlassDetails(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
			
	    }
		
		@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
		@PostMapping(value = "/slide12/getpublicliability")
		@ApiOperation(value = "This method is Save Public Liability Details")
		public ResponseEntity<CommonRes> getSlidePublicLiablityDetails(@RequestBody  SlideSectionGetReq req) {
			reqPrinter.reqPrint(req);
			CommonRes data = new CommonRes();
			/////// Get
			SlidePublicLiabilitySaveRes res = entityService.getSlidePublicLiablityDetails(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
			
	    }
		
		@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
		@PostMapping(value = "/slide13/getbusinessInterruption")
		@ApiOperation(value = "This method is Insert Burglary  and House breaking Details")
		public ResponseEntity<CommonRes> getbusinessInterruption(@RequestBody   SlideSectionGetReq req) {
			reqPrinter.reqPrint(req);
			CommonRes data = new CommonRes();
			/////// Get
			BusinessInterruptionRes res = entityService.getBusinessInterruption(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
			
	    }
		
		@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
		@PostMapping(value = "/slide14/getgoodsintransit")
		@ApiOperation(value = "This method is Insert Burglary  and House breaking Details")
		public ResponseEntity<CommonRes> getgoodsintransit(@RequestBody   SlideSectionGetReq req) {
			reqPrinter.reqPrint(req);
			CommonRes data = new CommonRes();
			/////// Get
			GoodInTransitRes res = entityService.getGoodsInTransit(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
			
	    }
	
	
	
		// ------------------------------------------- Section Delete Apis ------------------------------------------------------//
		@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
		@PostMapping(value = "/slide1/deleteaccidentdamage")
		@ApiOperation(value = "This method is Insert Accident Damage Details")
		public ResponseEntity<CommonRes> deleteAccidentDamgeDetails(@RequestBody   SlideSectionGetReq req) {
			reqPrinter.reqPrint(req);
			CommonRes data = new CommonRes();
			/////// delete
			SuccessRes res = entityService.deleteAccidentDamgeDetails(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
			
	    }
		
		@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
		@PostMapping(value = "/slide2/deleteallriskdetails")
		@ApiOperation(value = "This method is Insert All Risk Details")
		public ResponseEntity<CommonRes> deleteAllRiskDetails(@RequestBody   SlideSectionGetReq req) {
			reqPrinter.reqPrint(req);
			CommonRes data = new CommonRes();
			/////// delete
			SuccessRes res = entityService.deleteAllRiskDetails(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
			
	    }
		
		@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
		@PostMapping(value = "/slide3/deleteburglaryandhouse")
		@ApiOperation(value = "This method is Insert Burglary  and House breaking Details")
		public ResponseEntity<CommonRes> deleteBurglaryAndHouseBreakingDetails(@RequestBody   SlideSectionGetReq req) {
			reqPrinter.reqPrint(req);
			CommonRes data = new CommonRes();
			/////// delete
			SuccessRes res = entityService.deleteBurglaryAndHouseBreakingDetails(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
			
	    }
		
		@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
		@PostMapping(value = "/slide4/deletefireandperils")
		@ApiOperation(value = "This method is Insert Burglary  and House breaking Details")
		public ResponseEntity<CommonRes> deleteFireAndAlliedPerils(@RequestBody   SlideSectionGetReq req) {
			reqPrinter.reqPrint(req);
			CommonRes data = new CommonRes();
			/////// delete
			SuccessRes res = entityService.deleteFireAndAlliedPerils(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
			
	    }
		
		@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
		@PostMapping(value = "/slide5/deletecontent")
		@ApiOperation(value = "This method is Slide Save Content Details")
		public ResponseEntity<CommonRes> deleteContentDetails(@RequestBody  SlideSectionGetReq req) {
			reqPrinter.reqPrint(req);
			CommonRes data = new CommonRes();
			/////// delete
			SuccessRes res = entityService.deleteContentDetails(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
			
	    }
		
		@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
		@PostMapping(value = "/slide6/deleteelectronicequip")
		@ApiOperation(value = "This method is Slide Save Electronic Equip Details")
		public ResponseEntity<CommonRes> deleteElectronicEquipDetails(@RequestBody  SlideSectionGetReq req) {
			reqPrinter.reqPrint(req);
			CommonRes data = new CommonRes();
			/////// delete
			SuccessRes res = entityService.deleteElectronicEquipDetails(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
			
	    }
		
		@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
		@PostMapping(value = "/slide7/deleteempliablity")
		@ApiOperation(value = "This method is Slide Emp Liability Details")
		public ResponseEntity<CommonRes> deleteEmpLiabilityDetails(@RequestBody  SlideSectionGetReq req) {
			reqPrinter.reqPrint(req);
			CommonRes data = new CommonRes();
			/////// delete
			SuccessRes res = entityService.deleteEmpLiabilityDetails(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
			
	    }
		
		@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
		@PostMapping(value = "/slide8/deletefidelityemp")
		@ApiOperation(value = "This method is Save Slide Fidelity Emp Details")
		public ResponseEntity<CommonRes> deleteSlideFidelityGuarantyDetails(@RequestBody  SlideSectionGetReq req) {
			reqPrinter.reqPrint(req);
			CommonRes data = new CommonRes();
			/////// delete
			SuccessRes res = entityService.deleteSlideFidelityGuarantyDetails(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
			
	    }
		

		
		@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
		@PostMapping(value = "/slide9/deletemachinerybreakdown")
		@ApiOperation(value = "This method is Save Slide Machinery Breakdown Details")
		public ResponseEntity<CommonRes> deleteSlideMachineryBreakdownDetails(@RequestBody  SlideSectionGetReq req) {
			reqPrinter.reqPrint(req);
			CommonRes data = new CommonRes();
			/////// delete
			SuccessRes res = entityService.deleteSlideMachineryBreakdownDetails(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
			
	    }
		
		@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
		@PostMapping(value = "/slide10/deletemoneydetails")
		@ApiOperation(value = "This method is Save Money Details")
		public ResponseEntity<CommonRes> deleteSlideMoneyDetails(@RequestBody  SlideSectionGetReq req) {
			reqPrinter.reqPrint(req);
			CommonRes data = new CommonRes();
			/////// delete
			SuccessRes res = entityService.deleteSlideMoneyDetails(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
			
	    }
		
		@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
		@PostMapping(value = "/slide11/deleteplateglass")
		@ApiOperation(value = "This method is Save Plate Glass Details")
		public ResponseEntity<CommonRes> deleteSlidePlateGlassDetails(@RequestBody  SlideSectionGetReq req) {
			reqPrinter.reqPrint(req);
			CommonRes data = new CommonRes();
			/////// delete
			SuccessRes res = entityService.deleteSlidePlateGlassDetails(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
			
	    }
		
		@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
		@PostMapping(value = "/slide12/deletepublicliability")
		@ApiOperation(value = "This method is Save Public Liability Details")
		public ResponseEntity<CommonRes> deleteSlidePublicLiablityDetails(@RequestBody  SlideSectionGetReq req) {
			reqPrinter.reqPrint(req);
			CommonRes data = new CommonRes();
			/////// delete
			SuccessRes res = entityService.deleteSlidePublicLiablityDetails(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
			
	    }
		
		//Corporate plus main info skip validation 
		@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
		@PostMapping(value = "/maininfovalidation")
		@ApiOperation(value = "This method is used to through main info validation")
		public ResponseEntity<CommonRes> mainInfoValidation(@RequestBody  MainInfoValidationReq req) {
			reqPrinter.reqPrint(req);
			CommonRes data = new CommonRes();
			
			List<String> validationCodes =  validateService.mainInfoValidation(req);
			
			List<Error> validation = null;
			
			if(validationCodes!=null && validationCodes.size() > 0 ) {
					
				CommonErrorModuleReq comErrDescReq = new CommonErrorModuleReq();
				//comErrDescReq.setBranchCode(req.getBranchCode());
				//comErrDescReq.setInsuranceId(req.getCompanyId());
			    comErrDescReq.setProductId("99999");
				comErrDescReq.setModuleId("28");
				comErrDescReq.setModuleName("MAIN INFO RISK");
						
				validation = errorDescService.getErrorDesc(validationCodes ,comErrDescReq);
			}
			
			//Validation
			if(validation!=null && validation.size()!=0) {
				data.setCommonResponse(null);
				data.setIsError(true);
				data.setErrorMessage(validation);
				data.setMessage("Failed");
				return new ResponseEntity<CommonRes>(data, HttpStatus.OK);
			} else {
				data.setCommonResponse(null);
				data.setIsError(false);
				data.setMessage("Success");
				return new ResponseEntity<CommonRes>(data, HttpStatus.OK);
			}
		}
//***********************************************************************************************************
		// AOO DROPDOWN
		@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_APPROVER','ROLE_USER')")
		@PostMapping(value = "/dropdown/medmalinsuranceaoo", produces = "application/json")
		@ApiOperation(value = "This method is get Industry Master Drop Down")

		public ResponseEntity<DropdownCommonRes> getAooDropdown(@RequestBody MedMalDropDownReq req) {

			DropdownCommonRes data = new DropdownCommonRes();

			// Save
			List<DropDownRes> res = entityService.getAooDropdown(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");

			if (res != null) {
				return new ResponseEntity<DropdownCommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}

		}
		
		// AOO DROPDOWN
		@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_APPROVER','ROLE_USER')")
		@PostMapping(value = "/dropdown/medmalinsuranceagg", produces = "application/json")
		@ApiOperation(value = "This method is get Industry Master Drop Down")

		public ResponseEntity<DropdownCommonRes> getAggDropdown(@RequestBody MedMalDropDownReq req) {

			DropdownCommonRes data = new DropdownCommonRes();

			// Save
			List<DropDownRes> res = entityService.getAggDropdown(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");

			if (res != null) {
				return new ResponseEntity<DropdownCommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}

		}


			// Gruop Personal Accident
			@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
			@PostMapping(value = "/slide13/savepersonlaccident")
			@ApiOperation(value = "This method is Save Personal Accident")
			public ResponseEntity<CommonRes> savePersonalAccident(@RequestBody  List<SlidePersonalAccidentSaveReq> req) {
				reqPrinter.reqPrint(req);
				CommonRes data = new CommonRes();
				
				
				req = entityService.fetchOriginalRequest(req);
				
				List<String> validationCodes = validateService.validatePersonalAccident(req);
				
				List<Error> validation = null;
				
				if(validationCodes!=null && validationCodes.size() > 0 ) {
						
					CommonErrorModuleReq comErrDescReq = new CommonErrorModuleReq();
					
					for(SlidePersonalAccidentSaveReq list : req)
					{
						//comErrDescReq.setBranchCode(list.get);
					   comErrDescReq.setInsuranceId(list.getInsuranceId());
					}
				    comErrDescReq.setProductId("99999");
					comErrDescReq.setModuleId("29");
					comErrDescReq.setModuleName("PERSONAL ACCIDENT RISK");
							
					validation = errorDescService.getErrorDesc(validationCodes ,comErrDescReq);
				}
				
				
				//// validation
				if (validation != null && validation.size() != 0) {
					data.setCommonResponse(null);
					data.setIsError(true);
					data.setErrorMessage(validation);
					data.setMessage("Failed");
					return new ResponseEntity<CommonRes>(data, HttpStatus.OK);

				} else {
					/////// save
					List<SlideSectionSaveRes> res = entityService.savePersonalAccident(req);
					data.setCommonResponse(res);
					data.setIsError(false);
					data.setErrorMessage(Collections.emptyList());
					data.setMessage("Success");
					if (res != null) {
						return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
					} else {
						return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
					}
				}
		    }
			
			@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
			@PostMapping(value = "/slide13/getpersonlaaccident")
			@ApiOperation(value = "This method is Save Public Liability Details")
			public ResponseEntity<CommonRes> getSlidePersonalAccident(@RequestBody  SlideSectionGetReq req) {
				reqPrinter.reqPrint(req);
				CommonRes data = new CommonRes();
				/////// Get
				List<SlidePersonalAccidentGetRes> res = entityService.getSlidePersonalAccident(req);
				data.setCommonResponse(res);
				data.setIsError(false);
				data.setErrorMessage(Collections.emptyList());
				data.setMessage("Success");
				if (res != null) {
					return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
				} else {
					return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
				}
				
		    }
			
			
			@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
			@PostMapping(value = "/slide13/deletepersonalaccident")
			@ApiOperation(value = "This method is Save Public Liability Details")
			public ResponseEntity<CommonRes> deleteSlidePersonalAccident(@RequestBody  SlideSectionGetReq req) {
				reqPrinter.reqPrint(req);
				CommonRes data = new CommonRes();
				/////// delete
				SuccessRes res = entityService.deleteSlidePersonalAccident(req);
				data.setCommonResponse(res);
				data.setIsError(false);
				data.setErrorMessage(Collections.emptyList());
				data.setMessage("Success");
				if (res != null) {
					return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
				} else {
					return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
				}
				
		    }
			
			
			// Gruop Personal Accident
			@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
			@PostMapping(value = "/slide14/savebuilding")
			@ApiOperation(value = "This method is Save Building ")
			public ResponseEntity<CommonRes> saveBuilding(@RequestBody  SlideBuildingSaveReq req , @RequestBody(required = false ) List<EserviceBuildingDetails> NewDataList 
					 ) {
				reqPrinter.reqPrint(req);
				CommonRes data = new CommonRes();
				List<String> validationCodes =null;
				
					validationCodes = validateService.validateBuilding(req );
				
	           List<Error> validation = null;
				
				if(validationCodes!=null && validationCodes.size() > 0 ) {
						
					CommonErrorModuleReq comErrDescReq = new CommonErrorModuleReq();
					//comErrDescReq.setBranchCode(req.getBranchCode());
					comErrDescReq.setInsuranceId(req.getInsuranceId());
				    comErrDescReq.setProductId("99999");
					comErrDescReq.setModuleId("30");
					comErrDescReq.setModuleName("BUILDING RISK");
							
					validation = errorDescService.getErrorDesc(validationCodes ,comErrDescReq);
				}
				
				//// validation
				if (validation != null && validation.size() != 0) {
					data.setCommonResponse(null);
					data.setIsError(true);
					data.setErrorMessage(validation);
					data.setMessage("Failed");
					return new ResponseEntity<CommonRes>(data, HttpStatus.OK);

				} else {
					/////// save
					List<SlideSectionSaveRes> res = entityService.saveBuilding(req , NewDataList );
					data.setCommonResponse(res);
					data.setIsError(false);
					data.setErrorMessage(Collections.emptyList());
					data.setMessage("Success");
					if (res != null) {
						return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
					} else {
						return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
					}
				}
		    }
			
			@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
			@PostMapping(value = "/slide14/getbuilding")
			@ApiOperation(value = "This method is Get Building")
			public ResponseEntity<CommonRes> getSlideBuilding(@RequestBody  SlideSectionGetReq req) {
				reqPrinter.reqPrint(req);
				CommonRes data = new CommonRes();
				/////// Get
				List<SlideBuildingGetRes> res = entityService.getSlideBuilding(req);
				data.setCommonResponse(res);
				data.setIsError(false);
				data.setErrorMessage(Collections.emptyList());
				data.setMessage("Success");
				if (res != null) {
					return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
				} else {
					return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
				}
				
		    }
			
			
			@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
			@PostMapping(value = "/slide14/deletebuilding")
			@ApiOperation(value = "This method is delete building")
			public ResponseEntity<CommonRes> deleteSlideBuilding(@RequestBody  SlideSectionGetReq req) {
				reqPrinter.reqPrint(req);
				CommonRes data = new CommonRes();
				/////// delete
				SuccessRes res = entityService.deleteSlideBuilding(req);
				data.setCommonResponse(res);
				data.setIsError(false);
				data.setErrorMessage(Collections.emptyList());
				data.setMessage("Success");
				if (res != null) {
					return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
				} else {
					return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
				}
				
		    }
			


			@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
			@PostMapping(value = "/slide20/getBond")
			@ApiOperation(value = "This method is Get Building")
			public ResponseEntity<CommonRes> getBondDetails(@RequestBody SlideSectionGetReq req) {
				reqPrinter.reqPrint(req);
				CommonRes data = new CommonRes();
				/////// Get
				List<BondRes> res = entityService.getBoundDetails(req);
				data.setCommonResponse(res);
				data.setIsError(false);
				data.setErrorMessage(Collections.emptyList());
				data.setMessage("Success");
				if (res != null) {
					return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
				} else {
					return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
				}

			}
			@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
			@PostMapping(value = "/slide20/saveBond")
			@ApiOperation(value = "This method is Save Public Liability Details")
			public ResponseEntity<CommonRes> saveBond(@RequestBody BondCommonReq req) {
				reqPrinter.reqPrint(req);
				CommonRes data = new CommonRes();
				List<String> validationCodes =null;

				 validationCodes = validateService.validateBond(req);

				List<Error> validation = null;

				
				  if(validationCodes!=null && validationCodes.size() > 0 ) {
				  
				  CommonErrorModuleReq comErrDescReq = new CommonErrorModuleReq();
				  //comErrDescReq.setBranchCode(req.getBranchCode());
				  comErrDescReq.setInsuranceId(req.getInsuranceId());
				  comErrDescReq.setProductId("99999"); comErrDescReq.setModuleId("30");
				  comErrDescReq.setModuleName("BUILDING RISK");
				  
				  validation = errorDescService.getErrorDesc(validationCodes ,comErrDescReq); }
				 

				//// validation
				
				  if (validation != null && validation.size() != 0) {
				  data.setCommonResponse(null); data.setIsError(true);
				  data.setErrorMessage(validation); data.setMessage("Failed"); return new
				  ResponseEntity<CommonRes>(data, HttpStatus.OK);
				  
				  } else {
				 
				/////// save
				List<SlideSectionSaveRes> res = entityService.saveBond(req);
				
				  data.setCommonResponse(res); data.setIsError(false);
				  data.setErrorMessage(Collections.emptyList()); data.setMessage("Success"); if
				  (res != null) { return new ResponseEntity<CommonRes>(data,
				  HttpStatus.CREATED); } else { return new ResponseEntity<>(null,
				  HttpStatus.BAD_REQUEST); } }
				 

			}
	
					    
			@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_APPROVER','ROLE_USER')")
			@RequestMapping(value = "/saveAllSection", method = RequestMethod.POST)
			@ApiOperation(value = "Save all the setion data in same Api", notes = "Save Api For Section")
			public ResponseEntity<CommonResponse> saveAllSectionData(@RequestBody AllSectionSaveReq req) {

				CommonResponse data = new CommonResponse();
				List<Object> objects = new ArrayList<>();

				ResponseEntity<CommonResponse> res = entityService.saveAllSectionDetails(req, data, objects);
				return res;

			}
	         
			@RequestMapping("/deletefire") 
			public ResponseEntity<CommonRes> deleteFire(@RequestBody List<FireDelete> req)
			{
				
				CommonRes res = entityService.deletefire(req);	
				if (res != null) {
					return new ResponseEntity<CommonRes>(res, HttpStatus.CREATED);
				} else {
					return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
				}	
				
			}	
			

			@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_APPROVER','ROLE_USER')")
			@RequestMapping(value = "/saveFire", method = RequestMethod.POST)
			@ApiOperation(value = "Save all the setion data in same Api", notes = "Save Api For Section")
			public ResponseEntity<CommonRes> saveFire(@RequestBody  CommonRequest req1) {
				
               List<Error> validation1 = null;
				 CommonRes data1 = new CommonRes();
				 Error error = new Error();
				 List<Error> validation2 = null;
				 List<SlideSectionSaveRes> res1=new ArrayList<>();
				String requestrefno=null;
			     //generate Request Reference no
				List<String> validationCodes1=validateService.validatecommondetails(req1);
				List<Error> validationCodes2=validateService.validateFireAndAlliedPerills(req1);
				if(validationCodes1!=null && validationCodes1.size() > 0  ) {
					CommonErrorModuleReq comErrDescReq = new CommonErrorModuleReq();
					//comErrDescReq.setBranchCode(req.getBranchCode());
					comErrDescReq.setInsuranceId(req1.getInsuranceId());
					comErrDescReq.setProductId("99999");
					comErrDescReq.setModuleId("30");
					comErrDescReq.setModuleName("BUILDING RISK");
					validation1 = errorDescService.getErrorDesc(validationCodes1 ,comErrDescReq);
					
				}
				//// validation
				
				if ((validation1 != null && validation1.size() != 0) || (validationCodes2!=null && validationCodes2.size()>0  )) {
				   List<Error> data =new ArrayList<>();
					if(validation1!=null) data.addAll(validation1);
					if(!validationCodes2.isEmpty())data.addAll(validationCodes2);
					data1.setCommonResponse(null);
					data1.setIsError(true);
					data1.setErrorMessage(data);
					data1.setMessage("Failed");
					return new ResponseEntity<CommonRes>(data1, HttpStatus.OK);

				}
			     requestrefno = entityService.generaterequestno(req1);
			      if(req1.getRequestReferenceNo()==null)
			      {
			    	  req1.setRequestReferenceNo(requestrefno);
			    	  
			      }
			     boolean DeleteStatus= entityService.DeleteFire(req1);
			     if(DeleteStatus) {System.out.println("Deleted Successfully");}
					
					
					 
					 
			      for(int i=0;i<req1.getFiredel().size();i++) 
			      {  
			    	  //Request MApping
			    	  int rowIndex = i + 1;
			    	  FireReq fulldata=new DozerBeanMapper().map(req1,FireReq.class);
			    	  fulldata.setRequestReferenceNo(req1.getRequestReferenceNo());
			    	  fulldata.setRiskId(req1.getFiredel().get(i).getRiskId());
			    	  fulldata.setSectionId(req1.getFiredel().get(i).getSectionId());
			    	  fulldata.setBuildingSumInsured(req1.getFiredel().get(i).getBuildingSumInsured());
			    	  fulldata.setLocationName(req1.getFiredel().get(i).getLocationName());
			    	  fulldata.setIndustryType(req1.getFiredel().get(i).getIndustryType());
			    	  fulldata.setIndustrytypedesc(req1.getFiredel().get(i).getIndustrytypedesc());
			    	  fulldata.setOccupationId(req1.getFiredel().get(i).getOccupationId());
			    	  fulldata.setOccupationDesc(req1.getFiredel().get(i).getOccupationDesc());
			    	  fulldata.setCoveringDetails(req1.getFiredel().get(i).getCoveringDetails());
			    	  fulldata.setDescriptionOfRisk(req1.getFiredel().get(i).getDescriptionOfRisk());
			    	  fulldata.setRegionCode(req1.getFiredel().get(i).getRegionCode());
			    	  fulldata.setDistrictCode(req1.getFiredel().get(i).getDistrictCode());
			    	  fulldata.setBusinessInterruption(req1.getFiredel().get(i).getBusinessInterruption());
			          boolean flag= entityService.saveSectiondetails(fulldata);
			     
                  reqPrinter.reqPrint(req1);
				
				     /////// save
					if(flag) {
						SlideSectionSaveRes res  = entityService.saveFire(fulldata);
					 res1.add(res);
					
					}
					
					}
			        data1.setCommonResponse(res1);
					data1.setIsError(false);
					data1.setErrorMessage(Collections.emptyList());
					data1.setMessage("Success");
	           
			      
					if (!res1.isEmpty()) {
						return new ResponseEntity<CommonRes>(data1, HttpStatus.CREATED);
					} else {
						return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
					}
					
		}
					
			
		*/	

	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping(value = "/saveriskandcalcpremium")
	@ApiOperation(value = "This method is save Risk and ")
	public ResponseEntity<CommonRes> saveRiskDetailsWithPremiumCalc(@RequestBody  WhatsappPremiumCalcReq req,@RequestHeader("Authorization") String tokens) {
		reqPrinter.reqPrint(req);
		/////// delete
		CommonRes res = entityService.saveRiskDetailsWithPremiumCalc(req,tokens.replaceAll("Bearer ", "").split(",")[0]);
		return new ResponseEntity<CommonRes>(res, HttpStatus.CREATED);
		
    }
	
	
	
	//@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_APPROVER','ROLE_USER')")
    @PostMapping("/getbrokertiracode")
 	@ApiOperation(value = "This method is Get Broker Tira Code")
 	public ResponseEntity<CommonRes> searchPremiaBrokerTiraCode(@RequestBody PremiaTiraReq req) {
 		reqPrinter.reqPrint(req);
 		CommonRes data = new CommonRes();
 		
 		List<PremiaTiraRes> resList = tiraService.searchPremiaBrokerTiraCode(req);
 		
 		if (resList == null || resList.size() == 0) {
				List<Error> error = new  ArrayList<Error>();
				error.add(new Error("01","TiraCode","Broker Tira Code Not Available"));
				
			data.setCommonResponse(null);
			data.setIsError(true);
			data.setErrorMessage(error);
			data.setMessage("Failed");
			return new ResponseEntity<CommonRes>(data, HttpStatus.OK);

		} else {
			DropDownRes res = new DropDownRes();
 			res.setCode(resList.size()> 0? resList.get(0).getTiraCode() :"" );
 			res.setCodeDesc(resList.size()> 0? resList.get(0).getTiraCode() :"" );
 	 		data.setCommonResponse(res);
 	 		data.setIsError(false);
 	 		data.setErrorMessage(Collections.emptyList());
 	 		data.setMessage("Success");
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		}
			
			 
	}
	
	//@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_APPROVER','ROLE_USER')")
    @PostMapping("/getbrokerspcode")
 	@ApiOperation(value = "This method is Get Broker Sp Code")
 	public ResponseEntity<CommonRes> searchPremiaBrokerSpCode(@RequestBody PremiaSpCodeReq req) {
 		reqPrinter.reqPrint(req);
 		CommonRes data = new CommonRes();
			PremiaTiraReq req2 = new PremiaTiraReq(); 
			req2.setInsuranceId(req.getInsuranceId());
			req2.setPremiaCode(req.getSpCode());
			
 			List<PremiaTiraRes> resList = tiraService.searchPremiaBrokerSpCode(req2);

// 			if (resList == null || resList.size() == 0) {
// 				List<Error> error = new  ArrayList<Error>();
// 				error.add(new Error("01","SpCode","Broker Sale Point Code Not Available"));
// 				
//				data.setCommonResponse(null);
//				data.setIsError(true);
//				data.setErrorMessage(error);
//				data.setMessage("Failed");
//				return new ResponseEntity<CommonRes>(data, HttpStatus.OK);
//
//			}  else {
				List<DropDownRes> list = new ArrayList<DropDownRes>();
				for(PremiaTiraRes resp : resList) {
					DropDownRes res = new DropDownRes();
		 			res.setCode( resp.getTiraCode());
		 			res.setCodeDesc(resp.getTiraDesc() );
		 			list.add(res);
				}
	 	 		data.setCommonResponse(list);
	 	 		data.setIsError(false);
	 	 		data.setErrorMessage(Collections.emptyList());
	 	 		data.setMessage("Success");
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		//	}

			 
	}

		//----------------------------------------------------------------------------------------------//	
			
			@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
			@PostMapping(value = "/slide/?")
			@ApiOperation(value = "This method is Slide Common Details")
			public ResponseEntity<CommonRes> nonMotorSaveDetails(@RequestBody  NonMotorSaveReq req) {
				reqPrinter.reqPrint(req);
				CommonRes data = new CommonRes();
				List<String> validationCodes = validateService.CompanyBasedValidation(req);

				
				List<Error> validation = null;
				if(validationCodes!=null && validationCodes.size() > 0 ) {
					CommonErrorModuleReq comErrDescReq = new CommonErrorModuleReq();
					comErrDescReq.setBranchCode(req.getNonMotorPolicyReq().getBranchCode());
					comErrDescReq.setInsuranceId(req.getNonMotorPolicyReq().getCompanyId());
					comErrDescReq.setProductId("99999");
					comErrDescReq.setModuleId("30");
					comErrDescReq.setModuleName("BUILDING RISK");
					
					validation = errorDescService.getErrorDesc(validationCodes ,comErrDescReq);
				}
				// validation
				if (validation != null && validation.size() != 0) {
					data.setCommonResponse(null);
					data.setIsError(true);
					data.setErrorMessage(validation);
					data.setMessage("Failed");
					return new ResponseEntity<CommonRes>(data, HttpStatus.OK);

				}
			  else {
					// save
				  	List<SlideSectionSaveRes> res = entityService.MapBasedOnCompany(req);
					data.setCommonResponse(res);
					data.setIsError(false);
					data.setErrorMessage(Collections.emptyList());
					data.setMessage("Success");
					if (res != null) {
						return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
					} else {
						return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
					}
				}
		    }
			
			@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
			@PostMapping(value = "/slide/nonmotorsave")
			@ApiOperation(value = "This method is Slide Common Details")
			public ResponseEntity<CommonRes> sectionWiseEdit(@RequestBody NonMotorSaveReq req,
					@RequestHeader("Authorization") String tokens) {
				CommonRes data = new CommonRes();
				reqPrinter.reqPrint(req);
				long start = System.currentTimeMillis();
				System.out.println("Validaition Time Start : " + start);
				List<String> validationCodes = validateService.CompanyBasedValidation(req);
				long end = System.currentTimeMillis();
				long timeTaken = end - start;

				System.out.println("Validation Time END");
				System.out.println("⏳ Time Taken for CompanyBasedValidation: " + timeTaken + " ms");
				System.out.println("⏳ In seconds: " + (timeTaken / 1000.0) + " sec");
				System.out.println("⏳ In minutes: " + (timeTaken / 60000.0) + " min");

				List<Error> validation = null;
				if (validationCodes != null && validationCodes.size() > 0) {
					CommonErrorModuleReq comErrDescReq = new CommonErrorModuleReq();
					comErrDescReq.setBranchCode(req.getNonMotorPolicyReq().getBranchCode());
					comErrDescReq.setInsuranceId(req.getNonMotorPolicyReq().getCompanyId());
					comErrDescReq.setProductId("99999");
					comErrDescReq.setModuleId("30");
					comErrDescReq.setModuleName("BUILDING RISK");

					validation = errorDescService.getErrorDesc(validationCodes, comErrDescReq);
				}
				// validation
				if (validation != null && validation.size() != 0) {
					data.setCommonResponse(null);
					data.setIsError(true);
					data.setErrorMessage(validation);
					data.setMessage("Failed");
					return new ResponseEntity<CommonRes>(data, HttpStatus.OK);

				} else {
					String customerReferenceNo = req.getNonMotorPolicyReq().getCustomerReferenceNo();
					if (StringUtils.isNotBlank(customerReferenceNo)) {
						EserviceCustomerDetails cudt = custRepo.findByCustomerReferenceNo(customerReferenceNo);
						if (cudt != null) {
							req.getNonMotorBrokerReq().setCustomerName(cudt.getClientName());
							req.setCustomerDetails(cudt);
						}

					}
					CommonRes res = entityService.MapBasedOnCompany(req, tokens);
					res.setIsError(false);
					res.setErrorMessage(Collections.emptyList());
					res.setMessage("Success");
					if (res.getCommonResponse() != null) {
						return new ResponseEntity<CommonRes>(res, HttpStatus.CREATED);
					} else {
						return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
					}
				}

			}
			
			@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
			@PostMapping(value = "/slide/sectionWiseDelete")
			@ApiOperation(value = "This method is Slide Common Details")
			public ResponseEntity<CommonRes> sectionWisDelet(@RequestBody  SectionSaveReq req) {
				reqPrinter.reqPrint(req);
				CommonRes data = new CommonRes();
				data = entityService.deletSectionRecords(req);
				
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			}
			
			@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
			@PostMapping(value = "/slide/updateDateSection")
			@ApiOperation(value = "This method is Slide Common Details")
			public ResponseEntity<CommonRes> updateDateSection(@RequestBody  updateDateReq req) {
				reqPrinter.reqPrint(req);
				CommonRes data = new CommonRes();
				data = update.updateDateSection(req);
				
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			}

			
			//---Not used 
			@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
			@PostMapping(value = "/slide/OldgetNonMotor")
			@ApiOperation(value = "This method is Slide Common Details")
			public ResponseEntity<CommonRes> getAllNonMotorDetails(@RequestBody NonMotorComRes Req)
			{
				
				reqPrinter.reqPrint(Req);
				CommonRes data = new CommonRes();
				NonMotorRes res = entityService.getAllNonMotorDetails(Req);
				if (res != null) {
					data.setCommonResponse(res);
					data.setIsError(false);
					return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
				} else {
					data.setCommonResponse(res);
					data.setIsError(true);
					return new ResponseEntity<>(data,HttpStatus.CREATED);
				}

			}
			
			@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
			@PostMapping(value = "/slide/GetNonMotor")
			@ApiOperation(value = "This method is Slide Common Details")
			public ResponseEntity<CommonRes> getNonMotorDetails(@RequestBody NonMotorComRes Req)
			{
				
				reqPrinter.reqPrint(Req);
				CommonRes data = new CommonRes();
				NonMotorSaveRes res = entityService.getNonMotorDetails(Req);
				if (res != null) {
					data.setCommonResponse(res);
					data.setIsError(false);
					return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
				} else {
					data.setCommonResponse(res);
					data.setIsError(true);
					return new ResponseEntity<>(data,HttpStatus.CREATED);
				}

			}

			
			@PostMapping(value = "/slide/setNonMotor")
			@ApiOperation(value = "This method is Slide Common Details")
			public ResponseEntity<CommonRes> setNonMotor(@RequestBody NonMotorComRes Req)
			{
				
				reqPrinter.reqPrint(Req);
				CommonRes data = new CommonRes();
				NonMotorSaveRes res = entityService.setNonmotorsave(Req);
				if (res != null) {
					data.setCommonResponse(res);
					data.setIsError(false);
					return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
				} else {
					data.setCommonResponse(res);
					data.setIsError(true);
					return new ResponseEntity<>(data,HttpStatus.CREATED);
				}

			}

			
			@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
			@PostMapping(value = "/savefirstlosspayee")
			@ApiOperation(value = "This method is First Loss payee")
			public ResponseEntity<CommonRes> SaveFirstLossPayee(@RequestBody List<FirstLossPayeeReq> req)
			{
				reqPrinter.reqPrint(req);
				CommonRes data = new CommonRes();
				List<String> validationCodes = validateService.validatenonfirstLossPayee(req);
				
				List<Error> validation = null;
				if(validationCodes!=null && validationCodes.size() > 0 ) {
					CommonErrorModuleReq comErrDescReq = new CommonErrorModuleReq();
					comErrDescReq.setBranchCode(req.get(0).getBranchCode());
					comErrDescReq.setInsuranceId(req.get(0).getCompanyId());
					comErrDescReq.setProductId("99999");
					comErrDescReq.setModuleId("30");
					comErrDescReq.setModuleName("BUILDING RISK");
					
					validation = errorDescService.getErrorDesc(validationCodes ,comErrDescReq);
				}
				// validation
				if (validation != null && validation.size() != 0) {
					data.setCommonResponse(null);
					data.setIsError(true);
					data.setErrorMessage(validation);
					data.setMessage("Failed");
					return new ResponseEntity<CommonRes>(data, HttpStatus.OK);

				}
			  else {

				SuccessRes res = entityService.SaveFirstLossPayee(req);
				if (res != null) {
					data.setCommonResponse(res);
					data.setIsError(false);
					return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
				} else {
					data.setCommonResponse(res);
					data.setIsError(true);
					return new ResponseEntity<>(data,HttpStatus.CREATED);
				}
			  }

			}

			@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
			@PostMapping(value = "/getfirstlosspayee")
			@ApiOperation(value = "This method is Slide Common Details")
			public ResponseEntity<CommonRes> getFirstLossPayee(@RequestBody FirstLossPayeeReq Req)
			{
				
				reqPrinter.reqPrint(Req);
				CommonRes data = new CommonRes();
				List<FirstLossPayeeRes> res = entityService.getFirstLossPayee(Req);
				if (res != null) {
					data.setCommonResponse(res);
					data.setIsError(false);
					return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
				} else {
					data.setCommonResponse(res);
					data.setIsError(true);
					return new ResponseEntity<>(data,HttpStatus.CREATED);
				}

			}
         
			@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
			@PostMapping(value = "/InsertEngineerInfo")
			@ApiOperation(value = "Save Engineering info")
			public ResponseEntity<CommonRes> saveEngineeringInfo(@RequestBody List<EngineeringReq> req) {
				reqPrinter.reqPrint(req);
				CommonRes data = new CommonRes();
				
				//List<String> validationCodes = validateService.Engineervalidation(req);
				
				String response = entityService.saveEngineer(req);
				
				if(response!=null)
				{
					data.setCommonResponse("Saved");
					data.setIsError(false);
					return new ResponseEntity<>(data,HttpStatus.ACCEPTED);
				}else {
					data.setCommonResponse("Failed");
					data.setIsError(true);
					return new ResponseEntity<>(data,HttpStatus.OK);
				}

			}
			
			@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
			@PostMapping(value = "/insertMarineHull")
			@ApiOperation(value = "Save MarineHull info")
			public ResponseEntity<CommonRes> saveMarineHull(@RequestBody List<MarineHullReq> req) {
				reqPrinter.reqPrint(req);
				CommonRes data = new CommonRes();

				String response = entityService.insertMarineHull(req);

				if (response != null) {
					data.setCommonResponse("Saved");
					data.setIsError(false);
					return new ResponseEntity<>(data, HttpStatus.ACCEPTED);
				} else {
					data.setCommonResponse("Failed");
					data.setIsError(true);
					return new ResponseEntity<>(data, HttpStatus.OK);
				}

			}
			@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
			@PostMapping(value ="/getMarineHullInfo")
			@ApiOperation(value ="get Marine Hull Info")
			public ResponseEntity<CommonRes> getMarinHull(@RequestBody MarineHullInfoReq req)
			{
				reqPrinter.reqPrint(req);
				String requestRefNo = req.getRequestRefNo();
				CommonRes data = new CommonRes();
				List<MarineHullReq>  response =entityService.getMarinHullInfo(requestRefNo);
				if(response!=null)
				{
					data.setCommonResponse(response);
					data.setIsError(false);
					return new ResponseEntity<>(data,HttpStatus.OK);
				}
				else {
					data.setCommonResponse(response);
				    data.setIsError(true);
					return new ResponseEntity<>(HttpStatus.OK);

				}

			}

			@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
			@PostMapping(value ="/getEngineerInfo")
			@ApiOperation(value ="get Engineering info")
			public ResponseEntity<CommonRes> getEngineeringInfo(@RequestParam String RequestReferenceno)
			{
				reqPrinter.reqPrint(RequestReferenceno);
				CommonRes data = new CommonRes();
				List<EngineeringReq>  response =entityService.getEngineerInfo(RequestReferenceno);
				if(response!=null)
				{
					data.setCommonResponse(response);
					data.setIsError(false);
					return new ResponseEntity<>(data,HttpStatus.OK);
				}
				else {
					data.setCommonResponse(response);
				    data.setIsError(true);
					return new ResponseEntity<>(HttpStatus.OK);

				}

			}

			@PostMapping(value = "/slide/nonmotorcommonsave")
			@ApiOperation(value = "This method is Slide Common Details")
			public ResponseEntity<CommonRes> nonMotorCommonSaveDetails(@RequestBody NonMotorCommonRequest req, @RequestHeader("Authorization") String tokens) {
				reqPrinter.reqPrint(req);
				CommonRes data = new CommonRes();
				List<SlideSectionSaveRes> res = entityService.nonMotorCommonSaveDetails(req, tokens);
				data.setCommonResponse(res);
				data.setIsError(false);
				data.setErrorMessage(Collections.emptyList());
				data.setMessage("Success");

				if (res != null) {
					return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
				} else {
					return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
				}
			}

		 @PostMapping(value = "/slide/collateralValue")
			public ResponseEntity<CommonRes> collateralDetailSave(@RequestBody List<CollateralDetailsReq> req) {
    	CommonRes data = new CommonRes();
    
    	String response = entityService.saveCollateralDetailSave(req);
		
		if(response!=null)
		{
			data.setCommonResponse("Saved");
			data.setIsError(false);
			return new ResponseEntity<>(data,HttpStatus.ACCEPTED);
		}else {
			data.setCommonResponse("Failed");
			data.setIsError(true);
			return new ResponseEntity<>(HttpStatus.OK);

			}
    }
		 
			
			@PostMapping(value ="/getCollateralvalue")
//			@ApiOperation(value ="get Engineering info")
			public ResponseEntity<CommonRes> getCollateralValue(@RequestParam String RequestReferenceno)
			{
				reqPrinter.reqPrint(RequestReferenceno);
				CommonRes data = new CommonRes();
				List<CollateralDetailsReq>  response =entityService.getCollateralDetails(RequestReferenceno);
				if(response!=null)
				{
					data.setCommonResponse(response);
					data.setIsError(false);
					return new ResponseEntity<>(data,HttpStatus.OK);
				}
				else {
					data.setCommonResponse(response);
				    data.setIsError(true);
					return new ResponseEntity<>(HttpStatus.OK);

				}

			}
			
			@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
			@PostMapping(value = "/insertAviationInfo")
			@ApiOperation(value = "Save AviationInfo info")
			public ResponseEntity<CommonRes> saveAviationInfo(@RequestBody List<AviationInfoDto> req) {
				reqPrinter.reqPrint(req);
				CommonRes data = new CommonRes();

				String response = entityService.insertAviationInfoList(req);

				if ("Saved".equalsIgnoreCase(response)) {
					data.setCommonResponse("Saved");
					data.setIsError(false);
					return new ResponseEntity<>(data, HttpStatus.ACCEPTED);
				} else {
					data.setCommonResponse(response);
					data.setIsError(true);
					return new ResponseEntity<>(data, HttpStatus.OK);
				}

			}
			
			@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
			@PostMapping(value ="/getAviationInfo")
			@ApiOperation(value ="get AviationInfo ")
			public ResponseEntity<CommonRes> getAviationInfo(@RequestBody MarineHullInfoReq req)
			{
				reqPrinter.reqPrint(req);
				String requestRefNo = req.getRequestRefNo();
				CommonRes data = new CommonRes();
				List<AviationInfoDto>  response =entityService.getAviationInfoList(requestRefNo);
				if(response!=null)
				{
					data.setCommonResponse(response);
					data.setIsError(false);
					return new ResponseEntity<>(data,HttpStatus.OK);
				}
				else {
					data.setCommonResponse(response);
				    data.setIsError(true);
					return new ResponseEntity<>(HttpStatus.OK);

				}

			}
			
			
			@PostMapping("/addRiskInfoEndt")
			public ResponseEntity<CommonRes> addRiskInfo(@RequestBody AddRiskInfoReq request) {
				CommonRes data = new CommonRes();
				data = entityService.addRiskInfoEndt(request);
				 	if (data != null) {
						return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
					} else {
						return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
					}
			}
			
			@PostMapping("/getAddRiskInfo")
			public ResponseEntity<CommonRes> getAddRiskInfoList(@RequestBody GetAddInfoReq request) {
				CommonRes data = new CommonRes();
				data = entityService.getaddRiskInfoEndt(request);
				 	if (data != null) {
						return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
					} else {
						return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
					}
			}
			
			
			@PostMapping("/addRiskInfoEndtAll")
			public ResponseEntity<CommonRes> addRiskInfoAll(@RequestBody AdditionalInformationFlatRequest request) {
				CommonRes data = new CommonRes();
				data = entityService.addRiskInfoEndtAll(request);
				 	if (data != null) {
						return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
					} else {
						return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
					}
			}

			@PostMapping("/fireRiskInfo/saveOrUpdate")
			public ResponseEntity<CommonRes> saveOrUpdateFireRiskInfo(@RequestBody List<FireRiskInfoSaveReq> reqList) {

				reqPrinter.reqPrint(reqList);
				CommonRes data = new CommonRes();

				if (reqList == null || reqList.isEmpty()) {
					data.setCommonResponse(null);
					data.setIsError(true);
					data.setErrorMessage(Collections.emptyList());
					data.setMessage("Failed - Request list is empty");
					return new ResponseEntity<>(data, HttpStatus.OK);
				}

				String response = entityService.saveOrUpdateFireRiskInfo(reqList);

				if ("Saved".equalsIgnoreCase(response)) {
					data.setCommonResponse("Saved");
					data.setIsError(false);
					data.setErrorMessage(Collections.emptyList());
					data.setMessage("Success");
					return new ResponseEntity<>(data, HttpStatus.CREATED);
				} else {
					data.setCommonResponse(null);
					data.setIsError(true);
					data.setErrorMessage(Collections.emptyList());
					data.setMessage("Failed");
					return new ResponseEntity<>(data, HttpStatus.OK);
				}
			}
			
			@PostMapping("/fireRiskInfo/getList")
			public ResponseEntity<CommonRes> getFireRiskInfoList(@RequestBody FireRiskInfoGetReq req) {

				reqPrinter.reqPrint(req);
				CommonRes data = new CommonRes();

				List<FireRiskInfoRes> resList = entityService.getFireRiskInfoList(req);

				if (resList != null) {
					data.setCommonResponse(resList);
					data.setIsError(false);
					data.setErrorMessage(Collections.emptyList());
					data.setMessage("Success");
					return new ResponseEntity<>(data, HttpStatus.OK);
				} else {
					data.setCommonResponse(null);
					data.setIsError(true);
					data.setErrorMessage(Collections.emptyList());
					data.setMessage("No records found");
					return new ResponseEntity<>(data, HttpStatus.OK);
				}
			}
			
			
			
}
