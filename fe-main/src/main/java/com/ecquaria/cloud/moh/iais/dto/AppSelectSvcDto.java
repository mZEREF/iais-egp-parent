package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class AppSelectSvcDto implements Serializable {

    private static final long serialVersionUID = -5042358390403799455L;


//    private List<String> baseSvcIds;
//
//    private List<String> specifiedSvcIds;

    //sort
    private List<HcsaServiceDto> baseSvcDtoList;
    //sort
    private List<HcsaServiceDto> speSvcDtoList;

    private boolean align;

    private boolean chooseBaseSvc;

    private String alignLicPremId;

    //for back Position
    private boolean basePage;

    private boolean alignPage;

    private boolean licPage;

    private boolean newLicensee;

    private boolean initPagHandler;
}
