package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArChangeInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.EfoCycleStageDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * EfoCycleStageDelegator
 *
 * @author junyu
 * @date 2021/10/21
 */
@Delegator("efoCycleStageDelegator")
@Slf4j
public class EfoCycleStageDelegator extends CommonDelegator{
    private static final String SUBMIT_FLAG = "efoCycStgSubmitFlag__attr";

    @Override
    public void start(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto=DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        if(arSuperDataSubmissionDto==null){
            arSuperDataSubmissionDto = new ArSuperDataSubmissionDto();
        }
        if (DataSubmissionConsts.DS_CYCLE_SFO.equals(arSuperDataSubmissionDto.getSelectionDto().getCycle())){
            AuditTrailHelper.auditFunction("Assisted Reproduction", "SFO Cycle Stage");
            initSfoCycleDto(arSuperDataSubmissionDto);
        } else {
            AuditTrailHelper.auditFunction("Assisted Reproduction", "OFO Cycle Stage");
            initEfoCycleDto(arSuperDataSubmissionDto);
        }
        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.AR_DATA_SUBMISSION,arSuperDataSubmissionDto);
        ParamUtil.setSessionAttr(bpc.request, SUBMIT_FLAG, null);
    }

    private void initSfoCycleDto(ArSuperDataSubmissionDto arSuperDataSubmissionDto) {
        if(arSuperDataSubmissionDto.getEfoCycleStageDto() == null){
            arSuperDataSubmissionDto.setEfoCycleStageDto(new EfoCycleStageDto());
            arSuperDataSubmissionDto.getEfoCycleStageDto().setIsMedicallyIndicated(1);
            arSuperDataSubmissionDto.getEfoCycleStageDto().setPerformed(arSuperDataSubmissionDto.getPremisesDto().getPremiseLabel());
        }
    }

    private void initEfoCycleDto(ArSuperDataSubmissionDto arSuperDataSubmissionDto) {
        if(arSuperDataSubmissionDto.getOfoCycleStageDto()==null){
            arSuperDataSubmissionDto.setOfoCycleStageDto(new EfoCycleStageDto());
            arSuperDataSubmissionDto.getOfoCycleStageDto().setIsMedicallyIndicated(1);
            arSuperDataSubmissionDto.getOfoCycleStageDto().setPerformed(arSuperDataSubmissionDto.getPremisesDto().getPremiseLabel());
        }
    }

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto=DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        log.info(StringUtil.changeForLog("crud_action_type is ======>"+ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE)));
        List<SelectOption> efoReasonSelectOption= MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_EFO_REASON);
        if (DataSubmissionConsts.DS_CYCLE_EFO.equals(arSuperDataSubmissionDto.getSelectionDto().getCycle())){
            ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>Oocyte Freezing Only Cycle</strong>");
            ParamUtil.setRequestAttr(bpc.request,"efoReasonSelectOption",efoReasonSelectOption);
        } else {
            ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>Sperm Freezing Only Cycle</strong>");
            List<SelectOption> sfoReasonSelectOption = IaisCommonUtils.genNewArrayList(2);
            sfoReasonSelectOption.add(0, efoReasonSelectOption.get(0));
            sfoReasonSelectOption.add(1, efoReasonSelectOption.get(3));
            ParamUtil.setSessionAttr(bpc.request,"sfoReasonSelectOption", (Serializable) sfoReasonSelectOption);
        }

    }


    @Override
    public void pageAction(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ArSuperDataSubmissionDto arSuperDataSubmissionDto= DataSubmissionHelper.getCurrentArDataSubmission(request);
        if (arSuperDataSubmissionDto != null
                && arSuperDataSubmissionDto.getSelectionDto() != null
                && arSuperDataSubmissionDto.getSelectionDto().getCycle() != null){
            if (DataSubmissionConsts.AR_CYCLE_EFO.equals(arSuperDataSubmissionDto.getSelectionDto().getCycle())
                    || DataSubmissionConsts.DS_CYCLE_EFO.equals(arSuperDataSubmissionDto.getSelectionDto().getCycle())){
                setEfoCycleStageDto(arSuperDataSubmissionDto, request, arSuperDataSubmissionDto.getOfoCycleStageDto());
            } else if (DataSubmissionConsts.AR_CYCLE_SFO.equals(arSuperDataSubmissionDto.getSelectionDto().getCycle())
                    || DataSubmissionConsts.DS_CYCLE_SFO.equals(arSuperDataSubmissionDto.getSelectionDto().getCycle())){
                setEfoCycleStageDto(arSuperDataSubmissionDto, request, arSuperDataSubmissionDto.getEfoCycleStageDto());
            }
        }
    }

    private void setEfoCycleStageDto(ArSuperDataSubmissionDto arSuperDataSubmissionDto, HttpServletRequest request, EfoCycleStageDto efoCycleStageDto) {
        String othersReason = ParamUtil.getRequestString(request, "othersReason");
        String textReason = ParamUtil.getRequestString(request, "textReason");
        String reasonSelect = ParamUtil.getRequestString(request, "reasonSelect");
        int indicated =  ParamUtil.getInt(request, "indicatedRadio");
        String startDateStr = ParamUtil.getRequestString(request, "efoDateStarted");
        String yearNum = ParamUtil.getRequestString(request,"startYear");
        String monthNum = ParamUtil.getRequestString(request,"startMonth");
        Date startDate = DateUtil.parseDate(startDateStr, AppConsts.DEFAULT_DATE_FORMAT);
        String cryopresNumStr = ParamUtil.getString(request,"cryopresNum");
        if(yearNum != null && StringUtil.isNumber(yearNum)){
            efoCycleStageDto.setYearNum(Integer.parseInt(yearNum));
        }
        if(monthNum != null && StringUtil.isNumber(monthNum)){
            efoCycleStageDto.setMonthNum(Integer.parseInt(monthNum));
        }
        ArChangeInventoryDto arChangeInventoryDto = arSuperDataSubmissionDto.getArChangeInventoryDto();

        efoCycleStageDto.setCryopresNumStr(cryopresNumStr);
        efoCycleStageDto.setCryopresNum(null);
        if (StringUtil.isNumber(cryopresNumStr)) {
            efoCycleStageDto.setCryopresNum(Integer.parseInt(cryopresNumStr));
            efoCycleStageDto.setCryopresNumStr(null);

            if (DataSubmissionConsts.DS_CYCLE_EFO.equals(arSuperDataSubmissionDto.getSelectionDto().getCycle())) {
                 if (efoCycleStageDto.getCryopresNum() == 0) {
                     String others = ParamUtil.getRequestString(request, "others");
                     efoCycleStageDto.setOthers(others);
                 }
                 arChangeInventoryDto.setFrozenSpermNum(0);
                 arChangeInventoryDto.setFrozenOocyteNum(Integer.parseInt(cryopresNumStr));
            }
            if (DataSubmissionConsts.DS_CYCLE_SFO.equals(arSuperDataSubmissionDto.getSelectionDto().getCycle())) {
                arChangeInventoryDto.setFrozenSpermNum(Integer.parseInt(cryopresNumStr));
                arChangeInventoryDto.setFrozenOocyteNum(0);
            }
        }
        efoCycleStageDto.setStartDate(startDate);
        efoCycleStageDto.setIsMedicallyIndicated(indicated);
        if(indicated==1){
            efoCycleStageDto.setReason(reasonSelect);
            if(othersReason!=null&& DataSubmissionConsts.EFO_REASON_OTHERS.equals(reasonSelect)){
                efoCycleStageDto.setOtherReason(othersReason);
            }
        }
        if(indicated==0){
            efoCycleStageDto.setReason(textReason);
        }
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto, request);
        String actionType=ParamUtil.getRequestString(request,IaisEGPConstant.CRUD_ACTION_TYPE);
        if("confirm".equals(actionType)){
            ValidationResult validationResult = WebValidationHelper.validateProperty(efoCycleStageDto, "save");
            Map<String, String> errorMap = validationResult.retrieveAll();
            verifyCommon(request, errorMap);
            if (!errorMap.isEmpty() || validationResult.isHasErrors()) {
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, "page");
            }
            valRFC(request, efoCycleStageDto);
        }
    }

    @Override
    public void prepareConfim(BaseProcessClass bpc) {

    }

    @Override
    public void doSubmission(BaseProcessClass bpc) {
        String submitFlag = (String) ParamUtil.getSessionAttr(bpc.request, SUBMIT_FLAG);
        if (!StringUtil.isEmpty(submitFlag)) {
            throw new IaisRuntimeException("Double Submit");
        }
        super.doSubmission(bpc);
        ParamUtil.setSessionAttr(bpc.request, SUBMIT_FLAG, AppConsts.YES);
    }

    protected void valRFC(HttpServletRequest request, EfoCycleStageDto efoCycleStageDto){
        if(isRfc(request)){
            ArSuperDataSubmissionDto arOldSuperDataSubmissionDto = DataSubmissionHelper.getOldArDataSubmission(request);
            if(arOldSuperDataSubmissionDto != null
                    && arOldSuperDataSubmissionDto.getSelectionDto() != null
                    && valEfoCycleDto(arOldSuperDataSubmissionDto.getSelectionDto().getCycle(),efoCycleStageDto,arOldSuperDataSubmissionDto)){
                ParamUtil.setRequestAttr(request, DataSubmissionConstant.RFC_NO_CHANGE_ERROR, AppConsts.YES);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE,ACTION_TYPE_PAGE);
            }
        }
    }

    private static Boolean valEfoCycleDto(String cycle, EfoCycleStageDto newEfoCycleStageDto, ArSuperDataSubmissionDto arOldSuperDataSubmissionDto){
        if (cycle == null || newEfoCycleStageDto == null || arOldSuperDataSubmissionDto == null){
            return Boolean.FALSE;
        }
        Boolean judgeIsEfoCycle = DataSubmissionConsts.AR_CYCLE_SFO.equals(cycle)
                || DataSubmissionConsts.DS_CYCLE_SFO.equals(cycle);
        Boolean judgeIsSfoCycle = DataSubmissionConsts.AR_CYCLE_EFO.equals(cycle)
                || DataSubmissionConsts.DS_CYCLE_EFO.equals(cycle);
        Boolean isEfoCycle = judgeIsEfoCycle || judgeIsSfoCycle;
        Boolean isSameEfoCycleDto = Boolean.FALSE;
        if (judgeIsEfoCycle){
            EfoCycleStageDto oldEfoCycleStageDto = arOldSuperDataSubmissionDto.getOfoCycleStageDto();
            if (oldEfoCycleStageDto == null){
                return Boolean.FALSE;
            }
            isSameEfoCycleDto = newEfoCycleStageDto.equals(oldEfoCycleStageDto);
        }
        Boolean isSameSfoCycleDto = Boolean.FALSE;
        if (judgeIsSfoCycle){
            EfoCycleStageDto oldEfoCycleStageDto = arOldSuperDataSubmissionDto.getEfoCycleStageDto();
            if (oldEfoCycleStageDto == null){
                return Boolean.FALSE;
            }
            isSameSfoCycleDto = newEfoCycleStageDto.equals(oldEfoCycleStageDto);
        }
        return isEfoCycle && (isSameEfoCycleDto || isSameSfoCycleDto);
    }
}
