package sg.gov.moh.iais.egp.bsb.action;


import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.BsbInboxClient;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxApprovalFacAdminResultDto;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxApprovalSearchDto;
import sg.gov.moh.iais.egp.bsb.service.BsbInboxService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_INBOX_APPROVAL_FACILITY_ADMIN;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_INTERNAL_INBOX;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.INBOX_APPROVAL_SEARCH_PROCESS_TYPE_FAC;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.INBOX_APPROVAL_SEARCH_STATUS_FAC;
import static sg.gov.moh.iais.egp.bsb.constant.ResponseConstants.ERROR_CODE_VALIDATION_FAIL;
import static sg.gov.moh.iais.egp.bsb.constant.ResponseConstants.ERROR_INFO_ERROR_MSG;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_APPROVAL_STATUS_OPS;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_INBOX_APPROVAL_SEARCH_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_INBOX_DATA_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_INBOX_PAGE_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_PROCESS_TYPE_OPS;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_SEARCH_APPROVAL_NO;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_SEARCH_EXPIRY_DATE_FROM;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_SEARCH_EXPIRY_DATE_TO;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_SEARCH_FACILITY_NAME;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_SEARCH_PROCESS_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_SEARCH_START_DATE_FROM;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_SEARCH_START_DATE_TO;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_SEARCH_STATUS;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_ADDITIONAL;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_VALUE;

@Slf4j
@Delegator("bsbInboxApprovalFacAdminDelegator")
public class BsbInboxApprovalFacAdminDelegator {
    private final BsbInboxClient inboxClient;
    private final BsbInboxService inboxService;

    @Autowired
    public BsbInboxApprovalFacAdminDelegator(BsbInboxClient inboxClient, BsbInboxService inboxService) {
        this.inboxClient = inboxClient;
        this.inboxService = inboxService;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_INBOX_APPROVAL_SEARCH_DTO);
        AuditTrailHelper.auditFunction(MODULE_INTERNAL_INBOX, FUNCTION_INBOX_APPROVAL_FACILITY_ADMIN);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;

        InboxApprovalSearchDto searchDto = getSearchDto(request);
        String searchStatus = request.getParameter(KEY_SEARCH_STATUS);
        if(StringUtils.hasLength(searchStatus)){
            searchDto.setSearchStatus(searchStatus);
        }
        ParamUtil.setSessionAttr(request, KEY_INBOX_APPROVAL_SEARCH_DTO, searchDto);

        // call API to get dashboard data
        inboxService.retrieveDashboardData(request);

        ResponseDto<InboxApprovalFacAdminResultDto> resultDto = inboxClient.getInboxApprovalForFacAdmin(searchDto);
        if (resultDto.ok()) {
            ParamUtil.setRequestAttr(request, KEY_INBOX_PAGE_INFO, resultDto.getEntity().getPageInfo());
            ParamUtil.setRequestAttr(request, KEY_INBOX_DATA_LIST, resultDto.getEntity().getApprovalInfos());
        } else {
            log.warn("Search Inbox Approval for Facility Administrator Fail");
            ParamUtil.setRequestAttr(request, KEY_INBOX_PAGE_INFO, PageInfo.emptyPageInfo(searchDto));
            ParamUtil.setRequestAttr(request, KEY_INBOX_DATA_LIST, new ArrayList<>());
            if(ERROR_CODE_VALIDATION_FAIL.equals(resultDto.getErrorCode())) {
                ParamUtil.setRequestAttr(request, ERROR_INFO_ERROR_MSG, ValidationResultDto.toErrorMsg(resultDto.getErrorInfos().get(ERROR_INFO_ERROR_MSG)));
            }
        }

        ParamUtil.setRequestAttr(request, KEY_PROCESS_TYPE_OPS, INBOX_APPROVAL_SEARCH_PROCESS_TYPE_FAC);
        ParamUtil.setRequestAttr(request, KEY_APPROVAL_STATUS_OPS, INBOX_APPROVAL_SEARCH_STATUS_FAC);
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
        String sortType = ParamUtil.getString(request, KEY_ACTION_ADDITIONAL);
        searchDto.changeSort(field, sortType);
        ParamUtil.setSessionAttr(request, KEY_INBOX_APPROVAL_SEARCH_DTO, searchDto);
    }

    public void page(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InboxApprovalSearchDto searchDto = getSearchDto(request);
        BsbInboxService.page(request, searchDto);
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
        dto.setSearchApprovalNo(request.getParameter(KEY_SEARCH_APPROVAL_NO));
        dto.setSearchProcessType(request.getParameter(KEY_SEARCH_PROCESS_TYPE));
        dto.setSearchFacilityName(request.getParameter(KEY_SEARCH_FACILITY_NAME));
        dto.setSearchStatus(request.getParameter(KEY_SEARCH_STATUS));
        dto.setSearchStartDateFrom(request.getParameter(KEY_SEARCH_START_DATE_FROM));
        dto.setSearchStartDateTo(request.getParameter(KEY_SEARCH_START_DATE_TO));
        dto.setSearchExpiryDateFrom(request.getParameter(KEY_SEARCH_EXPIRY_DATE_FROM));
        dto.setSearchExpiryDateTo(request.getParameter(KEY_SEARCH_EXPIRY_DATE_TO));
        return dto;
    }
}
