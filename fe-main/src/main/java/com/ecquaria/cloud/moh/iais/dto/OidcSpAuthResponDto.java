package com.ecquaria.cloud.moh.iais.dto;

import java.io.Serializable;
import java.util.List;
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
public class OidcSpAuthResponDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String statusCode;
    private String authStatus;
    private String authMethod;
    private List<String> authMethodReferences;
    private String authContextReference;
    private OidcSpAuthUserInfoDto userInfo;
    private String expiresIn;
    private String accessToken;
}
