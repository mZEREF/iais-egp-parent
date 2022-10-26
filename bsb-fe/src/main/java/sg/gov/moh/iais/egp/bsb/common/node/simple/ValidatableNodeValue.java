package sg.gov.moh.iais.egp.bsb.common.node.simple;


import java.io.Serializable;


public interface ValidatableNodeValue extends Serializable {
    boolean doValidation();

    String retrieveValidationResult();

    void clearValidationResult();
}
