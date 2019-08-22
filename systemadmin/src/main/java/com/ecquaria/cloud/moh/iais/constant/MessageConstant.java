package com.ecquaria.cloud.moh.iais.constant;

public class MessageConstant {

    public static final String PARAM_MSG_ID = "msgId";
    public static final String PARAM_ROW_ID = "rowguid";
    public static final String PARAM_CODE_KEY = "codeKey";
    public static final String PARAM_DOMAIN_TYPE = "domainType";
    public static final String PARAM_MSG_TYPE = "msgType";
    public static final String PARAM_MODULE = "module";
    public static final String PARAM_DESCRIPTION = "description";
    public static final String PARAM_STATUS = "status";

    public static final String TYPE_INTER = "INTER";
    public static final String TYPE_INTRA = "INTRA";

    public static final String MESSAGETYPE_ERROR = "Error";
    public static final String MESSAGETYPE_ACKNOWLEDGEMENT = "Acknowledgement";

    public static final String MODULE_NEW = "New";
    public static final String MODULE_REANWAL = "Reanwal";
    public static final String MODULE_CESSATION = "Cessation";
    public static final String MODULE_AMENDMENT = "Amendment";
    public static final String MODULE_REINSTATE = "Reinstate";
    public static final String MODULE_AUDIT = "Audit";
    public static final String MODULE_COMMON = "Common";
    public static final String MODULE_OTHERS = "Others";

    public static final char STATUS_ACTIVE = 'Y';
    public static final char STATUS_DEACTIVATED = 'N';

    private MessageConstant() {
    }
}
