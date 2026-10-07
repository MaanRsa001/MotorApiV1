package com.maan.eway.agri.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class cropDropDownReq {

	private String companyId;
	private String itemType;
}
