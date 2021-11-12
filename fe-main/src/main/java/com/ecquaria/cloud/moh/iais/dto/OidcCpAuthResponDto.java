package com.ecquaria.cloud.moh.iais.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * OidcCpAuthResponDto
 *
 * @author Jinhua
 * @date 2021/11/10 15:13
 */
@Getter
@Setter
public class OidcCpAuthResponDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String statusCode;
    private String authStatus;
    private String authMethod;
    private OidcCpAuthUserInfoDto userInfo;
    private String expiresIn;
    private String accessToken;
    private String authInfo;
    private String tpAuthInfo;
}
