package sg.gov.moh.iais.egp.bsb.common.edit;

import java.util.Set;

public class FieldEditableSetJudger implements FieldEditableJudger {
    private final Set<String> editableFields;

    public FieldEditableSetJudger(Set<String> editableFields) {
        if (editableFields == null) {
            throw new IllegalArgumentException();
        }
        this.editableFields = editableFields;
    }

    @Override
    public boolean editable(String field) {
        return editableFields.contains(field);
    }
}
