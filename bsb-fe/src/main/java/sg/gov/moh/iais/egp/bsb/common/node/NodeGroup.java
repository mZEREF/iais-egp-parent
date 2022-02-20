package sg.gov.moh.iais.egp.bsb.common.node;

import com.google.common.collect.Maps;
import org.springframework.util.Assert;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * A NodeGroup is a group of Nodes, NodeGroup is a subclass of Node,
 * so a NodeGroup can have other groups as it's member nodes.
 * <p>
 * A NodeGroup can use a path format to retrieve a Node, you must understand the pathSeparator (default is underscore)
 * to use this. For example:
 * <pre>
 *   Node abc = nodeGroup.at("a_b_c");
 * </pre>
 * The path assumes this group contains a member 'a', and 'a' is 'a' NodeGroup also; 'a' contains 'b', 'b' is also
 * a NodeGroup, the destiny Node we want is the member 'c' in the 'b' group.
 *
 * <p>
 * A NodeGroup is not used to show a page, it is not visible. We can get the visible Node of this group with the
 * {@link #getCurrentVisibleNode} method. This method will return a member Node path that is not a group.
 */
public class NodeGroup extends Node {
    protected static final String ERR_MSG_NODE_NOT_NULL = "Node can not be null";
    protected static final String ERR_MSG_NODE_GROUP_NOT_EMPTY = "Nodes can not be empty";
    protected static final String ERR_MSG_INVALID_ACTIVE_NODE = "The node group does not contain the active node key!";
    protected static final String ERR_MSG_INVALID_PATH = "Path must not be empty!";

    /**
     * This map contains members of this group.
     * This map must never be empty.
     * The key is equal to the name of the node.
     */
    private final LinkedHashMap<String, Node> nodes;

    /** Active node of this group; never be null */
    private String activeNodeKey;

    /** Separator used to separate the node path.
     * Attention, the logic of this class depends on string processing,
     * so the name of nodes MUST not contain the separator */
    private final String pathSeparator;


    public NodeGroup(String name, Node[] dependNodes, Map<String, Node> nodes, String pathSeparator) {
        super(name, dependNodes);
        Assert.notEmpty(nodes, "A node group must contains nodes!");
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

    /** Get the amount of the children/members */
    public int count() {
        return this.nodes.size();
    }

    /**
     * Check if this group contains a member with the name
     * @param name of Node
     * @return true if this group contains a Node with specific name
     */
    public boolean contains(String name) {
        return this.nodes.containsKey(name);
    }

    /** Get a copy of the child/member nodes */
    public List<Node> getAllNodes() {
        return new ArrayList<>(this.nodes.values());
    }

    /** Get the Node by the specified key
     * @return a Node (may be a sub type of Node) if find; null if not */
    public Node getNode(String key) {
        return this.nodes.get(key);
    }

    /** Get the Node relative to this node group.
     * <p>
     * For example, the path is 'a_b_c', if this group doesn't contain 'a',
     * or if we find the 'a_b' but it doesn't contain 'c', this method will return null.
     * @return a Node if find; null if not */
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


    /** Get the first node key of the group */
    private String getFirstNodeKey() {
        return nodes.entrySet().iterator().next().getKey();
    }

    /**
     * This method will try to get the next node of sub group if active node is a group.
     * @return the path of the next node; return null if current node is the last node.
     *      return key path is not ensured to point to a visible node, it may be a path point to a node group.
     */
    public String getNextName() {
        Node activeNode = getNode(activeNodeKey);
        if (activeNode == null) {
            throw new IllegalStateException(ERR_MSG_INVALID_ACTIVE_NODE);
        }
        if (activeNode instanceof NodeGroup) {
            String subNextName = ((NodeGroup) activeNode).getNextName();
            /* if it's null, the sub group meets the last node, we need to get the next node of current group */
            if (subNextName != null) {
                return activeNodeKey + pathSeparator + subNextName;
            }
        }
        Iterator<Map.Entry<String, Node>> nodeIter = nodes.entrySet().iterator();
        String key;
        while (nodeIter.hasNext()) {
            key = nodeIter.next().getKey();
            if (activeNodeKey.equals(key)) {
                break;
            }
        }
        if (nodeIter.hasNext()) {
            return nodeIter.next().getKey();
        } else {
            return null;
        }
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
        if (activeNode instanceof NodeGroup) {
            String subPreviousName = ((NodeGroup) activeNode).getPreviousName();
            if (subPreviousName != null) {
                return activeNodeKey + pathSeparator + subPreviousName;
            }
        }
        Iterator<Map.Entry<String, Node>> nodeIter = nodes.entrySet().iterator();
        String prevKey = null;
        String key = null;
        while (nodeIter.hasNext()) {
            prevKey = key;
            key = nodeIter.next().getKey();
            if (activeNodeKey.equals(key)) {
                break;
            }
        }
        return prevKey;
    }



    public void addNode(Node node) {
        this.nodes.put(node.getName(), node);
    }

    public void removeNode(String name) {
        if (this.nodes.size() == 1) {
            throw new IllegalStateException(ERR_MSG_NODE_GROUP_NOT_EMPTY);
        }
        this.nodes.remove(name);
    }

    /**
     * Replace a member with the same node name.
     * If this group doesn't contain the node name, this method will do nothing.
     * The active node will not change after this method
     * @param node to be replaced
     */
    public void replaceNode(Node node) {
        Assert.notNull(node, ERR_MSG_NODE_NOT_NULL);
        String nodeName = node.getName();
        LinkedHashMap<String, Node> tmpMap = Maps.newLinkedHashMapWithExpectedSize(this.nodes.size());
        for (Map.Entry<String, Node> entry : this.nodes.entrySet()) {
            if (nodeName.equals(entry.getKey())) {
                tmpMap.put(entry.getKey(), node);
            } else {
                tmpMap.put(entry.getKey(), entry.getValue());
            }
        }
        this.nodes.clear();
        this.nodes.putAll(tmpMap);
    }

    /**
     * Replace a member has the name to another node.
     * If this group doesn't contain the node name, this method will do nothing.
     * The activeNode of this group won't change or change to the new node name (if the replaced name is the activeNode)
     * @param name of the Node to be replaced
     * @param node will replace the old Node
     */
    public void replaceNode(String name, Node node) {
        Assert.hasLength(name, ERR_MSG_NODE_NAME_NOT_EMPTY);
        Assert.notNull(node, ERR_MSG_NODE_NOT_NULL);
        LinkedHashMap<String, Node> tmpMap = Maps.newLinkedHashMapWithExpectedSize(this.nodes.size());
        for (Map.Entry<String, Node> entry : this.nodes.entrySet()) {
            if (name.equals(entry.getKey())) {
                tmpMap.put(node.getName(), node);
                if (this.activeNodeKey.equals(name)) {
                    this.activeNodeKey = node.getName();
                }
            } else {
                tmpMap.put(entry.getKey(), entry.getValue());
            }
        }
        this.nodes.clear();
        this.nodes.putAll(tmpMap);
    }

    /**
     * Replace nodes with the same name
     * @see #replaceNodes(Map)
     * @param nodes new Node list
     */
    public void replaceNodes(Node[] nodes) {
        Assert.notEmpty(nodes, ERR_MSG_NODE_GROUP_NOT_EMPTY);
        Map<String, Node> newNodeMap = Arrays.stream(nodes).collect(Collectors.toMap(Node::getName, Function.identity()));
        replaceNodes(newNodeMap);
    }

    /**
     * Replace nodes with the same name.
     * If the name in the parameter is not contained by this group, it will be ignored
     * @param nodes new nodes, key is node name, value is the Node
     */
    public void replaceNodes(Map<String, Node> nodes) {
        Assert.notEmpty(nodes, ERR_MSG_NODE_GROUP_NOT_EMPTY);
        LinkedHashMap<String, Node> tmpMap = Maps.newLinkedHashMapWithExpectedSize(this.nodes.size());
        for (Map.Entry<String, Node> entry : this.nodes.entrySet()) {
            Node newNode = nodes.get(entry.getKey());
            if (newNode != null) {
                tmpMap.put(newNode.getName(), newNode);
                if (entry.getKey().equals(this.activeNodeKey)) {
                    this.activeNodeKey = newNode.getName();
                }
            } else {
                tmpMap.put(entry.getKey(), entry.getValue());
            }
        }
        this.nodes.clear();
        this.nodes.putAll(tmpMap);
    }

    /**
     * Replace members in this group to specific list.
     * Keep the existing nodes if the given node contain the node with same name.
     * This method totally change the node list but keep the old data.
     * @param nodes to replace current members
     */
    public void reorganizeNodes(Node[] nodes) {
        Assert.notEmpty(nodes, ERR_MSG_NODE_GROUP_NOT_EMPTY);
        LinkedHashMap<String, Node> tmpMap = Maps.newLinkedHashMapWithExpectedSize(nodes.length);
        for (Node n : nodes) {
            Node existsNode = this.nodes.get(n.getName());
            if (existsNode != null) {
                tmpMap.put(n.getName(), existsNode);
            } else {
                tmpMap.put(n.getName(), n);
            }
        }
        this.nodes.clear();
        this.nodes.putAll(tmpMap);
        this.activeNodeKey = getFirstNodeKey();
    }


    /**
     * Get the relative path of the page this group should show
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
     * Check the accessibility of a sub node.
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


    public String getActiveNodeKey() {
        return activeNodeKey;
    }

    public void setActiveNodeKey(String activeNodeKey) {
        if (!nodes.containsKey(activeNodeKey)) {
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
            if (child instanceof NodeGroup) {
                String subPath = ((NodeGroup) child).getFirstNotValidatedNodePath();
                if (subPath != null) {
                    path = child.getName() + pathSeparator + subPath;
                }
            } else {
                if (!child.isValidated()) {
                    path = child.getName();
                }
            }
            // already get the result, stop checking
            if (path != null) {
                break;
            }
        }
        return path;
    }

    public boolean checkIfAllValidated() {
        return getFirstNotValidatedNodePath() == null;
    }
}
