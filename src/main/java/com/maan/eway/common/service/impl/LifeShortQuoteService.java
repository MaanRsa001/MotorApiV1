package com.maan.eway.common.service.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maan.eway.bean.BranchMaster;
import com.maan.eway.bean.EserviceCustomerDetails;
import com.maan.eway.bean.EserviceLifeDetails;
import com.maan.eway.bean.ListItemValue;
import com.maan.eway.bean.LoginMaster;
import com.maan.eway.bean.MsCustomerDetails;
import com.maan.eway.bean.MsLifeDetails;
import com.maan.eway.common.req.Illustration;
import com.maan.eway.common.req.ShortQuote;
import com.maan.eway.common.res.CommonRes;
import com.maan.eway.repository.BranchMasterRepository;
import com.maan.eway.repository.EserviceCustomerDetailsRepository;
import com.maan.eway.repository.EserviceLifeDetailsRepository;
import com.maan.eway.repository.ListItemValueRepository;
import com.maan.eway.repository.LoginMasterRepository;
import com.maan.eway.repository.MsCustomerDetailsRepository;
import com.maan.eway.repository.MsLifeDetailsRepository;
import com.maan.eway.req.life.Life;
import com.maan.eway.req.life.LifeDetail;
import com.maan.eway.req.life.QuoteDetail;

import jakarta.validation.Valid;

import com.maan.eway.error.Error;
@Service
public class LifeShortQuoteService {
	
	@Autowired
	private ListItemValueRepository listItemRepo;
	@Autowired
	private GenerateSeqNoServiceImpl genSeqNoService ;
	
	
	@Autowired
	private EserviceCustomerDetailsRepository eCustomerRepo;
	@Autowired
	private EserviceLifeDetailsRepository eLifeRepo;
	
	@Autowired
	private MsLifeDetailsRepository msLifeRepo;
	@Autowired
	private MsCustomerDetailsRepository msCustomerRepo;
	
	@Value(value = "${life.commonapi}")
	private String COMMON_API;
	@Value(value = "${life.motorapi}")
	private String MOTOR_API;

	public Map<String, Object> shortQuoteCalc(ShortQuote request,String tokens) {
		

		if(StringUtils.isBlank(request.getRequestReferenceNo())) {
			ListItemValue itemCode = listItemRepo.findByItemTypeAndItemCode("PRODUCT_SHORT_CODE", request.getProductID().toString()); 
			String refShortCode = itemCode.getItemValue();		 
			request.setRequestReferenceNo(refShortCode +"-" + genSeqNoService.generateRefNo()) ; // idf.format(new Date()) + random ;
		}
		HashMap<String, Object> onetime=null;
		try {
			
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", tokens.split(",")[0]);
			HttpEntity<Object> entityReq = new HttpEntity<>(request, headers);
			System.out.println(entityReq.getBody());
		   ResponseEntity<Object> response = restTemplate.postForEntity(MOTOR_API+"/onetime/life", entityReq, Object.class);
		   onetime= (HashMap<String, Object>) ((HashMap<String, Object>) response.getBody()).get("Result");
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			Map<String,Object> calcRequest=new HashMap<String, Object>();
			calcRequest.put("InsuranceId", request.getInsuranceID());
			calcRequest.put("BranchCode", request.getBranchCode());
			calcRequest.put("AgencyCode", request.getAgencyCode());
			calcRequest.put("SectionId", "");
			calcRequest.put("ProductId", request.getProductID());
			calcRequest.put("MSRefNo", onetime.get("msRefNo"));
			calcRequest.put("CdRefNo", onetime.get("cdRefNo"));
			calcRequest.put("VdRefNo", onetime.get("vdRefNo"));
			calcRequest.put("RequestReferenceNo", request.getRequestReferenceNo());
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			calcRequest.put("EffectiveDate", sdf.format(new Date()));
			calcRequest.put("PolicyEndDate", sdf.format(new Date()));
			calcRequest.put("VehicleId", "1");
			calcRequest.put("CoverModification", "N");
			
			
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", tokens.split(",")[0]);
			HttpEntity<Object> entityReq = new HttpEntity<>(calcRequest, headers);
			System.out.println(entityReq.getBody());
		   ResponseEntity<Object> response = restTemplate.postForEntity(COMMON_API+"/calculator/calc", entityReq, Object.class);
			return (HashMap<String, Object>) response.getBody();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Autowired
	private BranchMasterRepository ewayBranchRepo;
	
	@Autowired
	private LoginMasterRepository loginRepo;
	
	public Map<String, Object> converToQuote(Illustration request, String tokens) {
		try {
			String customerReferenceNo="CUST" +"-" + genSeqNoService.generateRefNo();
			
			MsCustomerDetails custHub = msCustomerRepo.findByCdRefno(request.getCdRefNo());
			MsLifeDetails lifeHub = msLifeRepo.findByVdRefno(request.getVdRefNo());
			
			ListItemValue itemCode = listItemRepo.findByItemTypeAndItemCode("PRODUCT_SHORT_CODE", lifeHub.getProductId().toString()); 
			String refShortCode = itemCode.getItemValue();		 
			String requestReferenceNo=refShortCode +"-" + genSeqNoService.generateRefNo() ;
			
			String loginId=request.getLoginId();
			if(!"1".equals(request.getApplicationId())) {
				BranchMaster b=ewayBranchRepo.findByBranchCodeAndCompanyIdAndStatusOrderByEffectiveDateEndDesc(request.getBranchCode(),lifeHub.getCompanyId(),"Y");
				loginId=b.getDirectBrokerId();
			}
			LoginMaster login = loginRepo.findByCompanyIdAndLoginId(lifeHub.getCompanyId(),loginId);
			EserviceCustomerDetails customer=EserviceCustomerDetails.builder()
					.companyId(lifeHub.getCompanyId())
					.isTaxExempted("N")
					.productId(lifeHub.getProductId())
					.occupation(custHub.getOccupation())
					.gender(custHub.getGender())
					.age(custHub.getAge())
					.idNumber("0")
					.policyHolderTypeid("1")
					.customerReferenceNo(customerReferenceNo)
					.mobileCode1(custHub.getMobileCode())
					.mobileNo1(custHub.getMobileNo())
					.email1(custHub.getEmail())
					.build();
			eCustomerRepo.save(customer);
			EserviceLifeDetails life=EserviceLifeDetails.builder()
					.requestReferenceNo(requestReferenceNo)
					.customerReferenceNo(customerReferenceNo)
					.riskId(1)
					.brokerBranchCode(request.getBranchCode())
					
					.vdRefno(lifeHub.getVdRefno())
					.cdRefno(request.getCdRefNo())
					.sumInsured(lifeHub.getSumInsured()) 
					.companyId(lifeHub.getCompanyId())					
					.currency(lifeHub.getCurrency())
					.exchangeRate(lifeHub.getExchangeRate())
					.entryDate(new Date())
					.havepromocode(lifeHub.getHavepromocode())
					
					.msRefno(request.getMsRefNo())
					.periodOfInsurance(lifeHub.getPeriodOfInsurance())
					.sectionId(lifeHub.getSectionId().toString())
					.status("Y")
					
					.loginId(loginId)
					.brokerCode(login.getOaCode())
					.createdBy("1".equals(request.getApplicationId())?loginId:request.getApplicationId())
					.agencyCode(login.getAgencyCode())
					.applicationId(request.getApplicationId())
					.build();
			eLifeRepo.save(life);
			
			 Map<String, Object> retu=new HashMap<String,Object>();
					 retu.put("CustomerReferenceNo", customerReferenceNo);
					 retu.put("RequestReferenceNo", requestReferenceNo);
					 return retu;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Map<String, Object> pushQuote(@Valid Life request) {
		try {
			if(StringUtils.isBlank(request.getQuoteDetail().getRequestReferenceNo())) {
				ListItemValue itemCode = listItemRepo.findByItemTypeAndItemCode("PRODUCT_SHORT_CODE",request.getQuoteDetail().getProductId().toString()); 
				String refShortCode = itemCode.getItemValue();		 
				String requestReferenceNo=refShortCode +"-" + genSeqNoService.generateRefNo() ;
				request.getQuoteDetail().setRequestReferenceNo(requestReferenceNo);
			}
			QuoteDetail quote = request.getQuoteDetail();
			LifeDetail lifeHub = request.getLifeDetail();
			LoginMaster login = loginRepo.findByCompanyIdAndLoginId(quote.getCompanyId(),quote.getLoginId());
			BigDecimal sumInsured=(lifeHub.getSumInsured()==null?BigDecimal.ZERO:lifeHub.getSumInsured());
			EserviceLifeDetails life=EserviceLifeDetails.builder()
					.requestReferenceNo(quote.getRequestReferenceNo())
					.customerReferenceNo(request.getCustomer().getCustomerId().get(0))
					.riskId(1)
					.brokerBranchCode(quote.getBrokerBranch())					
					.vdRefno(null)
					.cdRefno(null)
					.msRefno(null)
					.sumInsured(sumInsured) 
					.companyId(quote.getCompanyId())					
					.currency(quote.getCurrency())
					.exchangeRate(new BigDecimal(quote.getExchangeRate()))
					.entryDate(new Date())
					.havepromocode(quote.getHavePromoCode()) 
					.periodOfInsurance(quote.getPolicyPeriod())
					.sectionId(quote.getSectionId()==null?"0":quote.getSectionId())
					.status("Y")					
					.loginId(quote.getLoginId())
					.brokerCode(login.getOaCode())
					.createdBy("1".equals(quote.getApplicationId())?quote.getLoginId():quote.getApplicationId())
					.agencyCode(login.getAgencyCode())
					.applicationId(quote.getApplicationId())
					.bdmCode(quote.getBDMCode())
					.brokerBranchName(null)
					.companyName(null)					
					.policyEndDate(quote.getPolicyEndDate())
					.policyStartDate(quote.getPolicyStartDate())
					.productDesc(null)
					.quoteNo(quote.getQuoteNo())
					.sectionId(StringUtils.isBlank(quote.getSectionId())?"0":quote.getSectionId())
					.sourceType(quote.getSourceType())
					.subUserType(quote.getSubUserType())
					.sumInsuredLc(sumInsured.multiply(new BigDecimal(quote.getExchangeRate()),MathContext.DECIMAL32))					
					.build();
			eLifeRepo.save(life);
			
			Map<String,Object> calcRequest=new HashMap<String, Object>();
			calcRequest.put("RequestReferenceNo", quote.getRequestReferenceNo());
			return calcRequest;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

	public  CommonRes createQuote(@Valid Life request, String tokens) {
		try {
			
			if(StringUtils.isBlank(request.getQuoteDetail().getRequestReferenceNo())) {
				ListItemValue itemCode = listItemRepo.findByItemTypeAndItemCode("PRODUCT_SHORT_CODE",request.getQuoteDetail().getProductId().toString()); 
				String refShortCode = itemCode.getItemValue();		 
				String requestReferenceNo=refShortCode +"-" + genSeqNoService.generateRefNo() ;
				request.getQuoteDetail().setRequestReferenceNo(requestReferenceNo);
			}
			QuoteDetail quote = request.getQuoteDetail();
			LifeDetail life = request.getLifeDetail();
			String loginId=quote.getLoginId();
			if(!"1".equals(quote.getApplicationId())) {
				BranchMaster b=ewayBranchRepo.findByBranchCodeAndCompanyIdAndStatusOrderByEffectiveDateEndDesc(quote.getBranchCode(),quote.getCompanyId(),"Y");
				loginId=b.getDirectBrokerId();
				request.getQuoteDetail().setLoginId(loginId);

			}
			LoginMaster login = loginRepo.findByCompanyIdAndLoginId(quote.getCompanyId(),loginId);
			EserviceCustomerDetails eserviceCust=null;
			if(request.getCustomer()!=null && !request.getCustomer().getCustomerId().isEmpty()) {
				 eserviceCust = eCustomerRepo.findByCustomerReferenceNo(request.getCustomer().getCustomerId().get(0));
			}
			
					
			Gson g=new Gson();
			//Save Raw
			try {
				
				RestTemplate restTemplate = new RestTemplate();
				HttpHeaders headers = new HttpHeaders();
				headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
				headers.setContentType(MediaType.APPLICATION_JSON);
				headers.set("Authorization", tokens.split(",")[0]);
				HttpEntity<Object> entityReq = new HttpEntity<>(request, headers);
				System.out.println(entityReq.getBody());
			   ResponseEntity<CommonRes> response = restTemplate.postForEntity(MOTOR_API+"shortquote/pushQuote", entityReq, CommonRes.class);
			   
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			
			//Save UW
			Double uwLoading=0D;
			if(request.getUwQuestions()!=null && request.getUwQuestions().size()>0) {
				try {
					request.getUwQuestions().stream().forEach(u -> u.setCompanyId(request.getQuoteDetail().getCompanyId()));
					request.getUwQuestions().stream().forEach(u -> u.setProductId(request.getQuoteDetail().getProductId().toString()));
					request.getUwQuestions().stream().forEach(u -> u.setRequestReferenceNo(quote.getRequestReferenceNo()));
					
					uwLoading=request.getUwQuestions().stream().mapToDouble(t-> Double.parseDouble(t.getLoadingPercent())).sum();
					RestTemplate restTemplate = new RestTemplate();
					HttpHeaders headers = new HttpHeaders();
					headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
					headers.setContentType(MediaType.APPLICATION_JSON);
					headers.set("Authorization", tokens.split(",")[0]);
					HttpEntity<Object> entityReq = new HttpEntity<>(request.getUwQuestions(), headers);
					System.out.println(entityReq.getBody());
				   ResponseEntity<Object> response = restTemplate.postForEntity(COMMON_API+"api/saveuwquestions", entityReq, Object.class);
				   HashMap<String, Object> onetime = (HashMap<String, Object>) ((HashMap<String, Object>) response.getBody()).get("Result");
				}catch (Exception e) {
					e.printStackTrace();
				}
			}

			//Save Onetime
			HashMap<String, Object> onetime=null;
			try {
				
				ShortQuote shortQ=ShortQuote.builder()
						.agencyCode(login.getAgencyCode())
						.applicationID(quote.getApplicationId())
						.bdmCode(quote.getBDMCode())
						.branchCode(quote.getBranchCode())
						.brokerBranchCode(quote.getBrokerBranch())
						.brokerCode(login.getOaCode())
						.businessType(eserviceCust.getBusinessType())
						.businessTypeDesc(eserviceCust.getBusinessTypeDesc())
						.cityCode(eserviceCust.getCityCode().toString())
						.cityName(eserviceCust.getCityName())
						.createdBy("1".equals(quote.getApplicationId())?loginId:quote.getApplicationId())
						.currency(quote.getCurrency())
						.customerName(eserviceCust.getClientName())
						.dateOfBirth(eserviceCust.getDobOrRegDate())
						.emailId(eserviceCust.getEmail1())
						.uwLoading(uwLoading)
						.taxExemptedId(eserviceCust.getTaxExemptedId())
						.sumInsuredLc(null)// No need.
						.sumInsured(life.getSumInsured()==null?BigDecimal.ZERO:life.getSumInsured())
						.stateName(eserviceCust.getStateName())
						.stateCode(eserviceCust.getStateCode().toString())
						.sourceType(quote.getSourceType())
						.requestReferenceNo(quote.getRequestReferenceNo())
						.regionCode(eserviceCust.getRegionCode())					
						.productID(quote.getProductId())
						.policyTerm(life.getPolicyTerm())
						.policyHolderTypeid(eserviceCust.getPolicyHolderTypeid())
						.policyHolderType(eserviceCust.getPolicyHolderType())
						.placeOfBirth(eserviceCust.getPlaceOfBirth())
						.periodOfInsurance(life.getPolicyTerm())
						.paymentMode(life.getPremiumPayingTerm())
						.payingTerm(life.getPaymentTerm())
						.occupationDesc(eserviceCust.getOccupationDesc())
						.occupation(eserviceCust.getOccupation())
						.nationality(eserviceCust.getNationality())
						.mobileNo(eserviceCust.getMobileNo1())
						.mobileCode(eserviceCust.getMobileCode1() )
						.loginID(loginId)
						.isTaxExempted(eserviceCust.getIsTaxExempted())
						.insuranceID(eserviceCust.getCompanyId())
						.idTypeDesc(eserviceCust.getIdTypeDesc())
						.idNumber(eserviceCust.getIdNumber())
						.havepromocode(quote.getHavePromoCode())
						.promocode(quote.getPromoCode())
						.genderDesc(eserviceCust.getGenderDesc())
						.gender(eserviceCust.getGender())
						.exchangeRate(quote.getExchangeRate())					
						.build();
				
				
				
				
				RestTemplate restTemplate = new RestTemplate();
				HttpHeaders headers = new HttpHeaders();
				headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
				headers.setContentType(MediaType.APPLICATION_JSON);
				headers.set("Authorization", tokens.split(",")[0]);
				HttpEntity<Object> entityReq = new HttpEntity<>(shortQ, headers);
				System.out.println(entityReq.getBody());
			   ResponseEntity<CommonRes> response = restTemplate.postForEntity(MOTOR_API+"onetime/life", entityReq, CommonRes.class);
			   return response.getBody();
			}catch(HttpClientErrorException e) {
				String responseStr= e.getResponseBodyAsString();
				HashMap<String, Object> mp=	 g.fromJson(responseStr, new TypeToken<HashMap<String, Object>>() {}.getType());
				CommonRes json = g.fromJson(responseStr, CommonRes.class);
				json.setErrorMessage((List<Error>) mp.get("ErrorMessage"));
				return json;
				//e.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
				
			}
			
			
			try {
				Map<String,Object> calcRequest=new HashMap<String, Object>();
				calcRequest.put("InsuranceId", quote.getCompanyId());
				calcRequest.put("BranchCode", quote.getBranchCode());
				calcRequest.put("AgencyCode", login.getAgencyCode());
				calcRequest.put("SectionId", "");
				calcRequest.put("ProductId", quote.getProductId());
				calcRequest.put("MSRefNo", onetime.get("msRefNo"));
				calcRequest.put("CdRefNo", onetime.get("cdRefNo"));
				calcRequest.put("VdRefNo", onetime.get("vdRefNo"));
				calcRequest.put("RequestReferenceNo", quote.getRequestReferenceNo());
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				calcRequest.put("EffectiveDate", sdf.format(new Date()));
				calcRequest.put("PolicyEndDate", sdf.format(new Date()));
				calcRequest.put("VehicleId", "1");
				calcRequest.put("CoverModification", "N");
				
				
				RestTemplate restTemplate = new RestTemplate();
				HttpHeaders headers = new HttpHeaders();
				headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
				headers.setContentType(MediaType.APPLICATION_JSON);
				headers.set("Authorization", tokens.split(",")[0]);
				HttpEntity<Object> entityReq = new HttpEntity<>(calcRequest, headers);
				System.out.println(entityReq.getBody());
			   ResponseEntity<CommonRes> response = restTemplate.postForEntity(COMMON_API+"/calculator/calc", entityReq, CommonRes.class);
				return response.getBody();
			}catch(HttpClientErrorException e) {
				String responseStr= e.getResponseBodyAsString();
				HashMap<String, Object> mp=	 g.fromJson(responseStr, new TypeToken<HashMap<String, Object>>() {}.getType());
				CommonRes json = g.fromJson(responseStr, CommonRes.class);
				json.setErrorMessage((List<Error>) mp.get("ErrorMessage"));
				return json;
				//e.printStackTrace();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	

}
