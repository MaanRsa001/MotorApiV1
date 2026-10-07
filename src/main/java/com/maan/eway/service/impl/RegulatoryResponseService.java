package com.maan.eway.service.impl;


import java.io.BufferedWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.maan.eway.auth.dto.ClaimLoginResponse;
import com.maan.eway.auth.dto.CommonLoginRes;
import com.maan.eway.auth.dto.LoginRequest;
import com.maan.eway.auth.service.AuthendicationService;
import com.maan.eway.bean.ApiIntegMaster;
import com.maan.eway.bean.CompanyProductMaster;
import com.maan.eway.bean.HomePositionMaster;
import com.maan.eway.bean.LoginUserInfo;
import com.maan.eway.bean.MotorDataDetails;
import com.maan.eway.bean.PersonalInfo;
import com.maan.eway.bean.SectionDataDetails;
import com.maan.eway.bean.TiraTrackingDetails;
import com.maan.eway.common.res.SuccessRes;
import com.maan.eway.config.DigitalSignatureGenerator;
import com.maan.eway.repository.ApiIntegMasterRepository;
import com.maan.eway.repository.CompanyProductMasterRepository;
import com.maan.eway.repository.HomePositionMasterRepository;
import com.maan.eway.repository.LoginUserInfoRepository;
import com.maan.eway.repository.MotorDataDetailsRepository;
import com.maan.eway.repository.PersonalInfoRepository;
import com.maan.eway.repository.SectionDataDetailsRepository;
import com.maan.eway.repository.TiraTrackingDetailsRepository;
import com.maan.eway.req.TiraMsg;
import com.maan.eway.req.acknowledge.CoverNoteRefResAck;
import com.maan.eway.req.acknowledge.FleetResDtl;
import com.maan.eway.req.acknowledge.MotorCoverNoteRefResAck;
import com.maan.eway.req.acknowledge.TiraM;
import com.maan.eway.req.acknowledge.TiraMsgAcknowlege;
import com.maan.eway.req.acknowledge.TiraMsgAcknowlegeFleet;
import com.maan.eway.req.fleet.TiraMsgVehiclePushFleet;
import com.maan.eway.req.push.PolicyHolder;
import com.maan.eway.req.push.TiraMsgCoverPush;
import com.maan.eway.req.push.TiraMsgVehiclePush;
import com.maan.eway.res.MotorTiraMsgRes;
import com.maan.eway.res.NonMotorTiraMsgRes;


@Service
public class RegulatoryResponseService {

	private Logger log = LogManager.getLogger(RegulatoryResponseService.class);
	
	@Value("${file.primaryPath}")
	private String primaryPath ;
	
	@Value(value = "${PremiaPushLink}")
	private String premiaPushLink;
	
	@Autowired
	private TiraTrackingDetailsRepository tiraTrackRepo;
	
	@Autowired
	private HomePositionMasterRepository homePositionRepo;
	
	@Autowired
	private PersonalInfoRepository personalInfoRepos;

	@Autowired	
	private DigitalSignatureGenerator signature;
	
	@Autowired
	private SectionDataDetailsRepository sectionDataRepo;
	
	@Autowired
	private MotorDataDetailsRepository motDataRepo;
	
	@Autowired
	private CompanyProductMasterRepository companyProductRepo;
	
	@Autowired
	private ApiIntegMasterRepository apiIntegRepo;
	
	@Autowired
	private LoginUserInfoRepository loginUserInfoRepo;

	@Value("${mail.pushnotification}")
	private String mailLink;
	
	//@Async("RegulatoryRequestsExecutor")
	public Map<String,String> saveRequestAndResponse(String request,String response,String basePath) {
		Map<String,String> result=new HashMap<String, String>();
		try {
			SimpleDateFormat format=new SimpleDateFormat("YYYY-MM-dd'T'HH-mm-ss.SSS");			
			String seconds = format.format(new Date());
			
			
			Path directory =Paths.get(primaryPath);
			if(Files.exists(directory)) {
				BufferedWriter writer =null;
				try {
					Path fileDirectory = Paths.get(primaryPath.concat(basePath));
					if(!Files.exists(fileDirectory)) {
						Files.createDirectories(fileDirectory);
 					}
					String path = primaryPath.concat(basePath).concat("/REQ "+seconds+".txt");
					result.put("REQ_PATH", path);
					Path fileD= Paths.get(path);
					writer = Files.newBufferedWriter(fileD, Charset.forName("UTF-8"));
					writer.write(request);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {
					if(writer!=null)
						writer.close();
				}
				
				try {					
					//Respnse  
					Path fileDirectory = Paths.get(primaryPath.concat(basePath));
					if(!Files.exists(fileDirectory)) {
						Files.createDirectories(fileDirectory);
 					}
					String path = primaryPath.concat(basePath).concat("/RES "+seconds+".txt");
					result.put("RES_PATH", path);
					Path fileD= Paths.get(path);
					writer = Files.newBufferedWriter(fileD, Charset.forName("UTF-8"));
					writer.write(response);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {
					if(writer!=null)
						writer.close();
				}
				

			}else {
				log.info(primaryPath+" Not Exist");
			}
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}finally {
			
		}
		return result;
	}
	
	

	@Async("RegulatoryResponseUpdate")
	public void savePostResponseInTables(TiraMsgVehiclePush req, MotorTiraMsgRes res,String methodName,String xmlString, String tokens) {
		Map<String, String> paths=null;
		String quoteNo="",locationId="",sectionId="",riskId="",registrationNo="";
		Map<String ,Object> request=new HashMap<String, Object>();
		Map<String ,Object> customer=new HashMap<String, Object>();
		List<String> attachment=new ArrayList<String>();
		try{
			  registrationNo=req.getMotorCoverNoteRefReq().getCoverNoteDtlBean().getMotorDtlBean().getRegistrationNumber(); // ().getVerificationDtl().getMotorRegistrationNumber();
			 String chassisNo=req.getMotorCoverNoteRefReq().getCoverNoteDtlBean().getMotorDtlBean().getChassisNumber();
			  StringWriter sw = new StringWriter();
			 	try {
			    JAXBContext newInstancev1 = JAXBContext.newInstance(MotorTiraMsgRes.class);
				Marshaller createMarshallerv1 = newInstancev1.createMarshaller();
				createMarshallerv1.setProperty("com.sun.xml.bind.xmlDeclaration", false);
				
				createMarshallerv1.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				createMarshallerv1.marshal(res, sw);
			 	}catch (Exception e) {
			 		e.printStackTrace();
				}
				
			//paths =
			paths=saveRequestAndResponse(xmlString, res==null?"RESPONSE ERROR":sw.toString(), "PushPolicy/"+(StringUtils.isNotBlank(chassisNo)?chassisNo:registrationNo));
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		try {
				String reqPath="Not Found";
				String resPath="Not Found";
				if(paths!=null && !paths.isEmpty()) {
					reqPath=paths.get("REQ_PATH");
					resPath=paths.get("RES_PATH");
				}
			Boolean status=("TIRA001".equals(res.getMotorcoverNote().getAcknowledgementStatusCode())
							||
							("TIRA214".equals(res.getMotorcoverNote().getAcknowledgementStatusCode()) 
									&& "Transaction successfully cancelled".equalsIgnoreCase(res.getMotorcoverNote().getAcknowledgementStatusDesc()
									)))?true:false;	
			String quoteNoWithRisk=req.getMotorCoverNoteRefReq().getCoverNoteDtlBean().getCoverNoteNumber();
			// Q12345-11021_797987
			//QuoteNo-LocationId-SectionId-RiskId
			int hashIndex = quoteNoWithRisk.indexOf('_'); 
			 if (hashIndex != -1) { 
				 quoteNoWithRisk = quoteNoWithRisk.substring(0, hashIndex);
				 
			 }
				// Q12345-11021
			 
			 if(quoteNoWithRisk.indexOf("-")!=-1) {
				 String[] quoteNos = quoteNoWithRisk.split("-");
				 try {
					 if(quoteNos.length>0) {
						 quoteNo=quoteNos[0];
						 locationId=quoteNos[1];
						 sectionId=quoteNos[2];
						 riskId=quoteNos[3];
					 }
				 }catch (Exception e) {
					 e.printStackTrace();
				 }
			 }
			
			TiraTrackingDetails tira=TiraTrackingDetails.builder()
					.requestId(req.getMotorCoverNoteRefReq().getCoverNoteHdrBean().getRequestId())
					.acknowledgementId(res.getMotorcoverNote().getAcknowledgementId())
					.entryDate(new Date())
					.hitCount(0)
					.methodName(methodName)
					.policyNo(quoteNo)
					.status(status?"Y":"N")
					.statusCode(res.getMotorcoverNote().getAcknowledgementStatusCode())
					.statusDesc(res.getMotorcoverNote().getAcknowledgementStatusDesc())
					.chassisNo(req.getMotorCoverNoteRefReq().getCoverNoteDtlBean().getMotorDtlBean().getChassisNumber())
					.tiraTrackingId(Long.valueOf(Instant.now().toEpochMilli()))
					.requestFilePath(reqPath)
					.responseFilePath(resPath)
					.vehicleId(StringUtils.isBlank(riskId)?1:Integer.parseInt(riskId))
					.sectionId(StringUtils.isBlank(sectionId)?1:Integer.parseInt(sectionId))
					.locationId(StringUtils.isBlank(locationId)?1:Integer.parseInt(locationId))
					.build();
			tiraTrackRepo.save(tira);
			
			attachment.add(reqPath);
			attachment.add(resPath);
			
			if(status) {
				//CoverNoteNumber
				
				HomePositionMaster hm = homePositionRepo.findByQuoteNo(quoteNo);
				hm.setTiraRequestId(req.getMotorCoverNoteRefReq().getCoverNoteHdrBean().getRequestId());
				homePositionRepo.save(hm);
			}
		}catch (Exception e) {
			e.printStackTrace();
		} finally {

			HomePositionMaster hm = homePositionRepo.findByQuoteNo(quoteNo);
			if(hm != null) {
			PersonalInfo personalInfo = personalInfoRepos.findByCustomerId(hm.getCustomerId());
			PolicyHolder policyHolder = req.getMotorCoverNoteRefReq().getCoverNoteDtlBean().getPolicyHoldersBean()
					.getPolicyHolderBeanList().get(0);
			request.put("Attachments", attachment);
			request.put("BranchCode", hm.getBranchCode());
			 customer.put("Customermailid", "tanzaniatira01@gmail.com");
			//customer.put("Customermailid", policyHolder.getEmailAddress()+",tanzaniatira01@gmail.com");
			customer.put("Customername", policyHolder.getPolicyHolderName());
			customer.put("Customermessengercode", personalInfo != null ? personalInfo.getMobileCode1() :"0");
			//customer.put("Customermessengerphone", policyHolder.getPolicyHolderPhoneNumber());
			customer.put("Customermessengerphone", "123456");
			customer.put("Customerphonecode", personalInfo != null ? personalInfo.getMobileCode1() :"0");
			//customer.put("Customerphoneno", policyHolder.getPolicyHolderPhoneNumber());
			customer.put("Customerphoneno", "123456");
			request.put("Notifcationdate", new Date());
			request.put("Notifdescription", res.getMotorcoverNote().getAcknowledgementStatusCode() + "-"
					+ res.getMotorcoverNote().getAcknowledgementStatusDesc());
			request.put("Notifpriority", 0);
			request.put("Notifpushedstatus", "PENDING");
			request.put("Notiftemplatename", "TIRA_ACK_ERR");
			request.put("Policyno", req.getMotorCoverNoteRefReq().getCoverNoteDtlBean().getCoverNoteNumber());
			request.put("ProductId", hm.getProductId());
			request.put("Productname", hm.getProductName());
			//request.put("Quoteno", req.getMotorCoverNoteRefReq().getCoverNoteDtlBean().getCoverNoteNumber());
			request.put("Quoteno", registrationNo);
			request.put("RequestReferenceNo", req.getMotorCoverNoteRefReq().getCoverNoteDtlBean().getCoverNoteNumber());
			request.put("Statusmessage", res.getMotorcoverNote().getAcknowledgementStatusDesc());
			request.put("Customer", customer);
			request.put("CompanyId", hm.getCompanyId());

			//pushNotification(request, tokens);
			}
		}
	}
	
	@Async
	private void pushNotification(Map<String, Object> request, String tokens) {
		try {
			System.out.println("MAIL::Request::: " + request);
			try {
				if (!request.isEmpty()) {
					TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
						public java.security.cert.X509Certificate[] getAcceptedIssuers() {
							return null;
						}

						public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
						}

						public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
						}
					} };

					SSLContext sc = SSLContext.getInstance("SSL");
					sc.init(null, trustAllCerts, new java.security.SecureRandom());
					HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
					HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

						@Override
						public boolean verify(String hostname, SSLSession session) {
							// TODO Auto-generated method stub
							return true;
						}
					});

					RestTemplate restTemplate = new RestTemplate();
					HttpHeaders headers = new HttpHeaders();
					headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
					headers.setContentType(MediaType.APPLICATION_JSON);
					headers.set("Authorization", tokens);
					HttpEntity<Object> entityReq = new HttpEntity<>(request, headers);
					System.out.println(entityReq.getBody());
					ResponseEntity<Object> response = restTemplate.postForEntity(mailLink, entityReq, Object.class);
					System.out.println(response.getBody());
				} else {
					System.out.println("MAIL CANT sent no content ");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	

	public TiraMsgAcknowlege updateAcknowlegment(TiraMsgAcknowlege req,String tokens) {
		
		List<TiraTrackingDetails> previousOnes=tiraTrackRepo.findByRequestIdAndStatusAndMethodNameOrderByEntryDateDesc(req.getMotorCoverNoteRefRes().getRequestId(),"Y","/covernote/non-life/motor/v2/request");
		TiraTrackingDetails previousOne=previousOnes.get(0);
		String acknowledge=previousOne.getAcknowledgementId();
		String policyNo=previousOne.getPolicyNo();
		String chassisNo=previousOne.getChassisNo();
		TiraMsgAcknowlege ackResponse=null;
		PersonalInfo personalInfo = null;
		MotorDataDetails motData = null;
		HomePositionMaster hm =null;
		CompanyProductMaster product=null;
		Map<String, String> paths=null;
		Map<String ,Object> requests=new HashMap<String, Object>();
		Map<String ,Object> customer=new HashMap<String, Object>();
		List<String> attachment=new ArrayList<String>();
		
		try{
			 
			  StringWriter sw = new StringWriter();
			 	try {
			    JAXBContext newInstancev1 = JAXBContext.newInstance(TiraMsgAcknowlege.class);
				Marshaller createMarshallerv1 = newInstancev1.createMarshaller();
				createMarshallerv1.setProperty("com.sun.xml.bind.xmlDeclaration", false);
				
				createMarshallerv1.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				createMarshallerv1.marshal(req, sw);
				String request = sw.toString();
				
				ackResponse=new TiraMsgAcknowlege();
				MotorCoverNoteRefResAck resAck=new MotorCoverNoteRefResAck();
				resAck.setAcknowledgementId(acknowledge);
				resAck.setResponseId(req.getMotorCoverNoteRefRes().getResponseId());
				resAck.setAcknowledgementStatusCode(previousOne.getStatusCode());
				resAck.setAcknowledgementStatusDesc(previousOne.getStatusDesc());
				ackResponse.setMotorCoverNoteRefResAck(resAck);
				
				sw=new StringWriter();
				createMarshallerv1.marshal(resAck, sw);
				String response = sw.toString();
				response=response.replaceAll(">[\\s\r\n]*<", "><");//("[\r\n]+", "");				
				String collectMsgSignature = signature.collectMsgSignature(response);
				ackResponse.setMsgSignature(collectMsgSignature);
				
				paths=saveRequestAndResponse(request, response==null?"RESPONSE ERROR":response, "PushPolicy/"+chassisNo);
				 
			 	}catch (Exception e) {
			 		e.printStackTrace();
				}
				
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		Boolean status=false;
		
		
		try {
			String reqPath="Not Found";
			String resPath="Not Found";
			if(paths!=null && !paths.isEmpty()) {
				reqPath=paths.get("REQ_PATH");
				resPath=paths.get("RES_PATH");
			}
			
			status=("TIRA001".equals(req.getMotorCoverNoteRefRes().getResponseStatusCode())
					||
					("TIRA214".equals(req.getMotorCoverNoteRefRes().getResponseStatusCode()) 
							&& "Transaction successfully cancelled".equalsIgnoreCase(req.getMotorCoverNoteRefRes().getResponseStatusDesc()
									)))?true:false;	
			
			TiraTrackingDetails tira=TiraTrackingDetails.builder()
					.requestId(req.getMotorCoverNoteRefRes().getRequestId())
					.acknowledgementId(acknowledge)
					.responseId(req.getMotorCoverNoteRefRes().getResponseId())
					.entryDate(new Date())
					.hitCount(0)
					.methodName("/covernote/non-life/motor/v2/acknowledge")
					.policyNo(policyNo)
					.status(status?"Y":"N")
					.statusCode(req.getMotorCoverNoteRefRes().getResponseStatusCode())
					.statusDesc(req.getMotorCoverNoteRefRes().getResponseStatusDesc())
					.chassisNo(chassisNo)
					.tiraTrackingId(Long.valueOf(Instant.now().toEpochMilli()))
					.requestFilePath(reqPath)
					.responseFilePath(resPath)
					.vehicleId(previousOne.getVehicleId())
					.sectionId(previousOne.getSectionId())
					.locationId(previousOne.getLocationId())
					.build();
			tiraTrackRepo.save(tira);
			
			attachment.add(reqPath);
			attachment.add(resPath);
		}catch (Exception e) {
			e.printStackTrace();
		}
		try {
			
			if(previousOne!=null && StringUtils.isNotBlank(previousOne.getPolicyNo())) {
				try {
					List<SectionDataDetails> sections=sectionDataRepo.findByQuoteNo(previousOne.getPolicyNo());
					sections.stream().filter(s-> s.getRiskId().compareTo(previousOne.getVehicleId())==0).forEach(new Consumer<SectionDataDetails>() {

						@Override
						public void accept(SectionDataDetails t) {
							boolean  status=("TIRA001".equals(req.getMotorCoverNoteRefRes().getResponseStatusCode())
									||"TIRA024".equals(req.getMotorCoverNoteRefRes().getResponseStatusCode())
									||
									
									("TIRA214".equals(req.getMotorCoverNoteRefRes().getResponseStatusCode()) 
											&& "Transaction successfully cancelled".equalsIgnoreCase(req.getMotorCoverNoteRefRes().getResponseStatusDesc()
													)))?true:false;				
							if(!"TIRA001".equals(StringUtils.isBlank(t.getResponseStatusCode())?"":t.getResponseStatusCode())) {
								t.setTiraResponseId(req.getMotorCoverNoteRefRes().getResponseId());
								t.setResponseStatusCode(req.getMotorCoverNoteRefRes().getResponseStatusCode());
								t.setResponseStatusDesc(req.getMotorCoverNoteRefRes().getResponseStatusDesc());
							}
							if(status && StringUtils.isNotBlank(req.getMotorCoverNoteRefRes().getCoverNoteReferenceNumber())){
								t.setCoverNoteReferenceNo(req.getMotorCoverNoteRefRes().getCoverNoteReferenceNumber());
								t.setStickerNumber(req.getMotorCoverNoteRefRes().getStickerNumber());
							}
							
						}

					}); 
					sectionDataRepo.saveAll(sections);
				}catch (Exception e) {
					e.printStackTrace();
				}
				try {
					
					premiaIntegration(previousOne.getPolicyNo(),tokens,premiaPushLink);
						
				}catch (Exception e) {
					e.printStackTrace();
				}
				
				 hm = homePositionRepo.findByTiraRequestIdAndQuoteNo(req.getMotorCoverNoteRefRes().getRequestId(),previousOne.getPolicyNo());
				if(hm!=null) {
					
					
					hm.setTiraResponseId(req.getMotorCoverNoteRefRes().getResponseId());
					hm.setResponseStatusCode(req.getMotorCoverNoteRefRes().getResponseStatusCode());
					hm.setResponseStatusDesc(req.getMotorCoverNoteRefRes().getResponseStatusDesc());
					if(status) {
						hm.setCoverNoteReferenceNo(req.getMotorCoverNoteRefRes().getCoverNoteReferenceNumber());
						hm.setStickerNumber(req.getMotorCoverNoteRefRes().getStickerNumber());
					}					
					homePositionRepo.save(hm);   
					
				}
				
			}
			
			 HomePositionMaster home = homePositionRepo.findByQuoteNo(previousOne.getPolicyNo());
			 personalInfo = personalInfoRepos.findByCustomerId(home.getCustomerId());
			motData = motDataRepo.findByQuoteNoAndVehicleId(previousOne.getPolicyNo(), previousOne.getVehicleId().toString());
			product =companyProductRepo.findTopByProductIdAndCompanyIdAndStatusOrderByAmendIdDesc(home.getProductId(),home.getCompanyId(),"Y");
			
			String loginId="";
			if (home.getApplicationId() != null && "1".contentEquals(home.getApplicationId())) {
			    loginId = home.getLoginId();
			} else {
			    loginId = home.getApplicationId();
			}
			System.out.println("Login id for user" + loginId);
			String mailId = "";
			LoginUserInfo loginUserInfo = loginUserInfoRepo.findByLoginId(loginId);
			if (loginUserInfo != null) {
			    mailId = loginUserInfo.getUserMail();
			    System.out.println("user mailid" +mailId);			
			    }
			requests.put("Attachments", null);//attachment
			requests.put("BranchCode", home.getBranchCode());
			if(StringUtils.isNotBlank(mailId)){
				customer.put("Customermailid", "melina@alliance.co.tz,Bosco@alliance.co.tz,it@uniontrust.co.tz,tanzaniatira01@gmail.com"+","+mailId);
			}else {
				customer.put("Customermailid", "melina@alliance.co.tz,Bosco@alliance.co.tz,it@uniontrust.co.tz,tanzaniatira01@gmail.com");
			}
			//customer.put("Customermailid", personalInfo != null ? personalInfo.getEmail1()+",tanzaniatira01@gmail.com" :"");
			customer.put("Customername", home==null?"Customer":home.getCustomerName());
			customer.put("Customermessengercode", personalInfo != null ? personalInfo.getMobileCode1() : "0");
			//customer.put("Customermessengerphone", personalInfo != null ? personalInfo.getMobileNo1(): "0");
			customer.put("Customermessengerphone", "123456");
			customer.put("Customerphonecode",personalInfo != null ? personalInfo.getMobileCode1() : "0");
			//customer.put("Customerphoneno", personalInfo != null ? personalInfo.getMobileNo1(): "0");
			customer.put("Customerphoneno", "123456");
			requests.put("Notifcationdate", new Date());
			requests.put("Notifdescription", req.getMotorCoverNoteRefRes().getResponseStatusCode() +"-"+ req.getMotorCoverNoteRefRes().getResponseStatusDesc());
			requests.put("Notifpriority", 0);
			requests.put("Notifpushedstatus", "PENDING");
			if(!"TIRA001".equalsIgnoreCase(req.getMotorCoverNoteRefRes().getResponseStatusCode())) {
				requests.put("Notiftemplatename", "TIRA_ACK_ERR");
			}else {
				requests.put("Notiftemplatename", "TIRA_ACK_SUCCESS");
			}
			requests.put("Policyno", req.getMotorCoverNoteRefRes().getStickerNumber());
			requests.put("ProductId", home.getProductId());
			requests.put("Productname", home.getProductName());
			requests.put("Quoteno",  motData.getRegistrationNumber());
			requests.put("RequestReferenceNo",  req.getMotorCoverNoteRefRes().getCoverNoteReferenceNumber());
			requests.put("Statusmessage", req.getMotorCoverNoteRefRes().getResponseStatusDesc());			
			requests.put("Customer", customer); 
			requests.put("CompanyId", home.getCompanyId());
			requests.put("InsuranceClass", previousOne.getPolicyNo());
			SimpleDateFormat sdf =
			        new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

			String formattedDate = sdf.format(new Date());
			requests.put("RegistrationNo", formattedDate);
			return ackResponse;
		}catch (Exception e) {
			e.printStackTrace();
		}finally {

			// PolicyHolder policyHolder =
			// req.getMotorCoverNoteRefReq().getCoverNoteDtlBean().getPolicyHoldersBean().getPolicyHolderBeanList().get(0);

			System.out.println("In Finally CorrectData");
			correctData(req, previousOne);
			System.out.println("Completed req");

			pushNotification(requests,tokens);
			
			
			
			try {

			    if (hm != null
			            && "M".equalsIgnoreCase(product.getMotorYn())&& "TIRA001".equalsIgnoreCase(req.getMotorCoverNoteRefRes().getResponseStatusCode())
			            && StringUtils.isNotBlank(req.getMotorCoverNoteRefRes().getStickerNumber())) {

			        sendMotorPolicyWhatsapp(hm, personalInfo, motData, req, previousOne, product);
			    }

			} catch (Exception e) {
			    e.printStackTrace();
			}
			
			
		}
		
		
		return null;
	}
	
	private void sendMotorPolicyWhatsapp(HomePositionMaster hm,
            PersonalInfo personalInfo,
            MotorDataDetails motData,
            TiraMsgAcknowlege req,
            TiraTrackingDetails previousOne,
            CompanyProductMaster product) {

			try {
				
				List<ApiIntegMaster> apis =
				        apiIntegRepo.findAllByCompanyIdAndProductId(
				                hm.getCompanyId(),
				                hm.getProductId());

				if (apis == null || apis.isEmpty()) {
				    return;
				}

				String baseUrl = null;
				String token = null;

				for (ApiIntegMaster api : apis) {

				    if ("WHATSAPP_MSG_MOTOR".equalsIgnoreCase(api.getApiType())
				            && "Y".equalsIgnoreCase(api.getStatus())) {

				        baseUrl = api.getApiUrl();
				    }

				    if ("WHATSAPP_TOKEN_MOTOR".equalsIgnoreCase(api.getApiType())
				            && "Y".equalsIgnoreCase(api.getStatus())) {

				        token = api.getApiUrl();   
				    }
				}

				
				if (StringUtils.isBlank(baseUrl) || StringUtils.isBlank(token)) {
				    return;
				}
			
					String mobileWithCode =
					personalInfo.getMobileCode1() + personalInfo.getMobileNo1();
					
					String quoteNo = hm.getQuoteNo();
					
					String encodedQuote = Base64.getEncoder()
					.encodeToString(quoteNo.getBytes(StandardCharsets.UTF_8));
					
					String productName = product != null
					? product.getProductName()
					: "Motor";

					Map<String, Object> requestBody = new HashMap<>();
					requestBody.put("template_name", "policy_message");
					requestBody.put("broadcast_name", "string");
					
					List<Map<String, String>> parameters = new ArrayList<>();
					
					parameters.add(param("name", hm.getCustomerName()));
					parameters.add(param("1", encodedQuote));
					parameters.add(param("policyNo", hm.getPolicyNo()));
					parameters.add(param("stickerNo", req.getMotorCoverNoteRefRes().getStickerNumber()));
					parameters.add(param("product", productName));
					parameters.add(param("regNo", motData.getRegistrationNumber()));
					
					requestBody.put("parameters", parameters);
					requestBody.put("channel_number", "255743000303");
					
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.APPLICATION_JSON);
					headers.setBearerAuth(token);
					
					HttpEntity<Map<String, Object>> entity =
					new HttpEntity<>(requestBody, headers);
				
					RestTemplate restTemplate = new RestTemplate();
				
					restTemplate.exchange(
							baseUrl + mobileWithCode,HttpMethod.POST,entity,String.class
				);
			
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	private Map<String, String> param(String name, String value) {
	    Map<String, String> map = new HashMap<>();
	    map.put("name", name);
	    map.put("value", value);
	    return map;
	}

	public SuccessRes premiaIntegration(String quoteNo,String tokens,String premiaPushLink) {
		SuccessRes response = new SuccessRes();
//		  PremiaResponse response = new PremiaResponse();
		try {
			LoginRequest mslogin = new LoginRequest();
			mslogin.setLoginId("guest");
			mslogin.setPassword("Admin@01");
			mslogin.setReLoginKey("Y");
			CommonLoginRes checkUserLogin = authservice.checkUserLogin(mslogin, null);
			ClaimLoginResponse commonResponse = (ClaimLoginResponse) checkUserLogin.getCommonResponse();
			if (commonResponse != null) {
				String token = commonResponse.getToken();
				Set<String> stickerNoList = new HashSet<>();
				List<SectionDataDetails> risks = sectionDataRepo.findByQuoteNo(quoteNo);
				if (risks != null) {
					stickerNoList = risks.stream()
				                        .map(SectionDataDetails::getStickerNumber)
				                        .filter(Objects::nonNull) 
				                        .collect(Collectors.toSet());
				}
				System.out.println("Sticker Number"+stickerNoList);
				List<String> premiaIds = new ArrayList<>();
				premiaIds.add("1");premiaIds.add("2");premiaIds.add("3");premiaIds.add("4");premiaIds.add("5");premiaIds.add("6");premiaIds.add("7");premiaIds.add("8");premiaIds.add("9");premiaIds.add("10");premiaIds.add("11");
				PushIntegrationThread hit = new PushIntegrationThread(quoteNo, premiaIds, token, premiaPushLink);
				Thread push = new Thread(hit);
				push.start();
				response.setResponse("Integration Processing....");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	

	public void savePostResponseInTablesNonMotor(TiraMsgCoverPush req, NonMotorTiraMsgRes res, String methodName,
			String xmlString) {
		Map<String, String> paths=null;
		try{
			 String registrationNo="";//req.getCoverNoteRefReq().getCoverNoteDtlBean().getMotorDtlBean().getRegistrationNumber(); // ().getVerificationDtl().getMotorRegistrationNumber();
			 String chassisNo=req.getCoverNoteRefReq().getCoverNoteDtlBean().getCoverNoteNumber();//req.getCoverNoteRefReq().getCoverNoteDtlBean().getMotorDtlBean().getChassisNumber();
			  StringWriter sw = new StringWriter();
			 	try {
			    JAXBContext newInstancev1 = JAXBContext.newInstance(NonMotorTiraMsgRes.class);
				Marshaller createMarshallerv1 = newInstancev1.createMarshaller();
				createMarshallerv1.setProperty("com.sun.xml.bind.xmlDeclaration", false);
				
				createMarshallerv1.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				createMarshallerv1.marshal(res, sw);
			 	}catch (Exception e) {
			 		e.printStackTrace();
				}
				
			 paths=saveRequestAndResponse(xmlString, res==null?"RESPONSE ERROR":sw.toString(), "PushPolicy/"+(StringUtils.isNotBlank(chassisNo)?chassisNo:registrationNo));
			 
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		try {
				String reqPath="Not Found";
				String resPath="Not Found";
				if(paths!=null && !paths.isEmpty()) {
					reqPath=paths.get("REQ_PATH");
					resPath=paths.get("RES_PATH");
				}
			
			
					String quoteNoWithRisk=req.getCoverNoteRefReq().getCoverNoteDtlBean().getCoverNoteNumber();
					int hashIndex = quoteNoWithRisk.indexOf('_'); 
					 if (hashIndex != -1) { 
						 quoteNoWithRisk = quoteNoWithRisk.substring(0, hashIndex);
					 }					 
					 
					 String quoteNo="",locationId="",sectionId="",riskId="";
					 if(quoteNoWithRisk.indexOf("-")!=-1) {
						 String[] quoteNos = quoteNoWithRisk.split("-");
						 try {
							 if(quoteNos.length>0) {
								 quoteNo=quoteNos[0];
								 locationId=quoteNos[1];
								 sectionId=quoteNos[2];
								 riskId=quoteNos[3];
							 }
						 }catch (Exception e) {
							 e.printStackTrace();
						 }
					 }
					 Boolean status=("TIRA001".equals(res.getNonmotorcoverNote().getAcknowledgementStatusCode())
								||
								("TIRA214".equals(res.getNonmotorcoverNote().getAcknowledgementStatusCode()) 
										&& "Transaction successfully cancelled".equalsIgnoreCase(res.getNonmotorcoverNote().getAcknowledgementStatusDesc()
										)))?true:false;	
					 
					TiraTrackingDetails tira=TiraTrackingDetails.builder()
					.requestId(req.getCoverNoteRefReq().getCoverNoteHdrBean().getRequestId())
					.acknowledgementId(res.getNonmotorcoverNote().getAcknowledgementId())
					.entryDate(new Date())
					.hitCount(0)
					.methodName(methodName)
					.policyNo(quoteNo)
					.status(status?"Y":"N")
					.statusCode(res.getNonmotorcoverNote().getAcknowledgementStatusCode())
					.statusDesc(res.getNonmotorcoverNote().getAcknowledgementStatusDesc())
					.chassisNo(req.getCoverNoteRefReq().getCoverNoteDtlBean().getCoverNoteNumber())
					.tiraTrackingId(Long.valueOf(Instant.now().toEpochMilli()))
					.requestFilePath(reqPath)
					.responseFilePath(resPath)
					.vehicleId(StringUtils.isBlank(riskId)?1:Integer.parseInt(riskId))
					.sectionId(StringUtils.isBlank(sectionId)?1:Integer.parseInt(sectionId))
					.locationId(StringUtils.isBlank(locationId)?1:Integer.parseInt(locationId))
					.build();
			tiraTrackRepo.save(tira);
			
			if(status) {
				//CoverNoteNumber 
				HomePositionMaster hm = homePositionRepo.findByQuoteNo(req.getCoverNoteRefReq().getCoverNoteDtlBean().getCoverNoteNumber());
				hm.setTiraRequestId(req.getCoverNoteRefReq().getCoverNoteHdrBean().getRequestId());
				homePositionRepo.save(hm);
			}
		}catch (Exception e) {
			e.printStackTrace();
		} 
	}



	public TiraMsgAcknowlege updateAcknowlegmentForNonMotor(TiraMsgAcknowlege req , String tokens) {

		List<TiraTrackingDetails> previousOnes=tiraTrackRepo.findByRequestIdAndStatusAndMethodNameOrderByEntryDateDesc(req.getCoverNoteRefRes().getRequestId(),"Y","/covernote/non-life/other/v2/request");
		TiraTrackingDetails previousOne=previousOnes.get(0);
		String acknowledge=previousOne.getAcknowledgementId();
		String policyNo=previousOne.getPolicyNo();
		String chassisNo=previousOne.getChassisNo();
		Integer vehicleId=previousOne.getVehicleId();
		TiraMsgAcknowlege ackResponse=null;
		PersonalInfo personalInfo = null;
	//	MotorDataDetails motData = null;
		HomePositionMaster hm =null;
		CompanyProductMaster product=null;
		Map<String ,Object> requests=new HashMap<String, Object>();
		Map<String, String> paths=null;
		Map<String ,Object> customer=new HashMap<String, Object>();
		List<String> attachment=new ArrayList<String>();
		try{
			 
			  StringWriter sw = new StringWriter();
			 	try {
			    JAXBContext newInstancev1 = JAXBContext.newInstance(TiraMsgAcknowlege.class);
				Marshaller createMarshallerv1 = newInstancev1.createMarshaller();
				createMarshallerv1.setProperty("com.sun.xml.bind.xmlDeclaration", false);
				
				createMarshallerv1.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				createMarshallerv1.marshal(req, sw);
				String request = sw.toString();
				
				ackResponse=new TiraMsgAcknowlege();
				CoverNoteRefResAck resAck=new CoverNoteRefResAck();
				resAck.setAcknowledgementId(acknowledge);
				resAck.setResponseId(req.getCoverNoteRefRes().getResponseId());
				resAck.setAcknowledgementStatusCode(previousOne.getStatusCode());
				resAck.setAcknowledgementStatusDesc(previousOne.getStatusDesc());
				ackResponse.setCoverNoteRefResAck(resAck);
				
				sw=new StringWriter();
				createMarshallerv1.marshal(resAck, sw);
				String response = sw.toString();
				response=response.replaceAll(">[\\s\r\n]*<", "><");//("[\r\n]+", "");				
				String collectMsgSignature = signature.collectMsgSignature(response);
				ackResponse.setMsgSignature(collectMsgSignature);
				
				 paths=saveRequestAndResponse(request, response==null?"RESPONSE ERROR":response, "PushPolicy/"+chassisNo);
				
			 	}catch (Exception e) {
			 		e.printStackTrace();
				}
				
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		Boolean status=false;
		
		
		try {
			String reqPath="Not Found";
			String resPath="Not Found";
			if(paths!=null && !paths.isEmpty()) {
				reqPath=paths.get("REQ_PATH");
				resPath=paths.get("RES_PATH");
			}
			
			status=("TIRA001".equals(req.getCoverNoteRefRes().getResponseStatusCode())
					||
					("TIRA214".equals(req.getCoverNoteRefRes().getResponseStatusCode()) 
							&& "Transaction successfully cancelled".equalsIgnoreCase(req.getCoverNoteRefRes().getResponseStatusDesc()
									)))?true:false;	
			

			/*String quoteNoWithRisk=req.getCoverNoteRefReq().getCoverNoteDtlBean().getCoverNoteNumber();
			int hashIndex = quoteNoWithRisk.indexOf('_'); 
			 if (hashIndex != -1) { 
				 quoteNoWithRisk = quoteNoWithRisk.substring(0, hashIndex);
			 }					 
			String vehicleId = quoteNoWithRisk.substring(quoteNoWithRisk.lastIndexOf("-")+1,quoteNoWithRisk.length());
			String quoteNo=quoteNoWithRisk.replaceAll("-"+vehicleId, "");	*/	
			TiraTrackingDetails tira=TiraTrackingDetails.builder()
					.requestId(req.getCoverNoteRefRes().getRequestId())
					.acknowledgementId(acknowledge)
					.responseId(req.getCoverNoteRefRes().getResponseId())
					.entryDate(new Date())
					.hitCount(0)
					.methodName("/covernote/non-life/other/v2/acknowledge")
					.policyNo(policyNo)
					.status(status?"Y":"N")
					.statusCode(req.getCoverNoteRefRes().getResponseStatusCode())
					.statusDesc(req.getCoverNoteRefRes().getResponseStatusDesc())
					.chassisNo(chassisNo)
					.tiraTrackingId(Long.valueOf(Instant.now().toEpochMilli()))
					.requestFilePath(reqPath)
					.responseFilePath(resPath)
					.vehicleId(vehicleId)
					.sectionId(previousOne.getSectionId())
					.locationId(previousOne.getLocationId())
					.build();
			tiraTrackRepo.save(tira);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			if(previousOne!=null && StringUtils.isNotBlank(previousOne.getPolicyNo())) {
				try {
					List<SectionDataDetails> sections=sectionDataRepo.findByQuoteNoAndRiskIdAndSectionIdAndLocationId(previousOne.getPolicyNo(), previousOne.getVehicleId(),previousOne.getSectionId().toString(),previousOne.getLocationId());
					sections.stream().forEach(new Consumer<SectionDataDetails>() {

						@Override
						public void accept(SectionDataDetails t) {
							boolean  status=("TIRA001".equals(req.getCoverNoteRefRes().getResponseStatusCode())
									||"TIRA024".equals(req.getCoverNoteRefRes().getResponseStatusCode())
									||
									
									("TIRA214".equals(req.getCoverNoteRefRes().getResponseStatusCode()) 
											&& "Transaction successfully cancelled".equalsIgnoreCase(req.getCoverNoteRefRes().getResponseStatusDesc()
													)))?true:false;				
							if(!"TIRA001".equals(StringUtils.isBlank(t.getResponseStatusCode())?"":t.getResponseStatusCode())) {
								t.setTiraResponseId(req.getCoverNoteRefRes().getResponseId());
								t.setResponseStatusCode(req.getCoverNoteRefRes().getResponseStatusCode());
								t.setResponseStatusDesc(req.getCoverNoteRefRes().getResponseStatusDesc());
							}
							if(status && StringUtils.isNotBlank(req.getCoverNoteRefRes().getCoverNoteReferenceNumber())){
								t.setCoverNoteReferenceNo(req.getCoverNoteRefRes().getCoverNoteReferenceNumber());
								//t.setStickerNumber(req.getMotorCoverNoteRefRes().getStickerNumber());
							}
							
						}

					}); 
					sectionDataRepo.saveAll(sections);
				}catch (Exception e) {
					e.printStackTrace();
				}
				try {
					
					premiaIntegration(previousOne.getPolicyNo(),tokens,premiaPushLink);
						
				}catch (Exception e) {
					e.printStackTrace();
				}
			 
				 hm = homePositionRepo.findByTiraRequestIdAndQuoteNo(req.getCoverNoteRefRes().getRequestId(),previousOne.getPolicyNo());
				if(hm!=null) {
					
					
					hm.setTiraResponseId(req.getCoverNoteRefRes().getResponseId());
					hm.setResponseStatusCode(req.getCoverNoteRefRes().getResponseStatusCode());
					hm.setResponseStatusDesc(req.getCoverNoteRefRes().getResponseStatusDesc());
					if(status) {
						hm.setCoverNoteReferenceNo(req.getCoverNoteRefRes().getCoverNoteReferenceNumber());
						//hm.setStickerNumber(req.getCoverNoteRefRes().getStickerNumber());
					}					
					homePositionRepo.save(hm);
				}
			}
			HomePositionMaster home = homePositionRepo.findByQuoteNo(previousOne.getPolicyNo());
			personalInfo = personalInfoRepos.findByCustomerId(home.getCustomerId());
		//	motData = motDataRepo.findByQuoteNoAndVehicleId(previousOne.getPolicyNo(), previousOne.getVehicleId().toString());
			product =companyProductRepo.findTopByProductIdAndCompanyIdAndStatusOrderByAmendIdDesc(home.getProductId(),home.getCompanyId(),"Y");
			
			String loginId="";
			if (home.getApplicationId() != null && "1".contentEquals(home.getApplicationId())) {
			    loginId = home.getLoginId();
			} else {
			    loginId = home.getApplicationId();
			}
			System.out.println("Login id for user" + loginId);
			String mailId = "";
			LoginUserInfo loginUserInfo = loginUserInfoRepo.findByLoginId(loginId);
			if (loginUserInfo != null) {
			    mailId = loginUserInfo.getUserMail();
			    System.out.println("user mailid" +mailId);			
			    }
			requests.put("Attachments", null);//attachment
			requests.put("BranchCode", home.getBranchCode());
			if(StringUtils.isNotBlank(mailId)){
				customer.put("Customermailid", "melina@alliance.co.tz,Bosco@alliance.co.tz,it@uniontrust.co.tz,tanzaniatira01@gmail.com"+","+mailId);
			}else {
				customer.put("Customermailid", "melina@alliance.co.tz,Bosco@alliance.co.tz,it@uniontrust.co.tz,tanzaniatira01@gmail.com");
			}
			//customer.put("Customermailid", personalInfo != null ? personalInfo.getEmail1()+",tanzaniatira01@gmail.com" :"");
			customer.put("Customername", home==null?"Customer":home.getCustomerName());
			customer.put("Customermessengercode", personalInfo != null ? personalInfo.getMobileCode1() : "0");
			//customer.put("Customermessengerphone", personalInfo != null ? personalInfo.getMobileNo1(): "0");
			customer.put("Customermessengerphone", "123456");
			customer.put("Customerphonecode",personalInfo != null ? personalInfo.getMobileCode1() : "0");
			//customer.put("Customerphoneno", personalInfo != null ? personalInfo.getMobileNo1(): "0");
			customer.put("Customerphoneno", "123456");
			requests.put("Notifcationdate", new Date());
			requests.put("Notifdescription", req.getMotorCoverNoteRefRes().getResponseStatusCode() +"-"+ req.getMotorCoverNoteRefRes().getResponseStatusDesc());
			requests.put("Notifpriority", 0);
			requests.put("Notifpushedstatus", "PENDING");
			if(!"TIRA001".equalsIgnoreCase(req.getMotorCoverNoteRefRes().getResponseStatusCode())) {
				requests.put("Notiftemplatename", "TIRA_ACK_ERR");
			}else {
				requests.put("Notiftemplatename", "TIRA_ACK_SUCCESS");
			}
			requests.put("Policyno", req.getMotorCoverNoteRefRes().getStickerNumber());
			requests.put("ProductId", home.getProductId());
			requests.put("Productname", home.getProductName());
		//	requests.put("Quoteno",  motData.getRegistrationNumber());
			requests.put("RequestReferenceNo",  req.getMotorCoverNoteRefRes().getCoverNoteReferenceNumber());
			requests.put("Statusmessage", req.getMotorCoverNoteRefRes().getResponseStatusDesc());			
			requests.put("Customer", customer); 
			requests.put("CompanyId", home.getCompanyId());
			requests.put("InsuranceClass", previousOne.getPolicyNo());
			SimpleDateFormat sdf =
			        new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

			String formattedDate = sdf.format(new Date());
			requests.put("RegistrationNo", formattedDate);
			return ackResponse;
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			System.out.println("In Finally CorrectData");
			correctData(req, previousOne);
			System.out.println("Completed req");
			
			try {

			    if (hm != null
			            && "M".equalsIgnoreCase(product.getMotorYn())&& "TIRA001".equalsIgnoreCase(req.getMotorCoverNoteRefRes().getResponseStatusCode())
			            && StringUtils.isNotBlank(req.getMotorCoverNoteRefRes().getStickerNumber())) {

			    //    sendMotorPolicyWhatsapp(hm, personalInfo, motData, req, previousOne, product);
			    }

			} catch (Exception e) {
			    e.printStackTrace();
			}
			
			
			}
		return null;
	
	}



	public void savePostResponseInTables(TiraMsgVehiclePushFleet req, MotorTiraMsgRes res, String methodName,String xmlString) {

		Map<String, String> paths=null;
		try{
			 String registrationNo="";//req.getMotorCoverNoteRefReq().getCoverNoteDtlBean().getMotorDtlBean().getRegistrationNumber(); // ().getVerificationDtl().getMotorRegistrationNumber();
			 String chassisNo="";//req.getMotorCoverNoteRefReq().getCoverNoteDtlBean().getMotorDtlBean().getChassisNumber();
			  StringWriter sw = new StringWriter();
			 	try {
			    JAXBContext newInstancev1 = JAXBContext.newInstance(MotorTiraMsgRes.class);
				Marshaller createMarshallerv1 = newInstancev1.createMarshaller();
				createMarshallerv1.setProperty("com.sun.xml.bind.xmlDeclaration", false);
				
				createMarshallerv1.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				createMarshallerv1.marshal(res, sw);
			 	}catch (Exception e) {
			 		e.printStackTrace();
				}
				
			paths=saveRequestAndResponse(xmlString, res==null?"RESPONSE ERROR":sw.toString(), "PushPolicy/"+(StringUtils.isNotBlank(chassisNo)?chassisNo:registrationNo));

		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		try {
				String reqPath="Not Found";
				String resPath="Not Found";
				if(paths!=null && !paths.isEmpty()) {
					reqPath=paths.get("REQ_PATH");
					resPath=paths.get("RES_PATH");
				}
			Boolean status=("TIRA001".equals(res.getMotorcoverNote().getAcknowledgementStatusCode())
							||
							("TIRA214".equals(res.getMotorcoverNote().getAcknowledgementStatusCode()) 
									&& "Transaction successfully cancelled".equalsIgnoreCase(res.getMotorcoverNote().getAcknowledgementStatusDesc()
									)))?true:false;	
							
			TiraTrackingDetails tira=TiraTrackingDetails.builder()
					.requestId(req.getMotorCoverNoteRefReq().getCoverNoteHdrBean().getRequestId())
					.acknowledgementId(res.getMotorcoverNote().getAcknowledgementId())
					.entryDate(new Date())
					.hitCount(0)
					.methodName(methodName)
					.policyNo(req.getMotorCoverNoteRefReq().getCoverNoteDtlBean().getFleetHdr().getFleetId())
					.status(status?"Y":"N")
					.statusCode(res.getMotorcoverNote().getAcknowledgementStatusCode())
					.statusDesc(res.getMotorcoverNote().getAcknowledgementStatusDesc())
					.chassisNo(req.getMotorCoverNoteRefReq().getCoverNoteDtlBean().getFleetHdr().getFleetId())
					.tiraTrackingId(Long.valueOf(Instant.now().toEpochMilli()))
					.requestFilePath(reqPath)
					.responseFilePath(resPath)
					.sectionId(null)
					.vehicleId(null)
					.locationId(null)
					.build();
			tiraTrackRepo.save(tira);
			
			if(status) {
				//CoverNoteNumber
				HomePositionMaster hm = homePositionRepo.findByQuoteNo(req.getMotorCoverNoteRefReq().getCoverNoteDtlBean().getFleetHdr().getFleetId());
				hm.setTiraRequestId(req.getMotorCoverNoteRefReq().getCoverNoteHdrBean().getRequestId());
				homePositionRepo.save(hm);
			}
		}catch (Exception e) {
			e.printStackTrace();
		} 
	
		
	}



	public TiraM updateAcknowlegmentFleet(TiraMsgAcknowlegeFleet req) {
		List<TiraTrackingDetails> previousOnes=tiraTrackRepo.findByRequestIdAndStatusAndMethodNameOrderByEntryDateDesc(req.getMotorCoverNoteRefRes().getFleetResHdr().getRequestId(),"Y","/covernote/non-life/motor/v2/requestfleet");
		TiraTrackingDetails previousOne=previousOnes.get(0);
		String acknowledge=previousOne.getAcknowledgementId();
		String policyNo=previousOne.getPolicyNo();
		String chassisNo="";//previousOne.getChassisNo();
		TiraM ackResponse=null;
		Map<String, String> paths=null;
		try{
			 
			  StringWriter sw = new StringWriter();
			 	try {
			    JAXBContext newInstancev1 = JAXBContext.newInstance(TiraMsgAcknowlegeFleet.class);
				Marshaller createMarshallerv1 = newInstancev1.createMarshaller();
				createMarshallerv1.setProperty("com.sun.xml.bind.xmlDeclaration", false);
				
				createMarshallerv1.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				createMarshallerv1.marshal(req, sw);
				String request = sw.toString();
				
				ackResponse=new TiraM();
				MotorCoverNoteRefResAck resAck=new MotorCoverNoteRefResAck();
				resAck.setAcknowledgementId(acknowledge);
				resAck.setResponseId(req.getMotorCoverNoteRefRes().getFleetResHdr().getResponseId());
				resAck.setAcknowledgementStatusCode(previousOne.getStatusCode());
				resAck.setAcknowledgementStatusDesc(previousOne.getStatusDesc());
				ackResponse.setMotorCoverNoteRefResAck(resAck);
				
				sw=new StringWriter();
				  newInstancev1 = JAXBContext.newInstance(TiraM.class);
				  createMarshallerv1 = newInstancev1.createMarshaller();
				  createMarshallerv1.setProperty("com.sun.xml.bind.xmlDeclaration", false);
				createMarshallerv1.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				createMarshallerv1.marshal(resAck, sw);
				String response = sw.toString();
				response=response.replaceAll(">[\\s\r\n]*<", "><");//("[\r\n]+", "");				
				String collectMsgSignature = signature.collectMsgSignature(response);
				ackResponse.setMsgSignature(collectMsgSignature);
				
				paths= saveRequestAndResponse(request, response==null?"RESPONSE ERROR":response, "PushPolicy/"+chassisNo);
				
			 	}catch (Exception e) {
			 		e.printStackTrace();
				}
				
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		//return ackResponse;
		Boolean status=false;
		
		
		try {
			String reqPath="Not Found";
			String resPath="Not Found";
			if(paths!=null && !paths.isEmpty()) {
				reqPath=paths.get("REQ_PATH");
				resPath=paths.get("RES_PATH");
			}
			
			List<TiraTrackingDetails> ttds=new ArrayList<TiraTrackingDetails>();
			for (int i = 0;req.getMotorCoverNoteRefRes().getFleetResDtl()!=null &&  i < req.getMotorCoverNoteRefRes().getFleetResDtl().size(); i++) {
				FleetResDtl fleetResDtl = req.getMotorCoverNoteRefRes().getFleetResDtl().get(i);
				
				status=("TIRA001".equals(fleetResDtl.getResponseStatusCode())
						||
						("TIRA214".equals(fleetResDtl.getResponseStatusCode()) 
								&& "Transaction successfully cancelled".equalsIgnoreCase(fleetResDtl.getResponseStatusDesc()
										)))?true:false;	
				
				TiraTrackingDetails tira=TiraTrackingDetails.builder()
						.requestId(req.getMotorCoverNoteRefRes().getFleetResHdr().getRequestId())
						.acknowledgementId(acknowledge)
						.responseId(req.getMotorCoverNoteRefRes().getFleetResHdr().getResponseId())
						.entryDate(new Date())
						.hitCount(0)
						.methodName("/covernote/non-life/motor/v2/fleet/acknowledge")
						.policyNo(policyNo)
						.status(status?"Y":"N")
						.statusCode(fleetResDtl.getResponseStatusCode())
						.statusDesc(fleetResDtl.getResponseStatusDesc())
						.chassisNo(fleetResDtl.getFleetEntry()+"")
						.tiraTrackingId(Long.valueOf(Instant.now().toEpochMilli()+Long.valueOf(String.valueOf(Math.random()*100))))
						.requestFilePath(reqPath)
						.responseFilePath(resPath)
						.sectionId(null)
						.vehicleId(null)
						.locationId(null)
						.build();
				ttds.add(tira);
			}
			
			tiraTrackRepo.saveAll(ttds);
		}catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if(previousOne!=null && StringUtils.isNotBlank(previousOne.getPolicyNo())) {
				
				
				List<SectionDataDetails> sections=sectionDataRepo.findByQuoteNo(previousOne.getPolicyNo());
				for (int i = 0;req.getMotorCoverNoteRefRes().getFleetResDtl() !=null &&  i < req.getMotorCoverNoteRefRes().getFleetResDtl().size(); i++) {
					FleetResDtl fleetResDtl = req.getMotorCoverNoteRefRes().getFleetResDtl().get(i);
					sections.stream().filter(s-> s.getRiskId().compareTo(fleetResDtl.getFleetEntry().intValue())==0).forEach(new Consumer<SectionDataDetails>() {

						@Override
						public void accept(SectionDataDetails t) {
							// TODO Auto-generated method stub
							 
							boolean  status=("TIRA001".equals(fleetResDtl.getResponseStatusCode())
									||
									("TIRA214".equals(fleetResDtl.getResponseStatusCode()) 
											&& "Transaction successfully cancelled".equalsIgnoreCase(fleetResDtl.getResponseStatusDesc()
													)))?true:false;	
													
							t.setTiraResponseId(req.getMotorCoverNoteRefRes().getFleetResHdr().getResponseId());
							t.setResponseStatusCode(fleetResDtl.getResponseStatusCode());
							t.setResponseStatusDesc(fleetResDtl.getResponseStatusDesc());
							if(status) {
								t.setCoverNoteReferenceNo(fleetResDtl.getCoverNoteReferenceNumber());
								t.setStickerNumber(fleetResDtl.getStickerNumber());
							}		
						}
						
					});
				}
				sectionDataRepo.saveAll(sections);
				 
				
				HomePositionMaster hm = homePositionRepo.findByTiraRequestIdAndQuoteNo(req.getMotorCoverNoteRefRes().getFleetResHdr().getRequestId(),previousOne.getPolicyNo());
				if(hm!=null) {
					
					Long successCount=sections.stream().filter(t -> !StringUtils.isBlank(t.getCoverNoteReferenceNo())).count();
					
					hm.setTiraResponseId(req.getMotorCoverNoteRefRes().getFleetResHdr().getResponseId());
					hm.setResponseStatusCode((successCount.intValue()==sections.size())?"TIRA001":"MAAN001");
					hm.setResponseStatusDesc((successCount.intValue()==sections.size())?"FLEET POSTED":"SOME VEHICLE IS NOT POSTED");
					if(status) {
						hm.setCoverNoteReferenceNo(sections.get(0).getCoverNoteReferenceNo());
						hm.setStickerNumber(sections.get(0).getStickerNumber());
					}					
					homePositionRepo.save(hm);
				}
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return ackResponse;
	}

	
	@Autowired 
	private JdbcTemplate jdbcTemplate;
	
	public void checkBodyType(TiraMsg req, MotorTiraMsgRes tiraRes) {
		try{
			if(tiraRes.getMotorVerificationRes().getVerificationDtl()!=null && StringUtils.isNotBlank(tiraRes.getMotorVerificationRes().getVerificationDtl().getBodyType()) ) {
				String checkQuery = "SELECT * FROM eway_motor_bodytype_master WHERE company_id='100002' AND UPPER(body_name_en)=UPPER('"+tiraRes.getMotorVerificationRes().getVerificationDtl().getBodyType()+"')";

				String newbodyId = jdbcTemplate.queryForObject("SELECT MAX(body_id)+1 FROM eway_motor_bodytype_master WHERE company_id='100002' AND body_id!='99999'",String.class); 


				String duplicateQuery = "INSERT INTO eway_motor_bodytype_master (BODY_ID, SECTION_ID, BRANCH_CODE, COMPANY_ID, AMEND_ID,"
						+ " EFFECTIVE_DATE_START, EFFECTIVE_DATE_END, BODY_NAME_EN, STATUS, ENTRY_DATE, REMARKS, SEATING_CAPACITY, TONNAGE, "
						+ "CYCLINDERS, CREATED_BY, UPDATED_BY, UPDATED_DATE, REGULATORY_CODE, CORE_APP_CODE, BODY_TYPE, BODY_NAME_LOCAL, COLOR_CODE_LOCAL, COLOR_DESC_LOCAL) " 
						+ "SELECT "+newbodyId+" BODY_ID, SECTION_ID, BRANCH_CODE, COMPANY_ID, AMEND_ID,sysdate() EFFECTIVE_DATE_START, EFFECTIVE_DATE_END,'"+tiraRes.getMotorVerificationRes().getVerificationDtl().getBodyType()+"' BODY_NAME_EN, STATUS, ENTRY_DATE, REMARKS, SEATING_CAPACITY,"
						+ " TONNAGE, CYCLINDERS, CREATED_BY, UPDATED_BY, UPDATED_DATE, REGULATORY_CODE, CORE_APP_CODE, BODY_TYPE, BODY_NAME_LOCAL, COLOR_CODE_LOCAL, COLOR_DESC_LOCAL "
						+ "FROM eway_motor_bodytype_master WHERE body_id='99999'";
				// Execute the first query to check for records
				List<Map<String, Object>> result = jdbcTemplate.queryForList(checkQuery); 
				// Check if no records are returned
				if (result.isEmpty()) { 
					// Execute the duplicate query to insert data
					jdbcTemplate.update(duplicateQuery);

					String insertQuery= "INSERT INTO eway_motor_makemodel_master ("+
							"MAKE_ID, MODEL_ID, BODY_ID, COMPANY_ID, BRANCH_CODE, AMEND_ID, VEHICLEMODELCODE, STATUS, MAKE_NAME_EN, MODEL_NAME_EN, BODY_NAME_EN, VEH_CLASS, VEH_CLASS_EN, VEH_MANF_COUNTRY,"+ 
							"VEH_MANF_COUNTRY_EN, VEH_MANF_REGION, VEH_MANF_REGION_EN, VEH_CC, VEH_WEIGHT, VEH_FUELTYPE, CORE_MAKE_ID, CORE_MODEL_ID, CORE_BODY_ID, CORE_REF_NO, VEHICLE_TYPE_AR, MAKE_NAME_AR,"+
							" MODEL_NAME_AR, OTHR_MAKE_ID_1, OTHR_MODEL_ID_1, OTHR_BODY_ID_1, OTHR_MAKE_ID_2, OTHR_MODEL_ID_2, OTHR_BODY_ID_2, REF_NO, BATCH_ID, ENTRY_DATE, ENTRY_MODE, UPLOADED_BY, PREMIA_CODE,"+ 
							"MODEL_ID_OLD, CORE_APP_CODE, TPLRATE, BASERATE, NETRATE, EFFECTIVE_DATE_END, EFFECTIVE_DATE_START, REMARKS, OBSOLETE_FLAG, ROP_BODYID, REGULATORY_CODE, CREATED_BY, "+
							"UPDATED_BY, UPDATED_DATE, SECTION_ID, VEHICLE_VALUE, EXCESS, YOUNG_DRIVER_EXCESS, LOSS_OF_USE_DAYS, LOSS_OF_USE_VALUE, MAKE_NAME_LOCAL, BODY_NAME_LOCAL, MODEL_NAME_LOCAL,"+ 
							"VEH_CLASS_LOCAL, VEH_MANF_COUNTRY_LOCAL, VEH_MANF_REGION_LOCAL	 ) select MAKE_ID, MODEL_ID,'"+newbodyId+"' BODY_ID, COMPANY_ID, BRANCH_CODE, AMEND_ID, VEHICLEMODELCODE, STATUS, MAKE_NAME_EN, MODEL_NAME_EN, BODY_NAME_EN, VEH_CLASS, VEH_CLASS_EN, VEH_MANF_COUNTRY,"+ 
							"VEH_MANF_COUNTRY_EN, VEH_MANF_REGION, VEH_MANF_REGION_EN, VEH_CC, VEH_WEIGHT, VEH_FUELTYPE, CORE_MAKE_ID, CORE_MODEL_ID, CORE_BODY_ID, CORE_REF_NO, VEHICLE_TYPE_AR, MAKE_NAME_AR,"+
							"MODEL_NAME_AR, OTHR_MAKE_ID_1, OTHR_MODEL_ID_1, OTHR_BODY_ID_1, OTHR_MAKE_ID_2, OTHR_MODEL_ID_2, OTHR_BODY_ID_2, REF_NO, BATCH_ID, ENTRY_DATE, ENTRY_MODE, UPLOADED_BY, PREMIA_CODE,"+ 
							"MODEL_ID_OLD, CORE_APP_CODE, TPLRATE, BASERATE, NETRATE, EFFECTIVE_DATE_END, EFFECTIVE_DATE_START, REMARKS, OBSOLETE_FLAG, ROP_BODYID, REGULATORY_CODE, CREATED_BY, "+
							"UPDATED_BY, UPDATED_DATE, SECTION_ID, VEHICLE_VALUE, EXCESS, YOUNG_DRIVER_EXCESS, LOSS_OF_USE_DAYS, LOSS_OF_USE_VALUE, MAKE_NAME_LOCAL, BODY_NAME_LOCAL, MODEL_NAME_LOCAL,"+ 
							"VEH_CLASS_LOCAL, VEH_MANF_COUNTRY_LOCAL, VEH_MANF_REGION_LOCAL FROM eway_motor_makemodel_master WHERE company_id='100002' AND make_id='99999'";
					jdbcTemplate.update(insertQuery);



				}

			}
		}catch (Exception e) {
			e.printStackTrace();;
		}
		
	}
	@Autowired
	private MotorDataDetailsRepository mddRepo;


	public void correctData(TiraMsgAcknowlege req, TiraTrackingDetails previousOne) {
		
	    final int MAX_REHIT_ATTEMPTS = 5;
	    String responseCode = req.getMotorCoverNoteRefRes().getResponseStatusCode();
	    String policyNo     = previousOne.getPolicyNo();

	    int attemptCount = tiraTrackRepo.countAcknowledgeAttemptsByPolicyAndStatusCode(
	                           policyNo, responseCode);

	    if (attemptCount >= MAX_REHIT_ATTEMPTS) {
	        log.warn("[TIRA] Max rehit attempts ({}) reached for policyNo={}, statusCode={}. Stopping.",
	                 MAX_REHIT_ATTEMPTS, policyNo, responseCode);
	        return; 
	    }
		
		Long interval=1L;
		String quoteNo=previousOne.getPolicyNo();
		String vehicleId=previousOne.getVehicleId().toString();
		Boolean needToReHit=false;
		try {
			
				if("TIRA010".equals(req.getMotorCoverNoteRefRes().getResponseStatusCode())) {
					interval=60L;	
					needToReHit=true;
				}else if("TIRA020".equals(req.getMotorCoverNoteRefRes().getResponseStatusCode())) {
					HomePositionMaster hm = homePositionRepo.findByQuoteNo(quoteNo);
					String coverNoteType=StringUtils.isNotBlank(hm.getEndtTypeId())?"3":(StringUtils.isNotBlank(hm.getRenewalStatus()) ?"2":"1");
				 
					if("3".equals(coverNoteType) && "842".equals(hm.getEndtTypeId())) {
						hm.setEndorsementEffdate(new Date());						
					}else {
					 	hm.setInceptionDate(new Date());
						hm.setExpiryDate(addDays(new Date(),Integer.parseInt(hm.getPolicyTerm())));
					}
					homePositionRepo.save(hm);
					needToReHit=true;
				}else if("TIRA024".equals(req.getMotorCoverNoteRefRes().getResponseStatusCode())) {
					interval=2L;
					needToReHit=true;
				}else if("TIRA126".equals(req.getMotorCoverNoteRefRes().getResponseStatusCode())) {
					HomePositionMaster hm = homePositionRepo.findByQuoteNo(quoteNo);
					jdbcTemplate.update("update personal_info set policy_holder_typeid='3',Id_Type='3' where customer_id='"+ hm.getCustomerId()+"'");
					needToReHit=true;
				}else if("TIRA125".equals(req.getMotorCoverNoteRefRes().getResponseStatusCode())) {
					HomePositionMaster hm = homePositionRepo.findByQuoteNo(quoteNo);
					jdbcTemplate.update("update personal_info set policy_holder_type='1' where customer_id='"+ hm.getCustomerId()+"'");
					needToReHit=false;
				}else if("TIRA136".equals(req.getMotorCoverNoteRefRes().getResponseStatusCode())) {
					
					
					MotorDataDetails mdd = mddRepo.findByQuoteNoAndVehicleId(quoteNo,vehicleId);
					jdbcTemplate.update("UPDATE motor_vehicle_info SET RES_OWNER_CATEGORY ='1' WHERE reg_reg_number='"+ mdd.getRegistrationNumber()+"' and company_id='100002'");
					HomePositionMaster hm = homePositionRepo.findByQuoteNo(quoteNo);
					jdbcTemplate.update("update personal_info set policy_holder_type='1' where customer_id='"+ hm.getCustomerId()+"'");
					needToReHit=true;
				}else if("TIRA233".equals(req.getMotorCoverNoteRefRes().getResponseStatusCode())) {
					// No Actoin
					needToReHit=false;
				}else if("TIRA132".equals(req.getMotorCoverNoteRefRes().getResponseStatusCode())) {
					HomePositionMaster hm = homePositionRepo.findByQuoteNo(quoteNo);
					jdbcTemplate.update("update personal_info set mobile_no_1=SUBSTRING(mobile_no_1, 2)  where customer_id='"+ hm.getCustomerId()+"'");
					needToReHit=true;
				}else if("TIRA023".equals(req.getMotorCoverNoteRefRes().getResponseStatusCode())) {
					needToReHit=true;
					HomePositionMaster hm = homePositionRepo.findByQuoteNo(quoteNo);
					hm.setBrokerTiraCode("ICC110");
					hm.setSalePointCode("SP500");
					homePositionRepo.save(hm);
				}else if("TIRA122".equals(req.getMotorCoverNoteRefRes().getResponseStatusCode())) {
					needToReHit=true;
					HomePositionMaster hm = homePositionRepo.findByQuoteNo(quoteNo);
					jdbcTemplate.update("update personal_info set policy_holder_typeid='3',Id_Type='3' where customer_id='"+ hm.getCustomerId()+"'");
				}
				
				else {
					needToReHit=false;
				}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			
			if(needToReHit)
				reHit(quoteNo,interval);
		}
		
		
	}

	public static Date addDays(Date date, int days) {
		{ // Create a Calendar instance
			Calendar calendar = Calendar.getInstance();
			// Set the date
			calendar.setTime(date);
			// Add the specified number of days
			calendar.add(Calendar.DAY_OF_YEAR, days);
			// Return the new date 
			return calendar.getTime();
		}
	}
	@Autowired
	private AuthendicationService authservice;
	@Value(value = "${push.tira}")
	private String pushTiraLink;

	private void reHit(String quoteNo,final long waitngLimit) {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				try {					
					LoginRequest mslogin=new LoginRequest();
					mslogin.setLoginId("guest");
					mslogin.setPassword("Admin@01");
					mslogin.setReLoginKey("Y");
					CommonLoginRes checkUserLogin = authservice.checkUserLogin(mslogin,null);
					ClaimLoginResponse commonResponse =(ClaimLoginResponse) checkUserLogin.getCommonResponse();
					if(commonResponse!=null) {
						String tokeen = commonResponse.getToken();

						RestTemplate temp = new RestTemplate();
						HttpHeaders header = new HttpHeaders();
						header.setContentType(MediaType.APPLICATION_JSON);
						// header.setCharset("UTF-8");
						header.setBearerAuth(tokeen);

						String url = pushTiraLink;

						Map<String,String> tiraReq =new HashMap<String,String>();
						tiraReq.put("QuoteNo", quoteNo);
						tiraReq.put("RiskId", "");
						HttpEntity<?> requestent = new HttpEntity<>(tiraReq, header);

						System.out.println(new Date() + " Start " + url);
						ResponseEntity<Object> postEntity = temp.exchange(url, HttpMethod.POST, requestent,new ParameterizedTypeReference<Object>() {}) ;

						System.out.println("REHIT End"+postEntity.getBody());
					}


				}catch (Exception e) {
					e.printStackTrace();
				}
			};

		};
		Thread th=new Thread(runnable);		
		
		try {
			//Thread.sleep(1000*waitngLimit);
			System.out.println("REHIT triggerd "+new Date());
			
			System.out.println("REHIT Start "+new Date());
			th.start();
			//th.wait(1000*waitngLimit);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	}
	
}
