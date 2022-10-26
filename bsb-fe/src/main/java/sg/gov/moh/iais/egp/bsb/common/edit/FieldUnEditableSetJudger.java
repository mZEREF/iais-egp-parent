package sg.gov.moh.iais.egp.bsb.common.edit;

import java.util.Set;

public class FieldUnEditableSetJudger implements FieldEditableJudger {
    private final Set<String> unEditableFields;

    public FieldUnEditableSetJudger(Set<String> unEditableFields) {
        if (unEditableFields == null) {
            throw new IllegalArgumentException();
        }
        this.unEditableFields = unEditableFields;
    }

    @Override
    public boolean editable(String field) {
        return !unEditableFields.contains(field);
    }
}
