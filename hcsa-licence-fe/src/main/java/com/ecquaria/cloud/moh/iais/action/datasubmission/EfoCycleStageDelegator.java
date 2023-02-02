package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArChangeInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.EfoCycleStageDto;
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
import com.ecquaria.cloud.moh.iais.service.client.ArFeClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * EfoCycleStageDelegator
 *
 * @author junyu
 * @date 2021/10/21
 */
@Delegator("efoCycleStageDelegator")
@Slf4j
public class EfoCycleStageDelegator extends CommonDelegator{
    @Autowired
    private ArFeClient arFeClient;

    @Override
    public void start(BaseProcessClass bpc) {


        ArSuperDataSubmissionDto arSuperDataSubmissionDto=DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        if (DataSubmissionConsts.DS_CYCLE_SFO.equals(arSuperDataSubmissionDto.getSelectionDto().getCycle())){
            AuditTrailHelper.auditFunction("Assisted Reproduction", "SFO Cycle Stage");
        } else {
            AuditTrailHelper.auditFunction("Assisted Reproduction", "OFO Cycle Stage");
        }
        if(arSuperDataSubmissionDto==null){
            arSuperDataSubmissionDto=new ArSuperDataSubmissionDto();
        }
        if(arSuperDataSubmissionDto.getEfoCycleStageDto()==null){
            arSuperDataSubmissionDto.setEfoCycleStageDto(new EfoCycleStageDto());
            arSuperDataSubmissionDto.getEfoCycleStageDto().setIsMedicallyIndicated(1);
            arSuperDataSubmissionDto.getEfoCycleStageDto().setPerformed(arSuperDataSubmissionDto.getPremisesDto().getPremiseLabel());
        }
        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.AR_DATA_SUBMISSION,arSuperDataSubmissionDto);

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
        ArSuperDataSubmissionDto arSuperDataSubmissionDto= DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        EfoCycleStageDto efoCycleStageDto=arSuperDataSubmissionDto.getEfoCycleStageDto();
        HttpServletRequest request=bpc.request;
        String othersReason = ParamUtil.getRequestString(request, "othersReason");
        String textReason = ParamUtil.getRequestString(request, "textReason");
        String reasonSelect = ParamUtil.getRequestString(request, "reasonSelect");
        int indicated =  ParamUtil.getInt(request, "indicatedRadio");
        String startDateStr = ParamUtil.getRequestString(request, "efoDateStarted");
        String yearNum = ParamUtil.getRequestString(request,"startYear");
        String monthNum = ParamUtil.getRequestString(request,"startMonth");
        Date startDate = DateUtil.parseDate(startDateStr, AppConsts.DEFAULT_DATE_FORMAT);
        String cryopresNum = ParamUtil.getString(request,"cryopresNum");
        if(yearNum != null && StringUtil.isNumber(yearNum)){
            efoCycleStageDto.setYearNum(Integer.parseInt(yearNum));
        }
        if(monthNum != null && StringUtil.isNumber(monthNum)){
            efoCycleStageDto.setMonthNum(Integer.parseInt(monthNum));
        }
        ArChangeInventoryDto arChangeInventoryDto = arSuperDataSubmissionDto.getArChangeInventoryDto();
        if (cryopresNum != null && StringUtil.isNumber(cryopresNum)) {
            efoCycleStageDto.setCryopresNum(Integer.parseInt(cryopresNum));
             if (DataSubmissionConsts.DS_CYCLE_EFO.equals(arSuperDataSubmissionDto.getSelectionDto().getCycle())) {
                 if (efoCycleStageDto.getCryopresNum() == 0) {
                     String others = ParamUtil.getRequestString(request, "others");
                     efoCycleStageDto.setOthers(others);
                 }
                 arChangeInventoryDto.setFrozenSpermNum(0);
                 arChangeInventoryDto.setFrozenOocyteNum(Integer.parseInt(cryopresNum));
            }
            if (DataSubmissionConsts.DS_CYCLE_SFO.equals(arSuperDataSubmissionDto.getSelectionDto().getCycle())) {
                arChangeInventoryDto.setFrozenSpermNum(Integer.parseInt(cryopresNum));
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
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto,bpc.request);
        String actionType=ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE);
        if("confirm".equals(actionType)){
            ValidationResult validationResult = WebValidationHelper.validateProperty(efoCycleStageDto, "save");
            Map<String, String> errorMap = validationResult.retrieveAll();
            verifyCommon(request, errorMap);
            if (!errorMap.isEmpty() || validationResult.isHasErrors()) {
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "page");
            }
            valRFC(bpc.request,efoCycleStageDto);
        }
    }


    @Override
    public void prepareConfim(BaseProcessClass bpc) {

    }

    protected void valRFC(HttpServletRequest request, EfoCycleStageDto efoCycleStageDto){
        if(isRfc(request)){
            ArSuperDataSubmissionDto arOldSuperDataSubmissionDto = DataSubmissionHelper.getOldArDataSubmission(request);
            if(arOldSuperDataSubmissionDto != null && arOldSuperDataSubmissionDto.getEfoCycleStageDto()!= null && efoCycleStageDto.equals(arOldSuperDataSubmissionDto.getEfoCycleStageDto())){
                ParamUtil.setRequestAttr(request, DataSubmissionConstant.RFC_NO_CHANGE_ERROR, AppConsts.YES);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE,ACTION_TYPE_PAGE);
            }
        }
    }
}
