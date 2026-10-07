package com.maan.eway.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maan.eway.bean.DMVICToken;
import com.maan.eway.bean.DMVICTrancationLog;
import com.maan.eway.bean.DMVICVehicleDetails;
import com.maan.eway.bean.DMVICVehicleInfoDetails;
import com.maan.eway.bean.EserviceBuildingDetails;
import com.maan.eway.bean.EserviceDoctordetails;
import com.maan.eway.bean.HomePositionMaster;
import com.maan.eway.bean.ListItemValue;
import com.maan.eway.bean.PersonalInfo;
import com.maan.eway.repository.DMVICTokenRepository;
import com.maan.eway.repository.DMVICTrancationLogRepository;
import com.maan.eway.repository.DMVICVehicleDetailsRepository;
import com.maan.eway.repository.DMVICVehicleInfoDetailsRepository;
import com.maan.eway.repository.EServiceBuildingDetailsRepository;
import com.maan.eway.repository.EserviceDoctordetailsRepository;
import com.maan.eway.repository.HomePositionMasterRepository;
import com.maan.eway.repository.ListItemValueRepository;
import com.maan.eway.repository.PersonalInfoRepository;
import com.maan.eway.req.DMVICRequest;
import com.maan.eway.req.GetCertificateReq;
import com.maan.eway.req.PolicyIssuanceDto;
import com.maan.eway.req.ValidateKMPDCRequest;
import com.maan.eway.res.DMVICResponse;
import com.maan.eway.res.DoubleInsurance;
import com.maan.eway.res.DoubleInsuranceCallObj;
import com.maan.eway.res.GetCertificateRes;
import com.maan.eway.res.ValidateDoubleInsuranceRes;
import com.maan.eway.service.DMVICService;

@Service
public class DMVICServiceImpl implements DMVICService {

	private Logger log = LogManager.getLogger(DMVICServiceImpl.class);

	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	@Value(value = "${generateToken}")
	private String generateToken;

	@Value(value = "${DoubleInsuranceLink}")
	private String doubleInsuranceLink;

	@Value(value = "${PremiaDoubleInsuranceToken}")
	private String premiaDoubleInsuranceToken;

	@Value(value = "${firtsAssuranceFile}")
	private String firtsAssuranceFile;

	@Value(value = "${passWord}")
	private String passWord;

	@Value(value = "${kmpdcValidation}")
	private String kmpdcValidation;

	@Value(value = "${userName}")
	private String userName;

	@Value(value = "${userPassWord}")
	private String userPassWord;

	@Value(value = "${clientId}")
	private String clientId;

	@Value(value = "${integrationKey}")
	private String integrationKey;

	@Value(value = "${integrationReference}")
	private String integrationReference;

	@Value(value = "${issueProfessionalDoctor}")
	private String issueProfessionalDoctor;

	@Value(value = "${getCertificate}")
	private String getCertificate;

	@Value(value = "${vehicleSearch}")
	private String vehicleSearch;

	@Value(value = "${vehicleCertificate}")
	private String vehicleCertificate;

	@Autowired
	private DMVICTokenRepository dmvicTokenRepo;

	@Autowired
	private DMVICVehicleDetailsRepository dmvicVehicleDetailsRepo;

	@Autowired
	private DMVICTrancationLogRepository dmvicTrancationLogRepos;

	@Autowired
	private EserviceDoctordetailsRepository eserviceDoctordetailsRepos;

	@Autowired
	private HomePositionMasterRepository homePositionMasterRepos;

	@Autowired
	private PersonalInfoRepository personalInfoRepo;

	@Autowired
	private ListItemValueRepository listItemValueRepos;

	@Autowired
	private EServiceBuildingDetailsRepository eserviceBuildingDetailsRepo;

	@Autowired
	private EserviceDoctordetailsRepository eserviceDoctordetailsRepo;

	@Autowired
	private DMVICVehicleInfoDetailsRepository dmvicVehicleInfoDetailsRepo;

	@Override
	public ValidateDoubleInsuranceRes checkDMVIC(DMVICRequest req) {
		ValidateDoubleInsuranceRes res = new ValidateDoubleInsuranceRes();
		try {
			DMVICRequest req1 = new DMVICRequest();
			DozerBeanMapper dozerMapper = new DozerBeanMapper();
			dozerMapper.map(req, req1);
			String vehicleregistrationnumber = req.getVehicleregistrationnumber().trim();
			req1.setVehicleregistrationnumber(vehicleregistrationnumber);
			String token = null;
			ResponseEntity<String> response = null;
			ResponseEntity<String> response1 = null;
			ResponseEntity<DMVICResponse> response2 = null;
			Date curDate = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(curDate);
			cal.add(Calendar.DAY_OF_MONTH, 1);
			Date expireDate = cal.getTime();
			SimpleDateFormat sdformat = new SimpleDateFormat("dd/MM/YYYY");
			Date policyStartDate = sdformat.parse(req1.getPolicystartdate());
			List<DMVICVehicleDetails> dmvicVehicledetailsList = new ArrayList<>();
			List<DMVICToken> dmvicTokenList = dmvicTokenRepo.findByEntryDateBeforeAndExpireDateAfter(curDate, curDate);

			RestTemplate temp = new RestTemplateBuilder().setConnectTimeout(Duration.ofSeconds(5))
					.setReadTimeout(Duration.ofMinutes(2)).build();

			if (dmvicTokenList.isEmpty()) {
				HttpHeaders header = new HttpHeaders();
				header.setContentType(MediaType.APPLICATION_JSON);
				Map<String, String> requestBody = new HashMap<>();
				requestBody.put("username", "admin");
				requestBody.put("password", "admin");
				HttpEntity<?> requestent = new HttpEntity<>(requestBody, header);
				System.out.println("Calling Generate Token Api:  " + requestent);
				try {
					response = temp.exchange(generateToken, HttpMethod.POST, requestent, String.class);

					System.out.println("response Api:  " + response.getBody());
				} catch (RestClientException e) {
					if (e.getCause() instanceof ConnectException) {
						System.out.println("Connection refused: Unable to connect to the server at " + response);
						res.setErrorMessage("Connection refused: Unable to connect to the Premia server");
						res.setMassage("Failure");
						res.setSuccess(false);
					} else {
						System.out.println(
								"An error occurred while making the Validate Insurance Token call: " + e.getMessage());
						res.setErrorMessage("An error occurred while making the Validate Insurance Token call");
						res.setMassage("Failure");
						res.setSuccess(false);
					}
				}

				HttpHeaders header1 = new HttpHeaders();
				header1.setContentType(MediaType.APPLICATION_JSON);
				header1.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
				header1.setBearerAuth(response.getBody());
				Map<String, Object> inParams = new HashMap<>();
				inParams.put("Key", "value");
				Map<String, Object> body = new HashMap<>();
				body.put("procedureName", "WNPRC_DMVIC_TOKEN");
				body.put("inParams", inParams);
				HttpEntity<?> requestent1 = new HttpEntity<>(body, header1);
				System.out.println("Calling Premia Token Api:  " + body);
				try {
					response1 = temp.exchange(premiaDoubleInsuranceToken, HttpMethod.POST, requestent1, String.class);

					System.out.println("response Api:  " + response1.getBody());
				} catch (RestClientException e) {
					if (e.getCause() instanceof ConnectException) {
						System.out.println("Connection refused: Unable to connect to the server at " + response1);
						res.setErrorMessage("Connection refused: Unable to connect to the server");
						res.setMassage("Failure");
						res.setSuccess(false);
					} else {
						System.out.println(
								"An error occurred while making the Validate Insurance Token call: " + e.getMessage());
						res.setErrorMessage("An error occurred while making the Validate Insurance Token call");
						res.setMassage("Failure");
						res.setSuccess(false);
					}
				}
				if (response1 != null && response1.getBody() != null) {
					ObjectMapper mapper = new ObjectMapper();
					JsonNode root = mapper.readTree(response1.getBody());
					token = root.path("Data").path("P_TOKEN").asText();
					System.out.println("P_TOKEN: " + token);
					if (token != null && !token.isEmpty()) {
						DMVICToken dmvic = new DMVICToken();
						dmvic.setCompanyId(req1.getCompanyId() != null ? req1.getCompanyId() : "100020");
						dmvic.setEntryDate(curDate);
						dmvic.setExpireDate(expireDate);
						dmvic.setStatus("Y");
						dmvic.setToken(token);
						dmvicTokenRepo.save(dmvic);
					}
				}

			} else {
				token = dmvicTokenList.get(0).getToken();
			}

			List<DMVICVehicleDetails> dmvicVehicleDetails = dmvicVehicleDetailsRepo
					.findByRegistrationNumberAndPolicyDate(vehicleregistrationnumber, policyStartDate);
			if (dmvicVehicleDetails.isEmpty()) {
				HttpHeaders header2 = new HttpHeaders();
				header2.setContentType(MediaType.APPLICATION_JSON);
				header2.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
				header2.setBearerAuth(token);
				header2.set("ClientID", "A6B04E78-AD72-4B6D-9D55-72B62698FF8C");
				HttpEntity<?> requestent2 = new HttpEntity<>(req1, header2);
				System.out.println("calling Validate Double Insurance Api:  " + requestent2);
				try {
					response2 = temp.exchange(doubleInsuranceLink, HttpMethod.POST, requestent2, DMVICResponse.class);
					System.out.println("Validate Double Insurance response Api:  " + response2.getBody());

				} catch (RestClientException e) {
					if (e.getCause() instanceof ConnectException) {
						System.out.println("Connection refused: Unable to connect to the server at " + response2);
						res.setErrorMessage("Connection refused: Unable to connect to the server");
						res.setMassage("Failure");
						res.setSuccess(false);
					} else {
						System.out.println(
								"An error occurred while making the Validate Double Insurance call: " + e.getMessage());
						res.setErrorMessage("An error occurred while making the Validate Double Insurance call");
						res.setMassage("Failure");
						res.setSuccess(false);
					}
				}
				if (response2 != null && response2.getBody() != null) {
					if (response2.getBody().getSuccess() && response2.getBody().getError().isEmpty()) {
						res.setResponse(response2.getBody().getCallbackObj());
						res.setMassage("Failure");
						res.setSuccess(false);
						res.setErrorMessage("Already active certificate exist for this vehicle");
						DoubleInsuranceCallObj callbackObj = response2.getBody().getCallbackObj();
						List<DoubleInsurance> doubleInsurance = callbackObj.getDoubleInsurance();
						for (DoubleInsurance dob : doubleInsurance) {
							if (dob.getRegistrationNumber() != null) {
								DMVICVehicleDetails dmvicVehicleDe = new DMVICVehicleDetails();
								dmvicVehicleDe.setChassisNumber(
										dob.getChassisNumber() != null ? dob.getChassisNumber() : null);
								dmvicVehicleDe.setCoverEndDate(
										dob.getCoverEndDate() != null ? sdformat.parse(dob.getCoverEndDate()) : null);
								dmvicVehicleDe.setInsuranceCertificateNo(dob.getInsuranceCertificateNo());
								dmvicVehicleDe.setMemberCompanyName(dob.getMemberCompanyName());
								dmvicVehicleDe.setPolicyEndDate(
										dob.getCoverEndDate() != null ? sdformat.parse(dob.getCoverEndDate()) : null);
								dmvicVehicleDe.setPolicyStartDate(policyStartDate);
								dmvicVehicleDe.setRegistrationNumber(
										dob.getRegistrationNumber() != null ? dob.getRegistrationNumber() : null);
								dmvicVehicledetailsList.add(dmvicVehicleDe);
							}
						}
						if (dmvicVehicledetailsList != null && !dmvicVehicledetailsList.isEmpty()) {
							dmvicVehicleDetailsRepo.saveAll(dmvicVehicledetailsList);
						}

					} else {
						res.setResponse("");
						res.setMassage("Success");
						res.setSuccess(true);
						res.setErrorMessage("");
					}
				} else {
					System.out.println("An error occurred while making the Validate Double Insurance call");
					res.setErrorMessage("An error occurred while making the Validate Double Insurance call");
					res.setMassage("Failure");
					res.setSuccess(false);
				}
			} else {
				res.setResponse(dmvicVehicleDetails);
				res.setMassage("Failure");
				res.setSuccess(false);
				res.setErrorMessage("Already active certificate exist for this vehicle");
			}

			return res;
		} catch (Exception e) {
			e.printStackTrace();
			res.setErrorMessage("Connection refused: Unable to connect to the server");
			res.setMassage("Failure");
			res.setSuccess(false);
			return res;
		}
	}

	@Override
	public ValidateDoubleInsuranceRes kmpdcValidation(ValidateKMPDCRequest req) {
		ValidateDoubleInsuranceRes res = new ValidateDoubleInsuranceRes();
		try {
			Date today = new Date();
			// Create SSL context using your certificate
			SSLContext sslContext = getSSLSocketFactory(firtsAssuranceFile, passWord);
			SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

			// Open connection
			URL url = new URL(kmpdcValidation);
			log.info("URL: " + url.toString());
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

			String basicAuth = Base64.getEncoder().encodeToString((userName + ":" + userPassWord).getBytes());

			conn.setRequestProperty("Authorization", "Basic " + basicAuth);
			conn.setSSLSocketFactory(sslSocketFactory);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Cache-Control", "no-cache");
			conn.setRequestProperty("ClientID", clientId);
			conn.setRequestProperty("integrationKey", integrationKey);
			conn.setRequestProperty("integrationKeyReference", integrationReference);

			// Send request body
			JSONObject body = new JSONObject();
			body.put("RegNo", req.getRegistrationNumber());

			log.info("REQUEST: " + body);
			try (OutputStream os = conn.getOutputStream()) {
				os.write(body.toString().getBytes());
				os.flush();
			}

			// Read response
			BufferedReader br;
			if (conn.getResponseCode() == 200) {
				res.setSuccess(true);
				br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			} else {
				res.setSuccess(false);
				br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			}

			String output;
			JSONObject resposne;
			for (resposne = null; (output = br.readLine()) != null; resposne = new JSONObject()) {
				JSONParser parser = new JSONParser();
				JSONObject jsonObject = (JSONObject) parser.parse(output);
				res.setResponse(jsonObject);
				log.info("Response output :" + output);
				Map<String, Object> registration = (Map<String, Object>) ((Map<String, Object>) jsonObject.get("rObj"))
						.get("registration");

				insertEserviceDoctordetails(registration, req);

				DMVICTrancationLog dmvicTrancationLog = new DMVICTrancationLog();
				dmvicTrancationLog.setDmvicUrl(kmpdcValidation);
				dmvicTrancationLog.setEntryDate(today);
				dmvicTrancationLog.setRequest(body.toJSONString());
				dmvicTrancationLog.setResponse(jsonObject.toJSONString());
				dmvicTrancationLog.setRegNo(req.getRegistrationNumber());
				dmvicTrancationLogRepos.save(dmvicTrancationLog);
			}
			conn.disconnect();
		} catch (Exception e) {
			DMVICTrancationLog dmvicTrancationLog = new DMVICTrancationLog();
			dmvicTrancationLog.setDmvicUrl(kmpdcValidation);
			dmvicTrancationLog.setEntryDate(new Date());
			dmvicTrancationLog.setRequest(req.getRegistrationNumber());
			dmvicTrancationLog.setResponse("");
			dmvicTrancationLog.setRegNo(req.getRegistrationNumber());
			dmvicTrancationLogRepos.save(dmvicTrancationLog);
			e.printStackTrace();
		}

		return res;
	}

	@Override
	public ValidateDoubleInsuranceRes issuePDCert(String policyNo) {
		ValidateDoubleInsuranceRes res = new ValidateDoubleInsuranceRes();
		PolicyIssuanceDto policyRequest = new PolicyIssuanceDto();
		String requestreferenceNo = null;
		try {

			Date today = new Date();
			HomePositionMaster homePositionMaster = homePositionMasterRepos.findByPolicyNo(policyNo);
			requestreferenceNo = homePositionMaster.getRequestReferenceNo();
			PersonalInfo personalInfo = personalInfoRepo.findByCustomerId(homePositionMaster.getCustomerId());
			List<ListItemValue> listItemValueList = listItemValueRepos
					.findByItemTypeAndStatusAndCompanyIdOrderByItemCodeDesc("Profession", "Y",
							homePositionMaster.getCompanyId());

			EserviceBuildingDetails eserviceBuildingDetails = eserviceBuildingDetailsRepo
					.findByRequestReferenceNoAndCoverId(requestreferenceNo, 631);
			if (eserviceBuildingDetails != null) {

				EserviceDoctordetails eserviceDoctordetails = eserviceDoctordetailsRepo
						.findByregNo(eserviceBuildingDetails.getContentId());
				if (eserviceDoctordetails.getSpecialty() != null && !eserviceDoctordetails.getSpecialty().isEmpty()) {
					List<ListItemValue> collect = listItemValueList.stream().filter(
							f -> f.getItemValue().equalsIgnoreCase(eserviceDoctordetails.getSpecialty().toLowerCase()))
							.collect(Collectors.toList());
					if (collect != null && !collect.isEmpty() && collect.get(0).getItemCode() != null) {
						System.out.println(" ------------------------------" + collect.get(0).getItemCode());
						policyRequest.setSpecilisationID(Integer.valueOf(collect.get(0).getItemCode()));
					} else {
						policyRequest.setSpecilisationID(16);
					}

				} else {
					policyRequest.setSpecilisationID(16);
				}

				policyRequest.setInsuredPIN(personalInfo.getKraPin());
				policyRequest.setKmpdcRegNo(eserviceDoctordetails.getRegNo());
				policyRequest.setMemberCompanyID(20);
				policyRequest.setPolicyholder(personalInfo.getClientName());
				policyRequest.setPolicyHolderEmailId(personalInfo.getEmail1());
				policyRequest.setPolicyNumber(policyNo);
				policyRequest.setFromDate(sdf.format(homePositionMaster.getInceptionDate()));
				policyRequest.setToDate(sdf.format(homePositionMaster.getExpiryDate()));
				policyRequest.setProfession("Doctor");

				policyRequest.setAllclaims(Integer.valueOf(eserviceBuildingDetails.getIndemityPeriod()));
				policyRequest.setAnyoneclaims(Integer.valueOf(eserviceBuildingDetails.getIndemityPeriod()));
				policyRequest.setDishonesty(Integer.valueOf(eserviceBuildingDetails.getIndemityPeriod()) * 10);
				policyRequest.setDocuments(Integer.valueOf(eserviceBuildingDetails.getIndemityPeriod()) * 10);
				policyRequest.setSlandar(Integer.valueOf(eserviceBuildingDetails.getIndemityPeriod()) * 10);
				policyRequest.setExcess("1% Excess");
				policyRequest.setGroupID(eserviceBuildingDetails.getOccupationType() != null
						&& !eserviceBuildingDetails.getOccupationType().isEmpty()
								? Integer.valueOf(eserviceBuildingDetails.getOccupationType())
								: 2);

				policyRequest.setSubSpecilisationID(0);

				// Create SSL context using your certificate
				SSLContext sslContext = getSSLSocketFactory(firtsAssuranceFile, passWord);
				SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

				// Open connection
				URL url = new URL(issueProfessionalDoctor);
				log.info("URL: " + url.toString());
				HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

				String basicAuth = Base64.getEncoder().encodeToString((userName + ":" + userPassWord).getBytes());

				conn.setRequestProperty("Authorization", "Basic " + basicAuth);
				conn.setSSLSocketFactory(sslSocketFactory);
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/json");
				conn.setRequestProperty("Cache-Control", "no-cache");
				conn.setRequestProperty("ClientID", clientId);
				conn.setRequestProperty("integrationKey", integrationKey);
				conn.setRequestProperty("integrationKeyReference", integrationReference);

				ObjectMapper mapper = new ObjectMapper();
				JSONObject body = new JSONObject(mapper.convertValue(policyRequest, Map.class));
				log.info("REQUEST: " + body);
				try (OutputStream os = conn.getOutputStream()) {
					os.write(body.toString().getBytes());
					os.flush();
				}

				// Read response
				BufferedReader br;
				if (conn.getResponseCode() == 200) {
					res.setSuccess(true);
					br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				} else {
					res.setSuccess(false);
					br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
				}

				String output;
				JSONObject resposne;
				for (resposne = null; (output = br.readLine()) != null; resposne = new JSONObject()) {
					JSONParser parser = new JSONParser();
					JSONObject jsonObject = (JSONObject) parser.parse(output);
					res.setResponse(jsonObject);
					log.info("Response output :" + output);

					Map<String, Object> issuance = (Map<String, Object>) ((Map<String, Object>) jsonObject.get("rObj"))
							.get("Issuance");
					homePositionMaster.setActualCNo(getValue(issuance, "actualCNo"));
					homePositionMaster.setTransactionNo(getValue(issuance, "transactionNo"));
					homePositionMasterRepos.save(homePositionMaster);
					DMVICTrancationLog dmvicTrancationLog = new DMVICTrancationLog();
					dmvicTrancationLog.setDmvicUrl(issueProfessionalDoctor);
					dmvicTrancationLog.setEntryDate(today);
					dmvicTrancationLog.setRequest(body.toJSONString());
					dmvicTrancationLog.setResponse(jsonObject.toJSONString());
					dmvicTrancationLog.setPolicyNo(policyNo);
					dmvicTrancationLog.setRegNo(requestreferenceNo);
					dmvicTrancationLogRepos.save(dmvicTrancationLog);
				}
				System.out.println("response :" + resposne.toString());
				conn.disconnect();
				return res;
			}
		} catch (Exception e) {
			DMVICTrancationLog dmvicTrancationLog = new DMVICTrancationLog();
			dmvicTrancationLog.setDmvicUrl(issueProfessionalDoctor);
			dmvicTrancationLog.setEntryDate(new Date());
			dmvicTrancationLog.setRequest(policyRequest.toString());
			dmvicTrancationLog.setResponse("");
			dmvicTrancationLog.setPolicyNo(policyNo);
			dmvicTrancationLog.setRegNo(requestreferenceNo);
			dmvicTrancationLogRepos.save(dmvicTrancationLog);
			e.printStackTrace();
		}
		return res;
	}

	private static SSLContext getSSLSocketFactory(String PFX_location, String PFX_Password) throws Exception {
		SSLContext context = null;
		String CLIENT_KEYSTORE_TYPE = "PKCS12";
		File pKeyFile = new File(PFX_location);
		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		InputStream keyInput = new FileInputStream(pKeyFile);
		keyStore.load(keyInput, PFX_Password.toCharArray());
		keyInput.close();
		keyManagerFactory.init(keyStore, PFX_Password.toCharArray());
		context = SSLContext.getInstance("TLS");
		context.init(keyManagerFactory.getKeyManagers(), (TrustManager[]) null, new SecureRandom());
		return context;
	}

	private void insertEserviceDoctordetails(Map<String, Object> registration, ValidateKMPDCRequest req) {
		try {
			EserviceDoctordetails existEserviceDoctordetails = eserviceDoctordetailsRepos
					.findByregNo(getValue(registration, "regNo"));
			if (existEserviceDoctordetails == null) {
				Date today = new Date();
				EserviceDoctordetails eserviceDoctordetails = new EserviceDoctordetails();
				eserviceDoctordetails.setCadre(getValue(registration, "cadre"));
				eserviceDoctordetails.setEntryDate(today);
				eserviceDoctordetails.setFullName(getValue(registration, "fullNames"));
				eserviceDoctordetails.setGender(getValue(registration, "gender"));
				eserviceDoctordetails.setQualifications(getValue(registration, "qualifications"));
				eserviceDoctordetails.setRegNo(getValue(registration, "regNo"));
				eserviceDoctordetails.setSpecialty(getValue(registration, "specialty"));
				eserviceDoctordetails.setSubSpecialty(getValue(registration, "subSpecialty"));
				eserviceDoctordetails.setEmail(getValue(registration, "email"));
				eserviceDoctordetailsRepos.save(eserviceDoctordetails);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getValue(Map<String, Object> map, String key) {
		Object value = map.get(key);
		if (value == null)
			return "";
		String str = value.toString().trim();
		return str.isEmpty() ? "" : str;
	}

	@Override
	public GetCertificateRes getCertificate(GetCertificateReq req) {
		GetCertificateRes res = new GetCertificateRes();
		try {
			Date today = new Date();
			// Create SSL context using your certificate
			SSLContext sslContext = getSSLSocketFactory(firtsAssuranceFile, passWord);
			SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

			// Open connection
			URL url = new URL(getCertificate);
			log.info("URL: " + url.toString());
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

			String basicAuth = Base64.getEncoder().encodeToString((userName + ":" + userPassWord).getBytes());

			conn.setRequestProperty("Authorization", "Basic " + basicAuth);
			conn.setSSLSocketFactory(sslSocketFactory);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Cache-Control", "no-cache");
			conn.setRequestProperty("ClientID", clientId);
			conn.setRequestProperty("integrationKey", integrationKey);
			conn.setRequestProperty("integrationKeyReference", integrationReference);

			// Send request body
			JSONObject body = new JSONObject();
			body.put("certificateNumber", req.getCertificateNumber());

			log.info("REQUEST: " + body);
			try (OutputStream os = conn.getOutputStream()) {
				os.write(body.toString().getBytes());
				os.flush();
			}

			// Read response
			BufferedReader br;
			if (conn.getResponseCode() == 200) {
				res.setSuccess(true);
				br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			} else {
				res.setSuccess(false);
				br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			}

			String output;
			JSONObject resposne;
			for (resposne = null; (output = br.readLine()) != null; resposne = new JSONObject()) {
				System.out.println("Response from API: " + output);
				if (output != null && !output.trim().isEmpty() && !output.contains("No Client Certificate")) {
					JSONParser parser = new JSONParser();
					JSONObject jsonObject = (JSONObject) parser.parse(output);
					log.info("Response output :" + output);
					Map<String, Object> rObj = (Map<String, Object>) ((Map<String, Object>) jsonObject.get("rObj"));
					res.setDownloadURL(rObj.get("blobDownloadURL").toString());
					DMVICTrancationLog dmvicTrancationLog = new DMVICTrancationLog();
					dmvicTrancationLog.setDmvicUrl(getCertificate);
					dmvicTrancationLog.setEntryDate(today);
					dmvicTrancationLog.setRequest(body.toJSONString());
					dmvicTrancationLog.setResponse(jsonObject.toString());
					dmvicTrancationLog.setRegNo(req.getCertificateNumber());
					dmvicTrancationLog.setPolicyNo(req.getPolicyNo());
					dmvicTrancationLogRepos.save(dmvicTrancationLog);
				} else {
					res.setDownloadURL(output);
					DMVICTrancationLog dmvicTrancationLog = new DMVICTrancationLog();
					dmvicTrancationLog.setDmvicUrl(getCertificate);
					dmvicTrancationLog.setEntryDate(new Date());
					dmvicTrancationLog.setRequest(req.getCertificateNumber());
					dmvicTrancationLog.setResponse(output.toString());
					dmvicTrancationLog.setRegNo(req.getCertificateNumber());
					dmvicTrancationLog.setPolicyNo(req.getPolicyNo());
					dmvicTrancationLogRepos.save(dmvicTrancationLog);
				}
				conn.disconnect();
			}
		} catch (Exception e) {
//			DMVICTrancationLog dmvicTrancationLog = new DMVICTrancationLog();
//			dmvicTrancationLog.setDmvicUrl(getCertificate);
//			dmvicTrancationLog.setEntryDate(new Date());
//			dmvicTrancationLog.setRequest(req.getCertificateNumber());
//			dmvicTrancationLog.setResponse(e.getMessage());
//			dmvicTrancationLog.setRegNo(req.getCertificateNumber());
//			dmvicTrancationLog.setPolicyNo(req.getPolicyNo());
//			dmvicTrancationLogRepos.save(dmvicTrancationLog);
			e.printStackTrace();
		}

		return res;
	}

	@Override
	public ValidateDoubleInsuranceRes vehicleSearchDMVIC(DMVICRequest req) {
		ValidateDoubleInsuranceRes res = new ValidateDoubleInsuranceRes();
		try {
			DMVICRequest req1 = new DMVICRequest();
			DozerBeanMapper dozerMapper = new DozerBeanMapper();
			dozerMapper.map(req, req1);
			String vehicleregistrationnumber = req.getVehicleregistrationnumber();
			req1.setVehicleregistrationnumber(vehicleregistrationnumber);
			String token = null;
			ResponseEntity<String> response = null;
			ResponseEntity<String> response1 = null;
			ResponseEntity<String> response2 = null;
			Date curDate = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(curDate);
			cal.add(Calendar.DAY_OF_MONTH, 1);
			Date expireDate = cal.getTime();
			SimpleDateFormat sdformat = new SimpleDateFormat("dd/MM/YYYY");
			List<DMVICToken> dmvicTokenList = dmvicTokenRepo.findByEntryDateBeforeAndExpireDateAfter(curDate, curDate);

			RestTemplate temp = new RestTemplateBuilder().setConnectTimeout(Duration.ofSeconds(5))
					.setReadTimeout(Duration.ofMinutes(2)).build();

			if (dmvicTokenList.isEmpty()) {

				HttpHeaders header = new HttpHeaders();
				header.setContentType(MediaType.APPLICATION_JSON);
				Map<String, String> requestBody = new HashMap<>();
				requestBody.put("username", "admin");
				requestBody.put("password", "admin");
				HttpEntity<?> requestent = new HttpEntity<>(requestBody, header);
				System.out.println("Calling Generate Token Api:  " + requestent);
				try {
					response = temp.exchange(generateToken, HttpMethod.POST, requestent, String.class);

					System.out.println("response Api:  " + response.getBody());
				} catch (RestClientException e) {
					if (e.getCause() instanceof ConnectException) {
						System.out.println("Connection refused: Unable to connect to the server at " + response);
						res.setErrorMessage("Connection refused: Unable to connect to the Premia server");
						res.setMassage("Failure");
						res.setSuccess(false);
					} else {
						System.out.println(
								"An error occurred while making the Validate Insurance Token call: " + e.getMessage());
						res.setErrorMessage("An error occurred while making the Validate Insurance Token call");
						res.setMassage("Failure");
						res.setSuccess(false);
					}
				}

				HttpHeaders header1 = new HttpHeaders();
				header1.setContentType(MediaType.APPLICATION_JSON);
				header1.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
				header1.setBearerAuth(response.getBody());
				Map<String, Object> inParams = new HashMap<>();
				inParams.put("Key", "value");
				Map<String, Object> body = new HashMap<>();
				body.put("procedureName", "WNPRC_DMVIC_TOKEN");
				body.put("inParams", inParams);
				HttpEntity<?> requestent1 = new HttpEntity<>(body, header1);
				System.out.println("Calling Premia Token Api:  " + body);
				try {
					response1 = temp.exchange(premiaDoubleInsuranceToken, HttpMethod.POST, requestent1, String.class);

					System.out.println("response Api:  " + response1.getBody());
				} catch (RestClientException e) {
					if (e.getCause() instanceof ConnectException) {
						System.out.println("Connection refused: Unable to connect to the server at " + response1);
						res.setErrorMessage("Connection refused: Unable to connect to the server");
						res.setMassage("Failure");
						res.setSuccess(false);
					} else {
						System.out.println(
								"An error occurred while making the Validate Insurance Token call: " + e.getMessage());
						res.setErrorMessage("An error occurred while making the Validate Insurance Token call");
						res.setMassage("Failure");
						res.setSuccess(false);
					}
				}
				if (response1 != null && response1.getBody() != null) {
					ObjectMapper mapper = new ObjectMapper();
					JsonNode root = mapper.readTree(response1.getBody());
					token = root.path("Data").path("P_TOKEN").asText();
					System.out.println("P_TOKEN: " + token);
					if (token != null && !token.isEmpty()) {
						DMVICToken dmvic = new DMVICToken();
						dmvic.setCompanyId(req1.getCompanyId() != null ? req1.getCompanyId() : "100020");
						dmvic.setEntryDate(curDate);
						dmvic.setExpireDate(expireDate);
						dmvic.setStatus("Y");
						dmvic.setToken(token);
						dmvicTokenRepo.save(dmvic);
					}
				}

			} else {
				token = dmvicTokenList.get(0).getToken();
			}

			HttpHeaders header2 = new HttpHeaders();
			header2.setContentType(MediaType.APPLICATION_JSON);
			header2.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			header2.setBearerAuth(token);
			header2.set("ClientID", "A6B04E78-AD72-4B6D-9D55-72B62698FF8C");
			HttpEntity<?> requestent2 = new HttpEntity<>(req1, header2);
			System.out.println("Vehicle Search Insurance Api:  " + requestent2);
			try {
				response2 = temp.exchange(vehicleSearch, HttpMethod.POST, requestent2, String.class);
				System.out.println("Vehicle Search response Api:  " + response2.getBody());

			} catch (RestClientException e) {
				if (e.getCause() instanceof ConnectException) {
					System.out.println("Connection refused: Unable to connect to the server at " + response2);
					res.setErrorMessage("Connection refused: Unable to connect to the server");
					res.setMassage("Failure");
					res.setSuccess(false);
				} else {
					System.out.println(
							"An error occurred while making the Vehicle Search Insurance call: " + e.getMessage());
					res.setErrorMessage("An error occurred while making the Vehicle Search Insurance call");
					res.setMassage("Failure");
					res.setSuccess(false);
				}
			}
			if (response2 != null && response2.getBody() != null) {

				ObjectMapper mapper = new ObjectMapper();
				JsonNode root = mapper.readTree(response2.getBody());

				JsonNode callbackObj = root.path("callbackObj");

				JsonNode vehicle = callbackObj.path("Vehicle");
				JsonNode policy = callbackObj.path("PolicyHistory").isArray() ? callbackObj.path("PolicyHistory").get(0)
						: null;

				if (vehicle != null && !vehicle.isMissingNode() && !vehicle.isNull()) {
					DMVICVehicleInfoDetails dmvicVehicleDe = new DMVICVehicleInfoDetails();
					dmvicVehicleDe.setCarryingCapacity(
							vehicle.hasNonNull("CarryingCapacity") ? vehicle.get("CarryingCapacity").asInt() : null);
					dmvicVehicleDe.setRegistrationNumber(vehicle.path("VehicleRegistrationNumber").asText(null));

					dmvicVehicleDe.setTypeOfBody(vehicle.path("BodyType").asText(null));

					dmvicVehicleDe.setYearOfManufacture(vehicle.get("VehicleRegistrationYear").asText(null));
					dmvicVehicleDe.setChassisNumber(vehicle.path("ChassisNumber").asText(null));
					dmvicVehicleDe.setEngineNumber(vehicle.path("EngineNumber").asText(null));
					dmvicVehicleDe.setMake(vehicle.path("VehicleMake").asText(null));
					dmvicVehicleDe.setModel(vehicle.path("VehicleModel").asText(null));

					if (policy != null && !policy.isMissingNode() && !policy.isNull()) {

						dmvicVehicleDe.setPolicyNumber(policy.path("PolicyNumber").asText(null));
						dmvicVehicleDe.setTypeOfCover(policy.path("TypeOfCover").asText(null));
						dmvicVehicleDe.setMemberCompany(policy.path("MemberCompany").asText(null));

						if (policy.hasNonNull("CoverStartDate")
								&& !"Business Confidential".equalsIgnoreCase(policy.get("CoverStartDate").asText())) {
							dmvicVehicleDe.setCoverStartDate(sdformat.parse(policy.get("CoverStartDate").asText()));
						}

						if (policy.hasNonNull("CoverEndDate")
								&& !"Business Confidential".equalsIgnoreCase(policy.get("CoverEndDate").asText())) {
							dmvicVehicleDe.setCoverEndDate(sdformat.parse(policy.get("CoverEndDate").asText()));
						}
					}
					DMVICVehicleInfoDetails dmvicVehicleDetails = dmvicVehicleInfoDetailsRepo
							.findByRegistrationNumber(vehicleregistrationnumber);
					if (dmvicVehicleDetails == null) {
						if (dmvicVehicleDe.getRegistrationNumber() != null) {
							dmvicVehicleInfoDetailsRepo.save(dmvicVehicleDe);
						}
					} else {
						dmvicVehicleDetails.setCarryingCapacity(
								dmvicVehicleDe.getCarryingCapacity() != null ? dmvicVehicleDe.getCarryingCapacity()
										: null);
						dmvicVehicleDetails.setChassisNumber(
								dmvicVehicleDe.getChassisNumber() != null ? dmvicVehicleDe.getChassisNumber() : null);
						dmvicVehicleDetails.setCoverEndDate(
								dmvicVehicleDe.getCoverEndDate() != null ? dmvicVehicleDe.getCoverEndDate() : null);
						dmvicVehicleDetails.setCoverStartDate(
								dmvicVehicleDe.getCoverStartDate() != null ? dmvicVehicleDe.getCoverStartDate() : null);
						dmvicVehicleDetails.setEngineNumber(
								dmvicVehicleDe.getEngineNumber() != null ? dmvicVehicleDe.getEngineNumber() : null);
						dmvicVehicleDetails.setMake(dmvicVehicleDe.getMake() != null ? dmvicVehicleDe.getMake() : null);
						dmvicVehicleDetails.setMemberCompany(
								dmvicVehicleDe.getMemberCompany() != null ? dmvicVehicleDe.getMemberCompany() : null);
						dmvicVehicleDetails
								.setModel(dmvicVehicleDe.getModel() != null ? dmvicVehicleDe.getModel() : null);
						dmvicVehicleDetails.setPolicyNumber(
								dmvicVehicleDe.getPolicyNumber() != null ? dmvicVehicleDe.getPolicyNumber() : null);
//						dmvicVehicleDetails.setRegistrationNumber(
//								dmvicVehicleDe.getRegistrationNumber() != null ? dmvicVehicleDe.getRegistrationNumber()
//										: null);
						dmvicVehicleDetails.setTypeOfBody(
								dmvicVehicleDe.getTypeOfBody() != null ? dmvicVehicleDe.getTypeOfBody() : null);
						dmvicVehicleDetails.setTypeOfCover(
								dmvicVehicleDe.getTypeOfCover() != null ? dmvicVehicleDe.getTypeOfCover() : null);
						dmvicVehicleDetails.setYearOfManufacture(
								dmvicVehicleDe.getYearOfManufacture() != null ? dmvicVehicleDe.getYearOfManufacture()
										: null);
						dmvicVehicleInfoDetailsRepo.save(dmvicVehicleDetails);
					}

				}

				DMVICTrancationLog dmvicTrancationLog = new DMVICTrancationLog();
				dmvicTrancationLog.setDmvicUrl(vehicleSearch);
				dmvicTrancationLog.setEntryDate(curDate);
				dmvicTrancationLog.setRequest(req1.toString());
				dmvicTrancationLog.setResponse(callbackObj.toString());
				dmvicTrancationLog.setRegNo(vehicleregistrationnumber);
				dmvicTrancationLogRepos.save(dmvicTrancationLog);

				res.setResponse(vehicle);
				res.setMassage("Success");
				res.setSuccess(true);

			} else {
				System.out.println("An error occurred while making the Vehicle Search Insurance call");
				res.setErrorMessage("An error occurred while making the Vehicle Search Insurance call");
				res.setMassage("Failure");
				res.setSuccess(false);
				DMVICTrancationLog dmvicTrancationLog = new DMVICTrancationLog();
				dmvicTrancationLog.setDmvicUrl(vehicleSearch);
				dmvicTrancationLog.setEntryDate(curDate);
				dmvicTrancationLog.setRequest(req1.toString());
				dmvicTrancationLog.setResponse(response2.toString());
				dmvicTrancationLog.setRegNo(vehicleregistrationnumber);
				dmvicTrancationLogRepos.save(dmvicTrancationLog);
			}
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			res.setErrorMessage("Connection refused: Unable to connect to the server");
			res.setMassage("Failure");
			res.setSuccess(false);

			return res;
		}
	}

	@Override
	public ValidateDoubleInsuranceRes vehicleCertificate(ValidateKMPDCRequest req) {
		ValidateDoubleInsuranceRes res = new ValidateDoubleInsuranceRes();
		try {

			String token = null;
			String certificateNo = null;
			RestTemplate temp = new RestTemplateBuilder().setConnectTimeout(Duration.ofSeconds(5))
					.setReadTimeout(Duration.ofMinutes(2)).build();
			ResponseEntity<String> response = null;
			ResponseEntity<String> response1 = null;
			ResponseEntity<String> response2 = null;
			ResponseEntity<Map> response3 = null;
			HttpHeaders header = new HttpHeaders();
			header.setContentType(MediaType.APPLICATION_JSON);
			Map<String, String> requestBody = new HashMap<>();
			requestBody.put("username", "admin");
			requestBody.put("password", "admin");
			HttpEntity<?> requestent = new HttpEntity<>(requestBody, header);
			System.out.println("Calling Generate Token Api:  " + requestent);
			try {
				response = temp.exchange(generateToken, HttpMethod.POST, requestent, String.class);

				System.out.println("response Api:  " + response.getBody());
			} catch (RestClientException e) {
				if (e.getCause() instanceof ConnectException) {
					System.out.println("Connection refused: Unable to connect to the server at " + response);

				} else {
					System.out.println(
							"An error occurred while making the Validate Insurance Token call: " + e.getMessage());
				}
			}
			HttpHeaders header1 = new HttpHeaders();
			header1.setContentType(MediaType.APPLICATION_JSON);
			header1.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			header1.setBearerAuth(response.getBody());
			Map<String, Object> inParams = new HashMap<>();
			inParams.put("P_POL_NO", req.getPolicyNo());
			Map<String, Object> body = new HashMap<>();
			body.put("procedureName", "WNPRC_GET_CERT_NO");
			body.put("inParams", inParams);
			HttpEntity<?> requestent1 = new HttpEntity<>(body, header1);
			System.out.println("Calling Premia Token Api:  " + body);
			try {
				response1 = temp.exchange(premiaDoubleInsuranceToken, HttpMethod.POST, requestent1, String.class);

				System.out.println("response Api:  " + response1.getBody());
				if (response1 != null && response1.getBody() != null) {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode root = mapper.readTree(response1.getBody());
				certificateNo = root.path("Data").path("P_CERT_NO").asText();
				}

			} catch (RestClientException e) {
				if (e.getCause() instanceof ConnectException) {
					System.out.println("Connection refused: Unable to connect to the server at " + response1);

				} else {
					System.out.println(
							"An error occurred while making the Validate Insurance Token call: " + e.getMessage());
				}
			}

			HttpHeaders header2 = new HttpHeaders();
			header2.setContentType(MediaType.APPLICATION_JSON);
			header2.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			header2.setBearerAuth(response.getBody());
			Map<String, Object> inParams1 = new HashMap<>();
			inParams.put("Key", "value");
			Map<String, Object> body1 = new HashMap<>();
			body1.put("procedureName", "WNPRC_DMVIC_TOKEN");
			body1.put("inParams", inParams1);
			HttpEntity<?> requestent2 = new HttpEntity<>(body1, header2);
			System.out.println("Calling Premia Token Api:  " + body1);
			try {
				response2 = temp.exchange(premiaDoubleInsuranceToken, HttpMethod.POST, requestent2, String.class);

				System.out.println("response Api:  " + response2.getBody());
			} catch (RestClientException e) {
				if (e.getCause() instanceof ConnectException) {
					System.out.println("Connection refused: Unable to connect to the server at " + response2);
				} else {
					System.out.println(
							"An error occurred while making the Validate Insurance Token call: " + e.getMessage());
				}
			}
			if (response2 != null && response2.getBody() != null) {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode root = mapper.readTree(response2.getBody());
				token = root.path("Data").path("P_TOKEN").asText();
				System.out.println("P_TOKEN: " + token);
			}

			HttpHeaders header3 = new HttpHeaders();
			header3.setContentType(MediaType.APPLICATION_JSON);
			header3.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			header3.setBearerAuth(token);
			Map<String, Object> inParams3 = new HashMap<>();
			inParams3.put("CertificateNumber", certificateNo);
			header3.set("ClientID", "A6B04E78-AD72-4B6D-9D55-72B62698FF8C");
			HttpEntity<?> requestent3 = new HttpEntity<>(inParams3, header3);
			System.out.println("calling Validate Double Insurance Api:  " + requestent3);
			try {
				response3 = temp.exchange(vehicleCertificate, HttpMethod.POST, requestent3, Map.class);
				System.out.println("Validate Double Insurance response Api:  " + response3.getBody());
				Map<String, Object> responseBody = response3.getBody();
				System.out.println("Full Response : " + responseBody);

				Boolean success = (Boolean) responseBody.get("success");

				String apiRequestNo = (String) responseBody.get("APIRequestNumber");

				Map<String, Object> callbackObj = (Map<String, Object>) responseBody.get("callbackObj");
				String pdfUrl = (String) callbackObj.get("URL");

				System.out.println("Success : " + success);
				System.out.println("API Request No : " + apiRequestNo);
				System.out.println("PDF URL : " + pdfUrl);

				res.setResponse(pdfUrl);
				res.setMassage("Success");
				res.setSuccess(true);
			} catch (RestClientException e) {
				if (e.getCause() instanceof ConnectException) {
					System.out.println("Connection refused: Unable to connect to the server at " + response3);
					res.setErrorMessage("An error occurred while making the Vehicle Search Insurance call");
					res.setMassage("Failure");
					res.setSuccess(false);

				} else {
					System.out.println(
							"An error occurred while making the Validate Double Insurance call: " + e.getMessage());
					res.setErrorMessage("An error occurred while making the Vehicle Search Insurance call");
					res.setMassage("Failure");
					res.setSuccess(false);
				}
			}

			return res;
		} catch (Exception e) {
			e.printStackTrace();
			res.setErrorMessage("Connection refused: Unable to connect to the server");
			res.setMassage("Failure");
			res.setSuccess(false);

			return res;
		}
	}

}
