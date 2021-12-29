package com.ecquaria.cloud.moh.iais.constant;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;

/****
 *
 *   @date 12/27/2019
 *   @author zixian
 */
public class RfcConst {
    private RfcConst() {throw new IllegalStateException("RfcConst class");}
    //switch
        //control top menu
    public static final String CRUD_ACTION_TOP  = "crud_action_top";
    public static final String FORM_TAB_VALUE  = "form_tab_value";
    public static final String CRUD_ACTION_TYPE_MENU = "crud_action_type_menu";
    public static final String SWITCH_VALUE = "switch_value";
    public static final String SWITCH = "switch";


    public static final String LICENCEID = "LicenceId";
    //premises list page
    public static final String PREMISESLISTDTOS  = "PremisesListDtos";
    //edit premises page
    public static final String EDITPREMISESLISTDTO = "EditPremiseslistDto";

    public static final String PREMISESLISTQUERYDTO = "PremisesListQueryDto";

    public static final String RFCAPPSUBMISSIONDTO = "RfcAppSubmissionDto";
    public static final String APPSUBMISSIONDTORFCATTR = "AppSubmissionDtoRfcAttr";
    public static final String APPSUBMISSIONDTO =  "AppSubmissionDto";
    public static final String OLDAPPSUBMISSIONDTO =  "oldAppSubmissionDto";


    public static final String PERSONNELLISTMAP = "PersonnelListMap";
    public static final String PERSONNELEDITLIST = "PersonnelEditList";

    //reload-premises
    public static final String RELOADPREMISES = "ReloadPremises";

    public static final String RFC_BTN_OPTION_UNDO_ALL_CHANGES = "undo";
    public static final String RFC_BTN_OPTION_SKIP = "skip";

    public static final String RFC_CURRENT_EDIT = "RfcCurrentEdit";

    public static final String ACKMESSAGE ="AckMessage";
    public static final String DODRAFTCONFIG = "DoDraftConfig";
    public static final String FIRSTVIEW = "FirstView";


    //error_msg
    public static final String ERRORMAP_PREMISES = "errorMap_premises";

    public static final String EDIT_LICENSEE = "licensee";
    public static final String EDIT_PREMISES = "premises";
    public static final String EDIT_PRIMARY_DOC = "doc";
    public static final String EDIT_SERVICE = "service";

    public static final String PERSONNEL_PARAM = "personnelParam";
    public static final String PERSONNEL_RESULT = "personnelResult";

    public static final String PAYMENTPROCESS = "/hcsa-licence-web/eservice/INTERNET/MohNewApplication/PreparePayment?flag=transfer";

    public static final int DFT_MIN_PARALLEL_SIZE                       = AppConsts.DFT_MIN_PARALLEL_SIZE;

    public static final String SHOW_OTHER_ERROR                         = "showOtherError";
    public static final String SERVICE_CONFIG_CHANGE                    = "SERVICE_CONFIG_CHANGE";
    public static final String INVALID_LIC                              = "rfcInvalidLic";
    public static final String PENDING_APP                              = "rfcPendingApplication";

    public static final String PENDING_APP_VALUE                        = "errorRfcPendingApplication";
}
