package sg.gov.moh.iais.egp.bsb.common.node;

import org.springframework.util.Assert;
import java.io.Serializable;


/** This class is designed for easily jumping between pages in one flow.
 * Each page is a Node, each standalone flow (with steps) is a NodeGroup.
 * @see sg.gov.moh.iais.egp.bsb.common.node.NodeGroup
 */
public class Node implements Serializable {
    protected static final String ERR_MSG_NODE_NAME_NOT_EMPTY = "Node name must not be empty";

    /** Name of the node is the identifier, so name should be unique in one node group. */
    protected final String name;

    /** Available means if this node 'exists', if the value is false, we should treat this node as if it does not
     * exist. This means:
     * <ol>
     *     <li>Ignore this node when doing 'next' or 'previous'</li>
     *     <li>Treat this node as validated when checking dependencies</li>
     *     <li>When a node turns to available from unavailable, the group contains it turns to not validated</li>
     *     <li>When a node turns to available from unavailable, nodes depend on it turns to not validated</li>
     *     <li>When a node turns to unavailable from available, group should check if it becomes validated</li>
     * </ol>
     * Since the value change will impact group status, you should not just call {@link #appear()} or
     * {@link #disappear()}.
     * Please use {@link sg.gov.moh.iais.egp.bsb.common.node.Nodes#appear(NodeGroup, String)} instead. */
    protected boolean available;

    /** Validation status of this node, usually status change of one node will affect the status of node group.
     * So it's usually incorrect to just call {@link #passValidation()} or {@link #needValidation()} to one node.
     * Please use {@link sg.gov.moh.iais.egp.bsb.common.node.Nodes#passValidation(NodeGroup, String)} instead. */
    protected boolean validated;

    /** Node name list this node depends on, this means you can't visit current node until all nodes in this list
     * are completely validated.
     * Attention! the order of this list is important! It always checks in the order in the array. */
    protected Node[] dependNodes;


    /** This constructor takes the name and dependency nodes to avoid empty name or null dependNodes.
     * This method will set the init status of the node instance.
     * This class does not have a constructor without argument, which means you always need to re-create the
     * node group, and can not recover the group form a serialized bytecode or JSON. */
    public Node(String name, Node[] dependNodes) {
        Assert.hasLength(name, ERR_MSG_NODE_NAME_NOT_EMPTY);
        this.name = name;
        this.dependNodes = dependNodes == null ? new Node[0] : dependNodes.clone();
        this.available = true;
        this.validated = false;
    }


    /**
     * Checks whether this node is accessible.
     * This method will check all dependency nodes.
     * @return the name of this node if accessible; the name of the other node if you must handle other nodes first.
     */
    public String checkAccessCondition() {
        String result = this.name;
        for (Node n : dependNodes) {
            if (n.available && !n.validated) {
                result = n.name;
                break;
            }
        }
        return result;
    }

    /**
     * Checks if this node depends on the specific node name
     * @return true if current node contains a dependency with the same node name
     */
    public boolean dependsOn(String nodeName) {
        Assert.hasLength(nodeName, ERR_MSG_NODE_NAME_NOT_EMPTY);
        boolean depends = false;
        for (Node n : dependNodes) {
            if (nodeName.equals(n.getName())) {
                depends = true;
                break;
            }
        }
        return depends;
    }


    public String getName() {
        return name;
    }

    public boolean isAvailable() {
        return available;
    }

    /** Prefer {@link sg.gov.moh.iais.egp.bsb.common.node.Nodes#appear(NodeGroup, String)},
     * it will handle impact of calling this method. */
    public void appear() {
        this.available = true;
    }

    /** Prefer {@link sg.gov.moh.iais.egp.bsb.common.node.Nodes#disappear(NodeGroup, String)},
     * it will handle impact of calling this method. */
    public void disappear() {
        this.available = false;
    }

    public Node[] getDependNodes() {
        return dependNodes.clone();
    }

    /** The only way to make the node suitable to new group structure. Unless you know what you are doing,
     * try to use {@link sg.gov.moh.iais.egp.bsb.common.node.NodeGroup#replaceNode(Node)} or
     * {@link sg.gov.moh.iais.egp.bsb.common.node.NodeGroup#reorganizeNodes(Node[], String)} instead
     */
    public void setDependNodes(Node[] dependNodes) {
        this.dependNodes = dependNodes == null ? new Node[0] : dependNodes.clone();
    }

    public boolean isValidated() {
        return validated;
    }

    /** Prefer {@link sg.gov.moh.iais.egp.bsb.common.node.Nodes#passValidation(NodeGroup, String)},
     * it will handle impact of calling this method. */
    public void passValidation() {
        this.validated = true;
    }

    /**
     * Override this method for the consistence of {@link #doValidation()} and {@link #retrieveValidationResult()}
     * <p>
     * Prefer {@link sg.gov.moh.iais.egp.bsb.common.node.Nodes#needValidation(NodeGroup, String)}
     */
    public void needValidation() {
        this.validated = false;
    }


    /**
     * A method used to validate this node.
     * Override this method for requirement
     */
    public boolean doValidation() {
        return true;
    }

    /**
     * Retrieve the validation result, call this method after the {@link #doValidation()}.
     * Call this method before {@link #needValidation()}.
     * Override this method for requirement.
     */
    public String retrieveValidationResult() {
        return null;
    }
}
