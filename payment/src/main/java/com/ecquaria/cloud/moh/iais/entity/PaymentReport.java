package com.ecquaria.cloud.moh.iais.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * PaymentReport
 *
 * @author junyu
 * @date 2020/7/20
 */
@Getter
@Setter
public class PaymentReport {
    private String internalId;
    private String merchantId;
    private String reportType;
    private String startDate;
    private String endDate;
    private String fileName;
    private String downloadUrl;
    private String downloadStatus;
    private String intranetTransferStatus;


}
