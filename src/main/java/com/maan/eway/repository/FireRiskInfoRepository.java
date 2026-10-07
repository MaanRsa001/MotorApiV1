package com.maan.eway.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.maan.eway.bean.FireRiskInfo;
import com.maan.eway.bean.FireRiskInfoId;

@Repository
public interface FireRiskInfoRepository extends JpaRepository<FireRiskInfo, FireRiskInfoId> {

    List<FireRiskInfo> findByRequestReferenceNo(String requestReferenceNo);

}
