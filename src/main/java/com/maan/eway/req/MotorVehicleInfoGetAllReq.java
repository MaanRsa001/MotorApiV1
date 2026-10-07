package com.maan.eway.req;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data

public class MotorVehicleInfoGetAllReq {
	
    @JsonFormat(pattern = "dd/MM/yyy")
    @JsonProperty("EffectiveDateStart")
    private Date       effectiveDateStart ;
    @JsonFormat(pattern = "dd/MM/yyy")
    @JsonProperty("EffectiveDateEnd")
    private Date       effectiveDateEnd ;
    @JsonProperty("Limit")
    private String       Limit ;
    @JsonProperty("Offset")
    private String       Offset ;
	
}
