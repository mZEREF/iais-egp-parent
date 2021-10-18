package com.ecquaria.cloud.moh.iais.constant;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;

/****
 *
 *   @date 12/28/2019
 *   @author zixian
 */
public class NewApplicationConstant {
    private NewApplicationConstant() {throw new IllegalStateException("NewApplicationConstant class");}
    public static final String NEW_PREMISES = "newPremise";
    public static final String NEW_PSN = "newOfficer";
    public static final String PLEASEINDICATE = "Please indicate";
    public static final String SERVICE_SCOPE_LAB_OTHERS = "Others";
    public static final String DESIGNATION_OTHERS = "Others";

    public static final String SESSION_PARAM_APPLICATION_GROUP_ID = "appGroupId";
    public static final String SESSION_SELF_DECL_RFI_CORR_ID = "selfDeclRfiCorrId";
    public static final String SESSION_SELF_DECL_APPLICATION_NUMBER = "selfDeclApplicationNumber";
    public static final String SESSION_SELF_DECL_ACTION = "selfDeclAction";

    public static final String ATTR_RELOAD_PAYMENT_METHOD = "reloadPaymentMethod";

    public static final String ACK_STATUS_ERROR = "error";
    public static final String PREMISES_HCI_LIST= "premisesHciList";

    public static final String ACK_TITLE = "title";
    public static final String ACK_SMALL_TITLE = "smallTitle";

    public static final String TITLE_MODE_OF_SVCDLVY = ApplicationConsts.TITLE_MODE_OF_SVCDLVY;

    public static final String CO_MAP = "coMap";

}
