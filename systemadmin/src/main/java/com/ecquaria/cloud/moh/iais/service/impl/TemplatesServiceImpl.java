package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import com.ecquaria.cloud.moh.iais.helper.EicRequestTrackingHelper;
import com.ecquaria.cloud.moh.iais.service.TemplatesService;
import com.ecquaria.cloud.moh.iais.service.client.EicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2019-12-23 15:02
 **/
@Slf4j
@Service
public class TemplatesServiceImpl implements TemplatesService {

    @Autowired
    private MsgTemplateClient msgTemplateClient;

    @Autowired
    private EicGatewayClient eicGatewayClient;

    @Autowired
    private EicRequestTrackingHelper requestTrackingHelper;

    @Value("${spring.application.name}")
    private String currentApp;

    @Value("${iais.current.domain}")
    private String currentDomain;

    @Override
    @SearchTrack(catalog = MsgTemplateConstants.MSG_TEMPLATE_FILE, key = MsgTemplateConstants.MSG_TEMPLATE_SQL)
    public SearchResult<MsgTemplateQueryDto> getTemplateResults(SearchParam param) {
        return msgTemplateClient.getMsgTemplateResult(param).getEntity();
    }

    @Override
    public MsgTemplateDto getMsgTemplate(String id) {
        return msgTemplateClient.getMsgTemplate(id).getEntity();
    }

    @Override
    public MsgTemplateDto updateMsgTemplate(MsgTemplateDto msgTemplateDto) {
        return msgTemplateClient.updateMasterCode(msgTemplateDto).getEntity();
    }

    @Override
    public List<String> suggestTemplateCodeDescription(String code){
        return msgTemplateClient.suggestTemplateCodeDescription(code).getEntity();
    }

    @Override
    public void syncTemplateFe(MsgTemplateDto msgTemplateDto) {
        EicRequestTrackingDto track = requestTrackingHelper.clientSaveEicRequestTracking(EicClientConstant.SYSTEM_ADMIN_CLIENT,
                TemplatesService.class.getName(), "syncTemplateFeWithoutTrack", currentApp + "-" + currentDomain,
                MsgTemplateDto.class.getName(), JsonUtil.parseToJson(msgTemplateDto));
        track.setProcessNum(1);
        Date now = new Date();
        track.setFirstActionAt(now);
        track.setLastActionAt(now);
        try {
            syncTemplateFeWithoutTrack(msgTemplateDto);
            track.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
        } catch (Exception e) {
            // 4b) If failed, still needs to update the running data to DB.
            track.setStatus(AppConsts.EIC_STATUS_PENDING_PROCESSING);
            log.error(e.getMessage(), e);
        }
        requestTrackingHelper.saveEicTrack(EicClientConstant.SYSTEM_ADMIN_CLIENT, track);
    }

    public void syncTemplateFeWithoutTrack(MsgTemplateDto msgTemplateDto) {
        eicGatewayClient.syncTemplateFe(msgTemplateDto);
    }
}
