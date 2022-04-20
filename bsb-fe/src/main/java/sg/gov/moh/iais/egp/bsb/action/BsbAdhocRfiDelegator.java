package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.constant.reqForInfo.RequestForInformationConstants;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.OrgUserManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sg.gov.moh.iais.egp.bsb.client.AdhocRfiClient;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.AdhocRfiDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.AdhocRfiQueryDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.AdhocRfiQueryResultDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.ApplicationDto;
import sg.gov.moh.iais.egp.bsb.entity.Application;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
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
    @Autowired
    private OrgUserManageService orgUserManageService;


    public void start(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do Start start ...."));
        HttpServletRequest request=bpc.request;


    }

    public void preRfiList(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, KEY_ADHOC_RFI_LIST, null);

        AdhocRfiQueryDto searchDto = getSearchDto(request);
        ParamUtil.setSessionAttr(request, KEY_ADHOC_LIST_SEARCH_DTO, searchDto);
        ResponseDto<AdhocRfiQueryResultDto> resultDto = adhocRfiClient.queryAdhocRfi(searchDto);
        if (resultDto.ok()) {
            ParamUtil.setRequestAttr(request, KEY_ADHOC_PAGE_INFO, resultDto.getEntity().getPageInfo());
            List<AdhocRfiDto> reqForInfos = resultDto.getEntity().getRfiList();

            for (AdhocRfiDto adRfi:reqForInfos
                 ) {
                ApplicationDto applicationDto=adhocRfiClient.getApplicationDtoByAppId(adRfi.getApplicationId()).getEntity();
                adRfi.setApplication(MiscUtil.transferEntityDto(applicationDto, Application.class));
                FeUserDto orgUserDto=orgUserManageService.getUserAccount(adRfi.getId());
                adRfi.setRequestor(orgUserDto.getDisplayName());
            }
            ParamUtil.setSessionAttr(request, KEY_ADHOC_RFI_LIST, (Serializable) reqForInfos);
        } else {
            log.warn("get adhocRfi API doesn't return ok, the response is {}", resultDto);
            ParamUtil.setRequestAttr(request, KEY_ADHOC_PAGE_INFO, PageInfo.emptyPageInfo(searchDto));
            ParamUtil.setSessionAttr(request, KEY_ADHOC_RFI_LIST, new ArrayList<>());
        }

    }

    public void preAdhocRfiDetail(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AdhocRfiDto adhocRfiDto= (AdhocRfiDto) ParamUtil.getSessionAttr(request,"adhocReqForInfoDto");
        try {
            String id =  ParamUtil.getMaskedString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
            if(!StringUtil.isEmpty(id)){
                List<AdhocRfiDto> reqForInfos = (List<AdhocRfiDto>) ParamUtil.getSessionAttr(request, KEY_ADHOC_RFI_LIST);
                for (AdhocRfiDto rfi:reqForInfos
                     ) {
                    if(rfi.getId().equals(id)){
                        adhocRfiDto=rfi;
                        break;
                    }
                }

            }
        }catch (Exception e){
            log.error("not mask id");
        }
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
        adhocRfiClient.saveAdhocRfi(adhocRfiDto);
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
