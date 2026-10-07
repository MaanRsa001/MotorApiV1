package com.maan.eway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.eway.bean.DMVICVehicleInfoDetails;
import java.util.List;


public interface DMVICVehicleInfoDetailsRepository
		extends JpaRepository<DMVICVehicleInfoDetails, Integer>, JpaSpecificationExecutor<DMVICVehicleInfoDetails> {

	//List<DMVICVehicleInfoDetails> findByRegistrationNumber(String registrationNumber);
	
	DMVICVehicleInfoDetails findByRegistrationNumber(String registrationNumber);
}
