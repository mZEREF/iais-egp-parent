package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sg.gov.moh.iais.egp.bsb.client.BsbInboxClient;
import sg.gov.moh.iais.egp.bsb.dto.PagingAndSortingDto;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxDashboardDto;

import javax.servlet.http.HttpServletRequest;

import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_ACTION_VALUE_CHANGE_PAGE;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_ACTION_VALUE_CHANGE_SIZE;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_DASHBOARD_ACTIVE_APPROVAL_AMT;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_DASHBOARD_ACTIVE_FACILITY_AMT;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_DASHBOARD_DRAFT_APP_AMT;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_DASHBOARD_UNREAD_MSG_AMT;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_VALUE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_PAGE_NO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_PAGE_SIZE;

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

    public static void page(HttpServletRequest request, PagingAndSortingDto searchDto) {
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
        switch (actionValue) {
            case KEY_ACTION_VALUE_CHANGE_SIZE:
                int pageSize = ParamUtil.getInt(request, KEY_PAGE_SIZE);
                searchDto.setPage(0);
                searchDto.setSize(pageSize);
                break;
            case KEY_ACTION_VALUE_CHANGE_PAGE:
                int pageNo = ParamUtil.getInt(request, KEY_PAGE_NO);
                searchDto.setPage(pageNo - 1);
                break;
            default:
                log.warn("page, action_value is invalid: {}", actionValue);
                break;
        }
    }
}
