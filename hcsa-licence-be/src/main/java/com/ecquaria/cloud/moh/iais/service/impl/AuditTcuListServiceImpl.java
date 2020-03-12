package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditTaskDataFillterDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.service.AuditTcuListService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: jiahao
 * @Date: 2020/2/20 15:09
 */
@Slf4j
@Service
public class AuditTcuListServiceImpl implements AuditTcuListService {
    @Autowired
    HcsaLicenceClient hcsaLicenceClient;
    @Override
    public List<AuditTaskDataFillterDto> getAuditTcuList() {
        return hcsaLicenceClient.getAuditTcuList().getEntity();
    }

    @Override
    public void saveAuditTcuList(List<LicPremisesRecommendationDto> licPremisesRecommendationDtos){
        hcsaLicenceClient.saveAuditTcuList(licPremisesRecommendationDtos);
    }
}
