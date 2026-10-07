package com.maan.eway.tira.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.maan.eway.bean.EndtTypeMaster;
import com.maan.eway.bean.HomePositionMaster;
import com.maan.eway.bean.MotorDataDetails;
import com.maan.eway.bean.MotorVehicleInfo;
import com.maan.eway.bean.PersonalInfo;
import com.maan.eway.bean.PolicyCoverData;
import com.maan.eway.bean.ProductSectionMaster;
import com.maan.eway.bean.SectionDataDetails;
import com.maan.eway.repository.HomePositionMasterRepository;
import com.maan.eway.req.push.CoverNoteAddon;
import com.maan.eway.req.push.CoverNoteAddons;
import com.maan.eway.req.push.CoverNoteDtl;
import com.maan.eway.req.push.CoverNoteHdr;
import com.maan.eway.req.push.CoverNoteRefReq;
import com.maan.eway.req.push.DiscountOffered;
import com.maan.eway.req.push.MotorCoverNoteRefReq;
import com.maan.eway.req.push.MotorDtl;
import com.maan.eway.req.push.NonCoverNoteDtl;
import com.maan.eway.req.push.PolicyHolder;
import com.maan.eway.req.push.PolicyHolders;
import com.maan.eway.req.push.RiskCovered;
import com.maan.eway.req.push.RisksCovered;
import com.maan.eway.req.push.SubjectMatter;
import com.maan.eway.req.push.SubjectMattersCovered;
import com.maan.eway.req.push.TaxCharged;
import com.maan.eway.req.push.TiraMsgCoverPush;
import com.maan.eway.req.push.TiraMsgVehiclePush;
import com.maan.eway.tira.bean.MaansarovarToTira;
import com.maan.eway.tira.util.AddonFromData;
import com.maan.eway.tira.util.CoverFromData;
import com.maan.eway.tira.util.CoverFromDataForNM;
import com.maan.eway.tira.util.CoverFromDataForNMEndt;
import com.maan.eway.tira.util.DiscountFromData;
import com.maan.eway.tira.util.LoadingFromData;
import com.maan.eway.tira.util.TaxFromData;

@Service
public class ConvertTiraRequest {


	@Value("${vehiclePostingReturnLink}")
	private String motorPostingReturnLink ;
	
	@Value("${FleetMotorPostingV1Link}")
	private String fleetMotorPostingV1Link;
	
	@Value("${vehiclePostingReturnLink}")
	private String vehiclePostingReturnLink;
	
	@Value("${NonMotorPostingReturnLink}")
	private String nonMotorWebhook;
	
	@Value("${HeadearAdd1Value}")
	private String headearAdd1Value ;
	@Value("${tiraSystemCode}")
	private String tiraSystemCode;
	
	@Value("${tiraCompanyCode}")
	private String tiraCompanyCode;
	@Autowired
	private HomePositionMasterRepository home;
	
	public TiraMsgVehiclePush convert(MaansarovarToTira m) throws Exception {
		System.out.println("tira Convert Req:" + m.getPolicy());
		System.out.println("tira Convert Req:" + m.getBroker());
		System.out.println("tira Convert Req:" + m.getCompany());
		System.out.println("tira Convert Req:" + m.getCovers());
		System.out.println("tira Convert Req:" + m.getCustomerInfo());
		System.out.println("tira Convert Req:" + m.getEndt());
		System.out.println("tira Convert Req:" + m.getLogin());
		System.out.println("tira Convert Req:" + m.getLtPayment());
		System.out.println("tira Convert Req:" + m.getMdd());
		System.out.println("tira Convert Req:" + m.getProduct());
		System.out.println("tira Convert Req:" + m.getSection());
		System.out.println("tira Convert Req:" + m.getSectionData());
		System.out.println("tira Convert Req:" + m.getUw());
		System.out.println("tira Convert Req:" + m.getVehicleInfo());
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat secondsformat=new SimpleDateFormat("'T'HH:mm:ss");
		secondsformat.setTimeZone(TimeZone.getTimeZone("EAT"));
		format.setTimeZone(TimeZone.getTimeZone("EAT"));
		
		String seconds = secondsformat.format( new Date());
	
		CoverNoteDtl cn=new CoverNoteDtl();
		String coverNoteType=StringUtils.isNotBlank(m.getPolicy().getEndtTypeId())?"3":(StringUtils.isNotBlank(m.getPolicy().getRenewalStatus()) ?"2":"1");
		if(m.getMdd()!=null) {
			String joinWith = StringUtils.joinWith("-",m.getMdd().getLocationId().toString(), m.getMdd().getSectionId(), m.getMdd().getVehicleId().toString());	
			cn.setCoverNoteNumber(m.getPolicy().getQuoteNo()+"-"+joinWith+"_"+twoDigitRandom());
		}else
			cn.setCoverNoteNumber(m.getPolicy().getQuoteNo());
		cn.setPrevCoverNoteReferenceNumber(StringUtils.isNotBlank(m.getPolicy().getEndtTypeId())?m.getPolicy().getPrevCovernoteRefno():null);
		cn.setSalePointCode(StringUtils.isBlank(m.getPolicy().getSalePointCode())?"SP500":m.getPolicy().getSalePointCode()); //SC001 //"SC014"
		if("3".equals(coverNoteType) && "842".equals(m.getPolicy().getEndtTypeId())) {
			cn.setCoverNoteStartDate(format.format(m.getPolicy().getInceptionDate())+seconds);
			cn.setCoverNoteEndDate(format.format(m.getPolicy().getExpiryDate())+"T23:59:59");
//			cn.setCoverNoteStartDate(format.format(m.getPolicy().getEndorsementEffdate())+seconds);
//			cn.setCoverNoteEndDate(format.format(m.getPolicy().getCancelledDate())+"T23:59:59");
			cn.setPrevCoverNoteReferenceNumber(!m.getSectionData().isEmpty()?m.getSectionData().get(0).getPrevCovernoteRefno():null);
		}else {
			seconds=getZeroTimeDate(m.getPolicy().getInceptionDate()).compareTo(new Date())>=0?"T00:00:00":seconds;
			cn.setCoverNoteStartDate(format.format(m.getPolicy().getInceptionDate())+seconds);
			cn.setCoverNoteEndDate(format.format(m.getPolicy().getExpiryDate())+"T23:59:59");
		}
		cn.setCoverNoteDesc("Cover Note for "+m.getPolicy().getQuoteNo());
		cn.setOperativeClause(m.getProduct().getProductDesc());
		cn.setPaymentMode(m.getLtPayment().getItemCode());  
		cn.setCurrencyCode(m.getPolicy().getCurrency());
		cn.setExchangeRate(m.getPolicy().getExchangeRate().toString());
		
		double commissionRate = m.getPolicy().getCommissionPercentage() ==null ?0D:m.getPolicy().getCommissionPercentage().divide(new BigDecimal(100)).doubleValue(); 
		 MotorDataDetails mdd = m.getMdd();
		if(mdd!=null) {
			cn.setTotalPremiumExcludingTax(BigDecimal.valueOf(mdd.getActualPremiumFc()).toPlainString());
			cn.setTotalPremiumIncludingTax(BigDecimal.valueOf(mdd.getOverallPremiumFc()).toPlainString());
				
				String pattern =  "#####0.00";
				DecimalFormat decimalFormat = new DecimalFormat(pattern);
				try {
					String commissionPaid = decimalFormat.format((Double) mdd.getActualPremiumFc() * commissionRate);
					cn.setCommisionPaid(commissionPaid);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
		}else {
			cn.setTotalPremiumExcludingTax(m.getPolicy().getPremiumFc().toPlainString());
			cn.setTotalPremiumIncludingTax(m.getPolicy().getOverallPremiumFc().toPlainString());
			cn.setCommisionPaid(m.getPolicy().getCommission()==null?"0":m.getPolicy().getCommission().abs().toEngineeringString());
		}
		
		
		
		cn.setCommisionRate(String.valueOf(commissionRate));
		//cn.setCommisionPaid("0");
	
		
		//cn.setOfficerName((m.getPolicy().getApplicationId().equals("1") || m.getPolicy().getApplicationId().equals("01")) ?m.getBroker().getUserName():m.getUw().getCompanyName());
		cn.setOfficerName(m.getPolicy().getCustomerName());
		
		cn.setOfficerTitle(StringUtils.isNotBlank(m.getPolicy().getSourceType())?m.getPolicy().getSourceType().replaceAll("Premia ", "").toUpperCase(): "UnderWriter");
		cn.setProductCode(m.getSection().getRegulatoryCode());//m.getProduct().getRegulatoryCode()
		
		
		if("3".equals(coverNoteType)) {
			EndtTypeMaster e = m.getEndt();
			String endorsementtype="";
			if("842".equals(m.getPolicy().getEndtTypeId())) {
				endorsementtype="4";
				
			}else if(e.getIsCoverendt().equalsIgnoreCase("Y") ) {
				endorsementtype="3";
			}else if(m.getPolicy().getIsChargRefund().equalsIgnoreCase("CHARGE") ) {
				endorsementtype="1";
			}else if(m.getPolicy().getIsChargRefund().equalsIgnoreCase("REFUND") ) {
				endorsementtype="2";
			}
			cn.setEndorsementType(endorsementtype);
			cn.setEndorsementReason(m.getPolicy().getEndtTypeDesc());
			cn.setEndorsementPremiumEarned(m.getPolicy().getEndtPremium().abs().add(m.getPolicy().getEndtPremiumTax().abs()) .toPlainString());
			if("842".equals(m.getPolicy().getEndtTypeId())) {
				HomePositionMaster byQuoteNo = home.findByQuoteNo(m.getPolicy().getEndtPrevQuoteNo());
				cn.setCoverNoteStartDate(format.format(m.getPolicy().getEffectiveDate())+seconds);
				cn.setCoverNoteEndDate(format.format(byQuoteNo.getExpiryDate())+"T23:59:59");
				cn.setEndorsementPremiumEarned(m.getPolicy().getEndtEarendPre().abs().add(m.getPolicy().getEndtEarendTax().abs()) .toPlainString());
			}
		}else {
			cn.setEndorsementType(null);
			cn.setEndorsementReason(null);
			cn.setEndorsementPremiumEarned(null);
		}
		 
		riskData(m, cn);
		
		CoverNoteHdr hdr=CoverNoteHdr.builder()
				.callBackUrl(vehiclePostingReturnLink)
				.companyCode(tiraCompanyCode) // "ICC105"--1CC125
				.coverNoteType(coverNoteType)
				.insurerCompanyCode(headearAdd1Value)  // "ICC105" --ICC110
				.requestId("EWAY"+Calendar.getInstance().getTimeInMillis())
				.systemCode(tiraSystemCode) //"LSYS_EWAYINSURANCE_001"
				.tranCompanyCode(StringUtils.isBlank(m.getPolicy().getBrokerTiraCode())?headearAdd1Value:m.getPolicy().getBrokerTiraCode())  // "ICC105" --ICC110--HeadearAdd1Value


				.build();
		
		MotorCoverNoteRefReq mt=new MotorCoverNoteRefReq();		
		mt.setCoverNoteDtlBean(cn);
		mt.setCoverNoteHdrBean(hdr);
		TiraMsgVehiclePush p=new TiraMsgVehiclePush();
		p.setMotorCoverNoteRefReq(mt);
		System.out.println("tira Convert Res:" + p.getMotorCoverNoteRefReq());
		 return p;
		
	}
	// predicate to filter the duplicates by the given key extractor.
		public  <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
			Map<Object, Boolean> uniqueMap = new ConcurrentHashMap<>();
			return t -> uniqueMap.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
		}
	public void riskData(MaansarovarToTira m, CoverNoteDtl coverDtl) {

		 try {
			 
			 //MotorVehicleInfo vehicleInfo = m.getVehicleInfo();
			 List<PolicyCoverData> distinctSections = m.getCovers().stream().filter(distinctByKey(ss -> ss.getSectionId())).collect(Collectors.toList());
			   
			 List<RiskCovered> risks=new ArrayList<RiskCovered>();
			 for(PolicyCoverData policy :distinctSections) {
				 List<PolicyCoverData> distinctsVehicle = m.getCovers().stream()
						 .filter(pol -> pol.getSectionId().compareTo(policy.getSectionId())==0)
						 .filter(distinctByKey(cust -> cust.getVehicleId()))
						 .collect(Collectors.toList()); 

				 for(PolicyCoverData v  :distinctsVehicle) {
					 List<PolicyCoverData> distinctsCovers = m.getCovers().stream()
							 .filter(pol -> pol.getSectionId().compareTo(policy.getSectionId())==0)
							 .filter(p-> p.getVehicleId().equals(v.getVehicleId()) &&  p.getCoverageType().equals("B") )
							 .filter(distinctByKey(cust -> cust.getCoverId() ))
							 .collect(Collectors.toList());
					 List<PolicyCoverData> totalcovers = m.getCovers().stream()
							 .filter(pol -> pol.getSectionId().compareTo(policy.getSectionId())==0)
							 .filter(p-> p.getVehicleId().equals(v.getVehicleId()))									
							 .collect(Collectors.toList());

					 for (PolicyCoverData distintc : distinctsCovers) {
						 //	List<Cover> totalcovers=new ArrayList<Cover>();
						 List<PolicyCoverData> covers =totalcovers.stream()
								 .filter(p-> p.getCoverId().equals(distintc.getCoverId()) )									
								 .collect(Collectors.toList());

						 DiscountFromData discountUtil=new DiscountFromData();
						 List<DiscountOffered> discounts = covers.stream().map(discountUtil).filter(d->d!=null).collect(Collectors.toList());
						 LoadingFromData loadingtuils=new LoadingFromData();
						 List<DiscountOffered> loadings = covers.stream().map(loadingtuils).filter(d->d!=null).collect(Collectors.toList());
						 TaxFromData taxesutils=new TaxFromData();							
						 List<TaxCharged> taxes = covers.stream().filter(p -> p.getDiscLoadId()==0 ).map(taxesutils).filter(d->d!=null).collect(Collectors.toList());

						 CoverFromData splitsub=new CoverFromData("B",discounts,loadings,taxes);
						 List<RiskCovered> risk= covers.stream().map(splitsub).filter(d->d!=null).collect(Collectors.toList());   

						risks.addAll(risk);

					 }				 

				 }
			 } 
			 coverDtl.setRisksCoveredBean(RisksCovered.builder().riskCoveredBeanList(risks).build());
			  
			//Section
			 List<SubjectMatter> subjectMatter=new ArrayList<SubjectMatter>();
			 ProductSectionMaster section = m.getSection();
			 SubjectMatter subject=new SubjectMatter();
			 subject.setSubjectMatterReference(section.getCoreAppCode());
			 subject.setSubjectMatterDesc(section.getSectionName());
			 subjectMatter.add(subject);

			 SubjectMattersCovered subjectMatters=new SubjectMattersCovered();						
			 subjectMatters.setSubjectMatterBeanList(subjectMatter);
			 coverDtl.setSubjectMattersCoveredBean(subjectMatters);

			 List<PolicyCoverData> distinctsCovers = m.getCovers().stream()
					 
					 .filter(p-> /*p.getVehicleId().equals(v.getVehicleId())&&*/ p.getCoverageType().equalsIgnoreCase("O"))
					 .filter(distinctByKey(cust -> cust.getCoverId() ))
					 .collect(Collectors.toList());

			 List<CoverNoteAddon> addonsall=new ArrayList<CoverNoteAddon>();
			 for (PolicyCoverData distintc : distinctsCovers) {
				 List<PolicyCoverData> covers =m.getCovers().stream()
						 .filter(p-> p.getCoverId().equals(distintc.getCoverId()) )									
						 .collect(Collectors.toList());

				 TaxFromData taxesutils=new TaxFromData();							
				 List<TaxCharged> taxes = covers.stream().filter(p -> p.getDiscLoadId()==0 ).map(taxesutils).filter(d->d!=null).collect(Collectors.toList());

				 AddonFromData addutil=new AddonFromData(taxes);
				 List<CoverNoteAddon> addons = covers.stream().map(addutil).filter(d->d!=null).collect(Collectors.toList());  
				 addonsall.addAll(addons);

			 }
			 //addon reference
			 AtomicInteger a=new AtomicInteger(1);
			 addonsall.stream().forEach(i-> i.setAddonReference(String.valueOf(a.getAndIncrement())));
			 coverDtl.setCoverNoteAddonsBean(CoverNoteAddons.builder().coverNoteAddonBeanList(addonsall).build());
			   
				
					
				
				PersonalInfo c = m.getCustomerInfo();
				SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
			    String postaladdress=	(StringUtils.isNotBlank(c.getAddress1())?c.getAddress1():"")+(StringUtils.isNotBlank(c.getAddress2())?c.getAddress2():"");
			    MotorVehicleInfo v = m.getVehicleInfo();
			    MotorDataDetails mdd = m.getMdd();
			    PolicyHolder p=null;
			    String custName = null;
			    if(c.getPolicyHolderType().equalsIgnoreCase("1")) {
			    	custName =	(m.getPolicy().getProductId() == 5 && "6".equals(c.getIdType())) ? ""  : c.getClientName();
			    } else {
			    	custName = c.getClientName();
			    }
			    		
			    if(m.getPolicy().getProductId()==46) {
			    	p=PolicyHolder.builder()
			    			.countryCode(c.getNationality())
			    			.district(c.getStateName())
			    			.emailAddress(c.getEmail1())
			    			.gender(c.getGender())
			    			.policyHolderBirthDate(c.getDobOrRegDate()!=null?format.format(c.getDobOrRegDate()):null)
			    			.policyHolderFax(c.getFax())
			    			.policyHolderIdNumber(c.getIdNumber())
			    			//.policyHolderName((v!=null && v.getResOwnerName()!=null)?v.getResOwnerName():c.getClientName())
			    			.policyHolderName(c.getClientName())
			    			.policyHolderPhoneNumber(c.getMobileCodeDesc1().concat(c.getMobileNo1()))
			    			.policyHolderType("1")
			    			.policyHolderIdType("3")
			    			.postalAddress(postaladdress)
			    			.region(c.getStateName())
			    			.street(StringUtils.isBlank(c.getStreet())?c.getCityName():c.getStreet())	
			    			.district(c.getCityName())
			    			.build();
			    }else {
			    	
			    	
			    	p=PolicyHolder.builder()
			    			.countryCode(c.getNationality())
			    			.district(c.getStateName())
			    			.emailAddress(c.getEmail1())
			    			.gender(c.getGender())
			    			.policyHolderBirthDate(c.getDobOrRegDate()!=null?format.format(c.getDobOrRegDate()):null)
			    			.policyHolderFax(c.getFax())
			    			.policyHolderIdNumber(c.getIdNumber())
			    			//.policyHolderName((v!=null && v.getResOwnerName()!=null)?v.getResOwnerName():c.getClientName())
			    			.policyHolderName(custName)
			    			.policyHolderPhoneNumber(c.getMobileCodeDesc1().concat(c.getMobileNo1()))
			    			.policyHolderType(c.getPolicyHolderType())
			    			.policyHolderIdType(c.getIdType())
			    			.postalAddress(postaladdress)
			    			.region(c.getStateName())
			    			.street(StringUtils.isBlank(c.getStreet())?c.getCityName():c.getStreet())	
			    			.district(c.getCityName())
			    			.build();
			    }
				List<PolicyHolder> policyHolders=new ArrayList<PolicyHolder>();
				policyHolders.add(p);
				coverDtl.setPolicyHoldersBean(PolicyHolders.builder().policyHolderBeanList(policyHolders).build());
				
				
				if(v!=null) {
					MotorDtl mdl=MotorDtl.builder()
							.axleDistance(v.getResAxleDistance() != null ? v.getResAxleDistance().toString() : "0")
							.bodyType(v.getResBodyType())
							.chassisNumber(v.getResChassisNumber())
							.color(v.getResColor())
							.engineCapacity(v.getResEngineCapacity())
							.fuelUsed(v.getResFuelUsed())
							.grossWeight(v.getResGrossWeight().toString())
							.make(v.getResMake())
							.model(v.getResModel())
							.modelNumber(v.getModelNumber())
							.engineNumber(v.getResEngineNumber())
							.motorCategory(v.getResMotorCategory() != null ? v.getResMotorCategory().toString() : v.getReqMotorCategory().toString())
							.motorType("1")//doubt
							.motorUsage(v.getResMotorUsage().contains("Private")?"1":"2")
							.numberOfAxles(v.getResNumberOfAxles() != null ? v.getResNumberOfAxles().toString() : "0")
							.ownerAddress(c.getAddress1())
							.ownerCategory(v.getResOwnerCategory().contains("Company")?"2":"1")
							.ownerName(c.getClientName())//v.getResOwnerName()
							.registrationNumber(v.getResRegNumber())
							.sittingCapacity(v.getResSittingCapacity().toString())
							.tareWeight(v.getResTareWeight().toString())
							.yearOfManufacture(v.getResYearOfManufacture().toString())
							.build();

					coverDtl.setMotorDtlBean(mdl);
				}else if(mdd!=null) {

					MotorDtl mdl=MotorDtl.builder()
							.axleDistance("")
							.bodyType(mdd.getTiraBodyType())
							.chassisNumber(mdd.getChassisNumber())
							.color(mdd.getColorDesc())
							.engineCapacity(mdd.getCubicCapacity()==null?"":mdd.getCubicCapacity().toString())
							.fuelUsed(mdd.getFuelTypeDesc())
							.grossWeight(mdd.getGrossWeight()==null?"":mdd.getGrossWeight().toString())
							.make(mdd.getVehicleMakeDesc())
							.model(mdd.getVehcileModelDesc())
							.modelNumber("")
							.engineNumber(StringUtils.isBlank(mdd.getEngineNumber())?"":mdd.getEngineNumber())
							.motorCategory(mdd.getMotorCategory())
							.motorType("1")//doubt
							.motorUsage(mdd.getMotorUsageDesc().contains("Private")?"1":"2")// v.getResMotorUsage().contains("Private")?"1":"2"
							.numberOfAxles("")
							.ownerAddress(c.getAddress1())
							.ownerCategory(c.getPolicyHolderType())
							.ownerName(c.getClientName())
							.registrationNumber(mdd.getChassisNumber())
							.sittingCapacity(mdd.getSeatingCapacity().toString())
							.tareWeight("")
							.yearOfManufacture(mdd.getManufactureYear().toString())
							.build();

					coverDtl.setMotorDtlBean(mdl);
				
				}
				//return coverDtl;
		 }catch (Exception e) {
			 e.printStackTrace();
		}
		 
		 //return null
	}
	
	
	public void riskDataNonMotor(MaansarovarToTira m, NonCoverNoteDtl cn , boolean is,String endt) {

		 try {
			 
			 List<PolicyCoverData> distinctSections = m.getCovers().stream().filter(distinctByKey(ss -> ss.getSectionId())).collect(Collectors.toList());
			   
			 List<RiskCovered> risks=new ArrayList<RiskCovered>();
			 for(PolicyCoverData policy :distinctSections) {
				 List<PolicyCoverData> distinctsVehicle = m.getCovers().stream()
						 .filter(pol -> pol.getSectionId().compareTo(policy.getSectionId())==0)
						 .filter(distinctByKey(cust -> cust.getVehicleId()))
						 .collect(Collectors.toList()); 

				 for(PolicyCoverData v  :distinctsVehicle) {
//					 List<PolicyCoverData> distinctsCovers = m.getCovers().stream()
//							 .filter(pol -> pol.getSectionId().compareTo(policy.getSectionId())==0)
//							 .filter(p-> p.getVehicleId().equals(v.getVehicleId()) &&  (p.getCoverageType().equals("B") || (p.getCoverageType().equals("O")  || p.getCoverageType().equals("E")) )
//							 .filter(distinctByKey(cust -> cust.getCoverId()))
//							 .collect(Collectors.toList());
						List<PolicyCoverData> distinctsCovers = m.getCovers().stream()
								.filter(pol -> pol.getSectionId().compareTo(policy.getSectionId()) == 0)
								.filter(p -> p.getVehicleId().equals(v.getVehicleId())
										&& ("B".equals(p.getCoverageType()) || "O".equals(p.getCoverageType())
												|| "E".equals(p.getCoverageType())))
								.filter(distinctByKey(cust -> cust.getCoverId())).collect(Collectors.toList());
					 List<PolicyCoverData> totalcovers = m.getCovers().stream()
							 .filter(pol -> pol.getSectionId().compareTo(policy.getSectionId())==0)
							 .filter(p-> p.getVehicleId().equals(v.getVehicleId()))									
							 .collect(Collectors.toList());

					 for (PolicyCoverData distintc : distinctsCovers) {
						 //	List<Cover> totalcovers=new ArrayList<Cover>();
						 List<PolicyCoverData> covers =totalcovers.stream()
								 .filter(p-> p.getCoverId().equals(distintc.getCoverId()) )									
								 .collect(Collectors.toList());

						 DiscountFromData discountUtil=new DiscountFromData();
						 List<DiscountOffered> discounts = covers.stream().map(discountUtil).filter(d->d!=null).collect(Collectors.toList());
						 LoadingFromData loadingtuils=new LoadingFromData();
						 List<DiscountOffered> loadings = covers.stream().map(loadingtuils).filter(d->d!=null).collect(Collectors.toList());
						 TaxFromData taxesutils=new TaxFromData();							
						 List<TaxCharged> taxes = covers.stream().filter(p -> p.getDiscLoadId()==0 ).map(taxesutils).filter(d->d!=null).collect(Collectors.toList());

					//	 CoverFromData splitsub=new CoverFromData("B",discounts,loadings,taxes);
						 if(is)
						 {
							 List<TaxCharged> taxesEndt = covers.stream().filter(p -> p.getDiscLoadId().equals(Integer.valueOf(endt)) ).map(taxesutils).filter(d->d!=null).collect(Collectors.toList());
							 CoverFromDataForNMEndt splitsub=new CoverFromDataForNMEndt("E",discounts,loadings,taxesEndt);
							 List<RiskCovered> risk= covers.stream().map(splitsub).filter(d->d!=null).collect(Collectors.toList());  
							 
							 risks.addAll(risk);
						 }else
						 {
							 CoverFromDataForNM splitsub=new CoverFromDataForNM("B",discounts,loadings,taxes);
							 List<RiskCovered> risk= covers.stream().map(splitsub).filter(d->d!=null).collect(Collectors.toList());   
							 risks.addAll(risk);
						 }
						
						 

					 }				 

				 }
			 } 
			 cn.setRisksCoveredBean(RisksCovered.builder().riskCoveredBeanList(risks).build());
			  
			//Section
			 List<SubjectMatter> subjectMatter=new ArrayList<SubjectMatter>();
			 ProductSectionMaster section = m.getSection();
			 SubjectMatter subject=new SubjectMatter();
			 subject.setSubjectMatterReference(section.getCoreAppCode());
			 subject.setSubjectMatterDesc(section.getSectionName());
			 subjectMatter.add(subject);

			 SubjectMattersCovered subjectMatters=new SubjectMattersCovered();						
			 subjectMatters.setSubjectMatterBeanList(subjectMatter);
			 cn.setSubjectMattersCoveredBean(subjectMatters);
//
//			 List<PolicyCoverData> distinctsCovers = m.getCovers().stream()
//					 
//					 .filter(p-> /*p.getVehicleId().equals(v.getVehicleId())&&*/ p.getCoverageType().equalsIgnoreCase("O"))
//					 .filter(distinctByKey(cust -> cust.getCoverId() ))
//					 .collect(Collectors.toList());
//
//			 List<CoverNoteAddon> addonsall=new ArrayList<CoverNoteAddon>();
//			 for (PolicyCoverData distintc : distinctsCovers) {
//				 List<PolicyCoverData> covers =m.getCovers().stream()
//						 .filter(p-> p.getCoverId().equals(distintc.getCoverId()) )									
//						 .collect(Collectors.toList());
//
//				 TaxFromData taxesutils=new TaxFromData();							
//				 List<TaxCharged> taxes = covers.stream().filter(p -> p.getDiscLoadId()==0 ).map(taxesutils).filter(d->d!=null).collect(Collectors.toList());
//
//				 AddonFromData addutil=new AddonFromData(taxes);
//				 List<CoverNoteAddon> addons = covers.stream().map(addutil).filter(d->d!=null).collect(Collectors.toList());  
//				 addonsall.addAll(addons);
//
//			 }
			 //addon reference
			 List<CoverNoteAddon> addonsall=new ArrayList<CoverNoteAddon>();
//			 
//			 for(PolicyCoverData policy :distinctSections) {
//				 List<PolicyCoverData> distinctsVehicle = m.getCovers().stream()
//						 .filter(pol -> pol.getSectionId().compareTo(policy.getSectionId())==0)
//						 .filter(distinctByKey(cust -> cust.getVehicleId()))
//						 .collect(Collectors.toList()); 
//
//				 for(PolicyCoverData v  :distinctsVehicle) {
//					 List<PolicyCoverData> distinctsCovers = m.getCovers().stream()
//							 .filter(pol -> pol.getSectionId().compareTo(policy.getSectionId())==0)
//							 .filter(p-> p.getVehicleId().equals(v.getVehicleId()) &&  (p.getCoverageType().equals("O")) )
//							 .filter(distinctByKey(cust -> cust.getCoverId() ))
//							 .collect(Collectors.toList());
//					 List<PolicyCoverData> totalcovers = m.getCovers().stream()
//							 .filter(pol -> pol.getSectionId().compareTo(policy.getSectionId())==0)
//							 .filter(p-> p.getVehicleId().equals(v.getVehicleId()))									
//							 .collect(Collectors.toList());
//
//					 for (PolicyCoverData distintc : distinctsCovers) {
//						 List<PolicyCoverData> covers =totalcovers.stream()
//								 .filter(p-> p.getCoverId().equals(distintc.getCoverId()) )									
//								 .collect(Collectors.toList());
////						 List<PolicyCoverData> covers =m.getCovers().stream()
////								 .filter(p-> p.getCoverId().equals(distintc.getCoverId()) )									
////								 .collect(Collectors.toList());
//
//						 TaxFromData taxesutils=new TaxFromData();							
//						 List<TaxCharged> taxes = covers.stream().filter(p -> p.getDiscLoadId()==0 ).map(taxesutils).filter(d->d!=null).collect(Collectors.toList());
//
//						 AddonFromData addutil=new AddonFromData(taxes);
//						 List<CoverNoteAddon> addons = covers.stream().map(addutil).filter(d->d!=null).collect(Collectors.toList());  
//						 addonsall.addAll(addons);
//
//					 }				 
//
//				 }
//			 }
			 AtomicInteger a=new AtomicInteger(1);
			 addonsall.stream().forEach(i-> i.setAddonReference(String.valueOf(a.getAndIncrement())));
			 cn.setCoverNoteAddonsBean(CoverNoteAddons.builder().coverNoteAddonBeanList(addonsall).build());
			   
				
					
				
				PersonalInfo c = m.getCustomerInfo();
				SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
			    String postaladdress=	(StringUtils.isNotBlank(c.getAddress1())?c.getAddress1():"")+(StringUtils.isNotBlank(c.getAddress2())?c.getAddress2():"");
			    MotorVehicleInfo v = m.getVehicleInfo();
			    
				PolicyHolder p=PolicyHolder.builder()
							.countryCode(c.getNationality())
							.district(c.getStateName())
							.emailAddress(c.getEmail1())
							.gender(c.getGender())
							.policyHolderBirthDate(format.format(c.getDobOrRegDate()))
							.policyHolderFax(c.getFax())
							.policyHolderIdNumber(c.getIdNumber())
							.policyHolderName((v!=null && v.getResOwnerName()!=null)?v.getResOwnerName():c.getClientName())
							.policyHolderPhoneNumber(c.getMobileCodeDesc1().concat(c.getMobileNo1()))
							.policyHolderType(c.getPolicyHolderType())
							.policyHolderIdType(c.getIdType())
							.postalAddress(postaladdress)
							.region(c.getStateName())
							.street(StringUtils.isBlank(c.getStreet())?c.getCityName():c.getStreet())	
							.district(c.getCityName())
							.build();
							 
				List<PolicyHolder> policyHolders=new ArrayList<PolicyHolder>();
				policyHolders.add(p);
				cn.setPolicyHoldersBean(PolicyHolders.builder().policyHolderBeanList(policyHolders).build());
				
				
				
				//return coverDtl;
		 }catch (Exception e) {
			 e.printStackTrace();
		}
		 
		 //return null
	}
	
	public TiraMsgCoverPush convertDomestic(MaansarovarToTira m) {
		try
		{
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat secondsformat=new SimpleDateFormat("'T'HH:mm:ss");
		secondsformat.setTimeZone(TimeZone.getTimeZone("EAT"));
		format.setTimeZone(TimeZone.getTimeZone("EAT"));
		
		String seconds = secondsformat.format( new Date());
		NonCoverNoteDtl cn=new NonCoverNoteDtl();
		System.out.println("tira collectInfo Req Non motor 1==>:");
		//cn.setCoverNoteNumber(m.getPolicy().getQuoteNo());
		boolean is =false;
		SectionDataDetails dataDetail = m.getSectionData().get(0);
		System.out.println("tira collectInfo Req Non motor 2==>:" + dataDetail);
		String joinWith = StringUtils.joinWith("-",dataDetail.getLocationId().toString(), dataDetail.getSectionId(), dataDetail.getRiskId().toString());
		cn.setCoverNoteNumber(m.getPolicy().getQuoteNo()+"-"+joinWith+"_"+twoDigitRandom());
		System.out.println("tira collectInfo Req Non motor 3==>:");
		cn.setPrevCoverNoteReferenceNumber(StringUtils.isNotBlank(m.getPolicy().getEndtTypeId())?dataDetail.getPrevCovernoteRefno():null);
		System.out.println("tira collectInfo Req Non motor 4==>:");
		cn.setSalePointCode(StringUtils.isBlank(m.getPolicy().getSalePointCode())?"SP500":m.getPolicy().getSalePointCode());
		cn.setCoverNoteStartDate(format.format(m.getPolicy().getInceptionDate())+seconds);
		cn.setCoverNoteEndDate(format.format(m.getPolicy().getExpiryDate())+"T23:59:59");
		cn.setCoverNoteDesc("Cover Note for "+m.getPolicy().getQuoteNo());
		System.out.println("tira collectInfo Req Non motor 5==>:");
		cn.setOperativeClause(m.getProduct().getProductDesc());
		cn.setPaymentMode(m.getLtPayment().getItemCode());  
		cn.setCurrencyCode(m.getPolicy().getCurrency());
		System.out.println("tira collectInfo Req Non motor 6==>:");
		cn.setExchangeRate(m.getPolicy().getExchangeRate().toString());
		System.out.println("tira collectInfo Req Non motor 7==>:");
		String pattern =  "#####0.####" ;
		DecimalFormat decimalFormat = new DecimalFormat(pattern);
		BigDecimal actualPremim = m.getSectionData().stream().map(t-> t.getActualPremiumFc()).reduce(BigDecimal.ZERO, BigDecimal::add);
		System.out.println("tira collectInfo Req Non motor 8==>:");
		BigDecimal overallPremium = m.getSectionData().stream().map(t-> t.getOverallPremiumFc()).reduce(BigDecimal.ZERO, BigDecimal::add);
		System.out.println("tira collectInfo Req Non motor 09==>:");
		//BigDecimal overallPremium = m.getSectionData().stream().map(t-> t.getOverallPremiumFc()).reduce(BigDecimal.ZERO, BigDecimal::add);
		cn.setTotalPremiumExcludingTax(decimalFormat.format(actualPremim)); //ss
		cn.setTotalPremiumIncludingTax(decimalFormat.format(overallPremium));
		double commissionRate = m.getPolicy().getCommissionPercentage().divide(new BigDecimal(100)).doubleValue(); 
		
		
		
		cn.setCommisionRate(String.valueOf("0"));
		cn.setCommisionPaid("0");
		//cn.setCommisionPaid(m.getPolicy().getCommission().toEngineeringString());
		
		/*cn.setOfficerName((m.getPolicy().getApplicationId().equals("1") || m.getPolicy().getApplicationId().equals("01")) ?m.getBroker().getUserName():m.getUw().getCompanyName());
		
		cn.setOfficerTitle((m.getPolicy().getApplicationId().equals("1") || m.getPolicy().getApplicationId().equals("01"))?"Broker":"Underwriter");*/
		cn.setOfficerName(m.getPolicy().getCustomerName());	
		System.out.println("tira collectInfo Req Non motor 10==>:");
		cn.setOfficerTitle(StringUtils.isNotBlank(m.getPolicy().getSourceType())?m.getPolicy().getSourceType().replaceAll("Premia ", "").toUpperCase(): "UnderWriter");
		if("59".equals(m.getProduct().getProductId().toString())) {
			cn.setProductCode(m.getProduct().getRegulatoryCode());
		}else
		{
			cn.setProductCode(m.getSection().getRegulatoryCode());//m.getProduct().getRegulatoryCode()	
		}
		System.out.println("tira collectInfo Req Non motor 11==>:");
		
		String coverNoteType=StringUtils.isNotBlank(m.getPolicy().getEndtTypeId())?"3":(StringUtils.isNotBlank(m.getPolicy().getRenewalStatus()) ?"2":"1");
		if("3".equals(coverNoteType)) {
			EndtTypeMaster e = m.getEndt();
			String endorsementtype="";
			if("842".equals(m.getPolicy().getEndtTypeId())) {
				endorsementtype="4";
			}else if(e.getIsCoverendt().equalsIgnoreCase("Y") ) {
				endorsementtype="3";
			}else if(m.getPolicy().getIsChargRefund().equalsIgnoreCase("CHARGE") ) {
				endorsementtype="1";
			}else if(m.getPolicy().getIsChargRefund().equalsIgnoreCase("REFUND") ) {
				endorsementtype="2";
			}
			cn.setEndorsementType(endorsementtype);
			cn.setEndorsementReason(m.getPolicy().getEndtTypeDesc());
			cn.setEndorsementPremiumEarned(m.getPolicy().getEndtPremium().abs().add(m.getPolicy().getEndtPremiumTax().abs()) .toPlainString());
			if("842".equals(m.getPolicy().getEndtTypeId())) {
				HomePositionMaster byQuoteNo = home.findByQuoteNo(m.getPolicy().getEndtPrevQuoteNo());
				cn.setCoverNoteStartDate(format.format(m.getPolicy().getEffectiveDate())+seconds);
				cn.setCoverNoteEndDate(format.format(byQuoteNo.getExpiryDate())+"T23:59:59");
				cn.setEndorsementPremiumEarned(m.getPolicy().getEndtEarendPre().abs().add(m.getPolicy().getEndtEarendTax().abs()) .toPlainString());
				is=true;
			}
		}else {
			cn.setEndorsementType(null);
			cn.setEndorsementReason(null);
			cn.setEndorsementPremiumEarned(null);
		}
		
		riskDataNonMotor(m, cn,is,m.getPolicy().getEndtTypeId());
	
		CoverNoteHdr hdr=CoverNoteHdr.builder()
				.callBackUrl(nonMotorWebhook)
			/*	.companyCode("ICC105")
				.coverNoteType(coverNoteType)
				.insurerCompanyCode("ICC105")
				.requestId("EWAY"+Calendar.getInstance().getTimeInMillis())
				.systemCode("LSYS_EWAYINSURANCE_001")
				.tranCompanyCode("ICC105")*/
				.companyCode(tiraCompanyCode) // "ICC105"--1CC125
				.coverNoteType(coverNoteType)
				.insurerCompanyCode(headearAdd1Value)  // "ICC105" --ICC110
				.requestId("EWAY"+Calendar.getInstance().getTimeInMillis())
				.systemCode(tiraSystemCode) //"LSYS_EWAYINSURANCE_001"
				.tranCompanyCode(StringUtils.isBlank(m.getPolicy().getBrokerTiraCode())?headearAdd1Value:m.getPolicy().getBrokerTiraCode())  // "ICC105" --ICC110--HeadearAdd1Value
				.build();
		System.out.println("tira collectInfo Req Non motor 5==>:");
		CoverNoteRefReq mt=new CoverNoteRefReq();		
		mt.setCoverNoteDtlBean(cn);
		mt.setCoverNoteHdrBean(hdr);
		TiraMsgCoverPush p=new TiraMsgCoverPush();
		p.setCoverNoteRefReq(mt);
		System.out.println("tira collectInfo Req Non motor ==>:" + p);
		 return p;
		}catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("Exception in Tira Push for Non Motor ====>" + e);
			return null;
		}
		
	}
	
	private Date getZeroTimeDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		date = calendar.getTime();
		return date;
	}
	
	private int twoDigitRandom() {
		try {
			Random random = new Random(); 
			int randomTwoDigitNumber = 10 + random.nextInt(90);
			return randomTwoDigitNumber;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return 17;
	}
}
