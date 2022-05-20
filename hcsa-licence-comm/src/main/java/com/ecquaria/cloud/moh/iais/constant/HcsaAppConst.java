package com.ecquaria.cloud.moh.iais.constant;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import lombok.extern.slf4j.Slf4j;

/**
 * @Auther chenlei on 5/3/2022.
 */
@Slf4j
public final class HcsaAppConst {

    private HcsaAppConst() {throw new IllegalStateException("HcsaAppConst class");}

    public static final String NEW_PREMISES                                 = "newPremise";
    public static final String NEW_PSN                                      = "newOfficer";
    public static final String PLEASEINDICATE                               = "Please indicate";
    public static final String SERVICE_SCOPE_LAB_OTHERS                     = "Others";
    public static final String DESIGNATION_OTHERS                           = "Others";

    public static final String SESSION_PARAM_APPLICATION_GROUP_ID           = "appGroupId";
    public static final String SESSION_SELF_DECL_RFI_CORR_ID                = "selfDeclRfiCorrId";
    public static final String SESSION_SELF_DECL_APPLICATION_NUMBER         = "selfDeclApplicationNumber";
    public static final String SESSION_SELF_DECL_ACTION                     = "selfDeclAction";

    public static final String ATTR_RELOAD_PAYMENT_METHOD                   = "reloadPaymentMethod";

    public static final String PREMISES_HCI_LIST                            = "premisesHciList";

    public static final String DASHBOARDTITLE                               = "DashboardTitle";

    public static final String ACK_TITLE                                    = "title";
    public static final String ACK_SMALL_TITLE                              = "smallTitle";

    public static final String TITLE_LICENSEE                               = "Licensee Details";
    public static final String TITLE_MODE_OF_SVCDLVY                        = ApplicationConsts.TITLE_MODE_OF_SVCDLVY;
    public static final String TITLE_DOCUMENT                               = "Primary Documents";
    public static final String TITLE_SVCINFO                                = "Service-Related Information";

    public static final String CO_MAP                                       = "coMap";

    public static final String SECTION_LICENSEE                             = "licensee";
    public static final String SECTION_PREMISES                             = "premises";
    public static final String SECTION_DOCUMENT                             = "document";
    public static final String SECTION_SVCINFO                              = "information";
    public static final String SECTION_PREVIEW                              = "previewli";

    public static final String CURRENT_SVC_CODE                             = "CURRENT_SVC_CODE";

    public static final String ERRORMAP_PREMISES                            = "errorMap_premises";
    public static final String PREMISESTYPE                                 = "premisesType";
    public static final String HCSASERVICEDTO                               = "hcsaServiceDto";
    public static final String CURRENTSERVICEID                             = "currentServiceId";
    public static final String CURRENTSVCCODE                               = "currentSvcCode";
    public static final String APPSUBMISSIONDTO                             = "AppSubmissionDto";
    public static final String OLDAPPSUBMISSIONDTO                          = "oldAppSubmissionDto";
    public static final String COMMONHCSASVCDOCCONFIGDTO                    = "commonHcsaSvcDocConfigDto";
    public static final String PREMHCSASVCDOCCONFIGDTO                      = "premHcsaSvcDocConfigDto";
    public static final String RELOADAPPGRPPRIMARYDOCMAP                    = "reloadAppGrpPrimaryDocMap";
    public static final String APPGRPPRIMARYDOCERRMSGMAP                    = "appGrpPrimaryDocErrMsgMap";
    public static final String PRIMARY_DOC_CONFIG                           = "primaryDocConfig";
    public static final String SVC_DOC_CONFIG                               = "svcDocConfig";
    public static final String ALL_SVC_NAMES                                = "allSvcNames";

    public static final String REQUESTINFORMATIONCONFIG                     = "requestInformationConfig";
    public static final String ACKSTATUS                                    = "AckStatus";
    public static final String ACKMESSAGE                                   = "AckMessage";
    public static final String SERVICEALLPSNCONFIGMAP                       = "ServiceAllPsnConfigMap";
    public static final String FIRESTOPTION                                 = "Please Select";
    public static final String LIC_PREMISES_MAP                             = "LicAppGrpPremisesDtoMap";
    public static final String APP_PREMISES_MAP                             = "AppGrpPremisesDtoMap";
    public static final String PERSONSELECTMAP                              = "PersonSelectMap";
    public static final String LICPERSONSELECTMAP                           = "LicPersonSelectMap";

    public static final String DRAFTCONFIG                                  = "DraftConfig";
    public static final String GROUPLICENCECONFIG                          = "GroupLicenceConfig";
    public static final String RFI_REPLY_SVC_DTO                           = "rfiReplySvcDto";
    public static final String ASSESSMENTCONFIG                            = "AssessMentConfig";

    //page name
    public static final String APP_PAGE_NAME_LICENSEE                       = "APPPN00";
    public static final String APP_PAGE_NAME_PREMISES                       = "APPPN01";
    public static final String APP_PAGE_NAME_PRIMARY                        = "APPPN02";
    public static final String APP_SVC_PAGE_NAME_LABORATORY                 = "APPSPN01";
    public static final String APP_SVC_PAGE_NAME_GOVERNANCE_OFFICERS        = "APPSPN02";
    public static final String APP_SVC_PAGE_NAME_DISCIPLINE_ALLOCATION      = "APPSPN03";
    public static final String APP_SVC_PAGE_NAME_PRINCIPAL_OFFICERS         = "APPSPN04";
    public static final String APP_SVC_PAGE_NAME_DEPUTY_PRINCIPAL_OFFICERS  = "APPSPN05";
    public static final String APP_SVC_PAGE_NAME_DOCUMENT                   = "APPSPN06";
    public static final String APP_SVC_PAGE_NAME_SERVICE_PERSONNEL          = "APPSPN07";
    public static final String APP_SVC_PAGE_NAME_MEDALERT_PERSON            = "APPSPN08";
    public static final String SELECT_DRAFT_NO                              = "selectDraftNo";
    public static final String DRAFT_NUMBER                                 = "DraftNumber";
    //isClickEdit
    public static final String IS_EDIT                                      = "isEdit";

    public static final String LICENSEE_MAP                                 = "LICENSEE_MAP";
    public static final String LICENSEE_OPTIONS                             = "LICENSEE_OPTIONS";

    public static final String CURR_ORG_USER_ACCOUNT                        = "currOrgUserAccount";

    public static final String APP_SUBMISSIONS                              = "appSubmissionDtos";
    public static final String ACK_APP_SUBMISSIONS                          = "ackPageAppSubmissionDto";
    public static final String RFC_APP_GRP_PREMISES_DTO_LIST                = "applicationAppGrpPremisesDtoList";

    public static final String GOVERNANCEOFFICERS                           = "GovernanceOfficers";
    public static final String GOVERNANCEOFFICERSDTO                        = "GovernanceOfficersDto";
    public static final String GOVERNANCEOFFICERSDTOLIST                    = "GovernanceOfficersList";
    public static final String APPSVCRELATEDINFODTO                         = "AppSvcRelatedInfoDto";
    public static final String ERRORMAP_GOVERNANCEOFFICERS                  = "errorMap_governanceOfficers";
    public static final String RELOADSVCDOC                                 = "ReloadSvcDoc";
    public static final String SERVICEPERSONNELTYPE                         = "ServicePersonnelType";
    public static final String VEHICLEDTOLIST                               = "vehicleDtoList";
    public static final String VEHICLECONFIGDTO                             = "vehicleConfigDto";
    public static final String CLINICALDIRECTORDTOLIST                      = "clinicalDirectorDtoList";
    public static final String CLINICALDIRECTORCONFIG                       = "clinicalDirectorConfig";
    public static final String EASMTSSPECIALTYSELECTLIST                    = "easMtsSpecialtySelectList";
    public static final String EASMTSDESIGNATIONSELECTLIST                  = "easMtsDesignationSelectList";
    public static final String GENERALCHARGESDTOLIST                        = "generalChargesDtoList";
    public static final String GENERALCHARGESCONFIG                         = "generalChargesConfig";
    public static final String OTHERCHARGESDTOLIST                          = "otherChargesDtoList";
    public static final String OTHERCHARGESCONFIG                           = "otherChargesConfig";
    public static final String PREMALIGNBUSINESSMAP                         = "premAlignBusinessMap";

    //dropdown
    public static final String DROPWOWN_IDTYPESELECT                        = "IdTypeSelect";

    public static final String CURR_STEP_NAME                               = "currStepName";
    public static final String PAGE_NAME_PO                                 = "pageNamePo";
    public static final String PAGE_NAME_MAP                                = "pageNameMap";

    public static final String PERSON_OPTIONS                               = "PERSON_OPTIONS";

    public static final String SECTION_LEADER_LIST                          = "sectionLeaderList";

    public static final String PRS_SERVICE_DOWN                             = "PRS_SERVICE_DOWN";

    public static final String APP_SVC_RELATED_INFO_LIST                    = "appSvcRelatedInfoList";
    public static final String APP_SELECT_SERVICE                           = "appSelectSvc";

    public static final String ACK_STATUS_ERROR                             = "error";
    public static final String COND_TYPE_RFI                                = "isrfiSuccess";

    public static final String MAP_KEY_STATUS                               = "status";
    public static final String ERROR_APP                                    = "appError";

    public static final String ERROR_TYPE                                   = "ERROR_TYPE";
    public static final String ERROR_ROLE                                   = "ERROR_ROLE";

    public static final String CHECKED                                      = "CHECKED";
    public static final int CHECKED_AND_MSG                                 = 0;
    public static final int CHECKED_BTN                                     = 1;

    public static final String SHOW_EDIT_BTN                                = "showEditBtn";

    public static final String ACTION_LICENSEE                              = "licensee";
    public static final String ACTION_PREMISES                              = "premises";
    public static final String ACTION_DOCUMENTS                             = "documents";
    public static final String ACTION_FORMS                                 = "serviceForms";
    public static final String ACTION_PREVIEW                               = "preview";
    public static final String ACTION_JUMP                                  = "jump";
    public static final String ACTION_RFI                                   = "information";

}
