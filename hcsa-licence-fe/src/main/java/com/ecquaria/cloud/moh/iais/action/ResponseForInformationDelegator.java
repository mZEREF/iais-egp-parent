package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
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
import com.ecquaria.cloud.moh.iais.service.client.OrganizationLienceseeClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
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

    public void Start(BaseProcessClass bpc)  {
        log.debug(StringUtil.changeForLog("the do Start start ...."));
        HttpServletRequest request=bpc.request;
        String licenseeId = ParamUtil.getMaskedString(request,"licenseeId");

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
        LicPremisesReqForInfoDto licPremisesReqForInfoDto;
        try {
            String id =  ParamUtil.getMaskedString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
             licPremisesReqForInfoDto=responseForInformationService.getLicPreReqForInfo(id);
             logAbout("ReqForInfoId:"+licPremisesReqForInfoDto.getId());
        }catch (Exception e){
             licPremisesReqForInfoDto= (LicPremisesReqForInfoDto) ParamUtil.getSessionAttr(request,"licPreReqForInfoDto");
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
        String crudActionValue = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_VALUE);

        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, crudActionType);
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE, crudActionValue);
        // 		doBack->OnStepProcess
    }

    public void doSubmit(BaseProcessClass bpc) throws IOException {
        log.debug(StringUtil.changeForLog("the do doDetail start ...."));
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);

        String crudActionType = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);
        String crudActionValue = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_VALUE);

        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, crudActionType);
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE, crudActionValue);

        String userReply=mulReq.getParameter("userReply");
        LicPremisesReqForInfoDto licPremisesReqForInfoDto=responseForInformationService.getLicPreReqForInfo(crudActionValue);
        List<MultipartFile> files=  mulReq.getFiles( "UploadFile");
        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "Y");
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        errorMap=validate(bpc.request);
        if (!errorMap.isEmpty()) {
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "N");
            //
            return;
        }
        int i=0;
        for(MultipartFile file :files){
            if(file != null && file.getSize() != 0&&!StringUtil.isEmpty(file.getOriginalFilename())){
                LicPremisesReqForInfoDocDto doc=licPremisesReqForInfoDto.getLicPremisesReqForInfoDocDto().get(i);
                long size = file.getSize() / 1024;
                doc.setDocSize(Integer.valueOf(String.valueOf(size)));
                String fileRepoGuid = serviceConfigService.saveFileToRepo(file);
                doc.setFileRepoId(fileRepoGuid);
                doc.setSubmitDt(new Date());
                doc.setSubmitBy(licPremisesReqForInfoDto.getLicenseeId());
            }
            i++;
        }

        licPremisesReqForInfoDto.setReplyDate(new Date());
        LicenseeDto licenseeDto=licenceViewService.getLicenseeDtoBylicenseeId(licPremisesReqForInfoDto.getLicenseeId());
        licPremisesReqForInfoDto.setReplyUser(licenseeDto.getName());
        //licPremisesReqForInfoDto.setUserReply(userReply);
        LicPremisesReqForInfoDto licPremisesReqForInfoDto1=responseForInformationService.acceptLicPremisesReqForInfo(licPremisesReqForInfoDto);

        logAbout("preparetionData");
        String data = responseForInformationService.getData(licPremisesReqForInfoDto1);
        log.info("------------------- getData  end --------------");
        responseForInformationService.saveFile(data);
        log.info("------------------- saveFile  end --------------");
        responseForInformationService.compressFile(licPremisesReqForInfoDto1.getLicPremId());
        log.info("------------------- compressFile  end --------------");


        // 		doSubmit->OnStepProcess
    }
    private  void logAbout(String methodName){
        log.debug(StringUtil.changeForLog("****The***** " +methodName +" ******Start ****"));
    }

    public Map<String, String> validate(HttpServletRequest httpServletRequest) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) httpServletRequest.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        CommonsMultipartFile commonsMultipartFile= (CommonsMultipartFile) mulReq.getFile( "UploadFile");
        LicPremisesReqForInfoDto licPreReqForInfoDto= (LicPremisesReqForInfoDto) ParamUtil.getSessionAttr(httpServletRequest ,"licPreReqForInfoDto");
        if(licPreReqForInfoDto.isNeedDocument()){
            if(commonsMultipartFile==null){
                errMap.put("UploadFile","The file cannot be empty.");
            }else{
                Map<String, Boolean> booleanMap = ValidationUtils.validateFile(commonsMultipartFile);
                Boolean fileSize = booleanMap.get("fileSize");
                Boolean fileType = booleanMap.get("fileType");
                //size
                if(!fileSize){
                    errMap.put("UploadFile","The file size must less than " + 4 + "M.");
                }
                //type
                if(!fileType){
                    errMap.put("UploadFile","The file type is invalid.");
                }
            }
            String userReply=mulReq.getParameter("userReply");
            if(userReply.isEmpty()){
                errMap.put("userReply","ERR009");
            }
        }

        return errMap;
    }
}
