package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.reqForInfo.RequestForInformationConstants;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoReplyDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.AttachmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.BlastManagementDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.LicenceViewService;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.ResponseForInformationService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.FeMessageClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationLienceseeClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
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
    @Autowired
    private SystemParamConfig systemParamConfig;

    public void Start(BaseProcessClass bpc)  {
        log.debug(StringUtil.changeForLog("the do Start start ...."));
        HttpServletRequest request=bpc.request;
        String licenseeId;
        try {
            licenseeId = ParamUtil.getMaskedString(request,"licenseeId");

        }catch (Exception e){
            licenseeId= (String) ParamUtil.getSessionAttr(request,"licenseeId");
        }
        String messageId= (String) ParamUtil.getSessionAttr(request,AppConsts.SESSION_INTER_INBOX_MESSAGE_ID);
        //messageClient.updateMsgStatus(messageId, MessageConstants.MESSAGE_STATUS_RESPONSE);
        InterMessageDto messageDto=messageClient.getInterMessageById(messageId).getEntity();
        ParamUtil.setSessionAttr(request,"msg_action_id",messageId);
        ParamUtil.setSessionAttr(request,"msg_action_type",messageDto.getMessageType());
        ParamUtil.setSessionAttr(request,"IAIS_MSG_CONTENT",messageDto.getMsgContent());
        ParamUtil.setSessionAttr(request,"licenseeId",licenseeId);
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_LICENCE_MANAGEMENT, AuditTrailConsts.FUNCTION_REQUEST_FOR_INFORMATION);
        // 		Start->OnStepProcess
    }

    public void preRfi(BaseProcessClass bpc)  {
        log.debug(StringUtil.changeForLog("the do preRfi start ...."));
        HttpServletRequest request=bpc.request;
        String licenseeId = (String) ParamUtil.getSessionAttr(request,"licenseeId");
        List<LicPremisesReqForInfoDto> reqForInfoSearchListDtos=responseForInformationService.searchLicPreRfiBylicenseeId(licenseeId);
        ParamUtil.setRequestAttr(request,"reqForInfoSearchList",reqForInfoSearchListDtos);
        ParamUtil.setSessionAttr(request,"DashboardTitle","Adhoc Request For Information");

        // 		preRFI->OnStepProcess
    }

    public void preDetail(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do preDetail start ...."));
        HttpServletRequest request=bpc.request;
        LicPremisesReqForInfoDto licPremisesReqForInfoDto= (LicPremisesReqForInfoDto) ParamUtil.getSessionAttr(request,"licPreReqForInfoDto");
        ParamUtil.setRequestAttr(bpc.request,"sysFileSize",systemParamConfig.getUploadFileLimit());

        try {
            String id =  ParamUtil.getMaskedString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
            if(!StringUtil.isEmpty(id)){
                licPremisesReqForInfoDto=responseForInformationService.getLicPreReqForInfo(id);
                String str=ParamUtil.getRequestString(request,"rfiListGo");
                if(!StringUtil.isEmpty(str)&&licPremisesReqForInfoDto.isNeedDocument()){
                    for (Map.Entry<Integer,List<LicPremisesReqForInfoDocDto>> docs:licPremisesReqForInfoDto.getLicPremisesReqForInfoMultiFileDto().entrySet()
                    ) {
                        BlastManagementDto files=new BlastManagementDto();
                        List<AttachmentDto> attachmentDtos=IaisCommonUtils.genNewArrayList();
                        for (LicPremisesReqForInfoDocDto licDoc:docs.getValue()
                        ) {
                            if(!StringUtil.isEmpty(licDoc.getDocName())){
                                AttachmentDto file=new AttachmentDto();
                                file.setId(licDoc.getId());
                                file.setDocName(licDoc.getDocName());
                                file.setDocSize(String.valueOf(licDoc.getDocSize()));
                                file.setData(serviceConfigService.downloadFile(licDoc.getFileRepoId()));
                               // licDoc.setPassDocValidate(true);
                                attachmentDtos.add(file);
                            }
                        }
                        files.setAttachmentDtos(attachmentDtos);
                        ParamUtil.setSessionAttr(request,"rfiFileDto"+docs.getKey(),files);

                    }

                }
                logAbout("ReqForInfoId:"+licPremisesReqForInfoDto.getId());
            }

        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        ParamUtil.setSessionAttr(bpc.request,"svcDocReloadMap", (Serializable) licPremisesReqForInfoDto.getLicPremisesReqForInfoMultiFileDto());
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
        ParamUtil.setRequestAttr(bpc.request,"sysFileSize",systemParamConfig.getUploadFileLimit());
        // 		doBack->OnStepProcess
    }

    public void doSubmit(BaseProcessClass bpc) throws IOException {
        log.debug(StringUtil.changeForLog("the do doDetail start ...."));
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);

        String crudActionType = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);

        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, crudActionType);

        LicPremisesReqForInfoDto licPremisesReqForInfoDto=(LicPremisesReqForInfoDto) ParamUtil.getSessionAttr(bpc.request,"licPreReqForInfoDto");;
        if(licPremisesReqForInfoDto.isNeedDocument()){
            try {
                for (Map.Entry<Integer,List<LicPremisesReqForInfoDocDto>> docs:licPremisesReqForInfoDto.getLicPremisesReqForInfoMultiFileDto().entrySet()
                ) {
                    BlastManagementDto blastManagementDto = (BlastManagementDto) ParamUtil.getSessionAttr(bpc.request,"rfiFileDto"+docs.getKey());
                    List<LicPremisesReqForInfoDocDto> list=IaisCommonUtils.genNewArrayList();
                    for (AttachmentDto file:blastManagementDto.getAttachmentDtos()
                         ) {
                        LicPremisesReqForInfoDocDto docDto=new LicPremisesReqForInfoDocDto();
                        docDto.setDocName(file.getDocName());
                        docDto.setSeqNum(docs.getKey());
                        docDto.setSubmitDt(new Date());
                        docDto.setSubmitBy(licPremisesReqForInfoDto.getLicenseeId());
                        docDto.setDocSize(Integer.valueOf(file.getDocSize()));
                        docDto.setReqInfoId(docs.getValue().get(0).getReqInfoId());
                        MultipartFile multipartFilefile = toMultipartFile(file.getDocName(),file.getDocName(), file.getData());
                        String fileRepoGuid = serviceConfigService.saveFileToRepo(multipartFilefile);
                        docDto.setFileRepoId(fileRepoGuid);
                        docDto.setTitle(docs.getValue().get(0).getTitle());
                        list.add(docDto);
                    }
                    if(list.isEmpty()){
                        LicPremisesReqForInfoDocDto licDoc=docs.getValue().get(0);
                        licDoc.setDocSize(null);
                        licDoc.setDocName("");
                        licDoc.setFileRepoId(null);
                        list.add(licDoc);
                    }
                    docs.setValue(list);
                    licPremisesReqForInfoDto.getLicPremisesReqForInfoDocDto().addAll(list);
                }
            }catch (Exception e){
                log.info(e.getMessage(),e);
            }
            licPremisesReqForInfoDto.getLicPremisesReqForInfoDocDto().removeIf(next -> next.getId() != null);
        }
        ParamUtil.setSessionAttr(bpc.request,"licPreReqForInfoDto",licPremisesReqForInfoDto);
        try {
            for(LicPremisesReqForInfoReplyDto info :licPremisesReqForInfoDto.getLicPremisesReqForInfoReplyDtos()){
                String userReply=mulReq.getParameter("userReply"+info.getId());
                info.setUserReply(userReply);
            }
        }catch (Exception e){
            log.info("no info");
        }
        ParamUtil.setSessionAttr(bpc.request,"licPreReqForInfoDto",licPremisesReqForInfoDto);

        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "Y");
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        errorMap=validate(bpc.request,licPremisesReqForInfoDto);
        if (!errorMap.isEmpty()) {
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
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
        String messageId= (String) ParamUtil.getSessionAttr(bpc.request,AppConsts.SESSION_INTER_INBOX_MESSAGE_ID);
        messageClient.updateMsgStatus(messageId, MessageConstants.MESSAGE_STATUS_RESPONSE);

        // 		doSubmit->OnStepProcess
    }
    private  void logAbout(String methodName){
        log.debug(StringUtil.changeForLog("****The***** " +methodName +" ******Start ****"));
    }
    public static MultipartFile toMultipartFile(String fieldName, String fileName, byte[] fileByteArray) throws Exception {
        DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
        String contentType = new MimetypesFileTypeMap().getContentType(fileName);
        FileItem fileItem = diskFileItemFactory.createItem(fieldName, contentType, false, fileName);
        try (
                InputStream inputStream = new ByteArrayInputStream(fileByteArray);
                OutputStream outputStream = fileItem.getOutputStream()
        ) {
            FileCopyUtils.copy(inputStream, outputStream);
        } catch (Exception e) {
            throw e;
        }
        MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
        return multipartFile;
    }
    public Map<String, String> validate(HttpServletRequest httpServletRequest ,LicPremisesReqForInfoDto licPremisesReqForInfoDto) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) httpServletRequest.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String errDocument=MessageUtil.replaceMessage("GENERAL_ERR0006","Supporting Documents","field");

        if(licPremisesReqForInfoDto.isNeedDocument()){
            for (Map.Entry<Integer,List<LicPremisesReqForInfoDocDto>> docs:licPremisesReqForInfoDto.getLicPremisesReqForInfoMultiFileDto().entrySet()){
                if( StringUtil.isEmpty(docs.getValue().get(0).getDocName())){
                    errMap.put("UploadFile"+docs.getKey(),errDocument);
                }
            }
        }
        if(!IaisCommonUtils.isEmpty(licPremisesReqForInfoDto.getLicPremisesReqForInfoReplyDtos())){
            for(LicPremisesReqForInfoReplyDto info :licPremisesReqForInfoDto.getLicPremisesReqForInfoReplyDtos()){
                String userReply=mulReq.getParameter("userReply"+info.getId());
                if(StringUtil.isEmpty(userReply)){
                    errMap.put("userReply"+info.getId(),MessageUtil.replaceMessage("GENERAL_ERR0006","Information","field"));
                }
            }
        }

        return errMap;
    }
}
