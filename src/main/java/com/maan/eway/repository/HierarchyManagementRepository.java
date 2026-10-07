/**
 * @author : Ashok Kumar S 
 * @since  : 23-12-2024
 */
package com.maan.eway.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maan.eway.bean.HierarchyManagement;
import com.maan.eway.bean.HierarchyManagementId;




@Repository
public interface HierarchyManagementRepository extends JpaRepository<HierarchyManagement, HierarchyManagementId>{
	
	public List<HierarchyManagement> findAllByCompanyIdAndProductId(Integer companyId, Integer productId);

}
