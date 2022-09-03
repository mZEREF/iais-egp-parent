package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.SfoCycleStageDto;
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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Delegator("sfoCycleStageDelegator")
@Slf4j
public class SfoCycleStageDelegator extends CommonDelegator{
    @Autowired
    private ArFeClient arFeClient;

    @Override
    public void start(BaseProcessClass bpc) {
        AuditTrailHelper.auditFunction("Assisted Reproduction", "SFO Cycle Stage");

        ArSuperDataSubmissionDto arSuperDataSubmissionDto= DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        if(arSuperDataSubmissionDto==null){
            arSuperDataSubmissionDto=new ArSuperDataSubmissionDto();
        }
        if(arSuperDataSubmissionDto.getSfoCycleStageDto()==null){
            arSuperDataSubmissionDto.setSfoCycleStageDto(new SfoCycleStageDto());
            arSuperDataSubmissionDto.getSfoCycleStageDto().setIsMedicallyIndicated(1);
            arSuperDataSubmissionDto.getSfoCycleStageDto().setPerformed(arSuperDataSubmissionDto.getPremisesDto().getPremiseLabel());
        }
        Date startDate = DateUtil.parseDate(arSuperDataSubmissionDto.getPatientInfoDto().getPatient().getBirthDate(), AppConsts.DEFAULT_DATE_FORMAT);
        arSuperDataSubmissionDto.getSfoCycleStageDto().setYearNum(getYear(startDate,new Date()));
        arSuperDataSubmissionDto.getSfoCycleStageDto().setMonthNum(getMon(startDate,new Date()));
        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.AR_DATA_SUBMISSION,arSuperDataSubmissionDto);

    }

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("crud_action_type is ======>"+ParamUtil.getRequestString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE)));
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>Egg Freezing Only Cycle</strong>");
        List<SelectOption> sfoReasonSelectOption= MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_EFO_REASON);
        ParamUtil.setRequestAttr(bpc.request,"sfoReasonSelectOption",sfoReasonSelectOption);

    }


    @Override
    public void pageAction(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto= DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        SfoCycleStageDto sfoCycleStageDto=arSuperDataSubmissionDto.getSfoCycleStageDto();
        HttpServletRequest request=bpc.request;
        String othersReason = ParamUtil.getRequestString(request, "othersReason");
        String textReason = ParamUtil.getRequestString(request, "textReason");
        String reasonSelect = ParamUtil.getRequestString(request, "reasonSelect");
        int indicated =  ParamUtil.getInt(request, "indicatedRadio");
        String startDateStr = ParamUtil.getRequestString(request, "efoDateStarted");
        Date startDate = DateUtil.parseDate(startDateStr, AppConsts.DEFAULT_DATE_FORMAT);
        int cryopresNum = ParamUtil.getInt(request,"cryopresNum");
        sfoCycleStageDto.setStartDate(startDate);
        sfoCycleStageDto.setCryopresNum(cryopresNum);
        sfoCycleStageDto.setIsMedicallyIndicated(indicated);
        if(indicated==1){
            sfoCycleStageDto.setReason(reasonSelect);
            if(othersReason!=null&& DataSubmissionConsts.EFO_REASON_OTHERS.equals(reasonSelect)){
                sfoCycleStageDto.setOtherReason(othersReason);
            }
        }
        if(indicated==0){
            sfoCycleStageDto.setReason(textReason);
        }
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto,bpc.request);
        String actionType=ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE);
        if("confirm".equals(actionType)){
            ValidationResult validationResult = WebValidationHelper.validateProperty(sfoCycleStageDto, "save");
            Map<String, String> errorMap = validationResult.retrieveAll();
            verifyRfcCommon(request, errorMap);
            if (!errorMap.isEmpty() || validationResult.isHasErrors()) {
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "page");
            }
            valRFC(bpc.request,sfoCycleStageDto);
        }
    }


    private int getYear(Date startDate, Date expiryDate) {
        int result = 0;
        if (startDate != null && expiryDate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(expiryDate);
            calendar.add(Calendar.DATE, 1);
            result = Period.between(LocalDate.parse(sdf.format(startDate)), LocalDate.parse(sdf.format(calendar.getTime()))).getYears();
        }

        return result;
    }


    private int getMon(Date startDate, Date endDate) {
        int result = 0;
        if (startDate != null && endDate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(endDate);
            result = Period.between(LocalDate.parse(sdf.format(startDate)), LocalDate.parse(sdf.format(calendar.getTime()))).getMonths();
        }

        return result;
    }

    @Override
    public void prepareConfim(BaseProcessClass bpc) {

    }

    protected void valRFC(HttpServletRequest request, SfoCycleStageDto sfoCycleStageDto){
        if(isRfc(request)){
            ArSuperDataSubmissionDto arOldSuperDataSubmissionDto = DataSubmissionHelper.getOldArDataSubmission(request);
            if(arOldSuperDataSubmissionDto != null && arOldSuperDataSubmissionDto.getSfoCycleStageDto()!= null && sfoCycleStageDto.equals(arOldSuperDataSubmissionDto.getEfoCycleStageDto())){
                ParamUtil.setRequestAttr(request, DataSubmissionConstant.RFC_NO_CHANGE_ERROR, AppConsts.YES);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE,ACTION_TYPE_PAGE);
            }
        }
    }
}
