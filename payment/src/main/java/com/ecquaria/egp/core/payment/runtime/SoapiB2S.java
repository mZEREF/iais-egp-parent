package com.ecquaria.egp.core.payment.runtime;

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
public class SoapiB2S {
    private SoapiB2S.Msg msg;
    private String ss;
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Msg {

        private String netsMid;
        private String tid;
        private String merchantTxnRef;
        private String netsMidIndicator;
        private String paymentMode;
        private String submissionMode;
        private String currencyCode;
        private String merchantTxnDtm;
        private String merchantTimeZone;
        private String paymentType;
        private String clientType;
        private String language;
        private String txnAmount;
        private String ipAddress;
        private String supMsg;
        private String b2sTxnEndURL;
        private String b2sTxnEndURLParam;
        private String s2sTxnEndURL;
        private String s2sTxnEndURLParam;
    }
}
