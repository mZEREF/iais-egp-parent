package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.BsbInboxClient;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxAppSearchDto;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxAppPageInfoResultDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.service.BsbInboxService;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_INBOX_APPLICATION_FACILITY_ADMIN;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_INTERNAL_INBOX;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.INBOX_APP_SEARCH_STATUS_FAC;
import static sg.gov.moh.iais.egp.bsb.constant.ResponseConstants.ERROR_CODE_VALIDATION_FAIL;
import static sg.gov.moh.iais.egp.bsb.constant.ResponseConstants.ERROR_INFO_ERROR_MSG;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_APP_STATUS_OPS;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_APP_TYPE_OPS;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_INBOX_APP_SEARCH_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_INBOX_DATA_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_INBOX_PAGE_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_PROCESS_TYPE_OPS;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_SEARCH_APP_NO;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_SEARCH_APP_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_SEARCH_FACILITY_NAME;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_SEARCH_PROCESS_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_SEARCH_STATUS;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_SEARCH_SUBMISSION_DATE_FROM;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_SEARCH_SUBMISSION_DATE_TO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_ADDITIONAL;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_VALUE;


@Slf4j
@Delegator("bsbInboxAppDelegator")
public class BsbInboxAppDelegator {
    private final BsbInboxClient inboxClient;
    private final BsbInboxService inboxService;

    @Autowired
    public BsbInboxAppDelegator(BsbInboxClient inboxClient, BsbInboxService inboxService) {
        this.inboxClient = inboxClient;
        this.inboxService = inboxService;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_INBOX_APP_SEARCH_DTO);
        AuditTrailHelper.auditFunction(MODULE_INTERNAL_INBOX, FUNCTION_INBOX_APPLICATION_FACILITY_ADMIN);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        //dashboard get draft
        InboxAppSearchDto searchDto = getSearchDto(request);
        String searchStatus = request.getParameter(KEY_SEARCH_STATUS);
        if(StringUtils.hasLength(searchStatus)){
            searchDto.setSearchStatus(searchStatus);
        }

        ParamUtil.setSessionAttr(request, KEY_INBOX_APP_SEARCH_DTO, searchDto);

        // call API to get dashboard data
        inboxService.retrieveDashboardData(request);
        // call API to get searched data
        ResponseDto<InboxAppPageInfoResultDto> resultDto = inboxClient.getInboxApplication(searchDto);
        if (resultDto.ok()) {
            ParamUtil.setRequestAttr(request, KEY_INBOX_PAGE_INFO, resultDto.getEntity().getPageInfo());
            ParamUtil.setRequestAttr(request, KEY_INBOX_DATA_LIST, resultDto.getEntity().getApplications());
            ParamUtil.setRequestAttr(request,"facilityNoNameMap", resultDto.getEntity().getFacilityNoNameMap());
        } else {
            log.warn("Search Inbox Application Fail");
            ParamUtil.setRequestAttr(request, KEY_INBOX_PAGE_INFO, PageInfo.emptyPageInfo(searchDto));
            ParamUtil.setRequestAttr(request, KEY_INBOX_DATA_LIST, new ArrayList<>());
            if(ERROR_CODE_VALIDATION_FAIL.equals(resultDto.getErrorCode())) {
                ParamUtil.setRequestAttr(request, ERROR_INFO_ERROR_MSG, ValidationResultDto.toErrorMsg(resultDto.getErrorInfos().get(ERROR_INFO_ERROR_MSG)));
            }
        }

        List<SelectOption> processTypeOps = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_BSB_PRO_TYPE);
        ParamUtil.setRequestAttr(request, KEY_PROCESS_TYPE_OPS, processTypeOps);
        // inbox search app FAC status
        ParamUtil.setRequestAttr(request, KEY_APP_STATUS_OPS, INBOX_APP_SEARCH_STATUS_FAC);
        List<SelectOption> appTypeOps = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_BSB_APP_TYPE);
        ParamUtil.setRequestAttr(request, KEY_APP_TYPE_OPS, appTypeOps);
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
        BsbInboxService.page(request, searchDto);
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
        dto.setSearchAppNo(request.getParameter(KEY_SEARCH_APP_NO));
        dto.setSearchProcessType(request.getParameter(KEY_SEARCH_PROCESS_TYPE));
        dto.setSearchFacilityName(request.getParameter(KEY_SEARCH_FACILITY_NAME));
        dto.setSearchStatus(request.getParameter(KEY_SEARCH_STATUS));
        dto.setSearchAppType(request.getParameter(KEY_SEARCH_APP_TYPE));
        dto.setSearchSubmissionDateFrom(request.getParameter(KEY_SEARCH_SUBMISSION_DATE_FROM));
        dto.setSearchSubmissionDateTo(request.getParameter(KEY_SEARCH_SUBMISSION_DATE_TO));
        return dto;
    }
}
