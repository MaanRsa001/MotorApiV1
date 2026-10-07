package com.maan.eway.common.req;
 
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TravelGroupInsertReq {

	@JsonProperty("GroupId")
    private String    groupId     ;
	@JsonProperty("GroupDesc")
    private String    groupDesc     ;
	@JsonProperty("GroupMembers")
    private String    groupMembers    ;
	
//	@JsonProperty("GroupAgeFrom")
//    private Integer    groupAgeFrom    ;
//	
//	
//	@JsonProperty("GroupAgeTo")
//    private Integer    groupAgeTo   ;
	
	@JsonProperty("RiskId")
    private Integer    riskId     ;
	

//	@JsonProperty("RatingRelationId")
//	private String ratingRelationId;
//	
//	@JsonProperty("NickName")
//	   private String nickName;

	
	
}
