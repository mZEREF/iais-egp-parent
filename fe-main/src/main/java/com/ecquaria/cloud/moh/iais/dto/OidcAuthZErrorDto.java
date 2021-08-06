package com.ecquaria.cloud.moh.iais.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * OidcAuthZErrorDto
 *
 * @author Jinhua
 * @date 2021/7/27 18:27
 */
@Getter
@Setter
public class OidcAuthZErrorDto implements Serializable {
    private static final long serialVersionUID = 6733861975756729444L;
    private String error;
    private String errorDescription;
}
