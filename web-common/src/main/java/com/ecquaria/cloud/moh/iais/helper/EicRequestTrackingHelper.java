package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.client.AppEicClient;
import com.ecquaria.cloud.moh.iais.client.EicClient;
import com.ecquaria.cloud.moh.iais.client.HcsaServiceClient;
import com.ecquaria.cloud.moh.iais.client.LicEicClient;
import com.ecquaria.cloud.moh.iais.client.LicmEicClient;
import com.ecquaria.cloud.moh.iais.client.OnlineApptEicClient;
import com.ecquaria.cloud.moh.iais.client.OrgEicClient;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * @Author: yichen
 * @Description:
 * @Date:2020/5/15
 **/

@Service
@Slf4j
public final class EicRequestTrackingHelper {

    @Autowired
    private OrgEicClient orgTrackingClient;

    @Autowired
    private AppEicClient appEicClient;

    @Autowired
    private OnlineApptEicClient onlineApptEicClient;

    @Autowired
    private EicClient eicClient;

    @Autowired
    private HcsaServiceClient hcsaServiceClient;

    @Autowired
    private LicEicClient licEicClient;

    @Autowired
    private LicmEicClient hcsaConfigClient;

    public LicmEicClient getHcsaConfigClient() {
        return hcsaConfigClient;
    }

    public LicEicClient getLicEicClient() {
        return licEicClient;
    }

    public HcsaServiceClient getHcsaServiceClient() {
        return hcsaServiceClient;
    }

    public EicClient getEicClient() {
        return this.eicClient;
    }

    public OrgEicClient getOrgTrackingClient() {
        return this.orgTrackingClient;
    }

    public AppEicClient getAppEicClient() {
        return this.appEicClient;
    }

    public EicRequestTrackingDto clientSaveEicRequestTracking(int client, String actionClsName,
                                                              String actionMethod, String moduleName,
                                                              String dtoClsName, String dtoObject){
        EicRequestTrackingDto eicRequestTrackingDto = new EicRequestTrackingDto();
        String refNo = UUID.randomUUID().toString();

        log.info(StringUtil.changeForLog("eic client request tracking " + client));
        log.info(StringUtil.changeForLog("new eic client ref number" + refNo));

        eicRequestTrackingDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        eicRequestTrackingDto.setActionClsName(actionClsName);
        eicRequestTrackingDto.setDtoClsName(dtoClsName);
        eicRequestTrackingDto.setActionMethod(actionMethod);
        eicRequestTrackingDto.setModuleName(moduleName);
        eicRequestTrackingDto.setDtoObject(dtoObject);
        eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PENDING_PROCESSING);
        eicRequestTrackingDto.setRefNo(refNo);
        Date now = new Date();
        eicRequestTrackingDto.setProcessNum(0);
        eicRequestTrackingDto.setFirstActionAt(now);
        eicRequestTrackingDto.setLastActionAt(now);

        switch (client){
            case EicClientConstant.APPLICATION_CLIENT:
                appEicClient.saveEicTrack(eicRequestTrackingDto);
                break;
            case EicClientConstant.ORGANIZATION_CLIENT:
                orgTrackingClient.saveEicTrack(eicRequestTrackingDto);
                break;
            case EicClientConstant.SYSTEM_ADMIN_CLIENT:
                eicClient.saveEicTrack(eicRequestTrackingDto);
                break;
            case EicClientConstant.ONLINE_APPT_CLIENT:
                onlineApptEicClient.saveEicTrack(eicRequestTrackingDto);
                break;
            case EicClientConstant.HCSA_CONFIG:
                hcsaConfigClient.saveEicTrack(eicRequestTrackingDto);
                break;
            case EicClientConstant.LICENCE_CLIENT:
                licEicClient.saveEicTrack(eicRequestTrackingDto);
                break;
        }

        return eicRequestTrackingDto;
    }

}
