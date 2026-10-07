package com.maan.eway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.eway.bean.EserviceDoctordetails;

public interface EserviceDoctordetailsRepository
		extends JpaRepository<EserviceDoctordetails, Integer>, JpaSpecificationExecutor<EserviceDoctordetails> {

	EserviceDoctordetails findByregNo(String regNo);

}
