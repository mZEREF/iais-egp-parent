package sg.gov.moh.iais.egp.bsb.common.edit;

public class FieldAllEditableJudger implements FieldEditableJudger {
    @Override
    public boolean editable(String field) {
        return true;
    }
}
