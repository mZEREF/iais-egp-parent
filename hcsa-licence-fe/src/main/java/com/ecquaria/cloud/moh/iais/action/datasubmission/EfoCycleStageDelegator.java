package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;

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

    }

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {

    }

    @Override
    public void returnStep(BaseProcessClass bpc) {

    }

    @Override
    public void preparePage(BaseProcessClass bpc) {
        MasterCodeUtil.retrieveOptionsByCate("");

    }

    @Override
    public void prepareConfim(BaseProcessClass bpc) {

    }

    @Override
    public void draft(BaseProcessClass bpc) {

    }

    @Override
    public void submission(BaseProcessClass bpc) {

    }

    @Override
    public void pageAction(BaseProcessClass bpc) {

    }

    @Override
    public void pageConfirmAction(BaseProcessClass bpc) {

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
