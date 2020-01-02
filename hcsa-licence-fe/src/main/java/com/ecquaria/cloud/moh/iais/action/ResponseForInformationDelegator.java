package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.ResponseForInformationService;
import com.ecquaria.cloud.moh.iais.service.client.FileRepoClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    public void start(BaseProcessClass bpc)  {
        HttpServletRequest request=bpc.request;
        String licenseeId = ParamUtil.getString(request,"licenseeId");
        if(StringUtil.isEmpty(licenseeId)){
            licenseeId = "9ED45E34-B4E9-E911-BE76-000C29C8FBE4";
        }
        ParamUtil.setSessionAttr(request,"licenseeId",licenseeId);
        // 		Start->OnStepProcess
    }

    public void preRFI(BaseProcessClass bpc)  {
        HttpServletRequest request=bpc.request;
        String licenseeId = (String) ParamUtil.getSessionAttr(request,"licenseeId");
        List<LicPremisesReqForInfoDto> reqForInfoSearchListDtos=responseForInformationService.searchLicPreRfiBylicenseeId(licenseeId);
        ParamUtil.setRequestAttr(request,"reqForInfoSearchList",reqForInfoSearchListDtos);
        // 		preRFI->OnStepProcess
    }

    public void doRFI(BaseProcessClass bpc) {
        HttpServletRequest request=bpc.request;
        String id = (String) ParamUtil.getSessionAttr(bpc.request, "reqInfoId");
        LicPremisesReqForInfoDto licPremisesReqForInfoDto=responseForInformationService.getLicPreReqForInfo(id);
        licPremisesReqForInfoDto.setOfficerRemarks(licPremisesReqForInfoDto.getOfficerRemarks().split("\\|")[0]);
        ParamUtil.setRequestAttr(request,"licPreReqForInfoDto",licPremisesReqForInfoDto);
        // 		doRFI->OnStepProcess
    }

    public void doBack(BaseProcessClass bpc) {
        // 		doBack->OnStepProcess
    }

    public void doSubmit(BaseProcessClass bpc) {
        HttpServletRequest request=bpc.request;
        String id = (String) ParamUtil.getSessionAttr(request, "reqInfoId");
        LicPremisesReqForInfoDto licPremisesReqForInfoDto=responseForInformationService.getLicPreReqForInfo(id);
        //File uploadFile= ParamUtil.getString(request, "uploadFile");
        String userReply=ParamUtil.getString(request,"userReply");
        //fileRepoClient.saveFiles()
        // 		doSubmit->OnStepProcess
    }
}
