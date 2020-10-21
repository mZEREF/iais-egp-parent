package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptAppInfoShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.service.GiroDeductionBeService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Shicheng
 * @date 2020/10/19 15:52
 **/
@Service
@Slf4j
public class GiroDeductionBeServiceImpl implements GiroDeductionBeService {

    @Autowired
    private ApplicationClient applicationClient;

    @Override
    public void sendMessageEmail(List<String> appGroupList) {
        if(!IaisCommonUtils.isEmpty(appGroupList)){
            List<ApplicationGroupDto> applicationGroupDtos = IaisCommonUtils.genNewArrayList();
            for(String appGroupNo : appGroupList){
                ApplicationGroupDto applicationGroupDto = applicationClient.getAppGrpByNo(appGroupNo).getEntity();
                applicationGroupDto.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_GIRO_RETRIGGER);
                applicationClient.updateApplication(applicationGroupDto);
                applicationGroupDtos.add(applicationGroupDto);
            }
            //todo eic update appGroup
            ApptAppInfoShowDto apptAppInfoShowDto = new ApptAppInfoShowDto();
        } else {
            log.info("Giro Deduction appGroupList is null");
        }
    }
}
