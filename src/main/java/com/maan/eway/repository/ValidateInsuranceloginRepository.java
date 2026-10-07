package com.maan.eway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.maan.eway.bean.ValidateInsurancelogin;
import java.util.List;


public interface ValidateInsuranceloginRepository
		extends JpaRepository<ValidateInsurancelogin, Long>, JpaSpecificationExecutor<ValidateInsurancelogin> {
	
	ValidateInsurancelogin findByLoginUserId(String loginUserId);

	@Query("SELECT v FROM ValidateInsurancelogin v WHERE CURRENT_DATE BETWEEN FUNCTION('DATE', v.issueAt) AND FUNCTION('DATE', v.expires)")
	ValidateInsurancelogin findValidTokensToday();

}
