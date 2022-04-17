package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealApplicationDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.AppealApplicaionService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.util.EicUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * AppealApplicaionServiceImpl
 *
 * @author suocheng
 * @date 2/11/2020
 */
@Service
@Slf4j
public class AppealApplicaionServiceImpl implements AppealApplicaionService {
    @Autowired
    private BeEicGatewayClient beEicGatewayClient;

    @Autowired
    private ApplicationClient applicationClient;

    @Override
    public AppealApplicationDto updateFEAppealApplicationDto(String eventRefNum,String submissionId) {
        EicRequestTrackingDto appEicRequestTrackingDto = getAppEicRequestTrackingDtoByRefNo(eventRefNum);
        AppealApplicationDto appealApplicationDto = EicUtil.getObjectApp(appEicRequestTrackingDto, AppealApplicationDto.class);
        if (appealApplicationDto != null) {
            appealApplicationDto = beEicGatewayClient.callEicWithTrack(appealApplicationDto,
                    beEicGatewayClient::updateAppealApplication, "updateAppealApplication").getEntity();
        } else {
            log.debug(StringUtil.changeForLog("This eventReo can not get the AppEicRequestTrackingDto -->:" + eventRefNum));
        }
        return appealApplicationDto;
    }

    @Override
    public EicRequestTrackingDto getAppEicRequestTrackingDtoByRefNo(String refNo) {
        return applicationClient.getAppEicRequestTrackingDto(refNo).getEntity();
    }

}
