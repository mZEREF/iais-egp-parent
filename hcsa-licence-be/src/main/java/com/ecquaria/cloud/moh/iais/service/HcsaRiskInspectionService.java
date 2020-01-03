package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskInspectionMatrixDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.InspectionShowDto;

/**
 * @Author: jiahao
 * @Date: 2020/1/2 13:45
 */
public interface HcsaRiskInspectionService {
    InspectionShowDto getInspectionShowDto();
    void getOneFinDto(HcsaRiskInspectionMatrixDto fin, String caleftmod, String calefthigh, String carightlow, String carightmod, String caStartDate, String caEndDate, String mileftmod, String milefthigh, String mirightlow, String mirightmod, String miStartDate, String miEndDate, String mjleftmod, String mjlefthigh, String mjrightlow, String mjrightmod, String mjStartDate, String mjEndDate);
}
