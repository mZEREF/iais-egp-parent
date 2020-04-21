package com.ecquaria.cloud.moh.iais.util;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.risk.RiskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceBeConstant;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * LicenceUtil
 *
 * @author suocheng
 * @date 2/7/2020
 */

public class LicenceUtil {
    public static Date getExpiryDate(Date startDate, AppPremisesRecommendationDto appPremisesRecommendationDto){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.DATE,-1);
        if(appPremisesRecommendationDto != null){
            switch (appPremisesRecommendationDto.getChronoUnit()){
                case RiskConsts.YEAR :
                    calendar.add(Calendar.YEAR,appPremisesRecommendationDto.getRecomInNumber());
                    break;
                case RiskConsts.MONTH :
                    calendar.add(Calendar.MONTH,appPremisesRecommendationDto.getRecomInNumber());
                    break;
                case RiskConsts.WEEK  :
                    calendar.add(Calendar.WEEK_OF_YEAR,appPremisesRecommendationDto.getRecomInNumber());
                    break;
            }
        }else{
            calendar.add(Calendar.YEAR,1);
        }
        return  calendar.getTime();
    }
    public static Date getExpiryDate(Date startDate, int yearLength){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.YEAR,yearLength);
        return  calendar.getTime();
    }
    public static List<SelectOption> getResultsLastCompliance(){
        List<SelectOption> selectOptions = IaisCommonUtils.genNewArrayList();
        selectOptions.add(new SelectOption(HcsaLicenceBeConstant.RESULT_LAST_COMPLIANCE_FULL_CODE,HcsaLicenceBeConstant.RESULT_LAST_COMPLIANCE_FULL_NAME));
        selectOptions.add(new SelectOption(HcsaLicenceBeConstant.RESULT_LAST_COMPLIANCE_PARTIAL_CODE,HcsaLicenceBeConstant.RESULT_LAST_COMPLIANCE_PARTIAL_NAME));
        return  selectOptions;
    }

    public static List<SelectOption> getPremisesType(){
        List<SelectOption> selectOptions = IaisCommonUtils.genNewArrayList();
        selectOptions.add(new SelectOption( ApplicationConsts.PREMISES_TYPE_ON_SITE,ApplicationConsts.PREMISES_TYPE_ON_SITE_SHOW));
        selectOptions.add(new SelectOption(ApplicationConsts.PREMISES_TYPE_OFF_SITE,ApplicationConsts.PREMISES_TYPE_OFF_SITE_SHOW));
        selectOptions.add(new SelectOption(ApplicationConsts.PREMISES_TYPE_CONVEYANCE,ApplicationConsts.PREMISES_TYPE_CONVEYANCE_SHOW));
        return  selectOptions;
    }

    public static String getSelectOptionTextFromSelectOptions( List<SelectOption> selectOptions,String selectOptionVal){
        if (!IaisCommonUtils.isEmpty(selectOptions) && !StringUtil.isEmpty(selectOptionVal)) {
            for (SelectOption temp : selectOptions) {
                if (selectOptionVal.equals(temp.getValue())) {
                    return temp.getText();
                }
            }
        }
        return null;
    }

    public static   List<SelectOption> getRiskYearsOrMonthDrop(Boolean isYear){
        List<SelectOption> selectOptions;
        if(isYear){
            String dateTypeText =  MasterCodeUtil.getCodeDesc(RiskConsts.YEAR);
            selectOptions = new ArrayList<>(6);
            for(int i = 0;i<6;i++)
                selectOptions.add(new SelectOption(String.valueOf(i),i + " " + dateTypeText));
        }else {
            String dateTypeText =  MasterCodeUtil.getCodeDesc(RiskConsts.MONTH);
            selectOptions = new ArrayList<>(12);
            for(int i = 0;i<12;i++)
                selectOptions.add(new SelectOption(String.valueOf(i),i + " " + dateTypeText));
        }
        return selectOptions;
    }
}
