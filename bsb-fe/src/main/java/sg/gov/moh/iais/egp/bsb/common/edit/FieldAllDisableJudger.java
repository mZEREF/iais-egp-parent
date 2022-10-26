package sg.gov.moh.iais.egp.bsb.common.edit;

public class FieldAllDisableJudger implements FieldEditableJudger {
    @Override
    public boolean editable(String field) {
        return false;
    }
}
