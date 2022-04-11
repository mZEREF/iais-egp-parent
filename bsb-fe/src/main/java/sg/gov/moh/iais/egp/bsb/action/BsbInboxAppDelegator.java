package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.BsbInboxClient;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxAppSearchDto;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxAppSearchResultDto;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxDashboardDto;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.ResponseConstants.ERROR_CODE_VALIDATION_FAIL;
import static sg.gov.moh.iais.egp.bsb.constant.ResponseConstants.ERROR_INFO_ERROR_MSG;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_ADDITIONAL;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_VALUE;


@Slf4j
@Delegator("bsbInboxAppDelegator")
public class BsbInboxAppDelegator {
    private static final String KEY_INBOX_APP_PAGE_INFO = "pageInfo";

    private static final String KEY_PAGE_SIZE = "pageJumpNoPageSize";
    private static final String KEY_PAGE_NO = "pageJumpNoTextchangePage";

    private final BsbInboxClient inboxClient;

    @Autowired
    public BsbInboxAppDelegator(BsbInboxClient inboxClient) {
        this.inboxClient = inboxClient;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_INBOX_APP_SEARCH_DTO);

        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_INTERNAL_INBOX, "Inbox Application");
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;

        // get search DTO
        InboxAppSearchDto searchDto = getSearchDto(request);
        ParamUtil.setSessionAttr(request, KEY_INBOX_APP_SEARCH_DTO, searchDto);

        // call API to get dashboard data
        InboxDashboardDto dashboardDto = inboxClient.retrieveDashboardData();
        ParamUtil.setRequestAttr(request, KEY_DASHBOARD_UNREAD_MSG_AMT, dashboardDto.getNewMsgAmt());
        ParamUtil.setRequestAttr(request, KEY_DASHBOARD_DRAFT_APP_AMT, dashboardDto.getDraftAppAmt());
        ParamUtil.setRequestAttr(request, KEY_DASHBOARD_ACTIVE_FACILITY_AMT, dashboardDto.getActiveFacilityAmt());
        ParamUtil.setRequestAttr(request, KEY_DASHBOARD_ACTIVE_APPROVAL_AMT, dashboardDto.getActiveApprovalsAmt());

        // call API to get searched data
        ResponseDto<InboxAppSearchResultDto> resultDto = inboxClient.getInboxApplication(searchDto);
        if (resultDto.ok()) {
            ParamUtil.setRequestAttr(request, KEY_INBOX_APP_PAGE_INFO, resultDto.getEntity().getPageInfo());
            ParamUtil.setRequestAttr(request, KEY_INBOX_DATA_LIST, resultDto.getEntity().getApplications());
        } else {
            log.warn("Search Inbox Application Fail");
            ParamUtil.setRequestAttr(request, KEY_INBOX_APP_PAGE_INFO, PageInfo.emptyPageInfo(searchDto));
            ParamUtil.setRequestAttr(request, KEY_INBOX_DATA_LIST, new ArrayList<>());
            if(ERROR_CODE_VALIDATION_FAIL.equals(resultDto.getErrorCode())) {
                ParamUtil.setRequestAttr(request, ERROR_INFO_ERROR_MSG, resultDto.getErrorInfos().get(ERROR_INFO_ERROR_MSG));
            }
        }

        List<SelectOption> processTypeOps = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_BSB_PRO_TYPE);
        ParamUtil.setRequestAttr(request, "processTypeOps", processTypeOps);
        List<SelectOption> appStatusOps = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_BSB_APP_STATUS);
        ParamUtil.setRequestAttr(request, "appStatusOps", appStatusOps);
    }

    public void search(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InboxAppSearchDto searchDto = bindModel(request, getSearchDto(request));
        searchDto.setPage(0);
        ParamUtil.setSessionAttr(request, KEY_INBOX_APP_SEARCH_DTO, searchDto);
    }

    public void sort(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InboxAppSearchDto searchDto = getSearchDto(request);
        String field = ParamUtil.getString(request, KEY_ACTION_VALUE);
        String sortType = ParamUtil.getString(request, KEY_ACTION_ADDITIONAL);
        searchDto.changeSort(field, sortType);
        ParamUtil.setSessionAttr(request, KEY_INBOX_APP_SEARCH_DTO, searchDto);
    }

    public void page(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InboxAppSearchDto searchDto = getSearchDto(request);
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
        switch (actionValue) {
            case "changeSize":
                int pageSize = ParamUtil.getInt(request, KEY_PAGE_SIZE);
                searchDto.setPage(0);
                searchDto.setSize(pageSize);
                break;
            case "changePage":
                int pageNo = ParamUtil.getInt(request, KEY_PAGE_NO);
                searchDto.setPage(pageNo - 1);
                break;
            default:
                log.warn("page, action_value is invalid: {}", actionValue);
                break;
        }
        ParamUtil.setSessionAttr(request, KEY_INBOX_APP_SEARCH_DTO, searchDto);
    }

    public void deleteDraft(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String composedValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
        String maskedAppId = composedValue.substring("deleteDraft".length());
        String appId = MaskHelper.unmask("deleteId", maskedAppId);
        inboxClient.deleteDraftApplication(appId);
        ParamUtil.setRequestAttr(request, "AFTER_DELETE_DRAFT_APP", Boolean.TRUE);
    }


    private InboxAppSearchDto getSearchDto(HttpServletRequest request) {
        InboxAppSearchDto searchDto = (InboxAppSearchDto) ParamUtil.getSessionAttr(request, KEY_INBOX_APP_SEARCH_DTO);
        return searchDto == null ? getDefaultSearchDto() : searchDto;
    }

    private InboxAppSearchDto getDefaultSearchDto() {
        InboxAppSearchDto dto = new InboxAppSearchDto();
        dto.defaultPaging();
        return dto;
    }

    private static InboxAppSearchDto bindModel(HttpServletRequest request, InboxAppSearchDto dto) {
        dto.setSearchAppNo(request.getParameter("searchAppNo"));
        dto.setSearchProcessType(request.getParameter("searchProcessType"));
        dto.setSearchStatus(request.getParameter("searchStatus"));
        dto.setSearchSubmissionDateFrom(request.getParameter("searchSubmissionDateFrom"));
        dto.setSearchSubmissionDateTo(request.getParameter("searchSubmissionDateTo"));
        return dto;
    }
}
