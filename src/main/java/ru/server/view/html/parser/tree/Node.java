package ru.server.view.html.parser.tree;

import java.util.*;

public abstract class Node {

    protected String value;
    protected Map<String, String> attributes;
    protected List<Node> children;

    private final Set<String> nonClosableTags = Set.of("meta", "br", "!DOCTYPE");

    public Node() {
        this.children = new ArrayList<>();
        this.attributes = new HashMap<>();
        this.value = "";
    }

    public abstract String toHtml();

    public void setValue(String value) {
        this.value = value;
    }

    public void addAttr(String key, String value) {
        attributes.put(key, value);
    }

    public void addChild(Node node) {
        children.add(node);
    }

    public boolean isAssigned() {
        return !value.isEmpty();
    }

    public boolean isNonClosableTag() {
        return nonClosableTags.contains(value);
    }

    public String getValue() {
        return value;
    }

    public String getAttr(String key) {
        return attributes.get(key);
    }

    public List<Node> getChildren() {
        return children;
    }
}
