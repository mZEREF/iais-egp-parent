package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;

/**
 * AppointmentUtil
 *
 * @author junyu
 * @date 2020/7/7
 */
public class AppointmentUtil {
    public static String[] getNoReschdulingAppStatus(){

        return new String[]{
                ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION,
                ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_AO,
                ApplicationConsts.APPLICATION_STATUS_PSO_ROUTE_BACK,
                ApplicationConsts.APPLICATION_STATUS_INSPECTOR_ROUTE_BACK,
                ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_ASO,
                ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_PSO,
                ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_INSPECTOR,
                ApplicationConsts.APPLICATION_STATUS_RE_SCHEDULING_COMMON_POOL,
                ApplicationConsts.APPLICATION_STATUS_RE_SCHEDULING_APPLICANT,
                ApplicationConsts.APPLICATION_STATUS_OFFICER_RESCHEDULING_APPLICANT,
                ApplicationConsts.APPLICATION_STATUS_OFFICER_RESCHEDULING_PENDING_FE,
                ApplicationConsts.APPLICATION_STATUS_PENDING_RE_APPOINTMENT_SCHEDULING,
                ApplicationConsts.APPLICATION_STATUS_CREATE_AUDIT_TASK_CANCELED,
                ApplicationConsts.PENDING_ASO_REPLY,
                ApplicationConsts.PENDING_PSO_REPLY,
                ApplicationConsts.PENDING_INP_REPLY,
                ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION,
                ApplicationConsts.APPLICATION_STATUS_PENDING_DRAFT_EMAIL,
                ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT,
                ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW,
                ApplicationConsts.APPLICATION_STATUS_PENDING_RE_DRAFT_LETTER,
                ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_SENDING,
                ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVIEW,
                ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVISION,
                ApplicationConsts.APPLICATION_STATUS_WITHDRAWN,
                ApplicationConsts.APPLICATION_STATUS_DELETED

        };
    }
}
