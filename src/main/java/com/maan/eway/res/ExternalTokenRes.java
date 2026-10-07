package com.maan.eway.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ExternalTokenRes {

    @JsonProperty("id")
    private String       id ;
    @JsonProperty("firstName")
    private String       firstName ;
    @JsonProperty("lastName")
    private String       lastName ;
    @JsonProperty("username")
    private String       username ;
    @JsonProperty("token")
    private String       token ;
    
}
