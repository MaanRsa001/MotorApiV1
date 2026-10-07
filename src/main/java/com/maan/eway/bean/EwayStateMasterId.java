package com.maan.eway.bean;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class EwayStateMasterId implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer stateId;
    private String stateShortCode;
    private String countryId;
    private String regionCode;
    private Integer amendId;
    private Integer cityId;
    private Integer suburbId;
}
