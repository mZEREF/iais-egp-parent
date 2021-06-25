package com.ecquaria.cloud.moh.iais.service.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * SoapiS2S
 *
 * @author junyu
 * @date 2020/12/15
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class SoapiS2S {
    private SoapiS2S.Msg msg;
    private String ss;
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Msg {

        private String netsMid;
        private String merchantTxnRef;
        private String netsMidIndicator;
    }
}
