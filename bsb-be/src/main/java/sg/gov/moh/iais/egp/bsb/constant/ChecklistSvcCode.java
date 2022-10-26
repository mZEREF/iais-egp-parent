package sg.gov.moh.iais.egp.bsb.constant;

public enum ChecklistSvcCode {
    CHC("Certified High Containment (BSL-3) Facility"),
    CMC("Certified Maximum Containment (BSL-4) Facility"),
    UFT("Uncertified Facility"),
    LSP("Biomanufacturing (Large-Scale Production) Facility"),
    RFT("Registered Facility")
    ;

    private final String desc;

    ChecklistSvcCode(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
