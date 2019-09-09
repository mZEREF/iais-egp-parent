package com.ecquaria.cloud.moh.iais.constant;

public final class MessageConstant {

    //use for web page param
    public static final String PARAM_MSG_ID = "msgId";
    public static final String PARAM_ROW_ID = "rowguid";
    public static final String PARAM_CODE_KEY = "codeKey";
    public static final String PARAM_DOMAIN_TYPE = "domainType";
    public static final String PARAM_MSG_TYPE = "msgType";
    public static final String PARAM_MODULE = "module";
    public static final String PARAM_DESCRIPTION = "description";
    public static final String PARAM_MESSAGE = "message";
    public static final String PARAM_STATUS = "status";

    public static final String PARAM_MESSAGE_SEARCH = "msgSearchParam";
    public static final String PARAM_MESSAGE_SEARCH_RESULT = "msgSearchResult";

    public static final String MESSAGE_REQUEST_DTO = "msgRequestDto";

    public static final char STATUS_ACTIVE = 'Y';
    public static final char STATUS_DEACTIVATED = 'N';

    private MessageConstant() {
    }
}
