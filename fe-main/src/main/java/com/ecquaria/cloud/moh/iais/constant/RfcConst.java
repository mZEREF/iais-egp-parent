package com.ecquaria.cloud.moh.iais.constant;

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


    //premises list page
    public static final String PREMISESLISTDTOS  = "PremisesListDtos";
    //edit premises page
    public static final String EDITPREMISESLISTDTO = "EditPremiseslistDto";

    public static final String PREMISESLISTQUERYDTO = "PremisesListQueryDto";

    public static final String APPSUBMISSIONDTO =  "AppSubmissionDto";

    //reload-premises
    public static final String RELOADPREMISES = "ReloadPremises";

    //error_msg
    public static final String ERRORMAP_PREMISES = "errorMap_premises";



    //rfc amendType text
    public static final String REQUEST_FOR_CHANGE_TYPE_PREMISES_INFORMATION_TEXT = "Premises Information";
    public static final String REQUEST_FOR_CHANGE_TYPE_MEDALERT_PERSONNEL_TEXT  = "MedAlert Personnel";
    public static final String REQUEST_FOR_CHANGE_TYPE_PRINCIPAL_OFFICER_TEXT  = "Principal Officer";
    public static final String REQUEST_FOR_CHANGE_TYPE_DEPUTY_PRINCIPAL_OFFICER_TEXT  = "Deputy Principal Officer";
    public static final String REQUEST_FOR_CHANGE_TYPE_SERVICE_RELATED_INFORMATION_TEXT  = "Service-Related Information";
    public static final String REQUEST_FOR_CHANGE_TYPE_SUPPORTING_DOCUMENT_TEXT  = "Supporting Document(s)";

}
