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
import sg.gov.moh.iais.egp.bsb.constant.AuditConstants;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxDataSubResultDto;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxDataSubSearchDto;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.ResponseConstants.ERROR_CODE_VALIDATION_FAIL;
import static sg.gov.moh.iais.egp.bsb.constant.ResponseConstants.ERROR_INFO_ERROR_MSG;

@Slf4j
@Delegator("bsbInboxDataSubDelegator")
public class BsbInboxDataSubDelegator {
    private static final String KEY_INBOX_DATA_SUB_SEARCH_DTO = "inboxDataSubmissionSearchDto";
    private static final String KEY_INBOX_APP_PAGE_INFO = "pageInfo";
    private static final String KEY_INBOX_APP_DATA_LIST = "dataList";

    private static final String KEY_ACTION_VALUE = "action_value";
    private static final String KEY_ACTION_ADDT = "action_additional";

    private static final String KEY_PAGE_SIZE = "pageJumpNoPageSize";
    private static final String KEY_PAGE_NO = "pageJumpNoTextchangePage";

    private final BsbInboxClient inboxClient;

    @Autowired
    public BsbInboxDataSubDelegator(BsbInboxClient inboxClient) {
        this.inboxClient = inboxClient;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_INBOX_DATA_SUB_SEARCH_DTO);

        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_INTERNAL_INBOX, "Inbox Data Submission");
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        selectOption(request);
        // get search DTO
        InboxDataSubSearchDto searchDto = getSearchDto(request);
        ParamUtil.setSessionAttr(request, KEY_INBOX_DATA_SUB_SEARCH_DTO, searchDto);

        // call API to get searched data
        ResponseDto<InboxDataSubResultDto> resultDto = inboxClient.getInboxDataSubmission(searchDto);
        if (resultDto.ok()) {
            ParamUtil.setRequestAttr(request, KEY_INBOX_APP_PAGE_INFO, resultDto.getEntity().getPageInfo());
            ParamUtil.setRequestAttr(request, KEY_INBOX_APP_DATA_LIST, resultDto.getEntity().getDataSubInfos());
        } else {
            log.warn("Search Inbox Data Submission Fail");
            ParamUtil.setRequestAttr(request, KEY_INBOX_APP_PAGE_INFO, PageInfo.emptyPageInfo(searchDto));
            ParamUtil.setRequestAttr(request, KEY_INBOX_APP_DATA_LIST, new ArrayList<>());
            if(ERROR_CODE_VALIDATION_FAIL.equals(resultDto.getErrorCode())) {
                ParamUtil.setRequestAttr(request, ERROR_INFO_ERROR_MSG, resultDto.getErrorInfos().get(ERROR_INFO_ERROR_MSG));
            }
        }

        List<SelectOption> typeOps = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_BSB_DATA_SUBMISSION_TYPE);
        ParamUtil.setRequestAttr(request, "submissionTypeOps", typeOps);
        List<SelectOption> statusOps = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_BSB_DATA_SUBMISSION_STATUS);
        ParamUtil.setRequestAttr(request, "submissionStatusOps", statusOps);
    }

    public void search(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InboxDataSubSearchDto searchDto = bindModel(request, getSearchDto(request));
        searchDto.setPage(0);
        ParamUtil.setSessionAttr(request, KEY_INBOX_DATA_SUB_SEARCH_DTO, searchDto);
    }

    public void sort(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InboxDataSubSearchDto searchDto = getSearchDto(request);
        String field = ParamUtil.getString(request, KEY_ACTION_VALUE);
        String sortType = ParamUtil.getString(request, KEY_ACTION_ADDT);
        searchDto.changeSort(field, sortType);
        ParamUtil.setSessionAttr(request, KEY_INBOX_DATA_SUB_SEARCH_DTO, searchDto);
    }

    public void page(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InboxDataSubSearchDto searchDto = getSearchDto(request);
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
        ParamUtil.setSessionAttr(request, KEY_INBOX_DATA_SUB_SEARCH_DTO, searchDto);
    }

    private InboxDataSubSearchDto getSearchDto(HttpServletRequest request) {
        InboxDataSubSearchDto searchDto = (InboxDataSubSearchDto) ParamUtil.getSessionAttr(request, KEY_INBOX_DATA_SUB_SEARCH_DTO);
        return searchDto == null ? getDefaultSearchDto() : searchDto;
    }

    private InboxDataSubSearchDto getDefaultSearchDto() {
        InboxDataSubSearchDto dto = new InboxDataSubSearchDto();
        dto.defaultPaging();
        return dto;
    }

    private static InboxDataSubSearchDto bindModel(HttpServletRequest request, InboxDataSubSearchDto dto) {
        dto.setSearchDataSubNo(request.getParameter("searchDataSubNo"));
        dto.setSearchFacName(request.getParameter("searchFacilityName"));
        dto.setSearchStatus(request.getParameter("searchStatus"));
        dto.setSearchType(request.getParameter("searchType"));
        return dto;
    }

    public void selectOption(HttpServletRequest request) {
        List<String> facNames = inboxClient.queryDistinctFN().getEntity();
        List<SelectOption> selectModel = new ArrayList<>(facNames.size());
        for (String facName : facNames) {
            selectModel.add(new SelectOption(facName, facName));
        }
        ParamUtil.setRequestAttr(request, AuditConstants.PARAM_FACILITY_NAME, selectModel);
    }
}
