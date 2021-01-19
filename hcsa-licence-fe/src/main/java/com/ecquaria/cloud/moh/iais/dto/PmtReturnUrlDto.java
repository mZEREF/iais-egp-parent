package com.ecquaria.cloud.moh.iais.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author zixian
 * @date 2020/11/3 10:13
 * @description
 */
@Getter
@Setter
public class PmtReturnUrlDto implements Serializable {

    private static final long serialVersionUID = 7594813731835300579L;

    private String creditRetUrl;

    private String netsRetUrl;

    private String payNowRetUrl;

    private String otherRetUrl;

}
