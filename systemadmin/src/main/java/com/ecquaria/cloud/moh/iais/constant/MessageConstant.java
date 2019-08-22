package com.ecquaria.cloud.moh.iais.constant;

public interface MessageConstant {
    String CODE_ID = "CODE_ID";
    String CODE_KEY = "CODE_KEY";
    String TYPE = "MESSAGE_TYPE";
    String MODULE = "MODULE";
    String DESCRIPTION = "DESCRIPTION";
    String STATUS = "STATUS";

    String TYPE_INTER = "INTER";
    String TYPE_INTRA = "INTRA";

    String MESSAGETYPE_ERROR = "Error";
    String MESSAGETYPE_ACKNOWLEDGEMENT = "Acknowledgement";

    String MODULE_NEW = "New";
    String MODULE_REANWAL = "Reanwal";
    String MODULE_CESSATION = "Cessation";
    String MODULE_AMENDMENT = "Amendment";
    String MODULE_REINSTATE = "Reinstate";
    String MODULE_AUDIT = "Audit";
    String MODULE_COMMON = "Common";
    String MODULE_OTHERS = "Others";

    char STATUS_ACTIVE = 'Y';
    char STATUS_DEACTIVATED = 'N';
}
