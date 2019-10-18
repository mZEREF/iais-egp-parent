package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2019-10-15 17:53
 **/
@Getter
@Setter
public class ApplicationDto extends BaseEntity {
    private static final long serialVersionUID = -7357543222838980801L;
    private String id;

    private String applicationNo;

    private String applicationType;

    private String svcName;

    private String licenceId;

    private String originLicenceId;

    private String baseAppId;

    private String baseLicenceId;

    private Integer needInspection;

    private String status;

    private String appGrpId;
}
