package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.EfoCycleStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    public void start(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.CRUD_ACTION_TYPE, "page");
        ArSuperDataSubmissionDto arSuperDataSubmissionDto=new ArSuperDataSubmissionDto();

//        arSuperDataSubmissionDto.setEfoCycleStageDto(new EfoCycleStageDto());
//        Date birthDate= null;
//        try {
//            birthDate = Formatter.parseDateTime(arSuperDataSubmissionDto.getPatientInfoDto().getPatient().getBirthDate(), AppConsts.DEFAULT_DATE_FORMAT);
//        } catch (Exception e) {
//            log.error(e.getMessage(),e);
//        }
//        arSuperDataSubmissionDto.getEfoCycleStageDto().setYearNum(getYear(new Date(),new Date()));
//        arSuperDataSubmissionDto.getEfoCycleStageDto().setMonthNum(getMon(new Date(),new Date()));
//        arSuperDataSubmissionDto.getEfoCycleStageDto().setPerformed("");
//        arSuperDataSubmissionDto.getEfoCycleStageDto().setSubmissionId(MasterCodeUtil.CATE_ID_EFO_REASON);
//
//        arSuperDataSubmissionDto.getEfoCycleStageDto().setStartDate(new Date());

        ParamUtil.setSessionAttr(bpc.request,"arSuperDataSubmissionDto",arSuperDataSubmissionDto);

    }

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("crud_action_type is ======>"+ParamUtil.getRequestString(bpc.request,IntranetUserConstant.CRUD_ACTION_TYPE)));

    }

    @Override
    public void returnStep(BaseProcessClass bpc) {

    }

    @Override
    public void preparePage(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto= (ArSuperDataSubmissionDto) ParamUtil.getSessionAttr(bpc.request,"arSuperDataSubmissionDto");
        arSuperDataSubmissionDto.setEfoCycleStageDto(new EfoCycleStageDto());
        Date birthDate= null;
        try {
            birthDate = Formatter.parseDateTime(arSuperDataSubmissionDto.getPatientInfoDto().getPatient().getBirthDate(), AppConsts.DEFAULT_DATE_FORMAT);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        arSuperDataSubmissionDto.getEfoCycleStageDto().setYearNum(getYear(new Date(),new Date()));
        arSuperDataSubmissionDto.getEfoCycleStageDto().setMonthNum(getMon(new Date(),new Date()));
        arSuperDataSubmissionDto.getEfoCycleStageDto().setPerformed("");
        arSuperDataSubmissionDto.getEfoCycleStageDto().setSubmissionId(MasterCodeUtil.CATE_ID_EFO_REASON);

        List<SelectOption> efoReasonSelectOption= MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_EFO_REASON);
        ParamUtil.setRequestAttr(bpc.request,"efoReasonSelectOption",efoReasonSelectOption);

    }

    @Override
    public void prepareConfim(BaseProcessClass bpc) {

    }

    @Override
    public void draft(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.CRUD_ACTION_TYPE, "page");

    }

    @Override
    public void submission(BaseProcessClass bpc) {

    }

    @Override
    public void pageAction(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto= (ArSuperDataSubmissionDto) ParamUtil.getSessionAttr(bpc.request,"arSuperDataSubmissionDto");
        EfoCycleStageDto efoCycleStageDto=arSuperDataSubmissionDto.getEfoCycleStageDto();
        HttpServletRequest request=bpc.request;
        String othersReason = ParamUtil.getRequestString(request, "othersReason");
        String reasonSelect = ParamUtil.getRequestString(request, "reasonSelect");
        Integer indicated = (Integer) ParamUtil.getRequestAttr(request, "indicatedRadio");
        String startDateStr = ParamUtil.getRequestString(request, "efoDateStarted");
        Date startDate = DateUtil.parseDate(startDateStr, AppConsts.DEFAULT_DATE_FORMAT);
        efoCycleStageDto.setStartDate(startDate);
        efoCycleStageDto.setIsMedicallyIndicated(indicated);
        efoCycleStageDto.setReason(reasonSelect);
        if(othersReason!=null){
            efoCycleStageDto.setOthersReason(othersReason);
        }
        ValidationResult validationResult = WebValidationHelper.validateProperty(efoCycleStageDto, "save");
        Map<String, String> errorMap = validationResult.retrieveAll();
        ParamUtil.setSessionAttr(bpc.request, "arSuperDataSubmissionDto", arSuperDataSubmissionDto);
        if (!errorMap.isEmpty() || validationResult.isHasErrors()) {
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.CRUD_ACTION_TYPE, "page");
            return;
        }


        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.CRUD_ACTION_TYPE, "confirm");

    }

    @Override
    public void pageConfirmAction(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto= (ArSuperDataSubmissionDto) ParamUtil.getSessionAttr(bpc.request,"arSuperDataSubmissionDto");

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
}
