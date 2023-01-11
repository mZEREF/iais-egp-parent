package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranet.user.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.*;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.client.ArFeClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * EmbryoTransferDelegator
 *
 * @author jiawei
 * @date 11/1/2021
 */

@Delegator("embryoTransferDelegator")
@Slf4j
public class EmbryoTransferDelegator extends CommonDelegator {
    @Autowired
    private ArFeClient arFeClient;

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>Embryo Transferred Stage</strong>");
    }

    @Override
    public void preparePage(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        EmbryoTransferStageDto embryoTransferStageDto = arSuperDataSubmissionDto.getEmbryoTransferStageDto();
        EmbryoTransferDetailDto embryoTransferDetailDto = arSuperDataSubmissionDto.getEmbryoTransferDetailDto();
        List<EmbryoTransferDetailDto> embryoTransferDetailDtos = IaisCommonUtils.genNewArrayList(10);
        if (embryoTransferStageDto == null) {
            embryoTransferStageDto = new EmbryoTransferStageDto();
            embryoTransferStageDto.setTransferNum(1);
            if (embryoTransferStageDto.getEmbryoTransferDetailDtos() == null) {
                int embryoDetail = 1;
                for (; embryoDetail <= 10; embryoDetail++) {
                    embryoTransferDetailDtos.add(embryoTransferDetailDto);
                }
            }
            embryoTransferStageDto.setEmbryoTransferDetailDtos(embryoTransferDetailDtos);
            arSuperDataSubmissionDto.setEmbryoTransferStageDto(embryoTransferStageDto);
            DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto, bpc.request);
        } else if (embryoTransferStageDto.getEmbryoTransferDetailDtos() == null && embryoTransferStageDto.getTransferNum()!=null && embryoTransferStageDto.getId()!=null){
            int transferNum = embryoTransferStageDto.getTransferNum();
            List<EmbryoTransferDetailDto> embryoTransferDetailDtos1 = arFeClient.getEmbryoTransferDetail(embryoTransferStageDto.getId()).getEntity();
            embryoTransferDetailDtos.addAll(embryoTransferDetailDtos1);
            for (int i = transferNum; i < 11; i++){
                embryoTransferDetailDtos.add(embryoTransferDetailDto);
            }
            embryoTransferStageDto.setEmbryoTransferDetailDtos(embryoTransferDetailDtos);
            arSuperDataSubmissionDto.setEmbryoTransferStageDto(embryoTransferStageDto);
            arSuperDataSubmissionDto.setEmbryoTransferDetailDtos(embryoTransferDetailDtos);
            DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto, bpc.request);
            ParamUtil.setSessionAttr(bpc.request,"oldStage",embryoTransferStageDto);
        }

        List<SelectOption> transferNumSelectOption = IaisCommonUtils.genNewArrayList();
        List<SelectOption> embryoAgeSelectOption = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_AGE_OF_EMBRYO_TRANSFER);
        for (int i = 1; i < 11; i++) {
            String key = String.valueOf(i);
            transferNumSelectOption.add(new SelectOption(key,key));
            ParamUtil.setRequestAttr(bpc.request, i+"EmbryoAgeSelectOption", embryoAgeSelectOption);
        }
        ParamUtil.setRequestAttr(bpc.request, "transferNumSelectOption", transferNumSelectOption);

        setFlagCond(bpc.request);
    }

    private void setFlagCond(HttpServletRequest request) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        if (arSuperDataSubmissionDto == null) {
            return;
        }

        List<Integer> integers = Formatter.getYearsAndDays(arSuperDataSubmissionDto.getPatientInfoDto().getPatient().getBirthDate());
        int age = 0;
        if (IaisCommonUtils.isNotEmpty(integers)) {
            age = integers.get(0);
        }

        boolean haveStimulationCycles = false;
        boolean haveEmbryoTransferGreaterFiveDay = false;
        int embryoTransferCount = 0;
        CycleDto cycleDto = arSuperDataSubmissionDto.getCycleDto();
        if (cycleDto != null) {
            String patientCode = cycleDto.getPatientCode();
            haveStimulationCycles = arDataSubmissionService.haveStimulationCycles(patientCode);
            embryoTransferCount = arDataSubmissionService.embryoTransferCount(cycleDto.getId());
            haveEmbryoTransferGreaterFiveDay = arDataSubmissionService.haveEmbryoTransferGreaterFiveDay(cycleDto.getId());
        }

        int totalEmbryos = 0;
        ArCurrentInventoryDto arCurrentInventoryDto = DataSubmissionHelper.getCurrentArCurrentInventoryDto(request);
        if (arCurrentInventoryDto != null) {
            totalEmbryos += arCurrentInventoryDto.getFreshEmbryoNum();
            totalEmbryos += arCurrentInventoryDto.getThawedEmbryoNum();
            totalEmbryos += arCurrentInventoryDto.getFrozenEmbryoNum();
        }

        ParamUtil.setRequestAttr(request, "age", age);
        ParamUtil.setRequestAttr(request, "haveStimulationCycles", haveStimulationCycles);
        ParamUtil.setRequestAttr(request, "embryoTransferCount", embryoTransferCount);
        ParamUtil.setRequestAttr(request, "totalEmbryos", totalEmbryos);
        ParamUtil.setRequestAttr(request, "haveEmbryoTransferGreaterFiveDay", haveEmbryoTransferGreaterFiveDay);
    }

    @Override
    public void pageAction(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        EmbryoTransferStageDto embryoTransferStageDto = arSuperDataSubmissionDto.getEmbryoTransferStageDto();
        HttpServletRequest request = bpc.request;

        fromPageData(embryoTransferStageDto, request);
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto, request);

        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        String crud_action_type = ParamUtil.getRequestString(request, IntranetUserConstant.CRUD_ACTION_TYPE);

        if ("confirm".equals(crud_action_type)) {
            ValidationResult validationResult = WebValidationHelper.validateProperty(embryoTransferStageDto, "save");
            errorMap = validationResult.retrieveAll();
            verifyCommon(request, errorMap);
            if(errorMap.isEmpty()){
                valRFC(request, embryoTransferStageDto);
            }
        }

        if (!errorMap.isEmpty()) {
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, IntranetUserConstant.CRUD_ACTION_TYPE, "page");
        }
    }

    @Override
    public void prepareConfim(BaseProcessClass bpc) {
        HttpSession session = bpc.request.getSession();
        if(session != null){
            EmbryoTransferStageDto oldStageDto = (EmbryoTransferStageDto) session.getAttribute("oldStage");
            if(oldStageDto != null){
                session.removeAttribute("oldStage");
            }
        }
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        //i will fix this leater
//        ParamUtil.setRequestAttr(bpc.request, "flagTwo", arDataSubmissionService.flagOutEmbryoTransferAgeAndCount(arSuperDataSubmissionDto));
//        ParamUtil.setRequestAttr(bpc.request, "flagThree", arDataSubmissionService.flagOutEmbryoTransferCountAndPatAge(arSuperDataSubmissionDto));
        setPatientInv(arSuperDataSubmissionDto);
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto, bpc.request);
    }

    private void setPatientInv(ArSuperDataSubmissionDto arSuperDataSubmissionDto) {
        EmbryoTransferStageDto transferStageDto = arSuperDataSubmissionDto.getEmbryoTransferStageDto();
        List<EmbryoTransferDetailDto> embryoTransferDetailDtos = transferStageDto.getEmbryoTransferDetailDtos();
        int transferNum = transferStageDto.getTransferNum();
        int freshEmbryoNum = 0;
        int thawedEmbryoNum = 0;
        for (int i = 0; i < transferNum; i++) {
            if (DataSubmissionConsts.EMBRYO_TRANSFER_EMBRYO_TYPE_FRESH.equals(embryoTransferDetailDtos.get(i).getEmbryoType())) {
                freshEmbryoNum--;
            } else if (DataSubmissionConsts.EMBRYO_TRANSFER_EMBRYO_TYPE_THAWED.equals(embryoTransferDetailDtos.get(i).getEmbryoType())) {
                thawedEmbryoNum--;
            }
        }
        ArChangeInventoryDto arChangeInventoryDto = arSuperDataSubmissionDto.getArChangeInventoryDto();
        if (arChangeInventoryDto == null){
            arChangeInventoryDto = new ArChangeInventoryDto();
        }
        arChangeInventoryDto.setFreshEmbryoNum(freshEmbryoNum);
        arChangeInventoryDto.setThawedEmbryoNum(thawedEmbryoNum);
        arSuperDataSubmissionDto.setArChangeInventoryDto(arChangeInventoryDto);
    }
    private void fromPageData(EmbryoTransferStageDto embryoTransferStageDto, HttpServletRequest request) {
        Integer transferNum = ParamUtil.getInt(request, "transferNum");


        List<EmbryoTransferDetailDto> embryoTransferDetailDtos = embryoTransferStageDto.getEmbryoTransferDetailDtos();
        for (int i = 1; i <= transferNum; i++) {
            String embryoAge = ParamUtil.getRequestString(request, i+"EmbryoAge");
            String embryoType = ParamUtil.getRequestString(request, i+"EmbryoType");
            EmbryoTransferDetailDto embryoTransferDetailDto = embryoTransferDetailDtos.get(i-1);
            if (embryoTransferDetailDto == null) {
                 embryoTransferDetailDto = new EmbryoTransferDetailDto();
            }
            embryoTransferDetailDto.setEmbryoAge(embryoAge);
            embryoTransferDetailDto.setEmbryoType(embryoType);
            embryoTransferDetailDto.setSeqNumber(i);
            embryoTransferDetailDtos.set(i-1,embryoTransferDetailDto);
        }

        String firstTransferDateString = ParamUtil.getRequestString(request, "firstTransferDate");
        String secondTransferDateString = ParamUtil.getRequestString(request, "secondTransferDate");
        Date firstTransferDate = DateUtil.parseDate(firstTransferDateString, AppConsts.DEFAULT_DATE_FORMAT);
        Date secondTransferDate = DateUtil.parseDate(secondTransferDateString, AppConsts.DEFAULT_DATE_FORMAT);
        embryoTransferStageDto.setTransferNum(transferNum);
        embryoTransferStageDto.setFirstTransferDate(firstTransferDate);
        embryoTransferStageDto.setSecondTransferDate(secondTransferDate);
    }

    @Override
    public void pageConfirmAction(BaseProcessClass bpc) {
        super.pageConfirmAction(bpc);

        String actionType = ParamUtil.getString(bpc.request, DataSubmissionConstant.CRUD_TYPE);
        ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        if (DataSubmissionConsts.DS_APP_TYPE_NEW.equals(arSuperDataSubmission.getAppType()) && ACTION_TYPE_SUBMISSION.equals(actionType)) {
            String errorMapJson = (String) ParamUtil.getRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG);
            Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
            if (StringUtil.isNotEmpty(errorMapJson)) {
                List<Map> mapList = JsonUtil.parseToList(errorMapJson, Map.class);
                if (IaisCommonUtils.isNotEmpty(mapList)) {
                    for (Map map : mapList) {
                        errorMap.putAll(map);
                    }
                }
            }
            ArCurrentInventoryDto arCurrentInventoryDto = DataSubmissionHelper.getCurrentArCurrentInventoryDto(bpc.request);
            arCurrentInventoryDto = ArCurrentInventoryDto.addChange(arCurrentInventoryDto, arSuperDataSubmission.getArChangeInventoryDto());
            if (arCurrentInventoryDto != null) {
                int currentFreshEmbryos = arCurrentInventoryDto.getFreshEmbryoNum() + arCurrentInventoryDto.getFreshEmbryoNum();
                int currentThawedEmbryos = arCurrentInventoryDto.getThawedEmbryoNum() + arCurrentInventoryDto.getThawedEmbryoNum();
                if (arCurrentInventoryDto.getFreshOocyteNum() > 0
                        || arCurrentInventoryDto.getThawedOocyteNum() > 0
                        || currentFreshEmbryos > 0
                        || currentThawedEmbryos > 0) {
                    errorMap.put("inventoryNoZero", MessageUtil.getMessageDesc("DS_ERR017"));
                }
            }

            if (!errorMap.isEmpty()) {
                log.error("------inventory No Zero-----");
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_TYPE_CONFIRM);
            }
        }
    }

    protected void valRFC(HttpServletRequest request, EmbryoTransferStageDto embryoTransferStageDto) {
        if (isRfc(request)) {
            ArSuperDataSubmissionDto arOldSuperDataSubmissionDto = DataSubmissionHelper.getOldArDataSubmission(request);
            HttpSession session = request.getSession();
            if(session != null){
                EmbryoTransferStageDto oldStageDto = (EmbryoTransferStageDto) session.getAttribute("oldStage");
                if(oldStageDto != null){
                    arOldSuperDataSubmissionDto.setEmbryoTransferStageDto(oldStageDto);
                }
            }
            if (arOldSuperDataSubmissionDto != null && arOldSuperDataSubmissionDto.getEmbryoTransferStageDto() != null && embryoTransferStageDto.equals(arOldSuperDataSubmissionDto.getEmbryoTransferStageDto())) {
                ParamUtil.setRequestAttr(request, DataSubmissionConstant.RFC_NO_CHANGE_ERROR, AppConsts.YES);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_TYPE_PAGE);
            }
        }
    }
}
