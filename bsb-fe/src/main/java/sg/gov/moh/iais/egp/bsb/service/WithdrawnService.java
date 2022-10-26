package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.WithdrawApplicationDto;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.WithdrawnClient;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_AO_APPROVAL;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_AO_APPROVAL_LETTER_REVIEW;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_AO_RECTIFICATION_REVIEW;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_APPLICANT_CLARIFICATION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_APPLICANT_INPUT;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_APPOINTMENT_CONFIRMATION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_APPOINTMENT_SCHEDULING;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_CHECKLIST_SUBMISSION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_DO_APPROVAL_LETTER_DRAFT;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_DO_RECOMMENDATION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_DO_RECTIFICATION_REVIEW;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_DO_REPORT_APPROVAL;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_HM_APPROVAL;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_INSPECTION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_INSPECTION_READINESS;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_INSPECTION_REPORT;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_INSPECTION_REPORT_REVIEW;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_INSPECTION_REPORT_REVISION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_NC_RECTIFICATION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_TYPE_CANCEL;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_TYPE_DEREGISTRATION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_TYPE_NEW;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_TYPE_RENEW;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_TYPE_RFC;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_TYPE_SUBMISSION;


@Service
@Slf4j
@RequiredArgsConstructor
public class WithdrawnService {
    private static final String PARAM_BACK_URL = "backUrl";

    private final WithdrawnClient withdrawnClient;

    public void setBackUrl(HttpServletRequest request, String from) {
        if (StringUtils.hasLength(from)) {
            if (from.equals("application")) {
                ParamUtil.setSessionAttr(request, PARAM_BACK_URL, "/bsb-web/eservice/INTERNET/MohBSBInboxApp");
            } else if (from.equals("dataSubmission")) {
                ParamUtil.setSessionAttr(request, PARAM_BACK_URL, "/bsb-web/eservice/INTERNET/DataSubInbox");
            }
        } else {
            ParamUtil.setSessionAttr(request, PARAM_BACK_URL, "/bsb-web/eservice/INTERNET/MohBSBInboxMsg");
        }
    }

    public List<WithdrawApplicationDto> getCanWithdrawAppList() {
        List<WithdrawApplicationDto> withdrawApplicationDtoList = withdrawnClient.getApplicationByAppTypesAndStatus(getWithdrawnAppTypeAndStatus()).getEntity();
        for (WithdrawApplicationDto withdrawApplicationDto : withdrawApplicationDtoList) {
            String appNoMaskId = MaskUtil.maskValue("appNo", withdrawApplicationDto.getApplicationNo());
            withdrawApplicationDto.setMaskId(appNoMaskId);
        }
        return withdrawApplicationDtoList;
    }

    public Map<String, List<String>> getWithdrawnAppTypeAndStatus() {
        Map<String, List<String>> appTypeAndStatusMap = new HashMap<>(6);
        //New
        // TODO: check these app status
        List<String> newStatus = new ArrayList<>(19);
        newStatus.add(APP_STATUS_PEND_DO_RECOMMENDATION);
        newStatus.add(APP_STATUS_PEND_AO_APPROVAL);
        newStatus.add(APP_STATUS_PEND_HM_APPROVAL);
        newStatus.add(APP_STATUS_PEND_APPLICANT_INPUT);
        newStatus.add(APP_STATUS_PEND_APPOINTMENT_SCHEDULING);
        newStatus.add(APP_STATUS_PEND_CHECKLIST_SUBMISSION);
        newStatus.add(APP_STATUS_PEND_INSPECTION_READINESS);
        newStatus.add(APP_STATUS_PEND_APPLICANT_CLARIFICATION);
        newStatus.add(APP_STATUS_PEND_INSPECTION);
        newStatus.add(APP_STATUS_PEND_INSPECTION_REPORT);
        newStatus.add(APP_STATUS_PEND_INSPECTION_REPORT_REVIEW);
        newStatus.add(APP_STATUS_PEND_INSPECTION_REPORT_REVISION);
        newStatus.add(APP_STATUS_PEND_DO_REPORT_APPROVAL);
        newStatus.add(APP_STATUS_PEND_NC_RECTIFICATION);
        newStatus.add(APP_STATUS_PEND_DO_RECTIFICATION_REVIEW);
        newStatus.add(APP_STATUS_PEND_AO_RECTIFICATION_REVIEW);
        newStatus.add(APP_STATUS_PEND_DO_APPROVAL_LETTER_DRAFT);
        newStatus.add(APP_STATUS_PEND_AO_APPROVAL_LETTER_REVIEW);
        newStatus.add(APP_STATUS_PEND_APPOINTMENT_CONFIRMATION);
        appTypeAndStatusMap.put(APP_TYPE_NEW, newStatus);

        //Renew
        List<String> renewStatus = new ArrayList<>(19);
        renewStatus.add(APP_STATUS_PEND_DO_RECOMMENDATION);
        renewStatus.add(APP_STATUS_PEND_AO_APPROVAL);
        renewStatus.add(APP_STATUS_PEND_HM_APPROVAL);
        renewStatus.add(APP_STATUS_PEND_APPLICANT_INPUT);
        renewStatus.add(APP_STATUS_PEND_APPOINTMENT_SCHEDULING);
        renewStatus.add(APP_STATUS_PEND_CHECKLIST_SUBMISSION);
        renewStatus.add(APP_STATUS_PEND_INSPECTION_READINESS);
        renewStatus.add(APP_STATUS_PEND_APPLICANT_CLARIFICATION);
        renewStatus.add(APP_STATUS_PEND_INSPECTION);
        renewStatus.add(APP_STATUS_PEND_INSPECTION_REPORT);
        renewStatus.add(APP_STATUS_PEND_INSPECTION_REPORT_REVIEW);
        renewStatus.add(APP_STATUS_PEND_INSPECTION_REPORT_REVISION);
        renewStatus.add(APP_STATUS_PEND_DO_REPORT_APPROVAL);
        renewStatus.add(APP_STATUS_PEND_NC_RECTIFICATION);
        renewStatus.add(APP_STATUS_PEND_DO_RECTIFICATION_REVIEW);
        renewStatus.add(APP_STATUS_PEND_AO_RECTIFICATION_REVIEW);
        renewStatus.add(APP_STATUS_PEND_DO_APPROVAL_LETTER_DRAFT);
        renewStatus.add(APP_STATUS_PEND_AO_APPROVAL_LETTER_REVIEW);
        renewStatus.add(APP_STATUS_PEND_APPOINTMENT_CONFIRMATION);
        appTypeAndStatusMap.put(APP_TYPE_RENEW, renewStatus);

        //RFC
        List<String> rfcStatus = new ArrayList<>(19);
        rfcStatus.add(APP_STATUS_PEND_DO_RECOMMENDATION);
        rfcStatus.add(APP_STATUS_PEND_AO_APPROVAL);
        rfcStatus.add(APP_STATUS_PEND_HM_APPROVAL);
        rfcStatus.add(APP_STATUS_PEND_APPLICANT_INPUT);
        rfcStatus.add(APP_STATUS_PEND_APPOINTMENT_SCHEDULING);
        rfcStatus.add(APP_STATUS_PEND_CHECKLIST_SUBMISSION);
        rfcStatus.add(APP_STATUS_PEND_INSPECTION_READINESS);
        rfcStatus.add(APP_STATUS_PEND_APPLICANT_CLARIFICATION);
        rfcStatus.add(APP_STATUS_PEND_INSPECTION);
        rfcStatus.add(APP_STATUS_PEND_INSPECTION_REPORT);
        rfcStatus.add(APP_STATUS_PEND_INSPECTION_REPORT_REVIEW);
        rfcStatus.add(APP_STATUS_PEND_INSPECTION_REPORT_REVISION);
        rfcStatus.add(APP_STATUS_PEND_DO_REPORT_APPROVAL);
        rfcStatus.add(APP_STATUS_PEND_NC_RECTIFICATION);
        rfcStatus.add(APP_STATUS_PEND_DO_RECTIFICATION_REVIEW);
        rfcStatus.add(APP_STATUS_PEND_AO_RECTIFICATION_REVIEW);
        rfcStatus.add(APP_STATUS_PEND_DO_APPROVAL_LETTER_DRAFT);
        rfcStatus.add(APP_STATUS_PEND_AO_APPROVAL_LETTER_REVIEW);
        rfcStatus.add(APP_STATUS_PEND_APPOINTMENT_CONFIRMATION);
        appTypeAndStatusMap.put(APP_TYPE_RFC, rfcStatus);

        //Cancellation
        List<String> cancelStatus = new ArrayList<>(3);
        cancelStatus.add(APP_STATUS_PEND_DO_RECOMMENDATION);
        cancelStatus.add(APP_STATUS_PEND_AO_APPROVAL);
        cancelStatus.add(APP_STATUS_PEND_HM_APPROVAL);
        appTypeAndStatusMap.put(APP_TYPE_CANCEL, cancelStatus);

        //DeRegistration
        List<String> deRegistrationStatus = new ArrayList<>(3);
        deRegistrationStatus.add(APP_STATUS_PEND_DO_RECOMMENDATION);
        deRegistrationStatus.add(APP_STATUS_PEND_AO_APPROVAL);
        deRegistrationStatus.add(APP_STATUS_PEND_HM_APPROVAL);
        appTypeAndStatusMap.put(APP_TYPE_DEREGISTRATION, deRegistrationStatus);

        //Data Submission
        List<String> dataSubmissionStatus = new ArrayList<>(1);
        // TODO: check this app status
//        dataSubmissionStatus.add(APP_STATUS_PEND_ACKNOWLEDGEMENT);
        appTypeAndStatusMap.put(APP_TYPE_SUBMISSION, dataSubmissionStatus);

        return appTypeAndStatusMap;
    }
}
