package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.BsbInboxClient;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxApprovalSearchDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxApprovalAFCResultDto;
import sg.gov.moh.iais.egp.bsb.service.BsbInboxService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_INBOX_APPROVAL_AFC_ADMIN;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_INTERNAL_INBOX;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.PROCESS_TYPE_FAC_CERTIFIER_REG;
import static sg.gov.moh.iais.egp.bsb.constant.ResponseConstants.ERROR_CODE_VALIDATION_FAIL;
import static sg.gov.moh.iais.egp.bsb.constant.ResponseConstants.ERROR_INFO_ERROR_MSG;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_INBOX_APPROVAL_SEARCH_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_INBOX_DATA_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_INBOX_PAGE_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_SEARCH_APPROVAL_NO;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_SEARCH_EXPIRY_DATE_FROM;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_SEARCH_EXPIRY_DATE_TO;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_SEARCH_PROCESS_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_SEARCH_START_DATE_FROM;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_SEARCH_START_DATE_TO;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_STATUS;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_ADDITIONAL;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_VALUE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_PAGE_NO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_PAGE_SIZE;


@Slf4j
@Delegator("bsbInboxApprovalAFCDelegator")
public class BsbInboxApprovalAFCDelegator {
    private final BsbInboxClient inboxClient;
    private final BsbInboxService inboxService;

    @Autowired
    public BsbInboxApprovalAFCDelegator(BsbInboxClient inboxClient, BsbInboxService inboxService) {
        this.inboxClient = inboxClient;
        this.inboxService = inboxService;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_INBOX_APPROVAL_SEARCH_DTO);
        AuditTrailHelper.auditFunction(MODULE_INTERNAL_INBOX, FUNCTION_INBOX_APPROVAL_AFC_ADMIN);
    }

    public void init(BaseProcessClass bpc) {
        // do nothing
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;

        InboxApprovalSearchDto searchDto = getSearchDto(request);
        ParamUtil.setSessionAttr(request, KEY_INBOX_APPROVAL_SEARCH_DTO, searchDto);

        // call API to get dashboard data

        inboxService.retrieveDashboardData(request);

        ResponseDto<InboxApprovalAFCResultDto> resultDto = inboxClient.getInboxApprovalForAFCAdmin(searchDto);
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

        List<SelectOption> processTypeOps = new ArrayList<>(1);
        processTypeOps.add(new SelectOption(PROCESS_TYPE_FAC_CERTIFIER_REG, "Facility Certifier Registration"));
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
        String sortType = ParamUtil.getString(request, KEY_ACTION_ADDITIONAL);
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
        dto.setSearchApprovalNo(request.getParameter(KEY_SEARCH_APPROVAL_NO));
        dto.setSearchProcessType(request.getParameter(KEY_SEARCH_PROCESS_TYPE));
        dto.setSearchStartDateFrom(request.getParameter(KEY_SEARCH_START_DATE_FROM));
        dto.setSearchStartDateTo(request.getParameter(KEY_SEARCH_START_DATE_TO));
        dto.setSearchExpiryDateFrom(request.getParameter(KEY_SEARCH_EXPIRY_DATE_FROM));
        dto.setSearchExpiryDateTo(request.getParameter(KEY_SEARCH_EXPIRY_DATE_TO));
        dto.setSearchStatus(request.getParameter(KEY_STATUS));
        return dto;
    }
}
