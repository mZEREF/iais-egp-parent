package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.client.AdhocRfiClient;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.audit.AuditQueryDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.AdhocRfiDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.AdhocRfiQueryDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.AdhocRfiQueryResultDto;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.PARAM_AUDIT_SEARCH;

@Slf4j
@Delegator("bsbAdhocRfiDelegator")
public class BsbAdhocRfiDelegator {
    private static final String ACTION_TYPE = "action_type";
    public static final String ACTION_VALUE = "action_value";
    public static final String ACTION_ADDT = "action_additional";
    private static final String KEY_ADHOC_RFI_LIST = "infoList";
    public static final String KEY_ADHOC_LIST_SEARCH_DTO = "adhocSearchDto";
    public static final String KEY_ADHOC_PAGE_INFO = "pageInfo";
    private final AdhocRfiClient adhocRfiClient;


    public BsbAdhocRfiDelegator(AdhocRfiClient adhocRfiClient) {
        this.adhocRfiClient = adhocRfiClient;
    }

    public void doStart(BaseProcessClass bpc){
        log.info("=======>>>>>start>>>>>>>>>>>>>>>>bsbAdhocRfiDelegator");
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, KEY_ADHOC_LIST_SEARCH_DTO, null);

    }
    /**
     * preAdhocRfi
     */
    public void preAdhocRfi(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, KEY_ADHOC_RFI_LIST, null);


        AdhocRfiQueryDto searchDto = getSearchDto(request);
        ParamUtil.setSessionAttr(request, KEY_ADHOC_LIST_SEARCH_DTO, searchDto);
        ResponseDto<AdhocRfiQueryResultDto> resultDto = adhocRfiClient.queryAdhocRfi(searchDto);
        if (resultDto.ok()) {
            ParamUtil.setRequestAttr(request, KEY_ADHOC_PAGE_INFO, resultDto.getEntity().getPageInfo());
            List<AdhocRfiDto> reqInfos = resultDto.getEntity().getRfiList();
            ParamUtil.setSessionAttr(request, KEY_ADHOC_RFI_LIST, (Serializable) reqInfos);
        } else {
            log.warn("get adhocRfi API doesn't return ok, the response is {}", resultDto);
            ParamUtil.setRequestAttr(request, KEY_ADHOC_PAGE_INFO, PageInfo.emptyPageInfo(searchDto));
            ParamUtil.setRequestAttr(request, KEY_ADHOC_RFI_LIST, new ArrayList<>());
        }
    }
    /**
     * doAdhocRfi
     */
    public void doAdhocRfi(BaseProcessClass bpc){
        log.info("=======>>>>>start>>>>>>>>>>>>>>>>bsbAdhocRfiDelegator");
        String actionType =  ParamUtil.getRequestString(bpc.request, ACTION_TYPE);
        log.info(StringUtil.changeForLog("----- Action Type: " + actionType + " -----"));
        String facilityNo =ParamUtil.getString(bpc.request,"searchNo");
        AdhocRfiQueryDto searchDto = getSearchDto(bpc.request);
        searchDto.setFacilityNo(facilityNo);
        ParamUtil.setSessionAttr(bpc.request, KEY_ADHOC_LIST_SEARCH_DTO, searchDto);


    }
    /**
     * doPaging
     */
    public void doPaging(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AdhocRfiQueryDto searchDto = getSearchDto(request);
        String actionValue = ParamUtil.getString(request, ACTION_VALUE);
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
        ParamUtil.setSessionAttr(request, KEY_ADHOC_LIST_SEARCH_DTO, searchDto);
    }
    /**
     * doSort
     */
    public void doSort(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AdhocRfiQueryDto searchDto = getSearchDto(request);
        String field = ParamUtil.getString(request, ACTION_VALUE);
        String sortType = ParamUtil.getString(request, ACTION_ADDT);
        searchDto.changeSort(field, sortType);
        ParamUtil.setSessionAttr(request, KEY_ADHOC_LIST_SEARCH_DTO, searchDto);
    }
    /**
     * doSort
     */
    public void preNewAdhocRfi(BaseProcessClass bpc){

    }
    public void doGreateRfi(){

    }
    public void doUpdate(){

    }
    public void preViewAdhocRfi(){

    }
    public void doCancel(){

    }

    private AdhocRfiQueryDto getSearchDto(HttpServletRequest request) {
        AdhocRfiQueryDto searchDto = (AdhocRfiQueryDto) ParamUtil.getSessionAttr(request, KEY_ADHOC_LIST_SEARCH_DTO);
        return searchDto == null ? getDefaultSearchDto() : searchDto;
    }

    private AdhocRfiQueryDto getDefaultSearchDto() {
        AdhocRfiQueryDto dto = new AdhocRfiQueryDto();
        dto.defaultPaging();
        return dto;
    }
}
