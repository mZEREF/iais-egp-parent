package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sg.gov.moh.iais.egp.bsb.client.AdhocRfiClient;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.entity.AdhocRfiDto;
import sg.gov.moh.iais.egp.bsb.entity.AdhocRfiQueryDto;
import sg.gov.moh.iais.egp.bsb.entity.AdhocRfiQueryResultDto;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * BsbAdhocRfiDelegator
 *
 * @author junyu
 * @date 2022/4/15
 */
@Slf4j
@Delegator("bsbAdhicRfiDelegator")
public class BsbAdhocRfiDelegator {


    private static final String KEY_ADHOC_RFI_LIST = "reqForInfoSearchList";
    public static final String KEY_ADHOC_LIST_SEARCH_DTO = "adhocSearchDto";
    public static final String KEY_ADHOC_PAGE_INFO = "pageInfo";
    @Autowired
    private  AdhocRfiClient adhocRfiClient;


    public void start(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do Start start ...."));
        HttpServletRequest request=bpc.request;


    }

    public void preRfiList(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, KEY_ADHOC_RFI_LIST, null);


        AdhocRfiQueryDto searchDto = getSearchDto(request);
        ParamUtil.setSessionAttr(request, KEY_ADHOC_LIST_SEARCH_DTO, searchDto);
        ResponseDto<AdhocRfiQueryResultDto> resultDto = new ResponseDto<>();
        List<AdhocRfiDto> reqInfos = IaisCommonUtils.combineList();
        AdhocRfiDto adhocRfiDto = new AdhocRfiDto();
        adhocRfiDto.setId("adhocRfi.getId()");
        adhocRfiDto.setApplicationId("APP");
        adhocRfiDto.setFacilityNo("adhocRfi.getFacilityNo()");
        adhocRfiDto.setEmail("adhocRfi.getEmail()");
        adhocRfiDto.setStatus("adhocRfi.getStatus()");
        adhocRfiDto.setInformationRequired(true);
        adhocRfiDto.setDueDate(LocalDate.now());
        adhocRfiDto.setTitle("adhocRfi.getTitle()");
        adhocRfiDto.setTitleOfInformationRequired("adhocRfi.getTitleOfInformationRequired()");
        adhocRfiDto.setTitleOfSupportingDocRequired("adhocRfi.getTitleOfSupportingDocRequired()");
        adhocRfiDto.setSuppliedInformation("adhocRfi.getSuppliedInformation()");
        adhocRfiDto.setSubmissionType("adhocRfi.getSubmissionType()");
        adhocRfiDto.setSupportingDocRequired(true);
        adhocRfiDto.setStartDate(LocalDate.now());
        reqInfos.add(adhocRfiDto);
        ParamUtil.setSessionAttr(request, KEY_ADHOC_RFI_LIST, (Serializable) reqInfos);
//        if (resultDto.ok()) {
//            ParamUtil.setRequestAttr(request, KEY_ADHOC_PAGE_INFO, resultDto.getEntity().getPageInfo());
//            List<AdhocRfiDto> reqInfos = resultDto.getEntity().getRfiList();
//            ParamUtil.setSessionAttr(request, KEY_ADHOC_RFI_LIST, (Serializable) reqInfos);
//        } else {
//            log.warn("get adhocRfi API doesn't return ok, the response is {}", resultDto);
//            ParamUtil.setRequestAttr(request, KEY_ADHOC_PAGE_INFO, PageInfo.emptyPageInfo(searchDto));
//            ParamUtil.setRequestAttr(request, KEY_ADHOC_RFI_LIST, new ArrayList<>());
//        }

    }

    public void preAdhocRfiDetail(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AdhocRfiDto adhocRfiDto = new AdhocRfiDto();
        adhocRfiDto.setId("adhocRfi.getId()");
        adhocRfiDto.setApplicationId("APP");
        adhocRfiDto.setFacilityNo("adhocRfi.getFacilityNo()");
        adhocRfiDto.setEmail("adhocRfi.getEmail()");
        adhocRfiDto.setStatus("adhocRfi.getStatus()");
        adhocRfiDto.setInformationRequired(true);
        adhocRfiDto.setDueDate(LocalDate.now());
        adhocRfiDto.setTitle("adhocRfi.getTitle()");
        adhocRfiDto.setTitleOfInformationRequired("adhocRfi.getTitleOfInformationRequired()");
        adhocRfiDto.setTitleOfSupportingDocRequired("adhocRfi.getTitleOfSupportingDocRequired()");
        adhocRfiDto.setSuppliedInformation("adhocRfi.getSuppliedInformation()");
        adhocRfiDto.setSubmissionType("adhocRfi.getSubmissionType()");
        adhocRfiDto.setSupportingDocRequired(true);
        adhocRfiDto.setStartDate(LocalDate.now());
        ParamUtil.setSessionAttr(request,"licPreReqForInfoDto",adhocRfiDto);

    }
    public void next(BaseProcessClass bpc) {
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);

        String crudActionType = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);

        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, crudActionType);

    }

    public void submintAdhocRfi(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "Y");
    }

    public void backList(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;

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
