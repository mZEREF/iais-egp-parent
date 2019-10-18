package com.ecquaria.cloud.moh.iais.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * SubmissionDetailDto
 *
 * @author suocheng
 * @date 10/17/2019
 */
@Getter@Setter
public class SubmissionDetailDto implements Serializable {
    private static final long serialVersionUID = 7384139627690423474L;

    private String appGroupNo;
    private String appNo;
    private String appType;
    private String serviceType;
    private Date submitDate;
    private String currentStatus;

    private String submitBy;

}
