package com.ecquaria.cloud.moh.iais.dto;

/*
 *author: yichen
 *date time:9/25/2019 2:05 PM
 *description:
 */

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class HcsaServiceDto implements Serializable {
    private static final long serialVersionUID = -226486206932932737L;

    private String id;

    private String svcName;

    private String svcCode;

    private String svcDesc;

    private String svcDisplayDesc;

    private String svcType;

    private String effectiveDate;

    private Date endDate;

    private String status;

    private String categoryId;

}
