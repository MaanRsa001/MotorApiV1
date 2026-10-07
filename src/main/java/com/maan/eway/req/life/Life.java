package com.maan.eway.req.life;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class Life {
	@JsonProperty("Customer") 
    private Customer customer;
    @JsonProperty("QuoteDetail") 
    private QuoteDetail quoteDetail;
    @JsonProperty("LifeDetail") 
    private LifeDetail lifeDetail;
    @JsonProperty("UwQuestions") 
    private ArrayList<UwQuestionsDetailsSaveReq> uwQuestions;
}
