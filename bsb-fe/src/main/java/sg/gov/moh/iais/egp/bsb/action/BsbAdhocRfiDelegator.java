package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.constant.reqForInfo.RequestForInformationConstants;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sg.gov.moh.iais.egp.bsb.client.AdhocRfiClient;
import sg.gov.moh.iais.egp.bsb.dto.adhocRfi.AdhocRfiDto;
import sg.gov.moh.iais.egp.bsb.dto.adhocRfi.AdhocRfiQueryDto;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
        adhocRfiDto.setSubmissionType("adhocRfi.getSubmissionType()");
        adhocRfiDto.setSupportingDocRequired(true);
        adhocRfiDto.setStartDate(LocalDate.now());
        reqInfos.add(adhocRfiDto);
        ParamUtil.setSessionAttr(request, KEY_ADHOC_RFI_LIST, (Serializable) reqInfos);

//        AdhocRfiQueryDto searchDto = getSearchDto(request);
//        ParamUtil.setSessionAttr(request, KEY_ADHOC_LIST_SEARCH_DTO, searchDto);
//        ResponseDto<AdhocRfiQueryResultDto> resultDto = adhocRfiClient.queryAdhocRfi(searchDto);
//        if (resultDto.ok()) {
//            ParamUtil.setRequestAttr(request, KEY_ADHOC_PAGE_INFO, resultDto.getEntity().getPageInfo());
//            List<AdhocRfiDto> reqForInfos = resultDto.getEntity().getRfiList();
//            for (AdhocRfiDto adRfi:reqForInfos
//                 ) {
//                ApplicationDto applicationDto=adhocRfiClient.getApplicationDtoByAppId(adRfi.getApplicationId()).getEntity();
//                adRfi.setApplicationDto(applicationDto);
//
//            }
//            ParamUtil.setSessionAttr(request, KEY_ADHOC_RFI_LIST, (Serializable) reqForInfos);
//        } else {
//            log.warn("get adhocRfi API doesn't return ok, the response is {}", resultDto);
//            ParamUtil.setRequestAttr(request, KEY_ADHOC_PAGE_INFO, PageInfo.emptyPageInfo(searchDto));
//            ParamUtil.setRequestAttr(request, KEY_ADHOC_RFI_LIST, new ArrayList<>());
//        }

    }

    public void preAdhocRfiDetail(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AdhocRfiDto adhocRfiDto= (AdhocRfiDto) ParamUtil.getSessionAttr(request,"adhocReqForInfoDto");
        try {
            String id =  ParamUtil.getMaskedString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
            if(!StringUtil.isEmpty(id)){
                adhocRfiDto=adhocRfiClient.getAdhocRfiById(id).getEntity();

            }
        }catch (Exception e){
            log.error("not mask id");
        }
        adhocRfiDto = new AdhocRfiDto();
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
        adhocRfiDto.setSubmissionType("adhocRfi.getSubmissionType()");
        adhocRfiDto.setSupportingDocRequired(true);
        adhocRfiDto.setStartDate(LocalDate.now());
        ParamUtil.setSessionAttr(request,"adhocReqForInfoDto",adhocRfiDto);


    }
    public void next(BaseProcessClass bpc) {
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);

        String crudActionType = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);

        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, crudActionType);

    }

    public void submintAdhocRfi(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);

        String crudActionType = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);

        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, crudActionType);

        AdhocRfiDto adhocRfiDto=(AdhocRfiDto) ParamUtil.getSessionAttr(bpc.request,"adhocReqForInfoDto");


        ParamUtil.setSessionAttr(bpc.request,"adhocReqForInfoDto",adhocRfiDto);

        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "Y");
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        if(adhocRfiDto.getInformationRequired()){
            String userReply=mulReq.getParameter("userReply");
            adhocRfiDto.setSuppliedInformation(userReply);
            if(StringUtil.isEmpty(userReply)){
                errorMap.put("userReply", MessageUtil.replaceMessage("GENERAL_ERR0006","Information","field"));
            }else if(userReply.length()>1000){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("number","1000");
                repMap.put("fieldNo","Information");
                errorMap.put("userReply",MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap));

            }
        }
        if(adhocRfiDto.getSupportingDocRequired()){

        }
        if (!errorMap.isEmpty()) {
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "N");
            //
            return;
        }
        adhocRfiDto.setStatus(RequestForInformationConstants.RFI_CLOSE);
       // adhocRfiClient.saveAdhocRfi(adhocRfiDto);
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
