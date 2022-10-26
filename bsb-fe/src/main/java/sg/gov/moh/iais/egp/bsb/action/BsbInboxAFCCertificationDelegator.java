package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.client.BsbInboxClient;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxAFCSearchDto;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxCertificationPageInfoResultDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.service.BsbInboxService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_INBOX_CERTIFICATION_AFC_ADMIN;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_INTERNAL_INBOX;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.INBOX_CERTIFICATION_SEARCH_PROCESS_TYPE_AFC;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.INBOX_CERTIFICATION_SEARCH_STATUS_AFC;
import static sg.gov.moh.iais.egp.bsb.constant.ResponseConstants.ERROR_CODE_VALIDATION_FAIL;
import static sg.gov.moh.iais.egp.bsb.constant.ResponseConstants.ERROR_INFO_ERROR_MSG;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_APP_STATUS_OPS;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_INBOX_CERTIFICATION_SEARCH_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_INBOX_DATA_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_INBOX_PAGE_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_PROCESS_TYPE_OPS;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_ADDITIONAL;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_VALUE;


@Delegator("bsbInboxAFCCertificationDelegator")
@Slf4j
@RequiredArgsConstructor
public class BsbInboxAFCCertificationDelegator {
    private final BsbInboxClient inboxClient;
    private final BsbInboxService inboxService;

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_INBOX_CERTIFICATION_SEARCH_DTO);

        AuditTrailHelper.auditFunction(MODULE_INTERNAL_INBOX, FUNCTION_INBOX_CERTIFICATION_AFC_ADMIN);
    }

    public void init(BaseProcessClass bpc) {
        // do nothing now
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        inboxService.retrieveDashboardData(request);
        InboxAFCSearchDto searchDto = getSearchDto(request);
        ParamUtil.setSessionAttr(request, KEY_INBOX_CERTIFICATION_SEARCH_DTO, searchDto);

        ResponseDto<InboxCertificationPageInfoResultDto> resultDto = inboxClient.searchInboxCertificationForAFCAdmin(searchDto);
        if (resultDto.ok()) {
            ParamUtil.setRequestAttr(request, KEY_INBOX_PAGE_INFO, resultDto.getEntity().getPageInfo());
            ParamUtil.setRequestAttr(request, KEY_INBOX_DATA_LIST, resultDto.getEntity().getCertificationReports());
        } else {
            log.warn("Search Inbox Inspections/Certifications Fail");
            ParamUtil.setRequestAttr(request, KEY_INBOX_PAGE_INFO, PageInfo.emptyPageInfo(searchDto));
            ParamUtil.setRequestAttr(request, KEY_INBOX_DATA_LIST, new ArrayList<>());
            if(ERROR_CODE_VALIDATION_FAIL.equals(resultDto.getErrorCode())) {
                ParamUtil.setRequestAttr(request, ERROR_INFO_ERROR_MSG, ValidationResultDto.toErrorMsg(resultDto.getErrorInfos().get(ERROR_INFO_ERROR_MSG)));
            }
        }

        ParamUtil.setRequestAttr(request, KEY_PROCESS_TYPE_OPS, INBOX_CERTIFICATION_SEARCH_PROCESS_TYPE_AFC);
        ParamUtil.setRequestAttr(request, KEY_APP_STATUS_OPS, INBOX_CERTIFICATION_SEARCH_STATUS_AFC);
    }

    public void bindAction(BaseProcessClass bpc) {
        // do nothing now
    }

    public void search(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        InboxAFCSearchDto searchDto = getSearchDto(request);
        searchDto.reqObjMapping(request);
        searchDto.setPage(0);
        ParamUtil.setSessionAttr(request, KEY_INBOX_CERTIFICATION_SEARCH_DTO, searchDto);
    }


    public void sort(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InboxAFCSearchDto searchDto = getSearchDto(request);
        String field = ParamUtil.getString(request, KEY_ACTION_VALUE);
        String sortType = ParamUtil.getString(request, KEY_ACTION_ADDITIONAL);
        searchDto.changeSort(field, sortType);
        ParamUtil.setSessionAttr(request, KEY_INBOX_CERTIFICATION_SEARCH_DTO, searchDto);
    }

    public void page(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InboxAFCSearchDto searchDto = getSearchDto(request);
        BsbInboxService.page(request, searchDto);
        ParamUtil.setSessionAttr(request, KEY_INBOX_CERTIFICATION_SEARCH_DTO, searchDto);
    }

    private InboxAFCSearchDto getSearchDto(HttpServletRequest request) {
        InboxAFCSearchDto searchDto = (InboxAFCSearchDto) ParamUtil.getSessionAttr(request, KEY_INBOX_CERTIFICATION_SEARCH_DTO);
        return searchDto == null ? getDefaultSearchDto() : searchDto;
    }

    private InboxAFCSearchDto getDefaultSearchDto() {
        InboxAFCSearchDto dto = new InboxAFCSearchDto();
        dto.defaultPaging("application.modifiedAt",true);
        return dto;
    }
}
