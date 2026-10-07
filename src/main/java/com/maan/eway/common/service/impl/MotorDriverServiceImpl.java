package com.maan.eway.common.service.impl;


import java.util.ArrayList;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.eway.common.req.MotorDriverSaveReq;
import com.maan.eway.common.req.OtherVehicleInfoReq;

@Service
public class MotorDriverServiceImpl {

private Logger log = LogManager.getLogger(EserviceMotorDetailsServiceImpl.class);
	
@Autowired
private SanlamMotorDriverDetails sanlamMotDriDet;

@Autowired
private BurkinoMotorDriverDetails burkinoMotDriDet;

@Autowired
private AngolaMotorDriverDetails angolaMotDriDet;

@Autowired
private TanzaniaMotorDriverDetails tanzaniaMotDriDet;

@Autowired
private UgandaMotorDriverDetails ugandaMotDriDet;

@Autowired
private OromiaMotorDriverDetails oromiaMotDriDet;

@Autowired
private MadisonMotorDriverDetails madisonMotDriDet;

@Autowired
private KenyaMotorDriverDetails kenyaMotDriDet;

@Autowired
private EagleMotorDriverDetails eagleMotDriDet;

@Autowired
private PhoenixMotorDriverDetails phoenixMotDriDet;

public List<String> validateOtherVehicleInfo(OtherVehicleInfoReq ss) {
	List<String> error = new ArrayList<String>();
	
	try {
	if(StringUtils.isBlank(ss.getCompanyId()))
	{
		error.add("1096");	//companyid
	}
    if(ss!=null)

    {
    
		/*
		 * if(StringUtils.isBlank(ss.getVehicleId())) { error.add("2236"); }
		 */
			/*
			 * if(StringUtils.isBlank(ss.getSeriesNo())) { error.add("2235"); }
			 */
			/*
			 * if(StringUtils.isBlank(ss.getPlateColorId().)) { error.add("2237"); }
			 */
			/*
			 * if(StringUtils.isBlank(ss.getPlateType())) { error.add("2238"); }
			 * if(StringUtils.isBlank(String.valueOf(ss.getNoCylinder())) ) {
			 * error.add("2239"); } if(StringUtils.isBlank(String.valueOf(ss.getNoDoors())))
			 * { error.add("2240"); }
			 */
    		if(!StringUtils.isBlank(ss.getSeriesNo()))
    		{
    			if(!validateSeriesno(ss.getSeriesNo()))
    			{
    				error.add("2234");
    			
    			}
    		}
    		
     
    }
	}catch(Exception Problem)
	{
		System.out.println("********* Exception Occured in other vehicle save *****************");
		System.out.print(Problem.getMessage());
		Problem.printStackTrace();
	
		return null;	
	}
	return error;
}

public boolean validateSeriesno(String seriesno)
{
	try {
String regex ="(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[-]).{0,15}$";
if(seriesno.matches(regex))
{
return true;
}
	}catch(Exception ex)
	{
		System.out.println("**************Exception in validateSeriesno *****************");
       ex.getMessage();
		return false;
	}
return false;	
}
	public List<String> validateDriverDetails(List<MotorDriverSaveReq> req) {
		List<String> error = new ArrayList<String>();
		try {
			if(req!=null) {
				if("100004".equalsIgnoreCase(req.get(0).getInsuranceId()))	{
					error=madisonMotDriDet.validateDriverDetails(req);
				}else if("100018".equalsIgnoreCase(req.get(0).getInsuranceId()))	{
					error=oromiaMotDriDet.validateDriverDetails(req);
				}else if("100019".equalsIgnoreCase(req.get(0).getInsuranceId()))	{
					error=ugandaMotDriDet.validateDriverDetails(req);
				}else if("100020".equalsIgnoreCase(req.get(0).getInsuranceId()))	{
					error=kenyaMotDriDet.validateDriverDetails(req);
				}else if("100027".equalsIgnoreCase(req.get(0).getInsuranceId()))	{
					error=angolaMotDriDet.validateDriverDetails(req);
				}else if("100028".equalsIgnoreCase(req.get(0).getInsuranceId()))	{
					error=eagleMotDriDet.validateDriverDetails(req);
				}else if("100040".equalsIgnoreCase(req.get(0).getInsuranceId())){
					error=sanlamMotDriDet.validateDriverDetails(req);
				}else if("100042".equalsIgnoreCase(req.get(0).getInsuranceId()))	{
					error=burkinoMotDriDet.validateDriverDetails(req);
				}else if("100046".equalsIgnoreCase(req.get(0).getInsuranceId()))	{
					error=phoenixMotDriDet.validateDriverDetails(req);
				}else {
					error=tanzaniaMotDriDet.validateDriverDetails(req);
				}
			}
				
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			error.add("1951");
		}
		return error;
	}
}
