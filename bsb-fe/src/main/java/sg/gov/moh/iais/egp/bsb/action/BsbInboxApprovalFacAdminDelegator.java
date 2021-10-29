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
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxApprovalFacAdminResultDto;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxApprovalSearchDto;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.ResponseConstants.ERROR_CODE_VALIDATION_FAIL;
import static sg.gov.moh.iais.egp.bsb.constant.ResponseConstants.ERROR_INFO_ERROR_MSG;

@Slf4j
@Delegator("bsbInboxApprovalFacAdminDelegator")
public class BsbInboxApprovalFacAdminDelegator {
    private static final String KEY_INBOX_APPROVAL_SEARCH_DTO = "inboxApprovalSearchDto";
    private static final String KEY_INBOX_APPROVAL_PAGE_INFO = "pageInfo";
    private static final String KEY_INBOX_APPROVAL_DATA_LIST = "dataList";
    private static final String KEY_INBOX_MSG_UNREAD_AMT = "unreadMsgAmt";

    private static final String KEY_ACTION_TYPE = "action_type";
    private static final String KEY_ACTION_VALUE = "action_value";
    private static final String KEY_ACTION_ADDT = "action_additional";

    private static final String KEY_PAGE_SIZE = "pageJumpNoPageSize";
    private static final String KEY_PAGE_NO = "pageJumpNoTextchangePage";

    private final BsbInboxClient inboxClient;

    @Autowired
    public BsbInboxApprovalFacAdminDelegator(BsbInboxClient inboxClient) {
        this.inboxClient = inboxClient;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_INBOX_APPROVAL_SEARCH_DTO);

        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_INTERNAL_INBOX, AuditTrailConsts.FUNCTION_INBOX);
    }

    public void init(BaseProcessClass bpc) {
        // do nothing
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;

        InboxApprovalSearchDto searchDto = getSearchDto(request);
        ParamUtil.setSessionAttr(request, KEY_INBOX_APPROVAL_SEARCH_DTO, searchDto);

        ResponseDto<InboxApprovalFacAdminResultDto> resultDto = inboxClient.getInboxApprovalForFacAdmin(searchDto);
        if (resultDto.ok()) {
            ParamUtil.setRequestAttr(request, KEY_INBOX_APPROVAL_PAGE_INFO, resultDto.getEntity().getPageInfo());
            ParamUtil.setRequestAttr(request, KEY_INBOX_APPROVAL_DATA_LIST, resultDto.getEntity().getApprovalInfos());
            ParamUtil.setRequestAttr(request, KEY_INBOX_MSG_UNREAD_AMT, resultDto.getEntity().getUnreadMsgAmt());
        } else {
            log.warn("Search Inbox Approval for Facility Administrator Fail");
            ParamUtil.setRequestAttr(request, KEY_INBOX_APPROVAL_PAGE_INFO, PageInfo.emptyPageInfo(searchDto));
            ParamUtil.setRequestAttr(request, KEY_INBOX_APPROVAL_DATA_LIST, new ArrayList<>());
            ParamUtil.setRequestAttr(request, KEY_INBOX_MSG_UNREAD_AMT, 0);
            if(ERROR_CODE_VALIDATION_FAIL.equals(resultDto.getErrorCode())) {
                ParamUtil.setRequestAttr(request, ERROR_INFO_ERROR_MSG, ValidationResultDto.toErrorMsg(resultDto.getErrorInfos().get(ERROR_INFO_ERROR_MSG)));
            }
        }



        List<SelectOption> processTypeOps = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_BSB_PRO_TYPE);
        ParamUtil.setRequestAttr(request, "processTypeOps", processTypeOps);
        List<SelectOption> approvalStatusOps = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_BSB_APPROVAL_STATUS);
        ParamUtil.setRequestAttr(request, "approvalStatusOps", approvalStatusOps);
    }

    public void search(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InboxApprovalSearchDto searchDto = bindModel(request);
        searchDto.setPage(0);
        ParamUtil.setSessionAttr(request, KEY_INBOX_APPROVAL_SEARCH_DTO, searchDto);
    }

    public void sort(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InboxApprovalSearchDto searchDto = getSearchDto(request);
        String field = ParamUtil.getString(request, KEY_ACTION_VALUE);
        String sortType = ParamUtil.getString(request, KEY_ACTION_ADDT);
        searchDto.changeSort(field, sortType);
        ParamUtil.setSessionAttr(request, KEY_INBOX_APPROVAL_SEARCH_DTO, searchDto);
    }

    public void page(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InboxApprovalSearchDto searchDto = getSearchDto(request);
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
        ParamUtil.setSessionAttr(request, KEY_INBOX_APPROVAL_SEARCH_DTO, searchDto);
    }


    private InboxApprovalSearchDto getSearchDto(HttpServletRequest request) {
        InboxApprovalSearchDto searchDto = (InboxApprovalSearchDto) ParamUtil.getSessionAttr(request, KEY_INBOX_APPROVAL_SEARCH_DTO);
        return searchDto == null ? getDefaultSearchDto() : searchDto;
    }

    private InboxApprovalSearchDto getDefaultSearchDto() {
        InboxApprovalSearchDto searchDto = new InboxApprovalSearchDto();
        searchDto.defaultPaging();
        return searchDto;
    }

    private InboxApprovalSearchDto bindModel(HttpServletRequest request) {
        InboxApprovalSearchDto dto = getSearchDto(request);
        dto.setSearchApprovalNo(request.getParameter("searchApprovalNo"));
        dto.setSearchProcessType(request.getParameter("searchProcessType"));
        dto.setSearchStatus(request.getParameter("searchStatus"));
        dto.setSearchStartDateFrom(request.getParameter("searchStartDateFrom"));
        dto.setSearchStartDateTo(request.getParameter("searchStartDateTo"));
        dto.setSearchExpiryDateFrom(request.getParameter("searchExpiryDateFrom"));
        dto.setSearchExpiryDateTo(request.getParameter("searchExpiryDateTo"));
        return dto;
    }
}
