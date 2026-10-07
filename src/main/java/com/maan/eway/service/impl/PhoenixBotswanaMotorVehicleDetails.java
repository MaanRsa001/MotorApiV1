package com.maan.eway.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

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
public class PhoenixBotswanaMotorVehicleDetails {
	private Logger log=LogManager.getLogger(PhoenixMotorVehicleDetails.class);
	
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
	
	@Autowired
	private DozerBeanMapper mapper;
	
	Gson json = new Gson();
	
	public List<Error> validateVehicleInfo(MotorVehicleInfoSaveReq req) {

		List<String> error = new ArrayList<String>();
	
		CommonErrorModuleReq comErrDescReq = new CommonErrorModuleReq();
		
		 List<Error> errors = new ArrayList<Error>();
		 try {
			 
			 ListItemValue itemValue =constDet.findByItemTypeIgnoreCaseAndStatusIgnoreCase("SKIP_VALIDATION", "Y");
			 
			 String skipValidationCompanyId =StringUtils.isBlank(itemValue.getItemValue())?"":itemValue.getItemValue();
			
			 Boolean skipYn =false;
			 
			 if(StringUtils.isNotBlank(skipValidationCompanyId)) {
				 
				 String array[] =skipValidationCompanyId.split(",");
				 
				 skipYn =Stream.of(array).anyMatch(p ->p.equals(req.getInsuranceId()));
			 }
			 
			 if(!"100004".equalsIgnoreCase(req.getInsuranceId())) {
				 if (StringUtils.isBlank(req.getResMotorUsage())) {
					 errors.add(new Error("13", "MotorUsage" , "Please Select MotorUsage"));
				 }
			 }
			 if (StringUtils.isBlank(req.getResBodyType())) {
				 errors.add(new Error("03", "BodyType" , "Please Select BodyType"));
			 } 
			 if (StringUtils.isBlank(req.getResMake())) {
				 errors.add(new Error("10", "Make" , "Please Select Make"));
			 }
			 if (StringUtils.isBlank(req.getResModel())  && StringUtils.isNotBlank(req.getResBodyType()) ) {
					List<String> induvidualIds = new ArrayList<String>();  
					induvidualIds.add("1");induvidualIds.add("2");induvidualIds.add("3");induvidualIds.add("4");induvidualIds.add("5");
					 if(  ! induvidualIds.contains(req.getResBodyType()) ) {
						 errors.add(new Error("11", "Model" , "Please Select Model"));
					 } 
			 } else if ( StringUtils.isNotBlank(req.getResModel()) && req.getResModel().length() > 200  ) {
					 errors.add(new Error("11", "ModelDescription" , "Model Description Must be under 200 Charecters only allowed"));
			 } 
			 else if(StringUtils.isNotBlank(req.getResModel()) && req.getResModel().matches(".*[^a-zA-Z0-9 ].*"))
			 {
				 //errors.add(new Error("11", "ModelDescription" , "Model Description is Invaild "));

			 }
			 	 
			 if(!"100004".equalsIgnoreCase(req.getInsuranceId())) {
				 if (!"100028".equalsIgnoreCase(req.getInsuranceId()) ) {
					 if (StringUtils.isBlank(req.getResMotorCategory())) {
						// errors.add(new Error("12", "MotorCategory" , "Please Select MotorCategory"));
					 }
				 }
			 }
			 if(StringUtils.isBlank(req.getResRegNumber())) {
					error.add("3003");
			 }else if(StringUtils.isNotBlank(req.getResRegNumber())) {
				if(!req.getResRegNumber().matches("[a-zA-Z0-9]+")) {
					 errors.add(new Error("17", "RegNumber" , "Please Enter RegNumber in Correct Format"));
				 }
			 }
			 if(StringUtils.isNotBlank(req.getResRegNumber())) {
				 MotorVehicleInfo 	 findData = repository.findTop1ByResRegNumberAndSavedFromAndCompanyIdOrderByEntryDateDesc(req.getResRegNumber() ,"API",  req.getInsuranceId() ) ;
				 if( findData==null ) {
					 findData = repository.findTop1ByResRegNumberAndSavedFromAndCompanyIdOrderByEntryDateDesc(req.getResRegNumber() ,"WEB", req.getInsuranceId() ) ;
					 if( findData!=null ) {
						// errors.add(new Error("04", "RegisterNumber" , "Register Number Already Exist"));
					 }
				 }
			 }
			 if (StringUtils.isBlank(req.getResChassisNumber())) {
				 errors.add(new Error("04", "ChassisNo" , "Please Enter ChassisNo"));
			 } else if (StringUtils.isNotBlank(req.getResChassisNumber())&& (!req.getResChassisNumber().matches("[a-zA-Z0-9-]+"))) {
				 errors.add(new Error("04", "ChassisNo" , "Please Enter ChassisNo in Correct Format"));
			 }  else if (req.getResChassisNumber().length() < 5 ) {
				 errors.add(new Error("04", "ChassisNo" , "ChassisNo must be greater then 4 Charecters only allowed"));
			 }   else if (req.getResChassisNumber().length() > 20 ) {
				 errors.add(new Error("04", "ChassisNo" , "ChassisNo must be under 20 Charecters only allowed"));
			 } else if(StringUtils.isNotBlank(req.getResChassisNumber()) && StringUtils.isNotBlank(req.getResRegNumber()) ) {
				 MotorVehicleInfo 	 findData = repository.findTop1ByResChassisNumberAndCompanyIdOrderByEntryDateDesc(req.getResChassisNumber(), req.getInsuranceId()  ) ;
				 if( findData!=null && (! findData.getResRegNumber().equalsIgnoreCase(req.getResRegNumber())) ) {
					 errors.add(new Error("04", "ChassisNumber" , "Chassis Number Already linked with " + " Register Number-"+ findData.getResRegNumber()  ));
				 }
			 }
			 if (StringUtils.isBlank(req.getResEngineNumber())) {
				 errors.add(new Error("07", "EngineNumber" , "Please Enter EngineNumber"));
			 }	else if (StringUtils.isNotBlank(req.getResEngineNumber())&& (!req.getResEngineNumber().matches("[a-zA-Z0-9-]+"))) {
				 errors.add(new Error("07", "EngineNumber" , "Please Enter EngineNumber in Correct Format"));
			 }  else if (req.getResEngineNumber().length() < 2 ) {
				 errors.add(new Error("04", "EngineNumber" , "EngineNumber must be greater then 4 Charecters only allowed"));
			 }  else if (req.getResEngineNumber().length() > 20 ) {
				 errors.add(new Error("04", "EngineNumber" , "EngineNumber must be under 20 Charecters only allowed"));
			 } else if (StringUtils.isNotBlank(req.getResChassisNumber()) && StringUtils.isNotBlank(req.getResEngineNumber()) ) {
				if(  req.getResChassisNumber().trim().equalsIgnoreCase(req.getResEngineNumber().trim())) {
					//errors.add(new Error("07", "EngineNumber", "EngineNumber Is Not Same as Chassis Number"));
				}
			 }
			 
			 if(req.getResMotorUsage().equalsIgnoreCase("private")) {
			 
			 if (StringUtils.isBlank(req.getResEngineCapacity())) {
				 errors.add(new Error("06", "Engine Capacity" , "Please Enter Engine Capacity"));
			 } else if (! req.getResEngineCapacity().matches("[0-9.]+") ) {
				 errors.add(new Error("06", "Engine Capacity" , "Please Enter Valid Number in Engine Capacity"));
			 } else if (StringUtils.isNotBlank(req.getResEngineCapacity())&& req.getResEngineCapacity().equalsIgnoreCase("0")){
				 errors.add(new Error("06", "Engine Capacity" , "Please Enter Valid Capacity as Engine Capacity"));				 
			 }  else if ( Double.valueOf(req.getResEngineCapacity()) > 99999 ) {
				 errors.add(new Error("04", "Engine Capacity" , "Engine Capacity must be under 5 digit only allowed"));
			 }
			 }
			 
			 if (StringUtils.isBlank(req.getResSittingCapacity())) {
				 errors.add(new Error("18", "SeatingCapacity" , "Please Enter Seating Capacity"));
			 } else if (! req.getResSittingCapacity().matches("[0-9]{1,2}+") ) {
				 errors.add(new Error("14", "SeatingCapacity" , "Please Enter Valid Number in Seating Capacity, allowed only 2 digits"));
			 } else if(StringUtils.isNotBlank(req.getInsuranceId()) && StringUtils.isNotBlank(req.getBranchCode()) && StringUtils.isNotBlank(req.getResBodyType()) ) {
				 List<MotorBodyTypeMaster> bodyTypes =  regInfoServiceImpl.getInduvidualBodyTypeMasterDropdown(req.getInsuranceId() ,req.getBranchCode() , req.getResBodyType() ) ;
				 if (Integer.valueOf(req.getResSittingCapacity().toString() ) <= 0 && (!req.getInsuranceId().equals("100040"))) {
					 errors.add(new Error("09", "SeatingCapacity" , "Seating Capacity 0 not allowed "));
				 }else  if( bodyTypes.size() > 0 && bodyTypes.get(0).getSeatingCapacity() !=null && bodyTypes.get(0).getSeatingCapacity()  < Integer.valueOf(req.getResSittingCapacity()) && (!req.getInsuranceId().equals("100040"))   )  {
					 //errors.add(new Error("09", "SeatingCapacity" , "Seating Capacity Must be under " + bodyTypes.get(0).getSeatingCapacity() +  " only allowed "));
				 }
			 }
			 if (StringUtils.isBlank(req.getResYearOfManufacture())) {
				errors.add(new Error("18", "Manufacture Year" , "Please Select Manufacture Year"));
			 } else if((StringUtils.isNotBlank(req.getResYearOfManufacture())&&  ! req.getResYearOfManufacture().matches("[0-9]+")) || req.getResYearOfManufacture().length()>4 ) {
				errors.add(new Error("33", "Manufacture Year", "Please Enter Manufacture Year Format in YYYY"));
			 }else if((StringUtils.isNotBlank(req.getResYearOfManufacture())&&! req.getResYearOfManufacture().matches("[0-9]+")) || req.getResYearOfManufacture().length()<4 ) {
				errors.add(new Error("33", "Manufacture Year", "Please Enter Manufacture Year Format in YYYY"));
			 }
				
			 if(!"100004".equalsIgnoreCase(req.getInsuranceId())) {
				 if (StringUtils.isBlank(req.getResOwnerName())) {
					 errors.add(new Error("16", "OwnerName" , "Please Enter OwnerName"));
				  }
//				 else if (StringUtils.isNotBlank(req.getResOwnerName())&& (!req.getResOwnerName().matches("[a-zA-Z ]+"))) {
//					 errors.add(new Error("17", "OwnerName" , "Please Enter OwnerName in Correct Format"));
//				  }
			 }
				/*
				 * if(!"100004".equalsIgnoreCase(req.getInsuranceId())) { if
				 * (StringUtils.isBlank(req.getResOwnerCategory())) { errors.add(new Error("15",
				 * "OwnerCategory" , "Please Select OwnerCategory")); } }
				 */
			  if(!"100019".equalsIgnoreCase(req.getInsuranceId()) && !"100004".equalsIgnoreCase(req.getInsuranceId())) {
				 if(!req.getResMotorUsage().equalsIgnoreCase("private")) {
			   if (StringUtils.isBlank(req.getResGrossWeight())) {
					 errors.add(new Error("18", "GrossWeight" , "Please Enter Gross Weight"));
				   }else if (! req.getResGrossWeight().matches("[0-9.]+") ) {
					 errors.add(new Error("18", "GrossWeight" , "Please Enter Valid Number in Gross Weight"));
				   }
					if (StringUtils.isNotBlank(req.getResGrossWeight())) {
						if (Integer.valueOf(req.getResGrossWeight()) <= 0) {
							errors.add(new Error("18", "GrossWeight", "Please Enter Gross Weight Above Zero"));
						}
					}
			  }
			  }
			  if(!"100019".equalsIgnoreCase(req.getInsuranceId()) && !"100004".equalsIgnoreCase(req.getInsuranceId())) {
				if (StringUtils.isBlank(req.getResFuelUsed())) {
					// errors.add(new Error("08", "FuelUsed" , "Please Select FuelUsed"));
				 }
			} 
				
			 if (StringUtils.isBlank(req.getCreatedBy())) {
				 errors.add(new Error("01", "CreatedBy" , "Please Add CreatedBy"));
			 }
			 
			 if (StringUtils.isBlank(req.getInsuranceId())) {
				 errors.add(new Error("03", "InsuranceId" , "Please Select InsuranceId"));
			 }
			 
			 if (StringUtils.isBlank(req.getResColor())) {
				 errors.add(new Error("05", "Color" , "Please Select Color"));
			 }
			
			 if(!skipYn) {
				 
				 if (StringUtils.isBlank(req.getResNumberOfAxles())) {
					 //errors.add(new Error("14", "NumberOfAxles" , "Please Enter NumberOfAxles"));
				 } else if (! req.getResNumberOfAxles().matches("[1-9]+") ) {
					// errors.add(new Error("14", "ResNumberOfAxles" , "Please Enter Valid Number in ResNumberOfAxles"));
				 }else if (Integer.valueOf(req.getResNumberOfAxles())>5)  {
					// errors.add(new Error("14", "ResNumberOfAxles" , "Please Enter No Of Axle's Less Than or Equal to 5"));
				 }
				 
				 
			   if (StringUtils.isBlank(req.getResAxleDistance()) ) {
					// errors.add(new Error("02", "ResAxleDistance" , "Please Enter ResAxleDistance"));
				 }else if (Integer.valueOf(req.getResAxleDistance())<=0) {
					 //errors.add(new Error("02", "ResAxleDistance" , "Please Enter Axle Distance Above Zero "));
				 }
			   if (StringUtils.isBlank(req.getResTareWeight()) && !skipYn) {
					//errors.add(new Error("18", "TareWeight" , "Please Enter TareWeight"));
				} else if (! req.getResTareWeight().matches("[0-9.]+") ) {
					//errors.add(new Error("18", "TareWeight" , "Please Enter Valid Number in TareWeight"));
				}else if (Integer.valueOf(req.getResTareWeight())<=0) {
					//errors.add(new Error("18", "TareWeight" , "Please Enter TareWeight Above Zero"));
				}
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
				saveInfo.setReqChassisNumber(req.getResChassisNumber().toUpperCase() );
				saveInfo.setReqRegNumber(req.getResRegNumber().toUpperCase());
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
				saveInfo.setResChassisNumber(req.getResChassisNumber().toUpperCase());
				saveInfo.setResEngineNumber(req.getResEngineNumber().toUpperCase());
				saveInfo.setResRegNumber(req.getResRegNumber().toUpperCase());
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
