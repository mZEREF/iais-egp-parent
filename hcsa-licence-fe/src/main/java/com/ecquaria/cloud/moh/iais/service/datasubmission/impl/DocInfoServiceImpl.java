package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DoctorInformationDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.client.DoctorInfoClient;
import com.ecquaria.cloud.moh.iais.service.datasubmission.DocInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description DocInfoServiceImpl
 * @Auther fanghao on 5/30/2022.
 */
@Slf4j
@Service
public class DocInfoServiceImpl implements DocInfoService {

    @Autowired
    private DoctorInfoClient doctorInfoClient;

    @Override
    public DoctorInformationDto getDoctorInformationDtoByConds(String doctorReignNo,String doctorSource) {
        if (StringUtil.isEmpty(doctorReignNo) ) {
            return null;
        }
        return doctorInfoClient.getDoctorInformationDtoByConds(doctorReignNo,doctorSource).getEntity();
    }
}
