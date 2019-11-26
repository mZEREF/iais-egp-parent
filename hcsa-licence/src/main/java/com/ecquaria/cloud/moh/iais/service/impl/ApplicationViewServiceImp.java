package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
@Slf4j
public class ApplicationViewServiceImp implements ApplicationViewService {
    @Override
    public ApplicationViewDto searchByAppNo(String appNo) {
        Map<String,Object> map = new HashMap<>();
        map.put("appNo",appNo);
        return RestApiUtil.getByReqParam("iais-application:8883/iais-application-be/applicationview/{appNo}",map, ApplicationViewDto.class);

    }

    @Override
    public boolean isAllApplicationSubmit(String appNo) {
        boolean result = false;
        ApplicationDto applicationDto = getApplicaitonByAppNo(appNo);

        if(applicationDto != null){
            String appGroupId = applicationDto.getAppGrpId();
            //todo:judge the All Applicaitons Submit
        }else{
            log.error(StringUtil.changeForLog("The applicationDto is null"));
        }

        return result;
    }

    @Override
    public ApplicationDto getApplicaitonByAppNo(String appNo) {
        return   RestApiUtil.getByPathParam(RestApiUrlConsts.APPLICATION_APPLICATION_APPNO,appNo,ApplicationDto.class);
    }

    @Override
    public ApplicationDto updateApplicaiton(ApplicationDto applicationDto) {
        return RestApiUtil.update(RestApiUrlConsts.IAIS_APPLICATION_BE,applicationDto,ApplicationDto.class);
    }

    @Override
    public List<OrganizationDto> getUserNameById(List<String> userIdList) {
        return RestApiUtil.postGetList("iais-organization:8879/users-by-ids",userIdList,OrganizationDto.class);
    }

    @Override
    public List<HcsaSvcDocConfigDto> getTitleById(List<String> titleIdList) {
        return RestApiUtil.postGetList("hcsa-config:8878/iais-hcsa-service/list-svc-doc-config",titleIdList,HcsaSvcDocConfigDto.class);
    }

    @Override
    public List<HcsaSvcRoutingStageDto> getStageName(String serviceId, String stageId) {
        Map<String,Object> map = new HashMap<>();
        map.put("serviceId",serviceId);
        map.put("stageId",stageId);
        return RestApiUtil.getListByReqParam("hcsa-config:8878/hcsa-routing/stage-id",map,HcsaSvcRoutingStageDto.class);


    }


}
