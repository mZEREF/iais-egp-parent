package com.ecquaria.cloud.moh.iais.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * OidcAuthResponDto
 *
 * @author Jinhua
 * @date 2021/7/29 9:37
 */
@Getter
@Setter
public class OidcAuthResponDto implements Serializable {
    private static final long serialVersionUID = 4500842315163782753L;
    private String statusCode;
    private String authMethod;
    private OidcAuthUserInfoDto userInfo;
    private String expiresIn;
    private String accessToken;
}
