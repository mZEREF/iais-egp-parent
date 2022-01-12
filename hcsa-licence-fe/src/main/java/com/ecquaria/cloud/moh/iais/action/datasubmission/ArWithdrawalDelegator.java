package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsWithdrawCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceFeConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.AssistedReproductionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private AssistedReproductionService assistedReproductionService;

    public void start(BaseProcessClass bpc)  {
        List<String> submissionNos = (List<String>) ParamUtil.getSessionAttr(bpc.request,"submissionWithdrawalNos");
        List<ArSuperDataSubmissionDto> addWithdrawnDtoList= IaisCommonUtils.genNewArrayList();
        Map<String, ArSuperDataSubmissionDto> dataSubmissionDtoMap=IaisCommonUtils.genNewHashMap();

        for (String submissionNo:submissionNos
             ) {
            ArSuperDataSubmissionDto arSuper = arDataSubmissionService.getArSuperDataSubmissionDtoBySubmissionNo(
                    submissionNo);
            List<DataSubmissionDto> dataSubmissionDtoList=assistedReproductionService.allDataSubmissionByCycleId(arSuper.getDataSubmissionDto().getCycleId());

            for (DataSubmissionDto dataSubmissionDto:dataSubmissionDtoList
            ) {
                if(!dataSubmissionDto.getSubmitDt().before(arSuper.getDataSubmissionDto().getSubmitDt())&&!dataSubmissionDto.getStatus().equals(DataSubmissionConsts.DS_STATUS_WITHDRAW)&&!dataSubmissionDto.getStatus().equals(DataSubmissionConsts.DS_STATUS_LOCKED)){
                    ArSuperDataSubmissionDto arSuperDataSubmissionDto = assistedReproductionService.getArSuperDataSubmissionDto(
                            dataSubmissionDto.getSubmissionNo());
                    dataSubmissionDtoMap.put(arSuperDataSubmissionDto.getDataSubmissionDto().getSubmissionNo(),arSuperDataSubmissionDto);
                }
            }
        }
        for (Map.Entry<String, ArSuperDataSubmissionDto> dataSubmissionDtoEntry:dataSubmissionDtoMap.entrySet()
        ) {
            addWithdrawnDtoList.add(dataSubmissionDtoEntry.getValue());
        }
        ParamUtil.setSessionAttr(bpc.request, HcsaLicenceFeConstant.DASHBOARDTITLE,"Withdrawal From");
        ParamUtil.setSessionAttr(bpc.request, "addWithdrawnDtoList", (Serializable) addWithdrawnDtoList);
        ParamUtil.setSessionAttr(bpc.request,"submissionWithdrawalNos",null);
        ParamUtil.setSessionAttr(bpc.request, "withdrawnRemarks",null);
        DataSubmissionHelper.setCurrentArDataSubmission(null,bpc.request);
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_WITHDRAWAL, AuditTrailConsts.FUNCTION_WITHDRAWAL);
    }

    public void prepareDate(BaseProcessClass bpc)  {
        ArSuperDataSubmissionDto newDto = new ArSuperDataSubmissionDto();
        newDto.setAppType(DataSubmissionConsts.DS_APP_TYPE_WITHDRAW);
        String orgId = Optional.ofNullable(DataSubmissionHelper.getLoginContext(bpc.request))
                .map(LoginContext::getOrgId).orElse("");
        newDto.setOrgId(orgId);
        newDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        DataSubmissionDto dataSubmissionDto = DataSubmissionHelper.initDataSubmission(newDto, true);
        dataSubmissionDto.setStatus(DataSubmissionConsts.DS_STATUS_COMPLETED);
        dataSubmissionDto.setDeclaration(null);
        dataSubmissionDto.setSubmissionType(ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL);
        dataSubmissionDto.setCycleStage("");
        newDto.setDataSubmissionDto(dataSubmissionDto);

        DataSubmissionHelper.setCurrentArDataSubmission(newDto,bpc.request);

    }

    public void withdrawalStep(BaseProcessClass bpc)  {
        ArSuperDataSubmissionDto arSuperDataSubmission=DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        String remarks=ParamUtil.getString(bpc.request, "withdrawnRemarks");
        ParamUtil.setSessionAttr(bpc.request, "withdrawnRemarks",remarks);

        ParamUtil.setRequestAttr(bpc.request,"isValidate","Y");
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        if(StringUtil.isNotEmpty(remarks)){
            if(remarks.length()>100){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("number","100");
                repMap.put("fieldNo","Remarks");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMap.put("withdrawnRemarks", errMsg);
            }
        }else {
            String errMsgErr006 = MessageUtil.getMessageDesc("GENERAL_ERR0006");
            errorMap.put("withdrawnRemarks",errMsgErr006);
        }
        if (!errorMap.isEmpty()) {
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request,"isValidate","N");
            //
            return;
        }
        arSuperDataSubmission.getDataSubmissionDto().setRemarks(remarks);
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmission,bpc.request);

    }

    public void saveDate(BaseProcessClass bpc)  {
        List<ArSuperDataSubmissionDto> addWithdrawnDtoList= (List<ArSuperDataSubmissionDto>) ParamUtil.getSessionAttr(bpc.request, "addWithdrawnDtoList");
        List<DsWithdrawCorrelationDto> list=IaisCommonUtils.genNewArrayList();
        for (ArSuperDataSubmissionDto arSuperDataSubmission:addWithdrawnDtoList
             ) {
            arSuperDataSubmission.getDataSubmissionDto().setStatus(DataSubmissionConsts.DS_STATUS_WITHDRAW);
            DsWithdrawCorrelationDto dsWithdrawCorrelationDto=new DsWithdrawCorrelationDto();
            dsWithdrawCorrelationDto.setRelatedSubmissionId(arSuperDataSubmission.getDataSubmissionDto().getId());
            list.add(dsWithdrawCorrelationDto);
        }
        addWithdrawnDtoList = arDataSubmissionService.saveArSuperDataSubmissionDtoList(addWithdrawnDtoList);
        try {
            arDataSubmissionService.saveArSuperDataSubmissionDtoListToBE(addWithdrawnDtoList);
        } catch (Exception e) {
            log.error(StringUtil.changeForLog("The Eic saveArSuperDataSubmissionDtoToBE failed ===>" + e.getMessage()), e);
        }


        ArSuperDataSubmissionDto arSuperDataSubmission=DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        DataSubmissionDto dataSubmissionDto = arSuperDataSubmission.getDataSubmissionDto();
        arSuperDataSubmission.setDsWithdrawCorrelationDtoList(list);
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
        ParamUtil.setSessionAttr(bpc.request, "submittedBy", DataSubmissionHelper.getLoginContext(bpc.request).getUserName());
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, DataSubmissionConstant.PAGE_STAGE_ACK);
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.PRINT_FLAG, DataSubmissionConstant.PRINT_FLAG_ACKWD);
    }

    public void printStep(BaseProcessClass bpc)  {
        String remarks= ParamUtil.getString(bpc.request, "remarks");
        ParamUtil.setSessionAttr(bpc.request, "withdrawnRemarks",remarks);
    }
}
