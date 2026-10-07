package com.maan.eway.common.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskDuplicateCheckReq {

	private String riskId ;
	
	private String value ;
	
	private String serialNoDesc ;
}
