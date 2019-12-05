package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import java.util.List;

import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ApplicationServiceImpl
 *
 * @author suocheng
 * @date 11/28/2019
 */
@Service
@Slf4j
public class ApplicationServiceImpl implements ApplicationService {
    @Autowired
    private ApplicationClient applicationClient;
    @Override
    public List<ApplicationDto> getApplicaitonsByAppGroupId(String appGroupId) {

        return    applicationClient. getGroupAppsByNo(appGroupId).getEntity();
    }

    @Override
    public boolean isOtherApplicaitonSubmit(List<ApplicationDto> applicationDtoList,String appId,String status) {
        if(applicationDtoList == null || applicationDtoList.size() == 0 || StringUtil.isEmpty(appId) || StringUtil.isEmpty(status)){
            return  false;
        }
        boolean result = true;
        for(ApplicationDto applicationDto : applicationDtoList){
           if(appId.equals(applicationDto.getId())){
               continue;
           }else if(!status.equals(applicationDto.getStatus())){
                result = false;
                break;
           }
        }
        return result;
    }

//    @Override
//    public List<AppPremisesCorrelationDto> getAppPremisesCorrelationByAppGroupId(String appGroupId) {
//        return RestApiUtil.getListByPathParam(RestApiUrlConsts.APPLICATION_APPPREMISESCORRELATIONS_APPGROPID,appGroupId,AppPremisesCorrelationDto.class);
//    }
}
