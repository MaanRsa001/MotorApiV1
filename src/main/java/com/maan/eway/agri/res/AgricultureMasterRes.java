package com.maan.eway.agri.res;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgricultureMasterRes {

	private Integer sNo;
	private Integer companyId;
	private Integer productId;
	private Integer amendId;
	private Integer provinceId;
	private String provinceDesc;
	private Integer districtId;
	private String districtDesc;
	private Integer aez;
	private Integer cropId;
	private String cropDesc;
	private Double perHaCost;
	private Integer sectionId;
	private String coreAppCode;
	private String status;
	private Date entryDate;
	private Date effectiveDateStart;
	private Date effectiveDateEnd;
	private String remarks;
	private Integer yieldPercentage;
}
