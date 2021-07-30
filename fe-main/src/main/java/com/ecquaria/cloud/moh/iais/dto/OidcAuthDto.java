package com.ecquaria.cloud.moh.iais.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * OidcAuthDto
 *
 * @author Jinhua
 * @date 2021/7/27 18:24
 */
@Getter
@Setter
public class OidcAuthDto implements Serializable {
    private static final long serialVersionUID = 1287441721695125465L;
    private String code;
    private String nonce;
    private String clientId;
    private String redirectUri;
    private OidcAuthZErrorDto authZError;
}
