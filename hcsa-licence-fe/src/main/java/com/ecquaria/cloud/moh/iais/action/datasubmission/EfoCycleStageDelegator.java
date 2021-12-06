package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.EfoCycleStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInventoryDto;
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
        AuditTrailHelper.auditFunction("Assisted Reproduction", "EFO Cycle Stage");

        ArSuperDataSubmissionDto arSuperDataSubmissionDto=DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        if(arSuperDataSubmissionDto==null){
            arSuperDataSubmissionDto=new ArSuperDataSubmissionDto();
        }
        if(arSuperDataSubmissionDto.getEfoCycleStageDto()==null){
            arSuperDataSubmissionDto.setEfoCycleStageDto(new EfoCycleStageDto());
            arSuperDataSubmissionDto.getEfoCycleStageDto().setPerformed(arSuperDataSubmissionDto.getPremisesDto().getPremiseLabel());
        }
        Date startDate = DateUtil.parseDate(arSuperDataSubmissionDto.getPatientInfoDto().getPatient().getBirthDate(), AppConsts.DEFAULT_DATE_FORMAT);
        arSuperDataSubmissionDto.getEfoCycleStageDto().setYearNum(getYear(startDate,new Date()));
        arSuperDataSubmissionDto.getEfoCycleStageDto().setMonthNum(getMon(startDate,new Date()));
        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.AR_DATA_SUBMISSION,arSuperDataSubmissionDto);

    }

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("crud_action_type is ======>"+ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE)));
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>Cycle Stages</strong>");
        List<SelectOption> efoReasonSelectOption= MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_EFO_REASON);
        ParamUtil.setRequestAttr(bpc.request,"efoReasonSelectOption",efoReasonSelectOption);

    }


    @Override
    public void pageAction(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto= DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        try{
            String patientCode=arSuperDataSubmissionDto.getPatientInfoDto().getPatient().getPatientCode();
            String hciCode=arSuperDataSubmissionDto.getPremisesDto().getHciCode();
            PatientInventoryDto patientInventoryDto=arFeClient.patientInventoryByCode(patientCode,hciCode).getEntity();
            arSuperDataSubmissionDto.setPatientInventoryDto(patientInventoryDto);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            PatientInventoryDto patientInventoryDto=new PatientInventoryDto();
            arSuperDataSubmissionDto.setPatientInventoryDto(patientInventoryDto);
        }
        EfoCycleStageDto efoCycleStageDto=arSuperDataSubmissionDto.getEfoCycleStageDto();
        HttpServletRequest request=bpc.request;
        String othersReason = ParamUtil.getRequestString(request, "othersReason");
        String reasonSelect = ParamUtil.getRequestString(request, "reasonSelect");
        Integer indicated =  ParamUtil.getInt(request, "indicatedRadio");
        String startDateStr = ParamUtil.getRequestString(request, "efoDateStarted");
        Date startDate = DateUtil.parseDate(startDateStr, AppConsts.DEFAULT_DATE_FORMAT);
        efoCycleStageDto.setStartDate(startDate);
        efoCycleStageDto.setIsMedicallyIndicated(indicated);
        efoCycleStageDto.setReason(reasonSelect);
        if(othersReason!=null&& DataSubmissionConsts.EFO_REASON_OTHERS.equals(reasonSelect)){
            efoCycleStageDto.setOtherReason(othersReason);
        }
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto,bpc.request);
        String actionType=ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE);
        if("confirm".equals(actionType)){
            ValidationResult validationResult = WebValidationHelper.validateProperty(efoCycleStageDto, "save");
            Map<String, String> errorMap = validationResult.retrieveAll();
            if (!errorMap.isEmpty() || validationResult.isHasErrors()) {
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "page");
            }
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
        ArSuperDataSubmissionDto arSuperDataSubmissionDto= DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        PatientInventoryDto patientInventoryDto = new PatientInventoryDto();
        if(arSuperDataSubmissionDto.getPatientInventoryDto()!=null){
            patientInventoryDto=arSuperDataSubmissionDto.getPatientInventoryDto();
        }
    }
}
