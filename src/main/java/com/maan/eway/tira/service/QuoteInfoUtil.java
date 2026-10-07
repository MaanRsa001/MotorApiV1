package com.maan.eway.tira.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.maan.eway.bean.CompanyProductMaster;
import com.maan.eway.bean.EndtTypeMaster;
import com.maan.eway.bean.HomePositionMaster;
import com.maan.eway.bean.InsuranceCompanyMaster;
import com.maan.eway.bean.ListItemValue;
import com.maan.eway.bean.LoginMaster;
import com.maan.eway.bean.LoginUserInfo;
import com.maan.eway.bean.MotorDataDetails;
import com.maan.eway.bean.MotorVehicleInfo;
import com.maan.eway.bean.PaymentDetail;
import com.maan.eway.bean.PersonalInfo;
import com.maan.eway.bean.PolicyCoverData;
import com.maan.eway.bean.ProductSectionMaster;
import com.maan.eway.bean.SectionDataDetails;
import com.maan.eway.bean.TiraTrackingDetails;
import com.maan.eway.repository.CompanyProductMasterRepository;
import com.maan.eway.repository.EndtTypeMasterRepository;
import com.maan.eway.repository.HomePositionMasterRepository;
import com.maan.eway.repository.InsuranceCompanyMasterRepository;
import com.maan.eway.repository.ListItemValueRepository;
import com.maan.eway.repository.LoginMasterRepository;
import com.maan.eway.repository.LoginUserInfoRepository;
import com.maan.eway.repository.MotorDataDetailsRepository;
import com.maan.eway.repository.MotorVehicleInfoRepository;
import com.maan.eway.repository.PaymentDetailRepository;
import com.maan.eway.repository.PersonalInfoRepository;
import com.maan.eway.repository.PolicyCoverDataRepository;
import com.maan.eway.repository.ProductSectionMasterRepository;
import com.maan.eway.repository.SectionDataDetailsRepository;
import com.maan.eway.repository.TiraTrackingDetailsRepository;

@Component
public class QuoteInfoUtil {

	@Autowired
	private HomePositionMasterRepository hpmRepo;
	
	public HomePositionMaster getFromHomePositionMaster(String quoteNo) {
		try {
			hpmRepo.flush();
			HomePositionMaster h = hpmRepo.findByQuoteNo(quoteNo);
			return h;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	@Autowired
	private PolicyCoverDataRepository pcdRepo;
	
	public List<PolicyCoverData> getFromPolicyCoverData(String quoteNo){
		try {
			List<PolicyCoverData> qs=pcdRepo.findByQuoteNo(quoteNo);
			return qs;
		}catch (Exception e) { 
			e.printStackTrace();
		}
		return null;
		
	}
	
	@Autowired
	private LoginMasterRepository logRepo;
	
	public LoginMaster getLogInDetails(String loginId,String companyId) {

		try {
			LoginMaster qs=logRepo.findByCompanyIdAndLoginId(companyId,loginId);
			return qs;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	
	}
	
	
	@Autowired
	private LoginUserInfoRepository logUserRepo;
	
	public LoginUserInfo getLogInUserDetails(String loginId,String companyId) {

		try {
			LoginUserInfo qs=logUserRepo.findByLoginId(loginId);
			return qs;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;  
	}
	
	
	@Autowired
	private CompanyProductMasterRepository cpmRepo;
	public CompanyProductMaster getFromCompanyProductMaster(String companyId,String productId ) {

		try {
				List<CompanyProductMaster> qs=cpmRepo.findByCompanyIdAndProductIdOrderByAmendIdDesc(companyId,Integer.parseInt(productId));
				if(!qs.isEmpty())
			return qs.get(0);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;  
	}
	
	@Autowired
	private InsuranceCompanyMasterRepository insuranceRepo;
	
	public InsuranceCompanyMaster getFromInsuranceCompanyMaster(String companyId) {
		
		try {
			List<InsuranceCompanyMaster> ls = insuranceRepo.findByCompanyIdOrderByAmendIdDesc(companyId);
			if(!ls.isEmpty()) {
				return ls.get(0);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null; 
		}
		
	@Autowired
	private ListItemValueRepository listvRepo;
	
	public ListItemValue getFromListItemValue(String itemtype,String itemvalue, String companyId) { //PAYMENT_MODE
		try {
			ListItemValue l=listvRepo.findByItemTypeAndItemCodeAndStatusAndCompanyId(itemtype,itemvalue,"Y", companyId);			 
			return l;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Autowired
	private PaymentDetailRepository paymentDetailRepo;
	
	public PaymentDetail getpaymentDetail(String quoteNo) { //PAYMENT_MODE
		try {
			PaymentDetail p=paymentDetailRepo.findTopByQuoteNoOrderByEntryDateDesc(quoteNo);			 
			return p;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	//ltPayment

	@Autowired
	private PersonalInfoRepository personlRepo;
	public PersonalInfo getFromPresonalInfo(String companyId,String customerId) {
		try {
			List<PersonalInfo> p = personlRepo.findByCompanyIdAndCustomerId(companyId,customerId);
			if(!p.isEmpty()) {
				return p.get(0);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Autowired
	private MotorVehicleInfoRepository motorvehicleRepo;
	@Autowired
	private MotorDataDetailsRepository mddRepo;
	public MotorVehicleInfo getFromMotorVehicleInfo(String quoteNo,String vehicleId, String companyId) {
		try {
			MotorDataDetails mdd = mddRepo.findByQuoteNoAndVehicleId(quoteNo,vehicleId);
			MotorVehicleInfo m = motorvehicleRepo.findTop1ByResChassisNumberAndSavedFromAndCompanyIdOrderByEntryDateDesc(mdd.getChassisNumber(),"API", companyId );
			if(m==null ) 
				m = motorvehicleRepo.findTop1ByResChassisNumberAndSavedFromAndCompanyIdOrderByEntryDateDesc(mdd.getChassisNumber(),"WEB", companyId);
			return m;
		}catch (Exception e) {
			e.printStackTrace();
		}
		
 		return null;
	}
	@Autowired
	private ProductSectionMasterRepository sectionRepo;
	public Object getFromSectionMaster(String companyId, Integer productId, Integer sectionId) {
		 try {
			 List<ProductSectionMaster> p= sectionRepo.findByProductIdAndCompanyIdAndSectionIdAndStatusOrderByAmendIdDesc(productId, companyId,sectionId,"Y");
			 return p.get(0);
		 }catch (Exception e) {
			 e.printStackTrace();
		}
		return null;
	}
	
	@Autowired
	private EndtTypeMasterRepository endtRepo;
	public Object getFromEndtMaster(String companyId, Integer productId, Date effectiveDate, String endtTypeId) {
		 try {
			 Integer endtType=StringUtils.isBlank(endtTypeId)?0:Integer.parseInt(endtTypeId);	
			 List<EndtTypeMaster> p= endtRepo.findByCompanyIdAndProductIdAndEndtTypeIdAndStatusOrderByAmendIdDesc(companyId, productId, endtType,"Y"); //findByProductIdAndCompanyIdAndSectionIdAndStatusOrderByAmendIdDesc(productId, companyId,sectionId,"Y");
			 if(p!=null && p.size()>0)
				 return p.get(0);
		 }catch (Exception e) {
			 e.printStackTrace();
		}
		return null;
	}
	@Autowired
	private SectionDataDetailsRepository sectionDataRepo;
	
	public List<SectionDataDetails> getFromSectionDataDetails(String quoteNo, String riskId, String locationId, String sectionId) {
		try {
			List<SectionDataDetails> data=sectionDataRepo.findByQuoteNoAndRiskIdAndSectionIdAndLocationId(quoteNo,Integer.parseInt(riskId),sectionId,Integer.parseInt(locationId));
			return data;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	@Autowired
	private TiraTrackingDetailsRepository tiraTrackingRepo;
	public Object getFromTiraTrackingDetails(String quoteNo) {
		try {
			List<TiraTrackingDetails> data = tiraTrackingRepo.findByPolicyNoOrderByEntryDateDesc(quoteNo,Pageable.ofSize(10));
			return data;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public MotorDataDetails getFromMotorDataDetails(String quoteNo,String vehicleId) {
		MotorDataDetails mdd = mddRepo.findByQuoteNoAndVehicleId(quoteNo,vehicleId);
		return mdd;
	}
	public List<SectionDataDetails> getFromSectionDataDetails(String quoteNo) {
		 try {
			 List<SectionDataDetails> data=sectionDataRepo.findByQuoteNoOrderByRiskIdAsc(quoteNo);
			 return data;
		 }catch (Exception e) {
			 e.printStackTrace();
		}
		return null;
	}
	public List<PolicyCoverData> getFromPolicyCoverData(String quoteNo, Integer riskId, String locationId, String sectionId) {
		try {
			List<PolicyCoverData> qs=pcdRepo.findByQuoteNoAndVehicleIdAndLocationIdAndSectionId(quoteNo,riskId,Integer.parseInt(locationId),Integer.parseInt(sectionId));
			return qs;
		}catch (Exception e) { 
			e.printStackTrace();
		}
		return null;
	}

}
