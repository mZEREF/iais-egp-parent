package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.BsbInboxClient;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxRepResultDto;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxRepSearchDto;
import sg.gov.moh.iais.egp.bsb.service.BsbInboxService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_INBOX_REPORTABLE_EVENT;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_INTERNAL_INBOX;

@Delegator("bsbInboxIncidentDelegator")
@Slf4j
public class BsbInboxIncidentDelegator {
    private static final String KEY_REPORTABLE_EVENT_SEARCH_PARAMETER_DTO = "inboxSearchDto";
    private static final String KEY_REPORTABLE_EVENT_RESULT_DTO = "inboxResultDto";
    private static final String KEY_PAGE_INFO = "pageInfo";
    private final BsbInboxClient inboxClient;
    private final BsbInboxService inboxService;

    @Autowired
    public BsbInboxIncidentDelegator(BsbInboxClient inboxClient, BsbInboxService inboxService) {
        this.inboxClient = inboxClient;
        this.inboxService = inboxService;
    }


    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_REPORTABLE_EVENT_SEARCH_PARAMETER_DTO);
        AuditTrailHelper.auditFunction(MODULE_INTERNAL_INBOX, FUNCTION_INBOX_REPORTABLE_EVENT);
    }


    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        inboxService.retrieveDashboardData(request);
        //do search
        InboxRepSearchDto inboxRepSearchDto = getInboxRepSearchDto(request);
        InboxRepResultDto resultDto = inboxClient.searchInboxReportableEvent(inboxRepSearchDto).getEntity();
        ParamUtil.setRequestAttr(request,KEY_REPORTABLE_EVENT_RESULT_DTO,resultDto.getDataIncidentDtoList());
        ParamUtil.setRequestAttr(request,KEY_PAGE_INFO,resultDto.getPageInfo());
    }

    public void handleData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InboxRepSearchDto dto = getInboxRepSearchDto(request);
        //clear template  search parameter
        dto.clear();
        //retrieve info from jsp
        dto.reqObjMapping(request);
        ParamUtil.setSessionAttr(request,KEY_REPORTABLE_EVENT_SEARCH_PARAMETER_DTO,dto);
    }

    public InboxRepSearchDto getInboxRepSearchDto(HttpServletRequest request){
        InboxRepSearchDto dto = (InboxRepSearchDto) ParamUtil.getSessionAttr(request,KEY_REPORTABLE_EVENT_SEARCH_PARAMETER_DTO);
        return dto == null?new InboxRepSearchDto():dto;
    }
}
