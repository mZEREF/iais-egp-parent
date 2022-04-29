package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sg.gov.moh.iais.egp.bsb.client.BsbInboxClient;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxDashboardDto;

import javax.servlet.http.HttpServletRequest;

import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.*;

@Service
@Slf4j
public class BsbInboxService {
    private final BsbInboxClient inboxClient;

    public BsbInboxService(BsbInboxClient inboxClient) {
        this.inboxClient = inboxClient;
    }

    public void retrieveDashboardData(HttpServletRequest request){
        InboxDashboardDto dashboardDto = inboxClient.retrieveDashboardData();
        ParamUtil.setRequestAttr(request, KEY_DASHBOARD_UNREAD_MSG_AMT, dashboardDto.getNewMsgAmt());
        ParamUtil.setRequestAttr(request, KEY_DASHBOARD_DRAFT_APP_AMT, dashboardDto.getDraftAppAmt());
        ParamUtil.setRequestAttr(request, KEY_DASHBOARD_ACTIVE_FACILITY_AMT, dashboardDto.getActiveFacilityAmt());
        ParamUtil.setRequestAttr(request, KEY_DASHBOARD_ACTIVE_APPROVAL_AMT, dashboardDto.getActiveApprovalsAmt());
    }
}
