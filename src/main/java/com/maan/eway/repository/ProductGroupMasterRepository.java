package com.maan.eway.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.eway.bean.ProductGroupMaster;
import com.maan.eway.bean.ProductGroupMasterId;

public interface ProductGroupMasterRepository extends JpaRepository<ProductGroupMaster,ProductGroupMasterId > , JpaSpecificationExecutor<ProductGroupMaster> {

	List<ProductGroupMaster> findByCompanyIdAndProductId(String companyId, Integer valueOf);



}
