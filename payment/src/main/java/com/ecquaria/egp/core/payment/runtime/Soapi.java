package com.ecquaria.egp.core.payment.runtime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * Soapi
 *
 * @author junyu
 * @date 2020/11/6
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Soapi {
    private Soapi.Msg msg;
    private String ss;
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Msg {

        private String netsMid;
        private String merchantTxnRef;
        private String netsMidIndicator;
        private String netsTxnRef;
        private String paymentMode;
        private String submissionMode;
        private String currencyCode;
        private String merchantTxnDtm;
        private String merchantTimeZone;
        private String paymentType;
        private String clientType;
        private String maskPan;
        private String bankAuthId;
        private String stageRespCode;
        private String txnRand;
        private String actionCode;
        private String netsTxnDtm;
        private String netsTimeZone;
        private String netsTxnStatus;
        private String netsTxnMsg;
        private String netsAmountDeducted;
    }
}
