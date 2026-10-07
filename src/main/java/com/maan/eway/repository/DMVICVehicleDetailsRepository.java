package com.maan.eway.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.maan.eway.bean.DMVICVehicleDetails;

public interface DMVICVehicleDetailsRepository extends JpaRepository<DMVICVehicleDetails,Long > , JpaSpecificationExecutor<DMVICVehicleDetails>{
	
	  @Query("SELECT v FROM DMVICVehicleDetails v " +
	           "WHERE v.registrationNumber = :registrationNumber " +
	           "AND :policyDate BETWEEN v.policyStartDate AND v.coverEndDate")
	    List<DMVICVehicleDetails> findByRegistrationNumberAndPolicyDate(
	            @Param("registrationNumber") String registrationNumber,
	            @Param("policyDate") Date policyDate);
	

}
