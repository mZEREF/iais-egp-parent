package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArCycleStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInfoDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sop.util.DateUtil;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

/**
 * ArCycleStageDtoValidator
 *
 * @author wangyu
 * @date 2021/10/27
 */
@Component
@Slf4j
public class ArCycleStageDtoValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        ArCycleStageDto arCycleStageDto = arSuperDataSubmissionDto.getArCycleStageDto();
        PatientInfoDto patientInfoDto = arSuperDataSubmissionDto.getPatientInfoDto();

        if (patientInfoDto != null && arCycleStageDto != null){
            Date startDate = DateUtil.parseDate(arCycleStageDto.getStartDate(), AppConsts.DEFAULT_DATE_FORMAT);
            PatientDto patientDto = patientInfoDto.getPatient();
            errorMap.putAll(doValidationBirthDate(patientDto, startDate));
        }

        if(arCycleStageDto.getTotalPreviouslyPreviously() != null && arCycleStageDto.getTotalPreviouslyPreviously() != 21 && StringUtil.isEmpty(arCycleStageDto.getCyclesUndergoneOverseas())){
            errorMap.put("cyclesUndergoneOverseas" ,"GENERAL_ERR0006");
        }

        if (!StringUtil.isNumber(arCycleStageDto.getCyclesUndergoneOverseas())) {
            errorMap.put("cyclesUndergoneOverseas", "GENERAL_ERR0002");
        }

        if(arCycleStageDto.validateOtherIndicationOthersMandatory()
                && StringUtil.isEmpty(arCycleStageDto.getOtherIndicationOthers())){
            errorMap.put("otherIndicationOthers" ,"GENERAL_ERR0006");
        }

        if(arCycleStageDto.validateMainIndicationOtherMandatory() && StringUtil.isEmpty(arCycleStageDto.getMainIndicationOthers())){
            errorMap.put("mainIndicationOthers" ,"GENERAL_ERR0006");
        }

        if( !arCycleStageDto.validateEnhancedCounselling(arCycleStageDto.getEnhancedCounselling())){
            errorMap.put("enhancedCounselling" ,"GENERAL_ERR0006");
        }

        if( (IaisCommonUtils.getIntByNum(arCycleStageDto.getCurrentMarriageChildren(),0)
                + IaisCommonUtils.getIntByNum(arCycleStageDto. getPreviousMarriageChildren(),0) ) < IaisCommonUtils.getIntByNum(arCycleStageDto.getDeliveredThroughChildren(),0) ){
            Map<String,String> stringStringMap = IaisCommonUtils.genNewHashMap(3);
            stringStringMap.put("field1","");
            stringStringMap.put("field2","No. of Children from Current Marriage");
            stringStringMap.put("field3","No. of Children from Previous Marriage");
            errorMap.put("deliveredThroughChildren", MessageUtil.getMessageDesc("DS_ERR011",stringStringMap).trim());
        }
        DonorValidator.validateDonors(arCycleStageDto.getDonorDtos(),errorMap,arCycleStageDto.isUsedDonorOocyte());
        return errorMap;
    }

    private Map<String, String> doValidationBirthDate(PatientDto patientDto, Date startDate){
        Map<String, String> errMsg = IaisCommonUtils.genNewHashMap();
        if (patientDto != null && startDate != null){
            Date birthDate = DateUtil.parseDate(patientDto.getBirthDate(), AppConsts.DEFAULT_DATE_FORMAT);
            if (birthDate != null && birthDate.after(startDate)){
                errMsg.put("startDate",DataSubmissionHelper.getCompareStartAge());
            }
        }
        return errMsg;
    }

    /**
     *  validate start date whether within 14 days
     * @param previousDate
     * @param arCycleStageDto
     * @return
     */
    public static Map<String, String> doValidateNowDate(String previousDate, ArCycleStageDto arCycleStageDto){
        Map<String,String> errMsg = IaisCommonUtils.genNewHashMap();
        if (StringUtil.isEmpty(previousDate) || StringUtil.isEmpty(arCycleStageDto.getStartDate())){
            return errMsg;
        }
        if (comparePreviousDateAndNowDate(previousDate, arCycleStageDto.getStartDate())){
            errMsg.put("startDate","Date started within 14 days from previous cycle");
        }
        return errMsg;
    }

    private static Boolean comparePreviousDateAndNowDate(String preDate, String nowDate){
        if (StringUtil.isEmpty(preDate) || StringUtil.isEmpty(nowDate)){
            return Boolean.FALSE;
        }
        try {
            Date date1 = Formatter.parseDateTime(preDate,AppConsts.DEFAULT_DATE_FORMAT);
            Date date2 = Formatter.parseDateTime(nowDate,AppConsts.DEFAULT_DATE_FORMAT);
            long startTime = date1.getTime();
            long endTime = date2.getTime();
            return Math.abs((int) ((endTime - startTime) / (1000 * 60 * 60 * 24))) < 14;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }
}
