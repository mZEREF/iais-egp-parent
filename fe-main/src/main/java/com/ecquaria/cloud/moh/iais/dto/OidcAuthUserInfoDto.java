package com.ecquaria.cloud.moh.iais.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * OidcAuthUserInfoDto
 *
 * @author Jinhua
 * @date 2021/7/29 9:58
 */
@Getter
@Setter
public class OidcAuthUserInfoDto implements Serializable {
    private static final long serialVersionUID = 8963372401101429297L;
    private String nricFin;
    private String uuid;
}
