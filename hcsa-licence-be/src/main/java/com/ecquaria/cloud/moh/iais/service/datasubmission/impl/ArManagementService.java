package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArMgrQueryPatientDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.helper.EicRequestTrackingHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import java.util.Date;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * ArManagementService
 *
 * @author Jinhua
 * @date 2022/12/2 15:41
 */
@Service
@Slf4j
public class ArManagementService {
    @Value("${spring.application.name}")
    private String currentApp;

    @Value("${iais.current.domain}")
    private String currentDomain;

    @Autowired
    private HcsaLicenceClient client;

    @Autowired
    private EicRequestTrackingHelper eicRequestTrackingHelper;

    @Autowired
    private BeEicGatewayClient beEicGatewayClient;

    @Autowired
    private NotificationHelper notificationHelper;

    public SearchResult<ArMgrQueryPatientDto> queryPatient(SearchParam searchParam) {
        return client.queryForArPatients(searchParam).getEntity();
    }

    public void unlockDataSubmissions(String[] submissionNos) {
        client.unlockArDataSubmissions(submissionNos);
        eicRequestTrackingHelper.callEicWithTrack(submissionNos, this::syncUnlockArRecords, this.getClass().getName(),
                "syncUnlockArRecords", currentApp, currentDomain, EicClientConstant.LICENCE_CLIENT);
        //Send Email
        EmailParam emailParamEmail = new EmailParam();
        Map<String, Object> msgSubjectMap = IaisCommonUtils.genNewHashMap();
        StringBuilder sb = new StringBuilder();
        for (String submitNo : submissionNos) {
            if (sb.length() > 0) {
                sb.append(", " + submitNo);
            } else {
                sb.append(submitNo);
            }
        }
        String requestDate = Formatter.formatDate(new Date());
        msgSubjectMap.put("submissionId", sb.toString());
        msgSubjectMap.put("requestDate", requestDate);
        msgSubjectMap.put("officer_name", "officer_name");
        emailParamEmail.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_UNLOCK_MEMAIL_BE);
        emailParamEmail.setTemplateContent(msgSubjectMap);
        emailParamEmail.setQueryCode(IaisEGPHelper.generateRandomString(26));
        emailParamEmail.setReqRefNum(IaisEGPHelper.generateRandomString(26));
        emailParamEmail.setServiceTypes(DataSubmissionConsts.DS_AR_NEW);
        notificationHelper.sendNotification(emailParamEmail);
    }

    public void syncUnlockArRecords(String[] submissionNos) {
        beEicGatewayClient.syncUnlockArRecords(submissionNos);
    }
}
