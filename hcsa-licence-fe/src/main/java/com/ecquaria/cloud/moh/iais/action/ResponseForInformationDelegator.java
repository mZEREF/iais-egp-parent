package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.service.ResponseForInformationService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.List;

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
    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Autowired
    FeEicGatewayClient feEicGatewayClient;
    @Autowired
    private ServiceConfigService serviceConfigService;

    public void Start(BaseProcessClass bpc)  {
        log.debug(StringUtil.changeForLog("the do Start start ...."));
        HttpServletRequest request=bpc.request;
        String licenseeId = ParamUtil.getString(request,"licenseeId");
        if(StringUtil.isEmpty(licenseeId)){
            licenseeId = "9ED45E34-B4E9-E911-BE76-000C29C8FBE4";
        }
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
        String id =  ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
        LicPremisesReqForInfoDto licPremisesReqForInfoDto=responseForInformationService.getLicPreReqForInfo(id);
        licPremisesReqForInfoDto.setOfficerRemarks(licPremisesReqForInfoDto.getOfficerRemarks().split("\\|")[0]);
        ParamUtil.setRequestAttr(request,"licPreReqForInfoDto",licPremisesReqForInfoDto);
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
        CommonsMultipartFile file= (CommonsMultipartFile) mulReq.getFile( "UploadFile");
        if(file != null && file.getSize() != 0&&!StringUtil.isEmpty(file.getOriginalFilename())){
            file.getFileItem().setFieldName("selectedFile");
            licPremisesReqForInfoDto.setDocName(file.getOriginalFilename());
            long size = file.getSize() / 1024;
            licPremisesReqForInfoDto.setDocSize(Integer.valueOf(String.valueOf(size)));
            String fileRepoGuid = serviceConfigService.saveFileToRepo(file);
            licPremisesReqForInfoDto.setFileRepoId(fileRepoGuid);
            licPremisesReqForInfoDto.setSubmitDt(new Date());
            licPremisesReqForInfoDto.setSubmitBy(licPremisesReqForInfoDto.getLicenseeId());
        }
        licPremisesReqForInfoDto.setReplyDate(new Date());
        licPremisesReqForInfoDto.setReplyUser(licPremisesReqForInfoDto.getLicenseeId());
        licPremisesReqForInfoDto.setUserReply(userReply);

        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        feEicGatewayClient.routeRfiData(licPremisesReqForInfoDto,
                signature.date(), signature.authorization(), signature2.date(), signature2.authorization()).getEntity();

        responseForInformationService.acceptLicPremisesReqForInfo(licPremisesReqForInfoDto);

        logAbout("preparetionData");
        byte[] data = responseForInformationService.getData(licPremisesReqForInfoDto);
        log.info("------------------- getData  end --------------");
        responseForInformationService.saveFile(data);
        log.info("------------------- saveFile  end --------------");
        responseForInformationService.compressFile();
        log.info("------------------- compressFile  end --------------");
        // 		doSubmit->OnStepProcess
    }
    private  void logAbout(String methodName){
        log.debug(StringUtil.changeForLog("****The***** " +methodName +" ******Start ****"));
    }
}
