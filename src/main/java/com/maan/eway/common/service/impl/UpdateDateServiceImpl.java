package com.maan.eway.common.service.impl;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.maan.eway.common.req.updateDateReq;
import com.maan.eway.common.res.CommonRes;
import com.maan.eway.common.service.UpdateDateService;
@Service
public class UpdateDateServiceImpl implements UpdateDateService {

	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	@Override
	public CommonRes updateDateSection(updateDateReq req) {
		CommonRes res = new CommonRes();
		try {

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

			LocalDate startDate = LocalDate.parse(req.getStartDate(), formatter);
			LocalDate endDate   = LocalDate.parse(req.getEndDate(), formatter);

			LocalDateTime start = startDate.atStartOfDay();
			LocalDateTime end   = endDate.atTime(23, 59, 59);

			Timestamp startTs = Timestamp.valueOf(start);
			Timestamp endTs   = Timestamp.valueOf(end);

			String trace = "";
			
			int updatedTables = 0;

			//  policy_cover_data
			Integer count1 = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM policy_cover_data WHERE REQUEST_REFERENCE_NO=?",Integer.class, req.getRequestReferenceNo());

			if (count1 != null && count1 > 0) {
				jdbcTemplate.update("UPDATE policy_cover_data SET cover_period_from=?, cover_period_to=? WHERE REQUEST_REFERENCE_NO=?", startTs,endTs, req.getRequestReferenceNo());
				updatedTables++;
				trace=trace +"policy_cover_data";
				System.out.println("UPDATE policy_cover_data SET cover_period_from=?, cover_period_to=? WHERE REQUEST_REFERENCE_NO=?");
			}

			// 2 home_position_master
			Integer count2 = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM home_position_master WHERE REQUEST_REFERENCE_NO=?",Integer.class,  req.getRequestReferenceNo());

			if (count2 != null && count2 > 0) {
				jdbcTemplate.update("UPDATE home_position_master SET INCEPTION_DATE=?, EXPIRY_DATE=? WHERE REQUEST_REFERENCE_NO=?",startTs, endTs,  req.getRequestReferenceNo());
				updatedTables++;
				trace=trace +"home_position_master";
				System.out.println("UPDATE home_position_master SET INCEPTION_DATE=?, EXPIRY_DATE=? WHERE REQUEST_REFERENCE_NO=?");
			}

			// 3️ eservice_building_details
			Integer count3 = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM eservice_building_details WHERE REQUEST_REFERENCE_NO=?", Integer.class,  req.getRequestReferenceNo());

			if (count3 != null && count3 > 0) {
				jdbcTemplate.update("UPDATE eservice_building_details SET policy_start_date=?, policy_end_date=? WHERE REQUEST_REFERENCE_NO=?",startTs, endTs,  req.getRequestReferenceNo());
				updatedTables++;
				trace=trace +"eservice_building_details";
				System.out.println("UPDATE eservice_building_details SET policy_start_date=?, policy_end_date=? WHERE REQUEST_REFERENCE_NO=?");
			}

			// 4️ building_risk_details
			Integer count4 = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM building_risk_details WHERE REQUEST_REFERENCE_NO=?",Integer.class,  req.getRequestReferenceNo());

			if (count4 != null && count4 > 0) {
				jdbcTemplate.update("UPDATE building_risk_details SET policy_start_date=?, policy_end_date=? WHERE REQUEST_REFERENCE_NO=?",startTs, endTs,  req.getRequestReferenceNo());
				updatedTables++;
				trace=trace +"building_risk_details";
				System.out.println("UPDATE building_risk_details SET policy_start_date=?, policy_end_date=? WHERE REQUEST_REFERENCE_NO=?");
			}

			Integer count5 = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM eservice_common_details WHERE REQUEST_REFERENCE_NO=?", Integer.class,  req.getRequestReferenceNo());

	        if (count5 != null && count5 > 0) {
	            jdbcTemplate.update("UPDATE eservice_common_details SET POLICY_START_DATE=?, POLICY_END_DATE=? WHERE REQUEST_REFERENCE_NO=?",startTs, endTs,  req.getRequestReferenceNo());
	            updatedTables++;
	            trace=trace +"eservice_common_details";
	            System.out.println("UPDATE eservice_common_details SET POLICY_START_DATE=?, POLICY_END_DATE=? WHERE REQUEST_REFERENCE_NO=?");
	        }

	        // ⭐ 6 common_data_details  (NEW)
	        Integer count6 = jdbcTemplate.queryForObject( "SELECT COUNT(*) FROM common_data_details WHERE REQUEST_REFERENCE_NO=?", Integer.class,  req.getRequestReferenceNo());

	        if (count6 != null && count6 > 0) {
	            jdbcTemplate.update( "UPDATE common_data_details SET POLICY_START_DATE=?, POLICY_END_DATE=? WHERE REQUEST_REFERENCE_NO=?",startTs, endTs,  req.getRequestReferenceNo());
	            updatedTables++;
	            trace=trace +"common_data_details";
	            System.out.println("UPDATE common_data_details SET POLICY_START_DATE=?, POLICY_END_DATE=? WHERE REQUEST_REFERENCE_NO=?");
	        }
	        
	        Integer count7 = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM policy_cover_data WHERE REQUEST_REFERENCE_NO=?",Integer.class, req.getRequestReferenceNo());

			if (count7 != null && count1 > 0) {
				jdbcTemplate.update("UPDATE factor_rate_request_details SET COVER_PERIOD_FROM=?, COVER_PERIOD_TO=? WHERE REQUEST_REFERENCE_NO=?", startTs,endTs, req.getRequestReferenceNo());
				updatedTables++;
				trace=trace +"factor_rate_request_details";
				System.out.println("UPDATE factor_rate_request_details SET cover_period_from=?, cover_period_to=? WHERE REQUEST_REFERENCE_NO=?");
			}
			
			// If no table was updated
			if (updatedTables == 0) {
				res.setMessage("Quote No not found in any table: " +  req.getRequestReferenceNo());
				res.setErroCode(1);
				res.setIsError(true);
				return res;
			}

			res.setMessage("Success. Updated " + updatedTables + " table(s)." +trace );
			res.setErroCode(0);
			res.setIsError(false);
			return res;

		} catch (Exception e) {
			e.printStackTrace();
			res.setMessage("Failed" + e.getMessage());
			res.setErroCode(0);
			res.setIsError(true);
			return res;
		}
	}
}


