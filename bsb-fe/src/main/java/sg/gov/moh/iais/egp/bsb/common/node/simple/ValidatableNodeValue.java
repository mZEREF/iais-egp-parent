package sg.gov.moh.iais.egp.bsb.common.node.simple;


import java.io.Serializable;

/**
 * This class is simple object which can do validation for itself
 */
public class ValidatableNodeValue implements Serializable {

    public boolean doValidation() {
        return true;
    }

    public String retrieveValidationResult() {
        return null;
    }

    public void clearValidationResult() {
        // clear the object saves the validation result in the subclass
    }
}
