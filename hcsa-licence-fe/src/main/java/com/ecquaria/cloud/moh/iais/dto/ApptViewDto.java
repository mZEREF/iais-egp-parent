package com.ecquaria.cloud.moh.iais.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * ApptViewDto
 *
 * @author junyu
 * @date 2020/6/18
 */
@Setter
@Getter
public class ApptViewDto implements Serializable {
    private static final long serialVersionUID = 3687194251231208193L;

    private String address;
    private String appId;
    private String appGrpId;
    private String applicationNo;
    private String applicationType;
    private String applicationStatus;
    private String appCorrId;
    private String licenseeId;
    private Date inspStartDate;
    private List<String> svcIds;
    private String fastTracking;
    private String viewCorrId;


}
