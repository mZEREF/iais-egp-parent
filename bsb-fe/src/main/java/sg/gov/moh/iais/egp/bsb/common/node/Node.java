package sg.gov.moh.iais.egp.bsb.common.node;

import org.springframework.util.Assert;
import java.io.Serializable;


public class Node implements Serializable {
    protected static final String ERR_MSG_NODE_NAME_NOT_EMPTY = "Node name must not be empty";

    protected final String name;
    protected boolean available;
    protected boolean validated;

    /** Node name list this node depend on, this means you can't visit this node until all nodes in this list are completed.
     *  This also mean you are free to jump to these nodes without validation of current node
     *  Attention! the order of this list is important! */
    protected Node[] dependNodes;


    public Node(String name, Node[] dependNodes) {
        Assert.hasLength(name, ERR_MSG_NODE_NAME_NOT_EMPTY);
        this.name = name;
        this.dependNodes = dependNodes == null ? new Node[0] : dependNodes.clone();
        this.available = true;
        this.validated = false;
    }


    /**
     * check if you can access this node now.
     * @return the name of this node if accessible; the name of other node if you must handle other nodes first.
     */
    public String checkAccessCondition() {
        String result = this.name;
        for (Node n : dependNodes) {
            if (!n.validated) {
                result = n.name;
                break;
            }
        }
        return result;
    }

    public String getName() {
        return name;
    }

    /**
     * This method is needed because selection may impact nodes to be shown
     * @return if this node is available
     */
    public boolean isAvailable() {
        return available;
    }

    public boolean isValidated() {
        return validated;
    }

    public void passValidation() {
        this.validated = true;
    }

    /**
     * Override this method for the consistence of {@link #doValidation()} and {@link #retrieveValidationResult()}
     */
    public void needValidation() {
        this.validated = false;
    }


    /**
     * A method used to validate this node
     * Override this method for requirement
     */
    public boolean doValidation() {
        return true;
    }

    /**
     * Retrieve the validation result, call this method after the {@link #doValidation()}
     * Call this method before {@link #needValidation()}
     * Override this method for requirement
     */
    public String retrieveValidationResult() {
        return null;
    }
}
