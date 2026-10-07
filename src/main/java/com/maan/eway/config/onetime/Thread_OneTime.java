package com.maan.eway.config.onetime;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.maan.eway.bean.CurrencyMaster;
import com.maan.eway.bean.EserviceBuildingDetails;
import com.maan.eway.bean.EserviceCommonDetails;
import com.maan.eway.bean.EserviceCustomerDetails;
import com.maan.eway.bean.EserviceDriverDetails;
import com.maan.eway.bean.EserviceMotorDetails;
import com.maan.eway.bean.EserviceTravelDetails;
import com.maan.eway.bean.EserviceTravelGroupDetails;
import com.maan.eway.bean.ListItemValue;
import com.maan.eway.bean.MsAssetDetails;
import com.maan.eway.bean.MsCustomerDetails;
import com.maan.eway.bean.MsDriverDetails;
import com.maan.eway.bean.MsHumanDetails;
import com.maan.eway.bean.MsVehicleDetails;
import com.maan.eway.bean.SectionCoverMaster;
import com.maan.eway.bean.SeqOnetimetable;
import com.maan.eway.common.req.TravelGroupInsertReq;
import com.maan.eway.common.res.BuildingSectionRes;
import com.maan.eway.repository.EServiceBuildingDetailsRepository;
import com.maan.eway.repository.EServiceDriverDetailsRepository;
import com.maan.eway.repository.EServiceMotorDetailsRepository;
import com.maan.eway.repository.EServiceSectionDetailsRepository;
import com.maan.eway.repository.EserviceCommonDetailsRepository;
import com.maan.eway.repository.EserviceCustomerDetailsRepository;
import com.maan.eway.repository.EserviceTravelDetailsRepository;
import com.maan.eway.repository.EserviceTravelGroupDetailsRepository;
import com.maan.eway.repository.MsAssetDetailsRepository;
import com.maan.eway.repository.MsCustomerDetailsRepository;
import com.maan.eway.repository.MsDriverDetailsRepository;
import com.maan.eway.repository.MsHumanDetailsRepository;
import com.maan.eway.repository.MsVehicleDetailsRepository;
import com.maan.eway.repository.MsVehicleMinimal;
import com.maan.eway.repository.SeqOnetimetableRepository;
import com.maan.eway.req.OneTimeTableReq;
import com.maan.eway.res.OneTimeVehicleRes;
import com.maan.eway.service.OneTimeService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

public class Thread_OneTime implements Callable<Object> {

	private String type;

	private OneTimeTableReq request;
	
	private SeqOnetimetableRepository oneNoRepo ;

	private OneTimeService otSer;
	private EntityManager em;
	private MsVehicleDetailsRepository msVehicleRepo ;
	private MsDriverDetailsRepository msDriverRepo ;
	
	
	
	private EServiceMotorDetailsRepository eserviceMotorRepo;
	private MsCustomerDetailsRepository msCustomerRepo;
	private EserviceCustomerDetailsRepository eserviceCustomerRepo;
	private EserviceTravelGroupDetailsRepository groupRepo ;
	private Logger log = LogManager.getLogger(getClass());

	private EserviceMotorDetails motorDatas ;
	private EserviceDriverDetails motorDriverDatas ;
	
	private EserviceTravelDetailsRepository eserTravelRepo ;
	private MsHumanDetailsRepository msHumanRepo ;
	private EserviceTravelDetails travelData ;
	private EserviceCustomerDetails custData ;
	private  List<EserviceTravelGroupDetails> groupDatas;
	private EServiceBuildingDetailsRepository eserBuildRepo;
	private MsAssetDetailsRepository msAssetRepo;
	private List<EserviceBuildingDetails> eserBuildings;
	private EServiceSectionDetailsRepository eserSecRepo ; 
	private List<EserviceCommonDetails> esercommonDatas ;
	private EserviceCommonDetailsRepository eserCommonRepo ;
	private EServiceDriverDetailsRepository esDriverRepo;
	private MsAssetDetails msAsset;
	private DozerBeanMapper dozerMapper;
	private boolean is;
	
	public Thread_OneTime(String type, OneTimeTableReq request, OneTimeService otSer, EntityManager em,EserviceCustomerDetails custData ,  MsVehicleDetailsRepository msVehicleRepo, 
			EServiceMotorDetailsRepository eserviceMotorRepo, MsCustomerDetailsRepository msCustomerRepo, EserviceCustomerDetailsRepository eserviceCustomerRepo
			,EserviceMotorDetails motorDatas ,EserviceTravelDetailsRepository eserTravelRepo,MsHumanDetailsRepository msHumanRepo
			, EserviceTravelDetails travelData , List<EserviceTravelGroupDetails> groupDatas,EserviceTravelGroupDetailsRepository groupRepo,
			EServiceBuildingDetailsRepository eserBuildRepo,MsAssetDetailsRepository msAssetRepo,List<EserviceBuildingDetails> eserBuildings ,  EServiceSectionDetailsRepository eserSecRepo
			, SeqOnetimetableRepository oneNoRepo , List<EserviceCommonDetails> esercommonDatas  , EserviceCommonDetailsRepository eserCommonRepo,EServiceDriverDetailsRepository esDriverRepo,
			EserviceDriverDetails motorDriverDatas, MsDriverDetailsRepository msDriverRepo , DozerBeanMapper dozerMapper,boolean is) {
		
		this.type = type;
		this.request = request;
		this.otSer = otSer;
		this.em=em;
		this.custData= custData ;
		this.eserviceMotorRepo=eserviceMotorRepo;
		this.msCustomerRepo=msCustomerRepo;
		this.msVehicleRepo=msVehicleRepo;
		this.eserviceMotorRepo=eserviceMotorRepo;
		this.eserviceCustomerRepo=eserviceCustomerRepo; 
		this.motorDatas=motorDatas;
		this.eserTravelRepo=eserTravelRepo;
		this.msHumanRepo=msHumanRepo;
		this.travelData = travelData ;
		this.groupDatas = groupDatas ;
		this.groupRepo = groupRepo;
		this.eserBuildRepo = eserBuildRepo;
		this.msAssetRepo = msAssetRepo;
		this.eserBuildings = eserBuildings;
		this.eserSecRepo = eserSecRepo ;
		this.oneNoRepo = oneNoRepo ;
		this.esercommonDatas = esercommonDatas ;
		this.eserCommonRepo = eserCommonRepo ;
		this.esDriverRepo=esDriverRepo;
		this.motorDriverDatas=motorDriverDatas;
		this.msDriverRepo=msDriverRepo;
		this.dozerMapper=dozerMapper;
		this.is=is;
	}

	@Override
	public Map<String, Object> call() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {

			type = StringUtils.isBlank(type) ? "" : type;

			log.info("Thread_OneTime--> type: " + type);

			if (type.equalsIgnoreCase("MSVehicle")) {

				map.put("MSVehicle", call_MSVehicle(request));

			}else if (type.equalsIgnoreCase("MsDriver")) {

				map.put("MSDriver", call_MSDriver(request));

			} else if (type.equalsIgnoreCase("MSCustomer")) {

				map.put("MSCustomer", call_MSCustomer(request));

			} else if (type.equalsIgnoreCase("MSTravel")) {

				map.put("MSTravel", call_MSTravel(request));

			}
			else if (type.equalsIgnoreCase("MSAssetOrMsHuman")) {

				map.put("MSAsset", call_MSAssetOrMsHuman(request));

			}
//			else if (type.equalsIgnoreCase("MSHuman")) {
//
//				map.put("MSHuman", call_MSHuman(request));
//
//			}


		} catch (Exception e) {
			log.error(e);
		}
		return map;
	}
	
	
	
	@Transactional
	private synchronized  List<OneTimeVehicleRes> call_MSVehicle(OneTimeTableReq request) {
		List<OneTimeVehicleRes> resList = new ArrayList<OneTimeVehicleRes>();
		OneTimeVehicleRes res = new  OneTimeVehicleRes();
		List<MsVehicleDetails> list = new ArrayList<MsVehicleDetails>();

		String vdRefNo = "" ;
		String sectionId = "" ;
		String agencyCode = "" ;
		String branchCode = "" ;
		String productId = "" ;
		String companyId = "" ;
		String locationId="";
		String vehicleid="";
	//	SimpleDateFormat sdf = new SimpleDateFormat("yyMMddhhmmssSS");
		String decimalDigits = currencyDecimalFormat(motorDatas.getCompanyId() , motorDatas.getCurrency() ).toString();
		String stringFormat = "%0"+decimalDigits+"d" ;
		String decimalLength = decimalDigits.equals("0") ?"" : String.format(stringFormat ,0L)  ;
		String pattern = StringUtils.isBlank(decimalLength) ?  "#####0" :   "#####0." + decimalLength;
		DecimalFormat df = new DecimalFormat(pattern);
		
		
		DozerBeanMapper dozerMapper = new DozerBeanMapper();
		try {
			
			// Wind Screen SumInsured
//			BigDecimal windSumInsuredLc = BigDecimal.ZERO ;
//			if(  motorDatas.getWindScreenSumInsured()!=null && motorDatas.getWindScreenSumInsured().compareTo(BigDecimal.ZERO)>0 ) {
//				String itemType = "WINDSCREEN_SUMINSURED" ;
//				List<ListItemValue> getList  = getListItem(motorDatas.getCompanyId() , motorDatas.getBranchCode() , itemType);
//				List<ListItemValue> privateVehicle = getList.stream().filter( o -> o.getItemCode().equalsIgnoreCase("1") ).collect(Collectors.toList());
//				List<ListItemValue> otherVehicle = getList.stream().filter( o ->  o.getItemCode().equalsIgnoreCase("2") ).collect(Collectors.toList());
//				 
//				windSumInsuredLc = motorDatas.getWindScreenSumInsuredLc(); 
//				BigDecimal privateVehicleValue = privateVehicle.size() > 0 ? new BigDecimal (privateVehicle.get(0).getItemValue()) :  BigDecimal.ZERO  ;
//				BigDecimal otherVehicleValue =  otherVehicle.size() > 0 ? new BigDecimal (otherVehicle.get(0).getItemValue()) :  BigDecimal.ZERO  ;
//				
//				if(motorDatas.getSectionId().equalsIgnoreCase("10") && windSumInsuredLc.compareTo(privateVehicleValue) > 0  ) {
//					windSumInsuredLc = windSumInsuredLc.subtract(privateVehicleValue) ;
//				}  else if(motorDatas.getSectionId().equalsIgnoreCase("10") && windSumInsuredLc.compareTo(privateVehicleValue) <= 0  ) {
//					
//					windSumInsuredLc = new BigDecimal("-1") ;
//					
//				} else if(windSumInsuredLc.compareTo(otherVehicleValue) > 0 ) {
//					
//					windSumInsuredLc = windSumInsuredLc.subtract(otherVehicleValue) ;
//					
//				}  else if(windSumInsuredLc.compareTo(otherVehicleValue ) <= 0) {
//					
//					windSumInsuredLc = new BigDecimal("-1") ; 
//				}
//			}
			
			// Accessories SumInsured
//			BigDecimal accSumInsuredLc =  BigDecimal.ZERO  ;
//			if( motorDatas.getAcccessoriesSumInsured()!=null &&   motorDatas.getAcccessoriesSumInsured().compareTo(BigDecimal.ZERO)>0 ) {
//				String itemType = "ACCESSORIES_SUMINSURED" ;
//				List<ListItemValue> getList  = getListItem(motorDatas.getCompanyId() , motorDatas.getBranchCode() , itemType);
//				List<ListItemValue> privateVehicle = getList.stream().filter( o -> o.getItemCode().equalsIgnoreCase("1") ).collect(Collectors.toList());
//				List<ListItemValue> otherVehicle = getList.stream().filter( o ->  o.getItemCode().equalsIgnoreCase("2") ).collect(Collectors.toList());
//				
//				accSumInsuredLc = motorDatas.getAcccessoriesSumInsuredLc(); 
//				BigDecimal privateVehicleValue = privateVehicle.size() > 0 ? new BigDecimal(privateVehicle.get(0).getItemValue()) : BigDecimal.ZERO  ;
//				BigDecimal otherVehicleValue = otherVehicle.size() > 0 ? new BigDecimal(otherVehicle.get(0).getItemValue()) : BigDecimal.ZERO ;
//				
//				if(motorDatas.getSectionId().equalsIgnoreCase("10") && accSumInsuredLc.compareTo(privateVehicleValue)>0  ) {
//					accSumInsuredLc = accSumInsuredLc.subtract(privateVehicleValue) ;
//					
//				} else if(motorDatas.getSectionId().equalsIgnoreCase("10") && accSumInsuredLc.compareTo(privateVehicleValue) <= 0  ) {
//					accSumInsuredLc = new BigDecimal("-1") ;
//					
//				} else if(accSumInsuredLc.compareTo(otherVehicleValue ) > 0) {
//					accSumInsuredLc = accSumInsuredLc.subtract(otherVehicleValue) ;
//					
//				} else if(accSumInsuredLc.compareTo(otherVehicleValue ) <= 0) {
//					accSumInsuredLc = new BigDecimal("-1") ; 
//				} 
//			}
			
		//	motorDatas.getAcccessoriesSumInsuredLc().multiply(motorDatas.getExchangeRate())
			boolean newEntry = false ;
			sectionId = String.valueOf(request.getSectionId()); //motorDatas.getSectionId() ;
			branchCode = motorDatas.getBranchCode();
			agencyCode =  motorDatas.getAgencyCode();
			productId =  motorDatas.getProductId();
			companyId =  motorDatas.getCompanyId();
			locationId="1";
			BigDecimal grossWeight=BigDecimal.ZERO;
			if(companyId.equals("100018"))
			{
				 grossWeight =  motorDatas.getGrossWeight()!=null ?   new BigDecimal(df.format(motorDatas.getGrossWeight())) : null;
			}
			else
			{
			 grossWeight =  motorDatas.getGrossWeight()!=null ?   new BigDecimal(df.format(motorDatas.getGrossWeight().divide(new BigDecimal(1000)))) : null;
			}
			//BigDecimal grossWeight =  motorDatas.getGrossWeight()!=null ?   new BigDecimal(df.format(motorDatas.getGrossWeight().divide(new BigDecimal(1000)))) : null;
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<MsVehicleDetails> query = cb.createQuery(MsVehicleDetails.class);
			Date endDate = new Date(); // today
			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			cal.add(Calendar.DAY_OF_MONTH, -15); // 15 days ago
			Date startDate = cal.getTime();
			List<MsVehicleMinimal> minimalVehicles=null;
			if (companyId.equals("100002")) {
				
//				list =
//					    msVehicleRepo.findByChassisNumberAndRequestReferenceNoAndMotorUsageAndInsuranceTypeAndGpsYnAndNcdYnAndInsuranceClassAndEntryDateBetween(
//					        motorDatas.getChassisNumber(),
//					        motorDatas.getRequestReferenceNo(),
//					        motorDatas.getMotorUsage(),
//					        motorDatas.getInsuranceType(),
//					        motorDatas.getGpsTrackingInstalled(),
//					        motorDatas.getNcdYn(),
//					        motorDatas.getInsuranceClass(),
//					        startDate,
//					        endDate
//					    );		

				 minimalVehicles = msVehicleRepo.findMinimalVehicles(motorDatas.getChassisNumber(), motorDatas.getRequestReferenceNo(),
						motorDatas.getMotorUsage(), motorDatas.getInsuranceType(), motorDatas.getGpsTrackingInstalled(),
						motorDatas.getNcdYn(), motorDatas.getInsuranceClass(), startDate, endDate);

			}else {
			// Find All
			Root<MsVehicleDetails> b = query.from(MsVehicleDetails.class);

			// Select
			query.select(b);
			Predicate n1 = (cb.like(cb.lower(b.get("vehicleId")), motorDatas.getRiskId().toString().toLowerCase()));
			Predicate n2 = (cb.like(cb.lower(b.get("accidentYn")), motorDatas.getAccident().toLowerCase()));
			Predicate n3 = (cb.like(cb.lower(b.get("windScreenCoverRequeired")), motorDatas.getWindScreenCoverRequired().toLowerCase()));
			Predicate n4 = (cb.like(cb.lower(b.get("insuranceType")), motorDatas.getInsuranceType().toLowerCase()));
			Predicate n5 = (cb.like(cb.lower(b.get("insuranceClass")), motorDatas.getInsuranceClass().toLowerCase()));
			Predicate n6 = (cb.like(cb.lower(b.get("ownerCategory")), motorDatas.getOwnerCategory()==null ? null : motorDatas.getOwnerCategory().toLowerCase()));
			Predicate n7 = (cb.like(cb.lower(b.get("chassisNumber")), motorDatas.getChassisNumber().toLowerCase()));
			Predicate n8 = (cb.like(cb.lower(b.get("vehicleMake")), motorDatas.getVehicleMake().toLowerCase()));
			Predicate n9 = (cb.like(cb.lower(b.get("vehcileModel")), motorDatas.getVehcileModel().toLowerCase()));
			Predicate n10 = (cb.like(cb.lower(b.get("vehicleBodyType")), motorDatas.getVehicleType().toLowerCase()));				
			Predicate n11 = cb.equal(b.get("vehicleWeight"), grossWeight);
			Predicate n12 = cb.equal(b.get("manufactureYear"), motorDatas.getManufactureYear());
			Predicate n13 = cb.equal(b.get("manufactureAge"), motorDatas.getManufactureAge());
			Predicate n14=  motorDatas.getRegistrationAge() ==null? cb.isNull(b.get("registrationAge")) : cb.equal(b.get("registrationAge"), motorDatas.getRegistrationAge());
			Predicate n15 = motorDatas.getRegistrationYear()==null?cb.isNull(b.get("registrationYear")): cb.equal(b.get("registrationYear"), motorDatas.getRegistrationYear());
			Predicate n16 = cb.equal(b.get("seatingCapacity"), motorDatas.getSeatingCapacity());
			Predicate n17 = cb.equal(b.get("periodOfInsurance"), motorDatas.getPeriodOfInsurance());
			Predicate n21 = cb.equal(b.get("ncdyears"), motorDatas.getNcdYears()==null?0:motorDatas.getNcdYears());
			Predicate n22 = (cb.like(cb.lower(b.get("ncdYn")), motorDatas.getNcdYn().toLowerCase()));
			Predicate n23 = cb.equal(b.get("noOfClaims"), motorDatas.getNoOfClaims()==null?0:motorDatas.getNoOfClaims());
			Predicate n24 = cb.equal(b.get("currency"), motorDatas.getCurrency());
			Predicate n25 = cb.equal(b.get("exchangeRate"), motorDatas.getExchangeRate());
			Predicate n26 = (cb.like(cb.lower(b.get("fleetOwnerYn")), motorDatas.getFleetOwnerYn()==null ?null : motorDatas.getFleetOwnerYn().toLowerCase()));
			Predicate n27 = cb.equal(b.get("noOfVehicles"), motorDatas.getNoOfVehicles());
			Predicate n28 = cb.equal(b.get("noOfCompehensives"), motorDatas.getNoOfCompehensives());
			Predicate n29 = cb.equal(b.get("claimRatio"), motorDatas.getClaimRatio());
			//Predicate n30 =(motorDatas.getTppdIncreaeLimit()!=null ?(cb.equal(b.get("tpdIncreaseLimit"), motorDatas.getTppdIncreaeLimit())):cb.isNull(b.get("tpdIncreaseLimit")));
			//tpd_increase_limit
			Predicate n30 =(motorDatas.getTppdIncreaeLimit()!=null ?(cb.equal(b.get("tpdIncreaseLimit"), motorDatas.getTppdIncreaeLimit())):cb.equal(b.get("tpdIncreaseLimit"), BigDecimal.ZERO));
			Predicate n31 = (cb.like(cb.lower(b.get("motorUsage")),motorDatas.getMotorUsage()==null?null: motorDatas.getMotorUsage().toLowerCase()));
			Predicate n32 = (cb.like(cb.lower(b.get("status")), "Y".toLowerCase()));
			Predicate n33 = (cb.like(cb.lower(b.get("fuelType")),motorDatas.getFuelType()==null ?null :  motorDatas.getFuelType().toLowerCase()));
			Predicate n34 = (cb.like(cb.lower(b.get("chassisNumber")), motorDatas.getChassisNumber().toLowerCase()));
			Predicate n35 = (cb.like(cb.lower(b.get("gpsYn")), motorDatas.getGpsTrackingInstalled().toLowerCase()));
			Predicate n36 = (cb.like(cb.lower(b.get("requestReferenceNo")), motorDatas.getRequestReferenceNo().toString().toLowerCase()));	
			Predicate n37 = cb.equal(b.get("cityLimit"), StringUtils.isBlank(motorDatas.getCityLimit())?"0": motorDatas.getCityLimit());
			Predicate n38 = (cb.equal(b.get("havepromocode"), motorDatas.getHavepromocode()));
			Predicate n39 = (cb.equal(b.get("promocode"), StringUtils.isBlank(motorDatas.getPromocode())?"": motorDatas.getPromocode() ));
			Predicate n40 = (cb.equal(b.get("endtTypeId"),motorDatas.getEndorsementType()==null?0 :motorDatas.getEndorsementType() ));
			Predicate n41 = (cb.equal(b.get("endtCategoryId"),motorDatas.getIsFinaceYn()==null?"N" :motorDatas.getIsFinaceYn() ));
			Predicate n42 = motorDatas.getTppdFreeLimit()==null? cb.isNull(b.get("tppdFreeLimit")) :(cb.equal(b.get("tppdFreeLimit"),motorDatas.getTppdFreeLimit()));
			Predicate n43 = (cb.equal(b.get("sumInsuredLc"),motorDatas.getSumInsuredLc()));
			Predicate n18 = cb.equal(b.get("windScreenSumInsuredLc"), motorDatas.getWindScreenSumInsuredLc() );
			Predicate n19 = cb.equal(b.get("acccessoriesSumInsuredLc"), motorDatas.getAcccessoriesSumInsuredLc());
			Predicate n20 = cb.equal(b.get("sumInsured"), motorDatas.getSumInsured());
			Predicate n44 = (cb.equal(b.get("acccessoriesSumInsured"),motorDatas.getAcccessoriesSumInsured()) );
			Predicate n45 = (cb.equal(b.get("windScreenSumInsured"),motorDatas.getWindScreenSumInsured()));
			Predicate n46 = motorDatas.getTppdIncreaeLimitLc()==null ?cb.isNull(b.get("tppdIncreaeLimitLc")):(cb.equal(b.get("tppdIncreaeLimitLc"),motorDatas.getTppdIncreaeLimitLc()));
			Predicate n47 = motorDatas.getTppdFreeLimitLc()==null ?cb.isNull(b.get("tppdFreeLimitLc")) :(cb.equal(b.get("tppdFreeLimitLc"),motorDatas.getTppdFreeLimitLc()));
			Predicate n48 = (cb.equal(b.get("cubicCapacity"),motorDatas.getCubicCapacity()));
			Predicate n49 = (cb.equal(b.get("sourceType"),motorDatas.getSourceType()));
			Predicate n50 = motorDatas.getExcessLimit()==null? cb.isNull(b.get("excessLimit")) :(cb.equal(b.get("excessLimit"),motorDatas.getExcessLimit()));
			Predicate n51 = motorDatas.getExcessLimitLc()==null ? cb.isNull(b.get("excessLimitLc")):(cb.equal(b.get("excessLimitLc"),motorDatas.getExcessLimitLc()));
			Predicate n52 = motorDatas.getNonElecAccessoriesSi()==null?  cb.isNull(b.get("nonElecAccessoriesSi")) :(cb.equal(b.get("nonElecAccessoriesSi"),motorDatas.getNonElecAccessoriesSi()));
			Predicate n53 = motorDatas.getNonElecAccessoriesSiLc()==null?cb.isNull(b.get("nonElecAccessoriesSiLc")) : (cb.equal(b.get("nonElecAccessoriesSiLc"),motorDatas.getNonElecAccessoriesSiLc()));
			Predicate n54 = (cb.equal(b.get("customerType"),motorDatas.getCustomerType()));
			Predicate n55 = (cb.equal(b.get("vehicleMakeId"),motorDatas.getVehicleMakeId()));
			Predicate n56 = (cb.equal(b.get("vehicleModelId"),motorDatas.getVehicleModelId()));
			Predicate n57 = (cb.equal(b.get("fuelTypeId"),motorDatas.getFuelTypeId()));
			Predicate n58 = (cb.equal(b.get("ownerCategoryId"),motorDatas.getOwnerCategoryId()));
			Predicate n59 = (cb.equal(b.get("manufactureCountry"),motorDatas.getManufactureCountry()));
			Predicate n60 = (cb.like(cb.lower(b.get("carAlarmYn")), motorDatas.getCarAlarmYn().toLowerCase()));
			Predicate n61 = cb.equal(b.get("zone"), motorDatas.getZone());
			Predicate n62 = cb.equal(b.get("horsePower"), motorDatas.getHorsePower());
			Predicate n64 = cb.equal(b.get("paCoverId"), motorDatas.getPaCoverId());
			Predicate n65 = cb.equal(b.get("noOfPassengers"), motorDatas.getNoOfPassengers());
			Predicate n66 = cb.equal(b.get("coverId"), motorDatas.getCoverId());
			Predicate n67 = cb.equal(b.get("creditShortfallSi"), motorDatas.getCreditShortfallSi());


			Predicate n63 = cb.equal(b.get("locationId"), 1);
			query.where(n1,n2,n3,n4,n5,n6,n7,n8,n9,n10,
					n11,n12,n13,n14,n15,n16,n17,n18,n19,n20,
					n21,n22,n23,n24,n25,n26,n27,n28,n29,n30,
					n31,n32,n33,n34,n35, n36,n37,n38,n39 , n40
					,n41 , n42 ,n42 ,n43,n44,n45,n46,n47 ,n48,
					n49,n50,n51,n52,n53 ,n54,n55,n56,n57,n58,n59,n60,n61,n62,n63,n64,n65,n66,n67
					);
			
			
			// Get Result
			TypedQuery<MsVehicleDetails> result = em.createQuery(query);
			list = result.getResultList();		
			}

			if (list==null || list.size() <= 0 ) {
				newEntry = true ;

			} else {
				vdRefNo = String.valueOf(minimalVehicles.get(0).getVdRefno());
				locationId = String.valueOf(minimalVehicles.get(0).getLocationId());
				vehicleid=String.valueOf(minimalVehicles.get(0).getVehicleId());
				//res.setMsVehicleDetails(minimalVehicles.get(0));
			}
			if(newEntry==true  ) {
			//	Random rand = new Random();
	         // int random=rand.nextInt(90)+10;
				vdRefNo = genOneTimeTableRefNo() ;  // sdf.format(new Date()) + random ;
                 
				MsVehicleDetails saveVehicle = new MsVehicleDetails();
				dozerMapper.map(motorDatas, saveVehicle);
				saveVehicle.setVehicleId(request.getVehicleId()==null?motorDatas.getRiskId().toString():String.valueOf(request.getVehicleId()));
				saveVehicle.setLocationId(1);
				saveVehicle.setVdRefno(Long.valueOf(vdRefNo) );
				saveVehicle.setEntryDate(new Date());
				saveVehicle.setAccidentYn(StringUtils.isBlank(motorDatas.getAccident())?"N":motorDatas.getAccident());
				saveVehicle.setStatus(motorDatas.getStatus());
				saveVehicle.setNcdyears(motorDatas.getNcdYears());
				saveVehicle.setInsuranceClass(motorDatas.getInsuranceClass());
				if(request.getInsuranceId().equals("100040"))
				{
					saveVehicle.setInsuranceType(motorDatas.getInsuranceType());	
				}else {
					saveVehicle.setInsuranceType(request.getSectionId().toString());	
				}
				if(companyId.equals("100018"))
				{
					if(motorDatas.getCubicCapacity()==null)
							{
						saveVehicle.setCubicCapacity(null);
							}
					
				}
				
				saveVehicle.setNcdYn(motorDatas.getNcdYn());
				saveVehicle.setNoOfClaims(motorDatas.getNoOfClaims()==null?0 :motorDatas.getNoOfClaims());
				saveVehicle.setNoOfCompehensives(motorDatas.getNoOfCompehensives()==null?0 :motorDatas.getNoOfCompehensives());
				saveVehicle.setNoOfVehicles(motorDatas.getNoOfVehicles()==null?0 :motorDatas.getNoOfVehicles());
				saveVehicle.setNcdyears(motorDatas.getNcdYears()==null?0 :motorDatas.getNcdYears());
				saveVehicle.setOwnerCategory(motorDatas.getOwnerCategory()==null?"0":motorDatas.getOwnerCategory());
				saveVehicle.setRegistrationYear(motorDatas.getRegistrationYear());
				saveVehicle.setVehicleBodyType(motorDatas.getVehicleType());
				saveVehicle.setTpdIncreaseLimit(motorDatas.getTppdIncreaeLimit());
				saveVehicle.setWindScreenCoverRequeired(motorDatas.getWindScreenCoverRequired());
				saveVehicle.setMotorUsage(motorDatas.getMotorUsage());
				saveVehicle.setGpsYn(motorDatas.getGpsTrackingInstalled());
				saveVehicle.setGroupCount(1);
				saveVehicle.setCityLimit(StringUtils.isBlank(motorDatas.getCityLimit())?"0": motorDatas.getCityLimit());
				saveVehicle.setWindScreenSumInsuredLc(motorDatas.getWindScreenSumInsuredLc());
				saveVehicle.setAcccessoriesSumInsuredLc(motorDatas.getAcccessoriesSumInsuredLc());
				saveVehicle.setWindScreenSumInsured(motorDatas.getWindScreenSumInsured());
				saveVehicle.setAcccessoriesSumInsured(motorDatas.getAcccessoriesSumInsured());
				saveVehicle.setHavepromocode(motorDatas.getHavepromocode());
				saveVehicle.setPromocode(StringUtils.isBlank(motorDatas.getPromocode())?"": motorDatas.getPromocode());
				saveVehicle.setEndtTypeId(motorDatas.getEndorsementType()==null?0 :motorDatas.getEndorsementType() ) ;
				saveVehicle.setEndtCategoryId(motorDatas.getIsFinaceYn()==null?"N" :motorDatas.getIsFinaceYn() );
				saveVehicle.setTpdIncreaseLimit(motorDatas.getTppdIncreaeLimit());
				saveVehicle.setTppdIncreaeLimitLc(motorDatas.getTppdIncreaeLimitLc());
				saveVehicle.setCarAlarmYn(StringUtils.isNotBlank(motorDatas.getCarAlarmYn()) ? motorDatas.getCarAlarmYn() :"N") ;
				saveVehicle.setUwLoading(BigDecimal.ZERO);
				//Double grossWeight =  Double.valueOf(df.format(motorDatas.getGrossWeight()))/ 1000;
//				saveVehicle.setExcess(motorDatas.getExcess()== null  ? BigDecimal.ZERO:new BigDecimal(motorDatas.getExcess()));
				saveVehicle.setExcess(StringUtils.isBlank(motorDatas.getExcess())  ? "" :motorDatas.getExcess());
				saveVehicle.setVehicleWeight(grossWeight);
				saveVehicle.setVehicleValueType(StringUtils.isBlank(motorDatas.getVehicleValueType())? "":motorDatas.getVehicleValueType());
				saveVehicle.setInflation(StringUtils.isBlank(motorDatas.getInflation())? "":motorDatas.getInflation());
				saveVehicle.setNcb(motorDatas.getNcb()==null? "":motorDatas.getNcb());
				saveVehicle.setDefenceValue(StringUtils.isBlank(motorDatas.getDefenceValue())? "":motorDatas.getDefenceValue());
				saveVehicle.setRegistrationDate(motorDatas.getRegistrationDate()==null?null:motorDatas.getRegistrationDate());
				saveVehicle.setPurchaseDate(motorDatas.getPurchaseDate()==null?null:motorDatas.getPurchaseDate());
				saveVehicle.setNcdyears(motorDatas.getNcdYears()==null?0:motorDatas.getNcdYears());
				saveVehicle.setCustRenewalYn(motorDatas.getCustRenewalYn()==null?"N":motorDatas.getCustRenewalYn());
				saveVehicle.setZone(motorDatas.getZone()==null?null:motorDatas.getZone());
				saveVehicle.setHorsePower(motorDatas.getHorsePower()==null?null:motorDatas.getHorsePower());
				saveVehicle.setPaCoverId(motorDatas.getPaCoverId()==null?null:motorDatas.getPaCoverId());
                saveVehicle.setNoOfPassengers(motorDatas.getNoOfPassengers()==null?null:motorDatas.getNoOfPassengers());
                saveVehicle.setCoverId(motorDatas.getCoverId()!=null?motorDatas.getCoverId():0);
                saveVehicle.setCreditShortfallSi(motorDatas.getCreditShortfallSi());
                saveVehicle.setImportYN(motorDatas.getImportYN());
                vehicleid=msVehicleRepo.saveAndFlush(saveVehicle).getVehicleId();
				
				//res.setMsVehicleDetails(saveVehicle);

			}
			res.setVdRefNo(vdRefNo);
			res.setSectionId(sectionId);
			res.setAgencyCode(agencyCode);
			res.setBranchCode(branchCode);
			res.setProductId(productId);
			res.setCompanyId(companyId);
			res.setLocationId(StringUtils.isBlank(locationId)?"1":locationId);
			
			res.setVehicleId(vehicleid);
			resList.add(res);
		}catch (Exception e) {
			e.printStackTrace();
			log.error("Exception is ---> " + e.getMessage());
			return null ;
		}

		return resList;
	}
	
	
	@Transactional
	private synchronized  String call_MSDriver(OneTimeTableReq request) {
		List<OneTimeVehicleRes> resList = new ArrayList<OneTimeVehicleRes>();
		OneTimeVehicleRes res = new  OneTimeVehicleRes();
		List<MsDriverDetails> list = new ArrayList<MsDriverDetails>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String ddRefNo = "" ;
		String productId = "" ;
		String companyId = "" ;
		
		DozerBeanMapper dozerMapper = new DozerBeanMapper();
		try {
			
			boolean newEntry = false ;
			productId =  motorDriverDatas.getProductId().toString();
			companyId =  motorDriverDatas.getCompanyId();

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<MsDriverDetails> query = cb.createQuery(MsDriverDetails.class);

			// Find All
			Root<MsDriverDetails> b = query.from(MsDriverDetails.class);

			// Select
			query.select(b);
			Predicate n1 = cb.equal(b.get("riskId"), motorDriverDatas.getRiskId());		
			Predicate n2 = (cb.like(cb.lower(b.get("requestReferenceNo")), motorDriverDatas.getRequestReferenceNo().toString().toLowerCase()));	
			Predicate n4 = (cb.like(cb.lower(b.get("driverType")),StringUtils.isNotBlank(motorDriverDatas.getDriverType())? motorDriverDatas.getDriverType().toLowerCase():""));
			Predicate n5 = (cb.like(cb.lower(b.get("gender")), motorDriverDatas.getDriverGender().toLowerCase()));
			Predicate n6 = (cb.like(cb.lower(b.get("stateId")), motorDriverDatas.getStateId()));
			Predicate n7 = (cb.like(cb.lower(b.get("maritalStatus")), motorDriverDatas.getMaritalStatus().toLowerCase()));
			Predicate n8 = (cb.like(cb.lower(b.get("countryId")), motorDriverDatas.getCountryId().toLowerCase()));
//			Predicate n9 = (cb.like(cb.lower(b.get("countryName")), motorDriverDatas.getCountryName().toLowerCase()));
//			Predicate n10 = (cb.like(cb.lower(b.get("claimType")), motorDriverDatas.getClaimType().toLowerCase()));				
			Predicate n11 = cb.equal(b.get("areaGroup"), motorDriverDatas.getAreaGroup());
			Predicate n12 = cb.equal(b.get("driverId"), motorDriverDatas.getDriverId());
//			Date driverDob = sdf.parse(motorDriverDatas.getDriverDob().toString());
//			Calendar cal = new GregorianCalendar();
//			cal.setTime(driverDob);
//			cal.add(Calendar.DAY_OF_MONTH, 0);cal.set(Calendar.HOUR_OF_DAY, 23);cal.set(Calendar.MINUTE, 59);
//			Date dob = cal.getTime() ;
//			Predicate n14= cb.equal(b.get("driverDob"),dob);
			Predicate n14= cb.equal(b.get("driverDob"),motorDriverDatas.getDriverDob());
//			Predicate n15 = cb.equal(b.get("licenseIssueDt"), motorDriverDatas.getLicenseIssueDt());
			Predicate n16 = cb.equal(b.get("licenseExperience"), motorDriverDatas.getLicenseExperience());
			Predicate n17 = cb.equal(b.get("age"), motorDriverDatas.getDriverAge());
			Predicate n18 = cb.equal(b.get("cityId"), motorDriverDatas.getCityId());
			Predicate n19 = cb.equal(b.get("suburbId"), motorDriverDatas.getSuburbId());
			Predicate n20 = cb.equal(b.get("endorsementType"), motorDriverDatas.getEndorsementType()==null?0:motorDriverDatas.getEndorsementType());
			Predicate n21 = cb.equal(b.get("status"),"Y");
			Predicate n22 = cb.equal(b.get("locationId"), 1);	
			Predicate n23 = cb.equal(b.get("claimExperience"), motorDriverDatas.getClaimExperience());
			Predicate n24 = cb.equal(b.get("claimType"), motorDriverDatas.getClaimType());


			query.where(n1,n2,n4,n5,n6,n7,n8,
					n11,n12,n16,n17,n18,n19
					,n20,n21,n22,n23,n24
					);
			// Get Result
			TypedQuery<MsDriverDetails> result = em.createQuery(query);
			list = result.getResultList();		


			if (list==null || list.size() <= 0 ) {
				newEntry = true ;

			} else {
				ddRefNo = String.valueOf(list.get(0).getDdRefno());
//				res.setMsVehicleDetails(list.get(0));
			}
			if(newEntry==true  ) {
				ddRefNo = genOneTimeTableRefNo() ;  

				MsDriverDetails saveDriver = new MsDriverDetails();
				dozerMapper.map(motorDriverDatas, saveDriver);
				saveDriver.setRiskId(motorDriverDatas.getRiskId());
				saveDriver.setLocationId(1);
				saveDriver.setDdRefno(Long.valueOf(ddRefNo) );
				saveDriver.setEntryDate(new Date());
				saveDriver.setStatus(motorDriverDatas.getStatus());
				saveDriver.setAge(motorDriverDatas.getDriverAge()== null  ? 18:motorDriverDatas.getDriverAge());
				saveDriver.setAreaGroup(motorDriverDatas.getAreaGroup()== null  ? 0:motorDriverDatas.getAreaGroup());
				saveDriver.setCityId(motorDriverDatas.getCityId()== null  ? 0:motorDriverDatas.getCityId());
				saveDriver.setCountryId(motorDriverDatas.getCountryId()== null  ? "Kenya":motorDriverDatas.getCountryId());
				saveDriver.setCountryName(motorDriverDatas.getCountryName()== null  ? "Kenya":motorDriverDatas.getCountryName());
				saveDriver.setClaimType(motorDriverDatas.getClaimType()== null  ? "0":motorDriverDatas.getClaimType());
				saveDriver.setDriverDob(motorDriverDatas.getDriverDob()== null  ? new Date():motorDriverDatas.getDriverDob());
				saveDriver.setDriverId(motorDriverDatas.getDriverId()== null  ? 0:motorDriverDatas.getDriverId());
				saveDriver.setEndorsementType(motorDriverDatas.getEndorsementType()== null  ? 0:motorDriverDatas.getEndorsementType());
				saveDriver.setGender(motorDriverDatas.getDriverGender()== null  ? "M" : motorDriverDatas.getDriverGender());
				saveDriver.setLicenseExperience(motorDriverDatas.getLicenseExperience()== null ? 0: motorDriverDatas.getLicenseExperience());
				saveDriver.setLicenseIssueDt(motorDriverDatas.getLicenseIssueDt()== null ? new Date() :motorDriverDatas.getLicenseIssueDt());
				saveDriver.setMaritalStatus(motorDriverDatas.getMaritalStatus()== null?" ":motorDriverDatas.getMaritalStatus());
				saveDriver.setRequestReferenceNo(motorDriverDatas.getRequestReferenceNo()== null  ? " ":motorDriverDatas.getRequestReferenceNo());
				saveDriver.setStateId(motorDriverDatas.getStateId()== "0"  ? null:motorDriverDatas.getStateId());
				saveDriver.setDriverType(motorDriverDatas.getDriverType()== null  ? " " :motorDriverDatas.getDriverType());
				saveDriver.setSuburbId(motorDriverDatas.getSuburbId()== null  ? 0 :motorDriverDatas.getSuburbId());
				saveDriver.setClaimExperience(motorDriverDatas.getClaimExperience()== null  ? 0 :motorDriverDatas.getClaimExperience());
				saveDriver.setClaimType(motorDriverDatas.getClaimType()== null  ? "0" :motorDriverDatas.getClaimType());
				msDriverRepo.saveAndFlush(saveDriver);

			}
			res.setDdRefNo(ddRefNo);
			res.setProductId(productId);
			res.setCompanyId(companyId);
			res.setVehicleId(motorDriverDatas.getRiskId().toString());
			res.setLocationId("1");
			resList.add(res);
			return ddRefNo;
		}catch (Exception e) {
			e.printStackTrace();
			log.error("Exception is ---> " + e.getMessage());
			return null ;
		}

		//return resList;
	}
	
	public Integer currencyDecimalFormat(String insuranceId  ,String currencyId ) {
		Integer decimalFormat = 0 ;
		try {
			Date today = new Date();
			Calendar cal = new GregorianCalendar();
			cal.setTime(today);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 1);
			today = cal.getTime();
			cal.set(Calendar.HOUR_OF_DAY, 1);
			cal.set(Calendar.MINUTE, 1);
			Date todayEnd = cal.getTime();
			
			// Criteria
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<CurrencyMaster> query = cb.createQuery(CurrencyMaster.class);
			List<CurrencyMaster> list = new ArrayList<CurrencyMaster>();
			
			// Find All
			Root<CurrencyMaster>    c = query.from(CurrencyMaster.class);		
			
			// Select
			query.select(c);
			
		
			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(c.get("currencyName")));
			
			// Effective Date Max Filter
			Subquery<Date> effectiveDate = query.subquery(Date.class);
			Root<CurrencyMaster> ocpm1 = effectiveDate.from(CurrencyMaster.class);
			effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
			Predicate a11 = cb.equal(c.get("currencyId"),ocpm1.get("currencyId") );
			Predicate a12 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
			Predicate a18 = cb.equal(c.get("status"),ocpm1.get("status") );
			Predicate a22 = cb.equal(c.get("companyId"), ocpm1.get("companyId"));
			
			effectiveDate.where(a11,a12,a18,a22);
			
			// Effective Date Max Filter
			Subquery<Date> effectiveDate2 = query.subquery(Date.class);
			Root<CurrencyMaster> ocpm2 = effectiveDate2.from(CurrencyMaster.class);
			effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
			Predicate a13 = cb.equal(c.get("currencyId"),ocpm2.get("currencyId") );
			Predicate a14 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
			Predicate a19 = cb.equal(c.get("status"),ocpm2.get("status") );
			Predicate a23 = cb.equal(c.get("companyId"), ocpm2.get("companyId"));
			
			effectiveDate2.where(a13,a14,a19,a23);
			
		    // Where	
			Predicate n1 = cb.equal(c.get("status"), "Y");
			Predicate n2 = cb.equal(c.get("effectiveDateStart"), effectiveDate);
			Predicate n3 = cb.equal(c.get("effectiveDateEnd"), effectiveDate2);
			Predicate n4 = cb.equal(c.get("companyId"),insuranceId);
//			Predicate n5 = cb.equal(c.get("companyId"),"99999");
//			Predicate n6 = cb.or(n4,n5);
			Predicate n7 = cb.equal(c.get("currencyId"),currencyId);
			query.where(n1,n2,n3,n4,n7).orderBy(orderList);
			
			// Get Result
			TypedQuery<CurrencyMaster> result = em.createQuery(query);			
			list =  result.getResultList(); 
			
			decimalFormat = list.size() > 0 ? (list.get(0).getDecimalDigit()==null?0 :list.get(0).getDecimalDigit()) :0; 		
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null;
		}
		return decimalFormat;
	}
	
	public synchronized List<ListItemValue> getListItem(String insId , String branchCode , String itemType) {
		List<ListItemValue> list = new ArrayList<ListItemValue>();
		try {
			Date today = new Date();
			Calendar cal = new GregorianCalendar();
			cal.setTime(today);
			today = cal.getTime();
			Date todayEnd = cal.getTime();
			
			// Criteria
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<ListItemValue> query=  cb.createQuery(ListItemValue.class);
			// Find All
			Root<ListItemValue> c = query.from(ListItemValue.class);
			
			//Select
			query.select(c);
			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(c.get("branchCode")));
			
			
			// Effective Date Start Max Filter
			Subquery<Date> effectiveDate = query.subquery(Date.class);
			Root<ListItemValue> ocpm1 = effectiveDate.from(ListItemValue.class);
			effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
			Predicate a1 = cb.equal(c.get("itemId"),ocpm1.get("itemId"));
			Predicate a2 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
			Predicate b1= cb.equal(c.get("branchCode"),ocpm1.get("branchCode"));
			Predicate b2 = cb.equal(c.get("companyId"),ocpm1.get("companyId"));
			effectiveDate.where(a1,a2,b1,b2);
			
			// Effective Date End Max Filter
			Subquery<Date> effectiveDate2 = query.subquery(Date.class);
			Root<ListItemValue> ocpm2 = effectiveDate2.from(ListItemValue.class);
			effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
			Predicate a3 = cb.equal(c.get("itemId"),ocpm2.get("itemId"));
			Predicate a4 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
			Predicate b3= cb.equal(c.get("companyId"),ocpm2.get("companyId"));
			Predicate b4= cb.equal(c.get("branchCode"),ocpm2.get("branchCode"));
			effectiveDate2.where(a3,a4,b3,b4);
						
			// Where
			Predicate n1 = cb.equal(c.get("status"),"Y");
			Predicate n2 = cb.equal(c.get("effectiveDateStart"),effectiveDate);
			Predicate n3 = cb.equal(c.get("effectiveDateEnd"),effectiveDate2);	
			Predicate n4 = cb.equal(c.get("companyId"), insId);
	//		Predicate n5 = cb.equal(c.get("companyId"), "99999");
			Predicate n6 = cb.equal(c.get("branchCode"), branchCode);
			Predicate n7 = cb.equal(c.get("branchCode"), "99999");
	//		Predicate n8 = cb.or(n4,n5);
			Predicate n9 = cb.or(n6,n7);
			Predicate n10 = cb.equal(c.get("itemType"),itemType);
			query.where(n1,n2,n3,n4,n9,n10).orderBy(orderList);
			// Get Result
			TypedQuery<ListItemValue> result = em.createQuery(query);
			list = result.getResultList();
			
			list = list.stream().filter(distinctByKey(o -> Arrays.asList(o.getItemCode()))).collect(Collectors.toList());
			list.sort(Comparator.comparing(ListItemValue :: getItemValue));
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null; 
		}
		return list ;
	}

	private static <T> java.util.function.Predicate<T> distinctByKey(java.util.function.Function<? super T, ?> keyExtractor) {
	    Map<Object, Boolean> seen = new ConcurrentHashMap<>();
	    return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}
	
	
	@Transactional
	private synchronized List<OneTimeVehicleRes> call_MSTravel(OneTimeTableReq request) {
		List<OneTimeVehicleRes> resList = new ArrayList<OneTimeVehicleRes>();
		OneTimeVehicleRes res = new  OneTimeVehicleRes();
		List<MsHumanDetails> list = new ArrayList<MsHumanDetails>();

		String vdRefNo = "" ;
		String sectionId = "" ;
		String agencyCode = "" ;
		String branchCode = "" ;
		String productId = "" ;
		String companyId = "" ;
		String locationId="";
	//	SimpleDateFormat sdf = new SimpleDateFormat("yyMMddhhmmssSS");
		String pattern = "#####0.00";
		DecimalFormat df = new DecimalFormat(pattern);
		DozerBeanMapper dozerMapper = new DozerBeanMapper();
		try {
			
			boolean newEntry = true ;
			sectionId = travelData.getSectionId().toString() ;
			branchCode = travelData.getBranchCode();
			agencyCode =  travelData.getBrokerCode();
			productId =  travelData.getProductId().toString();
			companyId =  travelData.getCompanyId();
		//	Double grossWeight = Double.valueOf(df.format(motorDatas.getGrossWeight()/ 1000));


//			CriteriaBuilder cb = em.getCriteriaBuilder();
//			CriteriaQuery<MsHumanDetails> query = cb.createQuery(MsHumanDetails.class);
//
//			// Find All
//			Root<MsHumanDetails> b = query.from(MsHumanDetails.class);
//
//			// Select
//			query.select(b);
//			Predicate n7 = cb.equal(cb.lower(b.get("createdBy")), travelData.getCreatedBy().toString().toLowerCase());
//			Predicate n8 = cb.equal(cb.lower(b.get("covidCoverYn")), travelData.getCovidCoverYn().toString().toLowerCase());
//			Predicate n9 = cb.equal(cb.lower(b.get("currency")), travelData.getCurrency().toString().toLowerCase());
//			Predicate n10 = cb.equal(cb.lower(b.get("destinationCountry")), travelData.getDestinationCountry().toString().toLowerCase());
//			Predicate n11 = cb.equal(b.get("exchangeRate"), travelData.getExchangeRate());
//			Predicate n12 = cb.equal(cb.lower(b.get("havepromocode")), travelData.getHavepromocode().toString().toLowerCase());
//			Predicate n13 = cb.equal(b.get("planTypeId"), travelData.getPlanTypeId());
//			Predicate n14 = cb.equal(cb.lower(b.get("promocode")), travelData.getPromocode().toString().toLowerCase());
//			Predicate n15 = cb.equal(cb.lower(b.get("sourceCountry")), travelData.getSourceCountry().toString().toLowerCase());
//			Predicate n16 = cb.equal(cb.lower(b.get("sportsCoverYn")), travelData.getSportsCoverYn().toString().toLowerCase());
//			Predicate n17 = cb.equal(cb.lower(b.get("terrorismCoverYn")), travelData.getTerrorismCoverYn().toString().toLowerCase());
//			Predicate n18 = cb.equal(b.get("totalPassengers"), travelData.getTotalPassengers());
//			Predicate n19 = cb.equal(b.get("travelCoverDuration"), travelData.getTravelCoverDuration());
//			Predicate n20 = cb.equal(b.get("travelCoverId"), travelData.getTravelCoverId());
//			Predicate n21 = cb.equal(b.get("travelId"), travelData.getTravelId());
//			Predicate n22 = cb.equal(b.get("requestReferenceNo"), travelData.getRequestReferenceNo());
//			Predicate n23 = cb.equal(b.get("groupId"), groupData.getGroupId());
//			Predicate n24 = cb.equal(b.get("groupCount"), groupData.getGrouppMembers());
//			
//			query.where(n7,n8,n9,n10,n11,n12,n13,n14,n15,n16,n17,n18,n19,n20,n21,n22,n23,n24) ;			
//			// Get Result
//			TypedQuery<MsHumanDetails> result = em.createQuery(query);
//			list = result.getResultList();		
//
//
//			if (list==null || list.size() <= 0 ) {
//				newEntry = true ;
//
//			} else {
//				vdRefNo = String.valueOf(list.get(0).getVdRefno());
//				
//			}
			if(newEntry==true  ) {
			//	Random rand = new Random();
	        //  int random=rand.nextInt(90)+10;
				vdRefNo = genOneTimeTableRefNo() ; //sdf.format(new Date()) + random ;
			//	SectionCoverMaster covageageLimit=getCoverageLimit(request);
//				if(travelData.getPlanTypeId() == 3 ) {
//					MsHumanDetails saveHuman = new MsHumanDetails();
//					dozerMapper.map(travelData, saveHuman);
//					saveHuman.setHumanId(Integer.valueOf(3));
//					saveHuman.setVdRefno(Long.valueOf(vdRefNo) );
//					saveHuman.setEntryDate(new Date());
//					saveHuman.setGroupId(Integer.valueOf(3));
//					saveHuman.setSumInsured(covageageLimit.getCoverageLimit());
//					//saveHuman.setSumInsured(BigDecimal.ONE);
//					saveHuman.setGroupCount(1);
//					saveHuman.setPeriodOfInsurance(travelData.getTravelCoverDuration().toString());
//					saveHuman.setExchangeRate(BigDecimal.ONE);
//					saveHuman.setCurrency("USD");
//					saveHuman.setEndtTypeId(travelData.getEndorsementType()==null?0 :travelData.getEndorsementType() ) ;
//					saveHuman.setEndtCategoryId(travelData.getIsFinaceYn()==null?"N" :travelData.getIsFinaceYn() );
//
//
//					msHumanRepo.saveAndFlush(saveHuman);
//				} else {
					for(TravelGroupInsertReq data   : request.getGroupDetails() ) {
						
						List<String> skipPlanTypeCompanies = Arrays.asList("100046", "100047", "100048", "100049", "100050");
						String companyIdStr = String.valueOf(travelData.getCompanyId());
						boolean skipPlanTypeCheck = skipPlanTypeCompanies.contains(companyIdStr);
						

						if (skipPlanTypeCompanies.contains(travelData.getCompanyId())) {

						    MsHumanDetails saveHuman = new MsHumanDetails();
						    dozerMapper.map(travelData, saveHuman);
						    saveHuman.setHumanId(Integer.valueOf(data.getRiskId()));
						    saveHuman.setVdRefno(Long.valueOf(vdRefNo));
						    saveHuman.setEntryDate(new Date());
						    saveHuman.setGroupId(Integer.valueOf(data.getGroupId()));
						    saveHuman.setGroupCount(Integer.valueOf(data.getGroupMembers()));
						    saveHuman.setPeriodOfInsurance(travelData.getTravelCoverDuration());
						    saveHuman.setExchangeRate(BigDecimal.ONE);
						    saveHuman.setCurrency(travelData.getCurrency());
						    saveHuman.setEndtTypeId(travelData.getEndorsementType() == null ? 0 : travelData.getEndorsementType());
						    saveHuman.setEndtCategoryId(travelData.getIsFinyn() == null ? "N" : travelData.getIsFinyn());
						    saveHuman.setUwLoading(BigDecimal.ZERO);
						    saveHuman.setInsuranceClass("99999");
						    saveHuman.setLocationId(Integer.valueOf(request.getLocationId()));
						    msHumanRepo.saveAndFlush(saveHuman);

						} 
						
						else if (!skipPlanTypeCheck && travelData.getPlanTypeId() != null 
						         && !(travelData.getPlanTypeId().equals(3) && data.getGroupId().equalsIgnoreCase("1"))) {

						    MsHumanDetails saveHuman = new MsHumanDetails();
						    dozerMapper.map(travelData, saveHuman);
						    saveHuman.setHumanId(Integer.valueOf(data.getRiskId()));
						    saveHuman.setVdRefno(Long.valueOf(vdRefNo));
						    saveHuman.setEntryDate(new Date());
						    saveHuman.setGroupId(Integer.valueOf(data.getGroupId()));
						    
						    if (skipPlanTypeCheck) {
						        saveHuman.setGroupCount(Integer.valueOf(data.getGroupMembers()));
						    } else {
						        saveHuman.setGroupCount(travelData.getPlanTypeId().equals(3) ? 1 : Integer.valueOf(data.getGroupMembers()));
						    }

						    saveHuman.setPeriodOfInsurance(travelData.getTravelCoverDuration());
						    saveHuman.setExchangeRate(travelData.getExchangeRate());
						    saveHuman.setCurrency(travelData.getCurrency());
						    saveHuman.setEndtTypeId(travelData.getEndorsementType() == null ? 0 : travelData.getEndorsementType());
						    saveHuman.setEndtCategoryId(travelData.getIsFinyn() == null ? "N" : travelData.getIsFinyn());
						    saveHuman.setUwLoading(BigDecimal.ZERO);
						    saveHuman.setInsuranceClass("99999");
						    saveHuman.setLocationId(Integer.valueOf(request.getLocationId()));
						    msHumanRepo.saveAndFlush(saveHuman);
						}
					}
	
				}
				
		
//			}
			res.setLocationId(request.getLocationId()+"");			
			res.setVdRefNo(vdRefNo);
			res.setSectionId(sectionId);
			res.setAgencyCode(agencyCode);
			res.setBranchCode(branchCode);
			res.setProductId(productId);
			res.setCompanyId(companyId);
			resList.add(res);
		}catch (Exception e) {
			e.printStackTrace();
			log.error("Exception is ---> " + e.getMessage());
			return null ;
		}

		return resList;
	}
	
	@Transactional
	private synchronized List<OneTimeVehicleRes> call_MSHuman(OneTimeTableReq request) {
		List<OneTimeVehicleRes> resList = new ArrayList<OneTimeVehicleRes>();
		OneTimeVehicleRes res = new  OneTimeVehicleRes();
		List<MsHumanDetails> list = new ArrayList<MsHumanDetails>();

		String vdRefNo = "" ;
		String sectionId = "" ;
		String agencyCode = "" ;
		String branchCode = "" ;
		String productId = "" ;
		String companyId = "" ;
		DozerBeanMapper dozerMapper = new DozerBeanMapper();
		
		try {
			EserviceCommonDetails commonData = esercommonDatas.get(0) ;
			boolean newEntry = false ;
			sectionId = commonData.getSectionId() ;
			branchCode = commonData.getBranchCode();
			agencyCode =  commonData.getBrokerCode();
			productId =  commonData.getProductId();
			companyId =  commonData.getCompanyId();
			
			
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<MsHumanDetails> query = cb.createQuery(MsHumanDetails.class);

			// Find All
			Root<MsHumanDetails> b = query.from(MsHumanDetails.class);

			// Select
			query.select(b);
			Predicate n1 = (cb.like(cb.lower(b.get("requestReferenceNo")), commonData.getRequestReferenceNo().toString().toLowerCase()));		
			Predicate n4 = (cb.like(cb.lower(b.get("createdBy")), commonData.getCreatedBy().toString().toLowerCase()));		
			Predicate n5 = (cb.equal(b.get("humanId"), commonData.getRiskId()));		
			Predicate n8 = (cb.like(cb.lower(b.get("status")), commonData.getStatus().toString().toLowerCase()));		
			Predicate n9 = (cb.equal(b.get("periodOfInsurance"), commonData.getPolicyPeriod()));		
			Predicate n10 = (cb.like(cb.lower(b.get("currency")), commonData.getCurrency().toString().toLowerCase()));		
			Predicate n11 = (cb.equal(b.get("exchangeRate"), commonData.getExchangeRate()));		
			Predicate n12 = (cb.equal(b.get("sumInsured"), commonData.getSumInsured()));		
			Predicate n13 = (cb.equal(b.get("categoryId"), commonData.getCategoryId()));		
			Predicate n14 = cb.equal(cb.lower(b.get("havepromocode")), commonData.getHavepromocode().toString().toLowerCase());
			Predicate n15 = cb.equal(cb.lower(b.get("promocode")), commonData.getPromocode()==null?"" : commonData.getPromocode().toString().toLowerCase());
			Predicate n16 = cb.equal(cb.lower(b.get("groupId")), 1);
			Predicate n17 = cb.equal(cb.lower(b.get("groupCount")), 1);
			Predicate n40 = (cb.equal(b.get("endtTypeId"),commonData.getEndorsementType()==null?0 :commonData.getEndorsementType() ));
			Predicate n41 = (cb.equal(b.get("endtCategoryId"),commonData.getIsFinyn()==null?"N" :commonData.getIsFinyn() ));
			Predicate n42 = (cb.equal(b.get("natureOfBusinessId"),commonData.getNatureOfBusinessId()) );
			Predicate n43 = (cb.equal(b.get("totalNoOfEmployees"),commonData.getTotalNoOfEmployees()));
			Predicate n44 = (cb.equal(b.get("totalExcludedEmployees"),commonData.getTotalExcludedEmployees()));
			Predicate n45 = (cb.equal(b.get("totalRejoinedEmployees"),commonData.getTotalRejoinedEmployees()));
			Predicate n46 = (cb.equal(b.get("accountOutstandingEmployees"),commonData.getAccountOutstandingEmployees()));
			Predicate n47 = (cb.equal(b.get("accountAuditentType"),commonData.getAccountAuditentType()));
			
			query.where(n1,n4,n5,n8,n9,n10,n11,n12,n13,n14,n15 ,n16,n17,n40,n41,n42,n43,n44,n45,n46,n47 );					
			// Get Result
			TypedQuery<MsHumanDetails> result = em.createQuery(query);
			list = result.getResultList();		


			if (list==null || list.size() <= 0 ) {
				newEntry = true ;

			} else {
				vdRefNo = String.format("%05d",list.get(0).getVdRefno());
			}
		
			if(newEntry==true  ) {
				vdRefNo = genOneTimeTableRefNo() ; 
				MsHumanDetails saveHuman = new MsHumanDetails();
				dozerMapper.map(commonData, saveHuman);
				saveHuman.setHumanId(Integer.valueOf(commonData.getRiskId()));
				saveHuman.setVdRefno(Long.valueOf(vdRefNo) );
				saveHuman.setEntryDate(new Date());
				saveHuman.setGroupId(Integer.valueOf(1));
				saveHuman.setSumInsured(commonData.getSumInsured());
				saveHuman.setGroupCount(1);
				saveHuman.setPeriodOfInsurance(commonData.getPolicyPeriod());
				saveHuman.setCategoryId(commonData.getCategoryId());
				saveHuman.setEndtTypeId(commonData.getEndorsementType()==null?0 :commonData.getEndorsementType() ) ;
				saveHuman.setEndtCategoryId(commonData.getIsFinyn()==null?"N" :commonData.getIsFinyn() );
				saveHuman.setNatureOfBusinessId(commonData.getNatureOfBusinessId());
				saveHuman.setTotalNoOfEmployees(commonData.getTotalNoOfEmployees());
				saveHuman.setTotalExcludedEmployees(commonData.getTotalExcludedEmployees());
				saveHuman.setTotalRejoinedEmployees(commonData.getTotalRejoinedEmployees());
				saveHuman.setAccountOutstandingEmployees(commonData.getAccountOutstandingEmployees());
				saveHuman.setAccountAuditentType(commonData.getAccountAuditentType());
				
				msHumanRepo.saveAndFlush(saveHuman);
				
			}
			res.setVdRefNo(vdRefNo);
			res.setSectionId(sectionId);
			res.setAgencyCode(agencyCode);
			res.setBranchCode(branchCode);
			res.setProductId(productId);
			res.setCompanyId(companyId);
			resList.add(res);
			
		}catch (Exception e) {
			e.printStackTrace();
			log.error("Exception is ---> " + e.getMessage());
			return null ;
		}

		return resList;
	}
	
	private synchronized List<OneTimeVehicleRes> call_MSAssetOrMsHuman(OneTimeTableReq request) {
		List<OneTimeVehicleRes> resList = new  ArrayList<OneTimeVehicleRes>();
	
		String vdRefNo = "" ;
		String sectionId = "" ;
		String agencyCode = "" ;
		String branchCode = "" ;
		String productId = "" ;
		String companyId = "" ;
		String riskId = "" ;
		String locationId="";
		String Coverid="";
		 String param1="";
		 String param2="";
		 String param3="";
		 String param4="";
		 String param5="";
		 String param6="";
		 String param7="";
		 String param8="";
		 String param9="";
		 String param10="";
		
		
		// SimpleDateFormat sdf = new SimpleDateFormat("yyMMddhhmmssSS");
		String pattern = "#####0.00";
		DecimalFormat df = new DecimalFormat(pattern);
	//	DozerBeanMapper dozerMapper = new DozerBeanMapper();
		String  originalRiskId = "1";
		try {
			
			
			boolean newEntry = true ;
			
		for ( BuildingSectionRes sec :    request.getSectionList()  ) {
			OneTimeVehicleRes res = new  OneTimeVehicleRes();
			
			if ( sec.getMotorYn().equalsIgnoreCase("H") ) {
				
				List<MsHumanDetails> list = new ArrayList<MsHumanDetails>();
//				List<EserviceCommonDetails> filterHuman =  esercommonDatas.stream().filter(  o -> 
//				o.getOccupationType().equals(sec.getOccupationId()) && o.getSectionId().equalsIgnoreCase(sec.getSectionId())  &&  o.getLocationId().equals(Integer.valueOf(request.getLocationId()))).collect(Collectors.toList());
				
				List<EserviceCommonDetails> filterHuman =  esercommonDatas.stream().filter(  o ->  o.getSectionId().equalsIgnoreCase(sec.getSectionId())  &&  o.getLocationId().equals(Integer.valueOf(request.getLocationId())) && String.valueOf(o.getCoverId()).equals(String.valueOf(sec.getCoverId()))).collect(Collectors.toList());
			
	
				List<EserviceCommonDetails> NewFilter =	filterHuman.stream().filter( a -> a.getRiskId()!= null  &&   
						a.getRiskId().equals(sec.getRiskId() != null ?Integer.valueOf(sec.getRiskId()) : null )  ).collect(Collectors.toList());	
				
				if(null != NewFilter && !NewFilter.isEmpty()) {
					
					// Should be save and return , constraints blocks
					
					originalRiskId =    sec.getOriginalRiskId() != null ? sec.getOriginalRiskId().toString(): "1" ;	
					
					riskId = sec.getRiskId() != null ? sec.getRiskId().toString():NewFilter.get(0).getRiskId().toString();
					filterHuman = NewFilter ;
				}
				
				
				
				EserviceCommonDetails commonData = filterHuman.get(0);
		//		newEntry = false ;
				sectionId = commonData.getSectionId() ;
				branchCode = commonData.getBranchCode();
				agencyCode =  commonData.getBrokerCode();
				productId =  commonData.getProductId();
				companyId =  commonData.getCompanyId();
				
				locationId= commonData.getLocationId().toString();
				riskId 	   = commonData.getRiskId().toString();
				Coverid = commonData.getCoverId().toString();
				param1=StringUtils.isBlank(commonData.getParam1())?"":commonData.getParam1();
				param2=StringUtils.isBlank(commonData.getParam2())?"":commonData.getParam2();
				param3=StringUtils.isBlank(commonData.getParam3())?"":commonData.getParam3();
				param4=StringUtils.isBlank(commonData.getParam4())?"":commonData.getParam4();
				param5=StringUtils.isBlank(commonData.getParam5())?"":commonData.getParam5();
				param6=StringUtils.isBlank(commonData.getParam6())?"":commonData.getParam6();
				param7=StringUtils.isBlank(commonData.getParam7())?"":commonData.getParam7();
				param8=StringUtils.isBlank(commonData.getParam8())?"":commonData.getParam8();
				param9=StringUtils.isBlank(commonData.getParam9())?"":commonData.getParam9();
				param10=StringUtils.isBlank(commonData.getParam10())?"":commonData.getParam10();
			/*	
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<MsHumanDetails> query = cb.createQuery(MsHumanDetails.class);

				// Find All
				Root<MsHumanDetails> b = query.from(MsHumanDetails.class);
				List<Predicate> predicate = new ArrayList<Predicate>();
				
				// Select
				query.select(b);
				predicate.add(cb.like(cb.lower(b.get("requestReferenceNo")), commonData.getRequestReferenceNo().toString().toLowerCase()));		
				predicate.add(cb.like(cb.lower(b.get("createdBy")), commonData.getCreatedBy().toString().toLowerCase()));		
				predicate.add(cb.equal(b.get("humanId"), commonData.getRiskId()));
				predicate.add(cb.equal(b.get("locationId"), commonData.getLocationId()));
				predicate.add(cb.like(cb.lower(b.get("status")), commonData.getStatus().toString().toLowerCase()));		
				predicate.add(cb.equal(b.get("periodOfInsurance"), commonData.getPolicyPeriod()));		
				predicate.add(cb.like(cb.lower(b.get("currency")), commonData.getCurrency().toString().toLowerCase()));		
				predicate.add(cb.equal(b.get("exchangeRate"), commonData.getExchangeRate()));		
				predicate.add(cb.equal(b.get("sumInsured"), commonData.getSumInsured()));		
				predicate.add(cb.equal(b.get("categoryId"), commonData.getCategoryId()));		
				predicate.add(cb.equal(cb.lower(b.get("havepromocode")), commonData.getHavepromocode()));
				predicate.add(cb.equal(b.get("groupId"), commonData.getGroupId() != null ? commonData.getGroupId() : 0));
				predicate.add(cb.equal(b.get("groupCount"), 1));
				predicate.add(cb.equal(b.get("count"),commonData.getCount() != null ? commonData.getCount() : 0 ));
				predicate.add(cb.equal(b.get("endtTypeId"),commonData.getEndorsementType()==null?0 :commonData.getEndorsementType() ));
				predicate.add(cb.equal(b.get("endtCategoryId"),commonData.getIsFinyn()==null?"N" :commonData.getIsFinyn() ));
				predicate.add(cb.equal(b.get("totalNoOfEmployees"),commonData.getTotalNoOfEmployees()));
				predicate.add(cb.equal(b.get("coverId"),commonData.getCoverId()==null?0 :commonData.getCoverId() ));

				
				predicate.add(commonData.getNatureOfBusinessId() == null ? cb.isNull(b.get("natureOfBusinessId") ) : 
					cb.equal( b.get("natureOfBusinessId") , commonData.getNatureOfBusinessId() ));
				
				predicate.add(commonData.getPromocode() == null ? cb.isNull(b.get("promocode"))
						: cb.equal(cb.lower(b.get("promocode")), commonData.getPromocode()));

				predicate
						.add(commonData.getTotalExcludedEmployees() == null ? cb.isNull(b.get("totalExcludedEmployees"))
								: cb.equal(b.get("totalExcludedEmployees"), commonData.getTotalExcludedEmployees()));

				predicate
						.add(commonData.getTotalRejoinedEmployees() == null ? cb.isNull(b.get("totalRejoinedEmployees"))
								: cb.equal(b.get("totalRejoinedEmployees"), commonData.getTotalRejoinedEmployees()));
				
				predicate.add(commonData.getAccountOutstandingEmployees() == null
						? cb.isNull(b.get("accountOutstandingEmployees"))
						: cb.equal(b.get("accountOutstandingEmployees"), commonData.getAccountOutstandingEmployees()));


				predicate.add(commonData.getAccountAuditentType() == null 
				    ? cb.isNull(b.get("accountAuditentType")) 
				    : cb.equal(b.get("accountAuditentType"), commonData.getAccountAuditentType()));
//
//				predicate.add(commonData.getIndustryName() == null 
//				    ? cb.isNull(b.get("industryName")) 
//				    : cb.equal(b.get("industryName"), commonData.getIndustryName()));

//				predicate.add(commonData.getLiabilitySi() == null 
//				    ? cb.isNull(b.get("liabilitySi")) 
//				    : cb.equal(b.get("liabilitySi"), commonData.getLiabilitySi()));

				predicate.add(commonData.getFidEmpCount() == null 
				    ? cb.isNull(b.get("fidEmpCount")) 
				    : cb.equal(b.get("fidEmpCount"), commonData.getFidEmpCount()));

//				predicate.add(commonData.getFidEmpSi() == null 
//				    ? cb.isNull(b.get("fidEmpSi")) 
//				    : cb.equal(b.get("fidEmpSi"), commonData.getFidEmpSi()));

//				predicate.add(commonData.getEmpLiabilitySi() == null 
//				    ? cb.isNull(b.get("empLiabilitySi")) 
//				    : cb.equal(b.get("empLiabilitySi"), commonData.getEmpLiabilitySi()));

				predicate.add(commonData.getPersonalLiabilityOccupation() == null 
				    ? cb.isNull(b.get("personalLiabilityOccupation")) 
				    : cb.equal(b.get("personalLiabilityOccupation"), commonData.getPersonalLiabilityOccupation()));

//				predicate.add(commonData.getPersonalLiabilitySi() == null 
//				    ? cb.isNull(b.get("personalLiabilitySi")) 
//				    : cb.equal(b.get("personalLiabilitySi"), commonData.getPersonalLiabilitySi()));

				predicate.add(commonData.getPersonalLiabilityCategory() == null 
				    ? cb.isNull(b.get("personalLiabilityCategory")) 
				    : cb.equal(b.get("personalLiabilityCategory"), commonData.getPersonalLiabilityCategory()));

				predicate.add(commonData.getSumInsuredLc() == null 
				    ? cb.isNull(b.get("sumInsuredLc")) 
				    : cb.equal(b.get("sumInsuredLc"), commonData.getSumInsuredLc()));

//				predicate.add(commonData.getLiabilitySiLc() == null 
//				    ? cb.isNull(b.get("liabilitySiLc")) 
//				    : cb.equal(b.get("liabilitySiLc"), commonData.getLiabilitySiLc()));

//				predicate.add(commonData.getFidEmpSiLc() == null 
//				    ? cb.isNull(b.get("fidEmpSiLc")) 
//				    : cb.equal(b.get("fidEmpSiLc"), commonData.getFidEmpSiLc()));

//				predicate.add(commonData.getEmpLiabilitySiLc() == null 
//				    ? cb.isNull(b.get("empLiabilitySiLc")) 
//				    : cb.equal(b.get("empLiabilitySiLc"), commonData.getEmpLiabilitySiLc()));

//				predicate.add(commonData.getPersonalLiabilitySiLc() == null 
//				    ? cb.isNull(b.get("personalLiabilitySiLc")) 
//				    : cb.equal(b.get("personalLiabilitySiLc"), commonData.getPersonalLiabilitySiLc()));

//				predicate.add(commonData.getAooSuminsured() == null 
//				    ? cb.isNull(b.get("aooSuminsured")) 
//				    : cb.equal(b.get("aooSuminsured"), commonData.getAooSuminsured()));

//				predicate.add(commonData.getAooSuminsuredLc() == null 
//				    ? cb.isNull(b.get("aooSuminsuredLc")) 
//				    : cb.equal(b.get("aooSuminsuredLc"), commonData.getAooSuminsuredLc()));

//				predicate.add(commonData.getAggSuminsured() == null 
//				    ? cb.isNull(b.get("aggSuminsured")) 
//				    : cb.equal(b.get("aggSuminsured"), commonData.getAggSuminsured()));

//				predicate.add(commonData.getAggSuminsuredLc() == null 
//				    ? cb.isNull(b.get("aggSuminsuredLc")) 
//				    : cb.equal(b.get("aggSuminsuredLc"), commonData.getAggSuminsuredLc()));

//				predicate.add(commonData.getProductTurnoverSi() == null 
//				    ? cb.isNull(b.get("productTurnoverSi")) 
//				    : cb.equal(b.get("productTurnoverSi"), commonData.getProductTurnoverSi()));

//				predicate.add(commonData.getProductTurnoverSiLc() == null 
//				    ? cb.isNull(b.get("productTurnoverSiLc")) 
//				    : cb.equal(b.get("productTurnoverSiLc"), commonData.getProductTurnoverSiLc()));

				predicate.add(commonData.getRelationType() == null 
				    ? cb.isNull(b.get("ratingRelationId")) 
				    : cb.equal(b.get("ratingRelationId"), commonData.getRelationType()));

				predicate.add(commonData.getAge() == null 
				    ? cb.isNull(b.get("age")) 
				    : cb.equal(b.get("age"), commonData.getAge()));
				

//				predicate.add(commonData.getDomesticServentSi() == null 
//					    ? cb.isNull(b.get("domesticServentSi")) 
//					    : cb.equal(b.get("domesticServentSi"), commonData.getDomesticServentSi()));
				predicate.add(commonData.getIndemnityType() == null 
					    ? cb.isNull(b.get("indemnityType")) 
					    : cb.equal(b.get("indemnityType"), commonData.getIndemnityType()));
				
				predicate.add(commonData.getRegionCode() == null 
					    ? cb.isNull(b.get("regionCode")) 
					    : cb.equal(b.get("regionCode"), commonData.getRegionCode()));

//				predicate.add(commonData.getDomesticServentSi() == null 
//					    ? cb.isNull(b.get("domesticServentSi")) 
//					    : cb.equal(b.get("domesticServentSi"), commonData.getDomesticServentSi()));

				predicate.add(cb.equal(b.get("coverId"),commonData.getCoverId()==null?0 :commonData.getCoverId() ));

				query.where(predicate.toArray(new Predicate[0]));					
				// Get Result
				TypedQuery<MsHumanDetails> result = em.createQuery(query);
				list = result.getResultList();		


				if (list==null || list.size() <= 0 ) {
					newEntry = true ;

				} else {
					vdRefNo = String.format("%05d",list.get(0).getVdRefno());
					riskId  = String.valueOf(list.get(0).getHumanId());
					locationId  = String.valueOf(list.get(0).getLocationId());
				}
			*
			*/
				
				if(newEntry==true  ) {
					vdRefNo = genOneTimeTableRefNo() ; 
					riskId  = String.valueOf(commonData.getRiskId());
					locationId  = String.valueOf(commonData.getLocationId());
					
					MsHumanDetails saveHuman = new MsHumanDetails();
					dozerMapper.map(commonData, saveHuman);
					saveHuman.setHumanId(commonData.getRiskId() );
//					saveHuman.setHumanId(commonData.getOriginalRiskId() );
					saveHuman.setVdRefno(Long.valueOf(vdRefNo) );
					saveHuman.setEntryDate(new Date());
					saveHuman.setGroupId(commonData.getGroupId());
//					saveHuman.setDomesticServentSi(commonData.getDomesticServentSi());
//					saveHuman.setDomesticServentSiLc(commonData.getDomesticServentSiLc());
					saveHuman.setSumInsured(commonData.getSumInsured());
					//saveHuman.setGroupCount(commonData.getTotalNoOfEmployees()==null ? Integer.valueOf(1): Integer.valueOf(commonData.getTotalNoOfEmployees().toString()));
					saveHuman.setGroupCount(Integer.valueOf(1));
					saveHuman.setPeriodOfInsurance(commonData.getPolicyPeriod());
					saveHuman.setCategoryId(commonData.getCategoryId());
					saveHuman.setEndtTypeId(commonData.getEndorsementType()==null?0 :commonData.getEndorsementType() ) ;
					saveHuman.setEndtCategoryId(commonData.getIsFinyn()==null?"N" :commonData.getIsFinyn() );
					saveHuman.setNatureOfBusinessId(commonData.getNatureOfBusinessId());
					saveHuman.setTotalNoOfEmployees(commonData.getTotalNoOfEmployees());
					saveHuman.setFidEmpCount(commonData.getFidEmpCount()==null?0l:Long.valueOf(commonData.getFidEmpCount().toString()));
					saveHuman.setTotalExcludedEmployees(commonData.getTotalExcludedEmployees());
					saveHuman.setTotalRejoinedEmployees(commonData.getTotalRejoinedEmployees());
					saveHuman.setAccountOutstandingEmployees(commonData.getAccountOutstandingEmployees());
					saveHuman.setAccountAuditentType(commonData.getAccountAuditentType());
					saveHuman.setEndtTypeId(commonData.getEndorsementType()==null?0 :commonData.getEndorsementType() ) ;
					saveHuman.setEndtCategoryId(commonData.getIsFinyn()==null?"N" :commonData.getIsFinyn() );
					saveHuman.setUwLoading(BigDecimal.ZERO);
					if(commonData.getCompanyId().equals("100020") && commonData.getProductId().equals("60")) {
					saveHuman.setPersonalLiabilityOccupation(commonData.getOccupationType());
					}
					saveHuman.setInsuranceClass("99999");
					saveHuman.setAge(commonData.getAge());
					saveHuman.setRatingRelationId(commonData.getRelationType());
					saveHuman.setProfessionalType(commonData.getProfessionalType());
					saveHuman.setIndemnityType(commonData.getIndemnityType());
					saveHuman.setParam1(StringUtils.isBlank(commonData.getParam1())?"":commonData.getParam1());
					saveHuman.setParam2(StringUtils.isBlank(commonData.getParam2())?"":commonData.getParam2());
					saveHuman.setParam3(StringUtils.isBlank(commonData.getParam3())?"":commonData.getParam3());
					saveHuman.setParam4(StringUtils.isBlank(commonData.getParam4())?"":commonData.getParam4());
					saveHuman.setParam5(StringUtils.isBlank(commonData.getParam5())?"":commonData.getParam5());
					saveHuman.setParam6(StringUtils.isBlank(commonData.getParam6())?"":commonData.getParam6());
					saveHuman.setParam7(StringUtils.isBlank(commonData.getParam7())?"":commonData.getParam7());
					saveHuman.setParam8(StringUtils.isBlank(commonData.getParam8())?"":commonData.getParam8());
					saveHuman.setParam9(StringUtils.isBlank(commonData.getParam9())?"":commonData.getParam9());
					saveHuman.setParam10(StringUtils.isBlank(commonData.getParam10())?"":commonData.getParam10());
					saveHuman.setSectionid(StringUtils.isBlank(commonData.getSectionId())?0 :Integer.valueOf(commonData.getSectionId()));
//					saveHuman.setIndemnitySuminsured(commonData.getIndemnitySuminsured());
//					saveHuman.setDomesticServentSi(commonData.getDomesticServentSi()==null?BigDecimal.ZERO:commonData.getDomesticServentSi());
					if (commonData.getCompanyId()!=null && commonData.getCompanyId().equalsIgnoreCase("100020")  ) {
						if(commonData.getSectionId()!=null && commonData.getSectionId().equalsIgnoreCase("120") ) {
							saveHuman.setGroupCount(commonData.getCount());
						}
						
						 if(commonData.getSectionId().equals("45")) {
		                    	
		                    saveHuman.setGroupCount(commonData.getCount());           
		                  }
					}
//					saveHuman.setTtdSumInsured(commonData.getTtdSumInsured() !=null ? commonData.getTtdSumInsured() : 0);
//                  saveHuman.setMeSumInsured(commonData.getMeSumInsured() !=null ? commonData.getMeSumInsured() : 0);
//                  saveHuman.setFeSumInsured(commonData.getFeSumInsured() !=null ? commonData.getFeSumInsured() : 0);
//                  saveHuman.setPtdSumInsured(commonData.getPtdSumInsured() != null ? commonData.getPtdSumInsured() : 0);
                    saveHuman.setGroupId(commonData.getGroupId() != null ? commonData.getGroupId() : 0);
					saveHuman.setLocationId(commonData.getLocationId()==null?1:commonData.getLocationId());
//  				saveHuman.setPersonalLiabilitySi(commonData.getPersonalLiabilitySi()==null?null:commonData.getPersonalLiabilitySi());
//					saveHuman.setPersonalLiabilitySiLc(commonData.getPersonalLiabilitySiLc()==null?null:commonData.getPersonalLiabilitySiLc());
					saveHuman.setCount(commonData.getCount()==null?null:commonData.getCount());
					saveHuman.setIndemnityType(commonData.getIndemnityType()==null?null:commonData.getIndemnityType());
					saveHuman.setCoverId(commonData.getCoverId()!=null?commonData.getCoverId():0);
				//	msHumanRepo.saveAndFlush(saveHuman);
					request.setMsHuman(saveHuman);
					sectionId = sec.getSectionId() ;
				}
				
//				riskId  = String.valueOf(commonData.getOriginalRiskId());
				
			} else {
				
				List<EserviceBuildingDetails> filterBuilding = eserBuildings.stream().filter(  o ->  o.getRiskId().equals(Integer.valueOf(request.getVehicleId()))
						&& o.getSectionId().equalsIgnoreCase(sec.getSectionId()) 
						&& String.valueOf(o.getCoverId()).equals(String.valueOf(sec.getCoverId()))
						&&  o.getLocationId().equals(Integer.valueOf(request.getLocationId())) ).collect(Collectors.toList());
				
				EserviceBuildingDetails eserBuild = filterBuilding.get(0);
				newEntry = true ;
				sectionId = eserBuild.getSectionId() ;
				branchCode = eserBuild.getBranchCode();
				agencyCode =  eserBuild.getBrokerCode();
				productId =  eserBuild.getProductId();
				companyId =  eserBuild.getCompanyId();
				riskId 	   = eserBuild.getRiskId().toString();
				locationId=eserBuild.getLocationId().toString();
				Coverid=eserBuild.getCoverId().toString();
				/*
				 * List<MsAssetDetails> list = new ArrayList<MsAssetDetails>(); CriteriaBuilder
				 * cb = em.getCriteriaBuilder();
				 * 
				 * CriteriaQuery<MsAssetDetails> query = cb.createQuery(MsAssetDetails.class);
				 * 
				 * // Find All Root<MsAssetDetails> b = query.from(MsAssetDetails.class);
				 * List<Predicate> predicate = new ArrayList<Predicate>(); // Select
				 * query.select(b); predicate.add(cb.like(cb.lower(b.get("requestReferenceNo")),
				 * eserBuild.getRequestReferenceNo().toString().toLowerCase()));
				 * predicate.add(cb.like(b.get("branchCode"),
				 * eserBuild.getBranchCode().toString()));
				 * 
				 * predicate.add(eserBuild.getBuildingAge() == null ?
				 * cb.isNull(b.get("buildingAge")) : cb.equal(b.get("buildingAge"),
				 * eserBuild.getBuildingAge()));
				 * 
				 * 
				 * predicate.add(eserBuild.getBuildingFloors() == null ?
				 * cb.isNull(b.get("buildingFloors")) : cb.equal(b.get("buildingFloors"),
				 * eserBuild.getBuildingFloors()));
				 * 
				 * 
				 * // predicate.add(eserBuild.getBuildingSuminsured() == null // ?
				 * cb.isNull(b.get("buildingSuminsured")) // :
				 * cb.equal(b.get("buildingSuminsured"), eserBuild.getBuildingSuminsured())); //
				 * predicate.add(eserBuild.getElecEquipSuminsured() == null // ?
				 * cb.isNull(b.get("elecEquipSuminsured")) // :
				 * cb.equal(b.get("elecEquipSuminsured"), eserBuild.getBuildingSuminsured()));
				 * 
				 * predicate.add(cb.like(b.get("companyId"),
				 * eserBuild.getCompanyId().toString()));
				 * predicate.add(cb.like(cb.lower(b.get("createdBy")),
				 * eserBuild.getCreatedBy().toString().toLowerCase()));
				 * 
				 * //Location Id -------------------------
				 * predicate.add(cb.equal(b.get("locationId"), eserBuild.getLocationId()));
				 * //RiskId-------------------------------
				 * predicate.add(cb.equal(b.get("riskId"), eserBuild.getRiskId()));
				 * 
				 * predicate.add(cb.equal(b.get("productId"), eserBuild.getProductId()));
				 * predicate.add(cb.equal(b.get("sectionId"), sec.getSectionId()));
				 * predicate.add(cb.like(cb.lower(b.get("status")),
				 * eserBuild.getStatus().toString().toLowerCase()));
				 * predicate.add(cb.equal(b.get("periodOfInsurance"),
				 * eserBuild.getPolicyPeriord()));
				 * predicate.add(cb.like(cb.lower(b.get("currency")),
				 * eserBuild.getCurrency().toString().toLowerCase()));
				 * predicate.add(cb.equal(b.get("exchangeRate"), eserBuild.getExchangeRate()));
				 * predicate.add(cb.equal(b.get("coverId"), eserBuild.getCoverId()));
				 * 
				 * predicate.add(eserBuild.getBuildingUsageId() == null ?
				 * cb.isNull(b.get("buildingUsageId")) : cb.like(b.get("buildingUsageId"),
				 * eserBuild.getBuildingUsageId()));
				 * 
				 * // predicate.add(eserBuild.getAllriskSuminsured() == null // ?
				 * cb.isNull(b.get("allriskSuminsured")) // :
				 * cb.equal(b.get("allriskSuminsured"), eserBuild.getAllriskSuminsured()));
				 * 
				 * //
				 * 
				 * 
				 * //
				 * 
				 * 
				 * 
				 * 
				 * // predicate.add(eserBuild.getContentSuminsured() == null // ?
				 * cb.isNull(b.get("contentSuminsured")) // :
				 * cb.equal(b.get("contentSuminsured"), eserBuild.getContentSuminsured()));
				 * 
				 * 
				 * predicate.add(cb.equal(b.get("categoryId"), eserBuild.getCategoryId()));
				 * predicate.add(cb.equal(b.get("endtTypeId"),eserBuild.getEndorsementType()==
				 * null?0 :eserBuild.getEndorsementType() ));
				 * predicate.add(cb.equal(b.get("endtCategoryId"),eserBuild.getIsFinyn()==null?
				 * "N" :eserBuild.getIsFinyn() ));
				 * 
				 * 
				 * // predicate.add(eserBuild.getCashValueablesSi() == null // ?
				 * cb.isNull(b.get("cashValueablesSi")) // : cb.equal(b.get("cashValueablesSi"),
				 * eserBuild.getCashValueablesSi()));
				 * 
				 * // predicate.add(eserBuild.getGoodsSinglecarrySuminsured() == null // ?
				 * cb.isNull(b.get("goodsSinglecarrySuminsured")) // :
				 * cb.equal(b.get("goodsSinglecarrySuminsured"),
				 * eserBuild.getGoodsSinglecarrySuminsured()));
				 * 
				 * 
				 * 
				 * 
				 * // // predicate.add(eserBuild.getGoodsTurnoverSuminsured() == null // ?
				 * cb.isNull(b.get("goodsTurnoverSuminsured")) // :
				 * cb.equal(b.get("goodsTurnoverSuminsured"),
				 * eserBuild.getGoodsTurnoverSuminsured())); //
				 * 
				 * 
				 * 
				 * 
				 * // predicate.add(eserBuild.getGoodsTurnoverSuminsured() == null // ?
				 * cb.isNull(b.get("goodsTurnoverSuminsured")) // :
				 * cb.equal(b.get("goodsTurnoverSuminsured"),
				 * eserBuild.getGoodsTurnoverSuminsured()));
				 * 
				 * 
				 * 
				 * 
				 * predicate.add(cb.equal(b.get("industryId"), eserBuild.getIndustryId()));
				 * predicate.add(cb.equal(b.get("endtTypeId"),eserBuild.getEndorsementType()==
				 * null?0 :eserBuild.getEndorsementType() ));
				 * predicate.add(cb.equal(b.get("endtCategoryId"),eserBuild.getIsFinyn()==null?
				 * "N" :eserBuild.getIsFinyn() ));
				 * 
				 * 
				 * predicate.add(eserBuild.getInternalWallType() == null ?
				 * cb.isNull(b.get("internalWallType")) : cb.equal(b.get("internalWallType"),
				 * eserBuild.getInternalWallType()));
				 * 
				 * // predicate.add(eserBuild.getStockInTradeSi() == null // ?
				 * cb.isNull(b.get("stockInTradeSi")) // : cb.equal(b.get("stockInTradeSi"),
				 * eserBuild.getStockInTradeSi()));
				 * 
				 * // predicate.add(eserBuild.getGoodsSi() == null // ?
				 * cb.isNull(b.get("goodsSi")) // : cb.equal(b.get("goodsSi"),
				 * eserBuild.getGoodsSi()));
				 * 
				 * // predicate.add(eserBuild.getApplianceSi() == null // ?
				 * cb.isNull(b.get("applianceSi")) // : cb.equal(b.get("applianceSi"),
				 * eserBuild.getApplianceSi()));
				 * 
				 * // predicate.add(eserBuild.getCashValueablesSi() == null // ?
				 * cb.isNull(b.get("cashValueablesSi")) // : cb.equal(b.get("cashValueablesSi"),
				 * eserBuild.getCashValueablesSi()));
				 * 
				 * // predicate.add(eserBuild.getFurnitureLossPercent() == null // ?
				 * cb.isNull(b.get("furnitureLossPercent")) // :
				 * cb.equal(b.get("furnitureLossPercent"),
				 * eserBuild.getFurnitureLossPercent()));
				 * 
				 * // predicate.add(eserBuild.getStockLossPercent() == null // ?
				 * cb.isNull(b.get("stockLossPercent")) // : cb.equal(b.get("stockLossPercent"),
				 * eserBuild.getStockLossPercent())); // //
				 * predicate.add(eserBuild.getGoodsLossPercent() == null // ?
				 * cb.isNull(b.get("goodsLossPercent")) // : cb.equal(b.get("goodsLossPercent"),
				 * eserBuild.getGoodsLossPercent())); // //
				 * predicate.add(eserBuild.getFurnitureLossPercent() == null // ?
				 * cb.isNull(b.get("furnitureLossPercent")) // :
				 * cb.equal(b.get("furnitureLossPercent"),
				 * eserBuild.getFurnitureLossPercent())); // //
				 * predicate.add(eserBuild.getApplianceLossPercent() == null // ?
				 * cb.isNull(b.get("applianceLossPercent")) // :
				 * cb.equal(b.get("applianceLossPercent"),
				 * eserBuild.getApplianceLossPercent())); // //
				 * predicate.add(eserBuild.getCashValueablesLossPercent() == null // ?
				 * cb.isNull(b.get("cashValueablesLossPercent")) // :
				 * cb.equal(b.get("cashValueablesLossPercent"),
				 * eserBuild.getCashValueablesLossPercent())); //
				 * predicate.add(eserBuild.getBuildingAge() == null ?
				 * cb.isNull(b.get("buildingAge")) : cb.equal(b.get("buildingAge"),
				 * eserBuild.getBuildingAge()));
				 * 
				 * predicate.add(eserBuild.getIndustryId() == null ?
				 * cb.isNull(b.get("industryId")) : cb.equal(b.get("industryId"),
				 * eserBuild.getIndustryId()));
				 * 
				 * // predicate.add(eserBuild.getMachineEquipSi() == null // ?
				 * cb.isNull(b.get("machineEquipSi")) // : cb.equal(b.get("machineEquipSi"),
				 * eserBuild.getMachineEquipSi()));
				 * 
				 * //
				 * 
				 * 
				 * //
				 * 
				 * 
				 * 
				 * 
				 * // predicate.add(eserBuild.getPlateGlassSi() == null // ?
				 * cb.isNull(b.get("plateGlassSi")) // : cb.equal(b.get("plateGlassSi"),
				 * eserBuild.getPlateGlassSi()));
				 * 
				 * predicate.add(eserBuild.getFirstLossPercent() == null ?
				 * cb.isNull(b.get("firstLossPercent")) : cb.equal(b.get("firstLossPercent"),
				 * eserBuild.getFirstLossPercent()));
				 * 
				 * // predicate.add(eserBuild.getPowerPlantSi() == null // ?
				 * cb.isNull(b.get("powerPlantSi")) // : cb.equal(b.get("powerPlantSi"),
				 * eserBuild.getPowerPlantSi()));
				 * 
				 * 
				 * 
				 * 
				 * // // predicate.add(eserBuild.getMachineEquipSi() == null // ?
				 * cb.isNull(b.get("machineEquipSi")) // : cb.equal(b.get("machineEquipSi"),
				 * eserBuild.getMachineEquipSi())); // //
				 * predicate.add(eserBuild.getElecMachinesSi() == null // ?
				 * cb.isNull(b.get("elecMachinesSi")) // : cb.equal(b.get("elecMachinesSi"),
				 * eserBuild.getElecMachinesSi())); //
				 * 
				 * 
				 * 
				 * 
				 * // predicate.add(eserBuild.getMachineEquipSi() == null // ?
				 * cb.isNull(b.get("machineEquipSi")) // : cb.equal(b.get("machineEquipSi"),
				 * eserBuild.getMachineEquipSi()));
				 * 
				 * // predicate.add(eserBuild.getElecMachinesSi() == null // ?
				 * cb.isNull(b.get("elecMachinesSi")) // : cb.equal(b.get("elecMachinesSi"),
				 * eserBuild.getElecMachinesSi()));
				 * 
				 * 
				 * 
				 * // predicate.add(eserBuild.getEquipmentSi() == null // ?
				 * cb.isNull(b.get("equipmentSi")) // : cb.equal(b.get("equipmentSi"),
				 * eserBuild.getEquipmentSi()));
				 * 
				 * 
				 * 
				 * 
				 * // predicate.add(eserBuild.getGeneralMachineSi() == null // ?
				 * cb.isNull(b.get("generalMachineSi")) // : cb.equal(b.get("generalMachineSi"),
				 * eserBuild.getGeneralMachineSi()));
				 * 
				 * 
				 * 
				 * 
				 * // // predicate.add(eserBuild.getManuUnitsSi() == null // ?
				 * cb.isNull(b.get("manuUnitsSi")) // : cb.equal(b.get("manuUnitsSi"),
				 * eserBuild.getManuUnitsSi())); // //
				 * predicate.add(eserBuild.getBoilerPlantsSi() == null // ?
				 * cb.isNull(b.get("boilerPlantsSi")) // : cb.equal(b.get("boilerPlantsSi"),
				 * eserBuild.getBoilerPlantsSi())); // //
				 * predicate.add(eserBuild.getMiningPlantSi() == null // ?
				 * cb.isNull(b.get("miningPlantSi")) // : cb.equal(b.get("miningPlantSi"),
				 * eserBuild.getMiningPlantSi())); // //
				 * predicate.add(eserBuild.getNonminingPlantSi() == null // ?
				 * cb.isNull(b.get("nonminingPlantSi")) // : cb.equal(b.get("nonminingPlantSi"),
				 * eserBuild.getNonminingPlantSi())); // //
				 * predicate.add(eserBuild.getGensetsSi() == null // ?
				 * cb.isNull(b.get("gensetsSi")) // : cb.equal(b.get("gensetsSi"),
				 * eserBuild.getGensetsSi())); //
				 * 
				 * 
				 * 
				 * 
				 * // predicate.add(eserBuild.getManuUnitsSi() == null // ?
				 * cb.isNull(b.get("manuUnitsSi")) // : cb.equal(b.get("manuUnitsSi"),
				 * eserBuild.getManuUnitsSi()));
				 * 
				 * // predicate.add(eserBuild.getBoilerPlantsSi() == null // ?
				 * cb.isNull(b.get("boilerPlantsSi")) // : cb.equal(b.get("boilerPlantsSi"),
				 * eserBuild.getBoilerPlantsSi()));
				 * 
				 * // predicate.add(eserBuild.getMiningPlantSi() == null // ?
				 * cb.isNull(b.get("miningPlantSi")) // : cb.equal(b.get("miningPlantSi"),
				 * eserBuild.getMiningPlantSi()));
				 * 
				 * // predicate.add(eserBuild.getNonminingPlantSi() == null // ?
				 * cb.isNull(b.get("nonminingPlantSi")) // : cb.equal(b.get("nonminingPlantSi"),
				 * eserBuild.getNonminingPlantSi()));
				 * 
				 * // predicate.add(eserBuild.getGensetsSi() == null // ?
				 * cb.isNull(b.get("gensetsSi")) // : cb.equal(b.get("gensetsSi"),
				 * eserBuild.getGensetsSi()));
				 * 
				 * 
				 * 
				 * // predicate.add(eserBuild.getFirePlantSi() == null // ?
				 * cb.isNull(b.get("firePlantSi")) // : cb.equal(b.get("firePlantSi"),
				 * eserBuild.getFirePlantSi()));
				 * 
				 * // predicate.add(eserBuild.getOnStockSi() == null // ?
				 * cb.isNull(b.get("onStockSi")) // : cb.equal(b.get("onStockSi"),
				 * eserBuild.getOnStockSi())); // // predicate.add(eserBuild.getOnStockSiLc() ==
				 * null // ? cb.isNull(b.get("onStockSiLc")) // : cb.equal(b.get("onStockSiLc"),
				 * eserBuild.getOnStockSiLc())); // // predicate.add(eserBuild.getOnAssetsSi()
				 * == null // ? cb.isNull(b.get("onAssetsSi")) // :
				 * cb.equal(b.get("onAssetsSi"), eserBuild.getOnAssetsSi())); // //
				 * predicate.add(eserBuild.getOnAssetsSiLc() == null // ?
				 * cb.isNull(b.get("onAssetsSiLc")) // : cb.equal(b.get("onAssetsSiLc"),
				 * eserBuild.getOnAssetsSiLc()));
				 * 
				 * // predicate.add(eserBuild.getBurglarySi() == null // ?
				 * cb.isNull(b.get("burglarySi")) // : cb.equal(b.get("burglarySi"),
				 * eserBuild.getBurglarySi()));
				 * 
				 * //
				 * 
				 * 
				 * //
				 * 
				 * 
				 * 
				 * 
				 * // predicate.add(eserBuild.getBurglarySiLc() == null // ?
				 * cb.isNull(b.get("burglarySiLc")) // : cb.equal(b.get("burglarySiLc"),
				 * eserBuild.getBurglarySiLc()));
				 * 
				 * // predicate.add(eserBuild.getStrongroomSi() == null // ?
				 * cb.isNull(b.get("strongroomSi")) // : cb.equal(b.get("strongroomSi"),
				 * eserBuild.getStrongroomSi())); // //
				 * predicate.add(eserBuild.getStrongroomSiLc() == null // ?
				 * cb.isNull(b.get("strongroomSiLc")) // : cb.equal(b.get("strongroomSiLc"),
				 * eserBuild.getStrongroomSiLc())); // //
				 * predicate.add(eserBuild.getMoneyAnnualEstimate() == null // ?
				 * cb.isNull(b.get("moneyAnnualEstimate")) // :
				 * cb.equal(b.get("moneyAnnualEstimate"), eserBuild.getMoneyAnnualEstimate()));
				 * // // predicate.add(eserBuild.getMoneyAnnualEstimateLc() == null // ?
				 * cb.isNull(b.get("moneyAnnualEstimateLc")) // :
				 * cb.equal(b.get("moneyAnnualEstimateLc"),
				 * eserBuild.getMoneyAnnualEstimateLc())); // //
				 * predicate.add(eserBuild.getMoneyCollector() == null // ?
				 * cb.isNull(b.get("moneyCollector")) // : cb.equal(b.get("moneyCollector"),
				 * eserBuild.getMoneyCollector())); // //
				 * predicate.add(eserBuild.getMoneyCollectorLc() == null // ?
				 * cb.isNull(b.get("moneyCollectorLc")) // : cb.equal(b.get("moneyCollectorLc"),
				 * eserBuild.getMoneyCollectorLc())); // //
				 * predicate.add(eserBuild.getMoneyDirectorResidence() == null // ?
				 * cb.isNull(b.get("moneyDirectorResidence")) // :
				 * cb.equal(b.get("moneyDirectorResidence"),
				 * eserBuild.getMoneyDirectorResidence())); // //
				 * predicate.add(eserBuild.getMoneyDirectorResidenceLc() == null // ?
				 * cb.isNull(b.get("moneyDirectorResidenceLc")) // :
				 * cb.equal(b.get("moneyDirectorResidenceLc"),
				 * eserBuild.getMoneyDirectorResidenceLc())); // //
				 * predicate.add(eserBuild.getMoneyOutofSafe() == null // ?
				 * cb.isNull(b.get("moneyOutofSafe")) // : cb.equal(b.get("moneyOutofSafe"),
				 * eserBuild.getMoneyOutofSafe())); // //
				 * predicate.add(eserBuild.getMoneyOutofSafeLc() == null // ?
				 * cb.isNull(b.get("moneyOutofSafeLc")) // : cb.equal(b.get("moneyOutofSafeLc"),
				 * eserBuild.getMoneyOutofSafeLc())); // //
				 * predicate.add(eserBuild.getMoneySafeLimit() == null // ?
				 * cb.isNull(b.get("moneySafeLimit")) // : cb.equal(b.get("moneySafeLimit"),
				 * eserBuild.getMoneySafeLimit())); // //
				 * predicate.add(eserBuild.getMoneySafeLimitLc() == null // ?
				 * cb.isNull(b.get("moneySafeLimitLc")) // : cb.equal(b.get("moneySafeLimitLc"),
				 * eserBuild.getMoneySafeLimitLc())); // //
				 * predicate.add(eserBuild.getMoneyMajorLoss() == null // ?
				 * cb.isNull(b.get("moneyMajorLoss")) // : cb.equal(b.get("moneyMajorLoss"),
				 * eserBuild.getMoneyMajorLoss())); // //
				 * predicate.add(eserBuild.getMoneyMajorLossLc() == null // ?
				 * cb.isNull(b.get("moneyMajorLossLc")) // : cb.equal(b.get("moneyMajorLossLc"),
				 * eserBuild.getMoneyMajorLossLc())); // //
				 * predicate.add(eserBuild.getMachinerySi() == null // ?
				 * cb.isNull(b.get("machinerySi")) // : cb.equal(b.get("machinerySi"),
				 * eserBuild.getMachinerySi()));
				 * 
				 * 
				 * 
				 * 
				 * // // predicate.add(eserBuild.getMachinerySiLc() == null // ?
				 * cb.isNull(b.get("machinerySiLc")) // : cb.equal(b.get("machinerySiLc"),
				 * eserBuild.getMachinerySiLc())); // //
				 * predicate.add(eserBuild.getGrossProfitFc() == null // ?
				 * cb.isNull(b.get("grossProfitFc")) // : cb.equal(b.get("grossProfitFc"),
				 * eserBuild.getGrossProfitFc())); // //
				 * predicate.add(eserBuild.getGrossProfitLc() == null // ?
				 * cb.isNull(b.get("grossProfitLc")) // : cb.equal(b.get("grossProfitLc"),
				 * eserBuild.getGrossProfitLc())); // //
				 * predicate.add(eserBuild.getIndemnityPeriodFc() == null // ?
				 * cb.isNull(b.get("indemnityPeriodFc")) // :
				 * cb.equal(b.get("indemnityPeriodFc"), eserBuild.getIndemnityPeriodFc())); //
				 * // predicate.add(eserBuild.getIndemnityPeriodLc() == null // ?
				 * cb.isNull(b.get("indemnityPeriodLc")) // :
				 * cb.equal(b.get("indemnityPeriodLc"), eserBuild.getIndemnityPeriodLc()));
				 * 
				 * 
				 * 
				 * 
				 * // predicate.add(eserBuild.getMachinerySiLc() == null // ?
				 * cb.isNull(b.get("machinerySiLc")) // : cb.equal(b.get("machinerySiLc"),
				 * eserBuild.getMachinerySiLc()));
				 * 
				 * // predicate.add(eserBuild.getGrossProfitFc() == null // ?
				 * cb.isNull(b.get("grossProfitFc")) // : cb.equal(b.get("grossProfitFc"),
				 * eserBuild.getGrossProfitFc())); // //
				 * predicate.add(eserBuild.getGrossProfitLc() == null // ?
				 * cb.isNull(b.get("grossProfitLc")) // : cb.equal(b.get("grossProfitLc"),
				 * eserBuild.getGrossProfitLc())); // //
				 * predicate.add(eserBuild.getIndemnityPeriodFc() == null // ?
				 * cb.isNull(b.get("indemnityPeriodFc")) // :
				 * cb.equal(b.get("indemnityPeriodFc"), eserBuild.getIndemnityPeriodFc())); //
				 * // predicate.add(eserBuild.getIndemnityPeriodLc() == null // ?
				 * cb.isNull(b.get("indemnityPeriodLc")) // :
				 * cb.equal(b.get("indemnityPeriodLc"), eserBuild.getIndemnityPeriodLc()));
				 * 
				 * 
				 * 
				 * predicate.add(eserBuild.getTransportedBy() == null ?
				 * cb.isNull(b.get("transportedBy")) : cb.equal(b.get("transportedBy"),
				 * eserBuild.getTransportedBy()));
				 * 
				 * predicate.add(eserBuild.getModeOfTransport() == null ?
				 * cb.isNull(b.get("modeOfTransport")) : cb.equal(b.get("modeOfTransport"),
				 * eserBuild.getModeOfTransport()));
				 * 
				 * predicate.add(eserBuild.getGeographicalCoverage() == null ?
				 * cb.isNull(b.get("geographicalCoverage")) :
				 * cb.equal(b.get("geographicalCoverage"),
				 * eserBuild.getGeographicalCoverage()));
				 * 
				 * // predicate.add(eserBuild.getSingleRoadSiLc() == null // ?
				 * cb.isNull(b.get("singleRoadSiLc")) // : cb.equal(b.get("singleRoadSiLc"),
				 * eserBuild.getSingleRoadSiLc())); // //
				 * predicate.add(eserBuild.getSingleRoadSiFc() == null // ?
				 * cb.isNull(b.get("singleRoadSiFc")) // : cb.equal(b.get("singleRoadSiFc"),
				 * eserBuild.getSingleRoadSiFc())); // //
				 * predicate.add(eserBuild.getEstAnnualCarriesSiFc() == null // ?
				 * cb.isNull(b.get("estAnnualCarriesSiFc")) // :
				 * cb.equal(b.get("estAnnualCarriesSiFc"),
				 * eserBuild.getEstAnnualCarriesSiFc())); // //
				 * predicate.add(eserBuild.getEstAnnualCarriesSiLc() == null // ?
				 * cb.isNull(b.get("estAnnualCarriesSiLc")) // :
				 * cb.equal(b.get("estAnnualCarriesSiLc"),
				 * eserBuild.getEstAnnualCarriesSiLc())); // //
				 * predicate.add(eserBuild.getGroundUndergroundSi() == null // ?
				 * cb.isNull(b.get("groundUndergroundSi")) // :
				 * cb.equal(b.get("groundUndergroundSi"), eserBuild.getGroundUndergroundSi()));
				 * 
				 * predicate.add(eserBuild.getWallType() == null ? cb.isNull(b.get("wallType"))
				 * : cb.equal(b.get("wallType"), eserBuild.getWallType()));
				 * 
				 * predicate.add(eserBuild.getRoofType() == null ? cb.isNull(b.get("roofType"))
				 * : cb.equal(b.get("roofType"), eserBuild.getRoofType()));
				 * 
				 * // predicate.add(eserBuild.getBondSuminsured() == null // ?
				 * cb.isNull(b.get("bondSuminsured")) // : cb.equal(b.get("bondSuminsured"),
				 * eserBuild.getBondSuminsured()));
				 * 
				 * predicate.add(eserBuild.getBondType() == null ? cb.isNull(b.get("bondType"))
				 * : cb.equal(b.get("bondType"), eserBuild.getBondType()));
				 * 
				 * predicate.add(eserBuild.getBondYear() == null ? cb.isNull(b.get("bondYear"))
				 * : cb.equal(b.get("bondYear"), eserBuild.getBondYear()));
				 * 
				 * predicate.add(eserBuild.getInternalWallType() == null ?
				 * cb.isNull(b.get("internalWallType")) : cb.equal(b.get("internalWallType"),
				 * eserBuild.getInternalWallType())); //
				 * predicate.add(cb.equal(b.get("sumInsured"), eserBuild.getSumInsured()));
				 * predicate.add(eserBuild.getSumInsured() == null ?
				 * cb.isNull(b.get("sumInsured")) : cb.equal(b.get("sumInsured"),
				 * eserBuild.getSumInsured())); predicate.add(eserBuild.getSumInsuredLc() ==
				 * null ? cb.isNull(b.get("sumInsuredLc")) : cb.equal(b.get("sumInsuredLc"),
				 * eserBuild.getSumInsuredLc())); predicate.add(eserBuild.getIndemityPeriod() ==
				 * null ? cb.isNull(b.get("indemityPeriod")) : cb.equal(b.get("indemityPeriod"),
				 * eserBuild.getIndemityPeriod())); predicate.add(eserBuild.getContentId() ==
				 * null ? cb.isNull(b.get("contentId")) : cb.equal(b.get("contentId"),
				 * eserBuild.getContentId())); predicate.add(eserBuild.getNoOfClaim() == null ?
				 * cb.isNull(b.get("noOfClaim")) : cb.equal(b.get("noOfClaim"),
				 * eserBuild.getNoOfClaim())); predicate.add(eserBuild.getCoverId() == null ?
				 * cb.isNull(b.get("coverId")) : cb.equal(b.get("coverId"),
				 * eserBuild.getCoverId())); query.where(predicate.toArray(new Predicate[0]));
				 * 
				 * // Get Result TypedQuery<MsAssetDetails> result = em.createQuery(query); list
				 * = result.getResultList();
				 */				
//				if (list==null || list.size() <= 0 ) {
//					newEntry = true ;
//
//				} else {
//					vdRefNo = String.valueOf(list.get(0).getVdRefno());
//					//riskId  = String.valueOf(list.get(0).getLocationId());
//					riskId  = String.valueOf(list.get(0).getRiskId());
//					locationId  = String.valueOf(list.get(0).getLocationId());
//
//				}
			
				sectionId = sec.getSectionId() ;
				if(newEntry==true  ) {
				//	Random rand = new Random();
		         //    int random=rand.nextInt(90)+10;
					vdRefNo = genOneTimeTableRefNo() ; // sdf.format(new Date()) + random ;
					riskId 	   = eserBuild.getRiskId().toString();
					locationId=eserBuild.getLocationId().toString();
					
					MsAssetDetails saveAsset = new MsAssetDetails();
					//dozerMapper.map(eserBuild, saveAsset);
					
					// Basic Inputs
					dozerMapper.map(eserBuild, saveAsset);
					saveAsset.setVdRefno(Long.valueOf(vdRefNo));	
					//Location Id
					saveAsset.setLocationId(eserBuild.getLocationId()==null?1:eserBuild.getLocationId());
					//Risk Id
					saveAsset.setRiskId(eserBuild.getRiskId());
					saveAsset.setEntryDate(new Date());
					saveAsset.setStatus(eserBuild.getStatus());
					saveAsset.setGroupCount(1);
//					saveAsset.setBondSuminsured(eserBuild.getBondSuminsured());
					saveAsset.setPeriodOfInsurance(String.valueOf(eserBuild.getPolicyPeriord()));
					saveAsset.setExchangeRate(eserBuild.getExchangeRate());
					saveAsset.setCurrency(eserBuild.getCurrency());
					saveAsset.setRequestReferenceNo(eserBuild.getRequestReferenceNo());
					saveAsset.setSectionId(Integer.valueOf(sec.getSectionId()));
					saveAsset.setEndtTypeId(eserBuild.getEndorsementType()==null?0 :eserBuild.getEndorsementType() ) ;
					saveAsset.setEndtCategoryId(eserBuild.getIsFinyn()==null?"N" :eserBuild.getIsFinyn() );
					saveAsset.setIndustryId(eserBuild.getIndustryId() );
			//		saveAsset.setUwLoading(BigDecimal.ZERO);
					saveAsset.setBuildingAge(eserBuild.getBuildingAge() == null ? null : eserBuild.getBuildingAge());
					saveAsset.setCompanyId(eserBuild.getCompanyId());
					saveAsset.setBranchCode(eserBuild.getBranchCode());
					saveAsset.setProductId(eserBuild.getProductId()==null?null : Integer.valueOf(eserBuild.getProductId()));
					saveAsset.setCreatedBy(eserBuild.getCreatedBy());
					saveAsset.setPromocode(eserBuild.getPromocode());
					saveAsset.setHavepromocode(eserBuild.getHavepromocode());
					saveAsset.setInsuranceClass("99999");
					saveAsset.setWallType(eserBuild.getWallType());
					saveAsset.setRoofType(eserBuild.getRoofType());
					saveAsset.setFirstLossPercent(eserBuild.getFirstLossPercent()==null?null:Long.valueOf(eserBuild.getFirstLossPercent()));
					saveAsset.setInternalWallType(eserBuild.getInternalWallType()==null?null:eserBuild.getInternalWallType());
					saveAsset.setSumInsured(eserBuild.getSumInsured()==null?null:eserBuild.getSumInsured());
					saveAsset.setSumInsuredLc(eserBuild.getSumInsuredLc()==null?null:eserBuild.getSumInsuredLc());
					saveAsset.setIndemityPeriod(StringUtils.isBlank(eserBuild.getIndemityPeriod())?"0":eserBuild.getIndemityPeriod());
					saveAsset.setCoverId(eserBuild.getCoverId()!=null?eserBuild.getCoverId():0);
					saveAsset.setNoOfClaim(eserBuild.getNoOfClaim()!=null?eserBuild.getNoOfClaim():0);
					saveAsset.setParam1(StringUtils.isBlank(eserBuild.getParam1())?"":eserBuild.getParam1());
					saveAsset.setParam2(StringUtils.isBlank(eserBuild.getParam2())?"":eserBuild.getParam2());
					saveAsset.setParam3(StringUtils.isBlank(eserBuild.getParam3())?"":eserBuild.getParam3());
					saveAsset.setParam4(StringUtils.isBlank(eserBuild.getParam4())?"":eserBuild.getParam4());
					saveAsset.setParam5(StringUtils.isBlank(eserBuild.getParam5())?"":eserBuild.getParam5());
					saveAsset.setParam6(StringUtils.isBlank(eserBuild.getParam6())?"":eserBuild.getParam6());
					saveAsset.setParam7(StringUtils.isBlank(eserBuild.getParam7())?"":eserBuild.getParam7());
					saveAsset.setParam8(StringUtils.isBlank(eserBuild.getParam8())?"":eserBuild.getParam8());
					saveAsset.setParam9(StringUtils.isBlank(eserBuild.getParam9())?"":eserBuild.getParam9());
					saveAsset.setParam10(StringUtils.isBlank(eserBuild.getParam10())?"":eserBuild.getParam10());
					saveAsset.setParam11(StringUtils.isBlank(eserBuild.getParam11())?"":eserBuild.getParam11());
					saveAsset.setParam12(StringUtils.isBlank(eserBuild.getParam12())?"":eserBuild.getParam12());
					saveAsset.setParam13(StringUtils.isBlank(eserBuild.getParam13())?"":eserBuild.getParam13());
					saveAsset.setParam14(StringUtils.isBlank(eserBuild.getParam14())?"":eserBuild.getParam14());
					saveAsset.setParam15(StringUtils.isBlank(eserBuild.getParam14())?"":eserBuild.getParam15());
				//	msAssetRepo.saveAndFlush(saveAsset);
					request.setMsAssertlist(saveAsset);
					sectionId = sec.getSectionId() ;
					
				}
			}
			
			res.setVdRefNo(vdRefNo);
			res.setSectionId(sectionId);
			res.setAgencyCode(agencyCode);
			res.setBranchCode(branchCode);
			res.setProductId(productId);
			res.setCompanyId(companyId);
			res.setVehicleId(riskId);
			res.setLocationId(locationId);
			res.setCoverid(Coverid);
			res.setParam1(param1);
			res.setParam2(param2);
			res.setParam3(param3);
			res.setParam4(param4);
			res.setParam5(param5);
			res.setParam6(param6);
			res.setParam7(param7);
			res.setParam8(param8);
			res.setParam9(param9);
			res.setParam10(param10);
			

			
//			if ( null != sec  && StringUtils.isNotBlank(sec.getMotorYn()) &  sec.getMotorYn().equalsIgnoreCase("H")) {  // No need
//				res.setVehicleId(originalRiskId);
//			}

			resList.add(res);
		}
	
		
		}catch (Exception e) {
			e.printStackTrace();
			log.error("Exception is ---> " + e.getMessage());
			return null ;
		}

		return resList;
	}
	


	
	
	
	
	
	@Transactional
	public synchronized String call_MSCustomer(OneTimeTableReq request) {
		String cdRefNo = "" ;
		///DozerBeanMapper dozerMapper = new DozerBeanMapper(); 
		// SimpleDateFormat sdf = new SimpleDateFormat("yyMMddhhmmssSS");
		try {
			if(!request.isY())
			{
				List<MsCustomerDetails> list = new ArrayList<MsCustomerDetails>();
				
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<MsCustomerDetails> query = cb.createQuery(MsCustomerDetails.class);
				// Find All
				Root<MsCustomerDetails> b = query.from(MsCustomerDetails.class);
				// Select
				query.select(b);

				Predicate n1 = cb.equal(cb.lower(b.get("policyHolderTypeid")),custData.getPolicyHolderTypeid().toLowerCase());
				Predicate n2 = cb.equal(cb.lower(b.get("policyHolderType")),custData.getPolicyHolderType().toLowerCase());
				
				Predicate n3 = custData.getAge() != null ? cb.equal(b.get("age")  , custData.getAge() ) : cb.isNull(b.get("age"));
				
				Predicate n4 = cb.equal(cb.lower(b.get("gender")),custData.getGender().toLowerCase());
				              
				Predicate n5 =  custData.getOccupation() != null ? cb.equal(cb.lower(b.get("occupation")) , custData.getOccupation()) : cb.isNull(b.get("occupation") ); 
				Predicate n7 = cb.equal(cb.lower(b.get("regionCode")),custData.getRegionCode()==null?"NA":custData.getRegionCode().toLowerCase());
			//	Predicate n8 = cb.equal(b.get("cityCode"),custData.getCityCode());
				//Predicate n9 = StringUtils.isNotBlank(custData.getTaxExemptedId())?cb.equal(b.get("taxExemptedId"),custData.getTaxExemptedId()):cb.isNull(b.get("taxExemptedId"));
				Predicate n10 = cb.equal(cb.lower(b.get("status")),custData.getStatus().toLowerCase());
				Predicate n11 = cb.equal(cb.lower(b.get("idNumber")),custData.getIdNumber().toLowerCase());
	            Predicate n12 = cb.equal(b.get("taxExemptedId"),custData.getTaxExemptedId());
				query.where(n1,n2,n3,n4,n5,n7,n12,n10,n11);					

				TypedQuery<MsCustomerDetails> result = em.createQuery(query);
				list =  result.getResultList(); 
				if (list!=null && list.size() > 0  ) {
					cdRefNo =String.format("%05d", list.get(0).getCdRefno()) ;
				} else {
					//	Random rand = new Random();
			         // int random=rand.nextInt(90)+10;custData.getRegionCode()==null
						if(custData.getRegionCode()==null)
							custData.setRegionCode("NA");
						cdRefNo =  genOneTimeTableRefNo() ;  //sdf.format(new Date()) + random ;				
						MsCustomerDetails saveNewEntry  = new  MsCustomerDetails(); 
						dozerMapper.map(custData, saveNewEntry);
						saveNewEntry.setCdRefno(Long.valueOf(cdRefNo) );
						saveNewEntry.setEntryDate(new Date());
						
						msCustomerRepo.save(saveNewEntry);
					}
			}
			else {

//				Optional<MsCustomerDetails> customerOpt = msCustomerRepo.findMatchingCustomerFast(
//					    custData.getPolicyHolderTypeid(),
//					    custData.getPolicyHolderType(),
//					    custData.getAge(),
//					    custData.getGender(),
//					    custData.getOccupation(),
//					    custData.getStatus(),
//					    custData.getIdNumber(),
//					    custData.getTaxExemptedId(),
//					    custData.getRegionCode() == null ? "NA" : custData.getRegionCode()
//					);

				if (request.getOTCustome().isPresent()) {
					cdRefNo = String.format("%05d", request.getOTCustome().get().getCdRefno());
				} else {
					
					if (custData.getRegionCode() == null)
						custData.setRegionCode("NA");
					cdRefNo = genOneTimeTableRefNo(); // sdf.format(new Date()) + random ;
					MsCustomerDetails saveNewEntry = new MsCustomerDetails();
					dozerMapper.map(custData, saveNewEntry);
					saveNewEntry.setCdRefno(Long.valueOf(cdRefNo));
					saveNewEntry.setEntryDate(new Date());
					request.setMsCustomer(saveNewEntry);
					//msCustomerRepo.save(saveNewEntry);
				}
			}

		}catch (Exception e) {
			e.printStackTrace();
			log.error("Exception is ---> " + e.getMessage());
			return null ;
		}

		return cdRefNo;
	}
	
	

//	
//	 public synchronized String genOneTimeTableRefNo() {
//	       try {
//	    	   SeqOnetimetable entity;
//	            entity = oneNoRepo.save(new SeqOnetimetable());          
//	            return String.format("%05d",entity.getReferenceNo()) ;
//	        } catch (Exception e) {
//				e.printStackTrace();
//				log.info( "Exception is ---> " + e.getMessage());
//	            return null;
//	        }
//	       
//	 }
//	 
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public  String genOneTimeTableRefNo() {
	       try {
	    	   SeqOnetimetable entity;
	            entity = oneNoRepo.save(new SeqOnetimetable());          
	            return String.format("%05d",entity.getReferenceNo()) ;
	        } catch (Exception e) {
				e.printStackTrace();
				log.info( "Exception is ---> " + e.getMessage());
	            return null;
	        }
	       
	 }

	 
	 public SectionCoverMaster getCoverageLimit(OneTimeTableReq req) {
		 SectionCoverMaster res = new SectionCoverMaster();
			DozerBeanMapper dozerMapper = new  DozerBeanMapper();
		
			try {
				Date today  = new Date();
				Calendar cal = new GregorianCalendar(); 
				cal.setTime(today);
				cal.set(Calendar.HOUR_OF_DAY, 23);
				cal.set(Calendar.MINUTE, 1);
				today   = cal.getTime();
				
				// Criteria
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<SectionCoverMaster> query = cb.createQuery(SectionCoverMaster.class);
				List<SectionCoverMaster> list = new ArrayList<SectionCoverMaster>();
				
				// Find All
				Root<SectionCoverMaster>    c = query.from(SectionCoverMaster.class);		
				
				// Select
				query.select(c);
				
				// Amend ID Max Filter
				Subquery<Long> amendId = query.subquery(Long.class);
				Root<SectionCoverMaster> ocpm1 = amendId.from(SectionCoverMaster.class);
				amendId.select(cb.max(ocpm1.get("amendId")));
				jakarta.persistence.criteria.Predicate a1 = cb.equal(c.get("sectionId"),ocpm1.get("sectionId") );
				jakarta.persistence.criteria.Predicate a2 = cb.equal(c.get("productId"),ocpm1.get("productId") ) ;
				jakarta.persistence.criteria.Predicate a3 = cb.equal(c.get("companyId"),ocpm1.get("companyId") ) ;
				Predicate a4 = cb.equal(c.get("coverId"),ocpm1.get("coverId") ) ;
				Predicate a5 = cb.equal(c.get("subCoverId"),ocpm1.get("subCoverId") ) ;
				//jakarta.persistence.criteria.Predicate a4 = cb.lessThanOrEqualTo(c.get("effectiveDateStart"),today ) ;
				//Predicate a5 = cb.equal(ocpm1.get("branchCode"),c.get("branchCode"));
				amendId.where(a1,a2,a3,a4,a5);
				
				
				
				// Order By
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.desc(c.get("effectiveDateStart")));
				
			    // Where	
			
				jakarta.persistence.criteria.Predicate n1 = cb.equal(c.get("amendId"), amendId);		
				jakarta.persistence.criteria.Predicate n2 = cb.equal(c.get("sectionId"),req.getSectionId()) ;
				jakarta.persistence.criteria.Predicate n3 = cb.equal(c.get("productId"),req.getProductId()) ;
				jakarta.persistence.criteria.Predicate n4 = cb.equal(c.get("companyId"),req.getInsuranceId()) ;
				jakarta.persistence.criteria.Predicate n5 = cb.equal(c.get("coverId"),"5") ;
				Predicate n6 = cb.equal(c.get("subCoverId"),"0") ;
				query.where(n1 ,n2,n3,n4,n5,n6).orderBy(orderList);
				
				// Get Result
				TypedQuery<SectionCoverMaster> result = em.createQuery(query);			
				list =  result.getResultList();  
				list = list.stream().filter(distinctByKey(o -> Arrays.asList(o.getCoverId()))).collect(Collectors.toList());
				res =  list.size()>0 ? list.get(0) : null ;
//				list.sort(Comparator.comparing(SectionCoverMaster :: getSectionName ));
//				
//				res = dozerMapper.map(list.get(0) , ProductSectionMasterRes.class);
//				res.setSectionId(list.get(0).getSectionId().toString());
//				res.setEntryDate(list.get(0).getEntryDate());
//				res.setEffectiveDateStart(list.get(0).getEffectiveDateStart());
			
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Exception is ---> " + e.getMessage());
				return null;
			}
			return res;
		}

}
