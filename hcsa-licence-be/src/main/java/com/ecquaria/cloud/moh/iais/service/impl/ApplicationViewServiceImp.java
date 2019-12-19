package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Slf4j
public class ApplicationViewServiceImp implements ApplicationViewService {
    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private OrganizationClient organizationClient;
    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Override
    public ApplicationViewDto searchByAppNo(String appNo) {
        //return applicationClient.getAppViewByNo(appNo).getEntity();
        return applicationClient.getAppViewByNo(appNo).getEntity();
    }

    @Override
    public ApplicationDto getApplicaitonByAppNo(String appNo) {
        return applicationClient.getAppByNo(appNo).getEntity();
    }

    @Override
    public ApplicationDto updateApplicaiton(ApplicationDto applicationDto) {

        return  applicationClient.updateApplication(applicationDto).getEntity();
    }

    @Override
    public List<OrgUserDto> getUserNameById(List<String> userIdList) {

        return  organizationClient.retrieveOrgUserAccount(userIdList).getEntity();
    }

    @Override
    public List<HcsaSvcDocConfigDto> getTitleById(List<String> titleIdList) {

        return  hcsaConfigClient.listSvcDocConfig(titleIdList).getEntity();
    }

    @Override
    public List<HcsaSvcRoutingStageDto> getStage(String serviceId, String stageId) {
     
        return   hcsaConfigClient.getStageName(serviceId,stageId).getEntity();


    }

    @Override
    public OrgUserDto getUserById(String userId) {
        return organizationClient.retrieveOneOrgUserAccount(userId).getEntity();
    }


}
