package sg.gov.moh.iais.egp.bsb.constant;

public enum ChecklistSvcCode {
    BCD("(BSL-3) First and/or Second Schedule Biological Agent"),
    BCL("(BSL-3) Large-Scale Production of First Schedule Biological Agent"),
    BCF("(BSL-3) Handling of Fifth Schedule Toxin"),
    BCP("(BSL-3) Handling of Poliovirus Infectious Materials"),
    BDD("(BSL-4) First and/or Second Schedule Biological Agent"),
    BDL("(BSL-4) Large-Scale Production of First Schedule Biological Agent"),
    BDF("(BSL-4) Fifth Schedule Toxin"),
    BDP("(BSL-4) Poliovirus Infectious Materials"),
    UFD("(UF) First Schedule Biological Agent"),
    UFL("(UF) Large-Scale Production of Third Schedule Biological Agent"),
    UFF("(UF) Fifth Schedule Toxin"),
    LSF("(LSP) Large-Scale Production of First Schedule Biological Agent"),
    LST("(LSP) Large-Scale Production of Third Schedule Biological Agent"),
    RFF("(RF) Handling of Fifth Schedule Toxin for Exempted Purposes"),
    RFP("(RF) Handling of Poliovirus Potentially Infectious Materials")
    ;

    private final String desc;

    ChecklistSvcCode(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
