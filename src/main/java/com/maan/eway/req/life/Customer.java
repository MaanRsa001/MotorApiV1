package com.maan.eway.req.life;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
@Data
public class Customer {
	 @JsonProperty("CustomerId") 
	 @NotEmpty(message = "CustomerId is mandatory")
	    private ArrayList<String> customerId;
}
