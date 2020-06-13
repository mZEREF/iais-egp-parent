package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.constant.reqForInfo.RequestForInformationConstants;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoReplyDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.LicenceViewService;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.ResponseForInformationService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.FeMessageClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationLienceseeClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ResponseForInformationDelegator
 *
 * @author junyu
 * @date 2019/12/30
 */
@Slf4j
@Delegator("responseForInformationDelegator")
public class ResponseForInformationDelegator {
    @Autowired
    ResponseForInformationService responseForInformationService;
    @Autowired
    RequestForChangeService requestForChangeService;
    @Autowired
    FeEicGatewayClient feEicGatewayClient;
    @Autowired
    private ServiceConfigService serviceConfigService;
    @Autowired
    OrganizationLienceseeClient organizationLienceseeClient;
    @Autowired
    LicenceViewService licenceViewService;
   @Autowired
    FeMessageClient messageClient;

    public void Start(BaseProcessClass bpc)  {
        log.debug(StringUtil.changeForLog("the do Start start ...."));
        HttpServletRequest request=bpc.request;
        String licenseeId = ParamUtil.getMaskedString(request,"licenseeId");
        String messageId= (String) ParamUtil.getSessionAttr(request,AppConsts.SESSION_INTER_INBOX_MESSAGE_ID);
        messageClient.updateMsgStatus(messageId,"Response");
        InterMessageDto messageDto=messageClient.getInterMessageById(messageId).getEntity();
        ParamUtil.setSessionAttr(request,"msg_action_id",MaskUtil.maskValue("msg_action_id",messageId));
        ParamUtil.setSessionAttr(request,"msg_action_type",MaskUtil.maskValue("msg_action_type",messageDto.getMessageType()));
        ParamUtil.setSessionAttr(request,"licenseeId",licenseeId);
        // 		Start->OnStepProcess
    }

    public void preRfi(BaseProcessClass bpc)  {
        log.debug(StringUtil.changeForLog("the do preRfi start ...."));
        HttpServletRequest request=bpc.request;
        String licenseeId = (String) ParamUtil.getSessionAttr(request,"licenseeId");
        List<LicPremisesReqForInfoDto> reqForInfoSearchListDtos=responseForInformationService.searchLicPreRfiBylicenseeId(licenseeId);
        ParamUtil.setRequestAttr(request,"reqForInfoSearchList",reqForInfoSearchListDtos);
        // 		preRFI->OnStepProcess
    }

    public void preDetail(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do preDetail start ...."));
        HttpServletRequest request=bpc.request;
        LicPremisesReqForInfoDto licPremisesReqForInfoDto= (LicPremisesReqForInfoDto) ParamUtil.getSessionAttr(request,"licPreReqForInfoDto");
        if(licPremisesReqForInfoDto==null){
            try {
                String id =  ParamUtil.getMaskedString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
                licPremisesReqForInfoDto=responseForInformationService.getLicPreReqForInfo(id);
                logAbout("ReqForInfoId:"+licPremisesReqForInfoDto.getId());
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }
        }
        ParamUtil.setSessionAttr(request,"licPreReqForInfoDto",licPremisesReqForInfoDto);
        // 		doRFI->OnStepProcess
    }

    public void doBack(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do doBack start ...."));
        // 		doBack->OnStepProcess
    }
    public void doDetail(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do doBack start ...."));
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);

        String crudActionType = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);

        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, crudActionType);
        // 		doBack->OnStepProcess
    }

    public void doSubmit(BaseProcessClass bpc) throws IOException {
        log.debug(StringUtil.changeForLog("the do doDetail start ...."));
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);

        String crudActionType = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);

        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, crudActionType);

        LicPremisesReqForInfoDto licPremisesReqForInfoDto=(LicPremisesReqForInfoDto) ParamUtil.getSessionAttr(bpc.request,"licPreReqForInfoDto");;
        int i=0;
        try {
            for(LicPremisesReqForInfoDocDto doc :licPremisesReqForInfoDto.getLicPremisesReqForInfoDocDto()){
                CommonsMultipartFile file= (CommonsMultipartFile) mulReq.getFile( "UploadFile"+doc.getId());
                if(file != null && file.getSize() != 0&&!StringUtil.isEmpty(file.getOriginalFilename())){
                    file.getFileItem().setFieldName("selectedFile");
                    long size = file.getSize() / 1024;
                    doc.setDocName(file.getOriginalFilename());
                    doc.setDocSize(Integer.valueOf(String.valueOf(size)));
                    String fileRepoGuid = serviceConfigService.saveFileToRepo(file);
                    doc.setFileRepoId(fileRepoGuid);
                    doc.setSubmitDt(new Date());
                    doc.setSubmitBy(licPremisesReqForInfoDto.getLicenseeId());
                }
                i++;
            }
            i=0;
            for(LicPremisesReqForInfoReplyDto info :licPremisesReqForInfoDto.getLicPremisesReqForInfoReplyDtos()){
                String userReply=mulReq.getParameter("userReply"+info.getId());
                info.setUserReply(userReply);
                i++;
            }
        }catch (Exception e){
            log.info(e.getMessage(),e);
        }
        ParamUtil.setSessionAttr(bpc.request,"licPreReqForInfoDto",licPremisesReqForInfoDto);

        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "Y");
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        errorMap=validate(bpc.request,licPremisesReqForInfoDto);
        if (!errorMap.isEmpty()) {
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "N");
            //
            return;
        }


        licPremisesReqForInfoDto.setStatus(RequestForInformationConstants.RFI_CLOSE);
        licPremisesReqForInfoDto.setReplyDate(new Date());
        LicenseeDto licenseeDto=licenceViewService.getLicenseeDtoBylicenseeId(licPremisesReqForInfoDto.getLicenseeId());
        licPremisesReqForInfoDto.setReplyUser(licenseeDto.getName());
        LicPremisesReqForInfoDto licPremisesReqForInfoDto1=responseForInformationService.acceptLicPremisesReqForInfo(licPremisesReqForInfoDto);

        logAbout("preparetionData");
        responseForInformationService.getData(licPremisesReqForInfoDto1);
        log.info("------------------- getData  end --------------");
        responseForInformationService.saveFile(licPremisesReqForInfoDto1);
        log.info("------------------- saveFile  end --------------");
        responseForInformationService.compressFile(licPremisesReqForInfoDto1.getId());
        log.info("------------------- compressFile  end --------------");
        ParamUtil.setSessionAttr(bpc.request,"licPreReqForInfoDto",null);


        // 		doSubmit->OnStepProcess
    }
    private  void logAbout(String methodName){
        log.debug(StringUtil.changeForLog("****The***** " +methodName +" ******Start ****"));
    }

    public Map<String, String> validate(HttpServletRequest httpServletRequest ,LicPremisesReqForInfoDto licPremisesReqForInfoDto) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) httpServletRequest.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        if(!IaisCommonUtils.isEmpty(licPremisesReqForInfoDto.getLicPremisesReqForInfoDocDto())){
            for(LicPremisesReqForInfoDocDto doc :licPremisesReqForInfoDto.getLicPremisesReqForInfoDocDto()){
                CommonsMultipartFile file= (CommonsMultipartFile) mulReq.getFile( "UploadFile"+doc.getId());
                if(!(file != null && file.getSize() != 0&&!StringUtil.isEmpty(file.getOriginalFilename()))){
                    errMap.put("UploadFile"+doc.getId(),"The file cannot be empty.");
                }
                if(licPremisesReqForInfoDto.isNeedDocument()){
                    if(doc.getDocSize()==null){
                        errMap.put("UploadFile"+doc.getId(),"The file cannot be empty.");
                    }else{
                        Map<String, Boolean> booleanMap = ValidationUtils.validateFile(file);
                        Boolean fileSize = booleanMap.get("fileSize");
                        Boolean fileType = booleanMap.get("fileType");
                        //size
                        if(!fileSize){
                            errMap.put("UploadFile"+doc.getId(),"The file size must less than " + 4 + "M.");
                        }
                        //type
                        if(!fileType){
                            errMap.put("UploadFile"+doc.getId(),"The file type is invalid.");
                        }
                    }
                }

            }
        }
        if(!IaisCommonUtils.isEmpty(licPremisesReqForInfoDto.getLicPremisesReqForInfoReplyDtos())){
            for(LicPremisesReqForInfoReplyDto info :licPremisesReqForInfoDto.getLicPremisesReqForInfoReplyDtos()){
                String userReply=mulReq.getParameter("userReply"+info.getId());
                if(StringUtil.isEmpty(userReply)){
                    errMap.put("userReply"+info.getId(),"ERR0009");
                }
            }
        }

        return errMap;
    }
}
