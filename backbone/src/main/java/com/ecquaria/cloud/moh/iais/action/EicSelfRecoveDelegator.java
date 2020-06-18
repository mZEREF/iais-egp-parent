package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.client.AppEicClient;
import com.ecquaria.cloud.moh.iais.client.AtEicClient;
import com.ecquaria.cloud.moh.iais.client.EicClient;
import com.ecquaria.cloud.moh.iais.client.LicEicClient;
import com.ecquaria.cloud.moh.iais.client.LicmEicClient;
import com.ecquaria.cloud.moh.iais.client.OnlineApptEicClient;
import com.ecquaria.cloud.moh.iais.client.OrgEicClient;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * EicRecoverJob
 *
 * @author Jinhua
 * @date 2020/4/17 9:41
 */
@Slf4j
@Delegator("eicSelfRecoveDelegator")
public class EicSelfRecoveDelegator {
    @Autowired
    private AtEicClient atEicClient;
    @Autowired
    private AppEicClient appEicClient;
    @Autowired
    private LicEicClient licEicClient;
    @Autowired
    private LicmEicClient licmEicClient;
    @Autowired
    private EicClient eicClient;
    @Autowired
    private OrgEicClient orgEicClient;
    @Autowired
    private OnlineApptEicClient onlineApptEicClient;
    @Value("${spring.application.name}")
    private String currentApp;
    @Value("${iais.current.domain}")
    private String currentDomain;

    public void selfRecover(BaseProcessClass bpc) {
        log.info("<======== Start EIC Self Recover Job =========>");
        String moduleName = currentApp + "-" + currentDomain;
        List<EicRequestTrackingDto> atList = atEicClient.getPendingRecords(moduleName).getEntity();
        List<EicRequestTrackingDto> appList = appEicClient.getPendingRecords(moduleName).getEntity();
        List<EicRequestTrackingDto> licList = licEicClient.getPendingRecords(moduleName).getEntity();
        List<EicRequestTrackingDto> licmList = licmEicClient.getPendingRecords(moduleName).getEntity();
        List<EicRequestTrackingDto> orgList = orgEicClient.getPendingRecords(moduleName).getEntity();
        List<EicRequestTrackingDto> sysList = eicClient.getPendingRecords(moduleName).getEntity();
        List<EicRequestTrackingDto> apptList = onlineApptEicClient.getPendingRecords(moduleName).getEntity();
        AuditTrailDto auditTrailDto = AuditTrailHelper.getBatchJobDto(currentDomain);
        if (!IaisCommonUtils.isEmpty(atList)) {
            atList.forEach(ert -> {
                reTrigger(ert, auditTrailDto);
            });
            atEicClient.updateStatus(atList);
        }
        if (!IaisCommonUtils.isEmpty(appList)) {
            appList.forEach(ert -> {
                reTrigger(ert, auditTrailDto);
            });
            appEicClient.updateStatus(appList);
        }
        if (!IaisCommonUtils.isEmpty(licList)) {
            licList.forEach(ert -> {
                reTrigger(ert, auditTrailDto);
            });
            licEicClient.updateStatus(licList);
        }
        if (!IaisCommonUtils.isEmpty(licmList)) {
            licmList.forEach(ert -> {
                reTrigger(ert, auditTrailDto);
            });
            licmEicClient.updateStatus(licmList);
        }
        if (!IaisCommonUtils.isEmpty(orgList)) {
            orgList.forEach(ert -> {
                reTrigger(ert, auditTrailDto);
            });
            orgEicClient.updateStatus(orgList);
        }
        if (!IaisCommonUtils.isEmpty(sysList)) {
            sysList.forEach(ert -> {
                reTrigger(ert, auditTrailDto);
            });
            eicClient.updateStatus(sysList);
        }
        if (!IaisCommonUtils.isEmpty(apptList)) {
            apptList.forEach(ert -> {
                reTrigger(ert, auditTrailDto);
            });
            onlineApptEicClient.updateStatus(apptList);
        }
        log.info("<======== End EIC Self Recover Job =========>");
    }

    private void reTrigger(EicRequestTrackingDto ert, AuditTrailDto auditTrailDto) {
        Date now = new Date();
        ert.setLastActionAt(now);
        ert.setProcessNum(ert.getProcessNum() + 1);
        ert.setAuditTrailDto(auditTrailDto);
        if (ert.getFirstActionAt() == null) {
            ert.setFirstActionAt(now);
        }
        try {
            Class dtoCls = Class.forName(ert.getDtoClsName());
            Object obj = JsonUtil.parseToObject(ert.getDtoObject(), dtoCls);
            Class actCls = Class.forName(ert.getActionClsName());
            Object actObj = SpringContextHelper.getContext().getBean(actCls);
            Method method = actCls.getMethod(ert.getActionMethod(), dtoCls);
            method.invoke(actObj, obj);
            ert.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
        } catch (Exception e) {
            log.error("Error for EIC tracking id " + StringUtil.changeForLog(ert.getId()), e);
        }
    }
}
