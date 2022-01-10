package com.ecquaria.cloud.moh.iais.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * OidcCpTokenReqDto
 *
 * @author Jinhua
 * @date 2022/1/6 16:22
 */
@Getter
@Setter
public class OidcCpTokenReqDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String code;
    private String nonce;
    private String clientId;
    private String redirectUri;
}
