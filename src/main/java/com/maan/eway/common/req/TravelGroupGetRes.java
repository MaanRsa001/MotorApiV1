package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TravelGroupGetRes {


	@JsonProperty("GroupId")
    private String    groupId     ;
//	@JsonProperty("GroupDesc")
//    private String    groupDesc     ;
	@JsonProperty("GroupMembers")
    private String    groupMembers    ;
	
	@JsonProperty("TravelId")
    private String    travelId;
	
	@JsonProperty("RiskId")
    private Integer    riskId;
//	
//	@JsonProperty("RatingRelationId")
//    private String    ratingRelationId     ;
//	
//
//	@JsonProperty("RatingRelationDesc")
//    private String    ratingRelationDesc     ;
//	
//	@JsonProperty("NickName")
//    private String    nickName;
}
