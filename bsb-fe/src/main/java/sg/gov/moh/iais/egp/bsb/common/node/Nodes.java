package sg.gov.moh.iais.egp.bsb.common.node;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;


@Slf4j
public class Nodes {
    private Nodes() {}

    private static final String ERR_MSG_NULL_GROUP = "Node group must not be null!";
    private static final String ERR_MSG_EMPTY_PATH = "Path must not be empty!";



    /**
     * Get the page location, if the nodePath point to a node group, we make it point to a node.
     */
    public static String expandNode(NodeGroup root, String nodePath) {
        Assert.notNull(root, ERR_MSG_NULL_GROUP);
        String destNodePath = nodePath;
        Node node = root.at(nodePath);
        if (node instanceof NodeGroup) {
            destNodePath = nodePath + root.getPathSeparator() + ((NodeGroup) node).getCurrentVisibleNode();
        }
        return destNodePath;
    }

    /**
     * Get the first not validated visible node.
     * @param nodeName node name of the group
     * @return the first not validated node; will not point to a node group
     */
    public static String expandFailNode(NodeGroup root, String nodeName) {
        String result = nodeName;
        Node failNode = root.getNode(nodeName);
        if (failNode instanceof NodeGroup) {
            result = nodeName + root.getPathSeparator() + ((NodeGroup) failNode).getFirstNotValidatedNodePath();
        }
        return result;
    }

    /**
     * Get the node path of the next node to the active node
     * @return a node path which must point to a concrete node rather than node group
     *      or null if current active node is the last node
     */
    public static String getNextNodePath(NodeGroup root) {
        String nextNodeName = root.getNextName();
        return nextNodeName == null ? null : Nodes.expandNode(root, nextNodeName);
    }

    /**
     * Get the node path of the previous node to the active node
     * @return a node path which must point to a concrete node rather than node group
     *      or null if current active node is the first node
     */
    public static String getPreviousNodePath(NodeGroup root) {
        String previousNodeName = root.getPreviousName();
        return previousNodeName == null ? null : Nodes.expandNode(root, previousNodeName);
    }


    /**
     * Set status of node to validated, check the node group contains it at the same time
     * @param root the root node group related to the node path
     * @param currentNodePath the validated node at this path
     */
    public static void passValidation(NodeGroup root, String currentNodePath) {
        Assert.notNull(root, ERR_MSG_NULL_GROUP);
        Assert.hasLength(currentNodePath, ERR_MSG_EMPTY_PATH);

        Node currentNode = root.at(currentNodePath);
        Assert.notNull(currentNode, "Invalid node path!");

        currentNode.passValidation();

        String[] pathParts = currentNodePath.split(root.getPathSeparator());
        if (pathParts.length > 1) {
            StringBuilder parentPath = new StringBuilder(pathParts[0]);
            for (int i = 1; i < pathParts.length - 1; i++) {
                parentPath.append(root.getPathSeparator()).append(pathParts[i]);
            }
            NodeGroup parentGroup = (NodeGroup) root.at(parentPath.toString());
            boolean allValidated = parentGroup.checkIfAllValidated();
            if (allValidated) {
                parentGroup.passValidation();
            }
        }
    }


    /**
     * decide the routing logic
     * @param facRegRoot root data structure of this flow
     * @param destNode destiny node path we want to go
     * @return the path we should go to; if the value is not equal to the destNode, we need to show the error info
     */
    public static String jump(NodeGroup facRegRoot, String destNode) {
        String checkedDestNode = facRegRoot.checkMemberNodeAccessCondition(destNode);
        /* If the checkedDestNode is not equals to the destNode, we need to validate them one by one until not passed.
         * Because when user click back multi times, many nodes are actually valid but the status is not validated,
         * we need to let user pass by one click. */
        while (!checkedDestNode.equals(destNode)) {
            Node ptNode = facRegRoot.at(checkedDestNode);
            if (ptNode.doValidation()) {
                passValidation(facRegRoot, checkedDestNode);
                checkedDestNode = facRegRoot.checkMemberNodeAccessCondition(destNode);
            } else {
                break;
            }
        }
        /* Update the active node key since the destiny is determined */
        facRegRoot.setActiveNodeKeyRecursively(checkedDestNode);
        return checkedDestNode;
    }
}
