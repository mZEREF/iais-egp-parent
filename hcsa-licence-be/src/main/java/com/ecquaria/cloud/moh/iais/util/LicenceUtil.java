package com.ecquaria.cloud.moh.iais.util;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceBeConstant;
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


}
