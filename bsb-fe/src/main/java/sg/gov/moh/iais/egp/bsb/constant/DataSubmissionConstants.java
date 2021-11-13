package sg.gov.moh.iais.egp.bsb.constant;

/**
 * @author Zhu Tangtang
 */

public final class DataSubmissionConstants {
    private DataSubmissionConstants() {}

    public static final String KEY_FAC_ID = "facId";

    public static final String SEPARATOR                   = "--v--";
    public static final String KEY_SECTION_AMT             = "sectionAmt";
    public static final String KEY_PREFIX_SCHEDULE_TYPE    = "scheduleType";
    public static final String KEY_PREFIX_BAT         = "bat";
    public static final String KEY_PREFIX_CONSUME_TYPE = "consumeType";
    public static final String KEY_PREFIX_CONSUME_QTY      = "consumedQty";
    public static final String KEY_PREFIX_MEASUREMENT_UNIT = "meaUnit";
    public static final String KEY_PREFIX_REMARKS = "remarks";
    public static final String KEY_PREFIX_ENSURE = "ensure";

    public static final String KEY_PREFIX_DISPOSE_QTY = "disposedQty";
    public static final String KEY_PREFIX_DESTRUCT_METHOD = "destructMethod";
    public static final String KEY_PREFIX_DESTRUCT_DETAILS = "destructDetails";

    public static final String KEY_PREFIX_TRANSFER_TYPE = "transferType";
    public static final String KEY_PREFIX_TRANSFER_QTY      = "transferQty";
    public static final String KEY_PREFIX_RECEIVED_FACILITY = "receivedFacility";
    public static final String KEY_PREFIX_RECEIVED_COUNTRY = "receivedCountry";
    public static final String KEY_PREFIX_EXPORT_DATE = "exportDate";
    public static final String KEY_PREFIX_PROVIDER = "provider";
    public static final String KEY_PREFIX_FLIGHT_NO = "flightNo";

    public static final String KEY_PREFIX_RECEIVE_QTY = "receivedQty";
    public static final String KEY_PREFIX_MODE_PROCUREMENT      = "modeProcurement";
    public static final String KEY_PREFIX_SOURCE_FACILITY_NAME = "sourceFacilityName";
    public static final String KEY_PREFIX_SOURCE_FACILITY_ADDRESS = "sourceFacilityAddress";
    public static final String KEY_PREFIX_SOURCE_FACILITY_CONTACT_PERSON = "sourceFacilityContactPerson";
    public static final String KEY_PREFIX_CONTACT_PERSON_EMAIL = "contactPersonEmail";
    public static final String KEY_PREFIX_CONTACT_PERSON_TEL = "contactPersonTel";
    public static final String KEY_PREFIX_ACTUAL_ARRIVAL_DATE = "actualArrivalDate";
    public static final String KEY_PREFIX_ACTUAL_ARRIVAL_TIME = "actualArrivalTime";

    public static final String KEY_DOC_TYPE_INVENTORY_TOXIN="ityToxin";
    public static final String KEY_DOC_TYPE_INVENTORY_BAT  ="ityBat";
    public static final String KEY_DOC_TYPE_OTHERS  ="others";

    public static final String KEY_CONSUME_NOTIFICATION_DTO = "consumeNotification";
    public static final String KEY_DISPOSAL_NOTIFICATION_DTO = "disposalNotification";
    public static final String KEY_EXPORT_NOTIFICATION_DTO = "exportNotification";
    public static final String KEY_RECEIPT_NOTIFICATION_DTO = "receiveNotification";
    public static final String KEY_FACILITY_INFO = "facilityInfo";
    public static final String KEY_FAC_LISTS = "facLists";
    public static final String KEY_FAC_SELECTION = "facSelection";
    public static final String KEY_ACTION_TYPE = "action_type";
    public static final String KEY_FAC_LIST_DTO = "facListDto";
    public static final String KEY_DO_SETTINGS = "doSettings";
    public static final String KEY_DOC_META = "docMeta";
    public static final String KEY_SCHEDULE_TYPE = "scheduleType";
    public static final String KEY_NON_OBJECT_ERROR = "please ensure your object has value";
    public static final String KEY_EMPTY_LIST_ERROR = "you have not key your consumeList";
}
