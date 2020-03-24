package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.withdrawn.WithdrawnDto;
import com.ecquaria.cloud.moh.iais.service.WithdrawalService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.CessationClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class WithdrawalServiceImpl implements WithdrawalService {


    @Autowired
    private SystemAdminClient systemAdminClient;

    @Autowired
    private CessationClient cessationClient;

    @Autowired
    private ApplicationClient applicationClient;

    @Override
    public void saveWithdrawn(WithdrawnDto withdrawnDto) {
        ApplicationDto applicationDto = new ApplicationDto();
        applicationDto.setApplicationNo(systemAdminClient.applicationNumber(ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL).getEntity()+"-01");
        withdrawnDto.setApplicationDto(applicationDto);
        ApplicationGroupDto applicationGroupDto = withdrawnDto.getApplicationGroupDto();
        getNewApplicationGroup(applicationGroupDto);
        cessationClient.saveWithdrawn(withdrawnDto);
    }


    private void getNewApplicationGroup(ApplicationGroupDto applicationGroupDto){
        applicationGroupDto.setGroupNo(systemAdminClient.applicationNumber(ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL).getEntity());
        applicationGroupDto.setDeclStmt("N");
        applicationGroupDto.setIsCharitable(0);
        applicationGroupDto.setGrpLic(false);
        applicationGroupDto.setIsByGiro(0);
        applicationGroupDto.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
        applicationGroupDto.setIsBundledFee(0);
        applicationGroupDto.setAmount(1770.0);
        applicationGroupDto.setSubmitDt(new Date());
        applicationGroupDto.setIsInspectionNeeded(1);
        applicationGroupDto.setIsPreInspection(1);
        applicationGroupDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_SUBMITED);
        applicationGroupDto.setAppType(ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL);
    }
}