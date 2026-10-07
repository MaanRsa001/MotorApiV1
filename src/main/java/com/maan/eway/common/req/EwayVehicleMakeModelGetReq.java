package com.maan.eway.common.req;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
 
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EwayVehicleMakeModelGetReq {
	
	@JsonProperty("MakeId")
	private String makeId;
	@JsonProperty("ModelId")
	private String modelId;
	@JsonProperty("VehicleId")
	private String vehicleId;
	@JsonProperty("InsuranceId")
	private String companyId;
	@JsonProperty("ProductId")
	private String productId;
	
}
