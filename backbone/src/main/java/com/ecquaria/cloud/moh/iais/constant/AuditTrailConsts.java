package com.ecquaria.cloud.moh.iais.constant;

public class AuditTrailConsts {
    public static final int OPERATION_INTERNET_LOGIN             = 1;
    public static final int OPERATION_INTERNET_LOGOUT            = 2;
    public static final int OPERATION_INTERNET_LOGIN_FAIL        = 3;
    public static final int OPERATION_INTERNET_VIEW_RECORD       = 4;
    public static final int OPERATION_INTERNET_VALIDATION_FAIL   = 5;
    public static final int OPERATION_INTRANET_LOGIN             = 6;
    public static final int OPERATION_INTRANET_LOGOUT            = 7;
    public static final int OPERATION_INTRANET_LOGIN_FAIL        = 8;
    public static final int OPERATION_INTRANET_VIEW_RECORD       = 9;
    public static final int OPERATION_INTRANET_ALIDATION_FAIL    = 10;
    public static final int OPERATION_INSERT_INTERNET            = 11;
    public static final int OPERATION_UPDATE_INTERNET            = 12;
    public static final int OPERATION_DELETE_INTERNET            = 13;
    public static final int OPERATION_INSERT_INTRANET            = 14;
    public static final int OPERATION_UPDATE_INTRANET            = 15;
    public static final int OPERATION_DELETE_INTRANET            = 16;
    public static final int OPERATION_INSERT_BATCHJOB            = 17;
    public static final int OPERATION_UPDATE_BATCHJOB            = 18;
    public static final int OPERATION_DELETE_BATCHJOB            = 19;

    private AuditTrailConsts(){}
}
