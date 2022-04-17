package sg.gov.moh.iais.egp.bsb.constant;

/**
 *@author YiMing
 * @version 2021/10/15 14:16
 **/
public class BioSafetyEnquiryConstants {
    private BioSafetyEnquiryConstants() {}

    public static final String PARAM_SEARCH_CHK = "searchChk";
    public static final String PARAM_SEARCH_KEY= "searchKey";
    public static final String PARAM_COUNT = "count";
    public static final String PARAM_SEARCH_TEXT = "searchText";
    public static final String PARAM_CHOICE_APPLICATION = "app";
    public static final String PARAM_CHOICE_FACILITY = "fac";
    public static final String PARAM_CHOICE_APPROVAL = "approval";
    public static final String PARAM_CHOICE_APPROVED_FACILITY_CERTIFIER = "afc";

    public static final String MESSAGE_END_DATE_EARLIER_THAN_START_DATE = "EndDate can not be earlier than startDate.";
    public static final String KEY_APPLICATION_RESULT = "appResult";
    public static final String KEY_FACILITY_RESULT = "facResult";
    public static final String KEY_APPROVAL_RESULT = "approvalResult";
    public static final String KEY_AFC_RESULT = "afcResult";

    //common
    public static final String KEY_SEARCH_DTO_APPLICATION = "appSearchDto";
    public static final String KEY_SEARCH_DTO_FACILITY = "facSearchDto";
    public static final String KEY_SEARCH_DTO_APPROVAL = "approvalSearchDto";
    public static final String KEY_SEARCH_DTO_AFC = "afcSearchDto";
    public static final String KEY_PAGE_INFO = "pageInfo";
    public static final String KEY_DOWNLOAD = "download";
    public static final String KEY_SEARCH_DTO_SUFFIX = "SearchDto";

    //url
    public static final String URL_BIO_SAFETY_INFO_FILE = "bioSafety-information-file";

    //options
    public static final String OPTIONS_APPLICATION_TYPE = "appTypeOps";
    public static final String OPTIONS_APPLICATION_STATUS = "appStatusOps";
    public static final String OPTIONS_FACILITY_CLASSIFICATION = "facClassificationOps";
    public static final String OPTIONS_FACILITY_STATUS = "facStatusOps";
    public static final String OPTIONS_APPROVE_FACILITY_CERTIFIER = "afcSelectionOps";
    public static final String OPTIONS_PROCESS_TYPE = "processTypeOps";
    public static final String OPTIONS_APPROVAL_STATUS = "approvalStatusOps";
    public static final String OPTIONS_APPROVAL_TYPE = "approvalTypeOps";
    public static final String OPTIONS_AFC_STATUS = "afcStatusOps";

    //PARAM

    public static final String PARAM_ID = "id";
    public static final String PARAM_APPLICATION_NO = "appNo";
    public static final String PARAM_APPLICATION_TYPE = "appType";
    public static final String PARAM_APPLICATION_STATUS = "appStatus";
    public static final String PARAM_APPLICATION_SUBMISSION_DATE_FROM = "appSubmissionDtFrom";
    public static final String PARAM_APPLICATION_SUBMISSION_DATE_TO = "appSubmissionDtTo";
    public static final String PARAM_APPROVAL_DATE_FROM = "approvalDtFrom";
    public static final String PARAM_APPROVAL_DATE_TO = "approvalDtTo";
    public static final String PARAM_FACILITY_CLASSIFICATION = "facilityClassification";
    public static final String PARAM_FACILITY_TYPES = "facTypes";
    public static final String PARAM_FACILITY_NAME = "facName";
    public static final String PARAM_SCHEDULE_TYPE = "scheduleType";
    public static final String PARAM_NATURE_OF_SAMPLE = "sampleNature";
    public static final String PARAM_BIOLOGICAL_AGENT = "batName";
    public static final String PARAM_RISK_LEVEL_OF_THE_BIOLOGICAL_AGENT = "riskLevel";
    public static final String PARAM_PROCESS_TYPE = "processType";
    public static final String PARAM_FACILITY_EXPIRY_DATE_FROM = "facExpiryDtFrom";
    public static final String PARAM_FACILITY_EXPIRY_DATE_TO = "facExpiryDtTo";
    public static final String PARAM_GAZETTED_AREA = "gazettedArea";
    public static final String PARAM_FACILITY_OPERATOR = "facOperator";
    public static final String PARAM_FACILITY_ADMIN = "facAdmin";
    public static final String PARAM_AUTHORISED_PERSONNEL = "facAuthorisedPerson";
    public static final String PARAM_COMMITTEE_PERSONNEL = "facCommittee";
    public static final String PARAM_FACILITY_STATUS = "facStatus";
    public static final String PARAM_APPROVED_FACILITY_CERTIFIER = "afcName";
    public static final String PARAM_NATURE_OF_THE_SAMPLE = "natureOfTheSample";
    public static final String PARAM_APPROVAL_TYPE = "approvalType";
    public static final String PARAM_APPROVAL_SUBMISSION_DATE_FROM = "approvalSubDtFrom";
    public static final String PARAM_APPROVAL_SUBMISSION_DATE_TO = "approvalSubDtTo";
    public static final String PARAM_APPROVAL_STATUS = "approvalStatus";
    public static final String PARAM_ORGANISATION_NAME = "orgName";
    public static final String PARAM_FACILITY_ADMINISTRATOR = "adminName";
    public static final String PARAM_AFC_STATUS = "afcStatus";
    public static final String PARAM_TEAM_MEMBER_NAME = "teamMemberName";
    public static final String PARAM_TEAM_MEMBER_ID = "teamMemberId";
    public static final String PARAM_APPROVED_DATE_FROM = "approvedDtFrom";
    public static final String PARAM_APPROVED_DATE_TO = "approvedDtTo";
    public static final String PARAM_APPROVED_AFC_DATE_FROM = "approvedAfcDtFrom";
    public static final String PARAM_APPROVED_AFC_DATE_TO = "approvedAfcDtTo";


}
