package sg.gov.moh.iais.egp.bsb.ajax;

import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sg.gov.moh.iais.egp.bsb.client.BsbInboxClient;


import static sg.gov.moh.iais.egp.bsb.constant.InboxActionControlConstants.ACTION_APPROVAL_UPDATE;
import static sg.gov.moh.iais.egp.bsb.constant.InboxActionControlConstants.ACTION_FACILITY_APPLY_FOR_APPROVAL;
import static sg.gov.moh.iais.egp.bsb.constant.InboxActionControlConstants.ACTION_FACILITY_DEFER_RENEWAL;
import static sg.gov.moh.iais.egp.bsb.constant.InboxActionControlConstants.ACTION_FACILITY_RENEW;
import static sg.gov.moh.iais.egp.bsb.constant.InboxActionControlConstants.ACTION_FACILITY_UPDATE;
import static sg.gov.moh.iais.egp.bsb.constant.InboxActionControlConstants.URL_APPROVAL_UPDATE;
import static sg.gov.moh.iais.egp.bsb.constant.InboxActionControlConstants.URL_FACILITY_APPLY_FOR_APPROVAL;
import static sg.gov.moh.iais.egp.bsb.constant.InboxActionControlConstants.URL_FACILITY_DEFER_RENEWAL;
import static sg.gov.moh.iais.egp.bsb.constant.InboxActionControlConstants.URL_FACILITY_RENEW;
import static sg.gov.moh.iais.egp.bsb.constant.InboxActionControlConstants.URL_FACILITY_UPDATE;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/action-control")
public class ActionControlAjaxController {

    private final BsbInboxClient bsbInboxClient;

    @GetMapping(value = "/facility")
    public String facilityAction(@RequestParam("actionValue") String actionValue, @RequestParam("facilityId") String facilityId) {
        log.info("actionValue is {}", StringUtils.normalizeSpace(actionValue));
        String url = "";
        String maskedFacilityId;

        int ongoingApplicationSize = bsbInboxClient.countOngoingApplicationByFacId(facilityId).getEntity();
        log.info("ongoing application size is {}", ongoingApplicationSize);
        if (ongoingApplicationSize != 0) {
            return "hasOnGoingApp";
        }

        switch (actionValue) {
            case ACTION_FACILITY_APPLY_FOR_APPROVAL:
                maskedFacilityId = MaskUtil.maskValue("applyApprovalFacId", facilityId);
                url = URL_FACILITY_APPLY_FOR_APPROVAL + maskedFacilityId;
                break;
//            case ACTION_FACILITY_INVENTORY_NOTIFICATION_DATA_SUBMISSION:
//                maskedFacilityId = MaskUtil.maskValue("facId", facilityId);
//                url = URL_FACILITY_INVENTORY_NOTIFICATION_DATA_SUBMISSION + maskedFacilityId;
//                break;
            case ACTION_FACILITY_RENEW:
                maskedFacilityId = MaskUtil.maskValue("facId", facilityId);
                url = URL_FACILITY_RENEW + maskedFacilityId;
                break;
            case ACTION_FACILITY_UPDATE:
                maskedFacilityId = MaskUtil.maskValue("facId", facilityId);
                url = URL_FACILITY_UPDATE + maskedFacilityId;
                break;
//            case ACTION_FACILITY_INCIDENT_REPORTING:
//                maskedFacilityId = MaskUtil.maskValue("facId", facilityId);
//                url = URL_FACILITY_INCIDENT_REPORTING + maskedFacilityId;
//                break;
//            case ACTION_FACILITY_DEREGISTER:
//                maskedFacilityId = MaskUtil.maskValue("facId", facilityId);
//                url = URL_FACILITY_DEREGISTER + maskedFacilityId;
//                break;
            case ACTION_FACILITY_DEFER_RENEWAL:
                maskedFacilityId = MaskUtil.maskValue("facId", facilityId);
                url = URL_FACILITY_DEFER_RENEWAL + maskedFacilityId;
                break;
            default:
                log.error("no such action");
                break;
        }
        return url;
    }


    @GetMapping(value = "/approval")
    public String approvalAction(@RequestParam("actionValue") String actionValue, @RequestParam("approvalId") String approvalId) {
        log.info("actionValue is {}", StringUtils.normalizeSpace(actionValue));
        String url = "";
        String maskedApprovalId;

        int ongoingApplicationSize = bsbInboxClient.countOngoingApplicationByApprovalId(approvalId).getEntity();
        log.info("ongoing application size is {}", ongoingApplicationSize);
        if (ongoingApplicationSize != 0) {
            return "hasOnGoingApp";
        }

        switch (actionValue) {
            case ACTION_APPROVAL_UPDATE:
                maskedApprovalId = MaskUtil.maskValue("rfcApprovalId", approvalId);
                url = URL_APPROVAL_UPDATE + maskedApprovalId;
                break;
//            case ACTION_APPROVAL_CANCEL:
//                maskedApprovalId = MaskUtil.maskValue("approvalId", approvalId);
//                url = URL_APPROVAL_CANCEL + maskedApprovalId;
//                break;
            default:
                log.error("no such action");
                break;
        }
        return url;
    }
}
