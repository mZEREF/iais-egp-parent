package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionReportDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.service.InsRepService;
import org.springframework.stereotype.Service;

/**
 * @author weilu
 * date 2019/11/20 16:11
 */
@Service
public class InsRepServiceImpl implements InsRepService {
    @Override
    public InspectionReportDto getInsRepDto(String appNo) {
        InspectionReportDto inspectionReportDto = RestApiUtil.postGetObject("iais-application:8883/iais-inspection/report", appNo, InspectionReportDto.class);
        String licenceId = "92B33E39-B7FA-428A-901A-3AC7F8886F8C";
            InspectionReportDto insReportDto = RestApiUtil.postGetObject("iais-hcsa-licence:8882/hcsa-lic-licensee/detection-lic-licensee", licenceId, InspectionReportDto.class);
            inspectionReportDto.setLicenceNo(insReportDto.getLicenceNo());
            inspectionReportDto.setLicenseeName(insReportDto.getLicenseeName());
        return inspectionReportDto;

    }
}
