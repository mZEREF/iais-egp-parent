package sg.gov.moh.iais.egp.bsb.action;


import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.BsbInboxClient;
import sg.gov.moh.iais.egp.bsb.constant.OptionsConstants;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxFacPageInfoResultInfo;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxFacSearchDto;
import sg.gov.moh.iais.egp.bsb.service.BsbInboxService;
import sg.gov.moh.iais.egp.bsb.util.mastercode.MasterCodeHolder;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_INBOX_FACILITY;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_INTERNAL_INBOX;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_INBOX_FAC_SEARCH_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_ADDITIONAL;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_VALUE;


@Delegator("bsbInboxFacDelegator")
@Slf4j
public class BsbInboxFacilityDelegator {
    private static final String KEY_INBOX_FAC_PAGE_INFO = "pageInfo";
    private static final String KEY_INBOX_FAC_RESULT = "resultDto";

    private final BsbInboxClient inboxClient;
    private final BsbInboxService inboxService;

    @Autowired
    public BsbInboxFacilityDelegator(BsbInboxClient inboxClient, BsbInboxService inboxService) {
        this.inboxClient = inboxClient;
        this.inboxService = inboxService;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_INBOX_FAC_SEARCH_DTO);

        AuditTrailHelper.auditFunction(MODULE_INTERNAL_INBOX, FUNCTION_INBOX_FACILITY);
    }

    public void init(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, OptionsConstants.OPTIONS_FACILITY_STATUS, new ArrayList<>(MasterCodeHolder.APPROVAL_STATUS.allOptions()));
        ParamUtil.setSessionAttr(request,OptionsConstants.OPTIONS_ROLE_IN_FACILITY,new ArrayList<>(MasterCodeHolder.ROLE_IN_FACILITY.allOptions()));
    }

    public void prepareData(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        inboxService.retrieveDashboardData(request);
        InboxFacSearchDto searchDto = getSearchDto(request);
        String facilityStatus = request.getParameter("facilityStatus");
        if(StringUtils.hasLength(facilityStatus)){
            searchDto.setFacilityStatus(facilityStatus);
        }
        InboxFacPageInfoResultInfo pageInfoResultInfo =  inboxClient.searchInboxFacility(searchDto).getEntity();
        ParamUtil.setRequestAttr(request,KEY_INBOX_FAC_PAGE_INFO,pageInfoResultInfo.getPageInfo());
        ParamUtil.setRequestAttr(request,KEY_INBOX_FAC_RESULT,pageInfoResultInfo.getFacResultDtoList());
        ParamUtil.setSessionAttr(request,KEY_INBOX_FAC_SEARCH_DTO,searchDto);
    }

    public void search(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        InboxFacSearchDto searchDto = getSearchDto(request);
        searchDto.reqObjMapping(request);
        searchDto.setPage(0);
        ParamUtil.setSessionAttr(request, KEY_INBOX_FAC_SEARCH_DTO, searchDto);
    }


    public void sort(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InboxFacSearchDto searchDto = getSearchDto(request);
        String field = ParamUtil.getString(request, KEY_ACTION_VALUE);
        String sortType = ParamUtil.getString(request, KEY_ACTION_ADDITIONAL);
        searchDto.changeSort(field, sortType);
        ParamUtil.setSessionAttr(request, KEY_INBOX_FAC_SEARCH_DTO, searchDto);
    }

    public void page(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InboxFacSearchDto searchDto = getSearchDto(request);
        BsbInboxService.page(request, searchDto);
        ParamUtil.setSessionAttr(request, KEY_INBOX_FAC_SEARCH_DTO, searchDto);
    }

    private InboxFacSearchDto getSearchDto(HttpServletRequest request) {
        InboxFacSearchDto searchDto = (InboxFacSearchDto) ParamUtil.getSessionAttr(request, KEY_INBOX_FAC_SEARCH_DTO);
        return searchDto == null ? getDefaultSearchDto() : searchDto;
    }

    private InboxFacSearchDto getDefaultSearchDto() {
        InboxFacSearchDto dto = new InboxFacSearchDto();
        dto.defaultPaging();
        return dto;
    }
}
