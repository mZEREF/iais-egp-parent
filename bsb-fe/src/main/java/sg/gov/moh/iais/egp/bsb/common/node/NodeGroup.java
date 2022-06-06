package sg.gov.moh.iais.egp.bsb.common.node;

import com.google.common.collect.Maps;
import org.springframework.util.Assert;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * A NodeGroup is a group of Nodes, NodeGroup is a subclass of Node,
 * so a NodeGroup can have other groups as it's member nodes.
 * <p>
 * A NodeGroup can use a path format to retrieve a Node, you must understand the pathSeparator (default is underscore)
 * to use this. For example:
 * <pre>
 *   Node abc = nodeGroup.at("a_b_c");
 * </pre>
 * The path assumes this group contains a member 'a', and 'a' is a NodeGroup also; 'a' contains 'b', 'b' is also
 * a NodeGroup, the destiny Node we want is the member 'c' in the 'b' group.
 * <p>
 * A NodeGroup is not used to show a page, it is not visible. We can get the visible Node of this group with the
 * {@link #getCurrentVisibleNode} method. This method will return a member Node path that is not a group.
 */
public class NodeGroup extends Node {
    protected static final String ERR_MSG_NODE_NOT_NULL = "Node can not be null";
    protected static final String ERR_MSG_NODE_GROUP_NOT_EMPTY = "Nodes can not be empty";
    protected static final String ERR_MSG_INVALID_ACTIVE_NODE = "The node group does not contain the active node key!";
    protected static final String ERR_MSG_NODE_NAME_NOT_EMPTY = "Node name must not be empty";
    protected static final String ERR_MSG_INVALID_PATH = "Path must not be empty!";

    /**
     * This map contains members of this group.
     * This map must never be empty.
     * The key is the name of the node.
     */
    private final LinkedHashMap<String, Node> nodes;

    /** Active node of this group; never be null */
    private String activeNodeKey;

    /** Separator used to separate the node path.
     * <p>
     * Attention, the logic of this class depends on string processing,
     * so the name of nodes MUST not contain the separator */
    private final String pathSeparator;


    /** This class does not contain a constructor without argument, it means you always need to re-create the
     * node group by hand, and can not recover it from a serialized bytecode or JSON.
     * @param name name of the NodeGroup (NodeGroup is a Node)
     * @param dependNodes dependency nodes of this node group
     * @param nodes members in the node group
     * @param pathSeparator path separator used by calculating logic
     */
    public NodeGroup(String name, Node[] dependNodes, Map<String, Node> nodes, String pathSeparator) {
        super(name, dependNodes);
        Assert.notEmpty(nodes, "A node group must contains nodes!");
        Assert.hasLength(pathSeparator, "Path separator can not be empty!");
        this.nodes = new LinkedHashMap<>(nodes);
        this.activeNodeKey = getFirstNodeKey();
        this.pathSeparator = pathSeparator;
    }

    private NodeGroup(Builder builder) {
        this(builder.getName(), builder.getDependNodes(), builder.getNodes(), builder.getPathSeparator());
    }

    public static class Builder {
        private String name;
        private List<Node> dependNodes;
        private LinkedHashMap<String, Node> nodes;
        private String pathSeparator;

        public Builder() {
            dependNodes = new ArrayList<>();
            nodes = new LinkedHashMap<>();
            pathSeparator = "_";
        }

        public Builder(String name) {
            this();
            this.name = name;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder dependNodes(Node[] dependNodes) {
            this.dependNodes = new ArrayList<>(Arrays.asList(dependNodes));
            return this;
        }

        public Builder addDependNode(Node dependNode) {
            this.dependNodes.add(dependNode);
            return this;
        }

        public Builder addDependNodes(Node[] dependNodes) {
            Collections.addAll(this.dependNodes, dependNodes);
            return this;
        }

        public Builder addNode(Node node) {
            this.nodes.put(node.getName(), node);
            return this;
        }

        public Builder setNode(Map<String, Node> nodes) {
            Assert.notEmpty(nodes, ERR_MSG_NODE_GROUP_NOT_EMPTY);
            this.nodes = new LinkedHashMap<>(nodes);
            return this;
        }

        public Builder pathSeparator(String pathSeparator) {
            this.pathSeparator = pathSeparator;
            return this;
        }

        public NodeGroup build() {
            return new NodeGroup(this);
        }

        public String getName() {
            return name;
        }

        public Node[] getDependNodes() {
            return dependNodes.toArray(new Node[0]);
        }

        public Map<String, Node> getNodes() {
            return nodes;
        }

        public String getPathSeparator() {
            return pathSeparator;
        }
    }

    public String getPathSeparator() {
        return pathSeparator;
    }

    /** Gets the amount of the children/members */
    public int count() {
        return this.nodes.size();
    }

    /**
     * Checks if this group contains an available member with the specific name
     * @param name of Node
     * @return true if this group contains an available Node with specific name
     */
    public boolean contains(String name) {
        Node node = this.nodes.get(name);
        return node != null && node.isAvailable();
    }

    /** Checks if this group in fact contains the member no matter if it is available */
    public boolean inFactContains(String name) {
        return this.nodes.containsKey(name);
    }

    /** Checks if this group contains a specific path */
    public boolean containsPath(String path) {
        return at(path) != null;
    }

    /** Get a copy of the child/member nodes */
    public List<Node> getAllNodes() {
        return new ArrayList<>(this.nodes.values());
    }

    /** Gets the Node by the specified key
     * @return a Node (it may be a subtype of Node) if found; null if not */
    public Node getNode(String key) {
        return this.nodes.get(key);
    }

    /** Get the Node relative to this node group.
     * <p>
     * For example, the path is 'a_b_c', if this group doesn't contain 'a',
     * or if we find the 'a_b' but it doesn't contain 'c', this method will return null.
     * @return a Node if found; null if not */
    public Node at(String path) {
        Assert.hasLength(path, ERR_MSG_INVALID_PATH);
        Node destNode = null;
        String[] namePart = path.split(pathSeparator, 2);
        if (namePart.length > 1) {
            Node subGroup = getNode(namePart[0]);
            // if subGroup is null or not a NodeGroup, we stop searching, and return null
            if (subGroup instanceof NodeGroup) {
                destNode = ((NodeGroup) subGroup).at(namePart[1]);
            }
        } else {
            destNode = getNode(path);
        }
        return destNode;
    }

    /** Gets the first available node key of the group */
    private String getFirstNodeKey() {
        String key = null;
        for (Map.Entry<String, Node> stringNodeEntry : nodes.entrySet()) {
            Node node = stringNodeEntry.getValue();
            if (node.isAvailable()) {
                key = node.getName();
                break;
            }
        }
        if (key == null) {
            throw new IllegalStateException("No available group member!");
        }
        return key;
    }

    /**
     * This method will try to get the next node of subgroup if active node is a group.
     * @return the path of the next node; return null if current node is the last node.
     *      return key path is not ensured to point to a visible node, it may be a path point to a node group.
     */
    public String getNextName() {
        Node activeNode = getNode(activeNodeKey);
        if (activeNode == null) {
            throw new IllegalStateException(ERR_MSG_INVALID_ACTIVE_NODE);
        }
        /* If current active node is a group, we need to get the next step in the subgroup recursively. */
        if (activeNode instanceof NodeGroup) {
            String subNextName = ((NodeGroup) activeNode).getNextName();
            /* if it's null, the subgroup meets the last node, we need to get the next node of current group */
            if (subNextName != null) {
                return activeNodeKey + pathSeparator + subNextName;
            }
        }
        Iterator<Map.Entry<String, Node>> nodeIter = nodes.entrySet().iterator();
        // move iterator to active node
        while (nodeIter.hasNext()) {
            if (activeNodeKey.equals(nodeIter.next().getKey())) {
                break;
            }
        }
        // find an available next node, if not found, return null
        String nextName = null;
        while (nodeIter.hasNext()) {
            Node nextNode = nodeIter.next().getValue();
            if (nextNode.isAvailable()) {
                nextName = nextNode.getName();
                break;
            }
        }
        return nextName;
    }

    /**
     * This method will try to get the previous node of sub group if active node is a group.
     * @return the key path of the previous node; return null if current node is the first node.
     *      return key path is not ensured to point to a visible node, it may be a path point to a node group.
     */
    public String getPreviousName() {
        Node activeNode = getNode(activeNodeKey);
        if (activeNode == null) {
            throw new IllegalStateException(ERR_MSG_INVALID_ACTIVE_NODE);
        }
        /* If current active node is a group, we need to get the previous step in the subgroup recursively. */
        if (activeNode instanceof NodeGroup) {
            String subPreviousName = ((NodeGroup) activeNode).getPreviousName();
            /* if it's null, the subgroup meets the first node, we need to get the previous node of current group */
            if (subPreviousName != null) {
                return activeNodeKey + pathSeparator + subPreviousName;
            }
        }
        Iterator<Map.Entry<String, Node>> nodeIter = nodes.entrySet().iterator();
        /* find the previous available node */
        String prevKey = null;
        while (nodeIter.hasNext()) {
            Node node = nodeIter.next().getValue();
            if (activeNodeKey.equals(node.getName())) {
                break;
            }
            if (node.isAvailable()) {
                prevKey = node.getName();
            }
        }
        return prevKey;
    }


    /**
     * Replace a member with the same node name.
     * If this group doesn't contain the node name, this method will do nothing (just a log).
     * The active node will not change after this method.
     * <p>
     * DependNodes in the new node will be changed to members in the current group, nodes depends on the replaced
     * node in the group will be changed to new node.
     * @param node to be replaced
     */
    public void replaceNode(Node node) {
        Assert.notNull(node, ERR_MSG_NODE_NOT_NULL);
        Node[] oldDependNodes = node.getDependNodes();
        Node[] newDependNodes = new Node[oldDependNodes.length];
        int depIdx = 0;
        for (Map.Entry<String, Node> entry : this.nodes.entrySet()) {
            String key = entry.getKey();
            Node memberNode = entry.getValue();
            // check new node depend nodes
            if (depIdx < oldDependNodes.length && key.equals(oldDependNodes[depIdx].getName())) {
                newDependNodes[depIdx] = memberNode;
                ++depIdx;
            } else {
                /* We use if-else here because two nodes can not depend on each other */
                // check member node dependencies
                Node[] memberDependNodes = memberNode.getDependNodes();
                for (int i = 0; i < memberDependNodes.length; i++) {
                    if (memberDependNodes[i].getName().equals(node.getName())) {
                        memberDependNodes[i] = node;
                        memberNode.setDependNodes(memberDependNodes);
                        break;
                    }
                }
            }
        }
        node.setDependNodes(newDependNodes);
    }

    /**
     * Same as the overloaded method, except for use default activeNodeKey.
     * @see #reorganizeNodes(Node[], String)
     */
    public void reorganizeNodes(Node[] nodes) {
        reorganizeNodes(nodes, null);
    }

    /**
     * Changes node group members, this method may add, remove members,
     * or change order of the list.
     * This method will keep old data, if a node with the same name found.
     * (If you want to use new data, you should create a new NodeGroup and don't use this method)
     * <p>
     * When use old data, the dependencies should be modified to use new dependNodes.
     * Sub node group will also be re-organized recursively.
     * @param nodes to replace current members
     * @param activeNodeKey active node key after re-organization; if null, will retry to use old key, if old node does
     *                      not exist anymore, will use first key
     */
    public void reorganizeNodes(Node[] nodes, String activeNodeKey) {
        Assert.notEmpty(nodes, ERR_MSG_NODE_GROUP_NOT_EMPTY);
        Map<String, Node> existMap = new HashMap<>();
        LinkedHashMap<String, Node> tmpMap = Maps.newLinkedHashMapWithExpectedSize(this.nodes.size());
        for (Node n : nodes) {
            // update dependNodes according to old nodes
            Node[] declaredDepNodes = n.getDependNodes();
            for (int i = 0; i < declaredDepNodes.length; i++) {
                Node existReplacedNode = existMap.get(declaredDepNodes[i].getName());
                if (existReplacedNode != null) {
                    declaredDepNodes[i] = existReplacedNode;
                }
            }
            n.setDependNodes(declaredDepNodes);

            Node existsNode = this.nodes.get(n.getName());
            if (existsNode != null) {
                existsNode.setDependNodes(n.getDependNodes());

                /* If a node group is found, re-organize it also */
                if (existsNode instanceof NodeGroup && n instanceof NodeGroup) {
                    NodeGroup nodeGroup = (NodeGroup) n;
                    ((NodeGroup) existsNode).reorganizeNodes((nodeGroup).getAllNodes().toArray(new Node[0]), nodeGroup.getActiveNodeKey());
                }

                existMap.put(n.getName(), existsNode);
                tmpMap.put(n.getName(), existsNode);
            } else {
                tmpMap.put(n.getName(), n);
            }
        }
        this.nodes.clear();
        this.nodes.putAll(tmpMap);
        if (activeNodeKey != null) {
            Assert.notNull(this.nodes.get(activeNodeKey), ERR_MSG_INVALID_ACTIVE_NODE);
            this.activeNodeKey = activeNodeKey;
        } else {
            this.activeNodeKey = contains(this.activeNodeKey) ? this.activeNodeKey : getFirstNodeKey();
        }
    }


    /**
     * Gets the relative path of the page this group should show
     * @return a node path
     */
    public String getCurrentVisibleNode() {
        String resultNode;
        Node curNode = nodes.get(activeNodeKey);
        if (curNode == null || !curNode.isAvailable()) {
            activeNodeKey = getFirstNodeKey();
            curNode = nodes.get(activeNodeKey);
        }

        if (curNode instanceof NodeGroup) {
            resultNode = activeNodeKey + pathSeparator + ((NodeGroup) curNode).getCurrentVisibleNode();
        } else {
            resultNode = activeNodeKey;
        }
        return resultNode;
    }

    /**
     * Checks the accessibility of a sub node.
     * @param path node path
     * @return a path we should go to, the format may change, a 'a_b_c' input may return 'a_b_a', 'a_a'...
     *   If the result is equal to the parameter, we can access; if not equal, we can't access, and must handle the
     *   return page first.
     */
    public String checkMemberNodeAccessCondition(String path) {
        String result;
        String[] namePart = path.split(pathSeparator, 2);
        if (namePart.length > 1) {
            NodeGroup subGroup = (NodeGroup) getNode(namePart[0]);
            result = subGroup.checkAccessCondition();
            if (subGroup.name.equals(result)) {
                result = namePart[0] + pathSeparator + subGroup.checkMemberNodeAccessCondition(namePart[1]);
            } else {
                result = expandFailNode(result);
            }
        } else {
            result = this.nodes.get(path).checkAccessCondition();
            if (!path.equals(result)) {
                result = expandFailNode(result);
            }
        }
        return result;
    }

    /**
     * Get the first not validated visible node.
     * @param nodeName node name of the group
     * @return the first not validated node; will not point to a node group
     */
    public String expandFailNode(String nodeName) {
        String result = nodeName;
        Node failNode = getNode(nodeName);
        if (failNode instanceof NodeGroup) {
            result = nodeName + this.pathSeparator + ((NodeGroup) failNode).getFirstNotValidatedNodePath();
        }
        return result;
    }

    /**
     * When a node in this group turned to available or invalid, all nodes depend on it should
     * turn to invalid also. Besides, this group should be invalid.
     * @param nodeName name the node turned to available or invalid
     */
    public void invalidationWave(String nodeName) {
        if (!contains(nodeName)) {
            throw new IllegalArgumentException(ERR_MSG_NODE_NAME_NOT_EMPTY);
        }
        // make node group not validated
        needValidation();

        Iterator<Map.Entry<String, Node>> nodeIter = nodes.entrySet().iterator();
        // move iterator to active node
        while (nodeIter.hasNext()) {
            if (activeNodeKey.equals(nodeIter.next().getKey())) {
                break;
            }
        }
        // check if any member depends on the node
        while (nodeIter.hasNext()) {
            Node member = nodeIter.next().getValue();
            if (member.isValidated() && member.dependsOn(nodeName)) {
                member.needValidation();
            }
        }
    }


    public String getActiveNodeKey() {
        return activeNodeKey;
    }

    public void setActiveNodeKey(String activeNodeKey) {
        if (!contains(activeNodeKey)) {
            throw new IllegalArgumentException("This node group does not contain this key!");
        }
        this.activeNodeKey = activeNodeKey;
    }

    /**
     * Set active node key recursively by receiving a path
     * @throws IllegalArgumentException if can't find node/group according to the path
     */
    public void setActiveNodeKeyRecursively(String nodePath) {
        String[] nodePathParts = nodePath.split(pathSeparator, 2);
        if (nodePathParts.length > 1) {
            setActiveNodeKey(nodePathParts[0]);
            Node activeNode = getNode(nodePathParts[0]);
            if (activeNode instanceof NodeGroup) {
                ((NodeGroup) activeNode).setActiveNodeKeyRecursively(nodePathParts[1]);
            } else {
                throw new IllegalArgumentException("The node path is not applicable to this group");
            }
        } else {
            setActiveNodeKey(nodePath);
        }
    }

    /**
     * Get the first met node which is not validated
     * @return node path of the first not validated node; or null if all nodes validated
     */
    public String getFirstNotValidatedNodePath() {
        String path = null;
        for (Node child : this.nodes.values()) {
            if (child.isAvailable() && !child.isValidated()) {
                if (child instanceof NodeGroup) {
                    String subPath = ((NodeGroup) child).getFirstNotValidatedNodePath();
                    path = child.getName() + pathSeparator + subPath;
                } else {
                    path = child.getName();
                }
                break;
            }
        }
        return path;
    }

    public boolean checkIfAllValidated() {
        return getFirstNotValidatedNodePath() == null;
    }
}
