package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
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
import sg.gov.moh.iais.egp.bsb.service.BsbInboxService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_INBOX_DATA_SUBMISSION;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_INTERNAL_INBOX;
import static sg.gov.moh.iais.egp.bsb.constant.ResponseConstants.ERROR_CODE_VALIDATION_FAIL;
import static sg.gov.moh.iais.egp.bsb.constant.ResponseConstants.ERROR_INFO_ERROR_MSG;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_INBOX_DATA_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_ADDITIONAL;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_VALUE;

@Slf4j
@Delegator("bsbInboxDataSubDelegator")
public class BsbInboxDataSubDelegator {
    private static final String KEY_INBOX_DATA_SUB_SEARCH_DTO = "inboxDataSubmissionSearchDto";
    private static final String KEY_INBOX_APP_PAGE_INFO = "pageInfo";

    private final BsbInboxClient inboxClient;
    private final BsbInboxService inboxService;

    @Autowired
    public BsbInboxDataSubDelegator(BsbInboxClient inboxClient, BsbInboxService inboxService) {
        this.inboxClient = inboxClient;
        this.inboxService = inboxService;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_INBOX_DATA_SUB_SEARCH_DTO);

        AuditTrailHelper.auditFunction(MODULE_INTERNAL_INBOX, FUNCTION_INBOX_DATA_SUBMISSION);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        selectOption(request);
        // get search DTO
        InboxDataSubSearchDto searchDto = getSearchDto(request);
        ParamUtil.setSessionAttr(request, KEY_INBOX_DATA_SUB_SEARCH_DTO, searchDto);

        // call API to get dashboard data
        inboxService.retrieveDashboardData(request);

        // call API to get searched data
        ResponseDto<InboxDataSubResultDto> resultDto = inboxClient.getInboxDataSubmission(searchDto);
        if (resultDto.ok()) {
            ParamUtil.setRequestAttr(request, KEY_INBOX_APP_PAGE_INFO, resultDto.getEntity().getPageInfo());
            ParamUtil.setRequestAttr(request, KEY_INBOX_DATA_LIST, resultDto.getEntity().getDataSubInfos());
        } else {
            log.warn("Search Inbox Data Submission Fail");
            ParamUtil.setRequestAttr(request, KEY_INBOX_APP_PAGE_INFO, PageInfo.emptyPageInfo(searchDto));
            ParamUtil.setRequestAttr(request, KEY_INBOX_DATA_LIST, new ArrayList<>());
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
        String sortType = ParamUtil.getString(request, KEY_ACTION_ADDITIONAL);
        searchDto.changeSort(field, sortType);
        ParamUtil.setSessionAttr(request, KEY_INBOX_DATA_SUB_SEARCH_DTO, searchDto);
    }

    public void page(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InboxDataSubSearchDto searchDto = getSearchDto(request);
        BsbInboxService.page(request, searchDto);
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
