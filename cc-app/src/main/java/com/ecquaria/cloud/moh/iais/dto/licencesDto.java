package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2019-10-18 09:39
 **/
@Getter
@Setter
public class licencesDto extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 7148727610648699360L;
    private String id;
    private String licenceId;
    private String svcName;
    private String startDate;
    private String expiryDate;
    private String baseLicId;
    private String isGrpLic;
    private String organizationId;
    private String status;
}
