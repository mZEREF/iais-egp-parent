package sg.gov.moh.iais.egp.bsb.common.node.simple;

import org.springframework.util.Assert;
import sg.gov.moh.iais.egp.bsb.common.node.Node;


/**
 * A simple Node implementation.
 * The content of Node is totally in the {@link ValidatableNodeValue} field,
 * and the validation of the node is determined by the field too.
 */
public class SimpleNode extends Node {
    private final ValidatableNodeValue value;

    public SimpleNode(ValidatableNodeValue value, String name, Node[] dependNodes) {
        super(name, dependNodes);
        Assert.notNull(value, "Node value must not be null");
        this.value = value;
    }

    @Override
    public boolean doValidation() {
        return value.doValidation();
    }

    @Override
    public String retrieveValidationResult() {
        return value.retrieveValidationResult();
    }

    @Override
    public void needValidation() {
        super.needValidation();
        value.clearValidationResult();
    }

    public ValidatableNodeValue getValue() {
        return value;
    }
}
