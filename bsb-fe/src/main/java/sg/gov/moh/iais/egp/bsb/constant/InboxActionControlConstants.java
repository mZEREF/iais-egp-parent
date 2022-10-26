package sg.gov.moh.iais.egp.bsb.constant;

public class InboxActionControlConstants {
    private InboxActionControlConstants() {}
    // inbox facility
    public static final String URL_FACILITY_APPLY_FOR_APPROVAL                        = "/bsb-web/eservice/INTERNET/MohApprovalBatAndActivity?facId=";
    public static final String URL_FACILITY_INVENTORY_NOTIFICATION_DATA_SUBMISSION    = "";
    public static final String URL_FACILITY_RENEW                                     = "/bsb-web/eservice/INTERNET/MohRenewalFacilityRegistration?facId=";
    public static final String URL_FACILITY_UPDATE                                    = "/bsb-web/eservice/INTERNET/MohRfcFacilityRegistration?facId=";
    public static final String URL_FACILITY_INCIDENT_REPORTING                        = "";
    public static final String URL_FACILITY_DEREGISTER                                = "";
    public static final String URL_FACILITY_DEFER_RENEWAL                             = "/bsb-web/eservice/INTERNET/DefermentOfRenewal?facId=";

    public static final String ACTION_FACILITY_APPLY_FOR_APPROVAL                     = "Apply for Approval";
    public static final String ACTION_FACILITY_INVENTORY_NOTIFICATION_DATA_SUBMISSION = "Inventory Notification/ Data Submission";
    public static final String ACTION_FACILITY_RENEW                                  = "Renew";
    public static final String ACTION_FACILITY_UPDATE                                 = "Update";
    public static final String ACTION_FACILITY_INCIDENT_REPORTING                     = "Incident Reporting";
    public static final String ACTION_FACILITY_DEREGISTER                             = "Deregister";
    public static final String ACTION_FACILITY_DEFER_RENEWAL                          = "Defer Renewal";

    // inbox approval
    public static final String URL_APPROVAL_UPDATE                                    = "/bsb-web/eservice/INTERNET/MohRfcApprovalBatAndActivity?rfcApprovalId=";
    public static final String URL_APPROVAL_CANCEL                                    = "";

    public static final String ACTION_APPROVAL_UPDATE                                 = "Update";
    public static final String ACTION_APPROVAL_CANCEL                                 = "Cancel";
}
