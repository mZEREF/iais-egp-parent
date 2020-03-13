package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.service.AuditSystemListService;
import com.ecquaria.cloud.moh.iais.service.AuditTcuListService;
import java.util.Calendar;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * AuditTaskBatchjob
 *
 * @author Guyin
 * @date 09/03/2020
 */
@Delegator("SystemAddTCUBatchJob")
@Slf4j
public class AuditTaskBatchjob {

    private static String FREQUENCY = "3";//MSOO1
    private static String CHECKFORTCUS = "Wednesday";//MSOO2
    // MSOO3
    private static String MONTHSFORPRESETTCU = "1";//MSOO4

    @Autowired
    AuditTcuListService auditTcuListService;
    @Autowired
    AuditSystemListService auditSystemListService;

    public void start(BaseProcessClass bpc) {

    }

    public void doBatchJob(BaseProcessClass bpc) {

        if(CHECKFORTCUS.equals(getWeekOfDate())){
            //get license


        }


    }

    private static String getWeekOfDate() {
        Date dt = new Date();
        String[] weekDays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0){
            w = 0;
        }
        return weekDays[w];
    }

}
