package com.maan.eway.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.maan.eway.bean.EwayVehicleMakeModeMasterId;
import com.maan.eway.bean.EwayVehicleMakeModelMaster;

@Repository
public interface EwayVehicleMakeModelMasterDetailRepository extends JpaRepository<EwayVehicleMakeModelMaster,EwayVehicleMakeModeMasterId> , JpaSpecificationExecutor<EwayVehicleMakeModelMaster>{
	
	List<EwayVehicleMakeModelMaster> findByVehicleid(String string);

	List<EwayVehicleMakeModelMaster> findByModelId(Integer string);

	@Query(nativeQuery=true,value="SELECT model_id FROM `eway_motor_makemodel_master` WHERE company_id='100002' AND model_name_en=?1 AND STATUS='y' AND \r\n"
			+ "amend_id=(SELECT MAX(amend_id) FROM `eway_motor_makemodel_master` WHERE company_id='100002' AND model_name_en=?1 AND STATUS='y' )")
	Integer findModelIdWithMaxAmendId(String model);
	
	
	EwayVehicleMakeModelMaster findByCompanyId(String companyId);


}
