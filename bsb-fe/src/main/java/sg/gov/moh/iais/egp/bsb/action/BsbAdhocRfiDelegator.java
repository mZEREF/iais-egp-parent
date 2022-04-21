package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.constant.reqForInfo.RequestForInformationConstants;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.OrgUserManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sg.gov.moh.iais.egp.bsb.client.AdhocRfiClient;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.AdhocRfiDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.AdhocRfiQueryDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.AdhocRfiQueryResultDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.AdhocRfiViewDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
    @Autowired
    private  FileRepoClient fileRepoClient;

    public void start(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do Start start ...."));
        HttpServletRequest request=bpc.request;
        String facilityNo="LFA0408FSF02";
        AdhocRfiQueryDto dto = new AdhocRfiQueryDto();
        dto.defaultPaging();
        dto.setFacilityNo(facilityNo);
        ParamUtil.setSessionAttr(request, KEY_ADHOC_LIST_SEARCH_DTO, dto);


    }

    public void preRfiList(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, KEY_ADHOC_RFI_LIST, null);
        request.removeAttribute("adhocReqForInfoDto");
        AdhocRfiQueryDto searchDto = getSearchDto(request);
        ParamUtil.setSessionAttr(request, KEY_ADHOC_LIST_SEARCH_DTO, searchDto);
        ResponseDto<AdhocRfiQueryResultDto> resultDto = adhocRfiClient.queryAdhocRfi(searchDto);
        if (resultDto.ok()) {
            ParamUtil.setRequestAttr(request, KEY_ADHOC_PAGE_INFO, resultDto.getEntity().getPageInfo());
            List<AdhocRfiDto> reqForInfos = resultDto.getEntity().getRfiList();

            for (AdhocRfiDto adRfi:reqForInfos
                 ) {
                FeUserDto orgUserDto=orgUserManageService.getUserAccount(adRfi.getRequestor());
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
        AdhocRfiViewDto adhocRfiViewDto= (AdhocRfiViewDto) ParamUtil.getSessionAttr(request,"adhocReqForInfoDto");
        try {
            if(adhocRfiViewDto==null){
                adhocRfiViewDto=new AdhocRfiViewDto();
            }
            String id =  ParamUtil.getMaskedString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
            if(!StringUtil.isEmpty(id)){
                List<AdhocRfiDto> reqForInfos = (List<AdhocRfiDto>) ParamUtil.getSessionAttr(request, KEY_ADHOC_RFI_LIST);
                for (AdhocRfiDto rfi:reqForInfos
                     ) {
                    if(rfi.getId().equals(id)){
                        adhocRfiViewDto=adhocRfiClient.getAdhocRfiById(rfi.getId()).getEntity();
                        break;
                    }
                }

            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        ParamUtil.setSessionAttr(request,"adhocReqForInfoDto",  adhocRfiViewDto);


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

        AdhocRfiViewDto adhocRfiViewDto=(AdhocRfiViewDto) ParamUtil.getSessionAttr(bpc.request,"adhocReqForInfoDto");
        
        AdhocRfiDto adhocRfiDto=adhocRfiViewDto.getAdhocRfiDto();
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
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        if(adhocRfiDto.getSupportingDocRequired()){
            List<MultipartFile> mulReqFile= mulReq.getFiles("upload");
            adhocRfiViewDto.setApplicationDocDtos(IaisCommonUtils.genNewArrayList());
            if(IaisCommonUtils.isNotEmpty(mulReqFile)){
                for (MultipartFile file:mulReqFile
                     ) {
                    DocRecordInfo applicationDocDto=new DocRecordInfo();
                    applicationDocDto.setFilename(file.getOriginalFilename());
                    applicationDocDto.setSize(file.getSize());
                    List<String> repoIds = fileRepoClient.saveFiles(new MultipartFile[]{file}).getEntity();
                    applicationDocDto.setRepoId(repoIds.get(0));
                    applicationDocDto.setDocType(DocConstants.DOC_TYPE_ADHOC_RFI_UP);
                    applicationDocDto.setDocSubType(adhocRfiDto.getId());
                    applicationDocDto.setSubmitBy(loginContext.getUserId());
                    applicationDocDto.setSubmitDate(new Date());
                    adhocRfiViewDto.getApplicationDocDtos().add(applicationDocDto);
                }
            }

        }
        ParamUtil.setSessionAttr(bpc.request,"adhocReqForInfoDto",adhocRfiViewDto);
        if (!errorMap.isEmpty()) {
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "N");
            //
            return;
        }
        adhocRfiDto.setStatus(RequestForInformationConstants.RFI_CLOSE);
        AdhocRfiViewDto adhocRfiViewDto1=adhocRfiClient.saveAdhocRfi(adhocRfiViewDto).getEntity();

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


    

    public static boolean validateFile(HttpServletRequest request, MultipartFile file){
        if (file != null){
            String originalFileName = file.getOriginalFilename();
            if (!FileUtils.isExcel(originalFileName)){
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr("fileUploadError", "CHKL_ERR040"));
                return true;
            }
        }

        if (file == null || file.isEmpty()){
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr("fileUploadError", "GENERAL_ERR0020"));
            return true;
        }

        if (FileUtils.outFileSize(file.getSize())){
            int maxSize = SystemParamUtil.getFileMaxLimit();
            String replaceMsg = MessageUtil.replaceMessage("GENERAL_ERR0019", String.valueOf(maxSize),"sizeMax");
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr("fileUploadError", replaceMsg));
            return true;
        }

        return false;
    }


}
