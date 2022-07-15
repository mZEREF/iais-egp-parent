package com.ecquaria.cloud.moh.iais.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: HuangKunRoomDto
 * @author: haungkun
 * @date: 2022/7/8 16:44
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class HuangKunPersonDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String displayName;

    private String salutation;

    private String idType;

    private String idNo;

    private String designtion;

    private String mobileNo;

    private String officeTelNo;

    private String emailAddr;

    private String roomId;

    private String division;

    private String branchUnit;

    private String firstName;

    private String lastName;

    private Date effectiveFrom;

    private Date effectiveTo;

    private String intreInputOrgName;

    private String otherDesignation;

}
