package com.maan.eway.tira.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.maan.eway.bean.EndtTypeMaster;
import com.maan.eway.bean.MotorDataDetails;
import com.maan.eway.bean.MotorVehicleInfo;
import com.maan.eway.bean.PersonalInfo;
import com.maan.eway.bean.PolicyCoverData;
import com.maan.eway.bean.ProductSectionMaster;
import com.maan.eway.req.fleet.FleetCoverNoteDtl;
import com.maan.eway.req.fleet.FleetDtl;
import com.maan.eway.req.fleet.FleetHdr;
import com.maan.eway.req.fleet.FleetMotorCoverNoteRefReq;
import com.maan.eway.req.fleet.TiraMsgVehiclePushFleet;
import com.maan.eway.req.push.CoverNoteAddon;
import com.maan.eway.req.push.CoverNoteAddons;
import com.maan.eway.req.push.CoverNoteHdr;
import com.maan.eway.req.push.DiscountOffered;
import com.maan.eway.req.push.MotorDtl;
import com.maan.eway.req.push.PolicyHolder;
import com.maan.eway.req.push.PolicyHolders;
import com.maan.eway.req.push.RiskCovered;
import com.maan.eway.req.push.RisksCovered;
import com.maan.eway.req.push.SubjectMatter;
import com.maan.eway.req.push.SubjectMattersCovered;
import com.maan.eway.req.push.TaxCharged;
import com.maan.eway.tira.bean.MaansarovarToTira;
import com.maan.eway.tira.util.AddonFromData;
import com.maan.eway.tira.util.CoverFromData;
import com.maan.eway.tira.util.DiscountFromData;
import com.maan.eway.tira.util.LoadingFromData;
import com.maan.eway.tira.util.TaxFromData;

@Service
public class ConvertTiraFleetRequest {

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
	
	@Value("${vehicleFleetPostingReturnLink}")
	private String vehicleFleetPostingReturnLink;
	
	// predicate to filter the duplicates by the given key extractor.
	public  <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
		Map<Object, Boolean> uniqueMap = new ConcurrentHashMap<>();
		return t -> uniqueMap.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}
	
	public FleetDtl riskData(MaansarovarToTira m,Integer riskId) {

		 try {
			 FleetDtl coverDtl=new FleetDtl();
			 coverDtl.setFleetEntry(riskId);
			 String coverNoteType=StringUtils.isNotBlank(m.getPolicy().getEndtTypeId())?"3":(StringUtils.isNotBlank(m.getPolicy().getRenewalStatus()) ?"2":"1");
			    coverDtl.setCoverNoteNumber(m.getPolicy().getQuoteNo()+"-"+riskId);
				coverDtl.setPrevCoverNoteReferenceNumber(StringUtils.isNotBlank(m.getPolicy().getEndtTypeId())?m.getPolicy().getPrevCovernoteRefno():null);
				coverDtl.setCoverNoteDesc("Cover Note for "+m.getPolicy().getQuoteNo());
				coverDtl.setOperativeClause(m.getProduct().getProductDesc());
				 
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
					coverDtl.setEndorsementType(endorsementtype);
					coverDtl.setEndorsementReason(m.getPolicy().getEndtTypeDesc());
					coverDtl.setEndorsementPremiumEarned(m.getPolicy().getEndtPremium().abs().add(m.getPolicy().getEndtPremiumTax().abs()) .toPlainString());
				}else {
					coverDtl.setEndorsementType(null);
					coverDtl.setEndorsementReason(null);
					coverDtl.setEndorsementPremiumEarned(null);
				}
				 
			 
			 
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
				SimpleDateFormat format=new SimpleDateFormat("YYYY-MM-dd");
			  
			    MotorVehicleInfo v = m.getVehicleInfo();
			    MotorDataDetails mdd = m.getMdd();
			    
				  String postaladdress=	(StringUtils.isNotBlank(c.getAddress1())?c.getAddress1():"")+(StringUtils.isNotBlank(c.getAddress2())?c.getAddress2():"");
				 PolicyHolder p=PolicyHolder.builder()
							.countryCode(c.getNationality())
							.district(c.getStateName())
							.emailAddress(c.getEmail1())
							.gender(c.getGender())
							.policyHolderBirthDate(c.getDobOrRegDate()!=null?format.format(c.getDobOrRegDate()):null)
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
				//List<PolicyHolder> policyHolders=new ArrayList<PolicyHolder>();
				//policyHolders.add(p);
			//	coverDtl.setPolicyHoldersBean(PolicyHolders.builder().policyHolderBeanList(policyHolders).build());
				
				
				if(v!=null) {
					MotorDtl mdl=MotorDtl.builder()
							.axleDistance(v.getResAxleDistance().toString())
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
							.motorCategory(v.getResMotorCategory().toString())
							.motorType("1")//doubt
							.motorUsage(v.getResMotorUsage().contains("Private")?"1":"2")
							.numberOfAxles(v.getResNumberOfAxles().toString())
							.ownerAddress(c.getAddress1())
							.ownerCategory(v.getResOwnerCategory().contains("Company")?"2":"1")
							.ownerName(v.getResOwnerName())
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
				return coverDtl;
		 }catch (Exception e) {
			 e.printStackTrace();
		}
		 
		 return null;
	}

	public FleetHdr fleetHdr(MaansarovarToTira m,Integer fleetSize,Integer comphInsured) {
		try {
			FleetHdr hdr=new FleetHdr();
			PersonalInfo c = m.getCustomerInfo();
			SimpleDateFormat format=new SimpleDateFormat("YYYY-MM-dd");
			SimpleDateFormat secondsformat=new SimpleDateFormat("'T'HH:mm:ss");
			String seconds = secondsformat.format( new Date());
			
		    MotorVehicleInfo v = m.getVehicleInfo();
		    //MotorDataDetails mdd = m.getMdd();
		    
			  String postaladdress=	(StringUtils.isNotBlank(c.getAddress1())?c.getAddress1():"")+(StringUtils.isNotBlank(c.getAddress2())?c.getAddress2():"");
			 PolicyHolder p=PolicyHolder.builder()
						.countryCode(c.getNationality())
						.district(c.getStateName())
						.emailAddress(c.getEmail1())
						.gender(c.getGender())
						.policyHolderBirthDate(c.getDobOrRegDate()!=null?format.format(c.getDobOrRegDate()):null)
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
			hdr.setPolicyHoldersBean(PolicyHolders.builder().policyHolderBeanList(policyHolders).build());
			
			
			String coverNoteType=StringUtils.isNotBlank(m.getPolicy().getEndtTypeId())?"3":(StringUtils.isNotBlank(m.getPolicy().getRenewalStatus()) ?"2":"1");
			
			hdr.setFleetId(m.getPolicy().getQuoteNo());
			hdr.setFleetSize(fleetSize);
			Integer fleetType=StringUtils.isNotBlank(m.getPolicy().getEndtTypeId())?2:(StringUtils.isNotBlank(m.getPolicy().getRenewalStatus()) ?1:1);
			hdr.setFleetType(fleetType);
			hdr.setComprehensiveInsured(comphInsured);
			hdr.setSalePointCode(StringUtils.isBlank(m.getPolicy().getSalePointCode())?"SP500":m.getPolicy().getSalePointCode()); //SC001 //"SC014"
			if("3".equals(coverNoteType) && "842".equals(m.getPolicy().getEndtTypeId())) {
				hdr.setCoverNoteStartDate(format.format(m.getPolicy().getEndorsementEffdate())+seconds);
				hdr.setCoverNoteEndDate(format.format(m.getPolicy().getCancelledDate())+"T23:59:59");
			}else {
				hdr.setCoverNoteStartDate(format.format(m.getPolicy().getInceptionDate())+seconds);
				hdr.setCoverNoteEndDate(format.format(m.getPolicy().getExpiryDate())+"T23:59:59");
			}
			/*hdr.setCoverNoteDesc("Cover Note for "+m.getPolicy().getQuoteNo());
			hdr.setOperativeClause(m.getProduct().getProductDesc());*/
			hdr.setPaymentMode(m.getLtPayment().getItemCode());  
			hdr.setCurrencyCode(m.getPolicy().getCurrency());
			hdr.setExchangeRate(m.getPolicy().getExchangeRate().toString());
			hdr.setTotalPremiumExcludingTax(m.getPolicy().getPremiumFc().toPlainString());
			hdr.setTotalPremiumIncludingTax(m.getPolicy().getOverallPremiumFc().toPlainString());
			double commissionRate = m.getPolicy().getCommissionPercentage() ==null ?0D:m.getPolicy().getCommissionPercentage().divide(new BigDecimal(100)).doubleValue(); 
			
			hdr.setCommisionRate(String.valueOf(commissionRate));
			//hdr.setCommisionPaid("0");
			hdr.setCommisionPaid(m.getPolicy().getCommission()==null?"0":m.getPolicy().getCommission().abs().toEngineeringString());
			
			//hdr.setOfficerName((m.getPolicy().getApplicationId().equals("1") || m.getPolicy().getApplicationId().equals("01")) ?m.getBroker().getUserName():m.getUw().getCompanyName());
			hdr.setOfficerName(m.getPolicy().getCustomerName());
			
			hdr.setOfficerTitle(StringUtils.isNotBlank(m.getPolicy().getSourceType())?m.getPolicy().getSourceType().replaceAll("Premia ", "").toUpperCase(): "UnderWriter");
			hdr.setProductCode(m.getProduct().getRegulatoryCode());//m.getProduct().getRegulatoryCode()
			return hdr;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public TiraMsgVehiclePushFleet createCover(FleetHdr fleetHdr, List<FleetDtl> riskDatas,MaansarovarToTira m) {
		try {
			String coverNoteType=StringUtils.isNotBlank(m.getPolicy().getEndtTypeId())?"3":(StringUtils.isNotBlank(m.getPolicy().getRenewalStatus()) ?"2":"1");
			
			CoverNoteHdr hdr=CoverNoteHdr.builder()
					.callBackUrl(vehicleFleetPostingReturnLink)
					.companyCode(tiraCompanyCode) // "ICC105"--1CC125

					.coverNoteType(coverNoteType)
					.insurerCompanyCode(headearAdd1Value)  // "ICC105" --ICC110
					.requestId("EWAY"+Calendar.getInstance().getTimeInMillis())
					.systemCode(tiraSystemCode) //"LSYS_EWAYINSURANCE_001"
					.tranCompanyCode(StringUtils.isBlank(m.getPolicy().getBrokerTiraCode())?headearAdd1Value:m.getPolicy().getBrokerTiraCode())  // "ICC105" --ICC110--HeadearAdd1Value


					.build();
			
			FleetCoverNoteDtl fn=new FleetCoverNoteDtl();
			fn.setFleetDtl(riskDatas);
			fn.setFleetHdr(fleetHdr);
			
			FleetMotorCoverNoteRefReq mt=new FleetMotorCoverNoteRefReq();		
			mt.setCoverNoteDtlBean(fn);
			mt.setCoverNoteHdrBean(hdr);
			 
			TiraMsgVehiclePushFleet p=new TiraMsgVehiclePushFleet();
			p.setMotorCoverNoteRefReq(mt);
			return p;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
