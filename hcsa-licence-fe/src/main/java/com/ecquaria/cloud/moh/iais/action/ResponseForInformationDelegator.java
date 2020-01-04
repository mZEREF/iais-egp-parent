package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.service.ResponseForInformationService;
import com.ecquaria.cloud.moh.iais.service.client.FileRepoClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
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
    @Autowired
    FileRepoClient fileRepoClient;

    public void Start(BaseProcessClass bpc)  {
        HttpServletRequest request=bpc.request;
        String licenseeId = ParamUtil.getString(request,"licenseeId");
        if(StringUtil.isEmpty(licenseeId)){
            licenseeId = "9ED45E34-B4E9-E911-BE76-000C29C8FBE4";
        }
        ParamUtil.setSessionAttr(request,"licenseeId",licenseeId);
        // 		Start->OnStepProcess
    }

    public void preRfi(BaseProcessClass bpc)  {
        HttpServletRequest request=bpc.request;
        String licenseeId = (String) ParamUtil.getSessionAttr(request,"licenseeId");
        List<LicPremisesReqForInfoDto> reqForInfoSearchListDtos=responseForInformationService.searchLicPreRfiBylicenseeId(licenseeId);
        ParamUtil.setRequestAttr(request,"reqForInfoSearchList",reqForInfoSearchListDtos);
        // 		preRFI->OnStepProcess
    }

    public void preDetail(BaseProcessClass bpc) {
        HttpServletRequest request=bpc.request;
        String id =  ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
        LicPremisesReqForInfoDto licPremisesReqForInfoDto=responseForInformationService.getLicPreReqForInfo(id);
        licPremisesReqForInfoDto.setOfficerRemarks(licPremisesReqForInfoDto.getOfficerRemarks().split("\\|")[0]);
        ParamUtil.setRequestAttr(request,"licPreReqForInfoDto",licPremisesReqForInfoDto);
        // 		doRFI->OnStepProcess
    }

    public void doBack(BaseProcessClass bpc) {
        // 		doBack->OnStepProcess
    }

    public void doDetail(BaseProcessClass bpc) {
        HttpServletRequest request=bpc.request;
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String id = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_VALUE);
        LicPremisesReqForInfoDto licPremisesReqForInfoDto=responseForInformationService.getLicPreReqForInfo(id);
        CommonsMultipartFile file= (CommonsMultipartFile) mulReq.getFile( "UploadFile");
//        if(file != null && file.getSize() != 0){
//            if (!StringUtil.isEmpty(file.getOriginalFilename())) {
//                file.getFileItem().setFieldName("selectedFile");
//                licPremisesReqForInfoDto.setSvcComDocId(comm.getId());
//                licPremisesReqForInfoDto.setDocName(file.getOriginalFilename());
//                long size = file.getSize() / 1024;
//                licPremisesReqForInfoDto.setDocSize(Integer.valueOf(String.valueOf(size)));
//                String md5Code = FileUtil.genMd5FileChecksum(file.getBytes());
//                licPremisesReqForInfoDto.setMd5Code(md5Code);
//                //if  common ==> set null
//                licPremisesReqForInfoDto.setPremisessName("");
//                licPremisesReqForInfoDto.setPremisessType("");
//                commonsMultipartFileMap.put(comm.getId(), file);
//
//            }
//        }

        //fileRepoClient.saveFiles()
        String userReply=ParamUtil.getString(request,"userReply");
        licPremisesReqForInfoDto.setUserReply(userReply);
        //responseForInformationService.updateLicPremisesReqForInfo(licPremisesReqForInfoDto);
        responseForInformationService.deleteLicPremisesReqForInfoFe(licPremisesReqForInfoDto.getReqInfoId());
        // 		doSubmit->OnStepProcess
    }
}
