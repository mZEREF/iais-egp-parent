package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceFeConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * ArWithdrawalDelegator
 *
 * @author junyu
 * @date 2022/1/5
 */
@Slf4j
@Delegator("arWithdrawalDelegator")
public class ArWithdrawalDelegator {

    @Autowired
    private ArDataSubmissionService arDataSubmissionService;

    @Autowired
    private SystemAdminClient systemAdminClient;

    public void start(BaseProcessClass bpc)  {
        List<String> submissionNos = (List<String>) ParamUtil.getSessionAttr(bpc.request,"submissionWithdrawalNos");
        List<ArSuperDataSubmissionDto> addWithdrawnDtoList= IaisCommonUtils.genNewArrayList();
        for (String submissionNo:submissionNos
             ) {
            ArSuperDataSubmissionDto arSuper = arDataSubmissionService.getArSuperDataSubmissionDtoBySubmissionNo(
                    submissionNo);
            addWithdrawnDtoList.add(arSuper);
        }
        ParamUtil.setSessionAttr(bpc.request, HcsaLicenceFeConstant.DASHBOARDTITLE,"Withdrawal From");
        ParamUtil.setSessionAttr(bpc.request, "addWithdrawnDtoList", (Serializable) addWithdrawnDtoList);
        ParamUtil.setSessionAttr(bpc.request,"submissionWithdrawalNos",null);
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_WITHDRAWAL, AuditTrailConsts.FUNCTION_WITHDRAWAL);
    }

    public void prepareDate(BaseProcessClass bpc)  {
        ArSuperDataSubmissionDto newDto = new ArSuperDataSubmissionDto();
        newDto.setAppType(DataSubmissionConsts.DS_APP_TYPE_NEW);
        String orgId = Optional.ofNullable(DataSubmissionHelper.getLoginContext(bpc.request))
                .map(LoginContext::getOrgId).orElse("");
        newDto.setOrgId(orgId);
        newDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        DataSubmissionDto dataSubmissionDto = DataSubmissionHelper.initDataSubmission(newDto, true);
        if (DataSubmissionConsts.DS_APP_TYPE_RFC.equals(dataSubmissionDto.getAppType())) {
            dataSubmissionDto.setStatus(DataSubmissionConsts.DS_STATUS_AMENDED);
        } else if (StringUtil.isEmpty(dataSubmissionDto.getStatus())) {
            dataSubmissionDto.setStatus(DataSubmissionConsts.DS_STATUS_COMPLETED);
        }
        dataSubmissionDto.setDeclaration(null);
        newDto.setDataSubmissionDto(dataSubmissionDto);

        DataSubmissionHelper.setCurrentArDataSubmission(newDto,bpc.request);

    }

    public void withdrawalStep(BaseProcessClass bpc)  {

    }

    public void saveDate(BaseProcessClass bpc)  {
        List<ArSuperDataSubmissionDto> addWithdrawnDtoList= (List<ArSuperDataSubmissionDto>) ParamUtil.getSessionAttr(bpc.request, "addWithdrawnDtoList");
        for (ArSuperDataSubmissionDto arSuperDataSubmission:addWithdrawnDtoList
             ) {
            arSuperDataSubmission.getDataSubmissionDto().setStatus(DataSubmissionConsts.DS_STATUS_WITHDRAW);
            arSuperDataSubmission = arDataSubmissionService.saveArSuperDataSubmissionDto(arSuperDataSubmission);
            try {
                 arDataSubmissionService.saveArSuperDataSubmissionDtoToBE(arSuperDataSubmission);
            } catch (Exception e) {
                log.error(StringUtil.changeForLog("The Eic saveArSuperDataSubmissionDtoToBE failed ===>" + e.getMessage()), e);
            }
        }


        ArSuperDataSubmissionDto arSuperDataSubmission=DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        DataSubmissionDto dataSubmissionDto = arSuperDataSubmission.getDataSubmissionDto();
        LoginContext loginContext = DataSubmissionHelper.getLoginContext(bpc.request);
        if (loginContext != null) {
            dataSubmissionDto.setSubmitBy(loginContext.getUserId());
            dataSubmissionDto.setSubmitDt(new Date());
        }
        String submissionNo = systemAdminClient.applicationNumber(ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL).getEntity();
        dataSubmissionDto.setSubmissionNo(submissionNo);
        arSuperDataSubmission = arDataSubmissionService.saveArSuperDataSubmissionDto(arSuperDataSubmission);
        try {
            arDataSubmissionService.saveArSuperDataSubmissionDtoToBE(arSuperDataSubmission);
        } catch (Exception e) {
            log.error(StringUtil.changeForLog("The Eic saveArSuperDataSubmissionDtoToBE failed ===>" + e.getMessage()), e);
        }
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmission,bpc.request);
        ParamUtil.setRequestAttr(bpc.request, "emailAddress", DataSubmissionHelper.getLicenseeEmailAddrs(bpc.request));
        ParamUtil.setRequestAttr(bpc.request, "submittedBy", DataSubmissionHelper.getLoginContext(bpc.request).getUserName());
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, DataSubmissionConstant.PAGE_STAGE_ACK);
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.PRINT_FLAG, DataSubmissionConstant.PRINT_FLAG_WD);
    }

    public void printStep(BaseProcessClass bpc)  {

    }
}
