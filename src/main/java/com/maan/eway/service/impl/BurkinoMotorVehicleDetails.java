package com.maan.eway.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.maan.eway.bean.ListItemValue;
import com.maan.eway.bean.MotorBodyTypeMaster;
import com.maan.eway.bean.MotorColorMaster;
import com.maan.eway.bean.MotorMakeModelMaster;
import com.maan.eway.bean.MotorVehicleInfo;
import com.maan.eway.bean.MotorVehicleInfoArch;
import com.maan.eway.bean.MotorVehicleUsageMaster;
import com.maan.eway.common.service.impl.FetchErrorDescServiceImpl;
import com.maan.eway.error.Error;
import com.maan.eway.repository.ListItemValueRepository;
import com.maan.eway.repository.MotorBodyTypeMasterRepository;
import com.maan.eway.repository.MotorMakeModelMasterRepository;
import com.maan.eway.repository.MotorVehicleInfoArchRepository;
import com.maan.eway.repository.MotorVehicleInfoRepository;
import com.maan.eway.repository.MotorVehicleUsageMasterRepository;
import com.maan.eway.req.MotorVehicleInfoSaveReq;
import com.maan.eway.res.CommonErrorModuleReq;
import com.maan.eway.res.MotorSaveRes;
import com.maan.eway.res.MotorVehicleInfoRes;

@Service
public class BurkinoMotorVehicleDetails {
	private Logger log=LogManager.getLogger(BurkinoMotorVehicleDetails.class);
	
	@Autowired
	private ListItemValueRepository constDet;
	
	@Autowired
	private FetchErrorDescServiceImpl errorDescService ;
	
	@Autowired
	private MotorVehicleInfoRepository repository;
	
	@Autowired
	private RegulatoryInfoServiceImpl regInfoServiceImpl;
	
	@Autowired
	private MotorVehicleInfoArchRepository motorArchRepo;
	
	@Autowired
	private MotorVehicleUsageMasterRepository motorVehicleUsageMasterRepo;

	@Autowired
	private MotorMakeModelMasterRepository motorMakeModelMasterRepo;

	@Autowired
	private MotorBodyTypeMasterRepository motorBodyTypeMasterRepo;
	
	Gson json = new Gson();

	public List<Error> validateVehicleInfo(MotorVehicleInfoSaveReq req) {

		List<String> error = new ArrayList<String>();
	
		CommonErrorModuleReq comErrDescReq = new CommonErrorModuleReq();
		String bodyType="";
		 List<Error> errors = new ArrayList<Error>();
		 try {
			 List<MotorBodyTypeMaster> motorBody = motorBodyTypeMasterRepo.findByBodyNameEnAndBranchCodeAndCompanyIdOrderByAmendIdDesc(req.getResBodyType(),"99999",req.getInsuranceId());
			 if(!CollectionUtils.isEmpty(motorBody)) {
				 bodyType=motorBody.get(0).getBodyType();
			 }
			 ListItemValue itemValue =constDet.findByItemTypeIgnoreCaseAndStatusIgnoreCase("SKIP_VALIDATION", "Y");
			 
			 String skipValidationCompanyId =StringUtils.isBlank(itemValue.getItemValue())?"":itemValue.getItemValue();
			
			 Boolean skipYn =false;
			 
			 if(StringUtils.isNotBlank(skipValidationCompanyId)) {
				 
				 String array[] =skipValidationCompanyId.split(",");
				 
				 skipYn =Stream.of(array).anyMatch(p ->p.equals(req.getInsuranceId()));
			 }
			 
			 
			 if(!skipYn) {
				 
				 if (StringUtils.isBlank(req.getResNumberOfAxles())) {
					 errors.add(new Error("14", "NumberOfAxles" , "Please Enter NumberOfAxles"));
				 } else if (! req.getResNumberOfAxles().matches("[1-9]+") ) {
					 errors.add(new Error("14", "ResNumberOfAxles" , "Please Enter Valid Number in ResNumberOfAxles"));
				 }else if (Integer.valueOf(req.getResNumberOfAxles())>5)  {
					 errors.add(new Error("14", "ResNumberOfAxles" , "Please Enter No Of Axle's Less Than or Equal to 5"));
				 }
				 
				 
			   if (StringUtils.isBlank(req.getResAxleDistance()) ) {
					 errors.add(new Error("02", "ResAxleDistance" , "Please Enter ResAxleDistance"));
				 }else if (Integer.valueOf(req.getResAxleDistance())<=0) {
					 errors.add(new Error("02", "ResAxleDistance" , "Please Enter Axle Distance Above Zero "));
				 }
			  
			   
			   	
			 }
			 if (StringUtils.isBlank(req.getResBodyType())) {
				 errors.add(new Error("03", "BodyType" , "Please Select BodyType"));
			 }
			 
			 if(("50".equals(req.getResBodyType()) ||"51".equals(req.getResBodyType()) || "5".equals(req.getResBodyType()) || "58".equals(req.getResBodyType()) || "18".equals(req.getResBodyType()) || "25".equals(req.getResBodyType()))) { 
				if(StringUtils.isBlank(req.getDisplacementInCM3()) ) {
					error.add("3026");			
				}
				else if(!req.getDisplacementInCM3().matches("[0-9]+")){
					error.add("3027");			
				}
				else if(req.getDisplacementInCM3().length() > 3 ){
					   error.add("3028");			
				}
		 	 }
			 if (StringUtils.isBlank(req.getResMake())) {
				 errors.add(new Error("10", "Make" , "Please Select Make"));
			 }
			 if (StringUtils.isBlank(req.getResModel())  && StringUtils.isNotBlank(req.getResBodyType()) ) {
					List<String> induvidualIds = new ArrayList<String>();  
					induvidualIds.add("1");induvidualIds.add("2");induvidualIds.add("3");induvidualIds.add("4");induvidualIds.add("5");
					 if(!induvidualIds.contains(req.getResBodyType()) ) {
						 error.add("3006");
					 } 
			 }else if ( StringUtils.isNotBlank(req.getResModel()) && req.getResModel().length() > 25  ) {
					error.add("3002");
			 }
			 
			 if(StringUtils.isBlank(req.getResRegNumber())) {
					error.add("3003");
			 }
			 else if(StringUtils.isNotBlank(req.getResRegNumber())) {
				 if(req.getResRegNumber().length() > 25){
					 error.add("3005");
				 } else if(!req.getResRegNumber().matches("[a-zA-Z0-9\\-]+") || req.getResRegNumber().startsWith("-")){
					 error.add("3004");
				 }
				  
			 }
			/* if (StringUtils.isBlank(req.getResChassisNumber())) {
				 error.add("3007");			
			 } else if (StringUtils.isNotBlank(req.getResChassisNumber())&& (!req.getResChassisNumber().matches("[a-zA-Z0-9]+"))) {
				 error.add("3008");						 
			 } else if (req.getResChassisNumber().length() < 5 ) {
				 error.add("3009");						 
			 } else if (req.getResChassisNumber().length() > 25 ) {
				 error.add("3010");	
			 } else if(StringUtils.isNotBlank(req.getResChassisNumber()) && StringUtils.isNotBlank(req.getResRegNumber()) ) {
				 MotorVehicleInfo 	 findData = repository.findTop1ByResChassisNumberAndCompanyIdOrderByEntryDateDesc(req.getResChassisNumber(), req.getInsuranceId()  ) ;
				 if( findData!=null && (! findData.getResRegNumber().equalsIgnoreCase(req.getResRegNumber())) ) {
					 Error error1=new Error("04", "ChassisNumber" , "Chassis Number Already linked with " + " Register Number-"+ findData.getResRegNumber()  );
					 error1.setFieldLocal("Numéro de châssis");
					 error1.setMessageLocal("Numéro de châssis déjà lié au numéro de registre-"+ findData.getResRegNumber());
					 errors.add(error1);
				 }
			 }*/
			 
			 if(StringUtils.isNotBlank(req.getResRegNumber())) {
				 MotorVehicleInfo 	 findData = repository.findTop1ByResRegNumberAndSavedFromAndCompanyIdOrderByEntryDateDesc(req.getResRegNumber() ,"API",  req.getInsuranceId() ) ;
				 if( findData==null ) {
					 findData = repository.findTop1ByResRegNumberAndSavedFromAndCompanyIdOrderByEntryDateDesc(req.getResRegNumber() ,"WEB", req.getInsuranceId() ) ;
					 if( findData!=null ) {
						// errors.add(new Error("04", "RegisterNumber" , "Register Number Already Exist"));
					 }
				 }
			 }
			 
			 if (StringUtils.isBlank(req.getResEngineNumber())) {
					
			 }else if (StringUtils.isNotBlank(req.getResEngineNumber())&& (!req.getResEngineNumber().matches("[a-zA-Z0-9]+"))) {
				 error.add("3011");	
			 }else if (req.getResEngineNumber().length() < 4 ) {
				 error.add("3012");	
			 }else if (req.getResEngineNumber().length() > 25 ) {
				 error.add("3013");	
			 } else if (StringUtils.isNotBlank(req.getResChassisNumber()) && StringUtils.isNotBlank(req.getResEngineNumber()) ) {
				if(  req.getResChassisNumber().trim().equalsIgnoreCase(req.getResEngineNumber().trim())) {
					 error.add("3014");	
				}
			 }
			 
			 if (StringUtils.isBlank(req.getResSittingCapacity())) {
				 error.add("3015");	
			 } else if (! req.getResSittingCapacity().matches("[0-9]+") ) {
				 error.add("3016");	
			 } 	 
			 else if(Integer.valueOf(req.getResSittingCapacity().toString() ) <= 0){
				 error.add("3018");	
			 }	 
			 else if(req.getResSittingCapacity().length() > 2) {
				 error.add("3017");	
			 }
			 
			 if (StringUtils.isBlank(req.getResFuelUsed())) {
					errors.add(new Error("08", "FuelType" , "Please Select Fuel Type"));
			 }
			 
			 /*
			 if (StringUtils.isBlank(req.getPlateType())) {
					errors.add(new Error("08", "PlateType" , "Please Select Plate Type"));
			 }
			 */
			 
			 if (StringUtils.isBlank(req.getResTareWeight())) {
				    error.add("3019");			
			 } else if (!req.getResTareWeight().matches("[0-9.]+") ) {
				error.add("3020");			
			 }else if (Long.valueOf(req.getResTareWeight())<=0) {
				error.add("3021");			
			 }else if(req.getResTareWeight().length() > 5 ){
				   error.add("3022");			
			}
			 if("P".equals(bodyType) && (req.getResBodyType()!="50" && req.getResBodyType()!="51" && req.getResBodyType()!="5" && req.getResBodyType()!="58" && req.getResBodyType()!="28")) { 
				 if(StringUtils.isBlank(req.getHorsePower()) ) {
						error.add("3023");			
				 }else if(!req.getHorsePower().matches("[0-9]+")){
					error.add("3024");			
				 }else if(req.getHorsePower().length() > 5 ){
					error.add("3025");			
				 } 
			 }
			 
			 if("C".equals(bodyType) && (req.getResBodyType()!="50" && req.getResBodyType()!="51" && req.getResBodyType()!="5" && req.getResBodyType()!="58" && req.getResBodyType()!="28")) {
				if(StringUtils.isBlank(req.getResGrossWeight()) ) {
						error.add("3032");								
				}else if(!req.getResGrossWeight().matches("[0-9]+")){
					error.add("3033");			
				}else if(req.getResGrossWeight().length() > 5 ){
					error.add("3034");			
				}
			 }
			 
			 if((req.getResBodyType()!="50" && req.getResBodyType()!="51" && req.getResBodyType()!="5" && req.getResBodyType()!="58" && req.getResBodyType()!="28")) { 
				if(StringUtils.isBlank( String.valueOf(req.getNumberOfCyliners())) ) {
					  error.add("3029");			
				}else if(!String.valueOf(req.getNumberOfCyliners()).matches("[0-9]+")){
					  error.add("3030");			
				}
				else if(String.valueOf(req.getNumberOfCyliners()).length() > 2 ){
					   error.add("3031");			
				}
			 }
			
			 if (StringUtils.isBlank(req.getCreatedBy())) {
				 errors.add(new Error("01", "CreatedBy" , "Please Add CreatedBy"));
			 }
			 
			 if (StringUtils.isBlank(req.getInsuranceId())) {
				 errors.add(new Error("03", "InsuranceId" , "Please Select InsuranceId"));
			 }
			
			 if(req.getRegistrationDate() == null){
				error.add("3035");
			 }
				
			
			 comErrDescReq.setBranchCode("99999");
			 comErrDescReq.setInsuranceId("99999");
			 comErrDescReq.setProductId("99999");
			 comErrDescReq.setModuleId("10");
			 comErrDescReq.setModuleName("MOTOR CREATION");
			 List<Error> errors1 = errorDescService.getErrorDesc(error ,comErrDescReq);
			 errors.addAll(errors1);
		 } catch (Exception e) {
				e.printStackTrace();
				log.info(e.getMessage());
				
				return null;

		}
		 return errors;
	
	}


	public MotorSaveRes saveVehicleInfo(MotorVehicleInfoSaveReq req) {
		MotorSaveRes res = new MotorSaveRes();
		
		DozerBeanMapper mapper = new DozerBeanMapper();
		SimpleDateFormat arf = new SimpleDateFormat("yyMMddmmssSSS"); 
		try {
			String regNo = req.getResRegNumber() ;
		 	String chassisNo = req.getResChassisNumber();
		 	MotorVehicleInfo saveInfo = new MotorVehicleInfo();
		 	
		 	MotorVehicleInfo findData = repository.findTop1ByResRegNumberAndResChassisNumberAndSavedFromAndCompanyIdOrderByEntryDateDesc(regNo , chassisNo,"WEB", req.getInsuranceId());
		 	
		 	String policyYn = "N" ;
		 	
			if(findData!=null  && findData.getPolicyYn().equalsIgnoreCase("N") ) {
				saveInfo = findData ;
				
				policyYn  = "N";
				repository.delete(findData);
				MotorVehicleInfoArch archData   = new MotorVehicleInfoArch();  
				archData   = mapper.map(findData, MotorVehicleInfoArch.class);
				archData.setArchId("AI-" + arf.format(new Date()) );
				motorArchRepo.save(archData);
			} else if( findData!=null){
				saveInfo = findData ;
				policyYn = findData.getPolicyYn(); 
			}
			
			if  (policyYn.equalsIgnoreCase("N") ) {
				
				saveInfo = mapper.map(req, MotorVehicleInfo.class);
				saveInfo.setEntryDate(new Date());
				saveInfo.setReqChassisNumber(StringUtils.isBlank(req.getResChassisNumber())?"":req.getResChassisNumber().toUpperCase() );
				saveInfo.setReqRegNumber(StringUtils.isBlank(req.getResRegNumber())?"":req.getResRegNumber().toUpperCase());
				saveInfo.setPolicyYn("N");
				saveInfo.setStatus("Y");
				saveInfo.setReqMotorCategory(StringUtils.isBlank(req.getResMotorCategory()) ? null : Integer.valueOf(req.getResMotorCategory()));
				saveInfo.setResMotorCategory(StringUtils.isBlank(req.getResMotorCategory()) ? null :Integer.valueOf(req.getResMotorCategory()));
				String motorCategoryId = saveInfo.getResMotorCategory()!=null ?saveInfo.getResMotorCategory().toString() :""; 
				
				String motorCategoryDesc = ""; 			
				String motorCategoryDescLocal = ""; 
				Map<String, String> listItemLocal = regInfoServiceImpl.getListItemLocal(req.getInsuranceId(), req.getBranchCode(), "MOTOR_CATEGORY",  motorCategoryId);
				motorCategoryDesc = Optional.ofNullable(listItemLocal).map(map -> map.get("itemDesc")).orElse("");
				motorCategoryDescLocal = Optional.ofNullable(listItemLocal).map(map -> map.get("itemDescLocal")).orElse("");
				
				saveInfo.setMotorCategoryDesc(motorCategoryDesc);
				
				saveInfo.setResMotorCategoryDescLocal(motorCategoryDescLocal);
				saveInfo.setResChassisNumber(StringUtils.isBlank(req.getResChassisNumber())?"":req.getResChassisNumber().toUpperCase());
				saveInfo.setResEngineNumber(StringUtils.isBlank(req.getResEngineNumber())?"":req.getResEngineNumber().toUpperCase());
				saveInfo.setResRegNumber(StringUtils.isBlank(req.getResRegNumber())?"":req.getResRegNumber().toUpperCase());
				saveInfo.setSavedFrom("WEB");
				saveInfo.setCompanyId(req.getInsuranceId());
				saveInfo.setDisplacementInCM3(req.getDisplacementInCM3());
				saveInfo.setNoOfCylinders(req.getNumberOfCyliners());	
				saveInfo.setRegistrationDate(req.getRegistrationDate());	
				saveInfo.setPlateType(StringUtils.isBlank(req.getPlateType())?"0":req.getPlateType());
				
				
				//saving local description 
				//motor usage
		        List<MotorVehicleUsageMaster> motorUsage = motorVehicleUsageMasterRepo.findByBranchCodeAndCompanyIdAndVehicleUsageDescOrderByAmendIdDesc("99999",req.getInsuranceId(),req.getResMotorUsage());
		        saveInfo.setResMotorUsageLocal((motorUsage != null && motorUsage.size()>0) ? motorUsage.get(0).getVehicleUsageDescLocal() : req.getResMotorUsage());
		        //Motor Model
		        List<MotorMakeModelMaster> motormodel = motorMakeModelMasterRepo.findByBranchCodeAndCompanyIdAndModelNameEnOrderByAmendIdDesc("99999",req.getInsuranceId(),req.getResModel());
		        saveInfo.setResModelLocal((motormodel != null  && motormodel.size()>0)  ? motormodel.get(0).getModelNameLocal() : req.getResModel());
		        //Motor Make
		        List<MotorMakeModelMaster> motorMake = motorMakeModelMasterRepo.findByBranchCodeAndCompanyIdAndMakeNameEnOrderByAmendIdDesc("99999",req.getInsuranceId(),req.getResMake());
		        saveInfo.setResMakeLocal((motorMake != null  && motorMake.size()>0 ) ? motorMake.get(0).getMakeNameLocal() : req.getResMake());
		        //Body Type
		        List<MotorBodyTypeMaster> motorBody = motorBodyTypeMasterRepo.findByBodyNameEnAndBranchCodeAndCompanyIdOrderByAmendIdDesc(req.getResBodyType(),"99999",req.getInsuranceId());
		        saveInfo.setResBodyTypeLocal((motorBody != null && motorBody.size()>0) ? motorBody.get(0).getBodyNameLocal() : req.getResBodyType());
		        //vehicle color
		        List<MotorColorMaster> motorColor = regInfoServiceImpl.findMotorColorMasters(req.getInsuranceId(),req.getBranchCode(),req.getResColor());
		        saveInfo.setResColorLocal((motorColor != null && motorColor.size()>0) ? motorColor.get(0).getColorDescLocal() : req.getResColor());
		        //fuel type
		        List<ListItemValue> fuelUsage = regInfoServiceImpl.findEwayListItemValues(req.getInsuranceId(),req.getBranchCode(),"FUEL_TYPE",req.getResFuelUsed());
		        saveInfo.setResFuelUsedLocal((fuelUsage != null && fuelUsage.size()>0) ? fuelUsage.get(0).getItemValueLocal() : req.getResFuelUsed());
		        
				saveInfo.setHorsePower(StringUtils.isBlank(req.getHorsePower())?0:Integer.valueOf(req.getHorsePower()));
				saveInfo.setResMakeId(req.getResMakeId());
				saveInfo.setResModelId(req.getResModelId());
				
				repository.save(saveInfo);
			 	
			 	log.info(" Saved Details is ---> " + json.toJson(saveInfo) );

			}
			
			 MotorVehicleInfoRes showVehicleInfo = new  MotorVehicleInfoRes();
			 mapper.map(saveInfo, showVehicleInfo);
			res.setResponse("Saved Successfully");
			res.setShowVehicleInfo(showVehicleInfo);
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			return null;

		}
		return res;
	
	}

}
