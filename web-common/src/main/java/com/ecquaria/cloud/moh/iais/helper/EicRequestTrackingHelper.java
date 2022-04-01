package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import com.ecquaria.cloud.moh.iais.service.client.AppEicClient;
import com.ecquaria.cloud.moh.iais.service.client.EicClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaServiceClient;
import com.ecquaria.cloud.moh.iais.service.client.LicEicClient;
import com.ecquaria.cloud.moh.iais.service.client.LicmEicClient;
import com.ecquaria.cloud.moh.iais.service.client.OnlineApptEicClient;
import com.ecquaria.cloud.moh.iais.service.client.OrgEicClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

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

    public <T, R> R callEicWithTrack(T obj, Function<T, R> function, String actionClass, String actionMethod, String currentApp,
            String currentDomain, int client) {
        // 1) Create and save the tracking record into DB before everything
        EicRequestTrackingDto track = this.clientSaveEicRequestTracking(client, actionClass, actionMethod,
                currentApp + "-" + currentDomain, obj.getClass().getName(), JsonUtil.parseToJson(obj));
        //2) Before executing the EIC function set the running data
        track.setProcessNum(track.getProcessNum() + 1);
        Date now = new Date();
        track.setFirstActionAt(now);
        track.setLastActionAt(now);

        R invoke = null;
        try {
            // 3) Call the EIC in a try catch
            invoke = function.apply(obj);
            // 4a) If success then update the tracking status to complete
            track.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
        } catch (Exception e) {
            // 4b) If failed, still needs to update the running data to DB.
            track.setStatus(AppConsts.EIC_STATUS_PENDING_PROCESSING);
            log.error(StringUtil.changeForLog(e.getMessage()), e);
        }
        log.info(StringUtil.changeForLog("Call Eic With Track: " + client + " - " + track.getModuleName()));
        log.info(StringUtil.changeForLog(actionClass + " - " + actionMethod));
        // 5) Update tracking
        this.saveEicTrack(client, track);
        return invoke;
    }

    public <T> void callEicWithTrack(T obj, Consumer<T> consumer, String actionClass, String actionMethod, String currentApp,
            String currentDomain, int client) {
        // 1) Create and save the tracking record into DB before everything
        EicRequestTrackingDto track = this.clientSaveEicRequestTracking(client, actionClass, actionMethod,
                currentApp + "-" + currentDomain, obj.getClass().getName(), JsonUtil.parseToJson(obj));
        //2) Before executing the EIC function set the running data
        track.setProcessNum(track.getProcessNum() + 1);
        Date now = new Date();
        track.setFirstActionAt(now);
        track.setLastActionAt(now);

        try {
            // 3) Call the EIC in a try catch
            consumer.accept(obj);
            // 4a) If success then update the tracking status to complete
            track.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
        } catch (Exception e) {
            // 4b) If failed, still needs to update the running data to DB.
            track.setStatus(AppConsts.EIC_STATUS_PENDING_PROCESSING);
            log.error(StringUtil.changeForLog(e.getMessage()), e);
        }
        log.info(StringUtil.changeForLog("Call Eic With Track: " + client + " - " + track.getModuleName()));
        log.info(StringUtil.changeForLog(actionClass + " - " + actionMethod));
        // 5) Update tracking
        this.saveEicTrack(client, track);
    }

    public EicRequestTrackingDto clientSaveEicRequestTracking(int client, String actionClsName,
                                                              String actionMethod, String moduleName,
                                                              String dtoClsName, String dtoObject){
        EicRequestTrackingDto eicRequestTrackingDto = new EicRequestTrackingDto();
        String refNo = UUID.randomUUID().toString();

        log.info(StringUtil.changeForLog("eic client request tracking " + client));
        log.info(StringUtil.changeForLog("new eic client ref number" + refNo));

        eicRequestTrackingDto.setAuditTrailDto(AuditTrailHelper.getCurrentAuditTrailDto());
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

        return saveEicTrack(client, eicRequestTrackingDto);
    }

    public EicRequestTrackingDto saveEicTrack(int client, EicRequestTrackingDto eicRequestTrackingDto) {
        switch (client){
            case EicClientConstant.APPLICATION_CLIENT:
                eicRequestTrackingDto = appEicClient.saveEicTrack(eicRequestTrackingDto).getEntity();
                break;
            case EicClientConstant.ORGANIZATION_CLIENT:
                eicRequestTrackingDto = orgTrackingClient.saveEicTrack(eicRequestTrackingDto).getEntity();
                break;
            case EicClientConstant.SYSTEM_ADMIN_CLIENT:
                eicRequestTrackingDto = eicClient.saveEicTrack(eicRequestTrackingDto).getEntity();
                break;
            case EicClientConstant.ONLINE_APPT_CLIENT:
                eicRequestTrackingDto = onlineApptEicClient.saveEicTrack(eicRequestTrackingDto).getEntity();
                break;
            case EicClientConstant.HCSA_CONFIG:
                eicRequestTrackingDto = hcsaConfigClient.saveEicTrack(eicRequestTrackingDto).getEntity();
                break;
            case EicClientConstant.LICENCE_CLIENT:
                eicRequestTrackingDto = licEicClient.saveEicTrack(eicRequestTrackingDto).getEntity();
                break;
        }

        return eicRequestTrackingDto;
    }
}
