package sg.gov.moh.iais.egp.bsb.common.node;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import java.util.Map;


@Slf4j
public class Nodes {
    private Nodes() {}

    private static final String ERR_MSG_NULL_GROUP = "Node group must not be null!";
    private static final String ERR_MSG_EMPTY_PATH = "Path must not be empty!";


    /**
     * Processes the node path, calculate the path of its parent node group
     * @return group path or null if the node is the top level child
     */
    public static String getParentGroupPath(String nodePath, String pathSeparator) {
        Assert.hasLength(nodePath, ERR_MSG_EMPTY_PATH);
        int index = nodePath.lastIndexOf(pathSeparator);
        if (index > -1) {
            return nodePath.substring(0, index);
        } else {
            return null;
        }
    }

    /**
     * Processes the node path, calculate the name of the node
     * @return node name without parents' name
     */
    public static String getNodeName(String nodePath, String pathSeparator) {
        Assert.hasLength(nodePath, ERR_MSG_EMPTY_PATH);
        int index = nodePath.lastIndexOf(pathSeparator);
        if (index > -1) {
            return nodePath.substring(index + 1);
        } else {
            return nodePath;
        }
    }


    /**
     * Finds the NodeGroup that directly contains the node.
     * @param root a NodeGroup directly or nested-ly contains the node
     * @param nodePath path of the specific node
     * @return NodeGroup directly contains the node
     */
    public static NodeGroup findParentGroup4Node(NodeGroup root, String nodePath) {
        Assert.hasLength(nodePath, ERR_MSG_EMPTY_PATH);
        String parentPath = getParentGroupPath(nodePath, root.getPathSeparator());
        if (parentPath == null) {
            return root;
        } else {
            return (NodeGroup) root.at(parentPath);
        }
    }

    /**
     * Gets the page location, if the nodePath point to a node group, we make it point to a node.
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
     * Gets the node path of the next node to the active node
     * @return a node path which must point to a concrete node rather than node group
     *      or null if current active node is the last node
     */
    public static String getNextNodePath(NodeGroup root) {
        String nextNodeName = root.getNextName();
        return nextNodeName == null ? null : Nodes.expandNode(root, nextNodeName);
    }

    /**
     * Gets the node path of the previous node to the active node
     * @return a node path which must point to a concrete node rather than node group
     *      or null if current active node is the first node
     */
    public static String getPreviousNodePath(NodeGroup root) {
        String previousNodeName = root.getPreviousName();
        return previousNodeName == null ? null : Nodes.expandNode(root, previousNodeName);
    }

    /**
     * Makes the node turn to available.
     * Also handle nodes depend on it and the group contains it.
     */
    public static void appear(NodeGroup root, String currentNodePath) {
        Node node = root.at(currentNodePath);
        if (node.isAvailable()) {
            /* If the node is available currently, just do nothing. */
            return;
        }
        node.appear();
        NodeGroup parentGroup = findParentGroup4Node(root, currentNodePath);
        String nodeName = getNodeName(currentNodePath, root.getPathSeparator());
        parentGroup.invalidationWave(nodeName);
    }

    /**
     * Makes the node turn to unavailable.
     * Also checks if the parent node turns to all validated
     */
    public static void disappear(NodeGroup root, String currentNodePath) {
        Node node = root.at(currentNodePath);
        if (!node.isAvailable()) {
            /* If the node is unavailable currently, just do nothing. */
            return;
        }
        node.disappear();
        checkIfParentGroupPassValidation(root, currentNodePath);
    }


    /**
     * When we jump back to a node, the data in that node needs validation.
     * So we set 'validated' to false at all nodes in that path.
     * When a node turns to invalid, all nodes depends on it turns to invalid too, so
     * are the parent nodes.
     * @param root the root node group related to the node path
     * @param currentNodePath the node we jumped to and needs validation
     */
    public static void needValidation(NodeGroup root, String currentNodePath) {
        Assert.notNull(root, ERR_MSG_NULL_GROUP);
        Assert.hasLength(currentNodePath, ERR_MSG_EMPTY_PATH);

        Node currentNode = root.at(currentNodePath);
        if (!currentNode.isValidated()) {
            // if the node is not validated, we do nothing
            return;
        }

        String[] pathParts = currentNodePath.split(root.getPathSeparator(), 2);
        if (pathParts.length >  1) {
            NodeGroup subGroup = (NodeGroup) root.getNode(pathParts[0]);
            root.invalidationWave(pathParts[0]);
            needValidation(subGroup, pathParts[1]);
        } else {
            currentNode.needValidation();
        }
    }

    /**
     * Marks the node as validated, also check its parents whether all children are validated, if so
     * we need to mark parent as validated also recursively.
     * @param root the root node group related to the node path
     * @param currentNodePath the validated node at this path
     */
    public static void passValidation(NodeGroup root, String currentNodePath) {
        Assert.notNull(root, ERR_MSG_NULL_GROUP);
        Assert.hasLength(currentNodePath, ERR_MSG_EMPTY_PATH);
        Node currentNode = root.at(currentNodePath);
        currentNode.passValidation();
        checkIfParentGroupPassValidation(root, currentNodePath);
    }

    /**
     * Checks if parent of the node passed validation recursively.
     */
    public static void checkIfParentGroupPassValidation(NodeGroup root, String nodePath) {
        Assert.notNull(root, ERR_MSG_NULL_GROUP);
        Assert.hasLength(nodePath, ERR_MSG_EMPTY_PATH);
        String parentPath = getParentGroupPath(nodePath, root.getPathSeparator());
        if (parentPath != null) {
            NodeGroup parentGroup = (NodeGroup) root.at(parentPath);
            if (parentGroup.checkIfAllValidated()) {
                parentGroup.passValidation();
                checkIfParentGroupPassValidation(root, parentPath);
            }
        } else {
            if (root.checkIfAllValidated()) {
                root.passValidation();
            }
        }
    }


    /**
     * decide the routing logic
     * @param root root data structure of this flow
     * @param destNode destiny node path we want to go
     * @return the path we should go to; if the value is not equal to the destNode, we need to show the error info
     */
    public static String jump(NodeGroup root, String destNode) {
        String checkedDestNode = root.checkMemberNodeAccessCondition(destNode);
        /* If the checkedDestNode is not equals to the destNode, we need to validate them one by one until not passed.
         * Because when user click back multi times, many nodes are actually valid but the status is not validated,
         * we need to let user pass by one click. */
        /* In order to avoid infinite loop, we use a map to count fail amount for each node path,
         * if it fails 3 times for the same path, we think the code has error */
        Map<String, Integer> failCountMap = Maps.newHashMapWithExpectedSize(root.count());
        int failCount;

        while (!checkedDestNode.equals(destNode)) {
            // get fail count for this node
            failCount = failCountMap.getOrDefault(checkedDestNode, 0);
            if (failCount > 2) {
                throw new IllegalStateException("Infinite loop, node group has logic error!");
            }
            // add fail count by one
            failCountMap.put(checkedDestNode, ++failCount);

            // call validation
            Node ptNode = root.at(checkedDestNode);
            if (ptNode.doValidation()) {
                passValidation(root, checkedDestNode);
                checkedDestNode = root.checkMemberNodeAccessCondition(destNode);
            } else {
                break;
            }
        }
        /* Update the active node key since the destiny is determined */
        root.setActiveNodeKeyRecursively(checkedDestNode);
        return checkedDestNode;
    }
}
