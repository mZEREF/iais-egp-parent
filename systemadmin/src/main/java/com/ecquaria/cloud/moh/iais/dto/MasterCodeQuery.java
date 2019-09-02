package com.ecquaria.cloud.moh.iais.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2019-08-30 17:51
 **/
@Getter
@Setter
public class MasterCodeQuery implements Serializable {


    private static final long serialVersionUID = -1546053869121735254L;
    private Integer masterCodeId;
    private String rowguid;
    private String masterCodeKey;
    private int  codeCategory;
    private String codeValue;
    private String codeDescription;
    private String filterValue;
    private float sequence;
    private String remarks;
    private int status;
    private Date effectiveFrom;
    private Date effectiveTo;
    private int version;
}
