package com.ecquaria.cloud.moh.iais.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author ZiXian
 */
@Getter
@Setter
public class AjaxResDto implements Serializable {

    private String resCode;

    private String type;

    private Object resultJson;
}
