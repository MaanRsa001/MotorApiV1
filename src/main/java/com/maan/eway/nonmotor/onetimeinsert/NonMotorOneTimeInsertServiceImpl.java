package com.maan.eway.nonmotor.onetimeinsert;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.maan.eway.bean.EserviceCommonDetails;
import com.maan.eway.bean.MsCommonDetails;
import com.maan.eway.bean.MsCustomerDetails;
import com.maan.eway.bean.OccupationMaster;
import com.maan.eway.common.req.SequenceGenerateReq;
import com.maan.eway.common.res.CommonRes;
import com.maan.eway.common.service.impl.EserviceSlideSaveServiceImpl;
import com.maan.eway.common.service.impl.GenerateSeqNoServiceImpl;
import com.maan.eway.nonmotor.onetimeinsert.request.EndorsementRequest;
import com.maan.eway.nonmotor.onetimeinsert.request.NonMotorOneTimeRequest;
import com.maan.eway.nonmotor.onetimeinsert.request.PersonalAccidentRequest;
import com.maan.eway.repository.EserviceCommonDetailsRepository;
import com.maan.eway.repository.MsCommonDetailsRepository;

@Service
public class NonMotorOneTimeInsertServiceImpl implements  NonMotorOneTimeInsertService{
	
	Logger log = LogManager.getLogger(NonMotorOneTimeInsertServiceImpl.class);
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	@Autowired
	private NonMotorInputValidation validation;
	
	@Autowired
	private Gson print;
	
	@Autowired
	private EserviceCommonDetailsRepository ecdRepository;
	
	@Autowired
	private EserviceSlideSaveServiceImpl eserviceSlideSaveService;
	
	@Autowired
	private ParallelThreadServiceImpl parallelService;
	
	@Autowired
	private MsCommonDetailsRepository mscommonRepository;
	
	@Autowired
	private GenerateSeqNoServiceImpl sequence;
	
	
	@Override
	public CommonRes onetimeInsert(NonMotorOneTimeRequest req) {
		CommonRes response = new CommonRes();
		log.info("onetimeInsert request : "+print.toJson(req));
		try {
			
			List<com.maan.eway.error.Error> error =validation.nonotorReq(req);
			
			if(error.isEmpty()) {
				
				String request_ref_no =StringUtils.isBlank(req.getRequestReferenceNo())?"":req.getRequestReferenceNo();
				if(StringUtils.isBlank(request_ref_no)) {
					SequenceGenerateReq generateSeqReq = new SequenceGenerateReq();
				 	generateSeqReq.setInsuranceId(req.getInsuranceId());  
				 	generateSeqReq.setProductId(req.getProductId());
				 	generateSeqReq.setType("2");
				 	generateSeqReq.setTypeDesc("REQUEST_REFERENCE_NO");
				 	request_ref_no =  sequence.generateSeqCall(generateSeqReq);
				}
				req.setRequestReferenceNo(request_ref_no);
				if("FIRE".equalsIgnoreCase("FIREE")) {
					
					saveFireInfo(req);
					
				}else if("13".equals(req.getProductId())) {	
					
					List<EserviceCommonDetails> commonDetails = savePersonalAccident(req);			
					response = personalAccidentOneTimeInsert(commonDetails,req);					
				}
				
			}else {
				response.setErroCode(0);
				response.setErrorMessage(error);
				response.setIsError(true);
				response.setCommonResponse(Collections.EMPTY_LIST);
				response.setMessage("Error");
				
			}
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return response;
	}


	private CommonRes personalAccidentOneTimeInsert(List<EserviceCommonDetails> commonDetails,
			NonMotorOneTimeRequest req) {
		CommonRes response = new CommonRes();
		try {
			
			CompletableFuture<List<Map<String,Object>>> future_thread_1 = parallelService.personal_accident_insert(commonDetails);
			CompletableFuture<MsCustomerDetails> future_thread_2 = parallelService.ms_customer_insert(req.getCustomerReferenceNo());
			CompletableFuture.allOf(future_thread_1,future_thread_2).join();
			
			List<Map<String,Object>> pa_data = future_thread_1.get();
			MsCustomerDetails ms_customer = future_thread_2.get();
			List<Map<String,String>> return_response = new ArrayList<>();
			for (Map<String,Object> map : pa_data ) {
				
				Long vd_ref_no =map.get("vdRefNo")==null?0L:Long.valueOf(map.get("vdRefNo").toString());
				Long cd_ref_no =ms_customer.getCdRefno();
				String branch_code =req.getBranchCode();
				String agency_code=req.getAgencyCode();
				String company_id =req.getInsuranceId();
				Integer product_id =Integer.valueOf(req.getProductId());
				Integer section_id =map.get("SectionId")==null?0:Integer.valueOf(map.get("SectionId").toString());
				String risk_id =map.get("RiskId").toString();
				MsCommonDetails ms_common_exists = mscommonRepository.findByCdRefnoAndVdRefnoAndRequestreferencenoAndAgencyCodeAndBranchCodeAndInsuranceIdAndProductIdAndSectionIdAndStatusAndDdRefno(cd_ref_no ,vd_ref_no ,req.getRequestReferenceNo(),agency_code,branch_code,company_id,product_id,section_id,"Y",0L) ;
				String mcd_ref_no="";
				if(ms_common_exists!=null) {
					mcd_ref_no = String.valueOf(ms_common_exists.getMsRefno());
				}else {
					mcd_ref_no =parallelService.genOneTimeTableRefNo();				
					MsCommonDetails ms_common = MsCommonDetails.builder()
							.agencyCode(agency_code)
							.branchCode(branch_code)
							.insuranceId(company_id)
							.productId(product_id)
							.requestreferenceno(req.getRequestReferenceNo())
							.sectionId(section_id)
							.cdRefno(cd_ref_no)
							.msRefno(Long.valueOf(mcd_ref_no))
							.vdRefno(vd_ref_no)
							.ddRefno(0L)
							.entryDate(new Date())
							.status("Y")
							.build();
					mscommonRepository.save(ms_common);
				}
				
				Map<String,String> response_map = new HashMap<>();
				response_map.put("CdRefNo", cd_ref_no.toString());
				response_map.put("VdRefNo", vd_ref_no.toString());
				response_map.put("MSRefNo", mcd_ref_no);
				response_map.put("RequestReferenceNo", req.getRequestReferenceNo());
				response_map.put("VehicleId", risk_id);
				response_map.put("ProductId", product_id.toString());
				response_map.put("SectionId", section_id.toString());
				response_map.put("CompanyId", company_id);
				response_map.put("DdRefNo", "");
				return_response.add(response_map);
				
				response.setErroCode(0);
				response.setErrorMessage(Collections.EMPTY_LIST);
				response.setIsError(false);
				response.setCommonResponse(return_response);
				response.setMessage("Data has inserted successfully in table");
			}
			
			return response;
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return null;
	}


	private List<EserviceCommonDetails> savePersonalAccident(NonMotorOneTimeRequest req) {
		try {
			List<PersonalAccidentRequest> paList = req.getPaRequest();			
			String reference_no =req.getRequestReferenceNo();
			String product_id =req.getProductId();
			List<EserviceCommonDetails> ecdList = new ArrayList<>();
			
			for(PersonalAccidentRequest pa : paList) {
				Integer riskId =Integer.valueOf(pa.getRiskId());
				String sectionId =pa.getSectionId();
				EserviceCommonDetails ecd = ecdRepository.findByRequestReferenceNoAndProductIdAndSectionIdAndRiskId(reference_no ,product_id, sectionId,riskId);		 
			
				if(StringUtils.isBlank(req.getOrginalPolicyNo()) && ecd!=null)
					ecdRepository.delete(ecd);
				
				OccupationMaster occupationData =null;
				if(StringUtils.isNotBlank(pa.getOccupationId()) ) {
					 occupationData =
							eserviceSlideSaveService.getOccupationMasterDropdown(req.getInsuranceId(), req.getBranchCode(), product_id, pa.getOccupationId());					
				}
				
				Date policy_start_date =sdf.parse(req.getPolicyStartDate());
				Date policy_end_date =sdf.parse(req.getPolicyEndDate());
				Long policy_period = ChronoUnit.DAYS.between(policy_start_date.toInstant(), policy_end_date.toInstant());
				
				EndorsementRequest er =req.getEndormentRequest()==null?null:req.getEndormentRequest();
				
				EserviceCommonDetails saveHuman = EserviceCommonDetails.builder()
						.requestReferenceNo(reference_no)
						.riskId(riskId)
						.originalRiskId(riskId)
						.customerReferenceNo(req.getCustomerReferenceNo())
						.productId(product_id)
						.productDesc(null)
						.occupationDesc(occupationData==null?null:occupationData.getOccupationName())
						.occupationType(pa.getOccupationId())
						.sectionId(sectionId)
						.sectionName(null)
						.companyId(req.getInsuranceId())
						.companyName(null)
						.branchCode(req.getBranchCode())
						.categoryId(req.getCategoryId())
						.entryDate(new Date())
						.createdBy(req.getCreatedBy())
						.status(StringUtils.isBlank(req.getStatus())?"Y":req.getStatus())
						.updatedBy(ecd ==null?req.getCreatedBy(): ecd.getUpdatedBy())
						.updatedDate(ecd==null?new Date():ecd.getUpdatedDate())
						.loginId(req.getCreatedBy())
						.applicationId("1")
						.brokerBranchCode(req.getBrokerBranchCode())
						.brokerCode(req.getBrokerCode())
						.customerId("")
						.benefitCoverMonth(null)
						.salaryPerAnnum(new BigDecimal(pa.getSalary()))
						.policyPeriod(policy_period.intValue())
						.exchangeRate(new BigDecimal(req.getExchangeRate()))
						.agencyCode(req.getAgencyCode())
						.policyStartDate(policy_start_date)
						.policyEndDate(policy_end_date)
						.havepromocode(StringUtils.isBlank(req.getHavepromocode())?"N":req.getHavepromocode())
						.customerName(pa.getPersonName())
						.dob(sdf.parse(pa.getDob()))
						.subUserType(req.getSubUsertype())	
						.agencyCode(req.getAgencyCode())
						.currency(req.getCurrency())
						.sumInsured(new BigDecimal(pa.getSalary()))
						.endorsementType(0)
						.industryId(req.getCategoryId())
						.sourceTypeId(StringUtils.isBlank(req.getSourceTypeId())?null:req.getSourceTypeId())
						.customerCode(StringUtils.isBlank(req.getCustomerCode())?null:req.getCustomerCode())
						.promocode("Y".equals(req.getHavepromocode())?req.getPromoCode():null)
						.endorsementDate(er==null?null:sdf.parse(er.getEndorsementDate()))
						.endorsementEffdate(er==null?null:sdf.parse(er.getEndorsementEffectiveDate()))
						.endorsementRemarks(er==null?null:er.getEndorsementRemarks())
						.endorsementType(er==null?null:Integer.valueOf(er.getEndorsementRemarks()))
						.endorsementTypeDesc(er==null?null:er.getEndorsementTypeDesc())
						.endtCount(er==null?null:new BigDecimal(er.getEndtCount()))
						.endtPrevPolicyNo(er==null?null:er.getEndtPrevPolicyNo())
						.endtPrevQuoteNo(er==null?null:er.getEndtPrevQuoteNo())
						.endtStatus(er==null?"N":er.getEndtStatus())
						.isFinyn(er==null?"N":er.getIsFinanceEndt())
						.build();

				
				ecdList.add(ecdRepository.save(saveHuman));
					
			}
			
			return ecdList;
				
			
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return null;
	}

	private void saveFireInfo(NonMotorOneTimeRequest req) {
		try {
			
			
			
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		
	}
	
	public void ms_human_details() {
		try {
			
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
	}

}
