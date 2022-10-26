package sg.gov.moh.iais.egp.bsb.common.node.simple;


/**
 * This class is simple object which can do validation for itself
 */
public class DefaultValidatableNodeValue implements ValidatableNodeValue {

    @Override
    public boolean doValidation() {
        return true;
    }

    @Override
    public String retrieveValidationResult() {
        return null;
    }

    @Override
    public void clearValidationResult() {
        // clear the object saves the validation result in the subclass
    }
}
