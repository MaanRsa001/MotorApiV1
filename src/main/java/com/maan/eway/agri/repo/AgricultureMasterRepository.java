package com.maan.eway.agri.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.maan.eway.agri.entity.AgricultureMaster;
import com.maan.eway.agri.entity.AgricultureMasterId;
import com.maan.eway.agri.res.CropDropdownRes;

@Repository
public interface AgricultureMasterRepository extends JpaRepository<AgricultureMaster, AgricultureMasterId> {

	List<AgricultureMaster> findByCompanyIdAndProductIdOrderByAmendIdDesc(Integer companyId, Integer productId);

	List<AgricultureMaster> findByCompanyIdAndProductIdAndSnoOrderByAmendIdDesc(Integer companyId, Integer productId,
			Integer sNo);

	List<AgricultureMaster> findByAez(Integer aez);

	@Query("SELECT MAX(a.sno) FROM AgricultureMaster a WHERE a.companyId = :companyId AND a.productId = :productId")
	Integer findMaxSnoByCompanyIdAndProductId(@Param("companyId") Integer companyId, @Param("productId") Integer productId);
	

	@Query(value = "SELECT DISTINCT crop_id, crop_desc FROM agriculture_master", nativeQuery = true)
	List<Object[]> findDistinctCropIdAndDesc();



}
