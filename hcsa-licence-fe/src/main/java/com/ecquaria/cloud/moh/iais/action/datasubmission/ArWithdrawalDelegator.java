package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorSampleAgeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DpSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsWithdrawCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.LdtSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TopSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.VssSuperDataSubmissionDto;
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
import com.ecquaria.cloud.moh.iais.service.datasubmission.DpDataSubmissionService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.LdtDataSubmissionService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.TopDataSubmissionService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.VssDataSubmissionService;
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
    @Autowired
    private DpDataSubmissionService dpDataSubmissionService;

    @Autowired
    private LdtDataSubmissionService ldtDataSubmissionService;

    @Autowired
    private VssDataSubmissionService vssDataSubmissionService;

    @Autowired
    private TopDataSubmissionService topDataSubmissionService;

    public void start(BaseProcessClass bpc)  {
        List<String> submissionNos = (List<String>) ParamUtil.getSessionAttr(bpc.request,"submissionWithdrawalNos");
        String dsType = ParamUtil.getString(bpc.request, "dsType");
        ParamUtil.setSessionAttr(bpc.request,"wdDsType",dsType);
        switch (dsType){
            case DataSubmissionConsts.DS_AR:
                List<ArSuperDataSubmissionDto> addArWithdrawnDtoList= IaisCommonUtils.genNewArrayList();
                Map<String, ArSuperDataSubmissionDto> dataSubmissionDtoMap=IaisCommonUtils.genNewHashMap();

                for (String submissionNo:submissionNos
                ) {
                    ArSuperDataSubmissionDto arSuper = arDataSubmissionService.getArSuperDataSubmissionDtoBySubmissionNo(
                            submissionNo);
                    List<DataSubmissionDto> dataSubmissionDtoList=assistedReproductionService.allDataSubmissionByCycleId(arSuper.getDataSubmissionDto().getCycleId());

                    for (DataSubmissionDto dataSubmissionDto:dataSubmissionDtoList
                    ) {
                        if(!dataSubmissionDto.getSubmitDt().before(arSuper.getDataSubmissionDto().getSubmitDt())){
                            ArSuperDataSubmissionDto arSuperDataSubmissionDto = assistedReproductionService.getArSuperDataSubmissionDto(
                                    dataSubmissionDto.getSubmissionNo());
                            dataSubmissionDtoMap.put(arSuperDataSubmissionDto.getDataSubmissionDto().getSubmissionNo(),arSuperDataSubmissionDto);
                        }
                    }
                }
                for (Map.Entry<String, ArSuperDataSubmissionDto> dataSubmissionDtoEntry:dataSubmissionDtoMap.entrySet()
                ) {
                    addArWithdrawnDtoList.add(dataSubmissionDtoEntry.getValue());
                }
                ParamUtil.setSessionAttr(bpc.request, "addWithdrawnDtoList", (Serializable) addArWithdrawnDtoList);

                break;
            case DataSubmissionConsts.DS_DRP:
                List<DpSuperDataSubmissionDto> addDrWithdrawnDtoList= IaisCommonUtils.genNewArrayList();
                DpSuperDataSubmissionDto dpSuper = dpDataSubmissionService.getDpSuperDataSubmissionDto(submissionNos.get(0));
                addDrWithdrawnDtoList.add(dpSuper);
                ParamUtil.setSessionAttr(bpc.request, "addWithdrawnDtoList", (Serializable) addDrWithdrawnDtoList);

                break;
            case DataSubmissionConsts.DS_TOP:
                List<TopSuperDataSubmissionDto> addTopWithdrawnDtoList= IaisCommonUtils.genNewArrayList();
                TopSuperDataSubmissionDto topSuperDataSubmissionDto = topDataSubmissionService.getTopSuperDataSubmissionDto(submissionNos.get(0));
                addTopWithdrawnDtoList.add(topSuperDataSubmissionDto);
                ParamUtil.setSessionAttr(bpc.request, "addWithdrawnDtoList", (Serializable) addTopWithdrawnDtoList);
                break;
            case DataSubmissionConsts.DS_VSS:
                List<VssSuperDataSubmissionDto> addVssWithdrawnDtoList= IaisCommonUtils.genNewArrayList();
                VssSuperDataSubmissionDto vssSuperDataSubmissionDto = vssDataSubmissionService.getVssSuperDataSubmissionDto(submissionNos.get(0));
                addVssWithdrawnDtoList.add(vssSuperDataSubmissionDto);
                ParamUtil.setSessionAttr(bpc.request, "addWithdrawnDtoList", (Serializable) addVssWithdrawnDtoList);
                break;
            case DataSubmissionConsts.DS_LDT:
                List<LdtSuperDataSubmissionDto> addLdtWithdrawnDtoList= IaisCommonUtils.genNewArrayList();
                LdtSuperDataSubmissionDto ldtSuperDataSubmissionDto = ldtDataSubmissionService.getLdtSuperDataSubmissionDto(submissionNos.get(0));
                ldtSuperDataSubmissionDto.setAppType(ldtSuperDataSubmissionDto.getDataSubmissionDto().getAppType());
                addLdtWithdrawnDtoList.add(ldtSuperDataSubmissionDto);
                ParamUtil.setSessionAttr(bpc.request, "addWithdrawnDtoList", (Serializable) addLdtWithdrawnDtoList);
                break;
            default:break;
        }
        ParamUtil.setSessionAttr(bpc.request, HcsaLicenceFeConstant.DASHBOARDTITLE,"Withdrawal Form");
        ParamUtil.setSessionAttr(bpc.request,"submissionWithdrawalNos",null);
        ParamUtil.setSessionAttr(bpc.request, "withdrawnRemarks",null);
        ParamUtil.setSessionAttr(bpc.request, "arWdDto", null);
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

        ParamUtil.setSessionAttr(bpc.request, "arWdDto", newDto);
        ParamUtil.setRequestAttr(bpc.request, "underwayArWd",1);

    }

    public void withdrawalStep(BaseProcessClass bpc)  {
        ArSuperDataSubmissionDto arSuperDataSubmission= (ArSuperDataSubmissionDto) ParamUtil.getSessionAttr(bpc.request, "arWdDto");
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
        ParamUtil.setSessionAttr(bpc.request, "arWdDto", arSuperDataSubmission);

    }

    public void saveDate(BaseProcessClass bpc)  {
        List<DsWithdrawCorrelationDto> list=IaisCommonUtils.genNewArrayList();
        String dsType = (String) ParamUtil.getSessionAttr(bpc.request, "wdDsType");


        switch (dsType){
            case DataSubmissionConsts.DS_AR:
                List<ArSuperDataSubmissionDto> addWithdrawnDtoList= (List<ArSuperDataSubmissionDto>) ParamUtil.getSessionAttr(bpc.request, "addWithdrawnDtoList");
                Map<String,Boolean> cycleWd=IaisCommonUtils.genNewHashMap();
                for (ArSuperDataSubmissionDto arSuperDataSubmission:addWithdrawnDtoList
                ) {
                    String dataSubNo=arSuperDataSubmission.getDataSubmissionDto().getSubmissionNo();
                    if(dataSubNo.contains("-01")||!dataSubNo.contains("-")){
                        cycleWd.put(arSuperDataSubmission.getCycleDto().getId(),true);
                    }else if(!cycleWd.containsKey(arSuperDataSubmission.getCycleDto().getId()) || !cycleWd.get(arSuperDataSubmission.getCycleDto().getId())){
                        cycleWd.put(arSuperDataSubmission.getCycleDto().getId(),false);
                    }
                }
                for (ArSuperDataSubmissionDto arSuperDataSubmission:addWithdrawnDtoList
                ) {
                    arSuperDataSubmission.getDataSubmissionDto().setStatus(DataSubmissionConsts.DS_STATUS_WITHDRAW);
                    arSuperDataSubmission.getDataSubmissionDto().setLockStatus(arSuperDataSubmission.getDataSubmissionDto().getLockStatus()+1);
                    if(arSuperDataSubmission.getDataSubmissionDto().getCycleStage().equals(DataSubmissionConsts.AR_CYCLE_AR)){
                        if(IaisCommonUtils.isNotEmpty(arSuperDataSubmission.getArCycleStageDto().getDonorDtos())){
                            for (DonorDto donorDto:arSuperDataSubmission.getArCycleStageDto().getDonorDtos()
                            ) {
                                if(IaisCommonUtils.isNotEmpty(donorDto.getDonorSampleAgeDtos())){
                                    for (DonorSampleAgeDto age :donorDto.getDonorSampleAgeDtos()
                                    ) {
                                        age.setStatus(DataSubmissionConsts.DONOR_AGE_STATUS_ACTIVE);
                                    }
                                }
                            }
                        }
                    }
                    if(arSuperDataSubmission.getDataSubmissionDto().getCycleStage().equals(DataSubmissionConsts.AR_CYCLE_IUI)){
                        if(IaisCommonUtils.isNotEmpty(arSuperDataSubmission.getIuiCycleStageDto().getDonorDtos())){
                            for (DonorDto donorDto:arSuperDataSubmission.getIuiCycleStageDto().getDonorDtos()
                            ) {
                                if(IaisCommonUtils.isNotEmpty(donorDto.getDonorSampleAgeDtos())){
                                    for (DonorSampleAgeDto age :donorDto.getDonorSampleAgeDtos()
                                    ) {
                                        age.setStatus(DataSubmissionConsts.DONOR_AGE_STATUS_ACTIVE);
                                    }
                                }
                            }
                        }
                    }
                    DsWithdrawCorrelationDto dsWithdrawCorrelationDto=new DsWithdrawCorrelationDto();
                    dsWithdrawCorrelationDto.setRelatedSubmissionId(arSuperDataSubmission.getDataSubmissionDto().getId());
                    list.add(dsWithdrawCorrelationDto);
                    if(cycleWd.get(arSuperDataSubmission.getCycleDto().getId())){
                        arSuperDataSubmission.setAppType(DataSubmissionConsts.DS_APP_TYPE_WITHDRAW);
                        arSuperDataSubmission.getCycleDto().setStatus(DataSubmissionConsts.DS_STATUS_WITHDRAW);
                    }

                }
                addWithdrawnDtoList = arDataSubmissionService.saveArSuperDataSubmissionDtoList(addWithdrawnDtoList);
                try {
                    arDataSubmissionService.saveArSuperDataSubmissionDtoListToBE(addWithdrawnDtoList);
                } catch (Exception e) {
                    log.error(StringUtil.changeForLog("The Eic saveArSuperDataSubmissionDtoToBE failed ===>" + e.getMessage()), e);
                }
                break;
            case DataSubmissionConsts.DS_TOP:
                List<TopSuperDataSubmissionDto> addTopWithdrawnDtoList= (List<TopSuperDataSubmissionDto>) ParamUtil.getSessionAttr(bpc.request, "addWithdrawnDtoList");
                TopSuperDataSubmissionDto topSuper=addTopWithdrawnDtoList.get(0);
                DsWithdrawCorrelationDto dsWithdrawCorrelationDto1=new DsWithdrawCorrelationDto();
                dsWithdrawCorrelationDto1.setRelatedSubmissionId(topSuper.getDataSubmissionDto().getId());
                list.add(dsWithdrawCorrelationDto1);
                topSuper.getCycleDto().setStatus(DataSubmissionConsts.DS_STATUS_WITHDRAW);
                topSuper.setAppType(DataSubmissionConsts.DS_APP_TYPE_WITHDRAW);
                topSuper = topDataSubmissionService.saveTopSuperDataSubmissionDto(topSuper);
                try {
                    topDataSubmissionService.saveTopSuperDataSubmissionDtoToBE(topSuper);
                } catch (Exception e) {
                    log.error(StringUtil.changeForLog("The Eic saveTOPSuperDataSubmissionDtoToBE failed ===>" + e.getMessage()), e);
                }
                break;
            case DataSubmissionConsts.DS_DRP:
                List<DpSuperDataSubmissionDto> addDpWithdrawnDtoList= (List<DpSuperDataSubmissionDto>) ParamUtil.getSessionAttr(bpc.request, "addWithdrawnDtoList");
                DpSuperDataSubmissionDto dpSuper=addDpWithdrawnDtoList.get(0);
                DsWithdrawCorrelationDto dsWithdrawCorrelationDto2=new DsWithdrawCorrelationDto();
                dsWithdrawCorrelationDto2.setRelatedSubmissionId(dpSuper.getDataSubmissionDto().getId());
                list.add(dsWithdrawCorrelationDto2);
                dpSuper.setAppType(DataSubmissionConsts.DS_APP_TYPE_WITHDRAW);
                dpSuper.getCycleDto().setStatus(DataSubmissionConsts.DS_STATUS_WITHDRAW);
                dpSuper =dpDataSubmissionService.saveDpSuperDataSubmissionDto(dpSuper);
                try {
                     dpDataSubmissionService.saveDpSuperDataSubmissionDtoToBE(dpSuper);
                } catch (Exception e) {
                    log.error(StringUtil.changeForLog("The Eic saveDpSuperDataSubmissionDtoToBE failed ===>" + e.getMessage()), e);
                }
                break;
            case DataSubmissionConsts.DS_VSS:
                List<VssSuperDataSubmissionDto> addVssWithdrawnDtoList= (List<VssSuperDataSubmissionDto>) ParamUtil.getSessionAttr(bpc.request, "addWithdrawnDtoList");
                VssSuperDataSubmissionDto vssSuper=addVssWithdrawnDtoList.get(0);
                DsWithdrawCorrelationDto dsWithdrawCorrelationDto3=new DsWithdrawCorrelationDto();
                dsWithdrawCorrelationDto3.setRelatedSubmissionId(vssSuper.getDataSubmissionDto().getId());
                list.add(dsWithdrawCorrelationDto3);
                vssSuper.getCycleDto().setStatus(DataSubmissionConsts.DS_STATUS_WITHDRAW);
                vssSuper.setAppType(DataSubmissionConsts.DS_APP_TYPE_WITHDRAW);
                vssSuper =vssDataSubmissionService.saveVssSuperDataSubmissionDto(vssSuper);
                try {
                     vssDataSubmissionService.saveVssSuperDataSubmissionDtoToBE(vssSuper);
                } catch (Exception e) {
                    log.error(StringUtil.changeForLog("The Eic saveVssSuperDataSubmissionDtoToBE failed ===>" + e.getMessage()), e);
                }
                break;
            case DataSubmissionConsts.DS_LDT:
                List<LdtSuperDataSubmissionDto> addLdtWithdrawnDtoList= (List<LdtSuperDataSubmissionDto>) ParamUtil.getSessionAttr(bpc.request, "addWithdrawnDtoList");
                LdtSuperDataSubmissionDto ldtSuper=addLdtWithdrawnDtoList.get(0);
                DsWithdrawCorrelationDto dsWithdrawCorrelationDto4=new DsWithdrawCorrelationDto();
                dsWithdrawCorrelationDto4.setRelatedSubmissionId(ldtSuper.getDataSubmissionDto().getId());
                list.add(dsWithdrawCorrelationDto4);
                ldtSuper.getCycleDto().setStatus(DataSubmissionConsts.DS_STATUS_WITHDRAW);
                ldtSuper.setAppType(DataSubmissionConsts.DS_APP_TYPE_WITHDRAW);
                ldtSuper=ldtDataSubmissionService.saveLdtSuperDataSubmissionDto(ldtSuper);
                try {
                    ldtDataSubmissionService.saveLdtSuperDataSubmissionDtoToBE(ldtSuper);
                } catch (Exception e) {
                    log.error(StringUtil.changeForLog("The Eic saveLdtSuperDataSubmissionDtoToBE failed ===>" + e.getMessage()), e);
                }
                break;
            default:
        }

        ArSuperDataSubmissionDto arSuperDataSubmission= (ArSuperDataSubmissionDto) ParamUtil.getSessionAttr(bpc.request, "arWdDto");
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
        ParamUtil.setSessionAttr(bpc.request, "emailAddress", DataSubmissionHelper.getLicenseeEmailAddrs(bpc.request));
        ParamUtil.setSessionAttr(bpc.request, "submittedBy", DataSubmissionHelper.getLoginContext(bpc.request).getUserName());
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, DataSubmissionConstant.PAGE_STAGE_ACK);
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.PRINT_FLAG, DataSubmissionConstant.PRINT_FLAG_ACKWD);
    }

    public void printStep(BaseProcessClass bpc)  {
        String remarks= ParamUtil.getString(bpc.request, "remarks");
        ParamUtil.setSessionAttr(bpc.request, "withdrawnRemarks",remarks);
    }
}
