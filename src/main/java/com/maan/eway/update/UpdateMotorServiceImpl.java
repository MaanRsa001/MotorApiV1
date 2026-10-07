package com.maan.eway.update;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.maan.eway.bean.EserviceDriverDetails;
import com.maan.eway.bean.EserviceMotorDetails;
import com.maan.eway.bean.MotorDataDetails;
import com.maan.eway.common.req.EserviceMotorDetailsSaveRes;
import com.maan.eway.common.res.CommonRes;
import com.maan.eway.common.service.impl.GenerateSeqNoServiceImpl;
import com.maan.eway.repository.CompanyProductMasterRepository;
import com.maan.eway.repository.EServiceDriverDetailsRepository;
import com.maan.eway.repository.EServiceMotorDetailsRepository;
import com.maan.eway.repository.EServiceSectionDetailsRepository;
import com.maan.eway.repository.EserviceCustomerDetailsRepository;
import com.maan.eway.repository.HomePositionMasterRepository;
import com.maan.eway.repository.LoginBranchMasterRepository;
import com.maan.eway.repository.LoginMasterRepository;
import com.maan.eway.repository.LoginUserInfoRepository;
import com.maan.eway.repository.MotorColorMasterRepository;
import com.maan.eway.repository.MotorDataDetailsRepository;
import com.maan.eway.repository.MotorDriverDetailsRepository;
import com.maan.eway.repository.MotorMakeModelMasterRepository;
import com.maan.eway.repository.MotorVehicleInfoRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class UpdateMotorServiceImpl implements UpdateMotorService{
	private Logger log = LogManager.getLogger(UpdateMotorServiceImpl.class);
	@PersistenceContext
	private EntityManager em;
	
	Gson json = new Gson();
	@Autowired
	private EServiceMotorDetailsRepository repo;
	
	@Autowired
	private HomePositionMasterRepository homeRepo;
	
	@Autowired
	private MotorVehicleInfoRepository motorVehRepo;
	
	@Autowired
	private LoginMasterRepository loginRepo;
	
	@Autowired
	private GenerateSeqNoServiceImpl genSeqNoService;
	
	
	@Autowired
	private MotorDataDetailsRepository motRepo;
	
	@Autowired
	private EserviceCustomerDetailsRepository custRepo;
	
	@Autowired
	private LoginUserInfoRepository loginUserRepo;
	
	@Autowired
	private LoginBranchMasterRepository lbranchRepo;
	
	@Autowired
	private MotorMakeModelMasterRepository modelrepo;
	
	@Autowired
	private CompanyProductMasterRepository companyProductMasterRepo;
	
	@Autowired
	private EServiceSectionDetailsRepository eserSecRepo;
	
	@Autowired
	private EServiceDriverDetailsRepository eserDriverRepo;
	
	@Autowired
	private MotorDriverDetailsRepository driverRepo;
	
	@Autowired
	private MotorColorMasterRepository motorColorMasterRepository;
	
	
	public  static LocalDate convertToLocalDate(Date dateToConvert) {
		 if (dateToConvert == null) {
	            return null;
	        }
	        return dateToConvert.toInstant()
	                            .atZone(ZoneId.systemDefault())
	                            .toLocalDate();
	    }
	
	
	@Override
	public CommonRes updateMotorDetails(UpdateMotorReq req) {
		CommonRes res = new CommonRes();
		
		DozerBeanMapper mapper = new DozerBeanMapper();
		SimpleDateFormat yf = new SimpleDateFormat("yyyy");
		EserviceMotorDetails savedata = new EserviceMotorDetails();
		EserviceDriverDetails motorDriDetails = null;
		EserviceMotorDetails findData = null;
		String oaCode = "";
		try {
			
			Integer vehId = 0;
			String refNo = "";
			String quoteNo="";
			Date entryDate = null;
			String createdBy = "",oldPolicyNumber="";
			EserviceMotorDetails motOld = null;
			
			// Motor Update
				refNo = req.getRequestReferenceNo();
				vehId = Integer.parseInt(req.getVehicleId());
				List<EserviceMotorDetails> findoldData = repo.findByRequestReferenceNo(req.getRequestReferenceNo());
				EserviceMotorDetails eserMotor= repo.findByRequestReferenceNoAndRiskId(refNo, vehId);
				
				if(eserMotor!=null) {
					quoteNo=eserMotor.getQuoteNo();
					MotorDataDetails motData = motRepo.findByQuoteNoAndVehicleId(quoteNo, String.valueOf(vehId));
					if(motData!=null) {
						motData.setRequestReferenceNo(StringUtils.isBlank(req.getRequestReferenceNo()) ? motData.getRequestReferenceNo() : req.getRequestReferenceNo());
						motData.setAgencyCode(StringUtils.isBlank(req.getAgencyCode()) ? motData.getAgencyCode() : req.getAgencyCode());
						motData.setApplicationId(StringUtils.isBlank(req.getApplicationId()) ? motData.getApplicationId() : req.getApplicationId());
						motData.setAxelDistance(StringUtils.isBlank(req.getAxelDistance()) ? motData.getAxelDistance() : Integer.parseInt(req.getAxelDistance()));
						motData.setBdmCode(StringUtils.isBlank(req.getBdmCode()) ? motData.getBdmCode() : req.getBdmCode());
						motData.setBranchCode(StringUtils.isBlank(req.getBranchCode()) ? motData.getBranchCode() : req.getBranchCode());
						motData.setBrokerBranchCode(StringUtils.isBlank(req.getBrokerBranchCode()) ? motData.getBrokerBranchCode() : req.getBrokerBranchCode());
						motData.setChassisNumber(StringUtils.isBlank(req.getChassisNumber()) ? motData.getChassisNumber() : req.getChassisNumber());
						motData.setCollateralYn(StringUtils.isBlank(req.getCollateralYn()) ? motData.getCollateralYn() : req.getCollateralYn());
						motData.setColor(StringUtils.isBlank(req.getColor()) ? motData.getColor() : req.getColor());
						motData.setColorDesc(StringUtils.isBlank(req.getColorDesc()) ? motData.getColorDesc() : req.getColorDesc());
						motData.setCreatedBy(StringUtils.isBlank(req.getCreatedBy()) ? motData.getCreatedBy() : req.getCreatedBy());
						motData.setCubicCapacity(StringUtils.isBlank(req.getCubicCapacity()) ? motData.getCubicCapacity() : Double.parseDouble(req.getCubicCapacity()));
						motData.setCurrency(StringUtils.isBlank(req.getCurrency()) ? motData.getCurrency() : req.getCurrency());
						motData.setCustomerCode(StringUtils.isBlank(req.getCustomerCode()) ? motData.getCustomerCode() : req.getCustomerCode());
						motData.setCustomerName(StringUtils.isBlank(req.getCustomerName()) ? motData.getCustomerName() : req.getCustomerName());
//						motData.setCustomerReferenceNo(StringUtils.isBlank(req.getCustomerReferenceNo()) ? motData.getCustomerReferenceNo() : req.getCustomerReferenceNo());
//			    		motData.setDisplacementInCM3(StringUtils.isBlank(req.getDisplacementInCM3()) ? motData.getDisplacementInCM3() : req.getDisplacementInCM3());
						motData.setDrivenByDesc(StringUtils.isBlank(req.getDrivenByDesc()) ? motData.getDrivenByDesc() : req.getDrivenByDesc());
						motData.setEndorsementYn(StringUtils.isBlank(req.getEndorsementYn()) ? motData.getEndorsementYn() : req.getEndorsementYn());
//						motData.setEngineCapacity(StringUtils.isBlank(req.getEngineCapacity()) ? motData.getEngineCapacity() : req.getEngineCapacity());
						motData.setEngineNumber(StringUtils.isBlank(req.getEngineNumber()) ? motData.getEngineNumber() : req.getEngineNumber());
						motData.setExchangeRate(StringUtils.isBlank(req.getExchangeRate()) ? motData.getExchangeRate() : Double.parseDouble(req.getExchangeRate()));
						motData.setFleetOwnerYn(StringUtils.isBlank(req.getFleetOwnerYn()) ? motData.getFleetOwnerYn() : req.getFleetOwnerYn());
						motData.setFuelType(StringUtils.isBlank(req.getFuelType()) ? motData.getFuelType() : req.getFuelType());
						motData.setFuelTypeDesc(StringUtils.isBlank(req.getFuelTypeDesc()) ? motData.getFuelTypeDesc() : req.getFuelTypeDesc());
						motData.setGpsTrackingInstalled(StringUtils.isBlank(req.getGpsTrackingInstalled()) ? motData.getGpsTrackingInstalled() : req.getGpsTrackingInstalled());
						motData.setGrossWeight(StringUtils.isBlank(req.getGrossWeight()) ? motData.getGrossWeight() : Double.parseDouble(req.getGrossWeight()));
//						motData.setHavePromoCode(StringUtils.isBlank(req.getHavePromoCode()) ? motData.getHavePromoCode() : req.getHavePromoCode());
						motData.setHoldInsurancePolicy(StringUtils.isBlank(req.getHoldInsurancePolicy()) ? motData.getHoldInsurancePolicy() : req.getHoldInsurancePolicy());
//						motData.setHorsePower(StringUtils.isBlank(req.getHorsePower()) ? motData.getHorsePower() : Integer.parseInt(req.getHorsePower()));
						motData.setIdNumber(StringUtils.isBlank(req.getIdNumber()) ? motData.getIdNumber() : req.getIdNumber());
						motData.setInsuranceClass(StringUtils.isBlank(req.getInsuranceClass()) ? motData.getInsuranceClass() : req.getInsuranceClass());
//						motData.setInsuranceId(StringUtils.isBlank(req.getInsuranceId()) ? motData.getInsuranceId() : req.getInsuranceId());
						motData.setLocationId(StringUtils.isBlank(req.getLocationId()) ? motData.getLocationId() : Integer.parseInt(req.getLocationId()));
						motData.setLoginId(StringUtils.isBlank(req.getLoginId()) ? motData.getLoginId() : req.getLoginId());
						motData.setManufactureYear(StringUtils.isBlank(req.getManufactureYear()) ? motData.getManufactureYear() : Integer.parseInt(req.getManufactureYear()));
//						motData.setMobileCode(StringUtils.isBlank(req.getMobileCode()) ? motData.getMobileCode() : req.getMobileCode());
//						motData.setMobileNumber(StringUtils.isBlank(req.getMobileNumber()) ? motData.getMobileNumber() : req.getMobileNumber());
						motData.setMotorCategory(StringUtils.isBlank(req.getMotorCategory()) ? motData.getMotorCategory() : req.getMotorCategory());
//						motData.setMotorUsage(StringUtils.isBlank(req.getMotorusage()) ? motData.getMotorUsage() : req.getMotorusage());
//						motData.setMotorUsageId(StringUtils.isBlank(req.getMotorusageId()) ? motData.getMotorUsageId() : req.getMotorusageId());
						motData.setNoOfVehicles(StringUtils.isBlank(req.getNoOfVehicles()) ? motData.getNoOfVehicles() :  Integer.parseInt(req.getNoOfVehicles()));
						motData.setNumberOfAxels(StringUtils.isBlank(req.getNumberOfAxels()) ? motData.getNumberOfAxels() :  Integer.parseInt(req.getNumberOfAxels()));
//						motData.setNumberOfCylinders(StringUtils.isBlank(req.getNumberOfCylinders()) ? motData.getNumberOfCylinders() : Integer.parseInt(req.getNumberOfCylinders()));
//						motData.setPolicyEndDate(StringUtils.isBlank(req.getPolicyEndDate()) ? motData.getPolicyEndDate() : yf.parse(req.getPolicyEndDate()));
//						motData.setPolicyRenewalYn(StringUtils.isBlank(req.getPolicyRenewalYn()) ? motData.getPolicyRenewalYn() : req.getPolicyRenewalYn());
//						motData.setPolicyStartDate(StringUtils.isBlank(req.getPolicyStartDate()) ? motData.getPolicyStartDate() : req.getPolicyStartDate());
						motData.setProductId(StringUtils.isBlank(req.getProductId()) ? motData.getProductId() : Integer.parseInt(req.getProductId()));
//						motData.setQuoteExpiryDays(StringUtils.isBlank(req.getQuoteExpiryDays()) ? motData.getQuoteExpiryDays() : req.getQuoteExpiryDays());
						motData.setRegistrationYear(StringUtils.isBlank(req.getRegistrationYear()) ? motData.getRegistrationYear() : yf.parse(req.getRegistrationYear()));
						motData.setRegistrationNumber(StringUtils.isBlank(req.getRegistrationNumber()) ? motData.getRegistrationNumber() : req.getRegistrationNumber());
//						motData.setSaveOrSubmit(StringUtils.isBlank(req.getSaveOrSubmit()) ? motData.getSaveOrSubmit() : req.getSaveOrSubmit());
						motData.setSavedFrom(StringUtils.isBlank(req.getSavedFrom()) ? motData.getSavedFrom() : req.getSavedFrom());
//						motData.setSearchFromApi(req.getSearchFromApi() == null ? motData.isSearchFromApi() : req.getSearchFromApi());
						motData.setSeatingCapacity(StringUtils.isBlank(req.getSeatingCapacity()) ? motData.getSeatingCapacity() : Integer.parseInt(req.getSeatingCapacity()));
						motData.setSourceType(StringUtils.isBlank(req.getSourceType()) ? motData.getSourceType() : req.getSourceType());
						motData.setSourceTypeId(StringUtils.isBlank(req.getSourceTypeId()) ? motData.getSourceTypeId() : req.getSourceTypeId());
						motData.setStatus(StringUtils.isBlank(req.getStatus()) ? motData.getStatus() : req.getStatus());
						motData.setSubUserType(StringUtils.isBlank(req.getSubUserType()) ? motData.getSubUserType() : req.getSubUserType());
						motData.setTareWeight(StringUtils.isBlank(req.getTareWeight()) ? motData.getTareWeight() :Double.parseDouble( req.getTareWeight()));
//						motData.setUserType(StringUtils.isBlank(req.getUserType()) ? motData.getUserType() : req.getUserType());
						motData.setVehcileModel(StringUtils.isBlank(req.getVehicleModel()) ? motData.getVehcileModel() : req.getVehicleModel());
						motData.setVehicleModelId(StringUtils.isBlank(req.getVehicleModelId()) ? motData.getVehicleModelId() : req.getVehicleModelId());
						motData.setVehicleId(StringUtils.isBlank(req.getVehicleId()) ? motData.getVehicleId() : req.getVehicleId());
						motData.setVehicleTypeDesc(StringUtils.isBlank(req.getVehicleType()) ? motData.getVehicleTypeDesc() : req.getVehicleType());
						motData.setVehicleType(StringUtils.isBlank(req.getVehicleTypeId()) ? motData.getVehicleType() : req.getVehicleTypeId());
						motData.setVehicleMake(StringUtils.isBlank(req.getVehicleMake()) ? motData.getVehicleMake() : req.getVehicleMake());
						motData.setVehicleMakeId(StringUtils.isBlank(req.getVehicleMakeId() ) ? motData.getVehicleMakeId() : req.getVehicleMakeId());
                      motRepo.saveAndFlush(motData);
                      
                    res.setCommonResponse(motData);	
      				res.setIsError(false);
      				res.setMessage("Updated Successfully");
					}else {
						res.setMessage("No Data on Motor Data Details");
					}
				}else {
					res.setMessage("No Data on Eservice Motor Details ");
				}
				
				
		}

		catch (Exception e) {
			e.printStackTrace();
			log.info("Log Details" + e.getMessage());
			return null;
		}
		return res;
	}

	@Override
	public CommonRes getVehicleIds(UpdateMotorReq req) {
		CommonRes res=new CommonRes();
		List<VehicleRes> veh=new ArrayList<VehicleRes>();
		try {
			List<MotorDataDetails> motDatas = motRepo.findByRequestReferenceNoOrderByVehicleIdAsc(req.getRequestReferenceNo());
			List<MotorDataDetails> matchedData = motDatas.stream().filter(p-> p.getStatus().equalsIgnoreCase("P")).toList();
			if(!matchedData.isEmpty()) {

				for(MotorDataDetails data:matchedData) {
					VehicleRes v=new VehicleRes();
					v.setVehicleId(data.getVehicleId()) ;
					v.setRequestReferenceNo(req.getRequestReferenceNo());	
					v.setInsuranceId(data.getCompanyId());
					v.setLocationId(String.valueOf(data.getLocationId()));
					veh.add(v);
				}
				
				res.setCommonResponse(veh);	
				res.setIsError(false);
				res.setMessage("Successfully get Vehicle Ids");
			}else {
				res.setMessage("No Data For this RequestReferenceNo");
			}
		}catch(Exception e) {
			e.printStackTrace();
			res.setMessage("Exception Occurs");
			return res;
		}
		return res;
	}

}
