/**
 * @author : Ashok Kumar S 
 * @since  : 09-01-2025
 */
package com.maan.eway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maan.eway.bean.WorkflowFactorRateRequestDetail;
import com.maan.eway.bean.WorkflowFactorRateRequestDetailPK;



@Repository
public interface WorkflowFactorRateRequestDetailRepository extends 
			JpaRepository<WorkflowFactorRateRequestDetail, WorkflowFactorRateRequestDetailPK>{
	
}
