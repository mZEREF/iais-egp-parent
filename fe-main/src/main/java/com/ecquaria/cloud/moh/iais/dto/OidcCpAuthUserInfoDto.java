package com.ecquaria.cloud.moh.iais.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * OidcCpAuthUserInfoDto
 *
 * @author Jinhua
 * @date 2021/11/10 15:10
 */
@Getter
@Setter
public class OidcCpAuthUserInfoDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nricFin;
    private String uuid;
    private String cpAccType;
    private String cpUidFullName;
    private String isSPHolder;
    private String cpEntId;
    private String cpEntStatus;
    private String cpEntType;
}
