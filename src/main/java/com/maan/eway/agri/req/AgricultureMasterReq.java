package com.maan.eway.agri.req;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgricultureMasterReq {

	private Integer sno;
	private Integer companyId;
	private Integer productId;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date effectiveDateStart;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date effectiveDateEnd;
    private String remarks;
    private Integer yieldPercentage;
}
