package com.ecquaria.cloud.moh.iais.dto;

/**
 * TxnReqDto
 *
 * @author junyu
 * @date 2020/9/17
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class SoapiS2S<T> {
    private String ss;
    private  Msg msg;

    public SoapiS2S() {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Msg {

        private String netsMid;
        private String tid;
        private String submissionMode;
        private String txnAmount;
        private String merchantTxnRef;
        private String merchantTxnDtm;
        private String paymentType;
        private String currencyCode;
        private String paymentMode;
        private String merchantTimeZone;
        private String b2sTxnEndURL;
        private String b2sTxnEndURLParam;
        private String s2sTxnEndURL;
        private String s2sTxnEndURLParam;
        private String clientType;
        private String supMsg;
        private String netsMidIndicator;
        private String ipAddress;
        private String language;

        public String getNetsMid() {
            return netsMid;
        }

        public void setNetsMid(String netsMid) {
            this.netsMid = netsMid;
        }

        public String getTid() {
            return tid;
        }

        public void setTid(String tid) {
            this.tid = tid;
        }

        public String getSubmissionMode() {
            return submissionMode;
        }

        public void setSubmissionMode(String submissionMode) {
            this.submissionMode = submissionMode;
        }

        public String getTxnAmount() {
            return txnAmount;
        }

        public void setTxnAmount(String txnAmount) {
            this.txnAmount = txnAmount;
        }

        public String getMerchantTxnRef() {
            return merchantTxnRef;
        }

        public void setMerchantTxnRef(String merchantTxnRef) {
            this.merchantTxnRef = merchantTxnRef;
        }

        public String getMerchantTxnDtm() {
            return merchantTxnDtm;
        }

        public void setMerchantTxnDtm(String merchantTxnDtm) {
            this.merchantTxnDtm = merchantTxnDtm;
        }

        public String getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(String paymentType) {
            this.paymentType = paymentType;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public String getPaymentMode() {
            return paymentMode;
        }

        public void setPaymentMode(String paymentMode) {
            this.paymentMode = paymentMode;
        }

        public String getMerchantTimeZone() {
            return merchantTimeZone;
        }

        public void setMerchantTimeZone(String merchantTimeZone) {
            this.merchantTimeZone = merchantTimeZone;
        }

        public String getB2sTxnEndURL() {
            return b2sTxnEndURL;
        }

        public void setB2sTxnEndURL(String b2sTxnEndURL) {
            this.b2sTxnEndURL = b2sTxnEndURL;
        }

        public String getB2sTxnEndURLParam() {
            return b2sTxnEndURLParam;
        }

        public void setB2sTxnEndURLParam(String b2sTxnEndURLParam) {
            this.b2sTxnEndURLParam = b2sTxnEndURLParam;
        }

        public String getS2sTxnEndURL() {
            return s2sTxnEndURL;
        }

        public void setS2sTxnEndURL(String s2sTxnEndURL) {
            this.s2sTxnEndURL = s2sTxnEndURL;
        }

        public String getS2sTxnEndURLParam() {
            return s2sTxnEndURLParam;
        }

        public void setS2sTxnEndURLParam(String s2sTxnEndURLParam) {
            this.s2sTxnEndURLParam = s2sTxnEndURLParam;
        }

        public String getClientType() {
            return clientType;
        }

        public void setClientType(String clientType) {
            this.clientType = clientType;
        }

        public String getSupMsg() {
            return supMsg;
        }

        public void setSupMsg(String supMsg) {
            this.supMsg = supMsg;
        }

        public String getNetsMidIndicator() {
            return netsMidIndicator;
        }

        public void setNetsMidIndicator(String netsMidIndicator) {
            this.netsMidIndicator = netsMidIndicator;
        }

        public String getIpAddress() {
            return ipAddress;
        }

        public void setIpAddress(String ipAddress) {
            this.ipAddress = ipAddress;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }
    }

}
