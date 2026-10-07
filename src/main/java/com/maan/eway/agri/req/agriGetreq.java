package com.maan.eway.agri.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class agriGetreq {

	private Integer companyId;
	private Integer productId;
	private Integer sNo;
}
