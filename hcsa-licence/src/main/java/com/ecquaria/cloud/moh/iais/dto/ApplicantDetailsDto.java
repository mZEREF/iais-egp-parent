package com.ecquaria.cloud.moh.iais.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * ApplicantDetailsDto
 *
 * @author suocheng
 * @date 10/17/2019
 */
@Getter
@Setter
public class ApplicantDetailsDto implements Serializable {
    private static final long serialVersionUID = 7460290813480106103L;

    private String iserId;
    private String hciId;
    private String hciName;
    private String hciAddress;
    private String hciTelephone;
    private String hciFax;

}
