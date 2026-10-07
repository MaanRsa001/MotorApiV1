package com.maan.eway.service.impl;

import java.io.StringWriter;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maan.eway.bean.TiraErrorHistory;
import com.maan.eway.config.DigitalSignatureGenerator;
import com.maan.eway.repository.TiraErrorHistoryRepository;
import com.maan.eway.req.MotorVehicleInfoGetReq;
import com.maan.eway.req.MotorVerificationReq;
import com.maan.eway.req.MtpPolicyReq;
import com.maan.eway.req.PolicyResponse;
import com.maan.eway.req.TiraMsg;
import com.maan.eway.req.ValidateVrnRequest;
import com.maan.eway.req.VehicleValidationResponse;
import com.maan.eway.req.acknowledge.TiraM;
import com.maan.eway.req.acknowledge.TiraMsgAcknowlege;
import com.maan.eway.req.acknowledge.TiraMsgAcknowlegeFleet;
import com.maan.eway.req.fleet.FleetMotorCoverNoteRefReq;
import com.maan.eway.req.fleet.TiraMsgVehiclePushFleet;
import com.maan.eway.req.push.CoverNoteRefReq;
import com.maan.eway.req.push.MotorCoverNoteRefReq;
import com.maan.eway.req.push.TiraMsgCoverPush;
import com.maan.eway.req.push.TiraMsgVehiclePush;
import com.maan.eway.req.verification.CoverNoteVerificationReq;
import com.maan.eway.req.verification.TiraMsgVehicleVerification;
import com.maan.eway.res.CommonRes;
import com.maan.eway.res.MotorTiraMsgRes;
import com.maan.eway.res.MotorVehicleInfoRes;
import com.maan.eway.res.NonMotorTiraMsgRes;
import com.maan.eway.res.SuccessRes;
import com.maan.eway.res.verification.MotorTiraMsgVerificationRes;
import com.maan.eway.service.ExternalApiCallService;
import com.maan.eway.service.RegulatoryInfoService;



@Service
public class ExternalApiCallServiceImpl implements ExternalApiCallService {
	 
	@Value("${MotorVerificationV1Link}")
	private String motorVerificationV1Link ;
	
	@Value("${HeadearAdd1Key}")
	private String headearAdd1Key ;
	
	@Value("${HeadearAdd1Value}")
	private String headearAdd1Value ;
	
	@Value("${AuthApiKey}")
	private String authApiKey ;
	
	@Value("${AuthApiValue}")
	private String authApiValue ;
	
	@Value("${AuthApiCode}")
	private String authApiCode ;
	
	@Value("${MotorPostingV1Link}")
	private String motorpostingV1Link ;
	
	@Value("${CoverNoteVerificationLink}")
	private String coverNoteVerificationLink;
	
	@Value("${NonMotorPostingV1Link}")
	private String nonMotorpostingV1Link ;
	
	@Value("${FleetMotorPostingV1Link}")
	private String motorFleetpostingV1Link ;
	
	@Value(value = "${mptVehicleInfo}")
	private String mptVehicleInfo;
	
	@Value(value = "${mptVehicleDetails}")
	private String mptVehicleDetails;
	
	@Value(value = "${PremiaPushLink}")
	private String premiaPushLink;
	
	@Autowired
	private RegulatoryInfoService motorInfoService ;
	
	@Autowired	
	private DigitalSignatureGenerator signature;

	private Logger log=LogManager.getLogger(ExternalApiCallServiceImpl.class);
	
	@Autowired
	private RegulatoryResponseService regulatoryService;
	
	@Autowired
	private TiraErrorHistoryRepository tiraErrorHistoryRepo;
	
	@Override
	public MotorTiraMsgRes getSampleData(TiraMsg req) {
		String xmlString = "";
		MotorTiraMsgRes tiraRes =null;
		{
			try {
				StringWriter sw = new StringWriter();

				MotorVerificationReq motorVerificationReq = req.getMotorVerificationReq();				
				JAXBContext newInstancev1 = JAXBContext.newInstance(MotorVerificationReq.class);
				Marshaller createMarshallerv1 = newInstancev1.createMarshaller();
				createMarshallerv1.setProperty("com.sun.xml.bind.xmlDeclaration", false);
				createMarshallerv1.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				createMarshallerv1.marshal(motorVerificationReq, sw);
				String motorVerification = sw.toString();
				motorVerification=motorVerification.replaceAll(">[\\s\r\n]*<", "><");//("[\r\n]+", "");

				String collectMsgSignature = signature.collectMsgSignature(motorVerification);
				req.setMsgSignature(collectMsgSignature);

				sw.flush();
				sw = new StringWriter();
				JAXBContext newInstance = JAXBContext.newInstance(TiraMsg.class);
				Marshaller createMarshaller = newInstance.createMarshaller();
				createMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				createMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", false);
				createMarshaller.marshal(req, sw);
				xmlString = sw.toString().replaceAll(">[\\s\r\n]*<", "><");//("[\r\n]+", "");

				System.out.println("Request: "+xmlString); 

			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}


		try {
			RestTemplate restTemplate = new RestTemplate();
			String url = motorVerificationV1Link ;
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_XML }));
			headers.setContentType(MediaType.APPLICATION_XML);
			headers.set(headearAdd1Key, headearAdd1Value);
			headers.add(authApiKey, authApiValue);		
			HttpEntity<Object> entityReq = new HttpEntity<>(xmlString, headers);		 
			ResponseEntity<MotorTiraMsgRes> response = restTemplate.postForEntity(url, entityReq, MotorTiraMsgRes.class);
			tiraRes = response.getBody() ;
			// Save Motor Info
			//SuccessRes res  = motorInfoService.saveMotorInfo(req , tiraRes ,null) ;

			//System.out.println(res.getResponse());
			System.out.println(""+tiraRes);
			return tiraRes;

		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null ;
		}finally {
			String registrationNo=req.getMotorVerificationReq().getVerificationDtl().getMotorRegistrationNumber();
			String chassisNo=req.getMotorVerificationReq().getVerificationDtl().getMotorChassisNumber();
			regulatoryService.saveRequestAndResponse(xmlString, tiraRes.toString(), "MotorVerification/"+(StringUtils.isBlank(registrationNo)?chassisNo:registrationNo));
			regulatoryService.checkBodyType(req,tiraRes);
			try {
				StringWriter sww = new StringWriter();
				JAXBContext newInstance = JAXBContext.newInstance(MotorTiraMsgRes.class);
				Marshaller createMarshaller = newInstance.createMarshaller();
				createMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				createMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", false);
				createMarshaller.marshal(tiraRes, sww);

				TiraErrorHistory h=TiraErrorHistory.builder()
						.entryDate(new Date())
						.apiDescription("Motor Verification")
						.reqChassisNumber(chassisNo)
						.reqRegNumber(registrationNo)
						.requestHeaders(headearAdd1Value)
						.requestMethod("POST")
						.requestString(xmlString)
						.responseString(sww.toString()) 
						.responseStatus(tiraRes!=null? tiraRes.getMotorVerificationRes().getVerificationHdr().getResponseStatusCode():"SOMERR")
						.responseStatusDesc(tiraRes!=null? tiraRes.getMotorVerificationRes().getVerificationHdr().getResponseStatusDesc():"SOMERR")
						.build();

				tiraErrorHistoryRepo.save(h);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public MotorTiraMsgRes pushVehicleInfo(TiraMsgVehiclePush req,String tokens) {

		String xmlString = "";
		MotorTiraMsgRes tiraRes =null;
		{
			try {
				StringWriter sw = new StringWriter();
			    MotorCoverNoteRefReq coverNoteRefReqBean = req.getMotorCoverNoteRefReq();
				
				JAXBContext newInstancev1 = JAXBContext.newInstance(MotorCoverNoteRefReq.class);
				Marshaller createMarshallerv1 = newInstancev1.createMarshaller();
				createMarshallerv1.setProperty("com.sun.xml.bind.xmlDeclaration", false);
				//marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);


				createMarshallerv1.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				createMarshallerv1.marshal(coverNoteRefReqBean, sw);
				String motorVerification = sw.toString();
				motorVerification=motorVerification.replaceAll(">[\\s\r\n]*<", "><");//("[\r\n]+", ""); //
				
				String collectMsgSignature = signature.collectMsgSignature(motorVerification);
				req.setMsgSignature(collectMsgSignature);
				
				sw.flush();
				sw = new StringWriter();
				JAXBContext newInstance = JAXBContext.newInstance(TiraMsgVehiclePush.class);
				Marshaller createMarshaller = newInstance.createMarshaller();
				createMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				createMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", false);
				createMarshaller.marshal(req, sw);
				 xmlString = sw.toString().replaceAll(">[\\s\r\n]*<", "><");//("[\r\n]+", "");

				System.out.println("Request: "+xmlString); 
				
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
	 
	 try {
			RestTemplate restTemplate = new RestTemplate();
			String url = motorpostingV1Link ;
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_XML }));
			headers.setContentType(MediaType.APPLICATION_XML);
			headers.set(headearAdd1Key, headearAdd1Value);
			headers.add(authApiKey, authApiValue);
		
			HttpEntity<Object> entityReq = new HttpEntity<>(xmlString, headers);
		 
			ResponseEntity<MotorTiraMsgRes> response = restTemplate.postForEntity(url, entityReq, MotorTiraMsgRes.class);
			tiraRes = response.getBody() ;
			System.out.println("REsponse");
			System.out.println(""+tiraRes);
			
			regulatoryService.savePostResponseInTables(req,tiraRes,"/covernote/non-life/motor/v2/request",xmlString,tokens);
			return tiraRes;
		 
	 } catch (Exception e) {
		 e.printStackTrace();
		 log.info("Exception is ---> " + e.getMessage());
		 
		 //Logs
		 String registrationNo=req.getMotorCoverNoteRefReq().getCoverNoteDtlBean().getMotorDtlBean().getRegistrationNumber(); // ().getVerificationDtl().getMotorRegistrationNumber();
		 String chassisNo=req.getMotorCoverNoteRefReq().getCoverNoteDtlBean().getMotorDtlBean().getChassisNumber();
		 regulatoryService.saveRequestAndResponse(xmlString, (tiraRes==null?"RESPONSE ERROR":tiraRes.toString()), ("PushPolicy/"+(StringUtils.isNotBlank(chassisNo)?chassisNo:registrationNo)));
		 
		 return null ;
	 } 	
	
		
	}

	@Override
	public MotorTiraMsgVerificationRes coverVerification(TiraMsgVehicleVerification req, String tokens,MotorVehicleInfoGetReq request) {
		String xmlString = "";
		MotorTiraMsgVerificationRes tiraRes =null;
		{
			try {
				StringWriter sw = new StringWriter();
			    CoverNoteVerificationReq coverNoteRefReqBean = req.getCoverNoteVerificationReq();				
				JAXBContext newInstancev1 = JAXBContext.newInstance(CoverNoteVerificationReq.class);
				Marshaller createMarshallerv1 = newInstancev1.createMarshaller();
				createMarshallerv1.setProperty("com.sun.xml.bind.xmlDeclaration", false);
				createMarshallerv1.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				createMarshallerv1.marshal(coverNoteRefReqBean, sw);
				String motorVerification = sw.toString();
				motorVerification=motorVerification.replaceAll(">[\\s\r\n]*<", "><");//("[\r\n]+", ""); //
				
				String collectMsgSignature = signature.collectMsgSignature(motorVerification);
				req.setMsgSignature(collectMsgSignature);
				
				sw.flush();
				sw = new StringWriter();
				JAXBContext newInstance = JAXBContext.newInstance(TiraMsgVehicleVerification.class);
				Marshaller createMarshaller = newInstance.createMarshaller();
				createMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				createMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", false);
				createMarshaller.marshal(req, sw);
				 xmlString = sw.toString().replaceAll(">[\\s\r\n]*<", "><");//("[\r\n]+", "");

				System.out.println("Request: "+xmlString); 
				
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		
	 try {
			RestTemplate restTemplate = new RestTemplateBuilder()
			        .setConnectTimeout(Duration.ofSeconds(30))
			        .setReadTimeout(Duration.ofSeconds(30))
			        .build();

			String url = coverNoteVerificationLink ;
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_XML }));
			//headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setContentType(MediaType.APPLICATION_XML);
			headers.set(headearAdd1Key, headearAdd1Value);
			//headers.add("x-api-key", authApiCode);		
			//headers.add("ClientKey", "0*fZ^y5@G2b!pgGQAD9SnP");
			headers.add(authApiKey, authApiValue);
		
			HttpEntity<Object> entityReq = new HttpEntity<>(xmlString, headers);
		 
			ResponseEntity<MotorTiraMsgVerificationRes> response = restTemplate.postForEntity(url, entityReq, MotorTiraMsgVerificationRes.class);
			tiraRes = response.getBody() ;
			System.out.println("REsponse");
			System.out.println("---------------------------------------------------");
			System.out.println(""+response.getBody() );
			System.out.println("---------------------------------------------------");
			if(request!=null) {				 
				MotorVehicleInfoRes saveVerification = motorInfoService.saveVerification(response.getBody(),req,tokens,request);
				//return saveVerification;
			}
			return tiraRes;
		 
	 } catch (Exception e) {
		 e.printStackTrace();
		 log.info("Exception is ---> " + e.getMessage());
		 return null ;
	 }finally {
		 String registrationNo=req.getCoverNoteVerificationReq().getVerificationDtl().getMotorRegistrationNumber();
		 String chassisNo=req.getCoverNoteVerificationReq().getVerificationDtl().getMotorChassisNumber();
		 regulatoryService.saveRequestAndResponse(xmlString, tiraRes==null?"RESPONSE ERROR":tiraRes.toString(), "CoverVerifcation/"+(StringUtils.isBlank(registrationNo)?chassisNo:registrationNo));
		 
		 try {
			 StringWriter sww = new StringWriter();
			 JAXBContext newInstance = JAXBContext.newInstance(MotorTiraMsgVerificationRes.class);
			 Marshaller createMarshaller = newInstance.createMarshaller();
			 createMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			 createMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", false);
			 createMarshaller.marshal(tiraRes, sww);
		 
		 TiraErrorHistory h=TiraErrorHistory.builder()
					.entryDate(new Date())
					.apiDescription("Cover Verification")
					.reqChassisNumber(chassisNo)
					.reqRegNumber(registrationNo)
					.requestHeaders(headearAdd1Value)
					.requestMethod("POST")
					.requestString(xmlString)
					.responseString(tiraRes==null?"RESPONSE ERROR":sww.toString())
					.responseStatus(tiraRes!=null? tiraRes.getCoverNoteVerifications().getCoverNoteHdrList().getResponseStatusCode():"SOMERR")
					.responseStatusDesc(tiraRes!=null? tiraRes.getCoverNoteVerifications().getCoverNoteHdrList().getResponseStatusDesc():"SOMERR")
					.build();
			
			tiraErrorHistoryRepo.save(h);
		 }catch (Exception e) {
			 e.printStackTrace();
		}
	}
	 
		
	
		
	
	}

	@Override
	public TiraMsgAcknowlege saveAcknowledge(TiraMsgAcknowlege req,String tokens) {
		try {
			TiraMsgAcknowlege accepted=regulatoryService.updateAcknowlegment(req,tokens);
			 return accepted;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public NonMotorTiraMsgRes pushNonMotorInfo(TiraMsgCoverPush req, String tokens) {

		String xmlString = "";
		NonMotorTiraMsgRes tiraRes =null;
		{
			try {
				StringWriter sw = new StringWriter();
			    CoverNoteRefReq coverNoteRefReqBean = req.getCoverNoteRefReq();
				
				JAXBContext newInstancev1 = JAXBContext.newInstance(CoverNoteRefReq.class);
				Marshaller createMarshallerv1 = newInstancev1.createMarshaller();
				createMarshallerv1.setProperty("com.sun.xml.bind.xmlDeclaration", false);

				createMarshallerv1.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				createMarshallerv1.marshal(coverNoteRefReqBean, sw);
				String motorVerification = sw.toString();
				motorVerification=motorVerification.replaceAll(">[\\s\r\n]*<", "><");//("[\r\n]+", ""); //
				
				String collectMsgSignature = signature.collectMsgSignature(motorVerification);
				req.setMsgSignature(collectMsgSignature);
				
				sw.flush();
				sw = new StringWriter();
				JAXBContext newInstance = JAXBContext.newInstance(TiraMsgCoverPush.class);
				Marshaller createMarshaller = newInstance.createMarshaller();
				createMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				createMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", false);
				createMarshaller.marshal(req, sw);
				 xmlString = sw.toString().replaceAll(">[\\s\r\n]*<", "><");//("[\r\n]+", "");

				System.out.println("Request: "+xmlString); 
				
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
	 
	 try {
			RestTemplate restTemplate = new RestTemplate();
			String url = nonMotorpostingV1Link ;
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_XML }));
			headers.setContentType(MediaType.APPLICATION_XML);
			headers.set(headearAdd1Key, headearAdd1Value);
			headers.add(authApiKey, authApiValue);
		
			HttpEntity<Object> entityReq = new HttpEntity<>(xmlString, headers);
		 
			ResponseEntity<NonMotorTiraMsgRes> response = restTemplate.postForEntity(url, entityReq, NonMotorTiraMsgRes.class);
			tiraRes = response.getBody() ;
			System.out.println("REsponse");
			System.out.println(""+tiraRes);
			
			regulatoryService.savePostResponseInTablesNonMotor(req,tiraRes,"/covernote/non-life/other/v2/request",xmlString);
			return tiraRes;
		 
	 } catch (Exception e) {
		 e.printStackTrace();
		 log.info("Exception is ---> " + e.getMessage());
		 regulatoryService.savePostResponseInTablesNonMotor(req,tiraRes,"/covernote/non-life/other/v2/request",xmlString);
		 //Logs
		/* String registrationNo=req.getCoverNoteRefReq().getCoverNoteDtlBean().getMotorDtlBean().getRegistrationNumber(); // ().getVerificationDtl().getMotorRegistrationNumber();
		 String chassisNo=req.getCoverNoteRefReq().getCoverNoteDtlBean().getMotorDtlBean().getChassisNumber();
		 regulatoryService.saveRequestAndResponse(xmlString, tiraRes==null?"RESPONSE ERROR":tiraRes.toString(), "PushPolicy/"+(StringUtils.isNotBlank(chassisNo)?chassisNo:registrationNo));
		 */
		 return null ;
	 } 	
	
		
	}

	
	
	@Override
	public TiraMsgAcknowlege saveAcknowledgeForNonMotor(TiraMsgAcknowlege req, String tokens) {
		try {
			TiraMsgAcknowlege accepted=regulatoryService.updateAcknowlegmentForNonMotor(req,tokens);
			 return accepted;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;	
	}

	@Override
	public MotorTiraMsgRes pushVehicleInfoFleet(TiraMsgVehiclePushFleet req, String tokens) {


		String xmlString = "";
		MotorTiraMsgRes tiraRes =null;
		{
			try {
				StringWriter sw = new StringWriter();
			    FleetMotorCoverNoteRefReq coverNoteRefReqBean = req.getMotorCoverNoteRefReq();
				
				JAXBContext newInstancev1 = JAXBContext.newInstance(FleetMotorCoverNoteRefReq.class);
				Marshaller createMarshallerv1 = newInstancev1.createMarshaller();
				createMarshallerv1.setProperty("com.sun.xml.bind.xmlDeclaration", false);

				createMarshallerv1.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				createMarshallerv1.marshal(coverNoteRefReqBean, sw);
				String motorVerification = sw.toString();
				motorVerification=motorVerification.replaceAll(">[\\s\r\n]*<", "><");//("[\r\n]+", ""); //
				
				String collectMsgSignature = signature.collectMsgSignature(motorVerification);
				req.setMsgSignature(collectMsgSignature);
				
				sw.flush();
				sw = new StringWriter();
				JAXBContext newInstance = JAXBContext.newInstance(TiraMsgVehiclePushFleet.class);
				Marshaller createMarshaller = newInstance.createMarshaller();
				createMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				createMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", false);
				createMarshaller.marshal(req, sw);
				 xmlString = sw.toString().replaceAll(">[\\s\r\n]*<", "><");//("[\r\n]+", "");

				System.out.println("Request: "+xmlString); 
				
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
	
	 try {
			RestTemplate restTemplate = new RestTemplate();
			String url = motorFleetpostingV1Link ;
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_XML }));
			headers.setContentType(MediaType.APPLICATION_XML);
			headers.set(headearAdd1Key, headearAdd1Value);
			headers.add(authApiKey, authApiValue);
		
			HttpEntity<Object> entityReq = new HttpEntity<>(xmlString, headers);
		 
			ResponseEntity<MotorTiraMsgRes> response = restTemplate.postForEntity(url, entityReq, MotorTiraMsgRes.class);
			tiraRes = response.getBody() ;
			System.out.println("REsponse");
			System.out.println(""+tiraRes);
			
			regulatoryService.savePostResponseInTables(req,tiraRes,"/covernote/non-life/motor/v2/requestfleet",xmlString);
			return tiraRes;
		 
	 } catch (Exception e) {
		 e.printStackTrace();
		 log.info("Exception is ---> " + e.getMessage());
		 
		 //Logs
		 String registrationNo=""; 
		 String chassisNo="";
		 regulatoryService.saveRequestAndResponse(xmlString, tiraRes==null?"RESPONSE ERROR":tiraRes.toString(), "PushPolicy/"+(StringUtils.isNotBlank(chassisNo)?chassisNo:registrationNo));
		
		
	 } 	
	 return null ;
		
	
	}

	@Override
	public TiraM saveAcknowledgeForFleetMotor(TiraMsgAcknowlegeFleet req, String tokens) {

		try {
			TiraM accepted=regulatoryService.updateAcknowlegmentFleet(req);
			 return accepted;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	
		
	}

	@Override
	public SuccessRes callTiraIntegeration(TiraFrameReqCall req, String tokens) {
		SuccessRes res =new SuccessRes();
		try {
			regulatoryService.premiaIntegration(req.getQuoteNo(),tokens,premiaPushLink);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	@Override
	public CompletableFuture<VehicleValidationResponse> callMPTVehicleInfo(
	        MotorVehicleInfoGetReq req1,
	        String token) {

	    try {
	        ValidateVrnRequest req = new ValidateVrnRequest();
	        req.setAssessmentType("M");
	        req.setNumberPlate(req1.getReqRegNumber());

	        RestTemplate restTemplate = new RestTemplate();

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

	        token = token.replaceFirst("^Bearer\\s+", "");
	        headers.setBearerAuth(token.split(",")[0]);

	        HttpEntity<ValidateVrnRequest> entity = new HttpEntity<>(req, headers);

	        ResponseEntity<CommonRes> responseEntity =
	                restTemplate.exchange(
	                        mptVehicleInfo,
	                        HttpMethod.POST,
	                        entity,
	                        CommonRes.class);

	        CommonRes body = responseEntity.getBody();

	        if (body != null) {
	            VehicleValidationResponse response;

	            if (body.getCommonResponse() != null) {
	                ObjectMapper mapper = new ObjectMapper();
	                response = mapper.convertValue(body.getCommonResponse(), VehicleValidationResponse.class);
	            } else {
	                response = new VehicleValidationResponse();
	            }

	            if (Boolean.TRUE.equals(body.getIsError())) {
	                response.setIsError(true);
	                response.setErrorMessage(body.getErrorMessage());
	                log.error("MPT_VEHICLE_INFO_ERROR | RegNo={} | ErrorCode={} | Errors={}",
	                        req1.getReqRegNumber(), body.getErroCode(), body.getErrorMessage());
	            }

	            return CompletableFuture.completedFuture(response);
	        }

	    } catch (Exception e) {
	        log.error("MPT_VEHICLE_INFO_EXCEPTION | RegNo={}", req1.getReqRegNumber(), e);
	    }

	    return CompletableFuture.completedFuture(null);
	}
	
	@Override
	public CompletableFuture<PolicyResponse> callMPTVehiclePolicyDetails(MotorVehicleInfoGetReq req1, String token) {
		try {
			ResponseEntity<PolicyResponse> res = null;
			MtpPolicyReq req = new MtpPolicyReq();
			req.setVrn(req1.getReqRegNumber());
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders header = new HttpHeaders();
			header.setContentType(MediaType.APPLICATION_JSON);
			header.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			token = token.replaceFirst("^Bearer\\s+", "");
			String token1 = token.split(",")[0];
			header.setBearerAuth(token1);
			System.out.println("URL    : " + mptVehicleDetails);
			System.out.println("Token  : " + token1);
			System.out.println("Entity : " + req);
			HttpEntity<?> requestent2 = new HttpEntity<>(req, header);
			System.out.println("calling MPT Api:  " + requestent2);
			res = restTemplate.exchange(mptVehicleDetails, HttpMethod.POST, requestent2, PolicyResponse.class);
			if (res != null) {
				return CompletableFuture.completedFuture(res.getBody());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CompletableFuture.completedFuture(null);
	}
	 
}
